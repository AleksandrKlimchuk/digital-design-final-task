package org.example.mapper.project;

import lombok.NonNull;
import org.example.dto.project.FoundProjectsDTO;
import org.example.mapper.ToResultMapper;
import org.example.store.model.Project;
import org.springframework.stereotype.Component;

@Component
public class FindProjectMapper implements ToResultMapper<Project, FoundProjectsDTO.FoundProjectDTO> {

    @Override
    public FoundProjectsDTO.FoundProjectDTO mapToResult(@NonNull Project entity) {
        return FoundProjectsDTO.FoundProjectDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build();
    }
}
