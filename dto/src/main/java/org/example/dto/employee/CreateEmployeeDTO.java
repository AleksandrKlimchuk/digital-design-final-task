package org.example.dto.employee;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
