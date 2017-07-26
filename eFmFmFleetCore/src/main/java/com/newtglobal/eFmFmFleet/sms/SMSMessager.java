package com.newtglobal.eFmFmFleet.sms;

public interface SMSMessager {
	public boolean sendMessage(final String smsNumber, final String text,final String requestType) throws Exception;
}
