package com.hyl.userapi.model.constraint.validator;

import com.hyl.userapi.model.constraint.NoWhiteSpaceConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoWhiteSpaceValidator implements ConstraintValidator<NoWhiteSpaceConstraint, String> {

    @Override
    public void initialize(NoWhiteSpaceConstraint noWhiteSpaceConstraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {

        // Espacement sur les cotés interdits
        if (field == null || field.length() != field.trim().length()) {
            return false;
        }

        // Double espacement dans une chaine de caractères interdits
        for (int i = 0; i < field.length(); i++) {
            try {
                if (field.substring(i, i + 1).equals(" ") && field.substring(i + 1, i + 2).equals(" ")) {
                    return false;
                }
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        // Aucune erreur de validation
        return true;
    }
}
