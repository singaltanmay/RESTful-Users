package com.example.restfulusers.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class User {

    @Id
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public User(@JsonProperty UUID uuid,
                @JsonProperty String firstName,
                @JsonProperty String lastName,
                @JsonProperty String phoneNumber) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "UUID: " +
                uuid +
                "First Name: " +
                firstName +
                "Last Name: " +
                lastName +
                "Phone Number: " +
                phoneNumber;
    }

    public User() {
    }
}
