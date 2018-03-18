package com.conferencias.tfg.domain;

import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;

@Document
public class Place {

    @Id
    @JsonIgnore
    @JsonView(Views.Default.class)
    private String id;
    @NotBlank
    @JsonView(Views.Default.class)
    private String town;
    @NotBlank
    @JsonView(Views.Default.class)
    private String country;
    @NotBlank
    @JsonView(Views.Default.class)
    private String address;
    @NotBlank
    @Pattern(regexp = "((0[1-9]|5[0-2])|[1-4][0-9])[0-9]{3}")
    @JsonView(Views.Default.class)
    private String postalCode;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
