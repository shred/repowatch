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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.shredzone.repowatch.validator.RepositoryValidator;
import org.shredzone.repowatch.web.util.RequestMappingResolver;
import org.shredzone.repowatch.web.util.RequestMappingResolver.RequestParts;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of the repository admin masks.
 *
 * @author Richard "Shred" Körber
 */
@Controller
@SessionAttributes("repo")
public class AdminRepoController {

    @Resource
    private DomainDAO domainDao;

    @Resource
    private RepositoryDAO repositoryDao;

    private final static String REPOADD_PATTERN = "/admin/repo/add/*.html";
    private final static String REPOEDIT_PATTERN = "/admin/repo/edit/*.html";

    private final static RequestMappingResolver adminRepoAddResolver =
        new RequestMappingResolver(REPOADD_PATTERN);

    private final static RequestMappingResolver adminRepoEditResolver =
        new RequestMappingResolver(REPOEDIT_PATTERN);

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
     */
    @RequestMapping(value=REPOADD_PATTERN, method=RequestMethod.POST)
    public String addRepositoryFormHandler(@ModelAttribute("repo") Repository repository,
            BindingResult result, SessionStatus status) {
        new RepositoryValidator().validate(repository, result);
        if (result.hasErrors()) {
            return "adminrepo";
        }

        Domain dom = repository.getDomain();
        dom = domainDao.merge(dom);
        repository.setDomain(dom);

        repositoryDao.insert(repository);
        status.setComplete();
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
     */
    @RequestMapping(value=REPOEDIT_PATTERN, method=RequestMethod.POST)
    public String editDomainFormHandler(@ModelAttribute("repo") Repository repository,
            BindingResult result, SessionStatus status) {
        new RepositoryValidator().validate(repository, result);
        if (result.hasErrors()) {
            return "adminrepo";
        }

        repositoryDao.merge(repository);
        status.setComplete();
        return "forward:/admin/index.html";
    }

}
