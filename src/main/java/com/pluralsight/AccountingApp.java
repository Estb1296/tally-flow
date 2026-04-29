package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.String.format;

public class AccountingApp {
    static Scanner input = new Scanner(System.in);
    static ArrayList<Transactions> ledger = new ArrayList<>();
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";


    public static void main(String[] args) {
        System.out.println("Good to go");
        promptAndLoadFileIfEmpty(ledger);
        runHomeScreen(ledger);
    }

    public static void runHomeScreen(ArrayList<Transactions> ledger) {
        System.out.println("Welcome to our humble institution");
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("How can we assist you today? Please pick and enter the numbers associated with each menu item.");
            System.out.println("""
                    (1)AddDepositScreen
                    (2)MakePaymentScreen
                    (3)LedgerScreen
                    (4)ExitApp
                    """);
            int choice = input.nextInt();
            switch (choice) {
                case 1 -> runAddDepositScreen(ledger);
                case 2 -> runMakePaymentScreen(ledger);
                case 3 -> runLedgerScreen(ledger);
                case 4 -> isRunning = exitApp();
                default -> System.out.println("Invalid input");
            }
        }

    }

    private static boolean exitApp() {
        System.out.println("You have a nice day!");
        return false;
    }

    public static void runAddDepositScreen(ArrayList<Transactions> ledger) {
        handleNewDeposit(ledger);
    }

    public static void runMakePaymentScreen(ArrayList<Transactions> ledger) {
        handleNewPayment(ledger);
    }

    public static void runLedgerScreen(ArrayList<Transactions> ledger) {
        System.out.println("The ledger screen is working well so far.");
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("You are accessing your ledger now");
            System.out.println("Pick from the following choices by entering the number beside the choices.");
            System.out.println("""
                    (1)DisplayAllEntriesScreen
                    (2)DepositsScreen
                    (3)PaymentsScreen
                    (4)ReportsScreen
                    (5)Go Back To The HomeScreen
                    """);
            int choice = input.nextInt();
            switch (choice) {
                case 1 -> displayAllEntriesScreen(ledger);
                case 2 -> depositsScreen(ledger);
                case 3 -> paymentsScreen(ledger);
                case 4 -> runReportsScreen(ledger);
                case 5 -> isRunning = false;
                default -> System.out.println("Invalid input please try again.\n");
            }
        }
    }

    public static void runReportsScreen(ArrayList<Transactions> ledger) {
        System.out.println("this is the reports screen");
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("""
                    (1)MonthToDateScreen
                    (2)PreviousMonthScreen
                    (3)YearToDateScreen
                    (4)PreviousYearScreen
                    (5)SearchByVendorScreen
                    (6)CustomSearchScreen (bonus search by all the listed fields at once but they are all optional)
                    (7)goBackToLedger
                    """);
            int choice = input.nextInt();
            switch (choice) {
                case 1 -> monthToDateScreen(ledger);
                case 2 -> previousMonthScreen(ledger);
                case 3 -> yearToDateScreen(ledger);
                case 4 -> previousYearScreen(ledger);
                case 5 -> searchByVendorScreen(ledger);
                case 6 -> customSearchScreen(ledger);
                case 7 -> isRunning = false;
                default -> System.out.println("Invalid Input!");
            }
        }
    }

    public static void readTransactionsFromFile(String filename, ArrayList<Transactions> ledger) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));  // Use filename parameter
            String line;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] transactionRegistry = line.split("\\|");
                LocalDate date = LocalDate.parse(transactionRegistry[0].trim(), dateFormatter);
                LocalTime time = LocalTime.parse(transactionRegistry[1].trim(), timeFormatter);
                String description = transactionRegistry[2].trim();
                String vendor = transactionRegistry[3].trim();
                double amount = Double.parseDouble(transactionRegistry[4].trim());
                ledger.add(new Transactions(date, time, description, vendor, amount));
            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error File not found!");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static void promptAndLoadFileIfEmpty(ArrayList<Transactions> ledger) {
        loadTransactionsFromFile("transactions.csv", ledger);

        if (ledger.isEmpty()) {
            System.out.println("No transactions found in transactions.csv");
            System.out.println("Would you like to load a different file? (yes/no)");
            String choice = input.nextLine().trim();

            if (choice.equalsIgnoreCase("yes")) {
                System.out.println("Enter the filename you want to load:");
                String filename = getValidString("Filename: ");
                loadTransactionsFromFile(filename, ledger);
            } else {
                System.out.println("Starting with empty ledger...\n");
            }
        }
    }

    public static void loadTransactionsFromFile(String filename, ArrayList<Transactions> ledger) {
        readTransactionsFromFile(filename, ledger);
    }

    public static void displayAllEntriesScreen(ArrayList<Transactions> ledger) {
        if (ledger.isEmpty()) {
            System.out.println("No transactions recorded yet.");
            return;
        }
        sortByMostRecent(ledger);

        printStandaloneTitle("All Entries", 96, 146);
        for (Transactions transaction : ledger) {
            String look = getLook(transaction);

            System.out.print(look); // Use print() because %n already adds a newline
        }
    }

    private static String getLook(Transactions transaction) {
        String color = transaction.isDeposit() ? GREEN : RED;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        // This fills the %s at the very end
        return String.format("%sDate: %-12s | Time: %-12s%s | %sDescription: %-25s | Vendor: %-26s%s | Amount: %s$%8.2f%s%n",
                CYAN,                                                 // 1st %s - cyan color
                transaction.getDate().format(dateFormatter),         // 2nd %s - date
                transaction.getTime().format(timeFormatter),        // 3rd %s - time
                RESET,                                             // 4th %s - reset color
                YELLOW,                                           // 5th %s - yellow color
                transaction.getDescription(),                    // 6th %s - description
                transaction.getVendor(),                        // 7th %s - vendor
                RESET,                                         // 8th %s - reset color
                color,                                        // 9th %s - amount color
                transaction.getAmount(),                     // %8.2f - amount (different format)
                RESET);                                     // 10th %s - reset color
    }

    public static void promptUserForDepositInfo(ArrayList<Transactions> ledger) {
        double depositAmount = getValidDeposit("How much do you want to deposit?");
        System.out.printf("%.2f is the amount you have deposited into your account.\n", depositAmount);

        String vendor = getValidString("What is the source of funds of the deposit?");

        String description = getValidString("What is the description of said deposit?");

        LocalDate date = getValidDate("What is the date the deposit was made?(You can press enter to skip)");
        LocalTime time = LocalTime.now();
        ledger.add(new Transactions(date, time, description, vendor, depositAmount));

    }

    public static void addDepositToLedger(ArrayList<Transactions> ledger) {
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));  // true = append
            Transactions lastDeposit = ledger.get(ledger.size() - 1);
            String line = format("%s|%s|%s|%s|%.2f%n",
                    lastDeposit.getDate().format(dateFormatter),
                    lastDeposit.getTime().format(timeFormatter),
                    lastDeposit.getDescription(),
                    lastDeposit.getVendor(),
                    lastDeposit.getAmount());
            writer.write(line);
            writer.close();


            System.out.println("Deposit recorded successfully!");

        } catch (IOException e) {
            System.out.println("Unable to record transaction. Try again later.");
        }
    }

    public static void handleNewDeposit(ArrayList<Transactions> ledger) {
        promptUserForDepositInfo(ledger);
        addDepositToLedger(ledger);
    }

    public static void promptUserForPaymentInfo(ArrayList<Transactions> ledger) {

        double paymentAmount = getValidPayment("How much was the payment?");
        System.out.printf("%.2f is the amount you have paid from your account\n", paymentAmount);
        String vendor = getValidString("What is the recipient of the payment?");
        String description = getValidString("What is the description of said payment?");
        LocalDate date = getValidDate("What is the date the payment was made?(You can press enter to skip)");
        LocalTime time = LocalTime.now();
        ledger.add(new Transactions(date, time, description, vendor, paymentAmount));
    }

    public static void processPaymentMade(ArrayList<Transactions> ledger) {
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));  // true = append
            Transactions lastPayment = ledger.get(ledger.size() - 1);
            String line = format("%s|%s|%s|%s|%.2f%n",
                    lastPayment.getDate().format(dateFormatter),
                    lastPayment.getTime().format(timeFormatter),
                    lastPayment.getDescription(),
                    lastPayment.getVendor(),
                    lastPayment.getAmount());
            writer.write(line);
            writer.close();

            System.out.println("Payment recorded successfully!");

        } catch (IOException e) {
            System.out.println("Unable to record transaction. Try again later.");
        }
    }

    public static void handleNewPayment(ArrayList<Transactions> ledger) {
        promptUserForPaymentInfo(ledger);
        processPaymentMade(ledger);
    }

    public static LocalDate getValidDate(String prompt) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        boolean validDate = false;
        LocalDate date = null;

        while (!validDate) {
            try {
                System.out.println(prompt);
                System.out.println("(Format: yyyy-MM-dd)");
                String userInput = input.nextLine().trim();
                if (userInput.isEmpty()) {
                    date = LocalDate.now();
                } else {
                    date = LocalDate.parse(userInput, dateFormatter);
                }
                validDate = true;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd (example: 2026-04-25)");
            }
        }
        return date;
    }

    public static double getValidDeposit(String prompt) {
        boolean validAmount = false;
        double amount = 0;

        while (!validAmount) {
            try {
                System.out.println(prompt);
                amount = input.nextDouble();

                if (amount <= 0) {
                    System.out.println("Amount must be greater than zero.");
                    input.nextLine();
                    continue;
                }

                validAmount = true;
                input.nextLine();  // Clear buffer

            } catch (Exception e) {
                input.nextLine();  // Clear buffer
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        return amount;
    }

    public static double getValidPayment(String prompt) {
        boolean validAmount = false;
        double amount = 0;

        while (!validAmount) {
            try {
                System.out.println(prompt);
                amount = input.nextDouble();

                if (amount >= 0) {
                    System.out.println("Amount must be less than zero.");
                    input.nextLine();
                    continue;
                }

                validAmount = true;
                input.nextLine();  // Clear buffer

            } catch (Exception e) {
                input.nextLine();  // Clear buffer
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        return amount;
    }

    public static double getValidAmount(String prompt) {
        boolean validAmount = false;
        double amount = 0;

        while (!validAmount) {
            try {
                System.out.println(prompt);
                amount = input.nextDouble();
                validAmount = true;
                input.nextLine();  // Clear buffer

            } catch (Exception e) {
                input.nextLine();  // Clear buffer
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        return amount;
    }

    public static String getValidString(String prompt) {//clear buffer
        String userInput = "";
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println(prompt);
                userInput = input.nextLine().trim();

                if (userInput.isEmpty()) {
                    System.out.println("Input cannot be empty. Please try again.");
                    continue;
                }

                validInput = true;

            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
        return userInput;
    }

    public static void displayDeposits(ArrayList<Transactions> ledger) {
        sortByMostRecent(ledger);
        for (Transactions transaction : ledger) {
            if (transaction.isDeposit()) {
                String look = getLook(transaction);
                System.out.println(look);
            }
        }
    }

    public static void displayPayments(ArrayList<Transactions> ledger) {
        sortByMostRecent(ledger);
        for (Transactions transaction : ledger) {
            if (transaction.isPayment()) {
                String look = getLook(transaction);
                System.out.println(look);
            }
        }
    }

    public static void depositsScreen(ArrayList<Transactions> ledger) {
        printStandaloneTitle("Deposits", 96, 146);
        displayDeposits(ledger);
    }

    public static void paymentsScreen(ArrayList<Transactions> ledger) {
        printStandaloneTitle("Payments", 96, 146);
        displayPayments(ledger);
    }

    public static void previousYearSearch(ArrayList<Transactions> ledger) {
        double previousYearTotal = 0;
        double previousYearDepositTotal = 0;
        double previousYearPaymentTotal = 0;
        sortByMostRecent(ledger);
        for (Transactions transaction : ledger) {
            if (transaction.isFromPreviousYear()) {
                String look = getLook(transaction);
                System.out.println(look);
                if (transaction.isDeposit()) {
                    previousYearDepositTotal += transaction.total();
                }
                if (transaction.isPayment()) {
                    previousYearPaymentTotal += transaction.total();
                }
                previousYearTotal += transaction.total();
            }
        }
        System.out.println("===========================================================================================================================================");
        System.out.printf("$%.2f is the total deposit for the previous year.\n", previousYearDepositTotal);
        System.out.printf("$%.2f is the total payment made for the previous year.\n", previousYearPaymentTotal);
        System.out.printf("$%.2f is the total net for the previous year.\n", previousYearTotal);
        System.out.println("===========================================================================================================================================");
    }

    public static void previousMonthSearch(ArrayList<Transactions> ledger) {
        double previousMonthTotal = 0;
        double previousMonthDepositTotal = 0;
        double previousMonthPaymentTotal = 0;
        sortByMostRecent(ledger);
        for (Transactions transaction : ledger) {
            if (transaction.isFromPreviousMonth()) {
                String look = getLook(transaction);
                System.out.println(look);
                if (transaction.isDeposit()) {
                    previousMonthDepositTotal += transaction.total();  // Use getAmount()
                }
                if (transaction.isPayment()) {
                    previousMonthPaymentTotal += transaction.total();  // Use getAmount()
                }
                previousMonthTotal += transaction.total();  // Use getAmount()
            }
        }
        System.out.println("===========================================================================================================================================");
        System.out.printf("$%.2f is the total deposit for the previous month.\n", previousMonthDepositTotal);
        System.out.printf("$%.2f is the total payment made for the previous month.\n", previousMonthPaymentTotal);
        System.out.printf("$%.2f is the total net for the previous month.\n", previousMonthTotal);
        System.out.println("===========================================================================================================================================");
    }

    public static void yearToDate(ArrayList<Transactions> ledger) {
        double yearToDateTotal = 0;
        double yearToDateDepositTotal = 0;
        double yearToDatePaymentTotal = 0;
        sortByMostRecent(ledger);
        for (Transactions transaction : ledger) {
            if (transaction.isYearToDate()) {
                String look = getLook(transaction);
                System.out.println(look);
                if (transaction.isDeposit()) {
                    yearToDateDepositTotal += transaction.total();  // Use getAmount()
                }
                if (transaction.isPayment()) {
                    yearToDatePaymentTotal += transaction.total();  // Use getAmount()
                }
                //yearToDateTotal += transaction.getAmount();
                yearToDateTotal += transaction.total();
            }
        }
        System.out.println("===========================================================================================================================================");
        System.out.printf("$%.2f is the total deposit for this year so far.\n", yearToDateDepositTotal);
        System.out.printf("$%.2f is the total payment made for this year so far.\n", yearToDatePaymentTotal);
        System.out.printf("$%.2f is the total net for this year so far.\n", yearToDateTotal);
        System.out.println("===========================================================================================================================================");
    }

    public static void monthToDate(ArrayList<Transactions> ledger) {
        double monthTotal = 0;
        double monthDepositTotal = 0;
        double monthPaymentTotal = 0;
        sortByMostRecent(ledger);
        for (Transactions transaction : ledger) {
            if (transaction.isMonthToDate()) {
                String look = getLook(transaction);
                System.out.println(look);
                if (transaction.isDeposit()) {
                    monthDepositTotal += transaction.total();
                }
                if (transaction.isPayment()) {
                    monthPaymentTotal += transaction.total();
                }
                monthTotal += transaction.total();
            }
        }
        System.out.println("===========================================================================================================================================");
        System.out.printf("$%.2f is the total deposit for this month so far.\n", monthDepositTotal);
        System.out.printf("$%.2f is the total payment made for this month so far.\n", monthPaymentTotal);
        System.out.printf("$%.2f is the total net for this month so far.\n", monthTotal);
        System.out.println("===========================================================================================================================================");
    }

    public static void searchByVendor(ArrayList<Transactions> ledger) {
        input.nextLine();
        int resultCount = 0;
        double totalAmount = 0;
        System.out.println("Please enter the name of the vendor you want transactions pulled up from");
        String vendor = input.nextLine().trim();
        sortByMostRecent(ledger);
        printStandaloneTitle("Transactions", 96, 146);
        for (Transactions transaction : ledger) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                String look = getLook(transaction);
                System.out.println(look);
                resultCount++;
                totalAmount += transaction.getAmount();
            }
        }
        System.out.println("===========================================================================================================================================");
        System.out.printf("Results found: %d\n", resultCount);
        System.out.printf("Total: $%.2f\n", totalAmount);
        System.out.println("===========================================================================================================================================");
    }

    public static void sortByMostRecent(ArrayList<Transactions> ledger) {
        ledger.sort((a, b) -> {
            int dateCompare = b.getDate().compareTo(a.getDate());

            if (dateCompare != 0) {
                return dateCompare; // different dates → use date
            }

            // same date → compare time
            return b.getTime().compareTo(a.getTime());
        });
    }

    public static void previousMonthScreen(ArrayList<Transactions> ledger) {
        printStandaloneTitle("Previous Month Transactions", 96, 146);
        previousMonthSearch(ledger);
    }

    public static void monthToDateScreen(ArrayList<Transactions> ledger) {
        printStandaloneTitle("Month To Date Transactions", 96, 146);
        monthToDate(ledger);
    }

    public static void yearToDateScreen(ArrayList<Transactions> ledger) {
        printStandaloneTitle("Year To Date Transactions", 96, 146);
        yearToDate(ledger);
    }

    public static void previousYearScreen(ArrayList<Transactions> ledger) {
        printStandaloneTitle("Previous Year Transactions", 96, 146);
        previousYearSearch(ledger);
    }

    public static void searchByVendorScreen(ArrayList<Transactions> ledger) {
        searchByVendor(ledger);
    }

    public static void customSearch(ArrayList<Transactions> ledger) {
        sortByMostRecent(ledger);
        input.nextLine(); //clean buffer
        String vendorFilter = getOptionalString("What is the vendor name (or press Enter to skip): ");
        String descriptionFilter = getOptionalString("What is the description for the transaction(or press Enter to skip): ");
        String dateRangeChoice = getOptionalString("Search by date range? (yes/no, or press Enter to skip): ");
        String typeFilter = getOptionalString("Filter by type (deposit/payment/all, or press Enter for all): ");
        String amountRangeChoice = getOptionalString("Search by amount range? (yes/no, or press Enter to skip): ");
        double totalAmount = 0;
        int resultCount = 0;
        LocalDate startDate = null;
        LocalDate endDate = null;
        double minAmount = Double.NEGATIVE_INFINITY;
        double maxAmount = Double.POSITIVE_INFINITY;
        // Get date range if requested
        if (dateRangeChoice.equalsIgnoreCase("yes")) {
            startDate = getValidDate("Enter start date (yyyy-MM-dd): ");
            endDate = getValidDate("Enter end date (yyyy-MM-dd): ");
            while (endDate.isBefore(startDate)) {
                System.out.println("The end date can not be before the start date");
                endDate = getValidDate("Enter end date (yyyy-MM-dd): ");
            }
        }
        // Get amount range if requested
        if (amountRangeChoice.equalsIgnoreCase("yes")) {
            minAmount = getValidAmount("Enter minimum amount: ");
            maxAmount = getValidAmount("Enter maximum amount: ");
            while (maxAmount < minAmount) {
                System.out.println("The maximum amount has to be greater than the minimum");
                maxAmount = getValidAmount("Enter maximum amount: ");
            }
        }
        boolean usePartialMatch = isUsePartialMatch();
        printStandaloneTitle("Transactions", 96, 146);
        for (Transactions transaction : ledger) {
            boolean matches = true;

            // Check Vendor filter
            if (!vendorFilter.isEmpty()) {
                if (usePartialMatch) {
                    if (!transaction.getVendor().toLowerCase().contains(vendorFilter.toLowerCase())) {
                        matches = false;
                    }
                } else {
                    if (!transaction.getVendor().trim().equalsIgnoreCase(vendorFilter.trim())) {
                        matches = false;
                    }
                }
            }
            // Check description filter
            if (!descriptionFilter.isEmpty()) {
                if (usePartialMatch) {
                    if (!transaction.getDescription().toLowerCase().contains(descriptionFilter.toLowerCase())) {
                        matches = false;
                    }
                } else {
                    if (!transaction.getDescription().trim().equalsIgnoreCase(descriptionFilter.trim())) {
                        matches = false;
                    }
                }
            }
            // Check date range filter
            if (startDate != null && endDate != null) {
                if (transaction.getDate().isBefore(startDate) ||
                        transaction.getDate().isAfter(endDate)) {
                    matches = false;
                }
            }

            // Check type filter
            if (!typeFilter.isEmpty() && !typeFilter.equalsIgnoreCase("all")) {
                if (typeFilter.equalsIgnoreCase("deposit") && !transaction.isDeposit()) {
                    matches = false;
                }
                if (typeFilter.equalsIgnoreCase("payment") && !transaction.isPayment()) {
                    matches = false;
                }
            }

            // Check amount range filter
            if (transaction.getAmount() < minAmount || transaction.getAmount() > maxAmount) {
                matches = false;
            }
            if (matches) {
                String look = getLook(transaction);
                System.out.println(look);
                totalAmount += transaction.getAmount();
                resultCount++;
            }
        }
        System.out.println("===========================================================================================================================================");
        System.out.printf("Results found: %d\n", resultCount);
        System.out.printf("Total: $%.2f\n", totalAmount);
        System.out.println("===========================================================================================================================================");
    }

    private static boolean isUsePartialMatch() {
        System.out.println("Do you want partial matching? (yes/no)");
        String choice = input.nextLine().trim();
        while (!(choice.equalsIgnoreCase("yes") || choice.equalsIgnoreCase("no"))) {
            System.out.println("Please enter a valid input.(Yes/NO)");
            choice = input.nextLine();
        }
        return choice.equalsIgnoreCase("yes");
    }

    public static String getOptionalString(String prompt) {
        System.out.println(prompt);
        return input.nextLine().trim();
    }

    public static void customSearchScreen(ArrayList<Transactions> ledger) {
        customSearch(ledger);
    }

    public static void printStandaloneTitle(String title, int boxWidth, int screenWidth) {
        // 1. Top Border
        int leftMargin = (screenWidth - boxWidth) / 2;
        String margin = " ".repeat(Math.max(0, leftMargin));

        // 2. Top border (with margin)
        System.out.println(margin + "╔" + "═".repeat(boxWidth - 2) + "╗");

        // 3. The centered title row (with margin)
        String centeredText = center(title, boxWidth - 4);
        System.out.printf("%s║ %s ║%n", margin, centeredText);

        // 4. Bottom border (with margin)
        System.out.println(margin + "╚" + "═".repeat(boxWidth - 2) + "╝");
    }

    public static String center(String text, int width) {
        if (text == null || text.length() >= width) {
            return text; // Return as-is if it's too long
        }

        int padding = (width - text.length()) / 2;

        // This creates the leading spaces, adds the text,
        // then adds enough trailing spaces to hit the total width.
        return format("%" + (padding + text.length()) + "s", text)
                + format("%" + (width - (padding + text.length())) + "s", "");
    }
}
