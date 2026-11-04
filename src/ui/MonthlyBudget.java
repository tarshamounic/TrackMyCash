
package ui;

import dao.BudgetDAO;
import dao.ExpenseDAO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MonthlyBudget extends JFrame {

    private JComboBox<String> monthBox;
    private JTextField yearField;
    private JTextField budgetField;

    private JLabel budgetLabel;
    private JLabel spendingLabel;
    private JLabel remainingLabel;
    private JLabel statusLabel;
    private JLabel percentageLabel;

    private JProgressBar progressBar;

    private final Color NAVY = new Color(25, 42, 86);
    private final Color BLUE = new Color(59, 130, 246);
    private final Color GREEN = new Color(22, 163, 74);
    private final Color RED = new Color(220, 65, 65);
    private final Color BACKGROUND = new Color(245, 247, 251);
    private final Color TEXT = new Color(45, 55, 72);
    private final Color MUTED = new Color(120, 130, 150);

    private final String[] months = {
        "January", "February", "March",
        "April", "May", "June",
        "July", "August", "September",
        "October", "November", "December"
    };

    public MonthlyBudget() {

        setTitle("Monthly Budget Manager");
        setSize(1100, 750);
        setMinimumSize(new Dimension(850, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));

        JPanel headingPanel = new JPanel();
        headingPanel.setOpaque(false);
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Monthly Budget Manager");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "Plan your monthly spending and track your budget"
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(210, 220, 240));

        headingPanel.add(title);
        headingPanel.add(Box.createVerticalStrut(5));
        headingPanel.add(subtitle);

        JButton backButton = new JButton("← Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(55, 75, 125));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(190, 42));
        backButton.addActionListener(e -> goToDashboard());

        header.add(headingPanel, BorderLayout.WEST);
        header.add(backButton, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // MAIN CONTENT
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(22, 30, 25, 30)
        );

        // PERIOD AND BUDGET CARD
        JPanel inputCard = new JPanel(new GridBagLayout());
        inputCard.setBackground(Color.WHITE);
        inputCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 234, 242)),
            BorderFactory.createEmptyBorder(18, 22, 18, 22)
        ));
        inputCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel inputHeading = new JLabel("Set Monthly Budget");
        inputHeading.setFont(new Font("Segoe UI", Font.BOLD, 17));
        inputHeading.setForeground(NAVY);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputCard.add(inputHeading, gbc);

        gbc.gridwidth = 1;

        // Month
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputCard.add(createLabel("Select Month"), gbc);

        monthBox = new JComboBox<>(months);
        monthBox.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        monthBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        monthBox.setBackground(Color.WHITE);
        monthBox.setPreferredSize(new Dimension(230, 36));

        gbc.gridx = 1;
        inputCard.add(monthBox, gbc);

        // Year
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputCard.add(createLabel("Enter Year"), gbc);

        yearField = new JTextField(
            String.valueOf(LocalDate.now().getYear())
        );
        styleTextField(yearField);

        gbc.gridx = 1;
        inputCard.add(yearField, gbc);

        // Budget amount
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputCard.add(createLabel("Budget Amount (₹)"), gbc);

        budgetField = new JTextField();
        styleTextField(budgetField);

        gbc.gridx = 1;
        inputCard.add(budgetField, gbc);

        // BUTTONS
        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 12, 0)
        );
        buttonPanel.setOpaque(false);

        JButton saveButton = createButton("Save Budget", BLUE);
        JButton checkButton = createButton("Check Budget", NAVY);

        buttonPanel.add(saveButton);
        buttonPanel.add(checkButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 5, 8);

        inputCard.add(buttonPanel, gbc);

        mainPanel.add(inputCard);
        mainPanel.add(Box.createVerticalStrut(18));

        // SUMMARY HEADING
        JLabel summaryHeading = new JLabel("Budget Overview");
        summaryHeading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        summaryHeading.setForeground(NAVY);
        summaryHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(summaryHeading);
        mainPanel.add(Box.createVerticalStrut(12));

        // SUMMARY CARDS
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryPanel.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 105)
        );

        JPanel budgetCard = createSummaryCard(
            "MONTHLY BUDGET", "₹0.00"
        );

        JPanel spendingCard = createSummaryCard(
            "TOTAL SPENDING", "₹0.00"
        );

        JPanel remainingCard = createSummaryCard(
            "REMAINING", "₹0.00"
        );

        budgetLabel = (JLabel) budgetCard.getClientProperty("valueLabel");
        spendingLabel = (JLabel) spendingCard.getClientProperty("valueLabel");
        remainingLabel = (JLabel) remainingCard.getClientProperty("valueLabel");

        summaryPanel.add(budgetCard);
        summaryPanel.add(spendingCard);
        summaryPanel.add(remainingCard);

        mainPanel.add(summaryPanel);
        mainPanel.add(Box.createVerticalStrut(18));

        // PROGRESS CARD
        JPanel progressCard = new JPanel();
        progressCard.setLayout(new BoxLayout(progressCard, BoxLayout.Y_AXIS));
        progressCard.setBackground(Color.WHITE);
        progressCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        progressCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 234, 242)),
            BorderFactory.createEmptyBorder(18, 22, 18, 22)
        ));

        JPanel progressHeader = new JPanel(new BorderLayout());
        progressHeader.setOpaque(false);

        JLabel progressTitle = new JLabel("Budget Usage");
        progressTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        progressTitle.setForeground(NAVY);

        percentageLabel = new JLabel("0% Used");
        percentageLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        percentageLabel.setForeground(BLUE);

        progressHeader.add(progressTitle, BorderLayout.WEST);
        progressHeader.add(percentageLabel, BorderLayout.EAST);

        progressCard.add(progressHeader);
        progressCard.add(Box.createVerticalStrut(15));

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString("0%");
        progressBar.setForeground(BLUE);
        progressBar.setBackground(new Color(230, 235, 243));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(500, 20));
        progressBar.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 22)
        );

        progressCard.add(progressBar);
        progressCard.add(Box.createVerticalStrut(15));

        statusLabel = new JLabel(
            "Select a month and check your budget."
        );
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(MUTED);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        progressCard.add(statusLabel);

        mainPanel.add(progressCard);

        // ACTIONS
        saveButton.addActionListener(e -> saveBudget());
        checkButton.addActionListener(e -> checkBudget());
        yearField.addActionListener(e -> checkBudget());

        add(mainPanel, BorderLayout.CENTER);

        // Load current month's saved budget
        loadBudgetOverview(false);

        // OPEN MAXIMIZED
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    // BACK TO DASHBOARD
    private void goToDashboard() {

        dispose();

        Dashboard dashboard = Dashboard.getExistingDashboard();

        if (dashboard != null) {

            dashboard.refreshForNavigation();
            dashboard.setExtendedState(JFrame.MAXIMIZED_BOTH);
            dashboard.setVisible(true);
            dashboard.toFront();

        } else {

            new Dashboard();
        }
    }

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);

        return label;
    }

    private void styleTextField(JTextField field) {

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(230, 36));

        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 218, 230)),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
    }

    private JPanel createSummaryCard(String heading, String value) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 234, 242)),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        headingLabel.setForeground(MUTED);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(NAVY);

        card.add(headingLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(valueLabel);

        card.putClientProperty("valueLabel", valueLabel);

        return card;
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(145, 38));

        return button;
    }

    private int getSelectedMonth() {
        return monthBox.getSelectedIndex() + 1;
    }

    private int getSelectedYear() {
        return Integer.parseInt(yearField.getText().trim());
    }

    private boolean isValidYear(int year) {

        if (year < 1 || year > 9999) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid year.",
                "Invalid Year",
                JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    private void saveBudget() {

        try {

            int month = getSelectedMonth();
            int year = getSelectedYear();

            if (!isValidYear(year)) {
                return;
            }

            double budget = Double.parseDouble(
                budgetField.getText().trim()
            );

            if (!Double.isFinite(budget) || budget <= 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a budget greater than zero.",
                    "Invalid Budget",
                    JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            boolean saved = BudgetDAO.saveBudget(
                month,
                year,
                budget
            );

            if (saved) {

                JOptionPane.showMessageDialog(
                    this,
                    "Monthly budget saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                budgetField.setText("");

                loadBudgetOverview(false);

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Could not save the budget. Please try again.",
                    "Save Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid year and budget amount.",
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Unable to save budget.\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void checkBudget() {
        loadBudgetOverview(true);
    }

    private void loadBudgetOverview(boolean showMissingMessage) {

        try {

            int month = getSelectedMonth();
            int year = getSelectedYear();

            if (!isValidYear(year)) {
                return;
            }

            double budget = BudgetDAO.getBudget(month, year);

            if (budget <= 0) {

                budgetLabel.setText("Not set");
                spendingLabel.setText("₹0.00");
                remainingLabel.setText("₹0.00");

                percentageLabel.setText("0% Used");
                progressBar.setValue(0);
                progressBar.setString("No budget");
                progressBar.setForeground(BLUE);

                statusLabel.setText("No budget saved for this month.");
                statusLabel.setForeground(MUTED);

                if (showMissingMessage) {

                    JOptionPane.showMessageDialog(
                        this,
                        "No budget found for the selected month.",
                        "Budget Not Found",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }

                return;
            }

            double spending = ExpenseDAO.getMonthlyTotal(month, year);
            double remaining = budget - spending;

            budgetLabel.setText(String.format("₹%.2f", budget));
            spendingLabel.setText(String.format("₹%.2f", spending));
            remainingLabel.setText(String.format("₹%.2f", remaining));

            // Calculate actual usage percentage
            double usage = (spending / budget) * 100;

            int progress = (int) Math.min(100, Math.max(0, usage));

            progressBar.setValue(progress);
            percentageLabel.setText(String.format("%.1f%% Used", usage));

            if (remaining > 0) {

                progressBar.setForeground(
                    usage >= 80 ? new Color(234, 160, 35) : GREEN
                );

                progressBar.setString(String.format("%.1f%%", usage));

                statusLabel.setText(
                    String.format(
                        "Within Budget - ₹%.2f remaining",
                        remaining
                    )
                );

                statusLabel.setForeground(GREEN);

            } else if (remaining == 0) {

                progressBar.setForeground(RED);
                progressBar.setString("100%");

                statusLabel.setText("Budget Fully Used!");
                statusLabel.setForeground(RED);

            } else {

                progressBar.setForeground(RED);
                progressBar.setString("Exceeded");

                statusLabel.setText(
                    String.format(
                        "Budget Exceeded by ₹%.2f",
                        Math.abs(remaining)
                    )
                );

                statusLabel.setForeground(RED);
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid year.",
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Unable to retrieve budget details.\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
