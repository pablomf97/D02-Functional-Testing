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

import repositories.ProcessionRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.Enrolment;
import domain.March;
import domain.Platform;
import domain.Procession;

@Service
@Transactional
public class ProcessionService {

	// Managed repository ------------------------------------

	@Autowired
	private ProcessionRepository processionRepository;

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

	public Procession create() {
		Actor principal;
		Procession result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");

		result = new Procession();

		return result;
	}

	public Collection<Procession> findAll() {
		Collection<Procession> result;
		result = this.processionRepository.findAll();

		return result;
	}

	public Procession findOne(final int processionId) {
		Procession result;
		result = this.processionRepository.findOne(processionId);

		return result;
	}

	public Procession save(final Procession procession) {
		Actor principal;
		Brotherhood brotherhood;
		Procession result;

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

	public void delete(final Procession procession) {
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

	public Procession reconstruct(Procession procession, BindingResult binding) {
		Procession result;
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

	public Collection<Procession> findProcessionsByBrotherhoodId(
			int brotherhoodId) {
		Collection<Procession> result;

		result = this.processionRepository
				.findProcessionsByBrotherhoodId(brotherhoodId);

		return result;
	}

	public Collection<Procession> findAcceptedProcessionsByMemberId(int memberId) {
		Collection<Procession> result;

		result = this.processionRepository
				.findAcceptedProcessionsByMemberId(memberId);

		return result;
	}

	private Collection<Procession> findProcessionsAlreadyApplied(int memberId) {
		Collection<Procession> result;

		result = this.processionRepository
				.findProcessionsAlreadyApplied(memberId);

		return result;
	}

	public Collection<Procession> processionsToApply(int memberId) {
		Collection<Procession> toApply;
		Collection<Enrolment> memberEnrolments;
		Collection<Integer> brotherhoodIds = new ArrayList<>();

		Collection<Procession> notToApply = this
				.findProcessionsAlreadyApplied(memberId);

		toApply = this.findFinalProcessions();
		toApply.removeAll(notToApply);

		Collection<Procession> result = new ArrayList<Procession>(toApply);

		memberEnrolments = this.enrolmentService
				.findActiveEnrolmentsByMember(memberId);
		for (Enrolment enrolment : memberEnrolments) {
			brotherhoodIds.add(enrolment.getBrotherhood().getId());
		}

		for (Procession procesion : toApply) {
			for (Integer brotherhoodId : brotherhoodIds) {
				if (procesion.getBrotherhood().getId() != brotherhoodId) {
					result.remove(procesion);
				}
			}
		}

		return result;
	}

	private Collection<Procession> findFinalProcessions() {
		Collection<Procession> result;

		result = this.processionRepository.findFinalProcessions();

		return result;
	}

	public Collection<Procession> findEarlyProcessions() {
		Collection<Procession> result;
		Calendar c = new GregorianCalendar();
		c.add(Calendar.DATE, 30);
		Date maxDate = c.getTime();

		result = this.processionRepository.findEarlyProcessions(maxDate);
		Assert.notNull(result);

		return result;
	}

	public Boolean checkPos(Integer row, Integer column, Procession procession,
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

	public List<Integer> recommendedPos(Procession procession) {
		List<Integer> rowColumn = new ArrayList<>();
		Boolean validPos = false;
		Collection<March> marchs;

		marchs = this.marchService.findMarchByProcession(procession.getId());
		
		for(Integer auxRow = 1 ; auxRow < 20000 ; auxRow++) {
			for(Integer auxCol = 1 ; auxCol <= procession.getMaxCols() ; auxCol++) {
				validPos = this.checkPos(auxRow, auxCol, procession, marchs);
				if (validPos) {
					rowColumn.add(auxRow);
					rowColumn.add(auxCol);
					break;
				}
			}
			if(validPos)
				break;
		}
		return rowColumn;
	}
	
	private Collection<Procession> findFinalProcessionByBrotherhood(int brotherhoodId) {
		Collection<Procession> result;

		result = this.processionRepository.findFinalProcessionByBrotherhood(brotherhoodId);

		return result;
	}
	
	public Collection<Procession> findPossibleProcessionsToMarchByMember(int memberId){
		Collection<Procession> result = new ArrayList<>();
		Collection<Brotherhood> brotherhoods = this.brotherhoodService.brotherhoodsByMemberInId(memberId);
		
		for(Brotherhood brotherhood : brotherhoods) {
			Collection<Procession> aux1 = this.findFinalProcessionByBrotherhood(brotherhood.getId());
			Collection<Procession> aux2 = this.findProcessionsAlreadyApplied(memberId);
			aux1.removeAll(aux2);
			result.addAll(aux1);
		}
		return result;
	}
}
