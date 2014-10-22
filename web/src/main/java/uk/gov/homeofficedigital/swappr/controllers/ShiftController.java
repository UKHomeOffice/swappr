package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.homeofficedigital.swappr.controllers.forms.ShiftForm;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.model.Shift;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.LinkedHashMap;

@Controller
@RequestMapping("/shifts")
public class ShiftController {

    private final RotaDao rotaDao;
    private final ControllerHelper controllerHelper;

    public ShiftController(RotaDao rotaDao, ControllerHelper helper) {
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
    public String startNewShift(@RequestParam(value = "shiftDate", required = false) String shiftDateString, Model model) {
        ShiftForm shiftForm = new ShiftForm();
        if (shiftDateString != null) {
            LocalDate shiftDate = LocalDate.parse(shiftDateString);
            shiftForm.setFromDay(shiftDate.getDayOfMonth());
            shiftForm.setFromMonth(shiftDate.getMonthValue());
            shiftForm.setToDay(shiftDate.getDayOfMonth());
            shiftForm.setToMonth(shiftDate.getMonthValue());
        }
        model.addAttribute("shift", shiftForm);
        return "startShift";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String createShift(@Valid @ModelAttribute("shift") ShiftForm shiftForm, BindingResult result, Principal principal, RedirectAttributes attrs) {

        if (result.hasErrors()) {
            return "startShift";
        }

        for (LocalDate date = shiftForm.getFromDate().get(); date.isBefore(shiftForm.getToDate().get().plusDays(1)); date = date.plusDays(1)) {
            Shift shift = new Shift(date, shiftForm.getType());
            rotaDao.create(controllerHelper.userFromPrincipal(principal), shift);
        }

        attrs.addFlashAttribute("flashType", "addShift");
        return "redirect:/";
    }
}
