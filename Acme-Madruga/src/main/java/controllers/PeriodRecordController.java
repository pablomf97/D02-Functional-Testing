package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import domain.Brotherhood;
import domain.History;

import domain.PeriodRecord;

import services.ActorService;
import services.HistoryService;
import services.PeriodRecordService;

@Controller
@RequestMapping("/periodRecord")
public class PeriodRecordController extends AbstractController{

	//Services
	@Autowired
	private PeriodRecordService periodRecordService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private HistoryService historyService;

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

		res = new ModelAndView("miscellaneousRecord/display");
		res.addObject("periodRecord", periodRecord);
		return res;

	}
	//list
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int historyId) {
		ModelAndView result = null;
		Brotherhood principal;
		Collection<PeriodRecord> records;
		History history;
		Boolean possible;
		
		history=this.historyService.findOne(historyId);
		
		try{
		
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
		
		records=history.getPeriodRecords();
		possible = true;

		result = new ModelAndView("periodRecord/list");
		result.addObject("periodRecords",records);
		result.addObject("possible", possible);
		
		}catch(IllegalArgumentException oops){
			result = new ModelAndView("misc/403");
		}catch(Throwable oopsi){
			result = new ModelAndView("history/display");
			possible = false;

			result.addObject("possible", possible);
		}
	
		
			return result;
	}
	//EDIT 
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int periodRecordId){
		ModelAndView result;
		PeriodRecord periodRecord;

		periodRecord = this.periodRecordService.findOne(periodRecordId);
		Assert.notNull(periodRecord);

		result = this.createEditModelAndView(periodRecord);

		return result;

	}
	//SAVE
	@RequestMapping(value="/edit", method = RequestMethod.POST, params= "save")
	public ModelAndView save(@Valid final PeriodRecord periodRecord, final BindingResult binding){
		ModelAndView result;

		if(binding.hasErrors())
			result = this.createEditModelAndView(periodRecord);
		else
			try{
				this.periodRecordService.save(periodRecord);
				result = new ModelAndView("redirect:/history/list.do");

			}catch(final Throwable oops){
				result = this.createEditModelAndView(periodRecord, "mr.commit.error");
			}

		return result;
	}
	//delete
	@RequestMapping(value="/edit", method = RequestMethod.POST, params= "delete")
	public ModelAndView delete(@Valid final PeriodRecord periodRecord, final BindingResult binding){
		ModelAndView result;

		if(binding.hasErrors())
			result = this.createEditModelAndView(periodRecord);
		else
			try{
				this.periodRecordService.delete(periodRecord);
				result = new ModelAndView("redirect:/history/list.do");

			}catch(final Throwable oops){
				result = this.createEditModelAndView(periodRecord, "mr.commit.error");
			}

		return result;
	}

	
	
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord){
		ModelAndView result;

		result = this.createEditModelAndView(periodRecord,null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord, final String messageError){
		ModelAndView result;


		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("periodRecord", periodRecord);
		result.addObject("messageError", messageError);

		return result;
	}

}
