package org.example.dto.project_team;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class FoundAllEmployeesDTO {

    @NonNull
    List<Long> foundEmployeeIds;
}
