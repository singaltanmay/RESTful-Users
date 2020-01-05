package com.example.accessingdatajpa.core.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {

    enum TransactionTypes {
        DEBIT,
        CREDIT
    }

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String transactionID;

    @Column(name = "amount", nullable = false)
    private double transactionAmount;

    @Column(name = "type", nullable = false)
    private TransactionTypes transactionType;

    @OneToOne
    private Customer sender;

    @OneToOne
    @JoinColumn(name = "sendTo")
    private Customer receiver;

    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionTime = new Date();

    private String getPrefix() {
        switch (transactionType) {
            case CREDIT:
                return "cred";
            case DEBIT:
                return "deb";
            default:
                return "err";
        }
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TransactionTypes getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypes transactionType) {
        this.transactionType = transactionType;
    }

    public Customer getSender() {
        return sender;
    }

    public void setSender(Customer sender) {
        this.sender = sender;
    }

    public Customer getReceiver() {
        return receiver;
    }

    public void setReceiver(Customer receiver) {
        this.receiver = receiver;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }
}


