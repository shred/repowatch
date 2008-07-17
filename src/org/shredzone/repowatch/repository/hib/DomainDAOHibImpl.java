package org.shredzone.repowatch.repository.hib;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.repository.DomainDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository      // dang, a name collision
@Transactional
public class DomainDAOHibImpl implements DomainDAO {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public void create(Domain data) {
        sessionFactory.getCurrentSession().merge(data);
    }

    @Override
    public void delete(Domain data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    @Transactional(readOnly = true)
    @Override
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

        return (List<Domain>) q.list();
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
