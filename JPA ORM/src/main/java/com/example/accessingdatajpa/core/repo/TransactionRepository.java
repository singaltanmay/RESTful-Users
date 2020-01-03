package com.example.accessingdatajpa.core.repo;

import com.example.accessingdatajpa.core.model.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, String> {
}
