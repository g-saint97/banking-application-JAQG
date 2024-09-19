package com.jaqg.banking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    @NotBlank(message = "Name is mandatory")
    @NotNull
    private String fullName;

    @NotNull
    private boolean isRemoved = false;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<LocalAccount> accounts = new ArrayList<>();

    public Customer(String fullName) {
        this.fullName = fullName;
    }

    public Customer() {

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public List<LocalAccount> getAccounts() {
        return accounts;
    }

    public void addAccount(LocalAccount account) {
        if (account == null) {
            throw new NullPointerException("Can't add null account");
        }
        if (account.getCustomer() != null && !account.getCustomer().equals(this)) {
            throw new IllegalStateException("Account is already assigned to a Customer");
        }
        accounts.add(account);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return fullName;
    }
}
