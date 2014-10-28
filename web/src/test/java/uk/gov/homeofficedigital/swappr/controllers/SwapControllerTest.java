package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import uk.gov.homeofficedigital.swappr.daos.OfferDao;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.daos.VolunteerDao;
import uk.gov.homeofficedigital.swappr.model.Volunteer;
import uk.gov.homeofficedigital.swappr.model.VolunteerMaker;
import uk.gov.homeofficedigital.swappr.service.RotaService;

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
    private SwapController swapController = new SwapController(rotaDao, rotaService, offerDao, volunteerDao, helper);

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