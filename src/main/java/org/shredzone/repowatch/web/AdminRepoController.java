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
 * $Id: AdminRepoController.java 270 2009-02-25 23:06:14Z shred $
 */

package org.shredzone.repowatch.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.shredzone.repowatch.web.util.RequestMappingResolver;
import org.shredzone.repowatch.web.util.RequestMappingResolver.RequestParts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of the repository admin masks.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 270 $
 */
@Controller
@SessionAttributes("repo")
public class AdminRepoController {
    
    @Autowired
    private DomainDAO domainDao;

    @Autowired
    private RepositoryDAO repositoryDao;

    private final static String REPOADD_PATTERN = "/admin/repo/add/*.html";
    private final static String REPOEDIT_PATTERN = "/admin/repo/edit/*.html";
    private final static String REPODELETE_PATTERN = "/admin/repo/delete/*.html";

    private final static RequestMappingResolver adminRepoAddResolver =
        new RequestMappingResolver(REPOADD_PATTERN);

    private final static RequestMappingResolver adminRepoEditResolver =
        new RequestMappingResolver(REPOEDIT_PATTERN);

    private final static RequestMappingResolver adminRepoDeleteResolver =
        new RequestMappingResolver(REPODELETE_PATTERN);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields(new String[]{ "name", "architecture", "baseUrl", "repoviewUrl" });
        binder.setRequiredFields(new String[]{ "name", "architecture", "baseUrl" });
    }

    /**
     * Prepares to add a repository.
     */
    @RequestMapping(value=REPOADD_PATTERN, method=RequestMethod.GET)
    public ModelAndView addRepositoryHandler(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        RequestParts parts = adminRepoAddResolver.getRequestParts(req);
        if (parts.partCount() != 1) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        Domain domain = domainDao.fetch(Integer.parseInt(parts.getPart(0)));
        if (domain == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        Repository repo = new Repository();
        repo.setDomain(domain);
        
        ModelAndView mav = new ModelAndView("adminrepo");
        mav.addObject("repo", repo);
        return mav;
    }
    
    /**
     * Performs to add a repository.
     * 
     * @param repository     {@link Repository} to be added
     */
    @RequestMapping(value=REPOADD_PATTERN, method=RequestMethod.POST)
    public String addRepositoryFormHandler(@ModelAttribute("repo") Repository repository) {
        Domain dom = repository.getDomain();
        dom = domainDao.merge(dom);
        repository.setDomain(dom);

        repositoryDao.insert(repository);
        return "forward:/admin/index.html";
    }

    /**
     * Prepares to edit a repository.
     */
    @RequestMapping(value=REPOEDIT_PATTERN, method=RequestMethod.GET)
    public ModelAndView editRepositoryHandler(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        RequestParts parts = adminRepoEditResolver.getRequestParts(req);
        if (parts.partCount() != 1) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        Repository repository = repositoryDao.fetch(Integer.parseInt(parts.getPart(0)));
        if (repository == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        ModelAndView mav = new ModelAndView("adminrepo");
        mav.addObject("repo", repository);
        return mav;
    }
    
    /**
     * Performs to edit a repository.
     * 
     * @param req           {@link HttpServletRequest}
     * @param resp          {@link HttpServletResponse}
     */
    @RequestMapping(value=REPOEDIT_PATTERN, method=RequestMethod.POST)
    public String editDomainFormHandler(@ModelAttribute("repo") Repository repository) {
        repositoryDao.merge(repository);
        return "forward:/admin/index.html";
    }
    
    /**
     * Prepares to delete a repository.
     */
    @RequestMapping(value=REPODELETE_PATTERN, method=RequestMethod.GET)
    public ModelAndView deleteDomainHandler(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        RequestParts parts = adminRepoDeleteResolver.getRequestParts(req);
        if (parts.partCount() != 1) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        Repository repository = repositoryDao.fetch(Integer.parseInt(parts.getPart(0)));
        if (repository == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        ModelAndView mav = new ModelAndView("adminconfirmdelete");
        mav.addObject("repo", repository);
        return mav;
    }
    
    /**
     * Performs to delete a repository.
     * 
     * @param req           {@link HttpServletRequest}
     * @param resp          {@link HttpServletResponse}
     */
    @RequestMapping(value=REPODELETE_PATTERN, method=RequestMethod.POST)
    public String deleteDomainFormHandler(@ModelAttribute("repo") Repository repository) {
        // TODO : Check confirmation
        repositoryDao.delete(repository);
        return "forward:/admin/index.html";
    }

}
