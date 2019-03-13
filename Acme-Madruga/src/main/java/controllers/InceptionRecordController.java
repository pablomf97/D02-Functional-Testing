package controllers;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;

import domain.InceptionRecord;


import services.ActorService;
import services.InceptionRecordService;

@Controller
@RequestMapping("/inceptionRecord")
public class InceptionRecordController extends AbstractController {

	
	//Services
	@Autowired
	private InceptionRecordService inceptionRecordService;
	
	@Autowired
	private ActorService actorService;
	
	// Constructors
	
	public InceptionRecordController() {
		super();
	}
	
	// Display
	
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int inceptionRecordId) {
		ModelAndView res;
		InceptionRecord inceptionRecord;
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal,
				"BROTHERHOOD"));
		inceptionRecord=this.inceptionRecordService.findOne(inceptionRecordId);

		res=this.createEditModelAndView(inceptionRecord);
		return res;

	}

	//list
/*	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int historyId) {
		ModelAndView result = null;
		Actor principal;
		Collection<InceptionRecord> records;
		

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal,
				"BROTHERHOOD"));
		


	}
	*/
	//Create 
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		Actor principal;
		InceptionRecord inceptionRecord;
		
		try{
			
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"BROTHERHOOD"));
			inceptionRecord=this.inceptionRecordService.create();
			
			result = this.createEditModelAndView(inceptionRecord);
		}catch(IllegalArgumentException oops) {
			result = new ModelAndView("misc/403");
		}
		return result;
	}
	//save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid InceptionRecord inceptionRecord, final BindingResult binding) {
		ModelAndView result;
		Actor principal;
		
		if(binding.hasErrors()){
			result=this.createEditModelAndView(inceptionRecord);
		}
		else{
			try{
				principal = this.actorService.findByPrincipal();
				Assert.isTrue(this.actorService.checkAuthority(principal,
						"BROTHERHOOD"));
				this.inceptionRecordService.save(inceptionRecord);
				result=new ModelAndView("redirect:/inceptionRecord/brotherhood/display.do");
			
			}catch(final Throwable oops){
				result=this.createEditModelAndView(inceptionRecord,"commit.error");
			}
		}
		return result;
		

	}
	
	

	
	// Ancillary methods
		protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord) {
			ModelAndView result;

			result = this.createEditModelAndView(inceptionRecord, null);
			
			return result;
		}

		protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord,
				final String messageCode) {
			final ModelAndView result;
			
			result=new ModelAndView("inceptionRecord/edit");
			result.addObject("message",messageCode);

			return result;

		}
	
}
