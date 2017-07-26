package com.newtglobal.eFmFmFleet.services;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.newtglobal.eFmFmFleet.web.ContextLoader;

public class SendMailBySite {
    String webSiteUrl = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic", null);
    
    
    
    public  void feedBackMail(String to) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	   
  	   System.out.println("to"+to);
  	
  	   // Set Subject: header field
  	   message.setSubject("Feedback Email");	
  	   // Now set the actual message  	   
  //	 message.setText("Dear manager\n\nA new vendor "+vendorName+" has been added by "+name+".Please approve the vendor by logging into the transport link below.\n\n"+webSiteUrl+"\n\nThank you\nTransport desk");
  	 message.setText("Dear manager\n\nA New feedback has been created by an ayz employee");
  	   // Send message
  	   Transport.send(message);

  	   System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	

	public  void mailForVendorRegistration(String to,String name,String vendorName) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	   
  	   System.out.println("to"+to);
  	
  	   // Set Subject: header field
  	   message.setSubject("Approval For New Vendor Registraion");	
  	   // Now set the actual message  	   
  	 message.setText("Dear manager\n\nA new vendor "+vendorName+" has been added by "+name+".Please approve the vendor by logging into the transport link below.\n\n"+webSiteUrl+"\n\nThank you\nTransport desk");

  	   // Send message
  	   Transport.send(message);

  	   System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	
	public  void mailForVendorRemoval(String to,String name,String vendorName) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	
  	   // Set Subject: header field
  	   message.setSubject("Vendor Removed From System");	
  	   // Now set the actual message  	   
  	 message.setText("Dear manager\n\nA old vendor "+vendorName+" has been removed by "+name+".\n\n"+webSiteUrl+"\n\nThank you\nTransport desk");

  	   // Send message
  	   Transport.send(message);

  	   System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }

	
	public  void mailForNewDriverRegistration(String to,String name,String driverName) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	
  	   // Set Subject: header field
  	   message.setSubject("Approval For New Driver Registraion");	
  	   // Now set the actual message  	   
  	 message.setText("Dear manager\n\nA new driver "+driverName+" has been added by "+name+".Please approve the driver by logging into the transport link below.\n\n"+webSiteUrl+"\n\nThank you\nTransport desk");

  	   // Send message
  	   Transport.send(message);

  	   System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	
	
	
	
	
	public  void mailForDriverRemoval(String to,String name,String driverName) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	
  	   // Set Subject: header field
  	   message.setSubject("Driver Removed From System");	
  	   // Now set the actual message  	   
  	 message.setText("Dear manager\n\nA old driver "+driverName+" has been removed by "+name+".\n\n"+webSiteUrl+"\n\nThank you\nTransport desk");

  	   // Send message
  	   Transport.send(message);

  	   System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	
	public  void mailForNewEscortRegistration(String to,String name,String escortName) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	
  	   // Set Subject: header field
  	   message.setSubject("New Escort Registraion");	
  	   // Now set the actual message  	   
  	 message.setText("Dear manager\n\nA new driver "+escortName+" has been added by "+name+".\n\n"+webSiteUrl+"\n\nThank you\nTransport desk");

  	   // Send message
  	   Transport.send(message);

  	   System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	public  void mailForEscortRegistration(String to,String name,String escortName) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	   
  	   System.out.println("to"+to);
  	
  	   // Set Subject: header field
  	   message.setSubject("New escort Registration");	
  	   // Now set the actual message  	   
  	 message.setText("Dear manager\n\nA new escort "+escortName+" has been added by "+name+".For logging into the transport link below.\n\n"+webSiteUrl+"\n\nThank you\nTransport desk");

  	   // Send message
  	   Transport.send(message);

  	   System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	public  void mailForManualDistanceApproval(String to,String routeName,int routeId) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	
  	   // Set Subject: header field
  	   message.setSubject("Adding Manual Distance In Trip Sheet");	
  	   // Now set the actual message  	   
  	 message.setText("Dear manager,\n\nYour team updated the Manual Distance for  "+routeName+"  route its  route id is:-"+routeId+" \n\n"+webSiteUrl+"\n\nThank you\nTransport desk");

  	   // Send message
  	   Transport.send(message);

  	   System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	
	public  void resheduleRequestMailTemplate(String to,String oldShiftTime,String tripType,String newShiftTime,String rescheduleDate,String transportMailId) {
        // Sender's email ID needs to be mentioned      
  	  final String username = "transport@newtglobal.com";//change accordingly
  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	
  	   // Set Subject: header field
  	   message.setSubject("Reshedule Request Confirmation");	
  	   // Now set the actual message  	   
  	// message.setText("Dear employee,\n\nYou are request reshedule successfully.Please check it on booking schedule for logging into the transport link below.\n\n"+webSiteUrl+"\n\nThank you\nTransport desk");  	   
  	   message.setText("Dear Employee,\nYour request of re-schedule "+oldShiftTime+" hrs "+tripType+" to "+newShiftTime+" hrs "+tripType+" on "+rescheduleDate+" has been received.\nPlease visit "+webSiteUrl+" to check your booking schedule.\nPlease contact transport team at "+transportMailId+" for any concern.\n\nThanks\nTransport Team");

  	   // Send message
  	   Transport.send(message);

  	   System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	public  void cancelRequestMailTemplate(String to,String shiftTime,String tripType,String requestDate,String transportMailId) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	   
  	   // Now set the actual message  	   
  	   
  	   // Set Subject: header field
	   message.setSubject("Cancel Request Confirmation");	
  	   // Now set the actual message  	   
	
  	   message.setText("Dear Employee,\nYour request of "+shiftTime+" hrs "+tripType+" for "+requestDate+" has been canceled.\nPlease visit "+webSiteUrl+" to check your booking schedule.\nPlease contact transport team at "+transportMailId+" for any concern.\n\nThanks\nTransport Team");
 
  	   // Send message
  	   Transport.send(message);

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	public  void createRequestMailTemplate(String to,String shiftTime,String tripType,String requestStartDate,String requestEndDate,String transportMailId) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	
  	   // Set Subject: header field
	   message.setSubject("Request Creation Confirmation");	
  	   // Now set the actual message  	  
	   message.setText("Dear Employee,\nYour request of "+shiftTime+" hrs "+tripType+" from "+requestStartDate+" to "+requestEndDate+" has been received.\nPlease visit "+webSiteUrl+" to check your booking schedule.\nPlease contact transport team at "+transportMailId+" for any concern.\n\nThanks\nTransport Team");
	   
  	   // Send message
  	   Transport.send(message);

  	   System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	public  void createRequestMailByManager(String to,String shiftTime,String tripType,String 
			requestStartDate,String requestEndDate,String transportMailId ,String requstedBy,String employeeId) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	
  	   // Set Subject: header field
	   message.setSubject("Request Creation Confirmation");	
  	   // Now set the actual message 
	   if(requstedBy.equalsIgnoreCase("admin")){
		   message.setText("Dear TransportAdmin,"
		   				+ "\n Below employees trip request has been created my self ,approval does not required,please maks a schedule "
		   				+ " \n "   
				   		+ " \n EmployeeId     : "+employeeId+" "				   		
				   		+ " \n ShifTime       : "+shiftTime+" hrs "
				   		+ " \n TripType  	  : "+tripType+" "
				   		+ " \n Request From   : "+requestStartDate+"  to "+requestEndDate+" "
				   		+ " \n " 
				   		+ " \n Please visit "+webSiteUrl+" to check your booking schedule.\n"
				   		+ " Please contact transport team at "+ transportMailId+ " for any concern.\n\nThanks\nTransport Team"); 
		   
		   
		   
	   }else{
		   message.setText("Dear Employee,"
	   				+ "\n Your trip request has been created my self ,approval does not required"
	   				+ " \n " 				   		
			   		+ " \n ShifTime   : "+shiftTime+" hrs "
			   		+ " \n TripType   : "+tripType+" "
			   		+ " \n Request From   : "+requestStartDate+"  to "+requestEndDate+" "
			   		+ " \n " 
			   		+ " \n Please visit "+webSiteUrl+" to check your booking schedule.\n"
			   		+ " Please contact transport team at "+ transportMailId+ " for any concern.\n\nThanks\nTransport Team");
		   
	   }	   
	   
  	   // Send message
  	   Transport.send(message);  	 
  	   System.out.println(" createRequestMailByManager- Sent message successfully...."+to);
  	   System.out.println(" Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	
	public  void approvalAndAcknowledement(String to,String shiftTime,String tripType,
			String requestStartDate,String mesgType,String transportMailId,String employeeId,int RequestId,String Remarks,String ProjectId) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	
  	   // Set Subject: header field
	   message.setSubject("Cab Approval Request");	
  	   // Now set the actual message  	
	   if(mesgType.equalsIgnoreCase("AckMail")){
		   message.setText("Dear Employee,"
		   		+ " \n Below request has been approved  "
		   		+ " \n "   
		   		+ " \n EmployeeId : "+employeeId+" "
		   		+ " \n RequestId  : "+RequestId+" "
		   		+ " \n Project Id  	  : "+ProjectId+" "
		   		+ " \n ShifTime   : "+shiftTime+" hrs "
		   		+ " \n TripType   : "+tripType+" "
		   		+ " \n " 
		   		+ " \n Please visit "+webSiteUrl+" to check your booking schedule.\n"
		   		+ " Please contact transport team at "+ transportMailId+ " for any concern.\n\nThanks\nTransport Team");  
	   }else if (mesgType.equalsIgnoreCase("LimitExceeded")){
		   message.setText("Dear Employee,"
			   		+ " \n You could not able to travel with out approval . kinldy check your booking schedule on Webportal / Employee App "
/*			   		+ " \n EmployeeId "+employeeId+" "
			   		+ " \n RequestId "+RequestId+" "
			   		+ " \n ShifTime : "+shiftTime+" hrs "
			   		+ " \n TripType :"+tripType+" "*/		   		
			   		+ " \n Please visit "+webSiteUrl+" to check your booking schedule.\n"
			   		+ " Please contact transport team at "+ transportMailId+ " for any concern.\n\nThanks\nTransport Team"); 
	   }else if(mesgType.equalsIgnoreCase("RejectAckMail")){
		   message.setText("Dear Employee,"
			   		+ " \n Below request has been rejected  "
			   		+ " \n "   
			   		+ " \n Rejected Reason   : "+Remarks+" "
			   		+ " \n EmployeeId 		 : "+employeeId+" "
			   		+ " \n RequestId         : "+RequestId+" "
			   		+ " \n ShifTime          : "+shiftTime+" hrs "
			   		+ " \n TripType          : "+tripType+" "
			   		+ " \n Project Id  	     : "+ProjectId+" "
			   		+ " \n " 
			   		+ " \n Please visit "+webSiteUrl+" to check your booking schedule.\n"
			   		+ " Please contact transport team at "+ transportMailId+ " for any concern.\n\nThanks\nTransport Team");  
		   }else if (mesgType.equalsIgnoreCase("RejectConfirmationMail")){   
		   message.setText("Dear TransportAdmin,"
				   + " \n Below Employee request has been rejected & kindly cancle the schedule."
			   		+ " \n "  
			   		+ " \n Rejected Reason   : "+Remarks+" "
			   		+ " \n EmployeeId 		 : "+employeeId+" "
			   		+ " \n RequestId         : "+RequestId+" "
			   		+ " \n ShifTime          : "+shiftTime+" hrs "
			   		+ " \n TripType          : "+tripType+" "
			   		+ " \n Project Id  	     : "+ProjectId+" "
			   		+ " \n " 
			   		+ " \n Please visit "+webSiteUrl+" to check your booking schedule.\n"
			   		+ " Please contact transport team at "+ transportMailId+ " for any concern.\n\nThanks\nTransport Team");		   		   
		   
	   }else{	   
		   message.setText("Dear TransportAdmin,"
				   + " \n Below Employee request has been approved & kindly make the schedule."
			   		+ " \n "   
			   		+ " \n EmployeeId : "+employeeId+" "
			   		+ " \n RequestId  : "+RequestId+" "
			   		+ " \n ShifTime   : "+shiftTime+" hrs "
			   		+ " \n TripType   : "+tripType+" "
			   		+ " \n Project Id : "+ProjectId+" "
			   		+ " \n " 
			   		+ " \n Please visit "+webSiteUrl+" to check your booking schedule.\n"
			   		+ " Please contact transport team at "+ transportMailId+ " for any concern.\n\nThanks\nTransport Team");		   		   
		   
	   }	   
  	   // Send message
  	   Transport.send(message);
  	   System.out.println(" approvalAndAcknowledement- Sent message successfully...."+to);
  	   System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	
	public  void approvalMailTemplate(String to,String shiftTime,String tripType,String requestStartDate,String requestEndDate,String transportMailId,String employeeId,String ProjectId) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	
  	   // Set Subject: header field
	   message.setSubject("Cab Approval Request");	
  	   // Now set the actual message  	  
	   
	   message.setText("Dear Employee,"
  				+ "\n Kinldy approve below employee cab request"
  				+ " \n "   
		   		+ " \n EmployeeId : "+employeeId+" "				   		
		   		+ " \n ShifTime   : "+shiftTime+" hrs "
		   		+ " \n TripType   : "+tripType+" "
		   		+ " \n Project Id : "+ProjectId+" "
		   		+ " \n Request From   : "+requestStartDate+"  to "+requestEndDate+" "
		   		+ " \n " 
		   		+ " \n Please visit "+webSiteUrl+" to check your booking schedule.\n"
		   		+ " Please contact transport team at "+ transportMailId+ " for any concern.\n\nThanks\nTransport Team"); 
  
  	   // Send message
  	   Transport.send(message);
  	   System.out.println("ApprovalMailTemplate-Sent message successfully...." +to);
  	   System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	
	
	public  void manilaAdhocrequestAcceptance(String to,String shiftTime,String tripType,String requestStartDate,String transportMailId) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	
  	   // Set Subject: header field
	   message.setSubject("Request Acceptance Confirmation");	
  	   // Now set the actual message  	  
	   message.setText("Dear Employee,\n\nYour request of "+shiftTime+" hrs "+tripType+" for  "+requestStartDate+" has been received.\nPlease visit "+webSiteUrl+" to check your booking schedule.\n\nPlease contact transport team at "+transportMailId+" for any concern.\n\nWarm Regards\nTransport Control Room");
	   
  	   // Send message
  	   Transport.send(message);
  	   System.out.println("Sent message successfully....");
        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
	
	public  void manilaAdhocrequestRejectMail(String to,String transportMailId) {
        // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
  	  // Assuming you are sending email through relay.jangosmtp.net
        String host="smtp.bizmail.yahoo.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
    //    props.put("mail.smtp.port", "25");
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
  	   }
           });

        try {
  	   // Create a default MimeMessage object.
  	   Message message = new MimeMessage(session);
  	
  	   // Set From: header field of the header.
  	   message.setFrom(new InternetAddress(username));
  	
  	   // Set To: header field of the header.
  	   message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(to));
  	
  	   // Set Subject: header field
	   message.setSubject("Request Rejection Confirmation");	
	   
	   
	   
  	   // Now set the actual message  	  
	   message.setText("Dear Employee,\n\nWe do apologize for not accommodating your shuttle service request. We are fully booked for adhoc request. Reservations is on a First Come, First Serve basis.\n\nThank you very much for your cooperation.\n\nPlease contact transport team at "+transportMailId+" for any concern.\n\nBest Regards,\nTransport Control Room");
	   
  	   // Send message
  	   Transport.send(message);
  	   System.out.println("Sent message successfully....");
        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
     }
		
	public  void ServionMailServer(String to,String subject,String mailCondent) {
	      // Sender's email ID needs to be mentioned      
		  /*String from = "fmfm@newtglobal.com";
		  final String username = "fmfm@newtglobal.com";//change accordingly
		  final String password = "8sjfcgEM";//change accordingly*/      
		  String from ="transportdesk@servion.com";
		  final String username = "transportdesk@servion.com";//change accordingly
		  final String password = "test@123";//change accordingly
		  // Assuming you are sending email through relay.jangosmtp.net
	      //String host="smtp.bizmail.yahoo.com";
	      String host="smtp.office365.com";
	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true"); 
	      props.put("mail.smtp.host", host);
	      //props.put("mail.smtp.port", "25");
	      props.put("mail.smtp.port", "587");
	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
		   }
	         });

	      try {
		   // Create a default MimeMessage object.
		   Message message = new MimeMessage(session);
		   message.setFrom(new InternetAddress(from));
		   message.setRecipients(Message.RecipientType.TO,
	               InternetAddress.parse(to));
		   message.setSubject(subject);	   
		   message.setText(mailCondent);
		   Transport.send(message);
		   System.out.println("Sent message successfully....");
	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      }
	   }
	
	
	public  void newtGlobalEmailPortal(String to,String subject,String mailCondent) {
      // Sender's email ID needs to be mentioned      
	  	  final String username = "transport@newtglobal.com";//change accordingly
	  	  final String password = "efmfm@110";//change accordingly     	 
	  // Assuming you are sending email through relay.jangosmtp.net
      String host="smtp.bizmail.yahoo.com";
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true"); 
      props.put("mail.smtp.host", host);
  //    props.put("mail.smtp.port", "25");
      props.put("mail.smtp.port", "587");
      // Get the Session object.
      Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
	   }
         });

      try {
	   // Create a default MimeMessage object.
	   Message message = new MimeMessage(session);
	   message.setFrom(new InternetAddress(username));
	   message.setRecipients(Message.RecipientType.TO,
               InternetAddress.parse(to));
 	   message.setSubject(subject);  	   
	   message.setText(mailCondent);
	   Transport.send(message);
	   System.out.println("Sent message successfully....");
      } catch (MessagingException e) {
         throw new RuntimeException(e);
      }
   }
	
}
