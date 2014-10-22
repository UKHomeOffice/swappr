package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import uk.gov.homeofficedigital.swappr.controllers.forms.ShiftForm;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.daos.ShiftDao;
import uk.gov.homeofficedigital.swappr.model.Role;
import uk.gov.homeofficedigital.swappr.model.Shift;
import uk.gov.homeofficedigital.swappr.model.ShiftType;

import java.time.*;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class ShiftControllerTest {

    private ShiftDao shiftDao = mock(ShiftDao.class);
    private RotaDao rotaDao = mock(RotaDao.class);
    private User user = new User("Bob", "password", Arrays.asList(new SimpleGrantedAuthority(Role.USER.name())));
    private UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(user, null);
    private BindingResult bindingResult = mock(BindingResult.class);

    ShiftController shiftController = new ShiftController(shiftDao, rotaDao);

    @Before
    public void setUp() {
        reset(shiftDao, rotaDao, bindingResult);
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
        shiftController.createShift(form, bindingResult, principal);

        verify(shiftDao).create(LocalDate.of(2014, 8, 23), ShiftType.B1H);
        verify(shiftDao).create(LocalDate.of(2014, 8, 24), ShiftType.B1H);
        verify(shiftDao).create(LocalDate.of(2014, 8, 25), ShiftType.B1H);
        verifyNoMoreInteractions(shiftDao);

        verify(rotaDao, times(3)).create(eq(user), any(Shift.class));
        verifyNoMoreInteractions(rotaDao);
    }


}