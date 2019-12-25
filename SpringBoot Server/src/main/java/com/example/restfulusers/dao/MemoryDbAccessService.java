package com.example.restfulusers.dao;

import com.example.restfulusers.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("memoryDao")
public class MemoryDbAccessService implements UserDao {

    private static final List<User> database = new ArrayList<>();

    @Override
    public boolean insertNewUser(UUID uuid, User user) {
        user.setUUID(uuid);
        database.add(user);
        return true;
    }

    @Override
    public List<User> getAllUsers() {
        return database;
    }

    @Override
    public Optional<User> getUserByID(UUID uuid) {
        return database.stream()
                .filter(user -> user.getUUID().equals(uuid))
                .findFirst();
    }

    @Override
    public boolean deleteAllUsers() {
        database.clear();
        return true;
    }

    @Override
    public byte deleteUserByID(UUID uuid) {

        Optional<User> user = getUserByID(uuid);

        if (user.isPresent()) {
            database.remove(user.get());
            return UserDao.OK;
        } else return UserDao.RESOURCE_NOT_FOUND;

    }

    @Override
    public byte replaceOrInsertUserByID(UUID uuid, User user) {

        if (uuid == null) return UserDao.BAD_REQUEST;
        Optional<User> oldUser = getUserByID(uuid);

        if (oldUser.isPresent()) deleteUserByID(uuid);
        boolean b = insertNewUser(uuid, user);
        return b ? UserDao.OK : UserDao.RESOURCE_NOT_FOUND;

    }

    @Override
    public byte updateUserByID(UUID uuid, User user) {

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

            return UserDao.OK;
        } else return UserDao.RESOURCE_NOT_FOUND;
    }

    private boolean validString(String string) {
        return (string != null) && (!string.isBlank());
    }
}
