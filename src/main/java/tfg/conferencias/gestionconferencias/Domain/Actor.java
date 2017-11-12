package tfg.conferencias.gestionconferencias.Domain;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "actor")
public abstract class Actor extends DomainEntity {

	@NotBlank
	private String name;//
	@NotBlank
	private String surname;
	@Email
	@NotBlank
	private String email;
	@NotBlank
	private String phone;
	@URL
	private String photo;
	@NotNull
	private Boolean banned;

	private Address address;
	private List<Comment> comments;

 	public Actor(String name, String surname, String email, String phone, Address address) {

		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone = phone;
		this.banned = false;

		this.address = address;
		this.comments = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Override
	public String toString() {
		return String.format("Customer[ name='%s', surname='%s']", name, surname);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Boolean getBanned() {
		return banned;
	}

	public void setBanned(Boolean banned) {
		this.banned = banned;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
}