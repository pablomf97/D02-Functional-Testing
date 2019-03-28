
package forms;

import java.util.Collection;

import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

import domain.Zone;

public class BrotherhoodForm extends ActorForm {

	private String				title;
	private Collection<String>	pictures;
	private Zone				zone;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public Collection<String> getPictures() {
		return this.pictures;
	}

	public void setPictures(final Collection<String> pictures) {
		this.pictures = pictures;
	}

	@Valid
	@ManyToOne(optional = true)
	public Zone getZone() {
		return this.zone;
	}

	public void setZone(final Zone zone) {
		this.zone = zone;
	}

}
