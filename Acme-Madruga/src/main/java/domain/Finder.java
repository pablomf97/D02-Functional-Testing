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

	private String keyWord;
	private String area;
	private Date minimumMoment;
	private Date maximumMoment;
	private Date searchMoment;
	private Collection<Procession> searchResults;

	/* Getters&Setters */

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMinimumMoment() {
		return minimumMoment;
	}

	public void setMinimumMoment(Date minimumMoment) {
		this.minimumMoment = minimumMoment;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMaximumMoment() {
		return maximumMoment;
	}

	public void setMaximumMoment(Date maximumMoment) {
		this.maximumMoment = maximumMoment;
	}

	@Past
	public Date getSearchMoment() {
		return searchMoment;
	}

	public void setSearchMoment(Date searchMoment) {
		this.searchMoment = searchMoment;
	}

	@Valid
	@ManyToMany
	public Collection<Procession> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(Collection<Procession> searchResults) {
		this.searchResults = searchResults;
	}

}
