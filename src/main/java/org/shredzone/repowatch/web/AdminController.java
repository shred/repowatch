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
 * $Id: AdminController.java 265 2009-02-24 23:39:03Z shred $
 */

package org.shredzone.repowatch.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of the admin masks.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 265 $
 */
@Controller
public class AdminController {
    
    @Autowired
    private RepositoryDAO repositoryDao;
    
    @Autowired
    private DomainDAO domainDao;
    
    /**
     * Lists all changes of a repository, in a human readable form.
     * 
     * @return  {@link ModelAndView} for rendering.
     */
    @RequestMapping("/admin/index.html")
    public ModelAndView adminHandler() {
        ModelAndView mav = new ModelAndView("adminindex");

        Map<Domain,List<Repository>> repoMap = new HashMap<Domain,List<Repository>>(); 

        List<Domain> domainList = domainDao.findAllDomains();
        for (Domain domain : domainList) {
            List<Repository> repoList = repositoryDao.findRepositories(domain);
            repoMap.put(domain, repoList);
        }
        
        mav.addObject("domainList", domainList);
        mav.addObject("repoMap", repoMap);
        return mav;
    }
    
}
