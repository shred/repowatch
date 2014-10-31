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

package org.shredzone.repowatch.sync;

import javax.annotation.Resource;

import org.shredzone.repowatch.model.Repository;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

/**
 * A factory to create new {@link RepositorySynchronizer} objects easily
 * in a Spring context.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class RepositorySynchronizerFactory {

    @Resource
    private BeanFactory beanFactory;

    /**
     * Creates a new {@link RepositorySynchronizer} for a {@link Repository}.
     *
     * @param repository    {@link Repository} to create the synchronizer for
     * @return   The ready to use {@link RepositorySynchronizer}
     */
    public RepositorySynchronizer createRepositorySynchronizer(Repository repository) {
        RepositorySynchronizer result =
                (RepositorySynchronizer) beanFactory.getBean("repositorySynchronizerImpl");
        result.setRepository(repository);
        return result;
    }

}
