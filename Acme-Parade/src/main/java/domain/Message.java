package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Message extends DomainEntity {

	/* Attributes */

	private Date sentMoment;
	private String subject;
	private String body;
	private String priority;
	private String tags;
	private Collection<MessageBox> messageBoxes;
	private Actor sender;
	private Actor recipient;
	private boolean isSpam;
	
	/* Getters&Setters */

	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getSentMoment() {
		return sentMoment;
	}

	public void setSentMoment(Date sentMoment) {
		this.sentMoment = sentMoment;
	}

	@NotBlank
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@NotBlank
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@NotBlank
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Valid
	@NotNull
	@ManyToMany
	public Collection<MessageBox> getMessageBoxes() {
		return messageBoxes;
	}

	public void setMessageBoxes(Collection<MessageBox> messageBoxes) {
		this.messageBoxes = messageBoxes;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	@NotFound(action=NotFoundAction.IGNORE)
	public Actor getSender() {
		return sender;
	}

	public void setSender(Actor sender) {
		this.sender = sender;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	@NotFound(action=NotFoundAction.IGNORE)
	public Actor getRecipient() {
		return recipient;
	}

	public void setRecipient(Actor recipient) {
		this.recipient = recipient;
	}

	public boolean getIsSpam() {
		return isSpam;
	}

	public void setIsSpam(boolean isSpam) {
		this.isSpam = isSpam;
	}
	
	

}
