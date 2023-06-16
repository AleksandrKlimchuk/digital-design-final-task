package org.example.service.integrated;

import org.apache.commons.collections4.CollectionUtils;
import org.example.dto.project_team.AddEmployeeDTO;
import org.example.dto.project_team.ExcludeEmployeeDTO;
import org.example.dto.project_team.FindAllEmployeesDTO;
import org.example.dto.project_team.FoundAllEmployeesDTO;
import org.example.mapper.project_team.FindAllEmployeeMapper;
import org.example.service.ProjectTeamService;
import org.example.service.integrated.utils.ProjectTeamTestEntitiesFactory;
import org.example.status.ProjectTeamRole;
import org.example.store.model.Employee;
import org.example.store.model.Project;
import org.example.store.model.ProjectTeam;
import org.example.store.repository.EmployeeRepository;
import org.example.store.repository.ProjectRepository;
import org.example.store.repository.ProjectTeamRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(classes = Main.class)
public class ChangeRoleProjectTeamTest extends PostgresTestBase {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectTeamRepository projectTeamRepository;
    @Autowired
    ProjectTeamService projectTeamService;
    @Autowired
    FindAllEmployeeMapper findAllEmployeeMapper;

    Employee employee1 = ProjectTeamTestEntitiesFactory.createEmployee1();

    Project projectA = ProjectTeamTestEntitiesFactory.createProjectA();

    ProjectTeam employee1InProjectA;

    @BeforeEach
    @Transactional
    public void init() {
        employee1 = employeeRepository.save(employee1);

        projectA = projectRepository.save(projectA);

        employee1InProjectA = projectTeamRepository.save(new ProjectTeam(projectA, employee1, ProjectTeamRole.ANALYST));
    }

    @Test
    @DisplayName("Изменить роль сотрудника в команде проекта")
    @Tag("integrated")
    public void test() {
        Assertions.assertDoesNotThrow(
                () -> projectTeamService.excludeEmployeeFromProject(
                        new ExcludeEmployeeDTO(projectA.getId(), employee1.getId())
                ),
                "Сотрудник 1 не был удален из группы А"
        );
        employee1InProjectA.setRole(ProjectTeamRole.MANAGER);
        Assertions.assertDoesNotThrow(
                () -> projectTeamService.addEmployeeToProject(new AddEmployeeDTO(
                        employee1InProjectA.getProject().getId(),
                        employee1InProjectA.getEmployee().getId(),
                        employee1InProjectA.getRole()
                ))
        );
        final FoundAllEmployeesDTO foundAllEmployeesOfProjectA = Assertions.assertDoesNotThrow(
                () -> projectTeamService.getAllEmployeesOfProject(new FindAllEmployeesDTO(projectA.getId())),
                "Не получилось получить сотрудников проекта A"
        );
        Assertions.assertEquals(
                1, foundAllEmployeesOfProjectA.getFoundEmployees().size(), "Неожиданный размер команды проекта A"
        );
        final List<FoundAllEmployeesDTO.FoundProjectTeamEmployee> expectedEmployeesOfGroupA = List.of(
                findAllEmployeeMapper.mapToResult(employee1InProjectA)
        );
        Assertions.assertTrue(
                CollectionUtils.isEqualCollection(
                        expectedEmployeesOfGroupA, foundAllEmployeesOfProjectA.getFoundEmployees()
                ), "Неожиданный состав команды проекта А"
        );
    }

    @AfterEach
    @Transactional
    public void destroy() {
        projectTeamRepository.delete(employee1InProjectA);

        employeeRepository.delete(employee1);

        projectRepository.delete(projectA);
    }


}
