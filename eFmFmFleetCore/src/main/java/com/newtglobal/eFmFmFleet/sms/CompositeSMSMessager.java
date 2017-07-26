package com.newtglobal.eFmFmFleet.sms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class CompositeSMSMessager implements SMSMessager {

	private static Log log=LogFactory.getLog(CompositeSMSMessager.class);	
	private final List<SMSMessager> gateways;

	public CompositeSMSMessager(List<SMSMessager> gateways) {
		this.gateways = gateways;
	}

	@Override
	public boolean sendMessage(String smsNumber, String text,String requestType) throws Exception {
		for(SMSMessager gateway: gateways) {
			try {
				boolean retStatus = gateway.sendMessage(smsNumber, text,requestType);
				if(retStatus)
					return true;
			} catch (Exception ee) {
            			log.warn("Exception from Gateway: "+ee.getMessage());
			}
		}
		return false;
	}
}
