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
 * $Id: VersionDAOHibImpl.java 269 2009-02-25 23:05:17Z shred $
 */

package org.shredzone.repowatch.repository.hib;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.model.Version;
import org.shredzone.repowatch.repository.VersionDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Hibernate implementation of {@link VersionDAO}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 269 $
 */
@org.springframework.stereotype.Repository      // dang, a name collision
@Transactional
public class VersionDAOHibImpl extends BaseDAOHibImpl<Version> implements VersionDAO {

    @Transactional(readOnly = true)
    public Version fetch(long id) {
        return (Version) getCurrentSession().get(Version.class, id);
    }
    
    @Transactional(readOnly = true)
    public List<Version> findAllVersions(Repository repo) {
        return findAllVersions(repo, 0, -1);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Version> findAllVersions(Repository repo, int start, int limit) {
        Query q = getCurrentSession().createQuery(
                        "FROM Version AS v" +
                        " WHERE v.repository=:repository AND v.deleted=false" +
                        " ORDER BY v.package.name")
                .setParameter("repository", repo);
        
        if (limit >= 0) {
            q.setFirstResult(start).setMaxResults(limit);
        }
        
        return q.list();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Version> findAllVersions(Package pack) {
        Query q = getCurrentSession().createQuery(
                        "FROM Version AS v" +
                        " WHERE v.package=:package AND v.deleted=false" +
                        " ORDER BY v.repository.name, v.repository.architecture")
                .setParameter("package", pack);

        return q.list();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Version> findAllVersionsForName(String name) {
        Query q = getCurrentSession().createQuery(
                        "FROM Version AS v" +
                        " WHERE v.package.name=:name AND v.deleted=false" +
                        " ORDER BY v.package.domain.release," +
                        " v.package.domain.name, v.repository.name," +
                        " v.repository.architecture")
                .setParameter("name", name);

        return q.list();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Version> findAllVersionsExcept(String name, Package pack) {
        Query q = getCurrentSession().createQuery(
                        "FROM Version AS v" +
                        " WHERE v.package<>:package AND v.package.name=:name" +
                        " AND v.deleted=false" +
                        " ORDER BY v.package.domain.release," +
                        " v.package.domain.name, v.repository.name," +
                        " v.repository.architecture")
                .setParameter("package", pack)
                .setParameter("name", name);

        return q.list();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Version> findLastSeenBefore(Repository repo, Date now) {
        Query q = getCurrentSession().createQuery(
                "FROM Version AS v" +
                " WHERE v.deleted=false" +
                  " AND v.repository=:repository" +
                  " AND v.lastSeen<:now" +
                " ORDER BY v.package.name")
                .setParameter("repository", repo)
                .setParameter("now", now);

        return q.list();
    }

}
