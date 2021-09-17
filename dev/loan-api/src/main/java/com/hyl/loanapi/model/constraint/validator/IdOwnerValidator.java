package com.hyl.loanapi.model.constraint.validator;

import com.hyl.loanapi.controller.LoanController;
import com.hyl.loanapi.exception.CustomNotFoundException;
import com.hyl.loanapi.model.Loan;
import com.hyl.loanapi.model.constraint.IdOwnerConstraint;
import com.hyl.loanapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdOwnerValidator implements ConstraintValidator<IdOwnerConstraint, Loan> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    LoanService loanService;

    @Override
    public void initialize(IdOwnerConstraint idOwnerConstraint) {

    }

    @Override
    public boolean isValid(Loan pLoan, ConstraintValidatorContext context) {
        Loan loan;
        try {
            loan = loanService.getLoan(pLoan.getId());
        } catch (CustomNotFoundException e) {
            return false;
        }
        return LoanController.extractIdUserFromHeader(request) == loan.getIdOwner();
    }
}
