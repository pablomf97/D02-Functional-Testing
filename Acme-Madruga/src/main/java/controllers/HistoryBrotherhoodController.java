package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BrotherhoodService;
import services.HistoryService;
import domain.Brotherhood;
import domain.History;

@Controller
@RequestMapping("/history/brotherhood")
public class HistoryBrotherhoodController {

	//Services
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private BrotherhoodService brotherhoodService;
	
	@Autowired
	private ActorService actorService;
	
	//Display
	
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(){
		ModelAndView result;
		Brotherhood principal;
		History history;
		
		principal = (Brotherhood) this.actorService.findByPrincipal();
		
		history = principal.getHistory();
		
		result = new ModelAndView("history/display");
		result.addObject("history", history);
		
		return result;
	}
	
	@RequestMapping(value="/")
	public ModelAndView save(final History history, final BindingResult binding){
		ModelAndView result;
		
		return null;
		
	}
	
}
