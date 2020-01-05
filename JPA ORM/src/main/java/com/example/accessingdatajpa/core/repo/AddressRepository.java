package com.example.accessingdatajpa.core.repo;

import com.example.accessingdatajpa.core.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
