package org.example.dto.task;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.example.dto.employee.FoundEmployeeDTO;
import org.example.dto.project.FoundProjectDTO;
import org.example.status.TaskStatus;

import java.time.Instant;
import java.util.List;


@Value
public class FoundTasksDTO {

    @Value
    @Builder
    public static class FoundTaskDTO {
        @NonNull
        Long id;
        @NonNull
        String title;
        String description;
        FoundEmployeeDTO executor;
        @NonNull
        FoundProjectDTO project;
        @NonNull
        Long workload;
        @NonNull
        Instant deadline;
        @NonNull
        TaskStatus status;
        @NonNull
        FoundEmployeeDTO author;
        @NonNull
        Instant createdAt;
        @NonNull
        Instant updatedAt;
    }

    @NonNull
    List<FoundTaskDTO> foundTasks;
}
