package org.example.store.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.status.TaskStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "task")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task implements Serializable {

    @Serial
    private static final long serialVersionUID = -7118883711132880105L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String title;
    String description;
    @ManyToOne
    @JoinColumn(name = "executor_id")
    Employee executor;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    Project project;
    @Column(nullable = false)
    Long workload;
    @Column(nullable = false)
    Instant deadline;
    @Column(nullable = false)
    TaskStatus status;
    @ManyToOne
    @JoinColumn(name = "author_id")
    Employee author;
    @Column(nullable = false)
    Instant createdAt;
    @Column(nullable = false)
    Instant updatedAt;
}
