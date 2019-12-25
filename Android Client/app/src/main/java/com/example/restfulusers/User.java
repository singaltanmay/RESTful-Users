package com.example.restfulusers;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class User {

    @SerializedName("id")
    private UUID uuid;
    @SerializedName("f_name")
    private String firstName;
    @SerializedName("l_name")
    private String lastName;
    @SerializedName("ph_num")
    private String phoneNumber;

    public User(UUID uuid,
                String firstName,
                String lastName,
                String phoneNumber) {
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
}



