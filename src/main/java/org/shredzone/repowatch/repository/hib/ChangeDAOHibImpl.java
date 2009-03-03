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
 * $Id: ChangeDAOHibImpl.java 273 2009-03-03 00:03:00Z shred $
 */

package org.shredzone.repowatch.repository.hib;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.shredzone.repowatch.model.Change;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.ChangeDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Hibernate implementation of {@link ChangeDAO}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 273 $
 */
@org.springframework.stereotype.Repository      // class name collision!
@Transactional
public class ChangeDAOHibImpl extends BaseDAOHibImpl<Change> implements ChangeDAO {
    
    @Transactional(readOnly = true)
    public Change fetch(long id) {
        return (Change) getCurrentSession().get(Change.class, id);
    }

    @Transactional(readOnly = true)
    public long countChanges(Repository repo) {
        return countChanges(repo, true);
    }

    @Transactional(readOnly = true)
    public long countChanges(Repository repo, boolean updates) {
        Query q = getCurrentSession().createQuery(
                        "SELECT COUNT(*) FROM Change" +
                		" WHERE repository=:repo" +
                        " AND (:updates=true OR change<>:typeupdated)")
                .setParameter("repo", repo)
                .setParameter("updates", updates)
                .setParameter("typeupdated", Change.Type.UPDATED);
        
        return ((Long) q.iterate().next()).longValue();
    }

    @Transactional(readOnly = true)
    public List<Change> findAllChanges(Repository repo) {
        return findAllChanges(repo, 0, -1);
    }

    @Transactional(readOnly = true)
    public List<Change> findAllChanges(Repository repo, int start, int limit) {
        return findAllChanges(repo, true, start, limit);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Change> findAllChanges(Repository repo, boolean updates, int start, int limit) {
        Query q = getCurrentSession().createQuery(
                        "FROM Change AS c" +
                        " WHERE c.repository=:repository" +
                        " AND (:updates=true OR change<>:typeupdated)" +
                        " ORDER BY c.timestamp DESC, c.change, c.package.name")
                .setParameter("repository", repo)
                .setParameter("updates", updates)
                .setParameter("typeupdated", Change.Type.UPDATED);

        if (limit >= 0) {
            q.setFirstResult(start).setMaxResults(limit);
        }

        return q.list();
    }

    @Transactional(readOnly = true)
    public List<Change> findAllChangesUntil(Repository repo, Date limit, int maxrows) {
        return findAllChangesUntil(repo, true, limit, maxrows);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Change> findAllChangesUntil(Repository repo, boolean updates, Date limit, int maxrows) {
        Query q = getCurrentSession().createQuery(
                        "FROM Change AS c" +
                        " WHERE c.repository=:repository" +
                        " AND c.timestamp>=:limit" +
                        " AND (:updates=true OR change<>:typeupdated)" +
                        " ORDER BY c.timestamp DESC, c.change, c.package.name")
                .setParameter("repository", repo)
                .setParameter("limit", limit)
                .setParameter("updates", updates)
                .setParameter("typeupdated", Change.Type.UPDATED);

        if (maxrows >= 0) {
            q.setMaxResults(maxrows);
        }

        return q.list();
    }

    public void deleteAllChangesForRepository(Repository repository) {
        getCurrentSession().createQuery(
                "DELETE FROM Change AS c WHERE c.repository=:repository")
                .setParameter("repository", repository);
    }

}
