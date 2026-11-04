
package ui;

import dao.ExpenseDAO;
import model.Expense;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class ExpenseHistory extends JFrame {

    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JLabel countLabel;

    private final Color NAVY = new Color(25, 42, 86);
    private final Color BLUE = new Color(59, 130, 246);
    private final Color BACKGROUND = new Color(245, 247, 251);
    private final Color TEXT = new Color(45, 55, 72);
    private final Color MUTED = new Color(120, 130, 150);

    public ExpenseHistory() {

        setTitle("Expense History");
        setSize(1100, 750);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));

        JPanel headingPanel = new JPanel();
        headingPanel.setOpaque(false);
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Expense History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "View and manage all your recorded expenses"
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
        JPanel mainPanel = new JPanel(new BorderLayout(0, 18));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(25, 28, 25, 28)
        );

        // SUMMARY CARDS
        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        summaryPanel.setOpaque(false);

        JPanel totalCard = createSummaryCard(
            "TOTAL EXPENSES", "₹0.00"
        );

        JPanel countCard = createSummaryCard(
            "NUMBER OF RECORDS", "0"
        );

        totalLabel = (JLabel) totalCard.getClientProperty("valueLabel");
        countLabel = (JLabel) countCard.getClientProperty("valueLabel");

        summaryPanel.add(totalCard);
        summaryPanel.add(countCard);

        mainPanel.add(summaryPanel, BorderLayout.NORTH);

        // TABLE PANEL
        JPanel tableCard = new JPanel(new BorderLayout(0, 12));
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 234, 242)),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel tableTitle = new JLabel("All Transactions");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tableTitle.setForeground(TEXT);

        tableCard.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {
            "Expense Name",
            "Category",
            "Amount",
            "Date",
            "Database ID"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        expenseTable = new JTable(tableModel);

        expenseTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        expenseTable.setRowHeight(42);
        expenseTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        expenseTable.setShowGrid(false);
        expenseTable.setIntercellSpacing(new Dimension(0, 0));
        expenseTable.setBackground(Color.WHITE);
        expenseTable.setSelectionBackground(new Color(225, 237, 255));
        expenseTable.setSelectionForeground(NAVY);
        expenseTable.setFillsViewportHeight(true);

        JTableHeader tableHeader = expenseTable.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableHeader.setBackground(new Color(241, 245, 251));
        tableHeader.setForeground(NAVY);
        tableHeader.setPreferredSize(new Dimension(0, 42));
        tableHeader.setReorderingAllowed(false);

        // Hide database ID column
        expenseTable.removeColumn(
            expenseTable.getColumnModel().getColumn(4)
        );

        // Amount column alignment
        DefaultTableCellRenderer rightRenderer =
            new DefaultTableCellRenderer();

        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        expenseTable.getColumnModel().getColumn(2)
            .setCellRenderer(rightRenderer);

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(
            new Color(235, 238, 244)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableCard.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tableCard, BorderLayout.CENTER);

        // BOTTOM BUTTONS
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JLabel hint = new JLabel(
            "Select a transaction to delete it."
        );
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hint.setForeground(MUTED);

        JPanel buttons = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        buttons.setOpaque(false);

        JButton refreshButton = createButton("Refresh", BLUE);
        JButton deleteButton = createButton(
            "Delete Selected", new Color(220, 65, 65)
        );

        buttons.add(refreshButton);
        buttons.add(deleteButton);

        bottomPanel.add(hint, BorderLayout.WEST);
        bottomPanel.add(buttons, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        refreshButton.addActionListener(e -> loadExpenses());
        deleteButton.addActionListener(e -> deleteSelectedExpense());

        loadExpenses();

        // Open maximized
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

    private JPanel createSummaryCard(String heading, String value) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 234, 242)),
            BorderFactory.createEmptyBorder(15, 18, 15, 18)
        ));

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        headingLabel.setForeground(MUTED);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 23));
        valueLabel.setForeground(NAVY);

        card.add(headingLabel);
        card.add(Box.createVerticalStrut(8));
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
        button.setPreferredSize(new Dimension(145, 40));

        return button;
    }

    private void loadExpenses() {

        tableModel.setRowCount(0);

        try {

            ArrayList<Expense> expenses = ExpenseDAO.getAllExpenses();

            double total = 0;

            for (Expense expense : expenses) {

                Object[] row = {
                    expense.getExpenseName(),
                    expense.getCategory(),
                    String.format("₹%.2f", expense.getAmount()),
                    expense.getExpenseDate(),
                    expense.getId()
                };

                tableModel.addRow(row);
                total += expense.getAmount();
            }

            totalLabel.setText(String.format("₹%.2f", total));
            countLabel.setText(String.valueOf(expenses.size()));

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Unable to load expenses.\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteSelectedExpense() {

        int selectedRow = expenseTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select an expense to delete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow = expenseTable.convertRowIndexToModel(selectedRow);

        int expenseId = (int) tableModel.getValueAt(modelRow, 4);

        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this expense?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmation == JOptionPane.YES_OPTION) {

            boolean deleted = ExpenseDAO.deleteExpense(expenseId);

            if (deleted) {

                JOptionPane.showMessageDialog(
                    this,
                    "Expense deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                loadExpenses();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Could not delete the expense. Please try again.",
                    "Delete Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
