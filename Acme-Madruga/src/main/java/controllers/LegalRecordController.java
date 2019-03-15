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
import domain.LegalRecord;
import domain.PeriodRecord;


import services.ActorService;
import services.HistoryService;
import services.LegalRecordService;

@Controller
@RequestMapping("/legalRecord")
public class LegalRecordController {

	//Services
	@Autowired
	private LegalRecordService legalRecordService;

	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private HistoryService historyService;


	// Constructors

	public LegalRecordController() {
		super();
	}
	
	// Display
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int legalRecordId) {
		ModelAndView res;
		Actor principal;
		LegalRecord legalRecord;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal,
				"BROTHERHOOD"));
		legalRecord=this.legalRecordService.findOne(legalRecordId);

		res = new ModelAndView("legalRecord/display");
		res.addObject("legalRecord", legalRecord);
		return res;

	}
	
		//list
		@RequestMapping(value = "/list", method = RequestMethod.GET)
		public ModelAndView list(@RequestParam int historyId) {
			ModelAndView result = null;
			Brotherhood principal;
			Collection<LegalRecord> records;
			History history;
			Boolean possible;
			
			history=this.historyService.findOne(historyId);
			
			try{
			
			principal = (Brotherhood) this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
			
			records=history.getLegalRecords();
			possible = true;

			result = new ModelAndView("legalRecord/list");
			result.addObject("legalRecords",records);
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
		public ModelAndView edit(@RequestParam final int legalRecordId){
			ModelAndView result;
			LegalRecord legalRecord;

			legalRecord = this.legalRecordService.findOne(legalRecordId);
			Assert.notNull(legalRecord);

			result = this.createEditModelAndView(legalRecord);

			return result;

		}
	
		//SAVE
		@RequestMapping(value="/edit", method = RequestMethod.POST, params= "save")
		public ModelAndView save(@Valid final LegalRecord legalRecord, final BindingResult binding){
			ModelAndView result;

			if(binding.hasErrors())
				result = this.createEditModelAndView(legalRecord);
			else
				try{
					this.legalRecordService.save(legalRecord);
					result = new ModelAndView("redirect:/history/list.do");

				}catch(final Throwable oops){
					result = this.createEditModelAndView(legalRecord, "mr.commit.error");
				}

			return result;
		}
		//CREATE 
		
		@RequestMapping(value="/create", method = RequestMethod.GET)
		public ModelAndView create(){
			ModelAndView result;
			Actor principal;
			LegalRecord legalRecord;

			try{

				principal = this.actorService.findByPrincipal();
				Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
				legalRecord = this.legalRecordService.create();

				result = this.createEditModelAndView(legalRecord);
			}catch(IllegalArgumentException oops){
				result = new ModelAndView("misc/403");
			}

			return result;
		}

	
		//delete
		@RequestMapping(value="/edit", method = RequestMethod.POST, params= "delete")
		public ModelAndView delete(@Valid final LegalRecord legalRecord, final BindingResult binding){
			ModelAndView result;

			if(binding.hasErrors())
				result = this.createEditModelAndView(legalRecord);
			else
				try{
					this.legalRecordService.delete(legalRecord);
					result = new ModelAndView("redirect:/history/list.do");

				}catch(final Throwable oops){
					result = this.createEditModelAndView(legalRecord, "mr.commit.error");
				}

			return result;
		}

		// Ancillary methods
		protected ModelAndView createEditModelAndView(final LegalRecord legalRecord){
			ModelAndView result;

			result = this.createEditModelAndView(legalRecord,null);

			return result;

		}

		protected ModelAndView createEditModelAndView(final LegalRecord legalRecord, final String messageError){
			ModelAndView result;


			result = new ModelAndView("legalRecord/edit");
			result.addObject("legalRecord", legalRecord);
			result.addObject("messageError", messageError);

			return result;
		}

	
	
	
}
