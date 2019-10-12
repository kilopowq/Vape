package com.kilopo.vape.service.batch;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;

import java.util.List;

public class BatchValidationException extends DataIntegrityViolationException {
    private final List<List<Errors>> errors;

    BatchValidationException(List<List<Errors>> errors) {
        super("Validation failed!");
        this.errors = errors;
    }

    public List<List<Errors>> getErrors() {
        return errors;
    }
}