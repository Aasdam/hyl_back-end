package com.hyl.loanapi.model.constraint.validator;

import com.hyl.loanapi.controller.LoanController;
import com.hyl.loanapi.model.constraint.ReferenceConstraint;
import com.hyl.loanapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReferenceValidator implements ConstraintValidator<ReferenceConstraint, String> {

    @Autowired
    LoanService loanService;

    @Autowired
    HttpServletRequest request;

    @Override
    public void initialize(ReferenceConstraint referenceConstraint) {

    }

    @Override
    public boolean isValid(String reference, ConstraintValidatorContext context) {
       return loanService.isAlreadyInProgressByRefAndIdUser(reference, LoanController.extractIdUserFromHeader(request));
    }
}
