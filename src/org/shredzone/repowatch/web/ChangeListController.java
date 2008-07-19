package org.shredzone.repowatch.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.DateFormatter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.shredzone.repowatch.model.Change;
import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.ChangeDAO;
import org.shredzone.repowatch.repository.DomainDAO;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.shredzone.repowatch.web.util.BrowserData;
import org.shredzone.repowatch.web.util.RequestMappingResolver;
import org.shredzone.repowatch.web.util.RequestMappingResolver.RequestParts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChangeListController {
    
    @Autowired
    private DomainDAO domainDao;
    
    @Autowired
    private RepositoryDAO repositoryDao;
    
    @Autowired
    private ChangeDAO changeDao;
    
    /*TODO: Configure this by injection. */
    private int entriesPerPage = 25;    // A sensible default
    private int maxNumberOfDays = 500;
    private int maxNumberOfEntries = 50;
    
    private final static String LISTCHANGES_PATTERN = "/changes/*/*/*/*.html";
    private final static RequestMappingResolver listChangesResolver = 
            new RequestMappingResolver(LISTCHANGES_PATTERN);

    @RequestMapping(LISTCHANGES_PATTERN)
    public ModelAndView listChangesHandler(
            @RequestParam(value="page", required=false) Integer page,
            @RequestParam(value="updates", required=false) Boolean updateFlag,
            HttpServletRequest req
    ) {
        RequestParts parts = listChangesResolver.getRequestParts(req);
        if (! parts.hasParts()) {
            throw new IllegalArgumentException(req.getContextPath() + ":" + req.getRequestURI() + " does not match");
            /*TODO: 404 */
        }
        
        String domName    = parts.getPart(0);
        String domRelease = parts.getPart(1);
        String repoName   = parts.getPart(2);
        String repoArch   = parts.getPart(3);
        boolean includeUpdates = (updateFlag != null && updateFlag.booleanValue() == true);

        ModelAndView mav = new ModelAndView("listchanges");

        Domain domain = domainDao.findDomain(domName, domRelease);
        if (domain == null) {
            /*TODO: 404 */
        }

        Repository repository = repositoryDao.findRepository(domain, repoName, repoArch);
        if (repository == null) {
            /*TODO: 404 */
        }
        
        BrowserData browser = new BrowserData();
        String base = req.getServletPath();
        if (includeUpdates) base += "?updates=1";
        browser.setBaseurl(base);
        
        long counter = changeDao.countChanges(repository, includeUpdates);
        browser.setResultcount(counter);
        browser.setPagecount((int) Math.ceil((double)counter / entriesPerPage));
        browser.setPage(page != null ? page : 0);
        
        List<Change> changes = changeDao.findAllChanges(
                repository,
                includeUpdates,
                browser.getPage() * entriesPerPage,
                entriesPerPage);
        
        mav.addObject("domain", domain);
        mav.addObject("repository", repository);
        mav.addObject("changeList", changes);
        mav.addObject("browser", browser);
        mav.addObject("updates", includeUpdates);
        
        StringBuilder sb = new StringBuilder();
        sb.append("rss/")
            .append(domain.getName()).append('/')
            .append(domain.getRelease()).append('/')
            .append(repository.getName()).append('/')
            .append(repository.getArchitecture()).append(".rss");
        if (includeUpdates) {
            sb.append("?updates=1");
        }
        mav.addObject("rsslink", sb.toString());

        return mav;
    }

    private final static String LISTCHANGESRSS_PATTERN = "/rss/*/*/*/*.rss";
    private final static RequestMappingResolver listChangesRssResolver = 
            new RequestMappingResolver(LISTCHANGESRSS_PATTERN);

    @RequestMapping(LISTCHANGESRSS_PATTERN)
    public void listChangesRssHandler(
            @RequestParam(value="updates", required=false) Boolean updateFlag,
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws XMLStreamException, IOException {
        RequestParts parts = listChangesRssResolver.getRequestParts(req);
        if (! parts.hasParts()) {
            throw new IllegalArgumentException(req.getContextPath() + ":" + req.getRequestURI() + " does not match");
            /*TODO: 404 */
        }
        
        String domName    = parts.getPart(0);
        String domRelease = parts.getPart(1);
        String repoName   = parts.getPart(2);
        String repoArch   = parts.getPart(3);
        boolean includeUpdates = (updateFlag != null && updateFlag.booleanValue() == true);

        ModelAndView mav = new ModelAndView("listchanges");

        Domain domain = domainDao.findDomain(domName, domRelease);
        if (domain == null) {
            /*TODO: 404 */
        }

        Repository repository = repositoryDao.findRepository(domain, repoName, repoArch);
        if (repository == null) {
            /*TODO: 404 */
        }

        Date limit = new Date();
        limit.setTime(limit.getTime() - (maxNumberOfDays * 24 * 60 * 60 * 1000));
//        List<Change> changes = changeDao.findAllChangesUntil(repository, includeUpdates, limit, maxNumberOfEntries);
        List<Change> changes = changeDao.findAllChanges(repository, true, 0, 50);

        StringBuilder basepath = new StringBuilder();
        basepath.append(req.getScheme()).append("://").append(req.getServerName());
        if (req.getServerPort() != 80) {
            basepath.append(':').append(req.getServerPort());
        }
        basepath.append(req.getContextPath()).append("/");
        
        /*TODO: This one will require JDK6...*/
        /*TODO: Strongly requires some refactorization! */
        
        DateFormat dateFormatYmd = new SimpleDateFormat("yyyy-MM-dd", resp.getLocale());
        DateFormat dateFormatTimestamp = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        
        resp.setContentType("application/xml");
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        XMLStreamWriter xw = xof.createXMLStreamWriter(resp.getWriter());
        xw.writeStartDocument();
        xw.writeStartElement("rss");
        {
            xw.writeAttribute("version", "2.0");
            xw.writeStartElement("channel");
            {
                xw.writeStartElement("title");
                xw.writeCharacters("rss.title");
                xw.writeEndElement();
                
                xw.writeStartElement("link");
                xw.writeCharacters("${basepath}");
                xw.writeEndElement();
                
                xw.writeStartElement("description");
                xw.writeCharacters(MessageFormat.format(
                        "rss.description",
                        domain.getName(),
                        domain.getRelease(),
                        repository.getName(),
                        repository.getArchitecture()
                ));
                xw.writeEndElement();
                
                xw.writeStartElement("language");
                xw.writeCharacters(resp.getLocale().getLanguage());
                xw.writeEndElement();
                
                for (Change change : changes) {
                    xw.writeStartElement("item");
                    {
                        /*TODO: Use MessageFormat! */
                        StringBuilder title = new StringBuilder();
                        title.append(dateFormatYmd.format(change.getTimestamp()));
                        if (change.getChange().equals("ADDED")) {
                            title.append("rss.added");
                        } else if (change.getChange().equals("REMOVED")) {
                            title.append("rss.removed");
                        } else {
                            title.append("rss.updated");
                        }
                        title.append(change.getPackage().getName());
                        
                        xw.writeStartElement("title");
                        xw.writeCharacters(title.toString());
                        xw.writeEndElement();
                        
                        StringBuilder link = new StringBuilder();
                        link.append(basepath);
                        link.append("package/");
                        link.append(domain.getName()).append('/');
                        link.append(domain.getRelease()).append('/');
                        link.append(change.getPackage().getName());
                        link.append(".html");
                        
                        xw.writeStartElement("link");
                        xw.writeCharacters(link.toString());
                        xw.writeEndElement();
                        
                        xw.writeStartElement("description");
                        xw.writeCharacters(change.getPackage().getSummary());
                        xw.writeEndElement();
                        
                        xw.writeStartElement("pubDate");
                        xw.writeCharacters(dateFormatTimestamp.format(change.getTimestamp()));
                        xw.writeEndElement();
                        
                        StringBuilder guid = new StringBuilder();
                        guid.append(basepath);
                        guid.append("change-").append(change.getId()).append("-guid");
                        xw.writeStartElement("guid");
                        xw.writeAttribute("isPermaLink", "false");
                        xw.writeCharacters(guid.toString());
                        xw.writeEndElement();
                    }
                    xw.writeEndElement(); // item
                }
            }
            xw.writeEndElement(); // channel
        }
        xw.writeEndElement(); // rss
        xw.writeEndDocument();
        xw.flush();

    }

}
