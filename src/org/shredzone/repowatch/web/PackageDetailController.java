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
 * $Id: PackageDetailController.java 181 2008-07-22 11:35:11Z shred $
 */

package org.shredzone.repowatch.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Version;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.repository.VersionDAO;
import org.shredzone.repowatch.web.util.RequestMappingResolver;
import org.shredzone.repowatch.web.util.RequestMappingResolver.RequestParts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of showing details of packages.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 181 $
 */
@Controller
public class PackageDetailController {
    
    @Autowired
    private PackageDAO packageDao;
    
    @Autowired
    private VersionDAO versionDao;
    
    @Autowired
    private DomainDAO domainDao;
    
    
    private final static String SHOWPACKAGE_PATTERN = "/package/*.html";
    private final static RequestMappingResolver showpackageResolver = 
            new RequestMappingResolver(SHOWPACKAGE_PATTERN);

    /**
     * Shows the details of a package.
     *  
     * @param req           {@link HttpServletRequest}
     * @return  {@link ModelAndView} for rendering.
     */
    @RequestMapping(SHOWPACKAGE_PATTERN)
    public ModelAndView showPackageHandler(
            HttpServletRequest req
    ) {
        RequestParts parts = showpackageResolver.getRequestParts(req);
        if (! parts.hasParts()) {
            throw new IllegalArgumentException(req.getContextPath() + ":" + req.getRequestURI() + " does not match");
        }

        ModelAndView mav = new ModelAndView("showpackage");
        String packname = parts.getPart(0);
        
        Package pack = packageDao.findLatestPackage(packname);
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
     * @return  {@link ModelAndView} for rendering.
     */
    @RequestMapping(SHOWSINGLEPACKAGE_PATTERN)
    public ModelAndView showSinglePackageHandler(
            HttpServletRequest req
    ) {
        RequestParts parts = showSinglePackageResolver.getRequestParts(req);
        if (! parts.hasParts()) {
            throw new IllegalArgumentException(req.getContextPath() + ":" + req.getRequestURI() + " does not match");
        }
        
        ModelAndView mav = new ModelAndView("showpackage");
        String domName = parts.getPart(0);
        String domRelease = parts.getPart(1);
        String packName = parts.getPart(2);
        
        Domain domain = domainDao.findDomain(domName, domRelease);
        if (domain == null) {
            /*TODO: 404 */
        }
        
        Package pack = packageDao.findPackage(domain, packName);
        if (pack == null) {
            /*TODO: 404 */
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
