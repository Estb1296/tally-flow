package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;


/**
 * Represents a financial transaction in the accounting ledger.
 * Each transaction contains a date, time, description, vendor information, and amount.
 * Transactions can be either deposits (positive amounts) or payments (negative amounts).
 * Provides utility methods to query transaction properties such as date ranges and type.
 */
public class Transaction {
    private final LocalDate date;
    public LocalTime time;
    public String description;
    public String vendor;
    public double amount;

    /**
     * Constructs a new Transaction with the specified details.
     *
     * @param date The date the transaction occurred
     * @param time The time the transaction occurred
     * @param description A description of the transaction
     * @param vendor The vendor or recipient involved in the transaction
     * @param amount The transaction amount (positive for deposits, negative for payments)
     */
    Transaction(LocalDate date, LocalTime time, String description, String vendor, Double amount){
        this.date=date;
        this.time=time;
        this.description=description;
        this.vendor=vendor;
        this.amount=amount;
    }

    /**
     * Returns the date this transaction occurred.
     *
     * @return The LocalDate of the transaction
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the time this transaction occurred.
     *
     * @return The LocalTime of the transaction
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Returns the description of this transaction.
     *
     * @return The transaction description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the vendor or recipient of this transaction.
     *
     * @return The vendor name
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Returns the amount of this transaction.
     *
     * @return The transaction amount (positive for deposits, negative for payments)
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Determines if this transaction is a deposit (positive amount).
     *
     * @return true if the transaction amount is greater than 0, false otherwise
     */
    public boolean isDeposit(){
        return amount>0;
    }

    /**
     * Determines if this transaction is a payment (negative amount).
     *
     * @return true if the transaction amount is less than 0, false otherwise
     */
    public boolean isPayment(){
        return amount<0;
    }

    /**
     * Determines if this transaction occurred in the previous calendar year.
     *
     * @return true if the transaction date is in the year before the current year, false otherwise
     */
    public boolean isFromPreviousYear() {
        int currentYear = LocalDate.now().getYear();
        int previousYear = currentYear - 1;
        return date.getYear() == previousYear;
    }

    /**
     * Determines if this transaction occurred in the previous calendar month.
     * Handles year rollover automatically (e.g., December to January).
     *
     * @return true if the transaction date is in the previous month, false otherwise
     */
    public boolean isFromPreviousMonth(){
        LocalDate today = LocalDate.now();
        LocalDate previousMonth = today.minusMonths(1);
        return this.date.getMonthValue() == previousMonth.getMonthValue() &&
                this.date.getYear() == previousMonth.getYear();
    }

    /**
     * Determines if this transaction occurred in the current calendar year up to today.
     *
     * @return true if the transaction date is in the current year and on or before today, false otherwise
     */
    public boolean isYearToDate(){
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        return (this.date.getYear() == currentYear) && (this.date.isBefore(currentDate) || this.date.isEqual(currentDate));
    }

    /**
     * Determines if this transaction occurred in the current calendar month up to today.
     *
     * @return true if the transaction date is in the current month and on or before today, false otherwise
     */
    public boolean isMonthToDate(){
        LocalDate currentDate = LocalDate.now();
        return this.date.getMonthValue() == currentDate.getMonthValue() &&
                this.date.getYear() == currentDate.getYear() &&
                (this.date.isBefore(currentDate) || this.date.isEqual(currentDate));
    }

    /**
     * Returns the total amount of this transaction.
     * For deposits, this is a positive value; for payments, this is a negative value.
     *
     * @return The transaction amount
     */
    public double total(){
        double totalAmount = 0;
        totalAmount = totalAmount + this.amount;
        return totalAmount;
    }
}