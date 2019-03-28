
package controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SystemConfigurationService;
import domain.Actor;
import domain.SystemConfiguration;

@Controller
@RequestMapping("/sysconfig/administrator")
public class SystemConfigurationController extends AbstractController {

	// Services

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private ActorService				actorService;


	// Constructor

	public SystemConfigurationController() {
		super();
	}

	// Actions

	/* Display */

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView res;
		Actor principal;
		SystemConfiguration sysConfig;
		String[] spamWords;
		String[] negativeWords;
		String[] positiveWords;
		String[] messagePriority;
		Map<String, String> welcomeMessage;
		Map<String, String> breachNotification;
		Boolean err;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "ADMINISTRATOR"));

			sysConfig = this.systemConfigurationService.findMySystemConfiguration();

			spamWords = sysConfig.getSpamWords().split(",");
			negativeWords = sysConfig.getNegativeWords().split(",");
			positiveWords = sysConfig.getPossitiveWords().split(",");
			messagePriority = sysConfig.getMessagePriority().split(",");
			welcomeMessage = sysConfig.getWelcomeMessage();
			breachNotification = sysConfig.getBreachNotification();

			res = new ModelAndView("sysConfig/display");
			res.addObject("sysConfig", sysConfig);
			res.addObject("spamWords", spamWords);
			res.addObject("negativeWords", negativeWords);
			res.addObject("positiveWords", positiveWords);
			res.addObject("messagePriority", messagePriority);
			res.addObject("welcomeMessage", welcomeMessage);
			res.addObject("breachNotification", breachNotification);

		} catch (final IllegalArgumentException oops) {
			res = new ModelAndView("misc/403");
		} catch (final Throwable oopsie) {
			res = new ModelAndView("sysConfig/display");
			err = true;

			res.addObject("errMsg", oopsie);
			res.addObject("err", err);
		}
		return res;
	}

	// Editing sysConfig

	@RequestMapping(value = "/edit", method = RequestMethod.GET, params = "systemconfigurationID")
	public ModelAndView edit(@RequestParam final int systemconfigurationID) {
		ModelAndView res;
		SystemConfiguration systemConfiguration;

		systemConfiguration = this.systemConfigurationService.findOne(systemconfigurationID);
		Assert.notNull(systemConfiguration);
		res = this.createEditModelAndView(systemConfiguration);

		return res;
	}

	protected ModelAndView createEditModelAndView(final SystemConfiguration systemConfiguration) {
		ModelAndView res;

		res = this.createEditModelAndView(systemConfiguration, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final SystemConfiguration systemConfiguration, final String messageCode) {
		ModelAndView res;

		res = new ModelAndView("sysConfig/edit");
		res.addObject("sysConfig", systemConfiguration);
		res.addObject("message", messageCode);

		return res;
	}

	// Save

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(SystemConfiguration systemConfiguration, @RequestParam("nameES") final String nameES, @RequestParam("nameEN") final String nameEN, @RequestParam("nEs") final String nEs, @RequestParam("nEn") final String nEn,
		final BindingResult binding) {
		ModelAndView res;
		String message = null;

		if (binding.hasErrors())
			res = this.createEditModelAndView(systemConfiguration, binding.toString());
		else
			try {
				systemConfiguration = this.systemConfigurationService.reconstruct(systemConfiguration, nameES, nameEN, nEs, nEn, binding);

				this.systemConfigurationService.save(systemConfiguration);
				res = new ModelAndView("redirect:display.do");
			} catch (final Throwable oops) {
				if (systemConfiguration.getMaxResults() <= 0 || systemConfiguration.getMaxResults() > 100)
					message = "sysconfig.max.results.err";
				else if (systemConfiguration.getTimeResultsCached() < 0 || systemConfiguration.getTimeResultsCached() >= 24)
					message = "sysconfig.time.err";
				else
					message = "sysconfig.commit.error";
				res = this.createEditModelAndView(systemConfiguration, message);
			}
		return res;
	}

}
