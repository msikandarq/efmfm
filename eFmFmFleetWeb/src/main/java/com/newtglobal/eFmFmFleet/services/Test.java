package com.newtglobal.eFmFmFleet.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.newtglobal.eFmFmFleet.eFmFmFleet.CalculateDistance;


public class Test {
	static byte[] key;
	static String keyString ="iI1tzzodgnjqhMQq6zIyPJ9iy40=";
	static String clientId ="gme-newtglobalindiaprivate";

	public static void main(String[] args) throws ParseException, IOException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {

		
        List<Integer> clientDetail = new ArrayList<Integer>();	        

        clientDetail.add(1);
        clientDetail.add(2);
        clientDetail.add(3);

        System.out.println("clientDetailclientDetailclientDetail"+clientDetail);
        
        
      
        if(clientDetail.contains(1)){
        	System.out.println("llllllclientDetailclientDetailclientDetaill");
        }
        if(clientDetail.contains(3)){
        	System.out.println("333333333333");
        }
        if(clientDetail.contains(5)){
        	System.out.println("5555555555");
        }
		
		
		
		
		
	    String userFacilities = "3,1,";
String k=userFacilities.substring(0, userFacilities.length()-1);
String l=k.substring(1);

	//	System.out.println(k);
	//	System.out.println(l);

		
		
 //test	second change
		 String facilityIds="";
		facilityIds=facilityIds+1+",";
//System.out.println(facilityIds.substring(0, facilityIds.indexOf(",")));

		minusDayFromDate(new Date(), 2);
		
		DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String travelTimeFromDevice = "18-05-2017 23:17:17";

		Date travelTime = dateTimeFormate.parse(travelTimeFromDevice);
//		System.out.println(travelTime);
		
		
		
		
        final String USER_AGENT = "Mozilla/5.0";

        
 //       String GET_URL ="https://voice.solutionsinfini.com/api/v1/index.php?api_key=Ade04b869411f0f6f1755ad0012a5d5f1&method=dial.click2call&format=xml&receiver=9811172193&caller=9211516152";        

//        String GET_URL ="http://www.myvaluefirst.com/smpp/sendsms?username=shellindiahttp&password=shell1234&to=919811172193&from=SBOTVI&text=texting&dlr-mask=19&dlr-url";
//        
//        //String GET_URL ="http://api-alerts.solutionsinfini.com/v3/?method=sms&api_key=A008658e97a053fb4ce8139be1c565b37&methord=json&to="+routeIds.nextElement()+"&sender=NGFVPT&message="+text;
//        URL obj = new URL(GET_URL);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//        con.setRequestMethod("GET");
//        con.setRequestProperty("User-Agent", USER_AGENT);
//        int responseCode = con.getResponseCode();
//        StringBuffer response = new StringBuffer();
//        if (responseCode == HttpURLConnection.HTTP_OK) { // success
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            System.out.println("Message solutionsinfini Response From GateWay: " + response.toString() + " for Mobile: "
//                    + response.toString()+"responseCode"+responseCode);
//        } else {
//        	System.out.println(response.toString()+"responseCode"+responseCode);
//        }
//	}
		
		
		
		
		
		
		
		
		
		
		
		
		
		String string = "abc@gmail.com,abc@gmail.com,abc@gmail.com,abc@gmail.com";
		String[] strings = string.split(",");
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < strings.length; i++) {
		
			if (i==0) {
				stringBuffer.append("{");
			}
			
			stringBuffer.append('"').append(strings[i]).append('"');
			
			if (i!=(strings.length-1)) {
				stringBuffer.append(",");
			}
			
			if (i==(strings.length-1)) {
				stringBuffer.append("}");
			}
		}
	//	System.out.print(stringBuffer.toString());

		
		
//	    String templateName = command+".ftl";
//		Map<String,String> map=new HashMap<String,String>();	
//		URL url = null; 
//		  try{
//	//		   url = new URL(confirmLink+URLEncoder.encode(confirmCodeName, "UTF-8")+"&pc="+URLEncoder.encode(profileConversion, "UTF-8"));		
//		  }catch(MalformedURLException mue){
//		   System.err.println(mue); 
//		  }
//		map.put("url", url.toString());
//		map.put("confirmLink", "abc");
//		StringBuffer message = new StringBuffer();
//		try {
//			message.append(FreeMarkerTemplateUtils
//					.processTemplateIntoString(
//							templateConfig.getTemplate(templateName),
//							map));
//		} catch (IOException e) {
//
//			// handle
//		} catch (TemplateException e) {
//			// handle
//		}

    	CalculateDistance cal=new CalculateDistance();
 //   	System.out.println(cal.googlePlannedDistanceCalculation("-33.8670,151.1957", "-33.8670,151.1957"));
	//	System.out.println(cal.getGooglePlaceAPI());
		
		String urlLocation = "";
		int distance=0;
		URL geocodeURL;
	//	urlLocation ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670,151.1957&radius=500&types=food&name=cruise&key="+clientId;
		
	//	urlLocation ="https://roads.googleapis.com/v1/snapToRoads?path=-35.27801,149.12958|-35.28032,149.12907|-35.28099,149.12929|-35.28144,149.12984|-35.28194,149.13003|-35.28282,149.12956|-35.28302,149.12881|-35.28473,149.12836&interpolate=true&key="+clientId;
		
		
	//urlLocation ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&type=hospital&client="+clientId;
////	urlLocation = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+origin+"&destinations="+destination+"&mode=driving&units=metric&sensor=true&client="+clientId;
//	    URL url = new URL(urlLocation);
//	    CalculateDistance signer = new CalculateDistance();
//	    signer.passingKey(keyString);
//	    String request = signer.signRequest(url.getPath(),url.getQuery());
//	    urlLocation=url.getProtocol() + "://" + url.getHost() + request;
//			geocodeURL = new URL(urlLocation);
//			System.out.println(urlLocation);
//			URLConnection connection = geocodeURL.openConnection();
//			connection.connect();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//			StringBuffer data = new StringBuffer();
//			String line = "";
//			String availDistance="";
//			while((line = reader.readLine()) != null){
//				data.append(line.trim());
//			}		
//			JSONObject status = new JSONObject(data.toString());
//			System.out.println(status.toString());
//			String objstatus=status.getString("status");		
//			    if(objstatus.equals("OK")){
//					JSONArray rows = status.getJSONArray("rows");
//	     }	
//		
	
		
		
		
		
		
		
		
		
		
//		String abc="919990800488,919811319397,919810032583,918800262304,919953611434,919643809690,919810552541,917838899986,919990243667,919810036882,918527326711,919811783960,919999330353,919999335790,918860501380,919811615542,919811687638,918800865655,919910125489,918447009669,919711886764,919975986905,919711919113,919899782423,919643891229,919811596525,919971518658,919891196289,919818636518,919711282538,918588807181,919711888829,919999978322,919899199795,919711024442,917289929057,919999465196,919711144017,918800894734,917838711402,919873691815,919990211146,919810660157,919953767100,919582263673,917838381189,919560387488,919999084159,919650559134,919711886764,919811940486,919916395937,918971362004,919716375022,917503750189,919901346132,917838898694,919811172193";
//		
//		String abc1="919811172193,919811531751";
//		
//		
////		
////	String abc="36885,36886,36887";
//        final String USER_AGENT = "Mozilla/5.0";
//        String text="Dear Employee,Please download the eFmFm AIG transport application.Enter the Transport code as:-AIGTPG-,With registered mobile Number.";
//		StringTokenizer routeIds = new StringTokenizer(abc1, ",");
//		while (routeIds.hasMoreElements()) {
//	//	System.out.println(routeIds.nextElement());
//			
//			
//			
//			String GET_URL ="http://api-alerts.solutionsinfini.com/v3/?method=sms&api_key=A008658e97a053fb4ce8139be1c565b37&methord=json&to="+routeIds.nextElement()+"&sender=NGFVPT&message="+text;
//	        URL obj = new URL(GET_URL);
//	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//	        con.setRequestMethod("GET");
//	        con.setRequestProperty("User-Agent", USER_AGENT);
//	        int responseCode = con.getResponseCode();
//	        if (responseCode == HttpURLConnection.HTTP_OK) { // success
//	            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//	            String inputLine;
//	            StringBuffer response = new StringBuffer();
//	            while ((inputLine = in.readLine()) != null) {
//	                response.append(inputLine);
//	            }
//	            in.close();
//	            System.out.println("Message ValueFirstSMSMessager Response From GateWay: " + response.toString() + " for Mobile: "
//	                    + response.toString()+"responseCode"+responseCode);
//	        } else {
//	        	//System.out.println(response.toString()+"responseCode"+responseCode);
//	        }
//		}
//		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		int sec1=60;
		int minute1 =sec1/60;
	//	System.out.println(minute1);
				
		Date assignDate1=dateFormat.parse("24-01-2017 23:40:00");
		
//		System.out.println("milli"+assignDate1.getTime());

		String routeAssignDate=dateFormat.format(new Date());
//		System.out.println(routeAssignDate);
		Date assignDate=dateFormat.parse(routeAssignDate);
//		System.out.println(assignDate);
		long milliSecDateTime=getDisableTime(1, 20, assignDate)+7200000;
 //       System.out.println(new Date(milliSecDateTime));
		long estmatedwaitingTime=7200000;
//		long millis=assignroute.getShiftTime().getTime()+assignroute.getPlannedTime()+estmatedwaitingTime;
		long minute = (estmatedwaitingTime / (1000 * 60)) % 60;
		long hour = (estmatedwaitingTime / (1000 * 60 * 60)) % 24;	
		
		
	}
	
	
	public static long minusDayFromDate(Date date, int numDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, numDays);
		return calendar.getTimeInMillis();
	}
	
	public static long getDisableTime(int hours, int minutes, Date checkIndate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkIndate);
		calendar.add(Calendar.HOUR, hours);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTimeInMillis();
	}
	 public void passingKey(String keyString) throws IOException {
	      // Convert the key from 'web safe' base 64 to binary
	      keyString = keyString.replace('-', '+');
	      keyString = keyString.replace('_', '/');
	      Test.key = Base64.getDecoder().decode(keyString);  
	  }

	  public String signRequest(String path, String query)
	          throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, URISyntaxException {
	      // Retrieve the proper URL components to sign
	      String resource = path + '?' + query;
	      // Get an HMAC-SHA1 signing key from the raw key bytes
	      SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
	      // Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1
	      Mac mac = Mac.getInstance("HmacSHA1");
	      mac.init(sha1Key);
	      // compute the binary signature for the request
	      byte[] sigBytes = mac.doFinal(resource.getBytes());
	      // base 64 encode the binary signature
//	       String signature = Base64.encode(sigBytes);
	      String signature = Base64.getEncoder().encodeToString(sigBytes);
	      // convert the signature to 'web safe' base 64
	      signature = signature.replace('+', '-');
	      signature = signature.replace('/', '_');
	      return resource + "&signature=" + signature;
	  }
}
