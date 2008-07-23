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
 * $Id: RepositorySynchronizerFactory.java 182 2008-07-23 13:57:39Z shred $
 */

package org.shredzone.repowatch.sync;

import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.ChangeDAO;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.repository.VersionDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A factory to create new {@link RepositorySynchronizer} objects easily
 * in a Spring context. Let Spring inject this factory to your class.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 182 $
 */
public class RepositorySynchronizerFactory {

    @Autowired
    private PackageDAO packageDao;
    
    @Autowired
    private VersionDAO versionDao;
    
    @Autowired
    private ChangeDAO changeDao;

    /**
     * Creates a new {@link RepositorySynchronizer} for a {@link Repository}.
     * 
     * @param repository    {@link Repository} to create the synchronizer for
     * @return   The ready to use {@link RepositorySynchronizer}
     */
    public RepositorySynchronizer createRepositorySynchronizer(Repository repository) {
        return new RepositorySynchronizer(
                repository,
                packageDao,
                versionDao,
                changeDao
        );
    }
    
}
