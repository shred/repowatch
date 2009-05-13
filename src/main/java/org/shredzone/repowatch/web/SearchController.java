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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.shredzone.repowatch.config.Configuration;
import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.repository.SearchDTO;
import org.shredzone.repowatch.web.util.BrowserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * This controller takes care of all search operations.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 317 $
 */
@Controller
public class SearchController {
    
    @Autowired
    private PackageDAO packageDao;
    
    @Autowired
    private DomainDAO domainDao;
    
    @Autowired
    private Configuration config;

    
    /**
     * Lists all changes of a repository, in a human readable form.
     *  
     * @param req           {@link HttpServletRequest}
     * @param session       {@link HttpSession}
     * @param doFlag        Flag asking to perform a new search
     * @param term          Search term, or <code>null</code>
     * @param descFlag      Flag to search in descriptions, or <code>null</code>
     * @param page          Page number in the browser, or <code>null</code>
     * @param domainonlyFlag  Flag to limit the search to the domainId only
     * @param domainId      Domain ID to limit the search at
     * @return  {@link ModelAndView} for rendering.
     */
    @RequestMapping("/search.html")
    public ModelAndView repoHandler(
            HttpServletRequest req,
            HttpSession session,
            @RequestParam(value="do", required=false) Boolean doFlag,
            @RequestParam(value="term", required=false) String term,
            @RequestParam(value="desc", required=false) Boolean descFlag,
            @RequestParam(value="page", required=false) Integer page,
            @RequestParam(value="domainonly", required=false) Boolean domainonlyFlag,
            @RequestParam(value="domainid", required=false) Long domainId
    ) {
        ModelAndView mav = new ModelAndView("search");
        mav.addObject("domainList", domainDao.findAllDomains());
        
        SearchDTO search =  (SearchDTO) WebUtils.getOrCreateSessionAttribute(
                session, "searchData", SearchDTO.class);
        mav.addObject("searchData", search);
        
        if (doFlag != null && doFlag.booleanValue() == true) {
            search.setPage(0);

            search.setDescriptions(descFlag != null && descFlag.booleanValue() == true);
            
            if (domainonlyFlag != null && domainonlyFlag.booleanValue() == true && domainId != null) {
                Domain dom = domainDao.fetch(domainId);
                // If the domain was not found, null is returned, which is a valid
                // value that automatically turns off the domain only search. Thus,
                // we don't need to throw an error here.
                search.setDomainOnly(dom);
            } else {
                search.setDomainOnly(null);
            }

            if (term != null) {
                search.setTerm(term.trim());
            }
        }
        
        if (search.getTerm() == null) {
            return mav;
        }
        
        if (page != null) {
            search.setPage(page);
        }
        
        //--- Validate search ---
        if (search.getTerm().length() < config.getSearchTermMinLength()) {
            mav.addObject("message", "search.error.minlength");
            return mav;
        }
        
        int entriesPerPage = config.getEntriesPerPage();
        long count = packageDao.countSearchResult(search);
        if (count == 0) {
            mav.addObject("message", "search.error.empty");
            return mav;
        }
        
        BrowserData browser = new BrowserData();
        browser.setBaseurl(req.getServletPath());
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
