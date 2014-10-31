package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.controllers.views.DayView;
import uk.gov.homeofficedigital.swappr.controllers.views.RotaView;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;
import uk.gov.homeofficedigital.swappr.service.RotaService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Controller
@RequestMapping("/")
public class HomeController {

    private final RotaService rotaService;
    private final ControllerHelper helper;

    public HomeController(RotaService rotaService, ControllerHelper helper) {
        this.rotaService = rotaService;
        this.helper = helper;
    }

    private LinkedHashMap<Month, List<DayView>> daysByMonth(Set<RotaView> rotaViews) {
        LocalDate today = LocalDate.now();

        LinkedHashMap<Month, List<DayView>> daysByMonth = new LinkedHashMap<>();
        for (LocalDate day = today; day.isBefore(today.plusMonths(2)); day = day.plusDays(1)) {
            List<DayView> daysInMonth = daysByMonth.getOrDefault(day.getMonth(), new ArrayList<>());
            final LocalDate finalDay = day;
            DayView dayView = new DayView(day, rotaViews.stream().filter(rv -> rv.getRota().getShift().getDate().equals(finalDay)).findFirst());
            daysInMonth.add(dayView);
            daysByMonth.put(day.getMonth(), daysInMonth);
        }
        return daysByMonth;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showHomePage(Model model, Principal principal) {

        SwapprUser user = helper.userFromPrincipal(principal);
        Map<Month, List<DayView>> rotasByMonth = daysByMonth(rotaService.findMyRotas(user));

        model.addAttribute("rotasByMonth", rotasByMonth);
        return "home";
    }

}
