package uk.gov.homeofficedigital.swappr.controllers.forms;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidFromAndToDateValidator implements ConstraintValidator<ValidFromAndToDate, ShiftForm> {

    @Override
    public void initialize(ValidFromAndToDate constraintAnnotation) {

    }

    @Override
    public boolean isValid(ShiftForm form, ConstraintValidatorContext context) {
        if (form == null) {
            return true;
        }

        return form.getFromDate().isPresent() && form.getToDate().isPresent() && (
                form.getFromDate().get().isEqual(form.getToDate().get()) || form.getFromDate().get().isBefore(form.getToDate().get())
                );
    }
}
