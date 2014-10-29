package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import uk.gov.homeofficedigital.swappr.controllers.views.DayView;
import uk.gov.homeofficedigital.swappr.controllers.views.RotaView;
import uk.gov.homeofficedigital.swappr.model.*;
import uk.gov.homeofficedigital.swappr.service.RotaService;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HomeControllerTest {

    private RotaService rotaService = mock(RotaService.class);
    private SwapprUser user = make(a(UserMaker.User));
    private UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(user, null);
    private HomeController controller = new HomeController(rotaService, new ControllerHelper());

    @Test
    public void populateModelWithDaysMergedWithRotas() {

        LocalDate now = LocalDate.now();
        Set<RotaView> foundRotas = new HashSet<>();
        RotaView myRota = new RotaView(new Rota(34l, user, new Shift(now.plusMonths(1), ShiftType.CFH)), new HashSet<>(), new HashSet<>());
        foundRotas.add(myRota);
        when(rotaService.findMyRotas(user)).thenReturn(foundRotas);

        Model model = new ExtendedModelMap();

        String viewName = controller.showHomePage(model, principal);
        assertThat(viewName, equalTo("home"));

        Map<Month, List<DayView>> rotasByMonth = (Map<Month, List<DayView>>) model.asMap().get("rotasByMonth");
        assertThat(rotasByMonth.entrySet(), hasSize(3));

        List<DayView> dayViewsThisMonth = rotasByMonth.get(now.getMonth());
        assertThat("rotas this month", dayViewsThisMonth.stream().filter(hasRotaView()).collect(toList()), hasSize(0));

        List<DayView> dayViewsNextMonth = rotasByMonth.get(now.plusMonths(1).getMonth());
        assertThat("rotas next month", dayViewsNextMonth.stream().filter(hasRotaView()).collect(toList()), hasSize(1));

        List<DayView> dayViewsFinalMonth = rotasByMonth.get(now.plusMonths(2).getMonth());
        assertThat("rotas final month", dayViewsFinalMonth.stream().filter(hasRotaView()).collect(toList()), hasSize(0));
    }

    private Predicate<DayView> hasRotaView() {
        return dv -> dv.getRotaView().isPresent();
    }

}