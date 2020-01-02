package com.example.restfulusers.dao;

import com.example.restfulusers.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public class JpaDbAccessService extends CrudRepository<User, UUID> {
    
}
