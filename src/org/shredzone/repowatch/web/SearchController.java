package org.shredzone.repowatch.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.repository.SearchDTO;
import org.shredzone.repowatch.web.util.BrowserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {
    
    @Autowired
    private PackageDAO packageDao;
    
    /*TODO: Configure this by injection. */
    private int entriesPerPage = 25;    // A sensible default
    private int searchTermMinLength = 3;

    @RequestMapping("/search.html")
    public ModelAndView repoHandler(
            @RequestParam(value="do", required=false) Boolean doFlag,
            @RequestParam(value="term", required=false) String term,
            @RequestParam(value="desc", required=false) Boolean descFlag,
            @RequestParam(value="page", required=false) Integer page,
            HttpSession session
    ) {
        ModelAndView mav = new ModelAndView("search");
        
        SearchDTO search = (SearchDTO) session.getAttribute("searchData");
        if (search == null && doFlag == null) {
            return mav;
        }
        
        BrowserData browser = new BrowserData();
        
        if (search == null) {
            search = new SearchDTO();
            session.setAttribute("searchData", search);
        }
        mav.addObject("searchData", search);
        
        if (doFlag != null && doFlag.booleanValue() == true) {
            search.setPage(0);
            search.setDescriptions(descFlag != null && descFlag.booleanValue() == true);
        }

        if (term != null) {
            search.setTerm(term.trim());
        }
        
        if (page != null) {
            search.setPage(page);
        }
        
        //--- Validate search ---
        if (search.getTerm().length() < searchTermMinLength) {
            mav.addObject("message", "search.error.minlength");
            return mav;
        }
        
        long count = packageDao.countSearchResult(search);
        if (count == 0) {
            mav.addObject("message", "search.error.empty");
            return mav;
        }
        
        browser.setResultcount(count);
        browser.setPagecount((int) Math.ceil((double)count / entriesPerPage));
        browser.setPage(search.getPage());
        
        List<Package> result = packageDao.findSearchResult(
                search,
                browser.getPage() * entriesPerPage,
                entriesPerPage);

        mav.setViewName("searchresult");        
        mav.addObject("packageList", result);
        mav.addObject("browser", browser);
        return mav;
    }
    
}
