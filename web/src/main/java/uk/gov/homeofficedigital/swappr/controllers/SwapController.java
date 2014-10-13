package uk.gov.homeofficedigital.swappr.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.controllers.forms.SwapForm;
import uk.gov.homeofficedigital.swappr.model.ShiftType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.stream.Stream;

@Controller
@RequestMapping("/swap")
public class SwapController {

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String view(Model model) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        SwapForm form = new SwapForm();
        form.setFromDay(tomorrow.getDayOfMonth());
        form.setFromMonth(tomorrow.getMonthValue());
        form.setFromYear(tomorrow.getYear());
        form.setToDay(tomorrow.getDayOfMonth());
        form.setToMonth(tomorrow.getMonthValue());
        form.setToYear(tomorrow.getYear());
        model.addAttribute("swap", form);

        model.addAttribute("shifts", Stream.of(ShiftType.values()).collect(
                HashMap::new,
                (map, shift) -> map.put(shift.name(), shift.name()),
                (mapA, mapB) -> mapA.putAll(mapB)
                ));
        return "createSwap";
    }
}
