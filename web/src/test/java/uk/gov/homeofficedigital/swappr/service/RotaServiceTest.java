package uk.gov.homeofficedigital.swappr.service;

import org.junit.Before;
import org.junit.Test;
import uk.gov.homeofficedigital.swappr.daos.OfferDao;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.daos.VolunteerDao;
import uk.gov.homeofficedigital.swappr.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.mockito.Mockito.*;

public class RotaServiceTest {

    RotaDao rotaDao = mock(RotaDao.class);
    OfferDao offerDao = mock(OfferDao.class);
    VolunteerDao volunteerDao = mock(VolunteerDao.class);
    private RotaService service = new RotaService(rotaDao, offerDao, volunteerDao);

    @Before
    public void setUp() {
        reset(rotaDao, offerDao, volunteerDao);
    }

    @Test
    public void requestSwapShouldCreateOfferWithStatusOfCreated() {
        Rota rota = make(a(RotaMaker.Rota));
        Shift shift = new Shift(LocalDate.now(), ShiftType.B1H);
        service.requestSwap(rota, shift);

        verify(offerDao).create(rota, shift, OfferStatus.CREATED);
    }

    @Test
    public void volunteerSwapShouldCreateVolunteerWithStatusOfCreated() {
        Rota rota = make(a(RotaMaker.Rota));
        Offer offer = make(a(OfferMaker.Offer));
        service.volunteerSwap(rota, offer);

        verify(volunteerDao).create(rota, offer, VolunteerStatus.CREATED);
    }

    @Test
    public void acceptVolunteerShouldUpdateTheVolunteerStatusToAccepted() {
        Volunteer volunteer = make(a(VolunteerMaker.Volunteer));
        service.acceptVolunteer(volunteer);

        verify(volunteerDao).updateStatus(volunteer, VolunteerStatus.ACCEPTED);
    }

    @Test
    public void acceptVolunteerShouldUpdateTheOfferStatusToAccepted() {
        Volunteer volunteer = make(a(VolunteerMaker.Volunteer));
        service.acceptVolunteer(volunteer);

        verify(offerDao).updateStatus(volunteer.getSwapTo(), OfferStatus.ACCEPTED);
    }

    @Test
    public void acceptVolunteerShouldUpdateAllOtherVolunteersStatusesToRejected() {
        Volunteer volunteer = make(a(VolunteerMaker.Volunteer));
        Volunteer volunteer2 = make(a(VolunteerMaker.Volunteer));
        Volunteer volunteer3 = make(a(VolunteerMaker.Volunteer));


        when(volunteerDao.findByOffer(volunteer.getSwapTo())).thenReturn(new HashSet<Volunteer>(Arrays.asList(volunteer, volunteer2, volunteer3)));

        service.acceptVolunteer(volunteer);

        verify(volunteerDao).updateStatus(volunteer2, VolunteerStatus.REJECTED);
        verify(volunteerDao).updateStatus(volunteer3, VolunteerStatus.REJECTED);
        verify(volunteerDao, never()).updateStatus(volunteer, VolunteerStatus.REJECTED);
    }
}