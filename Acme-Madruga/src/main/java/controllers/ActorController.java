
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Member;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	@Autowired
	private ActorService	actorService;


	public ActorController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayGET() {
		ModelAndView result;
		final Actor actor = this.actorService.findByPrincipal();
		final String role = actor.getUserAccount().getAuthorities().iterator().next().toString();
		switch (role) {
		case "ADMINISTRATOR":
			final Administrator a = (Administrator) actor;
			result = new ModelAndView("redirect:/administrator/display.do?id=" + a.getId());
			break;
		case "BROTHERHOOD":
			final Brotherhood b = (Brotherhood) actor;
			result = new ModelAndView("redirect:/brotherhood/display.do?id=" + b.getId());
			break;
		case "MEMBER":
			final Member m = (Member) actor;
			result = new ModelAndView("redirect:/member/display.do?id=" + m.getId());
			break;
		default:
			//TODO: ver la posibilidad de pagina de error
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editGET() {
		ModelAndView result;
		final Actor actor = this.actorService.findByPrincipal();
		final String role = actor.getUserAccount().getAuthorities().iterator().next().toString();
		switch (role) {
		case "ADMINISTRATOR":
			final Administrator a = (Administrator) actor;
			result = new ModelAndView("redirect:/administrator/edit.do?id=" + a.getId());
			break;
		case "BROTHERHOOD":
			final Brotherhood b = (Brotherhood) actor;
			result = new ModelAndView("redirect:/brotherhood/edit.do?id=" + b.getId());
			break;
		case "MEMBER":
			final Member m = (Member) actor;
			result = new ModelAndView("redirect:/member/edit.do?id=" + m.getId());
			break;
		default:
			//TODO: ver la posibilidad de pagina de error
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

}