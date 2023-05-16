package org.example.dto.employee;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Value
public class CreatedEmployeeDTO {

    @NonNull
    Long id;
}
