package com.hyl.itemapi.model.constraint.validator;

import com.hyl.itemapi.model.constraint.AtomicIdLoanConstraint;
import com.hyl.itemapi.service.TrackingSheetService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtomicIdLoanValidator implements ConstraintValidator<AtomicIdLoanConstraint, Long> {

    @Override
    public void initialize(AtomicIdLoanConstraint atomicIdLoanConstraint) {
    }

    @Override
    public boolean isValid(Long idLoan, ConstraintValidatorContext cxt) {
        if (idLoan == null) return true;
        String result = TrackingSheetService.getCommentByIdLoan(idLoan);
        return result == null;
    }
}
