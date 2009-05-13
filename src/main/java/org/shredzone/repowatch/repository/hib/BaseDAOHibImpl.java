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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.shredzone.repowatch.model.BaseModel;
import org.shredzone.repowatch.repository.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base implementation of {@link BaseDAO}.
 *
 * @param <T> Type of entity
 * @author Richard "Shred" Körber
 */
@Repository
@Transactional
public abstract class BaseDAOHibImpl<T extends BaseModel> implements BaseDAO<T> {

    @Autowired
    private SessionFactory sessionFactory;
    
    /**
     * Gets the current {@link Session}.
     * 
     * @return Current {@link Session}
     */
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public void insert(T data) {
        getCurrentSession().persist(data);
    }
    
    @SuppressWarnings("unchecked")
    public T merge(T data) {
        // Requires Spring's IdTransferringMergeEventListener
        // TODO : is this still required?
        return (T) getCurrentSession().merge(data);
    }

    public void delete(T data) {
        getCurrentSession().delete(data);
    }

}
