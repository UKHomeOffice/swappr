package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import uk.gov.homeofficedigital.swappr.controllers.views.OfferView;
import uk.gov.homeofficedigital.swappr.controllers.views.RotaView;
import uk.gov.homeofficedigital.swappr.model.*;
import uk.gov.homeofficedigital.swappr.service.RotaService;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TimelineControllerTest {

    private SwapprUser user = make(a(UserMaker.User));
    private RotaService rotaService = mock(RotaService.class);
    private TimelineController controller = new TimelineController(rotaService);

    @Test
    public void offersByMonthInModelShouldBeEmptyWhenThereAreNoSwaps() {

        when(rotaService.findAllOffers()).thenReturn(new HashSet<>());

        Model model = new BindingAwareModelMap();

        String viewName = controller.showTimeline(model);

        assertThat(viewName, equalTo("timeline"));

        Map<Month, List<OfferView>> offersByMonth = (Map<Month, List<OfferView>>) model.asMap().get("offersByMonth");
        assertThat(offersByMonth.keySet(), hasSize(0));
    }

    @Test
    public void offersByMonthInModelShouldContainAllOffersGroupedByMonth() {
        Set<OfferView> foundOffers = new HashSet<>();
        LocalDate today = LocalDate.now();
        OfferView offerThisMonth = new OfferView(make(a(OfferMaker.Offer, with(OfferMaker.rota, new Rota(45l, user, new Shift(today, ShiftType.B1H))))), new HashSet<>());
        OfferView offer1NextMonth = new OfferView(make(a(OfferMaker.Offer, with(OfferMaker.rota, new Rota(46l, user, new Shift(today.plusMonths(1), ShiftType.B1H))))), new HashSet<>());
        OfferView offer2NextMonth = new OfferView(make(a(OfferMaker.Offer, with(OfferMaker.rota, new Rota(47l, user, new Shift(today.plusMonths(1), ShiftType.B1H))))), new HashSet<>());
        foundOffers.add(offerThisMonth);
        foundOffers.add(offer1NextMonth);
        foundOffers.add(offer2NextMonth);
        when(rotaService.findAllOffers()).thenReturn(foundOffers);

        Model model = new BindingAwareModelMap();

        controller.showTimeline(model);

        Map<Month, List<OfferView>> offersByMonth = (Map<Month, List<OfferView>>) model.asMap().get("offersByMonth");
        assertThat(offersByMonth.keySet(), hasSize(2));

        List<OfferView> rotasThisMonth = offersByMonth.get(today.getMonth());
        assertThat(rotasThisMonth, hasSize(1));
        assertThat(rotasThisMonth, hasItem(offerThisMonth));

        List<OfferView> rotasNextMonth = offersByMonth.get(today.plusMonths(1).getMonth());
        assertThat(rotasNextMonth, hasSize(2));
        assertThat(rotasNextMonth, hasItem(offer1NextMonth));
        assertThat(rotasNextMonth, hasItem(offer2NextMonth));
    }

}