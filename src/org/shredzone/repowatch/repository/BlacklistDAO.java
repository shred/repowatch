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
 * $Id: DomainDAO.java 205 2008-10-07 22:51:07Z shred $
 */

package org.shredzone.repowatch.repository;

import java.util.Set;

import org.shredzone.repowatch.model.Blacklist;
import org.springframework.security.annotation.Secured;

/**
 * Gives access to the domain management.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 205 $
 */
@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
public interface BlacklistDAO extends BaseDAO<Blacklist> {
 
    /**
     * Returns a set of all blacklists.
     * 
     * @return  Set of all {@link Blacklist} entities.
     */
    public Set<Blacklist> findAllBlacklists();

}

