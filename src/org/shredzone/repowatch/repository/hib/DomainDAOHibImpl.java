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
 * $Id: DomainDAOHibImpl.java 181 2008-07-22 11:35:11Z shred $
 */

package org.shredzone.repowatch.repository.hib;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.repository.DomainDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Hibernate implementation of {@link DomainDAO}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 181 $
 */
@org.springframework.stereotype.Repository      // class name collision!
@Transactional
public class DomainDAOHibImpl implements DomainDAO {

    @Autowired
    private SessionFactory sessionFactory;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Domain data) {
        sessionFactory.getCurrentSession().merge(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Domain data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Domain fetch(long id) {
        return (Domain) sessionFactory.getCurrentSession().get(Domain.class, id);
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    @Override
    public List<Domain> findAllDomains() {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Domain AS d" +
                        " ORDER BY d.name, d.release");

        return (List<Domain>) q.list();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Domain findDomain(String name, String release) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Domain AS d" +
                        " WHERE d.name=:name AND d.release=:release")
                .setParameter("name", name)
                .setParameter("release", release);
        
        return (Domain) q.uniqueResult();
    }

}
