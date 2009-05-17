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
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.repowatch.config.Configuration;
import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.model.Version;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.shredzone.repowatch.repository.VersionDAO;
import org.shredzone.repowatch.web.util.BrowserData;
import org.shredzone.repowatch.web.util.RequestMappingResolver;
import org.shredzone.repowatch.web.util.RequestMappingResolver.RequestParts;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of showing details of a package version.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 328 $
 */
@Controller
public class VersionsDetailController {
    
    @Resource
    private DomainDAO domainDao;
    
    @Resource
    private RepositoryDAO repositoryDao;
    
    @Resource
    private PackageDAO packageDao;
    
    @Resource
    private VersionDAO versionDao;
    
    @Resource
    private Configuration config;
    
    
    private final static String LISTVERSIONS_PATTERN = "/repo/*/*/*/*.html";
    private final static RequestMappingResolver listVersionsResolver =
            new RequestMappingResolver(LISTVERSIONS_PATTERN);

    /**
     * Lists all package versions of a repository.
     * 
     * @param req           {@link HttpServletRequest}
     * @param resp          {@link HttpServletResponse}
     * @param page          Browser page to be shown, or <code>null</code>
     * @return  {@link ModelAndView} for rendering.
     * @throws IOException
     */
    @RequestMapping(LISTVERSIONS_PATTERN)
    public ModelAndView listVersionsHandler(
            HttpServletRequest req,
            HttpServletResponse resp,
            @RequestParam(value="page", required=false) Integer page
    ) throws IOException {
        RequestParts parts = listVersionsResolver.getRequestParts(req);
        if (! parts.hasParts()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        String domName    = parts.getPart(0);
        String domRelease = parts.getPart(1);
        String repoName   = parts.getPart(2);
        String repoArch   = parts.getPart(3);

        ModelAndView mav = new ModelAndView("listversions");

        Domain domain = domainDao.findDomain(domName, domRelease);
        if (domain == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        Repository repository = repositoryDao.findRepository(domain, repoName, repoArch);
        if (repository == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        BrowserData browser = new BrowserData();
        browser.setBaseurl(req.getServletPath());

        int entriesPerPage = config.getEntriesPerPage();
        long count = packageDao.countPackages(repository);
        browser.setResultcount(count);
        browser.setPagecount((int) Math.ceil((double)count / entriesPerPage));
        browser.setPage(page != null ? page : 0);

        List<Version> versions = versionDao.findAllVersions(
                repository,
                browser.getPage() * entriesPerPage,
                entriesPerPage);
        
        mav.addObject("domain", domain);
        mav.addObject("repository", repository);
        mav.addObject("versionList", versions);
        mav.addObject("browser", browser);
        return mav;
    }

}
