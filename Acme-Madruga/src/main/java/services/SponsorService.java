package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;


import domain.Sponsor;


import forms.SponsorForm;

import repositories.SponsorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class SponsorService {


	@Autowired
	private ActorService actorService;
	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private SponsorRepository sponsorRepository;

	@Autowired
	private Validator validator;
	@Autowired
	private MessageBoxService messageBoxService;


	public Sponsor create() {

		final Sponsor res = new Sponsor();
		final UserAccount a = this.userAccountService.create();

		final Authority auth = new Authority();
		auth.setAuthority(Authority.SPONSOR);
		a.addAuthority(auth);
		res.setUserAccount(a);

		return res;
	}

	public SponsorForm createForm() {
		Assert.isTrue(this.actorService.checkAuthority(
				this.actorService.findByPrincipal(), "SPONSOR"));
		final SponsorForm res = new SponsorForm();
		return res;
	}

	public Sponsor findOne(final int id) {
		Sponsor res;
		Assert.isTrue(id != 0);
		res = this.sponsorRepository.findOne(id);
		Assert.notNull(res, "The sponsor does not exist");
		return res;
	}

	public Collection<Sponsor> findAll() {
		return this.sponsorRepository.findAll();
	}
	public Sponsor save(final Sponsor sponsor) {
		Sponsor result;
		if (sponsor.getId() == 0) {
			final UserAccount account = sponsor.getUserAccount();
			final Authority au = new Authority();
			au.setAuthority(Authority.SPONSOR);
			Assert.isTrue(account.getAuthorities().contains(au),
					"You can not register with this authority");
			final UserAccount savedAccount = this.userAccountService
					.save(account);
			sponsor.setUserAccount(savedAccount);
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String hash = encoder.encodePassword(sponsor.getUserAccount()
					.getPassword(), null);
			sponsor.getUserAccount().setPassword(hash);
			result = this.sponsorRepository.save(sponsor);
			this.messageBoxService.initializeDefaultBoxes(result);
		} else {
			final UserAccount userAccount = LoginService.getPrincipal();
			final Sponsor sponsorDB = this.sponsorRepository
					.findOne(sponsor.getId());
			Assert.isTrue(sponsor.getUserAccount().equals(userAccount)
					|| sponsorDB.getUserAccount().equals(userAccount),
					"This account does not belong to you");
			result = this.sponsorRepository.save(sponsor);
		}
		return result;
	}
	
	public void delete(final int id) {
		final Sponsor brotherhood = this.sponsorRepository
				.findOne(id);
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(brotherhood.getUserAccount().equals(userAccount),
				"This account does not belong to you");
		this.sponsorRepository.delete(id);
	}
	public Sponsor reconstruct(final SponsorForm sponsorForm,
			final BindingResult binding) {
		Sponsor result = this.create();
		if (sponsorForm.getId() == 0) {
			result.getUserAccount()
					.setUsername(sponsorForm.getUsername());
			result.getUserAccount()
					.setPassword(sponsorForm.getPassword());
			result.setAddress(sponsorForm.getAddress());
			result.setEmail(sponsorForm.getEmail());
			result.setMiddleName(sponsorForm.getMiddleName());
			result.setName(sponsorForm.getName());
			result.setPhoneNumber(sponsorForm.getPhoneNumber());
			result.setPhoto(sponsorForm.getPhoto());
			result.setSurname(sponsorForm.getSurname());
			this.validator.validate(sponsorForm, binding);

		} else {
			result = this.sponsorRepository.findOne(sponsorForm
					.getId());
			Assert.notNull(result);
			if (this.checkValidation(sponsorForm, binding, result)) {
				result.setAddress(sponsorForm.getAddress());
				result.setEmail(sponsorForm.getEmail());
				result.setMiddleName(sponsorForm.getMiddleName());
				result.setName(sponsorForm.getName());
				result.setPhoneNumber(sponsorForm.getPhoneNumber());
				result.setPhoto(sponsorForm.getPhoto());
				result.setSurname(sponsorForm.getSurname());
			} else {
				result = this.create();
				result.setAddress(sponsorForm.getAddress());
				result.setEmail(sponsorForm.getEmail());
				result.setMiddleName(sponsorForm.getMiddleName());
				result.setName(sponsorForm.getName());
				result.setPhoneNumber(sponsorForm.getPhoneNumber());
				result.setPhoto(sponsorForm.getPhoto());
				result.setSurname(sponsorForm.getSurname());
				result.setPhoto(sponsorForm.getPhoto());
				result.setId(sponsorForm.getId());
			}
		}
		return result;
	}
	private boolean checkValidation(final SponsorForm sponsorForm,
			final BindingResult binding, final Sponsor sponsor) {
		boolean check = true;
		sponsorForm.setCheckBox(true);
		sponsorForm.setPassword(sponsor.getUserAccount()
				.getPassword());
		sponsorForm.setPassword2(sponsor.getUserAccount()
				.getPassword());
		sponsorForm.setUsername(sponsor.getUserAccount()
				.getUsername());
		this.validator.validate(sponsorForm, binding);
		if (binding.hasErrors())
			check = false;
		return check;
	}

}
