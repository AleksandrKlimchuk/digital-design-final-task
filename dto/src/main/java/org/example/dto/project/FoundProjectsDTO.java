package org.example.dto.project;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.example.status.ProjectStatus;

import java.util.List;

@Value
@Builder
public class FoundProjectsDTO {

    @Value
    @Builder
    public static class FoundProjectDTO {

        @NonNull
        Long id;
        @NonNull
        String code;
        @NonNull
        String title;
        String description;
        @NonNull
        ProjectStatus status;
    }

    @NonNull
    List<FoundProjectsDTO> foundProjects;
}
