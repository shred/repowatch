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

package org.shredzone.repowatch.repository;

import java.util.List;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;
import org.springframework.security.annotation.Secured;

/**
 * Gives access to the repository part of the database.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 317 $
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

    /**
     * Returns a list of all repositories for the given {@link Domain}.
     *
     * @param domain        {@link Domain} to find all {@link Repository} for
     * @return  List of all {@link Repository} entities for that {@link Domain}.
     */
    public List<Repository> findRepositories(Domain domain);

}
