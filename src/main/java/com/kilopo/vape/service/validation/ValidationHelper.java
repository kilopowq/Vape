package com.kilopo.vape.service.validation;

import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Arrays;

@Component
public class ValidationHelper {
    private final Validator validator;
    private final PersistenceExceptionValidator.Factory excValidatorFactory;

    ValidationHelper(Validator validator, PersistenceExceptionValidator.Factory excValidatorFactory) {
        this.validator = validator;
        this.excValidatorFactory = excValidatorFactory;
    }

    public static BeanPropertyBindingResult buildErrors(Object entity) {
        return new BeanPropertyBindingResult(entity, entity.getClass().getSimpleName());
    }

    public Errors validateEntity(Object entity, Validator... customValidators) {
        Errors errors = buildErrors(entity);
        ValidationUtils.invokeValidator(validator, entity, errors);
        Arrays.stream(customValidators).forEach(v -> ValidationUtils.invokeValidator(v, entity, errors));
        return errors;
    }

    public void throwingValidateEntity(Object entity, Validator... customValidators) {
        Errors errors = buildErrors(entity);
        ValidationUtils.invokeValidator(validator, entity, errors);
        Arrays.stream(customValidators).forEach(v -> ValidationUtils.invokeValidator(v, entity, errors));

        if (errors.hasErrors())
            throw new RepositoryConstraintViolationException(errors);
    }

    public RuntimeException convertToValidationException(RuntimeException e, Object entity) {
        Errors errors = buildErrors(entity);
        ValidationUtils.invokeValidator(excValidatorFactory.create(e), entity, errors);

        if (errors.hasErrors())
            return new RepositoryConstraintViolationException(errors);
        return e;
    }
}
