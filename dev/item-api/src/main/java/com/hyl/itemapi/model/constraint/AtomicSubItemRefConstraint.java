package com.hyl.itemapi.model.constraint;

import com.hyl.itemapi.model.constraint.validator.AtomicSubItemRefValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtomicSubItemRefValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AtomicSubItemRefConstraint {
    String message() default "{hyl.subitem.reference.error.atomic}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
