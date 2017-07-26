package com.newtglobal.eFmFmFleet.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

class Geocode {
	public double lat;
	public double lng;

	public Geocode(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}
}

class Polyline {
	static List<Geocode> decode(String polyline, int precision) {
		List<Geocode> coordinates = new ArrayList<Geocode>();
		int index = 0, shift, result;
		int byte_;
		int lat = 0, lng = 0, latitude_change, longitude_change, factor = (int) Math.pow(10, precision);

		while (index < polyline.length()) {
			byte_ = 0;
			shift = 0;
			result = 0;

			do {
				byte_ = polyline.charAt(index++) - 63;
				result |= (byte_ & 0x1f) << shift;
				shift += 5;
			} while (byte_ >= 0x20);

			latitude_change = ((result % 2 == 1) ? ~(result >> 1) : (result >> 1));

			shift = result = 0;

			do {
				byte_ = polyline.charAt(index++) - 63;
				result |= (byte_ & 0x1f) << shift;
				shift += 5;
			} while (byte_ >= 0x20);

			longitude_change = ((result % 2 == 1) ? ~(result >> 1) : (result >> 1));

			lat += latitude_change;
			lng += longitude_change;

			coordinates.add(new Geocode((double) lat / factor, (double) lng / factor));
		}

		return coordinates;
	}
}

public class RouteGeocodeService {

	private static byte[] key;
	private static String keyString = "iI1tzzodgnjqhMQq6zIyPJ9iy40=";

	public List<String> getRoutePoints(String origin, String destination, String wayPoints)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
		ArrayList<String> lattilongi = new ArrayList<String>();

		String urlLocation = "http://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination="
				+ destination + "&waypoints=optimize:true|" + wayPoints
				+ "&mode=driving&language=en-EN&sensor=false&client=gme-newtglobalindiaprivate";
		URL geocodeURL;
		String polyline = "";
		URL url = new URL(urlLocation);
		CabRequestService signer = new CabRequestService();
		signer.passingKey(keyString);
		String request = signer.signRequest(url.getPath(), url.getQuery());
		urlLocation = url.getProtocol() + "://" + url.getHost() + request;
		geocodeURL = new URL(urlLocation);
		URLConnection connection = geocodeURL.openConnection();
		connection.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String data = "";
		String line = "";
		while ((line = reader.readLine()) != null) {
			data += line.trim();
		}
		JSONObject status = new JSONObject(data);
		String objstatus = status.getString("status");
		if (objstatus.equals("OK")) {
			JSONObject elements = new JSONObject(data).getJSONArray("routes").getJSONObject(0)
					.getJSONObject("overview_polyline");
			polyline += elements.get("points");
		}

		if (polyline.length() > 0) {
			List<Geocode> pointList = Polyline.decode(polyline, 5); // Use 5 for
																	// google
																	// maps
																	// polyline
			// pointList contains the list of all Geocodes in sequence in the
			// route
			for (Geocode G : pointList) {
				lattilongi.add(G.lat + "," + G.lng);
			}
		}
		return lattilongi;
	}

	public void passingKey(String keyString) throws IOException {
		// Convert the key from 'web safe' base 64 to binary
		keyString = keyString.replace('-', '+');
		keyString = keyString.replace('_', '/');
		// this.key = Base64.decode(keyString);
		RouteGeocodeService.key = Base64.getDecoder().decode(keyString);
	}

	public String signRequest(String path, String query)
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, URISyntaxException {
		// Retrieve the proper URL components to sign
		String resource = path + '?' + query;
		// Get an HMAC-SHA1 signing key from the raw key bytes
		SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
		// Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1
		// key
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(sha1Key);
		// compute the binary signature for the request
		byte[] sigBytes = mac.doFinal(resource.getBytes());
		// base 64 encode the binary signature
		// String signature = Base64.encode(sigBytes);
		String signature = Base64.getEncoder().encodeToString(sigBytes);
		// convert the signature to 'web safe' base 64
		signature = signature.replace('+', '-');
		signature = signature.replace('/', '_');
		return resource + "&signature=" + signature;
	}
}
