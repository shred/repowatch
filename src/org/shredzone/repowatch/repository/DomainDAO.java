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
 * $Id: DomainDAO.java 181 2008-07-22 11:35:11Z shred $
 */

package org.shredzone.repowatch.repository;

import java.util.List;

import org.shredzone.repowatch.model.Domain;

/**
 * Gives access to the domain management.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 181 $
 */
public interface DomainDAO extends BaseDAO<Domain> {
 
    /**
     * Returns a list of all domains registered with the database.
     * 
     * @return  List of all {@link Domain} entities.
     */
    public List<Domain> findAllDomains();

    /**
     * Finds a domain with the given name and release string.
     * 
     * @param name      Name of the domain
     * @param release   Release string
     * @return  {@link Domain} with that name and release, or <code>null</code>
     *          if there is none.
     */
    public Domain findDomain(String name, String release);
    
}