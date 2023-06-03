package org.example.store.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.status.EmployeeStatus;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "employee")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 8928580366687397378L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "last_name", nullable = false)
    String lastName;
    @Column(name = "first_name", nullable = false)
    String firstName;
    String patronymic;
    String account;
    String email;
    @Column(nullable = false)
    EmployeeStatus status;
}
