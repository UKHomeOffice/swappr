package uk.gov.homeofficedigital.swappr.daos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.homeofficedigital.swappr.SpringIntegrationTest;
import uk.gov.homeofficedigital.swappr.model.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

public class OfferDaoTest extends SpringIntegrationTest {

    @Autowired
    private OfferDao offerDao;

    @Autowired
    private RotaDao rotaDao;

    @Test
    public void create_shouldCreateAnOffer() throws Exception {
        LocalDate now = LocalDate.now();
        Shift rotaShift = new Shift(now, ShiftType.C1H);
        Rota rota = rotaDao.create(bill, rotaShift);
        Shift swapTo = new Shift(now.plusDays(2), ShiftType.BFH);
        Offer offer = offerDao.create(rota, swapTo, OfferStatus.CREATED);

        assertFalse(offer.getId() == 0L);

        Optional<Offer> stored = offerDao.findById(offer.getId());
        assertTrue(stored.isPresent());
        assertEquals(rota, stored.get().getSwapFrom());
        assertEquals(swapTo, stored.get().getSwapTo());
        assertEquals(OfferStatus.CREATED, stored.get().getStatus());
    }

    @Test
    public void findByRota_shouldRetrieveOffersForARota() throws Exception {
        LocalDate now = LocalDate.now();
        Shift shiftToday = new Shift(now, ShiftType.C1H);
        Shift shiftTomorrow = new Shift(now.plusDays(1), ShiftType.C1H);
        Rota rotaToday = rotaDao.create(bill, shiftToday);
        Rota rotaTomorrow = rotaDao.create(bill, shiftTomorrow);
        Shift swapTodayFor = new Shift(now.plusDays(2), ShiftType.BFH);
        Shift swapTomorrowFor = new Shift(now.plusDays(4), ShiftType.BFH);
        Offer offerToday = offerDao.create(rotaToday, swapTodayFor, OfferStatus.CREATED);
        Offer offerTomorrow = offerDao.create(rotaTomorrow, swapTomorrowFor, OfferStatus.CREATED);

        Set<Offer> actual = offerDao.findByRota(rotaToday);

        assertEquals(1, actual.size());
        assertEquals(offerToday, actual.iterator().next());
    }
}