package com.hyl.itemapi.model.constraint.validator;

import com.hyl.itemapi.controller.ItemController;
import com.hyl.itemapi.model.Category;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.constraint.IdOwnerConstraint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdOwnerValidator implements ConstraintValidator<IdOwnerConstraint, Object> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void initialize(IdOwnerConstraint idOwnerConstraint) {

    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        String className = object.getClass().getName();
        if (className.contains("$")) {
            className = className.substring(0, className.indexOf("$"));
        }

        if (className.equals(SubItem.class.getName())) {
            return ItemController.extractIdUserFromHeader(request) == ((SubItem) object).getItem().getIdUser();
        } else if (className.equals(Item.class.getName())) {
            return ItemController.extractIdUserFromHeader(request) == ((Item) object).getIdUser();
        } else if (className.equals(Category.class.getName())) {
            return ItemController.extractIdUserFromHeader(request) ==  ((Category) object).getIdUser();
        } else {
            return false;
        }
    }
}
