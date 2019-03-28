package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class SocialProfile extends DomainEntity {

	/* Attributes */

	private String nick;
	private String name;
	private String linkProfile;
	private Actor actor;

	/* Getters&Setters */

	@NotBlank
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	@URL
	public String getLinkProfile() {
		return linkProfile;
	}

	public void setLinkProfile(String linkProfile) {
		this.linkProfile = linkProfile;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	@NotFound(action=NotFoundAction.IGNORE)
	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

}
