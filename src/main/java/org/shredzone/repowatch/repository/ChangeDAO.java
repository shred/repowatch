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
 * $Id: ChangeDAO.java 226 2009-01-06 20:33:19Z shred $
 */

package org.shredzone.repowatch.repository;

import java.util.Date;
import java.util.List;

import org.shredzone.repowatch.model.Change;
import org.shredzone.repowatch.model.Repository;
import org.springframework.security.annotation.Secured;

/**
 * Gives access to the changelog part of the database.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 226 $
 */
@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
public interface ChangeDAO extends BaseDAO<Change> {
	
    /**
     * Counts all changes logged for a {@link Repository}. Updates are counted.
     * 
     * @param repo
     *            Repository to count the changes of.
     * @return Number of changes
     */
    public long countChanges(Repository repo);
    
    /**
     * Counts all changes logged for a {@link Repository}. Only additions and
     * removals are counted unless the updates option is set.
     * 
     * @param repo
     *            Repository to count the changes of.
     * @param updates
     *            Also count Updates
     * @return Number of changes 
     */
    public long countChanges(Repository repo, boolean updates);
    
    /**
     * Finds all changes logged for a {@link Repository}.
     * 
     * @param repo
     *            Repository to find changes for.
     * @return List of {@link Change} objects containing all changes.
     */
    public List<Change> findAllChanges(Repository repo);
    
    /**
     * Finds all changes logged for a {@link Repository}. Only the given
     * range is returned. Updates are included.
     * 
     * @param repo
     *            Repository to find changes for.
     * @param start
     *            First record to be returned
     * @param limit
     *            Maximum number of records to be returned. A negative
     *            value returns all records.
     * @return List of {@link Change} objects containing all changes.
     */
    public List<Change> findAllChanges(Repository repo, int start, int limit);
    
    /**
     * Finds all changes logged for a {@link Repository}. Only the given
     * range is returned. Only additions and removals are returned unless
     * the updates option is set.
     * 
     * @param repo
     *            Repository to find changes for.
     * @param updates
     *            Also show Updates
     * @param start
     *            First record to be returned
     * @param limit
     *            Maximum number of records to be returned. A negative
     *            value returns all records.
     * @return List of {@link Change} objects containing all changes.
     */
    public List<Change> findAllChanges(Repository repo, boolean updates, int start, int limit);
    
    /**
     * Finds all changes logged since a certain date. No more than a maximum
     * number of rows will be returned. Updates are included.
     * 
     * @param repo
     *            Repository to find changes for.
     * @param limit
     *            First date (inclusive) to return records from.
     * @param maxrows
     *            Maximum number of records to be returned. A negative
     *            value returns all records.
     * @return List of {@link Change} objects containing all changes.
     */
    public List<Change> findAllChangesUntil(Repository repo, Date limit, int maxrows);
    
    /**
     * Finds all changes logged since a certain date. No more than a maximum
     * number of rows will be returned. Only additions and removals are returned
     * unless the updates option is set.
     * 
     * @param repo
     *            Repository to find changes for.
     * @param updates
     *            Also show Updates
     * @param limit
     *            First date (inclusive) to return records from.
     * @param maxrows
     *            Maximum number of records to be returned. A negative
     *            value returns all records.
     * @return List of {@link Change} objects containing all changes.
     */
    public List<Change> findAllChangesUntil(Repository repo, boolean updates, Date limit, int maxrows);

}
