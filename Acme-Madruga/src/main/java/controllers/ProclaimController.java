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
import services.ProclaimService;
import domain.Chapter;
import domain.Proclaim;

@Controller
@RequestMapping(value="/proclaim")
public class ProclaimController extends AbstractController{

	//Services
	
	@Autowired
	private ProclaimService proclaimService;
	
	@Autowired
	private ActorService actorService;
	
	
	//LIST
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public ModelAndView list(@RequestParam final int chapterId){
		ModelAndView result;
		Collection<Proclaim> proclaims;
		
		proclaims = this.proclaimService.getProclaimsByChapter(chapterId);
		
		result = new ModelAndView("proclaim/list");
		
		result.addObject("proclaims", proclaims);
		result.addObject("chapterId", chapterId);
		
		return result;
		
		
	}
	
	//CREATE
	
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		Proclaim proclaim = null;
		
		try{
			proclaim = this.proclaimService.create();
			
			result = this.createEditModelAndView(proclaim);
		}catch(IllegalArgumentException oops){
			result = this.createEditModelAndView(proclaim, "proclaim.commit.error");
		}
		
		return result;
	}
	
	@RequestMapping(value="/create", method = RequestMethod.POST, params="save")
	public ModelAndView save(final Proclaim proclaim, final BindingResult binding){
		ModelAndView result;
		
		if(binding.hasErrors())
			result = this.createEditModelAndView(proclaim);
		else{
			try{
				this.proclaimService.save(proclaim,binding);
				result = new ModelAndView("redirect:/welcome/index.do");
			}catch(final Throwable oops){
				result = this.createEditModelAndView(proclaim, "proclaim.commit.error");
			}
		}
		return result;
	}
	
	//Ancillary methods ---------------------------------------
	
	protected ModelAndView createEditModelAndView(final Proclaim proclaim){
		ModelAndView result;

		result = this.createEditModelAndView(proclaim,null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final Proclaim proclaim, final String messageError){
		ModelAndView result;
		Chapter chapter = (Chapter) this.actorService.findByPrincipal();
		boolean possible = false;
		
		if(chapter.getId() == proclaim.getChapter().getId()){
			possible = true;
		}
		
		result = new ModelAndView("proclaim/edit");
		result.addObject("proclaim", proclaim);
		result.addObject("messageError", messageError);
		result.addObject("possible", possible);

		return result;
	}
	
}
