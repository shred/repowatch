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

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Version;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.repository.VersionDAO;
import org.shredzone.repowatch.web.util.RequestMappingResolver;
import org.shredzone.repowatch.web.util.RequestMappingResolver.RequestParts;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of showing details of packages.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 328 $
 */
@Controller
public class PackageDetailController {
    
    @Resource
    private PackageDAO packageDao;
    
    @Resource
    private VersionDAO versionDao;
    
    @Resource
    private DomainDAO domainDao;
    
    
    private final static String SHOWPACKAGE_PATTERN = "/package/*.html";
    private final static RequestMappingResolver showpackageResolver =
            new RequestMappingResolver(SHOWPACKAGE_PATTERN);

    /**
     * Shows the details of a package.
     * 
     * @param req           {@link HttpServletRequest}
     * @param resp          {@link HttpServletResponse}
     * @return  {@link ModelAndView} for rendering.
     * @throws IOException
     */
    @RequestMapping(SHOWPACKAGE_PATTERN)
    public ModelAndView showPackageHandler(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        RequestParts parts = showpackageResolver.getRequestParts(req);
        if (! parts.hasParts()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        ModelAndView mav = new ModelAndView("showpackage");
        String packname = parts.getPart(0);
        
        Package pack = packageDao.findLatestPackage(packname);
        if (pack == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        List<Version> alternatives = versionDao.findAllVersionsForName(packname);
        
        mav.addObject("package", pack);
        mav.addObject("versionList", null);
        mav.addObject("alternativeList", alternatives);
        return mav;
    }

    
    private final static String SHOWSINGLEPACKAGE_PATTERN = "/package/*/*/*.html";
    private final static RequestMappingResolver showSinglePackageResolver =
            new RequestMappingResolver(SHOWSINGLEPACKAGE_PATTERN);

    /**
     * Shows the details of a package, in the context of a certain domain.
     * 
     * @param req           {@link HttpServletRequest}
     * @param resp          {@link HttpServletResponse}
     * @return  {@link ModelAndView} for rendering.
     * @throws IOException
     */
    @RequestMapping(SHOWSINGLEPACKAGE_PATTERN)
    public ModelAndView showSinglePackageHandler(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        RequestParts parts = showSinglePackageResolver.getRequestParts(req);
        if (! parts.hasParts()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        ModelAndView mav = new ModelAndView("showpackage");
        String domName = parts.getPart(0);
        String domRelease = parts.getPart(1);
        String packName = parts.getPart(2);
        
        Domain domain = domainDao.findDomain(domName, domRelease);
        if (domain == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        Package pack = packageDao.findPackage(domain, packName);
        if (pack == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        List<Version> alternatives = versionDao.findAllVersionsExcept(packName, pack);
        List<Version> versions = versionDao.findAllVersions(pack);

        mav.addObject("domain", domain);
        mav.addObject("package", pack);
        mav.addObject("versionList", versions);
        mav.addObject("alternativeList", alternatives);
        
        return mav;
    }

}
