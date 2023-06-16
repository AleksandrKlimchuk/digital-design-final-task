package org.example.dto.project_team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.Value;
import org.example.dto.employee.FoundEmployeeDTO;
import org.example.status.ProjectTeamRole;

import java.util.List;

@Schema(description = "Все сотрудники проекта")
@Value
public class FoundAllEmployeesDTO {

    @Schema(description = "Информация о сотруднике в проекте")
    @Value
    public static class FoundProjectTeamEmployee {
        @Schema(description = "Сотрудник в проекте")
        @NonNull
        FoundEmployeeDTO employee;
        @Schema(description = "Роль сотрудника в проекте")
        @NonNull
        ProjectTeamRole role;
    }

    @Schema(description = "Список сотрудников проекта")
    @NonNull
    List<FoundProjectTeamEmployee> foundEmployees;
}
