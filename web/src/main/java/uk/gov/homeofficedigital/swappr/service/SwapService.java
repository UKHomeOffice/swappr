package uk.gov.homeofficedigital.swappr.service;

import uk.gov.homeofficedigital.swappr.daos.SwapDao;
import uk.gov.homeofficedigital.swappr.model.Swap;
import uk.gov.homeofficedigital.swappr.model.SwapStatus;

public class SwapService {

    private final SwapDao swapDao;

    public SwapService(SwapDao swapDao) {
        this.swapDao = swapDao;
    }

    public void acceptVolunteeredSwap(Swap volunteered) {
        if (volunteered.getStatus() != SwapStatus.VOLUNTEERED) {
            throw new IllegalStateException("To accept a swap, it must have a status of [VOLUNTEERED]");
        }
        swapDao.updateSwapStatus(volunteered.getId(), SwapStatus.ACCEPTED);
    }
}
