package org.example.dto.project;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class CreateProjectDTO {

    @NonNull
    String code;
    @NonNull
    String title;
    String description;
}
