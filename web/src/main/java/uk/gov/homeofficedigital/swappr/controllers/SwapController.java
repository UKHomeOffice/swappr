package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.homeofficedigital.swappr.controllers.exceptions.OfferNotFoundException;
import uk.gov.homeofficedigital.swappr.controllers.exceptions.RotaNotFoundException;
import uk.gov.homeofficedigital.swappr.controllers.forms.SwapForm;
import uk.gov.homeofficedigital.swappr.controllers.views.OfferView;
import uk.gov.homeofficedigital.swappr.daos.OfferDao;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.model.Offer;
import uk.gov.homeofficedigital.swappr.model.Rota;
import uk.gov.homeofficedigital.swappr.model.Shift;
import uk.gov.homeofficedigital.swappr.service.RotaService;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.LinkedHashMap;

@Controller
@RequestMapping("/swap")
public class SwapController {

    private final RotaDao rotaDao;
    private final RotaService rotaService;
    private final OfferDao offerDao;
    private final ControllerHelper controllerHelper;

    public SwapController(RotaDao rotaDao, RotaService rotaService, OfferDao offerDao, ControllerHelper helper) {
        this.rotaDao = rotaDao;
        this.rotaService = rotaService;
        this.offerDao = offerDao;
        this.controllerHelper = helper;
    }

    @ModelAttribute("availableMonths")
    public LinkedHashMap<String, String> availableMonths() {
        return controllerHelper.availableMonths();
    }

    @ModelAttribute("availableShifts")
    public LinkedHashMap<String, String> availableShifts() {
        return controllerHelper.availableShifts();
    }


    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String view(@RequestParam("rotaId") Long rotaId, Model model) {
        Rota rota = rotaDao.findById(rotaId).orElseThrow(RotaNotFoundException::new);

        LocalDate dateToSwap = rota.getShift().getDate();

        SwapForm form = new SwapForm();
        form.setFromDay(dateToSwap.getDayOfMonth());
        form.setFromMonth(dateToSwap.getMonthValue());
        form.setFromYear(dateToSwap.getYear());
        form.setFromShiftType(rota.getShift().getType());
        form.setToDay(dateToSwap.getDayOfMonth());
        form.setToMonth(dateToSwap.getMonthValue());
        form.setToYear(dateToSwap.getYear());
        model.addAttribute("swap", form);

        return "createSwap";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute("swap") SwapForm swap, BindingResult result, Principal principal, RedirectAttributes attrs) {
        if (result.hasErrors()) {
            return "createSwap";
        }

        User user = controllerHelper.userFromPrincipal(principal);
        Shift from = new Shift(swap.getFromDate(), swap.getFromShiftType());
        Shift to = new Shift(swap.getToDate(), swap.getToShiftType());
        Rota rota = rotaDao.findOrCreate(user, from);

        rotaService.requestSwap(rota, to);

        attrs.addFlashAttribute("flashType", "createSwap");
        return "redirect:/";
    }

    @RequestMapping(value = "/{id}/volunteer", method = RequestMethod.POST)
    public String volunteer(@PathVariable("id") Long offerId, Principal principal, RedirectAttributes attrs) {

        Offer offer = offerDao.findById(offerId).orElseThrow(OfferNotFoundException::new);

        User user = controllerHelper.userFromPrincipal(principal);

        Rota myRota = rotaDao.findOrCreate(user, offer.getSwapTo());

        rotaService.volunteerSwap(myRota, offer);

        attrs.addFlashAttribute("flashType", "volunteerSwap");
        return "redirect:/";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String showSwap(@PathVariable("id") Long offerId, Model model) {
        OfferView offer = rotaService.getOffer(offerId).orElseThrow(() -> new RuntimeException("No offer for given id"));

        model.addAttribute("offer", offer);
        return "swapView";
    }
}