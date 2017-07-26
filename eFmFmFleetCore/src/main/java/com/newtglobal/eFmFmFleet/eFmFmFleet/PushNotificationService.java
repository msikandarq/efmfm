package com.newtglobal.eFmFmFleet.eFmFmFleet;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class PushNotificationService {

	private static Log log = LogFactory.getLog(PushNotificationService.class);

	public void notification(String deviceToken,String userMessage ){
	final String GOOGLE_SERVER_KEY = "AIzaSyAapRgZnLGWytYGVbS29WUPYm0wXggb53c";	
	 final String MESSAGE_KEY = "message";
		Result result = null;
	// GCM RedgId of Android device to send push notification
	try {
			Sender sender = new Sender(GOOGLE_SERVER_KEY);
			Message message = new Message.Builder().timeToLive(30)
					.delayWhileIdle(true).addData(MESSAGE_KEY, userMessage).build();
			result = sender.send(message, deviceToken, 1);
			log.info("Android Push Send to "+new Date()+" push status"+result);
		} catch (Exception ioe) {
		log.info("Android Push Exception Block"+ioe);
		}
}
	
	
	public  void iosPushNotification(String token,String msg){
		try{
//		ApnsService service = APNS
//				.newService()
//			.withCert("/home/newtglobal/Dev/eFmFmPushNotification.p12",
//						"newt@123").withSandboxDestination().build();
		    
		    ApnsService service = APNS
	                .newService()
	                .withCert("/home/ubuntu/logs/eFmFmPushNotification.p12",
	                        "newt@123").withSandboxDestination().build();
		String payload = APNS.newPayload().alertBody(msg).badge(1).sound("default").build();
		service.push(token, payload);
		log.info("IOS Push Send to "+new Date()+" push status"+service.toString());
		}catch(Exception e){
			log.info("IOS Push Exception Block"+e);	
		}
	}
	
}