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

import java.util.SortedMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.shredzone.repowatch.config.Configuration;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.web.util.BrowserData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of listing packages.
 *
 * @author Richard "Shred" Körber
 */
@Controller
public class PackageListController {

    @Resource
    private PackageDAO packageDao;

    @Resource
    private Configuration config;


    /**
     * Lists all known packages.
     *
     * @param req           {@link HttpServletRequest}
     * @param page          Browser page to be shown, or <code>null</code>
     * @return  {@link ModelAndView} for rendering.
     */
    @RequestMapping("/packages.html")
    public ModelAndView packagesHandler(
            HttpServletRequest req,
            @RequestParam(value="page", required=false) Integer page
    ) {
        ModelAndView mav = new ModelAndView("listpackages");

        BrowserData browser = new BrowserData();
        browser.setBaseurl(req.getServletPath());

        int entriesPerPage = config.getEntriesPerPage();
        long count = packageDao.countAllPackages();
        browser.setResultcount(count);
        browser.setPagecount((int) Math.ceil((double)count / entriesPerPage));
        browser.setPage(page != null ? page : 0);

        SortedMap<String,String> packages = packageDao.findAllPackages(
                browser.getPage() * entriesPerPage,
                entriesPerPage);

        mav.addObject("packageList", packages);
        mav.addObject("browser", browser);
        return mav;
    }

}
