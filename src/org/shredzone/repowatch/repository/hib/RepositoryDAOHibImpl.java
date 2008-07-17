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

@org.springframework.stereotype.Repository      // dang, a name collision
@Transactional
public class RepositoryDAOHibImpl implements RepositoryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void create(Repository data) {
        sessionFactory.getCurrentSession().merge(data);
    }

    @Override
    public void delete(Repository data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    @Transactional(readOnly = true)
    @Override
    public Repository fetch(long id) {
        return (Repository) sessionFactory.getCurrentSession().get(Repository.class, id);
    }
    
    @Transactional(readOnly = true)
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
