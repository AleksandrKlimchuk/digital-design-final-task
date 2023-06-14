package org.example.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Schema(description = "Необходимые значения для изменения проекта")
@Value
@Builder
public class UpdateProjectDTO {

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
}
