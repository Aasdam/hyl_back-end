package com.hyl.userapi.model.constraint.validator;

import com.hyl.userapi.model.constraint.AtomicCellphoneConstraint;
import com.hyl.userapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtomicCellphoneValidator implements ConstraintValidator<AtomicCellphoneConstraint, String> {

    @Autowired
    UserService userService;

    @Override
    public void initialize(AtomicCellphoneConstraint atomicCellphoneConstraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {
        if (field != null && !field.isEmpty()) {
            return userService.checkAtomicCellphone(field);
        } else return true;
    }
}
