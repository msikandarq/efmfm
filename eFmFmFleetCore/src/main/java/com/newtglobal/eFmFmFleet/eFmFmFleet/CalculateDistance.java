package com.newtglobal.eFmFmFleet.eFmFmFleet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONArray;
import org.json.JSONObject;

public class CalculateDistance {
	private static  byte[] key;
	private static String keyString ="iI1tzzodgnjqhMQq6zIyPJ9iy40=";
	String clientId ="gme-newtglobalindiaprivate";
	
	public String googlePlannedDistanceCalculation(String origin,String destination) throws IOException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException{
		String urlLocation = "";
		int distance=0;
		URL geocodeURL;
		urlLocation = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+origin+"&destinations="+destination+"&mode=driving&units=metric&sensor=true&client="+clientId;
	    URL url = new URL(urlLocation);
	    CalculateDistance signer = new CalculateDistance();
	    signer.passingKey(keyString);
	    String request = signer.signRequest(url.getPath(),url.getQuery());
	    urlLocation=url.getProtocol() + "://" + url.getHost() + request;
			geocodeURL = new URL(urlLocation);
			URLConnection connection = geocodeURL.openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer data = new StringBuffer();
			String line = "";
			String availDistance="";
			while((line = reader.readLine()) != null){
				data.append(line.trim());
			}		
			JSONObject status = new JSONObject(data.toString());
			String objstatus=status.getString("status");		
			    if(objstatus.equals("OK")){
					JSONArray rows = status.getJSONArray("rows");
					for(int j=0;j<rows.length()-1;j++){
						JSONArray elements = rows.getJSONObject(j).getJSONArray("elements");	
			    	for(int i=0; i<elements.length();){			    		
			    		distance+=elements.getJSONObject(j).getJSONObject("distance").getInt("value");
						break;
						}
					}
					float input = (float) (distance * .001);
					DecimalFormat decimalFormat = new DecimalFormat("0.#");
					availDistance=decimalFormat.format(input);
	     }	
		
		return availDistance;
	}
	
	public String getGooglePlaceAPI() throws IOException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException{
		String urlLocation = "";
		int distance=0;
		URL geocodeURL;
//		urlLocation ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670,151.1957&radius=500&types=food&name=cruise&client="+clientId;
		urlLocation ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&key="+clientId;
//		urlLocation ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=38.26790166666667,-0.7052183333333333&radius=5000&types=restaurant|health|city_hall|gas_station|shopping_mall|grocery_or_supermarket&sensor=false&client="+clientId;
		//	urlLocation ="https://roads.googleapis.com/v1/snapToRoads?path=-35.27801,149.12958|-35.28032,149.12907|-35.28099,149.12929|-35.28144,149.12984|-35.28194,149.13003|-35.28282,149.12956|-35.28302,149.12881|-35.28473,149.12836&interpolate=true&key="+clientId;

		
//		urlLocation = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+origin+"&destinations="+destination+"&mode=driving&units=metric&sensor=true&client="+clientId;
	    URL url = new URL(urlLocation);
	    CalculateDistance signer = new CalculateDistance();
	    signer.passingKey(keyString);
	    String request = signer.signRequest(url.getPath(),url.getQuery());
	    urlLocation=url.getProtocol() + "://" + url.getHost() + request;
			geocodeURL = new URL(urlLocation);
			URLConnection connection = geocodeURL.openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer data = new StringBuffer();
			String line = "";
			String availDistance="";
			while((line = reader.readLine()) != null){
				data.append(line.trim());
			}		
			JSONObject status = new JSONObject(data.toString());
			System.out.println(data.toString());
			String objstatus=status.getString("status");		
			    if(objstatus.equals("OK")){
					JSONArray rows = status.getJSONArray("rows");
					for(int j=0;j<rows.length()-1;j++){
						JSONArray elements = rows.getJSONObject(j).getJSONArray("elements");	
			    	for(int i=0; i<elements.length();){			    		
			    		distance+=elements.getJSONObject(j).getJSONObject("distance").getInt("value");
						break;
						}
					}
					float input = (float) (distance * .001);
					DecimalFormat decimalFormat = new DecimalFormat("0.#");
					availDistance=decimalFormat.format(input);
	     }	
		
		return availDistance;
	}
	
	
	public double employeeDistanceCalculation(String origin,String destination) throws IOException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException{
		String urlLocation = "";
		double distance=0;
		URL geocodeURL;
		urlLocation = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+origin+"&destinations="+destination+"&mode=driving&units=metric&sensor=true&client="+clientId;
	    URL url = new URL(urlLocation);
	    CalculateDistance signer = new CalculateDistance();
	    signer.passingKey(keyString);
	    String request = signer.signRequest(url.getPath(),url.getQuery());
	    urlLocation=url.getProtocol() + "://" + url.getHost() + request;
			geocodeURL = new URL(urlLocation);
			URLConnection connection = geocodeURL.openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer data = new StringBuffer();
			String line = "";
			double empDistance = 0;
			while((line = reader.readLine()) != null){
				data.append(line.trim());
			}		
			JSONObject status = new JSONObject(data.toString());
			String objstatus=status.getString("status");		
			    if(objstatus.equals("OK")){
					JSONArray rows = status.getJSONArray("rows");
					JSONArray elements = rows.getJSONObject(0).getJSONArray("elements");
					distance= elements.getJSONObject(0).getJSONObject("distance").getInt("value");
					empDistance = distance * .001;
	     }	
		System.out.println(distance);
		return distance;
	}
	
	public  String getPlannedDistanceByMapApi(String origin,String destination,String allwayPoints){
		String urlLocation = "";
		URL geocodeURL;
		int totalDistance=0;
		String availDistance = "";
		try{
		urlLocation = "http://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination="+destination+"&waypoints=optimize:false|"+allwayPoints+"&mode=driving&language=en-EN&sensor=false&client="+clientId;				
		URL url = new URL(urlLocation);
	    CalculateDistance signer = new CalculateDistance();
		signer.passingKey(keyString);
		String request = signer.signRequest(url.getPath(),url.getQuery());
		urlLocation=url.getProtocol() + "://" + url.getHost() + request;			
		geocodeURL = new URL(urlLocation);
		URLConnection connection = geocodeURL.openConnection();
		connection.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String data = "";
		String line = "";
		JSONArray cabLocation =null;
		while((line = reader.readLine()) != null){
			data += line.trim();
		}
		JSONObject status = new JSONObject(data);
		String objstatus=status.getString("status");
		if(objstatus.equals("OK")){
			cabLocation = new JSONObject(data).getJSONArray("routes");	
			JSONArray elements = cabLocation.getJSONObject(0).getJSONArray("legs");    	
			for (int i=0; i<=elements.length()-1; i++){												
					totalDistance+= elements.getJSONObject(i).getJSONObject("distance").getInt("value");
				}
			float input = (float) (totalDistance * .001);
			DecimalFormat decimalFormat = new DecimalFormat("0.#");
			availDistance=decimalFormat.format(input);

			}
		
	}catch(Exception e){
		return availDistance;
	}
		return availDistance;
	}
	
	public  String getPlannedDistanceaAndETAForRoute(String origin,String destination,String allwayPoints){
		String urlLocation = "";
		URL geocodeURL;
		int totalDistance=0;
		 long eta = 0;
		String availDistance = "";
		try{
		urlLocation = "http://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination="+destination+"&waypoints=optimize:false|"+allwayPoints+"&mode=driving&language=en-EN&sensor=false&client="+clientId;				
		URL url = new URL(urlLocation);
	    CalculateDistance signer = new CalculateDistance();
		signer.passingKey(keyString);
		String request = signer.signRequest(url.getPath(),url.getQuery());
		urlLocation=url.getProtocol() + "://" + url.getHost() + request;			
		geocodeURL = new URL(urlLocation);
		URLConnection connection = geocodeURL.openConnection();
		connection.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String data = "";
		String line = "";
		JSONArray cabLocation =null;
		while((line = reader.readLine()) != null){
			data += line.trim();
		}
		JSONObject status = new JSONObject(data);
		String objstatus=status.getString("status");
		if(objstatus.equals("OK")){
			cabLocation = new JSONObject(data).getJSONArray("routes");	
			JSONArray elements = cabLocation.getJSONObject(0).getJSONArray("legs");    	
			for (int i=0; i<=elements.length()-1; i++){												
					totalDistance+= elements.getJSONObject(i).getJSONObject("distance").getInt("value");
                    eta += elements.getJSONObject(i).getJSONObject("duration").getInt("value");

				}
			float input = (float) (totalDistance * .001);
			DecimalFormat decimalFormat = new DecimalFormat("0.#");
			availDistance=decimalFormat.format(input);

			}
	}catch(Exception e){
		return availDistance+"-"+eta;
	}
		return availDistance+"-"+eta;
	}
	public int googleTravelledDistanceCalculation(String origin,String destination) throws IOException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException{
		String urlLocation = "";
		int distance=0;
		URL geocodeURL;
		urlLocation = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+origin+"&destinations="+destination+"&mode=driving&units=metric&sensor=true&client="+clientId;
	    URL url = new URL(urlLocation);
	    CalculateDistance signer = new CalculateDistance();
	    signer.passingKey(keyString);
	    String request = signer.signRequest(url.getPath(),url.getQuery());
	    urlLocation=url.getProtocol() + "://" + url.getHost() + request;
			geocodeURL = new URL(urlLocation);
			URLConnection connection = geocodeURL.openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer data = new StringBuffer();
			String line = "";
			while((line = reader.readLine()) != null){
				data.append(line.trim());
			}		
			JSONObject status = new JSONObject(data.toString());
			String objstatus=status.getString("status");
			    if(objstatus.equals("OK")){
					JSONArray rows = status.getJSONArray("rows");
					for(int j=0;j<rows.length();j++){
						JSONArray elements = rows.getJSONObject(j).getJSONArray("elements");	
			    	for(int i=0; i<elements.length();){			    		
			    		distance+=elements.getJSONObject(j).getJSONObject("distance").getInt("value");
						break;
						}
					}


	     }	
		
		return distance;
	}
	
	
	public String googleEtaCalculation(String origin,String destination) throws IOException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException{
		String urlLocation = "";
		int eta=0;
		URL geocodeURL;
		String etaInSec = "";	
		urlLocation = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+origin+"&destinations="+destination+"&mode=driving&units=metric&sensor=true&client="+clientId;
	    URL url = new URL(urlLocation);
		String cabCurrentLocation = "";
	    CalculateDistance signer = new CalculateDistance();
	    signer.passingKey(keyString);
	    String request = signer.signRequest(url.getPath(),url.getQuery());
	    urlLocation=url.getProtocol() + "://" + url.getHost() + request;
			geocodeURL = new URL(urlLocation);
			URLConnection connection = geocodeURL.openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer data = new StringBuffer();
			String line = "";
			while((line = reader.readLine()) != null){
				data.append(line.trim());
			}		
			JSONObject status = new JSONObject(data.toString());
			String objstatus=status.getString("status");
			    if(objstatus.equals("OK")){
					cabCurrentLocation = status.getJSONArray("origin_addresses").toString();
					JSONArray rows = status.getJSONArray("rows");
					for(int j=0;j<rows.length();j++){
						JSONArray elements = rows.getJSONObject(j).getJSONArray("elements");	
			    	for(int i=0; i<elements.length();){		
	                    eta += elements.getJSONObject(i).getJSONObject("duration").getInt("value");
						break;
						}
					}
	     }	
				int hours = eta / 3600;
				int minutes = (eta % 3600) / 60;
				int seconds = eta % 60;
				if (hours != 0) {
					etaInSec = hours + " h " + minutes + " min" + seconds + " sec";
				} else if (minutes != 0) {
					etaInSec = minutes + " min " + seconds + " sec";
				} else {
					etaInSec = seconds + " sec";
				}

		
		return etaInSec+"-"+cabCurrentLocation;
	}
	
	
	public String generateLatLngByaddress(String address) throws IOException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException{
		String urlLocation = "";		
		URL geocodeURL;
		String latlng="unknown";	
		try {
			urlLocation = "https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&client="+clientId;
		    URL url = new URL(urlLocation);			
		    CalculateDistance signer = new CalculateDistance();
		    signer.passingKey(keyString);
		    String request = signer.signRequest(url.getPath(),url.getQuery());
		    urlLocation=url.getProtocol() + "://" + url.getHost() + request;
				geocodeURL = new URL(urlLocation);
				URLConnection connection = geocodeURL.openConnection();
				connection.connect();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer data = new StringBuffer();
				String line = "";
				while((line = reader.readLine()) != null){
					data.append(line.trim());
				}		
				JSONObject status = new JSONObject(data.toString());
				String objstatus=status.getString("status");
				    if(objstatus.equals("OK")){
				    	double lon = status.getJSONArray("results").getJSONObject(0)
			                    .getJSONObject("geometry").getJSONObject("location")
			                    .getDouble("lat");												
						double lat = status.getJSONArray("results").getJSONObject(0)
			                    .getJSONObject("geometry").getJSONObject("location")
			                    .getDouble("lng");												
						latlng=Double.toString(lon)+","+Double.toString(lat);
						System.out.println(latlng.replaceAll("" ,""));
		     }		
		} catch (Exception e) {
			return latlng;
		}		
		return latlng;
	}
	
	
	
	public int employeeETACalculation(String origin,String destination) throws IOException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException{
		String urlLocation = "";
		int eta=0;
		URL geocodeURL;
		urlLocation = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+origin+"&destinations="+destination+"&mode=driving&units=metric&sensor=true&client="+clientId;
	    URL url = new URL(urlLocation);
	    CalculateDistance signer = new CalculateDistance();
	    signer.passingKey(keyString);
	    String request = signer.signRequest(url.getPath(),url.getQuery());
	    urlLocation=url.getProtocol() + "://" + url.getHost() + request;
			geocodeURL = new URL(urlLocation);
			URLConnection connection = geocodeURL.openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer data = new StringBuffer();
			String line = "";
			while((line = reader.readLine()) != null){
				data.append(line.trim());
			}		
			JSONObject status = new JSONObject(data.toString());
			String objstatus=status.getString("status");		
			    if(objstatus.equals("OK")){
					JSONArray rows = status.getJSONArray("rows");
					JSONArray elements = rows.getJSONObject(0).getJSONArray("elements");
					eta= elements.getJSONObject(0).getJSONObject("duration").getInt("value");
	     }	
		//Google will give data in seconds
		return eta;
	}

	
	

	public  double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
		try{
	      double theta = lon1 - lon2;
	      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	      dist = Math.acos(dist);
	      dist = rad2deg(dist);
	      dist = dist * 60 * 1.1515;
	      if (unit == 'K'){
	        dist = dist * 1.609344;
	      }
	      else if (unit == 'm'){
		        dist = dist * 1609.3;
		      }
	      else if (unit == 'N'){
	        dist = dist * 0.8684;  
	      }
	      StringBuffer sb  =new StringBuffer();
	      DecimalFormat decimalFormat = new DecimalFormat("0.#");
	      sb.append(decimalFormat.format(dist));
	      return (Double.parseDouble(sb.toString()));
		}
		catch(Exception e ){
				return 0;
		}
	    }	
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /*::  This function converts decimal degrees to radians             :*/
  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  public double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /*::  This function converts radians to decimal degrees             :*/
  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  public double rad2deg(double rad) {
    return (rad * 180.0 / Math.PI);
  }
	
  public void passingKey(String keyString) throws IOException {
      // Convert the key from 'web safe' base 64 to binary
      keyString = keyString.replace('-', '+');
      keyString = keyString.replace('_', '/');
      CalculateDistance.key = Base64.getDecoder().decode(keyString);  
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
//       String signature = Base64.encode(sigBytes);
      String signature = Base64.getEncoder().encodeToString(sigBytes);
      System.out.println("signature"+signature);
      // convert the signature to 'web safe' base 64
      signature = signature.replace('+', '-');
      signature = signature.replace('/', '_');
      return resource + "&signature=" + signature;
  }
}