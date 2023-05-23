package org.example.dto.employee;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CreateEmployeeDTO {

    @NonNull
    String lastName;
    @NonNull
    String firstName;
    String patronymic;
    UUID account;
    String email;
}
