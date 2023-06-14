package org.example.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.employee.*;
import org.example.exception.EmployeeDeletedException;
import org.example.exception.EmployeeNotExistsException;
import org.example.exception.EntityAlreadyExistsException;
import org.example.mapper.employee.CreateEmployeeMapper;
import org.example.mapper.employee.FindEmployeesMapper;
import org.example.mapper.employee.UpdateEmployeeMapper;
import org.example.service.specification.FindEmployeeSpecification;
import org.example.service.utils.ServiceUtils;
import org.example.status.EmployeeStatus;
import org.example.store.model.Employee;
import org.example.store.repository.EmployeeRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    ObjectProvider<FindEmployeeSpecification> findEmployeeSpecificationProvider;

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
                repository::findById, employeeData::getId, () -> new EmployeeNotExistsException(
                        "Employee with id %d doesn't exist and so can't be updated".formatted(employeeData.getId())
                )
        );
        if (storedEmployee.getStatus().equals(EmployeeStatus.DELETED)) {
            throw new EmployeeDeletedException("Employee is deleted before. Deleted employees can't be updated");
        }
        final Employee updatedEmployee = updateEmployeeMapper.mapToEntity(employeeData);
        updatedEmployee.setStatus(EmployeeStatus.ACTIVE);
        repository.save(updatedEmployee);
    }

    public void deleteEmployeeProfile(@NonNull Long employeeId) {
        final Employee storedEmployee = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, () -> employeeId, () -> new EmployeeNotExistsException(
                        "Employee with id %d doesn't exist and so can't be deleted".formatted(employeeId)
                )
        );
        storedEmployee.setStatus(EmployeeStatus.DELETED);
        repository.save(storedEmployee);
    }

    public FoundEmployeesDTO findEmployeesByFilter(FindEmployeesDTO filter) {
        final List<FoundEmployeeDTO> foundEmployees = repository
                .findAll(findEmployeeSpecificationProvider.getObject(filter))
                .stream()
                .map(findEmployeesMapper::mapToResult)
                .toList();
        return new FoundEmployeesDTO(foundEmployees);
    }

    public FoundEmployeeDTO findEmployeeById(@NonNull Long employeeId) {
        return findEmployeeByIdentifier(
                repository::findById, employeeId, () -> new EmployeeNotExistsException(
                        "Employee with id %d doesn't exists".formatted(employeeId)
                )
        );
    }

    public FoundEmployeeDTO findEmployeeByAccount(@NonNull String account) {
        return findEmployeeByIdentifier(
                repository::findByAccount, account, () -> new EmployeeNotExistsException(
                        "Employee profile with account %s doesn't exists".formatted(account)
                )
        );
    }

    Employee getEmployeeEntityById(@NonNull Long employeeId) {
        final Employee employee = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, () -> employeeId,
                () -> new EmployeeNotExistsException(
                        "Employee with id %d doesn't exists".formatted(employeeId)
                )
        );
        if (employee.getStatus().equals(EmployeeStatus.DELETED)) {
            throw new EmployeeDeletedException("Employee with id %d was deleted before".formatted(employeeId));
        }
        return employee;
    }

    Employee getEmployeeEntityByAccount(@NonNull String account) {
        return repository.findByAccount(account).orElseThrow(
                () -> new EmployeeNotExistsException("Employee with account %s doesn't exists".formatted(account))
        );
    }

    private void checkAccountIsUniqueBetweenActives(String account) {
        checkAccountIsUniqueBetweenActives(account, null);
    }

    private void checkAccountIsUniqueBetweenActives(String account, Long currentEmployeeId) {
        ServiceUtils.checkValueIsUniqueWithPredicateOrThrow(
                repository::findByAccount, () -> account,
                foundEmployee -> !foundEmployee.getId().equals(currentEmployeeId),
                () -> new EntityAlreadyExistsException(
                        "Active employee with account %s already exists".formatted(account)
                )
        );
    }

    private <IdentifierType> FoundEmployeeDTO findEmployeeByIdentifier(
            @NonNull Function<? super IdentifierType, Optional<? extends Employee>> extractor,
            @NonNull IdentifierType extractorParam,
            @NonNull Supplier<? extends RuntimeException> exceptionSupplier
    ) {
        Employee foundEmployee = extractor.apply(extractorParam).orElseThrow(exceptionSupplier);
        return findEmployeesMapper.mapToResult(foundEmployee);
    }
}
