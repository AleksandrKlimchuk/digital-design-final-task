package org.example.dto.employee;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.example.status.EmployeeStatus;

@Value
@Builder
public class FoundEmployeeDTO {

    @NonNull
    Long id;
    @NonNull
    String lastName;
    @NonNull
    String firstName;
    String patronymic;
    String account;
    String email;
    EmployeeStatus status;
}
