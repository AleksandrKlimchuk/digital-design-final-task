package org.example.dto.project;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.example.status.ProjectStatus;

@Value
@Builder
public class FoundProjectDTO {

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
