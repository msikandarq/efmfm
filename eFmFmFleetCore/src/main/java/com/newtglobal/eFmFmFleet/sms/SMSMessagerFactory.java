package com.newtglobal.eFmFmFleet.sms;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SMSMessagerFactory {

	public static final String SMS_PROPERTIES_FILE = "sms.properties";
	private static final Log log=LogFactory.getLog(SMSMessagerFactory.class);	

	private static volatile SMSMessager messager;
	private static volatile SMSMessager guestMessager;
	
	static {
		Properties prop = new Properties();
		try {
			InputStream isr = SMSMessagerFactory.class.getClassLoader().getResourceAsStream(SMS_PROPERTIES_FILE);
			prop.load(isr);
			isr.close();
		} catch(Exception e) {
			log.warn("SMS Properties: "+SMS_PROPERTIES_FILE+" not found. Using Default Configuration.");
		}
		final String messagerClass = prop.getProperty("messager", "com.newtglobal.eFmFmFleet.sms.ValueFirstSMSMessager");
		final String secondaryMessagerClass =prop.getProperty("secondaryMessager", "com.newtglobal.eFmFmFleet.sms.SMS99SMSMessager");

		log.info("Messager: "+messagerClass);
		log.info("Secondary Messager: "+secondaryMessagerClass);
		try { 
			SMSMessager messagerTemp = (SMSMessager) Class.forName(messagerClass).newInstance();
			SMSMessager secondaryMessagerTemp = (SMSMessager) Class.forName(secondaryMessagerClass).newInstance();
			List<SMSMessager> failovers = new ArrayList<SMSMessager>();
			failovers.add(messagerTemp);
			failovers.add(secondaryMessagerTemp);
			SMSMessager failoverMessagerTemp = new CompositeSMSMessager(failovers);
			setMessager(failoverMessagerTemp);
			setGuestMessager(failoverMessagerTemp);
		} catch(Exception e) {
			log.error("Unable to use SMS Properties. Error Was: "+e.getMessage());
		}
	}

	public static void setMessager(SMSMessager impl) {
		if(impl != null)
			messager = impl;
	}
	
	public static void setGuestMessager(SMSMessager guestImpl) {
		if(guestImpl != null)
			guestMessager = guestImpl;
	}
	
	public static SMSMessager getMessager() {
		return messager;
	}
	
	public static SMSMessager getGuestMessager() {
		return guestMessager;
	}

}
