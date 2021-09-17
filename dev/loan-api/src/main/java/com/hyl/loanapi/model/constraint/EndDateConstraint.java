package com.hyl.loanapi.model.constraint;

import com.hyl.loanapi.model.constraint.validator.EndDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EndDateValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EndDateConstraint {
    String message() default "{hyl.loan.enddate.error.constraint}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
