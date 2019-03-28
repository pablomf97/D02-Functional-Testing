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
import services.ParadeService;
import services.PlatformService;
import domain.Actor;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Parade;
import domain.Platform;

@Controller
@RequestMapping("/parade")
public class ParadeController extends AbstractController {

	// Services

	@Autowired
	private ParadeService paradeService;

	@Autowired
	private PlatformService platformService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private MessageService messageService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {

		ModelAndView result;
		Parade parade;
		boolean isPrincipal = false;
		Actor principal;

		parade = this.paradeService.findOne(paradeId);

		try {
			principal = this.actorService.findByPrincipal();

			if (parade.getBrotherhood().getId() == principal.getId())
				isPrincipal = true;
		} catch (final Throwable oops) {
		}

		result = new ModelAndView("parade/display");
		result.addObject("parade", parade);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("requestURI", "parade/display.do?paradeId=" + paradeId);

		return result;
	}

	// List

	@RequestMapping(value = "/member,brotherhood/list")
	public ModelAndView list(
			@RequestParam(required = false) final Integer brotherhoodId) {
		ModelAndView result;
		Collection<Parade> parades = null;
		Actor principal;
		String requestURI;
		Boolean isPrincipal = false;
		Boolean permission = true;
		
		try {
				if(brotherhoodId != null) {
					parades = this.paradeService
							.findParadesByBrotherhoodId(brotherhoodId);

					requestURI = "parade/member,brotherhood/list.do?brotherhoodId="
							+ brotherhoodId;
				} else {
					principal = this.actorService.findByPrincipal();
					isPrincipal = true;
					parades = this.paradeService
							.findParadesByBrotherhoodId(principal.getId());

					requestURI = "parade/member,brotherhood/list.do?brotherhoodId="
							+ principal.getId();
				}
				
				result = new ModelAndView("parade/list");
				result.addObject("requestURI", requestURI);
				result.addObject("parades", parades);
				result.addObject("permission", permission);
				result.addObject("isPrincipal", isPrincipal);

			
		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (final Throwable oopsie) {

			result = new ModelAndView("parade/member,brotherhood/list");
			permission = false;

			result.addObject("oopsie", oopsie);
			result.addObject("permission", permission);
		} finally {}

		return result;
	}
	
	@RequestMapping(value = "/member/list")
	public ModelAndView listMember(@RequestParam(required = false) final Integer memberId) {
		ModelAndView result;
		Collection<Parade> parades, toApply;
		Member principal;
		String requestURI;
		Boolean permission = false;

		try {
			principal = (Member) this.actorService.findByPrincipal();
			permission = true;

			parades = this.paradeService
					.findAcceptedParadesByMemberId(principal.getId());
			toApply = this.paradeService.paradesToApply(principal.getId());

			requestURI = "parade/member/list.do?memberId="
					+ memberId;

			result = new ModelAndView("parade/list");
			result.addObject("requestURI", requestURI);
			result.addObject("parades", parades);
			result.addObject("permission", permission);
			result.addObject("toApply", toApply);

		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		}
		return result;
	}

	@RequestMapping(value = "/chapter/list")
	public ModelAndView listChapter(@RequestParam(required = false) final Integer chapterId) {
		ModelAndView result;
		Collection<Parade> parades;
		Chapter principal;
		String requestURI;
		Boolean permission = false;

		try {
			principal = (Chapter) this.actorService.findByPrincipal();
			permission = true;

			parades = this.paradeService.findParadesByAres(principal.getZone()
					.getId());
			requestURI = "parade/chapter/list.do?chapterId="
					+ chapterId;

			result = new ModelAndView("chapter/listparade");
			result.addObject("requestURI", requestURI);
			result.addObject("parades", parades);
			result.addObject("permission", permission);

		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		}
		return result;
	}

	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Parade parade;

		Actor principal;
		Boolean error;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"BROTHERHOOD"));

			parade = this.paradeService.create();

			result = this.createEditModelAndView(parade);
		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (final Throwable oopsie) {

			result = new ModelAndView("parade/member,brotherhood/list");
			error = true;

			result.addObject("oopsie", oopsie);
			result.addObject("error", error);
		}

		return result;

	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;
		Actor principal;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"BROTHERHOOD"));

			parade = this.paradeService.findOne(paradeId);
			Assert.notNull(parade);
			result = this.createEditModelAndView(parade);

		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (final Throwable oopsie) {
			result = new ModelAndView("redirect:/enrolment/member/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(Parade parade, final BindingResult binding) {
		ModelAndView result;
		parade = this.paradeService.reconstruct(parade, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(parade);
		else
			try {
				parade.setIsDraft(false);
				this.paradeService.save(parade);

				this.messageService.notificationPublishParade(parade);

				result = new ModelAndView("redirect:member,brotherhood/list.do");
			} catch (final IllegalArgumentException oops) {
				result = new ModelAndView("misc/403");
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Parade parade, final BindingResult binding) {
		ModelAndView result;
		parade = this.paradeService.reconstruct(parade, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(parade);
		else
			try {
				parade.setIsDraft(true);
				this.paradeService.save(parade);
				result = new ModelAndView("redirect:member,brotherhood/list.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Parade parade, final BindingResult binding) {
		ModelAndView result;

		parade.setIsDraft(false);
		parade = this.paradeService.reconstruct(parade, binding);
		try {
			this.paradeService.delete(parade);
			result = new ModelAndView("redirect:member,brotherhood/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(parade, "parade.commit.error");
		}

		return result;
	}

	// Accept
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;
		boolean isPrincipal = false;
		Chapter principal;

		principal = (Chapter) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CHAPTER"));
		parade = this.paradeService.findOne(paradeId);
		Assert.notNull(parade);

		if (parade.getBrotherhood().getZone().equals(principal.getZone())) {
			isPrincipal = true;
			parade.setStatus("ACCEPTED");
			this.paradeService.save(parade);
		}

		this.paradeService.save(parade);
		
		Collection <Parade> coll = this.paradeService.findParadesByAres(parade.getBrotherhood().getZone().getId());

		result = new ModelAndView("redirect:chapter/list.do");
		result.addObject("parades", coll);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("parade", parade);

		return result;
	}

	// Reject
	@RequestMapping(value = "/rejectv", method = RequestMethod.GET)
	public ModelAndView rejectView(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;
		boolean isPrincipal = false;
		Chapter principal;

		principal = (Chapter) this.actorService.findByPrincipal();
		parade = this.paradeService.findOne(paradeId);
		Assert.notNull(parade);

		if (parade.getBrotherhood().getZone().equals(principal.getZone()))
			isPrincipal = true;
		result = new ModelAndView("parade/reject");
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("parade", parade);

		return result;
	}

	@RequestMapping(value = "/rejectb", method = RequestMethod.POST, params = "reject")
	public ModelAndView reject(Parade parade, final BindingResult binding) {
		ModelAndView result;
		Actor principal;

		parade.setStatus("REJECTED");
		parade = this.paradeService.reconstruct(parade, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(parade);
		else
			try {
				principal = this.actorService.findByPrincipal();
				Assert.isTrue(this.actorService.checkAuthority(principal, "CHAPTER"));
				this.paradeService.save(parade);
				
				Collection <Parade> coll = this.paradeService.findParadesByAres(parade.getBrotherhood().getZone().getId());

				result = new ModelAndView("redirect:chapter/list.do");
				result.addObject("parades", coll);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(parade,
						"march.commit.error");
			}
		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Parade parade) {
		ModelAndView result;

		result = this.createEditModelAndView(parade, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Parade parade,
			final String messageCode) {
		final ModelAndView result;
		Actor principal;
		boolean isPrincipal = false;
		Collection<Platform> platforms;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");

		final Brotherhood actorBrother = (Brotherhood) principal;

		platforms = this.platformService.findPlatformsByBrotherhoodId(principal
				.getId());

		result = new ModelAndView("parade/edit");
		result.addObject("parade", parade);
		result.addObject("message", messageCode);
		result.addObject("platforms", platforms);
		result.addObject("actorBrother", actorBrother);

		return result;
	}

	@RequestMapping(value = "/copy", method = RequestMethod.GET)
	public ModelAndView copy(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;
		Boolean error;
		try {
			parade = this.paradeService.copyParade(paradeId);
			result = new ModelAndView("redirect:/parade/display.do?paradeId="
					+ parade.getId());
		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (final Throwable oopsie) {
			result = new ModelAndView("parade/member,brotherhood/list");
			error = true;
			result.addObject("oopsie", oopsie);
			result.addObject("error", error);
		}
		return result;
	}
}
