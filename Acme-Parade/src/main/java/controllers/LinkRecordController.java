
package controllers;

import java.util.ArrayList;
import java.util.Collection;

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
import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;

@Controller
@RequestMapping("/linkRecord")
public class LinkRecordController extends AbstractController {

	//Services

	@Autowired
	private HistoryService		historyService;

	@Autowired
	private LinkRecordService	linkRecordService;

	@Autowired
	private ActorService		actorService;


	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Actor principal;
		LinkRecord linkRecord;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
			linkRecord = this.linkRecordService.create();

			result = this.createEditModelAndView(linkRecord);

		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		}
		return result;
	}

	//Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int linkRecordId) {
		ModelAndView result;
		LinkRecord record;
		try {
			record = this.linkRecordService.findOne(linkRecordId);
			result = new ModelAndView("linkRecord/display");
			result.addObject("linkRecord", record);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:/welcome/index.do");

		}
		return result;
	}

	//List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView result;
		Actor principal;
		History history;
		Collection<LinkRecord> linkRecords;
		Boolean possible;

		try {
			history = this.historyService.findOne(historyId);

			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));

			linkRecords = history.getLinkRecords();

			possible = true;

			result = new ModelAndView("linkRecord/list");
			result.addObject("linkRecords", linkRecords);
			result.addObject("possible", possible);
			result.addObject("historyId", historyId);
		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");

		} catch (final Throwable oopsi) {
			result = new ModelAndView("history/display");
			possible = false;

			result.addObject("possible", possible);
		}

		return result;
	}

	//Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int linkRecordId) {
		ModelAndView result;
		LinkRecord linkRecord;
		try {
			linkRecord = this.linkRecordService.findOne(linkRecordId);
			result = this.createEditModelAndView(linkRecord);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final LinkRecord record, final BindingResult binding) {
		ModelAndView result;
		final LinkRecord linkRecord;
		try {
			linkRecord = this.linkRecordService.reconstruct(record, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(linkRecord);
			else {
				this.linkRecordService.save(linkRecord);
				result = new ModelAndView("redirect:/history/display.do");
			}

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(record, "lr.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LinkRecord record, final BindingResult binding) {
		ModelAndView result;
		try {
			this.linkRecordService.delete(record);
			result = new ModelAndView("redirect:/history/display.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final LinkRecord record) {
		ModelAndView result;

		result = this.createEditModelAndView(record, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final LinkRecord record, final String messageError) {
		ModelAndView result;
		Brotherhood principal;
		int historyId;
		Collection<Brotherhood> brotherhoods = new ArrayList<Brotherhood>();

		brotherhoods = this.linkRecordService.getFreeBrotherhoods();

		principal = (Brotherhood) this.actorService.findByPrincipal();

		brotherhoods.remove(principal);

		historyId = principal.getHistory().getId();

		result = new ModelAndView("linkRecord/edit");
		result.addObject("linkRecord", record);
		result.addObject("messageError", messageError);
		result.addObject("historyId", historyId);
		result.addObject("brotherhoods", brotherhoods);

		return result;
	}

}
