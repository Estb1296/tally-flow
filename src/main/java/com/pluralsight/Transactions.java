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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

}
