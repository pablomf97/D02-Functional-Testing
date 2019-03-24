package controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import domain.Administrator;
import domain.Brotherhood;
import domain.Chapter;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.Member;
import domain.Message;
import domain.MessageBox;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;
import domain.SocialProfile;

import services.ActorService;

import services.BrotherhoodService;
import services.MessageBoxService;
import services.MessageService;
import services.SocialProfileService;
@Controller
public class ExportDataController extends AbstractController{

	@Autowired
	private ActorService actorService;


	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private MessageBoxService			messageBoxService;

	
	@Autowired
	private BrotherhoodService brotherhoodService;

	@RequestMapping(value = "administrator/export.do", method = RequestMethod.GET)
	public @ResponseBody  void downloadFileAdministrator(HttpServletResponse resp) {
		String downloadFileName= "dataUser";
		String res;
		final Administrator actor = (Administrator) this.actorService.findByPrincipal();
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.socialProfilesByUserId(actor.getId());
		final Collection<MessageBox> boxes = this.messageBoxService.findByOwner(actor.getId());
		final Collection<Message> messagesS = this.messageService.AllMessagesInvolvedActor(actor.getId());
		
		res="Data of your user account:";
		
		res += "\r\n\r\n";
		
		res+= "Name: " + actor.getName() + " \r\n" + "Middle Name: " + actor.getMiddleName() + " \r\n" + "Surname: " + actor.getSurname() + " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: " + actor.getEmail() + " \r\n" + "Phone Number: "
	 			+ actor.getPhoneNumber() + " \r\n" + "Address: " + actor.getAddress() + " \r\n";
		

 		res += "\r\n\r\n";
 		res += "----------------------------------------";
 		res += "\r\n\r\n";
 		
		
 		res += "\r\n\r\n";
 		res += "----------------------------------------";
 		res += "\r\n\r\n";
 		
 		if(!socialProfiles.isEmpty()){
 		res+="Social profiles:";
 		res += "\r\n";
 		
 			
 		
 		for (final SocialProfile socialProfile : socialProfiles)
 			res += "Nick: " + socialProfile.getNick() + " \r\n" + "Link profile: " + socialProfile.getLinkProfile() + " \r\n" + "Social Network: " + socialProfile.getLinkProfile() + "\r\n";

 		res += "\r\n\r\n";
 		res += "----------------------------------------";
 		res += "\r\n\r\n";
 		
 		res += "Boxes:\r\n";
 		res += "\r\n";
 		}
 		
 		for (final MessageBox box : boxes)
 			res += "Name: " + box.getName() + "\r\n";

 		res += "\r\n\r\n";
 		res += "-------------------------------------------------------------";
 		res += "\r\n\r\n";
 		res += "Messages involved:";
 		res += "\r\n\r\n";
 		if(!messagesS.isEmpty()){
 		for (final Message message : messagesS) {
 			if(message.getRecipient()!=null || message.getRecipient()!=null){
 				//res += "Recipient: " + message.getRecipient().getName() + " \r\n";
 				//res += "Sender: " + message.getSender().getName() + " \r\n";
 			}
 			
 			res+="Subject: "+message.getSubject()+ " \r\n";
 			res+="Body: "+message.getBody()+ " \r\n";
 			res+="Sent moment:"+message.getSentMoment()+ " \r\n";
 			res+="Priority: "+message.getPriority()+ " \r\n";
 			res+="Tags: "+message.getTags()+ " \r\n";
 		}
 		}
 
		
		String downloadStringContent= res; // implement this
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/plain; charset=utf-8");
			resp.addHeader("Content-Disposition","attachment; filename=\"" + downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}
	}
	@RequestMapping(value = "member/export.do", method = RequestMethod.GET)
	public @ResponseBody  void downloadFileMember(HttpServletResponse resp) {
		String downloadFileName= "dataUser";
		String res;
		final Member actor = (Member) this.actorService.findByPrincipal();
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.socialProfilesByUserId(actor.getId());
		final Collection<MessageBox> boxes = this.messageBoxService.findByOwner(actor.getId());
		final Collection<Message> messagesS = this.messageService.AllMessagesInvolvedActor(actor.getId());
		final Collection<Brotherhood> bros = this.brotherhoodService.brotherhoodsByMemberInId(actor.getId());
		res="Data of your user account:";
		
		res += "\r\n\r\n";
		
		res+= "Name: " + actor.getName() + " \r\n" + "Middle Name: " + actor.getMiddleName() + " \r\n" + "Surname: " + actor.getSurname() + " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: " + actor.getEmail() + " \r\n" + "Phone Number: "
	 			+ actor.getPhoneNumber() + " \r\n" + "Address: " + actor.getAddress() + " \r\n";
		

 		res += "\r\n\r\n";
 		res += "----------------------------------------";
 		res += "\r\n\r\n";
 		
 		res+="Finder:Search in caché";
 		res += "\r\n";
 		res+="Results in last search:"+actor.getFinder().getSearchResults()+"\r\n\r\n";
		
 		res += "\r\n\r\n";
 		res += "----------------------------------------";
 		res += "\r\n\r\n";
 		
 		if(!socialProfiles.isEmpty()){
 		res+="Social profiles:";
 		res += "\r\n";
 		
 			
 		
 		for (final SocialProfile socialProfile : socialProfiles)
 			res += "Nick: " + socialProfile.getNick() + " \r\n" + "Link profile: " + socialProfile.getLinkProfile() + " \r\n" + "Social Network: " + socialProfile.getLinkProfile() + "\r\n";

 		res += "\r\n\r\n";
 		res += "----------------------------------------";
 		res += "\r\n\r\n";
 		
 		res += "Boxes:\r\n";
 		res += "\r\n";
 		}
 		
 		for (final MessageBox box : boxes)
 			res += "Name: " + box.getName() + "\r\n";

 		res += "\r\n\r\n";
 		res += "-------------------------------------------------------------";
 		res += "\r\n\r\n";
 		res += "Messages involved:";
 		res += "\r\n\r\n";
 		if(!messagesS.isEmpty()){
 		for (final Message message : messagesS) {
 			if(message.getRecipient()!=null || message.getRecipient()!=null){
 				//res += "Recipient: " + message.getRecipient().getName() + " \r\n";
 				//res += "Sender: " + message.getSender().getName() + " \r\n";
 			}
 			
 			res+="Subject: "+message.getSubject()+ " \r\n";
 			res+="Body: "+message.getBody()+ " \r\n";
 			res+="Sent moment:"+message.getSentMoment()+ " \r\n";
 			res+="Priority: "+message.getPriority()+ " \r\n";
 			res+="Tags: "+message.getTags()+ " \r\n";
 		}
 		}
 		res += "\r\n\r\n";
 		res += "-------------------------------------------------------------";
 		res += "\r\n\r\n";
 		res += "Brotherhoods registered:";
 		res += "\r\n\r\n";
 		for(Brotherhood b :bros){
 			res+="Brotherhood: "+b.getTitle()+ " \r\n";
 		}
 		
 		String downloadStringContent= res; // implement this
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/plain; charset=utf-8");
			resp.addHeader("Content-Disposition","attachment; filename=\"" + downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}
	}

	@RequestMapping(value = "brotherhood/export.do", method = RequestMethod.GET)
	public @ResponseBody  void downloadFileBrotherhood(HttpServletResponse resp) {
		String downloadFileName= "dataUser";
		String res;
		final Brotherhood actor = (Brotherhood) this.actorService.findByPrincipal();
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.socialProfilesByUserId(actor.getId());
		final Collection<MessageBox> boxes = this.messageBoxService.findByOwner(actor.getId());
		final Collection<Message> messagesS = this.messageService.AllMessagesInvolvedActor(actor.getId());
		
		res="Data of your user account:";
		
		res += "\r\n\r\n";
		
		res+= "Name: " + actor.getName() + " \r\n" + "Middle Name: " + actor.getMiddleName() + " \r\n" + "Surname: " + actor.getSurname() + " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: " + actor.getEmail() + " \r\n" + "Phone Number: "
	 			+ actor.getPhoneNumber() + " \r\n" + "Address: " + actor.getAddress() + " \r\n"
	 			+"Title: "+ actor.getTitle() + " \r\n"	+"Establishment Date: "+ actor.getEstablishmentDate() + " \r\n"	+"Pictures: "+ actor.getPictures() + " \r\n"+"Area: "+ actor.getZone().getName() + " \r\n";
		

 		res += "\r\n\r\n";
 		res += "----------------------------------------";

 		res += "\r\n\r\n";
 		res+="History";
 		if(actor.getHistory().getInceptionRecord()!=null){
 			res+="Inception record: "+actor.getHistory().getInceptionRecord().getTitle()+" \r\n";
 			res+="Description: "+actor.getHistory().getInceptionRecord().getDescription()+" \r\n";
 		}
 		if(!actor.getHistory().getLegalRecords().isEmpty()){
 			for(LegalRecord lr:actor.getHistory().getLegalRecords()){
 				res+="Legal record: "+lr.getTitle()+" \r\n";
 				res+="Description: "+lr.getDescription()+" \r\n";
 				res+="Laws: "+lr.getLaws()+" \r\n";
 				res+="VAT "+lr.getVAT()+" \r\n";
 				res+="VAT "+lr.getName()+" \r\n";
 			}
 		}else if (!actor.getHistory().getLinkRecords().isEmpty()){
 			for(LinkRecord lr:actor.getHistory().getLinkRecords()){
 				res+="Link record: "+lr.getTitle()+" \r\n";
 				res+="Description: "+lr.getDescription()+" \r\n";
 				res+="Linked Brotherhood: "+lr.getLinkedBrotherhood().getName()+" \r\n";
 			
 			}
 		}else if(!actor.getHistory().getMiscellaneousRecords().isEmpty()){
 			for(MiscellaneousRecord lr:actor.getHistory().getMiscellaneousRecords()){
 				res+="Misc record: "+lr.getTitle()+" \r\n";
 				res+="Description: "+lr.getDescription()+" \r\n";
 				
 			
 			}
 		}else if (!actor.getHistory().getPeriodRecords().isEmpty()){
 			for(PeriodRecord lr:actor.getHistory().getPeriodRecords()){
 				res+="Period record: "+lr.getTitle()+" \r\n";
 				res+="Description: "+lr.getDescription()+" \r\n";
 				res+="End year: "+lr.getEndYear()+"Start Year: "+lr.getStartYear()+"\r\n";
 				res+="Photos: "+lr.getPhotos()+" \r\n";
 			}
 			
 		}
 		
 		
 		res += "\r\n\r\n";
 		res += "----------------------------------------";

 		res += "\r\n\r\n";
 		
 		if(!socialProfiles.isEmpty()){
 		res+="Social profiles:";
 		res += "\r\n";
 		
 			
 		
 		for (final SocialProfile socialProfile : socialProfiles)
 			res += "Nick: " + socialProfile.getNick() + " \r\n" + "Link profile: " + socialProfile.getLinkProfile() + " \r\n" + "Social Network: " + socialProfile.getLinkProfile() + "\r\n";

 		res += "\r\n\r\n";
 		res += "----------------------------------------";
 		res += "\r\n\r\n";
 		
 		res += "Boxes:\r\n";
 		res += "\r\n";
 		}
 		
 		for (final MessageBox box : boxes)
 			res += "Name: " + box.getName() + "\r\n";

 		res += "\r\n\r\n";
 		res += "-------------------------------------------------------------";
 		res += "\r\n\r\n";
 		res += "Messages involved:";
 		res += "\r\n\r\n";
 		if(!messagesS.isEmpty()){
 	 		for (final Message message : messagesS) {
 	 			if(message.getRecipient()!=null || message.getRecipient()!=null){
 	 			//	res += "Recipient: " + message.getRecipient().getName() + " \r\n";
 	 				//res += "Sender: " + message.getSender().getName() + " \r\n";
 	 			}
 	 			
 	 			res+="Subject: "+message.getSubject()+ " \r\n";
 	 			res+="Body: "+message.getBody()+ " \r\n";
 	 			res+="Sent moment:"+message.getSentMoment()+ " \r\n";
 	 			res+="Priority: "+message.getPriority()+ " \r\n";
 	 			res+="Tags: "+message.getTags()+ " \r\n";
 	 		}
 	 		}
 		res += "\r\n\r\n";
 		res += "-------------------------------------------------------------";
 		res += "\r\n\r\n";
 		
 		res += "";
 
 		String downloadStringContent= res; // implement this
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/plain; charset=utf-8");
			resp.addHeader("Content-Disposition","attachment; filename=\"" + downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}
	}
	@RequestMapping(value = "chapter/export.do", method = RequestMethod.GET)
	public @ResponseBody  void downloadFileChapter(HttpServletResponse resp) {
		String downloadFileName= "dataUser";
		String res;
		final Chapter actor = (Chapter) this.actorService.findByPrincipal();
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.socialProfilesByUserId(actor.getId());
		final Collection<MessageBox> boxes = this.messageBoxService.findByOwner(actor.getId());
		final Collection<Message> messagesS = this.messageService.AllMessagesInvolvedActor(actor.getId());
		
		res="Data of your user account:";
		
		res += "\r\n\r\n";
		
		res+= "Name: " + actor.getName() + " \r\n" + "Middle Name: " + actor.getMiddleName() + " \r\n" + "Surname: " + actor.getSurname() + " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: " + actor.getEmail() + " \r\n" + "Phone Number: "
	 			+ actor.getPhoneNumber() + " \r\n" + "Address: " + actor.getAddress() + " \r\n"+ "Tittle: " + actor.getTitle() + " \r\n";
		

 		res += "\r\n\r\n";
 		res += "----------------------------------------";
 		res += "\r\n\r\n";
 		
 		res += "Area: "+actor.getZone().getName();
 		
 		res += "\r\n\r\n";
 		res += "----------------------------------------";
 		res += "\r\n\r\n";
 		
 		if(!socialProfiles.isEmpty()){
 		res+="Social profiles:";
 		res += "\r\n";
 		
 			
 		
 		for (final SocialProfile socialProfile : socialProfiles)
 			res += "Nick: " + socialProfile.getNick() + " \r\n" + "Link profile: " + socialProfile.getLinkProfile() + " \r\n" + "Social Network: " + socialProfile.getLinkProfile() + "\r\n";

 		res += "\r\n\r\n";
 		res += "----------------------------------------";
 		res += "\r\n\r\n";
 		
 		res += "Boxes:\r\n";
 		res += "\r\n";
 		}
 		
 		for (final MessageBox box : boxes)
 			res += "Name: " + box.getName() + "\r\n";

 		res += "\r\n\r\n";
 		res += "-------------------------------------------------------------";
 		res += "\r\n\r\n";
 		res += "Messages involved:";
 		res += "\r\n\r\n";
 		if(!messagesS.isEmpty()){
 	 		for (final Message message : messagesS) {
 	 			if(message.getRecipient()!=null || message.getRecipient()!=null){
 	 			//	res += "Recipient: " + message.getRecipient().getName() + " \r\n";
 	 				//res += "Sender: " + message.getSender().getName() + " \r\n";
 	 			}
 	 			
 	 			res+="Subject: "+message.getSubject()+ " \r\n";
 	 			res+="Body: "+message.getBody()+ " \r\n";
 	 			res+="Sent moment:"+message.getSentMoment()+ " \r\n";
 	 			res+="Priority: "+message.getPriority()+ " \r\n";
 	 			res+="Tags: "+message.getTags()+ " \r\n";
 	 		}
 	 		}
 		res += "\r\n\r\n";
 		res += "-------------------------------------------------------------";
 		res += "\r\n\r\n";

		String downloadStringContent= res; // implement this
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/plain; charset=utf-8");
			resp.addHeader("Content-Disposition","attachment; filename=\"" + downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}
	}


}
