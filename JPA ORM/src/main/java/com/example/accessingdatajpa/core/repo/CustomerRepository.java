package com.example.accessingdatajpa.core.repo;

import com.example.accessingdatajpa.core.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
