package com.kilopo.vape.repository;

import com.diffplug.common.base.Errors;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

@Repository
public class PgConstraintRepository {
    private final JdbcTemplate jdbcTemplate;

    public PgConstraintRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Set<Constraint>> getTablesToConstraints() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(QUERY);

        Map<String, ? extends Set<Constraint>> grouped = rows.stream()
                .map(Errors.rethrow().wrap(PgConstraintRepository::rowMapper))
                .collect(Collectors.groupingBy(
                        row -> row.tableName,
                        Collectors.mapping(row -> row.constraint, toImmutableSet())));

        return ImmutableMap.copyOf(grouped);
    }

    private static Row rowMapper(Map<String, Object> row) throws SQLException {
        String tableName = (String) row.get("table_name");
        String constraintName = (String) row.get("constraint_name");
        Array arr = (Array) row.get("columnsname");
        Set<String> columns = ImmutableSet.copyOf((String[]) arr.getArray());
        ConstraintType type = ConstraintType.rawToType((String) row.get("contype"));
        return new Row(tableName, new Constraint(constraintName, columns, type));
    }

    public static class Constraint {
        public final String name;
        public final Set<String> columnsname;
        public final ConstraintType type;

        Constraint(String name, Set<String> columnsname, ConstraintType type) {
            this.name = name;
            this.columnsname = columnsname;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Constraint that = (Constraint) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(columnsname, that.columnsname) &&
                    Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, columnsname, type);
        }
    }

    private static class Row {
        private final String tableName;
        private final Constraint constraint;

        private Row(String tableName, Constraint constraint) {
            this.tableName = tableName;
            this.constraint = constraint;
        }
    }

    public static enum ConstraintType {
        Unique, Foreign;

        static ConstraintType rawToType(String raw) {
            if (raw.equals("u"))
                return Unique;
            if (raw.equals("f"))
                return Foreign;
            throw new RuntimeException("Unknown constraint type: " + raw);
        }
    }

    private static final String QUERY = String.join("\n",
            "WITH c AS (",
            "  SELECT",
            "  conrelid conrelid, conname constraint_name, unnest(conkey) column_index, contype",
            "  FROM pg_constraint",
            "  WHERE contype = 'u'",
            "  UNION",
            "  SELECT",
            "  confrelid conrelid, conname constraint_name, unnest(confkey) column_index, contype",
            "  FROM pg_constraint",
            "  WHERE contype = 'f')",
            "SELECT",
            "tables.relname as table_name, constraint_name, array_agg(attname) columnsName, contype",
            "FROM c",
            "INNER JOIN pg_class tables ON",
            "(c.conrelid = tables.oid)",
            "INNER JOIN pg_attribute a ON",
            "(c.conrelid = a.attrelid AND column_index = a.attnum)",
            "GROUP BY table_name, constraint_name, contype");

}

