package com.hyl.itemapi.model.constraint;

import com.hyl.itemapi.model.constraint.validator.AtomicItemNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtomicItemNameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AtomicItemNameConstraint {
    String message() default "{hyl.item.name.error.atomic}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
