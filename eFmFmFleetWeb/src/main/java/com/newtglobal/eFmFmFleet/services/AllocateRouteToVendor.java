package com.newtglobal.eFmFmFleet.services;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

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

import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/route")
@Consumes("application/json")
@Produces("application/json")
public class AllocateRouteToVendor {

	DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static Log log = LogFactory.getLog(AllocateRouteToVendor.class);

	@Context
	private HttpServletRequest httpRequest;
	JwtTokenGenerator token = new JwtTokenGenerator();

	/**
	 * Assign routes to vendor using selection on multiple check boxes .
	 *
	 * @author Sarfraz Khan
	 * 
	 * @since 2017-04-16
	 * 
	 * @return success/failure details.
	 */

	@POST
	@Path("/allocateToVendor")
	@Produces("application/json")
	public Response allocateRouteToVendor(EFmFmEmployeeTravelRequestPO travelRequestPO)
			throws ParseException, UnsupportedEncodingException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					travelRequestPO.getUserId()))) {
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
		} catch (Exception e) {
			log.info("authentication error" + e);
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
		Map<String, Object> allRoutesDetail = new HashMap<String, Object>();

		List<String> vendorName = new ArrayList<String>();
		List<Map<String, Object>> vendorDetails = new ArrayList<Map<String, Object>>();

		List<EFmFmAssignRoutePO> activeRoutes = iAssignRouteBO.getOpenRouteDetailFromAssignRouteId(excutionDate,
				excutionDate, travelRequestPO.getTripType(), shiftTimeFormat.format(dateShiftTime),
				travelRequestPO.getCombinedFacility());
		log.info("total routes" + activeRoutes.size());
		if (!(activeRoutes.isEmpty())) {
			for (EFmFmAssignRoutePO assignRoutes : activeRoutes) {
				List<Map<String, Object>> tripAllDetails = new ArrayList<Map<String, Object>>();
				vendorName.add(assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());				

				Map<String, Object> requests = new HashMap<String, Object>();
				StringBuffer waypoints = new StringBuffer();
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
				if (assignRoutes.getTripType().equalsIgnoreCase("PICKUP")) {
					employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployees(assignRoutes.getAssignRouteId());
					if (!(employeeTripDetailPO.isEmpty())) {
						requests.put("dropPickAreaName", employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
								.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());

					}
				} else {
					employeeTripDetailPO = iCabRequestBO.getDropTripAllSortedEmployees(assignRoutes.getAssignRouteId());
					if (!(employeeTripDetailPO.isEmpty())) {
						requests.put("dropPickAreaName",
								employeeTripDetailPO.get(employeeTripDetailPO.size() - 1)
										.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().getEfmFmAreaMaster()
										.getAreaName());
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
					if (assignRoutes.getIsBackTwoBack().equalsIgnoreCase("N")) {
						List<EFmFmAssignRoutePO> b2bDetails = iAssignRouteBO
								.getBackToBackTripDetailFromb2bId(assignRoutes.getAssignRouteId(), "DROP",assignRoutes.getCombinedFacility());
						if (!(b2bDetails.isEmpty())) {
							requests.put("suggestiveVehicleNumber", b2bDetails.get(0).getEfmFmVehicleCheckIn()
									.getEfmFmVehicleMaster().getVehicleNumber());
							requests.put("backTwoBackRouteId", b2bDetails.get(0).getAssignRouteId());
						} else {
							requests.put("suggestiveVehicleNumber", "No");
						}
					} else {
						if (assignRoutes.getIsBackTwoBack().equalsIgnoreCase("Y")) {
							List<EFmFmAssignRoutePO> b2bRouteDetails = iAssignRouteBO
									.getParticularRouteDetailFromAssignRouteId(assignRoutes.getBackTwoBackRouteId());
							if (assignRoutes.getTripType().equalsIgnoreCase("DROP")) {
								requests.put("suggestiveVehicleNumber",
										"B2b with Route Id " + b2bRouteDetails.get(0).getAssignRouteId() + ","
												+ shiftTimeFormater.format(b2bRouteDetails.get(0).getShiftTime())
												+ " and Route Name:" + b2bRouteDetails.get(0).geteFmFmRouteAreaMapping()
														.geteFmFmZoneMaster().getZoneName());
							} else {
								requests.put("suggestiveVehicleNumber", b2bRouteDetails.get(0).getEfmFmVehicleCheckIn()
										.getEfmFmVehicleMaster().getVehicleNumber());
								requests.put("backTwoBackRouteId", assignRoutes.getBackTwoBackRouteId());
							}

							// }

						}
					}

				} catch (Exception e) {
					log.info("Back2backShiftTime error" + e);
				}

				try {
					requests.put("vendorName", assignRoutes.getAssignedVendorName());
				} catch (Exception e) {
					requests.put("vendorName", "NA");
				}
				requests.put("routeType", assignRoutes.getRouteGenerationType());
				requests.put("isBackTwoBack", assignRoutes.getIsBackTwoBack());
				requests.put("escortRequired", assignRoutes.getEscortRequredFlag());
				requests.put("zoneRouteId", assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
				requests.put("routeType", assignRoutes.getRouteGenerationType());
				requests.put("vehicleType",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
				requests.put("transportNumber",
						assignRoutes.geteFmFmClientBranchPO().getTransportContactNumberForMsg());
				requests.put("tripActualAssignDate", dateFormat.format(assignRoutes.getTripAssignDate()));
				requests.put("shiftTime", shiftTimeFormater.format(assignRoutes.getShiftTime()));
				requests.put("vehicleStatus", assignRoutes.getVehicleStatus());
				requests.put("bucketStatus", assignRoutes.getBucketStatus());
				requests.put("tripStatus", assignRoutes.getTripStatus());
				requests.put("waypoints", waypoints);
				requests.put("baseLatLong", assignRoutes.geteFmFmClientBranchPO().getLatitudeLongitude());
				requests.put("routeId", assignRoutes.getAssignRouteId());
				requests.put("checkInId", assignRoutes.getEfmFmVehicleCheckIn().getCheckInId());

				if(assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName().contains("DUMMY")){
					requests.put("driverName", "NA");

				}else{
					requests.put("driverName", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				}
				requests.put("driverNumber",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				
				if(assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName().contains("DUMMY")){
					requests.put("vehicleNumber", "NA");

				}else{
					requests.put("vehicleNumber",
							assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				}
				
				
				requests.put("vendorId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorId());
				requests.put("vehicleType",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
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
			Set<String> mySet = new HashSet<String>(vendorName);
			for(String s: mySet){
				Map<String, Object> vendorNameCount = new HashMap<String, Object>();
				vendorNameCount.put(s, Collections.frequency(vendorName,s));
			 vendorDetails.add(vendorNameCount);
			}
			allRoutesDetail.put("routeDetails", allRoutes);
			allRoutesDetail.put("vendorDetails", vendorDetails);			
		}
		log.info("serviceEnd -UserId :" + travelRequestPO.getUserId());
		return Response.ok(allRoutes, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * 
	 * Update VendorId to the routes
	 * 
	 */

	@POST
	@Path("/assignVendor")
	public Response updateSuggestedVehicleEntry(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		final IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		IVendorDetailsBO iVendorMasterBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");

		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		log.info("assignedVendorId :" + assignRoutePO.getAssignedVendorName());
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					assignRoutePO.getUserId()))) {
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
		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmVendorMasterPO> venderExistCheck = iVendorMasterBO
				.getAllVendorName(assignRoutePO.getAssignedVendorName(),assignRoutePO.getCombinedFacility() );
		if (venderExistCheck.isEmpty()) {
			responce.put("status", "Vendor Name not exist please check.");
			log.info(" Last exist serviceEnd -UserId :" + assignRoutePO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}

		StringTokenizer routeIds = new StringTokenizer(assignRoutePO.getBulkRouteIds(), ",");
		while (routeIds.hasMoreElements()) {
			log.info("routeIds" + routeIds);
			int routeId = Integer.parseInt(routeIds.nextElement().toString());
			assignRouteBO.updateAssignVendorToRoute(routeId, assignRoutePO.getAssignedVendorName());
		}

		responce.put("status", "success");
		log.info(" Last serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

}
