package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.daos.HomeDao;

import java.security.Principal;

@Controller
public class HomeController {

    private HomeDao homeDao;

    public HomeController(HomeDao homeDao) {
        this.homeDao = homeDao;
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String home(Model model, Principal user) {
        model.addAttribute("name", homeDao.name());
        model.addAttribute("user", user.getName());
        return "home";
    }
}
