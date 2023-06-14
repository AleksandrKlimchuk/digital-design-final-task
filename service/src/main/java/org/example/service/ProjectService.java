package org.example.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.project.*;
import org.example.exception.EntityAlreadyExistsException;
import org.example.exception.ProjectNotExistsException;
import org.example.mapper.project.CreateProjectMapper;
import org.example.mapper.project.FindProjectMapper;
import org.example.mapper.project.UpdateProjectMapper;
import org.example.service.specification.FindProjectSpecification;
import org.example.service.utils.ServiceUtils;
import org.example.status.ProjectStatus;
import org.example.store.model.Project;
import org.example.store.repository.ProjectRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectService {

    ProjectRepository repository;

    CreateProjectMapper createProjectMapper;
    UpdateProjectMapper updateProjectMapper;
    FindProjectMapper findProjectMapper;

    ObjectProvider<FindProjectSpecification> findProjectSpecificationProvider;

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
                repository::findById, projectData::getId, () -> new ProjectNotExistsException(
                        "Project with id %s doesn't exist and so can't be updated".formatted(projectData.getCode())
                )
        );
        final Project updatedProject = updateProjectMapper.mapToEntity(projectData);
        updatedProject.setStatus(storedProject.getStatus());
        repository.save(updatedProject);
    }

    public FoundProjectsDTO findProjectByFilter(@NonNull FindProjectsDTO filter) {
        final List<FoundProjectDTO> foundProjects = repository
                .findAll(findProjectSpecificationProvider.getObject(filter))
                .stream()
                .map(findProjectMapper::mapToResult)
                .toList();
        return new FoundProjectsDTO(foundProjects);
    }

    public ChangedProjectStatusDTO advanceProject(@NonNull Long projectId) {
        final Project storedProject = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, () -> projectId, () -> new ProjectNotExistsException(
                        "Project with id %s doesn't exist and so can't be advanced".formatted(projectId)
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
                () -> new ProjectNotExistsException(
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
                () -> new EntityAlreadyExistsException("Project with code %s already exists".formatted(projectCode))
        );
    }
}
