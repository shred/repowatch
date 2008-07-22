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
 * $Id: RepositoryDAOHibImpl.java 181 2008-07-22 11:35:11Z shred $
 */

package org.shredzone.repowatch.repository.hib;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Hibernate implementation of {@link RepositoryDAO}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 181 $
 */
@org.springframework.stereotype.Repository      // dang, a name collision
@Transactional
public class RepositoryDAOHibImpl implements RepositoryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Repository data) {
        sessionFactory.getCurrentSession().merge(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Repository data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Repository fetch(long id) {
        return (Repository) sessionFactory.getCurrentSession().get(Repository.class, id);
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Repository findRepository(Domain domain, String name, String architecture) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Repository AS r" +
                        " WHERE r.domain=:domain AND r.name=:name" +
                        " AND r.architecture=:architecture")
                .setParameter("domain", domain)
                .setParameter("name", name)
                .setParameter("architecture", architecture);

        return (Repository) q.uniqueResult();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Repository> findAllRepositories() {
        if (sessionFactory == null) throw new NullPointerException("sessionFactory is null");
        Session s = sessionFactory.getCurrentSession();
        if (s == null) throw new NullPointerException("session is null");
        
        Query q = s
                .createQuery(
                        "FROM Repository AS r" +
                        " ORDER BY r.domain.name, r.domain.release," +
                        " r.name, r.architecture");

        return (List<Repository>) q.list();
    }

}
