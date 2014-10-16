package uk.gov.homeofficedigital.swappr.service;

import uk.gov.homeofficedigital.swappr.model.Swap;
import uk.gov.homeofficedigital.swappr.model.SwapStatus;

public class SwapService {

    public void acceptProposedSwap(Swap proposed) {
        if (proposed.getStatus() != SwapStatus.PROPOSED) {
            throw new IllegalStateException("To accept a swap, it must have a status of [PROPOSED]");
        }
    }
}
