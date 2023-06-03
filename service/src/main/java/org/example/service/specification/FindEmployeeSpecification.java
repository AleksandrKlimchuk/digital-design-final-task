package org.example.service.specification;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.employee.FindEmployeesDTO;
import org.example.service.utils.SpecificationUtils;
import org.example.status.EmployeeStatus;
import org.example.store.model.Employee;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FindEmployeeSpecification implements Specification<Employee> {

    FindEmployeesDTO filter;

    @Override
    public Predicate toPredicate(
            @NonNull Root<Employee> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder
    ) {
        final String filterData = SpecificationUtils.createPatternFromText(filter.getData());
        final List<Predicate> textPredicates = List.of(
                SpecificationUtils.ilike(root.get("firstName"), filterData, criteriaBuilder),
                SpecificationUtils.ilike(root.get("lastName"), filterData, criteriaBuilder),
                SpecificationUtils.ilike(root.get("patronymic"), filterData, criteriaBuilder),
                SpecificationUtils.ilike(root.get("account"), filterData, criteriaBuilder),
                SpecificationUtils.ilike(root.get("email"), filterData, criteriaBuilder)
        );
        final Predicate text = criteriaBuilder.or(SpecificationUtils.predicatesToArray(textPredicates));
        final Predicate status = criteriaBuilder.equal(root.get("status"), EmployeeStatus.ACTIVE);
        final Predicate textAndStatus = criteriaBuilder.and(text, status);
        return query
                .where(textAndStatus)
                .getRestriction();
    }
}
