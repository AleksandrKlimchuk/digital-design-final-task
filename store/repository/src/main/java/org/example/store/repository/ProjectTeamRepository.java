package org.example.store.repository;

import org.example.store.model.ProjectTeam;
import org.example.store.model.ProjectTeamId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTeamRepository extends JpaRepository<ProjectTeam, ProjectTeamId> {
}
