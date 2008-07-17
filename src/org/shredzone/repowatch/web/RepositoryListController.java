package org.shredzone.repowatch.web;

import org.shredzone.repowatch.repository.RepositoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RepositoryListController {
    
    @Autowired
    private RepositoryDAO repositoryDao;
    
    @RequestMapping("/repo.html")
    public ModelAndView repoHandler() {
        ModelAndView mav = new ModelAndView("listrepositories");
        mav.addObject(repositoryDao.findAllRepositories());
        return mav;
    }
    

}
