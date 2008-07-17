package org.shredzone.repowatch.repository.hib;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.model.Version;
import org.shredzone.repowatch.repository.VersionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository      // dang, a name collision
@Transactional
public class VersionDAOHibImpl implements VersionDAO {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public void create(Version data) {
        sessionFactory.getCurrentSession().merge(data);
    }

    @Override
    public void delete(Version data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    @Transactional(readOnly = true)
    @Override
    public Version fetch(long id) {
        return (Version) sessionFactory.getCurrentSession().get(Version.class, id);
    }
    
    @Transactional(readOnly = true)
    public List<Version> findAllVersions(Repository repo) {
        return findAllVersions(repo, 0, -1);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Version> findAllVersions(Repository repo, int start, int limit) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Version AS v" +
                        " WHERE v.repository=:repository AND v.deleted=false" +
                        " ORDER BY v.package.name")
                .setParameter("repository", repo);
        
        if (limit >= 0) {
            q.setFirstResult(start).setMaxResults(limit);
        }
        
        return (List<Version>) q.list();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Version> findAllVersions(Package pack) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Version AS v" +
                        " WHERE v.package=:package AND v.deleted=false" +
                        " ORDER BY v.repository.name, v.repository.architecture")
                .setParameter("package", pack);

        return (List<Version>) q.list();
    }

    @Transactional(readOnly = true)
    public Map<String,Version> findAllVersionsAsMap(Repository repo) {
        List<Version> versions = findAllVersions(repo);
        Map<String,Version> versionCache = new HashMap<String,Version>();
        for (Version v : versions) {
            versionCache.put(v.getPackage().getName(), v);
        }
        return versionCache;
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Version> findAllVersionsForName(String name) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Version AS v" +
                        " WHERE v.package.name=:name AND v.deleted=false" +
                        " ORDER BY v.package.domain.release," +
                        " v.package.domain.name, v.repository.name," +
                        " v.repository.architecture")
                .setParameter("name", name);

        return (List<Version>) q.list();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Version> findAllVersionsExcept(String name, Package pack) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Version AS v" +
                        " WHERE v.package<>:package AND v.package.name=:name" +
                        " AND v.deleted=false" +
                        " ORDER BY v.package.domain.release," +
                        " v.package.domain.name, v.repository.name," +
                        " v.repository.architecture")
                .setParameter("package", pack)
                .setParameter("name", name);

        return (List<Version>) q.list();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Version> findDeleted(Repository repo, Date now) {
        Query q = sessionFactory.getCurrentSession().createQuery(
                "FROM Version AS v" +
                " WHERE v.deleted=false" +
                  " AND v.repository=:repository" +
                  " AND v.lastSeen<:now" +
                " ORDER BY v.package.name")
                .setParameter("repository", repo)
                .setParameter("now", now);

        return (List<Version>) q.list();
    }

}
