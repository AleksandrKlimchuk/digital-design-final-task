package org.example.dto.employee;

import lombok.NonNull;
import lombok.Value;

@Value
public class DeleteEmployeeDTO {

    @NonNull
    Long id;
}
