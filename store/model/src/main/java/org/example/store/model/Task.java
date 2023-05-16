package org.example.store.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import org.example.status.TaskStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class Task implements Serializable, Id<Task> {

    @Serial
    private static final long serialVersionUID = -7118883711132880105L;

    @With
    Long id;
    @NonNull
    String title;
    String description;
    Long executorId;
    @NonNull
    Long projectId;
    @NonNull
    Long workload;
    @NonNull
    Instant deadline;
    @NonNull
    TaskStatus status;
    @NonNull
    Long authorId;
    @NonNull
    Instant createdAt;
    @NonNull
    Instant updatedAt;
}
