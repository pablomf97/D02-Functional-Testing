
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageBoxRepository;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Service
@Transactional
public class MessageBoxService {

	@Autowired
	private MessageBoxRepository	messageBoxRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MessageService			messageService;
	@Autowired
	private Validator				validator;


	public MessageBox create(final Actor actor) {
		final MessageBox b = new MessageBox();
		b.setOwner(actor);
		b.setIsPredefined(false);
		b.setMessages(new ArrayList<Message>());
		return b;
	}

	public MessageBox save(final MessageBox box) {
		// We search boxes from logged actor
		MessageBox saved;
		if (box.getId() != 0) {
			final MessageBox boxBD = this.findOne(box.getId());
			Assert.isTrue(this.actorService.findByPrincipal().equals(box.getOwner()) || this.actorService.findByPrincipal().equals(boxBD.getOwner()));
			if (boxBD.getIsPredefined() == false)
				boxBD.setName(box.getName());
			Assert.isTrue(this.checkParentBox(boxBD, box));
			boxBD.setParentMessageBoxes(box.getParentMessageBoxes());
			boxBD.setMessages(box.getMessages());
			saved = this.messageBoxRepository.save(boxBD);
		} else
			saved = this.messageBoxRepository.save(box);
		return saved;
	}

	public MessageBox insertMessage(MessageBox box, final Message message) {
		// We insert a new message to the box
		box = this.messageBoxRepository.findOne(box.getId());
		final Collection<Message> messages = box.getMessages();
		if (!messages.contains(message)) {
			messages.add(message);
			box.setMessages(messages);
		}
		final MessageBox saved = this.messageBoxRepository.save(box);
		return saved;
	}

	public Collection<MessageBox> findAll() {
		return this.messageBoxRepository.findAll();
	}

	public MessageBox findOne(final int idBox) {
		final MessageBox box = this.messageBoxRepository.findOne(idBox);
		return box;
	}

	public Collection<MessageBox> findByParent(final int idBox) {
		final Collection<MessageBox> boxes = this.messageBoxRepository.findByParent(idBox);
		return boxes;
	}

	public void delete(final MessageBox messageBox) {
		Assert.isTrue((this.actorService.findByPrincipal().equals(messageBox.getOwner())), "Box not belong to the logged actor");
		Assert.isTrue(!(messageBox.getIsPredefined()), "Box undeleteable");
		final Collection<MessageBox> childBoxes = this.messageBoxRepository.findByParent(messageBox.getId());
		if (!childBoxes.isEmpty())
			for (final MessageBox b : childBoxes)
				this.delete(b);
		if (messageBox.getMessages() != null)
			for (final Message m : messageBox.getMessages())
				//TODO: cuando esté mensajes hecho
				this.messageService.deleteMessages(m, messageBox);
		this.messageBoxRepository.delete(messageBox);
	}
	//Other requirements
	public void initializeDefaultBoxes(final Actor a) {

		final MessageBox in = this.create(a);
		in.setIsPredefined(true);
		in.setName("IN");
		this.save(in);

		final MessageBox trash = this.create(a);
		trash.setIsPredefined(true);
		trash.setName("TRASH");
		this.save(trash);

		final MessageBox out = this.create(a);
		out.setIsPredefined(true);
		out.setName("OUT");
		this.save(out);

		final MessageBox spam = this.create(a);
		spam.setIsPredefined(true);
		spam.setName("SPAM");
		this.save(spam);

		final MessageBox notification = this.create(a);
		notification.setIsPredefined(true);
		notification.setName("NOTIFICATION");
		this.save(notification);

	}

	public Collection<MessageBox> findByOwner(final int actorId) {
		return this.messageBoxRepository.boxesByActor(actorId);
	}
	/**
	 * First level of boxes
	 * 
	 * @param actorId
	 * @return
	 */
	public Collection<MessageBox> findByOwnerFirst(final int actorId) {
		return this.messageBoxRepository.firstBoxesByActor(actorId);
	}

	public MessageBox findByName(final int actorId, final String name) {
		return this.messageBoxRepository.boxByName(actorId, name);
	}

	public MessageBox reconstruct(final MessageBox messageBox, final BindingResult binding) {
		final Actor actor = this.actorService.findByPrincipal();
		final MessageBox result = this.create(actor);
		if (messageBox.getId() == 0) {
			result.setName(messageBox.getName());
			result.setParentMessageBoxes(messageBox.getParentMessageBoxes());
			this.validator.validate(result, binding);
		} else {
			final MessageBox bd = this.messageBoxRepository.findOne(messageBox.getId());
			Assert.notNull(bd);
			result.setName(messageBox.getName());
			result.setParentMessageBoxes(messageBox.getParentMessageBoxes());
			this.validator.validate(result, binding);
			if (!binding.hasErrors()) {
				result.setId(bd.getId());
				result.setIsPredefined(bd.getIsPredefined());
				result.setParentMessageBoxes(messageBox.getParentMessageBoxes());
			}
		}
		return result;
	}

	public boolean checkUniqueBox(final MessageBox box) {
		boolean bool = true;
		final Actor actor = this.actorService.findByPrincipal();
		final MessageBox mb = this.findByName(actor.getId(), box.getName());
		if (mb != null)
			bool = false;
		return bool;
	}
	public boolean checkParentBox(final MessageBox boxBD, final MessageBox box) {
		boolean bool = true;
		final Collection<MessageBox> boxes = this.messageBoxRepository.findByParent(boxBD.getId());
		if (boxes.contains(box.getParentMessageBoxes()))
			bool = false;
		return bool;

	}
}
