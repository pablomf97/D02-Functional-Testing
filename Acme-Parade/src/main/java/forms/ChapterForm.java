package forms;

import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

import domain.Zone;

public class ChapterForm extends ActorForm {

	private String title;
	private Zone zone;

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
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
