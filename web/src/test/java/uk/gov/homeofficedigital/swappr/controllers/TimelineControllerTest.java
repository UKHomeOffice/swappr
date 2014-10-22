package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import uk.gov.homeofficedigital.swappr.controllers.views.RotaView;
import uk.gov.homeofficedigital.swappr.model.Role;
import uk.gov.homeofficedigital.swappr.model.Rota;
import uk.gov.homeofficedigital.swappr.model.Shift;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.service.RotaService;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TimelineControllerTest {

    private User user = new User("Bob", "password", Arrays.asList(new SimpleGrantedAuthority(Role.USER.name())));
    private RotaService rotaService = mock(RotaService.class);
    private TimelineController controller = new TimelineController(rotaService);

    @Test
    public void rotasByMonthInModelShouldBeEmptyWhenThereAreNoSwaps() {

        when(rotaService.findAllRotas()).thenReturn(new HashSet<>());

        Model model = new BindingAwareModelMap();

        String viewName = controller.showTimeline(model);

        assertThat(viewName, equalTo("timeline"));

        Map<Month, List<RotaView>> rotasByMonth = (Map<Month, List<RotaView>>) model.asMap().get("rotasByMonth");
        assertThat(rotasByMonth.keySet(), hasSize(0));
    }

    @Test
    public void rotasByMonthInModelShouldContainAllRotasGroupedByMonth() {
        Set<RotaView> foundRotas = new HashSet<>();
        LocalDate today = LocalDate.now();
        RotaView rotaThisMonth = new RotaView(new Rota(45l, user, new Shift(today, ShiftType.B1H)), new HashSet<>(), new HashSet<>());
        RotaView rota1NextMonth = new RotaView(new Rota(46l, user, new Shift(today.plusMonths(1), ShiftType.B1H)), new HashSet<>(), new HashSet<>());
        RotaView rota2NextMonth = new RotaView(new Rota(47l, user, new Shift(today.plusMonths(1), ShiftType.B1H)), new HashSet<>(), new HashSet<>());
        foundRotas.add(rotaThisMonth);
        foundRotas.add(rota1NextMonth);
        foundRotas.add(rota2NextMonth);
        when(rotaService.findAllRotas()).thenReturn(foundRotas);

        Model model = new BindingAwareModelMap();

        controller.showTimeline(model);

        Map<Month, List<RotaView>> rotasByMonth = (Map<Month, List<RotaView>>) model.asMap().get("rotasByMonth");
        assertThat(rotasByMonth.keySet(), hasSize(2));

        List<RotaView> rotasThisMonth = rotasByMonth.get(today.getMonth());
        assertThat(rotasThisMonth, hasSize(1));
        assertThat(rotasThisMonth, hasItem(rotaThisMonth));

        List<RotaView> rotasNextMonth = rotasByMonth.get(today.plusMonths(1).getMonth());
        assertThat(rotasNextMonth, hasSize(2));
        assertThat(rotasNextMonth, hasItem(rota1NextMonth));
        assertThat(rotasNextMonth, hasItem(rota2NextMonth));
    }

}