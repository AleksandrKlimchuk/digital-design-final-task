package org.example.dto.task;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class UpdateTaskDTO {

    @NonNull
    Long id;
    @NonNull
    String title;
    String description;
    Long executorId;
    @NonNull
    Long workload;
    @NonNull
    Instant deadline;
    @NonNull
    Long authorId;
}
