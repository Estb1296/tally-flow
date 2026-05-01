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
## 🧩 Code Walkthrough
 
Here are five key methods from the custom search feature, each explained with the actual code.
 
---
 
### 1. Partial Match Toggle — `isUsePartialMatch()`
 
This method asks the user whether they want flexible (partial) or strict (exact) matching when searching. It loops until a valid `yes` or `no` is entered, so the program never crashes from bad input.
 
```java
private static boolean isUsePartialMatch() {
    System.out.println("Do you want partial matching? (yes/no)");
    String choice = input.nextLine().trim();
    while (!(choice.equalsIgnoreCase("yes") || choice.equalsIgnoreCase("no"))) {
        System.out.println("Please enter a valid input.(Yes/NO)");
        choice = input.nextLine();
    }
    return choice.equalsIgnoreCase("yes");
}
```
 
**How it works:** The `while` loop keeps prompting until the user types `yes` or `no` (case-insensitive). It returns `true` for partial matching and `false` for exact matching. This boolean gets passed down to the filter methods below.
 
---
 
### 2. Vendor Filter — `vendorFilterMatches()`
 
This method checks whether a transaction's vendor matches the user's search term. It supports both partial and exact matching depending on the user's earlier choice.
 
```java
private static boolean vendorFilterMatches(String vendorFilter, Transaction transaction, boolean usePartialMatch) {
    if (vendorFilter.isEmpty()) {
        return true;
    }
 
    if (usePartialMatch) {
        boolean searchContainsVendor = vendorFilter.toLowerCase()
                .contains(transaction.getVendor().toLowerCase());
        boolean vendorContainsSearch = transaction.getVendor().toLowerCase()
                .contains(vendorFilter.toLowerCase());
        return searchContainsVendor || vendorContainsSearch;
    } else {
        return transaction.getVendor().trim().equalsIgnoreCase(vendorFilter.trim());
    }
}
```
 
**How it works:** If the filter is blank, every transaction passes (no filter applied). With partial matching on, it checks both directions — does the search term contain the vendor, or does the vendor contain the search term? This means typing `"Ama"` will still find `"Amazon"`. With partial matching off, it requires an exact case-insensitive match.
 
---
 
### 3. Date Range Filter — `dateFilterMatches()`
 
This method checks whether a transaction falls within a user-specified start and end date. If no date range was entered, every transaction passes automatically.
 
```java
private static boolean dateFilterMatches(Transaction transaction, LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null) {
        return true;
    }
 
    return !transaction.getDate().isBefore(startDate) && !transaction.getDate().isAfter(endDate);
}
```
 
**How it works:** The `null` check lets users skip the date filter entirely. When a range is given, it uses Java's `LocalDate` comparison methods to confirm the transaction date is on or after the start date AND on or before the end date — both conditions must be true for the transaction to match.
 
---
 
### 4. Amount Range Filter — `amountFilterMatches()`
 
This method checks whether a transaction's dollar amount falls within a min/max range. It uses `Math.abs()` so that payments (stored as negative numbers) are compared fairly alongside deposits.
 
```java
private static boolean amountFilterMatches(Transaction transaction, double minAmount, double maxAmount) {
    double absoluteAmount = Math.abs(transaction.getAmount());
    return absoluteAmount >= minAmount && absoluteAmount <= maxAmount;
}
```
 
**How it works:** Since payments are stored as negative values (e.g., `-120.50`), comparing them directly to a positive min/max range would always fail. `Math.abs()` strips the sign so a `-$120.50` payment and a `$120.50` deposit are both treated as `$120.50` for filtering purposes.
 
---
 
### 5. Custom Search Orchestrator — `customSearch()`
 
This is the method that ties everything together. It collects all the user's filter choices, then loops through every transaction and applies all five filters at once.
 
```java
public static void customSearch(ArrayList<Transaction> ledger) {
    sortByMostRecent(ledger);
    input.nextLine(); // clean buffer
 
    Object[] filters = gatherSearchFilters();
    String vendorFilter = (String) filters[0];
    String descriptionFilter = (String) filters[1];
    String typeFilter = (String) filters[2];
    LocalDate startDate = (LocalDate) filters[3];
    LocalDate endDate = (LocalDate) filters[4];
    double minAmount = (double) filters[5];
    double maxAmount = (double) filters[6];
    boolean usePartialMatch = (boolean) filters[7];
 
    double totalAmount = 0;
    int resultCount = 0;
 
    printStandaloneTitle("Transactions", 96, 146);
 
    for (Transaction transaction : ledger) {
        boolean matches = vendorFilterMatches(vendorFilter, transaction, usePartialMatch)
                && descriptionFilterMatches(descriptionFilter, transaction, usePartialMatch)
                && dateFilterMatches(transaction, startDate, endDate)
                && typeFilterMatches(typeFilter, transaction)
                && amountFilterMatches(transaction, minAmount, maxAmount);
 
        if (matches) {
            String look = getLook(transaction);
            System.out.println(look);
            totalAmount += transaction.getAmount();
            resultCount++;
        }
    }
 
    displaySearchResults(totalAmount, resultCount);
}
```
 
**How it works:** First, the ledger is sorted newest-first. Then `gatherSearchFilters()` collects all the user's criteria and returns them packed into an `Object[]` array, which gets unpacked into typed variables. The `for` loop then runs every transaction through all five filter methods chained with `&&` — a transaction only prints if it passes every single filter. At the end, `displaySearchResults()` prints how many matches were found and their combined dollar total.
 
---
 
## 👤 Author

**Ezra Teshale**
Year Up United | Java Focus LTCA — Spring 2026

Instructor: Craig McKeachie

Email : Ezra.Teshale12@gmail.com
 
---
 
## 📝 License
 
This project was created for educational purposes as part of the Year Up United software training program.
