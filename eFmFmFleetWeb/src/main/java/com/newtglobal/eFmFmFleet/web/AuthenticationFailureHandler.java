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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;

public class AuthenticationFailureHandler  extends SimpleUrlAuthenticationFailureHandler {
	
	private static Log log = LogFactory.getLog(AuthenticationFailureHandler.class);
	public void	onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException{
		log.info("Authentication fail of "+request.getParameter("j_username")+" due to wrong password  !!");
		IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		List<EFmFmUserMasterPO> userMasterPO = iUserMasterBO.getSpecificUserDetailsByUserName(request.getParameter("j_username"));
		log.info("detail"+userMasterPO.size()+request.getParameter("j_username"));
		if(!(userMasterPO.isEmpty()) && !(userMasterPO.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO"))){
		    if (!(userMasterPO.isEmpty()) && (userMasterPO.get(0).getWrongPassAttempt() == userMasterPO.get(0).geteFmFmClientBranchPO()
	                .getNumberOfAttemptsWrongPass()) && (getDisableTime(24, 0, userMasterPO.get(0).getWrongPassAttemptDate()) > new Date().getTime())) { 
		        setDefaultFailureUrl("/disable");
	        }       		    
		else if (!(userMasterPO.isEmpty()) && (userMasterPO.get(0).getWrongPassAttempt() == userMasterPO.get(0).geteFmFmClientBranchPO()
					.getNumberOfAttemptsWrongPass()) && (getDisableTime(24, 0, userMasterPO.get(0).getWrongPassAttemptDate()) < new Date().getTime())) {
	                 userMasterPO.get(0).setWrongPassAttempt(1);
	                iUserMasterBO.update(userMasterPO.get(0));
				setDefaultFailureUrl("/loginFailed");
			}
			else if (!(userMasterPO.isEmpty()) && (userMasterPO.get(0).getWrongPassAttempt()== (userMasterPO.get(0).geteFmFmClientBranchPO()
                    .getNumberOfAttemptsWrongPass()-1))) {
                userMasterPO.get(0).setWrongPassAttempt(userMasterPO.get(0).getWrongPassAttempt()+1);
                userMasterPO.get(0).setWrongPassAttemptDate(new Date());
                iUserMasterBO.update(userMasterPO.get(0));
                setDefaultFailureUrl("/disable");
            }
			else if (!(userMasterPO.isEmpty()) && (userMasterPO.get(0).getWrongPassAttempt()== (userMasterPO.get(0).geteFmFmClientBranchPO()
					.getNumberOfAttemptsWrongPass()-2))) {	
				userMasterPO.get(0).setWrongPassAttempt(userMasterPO.get(0).getWrongPassAttempt()+1);
				iUserMasterBO.update(userMasterPO.get(0));
				setDefaultFailureUrl("/lastattempt");
			}

			else{			   
			userMasterPO.get(0).setWrongPassAttempt(userMasterPO.get(0).getWrongPassAttempt()+1);
			iUserMasterBO.update(userMasterPO.get(0));
			setDefaultFailureUrl("/loginFailed");
			}
		}
		else{
			setDefaultFailureUrl("/loginFailed");
		}
		super.onAuthenticationFailure(request, response, exception);
	}
	public long getDisableTime(int hours,int minutes,Date checkIndate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkIndate);
        calendar.add(Calendar.HOUR, hours);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTimeInMillis(); 
    }
    
}
