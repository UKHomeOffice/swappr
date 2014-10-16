package uk.gov.homeofficedigital.swappr.service;

import org.springframework.security.core.userdetails.User;
import uk.gov.homeofficedigital.swappr.daos.SwapDao;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.model.Swap;

import java.time.LocalDate;
import java.util.List;

import static uk.gov.homeofficedigital.swappr.model.SwapStatus.*;

public class SwapService {

    private final SwapDao swapDao;

    public SwapService(SwapDao swapDao) {
        this.swapDao = swapDao;
    }

    public void offerSwap(User user, LocalDate fromShiftDate, ShiftType fromShiftType, LocalDate toShiftDate, ShiftType toShiftType) {
        swapDao.createSwap(user.getUsername(), fromShiftDate, fromShiftType, toShiftDate, toShiftType, OFFERED);
    }

    public void acceptVolunteeredSwap(Swap volunteered) {
        if (volunteered.getStatus() != VOLUNTEERED) {
            throw new IllegalStateException("To accept a swap, it must have a status of [VOLUNTEERED]");
        }

        List<Swap> related = volunteered.getRelatedSwaps();

        if (related.size() != 1) {
            throw new IllegalStateException("To accept a swap, it must have only 1 related swap");
        }

        Swap offered = related.get(0);

        if (offered.getStatus() != OFFERED) {
            throw new IllegalStateException("To accept a swap, its related swap must have a status of [OFFERED]");
        }

        swapDao.updateSwapStatus(offered.getId(), PROPOSED);
        swapDao.updateSwapStatus(volunteered.getId(), ACCEPTED);

    }

    public Swap loadSwap(int id) {
        return swapDao.loadSwap(id).orElseThrow(() -> new IllegalArgumentException("not a valid ID"));
    }
}
