package uk.gov.homeofficedigital.swappr.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.time.LocalDate;

public class Swap {

    private int id;
    private final String username;
    private final LocalDate fromDate;
    private final ShiftType fromShiftType;
    private final LocalDate toDate;
    private final ShiftType toShiftType;
    private final SwapStatus status;

    public Swap(String username, LocalDate fromDate, ShiftType fromShiftType, LocalDate toDate, ShiftType toShiftType, SwapStatus status) {
        this.username = username;
        this.fromDate = fromDate;
        this.fromShiftType = fromShiftType;
        this.toDate = toDate;
        this.toShiftType = toShiftType;
        this.status = status;
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

    public SwapStatus getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
