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
 * $Id: BaseDAO.java 222 2009-01-05 23:40:44Z shred $
 */

package org.shredzone.repowatch.repository;

import org.acegisecurity.annotation.Secured;
import org.shredzone.repowatch.model.BaseModel;

/**
 * Base class of all DAOs.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 222 $
 */
@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
public interface BaseDAO<T extends BaseModel> {
	
    /**
     * Inserts a new entity.
     * 
     * @param data     Entity to insert.
     */
    @Secured("ROLE_ADMIN")
	public void insert(T data);
	
	/**
	 * Fetches the entity with the given ID.
	 * 
	 * @param id       Entity ID
	 * @return  Entity
	 */
	public T fetch(long id);

	/**
	 * Deletes an entity.
	 * 
	 * @param data     Entity to be deleted.
	 */
	@Secured("ROLE_ADMIN")
	public void delete(T data);
	
}