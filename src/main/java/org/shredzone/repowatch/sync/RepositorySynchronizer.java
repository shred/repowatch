/**
 * repowatch - A yum repository watcher
 *
 * Copyright (C) 2008 Richard "Shred" Körber
 *   http://repowatch.shredzone.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.shredzone.repowatch.sync;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.shredzone.repowatch.model.Blacklist;
import org.shredzone.repowatch.model.Change;
import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.model.Version;
import org.shredzone.repowatch.repository.BlacklistDAO;
import org.shredzone.repowatch.repository.ChangeDAO;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.repository.VersionDAO;
import org.shredzone.repowatch.service.SynchronizerException;

/**
 * This is the heart of the synchronizer. It synchronizes the repowatch
 * database with the repository's databases.
 * <p>
 * For Spring there is a {@link RepositorySynchronizerFactory} which creates
 * new and ready to use RepositorySynchronizer objects.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 317 $
 */
public class RepositorySynchronizer {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final Repository repository;
    private final BlacklistDAO blacklistDao;
    private final PackageDAO packageDao;
    private final VersionDAO versionDao;
    private final ChangeDAO changeDao;
    
    private URL baseUrl;
    private RepoMdParser repomd;
    private Date now;
    
    /**
     * Creates a new synchronizer. The synchronization does not take place
     * yet.
     * 
     * @param repository    {@link Repository} to synchronize with
     * @param packageDao    {@link PackageDAO} to be used
     * @param versionDao    {@link VersionDAO} to be used
     * @param changeDao     {@link ChangeDAO} to be used
     * @param blacklistDao  {@link BlacklistDAO} to be used
     */
    public RepositorySynchronizer(Repository repository, PackageDAO packageDao,
            VersionDAO versionDao, ChangeDAO changeDao,
            BlacklistDAO blacklistDao) {
        this.repository = repository;
        this.packageDao = packageDao;
        this.versionDao = versionDao;
        this.changeDao = changeDao;
        this.blacklistDao = blacklistDao;
    }
    
    /**
     * Starts the synchronization process.
     * 
     * @throws SynchronizerException  Synchronization failed for various reasons
     */
    public void doSynchronize() throws SynchronizerException {
        now = new Date();
        
        try {
            this.baseUrl = new URL(repository.getBaseUrl());
        } catch (MalformedURLException ex) {
            throw new SynchronizerException("Invalid base url", ex);
        }

        readRepoMd();
        readPrimary();
    }
    
    /**
     * Reads the repomd.xml file and fills the {@link RepoMdParser} with
     * {@link DatabaseLocation} entities.
     * 
     * @throws SynchronizerException
     */
    private void readRepoMd() throws SynchronizerException {
        logger.fine("Reading repomd.xml");
        try {
            repomd = new RepoMdParser(baseUrl);
            repomd.parse();
        } catch (IOException ex) {
            throw new SynchronizerException("Could not read repomd.xml", ex);
        }
    }
    
    /**
     * Reads and processes the primary.xml database. This could take some
     * time and some amount of heap memory.
     * 
     * @throws SynchronizerException
     */
    private void readPrimary() throws SynchronizerException {
        logger.fine("Reading primary.xml");
        boolean firstTime = false;
        
        DatabaseLocation location = repomd.getDatabaseLocation("primary");
        if (location == null) {
            throw new SynchronizerException("primary database not defined!");
        }
        
        //--- Reading for the first time? ---
        if (repository.getFirstScanned() == null) {
            repository.setFirstScanned(now);
            firstTime = true;
        }
        
        //--- Update available? ---
        long localModifiedSince = repository.getLastModified();
        long remoteModifiedSince = location.getTimestamp();

        if (localModifiedSince != 0 && remoteModifiedSince != 0
                && remoteModifiedSince <= localModifiedSince ) {
            return;
        }

        repository.setLastModified(remoteModifiedSince);
        
        //--- Create a Version cache ---
        Map<String,Version> versionCache = new HashMap<String,Version>();
        for (Version version : versionDao.findAllVersions(repository)) {
            versionCache.put(version.getPackage().getName(), version);
        }
        
        //--- Get the Blacklist ---
        Set<String> blacklist = new HashSet<String>();
        for (Blacklist bl : blacklistDao.findAllBlacklists()) {
            blacklist.add(bl.getName());
        }

        //--- Iterate through the elements ---
        logger.info("Repository " + repository.toString() + " is due for an update");
        int versionCounter = 0;
        int changeCounter = 0;
        int deleteCounter = 0;
        
        PrimaryParser parser = new PrimaryParser(repository, location);
        try {
            parser.parse();
            
            for (Version version : parser) {
                if (blacklist.contains(version.getPackage().getName())) {
                    continue;
                }

                versionCounter++;
                
                Date fileDate = version.getFileDate();
                if (fileDate == null || fileDate.after(now)) {
                    fileDate = now;
                }
                
                if (! firstTime) {
                    version.setFirstSeen(fileDate);
                }
                version.setLastSeen(now);

                Change change = syncVersion(version, versionCache);
                if (change != null && !firstTime) {
                    changeCounter++;
                    change.setTimestamp(fileDate);
                    changeDao.insert(change);
                }
            }
        } finally {
            parser.discard();
        }
        
        //--- Create "removed" entries ---
        for (Version version : versionDao.findLastSeenBefore(repository, now)) {
            Change change = new Change();
            change.setTimestamp(now);
            change.setChange(Change.Type.REMOVED);
            change.setRepository(repository);
            change.setPackage(version.getPackage());
            change.setEpoch(version.getEpoch());
            change.setVer(version.getVer());
            change.setRel(version.getRel());
            changeDao.insert(change);
            version.setDeleted(true);
            deleteCounter++;
        }
        
        //--- Successfully scanned ---
        repository.setLastScanned(now);
        logger.info("Repository " + repository.toString() +
                " successfully updated (" +
        		versionCounter + " seen, " +
        		changeCounter + " changed, " +
        		deleteCounter + " deleted)");
    }
    
    /**
     * Synchronizes a {@link Version} entity with the database.
     * 
     * @param version   Entity to sync with. Must be transient.
     * @param versionCache   Cache of all Versions of a repository, indexed by
     *      their Package names.
     * @return  Change entity, or null if the version was unchanged.
     */
    private Change syncVersion(Version version, Map<String,Version> versionCache) {
        //--- Create a Change entity ---
        Change change = new Change();
        change.setRepository(version.getRepository());
        change.setEpoch(version.getEpoch());
        change.setVer(version.getVer());
        change.setRel(version.getRel());
        
        //--- Find the Verison in Version cache ---
        Version dbversion = versionCache.get(version.getPackage().getName());

        if (dbversion != null) {
            // The version (and the package) does already exist in database.
            // This might be an update.
            
            dbversion.setLastSeen(version.getLastSeen());
            if (dbversion.getFileDate() == null
                    || version.getFileDate() == null
                    || version.getFileDate().after(dbversion.getFileDate())) {
                
                dbversion.setEpoch(version.getEpoch());
                dbversion.setVer(version.getVer());
                dbversion.setRel(version.getRel());
                dbversion.setFileLocation(version.getFileLocation());
                dbversion.setFileDate(version.getFileDate());

                if (version.getFileDate() != null) {
                    change.setChange(Change.Type.UPDATED);
                    change.setPackage(dbversion.getPackage());
                    return change;
                } else {
                    return null;
                }
            }
            
        } else {
            // Version is not yet in the database. We need to add it.
            // The package might be in the database, though.
            Package newdbpack = packageDao.findPackage(
                    version.getPackage().getDomain(),
                    version.getPackage().getName()
            );
            
            if (newdbpack != null) {
                // There is a package, so it is the first time for this
                // Version to appear in this repository.
                
                newdbpack.setSummary(version.getPackage().getSummary());
                newdbpack.setDescription(version.getPackage().getDescription());
                newdbpack.setHomeUrl(version.getPackage().getHomeUrl());
                newdbpack.setPackGroup(version.getPackage().getPackGroup());
                
                version.setPackage(newdbpack);
                versionDao.insert(version);
                versionCache.put(version.getPackage().getName(), version);
                
                change.setChange(Change.Type.ADDED);
                change.setPackage(newdbpack);
                return change;
                
            } else {
                // Package was not found, which means that also the Version
                // entity is not persisted yet. Persist the Version entity
                // as it is and return that the version was added.
                packageDao.insert(version.getPackage());
                versionDao.insert(version);
                versionCache.put(version.getPackage().getName(), version);

                change.setChange(Change.Type.ADDED);
                change.setPackage(version.getPackage());
                return change;
                
            }
        }
        
        // Nothing has changed
        return null;
    }
    
}
