package org.example.service;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.project.*;
import org.example.mapper.project.CreateProjectMapper;
import org.example.mapper.project.FindProjectMapper;
import org.example.mapper.project.UpdateProjectMapper;
import org.example.service.utils.ServiceUtils;
import org.example.status.ProjectStatus;
import org.example.store.model.Project;
import org.example.store.repository.ProjectRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectService {

    ProjectRepository repository;

    CreateProjectMapper createProjectMapper;
    UpdateProjectMapper updateProjectMapper;
    FindProjectMapper findProjectMapper;

    public CreatedProjectDTO createProject(@NonNull CreateProjectDTO projectData) {
        checkProjectCodeIsUnique(projectData.getCode());
        final Project project = createProjectMapper.mapToEntity(projectData);
        project.setStatus(ProjectStatus.DRAFT);
        final Project savedProject = repository.save(project);
        return createProjectMapper.mapToResult(savedProject);
    }

    public void updateProject(@NonNull UpdateProjectDTO projectData) {
        checkProjectCodeIsUnique(projectData.getCode(), projectData.getId());
        final Project storedProject = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, projectData::getId, () -> new IllegalArgumentException(
                        "Project with id %s doesn't exist and so can't be updated".formatted(projectData.getCode())
                )
        );
        final Project updatedProject = updateProjectMapper.mapToEntity(projectData);
        updatedProject.setStatus(storedProject.getStatus());
        repository.save(updatedProject);
    }

    public FoundProjectsDTO findProjectByFilter(@NonNull FindProjectsDTO filter) {
        final List<FoundProjectsDTO.FoundProjectDTO> foundProjects = repository
                .findAll(getFilterSpecification(filter))
                .stream()
                .map(findProjectMapper::mapToResult)
                .toList();
        return new FoundProjectsDTO(foundProjects);
    }

    public ChangedProjectStatusDTO advanceProject(@NonNull ChangeProjectStatusDTO projectData) {
        final Project storedProject = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, projectData::getId, () -> new IllegalArgumentException(
                        "Project with id %s doesn't exist and so can't be advanced".formatted(projectData.getId())
                )
        );
        final ProjectStatus advancedStatus = ProjectStatus.nextProjectStatus(storedProject.getStatus());
        storedProject.setStatus(advancedStatus);
        repository.save(storedProject);
        return new ChangedProjectStatusDTO(advancedStatus);
    }

    Project findProjectEntityById(@NonNull Long id) {
        return ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, () -> id,
                () -> new IllegalArgumentException(
                        "Project with id %d doesn't exists".formatted(id)
                )
        );
    }

    private void checkProjectCodeIsUnique(String projectCode) {
        checkProjectCodeIsUnique(projectCode, null);
    }

    private void checkProjectCodeIsUnique(String projectCode, Long currentProjectId) {
        ServiceUtils.checkValueIsUniqueWithPredicateOrThrow(
                repository::findByCode, () -> projectCode,
                foundProject -> !foundProject.getId().equals(currentProjectId),
                () -> new IllegalArgumentException("Project with code %s already exists".formatted(projectCode))
        );
    }

    private Specification<Project> getFilterSpecification(FindProjectsDTO filter) {
        return (root, query, criteriaBuilder) -> {
            final String textFilter = "%" + filter.getText() + "%";
            final List<Predicate> textPredicates = List.of(
                    criteriaBuilder.like(root.get("code"), textFilter),
                    criteriaBuilder.like(root.get("title"), textFilter)
            );
            Predicate finalPredicate = criteriaBuilder.or(textPredicates.toArray(Predicate[]::new));
            if (!Objects.isNull(filter.getStatuses()) && !filter.getStatuses().isEmpty()) {
                final Expression<ProjectStatus> statusExpression = root.get("status");
                final Predicate status = statusExpression.in(filter.getStatuses());
                finalPredicate = criteriaBuilder.and(finalPredicate, status);
            }
            return query
                    .where(finalPredicate)
                    .getRestriction();
        };
    }
}
