package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Administrator;
import forms.AdministratorForm;

@Service
@Transactional
public class AdministratorService {

	@Autowired
	private ActorService actorService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private AdministratorRepository administratorRepository;
	@Autowired
	private Validator validator;
	@Autowired
	private MessageBoxService messageBoxService;

	/**
	 * Create a new empty admin
	 * 
	 * @return admin
	 */
	public Administrator create() {

		final Administrator res = new Administrator();
		final UserAccount a = this.userAccountService.create();

		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMINISTRATOR);
		a.addAuthority(auth);
		res.setUserAccount(a);

		return res;
	}

	public AdministratorForm createForm() {
		Assert.isTrue(this.actorService.checkAuthority(
				this.actorService.findByPrincipal(), "ADMINISTRATOR"));
		final AdministratorForm res = new AdministratorForm();
		return res;
	}

	/**
	 * Find one admin by id
	 * 
	 * @param id
	 * @return admin
	 */
	public Administrator findOne(final int id) {
		Administrator res;
		Assert.isTrue(id != 0);
		res = this.administratorRepository.findOne(id);
		Assert.notNull(res, "The brotherhood does not exist");
		return res;
	}

	/**
	 * Find all admins
	 * 
	 * @return Collection<administrator>
	 */
	public Collection<Administrator> findAll() {
		return this.administratorRepository.findAll();
	}

	/**
	 * Save a new admin or edit a existing admin
	 * 
	 * @param admin
	 * @return admin
	 */
	public Administrator save(final Administrator admin) {
		Administrator result;
		if (admin.getId() == 0) {
			final UserAccount account = admin.getUserAccount();
			final Authority au = new Authority();
			au.setAuthority(Authority.ADMINISTRATOR);
			Assert.isTrue(account.getAuthorities().contains(au),
					"You can not register with this authority");
			final UserAccount savedAccount = this.userAccountService
					.save(account);
			admin.setUserAccount(savedAccount);
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String hash = encoder.encodePassword(admin.getUserAccount()
					.getPassword(), null);
			admin.getUserAccount().setPassword(hash);
			result = this.administratorRepository.save(admin);
			this.messageBoxService.initializeDefaultBoxes(result);
		} else {
			final UserAccount userAccount = LoginService.getPrincipal();
			final Administrator adminBD = this.administratorRepository
					.findOne(admin.getId());
			Assert.isTrue(admin.getUserAccount().equals(userAccount)
					|| adminBD.getUserAccount().equals(userAccount),
					"This account does not belong to you");
			result = this.administratorRepository.save(admin);
		}
		return result;
	}

	/**
	 * Remove the admin
	 * 
	 * @param id
	 */
	public void delete(final int id) {
		final Administrator brotherhood = this.administratorRepository
				.findOne(id);
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(brotherhood.getUserAccount().equals(userAccount),
				"This account does not belong to you");
		this.administratorRepository.delete(id);
	}

	/**
	 * Change the incomplete administrator to an domain object
	 * 
	 * @param administrator
	 * @param binding
	 * @return administrator
	 */
	public Administrator reconstruct(final AdministratorForm administratorForm,
			final BindingResult binding) {
		Administrator result = this.create();
		if (administratorForm.getId() == 0) {
			result.getUserAccount()
					.setUsername(administratorForm.getUsername());
			result.getUserAccount()
					.setPassword(administratorForm.getPassword());
			result.setAddress(administratorForm.getAddress());
			result.setEmail(administratorForm.getEmail());
			result.setMiddleName(administratorForm.getMiddleName());
			result.setName(administratorForm.getName());
			result.setPhoneNumber(administratorForm.getPhoneNumber());
			result.setPhoto(administratorForm.getPhoto());
			result.setSurname(administratorForm.getSurname());
			this.validator.validate(administratorForm, binding);

		} else {
			result = this.administratorRepository.findOne(administratorForm
					.getId());
			Assert.notNull(result);
			if (this.checkValidation(administratorForm, binding, result)) {
				result.setAddress(administratorForm.getAddress());
				result.setEmail(administratorForm.getEmail());
				result.setMiddleName(administratorForm.getMiddleName());
				result.setName(administratorForm.getName());
				result.setPhoneNumber(administratorForm.getPhoneNumber());
				result.setPhoto(administratorForm.getPhoto());
				result.setSurname(administratorForm.getSurname());
			} else {
				result = this.create();
				result.setAddress(administratorForm.getAddress());
				result.setEmail(administratorForm.getEmail());
				result.setMiddleName(administratorForm.getMiddleName());
				result.setName(administratorForm.getName());
				result.setPhoneNumber(administratorForm.getPhoneNumber());
				result.setPhoto(administratorForm.getPhoto());
				result.setSurname(administratorForm.getSurname());
				result.setPhoto(administratorForm.getPhoto());
				result.setId(administratorForm.getId());
			}
		}
		return result;
	}

	private boolean checkValidation(final AdministratorForm administratorForm,
			final BindingResult binding, final Administrator brotherhood) {
		boolean check = true;
		administratorForm.setCheckBox(true);
		administratorForm.setPassword(brotherhood.getUserAccount()
				.getPassword());
		administratorForm.setPassword2(brotherhood.getUserAccount()
				.getPassword());
		administratorForm.setUsername(brotherhood.getUserAccount()
				.getUsername());
		this.validator.validate(administratorForm, binding);
		if (binding.hasErrors())
			check = false;
		return check;
	}
}
