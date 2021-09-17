package com.hyl.loanapi.model.constraint;

import com.hyl.loanapi.model.constraint.validator.AlreadyClosedValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AlreadyClosedValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AlreadyClosedConstraint {
    String message() default "{hyl.loan.loan.error.alreadyclosed}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
