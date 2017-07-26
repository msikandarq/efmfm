package com.newtglobal.eFmFmFleet.services;

import java.io.UnsupportedEncodingException;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/app")
@Consumes("application/json")
@Produces("application/json")
public class AppDownloadService {
	
    private static Log log = LogFactory.getLog(AppDownloadService.class);
    
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();



	/*
	 * 1.Get All Users who have the install the Employee Application.1.App Download Count
	 */
	
	
	
    @POST
    @Path("/appDownload")
    public Response getAppDownloadedUsers(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) throws UnsupportedEncodingException {  	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		
		 Map<String, Object> responce = new HashMap<String, Object>();
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
	    		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
		   if (!(userDetailToken.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
		     userDetailToken.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetailToken.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		 }
		 
   	    log.info("appDownload serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
		List<Map<String, Object>> allusers = new ArrayList<Map<String, Object>>();
        List<EFmFmUserMasterPO> userDetail = userMasterBO.getAppDownloadUsersFromBranchId(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getStartPgNo(),
        		eFmFmDeviceMasterPO.getEndPgNo());
   	 int totalCount=0;
	 List<EFmFmUserMasterPO> userDetailCount=null;
	 if(eFmFmDeviceMasterPO.getStartPgNo()==0){
		 userDetailCount = userMasterBO.getAppDownloadUsersFromBranchId(eFmFmDeviceMasterPO.getCombinedFacility());
	    totalCount=userDetailCount.size();
	 }      
        
        if (!(userDetail.isEmpty())) {
        	for(EFmFmUserMasterPO user:userDetail){
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("userId", user.getUserId());
				requests.put("employeeId", user.getEmployeeId());
				requests.put("emailId", new String(Base64.getDecoder().decode(user.getEmailId()), "utf-8"));
				requests.put("userName", user.getUserName());
				try{
					requests.put("deviceType", user.getDeviceType());
					}catch(Exception e){
						requests.put("deviceType", "NO");
					}				
				requests.put("employeeAddress", new String(Base64.getDecoder().decode(user.getAddress()), "utf-8"));				
				requests.put("employeeLatiLongi",user.getLatitudeLongitude());
				requests.put("totalSummary",userDetail.size());
				requests.put("totalRecordCount",totalCount);
				requests.put("employeeName", new String(Base64.getDecoder().decode(user.getFirstName()), "utf-8"));
				requests.put("mobileNumber", new String(Base64.getDecoder().decode(user.getMobileNumber()), "utf-8"));
				allusers.add(requests);
        	}        	
        }
   	      log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
          return Response.ok(allusers, MediaType.APPLICATION_JSON).build();
    }
    
    /*
	 * 2.Get All Users who have not install the Employee Application till now.2.Yet To App Downloads
	 */
    
    @POST
    @Path("/appNonDownload")
    public Response getNonAppDownloadedUsers(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) throws UnsupportedEncodingException {  	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");		
		 Map<String, Object> responce = new HashMap<String, Object>();
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){	
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
		   if (!(userDetailToken.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
		     userDetailToken.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetailToken.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		 }
		 }
   	    log.info("appNonDownload serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
		List<Map<String, Object>> allusers = new ArrayList<Map<String, Object>>();
		
		 List<EFmFmUserMasterPO> userDetail = userMasterBO.getWithOutAppDownloadUsersFromBranchId(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getStartPgNo(),eFmFmDeviceMasterPO.getEndPgNo());
		 int totalCount=0;
		 List<EFmFmUserMasterPO> userDetailCount=null;
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){
		  userDetailCount= userMasterBO.getWithOutAppDownloadUsersFromBranchId(eFmFmDeviceMasterPO.getCombinedFacility());
		  totalCount=userDetailCount.size();
		 }
        if (!(userDetail.isEmpty())) {
        	for(EFmFmUserMasterPO user:userDetail){
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("userId", user.getUserId());
				requests.put("employeeId", user.getEmployeeId());
				requests.put("emailId", new String(Base64.getDecoder().decode(user.getEmailId()), "utf-8"));
				requests.put("userName", user.getUserName());
				try{
					requests.put("deviceType", user.getDeviceType());
					}catch(Exception e){
						requests.put("deviceType", "NO");
					}
				requests.put("employeeAddress", new String(Base64.getDecoder().decode(user.getAddress()), "utf-8"));
				requests.put("employeeLatiLongi",user.getLatitudeLongitude());
				requests.put("totalCount",userDetail.size());
				requests.put("totalRecordCount",totalCount);
				requests.put("employeeName", new String(Base64.getDecoder().decode(user.getFirstName()), "utf-8"));
				requests.put("mobileNumber", new String(Base64.getDecoder().decode(user.getMobileNumber()), "utf-8"));
				allusers.add(requests);
        	}        	
        }
   	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
        return Response.ok(allusers, MediaType.APPLICATION_JSON).build();
    }
    
    
    /*
  	 * 3.Get All Users who had done the GeoCoading for him/her self.3.GEO Coded Employee 
  	 */
    
    @POST
    @Path("/getAllGeocodedEmployee")
    public Response getAppDownloadedButNoGeoCodeUsers(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) throws UnsupportedEncodingException {  	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
		   if (!(userDetailToken.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
		     userDetailToken.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetailToken.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		   
		 }
   	    log.info("appDownloadNoGeo serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
		List<Map<String, Object>> allusers = new ArrayList<Map<String, Object>>();
		
		
		
		
        List<EFmFmUserMasterPO> userDetail = userMasterBO.getAllGeoCodedEmployeesList(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getStartPgNo(),eFmFmDeviceMasterPO.getEndPgNo());
        
        int totalCount=0;
		 List<EFmFmUserMasterPO> userDetailCount=null;
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){
		  userDetailCount= userMasterBO.getAllGeoCodedEmployeesList(eFmFmDeviceMasterPO.getCombinedFacility());
		  totalCount=userDetailCount.size();
		 }
        log.info("userDetail"+userDetail.size());
        if (!(userDetail.isEmpty())) {
        	for(EFmFmUserMasterPO user:userDetail){
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("userId", user.getUserId());
				requests.put("employeeId", user.getEmployeeId());
				requests.put("emailId", new String(Base64.getDecoder().decode(user.getEmailId()), "utf-8"));
				requests.put("userName", user.getUserName());
				try{
					requests.put("deviceType", user.getDeviceType());
					}catch(Exception e){
						requests.put("deviceType", "NO");
					}
				requests.put("employeeAddress", new String(Base64.getDecoder().decode(user.getAddress()), "utf-8"));
				requests.put("employeeLatiLongi",user.getLatitudeLongitude());
				requests.put("totalCount",userDetail.size());
				requests.put("totalRecordCount",totalCount);
				requests.put("employeeName", new String(Base64.getDecoder().decode(user.getFirstName()), "utf-8"));
				requests.put("mobileNumber", new String(Base64.getDecoder().decode(user.getMobileNumber()), "utf-8"));
				allusers.add(requests);
        	}        	
        }
   	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
        return Response.ok(allusers, MediaType.APPLICATION_JSON).build();
    }
    
    
    /*
  	 * 3.Get All Users who had not done the GeoCoading for him/her self.3.NON GEO Coded Employee 
  	 */
    
    @POST
    @Path("/getAllNonGeocodedEmployee")
    public Response getNonGeoCodingEmployee(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) throws UnsupportedEncodingException {  	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){	 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
		   if (!(userDetailToken.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
		     userDetailToken.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetailToken.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
         }
   	    log.info("appDownloadNoGeo serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
		List<Map<String, Object>> allusers = new ArrayList<Map<String, Object>>();
        List<EFmFmUserMasterPO> userDetail = userMasterBO.getAllNonGeoCodedEmployeesList(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getStartPgNo(),eFmFmDeviceMasterPO.getEndPgNo());
        
        int totalCount=0;
		 List<EFmFmUserMasterPO> userDetailCount=null;
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){
		  userDetailCount= userMasterBO.getAllNonGeoCodedEmployeesList(eFmFmDeviceMasterPO.getCombinedFacility());
		  totalCount=userDetailCount.size();
		 }
        log.info("userDetail"+userDetail.size());
        if (!(userDetail.isEmpty())) {
        	for(EFmFmUserMasterPO user:userDetail){
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("userId", user.getUserId());
				requests.put("employeeId", user.getEmployeeId());
				requests.put("emailId", new String(Base64.getDecoder().decode(user.getEmailId()), "utf-8"));
				requests.put("userName", user.getUserName());
				try{
					requests.put("deviceType", user.getDeviceType());
					}catch(Exception e){
						requests.put("deviceType", "NO");
					}
				requests.put("employeeAddress", new String(Base64.getDecoder().decode(user.getAddress()), "utf-8"));
				requests.put("employeeLatiLongi",user.getLatitudeLongitude());
				requests.put("totalCount",userDetail.size());
				requests.put("totalRecordCount",totalCount);
				requests.put("employeeName", new String(Base64.getDecoder().decode(user.getFirstName()), "utf-8"));
				requests.put("mobileNumber", new String(Base64.getDecoder().decode(user.getMobileNumber()), "utf-8"));
				allusers.add(requests);
        	}        	
        }
   	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
        return Response.ok(allusers, MediaType.APPLICATION_JSON).build();
    }
    
    /*
  	 * 3.Get All Users who have install the Employee Application but they did't did the GeoCoading.3.GEO Coded Employee 
  	 */
    
    @POST
    @Path("/appDownloadNoGeo")
    public Response getAppappDownloadNoGeo(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) throws UnsupportedEncodingException {  	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
		   if (!(userDetailToken.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
		     userDetailToken.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetailToken.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		 }
   	    log.info("appDownloadNoGeo serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
		List<Map<String, Object>> allusers = new ArrayList<Map<String, Object>>();
        List<EFmFmUserMasterPO> userDetail = userMasterBO.getAppDownloadedButNoGeoCodedFromBranchId(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getStartPgNo(),eFmFmDeviceMasterPO.getEndPgNo());
        
          int totalCount=0;
		 List<EFmFmUserMasterPO> userDetailCount=null;
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){
		 userDetailCount = userMasterBO.getAppDownloadedButNoGeoCodedFromBranchId(eFmFmDeviceMasterPO.getCombinedFacility());
		  totalCount=userDetailCount.size();
		 }
        log.info("userDetail"+userDetail.size());
        if (!(userDetail.isEmpty())) {
        	for(EFmFmUserMasterPO user:userDetail){
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("userId", user.getUserId());
				requests.put("employeeId", user.getEmployeeId());
				requests.put("emailId", new String(Base64.getDecoder().decode(user.getEmailId()), "utf-8"));
				requests.put("userName", user.getUserName());
				try{
					requests.put("deviceType", user.getDeviceType());
					}catch(Exception e){
						requests.put("deviceType", "NO");
					}
				requests.put("employeeAddress", new String(Base64.getDecoder().decode(user.getAddress()), "utf-8"));
				requests.put("employeeLatiLongi",user.getLatitudeLongitude());
				requests.put("totalCount",userDetail.size());
				requests.put("totalRecordCount",totalCount);
				requests.put("employeeName", new String(Base64.getDecoder().decode(user.getFirstName()), "utf-8"));
				requests.put("mobileNumber", new String(Base64.getDecoder().decode(user.getMobileNumber()), "utf-8"));
				allusers.add(requests);
        	}        	
        }
   	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
        return Response.ok(allusers, MediaType.APPLICATION_JSON).build();
    }
    
    /*
   	 * 4.Get All Users who have install the Employee Application and did the GeoCoading 
   	 */
    
    @POST
    @Path("/appDownloadGeoCode")
    public Response getAppDownloadedAndGeoCodedUsers(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) throws UnsupportedEncodingException {  	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		
		 Map<String, Object> responce = new HashMap<String, Object>();
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){	
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
		   if (!(userDetailToken.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
		     userDetailToken.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetailToken.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
		 }
   	    log.info("appDownloadGeoCode serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
		List<Map<String, Object>> allusers = new ArrayList<Map<String, Object>>();
        List<EFmFmUserMasterPO> userDetail = userMasterBO.getAppDownloadedAndGeoCodedUserFromBranchId
        		(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getStartPgNo(),eFmFmDeviceMasterPO.getEndPgNo());
      
         int totalCount=0;
		 List<EFmFmUserMasterPO> userDetailCount=null;
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){
		  userDetailCount = userMasterBO.getAppDownloadedAndGeoCodedUserFromBranchId(eFmFmDeviceMasterPO.getCombinedFacility());
		  totalCount=userDetailCount.size();
		 }
        if (!(userDetail.isEmpty())) {
        	for(EFmFmUserMasterPO user:userDetail){
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("userId", user.getUserId());
				requests.put("employeeId", user.getEmployeeId());
				requests.put("emailId", new String(Base64.getDecoder().decode(user.getEmailId()), "utf-8"));
				requests.put("userName", user.getUserName());				
				try{
				requests.put("deviceType", user.getDeviceType());
				}catch(Exception e){
					requests.put("deviceType", "NO");
				}
				requests.put("employeeName", new String(Base64.getDecoder().decode(user.getFirstName()), "utf-8"));
				requests.put("employeeAddress", new String(Base64.getDecoder().decode(user.getAddress()), "utf-8"));
				requests.put("employeeLatiLongi",user.getLatitudeLongitude());
				requests.put("totalRecordCount",totalCount);
				requests.put("totalCount",userDetail.size());
				requests.put("mobileNumber", new String(Base64.getDecoder().decode(user.getMobileNumber()), "utf-8"));
				allusers.add(requests);
        	}        	
        }
   	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
        return Response.ok(allusers, MediaType.APPLICATION_JSON).build();
    }
    
    /*
	 * 2.Get All Users who have not install the Employee Application but geo coded through web console.
	 */
    
    @POST
    @Path("/appNonDownloadAndGeoCoded")
    public Response getNonAppDownloadedButGeoCoded(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) throws UnsupportedEncodingException {  	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");		
		 Map<String, Object> responce = new HashMap<String, Object>();
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
		   if (!(userDetailToken.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
		     userDetailToken.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetailToken.get(0));
		    } catch (Exception e) {
		     log.info("error" + e);
		    }
		   }
         }
   	    log.info("appNonDownload serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
		List<Map<String, Object>> allusers = new ArrayList<Map<String, Object>>();
        List<EFmFmUserMasterPO> userDetail = userMasterBO.getWithOutAppDownloadUsersButWebGeocodedFromBranchId(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getStartPgNo(),eFmFmDeviceMasterPO.getEndPgNo());
    
        int totalCount=0;
		 List<EFmFmUserMasterPO> userDetailCount=null;
		 if(eFmFmDeviceMasterPO.getStartPgNo()==0){
			 userDetailCount = userMasterBO.getWithOutAppDownloadUsersButWebGeocodedFromBranchId(eFmFmDeviceMasterPO.getCombinedFacility());
		  totalCount=userDetailCount.size();
		 }
        if (!(userDetail.isEmpty())) {
        	for(EFmFmUserMasterPO user:userDetail){
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("userId", user.getUserId());
				requests.put("employeeId", user.getEmployeeId());
				requests.put("emailId", new String(Base64.getDecoder().decode(user.getEmailId()), "utf-8"));
				requests.put("userName", user.getUserName());
				try{
					requests.put("deviceType", user.getDeviceType());
					}catch(Exception e){
						requests.put("deviceType", "NO");
					}
				
				requests.put("employeeAddress", new String(Base64.getDecoder().decode(user.getAddress()), "utf-8"));
				requests.put("employeeLatiLongi",user.getLatitudeLongitude());
				requests.put("totalCount",userDetail.size());
				requests.put("totalRecordCount",totalCount);
				requests.put("employeeName", new String(Base64.getDecoder().decode(user.getFirstName()), "utf-8"));
				requests.put("mobileNumber", new String(Base64.getDecoder().decode(user.getMobileNumber()), "utf-8"));
				allusers.add(requests);
        	}        	
        }
   	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
        return Response.ok(allusers, MediaType.APPLICATION_JSON).build();
    }
}


