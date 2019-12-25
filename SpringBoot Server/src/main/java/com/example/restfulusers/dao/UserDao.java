package com.example.restfulusers.dao;

import com.example.restfulusers.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {

    byte OK = 0;
    byte RESOURCE_NOT_FOUND = 1;
    byte BAD_REQUEST = 2;

    boolean insertNewUser(UUID uuid, User user);

    default boolean insertNewUser(User user) {
        return insertNewUser(UUID.randomUUID(), user);
    }

    List<User> getAllUsers();

    Optional<User> getUserByID(UUID uuid);

    boolean deleteAllUsers();

    byte deleteUserByID(UUID uuid);

    byte replaceOrInsertUserByID(UUID uuid, User user);

    byte updateUserByID(UUID uuid, User user);

}
