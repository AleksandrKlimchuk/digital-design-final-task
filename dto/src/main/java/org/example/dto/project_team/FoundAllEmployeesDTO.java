package org.example.dto.project_team;

import lombok.NonNull;
import lombok.Value;
import org.example.dto.employee.FoundEmployeeDTO;
import org.example.status.ProjectTeamRole;

import java.util.List;

@Value
public class FoundAllEmployeesDTO {

    @Value
    public static class FoundProjectTeamEmployee {
        @NonNull
        FoundEmployeeDTO employee;
        @NonNull
        ProjectTeamRole role;
    }

    @NonNull
    List<FoundProjectTeamEmployee> foundEmployeeIds;
}
