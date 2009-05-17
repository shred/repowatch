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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.validator.DomainValidator;
import org.shredzone.repowatch.web.util.RequestMappingResolver;
import org.shredzone.repowatch.web.util.RequestMappingResolver.RequestParts;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of the domain admin masks.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 328 $
 */
@Controller
@SessionAttributes("domain")
public class AdminDomainController {
    
    @Resource
    private DomainDAO domainDao;
    
    private final static String DOMAINADD_PATTERN = "/admin/domain/add.html";
    private final static String DOMAINEDIT_PATTERN = "/admin/domain/edit/*.html";

    private final static RequestMappingResolver adminDomainEditResolver =
        new RequestMappingResolver(DOMAINEDIT_PATTERN);

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
    public String addDomainFormHandler(@ModelAttribute("domain") Domain domain,
            BindingResult result, SessionStatus status) {
        new DomainValidator().validate(domain, result);
        if (result.hasErrors()) {
            return "admindomain";
        }

        domainDao.insert(domain);
        status.setComplete();
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
    public String editDomainFormHandler(@ModelAttribute("domain") Domain domain,
            BindingResult result, SessionStatus status) {
        new DomainValidator().validate(domain, result);
        if (result.hasErrors()) {
            return "admindomain";
        }

        domainDao.merge(domain);
        status.setComplete();
        return "forward:/admin/index.html";
    }
    
}
