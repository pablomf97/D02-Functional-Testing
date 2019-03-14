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
import services.LinkRecordService;
import domain.Actor;
import domain.History;
import domain.LinkRecord;


@Controller
@RequestMapping("/linkRecord")
public class LinkRecordController extends AbstractController{

	//Services

	@Autowired
	private HistoryService historyService;

	@Autowired
	private LinkRecordService linkRecordService;

	@Autowired
	private ActorService actorService;

	//Create
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		Actor principal;
		LinkRecord linkRecord;

		try{
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
			linkRecord = this.linkRecordService.create();

			result = this.createEditModelAndView(linkRecord);
			
		}catch(IllegalArgumentException oops){
			result = new ModelAndView("misc/403");
		}
		return result;
	}


	//Display

	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int linkRecordId){
		ModelAndView result;
		LinkRecord linkRecord;

		linkRecord = this.linkRecordService.findOne(linkRecordId);
		Assert.notNull(linkRecord);

		result = new ModelAndView("linkedRecord/display");
		result.addObject("linkRecord", linkRecord);

		return result;


	}

	//List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId){
		ModelAndView result;
		Actor principal;
		History history;
		Collection<LinkRecord> linkRecords;
		Boolean possible;

		history = this.historyService.findOne(historyId);
		Assert.notNull(history);

		try{
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));

			linkRecords = history.getLinkRecords();

			possible = true;

			result = new ModelAndView("linkRecord/list");
			result.addObject("linkRecords", linkRecords);
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

	//Edition


	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int linkRecordId){
		ModelAndView result;
		LinkRecord linkRecord;

		linkRecord = this.linkRecordService.findOne(linkRecordId);
		Assert.notNull(linkRecord);

		result = this.createEditModelAndView(linkRecord);

		return result;

	}
	@RequestMapping(value="/edit", method = RequestMethod.POST, params= "save")
	public ModelAndView save(@Valid final LinkRecord record, final BindingResult binding){
		ModelAndView result;

		if(binding.hasErrors())
			result = this.createEditModelAndView(record);
		else
			try{
				this.linkRecordService.save(record);
				result = new ModelAndView("redirect:/history/list.do");

			}catch(final Throwable oops){
				result = this.createEditModelAndView(record, "lr.commit.error");
			}

		return result;
	}

	@RequestMapping(value="/edit", method = RequestMethod.POST, params= "delete")
	public ModelAndView delete(@Valid final LinkRecord record, final BindingResult binding){
		ModelAndView result;

		if(binding.hasErrors())
			result = this.createEditModelAndView(record);
		else
			try{
				this.linkRecordService.delete(record);
				result = new ModelAndView("redirect:/history/list.do");

			}catch(final Throwable oops){
				result = this.createEditModelAndView(record, "lr.commit.error");
			}

		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final LinkRecord record){
		ModelAndView result;

		result = this.createEditModelAndView(record,null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final LinkRecord record, final String messageError){
		ModelAndView result;


		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("linkRecord", record);
		result.addObject("messageError", messageError);

		return result;
	}


}
