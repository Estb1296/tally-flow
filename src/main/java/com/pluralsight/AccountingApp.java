package com.pluralsight;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
public class AccountingApp {
    static Scanner input=new Scanner(System.in);
    static ArrayList<Transactions> ledger= new ArrayList<>();
    public static void main(String[] args) {
        System.out.println("Good to go");
        promptAndLoadFileIfEmpty(ledger);
        runHomeScreen( ledger);
    }
    public static void runHomeScreen(ArrayList<Transactions> ledger){
        System.out.println("Welcome to our humble institution");
        boolean isRunning=true;
        while(isRunning) {
            System.out.println("How can we assist you today? Please pick and enter the numbers associated with each menu item.");
            System.out.println("""
                    (1)AddDepositScreen
                    (2)MakePaymentScreen
                    (3)LedgerScreen
                    (4)ExitApp
                    """);
            int choice=input.nextInt();
            switch(choice){
                case 1-> runAddDepositScreen(ledger);
                case 2->runMakePaymentScreen(ledger);
                case 3-> runLedgerScreen(ledger);
                case 4-> isRunning=exitApp();
                default-> System.out.println("Invalid input");
            }
        }

    }
    private static boolean exitApp() {
        System.out.println("You have a nice day!");
        return false;
    }

    public static void runAddDepositScreen(ArrayList<Transactions> ledger){
        handleNewDeposit(ledger);
    }
    public static void runMakePaymentScreen(ArrayList<Transactions> ledger){
       handleNewPayment(ledger);
    }
    public static void runLedgerScreen(ArrayList<Transactions> ledger){
        System.out.println("The ledger screen is working well so far.");
        boolean isRunning=true;
        while(isRunning){
            System.out.println("You are accessing your ledger now");
            System.out.println("Pick from the following choices by entering the number beside the choices.");
            System.out.println("""
                    (1)DisplayAllEntriesScreen
                    (2)DepositsScreen
                    (3)PaymentsScreen
                    (4)ReportsScreen
                    (5)Go Back To The HomeScreen
                    """);
            int choice= input.nextInt();
            switch(choice){
                case 1-> displayAllEntriesScreen(ledger);
                case 2-> depositsScreen(ledger);
                case 3-> paymentsScreen(ledger);
                case 4-> runReportsScreen(ledger);
                case 5->isRunning=false;
            }
        }
    }
    public static void runReportsScreen(ArrayList<Transactions> ledger){
        System.out.println("this is the reports screen");
        boolean isRunning=true;
        while(isRunning){
        System.out.println("""
                (1)MonthToDateScreen
                (2)PreviousMonthScreen
                (3)YearToDateScreen
                (4)PreviousYearScreen
                (5)SearchByVendorScreen
                (6)CustomSearchScreen (bonus search by all the listed fields at once but they are all optional)
                (7)goBackToLedger
                """);
        int choice=input.nextInt();
        switch(choice){
            case 1-> monthToDateScreen(ledger);
            case 2-> previousMonthScreen(ledger);
            case 3-> yearToDateScreen(ledger);
            case 4-> previousYearScreen(ledger);
            case 5-> searchByVendorScreen(ledger);
            case 6-> System.out.println("CustomSearchScreen (bonus search by all the listed fields at once but they are all optional)");
            case 7-> isRunning=false;
            default-> System.out.println("Invalid Input!");
        }
        }
    }
    public static void readTransactionsFromFile(String filename, ArrayList<Transactions> ledger){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));  // Use filename parameter
            String line;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            boolean firstLine = true;

            while((line = reader.readLine()) != null) {
                if(firstLine) {
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
            // Silent - do nothing
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    public static void promptAndLoadFileIfEmpty(ArrayList<Transactions> ledger) {
        loadTransactionsFromFile("transactions.csv", ledger);

        if(ledger.isEmpty()) {
            System.out.println("No transactions found in transactions.csv");
            System.out.println("Would you like to load a different file? (yes/no)");
            String choice = input.nextLine().trim();

            if(choice.equalsIgnoreCase("yes")) {
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
    public static void displayAllEntriesScreen(ArrayList<Transactions> ledger){
        if(ledger.isEmpty()) {
            System.out.println("No transactions recorded yet.");
            return;
        }
        for(Transactions transaction:ledger){
            System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount: $%.2f%n",
                    transaction.getDate(),transaction.getTime(),transaction.getDescription(),transaction.getVendor(),transaction.getAmount());
        }
    }
    public static void searchByVendor(ArrayList<Transactions> ledger){
        input.nextLine();
        System.out.println("Please enter the name of the vendor you want transactions pulled up from");
        String vendor=input.nextLine().trim();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        for(Transactions transaction : ledger) {
            if(transaction.getVendor().equalsIgnoreCase(vendor)) {
                System.out.printf("%s|%s|%s|%s|%.2f%n",
                        transaction.getDate().format(dateFormatter),
                        transaction.getTime().format(timeFormatter),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
    }
    public static void searchByVendorScreen(ArrayList<Transactions>ledger){
        searchByVendor(ledger);
    }
    public static void promptUserForDepositInfo(ArrayList<Transactions>ledger){

        double depositAmount=getValidAmount("How much do you want to deposit?");
        System.out.printf("%.2f is the amount you have deposited into your account.\n",depositAmount);

        String vendor = getValidString("What is the source of funds of the deposit?");

        String description=getValidString("What is the description of said deposit?");

        LocalDate date = getValidDate("What is the date the deposit was made?");
        LocalTime time=LocalTime.now();
        ledger.add(new Transactions(date,time,description,vendor,depositAmount));

    }
    public static void addDepositToLedger(ArrayList<Transactions>ledger){
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));  // true = append
            Transactions lastDeposit= ledger.get(ledger.size()-1);
            String line = String.format("%s|%s|%s|%s|%.2f%n", lastDeposit.getDate(), lastDeposit.getTime().format(timeFormatter), lastDeposit.getDescription(), lastDeposit.getVendor(), lastDeposit.getAmount());
            writer.write(line);
            writer.close();


            System.out.println("Deposit recorded successfully!");

        } catch (IOException e) {
            System.out.println("Unable to record transaction. Try again later.");
        }
    }
    public static void handleNewDeposit(ArrayList<Transactions> ledger){
        promptUserForDepositInfo(ledger);
        addDepositToLedger(ledger);
    }
    public static void promptUserForPaymentInfo(ArrayList<Transactions>ledger){

        double paymentAmount = getValidAmount("How much was the payment?");
        System.out.printf("%.2f is the amount you have paid from your account.%n", paymentAmount);

        input.nextLine();


        String vendor = getValidString("What is the recipient of the payment?");

        String description = getValidString("What is the description of said payment?");

        LocalDate date = getValidDate("What is the date the payment was made?");
        LocalTime time = LocalTime.now();

        ledger.add(new Transactions(date, time, description, vendor, paymentAmount));
    }
    public static void processPaymentMade(ArrayList<Transactions>ledger){
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));  // true = append
            Transactions lastPayment = ledger.get(ledger.size() - 1);
            String line = String.format("%s|%s|%s|%s|%.2f%n",
                    lastPayment.getDate().format(timeFormatter),
                    lastPayment.getTime(),
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
    public static void handleNewPayment(ArrayList<Transactions> ledger){
        promptUserForPaymentInfo(ledger);
        processPaymentMade(ledger);
    }
    public static LocalDate getValidDate(String prompt) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        boolean validDate = false;
        LocalDate date = null;

        while(!validDate) {
            try {
                System.out.println(prompt);
                System.out.println("(Format: yyyy-MM-dd)");
                date = LocalDate.parse(input.nextLine(), dateFormatter);
                validDate = true;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd (example: 2026-04-25)");
            }
        }

        return date;
    }
    public static double getValidAmount(String prompt) {
        boolean validAmount = false;
        double amount = 0;

        while(!validAmount) {
            try {
                System.out.println(prompt);
                amount = input.nextDouble();

                if(amount <= 0) {
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

    public static String getValidString(String prompt) {
        String userInput = "";
        boolean validInput = false;

        while(!validInput) {
            try {
                System.out.println(prompt);
                userInput = input.nextLine().trim();

                if(userInput.isEmpty()) {
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
    public static void displayDeposits(ArrayList<Transactions>ledger){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        for(Transactions transaction:ledger){
            if(transaction.isDeposit()){
                System.out.printf("%s|%s|%s|%s|%.2f%n",
                        transaction.getDate().format(dateFormatter),
                        transaction.getTime().format(timeFormatter),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
    }
    public static void displayPayments(ArrayList<Transactions>ledger){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        for(Transactions transaction:ledger){
            if(transaction.isPayment()){
                System.out.printf("%s|%s|%s|%s|%.2f%n",
                        transaction.getDate().format(dateFormatter),
                        transaction.getTime().format(timeFormatter),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
    }
    public static void depositsScreen(ArrayList<Transactions>ledger){
        System.out.println("==========All deposits are as follows==========");
        displayDeposits(ledger);
    }
    public static void paymentsScreen(ArrayList<Transactions>ledger){
        System.out.println("==========All payments processed are as follows==========");
        displayPayments(ledger);
    }

    public static void previousYearSearch(ArrayList<Transactions>ledger){
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    for(Transactions transaction : ledger) {
                if(transaction.isFromPreviousYear()) {
                    System.out.printf("%s|%s|%s|%s|%.2f%n",
                            transaction.getDate().format(dateFormatter),
                            transaction.getTime().format(timeFormatter),
                            transaction.getDescription(),
                            transaction.getVendor(),
                            transaction.getAmount());
                }
            }
     }
     public static void previousMonthSearch(ArrayList<Transactions>ledger){
     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
     DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
     for(Transactions transaction : ledger){
            if(transaction.isFromPreviousMonth()){
                System.out.printf("%s|%s|%s|%s|%.2f%n",
                        transaction.getDate().format(dateFormatter),
                        transaction.getTime().format(timeFormatter),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
     }
     }
    public static void yearToDate(ArrayList<Transactions>ledger){
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    for(Transactions transaction:ledger){
        if(transaction.isYearToDate()){
            System.out.printf("%s|%s|%s|%s|%.2f%n",
                    transaction.getDate().format(dateFormatter),
                    transaction.getTime().format(timeFormatter),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
        }
    }
    }
    public static void monthToDate(ArrayList<Transactions>ledger){
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    for(Transactions transaction:ledger){
        if(transaction.isMonthToDate()){
            System.out.printf("%s|%s|%s|%s|%.2f%n",
                    transaction.getDate().format(dateFormatter),
                    transaction.getTime().format(timeFormatter),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
        }
    }
    }
    public static void previousMonthScreen(ArrayList<Transactions>ledger){
        System.out.println("==========All Transactions are as follows==========");
        previousMonthSearch(ledger);
    }
    public static void monthToDateScreen(ArrayList<Transactions>ledger){
        System.out.println("==========All Transactions are as follows==========");
        monthToDate(ledger);
    }
    public static void yearToDateScreen(ArrayList<Transactions>ledger){
        System.out.println("==========All Transactions are as follows==========");
        yearToDate(ledger);
    }
    public static void previousYearScreen(ArrayList<Transactions>ledger){
        System.out.println("==========All Transactions are as follows==========");
        previousYearSearch(ledger);
    }
}
