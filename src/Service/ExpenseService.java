
package service;

import model.Expense;
import java.util.ArrayList;
import java.time.LocalDate;

public class ExpenseService {

    private static ArrayList<Expense> expenses =
            new ArrayList<>();

    // Add expense
    public static void addExpense(Expense expense) {
        expenses.add(expense);
    }

    // Get all expenses
    public static ArrayList<Expense> getExpenses() {
        return expenses;
    }

    // Calculate all expenses
    public static double getTotalExpenses() {

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        return total;
    }

    // Delete expense
    public static void deleteExpense(int index) {

        if (index >= 0 && index < expenses.size()) {
            expenses.remove(index);
        }
    }

    // Calculate monthly spending
    public static double getMonthlySpending(
            int month, int year) {

        double total = 0;

        for (Expense expense : expenses) {

            LocalDate date = expense.getExpenseDate();

            if (date.getMonthValue() == month &&
                date.getYear() == year) {

                total += expense.getAmount();
            }
        }

        return total;
    }

    // Count monthly expenses
    public static int getMonthlyExpenseCount(
            int month, int year) {

        int count = 0;

        for (Expense expense : expenses) {

            LocalDate date = expense.getExpenseDate();

            if (date.getMonthValue() == month &&
                date.getYear() == year) {

                count++;
            }
        }

        return count;
    }
    public static Expense getHighestExpense() {

    // Check whether the expense list is empty
    if (expenses.isEmpty()) {
        return null;
    }

    // Assume the first expense is the highest
    Expense highest = expenses.get(0);

    // Compare all expenses
    for (Expense expense : expenses) {

        if (expense.getAmount() > highest.getAmount()) {
            highest = expense;
        }
    }

    return highest;
}
}
