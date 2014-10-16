package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.daos.SwapDao;
import uk.gov.homeofficedigital.swappr.model.Swap;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private SwapDao swapDao;

    public HomeController(SwapDao swapDao) {
        this.swapDao = swapDao;
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String home(Model model, Principal user) {
        List<Swap> swaps = swapDao.findSwapsForUser(user.getName());
        DateDisplay dateDisplay = new DateDisplay();
        Map<String, List<Swap>> swapsByMonth = swaps.stream().collect(Collectors.groupingBy((swap) -> dateDisplay.month(swap.getFromDate())));
        model.addAttribute("swaps", swapsByMonth);
        model.addAttribute("months", Arrays.asList(dateDisplay.month(LocalDate.now()), dateDisplay.month(LocalDate.now().plusMonths(1))));
        return "home";
    }

    @RequestMapping(value="/timeline", method=RequestMethod.GET)
    public String timeline(Model model, Principal user) {
        List<Swap> swaps = swapDao.findAllSwaps();
        DateDisplay dateDisplay = new DateDisplay();
        Map<String, List<Swap>> swapsByMonth = swaps.stream().collect(Collectors.groupingBy((swap) -> dateDisplay.month(swap.getFromDate())));
        model.addAttribute("swaps", swapsByMonth);
        model.addAttribute("months", Arrays.asList(dateDisplay.month(LocalDate.now()), dateDisplay.month(LocalDate.now().plusMonths(1))));
        return "timeline";
    }
}