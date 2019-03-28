package controllers;

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
import services.ChapterService;
import services.ZoneService;
import domain.Chapter;
import domain.Zone;
import forms.ChapterForm;

@Controller
@RequestMapping("/chapter")
public class ChapterController extends AbstractController {

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private ZoneService zoneService;

	public ChapterController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayGET(@RequestParam final int id) {
		ModelAndView res;
		Chapter chapter;

		try {
			res = new ModelAndView("chapter/display");
			chapter = this.chapterService.findOne(id);

			res.addObject("chapter", chapter);
		} catch (final Throwable opps) {
			res = new ModelAndView("redirect:/welcome/index.do");
		}
		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editGET(@RequestParam final int id) {
		ModelAndView result;
		Chapter chapter;
		ChapterForm chapterForm;
		chapterForm = this.chapterService.createForm();
		final Collection<Zone> zones = this.zoneService.findAll();
		if (id == 0) {
			result = new ModelAndView("chapter/edit");
			result.addObject("chapterForm", chapterForm);
		} else
			try {
				result = new ModelAndView("chapter/edit");
				chapter = this.chapterService.findOne(id);
				Assert.isTrue(chapter.equals(this.actorService
						.findByPrincipal()));
				chapterForm.setId(chapter.getId());
				result.addObject("chapterForm", chapterForm);
				result.addObject("chapter", chapter);
				result.addObject("uri", "chapter/edit.do");
			} catch (final Throwable opps) {
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		result.addObject("areas", zones);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editPOST(final ChapterForm chapterForm,
			final BindingResult binding) {
		ModelAndView result;
		String emailError = "";
		String check = "";
		String passW = "";
		String uniqueUsername = "";
		Chapter chapter;

		try {
			chapter = this.chapterService.reconstruct(chapterForm, binding);

			if (chapter.getId() == 0) {

				passW = this.actorService.checkPass(chapterForm.getPassword(),
						chapterForm.getPassword2());
				uniqueUsername = this.actorService.checkUniqueUser(chapterForm
						.getUsername());
				check = this.actorService.checkLaw(chapterForm.getCheckBox());

			}

			final Collection<Zone> zones = this.zoneService.findAll();
			chapter.setEmail(chapter.getEmail().toLowerCase());
			emailError = this.actorService.checkEmail(chapter.getEmail(),
					chapter.getUserAccount().getAuthorities().iterator().next()
							.getAuthority());
			if (binding.hasErrors() || !emailError.isEmpty()
					|| !check.isEmpty() || !passW.isEmpty()
					|| !uniqueUsername.isEmpty()) {

				System.out.println("llega al binding"
						+ binding.getFieldErrors());
				result = new ModelAndView("chapter/edit");
				result.addObject("uri", "chapter/edit.do");
				chapter.getUserAccount().setPassword("");
				result.addObject("chapter", chapter);
				result.addObject("emailError", emailError);
				result.addObject("checkLaw", check);
				result.addObject("checkPass", passW);
				result.addObject("uniqueUsername", uniqueUsername);
				result.addObject("areas", zones);

			} else
				try {
					chapter.setPhoneNumber(this.actorService
							.checkSetPhoneCC(chapter.getPhoneNumber()));
					this.chapterService.save(chapter);
					result = new ModelAndView("redirect:/welcome/index.do");
				} catch (final Throwable opps) {
					result = new ModelAndView("chapter/edit");
					result.addObject("uri", "chapter/edit.do");
					result.addObject("messageCode", "actor.commit.error");
					result.addObject("emailError", emailError);
					chapter.getUserAccount().setPassword("");
					chapter.setZone(null);
					result.addObject("chapter", chapter);
					result.addObject("areas", zones);
				}
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

}
