package com.hyl.userapi.model.constraint;

import com.hyl.userapi.model.constraint.validator.CellphoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CellphoneValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CellphoneConstraint {
    String message() default "{hyl.user.cellphone.error.constraint}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
