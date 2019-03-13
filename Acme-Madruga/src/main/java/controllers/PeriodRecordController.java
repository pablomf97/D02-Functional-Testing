package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import domain.History;
import domain.InceptionRecord;
import domain.PeriodRecord;

import services.ActorService;
import services.PeriodRecordService;

@Controller
@RequestMapping("/periodRecord")
public class PeriodRecordController extends AbstractController{

	//Services
	@Autowired
	private PeriodRecordService periodRecordService;
	
	@Autowired
	private ActorService actorService;
	
//	@Autowired
//	private HistoryService historyService;

	// Constructors
	
	public PeriodRecordController() {
		super();
	}
	
	// Display
	
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int periodRecordId) {
		ModelAndView res;
		Actor principal;
		PeriodRecord periodRecord;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal,
				"BROTHERHOOD"));
		periodRecord=this.periodRecordService.findOne(periodRecordId);

		res=this.createEditModelAndView(periodRecord);
		return res;

	}
	//list
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int historyId) {
		ModelAndView result = null;
		Actor principal;
		Collection<PeriodRecord> records;
		History history;
		
//		history=this.historyService.findone(historyId);
		
//		records=history.getPeriodRecords();
		
		
		
		
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal,
				"BROTHERHOOD"));
		
		
		return result;
	}
	
	
	
	// Ancillary methods
		protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord) {
			ModelAndView result;

			result = this.createEditModelAndView(periodRecord, null);
			
			return result;
		}

		protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord,
				final String messageCode) {
			final ModelAndView result;
			
			result=new ModelAndView("inceptionRecord/edit");
			result.addObject("message",messageCode);

			return result;

		}

}
