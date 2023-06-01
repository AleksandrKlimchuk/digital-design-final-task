package org.example.mapper.project;

import lombok.NonNull;
import org.example.dto.project.UpdateProjectDTO;
import org.example.mapper.ToEntityMapper;
import org.example.store.model.Project;
import org.springframework.stereotype.Component;

@Component
public class UpdateProjectMapper implements ToEntityMapper<Project, UpdateProjectDTO> {

    @Override
    public Project mapToEntity(@NonNull UpdateProjectDTO dto) {
        return Project.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }
}
