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
 * $Id: PackageDAOHibImpl.java 197 2008-07-28 22:31:00Z shred $
 */

package org.shredzone.repowatch.repository.hib;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.repository.SearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Hibernate implementation of {@link PackageDAO}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 197 $
 */
@org.springframework.stereotype.Repository      // dang, a name collision
@Transactional
public class PackageDAOHibImpl implements PackageDAO {

    @Autowired
    private SessionFactory sessionFactory;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Package data) {
        sessionFactory.getCurrentSession().merge(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Package data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Package fetch(long id) {
        return (Package) sessionFactory.getCurrentSession().get(Package.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public long countPackages(Repository repo) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "SELECT COUNT(*) FROM Version" +
                        " WHERE repository=:repo AND deleted=false")
                .setParameter("repo", repo);

        return ((Long) q.iterate().next()).longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Package findPackage(Domain domain, String name) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Package AS p" +
                        " WHERE p.domain=:domain AND p.name=:name")
                .setParameter("domain", domain)
                .setParameter("name", name);

        return (Package) q.uniqueResult();
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Package findLatestPackage(String name) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "SELECT p FROM Version AS v" +
                        " INNER JOIN v.package AS p" +
                        " WHERE v.deleted=false AND p.name=:name" +
                        " ORDER BY v.ver, v.rel")
                .setParameter("name", name)
                .setMaxResults(1);
        
        return (Package) q.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public long countAllPackages() {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "SELECT COUNT(DISTINCT p.name) FROM Package AS p");

        return ((Long) q.iterate().next()).longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    @Override
    public SortedMap<String,String> findAllPackages(int start, int limit) {
        /*TODO: Currently only the summary with the maximal alphanumerical
         * value is returned. It would be better to return the summary of
         * the package with the latest update timestamp.
         */
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "SELECT p.name, MAX(p.summary)" +
                        " FROM Package AS p" +
                        " GROUP BY p.name" +
                        " ORDER BY p.name");

        if (limit >= 0) {
            q.setFirstResult(start).setMaxResults(limit);
        }
        
        SortedMap<String,String> result = new TreeMap<String,String>(); 
        for (Object[] data : (List<Object[]>) q.list()) {
            result.put(data[0].toString(), data[1].toString());
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Package> findAllPackagesForDomain(Domain domain) {
        return findAllPackages(domain, 0, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    @Override
    public List<Package> findAllPackages(Domain domain, int start, int limit) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Package AS p" +
                        " WHERE p.domain=:domain" +
                        " ORDER BY p.name")
                .setParameter("domain", domain);
        
        if (limit >= 0) {
            q.setFirstResult(start).setMaxResults(limit);
        }
        
        return (List<Package>) q.list();
    }
 
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public long countSearchResult(SearchDTO data) {
        Criteria crit = createCriteria(data);
        crit.setProjection(Projections.rowCount());
        return ((Number) crit.uniqueResult()).longValue();
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    @Override
    public List<Package> findSearchResult(SearchDTO data, int start, int limit) {
        Criteria crit = createCriteria(data);
        crit.addOrder(Order.asc("name"));
        crit.createCriteria("domain")
            .addOrder(Order.asc("name"))
            .addOrder(Order.desc("release"));
        
        if (limit >= 0) {
            crit.setFirstResult(start).setMaxResults(limit);
        }
        
        return (List<Package>) crit.list();
    }

    /**
     * Creates a {@link Query} from the parameters stored in {@link SearchDTO}.
     * 
     * @param data
     *            {@link SearchDTO} containing the search parameters.
     * @return {@link Criteria} object.
     */
    private Criteria createCriteria(SearchDTO data) {
        Criteria crit = sessionFactory.getCurrentSession().createCriteria(Package.class);
        
        String liketerm = data.getTerm().replaceAll("(%|_)", "\\\\$1");
        if (data.isDescriptions()) {
            crit.add(Restrictions.or(
                Restrictions.ilike("name", liketerm, MatchMode.ANYWHERE),
                Restrictions.or(
                    Restrictions.ilike("summary", liketerm, MatchMode.ANYWHERE),
                    Restrictions.ilike("description", liketerm, MatchMode.ANYWHERE)
                )
            ));
        } else {
            crit.add(Restrictions.ilike("name", liketerm, MatchMode.ANYWHERE));
        }
        
        Domain dom = data.getDomainOnly();
        if (dom != null) {
            crit.add(Restrictions.eq("domain", dom));
        }
        
        return crit;
    }
    
}
