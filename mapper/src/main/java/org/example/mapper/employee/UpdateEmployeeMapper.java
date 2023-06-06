package org.example.mapper.employee;

import lombok.NonNull;
import org.example.dto.employee.UpdateEmployeeDTO;
import org.example.mapper.ToEntityMapper;
import org.example.store.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class UpdateEmployeeMapper implements ToEntityMapper<Employee, UpdateEmployeeDTO> {

    @Override
    public Employee mapToEntity(@NonNull UpdateEmployeeDTO dto) {
        return Employee.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .patronymic(dto.getPatronymic())
                .account(dto.getAccount())
                .email(dto.getEmail())
                .build();
    }
}
