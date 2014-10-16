package uk.gov.homeofficedigital.swappr.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import uk.gov.homeofficedigital.swappr.daos.SwapDao;
import uk.gov.homeofficedigital.swappr.model.*;

import java.time.LocalDate;
import java.util.Arrays;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.mockito.Mockito.*;

public class SwapServiceTest {

    private SwapDao swapDao = mock(SwapDao.class);
    private SwapService service = new SwapService(swapDao);

    @Before
    public void setUp() {
        reset(swapDao);
    }

    @Test(expected = IllegalStateException.class)
    public void acceptingVolunteeredSwapShouldThrowAnExceptionIfTheSwapIsNotInTheExpectedState() {
        service.acceptVolunteeredSwap(make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.OFFERED))));
    }

    @Test
    public void acceptingVolunteeredSwapShouldUpdateTheVolunteeredSwapStatusToAccepted() {
        Swap swap = make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.VOLUNTEERED)));
        service.acceptVolunteeredSwap(swap);
        verify(swapDao).updateSwapStatus(swap.getId(), SwapStatus.ACCEPTED);
    }

    @Ignore
    public void acceptingVolunteeredSwapShouldUpdateTheRelatedOfferedSwapToProposed() {
        service.acceptVolunteeredSwap(make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.PROPOSED))));
    }

    @Ignore
    public void acceptingVolunteeredSwapShouldNotifyTAMS() {
        service.acceptVolunteeredSwap(make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.PROPOSED))));
    }

    @Test
    public void offerSwapShouldCreateSwapWithOfferedState() {
        User user = new User("Bob", "password", Arrays.asList(new SimpleGrantedAuthority(Role.USER.name())));
        LocalDate now = LocalDate.now();
        service.offerSwap(user, now, ShiftType.Earlies, now.plusDays(2), ShiftType.Lates);
        verify(swapDao).createSwap(user.getUsername(), now, ShiftType.Earlies, now.plusDays(2), ShiftType.Lates, SwapStatus.OFFERED);
    }

}