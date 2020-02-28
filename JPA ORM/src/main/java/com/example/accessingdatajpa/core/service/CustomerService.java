package com.example.accessingdatajpa.core.service;

import com.example.accessingdatajpa.core.model.Customer;
import com.example.accessingdatajpa.core.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }


    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerByAccountID(long accountID) {
        Optional<Customer> byId = repository.findById(accountID);
        return byId.get();
    }

    @Transactional
    public void insertCustomer(Customer customer) {
        repository.save(customer);
    }

    public void insertCustomerByAccountID(long accountID, Customer customer) throws Exception {
        if (repository.existsById(accountID)) {
            throw new Exception("A customer already exists at specified ID.");
        } else {
            customer.setAccountID(accountID);
            insertCustomer(customer);
        }
    }

    public void deleteAllCustomers() {
        repository.deleteAll();
    }

    public void deleteCustomerByAccountID(long accountID) throws Exception {
        if (!repository.existsById(accountID)) throw new Exception();
        else {
            repository.deleteById(accountID);
        }
    }


}
