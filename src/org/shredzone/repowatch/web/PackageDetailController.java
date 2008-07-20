package org.shredzone.repowatch.web;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PackageDetailController {
    
    @Autowired
    private PackageDAO packageDao;
    
    @Autowired
    private VersionDAO versionDao;
    
    @Autowired
    private DomainDAO domainDao;
    
    /*TODO: Configure this by injection. */
    private int entriesPerPage = 25;    // A sensible default
    
    private final static String SHOWPACKAGE_PATTERN = "/package/*.html";
    private final static RequestMappingResolver showpackageResolver = 
            new RequestMappingResolver(SHOWPACKAGE_PATTERN);

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
