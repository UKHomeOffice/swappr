package uk.gov.homeofficedigital.swappr.controllers.views;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.gov.homeofficedigital.swappr.model.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.Assert.*;

public class OfferViewTest {

    private SecurityContext ctx;

    @Before
    public void setup() throws Exception {
        ctx = SecurityContextHolder.getContext();
    }

    @After
    public void tearDown() throws Exception {
        SecurityContextHolder.setContext(ctx);
    }

    @Test
    public void isSameDaySwap_shouldReturnTrue_givenFromShiftDateAndToShiftDateAreTheSame() throws Exception {
        LocalDate toShift = LocalDate.now().plusDays(3);
        Rota rota = make(a(RotaMaker.Rota, with(RotaMaker.shift, new Shift(toShift, ShiftType.BFH))));
        Offer offer = make(an(OfferMaker.Offer,
                with(OfferMaker.rota, rota),
                with(OfferMaker.shift, new Shift(toShift, ShiftType.C1H))));

        OfferView view = new OfferView(offer, Collections.<Volunteer>emptySet());

        assertTrue(view.isSameDaySwap());
    }

    @Test
    public void isSameDaySwap_shouldReturnFalse_givenFromShiftDateAndToShiftDateAreDifferent() throws Exception {
        LocalDate toShift = LocalDate.now().plusDays(3);
        Rota rota = make(a(RotaMaker.Rota, with(RotaMaker.shift, new Shift(toShift, ShiftType.BFH))));
        Offer offer = make(an(OfferMaker.Offer,
                with(OfferMaker.rota, rota),
                with(OfferMaker.shift, new Shift(toShift.plusDays(1), ShiftType.C1H))));

        OfferView view = new OfferView(offer, Collections.<Volunteer>emptySet());

        assertFalse(view.isSameDaySwap());
    }

    @Test
    public void isOfferForCurrentUser_shouldBeTrue_givenCurrentUserOwnsTheAssociatedRota() throws Exception {
        SecurityContextHolder.setContext(new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return new UsernamePasswordAuthenticationToken((Principal) () -> "Fred", null);
            }

            @Override
            public void setAuthentication(Authentication authentication) {

            }
        });

        Offer offer = make(an(OfferMaker.Offer,
                with(OfferMaker.rota, make(a(RotaMaker.Rota,
                        with(RotaMaker.user, new SwapprUser("Fred", "abc", Collections.emptyList(), "Fred", "fred@mail.com")))))));
        OfferView offerView = new OfferView(offer, Collections.emptySet());

        assertTrue(offerView.isOfferForCurrentUser());
    }

    @Test
    public void isOfferForCurrentUser_shouldBeFalse_givenCurrentUserIsNotTheOwnerOfTheAssociatedRota() throws Exception {
        SecurityContextHolder.setContext(new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return new UsernamePasswordAuthenticationToken((Principal) () -> "Fred", null);
            }

            @Override
            public void setAuthentication(Authentication authentication) {

            }
        });

        Offer offer = make(an(OfferMaker.Offer,
                with(OfferMaker.rota, make(a(RotaMaker.Rota,
                        with(RotaMaker.user, new SwapprUser("Arthur", "abc", Collections.emptyList(), "Arthur", "a@mail.com")))))));
        OfferView offerView = new OfferView(offer, Collections.emptySet());

        assertFalse(offerView.isOfferForCurrentUser());
    }
}