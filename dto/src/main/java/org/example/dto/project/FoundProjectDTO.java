package org.example.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.example.status.ProjectStatus;

@Schema(description = "Информация о проекте")
@Value
@Builder
public class FoundProjectDTO {

    @Schema(description = "Уникальный идентификатор проекта")
    @NonNull
    Long id;
    @Schema(description = "Уникальный код")
    @NonNull
    String code;
    @Schema(description = "Название")
    @NonNull
    String title;
    @Schema(description = "Описание")
    String description;
    @Schema(description = "Статус")
    @NonNull
    ProjectStatus status;
}
