package org.example.store.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import org.example.status.ProjectTeamRole;

import java.io.Serial;
import java.io.Serializable;

@Value
@Builder(toBuilder = true)
public class ProjectTeam implements Serializable, Id<ProjectTeam> {

    @Serial
    private static final long serialVersionUID = -5087240905866944607L;

    @With
    Long id;
    @NonNull
    Long projectId;
    @NonNull
    Long employeeId;
    @NonNull
    ProjectTeamRole role;
}
