package mealplanner.dao;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class DataSource {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            log.error("Errore durante il caricamento del driver", e);
        }
    }

    private DataSource() {
    }

    // per utilizzare PostgreSQL
    private static final String databaseURI = "jdbc:postgresql:meals_db";
    private static final String userName = "postgres";
    private static final String password = "1111";


    public static Connection getConnection() throws DAOException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(databaseURI, userName, password);
        } catch (SQLException sqle) {
            close(connection);
            log.error("Errore durante l'esecuzione della query", sqle);
            throw new DAOException("getConnection: " + sqle);
        }
        return connection;
    }

    public static void rollback(Connection connection) throws DAOException {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqle) {
            log.error("Errore durante la chiusura", sqle);
        }
    }

    public static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException sqle) {
            log.error("Errore durante la chiusura", sqle);
        }
    }

    public static void close(PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException sqle) {
            log.error("Errore durante la chiusura", sqle);
        }
    }

    public static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException sqle) {
            log.error("Errore durante la chiusura", sqle);
        }
    }

}
