
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MarchRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.March;
import domain.Member;
import domain.Parade;

@Service
@Transactional
public class MarchService {

	// Managed repository ------------------------------------

	@Autowired
	private MarchRepository	marchRepository;

	// Supporting services -----------------------------------

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;

	@Autowired
	private ParadeService	paradeService;


	// Simple CRUD methods -----------------------------------

	public March create() {
		Actor principal;
		March result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MEMBER"), "not.allowed");

		result = new March();

		return result;
	}

	public Collection<March> findAll() {
		Collection<March> result;
		result = this.marchRepository.findAll();

		return result;
	}

	public March findOne(final int marchId) {
		March result;
		result = this.marchRepository.findOne(marchId);

		return result;
	}

	public March save(final March march) {
		Member member;
		Brotherhood brotherhood;
		Actor principal;
		March result;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal, "not.allowed");
		Assert.notNull(march);

		if (this.actorService.checkAuthority(principal, "MEMBER")) {

			Assert.isTrue(march.getId() == 0, "wrong.id");

			member = (Member) principal;
			march.setStatus("PENDING");
			march.setRow(null);
			march.setCol(null);
			march.setMember(member);

		} else if (this.actorService.checkAuthority(principal, "BROTHERHOOD")) {

			Assert.isTrue(march.getId() != 0);
			Assert.notNull(march.getMember());
			Assert.notNull(march.getParade());
			Assert.notNull(march.getStatus());

			brotherhood = (Brotherhood) principal;

			Assert.isTrue(march.getParade().getBrotherhood().equals(brotherhood), "not.allowed");
			if (march.getStatus() == "REJECT")
				Assert.notNull(march.getReason());

		}

		result = this.marchRepository.save(march);
		Assert.notNull(result);

		return result;
	}

	public void delete(final March march) {
		Actor principal;

		Assert.notNull(march);
		Assert.isTrue(march.getId() != 0, "wrong.id");

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MEMBER"), "not.allowed");

		Assert.isTrue(march.getMember().equals(principal), "not.allowed");

		this.marchRepository.delete(march.getId());

	}

	// Other business methods -------------------------------

	public March reconstruct(final March march, final BindingResult binding) {
		March result;
		Actor principal = null;

		if (march.getId() == 0) {
			principal = this.actorService.findByPrincipal();
			result = march;
			result.setStatus("PENDING");
			result.setMember((Member) principal);
		} else {
			result = this.findOne(march.getId());

			result.setStatus(march.getStatus());
			result.setRow(march.getRow());
			result.setCol(march.getCol());
			result.setReason(march.getReason());

		}

		this.validator.validate(result, binding);

		return result;
	}

	public Collection<March> findMarchsByMemberId(final int memberId) {
		Collection<March> result;

		result = this.marchRepository.findMarchsByMemberId(memberId);

		return result;
	}

	public Collection<March> findMarchsByBrotherhoodId(final int brotherhoodId) {
		Collection<March> result;

		result = this.marchRepository.findMarchsByBrotherhoodId(brotherhoodId);

		return result;
	}

	public Double ratioApprovedRequests() {
		Double result;

		result = this.marchRepository.ratioApprovedRequests();

		return result;
	}

	public Double ratioPendingRequests() {
		Double result;

		result = this.marchRepository.ratioPendingRequests();

		return result;
	}

	public Double ratioRejectedRequests() {
		Double result;

		result = this.marchRepository.ratioRejectedRequests();

		return result;
	}

	public Double[] ratioApprovedInAParade() {

		Collection<Parade> parades;
		Collection<March> marchsInAParade = new ArrayList<March>();
		final Collection<March> marchsApprovedInAParade = new ArrayList<March>();

		int count = 0;

		parades = this.paradeService.findAll();

		final Double[] result = new Double[parades.size()];

		for (final Parade p : parades) {

			Double ratio = 0.0;

			marchsInAParade = this.findMarchByParade(p.getId());

			for (final March m : marchsInAParade)
				if (m.getStatus().equals("APPROVED"))
					marchsApprovedInAParade.add(m);

			if (marchsInAParade.size() != 0)
				ratio = (double) (marchsApprovedInAParade.size() / marchsInAParade.size());

			result[count] = ratio;

			count++;

		}

		return result;

	}
	public Collection<March> findByMember(final int memberId) {
		Collection<March> result;

		result = this.marchRepository.findByMember(memberId);
		Assert.notNull(result);

		return result;
	}

	public Collection<March> findMarchByParade(final int paradeId) {
		Collection<March> result;

		result = this.marchRepository.findMarchByparade(paradeId);
		Assert.notNull(result);

		return result;
	}

	public Double[] ratioRejectedInAParade() {

		Collection<Parade> parades;
		Collection<March> marchsInAParade = new ArrayList<March>();
		final Collection<March> marchsRejectedInAParade = new ArrayList<March>();

		int count = 0;

		parades = this.paradeService.findAll();

		final Double[] result = new Double[parades.size()];

		for (final Parade p : parades) {

			Double ratio = 0.0;

			marchsInAParade = this.findMarchByParade(p.getId());
			Assert.notNull(marchsInAParade);

			for (final March m : marchsInAParade)
				if (m.getStatus().equals("REJECTED"))
					marchsRejectedInAParade.add(m);
			ratio = (double) ((double) marchsRejectedInAParade.size() / (double) marchsInAParade.size());

			result[count] = ratio;

			count++;

		}

		return result;

	}
	public Double[] ratioPendingInAParade() {

		Collection<Parade> parades;
		Collection<March> marchsInAParade = new ArrayList<March>();
		final Collection<March> marchsPendingInAParade = new ArrayList<March>();

		int count = 0;

		parades = this.paradeService.findAll();

		final Double[] result = new Double[parades.size()];

		for (final Parade p : parades) {

			Double ratio = 0.0;

			marchsInAParade = this.findMarchByParade(p.getId());
			Assert.notNull(marchsInAParade);

			for (final March m : marchsInAParade)
				if (m.getStatus().equals("PENDING"))
					marchsPendingInAParade.add(m);

			ratio = (double) ((double) marchsPendingInAParade.size() / (double) marchsInAParade.size());

			result[count] = ratio;

			count++;

		}

		return result;

	}

}
