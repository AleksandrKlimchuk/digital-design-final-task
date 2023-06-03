package org.example.mapper.task;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.task.FoundTasksDTO;
import org.example.mapper.ToResultMapper;
import org.example.mapper.employee.FindEmployeesMapper;
import org.example.mapper.project.FindProjectMapper;
import org.example.store.model.Task;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FindTasksMapper implements ToResultMapper<Task, FoundTasksDTO.FoundTaskDTO> {

    FindEmployeesMapper employeesMapper;
    FindProjectMapper projectMapper;

    @Override
    public FoundTasksDTO.FoundTaskDTO mapToResult(@NonNull Task entity) {
        return FoundTasksDTO.FoundTaskDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .executor(employeesMapper.mapToResult(entity.getExecutor()))
                .project(projectMapper.mapToResult(entity.getProject()))
                .workload(entity.getWorkload())
                .deadline(entity.getDeadline())
                .status(entity.getStatus())
                .author(employeesMapper.mapToResult(entity.getAuthor()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
