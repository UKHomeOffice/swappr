package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import uk.gov.homeofficedigital.swappr.controllers.forms.ShiftForm;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.model.Role;
import uk.gov.homeofficedigital.swappr.model.Shift;
import uk.gov.homeofficedigital.swappr.model.ShiftType;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ShiftControllerTest {

    private RotaDao rotaDao = mock(RotaDao.class);
    private User user = new User("Bob", "password", Arrays.asList(new SimpleGrantedAuthority(Role.USER.name())));
    private UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(user, null);
    private BindingResult bindingResult = mock(BindingResult.class);

    ShiftController shiftController = new ShiftController(rotaDao, new ControllerHelper());

    @Before
    public void setUp() {
        reset(rotaDao, bindingResult);
    }

    @Test
    public void createShiftShouldCreateAShiftAndRotaForEveryDayBetweenFromAndToInclusive() {
        ShiftForm form = new ShiftForm(Clock.fixed(LocalDateTime.of(2014, 8, 23, 12, 45).toInstant(ZoneOffset.UTC), ZoneId.systemDefault()));
        form.setType(ShiftType.B1H);
        form.setFromDay(23);
        form.setFromMonth(8);
        form.setToDay(25);
        form.setToMonth(8);

        when(bindingResult.hasErrors()).thenReturn(false);
        RedirectAttributesModelMap attrs = new RedirectAttributesModelMap();
        shiftController.createShift(form, bindingResult, principal, attrs);

        verify(rotaDao, times(3)).create(eq(user), any(Shift.class));
        verifyNoMoreInteractions(rotaDao);
        assertEquals("addShift", attrs.getFlashAttributes().get("flashType"));
    }

    @Test
    public void startNewShift_shouldPopulateShiftFormWithDate_givenADateIsSupplied() throws Exception {
        ExtendedModelMap model = new ExtendedModelMap();

        LocalDate now = LocalDate.now();
        shiftController.startNewShift(now.format(DateTimeFormatter.ISO_DATE), model);

        assertTrue(model.containsKey("shift"));
        ShiftForm form = (ShiftForm) model.get("shift");
        assertEquals(now.getDayOfMonth(), form.getFromDay().intValue());
        assertEquals(now.getMonthValue(), form.getFromMonth().intValue());
    }

}