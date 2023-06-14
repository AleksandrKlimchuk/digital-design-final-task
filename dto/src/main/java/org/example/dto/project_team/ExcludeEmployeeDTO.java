package org.example.dto.project_team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.Value;

@Schema(description = "Необходимая информация для удаления проекта")
@Value
public class ExcludeEmployeeDTO {

    @Schema(description = "Уникальный идентификтор проекта")
    @NonNull
    Long projectId;
    @Schema(description = "Уникальный идентификатор сотрудника")
    @NonNull
    Long employeeId;
}
