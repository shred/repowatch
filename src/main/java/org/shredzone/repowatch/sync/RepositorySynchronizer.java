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

import org.shredzone.repowatch.model.Repository;

/**
 * This is the heart of the synchronizer. It synchronizes the repowatch
 * database with the repository's databases.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 323 $
 */
public interface RepositorySynchronizer {

    /**
     * Sets the {@link Repository} to be synchronized.
     */    
    public void setRepository(Repository repository);
    
    /**
     * Starts the synchronization process.
     * 
     * @throws SynchronizerException  Synchronization failed for various reasons
     */
    public void doSynchronize() throws SynchronizerException;
    
}
