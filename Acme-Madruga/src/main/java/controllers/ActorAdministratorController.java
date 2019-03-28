package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import domain.Actor;

@Controller
@RequestMapping("/actor/administrator")
public class ActorAdministratorController extends AbstractController {

	@Autowired
	private ActorService actorService;

	@RequestMapping(value = "/list-suspicious-actors", method = RequestMethod.GET)
	public ModelAndView listSuspiciousActors() {
		ModelAndView res;
		Collection<Actor> actors;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal,
				"ADMINISTRATOR"));

		actors = this.actorService.findAllExceptPrincipal();

		res = new ModelAndView("actor/listSuspiciousActors");
		res.addObject("requestURI",
				"actor/administrator/list-suspicious-actors.do");
		res.addObject("actors", actors);

		return res;
	}

	// Ban actor
	@RequestMapping(value = "/ban", method = RequestMethod.GET, params = "actorID")
	public ModelAndView banActor(@RequestParam int actorID) {
		ModelAndView res;
		Actor toBan;
		Actor principal;
		Collection<Actor> actors;
		
		try {

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal,
				"ADMINISTRATOR"));

		toBan = this.actorService.findOne(actorID);

		this.actorService.ban(toBan);

		actors = this.actorService.findAllExceptPrincipal();

		res = new ModelAndView("actor/listSuspiciousActors");
		res.addObject("requestURI",
				"actor/administrator/list-suspicious-actors.do");
		res.addObject("actors", actors);
		
		} catch (final IllegalArgumentException oops) {
			res = new ModelAndView("redirect:/welcome/index.do");

		} 

		return res;
	}

	// Unban actor
	@RequestMapping(value = "/unban", method = RequestMethod.GET, params = "actorID")
	public ModelAndView unbanActor(@RequestParam int actorID) {
		ModelAndView res;
		Actor toUnban;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal,
				"ADMINISTRATOR"));
		toUnban = this.actorService.findOne(actorID);

		this.actorService.unban(toUnban);

		Collection<Actor> actors;

		actors = this.actorService.findAllExceptPrincipal();

		res = new ModelAndView("actor/listSuspiciousActors");
		res.addObject("requestURI",
				"actor/administrator/list-suspicious-actors.do");
		res.addObject("actors", actors);

		return res;
	}
}
