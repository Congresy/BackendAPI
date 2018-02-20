package com.conferencias.tfg.domain;

import java.util.Calendar;
import java.util.Date;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.conferencias.tfg.utilities.Views.Detailed;
import com.conferencias.tfg.utilities.Views.Shorted;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Document
public class Conference {

	@Id
	@JsonIgnore
	private Integer id;
	@JsonView(Shorted.class)
	private String name;
	@JsonView(Shorted.class)
	private String theme;
	@JsonView(Detailed.class)
	private Date start;
	@JsonView(Detailed.class)
	private Date finish;
	@JsonView(Detailed.class)
	private Double price;
	@JsonView(Detailed.class)
	private Address place;
	@JsonView(Shorted.class)
	private String talker;
	@JsonView(Shorted.class)
	private Integer duration;
	@JsonView(Detailed.class)
	@URL
	private String video;

	public Conference() {

	}

	public Conference(String name, String theme, Date start, Date finish, Double price, Address place, String talker) {
		super();
		this.id = ObjectId.getGeneratedProcessIdentifier();
		this.name = name;
		this.theme = theme;
		this.start = start;
		this.finish = finish;
		this.price = price;
		this.place = place;
		this.talker = talker;
		this.duration = getDuration();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getPlace() {
		return place;
	}

	public void setPlace(Address place) {
		this.place = place;
	}

	public String getTalker() {
		return talker;
	}

	public void setTalker(String talker) {
		this.talker = talker;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getFinish() {
		return finish;
	}

	public void setFinish(Date finish) {
		this.finish = finish;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getDuration() {
		Calendar start = Calendar.getInstance();
		start.setTime(this.start);
		Calendar finish = Calendar.getInstance();
		finish.setTime(this.finish);
		return (int) ((finish.getTimeInMillis() - start.getTimeInMillis()) / (24 * 60 * 60 * 1000));
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

}
