package controllers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import domain.Administrator;
import domain.Message;
import domain.MessageBox;
import domain.SocialProfile;

import services.ActorService;
import services.AdministratorService;
import services.MessageBoxService;
import services.MessageService;
import services.SocialProfileService;

@Controller
public class ExportDataController extends AbstractController {
	
	@Autowired
	private ActorService actorService;
	
	/*@RequestMapping(value="/export.do",method=RequestMethod.GET)
	public ModelAndView export(final Locale locale){
		ModelAndView result;
		//hacer cadena español e ingles
		
			String res;
			res="Name: "+this.actorService.dataMember().getName()+" /n"+"Apellidos: "+ this.actorService.dataMember().getAddress();
			try (PrintStream out = new PrintStream(new FileOutputStream("dataUser.txt"))) {
			    out.print(res);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		

		result=new ModelAndView("export");

		return result;
		
		
	}*/
/*	 @RequestMapping(value = "/export.do", method = RequestMethod.GET)
	 public @ResponseBody  void downloadFile(HttpServletResponse resp) {
	  String downloadFileName= "dataUser";
	  String res;
	  
	  res="Name: "+this.actorService.dataMember().getName()+" /n "+"Apellidos: "+ this.actorService.dataMember().getAddress();
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
*/

	 	@Autowired
	 	private SocialProfileService	socialProfileService;

	 	@Autowired
	 	private MessageService			messageService;

	 	@Autowired
	 	private MessageBoxService				messageBoxService;

	 	@Autowired
	 	private AdministratorService	administratorService;


	
}
