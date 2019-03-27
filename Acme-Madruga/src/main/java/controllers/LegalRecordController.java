
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
import services.LegalRecordService;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;

@Controller
@RequestMapping("/legalRecord")
public class LegalRecordController extends AbstractController {

	//Services
	@Autowired
	private LegalRecordService	legalRecordService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private HistoryService		historyService;


	// Constructors

	public LegalRecordController() {
		super();
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int legalRecordId) {
		ModelAndView res;
		Actor principal;
		LegalRecord legalRecord;
		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
			legalRecord = this.legalRecordService.findOne(legalRecordId);

			final Collection<String> laws = this.legalRecordService.getSplitLaws(legalRecord.getLaws());

			res = new ModelAndView("legalRecord/display");
			res.addObject("legalRecord", legalRecord);
			res.addObject("laws", laws);
		} catch (final Throwable opps) {
			res = new ModelAndView("redirect:/welcome/index.do");
		}
		return res;

	}

	//list
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView result = null;
		Brotherhood principal;
		Collection<LegalRecord> records;
		History history;
		Boolean possible;

		history = this.historyService.findOne(historyId);

		try {

			principal = (Brotherhood) this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));

			records = history.getLegalRecords();
			possible = true;

			result = new ModelAndView("legalRecord/list");
			result.addObject("legalRecords", records);
			result.addObject("possible", possible);

		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (final Throwable oopsi) {
			result = new ModelAndView("history/display");
			possible = false;

			result.addObject("possible", possible);
		}

		return result;
	}

	//EDIT 
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int legalRecordId) {
		ModelAndView result;
		LegalRecord legalRecord;
		Brotherhood principal;
		principal = (Brotherhood) this.actorService.findByPrincipal();

		legalRecord = this.legalRecordService.findOne(legalRecordId);
		Assert.notNull(legalRecord);
		Assert.isTrue(principal.getHistory().getLegalRecords().contains(legalRecord), "not.allowed");
		final Collection<String> laws = this.legalRecordService.getSplitLaws(legalRecord.getLaws());
		result = this.createEditModelAndView(legalRecord);
		result.addObject("laws", laws);
		return result;

	}

	//SAVE
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LegalRecord legalRecord, final BindingResult binding) {
		ModelAndView result;
		Integer historyId;
		Brotherhood principal;
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(legalRecord);
			final Collection<String> laws = this.legalRecordService.getSplitLaws(legalRecord.getLaws());
			result.addObject("laws", laws);

		} else
			try {

				principal = (Brotherhood) this.actorService.findByPrincipal();
				historyId = principal.getHistory().getId();
				Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
				this.legalRecordService.save(legalRecord);
				result = new ModelAndView("redirect:/legalRecord/list.do?historyId=" + historyId);

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalRecord, "mr.commit.error");
				final Collection<String> laws = this.legalRecordService.getSplitLaws(legalRecord.getLaws());
				result.addObject("laws", laws);
			}

		return result;
	}
	//CREATE 

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Actor principal;
		LegalRecord legalRecord;

		try {

			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
			legalRecord = this.legalRecordService.create();

			result = this.createEditModelAndView(legalRecord);
		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		}

		return result;
	}

	//delete
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final LegalRecord legalRecord, final BindingResult binding) {
		ModelAndView result;
		Brotherhood principal;
		Integer historyId;
		principal = (Brotherhood) this.actorService.findByPrincipal();

		historyId = principal.getHistory().getId();
		if (binding.hasErrors())
			result = this.createEditModelAndView(legalRecord);
		else
			try {
				this.legalRecordService.delete(legalRecord);
				result = new ModelAndView("redirect:/legalRecord/list.do?historyId=" + historyId);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalRecord, "mr.commit.error");
			}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(legalRecord, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord, final String messageError) {
		ModelAndView result;

		result = new ModelAndView("legalRecord/edit");
		result.addObject("legalRecord", legalRecord);
		result.addObject("messageError", messageError);

		return result;
	}

}
