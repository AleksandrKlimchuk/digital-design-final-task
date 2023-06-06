package org.example.service.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class SpecificationUtils {

    public String createPatternFromText(String text) {
        return "%" + Objects.requireNonNullElse(text, "") + "%";
    }

    public Predicate ilike(
            Expression<String> entityColumnValue, String pattern, @NonNull CriteriaBuilder criteriaBuilder
    ) {
        return criteriaBuilder.like(criteriaBuilder.lower(entityColumnValue), pattern.toLowerCase());
    }

    public Predicate[] predicatesToArray(@NonNull List<Predicate> predicates) {
        return predicates.toArray(Predicate[]::new);
    }
}
