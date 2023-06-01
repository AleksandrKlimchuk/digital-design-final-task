package org.example.mapper.task;

import lombok.NonNull;
import org.example.dto.task.FoundTasksDTO;
import org.example.mapper.ToResultMapper;
import org.example.store.model.Task;
import org.springframework.stereotype.Component;

@Component
public class FindTasksMapper implements ToResultMapper<Task, FoundTasksDTO.FoundTaskDTO> {

    @Override
    public FoundTasksDTO.FoundTaskDTO mapToResult(@NonNull Task entity) {
        return FoundTasksDTO.FoundTaskDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .executorId(entity.getExecutor().getId())
                .projectId(entity.getProject().getId())
                .workload(entity.getWorkload())
                .deadline(entity.getDeadline())
                .status(entity.getStatus())
                .authorId(entity.getAuthor().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
