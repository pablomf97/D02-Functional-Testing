
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MemberService;
import domain.Member;
import forms.MemberForm;

@Controller
@RequestMapping("/member")
public class MemberController extends AbstractController {

	@Autowired
	private MemberService	memberService;
	@Autowired
	private ActorService	actorService;


	public MemberController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayGET(@RequestParam final int id) {
		ModelAndView result;
		Member m;
		try {
			result = new ModelAndView("member/display");
			m = this.memberService.findOne(id);
			result.addObject("member", m);
		} catch (final Throwable opps) {
			opps.printStackTrace();
			//TODO: ver la posibilidada de una pantalla de error
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editGET(@RequestParam final int id) {
		ModelAndView result;
		Member m;
		MemberForm mf;
		mf = this.memberService.createForm();
		if (id == 0) {
			result = new ModelAndView("member/edit");
			result.addObject("memberForm", mf);
		} else
			try {
				result = new ModelAndView("member/edit");
				m = this.memberService.findOne(id);
				Assert.isTrue(m.equals(this.actorService.findByPrincipal()));
				mf.setId(m.getId());
				result.addObject("memberForm", mf);
				result.addObject("member", m);
				result.addObject("uri", "member/edit.do");
			} catch (final Throwable opps) {
				//TODO: ver la posibilidada de una pantalla de error
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editPOST(final MemberForm memberForm, final BindingResult binding) {
		ModelAndView result;
		String emailError = "";
		String check = "";
		String passW = "";
		String uniqueUsername = "";
		Member member;
		try {
			member = this.memberService.reconstruct(memberForm, binding);
			if (member.getId() == 0) {
				passW = this.actorService.checkPass(memberForm.getPassword(), memberForm.getPassword2());
				uniqueUsername = this.actorService.checkUniqueUser(memberForm.getUsername());
				check = this.actorService.checkLaw(memberForm.getCheckBox());
			}

			member.setEmail(member.getEmail().toLowerCase());
			emailError = this.actorService.checkEmail(member.getEmail(), member.getUserAccount().getAuthorities().iterator().next().getAuthority());
			if (binding.hasErrors() || !emailError.isEmpty() || !check.isEmpty() || !passW.isEmpty() || !uniqueUsername.isEmpty()) {
				result = new ModelAndView("member/edit");
				result.addObject("uri", "member/edit.do");
				member.getUserAccount().setPassword("");
				result.addObject("member", member);
				result.addObject("emailError", emailError);
				result.addObject("checkLaw", check);
				result.addObject("checkPass", passW);
				result.addObject("uniqueUsername", uniqueUsername);
			} else
				try {
					member.setPhoneNumber(this.actorService.checkSetPhoneCC(member.getPhoneNumber()));
					this.memberService.save(member);
					result = new ModelAndView("redirect:/welcome/index.do");
				} catch (final Throwable opps) {
					result = new ModelAndView("member/edit");
					result.addObject("uri", "member/edit.do");
					result.addObject("messageCode", "actor.commit.error");
					result.addObject("emailError", emailError);
					member.getUserAccount().setPassword("");
					result.addObject("member", member);
				}
		} catch (final Throwable opps) {
			//TODO: pantalla de error
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;

	}
}
