package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ParadeRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.Chapter;
import domain.Enrolment;
import domain.March;
import domain.Parade;
import domain.Platform;

@Service
@Transactional
public class ParadeService {

	// Managed repository ------------------------------------

	@Autowired
	private ParadeRepository paradeRepository;

	// Supporting services -----------------------------------

	@Autowired
	private ActorService actorService;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private EnrolmentService enrolmentService;

	@Autowired
	private MarchService marchService;

	@Autowired
	private Validator validator;

	@Autowired
	private BrotherhoodService brotherhoodService;

	// Simple CRUD methods -----------------------------------

	public Parade create() {
		Actor principal;
		Parade result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");

		result = new Parade();

		return result;
	}

	public Collection<Parade> findAll() {
		Collection<Parade> result;
		result = this.paradeRepository.findAll();

		return result;
	}

	public Parade findOne(final int paradeId) {
		Parade result;
		result = this.paradeRepository.findOne(paradeId);

		return result;
	}

	public Parade save(final Parade parade) {
		Actor principal;
		Brotherhood brotherhood;
		Chapter chapter;
		Parade result;

		principal = this.actorService.findByPrincipal();

		try {
			brotherhood = (Brotherhood) principal;

			Assert.isTrue(parade.getBrotherhood().equals(principal),
					"not.allowed");

			Assert.notNull(parade);
			Assert.notNull(parade.getDescription());
			Assert.notNull(parade.getMaxCols());
			Assert.notNull(parade.getTitle());
			Assert.notNull(parade.getOrganisedMoment());

			if (parade.getId() == 0) {
				Assert.notNull(brotherhood.getZone());
				parade.setStatus("SUBMITTED");
			}

		} catch (Throwable oops) {
			chapter = (Chapter) principal;

			Assert.isTrue(
					parade.getBrotherhood().getZone().equals(chapter.getZone()),
					"not.allowed");

			Assert.notNull(parade);
			Assert.notNull(parade.getDescription());
			Assert.notNull(parade.getMaxCols());
			Assert.notNull(parade.getTitle());
			Assert.notNull(parade.getOrganisedMoment());
		}

		result = this.paradeRepository.save(parade);
		Assert.notNull(result);

		return result;
	}

	public Parade accept(Parade parade) {
		Actor principal;
		Brotherhood brotherhood;
		Chapter chapter;
		Parade result = null;

		Assert.isTrue(parade.getStatus().equals("SUBMITTED"));
		Assert.isTrue(parade.getIsDraft() == false);

		principal = this.actorService.findByPrincipal();

		try {
			brotherhood = (Brotherhood) principal;

			Assert.isTrue(parade.getBrotherhood().equals(principal),
					"not.allowed");

			Assert.notNull(parade);
			Assert.notNull(parade.getDescription());
			Assert.notNull(parade.getMaxCols());
			Assert.notNull(parade.getTitle());
			Assert.notNull(parade.getOrganisedMoment());

		} catch (Throwable oops) {
			chapter = (Chapter) principal;

			Assert.isTrue(
					parade.getBrotherhood().getZone().equals(chapter.getZone()),
					"not.allowed");

			Assert.notNull(parade);
			Assert.notNull(parade.getDescription());
			Assert.notNull(parade.getMaxCols());
			Assert.notNull(parade.getTitle());
			Assert.notNull(parade.getOrganisedMoment());
		}
		parade.setStatus("ACCEPTED");
		result = this.paradeRepository.save(parade);
		Assert.notNull(result);

		return result;
	}

	public Parade reject(Parade parade) {
		Actor principal;
		Brotherhood brotherhood;
		Chapter chapter;
		Parade result = null;

		Assert.isTrue(parade.getStatus().equals("SUBMITTED"));
		Assert.isTrue(parade.getIsDraft() == false);

		principal = this.actorService.findByPrincipal();

		try {
			brotherhood = (Brotherhood) principal;

			Assert.isTrue(parade.getBrotherhood().equals(principal),
					"not.allowed");

			Assert.notNull(parade);
			Assert.notNull(parade.getDescription());
			Assert.notNull(parade.getMaxCols());
			Assert.notNull(parade.getTitle());
			Assert.notNull(parade.getOrganisedMoment());

		} catch (Throwable oops) {
			chapter = (Chapter) principal;

			Assert.isTrue(
					parade.getBrotherhood().getZone().equals(chapter.getZone()),
					"not.allowed");

			Assert.notNull(parade);
			Assert.notNull(parade.getDescription());
			Assert.notNull(parade.getMaxCols());
			Assert.notNull(parade.getTitle());
			Assert.notNull(parade.getOrganisedMoment());
		}
		parade.setStatus("REJECTED");
		result = this.paradeRepository.save(parade);
		Assert.notNull(result);

		return result;
	}

	public void delete(final Parade parade) {
		Actor principal;

		Assert.notNull(parade);
		Assert.isTrue(parade.getId() != 0, "wrong.id");

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");

		Assert.isTrue(parade.getBrotherhood().equals(principal), "not.allowed");

		this.paradeRepository.delete(parade.getId());

	}

	// Other business methods -------------------------------

	public Parade reconstruct(Parade parade, BindingResult binding) {
		Parade result;
		Chapter chapter;
		Actor principal = this.actorService.findByPrincipal();

		if (parade.getId() == 0) {

			result = parade;
			result.setTicker(this.utilityService.generateTicker());
			result.setPlatforms(new ArrayList<Platform>());
			result.setBrotherhood((Brotherhood) principal);
			result.setStatus("SUBMITTED");

		} else {
			result = this.findOne(parade.getId());
			Assert.notNull(result);

			try {
				Assert.isTrue(result.getBrotherhood().getId() == principal
						.getId());
				result.setTitle(parade.getTitle());
				result.setDescription(parade.getDescription());
				result.setPlatforms(parade.getPlatforms());
				result.setIsDraft(parade.getIsDraft());
				if (parade.getStatus() != null)
					result.setStatus(parade.getStatus());
			} catch (Throwable oops) {
				chapter = (Chapter) principal;
				Assert.isTrue(result.getBrotherhood().getZone().getId() == (chapter
						.getZone().getId()));
				if (parade.getStatus() != null)
					result.setStatus(parade.getStatus());
			}

		}

		validator.validate(result, binding);

		return result;
	}

	public Collection<Parade> findParadesByBrotherhoodId(int brotherhoodId) {
		Collection<Parade> result;

		result = this.paradeRepository
				.findParadesByBrotherhoodId(brotherhoodId);

		return result;
	}

	public Collection<Parade> findAcceptedParadesByMemberId(int memberId) {
		Collection<Parade> result;

		result = this.paradeRepository.findAcceptedParadesByMemberId(memberId);

		return result;
	}

	private Collection<Parade> findParadesAlreadyApplied(int memberId) {
		Collection<Parade> result;

		result = this.paradeRepository.findParadesAlreadyApplied(memberId);

		return result;
	}

	public Collection<Parade> paradesToApply(int memberId) {
		Collection<Parade> toApply;
		Collection<Enrolment> memberEnrolments;
		Collection<Integer> brotherhoodIds = new ArrayList<>();

		Collection<Parade> notToApply = this
				.findParadesAlreadyApplied(memberId);

		toApply = this.findFinalParades();
		toApply.removeAll(notToApply);

		Collection<Parade> result = new ArrayList<Parade>(toApply);

		memberEnrolments = this.enrolmentService
				.findActiveEnrolmentsByMember(memberId);
		for (Enrolment enrolment : memberEnrolments)
			brotherhoodIds.add(enrolment.getBrotherhood().getId());

		for (Parade procesion : toApply)
			for (Integer brotherhoodId : brotherhoodIds)
				if (procesion.getBrotherhood().getId() != brotherhoodId)
					result.remove(procesion);

		return result;
	}

	private Collection<Parade> findFinalParades() {
		Collection<Parade> result;

		result = this.paradeRepository.findFinalParades();

		return result;
	}

	public Collection<Parade> findEarlyParades() {
		Collection<Parade> result;
		Calendar c = new GregorianCalendar();
		c.add(Calendar.DATE, 30);
		Date maxDate = c.getTime();

		result = this.paradeRepository.findEarlyParades(maxDate);
		Assert.notNull(result);

		return result;
	}

	public Boolean checkPos(Integer row, Integer column, Parade parade,
			Collection<March> marchs) {
		Boolean validPos = true;
		Integer maxCols = parade.getMaxCols();

		if (column > maxCols)
			validPos = false;

		if (validPos) {
			for (March march : marchs) {
				if (row == march.getRow() && column == march.getCol()) {
					validPos = false;
					break;
				}
			}
		}
		return validPos;
	}

	public List<Integer> recommendedPos(Parade parade) {
		List<Integer> rowColumn = new ArrayList<>();
		Boolean validPos = false;
		Collection<March> marchs;

		marchs = this.marchService.findMarchByParade(parade.getId());

		for (Integer auxRow = 1; auxRow < 20000; auxRow++) {
			for (Integer auxCol = 1; auxCol <= parade.getMaxCols(); auxCol++) {
				validPos = this.checkPos(auxRow, auxCol, parade, marchs);
				if (validPos) {
					rowColumn.add(auxRow);
					rowColumn.add(auxCol);
					break;
				}
			}
			if (validPos)
				break;
		}
		return rowColumn;
	}

	private Collection<Parade> findFinalParadeByBrotherhood(int brotherhoodId) {
		Collection<Parade> result;

		result = this.paradeRepository
				.findFinalParadeByBrotherhood(brotherhoodId);

		return result;
	}

	public Collection<Parade> findPossibleParadesToMarchByMember(int memberId) {
		Collection<Parade> result = new ArrayList<>();
		Collection<Brotherhood> brotherhoods = this.brotherhoodService
				.brotherhoodsByMemberInId(memberId);

		for (Brotherhood brotherhood : brotherhoods) {
			Collection<Parade> aux1 = this
					.findFinalParadeByBrotherhood(brotherhood.getId());
			Collection<Parade> aux2 = this.findParadesAlreadyApplied(memberId);
			aux1.removeAll(aux2);
			result.addAll(aux1);
		}
		return result;
	}

	public Double ratioDraftVsFinal() {
		Double result;

		result = this.paradeRepository.ratioDraftVsFinal();

		return result;
	}

	public Collection<Double> ratioFinalModeGroupedByStatus() {
		Collection<Double> result;

		result = this.paradeRepository.ratioFinalModeGroupedByStatus();

		return result;
	}

	public Collection<Parade> findParadesByAres(int id) {
		return this.paradeRepository.findParadesByAres(id);
	}  

	public Parade copyParade(final Integer paradeId) {
		Parade res;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService
				.checkAuthority(principal, "BROTHERHOOD"));
		res = this.paradeRepository.findOne(paradeId);
		Assert.isTrue(res.getBrotherhood().equals(principal));
		final Parade copy = this.create();
		copy.setBrotherhood(res.getBrotherhood());
		copy.setDescription(res.getDescription());
		copy.setIsDraft(true);
		copy.setMaxCols(res.getMaxCols());
		copy.setOrganisedMoment(res.getOrganisedMoment());
		copy.setPlatforms(res.getPlatforms());
		copy.setReason("");
		copy.setStatus("SUBMITTED");
		copy.setTicker(this.utilityService.generateTicker());
		copy.setTitle(res.getTitle());
		res = this.save(copy);
		return res;
	}

	public void flush() {
		this.paradeRepository.flush();
	}
}
