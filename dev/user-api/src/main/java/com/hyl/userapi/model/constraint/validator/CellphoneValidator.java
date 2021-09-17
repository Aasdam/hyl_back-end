package com.hyl.userapi.model.constraint.validator;

import com.hyl.userapi.model.constraint.CellphoneConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CellphoneValidator implements ConstraintValidator<CellphoneConstraint, String> {

    @Override
    public void initialize(CellphoneConstraint cellphoneConstraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {

        if (field != null && !field.isEmpty()) {

            // Vérification longueur numéro + aucun espace
           if ( field.length() != 10 || field.contains(" ") ) return false;

           // Vérification que c'est bien un numéro de téléphone qui a été saisi
           try {
               Integer.parseInt(field);
           } catch (NumberFormatException ignored) {
               return false;
           }

           // Dernière vérification -> le numéro doit commencé par 06 ou 07
            return ( field.substring(0, 2).equals("06") || field.substring(0, 2).equals("07") );

        } else return true;

    }
}
