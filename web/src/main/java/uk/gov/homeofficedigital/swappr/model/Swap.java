package uk.gov.homeofficedigital.swappr.model;

import java.time.LocalDate;

public class Swap {
    private final String username;
    private final LocalDate fromDate;
    private final ShiftType fromShiftType;
    private final LocalDate toDate;
    private final ShiftType toShiftType;

    public Swap(String username, LocalDate fromDate, ShiftType fromShiftType, LocalDate toDate, ShiftType toShiftType) {
        this.username = username;
        this.fromDate = fromDate;
        this.fromShiftType = fromShiftType;
        this.toDate = toDate;
        this.toShiftType = toShiftType;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public ShiftType getFromShift() {
        return fromShiftType;
    }

    public ShiftType getToShift() {
        return toShiftType;
    }
}
