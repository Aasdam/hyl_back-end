package com.hyl.userapi.model.constraint;

import com.hyl.userapi.model.constraint.validator.CivilityValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CivilityValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CivilityConstraint {
    String message() default "{hyl.user.civility.error.constraint}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
