package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.homeofficedigital.swappr.controllers.forms.SwapForm;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.model.Swap;
import uk.gov.homeofficedigital.swappr.model.SwapMaker;
import uk.gov.homeofficedigital.swappr.service.SwapService;

import java.time.LocalDate;
import java.util.Collections;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SwapControllerTest {

    private SwapService swapService = mock(SwapService.class);
    private SwapController controller = new SwapController(swapService);

    @Test
    public void view_shouldDisplayTheCreateSwapTemplate() throws Exception {
        Model model = new ExtendedModelMap();

        String viewName = controller.view(model);

        assertEquals("createSwap", viewName);
        assertNotNull(model.containsAttribute("swap"));
        assertTrue(model.containsAttribute("shifts"));
    }

    @Test
    public void add_shouldCreateASwap_givenAValidSwapForm() throws Exception {
        SwapForm swapForm = new SwapForm();
        swapForm.setFromDay(10);
        swapForm.setFromMonth(7);
        swapForm.setFromYear(2013);
        swapForm.setFromShiftType(ShiftType.Earlies);
        swapForm.setToDay(11);
        swapForm.setToMonth(8);
        swapForm.setToYear(2014);
        swapForm.setToShiftType(ShiftType.Lates);

        UsernamePasswordAuthenticationToken principal = mock(UsernamePasswordAuthenticationToken.class);
        User trevor = new User("Trevor", "its a secret", Collections.emptyList());
        when(principal.getPrincipal()).thenReturn(trevor);
        String target = controller.add(swapForm, new BeanPropertyBindingResult(swapForm, "swap"), principal);

        verify(swapService).offerSwap(trevor, LocalDate.parse("2013-07-10"), ShiftType.Earlies, LocalDate.parse("2014-08-11"), ShiftType.Lates);

        assertEquals("redirect:/", target);
    }

    @Test
    public void add_shouldRedisplayCreateSwap_givenAnInvalidSwap() throws Exception {
        SwapForm swapForm = new SwapForm();
        swapForm.setFromDay(10);
        swapForm.setFromMonth(14); // invalid month
        swapForm.setFromYear(2013);
        swapForm.setFromShiftType(ShiftType.Earlies);
        swapForm.setToDay(11);
        swapForm.setToMonth(8);
        swapForm.setToYear(2014);
        swapForm.setToShiftType(ShiftType.Lates);

        UsernamePasswordAuthenticationToken principal = mock(UsernamePasswordAuthenticationToken.class);
        when(principal.getPrincipal()).thenReturn(new User("Trevor", "its a secret", Collections.emptyList()));
        BeanPropertyBindingResult binding = new BeanPropertyBindingResult(swapForm, "swap");
        binding.rejectValue("toMonth", "broke");
        String target = controller.add(swapForm, binding, principal);

        assertEquals("createSwap", target);
    }

    @Test
    public void showSwap_shouldDisplayTheSelectedSwap_givenAValidSwapId() throws Exception {
        Swap swap = make(a(SwapMaker.Swap, with(SwapMaker.id, 1003L)));
        when(swapService.loadSwap(1003l)).thenReturn(swap);
        ExtendedModelMap model = new ExtendedModelMap();

        String viewName = controller.showSwap(model, 1003l);

        assertEquals("showSwap", viewName);
        assertTrue(model.containsAttribute("swap"));
        assertEquals(swap, model.get("swap"));
    }
}