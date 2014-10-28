package uk.gov.homeofficedigital.swappr.controllers.views;

import org.junit.Test;
import uk.gov.homeofficedigital.swappr.model.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class DayViewTest {

    @Test
    public void status_shouldBeNotWorking_givenNoRota() throws Exception {
        DayView view = new DayView(LocalDate.now(), Optional.empty());
        assertEquals(SwapStatus.NotWorking, view.getStatus());
    }

    @Test
    public void status_shouldBeWorking_givenRotaWithNoOfferOrSwaps() throws Exception {
        DayView view = new DayView(LocalDate.now(), Optional.of(new RotaView(make(a(RotaMaker.Rota)), Collections.emptySet(), Collections.emptySet())));
        assertEquals(SwapStatus.WorkingOnly, view.getStatus());
    }

    @Test
    public void status_shouldBeOfferAwaitingVolunteers_givenRotaWithAnOfferWithNoVolunteers() throws Exception {
        DayView view = new DayView(LocalDate.now(), Optional.of(new RotaView(make(a(RotaMaker.Rota)), new HashSet<OfferView>(asList(new OfferView(make(an(OfferMaker.Offer, with(OfferMaker.status, OfferStatus.CREATED))), Collections.emptySet()))), Collections.emptySet())));
        assertEquals(SwapStatus.OfferAwaitingVolunteers, view.getStatus());
    }

    @Test
    public void status_shouldBeOfferWithVolunteers_givenRotaWithAnOfferInCreatedStateWithVolunteers() throws Exception {
        DayView view = new DayView(LocalDate.now(),
                Optional.of(new RotaView(make(a(RotaMaker.Rota)),
                new HashSet<OfferView>(asList(new OfferView(make(an(OfferMaker.Offer, with(OfferMaker.status, OfferStatus.CREATED))),
                    new HashSet<Volunteer>(asList(make(a(VolunteerMaker.Volunteer)))))))
                , Collections.emptySet())));
        assertEquals(SwapStatus.OfferWithVolunteers, view.getStatus());
    }

    @Test
    public void status_shouldBeOfferAwaitingApproval_givenRotaWithAnOfferInAcceptedStateWithVolunteers() throws Exception {
        DayView view = new DayView(LocalDate.now(),
                Optional.of(new RotaView(make(a(RotaMaker.Rota)),
                new HashSet<OfferView>(asList(new OfferView(make(an(OfferMaker.Offer, with(OfferMaker.status, OfferStatus.ACCEPTED))),
                    new HashSet<Volunteer>(asList(make(a(VolunteerMaker.Volunteer, with(VolunteerMaker.status, VolunteerStatus.ACCEPTED))))))))
                , Collections.emptySet())));
        assertEquals(SwapStatus.OfferAwaitingApproval, view.getStatus());
    }

    @Test
    public void status_shouldBeOfferApproved_givenRotaWithAnOfferInApprovedStateWithVolunteers() throws Exception {
        DayView view = new DayView(LocalDate.now(),
                Optional.of(new RotaView(make(a(RotaMaker.Rota)),
                new HashSet<OfferView>(asList(new OfferView(make(an(OfferMaker.Offer, with(OfferMaker.status, OfferStatus.APPROVED))),
                    new HashSet<Volunteer>(asList(make(a(VolunteerMaker.Volunteer, with(VolunteerMaker.status, VolunteerStatus.APPROVED))))))))
                , Collections.emptySet())));
        assertEquals(SwapStatus.OfferApproved, view.getStatus());
    }

    @Test
    public void status_shouldBeOfferDenied_givenRotaWithAnOfferInDeniedStateWithVolunteers() throws Exception {
        DayView view = new DayView(LocalDate.now(),
                Optional.of(new RotaView(make(a(RotaMaker.Rota)),
                new HashSet<OfferView>(asList(new OfferView(make(an(OfferMaker.Offer, with(OfferMaker.status, OfferStatus.DENIED))),
                    new HashSet<Volunteer>(asList(make(a(VolunteerMaker.Volunteer, with(VolunteerMaker.status, VolunteerStatus.DENIED))))))))
                , Collections.emptySet())));
        assertEquals(SwapStatus.OfferDenied, view.getStatus());
    }

    @Test
    public void status_shouldBeVolunteered_givenRotaWithAVolunteer() throws Exception {
        DayView view = new DayView(LocalDate.now(),
                Optional.of(new RotaView(make(a(RotaMaker.Rota)),
                Collections.emptySet(),
                new HashSet(asList(make(a(VolunteerMaker.Volunteer, with(VolunteerMaker.status, VolunteerStatus.CREATED))))))));

        assertEquals(SwapStatus.Volunteered, view.getStatus());
    }

}