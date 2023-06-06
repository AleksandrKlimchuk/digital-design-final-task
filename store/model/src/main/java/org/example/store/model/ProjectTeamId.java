package org.example.store.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class ProjectTeamId implements Serializable {

    @Serial
    private static final long serialVersionUID = -8934688508167961127L;

    Project project;
    Employee employee;
}
