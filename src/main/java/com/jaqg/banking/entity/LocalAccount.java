package com.jaqg.banking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@Entity
@DiscriminatorValue("LOCAL")
public class LocalAccount extends Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(length = 50)
    @NotBlank(message = "Name is mandatory")
    @NotNull
    private String name;

    @Column(precision = 16, scale = 2)
    @NotNull
    private BigDecimal openingBalance;

    @Column(precision = 16, scale = 2)
    @NotNull
    private BigDecimal balance;

    @NotNull
    private boolean isClosed = false;

    @ManyToOne
    protected Customer customer;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy(value = "dateTime desc")
    private final List<Transaction> debitTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy(value = "dateTime desc")
    private final List<Transaction> creditTransactions = new ArrayList<>();

    public LocalAccount(String name, BigDecimal openingBalance, Customer customer, Integer sortCode) {
        super(sortCode);
        this.name = name;
        this.openingBalance = openingBalance;
        this.balance = openingBalance;
        this.customer = customer;
    }

    public LocalAccount() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.addAccount(this);
    }

    public List<Transaction> getTransactions() {
        return Stream.concat(debitTransactions.stream(), creditTransactions.stream()).sorted().toList();
    }

    public void addDebitTransaction(Transaction transaction) {
        validateTransaction(transaction);
        debitTransactions.add(transaction);
    }

    public void addCreditTransaction(Transaction transaction) {
        validateTransaction(transaction);
        creditTransactions.add(transaction);
    }

    public boolean isClosed() {
        return this.isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    @Override
    public String toString() {
        return "LocalAccount{" +
                "number=" + id.number +
                ", sortCode=" + id.sortCode +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new NullPointerException("Can't add null transaction");
        }
    }
}
