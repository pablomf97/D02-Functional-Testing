package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CreditCardService;
import services.ParadeService;
import services.SponsorshipService;
import domain.CreditCard;
import domain.Parade;
import domain.Sponsor;
import domain.Sponsorship;

@Controller
@RequestMapping(value="/sponsorship")
public class SponsorshipController extends AbstractController{

	@Autowired
	private SponsorshipService sponsorshipService;

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private ParadeService paradeService;
	
	
	
	//Create ------------------------------------------------------
	
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public ModelAndView create(){
		final ModelAndView result;
		Sponsorship sponsorship;
		
		sponsorship = this.sponsorshipService.create();
		
		result = this.createEditModelAndView(sponsorship);
		
		return result;
	}
	
	//Edition ----------------------------------------------------------
	
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int sponsorshipId){
		final ModelAndView result;
		Sponsorship sponsorship;
		
		sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		Assert.notNull(sponsorship);
		
		result = new ModelAndView("sponsorship/display");
		result.addObject("sponsorship", sponsorship);
		
		return result;
	}
	
	@RequestMapping(value="/edit", method= RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId){
		final ModelAndView result;
		Sponsorship sponsorship;
		
		sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		Assert.notNull(sponsorship);
		
		result = this.createEditModelAndView(sponsorship);
		
		return result;
	}
	
	@RequestMapping(value="/edit", method =RequestMethod.POST, params="save")
	public ModelAndView save(final Sponsorship sponsorshipParam, final BindingResult binding){
		ModelAndView result;
		Sponsorship sponsorship;
		
		sponsorship = this.sponsorshipService.reconstruct(sponsorshipParam, binding);
		
		if(binding.hasErrors())
			result = this.createEditModelAndView(sponsorship);
		else
			try{
				this.sponsorshipService.save(sponsorship);
				result = new ModelAndView("redirect:/sponsorship/list.do");
				
			}catch(Throwable oops){
				result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");
			}
		
		return result;
		
		
	}
	
	@RequestMapping(value="/edit", method =RequestMethod.POST, params="delete")
	public ModelAndView delete(final Sponsorship sponsorshipParam, final BindingResult binding){
		ModelAndView result;
		Sponsorship sponsorship;
		
		sponsorship = this.sponsorshipService.reconstruct(sponsorshipParam, binding);
		
		if(binding.hasErrors())
			result = this.createEditModelAndView(sponsorship);
		else
			try{
				this.sponsorshipService.delete(sponsorship);
				result = new ModelAndView("redirect:/sponsorship/list.do");
				
			}catch(Throwable oops){
				result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");
			}
		
		return result;
		
		
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public ModelAndView list(){
		ModelAndView result;
		Collection<Sponsorship> sponsorships;
		Sponsor principal;
		
		principal = (Sponsor) this.actorService.findByPrincipal();
		
		sponsorships = this.sponsorshipService.getSponsorshipBySponsor(principal.getId());
		
		result = new ModelAndView("sponsorship/list");
		result.addObject("sponsorships", sponsorships);
		
		return result;
	}
	
	
	
	//Ancillary methods -----------------------------------------------------
	
	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship){
		ModelAndView result;

		result = this.createEditModelAndView(sponsorship,null);

		return result;

	}
	
	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String messageError){
		ModelAndView result;
		Collection<Sponsorship> sponsorships;
		Collection<CreditCard> selectedBySponsorship = new ArrayList<CreditCard>(),
														allCreditCards,
														notValidCreditCards = new ArrayList<CreditCard>();
		
		Collection<Parade> acceptedParades = new ArrayList<Parade>(), sponsorshipsParades = new ArrayList<Parade>();
		Sponsor principal = (Sponsor) this.actorService.findByPrincipal();
		boolean possible = false;
		
		if(principal.getId() == sponsorship.getSponsor().getId()){
			possible = true;
		}
		
		acceptedParades = this.paradeService.findFinalProcessions();
		allCreditCards = this.creditCardService.findAll();
		sponsorships = this.sponsorshipService.findAll();
		
		for(Sponsorship s : sponsorships){
			if(s.getCreditCard()!= null){
				selectedBySponsorship.add(s.getCreditCard());
			}
			
			sponsorshipsParades.add(s.getParade());
				
		
		}
		
		acceptedParades.removeAll(sponsorshipsParades);
		
		allCreditCards.removeAll(selectedBySponsorship);
		
		for(CreditCard c : allCreditCards){
			if(c.getExpirationYear().compareTo(Calendar.getInstance().get(Calendar.YEAR))<0 ){
				notValidCreditCards.add(c);
			}else{
				
			}if(c.getExpirationMonth().compareTo(Calendar.getInstance().get(Calendar.MONTH))<0){
				notValidCreditCards.add(c);
			}
		}
		
		
		
		allCreditCards.removeAll(notValidCreditCards);
		
		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorship", sponsorship);
		result.addObject("validCreditCards", allCreditCards);
		result.addObject("acceptedParades", acceptedParades);
		result.addObject("possible", possible);
		
		return result;
	}

	
	
}

