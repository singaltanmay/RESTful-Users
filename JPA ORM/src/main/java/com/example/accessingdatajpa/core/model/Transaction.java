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

    @Column(name = "sender_acc_id", length = 10, nullable = false)
    private String senderAccountId;

    @Column(name = "receiver_acc_id", length = 10, nullable = false)
    private String receiverAccountId;

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
}


