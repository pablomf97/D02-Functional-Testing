package controllers;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageService;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Controller
@RequestMapping("/message/administrator")
public class MessageAdministratorController extends AbstractController{

	//Services -----------------------------------------------------------

	@Autowired
	private MessageService messageService;

	@Autowired
	private ActorService actorService;

	//Creation --------------------------------------------------------

	@RequestMapping(value="/broadcast", method=RequestMethod.GET)
	public ModelAndView broadcast(){
		final ModelAndView result;
		Message mensaje;

		mensaje = this.messageService.create();

		mensaje.setRecipient(this.actorService.findByPrincipal());

		result = this.createEditModelAndView(mensaje);

		return result;
	}

	//Broadcast ---------------------------------------------------------------

	@RequestMapping(value="/broadcast", method = RequestMethod.POST, params="save")
	public ModelAndView broadcast(final Message mensaje, final BindingResult binding){
		ModelAndView result;
		Message message;

		message = this.messageService.reconstructBroadcast(mensaje, binding);

		if(binding.hasErrors()){
			result = this.createEditModelAndView(message);
		}else{
			try{
				this.messageService.broadcast(message);
				result = new ModelAndView("redirect:/messagebox/list.do");

			}catch(final Throwable oops){
				result = this.createEditModelAndView(message,"message.commit.error");
			}

		}
		return result;
	}

	//Ancillary methods ---------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Message mensaje){
		ModelAndView result;

		result = this.createEditModelAndView(mensaje, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message mensaje,
			final String messageError) {
		
		final ModelAndView result;
		Date sentMoment;
		Collection<MessageBox> messageBoxes;
		Actor sender;
		Actor recipient;

		sentMoment = mensaje.getSentMoment();
		messageBoxes = mensaje.getMessageBoxes();
		sender = mensaje.getSender();

		recipient = this.actorService.findOne(this.actorService
				.findByPrincipal().getId());

		result = new ModelAndView("message/broadcast");
		result.addObject("sentMoment", sentMoment);
		result.addObject("messageBoxes", messageBoxes);
		result.addObject("sender", sender);
		result.addObject("mensaje", mensaje);

		result.addObject("recipient", recipient);
		result.addObject("broadcast", true);
		result.addObject("possible", true);
		result.addObject("requestURI", "message/administrator/broadcast.do");
		result.addObject("message", messageError);

		return result;
	}
}
