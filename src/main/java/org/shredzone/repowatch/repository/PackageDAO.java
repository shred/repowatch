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
 * $Id: PackageDAO.java 273 2009-03-03 00:03:00Z shred $
 */

package org.shredzone.repowatch.repository;

import java.util.List;
import java.util.SortedMap;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Repository;
import org.springframework.security.annotation.Secured;

/**
 * Gives access to the package management.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 273 $
 */
@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
public interface PackageDAO extends BaseDAO<Package> {
    
    /**
     * Counts all packages that belong to the given {@link Repository}.
     * 
     * @param repo      A {@link Repository}
     * @return  Number of packages belonging to the repository.
     */
    public long countPackages(Repository repo);

    /**
     * Counts all packages known.
     * 
     * @return  Number of all packages.
     */
    public long countAllPackages();

    /**
     * Finds a package with the given name, in the given {@link Domain}.
     * 
     * @param domain    {@link Domain} to find the package in.
     * @param name      Name of the package.
     * @return  {@link Package} matching the name, or <code>null</code> if
     *          there is none.
     */
    public Package findPackage(Domain domain, String name);
    
    /**
     * Finds the latest version of a package with the given name.
     * 
     * @param name      Name of the package
     * @return  Latest {@link Package} matching the name, or <code>null</code>
     *          if there is no package with that name at all.
     */
    public Package findLatestPackage(String name);

    /**
     * Finds all packages for a {@link Domain}.
     * 
     * @param domain    A {@link Domain}
     * @return  A list of all {@link Package}s belonging to that {@link Domain}.
     */
    public List<Package> findAllPackagesForDomain(Domain domain);

    /**
     * Finds all packages for a {@link Domain}, within certain limits.
     * 
     * @param domain    A {@link Domain}
     * @param start     The first entity to start with
     * @param limit     The maximum number of returned entities
     * @return  A list of all {@link Package}s belonging to that {@link Domain}.
     */
    public List<Package> findAllPackages(Domain domain, int start, int limit);
    
    /**
     * Finds all packages for a {@link Domain}, within certain limits. Returns
     * the package name along with a package description.
     * 
     * @param start     The first entity to start with
     * @param limit     The maximum number of returned entities
     * @return  A sorted map of all {@link Package} names belonging to that
     *      {@link Domain}. The map's value contains a description of the
     *      package.
     */
    public SortedMap<String,String> findAllPackages(int start, int limit);

    /**
     * Counts the number of returned packages matching the given search.
     * 
     * @param data      {@link SearchDTO} with the search parameters
     * @return  Number of packages that were found.
     */
    public long countSearchResult(SearchDTO data);

    /**
     * Returns the packages matching the given search.
     * 
     * @param data      {@link SearchDTO} with the search parameters
     * @param start     The first entity to start with
     * @param limit     The maximum number of returned entities
     * @return  A list of all {@link Package}s matching the search.
     */
    public List<Package> findSearchResult(SearchDTO data, int start, int limit);
    
    /**
     * Deletes all packages refering to the given domain.
     * 
     * @param domain    {@link Domain} to delete all packages of.
     */
    @Secured("ROLE_ADMIN")
    public void deleteAllPackagesForDomain(Domain domain);

}