
package ui;

import dao.ExpenseDAO;
import model.Expense;
import service.ExpenseService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddExpense extends JFrame {

    private JTextField nameField;
    private JTextField amountField;
    private JTextField dateField;

    private JComboBox<String> categoryBox;

    private final Color navy = new Color(25, 42, 86);
    private final Color blue = new Color(55, 115, 230);
    private final Color background = new Color(245, 247, 251);
    private final Color textColor = new Color(45, 55, 75);
    private final Color muted = new Color(120, 130, 150);
    private final Color borderColor = new Color(220, 228, 240);

    public AddExpense() {

        setTitle("Add Expense - Personal Expense Tracker");

        setSize(1100, 750);
        setMinimumSize(new Dimension(850, 650));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(background);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createForm(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        // Open feature screen in maximized mode
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setVisible(true);
    }

    // HEADER

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(background);

        header.setBorder(
            new EmptyBorder(25, 35, 15, 35)
        );

        // Back button

        JButton backButton = new JButton("←  Back to Dashboard");

        backButton.setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );

        backButton.setForeground(navy);
        backButton.setBackground(Color.WHITE);

        backButton.setFocusPainted(false);
        backButton.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        backButton.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1, true),
                new EmptyBorder(10, 16, 10, 16)
            )
        );

        backButton.addActionListener(e -> goToDashboard());

        backButton.addMouseListener(
            new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    backButton.setBackground(
                        new Color(235, 240, 250)
                    );
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    backButton.setBackground(Color.WHITE);
                }
            }
        );

        JPanel titlePanel = new JPanel();

        titlePanel.setLayout(
            new BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        );

        titlePanel.setBackground(background);

        JLabel title = new JLabel("Add New Expense");

        title.setFont(
            new Font("Segoe UI", Font.BOLD, 28)
        );

        title.setForeground(navy);

        JLabel subtitle = new JLabel(
            "Record and manage your daily expenses."
        );

        subtitle.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        subtitle.setForeground(muted);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(7));
        titlePanel.add(subtitle);

        JPanel leftPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 0, 0)
        );

        leftPanel.setBackground(background);
        leftPanel.add(backButton);

        JPanel combined = new JPanel(
            new BorderLayout(0, 18)
        );

        combined.setBackground(background);
        combined.add(leftPanel, BorderLayout.NORTH);
        combined.add(titlePanel, BorderLayout.CENTER);

        header.add(combined, BorderLayout.CENTER);

        return header;
    }

    // FORM

    private JPanel createForm() {

        JPanel outer = new JPanel(new GridBagLayout());

        outer.setBackground(background);

        outer.setBorder(
            new EmptyBorder(5, 35, 15, 35)
        );

        JPanel card = new JPanel(new GridBagLayout());

        card.setBackground(Color.WHITE);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1, true),
                new EmptyBorder(30, 35, 30, 35)
            )
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.insets = new Insets(0, 0, 25, 0);

        JLabel sectionTitle = new JLabel("Expense Details");

        sectionTitle.setFont(
            new Font("Segoe UI", Font.BOLD, 19)
        );

        sectionTitle.setForeground(textColor);

        card.add(sectionTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 15);

        addLabel(card, "Expense Name", gbc, 0);
        addLabel(card, "Category", gbc, 1);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 22, 15);

        nameField = createTextField();

        addField(card, nameField, gbc, 0);

        String[] categories = {
            "Food",
            "Travel",
            "Shopping",
            "Education",
            "Bills",
            "Healthcare",
            "Entertainment",
            "Others"
        };

        categoryBox = new JComboBox<>(categories);

        categoryBox.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        categoryBox.setBackground(Color.WHITE);

        categoryBox.setPreferredSize(
            new Dimension(200, 44)
        );

        addField(card, categoryBox, gbc, 1);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 15);

        addLabel(card, "Amount (INR)", gbc, 0);
        addLabel(card, "Expense Date", gbc, 1);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 15);

        amountField = createTextField();
        dateField = createTextField();

        dateField.setText(LocalDate.now().toString());

        addField(card, amountField, gbc, 0);
        addField(card, dateField, gbc, 1);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(-5, 0, 15, 0);

        JLabel dateHint = new JLabel(
            "Date format: YYYY-MM-DD"
        );

        dateHint.setFont(
            new Font("Segoe UI", Font.PLAIN, 11)
        );

        dateHint.setForeground(muted);

        card.add(dateHint, gbc);

        // BUTTONS

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 0, 0, 0);

        JPanel buttons = new JPanel(
            new GridLayout(1, 2, 15, 0)
        );

        buttons.setBackground(Color.WHITE);

        JButton saveButton = createButton(
            "Save Expense",
            blue,
            Color.WHITE
        );

        JButton clearButton = createButton(
            "Clear Fields",
            new Color(235, 239, 247),
            navy
        );

        saveButton.addActionListener(
            e -> saveExpense()
        );

        clearButton.addActionListener(
            e -> clearFields()
        );

        buttons.add(saveButton);
        buttons.add(clearButton);

        card.add(buttons, gbc);

        outer.add(card);

        return outer;
    }

    // LABEL

    private void addLabel(
        JPanel panel,
        String text,
        GridBagConstraints gbc,
        int column
    ) {

        gbc.gridx = column;

        JLabel label = new JLabel(text);

        label.setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );

        label.setForeground(textColor);

        panel.add(label, gbc);
    }

    // INPUT FIELD

    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        field.setPreferredSize(
            new Dimension(200, 44)
        );

        field.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1, true),
                new EmptyBorder(8, 10, 8, 10)
            )
        );

        return field;
    }

    // ADD FIELD

    private void addField(
        JPanel panel,
        JComponent field,
        GridBagConstraints gbc,
        int column
    ) {

        gbc.gridx = column;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(field, gbc);
    }

    // BUTTON DESIGN

    private JButton createButton(
        String text,
        Color backgroundColor,
        Color foregroundColor
    ) {

        JButton button = new JButton(text);

        button.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);

        button.setFocusPainted(false);

        button.setBorder(
            new EmptyBorder(14, 15, 14, 15)
        );

        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    // FOOTER

    private JPanel createFooter() {

        JPanel footer = new JPanel();

        footer.setBackground(background);

        footer.setBorder(
            new EmptyBorder(5, 10, 18, 10)
        );

        JLabel footerText = new JLabel(
            "Personal Expense Tracker | Manage your money wisely"
        );

        footerText.setFont(
            new Font("Segoe UI", Font.PLAIN, 11)
        );

        footerText.setForeground(muted);

        footer.add(footerText);

        return footer;
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

    // SAVE EXPENSE

    private void saveExpense() {

        String name = nameField.getText().trim();

        String category =
            (String) categoryBox.getSelectedItem();

        String amountText = amountField.getText().trim();

        String dateText = dateField.getText().trim();

        if (name.isEmpty()) {

            showError("Please enter an expense name.");
            nameField.requestFocus();
            return;
        }

        double amount;

        try {

            amount = Double.parseDouble(amountText);

            if (!Double.isFinite(amount) || amount <= 0) {

                showError(
                    "Amount must be a positive valid number."
                );

                amountField.requestFocus();
                return;
            }

        } catch (NumberFormatException e) {

            showError("Please enter a valid amount.");
            amountField.requestFocus();
            return;
        }

        LocalDate date;

        try {

            date = LocalDate.parse(dateText);

        } catch (DateTimeParseException e) {

            showError(
                "Please enter a valid date in YYYY-MM-DD format."
            );

            dateField.requestFocus();
            return;
        }

        Expense expense = new Expense(
            name,
            category,
            amount,
            date
        );

        try {

            boolean saved = ExpenseDAO.addExpense(expense);

            if (saved) {

                ExpenseService.addExpense(expense);

                JOptionPane.showMessageDialog(
                    this,
                    "Expense saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();

            } else {

                showError(
                    "Could not save expense to database."
                );
            }

        } catch (Exception e) {

            showError(
                "Database error while saving expense."
            );

            e.printStackTrace();
        }
    }

    // CLEAR FIELDS

    private void clearFields() {

        nameField.setText("");
        amountField.setText("");

        dateField.setText(
            LocalDate.now().toString()
        );

        categoryBox.setSelectedIndex(0);

        nameField.requestFocus();
    }

    // ERROR MESSAGE

    private void showError(String message) {

        JOptionPane.showMessageDialog(
            this,
            message,
            "Validation Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
