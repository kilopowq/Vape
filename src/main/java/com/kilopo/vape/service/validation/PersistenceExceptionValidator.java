package com.kilopo.vape.service.validation;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Map;

public class PersistenceExceptionValidator implements Validator {
    private final ConstraintToCodesMapper codesMapper;
    private final ThrowableAnalyzer throwableAnalyzer = new ThrowableAnalyzer();
    private final RuntimeException exception;

    public PersistenceExceptionValidator(ConstraintToCodesMapper codesMapper, RuntimeException exception) {
        this.codesMapper = codesMapper;
        this.exception = exception;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (exception instanceof DataIntegrityViolationException
                || exception instanceof ConstraintViolationException)
            handleConstraintViolation(errors);
        else if (exception instanceof ObjectNotFoundException)
            handleObjectNotFound(errors);
    }

    private void handleConstraintViolation(Errors errors) {
        ConstraintViolationException violationException =
                (ConstraintViolationException) throwableAnalyzer.getFirstThrowableOfType(
                        ConstraintViolationException.class,
                        throwableAnalyzer.determineCauseChain(exception));

        if (violationException == null)
            return;

        String constraintName = violationException.getConstraintName();
        Map<String, String> mapping = codesMapper.getMapping(constraintName);

        if (mapping == null) {
            errors.reject(constraintName + ".ConstraintViolation");
            return;
        }

        mapping.forEach(errors::rejectValue);
    }

    private void handleObjectNotFound(Errors errors) {
        errors.reject("NotFound");
    }

    @Component
    public static class Factory {
        private final ConstraintToCodesMapper codesMapper;

        Factory(ConstraintToCodesMapper codesMapper) {
            this.codesMapper = codesMapper;
        }

        public PersistenceExceptionValidator create(RuntimeException exception) {
            return new PersistenceExceptionValidator(codesMapper, exception);
        }
    }
}
