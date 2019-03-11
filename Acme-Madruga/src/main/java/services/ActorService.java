package services;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import forms.ActorForm;

@Service
@Transactional
public class ActorService {

	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	public ActorForm createForm() {

		final ActorForm res = new ActorForm();

		return res;
	}

	/**
	 * Get all actors from db
	 * 
	 * @return Collection<Actor>
	 */
	public Collection<Actor> findAll() {
		Collection<Actor> result = null;
		result = this.actorRepository.findAll();
		return result;
	}

	/**
	 * Find an actor by id in the db
	 * 
	 * @param actorId
	 * @return actor
	 */
	public Actor findOne(final int actorId) {
		final Actor result = this.actorRepository.findOne(actorId);
		return result;
	}

	/**
	 * Find the logged actor
	 * 
	 * @return actor
	 */

	public Actor findByPrincipal() {
		Actor result = null;
		final UserAccount userAccount = LoginService.getPrincipal();
		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}

	/**
	 * Validate email pattern
	 * 
	 * @param email
	 * @return messageCode
	 */
	public String checkEmail(final String email, final String authority) {
		String result = "";
		final Pattern pattern = Pattern
				.compile("(^(([a-z]|[0-9]){1,}[@]{1}([a-z]|[0-9]){1,}([.]{1}([a-z]|[0-9]){1,}){1,})$)|(^((([a-z]|[0-9]){1,}[ ]{1}){1,}<(([a-z]|[0-9]){1,}[@]{1}([a-z]|[0-9]){1,}([.]{1}([a-z]|[0-9]){1,}){1,})>)$)");
		final Matcher matcher = pattern.matcher(email);
		if (authority.equals("ADMININISTRATOR") && matcher.matches()) {
			// TODO: faltaría comprobar si se intenta insertar un admin y que
			// compruebe su correo para su caso
			final Pattern patternAdmin = Pattern
					.compile("(^((([a-z]|[0-9]){1,}[@])$)|(^(([a-z]|[0-9]){1,}[ ]{1}){1,}<(([a-z]|[0-9]){1,}[@]>))$)");
			final Matcher matcherAdmin = patternAdmin.matcher(email);
			result = matcherAdmin.matches() ? "" : "actor.email.error";
		} else
			result = matcher.matches() ? "" : "actor.email.error";
		return result;
	}

	/**
	 * Validate the phone code country
	 * 
	 * @param phoneNumber
	 * @return phoneNumber
	 */
	public String checkSetPhoneCC(String phoneNumber) {
		final Pattern p = Pattern.compile("([0-9]{4}){1}([0-9]{0,})");
		final Matcher m = p.matcher(phoneNumber);
		final boolean b = m.matches();
		if (b)
			phoneNumber = this.systemConfigurationService.findAll().iterator()
					.next().getCountryCode()
					+ " " + phoneNumber;
		return phoneNumber;
	}

	/**
	 * Check the authority of the actor
	 * 
	 * @param actor
	 * @param authority
	 * @return boolean
	 */
	public boolean checkAuthority(final Actor actor, final String authority) {
		Assert.notNull(actor);
		boolean result = false;
		if (actor.getUserAccount().getAuthorities().iterator().next()
				.getAuthority().equals(authority))
			result = true;
		return result;
	}

	/**
	 * Check the terms and consitions acceptance of the actor
	 * 
	 * @param actor
	 * @param authority
	 * @return boolean
	 */
	public String checkLaw(final Boolean law) {
		String result = "";
		if (law != true)
			result = "actor.check.law";
		return result;
	}

	/**
	 * Check the password of the actor
	 * 
	 * @param actor
	 * @param authority
	 * @return boolean
	 */
	public String checkPass(final String password1, final String password2) {
		String result = "";
		if (!password1.equals(password2))
			result = "actor.check.passW";
		return result;
	}

	public String checkUniqueUser(final String username) {
		String result = "";
		if (this.userAccountRepository.findByUsername(username) != null)
			result = "actor.check.unique.user";
		return result;
	}

	public Collection<Actor> findAllExceptPrincipal() {
		Collection<Actor> result;
		Actor principal;

		result = this.actorRepository.findAll();
		Assert.notNull(result);

		principal = this.findByPrincipal();
		Assert.notNull(principal);

		result.remove(principal);
		return result;
	}
	
	public Actor findBySocialProfileId(int socialProfileId) {
		Actor result = this.actorRepository.findBySocialProfileId(socialProfileId);
		Assert.notNull(result);
		
		return result;
	}

}