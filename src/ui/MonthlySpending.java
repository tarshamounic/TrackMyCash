
package ui;

import dao.ExpenseDAO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MonthlySpending extends JFrame {

    private JComboBox<String> monthBox;
    private JTextField yearField;
    private JLabel totalLabel;
    private JLabel countLabel;
    private JLabel resultHeading;

    private final Color NAVY = new Color(25, 42, 86);
    private final Color BLUE = new Color(59, 130, 246);
    private final Color BACKGROUND = new Color(245, 247, 251);
    private final Color TEXT = new Color(45, 55, 72);
    private final Color MUTED = new Color(120, 130, 150);

    private final String[] months = {
        "January", "February", "March",
        "April", "May", "June",
        "July", "August", "September",
        "October", "November", "December"
    };

    public MonthlySpending() {

        setTitle("Monthly Spending");
        setSize(1100, 750);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));

        JLabel title = new JLabel("Monthly Spending");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "Analyze your expenses month by month"
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(210, 220, 240));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        // BACK BUTTON
        JButton backButton = new JButton("← Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(55, 75, 125));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(190, 42));
        backButton.addActionListener(e -> goToDashboard());

        header.add(heading, BorderLayout.WEST);
        header.add(backButton, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // MAIN PANEL
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(35, 45, 35, 45)
        );

        // SELECTION CARD
        JPanel selectionCard = new JPanel(new GridBagLayout());
        selectionCard.setBackground(Color.WHITE);
        selectionCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 234, 242)),
            BorderFactory.createEmptyBorder(25, 28, 25, 28)
        ));
        selectionCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel selectionTitle = new JLabel("Select Period");
        selectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        selectionTitle.setForeground(NAVY);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        selectionCard.add(selectionTitle, gbc);

        gbc.gridwidth = 1;

        // MONTH
        JLabel monthLabel = createLabel("Month");

        gbc.gridx = 0;
        gbc.gridy = 1;
        selectionCard.add(monthLabel, gbc);

        monthBox = new JComboBox<>(months);
        monthBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        monthBox.setBackground(Color.WHITE);
        monthBox.setPreferredSize(new Dimension(250, 40));
        monthBox.setSelectedIndex(
            LocalDate.now().getMonthValue() - 1
        );

        gbc.gridx = 1;
        selectionCard.add(monthBox, gbc);

        // YEAR
        JLabel yearLabel = createLabel("Year");

        gbc.gridx = 0;
        gbc.gridy = 2;
        selectionCard.add(yearLabel, gbc);

        yearField = new JTextField(
            String.valueOf(LocalDate.now().getYear())
        );
        yearField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        yearField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 218, 230)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        yearField.setPreferredSize(new Dimension(250, 40));

        gbc.gridx = 1;
        selectionCard.add(yearField, gbc);

        // CALCULATE BUTTON
        JButton calculateButton = createButton(
            "Calculate Spending", BLUE
        );

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(18, 8, 5, 8);

        selectionCard.add(calculateButton, gbc);

        mainPanel.add(selectionCard);
        mainPanel.add(Box.createVerticalStrut(25));

        // RESULTS HEADING
        resultHeading = new JLabel("Monthly Summary");
        resultHeading.setFont(new Font("Segoe UI", Font.BOLD, 19));
        resultHeading.setForeground(NAVY);
        resultHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(resultHeading);
        mainPanel.add(Box.createVerticalStrut(15));

        // RESULTS CARDS
        JPanel resultPanel = new JPanel(new GridLayout(1, 2, 18, 0));
        resultPanel.setOpaque(false);
        resultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultPanel.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 135)
        );

        JPanel totalCard = createResultCard(
            "TOTAL SPENDING", "₹0.00"
        );

        JPanel countCard = createResultCard(
            "NUMBER OF EXPENSES", "0"
        );

        totalLabel = (JLabel) totalCard.getClientProperty("valueLabel");
        countLabel = (JLabel) countCard.getClientProperty("valueLabel");

        resultPanel.add(totalCard);
        resultPanel.add(countCard);

        mainPanel.add(resultPanel);

        add(mainPanel, BorderLayout.CENTER);

        // ACTIONS
        calculateButton.addActionListener(e -> calculateSpending());
        yearField.addActionListener(e -> calculateSpending());

        // AUTOMATICALLY CALCULATE CURRENT MONTH
        calculateSpending();

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

    private JPanel createResultCard(String heading, String value) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 234, 242)),
            BorderFactory.createEmptyBorder(20, 22, 20, 22)
        ));

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        headingLabel.setForeground(MUTED);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        valueLabel.setForeground(NAVY);

        card.add(headingLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(valueLabel);

        card.putClientProperty("valueLabel", valueLabel);

        return card;
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 44));

        return button;
    }

    private void calculateSpending() {

        try {

            int month = monthBox.getSelectedIndex() + 1;
            int year = Integer.parseInt(yearField.getText().trim());

            if (year < 1 || year > 9999) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid year between 1 and 9999.",
                    "Invalid Year",
                    JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            double total = ExpenseDAO.getMonthlyTotal(month, year);
            int count = ExpenseDAO.getMonthlyExpenseCount(month, year);

            totalLabel.setText(String.format("₹%.2f", total));
            countLabel.setText(String.valueOf(count));

            resultHeading.setText(
                months[month - 1] + " " + year + " Summary"
            );

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
                "Unable to calculate monthly spending.\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
