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
import services.MiscellaneousRecordService;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.MiscellaneousRecord;

@Controller
@RequestMapping("/miscellaneousRecord")
public class MiscellaneousRecordController extends AbstractController{

	//Services

	@Autowired
	private MiscellaneousRecordService miscellaneousRecordService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private HistoryService historyService;

	//List

	@RequestMapping(value="/list" , method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId){
		ModelAndView result;
		Brotherhood principal;
		History history;
		Collection<MiscellaneousRecord> records;
		Boolean possible;

		history = this.historyService.findOne(historyId);
		Assert.notNull(history);

		try{
			principal = (Brotherhood) this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));

			records = history.getMiscellaneousRecords();

			possible = true;

			result = new ModelAndView("miscellaneousRecord/list");
			result.addObject("miscellaneousRecords",records);
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


	//Display

	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int miscellaneousRecordId){
		ModelAndView result;
		MiscellaneousRecord record;

		record = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
		Assert.notNull(record);

		result = new ModelAndView("miscellaneousRecord/display");
		result.addObject("record", record);

		return result;

	}

	//Edition


	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousRecordId){
		ModelAndView result;
		MiscellaneousRecord record;

		record = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
		Assert.notNull(record);

		result = this.createEditModelAndView(record);

		return result;


	}


	@RequestMapping(value="/edit", method = RequestMethod.POST, params= "save")
	public ModelAndView save(@Valid final MiscellaneousRecord record, final BindingResult binding){
		ModelAndView result;
		Brotherhood principal;
		Integer historyId;
		
		principal = (Brotherhood) this.actorService.findByPrincipal();
		
		historyId = principal.getHistory().getId();
		
		if(binding.hasErrors())
			result = this.createEditModelAndView(record);
		else
			try{
				this.miscellaneousRecordService.save(record);
				result = new ModelAndView("redirect:/miscellaneousRecord/list.do?historyId="+historyId);

			}catch(final Throwable oops){
				result = this.createEditModelAndView(record, "mr.commit.error");
			}

		return result;
	}

	@RequestMapping(value="/edit", method = RequestMethod.POST, params= "delete")
	public ModelAndView delete(@Valid final MiscellaneousRecord record, final BindingResult binding){
		ModelAndView result;
		Brotherhood principal;
		Integer historyId;
		
		principal = (Brotherhood) this.actorService.findByPrincipal();
		
		historyId = principal.getHistory().getId();
		
		if(binding.hasErrors())
			result = this.createEditModelAndView(record);
		else
			try{
				this.miscellaneousRecordService.delete(record);
				result = new ModelAndView("redirect:/miscellaneousRecord/list.do?historyId="+historyId);

			}catch(final Throwable oops){
				result = this.createEditModelAndView(record, "mr.commit.error");
			}

		return result;
	}

	//Create

	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		Actor principal;
		MiscellaneousRecord miscellaneousRecord;

		try{

			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
			miscellaneousRecord = this.miscellaneousRecordService.create();

			result = this.createEditModelAndView(miscellaneousRecord);
		}catch(IllegalArgumentException oops){
			result = new ModelAndView("misc/403");
		}

		return result;
	}



	//Ancillary methods

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord record){
		ModelAndView result;

		result = this.createEditModelAndView(record,null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord record, final String messageError){
		ModelAndView result;
		Brotherhood principal;
		int historyId;
		
		principal = (Brotherhood) this.actorService.findByPrincipal();
		
		historyId = principal.getHistory().getId();

		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("miscellaneousRecord", record);
		result.addObject("messageError", messageError);
		result.addObject("historyId", historyId);
		
		return result;
	}

}