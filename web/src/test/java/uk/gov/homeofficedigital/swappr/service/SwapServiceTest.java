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

    @Test(expected = IllegalStateException.class)
    public void acceptingVolunteeredSwapShouldThrowAnExceptionIfTheRelatedSwapIsNotInTheExpectedState() {
        Swap proposed = make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.PROPOSED)));
        Swap volunteered = make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.VOLUNTEERED)).but(with(SwapMaker.related, Arrays.asList(proposed))));

        service.acceptVolunteeredSwap(volunteered);
    }

    @Test(expected = IllegalStateException.class)
    public void acceptingVolunteeredSwapShouldThrowAnExceptionIfThereIsMoreThanOneRelatedSwap() {
        Swap proposed = make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.PROPOSED)));
        Swap offered = make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.OFFERED)));
        Swap volunteered = make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.VOLUNTEERED)).but(with(SwapMaker.related, Arrays.asList(offered, proposed))));

        service.acceptVolunteeredSwap(volunteered);
    }

    @Test
    public void acceptingVolunteeredSwapShouldUpdateTheVolunteeredSwapStatusToAccepted() {
        Swap offered = make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.OFFERED)));
        Swap swap = make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.VOLUNTEERED)).but(with(SwapMaker.related, Arrays.asList(offered))));
        service.acceptVolunteeredSwap(swap);
        verify(swapDao).updateSwapStatus(swap.getId(), SwapStatus.ACCEPTED);
    }

    @Test
    public void acceptingVolunteeredSwapShouldUpdateTheRelatedOfferedSwapToProposed() {
        Swap offered = make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.OFFERED)).but(with(SwapMaker.id, 1l)));
        Swap volunteered = make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.VOLUNTEERED)).but(with(SwapMaker.related, Arrays.asList(offered))).but(with(SwapMaker.id, 2l)));

        service.acceptVolunteeredSwap(volunteered);

        verify(swapDao).updateSwapStatus(offered.getId(), SwapStatus.PROPOSED);
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