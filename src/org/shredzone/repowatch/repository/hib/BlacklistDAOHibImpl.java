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
 * $Id: BlacklistDAOHibImpl.java 210 2008-10-16 22:31:46Z shred $
 */

package org.shredzone.repowatch.repository.hib;

import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.shredzone.repowatch.model.Blacklist;
import org.shredzone.repowatch.repository.BlacklistDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Hibernate implementation of {@link BlacklistDAO}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 210 $
 */
@Repository
@Transactional
public class BlacklistDAOHibImpl implements BlacklistDAO {

    @Autowired
    private SessionFactory sessionFactory;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Blacklist data) {
        sessionFactory.getCurrentSession().merge(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Blacklist data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Blacklist fetch(long id) {
        return (Blacklist) sessionFactory.getCurrentSession().get(Blacklist.class, id);
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    @Override
    public Set<Blacklist> findAllBlacklists() {
        Query q = sessionFactory.getCurrentSession()
                .createQuery("FROM Blacklist AS d");

        return (Set<Blacklist>) q.list();
    }

}
