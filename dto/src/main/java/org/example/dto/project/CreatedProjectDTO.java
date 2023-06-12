package org.example.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.Value;

@Schema(description = "Созданный проект")
@Value
public class CreatedProjectDTO {

    @Schema(description = "Уникальный идентификатор созданного проекта")
    @NonNull
    Long id;
}
