package com.ahrankina.task1;

import java.sql.*;

class JDBCUtils {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/ain?useSSL=false&useUnicode=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static String INSERT_NAME = "INSERT INTO articles(title, category, url, text) VALUES (?, ?, ?, ?);";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    static void insert(String title, String category, String url, String text) {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_NAME,
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE)) {
            statement.setString(1, title);
            statement.setString(2, category);
            statement.setString(3, url);
            statement.setString(4, text);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}