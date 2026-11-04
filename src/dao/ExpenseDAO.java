
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import model.Expense;

public class ExpenseDAO {

    // Add a new expense
    public static boolean addExpense(Expense expense) {

        String sql = "INSERT INTO expenses " +
                     "(expense_name, category, amount, expense_date) " +
                     "VALUES (?, ?, ?, ?)";

        Connection connection = DBConnection.getConnection();

        if (connection == null) {
            return false;
        }

        try (connection;
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, expense.getExpenseName());
            statement.setString(2, expense.getCategory());
            statement.setDouble(3, expense.getAmount());
            statement.setDate(
                4,
                java.sql.Date.valueOf(expense.getExpenseDate())
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error saving expense!");
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve all expenses
    public static ArrayList<Expense> getAllExpenses() {

        ArrayList<Expense> expenses = new ArrayList<>();

        String sql = "SELECT id, expense_name, category, amount, expense_date " +
                     "FROM expenses ORDER BY expense_date DESC, id DESC";

        try (Connection connection = DBConnection.getConnection()) {

            if (connection == null) {
                return expenses;
            }

            try (PreparedStatement statement =
                     connection.prepareStatement(sql);
                 ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Expense expense = new Expense(
                        result.getInt("id"),
                        result.getString("expense_name"),
                        result.getString("category"),
                        result.getDouble("amount"),
                        result.getDate("expense_date").toLocalDate()
                    );

                    expenses.add(expense);
                }
            }

        } catch (SQLException e) {

            System.out.println("Error retrieving expenses!");
            e.printStackTrace();
        }

        return expenses;
    }

    // Delete an expense using its database ID
    public static boolean deleteExpense(int id) {

        String sql = "DELETE FROM expenses WHERE id = ?";

        try (Connection connection = DBConnection.getConnection()) {

            if (connection == null) {
                return false;
            }

            try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

                statement.setInt(1, id);

                return statement.executeUpdate() > 0;
            }

        } catch (SQLException e) {

            System.out.println("Error deleting expense!");
            e.printStackTrace();
            return false;
        }
    }
    //monthlycalculationbudget
    
            public static double getMonthlyTotal(int month, int year) {

                String sql = "SELECT COALESCE(SUM(amount), 0) AS total " +
                            "FROM expenses " +
                            "WHERE MONTH(expense_date) = ? " +
                            "AND YEAR(expense_date) = ?";

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
                                return result.getDouble("total");
                            }
                        }

                    }

                } catch (SQLException e) {

                    System.out.println("Error calculating monthly spending!");
                    e.printStackTrace();
                }

                return 0;
            }
            //montlyexpensescount
            
public static int getMonthlyExpenseCount(int month, int year) {

    String sql = "SELECT COUNT(*) AS total_count " +
                 "FROM expenses " +
                 "WHERE MONTH(expense_date) = ? " +
                 "AND YEAR(expense_date) = ?";

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
                    return result.getInt("total_count");
                }
            }

        }

    } catch (SQLException e) {

        System.out.println("Error counting monthly expenses!");
        e.printStackTrace();
    }

    return 0;
}
        //highestExpense
        
public static Expense getHighestExpense() {

    String sql = "SELECT id, expense_name, category, amount, expense_date " +
                 "FROM expenses " +
                 "ORDER BY amount DESC, id DESC " +
                 "LIMIT 1";

    try (Connection connection = DBConnection.getConnection()) {

        if (connection == null) {
            return null;
        }

        try (PreparedStatement statement =
                 connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {

                return new Expense(
                    result.getInt("id"),
                    result.getString("expense_name"),
                    result.getString("category"),
                    result.getDouble("amount"),
                    result.getDate("expense_date").toLocalDate()
                );
            }

        }

    } catch (SQLException e) {

        System.out.println("Error retrieving highest expense!");
        e.printStackTrace();
    }

    return null;
}

    
public static Map<String, Double> getLastSixMonthsSpending() {

    Map<String, Double> monthlyTotals = new LinkedHashMap<>();

    // Identify the six-month date range
    YearMonth currentMonth = YearMonth.now();
    YearMonth firstMonth = currentMonth.minusMonths(5);

    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);

    // Initialize all six months with zero spending
    for (int i = 0; i < 6; i++) {

        YearMonth month = firstMonth.plusMonths(i);

        monthlyTotals.put(
            month.format(formatter),
            0.0
        );
    }

    String sql =
        "SELECT YEAR(expense_date) AS expense_year, " +
        "MONTH(expense_date) AS expense_month, " +
        "SUM(amount) AS total " +
        "FROM expenses " +
        "WHERE expense_date >= ? AND expense_date < ? " +
        "GROUP BY YEAR(expense_date), MONTH(expense_date) " +
        "ORDER BY expense_year, expense_month";

    try (Connection connection = DBConnection.getConnection()) {

        if (connection == null) {
            return monthlyTotals;
        }

        try (PreparedStatement statement =
                 connection.prepareStatement(sql)) {

            statement.setDate(
                1,
                Date.valueOf(firstMonth.atDay(1))
            );

            statement.setDate(
                2,
                Date.valueOf(currentMonth.plusMonths(1).atDay(1))
            );

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    YearMonth month = YearMonth.of(
                        result.getInt("expense_year"),
                        result.getInt("expense_month")
                    );

                    String monthName = month.format(formatter);

                    monthlyTotals.put(
                        monthName,
                        result.getDouble("total")
                    );
                }
            }
        }

    } catch (SQLException e) {

        System.out.println(
            "Error retrieving six-month spending!"
        );

        e.printStackTrace();
    }

    return monthlyTotals;
}
    
public static Map<String, Double> getCategoryWiseSpending() {

    Map<String, Double> categoryData = new LinkedHashMap<>();

    String sql = "SELECT category, SUM(amount) AS total " +
                 "FROM expenses " +
                 "GROUP BY category " +
                 "ORDER BY total DESC";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String category = rs.getString("category");
            double total = rs.getDouble("total");

            categoryData.put(category, total);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return categoryData;
}

}
