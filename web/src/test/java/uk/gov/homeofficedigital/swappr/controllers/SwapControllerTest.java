package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import uk.gov.homeofficedigital.swappr.controllers.exceptions.RotaNotFoundException;
import uk.gov.homeofficedigital.swappr.controllers.forms.SwapForm;
import uk.gov.homeofficedigital.swappr.daos.OfferDao;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.daos.VolunteerDao;
import uk.gov.homeofficedigital.swappr.model.*;
import uk.gov.homeofficedigital.swappr.service.RotaService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SwapControllerTest {

    private RotaDao rotaDao = mock(RotaDao.class);
    private RotaService rotaService = mock(RotaService.class);
    private OfferDao offerDao = mock(OfferDao.class);
    private VolunteerDao volunteerDao = mock(VolunteerDao.class);
    private ControllerHelper helper = mock(ControllerHelper.class);
    private SwapprUser user = make(a(UserMaker.User));
    private UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(user, null);
    private SwapController swapController = new SwapController(rotaDao, rotaService, offerDao, volunteerDao, helper);

    @Test(expected = RotaNotFoundException.class)
    public void view_shouldThrowAnExceptionIfTheRotaCannotBeFound() {

        when(rotaDao.findById(21l)).thenReturn(Optional.empty());

        swapController.view(21l, new BindingAwareModelMap());
    }

    @Test
    public void view_shouldPopulateModelWithTheGivenRotasShift() {

        Rota rota = make(a(RotaMaker.Rota));
        when(rotaDao.findById(22l)).thenReturn(Optional.of(rota));

        Model model = new BindingAwareModelMap();
        String viewName = swapController.view(22l, model);

        assertEquals(viewName, "createSwap");

        SwapForm form = (SwapForm) model.asMap().get("swap");
        assertEquals(form.getFromDate(), rota.getShift().getDate());
        assertEquals(form.getFromShiftType(), rota.getShift().getType());
        assertEquals(form.getToDate(), rota.getShift().getDate());
    }

    @Test
    public void add_shouldRedisplayTheFormWhenThereAreBindingErrors() {

        BindingResult bindingResult = new MapBindingResult(new HashMap<String, String>(), "swapForm");
        bindingResult.addError(new ObjectError("swapForm", "whoops"));
        SwapForm form = new SwapForm();
        String viewName = swapController.add(form, bindingResult, principal, new RedirectAttributesModelMap());
        assertEquals(viewName, "createSwap");
    }

    @Ignore // TODO: Complete test implementation
    public void add_shouldDelegateToRotaService() {

        SwapForm form = new SwapForm();
        BindingResult bindingResult = new MapBindingResult(new HashMap<String, String>(), "swapForm");

        LocalDate now = LocalDate.now();
        Rota createdRota = new Rota(44l, user, new Shift(now, ShiftType.B1H));
//        when(rotaDao.findOrCreate(user, new Shift())).thenReturn(createdRota);

        swapController.add(form, bindingResult, principal, new RedirectAttributesModelMap());

//        verify(rotaService).requestSwap(createdRota, );
    }


    @Test
    public void acceptSwap_shouldFindTheVolunteerAndDelegateToTheRotaService() throws Exception {
        Volunteer volunteer = make(a(VolunteerMaker.Volunteer));
        when(volunteerDao.findById(volunteer.getId())).thenReturn(Optional.of(volunteer));

        RedirectAttributesModelMap attrs = new RedirectAttributesModelMap();
        String target = swapController.acceptVolunteer(volunteer.getSwapTo().getId(), volunteer.getId(), attrs);

        assertEquals("redirect:/", target);
        verify(rotaService).acceptVolunteer(volunteer);
        assertEquals("acceptSwap", attrs.getFlashAttributes().get("flashType"));
    }
}