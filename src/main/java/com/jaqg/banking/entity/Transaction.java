package com.jaqg.banking.entity;

import com.jaqg.banking.enums.TransactionType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction implements Serializable, Comparable<Transaction> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This helps auto-generate primary keys
    private long id;

    @CreatedDate
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @Column(precision = 16, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private TransactionType type;

    @ManyToOne
    @JoinColumns( {
            @JoinColumn(name="recipient_number", referencedColumnName="number", updatable = false),
            @JoinColumn(name="recipient_source_code", referencedColumnName="sortCode", updatable = false)
    } )
    private Account recipient; // to account

    @ManyToOne
    @JoinColumns( {
            @JoinColumn(name="sender_number", referencedColumnName="number", updatable = false),
            @JoinColumn(name="sender_source_code", referencedColumnName="sortCode", updatable = false)
    } )
    private Account sender;

    public Transaction(BigDecimal amount, TransactionType type, Account recipient, Account sender) {
        this.amount = amount;
        this.type = type;
        setRecipient(recipient);
        setSender(sender);
    }

    public Transaction() {
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        if (sender != null) {
//            sender.addCreditTransaction(this);
        }
        this.sender = sender;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal transVal) {
        this.amount = transVal;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType transType) {
        this.type = transType;
    }

    public Account getRecipient() {
        return recipient;
    }

    public void setRecipient(Account recipient) {
        if (recipient != null) {
//            recipient.addDebitTransaction(this);
        }
        this.recipient = recipient;
    }

    public Long getFromAccountNumber() {
        return sender == null ? null : sender.getNumber();
    }

    public Integer getFromAccountSourceCode() {
        return sender == null ? null : sender.getSortCode();
    }

    public Long getToAccountNumber() {
        return recipient == null ? null : recipient.getNumber();
    }

    public Integer getToAccountSourceCode() {
        return recipient == null ? null : recipient.getSortCode();
    }

    @Override
    public int compareTo(Transaction o) {
        return dateTime.compareTo(o.dateTime);
    }
}
