package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.model.DayView;
import uk.gov.homeofficedigital.swappr.model.RotaView;
import uk.gov.homeofficedigital.swappr.service.RotaService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Controller
@RequestMapping("/")
public class HomeController {

    private final RotaService rotaService;

    public HomeController(RotaService rotaService) {
        this.rotaService = rotaService;
    }

    private User userFromPrincipal(Principal principal) {
        return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
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

        User user = userFromPrincipal(principal);
        Map<Month, List<DayView>> rotasByMonth = daysByMonth(rotaService.findMyRotas(user));

        model.addAttribute("rotasByMonth", rotasByMonth);
        return "home";
    }

    private Map<Month, List<RotaView>> collectRotasByMonth(Set<RotaView> rotas) {
        List<RotaView> rotaList = new ArrayList<>(rotas);
        rotaList.sort((a, b) -> a.getRota().getShift().getDate().compareTo(b.getRota().getShift().getDate()));
        return rotaList.stream().collect(groupingBy(rv -> rv.getRota().getShift().getDate().getMonth()));
    }

    @RequestMapping(value = "/timeline", method = RequestMethod.GET)
    public String showTimeline(Model model) {

        Map<Month, List<RotaView>> allRotasByMonth = collectRotasByMonth(rotaService.findAllRotas());
        model.addAttribute("rotasByMonth", allRotasByMonth);

        return "timeline";
    }
}
