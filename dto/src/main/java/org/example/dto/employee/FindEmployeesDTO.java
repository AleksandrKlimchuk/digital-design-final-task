package org.example.dto.employee;

import lombok.NonNull;
import lombok.Value;

@Value
public class FindEmployeesDTO {

    @NonNull
    String filter;
}
