package org.example.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.Value;
import org.example.status.TaskStatus;

@Schema(description = "Информация об измененном статусе задачи")
@Value
public class ChangedTaskStatusDTO {

    @Schema(description = "Новый статус задачи")
    @NonNull
    TaskStatus newTaskStatus;
}
