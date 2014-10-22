package uk.gov.homeofficedigital.swappr.controllers.forms;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { ValidFromDateValidator.class })
@Documented
public @interface ValidFromDate {

    String message() default "{uk.gov.homeofficedigital.swappr.controllers.forms.ShiftForm.ValidFromDate.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}