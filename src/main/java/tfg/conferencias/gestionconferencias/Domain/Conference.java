package tfg.conferencias.gestionconferencias.Domain;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "conference")
public class Conference extends DomainEntity {

	public Conference(String name, Double duration, String theme, Double price, Double popularity, Date start,
			Date end) {
		super();
		this.name = name;
		this.duration = duration;
		this.theme = theme;
		this.price = price;
		this.popularity = popularity;
		this.start = start;
		this.end = end;
	}

	@NotBlank
	private String name;
	@NotNull
	private Double duration;
	@NotBlank
	private String theme;
	@NotNull
	private Double price;
	@NotNull
	private Double popularity;
	@NotNull
	private Date start;
	@NotNull
	private Date end;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
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

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}
