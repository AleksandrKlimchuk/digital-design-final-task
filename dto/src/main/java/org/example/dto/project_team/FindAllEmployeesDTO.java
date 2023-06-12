package org.example.dto.project_team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.Value;

@Schema(description = "Информация о проекте, из которого должны быть получены сотрудники")
@Value
public class FindAllEmployeesDTO {

    @Schema(description = "Уникальный идентификтор проекта")
    @NonNull
    Long projectId;
}
