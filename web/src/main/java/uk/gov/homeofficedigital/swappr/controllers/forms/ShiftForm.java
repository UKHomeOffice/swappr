package uk.gov.homeofficedigital.swappr.controllers.forms;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import uk.gov.homeofficedigital.swappr.model.ShiftType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Optional;

@ValidToDate
@ValidFromDate
@ValidFromAndToDate
public class ShiftForm {

    private final Clock clock;

    @NotNull @Min(1) @Max(31)
    private Integer fromDay;
    @NotNull @Min(1) @Max(12)
    private Integer fromMonth;

    @NotNull @Min(1) @Max(31)
    private Integer toDay;
    @NotNull @Min(1) @Max(12)
    private Integer toMonth;

    @NotNull
    private ShiftType type;

    public ShiftForm() {
        this.clock = Clock.systemDefaultZone();
    }

    public ShiftForm(Clock testingClock) {
        this.clock = testingClock;
    }


    public Integer getFromDay() {
        return fromDay;
    }

    public void setFromDay(Integer fromDay) {
        this.fromDay = fromDay;
    }

    public Integer getFromMonth() {
        return fromMonth;
    }

    public Optional<LocalDate> getFromDate() {
        return inferYear(fromDay, fromMonth);
    }

    boolean atLeastToday(LocalDate localDate) {
        return LocalDate.now(clock).compareTo(localDate) <= 0;
    }


    private Optional<LocalDate> inferYear(Integer day, Integer month) {
        if (day == null || month == null) {
            return Optional.empty();
        }
        LocalDate now = LocalDate.now(clock);
        try {
            LocalDate fromMonthAndDay = LocalDate.of(now.getYear(), month, day);
            if (fromMonthAndDay.getMonthValue() < now.getMonthValue()) {
                fromMonthAndDay = fromMonthAndDay.plusYears(1);
            }
            return Optional.of(fromMonthAndDay);
        } catch (DateTimeException dte) {
            return Optional.empty();
        }
    }

    public void setFromMonth(Integer fromMonth) {
        this.fromMonth = fromMonth;
    }

    public Integer getToDay() {
        return toDay;
    }

    public void setToDay(Integer toDay) {
        this.toDay = toDay;
    }

    public Integer getToMonth() {
        return toMonth;
    }

    public void setToMonth(Integer toMonth) {
        this.toMonth = toMonth;
    }

    public Optional<LocalDate> getToDate() {
        return inferYear(toDay, toMonth);
    }

    public ShiftType getType() {
        return type;
    }

    public void setType(ShiftType type) {
        this.type = type;
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
}
