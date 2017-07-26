package com.newtglobal.eFmFmFleet.eFmFmFleet;

import java.security.Key;
import java.util.Base64;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;


public class JwtTokenGenerator {
	
	private static final Key SECRET = MacProvider.generateKey(SignatureAlgorithm.HS256);
	private static final byte[] SECRET_BYTES = SECRET.getEncoded();
	private static final String BASE64_SECRET_BYTES = Base64.getEncoder().encodeToString(SECRET_BYTES);

	/**
	* Method to generate token using SHA 256 Algorithm.
	* Set expire time for the token   
	*/	
	public   String generateToken() {
		try {
			String id = UUID.randomUUID().toString().replace("-", "");
			String token = Jwts.builder().setId(id).setSubject("authorization").signWith(SignatureAlgorithm.HS256, BASE64_SECRET_BYTES)
					.claim("Scope", "").compact();
			return token;
		} catch (Exception e) {
			return null;
		}
	}	
	
	 public  String getClientIpAddr(HttpServletRequest request) {  
	        String ip = request.getHeader("X-Forwarded-For");  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = request.getHeader("Proxy-Client-IP");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = request.getHeader("WL-Proxy-Client-IP");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = request.getHeader("HTTP_X_FORWARDED");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = request.getHeader("HTTP_CLIENT_IP");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = request.getHeader("HTTP_FORWARDED_FOR");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = request.getHeader("HTTP_FORWARDED");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = request.getHeader("HTTP_VIA");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = request.getHeader("REMOTE_ADDR");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = request.getRemoteAddr();  
	        }  
	        return ip;  
	    }
	 
	   public String userAgentSelection(String userAgent1){   	
	        String  browserDetails  =   userAgent1;
	        String  userAgent       =   browserDetails;
	        String  user            =   userAgent.toLowerCase();

	        String os = "";
	        String browser = "";
	        try{
	        //=================OS=======================
	         if (userAgent.toLowerCase().indexOf("windows") >= 0 )
	         {
	             os = "Windows";
	         } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
	         {
	             os = "Mac";
	         } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
	         {
	             os = "Unix";
	         } else if(userAgent.toLowerCase().indexOf("android") >= 0)
	         {
	             os = "Android";
	         } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
	         {
	             os = "IPhone";
	         }else{
	             os = "UnKnown, More-Info: "+userAgent;
	         }
	         //===============Browser===========================
	        if (user.contains("msie"))
	        {
	            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
	            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
	        } else if (user.contains("safari") && user.contains("version"))
	        {
	            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
	        } else if ( user.contains("opr") || user.contains("opera"))
	        {
	            if(user.contains("opera"))
	                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
	            else if(user.contains("opr"))
	                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
	        } else if (user.contains("chrome"))
	        {
	            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
	        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
	        {
	            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
	            browser = "Netscape-?";

	        } else if (user.contains("firefox"))
	        {
	            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
	        } else if(user.contains("rv"))
	        {
	            browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
	        } else
	        {
	            browser = "UnKnown, More-Info: "+userAgent;
	        }
		}catch(Exception e){
			
		}
		return browser;
	}

}
