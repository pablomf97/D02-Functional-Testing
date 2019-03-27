
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Segment extends DomainEntity {

	
	private Date	expectedTimeOrigin;
	private Date	expectedTimeDestination;
	private Parade	parade;
	private Double 	originLatitude;
	private Double 	originLongitude;
	private Double 	destinationLatitude;
	private Double 	destinationLongitude;
	private boolean isEditable;

	//@Future
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getExpectedTimeOrigin() {
		return this.expectedTimeOrigin;
	}

	public void setExpectedTimeOrigin(final Date expectedTimeOrigin) {
		this.expectedTimeOrigin = expectedTimeOrigin;
	}

	//@Future
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getExpectedTimeDestination() {
		return this.expectedTimeDestination;
	}

	public void setExpectedTimeDestination(final Date expectedTimeDestination) {
		this.expectedTimeDestination = expectedTimeDestination;
	}

	@Valid
	@NotNull
	@ManyToOne(optional=false)
	public Parade getParade() {
		return parade;
	}

	public void setParade(Parade parade) {
		this.parade = parade;
	}

	@NotNull
	@Range(min=-90,max=90)
	public Double getOriginLatitude() {
		return originLatitude;
	}

	public void setOriginLatitude(Double originLatitude) {
		this.originLatitude = originLatitude;
	}

	@NotNull
	@Range(min=-180,max=180)
	public Double getOriginLongitude() {
		return originLongitude;
	}

	public void setOriginLongitude(Double originLongitude) {
		this.originLongitude = originLongitude;
	}

	@NotNull
	@Range(min=-90,max=90)
	public Double getDestinationLatitude() {
		return destinationLatitude;
	}

	public void setDestinationLatitude(Double destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}

	@NotNull
	@Range(min=-180,max=180)
	public Double getDestinationLongitude() {
		return destinationLongitude;
	}

	public void setDestinationLongitude(Double destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}

	public boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	
	
}
