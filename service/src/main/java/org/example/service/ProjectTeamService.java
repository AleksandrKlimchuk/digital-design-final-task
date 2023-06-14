package org.example.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class ProjectTeamService {

    ProjectTeamRepository repository;

    ProjectService projectService;
    EmployeeService employeeService;

    FindAllEmployeeMapper findAllEmployeeMapper;

    public void addEmployeeToProject(@NonNull AddEmployeeDTO employeeAndProjectData) {
        log.info(
                "Add employee with id {} into project with id {}",
                employeeAndProjectData.getEmployeeId(), employeeAndProjectData.getProjectId()
        );
        final Long employeeId = employeeAndProjectData.getEmployeeId();
        final Long projectId = employeeAndProjectData.getProjectId();
        repository.findByEmployee_IdAndProject_Id(employeeId, projectId).ifPresent(
                foundEmployeeInProject -> {
                    log.error("Try add ProjectTeam entity, which already exists: {}", foundEmployeeInProject);
                    throw new EntityAlreadyExistsException(
                            "Employee with id %d already in team of project %d with role %s"
                                    .formatted(employeeId, projectId, foundEmployeeInProject.getRole())
                    );
                }
        );
        final Employee employee = employeeService.getEmployeeEntityById(employeeId);
        log.info("Fetched Employee entity: {}", employee);
        final Project project = projectService.getProjectEntityById(projectId);
        log.info("Fetched Project entity: {}", project);
        final ProjectTeam savedProject = ProjectTeam.builder()
                .project(project)
                .employee(employee)
                .role(employeeAndProjectData.getRole())
                .build();
        repository.save(savedProject);
        log.info("Project team entity saved in database: {}", savedProject);
    }

    public void excludeEmployeeFromProject(@NonNull ExcludeEmployeeDTO employeeAndProjectData) {
        log.info(
                "Exclude employee with id {} into project with id {}",
                employeeAndProjectData.getEmployeeId(), employeeAndProjectData.getProjectId()
        );
        final Long employeeId = employeeAndProjectData.getEmployeeId();
        final Long projectId = employeeAndProjectData.getProjectId();
        final ProjectTeam storedEmployeeInProjectTeam = getEmployeeInProject(employeeId, projectId)
                .orElseThrow(() -> {
                    log.error(
                            "ProjectTeam entity with employeeId {} and projectId {} doesn't exists",
                            employeeId, projectId
                    );
                    return new EntityNotExistsException(
                            "Employee with id %d isn't member of project with id %s team and so can't be excluded"
                                    .formatted(employeeId, projectId)
                    );
                });
        repository.delete(storedEmployeeInProjectTeam);
        log.info("ProjectTeam entity removed from database: {}", storedEmployeeInProjectTeam);
    }

    public FoundAllEmployeesDTO getAllEmployeesOfProject(@NonNull FindAllEmployeesDTO projectData) {
        log.info("Get employees from project with id {}", projectData.getProjectId());
        final Long projectId = projectData.getProjectId();
        Project project = projectService.getProjectEntityById(projectId);
        log.info("Fetched Project entity: {}", project);
        final List<FoundAllEmployeesDTO.FoundProjectTeamEmployee> allEmployeesOfProject = repository
                .findAllByProject_Id(projectId)
                .stream()
                .map(findAllEmployeeMapper::mapToResult)
                .toList();
        log.info("Fetched employees: {}", allEmployeesOfProject);
        return new FoundAllEmployeesDTO(allEmployeesOfProject);
    }

    Optional<ProjectTeam> getEmployeeInProject(@NonNull Long employeeId, @NonNull Long projectId) {
        log.info("Fetch ProjectTeam by employeeId {} and projectId {}", employeeId, projectId);
        return repository.findByEmployee_IdAndProject_Id(employeeId, projectId);
    }
}
