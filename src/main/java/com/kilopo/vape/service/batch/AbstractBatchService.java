package com.kilopo.vape.service.batch;

import com.google.common.collect.ImmutableList;
import com.kilopo.vape.domain.Mutation;
import com.kilopo.vape.domain.enums.MutateOperation;
import com.kilopo.vape.service.validation.ObjectNotFoundException;
import com.kilopo.vape.service.validation.PersistenceExceptionValidator;
import com.kilopo.vape.service.validation.ValidationHelper;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;

public abstract class AbstractBatchService<T, ID extends Serializable> {

    @Autowired
    protected EntityManager em;

    @Autowired
    protected PersistenceExceptionValidator.Factory excValidatorFactory;

    @Autowired
    protected ValidationHelper validationHelper;

    @Transactional
    public List<Mutation<T>> validateAndPerformMutations(List<Mutation<T>> mutations) {
        List<List<Errors>> mutationErrorsList = validateMutations(mutations);
        boolean thereIsAtLeastOneErrorInAnyMutation = mutationErrorsList.stream().anyMatch(
                errors -> errors.stream().anyMatch(Errors::hasErrors));
        if (thereIsAtLeastOneErrorInAnyMutation)
            throw new BatchValidationException(mutationErrorsList);
        return doBatchUpdate(mutations);
    }


    private List<Mutation<T>> doBatchUpdate(List<Mutation<T>> mutations) {
        ImmutableList.Builder<Mutation<T>> builder = ImmutableList.builder();
        int i = 0;

        for (Mutation<T> mutation : mutations) {
            try {
                builder.add(doBatchUpdate(mutation));
            } catch (DataIntegrityViolationException | ConstraintViolationException | ObjectNotFoundException e) {
                List<List<Errors>> errors = handleException(mutations, i, e);

                boolean thereIsAtLeastOneErrorInAnyMutation = errors.stream().anyMatch(
                        es -> es.stream().anyMatch(Errors::hasErrors));
                if (thereIsAtLeastOneErrorInAnyMutation)
                    throw new BatchValidationException(errors);
                throw e;
            }
            i++;
        }

        return builder.build();
    }

    private Mutation<T> doBatchUpdate(Mutation<T> m) {
        T entity = m.getEntity();
        switch (m.getOperation()) {
            case Insert:
                T savedEntity = createEntity(entity);
                getSession().flush();
                return new Mutation<>(m.getOperation(), savedEntity);

            case Update:
                T updatedEntity = updateEntity(entity);
                getSession().flush();
                return new Mutation<>(m.getOperation(), updatedEntity);

            case Delete:
                deleteEntity(entity);
                getSession().flush();
                return new Mutation<>(m.getOperation(), entity);

            default:
                throw new RuntimeException("Unreachable code");
        }
    }

    private List<List<Errors>> validateMutations(List<Mutation<T>> mutations) {
        return mutations.stream()
                .map(this::validateMutation)
                .collect(toImmutableList());
    }

    private List<Errors> validateMutation(Mutation<T> m) {
        T entity = m.getEntity();

        //Skip validation for delete
        if (m.getOperation() == MutateOperation.Delete) {
            return validateDelete(entity);
        }

        return validateEntity(entity);
    }

    protected Errors createErrors(Object entity) {
        return new BeanPropertyBindingResult(entity, entity.getClass().getSimpleName());
    }

    private Session getSession() {
        return em.unwrap(Session.class);
    }

    protected List<Errors> validateDelete(T entity) {
        return Arrays.asList(validationHelper.buildErrors(entity));
    }

    protected abstract List<Errors> validateEntity(T entity);

    protected abstract List<List<Errors>> handleException(List<Mutation<T>> ms, int index, RuntimeException exception);

    protected abstract T createEntity(T entity);

    protected abstract T updateEntity(T entity);

    protected abstract void deleteEntity(T entity);
}
