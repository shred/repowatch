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

import java.util.Date;
import java.util.List;

import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.model.Version;
import org.springframework.security.annotation.Secured;

/**
 * Gives access to the repository part of the database.
 *
 * @author Richard "Shred" Körber
 */
@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
public interface VersionDAO extends BaseDAO<Version> {

    /**
     * Returns all versions of a {@link Repository}.
     *
     * @param repo      {@link Repository}
     * @return  A list of all {@link Version} entities.
     */
    public List<Version> findAllVersions(Repository repo);

    /**
     * Returns all versions of a {@link Repository}.
     *
     * @param repo      {@link Repository}
     * @param start     First index to be returned
     * @param limit     Maximum number of entities to be returned
     * @return  A list of all {@link Version} entities.
     */
    public List<Version> findAllVersions(Repository repo, int start, int limit);

    /**
     * Returns all versions for a {@link Package}.
     *
     * @param pack      {@link Package}
     * @return  A list of all {@link Version} entities.
     */
    public List<Version> findAllVersions(Package pack);

    /**
     * Finds all versions for a given package name.
     *
     * @param name      Package name
     * @return  A list of all {@link Version} entities.
     */
    public List<Version> findAllVersionsForName(String name);

    /**
     * Finds all versions for a given package name, but keeps out the given
     * package.
     *
     * @param name      Package name
     * @param pack      {@link Package} to be ignored
     * @return  A list of all {@link Version} entities.
     */
    public List<Version> findAllVersionsExcept(String name, Package pack);

    /**
     * Finds all versions for a given {@link Repository} which are not
     * deleted and were last seen before the given date.
     *
     * @param repo      {@link Repository}
     * @param now       Date limit
     * @return  A list of all {@link Version} entities.
     */
    public List<Version> findLastSeenBefore(Repository repo, Date now);

    /**
     * Deletes all versions refering to the given repository.
     *
     * @param repo      {@link Repository} to delete all versions of.
     */
    @Secured("ROLE_ADMIN")
    public void deleteAllVersionsForRepository(Repository repo);

}
