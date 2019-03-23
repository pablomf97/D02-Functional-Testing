package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChapterRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Brotherhood;
import domain.Chapter;
import forms.ChapterForm;

@Service
@Transactional
public class ChapterService {

	/* Repository */

	@Autowired
	private ChapterRepository chapterRepository;

	/* Services */

	@Autowired
	private ActorService actorService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private Validator validator;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private MessageBoxService messageBoxService;

	@Autowired
	private ParadeService paradeService;

	/* Basic CRUD methods */

	/**
	 * 
	 * Create a new empty chapter
	 * 
	 * @return chapter
	 * 
	 */
	public Chapter create() {
		Chapter res;
		UserAccount userAccount;
		Authority authority;

		authority = new Authority();
		authority.setAuthority(Authority.CHAPTER);

		userAccount = this.userAccountService.create();
		userAccount.addAuthority(authority);

		res = new Chapter();
		res.setUserAccount(userAccount);

		return res;
	}

	public ChapterForm createForm() {

		final ChapterForm res = new ChapterForm();

		return res;
	}

	/**
	 * Find one chapter by id
	 * 
	 * @param id
	 * @return chapter
	 */
	public Chapter findOne(final int id) {
		Chapter res;
		Assert.isTrue(id != 0);
		res = this.chapterRepository.findOne(id);
		Assert.notNull(res, "chapter.not.exists");
		return res;
	}

	/**
	 * Find all chapters
	 * 
	 * @return Collection<chapter>
	 */
	public Collection<Chapter> findAll() {
		return this.chapterRepository.findAll();
	}

	/**
	 * Save a new chapter or edit a existing one
	 * 
	 * @param chapter
	 * @return chapter
	 */
	public Chapter save(Chapter chapter) {
		Chapter res;
		UserAccount userAccount;
		Authority authority;

		if (chapter.getId() == 0) {
			userAccount = chapter.getUserAccount();
			authority = new Authority();
			authority.setAuthority(Authority.CHAPTER);
			Assert.isTrue(userAccount.getAuthorities().contains(authority));

			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			String hash = encoder.encodePassword(chapter.getUserAccount()
					.getPassword(), null);
			chapter.getUserAccount().setPassword(hash);

			UserAccount savedAccount = this.userAccountService
					.save(userAccount);
			chapter.setUserAccount(savedAccount);

			res = this.chapterRepository.save(chapter);

			this.messageBoxService.initializeDefaultBoxes(res);
		} else {
			userAccount = LoginService.getPrincipal();
			Chapter chapterBD = this.chapterRepository.findOne(chapter.getId());
			Assert.isTrue(chapter.getUserAccount().equals(userAccount)
					|| chapterBD.getUserAccount().equals(userAccount),
					"chapter.illegal.login");
			res = this.chapterRepository.save(chapter);
		}

		return res;
	}

	/**
	 * Remove the chapter
	 * 
	 * @param id
	 */
	public void delete(int id) {
		final Chapter chapter = this.chapterRepository.findOne(id);
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(chapter.getUserAccount().equals(userAccount),
				"chapter.illegal.login");
		this.chapterRepository.delete(id);
	}

	/**
	 * Change the incomplete chapter to an domain object
	 * 
	 * @param chapter
	 * @param binding
	 * @return chapter
	 */
	public Chapter reconstruct(final ChapterForm chapterForm,
			final BindingResult binding) {
		Chapter result = this.create();

		if (chapterForm.getId() == 0) {

			result.getUserAccount().setUsername(chapterForm.getUsername());
			result.getUserAccount().setPassword(chapterForm.getPassword());
			result.setAddress(chapterForm.getAddress());
			result.setEmail(chapterForm.getEmail());
			result.setMiddleName(chapterForm.getMiddleName());
			result.setName(chapterForm.getName());
			result.setPhoneNumber(chapterForm.getPhoneNumber());
			result.setPhoto(chapterForm.getPhoto());
			result.setSurname(chapterForm.getSurname());
			result.setTitle(chapterForm.getTitle());
			if (chapterForm.getZone() != null)
				result.setZone(chapterForm.getZone());
			this.validator.validate(chapterForm, binding);

		} else {

			final Chapter bd = this.chapterRepository.findOne(chapterForm
					.getId());
			Assert.notNull(bd, "chapter.id.not.valid");
			Assert.isTrue(this.actorService.findByPrincipal().getId() == bd
					.getId());

			if (this.checkValidation(chapterForm, binding, bd)) {

				bd.setAddress(chapterForm.getAddress());
				bd.setEmail(chapterForm.getEmail());
				bd.setMiddleName(chapterForm.getMiddleName());
				bd.setName(chapterForm.getName());
				bd.setPhoneNumber(chapterForm.getPhoneNumber());
				bd.setPhoto(chapterForm.getPhoto());
				bd.setSurname(chapterForm.getSurname());
				bd.setTitle(chapterForm.getTitle());
				if (bd.getZone() == null && chapterForm.getZone() != null)
					bd.setZone(chapterForm.getZone());
				result = bd;

			} else {
				result.setId(chapterForm.getId());
				result.setAddress(chapterForm.getAddress());
				result.setEmail(chapterForm.getEmail());
				result.setMiddleName(chapterForm.getMiddleName());
				result.setName(chapterForm.getName());
				result.setPhoneNumber(chapterForm.getPhoneNumber());
				result.setPhoto(chapterForm.getPhoto());
				result.setSurname(chapterForm.getSurname());
				result.setTitle(chapterForm.getTitle());
				if (bd.getZone() != null)
					result.setZone(bd.getZone());
				else if (chapterForm.getZone() != null)
					result.setZone(chapterForm.getZone());
			}
		}
		return result;
	}

	// Other business methods
	private boolean checkValidation(final ChapterForm chapterForm,
			final BindingResult binding, final Chapter chapter) {
		boolean check = true;
		chapterForm.setCheckBox(true);
		chapterForm.setPassword(chapter.getUserAccount().getPassword());
		chapterForm.setPassword2(chapter.getUserAccount().getPassword());
		chapterForm.setUsername(chapter.getUserAccount().getUsername());
		this.validator.validate(chapterForm, binding);
		if (binding.hasErrors())
			check = false;
		return check;
	}

	public Double ratioAreasNotManaged() {
		return this.chapterRepository.ratioAreasNotManaged();
	}

	/**
	 * The average, the minimum, the maximum, and the standard deviation of the
	 * number of parades coordinated by the chapters.
	 * 
	 * @return Double[]
	 */
	public Double[] paradeChapterStatistics() {
		Collection<Chapter> chapters;
		Collection<Brotherhood> brotherhoods;
		List<Double> values = new ArrayList<>();
		Double[] res = new Double[4];

		chapters = this.findAll();
		brotherhoods = this.brotherhoodService.findAll();

		/* Getting the values of the parades */

		if (!brotherhoods.isEmpty() && !chapters.isEmpty()) {
			for (Brotherhood brotherhood : brotherhoods) {
				for (Chapter chapter : chapters) {
					if (chapter.getZone() != null) {

						if (chapter.getZone().getId() == brotherhood.getZone()
								.getId()) {
							values.add((double) (this.paradeService
									.findParadesByBrotherhoodId(
											brotherhood.getId()).size() - 1));
							break;
						}
					}
				}
			}

			if (!values.isEmpty() && values != null) {
				/* Average */
				Double avg = this.getAvg(values);

				res[0] = (avg >= 0. || avg != null) ? avg : 0.;

				/* Minimum */
				Double min = Collections.min(values);

				res[1] = (min >= 0. || min != null) ? min : 0.;

				/* Maximum */
				Double max = Collections.max(values);

				res[2] = (max >= 0. || max != null) ? max : 0.;

				/* Standard deviation */
				Double SD = this.getSD(values);

				res[3] = (SD >= 0. || SD != null) ? SD : 0.;
			} else {
				res = new Double[] { 0., 0., 0., 0. };
			}
		} else {
			res = new Double[] { 0., 0., 0., 0. };
		}
		return res;
	}

	/**
	 * The chapters that coordinate at least 10% more parades than the average
	 * 
	 * @return Collection<Chapter>
	 * 
	 * 
	 * */
	public Collection<Chapter> chapter10percentMore() {
		Collection<Chapter> chapters;
		Collection<Brotherhood> brotherhoods;
		Collection<Chapter> luckyOnes = new ArrayList<>();
		List<Double> values = new ArrayList<>();

		chapters = this.findAll();
		brotherhoods = this.brotherhoodService.findAll();

		/* Getting the values of the parades */

		if (!chapters.isEmpty() && !brotherhoods.isEmpty()) {
			for (Brotherhood brotherhood : brotherhoods) {
				for (Chapter chapter : chapters) {
					if (chapter.getZone() != null) {
						if (chapter.getZone().getId() == brotherhood.getZone()
								.getId()) {
							values.add((double) (this.paradeService
									.findParadesByBrotherhoodId(
											brotherhood.getId()).size() - 1));
							break;
						}
					}
				}
			}

			if (!values.isEmpty() && values != null) {
				Double avgtenpercent = this.getAvg(values) * 1.1;

				for (Chapter chapter : chapters) {
					if (this.chapterRepository.getParadesPerArea(
							chapter.getId()).size() > avgtenpercent) {
						luckyOnes.add(chapter);
					}
				}
			}
		}
		return luckyOnes;
	}

	/* Auxiliary method to get the average of a list of values */
	private Double getAvg(List<Double> values) {
		Double res = 0.;

		for (Double value : values) {
			res += value;
		}

		res = res / values.size();

		return res;
	}

	/* Auxiiary method to get the SD of a list of values */
	private Double getSD(List<Double> values) {
		Double standardDeviation = 0.;
		Double average = this.getAvg(values);

		for (double value : values) {
			standardDeviation += Math.pow(value - average, 2);
		}

		return Math.sqrt(standardDeviation / values.size());

	}

}
