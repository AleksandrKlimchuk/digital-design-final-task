package org.example.service.specification;

import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.task.FindTasksDTO;
import org.example.service.utils.SpecificationUtils;
import org.example.status.ProjectStatus;
import org.example.store.model.Task;
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
public class FindTaskSpecification implements Specification<Task> {

    FindTasksDTO filter;

    @Override
    public Predicate toPredicate(
            @NonNull Root<Task> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder
    ) {
        final String textFilter = SpecificationUtils.createPatternFromText(filter.getTitle());
        final List<Predicate> predicates = new ArrayList<>();
        if (!Objects.isNull(filter.getTitle())) {
            predicates.add(SpecificationUtils.ilike(root.get("title"), textFilter, criteriaBuilder));
        }
        if (!Objects.isNull(filter.getStatuses()) && !filter.getStatuses().isEmpty()) {
            final Expression<ProjectStatus> statusExpression = root.get("status");
            predicates.add(statusExpression.in(filter.getStatuses()));
        }
        if (!Objects.isNull(filter.getExecutorId())) {
            predicates.add(criteriaBuilder.equal(root.get("executor").get("id"), filter.getExecutorId()));
        }
        if (!Objects.isNull(filter.getAuthorId())) {
            predicates.add(criteriaBuilder.equal(root.get("author").get("id"), filter.getAuthorId()));
        }
        if (!Objects.isNull(filter.getDeadline())) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("deadline"), filter.getDeadline()));
        }
        if (!Objects.isNull(filter.getCreatedAt())) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedAt()));
        }
        final Predicate finalPredicate = criteriaBuilder.and(SpecificationUtils.predicatesToArray(predicates));
        return query
                .where(finalPredicate)
                .getRestriction();
    }
}
