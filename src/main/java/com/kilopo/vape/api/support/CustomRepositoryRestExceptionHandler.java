package com.kilopo.vape.api.support;


import com.fasterxml.jackson.annotation.JsonRootName;
import com.kilopo.vape.api.IndexController;
import com.kilopo.vape.service.batch.BatchValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.data.rest.webmvc.support.ExceptionMessage;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.toImmutableList;


@ControllerAdvice(basePackageClasses = {
        RepositoryRestExceptionHandler.class,
        IndexController.class})
public class CustomRepositoryRestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomRepositoryRestExceptionHandler.class);

    @ExceptionHandler
    ResponseEntity<Resource<EntityValidationErrors>> handleRepositoryConstraintViolationException(
            RepositoryConstraintViolationException exception) {

        EntityValidationErrors msgs = EntityValidationErrors.toErrors(exception.getErrors());

        return response(HttpStatus.BAD_REQUEST, new HttpHeaders(), new Resource<>(msgs));
    }

    @ExceptionHandler
    ResponseEntity<Resources<EntityValidationErrors>> handleBatchValidationException(BatchValidationException exception) {

        List<EntityValidationErrors> msgs = exception.getErrors().stream()
                .map(EntityValidationErrors::toErrors)
                .collect(toImmutableList());

        return response(HttpStatus.BAD_REQUEST, new HttpHeaders(), new Resources<>(msgs));
    }

    @ExceptionHandler({OptimisticLockingFailureException.class, DataIntegrityViolationException.class})
    ResponseEntity<ExceptionMessage> handleConflict(Exception exception) {

        return errorResponse(HttpStatus.CONFLICT, new HttpHeaders(), exception);
    }

    private static ResponseEntity<ExceptionMessage> errorResponse(HttpStatus status, HttpHeaders headers,
                                                                  Exception exception) {
        if (exception != null) {

            String message = exception.getMessage();
            logger.error(message, exception);

            if (StringUtils.hasText(message)) {
                return response(status, headers, new ExceptionMessage(exception));
            }
        }

        return response(status, headers, null);
    }

    private static <T> ResponseEntity<T> response(HttpStatus status, HttpHeaders headers, T body) {
        Assert.notNull(headers, "Headers must not be null!");
        Assert.notNull(status, "HttpStatus must not be null!");

        return new ResponseEntity<>(body, headers, status);
    }

    private static class ValidationError {
        public final String entity;
        public final String property;
        public final Object invalidValue;
        public final String message;
        public final Object[] arguments;

        private ValidationError(String entity, String property, Object invalidValue, String message, Object[] arguments) {
            this.entity = entity;
            this.property = property;
            this.invalidValue = invalidValue;
            this.message = message;
            this.arguments = arguments;
        }
    }

    @JsonRootName("validationError")
    private static class EntityValidationErrors {
        public final List<ValidationError> errors;

        private EntityValidationErrors(List<ValidationError> errors) {
            this.errors = errors;
        }

        private static EntityValidationErrors toErrors(Errors errors) {
            return toErrors(Arrays.asList(errors));
        }

        private static EntityValidationErrors toErrors(List<Errors> springErrors) {
            List<ObjectError> objectErrors = springErrors.stream().flatMap(
                    entityErrors -> entityErrors.getAllErrors().stream()
            ).collect(Collectors.toList());
            List<ValidationError> validationErrors = objectErrors.stream()
                    .map(e -> {
                        if (e instanceof FieldError) {
                            FieldError fe = (FieldError) e;
                            return new ValidationError(
                                    fe.getObjectName(), fe.getField(), fe.getRejectedValue(), fe.getCode(), e.getArguments());
                        }
                        return new ValidationError(e.getObjectName(), null, null, e.getCode(), e.getArguments());
                    })
                    .collect(toImmutableList());

            return new EntityValidationErrors(validationErrors);
        }
    }
}
