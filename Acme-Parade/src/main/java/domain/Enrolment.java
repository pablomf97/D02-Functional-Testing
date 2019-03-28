package domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Enrolment extends DomainEntity {

	/* Atributos */

	private Date moment;
	private List<Date> dropOutMoment;
	
	// Tipo envoltura ya que se haran comprobaciones en servicios para ciertas
	// operaciones
	private Boolean isOut;
	private Member member;
	private Brotherhood brotherhood;
	private Position position;

	/* Getters&Setters */

	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	@ElementCollection
	public List<Date> getDropOutMoment() {
		return dropOutMoment;
	}

	public void setDropOutMoment(List<Date> dropOutMoment) {
		this.dropOutMoment = dropOutMoment;
	}

	public Boolean getIsOut() {
		return isOut;
	}

	public void setIsOut(Boolean isOut) {
		this.isOut = isOut;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Brotherhood getBrotherhood() {
		return brotherhood;
	}

	public void setBrotherhood(Brotherhood brotherhood) {
		this.brotherhood = brotherhood;
	}

	@Valid
	@ManyToOne(optional = true)
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

}