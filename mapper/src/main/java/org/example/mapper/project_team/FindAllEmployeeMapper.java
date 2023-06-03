package org.example.mapper.project_team;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.project_team.FoundAllEmployeesDTO;
import org.example.mapper.ToResultMapper;
import org.example.mapper.employee.FindEmployeesMapper;
import org.example.store.model.ProjectTeam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FindAllEmployeeMapper implements ToResultMapper<ProjectTeam, FoundAllEmployeesDTO.FoundProjectTeamEmployee> {

    FindEmployeesMapper employeesMapper;

    @Override
    public FoundAllEmployeesDTO.FoundProjectTeamEmployee mapToResult(@NonNull ProjectTeam entity) {
        return new FoundAllEmployeesDTO.FoundProjectTeamEmployee(
                employeesMapper.mapToResult(entity.getEmployee()), entity.getRole()
        );
    }
}
