package com.example.restfulusers.service;

import com.example.restfulusers.dao.UserDao;
import com.example.restfulusers.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(@Qualifier("memoryDao") UserDao userDao) {
        this.userDao = userDao;
    }

    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userDao.getAllUsers();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    public ResponseEntity<User> getUserByID(UUID uuid) {
        Optional<User> user = userDao.getUserByID(uuid);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> insertNewUser(User user) {
        boolean i = userDao.insertNewUser(user);
        if (i) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<?> replaceOrInsertUserByID(UUID uuid, User user) {
        byte b = userDao.replaceOrInsertUserByID(uuid, user);
        if (b == UserDao.OK) return new ResponseEntity<>(HttpStatus.OK);
        else if (b == UserDao.BAD_REQUEST) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> updateUserByID(UUID uuid, User user) {
        byte code = userDao.updateUserByID(uuid, user);
        if (code == UserDao.OK) return new ResponseEntity<>(HttpStatus.OK);
        else if (code == UserDao.RESOURCE_NOT_FOUND) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> deleteAllUsers() {
        boolean b = userDao.deleteAllUsers();
        if (b) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> deleteUserByID(UUID uuid) {
        byte b = userDao.deleteUserByID(uuid);
        if (b == UserDao.OK) return new ResponseEntity<>(HttpStatus.OK);
        else if (b == UserDao.RESOURCE_NOT_FOUND) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
