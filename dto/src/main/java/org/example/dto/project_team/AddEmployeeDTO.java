package org.example.dto.project_team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.Value;
import org.example.status.ProjectTeamRole;

@Schema(description = "Необходимая информация для добавления сотрудника в команду проекта")
@Value
public class AddEmployeeDTO {

    @Schema(description = "Уникальный идентификтор проекта")
    @NonNull
    Long projectId;
    @Schema(description = "Уникальный идентификатор сотрудника")
    @NonNull
    Long employeeId;
    @Schema(description = "Роль сотрудника в проекте")
    @NonNull
    ProjectTeamRole role;
}
