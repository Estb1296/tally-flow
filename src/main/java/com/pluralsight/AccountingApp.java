package com.pluralsight;
import java.util.Scanner;
public class AccountingApp {
    static Scanner input=new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Good to go");
        runHomeScreen();
    }
    public static void runHomeScreen(){
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
                case 1-> runAddDepositScreen();
                case 2->runMakePaymentScreen();
                case 3-> runLedgerScreen();
                case 4-> isRunning=false;
                default-> System.out.println("Invalid input");
            }
        }
        exitApp();
    }
    public static void runAddDepositScreen(){
        System.out.println("How much do you want to deposit?");
        double depositAmount= input.nextDouble();
        System.out.printf("%.2f is the amount you have deposited into your account.\n",depositAmount);
    }
    public static void runMakePaymentScreen(){
        System.out.println("The second method is working");
        System.out.println("How much are you trying to make a payment on today?");
        double paymentAmount=input.nextDouble();
        System.out.printf("%.2f is the amount you paid today.\n",paymentAmount);
    }
    public static void runLedgerScreen(){
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
                case 1-> System.out.println("DisplayAllEntriesScreen");
                case 2-> System.out.println("DepositsScreen");
                case 3-> System.out.println("PaymentsScreen");
                case 4-> runReportsScreen();
                case 5->isRunning=false;

            }
        }
        runHomeScreen();
    }
    public static void exitApp(){
        System.out.println("You have a nice day!");
    }
    public static void runReportsScreen(){
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
            case 1-> System.out.println("MonthToDateScreen");
            case 2-> System.out.println("PreviousMonthScreen");
            case 3-> System.out.println("YearToDateScreen");
            case 4-> System.out.println("PreviousYearScreen");
            case 5-> System.out.println("SearchByVendorScreen");
            case 6-> System.out.println("CustomSearchScreen (bonus search by all the listed fields at once but they are all optional)");
            case 7-> isRunning=false;
            default-> System.out.println("Invalid Input!");
        }
        }
        runLedgerScreen();
    }
}
