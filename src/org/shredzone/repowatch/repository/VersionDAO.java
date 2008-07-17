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
 * $Id: VersionDAO.java 175 2008-07-17 23:14:30Z shred $
 */

package org.shredzone.repowatch.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.model.Version;

/**
 * Gives access to the repository part of the database.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 175 $
 */
public interface VersionDAO extends BaseDAO<Version> {
    
    public List<Version> findAllVersions(Repository repo);

    public List<Version> findAllVersions(Repository repo, int start, int limit);
    
    public List<Version> findAllVersions(Package pack);

    public Map<String,Version> findAllVersionsAsMap(Repository repo);

    public List<Version> findAllVersionsForName(String name);

    public List<Version> findAllVersionsExcept(String name, Package pack);
    
    public List<Version> findDeleted(Repository repo, Date now);
    
}
