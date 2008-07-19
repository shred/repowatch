package org.shredzone.repowatch.web;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Version;
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
    

}
