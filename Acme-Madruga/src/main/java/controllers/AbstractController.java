/*
 * AbstractController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import services.SystemConfigurationService;

@Controller
public class AbstractController {

	// Services

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	// Methods

	@ModelAttribute("banner")
	public String getBanner(Model model) {

		String urlBanner = this.systemConfigurationService.findMyBanner();
		return urlBanner;
	}
	@ModelAttribute("breachNotification")
	public Map<String,String> getBreachNotification(Model model) {
		Map<String,String> res=this.systemConfigurationService.findBreachNotification();
		
		return res;
	}
	

	// Panic handler ----------------------------------------------------------

	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result;

		result = new ModelAndView("misc/panic");
		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		result.addObject("exception", oops.getMessage());
		result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));
		
		
		result.addObject("banner",
				this.systemConfigurationService.findMyBanner());

		return result;
	}

}
