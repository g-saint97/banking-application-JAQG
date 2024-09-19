package com.jaqg.banking.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected AccountPK id = new AccountPK();

    public Account(Integer sortCode) {
        this.id.sortCode = sortCode;
    }

    public Account(Long number, Integer sortCode) {
        this.id.number = number;
        this.id.sortCode = sortCode;
    }

    public Account() {
    }

    public Long getNumber() {
        return id.number();
    }

    public void setNumber(Long number) {
        this.id.number = number;
    }

    public Integer getSortCode() {
        return id.sortCode();
    }

    public void setSortCode(int sortCode) {
        this.id.setSortCode(sortCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Account{" + "number=" + id.number() + ", sortCode=" + id.sortCode() + '}';
    }
}
