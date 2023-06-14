package org.example.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import org.example.status.TaskStatus;

import java.time.Instant;
import java.util.List;

@Schema(description = "Фильтр для поиска задач")
@Value
@Builder
public class FindTasksDTO {

    @Schema(description = "Фильтр по названию задач", nullable = true)
    String title;
    List<TaskStatus> statuses;
    @Schema(description = "Фильтр по исполнителю задач (уникальный идентификатор)", nullable = true)
    Long executorId;
    @Schema(description = "Фильтр по автору задач (уникальный идентификатор", nullable = true)
    Long authorId;
    @Schema(description = "Фильтр по сроку выполнения задачи", nullable = true)
    Instant deadline;
    @Schema(description = "Фильтр по сроку создания задачи", nullable = true)
    Instant createdAt;
}
