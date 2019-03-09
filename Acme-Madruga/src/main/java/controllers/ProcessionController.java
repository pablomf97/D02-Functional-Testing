package controllers;

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
import services.MessageService;
import services.PlatformService;
import services.ProcessionService;
import domain.Actor;
import domain.Brotherhood;
import domain.Platform;
import domain.Procession;

@Controller
@RequestMapping("/procession")
public class ProcessionController extends AbstractController {

	// Services

	@Autowired
	private ProcessionService processionService;

	@Autowired
	private PlatformService platformService;

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private MessageService messageService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int processionId) {

		ModelAndView result;
		Procession procession;
		boolean isPrincipal = false;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		procession = this.processionService.findOne(processionId);

		if (procession.getBrotherhood().getId() == principal.getId())
			isPrincipal = true;

		result = new ModelAndView("procession/display");
		result.addObject("procession", procession);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("requestURI", "procession/display.do?processionId="
				+ processionId);

		return result;
	}

	// List

	@RequestMapping(value = "/member,brotherhood/list")
	public ModelAndView list(@RequestParam(required = false) Integer memberId,
			@RequestParam(required = false) Integer brotherhoodId) {
		ModelAndView result;
		Collection<Procession> processions;
		Actor principal;

		Boolean permission;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(!this.actorService.checkAuthority(principal,
					"ADMINISTRATOR"));

			permission = true;

			principal = this.actorService.findByPrincipal();

			if (this.actorService.checkAuthority(principal, "BROTHERHOOD")) {

				processions = this.processionService
						.findProcessionsByBrotherhoodId(principal.getId());

				String requestURI = "procession/member,brotherhood/list.do?brotherhoodId="
						+ principal.getId();
				result = new ModelAndView("procession/list");
				result.addObject("requestURI", requestURI);
				result.addObject("processions", processions);

			} else {

				Collection<Procession> toApply;

				processions = this.processionService
						.findAcceptedProcessionsByMemberId(principal.getId());
				toApply = this.processionService.processionsToApply(principal
						.getId());

				String requestURI = "procession/member,brotherhood/list.do?memberId="
						+ principal.getId();
				result = new ModelAndView("procession/list");
				result.addObject("requestURI", requestURI);
				result.addObject("processions", processions);
				result.addObject("toApply", toApply);

			}
		} catch (IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (Throwable oopsie) {

			result = new ModelAndView("march/member,brotherhood/list");
			permission = false;

			result.addObject("oopsie", oopsie);
			result.addObject("permission", permission);
		}

		return result;
	}

	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Procession procession;

		Actor principal;
		Boolean error;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"BROTHERHOOD"));

			procession = this.processionService.create();

			result = this.createEditModelAndView(procession);
		} catch (IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (Throwable oopsie) {

			result = new ModelAndView("procession/member,brotherhood/list");
			error = true;

			result.addObject("oopsie", oopsie);
			result.addObject("error", error);
		}

		return result;

	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int processionId) {
		ModelAndView result;
		Procession procession;
		Actor principal;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"BROTHERHOOD"));

			procession = this.processionService.findOne(processionId);
			Assert.notNull(procession);
			result = this.createEditModelAndView(procession);

		} catch (IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (Throwable oopsie) {
			result = new ModelAndView("redirect:/enrolment/member/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(Procession procession,
			final BindingResult binding) {
		ModelAndView result;

		
		
		if (binding.hasErrors())
			result = this.createEditModelAndView(procession);
		else
			try {
				procession.setIsDraft(false);
				procession = this.processionService.reconstruct(procession,
						binding);
				this.processionService.save(procession);
				
				this.messageService.notificationPublishProcession(procession);
				
				result = new ModelAndView("redirect:member,brotherhood/list.do");
			} catch (IllegalArgumentException oops) {
				result = new ModelAndView("misc/403");
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Procession procession, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(procession);
		else
			try {
				procession.setIsDraft(true);
				procession = this.processionService.reconstruct(procession,
						binding);
				this.processionService.save(procession);
				result = new ModelAndView("redirect:member,brotherhood/list.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Procession procession,
			final BindingResult binding) {
		ModelAndView result;

		procession.setIsDraft(false);
		procession = this.processionService.reconstruct(procession, binding);
		try {
			this.processionService.delete(procession);
			result = new ModelAndView("redirect:member,brotherhood/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(procession,
					"procession.commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Procession procession) {
		ModelAndView result;

		result = this.createEditModelAndView(procession, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Procession procession,
			final String messageCode) {
		final ModelAndView result;
		Actor principal;
		boolean isPrincipal = false;
		Collection<Platform> platforms;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"), "not.allowed");
		
		Brotherhood actorBrother = (Brotherhood) principal;

		
		if(procession.getId() != 0 && procession.getBrotherhood().getId() == principal.getId())
			isPrincipal = true;

		platforms = this.platformService.findPlatformsByBrotherhoodId(principal
				.getId());

		result = new ModelAndView("procession/edit");
		result.addObject("procession", procession);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("message", messageCode);
		result.addObject("platforms", platforms);
		result.addObject("actorBrother", actorBrother);

		return result;
	}

}
