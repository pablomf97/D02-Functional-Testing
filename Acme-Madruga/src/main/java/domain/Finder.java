
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity {

	/* Attributes */

	private String				keyWord;
	private String				area;
	private Date				minimumMoment;
	private Date				maximumMoment;
	private Date				searchMoment;
	private Collection<Parade>	searchResults;


	/* Getters&Setters */

	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(final String keyWord) {
		this.keyWord = keyWord;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(final String area) {
		this.area = area;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMinimumMoment() {
		return this.minimumMoment;
	}

	public void setMinimumMoment(final Date minimumMoment) {
		this.minimumMoment = minimumMoment;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMaximumMoment() {
		return this.maximumMoment;
	}

	public void setMaximumMoment(final Date maximumMoment) {
		this.maximumMoment = maximumMoment;
	}

	@Past
	public Date getSearchMoment() {
		return this.searchMoment;
	}

	public void setSearchMoment(final Date searchMoment) {
		this.searchMoment = searchMoment;
	}

	@Valid
	@ManyToMany
	public Collection<Parade> getSearchResults() {
		return this.searchResults;
	}

	public void setSearchResults(final Collection<Parade> searchResults) {
		this.searchResults = searchResults;
	}

}
