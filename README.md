# 💰 Accounting Ledger App
 
A Java console application for tracking personal or business financial transactions. Add deposits, record payments, browse your ledger, and run detailed reports — all from a colorful, menu-driven CLI interface.
 
---
 
## 📖 Overview
 
The Accounting Ledger App reads and writes transactions to a local CSV file (`transactions.csv`). Every entry is stored with a date, time, description, vendor, and amount. Deposits are positive; payments are negative. The app color-codes output in the terminal so you can scan your finances at a glance.
 
---
 
## ✨ Features
 
- **Add Deposits** — log incoming funds with date, vendor/source, and description
- **Make Payments** — record outgoing payments (stored as negative amounts)
- **Ledger View** — browse all transactions, deposits only, or payments only
- **Reports** — filter by month-to-date, previous month, year-to-date, previous year, or vendor
- **Custom Search** — multi-filter search by vendor, description, date range, amount range, and transaction type with optional partial matching
- **File Persistence** — all transactions are saved to and loaded from `transactions.csv`.
- **Color-coded output** — green for deposits, red for payments, cyan/yellow for metadata
---
 
## 🗂️ Project Structure
 
```
accounting-ledger-app/
├── src/
│   └── com/pluralsight/
│       ├── AccountingApp.java   # Main class — all screens, menus, file I/O, and search logic
│       └── Transaction.java     # Transaction model — date, time, description, vendor, amount
├── transactions.csv             # Persistent transaction log (pipe-delimited)
└── README.md
```
 
---
 
## 🚀 Getting Started
 
### Prerequisites
 
- Java 17 or higher
- IntelliJ IDEA (recommended)
- Git
### Installation
 
```bash
# Clone the repository
git clone https://github.com/Estb1296/tally-flow
 
# Open in IntelliJ IDEA
# File > Open > select the project folder
 
# Run the application
# Right-click AccountingApp.java > Run 'AccountingApp.main()'
```
 
> On first launch, the app automatically loads `transactions.csv` from the project root. If the file is empty or missing, you'll be prompted to load a different file or start fresh.
 
---
 
## 🖥️ Menu System
 
### Home Screen
```
(1) Add Deposit
(2) Make Payment
(3) Ledger
(4) Exit
```
 
### Ledger Screen
```
(1) ALL
(2) Deposits
(3) Payments
(4) Reports
(5) Back
```
 
### Reports Screen
```
(1) Month To Date
(2) Previous Month
(3) Year To Date
(4) Previous Year
(5) Search By Vendor
(6) Custom Search
(7) Back
```
 
---
 
## 📄 Transaction File Format
 
Transactions are stored in `transactions.csv` using a pipe-delimited format:
 
```
yyyy-MM-dd|HH:mm:ss|description|vendor|amount
```
 
**Example entries:**
```
2026-04-15|09:32:11|Direct Deposit|Employer Inc|2500.00
2026-04-17|14:05:43|Electric Bill|Power Company|-120.50
2026-04-20|10:00:00|Freelance Payment|Client LLC|800.00
```
 
Deposits have positive amounts; payments have negative amounts.
 
---
 
## 🔍 Custom Search Filters
 
The custom search lets you combine any of the following filters:
 
| Filter | Description |
|---|---|
| Vendor | Exact or partial name match |
| Description | Exact or partial text match |
| Date Range | Start date to end date (yyyy-MM-dd) |
| Type | `deposit`, `payment`, or `all` |
| Amount Range | Min and max dollar amount (uses absolute value) |
| Partial Match | Toggle substring matching on/off |
 
---
 
## 🧱 Key Classes
 
### `AccountingApp.java`
The main class handles all screens, user input, file I/O, and business logic.
 
| Method | Purpose |
|---|---|
| `runHomeScreen()` | Main menu loop |
| `handleNewDeposit()` | Prompts user and saves deposit |
| `handleNewPayment()` | Prompts user and saves payment |
| `displayAllEntriesScreen()` | Shows full ledger with totals |
| `depositsScreen()` / `paymentsScreen()` | Filtered ledger views |
| `monthToDate()` / `yearToDate()` | Date-range report methods |
| `previousMonthSearch()` / `previousYearSearch()` | Historical report methods |
| `searchByVendor()` | Simple vendor search |
| `customSearch()` | Multi-filter advanced search |
| `readTransactionsFromFile()` | Loads transactions from CSV |
| `addDepositToLedger()` / `processPaymentMade()` | Appends new entries to CSV |
| `sortByMostRecent()` | Sorts ledger by date then time descending |
| `printStandaloneTitle()` | Renders a centered box title to the console |
 
### `Transaction.java`
Model representing a single ledger entry.
 
| Method | Purpose |
|---|---|
| `isDeposit()` | Returns true if amount > 0 |
| `isPayment()` | Returns true if amount < 0 |
| `isMonthToDate()` | True if transaction is in the current month up to today |
| `isYearToDate()` | True if transaction is in the current year up to today |
| `isFromPreviousMonth()` | True if transaction is from last calendar month |
| `isFromPreviousYear()` | True if transaction is from last calendar year |
| `total()` | Returns the transaction amount |
 
---
 
## 🎨 Console Color Coding
 
| Color | Meaning |
|---|---|
| 🟢 Green | Deposits / positive totals |
| 🔴 Red | Payments / negative totals |
| 🔵 Cyan | Date and time fields |
| 🟡 Yellow | Description and vendor fields |
 
---
 
## 👤 Author
 
**Ezra Teshale**
Year Up United | Java Focus LTCA — Spring 2026
Instructor: Craig McKeachie
 
---
 
## 📝 License
 
This project was created for educational purposes as part of the Year Up United software training program.
