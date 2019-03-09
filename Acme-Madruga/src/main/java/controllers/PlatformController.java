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
import services.PlatformService;
import domain.Actor;
import domain.Brotherhood;
import domain.Platform;

@Controller
@RequestMapping("/platform")
public class PlatformController extends AbstractController {

	// Services

	@Autowired
	private PlatformService platformService;

	@Autowired
	private ActorService actorService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int platformId) {

		ModelAndView result = null;
		Platform platform;
		boolean isPrincipal = false;
		Actor principal;
		try{
			principal = this.actorService.findByPrincipal();
			platform = this.platformService.findOne(platformId);
			if (platform.getBrotherhood().getId() == principal.getId())
				isPrincipal = true;

		}catch(Throwable oops){
			platform = this.platformService.findOne(platformId);

		}

		result = new ModelAndView("platform/display");
		result.addObject("platform", platform);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("requestURI", "platform/display.do?platformId="
				+ platformId);
		return result;


	}

	// List
	@RequestMapping(value = "/list")
	public ModelAndView list(@RequestParam Integer brotherhoodId) {
		ModelAndView result = null;
		Actor principal;
		Collection<Platform> platforms;
		String requestURI;
		platforms = this.platformService
				.findPlatformsByBrotherhoodId(brotherhoodId);
		try {
			principal = this.actorService.findByPrincipal();
			requestURI = "platform/list.do?=brotherhoodId"
					+ brotherhoodId;

		} catch (Throwable oops){

			requestURI = "platform/list.do?=brotherhoodId"
					+ brotherhoodId;

		}
		result = new ModelAndView("platform/list");
		result.addObject("requestURI", requestURI);
		result.addObject("platforms", platforms);

		return result;
	}

	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Platform platform;
		Actor principal;
		Boolean error;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"BROTHERHOOD"));

			platform = this.platformService.create();

			result = this.createEditModelAndView(platform);
		} catch (IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (Throwable oopsie) {

			result = new ModelAndView("platform/list");
			error = true;

			result.addObject("oopsie", oopsie);
			result.addObject("error", error);
		}
		return result;
	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int platformId) {
		ModelAndView result;
		final Platform platform;
		Actor principal;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"BROTHERHOOD"));

			platform = this.platformService.findOne(platformId);
			Assert.notNull(platform);
			result = this.createEditModelAndView(platform);
		} catch (IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (Throwable oopsie) {
			result = new ModelAndView("redirect:/enrolment/member/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Platform platform, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(platform);
		else
			try {
				platform = this.platformService.reconstruct(platform, binding);
				this.platformService.save(platform);
				result = new ModelAndView("redirect:list.do?brotherhoodId="+platform.getBrotherhood().getId());
			} catch (IllegalArgumentException oops) {
				result = new ModelAndView("misc/403");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(platform,
						"platform.commit.error");
			}

		return result;

	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Platform platform, final BindingResult binding) {
		ModelAndView result;
			
		try {
			platform = this.platformService.reconstruct(platform, binding);
			int brotherhoodId = platform.getBrotherhood().getId();
			
			this.platformService.delete(platform);
			
			result = new ModelAndView("redirect:list.do?brotherhoodId="+brotherhoodId);
		} catch (final Throwable oops) {
			result = new ModelAndView("welcome/index");

		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Platform platform) {
		ModelAndView result;

		result = this.createEditModelAndView(platform, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Platform platform,
			final String messageCode) {
		final ModelAndView result;
		Actor principal;
		boolean isBrotherhood = false;
		boolean isPrincipal = false;

		principal = this.actorService.findByPrincipal();

		Brotherhood actorBrother = (Brotherhood) principal;

		if(this.actorService.checkAuthority(principal, "BROTHERHOOD")) {

			isBrotherhood = true;
		}

		if (platform.getId() != 0
				&& platform.getBrotherhood().getId() == principal.getId())
			isPrincipal = true;

		result = new ModelAndView("platform/edit");
		result.addObject("platform", platform);
		result.addObject("isBrotherhood", isBrotherhood);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("message", messageCode);
		result.addObject("actorBrother", actorBrother);

		return result;

	}
}
