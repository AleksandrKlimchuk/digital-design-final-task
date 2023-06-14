package org.example.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.example.dto.employee.FoundEmployeeDTO;
import org.example.dto.project.FoundProjectDTO;
import org.example.status.TaskStatus;

import java.time.Instant;
import java.util.List;

@Schema(description = "Задачи, удовлетворяющих фильтру")
@Value
public class FoundTasksDTO {

    @Schema(description = "Информация о задачи")
    @Value
    @Builder
    public static class FoundTaskDTO {
        @Schema(description = "Уникальный идентификатор задачи")
        @NonNull
        Long id;
        @Schema(description = "Название")
        @NonNull
        String title;
        @Schema(description = "Описание", nullable = true)
        String description;
        @Schema(description = "Исполнитель", nullable = true)
        FoundEmployeeDTO executor;
        @Schema(description = "Проект")
        @NonNull
        FoundProjectDTO project;
        @Schema(description = "Трудозатраты в часах")
        @NonNull
        Long workload;
        @Schema(description = "Крайний срок выполнения")
        @NonNull
        Instant deadline;
        @Schema(description = "Статус задачи")
        @NonNull
        TaskStatus status;
        @Schema(description = "Автор задачи")
        @NonNull
        FoundEmployeeDTO author;
        @Schema(description = "Задача создана в период", type = "string", format = "date-time")
        @NonNull
        Instant createdAt;
        @Schema(description = "Задача последний раз изменена в период")
        @NonNull
        Instant updatedAt;
    }

    @Schema(description = "Список задач, удовлетворяющих фильтру")
    @NonNull
    List<FoundTaskDTO> foundTasks;
}
