
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/personal_expense_tracker";

    private static final String USER = "root";

    public static Connection getConnection() {

        try {

            String password = System.getenv("DB_PASSWORD");

            if (password == null || password.isEmpty()) {
                throw new IllegalStateException(
                    "DB_PASSWORD environment variable is not set."
                );
            }

            Connection connection = DriverManager.getConnection(
                URL, USER, password
            );

            System.out.println("MySQL Connected Successfully!");

            return connection;

        } catch (Exception e) {

            System.out.println("MySQL Connection Failed!");
            e.printStackTrace();

            return null;
        }
    }
}
