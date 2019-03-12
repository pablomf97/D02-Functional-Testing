
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class LinkRecord extends DomainEntity {

	private String	title;
	private String	description;
	private String	linkedBrotherhood;


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

	@NotBlank
	public String getLinkedBrotherhood() {
		return this.linkedBrotherhood;
	}

	public void setLinkedBrotherhood(final String linkedBrotherhood) {
		this.linkedBrotherhood = linkedBrotherhood;
	}

}
