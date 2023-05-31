package org.example.store.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.status.ProjectTeamRole;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "project_team")
@IdClass(ProjectTeamId.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectTeam implements Serializable {

    @Serial
    private static final long serialVersionUID = -5087240905866944607L;

    @Id
    @ManyToOne
    @JoinColumn(name = "project_id")
    Project project;
    @Id
    @ManyToOne
    @JoinColumn(name = "employee_id")
    Employee employee;
    @Column(nullable = false)
    ProjectTeamRole role;
}
