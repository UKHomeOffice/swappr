package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import uk.gov.homeofficedigital.swappr.controllers.forms.ShiftForm;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.model.Shift;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;
import uk.gov.homeofficedigital.swappr.model.UserMaker;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class ShiftControllerTest {

    private RotaDao rotaDao = mock(RotaDao.class);
    private SwapprUser user = make(a(UserMaker.User));
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

        verify(rotaDao, times(1)).create(user, new Shift(LocalDate.of(2014, 8, 23), ShiftType.B1H), new Shift(LocalDate.of(2014,8,25), ShiftType.B1H));
        verifyNoMoreInteractions(rotaDao);
        assertEquals("addShift", attrs.getFlashAttributes().get("flashType"));
    }

    @Test
    public void createShift_shouldShowAnError_givenADuplicateKeyExceptionIsThrownByTheRepo() {
        ShiftForm form = new ShiftForm(Clock.fixed(LocalDateTime.of(2014, 8, 23, 12, 45).toInstant(ZoneOffset.UTC), ZoneId.systemDefault()));
        form.setType(ShiftType.B1H);
        form.setFromDay(23);
        form.setFromMonth(8);
        form.setToDay(25);
        form.setToMonth(8);

        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new DuplicateKeyException("Duplicate")).when(rotaDao).create(user, new Shift(LocalDate.of(2014, 8, 23), ShiftType.B1H), new Shift(LocalDate.of(2014,8,25), ShiftType.B1H));
        RedirectAttributesModelMap attrs = new RedirectAttributesModelMap();

        String target = shiftController.createShift(form, bindingResult, principal, attrs);

        assertEquals("startShift", target);
        verify(bindingResult).addError(isA(ObjectError.class));
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