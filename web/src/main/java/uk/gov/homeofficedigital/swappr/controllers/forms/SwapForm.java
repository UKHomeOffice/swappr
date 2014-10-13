package uk.gov.homeofficedigital.swappr.controllers.forms;

import uk.gov.homeofficedigital.swappr.model.ShiftType;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.DateTimeException;
import java.time.LocalDate;

public class SwapForm {

    @Min(1)
    private int fromDay;
    @Min(1)
    private int fromMonth;
    @Min(1)
    private int fromYear;
    @NotNull
    private ShiftType fromShiftType;

    @Min(1)
    private int toDay;
    @Min(1)
    private int toMonth;
    @Min(1)
    private int toYear;
    @NotNull
    private ShiftType toShiftType;

    public int getFromDay() {
        return fromDay;
    }

    public void setFromDay(int fromDay) {
        this.fromDay = fromDay;
    }

    public int getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(int fromMonth) {
        this.fromMonth = fromMonth;
    }

    public int getFromYear() {
        return fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public ShiftType getFromShiftType() {
        return fromShiftType;
    }

    public void setFromShiftType(ShiftType fromShiftType) {
        this.fromShiftType = fromShiftType;
    }

    public int getToDay() {
        return toDay;
    }

    public void setToDay(int toDay) {
        this.toDay = toDay;
    }

    public int getToMonth() {
        return toMonth;
    }

    public void setToMonth(int toMonth) {
        this.toMonth = toMonth;
    }

    public int getToYear() {
        return toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }

    public ShiftType getToShiftType() {
        return toShiftType;
    }

    public void setToShiftType(ShiftType toShiftType) {
        this.toShiftType = toShiftType;
    }

    @AssertTrue(message = "Not a valid date")
    public boolean isValidFromDate() {
        try {
            getFromDate();
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    @AssertTrue(message = "Not a valid date")
    public boolean isValidToDate() {
        try {
            getToDate();
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    public LocalDate getFromDate() {
        return LocalDate.of(fromYear, fromMonth, fromDay);
    }

    public LocalDate getToDate() {
        return LocalDate.of(toYear, toMonth, toDay);
    }
}
