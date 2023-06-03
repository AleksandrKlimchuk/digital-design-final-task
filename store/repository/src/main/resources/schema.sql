CREATE TABLE IF NOT EXISTS employee(
    id          BIGSERIAL       PRIMARY KEY,
    first_name  VARCHAR(32)     NOT NULL,
    last_name   VARCHAR(32)     NOT NULL,
    patronymic  VARCHAR(32),
    account     VARCHAR(32),
    email       VARCHAR(256),
    status      SMALLINT        NOT NULL
);

CREATE TABLE IF NOT EXISTS project(
    id          BIGSERIAL       PRIMARY KEY,
    code        VARCHAR(64)     UNIQUE NOT NULL,
    title       VARCHAR(64)     NOT NULL,
    description TEXT,
    status      SMALLINT        NOT NULL
);

CREATE TABLE IF NOT EXISTS project_team(
    employee_id BIGINT          REFERENCES employee(id),
    project_id  BIGINT          REFERENCES project(id),
    role        SMALLINT        NOT NULL,
    PRIMARY KEY (employee_id, project_id)
);

CREATE TABLE IF NOT EXISTS task(
    id          BIGSERIAL       PRIMARY KEY,
    title       VARCHAR(64)     NOT NULL,
    description TEXT,
    executor_id BIGINT          REFERENCES employee(id),
    project_id  BIGINT          NOT NULL REFERENCES project(id),
    workload    SMALLINT        NOT NULL,
    deadline    TIMESTAMP       NOT NULL,
    status      SMALLINT        NOT NULL,
    author_id   BIGINT          NOT NULL REFERENCES employee(id),
    created_at  TIMESTAMP       NOT NULL,
    updated_at  TIMESTAMP       NOT NULL,
    CHECK (created_at + make_interval(hours := workload) < deadline)
);
