package org.shredzone.repowatch.web;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.model.Version;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.shredzone.repowatch.repository.VersionDAO;
import org.shredzone.repowatch.web.util.BrowserData;
import org.shredzone.repowatch.web.util.RequestMappingResolver;
import org.shredzone.repowatch.web.util.RequestMappingResolver.RequestParts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class VersionsDetailController {
    
    @Autowired
    private DomainDAO domainDao;
    
    @Autowired
    private RepositoryDAO repositoryDao;
    
    @Autowired
    private PackageDAO packageDao;
    
    @Autowired
    private VersionDAO versionDao;
    
    /*TODO: Configure this by injection. */
    private int entriesPerPage = 25;    // A sensible default
    
    private final static String LISTVERSIONS_PATTERN = "/repo/*/*/*/*.html";
    private final static RequestMappingResolver listVersionsResolver = 
            new RequestMappingResolver(LISTVERSIONS_PATTERN);

    @RequestMapping(LISTVERSIONS_PATTERN)
    public ModelAndView listVersionsHandler(
            @RequestParam(value="page", required=false) Integer page,
            HttpServletRequest req
    ) {
        RequestParts parts = listVersionsResolver.getRequestParts(req);
        if (! parts.hasParts()) {
            throw new IllegalArgumentException(req.getContextPath() + ":" + req.getRequestURI() + " does not match");
            /*TODO: 404 */
        }
        
        String domName    = parts.getPart(0);
        String domRelease = parts.getPart(1);
        String repoName   = parts.getPart(2);
        String repoArch   = parts.getPart(3);

        ModelAndView mav = new ModelAndView("listversions");

        Domain domain = domainDao.findDomain(domName, domRelease);
        if (domain == null) {
            /*TODO: 404 */
        }

        Repository repository = repositoryDao.findRepository(domain, repoName, repoArch);
        if (repository == null) {
            /*TODO: 404 */
        }

        BrowserData browser = new BrowserData();
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
