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

	private String systemName;
	private Map<String, String> welcomeMessage;
	private String banner;
	private String countryCode;
	private int timeResultsCached;
	private int maxResults;
	private String messagePriority;
	private String spamWords;
	private String possitiveWords;
	private String negativeWords;
	private Map<String,String> breachNotification;

	/* Getters&Setters */
	
	@ElementCollection
	public Map<String, String> getBreachNotification() {
		return breachNotification;
	}

	public void setBreachNotification(Map<String, String> breachNotification) {
		this.breachNotification = breachNotification;
	}
	
	
	@NotBlank
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@NotNull
	@NotEmpty
	@ElementCollection
	public Map<String, String> getWelcomeMessage() {
		return welcomeMessage;
	}

	public void setWelcomeMessage(Map<String, String> welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	@URL
	@NotBlank
	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	@NotBlank
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Range(min = 1, max = 24)
	public int getTimeResultsCached() {
		return timeResultsCached;
	}

	public void setTimeResultsCached(int timeResultsCached) {
		this.timeResultsCached = timeResultsCached;
	}

	@Range(min = 0, max = 100)
	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	@NotBlank
	public String getMessagePriority() {
		return messagePriority;
	}

	public void setMessagePriority(String messagePriority) {
		this.messagePriority = messagePriority;
	}

	@NotBlank
	public String getSpamWords() {
		return spamWords;
	}

	public void setSpamWords(String spamWords) {
		this.spamWords = spamWords;
	}

	@NotBlank
	public String getPossitiveWords() {
		return possitiveWords;
	}

	public void setPossitiveWords(String possitiveWords) {
		this.possitiveWords = possitiveWords;
	}

	@NotBlank
	public String getNegativeWords() {
		return negativeWords;
	}

	public void setNegativeWords(String negativeWords) {
		this.negativeWords = negativeWords;
	}

}
