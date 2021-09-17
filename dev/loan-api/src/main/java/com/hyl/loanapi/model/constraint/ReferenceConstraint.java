package com.hyl.loanapi.model.constraint;

import com.hyl.loanapi.model.constraint.validator.ReferenceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReferenceValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReferenceConstraint {
    String message() default "{hyl.loan.reference.error.alreadyinprogress}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
