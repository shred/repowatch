package org.shredzone.repowatch.web;

import java.util.List;

import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.web.util.BrowserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PackageListController {
    
    @Autowired
    private PackageDAO packageDao;
    
    /*TODO: Configure this by injection. */
    private int entriesPerPage = 25;    // A sensible default
    
    @RequestMapping("/packages.html")
    public ModelAndView packagesHandler(
            @RequestParam(value="page", required=false) Integer page,
            WebRequest webrequest
    ) {
        ModelAndView mav = new ModelAndView("listpackages");
        
        BrowserData browser = new BrowserData();
        
        long count = packageDao.countAllPackages();
        browser.setResultcount(count);
        browser.setPagecount((int) Math.ceil((double)count / entriesPerPage));
        browser.setPage(page != null ? page : 0);
        
        List<Object[]> packages = packageDao.findAllPackages(
                browser.getPage() * entriesPerPage,
                entriesPerPage);
        
        mav.addObject("packageList", packages);
        mav.addObject("browser", browser);
        return mav;
    }
    

}
