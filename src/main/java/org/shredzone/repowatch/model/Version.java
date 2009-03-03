/*
 * Repowatch -- A repository watcher
 *   (C) 2008 Richard "Shred" Körber
 *   http://repowatch.shredzone.org/
 *-----------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * $Id: Version.java 272 2009-03-03 00:02:39Z shred $
 */

package org.shredzone.repowatch.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents a version of a package.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 272 $
 */
@Entity
public class Version extends BaseModel {
    private static final long serialVersionUID = 4538841339588675524L;
    
    private Repository repo;
    private Package pack;
    private String epoch;
    private String ver;
    private String rel;
    private Date firstSeen;
    private Date lastSeen;
    private Date fileDate;
    private String fileLocation;
    private boolean deleted;
    
    /**
     * {@link Repository} this version belongs to.
     */
    @ManyToOne
    @Basic(fetch = FetchType.LAZY, optional = false)
    public Repository getRepository()          { return repo; }
    public void setRepository(Repository repo) { this.repo = repo; }

    /**
     * {@link Package} this version belongs to.
     */
    @ManyToOne
    @Basic(fetch = FetchType.LAZY, optional = false)
    public Package getPackage()             { return pack; }
    public void setPackage(Package pack)    { this.pack = pack; }

    /**
     * Current epoch of that package.
     */
    public String getEpoch()                { return epoch; }
    public void setEpoch(String epoch)      { this.epoch = epoch; }

    /**
     * Current version of that package.
     */
    public String getVer()                  { return ver; }
    public void setVer(String ver)          { this.ver = ver; }

    /**
     * Current release of that package.
     */
    public String getRel()                  { return rel; }
    public void setRel(String rel)          { this.rel = rel; }

    /**
     * First time the package was seen in this repository.
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFirstSeen()              { return firstSeen; }
    public void setFirstSeen(Date firstSeen) { this.firstSeen = firstSeen; }

    /**
     * Last time the package was seen in this repository.
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastSeen()               { return lastSeen; }
    public void setLastSeen(Date lastSeen)  { this.lastSeen = lastSeen; }

    /**
     * Creation timestamp of the package file.
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFileDate()               { return fileDate; }
    public void setFileDate(Date fileDate)  { this.fileDate = fileDate; }

    /**
     * File location of the package file, relative to the repository base URL.
     */
    public String getFileLocation()         { return fileLocation; }
    public void setFileLocation(String fileLocation) {  this.fileLocation = fileLocation; }
    
    /**
     * Has the package been remove from the repository.
     */
    @Basic(optional=false)
    public boolean isDeleted()              { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    
}
