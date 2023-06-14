package org.example.dto.employee;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class FoundEmployeesDTO {

    @NonNull
    List<FoundEmployeeDTO> foundEmployees;
}
