package uk.gov.homeofficedigital.swappr.daos;

import uk.gov.homeofficedigital.swappr.model.Shift;
import uk.gov.homeofficedigital.swappr.model.ShiftType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class ShiftDao {

    private Set<Shift> shifts = new HashSet<>();
    private AtomicLong incrementingId = new AtomicLong(0);

    public Shift create(LocalDate date, ShiftType type) {
        Shift shift = new Shift(incrementingId.incrementAndGet(), date, type);
        shifts.add(shift);
        return shift;
    }

    public Optional<Shift> findById(Long shiftId) {
        return shifts.stream().filter(s -> s.getId().equals(shiftId)).findFirst();
    }
}
