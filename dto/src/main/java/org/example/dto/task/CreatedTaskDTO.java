package org.example.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.Value;

@Schema(description = "Созданная задача")
@Value
public class CreatedTaskDTO {

    @Schema(description = "Уникальный идентификатор задачи")
    @NonNull
    Long id;
}
