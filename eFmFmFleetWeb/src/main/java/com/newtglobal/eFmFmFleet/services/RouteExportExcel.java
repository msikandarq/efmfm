package com.newtglobal.eFmFmFleet.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.MessagingService;
import com.newtglobal.eFmFmFleet.eFmFmFleet.PushNotificationService;
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
@Path("/xlEmployeeExport")
public class RouteExportExcel {
    private static Log logs = LogFactory.getLog(RouteExportExcel.class);

    private static final String SERVER_UPLOAD_LINUX_LOCATION_FOLDER = ContextLoader.getContext()
            .getMessage("upload.docsLinux", null, "docsLinux", null);
    private static final String SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER = ContextLoader.getContext()
            .getMessage("upload.docsWindows", null, "docsWindows", null);
	private static Log log = LogFactory.getLog(RouteExportExcel.class);
	DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

    
    /*
     * Function For getting all routes of particular shift
     * 
     * @parameter is date, shift time and trip type as a request parameter 
     * 
     * @author Sarfraz Khan
     * 
     * @since 2016-10-13
     */
    
    @POST
    @Path("/printall")
    @Produces("application/json")
    public Response getPrintForParticularShiftAllRoutes(EFmFmEmployeeTravelRequestPO travelRequestPO) throws ParseException, UnsupportedEncodingException {
        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequestPO.getUserId()))){
        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(travelRequestPO.getUserId());
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
        log.info(travelRequestPO.getCombinedFacility()+"serviceStart -UserId :" + travelRequestPO.getUserId());
        DateFormat shiftTimeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat shiftTimeFormate = new SimpleDateFormat("HH:mm");
        String shiftTime = travelRequestPO.getTime();
        Date shift = shiftTimeFormate.parse(shiftTime);
        java.sql.Time dateShiftTime = new java.sql.Time(shift.getTime());
        Date excutionDate = dateformate.parse(travelRequestPO.getExecutionDate());
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
        List<EFmFmAssignRoutePO> activeRoutes = iAssignRouteBO.getAllRoutesForPrintForParticularShift(excutionDate, excutionDate,
                travelRequestPO.getTripType(), shiftTimeFormat.format(dateShiftTime), travelRequestPO.getCombinedFacility());
        int totalNumberOfEmployee=0;
        if (!(activeRoutes.isEmpty())) {
        	for(EFmFmAssignRoutePO assignRoutes:activeRoutes){
				List<Map<String, Object>> tripAllDetails = new ArrayList<Map<String, Object>>();
				Map<String, Object> requests = new HashMap<String, Object>();
				StringBuffer waypoints = new StringBuffer();
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
				if (assignRoutes.getTripType().equalsIgnoreCase("PICKUP")) {
					employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployees(assignRoutes.getAssignRouteId());
				} else {
					employeeTripDetailPO = iCabRequestBO.getDropTripAllSortedEmployees(assignRoutes.getAssignRouteId());
				}
				totalNumberOfEmployee=totalNumberOfEmployee+employeeTripDetailPO.size();
				if (!(employeeTripDetailPO.isEmpty())) {
					for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
						Map<String, Object> employeeDetails = new HashMap<String, Object>();
						String wayPointsAdhocRequest = "";
						employeeDetails
								.put("name",
										new String(Base64.getDecoder().decode(employeeTripDetail
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
								"utf-8"));
						employeeDetails.put("employeeId", employeeTripDetail.geteFmFmEmployeeTravelRequest()
								.getEfmFmUserMaster().getEmployeeId());
						employeeDetails.put("facilityName", employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmClientBranchPO().getBranchName());

						employeeDetails.put("requestId",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestId());
						employeeDetails.put("requestType",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestType());
						employeeDetails.put("tripDetailId", employeeTripDetail.getEmpTripId());
						employeeDetails
								.put("physicallyChallenged",
										new String(
												Base64.getDecoder()
														.decode(employeeTripDetail.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getPhysicallyChallenged()),
												"utf-8"));
						employeeDetails
								.put("isInjured",
										new String(Base64.getDecoder().decode(employeeTripDetail
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getIsInjured()),
								"utf-8"));
						employeeDetails.put("pragnentLady", new String(Base64.getDecoder().decode(employeeTripDetail
								.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getPragnentLady()), "utf-8"));
						if (employeeTripDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
							if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("D")) {
								employeeDetails.put("boardedStatus", "Dropped");
							} else if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO")) {
								employeeDetails.put("boardedStatus", "No Show");
							} else {
								employeeDetails.put("boardedStatus", "Yet to dropped");
							}

						} else if (employeeTripDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
							if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("B")) {
								employeeDetails.put("boardedStatus", "PickedUp");
							} else if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO")) {
								employeeDetails.put("boardedStatus", "No Show");
							} else {
								employeeDetails.put("boardedStatus", "Yet to picked up");
							}
						}
						if (assignRoutes.getRouteGenerationType().equalsIgnoreCase("AdhocRequest")) {
							if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
									.geteFmFmEmployeeRequestMaster().getDestination1AddressLattitudeLongitude()
									.equalsIgnoreCase("N")) {
								wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
								.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
								.getDestination1AddressLattitudeLongitude() + "|";
								if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
										.geteFmFmEmployeeRequestMaster().getDestination2AddressLattitudeLongitude()
										.equalsIgnoreCase("N")) {
									wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
									.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
									.getDestination1AddressLattitudeLongitude() + "|";
									if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
											.geteFmFmEmployeeRequestMaster().getDestination3AddressLattitudeLongitude()
											.equalsIgnoreCase("N")) {
										wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
										.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
										.getDestination1AddressLattitudeLongitude() + "|";
										if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
												.geteFmFmEmployeeRequestMaster()
												.getDestination4AddressLattitudeLongitude().equalsIgnoreCase("N")) {
											wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
											.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getDestination1AddressLattitudeLongitude() + "|";
											if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
													.geteFmFmEmployeeRequestMaster()
													.getDestination5AddressLattitudeLongitude().equalsIgnoreCase("N")) {
												wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
												.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination1AddressLattitudeLongitude() + "|";
											}
										}
									}
								}
							}
							waypoints.append(wayPointsAdhocRequest + employeeTripDetailPO.get(0)
							.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
							.getEndDestinationAddressLattitudeLongitude() + "|");
							employeeDetails.put("address",
									new String(
											Base64.getDecoder().decode(employeeTripDetail
													.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()),
									"utf-8"));

						}
						else if (assignRoutes.getRouteGenerationType().contains("nodal")) {
							if (!(waypoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()))) {
								waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints() + "|");
							}
							employeeDetails.put("address", employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());

						} else {
							waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
									.getLatitudeLongitude() + "|");
							employeeDetails.put("address",
									new String(
											Base64.getDecoder().decode(employeeTripDetail
													.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()),
									"utf-8"));
						}
						employeeDetails.put("employeeNumber", new String(Base64.getDecoder().decode(employeeTripDetail
								.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber()), "utf-8"));
						employeeDetails.put("empComingStatus", employeeTripDetail.getComingStatus());
						employeeDetails.put("requestColor", employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestColor());
						
						employeeDetails.put("locationFlg",employeeTripDetail.geteFmFmEmployeeTravelRequest().getLocationFlg());
						employeeDetails.put("locationWayPointsIds",employeeTripDetail.geteFmFmEmployeeTravelRequest().getLocationWaypointsIds());	
						
						try {
							if (employeeTripDetail.geteFmFmEmployeeTravelRequest().getLocationFlg()
									.equalsIgnoreCase("M")) {
								// waypoints.setLength(0);							
								Map<String, Object> waypointsList = new CabRequestService().listOfPointsForMultipleLocation(
										employeeTripDetail.geteFmFmEmployeeTravelRequest().getLocationWaypointsIds(),
										assignRoutes.geteFmFmClientBranchPO().getBranchId(),employeeTripDetail
										.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getUserId(),travelRequestPO.getCombinedFacility());
								if (!(waypointsList.isEmpty())) {
									for (Map.Entry<String, Object> listofValues : waypointsList.entrySet()) {
										if (listofValues.getKey().equalsIgnoreCase("wayPointsList")) {
											employeeDetails.put("multipleWaypoints", listofValues.getValue());
										} else {
											requests.put("waypointsList", listofValues.getValue());
										}

									}
								}
							}
						} catch (Exception e) {
							log.debug("multilocation Value not updated");
						}		
						
						
						employeeDetails.put("tripType", assignRoutes.getTripType());
						employeeDetails.put("plaCardPrint",employeeTripDetail.getEfmFmAssignRoute().geteFmFmClientBranchPO().getPlaCardPrint());
						employeeDetails.put("empArea", employeeTripDetail.geteFmFmEmployeeTravelRequest()
								.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
						if (new String(Base64.getDecoder().decode(
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),
								"utf-8").equalsIgnoreCase("Male")) {
							employeeDetails.put("gender", 1);
						} else {
							employeeDetails.put("gender", 2);
						}
						employeeDetails.put("boardedFlg", employeeTripDetail.getBoardedFlg());
						employeeDetails.put("tripTime",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getShiftTime());
						if (assignRoutes.getTripType().equalsIgnoreCase("PICKUP")) {
							employeeDetails.put("pickUpTime",
									employeeTripDetail.geteFmFmEmployeeTravelRequest().getPickUpTime());
						} else {
							employeeDetails.put("pickUpTime",
									employeeTripDetail.geteFmFmEmployeeTravelRequest().getDropSequence());
						}
						requests.put("boardedFlg", employeeTripDetail.getBoardedFlg());
						tripAllDetails.add(employeeDetails);

					}
					if (assignRoutes.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoutes.getNodalPoints());
					}

				} 
				else {
					if (assignRoutes.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoutes.getNodalPoints());
					}
				}
				requests.put("assignRouteId", assignRoutes.getAssignRouteId());
				requests.put("tripType", assignRoutes.getTripType());
				if (assignRoutes.getEscortRequredFlag().equalsIgnoreCase("Y")) {
					try {
						int escortId = assignRoutes.geteFmFmEscortCheckIn().getEscortCheckInId();
						log.info("escortId" + escortId);
						requests.put("escortName",
								assignRoutes.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
						requests.put("escortId", assignRoutes.geteFmFmEscortCheckIn().getEscortCheckInId());
					} catch (Exception e) {
						requests.put("escortId", "N");
						requests.put("escortName", "Escort Required But Not Available");
					}
				} else {
					requests.put("escortName", "Not Required");
				}
				try {					
					if(assignRoutes.getIsBackTwoBack().equalsIgnoreCase("N")){
				        List<EFmFmAssignRoutePO> b2bDetails=iAssignRouteBO.getBackToBackTripDetailFromb2bId(assignRoutes.getAssignRouteId(), "DROP",travelRequestPO.getCombinedFacility());
						if(!(b2bDetails.isEmpty())){
				        requests.put("suggestiveVehicleNumber",b2bDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						requests.put("backTwoBackRouteId",   b2bDetails.get(0).getAssignRouteId());
						}else{
						requests.put("suggestiveVehicleNumber","No");
						}
					}else{
						if(assignRoutes.getIsBackTwoBack().equalsIgnoreCase("Y")){
					        List<EFmFmAssignRoutePO> b2bRouteDetails=iAssignRouteBO.getParticularRouteDetailFromAssignRouteId(assignRoutes.getBackTwoBackRouteId());
									if(assignRoutes.getTripType().equalsIgnoreCase("DROP")){
							        	requests.put("suggestiveVehicleNumber","B2b with Route Id "+b2bRouteDetails.get(0).getAssignRouteId()+","+shiftTimeFormater.format(b2bRouteDetails.get(0).getShiftTime()) + " and Route Name:"+b2bRouteDetails.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
									}
									else{																				
										requests.put("suggestiveVehicleNumber",b2bRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
										requests.put("backTwoBackRouteId",   assignRoutes.getBackTwoBackRouteId());
									}
										
//								}
							
						}
						}
					
				} catch (Exception e) {
					log.info("Back2backShiftTime error" + e);
				}
				requests.put("tripConfirmation", assignRoutes.getTripConfirmationFromDriver());
				requests.put("routeType", assignRoutes.getRouteGenerationType());
				requests.put("isBackTwoBack", assignRoutes.getIsBackTwoBack());
				requests.put("plannedDis", assignRoutes.getPlannedDistance());

				requests.put("escortRequired", assignRoutes.getEscortRequredFlag());
				requests.put("zoneRouteId",assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
				requests.put("routeType", assignRoutes.getRouteGenerationType());
				requests.put("vehicleType", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
				requests.put("transportNumber", assignRoutes.geteFmFmClientBranchPO().getTransportContactNumberForMsg());
				requests.put("tripActualAssignDate", dateFormat.format(assignRoutes.getTripAssignDate()));
				requests.put("shiftTime", shiftTimeFormater.format(assignRoutes.getShiftTime()));
				requests.put("vehicleStatus", assignRoutes.getVehicleStatus());
				requests.put("bucketStatus", assignRoutes.getBucketStatus());
				requests.put("tripStatus", assignRoutes.getTripStatus());
				requests.put("waypoints", waypoints);
				requests.put("baseLatLong", assignRoutes.geteFmFmClientBranchPO().getLatitudeLongitude());
				requests.put("routeId", assignRoutes.getAssignRouteId());
				requests.put("checkInId", assignRoutes.getEfmFmVehicleCheckIn().getCheckInId());
							
				requests.put("driverName", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				requests.put("driverNumber",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				requests.put("vehicleNumber",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				requests.put("vendorId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorId());
				requests.put("vehicleType", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
				requests.put("deviceNumber",
						assignRoutes.getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId());				
				requests.put("driverId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				requests.put("vehicleId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				requests.put("vehicleAvailableCapacity",
						(assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity() - 1)
								- employeeTripDetailPO.size());
				requests.put("capacity", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity());

				requests.put("empDetails", tripAllDetails);
				requests.put("routeName", assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
		//		requests.put("totalNumberOfEmployee", totalNumberOfEmployee);
				allRoutes.add(requests);
			}
        }

   	 	log.info("serviceEnd -UserId :" + travelRequestPO.getUserId());
        return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
    }
    
    
    /*
     * Function For getting all open routes of particular shift and Date
     * 
     * @parameter is date, shift time and trip type as a request parameter 
     * 
     * @author Sarfraz Khan
     * 
     * @since 2017-02-02
     */
    
    @POST
    @Path("/openRoutes")
    @Produces("application/json")
    public Response getAllOpenRoutes(EFmFmEmployeeTravelRequestPO travelRequestPO) throws ParseException, UnsupportedEncodingException {
        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequestPO.getUserId()))){
        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(travelRequestPO.getUserId());
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
        log.info("serviceStart -UserId :" + travelRequestPO.getUserId());
        DateFormat shiftTimeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat shiftTimeFormate = new SimpleDateFormat("HH:mm");
        String shiftTime = travelRequestPO.getTime();
        Date shift = shiftTimeFormate.parse(shiftTime);
        java.sql.Time dateShiftTime = new java.sql.Time(shift.getTime());
        Date excutionDate = dateformate.parse(travelRequestPO.getExecutionDate());
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
        List<EFmFmAssignRoutePO> activeRoutes = iAssignRouteBO.getOpenRouteDetailFromAssignRouteId(excutionDate, excutionDate,
                travelRequestPO.getTripType(), shiftTimeFormat.format(dateShiftTime), travelRequestPO.getCombinedFacility());
        log.info("total routes"+activeRoutes.size());
        if (!(activeRoutes.isEmpty())) {
        	for(EFmFmAssignRoutePO assignRoutes:activeRoutes){
				List<Map<String, Object>> tripAllDetails = new ArrayList<Map<String, Object>>();
				Map<String, Object> requests = new HashMap<String, Object>();
				StringBuffer waypoints = new StringBuffer();
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
				if (assignRoutes.getTripType().equalsIgnoreCase("PICKUP")) {
					employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployees(assignRoutes.getAssignRouteId());
					 if (!(employeeTripDetailPO.isEmpty())) {
							requests.put("dropPickAreaName", employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());

					 }
				} else {
					employeeTripDetailPO = iCabRequestBO.getDropTripAllSortedEmployees(assignRoutes.getAssignRouteId());
                      if (!(employeeTripDetailPO.isEmpty())) {
							requests.put("dropPickAreaName", employeeTripDetailPO.get(employeeTripDetailPO.size()-1).geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
					 }
				}
				requests.put("numberOfEmployees", employeeTripDetailPO.size());
				requests.put("assignRouteId", assignRoutes.getAssignRouteId());
				requests.put("tripType", assignRoutes.getTripType());
				if (assignRoutes.getEscortRequredFlag().equalsIgnoreCase("Y")) {
					try {
						int escortId = assignRoutes.geteFmFmEscortCheckIn().getEscortCheckInId();
						log.info("escortId" + escortId);
						requests.put("escortName",
								assignRoutes.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
						requests.put("escortId", assignRoutes.geteFmFmEscortCheckIn().getEscortCheckInId());
					} catch (Exception e) {
						requests.put("escortId", "N");
						requests.put("escortName", "Escort Required But Not Available");
					}
				} else {
					requests.put("escortName", "Not Required");
				}
				try {					
					if(assignRoutes.getIsBackTwoBack().equalsIgnoreCase("N")){
				        List<EFmFmAssignRoutePO> b2bDetails=iAssignRouteBO.getBackToBackTripDetailFromb2bId(assignRoutes.getAssignRouteId(), "DROP",travelRequestPO.getCombinedFacility());
						if(!(b2bDetails.isEmpty())){
				        requests.put("suggestiveVehicleNumber",b2bDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						requests.put("backTwoBackRouteId",   b2bDetails.get(0).getAssignRouteId());
						}else{
						requests.put("suggestiveVehicleNumber","No");
						}
					}else{
						if(assignRoutes.getIsBackTwoBack().equalsIgnoreCase("Y")){
					        List<EFmFmAssignRoutePO> b2bRouteDetails=iAssignRouteBO.getParticularRouteDetailFromAssignRouteId(assignRoutes.getBackTwoBackRouteId());
									if(assignRoutes.getTripType().equalsIgnoreCase("DROP")){
							        	requests.put("suggestiveVehicleNumber","B2b with Route Id "+b2bRouteDetails.get(0).getAssignRouteId()+","+shiftTimeFormater.format(b2bRouteDetails.get(0).getShiftTime()) + " and Route Name:"+b2bRouteDetails.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
									}
									else{																				
										requests.put("suggestiveVehicleNumber",b2bRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
										requests.put("backTwoBackRouteId",   assignRoutes.getBackTwoBackRouteId());
									}
										
//								}
							
						}
						}
					
				} catch (Exception e) {
					log.info("Back2backShiftTime error" + e);
				}
				
				requests.put("routeType", assignRoutes.getRouteGenerationType());
				requests.put("isBackTwoBack", assignRoutes.getIsBackTwoBack());
				requests.put("plannedDis", assignRoutes.getPlannedDistance());

				requests.put("escortRequired", assignRoutes.getEscortRequredFlag());
				requests.put("zoneRouteId",assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
				requests.put("routeType", assignRoutes.getRouteGenerationType());
				requests.put("vehicleType", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
				requests.put("transportNumber", assignRoutes.geteFmFmClientBranchPO().getTransportContactNumberForMsg());
				requests.put("tripActualAssignDate", dateFormat.format(assignRoutes.getTripAssignDate()));
				requests.put("shiftTime", shiftTimeFormater.format(assignRoutes.getShiftTime()));
				requests.put("vehicleStatus", assignRoutes.getVehicleStatus());
				requests.put("bucketStatus", assignRoutes.getBucketStatus());
				requests.put("tripStatus", assignRoutes.getTripStatus());
				requests.put("waypoints", waypoints);
				requests.put("baseLatLong", assignRoutes.geteFmFmClientBranchPO().getLatitudeLongitude());
				requests.put("routeId", assignRoutes.getAssignRouteId());
				requests.put("checkInId", assignRoutes.getEfmFmVehicleCheckIn().getCheckInId());
							
				requests.put("driverName", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				requests.put("driverNumber",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				requests.put("vehicleNumber",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				requests.put("vendorId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorId());
				requests.put("vehicleType", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
				requests.put("deviceNumber",
						assignRoutes.getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId());				
				requests.put("driverId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				requests.put("vehicleId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				requests.put("vehicleAvailableCapacity",
						(assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity() - 1)
								- employeeTripDetailPO.size());
				requests.put("capacity", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity());

				requests.put("empDetails", tripAllDetails);
				requests.put("routeName", assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				allRoutes.add(requests);
			}
        }
   	 	log.info("serviceEnd -UserId :" + travelRequestPO.getUserId());
        return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
    }
    
    
    /*
	 * 
	 * Update Vehicle numbers to all routes Bulk close
	 * 
	 */

	@POST
	@Path("/bulkRouteClose")
	public Response updateSuggestedVehicleEntry(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		final IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		IVehicleCheckInBO vehicleCheckIn = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		
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
		log.info("serviceStart -UserId :" + assignRoutePO.getBulkRouteIds());
		

		StringTokenizer routeIds = new StringTokenizer(assignRoutePO.getBulkRouteIds(), ",");
		boolean dummyVehicle=true;
outer:		while (routeIds.hasMoreElements()) {
			log.info("routeIds" + routeIds);
			int routeId=Integer.parseInt(routeIds.nextElement().toString());
			assignRoutePO.setAssignRouteId(routeId);
			final List<EFmFmAssignRoutePO> assignRoutes = assignRouteBO
					.getParticularRouteDetailFromAssignRouteId(routeId);
			if(!(assignRoutes.isEmpty()) && assignRoutes.get(0).geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP") && !(assignRoutes.get(0).getRoutingCompletionStatus().equalsIgnoreCase("completed"))){		
				responce.put("status", "Sorry you can not close route ,Please press the complete routing first.");
				 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}

			assignRoutes.get(0).setTripUpdateTime(new Date());
			assignRoutes.get(0).setPlannedReadFlg("N");
			assignRoutes.get(0).setScheduleReadFlg("N");
			final MessagingService messagingService = new MessagingService();
			final PushNotificationService pushNotification = new PushNotificationService();
			final int branchId = assignRoutePO.getBranchId();
			List<EFmFmEmployeeTripDetailPO> finalTripEmployees = null;
			log.info("assignRoutes" + assignRoutePO.getAssignRouteId());
			try {
				List<EFmFmAssignRoutePO> todayRoutesCheck = assignRouteBO.getDuplicateTripAllocationCheck(
						assignRoutes.get(0).getEfmFmVehicleCheckIn().getCheckInId(), assignRoutePO.getCombinedFacility());
				if (!todayRoutesCheck.isEmpty() && todayRoutesCheck.get(0).getShiftTime().getTime() == assignRoutes
						.get(0).getShiftTime().getTime()) {
					responce.put("status", "Vehicle Number-"+assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber()+" already allocated to same shift,you can not allocate a second trip to same Vehicle");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
				
				else if(assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber().contains("DUMMY")){
					dummyVehicle=false;
					continue outer;
					//return Response.ok(responce, MediaType.APPLICATION_JSON).build();

				}
			} catch (Exception e) {
				log.info("duplicate trip allocation erroe" + e);
			}

			if (assignRoutes.get(0).getRouteGenerationType().equalsIgnoreCase("nodalAdhoc")) {
				assignRoutes.get(0).setBucketStatus("Y");
				assignRoutes.get(0).setVehicleStatus("F");
				assignRouteBO.update(assignRoutes.get(0));
				responce.put("availableCapacity",
						((assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity() - 1)));
				responce.put("status", "success");
				log.info(" First serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			if (assignRoutes.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
				finalTripEmployees = iCabRequestBO
						.getParticularTripAllEmployees(assignRoutes.get(0).getAssignRouteId());
				if ((assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()
						- 1) == finalTripEmployees.size()
						&& assignRoutes.get(0).getEscortRequredFlag().equalsIgnoreCase("N")
						&& new String(Base64.getDecoder()
								.decode(finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getGender()),
								"utf-8").equalsIgnoreCase("Female")
						&& (finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getShiftTime().getHours() >= 20
								|| finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getShiftTime()
										.getHours() == 0
								|| finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getShiftTime()
										.getHours() <= 7)) {
					responce.put("status", "Sorry you can not close "+assignRoutes.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName()+ " route because first pickup is female and escort is mandatory.");
					log.info("Second serviceEnd -UserId :" + assignRoutePO.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
				else if ((assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()
						- 1) > finalTripEmployees.size()
						&& assignRoutes.get(0).getEscortRequredFlag().equalsIgnoreCase("N")
						&& new String(Base64.getDecoder()
								.decode(finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getGender()),
								"utf-8").equalsIgnoreCase("Female")
						&& (finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getShiftTime().getHours() >= 20
								|| finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getShiftTime()
										.getHours() == 0
								|| finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getShiftTime()
										.getHours() <= 7)) {
					responce.put("status", "Sorry you can not close "+assignRoutes.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName()+ " route because first pickup is female and escort is mandatory.");
					log.info("Second serviceEnd -UserId :" + assignRoutePO.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
				
				EFmFmVehicleMasterPO vehicleMaster = vehicleCheckIn.getParticularVehicleDetail(
						assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				if (assignRoutes.get(0).getAllocationMsg().equalsIgnoreCase("N")) {
					Thread thread1 = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								ICabRequestBO iCabRequestBO1 = (ICabRequestBO) ContextLoader.getContext()
										.getBean("ICabRequestBO");
								List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO1
										.getParticularTripAllEmployees(assignRoutes.get(0).getAssignRouteId());
								for (EFmFmEmployeeTripDetailPO tripDetailPO : employeeTripDetailPO) {
									String text = "Vehicle number "
											+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													.getVehicleNumber()
											+ " "
											+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													.getVehicleModel()
											+ " has been assigned for your pickup for "
											+ shiftTimeFormater.format(assignRoutes.get(0).getShiftTime())
											+ " shift.\nBoarding Time is "
											+ shiftTimeFormater.format(
													tripDetailPO.geteFmFmEmployeeTravelRequest().getPickUpTime())
											+ "\nFor feedback write to us @"
											+ assignRoutes.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();
									if (tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType()
											.equalsIgnoreCase("guest")) {
										String coPassengers = "";
										if (employeeTripDetailPO.size() > 1) {
											List<EFmFmEmployeeTripDetailPO> coPassengerList = iCabRequestBO1
													.getCoPassenger(assignRoutes.get(0).getAssignRouteId(), tripDetailPO
															.geteFmFmEmployeeTravelRequest().getRequestId());
											for (EFmFmEmployeeTripDetailPO passenger : coPassengerList) {
												coPassengers += new String(Base64.getDecoder()
														.decode(passenger.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getFirstName()),
														"utf-8") + " and ";
											}
											coPassengers = coPassengers.substring(0, coPassengers.lastIndexOf("and"));
											log.info("coPassengers" + coPassengers);
										} else {
											coPassengers = "None";
										}
										String hostText = "Dear Host,Your guest "
												+ new String(Base64.getDecoder()
														.decode(tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getFirstName()),
														"utf-8")
												+ " pickup details,Cab No-"
												+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
														.getVehicleNumber()
												+ "\nDriver-"
												+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
														.getFirstName()
												+ " Mobile- "
												+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
														.getMobileNumber()
												+ "\nGuest reporting time is-"
												+ shiftTimeFormater.format(
														tripDetailPO.geteFmFmEmployeeTravelRequest().getPickUpTime())
												+ ".His co-passengers are " + coPassengers + "\nRegards SBOT +"
												+ assignRoutes.get(0).geteFmFmClientBranchPO()
														.getTransportContactNumberForMsg();
										text = "Dear Guest,Your pickup details,Cab No-"
												+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
														.getVehicleNumber()
												+ "\nDriver-"
												+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
														.getFirstName()
												+ " Mobile- "
												+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
														.getMobileNumber()
												+ ".\nyour reporting time is-"
												+ shiftTimeFormater.format(
														tripDetailPO.geteFmFmEmployeeTravelRequest().getPickUpTime())
												+ ".Your co-passengers are " + coPassengers + "\nRegards SBOT +"
												+ assignRoutes.get(0).geteFmFmClientBranchPO()
														.getTransportContactNumberForMsg();
										messagingService.sendMessageToGuest(
												new String(
														Base64.getDecoder()
																.decode(tripDetailPO.geteFmFmEmployeeTravelRequest()
																		.getEfmFmUserMaster().getMobileNumber()),
														"utf-8"),
												text, tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType());

										StringTokenizer token = new StringTokenizer(
												new String(
														Base64.getDecoder()
																.decode(tripDetailPO.geteFmFmEmployeeTravelRequest()
																		.getEfmFmUserMaster().getHostMobileNumber()),
														"utf-8"),
												",");
										while (token.hasMoreElements()) {
											messagingService.sendTripAsMessage(token.nextElement().toString(), hostText,
													tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType());
										}
										List<EFmFmUserMasterPO> hostDetails = userMasterBO
												.getEmployeeUserDetailFromMobileNumber(branchId,
														tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getHostMobileNumber());
										try {
											if (tripDetailPO.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
													.getDeviceType().contains("Android")) {
												pushNotification
														.notification(tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getDeviceToken(), text);
												if (!(hostDetails.isEmpty()))
													pushNotification.notification(hostDetails.get(0).getDeviceToken(),
															hostText);
											} else {
												pushNotification.iosPushNotification(
														tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getDeviceToken(),
														text);
												if (!(hostDetails.isEmpty()))
													pushNotification.iosPushNotification(
															hostDetails.get(0).getDeviceToken(), hostText);
											}

										} catch (Exception e) {
											log.info("PushStatus" + e);
										}
									} else {
										// EAP..Allocation message for Pickup
										// Guest
										// and Host
										try {
											if (tripDetailPO.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
													.getDeviceType().contains("Android")) {
												pushNotification
														.notification(tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getDeviceToken(), text);
											} else {
												pushNotification.iosPushNotification(
														tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getDeviceToken(),
														text);
											}
											log.info("notiE");
										} catch (Exception e) {
											log.info("PushStatus" + e);
										}
										messagingService.sendTripAsMessage(
												new String(
														Base64.getDecoder()
																.decode(tripDetailPO.geteFmFmEmployeeTravelRequest()
																		.getEfmFmUserMaster().getMobileNumber()),
														"utf-8"),
												text, tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType());
									}
								}
							} catch (Exception e) {
								log.info("Error" + e);
							}
						}
					});
					thread1.start();
					assignRoutes.get(0).setAllocationMsg("Y");
					assignRoutes.get(0).setAllocationMsgDeliveryDate(new Date());
				}
				if (assignRoutes.get(0).getEscortRequredFlag().equalsIgnoreCase("N")
						&& !(finalTripEmployees.isEmpty())) {
					if (new String(Base64.getDecoder()
							.decode(finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
									.getGender()),
							"utf-8").equalsIgnoreCase("Female")
							&& (finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getShiftTime()
									.getHours() >= 20
									|| finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getShiftTime()
											.getHours() == 0
									|| finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getShiftTime()
											.getHours() <= 7)) {
						List<EFmFmEscortCheckInPO> escortList = vehicleCheckIn.getAllCheckedInEscort(assignRoutePO.getCombinedFacility());
						if (!(escortList.isEmpty()) || escortList.size() != 0) {
							EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
							checkInEscort.setEscortCheckInId(escortList.get(0).getEscortCheckInId());
							assignRoutes.get(0).seteFmFmEscortCheckIn(checkInEscort);
							escortList.get(0).setStatus("N");
							vehicleCheckIn.update(escortList.get(0));
						}
						assignRoutes.get(0).setEscortRequredFlag("Y");
						assignRoutes.get(0).setVehicleStatus("F");
						assignRoutes.get(0).setBucketStatus("Y");
						assignRouteBO.update(assignRoutes.get(0));
						// vehicleMaster.setAvailableSeat(vehicleMaster.getAvailableSeat()-1);
						vehicleCheckIn.update(vehicleMaster);
					} else {
						assignRoutes.get(0).setBucketStatus("Y");
						assignRoutes.get(0).setVehicleStatus("F");
						assignRouteBO.update(assignRoutes.get(0));
					}
				} else {
					assignRoutes.get(0).setBucketStatus("Y");
					assignRoutes.get(0).setVehicleStatus("F");
					assignRouteBO.update(assignRoutes.get(0));
				}
			} else if (assignRoutes.get(0).getTripType().equalsIgnoreCase("DROP")) {
				log.info("drop call");
				finalTripEmployees = iCabRequestBO
						.getDropTripAllSortedEmployees(assignRoutes.get(0).getAssignRouteId());
				if ((assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()
						- 1) == finalTripEmployees.size()
						&& assignRoutes.get(0).getEscortRequredFlag()
								.equalsIgnoreCase(
										"N")
						&& new String(
								Base64.getDecoder()
										.decode(finalTripEmployees.get(finalTripEmployees.size() - 1)
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),
								"utf-8").equalsIgnoreCase("Female")
						&& (finalTripEmployees.get(finalTripEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
								.getShiftTime().getHours() >= 19
								|| finalTripEmployees.get(finalTripEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
										.getShiftTime().getHours() == 0
								|| finalTripEmployees.get(finalTripEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
										.getShiftTime().getHours() < 7)) {
//					responce.put("status", "notClose");
					responce.put("status", "Sorry you can not close "+assignRoutes.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName()+" bucket because last drop is female and escort is mandatory.");
					log.info(" Third serviceEnd -UserId :" + assignRoutePO.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
				else if ((assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()
						- 1) > finalTripEmployees.size()
						&& assignRoutes.get(0).getEscortRequredFlag()
								.equalsIgnoreCase(
										"N")
						&& new String(
								Base64.getDecoder()
										.decode(finalTripEmployees.get(finalTripEmployees.size() - 1)
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),
								"utf-8").equalsIgnoreCase("Female")
						&& (finalTripEmployees.get(finalTripEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
								.getShiftTime().getHours() >= 19
								|| finalTripEmployees.get(finalTripEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
										.getShiftTime().getHours() == 0
								|| finalTripEmployees.get(finalTripEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
										.getShiftTime().getHours() < 7)) {
//					responce.put("type", "Sorry you can not close "+assignRoutes.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName()+" bucket because last drop is female and escort is mandatory.");
//					responce.put("status", "notClose");
					responce.put("status", "Sorry you can not close "+assignRoutes.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName()+" bucket because last drop is female and escort is mandatory.");
					log.info(" Third serviceEnd -UserId :" + assignRoutePO.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
				EFmFmVehicleMasterPO vehicleMaster = vehicleCheckIn.getParticularVehicleDetail(
						assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				if (assignRoutes.get(0).getAllocationMsg().equalsIgnoreCase("N")) {
					Thread thread1 = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								ICabRequestBO iCabRequestBO1 = (ICabRequestBO) ContextLoader.getContext()
										.getBean("ICabRequestBO");
								List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO1
										.getDropTripAllSortedEmployees(assignRoutes.get(0).getAssignRouteId());
								for (EFmFmEmployeeTripDetailPO tripDetailPO : employeeTripDetailPO) {
									String text = "Vehicle number "
											+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													.getVehicleNumber()
											+ " "
											+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													.getVehicleModel()
											+ " has been assigned for your drop for "
											+ assignRoutes.get(0).getShiftTime() + " shift.For feedback write to us @"
											+ assignRoutes.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();
									if (tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType()
											.equalsIgnoreCase("guest")) {
										String coPassengers = "";
										if (employeeTripDetailPO.size() > 1) {
											List<EFmFmEmployeeTripDetailPO> coPassengerList = iCabRequestBO1
													.getCoPassenger(assignRoutes.get(0).getAssignRouteId(), tripDetailPO
															.geteFmFmEmployeeTravelRequest().getRequestId());

											for (EFmFmEmployeeTripDetailPO passenger : coPassengerList) {
												coPassengers = new String(Base64.getDecoder()
														.decode(passenger.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getFirstName()),
														"utf-8") + " and ";
											}
											coPassengers = coPassengers.substring(0, coPassengers.lastIndexOf("and"));
											log.info("coPassengers" + coPassengers);

										} else {
											coPassengers = "None";
										}
										String hostText = "Dear Host,Your guest "
												+ new String(Base64.getDecoder()
														.decode(tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getFirstName()))
												+ " drop details,Cab No-"
												+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
														.getVehicleNumber()
												+ "\nDriver-"
												+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
														.getFirstName()
												+ " Mobile- "
												+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
														.getMobileNumber()
												+ "\nGuest reporting time is-"
												+ shiftTimeFormater.format(
														tripDetailPO.geteFmFmEmployeeTravelRequest().getShiftTime())
												+ ".His co-passengers are " + coPassengers + "\nRegards SBOT +"
												+ assignRoutes.get(0).geteFmFmClientBranchPO()
														.getTransportContactNumberForMsg();
										text = "Dear Guest,Your drop details,Cab No-"
												+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
														.getVehicleNumber()
												+ "\nDriver-"
												+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
														.getFirstName()
												+ " Mobile- "
												+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
														.getMobileNumber()
												+ "\nyour reporting time is-"
												+ shiftTimeFormater.format(
														tripDetailPO.geteFmFmEmployeeTravelRequest().getShiftTime())
												+ ".Your co-passengers are " + coPassengers + "\nRegards SBOT +"
												+ assignRoutes.get(0).geteFmFmClientBranchPO()
														.getTransportContactNumberForMsg();
										messagingService.sendMessageToGuest(
												new String(Base64.getDecoder()
														.decode(tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getMobileNumber())),
												text, tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType());
										StringTokenizer token = new StringTokenizer(
												new String(
														Base64.getDecoder()
																.decode(tripDetailPO.geteFmFmEmployeeTravelRequest()
																		.getEfmFmUserMaster().getHostMobileNumber()),
														"utf-8"),
												",");
										while (token.hasMoreElements()) {
											messagingService.sendTripAsMessage(token.nextElement().toString(), hostText,
													tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType());
										}

										// GHAP..Allocation message for Pickup
										// Guest
										// and Host
										List<EFmFmUserMasterPO> hostDetails = userMasterBO
												.getEmployeeUserDetailFromMobileNumber(branchId,
														tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getHostMobileNumber());
										try {
											if (tripDetailPO.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
													.getDeviceType().contains("Android")) {
												pushNotification
														.notification(tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getDeviceToken(), text);
												if ((!hostDetails.isEmpty()) && !(hostDetails.size() != 0))
													pushNotification.notification(hostDetails.get(0).getDeviceToken(),
															hostText);
											} else {
												pushNotification.iosPushNotification(
														tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getDeviceToken(),
														text);
												if ((!hostDetails.isEmpty()) && !(hostDetails.size() != 0))
													pushNotification.iosPushNotification(
															hostDetails.get(0).getDeviceToken(), hostText);
											}

										} catch (Exception e) {
											log.info("PushStatus" + e);
										}
									} else {
										// EAP..Allocation message for drop
										// Guest
										// and Host
										try {
											if (tripDetailPO.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
													.getDeviceType().contains("Android")) {
												pushNotification
														.notification(tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getDeviceToken(), text);
											} else {
												pushNotification.iosPushNotification(
														tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getDeviceToken(),
														text);

											}
											log.info("notiE");
										} catch (Exception e) {
											log.info("PushStatus" + e);
										}
										messagingService.sendTripAsMessage(
												new String(Base64.getDecoder()
														.decode(tripDetailPO.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getMobileNumber())),
												text, tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType());
									}
								}
							} catch (Exception e) {
								log.info("error" + e);
							}
						}
					});
					thread1.start();
					assignRoutes.get(0).setAllocationMsg("Y");
					assignRoutes.get(0).setAllocationMsgDeliveryDate(new Date());
				}
				if (assignRoutes.get(0).getEscortRequredFlag().equalsIgnoreCase("N")) {
					if (new String(Base64.getDecoder()
							.decode(finalTripEmployees.get(finalTripEmployees.size() - 1)
									.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),
							"utf-8").equalsIgnoreCase("Female")
							&& (finalTripEmployees.get(finalTripEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
									.getShiftTime().getHours() >= 19
									|| finalTripEmployees.get(finalTripEmployees.size() - 1)
											.geteFmFmEmployeeTravelRequest().getShiftTime().getHours() == 0
									|| finalTripEmployees.get(finalTripEmployees.size() - 1)
											.geteFmFmEmployeeTravelRequest().getShiftTime().getHours() < 7)) {
						List<EFmFmEscortCheckInPO> escortList = vehicleCheckIn.getAllCheckedInEscort(assignRoutePO.getCombinedFacility());
						if (!(escortList.isEmpty()) || escortList.size() != 0) {
							EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
							checkInEscort.setEscortCheckInId(escortList.get(0).getEscortCheckInId());
							// escortName=escortList.get(0).geteFmFmEscortMaster().getFirstName();
							assignRoutes.get(0).seteFmFmEscortCheckIn(checkInEscort);
							escortList.get(0).setStatus("N");
							vehicleCheckIn.update(escortList.get(0));
						}
						assignRoutes.get(0).setEscortRequredFlag("Y");
						assignRoutes.get(0).setVehicleStatus("F");
						assignRoutes.get(0).setBucketStatus("Y");
						assignRouteBO.update(assignRoutes.get(0));
						// vehicleMaster.setAvailableSeat(vehicleMaster.getAvailableSeat()-1);
						vehicleCheckIn.update(vehicleMaster);
					} else {
						assignRoutes.get(0).setBucketStatus("Y");
						assignRoutes.get(0).setVehicleStatus("F");
						assignRouteBO.update(assignRoutes.get(0));
					}
				} else {
					assignRoutes.get(0).setBucketStatus("Y");
					assignRoutes.get(0).setVehicleStatus("F");
					assignRouteBO.update(assignRoutes.get(0));

				}
			}
			List<EFmFmAssignRoutePO> assignRoutesDetail = assignRouteBO
					.getParticularRouteDetailFromAssignRouteId(routeId);
			EFmFmDriverMasterPO particularDriverDetails = approvalBO.getParticularDriverDetail(
					assignRoutesDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
			particularDriverDetails.setStatus("allocated");
			approvalBO.update(particularDriverDetails);

			List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckIn.getDeviceDetailsFromDeviceId(
					assignRoutesDetail.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
			deviceDetails.get(0).setStatus("allocated");
			vehicleCheckIn.update(deviceDetails.get(0));

			if (assignRoutesDetail.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
				try {
					int escortId = assignRoutes.get(0).geteFmFmEscortCheckIn().getEscortCheckInId();
					log.info("escortId" + escortId);
					responce.put("escortName",
							assignRoutes.get(0).geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
					responce.put("escortId", assignRoutes.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
				} catch (Exception e) {
					responce.put("escortId", "N");
					responce.put("escortName", "Escort Required But Not Available");
				}
			} else {
				responce.put("escortId", "N");
				responce.put("escortName", "Not Required");

			}
			responce.put("availableCapacity",
					((assignRoutesDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity() - 1)
							- finalTripEmployees.size()));
			try {
				List<EFmFmVehicleCheckInPO> checkInDetails = vehicleCheckIn
						.getCheckedInVehicleDetailsFromChecInAndBranchId(
								assignRoutesDetail.get(0).getEfmFmVehicleCheckIn().getCheckInId(), branchId);
				checkInDetails.get(0).setStatus("N");
				vehicleCheckIn.update(checkInDetails.get(0));
			} catch (Exception e) {
				log.info("error on bucket close update checkIn table" + e);
			}
		}
		if(!dummyVehicle)
		{
			responce.put("status", "You Can't close route with dummy vehicle please change vehicle number");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}
		responce.put("status", "Routes closed successfully");
		log.info(" Last serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
    
    /*
     * @Reading employee details from employee_master xl utility.
     * 
     * @Stored all the values on Arraylist.
     * 
     * @author Rajan R
     * 
     * @since 2015-05-12
     */
    
    @POST
    @Path("/exportRoute")
	@Produces("application/vnd.ms-excel") 
    public Response exporCreatedRoute(EFmFmEmployeeTravelRequestPO travelRequestPO) throws ParseException, UnsupportedEncodingException {
        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
        log.info("serviceStart -UserId :" + travelRequestPO.getUserId());
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//        try{        		
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequestPO.getUserId()))){
//        		responce.put("status", "invalidRequest");
//        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
//			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(travelRequestPO.getUserId());
//			   if (!(userDetail.isEmpty())) {
//				String jwtToken = "";
//				try {
//				 JwtTokenGenerator token = new JwtTokenGenerator();
//				 jwtToken = token.generateToken();
//				 userDetail.get(0).setAuthorizationToken(jwtToken);
//				 userDetail.get(0).setTokenGenerationTime(new Date());
//				 userMasterBO.update(userDetail.get(0));
//				} catch (Exception e) {
//				 log.info("error" + e);
//				}
//		   }
//			}catch(Exception e){
//        		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//        	}
        List<EFmFmEmployeeTripDetailPO> employeeTripList = null;
        String name = "os.name", filePath = "";
        boolean OsName = System.getProperty(name).startsWith("Windows");
        if (OsName) {
            filePath = SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER;
        } else {
            filePath = SERVER_UPLOAD_LINUX_LOCATION_FOLDER;
        }
        DateFormat shiftTimeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat shiftTimeFormate = new SimpleDateFormat("HH:mm");
        String shiftTime = travelRequestPO.getTime();
        Date shift = shiftTimeFormate.parse(shiftTime);
        java.sql.Time dateShiftTime = new java.sql.Time(shift.getTime());

 //       Date today = new Date();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Route Details");
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFCellStyle routetyle = workbook.createCellStyle();      
        
        Font routeFont = workbook.createFont();
        routeFont.setColor(IndexedColors.RED.getIndex());
        routetyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        routetyle.setFont(routeFont);
        
        
        style.setBorderTop((short) 6); // double lines border
        style.setBorderBottom((short) 1); // single line border
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(177, 162, 186)));
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(font);
        Date excutionDate = dateformate.parse(travelRequestPO.getExecutionDate());
        int rownum = 0, noOfRoute = 0;
        logs.info("route Excel");       
        List<EFmFmAssignRoutePO> activeRoutes = iAssignRouteBO.getExportTodayTrips(excutionDate, excutionDate,
                travelRequestPO.getTripType(), shiftTimeFormat.format(dateShiftTime), travelRequestPO.getCombinedFacility());
        if (!(activeRoutes.isEmpty())) {
            for (EFmFmAssignRoutePO activeRouteList : activeRoutes) {      	
            	
            	
                Row OutSiderow = sheet.createRow(rownum++);
                for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
                    sheet.autoSizeColumn(columnIndex);
                }
                noOfRoute++;
                Cell cell0 = OutSiderow.createCell(0);
                cell0.setCellValue(noOfRoute);
                cell0.setCellStyle(style);

                Cell routeIdHeading = OutSiderow.createCell(1);
                routeIdHeading.setCellValue("RouteId");
                routeIdHeading.setCellStyle(routetyle);

                Cell routeId = OutSiderow.createCell(2);
                routeId.setCellValue(activeRouteList.getAssignRouteId());
                routeId.setCellStyle(routetyle);

                Cell routeNameHeading = OutSiderow.createCell(3);
                routeNameHeading.setCellValue("RouteName");
                routeNameHeading.setCellStyle(routetyle);
                
                Cell routeName = OutSiderow.createCell(4);
                routeName.setCellValue(activeRouteList.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
                routeName.setCellStyle(routetyle);
                Cell shftTime = OutSiderow.createCell(5);
                shftTime.setCellValue("ShiftDateTime");
                shftTime.setCellStyle(routetyle);
                Cell shftTimeVal = OutSiderow.createCell(6);
                shftTimeVal.setCellValue(travelRequestPO.getExecutionDate() + " "
                        + shiftTimeFormat.format(activeRouteList.getShiftTime()));
                shftTimeVal.setCellStyle(routetyle);

                /* outSide Heading Start */
                Row outSideHeading = sheet.createRow(rownum++);
                Cell sNo = outSideHeading.createCell(0);
                sNo.setCellValue("S.No");
                sNo.setCellStyle(style);
                Cell empId = outSideHeading.createCell(1);
                empId.setCellValue("EmployeeId");
                empId.setCellStyle(style);
                Cell empName = outSideHeading.createCell(2);
                empName.setCellValue("EmployeeName");
                empName.setCellStyle(style);
                Cell empGender = outSideHeading.createCell(3);
                empGender.setCellValue("Gender");
                empGender.setCellStyle(style);
                Cell picKupTime = outSideHeading.createCell(4);
                if (travelRequestPO.getTripType().equalsIgnoreCase("PICKUP")) {
                    picKupTime.setCellValue("PickupTime");
                } else {
                    picKupTime.setCellValue("DropTime");
                }
                picKupTime.setCellStyle(style);
                Cell areaName = outSideHeading.createCell(5);
                areaName.setCellValue("AreaName");
                areaName.setCellStyle(style);
                Cell empAddress = outSideHeading.createCell(6);
                empAddress.setCellValue("EmployeeAddress");
                empAddress.setCellStyle(style);
                /* outSide Heading End */
                if (travelRequestPO.getTripType().equalsIgnoreCase("DROP")) {
                    employeeTripList = iCabRequestBO.getNonDropTripAllSortedEmployees(activeRouteList.getAssignRouteId());
                } else {
                    employeeTripList = iCabRequestBO.getParticularTripAllEmployees(activeRouteList.getAssignRouteId());
                }
                if (employeeTripList.size() > 0) {
                    int rowNum = 1;
                    for (EFmFmEmployeeTripDetailPO employeeList : employeeTripList) { 
                        Row insideRow = sheet.createRow(rownum++);
                        XSSFCellStyle cellColorStyle = workbook.createCellStyle();
                        
                        if(!employeeList.geteFmFmEmployeeTravelRequest().getRequestColor().equalsIgnoreCase("yellow")){                 
                            cellColorStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());   
                            cellColorStyle.setFillPattern(cellColorStyle.SOLID_FOREGROUND); 
                        }else{                
                           cellColorStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(236,227,227))); 
                           cellColorStyle.setFillPattern(cellColorStyle.SOLID_FOREGROUND); 
                        }
                        cellColorStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
                        cellColorStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
                        cellColorStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);	                        
                	    Font cellFont = workbook.createFont();
                	    cellFont.setColor(IndexedColors.BLACK.getIndex());
                	    cellColorStyle.setFont(cellFont);  
                	    
                        Cell rowNo = insideRow.createCell(0);             
                        rowNo.setCellValue(rowNum++);
                        rowNo.setCellStyle(cellColorStyle); 
                        Cell employeeId = insideRow.createCell(1);
                        if (employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
                                .getEmployeeId() instanceof String)
                            employeeId.setCellValue((String) employeeList.geteFmFmEmployeeTravelRequest()
                                    .getEfmFmUserMaster().getEmployeeId());
                        employeeId.setCellStyle(cellColorStyle);     
                        Cell employeeName = insideRow.createCell(2);
                        if (employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
                                .getFirstName() instanceof String)
                            employeeName.setCellValue(new String(Base64.getDecoder().decode((String) employeeList.geteFmFmEmployeeTravelRequest()
                                    .getEfmFmUserMaster().getFirstName()), "utf-8"));
                        employeeName.setCellStyle(cellColorStyle); 
                        Cell employeeGender = insideRow.createCell(3);
                        if (employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
                                .getGender() instanceof String)
                            employeeGender.setCellValue(new String(Base64.getDecoder().decode((String) employeeList.geteFmFmEmployeeTravelRequest()
                                    .getEfmFmUserMaster().getGender()), "utf-8"));
                        employeeGender.setCellStyle(cellColorStyle); 
                        Cell pickupTime = insideRow.createCell(4);
                        if (employeeList.geteFmFmEmployeeTravelRequest().getPickUpTime() instanceof Date) {
                            pickupTime.setCellValue((String) shiftTimeFormate
                                    .format(employeeList.geteFmFmEmployeeTravelRequest().getPickUpTime()));
                        } else {
                            pickupTime.setCellValue((String) "00:00");
                        }
                        pickupTime.setCellStyle(cellColorStyle); 
                        Cell employeeAreaName = insideRow.createCell(5);
                        if (employeeList.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping()
                                .getEfmFmAreaMaster().getAreaName() instanceof String)
                            employeeAreaName.setCellValue(
                                    (String) employeeList.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
                        employeeAreaName.setCellStyle(cellColorStyle);
                        Cell employeeAddress = insideRow.createCell(6);
                        
                        if (employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
                                .getAddress() instanceof String)
                        	try{
                        	if(activeRouteList.getRouteGenerationType().equalsIgnoreCase("nodal")){
                                employeeAddress.setCellValue(employeeList.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());
                        	}
                        	else{
                                employeeAddress.setCellValue(new String(Base64.getDecoder().decode((String) employeeList.geteFmFmEmployeeTravelRequest()
                                        .getEfmFmUserMaster().getAddress()), "utf-8"));
                        	}
                        	employeeAddress.setCellStyle(cellColorStyle);
                        	}catch(Exception e){
                                employeeAddress.setCellValue(new String(Base64.getDecoder().decode((String) employeeList.geteFmFmEmployeeTravelRequest()
                                        .getEfmFmUserMaster().getAddress()), "utf-8"));
                                employeeAddress.setCellStyle(cellColorStyle);
                        	}

                    }
                }

            }
        }      
     		StreamingOutput streamOutput = new StreamingOutput() {
     			@Override
     			public void write(OutputStream out) throws IOException, WebApplicationException {
     				workbook.write(out);
     			}			
     		};
     		ResponseBuilder response = Response.ok(streamOutput,
     				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
     		response.header("content-disposition", "attachment; filename=\"Report-" + 1 + "\".xlsx");
     		return response.build();   
             
   }


    /*
     * Assign Cab pop up Inputs
     */
    @POST
    @Path("/algoPopUpInputs")
    @Consumes("application/json")
    @Produces("application/json")
    public Response algoPopUpInputs(EFmFmEmployeeTravelRequestPO travelRequestPO) throws ParseException {
        IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        log.info("serviceStart -UserId :" + travelRequestPO.getUserId());
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequestPO.getUserId()))){
        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(travelRequestPO.getUserId());
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
        List<Map<String, Object>> availableVehicleDetails = new ArrayList<Map<String, Object>>();
        DateFormat shiftTimeFormate = new SimpleDateFormat("HH:mm");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = inputFormat.parse(travelRequestPO.getExecutionDate());
        String convertedCurrentDate = outputFormat.format(date);
        String shiftTime = travelRequestPO.getTime();
        Date shift = shiftTimeFormate.parse(shiftTime);
        java.sql.Time dateShiftTime = new java.sql.Time(shift.getTime());
        List<EFmFmEmployeeTravelRequestPO> noOfEmployee = iCabRequestBO.listOfTravelRequestByShiftTripType(
                travelRequestPO.getBranchId(), dateShiftTime, travelRequestPO.getTripType(), convertedCurrentDate);
        List<EFmFmVehicleMasterPO> listOfAvailableVehicles = iVehicleCheckInBO
                .getCheckInVehicle(travelRequestPO.getBranchId(), new Date());
        if ((!(listOfAvailableVehicles.isEmpty())) || listOfAvailableVehicles.size() != 0) {
            for (EFmFmVehicleMasterPO VehicleList : listOfAvailableVehicles) {
                Map<String, Object> vehicleDetails = new HashMap<String, Object>();
                if (VehicleList.getCapacity() == 6 && VehicleList.getNoOfVehicles() != 0) {
                    List<Map<String, Object>> capacityVehicleDetails = new ArrayList<Map<String, Object>>();
                    for (int noofVehicle = 0; noofVehicle <= VehicleList.getNoOfVehicles(); noofVehicle++) {
                        Map<String, Object> capacityList = new HashMap<String, Object>();
                        capacityList.put("valueList", noofVehicle);
                        capacityVehicleDetails.add(capacityList);
                    }
                    vehicleDetails.put("inova", capacityVehicleDetails);
                } else if (VehicleList.getCapacity() == 13 && VehicleList.getNoOfVehicles() != 0) {
                    List<Map<String, Object>> capacityVehicleDetails = new ArrayList<Map<String, Object>>();
                    for (int noofVehicle = 0; noofVehicle <= VehicleList.getNoOfVehicles(); noofVehicle++) {
                        Map<String, Object> capacityTempo = new HashMap<String, Object>();
                        capacityTempo.put("valueList", noofVehicle);
                        capacityVehicleDetails.add(capacityTempo);
                    }
                    vehicleDetails.put("tempo", capacityVehicleDetails);
                }
                vehicleDetails.put("employeeCount", noOfEmployee.size());
                availableVehicleDetails.add(vehicleDetails);
            }
        }
   	 log.info("serviceEnd -UserId :" + travelRequestPO.getUserId());
        return Response.ok(availableVehicleDetails, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/listOfFileNames")
    @Consumes("application/json")
    @Produces("application/json")
    public Response listOfFileNames(EFmFmEmployeeTravelRequestPO travelRequestPO) throws ParseException {
        List<Map<String, Object>> availableFileDetails = new ArrayList<Map<String, Object>>();
        log.info("serviceStart -UserId :" + travelRequestPO.getUserId());
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequestPO.getUserId()))){
        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(travelRequestPO.getUserId());
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
        String name = "os.name", filePath = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date TodayDate = new Date();
        boolean OsName = System.getProperty(name).startsWith("Windows");
        if (OsName) {
            filePath = SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER;
        } else {
            filePath = SERVER_UPLOAD_LINUX_LOCATION_FOLDER;
        }
        try {
            File folder = new File(filePath);
            File[] listOfFiles = folder.listFiles();
            Arrays.asList(listOfFiles);
            List listFile = Arrays.asList(listOfFiles);
            Collections.sort(listFile, Collections.reverseOrder());
            if (listOfFiles.length > 0) {
                for (int fileCount = 0; fileCount < listOfFiles.length; fileCount++) {
                    Map<String, Object> fileDetails = new HashMap<String, Object>();
                    if (listOfFiles[fileCount].isFile()) {
                        if (listOfFiles[fileCount].getName().contains(dateFormat.format(TodayDate))) {
                            fileDetails.put("reportName", listOfFiles[fileCount].getName());
                            availableFileDetails.add(fileDetails);
                        } else {
                            (new File(listOfFiles[fileCount].getName())).delete();
                        }
                    }

                }
            }
        } catch (Exception e) {
        }
   	 log.info("serviceEnd -UserId :" + travelRequestPO.getUserId());
        return Response.ok(availableFileDetails, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/readCreatedRoute")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response upload_bak(@FormDataParam("filename") InputStream uploadedInputStream,
            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
            @Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException, InvalidKeyException,
                    NoSuchAlgorithmException, URISyntaxException {

        List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
        String routeIdRow = "", employeeIdRow = "", issueText = "", result = "";
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),userId))){
        		log.info(" inside authentication if"+httpRequest.getHeader("authenticationToken"));
        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(userId);
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
        int row_end = 0;
        log.info("serviceStart -UserId :" +userId);
        if (fileDetail.getFileName().endsWith("xlsx")) {
            try {
                System.out.println("fileName" + fileDetail.getFileName());
                Map<Integer, Object> vehicleDetails = new HashMap<Integer, Object>();
                int rowId = 0;
                XSSFWorkbook workbook = new XSSFWorkbook(uploadedInputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    ArrayList<Object> columnValue = new ArrayList<Object>();
                    Row row = rowIterator.next();
                    rowId++;
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Map<String, Object> valueCheck = new HashMap<String, Object>();
                        Cell cell = cellIterator.next();
                        switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            columnValue.add(cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            columnValue.add(cell.getStringCellValue());
                            result = xlvalidatior(routeIdRow, cell.getStringCellValue().toString().trim(),
                                    cell.getColumnIndex(), row_end);
                            if (!"success".equalsIgnoreCase(result)) {
                                valueCheck.put(Integer.toString(rowId), result);
                                response.add(valueCheck);
                            }
                            routeIdRow = cell.getStringCellValue().toString().trim();
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                Date date = DateUtil.getJavaDate((double) cell.getNumericCellValue());
                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                result = xlvalidatior(routeIdRow, df.format(date), cell.getColumnIndex(), row_end);
                                columnValue.add(df.format(date));
                                routeIdRow = Double.toString(cell.getNumericCellValue());
                                if (!"success".equalsIgnoreCase(result)) {
                                    valueCheck.put(Integer.toString(rowId), result);
                                    response.add(valueCheck);
                                }
                                System.out.println(cell.getColumnIndex());
                            } else {
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                columnValue.add(cell.getStringCellValue().trim());
                                result = xlvalidatior(routeIdRow, cell.getStringCellValue().trim(),
                                        cell.getColumnIndex(), row_end);
                                routeIdRow = cell.getStringCellValue().toString().trim();
                                if ("RouteId".equalsIgnoreCase(cell.getStringCellValue().toString().trim())) {
                                    row_end = 1;
                                } else if ("EmployeeAddress"
                                        .equalsIgnoreCase(cell.getStringCellValue().toString().trim())) {
                                    row_end = 2;
                                }
                                if (!"success".equalsIgnoreCase(result)) {
                                    valueCheck.put(Integer.toString(rowId), result);
                                    response.add(valueCheck);
                                }
                            }
                            System.out.println(cell.getColumnIndex());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            columnValue.add(cell.getStringCellValue().toString().trim());
                            result = xlvalidatior(routeIdRow, cell.getStringCellValue().toString().trim(),
                                    cell.getColumnIndex(), row_end);
                            if (!"success".equalsIgnoreCase(result)) {
                                valueCheck.put(Integer.toString(rowId), result);
                                response.add(valueCheck);
                            }
                            routeIdRow = cell.getStringCellValue().toString().trim();
                            if ("RouteId".equalsIgnoreCase(cell.getStringCellValue().toString().trim())) {
                                row_end = 1;
                            } else
                                if ("EmployeeAddress".equalsIgnoreCase(cell.getStringCellValue().toString().trim())) {
                                row_end = 2;
                            }
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            columnValue.add("");
                            break;
                        default:
                            columnValue.add("");
                            break;
                        }
                    }
                    if (columnValue.size() > 1) {
                        vehicleDetails.put(rowId, columnValue);
                    }
                }
                log.info("Size" + vehicleDetails.size());
                if (response.isEmpty()) {
                    if (vehicleDetails.size() > 1) {
                        response = extractRecord(vehicleDetails, branchId,userId,String.valueOf(branchId));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        log.info("serviceEnd -UserId :" +userId);
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    public String xlvalidatior(String routeIdRow, String cellValue, int cellIndex, int row_end) {
        DateFormat dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat shiftFormate = new SimpleDateFormat("HH:mm:ss");
//        Date todayDate = new Date();
        String Status = "success", pickupTime = "";
        String regex="^[0-9]*$";
        try {
            if ("RouteId".equalsIgnoreCase(routeIdRow)) {            	
                if (cellValue == null 
                		|| cellValue.replace(".0"," ").trim().equalsIgnoreCase("")
                        || !cellValue.toString().trim().equalsIgnoreCase("New")) {                	
                    if (!cellValue.replace(".0"," ").trim().matches(regex)) {                    	
                        Status = "kindly check the RouteId values Incase new route is added  on excel ,routeId should be 'New' "+cellValue;
                    }
                }
                
            } else if ("RouteName".equalsIgnoreCase(routeIdRow)) {
                if (cellValue.toString() == null || cellValue.toString().replace(".0", " ").trim().equalsIgnoreCase("")
                        || cellValue.toString().isEmpty()) {
                    Status = "kindly check the RouteName values";
                }
            } else if ("ShiftDateTime".equalsIgnoreCase(routeIdRow)) {
                try {
                    if (cellValue.length() <= 10 && cellValue.length() < 15) {
                        Status = "kindly check the DateTime format DD-MM-YYYY HH:SS:MM ex:17-06-2016 14:00:00 1"
                                + cellValue;
                    }
//                    else if (date.parse(cellValue).before(date.parse(date.format(todayDate)))) {
//                        Status = "kindly check the DateTime format DD-MM-YYYY HH:SS:MM ex:17-06-2016 14:00:00 2"
//                                + cellValue;
//                    } 
                    
                    else {
                        java.sql.Time startTime = java.sql.Time.valueOf("00:00:00");
                        if (dateTime.parse(cellValue).getTime() == startTime.getTime()) {
                            Status = "kindly check the DateTime format DD-MM-YYYY HH:SS:MM ex:17-06-2016 14:00:00"
                                    + cellValue;
                        }
                    }
                } catch (Exception e) {
                    Status = "kindly check the DateTime format" + e;
                }

            } else if (row_end == 2) {
                if (cellIndex == 1) {
                    if (cellValue.toString() == null
                            || cellValue.toString().replace(".0", " ").trim().equalsIgnoreCase("")
                            || cellValue.toString().isEmpty()) {
                        Status = "kindly check the employee Id : " + cellValue;
                    }
                } else if (cellIndex == 3) {
                    if (cellValue.toString() == null
                            || cellValue.toString().replace(".0", " ").trim().equalsIgnoreCase("")
                            || cellValue.toString().isEmpty()) {
                        Status = "kindly check the employee sex : " + cellValue;
                    } else if (!"FEMALE".equalsIgnoreCase(cellValue.toString())
                            && !"MALE".equalsIgnoreCase(cellValue.toString())) {
                        Status = "kindly check the employee sex : " + cellValue;
                    }
                } else if (cellIndex == 4) {
                    if (cellValue.toString() == null
                            || cellValue.toString().replace(".0", " ").trim().equalsIgnoreCase("")
                            || cellValue.toString().isEmpty()) {
                        Status = "kindly check the employee pickup/Drop Time : " + cellValue;
                    } else if (cellValue.toString() != "") {
                        try {
                            if (cellValue.toString().trim().length() == 5) {
                                pickupTime = cellValue.toString().trim().concat(":00");
                            } else {
                                System.out.println("value" + cellValue.toString().trim());
                                pickupTime = shiftFormate.format(date_format.parse(cellValue.toString()));
                            }

                        } catch (Exception e) {
                            Status = "kindly check the employee pickup/Drop Time : " + e;
                        }
                    }

                }
            }

        } catch (Exception e) {
            logs.info("Error" + e);
            return Status;
        }

        return Status;
    }

    public List<Map<String, Object>> extractRecord(Map<Integer, Object> vehicleDetails, int branchId,int userId,String combinedBranchId)
            throws ParseException {
        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateFormat shiftFormate = new SimpleDateFormat("HH:mm:ss");
        EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
        List<Map<String, Object>> exportExcel = new ArrayList<Map<String, Object>>();
        Map<String, Object> issueList = new HashMap<String, Object>();
        eFmFmClientBranchPO.setBranchId(branchId);
        int routeId = 0, empCount = 0, avaiable_seat = 0, seq_count = 0, loop_Count = vehicleDetails.size();
        boolean route_executed = false, escort_req = false, update_route = false;
        String shiftTime = "", tripType = "", stauts = "success";
        try {
            for (Entry<Integer, Object> entry : vehicleDetails.entrySet()) {
                issueList = new HashMap<String, Object>();
                ArrayList xlvalues = (ArrayList) entry.getValue();
                loop_Count--;
                if ("RouteId".equalsIgnoreCase(xlvalues.get(1).toString().trim())) {
                    seq_count = 0;
                    if (route_executed && routeId != 0) {
                        stauts = vehicleAllocation(routeId, escort_req, empCount, tripType, avaiable_seat,
                                eFmFmClientBranchPO, shiftTime,userId,combinedBranchId);
                        routeId = 0;
                    }
                    if ("New".equalsIgnoreCase(xlvalues.get(2).toString().trim())) {
                        tripType = "";
 //                       Date shiftReq_Date = dateTime.parse(xlvalues.get(6).toString().trim());
                        int newCheckInId = addNewRoute(branchId, xlvalues.get(6).toString().trim(),
                                xlvalues.get(4).toString().trim());
                        if (newCheckInId != 0) {
                            List<EFmFmAssignRoutePO> assignCount = iCabRequestBO.getLastRouteDetails(newCheckInId,
                                    branchId, tripType);
                            if (!assignCount.isEmpty()) {
                                routeId = assignCount.get(0).getAssignRouteId();
                                tripType = assignCount.get(0).getTripType();
                                shiftTime = xlvalues.get(6).toString().trim();
                                avaiable_seat = assignCount.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
                                        .getCapacity();
                            }
                        }
                    }
                    if (xlvalues.get(2).toString() != null
                            && !xlvalues.get(2).toString().replace(".0", " ").trim().equalsIgnoreCase("")
                            && !xlvalues.get(2).toString().trim().equalsIgnoreCase("New")) {
                        int assignId = Integer.parseInt(xlvalues.get(2).toString().replace(".0", " ").trim());
                        System.out.println("routeId" + assignId);
                        List<EFmFmEmployeeTripDetailPO> noOfEmp = iCabRequestBO.getParticularTripAllEmployees(assignId);
                        if (noOfEmp.size() > 0) {
                            for (EFmFmEmployeeTripDetailPO tripDetails : noOfEmp) {
                                EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO = new EFmFmEmployeeTravelRequestPO();
                                eFmFmEmployeeTravelRequestPO.setBranchId(branchId);
                                eFmFmEmployeeTravelRequestPO
                                        .setRequestId(tripDetails.geteFmFmEmployeeTravelRequest().getRequestId());
                                List<EFmFmEmployeeTravelRequestPO> cabRequests = null;
                                cabRequests = iCabRequestBO
                                        .getparticularRequestwithShiftTime(eFmFmEmployeeTravelRequestPO);
                                System.out.println("Size" + cabRequests.size());
                                cabRequests.get(0).setReadFlg("Y");
                                iCabRequestBO.update(cabRequests.get(0));
                            }
                            avaiable_seat = noOfEmp.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
                                    .getEfmFmVehicleMaster().getCapacity();
                            tripType = noOfEmp.get(0).geteFmFmEmployeeTravelRequest().getTripType();
                            routeId = assignId;
                            shiftTime = xlvalues.get(6).toString().trim();
                            iCabRequestBO.deleteParticularTripDetailByRouteId(routeId);
                            route_executed = true;
                            escort_req = false;
                            empCount = 0;

                        } else {
                            EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
                            assignRoutePO.setAssignRouteId(assignId);
                            assignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
                            List<EFmFmAssignRoutePO> assignCount = iCabRequestBO
                                    .getParticularDriverAssignTripDetail(assignRoutePO);
                            if (assignCount.size() > 0) {
                                routeId = assignId;
                                tripType = assignCount.get(0).getTripType();
                                shiftTime = xlvalues.get(6).toString().trim();
                                avaiable_seat = assignCount.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
                                        .getCapacity();
                            }
                        }
                    }
                } else {
                    if (!xlvalues.get(1).toString().equalsIgnoreCase("EmployeeId") && routeId != 0) {
                        String employeeId = xlvalues.get(1).toString().replace(".0", "").trim();
                        String gender = xlvalues.get(3).toString().trim();
                        String pickupTime = "";
                        if (xlvalues.get(4).toString().trim().length() == 5) {
                            pickupTime = xlvalues.get(4).toString().trim().concat(":00");
                        } else {
                            System.out.println("value" + xlvalues.get(4).toString().trim());
                            pickupTime = shiftFormate.format(date_format.parse(xlvalues.get(4).toString()));
                        }
                        System.out.println("value -" + employeeId + " -- " + gender + " -- " + pickupTime);
                        stauts = employeeAllocate(routeId, employeeId, tripType, branchId, pickupTime, shiftTime,seq_count++);
                        empCount++;
                        if (!"success".equalsIgnoreCase(stauts)) {
                            String responce = routeId + "--" + employeeId + " -- " + gender + " -- " + pickupTime + "--"
                                    + stauts;
                            issueList.put("issueData", responce);
                            exportExcel.add(issueList);
                        }

                        if (tripType.equalsIgnoreCase("PICKUP") && empCount == 1 && gender.equalsIgnoreCase("FEMALE")) {
                            escort_req = true;
                        } else if (tripType.equalsIgnoreCase("DROP") && gender.equalsIgnoreCase("FEMALE")) {
                            escort_req = true;
                        } else {
                            escort_req = false;
                        }
                    } else {
                        if (xlvalues.get(4).toString().equalsIgnoreCase("PickupTime")) {
                            tripType = "PICKUP";
                        } else {
                            tripType = "DROP";
                        }
                        empCount = 0;
                    }
                    if (loop_Count == 0 && routeId != 0) {
                        stauts = vehicleAllocation(routeId, escort_req, empCount, tripType, avaiable_seat,
                                eFmFmClientBranchPO, shiftTime,userId,combinedBranchId);
                        routeId = 0;
                    }
                }
            }
        } catch (Exception e) {
            logs.info("Error" + e);
            String response = routeId + "kinldy check the employee time format for this route" + e;
            issueList.put("issueData", response);
            exportExcel.add(issueList);
        }
        return exportExcel;
    }

    public String employeeAllocate(int routeId, String employeeId, String tripType, int branchId, String pickupTime,
            String shiftTime, int seq_count) throws ParseException {
        String empAllocation = "success";
        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        EFmFmEmployeeTripDetailPO employeeTripDetailPO = new EFmFmEmployeeTripDetailPO();
        
        DateFormat shiftFormate = new SimpleDateFormat("HH:mm:ss");
        DateFormat shiftDateFormate = new SimpleDateFormat("dd-MM-yyyy");
        EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO = new EFmFmEmployeeTravelRequestPO();
        eFmFmEmployeeTravelRequestPO.setBranchId(branchId);
        List<EFmFmEmployeeTravelRequestPO> cabRequests = null;
        DateFormat dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date shiftReq_Date = dateTime.parse(shiftTime);
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = shiftDateFormate.parse(shiftDateFormate.format(shiftReq_Date));
            String convertedCurrentDate = outputFormat.format(date);
            EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
            EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO = new EFmFmRouteAreaMappingPO();
            eFmFmClientBranchPO.setBranchId(branchId);
            cabRequests = iCabRequestBO.getparticularEmployeeRequest(convertedCurrentDate, employeeId, tripType,
                    branchId, shiftFormate.format(shiftReq_Date));
            if (cabRequests.size() > 0) {
                EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
                assignRoutePO.setAssignRouteId(routeId);
                assignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
                if (tripType.equalsIgnoreCase("DROP")) {
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
                employeeTripDetailPO.seteFmFmEmployeeTravelRequest(cabRequests.get(0));
                employeeTripDetailPO.setEfmFmAssignRoute(assignRoutePO);
                employeeTripDetailPO.setCurrenETA("0");
                employeeTripDetailPO.setEmployeeStatus("allocated");
				employeeTripDetailPO.setComingStatus("Yet to confirm");
				employeeTripDetailPO.setEmployeeOnboardStatus("NO");

                List<EFmFmEmployeeTripDetailPO> allocationCount = iCabRequestBO.getparticularEmployeeTripDetails(
                        convertedCurrentDate, employeeId, tripType, branchId, shiftFormate.format(shiftReq_Date));
                if (allocationCount.size() > 0) {
                    allocationCount.get(0).getEmpTripId();
                    iCabRequestBO.deleteParticularTripDetail(allocationCount.get(0).getEmpTripId());
                }
                iCabRequestBO.update(employeeTripDetailPO);
                cabRequests.get(0).setReadFlg("R");
                cabRequests.get(0).setDropSequence(seq_count);
                java.sql.Time myTime = java.sql.Time.valueOf(pickupTime);
                cabRequests.get(0).setPickUpTime(myTime);
                iCabRequestBO.update(cabRequests.get(0));
            } else {
                empAllocation = "kinldy check the employee Id " + employeeId;
            }
        } catch (Exception e) {
            System.out.println("exception-Pcikup time xl sheet column Need to format the text" + e);
            empAllocation = "Exception pickup/Drop time xl sheet column Need to format the text" + e;
        }

        return empAllocation;

    }

    public String vehicleAllocation(int routeId, boolean escort_req, int empCount, String tripType, int avaiable_seat,
            EFmFmClientBranchPO eFmFmClientBranchPO, String shiftTime,int userId,String combinedBranchId) throws ParseException {
        String vehicleAllocation = "success";
        try {
            ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
            IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                    .getBean("IVehicleCheckInBO");
            IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
            boolean escort_required_time = false;
            DateFormat shiftFormate = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date shiftReq_Date = dateTime.parse(shiftTime);
            java.sql.Time currentShiftTime = java.sql.Time.valueOf(shiftFormate.format(shiftReq_Date));
            java.sql.Time startTime = java.sql.Time.valueOf("07:00:00");
            java.sql.Time Endtime = java.sql.Time.valueOf("19:00:00");
            EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
            assignRoutePO.setAssignRouteId(routeId);
            assignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
            if (startTime.getTime() >= currentShiftTime.getTime() || Endtime.getTime() <= currentShiftTime.getTime()) {
                escort_required_time = true;
            }
            if (empCount == 0) {
                travelStatusReset(routeId, eFmFmClientBranchPO);
                iAssignRouteBO.deleteParticularAssignRoute(routeId);
            } else {
                if (empCount > (avaiable_seat - 1)
                        || (empCount == (avaiable_seat - 1) && escort_required_time == true)) {
                    if (escort_required_time) {
                        empCount += empCount;
                    }
                    vehicleAllocation = changeVehicleByCapacity(empCount, eFmFmClientBranchPO, escort_required_time,
                            routeId, (avaiable_seat - 1),userId,combinedBranchId);
                    vehicleAllocation = travelStatusReset(routeId, eFmFmClientBranchPO);
                } else if (empCount == (avaiable_seat - 1) && escort_required_time == false) {
                    List<EFmFmAssignRoutePO> assignCount = iCabRequestBO
                            .getParticularDriverAssignTripDetail(assignRoutePO);
                    assignCount.get(0).setVehicleStatus("F");
                    iCabRequestBO.update(assignCount.get(0));
                } else if (empCount < (avaiable_seat - 1)) {
                    List<EFmFmEmployeeTripDetailPO> empCountCount = iCabRequestBO
                            .getParticularTripNonDropEmployeesDetails(routeId);
                    int employeeCounts = 0;
                    if (empCountCount.size() > 0) {
                        employeeCounts = empCountCount.size();
                        if (empCountCount.get(0).getEfmFmAssignRoute().getEscortRequredFlag().equalsIgnoreCase("Y")) {
                            employeeCounts = empCountCount.size() + 1;
                        }

                    }
                    List<EFmFmAssignRoutePO> capcity_Update = iCabRequestBO
                            .getParticularDriverAssignTripDetail(assignRoutePO);
                    if (capcity_Update.size() > 0) {
                        EFmFmVehicleMasterPO vehicleDetails = iCabRequestBO.getVehicleDetail(
                                capcity_Update.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
                        vehicleDetails.setStatus("A");
                        iVehicleCheckInBO.update(vehicleDetails);
                    }
                }
            }
        } catch (Exception e) {
        	log.info("error"+e);
            if (!vehicleAllocation.equalsIgnoreCase("success")) {
                vehicleAllocation = vehicleAllocation + e;
            } else {
                vehicleAllocation = "Exception happend while allocating the vehicle" + e;
            }
        }
        return vehicleAllocation;

    }

    public int addNewRoute(int branchId, String dateAndShifTime, String areaName) {
        int newRouteCreated = 0;
        try {
            IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
            IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                    .getBean("IVehicleCheckInBO");
            IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
            IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
            ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
            List<EFmFmVehicleCheckInPO> checkedInEntity = iVehicleCheckInBO
                    .getAllCheckedInVehicleForCreatingNewBucket(branchId);
            if ((checkedInEntity.isEmpty()) || checkedInEntity.size() == 0) {
                newRouteCreated = 0;
                return newRouteCreated;
            }
            
//            DateFormat shiftFormate = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat shiftFormate = new SimpleDateFormat("HH:mm:ss");
    		DateFormat requestDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

            Date shiftReq_Date = dateTime.parse(dateAndShifTime);
            shiftFormate.format(shiftReq_Date);
    		Date shift = shiftFormate.parse(shiftFormate.format(shiftReq_Date));

            java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
           

    		String requestDate = requestDateFormat.format(shiftReq_Date);

    		String requestDateAndTime = requestDate + " " + shiftTime;

            
            EFmFmAssignRoutePO assignRoute = new EFmFmAssignRoutePO();
            EFmFmVehicleCheckInPO vehicleCheckInPO = new EFmFmVehicleCheckInPO();
            vehicleCheckInPO.setCheckInId(checkedInEntity.get(0).getCheckInId());
            newRouteCreated = checkedInEntity.get(0).getCheckInId();
            checkedInEntity.get(0).setStatus("N");
            assignRoute.setEfmFmVehicleCheckIn(vehicleCheckInPO);
            List<EFmFmRouteAreaMappingPO> routeAreaId = iRouteDetailBO.getRouteAreaIdbyAreaName(areaName);
            if (!routeAreaId.isEmpty()) {
                EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
                routeAreaMapping.setRouteAreaId(routeAreaId.get(0).getRouteAreaId());
                assignRoute.seteFmFmRouteAreaMapping(routeAreaMapping);
            }
            
            
			assignRoute.setEscortRequredFlag("N");
			assignRoute.setAllocationMsg("N");
			assignRoute.setLocationFlg("N");
			assignRoute.setShiftTime(shiftTime);
			assignRoute.setTripStatus("allocated");
			assignRoute.setTripConfirmationFromDriver("Not Delivered");

            assignRoute.setRoutingCompletionStatus("Started");
            assignRoute.setSelectedBaseFacility(branchId);

            assignRoute.setAssignedVendorName("NA"); 

			assignRoute.setIsBackTwoBack("N");
			assignRoute.setDistanceUpdationFlg("Y");
			assignRoute.setPlannedReadFlg("Y");
			assignRoute.setScheduleReadFlg("Y");
			assignRoute.setRemarksForEditingTravelledDistance("NO");
			assignRoute.setEditDistanceApproval("NO");
			assignRoute.setRouteGenerationType("normal");			
			assignRoute.setCreatedDate(new Date());
			assignRoute.setTripAssignDate(requestDateTimeFormat.parse(requestDateAndTime));
			assignRoute.setTripUpdateTime(requestDateTimeFormat.parse(requestDateAndTime));
			assignRoute.setVehicleStatus("A");
			assignRoute.setBucketStatus("N");

            
            EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
            eFmFmClientBranchPO.setBranchId(branchId);
            assignRoute.seteFmFmClientBranchPO(eFmFmClientBranchPO);
            assignRoute.setTripType("");
            iVehicleCheckInBO.update(checkedInEntity.get(0));
            assignRouteBO.save(assignRoute);
            
            
            EFmFmVehicleMasterPO vehicleMaster = iCabRequestBO
                    .getVehicleDetail(checkedInEntity.get(0).getEfmFmVehicleMaster().getVehicleId());
            vehicleMaster.setStatus("allocated");
            iVehicleCheckInBO.update(vehicleMaster);
            EFmFmDriverMasterPO particularDriverDetails = approvalBO
                    .getParticularDriverDetail(checkedInEntity.get(0).getEfmFmDriverMaster().getDriverId());
            particularDriverDetails.setStatus("allocated");
            approvalBO.update(particularDriverDetails);
            List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
                    checkedInEntity.get(0).geteFmFmDeviceMaster().getDeviceId(), new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),eFmFmClientBranchPO.getCombinedFacility()));
            deviceDetails.get(0).setStatus("allocated");
            iVehicleCheckInBO.update(deviceDetails.get(0));
        } catch (Exception e) {
            logs.info("addNewRoute" + e);
        }
        return newRouteCreated;
    }

    public String travelStatusReset(int routeId, EFmFmClientBranchPO eFmFmClientBranchPO) {
        String travelStatusReset = "success";
        try {
            IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                    .getBean("IVehicleCheckInBO");
            ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
            IApprovalBO iApprovalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
            EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
            assignRoutePO.setAssignRouteId(routeId);
            assignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
            List<EFmFmAssignRoutePO> assignCount = iCabRequestBO.getParticularDriverAssignTripDetail(assignRoutePO);
            if (assignCount.size() > 0) {
                EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
                eFmFmVehicleCheckInPO.setCheckInId(assignCount.get(0).getEfmFmVehicleCheckIn().getCheckInId());
                List<EFmFmVehicleCheckInPO> vehicleCheckin = iVehicleCheckInBO
                        .getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
                vehicleCheckin.get(0).setStatus("Y");
                iVehicleCheckInBO.update(vehicleCheckin.get(0));
                List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
                        assignCount.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),
                        new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),eFmFmClientBranchPO.getCombinedFacility()));
                deviceDetails.get(0).setStatus("Y");
                iVehicleCheckInBO.update(deviceDetails.get(0));
                EFmFmVehicleMasterPO vehicleDetails = iCabRequestBO.getVehicleDetail(
                        assignCount.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
                vehicleDetails.setStatus("A");
                iVehicleCheckInBO.update(vehicleDetails);
                EFmFmDriverMasterPO driverDetails = iApprovalBO.getParticularDriverDetail(
                        assignCount.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
                driverDetails.setStatus("A");
                iApprovalBO.update(driverDetails);

                if (assignCount.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
 //                   EFmFmEscortMasterPO eFmFmEscortMasterPO = new EFmFmEscortMasterPO();
                    /*
                     * int
                     * escortCheckInId=assignCount.get(0).geteFmFmEscortCheckIn(
                     * ).getEscortCheckInId();
                     * iVehicleCheckInBO.update(eFmFmEscortCheckInPO); int
                     * escortId=assignCount.get(0).geteFmFmEscortCheckIn().
                     * geteFmFmEscortMaster().getEscortId();
                     * List<EFmFmEscortMasterPO>
                     * escortDetails=iVehicleCheckInBO.
                     * getParticularEscortDetails(eFmFmEscortMasterPO);
                     * escortDetails.get(0).setIsActive("Y");
                     * iVehicleCheckInBO.updateEscortDetails(escortDetails.get(0
                     * ));
                     */
                }

            }
        } catch (Exception e) {
            logs.info("addNewRoute" + e);
            travelStatusReset = "Exception happend at travelStatusReset" + e;
        }
        return travelStatusReset;

    }

    public String changeVehicleByCapacity(int employeeSeatCapacity, EFmFmClientBranchPO eFmFmClientBranchPO,
            boolean escortRequired, int routeId, int vehicleSeatCapacity,int userId,String combinedBranchId) {
        String VehicleByCapacity = "success";
        try {

            ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
            IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
            IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                    .getBean("IVehicleCheckInBO");
            EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
            assignRoutePO.setAssignRouteId(routeId);
            assignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
            int emp_no = 0;
            boolean escort_Required = false;
            List<EFmFmVehicleCheckInPO> allCheckInVehicles;
            allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLargeCapacity(eFmFmClientBranchPO.getCombinedFacility(),
                    employeeSeatCapacity);
            if (allCheckInVehicles.isEmpty()) {
                // Splitting two vehicles
                List<EFmFmEmployeeTripDetailPO> listOfEmployees = iCabRequestBO.getParticularTripAllEmployees(routeId);
                int emp_count = listOfEmployees.size();
                for (EFmFmEmployeeTripDetailPO emp_list : listOfEmployees) {
                    boolean new_route_update = false;
                    vehicleSeatCapacity--;
                    emp_no++;
                    emp_count--;
                    if (escortRequired) {
                        employeeSeatCapacity = employeeSeatCapacity - 1;
                        if (emp_list.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP") && emp_count == 1
                                && emp_list.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()
                                        .equalsIgnoreCase("FEMALE")) {
                            escort_Required = true;
                            vehicleSeatCapacity--;
                            if (vehicleSeatCapacity < 0) {
                                new_route_update = true;
                            }

                        } else if (emp_list.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")
                                && emp_list.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()
                                        .equalsIgnoreCase("FEMALE")
                                && vehicleSeatCapacity <= 1 || emp_count == 0) {
                            escort_Required = true;
                            if (vehicleSeatCapacity < 0) {
                                new_route_update = true;
                            }
                        } else {
                            escort_Required = false;
                        }

                    }

                    if (new_route_update == true || vehicleSeatCapacity < 0) {
                        if (emp_list.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")
                                && (employeeSeatCapacity - emp_no) > 0) {
                            allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLessCapacity(
                            		new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),eFmFmClientBranchPO.getCombinedFacility()), (employeeSeatCapacity - emp_no));
                            vehicleSeatCapacity = allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity();
                            int seat_Capacity = 1;
                            if (escortRequired == true && emp_list.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
                                    .getGender().equalsIgnoreCase("FEMALE")) {
                                seat_Capacity = 2;
                            }
                            employeeSeatCapacity = employeeSeatCapacity - emp_no;
                            emp_no = 0;
                            new_route_update = false;
                            routeId = VehicleAllocate(emp_list.geteFmFmEmployeeTravelRequest().getRequestId(),
                                    eFmFmClientBranchPO.getBranchId(), allCheckInVehicles, seat_Capacity,userId,combinedBranchId);

                        } else if (emp_list.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")
                                && (employeeSeatCapacity - emp_no) > 0) {
                            allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLessCapacity(
                            		new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),eFmFmClientBranchPO.getCombinedFacility()), (employeeSeatCapacity - emp_no));
                            vehicleSeatCapacity = allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity();
                            int seat_Capacity = 1;
                            if (escortRequired == true && emp_list.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
                                    .getGender().equalsIgnoreCase("FEMALE")) {
                                seat_Capacity = 2;
                            }
                            employeeSeatCapacity = employeeSeatCapacity - emp_no;
                            emp_no = 0;
                            new_route_update = false;
                            routeId = VehicleAllocate(emp_list.geteFmFmEmployeeTravelRequest().getRequestId(),
                                    eFmFmClientBranchPO.getBranchId(), allCheckInVehicles, seat_Capacity,userId,combinedBranchId);

                        }
                    }
                    EFmFmEmployeeTripDetailPO list = iCabRequestBO.ParticularTripDetail(emp_list.getEmpTripId());
                    EFmFmAssignRoutePO efmFmAssignRoute = new EFmFmAssignRoutePO();
                    efmFmAssignRoute.setAssignRouteId(routeId);
                    list.setEfmFmAssignRoute(efmFmAssignRoute);
                    iCabRequestBO.update(list);
                }
            }
            if (!allCheckInVehicles.isEmpty() && allCheckInVehicles.size() != 0) {
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
                        new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),eFmFmClientBranchPO.getCombinedFacility()));
                deviceDetails.get(0).setStatus("Y");
                iVehicleCheckInBO.update(deviceDetails.get(0));
                if (!allCheckInVehicles.isEmpty() && allCheckInVehicles.size() != 0) {
                    List<EFmFmAssignRoutePO> assignRoute = iCabRequestBO
                            .getParticularDriverAssignTripDetail(assignRoutePO);
                    EFmFmVehicleCheckInPO vehicleCheckInPO = new EFmFmVehicleCheckInPO();
                    vehicleCheckInPO.setCheckInId(
                            allCheckInVehicles.get(allCheckInVehicles.size() - 1)
                                    .getCheckInId());
                    EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
                    routeAreaMapping.setRouteAreaId(assignRoute.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
                    assignRoute.get(0).seteFmFmRouteAreaMapping(routeAreaMapping);
                    if (escortRequired == true) {
                        assignRoute.get(0).setEscortRequredFlag("Y");
                        List<EFmFmEscortCheckInPO> escortList = iCabRequestBO
                                .getAllCheckedInEscort(new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),eFmFmClientBranchPO.getCombinedFacility()));
                        if (!(escortList.isEmpty())) {
                            EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
                            checkInEscort.setEscortCheckInId(
                                    escortList.get(0).getEscortCheckInId());
                            assignRoute.get(0).seteFmFmEscortCheckIn(checkInEscort);
                            escortList.get(0).setStatus("N");
                            iVehicleCheckInBO.update(escortList.get(0));
                        }
                    } else {
                        assignRoute.get(0).setEscortRequredFlag("N");
                    }
                    assignRoute.get(0).setAllocationMsg("N");
                    assignRoute.get(0).setTripStatus("allocated");                  
 //                 assignRoute.get(0).setTripAssignDate(new Date());
                    
                    assignRoute.get(0).setVehicleStatus("A");
                    assignRoute.get(0).setBucketStatus("N");
                    iCabRequestBO.update(assignRoute.get(0));
                    allCheckInVehicles.get(allCheckInVehicles.size() - 1).setStatus("N");
                    iVehicleCheckInBO
                            .update(allCheckInVehicles.get(allCheckInVehicles.size() - 1));
                }
            }
            List<EFmFmAssignRoutePO> assignRoute = iCabRequestBO.getParticularDriverAssignTripDetail(assignRoutePO);
            if (assignRoute.size() > 0) {
                EFmFmVehicleMasterPO vehicleMaster = iCabRequestBO
                        .getVehicleDetail(assignRoute.get(0).getEfmFmVehicleCheckIn()
                                .getEfmFmVehicleMaster().getVehicleId());
                vehicleMaster.setStatus("allocated");
                iVehicleCheckInBO.update(vehicleMaster);
                EFmFmDriverMasterPO particularDriverDetails = approvalBO.getParticularDriverDetail(
                        assignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
                particularDriverDetails.setStatus("allocated");
                approvalBO.update(particularDriverDetails);
                List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
                        assignRoute.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),
                        new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),eFmFmClientBranchPO.getCombinedFacility()));
                deviceDetails.get(0).setStatus("allocated");
                iVehicleCheckInBO.update(deviceDetails.get(0));
            }

        } catch (Exception e) {
            logs.info("exception-Pcikup time xl sheet column Need to format the text" + e);
            VehicleByCapacity = "vehicle allcation exception kinldy check the vehicle availability" + e;
        }
        return VehicleByCapacity;
    }

    public int VehicleAllocate(int employeeReqId, int BranchId, List<EFmFmVehicleCheckInPO> allCheckInVehicles,
            int seat_Capacity,int userId,String combinedBranchId) throws ParseException {
        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        int routeId = 0;
        List<EFmFmAssignRoutePO> assignRoutePO;
        EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO = new EFmFmEmployeeTravelRequestPO();
        eFmFmEmployeeTravelRequestPO.setBranchId(BranchId);
        eFmFmEmployeeTravelRequestPO.setRequestId(employeeReqId);
        List<EFmFmEmployeeTravelRequestPO> cabRequests = null;
        cabRequests = iCabRequestBO.getparticularRequestwithShiftTime(eFmFmEmployeeTravelRequestPO);
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
                    allCheckInVehicles.get(0).geteFmFmDeviceMaster().getDeviceId(), new MultifacilityService().combinedBranchIdDetails(userId,combinedBranchId));
            deviceDetails.get(0).setStatus("Y");
            iVehicleCheckInBO.update(deviceDetails.get(0));
            if (!allCheckInVehicles.isEmpty()) {
                EFmFmAssignRoutePO assignRoute = new EFmFmAssignRoutePO();
                EFmFmVehicleCheckInPO vehicleCheckInPO = new EFmFmVehicleCheckInPO();
                vehicleCheckInPO.setCheckInId(
                        allCheckInVehicles.get(allCheckInVehicles.size() - 1).getCheckInId());
                assignRoute.setEfmFmVehicleCheckIn(vehicleCheckInPO);
                EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
                routeAreaMapping.setRouteAreaId(
                        cabRequests.get(0).getEfmFmUserMaster().geteFmFmRouteAreaMapping().getRouteAreaId());// change
                assignRoute.seteFmFmRouteAreaMapping(routeAreaMapping);
        		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        		DateFormat requestDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        		String requestDate = requestDateFormat.format(cabRequests.get(0).getRequestDate());
        		String requestDateAndTime = requestDate + " " + cabRequests.get(0).getShiftTime();                
				assignRoute.setEscortRequredFlag("N");
				assignRoute.setAllocationMsg("N");
				assignRoute.setLocationFlg("N");
				assignRoute.setShiftTime(cabRequests.get(0).getShiftTime());
				assignRoute.setTripStatus("allocated");
				assignRoute.setTripConfirmationFromDriver("Not Delivered");

                assignRoute.setRoutingCompletionStatus("Started");
                assignRoute.setAssignedVendorName("NA"); 
                assignRoute.setSelectedBaseFacility(cabRequests.get(0).getEfmFmUserMaster()
                        .geteFmFmClientBranchPO().getBranchId());


				assignRoute.setIsBackTwoBack("N");
				assignRoute.setDistanceUpdationFlg("Y");
				assignRoute.setPlannedReadFlg("Y");
				assignRoute.setScheduleReadFlg("Y");
				assignRoute.setRemarksForEditingTravelledDistance("NO");
				assignRoute.setEditDistanceApproval("NO");
				if (cabRequests.get(0).getRequestType().equalsIgnoreCase("AdhocRequest")) {
					assignRoute.setRouteGenerationType("AdhocRequest");
				} else if (cabRequests.get(0).geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName()
						.equalsIgnoreCase("default")) {
					assignRoute.setRouteGenerationType("normal");
				} else {
					assignRoute.setRouteGenerationType("nodal");
				}
				assignRoute.setCreatedDate(new Date());
				assignRoute.setTripAssignDate(requestDateTimeFormat.parse(requestDateAndTime));
				assignRoute.setTripUpdateTime(requestDateTimeFormat.parse(requestDateAndTime));
				assignRoute.setVehicleStatus("A");
				assignRoute.setBucketStatus("N");
                
                EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
                eFmFmClientBranchPO.setBranchId(cabRequests.get(0).getEfmFmUserMaster()
                        .geteFmFmClientBranchPO().getBranchId());
                assignRoute.seteFmFmClientBranchPO(eFmFmClientBranchPO);
                assignRoute.setTripType(cabRequests.get(0).getTripType());
                allCheckInVehicles.get(allCheckInVehicles.size() - 1).setStatus("N");
                iVehicleCheckInBO.update(allCheckInVehicles.get(allCheckInVehicles.size() - 1));
                iCabRequestBO.update(assignRoute);
                cabRequests.get(0).setReadFlg("R");
                iCabRequestBO.update(cabRequests.get(0));
            }
        }
        assignRoutePO = iCabRequestBO.getCreatedAssignRoute(
                cabRequests.get(0).geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().geteFmFmClientBranchPO()
                        .getBranchId(),
                cabRequests.get(0).geteFmFmEmployeeRequestMaster().getTripType(), cabRequests.get(0).getShiftTime());
        if (assignRoutePO.size() > 0) {
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
                    assignRoutePO.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(), new MultifacilityService().combinedBranchIdDetails(userId,combinedBranchId));
            deviceDetails.get(0).setStatus("allocated");
            iVehicleCheckInBO.update(deviceDetails.get(0));
            routeId = assignRoutePO.get(0).getAssignRouteId();
        }
        return routeId;
    }  
}