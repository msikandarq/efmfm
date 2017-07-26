package com.newtglobal.eFmFmFleet.web;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.newtglobal.eFmFmFleet.business.bo.ISessionManagementBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.model.EFmFmSessionManagementPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;


public class CustomLogoutFilter extends SimpleUrlLogoutSuccessHandler {

	private static Log log = LogFactory.getLog(CustomLogoutFilter.class);
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
        IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 ISessionManagementBO iSessionManagementBO = (ISessionManagementBO) ContextLoader.getContext().getBean("ISessionManagementBO");
      try{
		 List<EFmFmUserMasterPO> userDetails = iUserMasterBO
                .getSpecificUserDetailsByUserName(authentication.getName());
        String userIPAddress="";
        userIPAddress = request.getRemoteAddr();
    	if (userIPAddress.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
    	    InetAddress inetAddress = InetAddress.getLocalHost();
    	    String ipAddress = inetAddress.getHostAddress();
    	    userIPAddress = ipAddress;
    	}
  	    log.info("Logged In User IP Adress"+userDetails.get(0).getUserId()+userDetails.get(0).geteFmFmClientBranchPO().getBranchId()+request.getHeader("User-Agent")+userIPAddress);
        if(!(userDetails.isEmpty())){
        	List<EFmFmSessionManagementPO> sessionDetail= 	iSessionManagementBO.getAllBrowserSessionsFromUserAgentAndUserId(userDetails.get(0).getUserId(),userAgentSelection(request.getHeader("User-Agent")), userIPAddress);
       for(EFmFmSessionManagementPO session:sessionDetail){
    	   session.setSessionActiveStatus("N");
    	   session.setSessionEndTime(new Date());
    	   iSessionManagementBO.update(session);
       }
        }
		log.info("CustomLogoutFilter called"+authentication.getName());
      }
      catch(Exception e){
  		log.info("Error :-"+e);
 
      }
//		if (authentication != null) {
//			request.getSession().invalidate();
//		}
		 request.getSession().invalidate();
		 response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		 response.setHeader("Pragma", "no-cache");
		 log.info("index");
		 setDefaultTargetUrl("/index");
		super.onLogoutSuccess(request, response, authentication);
	}
	
	 private String userAgentSelection(String userAgent1){
	    	
	        String  browserDetails  =   userAgent1;
	        String  userAgent       =   browserDetails;
	        String  user            =   userAgent.toLowerCase();

	        String os = "";
	        String browser = "";
	        try{
	        log.info("User Agent for the request is===>"+browserDetails);
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
	        log.info("Operating System======>"+os);
	        log.info("Browser Name==========>"+browser);
		}catch(Exception e){
			
		}
		return browser;
	}


}
