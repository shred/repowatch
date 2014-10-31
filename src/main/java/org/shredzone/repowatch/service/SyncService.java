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

package org.shredzone.repowatch.service;

import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.sync.SynchronizerException;

/**
 * A service providing everything that is required for keeping the
 * database in sync with the original repository database.
 *
 * @author Richard "Shred" Körber
 */
public interface SyncService {

    /**
     * Synchronizes only a single repository.
     *
     * @param repo  {@link Repository} to synchronize.
     * @throws  SynchronizerException   Synchronization failed
     */
    public void syncRepository(Repository repo) throws SynchronizerException;

}
