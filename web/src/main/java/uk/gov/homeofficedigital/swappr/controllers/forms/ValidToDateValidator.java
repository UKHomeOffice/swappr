package uk.gov.homeofficedigital.swappr.controllers.forms;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidToDateValidator implements ConstraintValidator<ValidToDate, ShiftForm> {

    @Override
    public void initialize(ValidToDate constraintAnnotation) {

    }

    @Override
    public boolean isValid(ShiftForm form, ConstraintValidatorContext context) {
        if (form == null) {
            return true;
        }

        return form.getToDate().isPresent();
    }
}
