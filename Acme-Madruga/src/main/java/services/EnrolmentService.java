package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EnrolmentRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.Position;

@Service
@Transactional
public class EnrolmentService {

	// Managed repository

	@Autowired
	private EnrolmentRepository enrolmentRepository;

	// Supporting Services

	@Autowired
	private MemberService memberService;

	@Autowired
	private Validator validator;

	@Autowired
	private ActorService actorService;

	@Autowired
	private PositionService positionService;

	// Simple CRUD methods

	/* Find one by ID */
	public Enrolment findOne(final int enrolmentId) {
		Enrolment res;

		res = this.enrolmentRepository.findOne(enrolmentId);

		return res;

	}

	/* Find all enrollments */
	public Collection<Enrolment> findAll() {
		Collection<Enrolment> res;

		res = this.enrolmentRepository.findAll();
		Assert.notNull(res, "no.enrolments");

		return res;
	}

	/* Create an enrollment */
	public Enrolment create() {
		Member principal;
		Enrolment res;

		principal = this.memberService.findByPrincipal();
		Assert.notNull(principal, "not.allowed");

		res = new Enrolment();

		List<Date> dropOutMoment = new ArrayList<>();
		res.setDropOutMoment(dropOutMoment);

		return res;
	}

	/* Save an enrollment */
	public Enrolment save(Enrolment enrolment) {
		Actor principal;
		Enrolment res;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal instanceof Member
				|| principal instanceof Brotherhood, "not.allowed");

		Assert.notNull(enrolment, "null.enrolment");

		res = this.enrolmentRepository.save(enrolment);

		return res;
	}

	// Other business methods

	/* Find enrollments by memberID */
	public Collection<Enrolment> getEnrollmentsByMember(int memberID) {
		return this.enrolmentRepository.getEnrollmentsByMember(memberID);
	}

	/* Find enrollments by brotheerhoodID */
	public Collection<Enrolment> getEnrollmentsByBrotherhood(int brotherhoodID) {
		return this.enrolmentRepository
				.getEnrollmentsByBrotherhood(brotherhoodID);
	}

	public Collection<Enrolment> findActiveEnrolmentByBrotherhood(
			int brotherhoodId) {
		Collection<Enrolment> result;

		result = this.enrolmentRepository
				.findActiveEnrolmentsByBrotherhood(brotherhoodId);
		Assert.notNull(result);

		return result;

	}

	public Collection<Enrolment> findActiveEnrolmentsByMember(int memberId) {
		Collection<Enrolment> result;

		result = this.enrolmentRepository
				.findActiveEnrolmentsByMember(memberId);
		Assert.notNull(result);

		return result;

	}

	public Collection<Enrolment> findActiveEnrolments() {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findActiveEnrolments();
		Assert.notNull(result);

		return result;
	}

	/* Reconstruct */
	public Enrolment reconstruct(Enrolment enrolment, String[] position,
			BindingResult binding) {
		Enrolment res;
		Position fullPosition;

		res = this.enrolmentRepository.findOne(enrolment.getId());

		fullPosition = this.positionService.findByName(position[0]);

		res.setPosition(fullPosition);
		res.setIsOut(false);

		this.validator.validate(res, binding);

		return res;
	}

	public boolean isAssigned(Position position) {
		Collection<Enrolment> enrolments;

		enrolments = this.enrolmentRepository
				.getEnrolmentsUsingPosition(position.getId());

		return !(enrolments.isEmpty());
	}

}
