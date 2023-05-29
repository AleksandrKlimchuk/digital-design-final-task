package org.example.store.repository.jdbc_repository;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.postgresql.ds.PGConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConnectionPool {

    PGConnectionPoolDataSource DATA_SOURCE;

    static String HOST_NAME = "0.0.0.0";
    static String DB_NAME = "digital-design-final-task";
    static String USERNAME = "postgres";
    static String PASSWORD = "postgres"; // Конечно плохо так хранить, но и postgres postgres знают все
    static int PORT = 4123;

    private static class DataSourceHolder {
        static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    ConnectionPool() {
        DATA_SOURCE = new PGConnectionPoolDataSource();
        DATA_SOURCE.setServerNames(new String[]{HOST_NAME});
        DATA_SOURCE.setPortNumbers(new int[]{PORT});
        DATA_SOURCE.setDatabaseName(DB_NAME);
        DATA_SOURCE.setUser(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
    }

    @SneakyThrows(SQLException.class)
    public static Connection getConnection() {
        return DataSourceHolder.INSTANCE.DATA_SOURCE.getConnection();
    }
}
