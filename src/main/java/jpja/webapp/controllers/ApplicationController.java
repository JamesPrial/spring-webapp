package jpja.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jpja.webapp.daos.TestDao;

@Controller
public class ApplicationController {

    @Autowired
    private TestDao testDao;

    @RequestMapping(value="/home.html", method = RequestMethod.GET)
    public String home(){
        return "home.html";
    }

    @RequestMapping(value="/ex.html", method = RequestMethod.GET)
    public String ex(ModelMap modelMap){
        modelMap.addAttribute("name", testDao.getFirstName());
        return "ex.html";
    }
}
