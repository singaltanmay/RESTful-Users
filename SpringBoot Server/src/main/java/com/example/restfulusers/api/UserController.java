package com.example.restfulusers.api;

import com.example.restfulusers.model.User;
import io.swagger.annotations.*;
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

    @ApiOperation(value = "Gets all users from the database.")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return service.getAllUsers();
    }

    @ApiOperation(value = "Gets the user having given ID from the database.")
    /*@ApiImplicitParams(name = "ID", value = "UUID of the required user.")*/
    @GetMapping(path = "{id}")
    public ResponseEntity<User> getUserByID(@PathVariable("id") UUID uuid) {
        return service.getUserByID(uuid);
    }

    @ApiOperation(value = "Adds a new user to the database having a randomly generated ID.")
    @PostMapping
    public ResponseEntity<?> insertNewUser(@RequestBody User user) {
        return service.insertNewUser(user);
    }

    @ApiOperation(value = "Replaces pre-existing user at specified ID else it adds a new user at that ID.")
    @PutMapping(path = "{id}")
    public ResponseEntity<?> replaceOrInsertUserByID(@PathVariable("id") UUID uuid, @RequestBody User user) {
        return service.replaceOrInsertUserByID(uuid, user);
    }

    @ApiOperation(value = "Modifies the user at specified ID with given data. Only provided values are modified and rest are retained.")
    @PatchMapping(path = "{id}")
    public ResponseEntity<?> updateUserByID(@PathVariable("id") UUID uuid, @RequestBody User user) {
        return service.updateUserByID(uuid, user);
    }

    @ApiOperation("Deletes all users present in the database.")
    @DeleteMapping
    public ResponseEntity<?> deleteAllUsers() {
        return service.deleteAllUsers();
    }

    @ApiOperation("Deletes the user present at specified ID.")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteUserByID(@PathVariable("id") UUID uuid) {
        return service.deleteUserByID(uuid);
    }

}
