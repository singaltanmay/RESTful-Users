package com.example.restfulusers.api;

import com.example.restfulusers.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.restfulusers.service.UserService;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/user")
@RestController
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<User> getUserByID(@PathVariable("id") UUID uuid) {
        return service.getUserByID(uuid);
    }

    @PostMapping
    public ResponseEntity<?> insertNewUser(@RequestBody User user) {
        return service.insertNewUser(user);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> replaceOrInsertUserByID(@PathVariable("id") UUID uuid, @RequestBody User user) {
        return service.replaceOrInsertUserByID(uuid, user);
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<?> updateUserByID(@PathVariable("id") UUID uuid, @RequestBody User user) {
        return service.updateUserByID(uuid, user);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllUsers() {
        return service.deleteAllUsers();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteUserByID(@PathVariable("id") UUID uuid) {
        return service.deleteUserByID(uuid);
    }

}
