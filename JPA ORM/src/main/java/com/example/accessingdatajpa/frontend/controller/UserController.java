package com.example.accessingdatajpa.frontend.controller;

import com.example.accessingdatajpa.core.model.Customer;
import com.example.accessingdatajpa.core.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/bank")
@RestController
public class UserController {

    final CustomerService customerService;

    @Autowired
    public UserController(CustomerService customerService) {
        this.customerService = customerService;
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

}
