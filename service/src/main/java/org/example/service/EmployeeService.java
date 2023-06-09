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
import java.util.Objects;
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

    public void deleteEmployeeProfile(@NonNull DeleteEmployeeDTO employeeData) {
        final Employee storedEmployee = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, employeeData::getId, () -> new EmployeeNotExistsException(
                        "Employee with id %d doesn't exist and so can't be deleted".formatted(employeeData.getId())
                )
        );
        storedEmployee.setStatus(EmployeeStatus.DELETED);
        repository.save(storedEmployee);
    }

    public FoundEmployeesDTO findEmployeesByFilter(@NonNull FindEmployeesDTO filter) {
        final List<FoundEmployeeDTO> foundEmployees = repository
                .findAll(findEmployeeSpecificationProvider.getObject(filter))
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
                () -> new EmployeeNotExistsException(
                        "Employee with id %d doesn't exists".formatted(id)
                )
        );
        if (employee.getStatus().equals(EmployeeStatus.DELETED)) {
            throw new EmployeeDeletedException("Employee with id %d was deleted before".formatted(id));
        }
        return employee;
    }

    private void checkAccountIsUniqueBetweenActives(String account) {
        checkAccountIsUniqueBetweenActives(account, null);
    }

    private void checkAccountIsUniqueBetweenActives(String account, Long currentEmployeeId) {
        ServiceUtils.checkValueIsUniqueWithPredicateOrThrow(
                repository::findByAccount, () -> account,
                foundEmployee -> foundEmployee.getStatus().equals(EmployeeStatus.ACTIVE)
                        && !foundEmployee.getId().equals(currentEmployeeId),
                () -> new EntityAlreadyExistsException(
                        "Active employee with account %s already exists".formatted(account)
                )
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
