package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.controllers.forms.ShiftForm;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.daos.ShiftDao;
import uk.gov.homeofficedigital.swappr.model.Shift;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.LinkedHashMap;

@Controller
@RequestMapping("/shifts")
public class ShiftController {

    private final ShiftDao shiftDao;
    private final RotaDao rotaDao;
    private final ControllerHelper controllerHelper;

    public ShiftController(ShiftDao shiftDao, RotaDao rotaDao, ControllerHelper helper) {
        this.shiftDao = shiftDao;
        this.rotaDao = rotaDao;
        this.controllerHelper = helper;
    }


    @ModelAttribute("availableShifts")
    public LinkedHashMap<String, String> availableShifts() {
        return controllerHelper.availableShifts();
    }

    @ModelAttribute("availableMonths")
    public LinkedHashMap<String, String> availableMonths() {
        return controllerHelper.availableMonths();
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String startNewShift(Model model) {
        model.addAttribute("shift", new ShiftForm());
        return "startShift";
    }


    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String createShift(@Valid @ModelAttribute("shift") ShiftForm shiftForm, BindingResult result, Principal principal) {

        if (result.hasErrors()) {
            return "startShift";
        }

        for (LocalDate date = shiftForm.getFromDate().get(); date.isBefore(shiftForm.getToDate().get().plusDays(1)); date = date.plusDays(1)) {
            Shift shift = shiftDao.create(date, shiftForm.getType());
            rotaDao.create(controllerHelper.userFromPrincipal(principal), shift);
        }

        return "redirect:/";
    }
}
