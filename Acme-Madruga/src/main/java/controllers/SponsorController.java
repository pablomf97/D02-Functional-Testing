package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;

import domain.Sponsor;

import forms.SponsorForm;

import services.ActorService;
import services.SponsorService;
import services.UtilityService;

@Controller
@RequestMapping("/sponsor")
public class SponsorController extends AbstractController{

	@Autowired
	private SponsorService sponsorService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UtilityService			utilityService;

	public SponsorController() {
		super();
	}
	

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayGET(@RequestParam final int id) {
		ModelAndView result;
		Sponsor a;
		Boolean isPrincipal = false;
		Actor principal;
		
		try {
			principal = this.actorService.findByPrincipal();
			if(principal.getId() == id) {
				isPrincipal = true;
			}
			
			result = new ModelAndView("sponsor/display");
			a = this.sponsorService.findOne(id);
			Assert.isTrue(a.equals(this.actorService.findByPrincipal()));
			result.addObject("sponsor", a);
			result.addObject("isPrincipal", isPrincipal);
		} catch (final Throwable opps) {
			//TODO: ver la posibilidada de una pantalla de error
			result = new ModelAndView("redirect:/welcome/index.do");

		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editGET(@RequestParam final int id) {
		ModelAndView result;
		Sponsor a;
		SponsorForm af;
		af = this.sponsorService.createForm();
		if (id == 0) {
			result = new ModelAndView("sponsor/edit");
			a = this.sponsorService.create();
			a.setId(0);
			result.addObject("sponsor", a);
			result.addObject("sponsorForm", af);
		} else
			try {
				result = new ModelAndView("sponsor/edit");
				a = this.sponsorService.findOne(id);
				Assert.isTrue(a.equals(this.actorService.findByPrincipal()));
				af.setId(a.getId());
				result.addObject("sponsorForm", af);
				result.addObject("sponsor", a);
				result.addObject("uri", "sponsor/edit.do");

			} catch (final Throwable opps) {
				//TODO: ver la posibilidada de una pantalla de error
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editPOST(final SponsorForm sponsorForm, final BindingResult binding) {
		ModelAndView result;
		String emailError = "";
		String passW = "";
		String uniqueUsername = "";
		Sponsor admin;
		try {
			admin = this.sponsorService.reconstruct(sponsorForm, binding);
			if (admin.getId() == 0) {
				passW = this.actorService.checkPass(sponsorForm.getPassword(), sponsorForm.getPassword2());
				uniqueUsername = this.actorService.checkUniqueUser(sponsorForm.getUsername());
			}
			admin.setEmail(admin.getEmail().toLowerCase());
			emailError = this.actorService.checkEmail(admin.getEmail(), admin.getUserAccount().getAuthorities().iterator().next().getAuthority());
			if (binding.hasErrors() || !emailError.isEmpty() || !passW.isEmpty() || !uniqueUsername.isEmpty()) {
				result = new ModelAndView("sponsor/edit");
				result.addObject("uri", "sponsor/edit.do");
				admin.getUserAccount().setPassword("");
				result.addObject("sponsor", admin);
				result.addObject("emailError", emailError);
				result.addObject("checkPass", passW);
				result.addObject("uniqueUsername", uniqueUsername);
			} else
				try {
					admin.setPhoneNumber(this.actorService.checkSetPhoneCC(admin.getPhoneNumber()));
					final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
					final String hash = encoder.encodePassword(admin.getUserAccount().getPassword(), null);
					admin.getUserAccount().setPassword(hash);
					this.sponsorService.save(admin);
					result = new ModelAndView("redirect:/welcome/index.do");
				} catch (final Throwable opps) {
					result = new ModelAndView("sponsor/edit");
					result.addObject("uri", "sponsor/edit.do");
					result.addObject("messageCode", "actor.commit.error");
					result.addObject("emailError", emailError);
					admin.getUserAccount().setPassword("");
					result.addObject("sponsor", admin);
				}
		} catch (final Throwable opps) {
			//TODO: pantalla de error
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}


	
	
}
