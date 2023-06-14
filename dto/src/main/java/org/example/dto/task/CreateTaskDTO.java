package org.example.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;

@Schema(description = "Необходимые значения для создания задачи")
@Value
@Builder
public class CreateTaskDTO {

    @Schema(description = "Название")
    @NonNull
    String title;
    @Schema(description = "Описание", nullable = true)
    String description;
    @Schema(description = "Уникальный идентификатор исполнителя", nullable = true)
    Long executorId;
    @Schema(description = "Уникальный идентификатор проекта")
    @NonNull
    Long projectId;
    @Schema(description = "Трудозатраты в часах")
    @NonNull
    Long workload;
    @Schema(description = "Крайний срок выполнения")
    @NonNull
    Instant deadline;
}
