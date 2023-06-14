package org.example.api.utils.swagger;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProjectSwaggerDescriptions {

    public final String CREATE_PROJECT_DESCRIPTION = """
            Создает проект с перечисленными в теле запроса набором атрибутов и статусом Черновик""";

    public final String CREATE_PROJECT_SUCCESS_RESPONSE = """
            Проект создан успешно. В теле ответа размещен id созданного проекта.""";

    public final String CREATE_PROJECT_ALREADY_EXISTS = """
            Уже существует проект с указанным кодом.""" +
            ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String UPDATE_PROJECT_DESCRIPTION = """
            Изменяет проект в соответствии с аттрибутами, переданными в теле запроса.""";

    public final String UPDATE_PROJECT_SUCCESS_RESPONSE = """
            Проект обновлен успешно.""";

    public final String UPDATE_PROJECT_NOT_EXISTS = """
            Попытка обновить проект по id, который не соответствует ни одному проекту.""" +
            ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String UPDATE_PROJECT_CONFLICT = """
            Попытка обновить проект, неуникальным кодом""" +
            ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String GET_PROJECTS_BY_FILTER_DESCRIPTION = """
            Поиск осущетсвляется по текстовому фильтру (по полям Наименование преокта, Код проекта) и с применением
            фильтра по статусу проекта. Если ни один из фильтров не задан, то возвращаются все созданные проекты""";

    public final String GET_PROJECTS_BY_FILTER_SUCCESS = """
            В теле ответа возвращается список всех проектов, удовлетворяющих фильтру.""";

    public final String CHANGE_STATUS_DESCRIPTION = """
            Переводит проект на следующий этап""";

    public final String CHANGE_STATUS_SUCCESS_RESPONSE = """
            Статус проекта успешно изменен, в теле ответа размещен новый статус""";

    public final String CHANGE_STATUS_NOT_EXISTS = """
            Попытка перевести статус для проекта по id, который не соответствует ни одному проекту""";
}
