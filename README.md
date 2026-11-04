# TrackMyCash – Personal Expense Tracker

A desktop-based Personal Expense Tracker developed using Java, Java Swing, MySQL, and JFreeChart. Track daily expenses, manage monthly budgets, and visualize spending patterns through interactive charts.

## Features

- **Add Expense:** Record expenses with details and validation.
- **Expense History:** View, refresh, and delete recorded expenses.
- **Monthly Spending:** Analyze expenses by month and year.
- **Highest Expense:** Identify your highest recorded expense.
- **Monthly Budget:** Set monthly budgets and monitor spending progress.
- **Dashboard:** View expense summaries, bar charts, and pie charts.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java (JDK 21) | Core programming |
| Java Swing | Desktop user interface |
| MySQL 8 | Database |
| JDBC | Database connectivity |
| JFreeChart 1.5.6 | Data visualization |
| MySQL Connector/J | MySQL connectivity |
| VS Code | Development environment |

## Project Structure

```text
Personal Financial Tracker/
│
├── src/
│   ├── app/
│   ├── dao/
│   ├── model/
│   ├── Service/
│   └── ui/
│
├── lib/
│   ├── jfreechart-1.5.6.jar
│   └── mysql-connector-j-26.10.0.jar
│
├── .gitignore
└── README.md
```

## Database Setup

1. Install MySQL Server 8.0.
2. Create a database named `personal_expense_tracker`.
3. Create the tables required by the application.
4. Configure your database username and password.

Set your database password in the `DB_PASSWORD` environment variable. Never hardcode or upload database credentials.

## How to Run

1. Install JDK 21.
2. Clone this repository:

```bash
git clone https://github.com/tarshamounic/TrackMyCash.git
```

3. Open the project folder in VS Code.
4. Set the `DB_PASSWORD` environment variable in your terminal.
5. Compile the project in PowerShell:

```powershell
javac -cp "lib/*" -d out src/model/Expense.java src/dao/*.java src/Service/*.java src/ui/*.java src/app/Main.java
```

6. Run the application:

```powershell
java -cp "out;lib/*" app.Main
```



## Author

TrackMyCash was developed as a personal project to demonstrate Java programming, database connectivity, desktop application development, and data visualization.

GitHub: @tarshamounic
