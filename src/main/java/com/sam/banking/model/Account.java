package com.sam.banking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private BigDecimal balance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    public Account() {
    }

    public Account(Long id, String username, BigDecimal balance, String password, List<Transaction> transactions) {
        this.id = id;
        this.username = username;
        this.balance = balance;
        this.password = password;
        this.transactions = transactions;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public BigDecimal getBalance() {
//        return balance;
//    }
//
//    public void setBalance(BigDecimal balance) {
//        this.balance = balance;
//    }
//
//    public List<Transaction> getTransactions() {
//        return transactions;
//    }
//
//    public void setTransactions(List<Transaction> transactions) {
//        this.transactions = transactions;
//    }
}
