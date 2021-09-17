package com.hyl.itemapi.model.constraint.validator;

import com.hyl.itemapi.controller.ItemController;
import com.hyl.itemapi.model.constraint.AtomicItemNameConstraint;
import com.hyl.itemapi.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtomicItemNameValidator implements ConstraintValidator<AtomicItemNameConstraint, String> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void initialize(AtomicItemNameConstraint atomicItemNameConstraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {
        return ItemService.checkAtomicName(field, ItemController.extractIdUserFromHeader(request));
    }
}
