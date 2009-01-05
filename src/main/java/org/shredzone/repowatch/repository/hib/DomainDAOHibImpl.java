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
 * $Id: DomainDAOHibImpl.java 222 2009-01-05 23:40:44Z shred $
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
 * @version $Revision: 222 $
 */
@org.springframework.stereotype.Repository      // class name collision!
@Transactional
public class DomainDAOHibImpl implements DomainDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void insert(Domain data) {
        sessionFactory.getCurrentSession().merge(data);
    }

    public void delete(Domain data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    @Transactional(readOnly = true)
    public Domain fetch(long id) {
        return (Domain) sessionFactory.getCurrentSession().get(Domain.class, id);
    }
    
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Domain> findAllDomains() {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Domain AS d" +
                        " ORDER BY d.name, d.release");

        return q.list();
    }

    @Transactional(readOnly = true)
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
