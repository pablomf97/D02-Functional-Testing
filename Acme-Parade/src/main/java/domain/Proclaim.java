
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Proclaim extends DomainEntity {

	private Chapter	chapter;
	private Date	publishedMoment;
	private String	description;


	@Valid
	@ManyToOne(optional = false)
	public Chapter getChapter() {
		return this.chapter;
	}

	public void setChapter(final Chapter chapter) {
		this.chapter = chapter;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getPublishedMoment() {
		return this.publishedMoment;
	}

	public void setPublishedMoment(final Date publishedMoment) {
		this.publishedMoment = publishedMoment;
	}

	@NotBlank
	@Size(max = 250)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
}
