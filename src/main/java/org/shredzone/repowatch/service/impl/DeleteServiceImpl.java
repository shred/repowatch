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

package org.shredzone.repowatch.service.impl;

import org.hibernate.SessionFactory;
import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.ChangeDAO;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.shredzone.repowatch.repository.VersionDAO;
import org.shredzone.repowatch.service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A standard implementation of the {@link DeleteService} service.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 327 $
 */
@Service
@Transactional
public class DeleteServiceImpl implements DeleteService {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private DomainDAO domainDao;

    @Autowired
    private PackageDAO packageDao;

    @Autowired
    private RepositoryDAO repositoryDao;

    @Autowired
    private ChangeDAO changeDao;

    @Autowired
    private VersionDAO versionDao;
    
    @Override
    public void deleteDomain(Domain domain) {
        Domain attachedDomain = domainDao.merge(domain);
        
        for (Repository repo : repositoryDao.findRepositories(attachedDomain)) {
            deleteRepository(repo);
        }
        packageDao.deleteAllPackagesForDomain(domain);
        
        sessionFactory.getCurrentSession().flush();
        
        domainDao.delete(attachedDomain);
    }

    @Override
    public void deleteRepository(Repository repository) {
        Repository attachedRepository = repositoryDao.merge(repository);
        
        changeDao.deleteAllChangesForRepository(attachedRepository);
        versionDao.deleteAllVersionsForRepository(attachedRepository);

        sessionFactory.getCurrentSession().flush();
        
        repositoryDao.delete(attachedRepository);
    }

}
