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

package org.shredzone.repowatch.job;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.shredzone.repowatch.service.SyncService;
import org.shredzone.repowatch.sync.SynchronizerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

/**
 * A scheduler job for synchronizing the repository database.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 323 $
 */
public class RepositorySyncJob {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private SyncService syncService;

    @Autowired
    private RepositoryDAO repositoryDao;

    /**
     * Perform the synchronization process.
     */
    public void doSynchronization() {
        logger.info("Synchronization job started");
        
        Authentication auth = new CronAuthenticationToken(this);
        SecurityContextHolder.getContext().setAuthentication(auth);
        try {

            for (Repository repo : repositoryDao.findAllRepositories()) {
                try {
                    syncService.syncRepository(repo);
                } catch (SynchronizerException ex) {
                    logger.log(Level.WARNING, "Synchronization job failed", ex);
                }
            }
        
            logger.info("Synchronization job completed");
        
        } finally {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }
    
}
