
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Segment extends DomainEntity {

	private String	origen;
	private String	destino;
	private Date	expectedTimeOrigin;
	private Date	expectedTimeDestination;


	@NotBlank
	public String getOrigen() {
		return this.origen;
	}

	public void setOrigen(final String origen) {
		this.origen = origen;
	}

	@NotBlank
	public String getDestino() {
		return this.destino;
	}

	public void setDestino(final String destino) {
		this.destino = destino;
	}

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

}
