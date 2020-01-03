package com.example.accessingdatajpa.core.service;

import com.example.accessingdatajpa.core.model.Transaction;
import com.example.accessingdatajpa.core.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TransactionService {

    private final TransactionRepository repository;

    @Autowired
    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }


    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new LinkedList<>();
        Iterable<Transaction> all = repository.findAll();

        for (Transaction c : all) {
            list.add(c);
        }

        return list;
    }

    public Transaction getTransactionByTransactionID(String transactionID) {
        Optional<Transaction> byId = repository.findById(transactionID);
        return byId.get();
    }

    public void insertTransaction(Transaction Transaction) {
        repository.save(Transaction);
    }

    public void insertTransactionByTransactionID(String transactionID, Transaction Transaction) {
        if (repository.existsById(transactionID)) {
            throw new NullPointerException("A Transaction already exists at specified ID.");
        } else {
            Transaction.setTransactionID(transactionID);
            insertTransaction(Transaction);
        }
    }

    public void deleteAllTransactions() {
        repository.deleteAll();
    }

    public void deleteTransactionByTransactionID(String transactionID) {
        if (!repository.existsById(transactionID)) throw new NullPointerException();
        else {
            repository.deleteById(transactionID);
        }
    }


}
