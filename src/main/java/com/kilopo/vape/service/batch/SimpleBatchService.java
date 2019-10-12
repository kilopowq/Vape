package com.kilopo.vape.service.batch;

import com.kilopo.vape.domain.Mutation;
import com.kilopo.vape.service.validation.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.Errors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class SimpleBatchService<T, ID extends Serializable> extends AbstractBatchService<T, ID> {

    @Autowired
    protected JpaRepository<T, ID> repo;

    @Override
    protected List<Errors> validateEntity(T entity) {
        return Arrays.asList(validationHelper.validateEntity(entity));
    }

    @Override
    protected List<List<Errors>> handleException(List<Mutation<T>> ms, int index, RuntimeException exception) {
        List<Errors> errorList = ms.stream()
                .map(m -> createErrors(m.getEntity()))
                .collect(toImmutableList());

        Errors errors = errorList.get(index);

        excValidatorFactory.create(exception).validate(ms.get(index), errors);

        return errorList.stream().map(e -> Arrays.asList(e)).collect(toImmutableList());
    }

    @Override
    protected T createEntity(T entity) {
        return repo.save(entity);
    }

    @Override
    protected T updateEntity(T entity) {
        return repo.save(entity);
    }

    @Override
    protected void deleteEntity(T entity) {
        ID entityId = getEntityId(entity);
        if (repo.existsById(entityId)) {
            repo.deleteById(entityId);
        } else {
            throw new ObjectNotFoundException(entityId);
        }
    }

    @SuppressWarnings("unchecked")
    private ID getEntityId(Object entity) {
        return (ID) em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
    }
}
