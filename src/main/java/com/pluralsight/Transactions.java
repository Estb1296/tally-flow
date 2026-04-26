package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transactions {
    public LocalDate date;
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

}
