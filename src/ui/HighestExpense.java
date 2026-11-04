
package ui;

import dao.ExpenseDAO;
import model.Expense;

import javax.swing.*;
import java.awt.*;

public class HighestExpense extends JFrame {

    private JLabel nameLabel;
    private JLabel categoryLabel;
    private JLabel amountLabel;
    private JLabel dateLabel;
    private JLabel statusLabel;

    private final Color NAVY = new Color(25, 42, 86);
    private final Color BLUE = new Color(59, 130, 246);
    private final Color BACKGROUND = new Color(245, 247, 251);
    private final Color TEXT = new Color(45, 55, 72);
    private final Color MUTED = new Color(120, 130, 150);

    public HighestExpense() {

        setTitle("Highest Expense");
        setSize(1100, 750);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(23, 28, 23, 28));

        JPanel headingPanel = new JPanel();
        headingPanel.setOpaque(false);
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Highest Expense");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "Discover your largest recorded transaction"
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
            BorderFactory.createEmptyBorder(35, 50, 35, 50)
        );

        // AMOUNT CARD
        JPanel amountCard = new JPanel();
        amountCard.setLayout(new BoxLayout(amountCard, BoxLayout.Y_AXIS));
        amountCard.setBackground(Color.WHITE);
        amountCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        amountCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 234, 242)),
            BorderFactory.createEmptyBorder(30, 25, 30, 25)
        ));

        JLabel amountHeading = new JLabel("HIGHEST EXPENSE AMOUNT");
        amountHeading.setFont(new Font("Segoe UI", Font.BOLD, 12));
        amountHeading.setForeground(MUTED);
        amountHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        amountLabel = new JLabel("₹0.00");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        amountLabel.setForeground(BLUE);
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel("Your largest recorded expense");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(MUTED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        amountCard.add(amountHeading);
        amountCard.add(Box.createVerticalStrut(15));
        amountCard.add(amountLabel);
        amountCard.add(Box.createVerticalStrut(10));
        amountCard.add(statusLabel);

        mainPanel.add(amountCard);
        mainPanel.add(Box.createVerticalStrut(25));

        // DETAILS CARD
        JPanel detailsCard = new JPanel(new BorderLayout());
        detailsCard.setBackground(Color.WHITE);
        detailsCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailsCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 234, 242)),
            BorderFactory.createEmptyBorder(25, 28, 25, 28)
        ));

        JLabel detailsHeading = new JLabel("Expense Details");
        detailsHeading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        detailsHeading.setForeground(NAVY);

        nameLabel = createDetailLabel("Expense Name", "-");
        categoryLabel = createDetailLabel("Category", "-");
        dateLabel = createDetailLabel("Date", "-");

        JPanel detailWrapper = new JPanel();
        detailWrapper.setOpaque(false);
        detailWrapper.setLayout(new BoxLayout(detailWrapper, BoxLayout.Y_AXIS));

        detailWrapper.add(detailsHeading);
        detailWrapper.add(Box.createVerticalStrut(22));
        detailWrapper.add(nameLabel);
        detailWrapper.add(Box.createVerticalStrut(18));
        detailWrapper.add(categoryLabel);
        detailWrapper.add(Box.createVerticalStrut(18));
        detailWrapper.add(dateLabel);

        detailsCard.add(detailWrapper, BorderLayout.CENTER);

        mainPanel.add(detailsCard);
        mainPanel.add(Box.createVerticalStrut(25));

        // REFRESH BUTTON
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton refreshButton = new JButton("↻  Refresh Details");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(BLUE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setPreferredSize(new Dimension(190, 44));

        refreshButton.addActionListener(e -> loadHighestExpense());

        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);

        loadHighestExpense();

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

    private JLabel createDetailLabel(String heading, String value) {

        JLabel label = new JLabel(heading + ": " + value);

        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(TEXT);

        return label;
    }

    private void loadHighestExpense() {

        try {

            Expense highestExpense = ExpenseDAO.getHighestExpense();

            if (highestExpense == null) {

                nameLabel.setText("Expense Name: -");
                categoryLabel.setText("Category: -");
                amountLabel.setText("₹0.00");
                dateLabel.setText("Date: -");
                statusLabel.setText(
                    "No expenses have been recorded yet."
                );

                return;
            }

            nameLabel.setText(
                "Expense Name: " + highestExpense.getExpenseName()
            );

            categoryLabel.setText(
                "Category: " + highestExpense.getCategory()
            );

            amountLabel.setText(
                String.format("₹%.2f", highestExpense.getAmount())
            );

            dateLabel.setText(
                "Date: " + highestExpense.getExpenseDate()
            );

            statusLabel.setText("Your largest recorded expense");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Unable to retrieve the highest expense.\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
