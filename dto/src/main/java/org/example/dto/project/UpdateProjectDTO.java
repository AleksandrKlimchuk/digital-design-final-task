package org.example.dto.project;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class UpdateProjectDTO {

    @NonNull
    Long id;
    @NonNull
    String code;
    @NonNull
    String title;
    String description;
}
