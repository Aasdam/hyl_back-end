package com.hyl.userapi.model.constraint;

import com.hyl.userapi.model.constraint.validator.AtomicCellphoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtomicCellphoneValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AtomicCellphoneConstraint {
    String message() default "{hyl.user.cellphone.error.atomic}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
