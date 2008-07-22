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
 * $Id: RepositoryListController.java 181 2008-07-22 11:35:11Z shred $
 */

package org.shredzone.repowatch.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of listing repositories.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 181 $
 */
@Controller
public class RepositoryListController {
    
    @Autowired
    private RepositoryDAO repositoryDao;
    
    @Autowired
    private PackageDAO packageDao;
    

    /**
     * Lists all repositories.
     * @return  {@link ModelAndView} for rendering.
     */
    @RequestMapping("/repo.html")
    public ModelAndView repoHandler() {
        ModelAndView mav = new ModelAndView("listrepositories");
        
        List<Repository> repositories = repositoryDao.findAllRepositories();

        Map<Repository,Long> counter = new HashMap<Repository,Long>();
        for (Repository repo : repositories) {
            counter.put(repo, packageDao.countPackages(repo));
        }
        
        mav.addObject(repositories);
        mav.addObject("counterMap", counter);
        return mav;
    }
    
}
