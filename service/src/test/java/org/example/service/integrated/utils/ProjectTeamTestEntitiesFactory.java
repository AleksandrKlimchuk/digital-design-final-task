package org.example.service.integrated.utils;

import lombok.experimental.UtilityClass;
import org.example.status.EmployeeStatus;
import org.example.status.ProjectStatus;
import org.example.store.model.Employee;
import org.example.store.model.Project;

@UtilityClass
public class ProjectTeamTestEntitiesFactory {

    public static Employee createEmployee1() {
        return Employee.builder().firstName("1").lastName("11").status(EmployeeStatus.ACTIVE).build();
    }

    public static Employee createEmployee2() {
        return Employee.builder().firstName("2").lastName("22").status(EmployeeStatus.ACTIVE).build();
    }

    public static Employee createEmployee3() {
        return Employee.builder().firstName("3").lastName("33").status(EmployeeStatus.ACTIVE).build();
    }

    public static Project createProjectA() {
        return Project.builder().code("A").title("AA").status(ProjectStatus.DRAFT).build();
    }
    public static Project createProjectB() {
        return Project.builder().code("B").title("BB").status(ProjectStatus.DRAFT).build();
    }
}
