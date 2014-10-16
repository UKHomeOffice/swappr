package uk.gov.homeofficedigital.swappr.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import uk.gov.homeofficedigital.swappr.daos.SwapDao;
import uk.gov.homeofficedigital.swappr.model.Swap;
import uk.gov.homeofficedigital.swappr.model.SwapMaker;
import uk.gov.homeofficedigital.swappr.model.SwapStatus;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

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
        swap.setId(23);
        service.acceptVolunteeredSwap(swap);
        verify(swapDao).updateSwapStatus(23, SwapStatus.ACCEPTED);
    }

    @Ignore
    public void acceptingVolunteeredSwapShouldUpdateTheRelatedOfferedSwapToProposed() {
        service.acceptVolunteeredSwap(make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.PROPOSED))));
    }

    @Ignore
    public void acceptingVolunteeredSwapShouldNotifyTAMS() {
        service.acceptVolunteeredSwap(make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.PROPOSED))));
    }

}