package com.hyl.itemapi.model.constraint;

import com.hyl.itemapi.model.constraint.validator.MaxSubItemValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxSubItemValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxSubItemConstraint {
    String message() default "{hyl.subitem.authorized.error.max}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
