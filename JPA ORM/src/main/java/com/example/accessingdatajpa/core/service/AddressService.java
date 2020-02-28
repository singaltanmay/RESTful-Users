package com.example.accessingdatajpa.core.service;

import com.example.accessingdatajpa.core.model.Address;
import com.example.accessingdatajpa.core.repo.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AddressService {

    private final AddressRepository repository;

    @Autowired
    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public List<Address> getAllAddresses() {
        return repository.findAll();
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void insertAddress(Address address) {
        repository.save(address);
    }

    public Address getAddressById(long id) {
        return repository.findById(id).get();
    }

}
