package org.shredzone.repowatch.repository.hib;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.PackageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository      // dang, a name collision
@Transactional
public class PackageDAOHibImpl implements PackageDAO {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public void create(Package data) {
        sessionFactory.getCurrentSession().merge(data);
    }

    @Override
    public void delete(Package data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    @Transactional(readOnly = true)
    @Override
    public Package fetch(long id) {
        return (Package) sessionFactory.getCurrentSession().get(Package.class, id);
    }

    @Transactional(readOnly = true)
    public long countPackages(Repository repo) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "SELECT COUNT(*) FROM Version" +
                        " WHERE repository=:repo AND deleted=false")
                .setParameter("repo", repo);

        return ((Long) q.iterate().next()).longValue();
    }

    @Transactional(readOnly = true)
    public Package findPackage(Domain domain, String name) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Package AS p" +
                        " WHERE p.domain=:domain AND p.name=:name")
                .setParameter("domain", domain)
                .setParameter("name", name);

        return (Package) q.uniqueResult();
    }
    
    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public long countAllPackages() {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "SELECT COUNT(DISTINCT p.name) FROM Package AS p");

        return ((Long) q.iterate().next()).longValue();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Object[]> findAllPackages(int start, int limit) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "SELECT p.name, MAX(p.summary)" +
                        " FROM Package AS p" +
                        " GROUP BY p.name" +
                        " ORDER BY p.name");

        if (limit >= 0) {
            q.setFirstResult(start).setMaxResults(limit);
        }

        return (List<Object[]>) q.list();
    }

    @Transactional(readOnly = true)
    public List<Package> findAllPackagesForDomain(Domain domain) {
        return findAllPackages(domain, 0, -1);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
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
    
}
