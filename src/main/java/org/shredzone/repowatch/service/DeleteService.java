/*
 * Repowatch -- A repository watcher
 *   (C) 2009 Richard "Shred" Körber
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
 * $Id: DeleteService.java 274 2009-03-03 00:04:01Z shred $
 */

package org.shredzone.repowatch.service;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;

/**
 * A service for deleting repositories and entire domains. The service takes care for
 * recursively deleting all dependent entities.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 274 $
 */
public interface DeleteService {

    /**
     * Deletes a {@link Repository}.
     * 
     * @param repository    {@link Repository} to be deleted.
     */
    public void deleteRepository(Repository repository);
    
    /**
     * Deletes a {@link Domain}.
     * 
     * @param domain        {@link Domain} to be deleted.
     */
    public void deleteDomain(Domain domain);
    
}
