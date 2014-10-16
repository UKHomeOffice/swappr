package uk.gov.homeofficedigital.swappr.service;

import org.junit.Test;
import uk.gov.homeofficedigital.swappr.model.SwapMaker;
import uk.gov.homeofficedigital.swappr.model.SwapStatus;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;

public class SwapServiceTest {

    private SwapService service = new SwapService();

    @Test(expected = IllegalStateException.class)
    public void acceptingProposedSwapShouldThrowAnExceptionIfTheSwapIsNotInTheExpectedState() {
        service.acceptProposedSwap(make(a(SwapMaker.Swap, with(SwapMaker.status, SwapStatus.OFFERED))));
    }

}