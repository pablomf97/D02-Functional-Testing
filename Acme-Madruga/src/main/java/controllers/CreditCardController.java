package controllers;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import domain.CreditCard;
import domain.Sponsor;
import domain.SystemConfiguration;

import services.ActorService;
import services.CreditCardService;
import services.SystemConfigurationService;
import services.UtilityService;

@Controller
@RequestMapping("/creditCard")
public class CreditCardController extends AbstractController {


	// Services

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private ActorService actorService;
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	@Autowired
	private UtilityService			utilityService;

	// Constructor

	public CreditCardController() {
		super();
	}


	// Create

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		Sponsor principal;
		CreditCard creditCard;
		principal = (Sponsor) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService
				.checkAuthority(principal, "SPONSOR"));

		creditCard = this.creditCardService.create();
		res = this.createEditModelAndView(creditCard);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid CreditCard creditCard, BindingResult binding) {
		ModelAndView result;
		Sponsor principal;

		if (binding.hasErrors()) {
			result = createEditModelAndView(creditCard);
		} else {
			try {
				
				
				principal = (Sponsor) this.actorService.findByPrincipal();
				Assert.isTrue(this.actorService
						.checkAuthority(principal, "SPONSOR"));
				
				this.creditCardService.save(creditCard);
				
				result =new ModelAndView("redirect:/actor/display.do?actorID="
						+ principal.getId());
			}
		catch (Throwable oops) {
			result = createEditModelAndView(creditCard,
					"creditcard.commit.error");
		}
		}
	return result;
}

protected ModelAndView createEditModelAndView(CreditCard creditCard) {
	ModelAndView res;

	res = createEditModelAndView(creditCard, null);

	return res;
}


protected ModelAndView createEditModelAndView(CreditCard creditCard,
		String messageCode) {
	ModelAndView res;
	SystemConfiguration systemConfiguration;
	String[] makes;
	Actor principal;

	systemConfiguration = this.systemConfigurationService
			.findMySystemConfiguration();
	makes = systemConfiguration.getMakers().split(",");

	principal = this.actorService.findByPrincipal();

	res = new ModelAndView("creditCard/edit");
	res.addObject("creditCard", creditCard);
	res.addObject("makes", makes);
	res.addObject("principal", principal);
	res.addObject("message", messageCode);

	return res;
}





}
