package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uk.gov.homeofficedigital.swappr.controllers.forms.SwapForm;
import uk.gov.homeofficedigital.swappr.daos.OfferDao;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.daos.ShiftDao;
import uk.gov.homeofficedigital.swappr.model.Offer;
import uk.gov.homeofficedigital.swappr.model.Rota;
import uk.gov.homeofficedigital.swappr.model.Shift;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.service.RotaService;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
@RequestMapping("/swap")
public class SwapController {

    private final ShiftDao shiftDao;
    private final RotaDao rotaDao;
    private final RotaService rotaService;
    private final OfferDao offerDao;
    private final UserHelper userHelper = new UserHelper();

    public SwapController(ShiftDao shiftDao, RotaDao rotaDao, RotaService rotaService, OfferDao offerDao) {
        this.shiftDao = shiftDao;
        this.rotaDao = rotaDao;
        this.rotaService = rotaService;
        this.offerDao = offerDao;
    }

    @RequestMapping(value = "/{rotaId}", method = RequestMethod.GET)
    public String view(@PathVariable Long rotaId, Model model) {

        Rota rota = rotaDao.findById(rotaId).orElseThrow(RotaNotFoundException::new);

        LocalDate dateToSwap = rota.getShift().getDate();

        SwapForm form = new SwapForm();
        form.setFromDay(dateToSwap.getDayOfMonth());
        form.setFromMonth(dateToSwap.getMonthValue());
        form.setFromYear(dateToSwap.getYear());
        form.setToDay(dateToSwap.getDayOfMonth());
        form.setToMonth(dateToSwap.getMonthValue());
        form.setToYear(dateToSwap.getYear());
        model.addAttribute("swap", form);

        model.addAttribute("shifts", Stream.of(ShiftType.values()).collect(
                HashMap::new,
                (map, shift) -> map.put(shift.name(), shift.name()),
                (mapA, mapB) -> mapA.putAll(mapB)
        ));
        return "createSwap";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute("swap") SwapForm swap, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            return "createSwap";
        }

        User user = userHelper.userFromPrincipal(principal);
        Shift from = shiftDao.create(swap.getFromDate(), swap.getFromShiftType());
        Shift to = shiftDao.create(swap.getToDate(), swap.getToShiftType());
        Rota rota = rotaDao.create(user, from);

        rotaService.requestSwap(rota, to);

        return "redirect:/";
    }

    @RequestMapping(value = "/{id}/volunteer", method = RequestMethod.GET)
    public String volunteer(@RequestParam Long offerId, Principal principal) {
        Optional<Offer> oOffer = offerDao.findById(offerId);
        if (!oOffer.isPresent()) {
            throw new OfferNotFoundException();
        }

        Offer offer = oOffer.get();

        User user = userHelper.userFromPrincipal(principal);

        Rota myRota = rotaDao.create(user, offer.getSwapTo());

        rotaService.volunteerSwap(myRota, offer);

        return "redirect:/";
    }

}