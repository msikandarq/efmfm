package com.newtglobal.eFmFmFleet.web;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.ISessionManagementBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientUserRolePO;
import com.newtglobal.eFmFmFleet.model.EFmFmSessionManagementPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.PersistentLoginPO;

@Component
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private static Log log = LogFactory.getLog(MyAuthenticationSuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info(request.getParameter("j_username") + " Authenticated Successfully !!");
		//log.info(request.getParameter("j_password") + " pass -Authenticated Successfully !!");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 ISessionManagementBO iSessionManagementBO = (ISessionManagementBO) ContextLoader.getContext().getBean("ISessionManagementBO");
		List<EFmFmClientUserRolePO> userDetail = userMasterBO
				.getUserRoleByUserName(request.getParameter("j_username"));		
		log.info("userMasterPO"+userDetail.size());
		List<EFmFmUserMasterPO> userMasterPO = userMasterBO
				.getSpecificUserDetailsByUserName(request.getParameter("j_username"));			
		log.info("userMasterPO"+userMasterPO.size());
		if(userDetail.isEmpty()){
			setDefaultTargetUrl("/loginFailed");			
		}
		else if (!(userMasterPO.isEmpty()) && userMasterPO.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO") && (userMasterPO.get(0).getWrongPassAttempt() == userMasterPO.get(0).geteFmFmClientBranchPO()
                .getNumberOfAttemptsWrongPass()) && (getDisableTime(24, 0, userMasterPO.get(0).getWrongPassAttemptDate()) > new Date().getTime())) { 
            setDefaultTargetUrl("/disable");
        }	
		else if (!(userMasterPO.isEmpty()) && userMasterPO.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO") && (userDetail.get(0).getEfmFmUserMaster().isTempPassWordChange())) { 
            setDefaultTargetUrl("/tempLogin");
        }   
		else if ((!userDetail.isEmpty()) && userMasterPO.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO") 
				&&  (passwordChangeDate(userDetail.get(0).getEfmFmUserMaster().getPasswordChangeDate(),
						userDetail.get(0).geteFmFmClientBranchPO().getPasswordResetPeriodForAdminInDays()) < new Date()
								.getTime()) && userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase("admin")) {
//		    passAccess=true;
			log.info("inside logindenied");
			setDefaultTargetUrl("/logindenied");			
			
		}
		else if ((!userDetail.isEmpty()) && userMasterPO.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO")
                && (passwordChangeDate(userDetail.get(0).getEfmFmUserMaster().getPasswordChangeDate(),
                        userDetail.get(0).geteFmFmClientBranchPO().getPasswordResetPeriodForUserInDays()) < new Date()
                                .getTime()) && !(userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase("admin"))) {
            log.info("inside logindenied");
            setDefaultTargetUrl("/logindenied");
        }
		else if ( (!userDetail.isEmpty()) && userMasterPO.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO") && passwordChangeDate(userMasterPO.get(0).getLastLoginTime(),userMasterPO.get(0).geteFmFmClientBranchPO().getInactiveAdminAccountAfterNumOfDays()) < new Date().getTime() && userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase("admin")){
            setDefaultTargetUrl("/inactive");
        }
		
		else if (!(userMasterPO.isEmpty()) && userMasterPO.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO") && (userMasterPO.get(0).getWrongPassAttempt() == userMasterPO.get(0).geteFmFmClientBranchPO()
                .getNumberOfAttemptsWrongPass()) && (getDisableTime(24, 0, userMasterPO.get(0).getWrongPassAttemptDate()) < new Date().getTime())) {                           
                userMasterPO.get(0).setWrongPassAttempt(0);
                userMasterBO.update(userMasterPO.get(0));       
                setDefaultTargetUrl("/home");
        }
		else {
			if (userMasterBO.isAleradyLoggedin(request.getParameter("j_username")) == 1) {
				userMasterPO.get(0).setWrongPassAttempt(0);
				userMasterBO.update(userMasterPO.get(0));		
				setDefaultTargetUrl("/home");
			} else {
				log.info(request.getParameter("j_username") + " tried again to login");				
					List<PersistentLoginPO> persistentLoginPO = userMasterBO
							.getUserLoggedInDetail(request.getParameter("j_username"));	
					
                    log.info("sessionDetail"+persistentLoginPO.size());
					if(!(persistentLoginPO.isEmpty())){						
					    List<EFmFmSessionManagementPO> sessionDetail= 	iSessionManagementBO.OnPasswordChangeInvalidateAllTheSessions(userMasterPO.get(0).getUserId());
                    log.info("sessionDetail"+sessionDetail.size());    
                    if(!(sessionDetail.isEmpty())){
					    if(sessionDetail.get(0).getUserAgent().equalsIgnoreCase(userAgentSelection(request.getHeader("User-Agent")))){
     					userMasterBO.delteRecordFromSeries(persistentLoginPO.get(persistentLoginPO.size()-1).getSeries());
                     }
                     else{
     		//			userMasterBO.delteRecordFromSeries(persistentLoginPO.get(0).getSeries());
                     }	
						   for(EFmFmSessionManagementPO session:sessionDetail){
							   session.setSessionActiveStatus("N");
							   session.setSessionEndTime(new Date());
							   iSessionManagementBO.update(session);
						   }
					    
                    }
					}
//					setDefaultTargetUrl("/alreadylogin");
					/*
					 * Servion Code
					 */
					/*LdapContext ctx;
					try {
						ctx = ActiveDirectory.getConnection(userName,password);
						ctx.close();
					} catch (NamingException e) {	
						e.printStackTrace();
						setDefaultTargetUrl("/home");
					}*/
					
					
					setDefaultTargetUrl("/home");								

				}
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}
	public long getDisableTime(int hours,int minutes,Date checkIndate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkIndate);
        calendar.add(Calendar.HOUR, hours);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTimeInMillis(); 
    }
	public long passwordChangeDate(Date pastchangePassDate, int numDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pastchangePassDate);
		calendar.add(Calendar.DAY_OF_YEAR, numDays);
		return calendar.getTimeInMillis();
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
