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

        Address address = new Address("house", "str", "city", "stte");
        Customer customer = new Customer();
        customer.setHome(address);
//        address.setResident(customer);

        Address address1 = new Address("offide", "eg", "vrsg", "geon");
        customer.setOffice(address1);
//        address1.setWorker(customer);

//        addressService.insertAddress(address);
//        addressService.insertAddress(address1);
        customerService.insertCustomer(customer);

    }

}
