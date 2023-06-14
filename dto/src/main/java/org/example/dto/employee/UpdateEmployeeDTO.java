package org.example.dto.employee;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class UpdateEmployeeDTO {

    @NonNull
    Long id;
    @NonNull
    String lastName;
    @NonNull
    String firstName;
    String patronymic;
    String account;
    String email;
}
