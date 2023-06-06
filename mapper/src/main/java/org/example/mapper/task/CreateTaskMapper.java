package org.example.mapper.task;

import lombok.NonNull;
import org.example.dto.task.CreateTaskDTO;
import org.example.dto.task.CreatedTaskDTO;
import org.example.mapper.Mapper;
import org.example.store.model.Task;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CreateTaskMapper implements Mapper<Task, CreateTaskDTO, CreatedTaskDTO> {

    @Override
    public Task mapToEntity(@NonNull CreateTaskDTO dto) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .workload(dto.getWorkload())
                .deadline(dto.getDeadline())
                .build();
    }

    @Override
    public CreatedTaskDTO mapToResult(@NonNull Task entity) {
        Objects.requireNonNull(entity.getId(), "Can not map task to result with unspecified id");
        return new CreatedTaskDTO(entity.getId());
    }
}
