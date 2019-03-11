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
import domain.Procession;


@Service
@Transactional
public class MarchService {

	// Managed repository ------------------------------------

	@Autowired
	private MarchRepository marchRepository;

	// Supporting services -----------------------------------

	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	@Autowired
	private ProcessionService processionService;


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

		if(this.actorService.checkAuthority(principal, "MEMBER")){

			Assert.isTrue(march.getId()== 0, "wrong.id");

			member = (Member) principal;
			march.setStatus("PENDING");
			march.setRow(null);
			march.setCol(null);
			march.setMember(member);

		} else if (this.actorService.checkAuthority(principal, "BROTHERHOOD")){

			Assert.isTrue(march.getId() != 0);
			Assert.notNull(march.getMember());
			Assert.notNull(march.getProcession());
			Assert.notNull(march.getStatus());
			
			brotherhood = (Brotherhood) principal;

			Assert.isTrue(march.getProcession().getBrotherhood().equals(brotherhood), "not.allowed");
			if(march.getStatus() == "REJECT") {
				Assert.notNull(march.getReason());
			}

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

	public March reconstruct(March march, BindingResult binding) {
		March result;
		Actor principal = null;

		if(march.getId() == 0) {
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

		validator.validate(result, binding);

		return result;
	}

	public Collection<March> findMarchsByMemberId(int memberId) {
		Collection<March> result;

		result = this.marchRepository.findMarchsByMemberId(memberId);

		return result;
	}

	public Collection<March> findMarchsByBrotherhoodId(int brotherhoodId) {
		Collection<March> result;

		result = this.marchRepository.findMarchsByBrotherhoodId(brotherhoodId);

		return result;
	}

	public Double ratioApprovedRequests(){
		Double result;

		result = this.marchRepository.ratioApprovedRequests();
		

		return result;
	}

	public Double ratioPendingRequests(){
		Double result;

		result = this.marchRepository.ratioPendingRequests();
		

		return result;
	}

	public Double ratioRejectedRequests(){
		Double result;

		result = this.marchRepository.ratioRejectedRequests();
		

		return result;
	}


	public Double[] ratioApprovedInAProcession(){
		
		Collection<Procession> processions;
		Collection<March> marchsInAProcession = new ArrayList<March>(),marchsApprovedInAProcession = new ArrayList<March>();

		int count = 0;

		processions = this.processionService.findAll();
		
		Double[] result = new Double[processions.size()];
		
		for(Procession p : processions){

			Double ratio = 0.0;

			marchsInAProcession = this.findMarchByProcession(p.getId());
			

			for(March m : marchsInAProcession){

				if(m.getStatus().equals("APPROVED")){
					marchsApprovedInAProcession.add(m);
				}

			}
			
			if(marchsInAProcession.size() != 0){
				ratio = (double) (marchsApprovedInAProcession.size()/marchsInAProcession.size());
			}


			result[count] = ratio;


			count++;



		}



		return result;

	}
	public Collection<March> findByMember(int memberId){
		Collection<March> result;

		result = this.marchRepository.findByMember(memberId);
		Assert.notNull(result);

		return result;
	}
	

	public Collection<March> findMarchByProcession(int processionId){
		Collection<March> result;

		result = this.marchRepository.findMarchByProcession(processionId);
		Assert.notNull(result);

		return result;
	}
	
	public Double[] ratioRejectedInAProcession(){
		
		Collection<Procession> processions;
		Collection<March> marchsInAProcession = new ArrayList<March>(),marchsRejectedInAProcession = new ArrayList<March>();

		int count = 0;

		processions = this.processionService.findAll();
		
		Double[] result = new Double[processions.size()];
		
		for(Procession p : processions){

			Double ratio = 0.0;

			marchsInAProcession = this.findMarchByProcession(p.getId());
			Assert.notNull(marchsInAProcession);

			for(March m : marchsInAProcession){

				if(m.getStatus().equals("REJECTED")){
					marchsRejectedInAProcession.add(m);
				}

			}
			ratio = (double) ((double)marchsRejectedInAProcession.size()/(double)marchsInAProcession.size());


			result[count] = ratio;


			count++;



		}



		return result;

	}
	public Double[] ratioPendingInAProcession(){
		
		Collection<Procession> processions;
		Collection<March> marchsInAProcession = new ArrayList<March>(),marchsPendingInAProcession = new ArrayList<March>();

		int count = 0;

		processions = this.processionService.findAll();
		
		Double[] result = new Double[processions.size()];
		
		for(Procession p : processions){

			Double ratio = 0.0;

			marchsInAProcession = this.findMarchByProcession(p.getId());
			Assert.notNull(marchsInAProcession);

			for(March m : marchsInAProcession){

				if(m.getStatus().equals("PENDING")){
					marchsPendingInAProcession.add(m);
				}

			}

			ratio = (double) ((double)marchsPendingInAProcession.size()/(double)marchsInAProcession.size());


			result[count] = ratio;


			count++;



		}



		return result;

	}


}
