package org.example.mapper.project;

import lombok.NonNull;
import org.example.dto.project.FoundProjectDTO;
import org.example.mapper.ToResultMapper;
import org.example.store.model.Project;
import org.springframework.stereotype.Component;

@Component
public class FindProjectMapper implements ToResultMapper<Project, FoundProjectDTO> {

    @Override
    public FoundProjectDTO mapToResult(@NonNull Project entity) {
        return FoundProjectDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build();
    }
}
