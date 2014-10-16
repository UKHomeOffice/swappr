package uk.gov.homeofficedigital.swappr.service;

import org.springframework.security.core.userdetails.User;
import uk.gov.homeofficedigital.swappr.daos.SwapDao;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.model.Swap;
import uk.gov.homeofficedigital.swappr.model.SwapStatus;

import java.time.LocalDate;

public class SwapService {

    private final SwapDao swapDao;

    public SwapService(SwapDao swapDao) {
        this.swapDao = swapDao;
    }

    public void offerSwap(User user, LocalDate fromShiftDate, ShiftType fromShiftType, LocalDate toShiftDate, ShiftType toShiftType) {
        swapDao.createSwap(user.getUsername(), fromShiftDate, fromShiftType, toShiftDate, toShiftType, SwapStatus.OFFERED);
    }

    public void acceptVolunteeredSwap(Swap volunteered) {
        if (volunteered.getStatus() != SwapStatus.VOLUNTEERED) {
            throw new IllegalStateException("To accept a swap, it must have a status of [VOLUNTEERED]");
        }
        swapDao.updateSwapStatus(volunteered.getId(), SwapStatus.ACCEPTED);
    }

    public Swap loadSwap(int id) {
        return swapDao.loadSwap(id).orElseThrow(() -> new IllegalArgumentException("not a valid ID"));
    }
}
