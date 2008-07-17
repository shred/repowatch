/* 
 * Repowatch -- A repository watcher
 *   (C) 2007 Richard "Shred" Körber
 *   http://www.shredzone.net/go/repowatch
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
 * $Id: BaseDAO.java 175 2008-07-17 23:14:30Z shred $
 */

package org.shredzone.repowatch.repository;

import org.shredzone.repowatch.model.BaseModel;

/**
 * Base class of all DAOs.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 175 $
 */
public interface BaseDAO<T extends BaseModel> {
	
	public void create(T data);
	
	public T fetch(long id);

	public void delete(T data);
	

}
