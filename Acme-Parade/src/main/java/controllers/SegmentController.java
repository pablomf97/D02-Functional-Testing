
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
import services.ParadeService;
import services.SegmentService;
import domain.Actor;
import domain.Brotherhood;
import domain.Parade;
import domain.Segment;

@Controller
@RequestMapping("/segment")
public class SegmentController extends AbstractController {

	// Services

	@Autowired
	private ActorService	actorService;

	@Autowired
	private SegmentService	segmentService;

	@Autowired
	private ParadeService	paradeService;


	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int segmentId) {

		ModelAndView result = null;
		Segment segment;
		boolean isPrincipal = false;
		Actor principal;
		try {
			principal = this.actorService.findByPrincipal();
			segment = this.segmentService.findOne(segmentId);
			if (principal != null && segment.getParade().getBrotherhood().getId() == principal.getId())
				isPrincipal = true;

		} catch (final Throwable oops) {
			segment = this.segmentService.findOne(segmentId);

		}

		result = new ModelAndView("segment/display");
		result.addObject("segment", segment);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("requestURI", "segment/display.do?segmentId=" + segmentId);
		return result;

	}

	// List
	@RequestMapping(value = "/list")
	public ModelAndView list(@RequestParam final Integer paradeId) {
		ModelAndView result = null;
		final Actor principal;
		Collection<Segment> segments = null;
		String requestURI;
		final int pId = paradeId;
		try {
			requestURI = "segment/list.do?paradeId=" + paradeId;
			segments = this.segmentService.findAllSegmentsByParadeId(paradeId);
		} catch (final Throwable oops) {
			requestURI = "parade/display.do?paradeId=" + paradeId;
		}
		result = new ModelAndView("segment/list");
		result.addObject("requestURI", requestURI);
		result.addObject("segments", segments);
		result.addObject("pId", pId);

		return result;
	}

	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		ModelAndView result;
		Segment segment;
		Actor principal;
		Boolean error;
		Parade par = null;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));

			segment = this.segmentService.create();
			par = this.paradeService.findOne(paradeId);

			result = this.createEditModelAndView(segment);
		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (final Throwable oopsie) {

			result = new ModelAndView("redirect:/parade/member,brotherhood/list.do");
			error = true;

			result.addObject("oopsie", oopsie);
			result.addObject("error", error);
		}
		result.addObject("par", par);

		return result;
	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int segmentId) {
		ModelAndView result;
		final Segment segment;
		Actor principal;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));

			segment = this.segmentService.findOne(segmentId);
			Assert.notNull(segment);
			result = this.createEditModelAndView(segment);
		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (final Throwable oopsie) {
			result = new ModelAndView("redirect:/parade/member,brotherhood/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Segment segment, final BindingResult binding) {
		ModelAndView result;

		try {
			final Segment res = this.segmentService.reconstruct(segment, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(segment);
			else
				try {

					this.segmentService.save(res);

					result = new ModelAndView("redirect:/segment/list.do?paradeId=" + segment.getParade().getId());
				} catch (final IllegalArgumentException oops) {
					result = new ModelAndView("misc/403");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(segment, "segment.commit.error");
				}
		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(segment, "segment.commit.error");
		}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Segment segment, final BindingResult binding) {
		ModelAndView result;

		try {
			final Segment res = this.segmentService.reconstruct(segment, binding);
			final int paradeId = segment.getParade().getId();
			this.segmentService.delete(res);

			result = new ModelAndView("redirect:/segment/list.do?paradeId=" + paradeId);
		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(segment, "segment.commit.error");
		}
		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Segment segment) {
		ModelAndView result;

		result = this.createEditModelAndView(segment, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Segment segment, final String messageCode) {
		final ModelAndView result;
		Actor principal;
		boolean isPrincipal = false;

		principal = this.actorService.findByPrincipal();

		final Brotherhood actorBrother = (Brotherhood) principal;

		if (segment.getId() == 0)
			isPrincipal = true;
		else if (this.actorService.checkAuthority(principal, "BROTHERHOOD") && (segment.getParade().getBrotherhood().getId() == principal.getId()))
			isPrincipal = true;

		result = new ModelAndView("segment/edit");
		result.addObject("segment", segment);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("message", messageCode);
		result.addObject("actorBrother", actorBrother);

		return result;

	}
}
