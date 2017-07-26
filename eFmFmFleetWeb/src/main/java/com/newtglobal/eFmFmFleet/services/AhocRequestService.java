package com.newtglobal.eFmFmFleet.services;

import java.io.UnsupportedEncodingException;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IEmployeeDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.CalculateDistance;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.PasswordEncryption;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchSubConfigurationPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientUserRolePO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeRequestMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRoleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripTimingMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/adhoc")
@Consumes("application/json")
@Produces("application/json")
public class AhocRequestService {
    
    DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
    private static Log log = LogFactory.getLog(AhocRequestService.class);
    
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

   

    /*
     * Adhoc request form  Web console
     * 
     * 
     */

    @POST
    @Path("/guestAdhocRequest")
    public Response cabRequestForGuest(EFmFmEmployeeRequestMasterPO travelRequest) throws ParseException {
        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        log.info("serviceStart -UserId :" + travelRequest.getUserId());
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequest.getUserId()))){
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(travelRequest.getUserId());
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
			log.info("authenticationToken error"+e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		 
        
   	    
        List<EFmFmUserMasterPO> guestIdExistsCheck = iEmployeeDetailBO.getEmpDetailsFromEmployeeId(
                travelRequest.getEmployeeId());
        log.info("travelRequest.getEmployeeId()"+travelRequest.getEmployeeId());
        if(travelRequest.getEmployeeId()==null || travelRequest.getEmployeeId()=="null"){
        	System.out.println("null block");
        	guestIdExistsCheck = iEmployeeDetailBO.getParticularEmpDetailsFromUserIdWithOutStatus(
                    travelRequest.getUserId(),travelRequest.getCombinedFacility());       	
        }
//        if (!(guestIdExistsCheck.isEmpty())) {
//            if (guestIdExistsCheck.get(0).getUserType().equalsIgnoreCase("employee")) {
//                responce.put("status", "alreadyRegisterAsEmp");
//                return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//            }
//        }

        EFmFmRouteAreaMappingPO routeAreaDetails = new EFmFmRouteAreaMappingPO();       
        try {
            DateFormat timeformate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
            String shiftDate = travelRequest.getTime();
            Date shift = shiftFormate.parse(shiftDate);
            java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
            Date startDate = timeformate.parse(travelRequest.getStartDate() + " " + "00:00");
            Date endDate = timeformate.parse(travelRequest.getEndDate() + " " + "23:59");
            if (!(guestIdExistsCheck.isEmpty())) {
                List<EFmFmEmployeeRequestMasterPO> existRequestsInReqMaster = iCabRequestBO
                        .getEmplyeeRequestsForSameDateAndShiftTime(startDate, shiftTime,
                        		String.valueOf(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId()),
                                guestIdExistsCheck.get(0).getUserId(), travelRequest.getTripType());
                if (!(existRequestsInReqMaster.isEmpty())) {
                    responce.put("status", "alreadyRaised");
                    log.info("serviceEnd -UserId :" + travelRequest.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }
                if (guestIdExistsCheck.get(0).getStatus().equalsIgnoreCase("N")) {
                    responce.put("status", "empDisable");
                    log.info("serviceEnd -UserId :" + travelRequest.getUserId());
                    return Response.ok(responce, MediaType.APPLICATION_JSON).build();
                }
                   // Emp detail From UserId
                EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO = new EFmFmClientProjectDetailsPO();
                
                if(travelRequest.getEmployeeId()==null || travelRequest.getEmployeeId()=="null"){
                    guestIdExistsCheck.get(0).setEmployeeId(guestIdExistsCheck.get(0).getEmployeeId());
                }
                else{
                    guestIdExistsCheck.get(0).setEmployeeId(travelRequest.getEmployeeId());
                }
                
                
   //             PasswordEncryption passwordEncryption= new PasswordEncryption();
   //             guestIdExistsCheck.get(0).setPassword(passwordEncryption.PasswordEncoderGenerator(travelRequest.getEmployeeId())); 
 //               guestIdExistsCheck.get(0).setUserName(travelRequest.getEmployeeId());
                
                guestIdExistsCheck.get(0).setHostMobileNumber(Base64.getEncoder().encodeToString(travelRequest.getHostMobileNumber().getBytes("utf-8")));
               // guestIdExistsCheck.get(0).setGuestMiddleLoc(Base64.getEncoder().encodeToString(travelRequest.getGuestMiddleLoc().getBytes("utf-8")));
                guestIdExistsCheck.get(0).setFirstName(Base64.getEncoder().encodeToString(travelRequest.getFirstName().getBytes("utf-8")));
                guestIdExistsCheck.get(0).setLastName(Base64.getEncoder().encodeToString(travelRequest.getLastName().getBytes("utf-8")));
                guestIdExistsCheck.get(0).setGender(Base64.getEncoder().encodeToString(travelRequest.getGender().toUpperCase().getBytes("utf-8")));
                guestIdExistsCheck.get(0).setMobileNumber(Base64.getEncoder().encodeToString(travelRequest.getMobileNumber().getBytes("utf-8")));
                guestIdExistsCheck.get(0).setEmailId(Base64.getEncoder().encodeToString(travelRequest.getEmailId().getBytes("utf-8")));
                //guestIdExistsCheck.get(0).setAddress(Base64.getEncoder().encodeToString(travelRequest.getAddress().getBytes("utf-8")));
                               
                guestIdExistsCheck.get(0).setStatus("Y");
                guestIdExistsCheck.get(0).setUpdatedTime(new Date());
                guestIdExistsCheck.get(0).setLastLoginTime(new Date());
                EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();              
                eFmFmClientBranch
                        .setBranchId(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
                guestIdExistsCheck.get(0).seteFmFmClientBranchPO(eFmFmClientBranch);
                eFmFmClientProjectDetailsPO.setProjectId(1);
                guestIdExistsCheck.get(0).seteFmFmClientProjectDetails(eFmFmClientProjectDetailsPO);
                guestIdExistsCheck.get(0).setLatitudeLongitude(travelRequest.getLatitudeLongitude());
                List<EFmFmClientBranchPO> clientBranchDetails = userMasterBO
                        .getClientDetails(String.valueOf(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId()));
                CalculateDistance empDistance = new CalculateDistance();
                try {
                    guestIdExistsCheck.get(0).setDistance(empDistance.employeeDistanceCalculation(
                            clientBranchDetails.get(0).getLatitudeLongitude(), travelRequest.getLatitudeLongitude()));
                } catch (Exception e) {
                }
                
                
                routeAreaDetails.setRouteAreaId(1);
                guestIdExistsCheck.get(0).seteFmFmRouteAreaMapping(routeAreaDetails);
                userMasterBO.update(guestIdExistsCheck.get(0));

                // Emp detail From EmployeeId
//                List<EFmFmUserMasterPO> employeeDetailFromEmpId = iEmployeeDetailBO
//                        .getParticularEmpDetailsFromEmployeeId(travelRequest.getEmployeeId(),
//                                travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());

                travelRequest
                        .setEmployeeId(guestIdExistsCheck.get(guestIdExistsCheck.size() - 1).getEmployeeId());
                EFmFmClientUserRolePO eFmFmClientUserRolePO = new EFmFmClientUserRolePO();
                EFmFmRoleMasterPO efmFmRoleMaster = new EFmFmRoleMasterPO();
                efmFmRoleMaster.setRoleId(4);
                eFmFmClientUserRolePO.setEfmFmUserMaster(guestIdExistsCheck.get(0));
                eFmFmClientUserRolePO.setEfmFmRoleMaster(efmFmRoleMaster);
                eFmFmClientUserRolePO.seteFmFmClientBranchPO(eFmFmClientBranch);
                EFmFmClientBranchSubConfigurationPO eFmFmClientBranchSubConfiguration = new EFmFmClientBranchSubConfigurationPO();
                eFmFmClientBranchSubConfiguration.setClientBranchSubConfigurationId(81);
                eFmFmClientUserRolePO.seteFmFmClientBranchSubConfiguration(eFmFmClientBranchSubConfiguration);
                userMasterBO.save(eFmFmClientUserRolePO);
                EFmFmUserMasterPO efmFmUserMaster = new EFmFmUserMasterPO();
                EFmFmEmployeeRequestMasterPO eFmFmEmployeeReqMasterPO = new EFmFmEmployeeRequestMasterPO();
                efmFmUserMaster.setUserId(guestIdExistsCheck.get(0).getUserId());
                EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO = new EFmFmRouteAreaMappingPO();
                eFmFmRouteAreaMappingPO
                        .setRouteAreaId(guestIdExistsCheck.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
                
                eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
                eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);
                eFmFmEmployeeReqMasterPO.setStatus("Y");
                eFmFmEmployeeReqMasterPO.setRequestType("AdhocRequest");
                eFmFmEmployeeReqMasterPO.setReadFlg("Y");
                eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
                eFmFmEmployeeReqMasterPO.setDropSequence(1);
                eFmFmEmployeeReqMasterPO.setRequestFrom(travelRequest.getRequestFrom());
                eFmFmEmployeeReqMasterPO.setDestination1Address(travelRequest.getDestination1Address());
                eFmFmEmployeeReqMasterPO.setDestination2Address(travelRequest.getDestination2Address());
                eFmFmEmployeeReqMasterPO.setDestination3Address(travelRequest.getDestination3Address());
                eFmFmEmployeeReqMasterPO.setDestination4Address(travelRequest.getDestination4Address());
                eFmFmEmployeeReqMasterPO.setDestination5Address(travelRequest.getDestination5Address());
                eFmFmEmployeeReqMasterPO.setDestination1AddressLattitudeLongitude(travelRequest.getDestination1AddressLattitudeLongitude());
                eFmFmEmployeeReqMasterPO.setDestination2AddressLattitudeLongitude(travelRequest.getDestination2AddressLattitudeLongitude()); 
                eFmFmEmployeeReqMasterPO.setDestination3AddressLattitudeLongitude(travelRequest.getDestination3AddressLattitudeLongitude());
                eFmFmEmployeeReqMasterPO.setDestination4AddressLattitudeLongitude(travelRequest.getDestination4AddressLattitudeLongitude());
                eFmFmEmployeeReqMasterPO.setDestination5AddressLattitudeLongitude(travelRequest.getDestination5AddressLattitudeLongitude());
                eFmFmEmployeeReqMasterPO.setOriginAddress(travelRequest.getOriginAddress());   
                eFmFmEmployeeReqMasterPO.setEndDestinationAddress(travelRequest.getEndDestinationAddress()); 
                eFmFmEmployeeReqMasterPO.setEndDestinationAddressLattitudeLongitude(travelRequest.getEndDestinationAddressLattitudeLongitude());  
                eFmFmEmployeeReqMasterPO.setDurationInHours(travelRequest.getDurationInHours());
                eFmFmEmployeeReqMasterPO.setOriginLattitudeLongitude(travelRequest.getOriginLattitudeLongitude());   
                eFmFmEmployeeReqMasterPO.setAccountName(travelRequest.getAccountName());
                eFmFmEmployeeReqMasterPO.setPaymentType(travelRequest.getPaymentType());
                eFmFmEmployeeReqMasterPO.setRemarks(travelRequest.getRemarks());
                eFmFmEmployeeReqMasterPO.setReservationType(travelRequest.getReservationType());
                eFmFmEmployeeReqMasterPO.setBookedBy(travelRequest.getBookedBy());
                eFmFmEmployeeReqMasterPO.setChargedTo(travelRequest.getChargedTo());                               
                eFmFmEmployeeReqMasterPO.setRequestDate(new Date());
                eFmFmEmployeeReqMasterPO.setTripType(travelRequest.getTripType());
                eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
                eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
                eFmFmEmployeeReqMasterPO.seteFmFmRouteAreaMapping(routeAreaDetails);
                iCabRequestBO.save(eFmFmEmployeeReqMasterPO);
            } else {
                // Emp detail From UserId
                EFmFmUserMasterPO employeeDetailsPO = new EFmFmUserMasterPO();
                EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO = new EFmFmClientProjectDetailsPO();
                employeeDetailsPO.setEmployeeId(travelRequest.getEmployeeId());                
                PasswordEncryption passwordEncryption= new PasswordEncryption();
                employeeDetailsPO.setPassword(passwordEncryption.PasswordEncoderGenerator(travelRequest.getEmployeeId())); 
                employeeDetailsPO.setUserName(travelRequest.getEmployeeId());
                employeeDetailsPO.setHostMobileNumber(Base64.getEncoder().encodeToString(travelRequest.getHostMobileNumber().getBytes("utf-8")));
               // employeeDetailsPO.setGuestMiddleLoc(Base64.getEncoder().encodeToString(travelRequest.getGuestMiddleLoc().getBytes("utf-8")));
                employeeDetailsPO.setFirstName(Base64.getEncoder().encodeToString(travelRequest.getFirstName().getBytes("utf-8")));
                employeeDetailsPO.setLastName(Base64.getEncoder().encodeToString(travelRequest.getLastName().getBytes("utf-8")));
                employeeDetailsPO.setGender(Base64.getEncoder().encodeToString(travelRequest.getGender().toUpperCase().getBytes("utf-8")));
                employeeDetailsPO.setMobileNumber(Base64.getEncoder().encodeToString(travelRequest.getMobileNumber().getBytes("utf-8")));
                employeeDetailsPO.setEmailId(Base64.getEncoder().encodeToString(travelRequest.getEmailId().getBytes("utf-8")));
                employeeDetailsPO.setAddress(Base64.getEncoder().encodeToString(travelRequest.getOriginAddress().getBytes("utf-8")));               
                
                employeeDetailsPO.setPhysicallyChallenged(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));               
                employeeDetailsPO.setEmployeeDesignation(Base64.getEncoder().encodeToString("Shell".getBytes("utf-8")));
                employeeDetailsPO.setPanicNumber(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
                employeeDetailsPO.setIsInjured(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
                employeeDetailsPO.setPragnentLady(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
                employeeDetailsPO.setCreationTime(new Date());
                employeeDetailsPO.setUpdatedTime(new Date());   
                employeeDetailsPO.setDeviceId("NO");
                employeeDetailsPO.setEmployeeProfilePic("NO");
                employeeDetailsPO.setLastLoginTime(new Date());
                employeeDetailsPO.setTempPassWordChange(false);
				employeeDetailsPO.setPasswordChangeDate(new Date());
                employeeDetailsPO.setWrongPassAttempt(0);
                EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
                eFmFmClientBranch
                        .setBranchId(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
                employeeDetailsPO.seteFmFmClientBranchPO(eFmFmClientBranch);
                employeeDetailsPO.setStatus("Y");
                employeeDetailsPO.setLocationStatus("N");
                eFmFmClientProjectDetailsPO.setProjectId(1);
                employeeDetailsPO.seteFmFmClientProjectDetails(eFmFmClientProjectDetailsPO);

                employeeDetailsPO.setLatitudeLongitude(travelRequest.getLatitudeLongitude());
                employeeDetailsPO.setWeekOffDays("Sunday");
                employeeDetailsPO.setUserType("guest");

                List<EFmFmClientBranchPO> clientBranchDetails = userMasterBO
                        .getClientDetails(String.valueOf(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId()));
                CalculateDistance empDistance = new CalculateDistance();
                try {
                    employeeDetailsPO.setDistance(empDistance.employeeDistanceCalculation(
                            clientBranchDetails.get(0).getLatitudeLongitude(), travelRequest.getLatitudeLongitude()));
                } catch (Exception e) {
                }
                routeAreaDetails.setRouteAreaId(1);
                employeeDetailsPO.seteFmFmRouteAreaMapping(routeAreaDetails);
                userMasterBO.save(employeeDetailsPO);

                // Emp detail From EmployeeId
                List<EFmFmUserMasterPO> employeeDetailFromEmpId = iEmployeeDetailBO
                        .getParticularEmpDetailsFromEmployeeId(travelRequest.getEmployeeId(),travelRequest.getCombinedFacility());
                travelRequest.setEmployeeId(employeeDetailFromEmpId.get(0).getEmployeeId());
                EFmFmClientUserRolePO eFmFmClientUserRolePO = new EFmFmClientUserRolePO();
                EFmFmRoleMasterPO efmFmRoleMaster = new EFmFmRoleMasterPO();
                efmFmRoleMaster.setRoleId(4);
                eFmFmClientUserRolePO.setEfmFmUserMaster(employeeDetailFromEmpId.get(0));
                eFmFmClientUserRolePO.setEfmFmRoleMaster(efmFmRoleMaster);
                eFmFmClientUserRolePO.seteFmFmClientBranchPO(eFmFmClientBranch);

                EFmFmClientBranchSubConfigurationPO eFmFmClientBranchSubConfiguration = new EFmFmClientBranchSubConfigurationPO();
                eFmFmClientBranchSubConfiguration.setClientBranchSubConfigurationId(81);
                eFmFmClientUserRolePO.seteFmFmClientBranchSubConfiguration(eFmFmClientBranchSubConfiguration);

                userMasterBO.save(eFmFmClientUserRolePO);
                EFmFmUserMasterPO efmFmUserMaster = new EFmFmUserMasterPO();
                EFmFmEmployeeRequestMasterPO eFmFmEmployeeReqMasterPO = new EFmFmEmployeeRequestMasterPO();
                efmFmUserMaster.setUserId(employeeDetailFromEmpId.get(employeeDetailFromEmpId.size() - 1).getUserId());
                EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO = new EFmFmRouteAreaMappingPO();
                eFmFmRouteAreaMappingPO
                        .setRouteAreaId(employeeDetailFromEmpId.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
                eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
                eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);
                eFmFmEmployeeReqMasterPO.setStatus("Y");
                eFmFmEmployeeReqMasterPO.setRequestType("AdhocRequest");
                eFmFmEmployeeReqMasterPO.setReadFlg("Y");
                eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
                eFmFmEmployeeReqMasterPO.setDropSequence(1);
                eFmFmEmployeeReqMasterPO.setRequestFrom(travelRequest.getRequestFrom());
                eFmFmEmployeeReqMasterPO.setDestination1Address(travelRequest.getDestination1Address());
                eFmFmEmployeeReqMasterPO.setDestination2Address(travelRequest.getDestination2Address());
                eFmFmEmployeeReqMasterPO.setDestination3Address(travelRequest.getDestination3Address());
                eFmFmEmployeeReqMasterPO.setDestination4Address(travelRequest.getDestination4Address());
                eFmFmEmployeeReqMasterPO.setDestination5Address(travelRequest.getDestination5Address());
                eFmFmEmployeeReqMasterPO.setDestination1AddressLattitudeLongitude(travelRequest.getDestination1AddressLattitudeLongitude());
                eFmFmEmployeeReqMasterPO.setDestination2AddressLattitudeLongitude(travelRequest.getDestination2AddressLattitudeLongitude()); 
                eFmFmEmployeeReqMasterPO.setDestination3AddressLattitudeLongitude(travelRequest.getDestination3AddressLattitudeLongitude());
                eFmFmEmployeeReqMasterPO.setDestination4AddressLattitudeLongitude(travelRequest.getDestination4AddressLattitudeLongitude());
                eFmFmEmployeeReqMasterPO.setDestination5AddressLattitudeLongitude(travelRequest.getDestination5AddressLattitudeLongitude());
                eFmFmEmployeeReqMasterPO.setOriginAddress(travelRequest.getOriginAddress());   
                eFmFmEmployeeReqMasterPO.setEndDestinationAddress(travelRequest.getEndDestinationAddress()); 
                eFmFmEmployeeReqMasterPO.setEndDestinationAddressLattitudeLongitude(travelRequest.getEndDestinationAddressLattitudeLongitude());  
                eFmFmEmployeeReqMasterPO.setDurationInHours(travelRequest.getDurationInHours());
                eFmFmEmployeeReqMasterPO.setOriginLattitudeLongitude(travelRequest.getOriginLattitudeLongitude());   
                eFmFmEmployeeReqMasterPO.setAccountName(travelRequest.getAccountName());
                eFmFmEmployeeReqMasterPO.setPaymentType(travelRequest.getPaymentType());
                eFmFmEmployeeReqMasterPO.setRemarks(travelRequest.getRemarks());
                eFmFmEmployeeReqMasterPO.setReservationType(travelRequest.getReservationType());
                eFmFmEmployeeReqMasterPO.setBookedBy(travelRequest.getBookedBy());
                eFmFmEmployeeReqMasterPO.setChargedTo(travelRequest.getChargedTo());                               
                eFmFmEmployeeReqMasterPO.setRequestDate(new Date());
                eFmFmEmployeeReqMasterPO.setTripType(travelRequest.getTripType());
                eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
                eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
                eFmFmEmployeeReqMasterPO.seteFmFmRouteAreaMapping(routeAreaDetails);
                iCabRequestBO.save(eFmFmEmployeeReqMasterPO);
            }
            responce.put("status", "success");
        } catch (Exception e) {
            log.info("Inside error blog" + e);
        }
        responce.put("status", "success");
        log.info("serviceEnd -UserId :" + travelRequest.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    
    }
    
    //AdhocRequest for the manila accept button click
    
    @POST
    @Path("/employeeRequestAcceptance")
    public Response acceptAdhocRequestButtonClick(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
            throws ParseException, UnsupportedEncodingException {
        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");	
        Map<String, Object> responce = new HashMap<String, Object>(); 	
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
  	  log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		
			
			}}catch(Exception e){
    		log.info("authenticationToken error"+e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		 List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
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
        
   	    
		List<EFmFmEmployeeTravelRequestPO> cabRequestDetail = iCabRequestBO.getParticularRequestDetailOnTripComplete(employeeTravelRequestPO.getRequestId());
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		if(!(cabRequestDetail.isEmpty())){		
			try {String toMailId = new String(
					Base64.getDecoder().decode(cabRequestDetail.get(0).getEfmFmUserMaster().getEmailId()),
					"utf-8");
			String reqDate = dateFormat.format(cabRequestDetail.get(0).getRequestDate());
				log.info("employeeRequestAcceptance  mail confirmation");
				Thread thread1 = new Thread(new Runnable() {
					@Override
					public void run() {
						SendMailBySite mailSender = new SendMailBySite();									
						mailSender.manilaAdhocrequestAcceptance(toMailId, shiftFormate.format(cabRequestDetail.get(0).getShiftTime()), cabRequestDetail.get(0).getTripType(), reqDate,cabRequestDetail.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId());											
					}
				});
				thread1.start();
			} catch (Exception e) {
				log.info("employeeRequestAcceptance  mail confirmation" + e);
			}
			cabRequestDetail.get(0).setRequestRemarks("Acceptance");
			iCabRequestBO.update(cabRequestDetail.get(0));						
		 }
		log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
  //AdhocRequest for the manila Reject button click
    
    @POST
    @Path("/employeeRequestRejectance")
    public Response rejectAdhocRequestButtonClick(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
            throws ParseException, UnsupportedEncodingException {
        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
      	 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());

        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){
        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
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
        		log.info("authenticationToken error"+e);
    			responce.put("status", "invalidRequest");
    			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

        	}
        
        
		List<EFmFmEmployeeTravelRequestPO> cabRequestDetail = iCabRequestBO.getParticularRequestDetailOnTripComplete(employeeTravelRequestPO.getRequestId());
		if(!(cabRequestDetail.isEmpty())){					
			try {
				String toMailId = new String(
						Base64.getDecoder().decode(cabRequestDetail.get(0).getEfmFmUserMaster().getEmailId()),
						"utf-8");
			Thread thread1 = new Thread(new Runnable() {
				@Override
				public void run() {
					SendMailBySite mail =new SendMailBySite();
					mail.manilaAdhocrequestRejectMail(toMailId,cabRequestDetail.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId());
				}
			});
			thread1.start();
		} catch (Exception e) {
			log.info("manila  request  rejection confirmation" + e);
		}
			try{
			cabRequestDetail.get(0).setRequestStatus("C");
			cabRequestDetail.get(0).setIsActive("N");
			cabRequestDetail.get(0).setApproveStatus("Y");
			cabRequestDetail.get(0).setReadFlg("N");
			iCabRequestBO.update(cabRequestDetail.get(0));
		} catch (Exception e) {
			log.info("manila  request  rejection button click on Adhocctravel request" + e);
		}
		 }
		responce.put("status", "success");	
		log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
    
    
    
    @POST
    @Path("/employeeAdhocRoster")
    public Response employeeAdhocRosterDetails(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
            throws ParseException, UnsupportedEncodingException {
        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        List<Map<String, Object>> shitTimings = new ArrayList<Map<String, Object>>();
        Map<String, Object> responce = new HashMap<String, Object>();
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
        if(employeeTravelRequestPO.getStartPgNo()==0){
        try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){
        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}}catch(Exception e){
        		log.info("authenticationToken error"+e);
    			responce.put("status", "invalidRequest");
    			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
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
        
        }
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat shiftDateFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        DateFormat shiftTimeFormaters = new SimpleDateFormat("HH:mm:ss");
        employeeTravelRequestPO.setRequestDate(new Date());
        List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO.listOfShiftTime(new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getUserId(),employeeTravelRequestPO.getCombinedFacility()));
        if (!(shiftTimeDetails.isEmpty())) {
            for (EFmFmTripTimingMasterPO shiftiming : shiftTimeDetails) {
                Map<String, Object> requestList = new HashMap<String, Object>();
                requestList.put("shiftTime", shiftTimeFormater.format(shiftiming.getShiftTime()));
                shitTimings.add(requestList);
            }
        }        
        
        List<EFmFmEmployeeTravelRequestPO> travelDetails = iCabRequestBO
                .listOfAdhocReservationsForGuestTravelRequests(new MultifacilityService().combinedBranchIdDetails(
                		employeeTravelRequestPO.getUserId(),employeeTravelRequestPO.getCombinedFacility()),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
        
        List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
        if (!(travelDetails.isEmpty())) {
            for (EFmFmEmployeeTravelRequestPO allTravelRequest : travelDetails) {
                Map<String, Object> requestList = new HashMap<String, Object>();
                requestList.put("employeeId",
                        allTravelRequest.getEfmFmUserMaster().getEmployeeId());
                requestList.put("requestId", allTravelRequest.getRequestId());
                String cabAvailableTime = formatter.format(allTravelRequest.getRequestDate()) + " "
                        + allTravelRequest.getShiftTime();
                if (allTravelRequest.getTripType().equalsIgnoreCase("DROP")) {
                    requestList.put("pickUpTime", allTravelRequest.getDropSequence());
                    if (shiftDateFormater.parse(cabAvailableTime).getTime() < System.currentTimeMillis() + 900000) {
                        requestList.put("cabAvailable", "Cab not available");
                    } else {
                        requestList.put("cabAvailable", "Waiting");
                    }
                } else {
                    try {
                        requestList.put("pickUpTime", shiftTimeFormater.format(allTravelRequest.getPickUpTime()));
                    } catch (Exception e) {
                        requestList.put("pickUpTime", "0");
                    }
                    if (shiftDateFormater.parse(cabAvailableTime).getTime() < System.currentTimeMillis() + 16200000) {
                        requestList.put("cabAvailable", "Cab not available");
                    } else {
                        requestList.put("cabAvailable", "Waiting");
                    }
                }
                
                try{
                	if(allTravelRequest.getRequestRemarks().equalsIgnoreCase("trest"));
                    requestList.put("requestButtonStatus", "Y");	
                }catch(Exception e){
                    requestList.put("requestButtonStatus", "N");	
                }
                requestList.put("tripDate", formatter.format(allTravelRequest.getRequestDate()));
                requestList.put("weekOffs", allTravelRequest.getEfmFmUserMaster().getWeekOffDays());
                requestList.put("nodalPoints",
                        allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints());
                requestList.put("nodalPointId",
                        allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointId());
                requestList.put("nodalPointTitle",
                        allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());
                requestList.put("nodalPointDescription", allTravelRequest.geteFmFmRouteAreaMapping()
                        .geteFmFmNodalAreaMaster().getNodalPointDescription());
                requestList.put("tripType", allTravelRequest.getTripType());
                requestList.put("requestType", allTravelRequest.getRequestType());
                requestList.put("tripTime", shiftTimeFormaters.format(allTravelRequest.getShiftTime()));                        
                requestList.put("employeeRouteName",
                        allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
                requestList.put("employeeRouteId",
                        allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
                requestList.put("employeeAreaName",
                        allTravelRequest.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
                requestList.put("employeeAreaName",
                        allTravelRequest.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
                requestList.put("mobileNumber", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getMobileNumber()), "utf-8"));
                requestList.put("startDate", formatter.format(allTravelRequest.getRequestDate()));
                requestList.put("endDate", formatter.format(allTravelRequest.geteFmFmEmployeeRequestMaster().getTripRequestEndDate()));
                requestList.put("employeeAreaId",
                        allTravelRequest.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaId());
                requestList.put("employeePickUpTime", allTravelRequest.geteFmFmEmployeeRequestMaster().getPickUpTime());
                requestList.put("employeeName",new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));               
                log.info("EmployeeId"+allTravelRequest.getEfmFmUserMaster().getEmployeeId());
                requestList.put("employeeAddress", new String(Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
                if(allTravelRequest.getRequestType().equalsIgnoreCase("AdhocRequest")){
                requestList.put("chargedTo",allTravelRequest.geteFmFmEmployeeRequestMaster().getChargedTo()); 
                requestList.put("bookedBy",allTravelRequest.geteFmFmEmployeeRequestMaster().getBookedBy());                 
                requestList.put("pickUpTime", shiftTimeFormaters.format(allTravelRequest.getShiftTime()));                        
                requestList.put("accountName",allTravelRequest.geteFmFmEmployeeRequestMaster().getAccountName());             
                requestList.put("originAddress",allTravelRequest.geteFmFmEmployeeRequestMaster().getOriginAddress());                        
                requestList.put("destinationAddress",allTravelRequest.geteFmFmEmployeeRequestMaster().getEndDestinationAddress());
                requestList.put("destinationAddress1",allTravelRequest.geteFmFmEmployeeRequestMaster().getDestination1Address());
                requestList.put("destinationAddress2",allTravelRequest.geteFmFmEmployeeRequestMaster().getDestination2Address());
                requestList.put("destinationAddress3",allTravelRequest.geteFmFmEmployeeRequestMaster().getDestination3Address());
                requestList.put("destinationAddress4",allTravelRequest.geteFmFmEmployeeRequestMaster().getDestination4Address()); 
                requestList.put("destinationAddress5",allTravelRequest.geteFmFmEmployeeRequestMaster().getDestination5Address());        
                requestList.put("reservationType",allTravelRequest.geteFmFmEmployeeRequestMaster().getReservationType());      
                requestList.put("durationInHours",allTravelRequest.geteFmFmEmployeeRequestMaster().getDurationInHours());        
                requestList.put("paymentType",allTravelRequest.geteFmFmEmployeeRequestMaster().getPaymentType());                
                requestList.put("remarks",allTravelRequest.geteFmFmEmployeeRequestMaster().getRemarks()); 
                }
                requestList.put("employeeWaypoints",
                        allTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getLatitudeLongitude());
                travelRequestList.add(requestList);
            }

        }
        responce.put("shifts", shitTimings);
        responce.put("requests", travelRequestList);
        log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());       
        return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    }
}
