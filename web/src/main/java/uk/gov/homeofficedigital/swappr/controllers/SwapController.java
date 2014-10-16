package uk.gov.homeofficedigital.swappr.controllers;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.controllers.forms.SwapForm;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.service.SwapService;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.stream.Stream;

@Controller
@RequestMapping("/swap")
public class SwapController {

    private final SwapService swapService;

    public SwapController(SwapService swapService) {
        this.swapService = swapService;
    }

    @RequestMapping(method= RequestMethod.GET)
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

    @RequestMapping(value="/create", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute("swap") SwapForm swap, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            return "createSwap";
        }
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
        User user = (User) auth.getPrincipal();
        swapService.offerSwap(user, swap.getFromDate(), swap.getFromShiftType(), swap.getToDate(), swap.getToShiftType());
        return "redirect:/";
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public String showSwap(Model model, @PathVariable int id) {
        model.addAttribute("swap", swapService.loadSwap(id));
        return "showSwap";
    }
}