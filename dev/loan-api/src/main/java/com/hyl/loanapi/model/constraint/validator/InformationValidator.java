package com.hyl.loanapi.model.constraint.validator;

import com.hyl.loanapi.model.constraint.InformationConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InformationValidator implements ConstraintValidator<InformationConstraint, String> {

    private int min;
    private int max;

    @Override
    public void initialize(InformationConstraint informationConstraint) {
        this.min = informationConstraint.min();
        this.max = informationConstraint.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.isEmpty()) return true;
        else if (value.isBlank()) return false;
        else return value.length() >= min && value.length() <= max;
    }
}
