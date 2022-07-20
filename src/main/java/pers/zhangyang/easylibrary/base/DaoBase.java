package pers.zhangyang.easylibrary.base;

import pers.zhangyang.easylibrary.yaml.DatabaseYaml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DaoBase {
    public abstract int init() throws SQLException;

    private static final ThreadLocal<Connection> t = new ThreadLocal<>();
    /**
     * return 返回当前线程的Connection对象
     *
     * @throws SQLException 数据库异常
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = t.get();
        if (connection == null || connection.isClosed()) {
            DatabaseYaml databaseYamlManager = DatabaseYaml.INSTANCE;
            connection = DriverManager.getConnection(databaseYamlManager.getStringDefault("database.url"),
                    databaseYamlManager.getString("database.username"),
                    databaseYamlManager.getString("database.password"));
            connection.setAutoCommit(false);
            t.set(connection);
        }
        return connection;
    }

}
