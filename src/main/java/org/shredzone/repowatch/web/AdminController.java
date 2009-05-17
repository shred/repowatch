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

package org.shredzone.repowatch.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of the admin masks.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 328 $
 */
@Controller
public class AdminController {
    
    @Resource
    private RepositoryDAO repositoryDao;
    
    @Resource
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
