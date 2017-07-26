package com.newtglobal.eFmFmFleet.services;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
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

import com.newtglobal.eFmFmFleet.business.bo.IAlertBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IEmployeeDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmCheckInVehicleTrackingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlertTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLiveRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/view")
@Consumes("application/json")
@Produces("application/json")
public class ViewMapService {
	private static Log log = LogFactory.getLog(ViewMapService.class);
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();


	@POST
	@Path("/livetrips")
	public Response allLiveTrips(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");	

		Map<String, Object> responce = new HashMap<String, Object>();				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		
		assignRoutePO.setTripAssignDate(new Date());
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		long dbQueryTime = new Date().getTime();
		log.info("****** livetrip Data START Time" + dbQueryTime);
		log.info("ShiftTime" + assignRoutePO.getTime());
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

		Date shift;
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		try {
			String shiftDate = assignRoutePO.getTime();
			shift = shiftFormate.parse(shiftDate);
		} catch (Exception e) {
			String shiftDate = "00:00";
			shift = shiftFormate.parse(shiftDate);
		}
		List<Map<String, Object>> tripAlerts = new ArrayList<Map<String, Object>>();			

		java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
		List<EFmFmAssignRoutePO> assignRoutes = assignRouteBO.getAllLiveTripsByTripypeDateAndShiftTime(
				assignRoutePO.getCombinedFacility(), assignRoutePO.getTripType(), shiftTime);
		long dbQueryEndTime = new Date().getTime();
		Map<String, Object> mapAlerts = new HashMap<String, Object>();

		long dbTime = dbQueryEndTime - dbQueryTime;
		log.info("****** livetrip Data Query End Time: " + dbTime);
		log.info("dataSize: " + assignRoutes.size());
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		if (!(assignRoutes.isEmpty())) {
			//Get the routes feedbacks given by the Employees
			//Last 30 days alerts
			try{
	    	Date fromDate=new Date(minusDayFromDate(new Date(),-30));
			List<EFmFmTripAlertsPO> alertDetail=iAlertBO.getAllTripTypeAndShiftAlertsAndFeedbacks(fromDate, new Date(),assignRoutePO.getCombinedFacility(),assignRoutePO.getTripType(),shiftTime,"tracking");
			log.info("total alerts"+alertDetail.size());
			if(!alertDetail.isEmpty()){
				for(EFmFmTripAlertsPO alerts:alertDetail){
					List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(alerts.getUserId());					
					List<EFmFmAlertTypeMasterPO> alertType=iAlertBO.getlertDetailFromAlertIds(alerts.getEfmFmAlertTypeMaster().getAlertId());						
					 Map<String, Object> alert = new HashMap<String, Object>();						 
					 alert.put("alertId", alerts.getTripAlertsId());
					 alert.put("alertStatus", alerts.getAlertOpenStatus());
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
					 
					 tripAlerts.add(alert);
				}
				
			}
			}catch(Exception e){
				log.info("Error"+e);
			}
			mapAlerts.put("feedbacks", tripAlerts);
			allRoutes.add(mapAlerts);
			for (EFmFmAssignRoutePO assignRoute : assignRoutes) {
				StringBuffer waypoints = new StringBuffer();
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO
						.getParticularTripAllEmployees(assignRoute.getAssignRouteId());
				Map<String, Object> requests = new HashMap<String, Object>();
				if (!(employeeTripDetailPO.isEmpty())) {
					for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
						String wayPointsAdhocRequest = "";
						if (assignRoute.getRouteGenerationType().equalsIgnoreCase("AdhocRequest")) {
							wayPointsAdhocRequest = employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmEmployeeRequestMaster().getOriginLattitudeLongitude() + "|";
							if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
									.getDestination1AddressLattitudeLongitude().equalsIgnoreCase("N")) {
								wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
										.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
										.getDestination1AddressLattitudeLongitude() + "|";
								if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
										.getDestination2AddressLattitudeLongitude().equalsIgnoreCase("N")) {
									wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
											.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getDestination2AddressLattitudeLongitude() + "|";
									if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmEmployeeRequestMaster().getDestination3AddressLattitudeLongitude()
											.equalsIgnoreCase("N")) {
										wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
												.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination3AddressLattitudeLongitude() + "|";
										if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmEmployeeRequestMaster()
												.getDestination4AddressLattitudeLongitude().equalsIgnoreCase("N")) {
											wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
													.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
													.getDestination4AddressLattitudeLongitude() + "|";
											if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
													.geteFmFmEmployeeRequestMaster()
													.getDestination5AddressLattitudeLongitude().equalsIgnoreCase("N")) {
												wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
														.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
														.getDestination5AddressLattitudeLongitude() + "|";
											}
										}
									}
								}
							}
							waypoints.append(wayPointsAdhocRequest + employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmEmployeeRequestMaster().getEndDestinationAddressLattitudeLongitude()
									+ "|");

						} else if (assignRoute.getRouteGenerationType().contains("nodal")) {
							if(!(waypoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()))){
								waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints() + "|");	
							}
						} else {
							waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
									.getLatitudeLongitude() + "|");
						}
						
						try {
							if (assignRoute.getLocationFlg().equalsIgnoreCase("M")) {
								waypoints.setLength(0);			
								Map<String,Object> waypointsList=new CabRequestService().multilocationlistOfWayPointsAfterAllocated(employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestId(),
										employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getUserId(),assignRoutePO.getCombinedFacility());
								
								if(!(waypointsList.isEmpty())){									
									for( Map.Entry<String,Object> listofValues:waypointsList.entrySet()){
										if(listofValues.getKey().equalsIgnoreCase("wayPointsList")){
											waypoints.append(listofValues.getValue());
										}else{
											requests.put("waypointsList",listofValues.getValue());
										}
										
									}				
								}				
							}
						} catch (Exception e) {
							log.debug("multilocation Value not updated");
						}
						
					}
					if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoute.getNodalPoints());
					}

				} else {
					if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoute.getNodalPoints());
						log.info("nodalAdhoc" + waypoints);
					}
				}
				requests.put("baseLatLong", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
				requests.put("escortStatus", assignRoute.getEscortRequredFlag());
				requests.put("branchName", assignRoute.geteFmFmClientBranchPO().getBranchName());

				requests.put("routeId", assignRoute.getAssignRouteId());
				requests.put("locationFlg",assignRoute.getLocationFlg());
				requests.put("shiftTime", formatter.format(assignRoute.getShiftTime()));
				requests.put("tripType", assignRoute.getTripType());
				requests.put("driverName", assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				requests.put("driverNumber",
						assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				requests.put("vehicleNumber",
						assignRoute.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				requests.put("zoneName", assignRoute.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				requests.put("numberOfEmployees", employeeTripDetailPO.size());
				requests.put("status", assignRoute.getTripStatus());
				requests.put("waypoints", waypoints);
				String speed = "";
				DecimalFormat decimalFormat = new DecimalFormat("0.#");
				List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
						.getRouteLastEtaAndDistanceFromAssignRouteId(assignRoute.geteFmFmClientBranchPO().getBranchId(),
								assignRoute.getAssignRouteId());
				if (!(actualRouteTravelled.isEmpty())) {
					if (actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation() == null) {
						List<EFmFmLiveRoutTravelledPO> travelledEta = assignRouteBO
								.getRouteLastEtaAndDistanceFromAssignRouteId(
										assignRoute.geteFmFmClientBranchPO().getBranchId(),
										assignRoute.getAssignRouteId());
						if (!(travelledEta.isEmpty())) {
							requests.put("ExpectedTime", travelledEta.get(travelledEta.size() - 1).getLiveEta());
							requests.put("currentCabLatiLongi", actualRouteTravelled
									.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
							requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
									.getLiveCurrentCabLocation());
							try {
								float speedInput = Float.parseFloat(
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
								speed = decimalFormat.format(speedInput);
							} catch (Exception e) {
							}
							requests.put("speed", speed);
						} else {
							requests.put("ExpectedTime", "calculating...");
							requests.put("currentCabLatiLongi", actualRouteTravelled
									.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
							requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
									.getLiveCurrentCabLocation());

							try {
								float speedInput = Float.parseFloat(
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
								speed = decimalFormat.format(speedInput);
							} catch (Exception e) {
							}
							requests.put("speed", speed);
						}
					} else {
						requests.put("ExpectedTime",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta());
						requests.put("currentCabLatiLongi",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
						requests.put("currentCabLocation",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation());
						try {
							float speedInput = Float.parseFloat(
									actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
							speed = decimalFormat.format(speedInput);
						} catch (Exception e) {
							log.info("error" + e);
						}
						requests.put("speed", speed);
					}
				} else {
					requests.put("ExpectedTime", "calculating...");
					requests.put("speed", 0);
					requests.put("currentCabLatiLongi", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
					requests.put("currentCabLocation", assignRoute.geteFmFmClientBranchPO().getAddress());

				}
				allRoutes.add(requests);
			}
		}		
		long finalResponceTime = new Date().getTime() - dbQueryTime;
		log.info("****** finalResponceTime Time" + finalResponceTime);
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/checkInVehicleTrack")
	public Response checkInVehicleTracking(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		List<Map<String, Object>> listOfVehicle = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleCheckInPO> listOfCheckedInVehicle = iVehicleCheckInBO
				.getCheckedInVehicleDetails(assignRoutePO.getCombinedFacility(), new Date());
		
		if (!(listOfCheckedInVehicle.isEmpty())) {
			for (EFmFmVehicleCheckInPO vehicleDetails : listOfCheckedInVehicle) {
				List<EFmCheckInVehicleTrackingPO> trackVehicleDetails = assignRouteBO
						.vehicleTrackingAfterCheckIn(assignRoutePO.getCombinedFacility(), vehicleDetails.getCheckInId());
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				if (!(trackVehicleDetails.isEmpty())) {
					vehicleList.put("checkInVehicleTrackingId",
							trackVehicleDetails.get(trackVehicleDetails.size() - 1).getCheckInVehicleTrackingId());
					vehicleList.put("lattiLongi",
							trackVehicleDetails.get(trackVehicleDetails.size() - 1).getLatitudeLongitude());
					vehicleList.put("cabLocation", trackVehicleDetails.get(trackVehicleDetails.size() - 1).getCurrentCabLocation());
					vehicleList.put("vehicleSpeed", trackVehicleDetails.get(trackVehicleDetails.size() - 1).getSpeed());
					vehicleList.put("currentTime", dateFormatter
							.format(trackVehicleDetails.get(trackVehicleDetails.size() - 1).getCurrentTime()));
				}
				vehicleList.put("checkInId", vehicleDetails.getCheckInId());
				vehicleList.put("branchName", vehicleDetails.getEfmFmDriverMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

				vehicleList.put("driverName", vehicleDetails.getEfmFmDriverMaster().getFirstName());
				vehicleList.put("driverNumber", vehicleDetails.getEfmFmDriverMaster().getMobileNumber());
				vehicleList.put("driverId", vehicleDetails.getEfmFmDriverMaster().getDriverId());
				vehicleList.put("DriverName", vehicleDetails.getEfmFmDriverMaster().getFirstName());
				vehicleList.put("MobileNumber", vehicleDetails.getEfmFmDriverMaster().getMobileNumber());
				vehicleList.put("deviceNumber", vehicleDetails.geteFmFmDeviceMaster().getMobileNumber());
				vehicleList.put("deviceId", vehicleDetails.geteFmFmDeviceMaster().getDeviceId());
				vehicleList.put("capacity", vehicleDetails.getEfmFmVehicleMaster().getCapacity());
				vehicleList.put("vehicleNumber", vehicleDetails.getEfmFmVehicleMaster().getVehicleNumber());
				vehicleList.put("vendorName",
						vehicleDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				vehicleList.put("status", vehicleDetails.getStatus());
				listOfVehicle.add(vehicleList);
			}
		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(listOfVehicle, MediaType.APPLICATION_JSON).build();
	}

	// Live routes search on the basis of the employee id
	@POST
	@Path("/enmployeeSerchInLiveTrip")
	public Response getParticularEmployeeLiveTrip(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
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
		
		IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		assignRoutePO.setTripAssignDate(new Date());
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		log.info("assignRoutePO.getSearchType()"+assignRoutePO.getSearchType());
		List<EFmFmAssignRoutePO> assignRoutes = new ArrayList<EFmFmAssignRoutePO>();
		if (assignRoutePO.getSearchType().equalsIgnoreCase("EmployeeId")) {
			List<EFmFmUserMasterPO> requestEmployeeIdExitCheck = iEmployeeDetailBO
					.getParticularEmpDetailsFromEmployeeId(assignRoutePO.getEmployeeId(),assignRoutePO.getCombinedFacility());
			if (requestEmployeeIdExitCheck.isEmpty()) {
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("status", "empIdNotExist");
				allRoutes.add(requests);
				 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmEmployeeTripDetailPO> allocatedEmployeeDetail = iCabRequestBO.getAllocatedEmployeeDetail(
					requestEmployeeIdExitCheck.get(0).getUserId(), assignRoutePO.getCombinedFacility(),
					new Date());
			if (allocatedEmployeeDetail.isEmpty()) {
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("status", "notAllocatedForCab");
				allRoutes.add(requests);
				 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
			}
			if (!(allocatedEmployeeDetail.isEmpty())) {
				if (allocatedEmployeeDetail.get(allocatedEmployeeDetail.size() - 1).getEfmFmAssignRoute()
						.getTripStatus().equalsIgnoreCase("allocated")) {
					Map<String, Object> requests = new HashMap<String, Object>();
					requests.put("status", "vehicleNotStarted");
					allRoutes.add(requests);
					 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
					return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
				}
				assignRoutePO.setAssignRouteId(allocatedEmployeeDetail.get(allocatedEmployeeDetail.size() - 1)
						.getEfmFmAssignRoute().getAssignRouteId());
				assignRoutes = iCabRequestBO.getParticularDriverAssignTripDetail(assignRoutePO);
			}
		} else {
			List<EFmFmVehicleMasterPO> eFmFmVehicleMaster = iVehicleCheckInBO.getVehicleDetailsFromVehicleNumber(
					assignRoutePO.getEmployeeId(), assignRoutePO.getCombinedFacility());
			log.info("Vehicle Number"+eFmFmVehicleMaster.size());
			if (!(eFmFmVehicleMaster.isEmpty())) {
				List<EFmFmVehicleCheckInPO> checkInVehicles=iVehicleCheckInBO.getParticularCheckedInVehicles(assignRoutePO.getCombinedFacility(), eFmFmVehicleMaster.get(0).getVehicleId());
				if (!(checkInVehicles.isEmpty()) && checkInVehicles.get(0).getStatus().equalsIgnoreCase("Y")) {
					Map<String, Object> requests = new HashMap<String, Object>();
					requests.put("status", "vehicleCheckedIn");
					allRoutes.add(requests);
					 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
					return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
				}
				else if(!(checkInVehicles.isEmpty())){
					 assignRoutes=iCabRequestBO.getStartedVehicleDetailFromVehicleNumber(checkInVehicles.get(0).getCheckInId(),assignRoutePO.getCombinedFacility());
					 if(assignRoutes.isEmpty()){
							Map<String, Object> requests = new HashMap<String, Object>();
							requests.put("status", "vehicleNotStarted");
							allRoutes.add(requests);
							 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
							return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
					 }
				}
				else{
					Map<String, Object> requests = new HashMap<String, Object>();
					requests.put("status", "vehicleNotCheckedIn");
					allRoutes.add(requests);
					 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
					return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
				}
			} else {
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("status", "vehicleNumNotExist");
				allRoutes.add(requests);
				 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
			}
		}

		if ((!(assignRoutes.isEmpty())) && assignRoutes.get(0).getTripStatus().equalsIgnoreCase("Started")) {
			for (EFmFmAssignRoutePO assignRoute : assignRoutes) {
				StringBuffer waypoints = new StringBuffer();
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO
						.getParticularTripAllEmployees(assignRoute.getAssignRouteId());
				Map<String, Object> requests = new HashMap<String, Object>();
				if (!(employeeTripDetailPO.isEmpty())) {
					for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
						/*
						 * String wayPointsAdhocRequest=""; if
						 * (employeeTripDetail.getEfmFmAssignRoute().
						 * getRouteGenerationType()
						 * .equalsIgnoreCase("AdhocRequest")) {
						 * wayPointsAdhocRequest =
						 * employeeTripDetail.geteFmFmEmployeeTravelRequest()
						 * .geteFmFmEmployeeRequestMaster().
						 * getOriginLattitudeLongitude() + "|"; if
						 * (!employeeTripDetail.geteFmFmEmployeeTravelRequest().
						 * geteFmFmEmployeeRequestMaster()
						 * .getDestination1AddressLattitudeLongitude().
						 * equalsIgnoreCase("N")) {
						 * wayPointsAdhocRequest=wayPointsAdhocRequest+
						 * employeeTripDetail.geteFmFmEmployeeTravelRequest().
						 * geteFmFmEmployeeRequestMaster()
						 * .getDestination1AddressLattitudeLongitude()+"|"; if
						 * (!employeeTripDetail.geteFmFmEmployeeTravelRequest().
						 * geteFmFmEmployeeRequestMaster()
						 * .getDestination2AddressLattitudeLongitude().
						 * equalsIgnoreCase("N")) {
						 * wayPointsAdhocRequest=wayPointsAdhocRequest+
						 * employeeTripDetail.geteFmFmEmployeeTravelRequest().
						 * geteFmFmEmployeeRequestMaster()
						 * .getDestination2AddressLattitudeLongitude()+"|"; if
						 * (!employeeTripDetail.geteFmFmEmployeeTravelRequest().
						 * geteFmFmEmployeeRequestMaster()
						 * .getDestination3AddressLattitudeLongitude().
						 * equalsIgnoreCase("N")) {
						 * wayPointsAdhocRequest=wayPointsAdhocRequest+
						 * employeeTripDetail.geteFmFmEmployeeTravelRequest().
						 * geteFmFmEmployeeRequestMaster()
						 * .getDestination3AddressLattitudeLongitude()+"|"; if
						 * (!employeeTripDetail.geteFmFmEmployeeTravelRequest().
						 * geteFmFmEmployeeRequestMaster()
						 * .getDestination4AddressLattitudeLongitude().
						 * equalsIgnoreCase("N")) {
						 * wayPointsAdhocRequest=wayPointsAdhocRequest+
						 * employeeTripDetail.geteFmFmEmployeeTravelRequest().
						 * geteFmFmEmployeeRequestMaster()
						 * .getDestination4AddressLattitudeLongitude()+"|"; if
						 * (!employeeTripDetail.geteFmFmEmployeeTravelRequest().
						 * geteFmFmEmployeeRequestMaster()
						 * .getDestination5AddressLattitudeLongitude().
						 * equalsIgnoreCase("N")) {
						 * wayPointsAdhocRequest=wayPointsAdhocRequest+
						 * employeeTripDetail.geteFmFmEmployeeTravelRequest().
						 * geteFmFmEmployeeRequestMaster()
						 * .getDestination5AddressLattitudeLongitude()+"|"; } }
						 * } } } waypoints.append(wayPointsAdhocRequest+
						 * employeeTripDetail.geteFmFmEmployeeTravelRequest().
						 * geteFmFmEmployeeRequestMaster()
						 * .getEndDestinationAddressLattitudeLongitude()+"|");
						 * 
						 * }else{
						 */
						if(assignRoute.getRouteGenerationType().equalsIgnoreCase("nodal") && !(waypoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
								.getLatitudeLongitude()))){
							waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
									.getLatitudeLongitude() + "|");

						}
						else{
						waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
								.getLatitudeLongitude() + "|");
						 }
					}
					if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.setLength(0);
						waypoints.append(assignRoute.getNodalPoints());
					}

				} else {
					if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.setLength(0);
						waypoints.append(assignRoute.getNodalPoints());
					}
				}

				requests.put("baseLatLong", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
				
				requests.put("branchName", assignRoute.geteFmFmClientBranchPO().getBranchName());

				requests.put("escortStatus", assignRoute.getEscortRequredFlag());
				requests.put("routeId", assignRoute.getAssignRouteId());
				requests.put("shiftTime", formatter.format(assignRoute.getShiftTime()));
				requests.put("tripType", assignRoute.getTripType());
				requests.put("driverName", assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				requests.put("driverNumber",
						assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				requests.put("vehicleNumber",
						assignRoute.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				requests.put("zoneName", assignRoute.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				requests.put("numberOfEmployees", employeeTripDetailPO.size());
				requests.put("status", assignRoute.getTripStatus());
				requests.put("waypoints", waypoints);
				String speed = "";
				DecimalFormat decimalFormat = new DecimalFormat("0.#");
				List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
						.getRouteLastEtaAndDistanceFromAssignRouteId(assignRoute.geteFmFmClientBranchPO().getBranchId(),
								assignRoute.getAssignRouteId());
				if (!(actualRouteTravelled.isEmpty())) {
					if (actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation() == null) {
						List<EFmFmLiveRoutTravelledPO> travelledEta = assignRouteBO
								.getRouteLastEtaAndDistanceFromAssignRouteId(
										assignRoute.geteFmFmClientBranchPO().getBranchId(),
										assignRoute.getAssignRouteId());
						if (!(travelledEta.isEmpty())) {
							requests.put("ExpectedTime", travelledEta.get(travelledEta.size() - 1).getLiveEta());
							requests.put("currentCabLatiLongi", actualRouteTravelled
									.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
							requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
									.getLiveCurrentCabLocation());
							try {
								float speedInput = Float.parseFloat(
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
								speed = decimalFormat.format(speedInput);
							} catch (Exception e) {
							}
							requests.put("speed", speed);
						} else {
							requests.put("ExpectedTime", "calculating...");
							requests.put("currentCabLatiLongi", actualRouteTravelled
									.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
							requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
									.getLiveCurrentCabLocation());
							try {
								float speedInput = Float.parseFloat(
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
								speed = decimalFormat.format(speedInput);
							} catch (Exception e) {
							}
							requests.put("speed", speed);
						}
					} else {
						requests.put("ExpectedTime",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta());
						requests.put("currentCabLatiLongi",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
						requests.put("currentCabLocation",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation());

						try {
							float speedInput = Float.parseFloat(
									actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
							speed = decimalFormat.format(speedInput);
						} catch (Exception e) {
						}
						requests.put("speed", speed);
					}
				} else {
					requests.put("ExpectedTime", "calculating...");
					requests.put("speed", 0);
					requests.put("currentCabLatiLongi", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
					requests.put("currentCabLocation", assignRoute.geteFmFmClientBranchPO().getAddress());
				}
				// requests.put("status", "success");

				allRoutes.add(requests);
			}
		} else {
			Map<String, Object> requests = new HashMap<String, Object>();
			requests.put("status", "notExistInTracking");
			allRoutes.add(requests);
			 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
			return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();

		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/individualtrip")
	public Response indevidualTripDetail(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
//			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
//			   if (!(userDetail.isEmpty())) {
//				    String jwtToken = "";
//				    try {
//				     JwtTokenGenerator token = new JwtTokenGenerator();
//				     jwtToken = token.generateToken();
//				     userDetail.get(0).setAuthorizationToken(jwtToken);
//				     userDetail.get(0).setTokenGenerationTime(new Date());
//				     userMasterBO.update(userDetail.get(0));
//				    } catch (Exception e) {
//				     log.info("error" + e);
//				    }
//				   }	
//			}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
		
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		assignRoutePO.setAssignRouteId(assignRoutePO.getAssignRouteId());
		List<EFmFmAssignRoutePO> assignRoutes = assignRouteBO.closeParticularTrips(assignRoutePO);
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		if (!(assignRoutes.isEmpty())) {
			for (EFmFmAssignRoutePO assignRoute : assignRoutes) {
				List<Map<String, Object>> tripAllDetails = new ArrayList<Map<String, Object>>();
				Map<String, Object> requests = new HashMap<String, Object>();
				StringBuffer waypoints = new StringBuffer();
				StringBuffer multiLocationWaypoints = new StringBuffer();
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
				if (assignRoute.getTripType().equalsIgnoreCase("PICKUP")) {
					employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployees(assignRoute.getAssignRouteId());
				} else {
					employeeTripDetailPO = iCabRequestBO.getDropTripAllSortedEmployees(assignRoute.getAssignRouteId());
				}
				if (!(employeeTripDetailPO.isEmpty())) {
					for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {

						Map<String, Object> employeeDetails = new HashMap<String, Object>();
						List<EFmFmUserMasterPO> employeeDetail = employeeDetailBO
								.getParticularEmpDetailsFromUserIdWithOutStatus(
										employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.getEfmFmUserMaster().getUserId(),
												assignRoutePO.getCombinedFacility());
						employeeDetails
								.put("name",
										new String(Base64.getDecoder().decode(employeeTripDetail
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
								"utf-8"));
						employeeDetails.put("employeeId", employeeTripDetail.geteFmFmEmployeeTravelRequest()
								.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getEmployeeId());
						employeeDetails.put("employeeNum", new String(Base64.getDecoder().decode(employeeTripDetail
								.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber()), "utf-8"));
						
						employeeDetails.put("gender", new String(Base64.getDecoder().decode(employeeTripDetail
								.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8"));
						String dateFormatted = "0";
						DateFormat formatter = new SimpleDateFormat("HH:mm:ss");

						if (employeeTripDetail.getCabRecheddestinationTime() != 0) {
							Date date = new Date(employeeTripDetail.getCabRecheddestinationTime());
							dateFormatted = formatter.format(date);
						}
						employeeDetails.put("pickedUpTime", dateFormatted);
						/*
						 * if(employeeTripDetail.getBoardedFlg().
						 * equalsIgnoreCase("NO")){
						 * employeeDetails.put("status","No Show"); }
						 */
						if (employeeTripDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
							if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("D")) {
								employeeDetails.put("status", "Dropped");
							} else if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO")) {
								employeeDetails.put("status", "No Show");
							} else {
								employeeDetails.put("status", "Yet to dropped");
							}

						} else if (employeeTripDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
							if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("B")) {
								employeeDetails.put("status", "PickedUp");
							} else if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO")) {
								employeeDetails.put("status", "No Show");
							} else {
								employeeDetails.put("status", "Yet to picked up");
							}
						}
						employeeDetails.put("tripTime",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getShiftTime());
						String wayPointsAdhocRequest = "";
						if (assignRoute.getRouteGenerationType().equalsIgnoreCase("AdhocRequest")) {
							waypoints.setLength(0);
							wayPointsAdhocRequest = employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmEmployeeRequestMaster().getOriginLattitudeLongitude() + "|";
							if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
									.getDestination1AddressLattitudeLongitude().equalsIgnoreCase("N")) {
								wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
										.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
										.getDestination1AddressLattitudeLongitude() + "|";
								if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
										.getDestination2AddressLattitudeLongitude().equalsIgnoreCase("N")) {
									wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
											.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getDestination2AddressLattitudeLongitude() + "|";
									if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmEmployeeRequestMaster().getDestination3AddressLattitudeLongitude()
											.equalsIgnoreCase("N")) {
										wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
												.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination3AddressLattitudeLongitude() + "|";
										if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmEmployeeRequestMaster()
												.getDestination4AddressLattitudeLongitude().equalsIgnoreCase("N")) {
											wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
													.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
													.getDestination4AddressLattitudeLongitude() + "|";
											if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
													.geteFmFmEmployeeRequestMaster()
													.getDestination5AddressLattitudeLongitude().equalsIgnoreCase("N")) {
												wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
														.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
														.getDestination5AddressLattitudeLongitude() + "|";
											}
										}
									}
								}
							}
							waypoints.append(wayPointsAdhocRequest + employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmEmployeeRequestMaster().getEndDestinationAddressLattitudeLongitude()
									+ "|");
							employeeDetails.put("emplatlng",employeeDetail.get(0).getLatitudeLongitude());					
							employeeDetails.put("address", new String(
									Base64.getDecoder().decode(employeeDetail.get(0).getAddress()), "utf-8"));

						} else if (assignRoute.getRouteGenerationType().contains("nodal")) {
							if(!(waypoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()))){
								waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints() + "|");
							}
							employeeDetails.put("emplatlng",employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints());

							employeeDetails.put("address", employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());
						} else {
							waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
									.getLatitudeLongitude() + "|");
							System.out.println("employeeDetail.get(0).getLatitudeLongitude()"+employeeDetail.get(0).getUserId());

							System.out.println("employeeDetail.get(0).getLatitudeLongitude()"+employeeDetail.get(0).getLatitudeLongitude());
						
							employeeDetails.put("emplatlng",employeeDetail.get(0).getLatitudeLongitude());
							employeeDetails.put("address", new String(
									Base64.getDecoder().decode(employeeDetail.get(0).getAddress()), "utf-8"));

						}
						try {
							if (assignRoute.getLocationFlg().equalsIgnoreCase("M")) {
													
								Map<String,Object> waypointsList=new CabRequestService().multilocationlistOfWayPointsAfterAllocated(employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestId(),
										employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getUserId(),assignRoute.getCombinedFacility());
								
								if(!(waypointsList.isEmpty())){									
									for( Map.Entry<String,Object> listofValues:waypointsList.entrySet()){
										if(listofValues.getKey().equalsIgnoreCase("wayPointsList")){
											multiLocationWaypoints.append(listofValues.getValue());
										}else{
											employeeDetails.put("waypointsList",listofValues.getValue());
										}
										
									}				
								}				
							}
						} catch (Exception e) {
							log.debug("multilocation Value not updated");
						}
						
						try {
							if (!(employeeTripDetail.getCabRecheddestinationTime() == 0)) {
								employeeDetails.put("cabReachedTime",
										formatter.format(new Date(employeeTripDetail.getCabRecheddestinationTime())));
							} else {
								employeeDetails.put("cabReachedTime", "0");

							}

						} catch (Exception e) {
							employeeDetails.put("cabReachedTime", "0");
						}
						if (assignRoute.getTripType().equalsIgnoreCase("PICKUP")) {
							try {
								employeeDetails.put("pickUpTime",
										employeeTripDetail.geteFmFmEmployeeTravelRequest().getPickUpTime());
								if (!(employeeTripDetail.getCabstartFromDestination() == 0)
										|| !(employeeTripDetail.getPickedUpDateAndTime() == 0)) {
									if (!(employeeTripDetail.getCabstartFromDestination() == 0)
											&& (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO"))) {
										employeeDetails.put("actualUpTime", formatter
												.format(new Date(employeeTripDetail.getCabstartFromDestination())));
									}
									if (!(employeeTripDetail.getPickedUpDateAndTime() == 0)
											&& (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("B"))) {
										employeeDetails.put("actualUpTime", formatter
												.format(new Date(employeeTripDetail.getPickedUpDateAndTime())));
									}
								} else {
									employeeDetails.put("actualUpTime", "0");

								}
							} catch (Exception e) {
								employeeDetails.put("pickUpTime", "0");
								employeeDetails.put("actualUpTime", "0");
								log.info("Error in drop type" + e);
							}
						}
						if (assignRoute.getTripType().equalsIgnoreCase("DROP")) {
							try {
								if (!(employeeTripDetail.getCabstartFromDestination() == 0)
										|| !(employeeTripDetail.getPickedUpDateAndTime() == 0)) {
									if (!(employeeTripDetail.getCabstartFromDestination() == 0)
											&& (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO"))) {
										employeeDetails.put("actualUpTime", formatter
												.format(new Date(employeeTripDetail.getCabstartFromDestination())));
									}
									if (!(employeeTripDetail.getPickedUpDateAndTime() == 0)
											&& (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("D"))) {
										employeeDetails.put("actualUpTime", formatter
												.format(new Date(employeeTripDetail.getPickedUpDateAndTime())));
									}
								} else {
									employeeDetails.put("actualUpTime", "0");

								}
							} catch (Exception e) {
								employeeDetails.put("actualUpTime", "0");
								log.info("Error in drop type" + e);
							}
						}
						tripAllDetails.add(employeeDetails);
					}
					if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoute.getNodalPoints());
					}
				} else {
					if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoute.getNodalPoints());
					}

				}
				if (assignRoute.getLocationFlg().equalsIgnoreCase("M")) {					
					requests.put("waypoints", multiLocationWaypoints);
				}else{
				  requests.put("waypoints", waypoints);
				}
				requests.put("tripTime",
						assignRoute.getShiftTime());
				requests.put("escortStatus", assignRoute.getEscortRequredFlag());
				requests.put("branchName", assignRoute.geteFmFmClientBranchPO().getBranchName());

				requests.put("tripType", assignRoute.getTripType());
				requests.put("tripStatus", assignRoute.getTripStatus());
				requests.put("routeId", assignRoute.getAssignRouteId());
				requests.put("locationFlg", assignRoute.getLocationFlg());
				requests.put("driverName", assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				requests.put("driverNumber",
						assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				requests.put("vehicleNumber",
						assignRoute.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				requests.put("zoneName", assignRoute.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				requests.put("baseLatLong", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
				requests.put("plannedDistance", assignRoute.getPlannedDistance());
				List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
						.getRouteLastEtaAndDistanceFromAssignRouteId(assignRoute.geteFmFmClientBranchPO().getBranchId(),
								assignRoute.getAssignRouteId());
				List<EFmFmLiveRoutTravelledPO> cabLocation = assignRouteBO
						.getRouteLastEtaAndDistanceFromAssignRouteIdForCabLastLocation(
								assignRoute.geteFmFmClientBranchPO().getBranchId(), assignRoute.getAssignRouteId());
				if (!(actualRouteTravelled.isEmpty())) {
					requests.put("currentCabLatiLongi",
							actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
					requests.put("expectedTime",
							actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta());
					String availDistance = "";
					String speed = "";
					try {
						float distanceInput = (float) (assignRoute.getTravelledDistance());
						float speedInput = Float
								.parseFloat(actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
						DecimalFormat decimalFormat = new DecimalFormat("0.#");
						availDistance = decimalFormat.format(distanceInput);
						speed = decimalFormat.format(speedInput);
					} catch (Exception e) {
						log.info("error" + e);
					}
					requests.put("travelledDistance", availDistance);
					requests.put("speed", speed);
					if (actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation() == null) {
						if (!(cabLocation.isEmpty())) {
							requests.put("currentLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
									.getLiveCurrentCabLocation());
							requests.put("cabStartLocation", cabLocation.get(0).getLiveCurrentCabLocation());
							requests.put("ExpectedTime",
									actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta());
						} else {
							requests.put("currentLocation", "waiting");
							requests.put("cabStartLocation", assignRoutes.get(0).geteFmFmClientBranchPO().getAddress());
						}
					} else {
						requests.put("ExpectedTime",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta());
						requests.put("currentLocation",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation());
					}
					requests.put("cabStartLocation", cabLocation.get(0).getLiveCurrentCabLocation());
				} else {
					requests.put("currentLocation", assignRoute.geteFmFmClientBranchPO().getAddress());
					requests.put("cabStartLocation", assignRoute.geteFmFmClientBranchPO().getAddress());
					requests.put("travelledDistance", 0);
					requests.put("speed", 0);
					requests.put("ExpectedTime", "calculating...");
					requests.put("currentCabLatiLongi", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
				}
				requests.put("empDetails", tripAllDetails);

				allRoutes.add(requests);
			}
		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/femaleTracking")
	public Response femaleEmployeeTracking(EFmFmAssignRoutePO assignRoutePO) throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
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
		
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		long dbQueryTime = new Date().getTime();
	    List<EFmFmAssignRoutePO> assignRoutes = assignRouteBO.getAllStartedTripsFromAssignRoute(assignRoutePO.getCombinedFacility());
		long dbQueryEndTime = new Date().getTime();
		long dbTime = dbQueryEndTime - dbQueryTime;
		log.info("****** livetrip Data Query End Time: " + dbTime);
		log.info("dataSize: " + assignRoutes.size());
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		if (!(assignRoutes.isEmpty())) {
			for (EFmFmAssignRoutePO assignRoute : assignRoutes) {
				StringBuffer waypoints = new StringBuffer();
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
				if (assignRoute.getTripType().equalsIgnoreCase("PICKUP")) {
					employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployees(assignRoute.getAssignRouteId());
				} else {
					employeeTripDetailPO = iCabRequestBO.getDropTripAllSortedEmployees(assignRoute.getAssignRouteId());
				}
				if(assignRoute.getEscortRequredFlag().equalsIgnoreCase("Y")){				
				Map<String, Object> requests = new HashMap<String, Object>();
				if (!(employeeTripDetailPO.isEmpty())) {
					for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
						String wayPointsAdhocRequest = "";
						if (assignRoute.getRouteGenerationType().equalsIgnoreCase("AdhocRequest")) {
							wayPointsAdhocRequest = employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmEmployeeRequestMaster().getOriginLattitudeLongitude() + "|";
							if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
									.getDestination1AddressLattitudeLongitude().equalsIgnoreCase("N")) {
								wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
										.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
										.getDestination1AddressLattitudeLongitude() + "|";
								if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
										.getDestination2AddressLattitudeLongitude().equalsIgnoreCase("N")) {
									wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
											.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getDestination2AddressLattitudeLongitude() + "|";
									if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmEmployeeRequestMaster().getDestination3AddressLattitudeLongitude()
											.equalsIgnoreCase("N")) {
										wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
												.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination3AddressLattitudeLongitude() + "|";
										if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmEmployeeRequestMaster()
												.getDestination4AddressLattitudeLongitude().equalsIgnoreCase("N")) {
											wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
													.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
													.getDestination4AddressLattitudeLongitude() + "|";
											if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
													.geteFmFmEmployeeRequestMaster()
													.getDestination5AddressLattitudeLongitude().equalsIgnoreCase("N")) {
												wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
														.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
														.getDestination5AddressLattitudeLongitude() + "|";
											}
										}
									}
								}
							}
							waypoints.append(wayPointsAdhocRequest + employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmEmployeeRequestMaster().getEndDestinationAddressLattitudeLongitude()
									+ "|");

						} else if (assignRoute.getRouteGenerationType().contains("nodal")) {
							if(!(waypoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()))){
								waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints() + "|");	
							}
						} else {
							waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
									.getLatitudeLongitude() + "|");
						}
					}
					if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoute.getNodalPoints());
					}

				} else {
					if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoute.getNodalPoints());
						log.info("nodalAdhoc" + waypoints);
					}
				}
				requests.put("baseLatLong", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
				requests.put("branchName", assignRoute.geteFmFmClientBranchPO().getBranchName());
				requests.put("escortStatus", assignRoute.getEscortRequredFlag());
				requests.put("routeId", assignRoute.getAssignRouteId());
				requests.put("shiftTime", formatter.format(assignRoute.getShiftTime()));
				requests.put("tripType", assignRoute.getTripType());
				requests.put("driverName", assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				requests.put("driverNumber",
						assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				requests.put("vehicleNumber",
						assignRoute.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				requests.put("zoneName", assignRoute.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				requests.put("numberOfEmployees", employeeTripDetailPO.size());
				requests.put("status", assignRoute.getTripStatus());
				requests.put("waypoints", waypoints);
				String speed = "";
				DecimalFormat decimalFormat = new DecimalFormat("0.#");
				List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
						.getRouteLastEtaAndDistanceFromAssignRouteId(assignRoute.geteFmFmClientBranchPO().getBranchId(),
								assignRoute.getAssignRouteId());
				if (!(actualRouteTravelled.isEmpty())) {
					if (actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation() == null) {
						List<EFmFmLiveRoutTravelledPO> travelledEta = assignRouteBO
								.getRouteLastEtaAndDistanceFromAssignRouteId(
										assignRoute.geteFmFmClientBranchPO().getBranchId(),
										assignRoute.getAssignRouteId());
						if (!(travelledEta.isEmpty())) {
							requests.put("ExpectedTime", travelledEta.get(travelledEta.size() - 1).getLiveEta());
							requests.put("currentCabLatiLongi", actualRouteTravelled
									.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
							requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
									.getLiveCurrentCabLocation());
							try {
								float speedInput = Float.parseFloat(
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
								speed = decimalFormat.format(speedInput);
							} catch (Exception e) {
							}
							requests.put("speed", speed);
						} else {
							requests.put("ExpectedTime", "calculating...");
							requests.put("currentCabLatiLongi", actualRouteTravelled
									.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
							requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
									.getLiveCurrentCabLocation());

							try {
								float speedInput = Float.parseFloat(
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
								speed = decimalFormat.format(speedInput);
							} catch (Exception e) {
							}
							requests.put("speed", speed);
						}
					} else {
						requests.put("ExpectedTime",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta());
						requests.put("currentCabLatiLongi",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
						requests.put("currentCabLocation",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation());
						try {
							float speedInput = Float.parseFloat(
									actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
							speed = decimalFormat.format(speedInput);
						} catch (Exception e) {
							log.info("error" + e);
						}
						requests.put("speed", speed);
					}
				} else {
					requests.put("ExpectedTime", "calculating...");
					requests.put("speed", 0);
					requests.put("currentCabLatiLongi", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
					requests.put("currentCabLocation", assignRoute.geteFmFmClientBranchPO().getAddress());

				}
				allRoutes.add(requests);
			}
			else if(assignRoute.getEscortRequredFlag().equalsIgnoreCase("N") && assignRoute.getTripType().equalsIgnoreCase("PICKUP") && 
					new String(Base64.getDecoder().decode(employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),
					"utf-8").equalsIgnoreCase("Female")){				
				Map<String, Object> requests = new HashMap<String, Object>();					
					if (!(employeeTripDetailPO.isEmpty())) {
						for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
							String wayPointsAdhocRequest = "";
							if (assignRoute.getRouteGenerationType().equalsIgnoreCase("AdhocRequest")) {
								wayPointsAdhocRequest = employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.geteFmFmEmployeeRequestMaster().getOriginLattitudeLongitude() + "|";
								if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
										.getDestination1AddressLattitudeLongitude().equalsIgnoreCase("N")) {
									wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
											.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getDestination1AddressLattitudeLongitude() + "|";
									if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getDestination2AddressLattitudeLongitude().equalsIgnoreCase("N")) {
										wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
												.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination2AddressLattitudeLongitude() + "|";
										if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmEmployeeRequestMaster().getDestination3AddressLattitudeLongitude()
												.equalsIgnoreCase("N")) {
											wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
													.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
													.getDestination3AddressLattitudeLongitude() + "|";
											if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
													.geteFmFmEmployeeRequestMaster()
													.getDestination4AddressLattitudeLongitude().equalsIgnoreCase("N")) {
												wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
														.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
														.getDestination4AddressLattitudeLongitude() + "|";
												if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
														.geteFmFmEmployeeRequestMaster()
														.getDestination5AddressLattitudeLongitude().equalsIgnoreCase("N")) {
													wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
															.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
															.getDestination5AddressLattitudeLongitude() + "|";
												}
											}
										}
									}
								}
								waypoints.append(wayPointsAdhocRequest + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.geteFmFmEmployeeRequestMaster().getEndDestinationAddressLattitudeLongitude()
										+ "|");

							} else if (assignRoute.getRouteGenerationType().contains("nodal")) {
								if(!(waypoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()))){
									waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints() + "|");	
								}
							} else {
								waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getLatitudeLongitude() + "|");
							}
						}
						if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
							waypoints.append(assignRoute.getNodalPoints());
						}

					} else {
						if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
							waypoints.append(assignRoute.getNodalPoints());
							log.info("nodalAdhoc" + waypoints);
						}
					}
					requests.put("baseLatLong", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
					requests.put("escortStatus", assignRoute.getEscortRequredFlag());
					requests.put("routeId", assignRoute.getAssignRouteId());
					requests.put("shiftTime", formatter.format(assignRoute.getShiftTime()));
					requests.put("tripType", assignRoute.getTripType());
					requests.put("driverName", assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
					requests.put("driverNumber",
							assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
					requests.put("vehicleNumber",
							assignRoute.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
					requests.put("zoneName", assignRoute.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					requests.put("numberOfEmployees", employeeTripDetailPO.size());
					requests.put("status", assignRoute.getTripStatus());
					requests.put("waypoints", waypoints);
					String speed = "";
					DecimalFormat decimalFormat = new DecimalFormat("0.#");
					List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
							.getRouteLastEtaAndDistanceFromAssignRouteId(assignRoute.geteFmFmClientBranchPO().getBranchId(),
									assignRoute.getAssignRouteId());
					if (!(actualRouteTravelled.isEmpty())) {
						if (actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation() == null) {
							List<EFmFmLiveRoutTravelledPO> travelledEta = assignRouteBO
									.getRouteLastEtaAndDistanceFromAssignRouteId(
											assignRoute.geteFmFmClientBranchPO().getBranchId(),
											assignRoute.getAssignRouteId());
							if (!(travelledEta.isEmpty())) {
								requests.put("ExpectedTime", travelledEta.get(travelledEta.size() - 1).getLiveEta());
								requests.put("currentCabLatiLongi", actualRouteTravelled
										.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
								requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
										.getLiveCurrentCabLocation());
								try {
									float speedInput = Float.parseFloat(
											actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
									speed = decimalFormat.format(speedInput);
								} catch (Exception e) {
								}
								requests.put("speed", speed);
							} else {
								requests.put("ExpectedTime", "calculating...");
								requests.put("currentCabLatiLongi", actualRouteTravelled
										.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
								requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
										.getLiveCurrentCabLocation());

								try {
									float speedInput = Float.parseFloat(
											actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
									speed = decimalFormat.format(speedInput);
								} catch (Exception e) {
								}
								requests.put("speed", speed);
							}
						} else {
							requests.put("ExpectedTime",
									actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta());
							requests.put("currentCabLatiLongi",
									actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
							requests.put("currentCabLocation",
									actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation());
							try {
								float speedInput = Float.parseFloat(
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
								speed = decimalFormat.format(speedInput);
							} catch (Exception e) {
								log.info("error" + e);
							}
							requests.put("speed", speed);
						}
					} else {
						requests.put("ExpectedTime", "calculating...");
						requests.put("speed", 0);
						requests.put("currentCabLatiLongi", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
						requests.put("currentCabLocation", assignRoute.geteFmFmClientBranchPO().getAddress());

					}
					allRoutes.add(requests);
				
				}
			else if(assignRoute.getEscortRequredFlag().equalsIgnoreCase("N") && assignRoute.getTripType().equalsIgnoreCase("DROP") && 
					new String(Base64.getDecoder().decode(employeeTripDetailPO.get(employeeTripDetailPO.size()-1).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),
					"utf-8").equalsIgnoreCase("Female")){				
				Map<String, Object> requests = new HashMap<String, Object>();					
					if (!(employeeTripDetailPO.isEmpty())) {
						for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
							String wayPointsAdhocRequest = "";
							if (assignRoute.getRouteGenerationType().equalsIgnoreCase("AdhocRequest")) {
								wayPointsAdhocRequest = employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.geteFmFmEmployeeRequestMaster().getOriginLattitudeLongitude() + "|";
								if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
										.getDestination1AddressLattitudeLongitude().equalsIgnoreCase("N")) {
									wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
											.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getDestination1AddressLattitudeLongitude() + "|";
									if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getDestination2AddressLattitudeLongitude().equalsIgnoreCase("N")) {
										wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
												.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination2AddressLattitudeLongitude() + "|";
										if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmEmployeeRequestMaster().getDestination3AddressLattitudeLongitude()
												.equalsIgnoreCase("N")) {
											wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
													.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
													.getDestination3AddressLattitudeLongitude() + "|";
											if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
													.geteFmFmEmployeeRequestMaster()
													.getDestination4AddressLattitudeLongitude().equalsIgnoreCase("N")) {
												wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
														.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
														.getDestination4AddressLattitudeLongitude() + "|";
												if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
														.geteFmFmEmployeeRequestMaster()
														.getDestination5AddressLattitudeLongitude().equalsIgnoreCase("N")) {
													wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
															.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
															.getDestination5AddressLattitudeLongitude() + "|";
												}
											}
										}
									}
								}
								waypoints.append(wayPointsAdhocRequest + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.geteFmFmEmployeeRequestMaster().getEndDestinationAddressLattitudeLongitude()
										+ "|");

							} else if (assignRoute.getRouteGenerationType().contains("nodal")) {
								if(!(waypoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()))){
									waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints() + "|");	
								}
							} else {
								waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getLatitudeLongitude() + "|");
							}
						}
						if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
							waypoints.append(assignRoute.getNodalPoints());
						}

					} else {
						if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
							waypoints.append(assignRoute.getNodalPoints());
							log.info("nodalAdhoc" + waypoints);
						}
					}
					requests.put("baseLatLong", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
					requests.put("branchName", assignRoute.geteFmFmClientBranchPO().getBranchName());

					requests.put("escortStatus", assignRoute.getEscortRequredFlag());
					requests.put("routeId", assignRoute.getAssignRouteId());
					requests.put("shiftTime", formatter.format(assignRoute.getShiftTime()));
					requests.put("tripType", assignRoute.getTripType());
					requests.put("driverName", assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
					requests.put("driverNumber",
							assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
					requests.put("vehicleNumber",
							assignRoute.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
					requests.put("zoneName", assignRoute.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					requests.put("numberOfEmployees", employeeTripDetailPO.size());
					requests.put("status", assignRoute.getTripStatus());
					requests.put("waypoints", waypoints);
					String speed = "";
					DecimalFormat decimalFormat = new DecimalFormat("0.#");
					List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
							.getRouteLastEtaAndDistanceFromAssignRouteId(assignRoute.geteFmFmClientBranchPO().getBranchId(),
									assignRoute.getAssignRouteId());
					if (!(actualRouteTravelled.isEmpty())) {
						if (actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation() == null) {
							List<EFmFmLiveRoutTravelledPO> travelledEta = assignRouteBO
									.getRouteLastEtaAndDistanceFromAssignRouteId(
											assignRoute.geteFmFmClientBranchPO().getBranchId(),
											assignRoute.getAssignRouteId());
							if (!(travelledEta.isEmpty())) {
								requests.put("ExpectedTime", travelledEta.get(travelledEta.size() - 1).getLiveEta());
								requests.put("currentCabLatiLongi", actualRouteTravelled
										.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
								requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
										.getLiveCurrentCabLocation());
								try {
									float speedInput = Float.parseFloat(
											actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
									speed = decimalFormat.format(speedInput);
								} catch (Exception e) {
								}
								requests.put("speed", speed);
							} else {
								requests.put("ExpectedTime", "calculating...");
								requests.put("currentCabLatiLongi", actualRouteTravelled
										.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
								requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
										.getLiveCurrentCabLocation());

								try {
									float speedInput = Float.parseFloat(
											actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
									speed = decimalFormat.format(speedInput);
								} catch (Exception e) {
								}
								requests.put("speed", speed);
							}
						} else {
							requests.put("ExpectedTime",
									actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta());
							requests.put("currentCabLatiLongi",
									actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
							requests.put("currentCabLocation",
									actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation());
							try {
								float speedInput = Float.parseFloat(
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
								speed = decimalFormat.format(speedInput);
							} catch (Exception e) {
								log.info("error" + e);
							}
							requests.put("speed", speed);
						}
					} else {
						requests.put("ExpectedTime", "calculating...");
						requests.put("speed", 0);
						requests.put("currentCabLatiLongi", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
						requests.put("currentCabLocation", assignRoute.geteFmFmClientBranchPO().getAddress());

					}
					allRoutes.add(requests);
				
				}
			else{
				List<EFmFmEmployeeTripDetailPO> noShowemployeeTripDetailPO = null;
				if (assignRoute.getTripType().equalsIgnoreCase("PICKUP")) {
					noShowemployeeTripDetailPO = iCabRequestBO.getAllWithOutNoShowPickupTripEmployees(assignRoute.getAssignRouteId());
				} else {
					noShowemployeeTripDetailPO = iCabRequestBO.getAllWithOutNoShowDropTripEmployees(assignRoute.getAssignRouteId());
				}
				if(assignRoute.getEscortRequredFlag().equalsIgnoreCase("N") && assignRoute.getTripType().equalsIgnoreCase("PICKUP") && 
						new String(Base64.getDecoder().decode(noShowemployeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),
						"utf-8").equalsIgnoreCase("Female")){				
					Map<String, Object> requests = new HashMap<String, Object>();					
						if (!(employeeTripDetailPO.isEmpty())) {
							for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
								String wayPointsAdhocRequest = "";
								if (assignRoute.getRouteGenerationType().equalsIgnoreCase("AdhocRequest")) {
									wayPointsAdhocRequest = employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmEmployeeRequestMaster().getOriginLattitudeLongitude() + "|";
									if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getDestination1AddressLattitudeLongitude().equalsIgnoreCase("N")) {
										wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
												.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination1AddressLattitudeLongitude() + "|";
										if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination2AddressLattitudeLongitude().equalsIgnoreCase("N")) {
											wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
													.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
													.getDestination2AddressLattitudeLongitude() + "|";
											if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
													.geteFmFmEmployeeRequestMaster().getDestination3AddressLattitudeLongitude()
													.equalsIgnoreCase("N")) {
												wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
														.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
														.getDestination3AddressLattitudeLongitude() + "|";
												if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
														.geteFmFmEmployeeRequestMaster()
														.getDestination4AddressLattitudeLongitude().equalsIgnoreCase("N")) {
													wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
															.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
															.getDestination4AddressLattitudeLongitude() + "|";
													if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
															.geteFmFmEmployeeRequestMaster()
															.getDestination5AddressLattitudeLongitude().equalsIgnoreCase("N")) {
														wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
																.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
																.getDestination5AddressLattitudeLongitude() + "|";
													}
												}
											}
										}
									}
									waypoints.append(wayPointsAdhocRequest + employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmEmployeeRequestMaster().getEndDestinationAddressLattitudeLongitude()
											+ "|");

								} else if (assignRoute.getRouteGenerationType().contains("nodal")) {
									if(!(waypoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()))){
										waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints() + "|");	
									}
								} else {
									waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
											.getLatitudeLongitude() + "|");
								}
							}
							if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
								waypoints.append(assignRoute.getNodalPoints());
							}

						} else {
							if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
								waypoints.append(assignRoute.getNodalPoints());
								log.info("nodalAdhoc" + waypoints);
							}
						}
						requests.put("baseLatLong", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
						requests.put("branchName", assignRoute.geteFmFmClientBranchPO().getBranchName());
						requests.put("escortStatus", assignRoute.getEscortRequredFlag());
						requests.put("routeId", assignRoute.getAssignRouteId());
						requests.put("shiftTime", formatter.format(assignRoute.getShiftTime()));
						requests.put("tripType", assignRoute.getTripType());
						requests.put("driverName", assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						requests.put("driverNumber",
								assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
						requests.put("vehicleNumber",
								assignRoute.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						requests.put("zoneName", assignRoute.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
						requests.put("numberOfEmployees", employeeTripDetailPO.size());
						requests.put("status", assignRoute.getTripStatus());
						requests.put("waypoints", waypoints);
						String speed = "";
						DecimalFormat decimalFormat = new DecimalFormat("0.#");
						List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
								.getRouteLastEtaAndDistanceFromAssignRouteId(assignRoute.geteFmFmClientBranchPO().getBranchId(),
										assignRoute.getAssignRouteId());
						if (!(actualRouteTravelled.isEmpty())) {
							if (actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation() == null) {
								List<EFmFmLiveRoutTravelledPO> travelledEta = assignRouteBO
										.getRouteLastEtaAndDistanceFromAssignRouteId(
												assignRoute.geteFmFmClientBranchPO().getBranchId(),
												assignRoute.getAssignRouteId());
								if (!(travelledEta.isEmpty())) {
									requests.put("ExpectedTime", travelledEta.get(travelledEta.size() - 1).getLiveEta());
									requests.put("currentCabLatiLongi", actualRouteTravelled
											.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
									requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
											.getLiveCurrentCabLocation());
									try {
										float speedInput = Float.parseFloat(
												actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
										speed = decimalFormat.format(speedInput);
									} catch (Exception e) {
									}
									requests.put("speed", speed);
								} else {
									requests.put("ExpectedTime", "calculating...");
									requests.put("currentCabLatiLongi", actualRouteTravelled
											.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
									requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
											.getLiveCurrentCabLocation());

									try {
										float speedInput = Float.parseFloat(
												actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
										speed = decimalFormat.format(speedInput);
									} catch (Exception e) {
									}
									requests.put("speed", speed);
								}
							} else {
								requests.put("ExpectedTime",
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta());
								requests.put("currentCabLatiLongi",
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
								requests.put("currentCabLocation",
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation());
								try {
									float speedInput = Float.parseFloat(
											actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
									speed = decimalFormat.format(speedInput);
								} catch (Exception e) {
									log.info("error" + e);
								}
								requests.put("speed", speed);
							}
						} else {
							requests.put("ExpectedTime", "calculating...");
							requests.put("speed", 0);
							requests.put("currentCabLatiLongi", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
							requests.put("currentCabLocation", assignRoute.geteFmFmClientBranchPO().getAddress());

						}
						allRoutes.add(requests);
					
					}
				else if(assignRoute.getEscortRequredFlag().equalsIgnoreCase("N") && assignRoute.getTripType().equalsIgnoreCase("DROP") && 
						new String(Base64.getDecoder().decode(noShowemployeeTripDetailPO.get(noShowemployeeTripDetailPO.size()-1).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),
						"utf-8").equalsIgnoreCase("Female")){				
					Map<String, Object> requests = new HashMap<String, Object>();					
						if (!(employeeTripDetailPO.isEmpty())) {
							for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
								String wayPointsAdhocRequest = "";
								if (assignRoute.getRouteGenerationType().equalsIgnoreCase("AdhocRequest")) {
									wayPointsAdhocRequest = employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmEmployeeRequestMaster().getOriginLattitudeLongitude() + "|";
									if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getDestination1AddressLattitudeLongitude().equalsIgnoreCase("N")) {
										wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
												.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination1AddressLattitudeLongitude() + "|";
										if (!employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination2AddressLattitudeLongitude().equalsIgnoreCase("N")) {
											wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
													.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
													.getDestination2AddressLattitudeLongitude() + "|";
											if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
													.geteFmFmEmployeeRequestMaster().getDestination3AddressLattitudeLongitude()
													.equalsIgnoreCase("N")) {
												wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
														.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
														.getDestination3AddressLattitudeLongitude() + "|";
												if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
														.geteFmFmEmployeeRequestMaster()
														.getDestination4AddressLattitudeLongitude().equalsIgnoreCase("N")) {
													wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
															.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
															.getDestination4AddressLattitudeLongitude() + "|";
													if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
															.geteFmFmEmployeeRequestMaster()
															.getDestination5AddressLattitudeLongitude().equalsIgnoreCase("N")) {
														wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
																.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
																.getDestination5AddressLattitudeLongitude() + "|";
													}
												}
											}
										}
									}
									waypoints.append(wayPointsAdhocRequest + employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmEmployeeRequestMaster().getEndDestinationAddressLattitudeLongitude()
											+ "|");

								} else if (assignRoute.getRouteGenerationType().contains("nodal")) {
									if(!(waypoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()))){
										waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints() + "|");	
									}
								} else {
									waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
											.getLatitudeLongitude() + "|");
								}
							}
							if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
								waypoints.append(assignRoute.getNodalPoints());
							}

						} else {
							if (assignRoute.getRouteGenerationType().contains("nodalAdhoc")) {
								waypoints.append(assignRoute.getNodalPoints());
								log.info("nodalAdhoc" + waypoints);
							}
						}
						requests.put("baseLatLong", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
						requests.put("branchName", assignRoute.geteFmFmClientBranchPO().getBranchName());
						requests.put("escortStatus", assignRoute.getEscortRequredFlag());
						requests.put("routeId", assignRoute.getAssignRouteId());
						requests.put("shiftTime", formatter.format(assignRoute.getShiftTime()));
						requests.put("tripType", assignRoute.getTripType());
						requests.put("driverName", assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						requests.put("driverNumber",
								assignRoute.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
						requests.put("vehicleNumber",
								assignRoute.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						requests.put("zoneName", assignRoute.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
						requests.put("numberOfEmployees", employeeTripDetailPO.size());
						requests.put("status", assignRoute.getTripStatus());
						requests.put("waypoints", waypoints);
						String speed = "";
						DecimalFormat decimalFormat = new DecimalFormat("0.#");
						List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
								.getRouteLastEtaAndDistanceFromAssignRouteId(assignRoute.geteFmFmClientBranchPO().getBranchId(),
										assignRoute.getAssignRouteId());
						if (!(actualRouteTravelled.isEmpty())) {
							if (actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation() == null) {
								List<EFmFmLiveRoutTravelledPO> travelledEta = assignRouteBO
										.getRouteLastEtaAndDistanceFromAssignRouteId(
												assignRoute.geteFmFmClientBranchPO().getBranchId(),
												assignRoute.getAssignRouteId());
								if (!(travelledEta.isEmpty())) {
									requests.put("ExpectedTime", travelledEta.get(travelledEta.size() - 1).getLiveEta());
									requests.put("currentCabLatiLongi", actualRouteTravelled
											.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
									requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
											.getLiveCurrentCabLocation());
									try {
										float speedInput = Float.parseFloat(
												actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
										speed = decimalFormat.format(speedInput);
									} catch (Exception e) {
									}
									requests.put("speed", speed);
								} else {
									requests.put("ExpectedTime", "calculating...");
									requests.put("currentCabLatiLongi", actualRouteTravelled
											.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
									requests.put("currentCabLocation", actualRouteTravelled.get(actualRouteTravelled.size() - 1)
											.getLiveCurrentCabLocation());

									try {
										float speedInput = Float.parseFloat(
												actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
										speed = decimalFormat.format(speedInput);
									} catch (Exception e) {
									}
									requests.put("speed", speed);
								}
							} else {
								requests.put("ExpectedTime",
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta());
								requests.put("currentCabLatiLongi",
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
								requests.put("currentCabLocation",
										actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation());
								try {
									float speedInput = Float.parseFloat(
											actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveSpeed());
									speed = decimalFormat.format(speedInput);
								} catch (Exception e) {
									log.info("error" + e);
								}
								requests.put("speed", speed);
							}
						} else {
							requests.put("ExpectedTime", "calculating...");
							requests.put("speed", 0);
							requests.put("currentCabLatiLongi", assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude());
							requests.put("currentCabLocation", assignRoute.geteFmFmClientBranchPO().getAddress());

						}
						allRoutes.add(requests);
					
					}		
			}
				
			}
		}
		long finalResponceTime = new Date().getTime() - dbQueryTime;
		log.info("****** finalResponceTime Time" + finalResponceTime);
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
	}

	public long minusDayFromDate(Date date, int numDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, numDays);
		return calendar.getTimeInMillis();
	}
}
