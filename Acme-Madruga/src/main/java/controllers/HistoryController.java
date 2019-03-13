package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.HistoryService;
import domain.Brotherhood;
import domain.History;

@Controller
@RequestMapping("/history/notAuthenticated")
public class HistoryController extends AbstractController{

	//Services
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private BrotherhoodService brotherhoodService;
	
	
	//Display para no autenticados
	
	@RequestMapping(value="/display", method =  RequestMethod.GET)
	public ModelAndView display(@RequestParam final int brotherhoodId){
		ModelAndView result;
		Brotherhood brotherhood;
		History history;
		
		brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		Assert.notNull(brotherhood);
		
		history = brotherhood.getHistory();
		
		result = new ModelAndView("history/display");
		result.addObject("nonHistory", history);
		
		return result;
	}
}
