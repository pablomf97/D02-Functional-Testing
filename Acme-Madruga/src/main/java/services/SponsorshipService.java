package services;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.util.List;


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
	private SponsorService sponsorService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	// CRUD Methods -------------------------------------------------------
	public Sponsorship create() {
		final Sponsorship result;
		Sponsor principal;

		principal = (Sponsor) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "SPONSOR"));

		result = new Sponsorship();

		result.setIsDeactivated(false);
		result.setSponsor(principal);

		return result;

	}

	public Sponsorship save(final Sponsorship sponsorship) {
		Sponsorship saved = new Sponsorship();
		Sponsor principal;

		principal = (Sponsor) this.actorService.findByPrincipal();
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

	public void delete(Sponsorship sponsorship) {

		sponsorship.setIsDeactivated(true);
		sponsorship.setBanner("Eliminated sponsorship");

	}

	public Sponsorship reconstruct(final Sponsorship sponsorship,
			final BindingResult binding) {
		Sponsorship result = this.create();

		if (sponsorship.getId() == 0) {
			result.setBanner(sponsorship.getBanner());
			result.setTarget(sponsorship.getTarget());
			result.setCreditCard(sponsorship.getCreditCard());
			result.setParade(sponsorship.getParade());
		} else {
			Sponsorship sponsorshipBD = this.findOne(sponsorship.getId());

			sponsorshipBD.setBanner(sponsorship.getBanner());
			sponsorshipBD.setTarget(sponsorship.getTarget());

			result = sponsorshipBD;
		}

		this.validator.validate(result, binding);

		return result;
	}

	public Collection<Sponsorship> findAll() {
		Collection<Sponsorship> result;

		result = this.sponsorshipRepository.findAll();

		return result;
	}

	public Sponsorship findOne(int sponsorshipId) {
		Sponsorship result;

		result = this.sponsorshipRepository.findOne(sponsorshipId);

		return result;
	}

	public Collection<Sponsorship> getSponsorshipBySponsor(final int sponsorId) {
		Collection<Sponsorship> result;

		result = this.sponsorshipRepository.getSponsorshipsBySponsor(sponsorId);

		return result;
	}

	public Double ratioActiveSponsorship() {
		return this.sponsorshipRepository.ratioActiveSponsorship();
	}

	// The average, the minimum, the maximum, and the standard deviation of
	// active sponsorships per sponsor.

	public Integer maxActiveSponsorshipsPerSponsor(){
		Collection<Sponsor>sponsors=this.sponsorService.findAll();
		List<Integer> ships=new ArrayList<Integer> ();
		Integer res=0;
		for(Sponsor s :sponsors){

			ships.add(this.sponsorshipRepository.numberActiveSponsorshipPerSponsor(s.getId()));

		}
		res=Collections.max(ships);
		return res;

	}

	public Integer minActiveSponsorshipsPerSponsor(){
		Collection<Sponsor>sponsors=this.sponsorService.findAll();
		List<Integer> ships=new ArrayList<Integer> ();
		Integer res=0;
		for(Sponsor s :sponsors){

			ships.add(this.sponsorshipRepository.numberActiveSponsorshipPerSponsor(s.getId()));

		}
		res=Collections.min(ships);
		return res;

	}
	public Double avgActiveSponsorshipsPerSponsor(){
		Collection<Sponsor>sponsors=this.sponsorService.findAll();
		List<Integer> ships=new ArrayList<Integer> ();
		Double res=0.;
		for(Sponsor s :sponsors){

			ships.add(this.sponsorshipRepository.numberActiveSponsorshipPerSponsor(s.getId()));

		}
		res=this.calculateAverage(ships);
		return res;

	}
	private double calculateAverage(List <Integer> marks) {
		Integer sum = 0;
		if(!marks.isEmpty()) {
			for (Integer mark : marks) {
				sum += mark;
			}
			return sum.doubleValue() / marks.size();
		}
		return sum;
	}
	//Standard deviation
	public Double SteDevActiveSponsorshipsPerSponsor (){
		Collection<Sponsor>sponsors=this.sponsorService.findAll();
		List<Integer> ships=new ArrayList<Integer> ();
		for(Sponsor s :sponsors){

			ships.add(this.sponsorshipRepository.numberActiveSponsorshipPerSponsor(s.getId()));

		}
		
	    double mean = this.avgActiveSponsorshipsPerSponsor();
	    double temp = 0;

	    for (int i = 0; i < ships.size(); i++)
	    {
	        int val = ships.get(i);

	      
	        double squrDiffToMean = Math.pow(val - mean, 2);

	      
	        temp += squrDiffToMean;
	    }

	    
	    double meanOfDiffs = (double) temp / (double) (ships.size());

	    
	    return Math.sqrt(meanOfDiffs);
	}
	// The top-5 sponsors in terms of number of active sponsorships.

	/*	public Collection<Sponsor> top5SponsorsPerActiveSponsorships(){
		Collection<Sponsor>sponsors=this.sponsorService.findAll(); List<Integer>
		nShipsPerSponsor = new ArrayList<Integer>(); Collection<Sponsor> res =
		null; int sponsorships; if(sponsors.size()<=5){ res=sponsors; }else{

			for(Sponsor s :sponsors){

				sponsorships=this.sponsorshipRepository.numberActiveSponsorshipPerSponsor(
						s.getId()); nShipsPerSponsor.add(sponsorships);


			} Collections.sort(nShipsPerSponsor); nShipsPerSponsor.subList(0, 4);
			res= }

		return res; } 
	 */
	//The top-5 sponsors in terms of number of active sponsorships. 

	public Collection<Sponsor>top5SponsorsPerActiveSponsorships(){

		Collection<Sponsor>sponsors=this.sponsorService.findAll();
		Collection<Sponsor> res = null; 
		Map<Sponsor,Integer> map=new HashMap<Sponsor,Integer>() ; 
		int sponsorships; 
		if(sponsors.size()<=5){

			res=sponsors; 
		}
		else{

			for(Sponsor s :sponsors){

				sponsorships=this.sponsorshipRepository.numberActiveSponsorshipPerSponsor(s.getId()); 
				map.put(s, sponsorships);

			} 
			Collections.sort((List<Integer>) map.values()); sponsors=map.keySet();
			List<Sponsor> sortedList = new ArrayList<Sponsor>(sponsors);
			res=sortedList.subList(0,4);

		}
		return res; 
	}

}
