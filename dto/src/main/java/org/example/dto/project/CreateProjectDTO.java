package org.example.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Schema(description = "Необходимые значения для создания нового проекта")
@Value
@Builder
public class CreateProjectDTO {

    @Schema(description = "Уникальный код")
    @NonNull
    String code;
    @Schema(description = "Название")
    @NonNull
    String title;
    @Schema(description = "Описание", nullable = true)
    String description;
}
