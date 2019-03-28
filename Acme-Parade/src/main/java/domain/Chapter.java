
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Chapter extends Actor {

	private String	title;
	private Zone	zone;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Valid
	@OneToOne(optional = true)
	public Zone getZone() {
		return this.zone;
	}

	public void setZone(final Zone zone) {
		this.zone = zone;
	}

}
