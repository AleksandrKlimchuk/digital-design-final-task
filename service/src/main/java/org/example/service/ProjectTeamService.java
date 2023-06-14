package org.example.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.project_team.AddEmployeeDTO;
import org.example.dto.project_team.ExcludeEmployeeDTO;
import org.example.dto.project_team.FindAllEmployeesDTO;
import org.example.dto.project_team.FoundAllEmployeesDTO;
import org.example.exception.EntityAlreadyExistsException;
import org.example.exception.EntityNotExistsException;
import org.example.mapper.project_team.FindAllEmployeeMapper;
import org.example.store.model.Employee;
import org.example.store.model.Project;
import org.example.store.model.ProjectTeam;
import org.example.store.repository.ProjectTeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectTeamService {

    ProjectTeamRepository repository;

    ProjectService projectService;
    EmployeeService employeeService;

    FindAllEmployeeMapper findAllEmployeeMapper;

    public void addEmployeeToProject(@NonNull AddEmployeeDTO employeeAndProjectData) {
        final Long employeeId = employeeAndProjectData.getEmployeeId();
        final Long projectId = employeeAndProjectData.getProjectId();
        repository.findByEmployee_IdAndProject_Id(employeeId, projectId).ifPresent(
                foundEmployeeInProject -> {
                    throw new EntityAlreadyExistsException(
                            "Employee with id %d already in team of project %d with role %s"
                                    .formatted(employeeId, projectId, foundEmployeeInProject.getRole())
                    );
                }
        );
        final Employee employee = employeeService.getEmployeeEntityById(employeeId);
        final Project project = projectService.findProjectEntityById(projectId);
        final ProjectTeam savedProject = ProjectTeam.builder()
                .project(project)
                .employee(employee)
                .role(employeeAndProjectData.getRole())
                .build();
        repository.save(savedProject);
    }

    public void excludeEmployeeFromProject(@NonNull ExcludeEmployeeDTO employeeAndProjectData) {
        final Long employeeId = employeeAndProjectData.getEmployeeId();
        final Long projectId = employeeAndProjectData.getProjectId();
        final ProjectTeam storedEmployeeInProjectTeam = getEmployeeInProject(employeeId, projectId)
                .orElseThrow(() -> new EntityNotExistsException(
                        "Employee with id %d isn't member of project with id %s team and so can't be excluded"
                                .formatted(employeeId, projectId)
                ));
        repository.delete(storedEmployeeInProjectTeam);
    }

    public FoundAllEmployeesDTO getAllEmployeesOfProject(@NonNull FindAllEmployeesDTO projectData) {
        final List<FoundAllEmployeesDTO.FoundProjectTeamEmployee> allEmployeesOfProject = repository
                .findAllByProject_Id(projectData.getProjectId())
                .stream()
                .map(findAllEmployeeMapper::mapToResult)
                .toList();
        return new FoundAllEmployeesDTO(allEmployeesOfProject);
    }

    Optional<ProjectTeam> getEmployeeInProject(@NonNull Long employeeId, @NonNull Long projectId) {
        return repository
                .findByEmployee_IdAndProject_Id(employeeId, projectId);
    }
}
