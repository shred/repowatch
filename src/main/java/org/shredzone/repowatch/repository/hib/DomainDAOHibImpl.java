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
import org.shredzone.repowatch.repository.DomainDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Hibernate implementation of {@link DomainDAO}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 317 $
 */
@org.springframework.stereotype.Repository      // class name collision!
@Transactional
public class DomainDAOHibImpl extends BaseDAOHibImpl<Domain> implements DomainDAO {

    @Transactional(readOnly = true)
    public Domain fetch(long id) {
        return (Domain) getCurrentSession().get(Domain.class, id);
    }
    
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Domain> findAllDomains() {
        Query q = getCurrentSession().createQuery(
                        "FROM Domain AS d" +
                        " ORDER BY d.name, d.release");

        return q.list();
    }

    @Transactional(readOnly = true)
    public Domain findDomain(String name, String release) {
        Query q = getCurrentSession().createQuery(
                        "FROM Domain AS d" +
                        " WHERE d.name=:name AND d.release=:release")
                .setParameter("name", name)
                .setParameter("release", release);
        
        return (Domain) q.uniqueResult();
    }

}
