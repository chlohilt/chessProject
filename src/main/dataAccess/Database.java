package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Database is responsible for creating connections to the database. Connections are
 * managed with a simple pool in order to increase performance. To obtain and
 * use connections represented by this class use the following pattern.
 *
 * <pre>
 *  public boolean example(String selectStatement, Database db) throws DataAccessException{
 *    var conn = db.getConnection();
 *    try (var preparedStatement = conn.prepareStatement(selectStatement)) {
 *        return preparedStatement.execute();
 *    } catch (SQLException ex) {
 *        throw new DataAccessException(ex.toString());
 *    } finally {
 *        db.returnConnection(conn);
 *    }
 *  }
 * </pre>
 */
public class Database {
    public static final String DB_NAME = "chess";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = System.getenv().get("MYSQL_PASSWORD"); // environment variable for password

    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306";

    private final LinkedList<Connection> connections = new LinkedList<>();

    /**
     * Get a connection to the database. This pulls a connection out of a simple
     * pool implementation. The connection must be returned to the pool after
     * you are done with it by calling {@link #returnConnection(Connection) returnConnection}.
     *
     * @return Connection
     */
    synchronized public Connection getConnection() throws DataAccessException {
        try {
            Connection connection;
            if (connections.isEmpty()) {
                connection = DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
                connection.setCatalog(DB_NAME);
            } else {
                connection = connections.removeFirst();
            }
            return connection;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Return a previously acquired connection to the pool.
     *
     * @param connection previous obtained by calling {@link #getConnection() getConnection}.
     */
    synchronized public void returnConnection(Connection connection) {
        connections.add(connection);
    }

    void configureUserDatabase() throws DataAccessException, SQLException {
        try (var conn = getConnection()) {
            // DATABASE CREATION
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess");
            createDbStatement.executeUpdate();

            // USER TABLE CREATION
            conn.setCatalog("user_data");

            var createUserTable = """
            CREATE TABLE  IF NOT EXISTS user_data (
                username VARCHAR(225) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
            )""";


            try (var createTableStatement = conn.prepareStatement(createUserTable)) {
                createTableStatement.executeUpdate();
            }

            // GAME TABLE CREATION
            conn.setCatalog("game_data");

            var createGameTable = """
            CREATE TABLE  IF NOT EXISTS game_data (
                gameID INT NOT NULL,
                gameName VARCHAR(255) NOT NULL,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameInfo longtext NOT NULL,
                PRIMARY KEY (username)
            )""";


            try (var createTableStatement = conn.prepareStatement(createGameTable)) {
                createTableStatement.executeUpdate();
            }

            // AUTH TABLE CREATION
            conn.setCatalog("auth_data");

            var createAuthTable = """
            CREATE TABLE  IF NOT EXISTS auth_data (
                authToken VARCHAR(225) NOT NULL,
                username VARCHAR(255) NOT NULL,
                PRIMARY KEY (authToken)
            )""";


            try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }
        }
    }

    public boolean runStatement(String statement, Database db) throws DataAccessException{

        var conn = db.getConnection();

        try (var preparedStatement = conn.prepareStatement(statement)) {
            return preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(conn);
        }
    }

}

