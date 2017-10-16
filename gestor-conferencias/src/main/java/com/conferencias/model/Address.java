package com.conferencias.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Address {

    private String city;
    private String country;

    @JsonCreator
    public Address(){}

    public Address(String city, String country){
        this.city = city;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
