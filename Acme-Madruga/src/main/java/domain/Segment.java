
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Segment extends DomainEntity {

	
	private Date	expectedTimeOrigin;
	private Date	expectedTimeDestination;
	private Parade	parade;
	private Coordinate origin;
	private Coordinate destination;
	private boolean isEditable;

	@Future
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getExpectedTimeOrigin() {
		return this.expectedTimeOrigin;
	}

	public void setExpectedTimeOrigin(final Date expectedTimeOrigin) {
		this.expectedTimeOrigin = expectedTimeOrigin;
	}

	@Future
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

	@Valid
	@NotNull
	@OneToOne(optional=false)
	public Coordinate getOrigin() {
		return origin;
	}

	public void setOrigin(Coordinate origin) {
		this.origin = origin;
	}

	@Valid
	@NotNull
	@OneToOne(optional=false)
	public Coordinate getDestination() {
		return destination;
	}

	public void setDestination(Coordinate destination) {
		this.destination = destination;
	}

	public boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	
	
}
