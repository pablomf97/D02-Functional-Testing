
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class PeriodRecord extends DomainEntity {

	private String	title;
	private String	description;
	private Integer	startYear;
	private Integer	endYear;
	private String	photos;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	public Integer getStartYear() {
		return this.startYear;
	}

	public void setStartYear(final Integer startYear) {
		this.startYear = startYear;
	}

	@NotNull
	public Integer getEndYear() {
		return this.endYear;
	}

	public void setEndYear(final Integer endYear) {
		this.endYear = endYear;
	}

	@NotBlank
	public String getPhotos() {
		return this.photos;
	}

	public void setPhotos(final String photos) {
		this.photos = photos;
	}

}
