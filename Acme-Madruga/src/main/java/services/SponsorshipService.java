package services;


import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import domain.Sponsor;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	@Autowired
	private SponsorshipRepository sponsorshipRepository;

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private Validator validator;

	//CRUD Methods -------------------------------------------------------
	public Sponsorship create(){
		final Sponsorship result;
		Sponsor principal;

		principal = (Sponsor) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "SPONSOR"));

		result = new Sponsorship();

		result.setIsDeactivated(false);
		result.setSponsor(principal);

		return result;

	}

	public Sponsorship save(final Sponsorship sponsorship){
		Sponsorship saved = new Sponsorship();
		Sponsor principal;

		principal = (Sponsor)this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "SPONSOR"));

		Assert.isTrue(sponsorship.getSponsor().getId() == principal.getId());


		Assert.notNull(sponsorship.getBanner());
		Assert.notNull(sponsorship.getTarget());
		Assert.notNull(sponsorship.getCreditCard());
		Assert.notNull(sponsorship.getParade());
		Assert.isTrue(sponsorship.getIsDeactivated() == false);
		Assert.notNull(sponsorship.getSponsor());

		saved = this.sponsorshipRepository.save(sponsorship);

		return saved;
	}

	public void delete(Sponsorship sponsorship, BindingResult binding){
		
		Assert.isTrue(sponsorship.getId() != 0);
		
		Sponsorship sponsorshipBD = this.sponsorshipRepository.findOne(sponsorship.getId());
		
		
		sponsorshipBD.setIsDeactivated(true);
		sponsorshipBD.setTarget("Eliminated sponsorship");
		
		this.validator.validate(sponsorshipBD, binding);

	}

	public Sponsorship reconstruct(final Sponsorship sponsorship, final BindingResult binding){
		Sponsorship result = this.create();
		
		if(sponsorship.getId()==0){
			result.setBanner(sponsorship.getBanner());
			result.setTarget(sponsorship.getTarget());
			result.setCreditCard(sponsorship.getCreditCard());
			result.setParade(sponsorship.getParade());
		}else{
			Sponsorship sponsorshipBD = this.findOne(sponsorship.getId());
			
			result = sponsorshipBD;
			
			result.setBanner(sponsorship.getBanner());
			result.setTarget(sponsorship.getTarget());	
		
		}
		
		this.validator.validate(result, binding);
		
		return result;
	}



	public Collection<Sponsorship> findAll(){
		Collection<Sponsorship> result;

		result = this.sponsorshipRepository.findAll();

		return result;
	}

	public Sponsorship findOne(int sponsorshipId){
		Sponsorship result;

		result = this.sponsorshipRepository.findOne(sponsorshipId);

		return result;
	}
	
	public Collection<Sponsorship> getSponsorshipBySponsor(final int sponsorId){
		Collection<Sponsorship> result;
		
		result = this.sponsorshipRepository.getSponsorshipsBySponsor(sponsorId);
		
		return result;
	}
}

