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

import org.shredzone.repowatch.model.Blacklist;
import org.shredzone.repowatch.repository.BlacklistDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Hibernate implementation of {@link BlacklistDAO}.
 *
 * @author Richard "Shred" Körber
 */
@Repository
@Transactional
public class BlacklistDAOHibImpl extends BaseDAOHibImpl<Blacklist> implements BlacklistDAO {

    @Transactional(readOnly = true)
    @Override
    public Blacklist fetch(long id) {
        return (Blacklist) getCurrentSession().get(Blacklist.class, id);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    @Override
    public List<Blacklist> findAllBlacklists() {
        return getCurrentSession().createQuery("FROM Blacklist AS d").list();
    }

}
