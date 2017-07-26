package com.newtglobal.eFmFmFleet.web;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.util.StringUtils;

import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Base;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
	

public class CustomAutoLogin {
	
	public static final String cookieName = "SPRING_SECURITY_REMEMBER_ME_COOKIE";
	private static final String DELIMITER = ":";
	

	public String autoLogin(Cookie[] cookies,HttpSession session,HttpServletRequest request) throws SocketException, UnsupportedEncodingException
	{
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader	.getContext().getBean("IUserMasterBO"); 	
		String[] cookieTokens=null;
		for (int i = 0; i < cookies.length; i++) {
	           if (cookieName.equals(cookies[i].getName())) {
	               cookieTokens = decodeCookie(cookies[i].getValue());
	   }
	}
		 
		if(cookieTokens==null)
		{
			
			userMasterBO.delteRecord(request.getRemoteAddr());
			return "login";
		}	
		else{
				String userName  = userMasterBO.getUserNamebySeries(cookieTokens[0]);
					 if(userName==null)
			         {
						 userMasterBO.delteRecord(request.getRemoteAddr());
			      	return "login"  ;
			         }
			         else
			         {
			        	 IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader
									.getContext().getBean("IUserMasterBO");
					EFmFmUserMasterPO userMasterPO = iUserMasterBO.getUserDetailByUserName(userName);
//					RoleMasterPO roleMasterPO = iRoleMasterBO.getUserRoleByRoleId(userMasterPO.getUserRoleId());
						session.setAttribute("role", "admin");
			     		session.setAttribute("firstName",userMasterPO.getLastName());
			     		session.setAttribute("lastName", userMasterPO.getLastName());
			     		session.setAttribute("clientId", 1);
			     		session.setAttribute("profileId",1);
			     		session.setAttribute("productName",1);
			     		session.setAttribute("userName", userName);
			     		session.setAttribute("lastRequest", new Date());
			      	   return "home";
			         }
		}
		
	}
		
		 protected String extractRememberMeCookie(HttpServletRequest request) {
		      Cookie[] cookies = request.getCookies();
		        if ((cookies == null) || (cookies.length == 0)) {
		            return null;
		        }
		        for (int i = 0; i < cookies.length; i++) {
		            if (cookieName.equals(cookies[i].getName())) {
		                return cookies[i].getValue();
		            }
		        }
		
		       return null;
		   } 
	 protected String[] decodeCookie(String cookieValue) throws InvalidCookieException {
		         for (int j = 0; j < cookieValue.length() % 4; j++) {
		             cookieValue = cookieValue + "=";
		        }	 
		        if (!Base.isBase64(cookieValue.getBytes())) {
		             throw new InvalidCookieException( "Cookie token was not Base64 encoded; value was '" + cookieValue + "'");
		        }		 
		         String cookieAsPlainText = new String(Base.decode(cookieValue.getBytes()));		 
		         String[] tokens = StringUtils.delimitedListToStringArray(cookieAsPlainText, DELIMITER);		 
		         if ((tokens[0].equalsIgnoreCase("http") || tokens[0].equalsIgnoreCase("https")) && tokens[1].startsWith("//")) {
		             // Assume we've accidentally split a URL (OpenID identifier)
		             String[] newTokens = new String[tokens.length - 1];
		             newTokens[0] = tokens[0] + ":" + tokens[1];
		             System.arraycopy(tokens, 2, newTokens, 1, newTokens.length - 1);
		             tokens = newTokens;
		        }
		 
		        return tokens;
		    }

	 public void setSessionAttribute(HttpSession session, String role,
				String profileName, String emailId, String userName, int userId,String language,String backgroundTheam,int institutionId, String institutionType, String instName) {
			session.setAttribute("userRole", role);
			session.setAttribute("userProfileName", profileName);
			session.setAttribute("userEmailId", emailId);
			session.setAttribute("userName", userName);
			session.setAttribute("userId", userId);
			session.setAttribute("currentlanguage", language);
			session.setAttribute("isLoggedIn", "true");
		}


}
