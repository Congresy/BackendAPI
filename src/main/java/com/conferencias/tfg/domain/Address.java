package com.conferencias.tfg.domain;

import com.conferencias.tfg.utilities.Views.Detailed;
import com.fasterxml.jackson.annotation.JsonView;

public class Address {
	@JsonView(Detailed.class)
	private String city;
	@JsonView(Detailed.class)
	private String country;
	@JsonView(Detailed.class)
	private String address;

	public Address() {

	}

	public Address(String city, String country, String address) {
		this.city = city;
		this.country = country;
		this.address = address;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
