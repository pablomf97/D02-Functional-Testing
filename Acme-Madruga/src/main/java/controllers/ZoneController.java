package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Zone;

import services.ActorService;
import services.ZoneService;

@Controller
@RequestMapping("/zone")
public class ZoneController extends AbstractController  {
	
	// Services -----------------------------------------------------------

		@Autowired
		private ZoneService zoneService;
		
		//Display
		
		@RequestMapping(value="/display", method= RequestMethod.GET)
		public ModelAndView display(@RequestParam final int chapterId){
			ModelAndView result;
			Zone zone;
			
			zone = this.zoneService.getZoneByChapter(chapterId);
			
			result = new ModelAndView("zone/display");
			
			result.addObject("zone", zone);
			
			return result;
			
		}
}
