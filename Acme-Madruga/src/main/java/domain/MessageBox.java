package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class MessageBox extends DomainEntity {

	/* Attributes */

	private String name;
	private boolean isPredefined;
	private MessageBox parentMessageBoxes;
	private Collection<Message> messages;
	private Actor owner;

	/* Getters&Setters */

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean getIsPredefined() {
		return this.isPredefined;
	}

	public void setIsPredefined(final boolean isPredefined) {
		this.isPredefined = isPredefined;
	}

	@Valid
	@ManyToOne
	public Actor getOwner() {
		return this.owner;
	}

	public void setOwner(final Actor owner) {
		this.owner = owner;
	}

	@Valid
	@ManyToOne(optional = true)

	public MessageBox getParentMessageBoxes() {
		return this.parentMessageBoxes;
	}

	public void setParentMessageBoxes(final MessageBox parentMessageBoxes) {
		this.parentMessageBoxes = parentMessageBoxes;
	}

	@Valid
	@ManyToMany
	public Collection<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(final Collection<Message> messages) {
		this.messages = messages;
	}

}
