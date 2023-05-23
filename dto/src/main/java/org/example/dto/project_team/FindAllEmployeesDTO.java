package org.example.dto.project_team;

import lombok.NonNull;
import lombok.Value;

@Value
public class FindAllEmployeesDTO {

    @NonNull
    Long projectId;
}
