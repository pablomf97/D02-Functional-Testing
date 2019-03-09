
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
import services.MessageBoxService;
import domain.Actor;
import domain.MessageBox;

@Controller
@RequestMapping("/messagebox")
public class MessageBoxController extends AbstractController {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private MessageBoxService	messageBoxService;


	public MessageBoxController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listAll() {
		ModelAndView result;
		try {
			result = new ModelAndView("messagebox/list");
			final Actor actor = this.actorService.findByPrincipal();
			final Collection<MessageBox> boxes = this.messageBoxService.findByOwnerFirst(actor.getId());
			result.addObject("requestURI", "/messagebox/list.do");
			result.addObject("messageboxes", boxes);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "box.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "content", method = RequestMethod.GET)
	public ModelAndView listBox(@RequestParam final int Id) {
		ModelAndView result;
		try {
			result = new ModelAndView("messagebox/content");
			final Actor actor = this.actorService.findByPrincipal();
			final MessageBox box = this.messageBoxService.findOne(Id);
			Assert.isTrue(box.getOwner().equals(actor));
			result.addObject("requestURI", "/messagebox/content.do");
			final Collection<MessageBox> childBoxes = this.messageBoxService.findByParent(box.getId());
			result.addObject("box", box);
			result.addObject("messageBoxes", childBoxes);
			result.addObject("messages", box.getMessages());
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "messagebox.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteBox(@RequestParam final int Id) {
		ModelAndView result;
		try {
			final MessageBox box = this.messageBoxService.findOne(Id);
			this.messageBoxService.delete(box);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageCode", "messagebox.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView saveBox(MessageBox box, final BindingResult binding) {
		ModelAndView result;
		String messageBoxName = "";
		try {
			box = this.messageBoxService.reconstruct(box, binding);
			final Collection<MessageBox> boxes = this.messageBoxService.findByOwner(this.actorService.findByPrincipal().getId());
			boxes.remove(box);
			final Collection<MessageBox> childBoxes = this.messageBoxService.findByParent(box.getId());
			boxes.removeAll(childBoxes);
			if (!this.messageBoxService.checkUniqueBox(box))
				messageBoxName = "messagebox.name.unique";
			if (binding.hasErrors() || !messageBoxName.isEmpty()) {
				result = new ModelAndView("messagebox/edit");
				result.addObject("messageBox", box);
				result.addObject("messageBoxes", boxes);
				result.addObject("uniqueBox", messageBoxName);
			} else
				try {
					if (box.getId() != 0) {
						final MessageBox b = this.messageBoxService.findOne(box.getId());
						final Actor actorLogged = this.actorService.findByPrincipal();
						Assert.isTrue(b.getOwner().equals(actorLogged));
					}
					this.messageBoxService.save(box);
					result = new ModelAndView("redirect:list.do");
				} catch (final Throwable opps) {
					opps.printStackTrace();
					result = new ModelAndView("messagebox/edit");
					result.addObject("messageBoxes", boxes);
					result.addObject("messageBox", box);
					result.addObject("messageCode", "messagebox.commit.error");
				}
		} catch (final Throwable opps) {
			//TODO: pantalla de error
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editBox(@RequestParam final int Id) {
		ModelAndView result;
		MessageBox box;
		try {
			box = this.messageBoxService.findOne(Id);
			final Actor actorLogged = this.actorService.findByPrincipal();
			Assert.isTrue(box.getOwner().equals(actorLogged));
			result = new ModelAndView("messagebox/edit");
			final Collection<MessageBox> boxes = this.messageBoxService.findByOwner(actorLogged.getId());
			final Collection<MessageBox> childBoxes = this.messageBoxService.findByParent(box.getId());
			Assert.notNull(box);
			boxes.remove(box);
			boxes.removeAll(childBoxes);
			result.addObject("messageBox", box);
			result.addObject("messageBoxes", boxes);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageCode", "messagebox.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Collection<MessageBox> boxes = this.messageBoxService.findByOwner(this.actorService.findByPrincipal().getId());
		try {
			final Actor actor = this.actorService.findByPrincipal();
			result = new ModelAndView("messagebox/edit");
			final MessageBox box = this.messageBoxService.create(actor);
			result.addObject("messageBox", box);
			result.addObject("messageBoxes", boxes);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageCode", "messagebox.commit.error");
		}
		return result;

	}
}
