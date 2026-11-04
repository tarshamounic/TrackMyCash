
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetDAO {

    // Save or update a monthly budget
    public static boolean saveBudget(int month, int year, double amount) {

        String sql = "INSERT INTO budgets (month, year, amount) " +
                     "VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE amount = ?";

        try (Connection connection = DBConnection.getConnection()) {

            if (connection == null) {
                return false;
            }

            try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

                statement.setInt(1, month);
                statement.setInt(2, year);
                statement.setDouble(3, amount);
                statement.setDouble(4, amount);

                return statement.executeUpdate() > 0;
            }

        } catch (SQLException e) {

            System.out.println("Error saving budget!");
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve a budget for a particular month and year
    public static double getBudget(int month, int year) {

        String sql = "SELECT amount FROM budgets " +
                     "WHERE month = ? AND year = ?";

        try (Connection connection = DBConnection.getConnection()) {

            if (connection == null) {
                return 0;
            }

            try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

                statement.setInt(1, month);
                statement.setInt(2, year);

                try (ResultSet result = statement.executeQuery()) {

                    if (result.next()) {
                        return result.getDouble("amount");
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println("Error retrieving budget!");
            e.printStackTrace();
        }

        return 0;
    }
}
