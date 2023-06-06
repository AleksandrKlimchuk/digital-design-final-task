package org.example.mapper.employee;

import lombok.NonNull;
import org.example.dto.employee.CreateEmployeeDTO;
import org.example.dto.employee.CreatedEmployeeDTO;
import org.example.mapper.Mapper;
import org.example.store.model.Employee;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CreateEmployeeMapper implements Mapper<Employee, CreateEmployeeDTO, CreatedEmployeeDTO> {

    public Employee mapToEntity(@NonNull CreateEmployeeDTO dto) {
        return Employee.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .patronymic(dto.getPatronymic())
                .account(dto.getAccount())
                .email(dto.getEmail())
                .build();
    }

    @Override
    public CreatedEmployeeDTO mapToResult(@NonNull Employee entity) {
        Objects.requireNonNull(entity.getId(), "Can not map employee to result with unspecified id");
        return new CreatedEmployeeDTO(entity.getId());
    }
}
