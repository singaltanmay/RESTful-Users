package com.example.restfulusers.service;

import com.example.restfulusers.dao.UserJpaDao;
import com.example.restfulusers.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserJpaService {

    private final UserJpaDao dao;

    @Autowired
    public UserJpaService(UserJpaDao userDao) {
        this.dao = userDao;
    }

    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = dao.getAllUsers();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    public ResponseEntity<User> getUserByID(UUID uuid) {
        Optional<User> user = dao.getUserByID(uuid);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> insertNewUser(User user) {
        boolean i = dao.insertNewUser(user);
        if (i) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<?> replaceOrInsertUserByID(UUID uuid, User user) {
        if (dao.replaceOrInsertUserByID(uuid, user)) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> updateUserByID(UUID uuid, User user) {
        byte code = dao.updateUserByID(uuid, user);
        if (code == dao.OK) return new ResponseEntity<>(HttpStatus.OK);
        else if (code == dao.RESOURCE_NOT_FOUND) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> deleteAllUsers() {
        dao.deleteAllUsers();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUserByID(UUID uuid) {
        dao.deleteUserByID(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
