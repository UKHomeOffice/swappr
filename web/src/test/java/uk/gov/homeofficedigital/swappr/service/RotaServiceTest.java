package uk.gov.homeofficedigital.swappr.service;

import org.junit.Before;
import org.junit.Test;
import uk.gov.homeofficedigital.swappr.controllers.views.OfferView;
import uk.gov.homeofficedigital.swappr.controllers.views.RotaView;
import uk.gov.homeofficedigital.swappr.daos.OfferDao;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.daos.VolunteerDao;
import uk.gov.homeofficedigital.swappr.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

public class RotaServiceTest {

    private RotaDao rotaDao = mock(RotaDao.class);
    private OfferDao offerDao = mock(OfferDao.class);
    private VolunteerDao volunteerDao = mock(VolunteerDao.class);
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


        when(volunteerDao.findActiveByOffer(volunteer.getSwapTo())).thenReturn(new HashSet<>(Arrays.asList(volunteer, volunteer2, volunteer3)));

        service.acceptVolunteer(volunteer);

        verify(volunteerDao).updateStatus(volunteer2, VolunteerStatus.REJECTED);
        verify(volunteerDao).updateStatus(volunteer3, VolunteerStatus.REJECTED);
        verify(volunteerDao, never()).updateStatus(volunteer, VolunteerStatus.REJECTED);
    }

    @Test
    public void rejectVolunteerShouldUpdateStatusToRejected() {
        Volunteer volunteer = make(a(VolunteerMaker.Volunteer));

        service.rejectVolunteer(volunteer);

        verify(volunteerDao).updateStatus(volunteer, VolunteerStatus.REJECTED);
    }

    @Test
    public void approveSwapShouldUpdateTheVolunteerStatusToApproved() {
        Volunteer volunteer = make(a(VolunteerMaker.Volunteer));
        service.approveSwap(volunteer);

        verify(volunteerDao).updateStatus(volunteer, VolunteerStatus.APPROVED);
    }

    @Test
    public void approveSwapShouldUpdateTheOfferStatusToApproved() {
        Volunteer volunteer = make(a(VolunteerMaker.Volunteer));
        service.approveSwap(volunteer);

        verify(offerDao).updateStatus(volunteer.getSwapTo(), OfferStatus.APPROVED);
    }

    @Test
    public void denySwapShouldUpdateTheVolunteerStatusToDenied() {
        Volunteer volunteer = make(a(VolunteerMaker.Volunteer));
        service.denySwap(volunteer);

        verify(volunteerDao).updateStatus(volunteer, VolunteerStatus.DENIED);
    }

    @Test
    public void denySwapShouldUpdateTheOfferStatusToDenied() {
        Volunteer volunteer = make(a(VolunteerMaker.Volunteer));
        service.denySwap(volunteer);

        verify(offerDao).updateStatus(volunteer.getSwapTo(), OfferStatus.DENIED);
    }

    @Test
    public void findMyRotasShouldCollateTheRotasOffersAndVolunteers() {
        SwapprUser user = make(a(UserMaker.User));
        Rota rota1 = make(a(RotaMaker.Rota));
        Rota rota2 = make(a(RotaMaker.Rota));
        when(rotaDao.findByWorker(user)).thenReturn(new HashSet<>(Arrays.asList(rota1, rota2)));
        Offer offer = make(a(OfferMaker.Offer));
        Set<Offer> rota1Offers = new HashSet<>(Arrays.asList(offer));
        Set<Volunteer> rota1Volunteers = new HashSet<>(Arrays.asList(make(a(VolunteerMaker.Volunteer))));
        Set<Volunteer> offerVolunteers = new HashSet<>(Arrays.asList(make(a(VolunteerMaker.Volunteer, with(VolunteerMaker.offer, offer)))));

        when(offerDao.findByRota(rota1)).thenReturn(new HashSet<>(rota1Offers));
        when(volunteerDao.findByRota(rota1)).thenReturn(new HashSet<>(rota1Volunteers));
        when(volunteerDao.findActiveByOffer(offer)).thenReturn(offerVolunteers);

        Set<RotaView> myRotas = service.findMyRotas(user);

        assertThat(myRotas, hasSize(2));

        assertThat(myRotas, hasItem(new RotaView(rota1, new HashSet<OfferView>(Arrays.asList(new OfferView(offer, offerVolunteers))), rota1Volunteers)));
        assertThat(myRotas, hasItem(new RotaView(rota2, new HashSet<>(), new HashSet<>())));
    }

    @Test
    public void findAllRotasShouldCollateTheRotasOffersAndVolunteers() {
        Rota rota1 = make(a(RotaMaker.Rota));
        Rota rota2 = make(a(RotaMaker.Rota));
        when(rotaDao.findAll()).thenReturn(new HashSet<>(Arrays.asList(rota1, rota2)));
        Offer offer = make(a(OfferMaker.Offer));
        Set<Offer> rota1Offers = new HashSet<>(Arrays.asList(offer));
        Set<Volunteer> rota1Volunteers = new HashSet<Volunteer>(Arrays.asList(make(a(VolunteerMaker.Volunteer))));
        Set<Volunteer> offerVolunteers = new HashSet<>(Arrays.asList(make(a(VolunteerMaker.Volunteer, with(VolunteerMaker.offer, offer)))));
        when(offerDao.findByRota(rota1)).thenReturn(new HashSet<>(rota1Offers));
        when(volunteerDao.findByRota(rota1)).thenReturn(new HashSet<>(rota1Volunteers));
        when(volunteerDao.findActiveByOffer(offer)).thenReturn(offerVolunteers);

        Set<RotaView> allRotas = service.findAllRotas();

        assertThat(allRotas, hasSize(2));
        assertThat(allRotas, hasItem(new RotaView(rota1, new HashSet<>(Arrays.asList(new OfferView(offer, offerVolunteers))), rota1Volunteers)));
        assertThat(allRotas, hasItem(new RotaView(rota2, new HashSet<>(), new HashSet<>())));
    }



}