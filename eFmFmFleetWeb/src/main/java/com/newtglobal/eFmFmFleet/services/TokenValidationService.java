package com.newtglobal.eFmFmFleet.services;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.ISessionManagementBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmSessionManagementPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.PersistentLoginPO;
import com.newtglobal.eFmFmFleet.model.TokenDetails;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/authorization")
public class TokenValidationService {
	
	private static Log log = LogFactory.getLog(TokenValidationService.class);
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	
	/**
	* Method to generate JWT token.
	*/	
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/refreshToken")
	public Response generateJWTAccessToken(TokenDetails tokenData) throws ParseException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> response = new HashMap<String, Object>();	
	//	log.info("Inside token refresh "+tokenData.getUserId());
		try {
			List<TokenDetails> tokenDetails=userMasterBO.getAuthorizationTokenForParticularUserFromUserId(tokenData.getUserId());
			String token="";
//			log.info("tokenDetails"+tokenDetails.size());
			if(!(tokenDetails.isEmpty())){
				//2 minutes total milliseconds....120000
				if((tokenDetails.get(0).getTakenGenrationTime().getTime()+120000)<(new Date().getTime())){
					List<EFmFmUserMasterPO> tokenUpdates=userMasterBO.getUserDetailFromUserId(tokenData.getUserId());
	//				log.info("tokenUpdates"+tokenUpdates.size());

					if(!(tokenUpdates.isEmpty())){
						JwtTokenGenerator getToken=new JwtTokenGenerator();
					token=getToken.generateToken();
					tokenUpdates.get(0).setAuthorizationToken(token);
					tokenUpdates.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(tokenUpdates.get(0));
					}
				}
				else{
					token=tokenDetails.get(0).getAuthorizationToken();
				}
				response.put("token",token );			
			}
		} catch (Exception ex) {
			log.info("error"+ex);
			response.put("token", "error");			
		}
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}

    /*
     * User can disable all previous session on clicking of yes
     * delete all persistence entries for this particular user and in active all the session
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces("application/json")
    @Path("/clearSession")
    public Response disableFromAllOtherSession(TokenDetails tokenData) {
		 ISessionManagementBO iSessionManagementBO = (ISessionManagementBO) ContextLoader.getContext().getBean("ISessionManagementBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		List<PersistentLoginPO> persistentDetails = userMasterBO
				.getUserLoggedInDetail(tokenData.getUserName());	
		if(!(persistentDetails.isEmpty())){			
			userMasterBO.delteRecordFromUserName(persistentDetails.get(0).getUserName());						
		}
		List<EFmFmUserMasterPO> userDetails = userMasterBO
				.getSpecificUserDetailsByUserName(tokenData.getUserName());			
		if(!(userDetails.isEmpty())){
		    List<EFmFmSessionManagementPO> sessionDetail= 	iSessionManagementBO.OnPasswordChangeInvalidateAllTheSessions(userDetails.get(0).getUserId());
			   for(EFmFmSessionManagementPO session:sessionDetail){
				   session.setSessionActiveStatus("N");
				   session.setSessionEndTime(new Date());
				   iSessionManagementBO.update(session);
			   }
		}		
		responce.put("status", "sucess");
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
    
    @POST
    @Path("/sessionValidityCheck")
    public Response getSessionValidityCheck(EFmFmSessionClient eFmFmSessionClientPO) {
	 ISessionManagementBO iSessionManagementBO = (ISessionManagementBO) ContextLoader.getContext().getBean("ISessionManagementBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

	 Map<String, Object> response = new HashMap<String, Object>();  	
	 try {
			List<TokenDetails> tokenDetails=userMasterBO.getAuthorizationTokenForParticularUserFromUserId(eFmFmSessionClientPO.getUserId());
			String token="";
			if(!(tokenDetails.isEmpty())){
				//2 minutes total milliseconds....120000
				if((tokenDetails.get(0).getTakenGenrationTime().getTime()+120000)<(new Date().getTime())){
					List<EFmFmUserMasterPO> tokenUpdates=userMasterBO.getUserDetailFromUserId(eFmFmSessionClientPO.getUserId());
					if(!(tokenUpdates.isEmpty())){
						JwtTokenGenerator getToken=new JwtTokenGenerator();
					token=getToken.generateToken();
					tokenUpdates.get(0).setAuthorizationToken(token);
					tokenUpdates.get(0).setTokenGenerationTime(new Date());
					userMasterBO.update(tokenUpdates.get(0));
					}
				}
				else{
					token=tokenDetails.get(0).getAuthorizationToken();
				}
				response.put("token",token );			
			}
		} catch (Exception ex) {
			log.info("error"+ex);
			response.put("token", "error");			
		}
	String  sessionReturn=iSessionManagementBO.getSessionValidityCheck(eFmFmSessionClientPO.getUserId(),eFmFmSessionClientPO.getBranchId(),eFmFmSessionClientPO.getUserAgent(),eFmFmSessionClientPO.getUserIPAddress());
 //  log.info("sessionReturn"+sessionReturn);
	response.put("data", sessionReturn);
   	return Response.ok(response, MediaType.APPLICATION_JSON).build();
   	
    }
    
	
    @POST
	@Path("/clearSession/{userName}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces("application/json")
	public Response getCurrentConversionMessage(
			@PathParam("userName") String userName){
		 ISessionManagementBO iSessionManagementBO = (ISessionManagementBO) ContextLoader.getContext().getBean("ISessionManagementBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();		
 	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		List<PersistentLoginPO> persistentDetails = userMasterBO
				.getUserLoggedInDetail(userName);	
		if(!(persistentDetails.isEmpty())){			
			userMasterBO.delteRecordFromUserName(persistentDetails.get(0).getUserName());						
		}
		log.info("userName "+userName);

		List<EFmFmUserMasterPO> userDetails = userMasterBO
				.getSpecificUserDetailsByUserName(userName);			
		if(!(userDetails.isEmpty())){
		    List<EFmFmSessionManagementPO> sessionDetail= 	iSessionManagementBO.OnPasswordChangeInvalidateAllTheSessions(userDetails.get(0).getUserId());
			   for(EFmFmSessionManagementPO session:sessionDetail){
				   session.setSessionActiveStatus("N");
				   session.setSessionEndTime(new Date());
				   iSessionManagementBO.update(session);
			   }
		}		
		responce.put("status", "sucess");
       return Response.ok(responce, MediaType.APPLICATION_JSON).build();
   }
	
}
