package org.shredzone.repowatch.repository.hib;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.shredzone.repowatch.model.Change;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.ChangeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository      // dang, a name collision
@Transactional
public class ChangeDAOHibImpl implements ChangeDAO {
    
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Adds a {@link Change} to the changelog.
     * 
     * @param change
     *            {@link Change} to be added.
     */
    @Override
    public void create(Change data) {
        sessionFactory.getCurrentSession().merge(data);
    }

    @Override
    public void delete(Change data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    @Transactional(readOnly = true)
    @Override
    public Change fetch(long id) {
        return (Change) sessionFactory.getCurrentSession().get(Change.class, id);
    }

    /**
     * Counts all changes logged for a {@link Repository}. Updates are counted.
     * 
     * @param repo
     *            Repository to count the changes of.
     * @return Number of changes
     */
    @Transactional(readOnly = true)
    public long countChanges(Repository repo) {
        return countChanges(repo, true);
    }

    /**
     * Counts all changes logged for a {@link Repository}. Only additions and
     * removals are counted unless the updates option is set.
     * 
     * @param repo
     *            Repository to count the changes of.
     * @param updates
     *            Also count Updates
     * @return Number of changes
     */
    @Transactional(readOnly = true)
    public long countChanges(Repository repo, boolean updates) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "SELECT COUNT(*) FROM Change" +
                		" WHERE repository=:repo" +
                        " AND (:updates=true OR change<>:typeupdated)")
                .setParameter("repo", repo)
                .setParameter("updates", updates)
                .setParameter("typeupdated", Change.Type.UPDATED);
        
        return ((Long) q.iterate().next()).longValue();
    }

    /**
     * Finds all changes logged for a {@link Repository}.
     * 
     * @param repo
     *            Repository to find changes for.
     * @return List of {@link Change} objects containing all changes.
     */
    @Transactional(readOnly = true)
    public List<Change> findAllChanges(Repository repo) {
        return findAllChanges(repo, 0, -1);
    }

    /**
     * Finds all changes logged for a {@link Repository}. Only the given range
     * is returned. Updates are included.
     * 
     * @param repo
     *            Repository to find changes for.
     * @param start
     *            First record to be returned
     * @param limit
     *            Maximum number of records to be returned. A negative value
     *            returns all records.
     * @return List of {@link Change} objects containing all changes.
     */
    @Transactional(readOnly = true)
    public List<Change> findAllChanges(Repository repo, int start, int limit) {
        return findAllChanges(repo, true, start, limit);
    }

    /**
     * Finds all changes logged for a {@link Repository}. Only the given range
     * is returned. Only additions and removals are returned unless the updates
     * option is set.
     * 
     * @param repo
     *            Repository to find changes for.
     * @param updates
     *            Also show Updates
     * @param start
     *            First record to be returned
     * @param limit
     *            Maximum number of records to be returned. A negative value
     *            returns all records.
     * @return List of {@link Change} objects containing all changes.
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Change> findAllChanges(Repository repo, boolean updates, int start, int limit) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Change AS c" +
                        " WHERE c.repository=:repository" +
                        " AND (:updates=true OR change<>:typeupdated)" +
                        " ORDER BY c.timestamp DESC, c.change, c.package.name")
                .setParameter("repository", repo)
                .setParameter("updates", updates)
                .setParameter("typeupdated", Change.Type.UPDATED);

        if (limit >= 0) {
            q.setFirstResult(start).setMaxResults(limit);
        }

        return (List<Change>) q.list();
    }

    /**
     * Finds all changes logged since a certain date. No more than a maximum
     * number of rows will be returned. Updates are included.
     * 
     * @param repo
     *            Repository to find changes for.
     * @param limit
     *            First date (inclusive) to return records from.
     * @param maxrows
     *            Maximum number of records to be returned. A negative value
     *            returns all records.
     * @return List of {@link Change} objects containing all changes.
     */
    @Transactional(readOnly = true)
    public List<Change> findAllChangesUntil(Repository repo, Date limit, int maxrows) {
        return findAllChangesUntil(repo, true, limit, maxrows);
    }

    /**
     * Finds all changes logged since a certain date. No more than a maximum
     * number of rows will be returned. Only additions and removals are returned
     * unless the updates option is set.
     * 
     * @param repo
     *            Repository to find changes for.
     * @param updates
     *            Also show Updates
     * @param limit
     *            First date (inclusive) to return records from.
     * @param maxrows
     *            Maximum number of records to be returned. A negative value
     *            returns all records.
     * @return List of {@link Change} objects containing all changes.
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Change> findAllChangesUntil(Repository repo, boolean updates, Date limit, int maxrows) {
        Query q = sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Change AS c" +
                        " WHERE c.repository=:repository" +
                        " AND c.timestamp>=:limit" +
                        " AND (:updates=true OR change<>:typeupdated)" +
                        " ORDER BY c.timestamp DESC, c.change, c.package.name")
                .setParameter("repository", repo)
                .setParameter("limit", limit)
                .setParameter("updates", updates)
                .setParameter("typeupdated", Change.Type.UPDATED);

        if (maxrows >= 0) {
            q.setMaxResults(maxrows);
        }

        return (List<Change>) q.list();
    }

}
