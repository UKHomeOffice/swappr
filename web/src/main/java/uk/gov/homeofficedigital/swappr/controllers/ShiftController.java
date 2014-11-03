package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.homeofficedigital.swappr.controllers.forms.ShiftForm;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.model.Shift;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;

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
    public String createShift(@Valid @ModelAttribute("shift") ShiftForm shiftForm, BindingResult result, SwapprUser user, RedirectAttributes attrs) {

        if (result.hasErrors()) {
            return "startShift";
        }
        try {
            Shift from = new Shift(shiftForm.getFromDate().get(), shiftForm.getType());
            Shift to = new Shift(shiftForm.getToDate().get(), shiftForm.getType());
            rotaDao.create(user, from, to);
        } catch (DuplicateKeyException ex) {
            result.addError(new ObjectError("shift", "You cannot work multiple shifts on the same day"));
            return "startShift";
        }

        attrs.addFlashAttribute("flashType", "addShift");
        return "redirect:/";
    }
}
