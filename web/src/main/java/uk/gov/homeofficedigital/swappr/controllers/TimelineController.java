package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.controllers.views.RotaView;
import uk.gov.homeofficedigital.swappr.service.RotaService;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

@Controller
@RequestMapping("/timeline")
public class TimelineController {

    private final RotaService rotaService;

    public TimelineController(RotaService rotaService) {
        this.rotaService = rotaService;
    }

    private Map<Month, List<RotaView>> collectRotasByMonth(Set<RotaView> rotas) {
        List<RotaView> rotaList = new ArrayList<>(rotas);
        rotaList.sort((a, b) -> a.getRota().getShift().getDate().compareTo(b.getRota().getShift().getDate()));
        return rotaList.stream().collect(groupingBy(rv -> rv.getRota().getShift().getDate().getMonth()));
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showTimeline(Model model) {

        Map<Month, List<RotaView>> allRotasByMonth = collectRotasByMonth(rotaService.findAllRotas());
        model.addAttribute("rotasByMonth", allRotasByMonth);

        return "timeline";
    }

}
