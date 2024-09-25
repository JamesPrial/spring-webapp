package jpja.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jpja.webapp.daos.TestDao;

@Controller
public class ApplicationController {

    @Autowired
    private TestDao testDao;

    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    @ResponseBody
    public String index(){
        return "hello world";
    }

    @RequestMapping(value="/home.html", method = RequestMethod.GET)
    public String home(ModelMap modelMap){
        modelMap.addAttribute("name", testDao.getFirstName());
        return "home.html";
    }
}
