
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Actor;
import domain.Enrolment;
import domain.March;
import domain.Message;
import domain.MessageBox;
import domain.Parade;

@Service
@Transactional
public class MessageService {

	@Autowired
	private MessageRepository	messageRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;

	@Autowired
	private MessageBoxService	messageBoxService;

	@Autowired
	private UtilityService		utilityService;


	// CRUD Methods -----------------------------------------------------------
	public Message create() {

		final Message result;
		Actor principal;
		final Collection<MessageBox> boxes = new ArrayList<MessageBox>();

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = new Message();

		result.setSender(principal);
		result.setMessageBoxes(boxes);
		result.setIsSpam(false);
		result.setSentMoment(new Date(System.currentTimeMillis() - 1));

		return result;

	}

	public Message save(final Message message) {
		Message result = null;
		Actor principal;
		Date sentMoment;
		String tags;
		final Collection<MessageBox> messageBoxes = new ArrayList<MessageBox>();
		MessageBox inSpamBox = null, outBox;

		// TODO: Check spam

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(message);
		Assert.isTrue(message.getId() == 0);

		tags = message.getTags();

		sentMoment = new Date(System.currentTimeMillis() - 1);

		final List<String> atributosAComprobar = new ArrayList<>();
		atributosAComprobar.add(message.getBody());
		atributosAComprobar.add(message.getSubject());

		final boolean containsSpam = this.utilityService.isSpam(atributosAComprobar);
		if (containsSpam) {
			message.setIsSpam(true);
			message.setTags("spam");
		}

		if (message.getTags() != null) {
			if (message.getTags().equals("spam")) {

				inSpamBox = this.messageBoxService.findByName(message.getRecipient().getId(), "Spam box");
				Assert.notNull(inSpamBox);

			} else {

				inSpamBox = this.messageBoxService.findByName(message.getRecipient().getId(), "In box");
				Assert.notNull(inSpamBox);

			}
		} else {

			inSpamBox = this.messageBoxService.findByName(message.getRecipient().getId(), "In box");
			Assert.notNull(inSpamBox);

		}

		outBox = this.messageBoxService.findByName(principal.getId(), "Out box");
		Assert.notNull(outBox);

		messageBoxes.add(inSpamBox);
		messageBoxes.add(outBox);

		message.setSentMoment(sentMoment);
		message.setMessageBoxes(messageBoxes);

		result = this.messageRepository.save(message);

		outBox.getMessages().add(result);
		inSpamBox.getMessages().add(result);

		return result;
	}

	public void delete(final Message message) {
		Actor principal;
		MessageBox trashBoxPrincipal;
		Collection<Message> messagesTrashBox;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);

		trashBoxPrincipal = this.messageBoxService.findByName(principal.getId(), "Trash box");
		Assert.notNull(trashBoxPrincipal);

		messagesTrashBox = trashBoxPrincipal.getMessages();
		Assert.notNull(messagesTrashBox);

		if (messagesTrashBox.contains(message)) {
			if (this.findMessage(message))
				messagesTrashBox.remove(message);
			else {
				messagesTrashBox.remove(message);
				this.messageRepository.delete(message);

			}
		} else
			this.move(message, trashBoxPrincipal);

	}

	public Message findOne(final int messageId) {
		Message result;

		result = this.messageRepository.findOne(messageId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Message> findAll() {
		Collection<Message> result;

		result = this.messageRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	// Other business methods --------------------------------------------------

	public boolean findMessage(final Message message) {
		boolean result = false;
		Actor sender;
		Actor recipient;
		Collection<MessageBox> messageBoxSender, messageBoxRecipient;

		messageBoxRecipient = new ArrayList<MessageBox>();
		messageBoxSender = new ArrayList<MessageBox>();

		sender = message.getSender();
		Assert.notNull(sender);

		recipient = message.getRecipient();
		Assert.notNull(recipient);

		messageBoxRecipient = this.messageBoxService.findByOwner(recipient.getId());
		Assert.notNull(messageBoxRecipient);

		messageBoxSender = this.messageBoxService.findByOwner(sender.getId());
		Assert.notNull(messageBoxSender);

		for (final MessageBox mb : messageBoxSender)
			if (!mb.getName().equals("Trash box"))
				if (mb.getMessages().contains(message))
					result = true;

		for (final MessageBox mb : messageBoxRecipient)
			if (!mb.getName().equals("Trash box"))
				if (mb.getMessages().contains(message)) {
					result = true;
					break;
				}
		return result;
	}

	public void move(final Message message, final MessageBox destination) {
		Actor principal;
		MessageBox origin = null;
		Collection<MessageBox> principalBoxes, messageBoxes, recipientBoxes;
		Collection<Message> messages, updatedOriginBox, updatedDestinationBox;
		Actor recipient;

		principalBoxes = new ArrayList<MessageBox>();
		messageBoxes = new ArrayList<MessageBox>();
		messages = new ArrayList<Message>();
		updatedOriginBox = new ArrayList<Message>();
		updatedDestinationBox = new ArrayList<Message>();
		recipientBoxes = new ArrayList<MessageBox>();

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		recipient = message.getRecipient();

		Assert.isTrue(message.getId() != 0);
		Assert.isTrue(destination.getId() != 0);

		principalBoxes = this.messageBoxService.findByOwner(principal.getId());
		recipientBoxes = this.messageBoxService.findByOwner(recipient.getId());

		for (final MessageBox mb : principalBoxes)
			messages.addAll(mb.getMessages());

		Assert.isTrue(messages.contains(message));

		for (final MessageBox principalBox : principalBoxes)
			if (principalBox.getMessages().contains(message)) {
				origin = principalBox;
				break;
			}

		for (final MessageBox recipientBox : recipientBoxes)
			if (recipientBox.getMessages().contains(message))
				messageBoxes.add(recipientBox);

		messageBoxes.add(destination);

		Assert.isTrue(principalBoxes.contains(origin));
		Assert.isTrue(principalBoxes.contains(destination));

		message.setMessageBoxes(messageBoxes);

		updatedOriginBox = origin.getMessages();
		updatedDestinationBox = destination.getMessages();

		updatedOriginBox.remove(message);
		updatedDestinationBox.add(message);

		origin.setMessages(updatedOriginBox);
		destination.setMessages(updatedDestinationBox);

	}

	public void broadcast(final Message m) {
		Actor principal;
		String subject, priority, body, tags;
		Date sentMoment;
		boolean isSpam;
		MessageBox outBoxAdmin;
		Collection<MessageBox> allBoxes, boxes, notificationBoxes;
		Message saved;
		Collection<Actor> recipients;

		allBoxes = new ArrayList<MessageBox>();
		notificationBoxes = new ArrayList<MessageBox>();
		boxes = new ArrayList<MessageBox>();
		recipients = new ArrayList<Actor>();

		recipients = this.actorService.findAll();

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMINISTRATOR"));

		Assert.notNull(m);

		subject = m.getSubject();
		body = m.getBody();
		priority = m.getPriority();
		tags = m.getTags();
		isSpam = false;
		sentMoment = new Date(System.currentTimeMillis() - 1);

		for (final Actor a : recipients)
			if (!(this.actorService.checkAuthority(a, "ADMINISTRATOR")))
				notificationBoxes.add(this.messageBoxService.findByName(a.getId(), "Notification box"));

		outBoxAdmin = this.messageBoxService.findByName(principal.getId(), "Out box");
		Assert.notNull(outBoxAdmin);

		final Message message = new Message();
		message.setSubject(subject);
		message.setBody(body);
		message.setSentMoment(sentMoment);
		message.setPriority(priority);
		message.setTags(tags);
		message.setIsSpam(isSpam);
		message.setRecipient(principal);
		message.setSender(principal);

		boxes.add(outBoxAdmin);
		boxes.addAll(notificationBoxes);

		message.setMessageBoxes(boxes);

		saved = this.messageRepository.save(message);

		for (final MessageBox notBox : notificationBoxes)
			notBox.getMessages().add(saved);

		outBoxAdmin.getMessages().add(saved);

	}

	public void deleteMessages(final Message m, final MessageBox mb) {
		final Collection<Message> messages = new ArrayList<Message>();

		mb.setMessages(messages);

	}

	public Message reconstruct(final Message message, final BindingResult binding) {
		Message result = this.create();

		if (message.getId() == 0) {
			result.setSubject(message.getSubject());
			result.setBody(message.getBody());
			result.setPriority(message.getPriority());
			result.setTags(message.getTags());
			result.setRecipient(message.getRecipient());

			this.validator.validate(result, binding);

		} else {
			final Message m = this.messageRepository.findOne(message.getId());
			message.setSubject(m.getSubject());
			message.setBody(m.getBody());
			message.setPriority(m.getPriority());
			message.setSentMoment(m.getSentMoment());
			message.setTags(m.getTags());
			message.setRecipient(m.getRecipient());
			message.setSender(m.getSender());

			result = message;

			this.validator.validate(result, binding);

		}
		return result;
	}

	public Message reconstructBroadcast(final Message message, final BindingResult binding) {

		final Message result = this.create();
		Actor principal;

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMINISTRATOR"));
		Assert.isTrue(message.getId() == 0);

		result.setSubject(message.getSubject());
		result.setBody(message.getBody());
		result.setPriority(message.getPriority());
		result.setTags(message.getTags());
		result.setRecipient(principal);

		this.validator.validate(result, binding);

		return result;

	}

	public Message changeStatusNotfication(final March march, final Actor actor, final Date moment) {

		Message result;
		Collection<MessageBox> boxes;
		MessageBox notificationBox, outBox;
		Actor principal;
		Message saved;

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
		Assert.isTrue(actor.getId() == march.getMember().getId());

		boxes = new ArrayList<MessageBox>();

		notificationBox = this.messageBoxService.findByName(actor.getId(), "Notification box");
		Assert.notNull(notificationBox);

		outBox = this.messageBoxService.findByName(principal.getId(), "Out box");
		Assert.notNull(outBox);

		boxes.add(notificationBox);
		boxes.add(outBox);

		result = new Message();
		result.setSender(principal);
		result.setRecipient(actor);
		result.setPriority("NORMAL");
		result.setSentMoment(moment);
		result.setIsSpam(false);
		result.setMessageBoxes(boxes);
		result.setBody("Request have been changed status to " + march.getStatus());
		result.setSubject("Request status changed");
		result.setTags("status changed");

		saved = this.messageRepository.save(result);

		outBox.getMessages().add(saved);
		notificationBox.getMessages().add(saved);

		return result;

	}

	public Message notificationPublishParade(final Parade p) {
		final Message result = new Message();
		Actor principal;
		Collection<Actor> recipients;
		Collection<MessageBox> notBoxes, boxes;
		MessageBox outBox;
		Message saved;

		notBoxes = new ArrayList<MessageBox>();
		boxes = new ArrayList<MessageBox>();

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));

		recipients = this.actorService.findAll();

		for (final Actor a : recipients)
			notBoxes.add(this.messageBoxService.findByName(a.getId(), "Notification Box"));

		outBox = this.messageBoxService.findByName(principal.getId(), "Out box");

		boxes.add(outBox);
		boxes.addAll(notBoxes);

		result.setSubject("parade: " + p.getTitle() + "is published");
		result.setBody("Its date is: " + p.getOrganisedMoment());
		result.setSender(principal);
		result.setRecipient(principal);
		result.setPriority("NORMAL");
		result.setIsSpam(false);
		result.setMessageBoxes(boxes);
		result.setSentMoment(new Date(System.currentTimeMillis() - 1));
		result.setTags("new parade");

		saved = this.messageRepository.save(result);

		for (final MessageBox mb : notBoxes)
			mb.getMessages().add(saved);

		outBox.getMessages().add(saved);

		return saved;
	}

	public Message notificationMessageEnrolAMember(final Enrolment e) {
		final Message result = new Message();

		Actor principal;
		MessageBox outBox, notBox;
		Collection<MessageBox> boxes;
		Message saved;

		boxes = new ArrayList<MessageBox>();

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
		Assert.isTrue(this.actorService.checkAuthority(e.getMember(), "MEMBER"));

		outBox = this.messageBoxService.findByName(principal.getId(), "Out Box");
		Assert.notNull(outBox);

		notBox = this.messageBoxService.findByName(e.getMember().getId(), "Notification Box");
		Assert.notNull(notBox);

		boxes.add(outBox);
		boxes.add(notBox);

		result.setSubject("Member " + e.getMember().getName() + " has been enrolled in this brotherhood: " + e.getBrotherhood().getTitle());
		result.setBody("Date is: " + e.getMoment());
		result.setSender(principal);
		result.setRecipient(e.getMember());
		result.setPriority("NORMAL");
		result.setIsSpam(false);
		result.setMessageBoxes(boxes);
		result.setSentMoment(new Date(System.currentTimeMillis() - 1));
		result.setTags("New member enrolled");

		saved = this.messageRepository.save(result);

		outBox.getMessages().add(saved);
		notBox.getMessages().add(saved);

		return saved;

	}

	public Message notificationOutMember(final Enrolment e) {

		Assert.isTrue(e.getIsOut() == true);

		final Message result = new Message();
		Actor principal;
		MessageBox outBox, notBox;
		Collection<MessageBox> boxes;
		Message saved;

		boxes = new ArrayList<MessageBox>();

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(this.actorService.checkAuthority(principal, "MEMBER") || this.actorService.checkAuthority(principal, "BROTHERHOOD"));
		Assert.isTrue((principal.getId() == e.getMember().getId()) || (principal.getId() == e.getBrotherhood().getId()));

		outBox = this.messageBoxService.findByName(principal.getId(), "Out box");
		Assert.notNull(outBox);

		notBox = this.messageBoxService.findByName(e.getBrotherhood().getId(), "Notification box");
		Assert.notNull(notBox);

		boxes.add(outBox);
		boxes.add(notBox);

		result.setSubject("Member " + e.getMember().getName() + " is Out from Brotherhood :" + e.getBrotherhood().getTitle());
		result.setBody("Date is: " + e.getMoment());
		result.setSender(principal);
		result.setRecipient(e.getBrotherhood());
		result.setMessageBoxes(boxes);
		result.setTags("Member out of a brotherhood");
		result.setPriority("NEUTRAL");
		result.setIsSpam(false);
		result.setSentMoment(new Date(System.currentTimeMillis() - 1));

		saved = this.messageRepository.save(result);

		outBox.getMessages().add(saved);
		notBox.getMessages().add(saved);

		return saved;

	}

	public Integer findNumberMessagesByActorId(final int actorId) {
		final Integer result = this.messageRepository.findNumberMessagesByActorId(actorId);

		return result;
	}

	public Integer findNumberMessagesSpamByActorId(final int actorId) {
		final Integer result = this.messageRepository.findNumberMessagesSpamByActorId(actorId);

		return result;
	}

	public Collection<Message> findMessagesByActorId(final int actorId) {
		final Collection<Message> result = this.messageRepository.findMessagesByActorId(actorId);

		return result;
	}
	public Collection<Message> AllMessagesInvolvedActor(final int actorId) {
		return this.messageRepository.AllMessagesInvolvedActor(actorId);
	}

}
