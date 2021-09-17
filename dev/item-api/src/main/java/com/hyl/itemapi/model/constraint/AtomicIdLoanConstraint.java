package com.hyl.itemapi.model.constraint;

import com.hyl.itemapi.model.constraint.validator.AtomicIdLoanValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtomicIdLoanValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AtomicIdLoanConstraint {
    String message() default "{hyl.trackingsheet.idloan.error.atomic}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
