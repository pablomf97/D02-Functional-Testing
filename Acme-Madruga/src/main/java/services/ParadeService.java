
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
import domain.Enrolment;
import domain.March;
import domain.Parade;
import domain.Platform;

@Service
@Transactional
public class ParadeService {

	// Managed repository ------------------------------------

	@Autowired
	private ParadeRepository	paradeRepository;

	// Supporting services -----------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UtilityService		utilityService;

	@Autowired
	private EnrolmentService	enrolmentService;

	@Autowired
	private MarchService		marchService;

	@Autowired
	private Validator			validator;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Simple CRUD methods -----------------------------------

	public Parade create() {
		Actor principal;
		Parade result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"), "not.allowed");

		result = new Parade();

		return result;
	}

	public Collection<Parade> findAll() {
		Collection<Parade> result;
		result = this.paradeRepository.findAll();

		return result;
	}

	public Parade findOne(final int processionId) {
		Parade result;
		result = this.paradeRepository.findOne(processionId);

		return result;
	}

	public Parade save(final Parade procession) {
		Actor principal;
		Brotherhood brotherhood;
		Parade result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"), "not.allowed");

		Assert.isTrue(procession.getBrotherhood().equals(principal), "not.allowed");

		Assert.notNull(procession);
		Assert.notNull(procession.getDescription());
		Assert.notNull(procession.getMaxCols());
		Assert.notNull(procession.getTitle());
		Assert.notNull(procession.getOrganisedMoment());

		brotherhood = (Brotherhood) principal;

		if (procession.getId() == 0)
			Assert.notNull(brotherhood.getZone());

		result = this.paradeRepository.save(procession);
		Assert.notNull(result);

		return result;
	}

	public void delete(final Parade procession) {
		Actor principal;

		Assert.notNull(procession);
		Assert.isTrue(procession.getId() != 0, "wrong.id");

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"), "not.allowed");

		Assert.isTrue(procession.getBrotherhood().equals(principal), "not.allowed");

		this.paradeRepository.delete(procession.getId());

	}

	// Other business methods -------------------------------

	public Parade reconstruct(final Parade procession, final BindingResult binding) {
		Parade result;
		final Actor principal = this.actorService.findByPrincipal();

		if (procession.getId() == 0) {

			result = procession;
			result.setTicker(this.utilityService.generateTicker());
			result.setPlatforms(new ArrayList<Platform>());
			result.setBrotherhood((Brotherhood) principal);

		} else {
			result = this.findOne(procession.getId());
			Assert.notNull(result);
			Assert.isTrue(result.getBrotherhood().getId() == principal.getId());

			result.setTitle(procession.getTitle());
			result.setDescription(procession.getDescription());
			result.setPlatforms(procession.getPlatforms());
			result.setIsDraft(procession.getIsDraft());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public Collection<Parade> findParadeByBrotherhoodId(final int id) {
		return this.paradeRepository.findParadesByBrotherhoodId(id);
	}

	public Collection<Parade> findProcessionsByBrotherhoodId(final int brotherhoodId) {
		Collection<Parade> result;

		result = this.paradeRepository.findParadesByBrotherhoodId(brotherhoodId);

		return result;
	}

	public Collection<Parade> findAcceptedProcessionsByMemberId(final int memberId) {
		Collection<Parade> result;

		result = this.paradeRepository.findAcceptedParadesByMemberId(memberId);

		return result;
	}

	private Collection<Parade> findProcessionsAlreadyApplied(final int memberId) {
		Collection<Parade> result;

		result = this.paradeRepository.findParadesAlreadyApplied(memberId);

		return result;
	}

	public Collection<Parade> paradeToApply(final int memberId) {
		Collection<Parade> toApply;
		Collection<Enrolment> memberEnrolments;
		final Collection<Integer> brotherhoodIds = new ArrayList<>();

		final Collection<Parade> notToApply = this.findProcessionsAlreadyApplied(memberId);

		toApply = this.findFinalProcessions();
		toApply.removeAll(notToApply);

		final Collection<Parade> result = new ArrayList<Parade>(toApply);

		memberEnrolments = this.enrolmentService.findActiveEnrolmentsByMember(memberId);
		for (final Enrolment enrolment : memberEnrolments)
			brotherhoodIds.add(enrolment.getBrotherhood().getId());

		for (final Parade procesion : toApply)
			for (final Integer brotherhoodId : brotherhoodIds)
				if (procesion.getBrotherhood().getId() != brotherhoodId)
					result.remove(procesion);

		return result;
	}

	private Collection<Parade> findFinalProcessions() {
		Collection<Parade> result;

		result = this.paradeRepository.findFinalParades();

		return result;
	}

	public Collection<Parade> findEarlyProcessions() {
		Collection<Parade> result;
		final Calendar c = new GregorianCalendar();
		c.add(Calendar.DATE, 30);
		final Date maxDate = c.getTime();

		result = this.paradeRepository.findEarlyParades(maxDate);
		Assert.notNull(result);

		return result;
	}

	public Boolean checkPos(final Integer row, final Integer column, final Parade procession, final Collection<March> marchs) {
		Boolean validPos = true;
		final Integer maxCols = procession.getMaxCols();

		if (column > maxCols)
			validPos = false;

		if (validPos)
			for (final March march : marchs)
				if (row == march.getRow() && column == march.getCol()) {
					validPos = false;
					break;
				}
		return validPos;
	}

	public List<Integer> recommendedPos(final Parade procession) {
		final List<Integer> rowColumn = new ArrayList<>();
		Boolean validPos = false;
		Collection<March> marchs;

		marchs = this.marchService.findMarchByProcession(procession.getId());

		for (Integer auxRow = 1; auxRow < 20000; auxRow++) {
			for (Integer auxCol = 1; auxCol <= procession.getMaxCols(); auxCol++) {
				validPos = this.checkPos(auxRow, auxCol, procession, marchs);
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

	private Collection<Parade> findFinalProcessionByBrotherhood(final int brotherhoodId) {
		Collection<Parade> result;

		result = this.paradeRepository.findFinalParadeByBrotherhood(brotherhoodId);

		return result;
	}

	public Collection<Parade> findPossibleProcessionsToMarchByMember(final int memberId) {
		final Collection<Parade> result = new ArrayList<>();
		final Collection<Brotherhood> brotherhoods = this.brotherhoodService.brotherhoodsByMemberInId(memberId);

		for (final Brotherhood brotherhood : brotherhoods) {
			final Collection<Parade> aux1 = this.findFinalProcessionByBrotherhood(brotherhood.getId());
			final Collection<Parade> aux2 = this.findProcessionsAlreadyApplied(memberId);
			aux1.removeAll(aux2);
			result.addAll(aux1);
		}
		return result;
	}

	public Double ratioDraftVsFinal() {
		return this.paradeRepository.ratioDraftVsFinal();
	}

	public Double[] ratioFinalModeGroupedByStatus() {
		final Double[] res = this.paradeRepository.ratioFinalModeGroupedByStatus();
		return (res.length > 0) ? res : new Double[] {
			0., 0., 0.
		};
	}

	public Parade copyParade(final Integer paradeId) {
		Parade res;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
		res = this.paradeRepository.findOne(paradeId);
		Assert.isTrue(res.getBrotherhood().equals(principal));
		final Parade copy = this.create();
		copy.setBrotherhood(res.getBrotherhood());
		copy.setDescription(res.getDescription());
		copy.setIsDraft(true);
		copy.setMaxCols(res.getMaxCols());
		copy.setOrganisedMoment(res.getOrganisedMoment());
		copy.setPath(res.getPath());
		copy.setPlatforms(res.getPlatforms());
		copy.setReason("");
		copy.setStatus("SUBMITTED");
		copy.setTicker(this.utilityService.generateTicker());
		copy.setTitle(res.getTitle());
		res = this.save(copy);
		return res;
	}
}
