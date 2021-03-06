
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
import services.InceptionRecordService;
import domain.Actor;
import domain.Brotherhood;
import domain.InceptionRecord;

@Controller
@RequestMapping("/inceptionRecord")
public class InceptionRecordController extends AbstractController {

	//Services
	@Autowired
	private InceptionRecordService	inceptionRecordService;

	@Autowired
	private ActorService			actorService;


	// Constructors

	public InceptionRecordController() {
		super();
	}

	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int inceptionRecordId) {
		ModelAndView res;
		InceptionRecord inceptionRecord;
		try {
			inceptionRecord = this.inceptionRecordService.findOne(inceptionRecordId);

			final Collection<String> photos = this.inceptionRecordService.getSplitPictures(inceptionRecord.getPhotos());

			res = new ModelAndView("inceptionRecord/display");
			res.addObject("photos", photos);
			res.addObject("inceptionRecord", inceptionRecord);
		} catch (final Throwable opps) {

			res = new ModelAndView("redirect:/welcome/index.do");
		}
		return res;

	}

	//Create 
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Actor principal;
		InceptionRecord inceptionRecord;
		Boolean error;
		try {

			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
			inceptionRecord = this.inceptionRecordService.create();

			result = this.createEditModelAndView(inceptionRecord);
		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (final Throwable oopsie) {

			result = new ModelAndView("history/display");
			error = true;

			result.addObject("oopsie", oopsie);
			result.addObject("error", error);
		}

		return result;
	}

	//EDIT 
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int inceptionRecordId) {
		ModelAndView result = null;
		try {
			InceptionRecord inceptionRecord;
			Brotherhood principal;
			principal = (Brotherhood) this.actorService.findByPrincipal();
			Assert.isTrue(principal.getHistory().getInceptionRecord().getId() == inceptionRecordId, "not.allowed");

			inceptionRecord = this.inceptionRecordService.findOne(inceptionRecordId);
			Assert.notNull(inceptionRecord);
			Assert.isTrue(principal.getHistory().getInceptionRecord().equals(inceptionRecord), "not.allowed");

			final Collection<String> photos = this.inceptionRecordService.getSplitPictures(inceptionRecord.getPhotos());

			result = this.createEditModelAndView(inceptionRecord);
			result.addObject("photos", photos);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:/welcome/index.do");

		}
		return result;

	}

	//save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final InceptionRecord inceptionRecord, final BindingResult binding) {
		ModelAndView result;
		Brotherhood principal;
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(inceptionRecord);
			final Collection<String> photos = this.inceptionRecordService.getSplitPictures(inceptionRecord.getPhotos());
			result.addObject("photos", photos);

		} else
			try {

				principal = (Brotherhood) this.actorService.findByPrincipal();

				Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
				this.inceptionRecordService.save(inceptionRecord);
				result = new ModelAndView("redirect:/history/display.do");
			} catch (final Throwable oops) {
				oops.printStackTrace();
				final Collection<String> photos = this.inceptionRecordService.getSplitPictures(inceptionRecord.getPhotos());
				result = this.createEditModelAndView(inceptionRecord, "mr.commit.error");
				result.addObject("photos", photos);

			}
		return result;

	}

	//delete
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final InceptionRecord inceptionRecord, final BindingResult binding) {
		ModelAndView result;

		try {
			this.inceptionRecordService.delete(inceptionRecord);
			result = new ModelAndView("redirect:/history/display.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(inceptionRecord, "mr.commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(inceptionRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("inceptionRecord/edit");
		result.addObject("message", messageCode);
		result.addObject("inceptionRecord", inceptionRecord);

		return result;

	}

}
