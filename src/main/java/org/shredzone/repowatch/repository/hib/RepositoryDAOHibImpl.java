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

package org.shredzone.repowatch.repository.hib;

import java.util.List;

import org.hibernate.Query;
import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Hibernate implementation of {@link RepositoryDAO}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 328 $
 */
@org.springframework.stereotype.Repository      // dang, a name collision
@Transactional
public class RepositoryDAOHibImpl extends BaseDAOHibImpl<Repository> implements RepositoryDAO {

    @Transactional(readOnly = true)
    @Override
    public Repository fetch(long id) {
        return (Repository) getCurrentSession().get(Repository.class, id);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Repository findRepository(Domain domain, String name, String architecture) {
        Query q = getCurrentSession().createQuery(
                        "FROM Repository AS r" +
                        " WHERE r.domain=:domain AND r.name=:name" +
                        " AND r.architecture=:architecture")
                .setParameter("domain", domain)
                .setParameter("name", name)
                .setParameter("architecture", architecture);

        return (Repository) q.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Repository> findAllRepositories() {
        Query q = getCurrentSession().createQuery(
                        "FROM Repository AS r" +
                        " ORDER BY r.domain.name, r.domain.release," +
                        " r.name, r.architecture");

        return q.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Repository> findRepositories(Domain domain) {
        Query q = getCurrentSession().createQuery(
                        "FROM Repository AS r WHERE r.domain=:domain" +
                        " ORDER BY r.domain.name, r.domain.release," +
                        " r.name, r.architecture")
                .setParameter("domain", domain);

        return q.list();
    }

}
