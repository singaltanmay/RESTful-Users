package com.example.restfulusers.dao;

import com.example.restfulusers.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaDao extends CrudRepository<User, UUID> {

    byte OK = 0;
    byte RESOURCE_NOT_FOUND = 1;

    default List<User> getAllUsers() {

        List<User> list = new LinkedList<>();
        this.findAll().forEach(list::add);

        return list;
    }

    default Optional<User> getUserByID(UUID uuid) {
        return this.findById(uuid);
    }

    default boolean insertNewUser(User user) {
        return insertNewUser(UUID.randomUUID(), user);
    }

    default boolean insertNewUser(UUID uuid, User user) {
        if (this.existsById(uuid)) return false;
        user.setUUID(uuid);
        this.save(user);
        return true;
    }

    default boolean replaceOrInsertUserByID(UUID uuid, User user) {
        if (this.existsById(uuid)) {
            deleteById(uuid);
        }

        return insertNewUser(uuid, user);
    }

    default byte updateUserByID(UUID uuid, User user) {

        Optional<User> oldUser = getUserByID(uuid);

        if (oldUser.isPresent()) {
            User u = oldUser.get();

            String first_name = user.getFirstName();
            if (validString(first_name)) {
                u.setFirstName(first_name);
            }

            String last_name = user.getLastName();
            if (validString(last_name)) {
                u.setLastName(last_name);
            }

            String phone_number = user.getPhoneNumber();
            if (validString(phone_number)) {
                u.setPhoneNumber(phone_number);
            }

            deleteById(uuid);
            insertNewUser(uuid, u);

            return this.OK;
        } else return this.RESOURCE_NOT_FOUND;

    }

    default void deleteAllUsers() {
        this.deleteAll();
    }

    default void deleteUserByID(UUID uuid) {
        this.deleteById(uuid);
    }

    private boolean validString(String string) {
        return (string != null) && (!string.isBlank());
    }

}
