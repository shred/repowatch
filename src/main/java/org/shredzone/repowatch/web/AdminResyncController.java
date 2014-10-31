/**
 * repowatch - A yum repository watcher
 *
 * Copyright (C) 2009 Richard "Shred" Körber
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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.shredzone.repowatch.service.SyncService;
import org.shredzone.repowatch.sync.SynchronizerException;
import org.shredzone.repowatch.web.util.RequestMappingResolver;
import org.shredzone.repowatch.web.util.RequestMappingResolver.RequestParts;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller takes care of manual resynchronizing.
 *
 * @author Richard "Shred" Körber
 */
@Controller
public class AdminResyncController {

    @Resource
    private SyncService syncService;

    @Resource
    private DomainDAO domainDao;

    @Resource
    private RepositoryDAO repositoryDao;

    private final static String RESYNC_ALL_PATTERN = "/admin/resync/all.html";
    private final static String RESYNC_DOMAIN_PATTERN = "/admin/resync/domain/*.html";
    private final static String RESYNC_REPO_PATTERN = "/admin/resync/repo/*.html";

    private final static RequestMappingResolver adminResyncDomainResolver =
        new RequestMappingResolver(RESYNC_DOMAIN_PATTERN);

    private final static RequestMappingResolver adminResyncRepoResolver =
        new RequestMappingResolver(RESYNC_REPO_PATTERN);

    /**
     * Resync everything.
     */
    @RequestMapping(value=RESYNC_ALL_PATTERN, method=RequestMethod.GET)
    public ModelAndView resyncAllHandler() {
        List<SyncResult> resultList = new ArrayList<SyncResult>();

        for (Repository repo : repositoryDao.findAllRepositories()) {
            SyncResult result = new SyncResult(repo);
            try {
                result.startTimer();
                syncService.syncRepository(repo);
            } catch (SynchronizerException ex) {
                result.failed(ex);
            } finally {
                result.stopTimer();
            }
            resultList.add(result);
        }

        ModelAndView mav = new ModelAndView("adminresync");
        mav.addObject("resultList", resultList);
        return mav;
    }

    /**
     * Resync a domain.
     */
    @RequestMapping(value=RESYNC_DOMAIN_PATTERN, method=RequestMethod.GET)
    public ModelAndView resyncDomainHandler(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        RequestParts parts = adminResyncDomainResolver.getRequestParts(req);
        if (parts.partCount() != 1) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        Domain domain = domainDao.fetch(Integer.parseInt(parts.getPart(0)));
        if (domain == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        List<SyncResult> resultList = new ArrayList<SyncResult>();

        for (Repository repo : repositoryDao.findRepositories(domain)) {
            SyncResult result = new SyncResult(repo);
            try {
                result.startTimer();
                syncService.syncRepository(repo);
            } catch (SynchronizerException ex) {
                result.failed(ex);
            } finally {
                result.stopTimer();
            }
            resultList.add(result);
        }

        ModelAndView mav = new ModelAndView("adminresync");
        mav.addObject("resultList", resultList);
        return mav;
    }

    /**
     * Resync a repository.
     */
    @RequestMapping(value=RESYNC_REPO_PATTERN, method=RequestMethod.GET)
    public ModelAndView resyncRepositoryHandler(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        RequestParts parts = adminResyncRepoResolver.getRequestParts(req);
        if (parts.partCount() != 1) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        Repository repo = repositoryDao.fetch(Integer.parseInt(parts.getPart(0)));
        if (repo == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        List<SyncResult> resultList = new ArrayList<SyncResult>();
        SyncResult result = new SyncResult(repo);
        try {
            result.startTimer();
            syncService.syncRepository(repo);
        } catch (SynchronizerException ex) {
            result.failed(ex);
        } finally {
            result.stopTimer();
        }
        resultList.add(result);

        ModelAndView mav = new ModelAndView("adminresync");
        mav.addObject("resultList", resultList);
        return mav;
    }

    /**
     * Contains the result of a single synchronization process.
     */
    public static class SyncResult {
        private final Repository repository;
        private SynchronizerException exception;
        private long startTimestamp;
        private long stopTimestamp;

        public SyncResult(Repository repository) {
            this.repository = repository;
        }

        public void failed(SynchronizerException ex) {
            this.exception = ex;
        }

        public void startTimer() {
            startTimestamp = System.currentTimeMillis();
        }

        public void stopTimer() {
            stopTimestamp = System.currentTimeMillis();
        }

        public Repository getRepository() {
            return repository;
        }

        public SynchronizerException getException() {
            return exception;
        }

        public long getRequiredTime() {
            return stopTimestamp - startTimestamp;
        }
    }

}
