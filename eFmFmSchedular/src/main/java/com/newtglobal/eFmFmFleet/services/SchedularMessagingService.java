package com.newtglobal.eFmFmFleet.services;

import com.newtglobal.eFmFmFleet.sms.SMSMessagerFactory;

public class SchedularMessagingService {

	public void etaMessageFromSchedular(String vehicleNum, String eta,
			String mobileNumber,String requestType) throws Exception {
		String text = "Dear employee your cab "
				+ vehicleNum + ",will be at your pickup point in next " + eta;
		SMSMessagerFactory.getMessager().sendMessage(mobileNumber, text,requestType);
	}

	public void cabReachedMessageForSch(String mobileNumber, String text,String requestType)
			throws Exception {
		SMSMessagerFactory.getMessager().sendMessage(mobileNumber, text,requestType);
	}

	public void etaMessageWhenCabDelayFromSch(String empName,
			String vehicleNum, String mobileNumber,String requestType) throws Exception {
		String text = "Dear employee your cab "
				+ vehicleNum + ", delayed will update you";
		SMSMessagerFactory.getMessager().sendMessage(mobileNumber, text,requestType);
	}

	public void cabHasLeftMessageForSch(String mobileNumber, String text,String requestType)
			throws Exception {
		SMSMessagerFactory.getMessager().sendMessage(mobileNumber, text,requestType);
	}

	public void cabHasLeftMessageForGuestFromSch(String mobileNumber,
			String text,String requestType) throws Exception {
		SMSMessagerFactory.getGuestMessager().sendMessage(mobileNumber, text,requestType);
	}

	public void etaMessagesForGuestFromSchedular(String vehicleNum, String eta,
			String mobileNumber,String requestType) throws Exception {
		String text = "Dear guest your cab "
				+ vehicleNum + ",will be at your pickup point in next " + eta;
		SMSMessagerFactory.getGuestMessager().sendMessage(mobileNumber, text,requestType);
	}

	public void cabReachedMessageForGuestFromSch(String mobileNumber,
			String text,String requestType) throws Exception {
		SMSMessagerFactory.getGuestMessager().sendMessage(mobileNumber, text,requestType);
	}

	public void etaMessagesForGuestWhenCabDelayFromSch(String empName,
			String vehicleNum, String mobileNumber,String requestType) throws Exception {
		String text = "Dear guest your cab "
				+ vehicleNum + ",delayed will update you";
		SMSMessagerFactory.getGuestMessager().sendMessage(mobileNumber, text,requestType);
	}

}