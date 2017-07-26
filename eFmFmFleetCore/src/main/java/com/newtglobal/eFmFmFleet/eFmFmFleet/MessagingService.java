package com.newtglobal.eFmFmFleet.eFmFmFleet;

import com.newtglobal.eFmFmFleet.sms.SMSMessagerFactory;

public class MessagingService {

    public void etaMessage(String empName, String vehicleNum, String eta,
            String mobileNum, String requestType) throws Exception {
        String text = "Your ride "
                + vehicleNum
                + " will be at your pickup point in the next "
                + eta
                + ".\nFor feedback write to us @SSSCCH-Transport-Team@shell.com";
        SMSMessagerFactory.getMessager().sendMessage(mobileNum, text,
                requestType);
    }

    public void cabReachedMessage(String mobileNumber, String text,
            String requestType) throws Exception {
        SMSMessagerFactory.getMessager().sendMessage(mobileNumber, text,
                requestType);
    }

    public void sendTripAsMessage(String mobileNumber, String text,
            String requestType) throws Exception {
        SMSMessagerFactory.getMessager().sendMessage(mobileNumber, text,
                requestType);
    }

    public void etaMessageWhenCabDelay(String empName, String vehicleNum,
            String eta, String mobileNumber, String requestType)
            throws Exception {
        String text = "Your ride "
                + vehicleNum
                + " is running behind schedule and has been delayed.\nRegret the inconvenience caused if any.\nFor feedback write to us @SSSCCH-Transport-Team@shell.com";
        SMSMessagerFactory.getMessager().sendMessage(mobileNumber, text,
                requestType);
    }

    public void cabHasLeftMessageForSch(String mobileNumber, String text,
            String requestType) throws Exception {
        SMSMessagerFactory.getMessager().sendMessage(mobileNumber, text,
                requestType);
    }

    public void cabHasLeftMessageForGuestFromSch(String mobileNumber,
            String text, String requestType) throws Exception {
        SMSMessagerFactory.getMessager().sendMessage(mobileNumber, text,
                requestType);
    }

    public void etaMessagesForGuest(String empName, String vehicleNum,
            String eta, String mobileNumber, String requestType)
            throws Exception {
        String text = "Your ride "
                + vehicleNum
                + " will be at your pickup point in the next "
                + eta
                + ".\nFor feedback write to us @SSSCCH-Transport-Team@shell.com";

        SMSMessagerFactory.getGuestMessager().sendMessage(mobileNumber, text,
                requestType);
    }

    public void cabReachedMessageForGuest(String mobileNumber, String text,
            String requestType) throws Exception {
        SMSMessagerFactory.getGuestMessager().sendMessage(mobileNumber, text,
                requestType);
    }

    public void sendMessageToGuest(String mobileNumber, String text,
            String requestType) throws Exception {
        SMSMessagerFactory.getGuestMessager().sendMessage(mobileNumber, text,
                requestType);
    }

    public void etaMessagesForGuestWhenCabDelay(String empName,
            String vehicleNum, String eta, String mobileNumber,
            String requestType) throws Exception {
        String text = "Your ride "
                + vehicleNum
                + " is running behind schedule and has been delayed.\nRegret the inconvenience caused if any.\nFor feedback write to us @SSSCCH-Transport-Team@shell.com";
        SMSMessagerFactory.getGuestMessager().sendMessage(mobileNumber, text,
                requestType);
    }
    
    public void adminMessage(String empName, String message,String mobileNum) throws Exception {
        String text = "Hello "
                + empName
                +""
                + message;
        SMSMessagerFactory.getMessager().sendMessage(mobileNum, text,"" );
    }
}
