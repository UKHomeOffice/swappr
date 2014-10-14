package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import uk.gov.homeofficedigital.swappr.controllers.forms.SwapForm;
import uk.gov.homeofficedigital.swappr.daos.SwapDao;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.model.Swap;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SwapControllerTest {
    private SwapDao swapDao = mock(SwapDao.class);
    private SwapController controller = new SwapController(swapDao);

    @Test
    public void view_shouldDisplayTheCreateSwapTemplate() throws Exception {
        Model model = new ExtendedModelMap();

        String viewName = controller.view(model);

        assertEquals("createSwap", viewName);
        assertNotNull(model.containsAttribute("swap"));
    }

    @Test
    public void create_shouldCreateASwap_givenAValidSwapForm() throws Exception {
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
        when(principal.getPrincipal()).thenReturn(new User("Trevor", "its a secret", Collections.emptyList()));
        String target = controller.add(swapForm, new BeanPropertyBindingResult(swapForm, "swap"), principal);

        ArgumentCaptor<Swap> captor = ArgumentCaptor.forClass(Swap.class);
        verify(swapDao).createSwap(captor.capture());

        assertNotNull(captor.getValue());
        Swap actual = captor.getValue();
        assertEquals("Trevor", captor.getValue().getUsername());
        assertEquals(LocalDate.parse("2013-07-10"), actual.getFromDate());
        assertEquals(ShiftType.Earlies, actual.getFromShift());
        assertEquals(LocalDate.parse("2014-08-11"), actual.getToDate());
        assertEquals(ShiftType.Lates, actual.getToShift());
        assertEquals("redirect:/", target);
    }
}