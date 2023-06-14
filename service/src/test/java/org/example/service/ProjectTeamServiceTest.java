package org.example.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.example.dto.project_team.AddEmployeeDTO;
import org.example.dto.project_team.ExcludeEmployeeDTO;
import org.example.dto.project_team.FindAllEmployeesDTO;
import org.example.dto.project_team.FoundAllEmployeesDTO;
import org.example.exception.EntityAlreadyExistsException;
import org.example.exception.EntityNotExistsException;
import org.example.mapper.employee.FindEmployeesMapper;
import org.example.mapper.project_team.FindAllEmployeeMapper;
import org.example.status.EmployeeStatus;
import org.example.status.ProjectTeamRole;
import org.example.store.model.Employee;
import org.example.store.model.Project;
import org.example.store.model.ProjectTeam;
import org.example.store.repository.ProjectTeamRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectTeamServiceTest {

    @Mock
    ProjectTeamRepository repository;
    @Mock
    ProjectService projectService;
    @Mock
    EmployeeService employeeService;

    @Spy
    FindEmployeesMapper findEmployeesMapper = new FindEmployeesMapper();
    @Spy
    FindAllEmployeeMapper findAllEmployeeMapper = new FindAllEmployeeMapper(findEmployeesMapper);

    @InjectMocks
    ProjectTeamService service;

    @SuppressWarnings("DataFlowIssue")
    @Test
    @DisplayName("Добавить null")
    @Tag("ProjectTeamService.addEmployeeToProject_AddEmployeeDTO")
    @Tag("negative")
    public void addNullEmployeeToProject() {
        assertThrows(NullPointerException.class, () -> service.addEmployeeToProject(null), "Ожидался null");
    }

    @Test
    @DisplayName("Добавить в проект сотрудника, который уже является участником")
    @Tag("ProjectTeamService.addEmployeeToProject_AddEmployeeDTO")
    @Tag("negative")
    public void addAlreadyExistsEmployeeToProjectTest() {
        final ProjectTeam alreadyExistsEmployee = generateProjectTeam();
        final AddEmployeeDTO employeeToAdd = new AddEmployeeDTO(
                alreadyExistsEmployee.getProject().getId(),
                alreadyExistsEmployee.getEmployee().getId(),
                alreadyExistsEmployee.getRole()
        );
        when(repository.findByEmployee_IdAndProject_Id(any(), any())).thenReturn(Optional.of(alreadyExistsEmployee));
        final String exceptionMessage = assertThrows(
                EntityAlreadyExistsException.class, () -> service.addEmployeeToProject(employeeToAdd)
        ).getMessage();
        assertTrue(
                exceptionMessage.contains("id " + alreadyExistsEmployee.getEmployee().getId()),
                "id сотрудника не совпадает"
        );
        assertTrue(
                exceptionMessage.contains("project " + alreadyExistsEmployee.getProject().getId()),
                "id проекта не совпадает"
        );
        assertTrue(
                exceptionMessage.contains("role " + alreadyExistsEmployee.getRole()),
                "Роль сотрудника не совпадает"
        );
    }

    @Test
    @DisplayName("Добавить в проект сотрудника")
    @Tag("ProjectTeamService.addEmployeeToProject_AddEmployeeDTO")
    @Tag("positive")
    public void addNotExistsEmployeeToProjectTest() {
        final Project project = generateProject();
        final Employee employee = generateEmployee();
        final AddEmployeeDTO employeeToAdd = new AddEmployeeDTO(
                project.getId(), employee.getId(), ProjectTeamRole.ANALYST
        );
        when(repository.findByEmployee_IdAndProject_Id(any(), any())).thenReturn(Optional.empty());
        when(employeeService.getEmployeeEntityById(any())).thenReturn(employee);
        when(projectService.getProjectEntityById(any())).thenReturn(project);
        when(repository.save(any())).thenReturn(null);
        assertDoesNotThrow(() -> service.addEmployeeToProject(employeeToAdd), "Метод выбросил исключение");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    @DisplayName("Добавить в проект сотрудника null")
    @Tag("ProjectTeamService.excludeEmployeeFromProject_ExcludeEmployeeDTO")
    @Tag("negative")
    public void excludeNullEmployeeFromProject() {
        assertThrows(NullPointerException.class, () -> service.excludeEmployeeFromProject(null), "Ожидался null");
    }

    @Test
    @DisplayName("Исключить из проекта сотрудника, который не является участником")
    @Tag("ProjectTeamService.excludeEmployeeFromProject_ExcludeEmployeeDTO")
    @Tag("negative")
    public void excludeNotExistsEmployeeFromProjectTest() {
        final Project project = generateProject();
        final Employee employee = generateEmployee();
        final ExcludeEmployeeDTO employeeToExclude = new ExcludeEmployeeDTO(project.getId(), employee.getId());
        when(repository.findByEmployee_IdAndProject_Id(any(), any())).thenReturn(Optional.empty());
        final String exceptionMessage = assertThrows(
                EntityNotExistsException.class, () -> service.excludeEmployeeFromProject(employeeToExclude)
        ).getMessage();
        assertTrue(
                exceptionMessage.contains("Employee with id " + employeeToExclude.getEmployeeId()),
                "id сотрудника не совпадает"
        );
        assertTrue(
                exceptionMessage.contains("project with id " + employeeToExclude.getProjectId()),
                "id проекта не совпадает"
        );
    }

    @Test
    @DisplayName("Исключить из проекта сотрудника")
    @Tag("ProjectTeamService.excludeEmployeeFromProject_ExcludeEmployeeDTO")
    @Tag("positive")
    public void excludeEmployeeFromProjectTest() {
        final ProjectTeam foundEmployeeInProject = generateProjectTeam();
        final ExcludeEmployeeDTO employeeToExclude = new ExcludeEmployeeDTO(
                foundEmployeeInProject.getProject().getId(),
                foundEmployeeInProject.getEmployee().getId()
        );
        when(repository.findByEmployee_IdAndProject_Id(any(), any())).thenReturn(Optional.of(foundEmployeeInProject));
        doNothing().when(repository).delete(any());
        assertDoesNotThrow(() -> service.excludeEmployeeFromProject(employeeToExclude), "Метод выбросил исключение");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    @DisplayName("Получить всех сотрудников проекта null")
    @Tag("ProjectTeamService.getAllEmployeesOfProject_FindAllEmployeesDTO")
    @Tag("negative")
    public void getAllEmployeesOfProjectNullTest() {
        Assertions.assertThrows(NullPointerException.class, () -> service.getAllEmployeesOfProject(null));
    }

    @RepeatedTest(10)
    @DisplayName("Получить всех сотрудников проекта")
    @Tag("ProjectTeamService.getAllEmployeesOfProject_FindAllEmployeesDTO")
    @Tag("positive")
    public void getAllEmployeesOfProjectTest() {
        final FindAllEmployeesDTO projectData = new FindAllEmployeesDTO(nextLong());
        final List<ProjectTeam> employeesFromProject = IntStream.range(0, RandomUtils.nextInt(0, 5))
                .mapToObj(i -> generateProjectTeam())
                .toList();
        FoundAllEmployeesDTO foundEmployeesData = new FoundAllEmployeesDTO(
                employeesFromProject.stream().map(findAllEmployeeMapper::mapToResult).toList()
        );
        when(projectService.getProjectEntityById(any())).thenReturn(null);
        when(repository.findAllByProject_Id(any())).thenReturn(employeesFromProject);
        FoundAllEmployeesDTO foundEmployeesFromService = assertDoesNotThrow(
                () -> service.getAllEmployeesOfProject(projectData)
        );
        Assertions.assertTrue(CollectionUtils.isEqualCollection(
                foundEmployeesData.getFoundEmployees(), foundEmployeesFromService.getFoundEmployees()
        ));
    }

    private static ProjectTeam generateProjectTeam() {
        final Project project = generateProject();
        final Employee employee = generateEmployee();
        final ProjectTeamRole role = ProjectTeamRole.values()[RandomUtils.nextInt(0, ProjectTeamRole.values().length)];
        return new ProjectTeam(project, employee, role);
    }

    private static Employee generateEmployee() {
        return Employee.builder()
                .id(nextLong())
                .firstName(randomString())
                .lastName(randomString())
                .status(EmployeeStatus.ACTIVE)
                .build();
    }

    private static Project generateProject() {
        return Project.builder().id(nextLong()).code(randomString()).build();
    }

    private static Long nextLong() {
        return RandomUtils.nextLong(1, Long.MAX_VALUE);
    }

    private static String randomString() {
        return RandomStringUtils.random(3, true, false);
    }
}
