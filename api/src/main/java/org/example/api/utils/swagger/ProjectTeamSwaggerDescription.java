package org.example.api.utils.swagger;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProjectTeamSwaggerDescription {

    public final String ADD_EMPLOYEE_IN_PROJECT_DESCRIPTION = """
            Добавить сотрудника в команду проекта""";

    public final String ADD_EMPLOYEE_IN_PROJECT_SUCCESS_RESPONSE = """
            Сотрудник успешно добавлен в команду проекта.""";

    public final String ADD_EMPLOYEE_IN_PROJECT_NOT_FOUND = """
            Сотрудник или проект не существуют""" + ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String ADD_EMPLOYEE_IN_PROJECT_ALREADY_EXISTS = """
            Сотрудник уже был ранее добавлен в команду проекта или статус сотрудника Удален.""" +
            ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String EXCLUDE_EMPLOYEE_FROM_PROJECT_DESCRIPTION = """
            Исключить сотрудника из команды проекта""";

    public final String EXCLUDE_EMPLOYEE_FROM_PROJECT_SUCCESS_RESPONSE = """
            Сотрудник успешно исключен из команды проекта.""";

    public final String EXCLUDE_EMPLOYEE_FROM_PROJECT_NOT_FOUND = """
            Сотрудник не является участником команды проекта.""" + ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String GET_EMPLOYEES_FROM_PROJECT_DESCRIPTION = """
            Получить всех участников проекта""";

    public final String GET_EMPLOYEES_FROM_PROJECT_SUCCESS_RESPONSE = """
            В теле ответа содрежатся все сотрудники проекта.""";

    public final String GET_EMPLOYEES_FROM_PROJECT_NOT_FOUND = """
            Попытка получить всех сотрудников проекта, которого не существует.""" +
            ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;
}
