package org.example.store.repository.jdbc_repository;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.example.dto.filter.SearchFilterDTO;
import org.example.status.EmployeeStatus;
import org.example.store.model.Employee;
import org.example.store.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class EmployeeJdbcRepository implements Repository<Employee> {

    private static final String INSERT_QUERY = """
            INSERT INTO employee (first_name, last_name, patronymic, account, email, status)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_QUERY = """
            UPDATE employee
            SET first_name=?, last_name=?, patronymic=?, account=?, email=?, status=?
            WHERE id=?
            """;

    private static final String SELECT_BY_ID_QUERY = """
            SELECT * FROM employee WHERE id=?
            """;

    private static final String SELECT_ALL_QUERY = """
            SELECT * FROM employee
            """;

    private static final String DELETE_BY_ID_QUERY = """
            DELETE FROM employee WHERE id=?
            """;

    // Выборка сотрудников по имени проекта и двум необязательным параметрам: роль сотрудника в проекте
    // и префик электронной почты сотрудника
    private static final String SELECT_WITH_FILTER_QUERY_BASE = """
            select emp.id, first_name, last_name, patronymic, account, email, emp.status from employee emp
                join project_team pt on emp.id = pt.employee_id
                join project p on pt.project_id = p.id
                where p.title = ?
            """;
    private static final String SELECT_WITH_FILTER_PROJECT_ROLE_EXTENSION = """
            and pt.role=?
            """;
    private static final String SELECT_WITH_FILTER_EMAIL_PREFIX_EXTENSION = """
            and email like ?;
            """;


    @Override
    @SneakyThrows
    public Employee create(@NonNull Employee entity) {
        if (!Objects.isNull(entity.getId())) {
            throw new RuntimeException("Insert employee %s interrupted. To save entity, id must be unspecified");
        }
        try (
                final Connection connection = ConnectionPool.getConnection();
                final PreparedStatement insertStatement = connection.prepareStatement(
                        INSERT_QUERY, Statement.RETURN_GENERATED_KEYS
                )
        ) {
            fillInPreparedStatement(insertStatement, entity);
            final int affectedRows = insertStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Insert operation did not execute");
            }
            try (final ResultSet generatedValues = insertStatement.getGeneratedKeys()) {
                generatedValues.next();
                final long generatedId = generatedValues.getLong("id");
                return entity.withId(generatedId);
            }
        }
    }

    @Override
    @SneakyThrows
    public Employee update(@NonNull Employee entity) {
        if (Objects.isNull(entity.getId())) {
            throw new RuntimeException("Update employee %s interrupted. To save entity, id must be specified");
        }
        try (
                final Connection connection = ConnectionPool.getConnection();
                final PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            final int paramPlace = fillInPreparedStatement(updateStatement, entity);
            updateStatement.setLong(paramPlace, entity.getId());
            final int affectedRows = updateStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException(
                        "Update operation did not execute. Employee with id %d doesn't exist".formatted(entity.getId())
                );
            }
            return entity;
        }
    }

    @Override
    @SneakyThrows
    public Optional<? extends Employee> getById(@NonNull Long id) {
        try (
                final Connection connection = ConnectionPool.getConnection();
                final PreparedStatement selectStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)
        ) {
            selectStatement.setLong(1, id);
            try (final ResultSet selectedRow = selectStatement.executeQuery()) {
                return selectedRow.next()
                        ? Optional.of(fillOutEmployeeByRow(selectedRow))
                        : Optional.empty();
            }
        }
    }

    @Override
    @SneakyThrows
    public List<? extends Employee> getAll() {
        try (
                final Connection connection = ConnectionPool.getConnection();
                final Statement selectStatement = connection.createStatement();
                final ResultSet selectedRows = selectStatement.executeQuery(SELECT_ALL_QUERY)
        ) {
            final List<Employee> selectedEmployees = new ArrayList<>();
            while (selectedRows.next()) {
                selectedEmployees.add(fillOutEmployeeByRow(selectedRows));
            }
            return selectedEmployees;
        }
    }

    @SneakyThrows
    public List<? extends Employee> getAllWithFilter(@NonNull SearchFilterDTO searchFilter) {
        try (
                final Connection connection = ConnectionPool.getConnection();
                final PreparedStatement selectStatement = createSelectPreparedStatementWithFilter(
                        connection, searchFilter
                );
                final ResultSet selectedRows = selectStatement.executeQuery()
        ) {
            final List<Employee> selectedEmployees = new ArrayList<>();
            while (selectedRows.next()) {
                selectedEmployees.add(fillOutEmployeeByRow(selectedRows));
            }
            return selectedEmployees;
        }
    }

    @Override
    @SneakyThrows
    public Employee deleteById(@NonNull Long id) {
        try (
                final Connection connection = ConnectionPool.getConnection();
                final PreparedStatement deleteStatement = connection.prepareStatement(DELETE_BY_ID_QUERY)
        ) {
            final Employee employee = getById(id).orElseThrow(() -> new IllegalArgumentException(
                    "Employee with id %d doesn't exist".formatted(id)
            ));
            deleteStatement.setLong(1, id);
            final int affectedRows = deleteStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Delete operation did not execute. May be id is invalid");
            }
            return employee;
        }
    }

    @SneakyThrows
    private PreparedStatement createSelectPreparedStatementWithFilter(
            @NonNull Connection connection,
            @NonNull SearchFilterDTO searchFilterDTO
    ) {
        final StringBuilder extendedSelectQuery = new StringBuilder(SELECT_WITH_FILTER_QUERY_BASE);
        if (!Objects.isNull(searchFilterDTO.getRole())) {
            extendedSelectQuery.append(SELECT_WITH_FILTER_PROJECT_ROLE_EXTENSION);
        }
        if (!Objects.isNull(searchFilterDTO.getEmailPrefix())) {
            extendedSelectQuery.append(SELECT_WITH_FILTER_EMAIL_PREFIX_EXTENSION);
        }
        final PreparedStatement selectStatement = connection.prepareStatement(extendedSelectQuery.toString());
        int paramPlace = 1;
        selectStatement.setString(paramPlace++, searchFilterDTO.getProjectTitle());
        if (!Objects.isNull(searchFilterDTO.getRole())) {
            selectStatement.setInt(paramPlace++, searchFilterDTO.getRole().ordinal());
        }
        if (!Objects.isNull(searchFilterDTO.getEmailPrefix())) {
            selectStatement.setString(paramPlace, searchFilterDTO.getEmailPrefix() + "%");
        }
        return selectStatement;
    }

    @SneakyThrows
    private int fillInPreparedStatement(@NonNull PreparedStatement statement, Employee entity) {
        int paramPlace = 1;
        statement.setString(paramPlace++, entity.getFirstName());
        statement.setString(paramPlace++, entity.getLastName());
        statement.setString(paramPlace++, entity.getPatronymic());
        statement.setObject(paramPlace++, entity.getAccount());
        statement.setString(paramPlace++, entity.getEmail());
        statement.setInt(paramPlace++, entity.getStatus().ordinal());
        return paramPlace;
    }

    @SneakyThrows
    private Employee fillOutEmployeeByRow(@NonNull ResultSet selectedRow) {
        return Employee.builder()
                .id(selectedRow.getLong("id"))
                .firstName(selectedRow.getString("first_name"))
                .lastName(selectedRow.getString("last_name"))
                .patronymic(selectedRow.getString("patronymic"))
                .account((UUID) selectedRow.getObject("account"))
                .email(selectedRow.getString("email"))
                .status(EmployeeStatus.values()[selectedRow.getInt("status")])
                .build();
    }
}
