
package app;

import dao.ExpenseDAO;
import model.Expense;

import java.time.LocalDate;

public class TestExpenseDAO {

    public static void main(String[] args) {

        Expense expense = new Expense(
            "Test Lunch",
            "Food",
            150.00,
            LocalDate.now()
        );

        boolean result = ExpenseDAO.addExpense(expense);

        if (result) {
            System.out.println("Expense saved to MySQL!");
        } else {
            System.out.println("Expense could not be saved.");
        }
    }
}
