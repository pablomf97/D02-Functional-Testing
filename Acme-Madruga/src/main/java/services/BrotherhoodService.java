package services;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BrotherhoodRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Zone;
import forms.BrotherhoodForm;

@Service
@Transactional
public class BrotherhoodService {

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private BrotherhoodRepository brotherhoodRepository;
	@Autowired
	private Validator validator;

	@Autowired
	private EnrolmentService enrolmentService;

	@Autowired
	private MessageBoxService messageBoxService;

	@Autowired
	private ZoneService zoneService;

	/**
	 * Create a new empty brotherhood
	 * 
	 * @return brotherhood
	 */
	public Brotherhood create() {

		final Brotherhood res = new Brotherhood();
		final UserAccount a = this.userAccountService.create();

		final Authority auth = new Authority();
		auth.setAuthority(Authority.BROTHERHOOD);
		a.addAuthority(auth);
		res.setUserAccount(a);
		final Date establishmentDate = new Date();
		res.setEstablishmentDate(establishmentDate);

		return res;
	}

	public BrotherhoodForm createForm() {

		final BrotherhoodForm res = new BrotherhoodForm();

		return res;
	}

	/**
	 * Find one brotherhood by id
	 * 
	 * @param id
	 * @return brotherhood
	 */
	public Brotherhood findOne(final int id) {
		Brotherhood res;
		Assert.isTrue(id != 0);
		res = this.brotherhoodRepository.findOne(id);
		Assert.notNull(res, "The brotherhood does not exist");
		return res;
	}

	/**
	 * Find all brotherhoods
	 * 
	 * @return Collection<brotherhood>
	 */
	public Collection<Brotherhood> findAll() {
		return this.brotherhoodRepository.findAll();
	}

	/**
	 * Save a new brotherhood or edit a existing brotherhood
	 * 
	 * @param brotherhood
	 * @return brotherhood
	 */
	public Brotherhood save(final Brotherhood brotherhood) {
		Brotherhood result;
		// TODO comprobar que la zona exista
		if (brotherhood.getId() == 0) {
			final UserAccount account = brotherhood.getUserAccount();
			final Authority au = new Authority();
			au.setAuthority(Authority.BROTHERHOOD);
			Assert.isTrue(account.getAuthorities().contains(au),
					"You can not register with this authority");
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String hash = encoder.encodePassword(brotherhood
					.getUserAccount().getPassword(), null);
			brotherhood.getUserAccount().setPassword(hash);
			final UserAccount savedAccount = this.userAccountService
					.save(account);
			brotherhood.setUserAccount(savedAccount);
			result = this.brotherhoodRepository.save(brotherhood);
			this.messageBoxService.initializeDefaultBoxes(result);
		} else {
			final UserAccount userAccount = LoginService.getPrincipal();
			final Brotherhood brotherhoodBD = this.brotherhoodRepository
					.findOne(brotherhood.getId());
			Assert.isTrue(brotherhood.getUserAccount().equals(userAccount)
					|| brotherhoodBD.getUserAccount().equals(userAccount),
					"This account does not belong to you");
			result = this.brotherhoodRepository.save(brotherhood);
		}
		return result;
	}

	/**
	 * Remove the brotherhood
	 * 
	 * @param id
	 */
	public void delete(final int id) {
		final Brotherhood brotherhood = this.brotherhoodRepository.findOne(id);
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(brotherhood.getUserAccount().equals(userAccount),
				"This account does not belong to you");
		this.brotherhoodRepository.delete(id);
	}

	/**
	 * Change the incomplete brotherhood to an domain object
	 * 
	 * @param brotherhood
	 * @param binding
	 * @return brotherhood
	 */
	public Brotherhood reconstruct(final BrotherhoodForm brotherhoodForm,
			final BindingResult binding) {
		Brotherhood result = this.create();
		if (brotherhoodForm.getId() == 0) {
			result.getUserAccount().setUsername(brotherhoodForm.getUsername());
			result.getUserAccount().setPassword(brotherhoodForm.getPassword());
			result.setAddress(brotherhoodForm.getAddress());
			result.setEmail(brotherhoodForm.getEmail());
			result.setMiddleName(brotherhoodForm.getMiddleName());
			result.setName(brotherhoodForm.getName());
			result.setPhoneNumber(brotherhoodForm.getPhoneNumber());
			result.setPhoto(brotherhoodForm.getPhoto());
			result.setSurname(brotherhoodForm.getSurname());
			result.setTitle(brotherhoodForm.getTitle());
			if (brotherhoodForm.getZone() != null)
				result.setZone(brotherhoodForm.getZone());
			this.validator.validate(brotherhoodForm, binding);

		} else {
			final Brotherhood bd = this.brotherhoodRepository
					.findOne(brotherhoodForm.getId());
			Assert.notNull(bd, "NotIdValid");
			if (this.checkValidation(brotherhoodForm, binding, bd)) {
				result.setId(brotherhoodForm.getId());
				final UserAccount user = new UserAccount();
				// user.setAuthorities(bd.getUserAccount().getAuthorities());
				result.setUserAccount(user);
				result.setAddress(brotherhoodForm.getAddress());
				result.setEmail(brotherhoodForm.getEmail());
				result.setMiddleName(brotherhoodForm.getMiddleName());
				result.setName(brotherhoodForm.getName());
				result.setPhoneNumber(brotherhoodForm.getPhoneNumber());
				result.setPhoto(brotherhoodForm.getPhoto());
				result.setSurname(brotherhoodForm.getSurname());
				result.setTitle(brotherhoodForm.getTitle());
				if (bd.getZone() == null && brotherhoodForm.getZone() != null)
					result.setZone(brotherhoodForm.getZone());
			} else {
				result = this.create();
				result.setAddress(brotherhoodForm.getAddress());
				result.setEmail(brotherhoodForm.getEmail());
				result.setMiddleName(brotherhoodForm.getMiddleName());
				result.setName(brotherhoodForm.getName());
				result.setPhoneNumber(brotherhoodForm.getPhoneNumber());
				result.setPhoto(brotherhoodForm.getPhoto());
				result.setSurname(brotherhoodForm.getSurname());
				result.setTitle(brotherhoodForm.getTitle());
				if (bd.getZone() != null)
					result.setZone(bd.getZone());
				else if (bd.getZone() == null
						&& brotherhoodForm.getZone() != null)
					result.setZone(bd.getZone());

			}
		}
		return result;
	}

	private boolean checkValidation(final BrotherhoodForm brotherhoodForm,
			final BindingResult binding, final Brotherhood brotherhood) {
		boolean check = true;
		brotherhoodForm.setCheckBox(true);
		brotherhoodForm.setPassword(brotherhood.getUserAccount().getPassword());
		brotherhoodForm
				.setPassword2(brotherhood.getUserAccount().getPassword());
		brotherhoodForm.setUsername(brotherhood.getUserAccount().getUsername());
		this.validator.validate(brotherhoodForm, binding);
		if (binding.hasErrors())
			check = false;
		return check;
	}

	public Collection<String> getSplitPictures(final String pictures) {
		final Collection<String> res = new ArrayList<>();
		final String[] slice = pictures.split("< >");
		for (final String p : slice)
			res.add(p);
		return res;
	}

	public String convetCollectionToString(final Collection<String> pictures)
			throws MalformedURLException, URISyntaxException {
		String result = "";
		if (pictures != null)
			for (final String p : pictures) {
				(new URL(p.trim())).toURI();
				result = result + p.trim() + "< >";
			}
		return result;
	}

	public String checkURLPictures(final Collection<String> pictures) {
		String result = "";
		if (!pictures.isEmpty())
			for (final String p : pictures)
				result = result + p.trim() + "< >";
		return result;
	}

	public Brotherhood largestBrotherhood() {
		Brotherhood result = null;
		Collection<Brotherhood> brotherhoods;
		Collection<Enrolment> enrolments;
		int count = 0;

		brotherhoods = this.findAll();

		for (final Brotherhood b : brotherhoods) {
			enrolments = this.enrolmentService
					.findActiveEnrolmentByBrotherhood(b.getId());

			if (count == 0)
				result = b;

			if (this.enrolmentService.getEnrollmentsByBrotherhood(
					result.getId()).size() < enrolments.size())
				result = b;

			count++;
		}

		return result;
	}

	public Brotherhood smallestBrotherhood() {
		Brotherhood result = null;
		Collection<Brotherhood> brotherhoods;
		Collection<Enrolment> enrolments;
		int count = 0;

		brotherhoods = this.findAll();

		for (final Brotherhood b : brotherhoods) {
			enrolments = this.enrolmentService
					.findActiveEnrolmentByBrotherhood(b.getId());

			if (count == 0)
				result = b;

			if (this.enrolmentService.getEnrollmentsByBrotherhood(
					result.getId()).size() > enrolments.size())
				result = b;

			count++;
		}

		return result;
	}

	public Double ratioBrotherhoodsPerArea() {
		Collection<Zone> zones;
		Collection<Brotherhood> brotherhoods;
		Collection<Brotherhood> brotherhoodsInZone = new ArrayList<Brotherhood>();
		Double size;
		Double ratio;

		zones = this.zoneService.findAll();
		brotherhoods = this.brotherhoodRepository.findAll();

		for (Zone z : zones) {
			brotherhoodsInZone = this.findBrotherhoodsByZone(z.getId());

		}

		size = (double) brotherhoodsInZone.size();

		ratio = size / brotherhoods.size();

		return ratio;

	}

	public Double countBrotherhoodsPerArea() {
		Collection<Zone> zones;
		Collection<Brotherhood> brotherhoodsInZone = new ArrayList<Brotherhood>();

		zones = this.zoneService.findAll();

		for (Zone z : zones) {
			brotherhoodsInZone = this.findBrotherhoodsByZone(z.getId());

		}

		return (double) brotherhoodsInZone.size();

	}

	public Double maxBrotherhoodPerArea() {
		Double result = null;
		Collection<Zone> zones;
		Collection<Brotherhood> brotherhoodsInZone;
		int count = 0;

		zones = this.zoneService.findAll();
		Assert.notNull(zones);

		for (Zone z : zones) {
			brotherhoodsInZone = this.findBrotherhoodsByZone(z.getId());

			if (count == 0) {
				result = (double) brotherhoodsInZone.size();
			}

			if (brotherhoodsInZone.size() > result) {

				result = (double) brotherhoodsInZone.size();
			}

			count++;
		}
		return result;

	}

	public Double minBrotherhoodPerArea() {
		Double result = null;
		Collection<Zone> zones;
		Collection<Brotherhood> brotherhoodsInZone;
		int count = 0;

		zones = this.zoneService.findAll();
		Assert.notNull(zones);

		for (Zone z : zones) {
			brotherhoodsInZone = this.findBrotherhoodsByZone(z.getId());

			if (count == 0) {
				result = (double) brotherhoodsInZone.size();
			}

			if (brotherhoodsInZone.size() < result) {

				result = (double) brotherhoodsInZone.size();
			}

			count++;
		}
		return result;

	}

	public Collection<Brotherhood> allBros() {
		return this.brotherhoodRepository.allBros();
	}

	public Collection<Brotherhood> findBrotherhoodsByZone(int zoneId) {
		Collection<Brotherhood> result = this.brotherhoodRepository
				.brotherhoodsByZone(zoneId);
		return result;

	}

	public Double stdevBrotherhoodPerArea() {
		Collection<Zone> zones;
		Collection<Brotherhood> brotherhoods = new ArrayList<Brotherhood>();

		zones = this.zoneService.findAll();

		for (Zone z : zones) {
			brotherhoods.addAll(this.findBrotherhoodsByZone(z.getId()));

		}
		Double n = (double) brotherhoods.size();
		Double average = (double) brotherhoods.size() / zones.size();
		Double averageTimes = (double) average * n;
		Double lele = (Math.pow(n - averageTimes, 2));
		Double lolo = 1 / (n - 1);
		Double stdev = (double) Math.sqrt(lolo * lele);

		return stdev;
	}

	public Collection<Brotherhood> brotherhoodsByMemberId(int memberId) {
		Collection<Brotherhood> res;

		res = this.brotherhoodRepository.brotherhoodsByMemberId(memberId);

		return res;
	}
	
	public Collection<Brotherhood> brotherhoodsByMemberInId(int memberId) {
		Collection<Brotherhood> res;

		res = this.brotherhoodRepository.brotherhoodsByMemberInId(memberId);

		return res;
	}

	public Collection<Brotherhood> allBrotherhoodsByMemberId(int memberId) {
		Collection<Brotherhood> res;

		res = this.brotherhoodRepository.allBrotherhoodsByMemberId(memberId);

		return res;
	}
}
