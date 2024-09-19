package com.jaqg.banking.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serial;
import java.io.Serializable;


@Entity
@DiscriminatorValue("REMOTE")
public class RemoteAccount extends Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public RemoteAccount(Long number, Integer sourceCode) {
        super(number, sourceCode);
    }

    public RemoteAccount() {

    }

    @Override
    public String toString() {
        return "RemoteAccount{" + "number=" + id.number + ", sortCode=" + id.sortCode + '}';
    }

}
