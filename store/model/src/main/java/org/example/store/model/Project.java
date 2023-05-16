package org.example.store.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import org.example.status.ProjectStatus;

import java.io.Serial;
import java.io.Serializable;

@Value
@Builder(toBuilder = true)
public class Project implements Serializable, Id<Project> {

    @Serial
    private static final long serialVersionUID = 5659540381270109028L;

    @With
    Long id;
    @NonNull
    String code;
    @NonNull
    String title;
    String description;
    @NonNull
    ProjectStatus status;
}
