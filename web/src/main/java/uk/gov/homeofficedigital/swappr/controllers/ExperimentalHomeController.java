package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.model.RotaView;
import uk.gov.homeofficedigital.swappr.service.RotaService;

import java.security.Principal;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class ExperimentalHomeController {

    private final RotaService rotaService;

    public ExperimentalHomeController(RotaService rotaService) {
        this.rotaService = rotaService;
    }

    private User userFromPrincipal(Principal principal) {
        return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showHomePage(Model model, Principal principal) {

        User user = userFromPrincipal(principal);
        Map<Month, List<RotaView>> rotasByMonth = collectRotasByMonth(rotaService.findMyRotas(user));

        model.addAttribute("rotasByMonth", rotasByMonth);
        return "experimentalHome";
    }

    private Map<Month, List<RotaView>> collectRotasByMonth(Set<RotaView> rotas) {
        List<RotaView> rotaList = new ArrayList<>(rotas);
        rotaList.sort((a, b) -> a.getRota().getShift().getDate().compareTo(b.getRota().getShift().getDate()));
        return rotaList.stream().collect(Collectors.groupingBy(rv -> rv.getRota().getShift().getDate().getMonth()));
    }

    @RequestMapping(value = "/timeline", method = RequestMethod.GET)
    public String showTimeline(Model model) {

        Map<Month, List<RotaView>> allRotasByMonth = collectRotasByMonth(rotaService.findAllRotas());
        model.addAttribute("rotasByMonth", allRotasByMonth);

        return "experimentalTimeline";
    }
}
