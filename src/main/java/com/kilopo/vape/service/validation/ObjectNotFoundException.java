package com.kilopo.vape.service.validation;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(Object id) {
        super(String.format("Object %s not found!", id));
    }
}

