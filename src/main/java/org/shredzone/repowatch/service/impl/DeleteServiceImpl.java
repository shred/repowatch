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
 * @version $Revision: 317 $
 */
@Service
@Transactional
public class DeleteServiceImpl implements DeleteService {

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
        for (Repository repo : repositoryDao.findRepositories(domain)) {
            deleteRepository(repo);
        }
        packageDao.deleteAllPackagesForDomain(domain);
        
        // I don't know why it is required to reattach the domain here.
        Domain mergedDomain = domainDao.merge(domain);
        domainDao.delete(mergedDomain);
    }

    @Override
    public void deleteRepository(Repository repository) {
        changeDao.deleteAllChangesForRepository(repository);
        versionDao.deleteAllVersionsForRepository(repository);

        // I don't know why it is required to reattach the domain here.
        Repository mergedRepository = repositoryDao.merge(repository);
        repositoryDao.delete(mergedRepository);
    }

}
