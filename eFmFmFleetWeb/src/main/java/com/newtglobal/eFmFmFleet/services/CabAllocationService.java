package com.newtglobal.eFmFmFleet.services;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLocationMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmMultipleLocationTvlReqPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/request")
public class CabAllocationService {
	private static Log log = LogFactory.getLog(CabAllocationService.class);

	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	
	@POST
	@Path("/caballocation")
	@Consumes("application/json")
	@Produces("application/json")
	public void getAllRequestOfParticularShiftAndRouteForAllocation(EFmFmEmployeeTravelRequestPO travelRequestPO)
			throws ParseException {		
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));		
		 log.info("serviceStart -UserId :" + travelRequestPO.getUserId());
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		log.info("Zone Id " + travelRequestPO.getZoneId());
		log.info("Selected Shift Time Id " + travelRequestPO.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = dateFormat.parse(travelRequestPO.getExecutionDate());
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//DateFormat requestDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String requestDate = requestDateFormat.format(date);
		//String requestDateAndTime = requestDate + " " + travelRequestPO.getTime();
		String shiftTime = travelRequestPO.getTime();
		Date shift = shiftFormate.parse(shiftTime);
		java.sql.Time dateShiftTime = new java.sql.Time(shift.getTime());
		List<EFmFmEmployeeTravelRequestPO> travelDetails = null;
				
		if (travelRequestPO.getZoneId() == 0) {			
			travelDetails = iCabRequestBO.assignCabByLocationFlg(requestDate,travelRequestPO.getCombinedFacility(),travelRequestPO.getTripType(),dateShiftTime);		
			if(!(travelDetails.isEmpty())){
				routeForAllocation(travelDetails,travelRequestPO,travelRequestPO.getBaseFacility());
			}			
			if(travelRequestPO.getTripType().equalsIgnoreCase("PICKUP")){
				travelDetails = iCabRequestBO.assignCabToPickupShiftOrDateEmployees(requestDate,travelRequestPO.getCombinedFacility(), travelRequestPO.getTripType(), dateShiftTime);
			}
			else{
				travelDetails = iCabRequestBO.assignCabToDropShiftOrDateEmployees(requestDate,travelRequestPO.getCombinedFacility(), travelRequestPO.getTripType(), dateShiftTime);
			}		
		} else {						
			travelDetails=iCabRequestBO.assignCabRequestToParticularShiftOrRouteEmployeesByLocationFlg(requestDate,travelRequestPO.getCombinedFacility(),travelRequestPO.getTripType(),dateShiftTime, travelRequestPO.getZoneId());
			if(!(travelDetails.isEmpty())){
				routeForAllocation(travelDetails,travelRequestPO,travelRequestPO.getBaseFacility());
			}			
			if(travelRequestPO.getTripType().equalsIgnoreCase("PICKUP")){
				travelDetails=iCabRequestBO.assignCabRequestToParticularShiftOrRouteEmployees(requestDate,travelRequestPO.getCombinedFacility(),travelRequestPO.getTripType(),dateShiftTime, travelRequestPO.getZoneId());		
			}
			else{
				travelDetails=iCabRequestBO.assignCabRequestToParticularShiftOrRouteEmployees(requestDate,travelRequestPO.getCombinedFacility(),travelRequestPO.getTripType(),dateShiftTime, travelRequestPO.getZoneId());		
			}
		}
	
		log.info("traveler count " + travelDetails.size());		
		routeForAllocation(travelDetails,travelRequestPO,travelRequestPO.getBaseFacility());
	
	}
	
	public void routeForAllocation(List<EFmFmEmployeeTravelRequestPO> travelDetails,EFmFmEmployeeTravelRequestPO travelRequestPO,int selectedBaseFacility)
			throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");		
		 	log.info("serviceStart -UserId :" + travelRequestPO.getUserId());
			DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
			log.info("Zone Id " + travelRequestPO.getZoneId());
			log.info("Selected Shift Time Id " + travelRequestPO.getTime());
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = dateFormat.parse(travelRequestPO.getExecutionDate());
			DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat requestDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			String requestDate = requestDateFormat.format(date);
			String requestDateAndTime = requestDate + " " + travelRequestPO.getTime();
			String shiftTime = travelRequestPO.getTime();
			Date shift = shiftFormate.parse(shiftTime);
			java.sql.Time dateShiftTime = new java.sql.Time(shift.getTime());
		if (!(travelDetails.isEmpty())) {
			outer: for (EFmFmEmployeeTravelRequestPO cabRequests : travelDetails) {
				List<EFmFmAssignRoutePO> assignRoutePO;
				long particularRouteEmplyees;				
				if(cabRequests.getLocationFlg()!=null){				
					particularRouteEmplyees = iCabRequestBO
							.assignCabCountToParticularShiftDateByLocationFlg(requestDate,travelRequestPO.getCombinedFacility(),
									travelRequestPO.getTripType(), dateShiftTime,
									cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),cabRequests.getLocationFlg());
				}else{
					particularRouteEmplyees = iCabRequestBO
							.assignCabCountToParticularShiftDateOrRouteEmployees(requestDate,travelRequestPO.getCombinedFacility(),
									travelRequestPO.getTripType(), dateShiftTime,
									cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
				}	           
		log.info("particularRouteEmplyees"+(int)particularRouteEmplyees);	
		if((int)particularRouteEmplyees!=0){				
				 log.info("travelRequestPO.getCombinedFacility()"+travelRequestPO.getCombinedFacility());
				List<EFmFmVehicleCheckInPO> allCheckInVehicles = null;				
				List<Integer> capacities=iCabRequestBO.getAllCapacitiesOfTheVehicles(travelRequestPO.getCombinedFacility());	
				if(!(capacities.isEmpty())){
		        if(capacities.contains((int)particularRouteEmplyees)){
                    allCheckInVehicles = iCabRequestBO.getAllCheckedInVehiclesForSpecificCapacity(travelRequestPO.getCombinedFacility(),(int)particularRouteEmplyees);
		        }
		        else{	
		        	log.info("capacities capacities"+capacities.size());
					allCheckInVehicles = iCabRequestBO.getAllDummyCheckedInVehiclesForGreatestCapacity(travelRequestPO.getCombinedFacility(),(int)particularRouteEmplyees);
		        }
               if(allCheckInVehicles.isEmpty()){
					allCheckInVehicles = iCabRequestBO.getAllDummyCheckedInVehiclesForLessCapacity(travelRequestPO.getCombinedFacility(),(int)particularRouteEmplyees);

               }				 
				 try{
				 if(allCheckInVehicles.isEmpty()){
					 log.info("travelRequestPO.getCombinedFacility()"+travelRequestPO.getCombinedFacility());
					 List<EFmFmVehicleCheckInPO> dummyVehicleDetails=iCabRequestBO.getAllCheckedInDummyVehicles(travelRequestPO.getCombinedFacility());
						log.info("allCheckInVehicles Details=" + dummyVehicleDetails.size());
					 if(!(dummyVehicleDetails.isEmpty())){
						 for(EFmFmVehicleCheckInPO checkInDetail:dummyVehicleDetails){
							 checkInDetail.setStatus("Y");
							 iVehicleCheckInBO.update(checkInDetail);
						 }
					 }					 
				        if(capacities.contains((int)particularRouteEmplyees)){
		                    allCheckInVehicles = iCabRequestBO.getAllCheckedInVehiclesForSpecificCapacity(travelRequestPO.getCombinedFacility(),(int)particularRouteEmplyees);
				        }
				        else{		        			        	
							allCheckInVehicles = iCabRequestBO.getAllDummyCheckedInVehiclesForGreatestCapacity(travelRequestPO.getCombinedFacility(),(int)particularRouteEmplyees);
				        }
		               if(allCheckInVehicles.isEmpty()){
							allCheckInVehicles = iCabRequestBO.getAllDummyCheckedInVehiclesForLessCapacity(travelRequestPO.getCombinedFacility(),(int)particularRouteEmplyees);

		               }			 
				 }
				 }catch(Exception e){
					 log.info("Error in updating the dummy checkin"+e);
				 }
				log.info("CheckIn Vehicle Details=" + (int)particularRouteEmplyees);
				log.info("allCheckInVehicles" + allCheckInVehicles.size());

				if (!allCheckInVehicles.isEmpty()) {
					EFmFmVehicleMasterPO updateVehicleStatus = iCabRequestBO
							.getVehicleDetail(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId());
					updateVehicleStatus.setStatus("A");
					iVehicleCheckInBO.update(updateVehicleStatus);
					EFmFmDriverMasterPO particularDriverDetails = approvalBO
							.getParticularDriverDetail(allCheckInVehicles.get(0).getEfmFmDriverMaster().getDriverId());
					particularDriverDetails.setStatus("A");
					approvalBO.update(particularDriverDetails);
					List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
							allCheckInVehicles.get(0).geteFmFmDeviceMaster().getDeviceId(),travelRequestPO.getCombinedFacility());
					deviceDetails.get(0).setStatus("Y");
					iVehicleCheckInBO.update(deviceDetails.get(0));
				}				
				if(cabRequests.getLocationFlg() !=null){
					assignRoutePO = iCabRequestBO.getHalfCompletedAssignRouteByLocationFlg(requestDate,cabRequests.geteFmFmClientBranchPO().getBranchId(),
							cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
							cabRequests.getTripType(), cabRequests.getShiftTime(),cabRequests.getLocationFlg());
				}else{
					assignRoutePO = iCabRequestBO.getHalfCompletedAssignRouteByDateWithNormalFlg(requestDate,cabRequests.geteFmFmClientBranchPO().getBranchId(),
							cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
							cabRequests.getTripType(), cabRequests.getShiftTime());
					
				}
				EFmFmEmployeeTripDetailPO employeeTripDetailPO = new EFmFmEmployeeTripDetailPO();
				EFmFmAssignRoutePO eAssignRoutePO = new EFmFmAssignRoutePO();
				log.info("checkInVehicleSize"+allCheckInVehicles.size());

				if (assignRoutePO.isEmpty() ||  !(assignRoutePO.get(0).geteFmFmRouteAreaMapping()
						.geteFmFmZoneMaster().getZoneName()
						.equalsIgnoreCase(cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName())) || (assignRoutePO.get(0).geteFmFmClientBranchPO().getBranchId() != cabRequests.geteFmFmClientBranchPO().getBranchId())) {
					if (!(allCheckInVehicles.isEmpty())) {
						EFmFmAssignRoutePO assignRoute = new EFmFmAssignRoutePO();
						assignRoute.setEfmFmVehicleCheckIn(allCheckInVehicles.get(0));
						EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
						routeAreaMapping.setRouteAreaId(cabRequests.geteFmFmRouteAreaMapping().getRouteAreaId());
						assignRoute.seteFmFmRouteAreaMapping(routeAreaMapping);
						assignRoute.setEscortRequredFlag("N");
						assignRoute.setAllocationMsg("N");
						assignRoute.setShiftTime(cabRequests.getShiftTime());
						assignRoute.setTripStatus("allocated");
						assignRoute.setLocationFlg("N");
						assignRoute.setTripConfirmationFromDriver("Not Delivered");
						assignRoute.setIsToll("No");
		                assignRoute.setRoutingCompletionStatus("Started");	
		                assignRoute.setSelectedBaseFacility(selectedBaseFacility);
		                assignRoute.setAssignedVendorName("NA"); 
						assignRoute.setIsBackTwoBack("N");
						assignRoute.setDistanceUpdationFlg("Y");
						assignRoute.setPlannedReadFlg("Y");
						assignRoute.setScheduleReadFlg("Y");
						assignRoute.setRemarksForEditingTravelledDistance("NO");
						assignRoute.setEditDistanceApproval("NO");
						if (cabRequests.getRequestType().equalsIgnoreCase("AdhocRequest")) {
							assignRoute.setRouteGenerationType("AdhocRequest");
						} else if (cabRequests.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName()
								.equalsIgnoreCase("default")) {
							assignRoute.setRouteGenerationType("normal");
						} else {
							assignRoute.setRouteGenerationType("nodal");
						}
						assignRoute.setCreatedDate(new Date());
						assignRoute.setTripAssignDate(requestDateTimeFormat.parse(requestDateAndTime));
						assignRoute.setTripUpdateTime(requestDateTimeFormat.parse(requestDateAndTime));
						assignRoute.setVehicleStatus("A");
						if(cabRequests.getLocationFlg() ==null){
								assignRoute.setLocationFlg("N");//normal Routes
						}else{			
							if(cabRequests.getLocationFlg().equalsIgnoreCase("M") || cabRequests.getLocationFlg().equalsIgnoreCase("S")){
								//assignRoute.setVehicleStatus("F");
								assignRoute.setLocationFlg(cabRequests.getLocationFlg());//Multiple destination routes								
								String locationAddingStatus=multiLocationAdd(cabRequests);								
							}else{
								assignRoute.setLocationFlg("N");
							}
						}						
						assignRoute.setBucketStatus("N");
						EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
						
						//base facility Id is required.
						eFmFmClientBranchPO
								.setBranchId(cabRequests.geteFmFmClientBranchPO().getBranchId());
						
						assignRoute.seteFmFmClientBranchPO(eFmFmClientBranchPO);
						assignRoute.setTripType(cabRequests.getTripType());
						allCheckInVehicles.get(0).setStatus("N");
						if (cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
								.equalsIgnoreCase("Always")
								|| cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
										.equalsIgnoreCase("femalepresent")
										&& cabRequests.getEfmFmUserMaster().getGender().equalsIgnoreCase("Female")) {
							List<EFmFmEscortCheckInPO> escortList = iCabRequestBO
									.getAllCheckedInEscort(travelRequestPO.getCombinedFacility());
							if (!escortList.isEmpty()) {
								EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
								checkInEscort.setEscortCheckInId(
										escortList.get(0).getEscortCheckInId());
								assignRoute.seteFmFmEscortCheckIn(checkInEscort);
								escortList.get(0).setStatus("N");
								iVehicleCheckInBO.update(escortList.get(0));
							}
							assignRoute.setEscortRequredFlag("Y");
						}
						iVehicleCheckInBO
								.update(allCheckInVehicles.get(0));
						iCabRequestBO.update(assignRoute);
						cabRequests.setRequestColor("yellow");
						cabRequests.setReadFlg("R");
						iCabRequestBO.update(cabRequests);

						assignRoutePO = iCabRequestBO.getHalfCompletedAssignRouteFromCheckInIdByDate(requestDate,cabRequests.geteFmFmClientBranchPO().getBranchId(),
								cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
								cabRequests.getTripType(), cabRequests.getShiftTime(),
								allCheckInVehicles.get(0).getCheckInId());

						EFmFmVehicleMasterPO vehicleMaster = iCabRequestBO
								.getVehicleDetail(assignRoutePO.get(0).getEfmFmVehicleCheckIn()
										.getEfmFmVehicleMaster().getVehicleId());
						vehicleMaster.setStatus("allocated");
						iVehicleCheckInBO.update(vehicleMaster);
						EFmFmDriverMasterPO particularDriverDetails = approvalBO.getParticularDriverDetail(
								assignRoutePO.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
						particularDriverDetails.setStatus("allocated");
						approvalBO.update(particularDriverDetails);

						List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
								assignRoutePO.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),travelRequestPO.getCombinedFacility());
						deviceDetails.get(0).setStatus("allocated");
						iVehicleCheckInBO.update(deviceDetails.get(0));
						eAssignRoutePO.setAssignRouteId(assignRoutePO.get(0).getAssignRouteId());
						if (assignRoutePO.get(0).getTripType().equalsIgnoreCase("DROP")) {
							employeeTripDetailPO.setTenMinuteMessageStatus("Y");
							employeeTripDetailPO.setTwoMinuteMessageStatus("Y");
							employeeTripDetailPO.setReachedFlg("Y");
							employeeTripDetailPO.setCabDelayMsgStatus("Y");
						} else {
							employeeTripDetailPO.setTenMinuteMessageStatus("N");
							employeeTripDetailPO.setTwoMinuteMessageStatus("N");
							employeeTripDetailPO.setReachedFlg("N");
							employeeTripDetailPO.setCabDelayMsgStatus("N");
						}
						employeeTripDetailPO.setActualTime(new Date());
						employeeTripDetailPO.setGoogleEta(0);
						employeeTripDetailPO.setBoardedFlg("N");
						employeeTripDetailPO.seteFmFmEmployeeTravelRequest(cabRequests);
						employeeTripDetailPO.setEfmFmAssignRoute(eAssignRoutePO);
						employeeTripDetailPO.setCurrenETA("0");
						employeeTripDetailPO.setEmployeeStatus("allocated");
						employeeTripDetailPO.setComingStatus("Yet to confirm");
						employeeTripDetailPO.setEmployeeOnboardStatus("NO");
						iCabRequestBO.update(employeeTripDetailPO);
					}
				} else {
					EFmFmVehicleMasterPO vehicleMaster = iCabRequestBO
							.getVehicleDetail(assignRoutePO.get(0).getEfmFmVehicleCheckIn()
									.getEfmFmVehicleMaster().getVehicleId());
					assignRoutePO.get(0).getAssignRouteId();
					List<EFmFmEmployeeTripDetailPO> allEmployees = iCabRequestBO.getParticularTripAllEmployees(
							assignRoutePO.get(0).getAssignRouteId());

					int availableCapacity = 0;
					availableCapacity = (vehicleMaster.getCapacity() - 1) - allEmployees.size();
					if (availableCapacity > 1
							&& assignRoutePO.get(0).geteFmFmClientBranchPO().getEscortRequired()
									.equalsIgnoreCase("femalepresent")
							&& cabRequests.getEfmFmUserMaster().getGender().equalsIgnoreCase("Female")
							&& assignRoutePO.get(0).getEscortRequredFlag()
									.equalsIgnoreCase("N")) {
						List<EFmFmEscortCheckInPO> escortList = iCabRequestBO.getAllCheckedInEscort(travelRequestPO.getCombinedFacility());
						if (!(escortList.isEmpty())) {
							EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
							checkInEscort.setEscortCheckInId(
									escortList.get(0).getEscortCheckInId());
							assignRoutePO.get(0).seteFmFmEscortCheckIn(checkInEscort);
							escortList.get(0).setStatus("N");
							iVehicleCheckInBO.update(escortList.get(0));
						}
						assignRoutePO.get(0).setEscortRequredFlag("Y");
						assignRoutePO.get(0).setVehicleStatus("F");
						iCabRequestBO.update(assignRoutePO.get(0));
						// availableCapacity = vehicleMaster.getAvailableSeat()
					}
					if (availableCapacity == 1
							&& assignRoutePO.get(0).geteFmFmClientBranchPO().getEscortRequired()
									.equalsIgnoreCase("femalepresent")
							&& cabRequests.getEfmFmUserMaster().getGender().equalsIgnoreCase("Female")
							&& assignRoutePO.get(0).getEscortRequredFlag().equalsIgnoreCase("N"))
						continue outer;
					if (cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
							.equalsIgnoreCase("firstlastfemale")
							&& cabRequests.geteFmFmEmployeeRequestMaster().getTripType().equalsIgnoreCase("PICKUP")
							&& availableCapacity == 1) {
						boolean requestAddingFlg = true;
						for (EFmFmAssignRoutePO assignRouteDetails : assignRoutePO) {
							EFmFmEmployeeTripDetailPO employeeTripDetail = new EFmFmEmployeeTripDetailPO();
							eAssignRoutePO.setAssignRouteId(assignRouteDetails.getAssignRouteId());
							if (assignRouteDetails.getTripType().equalsIgnoreCase("DROP")) {
								employeeTripDetail.setTenMinuteMessageStatus("Y");
								employeeTripDetail.setTwoMinuteMessageStatus("Y");
								employeeTripDetail.setReachedFlg("Y");
								employeeTripDetail.setCabDelayMsgStatus("Y");

							} else {
								employeeTripDetail.setTenMinuteMessageStatus("N");
								employeeTripDetail.setTwoMinuteMessageStatus("N");
								employeeTripDetail.setReachedFlg("N");
								employeeTripDetail.setCabDelayMsgStatus("N");

							}

							employeeTripDetail.setActualTime(new Date());
							employeeTripDetail.setGoogleEta(0);
							employeeTripDetail.setBoardedFlg("N");
							employeeTripDetail.setCurrenETA("0");
							employeeTripDetail.seteFmFmEmployeeTravelRequest(cabRequests);
							employeeTripDetail.setEfmFmAssignRoute(eAssignRoutePO);
							employeeTripDetail.setEmployeeStatus("allocated");
							employeeTripDetail.setComingStatus("Yet to confirm");
							employeeTripDetail.setEmployeeOnboardStatus("NO");

							iCabRequestBO.save(employeeTripDetail);
							if (allEmployees.get(0).geteFmFmEmployeeTravelRequest()
									.getEfmFmUserMaster().getGender().equalsIgnoreCase("Female")
									&& (allEmployees.get(0)
											.geteFmFmEmployeeTravelRequest().getShiftTime().getHours() >= 20
											|| allEmployees.get(0)
													.geteFmFmEmployeeTravelRequest().getShiftTime().getHours() == 0
											|| allEmployees.get(0)
													.geteFmFmEmployeeTravelRequest().getShiftTime().getHours() <= 7)) {
								List<EFmFmEmployeeTripDetailPO> tripId = iCabRequestBO
										.getRequestStatusFromBranchIdAndRequestId(travelRequestPO.getCombinedFacility(),
												cabRequests.getRequestId());
								iCabRequestBO.deleteParticularRequestFromEmployeeTripDetail(
										tripId.get(0).getEmpTripId());
							} else {
								assignRoutePO.get(0).setVehicleStatus("F");
								iCabRequestBO.update(assignRoutePO.get(0));
								iVehicleCheckInBO.update(vehicleMaster);
								cabRequests.setRequestColor("yellow");
								cabRequests.setReadFlg("R");
								iCabRequestBO.update(cabRequests);
								requestAddingFlg = false;
								continue outer;
							}
						}

						if (requestAddingFlg) {
							if ((int)particularRouteEmplyees <= 6)
								allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLessCapacity(travelRequestPO.getCombinedFacility(), 10);
							else
								allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLessCapacity(travelRequestPO.getCombinedFacility(), 20);
							if ((!allCheckInVehicles.isEmpty() && allCheckInVehicles.size() != 0 && !((int)particularRouteEmplyees==0))) {
								EFmFmAssignRoutePO assignRoute1 = new EFmFmAssignRoutePO();
								EFmFmVehicleCheckInPO vehicleCheckInPO = new EFmFmVehicleCheckInPO();
								vehicleCheckInPO.setCheckInId(
										allCheckInVehicles.get(0)
												.getCheckInId());
								assignRoute1.setEfmFmVehicleCheckIn(vehicleCheckInPO);
								EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
								routeAreaMapping
										.setRouteAreaId(cabRequests.geteFmFmRouteAreaMapping().getRouteAreaId());
								assignRoute1.seteFmFmRouteAreaMapping(routeAreaMapping);
								assignRoute1.setEscortRequredFlag("N");
								assignRoute1.setAllocationMsg("N");
								assignRoute1.setTripStatus("allocated");
								assignRoute1.setLocationFlg("N");
								assignRoute1.setTripConfirmationFromDriver("Not Delivered");

								assignRoute1.setIsToll("No");

				                assignRoute1.setRoutingCompletionStatus("Started");
				                assignRoute1.setSelectedBaseFacility(selectedBaseFacility);

				                assignRoute1.setAssignedVendorName("NA"); 

								assignRoute1.setIsBackTwoBack("N");
								assignRoute1.setDistanceUpdationFlg("Y");

								assignRoute1.setPlannedReadFlg("Y");
								assignRoute1.setScheduleReadFlg("Y");

								assignRoute1.setRemarksForEditingTravelledDistance("NO");
								assignRoute1.setEditDistanceApproval("NO");
								if (cabRequests.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName()
										.equalsIgnoreCase("default")) {
									assignRoute1.setRouteGenerationType("normal");
								} else {
									assignRoute1.setRouteGenerationType("nodal");
								}
								assignRoute1.setTripUpdateTime(requestDateFormat.parse(requestDate));
								assignRoute1.setCreatedDate(new Date());
								assignRoute1.setTripAssignDate(requestDateFormat.parse(requestDate));
								assignRoute1.setShiftTime(cabRequests.getShiftTime());
								assignRoute1.setVehicleStatus("A");
								if(cabRequests.getLocationFlg() ==null){
										assignRoute1.setLocationFlg("N");
								}else{			
									if(cabRequests.getLocationFlg().equalsIgnoreCase("M") || cabRequests.getLocationFlg().equalsIgnoreCase("S")){
										//assignRoute.setVehicleStatus("F");
										assignRoute1.setLocationFlg(cabRequests.getLocationFlg());//Multiple destination routes								
										String locationAddingStatus=multiLocationAdd(cabRequests);								
									}else{
										assignRoute1.setLocationFlg("N");
									}
								}
								
								assignRoute1.setBucketStatus("N");

								EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
								//base facility Id is required.
								eFmFmClientBranchPO.setBranchId(cabRequests.geteFmFmClientBranchPO().getBranchId());
								assignRoute1.seteFmFmClientBranchPO(eFmFmClientBranchPO);
								assignRoute1.setTripType(cabRequests.getTripType());
								allCheckInVehicles.get(0)
										.setStatus("N");
								if (cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
										.equalsIgnoreCase("Always")
										|| cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
												.equalsIgnoreCase("femalepresent")
												&& cabRequests.getEfmFmUserMaster().getGender()
														.equalsIgnoreCase("Female")) {
									List<EFmFmEscortCheckInPO> escortList = iCabRequestBO.getAllCheckedInEscort(travelRequestPO.getCombinedFacility());
									if (!escortList.isEmpty() || escortList.size() != 0) {
										EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
										checkInEscort.setEscortCheckInId(
												escortList.get(0).getEscortCheckInId());
										assignRoute1.seteFmFmEscortCheckIn(checkInEscort);
										escortList.get(0).setStatus("N");
										iVehicleCheckInBO.update(escortList.get(0));
									}
									assignRoute1.setEscortRequredFlag("Y");
								}

								iCabRequestBO.update(assignRoute1);
								iVehicleCheckInBO.update(
										allCheckInVehicles.get(0));
								cabRequests.setRequestColor("yellow");
								cabRequests.setReadFlg("R");
								iCabRequestBO.update(cabRequests);
								List<EFmFmAssignRoutePO> assignRoutePO1 = iCabRequestBO
										.getHalfCompletedAssignRouteFromCheckInIdByDate(requestDate,cabRequests.geteFmFmClientBranchPO().getBranchId(),
												cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
												cabRequests.getTripType(), cabRequests.getShiftTime(),
												allCheckInVehicles
														.get(0).getCheckInId());
								EFmFmVehicleMasterPO vehicleMaster1 = iCabRequestBO
										.getVehicleDetail(assignRoutePO1.get(0)
												.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
								// vehicleMaster1.setAvailableSeat(vehicleMaster1.getAvailableSeat()
								// - 1);
								vehicleMaster1.setStatus("allocated");
								iVehicleCheckInBO.update(vehicleMaster1);
								EFmFmDriverMasterPO particularDriverDetails1 = approvalBO
										.getParticularDriverDetail(assignRoutePO1.get(0).getEfmFmVehicleCheckIn()
												.getEfmFmDriverMaster().getDriverId());
								particularDriverDetails1.setStatus("allocated");
								approvalBO.update(particularDriverDetails1);
								List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO
										.getDeviceDetailsFromDeviceId(assignRoutePO1.get(0).getEfmFmVehicleCheckIn()
												.geteFmFmDeviceMaster().getDeviceId(), travelRequestPO.getCombinedFacility());
								deviceDetails.get(0).setStatus("allocated");
								iVehicleCheckInBO.update(deviceDetails.get(0));
								EFmFmEmployeeTripDetailPO employeeTripDetailPO1 = new EFmFmEmployeeTripDetailPO();
								eAssignRoutePO.setAssignRouteId(
										assignRoutePO1.get(0).getAssignRouteId());
								if (assignRoutePO1.get(0).getTripType()
										.equalsIgnoreCase("DROP")) {
									employeeTripDetailPO1.setTenMinuteMessageStatus("Y");
									employeeTripDetailPO1.setTwoMinuteMessageStatus("Y");
									employeeTripDetailPO1.setReachedFlg("Y");
									employeeTripDetailPO1.setCabDelayMsgStatus("Y");
								} else {
									employeeTripDetailPO1.setTenMinuteMessageStatus("N");
									employeeTripDetailPO1.setTwoMinuteMessageStatus("N");
									employeeTripDetailPO1.setReachedFlg("N");
									employeeTripDetailPO1.setCabDelayMsgStatus("N");

								}
								employeeTripDetailPO1.setActualTime(new Date());
								employeeTripDetailPO1.setGoogleEta(0);
								employeeTripDetailPO1.setBoardedFlg("N");
								employeeTripDetailPO1.seteFmFmEmployeeTravelRequest(cabRequests);
								employeeTripDetailPO1.setEfmFmAssignRoute(eAssignRoutePO);
								employeeTripDetailPO1.setEmployeeStatus("allocated");
								employeeTripDetailPO1.setComingStatus("Yet to confirm");
								employeeTripDetailPO1.setEmployeeOnboardStatus("NO");

								employeeTripDetailPO1.setCurrenETA("0");
								iCabRequestBO.update(employeeTripDetailPO1);
								continue outer;
							} else {
								continue outer;
							}
						}
					}
					if (cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
							.equalsIgnoreCase("firstlastfemale")
							&& cabRequests.geteFmFmEmployeeRequestMaster().getTripType().equalsIgnoreCase("DROP")
							&& availableCapacity == 1) {
						boolean requestAddingFlg = true;
						for (EFmFmAssignRoutePO assignRouteDetails : assignRoutePO) {
							EFmFmEmployeeTripDetailPO employeeTripDetail = new EFmFmEmployeeTripDetailPO();
							eAssignRoutePO.setAssignRouteId(assignRouteDetails.getAssignRouteId());
							if (assignRouteDetails.getTripType().equalsIgnoreCase("DROP")) {
								employeeTripDetail.setTenMinuteMessageStatus("Y");
								employeeTripDetail.setTwoMinuteMessageStatus("Y");
								employeeTripDetail.setReachedFlg("Y");
								employeeTripDetail.setCabDelayMsgStatus("Y");

							} else {
								employeeTripDetail.setTenMinuteMessageStatus("N");
								employeeTripDetail.setTwoMinuteMessageStatus("N");
								employeeTripDetail.setReachedFlg("N");
								employeeTripDetail.setCabDelayMsgStatus("N");
							}
							employeeTripDetail.setActualTime(new Date());
							employeeTripDetail.setGoogleEta(0);
							employeeTripDetail.setBoardedFlg("N");
							employeeTripDetail.setCurrenETA("0");
							employeeTripDetail.seteFmFmEmployeeTravelRequest(cabRequests);
							employeeTripDetail.setEfmFmAssignRoute(eAssignRoutePO);
							employeeTripDetail.setEmployeeStatus("allocated");
							employeeTripDetail.setComingStatus("Yet to confirm");
							employeeTripDetail.setEmployeeOnboardStatus("NO");

							iCabRequestBO.save(employeeTripDetail);
							List<EFmFmEmployeeTripDetailPO> allSortedEmployees = iCabRequestBO
									.getDropTripAllSortedEmployees(assignRouteDetails.getAssignRouteId());
							if (allSortedEmployees.get(allSortedEmployees.size() - 1)
									.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()
									.equalsIgnoreCase("Female")
									&& (allSortedEmployees
											.get(allSortedEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
													.getShiftTime().getHours() >= 19
											|| allSortedEmployees
													.get(allSortedEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
															.getShiftTime().getHours() == 0
											|| allSortedEmployees
													.get(allSortedEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
															.getShiftTime().getHours() < 7)) {
								List<EFmFmEmployeeTripDetailPO> tripId = iCabRequestBO
										.getRequestStatusFromBranchIdAndRequestId(travelRequestPO.getCombinedFacility(),cabRequests.getRequestId());
								iCabRequestBO.deleteParticularRequestFromEmployeeTripDetail(
										tripId.get(0).getEmpTripId());
							} else {
								assignRoutePO.get(0).setVehicleStatus("F");
								iCabRequestBO.update(assignRoutePO.get(0));
								iVehicleCheckInBO.update(vehicleMaster);
								cabRequests.setRequestColor("yellow");
								cabRequests.setReadFlg("R");
								iCabRequestBO.update(cabRequests);
								requestAddingFlg = false;
								continue outer;
							}
						}
						if (requestAddingFlg) {
							if ((int)particularRouteEmplyees <= 6)
								allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLessCapacity(travelRequestPO.getCombinedFacility(), 10);
							else
								allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLessCapacity(travelRequestPO.getCombinedFacility(), 20);
							if (!allCheckInVehicles.isEmpty() && allCheckInVehicles.size() != 0 && !((int)particularRouteEmplyees==0)) {
								EFmFmAssignRoutePO assignRoute1 = new EFmFmAssignRoutePO();

								EFmFmVehicleCheckInPO vehicleCheckInPO = new EFmFmVehicleCheckInPO();
								vehicleCheckInPO.setCheckInId(
										allCheckInVehicles.get(0)
												.getCheckInId());
								assignRoute1.setEfmFmVehicleCheckIn(vehicleCheckInPO);
								EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
								routeAreaMapping
										.setRouteAreaId(cabRequests.geteFmFmRouteAreaMapping().getRouteAreaId());
								assignRoute1.seteFmFmRouteAreaMapping(routeAreaMapping);
								assignRoute1.setEscortRequredFlag("N");
								assignRoute1.setAllocationMsg("N");
								assignRoute1.setTripStatus("allocated");
								assignRoute1.setLocationFlg("N");
								assignRoute1.setTripConfirmationFromDriver("Not Delivered");

								assignRoute1.setIsToll("No");

				                assignRoute1.setRoutingCompletionStatus("Started");
				                assignRoute1.setSelectedBaseFacility(selectedBaseFacility);

				                assignRoute1.setAssignedVendorName("NA"); 
								assignRoute1.setIsBackTwoBack("N");
								assignRoute1.setDistanceUpdationFlg("Y");

								assignRoute1.setPlannedReadFlg("Y");
								assignRoute1.setScheduleReadFlg("Y");

								assignRoute1.setRemarksForEditingTravelledDistance("NO");
								assignRoute1.setEditDistanceApproval("NO");
								if (cabRequests.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName()
										.equalsIgnoreCase("default")) {
									assignRoute1.setRouteGenerationType("normal");
								} else {
									assignRoute1.setRouteGenerationType("nodal");
								}

								assignRoute1.setTripAssignDate(requestDateFormat.parse(requestDate));
								assignRoute1.setTripUpdateTime(requestDateFormat.parse(requestDate));
								assignRoute1.setCreatedDate(new Date());
								assignRoute1.setShiftTime(cabRequests.getShiftTime());
								assignRoute1.setVehicleStatus("A");
								if(cabRequests.getLocationFlg() ==null){
									assignRoute1.setLocationFlg("N");
								}else{			
									if(cabRequests.getLocationFlg().equalsIgnoreCase("M") || cabRequests.getLocationFlg().equalsIgnoreCase("S")){
										//assignRoute.setVehicleStatus("F");
										assignRoute1.setLocationFlg(cabRequests.getLocationFlg());//Multiple destination routes								
										String locationAddingStatus=multiLocationAdd(cabRequests);								
									}else{
										assignRoute1.setLocationFlg("N");
									}
								}								
								assignRoute1.setBucketStatus("N");

								EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
								//base facility Id is required.
								eFmFmClientBranchPO.setBranchId(cabRequests.geteFmFmClientBranchPO().getBranchId());
								assignRoute1.seteFmFmClientBranchPO(eFmFmClientBranchPO);
								assignRoute1.setTripType(cabRequests.getTripType());
								allCheckInVehicles.get(0)
										.setStatus("N");
								if (cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
										.equalsIgnoreCase("Always")
										|| cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
												.equalsIgnoreCase("femalepresent")
												&& cabRequests.getEfmFmUserMaster().getGender()
														.equalsIgnoreCase("Female")) {
									List<EFmFmEscortCheckInPO> escortList = iCabRequestBO.getAllCheckedInEscort(travelRequestPO.getCombinedFacility());
									if (!escortList.isEmpty() || escortList.size() != 0) {
										EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
										checkInEscort.setEscortCheckInId(
												escortList.get(0).getEscortCheckInId());
										assignRoute1.seteFmFmEscortCheckIn(checkInEscort);
										escortList.get(0).setStatus("N");
										iVehicleCheckInBO.update(escortList.get(0));
									}
									assignRoute1.setEscortRequredFlag("Y");
								}
								iCabRequestBO.update(assignRoute1);
								iVehicleCheckInBO.update(
										allCheckInVehicles.get(0));
								cabRequests.setRequestColor("yellow");
								cabRequests.setReadFlg("R");
								iCabRequestBO.update(cabRequests);
								List<EFmFmAssignRoutePO> assignRoutePO1 = iCabRequestBO
										.getHalfCompletedAssignRouteFromCheckInIdByDate(requestDate,cabRequests.geteFmFmClientBranchPO().getBranchId(),
												cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
												cabRequests.getTripType(), cabRequests.getShiftTime(),
												allCheckInVehicles
														.get(0).getCheckInId());
								EFmFmVehicleMasterPO vehicleMaster1 = iCabRequestBO
										.getVehicleDetail(assignRoutePO1.get(0)
												.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
								vehicleMaster1.setStatus("allocated");
								iVehicleCheckInBO.update(vehicleMaster1);
								EFmFmDriverMasterPO particularDriverDetails1 = approvalBO
										.getParticularDriverDetail(assignRoutePO1.get(0).getEfmFmVehicleCheckIn()
												.getEfmFmDriverMaster().getDriverId());
								particularDriverDetails1.setStatus("allocated");
								approvalBO.update(particularDriverDetails1);
								List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO
										.getDeviceDetailsFromDeviceId(assignRoutePO1.get(0).getEfmFmVehicleCheckIn()
												.geteFmFmDeviceMaster().getDeviceId(), travelRequestPO.getCombinedFacility());
								deviceDetails.get(0).setStatus("allocated");
								iVehicleCheckInBO.update(deviceDetails.get(0));

								EFmFmEmployeeTripDetailPO employeeTripDetailPO1 = new EFmFmEmployeeTripDetailPO();
								eAssignRoutePO.setAssignRouteId(
										assignRoutePO1.get(0).getAssignRouteId());
								if (assignRoutePO1.get(0).getTripType()
										.equalsIgnoreCase("DROP")) {
									employeeTripDetailPO1.setTenMinuteMessageStatus("Y");
									employeeTripDetailPO1.setTwoMinuteMessageStatus("Y");
									employeeTripDetailPO1.setReachedFlg("Y");
									employeeTripDetailPO1.setCabDelayMsgStatus("Y");
								} else {
									employeeTripDetailPO1.setTenMinuteMessageStatus("N");
									employeeTripDetailPO1.setTwoMinuteMessageStatus("N");
									employeeTripDetailPO1.setReachedFlg("N");
									employeeTripDetailPO1.setCabDelayMsgStatus("N");
								}
								employeeTripDetailPO1.setActualTime(new Date());
								employeeTripDetailPO1.setGoogleEta(0);
								employeeTripDetailPO1.setBoardedFlg("N");
								employeeTripDetailPO1.seteFmFmEmployeeTravelRequest(cabRequests);
								employeeTripDetailPO1.setEfmFmAssignRoute(eAssignRoutePO);
								employeeTripDetailPO1.setEmployeeStatus("allocated");
								employeeTripDetailPO1.setComingStatus("Yet to confirm");
								employeeTripDetailPO1.setEmployeeOnboardStatus("NO");

								employeeTripDetailPO1.setCurrenETA("0");
								iCabRequestBO.update(employeeTripDetailPO1);
								continue outer;
							}
							continue outer;
						}
					}
					if (availableCapacity == 1) {
						assignRoutePO.get(0).setVehicleStatus("F");
						iCabRequestBO.update(assignRoutePO.get(0));
					}
					// vehicleMaster.setAvailableSeat(availableCapacity);
					iVehicleCheckInBO.update(vehicleMaster);
					cabRequests.setRequestColor("yellow");
					cabRequests.setReadFlg("R");
					iCabRequestBO.update(cabRequests);
					eAssignRoutePO.setAssignRouteId(assignRoutePO.get(0).getAssignRouteId());
					if (assignRoutePO.get(0).getTripType().equalsIgnoreCase("DROP")) {
						employeeTripDetailPO.setTenMinuteMessageStatus("Y");
						employeeTripDetailPO.setTwoMinuteMessageStatus("Y");
						employeeTripDetailPO.setReachedFlg("Y");
						employeeTripDetailPO.setCabDelayMsgStatus("Y");

					} else {
						employeeTripDetailPO.setTenMinuteMessageStatus("N");
						employeeTripDetailPO.setTwoMinuteMessageStatus("N");
						employeeTripDetailPO.setReachedFlg("N");
						employeeTripDetailPO.setCabDelayMsgStatus("N");
					}
					employeeTripDetailPO.setActualTime(new Date());
					employeeTripDetailPO.setGoogleEta(0);
					employeeTripDetailPO.setBoardedFlg("N");
					employeeTripDetailPO.setCurrenETA("0");
					employeeTripDetailPO.seteFmFmEmployeeTravelRequest(cabRequests);
					employeeTripDetailPO.setEfmFmAssignRoute(eAssignRoutePO);
					employeeTripDetailPO.setEmployeeStatus("allocated");
					employeeTripDetailPO.setComingStatus("Yet to confirm");
					employeeTripDetailPO.setEmployeeOnboardStatus("NO");
					iCabRequestBO.save(employeeTripDetailPO);
				}
			}
			}
			}
		}
	}
	
	
	
	public String multiLocationAdd(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO){		
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		String response="AlreadyExist";		
		List<EFmFmMultipleLocationTvlReqPO> locationTvlReq=iCabRequestBO.getLocationTravelDetailsByRequestId(eFmFmEmployeeTravelRequestPO.getRequestId());
		if(locationTvlReq.isEmpty()){
			try {
					if(!(eFmFmEmployeeTravelRequestPO.getLocationWaypointsIds().replace(",","").isEmpty())){
						String[] locationArray=eFmFmEmployeeTravelRequestPO.getLocationWaypointsIds().split(",");
						for(String locationId:locationArray){
							if(!(locationId.trim().isEmpty())){					
								EFmFmMultipleLocationTvlReqPO eFmFmMultipleLocationTvlReqPO=new EFmFmMultipleLocationTvlReqPO();
								//eFmFmEmployeeTravelRequestPO.setRequestId(requestId);
								eFmFmMultipleLocationTvlReqPO.seteFmFmEmployeeTravelRequest(eFmFmEmployeeTravelRequestPO);
								EFmFmLocationMasterPO eFmFmLocationMasterPO=new EFmFmLocationMasterPO();
								eFmFmLocationMasterPO.setLocationId(Integer.parseInt(locationId));
								eFmFmMultipleLocationTvlReqPO.seteFmFmLocationMaster(eFmFmLocationMasterPO);
								eFmFmMultipleLocationTvlReqPO.setRequestUpdateDate(new Date());
								eFmFmMultipleLocationTvlReqPO.setLocationStatus("Y");
								eFmFmMultipleLocationTvlReqPO.setTravelledStatus("allocated");
								iCabRequestBO.save(eFmFmMultipleLocationTvlReqPO);
								response="success";
							}
						}			
					}else{
						response="INVALIDLOCATIONWAYPOINTS";
					}
			} catch (Exception e) {
				log.debug("log"+e);
			}	
		}	
		return response;	
	}	

/*	@POST
	@Path("/algoCaballocation")
	@Consumes("application/json")
	@Produces("application/json")
	public Response algoCaballocation(EFmFmEmployeeTravelRequestPO travelRequestPO) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
		 	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken")))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authenticationToken error"+e);
		 	}
		 log.info("serviceStart -UserId :" + travelRequestPO.getUserId());
		String shiftTime = travelRequestPO.getTime();
		Date shift = shiftFormate.parse(shiftTime);
		log.info("inova" + travelRequestPO.getInnovaCount());
		log.info("tempo" + travelRequestPO.getTempoCount());
		log.info("excution" + travelRequestPO.getTypeExecution());
		log.info("Date:" + travelRequestPO.getExecutionDate());
		log.info("employeeCount:" + travelRequestPO.getEmployeeCount());
		DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = inputFormat.parse(travelRequestPO.getExecutionDate());
		String convertedCurrentDate = outputFormat.format(date);

		java.sql.Time dateShiftTime = new java.sql.Time(shift.getTime());
		ArrayList<JsonEmployee> employees_roster_list = new ArrayList<JsonEmployee>();
		travelRequestPO.setShiftTime(dateShiftTime);
		List<EFmFmEmployeeTravelRequestPO> travelDetails = iCabRequestBO.listOfTravelRequestByShiftTripType(
				travelRequestPO.getBranchId(), dateShiftTime, travelRequestPO.getTripType(), convertedCurrentDate);
		if (travelDetails.size() > 0) {
			for (EFmFmEmployeeTravelRequestPO employeeReqDetails : travelDetails) {
				JsonGeocode pickup = new JsonGeocode(employeeReqDetails.getEfmFmUserMaster().getLatitudeLongitude());
				JsonGeocode drop = new JsonGeocode(
						employeeReqDetails.getEfmFmUserMaster().geteFmFmClientBranchPO().getLatitudeLongitude());
				employees_roster_list.add(
						new JsonEmployee(employeeReqDetails.getRequestId() + "", travelRequestPO.getBranchId() + "",
								pickup, drop, employeeReqDetails.getEfmFmUserMaster().getGender()));
			}
		}
		ArrayList<JsonDepot> depots = new ArrayList<JsonDepot>();
		ArrayList<JsonVehicleType> vehicleTypes = new ArrayList<JsonVehicleType>();
		List<EFmFmClientBranchPO> branchDetails = iCabRequestBO.getBranchDetails(travelRequestPO.getBranchId());
		if (branchDetails.size() > 0) {
			depots.add(new JsonDepot(travelRequestPO.getBranchId() + "", branchDetails.get(0).getBranchName(),
					new JsonGeocode(branchDetails.get(0).getLatitudeLongitude())));
		}
		EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
		eFmFmVehicleCheckInPO.setBranchId(travelRequestPO.getBranchId());
		List<EFmFmVehicleMasterPO> vehicleDetails = iVehicleCheckInBO.getAllVehicleModel(travelRequestPO.getBranchId());
		if (vehicleDetails.size() > 0) {
			for (EFmFmVehicleMasterPO vehicleModelList : vehicleDetails) {
				JsonVehicleType V = new JsonVehicleType(vehicleModelList.getVehicleNumber(),
						vehicleModelList.getVehicleModel(), (vehicleModelList.getCapacity() - 1));
				vehicleTypes.add(V);
				if (V.capacity < 6) {
					depots.get(0).add_vehicle(V, travelRequestPO.getInnovaCount());
				} else {
					depots.get(0).add_vehicle(V, travelRequestPO.getTempoCount());
				}
			}
		}

		java.sql.Time startTime = java.sql.Time.valueOf("07:00:00");
		java.sql.Time Endtime = java.sql.Time.valueOf("19:00:00");
		ArrayList<JsonVehicle> vehicles = new ArrayList<JsonVehicle>();
		log.info("JsonProblem Constractor in allocation employees_roster_list" + employees_roster_list.toString()
				+ "depots.." + depots.toString() + "vehicleTypes" + vehicleTypes.toString() + "vehicles"
				+ vehicles.toString());
		JsonProblem problem = new JsonProblem(employees_roster_list, depots, vehicleTypes, vehicles);

		// settings for algorithm, make changes here for now to use different
		// constraints
		problem.settings.shift_time.setHours(shift.getHours());
		problem.settings.shift_time.setMinutes(shift.getMinutes());
		problem.settings.shift_time.setSeconds(0);
		problem.settings.use_api = true;

		if (travelDetails.size() > 0) {

			problem.settings.max_radial_distance = 1000 * travelDetails.get(0).getEfmFmUserMaster()
					.geteFmFmClientBranchPO().getMaxRadialDistanceEmployeeWiseInKm();

			problem.settings.max_route_length = 1000
					* travelDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getMaxRouteLengthInKm();

			problem.settings.max_travel_time = 60 * travelDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
					.getMaxTravelTimeEmployeeWiseInMin();

			problem.settings.shift_buffer_time = 300;// field Required

			problem.settings.max_cluster_size = 15; // field Name need to change

			problem.settings.employee_service_time = 60
					* travelDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getEmployeeWaitingTime(); // required

			problem.settings.max_employee_deviation = 1000
					* travelDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getMaxRouteDeviationInKm();

			if (travelDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getAutoClustering()
					.equalsIgnoreCase("Disable")) {

				problem.settings.auto_clustering = false;

			} else {
				problem.settings.auto_clustering = true;
			}

		}

		if (startTime.getTime() >= dateShiftTime.getTime() || Endtime.getTime() <= dateShiftTime.getTime()) {

			problem.settings.escorts = true;
		} else {
			problem.settings.escorts = false;
		}

		if (travelRequestPO.getTripType().equalsIgnoreCase("PICKUP")) {
			problem.settings.trip_type = "login";
		} else {
			problem.settings.trip_type = "logout";
		}
		log.info("Calling problem.startSolve" + problem.toString());
		problem.startSolve();
		log.info("after problem.startSolve" + problem.toString());
		 log.info("serviceEnd -UserId :" + travelRequestPO.getUserId());
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	}*/

	/*public long getDisableTime(int hours, int minutes, Date checkIndate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkIndate);
		calendar.add(Calendar.HOUR, hours);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTimeInMillis();
	}*/
	
	public long getDisableTime(Time hours, int minutes, Date checkIndate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkIndate);
		calendar.add(Calendar.HOUR, hours.getHours());
		calendar.add(Calendar.MINUTE, hours.getMinutes());
		return calendar.getTimeInMillis();
	}
}
