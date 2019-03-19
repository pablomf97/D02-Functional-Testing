
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
public class Parade extends DomainEntity {

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
	//new atributte
	private String					status;
	private String					reason;
	private Path					path;


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

	//new get and set
	@NotBlank
	@Pattern(regexp = "\\b(SUBMITTED|ACCEPTED|REJECTED)\\b")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(final String reason) {
		this.reason = reason;
	}

	@Valid
	@OneToOne(optional = true)
	public Path getPath() {
		return this.path;
	}

	public void setPath(final Path path) {
		this.path = path;
	}
}
