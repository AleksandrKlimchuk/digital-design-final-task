package org.example.mapper.employee;

import lombok.NonNull;
import org.example.dto.employee.FoundEmployeeDTO;
import org.example.mapper.ToResultMapper;
import org.example.store.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class FindEmployeesMapper implements ToResultMapper<Employee, FoundEmployeeDTO> {

    public FoundEmployeeDTO mapToResult(@NonNull Employee entity) {
        return FoundEmployeeDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .patronymic(entity.getPatronymic())
                .account(entity.getAccount())
                .email(entity.getEmail())
                .status(entity.getStatus())
                .build();
    }
}
