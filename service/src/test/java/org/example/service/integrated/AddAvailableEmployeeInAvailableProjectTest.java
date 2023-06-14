package org.example.service.integrated;

import org.apache.commons.collections4.CollectionUtils;
import org.example.dto.project_team.AddEmployeeDTO;
import org.example.dto.project_team.FindAllEmployeesDTO;
import org.example.dto.project_team.FoundAllEmployeesDTO;
import org.example.exception.EntityAlreadyExistsException;
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
import java.util.stream.Stream;

@SpringBootTest(classes = Main.class)
public class AddAvailableEmployeeInAvailableProjectTest extends PostgresTestBase {

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
    Employee employee2 = ProjectTeamTestEntitiesFactory.createEmployee2();
    Employee employee3 = ProjectTeamTestEntitiesFactory.createEmployee3();

    Project projectA = ProjectTeamTestEntitiesFactory.createProjectA();
    Project projectB = ProjectTeamTestEntitiesFactory.createProjectB();

    ProjectTeam employee3InProjectA;
    ProjectTeam employee3InProjectB;
    ProjectTeam employee1InProjectA;
    ProjectTeam employee2InProjectB;

    @BeforeEach
    @Transactional
    public void init() {
        employee1 = employeeRepository.save(employee1);
        employee2 = employeeRepository.save(employee2);
        employee3 = employeeRepository.save(employee3);

        projectA = projectRepository.save(projectA);
        projectB = projectRepository.save(projectB);

        employee1InProjectA = new ProjectTeam(projectA, employee1, ProjectTeamRole.ANALYST);
        employee3InProjectA = new ProjectTeam(projectA, employee3, ProjectTeamRole.ANALYST);
        employee2InProjectB = new ProjectTeam(projectB, employee2, ProjectTeamRole.MANAGER);
        employee3InProjectB = new ProjectTeam(projectB, employee3, ProjectTeamRole.DEVELOPER);

        employee2InProjectB = projectTeamRepository.save(employee2InProjectB);
    }

    @Test
    @DisplayName("Добавить доступного сотрудника в доступный проект")
    @Tag("integrated")
    public void test() {
        Assertions.assertDoesNotThrow(
                () -> projectTeamService.addEmployeeToProject(
                        new AddEmployeeDTO(projectA.getId(), employee1.getId(), employee1InProjectA.getRole())
                ),
                "Сотрудник 1 не был добавлен в проект A"
        );
        Assertions.assertThrows(
                EntityAlreadyExistsException.class,
                () -> projectTeamService.addEmployeeToProject(
                        new AddEmployeeDTO(projectB.getId(), employee2.getId(), employee2InProjectB.getRole())
                ),
                "Сотрудник 2 был добавлен в проект B, в котором уже был ранее"
        );
        Assertions.assertDoesNotThrow(
                () -> projectTeamService.addEmployeeToProject(
                        new AddEmployeeDTO(projectA.getId(), employee3.getId(), employee3InProjectA.getRole())
                ),
                "Сотрудник 3 не был добавлен в проект A"
        );
        Assertions.assertDoesNotThrow(
                () -> projectTeamService.addEmployeeToProject(
                        new AddEmployeeDTO(projectB.getId(), employee3.getId(), employee3InProjectB.getRole())
                ),
                "Сотрудник 3 не был добавлен в проект B"
        );
        final FoundAllEmployeesDTO foundAllEmployeesOfProjectA = Assertions.assertDoesNotThrow(
                () -> projectTeamService.getAllEmployeesOfProject(new FindAllEmployeesDTO(projectA.getId())),
                "Не получилось получить сотрудников проекта A"
        );
        Assertions.assertEquals(
                2, foundAllEmployeesOfProjectA.getFoundEmployees().size(), "Неожиданный размер команды проекта A"
        );
        final List<FoundAllEmployeesDTO.FoundProjectTeamEmployee> expectedEmployeesOfGroupA = Stream.of(
                        employee1InProjectA, employee3InProjectA
                )
                .map(findAllEmployeeMapper::mapToResult)
                .toList();
        Assertions.assertTrue(
                CollectionUtils.isEqualCollection(
                        expectedEmployeesOfGroupA, foundAllEmployeesOfProjectA.getFoundEmployees()
                ), "Неожиданный состав команды проекта А"
        );
        final FoundAllEmployeesDTO foundAllEmployeesOfProjectB = Assertions.assertDoesNotThrow(
                () -> projectTeamService.getAllEmployeesOfProject(new FindAllEmployeesDTO(projectB.getId())),
                "Не получилось получить сотрудников проекта B"
        );
        Assertions.assertEquals(
                2, foundAllEmployeesOfProjectB.getFoundEmployees().size(), "Неожиданный размер команды проекта B"
        );
        final List<FoundAllEmployeesDTO.FoundProjectTeamEmployee> expectedEmployeesOfGroupB = Stream.of(
                        employee2InProjectB, employee3InProjectB
                )
                .map(findAllEmployeeMapper::mapToResult)
                .toList();
        Assertions.assertTrue(
                CollectionUtils.isEqualCollection(
                        expectedEmployeesOfGroupB, foundAllEmployeesOfProjectB.getFoundEmployees()
                ), "Неожиданный состав команды проекта B"
        );
    }

    @AfterEach
    @Transactional
    public void destroy() {
        projectTeamRepository.delete(employee1InProjectA);
        projectTeamRepository.delete(employee3InProjectA);
        projectTeamRepository.delete(employee2InProjectB);
        projectTeamRepository.delete(employee3InProjectB);

        employeeRepository.delete(employee1);
        employeeRepository.delete(employee2);
        employeeRepository.delete(employee3);

        projectRepository.delete(projectA);
        projectRepository.delete(projectB);
    }
}
