/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

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

import services.ActorService;
import services.AdministratorService;
import services.UtilityService;
import domain.Actor;
import domain.Administrator;
import forms.AdministratorForm;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private UtilityService utilityService;

	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayGET(@RequestParam final int id) {
		ModelAndView result;
		Administrator a;
		Boolean isPrincipal = false;
		Actor principal;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "ADMINISTRATOR"));
			if (principal.getId() == id) {
				isPrincipal = true;
			}

			result = new ModelAndView("administrator/display");
			a = this.administratorService.findOne(id);
			result.addObject("administrator", a);
			result.addObject("isPrincipal", isPrincipal);
		} catch (final Throwable opps) {
			// TODO: ver la posibilidada de una pantalla de error
			result = new ModelAndView("redirect:/welcome/index.do");

		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editGET(@RequestParam final int id) {
		ModelAndView result;
		Administrator a;
		AdministratorForm af;
		af = this.administratorService.createForm();
		if (id == 0) {
			result = new ModelAndView("administrator/edit");
			a = this.administratorService.create();
			a.setId(0);
			result.addObject("administrator", a);
			result.addObject("administratorForm", af);
		} else
			try {
				result = new ModelAndView("administrator/edit");
				a = this.administratorService.findOne(id);
				Assert.isTrue(a.equals(this.actorService.findByPrincipal()));
				af.setId(a.getId());
				result.addObject("administratorForm", af);
				result.addObject("administrator", a);
				result.addObject("uri", "administrator/edit.do");

			} catch (final Throwable opps) {
				// TODO: ver la posibilidada de una pantalla de error
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		return result;
	}

	// Flag spammers
	@RequestMapping(value = "/flag-spammers", method = RequestMethod.GET)
	public ModelAndView flagSpammers() {
		ModelAndView res;
		Actor a;

		a = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(a, "ADMINISTRATOR"));

		this.utilityService.checkSpammers();

		res = new ModelAndView("redirect:/administrator/display.do?id="
				+ a.getId());
		res.addObject("administrator", a);

		return res;
	}

	// Compute score
	@RequestMapping(value = "/compute-scores", method = RequestMethod.GET)
	public ModelAndView computeScores() {
		ModelAndView res;
		Actor a;

		a = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(a, "ADMINISTRATOR"));

		this.utilityService.computeScore();

		res = new ModelAndView("redirect:/administrator/display.do?id="
				+ a.getId());
		res.addObject("administrator", a);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editPOST(final AdministratorForm administratorForm,
			final BindingResult binding) {
		ModelAndView result;
		String emailError = "";
		String passW = "";
		String uniqueUsername = "";
		Administrator admin;
		try {
			admin = this.administratorService.reconstruct(administratorForm,
					binding);
			if (admin.getId() == 0) {
				passW = this.actorService.checkPass(
						administratorForm.getPassword(),
						administratorForm.getPassword2());
				uniqueUsername = this.actorService
						.checkUniqueUser(administratorForm.getUsername());
			}
			admin.setEmail(admin.getEmail().toLowerCase());
			emailError = this.actorService.checkEmail(admin.getEmail(), admin
					.getUserAccount().getAuthorities().iterator().next()
					.getAuthority());
			if (binding.hasErrors() || !emailError.isEmpty()
					|| !passW.isEmpty() || !uniqueUsername.isEmpty()) {
				result = new ModelAndView("administrator/edit");
				result.addObject("uri", "administrator/edit.do");
				admin.getUserAccount().setPassword("");
				result.addObject("administrator", admin);
				result.addObject("emailError", emailError);
				result.addObject("checkPass", passW);
				result.addObject("uniqueUsername", uniqueUsername);
			} else
				try {
					admin.setPhoneNumber(this.actorService
							.checkSetPhoneCC(admin.getPhoneNumber()));
					final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
					final String hash = encoder.encodePassword(admin
							.getUserAccount().getPassword(), null);
					admin.getUserAccount().setPassword(hash);
					this.administratorService.save(admin);
					result = new ModelAndView("redirect:/welcome/index.do");
				} catch (final Throwable opps) {
					result = new ModelAndView("administrator/edit");
					result.addObject("uri", "administrator/edit.do");
					result.addObject("messageCode", "actor.commit.error");
					result.addObject("emailError", emailError);
					admin.getUserAccount().setPassword("");
					result.addObject("administrator", admin);
				}
		} catch (final Throwable opps) {
			// TODO: pantalla de error
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

}
