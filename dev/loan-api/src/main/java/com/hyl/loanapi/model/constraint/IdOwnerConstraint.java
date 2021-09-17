package com.hyl.loanapi.model.constraint;

import com.hyl.loanapi.model.constraint.validator.IdOwnerValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IdOwnerValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IdOwnerConstraint {
    String message() default "{hyl.loan.idowner.error.constraint}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
