package org.example.mapper.project;

import lombok.NonNull;
import org.example.dto.project.CreateProjectDTO;
import org.example.dto.project.CreatedProjectDTO;
import org.example.mapper.Mapper;
import org.example.store.model.Project;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CreateProjectMapper implements Mapper<Project, CreateProjectDTO, CreatedProjectDTO> {

    @Override
    public Project mapToEntity(@NonNull CreateProjectDTO dto) {
        return Project.builder()
                .code(dto.getCode())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    @Override
    public CreatedProjectDTO mapToResult(@NonNull Project entity) {
        Objects.requireNonNull(entity.getId(), "Can not map project to result with unspecified id");
        return new CreatedProjectDTO(entity.getId());
    }
}
