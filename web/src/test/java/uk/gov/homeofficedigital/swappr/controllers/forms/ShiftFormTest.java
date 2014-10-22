package uk.gov.homeofficedigital.swappr.controllers.forms;

import org.junit.Before;
import org.junit.Test;
import uk.gov.homeofficedigital.swappr.model.ShiftType;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.*;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;

public class ShiftFormTest {

    private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private LocalDateTime fixed = LocalDateTime.of(2014, 11, 16, 3, 2);
    private ShiftForm form;
    private Validator validator;

    @Before
    public void setUp() {
        form = new ShiftForm(Clock.fixed(fixed.toInstant(ZoneOffset.UTC), ZoneId.systemDefault()));
        validator = validatorFactory.getValidator();
    }

    @Test
    public void getFromDateShouldBeThisYearIfTheDayAndMonthAreLessThanOrEqualToNow() {
        form.setFromDay(fixed.getDayOfMonth());
        form.setFromMonth(fixed.getMonthValue());
        assertThat(form.getFromDate().get(), equalTo(fixed.toLocalDate()));
    }

    @Test
    public void fromDateShouldBeNoEarlierThanToday() {
        form.setFromDay(fixed.getDayOfMonth() - 1);
        form.setFromMonth(fixed.getMonthValue());
        form.setToDay(fixed.getDayOfMonth());
        form.setToMonth(fixed.getMonthValue());
        form.setType(ShiftType.S1H);
        Set<ConstraintViolation<ShiftForm>> violations = validator.validate(form);

        assertThat(violations, hasSize(1));
        assertThat(violations, hasItem((org.hamcrest.Matcher<? super ConstraintViolation<ShiftForm>>) hasProperty("message", equalTo("From Day and Month must together be a valid date"))));

        form.setFromDay(fixed.getDayOfMonth());
        violations = validator.validate(form);
        assertThat(violations, hasSize(0));
    }

    @Test
    public void getFromDateShouldReturnTheNextYearIfTheMonthIsLessThanToday() {
        form.setFromDay(20);
        form.setFromMonth(1);
        assertThat(form.getFromDate().get(), equalTo(LocalDate.of(2015, 1, 20)));
    }

    @Test
    public void invalidMonthAndDayShouldTriggerValidationErrors() {
        form.setFromDay(31);
        form.setFromMonth(2);
        form.setToDay(2);
        form.setToMonth(3);
        form.setType(ShiftType.B1H);
        Set<ConstraintViolation<ShiftForm>> violations = validator.validate(form);

        assertThat(violations, hasItem((org.hamcrest.Matcher<? super ConstraintViolation<ShiftForm>>) hasProperty("message", equalTo("From Day and Month must together be a valid date"))));
        assertThat(violations, hasItem((org.hamcrest.Matcher<? super ConstraintViolation<ShiftForm>>) hasProperty("message", equalTo("From date must be no greater than To date"))));
        assertThat(violations, hasSize(2));
    }

    @Test
    public void fromDateShouldBeBeforeOrEqualToToDate() {
        form.setFromDay(20);
        form.setFromMonth(3);
        form.setToDay(19);
        form.setToMonth(3);
        form.setType(ShiftType.C1H);
        Set<ConstraintViolation<ShiftForm>> violations = validator.validate(form);
        assertThat(violations, hasSize(1));
        assertThat("to one day before from", violations, hasItem((org.hamcrest.Matcher<? super ConstraintViolation<ShiftForm>>) hasProperty("message", equalTo("From date must be no greater than To date"))));
        form.setToDay(20);
        violations = validator.validate(form);
        assertThat("to the same day as from", violations, hasSize(0));
    }

    @Test
    public void getToDateShouldBeThisYearIfTheDayAndMonthAreLessThanOrEqualToNow() {
        form.setToDay(fixed.getDayOfMonth());
        form.setToMonth(fixed.getMonthValue());
        assertThat(form.getToDate().get(), equalTo(fixed.toLocalDate()));
    }

    @Test
    public void getToDateShouldReturnTheNextYearIfTheMonthIsLessThanToday() {
        form.setToDay(22);
        form.setToMonth(2);
        assertThat(form.getToDate().get(), equalTo(LocalDate.of(2015, 2, 22)));
    }

}