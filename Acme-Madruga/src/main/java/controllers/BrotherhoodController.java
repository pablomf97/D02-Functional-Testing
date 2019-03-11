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
import services.BrotherhoodService;
import services.MemberService;
import services.ZoneService;
import domain.Actor;
import domain.Brotherhood;
import domain.Member;
import domain.Zone;
import forms.BrotherhoodForm;

@Controller
@RequestMapping("/brotherhood")
public class BrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService brotherhoodService;
	@Autowired
	private ActorService actorService;
	@Autowired
	private ZoneService zoneService;
	
	@Autowired
	private MemberService memberService;

	public BrotherhoodController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayGET(@RequestParam final int id) {
		ModelAndView result;
		Brotherhood b;
		try {
			result = new ModelAndView("brotherhood/display");
			b = this.brotherhoodService.findOne(id);
			Assert.isTrue(b.equals(this.actorService.findByPrincipal()));
			final Collection<String> pictures = this.brotherhoodService
					.getSplitPictures(b.getPictures());
			result.addObject("brotherhood", b);
			result.addObject("pictures", pictures);
		} catch (final Throwable opps) {
			// TODO: ver la posibilidada de una pantalla de error
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editGET(@RequestParam final int id) {
		ModelAndView result;
		Brotherhood b;
		BrotherhoodForm bf;
		bf = this.brotherhoodService.createForm();
		final Collection<Zone> zones = this.zoneService.findAll();
		if (id == 0) {
			result = new ModelAndView("brotherhood/edit");
			result.addObject("brotherhoodForm", bf);
		} else
			try {
				result = new ModelAndView("brotherhood/edit");
				b = this.brotherhoodService.findOne(id);
				Assert.isTrue(b.equals(this.actorService.findByPrincipal()));
				bf.setId(b.getId());
				result.addObject("brotherhoodForm", bf);
				result.addObject("brotherhood", b);
				result.addObject("uri", "brotherhood/edit.do");
				final Collection<String> pictures = this.brotherhoodService
						.getSplitPictures(b.getPictures());
				result.addObject("pictures", pictures);
			} catch (final Throwable opps) {
				// TODO: ver la posibilidada de una pantalla de error
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		result.addObject("areas", zones);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editPOST(final BrotherhoodForm brotherhoodForm,
			final BindingResult binding) {
		ModelAndView result;
		String emailError = "";
		String check = "";
		String passW = "";
		String uniqueUsername = "";
		String pictureError = "";
		Collection<String> pictures = new ArrayList<>();
		Brotherhood brotherhood;
		String pictureString = "";
		try {
			brotherhood = this.brotherhoodService.reconstruct(brotherhoodForm,
					binding);
			try {
				pictureString = this.brotherhoodService
						.convetCollectionToString(brotherhoodForm.getPictures());
			} catch (final Throwable opps) {
				pictureError = "brotherhood.pictures.error.url";
			}
			brotherhood.setPictures(pictureString);
			if (brotherhood.getId() == 0) {
				passW = this.actorService.checkPass(
						brotherhoodForm.getPassword(),
						brotherhoodForm.getPassword2());
				uniqueUsername = this.actorService
						.checkUniqueUser(brotherhoodForm.getUsername());
				check = this.actorService.checkLaw(brotherhoodForm
						.getCheckBox());
			}
			if (pictureError.isEmpty())
				pictures = this.brotherhoodService.getSplitPictures(brotherhood
						.getPictures());
			else
				pictures = brotherhoodForm.getPictures();
			final Collection<Zone> zones = this.zoneService.findAll();
			brotherhood.setEmail(brotherhood.getEmail().toLowerCase());
			// emailError = this.actorService.checkEmail(brotherhood.getEmail(),
			// brotherhood.getUserAccount().getAuthorities().iterator()
			//	.next().getAuthority());
			if (binding.hasErrors() || !emailError.isEmpty()
					|| !check.isEmpty() || !passW.isEmpty()
					|| !uniqueUsername.isEmpty() || !pictureError.isEmpty()) {
				System.out.println("llega al binding"
						+ binding.getFieldErrors());
				result = new ModelAndView("brotherhood/edit");
				result.addObject("uri", "brotherhood/edit.do");
				brotherhood.getUserAccount().setPassword("");
				brotherhood.setZone(null);
				result.addObject("brotherhood", brotherhood);
				result.addObject("emailError", emailError);
				result.addObject("checkLaw", check);
				result.addObject("checkPass", passW);
				result.addObject("uniqueUsername", uniqueUsername);
				result.addObject("pictures", pictures);
				result.addObject("areas", zones);
				result.addObject("picturesError", pictureError);
			} else
				try {
					brotherhood.setPhoneNumber(this.actorService
							.checkSetPhoneCC(brotherhood.getPhoneNumber()));
					this.brotherhoodService.save(brotherhood);
					result = new ModelAndView("redirect:/welcome/index.do");
				} catch (final Throwable opps) {
					result = new ModelAndView("brotherhood/edit");
					result.addObject("uri", "brotherhood/edit.do");
					result.addObject("messageCode", "actor.commit.error");
					result.addObject("emailError", emailError);
					brotherhood.getUserAccount().setPassword("");
					brotherhood.setZone(null);
					result.addObject("brotherhood", brotherhood);
					result.addObject("pictures", pictures);
					result.addObject("areas", zones);
				}
		} catch (final Throwable opps) {
			// TODO: pantalla de error
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	/* Listing Brotherhoods */

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Actor principal = null;
		boolean isMember;
		Collection<Brotherhood> brotherhoods;
		Collection<Brotherhood> enrolledBrotherhoods;
		Collection<Brotherhood> moreBrotherhoods;

		try {
			principal = this.actorService.findByPrincipal();
			isMember = this.actorService.checkAuthority(principal, "MEMBER");
			enrolledBrotherhoods = this.brotherhoodService
					.brotherhoodsByMemberId(principal.getId());
			moreBrotherhoods = this.brotherhoodService
					.allBrotherhoodsByMemberId(principal.getId());
			for (Brotherhood b : moreBrotherhoods) {
				enrolledBrotherhoods.add(b);
			}

		} catch (Throwable oops) {
			isMember = false;
			enrolledBrotherhoods = new ArrayList<>();
		}

		brotherhoods = this.brotherhoodService.findAll();

		res = new ModelAndView("brotherhood/list");
		res.addObject("brotherhoods", brotherhoods);
		res.addObject("isMember", isMember);
		res.addObject("enrolledBrotherhoods", enrolledBrotherhoods);

		return res;
	}
	
	@RequestMapping(value="/members/list", method = RequestMethod.GET)
	public ModelAndView listMembers(@RequestParam int brotherhoodId){
		ModelAndView result;
		Collection<Member> members = new ArrayList<Member>();
			
		members.addAll(this.memberService.findAllMembersByBrotherhood(brotherhoodId));
		
		result = new ModelAndView("brotherhood/listMembers");
		result.addObject("members", members);
		
		return result;
			
	}
	
}
