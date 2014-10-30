package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;

class ControllerHelper {

    SwapprUser userFromPrincipal(Principal principal) {
        return (SwapprUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }

    LinkedHashMap<String, String> availableMonths() {
        DateDisplay dateDisplay = new DateDisplay();
        Month thisMonth = LocalDate.now().getMonth();

        LinkedHashMap<String, String> months = new LinkedHashMap<>();
        months.put("", "Select month");
        months.put(String.valueOf(thisMonth.getValue()), dateDisplay.month(thisMonth));
        months.put(String.valueOf(thisMonth.plus(1).getValue()), dateDisplay.month(thisMonth.plus(1)));
        months.put(String.valueOf(thisMonth.plus(2).getValue()), dateDisplay.month(thisMonth.plus(2)));

        return months;
    }

    LinkedHashMap<String, String> availableShifts() {

        LinkedHashMap<String, String> shifts = new LinkedHashMap<>();
        shifts.put("", "Select shift");

        for (ShiftType shiftType : ShiftType.values()) {
            shifts.put(shiftType.name(), shiftType.name() + " - " + shiftType.getLabel().name());
        }

        return shifts;
    }

}