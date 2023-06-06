package org.example.service.specification;

import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.project.FindProjectsDTO;
import org.example.service.utils.SpecificationUtils;
import org.example.status.ProjectStatus;
import org.example.store.model.Project;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FindProjectSpecification implements Specification<Project> {

    FindProjectsDTO filter;

    @Override
    public Predicate toPredicate(
            @NonNull Root<Project> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder
    ) {
        final String textFilter = SpecificationUtils.createPatternFromText(filter.getText());
        final List<Predicate> predicates = new ArrayList<>();
        final List<Predicate> textPredicates = List.of(
                SpecificationUtils.ilike(root.get("code"), textFilter, criteriaBuilder),
                SpecificationUtils.ilike(root.get("title"), textFilter, criteriaBuilder)
        );
        predicates.add(criteriaBuilder.or(SpecificationUtils.predicatesToArray(textPredicates)));
        if (!Objects.isNull(filter.getStatuses()) && !filter.getStatuses().isEmpty()) {
            final Expression<ProjectStatus> statusExpression = root.get("status");
            predicates.add(statusExpression.in(filter.getStatuses()));
        }
        final Predicate finalPredicate = criteriaBuilder.and(SpecificationUtils.predicatesToArray(predicates));
        return query
                .where(finalPredicate)
                .getRestriction();
    }
}
