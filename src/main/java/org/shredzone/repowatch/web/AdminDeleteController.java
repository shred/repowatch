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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.shredzone.repowatch.service.DeleteService;
import org.shredzone.repowatch.web.util.RequestMappingResolver;
import org.shredzone.repowatch.web.util.RequestMappingResolver.RequestParts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of deletion.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 327 $
 */
@Controller
@SessionAttributes({"domain", "repo"})
public class AdminDeleteController {
    
    @Autowired
    private DeleteService deleteService;
    
    @Autowired
    private DomainDAO domainDao;

    @Autowired
    private RepositoryDAO repositoryDao;

    private final static String REPODELETE_PATTERN = "/admin/repo/delete/*.html";
    private final static String DOMAINDELETE_PATTERN = "/admin/domain/delete/*.html";

    private final static RequestMappingResolver adminDomainDeleteResolver =
        new RequestMappingResolver(DOMAINDELETE_PATTERN);

    private final static RequestMappingResolver adminRepoDeleteResolver =
        new RequestMappingResolver(REPODELETE_PATTERN);

    /**
     * Prepares to delete a domain.
     */
    @RequestMapping(value=DOMAINDELETE_PATTERN, method=RequestMethod.GET)
    public ModelAndView deleteDomainHandler(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        RequestParts parts = adminDomainDeleteResolver.getRequestParts(req);
        if (parts.partCount() != 1) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        Domain domain = domainDao.fetch(Integer.parseInt(parts.getPart(0)));
        if (domain == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        ModelAndView mav = new ModelAndView("adminconfirmdelete");
        mav.addObject("domain", domain);
        return mav;
    }
    
    /**
     * Performs to delete a domain.
     * 
     * @param req           {@link HttpServletRequest}
     * @param resp          {@link HttpServletResponse}
     */
    @RequestMapping(value=DOMAINDELETE_PATTERN, method=RequestMethod.POST)
    public String deleteDomainFormHandler(
            @ModelAttribute("domain") Domain domain,
            @RequestParam("confirmed") boolean confirmed,
            SessionStatus status
    ) {
        if (confirmed) {
            deleteService.deleteDomain(domain);
            status.setComplete();
        }
        return "forward:/admin/index.html";
    }
    
    /**
     * Prepares to delete a repository.
     */
    @RequestMapping(value=REPODELETE_PATTERN, method=RequestMethod.GET)
    public ModelAndView deleteRepositoryHandler(
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
    public String deleteRepositoryFormHandler(
            @ModelAttribute("repo") Repository repository,
            @RequestParam("confirmed") boolean confirmed,
            SessionStatus status
    ) {
        if (confirmed) {
            deleteService.deleteRepository(repository);
            status.setComplete();
        }
        return "forward:/admin/index.html";
    }
    
}
