
package controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MarchService;
import services.MessageService;
import services.ProcessionService;
import domain.Actor;
import domain.March;
import domain.Procession;

@Controller
@RequestMapping("/march")
public class MarchController extends AbstractController {

	// Services

	@Autowired
	private MarchService	marchService;

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ProcessionService processionService;
	
	@Autowired
	private MessageService messageService;

	// Display
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int marchId) {

		ModelAndView result;
		March march;
		boolean isPrincipal = false;
		Actor principal;
		
		principal = this.actorService.findByPrincipal();
		march = this.marchService.findOne(marchId);
		
		if(march.getMember().getId() == principal.getId())
			isPrincipal = true;
		
		result = new ModelAndView("march/display");
		result.addObject("march", march);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("requestURI", "march/display.do?marchId=" + marchId);

		return result;
	}
	
	//List

	@RequestMapping(value = "/member,brotherhood/list")
	public ModelAndView list(@RequestParam(required = false) Integer memberId, @RequestParam(required = false) Integer brotherhoodId) {
		ModelAndView result;
		Collection<March> marchs;
		Actor principal;
		Boolean permission;
		
		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(!this.actorService.checkAuthority(principal, "ADMINISTRATOR"));
			
			permission = true;
			
			principal = this.actorService.findByPrincipal();

			if (this.actorService.checkAuthority(principal, "BROTHERHOOD")) {

				marchs = this.marchService.findMarchsByBrotherhoodId(principal.getId());
				
				String requestURI = "march/member,brotherhood/list.do?brotherhoodId=" + principal.getId();
				result = new ModelAndView("march/list");
				result.addObject("requestURI", requestURI);
				result.addObject("marchs", marchs);

			} else {

				marchs = this.marchService.findMarchsByMemberId(principal.getId());
				
				String requestURI = "march/member,brotherhood/list.do?memberId=" + principal.getId();
				result = new ModelAndView("march/list");
				result.addObject("requestURI", requestURI);
				result.addObject("marchs", marchs);

			}
		} catch (IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (Throwable oopsie) {
			
			result = new ModelAndView("march/member,brotherhood/list");
			permission = false;
			
			result.addObject("oopsie", oopsie);
			result.addObject("permission", permission);
		}	
		
		return result;
	}
		
	// Creation 
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		March march;
		
		Actor principal;
		Boolean error;
		
		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "MEMBER"));
						
			march= this.marchService.create();

			result = this.createEditModelAndView(march);
		} catch (IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		} catch (Throwable oopsie) {
			
			result = new ModelAndView("march/member,brotherhood/list");
			error = true;
			
			result.addObject("oopsie", oopsie);
			result.addObject("error", error);
		}
		return result;	

	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int marchId) {
		ModelAndView result;
		March march;

		march = this.marchService.findOne(marchId);
		Assert.notNull(march);
		result = this.createEditModelAndView(march);

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save (March march, final BindingResult binding) {
		ModelAndView result;

		march = this.marchService.reconstruct(march, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(march);
		else
			try {
				this.marchService.save(march);
				result = new ModelAndView("redirect:member,brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(march,
						"march.commit.error");
			}
		return result;
	}
	
	//Accept
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView acceptView(@RequestParam final int marchId) {
		ModelAndView result;
		March march;
		boolean isPrincipal = false;
		Actor principal;
		List<Integer> recomendedPos;
	
		
		principal = this.actorService.findByPrincipal();
		march = this.marchService.findOne(marchId);
		Assert.notNull(march);
		
		
		recomendedPos = this.processionService.recommendedPos(march.getProcession());
		
		if(march.getProcession().getBrotherhood().getId() == principal.getId())
			isPrincipal = true;
		
		result = new ModelAndView("march/accept");
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("recomRow", recomendedPos.get(0));
		result.addObject("recomCol", recomendedPos.get(1));
		result.addObject("march", march);

		return result;
	}
	
	@RequestMapping(value = "/accept", method = RequestMethod.POST, params = "accept")
	public ModelAndView accept(March march, final BindingResult binding) {
		
		
		
		ModelAndView result;

		march.setStatus("APPROVED");

		march = this.marchService.reconstruct(march, binding);
		
		if (binding.hasErrors())
			result = this.createEditModelAndView(march);
		else
			try {
				this.marchService.save(march);
				
				this.messageService.changeStatusNotfication(march,march.getMember(), new Date(System.currentTimeMillis()-1));
				
				result = new ModelAndView("redirect:member,brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(march,
						"march.commit.error");
			}
		return result;
	}

	//Reject
	@RequestMapping(value = "/rejectv", method = RequestMethod.GET)
	public ModelAndView rejectView(@RequestParam final int marchId) {
		ModelAndView result;
		March march;
		boolean isPrincipal = false;
		Actor principal;
		
		principal = this.actorService.findByPrincipal();
		march = this.marchService.findOne(marchId);
		Assert.notNull(march);
		
		if(march.getProcession().getBrotherhood().getId() == principal.getId())
			isPrincipal = true;
		
		result = new ModelAndView("march/reject");
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("march", march);
		
		return result;
	}
	
	@RequestMapping(value = "/rejectb", method = RequestMethod.POST, params = "reject")
	public ModelAndView reject(March march, final BindingResult binding) {
		ModelAndView result;

		march.setStatus("REJECTED");
		march = this.marchService.reconstruct(march, binding);
		
		
		
		if (binding.hasErrors())
			result = this.createEditModelAndView(march);
		else
			try {
				this.marchService.save(march);
				this.messageService.changeStatusNotfication(march,march.getMember(),new Date(System.currentTimeMillis()-1));
				
				result = new ModelAndView("redirect:member,brotherhood/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(march,
						"march.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete(@RequestParam int marchId) {
		ModelAndView result;
		March toDelete;
		Collection<March> marchs;
		Actor principal;
		
		principal = this.actorService.findByPrincipal();
		
		toDelete = this.marchService.findOne(marchId);
		this.marchService.delete(toDelete);
		
		marchs = this.marchService.findMarchsByMemberId(principal.getId());
		
		String requestURI = "march/member,brotherhood/list.do";
		result = new ModelAndView("march/list");
		result.addObject("requestURI", requestURI);
		result.addObject("marchs", marchs);		

		return result;
	}

	//Ancillary methods
	protected ModelAndView createEditModelAndView(final March march) {
		ModelAndView result;

		result = this.createEditModelAndView(march, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final March march, final String messageCode) {
		final ModelAndView result;
		Actor principal;
		boolean isPrincipal = false;
		Collection<Procession> toApply = new ArrayList<>();
		
		principal = this.actorService.findByPrincipal();
		
		
		
		if(this.actorService.checkAuthority(principal, "MEMBER")){
			toApply = this.processionService.findPossibleProcessionsToMarchByMember(principal.getId());
			if(principal.getId() == march.getId()){
				isPrincipal = true;
			}
		} else if(this.actorService.checkAuthority(principal, "BROTHERHOOD") && (principal.getId() == march.getProcession().getBrotherhood().getId())){
			isPrincipal = true;
		}
		
		result = new ModelAndView("march/edit");
		result.addObject("march", march);
		result.addObject("message", messageCode);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("toApply", toApply);

		return result;
	}

}
