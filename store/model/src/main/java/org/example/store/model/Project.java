package org.example.store.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.status.ProjectStatus;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "project")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project implements Serializable {

    @Serial
    private static final long serialVersionUID = 5659540381270109028L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String code;
    @Column(nullable = false)
    String title;
    String description;
    @Column(nullable = false)
    ProjectStatus status;
}
