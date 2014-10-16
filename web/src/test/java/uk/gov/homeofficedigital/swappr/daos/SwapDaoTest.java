package uk.gov.homeofficedigital.swappr.daos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.homeofficedigital.swappr.SpringIntegrationTest;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.model.Swap;
import uk.gov.homeofficedigital.swappr.model.SwapStatus;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SwapDaoTest extends SpringIntegrationTest {

    @Autowired
    private SwapDao swapDao;

    @Test
    public void createSwap_shouldPersistTheSwapInstance() throws Exception {
        Swap swap = new Swap("Bill", LocalDate.now(), ShiftType.Earlies, LocalDate.now().plusDays(2), ShiftType.Lates, SwapStatus.PROPOSED);

        swapDao.createSwap(swap);

        List<Swap> swaps = swapDao.findSwapsForUser("Bill");
        assertEquals(1, swaps.size());
        Swap saved = swaps.get(0);
        assertEquals(swap.getUsername(), saved.getUsername());
        assertEquals(swap.getFromDate(), saved.getFromDate());
        assertEquals(swap.getFromShift(), saved.getFromShift());
        assertEquals(swap.getToDate(), saved.getToDate());
        assertEquals(swap.getToShift(), saved.getToShift());
        assertEquals(swap.getStatus(), saved.getStatus());
    }
}