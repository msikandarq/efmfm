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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/employee")
@Consumes("application/json")
@Produces("application/json")
public class EmployeeOnBoardService {
	
	private static Log log = LogFactory.getLog(AlertService.class);	
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();


	/**
	* The update On board  method implemented.
	* for modifying Employee Details Transaction table.   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-04-30 
	* 
	* @return success/failure details.
	*/	
	
	@POST
	@Path("/employeeOnboardStatus")
	public Response employeeComingStatus(EFmFmEmployeeTripDetailPO employeeTripDetailPO) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + employeeTripDetailPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
	 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),employeeTripDetailPO.getUserId()))){
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTripDetailPO.getUserId());
	 			if (!(userDetail.isEmpty())) {
	 				String jwtToken = "";
	 				try {
	 					JwtTokenGenerator token = new JwtTokenGenerator();
	 					jwtToken = token.generateToken();
	 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
	 					userDetail.get(0).setMobTokenGenerationTime(new Date());
	 					userMasterBO.update(userDetail.get(0));
	 				} catch (Exception e) {
	 					log.info("error" + e);
	 				}
	                responce.put("token", jwtToken);
	 			}
	 		
	 		}catch(Exception e){
	 				log.info("authentication error"+e);
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
		 List<EFmFmEmployeeTripDetailPO> empDetails = iCabRequestBO.getParticularTripEmployeeRequestDetails(
				employeeTripDetailPO.getEmpTripId(), employeeTripDetailPO.getRequestId(),
				employeeTripDetailPO.getAssignRouteId());
		log.info("Employee status update OnBoard "+empDetails.size());
		responce.put("status", "success");
		if (!(empDetails.isEmpty())) {
				empDetails.get(0).setEmployeeOnboardStatus("OnBoard");
	  			empDetails.get(0).setEmployeeOnboardStatusTime(new Date().getTime());  			
			    iCabRequestBO.update(empDetails.get(0));
		}
		 log.info("serviceEnd -UserId :" + employeeTripDetailPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	* The tripConfirmation From Driver.
	* for assign access  Details Transaction table.   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-04-30 
	* 
	* @return success/failure details.
	*/	
	@POST
	@Path("/tripConfirmationFromDriver")
	public Response tripConfirmationFromDriver(EFmFmAssignRoutePO assignRoutePO) {
		 log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		Map<String, Object> requests = new HashMap<String, Object>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
//
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}
//		 
//		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
//		   if (!(userDetailToken.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
//		     userDetailToken.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetailToken.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
			List<EFmFmAssignRoutePO> assignRoute = iCabRequestBO
					.getParticularDriverAssignTripDetail(assignRoutePO);
			assignRoute.get(0).setTripConfirmationFromDriver("Delivered");
			iCabRequestBO.update(assignRoute.get(0));	 
		  requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

}
