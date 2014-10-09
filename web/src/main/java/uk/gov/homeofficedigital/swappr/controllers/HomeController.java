package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.daos.HomeDao;

@Controller
public class HomeController {

    private HomeDao homeDao;

    public HomeController(HomeDao homeDao) {
        this.homeDao = homeDao;
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("name", homeDao.name());
        return "home";
    }
}
