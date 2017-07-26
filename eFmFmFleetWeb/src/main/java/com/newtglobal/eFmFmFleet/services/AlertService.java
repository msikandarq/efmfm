package com.newtglobal.eFmFmFleet.services;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IAlertBO;
import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAlertTxnPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlertTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmZoneMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;


@Component
@Path("/alert")
@Consumes("application/json")
@Produces("application/json")
public class AlertService {	
	
	private static Log log = LogFactory.getLog(AlertService.class);	
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	
	/**
	* The updateAlert method implemented.
	* for modifying Alert record on alert Transaction table.   
	*
	* @author  Rajan R
	* 
	* @since   2015-05-26 
	* 
	* @return success/failure details.
	*/	
	
	@POST
	@Path("/updateAlert")
	public Response updateAlert(EFmFmAlertTxnPO eFmFmAlertTxnPO){		
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		 log.info("serviceStart -UserId :" + eFmFmAlertTxnPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 log.info("serviceEnd -UserId :" + eFmFmAlertTxnPO.getUserId());
//		 try{
//				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//	    		log.info("authenticationToken error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();		 		
//		 	}
//		 List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
//		   if (!(userDetail.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetail.get(0).setAuthorizationToken(jwtToken);
//		     userDetail.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetail.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
		 
		EFmFmAlertTxnPO alertTxnPO=iAlertBO.getParticularAlert(eFmFmAlertTxnPO);		
		iAlertBO.update(alertTxnPO);
		 
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	}
	

	/**
	* The deleteAlert method implemented.
	* for deleting unwanted Alert record on alert Transaction table.   
	*
	* @author  Rajan R
	* 
	* @since   2015-05-26 
	* 
	* @return success/failure details.
	*/	
	
	@POST
	@Path("/deleteAlert")
	public Response deleteAlert(EFmFmAlertTxnPO eFmFmAlertTxnPO){		
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		 log.info("serviceStart -UserId :" + eFmFmAlertTxnPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 log.info("serviceEnd -UserId :" + eFmFmAlertTxnPO.getUserId());
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
	    		log.info("authenticationToken error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
		   if (!(userDetail.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetail.get(0).setAuthorizationToken(jwtToken);
		     userDetail.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetail.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		 
		EFmFmAlertTxnPO alertTxnPO=iAlertBO.getParticularAlert(eFmFmAlertTxnPO);
		alertTxnPO.setStatus("D");
		iAlertBO.update(alertTxnPO);
		 log.info("serviceEnd -UserId :" + eFmFmAlertTxnPO.getUserId());
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	}
	

	/**
	* The postAlert method implemented.
	* for adding new Request to Alert Transaction table.   
	*
	* @author  Rajan R
	* 
	* @since   2015-05-26 
	* 
	*  @return success/failure details.
	*/	
	
	@POST
	@Path("/postAlertFromMobile")
	public Response postAlertFromMobile(EFmFmAlertTxnPO eFmFmAlertTxnPO){		
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		 log.info("serviceStart -UserId :" + eFmFmAlertTxnPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
	    		log.info("authenticationToken error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
		   if (!(userDetail.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetail.get(0).setAuthorizationToken(jwtToken);
		     userDetail.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetail.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		 
		EFmFmAlertTypeMasterPO  eFmFmAlertTypeMasterPO=new EFmFmAlertTypeMasterPO();
		EFmFmClientBranchPO eFmFmClientBranchPO=new EFmFmClientBranchPO();		
		eFmFmAlertTypeMasterPO.setAlertId(eFmFmAlertTxnPO.getEfmFmAlertTypeMaster().getAlertId());
		eFmFmAlertTypeMasterPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);		
//		EFmFmAlertTypeMasterPO  alertTypeMasterPO=iAlertBO.getAlertTypeIdDetails(eFmFmAlertTypeMasterPO);
				
		/*
		 * set the values for the alertMaster
		 */
		
		iAlertBO.save(eFmFmAlertTxnPO);	
		 log.info("serviceEnd -UserId :" + eFmFmAlertTxnPO.getUserId());
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	}
	
	/**
	* The listOfAlertType Details method implemented.
	* for getting the AlertType Details.   
	*
	* @author  Rajan R
	* 
	* @since   2015-05-26 
	* 
	* @return AlertType details.
	*/	
	@POST
	@Path("/listOfAlertType")
	public Response listOfAlertType(EFmFmAlertTypeMasterPO eFmFmAlertTypeMasterPO){		
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");	
		 log.info("serviceStart -UserId :" + eFmFmAlertTypeMasterPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 log.info("serviceEnd -UserId :" + eFmFmAlertTypeMasterPO.getUserId());
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTypeMasterPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
	    		log.info("authenticationToken error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmAlertTypeMasterPO.getUserId());
		   if (!(userDetail.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetail.get(0).setAuthorizationToken(jwtToken);
		     userDetail.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetail.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		 
		List<Map<String, Object>> listOfAlertTypeDetails= new ArrayList<Map<String, Object>>();
		List<EFmFmAlertTypeMasterPO> listOfAlertType=iAlertBO.getAllAlertTypeDetails(eFmFmAlertTypeMasterPO);		
		if(!(listOfAlertType.isEmpty())){			
			for(EFmFmAlertTypeMasterPO alertDetails:listOfAlertType){				
					Map<String, Object>  alertList= new HashMap<String, Object>();					
					alertList.put("alertId",alertDetails.getAlertId());
					listOfAlertTypeDetails.add(alertList);					
				}			
			}
		 
		return Response.ok(listOfAlertTypeDetails, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	* Get all posted alerts and get all zones,get all created alerts.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-06-26 
	* 
	* @return showing all posted alerts details.
	 * @throws ParseException 
	*/	
	
	@POST
	@Path("/postedalerts")
	public Response showAllPostedAlertZoneAndAlertTitlesDetails(EFmFmAlertTxnPO eFmFmAlertTxnPO) throws ParseException{		
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		 log.info("serviceStart -UserId :" + eFmFmAlertTxnPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
	    		log.info("authenticationToken error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
		   if (!(userDetail.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetail.get(0).setAuthorizationToken(jwtToken);
		     userDetail.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetail.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		 
		List<Map<String, Object>> allzoneDetails= new ArrayList<Map<String, Object>>();	
		List<Map<String, Object>> masteralerts= new ArrayList<Map<String, Object>>();	
		log.info("postedalerts Service..");
		Map<String, Object>  request= new HashMap<String, Object>();
		DateFormat  formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate  = formatter.parse(eFmFmAlertTxnPO.getFromDate());
		Date toDate  = formatter.parse(eFmFmAlertTxnPO.getToDate());
		List<Map<String, Object>> allAlertsDetail= new ArrayList<Map<String, Object>>();	
		List<EFmFmZoneMasterPO> allZones=iRouteDetailBO.getAllZoneNames(eFmFmAlertTxnPO.getEfmFmAlertTypeMaster().geteFmFmClientBranchPO().getBranchId());		
		if(!(allZones.isEmpty())){				
			for(EFmFmZoneMasterPO zoneDetails:allZones){				
					Map<String, Object>  zoneDetail= new HashMap<String, Object>();					
					zoneDetail.put("routeId",zoneDetails.getZoneId());
					zoneDetail.put("routeName",zoneDetails.getZoneName());										
					allzoneDetails.add(zoneDetail);			
				}			
			}	
		EFmFmAlertTypeMasterPO eFmFmAlertTypeMasterPO=new EFmFmAlertTypeMasterPO();
		EFmFmClientBranchPO branchId=new EFmFmClientBranchPO();
		branchId.setBranchId(eFmFmAlertTxnPO.getEfmFmAlertTypeMaster().geteFmFmClientBranchPO().getBranchId());
		eFmFmAlertTypeMasterPO.seteFmFmClientBranchPO(branchId);
		List<EFmFmAlertTypeMasterPO> allActiveAlerts=iAlertBO.getAllAlertTypeDetails(eFmFmAlertTypeMasterPO);
		if(!(allActiveAlerts.isEmpty())){				
			for(EFmFmAlertTypeMasterPO alertDetail:allActiveAlerts){				
					Map<String, Object>  alerts= new HashMap<String, Object>();					
					alerts.put("alertId",alertDetail.getAlertId());
					alerts.put("alertTitle",alertDetail.getAlertTitle());	
					alerts.put("alertType",alertDetail.getAlertType());										
					masteralerts.add(alerts);			
				}			
			}	
		List<EFmFmAlertTxnPO> allAlerts=iAlertBO.getCreatedAlertsByDate(fromDate, toDate, eFmFmAlertTxnPO.getEfmFmAlertTypeMaster().geteFmFmClientBranchPO().getBranchId());		
		for(EFmFmAlertTxnPO alerts:allAlerts){
			Map<String, Object>  alertsDetails= new HashMap<String, Object>();
			alertsDetails.put("startDate", formatter.format(alerts.getStartDate()));
			alertsDetails.put("endDate", formatter.format(alerts.getEndDate()));
			alertsDetails.put("tittle", alerts.getEfmFmAlertTypeMaster().getAlertTitle());
			alertsDetails.put("alertsType", alerts.getUserType());
			alertsDetails.put("postAlertId", alerts.getId());
			alertsDetails.put("description", alerts.getDescription());
			alertsDetails.put("zoneName", alerts.geteFmFmZoneMaster().getZoneName());
			allAlertsDetail.add(alertsDetails);
		}
		request.put("zones", allzoneDetails);
		request.put("alerts", masteralerts);
		request.put("postalerts", allAlertsDetail);	
		 log.info("serviceEnd -UserId :" + eFmFmAlertTxnPO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}
	
	
	/**
	* Creation an master alerts before posting.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-06-26 
	* 
	* @return AlertType details.
	*/	
	
	@POST
	@Path("/create")
	public Response createAnAlert(EFmFmAlertTypeMasterPO eFmFmAlertTypeMasterPO){		
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTypeMasterPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
	    		log.info("authenticationToken error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmAlertTypeMasterPO.getUserId());
		   if (!(userDetail.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetail.get(0).setAuthorizationToken(jwtToken);
		     userDetail.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetail.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		 
		 log.info("serviceStart -UserId :" + eFmFmAlertTypeMasterPO.getUserId());
		eFmFmAlertTypeMasterPO.setStatus("Y");
		Map<String, Object>  request= new HashMap<String, Object>();	
		request.put("status", "success");
		iAlertBO.save(eFmFmAlertTypeMasterPO);
		 log.info("serviceEnd -UserId :" + eFmFmAlertTypeMasterPO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}
	

	/**
	* post an allready created alerts for specific time period for particular date.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-06-26 
	* 
	* @return EFmFmAlertTxnPO details.
	 * @throws ParseException 
	*/	
	
	@POST
	@Path("/postalert")
	public Response postAnAlert(EFmFmAlertTxnPO eFmFmAlertTxnPO) throws ParseException{		
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
	    		log.info("authenticationToken error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
		   if (!(userDetail.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetail.get(0).setAuthorizationToken(jwtToken);
		     userDetail.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetail.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		 
		 log.info("serviceStart -UserId :" + eFmFmAlertTxnPO.getUserId());
		Map<String, Object>  request= new HashMap<String, Object>();
		DateFormat  formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate  = formatter.parse(eFmFmAlertTxnPO.getFromDate());
		Date toDate  = formatter.parse(eFmFmAlertTxnPO.getToDate());
		eFmFmAlertTxnPO.setCreationTime(new Date());
		eFmFmAlertTxnPO.setStartDate(fromDate);
		eFmFmAlertTxnPO.setStatus("Y");
		eFmFmAlertTxnPO.setEndDate(toDate);
		eFmFmAlertTxnPO.setDescription(eFmFmAlertTxnPO.getDescription());
		eFmFmAlertTxnPO.setUserType(eFmFmAlertTxnPO.getUserType());
		request.put("status", "success");
		iAlertBO.save(eFmFmAlertTxnPO);
		 log.info("serviceEnd -UserId :" + eFmFmAlertTxnPO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}
	
	
	/**
	* Edit the  posted alerts.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-07-01 
	* 
	* @return EFmFmAlertTxnPO details.
	 * @throws ParseException 
	*/	
	
	@POST
	@Path("/editalert")
	public Response editAnpostAlert(EFmFmAlertTxnPO eFmFmAlertTxnPO) throws ParseException{		
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
	    		log.info("authenticationToken error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
		   if (!(userDetail.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetail.get(0).setAuthorizationToken(jwtToken);
		     userDetail.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetail.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		 
		 log.info("serviceStart -UserId :" + eFmFmAlertTxnPO.getUserId());
		Map<String, Object>  request= new HashMap<String, Object>();
		EFmFmAlertTxnPO eFmFmAlertTxn=iAlertBO.getParticularAlert(eFmFmAlertTxnPO);
		eFmFmAlertTxn.setDescription(eFmFmAlertTxnPO.getDescription());
		eFmFmAlertTxn.setUpdatedTime(new Date());
		iAlertBO.update(eFmFmAlertTxn);	
		request.put("status", "success");
		 log.info("serviceEnd -UserId :" + eFmFmAlertTxnPO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	* Cancel the  posted alerts.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-07-01 
	* 
	* @return EFmFmAlertTxnPO details.
	 * @throws ParseException 
	*/	
	
	@POST
	@Path("/cancelalert")
	public Response cancelAnpostAlert(EFmFmAlertTxnPO eFmFmAlertTxnPO) throws ParseException{		
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
	    		log.info("authenticationToken error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
		   if (!(userDetail.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetail.get(0).setAuthorizationToken(jwtToken);
		     userDetail.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetail.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		 
		 log.info("serviceStart -UserId :" + eFmFmAlertTxnPO.getUserId());
		Map<String, Object>  request= new HashMap<String, Object>();
		EFmFmAlertTxnPO eFmFmAlertTxn=iAlertBO.getParticularAlert(eFmFmAlertTxnPO);
		eFmFmAlertTxn.setStatus("N");
		iAlertBO.update(eFmFmAlertTxn);	
		request.put("status", "success");
		 log.info("serviceEnd -UserId :" + eFmFmAlertTxnPO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}
	

	/**
	* get all the trip alerts from driver device.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-06-30
	* 
	* @return EFmFmTripAlertsPO details.
	 * @throws ParseException 
	*/	
	
	@POST
	@Path("/driveralert")
	public Response getDriverAlerts(EFmFmTripAlertsPO eFmFmTripAlertsPO) throws IOException{
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
//		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
//		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmTripAlertsPO.getUserId()))){
//
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//	    		log.info("authenticationToken error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}
//		 
//		 List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmTripAlertsPO.getUserId());
//		   if (!(userDetail.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetail.get(0).setAuthorizationToken(jwtToken);
//		     userDetail.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetail.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
		 log.info("serviceStart -UserId :" + eFmFmTripAlertsPO.getUserId());
		Map<String, Object>  requests = new HashMap<String, Object>();	
		EFmFmAssignRoutePO assignRoutePO=new EFmFmAssignRoutePO();
		assignRoutePO.setAssignRouteId(eFmFmTripAlertsPO.getEfmFmAssignRoute().getAssignRouteId());
		EFmFmAlertTypeMasterPO alertTypeMasterPO=new EFmFmAlertTypeMasterPO();
		alertTypeMasterPO.setAlertId(eFmFmTripAlertsPO.getEfmFmAlertTypeMaster().getAlertId());
		eFmFmTripAlertsPO.setEfmFmAlertTypeMaster(alertTypeMasterPO);
		eFmFmTripAlertsPO.setEfmFmAssignRoute(assignRoutePO);
		eFmFmTripAlertsPO.setCreationTime(new Date());
		eFmFmTripAlertsPO.setReadFlg("N");
		eFmFmTripAlertsPO.setUpdatedTime(new Date());
		eFmFmTripAlertsPO.setUserType("driver");
		eFmFmTripAlertsPO.setAlertClosingDescription("No Action");
		eFmFmTripAlertsPO.setAlertOpenStatus("open");
		eFmFmTripAlertsPO.setStatus("Y");		
		iAlertBO.save(eFmFmTripAlertsPO);		
		requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + eFmFmTripAlertsPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	* get all the trip alerts from employee device.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-06-30
	* 
	* @return EFmFmTripAlertsPO details.
	 * @throws ParseException 
	*/	
	
	
	@POST
	@Path("/employeealert")
	public Response getEmployeeAlerts(EFmFmTripAlertsPO eFmFmTripAlertsPO) throws IOException{
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" + eFmFmTripAlertsPO.getUserId());		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmTripAlertsPO.getUserId()))){
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//	    		log.info("authenticationToken error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}
		Map<String, Object>  requests = new HashMap<String, Object>();	
		EFmFmAssignRoutePO assignRoutePO=new EFmFmAssignRoutePO();
		assignRoutePO.setAssignRouteId(eFmFmTripAlertsPO.getEfmFmAssignRoute().getAssignRouteId());
		EFmFmAlertTypeMasterPO alertTypeMasterPO=new EFmFmAlertTypeMasterPO();
		alertTypeMasterPO.setAlertId(eFmFmTripAlertsPO.getEfmFmAlertTypeMaster().getAlertId());
		eFmFmTripAlertsPO.setEfmFmAlertTypeMaster(alertTypeMasterPO);
		eFmFmTripAlertsPO.setCreationTime(new Date());
		eFmFmTripAlertsPO.setUpdatedTime(new Date());
		eFmFmTripAlertsPO.setReadFlg("N");
		eFmFmTripAlertsPO.setAlertClosingDescription("No Action");
		eFmFmTripAlertsPO.setAlertOpenStatus("open");
		eFmFmTripAlertsPO.setEfmFmAssignRoute(assignRoutePO);
		eFmFmTripAlertsPO.setUserType("employee");
		eFmFmTripAlertsPO.setStatus("Y");
		iAlertBO.save(eFmFmTripAlertsPO);
		requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + eFmFmTripAlertsPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	* get all the trip Unread alerts on webconsole.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since  15 march 2015
	* 
	* @return EFmFmTripAlertsPO details.
	 * @throws ParseException 
	*/	
	
	
	@POST
	@Path("/unreadAlerts")
	public Response getAllUnReadAlerts(EFmFmTripAlertsPO eFmFmTripAlertsPO) throws IOException{
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
//		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmTripAlertsPO.getUserId()))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 }catch(Exception e){
	    		log.info("authenticationToken error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 
//		 log.info("serviceStart -UserId :" + eFmFmTripAlertsPO.getUserId());
		List<EFmFmTripAlertsPO> unReadAlerts=iAlertBO.getAllUnReadOpenAlerts(eFmFmTripAlertsPO.getBranchId());
		List<Map<String, Object>> allAlertsDetail= new ArrayList<Map<String, Object>>();
		Map<String, Object>  requests = new HashMap<String, Object>();	
		if(!(unReadAlerts.isEmpty())){
			requests.put("tripAlertId", unReadAlerts.get(0).getTripAlertsId());
			requests.put("alertTitle", unReadAlerts.get(0).getEfmFmAlertTypeMaster().getAlertTitle());
			requests.put("assignRouteId", unReadAlerts.get(0).getEfmFmAssignRoute().getAssignRouteId());
			requests.put("alertActualId", unReadAlerts.get(0).getEfmFmAlertTypeMaster().getAlertId());
			if(unReadAlerts.get(0).getEfmFmAlertTypeMaster().getAlertId()==10)
			requests.put("speed", unReadAlerts.get(0).getCurrentSpeed());
			requests.put("alertType", unReadAlerts.get(0).getEfmFmAlertTypeMaster().getAlertType());
			requests.put("vehicleNumber", unReadAlerts.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
			requests.put("driverName", unReadAlerts.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());

			allAlertsDetail.add(requests);		
			unReadAlerts.get(0).setReadFlg("R");
			iAlertBO.update(unReadAlerts.get(0));			
		}
//		 log.info("serviceEnd -UserId :" + eFmFmTripAlertsPO.getUserId());
		return Response.ok(allAlertsDetail, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	* get all the  alerts(Feedback) suggestion and complain from employee device.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-05-07
	* 
	* @return EFmFmTripAlertsPO details.
	 * @throws ParseException 
	*/	
	
	
	@POST
	@Path("/employeeDeviceAlert")
	public Response getAlerstFromEmployeeApp(EFmFmTripAlertsPO eFmFmTripAlertsPO) throws IOException, ParseException{
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" + eFmFmTripAlertsPO.getUserId());		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
			Date alertDate = (Date) formatter.parse(eFmFmTripAlertsPO.getToDate());
			String shiftDate = eFmFmTripAlertsPO.getTime();
			Date shift = shiftFormate.parse(shiftDate);
			java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
			DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
			String todayDate=dateFormatter.format(new Date());

List<EFmFmAlertTypeMasterPO> alertDetail=iAlertBO.getAlertIdFromAlertTitleAndBranchId(eFmFmTripAlertsPO.getAlertTitle(), 1);
	if(!(alertDetail.isEmpty()) && userMasterBO.checkEmployeeUserIdExistOrNot(eFmFmTripAlertsPO.getUserId())){	
	//eFmFmTripAlertsPO.getAlertType() taking this parameter because same alert title may exist in multiple alert type.
		if(alertDetail.get(0).geteFmFmClientBranchPO().getEmployeeFeedbackEmail().equalsIgnoreCase("Yes")){					
		//Email Need to triggered to the selected email id's				
		try {
			log.info("feedback confirmation");
			Thread thread1 = new Thread(new Runnable() {
				@Override
				public void run() {				
					    Client client = ClientBuilder.newClient();	
					 //   String baseUrl =ContextLoader.getContext().getMessage("restcall.url", null, "url", null);
					   String baseUrl="http://localhost:8080/eFmFmFleetWeb/services";
					   String apiCall=baseUrl+"/notifications/feedBackMail/"+todayDate+"/"+eFmFmTripAlertsPO.getAlertType()+"/"+alertDetail.get(0).getAlertTitle()+"/"+eFmFmTripAlertsPO.getEmployeeComments()+"/"+alertDetail.get(0).geteFmFmClientBranchPO().getToEmployeeFeedBackEmail()+"/"+alertDetail.get(0).geteFmFmClientBranchPO().getEmployeeFeedbackEmailId()+"/"+eFmFmTripAlertsPO.getUserId();
					   WebTarget webTarget = client
					     .target(apiCall);
					   Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
					   log.info("response"+response.getStatus());				
				}
			});
			thread1.start();
		} catch (Exception e) {
			log.info("create request  mail Approval confirmation" + e);
		}
	}
		
	if(eFmFmTripAlertsPO.getAlertType().equalsIgnoreCase("Complain")){

		//Alert Id 11 for complain
		eFmFmTripAlertsPO.setEfmFmAlertTypeMaster(alertDetail.get(0));
		eFmFmTripAlertsPO.setEmployeeComments(eFmFmTripAlertsPO.getEmployeeComments());
		eFmFmTripAlertsPO.setUserId(eFmFmTripAlertsPO.getUserId());
		eFmFmTripAlertsPO.setCreationTime((Date) formatter.parse(todayDate));
		eFmFmTripAlertsPO.setUpdatedTime((Date) formatter.parse(todayDate));
		eFmFmTripAlertsPO.setReadFlg("N");
		eFmFmTripAlertsPO.setAssignFeedbackTo("none");
		eFmFmTripAlertsPO.setFeadBackSelectedDateTime(alertDate);
		eFmFmTripAlertsPO.setShiftTime(shiftTime);
		eFmFmTripAlertsPO.setTripType(eFmFmTripAlertsPO.getTripType());
		eFmFmTripAlertsPO.setAlertClosingDescription("No Action");
		eFmFmTripAlertsPO.setAlertOpenStatus("open");
		eFmFmTripAlertsPO.setCurrentSpeed("No");
	//	eFmFmTripAlertsPO.setEfmFmAssignRoute(assignRoutePO);
		eFmFmTripAlertsPO.setUserType("employee");
		eFmFmTripAlertsPO.setStatus("Y");
		iAlertBO.save(eFmFmTripAlertsPO);
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + eFmFmTripAlertsPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}
	else if(eFmFmTripAlertsPO.getAlertType().equalsIgnoreCase("Feedback")){
		//Alert Id 12 for complain
		eFmFmTripAlertsPO.setEfmFmAlertTypeMaster(alertDetail.get(0));
		eFmFmTripAlertsPO.setEmployeeComments(eFmFmTripAlertsPO.getEmployeeComments());
		eFmFmTripAlertsPO.setUserId(eFmFmTripAlertsPO.getUserId());
		eFmFmTripAlertsPO.setCreationTime(new Date());
		eFmFmTripAlertsPO.setUpdatedTime(new Date());
		eFmFmTripAlertsPO.setReadFlg("N");
		eFmFmTripAlertsPO.setAssignFeedbackTo("none");	
		eFmFmTripAlertsPO.setFeadBackSelectedDateTime(alertDate);
		eFmFmTripAlertsPO.setShiftTime(shiftTime);
		eFmFmTripAlertsPO.setTripType(eFmFmTripAlertsPO.getTripType());

		
		eFmFmTripAlertsPO.setAlertClosingDescription("No Action");
		eFmFmTripAlertsPO.setAlertOpenStatus("open");
		eFmFmTripAlertsPO.setCurrentSpeed("No");
	//	eFmFmTripAlertsPO.setEfmFmAssignRoute(assignRoutePO);
		eFmFmTripAlertsPO.setUserType("employee");
		eFmFmTripAlertsPO.setStatus("Y");
		iAlertBO.save(eFmFmTripAlertsPO);
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + eFmFmTripAlertsPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}
	
}	
        // response for Alert Title not exist
	     responce.put("status", "failed");
		 log.info("serviceEnd -UserId :" + eFmFmTripAlertsPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	* get all the  alerts(Feedback) suggestion and complain from employee device.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-05-07
	* 
	* @return EFmFmTripAlertsPO details.
	 * @throws ParseException 
	*/	
	
	
	@POST
	@Path("/getFeedBacks")
	public Response getAllAlertsAndFeedbacks(EFmFmTripAlertsPO eFmFmTripAlertsPO) throws IOException, ParseException{
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
			List<Map<String, Object>> trip = new ArrayList<Map<String, Object>>();			
			log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
			log.info("serviceStart -UserId :" + eFmFmTripAlertsPO.getUserId());
//			try{
//				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmTripAlertsPO.getUserId()))){
//					responce.put("status", "invalidRequest");
//					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//				}
//				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmTripAlertsPO.getUserId());
//				   if (!(userDetail.isEmpty())) {
//					String jwtToken = "";
//					try {
//					 JwtTokenGenerator token = new JwtTokenGenerator();
//					 jwtToken = token.generateToken();
//					 userDetail.get(0).setAuthorizationToken(jwtToken);
//					 userDetail.get(0).setTokenGenerationTime(new Date());
//					 userMasterBO.update(userDetail.get(0));
//					} catch (Exception e) {
//					 log.info("error" + e);
//					}
//			   }
//				}catch(Exception e){
//					log.info("authentication error"+e);
//					responce.put("status", "invalidRequest");
//					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//					}			
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
			Date fromDate = (Date) formatter.parse(eFmFmTripAlertsPO.getFromDate());
			Date toDate = (Date) formatter.parse(eFmFmTripAlertsPO.getToDate());
			DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
			
			if(eFmFmTripAlertsPO.getAlertType().equalsIgnoreCase("open") || eFmFmTripAlertsPO.getAlertType().equalsIgnoreCase("close") || eFmFmTripAlertsPO.getAlertType().equalsIgnoreCase("inprogress") || eFmFmTripAlertsPO.getAlertType().equalsIgnoreCase("all")){
				List<EFmFmTripAlertsPO> alertDetail=new ArrayList<EFmFmTripAlertsPO>();
				if(eFmFmTripAlertsPO.getAlertType().equalsIgnoreCase("all")){
					alertDetail=iAlertBO.getAllTripAlertsAndFeedbacks(fromDate, toDate, 1);		 
				}
				else{
					 alertDetail=iAlertBO.getAllTripAlertsAndFeedbacksBasedOnSelectedDates(fromDate, toDate, 1, eFmFmTripAlertsPO.getAlertType());		 
				}
				if(!(alertDetail.isEmpty())){
					for(EFmFmTripAlertsPO alerts:alertDetail){
						List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(alerts.getUserId());					
						List<EFmFmAlertTypeMasterPO> alertType=iAlertBO.getlertDetailFromAlertIds(alerts.getEfmFmAlertTypeMaster().getAlertId());						
						 Map<String, Object> alert = new HashMap<String, Object>();	
						 
						 alert.put("alertId", alerts.getTripAlertsId());
						 alert.put("alertStatus", alerts.getAlertOpenStatus());
						 alert.put("assignFeedbackTo", alerts.getAssignFeedbackTo());
						 try{
						 alert.put("empId", userDetail.get(0).getEmployeeId());						 
						 alert.put("empName",new String(Base64.getDecoder().decode(userDetail.get(0).getFirstName()), "utf-8"));
						 }catch(Exception e){
							 alert.put("empId", "NA");
							 alert.put("empName", "NA");
							 log.info("error"+e);
						 }
						 alert.put("alertType", alertType.get(0).getAlertType());
						 alert.put("alertTitle", alertType.get(0).getAlertTitle());				 
						 alert.put("creationTime", dateFormatter.format(alerts.getCreationTime()));
						 
						 
						 try{
						 alert.put("alertDate", dateFormatter.format(alerts.getFeadBackSelectedDateTime()));	
						 }
						 catch(Exception e){
							 alert.put("alertDate", "NA");
							 log.info("error"+e);
						 }
						 
						 alert.put("raiting", alerts.getDriverRaiting());

						 alert.put("empDescription", alerts.getEmployeeComments());
						 alert.put("tripType", alerts.getTripType());

						 alert.put("remarks", alerts.getAlertClosingDescription());
						 alert.put("updationDateTime", dateFormatter.format(alerts.getUpdatedTime()));
						 try{
						 alert.put("shiftTime", shiftFormate.format(alerts.getShiftTime()));
					 }
					 catch(Exception e){
						 alert.put("shiftTime", "NA");
						 log.info("error"+e);
					 }
						 
						 trip.add(alert);
					}
					
				}						 
				 responce.put("status", "success");
				 responce.put("data", trip);

				 log.info("serviceEnd -UserId :" + eFmFmTripAlertsPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

			}
		 
			
			
        // response for Alert Title not exist
	     responce.put("status", "failed");
		 log.info("serviceEnd -UserId :" + eFmFmTripAlertsPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	* update the  alerts(Feedback) suggestion and complain from employee device.
	* .   
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-05-08
	* 
	* @return EFmFmTripAlertsPO details.
	 * @throws ParseException 
	*/	
	
	
	@POST
	@Path("/updateFeedBacks")
	public Response updateAlertsAndFeedbacks(EFmFmTripAlertsPO eFmFmTripAlertsPO) throws IOException, ParseException{
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
			log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
			log.info("serviceStart -UserId :" + eFmFmTripAlertsPO.getUserId());
//			try{
//				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmTripAlertsPO.getUserId()))){
//					responce.put("status", "invalidRequest");
//					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//				}
//				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmTripAlertsPO.getUserId());
//				   if (!(userDetail.isEmpty())) {
//					String jwtToken = "";
//					try {
//					 JwtTokenGenerator token = new JwtTokenGenerator();
//					 jwtToken = token.generateToken();
//					 userDetail.get(0).setAuthorizationToken(jwtToken);
//					 userDetail.get(0).setTokenGenerationTime(new Date());
//					 userMasterBO.update(userDetail.get(0));
//					} catch (Exception e) {
//					 log.info("error" + e);
//					}
//			   }
//				}catch(Exception e){
//					log.info("authentication error"+e);
//					responce.put("status", "invalidRequest");
//					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//					}				
			List<EFmFmTripAlertsPO> alertDetail=iAlertBO.getParticularAlertDetailFromAlertId(eFmFmTripAlertsPO.getTripAlertsId());
			if(!(alertDetail.isEmpty())){
				alertDetail.get(0).setAssignFeedbackTo(eFmFmTripAlertsPO.getAssignFeedbackTo());
				alertDetail.get(0).setAlertClosingDescription(eFmFmTripAlertsPO.getAlertClosingDescription());
				alertDetail.get(0).setAlertOpenStatus(eFmFmTripAlertsPO.getAlertOpenStatus());
				alertDetail.get(0).setUpdatedTime(new Date());
				iAlertBO.update(alertDetail.get(0));
			    responce.put("status", "success");
				log.info("serviceEnd -UserId :" + eFmFmTripAlertsPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
        // response for Alert Title not exist
	     responce.put("status", "failed");
		 log.info("serviceEnd -UserId :" + eFmFmTripAlertsPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	
	}
