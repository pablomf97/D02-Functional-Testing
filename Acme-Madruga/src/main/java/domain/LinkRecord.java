package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class LinkRecord extends DomainEntity {

	private String title;
	private String description;
	private Brotherhood linkedBrotherhood;

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

	@Valid
	@NotNull
	@OneToOne(optional=false)
	public Brotherhood getLinkedBrotherhood() {
		return linkedBrotherhood;
	}

	public void setLinkedBrotherhood(Brotherhood linkedBrotherhood) {
		this.linkedBrotherhood = linkedBrotherhood;
	}

}
