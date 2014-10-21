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
import uk.gov.homeofficedigital.swappr.model.ShiftType;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/shifts")
public class ShiftController {

    private final ShiftDao shiftDao;
    private final RotaDao rotaDao;
    private final UserHelper userHelper = new UserHelper();

    public ShiftController(ShiftDao shiftDao, RotaDao rotaDao) {
        this.shiftDao = shiftDao;
        this.rotaDao = rotaDao;
    }


    @ModelAttribute("availableShifts")
    public LinkedHashMap<String, String> availableShifts() {
        return Stream.of(ShiftType.values())
                .collect(
                        Collectors.toMap(
                                ShiftType::name,
                                st -> st.name() + " - " + st.getLabel().name(),
                                (a, b) -> a,
                                LinkedHashMap::new));

    }

    @ModelAttribute("availableMonths")
    public LinkedHashMap<Integer, String> availableMonths() {
        Month thisMonth = LocalDate.now().getMonth();
        return Stream.of(thisMonth, thisMonth.plus(1), thisMonth.plus(2))
                .collect(
                        Collectors.toMap(
                                Month::getValue,
                                month -> new DateDisplay().month(month),
                                (a, b) -> a,
                                LinkedHashMap::new));
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
            rotaDao.create(userHelper.userFromPrincipal(principal), shift);
        }

        return "redirect:/";
    }
}
