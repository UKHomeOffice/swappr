package uk.gov.homeofficedigital.swappr.daos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import uk.gov.homeofficedigital.swappr.SpringIntegrationTest;
import uk.gov.homeofficedigital.swappr.model.Rota;
import uk.gov.homeofficedigital.swappr.model.Shift;
import uk.gov.homeofficedigital.swappr.model.ShiftType;

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
        User user = new User("Bill", "password", Collections.emptyList());
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
        User bill = new User("Bill", "password", Collections.emptyList());
        User ben = new User("Ben", "password", Collections.emptyList());

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
        User bill = new User("Bill", "password", Collections.emptyList());
        User ben = new User("Ben", "password", Collections.emptyList());

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
}