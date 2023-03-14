package ru.javawebinar.topjava.util;

import org.springframework.util.CollectionUtils;

import javax.validation.*;
import java.util.Set;

public class ValidationService {

    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private ValidationService() {
    }

    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
