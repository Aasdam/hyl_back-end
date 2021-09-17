package com.hyl.itemapi.model.constraint.validator;

import com.hyl.itemapi.controller.ItemController;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.constraint.MaxSubItemConstraint;
import com.hyl.itemapi.service.ItemService;
import com.hyl.itemapi.service.SubItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class MaxSubItemValidator implements ConstraintValidator<MaxSubItemConstraint, SubItem> {

    @Autowired
    private HttpServletRequest request;


    @Override
    public void initialize(MaxSubItemConstraint maxSubItemConstraint) {

    }

    @Override
    public boolean isValid(SubItem pSubItem, ConstraintValidatorContext context) {
        return SubItemService.maxSubItemByUserIsValid(ItemController.extractIdUserFromHeader(request));
    }
}
