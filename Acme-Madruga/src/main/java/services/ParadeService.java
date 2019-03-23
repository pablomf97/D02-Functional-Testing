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
	private ParadeRepository processionRepository;

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
		result = this.processionRepository.findAll();

		return result;
	}

	public Parade findOne(final int processionId) {
		Parade result;
		result = this.processionRepository.findOne(processionId);

		return result;
	}

	public Parade save(final Parade procession) {
		Actor principal;
		Brotherhood brotherhood;
		Parade result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");

		Assert.isTrue(procession.getBrotherhood().equals(principal),
				"not.allowed");

		Assert.notNull(procession);
		Assert.notNull(procession.getDescription());
		Assert.notNull(procession.getMaxCols());
		Assert.notNull(procession.getTitle());
		Assert.notNull(procession.getOrganisedMoment());

		brotherhood = (Brotherhood) principal;

		if (procession.getId() == 0) {
			Assert.notNull(brotherhood.getZone());
		}

		result = this.processionRepository.save(procession);
		Assert.notNull(result);

		return result;
	}

	public void delete(final Parade procession) {
		Actor principal;

		Assert.notNull(procession);
		Assert.isTrue(procession.getId() != 0, "wrong.id");

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");

		Assert.isTrue(procession.getBrotherhood().equals(principal),
				"not.allowed");

		this.processionRepository.delete(procession.getId());

	}

	// Other business methods -------------------------------

	public Parade reconstruct(Parade procession, BindingResult binding) {
		Parade result;
		Actor principal = this.actorService.findByPrincipal();

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

		validator.validate(result, binding);

		return result;
	}

	public Collection<Parade> findParadeByBrotherhoodId(int id) {
		return this.processionRepository.findParadesByBrotherhoodId(id);
	}

	public Collection<Parade> findProcessionsByBrotherhoodId(int brotherhoodId) {
		Collection<Parade> result;

		result = this.processionRepository
				.findParadesByBrotherhoodId(brotherhoodId);

		return result;
	}

	public Collection<Parade> findAcceptedProcessionsByMemberId(int memberId) {
		Collection<Parade> result;

		result = this.processionRepository
				.findAcceptedParadesByMemberId(memberId);

		return result;
	}

	private Collection<Parade> findProcessionsAlreadyApplied(int memberId) {
		Collection<Parade> result;

		result = this.processionRepository.findParadesAlreadyApplied(memberId);

		return result;
	}

	public Collection<Parade> processionsToApply(int memberId) {
		Collection<Parade> toApply;
		Collection<Enrolment> memberEnrolments;
		Collection<Integer> brotherhoodIds = new ArrayList<>();

		Collection<Parade> notToApply = this
				.findProcessionsAlreadyApplied(memberId);

		toApply = this.findFinalProcessions();
		toApply.removeAll(notToApply);

		Collection<Parade> result = new ArrayList<Parade>(toApply);

		memberEnrolments = this.enrolmentService
				.findActiveEnrolmentsByMember(memberId);
		for (Enrolment enrolment : memberEnrolments) {
			brotherhoodIds.add(enrolment.getBrotherhood().getId());
		}

		for (Parade procesion : toApply) {
			for (Integer brotherhoodId : brotherhoodIds) {
				if (procesion.getBrotherhood().getId() != brotherhoodId) {
					result.remove(procesion);
				}
			}
		}

		return result;
	}

	public Collection<Parade> findFinalProcessions() {
		Collection<Parade> result;

		result = this.processionRepository.findFinalParades();

		return result;
	}

	public Collection<Parade> findEarlyProcessions() {
		Collection<Parade> result;
		Calendar c = new GregorianCalendar();
		c.add(Calendar.DATE, 30);
		Date maxDate = c.getTime();

		result = this.processionRepository.findEarlyParades(maxDate);
		Assert.notNull(result);

		return result;
	}

	public Boolean checkPos(Integer row, Integer column, Parade procession,
			Collection<March> marchs) {
		Boolean validPos = true;
		Integer maxCols = procession.getMaxCols();

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

	public List<Integer> recommendedPos(Parade procession) {
		List<Integer> rowColumn = new ArrayList<>();
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

	private Collection<Parade> findFinalProcessionByBrotherhood(
			int brotherhoodId) {
		Collection<Parade> result;

		result = this.processionRepository
				.findFinalParadeByBrotherhood(brotherhoodId);

		return result;
	}

	public Collection<Parade> findPossibleProcessionsToMarchByMember(
			int memberId) {
		Collection<Parade> result = new ArrayList<>();
		Collection<Brotherhood> brotherhoods = this.brotherhoodService
				.brotherhoodsByMemberInId(memberId);

		for (Brotherhood brotherhood : brotherhoods) {
			Collection<Parade> aux1 = this
					.findFinalProcessionByBrotherhood(brotherhood.getId());
			Collection<Parade> aux2 = this
					.findProcessionsAlreadyApplied(memberId);
			aux1.removeAll(aux2);
			result.addAll(aux1);
		}
		return result;
	}

	public Double ratioDraftVsFinal() {
		return this.processionRepository.ratioDraftVsFinal();
	}

	public Double[] ratioFinalModeGroupedByStatus() {
		Double[] res = this.processionRepository
				.ratioFinalModeGroupedByStatus();
		return (res.length > 0) ? res : new Double[] { 0., 0., 0. };
	}
	
	
	public Collection<Parade> getAcceptedParades(){
		Collection<Parade> result;
		
		result = this.processionRepository.getAcceptedParades();
		
		return result;
		
	}
}
