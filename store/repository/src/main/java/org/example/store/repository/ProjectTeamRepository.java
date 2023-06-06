package org.example.store.repository;

import lombok.NonNull;
import org.example.store.model.ProjectTeam;
import org.example.store.model.ProjectTeamId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectTeamRepository extends JpaRepository<ProjectTeam, ProjectTeamId> {

    Optional<ProjectTeam> findByEmployee_IdAndProject_Id(@NonNull Long employeeId, @NonNull Long projectId);

    List<ProjectTeam> findAllByProject_Id(@NonNull Long projectId);
}
