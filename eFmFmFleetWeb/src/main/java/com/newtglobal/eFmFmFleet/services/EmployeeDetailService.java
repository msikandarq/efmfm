package com.newtglobal.eFmFmFleet.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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

import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IEmployeeDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaNodalMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientRouteMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientUserRolePO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLocationMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmZoneMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/employee")
@Consumes("application/json")
@Produces("application/json")
public class EmployeeDetailService {

    private static Log log = LogFactory.getLog(EmployeeDetailService.class);
    
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();	
	
	  /*
     * starting Particular Deriver Detail service for
     */

    @POST
    @Path("/employeeData")
    public Response getParticularDriverDetail(EmployeeJSON userDetails) {  	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");	
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");

		Map<String, Object> responce = new HashMap<String, Object>();
        List<Map<String, Object>> dataStatus = new ArrayList<Map<String, Object>>();
		try{
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		List<EFmFmUserMasterPO> employeeDetails=userDetails.getEmpData();
		
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
		if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),8786))){
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(8786);	
	 	 if((userDetail.get(0).getTokenGenerationTime().getTime()+120000) < new Date().getTime() ){	
				responce.put("status", "tokenExpiry");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 	 }

	 	 else if (!(userDetail.isEmpty())) {
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
		
		
		}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		
        if(employeeDetails.size()>200){
        	responce.put("status", "ObjectLength");
            return Response.ok(requests, MediaType.APPLICATION_JSON).build();	
        }
        
    	StringBuffer temp=new StringBuffer("PleaseCheck");
	    String status="input";
	     boolean dataValid=true;
	     boolean reason=true;
	     boolean finalStatus=true;



     for(EFmFmUserMasterPO userDetail:employeeDetails){
 		Map<String, Object> userData = new HashMap<String, Object>();
    	reason=true;
        	log.info(userDetail.getEmployeeId());
        	log.info(userDetail.getUserName());
        	log.info(userDetail.getMobileNumber());
        	log.info(userDetail.getEmailId());      
        	userData.put("employeeId", userDetail.getEmployeeId());        

        if(employeeDetailBO.doesCommanEmployeeIdExist(userDetail.getEmployeeId())){
       // 	userData.put("employeeId", userDetail.getEmployeeId());        
       // 	userData.put("status", "updated");
      //  	dataValid=false;
        }
        else if(employeeDetailBO.doesCommanUseNameExist(userDetail.getUserName())){
        	userData.put("status", "fail");
        	userData.put("reason", "username of this employee already exist with other employee id");
        	reason=false;
        	dataValid=false;
        	finalStatus=false;
        }
        else if(employeeDetailBO.doesCommanMobileNumberExist(userDetail.getMobileNumber())){
        	userData.put("status", "fail");
        	userData.put("reason", "mobile number of this employee already exist with other employee id");	
        	reason=false;
            dataValid=false;
        	finalStatus=false;

        }
        
		else if(employeeDetailBO.doesCommanEmailIdExist(userDetail.getEmailId())){
        	userData.put("status", "fail");
        	reason=false;
        	userData.put("reason", "emailId of this employee already exist with other employee id");	
        	dataValid=false;
        	finalStatus=false;

 		}
        
		else if((userDetail.getEmployeeId().isEmpty()) || userDetail.getUserName().isEmpty() || userDetail.getMobileNumber().isEmpty() || userDetail.getEmailId().isEmpty()){
			userData.put("status", "fail");
        	reason=false;
        	userData.put("reason", "Please fill all required empty fields");	
        	dataValid=false;
        	finalStatus=false;

		}
        if(dataValid){
            employeeDetailBO.save(userDetail);
        List<EFmFmUserMasterPO> userExistCheck=employeeDetailBO.getEmpDetailsFromEmployeeId(userDetail.getEmployeeId());
        log.info("userExistCheck"+userExistCheck.size());
        if(userExistCheck.isEmpty()){
			userData.put("status", "fail");
        	reason=false;
        	userData.put("reason", "Please fill all required empty fields");	
        	dataValid=false;
        	finalStatus=false;


 //       	userData.put("status", "inserted");
 //       	userData.put("reason", "");	
        }
        }
        
        if(!(reason)){
        dataStatus.add(userData);
        }
        
        }

     if(!(finalStatus)){
     	responce.put("status", "fail");
     }
     else{
     	responce.put("status", "success");

     }
    	responce.put("data", dataStatus);
    }
    catch(Exception e){
    log.info("errror"+e);	
	responce.put("status", "fail");
	responce.put("data", dataStatus);

    }
    	
    	

         return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
	/*
	 * 
	 * empUpdateToken from Oracle HRMS Team
	 * 
	 */	
	@POST
	@Path("/getToken")
	public Response updateEmployeeAppTokenForHrmsTeam(secretKeyJson secretKeyJson) throws IOException{		
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
        String secretKey = ContextLoader.getContext().getMessage("user.secretKey", null, "secretKey", null);
       if(!(httpRequest.getHeader("secretKey").equals(secretKey))){
			responce.put("status", "invalidSecretKey");		
			return Response.ok(responce, MediaType.APPLICATION_JSON).build(); 
       }
		
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));		
 		List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(8786);
			if (!(userDetail.isEmpty())) {
	 	if(userDetail.get(0).getAuthorizationToken()==null){							
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
                responce.put("authenticationToken", jwtToken);
			}
	 	else if((userDetail.get(0).getTokenGenerationTime().getTime()+120000) < new Date().getTime() ){							
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
            responce.put("authenticationToken", jwtToken);
		}
	 	else{
            responce.put("authenticationToken", userDetail.get(0).getAuthorizationToken());

		}
	 	}			
			responce.put("status", "success");		
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}


    @POST
    @Path("/details")
    public Response allEmployeeDetails(EFmFmUserMasterPO eFmFmUserMasterPO) throws UnsupportedEncodingException {   	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
	   	  log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
  	    if(eFmFmUserMasterPO.getStartPgNo()==0){
		try{
		if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMasterPO.getUserId());
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
		
		
		}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
  	    }
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        long l1 = System.currentTimeMillis();
        log.info("time" + System.currentTimeMillis());
        List<EFmFmUserMasterPO> employeeMasterData = employeeDetailBO
                .getAllEmployeeDetailsFromBranchId(eFmFmUserMasterPO.getCombinedFacility(),eFmFmUserMasterPO.getStartPgNo(),eFmFmUserMasterPO.getEndPgNo());
        log.info("qwery done"+employeeMasterData.size());
        List<Map<String, Object>> employeeProfile = new ArrayList<Map<String, Object>>();
        if (!(employeeMasterData.isEmpty())) {
            for (EFmFmUserMasterPO empProfileDetails : employeeMasterData) {
                Map<String, Object> profileValues = new HashMap<String, Object>();
                profileValues.put("employeeId", empProfileDetails.getEmployeeId());
                profileValues.put("userId", empProfileDetails.getUserId());
                profileValues.put("userName", empProfileDetails.getUserName());

                profileValues.put("employeeName",new String(Base64.getDecoder().decode(empProfileDetails.getFirstName()), "utf-8"));
                profileValues.put("facilityName",empProfileDetails.geteFmFmClientBranchPO().getBranchName());
                try{
                profileValues.put("tripType",empProfileDetails.getTripType());
                }catch(Exception e){
                    profileValues.put("tripType","B");
                }
                profileValues.put("locationStatus", empProfileDetails.getLocationStatus());
                profileValues.put("physicallyChallenged", new String(Base64.getDecoder().decode(empProfileDetails.getPhysicallyChallenged()), "utf-8"));
                profileValues.put("isInjured", new String(Base64.getDecoder().decode(empProfileDetails.getIsInjured()), "utf-8"));
                profileValues.put("pragnentLady", new String(Base64.getDecoder().decode(empProfileDetails.getPragnentLady()), "utf-8"));  
                if(null!=empProfileDetails.getIsVIP())
                profileValues.put("isVIP", new String(Base64.getDecoder().decode(empProfileDetails.getIsVIP()), "utf-8"));              

                profileValues.put("areaId", empProfileDetails.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaId());
                profileValues.put("areaName", empProfileDetails.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());   
                
                profileValues.put("routeId", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
                profileValues.put("routeName", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());   

                profileValues.put("nodalPointId", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointId());
                profileValues.put("nodalPointName", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());
                profileValues.put("startPgNo",(eFmFmUserMasterPO.getStartPgNo()+eFmFmUserMasterPO.getEndPgNo()));

                profileValues.put("employeeAddress", new String(Base64.getDecoder().decode(empProfileDetails.getAddress()), "utf-8"));               
                profileValues.put("employeeLatiLongi", empProfileDetails.getLatitudeLongitude());
                profileValues.put("employeeDesignation", new String(Base64.getDecoder().decode(empProfileDetails.getEmployeeDesignation()), "utf-8"));               
               try{
            	   log.debug("employeeId"+empProfileDetails.getEmployeeId());
            	   if(null !=empProfileDetails.getEmployeeDepartment()){
            		   profileValues.put("employeeDepartment", new String(Base64.getDecoder().decode(empProfileDetails.getEmployeeDepartment()), "utf-8"));
            	   }else{
            		   profileValues.put("employeeDepartment", "");
            	   }
               }catch(Exception e){
                   profileValues.put("employeeDepartment", "");
                  e.printStackTrace();
                  log.info("erroe"+e);
               }
                profileValues.put("status", empProfileDetails.getStatus());
                profileValues.put("weekOffs", empProfileDetails.getWeekOffDays());
                profileValues.put("emailId", new String(Base64.getDecoder().decode(empProfileDetails.getEmailId()), "utf-8"));
                if (new String(Base64.getDecoder().decode(empProfileDetails.getGender()), "utf-8").equalsIgnoreCase("Male")) {
                    profileValues.put("employeeGender", 1);
                } else {
                    profileValues.put("employeeGender", 2);
                }

                profileValues.put("mobileNumber", new String(Base64.getDecoder().decode(empProfileDetails.getMobileNumber()), "utf-8"));
                try{
                	if(empProfileDetails.getHostMobileNumber()==null){
                		 profileValues.put("hostMobileNumber","N/A");
                	}else{
                		 profileValues.put("hostMobileNumber",new String(Base64.getDecoder().decode(empProfileDetails.getHostMobileNumber()), "utf-8"));
                	}                   
                   }catch(Exception e){
                      profileValues.put("hostMobileNumber","N/A");
                	   log.info("erroe"+e);
                   }            
                   employeeProfile.add(profileValues);
            }
        }
        long l2 = (System.currentTimeMillis() - l1);
        log.info("total Excution time" + l2);
   	 	log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(employeeProfile, MediaType.APPLICATION_JSON).build();
    }
    

	/*
	 * 
	 * Connect to driver from employee App 
	 * 
	 */	
	@POST
	@Path("/connectDriver")
	public Response connectToDriver(EFmFmDriverMasterPO eFmFmDriverMasterPO) throws IOException{		
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" + eFmFmDriverMasterPO.getUserId());		
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));		
//		try{
// 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),eFmFmDriverMasterPO.getUserId()))){
// 				responce.put("status", "invalidRequest");
// 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
// 			}
// 		
 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDriverMasterPO.getUserId());
// 			if (!(userDetail.isEmpty())) {
// 				String jwtToken = "";
// 				try {
// 					JwtTokenGenerator token = new JwtTokenGenerator();
// 					jwtToken = token.generateToken();
// 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
// 					userDetail.get(0).setMobTokenGenerationTime(new Date());
// 					userMasterBO.update(userDetail.get(0));
// 				} catch (Exception e) {
// 					log.info("error" + e);
// 				}
//                responce.put("token", jwtToken);
// 			}
// 		
// 		}catch(Exception e){
// 				log.info("authentication error"+e);
// 				responce.put("status", "invalidRequest");
// 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
// 			}
		
		List<EFmFmEmployeeTripDetailPO> empTripDetails = iCabRequestBO.getAllocatedTripDetails(
				eFmFmDriverMasterPO.getUserId(), eFmFmDriverMasterPO.getCombinedFacility());
		if(userDetail.get(0).geteFmFmClientBranchPO().getEmployeeCallToDriver().equalsIgnoreCase("ivr")){
		if(!(empTripDetails.isEmpty())){			
			try {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
					public void run() {
            			try {
            				String ivrUrl = ContextLoader.getContext().getMessage("click.call", null, "call", null);	
            				ivrUrl=ivrUrl+new String(Base64.getDecoder().decode(empTripDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber()), "utf-8")+"&caller="+empTripDetails.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber();
							log.info("API Url"+ivrUrl);
            				clickToCallDriver(ivrUrl);
						} catch (IOException e) {
							log.info("driver error"+e);							
						}
                    }
                });
                thread1.start();
            } catch (Exception e) {
                log.info("Connect to driver" + e);
    			responce.put("status", "checkIvr");
    			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
			responce.put("status", "success");
			log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		else{
			responce.put("status", "notrip");		
			log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
	}
		responce.put("status", "success");		
		log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	/*
	 * 
	 * Connect to transport from employee App 
	 * 
	 */	
	@POST
	@Path("/connectTransport")
	public Response connectToTransport(EFmFmDriverMasterPO eFmFmDriverMasterPO) throws IOException{		
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" + eFmFmDriverMasterPO.getUserId());		
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));		
//		try{
// 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),eFmFmDriverMasterPO.getUserId()))){
// 				responce.put("status", "invalidRequest");
// 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
// 			}
// 		
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDriverMasterPO.getUserId());
// 			if (!(userDetail.isEmpty())) {
// 				String jwtToken = "";
// 				try {
// 					JwtTokenGenerator token = new JwtTokenGenerator();
// 					jwtToken = token.generateToken();
// 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
// 					userDetail.get(0).setMobTokenGenerationTime(new Date());
// 					userMasterBO.update(userDetail.get(0));
// 				} catch (Exception e) {
// 					log.info("error" + e);
// 				}
//                responce.put("token", jwtToken);
// 			}
// 		
// 		}catch(Exception e){
// 				log.info("authentication error"+e);
// 				responce.put("status", "invalidRequest");
// 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
// 			}
		if(userDetail.get(0).geteFmFmClientBranchPO().getEmployeeCallToDriver().equalsIgnoreCase("ivr")){
			 log.info("Ivr -call Intiated :" + eFmFmDriverMasterPO.getUserId());		
			try {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
					public void run() {
            			try {
            				String ivrUrl = ContextLoader.getContext().getMessage("click.call", null, "call", null);	
            				ivrUrl=ivrUrl+new String(Base64.getDecoder().decode(userDetail.get(0).getMobileNumber()), "utf-8")+"&caller="+userDetail.get(0).geteFmFmClientBranchPO().getTransportContactNumberForMsg();
							log.info("API Url"+ivrUrl);
            				clickToCallDriver(ivrUrl);
						} catch (IOException e) {
							log.info("transport error"+e);
						}
                    }
                });
                thread1.start();
            } catch (Exception e) {
                log.info("Connect to transport " + e);
            }
		}else{
            log.info("Connect to transport intiated");
		}
			responce.put("status", "success");
			log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
	
    
    
    /*
	 * 
	 * report a bug from  from Android employee App 
	 * 
	 */	
	@POST
	@Path("/reportBugFromAndroid")
	public Response reportBugFromAndroidEmployeeApp(EFmFmDriverMasterPO eFmFmDriverMasterPO) throws IOException{		
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" + eFmFmDriverMasterPO.getUserId());		
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));		
//		try{
// 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),eFmFmDriverMasterPO.getUserId()))){
// 				responce.put("status", "invalidRequest");
// 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
// 			}
// 		
 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDriverMasterPO.getUserId());
// 			if (!(userDetail.isEmpty())) {
// 				String jwtToken = "";
// 				try {
// 					JwtTokenGenerator token = new JwtTokenGenerator();
// 					jwtToken = token.generateToken();
// 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
// 					userDetail.get(0).setMobTokenGenerationTime(new Date());
// 					userMasterBO.update(userDetail.get(0));
// 				} catch (Exception e) {
// 					log.info("error" + e);
// 				}
//                responce.put("token", jwtToken);
// 			}
// 		
// 		}catch(Exception e){
// 				log.info("authentication error"+e);
// 				responce.put("status", "invalidRequest");
// 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
// 			}
		
			DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

			String todayDate=dateFormatter.format(new Date());

 			
 			if(userDetail.get(0).geteFmFmClientBranchPO().getEmployeeAppReportBug().equalsIgnoreCase("Yes")){					
 				//Email Need to triggered to the selected email id's				
 				try {
 					log.info("Employee reported a bug"+eFmFmDriverMasterPO.getDeviceModel());
 					Thread thread1 = new Thread(new Runnable() {
 						String bugType="bug";
 						String toBugEmail="ksarfraz@newtglobal.com";
 						@Override
 						public void run() {				
 							    Client client = ClientBuilder.newClient();	
 							 //   String baseUrl =ContextLoader.getContext().getMessage("restcall.url", null, "url", null);
 							   String baseUrl="http://localhost:8080/eFmFmFleetWeb/services";
 							   String apiCall=baseUrl+"/notifications/bugEmailFromEmployeeApp/"+todayDate+"/"+bugType+"/"+eFmFmDriverMasterPO.getRemarks()+"/"+eFmFmDriverMasterPO.getDeviceModel()+"/"+toBugEmail+"/"+userDetail.get(0).geteFmFmClientBranchPO().getReportBugEmailIds()+"/"+eFmFmDriverMasterPO.getUserId();
 							   WebTarget webTarget = client
 							     .target(apiCall);
 							   Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
 							   log.info("response"+response.getStatus());				
 						}
 					});
 					thread1.start();
 				} catch (Exception e) {
 					log.info("report a bug from EmployeeApp" + e);
 				}
 			}
 				
		
		responce.put("status", "success");		
		log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
    
	
	 /*
		 * 
		 * report a bug from  from IOS employee App 
		 * 
		 */	
		@POST
		@Path("/reportBugFromIos")
		public Response reportBugFromIosEmployeeApp(EFmFmDriverMasterPO eFmFmDriverMasterPO) throws IOException{		
			IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
			ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
			Map<String, Object> responce = new HashMap<String, Object>();
			 log.info("serviceStart -UserId :" + eFmFmDriverMasterPO.getUserId());		
			log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));		
//			try{
//	 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),eFmFmDriverMasterPO.getUserId()))){
//	 				responce.put("status", "invalidRequest");
//	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//	 			}
//	 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDriverMasterPO.getUserId());
//	 			if (!(userDetail.isEmpty())) {
//	 				String jwtToken = "";
//	 				try {
//	 					JwtTokenGenerator token = new JwtTokenGenerator();
//	 					jwtToken = token.generateToken();
//	 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
//	 					userDetail.get(0).setMobTokenGenerationTime(new Date());
//	 					userMasterBO.update(userDetail.get(0));
//	 				} catch (Exception e) {
//	 					log.info("error" + e);
//	 				}
//	                responce.put("token", jwtToken);
//	 			}
//	 		
//	 		}catch(Exception e){
//	 				log.info("authentication error"+e);
//	 				responce.put("status", "invalidRequest");
//	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//	 			}
			
			
			responce.put("status", "success");		
			log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
	    
		 /*
		 * 
		 * empUpdateToken from employee App
		 * 
		 */	
		@POST
		@Path("/empUpdateToken")
		public Response updateEmployeeAppToken(EFmFmDriverMasterPO eFmFmDriverMasterPO) throws IOException{		
			IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
			Map<String, Object> responce = new HashMap<String, Object>();
			log.info("serviceStart -UserId :" + eFmFmDriverMasterPO.getUserId());		
			log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));		
	 		List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDriverMasterPO.getUserId());
 			if (!(userDetail.isEmpty())) {
		 	if(userDetail.get(0).getMobAuthorizationToken()==null){							
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
 			}else{
                responce.put("token", userDetail.get(0).getMobAuthorizationToken());

			}
		 	}			
 			responce.put("status", "success");		
			log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
    
    
    @POST
    @Path("/employeeSelectedDetails")
    public Response employeeSelectedDetails(EFmFmUserMasterPO eFmFmUserMasterPO) throws UnsupportedEncodingException {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
	   	  log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());
	
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
		if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMasterPO.getUserId());
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
		
		
		}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
      
        List<EFmFmUserMasterPO> employeeMasterData = employeeDetailBO
                .getAllEmployeeDetailsFromClientId(eFmFmUserMasterPO.getCombinedFacility());
        log.info("qwery done");
        List<Map<String, Object>> employeeProfile = new ArrayList<Map<String, Object>>();
        if (!(employeeMasterData.isEmpty())) {
            for (EFmFmUserMasterPO empProfileDetails : employeeMasterData) {
                Map<String, Object> profileValues = new HashMap<String, Object>();
                log.info("empProfileDetails.getEmployeeId())"+empProfileDetails.getEmployeeId());
                profileValues.put("employeeId", empProfileDetails.getEmployeeId());
                profileValues.put("userId", empProfileDetails.getUserId());
                profileValues.put("employeeName",new String(Base64.getDecoder().decode(empProfileDetails.getFirstName()), "utf-8"));   
                profileValues.put("emailId", new String(Base64.getDecoder().decode(empProfileDetails.getEmailId()), "utf-8"));                
                profileValues.put("mobileNumber", new String(Base64.getDecoder().decode(empProfileDetails.getMobileNumber()), "utf-8"));
               employeeProfile.add(profileValues);
            }
        }
        log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(employeeProfile, MediaType.APPLICATION_JSON).build();
    }
    
    /*
     * get all employees by usertype
     */
    
    
    @POST
    @Path("/userTypeDetails")
    public Response getAllEmployeeByUserTypeDetails(EFmFmUserMasterPO eFmFmUserMasterPO) throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
	   	 log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMasterPO.getUserId());
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
		
		}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

    	
    	
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        long l1 = System.currentTimeMillis();
        log.info("time" + System.currentTimeMillis());
        List<EFmFmUserMasterPO> employeeMasterData = employeeDetailBO
                .getEmployeeTypeDetailsByBranchId(eFmFmUserMasterPO.getUserType(),eFmFmUserMasterPO.getCombinedFacility());
        log.info("qwery done");
        List<Map<String, Object>> employeeProfile = new ArrayList<Map<String, Object>>();
        if (!(employeeMasterData.isEmpty())) {
            for (EFmFmUserMasterPO empProfileDetails : employeeMasterData) {
                Map<String, Object> profileValues = new HashMap<String, Object>();
                profileValues.put("employeeId", empProfileDetails.getEmployeeId());
                profileValues.put("userId", empProfileDetails.getUserId());
                profileValues.put("userName", empProfileDetails.getUserName());

                log.info("employeeId"+empProfileDetails.getEmployeeId());
                profileValues.put("employeeName",new String(Base64.getDecoder().decode(empProfileDetails.getFirstName()), "utf-8"));
                profileValues.put("locationStatus", empProfileDetails.getLocationStatus());
                profileValues.put("physicallyChallenged", new String(Base64.getDecoder().decode(empProfileDetails.getPhysicallyChallenged()), "utf-8"));
                profileValues.put("isInjured", new String(Base64.getDecoder().decode(empProfileDetails.getIsInjured()), "utf-8"));
                profileValues.put("isVIP", new String(Base64.getDecoder().decode(empProfileDetails.getIsVIP()), "utf-8"));              
                profileValues.put("pragnentLady", new String(Base64.getDecoder().decode(empProfileDetails.getPragnentLady()), "utf-8"));
                profileValues.put("areaId", empProfileDetails.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaId());
                profileValues.put("areaName", empProfileDetails.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());   
                profileValues.put("routeId", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
                profileValues.put("routeName", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());   
                profileValues.put("weekOffs", empProfileDetails.getWeekOffDays());
                profileValues.put("nodalPointId", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointId());
                profileValues.put("nodalPointName", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());   
                try{
                    profileValues.put("tripType",empProfileDetails.getTripType());
                    }catch(Exception e){
                        profileValues.put("tripType","B");
                    }
                profileValues.put("employeeAddress", new String(Base64.getDecoder().decode(empProfileDetails.getAddress()), "utf-8"));               
                profileValues.put("employeeLatiLongi", empProfileDetails.getLatitudeLongitude());
                profileValues.put("employeeDesignation", new String(Base64.getDecoder().decode(empProfileDetails.getEmployeeDesignation()), "utf-8"));
                profileValues.put("status", empProfileDetails.getStatus());
                profileValues.put("emailId", new String(Base64.getDecoder().decode(empProfileDetails.getEmailId()), "utf-8"));
                if (new String(Base64.getDecoder().decode(empProfileDetails.getGender()), "utf-8").equalsIgnoreCase("Male")) {
                    profileValues.put("employeeGender", 1);
                } else {
                    profileValues.put("employeeGender", 2);
                }

                try{
                    profileValues.put("mobileNumber", new String(Base64.getDecoder().decode(empProfileDetails.getMobileNumber()), "utf-8"));
                    }
                    catch(Exception e){
                    	 profileValues.put("mobileNumber", "");
                    }
                employeeProfile.add(profileValues);
            }
        }
        long l2 = (System.currentTimeMillis() - l1);
        log.info("total Excution time" + l2);
   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(employeeProfile, MediaType.APPLICATION_JSON).build();
    }

    /**
     * 
     * @param Check
     *            EmployeeId in eFmFmUserMaster Exist Or not
     * @return employee detail
     * @throws UnsupportedEncodingException 
     */

    @POST
    @Path("/empiddetails")
    public Response employeeDetailsFromEmployeeId(EFmFmUserMasterPO eFmFmUserMasterPO) throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
	   	  log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMasterPO.getUserId());
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
		}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        List<EFmFmUserMasterPO> employeeMasterData = employeeDetailBO.getEmpDetailsFromEmployeeIdAndBranchId(
                eFmFmUserMasterPO.getEmployeeId(), eFmFmUserMasterPO.getCombinedFacility());
        List<Map<String, Object>> employeeProfile = new ArrayList<Map<String, Object>>();
        if (!(employeeMasterData.isEmpty())) {
            for (EFmFmUserMasterPO empProfileDetails : employeeMasterData) {
                Map<String, Object> profileValues = new HashMap<String, Object>();
                List<EFmFmClientUserRolePO> roleMaster = userMasterBO.getUserRolesFromUserIdAndBranchId(
                        employeeMasterData.get(0).getUserId());
                if (!(roleMaster.isEmpty())) {
                    profileValues.put("userRole", roleMaster.get(0).getEfmFmRoleMaster().getRole());
                }
                profileValues.put("employeeId", empProfileDetails.getEmployeeId());
                profileValues.put("userId", empProfileDetails.getUserId());
                profileValues.put("userName", empProfileDetails.getUserName());
                profileValues.put("weekOffs", empProfileDetails.getWeekOffDays());

                profileValues.put("employeeName",new String(Base64.getDecoder().decode(empProfileDetails.getFirstName()), "utf-8"));
                profileValues.put("locationStatus", empProfileDetails.getLocationStatus());
                profileValues.put("physicallyChallenged", new String(Base64.getDecoder().decode(empProfileDetails.getPhysicallyChallenged()), "utf-8"));
                profileValues.put("isInjured", new String(Base64.getDecoder().decode(empProfileDetails.getIsInjured()), "utf-8"));
                profileValues.put("pragnentLady", new String(Base64.getDecoder().decode(empProfileDetails.getPragnentLady()), "utf-8"));
                profileValues.put("isVIP", new String(Base64.getDecoder().decode(empProfileDetails.getIsVIP()), "utf-8"));              

                profileValues.put("areaId", empProfileDetails.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaId());
                profileValues.put("areaName", empProfileDetails.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());   
                profileValues.put("routeId", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
                profileValues.put("routeName", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());   

                profileValues.put("facilityName", empProfileDetails.geteFmFmClientBranchPO().getBranchName());
                try{
                    profileValues.put("tripType",empProfileDetails.getTripType());
                    }catch(Exception e){
                        profileValues.put("tripType","B");
                    }
                
                profileValues.put("nodalPointId", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointId());
                profileValues.put("nodalPointName", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());   

                profileValues.put("employeeAddress", new String(Base64.getDecoder().decode(empProfileDetails.getAddress()), "utf-8"));               
                profileValues.put("employeeLatiLongi", empProfileDetails.getLatitudeLongitude());
                profileValues.put("employeeDesignation", new String(Base64.getDecoder().decode(empProfileDetails.getEmployeeDesignation()), "utf-8"));
                
                try{
                    profileValues.put("employeeDepartment", new String(Base64.getDecoder().decode(empProfileDetails.getEmployeeDepartment()), "utf-8"));
                   }catch(Exception e){
                       profileValues.put("employeeDepartment", "");
                	   log.info("erroe"+e);
                   }
                
                profileValues.put("status", empProfileDetails.getStatus());
                profileValues.put("emailId", new String(Base64.getDecoder().decode(empProfileDetails.getEmailId()), "utf-8"));
                System.out.println(empProfileDetails.getGender());
                if (new String(Base64.getDecoder().decode(empProfileDetails.getGender()), "utf-8").equalsIgnoreCase("Male")) {
                    profileValues.put("employeeGender", 1);
                } else {
                    profileValues.put("employeeGender", 2);
                }
                
                try{
                	if(empProfileDetails.getHostMobileNumber()==null){
                		 profileValues.put("hostMobileNumber","N/A");
                	}else{
                		 profileValues.put("hostMobileNumber",new String(Base64.getDecoder().decode(empProfileDetails.getHostMobileNumber()), "utf-8"));
                	}                   
                   }catch(Exception e){
                      profileValues.put("hostMobileNumber","N/A");
                	   log.info("erroe"+e);
                   }
                
                
                try{
                profileValues.put("mobileNumber", new String(Base64.getDecoder().decode(empProfileDetails.getMobileNumber()), "utf-8"));
                }
                catch(Exception e){
                	 profileValues.put("mobileNumber", "");
                }
                employeeProfile.add(profileValues);
            
            }
        }
   	    log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(employeeProfile, MediaType.APPLICATION_JSON).build();
    }
    
    /**
     * 
     * @param Check
     *            EmployeeIdOrMoblieOrEmail in eFmFmUserMaster Exist Or not
     * @return employee detail
     * @throws UnsupportedEncodingException 
     */

    @POST
    @Path("/empSearchByIdMobOrEmail")
    public Response employeeDetailsFromEmployeeIdOrMoblieOrEmail(EFmFmUserMasterPO eFmFmUserMasterPO) throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
	   	  log.info(eFmFmUserMasterPO.getCombinedFacility()+"serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){
//			responce.put("status", "invalidRequest");
//			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		}
//			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMasterPO.getUserId());
//			if (!(userDetail.isEmpty())) {
//				String jwtToken = "";
//				try {
//					JwtTokenGenerator token = new JwtTokenGenerator();
//					jwtToken = token.generateToken();
//					userDetail.get(0).setAuthorizationToken(jwtToken);
//					userDetail.get(0).setTokenGenerationTime(new Date());
//					userMasterBO.update(userDetail.get(0));
//				} catch (Exception e) {
//					log.info("error" + e);
//				}
//			}	
//		}catch(Exception e){
//			log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		}

        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
		List<EFmFmUserMasterPO> employeeDetail=null;		
        if(eFmFmUserMasterPO.getSearchType().equalsIgnoreCase("mobile")){
			log.info("mobile");
        	employeeDetail = employeeDetailBO.getEmpMobileNoDetails(Base64.getEncoder().encodeToString(eFmFmUserMasterPO.getEmployeeId().getBytes("utf-8")),eFmFmUserMasterPO.getCombinedFacility());
        }
        else if(eFmFmUserMasterPO.getSearchType().equalsIgnoreCase("email")){
			log.info("email");
        	employeeDetail = employeeDetailBO.getAllEmployeeDetailsFromEmailId(Base64.getEncoder().encodeToString(eFmFmUserMasterPO.getEmployeeId().getBytes("utf-8")),eFmFmUserMasterPO.getCombinedFacility());
        }
       else if(eFmFmUserMasterPO.getSearchType().equalsIgnoreCase("empId")){
			log.info("empId");
       	employeeDetail = employeeDetailBO.getEmpDetailsFromEmployeeIdAndBranchId(
                eFmFmUserMasterPO.getEmployeeId(),eFmFmUserMasterPO.getCombinedFacility());
        }
       else{
			responce.put("status", "invalidSearch");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
       }
        
        List<Map<String, Object>> employeeProfile = new ArrayList<Map<String, Object>>();
        log.info("employeeDetail"+employeeDetail.size());
        if (!(employeeDetail.isEmpty())) {
            for (EFmFmUserMasterPO empProfileDetails : employeeDetail) {
                Map<String, Object> profileValues = new HashMap<String, Object>();
                List<EFmFmClientUserRolePO> roleMaster = userMasterBO.getUserRolesFromUserIdAndBranchId(
                		eFmFmUserMasterPO.getUserId());
                if (!(roleMaster.isEmpty())) {
                    profileValues.put("userRole", roleMaster.get(0).getEfmFmRoleMaster().getRole());
                }
                profileValues.put("employeeId", empProfileDetails.getEmployeeId());
                profileValues.put("userId", empProfileDetails.getUserId());
                profileValues.put("userName", empProfileDetails.getUserName());
                profileValues.put("weekOffs", empProfileDetails.getWeekOffDays());
                profileValues.put("facilityName",empProfileDetails.geteFmFmClientBranchPO().getBranchName());
                try{
                    profileValues.put("tripType",empProfileDetails.getTripType());
                    }catch(Exception e){
                        profileValues.put("tripType","B");
                    }
                profileValues.put("employeeName",new String(Base64.getDecoder().decode(empProfileDetails.getFirstName()), "utf-8"));
                profileValues.put("locationStatus", empProfileDetails.getLocationStatus());
                profileValues.put("physicallyChallenged", new String(Base64.getDecoder().decode(empProfileDetails.getPhysicallyChallenged()), "utf-8"));
                profileValues.put("isInjured", new String(Base64.getDecoder().decode(empProfileDetails.getIsInjured()), "utf-8"));
                profileValues.put("pragnentLady", new String(Base64.getDecoder().decode(empProfileDetails.getPragnentLady()), "utf-8"));
                profileValues.put("isVIP", new String(Base64.getDecoder().decode(empProfileDetails.getIsVIP()), "utf-8"));              

                profileValues.put("areaId", empProfileDetails.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaId());
                profileValues.put("areaName", empProfileDetails.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());   
                profileValues.put("routeId", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
                profileValues.put("routeName", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());   

                profileValues.put("nodalPointId", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointId());
                profileValues.put("nodalPointName", empProfileDetails.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());   
                try{
                	profileValues.put("deviceType", empProfileDetails.getDeviceType());
					}catch(Exception e){
						profileValues.put("deviceType", "NO");
					}	
                profileValues.put("employeeAddress", new String(Base64.getDecoder().decode(empProfileDetails.getAddress()), "utf-8"));               
                profileValues.put("employeeLatiLongi", empProfileDetails.getLatitudeLongitude());
                profileValues.put("employeeDesignation", new String(Base64.getDecoder().decode(empProfileDetails.getEmployeeDesignation()), "utf-8"));
                
                try{
                    profileValues.put("employeeDepartment", new String(Base64.getDecoder().decode(empProfileDetails.getEmployeeDepartment()), "utf-8"));
                   }catch(Exception e){
                       profileValues.put("employeeDepartment", "");
                	   log.info("erroe"+e);
                   }
                
                profileValues.put("status", empProfileDetails.getStatus());
                profileValues.put("emailId", new String(Base64.getDecoder().decode(empProfileDetails.getEmailId()), "utf-8"));
                System.out.println(empProfileDetails.getGender());
                if (new String(Base64.getDecoder().decode(empProfileDetails.getGender()), "utf-8").equalsIgnoreCase("Male")) {
                    profileValues.put("employeeGender", 1);
                } else {
                    profileValues.put("employeeGender", 2);
                }
                
                try{
                	if(empProfileDetails.getHostMobileNumber()==null){
                		 profileValues.put("hostMobileNumber","N/A");
                	}else{
                		 profileValues.put("hostMobileNumber",new String(Base64.getDecoder().decode(empProfileDetails.getHostMobileNumber()), "utf-8"));
                	}                   
                   }catch(Exception e){
                      profileValues.put("hostMobileNumber","N/A");
                	   log.info("erroe"+e);
                   }
                
                
                try{
                profileValues.put("mobileNumber", new String(Base64.getDecoder().decode(empProfileDetails.getMobileNumber()), "utf-8"));
                }
                catch(Exception e){
                	 profileValues.put("mobileNumber", "");
                }
                employeeProfile.add(profileValues);
            
            }
        }
       
   	    log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(employeeProfile, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/allActiveLocation")
    public Response getAllActiveLocation(EFmFmLocationMasterPO eFmFmLocationMasterPO) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO"); 
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
   	  	log.info("serviceStart -UserId :" + eFmFmLocationMasterPO.getUserId());  		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmLocationMasterPO.getUserId()) && !(httpRequest.getHeader("authenticationToken").equalsIgnoreCase(null)))){
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
   	    List<Map<String, Object>> locationData = new ArrayList<Map<String, Object>>();   	    
        List<EFmFmLocationMasterPO> locationMasterData = employeeDetailBO.getAllActiveLocation(eFmFmLocationMasterPO.getIsActive(),eFmFmLocationMasterPO.getCombinedFacility());       
        if (!(locationMasterData.isEmpty())) {
            for (EFmFmLocationMasterPO locationDetails : locationMasterData) {            	
                Map<String, Object> locationValues = new HashMap<String, Object>();   
                locationValues.put("areaId", locationDetails.getLocationId());
                locationValues.put("areaName", locationDetails.getLocationName());            
                locationValues.put("locationAddress", locationDetails.getLocationAddress());
                locationValues.put("locationLatLng", locationDetails.getLocationLatLng());
                locationValues.put("userId", locationDetails.getEfmFmUserMaster().getUserId());
                locationValues.put("routeAreaId", locationDetails.geteFmFmRouteAreaMapping().getRouteAreaId());
                locationValues.put("employeeName",new String(Base64.getDecoder().decode(locationDetails.getEfmFmUserMaster().getFirstName()),"utf-8"));
                if(locationDetails.getLocationName().replaceAll(" ","").toLowerCase().equalsIgnoreCase("homelocation")){
                	List<EFmFmUserMasterPO> userDetails =employeeDetailBO.getParticularEmpDetailsFromUserIdWithOutStatus(eFmFmLocationMasterPO.getUserId(), eFmFmLocationMasterPO.getCombinedFacility());                	
                    locationValues.put("locationAddress", new String(Base64.getDecoder().decode(userDetails.get(0).getAddress()),"utf-8"));
                    locationValues.put("locationLatLng", userDetails.get(0).getLatitudeLongitude());                                       
                }                
                locationData.add(locationValues);                
            }
        }
   	    log.info("serviceEnd -UserId :" + eFmFmLocationMasterPO.getUserId());
        return Response.ok(locationData, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/getAllUserProjectDetails")
    public Response getAllUserProjectDetails(EFmFmClientBranchPO eFmFmClientBranchPO) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO"); 
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientBranchPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmClientBranchPO.getUserId());
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
		}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
   	  	log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
   	    List<Map<String, Object>> projectIdData = new ArrayList<Map<String, Object>>();   	    
        List<EFmFmEmployeeProjectDetailsPO> projectDetails = employeeDetailBO.getListOfProjectIdByUserId(eFmFmClientBranchPO.getUserId(),eFmFmClientBranchPO.getCombinedFacility());       
        if (!(projectDetails.isEmpty())) {
            for (EFmFmEmployeeProjectDetailsPO projectList : projectDetails) {            	
                Map<String, Object> projectValues = new HashMap<String, Object>();  
                projectValues.put("projectId", projectList.geteFmFmClientProjectDetails().getClientProjectId());
                projectValues.put("projectName", projectList.geteFmFmClientProjectDetails().getEmployeeProjectName()); 
                projectValues.put("reportingManagerId", projectList.getReportingManagerUserId());                                          
                projectIdData.add(projectValues);                
            }
        }
   	    log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
        return Response.ok(projectIdData, MediaType.APPLICATION_JSON).build();
    }
    
    
    
    @POST
    @Path("/locationDetailsById")
    public Response multipleLocation(EFmFmLocationMasterPO eFmFmLocationMasterPO) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO"); 
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmLocationMasterPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
   	  	log.info("serviceStart -UserId :" + eFmFmLocationMasterPO.getUserId());
   	    List<Map<String, Object>> locationData = new ArrayList<Map<String, Object>>();   	    
        List<EFmFmLocationMasterPO> locationMasterData = employeeDetailBO.getMultipleLocation(eFmFmLocationMasterPO.getMultipleLocation(),eFmFmLocationMasterPO.getCombinedFacility());       
        if (!(locationMasterData.isEmpty())) {
            for (EFmFmLocationMasterPO locationDetails : locationMasterData) {            	
                Map<String, Object> locationValues = new HashMap<String, Object>();   
                locationValues.put("locationId", locationDetails.getLocationId());
                locationValues.put("locationName", locationDetails.getLocationName());
                locationValues.put("locationAddress", locationDetails.getLocationAddress());
                locationValues.put("locationLatLng", locationDetails.getLocationLatLng());
                locationValues.put("userId", locationDetails.getEfmFmUserMaster().getUserId());                         
                locationValues.put("employeeName",new String(Base64.getDecoder().decode(locationDetails.getEfmFmUserMaster().getFirstName()),"utf-8"));              
                locationData.add(locationValues);  
            }
        }
   	    log.info("serviceEnd -UserId :" + eFmFmLocationMasterPO.getUserId());
        return Response.ok(locationData, MediaType.APPLICATION_JSON).build();
    }
    
    
    @POST
    @Path("/projectEmployeesByRepMng")
    public Response projectEmployeesByRepMng(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO"); 
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        /*try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmLocationMasterPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}*/
   	  	log.info("serviceStart -UserId :" + eFmFmEmployeeTravelRequestPO.getUserId());
   	    List<Map<String, Object>> employeeData = new ArrayList<Map<String, Object>>();   	    
        List<EFmFmEmployeeProjectDetailsPO> projectEmployeeData = employeeDetailBO.getAllProjectUserByrepManagerWithProjectId(Integer.valueOf(eFmFmEmployeeTravelRequestPO.getReportingManagerUserId()),eFmFmEmployeeTravelRequestPO.getCombinedFacility(),eFmFmEmployeeTravelRequestPO.getProjectId());     
        if (!(projectEmployeeData.isEmpty())) {
            for (EFmFmEmployeeProjectDetailsPO employeeDetails : projectEmployeeData) {            	
                Map<String, Object> employeeList = new HashMap<String, Object>();
                employeeList.put("employeeId", employeeDetails.getEfmFmUserMaster().getEmployeeId() +" | "+new String(Base64.getDecoder().decode(employeeDetails.getEfmFmUserMaster().getFirstName()),"utf-8"));
                employeeList.put("userId", employeeDetails.getEfmFmUserMaster().getUserId());          
                employeeData.add(employeeList);  
            }
        }
   	    log.info("serviceEnd -UserId :" + eFmFmEmployeeTravelRequestPO.getUserId());
        return Response.ok(employeeData, MediaType.APPLICATION_JSON).build();
    }
    
    
    
    @POST
    @Path("/requestAddLocation")
    public Response addLocation(EFmFmLocationMasterPO eFmFmLocationMasterPO) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");  
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
   	  	log.info("serviceStart -UserId :" + eFmFmLocationMasterPO.getUserId()); 		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmLocationMasterPO.getUserId()) && !(httpRequest.getHeader("authenticationToken").equalsIgnoreCase(null)))){
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
   		
        List<EFmFmLocationMasterPO> locationMasterData = employeeDetailBO.getLocationNameExist(eFmFmLocationMasterPO.getLocationName(),eFmFmLocationMasterPO.getCombinedFacility());       
        if (locationMasterData.isEmpty()) {
        	EFmFmUserMasterPO efmFmUserMaster=new EFmFmUserMasterPO();
        	efmFmUserMaster.setUserId(eFmFmLocationMasterPO.getUserId());
       	    eFmFmLocationMasterPO.setCreationTime(new Date());
       	    eFmFmLocationMasterPO.setEfmFmUserMaster(efmFmUserMaster);
       	    employeeDetailBO.saveLocationMaster(eFmFmLocationMasterPO);
       	    responce.put("status", "success");
       	    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        responce.put("status", "alreadyExist");
   	    log.info("serviceEnd -UserId :" + eFmFmLocationMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }    
    
    @POST
    @Path("/modifyLocation")
    public Response modifyLocation(EFmFmLocationMasterPO eFmFmLocationMasterPO) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO"); 
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmLocationMasterPO.getUserId()))){
        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmLocationMasterPO.getUserId());
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
        }catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
   	  	log.info("serviceStart -UserId :" + eFmFmLocationMasterPO.getUserId());
   	    List<Map<String, Object>> locationData = new ArrayList<Map<String, Object>>();	
   	
        List<EFmFmLocationMasterPO> locationMasterData = employeeDetailBO.getMultipleLocation(eFmFmLocationMasterPO.getMultipleLocation(),eFmFmLocationMasterPO.getCombinedFacility());       
        if (!(locationMasterData.isEmpty())) {
        	EFmFmUserMasterPO userMaster=new EFmFmUserMasterPO();
        	userMaster.setUserId(eFmFmLocationMasterPO.getUserId());        	
        	locationMasterData.get(0).setUpdatedTime(new Date()); 
       	    locationMasterData.get(0).setLocationId(locationMasterData.get(0).getLocationId());    	    
       	    locationMasterData.get(0).setLocationName(eFmFmLocationMasterPO.getLocationName());       	    
       	    locationMasterData.get(0).setLocationAddress(eFmFmLocationMasterPO.getLocationAddress());
       	 	locationMasterData.get(0).setLocationLatLng(eFmFmLocationMasterPO.getLocationLatLng()); 
       	 	locationMasterData.get(0).setEfmFmUserMaster(userMaster);       	 	
       	    employeeDetailBO.updateLocationMaster(locationMasterData.get(0));
       	    responce.put("status", "success");
    	    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        responce.put("status", "notExist");
   	    log.info("serviceEnd -UserId :" + eFmFmLocationMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/disableAndEnable")
    public Response disableAndEnable(EFmFmLocationMasterPO eFmFmLocationMasterPO) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO"); 
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmLocationMasterPO.getUserId()))){
        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        	
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmLocationMasterPO.getUserId());
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
        }catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        List<EFmFmLocationMasterPO> locationMasterData = employeeDetailBO.getMultipleLocation(eFmFmLocationMasterPO.getMultipleLocation(),eFmFmLocationMasterPO.getCombinedFacility());       
        if (!(locationMasterData.isEmpty())) {
        	EFmFmUserMasterPO userMaster=new EFmFmUserMasterPO();
        	userMaster.setUserId(eFmFmLocationMasterPO.getUserId());        	
        	locationMasterData.get(0).setUpdatedTime(new Date());       	    
       	    locationMasterData.get(0).setIsActive(eFmFmLocationMasterPO.getIsActive());       	  
       	 	locationMasterData.get(0).setEfmFmUserMaster(userMaster);       	 	
       	    employeeDetailBO.updateLocationMaster(locationMasterData.get(0));
       	    responce.put("status", "success");
       	    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        responce.put("status", "notExist");
   	    log.info("serviceEnd -UserId :" + eFmFmLocationMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
    
    @POST
    @Path("/callToDriver")
    public Response connectCallToDriverFromEmpAPP(EFmFmLocationMasterPO eFmFmLocationMasterPO) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO"); 
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmLocationMasterPO.getUserId()))){
        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        	
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmLocationMasterPO.getUserId());
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
        }catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        List<EFmFmLocationMasterPO> locationMasterData = employeeDetailBO.getMultipleLocation(eFmFmLocationMasterPO.getMultipleLocation(),eFmFmLocationMasterPO.geteFmFmClientBranchPO().getCombinedFacility());       
        if (!(locationMasterData.isEmpty())) {
        	EFmFmUserMasterPO userMaster=new EFmFmUserMasterPO();
        	userMaster.setUserId(eFmFmLocationMasterPO.getUserId());        	
        	locationMasterData.get(0).setUpdatedTime(new Date());       	    
       	    locationMasterData.get(0).setIsActive(eFmFmLocationMasterPO.getIsActive());       	  
       	 	locationMasterData.get(0).setEfmFmUserMaster(userMaster);       	 	
       	    employeeDetailBO.updateLocationMaster(locationMasterData.get(0));
       	    responce.put("status", "success");
       	    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        responce.put("status", "notExist");
   	    log.info("serviceEnd -UserId :" + eFmFmLocationMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/addLocation")
    public Response requestAddLocation(EFmFmLocationMasterPO eFmFmLocationMasterPO) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");  
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
        Map<String, Object> responce = new HashMap<String, Object>();
   	  	log.info("serviceStart -UserId :" + eFmFmLocationMasterPO.getUserId()); 		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmLocationMasterPO.getUserId()) && !(httpRequest.getHeader("authenticationToken").equalsIgnoreCase(null)))){
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}   		
        List<EFmFmLocationMasterPO> locationMasterData = employeeDetailBO.getLocationNameExist(eFmFmLocationMasterPO.getLocationName(),eFmFmLocationMasterPO.getCombinedFacility());       
        if (locationMasterData.isEmpty()) {
        	EFmFmUserMasterPO efmFmUserMaster=new EFmFmUserMasterPO();
        	efmFmUserMaster.setUserId(eFmFmLocationMasterPO.getUserId());
       	    eFmFmLocationMasterPO.setCreationTime(new Date());
       	    eFmFmLocationMasterPO.setEfmFmUserMaster(efmFmUserMaster);
     	 
		    List<EFmFmClientRouteMappingPO> allRoutes = iRouteDetailBO.getParticularRouteDetailByClient(eFmFmLocationMasterPO.getCombinedFacility(),eFmFmLocationMasterPO.getLocationName());
				if (!(allRoutes.isEmpty())) {		
					List<EFmFmAreaMasterPO> areaDetials=iRouteDetailBO.getAllAreaName(eFmFmLocationMasterPO.getLocationName());
			       	 if(areaDetials.isEmpty()){
			       		responce.put("status", "areaNameFailed");		       		 
			       	 }else{			       		 
			       		EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping = new EFmFmRouteAreaMappingPO();
						EFmFmAreaNodalMasterPO eFmFmAreaNodalMasterPO = new EFmFmAreaNodalMasterPO();
						eFmFmAreaNodalMasterPO.setNodalPointId(1);// default ZoneId
						eFmFmRouteAreaMapping.setEfmFmAreaMaster(areaDetials.get(0));
						eFmFmRouteAreaMapping.seteFmFmZoneMaster(allRoutes.get(0).geteFmFmZoneMaster());
						eFmFmRouteAreaMapping.seteFmFmNodalAreaMaster(eFmFmAreaNodalMasterPO);
						int areaRouteId=iRouteDetailBO.saveAreaWithZone(eFmFmRouteAreaMapping);	
						eFmFmRouteAreaMapping.setRouteAreaId(areaRouteId);
						eFmFmLocationMasterPO.seteFmFmRouteAreaMapping(eFmFmRouteAreaMapping);
			     	    employeeDetailBO.saveLocationMaster(eFmFmLocationMasterPO);
			       	    responce.put("status", "success");  
			       	 }
			   }else{
					int addresponse=addNewZone(eFmFmLocationMasterPO);
					if(addresponse==0){
						responce.put("status", "areaCreationFailed");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}else{
						EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping=new EFmFmRouteAreaMappingPO();
						eFmFmRouteAreaMapping.setRouteAreaId(addresponse);						
						eFmFmLocationMasterPO.seteFmFmRouteAreaMapping(eFmFmRouteAreaMapping);
			     	    employeeDetailBO.saveLocationMaster(eFmFmLocationMasterPO);
			       	    responce.put("status", "success");  
					}
				}       	    
       	 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        responce.put("status", "alreadyExist");
   	    log.info("serviceEnd -UserId :" + eFmFmLocationMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }	
	public int addNewZone(EFmFmLocationMasterPO eFmFmLocationMasterPO ) {
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");		
		int responce=0;
		try {
			List<EFmFmZoneMasterPO> newZoneDetail = iRouteDetailBO
					.getAllRouteName(eFmFmLocationMasterPO.getLocationName());
			if (newZoneDetail.isEmpty()) {
				EFmFmAreaMasterPO eFmFmAreaMaster = new EFmFmAreaMasterPO();
				eFmFmAreaMaster.setAreaDescription(eFmFmLocationMasterPO.getLocationName());
				eFmFmAreaMaster.setAreaName(eFmFmLocationMasterPO.getLocationName().toUpperCase());
				int areaId = iRouteDetailBO.addAreaName(eFmFmAreaMaster);
				eFmFmAreaMaster.setAreaId(areaId);

				EFmFmZoneMasterPO eFmFmZoneMaster = new EFmFmZoneMasterPO();
				eFmFmZoneMaster.setZoneName(eFmFmLocationMasterPO.getLocationName().toUpperCase());
				eFmFmZoneMaster.setStatus("Y");
				eFmFmZoneMaster.setNodalRoute(false);
				eFmFmZoneMaster.setCreationTime(new Date());
				eFmFmZoneMaster.setUpdatedTime(new Date());
				int zoneId = iRouteDetailBO.addRouteName(eFmFmZoneMaster);
				eFmFmZoneMaster.setZoneId(zoneId);
				
				EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO = new EFmFmClientRouteMappingPO();
				newZoneDetail = iRouteDetailBO.getAllRouteName(eFmFmLocationMasterPO.getLocationName().toUpperCase());
				eFmFmClientRouteMappingPO.setRouteName(eFmFmLocationMasterPO.getLocationName().toUpperCase());
				eFmFmClientRouteMappingPO.seteFmFmClientBranchPO(eFmFmLocationMasterPO.geteFmFmClientBranchPO());
				eFmFmClientRouteMappingPO.seteFmFmZoneMaster(newZoneDetail.get(0));
				iRouteDetailBO.saveClientRouteMapping(eFmFmClientRouteMappingPO);

				EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping = new EFmFmRouteAreaMappingPO();
				EFmFmAreaNodalMasterPO eFmFmAreaNodalMasterPO = new EFmFmAreaNodalMasterPO();
				eFmFmAreaNodalMasterPO.setNodalPointId(1);// default ZoneId
				eFmFmRouteAreaMapping.setEfmFmAreaMaster(eFmFmAreaMaster);
				eFmFmRouteAreaMapping.seteFmFmZoneMaster(eFmFmZoneMaster);
				eFmFmRouteAreaMapping.seteFmFmNodalAreaMaster(eFmFmAreaNodalMasterPO);
				int areaRouteId=iRouteDetailBO.saveAreaWithZone(eFmFmRouteAreaMapping);
				responce=areaRouteId;
			}
		} catch (Exception e) {
			responce =0;
		}		
			return responce;
	}
	public void clickToCallDriver(String GET_URL) throws IOException{
		final String USER_AGENT = "Mozilla/5.0";        
		        URL obj = new URL(GET_URL);
		        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		        con.setRequestMethod("GET");
		        con.setRequestProperty("User-Agent", USER_AGENT);
		        int responseCode = con.getResponseCode();
		        StringBuffer response = new StringBuffer();
		        if (responseCode == HttpURLConnection.HTTP_OK) { // success
		            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		            String inputLine;
		            while ((inputLine = in.readLine()) != null) {
		                response.append(inputLine);
		            }
		            in.close();
		            System.out.println("Message solutionsinfini Response From GateWay: " + response.toString() + " for Mobile: "
		                    + response.toString()+"responseCode"+responseCode);
		        } else {
		        	System.out.println(response.toString()+"responseCode"+responseCode);
		        }
	}
    @POST
    @Path("/addProjectDetails")
    public Response addProjectDetails(EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO) throws UnsupportedEncodingException, ParseException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");  
        Map<String, Object> responce = new HashMap<String, Object>();
   	  	log.info("serviceStart -UserId :" + eFmFmClientProjectDetailsPO.getUserId()); 		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));       
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
  		
    	List<EFmFmClientProjectDetailsPO> projectIdExist = employeeDetailBO
                .getProjectDetails(eFmFmClientProjectDetailsPO.getClientProjectId().toUpperCase(),eFmFmClientProjectDetailsPO.getCombinedFacility());
        if (projectIdExist.isEmpty()) { 
        	try {
        		EFmFmClientProjectDetailsPO eFmFmClientProjectDetails=new EFmFmClientProjectDetailsPO();
            	eFmFmClientProjectDetails.setClientProjectId(eFmFmClientProjectDetailsPO.getClientProjectId());
            	eFmFmClientProjectDetails.setEmployeeProjectName(eFmFmClientProjectDetailsPO.getEmployeeProjectName());
            	Date projectAllocationStarDate = dateFormatter.parse(eFmFmClientProjectDetailsPO.getStartDate());
            	eFmFmClientProjectDetails.setProjectAllocationStarDate(projectAllocationStarDate);
            	Date projectAllocationEndDate = dateFormatter.parse(eFmFmClientProjectDetailsPO.getEndDate());
            	eFmFmClientProjectDetails.setProjectAllocationEndDate(projectAllocationEndDate);
            	eFmFmClientProjectDetails.setCreatedDate(new Date());
            	eFmFmClientProjectDetails.setIsActive("A");
            	eFmFmClientProjectDetails.seteFmFmClientBranchPO(eFmFmClientProjectDetailsPO.geteFmFmClientBranchPO()); 
            	if(!(projectAllocationStarDate.getTime()<projectAllocationEndDate.getTime())){
                	responce.put("status", "Project Start Date Should be less than projectEnd Date");  	    
        		    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            	}
            	employeeDetailBO.save(eFmFmClientProjectDetails);
            	responce.put("status", "success");  	    
    		    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				
			} catch (Exception e) {
				log.debug("Project not added"+e);
				e.printStackTrace();
			}        	
        }
        responce.put("status", "alreadyExist");
   	    log.info("serviceEnd -UserId :" + eFmFmClientProjectDetailsPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
    
    @POST
    @Path("/listofProjectId")
    public Response listofProjectId(EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");  
        Map<String, Object> responce = new HashMap<String, Object>();    
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
       /* log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientProjectDetailsPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}*/
   	  	log.info("serviceStart -UserId :" + eFmFmClientProjectDetailsPO.getUserId());
   	    List<Map<String, Object>> projectData = new ArrayList<Map<String, Object>>();   	    
        List<EFmFmClientProjectDetailsPO> listofProjectDetails = employeeDetailBO.getListOfProjectDetails
        		(eFmFmClientProjectDetailsPO.getIsActive(), eFmFmClientProjectDetailsPO.getCombinedFacility());     
        if (!(listofProjectDetails.isEmpty())) {
            for (EFmFmClientProjectDetailsPO listofProjects : listofProjectDetails) {            	
                Map<String, Object> listValues = new HashMap<String, Object>();   
                listValues.put("projectId", listofProjects.getProjectId());
                listValues.put("clientProjectId", listofProjects.getClientProjectId());
                listValues.put("projectName", listofProjects.getEmployeeProjectName());
                listValues.put("projectStartDate", dateFormatter.format(listofProjects.getProjectAllocationStarDate()));
                listValues.put("projectEndDate", dateFormatter.format(listofProjects.getProjectAllocationEndDate()));    
                listValues.put("isActive", listofProjects.getIsActive()); 
                             
                projectData.add(listValues);  
            }
        }
   	    log.info("serviceEnd -UserId :" + eFmFmClientProjectDetailsPO.getUserId());
        return Response.ok(projectData, MediaType.APPLICATION_JSON).build();
    }
    
    
    @POST
    @Path("/updatedAndDisableProejctDetails")
    public Response updatedAndDisableProejctDetails(EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO) throws UnsupportedEncodingException, ParseException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");  
        Map<String, Object> responce = new HashMap<String, Object>(); 
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
       /* log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientProjectDetailsPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}*/
   	  	log.info("serviceStart -UserId :" + eFmFmClientProjectDetailsPO.getUserId());
   	    List<Map<String, Object>> projectData = new ArrayList<Map<String, Object>>();   	    
        List<EFmFmClientProjectDetailsPO> listofProjectDetails = employeeDetailBO.getParticularProjectDetails(eFmFmClientProjectDetailsPO.getProjectId(), eFmFmClientProjectDetailsPO.getCombinedFacility());     
        if (!(listofProjectDetails.isEmpty())) {
        	Date projectAllocationStarDate = dateFormatter.parse(eFmFmClientProjectDetailsPO.getStartDate());
         	listofProjectDetails.get(0).setProjectAllocationStarDate(projectAllocationStarDate);
         	Date projectAllocationEndDate = dateFormatter.parse(eFmFmClientProjectDetailsPO.getEndDate());
         	listofProjectDetails.get(0).setProjectAllocationEndDate(projectAllocationEndDate);
        	listofProjectDetails.get(0).setClientProjectId(eFmFmClientProjectDetailsPO.getClientProjectId());
        	listofProjectDetails.get(0).setEmployeeProjectName(eFmFmClientProjectDetailsPO.getEmployeeProjectName());
        	if(null==eFmFmClientProjectDetailsPO.getRemarks()){
        		listofProjectDetails.get(0).setRemarks("NA");
        	}else{
        		listofProjectDetails.get(0).setRemarks(eFmFmClientProjectDetailsPO.getRemarks());
        	}
        	listofProjectDetails.get(0).setIsActive(eFmFmClientProjectDetailsPO.getIsActive());   	
        	employeeDetailBO.updateClientProject(listofProjectDetails.get(0));        	
        	List<EFmFmEmployeeProjectDetailsPO >  listOfProjectUser=employeeDetailBO.getAllEmployeeByProjectId(eFmFmClientProjectDetailsPO.geteFmFmClientBranchPO().getBranchId(),
        			eFmFmClientProjectDetailsPO.getProjectId());
        	if(!(listOfProjectUser.isEmpty())){
        		for(EFmFmEmployeeProjectDetailsPO userDetails:listOfProjectUser){
        			userDetails.setIsActive("N");
        			userDetails.setRemarks(listofProjectDetails.get(0).getRemarks());
        			employeeDetailBO.updateEmployeeProjectDetails(userDetails);       			
        		}  		
        	}        	
        	responce.put("status", "success");  	    
		    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			
        }
        responce.put("status", "projectIdNotExist");
   	    log.info("serviceEnd -UserId :" + eFmFmClientProjectDetailsPO.getUserId());
        return Response.ok(projectData, MediaType.APPLICATION_JSON).build();
    }
    
    
    @POST
    @Path("/listofProjectMngAndSpoc")
    public Response listofProjectMngAndSpoc(EFmFmClientBranchPO emFmClientBranchPO) throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();   
       /* log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientProjectDetailsPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}*/
   	  	log.info("serviceStart -UserId :" + emFmClientBranchPO.getUserId());
   	    List<Map<String, Object>> projectData = new ArrayList<Map<String, Object>>();   	    
        List<EFmFmClientUserRolePO> listofUserDetails = userMasterBO.getEmployeeDetailsByRole(emFmClientBranchPO.getUserRoles(),
        		emFmClientBranchPO.getBranchId());     
        if (!(listofUserDetails.isEmpty())) {
            for (EFmFmClientUserRolePO listofUser : listofUserDetails) {            	
                Map<String, Object> listValues = new HashMap<String, Object>();   
                listValues.put("UserId", listofUser.getEfmFmUserMaster().getUserId());
                listValues.put("UserRole", listofUser.getEfmFmRoleMaster().getRole()); 
                listValues.put("employeeId", listofUser.getEfmFmUserMaster().getEmployeeId() +" | "+new String(Base64.getDecoder().decode(listofUser.getEfmFmUserMaster().getFirstName()),"utf-8"));                             
                projectData.add(listValues);  
            }
        }
   	    log.info("serviceEnd -UserId :" + emFmClientBranchPO.getUserId());
        return Response.ok(projectData, MediaType.APPLICATION_JSON).build();
    }
    @POST
    @Path("/projectAllocation")
    public Response projectAllocation(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO) throws UnsupportedEncodingException {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");
        Map<String, Object> responce = new HashMap<String, Object>(); 
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
       /* log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientProjectDetailsPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}*/
   	  	log.info("serviceStart -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());   	      
		   	 try {
		   		Date projectAllocationStarDate = dateFormatter.parse(eFmFmEmployeeProjectDetailsPO.getStartDate());            	
            	Date projectAllocationEndDate = dateFormatter.parse(eFmFmEmployeeProjectDetailsPO.getEndDate());            	
		   		if(projectAllocationStarDate.getTime()<projectAllocationEndDate.getTime()){		   			
					List<EFmFmEmployeeProjectDetailsPO> data = eFmFmEmployeeProjectDetailsPO.getAllocatedEmployees();		   	 
				   	 for(EFmFmEmployeeProjectDetailsPO values:data){				   		
				   		EFmFmEmployeeProjectDetailsPO projectAllocationDetails = new EFmFmEmployeeProjectDetailsPO();				   		
				   		projectAllocationDetails.setCreatedBy(String.valueOf(eFmFmEmployeeProjectDetailsPO.getUserId()));
				   		projectAllocationDetails.setCreatedDate(new Date());
				   		projectAllocationDetails.setIsActive(eFmFmEmployeeProjectDetailsPO.getIsActive());
				   		if(null == eFmFmEmployeeProjectDetailsPO.getRemarks()){
				   			projectAllocationDetails.setRemarks("NA");
				   		}else{
				   			projectAllocationDetails.setRemarks(eFmFmEmployeeProjectDetailsPO.getRemarks());
				   		}
				   		projectAllocationDetails.seteFmFmClientProjectDetails(eFmFmEmployeeProjectDetailsPO.geteFmFmClientProjectDetails());				   		
				   		projectAllocationDetails.setReportingManagerUserId(eFmFmEmployeeProjectDetailsPO.getReportingManagerUserId());
				   		projectAllocationDetails.setProjectAllocationStarDate(projectAllocationStarDate);
				   		projectAllocationDetails.setProjectAllocationEndDate(projectAllocationEndDate);  		
				   		List<EFmFmEmployeeProjectDetailsPO> projectAllocationExist=employeeDetailBO.getListOfUserByreportingManager
				   				(Integer.valueOf(eFmFmEmployeeProjectDetailsPO.getReportingManagerUserId()), eFmFmEmployeeProjectDetailsPO.getCombinedFacility(),
				   						values.getEfmFmUserMaster().getUserId());				   				
				   		if(projectAllocationExist.isEmpty()){	
				   			projectAllocationDetails.setEfmFmUserMaster(values.getEfmFmUserMaster());
				   			employeeDetailBO.addEmployeeProjectDetails(projectAllocationDetails);
				   			responce.put("status","Project allocated successfully,Kindly make sure the employee allocation details at project allocation details tab");
				   			
				   		}else{
				   			responce.put("status","Project already allocated,Kindly check the Status on project allocation details tab");
				   		}
				   	 }				   	
            	}else{
            		responce.put("status", "Project Start Date Should be less than projectEnd Date");  	    
        		    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            	}
		 	 }catch (Exception e) {	           
				       log.debug("statusFailedAddProjectDetails"+e);	
				       e.printStackTrace();
				       responce.put("status", "failed");				       
		     } 
		        
   	    log.info("serviceEnd -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
    
    @POST
    @Path("/disableAllProjectAllocation")
    public Response disableAllProjectAllocation(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO) throws UnsupportedEncodingException {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");
        Map<String, Object> responce = new HashMap<String, Object>(); 
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
       /* log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientProjectDetailsPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}*/
   	  	log.info("serviceStart -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());   	      
		   	 try {           	
	   			
					List<EFmFmEmployeeProjectDetailsPO> data = eFmFmEmployeeProjectDetailsPO.getAllocatedEmployees();		   	 
				   	 for(EFmFmEmployeeProjectDetailsPO values:data){				   		 
				   		List<EFmFmEmployeeProjectDetailsPO> projectAllocationExist=employeeDetailBO.getClientProjectIdByMangerAndEmployee
				   				(Integer.valueOf(eFmFmEmployeeProjectDetailsPO.getReportingManagerUserId()),
				   						eFmFmEmployeeProjectDetailsPO.getCombinedFacility()
				   						,eFmFmEmployeeProjectDetailsPO.geteFmFmClientProjectDetails().getProjectId(),values.getEfmFmUserMaster().getUserId());			   				
				   		if(!(projectAllocationExist.isEmpty())){
				   			for(EFmFmEmployeeProjectDetailsPO valuelist:projectAllocationExist){				   							   		
				   				valuelist.setCreatedBy(String.valueOf(eFmFmEmployeeProjectDetailsPO.getUserId()));
				   				valuelist.setCreatedDate(new Date());
				   				valuelist.setIsActive(eFmFmEmployeeProjectDetailsPO.getIsActive());
						   		if(null == eFmFmEmployeeProjectDetailsPO.getRemarks()){
						   			valuelist.setRemarks("NA");
						   		}else{
						   			valuelist.setRemarks(eFmFmEmployeeProjectDetailsPO.getRemarks());
						   		}						   		
						   		employeeDetailBO.updateEmployeeProjectDetails(valuelist);
				   			}   			
				   		  }			   			
				   		}          	
		 	 }catch (Exception e) {	           
				       log.debug("statusFailedAddProjectDetails"+e);	
				       e.printStackTrace();
				       responce.put("status", "failed");				       
		     } 
		        
   	    log.info("serviceEnd -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
    
    @POST
    @Path("/deligatedUserByReportingManager")
    public Response deligatedUserByReportingManager(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO) throws UnsupportedEncodingException {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");
        Map<String, Object> responce = new HashMap<String, Object>();       
       /* log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientProjectDetailsPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}*/
   	  	    log.info("serviceStart -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());   	      
		   	 try {
		   		List<EFmFmClientProjectDetailsPO> projectDetails = eFmFmEmployeeProjectDetailsPO.getProjectList();		   	 
			   	 for(EFmFmClientProjectDetailsPO projectValue:projectDetails){
				    List<EFmFmEmployeeProjectDetailsPO> projectAllocationExist=employeeDetailBO.getAllProjectUserByrepManagerWithProjectId
			    		(Integer.parseInt(eFmFmEmployeeProjectDetailsPO.getReportingManagerUserId()),
			    				eFmFmEmployeeProjectDetailsPO.getCombinedFacility(), 
			    				projectValue.getProjectId());       	
			   		if(!(projectAllocationExist.isEmpty())){	
					   	 for(EFmFmEmployeeProjectDetailsPO projectValues:projectAllocationExist){				   		 
					   		EFmFmEmployeeProjectDetailsPO projectAllocationDetails = new EFmFmEmployeeProjectDetailsPO();				   		
					   		projectAllocationDetails.setCreatedBy(String.valueOf(eFmFmEmployeeProjectDetailsPO.getUserId()));
					   		projectAllocationDetails.setCreatedDate(new Date());
					   		projectAllocationDetails.setIsActive(projectValues.getIsActive());				   		
					   		projectAllocationDetails.seteFmFmClientProjectDetails(projectValues.geteFmFmClientProjectDetails());				   		
					   		projectAllocationDetails.setReportingManagerUserId(String.valueOf(eFmFmEmployeeProjectDetailsPO.getDelegatedUserId()));	
					   		projectAllocationDetails.setIsDelegatedUser(1);
					   	    projectAllocationDetails.setProjectAllocationStarDate(projectValues.getProjectAllocationStarDate());
					   		projectAllocationDetails.setProjectAllocationEndDate(projectValues.getProjectAllocationEndDate());
					   		projectAllocationDetails.setDelegatedBy(Integer.parseInt(eFmFmEmployeeProjectDetailsPO.getReportingManagerUserId()));
					   		projectAllocationDetails.setEfmFmUserMaster(projectValues.getEfmFmUserMaster());
					   		projectAllocationDetails.setRemarks(projectValues.getRemarks());
					   		List<EFmFmEmployeeProjectDetailsPO> deligatedUserExist=employeeDetailBO.
					   				getClientProjectIdByMangerAndEmployee(Integer.valueOf(eFmFmEmployeeProjectDetailsPO.getReportingManagerUserId()),
					   						eFmFmEmployeeProjectDetailsPO.getCombinedFacility(),
					   						eFmFmEmployeeProjectDetailsPO.geteFmFmClientProjectDetails().getProjectId(),eFmFmEmployeeProjectDetailsPO.getDelegatedUserId());				   				
					   		if(deligatedUserExist.isEmpty()){	
					   			employeeDetailBO.addEmployeeProjectDetails(projectAllocationDetails);	
						   		projectValues.setDelegatedUserId(eFmFmEmployeeProjectDetailsPO.getDelegatedUserId());
						   		employeeDetailBO.updateEmployeeProjectDetails(projectValues);
					   		}else{
					   			responce.put("status", "already delegated for this user based on this project");  	    
			        		    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					   		}
					   		
					   	 }				   	
	            	}else{
	            		responce.put("status", "No Record found for this Reporting mangager with project");  	    
	        		    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	            	}
			   	 }
		 	 }catch (Exception e) {	           
				       log.debug("statusFailedAddProjectDetails"+e);	
				       e.printStackTrace();
				       responce.put("status", "failed");				       
		     } 
		        
   	    log.info("serviceEnd -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
    
    @POST
    @Path("/listofProjectAllocationDetails")
    public Response listofProjectAllocationDetails(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO) throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");
        Map<String, Object> responce = new HashMap<String, Object>();   
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
       /* log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientProjectDetailsPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}*/
   	  	log.info("serviceStart -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
   	    List<Map<String, Object>> projectData = new ArrayList<Map<String, Object>>();   	    
   	    List<EFmFmEmployeeProjectDetailsPO> projectAllocationExist=employeeDetailBO.getAllProjectUserByrepManagerWithProjectId
   	    		(Integer.parseInt(eFmFmEmployeeProjectDetailsPO.getReportingManagerUserId()),
   	    				eFmFmEmployeeProjectDetailsPO.getCombinedFacility(), 
   	    				eFmFmEmployeeProjectDetailsPO.geteFmFmClientProjectDetails().getProjectId());
   	    if(!(projectAllocationExist.isEmpty())){
            for (EFmFmEmployeeProjectDetailsPO listofUser : projectAllocationExist) {            	
                Map<String, Object> listValues = new HashMap<String, Object>();             
                listValues.put("projectName", listofUser.geteFmFmClientProjectDetails().getEmployeeProjectName());
                listValues.put("projectId", listofUser.geteFmFmClientProjectDetails().getProjectId());
                listValues.put("clientProjectId", listofUser.geteFmFmClientProjectDetails().getClientProjectId());
                listValues.put("projectStartDate", dateFormatter.format(listofUser.getProjectAllocationStarDate()));
                listValues.put("projectEndDate", dateFormatter.format(listofUser.getProjectAllocationEndDate()));                
                listValues.put("employeeId", listofUser.getEfmFmUserMaster().getEmployeeId() +" | "+new String(Base64.getDecoder().decode(listofUser.getEfmFmUserMaster().getFirstName()),"utf-8"));                             
                projectData.add(listValues);  
                
            }
        }
   	    log.info("serviceEnd -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
        return Response.ok(projectData, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/AllEmployeeDetails")
    public Response allListEmployeeDetails(EFmFmUserMasterPO eFmFmUserMasterPO) throws UnsupportedEncodingException {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
	   	  log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());
	
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
		if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMasterPO.getUserId());
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
		
		
		}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        long l1 = System.currentTimeMillis();
        log.info("time" + System.currentTimeMillis());
        List<EFmFmUserMasterPO> employeeMasterData = employeeDetailBO
                .getListOfEmployeeDetailsByBranch(eFmFmUserMasterPO.getCombinedFacility());
        log.info("qwery done");
        List<Map<String, Object>> employeeProfile = new ArrayList<Map<String, Object>>();
        if (!(employeeMasterData.isEmpty())) {
            for (EFmFmUserMasterPO empProfileDetails : employeeMasterData) {
                Map<String, Object> profileValues = new HashMap<String, Object>();
//                log.info("empProfileDetails.getEmployeeId())"+empProfileDetails.getEmployeeId());
                profileValues.put("employeeId", empProfileDetails.getEmployeeId());
                profileValues.put("userId", empProfileDetails.getUserId());
                profileValues.put("employeeName",empProfileDetails.getEmployeeId()+"|"+new String(Base64.getDecoder().decode(empProfileDetails.getFirstName()), "utf-8"));               
                employeeProfile.add(profileValues);
            }
        }
  
   	 	log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(employeeProfile, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/listofDeligatedUserDetailsByUserAndAdmin")
    public Response listofDeligatedUserDetailsByUserAndAdmin(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO) throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");
        Map<String, Object> responce = new HashMap<String, Object>();   
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
       /* log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientProjectDetailsPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}*/
   	  	log.info("serviceStart -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
   	    List<Map<String, Object>> projectData = new ArrayList<Map<String, Object>>(); 
   	 List<EFmFmEmployeeProjectDetailsPO> deligatedUserDetails=null;
   	    if(eFmFmEmployeeProjectDetailsPO.getDelegatedCall()==1){
   	    	deligatedUserDetails=employeeDetailBO.getDeligatedUserDetails
   	    			(eFmFmEmployeeProjectDetailsPO.getCombinedFacility()
   	    					,Integer.parseInt(eFmFmEmployeeProjectDetailsPO.getReportingManagerUserId()));
   	    }else{
   	       deligatedUserDetails=employeeDetailBO.getAllDeligatedUserDetails(eFmFmEmployeeProjectDetailsPO.getCombinedFacility()); 
   	    }  	    
   	    if(!(deligatedUserDetails.isEmpty())){
            for (EFmFmEmployeeProjectDetailsPO listofUser : deligatedUserDetails) {            	
                Map<String, Object> listValues = new HashMap<String, Object>();             
                listValues.put("projectName", listofUser.geteFmFmClientProjectDetails().getEmployeeProjectName());
                listValues.put("empProjectId", listofUser.getEmpProjectId());
                listValues.put("projectId", listofUser.geteFmFmClientProjectDetails().getProjectId());
                listValues.put("clientProjectId", listofUser.geteFmFmClientProjectDetails().getClientProjectId());
                listValues.put("projectStartDate", dateFormatter.format(listofUser.getProjectAllocationStarDate()));
                listValues.put("projectEndDate", dateFormatter.format(listofUser.getProjectAllocationEndDate())); 
                listValues.put("delegatedBy", listofUser.getDelegatedBy()); 
                listValues.put("reportingManagerUserId", listofUser.getReportingManagerUserId());
                List<EFmFmUserMasterPO> delegatedUserId =employeeDetailBO.getParticularEmpDetailsFromUserIdWithOutStatus
                		(Integer.parseInt(listofUser.getReportingManagerUserId()),eFmFmEmployeeProjectDetailsPO.getCombinedFacility());
                if(!(delegatedUserId.isEmpty())){
	               listValues.put("employeeId", delegatedUserId.get(0).getEmployeeId() +" | "+ new String(Base64.getDecoder().decode(delegatedUserId.get(0).getFirstName()),"utf-8"));                            
                }
                List<EFmFmUserMasterPO> delegatedByUser =employeeDetailBO.getParticularEmpDetailsFromUserIdWithOutStatus
                		(listofUser.getDelegatedBy(),eFmFmEmployeeProjectDetailsPO.getCombinedFacility());
                if(!(delegatedByUser.isEmpty())){
	                listValues.put("delegatedBy", delegatedByUser.get(0).getEmployeeId() +" | "+ new String(Base64.getDecoder().decode(delegatedByUser.get(0).getFirstName()),"utf-8"));                             
	                projectData.add(listValues); 
                }
            }
        }
   	    log.info("serviceEnd -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
        return Response.ok(projectData, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/modifyDeligatedUser")
    public Response modifyDeligatedUser(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO) throws UnsupportedEncodingException {		
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");
        Map<String, Object> responce = new HashMap<String, Object>();        
       /* log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientProjectDetailsPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}*/
   	  	log.info("serviceStart -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());

   	 List<EFmFmEmployeeProjectDetailsPO> deligatedUserDetails=employeeDetailBO.
   			 getDeligatedUserDetailsByReportingManager(eFmFmEmployeeProjectDetailsPO.geteFmFmClientProjectDetails().geteFmFmClientBranchPO().getBranchId(), 
   					 eFmFmEmployeeProjectDetailsPO.getDelegatedBy(),
   					 Integer.parseInt(eFmFmEmployeeProjectDetailsPO.getReportingManagerUserId()),
   					 eFmFmEmployeeProjectDetailsPO.geteFmFmClientProjectDetails().getProjectId());	    
   	    if(!(deligatedUserDetails.isEmpty())){
            for (EFmFmEmployeeProjectDetailsPO listofUser : deligatedUserDetails) {  
            	listofUser.setReportingManagerUserId(String.valueOf(eFmFmEmployeeProjectDetailsPO.getDelegatedUserId()));                                            
            	employeeDetailBO.updateEmployeeProjectDetails(listofUser);                
            }
        }else{
        	responce.put("status", "No Record found for this Reporting mangager with project");  	    
		    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
   	    log.info("serviceEnd -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    @POST
    @Path("/disableDeligatedUser")
    public Response disableDeligatedUser(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO) throws UnsupportedEncodingException {		
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");
        Map<String, Object> responce = new HashMap<String, Object>();        
       /* log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientProjectDetailsPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}*/
   	  	log.info("serviceStart -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());

   	 List<EFmFmEmployeeProjectDetailsPO> deligatedUserDetails=employeeDetailBO.
   			getParticularProjectAllocation(eFmFmEmployeeProjectDetailsPO.getEmpProjectId());	    
   	    if(!(deligatedUserDetails.isEmpty())){            
   	    	 deligatedUserDetails.get(0).setIsActive("D");                                     
             employeeDetailBO.updateEmployeeProjectDetails(deligatedUserDetails.get(0));                
            
        }else{
        	responce.put("status", "No Record found for this Reporting mangager with project");  	    
		    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
   	    log.info("serviceEnd -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/paginationTest")
    public Response paginationTest(EFmFmUserMasterPO eFmFmUserMasterPO) throws UnsupportedEncodingException {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
	   	  log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId()); 	   

        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
      
        List<EFmFmUserMasterPO> employeeMasterData = employeeDetailBO
                .getAllEmployeeDetailsByPagination(eFmFmUserMasterPO.getCombinedFacility(), eFmFmUserMasterPO.getStartPgNo(),
                		eFmFmUserMasterPO.getEndPgNo());
        log.info("qwery done");
        List<Map<String, Object>> employeeProfile = new ArrayList<Map<String, Object>>();
        if (!(employeeMasterData.isEmpty())) {
            for (EFmFmUserMasterPO empProfileDetails : employeeMasterData) {
                Map<String, Object> profileValues = new HashMap<String, Object>();
                log.info("empProfileDetails.getEmployeeId())"+empProfileDetails.getEmployeeId());
                profileValues.put("employeeId", empProfileDetails.getEmployeeId());
                profileValues.put("userId", empProfileDetails.getUserId());
                profileValues.put("employeeName",new String(Base64.getDecoder().decode(empProfileDetails.getFirstName()), "utf-8"));   
                profileValues.put("emailId", new String(Base64.getDecoder().decode(empProfileDetails.getEmailId()), "utf-8"));                
                profileValues.put("mobileNumber", new String(Base64.getDecoder().decode(empProfileDetails.getMobileNumber()), "utf-8"));
               employeeProfile.add(profileValues);
            }
        }
        log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(employeeProfile, MediaType.APPLICATION_JSON).build();
    }
    
    
    

}
