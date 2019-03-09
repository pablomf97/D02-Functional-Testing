
package forms;

import javax.persistence.Column;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import domain.DomainEntity;

public class ActorForm extends DomainEntity {

	/* Attributes */

	private String	name;
	private String	middleName;
	private String	surname;
	private String	photo;
	private String	email;
	private String	phoneNumber;
	private String	address;
	private String	username;
	private String	password;
	private String	password2;
	private Boolean	checkBox;


	/* Getters&Setters */
	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	@NotBlank
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotBlank
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean getCheckBox() {
		return this.checkBox;
	}

	public void setCheckBox(final Boolean checkBox) {
		this.checkBox = checkBox;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@NotBlank
	@Size(min = 5, max = 32)
	@Column(unique = true)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}
	@NotBlank
	@Size(min = 5, max = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}
	@NotBlank
	@Size(min = 5, max = 32)
	public String getPassword2() {
		return this.password2;
	}

	public void setPassword2(final String password2) {
		this.password2 = password2;
	}

}
