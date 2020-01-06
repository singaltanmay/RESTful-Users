package com.example.accessingdatajpa.frontend.controller;

import com.example.accessingdatajpa.core.model.Address;
import com.example.accessingdatajpa.core.model.Customer;
import com.example.accessingdatajpa.core.service.AddressService;
import com.example.accessingdatajpa.core.service.CustomerService;
import com.example.accessingdatajpa.core.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("api/bank")
@RestController
public class UserController {

    final CustomerService customerService;
    final TransactionService transactionService;
    final AddressService addressService;

    @Autowired
    public UserController(CustomerService customerService, TransactionService transactionService, AddressService addressService) {
        this.customerService = customerService;
        this.transactionService = transactionService;
        this.addressService = addressService;
    }

    @GetMapping(path = "/customer")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping(path = "/customer")
    public ResponseEntity<?> insertCustomer(@RequestBody Customer customer) {
        try {
            customerService.insertCustomer(customer);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/test")
    public void runTest() {

        Customer customer = new Customer();

        Address address = new Address("house", "str", "city", "stte");
        Address address1 = new Address("offide", "eg", "vrsg", "geon");

        ArrayList<Address> home = new ArrayList<>();
        home.add(address);
        home.add(address1);
        customer.setHome(home);

        customerService.insertCustomer(customer);

        Customer customer1 = new Customer();
        customer1.setHome(List.of(address));
        customerService.insertCustomer(customer1);

    }

}
