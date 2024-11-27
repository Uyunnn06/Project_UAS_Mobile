package com.example.utskelompok;

public class Transaction {
    private String description;
    private int amount;
    private String date;
    private String type; // income or expense

    public Transaction(String description, int amount, String date, String type) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}