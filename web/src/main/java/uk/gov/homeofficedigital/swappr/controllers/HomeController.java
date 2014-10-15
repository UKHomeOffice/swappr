package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.daos.SwapDao;
import uk.gov.homeofficedigital.swappr.model.Swap;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
        Map<String, List<Swap>> swapsByMonth = swaps.stream().collect(Collectors.groupingBy((swap) -> getMonthName(swap.getFromDate())));
        model.addAttribute("swaps", swapsByMonth);
        model.addAttribute("months", Arrays.asList(getMonthName(LocalDate.now()), getMonthName(LocalDate.now().plusMonths(1))));
        model.addAttribute("display", new DateDisplay());
        return "home";
    }

    public static class DateDisplay {
        public String month(LocalDate date) {
            return date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        }

        public String day(LocalDate date) {
            return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        }
    }

    private String getMonthName(LocalDate date) {
        return date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
}