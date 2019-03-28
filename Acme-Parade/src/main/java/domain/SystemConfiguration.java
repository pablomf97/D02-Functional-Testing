
package domain;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class SystemConfiguration extends DomainEntity {

	/* Attributes */

	private String				systemName;
	private Map<String, String>	welcomeMessage;
	private String				banner;
	private String				countryCode;
	private int					timeResultsCached;
	private int					maxResults;
	private String				messagePriority;
	private String				spamWords;
	private String				possitiveWords;
	private String				negativeWords;
	private Map<String, String>	breachNotification;
	private String				makers;
	private Double				fare;
	private Double				VAT;


	/* Getters&Setters */

	@ElementCollection
	public Map<String, String> getBreachNotification() {
		return this.breachNotification;
	}

	public void setBreachNotification(final Map<String, String> breachNotification) {
		this.breachNotification = breachNotification;
	}

	@NotBlank
	public String getSystemName() {
		return this.systemName;
	}

	public void setSystemName(final String systemName) {
		this.systemName = systemName;
	}

	@NotNull
	@NotEmpty
	@ElementCollection
	public Map<String, String> getWelcomeMessage() {
		return this.welcomeMessage;
	}

	public void setWelcomeMessage(final Map<String, String> welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	@URL
	@NotBlank
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	@NotBlank
	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}

	@Range(min = 1, max = 24)
	public int getTimeResultsCached() {
		return this.timeResultsCached;
	}

	public void setTimeResultsCached(final int timeResultsCached) {
		this.timeResultsCached = timeResultsCached;
	}

	@Range(min = 0, max = 100)
	public int getMaxResults() {
		return this.maxResults;
	}

	public void setMaxResults(final int maxResults) {
		this.maxResults = maxResults;
	}

	@NotBlank
	public String getMessagePriority() {
		return this.messagePriority;
	}

	public void setMessagePriority(final String messagePriority) {
		this.messagePriority = messagePriority;
	}

	@NotBlank
	public String getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final String spamWords) {
		this.spamWords = spamWords;
	}

	@NotBlank
	public String getPossitiveWords() {
		return this.possitiveWords;
	}

	public void setPossitiveWords(final String possitiveWords) {
		this.possitiveWords = possitiveWords;
	}

	@NotBlank
	public String getNegativeWords() {
		return this.negativeWords;
	}

	public void setNegativeWords(final String negativeWords) {
		this.negativeWords = negativeWords;
	}

	@NotBlank
	public String getMakers() {
		return this.makers;
	}

	public void setMakers(final String makers) {
		this.makers = makers;
	}

	@NotNull
	public Double getFare() {
		return this.fare;
	}

	public void setFare(final Double fare) {
		this.fare = fare;
	}

	@NotNull
	@Range(max = 1)
	public Double getVAT() {
		return this.VAT;
	}

	public void setVAT(final Double vAT) {
		this.VAT = vAT;
	}
}
