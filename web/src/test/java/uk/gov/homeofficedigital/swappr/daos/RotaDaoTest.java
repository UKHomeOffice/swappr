package uk.gov.homeofficedigital.swappr.daos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.homeofficedigital.swappr.SpringIntegrationTest;
import uk.gov.homeofficedigital.swappr.model.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RotaDaoTest extends SpringIntegrationTest {

    @Autowired
    private RotaDao rotaDao;

    @Test
    public void create_shouldCreateAndPersistAShift() throws Exception {
        LocalDate now = LocalDate.now();
        SwapprUser user = UserMaker.bill();
        Shift expectedShift = new Shift(now, ShiftType.C1H);
        Rota rota = rotaDao.create(user, expectedShift);

        assertFalse(rota.getId() == 0L);

        Optional<Rota> stored = rotaDao.findById(rota.getId());
        assertTrue(stored.isPresent());
        assertEquals(expectedShift, stored.get().getShift());
        assertEquals(user, stored.get().getWorker());
    }

    @Test
    public void findByWorker_shouldFindRotasForWorker() throws Exception {
        SwapprUser bill = UserMaker.bill();
        SwapprUser ben = UserMaker.ben();

        LocalDate now = LocalDate.now();
        List<Rota> billsRota = Arrays.asList(
                rotaDao.create(bill, new Shift(now, ShiftType.C1H)),
                rotaDao.create(bill, new Shift(now.plusDays(1), ShiftType.C1H)));
        List<Rota> bensRota = Arrays.asList(
                rotaDao.create(ben, new Shift(now, ShiftType.C1H)),
                rotaDao.create(ben, new Shift(now.plusDays(1), ShiftType.C1H)));

        Set<Rota> actual = rotaDao.findByWorker(bill);

        assertTrue(actual.containsAll(billsRota));
        assertFalse(actual.containsAll(bensRota));
    }

    @Test
    public void findAll_shouldFindRotasForAllWorker() throws Exception {
        SwapprUser bill = UserMaker.bill();
        SwapprUser ben = UserMaker.ben();

        LocalDate now = LocalDate.now();
        List<Rota> billsRota = Arrays.asList(
                rotaDao.create(bill, new Shift(now, ShiftType.C1H)),
                rotaDao.create(bill, new Shift(now.plusDays(1), ShiftType.C1H)));
        List<Rota> bensRota = Arrays.asList(
                rotaDao.create(ben, new Shift(now, ShiftType.C1H)),
                rotaDao.create(ben, new Shift(now.plusDays(1), ShiftType.C1H)));

        Set<Rota> actual = rotaDao.findAll();

        assertTrue(actual.containsAll(billsRota));
        assertTrue(actual.containsAll(bensRota));
    }


    @Test
    public void findAll_shouldIgnoreRotasInThePastAndMoreThan2MonthsInTheFuture() throws Exception {
        SwapprUser bill = UserMaker.bill();
        SwapprUser ben = UserMaker.ben();

        LocalDate now = LocalDate.now();
        Rota pastRota = rotaDao.create(bill, new Shift(now.minusDays(1), ShiftType.C1H));
        Rota presentRota = rotaDao.create(ben, new Shift(now.plusDays(2), ShiftType.C1H));
        Rota futureRota = rotaDao.create(ben, new Shift(now.plusDays(1).plusMonths(2), ShiftType.C1H));

        Set<Rota> actual = rotaDao.findAll();

        assertTrue(actual.contains(presentRota));
        assertFalse(actual.contains(pastRota));
        assertFalse(actual.contains(futureRota));
    }

    @Test
    public void findOrCreate_shouldCreateRota_givenRotaDoesNotExist() throws Exception {
        LocalDate now = LocalDate.now();
        SwapprUser user = UserMaker.bill();
        Shift expectedShift = new Shift(now, ShiftType.C1H);
        Rota rota = rotaDao.findOrCreate(user, expectedShift);

        assertNotNull(rota.getId());
        Rota reload = rotaDao.findOrCreate(user, expectedShift);
        assertEquals(rota.getId(), reload.getId());
    }
}