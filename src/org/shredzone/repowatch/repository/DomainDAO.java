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
 * $Id: DomainDAO.java 175 2008-07-17 23:14:30Z shred $
 */

package org.shredzone.repowatch.repository;

import java.util.List;

import org.shredzone.repowatch.model.Domain;

/**
 * Gives access to the repository part of the database.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 175 $
 */
public interface DomainDAO extends BaseDAO<Domain> {
 
    public List<Domain> findAllDomains();
    
    public Domain findDomain(String name, String release);
    
}