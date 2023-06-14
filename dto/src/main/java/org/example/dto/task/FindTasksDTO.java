package org.example.dto.task;

import lombok.Builder;
import lombok.Value;
import org.example.status.TaskStatus;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class FindTasksDTO {

    String title;
    List<TaskStatus> statuses;
    Long executorId;
    Long authorId;
    Instant deadline;
    Instant createdAt;
}
