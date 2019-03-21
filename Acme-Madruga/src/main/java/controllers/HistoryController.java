package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BrotherhoodService;
import services.HistoryService;
import domain.Brotherhood;
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Controller
@RequestMapping("/history")
public class HistoryController extends AbstractController {

	// Services

	@Autowired
	private HistoryService historyService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private ActorService actorService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(
			@RequestParam(required = false) final Integer brotherhoodId) {
		ModelAndView result;
		Brotherhood principal;
		History history = null;
		InceptionRecord inceptionRecord;
		Collection<LinkRecord> linkRecords;
		Collection<PeriodRecord> periodRecords;
		Collection<LegalRecord> legalRecords;
		Collection<MiscellaneousRecord> miscellaneousRecords = new ArrayList<MiscellaneousRecord>();
		boolean isInceptionRecord = false;
		boolean possible = false;

		if (brotherhoodId != null) {

			try {
				principal = (Brotherhood) this.actorService.findByPrincipal();

				history = principal.getHistory();

				if (brotherhoodId == principal.getId()) {
					possible = true;
				}
			} catch (Throwable oops) {
				principal = this.brotherhoodService.findOne(brotherhoodId);
				
				history = principal.getHistory();
			}

		} else {
			principal = (Brotherhood) this.actorService.findByPrincipal();

			history = principal.getHistory();

		}

		inceptionRecord = history.getInceptionRecord();
		linkRecords = history.getLinkRecords();
		periodRecords = history.getPeriodRecords();
		legalRecords = history.getLegalRecords();
		miscellaneousRecords = history.getMiscellaneousRecords();

		result = new ModelAndView("history/display");

		if (inceptionRecord != null) {
			isInceptionRecord = true;
		}
		if (!(linkRecords.isEmpty())) {
			result.addObject("linkRecord", linkRecords.iterator().next());
		}

		if (!(periodRecords.isEmpty())) {
			result.addObject("periodRecord", periodRecords.iterator().next());
		}

		if (!(miscellaneousRecords.isEmpty())) {
			result.addObject("miscellaneousRecord", miscellaneousRecords
					.iterator().next());
		}

		if (!(legalRecords.isEmpty())) {
			result.addObject("legalRecord", legalRecords.iterator().next());
		}

		result.addObject("history", history);
		result.addObject("inceptionRecord", inceptionRecord);
		result.addObject("legalRecords", legalRecords);
		result.addObject("periodRecords", periodRecords);
		result.addObject("miscellaneousRecords", miscellaneousRecords);
		result.addObject("linkRecords", linkRecords);
		result.addObject("isInceptionRecord", isInceptionRecord);
		result.addObject("possible", possible);

		return result;
	}

}
