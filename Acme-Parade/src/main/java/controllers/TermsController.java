package controllers;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TermsController extends AbstractController {
	
	@RequestMapping(value="/terms.do",method=RequestMethod.GET)
	public ModelAndView terms(final Locale locale){
		ModelAndView result;
		String language;
		String español;
		String english;
		español = "es";
		english = "en";
		language = locale.getLanguage();
		result=new ModelAndView("terms");
		result.addObject("language", language);
		result.addObject("esp", español);
		result.addObject("english", english);
		
		return result;
		
		
	}
	

}
