package com.kilopo.vape.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kilopo.vape.domain.enums.MutateOperation;

public class Mutation<T> {
    private final MutateOperation operation;
    private final T entity;

    @JsonCreator
    public Mutation(@JsonProperty("operation") MutateOperation operation, @JsonProperty("entity") T entity) {
        this.operation = operation;
        this.entity = entity;
    }

    public MutateOperation getOperation() {
        return operation;
    }

    public T getEntity() {
        return entity;
    }
}
