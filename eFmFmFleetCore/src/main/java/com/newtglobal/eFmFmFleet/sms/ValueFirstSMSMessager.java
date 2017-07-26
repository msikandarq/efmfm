package com.newtglobal.eFmFmFleet.sms;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class ValueFirstSMSMessager implements SMSMessager {

    public static final String SMS_PROPERTIES_FILE = "sms.properties";
    private static Log log = LogFactory.getLog(ValueFirstSMSMessager.class);

    @Override
    public boolean sendMessage(String smsNumber, String text, String requestType) throws Exception {
        Properties prop = new Properties();
        try {
            InputStream isr = SMSMessagerFactory.class.getClassLoader().getResourceAsStream(SMS_PROPERTIES_FILE);
            prop.load(isr);
            isr.close();
        } catch (Exception e) {
            log.warn("SMS Properties: " + SMS_PROPERTIES_FILE + " not found. Using Default Configuration.");
        }
        final String myvaluefirstIndia = prop.getProperty("myvaluefirstIndia",
                "com.newtglobal.eFmFmFleet.sms.ValueFirstSMSMessager");
        final String USER_AGENT = "Mozilla/5.0";
        String GET_URL = "";
        text = URLEncoder.encode(text, "UTF-8");
        String senderId = prop.getProperty("valuefirstsenderid", "com.newtglobal.eFmFmFleet.sms.ValueFirstSMSMessager");
      // GET_URL = myvaluefirstIndia + smsNumber + "&from=" + senderId + "&text=" + text + "&dlr-mask=19&dlr-url";     
        GET_URL ="http://api-alerts.solutionsinfini.com/v3/?method=sms&api_key=A008658e97a053fb4ce8139be1c565b37&methord=json&to="+smsNumber+"&sender=NGFVPT&message="+text;
        log.info("Msg from National API" + GET_URL);
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            /*
             * if(response.toString().contains("Service not available")){
             * log.warn(
             * "Message Responce From Gateway GET request not worked for Mobile: "
             * +smsNumber+" : Service not available Message Text"+text); return
             * false; }
             */
            log.info("Message ValueFirstSMSMessager Response From GateWay: " + response.toString() + " for Mobile: "
                    + smsNumber + " : " + responseCode + " Message Text" + text);
        } else {
            log.warn("Message ValueFirstSMSMessager Responce From Gateway GET request not worked for Mobile: "
                    + smsNumber + " : " + responseCode + " Message Text" + text);
            return false;
        }
        return true;
    }

}
