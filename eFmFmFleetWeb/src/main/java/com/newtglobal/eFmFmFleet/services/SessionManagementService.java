package com.newtglobal.eFmFmFleet.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.ISessionManagementBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/sessionManagement")
@Consumes("application/json")
@Produces("application/json")
public class SessionManagementService {

//	private static Log log = LogFactory.getLog(SessionManagementService.class);
	
	@Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();
	
	
	    @POST
	    @Path("/addingSessionDetails")
	    public Response addingSessionDetails(HttpServletRequest request) {	    	
//		 ISessionManagementBO iSessionManagementBO = (ISessionManagementBO) ContextLoader.getContext().getBean("ISessionManagementBO");
//		 EFmFmSessionManagementPO eFmFmSessionManagementPO;
//	   	iSessionManagementBO.saveSessionData(eFmFmSessionManagementPO);
	    return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	    }
	    
	    @POST
	    @Path("/logOutAllSession")
	    public Response logOutAllSession(EFmFmSessionClient eFmFmSessionClientPO) {
		 ISessionManagementBO iSessionManagementBO = (ISessionManagementBO) ContextLoader.getContext().getBean("ISessionManagementBO");
	        
	   	iSessionManagementBO.logOutAllSession(eFmFmSessionClientPO.getUserId(),eFmFmSessionClientPO.getBranchId());
	    return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	    }
//	    
//	    @POST
//	    @Path("/logOutIndividualSession")
//	    public Response logOutIndividualSession(String userId,String sessionId,int branchId) {
//		 ISessionManagementBO iSessionManagementBO = (ISessionManagementBO) ContextLoader.getContext().getBean("ISessionManagementBO");
//	        
//	   	iSessionManagementBO.logOutIndividualSession(userId,sessionId,branchId);
//	    return Response.ok("Success", MediaType.APPLICATION_JSON).build();
//	    }
//	    
//	    @POST
//	    @Path("/logOutAllSessionExceptCurrent")
//	    public Response logOutAllSessionExceptCurrent(String userId,String sessionId,int branchId) {
//		 ISessionManagementBO iSessionManagementBO = (ISessionManagementBO) ContextLoader.getContext().getBean("ISessionManagementBO");
//	        
//	   	iSessionManagementBO.logOutAllSessionExceptCurrent(userId,sessionId,branchId);
//	    return Response.ok("Success", MediaType.APPLICATION_JSON).build();
//	    }
	    
	    
//	    @POST
//	    @Path("/getSessionValidityCheck")
//	    public Response getSessionValidityCheck(EFmFmSessionClient eFmFmSessionClientPO) {
//		 ISessionManagementBO iSessionManagementBO = (ISessionManagementBO) ContextLoader.getContext().getBean("ISessionManagementBO");
//		 Map<String, Object> responce = new HashMap<String, Object>();  					
//	   	String  sessionReturn=iSessionManagementBO.getSessionValidityCheck(eFmFmSessionClientPO.getUserId(),eFmFmSessionClientPO.getBranchId(),eFmFmSessionClientPO.getUserAgent(),eFmFmSessionClientPO.getUserIPAddress());
//	   	responce.put("data", sessionReturn);
//	   	return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//	   	
//	    }
	    
	    
	    
	
}
