package org.shredzone.repowatch.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.repository.PackageDAO;
import org.shredzone.repowatch.repository.RepositoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RepositoryListController {
    
    @Autowired
    private RepositoryDAO repositoryDao;
    
    @Autowired
    private PackageDAO packageDao;
    
    @RequestMapping("/repo.html")
    public ModelAndView repoHandler() {
        ModelAndView mav = new ModelAndView("listrepositories");
        
        List<Repository> repositories = repositoryDao.findAllRepositories();

        Map<Repository,Long> counter = new HashMap<Repository,Long>();
        for (Repository repo : repositories) {
            counter.put(repo, packageDao.countPackages(repo));
        }
        
        mav.addObject(repositories);
        mav.addObject("counterMap", counter);
        return mav;
    }
    

}
