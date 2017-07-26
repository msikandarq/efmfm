package com.newtglobal.eFmFmFleet.services;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Component
@Path("/notifications")
public class EmailNotification {

	private static Log log = LogFactory.getLog(EmailNotification.class);
	private JavaMailSender mailSender;
	private Configuration configuration;
	private String freemarkerTemplate;
	
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	   
	public void setFreemarkerMailConfiguration(Configuration configuration) {
	    this.configuration = configuration;
	}

	public void setFreemarkerTemplate(String freemarkerTemplate) {
	    this.freemarkerTemplate = freemarkerTemplate;
	}


	@GET
	@Path("/feedBackMail/{feedBackDateTime}/{category}/{subject}/{empDescription}/{toEmployeeFeedBackEmail}/{employeeFeedBackMailIds}/{userId}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void feedbackEmail(
			@PathParam("feedBackDateTime") String feedBackDateTime,
			@PathParam("category") String category,			
			@PathParam("subject") String subject,
			@PathParam("empDescription") String empDescription,			
			@PathParam("toEmployeeFeedBackEmail") String toEmployeeFeedBackEmail,
			@PathParam("employeeFeedBackMailIds") String employeeFeedBackMailIds,
			@PathParam("userId") int userId

			) throws Exception
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("MailNotification.xml");
		Configuration templateConfig = (Configuration) context.getBean("freemarkerMailConfiguration");
		JavaMailSender mailNotification = (JavaMailSender) context.getBean("mailSender");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(userId);

		
		System.out.println("toEmployeeFeedBackEmail"+toEmployeeFeedBackEmail);
		
		System.out.println("employeeFeedBackMailIds"+employeeFeedBackMailIds);

		
//		String templateName = command+".ftl";
//		final String strongmomentLink = ContextLoader.getContext().getMessage("Sm.acceptlinkpath", null, "url", null);
//		final String fromEmail =ContextLoader.getContext().getMessage("Sm.email", null, "smadmin@atsgrid.com", null);
	    String templateName = "Confirmation"+".ftl";		
		Map<String,String> map=new HashMap<String,String>();
		map.put("feedBackDateTime", feedBackDateTime);
        map.put("empName",new String(Base64.getDecoder().decode(userDetail.get(0).getFirstName()), "utf-8"));
        map.put("empEmailId",new String(Base64.getDecoder().decode(userDetail.get(0).getEmailId()), "utf-8"));
		map.put("category", category);
		map.put("subject", subject);
		map.put("empDescription", empDescription);
		map.put("imageIcon", "/images/OcwenLogo.png");

		
		StringBuffer message = new StringBuffer();
		try {
			message.append(FreeMarkerTemplateUtils
					.processTemplateIntoString(
							templateConfig.getTemplate(templateName),
							map));
		} catch (IOException e) {

	//		System.out.println(e);
			// handle
		} catch (TemplateException e) {
			// handle
		}
	
		SimpleMailMessage messg = new SimpleMailMessage();
		messg.setFrom("transport@newtglobal.com");
		messg.setTo(toEmployeeFeedBackEmail);		
		messg.setCc(stringArray(employeeFeedBackMailIds));
		messg.setSubject("Feedback Details");
		messg.setText(message.toString());
		
		MimeMessage msg = mailNotification.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setFrom("transport@newtglobal.com");
			helper.setTo(messg.getTo());
			helper.setCc(messg.getCc());
			helper.setSubject(messg.getSubject());
			helper.setText(messg.getText(), true);
			
		} catch (Exception e) {
			throw e;
		}
		mailNotification.send(msg);
		
		log.info("Email Sent With feedback ");
	}

	
	
	
	@GET
	@Path("/bugEmailFromEmployeeApp/{feedBackDateTime}/{category}/{subject}/{empDescription}/{toEmployeeFeedBackEmail}/{employeeFeedBackMailIds}/{userId}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void reportABugFromEmployeeApp(
			@PathParam("feedBackDateTime") String feedBackDateTime,
			@PathParam("category") String category,			
			@PathParam("subject") String subject,
			@PathParam("empDescription") String empDescription,			
			@PathParam("toEmployeeFeedBackEmail") String toEmployeeFeedBackEmail,
			@PathParam("employeeFeedBackMailIds") String employeeFeedBackMailIds,
			@PathParam("userId") int userId

			) throws Exception
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("MailNotification.xml");
		Configuration templateConfig = (Configuration) context.getBean("freemarkerMailConfiguration");
		JavaMailSender mailNotification = (JavaMailSender) context.getBean("mailSender");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(userId);

		
		
//		String templateName = command+".ftl";
//		final String strongmomentLink = ContextLoader.getContext().getMessage("Sm.acceptlinkpath", null, "url", null);
//		final String fromEmail =ContextLoader.getContext().getMessage("Sm.email", null, "smadmin@atsgrid.com", null);
	    String templateName = "Confirmation"+".ftl";		
		Map<String,String> map=new HashMap<String,String>();
		map.put("feedBackDateTime", feedBackDateTime);
        map.put("empName",new String(Base64.getDecoder().decode(userDetail.get(0).getFirstName()), "utf-8"));
        map.put("empEmailId",new String(Base64.getDecoder().decode(userDetail.get(0).getEmailId()), "utf-8"));
		map.put("category", category);
		map.put("subject", subject);
		map.put("empDescription", empDescription);
		map.put("imageIcon", "/images/OcwenLogo.png");

		
		StringBuffer message = new StringBuffer();
		try {
			message.append(FreeMarkerTemplateUtils
					.processTemplateIntoString(
							templateConfig.getTemplate(templateName),
							map));
		} catch (IOException e) {

	//		System.out.println(e);
			// handle
		} catch (TemplateException e) {
			// handle
		}
	
		SimpleMailMessage messg = new SimpleMailMessage();
		messg.setFrom("transport@newtglobal.com");
		messg.setTo(toEmployeeFeedBackEmail);		
		messg.setCc(stringArray(employeeFeedBackMailIds));
		messg.setSubject("Report a bug");
		messg.setText(message.toString());
		
		MimeMessage msg = mailNotification.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setFrom("transport@newtglobal.com");
			helper.setTo(messg.getTo());
			helper.setCc(messg.getCc());
			helper.setSubject(messg.getSubject());
			helper.setText(messg.getText(), true);
			
		} catch (Exception e) {
			throw e;
		}
		mailNotification.send(msg);
		
		log.info("Email Sent With feedback ");
	}
	public String[] stringArray(String emailIds){
		String[] strings = emailIds.split(",");
	StringBuffer stringBuffer = new StringBuffer();
	String[] arrayStrings=new String[strings.length];

	
	for (int i = 0; i < strings.length; i++) {
		stringBuffer = new StringBuffer();
//		if (i==0) {
//			stringBuffer.append("{");
//		}
		
		
		stringBuffer.append(strings[i]);
		
		
		
//		if (i!=(strings.length-1)) {
//			stringBuffer.append(",");
//		}
		
//		if (i==(strings.length-1)) {
//			stringBuffer.append("}");
//		}
		arrayStrings[i]=stringBuffer.toString();
	}
	
//	System.out.println("rajannnnn"+arrayStrings);
	
	return arrayStrings;
}
	
}