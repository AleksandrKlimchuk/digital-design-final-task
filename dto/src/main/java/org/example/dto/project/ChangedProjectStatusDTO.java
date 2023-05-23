package org.example.dto.project;

import lombok.NonNull;
import lombok.Value;
import org.example.status.ProjectStatus;

@Value
public class ChangedProjectStatusDTO {

    @NonNull
    ProjectStatus newStatus;
}
