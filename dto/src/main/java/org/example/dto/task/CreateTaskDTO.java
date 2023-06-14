package org.example.dto.task;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class CreateTaskDTO {

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
    Long authorId;
}
