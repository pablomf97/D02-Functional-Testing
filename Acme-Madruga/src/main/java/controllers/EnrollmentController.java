package controllers;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BrotherhoodService;
import services.EnrolmentService;
import services.MessageService;
import services.PositionService;
import domain.Actor;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;

@Controller
@RequestMapping("/enrolment")
public class EnrollmentController extends AbstractController {

	// Services

	@Autowired
	private EnrolmentService enrolmentService;
	
	@Autowired
	private MessageService messageService;
	

	@Autowired
	private ActorService actorService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private PositionService positionService;

	// Constructor

	public EnrollmentController() {
		super();
	}

	// Methods

	/* List of enrollments of a member */
	@RequestMapping(value = "/member/list", method = RequestMethod.GET)
	public ModelAndView listMember() {
		ModelAndView res;
		Actor principal;
		Collection<Enrolment> enrolments;
		Boolean permission;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "MEMBER"));

			enrolments = this.enrolmentService.getEnrollmentsByMember(principal
					.getId());

			permission = true;

			res = new ModelAndView("enrolment/listMember");
			res.addObject("enrolments", enrolments);
			res.addObject("permission", permission);
		} catch (IllegalArgumentException oops) {
			res = new ModelAndView("misc/403");
		} catch (Throwable oopsie) {
			res = new ModelAndView("enrolment/listMember");
			permission = false;

			res.addObject("errMsg", oopsie);
			res.addObject("permission", permission);
		}
		return res;
	}

	/* List of enrollments of a brotherhood */
	@RequestMapping(value = "/brotherhood/list", method = RequestMethod.GET)
	public ModelAndView listBrotherhood() {
		ModelAndView res;
		Actor principal;
		Collection<Enrolment> enrolments;
		Boolean permission;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"BROTHERHOOD"));

			enrolments = this.enrolmentService
					.getEnrollmentsByBrotherhood(principal.getId());

			permission = true;

			res = new ModelAndView("enrolment/listBrotherhood");
			res.addObject("enrolments", enrolments);
			res.addObject("permission", permission);
		} catch (IllegalArgumentException oops) {
			res = new ModelAndView("misc/403");
		} catch (Throwable oopsie) {
			res = new ModelAndView("enrolment/listBrotherhood");
			permission = false;

			res.addObject("errMsg", oopsie);
			res.addObject("permission", permission);
		}
		return res;
	}

	/* Request for an enrollment */
	@RequestMapping(value = "/member/request", method = RequestMethod.GET)
	public ModelAndView request(@RequestParam int brotherhoodID) {
		ModelAndView res;
		Actor principal;
		Enrolment enrolment;
		Brotherhood brotherhood;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "MEMBER"));

			enrolment = this.enrolmentService.create();

			brotherhood = this.brotherhoodService.findOne(brotherhoodID);

			enrolment.setMoment(new Date(System.currentTimeMillis() - 1));
			enrolment.setBrotherhood(brotherhood);
			enrolment.setMember((Member) principal);

			this.enrolmentService.save(enrolment);

			res = new ModelAndView("redirect:/enrolment/member/list.do");

		} catch (IllegalArgumentException oops) {
			res = new ModelAndView("misc/403");
		} catch (Throwable oopsie) {
			// TODO Cambiar cuando tenga lo de chema
			res = new ModelAndView("redirect:/enrolment/member/list.do");
		}
		return res;
	}

	/* Accept or reject an enrollment */
	@RequestMapping(value = "/brotherhood/action", method = RequestMethod.GET)
	public ModelAndView actionsEnrolments(@RequestParam String action,
			@RequestParam int enrolmentID) {
		ModelAndView res;
		Actor principal;
		Enrolment enrolment;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue((this.actorService.checkAuthority(principal,
					"BROTHERHOOD"))
					|| (this.actorService.checkAuthority(principal, "MEMBER")));

			enrolment = this.enrolmentService.findOne(enrolmentID);

			if (principal instanceof Member) {
				Assert.isTrue(principal.getId() == enrolment.getMember()
						.getId());
			} else {
				Assert.isTrue(enrolment.getBrotherhood().getId() == principal
						.getId());
			}

			if (action.equals("accept")) {

				enrolment.setIsOut(false);
				this.enrolmentService.save(enrolment);
				res = this.createEditModelAndView(enrolment);

			} else if (action.equals("reject")) {

				// TODO isout null
				if (!enrolment.getIsOut()) {
					enrolment.getDropOutMoment().add(
							new Date(System.currentTimeMillis() - 1));
				}

				enrolment.setIsOut(true);
				enrolment.setPosition(null);
				this.enrolmentService.save(enrolment);

				
				this.messageService.notificationOutMember(enrolment);
				
				// TODO Cambiar cuando tenga lo de chema
				res = new ModelAndView(
						"redirect:/enrolment/brotherhood/list.do");


				if (principal instanceof Member) {
					res = new ModelAndView("redirect:/enrolment/member/list.do");
				} else {
					res = new ModelAndView(
							"redirect:/enrolment/brotherhood/list.do");
				}


			} else {

				res = new ModelAndView("misc/403");

			}
		} catch (IllegalArgumentException oops) {
			res = new ModelAndView("misc/403");
		} catch (Throwable oopsie) {
			// TODO Cambiar cuando tenga lo de chema
			res = new ModelAndView("redirect:/enrolment/member/list.do");
		}
		return res;
	}

	/* Saving an enrollment */
	@RequestMapping(value = "/brotherhood/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Enrolment enrolment,
			@RequestParam("positionT") String[] position, BindingResult binding) {

		ModelAndView res;
		Actor principal;

		enrolment = this.enrolmentService.reconstruct(enrolment, position,
				binding);

		if (binding.hasErrors()) {
			res = this.createEditModelAndView(enrolment, binding.toString());
		} else {
			try {
				principal = this.actorService.findByPrincipal();
				Assert.isTrue(this.actorService.checkAuthority(principal,
						"BROTHERHOOD"));

				Assert.isTrue(enrolment.getBrotherhood().getId() == principal
						.getId());

				this.enrolmentService.save(enrolment);
				
				this.messageService.notificationMessageEnrolAMember(enrolment);

				res = new ModelAndView("redirect:list.do");
			} catch (IllegalArgumentException oops) {
				res = new ModelAndView("misc/403");
			} catch (Throwable oopsie) {
				res = this
						.createEditModelAndView(enrolment, binding.toString());
			}
		}

		return res;
	}

	// Manage methods
	protected ModelAndView createEditModelAndView(Enrolment enrolment) {
		ModelAndView res;

		res = createEditModelAndView(enrolment, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(Enrolment enrolment,
			String messageCode) {
		ModelAndView res;

		res = new ModelAndView("enrolment/edit");
		res.addObject("enrolment", enrolment);
		res.addObject("message", messageCode);
		res.addObject("positions", this.positionService.findAll());
		return res;
	}
}
