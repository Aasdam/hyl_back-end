package com.hyl.userapi.model.constraint.validator;

import com.hyl.userapi.model.constraint.CivilityConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CivilityValidator implements ConstraintValidator<CivilityConstraint, String> {

    @Override
    public void initialize(CivilityConstraint civilityConstraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {

        if (field != null && !field.isEmpty()) {
            return ( field.equals("M") || field.equals("m") || field.equals("W") || field.equals("w") );
        } else return true;
    }
}
