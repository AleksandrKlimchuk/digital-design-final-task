package org.example.store.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import org.example.status.EmployeeStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Employee implements Serializable, Id<Employee> {

    @Serial
    private static final long serialVersionUID = 8928580366687397378L;

    @With
    Long id;
    @NonNull
    String lastName;
    @NonNull
    String firstName;
    String patronymic;
    UUID account;
    String email;
    @NonNull
    EmployeeStatus status;
}
