package com.conferencias.tfg.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.conferencias.tfg.utilities.Views;
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

import javax.annotation.Generated;
import javax.jdo.annotations.Serialized;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Document
public class Conference {

	@Id
	@JsonIgnore
	private String id;
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
    @NotBlank
    @Pattern(regexp = "^\\d{2}\\/\\d{2}\\/\\d{4}\\s*(?:\\d{2}:\\d{2}(?::\\d{2})?)?$")
    @JsonView(Shorted.class)
	private String start;
    @NotBlank
    @Pattern(regexp = "^\\d{2}\\/\\d{2}\\/\\d{4}\\s*(?:\\d{2}:\\d{2}(?::\\d{2})?)?$")
    @JsonView(Detailed.class)
	private String end;
	@NotBlank
	@Length(max = 50)
	@JsonView(Detailed.class)
	private String speakersNames;
	@NotBlank
	@JsonView(Detailed.class)
	private String description;

	public Conference() {
	}

    public Conference(String name, String theme, Double price, String start, String end, String speakersNames, String description) {
        this.name = name;
        this.theme = theme;
        this.price = price;
        this.popularity = 0.0;
        this.start = start;
        this.end = end;
        this.speakersNames = speakersNames;
        this.description = description;
        this.organizators = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSpeakersNames() {
        return speakersNames;
    }

    public void setSpeakersNames(String speakersNames) {
        this.speakersNames = speakersNames;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // -----------------------------------------------------------------------------------------------------------------

	@JsonView(Detailed.class)
	private List<String> events;
	@NotNull
	@JsonView(Detailed.class)
	private List<String> organizators;

	public List<String> getEvents() {
		return events;
	}

	public void setEvents(List<String> events) {
		this.events = events;
	}

	public List<String> getOrganizators() {
		return organizators;
	}

	public void setOrganizators(List<String> organizators) {
		this.organizators = organizators;
	}
}
