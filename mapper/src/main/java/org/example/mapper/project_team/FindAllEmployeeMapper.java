package org.example.mapper.project_team;

import lombok.NonNull;
import org.example.dto.project_team.FoundAllEmployeesDTO;
import org.example.mapper.ToResultMapper;
import org.example.store.model.ProjectTeam;
import org.springframework.stereotype.Component;

@Component
public class FindAllEmployeeMapper implements ToResultMapper<ProjectTeam, FoundAllEmployeesDTO.FoundEmployeeDTO> {

    @Override
    public FoundAllEmployeesDTO.FoundEmployeeDTO mapToResult(@NonNull ProjectTeam entity) {
        return new FoundAllEmployeesDTO.FoundEmployeeDTO(entity.getEmployee().getId(), entity.getRole());
    }
}
