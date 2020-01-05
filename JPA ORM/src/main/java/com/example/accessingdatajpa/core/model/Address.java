package com.example.accessingdatajpa.core.model;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Transient
    private String address;

    @OneToOne(mappedBy = "home")
    private Customer resident;

    @OneToOne(mappedBy = "office")
    private Customer worker;

    private String house;
    private String street;
    private String city;
    private String state;

    public Address(String house, String street, String city, String state) {
        this.house = house;
        this.street = street;
        this.city = city;
        this.state = state;
    }

    private void generateAddress() {
        address = house + ", " + street + ", " + city + ", " + state + ".";
    }

    public String getAddress() {
        generateAddress();
        return address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getResident() {
        return resident;
    }

    public void setResident(Customer resident) {
        this.resident = resident;
    }

    public Customer getWorker() {
        return worker;
    }

    public void setWorker(Customer worker) {
        this.worker = worker;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}