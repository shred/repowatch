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
 * $Id: RepositoryDAO.java 222 2009-01-05 23:40:44Z shred $
 */
package org.shredzone.repowatch.repository;

import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;

/**
 * Gives access to the repository part of the database.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 222 $
 */
@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
public interface RepositoryDAO extends BaseDAO<Repository> {
    
    /**
     * Finds a repository in the {@link Domain} with the given name and
     * architecture.
     * 
     * @param domain        {@link Domain} to search for the repository.
     * @param name          Name of the repository (e.g. "updates")
     * @param architecture  Architecture string (e.g. "i386")
     * @return  The {@link Repository} that was found, or <code>null</code>
     *      if there was none.
     */
    public Repository findRepository(Domain domain, String name, String architecture);
    
    /**
     * Returns a list of all repositories.
     * 
     * @return  List of all {@link Repository} entities.
     */
    public List<Repository> findAllRepositories();

}