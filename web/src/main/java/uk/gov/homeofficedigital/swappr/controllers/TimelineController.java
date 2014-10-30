package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.controllers.views.OfferView;
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

    private Map<Month, List<OfferView>> collectOffersByMonth(Set<OfferView> offers) {
        List<OfferView> offerList = new ArrayList<>(offers);
        offerList.sort((a, b) -> a.getOffer().getSwapFrom().getShift().getDate().compareTo(b.getOffer().getSwapFrom().getShift().getDate()));
        return offerList.stream().collect(groupingBy(rv -> rv.getOffer().getSwapFrom().getShift().getDate().getMonth()));
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showTimeline(Model model) {

        Map<Month, List<OfferView>> allOffersByMonth = collectOffersByMonth(rotaService.findAllOffers());
        model.addAttribute("offersByMonth", allOffersByMonth);

        return "timeline";
    }

}
