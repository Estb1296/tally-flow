package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;


public class Transactions {
    private LocalDate date;
    public LocalTime time;
    public String description;
    public String vendor;
    public double amount;
    Transactions(LocalDate date, LocalTime time, String description, String vendor, Double amount){
        this.date=date;
        this.time=time;
        this.description=description;
        this.vendor=vendor;
        this.amount=amount;
    }
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }
    public boolean isDeposit(){
        return amount>0;
    }
    public boolean isPayment(){
        return amount<0;
    }
    public boolean isFromPreviousYear() {
        int currentYear = LocalDate.now().getYear();
        int previousYear = currentYear - 1;
        return date.getYear() == previousYear;
    }
    public boolean isFromPreviousMonth(){
        LocalDate today = LocalDate.now();
        LocalDate previousMonth = today.minusMonths(1);  // Handles year rollover automatically
        return this.date.getMonthValue() == previousMonth.getMonthValue() &&
                this.date.getYear() == previousMonth.getYear();
    }
    public boolean isYearToDate(){
        LocalDate currentDate =LocalDate.now();
        int currentYear=currentDate.getYear();
        return (this.date.getYear()==currentYear)&&(this.date.isBefore(currentDate)||this.date.isEqual(currentDate));
     }
     public boolean isMonthToDate(){
        LocalDate currentDate=LocalDate.now();
        return this.date.getMonthValue()==currentDate.getMonthValue()&&this.date.getYear()==currentDate.getYear()&&this.date.isBefore(currentDate)||this.date.isEqual(currentDate);
     }
    public double total(){
        double totalAmount=0;
         totalAmount = totalAmount + this.amount;
         return totalAmount;
    }
}
