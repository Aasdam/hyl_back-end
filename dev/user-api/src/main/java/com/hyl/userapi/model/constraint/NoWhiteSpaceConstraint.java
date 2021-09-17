package com.hyl.userapi.model.constraint;

import com.hyl.userapi.model.constraint.validator.NoWhiteSpaceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoWhiteSpaceValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoWhiteSpaceConstraint {
    String message() default "Default NoWhiteSpaceConstraint message.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
