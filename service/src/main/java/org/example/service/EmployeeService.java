package org.example.service;

import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.employee.*;
import org.example.mapper.employee.CreateEmployeeMapper;
import org.example.mapper.employee.FindEmployeesMapper;
import org.example.mapper.employee.UpdateEmployeeMapper;
import org.example.service.utils.ServiceUtils;
import org.example.status.EmployeeStatus;
import org.example.store.model.Employee;
import org.example.store.repository.EmployeeRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeService {

    EmployeeRepository repository;

    CreateEmployeeMapper createEmployeeMapper;
    UpdateEmployeeMapper updateEmployeeMapper;
    FindEmployeesMapper findEmployeesMapper;

    public CreatedEmployeeDTO createEmployeeProfile(@NonNull CreateEmployeeDTO employeeData) {
        checkAccountIsUniqueBetweenActives(employeeData.getAccount());
        final Employee employee = createEmployeeMapper.mapToEntity(employeeData);
        employee.setStatus(EmployeeStatus.ACTIVE);
        final Employee savedEmployee = repository.save(employee);
        return createEmployeeMapper.mapToResult(savedEmployee);
    }

    public void updateEmployeeProfile(@NonNull UpdateEmployeeDTO employeeData) {
        checkAccountIsUniqueBetweenActives(employeeData.getAccount(), employeeData.getId());
        final Employee storedEmployee = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, employeeData::getId, () -> new IllegalArgumentException(
                        "Employee with id %d doesn't exist and so can't be updated".formatted(employeeData.getId())
                )
        );
        if (storedEmployee.getStatus().equals(EmployeeStatus.DELETED)) {
            throw new IllegalArgumentException("Employee is deleted before. Deleted employees can't be updated");
        }
        final Employee updatedEmployee = updateEmployeeMapper.mapToEntity(employeeData);
        updatedEmployee.setStatus(EmployeeStatus.ACTIVE);
        repository.save(updatedEmployee);
    }

    public void deleteEmployeeProfile(@NonNull DeleteEmployeeDTO employeeData) {
        final Employee storedEmployee = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, employeeData::getId, () -> new IllegalArgumentException(
                        "Employee with id %d doesn't exist and so can't be deleted".formatted(employeeData.getId())
                )
        );
        storedEmployee.setStatus(EmployeeStatus.DELETED);
        repository.save(storedEmployee);
    }

    public FoundEmployeesDTO findEmployeesByFilter(@NonNull FindEmployeesDTO filter) {
        final List<FoundEmployeeDTO> foundEmployees = repository
                .findAll(getFilterSpecification(filter))
                .stream()
                .map(findEmployeesMapper::mapToResult)
                .toList();
        return new FoundEmployeesDTO(foundEmployees);
    }

    public Optional<FoundEmployeeDTO> findEmployee(@NonNull FindEmployeeDTO identifier) {
        return Objects.isNull(identifier.getAccount())
                ? findEmployeeById(identifier)
                : findEmployeeByAccount(identifier);
    }

    Employee getEmployeeEntityById(@NonNull Long id) {
        final Employee employee = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, () -> id,
                () -> new IllegalArgumentException(
                        "Employee with id %d doesn't exists".formatted(id)
                )
        );
        if (employee.getStatus().equals(EmployeeStatus.DELETED)) {
            throw new IllegalArgumentException("Employee with id %d was deleted before".formatted(id));
        }
        return employee;
    }

    private Specification<Employee> getFilterSpecification(FindEmployeesDTO filter) {
        return (root, query, criteriaBuilder) -> {
            final String filterData = "%" + filter.getData() + "%";
            final List<Predicate> textPredicates = List.of(
                    criteriaBuilder.like(root.get("firstName"), filterData),
                    criteriaBuilder.like(root.get("lastName"), filterData),
                    criteriaBuilder.like(root.get("patronymic"), filterData),
                    criteriaBuilder.equal(root.get("account"), getUUIDFromString(filterData)),
                    criteriaBuilder.like(root.get("email"), filterData)
            );
            final Predicate text = criteriaBuilder.or(textPredicates.toArray(Predicate[]::new));
            final Predicate status = criteriaBuilder.equal(root.get("status"), EmployeeStatus.ACTIVE);
            final Predicate textAndStatus = criteriaBuilder.and(text, status);
            return query
                    .where(textAndStatus)
                    .getRestriction();
        };
    }

    private static UUID getUUIDFromString(String str) {
        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void checkAccountIsUniqueBetweenActives(UUID account) {
        checkAccountIsUniqueBetweenActives(account, null);
    }

    private void checkAccountIsUniqueBetweenActives(UUID account, Long currentEmployeeId) {
        ServiceUtils.checkValueIsUniqueWithPredicateOrThrow(
                repository::findByAccount, () -> account,
                foundEmployee -> foundEmployee.getStatus().equals(EmployeeStatus.ACTIVE)
                        && !foundEmployee.getId().equals(currentEmployeeId),
                () -> new IllegalArgumentException("Active employee with account %s already exists".formatted(account))
        );
    }

    private Optional<FoundEmployeeDTO> findEmployeeById(@NonNull FindEmployeeDTO identifier) {
        return findEmployeeByIdentifier(repository::findById, identifier::getId);
    }

    private Optional<FoundEmployeeDTO> findEmployeeByAccount(@NonNull FindEmployeeDTO identifier) {
        return findEmployeeByIdentifier(repository::findByAccount, identifier::getAccount);
    }

    private <IdentifierType> Optional<FoundEmployeeDTO> findEmployeeByIdentifier(
            @NonNull Function<? super IdentifierType, Optional<? extends Employee>> extractor,
            @NonNull Supplier<IdentifierType> extractorParamSupplier
    ) {
        Optional<? extends Employee> foundEmployee = extractor.apply(extractorParamSupplier.get());
        return foundEmployee.map(findEmployeesMapper::mapToResult);
    }
}
