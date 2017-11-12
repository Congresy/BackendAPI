package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "address")
public class Address extends DomainEntity{

    private String city;
    private String country;//
    private String postal;
    private String address;

    @NotNull
    private Actor actor;

    public Address(String city, String country, String postal, String address, Actor actor) {
        this.city = city;
        this.country = country;
        this.postal = postal;
        this.address = address;

        this.actor = actor;
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

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

}
