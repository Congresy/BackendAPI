package com.conferencias.tfg.domain;

import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;
import java.util.Objects;

@Document
public class Place {

    @Id
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
    @JsonView(Views.Default.class)
    private String details;
    @NotBlank
    @JsonView(Views.Default.class)
    private String postalCode;

    //TODO añadir que ciertos valores sean
    public Place() {

    }

    public Place(String id, String town, String country, String address, String postalCode, String details) {
        this.id = id;
        this.town = town;
        this.country = country;
        this.address = address;
        this.postalCode = postalCode;
        this.details = details;
    }

    public Place(String town, String country, String address, String postalCode, String details) {
        this.town = town;
        this.country = country;
        this.address = address;
        this.postalCode = postalCode;
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(id, place.id) &&
                Objects.equals(address, place.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, address);
    }
}
