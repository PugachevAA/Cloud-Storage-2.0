import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class DB {
    private Connection sqlConnection;
    private Statement statement;

    public void getConnection() throws SQLException {
        try {
            sqlConnection = DriverManager.getConnection(Config.SQL_URL, Config.SQL_USER, Config.SQL_PASS);
            statement = sqlConnection.createStatement();
            log.debug("Подключение к БД открыто");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Ошибка подключения к БД");
        }
    }
    public void closeConnection() {
        try {
            sqlConnection.close();
            statement.close();
            log.debug("Подключение к БД закрыто");
        } catch (SQLException throwables) {
            log.error("Ошибка закрытия подключения к БД");
        }
    }

    public ResultSet read(String query) {
        ResultSet queryResult = null;
        try {
            queryResult = statement.executeQuery(query);
        } catch (SQLException throwables) {
            log.error("Ошибка чтения запроса");
        }
        return queryResult;
    }

    public int update(String query) {
        int queryResult = 0;
        try {
            queryResult = statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return queryResult;
    }
}
