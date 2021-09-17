package com.hyl.itemapi.model.constraint.validator;

import com.hyl.itemapi.controller.ItemController;
import com.hyl.itemapi.model.constraint.AtomicCategoryNameConstraint;
import com.hyl.itemapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtomicCategoryNameValidator implements ConstraintValidator<AtomicCategoryNameConstraint, String> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void initialize(AtomicCategoryNameConstraint atomicCategoryNameConstraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {
        return CategoryService.checkAtomicName(field, ItemController.extractIdUserFromHeader(request));
    }
}
