package com.conferencias.tfg.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.tomcat.jni.Local;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.conferencias.tfg.utilities.Views.Detailed;
import com.conferencias.tfg.utilities.Views.Shorted;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Document
public class Conference {

	@Id
	@JsonIgnore
	private long id;
	@NotBlank
	@JsonView(Shorted.class)
	private String name;
	@NotBlank
	@JsonView(Detailed.class)
	private String theme;
	@DecimalMin("0.0")
	@JsonView(Detailed.class)
	private Double price;
	@NotNull
	@JsonView(Shorted.class)
	private Double popularity;
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonView(Shorted.class)
	private LocalDateTime start;
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonView(Detailed.class)
	private LocalDateTime end;
	@NotBlank
	@Length(max = 50)
	@JsonView(Detailed.class)
	private String guests;
	@NotBlank
	@JsonView(Detailed.class)
	private String description;
	/*@URL
	private String video_; 					//TODO incorporar API de YouTube para videos en directo
	private List<Calendar> calendars; */	//TODO incorporar API de Google Calendar para los calendarios

	public Conference() {

	}

	public Conference(String name, String theme, Double price, LocalDateTime start, LocalDateTime end,
					  String description, String guests) {
		this.name = name;
		this.theme = theme;
		this.price = price;
		this.popularity = 0.0;
		this.start = start;
		this.end = end;
		this.description = description;
		this.guests = guests;
		this.organizators = new ArrayList<>();
		this.events = new ArrayList<>();
	}

	public String getGuests() {
		return guests;
	}

	public void setGuests(String guests) {
		this.guests = guests;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPopularity() {
		return popularity;
	}

	public void setPopularity(Double popularity) {
		this.popularity = popularity;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// -----------------------------------------------------------------------------------------------------------------

	//TODO mirar porqué no funcionan los @ aquí, popula aunque esté vacio y esté indicado @NotEmpty
	private List<Long> events;
	private List<Long> organizators;

	public List<Long> getEvents() {
		return events;
	}

	public void setEvents(List<Long> events) {
		this.events = events;
	}

	public List<Long> getOrganizators() {
		return organizators;
	}

	public void setOrganizators(List<Long> organizators) {
		this.organizators = organizators;
	}
}
