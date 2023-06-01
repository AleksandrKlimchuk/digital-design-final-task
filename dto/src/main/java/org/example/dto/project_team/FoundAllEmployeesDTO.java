package org.example.dto.project_team;

import lombok.NonNull;
import lombok.Value;
import org.example.status.ProjectTeamRole;

import java.util.List;

@Value
public class FoundAllEmployeesDTO {

    @Value
    public static class FoundEmployeeDTO {
        @NonNull
        Long employeeId;
        @NonNull
        ProjectTeamRole role;
    }

    @NonNull
    List<FoundEmployeeDTO> foundEmployeeIds;
}
