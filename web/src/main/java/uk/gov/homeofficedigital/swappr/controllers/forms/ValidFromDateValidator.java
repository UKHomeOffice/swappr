package uk.gov.homeofficedigital.swappr.controllers.forms;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidFromDateValidator implements ConstraintValidator<ValidFromDate, ShiftForm> {

    @Override
    public void initialize(ValidFromDate constraintAnnotation) {

    }

    @Override
    public boolean isValid(ShiftForm form, ConstraintValidatorContext context) {
        if (form == null) {
            return true;
        }

        return form.getFromDate().isPresent() && form.atLeastToday(form.getFromDate().get());
    }
}
