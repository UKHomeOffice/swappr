package uk.gov.homeofficedigital.swappr.controllers.forms;

import uk.gov.homeofficedigital.swappr.model.ShiftType;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
        return false;
    }

    @AssertTrue(message = "Not a valid date")
    public boolean isValidToDate() {
        return false;
    }
}
