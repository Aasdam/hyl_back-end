package com.hyl.loanapi.model.constraint;

import com.hyl.loanapi.model.constraint.validator.InformationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InformationValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface InformationConstraint {
    String message() default "{hyl.loan.information.error.constraint}";
    int min() default 15;
    int max() default 100;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
