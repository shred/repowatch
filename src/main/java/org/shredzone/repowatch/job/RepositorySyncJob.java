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

import org.shredzone.repowatch.service.SyncService;
import org.shredzone.repowatch.service.SynchronizerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

/**
 * A scheduler job for synchronizing the repository database.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 317 $
 */
public class RepositorySyncJob {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private SyncService syncService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    private String authUser, authPwd;

    /**
     * Sets the user name for cronjob authentication.
     * 
     * @param authUser
     */
    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    /**
     * Sets the password for cronjob authentication.
     * 
     * @param authPwd
     */
    public void setAuthPwd(String authPwd) {
        this.authPwd = authPwd;
    }

    /**
     * Perform the synchronization process.
     */
    public void doSynchronization() {
        logger.info("Synchronization job started");
        
        Authentication auth = new UsernamePasswordAuthenticationToken(authUser, authPwd);
        auth = authenticationManager.authenticate(auth);
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        try {
            syncService.syncAllRepositories();
            logger.info("Synchronization job completed");
        } catch (SynchronizerException ex) {
            logger.log(Level.WARNING, "Synchronization job failed", ex);
        } finally {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }
    
}
