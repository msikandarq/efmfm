package com.newtglobal.eFmFmFleet.services;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/learnRoute")
@Consumes("application/json")
@Produces("application/json")
public class RememberRouteService {

	private static Log log = LogFactory.getLog(RememberRouteService.class);
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
	DateFormat requestDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	Format formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();


	@POST
	@Path("/rememberRoute")
	public Response getAllZonesAndRoutesDetails(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
				
		Map<String, Object> responce = new HashMap<String, Object>();			
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
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
		 log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		
		
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
        log.info("to date"+assignRoutePO.getToDate());		
		String shiftDate = assignRoutePO.getTime();
		Date shift = shiftTimeFormater.parse(shiftDate);
		java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
		assignRoutePO.setShiftTime(shiftTime);		
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");	
				
		//Request Date 
		Date toDate = dateFormat.parse(assignRoutePO.getToDate());
		String travelRequestDate = formatter.format(toDate);		
		
		//Remember Route Date
		Date date = dateFormat.parse(assignRoutePO.getFromDate());
		String requestDate = requestDateFormat.format(date);
		assignRoutePO.setToDate(requestDate);		
		assignRoutePO.setCombinedFacility(assignRoutePO.getCombinedFacility());
        //Trip Assign Date
		List<EFmFmAssignRoutePO> assignRoute=assignRouteBO.getAssignRouteDetailsByDate(assignRoutePO);
        log.info("totalroutes"+assignRoute.size());	
		List<EFmFmAssignRoutePO> routeDetails = null;
        if(!(assignRoute.isEmpty())){
            for(EFmFmAssignRoutePO routeDetail:assignRoute){
            	
			List<EFmFmEmployeeTripDetailPO> employeeTripDetail = iCabRequestBO.getParticularTripAllEmployees(routeDetail.getAssignRouteId());			
			if(!(employeeTripDetail.isEmpty())){				
				List<EFmFmVehicleCheckInPO> allCheckInVehicles = null;	
				allCheckInVehicles = iVehicleCheckInBO.getVehicleAlreadyCheckInOrNot(routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());				  
				  if(allCheckInVehicles.isEmpty()){
		                 allCheckInVehicles = iCabRequestBO.getAllCheckedInVehiclesForSpecificCapacity(assignRoutePO.getCombinedFacility(),employeeTripDetail.size());
					}
					if(allCheckInVehicles.isEmpty()){
						allCheckInVehicles = iCabRequestBO.getAllDummyCheckedInVehiclesForGreatestCapacity(assignRoutePO.getCombinedFacility(),employeeTripDetail.size());

					}
					if(allCheckInVehicles.isEmpty()){
						allCheckInVehicles = iCabRequestBO.getAllDummyCheckedInVehiclesForLessCapacity(assignRoutePO.getCombinedFacility(),employeeTripDetail.size());
					}
				  
            boolean routeEntry=true;
			if (!allCheckInVehicles.isEmpty()) {
				log.info("AssignRoute"+routeDetail.getAssignRouteId());
			for(EFmFmEmployeeTripDetailPO employeeDetail:employeeTripDetail){
				List<EFmFmEmployeeTravelRequestPO> travelRequestDetail=iCabRequestBO.getParticularActiveRequestDetail(employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getUserId(), assignRoutePO.getTripType(),travelRequestDate,shiftTime);
				log.info("travelRequestDetail"+travelRequestDetail.size()); 
				if(!(travelRequestDetail.isEmpty())){
                	if(employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getUserId()==travelRequestDetail.get(0).getEfmFmUserMaster().getUserId()){               	       			
                		log.info("Inside Equal............"+assignRoutePO.getTripType());
                		//Start new route entry
                		if(routeEntry){
                			routeEntry=false;
        				EFmFmVehicleMasterPO updateVehicleStatus = iCabRequestBO
        						.getVehicleDetail(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId());
        				updateVehicleStatus.setStatus("A");
        				iVehicleCheckInBO.update(updateVehicleStatus);
        				EFmFmDriverMasterPO particularDriverDetails = approvalBO
        						.getParticularDriverDetail(allCheckInVehicles.get(0).getEfmFmDriverMaster().getDriverId());
        				particularDriverDetails.setStatus("A");
        				approvalBO.update(particularDriverDetails);
        				List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
        						allCheckInVehicles.get(0).geteFmFmDeviceMaster().getDeviceId(),
        						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
        				deviceDetails.get(0).setStatus("Y");
        				iVehicleCheckInBO.update(deviceDetails.get(0));
        			String requestDateAndTime = travelRequestDate + " " + assignRoutePO.getTime();

        			EFmFmAssignRoutePO assignRouteEntry = new EFmFmAssignRoutePO();
        			EFmFmVehicleCheckInPO vehicleCheckInPO = new EFmFmVehicleCheckInPO();
        			vehicleCheckInPO
        					.setCheckInId(allCheckInVehicles.get(0).getCheckInId());	
        			assignRouteEntry.setEfmFmVehicleCheckIn(vehicleCheckInPO);
        			EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
        			routeAreaMapping.setRouteAreaId(routeDetail.geteFmFmRouteAreaMapping().getRouteAreaId());			
        			assignRouteEntry.seteFmFmRouteAreaMapping(routeAreaMapping);
        			assignRouteEntry.setEscortRequredFlag("N");
        			assignRouteEntry.setAllocationMsg("N");
        			assignRouteEntry.setShiftTime(routeDetail.getShiftTime());
        			assignRouteEntry.setTripStatus("allocated");
        			assignRouteEntry.setLocationFlg("N");
					assignRouteEntry.setTripConfirmationFromDriver("Not Delivered");

        			assignRouteEntry.setIsToll("No");
        			assignRouteEntry.setRoutingCompletionStatus("Started");
        			assignRouteEntry.setSelectedBaseFacility(routeDetail.getSelectedBaseFacility());
        			assignRouteEntry.setAssignedVendorName("NA"); 
        			assignRouteEntry.setIsBackTwoBack("N");
        			assignRouteEntry.setDistanceUpdationFlg("Y");
        			assignRouteEntry.setPlannedReadFlg("Y");
        			assignRouteEntry.setScheduleReadFlg("Y");
        			assignRouteEntry.setRemarksForEditingTravelledDistance("NO");
        			assignRouteEntry.setEditDistanceApproval("NO");
        			assignRouteEntry.setRouteGenerationType(routeDetail.getRouteGenerationType());
        			assignRouteEntry.setCreatedDate(new Date());
        			assignRouteEntry.setTripAssignDate(requestDateTimeFormat.parse(requestDateAndTime));
        			assignRouteEntry.setTripUpdateTime(requestDateTimeFormat.parse(requestDateAndTime));
        			assignRouteEntry.setVehicleStatus("A");
        			assignRouteEntry.setBucketStatus("N");
        			EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
        			eFmFmClientBranchPO
        					.setBranchId(routeDetail.geteFmFmClientBranchPO().getBranchId());
        			assignRouteEntry.seteFmFmClientBranchPO(eFmFmClientBranchPO);
        			assignRouteEntry.setTripType(assignRoutePO.getTripType());
        			allCheckInVehicles.get(0).setStatus("N");			
        			if(routeDetail.getEscortRequredFlag().equalsIgnoreCase("Y")){				
        				List<EFmFmEscortCheckInPO> escortList = iCabRequestBO
        						.getAllCheckedInEscort(assignRoutePO.getCombinedFacility());
        				if (!escortList.isEmpty()) {
        					EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
        					checkInEscort.setEscortCheckInId(
        							escortList.get(0).getEscortCheckInId());
        					assignRouteEntry.seteFmFmEscortCheckIn(checkInEscort);
        					escortList.get(0).setStatus("N");
        					iVehicleCheckInBO.update(escortList.get(0));
        				}
        				assignRouteEntry.setEscortRequredFlag("Y");
        			}
        			iVehicleCheckInBO
        					.update(allCheckInVehicles.get(0));
        			iCabRequestBO.update(assignRouteEntry);
            		routeDetails = iCabRequestBO.getHalfCompletedAssignRouteFromCheckInIdByDate(travelRequestDate,employeeDetail.geteFmFmEmployeeTravelRequest().geteFmFmClientBranchPO().getBranchId(),
            				routeDetail.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
        					routeDetail.getTripType(), routeDetail.getShiftTime(),
        					allCheckInVehicles.get(0).getCheckInId());
                	}
                		//End new route entry
                		EFmFmEmployeeTripDetailPO employeeTripDetailPO = new EFmFmEmployeeTripDetailPO();
        				log.info("OutSide manual"+assignRoutePO.getTripType());
        				if(!(routeDetails.isEmpty())){
            				log.info("Inside manual"+assignRoutePO.getTripType());
    						if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
    							travelRequestDetail.get(0).setDropSequence(employeeDetail.geteFmFmEmployeeTravelRequest().getDropSequence());
    							employeeTripDetailPO.setTenMinuteMessageStatus("Y");
    							employeeTripDetailPO.setTwoMinuteMessageStatus("Y");
    							employeeTripDetailPO.setReachedFlg("Y");
    							employeeTripDetailPO.setCabDelayMsgStatus("Y");
    						} else {
    							travelRequestDetail.get(0).setPickUpTime(employeeDetail.geteFmFmEmployeeTravelRequest().getPickUpTime());
    							employeeTripDetailPO.setTenMinuteMessageStatus("N");
    							employeeTripDetailPO.setTwoMinuteMessageStatus("N");
    							employeeTripDetailPO.setReachedFlg("N");
    							employeeTripDetailPO.setCabDelayMsgStatus("N");
    						}
    						employeeTripDetailPO.setActualTime(new Date());
    						employeeTripDetailPO.setGoogleEta(0);
    						employeeTripDetailPO.setBoardedFlg("N");
    						employeeTripDetailPO.seteFmFmEmployeeTravelRequest(travelRequestDetail.get(0));
    						employeeTripDetailPO.setEfmFmAssignRoute(routeDetails.get(0));
    						employeeTripDetailPO.setCurrenETA("0");
    						employeeTripDetailPO.setEmployeeStatus("allocated");
    						employeeTripDetailPO.setComingStatus("Yet to confirm");
    						employeeTripDetailPO.setEmployeeOnboardStatus("NO");
    						iCabRequestBO.update(employeeTripDetailPO);  
    	  						
    						travelRequestDetail.get(0).setReadFlg("R");
    						travelRequestDetail.get(0).setRequestColor("white");
                			iCabRequestBO.update(travelRequestDetail.get(0));

                		}
                		
                	}
    			}
                }				
			}
			else{
				responce.put("status", "dVehicleNotAvailable");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
        }	
        }
		responce.put("status", "emptyRoute");
        }else{
    		responce.put("status", "emptyRoute");
        }
        log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

}
