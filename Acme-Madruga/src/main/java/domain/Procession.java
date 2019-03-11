
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "ticker,title,description,organisedMoment,isDraft,brotherhood")
})
public class Procession extends DomainEntity {

	/* Attributes */
	private String					ticker;
	private String					title;
	private String					description;
	private Date					organisedMoment;
	private Integer					maxCols;
	private boolean					isDraft;
	// Float es un tipo ya predefinido en java, por lo que por ahora Float pasa
	// a ser Platform.
	private Collection<Platform>	platforms;
	private Brotherhood				brotherhood;


	/* Getters&Setters */

	@NotBlank
	@Pattern(regexp = "\\d{6}-[A-Z]{5}")
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

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
	@Future
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getOrganisedMoment() {
		return this.organisedMoment;
	}

	public void setOrganisedMoment(final Date organisedMoment) {
		this.organisedMoment = organisedMoment;
	}

	@NotNull
	@Min(value = 1)
	public Integer getMaxCols() {
		return this.maxCols;
	}

	public void setMaxCols(final Integer maxCols) {
		this.maxCols = maxCols;
	}

	public boolean getIsDraft() {
		return this.isDraft;
	}

	public void setIsDraft(final boolean isDraft) {
		this.isDraft = isDraft;
	}

	@Valid
	@ManyToMany
	public Collection<Platform> getPlatforms() {
		return this.platforms;
	}

	public void setPlatforms(final Collection<Platform> platforms) {
		this.platforms = platforms;
	}

	@Valid
	@ManyToOne(optional = false)
	public Brotherhood getBrotherhood() {
		return this.brotherhood;
	}

	public void setBrotherhood(final Brotherhood brotherhood) {
		this.brotherhood = brotherhood;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.brotherhood == null) ? 0 : this.brotherhood.hashCode());
		result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = prime * result + (this.isDraft ? 1231 : 1237);
		result = prime * result + ((this.organisedMoment == null) ? 0 : this.organisedMoment.hashCode());
		result = prime * result + ((this.platforms == null) ? 0 : this.platforms.hashCode());
		result = prime * result + ((this.ticker == null) ? 0 : this.ticker.hashCode());
		result = prime * result + ((this.title == null) ? 0 : this.title.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Procession other = (Procession) obj;
		if (this.brotherhood == null) {
			if (other.brotherhood != null)
				return false;
		} else if (!this.brotherhood.equals(other.brotherhood))
			return false;
		if (this.description == null) {
			if (other.description != null)
				return false;
		} else if (!this.description.equals(other.description))
			return false;
		if (this.isDraft != other.isDraft)
			return false;
		if (this.organisedMoment == null) {
			if (other.organisedMoment != null)
				return false;
		} else if (!this.organisedMoment.equals(other.organisedMoment))
			return false;
		if (this.platforms == null) {
			if (other.platforms != null)
				return false;
		} else if (!this.platforms.equals(other.platforms))
			return false;
		if (this.ticker == null) {
			if (other.ticker != null)
				return false;
		} else if (!this.ticker.equals(other.ticker))
			return false;
		if (this.title == null) {
			if (other.title != null)
				return false;
		} else if (!this.title.equals(other.title))
			return false;
		return true;
	}

}
