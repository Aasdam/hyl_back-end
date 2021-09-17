package com.hyl.userapi.model.constraint;

import com.hyl.userapi.model.constraint.validator.CharacterRepetitionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CharacterRepetitionValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CharacterRepetitionConstraint {
    String message() default "Default CharacterRepetionConstraint message.";
    int value() default 3;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
