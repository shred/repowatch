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
 * $Id: AdminDomainController.java 271 2009-02-26 23:23:35Z shred $
 */

package org.shredzone.repowatch.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.web.util.RequestMappingResolver;
import org.shredzone.repowatch.web.util.RequestMappingResolver.RequestParts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of the domain admin masks.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 271 $
 */
@Controller
@SessionAttributes("domain")
public class AdminDomainController {
    
    @Autowired
    private DomainDAO domainDao;
    
    private final static String DOMAINADD_PATTERN = "/admin/domain/add.html";
    private final static String DOMAINEDIT_PATTERN = "/admin/domain/edit/*.html";

    private final static RequestMappingResolver adminDomainEditResolver =
        new RequestMappingResolver(DOMAINEDIT_PATTERN);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields(new String[]{ "name", "release", "homeUrl" });
        binder.setRequiredFields(new String[]{ "name", "release" });
    }

    /**
     * Prepares to add a domain.
     */
    @RequestMapping(value=DOMAINADD_PATTERN, method=RequestMethod.GET)
    public ModelAndView addDomainHandler(){
        ModelAndView mav = new ModelAndView("admindomain");
        mav.addObject("domain", new Domain());
        return mav;
    }
    
    /**
     * Performs to add a domain.
     * 
     * @param domain        {@link Domain} to be added
     */
    @RequestMapping(value=DOMAINADD_PATTERN, method=RequestMethod.POST)
    public String addDomainFormHandler(@ModelAttribute("domain") Domain domain) {
        domainDao.insert(domain);
        return "forward:/admin/index.html";
    }

    /**
     * Prepares to edit a domain.
     */
    @RequestMapping(value=DOMAINEDIT_PATTERN, method=RequestMethod.GET)
    public ModelAndView editDomainHandler(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        RequestParts parts = adminDomainEditResolver.getRequestParts(req);
        if (parts.partCount() != 1) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        Domain domain = domainDao.fetch(Integer.parseInt(parts.getPart(0)));
        if (domain == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        ModelAndView mav = new ModelAndView("admindomain");
        mav.addObject("domain", domain);
        return mav;
    }
    
    /**
     * Performs to edit a domain.
     * 
     * @param req           {@link HttpServletRequest}
     * @param resp          {@link HttpServletResponse}
     */
    @RequestMapping(value=DOMAINEDIT_PATTERN, method=RequestMethod.POST)
    public String editDomainFormHandler(@ModelAttribute("domain") Domain domain) {
        domainDao.merge(domain);
        return "forward:/admin/index.html";
    }
    
}
