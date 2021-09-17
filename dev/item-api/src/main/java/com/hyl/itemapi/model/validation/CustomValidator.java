package com.hyl.itemapi.model.validation;

import com.hyl.itemapi.exception.CustomBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class CustomValidator {

    private static Validator validator;

    @Autowired
    public void setValidator(@Qualifier("getValidator") Validator validator) {
        CustomValidator.validator = validator;
    }

    public static void validate(Object obj, Class<?> group) {
        Set<ConstraintViolation<Object>> violations = validator.validate(obj, group);
        if (!violations.isEmpty()) {
            StringBuilder message = new StringBuilder("Erreurs{[");
            violations.iterator().forEachRemaining(violation -> {
                message.append(violation.getMessage()).append("], [");
            });
            message.replace(message.length() - 3, message.length(), "}.");
            throw new CustomBadRequestException("L'objet "+obj.getClass().getName()+" est invalide -> "+message);
        }
    }
}
