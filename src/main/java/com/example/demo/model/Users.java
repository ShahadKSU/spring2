package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

@Document(collection = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {


    @Id
    private String id;
    private String AccountNumber;
    private String CardNumber;
    private double janaPoints;
    private String username;
    private String password;


    private String FullName;
    private double Balance;
    private List<Transaction> transactions;

    @JsonSerialize
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Transaction {
        private String date;
        private double amount;
        private String description;
        private String merchantName;
        private String merchantNameInArabic;
        private String category;
        private String transaction_type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    // Getters and setters
    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String AccountNumber) {
        this.AccountNumber = AccountNumber;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String CardNumber) {
        this.CardNumber = CardNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double Balance) {
        this.Balance = Balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setJanaPoints(double janaPoints) {
        this.janaPoints = janaPoints;
    }

    public double getJanaPoints() {
        return janaPoints;
    }


}
