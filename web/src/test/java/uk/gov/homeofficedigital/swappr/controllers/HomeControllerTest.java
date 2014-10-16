package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import uk.gov.homeofficedigital.swappr.daos.SwapDao;
import uk.gov.homeofficedigital.swappr.model.Swap;
import uk.gov.homeofficedigital.swappr.model.SwapMaker;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HomeControllerTest {

    private SwapDao swapDao = mock(SwapDao.class);
    private HomeController controller = new HomeController(swapDao);

    @Test
    public void home_shouldDisplayTheSwapsForTheCurrentUser() throws Exception {
        ExtendedModelMap model = new ExtendedModelMap();
        Principal principal = () -> "Fred";
        List<Swap> expectedSwaps = Arrays.asList(make(a(SwapMaker.Swap, with(SwapMaker.user, "Fred"))));
        when(swapDao.findSwapsForUser("Fred")).thenReturn(expectedSwaps);

        String result = controller.home(model, principal);

        assertEquals("home", result);
        assertTrue(model.containsAttribute("months"));
        assertEquals(2, ((List)model.get("months")).size());
        assertTrue(model.containsAttribute("swaps"));
        assertEquals(expectedSwaps, ((Map <String, Swap>)model.get("swaps")).get(LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)));
        assertTrue(model.containsAttribute("display"));
    }
}