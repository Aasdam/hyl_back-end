package com.hyl.userapi.model.constraint;

import com.hyl.userapi.model.constraint.validator.AtomicEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtomicEmailValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AtomicEmailConstraint {
    String message() default "{hyl.user.email.error.atomic}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
