package com.hyl.itemapi.model.constraint;

import com.hyl.itemapi.model.constraint.validator.IdOwnerValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IdOwnerValidator.class)
@Target( { ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IdOwnerConstraint {
    String message() default "{hyl.object.iduser.error.constraint}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
