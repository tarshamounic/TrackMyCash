
package ui;

import dao.ExpenseDAO;
import dao.BudgetDAO;
import model.Expense;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Locale;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class Dashboard extends JFrame {

    // EXISTING DASHBOARD INSTANCE
    private static Dashboard existingDashboard;

    public static Dashboard getExistingDashboard() {
        return existingDashboard;
    }

    public void refreshForNavigation() {
        refreshDashboard();
    }

    private final Color navy = new Color(25, 42, 86);
    private final Color blue = new Color(55, 115, 230);
    private final Color background = new Color(245, 247, 251);
    private final Color textColor = new Color(45, 55, 75);
    private final Color muted = new Color(120, 130, 150);
    private final Color borderColor = new Color(225, 230, 240);

    private JLabel totalValue;
    private JLabel monthlyValue;
    private JLabel budgetValue;
    private JLabel remainingValue;

    private DefaultCategoryDataset monthlyDataset;
    private DefaultPieDataset<String> categoryDataset;
    private PiePlot<?> categoryPlot;

    public Dashboard() {

        existingDashboard = this;

        setTitle("Personal Expense Tracker");

        setSize(1200, 850);
        setMinimumSize(new Dimension(950, 650));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(background);

        add(createSidebar(), BorderLayout.WEST);
        add(createMainContent(), BorderLayout.CENTER);

        refreshDashboard();

        setVisible(true);
    }

    // SIDEBAR

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setBackground(navy);
        sidebar.setPreferredSize(new Dimension(215, 750));

        sidebar.setLayout(
            new BoxLayout(sidebar, BoxLayout.Y_AXIS)
        );

        sidebar.setBorder(
            new EmptyBorder(30, 15, 25, 15)
        );

        JLabel appTitle = new JLabel("EXPENSE TRACKER");

        appTitle.setForeground(Color.WHITE);
        appTitle.setFont(
            new Font("Segoe UI", Font.BOLD, 18)
        );

        appTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Personal Finance Manager");

        subtitle.setForeground(new Color(190, 200, 225));
        subtitle.setFont(
            new Font("Segoe UI", Font.PLAIN, 11)
        );

        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(appTitle);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(40));

        addNavButton(sidebar, "Dashboard", null);

        addNavButton(
            sidebar,
            "Add Expense",
            () -> openFeature(new AddExpense())
        );

        addNavButton(
            sidebar,
            "Expense History",
            () -> openFeature(new ExpenseHistory())
        );

        addNavButton(
            sidebar,
            "Monthly Spending",
            () -> openFeature(new MonthlySpending())
        );

        addNavButton(
            sidebar,
            "Highest Expense",
            () -> openFeature(new HighestExpense())
        );

        addNavButton(
            sidebar,
            "Monthly Budget",
            () -> openFeature(new MonthlyBudget())
        );

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("Manage your money wisely");

        footer.setForeground(new Color(190, 200, 225));
        footer.setFont(
            new Font("Segoe UI", Font.ITALIC, 11)
        );

        sidebar.add(footer);

        return sidebar;
    }

    // OPEN FEATURE AND HIDE DASHBOARD

    private void openFeature(JFrame feature) {

        setVisible(false);

        feature.setDefaultCloseOperation(
            JFrame.DISPOSE_ON_CLOSE
        );

        feature.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // RESTORE DASHBOARD WHEN FEATURE WINDOW IS CLOSED
        feature.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {

                // Avoid refreshing twice when Back was clicked.
                if (!Dashboard.this.isVisible()) {
                    returnToDashboard();
                }
            }
        });

        feature.setVisible(true);
    }

    // RETURN TO EXISTING DASHBOARD

    private void returnToDashboard() {

        refreshForNavigation();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        toFront();
    }

    // NAVIGATION BUTTONS

    private void addNavButton(
        JPanel sidebar,
        String name,
        Runnable action
    ) {

        JButton button = new JButton(name);

        button.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(navy);

        button.setHorizontalAlignment(
            SwingConstants.LEFT
        );

        button.setBorder(
            new EmptyBorder(13, 12, 13, 8)
        );

        button.setFocusPainted(false);

        button.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 46)
        );

        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        if (action == null) {

            button.setBackground(blue);

        } else {

            button.addActionListener(e -> action.run());
        }

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(8));
    }

    // MAIN CONTENT

    private JPanel createMainContent() {

        JPanel main = new JPanel(new BorderLayout(15, 15));

        main.setBackground(background);

        main.setBorder(
            new EmptyBorder(25, 25, 25, 25)
        );

        // HEADER

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(background);

        JPanel headingPanel = new JPanel();

        headingPanel.setLayout(
            new BoxLayout(headingPanel, BoxLayout.Y_AXIS)
        );

        headingPanel.setBackground(background);

        JLabel heading = new JLabel("Dashboard");

        heading.setFont(
            new Font("Segoe UI", Font.BOLD, 28)
        );

        heading.setForeground(textColor);

        JLabel welcome = new JLabel(
            "Welcome back! Here's your financial overview."
        );

        welcome.setFont(
            new Font("Segoe UI", Font.PLAIN, 13)
        );

        welcome.setForeground(muted);

        headingPanel.add(heading);
        headingPanel.add(Box.createVerticalStrut(5));
        headingPanel.add(welcome);

        // REFRESH BUTTON

        JButton refreshButton = new JButton("Refresh");

        refreshButton.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(blue);

        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setOpaque(true);

        refreshButton.setPreferredSize(
            new Dimension(125, 42)
        );

        refreshButton.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        refreshButton.addActionListener(
            e -> refreshDashboard()
        );

        refreshButton.addMouseListener(
            new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    refreshButton.setBackground(
                        new Color(35, 90, 200)
                    );
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    refreshButton.setBackground(blue);
                }
            }
        );

        header.add(headingPanel, BorderLayout.CENTER);
        header.add(refreshButton, BorderLayout.EAST);

        main.add(header, BorderLayout.NORTH);

        // CENTER CONTENT

        JPanel center = new JPanel();

        center.setBackground(background);

        center.setLayout(
            new BoxLayout(center, BoxLayout.Y_AXIS)
        );

        center.setBorder(
            new EmptyBorder(10, 0, 15, 0)
        );

        // SUMMARY CARDS

        JPanel cards = new JPanel(
            new GridLayout(1, 4, 12, 0)
        );

        cards.setBackground(background);
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);

        totalValue = new JLabel("Loading...");
        monthlyValue = new JLabel("Loading...");
        budgetValue = new JLabel("Loading...");
        remainingValue = new JLabel("Loading...");

        cards.add(
            createCard(
                "Total Expenses",
                totalValue,
                "All saved expenses"
            )
        );

        cards.add(
            createCard(
                "This Month",
                monthlyValue,
                "Current month spending"
            )
        );

        cards.add(
            createCard(
                "Monthly Budget",
                budgetValue,
                "Current month budget"
            )
        );

        cards.add(
            createCard(
                "Remaining Budget",
                remainingValue,
                "Budget minus spending"
            )
        );

        cards.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 145)
        );

        center.add(cards);
        center.add(Box.createVerticalStrut(25));

        // MONTHLY CHART

        center.add(
            createSectionHeading("Monthly Spending Overview")
        );

        center.add(Box.createVerticalStrut(10));

        JPanel monthlyChart = createMonthlyChart();

        monthlyChart.setAlignmentX(Component.LEFT_ALIGNMENT);

        monthlyChart.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 300)
        );

        center.add(monthlyChart);
        center.add(Box.createVerticalStrut(25));

        // CATEGORY PIE CHART

        center.add(
            createSectionHeading("Category-wise Expense Breakdown")
        );

        center.add(Box.createVerticalStrut(10));

        JPanel pieChart = createCategoryPieChart();

        pieChart.setAlignmentX(Component.LEFT_ALIGNMENT);

        pieChart.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 300)
        );

        center.add(pieChart);

        // SCROLL PANE

        JScrollPane scrollPane = new JScrollPane(center);

        scrollPane.setBorder(null);
        scrollPane.setBackground(background);

        scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.getViewport().setBackground(background);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        main.add(scrollPane, BorderLayout.CENTER);

        return main;
    }

    // SECTION HEADING

    private JLabel createSectionHeading(String title) {

        JLabel label = new JLabel(title);

        label.setFont(
            new Font("Segoe UI", Font.BOLD, 19)
        );

        label.setForeground(textColor);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    // SUMMARY CARDS

    private JPanel createCard(
        String title,
        JLabel value,
        String description
    ) {

        JPanel card = new JPanel();

        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(15, 10, 15, 10)
            )
        );

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(
            new Font("Segoe UI", Font.PLAIN, 12)
        );

        titleLabel.setForeground(muted);

        value.setFont(
            new Font("Segoe UI", Font.BOLD, 17)
        );

        value.setForeground(blue);

        JLabel descriptionLabel = new JLabel(description);

        descriptionLabel.setFont(
            new Font("Segoe UI", Font.PLAIN, 10)
        );

        descriptionLabel.setForeground(
            new Color(145, 150, 165)
        );

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(value);
        card.add(Box.createVerticalStrut(8));
        card.add(descriptionLabel);

        return card;
    }

    // MONTHLY BAR CHART

    private JPanel createMonthlyChart() {

        monthlyDataset = new DefaultCategoryDataset();

        JFreeChart chart = ChartFactory.createBarChart(
            "Monthly Spending - Last 6 Months",
            "Month",
            "Expense Amount (INR)",
            monthlyDataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );

        chart.setBackgroundPaint(Color.WHITE);

        chart.getTitle().setFont(
            new Font("Segoe UI", Font.BOLD, 17)
        );

        CategoryPlot plot = chart.getCategoryPlot();

        plot.setBackgroundPaint(Color.WHITE);

        plot.setRangeGridlinePaint(
            new Color(225, 230, 240)
        );

        plot.getDomainAxis().setLabelFont(
            new Font("Segoe UI", Font.BOLD, 12)
        );

        plot.getRangeAxis().setLabelFont(
            new Font("Segoe UI", Font.BOLD, 12)
        );

        BarRenderer renderer =
            (BarRenderer) plot.getRenderer();

        renderer.setSeriesPaint(0, blue);

        renderer.setDefaultToolTipGenerator(
            new org.jfree.chart.labels.StandardCategoryToolTipGenerator(
                "{1}: ₹ {2}",
                java.text.NumberFormat.getNumberInstance(Locale.US)
            )
        );

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(
            new Dimension(700, 260)
        );

        chartPanel.setMinimumSize(
            new Dimension(300, 220)
        );

        return createChartContainer(chartPanel);
    }

    // CATEGORY PIE CHART

    private JPanel createCategoryPieChart() {

        categoryDataset = new DefaultPieDataset<>();

        JFreeChart chart = ChartFactory.createPieChart(
            "Expense Distribution by Category",
            categoryDataset,
            true,
            true,
            false
        );

        chart.setBackgroundPaint(Color.WHITE);

        chart.getTitle().setFont(
            new Font("Segoe UI", Font.BOLD, 17)
        );

        categoryPlot = (PiePlot<?>) chart.getPlot();

        categoryPlot.setBackgroundPaint(Color.WHITE);

        categoryPlot.setNoDataMessage(
            "No expense data available"
        );

        java.text.NumberFormat currencyFormat =
            java.text.NumberFormat.getCurrencyInstance(
                new Locale("en", "IN")
            );

        java.text.NumberFormat percentFormat =
            java.text.NumberFormat.getPercentInstance();

        percentFormat.setMaximumFractionDigits(1);

        categoryPlot.setLabelGenerator(
            new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})",
                currencyFormat,
                percentFormat
            )
        );

        categoryPlot.setLabelFont(
            new Font("Segoe UI", Font.PLAIN, 11)
        );

        categoryPlot.setLabelBackgroundPaint(Color.WHITE);
        categoryPlot.setLabelOutlinePaint(null);
        categoryPlot.setLabelShadowPaint(null);

        categoryPlot.setInteriorGap(0.05);

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(
            new Dimension(700, 260)
        );

        chartPanel.setMinimumSize(
            new Dimension(300, 220)
        );

        return createChartContainer(chartPanel);
    }

    // CHART CONTAINER

    private JPanel createChartContainer(ChartPanel chartPanel) {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(Color.WHITE);

        panel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(3, 3, 3, 3)
            )
        );

        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    // REFRESH DASHBOARD DATA

    private void refreshDashboard() {

        try {

            List<Expense> expenses =
                ExpenseDAO.getAllExpenses();

            double total = 0;

            for (Expense expense : expenses) {
                total += expense.getAmount();
            }

            LocalDate today = LocalDate.now();

            int month = today.getMonthValue();
            int year = today.getYear();

            double monthlyTotal =
                ExpenseDAO.getMonthlyTotal(month, year);

            double budget =
                BudgetDAO.getBudget(month, year);

            double remaining = budget - monthlyTotal;

            totalValue.setText(formatAmount(total));

            monthlyValue.setText(
                formatAmount(monthlyTotal)
            );

            budgetValue.setText(
                formatAmount(budget)
            );

            if (budget <= 0) {

                remainingValue.setText("Not set");
                remainingValue.setForeground(blue);

            } else {

                remainingValue.setText(
                    formatAmount(remaining)
                );

                if (remaining < 0) {

                    remainingValue.setForeground(
                        new Color(210, 65, 65)
                    );

                } else {

                    remainingValue.setForeground(
                        new Color(35, 155, 100)
                    );
                }
            }

            // UPDATE BAR CHART

            Map<String, Double> monthlyData =
                ExpenseDAO.getLastSixMonthsSpending();

            monthlyDataset.clear();

            for (Map.Entry<String, Double> entry :
                    monthlyData.entrySet()) {

                monthlyDataset.addValue(
                    entry.getValue(),
                    "Expenses",
                    entry.getKey()
                );
            }

            // UPDATE PIE CHART

            Map<String, Double> categoryData =
                ExpenseDAO.getCategoryWiseSpending();

            categoryDataset.clear();

            Color[] categoryColors = {

                new Color(55, 115, 230),
                new Color(40, 170, 150),
                new Color(255, 175, 75),
                new Color(150, 110, 220),
                new Color(235, 105, 125),
                new Color(90, 150, 190),
                new Color(130, 155, 100)
            };

            int colorIndex = 0;

            for (Map.Entry<String, Double> entry :
                    categoryData.entrySet()) {

                String category = entry.getKey();

                double amount = entry.getValue();

                categoryDataset.setValue(
                    category,
                    amount
                );

                categoryPlot.setSectionPaint(
                    category,
                    categoryColors[
                        colorIndex % categoryColors.length
                    ]
                );

                colorIndex++;
            }

        } catch (Exception e) {

            totalValue.setText("Unavailable");
            monthlyValue.setText("Unavailable");
            budgetValue.setText("Unavailable");
            remainingValue.setText("Unavailable");

            JOptionPane.showMessageDialog(
                this,
                "Unable to load dashboard data.\n"
                    + "Please check your database connection.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // CURRENCY FORMATTING

    private String formatAmount(double amount) {

        return String.format(
            Locale.US,
            "₹ %.2f",
            amount
        );
    }
}
