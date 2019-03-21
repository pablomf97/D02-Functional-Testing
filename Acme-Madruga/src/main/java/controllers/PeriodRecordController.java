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

import services.ActorService;
import services.HistoryService;
import services.PeriodRecordService;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;

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

		try{
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"BROTHERHOOD"));
			periodRecord=this.periodRecordService.findOne(periodRecordId);

			final Collection<String> photos = this.periodRecordService
					.getSplitPictures(periodRecord.getPhotos());

			res=new ModelAndView("periodRecord/display");
			res.addObject("photos",photos);
			res.addObject("periodRecord",periodRecord);
		} catch (final Throwable opps) {

			res = new ModelAndView("redirect:/welcome/index.do");
		}
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
			result.addObject("historyId", history.getId());

		}catch(IllegalArgumentException oops){
			result = new ModelAndView("misc/403");
		}catch(Throwable oopsi){
			result = new ModelAndView("history/display");
			possible = false;

			result.addObject("possible", possible);
		}


		return result;
	}
	//CREATE 

	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		Actor principal;
		PeriodRecord periodRecord;

		try{

			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
			periodRecord = this.periodRecordService.create();

			result = this.createEditModelAndView(periodRecord);
		}catch(IllegalArgumentException oops){
			result = new ModelAndView("misc/403");
		}

		return result;
	}

	//EDIT 
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int periodRecordId){
		ModelAndView result = null;
		PeriodRecord periodRecord;
		Brotherhood principal;
		principal = (Brotherhood) this.actorService.findByPrincipal();
		


		periodRecord = this.periodRecordService.findOne(periodRecordId);
		Assert.notNull(periodRecord);
		Assert.isTrue(principal.getHistory().getPeriodRecords().contains(periodRecord),"not.allowed");
			final Collection<String> photos = this.periodRecordService.getSplitPictures(periodRecord.getPhotos());


		result = this.createEditModelAndView(periodRecord);
		result.addObject("photos", photos);
		return result;


	}
	//SAVE
	@RequestMapping(value="/edit", method = RequestMethod.POST, params= "save")
	public ModelAndView save(@Valid final PeriodRecord periodRecord, final BindingResult binding){
		ModelAndView result;

		Brotherhood principal;
		Integer historyId;
		
		if(binding.hasErrors()){
			result=this.createEditModelAndView(periodRecord);
		}
		else{
			try{
			
				principal = (Brotherhood) this.actorService.findByPrincipal();
				historyId = principal.getHistory().getId();
				Assert.isTrue(this.actorService.checkAuthority(principal,
						"BROTHERHOOD"));
				this.periodRecordService.save(periodRecord);
				result = new ModelAndView("redirect:/periodRecord/list.do?historyId="+historyId);
				
			}catch(final Throwable oops){
				result=this.createEditModelAndView(periodRecord,"mr.commit.error");

			}
		}
		return result;
	}
	//delete
	@RequestMapping(value="/edit", method = RequestMethod.POST, params= "delete")
	public ModelAndView delete(@Valid final PeriodRecord periodRecord, final BindingResult binding){
		ModelAndView result;
		Brotherhood principal;
		Integer historyId;
		principal = (Brotherhood) this.actorService.findByPrincipal();

		historyId = principal.getHistory().getId();
		if(binding.hasErrors())
			result = this.createEditModelAndView(periodRecord);
		else
			try{
				this.periodRecordService.delete(periodRecord);
				result = new ModelAndView("redirect:/periodRecord/list.do?historyId="+historyId);

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


		result = new ModelAndView("periodRecord/edit");
		result.addObject("periodRecord", periodRecord);
		result.addObject("messageError", messageError);

		return result;
	}

}
