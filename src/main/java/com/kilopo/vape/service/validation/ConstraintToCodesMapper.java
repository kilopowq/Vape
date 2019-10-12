package com.kilopo.vape.service.validation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.kilopo.vape.repository.PgConstraintRepository;
import com.kilopo.vape.repository.PgConstraintRepository.Constraint;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

@Component
public class ConstraintToCodesMapper {
    private final PgConstraintRepository repo;
    private final EntityManagerFactory emf;

    private final Set<Class<?>> scanEntites = ImmutableSet.of();

    private volatile Map<String, Map<String, String>> mapping;

    public ConstraintToCodesMapper(PgConstraintRepository repo, EntityManagerFactory emf) {
        this.repo = repo;
        this.emf = emf;
    }

    @PostConstruct
    void init() {
        Map<String, Set<Constraint>> tableToConstraints = repo.getTablesToConstraints();
        Map<String, Map<String, String>> mutableMapping = scanEntites.stream()
                .map(c -> scanEntity(tableToConstraints, c))
                .collect(HashMap::new, Map::putAll, Map::putAll);

        mapping = ImmutableMap.copyOf(mutableMapping);
    }

    public Map<String, String> getMapping(String constraintName) {
        return mapping.get(constraintName);
    }

    private Map<String, Map<String, String>> scanEntity(
            Map<String, Set<Constraint>> tableToConstraints, Class<?> entityClass) {

        AbstractEntityPersister metadata = (AbstractEntityPersister) getSessionFactory().getClassMetadata(entityClass);
        String tableName = metadata.getTableName();
        Map<String, String> columnsToFields = columnsToFields(metadata);
        Set<Constraint> constraints = tableToConstraints.get(tableName);

        return constraints.stream().collect(toImmutableMap(
                c -> c.name,
                c -> columnsToValidationMapping(columnsToFields, c)
        ));
    }

    private Map<String, String> columnsToValidationMapping(Map<String, String> columnsToFields, Constraint constraint) {
        return constraint.columnsname.stream().collect(toImmutableMap(
                columnsToFields::get,
                name -> constraint.type.toString()
        ));
    }

    private Map<String, String> columnsToFields(AbstractEntityPersister metadata) {
        Map<String, String> result = new HashMap<>();
        result.putAll(
                Arrays.stream(metadata.getPropertyNames()).collect(toImmutableMap(
                        name -> metadata.getPropertyColumnNames(name)[0],
                        name -> name
                )));
        result.put(
                metadata.getIdentifierColumnNames()[0],
                metadata.getIdentifierPropertyName());
        return ImmutableMap.copyOf(result);
    }

    private SessionFactory getSessionFactory() {
        return emf.unwrap(SessionFactory.class);
    }

}
