package com.hyl.itemapi.model.constraint.validator;

import com.hyl.itemapi.controller.ItemController;
import com.hyl.itemapi.model.constraint.AtomicSubItemRefConstraint;
import com.hyl.itemapi.service.SubItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtomicSubItemRefValidator implements ConstraintValidator<AtomicSubItemRefConstraint, String> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void initialize(AtomicSubItemRefConstraint atomicSubItemRefConstraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {
        return SubItemService.checkAtomicRef(field, ItemController.extractIdUserFromHeader(request));
    }
}
