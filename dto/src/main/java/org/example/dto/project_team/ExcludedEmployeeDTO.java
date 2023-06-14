package org.example.dto.project_team;

import lombok.NonNull;
import lombok.Value;
import org.example.status.ProjectTeamRole;

@Value
public class ExcludedEmployeeDTO {

    @NonNull
    Long employeeId;
    @NonNull
    ProjectTeamRole role;
}
