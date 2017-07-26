package com.newtglobal.eFmFmFleet.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IEmployeeDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IFacilityBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.MessagingService;
import com.newtglobal.eFmFmFleet.eFmFmFleet.PasswordEncryption;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientUserRolePO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverFeedbackPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeModuleMappingWithBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserPasswordPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/user")
@Consumes("application/json")
@Produces("application/json")
public class RegistrationService {
    String result = "";
    private static Log log = LogFactory.getLog(RegistrationService.class);
	DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");

	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

    
    /**
     * 
     * @param Device
     *            Registration with a specific Number
     * 
     * @return driver details exist check
     *  SMS DLR request..status report form sms provider
     */
    @GET
    @Path("/dlrrequest")
    public Response dlrRequest(@QueryParam("myid") String myid, @QueryParam("status") String status,
            @QueryParam("reciever") String reciever, @QueryParam("updated_on") String updated_on,
            @QueryParam("res") String res) {
        log.info("myid" + myid);
        log.info("status" + status);
        log.info("reciever" + reciever);
        log.info("updated_on" + updated_on);
        log.info("res" + res);
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken")))){
//
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
       
        responce.put("status", "success");
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /**
     * 
     * @param Device
     *            Registration with a specific Number
     * 
     * @return driver details exist check
     */

    @POST
    @Path("/deviceregisteration")
    public Response deviceRegistration(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) {
        IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
      	 log.info("serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
//
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
//        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
//     	   if (!(userDetail.isEmpty())) {
//     		String jwtToken = "";
//     		try {
//     		 JwtTokenGenerator token = new JwtTokenGenerator();
//     		 jwtToken = token.generateToken();
//     		 userDetail.get(0).setAuthorizationToken(jwtToken);
//     		 userDetail.get(0).setTokenGenerationTime(new Date());
//     		 userMasterBO.update(userDetail.get(0));
//     		} catch (Exception e) {
//     		 log.info("error" + e);
//     		}
//        }
//        	}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}

        
        log.info("eFmFmDeviceMasterPO.getMobileNumber()" + eFmFmDeviceMasterPO.getMobileNumber());
        log.info(
                "eFmFmDeviceMasterPO.geteFmFmClientBranc" + eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId());
		List<EFmFmDeviceMasterPO> deviceDetail = iVehicleCheckInBO.deviceImeiNumberExistsCheck(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getImeiNumber());
        if (!(deviceDetail.isEmpty())) {
            deviceDetail.get(0).setStatus("Y");
            deviceDetail.get(0).setDeviceType("Android");
            deviceDetail.get(0).setIsActive("Y");
            deviceDetail.get(0).setImeiNumber(eFmFmDeviceMasterPO.getImeiNumber());
            deviceDetail.get(0).setDeviceToken(eFmFmDeviceMasterPO.getDeviceToken());
            iVehicleCheckInBO.update(deviceDetail.get(0));
            responce.put("status", "success");
       	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        eFmFmDeviceMasterPO.setStatus("Y");
        eFmFmDeviceMasterPO.setImeiNumber(eFmFmDeviceMasterPO.getImeiNumber());
        eFmFmDeviceMasterPO.setDeviceType("Android");
        eFmFmDeviceMasterPO.setDeviceToken(eFmFmDeviceMasterPO.getDeviceToken());
        eFmFmDeviceMasterPO.setIsActive("Y");
        iVehicleCheckInBO.save(eFmFmDeviceMasterPO);
        responce.put("status", "success");
   	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /**
     * 
     * @param Device
     *            Splash Activity
     * 
     * @return device exist or check in check
     */

    @POST
    @Path("/devicecheck")
    public Response deviceSplashCheck(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) {
        IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
//        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//        log.info("serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
//        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
//     	   if (!(userDetail.isEmpty())) {
//     		String jwtToken = "";
//     		try {
//     		 JwtTokenGenerator token = new JwtTokenGenerator();
//     		 jwtToken = token.generateToken();
//     		 userDetail.get(0).setAuthorizationToken(jwtToken);
//     		 userDetail.get(0).setTokenGenerationTime(new Date());
//     		 userMasterBO.update(userDetail.get(0));
//     		} catch (Exception e) {
//     		 log.info("error" + e);
//     		}
//     	   	}	
//        	}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
   	 
   
		List<EFmFmDeviceMasterPO> deviceDetail = iVehicleCheckInBO.deviceImeiNumberExistsCheck(String.valueOf(eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId()),eFmFmDeviceMasterPO.getImeiNumber());

        if (deviceDetail.isEmpty()) {
            responce.put("status", "success");
       	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        } else {
            List<EFmFmVehicleCheckInPO> checkInDetail = iVehicleCheckInBO.getParticularCheckedInDeviceDetails(
                    eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId(), deviceDetail.get(0).getDeviceId());
            if (checkInDetail.isEmpty()) {
                List<EFmFmVehicleCheckInPO> lastCheckInDetail = iVehicleCheckInBO.getLastCheckedOutInDeviceDetails(
                        eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId(), deviceDetail.get(0).getDeviceId());
                if (!(lastCheckInDetail.isEmpty()))
                    responce.put("vehicleNum", lastCheckInDetail.get(lastCheckInDetail.size() - 1)
                            .getEfmFmVehicleMaster().getVehicleNumber());
                responce.put("status", "checkin");
           	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            if (!(checkInDetail.isEmpty())) {
                DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                responce.put("driverId", checkInDetail.get(0).getEfmFmDriverMaster().getDriverId());
                responce.put("driverName", checkInDetail.get(0).getEfmFmDriverMaster().getFirstName());
                responce.put("driverNumber", checkInDetail.get(0).getEfmFmDriverMaster().getMobileNumber());
                responce.put("autoCallAndSms", checkInDetail.get(0).getEfmFmDriverMaster().getEfmFmVendorMaster()
                        .geteFmFmClientBranchPO().getDriverDeviceAutoCallAndsms());
                responce.put("licenceValid",
                        dateFormatter.format(checkInDetail.get(0).getEfmFmDriverMaster().getLicenceValid()));
                responce.put("vehicleNumber", checkInDetail.get(0).getEfmFmVehicleMaster().getVehicleNumber());
                responce.put("vehicleId", checkInDetail.get(0).getEfmFmVehicleMaster().getVehicleId());
                responce.put("capacity", checkInDetail.get(0).getEfmFmVehicleMaster().getCapacity());
                responce.put("vehicleCheckList", checkInDetail.get(0).getEfmFmDriverMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getVehiceCheckList());
                responce.put("driverHistory", checkInDetail.get(0).getEfmFmDriverMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getDriverTripHistory());

                try {
                    String profilePicPath = "";
                    if (checkInDetail.get(0).getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO()
                            .getDriverDeviceDriverProfilePicture().equalsIgnoreCase("Yes")) {
                        profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
                                null);
                    } else {
                        profilePicPath = ContextLoader.getContext().getMessage("default.profilePic", null, "profilePic",
                                null);
                    }
                    responce.put("profilePic",
                            profilePicPath + checkInDetail.get(0).getEfmFmDriverMaster().getProfilePicPath().substring(
                                    checkInDetail.get(0).getEfmFmDriverMaster().getProfilePicPath().indexOf("upload")
                                            - 1));
                } catch (Exception e) {
                    String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
                            "profilePic", null);
                    responce.put("profilePic", defaultProfilePic);
                }

                
                responce.put("status", "home");
           	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
        }
        responce.put("status", "success");
   	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
    
    //temp pass change service for mobile device
    
    @POST
    @Path("/tempPassChange")
    public Response tempPassChangeService(EFmFmUserMasterPO eFmFmUserMasterPO) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
      	 log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()) && !(httpRequest.getHeader("authenticationToken").equalsIgnoreCase(null)))){
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
      
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        if(!(eFmFmUserMasterPO.getNewPassword().matches(ContextLoader.getContext().getMessage("strong.password", null, "password", null)))){
            responce.put("status", "wrongPattern");
       	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();  
        }
        List<EFmFmUserMasterPO> employeeDetails = employeeDetailBO
                .getParticularEmpDetailsFromMobileNumberAndBranhId(Base64.getEncoder().encodeToString(eFmFmUserMasterPO.getMobileNumber().getBytes("utf-8")),eFmFmUserMasterPO.getBranchId());
        log.info("splace tempPassChange");       
        if (!(employeeDetails.isEmpty()) && employeeDetails.get(0).getStatus().equalsIgnoreCase("N")) {
                responce.put("status", "disable");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();    
        }  
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!(employeeDetails.isEmpty())) {
            boolean passexist = false;
            List<EFmFmUserPasswordPO> passDetails = employeeDetailBO.getUserPasswordDetailsFromUserIdAndBranchId(
                    employeeDetails.get(0).getUserId());
            if (!(passDetails.isEmpty())) {
                if (passDetails.size() > employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass()) {
                    for(int i=0;i<((passDetails.size())-employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass());i++){
                        employeeDetailBO.deleteLastPasswordForParticularEmployeCrossingDefineLimit(
                                passDetails.get(i).getPasswordId());
                    }
                }
                log.info("passDetails.size()" + passDetails.size());
                for (EFmFmUserPasswordPO pass : passDetails) {
                    if (encoder.matches(eFmFmUserMasterPO.getNewPassword(), pass.getPassword())) {
                        passexist = true;
                        break;
                    }

                }
            }
            if (passexist) {
                if (passDetails.size() <= employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass())
                    responce.put("numberOfPasswords", passDetails.size());
                else {
                    responce.put("numberOfPasswords",employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass());
                }
                responce.put("status", "oldpass");
                responce.put("lastChangeDateTime",
                        dateFormatter.format(passDetails.get(passDetails.size() - 1).getCreationTime()));
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            employeeDetails.get(0)
                    .setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmUserMasterPO.getNewPassword()));
            employeeDetails.get(0).setPasswordChangeDate(new Date());
            employeeDetails.get(0).setLastLoginTime(new Date());
            employeeDetails.get(0).setTempPassWordChange(false);
            employeeDetailBO.update(employeeDetails.get(0));
            EFmFmUserPasswordPO userPassword = new EFmFmUserPasswordPO();
            EFmFmClientBranchPO clientDetail = new EFmFmClientBranchPO();
            clientDetail.setBranchId(eFmFmUserMasterPO.getBranchId());
            userPassword.setCreationTime(new Date());
            userPassword.setEfmFmUserMaster(employeeDetails.get(0));
            userPassword.setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmUserMasterPO.getNewPassword()));
            userPassword.seteFmFmClientBranchPO(clientDetail);
            employeeDetailBO.save(userPassword);
            if (!(passDetails.isEmpty())) {
                if (passDetails.size() == employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass()) {
                    employeeDetailBO.deleteLastPasswordForParticularEmployeCrossingDefineLimit(
                            passDetails.get(0).getPasswordId());
                }
            }

            responce.put("status", "success");
       	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        responce.put("status", "fail");
   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /**
     * 
     * 
     * @param driverMaster
     *            checkin the particular driver with a particular vehicle from
     *            mobile device
     * @return if enter vehicle number is exist
     */

    @POST
    @Path("/deviceCheckIn")
    public Response driverCheckInFromDevice(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) {
        IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
    	 log.info("serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
//
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
//        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
//     	   if (!(userDetail.isEmpty())) {
//     		String jwtToken = "";
//     		try {
//     		 JwtTokenGenerator token = new JwtTokenGenerator();
//     		 jwtToken = token.generateToken();
//     		 userDetail.get(0).setAuthorizationToken(jwtToken);
//     		 userDetail.get(0).setTokenGenerationTime(new Date());
//     		 userMasterBO.update(userDetail.get(0));
//     		} catch (Exception e) {
//     		 log.info("error" + e);
//     		}
//        }
//        	}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
//  
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");

        
        EFmFmVehicleMasterPO vehicleDetails = vehicleCheckInBO.getParticularVehicleDetails(
                eFmFmDeviceMasterPO.getVehicleNum(), eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId());
        List<EFmFmDriverMasterPO> driverDetails = approvalBO
                .getParticularDriverDetailFromDeriverId(eFmFmDeviceMasterPO.getDriverId());
        
       log.info("etBranchId"+eFmFmDeviceMasterPO.getVehicleNum()+eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId());
    
       
         if (vehicleDetails != null) {
        	 if(vehicleDetails.getVehicleNumber().contains("DUMMY")){
                 responce.put("status", "dummy");
            	   log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
                 return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
             }
        	 if(vehicleDetails.getEfmFmVendorMaster().getStatus().equalsIgnoreCase("P")){
                 responce.put("status", "vendorNotApproved");
            	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
                 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
             }
            if (vehicleDetails.getStatus().equalsIgnoreCase("P") || vehicleDetails.getStatus().equalsIgnoreCase("R")) {
                responce.put("status", "VNA");
           	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
        } else if (vehicleDetails == null) {
            responce.put("status", "Vwrong");
       	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        if (!(driverDetails.isEmpty())) {
            if (driverDetails.get(0).getStatus().equalsIgnoreCase("P")
                    || driverDetails.get(0).getStatus().equalsIgnoreCase("R")) {
                responce.put("status", "DNA");
           	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
        } else if (driverDetails.isEmpty()) {
            responce.put("status", "Dwrong");
       	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
        EFmFmVehicleMasterPO vehicleMaster = new EFmFmVehicleMasterPO();
        vehicleMaster.setVehicleId(vehicleDetails.getVehicleId());
        EFmFmDriverMasterPO driverMaster = new EFmFmDriverMasterPO();
        driverMaster.setDriverId(driverDetails.get(0).getDriverId());
        eFmFmVehicleCheckInPO.setCheckInTime(new Date());
        eFmFmVehicleCheckInPO.setReadFlg("Y");
        eFmFmVehicleCheckInPO.setCheckInType("mobile");
        eFmFmVehicleCheckInPO.setAdminMailTriggerStatus(false);
        eFmFmVehicleCheckInPO.setSupervisorMailTriggerStatus(false);
        eFmFmVehicleCheckInPO.setEfmFmVehicleMaster(vehicleMaster);
        List<EFmFmVehicleCheckInPO> checkInVehicle = vehicleCheckInBO.getParticularCheckedInVehicles(String.valueOf(eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId()), vehicleDetails.getVehicleId());
        if (!(checkInVehicle.isEmpty())) {
            responce.put("status", "VcheckedIn");
       	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        eFmFmVehicleCheckInPO.setEfmFmDriverMaster(driverMaster);
        List<EFmFmVehicleCheckInPO> checkInDriver = vehicleCheckInBO
                .getParticularCheckedInDriver(eFmFmVehicleCheckInPO);
        if (!(checkInDriver.isEmpty())) {
            responce.put("status", "DcheckedIn");
       	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
		List<EFmFmDeviceMasterPO> deviceDetail = vehicleCheckInBO.deviceImeiNumberExistsCheck(String.valueOf(eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId()),eFmFmDeviceMasterPO.getImeiNumber());

        if (!(driverDetails.get(0).getEfmFmVendorMaster().getVendorId() == vehicleDetails.getEfmFmVendorMaster()
                .getVendorId())) {
            responce.put("status", "mismatch");
       	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        EFmFmDeviceMasterPO deviceMaster = new EFmFmDeviceMasterPO();
        deviceMaster.setDeviceId(deviceDetail.get(0).getDeviceId());
        eFmFmVehicleCheckInPO.seteFmFmDeviceMaster(deviceMaster);
        eFmFmVehicleCheckInPO.setStatus("Y");
        vehicleCheckInBO.vehicleDriverCheckIn(eFmFmVehicleCheckInPO);
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        responce.put("driverName", driverDetails.get(0).getFirstName());
        responce.put("driverId", driverDetails.get(0).getDriverId());
        responce.put("driverNumber", driverDetails.get(0).getMobileNumber());
        responce.put("licenceValid", dateFormatter.format(driverDetails.get(0).getLicenceValid()));
        responce.put("vehicleNumber", vehicleDetails.getVehicleNumber());
        responce.put("vehicleId", vehicleDetails.getVehicleId());
        responce.put("autoCallAndSms",driverDetails.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO().getDriverDeviceAutoCallAndsms());
        responce.put("vehicleCheckList", driverDetails.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO().getVehiceCheckList());
        responce.put("driverHistory", driverDetails.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO().getDriverTripHistory());
        try {
            String profilePicPath = "";
            if (driverDetails.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO()
                    .getDriverDeviceDriverProfilePicture().equalsIgnoreCase("Yes")) {
                profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic", null);
            } else {
                profilePicPath = ContextLoader.getContext().getMessage("default.profilePic", null, "profilePic", null);
            }
            responce.put("profilePic", profilePicPath + driverDetails.get(0).getProfilePicPath()
                    .substring(driverDetails.get(0).getProfilePicPath().indexOf("upload") - 1));
        } catch (Exception e) {
            String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null, "profilePic",
                    null);
            responce.put("profilePic", defaultProfilePic);
        }
        responce.put("capacity", vehicleDetails.getCapacity());
        responce.put("status", "success");
   	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /**
     * 
     * 
     * @param driverMaster
     *            checkout the particular driver with a particular vehicle from
     *            device
     * @return return Success
     */

    @POST
    @Path("/driverCheckOut")
    public Response driverCheckOutFromDevice(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) {
        IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        log.info("serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
//
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
//        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
//     	   if (!(userDetail.isEmpty())) {
//     		String jwtToken = "";
//     		try {
//     		 JwtTokenGenerator token = new JwtTokenGenerator();
//     		 jwtToken = token.generateToken();
//     		 userDetail.get(0).setAuthorizationToken(jwtToken);
//     		 userDetail.get(0).setTokenGenerationTime(new Date());
//     		 userMasterBO.update(userDetail.get(0));
//     		} catch (Exception e) {
//     		 log.info("error" + e);
//     		}
//        }
//        	}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
   	 
		List<EFmFmDeviceMasterPO> deviceDetail = vehicleCheckInBO.deviceImeiNumberExistsCheck(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getImeiNumber());

        EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
        eFmFmVehicleCheckInPO.setCheckInTime(new Date());
        eFmFmVehicleCheckInPO.seteFmFmDeviceMaster(deviceDetail.get(0));
        List<EFmFmVehicleCheckInPO> vehicleCheckInPO = vehicleCheckInBO
                .checkedOutParticularDriver(eFmFmVehicleCheckInPO);
        if (!(vehicleCheckInPO.isEmpty())) {
            if (vehicleCheckInPO.get(0).getStatus().equalsIgnoreCase("N")) {
                responce.put("status", "fail");
           	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            vehicleCheckInPO.get(vehicleCheckInPO.size() - 1).setStatus("N");
            // Means it is successfully CheckOut by driver on time
            vehicleCheckInPO.get(0).setReadFlg("S");
            vehicleCheckInPO.get(vehicleCheckInPO.size() - 1).setCheckOutTime(new Date());
            vehicleCheckInBO.update(vehicleCheckInPO.get(vehicleCheckInPO.size() - 1));
        }
        responce.put("status", "success");
   	 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /**
     * 
     * @param driver
     *            profile
     * 
     * @return driver details
     */

    @POST
    @Path("/driverprofile")
    public Response getDriverProfile(EFmFmDeviceMasterPO deviceMasterPO) {
        IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
      	 log.info("serviceStart -UserId :" + deviceMasterPO.getUserId());
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),deviceMasterPO.getUserId()))){
//
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
//        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(deviceMasterPO.getUserId());
//     	   if (!(userDetail.isEmpty())) {
//     		String jwtToken = "";
//     		try {
//     		 JwtTokenGenerator token = new JwtTokenGenerator();
//     		 jwtToken = token.generateToken();
//     		 userDetail.get(0).setAuthorizationToken(jwtToken);
//     		 userDetail.get(0).setTokenGenerationTime(new Date());
//     		 userMasterBO.update(userDetail.get(0));
//     		} catch (Exception e) {
//     		 log.info("error" + e);
//     		}
//        }
//        	}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}

 		List<EFmFmDeviceMasterPO> deviceDetail = iVehicleCheckInBO.deviceImeiNumberExistsCheck(String.valueOf(deviceMasterPO.geteFmFmClientBranchPO().getBranchId()),deviceMasterPO.getImeiNumber());

        List<EFmFmVehicleCheckInPO> checkInDriverDetail = iVehicleCheckInBO.getParticularCheckedInDeviceDetails(
                deviceMasterPO.geteFmFmClientBranchPO().getBranchId(), deviceDetail.get(0).getDeviceId());
        log.info("Inside driver profile" + checkInDriverDetail.size());
        if (!(checkInDriverDetail.isEmpty())) {
            DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
            responce.put("driverName", checkInDriverDetail.get(0).getEfmFmDriverMaster().getFirstName());
            responce.put("driverId", checkInDriverDetail.get(0).getEfmFmDriverMaster().getDriverId());
            responce.put("driverNumber", checkInDriverDetail.get(0).getEfmFmDriverMaster().getMobileNumber());
            responce.put("licenceValid",
                    dateFormatter.format(checkInDriverDetail.get(0).getEfmFmDriverMaster().getLicenceValid()));
            responce.put("vehicleNumber", checkInDriverDetail.get(0).getEfmFmVehicleMaster().getVehicleNumber());
            responce.put("vehicleId", checkInDriverDetail.get(0).getEfmFmVehicleMaster().getVehicleId());
            try {
                String profilePicPath = "";
                if (checkInDriverDetail.get(0).getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO()
                        .getDriverDeviceDriverProfilePicture().equalsIgnoreCase("Yes")) {
                    profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
                            null);
                } else {
                    profilePicPath = ContextLoader.getContext().getMessage("default.profilePic", null, "profilePic",
                            null);
                }
                responce.put("profilePic", profilePicPath
                        + checkInDriverDetail.get(0).getEfmFmDriverMaster().getProfilePicPath().substring(
                                checkInDriverDetail.get(0).getEfmFmDriverMaster().getProfilePicPath().indexOf("upload")
                                        - 1));
            } catch (Exception e) {
                String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
                        "profilePic", null);
                responce.put("profilePic", defaultProfilePic);
            }
            responce.put("capacity", checkInDriverDetail.get(0).getEfmFmVehicleMaster().getCapacity());
            responce.put("status", "success");
        } else {
            List<EFmFmVehicleCheckInPO> lastCheckInDetail = iVehicleCheckInBO.getLastCheckedOutInDeviceDetails(
                    deviceMasterPO.geteFmFmClientBranchPO().getBranchId(), deviceDetail.get(0).getDeviceId());
            log.info("Inside driver profile CheckOut" + checkInDriverDetail.size());
            if (!(lastCheckInDetail.isEmpty())) {
                responce.put("vehicleNum",
                        lastCheckInDetail.get(lastCheckInDetail.size() - 1).getEfmFmVehicleMaster().getVehicleNumber());
                responce.put("status", "fail");
            }
        }
   	 log.info("serviceEnd -UserId :" + deviceMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /*
     * 
     * Employee splace activity Check
     */

    @POST
    @Path("/registercheck")
    public Response registerCheck(EFmFmUserMasterPO eFmFmUserMasterPO) throws IOException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
         IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
         IFacilityBO iFacilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
         Map<String, Object> responce = new HashMap<String, Object>();
       		
         log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
      	 log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());
//  		try{
// 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){
// 				responce.put("status", "invalidRequest");
// 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
// 			}
// 		
// 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMasterPO.getUserId());
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
        
        List<Map<String, Object>> userAccessData = new ArrayList<Map<String, Object>>();
        log.info("eFmFmUserMasterPO.getDeviceId()"+eFmFmUserMasterPO.getDeviceId());
        log.info("eFmFmUserMasterPO.getBranchId()"+eFmFmUserMasterPO.getBranchId());

        log.info("eFmFmUserMasterPO.getMobileNumber()"+eFmFmUserMasterPO.getMobileNumber());

        List<EFmFmUserMasterPO> userMasterDetail = employeeDetailBO
                .getParticularEmployeeDetailsFromDeviceIdAndMobileNumber(eFmFmUserMasterPO.getDeviceId(),
                        eFmFmUserMasterPO.getBranchId(), Base64.getEncoder().encodeToString(eFmFmUserMasterPO.getMobileNumber().getBytes("utf-8")));
        log.info("splace activity");
        if (!(userMasterDetail.isEmpty())) {
        	
        	if(userMasterDetail.size()>1)
        	
        	//iFacilityBO.getAllFacilitiesAttachedToUser(userMasterDetail.get(0).getUserId())
        	
            if (userMasterDetail.get(0).getStatus().equalsIgnoreCase("N")) {
                responce.put("status", "disable");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            List<EFmFmClientUserRolePO> userDetail = userMasterBO
                    .getAdminUserRoleByUserName(userMasterDetail.get(0).getUserName());
            if (!(userDetail.isEmpty()) && !(userMasterDetail.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO"))) {
                if (passwordChangeDate(userMasterDetail.get(0).getPasswordChangeDate(),
                        userMasterDetail.get(0).geteFmFmClientBranchPO()
                                .getPasswordResetPeriodForAdminInDays()) < new Date().getTime()
                        && userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase("admin")) {
                    log.info("inside logindenied");
                    responce.put("status", "passReset");
               	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }
            }
            if (!(userDetail.isEmpty()) && !(userMasterDetail.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO"))) {
                if (passwordChangeDate(userMasterDetail.get(0).getPasswordChangeDate(),
                        userMasterDetail.get(0).geteFmFmClientBranchPO()
                                .getPasswordResetPeriodForUserInDays()) < new Date().getTime()
                        && !(userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase("admin"))) {
                    log.info("inside logindenied");
                    responce.put("status", "passReset");
               	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();

                }
            }
            
             if (passwordChangeDate(userMasterDetail.get(0).getLastLoginTime(), userMasterDetail.get(0).geteFmFmClientBranchPO().getInactiveAdminAccountAfterNumOfDays()) < new Date().getTime() && !(userMasterDetail.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO"))) {
                 log.info("inside inactive");
                 responce.put("status", "inactive");
            	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            log.info("Logged In"+userMasterDetail.get(0).isLoggedIn());
            if (userMasterDetail.get(0).isLoggedIn()) {
            	String rolevalue="";
                responce.put("employeeName",new String(Base64.getDecoder().decode(userMasterDetail.get(0).getFirstName()), "utf-8"));                
                responce.put("userId", userMasterDetail.get(0).getUserId());
                
                if(userDetail.isEmpty()){
                    List<EFmFmClientUserRolePO> userRole = userMasterBO
                            .getUserRoleByUserName(userMasterDetail.get(0).getUserName());           
                    responce.put("userRole",userRole.get(0).getEfmFmRoleMaster().getRole());
                    rolevalue=userRole.get(0).getEfmFmRoleMaster().getRole();
                }
                else{
                    responce.put("userRole",userDetail.get(0).getEfmFmRoleMaster().getRole());	
                    rolevalue=userDetail.get(0).getEfmFmRoleMaster().getRole();
                }  
                try {             	
                	 responce.put("multiplefacilityFlg", userMasterDetail.get(0).geteFmFmClientBranchPO().isMultiFacility());
                	 if(userMasterDetail.get(0).geteFmFmClientBranchPO().isMultiFacility()){
	                	 List<Map<String, Object>> facilityDetails= getfacilityDetailsByUserId(userMasterDetail.get(0).getUserId());
	                     if(!(facilityDetails.isEmpty())){
	                     	responce.put("multiplefacilties",facilityDetails);	
	                     }else{
	                     	responce.put("multiplefacilties","MULTIFACILITYNOTFOUND");
	                     }
                	 }
				} catch (Exception e) {
					responce.put("multiplefacilties","MULTIFACILITYNOTFOUND");					
				}        
                
                responce.put("locationStatus", userMasterDetail.get(0).getLocationStatus());
                responce.put("panicStatus", userMasterDetail.get(0).geteFmFmClientBranchPO().getPanicAlertNeeded());
                responce.put("branchId", userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchId());
                responce.put("mobilePageCount", userMasterDetail.get(0).geteFmFmClientBranchPO().getMobilePageCount());
                responce.put("branchCode", userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchCode());
                responce.put("adhocTimePicker",
                        userMasterDetail.get(0).geteFmFmClientBranchPO().getAdhocTimePickerForEmployee());
                responce.put("dropPriorTimePeriod",
                        userMasterDetail.get(0).geteFmFmClientBranchPO().getDropPriorTimePeriod());
                responce.put("pickupPriorTimePeriod",
                        userMasterDetail.get(0).geteFmFmClientBranchPO().getPickupPriorTimePeriod());
                responce.put("driverName", userMasterDetail.get(0).geteFmFmClientBranchPO().getEmpDeviceDriverName());
                responce.put("driverMobile",
                        userMasterDetail.get(0).geteFmFmClientBranchPO().getEmpDeviceDriverMobileNumber());
                responce.put("driverProfilePic",
                        userMasterDetail.get(0).geteFmFmClientBranchPO().getDriverDeviceDriverProfilePicture());
                responce.put("employeeId", userMasterDetail.get(0).getEmployeeId());
                responce.put("cutOffTimeFlg", userMasterDetail.get(0).geteFmFmClientBranchPO().getCutOffTime());            
                responce.put("locationVisible",userMasterDetail.get(0).geteFmFmClientBranchPO().getLocationVisible());    		
        		responce.put("destinationPointLimit",userMasterDetail.get(0).geteFmFmClientBranchPO().getDestinationPointLimit());        		
        		    		 		
                responce.put("callToDriver",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeCallToDriver());    		
                responce.put("callToTransportDesk",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeCallToTransport());   
              //responce.put("requestWithProject",userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject()); 
                responce.put("employeeChecKInVia",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeChecKInVia());  
                responce.put("reportAbug",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeAppReportBug()); 

                try{
                responce.put("empGeocode",userMasterDetail.get(0).getLatitudeLongitude());  
                }catch(Exception e){
                    responce.put("empGeocode","0.0");  
                }
                
              //responce.put("authenticationToken",userMasterDetail.get(0).getAuthorizationToken());   
                

               /* responce.put("requestWithProject",userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject());  
                responce.put("managerReqCreateProcess",userMasterDetail.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess()); */           
                
                if(rolevalue.equalsIgnoreCase("webuser")){                	
                	 if(userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("Yes")){
                    	 responce.put("requestWithProject","Yes");
                    	 responce.put("managerReqCreateProcess","NO");                    	
                    }else{
                    	responce.put("requestWithProject","NO");
                   	    responce.put("managerReqCreateProcess","NO"); 
                    }
                }else if(rolevalue.equalsIgnoreCase("manager") || rolevalue.equalsIgnoreCase("supervisor") ){  
                	
                	if(userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("YES") 
                   			&& userMasterDetail.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess().equalsIgnoreCase("NO")){
	                   	 responce.put("requestWithProject","YES");
	                   	 responce.put("managerReqCreateProcess","NO");                    	
                  	}else if(userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("NO") 
                   			&& userMasterDetail.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess().equalsIgnoreCase("NO")){
	                   	 responce.put("requestWithProject","NO");
	                   	 responce.put("managerReqCreateProcess","NO");                    	
                  	}else{
                  		 responce.put("requestWithProject","YES");
	                   	 responce.put("managerReqCreateProcess","YES"); 
                  	}
                	
                	
                }else if(rolevalue.equalsIgnoreCase("admin") || rolevalue.equalsIgnoreCase("superadmin") ){
                	
               	 if(userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("YES") 
                			|| userMasterDetail.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess().equalsIgnoreCase("YES")){
                    	 responce.put("requestWithProject","YES");
                    	 responce.put("managerReqCreateProcess","YES");                    	
               	}
              }
                
          
                responce.put("reportAbug",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeAppReportBug());    		
        		
                //Tk8= this meands NO
                if (userMasterDetail.get(0).getPanicNumber().equalsIgnoreCase("Tk8=")) {
                    responce.put("empPanicNumber",userMasterDetail.get(0).geteFmFmClientBranchPO().getTransportContactNumberForMsg()); 
                } else {
                    responce.put("empPanicNumber", "+" + userMasterDetail.get(0).geteFmFmClientBranchPO().getTransportContactNumberForMsg()); 
                }
                
                //Tk8= means check for NO
                    responce.put("empSecondaryPanicNumber", userMasterDetail.get(0).geteFmFmClientBranchPO().getTransportContactNumberForMsg());                
                responce.put("emergencyNum",
                        "+" + userMasterDetail.get(0).geteFmFmClientBranchPO().getEmergencyContactNumber());
                responce.put("emailId", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getEmailId()), "utf-8"));
                responce.put("designation", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getEmployeeDesignation()), "utf-8"));
                responce.put("mobileNumber", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getMobileNumber()), "utf-8"));
                try {
                    String profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null,
                            "profilePic", null);
                    responce.put("profilePic", profilePicPath + userMasterDetail.get(0).getEmployeeProfilePic()
                            .substring(userMasterDetail.get(0).getEmployeeProfilePic().indexOf("upload") - 1));
                } catch (Exception e) {
                    log.info("splace error" + e);
                    String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
                            "profilePic", null);
                    responce.put("profilePic", defaultProfilePic);
                }
                userMasterDetail.get(0).setLastLoginTime(new Date());
                employeeDetailBO.update(userMasterDetail.get(0));
                // module data .....we have to store same branch name in db as well
                   
                List<EFmFmEmployeeModuleMappingWithBranchPO> empModuleDetails=userMasterBO.getAllEmployeeModuleAccessFromBranchId(userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchId());
               if(!(empModuleDetails.isEmpty())){
                for(EFmFmEmployeeModuleMappingWithBranchPO module:empModuleDetails){
                    Map<String, Object> requests = new HashMap<String, Object>();
                    requests.put("moduleName", module.geteFmFmEmployeeModuleMasterPO().getEmpModuleName());
                    requests.put("isActive", module.isEmpModuleStatus());
                    userAccessData.add(requests);
                }}
                
                
               responce.put("moduleData", userAccessData);
                responce.put("status", "loggedIn");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            responce.put("status", "notLoggedIn");
       	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();

        }
        responce.put("status", "notRegister");
   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /*
     * Employee device registration....Service
     */

    @POST
    @Path("/employeeregister")
    public Response employeeRegistration(final EFmFmUserMasterPO eFmFmUserMasterPO) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
      	 log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId()); 		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
// 		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
//		
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
//                responce.put("token", jwtToken);
//			}
//		
//		}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
      
        log.info("device token" + eFmFmUserMasterPO.getDeviceToken());
        log.info("device id" + eFmFmUserMasterPO.getDeviceId());
        log.info("device Type" + eFmFmUserMasterPO.getDeviceType());
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        for (int i = 0; i < 4; i++) {
            result += numbers.get(i).toString();
        }
        List<EFmFmClientBranchPO> clientMasterDetail = employeeDetailBO
                .doesClientCodeExist(eFmFmUserMasterPO.getBranchCode());
        eFmFmUserMasterPO.setCombinedFacility(String.valueOf(clientMasterDetail.get(0).getBranchId()));
       
        if (!(clientMasterDetail.isEmpty())) {
            final List<EFmFmUserMasterPO> userMasterDetail = employeeDetailBO.getEmpDetailsFromMobileNumberAndBranchId
            		(Base64.getEncoder().encodeToString(eFmFmUserMasterPO.getMobileNumber().getBytes("utf-8")),
            				String.valueOf(clientMasterDetail.get(0).getBranchId()));          
            if (!(userMasterDetail.isEmpty())) {
                if (userMasterDetail.get(0).getStatus().equalsIgnoreCase("N")) {
                    responce.put("status", "disable");
               	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }
                try {
                    if (userMasterDetail.get(0).getDeviceId().equalsIgnoreCase(eFmFmUserMasterPO.getDeviceId())) {
                        responce.put("status", "already");
                   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                    }
                } catch (Exception e) {
                    log.info("register exception" + e);
                    responce.put("status", "failed");
               	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }
                try {
                    if (!(userMasterDetail.get(0).getDeviceId().equalsIgnoreCase("NO"))) {
                        userMasterDetail.get(0).setLoggedIn(false);
                        userMasterDetail.get(0).setDeviceToken(eFmFmUserMasterPO.getDeviceToken());
                        userMasterDetail.get(0).setDeviceType(eFmFmUserMasterPO.getDeviceType());
                        userMasterDetail.get(0).setDeviceId(eFmFmUserMasterPO.getDeviceId());
                        employeeDetailBO.update(userMasterDetail.get(0));
                        responce.put("status", "already");
                   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                    }
                } catch (Exception e) {
                    log.info("Exception Inside the after registration changing device");
                    responce.put("status", "failed");
               	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }
                userMasterDetail.get(0).setLoggedIn(false);
                userMasterDetail.get(0).setDeviceToken(eFmFmUserMasterPO.getDeviceToken());
                userMasterDetail.get(0).setDeviceType(eFmFmUserMasterPO.getDeviceType());
                userMasterDetail.get(0).setDeviceId(eFmFmUserMasterPO.getDeviceId());
                if(!(userMasterDetail.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO"))){
                try {
                    Thread thread1 = new Thread(new Runnable() {
                        @Override
						public void run() {

                            MessagingService messaging = new MessagingService();
                            String text = "Dear user,Please find login credentials for eFmFm application.\nUsername: "
                                    + new String(Base64.getDecoder().decode(userMasterDetail.get(0).getMobileNumber())) + "\nPassword: " + result
                                    + "\nFor feedback write to us @"
                                    + userMasterDetail.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();
                            try {
                                messaging.cabHasLeftMessageForSch(new String(Base64.getDecoder().decode(userMasterDetail.get(0).getMobileNumber()), "utf-8"), text,
                                        "no");
                                log.info("Register Message Sent" + new String(Base64.getDecoder().decode(userMasterDetail.get(0).getMobileNumber())));

                            } catch (Exception e) {
                                log.info("Register Message Sent Exception" + e);

                            }
                        }
                    });
                    thread1.start();
                } catch (Exception e) {
                    log.info("Register Password mail" + e);
                }
                }             
                PasswordEncryption passwordEncryption = new PasswordEncryption();
                userMasterDetail.get(0).setPassword(passwordEncryption.PasswordEncoderGenerator(result));
                userMasterDetail.get(0).setPasswordChangeDate(new Date());
                userMasterDetail.get(0).setLastLoginTime(new Date());
                userMasterDetail.get(0).setTempPassWordChange(true);
                employeeDetailBO.update(userMasterDetail.get(0));
                responce.put("status", "success");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            } else {
                responce.put("status", "failed");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
        }
        responce.put("status", "codeerror");
   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /*
     * Employee Login Service
     * 
     */

    @POST
    @Path("/login")
    public Response employeeLogin(EFmFmUserMasterPO eFmFmUserMasterPO) throws IOException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
      	 log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        
        List<EFmFmUserMasterPO> userMasterDetail = employeeDetailBO
                .getParticularEmpDetailsFromMobileNumber(Base64.getEncoder().encodeToString(eFmFmUserMasterPO.getMobileNumber().getBytes("utf-8")));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        log.info("userDetail" + userMasterDetail.size());
        String rolevalue="";
        if (!(userMasterDetail.isEmpty())) {
            if (!(encoder.matches(eFmFmUserMasterPO.getPassword().trim(), userMasterDetail.get(0).getPassword().trim())) && !(userMasterDetail.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO"))) {
                if (!(userMasterDetail.isEmpty()) && (userMasterDetail.get(0).getWrongPassAttempt() == userMasterDetail.get(0).geteFmFmClientBranchPO()
                        .getNumberOfAttemptsWrongPass()) && (getDisableTime(24, 0, userMasterDetail.get(0).getWrongPassAttemptDate()) > new Date().getTime())) { 
                    responce.put("status", "passDisable");
               	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }   
                else if (!(userMasterDetail.isEmpty()) && (userMasterDetail.get(0).getWrongPassAttempt() == userMasterDetail.get(0).geteFmFmClientBranchPO()
                        .getNumberOfAttemptsWrongPass()) && (getDisableTime(24, 0, userMasterDetail.get(0).getWrongPassAttemptDate()) < new Date().getTime())) {
                    userMasterDetail.get(0).setWrongPassAttempt(1);
                    employeeDetailBO.update(userMasterDetail.get(0));
                    responce.put("status", "fail");
               	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();  
                }
                else if (!(userMasterDetail.isEmpty()) && (userMasterDetail.get(0).getWrongPassAttempt()== (userMasterDetail.get(0).geteFmFmClientBranchPO()
                        .getNumberOfAttemptsWrongPass()-1))) {
                    userMasterDetail.get(0).setWrongPassAttempt(userMasterDetail.get(0).getWrongPassAttempt()+1);
                    userMasterDetail.get(0).setWrongPassAttemptDate(new Date());
                    employeeDetailBO.update(userMasterDetail.get(0));
                    responce.put("status", "passDisable");
               	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }
                else if (!(userMasterDetail.isEmpty()) && (userMasterDetail.get(0).getWrongPassAttempt()== (userMasterDetail.get(0).geteFmFmClientBranchPO()
                        .getNumberOfAttemptsWrongPass()-2))) {  
                    userMasterDetail.get(0).setWrongPassAttempt(userMasterDetail.get(0).getWrongPassAttempt()+1);
                    employeeDetailBO.update(userMasterDetail.get(0));
                    responce.put("status", "lastAttempt");
               	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }
                else{              
                    userMasterDetail.get(0).setWrongPassAttempt(userMasterDetail.get(0).getWrongPassAttempt()+1);
                    employeeDetailBO.update(userMasterDetail.get(0));
                    responce.put("status", "fail");
               	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                    }
            }           
            List<EFmFmClientUserRolePO> userDetail = userMasterBO
                    .getAdminUserRoleByUserName(userMasterDetail.get(0).getUserName());                     
            if (!(userMasterDetail.isEmpty()) && (userMasterDetail.get(0).getWrongPassAttempt() == userMasterDetail.get(0).geteFmFmClientBranchPO()
                    .getNumberOfAttemptsWrongPass()) && (getDisableTime(24, 0, userMasterDetail.get(0).getWrongPassAttemptDate()) > new Date().getTime())) { 
                responce.put("status", "passDisable");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }   
            else if (!(userMasterDetail.isEmpty()) && (userMasterDetail.get(0).isTempPassWordChange())) { 
                responce.put("status", "tempPassChange");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }   
            else if ((!userDetail.isEmpty()) 
                    && (passwordChangeDate(userDetail.get(0).getEfmFmUserMaster().getPasswordChangeDate(),
                            userDetail.get(0).geteFmFmClientBranchPO().getPasswordResetPeriodForAdminInDays()) < new Date()
                                    .getTime()) && userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase("admin")) {
                responce.put("status", "passReset");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            else if ((!userDetail.isEmpty()) 
                    && (passwordChangeDate(userDetail.get(0).getEfmFmUserMaster().getPasswordChangeDate(),
                            userDetail.get(0).geteFmFmClientBranchPO().getPasswordResetPeriodForUserInDays()) < new Date()
                                    .getTime()) && !(userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase("admin"))) {
                responce.put("status", "passReset");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            else if (passwordChangeDate(userMasterDetail.get(0).getLastLoginTime(),userMasterDetail.get(0).geteFmFmClientBranchPO().getInactiveAdminAccountAfterNumOfDays()) < new Date().getTime() && userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase("admin")){
                responce.put("status", "inactive");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
  

            if (userMasterDetail.get(0).getStatus().equalsIgnoreCase("N")) {
                responce.put("status", "disable");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            List<Map<String, Object>> userAccessData = new ArrayList<Map<String, Object>>();
            responce.put("employeeName", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getFirstName())));
            responce.put("userId", userMasterDetail.get(0).getUserId());
            if(userDetail.isEmpty()){
                List<EFmFmClientUserRolePO> userRole = userMasterBO
                        .getUserRoleByUserName(userMasterDetail.get(0).getUserName());           
                responce.put("userRole",userRole.get(0).getEfmFmRoleMaster().getRole());
                rolevalue=userRole.get(0).getEfmFmRoleMaster().getRole();
            }
            else{
                responce.put("userRole",userDetail.get(0).getEfmFmRoleMaster().getRole());	
                rolevalue=userDetail.get(0).getEfmFmRoleMaster().getRole();	
            }
            
            try {             	
           	 responce.put("multiplefacilityFlg", userMasterDetail.get(0).geteFmFmClientBranchPO().isMultiFacility());
           	 if(userMasterDetail.get(0).geteFmFmClientBranchPO().isMultiFacility()){
               	 List<Map<String, Object>> facilityDetails= getfacilityDetailsByUserId(userMasterDetail.get(0).getUserId());
                    if(!(facilityDetails.isEmpty())){
                    	responce.put("multiplefacilties",facilityDetails);	
                    }else{
                    	responce.put("multiplefacilties","MULTIFACILITYNOTFOUND");
                    }
           	 }
			} catch (Exception e) {
				responce.put("multiplefacilties","MULTIFACILITYNOTFOUND");					
			}        
            responce.put("locationStatus", userMasterDetail.get(0).getLocationStatus());
            responce.put("panicStatus", userMasterDetail.get(0).geteFmFmClientBranchPO().getPanicAlertNeeded());
            responce.put("mobilePageCount", userMasterDetail.get(0).geteFmFmClientBranchPO().getMobilePageCount());            
            responce.put("branchId", userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchId());
            responce.put("branchCode", userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchCode());
            responce.put("adhocTimePicker",
                    userMasterDetail.get(0).geteFmFmClientBranchPO().getAdhocTimePickerForEmployee());
            responce.put("dropPriorTimePeriod",
                    userMasterDetail.get(0).geteFmFmClientBranchPO().getDropPriorTimePeriod());
            responce.put("pickupPriorTimePeriod",
                    userMasterDetail.get(0).geteFmFmClientBranchPO().getPickupPriorTimePeriod());
            responce.put("driverName", userMasterDetail.get(0).geteFmFmClientBranchPO().getEmpDeviceDriverName());
            responce.put("driverMobile",
                    userMasterDetail.get(0).geteFmFmClientBranchPO().getEmpDeviceDriverMobileNumber());
            responce.put("driverProfilePic",
                    userMasterDetail.get(0).geteFmFmClientBranchPO().getDriverDeviceDriverProfilePicture());
            responce.put("employeeId", userMasterDetail.get(0).getEmployeeId());
            responce.put("tempPassChange", userMasterDetail.get(0).isTempPassWordChange());
            responce.put("cutOffTimeFlg", userMasterDetail.get(0).geteFmFmClientBranchPO().getCutOffTime());   
            
            responce.put("locationVisible",userMasterDetail.get(0).geteFmFmClientBranchPO().getLocationVisible());    		
    		responce.put("destinationPointLimit",userMasterDetail.get(0).geteFmFmClientBranchPO().getDestinationPointLimit());       
            responce.put("callToDriver",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeCallToDriver());    		
            responce.put("callToTransportDesk",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeCallToTransport());
            //responce.put("requestWithProject",userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject()); 
            responce.put("employeeChecKInVia",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeChecKInVia());    		
            responce.put("token",userMasterDetail.get(0).getMobAuthorizationToken());   
            //responce.put("managerReqCreateProcess",userMasterDetail.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess()); 
            responce.put("reportAbug",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeAppReportBug()); 
            try{
                responce.put("empGeocode",userMasterDetail.get(0).getLatitudeLongitude());  
                }catch(Exception e){
                    responce.put("empGeocode","0.0");  
                }
            
			if(rolevalue.equalsIgnoreCase("webuser")){                	
           	 if(userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("Yes")){
               	 responce.put("requestWithProject","Yes");
               	 responce.put("managerReqCreateProcess","NO");                    	
               }else{
               	responce.put("requestWithProject","NO");
              	    responce.put("managerReqCreateProcess","NO"); 
               }
           }else if(rolevalue.equalsIgnoreCase("manager") || rolevalue.equalsIgnoreCase("supervisor") ){  
           	
           	if(userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("YES") 
              			&& userMasterDetail.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess().equalsIgnoreCase("NO")){
                  	 responce.put("requestWithProject","YES");
                  	 responce.put("managerReqCreateProcess","NO");                    	
             	}else if(userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("NO") 
              			&& userMasterDetail.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess().equalsIgnoreCase("NO")){
                  	 responce.put("requestWithProject","NO");
                  	 responce.put("managerReqCreateProcess","NO");                    	
             	}else{
             		 responce.put("requestWithProject","YES");
                  	 responce.put("managerReqCreateProcess","YES"); 
             	}
           	
           	
             }else if(rolevalue.equalsIgnoreCase("admin") || rolevalue.equalsIgnoreCase("superadmin") ){
            	 if(userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("YES") 
               			|| userMasterDetail.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess().equalsIgnoreCase("YES")){
                   	 responce.put("requestWithProject","YES");
                   	 responce.put("managerReqCreateProcess","YES");                    	
              	}
             }
    		
            if (userMasterDetail.get(0).getPanicNumber().equalsIgnoreCase("Tk8=")) {
                responce.put("empPanicNumber",userMasterDetail.get(0).geteFmFmClientBranchPO().getTransportContactNumberForMsg()); 
            } else {
                responce.put("empPanicNumber", "+" + userMasterDetail.get(0).geteFmFmClientBranchPO().getTransportContactNumberForMsg()); 
            }
            
            //Tk8= means check for NO
            if (userMasterDetail.get(0).getSecondaryPanicNumber().equalsIgnoreCase("Tk8=")) {
                responce.put("empSecondaryPanicNumber", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getSecondaryPanicNumber())));
            } else {
                responce.put("empSecondaryPanicNumber", "+" + new String(Base64.getDecoder().decode(userMasterDetail.get(0).getSecondaryPanicNumber())));
            }
            
            responce.put("emergencyNum",
                    "+" + userMasterDetail.get(0).geteFmFmClientBranchPO().getEmergencyContactNumber());
            responce.put("emailId", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getEmailId()), "utf-8"));
            responce.put("designation", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getEmployeeDesignation())));
            responce.put("mobileNumber", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getMobileNumber())));
            try {
                String profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
                        null);
                responce.put("profilePic", profilePicPath + userMasterDetail.get(0).getEmployeeProfilePic()
                        .substring(userMasterDetail.get(0).getEmployeeProfilePic().indexOf("upload") - 1));
            } catch (Exception e) {
                String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
                        "profilePic", null);
                responce.put("profilePic", defaultProfilePic);
            }
            responce.put("status", "success");
            userMasterDetail.get(0).setLastLoginTime(new Date());
            userMasterDetail.get(0).setWrongPassAttempt(0);
            userMasterDetail.get(0).setoTPGenratingCount(0);
            userMasterDetail.get(0).setoTPGenratingDate(new Date());
            userMasterDetail.get(0).setLoggedIn(true);
 //           userMasterDetail.get(0).setTempPassWordChange(true);
            employeeDetailBO.update(userMasterDetail.get(0));
            
            
            List<EFmFmEmployeeModuleMappingWithBranchPO> empModuleDetails=userMasterBO.getAllEmployeeModuleAccessFromBranchId(userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchId());
           if(!(empModuleDetails.isEmpty())){
            for(EFmFmEmployeeModuleMappingWithBranchPO module:empModuleDetails){
                Map<String, Object> requests = new HashMap<String, Object>();
                requests.put("moduleName", module.geteFmFmEmployeeModuleMasterPO().getEmpModuleName());
                requests.put("isActive", module.isEmpModuleStatus());
                userAccessData.add(requests);
            }}

            
            
            
            
            responce.put("moduleData", userAccessData);
       	    log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        } else {
            responce.put("status", "fail");
        }
   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /*
     * Employee Logout Service
     */

    @POST
    @Path("/logout")
    public Response employeeLogout(EFmFmUserMasterPO eFmFmUserMasterPO) {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
      	 log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId()); 		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId())) && !(httpRequest.getHeader("authenticationToken").equalsIgnoreCase(null))){
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
       
        List<EFmFmUserMasterPO> userMasterDetail = employeeDetailBO.getParticularEmpDetailsFromUserId(
                eFmFmUserMasterPO.getUserId(), eFmFmUserMasterPO.geteFmFmClientBranchPO().getBranchId());
        userMasterDetail.get(0).setLoggedIn(false);
        employeeDetailBO.update(userMasterDetail.get(0));
        responce.put("status", "success");
   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /*
     * Employee feed back for driver
     */

    @POST
    @Path("/feedback")
    public Response employeeFeedbackToDriver(EFmFmDriverFeedbackPO driverFeedbackPO) throws ParseException {
        IVendorDetailsBO vendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
   	    log.info("serviceStart -UserId :" + driverFeedbackPO.getUserId());		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),driverFeedbackPO.getEfmFmUserMaster().getUserId()))){
 				responce.put("status", "invalidRequest");
 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
 			}
 		
 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(driverFeedbackPO.getEfmFmUserMaster().getUserId());
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
 		 
 	    
        EFmFmEmployeeTravelRequestPO employeeTravelRequestPO = new EFmFmEmployeeTravelRequestPO();
        employeeTravelRequestPO.setRequestDate(new Date());
        EFmFmUserMasterPO efmFmUserMaster = new EFmFmUserMasterPO();
        EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
        eFmFmClientBranchPO.setBranchId(driverFeedbackPO.getBranchId());
        efmFmUserMaster.seteFmFmClientBranchPO(eFmFmClientBranchPO);
        employeeTravelRequestPO.setEfmFmUserMaster(efmFmUserMaster);
        efmFmUserMaster.setUserId(driverFeedbackPO.getEfmFmUserMaster().getUserId());
       
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date tripDate = formatter.parse(driverFeedbackPO.getTime());
        driverFeedbackPO.setTripDate(tripDate);
        driverFeedbackPO.setEfmFmUserMaster(efmFmUserMaster);
        vendorDetailsBO.save(driverFeedbackPO);
        responce.put("status", "success");
   	 	log.info("serviceEnd -UserId :" + driverFeedbackPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    /*
     * temp code Forgot password service call form mobile .
     * 
     */

    @POST
    @Path("/forgotpassword")
    public Response emailCheck(EFmFmClientBranchPO clientDetails) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
      	 log.info("serviceStart -UserId :" + clientDetails.getUserId());
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));

        
        
//        try{
// 			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),clientDetails.getUserId()))){
// 				responce.put("status", "invalidRequest");
// 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
// 			}
// 		
// 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(clientDetails.getUserId());
// 			if (!(userDetail.isEmpty())) {
// 				String jwtToken = "";
// 				try {
// 					JwtTokenGenerator token = new JwtTokenGenerator();
// 					jwtToken = token.generateToken();
// 					userDetail.get(0).setAuthorizationToken(jwtToken);
// 					userDetail.get(0).setTokenGenerationTime(new Date());
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
 		 
 		 
        
        
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        for (int i = 0; i < 4; i++) {
            result += numbers.get(i).toString();
        }
        final List<EFmFmUserMasterPO> empDetails = employeeDetailBO.getParticularEmpDetailsFromMobileNumberAndBranhId(
                Base64.getEncoder().encodeToString(clientDetails.getMobileNumber().getBytes("utf-8")), clientDetails.getBranchId());
        if (!(empDetails.isEmpty())) {
            if ((empDetails.get(0).getWrongPassAttempt() == empDetails.get(0).geteFmFmClientBranchPO()
                    .getNumberOfAttemptsWrongPass())
                    && (getDisableTime(24, 0, empDetails.get(0).getWrongPassAttemptDate()) > new Date()
                            .getTime())) {
                responce.put("status", "accountDisableTwentyFourHours");
           	 log.info("serviceEnd -UserId :" + clientDetails.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            if (empDetails.get(0).getStatus().equalsIgnoreCase("N")) {
                responce.put("status", "disable");
           	 log.info("serviceEnd -UserId :" + clientDetails.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            
			// OTP Maximum Time Allow check
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
				Date userOTPGenerateDate = sdf.parse(sdf.format(empDetails.get(0).getoTPGenratingDate()));

				Calendar cal = Calendar.getInstance();
				cal.setTime(empDetails.get(0).geteFmFmClientBranchPO().getDissableTimeOTP());
				long millis = cal.getTimeInMillis();
				java.sql.Time shiftTimeValue = new java.sql.Time(millis);

				String CalenData = sd.format(Calendar.getInstance().getTime());
				CalenData = CalenData.concat(" ");
				CalenData = CalenData.concat(shiftTimeValue.toString());

				Date clientOTPGenerateDate = sdf.parse(sdf.format(sdf.parse(CalenData)));

				Calendar c1 = Calendar.getInstance();
				c1.setTime(userOTPGenerateDate);
				Calendar c2 = Calendar.getInstance();
				c2.setTime(clientOTPGenerateDate);
				Calendar cTotal = (Calendar) c1.clone();

				cTotal.add(Calendar.HOUR, c2.get(Calendar.HOUR));

				cTotal.add(Calendar.MINUTE, c2.get(Calendar.MINUTE));

				Calendar cUser = Calendar.getInstance();
				cUser.setTime(userOTPGenerateDate);
				if (empDetails.get(0).geteFmFmClientBranchPO()
						.getMaxTimeOTP() <= (empDetails.get(0).getoTPGenratingCount())
						&& Calendar.getInstance().getTime().after(cTotal.getTime())) {
					empDetails.get(0).setoTPGenratingCount(0);
					empDetails.get(0).setoTPGenratingDate(new Date());
				}

				if (Calendar.getInstance().getTime().before(cTotal.getTime()) && empDetails.get(0)
						.geteFmFmClientBranchPO().getMaxTimeOTP() <=empDetails.get(0).getoTPGenratingCount()) {
					log.info("Maximum Time Came");
					responce.put("otoEnableTime", shiftTimeFormater.format(empDetails.get(0).geteFmFmClientBranchPO().getDissableTimeOTP()));
					responce.put("status", "disableOTP");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}

			} catch (Exception e) {
				log.info("Error"+e);
			}
            
            
            Thread thread1 = new Thread(new Runnable() {
                @Override
				public void run() {

                    MessagingService messaging = new MessagingService();
                    String text = "Please find your temporary code for eFmFm application =" + result
                            + "\nFor feedback write to us @"
                            + empDetails.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();
                    try {
                        messaging.cabHasLeftMessageForSch(new String(Base64.getDecoder().decode(empDetails.get(0).getMobileNumber()), "utf-8"), text, "no");
                        log.info("Register Message Sent" + new String(Base64.getDecoder().decode(empDetails.get(0).getMobileNumber()), "utf-8"));

                    } catch (Exception e) {
                        log.info("Register Message Sent Exception" + e);

                    }
                }
            });
            thread1.start();
            empDetails.get(0).setTempCode(result);
            empDetails.get(0).setoTPGenratingCount(empDetails.get(0).getoTPGenratingCount()+1);
            empDetails.get(0).setoTPGenratingDate(new Date());
            empDetails.get(0).setLoggedIn(false);
            employeeDetailBO.update(empDetails.get(0));
            responce.put("status", "success");
       	 log.info("serviceEnd -UserId :" + clientDetails.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        responce.put("status", "invalid");
   	    log.info("serviceEnd -UserId :" + clientDetails.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();

    }

    /*
     * Change Forgot password service call form mobile.
     */

    @POST
    @Path("/changeForgotPassword")
    public Response changeForgotPassword(EFmFmUserMasterPO eFmFmUserMasterPO) throws ParseException, UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
   	    log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        
       
//        try{
// 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){
// 				responce.put("status", "invalidRequest");
// 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
// 			}
// 		
// 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMasterPO.getUserId());
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
 		 
        
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        if(!(eFmFmUserMasterPO.getNewPassword().matches(ContextLoader.getContext().getMessage("strong.password", null, "password", null)))){
            responce.put("status", "wrongPattern");
       	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();  
        }
        final List<EFmFmUserMasterPO> employeeDetails = employeeDetailBO
                .getParticularEmpDetailsFromEmployeeIdAndbranchIdAndToken(Base64.getEncoder().encodeToString(eFmFmUserMasterPO.getMobileNumber().getBytes("utf-8")),
                        eFmFmUserMasterPO.getTempCode(), eFmFmUserMasterPO.getBranchId());
        if (!(employeeDetails.isEmpty())) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean passexist = false;
            List<EFmFmUserPasswordPO> passDetails = employeeDetailBO.getUserPasswordDetailsFromUserIdAndBranchId(
                    employeeDetails.get(0).getUserId());
            if (!(passDetails.isEmpty())) {
                if (passDetails.size() > employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass()) {
                    for(int i=0;i<((passDetails.size())-employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass());i++){
                        employeeDetailBO.deleteLastPasswordForParticularEmployeCrossingDefineLimit(
                                passDetails.get(i).getPasswordId());
                    }
                }
                log.info("passDetails.size()" + passDetails.size());
                for (EFmFmUserPasswordPO pass : passDetails) {
                    if (encoder.matches(eFmFmUserMasterPO.getNewPassword(), pass.getPassword())) {
                        passexist = true;
                        break;
                    }

                }
            }

            if (passexist) {
                if (passDetails.size() <= employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass())
                    responce.put("numberOfPasswords", passDetails.size());
                else {
                    responce.put("numberOfPasswords", employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass());
                }
                responce.put("status", "oldpass");
                responce.put("lastChangeDateTime",
                        dateFormatter.format(passDetails.get(passDetails.size() - 1).getCreationTime()));
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }

            if (employeeDetails.get(0).getStatus().equalsIgnoreCase("N")) {
                responce.put("status", "disable");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            employeeDetails.get(0)
                    .setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmUserMasterPO.getNewPassword()));
            employeeDetails.get(0).setPasswordChangeDate(new Date());
            employeeDetails.get(0).setLastLoginTime(new Date());

            employeeDetails.get(0).setWrongPassAttempt(0);
            employeeDetails.get(0).setTempCode(null);
            employeeDetails.get(0).setTempPassWordChange(false);

            employeeDetailBO.update(employeeDetails.get(0));
            EFmFmUserPasswordPO userPassword = new EFmFmUserPasswordPO();
            EFmFmClientBranchPO clientDetail = new EFmFmClientBranchPO();
            clientDetail.setBranchId(eFmFmUserMasterPO.getBranchId());
            // userPassword.setCreatedBy(createdBy);
            userPassword.setCreationTime(new Date());
            userPassword.setEfmFmUserMaster(employeeDetails.get(0));
            userPassword.setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmUserMasterPO.getNewPassword()));
            userPassword.seteFmFmClientBranchPO(clientDetail);
            employeeDetailBO.save(userPassword);

            if (!(passDetails.isEmpty())) {
                if (passDetails.size() == employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass()) {
                    employeeDetailBO.deleteLastPasswordForParticularEmployeCrossingDefineLimit(
                            passDetails.get(0).getPasswordId());
                }
            }
            responce.put("status", "success");
       	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        responce.put("status", "fail");
   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /*
     * Change Forgot password service call form web console.
     */
    @POST
    @Path("/changepassword")
    public Response changePassword(EFmFmUserMasterPO eFmFmUserMasterPO) throws ParseException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
      	 log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId()); 		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));

        
        
        try{
 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){
 				responce.put("status", "invalidRequest");
 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
 			}
 		
 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMasterPO.getUserId());
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
 		 
        
        
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        List<EFmFmUserMasterPO> employeeDetails = employeeDetailBO.getParticularEmpDetailsFromUserId(
                eFmFmUserMasterPO.getUserId(), eFmFmUserMasterPO.geteFmFmClientBranchPO().getBranchId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(eFmFmUserMasterPO.getPassword(), employeeDetails.get(0).getPassword().trim())) {
            boolean passexist = false;
            List<EFmFmUserPasswordPO> passDetails = employeeDetailBO.getUserPasswordDetailsFromUserIdAndBranchId(
                    employeeDetails.get(0).getUserId());
            if (!(passDetails.isEmpty())) {
                if (passDetails.size() > employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass()) {
                    for(int i=0;i<((passDetails.size())-employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass());i++){
                        employeeDetailBO.deleteLastPasswordForParticularEmployeCrossingDefineLimit(
                                passDetails.get(i).getPasswordId());
                    }
                }
                log.info("passDetails.size()" + passDetails.size());
                for (EFmFmUserPasswordPO pass : passDetails) {
                    if (encoder.matches(eFmFmUserMasterPO.getNewPassword(), pass.getPassword())) {
                        passexist = true;
                        break;
                    }

                }
            }
            if (passexist) {
                if (passDetails.size() <= employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass())
                    responce.put("numberOfPasswords", passDetails.size());
                else {
                    responce.put("numberOfPasswords",employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass());
                }
                responce.put("status", "oldpass");
                responce.put("lastChangeDateTime",
                        dateFormatter.format(passDetails.get(passDetails.size() - 1).getCreationTime()));
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            employeeDetails.get(0)
                    .setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmUserMasterPO.getNewPassword()));
            employeeDetails.get(0).setPasswordChangeDate(new Date());
            employeeDetails.get(0).setLastLoginTime(new Date());
            employeeDetails.get(0).setTempPassWordChange(false);
            employeeDetailBO.update(employeeDetails.get(0));
            EFmFmUserPasswordPO userPassword = new EFmFmUserPasswordPO();
            EFmFmClientBranchPO clientDetail = new EFmFmClientBranchPO();
            clientDetail.setBranchId(eFmFmUserMasterPO.geteFmFmClientBranchPO().getBranchId());
            // userPassword.setCreatedBy(createdBy);
            userPassword.setCreationTime(new Date());
            userPassword.setEfmFmUserMaster(employeeDetails.get(0));
            userPassword.setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmUserMasterPO.getNewPassword()));
            userPassword.seteFmFmClientBranchPO(clientDetail);
            employeeDetailBO.save(userPassword);

            if (!(passDetails.isEmpty())) {
                if (passDetails.size() == employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass()) {
                    employeeDetailBO.deleteLastPasswordForParticularEmployeCrossingDefineLimit(
                            passDetails.get(0).getPasswordId());
                }
            }

            responce.put("status", "success");
       	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        responce.put("status", "fail");
   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /*
     * temp code Forgot password service call form web console .
     * 
     */
    @POST
    @Path("/webforgotpassword")
    public Response forgotPasswordFromWeb(EFmFmClientBranchPO clientDetails) throws UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        Map<String, Object> responce = new HashMap<String, Object>();      		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
       
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        for (int i = 0; i < 4; i++) {
            result += numbers.get(i).toString();
        }
        log.info("clientDetails.getBranchCode()" + clientDetails.getBranchCode());
        List<EFmFmClientBranchPO> clientData = employeeDetailBO.doesClientCodeExist(clientDetails.getBranchCode());
        if (!(clientData.isEmpty())) {
            final List<EFmFmUserMasterPO> empDetails = employeeDetailBO
                    .getParticularEmpDetailsFromMobileNumberAndBranhId(Base64.getEncoder().encodeToString(clientDetails.getMobileNumber().getBytes("utf-8")),
                            clientData.get(0).getBranchId());
            if (!(empDetails.isEmpty())) {
                if ((empDetails.get(0).getWrongPassAttempt() == empDetails.get(0).geteFmFmClientBranchPO()
                        .getNumberOfAttemptsWrongPass())
                        && (getDisableTime(24, 0, empDetails.get(0).getWrongPassAttemptDate()) > new Date()
                                .getTime())) {
                    responce.put("status", "disabletwenty");
               	 log.info("serviceEnd -UserId :" + clientDetails.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }

                if (empDetails.get(0).getStatus().equalsIgnoreCase("N")) {
                    responce.put("status", "disable");
               	 log.info("serviceEnd -UserId :" + clientDetails.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }
                
				// OTP Maximum Time Allow check
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
					Date userOTPGenerateDate = sdf.parse(sdf.format(empDetails.get(0).getoTPGenratingDate()));

					Calendar cal = Calendar.getInstance();
					cal.setTime(empDetails.get(0).geteFmFmClientBranchPO().getDissableTimeOTP());
					long millis = cal.getTimeInMillis();
					java.sql.Time shiftTimeValue = new java.sql.Time(millis);

					String CalenData = sd.format(Calendar.getInstance().getTime());
					CalenData = CalenData.concat(" ");
					CalenData = CalenData.concat(shiftTimeValue.toString());

					Date clientOTPGenerateDate = sdf.parse(sdf.format(sdf.parse(CalenData)));

					Calendar c1 = Calendar.getInstance();
					c1.setTime(userOTPGenerateDate);
					Calendar c2 = Calendar.getInstance();
					c2.setTime(clientOTPGenerateDate);
					Calendar cTotal = (Calendar) c1.clone();

					cTotal.add(Calendar.HOUR, c2.get(Calendar.HOUR));
					cTotal.add(Calendar.MINUTE, c2.get(Calendar.MINUTE));
					Calendar cUser = Calendar.getInstance();
					cUser.setTime(userOTPGenerateDate);

					if (empDetails.get(0).geteFmFmClientBranchPO()
							.getMaxTimeOTP() <= (empDetails.get(0).getoTPGenratingCount())
							&& Calendar.getInstance().getTime().after(cTotal.getTime())) {
						empDetails.get(0).setoTPGenratingCount(0);
						empDetails.get(0).setoTPGenratingDate(new Date());
					}

					if (Calendar.getInstance().getTime().before(cTotal.getTime()) && empDetails.get(0)
							.geteFmFmClientBranchPO().getMaxTimeOTP() <=empDetails.get(0).getoTPGenratingCount()) {
						log.info("Maximum Time Came");
						responce.put("otoEnableTime", shiftTimeFormater.format(empDetails.get(0).geteFmFmClientBranchPO().getDissableTimeOTP()));
						responce.put("status", "disableOTP");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}

				} catch (Exception e) {
					log.info("Error"+e);
				}
                               
                 Thread thread1 = new Thread(new Runnable() {
                 @Override
				public void run() {
                
                 MessagingService messaging = new MessagingService();
                 String text = "Dear user,Please find your temporary code for eFmFm application " + result
                 + "\nFor feedback write to us @"
                 +
                 empDetails.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();
                 try {
                 messaging.cabHasLeftMessageForSch(new String(Base64.getDecoder().decode(empDetails.get(0).getMobileNumber()), "utf-8"),
                 text, "no");
                 log.info("Register Message Sent" +
                         new String(Base64.getDecoder().decode(empDetails.get(0).getMobileNumber()), "utf-8"));
                
                 } catch (Exception e) {                
                 log.info("Register Message Sent Exception" + e);
                
                 }
                 }
                 });
                 thread1.start();
                empDetails.get(0).setTempCode(result);
                empDetails.get(0).setLoggedIn(false);
                empDetails.get(0).setoTPGenratingCount(empDetails.get(0).getoTPGenratingCount()+1);
                empDetails.get(0).setoTPGenratingDate(new Date());
                employeeDetailBO.update(empDetails.get(0));
                responce.put("status", "success");
           	 log.info("serviceEnd -UserId :" + clientDetails.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            responce.put("status", "invalidNum");
       	 log.info("serviceEnd -UserId :" + clientDetails.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        responce.put("status", "invalidCode");
   	 log.info("serviceEnd -UserId :" + clientDetails.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();

    }

    /*
     * Change Web Forgot password service call.
     */

    @POST
    @Path("/changeWebForgotPassword")
    public Response changeWebForgotPassword(EFmFmUserMasterPO eFmFmUserMasterPO) throws ParseException, UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//        log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){
//
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
//        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMasterPO.getUserId());
//     	   if (!(userDetail.isEmpty())) {
//     		String jwtToken = "";
//     		try {
//     		 JwtTokenGenerator token = new JwtTokenGenerator();
//     		 jwtToken = token.generateToken();
//     		 userDetail.get(0).setAuthorizationToken(jwtToken);
//     		 userDetail.get(0).setTokenGenerationTime(new Date());
//     		 userMasterBO.update(userDetail.get(0));
//     		} catch (Exception e) {
//     		 log.info("error" + e);
//     		}
//        }
//        	}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
   	
        
        List<EFmFmClientBranchPO> clientData = employeeDetailBO.doesClientCodeExist(eFmFmUserMasterPO.getBranchCode());
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        if(!(eFmFmUserMasterPO.getNewPassword().matches(ContextLoader.getContext().getMessage("strong.password", null, "password", null)))){
            responce.put("status", "wrongPattern");
       	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();  
        }
        if (!(clientData.isEmpty())) {
            final List<EFmFmUserMasterPO> employeeDetails = employeeDetailBO
                    .getParticularEmpDetailsFromEmployeeIdAndbranchIdAndToken(Base64.getEncoder().encodeToString(eFmFmUserMasterPO.getMobileNumber().getBytes("utf-8")),
                            eFmFmUserMasterPO.getTempCode(), clientData.get(0).getBranchId());
            if (!(employeeDetails.isEmpty())) {
                boolean passexist = false;
                List<EFmFmUserPasswordPO> passDetails = employeeDetailBO.getUserPasswordDetailsFromUserIdAndBranchId(
                        employeeDetails.get(0).getUserId());
                if (!(passDetails.isEmpty())) {
                    if (passDetails.size() > employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass()) {
                        for(int i=0;i<((passDetails.size())-employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass());i++){
                            employeeDetailBO.deleteLastPasswordForParticularEmployeCrossingDefineLimit(
                                    passDetails.get(i).getPasswordId());
                        }
                    }
                    log.info("passDetails.size()" + passDetails.size());
                    for (EFmFmUserPasswordPO pass : passDetails) {
                        if (encoder.matches(eFmFmUserMasterPO.getNewPassword(), pass.getPassword())) {
                            passexist = true;
                            break;
                        }

                    }
                }

                if (passexist) {
                    if (passDetails.size() <= employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass())
                        responce.put("numberOfPasswords", passDetails.size());
                    else {
                        responce.put("numberOfPasswords",employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass());
                    }
                    responce.put("status", "oldpass");
                    responce.put("lastChangeDateTime",
                            dateFormatter.format(passDetails.get(passDetails.size() - 1).getCreationTime()));
               	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }

                if (employeeDetails.get(0).getStatus().equalsIgnoreCase("N")) {
                    responce.put("status", "disable");
               	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }
                employeeDetails.get(0)
                        .setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmUserMasterPO.getNewPassword()));
                employeeDetails.get(0).setPasswordChangeDate(new Date());
                employeeDetails.get(0).setWrongPassAttempt(0);
                employeeDetails.get(0).setLastLoginTime(new Date());
                employeeDetails.get(0).setoTPGenratingCount(0);
                employeeDetails.get(0).setoTPGenratingDate(new Date());
                employeeDetails.get(0).setTempCode(null);
                employeeDetails.get(0).setTempPassWordChange(false);
                employeeDetailBO.update(employeeDetails.get(0));
                EFmFmUserPasswordPO userPassword = new EFmFmUserPasswordPO();
                EFmFmClientBranchPO clientDetail = new EFmFmClientBranchPO();
                clientDetail.setBranchId(clientData.get(0).getBranchId());
                // userPassword.setCreatedBy(createdBy);
                userPassword.setCreationTime(new Date());
                userPassword.setEfmFmUserMaster(employeeDetails.get(0));
                userPassword
                        .setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmUserMasterPO.getNewPassword()));
                userPassword.seteFmFmClientBranchPO(clientDetail);
                employeeDetailBO.save(userPassword);

                if (!(passDetails.isEmpty())) {
                    if (passDetails.size() == employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass()) {
                        employeeDetailBO.deleteLastPasswordForParticularEmployeCrossingDefineLimit(
                                passDetails.get(0).getPasswordId());
                    }
                }
                responce.put("status", "success");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            responce.put("status", "fail");
       	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        responce.put("status", "invalid");
   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /*
     * Check panic number exist or not.If yes make a call.If not please ask user
     * to update it.
     */

    @POST
    @Path("/panic")
    public Response panicNumbers(EFmFmUserMasterPO eFmFmUserMasterPO) throws ParseException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
      	 log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());
       	 log.info("getLatitudeLongitude  :" + eFmFmUserMasterPO.getLatitudeLongitude());
       	 log.info("address  :" + eFmFmUserMasterPO.getAddress());		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//        try{
//        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId())) && !(httpRequest.getHeader("authenticationToken").equalsIgnoreCase(null))){
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
      
        final List<EFmFmUserMasterPO> employeeDetails = employeeDetailBO.getParticularEmpDetailsFromUserId(
                eFmFmUserMasterPO.getUserId(), eFmFmUserMasterPO.geteFmFmClientBranchPO().getBranchId());
        if (!(employeeDetails.isEmpty())) {
            //Tk8=0 means check for NO
            if (employeeDetails.get(0).getPanicNumber().equalsIgnoreCase("Tk8=")) {
                responce.put("panicNumber",
                        employeeDetails.get(0).geteFmFmClientBranchPO().getEmergencyContactNumber());
            } else {
                responce.put("panicNumber",
                        "+" + employeeDetails.get(0).geteFmFmClientBranchPO().getEmergencyContactNumber());
            }
            responce.put("status", "success");
            try {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
					public void run() {
                        MessagingService messaging = new MessagingService();
                        String text = null;
                        try {
                            text = new String(Base64.getDecoder().decode(employeeDetails.get(0).getFirstName()), "utf-8")
                                    + " (Emp Id-"+employeeDetails.get(0).getEmployeeId()+") has an  Emergency!.\nLast location is at -"
                                    +eFmFmUserMasterPO.getAddress()+ ".\nPlease call him at-"
                                    + new String(Base64.getDecoder().decode(employeeDetails.get(0).getMobileNumber()), "utf-8") + ".";
                        } catch (UnsupportedEncodingException e1) {
                          log.info("error"+e1);          
                          }
                        try {
                            if (!(employeeDetails.get(0).getPanicNumber().equalsIgnoreCase("Tk8="))) {
                                messaging.cabHasLeftMessageForSch(new String(Base64.getDecoder().decode(employeeDetails.get(0).getPanicNumber())), text, "no");
                            }
                          messaging.cabHasLeftMessageForSch(employeeDetails.get(0).geteFmFmClientBranchPO().getTransportContactNumberForMsg(), text, "no");
                            messaging.cabHasLeftMessageForSch(employeeDetails.get(0).geteFmFmClientBranchPO().getEmergencyContactNumber(), text, "no");
                        } catch (Exception e) {
                            log.info("Register Message Sent Exception" + e);
                        }
                    }
                });
                thread1.start();
                log.info("Register Message Sent" + employeeDetails.get(0).getPanicNumber());
            } catch (Exception e) {
                log.info("panic message Error" + e);
            }
       	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();

        }
        responce.put("status", "fail");
   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    /*
     * Here you can update the panic number
     */

    @POST
    @Path("/panicnumberupdate")
    public Response panicNumbersUpdate(EFmFmUserMasterPO eFmFmUserMasterPO) throws ParseException, UnsupportedEncodingException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
      	 log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId()); 		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));

        
        
        try{
 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){
 				responce.put("status", "invalidRequest");
 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
 			}
 		
 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMasterPO.getUserId());
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
 		 
 		 
 		 
        
        
        List<EFmFmUserMasterPO> employeeDetails = employeeDetailBO.getParticularEmpDetailsFromUserId(
                eFmFmUserMasterPO.getUserId(), eFmFmUserMasterPO.geteFmFmClientBranchPO().getBranchId());
        if (employeeDetails.get(0).geteFmFmClientBranchPO().getEmergencyContactNumber().equalsIgnoreCase(eFmFmUserMasterPO.getPanicNumber())) {
            log.info("panicnumberupdate Status-"+"same"+eFmFmUserMasterPO.getPanicNumber());
        	responce.put("status", "same");
       	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        if (employeeDetails.get(0).getMobileNumber().equalsIgnoreCase(Base64.getEncoder().encodeToString(eFmFmUserMasterPO.getPanicNumber().getBytes("utf-8")))) {
            log.info("personalsame Status-"+"same"+eFmFmUserMasterPO.getPanicNumber());
        	responce.put("status", "personalsame");
       	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        log.info("panicnumberupdate Status-"+"success"+eFmFmUserMasterPO.getPanicNumber());
        employeeDetails.get(0).setPanicNumber(Base64.getEncoder().encodeToString(eFmFmUserMasterPO.getPanicNumber().getBytes("utf-8")));
        employeeDetailBO.update(employeeDetails.get(0));
        responce.put("status", "success");
   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
    /*
     * Employee Login Service Via SSO success
     * 
     */

    @POST
    @Path("/loginViaSSO")
    public Response loginViaSSO(EFmFmUserMasterPO eFmFmUserMasterPO) throws IOException {
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
      	 log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
       
        List<EFmFmUserMasterPO> userMasterDetail = employeeDetailBO
                .getParticularEmpDetailsFromUserName(eFmFmUserMasterPO.getUserName(), eFmFmUserMasterPO.getBranchId());
        log.info("userDetail" + userMasterDetail.size());
        String rolevalue="";
        if (!(userMasterDetail.isEmpty())) {
            List<EFmFmClientUserRolePO> userDetail = userMasterBO
                    .getAdminUserRoleByUserName(userMasterDetail.get(0).getUserName());                     
            if (userMasterDetail.get(0).getStatus().equalsIgnoreCase("N")) {
                responce.put("status", "disable");
           	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
            }
            List<Map<String, Object>> userAccessData = new ArrayList<Map<String, Object>>();
            responce.put("employeeName", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getFirstName())));
            responce.put("userId", userMasterDetail.get(0).getUserId());
            if(userDetail.isEmpty()){
                List<EFmFmClientUserRolePO> userRole = userMasterBO
                        .getUserRoleByUserName(userMasterDetail.get(0).getUserName());           
                responce.put("userRole",userRole.get(0).getEfmFmRoleMaster().getRole());
                rolevalue=userRole.get(0).getEfmFmRoleMaster().getRole();
            }
            else{
                responce.put("userRole",userDetail.get(0).getEfmFmRoleMaster().getRole());	
                rolevalue=userDetail.get(0).getEfmFmRoleMaster().getRole();	
            }
            responce.put("locationStatus", userMasterDetail.get(0).getLocationStatus());
            responce.put("panicStatus", userMasterDetail.get(0).geteFmFmClientBranchPO().getPanicAlertNeeded());
            
            responce.put("branchId", userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchId());
            responce.put("branchCode", userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchCode());
            responce.put("adhocTimePicker",
                    userMasterDetail.get(0).geteFmFmClientBranchPO().getAdhocTimePickerForEmployee());
            responce.put("dropPriorTimePeriod",
                    userMasterDetail.get(0).geteFmFmClientBranchPO().getDropPriorTimePeriod());
            responce.put("pickupPriorTimePeriod",
                    userMasterDetail.get(0).geteFmFmClientBranchPO().getPickupPriorTimePeriod());
            responce.put("driverName", userMasterDetail.get(0).geteFmFmClientBranchPO().getEmpDeviceDriverName());
            responce.put("driverMobile",
                    userMasterDetail.get(0).geteFmFmClientBranchPO().getEmpDeviceDriverMobileNumber());
            responce.put("driverProfilePic",
                    userMasterDetail.get(0).geteFmFmClientBranchPO().getDriverDeviceDriverProfilePicture());
            responce.put("employeeId", userMasterDetail.get(0).getEmployeeId());
            if(userMasterDetail.get(0).geteFmFmClientBranchPO().getMobileLoginVia().equalsIgnoreCase("SSO")) {
                responce.put("tempPassChange",true);	

            }else{
                responce.put("tempPassChange", userMasterDetail.get(0).isTempPassWordChange());	
            }
            
            responce.put("cutOffTimeFlg", userMasterDetail.get(0).geteFmFmClientBranchPO().getCutOffTime());   
            
            responce.put("locationVisible",userMasterDetail.get(0).geteFmFmClientBranchPO().getLocationVisible());    		
    		responce.put("destinationPointLimit",userMasterDetail.get(0).geteFmFmClientBranchPO().getDestinationPointLimit());       
            responce.put("callToDriver",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeCallToDriver());    		
            responce.put("callToTransportDesk",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeCallToTransport());
            //responce.put("requestWithProject",userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject()); 
            responce.put("employeeChecKInVia",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeChecKInVia());    		
            responce.put("token",userMasterDetail.get(0).getMobAuthorizationToken());   
            //responce.put("managerReqCreateProcess",userMasterDetail.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess()); 
            responce.put("reportAbug",userMasterDetail.get(0).geteFmFmClientBranchPO().getEmployeeAppReportBug()); 
            try{
                responce.put("empGeocode",userMasterDetail.get(0).getLatitudeLongitude());  
                }catch(Exception e){
                    responce.put("empGeocode","0.0");  
                }
            try {             	
           	 responce.put("multiplefacilityFlg", userMasterDetail.get(0).geteFmFmClientBranchPO().isMultiFacility());
           	 if(userMasterDetail.get(0).geteFmFmClientBranchPO().isMultiFacility()){
               	 List<Map<String, Object>> facilityDetails= getfacilityDetailsByUserId(userMasterDetail.get(0).getUserId());
                    if(!(facilityDetails.isEmpty())){
                    	responce.put("multiplefacilties",facilityDetails);	
                    }else{
                    	responce.put("multiplefacilties","MULTIFACILITYNOTFOUND");
                    }
           	 }
			} catch (Exception e) {
				responce.put("multiplefacilties","MULTIFACILITYNOTFOUND");					
			}
            
			if(rolevalue.equalsIgnoreCase("webuser")){                	
           	 if(userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("Yes")){
               	 responce.put("requestWithProject","Yes");
               	 responce.put("managerReqCreateProcess","NO");                    	
               }else{
               	responce.put("requestWithProject","NO");
              	    responce.put("managerReqCreateProcess","NO"); 
               }
           }else if(rolevalue.equalsIgnoreCase("manager") || rolevalue.equalsIgnoreCase("supervisor") ){  
           	
           	if(userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("YES") 
              			&& userMasterDetail.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess().equalsIgnoreCase("NO")){
                  	 responce.put("requestWithProject","YES");
                  	 responce.put("managerReqCreateProcess","NO");                    	
             	}else if(userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("NO") 
              			&& userMasterDetail.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess().equalsIgnoreCase("NO")){
                  	 responce.put("requestWithProject","NO");
                  	 responce.put("managerReqCreateProcess","NO");                    	
             	}else{
             		 responce.put("requestWithProject","YES");
                  	 responce.put("managerReqCreateProcess","YES"); 
             	}
           	
           	
             }else if(rolevalue.equalsIgnoreCase("admin") || rolevalue.equalsIgnoreCase("superadmin") ){
            	 if(userMasterDetail.get(0).geteFmFmClientBranchPO().getRequestWithProject().equalsIgnoreCase("YES") 
               			|| userMasterDetail.get(0).geteFmFmClientBranchPO().getManagerReqCreateProcess().equalsIgnoreCase("YES")){
                   	 responce.put("requestWithProject","YES");
                   	 responce.put("managerReqCreateProcess","YES");                    	
              	}
             }    	   
    		
            if (userMasterDetail.get(0).getPanicNumber().equalsIgnoreCase("Tk8=")) {
                responce.put("empPanicNumber",userMasterDetail.get(0).geteFmFmClientBranchPO().getTransportContactNumberForMsg()); 
            } else {
                responce.put("empPanicNumber", "+" + userMasterDetail.get(0).geteFmFmClientBranchPO().getTransportContactNumberForMsg()); 
            }
            
            //Tk8= means check for NO
            if (userMasterDetail.get(0).getSecondaryPanicNumber().equalsIgnoreCase("Tk8=")) {
                responce.put("empSecondaryPanicNumber", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getSecondaryPanicNumber())));
            } else {
                responce.put("empSecondaryPanicNumber", "+" + new String(Base64.getDecoder().decode(userMasterDetail.get(0).getSecondaryPanicNumber())));
            }
            
            responce.put("emergencyNum",
                    "+" + userMasterDetail.get(0).geteFmFmClientBranchPO().getEmergencyContactNumber());
            responce.put("emailId", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getEmailId()), "utf-8"));
            responce.put("designation", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getEmployeeDesignation())));
            responce.put("mobileNumber", new String(Base64.getDecoder().decode(userMasterDetail.get(0).getMobileNumber())));
            try {
                String profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
                        null);
                responce.put("profilePic", profilePicPath + userMasterDetail.get(0).getEmployeeProfilePic()
                        .substring(userMasterDetail.get(0).getEmployeeProfilePic().indexOf("upload") - 1));
            } catch (Exception e) {
                String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
                        "profilePic", null);
                responce.put("profilePic", defaultProfilePic);
            }
            responce.put("status", "success");
            userMasterDetail.get(0).setLastLoginTime(new Date());
            userMasterDetail.get(0).setWrongPassAttempt(0);
            userMasterDetail.get(0).setoTPGenratingCount(0);
            userMasterDetail.get(0).setoTPGenratingDate(new Date());
            userMasterDetail.get(0).setLoggedIn(true);
 //           userMasterDetail.get(0).setTempPassWordChange(true);
            employeeDetailBO.update(userMasterDetail.get(0));
            
            List<EFmFmEmployeeModuleMappingWithBranchPO> empModuleDetails=userMasterBO.getAllEmployeeModuleAccessFromBranchId(userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchId());
           if(!(empModuleDetails.isEmpty())){
            for(EFmFmEmployeeModuleMappingWithBranchPO module:empModuleDetails){
                Map<String, Object> requests = new HashMap<String, Object>();
                requests.put("moduleName", module.geteFmFmEmployeeModuleMasterPO().getEmpModuleName());
                requests.put("isActive", module.isEmpModuleStatus());
                userAccessData.add(requests);
            }}

            
   
            responce.put("moduleData", userAccessData);
       	    log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        } else {
            responce.put("status", "fail");
        }
   	 log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }

    
    
    public List<Map<String, Object>> getfacilityDetailsByUserId(int userId){
        IFacilityBO iFacilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");        
        List<Map<String, Object>> facilityByData = new ArrayList<Map<String, Object>>();
        List<EFmFmUserFacilityMappingPO> multipleFacility=iFacilityBO.
    			getAllFacilitiesAttachedToUser(userId);
        if (!(multipleFacility.isEmpty())) {
        	for(EFmFmUserFacilityMappingPO faciltyName:multipleFacility){
        		Map<String, Object> data = new HashMap<String, Object>();
        		data.put("facilityId", faciltyName.geteFmFmClientBranchPO().getBranchId());            	
            	data.put("facilityName", faciltyName.geteFmFmClientBranchPO().getBranchName());
            	facilityByData.add(data);
        	}      
        }	    
        return facilityByData;
    }

    public long getDisableTime(int hours, int minutes, Date checkIndate) {
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
}
