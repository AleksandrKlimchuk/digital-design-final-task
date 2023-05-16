package org.example.dto.project_team;

import lombok.NonNull;
import lombok.Value;
import org.example.status.ProjectTeamRole;

@Value
public class AddEmployeeDTO {

    @NonNull
    Long projectId;
    @NonNull
    Long employeeId;
    @NonNull
    ProjectTeamRole role;
}
