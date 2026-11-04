package service;

import java.util.HashMap;

public class BudgetService {

    // Stores a separate budget for each month and year
    private static HashMap<String, Double> budgets = new HashMap<>();

    // Create a unique key for each month
    private static String getKey(int month, int year) {
        return year + "-" + month;
    }

    // Save or update a monthly budget
    public static void setBudget(int month, int year, double amount) {
        budgets.put(getKey(month, year), amount);
    }

    // Get the budget for a particular month
    public static double getBudget(int month, int year) {
        return budgets.getOrDefault(getKey(month, year), 0.0);
    }
}