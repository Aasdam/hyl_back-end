package com.hyl.itemapi.model.constraint;

import com.hyl.itemapi.model.constraint.validator.AtomicCategoryNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtomicCategoryNameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AtomicCategoryNameConstraint {
    String message() default "{hyl.category.name.error.atomic}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
