
package model;

import java.time.LocalDate;

public class Expense {

    private int id;
    private String expenseName;
    private String category;
    private double amount;
    private LocalDate expenseDate;

    // Constructor for a newly created expense
    public Expense(String expenseName, String category,
                   double amount, LocalDate expenseDate) {

        this.id = -1;
        this.expenseName = expenseName;
        this.category = category;
        this.amount = amount;
        this.expenseDate = expenseDate;
    }

    // Constructor for an expense retrieved from MySQL
    public Expense(int id, String expenseName, String category,
                   double amount, LocalDate expenseDate) {

        this.id = id;
        this.expenseName = expenseName;
        this.category = category;
        this.amount = amount;
        this.expenseDate = expenseDate;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }
}
