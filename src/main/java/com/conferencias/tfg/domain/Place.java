package com.conferencias.tfg.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "place")
public class Place {

    @Id
    @JsonIgnore
    private long id;
    @NotBlank
    private String town;
    @NotBlank
    private String country;
    @NotBlank
    private String address;
    @NotBlank
    private String postalCode;                                  //TODO añadir regex

    public Place() {

    }

    public Place(String town, String country, String address, String postalCode) {
        this.town = town;
        this.country = country;
        this.address = address;
        this.postalCode = postalCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
