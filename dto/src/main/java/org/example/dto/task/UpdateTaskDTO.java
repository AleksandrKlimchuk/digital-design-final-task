package org.example.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;

@Schema(description = "Необходимые значения для изменения созданной задачи")
@Value
@Builder
public class UpdateTaskDTO {

    @Schema(description = "Уникальный идентификатор задачи")
    @NonNull
    Long id;
    @Schema(description = "Название")
    @NonNull
    String title;
    @Schema(description = "Описание", nullable = true)
    String description;
    @Schema(description = "Уникальный идентификатор исполнителя", nullable = true)
    Long executorId;
    @Schema(description = "Трудозатраты в часах")
    @NonNull
    Long workload;
    @Schema(description = "Крайний срок выполнения")
    @NonNull
    Instant deadline;
}
