package com.newtglobal.eFmFmFleet.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IAlertBO;
import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IEmployeeDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.MessagingService;
import com.newtglobal.eFmFmFleet.eFmFmFleet.PushNotificationService;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Validator;
import com.newtglobal.eFmFmFleet.model.EFmFmAlertTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaNodalMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientRouteMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeRequestMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLiveRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripTimingMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmZoneMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/zones")
@Consumes("application/json")
public class ServiceMappingService {

	DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private static Log log = LogFactory.getLog(ServiceMappingService.class);

	@Context
	private HttpServletRequest httpRequest;
	JwtTokenGenerator token = new JwtTokenGenerator();

	@POST
	@Path("/allzones")
	public Response getAllZonesAndRoutesDetails(EFmFmAssignRoutePO assignRoutePO) {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		assignRoutePO.setTripAssignDate(new Date());
		List<String> zones = new ArrayList<String>();
		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.getAllTodaysTripsWithOutDummyVehicle(assignRoutePO);

		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<TreeMap<String, Object>> allRoutes = new ArrayList<TreeMap<String, Object>>();
		log.info("total Routes v" + assignRoute.size());
		int totalNumberOfEmployee = 0;
		int totalNumberMaleEmployees = 0;
		int totalNumberFemaleEmployees = 0;
		int prgnentLady = 0;
		int physicallyChallaged = 0;
		int injured = 0;
		int isVIP = 0;
		int escortRequired = 0;
		int onBoard = 0;

		if (!(assignRoute.isEmpty())) {
			for (EFmFmAssignRoutePO assignRoutes : assignRoute) {
				prgnentLady = 0;
				physicallyChallaged = 0;
				injured = 0;
				isVIP = 0;							
				if (!(zones.contains(assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName()))) {
					TreeMap<String, Object> requests = new TreeMap<String, Object>();
					zones.add(assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					EFmFmRouteAreaMappingPO routeId = new EFmFmRouteAreaMappingPO();
					routeId.setRouteAreaId(assignRoutes.geteFmFmRouteAreaMapping().getRouteAreaId());
					EFmFmZoneMasterPO eFmFmZoneMaster = new EFmFmZoneMasterPO();
					eFmFmZoneMaster.setZoneId(assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
					routeId.seteFmFmZoneMaster(eFmFmZoneMaster);
					assignRoutePO.seteFmFmRouteAreaMapping(routeId);
					List<EFmFmAssignRoutePO> routesInZone = assignRouteBO.getAllRoutesOfParticularZone(assignRoutePO);

					requests.put("routeId", assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
					requests.put("routeName",
							assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					requests.put("NumberOfRoutes", routesInZone.size());
					List<EFmFmEmployeeTripDetailPO> employeeTripDetail = iCabRequestBO
							.getParticularTripAllEmployees(assignRoutes.getAssignRouteId());

					if (!(employeeTripDetail.isEmpty())) {
						totalNumberOfEmployee = totalNumberOfEmployee + employeeTripDetail.size();
						for (EFmFmEmployeeTripDetailPO tripDetail : employeeTripDetail) {
							if (tripDetail.getEmployeeOnboardStatus().equalsIgnoreCase("OnBoard")) {
								onBoard++;
							}
							if (new String(Base64.getDecoder().decode(
									tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()))
											.equalsIgnoreCase("Male")) {
								totalNumberMaleEmployees++;
							} else {
								totalNumberFemaleEmployees++;
							}
							
							if (new String(Base64.getDecoder().decode(
									tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getPragnentLady()))
											.equalsIgnoreCase("Yes")) {
								prgnentLady++;
							}
							if (new String(Base64.getDecoder().decode(
									tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getIsInjured()))
											.equalsIgnoreCase("Yes")) {
								injured++;

							}
							if (new String(Base64.getDecoder().decode(tripDetail.geteFmFmEmployeeTravelRequest()
									.getEfmFmUserMaster().getPhysicallyChallenged())).equalsIgnoreCase("Yes")) {
								physicallyChallaged++;
							}
							if(null !=tripDetail.geteFmFmEmployeeTravelRequest()
									.getEfmFmUserMaster().getIsVIP())
							if (new String(Base64.getDecoder().decode(tripDetail.geteFmFmEmployeeTravelRequest()
									.getEfmFmUserMaster().getIsVIP())).equalsIgnoreCase("Yes")) {
								isVIP++;
							}

						}
					}
					requests.put("physicallyChallenged", physicallyChallaged);
					requests.put("isInjured", injured);
					requests.put("pragnentLady", prgnentLady);
					requests.put("isVIP", isVIP);
					allRoutes.add(requests);
				} else {
					List<EFmFmEmployeeTripDetailPO> employeeTripDetail = iCabRequestBO
							.getParticularTripAllEmployees(assignRoutes.getAssignRouteId());
					if (!(employeeTripDetail.isEmpty())) {
						totalNumberOfEmployee = totalNumberOfEmployee + employeeTripDetail.size();
						for (EFmFmEmployeeTripDetailPO tripDetail : employeeTripDetail) {
							if (tripDetail.getEmployeeOnboardStatus().equalsIgnoreCase("OnBoard")) {
								onBoard++;
							}

							if (new String(Base64.getDecoder().decode(
									tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()))
											.equalsIgnoreCase("Male")) {
								totalNumberMaleEmployees++;
							} else {
								totalNumberFemaleEmployees++;
							}

						}

					}

				}
				// only after route close we can see the escort count
				if (assignRoutes.getEscortRequredFlag().equalsIgnoreCase("Y")) {
					escortRequired++;
				}

			}
		}
		
		allRoutesSingleObj.put("onBoard", onBoard);
		allRoutesSingleObj.put("YetToBoard", totalNumberOfEmployee-onBoard);
		allRoutesSingleObj.put("escortRequired", escortRequired);

		allRoutesSingleObj.put("totalNumberOfRoutes", assignRoute.size());
		allRoutesSingleObj.put("totalNumberMaleEmployees",totalNumberMaleEmployees);
		allRoutesSingleObj.put("totalNumberFemaleEmployees", totalNumberFemaleEmployees);
		allRoutesSingleObj.put("totalNumberOfEmployee", totalNumberOfEmployee);

		allRoutesSingleObj.put("routeDetails", allRoutes);
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/allroutes")
	public Response getAllRoutesOfParticulaZone(EFmFmAssignRoutePO assignRoutePO)
			throws UnsupportedEncodingException, ParseException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
		//
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		assignRoutePO.setTripAssignDate(new Date());
		log.info(assignRoutePO.getSearchType() + "Inside allroutes Funtion");
		DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
		// System.out.println("searchType"+assignRoutePO.getAdvanceFlag());
		List<EFmFmAssignRoutePO> assignRoute = new ArrayList<EFmFmAssignRoutePO>();
		if (assignRoutePO.getAdvanceFlag().equalsIgnoreCase("false")) {
			// assignRoute =
			// assignRouteBO.getAllRoutesOfParticularZone(assignRoutePO);
			assignRoute = assignRouteBO.getAllRoutesOfParticularZoneWithOrOutDummyVehicles(assignRoutePO);
		} else {
			String shiftDate = assignRoutePO.getTime();
			Date shift = shiftTimeFormater.parse(shiftDate);
			java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
			assignRoutePO.setShiftTime(shiftTime);
			DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(assignRoutePO.getToDate());
			String requestDate = requestDateFormat.format(date);
			assignRoutePO.setToDate(requestDate);
			if (assignRoutePO.getSearchType().equalsIgnoreCase("All")) {
				assignRoute = assignRouteBO.getAllRoutesBasedOnTripTypeAndShiftTimeAdvanceSearch(assignRoutePO);
			} else if (assignRoutePO.getSearchType().equalsIgnoreCase("Close")) {
				assignRoute = assignRouteBO.getAllBucketClosedRoutesOnAdvanceSearch(assignRoutePO);
			} else if (assignRoutePO.getSearchType().equalsIgnoreCase("Started")) {
				assignRoute = assignRouteBO.getAllStartedRoutesOnAdvanceSearch(assignRoutePO);
			} else if (assignRoutePO.getSearchType().equalsIgnoreCase("open")) {
				assignRoute = assignRouteBO.getAllOpenBucketRoutesOnAdvanceSearch(assignRoutePO);
			} else if (assignRoutePO.getSearchType().equalsIgnoreCase("closeNotStarted")) {
				assignRoute = assignRouteBO.getAllBucketClosedAndNotStartedRoutesAdvanceSearch(assignRoutePO);
			} else if (assignRoutePO.getSearchType().equalsIgnoreCase("backToBack")) {
				assignRoute = assignRouteBO.getAllBackToRoutesForParticularShift(assignRoutePO);
			}
		}
		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		int totalNumberOfEmployee = 0;
		log.info("assignRoute" + assignRoute.size());
		if (!(assignRoute.isEmpty())) {
			for (EFmFmAssignRoutePO assignRoutes : assignRoute) {
				List<Map<String, Object>> tripAllDetails = new ArrayList<Map<String, Object>>();
				Map<String, Object> requests = new HashMap<String, Object>();
				StringBuffer waypoints = new StringBuffer();
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
				if (assignRoutes.getTripType().equalsIgnoreCase("PICKUP")) {
					employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployees(assignRoutes.getAssignRouteId());
				} else {
					employeeTripDetailPO = iCabRequestBO.getDropTripAllSortedEmployees(assignRoutes.getAssignRouteId());
				}
				if (!(employeeTripDetailPO.isEmpty())) {
					totalNumberOfEmployee = totalNumberOfEmployee + employeeTripDetailPO.size();
					for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
						String wayPointsAdhocRequest = "";
						Map<String, Object> employeeDetails = new HashMap<String, Object>();
						employeeDetails
								.put("name",
										new String(Base64.getDecoder().decode(employeeTripDetail
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
								"utf-8"));
						employeeDetails.put("employeeId", employeeTripDetail.geteFmFmEmployeeTravelRequest()
								.getEfmFmUserMaster().getEmployeeId());

						employeeDetails.put("facilityName", employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmClientBranchPO().getBranchName());
						employeeDetails.put("requestWithProject", employeeTripDetail.getEfmFmAssignRoute()
								.geteFmFmClientBranchPO().getRequestWithProject());

						if (employeeTripDetail.getEfmFmAssignRoute().geteFmFmClientBranchPO().getRequestWithProject()
								.equalsIgnoreCase("Yes")) {
							employeeDetails.put("projectId", employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.getEfmFmUserMaster().geteFmFmClientProjectDetails().getClientProjectId());
						}
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
						if (null != employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getIsVIP())
							employeeDetails.put("isVIP",
									new String(
											Base64.getDecoder().decode(employeeTripDetail
													.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getIsVIP()),
									"utf-8"));

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
							wayPointsAdhocRequest = employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
									.geteFmFmEmployeeRequestMaster().getOriginLattitudeLongitude() + "|";
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
											.getDestination2AddressLattitudeLongitude() + "|";
									if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
											.geteFmFmEmployeeRequestMaster().getDestination3AddressLattitudeLongitude()
											.equalsIgnoreCase("N")) {
										wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
												.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination3AddressLattitudeLongitude() + "|";
										if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
												.geteFmFmEmployeeRequestMaster()
												.getDestination4AddressLattitudeLongitude().equalsIgnoreCase("N")) {
											wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
													.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
													.getDestination4AddressLattitudeLongitude() + "|";
											if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
													.geteFmFmEmployeeRequestMaster()
													.getDestination5AddressLattitudeLongitude().equalsIgnoreCase("N")) {
												wayPointsAdhocRequest = wayPointsAdhocRequest
														+ employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
																.geteFmFmEmployeeRequestMaster()
																.getDestination5AddressLattitudeLongitude()
														+ "|";
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
						employeeDetails.put("locationFlg",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getLocationFlg());
						employeeDetails.put("locationWayPointsIds",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getLocationWaypointsIds());

						try {
							if (employeeTripDetail.geteFmFmEmployeeTravelRequest().getLocationFlg()
									.equalsIgnoreCase("M")) {
								// waypoints.setLength(0);
								Map<String, Object> waypointsList = new CabRequestService()
										.listOfPointsForMultipleLocation(
												employeeTripDetail.geteFmFmEmployeeTravelRequest()
														.getLocationWaypointsIds(),
												assignRoutes.geteFmFmClientBranchPO().getBranchId(),
												employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
														.getUserId(),
												assignRoutePO.getCombinedFacility());

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
							log.debug("multilocationFlg Value not updated");
						}

						employeeDetails.put("requestColor",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestColor());

						employeeDetails.put("tripConfirmation", assignRoutes.getTripConfirmationFromDriver());

						employeeDetails.put("tripType", assignRoutes.getTripType());
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
						waypoints.append(assignRoute.get(0).getNodalPoints());
					}

				} else {
					if (assignRoutes.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoute.get(0).getNodalPoints());
					}
				}
				requests.put("tripConfirmation", assignRoutes.getTripConfirmationFromDriver());

				requests.put("assignRouteId", assignRoutes.getAssignRouteId());
				requests.put("locationFlg", assignRoutes.getLocationFlg());
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
				requests.put("escortRequired", assignRoutes.getEscortRequredFlag());

				// Driver Maximum hours per day basis.now taking 32400 in
				// seconds as a standard 9 hours
				if ((assignRoutes.getEfmFmVehicleCheckIn().getTotalTravelTime()) >= 32400) {
					requests.put("perdayWorkingHours", "Yes");
				} else {
					requests.put("perdayWorkingHours", "No");
				}
				try {
					//
					if (assignRoutes.getIsBackTwoBack().equalsIgnoreCase("N")) {
						List<EFmFmAssignRoutePO> b2bDetails = assignRouteBO.getBackToBackTripDetailFromb2bId(
								assignRoutes.getAssignRouteId(), "DROP", assignRoutePO.getCombinedFacility());
						if (!(b2bDetails.isEmpty())) {
							requests.put("suggestiveVehicleNumber", b2bDetails.get(0).getEfmFmVehicleCheckIn()
									.getEfmFmVehicleMaster().getVehicleNumber());
							requests.put("backTwoBackRouteId", b2bDetails.get(0).getAssignRouteId());
						} else {
							requests.put("suggestiveVehicleNumber", "No");
						}
					} else {
						if (assignRoutes.getIsBackTwoBack().equalsIgnoreCase("Y")) {
							List<EFmFmAssignRoutePO> b2bRouteDetails = assignRouteBO
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
				requests.put("routeType", assignRoutes.getRouteGenerationType());
				requests.put("isToll", assignRoutes.getIsToll());
				requests.put("plannedDis", assignRoutes.getPlannedDistance());

				requests.put("isBackTwoBack", assignRoutes.getIsBackTwoBack());
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
				requests.put("driverName", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				requests.put("driverNumber",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				requests.put("vehicleNumber",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				log.info("vehicle no"
						+ assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				requests.put("vendorId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorId());
				requests.put("driverId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				requests.put("checkInId", assignRoutes.getEfmFmVehicleCheckIn().getCheckInId());
				requests.put("vehicleId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				requests.put("vehicleAvailableCapacity",
						(assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity() - 1)
								- employeeTripDetailPO.size());
				requests.put("capacity", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity());
				requests.put("plaCardPrint", assignRoutes.geteFmFmClientBranchPO().getPlaCardPrint());
				requests.put("deviceNumber",
						assignRoutes.getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId());
				requests.put("empDetails", tripAllDetails);
				requests.put("routeName", assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				requests.put("zoneId", assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
				allRoutes.add(requests);
			}
			allRoutesSingleObj.put("routeDetails", allRoutes);
			allRoutesSingleObj.put("totalRoutes", assignRoute.size());
			allRoutesSingleObj.put("totalNumberOfEmployee", totalNumberOfEmployee);

		}
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/routeIds")
	public Response getRouteIdsOnSelectOnZoneDropdownInServiceMapping(EFmFmAssignRoutePO assignRoutePO)
			throws UnsupportedEncodingException, ParseException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info(assignRoutePO.getSearchType() + "Inside allroutes Funtion");
		List<EFmFmAssignRoutePO> assignRoute = new ArrayList<EFmFmAssignRoutePO>();
		assignRoute = assignRouteBO.getAllRoutesOfParticularZoneWithOrOutDummyVehicles(assignRoutePO);
		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		log.info("assignRoute" + assignRoute.size());
		if (!(assignRoute.isEmpty())) {
			for (EFmFmAssignRoutePO assignRoutes : assignRoute) {
				Map<String, Object> requests = new HashMap<String, Object>();
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO
						.getDropTripAllSortedEmployees(assignRoutes.getAssignRouteId());
				if ((employeeTripDetailPO
						.size()) < (assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity() - 1)) {
					requests.put("assignRouteId", assignRoutes.getAssignRouteId());
					requests.put("routeName",
							assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					requests.put("zoneId", assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
					allRoutes.add(requests);
				}
			}
			allRoutesSingleObj.put("routeDetails", allRoutes);
		}
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	// Service Call For Dropdown in service mapping

	@POST
	@Path("/allroutesdropdown")
	public Response getAllRoutesOfServiceMappingDropDown(EFmFmAssignRoutePO assignRoutePO)
			throws UnsupportedEncodingException, ParseException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		assignRoutePO.setTripAssignDate(new Date());
		DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
		// System.out.println("searchType"+assignRoutePO.getAdvanceFlag());
		List<EFmFmAssignRoutePO> assignRoute = null;
		if (assignRoutePO.getAdvanceFlag().equalsIgnoreCase("false")) {
			// assignRoute =
			// assignRouteBO.getAllRoutesOfParticularZone(assignRoutePO);
			assignRoute = assignRouteBO.getAllRoutesOfParticularZoneWithOutDummyVehicles(assignRoutePO);

		} else {
			String shiftDate = assignRoutePO.getTime();
			Date shift = shiftTimeFormater.parse(shiftDate);
			java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
			assignRoutePO.setShiftTime(shiftTime);
			DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(assignRoutePO.getToDate());
			String requestDate = requestDateFormat.format(date);
			assignRoutePO.setToDate(requestDate);
			if (assignRoutePO.getSearchType().equalsIgnoreCase("All")) {
				assignRoute = assignRouteBO.getAllRoutesBasedOnTripTypeAndShiftTime(assignRoutePO);
			} else if (assignRoutePO.getSearchType().equalsIgnoreCase("Close")) {
				assignRoute = assignRouteBO.getAllBucketClosedRoutes(assignRoutePO);
			} else if (assignRoutePO.getSearchType().equalsIgnoreCase("Started")) {
				assignRoute = assignRouteBO.getAllStartedRoutes(assignRoutePO);
			} else if (assignRoutePO.getSearchType().equalsIgnoreCase("open")) {
				assignRoute = assignRouteBO.getAllOpenBucketRoutes(assignRoutePO);
			} else if (assignRoutePO.getSearchType().equalsIgnoreCase("closeNotStarted")) {
				assignRoute = assignRouteBO.getAllBucketClosedAndNotStartedRoutes(assignRoutePO);
			} else if (assignRoutePO.getSearchType().equalsIgnoreCase("backToBack")) {
				assignRoute = assignRouteBO.getAllBackToRoutesForParticularShift(assignRoutePO);
			}
		}
		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		if (!(assignRoute.isEmpty())) {
			for (EFmFmAssignRoutePO assignRoutes : assignRoute) {
				List<Map<String, Object>> tripAllDetails = new ArrayList<Map<String, Object>>();
				Map<String, Object> requests = new HashMap<String, Object>();
				StringBuffer waypoints = new StringBuffer();
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
				if (assignRoutes.getTripType().equalsIgnoreCase("PICKUP")) {
					employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployees(assignRoutes.getAssignRouteId());
				} else {
					employeeTripDetailPO = iCabRequestBO.getDropTripAllSortedEmployees(assignRoutes.getAssignRouteId());
				}
				if (!(employeeTripDetailPO.isEmpty())) {
					for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
						String wayPointsAdhocRequest = "";
						Map<String, Object> employeeDetails = new HashMap<String, Object>();
						employeeDetails
								.put("name",
										new String(Base64.getDecoder().decode(employeeTripDetail
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
								"utf-8"));
						employeeDetails.put("employeeId", employeeTripDetail.geteFmFmEmployeeTravelRequest()
								.getEfmFmUserMaster().getEmployeeId());
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

						employeeDetails
								.put("isVIP",
										new String(Base64.getDecoder().decode(employeeTripDetail
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getIsVIP()),
								"utf-8"));

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
							wayPointsAdhocRequest = employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
									.geteFmFmEmployeeRequestMaster().getOriginLattitudeLongitude() + "|";
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
											.getDestination2AddressLattitudeLongitude() + "|";
									if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
											.geteFmFmEmployeeRequestMaster().getDestination3AddressLattitudeLongitude()
											.equalsIgnoreCase("N")) {
										wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
												.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination3AddressLattitudeLongitude() + "|";
										if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
												.geteFmFmEmployeeRequestMaster()
												.getDestination4AddressLattitudeLongitude().equalsIgnoreCase("N")) {
											wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
													.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
													.getDestination4AddressLattitudeLongitude() + "|";
											if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
													.geteFmFmEmployeeRequestMaster()
													.getDestination5AddressLattitudeLongitude().equalsIgnoreCase("N")) {
												wayPointsAdhocRequest = wayPointsAdhocRequest
														+ employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
																.geteFmFmEmployeeRequestMaster()
																.getDestination5AddressLattitudeLongitude()
														+ "|";
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
						employeeDetails.put("locationFlg",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getLocationFlg());
						employeeDetails.put("locationWayPointsIds",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getLocationWaypointsIds());

						try {
							if (employeeTripDetail.geteFmFmEmployeeTravelRequest().getLocationFlg()
									.equalsIgnoreCase("M")) {
								// waypoints.setLength(0);
								Map<String, Object> waypointsList = new CabRequestService()
										.listOfPointsForMultipleLocation(
												employeeTripDetail.geteFmFmEmployeeTravelRequest()
														.getLocationWaypointsIds(),
												assignRoutes.geteFmFmClientBranchPO().getBranchId(),
												employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
														.getUserId(),
												assignRoutePO.getCombinedFacility());

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

						employeeDetails.put("requestColor",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestColor());
						employeeDetails.put("tripType", assignRoutes.getTripType());
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
						waypoints.append(assignRoute.get(0).getNodalPoints());
					}

				} else {
					if (assignRoutes.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoute.get(0).getNodalPoints());
					}
				}
				requests.put("assignRouteId", assignRoutes.getAssignRouteId());
				requests.put("locationFlg", assignRoutes.getLocationFlg());
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
				requests.put("escortRequired", assignRoutes.getEscortRequredFlag());

				// Driver Maximum hours per day basis.now taking 32400 in
				// seconds as a standard 9 hours
				if ((assignRoutes.getEfmFmVehicleCheckIn().getTotalTravelTime()) >= 32400) {
					requests.put("perdayWorkingHours", "Yes");
				} else {
					requests.put("perdayWorkingHours", "No");
				}

				requests.put("routeType", assignRoutes.getRouteGenerationType());
				requests.put("isToll", assignRoutes.getIsToll());
				requests.put("plannedDis", assignRoutes.getPlannedDistance());

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
				requests.put("driverName", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				requests.put("driverNumber",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				requests.put("vehicleNumber",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				requests.put("vendorId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorId());
				requests.put("driverId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				requests.put("checkInId", assignRoutes.getEfmFmVehicleCheckIn().getCheckInId());
				requests.put("vehicleId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				requests.put("vehicleAvailableCapacity",
						(assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity() - 1)
								- employeeTripDetailPO.size());
				requests.put("capacity", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity());
				requests.put("deviceNumber",
						assignRoutes.getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId());
				requests.put("empDetails", tripAllDetails);
				requests.put("routeName", assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				requests.put("zoneId", assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
				allRoutes.add(requests);
			}
			allRoutesSingleObj.put("routeDetails", allRoutes);
			allRoutesSingleObj.put("totalRoutes", assignRoute.size());
		}
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * 
	 * Update checkIn entity from suggested Vehicle Number in service mapping
	 * 
	 */

	@POST
	@Path("/updateSuggestedVehicleNum")
	public Response updateSuggestedVehicleEntry(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		List<EFmFmAssignRoutePO> currentAssignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);

		if (!(currentAssignRoute.isEmpty())) {
			assignRoutePO.setAssignRouteId(assignRoutePO.getBackTwoBackRouteId());
			List<EFmFmAssignRoutePO> b2bAssignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);

			List<EFmFmVehicleCheckInPO> allCheckInVehicles = iVehicleCheckInBO.getParticularCheckedInVehicles(
					assignRoutePO.geteFmFmClientBranchPO().getCombinedFacility(),
					b2bAssignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
			if (allCheckInVehicles.isEmpty()) {
				responce.put("status", "B2bVehicleNotCheckIn");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			// making Old Vehicle Free
			List<EFmFmVehicleCheckInPO> oldVehiclesCheckInDetail = iVehicleCheckInBO
					.getCheckedInVehicleDetailsFromChecInId(
							currentAssignRoute.get(0).getEfmFmVehicleCheckIn().getCheckInId());
			oldVehiclesCheckInDetail.get(0).setStatus("Y");
			iVehicleCheckInBO.update(oldVehiclesCheckInDetail.get(0));

			// update back2back drop vehicle using back2back flag
			assignRoutePO.setAssignRouteId(assignRoutePO.getBackTwoBackRouteId());

			// b2bAssignRoute.get(0).setIsBackTwoBack("Y");
			// b2bAssignRoute.get(0).setBackTwoBackRouteId(currentAssignRoute.get(0).getAssignRouteId());
			// assignRouteBO.update(b2bAssignRoute.get(0));
			if (!(allCheckInVehicles.isEmpty())) {
				allCheckInVehicles.get(0).setStatus("N");
				try {
					allCheckInVehicles.get(0).setTotalTravelDistance(currentAssignRoute.get(0).getPlannedDistance());
				} catch (Exception e) {
					log.info("Error trip complete button updating the travelled and number of trips" + e);
				}
				allCheckInVehicles.get(0).setNumberOfTrips(allCheckInVehicles.get(0).getNumberOfTrips() + 1);
				allCheckInVehicles.get(0).setTotalTravelTime(currentAssignRoute.get(0).getPlannedTime());
				iVehicleCheckInBO.update(allCheckInVehicles.get(0));
				currentAssignRoute.get(0).setIsBackTwoBack("Y");
				currentAssignRoute.get(0).setBackTwoBackRouteId(assignRoutePO.getBackTwoBackRouteId());
				currentAssignRoute.get(0).setEfmFmVehicleCheckIn(allCheckInVehicles.get(0));
				assignRouteBO.update(currentAssignRoute.get(0));

				// Updating vehicle contracted distance
				// EFmFmVehicleMasterPO vehicleList =
				// iVehicleCheckInBO.getParticularVehicleDetail(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId());
				// vehicleList.setMonthlyPendingFixedDistance(vehicleList.getMonthlyPendingFixedDistance()
				// - currentAssignRoute.get(0).getPlannedDistance());
				// iVehicleCheckInBO.update(vehicleList);
				responce.put("vehicleNumber", allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleNumber());
				responce.put("vehicleId", allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId());
				responce.put("driverName", allCheckInVehicles.get(0).getEfmFmDriverMaster().getFirstName());
				responce.put("driverNumber", allCheckInVehicles.get(0).getEfmFmDriverMaster().getMobileNumber());
				responce.put("deviceNumber", allCheckInVehicles.get(0).geteFmFmDeviceMaster().getDeviceId());
			} else {
				responce.put("status", "notCheckIn");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

			}
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/*
	 * 
	 * Delete checkIn entity from suggested Vehicle Number in service mapping
	 * 
	 */

	@POST
	@Path("/deleteSuggestedVehicleNum")
	public Response resetSuggestedVehicleEntry(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		// IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO)
		// ContextLoader
		// .getContext().getBean("IVehicleCheckInBO");
		;
		List<EFmFmAssignRoutePO> currentAssignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		if (!(currentAssignRoute.isEmpty())) {
			// This will fatch dummy entry only
			List<EFmFmVehicleCheckInPO> allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLargeCapacity(
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),
							assignRoutePO.getCombinedFacility()),
					currentAssignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity());
			if (!(allCheckInVehicles.isEmpty())) {
				// update back2back drop vehicle using back2back flag
				assignRoutePO.setAssignRouteId(assignRoutePO.getBackTwoBackRouteId());
				// List<EFmFmAssignRoutePO> b2bAssignRoute =
				// assignRouteBO.closeParticularTrips(assignRoutePO);
				// b2bAssignRoute.get(0).setIsBackTwoBack("N");
				// b2bAssignRoute.get(0).setBackTwoBackRouteId(currentAssignRoute.get(0).getAssignRouteId());
				// assignRouteBO.update(b2bAssignRoute.get(0));

				currentAssignRoute.get(0).setEfmFmVehicleCheckIn(allCheckInVehicles.get(0));
				currentAssignRoute.get(0).setIsBackTwoBack("N");
				currentAssignRoute.get(0).setBackTwoBackRouteId(0);
				assignRouteBO.update(currentAssignRoute.get(0));

				// Updating vehicle contracted distance
				// EFmFmVehicleMasterPO vehicleList =
				// iVehicleCheckInBO.getParticularVehicleDetail(b2bAssignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				// vehicleList.setMonthlyPendingFixedDistance(vehicleList.getMonthlyPendingFixedDistance()
				// + currentAssignRoute.get(0).getPlannedDistance());
				// iVehicleCheckInBO.update(vehicleList);

				responce.put("vehicleNumber", allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleNumber());
				responce.put("vehicleId", allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId());
				responce.put("driverName", allCheckInVehicles.get(0).getEfmFmDriverMaster().getFirstName());
				responce.put("driverNumber", allCheckInVehicles.get(0).getEfmFmDriverMaster().getMobileNumber());
				responce.put("deviceNumber", allCheckInVehicles.get(0).geteFmFmDeviceMaster().getDeviceId());
			} else {
				responce.put("status", "notCheckInvehicle");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/*
	 * 
	 * Update checkIn entity from manual b2b Vehicle Number in service mapping
	 * 
	 */

	@POST
	@Path("/updateManualVehicleNum")
	public Response updateManualVehicleEntry(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");

		List<EFmFmAssignRoutePO> currentAssignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		List<EFmFmVehicleCheckInPO> b2bDropVehicle = iVehicleCheckInBO
				.getParticularCheckedInVehicles(assignRoutePO.getCombinedFacility(), assignRoutePO.getVehicleId());
		log.info("vehicle id" + assignRoutePO.getVehicleId());
		List<EFmFmAssignRoutePO> b2bAssignRoute = assignRouteBO
				.getLastTripFromCheckInIdAndTripType(b2bDropVehicle.get(0).getCheckInId(), "DROP");
		if (!(b2bAssignRoute.isEmpty())) {
			// List<EFmFmVehicleCheckInPO> allCheckInVehicles
			// =iVehicleCheckInBO.getParticularCheckedInVehicles(assignRoutePO.geteFmFmClientBranchPO().getBranchId(),b2bAssignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
			// if(!(allCheckInVehicles.isEmpty())){
			// making Old Vehicle Free
			// List<EFmFmVehicleCheckInPO> oldVehiclesCheckInDetail
			// =iVehicleCheckInBO.getCheckedInVehicleDetailsFromChecInId(currentAssignRoute.get(0).getEfmFmVehicleCheckIn().getCheckInId());
			// oldVehiclesCheckInDetail.get(0).setStatus("Y");
			// iVehicleCheckInBO.update(oldVehiclesCheckInDetail.get(0));

			// update back2back drop vehicle using back2back flag
			b2bAssignRoute.get(b2bAssignRoute.size() - 1).setIsBackTwoBack("Y");
			b2bAssignRoute.get(b2bAssignRoute.size() - 1)
					.setBackTwoBackRouteId(currentAssignRoute.get(0).getAssignRouteId());
			assignRouteBO.update(b2bAssignRoute.get(b2bAssignRoute.size() - 1));
			// allCheckInVehicles.get(0).setStatus("N");
			// try {
			// allCheckInVehicles.get(0).setTotalTravelDistance(currentAssignRoute.get(0).getPlannedDistance());
			// } catch (Exception e) {
			// log.info("Error trip complete button updating the travelled and
			// number of trips" + e);
			// }
			// allCheckInVehicles.get(0).setNumberOfTrips(allCheckInVehicles.get(0).getNumberOfTrips()+1);
			// allCheckInVehicles.get(0).setTotalTravelTime(currentAssignRoute.get(0).getPlannedTime());
			// iVehicleCheckInBO.update(allCheckInVehicles.get(0));
			currentAssignRoute.get(0).setIsBackTwoBack("Y");
			currentAssignRoute.get(0)
					.setBackTwoBackRouteId(b2bAssignRoute.get(b2bAssignRoute.size() - 1).getAssignRouteId());
			// currentAssignRoute.get(0).setEfmFmVehicleCheckIn(allCheckInVehicles.get(0));
			assignRouteBO.update(currentAssignRoute.get(0));

			// Updating vehicle contracted distance
			// EFmFmVehicleMasterPO vehicleList =
			// iVehicleCheckInBO.getParticularVehicleDetail(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId());
			// vehicleList.setMonthlyPendingFixedDistance(vehicleList.getMonthlyPendingFixedDistance()
			// - currentAssignRoute.get(0).getPlannedDistance());
			// iVehicleCheckInBO.update(vehicleList);
			// responce.put("vehicleNumber",
			// allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleNumber());
			// responce.put("vehicleId",
			// allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId());
			// responce.put("driverName",
			// allCheckInVehicles.get(0).getEfmFmDriverMaster().getFirstName());
			// responce.put("driverNumber",
			// allCheckInVehicles.get(0).getEfmFmDriverMaster().getMobileNumber());
			// responce.put("deviceNumber",
			// allCheckInVehicles.get(0).geteFmFmDeviceMaster().getDeviceId());
			// }

		} else {
			responce.put("status", "notTravelledInDrop");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/*
	 * 
	 * Reset checkIn entity from manual Vehicle Number in service mapping
	 * 
	 */

	@POST
	@Path("/resetManualVehicleNum")
	public Response deleteManualVehicleEntry(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		// IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO)
		// ContextLoader
		// .getContext().getBean("IVehicleCheckInBO");

		List<EFmFmAssignRoutePO> currentAssignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		// List<EFmFmVehicleCheckInPO> b2bDropVehicle
		// =iVehicleCheckInBO.getParticularCheckedInVehicles(assignRoutePO.geteFmFmClientBranchPO().getBranchId(),assignRoutePO.getVehicleId());

		if (!(currentAssignRoute.isEmpty())) {
			// This will fatch dummy entry only
			List<EFmFmVehicleCheckInPO> allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLargeCapacity(
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),
							assignRoutePO.getCombinedFacility()),
					currentAssignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity());
			if (!(allCheckInVehicles.isEmpty())) {
				// update back2back drop vehicle using back2back flag
				// assignRoutePO.setAssignRouteId(assignRoutePO.getBackTwoBackRouteId());
				// List<EFmFmAssignRoutePO> b2bAssignRoute =
				// assignRouteBO.closeParticularTrips(assignRoutePO);
				// List<EFmFmAssignRoutePO> b2bAssignRoute =
				// assignRouteBO.getLastTripFromCheckInIdAndTripType(b2bDropVehicle.get(0).getCheckInId(),"DROP");
				// if(!(b2bAssignRoute.isEmpty())){
				// b2bAssignRoute.get(0).setIsBackTwoBack("N");
				// b2bAssignRoute.get(0).setBackTwoBackRouteId(currentAssignRoute.get(0).getAssignRouteId());
				// assignRouteBO.update(b2bAssignRoute.get(0));
				// }

				currentAssignRoute.get(0).setEfmFmVehicleCheckIn(allCheckInVehicles.get(0));
				currentAssignRoute.get(0).setIsBackTwoBack("N");
				currentAssignRoute.get(0).setBackTwoBackRouteId(0);
				assignRouteBO.update(currentAssignRoute.get(0));

				// Updating vehicle contracted distance
				// EFmFmVehicleMasterPO vehicleList =
				// iVehicleCheckInBO.getParticularVehicleDetail(b2bAssignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				// vehicleList.setMonthlyPendingFixedDistance(vehicleList.getMonthlyPendingFixedDistance()
				// + currentAssignRoute.get(0).getPlannedDistance());
				// iVehicleCheckInBO.update(vehicleList);

				// responce.put("vehicleNumber",
				// allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleNumber());
				// responce.put("vehicleId",
				// allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId());
				// responce.put("driverName",
				// allCheckInVehicles.get(0).getEfmFmDriverMaster().getFirstName());
				// responce.put("driverNumber",
				// allCheckInVehicles.get(0).getEfmFmDriverMaster().getMobileNumber());
				// responce.put("deviceNumber",
				// allCheckInVehicles.get(0).geteFmFmDeviceMaster().getDeviceId());
			} else {
				responce.put("status", "notCheckInvehicle");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/*
	 * update route drop Sequence from service mapping
	 */

	@POST
	@Path("/updateDropSequnce")
	public Response updateDropTripRouteSquencing(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		List<EFmFmEmployeeTravelRequestPO> cabRequestDetail = iCabRequestBO
				.getParticularRequestDetailOnTripComplete(assignRoutePO.getRequestId());
		if (!(cabRequestDetail.isEmpty())) {
			// Time is int varible for updating drop sequence
			cabRequestDetail.get(0).setDropSequence(Integer.parseInt(assignRoutePO.getTime()));
			iCabRequestBO.update(cabRequestDetail.get(0));
		}
		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		if (!(assignRoute.isEmpty())) {
			assignRoute.get(0).setTripUpdateTime(new Date());
			assignRouteBO.update(assignRoute.get(0));
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/*
	 * add or remove the escorts from the routes
	 */

	@POST
	@Path("/addOrRemoveEscorts")
	public Response addOrDeleteTheEscortsFromRoutes(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO
				.getParticularTripAllEmployees(assignRoute.get(0).getAssignRouteId());
		if (!(assignRoute.isEmpty())) {
			if (assignRoutePO.getSearchType().equalsIgnoreCase("addEscort")) {
				if (employeeTripDetailPO
						.size() == (assignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()
								- 1)) {
					responce.put("status", "bucketFull");
					log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
				List<EFmFmEscortCheckInPO> checkInEscortDetails = iVehicleCheckInBO
						.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
								assignRoutePO.getEscortCheckInId());
				if (!checkInEscortDetails.isEmpty()) {
					checkInEscortDetails.get(0).setStatus("N");
					assignRoute.get(0).setEscortRequredFlag("Y");
					EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
					checkInEscort.setEscortCheckInId(checkInEscortDetails.get(0).getEscortCheckInId());
					assignRoute.get(0).seteFmFmEscortCheckIn(checkInEscort);
					assignRoute.get(0).setTripUpdateTime(new Date());
					iVehicleCheckInBO.update(checkInEscortDetails.get(0));
					assignRouteBO.update(assignRoute.get(0));
				}
				// }
			}

			else {// case when there is no escort check in id
				try {
					int checkInId = assignRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId();
					log.info("escortId" + checkInId);
				} catch (Exception e) {
					assignRoute.get(0).setEscortRequredFlag("N");
					assignRoute.get(0).setTripUpdateTime(new Date());
					assignRouteBO.update(assignRoute.get(0));
					responce.put("status", "success");
					log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}

				List<EFmFmEscortCheckInPO> checkInEscortDetails = iVehicleCheckInBO
						.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
								assignRoutePO.getEscortCheckInId());
				if (!checkInEscortDetails.isEmpty()) {
					checkInEscortDetails.get(0).setStatus("Y");
					assignRoute.get(0).setEscortRequredFlag("N");
					assignRoute.get(0).seteFmFmEscortCheckIn(null);
					assignRoute.get(0).setTripUpdateTime(new Date());
					iVehicleCheckInBO.update(checkInEscortDetails.get(0));
					assignRouteBO.update(assignRoute.get(0));
				}

			}
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/*
	 * Update Request status from cab allocation page.
	 */

	@POST
	@Path("/updateRequestStatus")
	public Response updateRequestStatusNoshow(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		try {
			List<EFmFmEmployeeTripDetailPO> tripRequestDetail = iCabRequestBO.getParticularTripEmployeeRequestDetails(
					assignRoutePO.getTripId(), assignRoutePO.getRequestId(), assignRoutePO.getAssignRouteId());
			if (!(tripRequestDetail.isEmpty())) {
				tripRequestDetail.get(0).setBoardedFlg(assignRoutePO.getBoardedFlg());
				iCabRequestBO.update(tripRequestDetail.get(0));
			}
			List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
			if (!(assignRoute.isEmpty())) {
				assignRoute.get(0).setTripUpdateTime(new Date());
				assignRouteBO.update(assignRoute.get(0));
			}
		} catch (Exception e) {
			log.info("inside updateRequestStatus method" + e);
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/*
	 * Update employe route status from allocation page on complete button.
	 */

	@POST
	@Path("/employeeStatus")
	public Response checkParticularRouteEmployeeStatus(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		// IUserMasterBO userMasterBO = (IUserMasterBO)
		// ContextLoader.getContext().getBean("IUserMasterBO");

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		// this service we are calling just before the complete service
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
		//
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		try {
			List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO
					.getRouteEmployeeStatus(assignRoutePO.getAssignRouteId());
			log.info("AssignRouteId" + assignRoutePO.getAssignRouteId() + "employeeTripDetailPO"
					+ employeeTripDetailPO.size());
			if (!(employeeTripDetailPO.isEmpty())) {
				responce.put("status", "fail");
				log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}

		} catch (Exception e) {
			log.info("inside employeeStatus on complete button method" + e);
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/*
	 * update route pickuptime for Sequence from service mapping
	 */

	@POST
	@Path("/updatePickUpTime")
	public Response updateRouteSquencing(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		List<EFmFmEmployeeTripDetailPO> empDetails = iCabRequestBO.getParticularTriprEmployeeBoardedStatus(
				assignRoutePO.getRequestId(), assignRoutePO.getAssignRouteId());
		if (!(empDetails.isEmpty()) && !(empDetails.get(0).getBoardedFlg().equalsIgnoreCase("N"))) {
			responce.put("status", empDetails.get(0).getBoardedFlg());
			log.info("Not Update Function");
			log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmEmployeeTravelRequestPO> cabRequestDetail = iCabRequestBO
				.getParticularRequestDetailOnTripComplete(assignRoutePO.getRequestId());
		log.info("Update Function" + cabRequestDetail.size());
		DateFormat timeformate = new SimpleDateFormat("HH:mm");
		String pickUpTime = assignRoutePO.getTime();
		Date changePickUpTime = timeformate.parse(pickUpTime);
		java.sql.Time pickTime = new java.sql.Time(changePickUpTime.getTime());
		if (!(cabRequestDetail.isEmpty())) {
			cabRequestDetail.get(0).setPickUpTime(pickTime);
			iCabRequestBO.update(cabRequestDetail.get(0));
		}
		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		if (!(assignRoute.isEmpty())) {
			assignRoute.get(0).setTripUpdateTime(new Date());
			assignRouteBO.update(assignRoute.get(0));
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/*
	 * update employee boarded status from service mapping
	 */

	@POST
	@Path("/updateEmployeeStatus")
	public Response updateEmployeeBoardingStatusByAdmin(EFmFmEmployeeTripDetailPO employeeTripDetailPO)
			throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		// final IUserMasterBO userMasterBO = (IUserMasterBO)
		// ContextLoader.getContext().getBean("IUserMasterBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		// log.info("serviceStart -UserId :" +
		// employeeTripDetailPO.getUserId());
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTripDetailPO.getUserId()))){
		//
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(employeeTripDetailPO.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		final List<EFmFmEmployeeTripDetailPO> empDetails = iCabRequestBO.getParticularTriprEmployeeBoardedStatus(
				employeeTripDetailPO.geteFmFmEmployeeTravelRequest().getRequestId(),
				employeeTripDetailPO.getEfmFmAssignRoute().getAssignRouteId());
		Map<String, Object> requests = new HashMap<String, Object>();
		log.info("employeeTripDetailPO.getBoardedFlg()" + employeeTripDetailPO.getBoardedFlg());
		// admin can update a staus any time
		/// Cab Has Left Message........
		// final PushNotificationService pushNotification = new
		// PushNotificationService();
		if (employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("NO") && !(empDetails.get(0).getEfmFmAssignRoute()
				.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber().contains("DUMMY"))) {
			// Thread thread1 = new Thread(new Runnable() {
			// @Override
			// public synchronized void run() {
			// try {
			// String text = "";
			// MessagingService messaging = new MessagingService();
			// text = "Sorry you missed us.\nYour ride "
			// +
			// empDetails.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
			// .getEfmFmVehicleMaster().getVehicleNumber()
			// + " has left your " +
			// empDetails.get(0).geteFmFmEmployeeTravelRequest().getTripType()
			// + " point.\nFor feedback write to us @"
			// +
			// empDetails.get(0).getEfmFmAssignRoute().geteFmFmClientBranchPO().getFeedBackEmailId();
			// messaging.cabHasLeftMessageForSch(
			// new String(Base64.getDecoder()
			// .decode(empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
			// .getMobileNumber()),
			// "utf-8"),
			// text,
			// empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
			// log.info("Time taken by cab left message from gate way and button
			// click for trip Id: "
			// + empDetails.get(0).getEmpTripId());
			// try {
			// if
			// (empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceType()
			// .contains("Android")) {
			// pushNotification.notification(empDetails.get(0).geteFmFmEmployeeTravelRequest()
			// .getEfmFmUserMaster().getDeviceToken(), text);
			// } else {
			// pushNotification.iosPushNotification(empDetails.get(0).geteFmFmEmployeeTravelRequest()
			// .getEfmFmUserMaster().getDeviceToken(), text);
			// }
			// // }
			// } catch (Exception e) {
			// log.info("PushStatus employeestatus" + e);
			// }
			// Thread.currentThread().stop();
			//
			// } catch (Exception e) {
			// try {
			// log.info("Error Cab has left Message Triggered for First employee
			// from button click "
			// + new String(Base64.getDecoder().decode(empDetails.get(0)
			// .geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber()),
			// "utf-8"));
			// } catch (UnsupportedEncodingException e1) {
			// log.info("Error" + e1);
			// }
			// Thread.currentThread().stop();
			// }
			// }
			// });
			// thread1.start();
			empDetails.get(0).setCabstartFromDestination(new Date().getTime());
		}
		if (employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("B")
				|| employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("D")) {
			empDetails.get(0).setPickedUpDateAndTime(new Date().getTime());
			empDetails.get(0).setEmployeeOnboardStatus("OnBoard");
		}
		empDetails.get(0).setBoardedFlg(employeeTripDetailPO.getBoardedFlg());
		empDetails.get(0).setEmployeeStatus("completed");
		
		
		// iCabRequestBO.update(empDetails.get(0));
		EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
		assignRoutePO.setAssignRouteId(employeeTripDetailPO.getEfmFmAssignRoute().getAssignRouteId());
		EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(employeeTripDetailPO.getBranchId());
		assignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		log.info("BranchId" + employeeTripDetailPO.getBranchId());
		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		if (!(assignRoute.isEmpty())) {
			assignRoute.get(0).setTripUpdateTime(new Date());
			assignRouteBO.update(assignRoute.get(0));
		}
		// Auto Drop Cancel Code
		// if
		// (empDetails.get(0).geteFmFmEmployeeTravelRequest().getTripType().equalsIgnoreCase("PICKUP")
		// && employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("NO")) {
		// String shiftTime =
		// shifTimeFormate.format(empDetails.get(0).geteFmFmEmployeeTravelRequest().getShiftTime());
		// java.sql.Time dropShiftTimings = new
		// java.sql.Time(shifTimeFormate.parse(shiftTime).getTime());
		// String reqDate =
		// dateHypenFormat.format(empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestDate())
		// + " " + shifTimeFormate.format(dropShiftTimings);
		// long dropRequestDate = getDisableTime(9, 0,
		// dateTimeFormate.parse(reqDate));
		// Date requestDateForDrop = new Date(dropRequestDate);
		// String dropShiftTime = requestDateForDrop.getHours() + ":" +
		// requestDateForDrop.getMinutes();
		// String dropRequestAndStart = dateFormat.format(requestDateForDrop);
		// java.sql.Time dropShift = new
		// java.sql.Time(shifTimeFormate.parse(dropShiftTime).getTime());
		// Date dropRequestAndStartDate = dateFormat.parse(dropRequestAndStart);
		// List<EFmFmEmployeeTravelRequestPO> employeeRequestUpdateForDrop =
		// iCabRequestBO
		// .getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(dropRequestAndStartDate,
		// dropShift,
		// empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
		// .geteFmFmClientBranchPO().getBranchId(),
		// empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getUserId(),
		// "DROP");
		// log.info("Size" + employeeRequestUpdateForDrop.size() + "Id"
		// +
		// empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getUserId());
		// if (!(employeeRequestUpdateForDrop.isEmpty())) {
		// String shiftDate = "23:50:00";
		// DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		// Date shift = shiftFormate.parse(shiftDate);
		// java.sql.Time updatedShiftTime = new java.sql.Time(shift.getTime());
		// employeeRequestUpdateForDrop.get(0).setShiftTime(updatedShiftTime);
		// iCabRequestBO.update(employeeRequestUpdateForDrop.get(0));
		// }
		// }
		// final List<EFmFmUserMasterPO> hostDetails =
		// userMasterBO.getEmployeeUserDetailFromMobileNumber(
		// empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().geteFmFmClientBranchPO()
		// .getBranchId(),
		// empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber());
		// if
		// (empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestType().equalsIgnoreCase("guest")
		// && (employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("B")
		// || employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("D"))) {
		// Thread thread1 = new Thread(new Runnable() {
		// @Override
		// public synchronized void run() {
		// try {
		// String hostText = "";
		// MessagingService messagingService = new MessagingService();
		// if
		// (empDetails.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP"))
		// {
		// hostText = "Dear Host,Your guest "
		// + new String(
		// Base64.getDecoder()
		// .decode(empDetails.get(0).geteFmFmEmployeeTravelRequest()
		// .getEfmFmUserMaster().getFirstName()),
		// "utf-8")
		// + " is Dropped.";
		// } else {
		// hostText = "Dear Host,Your guest "
		// + new String(
		// Base64.getDecoder()
		// .decode(empDetails.get(0).geteFmFmEmployeeTravelRequest()
		// .getEfmFmUserMaster().getFirstName()),
		// "utf-8")
		// + " is picked up.";
		// }
		// try {
		// if (hostDetails.isEmpty() && !(hostDetails.isEmpty())) {
		// if (hostDetails.get(0).getDeviceType().contains("Android")) {
		// pushNotification.notification(hostDetails.get(0).getDeviceToken(),
		// hostText);
		// } else {
		// pushNotification.iosPushNotification(hostDetails.get(0).getDeviceToken(),
		// hostText);
		// }
		// }
		// } catch (Exception e) {
		// log.info("PushStatus employeestatus" + e);
		// }
		// StringTokenizer token = new StringTokenizer(
		// new
		// String(Base64.getDecoder().decode(empDetails.get(0).geteFmFmEmployeeTravelRequest()
		// .getEfmFmUserMaster().getHostMobileNumber()), "utf-8"),
		// ",");
		// while (token.hasMoreElements()) {
		// messagingService.sendTripAsMessage(token.nextElement().toString(),
		// hostText,
		// empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
		// }
		// Thread.currentThread().stop();
		// } catch (Exception e) {
		// log.info("Error" + e);
		// }
		// }
		// });
		// thread1.start();
		// }

		iCabRequestBO.update(empDetails.get(0));
		requests.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * update employee boarded status from employee device
	 */

	@POST
	@Path("/employeeBoradingStatus")
	public Response updateEmployeeBoardingStatusByEmployeeevice(EFmFmEmployeeTripDetailPO employeeTripDetailPO)
			throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		// final IUserMasterBO userMasterBO = (IUserMasterBO)
		// ContextLoader.getContext().getBean("IUserMasterBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + employeeTripDetailPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					employeeTripDetailPO.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTripDetailPO.getUserId());
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
		final List<EFmFmEmployeeTripDetailPO> empDetails = iCabRequestBO.getParticularTriprEmployeeBoardedStatus(
				employeeTripDetailPO.geteFmFmEmployeeTravelRequest().getRequestId(),
				employeeTripDetailPO.getEfmFmAssignRoute().getAssignRouteId());
		Map<String, Object> requests = new HashMap<String, Object>();
		log.info("Employee boarding status from Mobile App." + employeeTripDetailPO.getBoardedFlg());
		/// Cab Has Left Message........
		// final PushNotificationService pushNotification = new
		// PushNotificationService();
		if (employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("NO") && !(empDetails.get(0).getEfmFmAssignRoute()
				.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber().contains("DUMMY"))) {
			empDetails.get(0).setCabstartFromDestination(new Date().getTime());
		}
		if (employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("B")
				|| employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("D")) {
			empDetails.get(0).setPickedUpDateAndTime(new Date().getTime());
			empDetails.get(0).setEmployeeOnboardStatus("OnBoard");
		}
		empDetails.get(0).setBoardedFlg(employeeTripDetailPO.getBoardedFlg());
		empDetails.get(0).setEmployeeStatus("completed");
		EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
		assignRoutePO.setAssignRouteId(employeeTripDetailPO.getEfmFmAssignRoute().getAssignRouteId());
		EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(employeeTripDetailPO.getBranchId());
		assignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		log.info("BranchId" + employeeTripDetailPO.getBranchId());
		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		if (!(assignRoute.isEmpty())) {
			assignRoute.get(0).setTripUpdateTime(new Date());
			assignRouteBO.update(assignRoute.get(0));
		}
		iCabRequestBO.update(empDetails.get(0));
		requests.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Deleting an empty Route or bucket
	 * 
	 */

	@POST
	@Path("/deleteroute")
	public Response deleteAnemptyRoute(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info(assignRoutePO.getCombinedFacility() + "Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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

		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO
				.getDropTripAllSortedEmployees(assignRoute.get(0).getAssignRouteId());
		if (!(employeeTripDetailPO.isEmpty()) && !(assignRoute.get(0).getTripStatus().equalsIgnoreCase("allocated"))) {
			responce.put("status", "failed");
			log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		} else {
			if (employeeTripDetailPO.isEmpty()) {
				EFmFmVehicleCheckInPO vehicleCheckIn = iVehicleCheckInBO.getCheckedInEntityDetailFromChecInId(
						assignRoute.get(0).getEfmFmVehicleCheckIn().getCheckInId());

				EFmFmVehicleMasterPO vehicleMaster = iVehicleCheckInBO.getParticularVehicleDetail(
						assignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				vehicleMaster.setStatus("A");
				iVehicleCheckInBO.update(vehicleMaster);

				EFmFmDriverMasterPO particularDriverDetails = approvalBO.getParticularDriverDetail(
						assignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				particularDriverDetails.setStatus("A");
				approvalBO.update(particularDriverDetails);
				List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
						assignRoute.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),
								assignRoutePO.getCombinedFacility()));
				deviceDetails.get(0).setStatus("Y");
				iVehicleCheckInBO.update(deviceDetails.get(0));
				vehicleCheckIn.setStatus("Y");
				iVehicleCheckInBO.update(vehicleCheckIn);
				if (assignRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
					try {
						List<EFmFmEscortCheckInPO> checkInEscortDetails = iVehicleCheckInBO
								.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
										assignRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
						checkInEscortDetails.get(0).setStatus("Y");
						iVehicleCheckInBO.update(checkInEscortDetails.get(0));
					} catch (Exception e) {
						log.info("Error" + e);
					}
				}
				assignRouteBO.deleteParticularAssignRoute(assignRoute.get(0).getAssignRouteId());
				responce.put("status", "success");
				log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			for (EFmFmEmployeeTripDetailPO tripDetail : employeeTripDetailPO) {
				// EFmFmClientBranchPO eFmFmClientBranchPO = new
				// EFmFmClientBranchPO();
				// eFmFmClientBranchPO.setBranchId(assignRoutePO.geteFmFmClientBranchPO().getBranchId());
				// EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO =
				// new EFmFmEmployeeTravelRequestPO();
				// eFmFmEmployeeTravelRequestPO.setRequestId(tripDetail.geteFmFmEmployeeTravelRequest().getRequestId());
				// EFmFmUserMasterPO eFmFmUserMasterPO = new
				// EFmFmUserMasterPO();
				// eFmFmUserMasterPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
				// eFmFmEmployeeTravelRequestPO.setEfmFmUserMaster(eFmFmUserMasterPO);
				List<EFmFmEmployeeTravelRequestPO> cabRequest = iCabRequestBO.getParticularRequestDetailOnTripComplete(
						tripDetail.geteFmFmEmployeeTravelRequest().getRequestId());

				// DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
				// String shiftTime = "23:50:00";
				// Date shift = shiftFormate.parse(shiftTime);
				// java.sql.Time dateShiftTime = new
				// java.sql.Time(shift.getTime());
				// cabRequest.get(0).setShiftTime(dateShiftTime);
				cabRequest.get(0).setReadFlg("Y");
				cabRequest.get(0).setRoutingAreaCreation("Y");
				iCabRequestBO.update(cabRequest.get(0));
				iCabRequestBO.deleteParticularTripDetail(tripDetail.getEmpTripId());
			}

		}
		EFmFmVehicleCheckInPO vehicleCheckIn = iVehicleCheckInBO
				.getCheckedInEntityDetailFromChecInId(assignRoute.get(0).getEfmFmVehicleCheckIn().getCheckInId());
		List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
				assignRoute.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
				assignRoute.get(0).geteFmFmClientBranchPO().getBranchId());
		EFmFmVehicleMasterPO vehicleMaster = iVehicleCheckInBO.getParticularVehicleDetail(
				assignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
		System.out.println("CheckInId" + vehicleCheckIn.getCheckInId());
		if (oldCheckRoutesCheck.size() == 1) {
			EFmFmDriverMasterPO particularDriverDetails = approvalBO.getParticularDriverDetail(
					assignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
			particularDriverDetails.setStatus("A");
			approvalBO.update(particularDriverDetails);
			List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
					assignRoute.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),
							assignRoutePO.getCombinedFacility()));
			deviceDetails.get(0).setStatus("Y");
			iVehicleCheckInBO.update(deviceDetails.get(0));
			// vehicleMaster.setStatus("A");
		}
		try {
			if (vehicleCheckIn.getNumberOfTrips() != 0) {
				int numberOfTrips = vehicleCheckIn.getNumberOfTrips() - 1;
				long totalTravelTime = vehicleCheckIn.getTotalTravelTime() - assignRoute.get(0).getPlannedTime();
				double totalTravelDistance = vehicleCheckIn.getTotalTravelDistance()
						- assignRoute.get(0).getPlannedDistance();
				if (oldCheckRoutesCheck.size() == 1) {
					iVehicleCheckInBO.updateVehicleCheckInDetailsWithStatus(vehicleCheckIn.getCheckInId(),
							numberOfTrips, totalTravelTime, totalTravelDistance, "Y");
					log.info("InSide setStatus Y" + vehicleCheckIn.getCheckInId());
				} else {
					iVehicleCheckInBO.updateVehicleCheckInDetailsWithOutStatus(vehicleCheckIn.getCheckInId(),
							numberOfTrips, totalTravelTime, totalTravelDistance);
					log.info("InSide setStatus N" + vehicleCheckIn.getCheckInId());
				}
			} else {
				vehicleCheckIn.setStatus("Y");
				iVehicleCheckInBO.update(vehicleCheckIn);
			}

			double monthlyFixedDistance = vehicleMaster.getMonthlyPendingFixedDistance()
					+ assignRoute.get(0).getPlannedDistance();
			iVehicleCheckInBO.updateVehicleMonthlyDistance(vehicleMaster.getVehicleId(), monthlyFixedDistance, "A");
		} catch (Exception e) {
			log.info("Cab allocation updates" + e);
		}

		if (!(assignRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("N"))) {
			try {
				List<EFmFmEscortCheckInPO> checkInEscortDetails = iVehicleCheckInBO
						.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
								assignRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
				checkInEscortDetails.get(0).setStatus("Y");
				iVehicleCheckInBO.update(checkInEscortDetails.get(0));
			} catch (Exception e) {
				log.info("Escort error" + e);
			}
		}
		assignRouteBO.deleteParticularAssignRoute(assignRoute.get(0).getAssignRouteId());
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/*
	 * Deleting a request from Bucket
	 * 
	 */

	@POST
	@Path("/cancelbucketrequest")
	public Response cancelRequestFromBucket(EFmFmEmployeeTripDetailPO eFmFmEmployeeTripDetailPO) throws ParseException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmEmployeeTripDetailPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmEmployeeTripDetailPO.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmEmployeeTripDetailPO.getUserId());
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
		log.info("serviceMapping Delete");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");

		// For making In active the request from travel desk.
		EFmFmAssignRoutePO eFmFmAssignRoutePO = new EFmFmAssignRoutePO();
		eFmFmAssignRoutePO.setAssignRouteId(eFmFmEmployeeTripDetailPO.getEfmFmAssignRoute().getAssignRouteId());

		EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(eFmFmEmployeeTripDetailPO.getBranchId());
		eFmFmAssignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);

		EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO = new EFmFmEmployeeTravelRequestPO();
		eFmFmEmployeeTravelRequestPO
				.setRequestId(eFmFmEmployeeTripDetailPO.geteFmFmEmployeeTravelRequest().getRequestId());

		EFmFmUserMasterPO eFmFmUserMasterPO = new EFmFmUserMasterPO();
		eFmFmUserMasterPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		eFmFmEmployeeTravelRequestPO.setEfmFmUserMaster(eFmFmUserMasterPO);
		List<EFmFmEmployeeTravelRequestPO> cabRequest = iCabRequestBO
				.getparticularRequestDetail(eFmFmEmployeeTravelRequestPO);
		eFmFmUserMasterPO.setUserId(cabRequest.get(0).getEfmFmUserMaster().getUserId());
		eFmFmEmployeeTravelRequestPO.setEfmFmUserMaster(eFmFmUserMasterPO);
		eFmFmEmployeeTravelRequestPO.setRequestType(cabRequest.get(0).getRequestType());
		eFmFmEmployeeTravelRequestPO.setTripType(cabRequest.get(0).getTripType());
		if (cabRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchCode()
				.equalsIgnoreCase("SBOBNG")) {
			DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
			String shiftTime = "23:50:00";
			Date shift = shiftFormate.parse(shiftTime);
			java.sql.Time dateShiftTime = new java.sql.Time(shift.getTime());
			cabRequest.get(0).setShiftTime(dateShiftTime);

		}
		cabRequest.get(0).setReadFlg("Y");
		cabRequest.get(0).setRoutingAreaCreation("Y");
		iCabRequestBO.update(cabRequest.get(0));
		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(eFmFmAssignRoutePO);
		// assignRoute.get(0).setBucketStatus("N");
		assignRoute.get(0).setTripUpdateTime(new Date());
		assignRouteBO.update(assignRoute.get(0));
		List<EFmFmEmployeeTripDetailPO> tripEmployees = iCabRequestBO
				.getDropTripAllSortedEmployees(assignRoute.get(0).getAssignRouteId());
		List<EFmFmEmployeeTripDetailPO> tripRequest = iCabRequestBO.getParticularTriprEmployeeBoardedStatus(
				eFmFmEmployeeTripDetailPO.geteFmFmEmployeeTravelRequest().getRequestId(),
				eFmFmEmployeeTripDetailPO.getEfmFmAssignRoute().getAssignRouteId());
		// update the vehicle capacity after deletion
		EFmFmVehicleMasterPO eFmFmVehicleMasterPO = iVehicleCheckInBO.getParticularVehicleDetails(tripRequest.get(0)
				.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber(),
				eFmFmEmployeeTripDetailPO.getBranchId());
		if ((((eFmFmVehicleMasterPO.getCapacity() - tripEmployees.size()) == (eFmFmVehicleMasterPO.getCapacity() - 1))
				&& assignRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("N"))
				|| ((tripEmployees.size() + 2 == eFmFmVehicleMasterPO.getCapacity() - 1)
						&& assignRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y"))) {
			// Delete travel desk when vehicle started...
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
			eFmFmVehicleCheckInPO.setCheckInTime(new Date());
			eFmFmVehicleCheckInPO
					.setCheckInId(tripRequest.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getCheckInId());
			List<EFmFmVehicleCheckInPO> vehicleCheckIn = iVehicleCheckInBO
					.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
			List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
					vehicleCheckIn.get(0).getCheckInId(), assignRoute.get(0).geteFmFmClientBranchPO().getBranchId());
			try {
				if (vehicleCheckIn.get(0).getNumberOfTrips() != 0) {
					int numberOfTrips = vehicleCheckIn.get(0).getNumberOfTrips() - 1;
					long totalTravelTime = vehicleCheckIn.get(0).getTotalTravelTime()
							- assignRoute.get(0).getPlannedTime();
					double totalTravelDistance = vehicleCheckIn.get(0).getTotalTravelDistance()
							- assignRoute.get(0).getPlannedDistance();
					if (oldCheckRoutesCheck.size() == 1) {
						iVehicleCheckInBO.updateVehicleCheckInDetailsWithStatus(vehicleCheckIn.get(0).getCheckInId(),
								numberOfTrips, totalTravelTime, totalTravelDistance, "Y");
						// eFmFmVehicleMasterPO.setStatus("A");
						log.info("InSide setStatus Y" + vehicleCheckIn.get(0).getCheckInId());
					} else {
						iVehicleCheckInBO.updateVehicleCheckInDetailsWithStatus(vehicleCheckIn.get(0).getCheckInId(),
								numberOfTrips, totalTravelTime, totalTravelDistance, "N");
						log.info("InSide setStatus N" + vehicleCheckIn.get(0).getCheckInId());
					}
				} else {
					vehicleCheckIn.get(0).setStatus("Y");
					iVehicleCheckInBO.update(vehicleCheckIn.get(0));

				}
			} catch (Exception e) {
				log.info("Cab allocation updates");
			}

			if (!(assignRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("N"))) {
				/*
				 * eFmFmVehicleMasterPO.setAvailableSeat(eFmFmVehicleMasterPO.
				 * getAvailableSeat()+1); } else{
				 */
				List<EFmFmEscortCheckInPO> checkInEscortDetails = iVehicleCheckInBO
						.getParticulaEscortDetailFromEscortId(eFmFmEmployeeTripDetailPO.getCombinedFacility(),
								assignRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());

				checkInEscortDetails.get(0).setStatus("Y");
				iVehicleCheckInBO.update(checkInEscortDetails.get(0));
				// eFmFmVehicleMasterPO.setAvailableSeat(eFmFmVehicleMasterPO.getAvailableSeat()+2);
			}

			double monthlyFixedDistance = eFmFmVehicleMasterPO.getMonthlyPendingFixedDistance()
					+ assignRoute.get(0).getPlannedDistance();
			iVehicleCheckInBO.updateVehicleMonthlyDistance(eFmFmVehicleMasterPO.getVehicleId(), monthlyFixedDistance,
					"A");

			iCabRequestBO.deleteParticularTripDetail(tripRequest.get(0).getEmpTripId());
			if (oldCheckRoutesCheck.size() == 1) {
				EFmFmDriverMasterPO particularDriverDetails = approvalBO.getParticularDriverDetail(
						assignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				particularDriverDetails.setStatus("A");
				approvalBO.update(particularDriverDetails);
				List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
						assignRoute.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),eFmFmEmployeeTripDetailPO.getCombinedFacility());
				deviceDetails.get(0).setStatus("Y");
				iVehicleCheckInBO.update(deviceDetails.get(0));
			}

			List<EFmFmTripAlertsPO> allAlerts = iAlertBO.getParticularTripAlerts(
					assignRoute.get(0).geteFmFmClientBranchPO().getBranchId(), assignRoute.get(0).getAssignRouteId());
			if (!(allAlerts.isEmpty())) {
				for (EFmFmTripAlertsPO tripAlertsPO : allAlerts) {
					iAlertBO.deleteAllAlerts(tripAlertsPO.getTripAlertsId());
				}
			}
			List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO.getRouteDetailsFromAssignRouteId(
					assignRoute.get(0).geteFmFmClientBranchPO().getBranchId(), assignRoute.get(0).getAssignRouteId());
			if (!(actualRouteTravelled.isEmpty())) {
				for (EFmFmLiveRoutTravelledPO actulaTraveled : actualRouteTravelled) {
					assignRouteBO.deleteParticularActualTravelled(actulaTraveled.getLiveRouteTravelId());
				}
			}

			assignRouteBO.deleteParticularAssignRoute(assignRoute.get(0).getAssignRouteId());
		} else {
			List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
			if (assignRoute.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
				employeeTripDetailPO = iCabRequestBO
						.getParticularTripAllEmployees(assignRoute.get(0).getAssignRouteId());
				// availableCapacity=employeeTripDetailPO.size()+1;
				if (assignRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
					// availableCapacity=eFmFmVehicleMasterPO.getAvailableSeat()+2;
					assignRoute.get(0).setEscortRequredFlag("N");
					assignRouteBO.update(assignRoute.get(0));
				}
			} else {
				employeeTripDetailPO = iCabRequestBO
						.getDropTripAllSortedEmployees(assignRoute.get(0).getAssignRouteId());
				// availableCapacity=eFmFmVehicleMasterPO.getAvailableSeat()+1;
				if (assignRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
					// availableCapacity=eFmFmVehicleMasterPO.getAvailableSeat()+2;
					assignRoute.get(0).setEscortRequredFlag("N");
					assignRouteBO.update(assignRoute.get(0));
				}
			}
			log.info("escortId" + employeeTripDetailPO.toString());
			// eFmFmVehicleMasterPO.setAvailableSeat(availableCapacity);
			iVehicleCheckInBO.update(eFmFmVehicleMasterPO);
			iCabRequestBO.deleteParticularTripDetail(tripRequest.get(0).getEmpTripId());

		}
		List<EFmFmEmployeeTripDetailPO> tripEmployee = iCabRequestBO
				.getDropTripAllSortedEmployees(assignRoute.get(0).getAssignRouteId());
		responce.put("escortName", "Not Required");
		responce.put("availableCapacity", (eFmFmVehicleMasterPO.getCapacity() - 1) - tripEmployee.size());
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmEmployeeTripDetailPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/bucketclose")
	public Response manualBucketClose(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, InvalidKeyException, NoSuchAlgorithmException, IOException, URISyntaxException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		final IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");

		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		// log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
		//
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }

		final List<EFmFmAssignRoutePO> assignRoutes = assignRouteBO.closeParticularTrips(assignRoutePO);
		IVehicleCheckInBO vehicleCheckIn = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		assignRoutes.get(0).setTripUpdateTime(new Date());
		assignRoutes.get(0).setPlannedReadFlg("N");
		assignRoutes.get(0).setScheduleReadFlg("N");
		if (assignRoutes.get(0).getAssignedVendorName().equalsIgnoreCase("NA")) {
			assignRoutes.get(0).setAssignedVendorName(assignRoutes.get(0).getEfmFmVehicleCheckIn()
					.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
		}
		final MessagingService messagingService = new MessagingService();
		final PushNotificationService pushNotification = new PushNotificationService();
		final int branchId = assignRoutePO.geteFmFmClientBranchPO().getBranchId();
		//Rohit Change
		ArrayList<EFmFmClientBranchPO> alEscortLogingReturn=(ArrayList<EFmFmClientBranchPO>) userMasterBO.getEscortTimeDetails(assignRoutePO.getCombinedFacility());
		EFmFmClientBranchPO po=alEscortLogingReturn.get(0);
				
		List<EFmFmEmployeeTripDetailPO> finalTripEmployees = null;
		log.info("assignRoutes" + assignRoutePO.getAssignRouteId());
		if (!(assignRoutes.isEmpty())
				&& assignRoutes.get(0).geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP")
				&& !(assignRoutes.get(0).getRoutingCompletionStatus().equalsIgnoreCase("completed"))) {
			responce.put("status", "routingNotComplete");
			log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}

		try {
			List<EFmFmAssignRoutePO> todayRoutesCheck = assignRouteBO.getDuplicateTripAllocationCheck(
					assignRoutes.get(0).getEfmFmVehicleCheckIn().getCheckInId(), assignRoutePO.getCombinedFacility());
			if (!todayRoutesCheck.isEmpty() && todayRoutesCheck.get(0).getShiftTime().getTime() == assignRoutes.get(0)
					.getShiftTime().getTime()) {
				responce.put("status", "duplicat");
				log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		} catch (Exception e) {
			log.info("duplicate trip allocation erroe" + e);
		}
		// //Driver Maximum hours per day basis.now taking 32400 in seconds as a
		// standard 9 hours
		// if(!(assignRoutes.isEmpty()) &&
		// (assignRoutes.get(0).getEfmFmVehicleCheckIn().getTotalTravelTime()>=32400)){
		// responce.put("status", "NineHours");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }

		// List<EFmFmAssignRoutePO> lastCompletedRoute =
		// assignRouteBO.getLastCompletedTripByDriver(assignRoutes.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
		// branchId);
		// if(!(lastCompletedRoute.isEmpty()) &&
		// !(lastCompletedRoute.get(lastCompletedRoute.size()-1).getTripStatus().equalsIgnoreCase("completed"))
		// &&
		// (lastCompletedRoute.get(lastCompletedRoute.size()-1).getPlannedTime()>=14400)){
		// responce.put("status", "FourHoursBrkExp");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// if(!(lastCompletedRoute.isEmpty()) &&
		// (lastCompletedRoute.get(lastCompletedRoute.size()-1).getTripStatus().equalsIgnoreCase("completed"))
		// &&
		// (lastCompletedRoute.get(lastCompletedRoute.size()-1).getPlannedTime()>=14400)){
		// responce.put("status", "FourHoursBrk");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }

		if (assignRoutes.get(0).getRouteGenerationType().equalsIgnoreCase("nodalAdhoc")) {
			assignRoutes.get(0).setBucketStatus("Y");
			assignRoutes.get(0).setVehicleStatus("F");
			assignRouteBO.update(assignRoutes.get(0));
			responce.put("availableCapacity",
					((assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity() - 1)));
			responce.put("status", "success");
			log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		//Rohit Change
				SimpleDateFormat sdf=new SimpleDateFormat("hh:MM:ss");
				String hoursData="";
				String localGender="";
				Date hoursDate=null;
				Date startEscortDate=new Date();
				Date endEscortDate=new Date();
				Date requestDateEscort=null;
				
				finalTripEmployees = iCabRequestBO.getParticularTripAllEmployees(assignRoutes.get(0).getAssignRouteId());
		
		if (assignRoutes.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
			//Rohit Changes
//	//		startEscortDate=sdf.parse(String.valueOf(po.getEscortStartTimePickup()));
//	//	    endEscortDate=sdf.parse(String.valueOf(po.getEscortEndTimePickup()));
//			Calendar cStart=Calendar.getInstance();
//			cStart.setTime(po.getEscortStartTimePickup());
			
		  //Rohit Change
//			hoursDate=finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime();
//		    System.out.println("po.getEscortStartTimePickup() 2=="+po.getEscortStartTimePickup());
//		    System.out.println("startEscortDate 2=="+startEscortDate);
//		    System.out.println("startEscortDate 3=="+cStart.getTime());
//		    System.out.println("po.getEscortEndTimePickup() 2=="+po.getEscortEndTimePickup());
//		    System.out.println("endEscortDate 2=="+endEscortDate);
		    
		    startEscortDate=po.getEscortStartTimePickup();
		    endEscortDate=po.getEscortEndTimePickup();
		    
			if(finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime()!=null){
				hoursDate=finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime();
				}else{
					hoursDate=finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getShiftTime();
				}
			 localGender=new String(Base64.getDecoder().decode(finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),"utf-8");
			 requestDateEscort=finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getRequestDate();
			 System.out.println("Request Date=="+finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getRequestDate());
		    
//			finalTripEmployees = iCabRequestBO.getParticularTripAllEmployees(assignRoutes.get(0).getAssignRouteId());
			if ((assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()
					- 1) == finalTripEmployees.size()
					&& assignRoutes.get(0).getEscortRequredFlag().equalsIgnoreCase("N")
					&& escortProvideVerification(localGender, po.getEscortTimeWindowEnable(), po.getEscortRequired(),
							startEscortDate, endEscortDate, hoursDate, requestDateEscort, finalTripEmployees)) {
				responce.put("errorMessage", errorMsgForEscort("PICKUP",po.getEscortRequired()));
				responce.put("type", "PICKUP");				
				responce.put("status", "notClose");
				log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			else if ((assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()
					- 1) > finalTripEmployees.size()
					&& assignRoutes.get(0).getEscortRequredFlag().equalsIgnoreCase("N")					
					&& escortProvideVerification(localGender,po.getEscortTimeWindowEnable(),po.getEscortRequired(),startEscortDate,endEscortDate,hoursDate,requestDateEscort,finalTripEmployees)) {
				responce.put("errorMessage", errorMsgForEscort("PICKUP",po.getEscortRequired()));
				responce.put("type", "PICKUP");
				responce.put("status", "notClose");
				log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
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
							int seqCount = 0;
							for (EFmFmEmployeeTripDetailPO tripDetailPO : employeeTripDetailPO) {
								seqCount++;
								String text = "Vehicle number "
										+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
												.getVehicleNumber()
										+ " "
										+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
												.getVehicleModel()
										+ " has been assigned for your pickup for "
										+ shiftTimeFormater.format(assignRoutes.get(0).getShiftTime())
										+ " shift.\nBoarding Time is "
										+ shiftTimeFormater
												.format(tripDetailPO.geteFmFmEmployeeTravelRequest().getPickUpTime())
										+ "&Seq Num-" + seqCount + ".For feedback write to us @"
										+ assignRoutes.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();
								if (tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType()
										.equalsIgnoreCase("guest")) {
									String coPassengers = "";
									if (employeeTripDetailPO.size() > 1) {
										List<EFmFmEmployeeTripDetailPO> coPassengerList = iCabRequestBO1.getCoPassenger(
												assignRoutes.get(0).getAssignRouteId(),
												tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestId());
										for (EFmFmEmployeeTripDetailPO passenger : coPassengerList) {
											coPassengers += new String(
													Base64.getDecoder()
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
									messagingService
											.sendMessageToGuest(
													new String(
															Base64.getDecoder()
																	.decode(tripDetailPO.geteFmFmEmployeeTravelRequest()
																			.getEfmFmUserMaster().getMobileNumber()),
															"utf-8"),
													text,
													tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType());

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
													tripDetailPO.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
															.getHostMobileNumber());
									try {
										if (tripDetailPO.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
												.getDeviceType().contains("Android")) {
											pushNotification.notification(tripDetailPO.geteFmFmEmployeeTravelRequest()
													.getEfmFmUserMaster().getDeviceToken(), text);
											if (!(hostDetails.isEmpty()))
												pushNotification.notification(hostDetails.get(0).getDeviceToken(),
														hostText);
										} else {
											pushNotification
													.iosPushNotification(tripDetailPO.geteFmFmEmployeeTravelRequest()
															.getEfmFmUserMaster().getDeviceToken(), text);
											if (!(hostDetails.isEmpty()))
												pushNotification.iosPushNotification(
														hostDetails.get(0).getDeviceToken(), hostText);
										}

									} catch (Exception e) {
										log.info("PushStatus" + e);
									}
								} else {
									// EAP..Allocation message for Pickup Guest
									// and Host
									try {
										if (tripDetailPO.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
												.getDeviceType().contains("Android")) {
											pushNotification.notification(tripDetailPO.geteFmFmEmployeeTravelRequest()
													.getEfmFmUserMaster().getDeviceToken(), text);
										} else {
											pushNotification
													.iosPushNotification(tripDetailPO.geteFmFmEmployeeTravelRequest()
															.getEfmFmUserMaster().getDeviceToken(), text);
										}
										log.info("notiE");
									} catch (Exception e) {
										log.info("PushStatus" + e);
									}
									messagingService
											.sendTripAsMessage(
													new String(
															Base64.getDecoder()
																	.decode(tripDetailPO.geteFmFmEmployeeTravelRequest()
																			.getEfmFmUserMaster().getMobileNumber()),
															"utf-8"),
													text,
													tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType());
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
			if (assignRoutes.get(0).getEscortRequredFlag().equalsIgnoreCase("N") && !(finalTripEmployees.isEmpty())) {
				//Rohit Change
				if (escortProvideVerification(localGender,po.getEscortTimeWindowEnable(),po.getEscortRequired(),startEscortDate,endEscortDate,hoursDate,requestDateEscort,finalTripEmployees)) {
					List<EFmFmEscortCheckInPO> escortList = vehicleCheckIn.getAllCheckedInEscort(
							assignRoutes.get(0).getCombinedFacility());
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
			finalTripEmployees = iCabRequestBO.getDropTripAllSortedEmployees(assignRoutes.get(0).getAssignRouteId());			
			//Rohit Change			
			hoursDate=finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getShiftTime();	
			if(finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime()!=null){
				hoursDate=finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime();
			}else{
				hoursDate=finalTripEmployees.get(0).geteFmFmEmployeeTravelRequest().getShiftTime();
			}
			
			localGender=new String(Base64.getDecoder()
					.decode(finalTripEmployees.get(finalTripEmployees.size() - 1).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),"utf-8");
			requestDateEscort=finalTripEmployees.get(finalTripEmployees.size() - 1).geteFmFmEmployeeTravelRequest().getRequestDate();
//			startEscortDate=sdf.parse(String.valueOf(po.getEscortStartTimeDrop()));
//		    endEscortDate=sdf.parse(String.valueOf(po.getEscortEndTimeDrop()));
			startEscortDate=po.getEscortStartTimeDrop();
		    endEscortDate=po.getEscortEndTimeDrop();
			
		    if ((assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()
					- 1) == finalTripEmployees
							.size()
					&& assignRoutes.get(0).getEscortRequredFlag()
							.equalsIgnoreCase(
									"N")
					&& escortProvideVerification(localGender,po.getEscortTimeWindowEnable(),po.getEscortRequired(),startEscortDate,endEscortDate,hoursDate,requestDateEscort,finalTripEmployees)
					) {
		    	responce.put("errorMessage", errorMsgForEscort("DROP",po.getEscortRequired()));
				responce.put("type", "DROP");
				responce.put("status", "notClose");
				log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			} else if ((assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()
					- 1) > finalTripEmployees
							.size()
					&& assignRoutes.get(0).getEscortRequredFlag()
							.equalsIgnoreCase(
									"N")
					&& escortProvideVerification(localGender,po.getEscortTimeWindowEnable(),po.getEscortRequired(),startEscortDate,endEscortDate,hoursDate,requestDateEscort,finalTripEmployees)
					) {
		    	responce.put("errorMessage", errorMsgForEscort("DROP",po.getEscortRequired()));
				responce.put("type", "DROP");
				responce.put("status", "notClose");
				log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
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
							int seqCount = 0;
							for (EFmFmEmployeeTripDetailPO tripDetailPO : employeeTripDetailPO) {
								seqCount++;
								String text = "Vehicle number "
										+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
												.getVehicleNumber()
										+ " "
										+ assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
												.getVehicleModel()
										+ " has been assigned for your drop for " + assignRoutes.get(0).getShiftTime()
										+ " shift.Seq Num- " + seqCount + " For feedback write to us @"
										+ assignRoutes.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();
								if (tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType()
										.equalsIgnoreCase("guest")) {
									String coPassengers = "";
									if (employeeTripDetailPO.size() > 1) {
										List<EFmFmEmployeeTripDetailPO> coPassengerList = iCabRequestBO1.getCoPassenger(
												assignRoutes.get(0).getAssignRouteId(),
												tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestId());

										for (EFmFmEmployeeTripDetailPO passenger : coPassengerList) {
											coPassengers = new String(
													Base64.getDecoder()
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
											+ shiftTimeFormater
													.format(tripDetailPO.geteFmFmEmployeeTravelRequest().getShiftTime())
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
											+ shiftTimeFormater
													.format(tripDetailPO.geteFmFmEmployeeTravelRequest().getShiftTime())
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

									// GHAP..Allocation message for Pickup Guest
									// and Host
									List<EFmFmUserMasterPO> hostDetails = userMasterBO
											.getEmployeeUserDetailFromMobileNumber(branchId,
													tripDetailPO.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
															.getHostMobileNumber());
									try {
										if (tripDetailPO.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
												.getDeviceType().contains("Android")) {
											pushNotification.notification(tripDetailPO.geteFmFmEmployeeTravelRequest()
													.getEfmFmUserMaster().getDeviceToken(), text);
											if ((!hostDetails.isEmpty()) && !(hostDetails.size() != 0))
												pushNotification.notification(hostDetails.get(0).getDeviceToken(),
														hostText);
										} else {
											pushNotification
													.iosPushNotification(tripDetailPO.geteFmFmEmployeeTravelRequest()
															.getEfmFmUserMaster().getDeviceToken(), text);
											if ((!hostDetails.isEmpty()) && !(hostDetails.size() != 0))
												pushNotification.iosPushNotification(
														hostDetails.get(0).getDeviceToken(), hostText);
										}

									} catch (Exception e) {
										log.info("PushStatus" + e);
									}
								} else {
									// EAP..Allocation message for drop Guest
									// and Host
									try {
										if (tripDetailPO.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
												.getDeviceType().contains("Android")) {
											pushNotification.notification(tripDetailPO.geteFmFmEmployeeTravelRequest()
													.getEfmFmUserMaster().getDeviceToken(), text);
										} else {
											pushNotification
													.iosPushNotification(tripDetailPO.geteFmFmEmployeeTravelRequest()
															.getEfmFmUserMaster().getDeviceToken(), text);

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
				//Rohit Changes
				localGender=new String(Base64.getDecoder()
				.decode(finalTripEmployees.get(finalTripEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
				.getEfmFmUserMaster().getGender()),"utf-8");
				if (escortProvideVerification(localGender,po.getEscortTimeWindowEnable(),po.getEscortRequired(),startEscortDate,endEscortDate,hoursDate,requestDateEscort,finalTripEmployees)) {
					List<EFmFmEscortCheckInPO> escortList = vehicleCheckIn.getAllCheckedInEscort(
							assignRoutes.get(0).getCombinedFacility());
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
		List<EFmFmAssignRoutePO> assignRoutesDetail = assignRouteBO.closeParticularTrips(assignRoutePO);
		EFmFmDriverMasterPO particularDriverDetails = approvalBO.getParticularDriverDetail(
				assignRoutesDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
		particularDriverDetails.setStatus("allocated");
		approvalBO.update(particularDriverDetails);

		List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckIn.getDeviceDetailsFromDeviceId(
				assignRoutesDetail.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),
				new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),
						assignRoutePO.getCombinedFacility()));
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
			List<EFmFmVehicleCheckInPO> checkInDetails = vehicleCheckIn.getCheckedInVehicleDetailsFromChecInAndBranchId(
					assignRoutesDetail.get(0).getEfmFmVehicleCheckIn().getCheckInId(), branchId);
			checkInDetails.get(0).setStatus("N");
			vehicleCheckIn.update(checkInDetails.get(0));
		} catch (Exception e) {
			log.info("error on bucket close update checkIn table" + e);
		}

		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/saveeditbucket")
	public Response editBucketSaveClick(EFmFmAssignRoutePO assignRoutePO) throws ParseException, IOException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		// log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
		//
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }

		// current Route Details
		List<EFmFmAssignRoutePO> assignRouteDetails = assignRouteBO.closeParticularTrips(assignRoutePO);
		assignRouteDetails.get(0).setTripUpdateTime(new Date());
		final List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO
				.getDropTripAllSortedEmployees(assignRouteDetails.get(0).getAssignRouteId());
		if (assignRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId() != assignRoutePO.getNewCheckInId()) {
			// Start Update Old CheckIn Entry Details
			List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
					assignRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
					assignRoutePO.geteFmFmClientBranchPO().getBranchId());

			if (oldCheckRoutesCheck.size() == 1) {
				EFmFmDriverMasterPO particularDriverDetails = approvalBO.getParticularDriverDetail(
						assignRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				particularDriverDetails.setStatus("A");
				approvalBO.update(particularDriverDetails);
				List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
						assignRouteDetails.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),
								assignRoutePO.getCombinedFacility()));
				deviceDetails.get(0).setStatus("Y");
				iVehicleCheckInBO.update(deviceDetails.get(0));
			}
			// old vehicle CheckIn Entries
			List<EFmFmVehicleCheckInPO> oldCheckInEntryDetail = iVehicleCheckInBO
					.getCheckedInVehicleDetailsFromChecInId(
							assignRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId());

			// New vehicle CheckIn Entries
			List<EFmFmVehicleCheckInPO> newCheckInEntryDetail = iVehicleCheckInBO
					.getCheckedInVehicleDetailsFromChecInId(assignRoutePO.getNewCheckInId());

			EFmFmVehicleMasterPO newVehicleDetail = iVehicleCheckInBO
					.getParticularVehicleDetail(newCheckInEntryDetail.get(0).getEfmFmVehicleMaster().getVehicleId());
			// int
			// usedCapacity=assignRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()-assignRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getAvailableSeat();

			if (employeeTripDetailPO.size() > (newVehicleDetail.getCapacity() - 1)) {
				responce.put("status", "lessCapacity");
				responce.put("capacity", employeeTripDetailPO.size());
				log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			final PushNotificationService pushNotification = new PushNotificationService();
			if (assignRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")
					&& !(newVehicleDetail.getVehicleNumber().contains("DUMMY"))) {
				final String vehicleNumber = newVehicleDetail.getVehicleNumber();
				Thread thread1 = new Thread(new Runnable() {
					@Override
					public void run() {

						try {
							for (EFmFmEmployeeTripDetailPO tripDetailPO : employeeTripDetailPO) {
								String text = "";
								if (tripDetailPO.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {

									text = "Dear employee your cab is changed,\nthe new cab is\n" + vehicleNumber
											+ "\nYour schedule pickup time is\n"
											+ tripDetailPO.geteFmFmEmployeeTravelRequest().getPickUpTime();
								} else {
									text = "Dear employee your cab is changed,\nthe new cab is\n" + vehicleNumber;
								}
								MessagingService messagingService = new MessagingService();
								messagingService.sendTripAsMessage(
										new String(Base64.getDecoder()
												.decode(tripDetailPO.geteFmFmEmployeeTravelRequest()
														.getEfmFmUserMaster().getMobileNumber())),
										text, tripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType());
								try {
									if (tripDetailPO.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
											.getDeviceType().contains("Android")) {
										pushNotification.notification(tripDetailPO.geteFmFmEmployeeTravelRequest()
												.getEfmFmUserMaster().getDeviceToken(), text);
									} else {
										pushNotification.iosPushNotification(tripDetailPO
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
												text);
									}
								} catch (Exception e) {
									log.info("Cab changed Msg" + e);
								}
							}
						} catch (Exception e) {
							log.info("Error" + e);
						}
					}
				});
				thread1.start();
			}

			EFmFmVehicleMasterPO updatePreviousVehicleDetail = iVehicleCheckInBO.getParticularVehicleDetail(
					assignRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());

			// removing planned distance
			// updatePreviousVehicleDetail
			// .setMonthlyPendingFixedDistance(updatePreviousVehicleDetail.getMonthlyPendingFixedDistance()
			// + assignRouteDetails.get(0).getPlannedDistance());

			if (oldCheckRoutesCheck.size() == 1) {
				double oldMonthlyFixedDistance = updatePreviousVehicleDetail.getMonthlyPendingFixedDistance()
						+ assignRouteDetails.get(0).getPlannedDistance();
				iVehicleCheckInBO.updateVehicleMonthlyDistance(updatePreviousVehicleDetail.getVehicleId(),
						oldMonthlyFixedDistance, "A");

			}

			// updatePreviousVehicleDetail.setStatus("A");
			// updatePreviousVehicleDetail.setAvailableSeat(updatePreviousVehicleDetail.getCapacity()-1);
			// iVehicleCheckInBO.update(updatePreviousVehicleDetail);
			// End Update Old CheckIn Entry Details

			// Start Update New CheckIn Entry Details
			EFmFmDriverMasterPO newDriverDetails = approvalBO
					.getParticularDriverDetail(newCheckInEntryDetail.get(0).getEfmFmDriverMaster().getDriverId());
			newDriverDetails.setStatus("allocated");
			approvalBO.update(newDriverDetails);

			List<EFmFmDeviceMasterPO> newDeviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
					newCheckInEntryDetail.get(0).geteFmFmDeviceMaster().getDeviceId(), new MultifacilityService()
							.combinedBranchIdDetails(assignRoutePO.getUserId(), assignRoutePO.getCombinedFacility()));
			newDeviceDetails.get(0).setStatus("allocated");
			iVehicleCheckInBO.update(newDeviceDetails.get(0));
			// newVehicleDetail.setStatus("allocated");
			// List<EFmFmAssignRoutePO>
			// newCheckRoutesCheck=assignRouteBO.getTripAllocatedRoute(newCheckInEntryDetail.get(0).getCheckInId(),
			// assignRoutePO.geteFmFmClientBranchPO().getBranchId());
			// if(!(newCheckRoutesCheck.isEmpty())){
			// newCheckInEntryDetail.get(0).setTotalTravelDistance(((newCheckInEntryDetail.get(0).getTotalTravelDistance()-newCheckRoutesCheck.get(0).getPlannedDistance())+assignRouteDetails.get(0).getPlannedDistance()));
			// newCheckInEntryDetail.get(0).setTotalTravelTime(((newCheckInEntryDetail.get(0).getTotalTravelTime()-newCheckRoutesCheck.get(0).getPlannedTime())+assignRouteDetails.get(0).getPlannedTime()));
			// newVehicleDetail.setMonthlyPendingFixedDistance((updatePreviousVehicleDetail.getMonthlyPendingFixedDistance()+newCheckRoutesCheck.get(0).getPlannedDistance())-assignRouteDetails.get(0).getPlannedDistance());
			// }
			// else{
			try {

				if (!(newCheckInEntryDetail.isEmpty())) {
					log.info("New vehicle");
					int numberOfTrips = newCheckInEntryDetail.get(0).getNumberOfTrips() + 1;
					long totalTravelTime = newCheckInEntryDetail.get(0).getTotalTravelTime()
							+ assignRouteDetails.get(0).getPlannedTime();
					double totalTravelDistance = newCheckInEntryDetail.get(0).getTotalTravelDistance()
							+ assignRouteDetails.get(0).getPlannedDistance();
					iVehicleCheckInBO.updateVehicleCheckInDetailsWithStatus(newCheckInEntryDetail.get(0).getCheckInId(),
							numberOfTrips, totalTravelTime, totalTravelDistance, "N");
					log.info("InSide setStatus Y" + newCheckInEntryDetail.get(0).getCheckInId());
				}

				double monthlyFixedDistance = newVehicleDetail.getMonthlyPendingFixedDistance()
						- assignRouteDetails.get(0).getPlannedDistance();
				iVehicleCheckInBO.updateVehicleMonthlyDistance(newVehicleDetail.getVehicleId(), monthlyFixedDistance,
						"allocated");
			} catch (Exception e) {
				log.info("Cab allocation updates");
			}

			// }
			// iVehicleCheckInBO.update(newVehicleDetail);

			// End Update New CheckIn Entry Details
			// newCheckInEntryDetail.get(0).setStatus("N");
			// iVehicleCheckInBO.update(newCheckInEntryDetail.get(0));

			// if (oldCheckRoutesCheck.size() == 1)
			// oldCheckInEntryDetail.get(0).setStatus("Y");

			// if (oldCheckInEntryDetail.get(0).getNumberOfTrips() != 0) {
			// log.info("InSide oldCheckInEntryDetail");
			// oldCheckInEntryDetail.get(0).setNumberOfTrips(oldCheckInEntryDetail.get(0).getNumberOfTrips()
			// - 1);
			// oldCheckInEntryDetail.get(0)
			// .setTotalTravelDistance((oldCheckInEntryDetail.get(0).getTotalTravelDistance()
			// - assignRouteDetails.get(0).getPlannedDistance()));
			// oldCheckInEntryDetail.get(0).setTotalTravelTime((oldCheckInEntryDetail.get(0).getTotalTravelTime()
			// - assignRouteDetails.get(0).getPlannedTime()));
			// }

			try {

				if (oldCheckInEntryDetail.get(0).getNumberOfTrips() != 0) {
					log.info("Old vehicle");
					int numberOfTrips = oldCheckInEntryDetail.get(0).getNumberOfTrips() - 1;
					long totalTravelTime = oldCheckInEntryDetail.get(0).getTotalTravelTime()
							- assignRouteDetails.get(0).getPlannedTime();
					double totalTravelDistance = oldCheckInEntryDetail.get(0).getTotalTravelDistance()
							- assignRouteDetails.get(0).getPlannedDistance();

					if (oldCheckRoutesCheck.size() == 1) {
						iVehicleCheckInBO.updateVehicleCheckInDetailsWithStatus(
								oldCheckInEntryDetail.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
								totalTravelDistance, "Y");
						log.info("InSide setStatus Y" + newCheckInEntryDetail.get(0).getCheckInId());
					} else {
						iVehicleCheckInBO.updateVehicleCheckInDetailsWithStatus(
								oldCheckInEntryDetail.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
								totalTravelDistance, "N");
						log.info("InSide setStatus N" + oldCheckInEntryDetail.get(0).getCheckInId());
					}
				}

				double oldVehiclemonthlyFixedDistance = newVehicleDetail.getMonthlyPendingFixedDistance()
						- assignRouteDetails.get(0).getPlannedDistance();
				iVehicleCheckInBO.updateVehicleMonthlyDistance(newVehicleDetail.getVehicleId(),
						oldVehiclemonthlyFixedDistance, "allocated");
			} catch (Exception e) {
				log.info("Cab allocation updates");
			}

			// iVehicleCheckInBO.update(oldCheckInEntryDetail.get(0));

			assignRouteDetails.get(0).setEfmFmVehicleCheckIn(newCheckInEntryDetail.get(0));
		}

		if (assignRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")
				&& assignRoutePO.getEscortCheckInId() != 0) {
			// Updating the change escort details .
			List<EFmFmEscortCheckInPO> changeCheckInEscortDetails = iVehicleCheckInBO
					.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
							assignRoutePO.getEscortCheckInId());
			changeCheckInEscortDetails.get(0).setStatus("N");
			iVehicleCheckInBO.update(changeCheckInEscortDetails.get(0));
			// Updating the previous allocated escort details
			try {
				List<EFmFmEscortCheckInPO> allocatedCheckInEscortDetails = iVehicleCheckInBO
						.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
								assignRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
				allocatedCheckInEscortDetails.get(0).setStatus("Y");
				iVehicleCheckInBO.update(allocatedCheckInEscortDetails.get(0));
			} catch (Exception e) {
			}
			assignRouteDetails.get(0).seteFmFmEscortCheckIn(changeCheckInEscortDetails.get(0));
		}
		assignRouteBO.update(assignRouteDetails.get(0));
		List<EFmFmAssignRoutePO> routeDetails = assignRouteBO.closeParticularTrips(assignRoutePO);
		responce.put("status", "success");
		responce.put("availableCapacity",
				(routeDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity() - 1)
						- employeeTripDetailPO.size());
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Path("/swapemployee")
	public Response swapEmployeeFromOneRouteToAnother(EFmFmAssignRoutePO assignRoutePO) throws Exception {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IVehicleCheckInBO vehicleCheckIn = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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

		// Courrent Route Details....
		final List<EFmFmAssignRoutePO> currentRouteDetails = assignRouteBO.closeParticularTrips(assignRoutePO);
		currentRouteDetails.get(0).setTripUpdateTime(new Date());
		assignRouteBO.update(currentRouteDetails.get(0));
		List<EFmFmEmployeeTripDetailPO> sortedDropEmployeeList = iCabRequestBO
				.getDropTripAllSortedEmployees(currentRouteDetails.get(0).getAssignRouteId());

		// Selected route Details
		EFmFmAssignRoutePO eFmFmAssignRoutePO = new EFmFmAssignRoutePO();
		eFmFmAssignRoutePO.setAssignRouteId(assignRoutePO.getSelectedAssignRouteId());
		EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(assignRoutePO.geteFmFmClientBranchPO().getBranchId());
		eFmFmAssignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		final List<EFmFmAssignRoutePO> selectedRouteDetails = assignRouteBO.closeParticularTrips(eFmFmAssignRoutePO);
		selectedRouteDetails.get(0).setTripUpdateTime(new Date());
		assignRouteBO.update(selectedRouteDetails.get(0));
		// Current route
		EFmFmAssignRoutePO currentAssignRoute = new EFmFmAssignRoutePO();
		currentAssignRoute.setAssignRouteId(currentRouteDetails.get(0).getAssignRouteId());
		List<EFmFmEmployeeTripDetailPO> selectedRouteEmployeesList = iCabRequestBO
				.getDropTripAllSortedEmployees(selectedRouteDetails.get(0).getAssignRouteId());

		//Rohit Changes 
		 userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		ArrayList<EFmFmClientBranchPO> alEscortLogingReturn=(ArrayList<EFmFmClientBranchPO>) userMasterBO.getEscortTimeDetails(new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
			EFmFmClientBranchPO po=alEscortLogingReturn.get(0);
			
			SimpleDateFormat sdf=new SimpleDateFormat("hh:MM:ss");
			String hoursData="";
			String localGender="";
			Date hoursDate=null;
			Date startEscortDate=new Date();
			Date endEscortDate=new Date();
			Date requestDateEscort=null;
			if(selectedRouteDetails.get(0).getTripType().equalsIgnoreCase("DROP")){
//				startEscortDate=sdf.parse(String.valueOf(po.getEscortStartTimeDrop()));
//				endEscortDate=sdf.parse(String.valueOf(po.getEscortEndTimeDrop()));
				startEscortDate=po.getEscortStartTimeDrop();
			    endEscortDate=po.getEscortEndTimeDrop();
			}if(selectedRouteDetails.get(0).getTripType().equalsIgnoreCase("PICKUP")){
//				startEscortDate=sdf.parse(String.valueOf(po.getEscortStartTimePickup()));
//				endEscortDate=sdf.parse(String.valueOf(po.getEscortEndTimePickup()));
				startEscortDate=po.getEscortStartTimePickup();
			    endEscortDate=po.getEscortEndTimePickup();
			}
		
		
		if ((selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()
				- 1) == selectedRouteEmployeesList.size()) {
			final List<EFmFmEmployeeTripDetailPO> requestTripDetail = iCabRequestBO
					.getParticularTriprEmployeeBoardedStatus(assignRoutePO.getRequestId(),
							currentRouteDetails.get(0).getAssignRouteId());
			requestTripDetail.get(0).setEfmFmAssignRoute(selectedRouteDetails.get(0));
			iCabRequestBO.update(requestTripDetail.get(0));
			List<EFmFmEmployeeTripDetailPO> tripEmployees = iCabRequestBO
					.getParticularTripAllEmployees(selectedRouteDetails.get(0).getAssignRouteId());
			List<EFmFmEmployeeTripDetailPO> allSortedEmployees = iCabRequestBO
					.getDropTripAllSortedEmployees(selectedRouteDetails.get(0).getAssignRouteId());
			//Rohit Changes
//			hoursDate=allSortedEmployees.get(allSortedEmployees.size() - 1).geteFmFmEmployeeTravelRequest().getShiftTime();
			if(allSortedEmployees.get(allSortedEmployees.size() - 1).geteFmFmEmployeeTravelRequest().getPickUpTime()!=null){
				hoursDate=allSortedEmployees.get(allSortedEmployees.size() - 1).geteFmFmEmployeeTravelRequest().getPickUpTime();
			}else{
				hoursDate=allSortedEmployees.get(allSortedEmployees.size() - 1).geteFmFmEmployeeTravelRequest().getShiftTime();
			}
			 localGender=new String(Base64.getDecoder().decode(allSortedEmployees.get(allSortedEmployees.size() - 1)
						.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),"utf-8");
			 requestDateEscort=tripEmployees.get(0).geteFmFmEmployeeTravelRequest().getRequestDate();
			
			 if (escortProvideVerification(localGender,po.getEscortTimeWindowEnable(),po.getEscortRequired(),startEscortDate,endEscortDate,hoursDate,requestDateEscort,sortedDropEmployeeList)
						&& selectedRouteDetails.get(0).getTripType().equalsIgnoreCase("DROP")
						&& selectedRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("N")
						) {
				requestTripDetail.get(0).setEfmFmAssignRoute(currentRouteDetails.get(0));
				iCabRequestBO.update(requestTripDetail.get(0));
				responce.put("status", "failed");
				log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			} else if (escortProvideVerification(new String(
					Base64.getDecoder().decode(tripEmployees.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),"utf-8")
					,po.getEscortTimeWindowEnable(),po.getEscortRequired(),startEscortDate,endEscortDate,hoursDate,tripEmployees.get(0).geteFmFmEmployeeTravelRequest().getRequestDate(),sortedDropEmployeeList)
					&& selectedRouteDetails.get(0).getTripType().equalsIgnoreCase("PICKUP")
					&& selectedRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("N")
					) {
				requestTripDetail.get(0).setEfmFmAssignRoute(currentRouteDetails.get(0));
				iCabRequestBO.update(requestTripDetail.get(0));
				responce.put("status", "failed");
				log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			} else {
				selectedRouteDetails.get(0).setVehicleStatus("F");
				iCabRequestBO.update(selectedRouteDetails.get(0));

				EFmFmVehicleMasterPO updateSelectedRouteVehicleCapacity = vehicleCheckIn.getParticularVehicleDetail(
						selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				if (selectedRouteDetails.get(0).getEscortRequredFlag()
						.equalsIgnoreCase(
								"Y")
						&& new String(Base64.getDecoder().decode(requestTripDetail.get(0)
								.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()), "utf-8")
										.equalsIgnoreCase("Male")) {
					vehicleCheckIn.update(updateSelectedRouteVehicleCapacity);
					try {
						List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
								.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
										selectedRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
						checkInEscortDetails.get(0).setStatus("Y");
						vehicleCheckIn.update(checkInEscortDetails.get(0));
						selectedRouteDetails.get(0).setVehicleStatus("A");
						selectedRouteDetails.get(0).setEscortRequredFlag("N");
						iCabRequestBO.update(selectedRouteDetails.get(0));

					} catch (Exception e) {
						selectedRouteDetails.get(0).setVehicleStatus("A");
						selectedRouteDetails.get(0).setEscortRequredFlag("N");
						iCabRequestBO.update(selectedRouteDetails.get(0));
					}
				} else {
					// updateSelectedRouteVehicleCapacity.setAvailableSeat(updateSelectedRouteVehicleCapacity.getAvailableSeat()-1);
					vehicleCheckIn.update(updateSelectedRouteVehicleCapacity);

				}
				// Update Current Route Details
				EFmFmVehicleMasterPO currentRouteVehicleCapacity = vehicleCheckIn.getParticularVehicleDetail(
						currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				// Updating current route Details
				if (currentRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")
						&& currentRouteDetails.get(0).getTripType().equalsIgnoreCase("DROP")) {
					// List<EFmFmEmployeeTripDetailPO>
					// dropEmployeeSortedList=iCabRequestBO.getDropTripAllSortedEmployees(currentRouteDetails.get(0).getAssignRouteId());
					if (sortedDropEmployeeList.get(sortedDropEmployeeList.size() - 1)
							.getEmpTripId() == requestTripDetail.get(0).getEmpTripId()) {
						// currentRouteVehicleCapacity.setAvailableSeat(currentRouteVehicleCapacity.getAvailableSeat()+2);
						vehicleCheckIn.update(currentRouteVehicleCapacity);
						currentRouteVehicleCapacity = vehicleCheckIn.getParticularVehicleDetail(currentRouteDetails
								.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
						if (currentRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
							try {
								List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
										.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
												currentRouteDetails.get(0).geteFmFmEscortCheckIn()
														.getEscortCheckInId());
								checkInEscortDetails.get(0).setStatus("Y");
								vehicleCheckIn.update(checkInEscortDetails.get(0));
								currentRouteDetails.get(0).setVehicleStatus("A");
								currentRouteDetails.get(0).setEscortRequredFlag("N");
								iCabRequestBO.update(currentRouteDetails.get(0));

							} catch (Exception e) {
								currentRouteDetails.get(0).setVehicleStatus("A");
								currentRouteDetails.get(0).setEscortRequredFlag("N");
								iCabRequestBO.update(currentRouteDetails.get(0));
							}

						}

						if ((currentRouteVehicleCapacity.getCapacity()
								- sortedDropEmployeeList.size()) == (currentRouteVehicleCapacity.getCapacity() - 1)) {
							// currentRouteVehicleCapacity.setStatus("A");
							EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
							eFmFmVehicleCheckInPO.setCheckInTime(new Date());
							eFmFmVehicleCheckInPO
									.setCheckInId(currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId());
							List<EFmFmVehicleCheckInPO> vehicleCheck = vehicleCheckIn
									.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
							vehicleCheck.get(0).setStatus("Y");
							vehicleCheckIn.update(vehicleCheck.get(0));
							List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
									.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
											currentRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
							checkInEscortDetails.get(0).setStatus("Y");
							vehicleCheckIn.update(checkInEscortDetails.get(0));

							EFmFmDriverMasterPO particularDriverDetails = approvalBO
									.getParticularDriverDetail(currentRouteDetails.get(0).getEfmFmVehicleCheckIn()
											.getEfmFmDriverMaster().getDriverId());
							particularDriverDetails.setStatus("A");
							approvalBO.update(particularDriverDetails);
							List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckIn.getDeviceDetailsFromDeviceId(
									currentRouteDetails.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster()
											.getDeviceId(),
									new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),
											eFmFmClientBranchPO.getCombinedFacility()));
							deviceDetails.get(0).setStatus("Y");
							vehicleCheckIn.update(deviceDetails.get(0));
							// when trip will start then this code will work
							// start
							List<EFmFmTripAlertsPO> allAlerts = iAlertBO.getParticularTripAlerts(
									currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
									currentRouteDetails.get(0).getAssignRouteId());
							if (!(allAlerts.isEmpty()) || allAlerts.size() != 0) {
								for (EFmFmTripAlertsPO tripAlertsPO : allAlerts) {
									iAlertBO.deleteAllAlerts(tripAlertsPO.getTripAlertsId());
								}
							}
							List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
									.getRouteDetailsFromAssignRouteId(
											currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
											currentRouteDetails.get(0).getAssignRouteId());
							if (!(actualRouteTravelled.isEmpty())) {
								for (EFmFmLiveRoutTravelledPO actulaTraveled : actualRouteTravelled) {
									assignRouteBO
											.deleteParticularActualTravelled(actulaTraveled.getLiveRouteTravelId());
								}
							}
							// End

							// Auto Vehicle allocation start Code
							List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
									currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
									currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId());
							try {
								if (vehicleCheck.get(0).getNumberOfTrips() != 0) {
									int numberOfTrips = vehicleCheck.get(0).getNumberOfTrips() - 1;
									long totalTravelTime = vehicleCheck.get(0).getTotalTravelTime()
											- currentRouteDetails.get(0).getPlannedTime();
									double totalTravelDistance = vehicleCheck.get(0).getTotalTravelDistance()
											- currentRouteDetails.get(0).getPlannedDistance();
									if (oldCheckRoutesCheck.size() == 1) {
										vehicleCheckIn.updateVehicleCheckInDetailsWithStatus(
												vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
												totalTravelDistance, "Y");
										log.info("InSide setStatus Y" + vehicleCheck.get(0).getCheckInId());
									} else {
										vehicleCheckIn.updateVehicleCheckInDetailsWithOutStatus(
												vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
												totalTravelDistance);
										log.info("InSide setStatus N" + vehicleCheck.get(0).getCheckInId());
									}
								}
								double monthlyFixedDistance = currentRouteVehicleCapacity
										.getMonthlyPendingFixedDistance()
										+ currentRouteDetails.get(0).getPlannedDistance();
								vehicleCheckIn.updateVehicleMonthlyDistance(currentRouteVehicleCapacity.getVehicleId(),
										monthlyFixedDistance, "A");
							} catch (Exception e) {
								log.info("Cab allocation updates");
							}
							// Auto Vehicle allocation end Code

							assignRouteBO.deleteParticularAssignRoute(currentRouteDetails.get(0).getAssignRouteId());
							// vehicleCheckIn.update(currentRouteVehicleCapacity);
						}

					} else {
						// currentRouteVehicleCapacity.setAvailableSeat(currentRouteVehicleCapacity.getAvailableSeat()+2);
						vehicleCheckIn.update(currentRouteVehicleCapacity);
						currentRouteVehicleCapacity = vehicleCheckIn.getParticularVehicleDetail(currentRouteDetails
								.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
						if (currentRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
							try {
								List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
										.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
												currentRouteDetails.get(0).geteFmFmEscortCheckIn()
														.getEscortCheckInId());
								checkInEscortDetails.get(0).setStatus("Y");
								vehicleCheckIn.update(checkInEscortDetails.get(0));
								currentRouteDetails.get(0).setVehicleStatus("A");
								currentRouteDetails.get(0).setEscortRequredFlag("N");
								iCabRequestBO.update(currentRouteDetails.get(0));

							} catch (Exception e) {
								currentRouteDetails.get(0).setVehicleStatus("A");
								currentRouteDetails.get(0).setEscortRequredFlag("N");
								iCabRequestBO.update(currentRouteDetails.get(0));
							}

						}

						if ((currentRouteVehicleCapacity.getCapacity()
								- sortedDropEmployeeList.size()) == (currentRouteVehicleCapacity.getCapacity() - 1)) {
							currentRouteVehicleCapacity.setStatus("A");
							EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
							eFmFmVehicleCheckInPO.setCheckInTime(new Date());
							eFmFmVehicleCheckInPO
									.setCheckInId(currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId());
							List<EFmFmVehicleCheckInPO> vehicleCheck = vehicleCheckIn
									.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
							// vehicleCheck.get(0).setStatus("Y");
							// vehicleCheckIn.update(vehicleCheck.get(0));
							List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
									.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
											currentRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
							checkInEscortDetails.get(0).setStatus("Y");
							vehicleCheckIn.update(checkInEscortDetails.get(0));

							EFmFmDriverMasterPO particularDriverDetails = approvalBO
									.getParticularDriverDetail(currentRouteDetails.get(0).getEfmFmVehicleCheckIn()
											.getEfmFmDriverMaster().getDriverId());
							particularDriverDetails.setStatus("A");
							approvalBO.update(particularDriverDetails);
							List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckIn.getDeviceDetailsFromDeviceId(
									currentRouteDetails.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster()
											.getDeviceId(),
									new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),
											eFmFmClientBranchPO.getCombinedFacility()));
							deviceDetails.get(0).setStatus("Y");
							vehicleCheckIn.update(deviceDetails.get(0));
							// when trip will start then this code will work
							// start
							List<EFmFmTripAlertsPO> allAlerts = iAlertBO.getParticularTripAlerts(
									currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
									currentRouteDetails.get(0).getAssignRouteId());
							if (!(allAlerts.isEmpty())) {
								for (EFmFmTripAlertsPO tripAlertsPO : allAlerts) {
									iAlertBO.deleteAllAlerts(tripAlertsPO.getTripAlertsId());
								}
							}
							List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
									.getRouteDetailsFromAssignRouteId(
											currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
											currentRouteDetails.get(0).getAssignRouteId());
							if (!(actualRouteTravelled.isEmpty())) {
								for (EFmFmLiveRoutTravelledPO actulaTraveled : actualRouteTravelled) {
									assignRouteBO
											.deleteParticularActualTravelled(actulaTraveled.getLiveRouteTravelId());
								}
							}

							// End

							// Auto Vehicle allocation start Code
							try {
								List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
										currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
										currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId());
								if (vehicleCheck.get(0).getNumberOfTrips() != 0) {
									int numberOfTrips = vehicleCheck.get(0).getNumberOfTrips() - 1;
									long totalTravelTime = vehicleCheck.get(0).getTotalTravelTime()
											- currentRouteDetails.get(0).getPlannedTime();
									double totalTravelDistance = vehicleCheck.get(0).getTotalTravelDistance()
											- currentRouteDetails.get(0).getPlannedDistance();
									if (oldCheckRoutesCheck.size() == 1) {
										vehicleCheckIn.updateVehicleCheckInDetailsWithStatus(
												vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
												totalTravelDistance, "Y");
										log.info("InSide setStatus Y" + vehicleCheck.get(0).getCheckInId());
									} else {
										vehicleCheckIn.updateVehicleCheckInDetailsWithOutStatus(
												vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
												totalTravelDistance);
										log.info("InSide setStatus N" + vehicleCheck.get(0).getCheckInId());
									}
								}
								double monthlyFixedDistance = currentRouteVehicleCapacity
										.getMonthlyPendingFixedDistance()
										+ currentRouteDetails.get(0).getPlannedDistance();
								vehicleCheckIn.updateVehicleMonthlyDistance(currentRouteVehicleCapacity.getVehicleId(),
										monthlyFixedDistance, "A");
							} catch (Exception e) {
								log.info("Cab allocation updates");
							}
							// Auto Vehicle allocation end Code

							assignRouteBO.deleteParticularAssignRoute(currentRouteDetails.get(0).getAssignRouteId());
							// vehicleCheckIn.update(currentRouteVehicleCapacity);
						}
					}
				} else if (currentRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")
						&& currentRouteDetails.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
					List<EFmFmEmployeeTripDetailPO> tripEmployeesForCurrentRoute = iCabRequestBO
							.getParticularTripAllEmployees(currentRouteDetails.get(0).getAssignRouteId());
					if (tripEmployeesForCurrentRoute.get(0).getEmpTripId() == requestTripDetail.get(0).getEmpTripId()) {
						// currentRouteVehicleCapacity.setAvailableSeat(currentRouteVehicleCapacity.getAvailableSeat()+2);
						vehicleCheckIn.update(currentRouteVehicleCapacity);
						currentRouteVehicleCapacity = vehicleCheckIn.getParticularVehicleDetail(currentRouteDetails
								.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
						if (currentRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
							try {
								List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
										.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
												currentRouteDetails.get(0).geteFmFmEscortCheckIn()
														.getEscortCheckInId());
								checkInEscortDetails.get(0).setStatus("Y");
								vehicleCheckIn.update(checkInEscortDetails.get(0));
								currentRouteDetails.get(0).setVehicleStatus("A");
								currentRouteDetails.get(0).setEscortRequredFlag("N");
								iCabRequestBO.update(currentRouteDetails.get(0));

							} catch (Exception e) {
								currentRouteDetails.get(0).setVehicleStatus("A");
								currentRouteDetails.get(0).setEscortRequredFlag("N");
								iCabRequestBO.update(currentRouteDetails.get(0));
							}

						}
						if ((currentRouteVehicleCapacity.getCapacity()
								- tripEmployeesForCurrentRoute.size()) == (currentRouteVehicleCapacity.getCapacity()
										- 1)) {
							currentRouteVehicleCapacity.setStatus("A");
							EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
							eFmFmVehicleCheckInPO.setCheckInTime(new Date());
							eFmFmVehicleCheckInPO
									.setCheckInId(currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId());
							List<EFmFmVehicleCheckInPO> vehicleCheck = vehicleCheckIn
									.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
							// vehicleCheck.get(0).setStatus("Y");
							// vehicleCheckIn.update(vehicleCheck.get(0));
							List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
									.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
											currentRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
							checkInEscortDetails.get(0).setStatus("Y");
							vehicleCheckIn.update(checkInEscortDetails.get(0));

							EFmFmDriverMasterPO particularDriverDetails = approvalBO
									.getParticularDriverDetail(currentRouteDetails.get(0).getEfmFmVehicleCheckIn()
											.getEfmFmDriverMaster().getDriverId());
							particularDriverDetails.setStatus("A");
							approvalBO.update(particularDriverDetails);
							List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckIn.getDeviceDetailsFromDeviceId(
									currentRouteDetails.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster()
											.getDeviceId(),
									new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),
											eFmFmClientBranchPO.getCombinedFacility()));
							deviceDetails.get(0).setStatus("Y");
							vehicleCheckIn.update(deviceDetails.get(0));
							// when trip will start then this code will work
							// start
							List<EFmFmTripAlertsPO> allAlerts = iAlertBO.getParticularTripAlerts(
									currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
									currentRouteDetails.get(0).getAssignRouteId());
							if (!(allAlerts.isEmpty())) {
								for (EFmFmTripAlertsPO tripAlertsPO : allAlerts) {
									iAlertBO.deleteAllAlerts(tripAlertsPO.getTripAlertsId());
								}
							}
							List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
									.getRouteDetailsFromAssignRouteId(
											currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
											currentRouteDetails.get(0).getAssignRouteId());
							if (!(actualRouteTravelled.isEmpty())) {
								for (EFmFmLiveRoutTravelledPO actulaTraveled : actualRouteTravelled) {
									assignRouteBO
											.deleteParticularActualTravelled(actulaTraveled.getLiveRouteTravelId());
								}
							}
							// End

							// Auto Vehicle allocation start Code
							try {
								List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
										currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
										currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId());
								if (vehicleCheck.get(0).getNumberOfTrips() != 0) {
									int numberOfTrips = vehicleCheck.get(0).getNumberOfTrips() - 1;
									long totalTravelTime = vehicleCheck.get(0).getTotalTravelTime()
											- currentRouteDetails.get(0).getPlannedTime();
									double totalTravelDistance = vehicleCheck.get(0).getTotalTravelDistance()
											- currentRouteDetails.get(0).getPlannedDistance();
									if (oldCheckRoutesCheck.size() == 1) {
										vehicleCheckIn.updateVehicleCheckInDetailsWithStatus(
												vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
												totalTravelDistance, "Y");
										log.info("InSide setStatus Y" + vehicleCheck.get(0).getCheckInId());
									} else {
										vehicleCheckIn.updateVehicleCheckInDetailsWithOutStatus(
												vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
												totalTravelDistance);
										log.info("InSide setStatus N" + vehicleCheck.get(0).getCheckInId());
									}
								}
								double monthlyFixedDistance = currentRouteVehicleCapacity
										.getMonthlyPendingFixedDistance()
										+ currentRouteDetails.get(0).getPlannedDistance();
								vehicleCheckIn.updateVehicleMonthlyDistance(currentRouteVehicleCapacity.getVehicleId(),
										monthlyFixedDistance, "A");
							} catch (Exception e) {
								log.info("Cab allocation updates");
							}
							// Auto Vehicle allocation end Code

							assignRouteBO.deleteParticularAssignRoute(currentRouteDetails.get(0).getAssignRouteId());
							// vehicleCheckIn.update(currentRouteVehicleCapacity);
						}

					}
				} else {
					// currentRouteVehicleCapacity.setAvailableSeat(currentRouteVehicleCapacity.getAvailableSeat()+1);
					List<EFmFmEmployeeTripDetailPO> tripEmployeesForCurrentRoute = iCabRequestBO
							.getParticularTripAllEmployees(currentRouteDetails.get(0).getAssignRouteId());
					if ((currentRouteVehicleCapacity.getCapacity()
							- tripEmployeesForCurrentRoute.size()) == (currentRouteVehicleCapacity.getCapacity() - 1)) {
						currentRouteVehicleCapacity.setStatus("A");
						EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
						eFmFmVehicleCheckInPO.setCheckInTime(new Date());
						eFmFmVehicleCheckInPO
								.setCheckInId(currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId());
						List<EFmFmVehicleCheckInPO> vehicleCheck = vehicleCheckIn
								.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
						// vehicleCheck.get(0).setStatus("Y");
						// vehicleCheckIn.update(vehicleCheck.get(0));

						EFmFmDriverMasterPO particularDriverDetails = approvalBO
								.getParticularDriverDetail(currentRouteDetails.get(0).getEfmFmVehicleCheckIn()
										.getEfmFmDriverMaster().getDriverId());
						particularDriverDetails.setStatus("A");
						approvalBO.update(particularDriverDetails);
						List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckIn.getDeviceDetailsFromDeviceId(
								currentRouteDetails.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster()
										.getDeviceId(),
								new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),
										eFmFmClientBranchPO.getCombinedFacility()));
						deviceDetails.get(0).setStatus("Y");
						vehicleCheckIn.update(deviceDetails.get(0));
						// when trip will start then this code will work start
						List<EFmFmTripAlertsPO> allAlerts = iAlertBO.getParticularTripAlerts(
								currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
								currentRouteDetails.get(0).getAssignRouteId());
						if (!(allAlerts.isEmpty())) {
							for (EFmFmTripAlertsPO tripAlertsPO : allAlerts) {
								iAlertBO.deleteAllAlerts(tripAlertsPO.getTripAlertsId());
							}
						}
						List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
								.getRouteDetailsFromAssignRouteId(
										currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
										currentRouteDetails.get(0).getAssignRouteId());
						if (!(actualRouteTravelled.isEmpty())) {
							for (EFmFmLiveRoutTravelledPO actulaTraveled : actualRouteTravelled) {
								assignRouteBO.deleteParticularActualTravelled(actulaTraveled.getLiveRouteTravelId());
							}
						}
						// End

						// Auto Vehicle allocation start Code
						try {
							List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
									currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
									currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId());
							if (vehicleCheck.get(0).getNumberOfTrips() != 0) {
								int numberOfTrips = vehicleCheck.get(0).getNumberOfTrips() - 1;
								long totalTravelTime = vehicleCheck.get(0).getTotalTravelTime()
										- currentRouteDetails.get(0).getPlannedTime();
								double totalTravelDistance = vehicleCheck.get(0).getTotalTravelDistance()
										- currentRouteDetails.get(0).getPlannedDistance();
								if (oldCheckRoutesCheck.size() == 1) {
									vehicleCheckIn.updateVehicleCheckInDetailsWithStatus(
											vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
											totalTravelDistance, "Y");
									log.info("InSide setStatus Y" + vehicleCheck.get(0).getCheckInId());
								} else {
									vehicleCheckIn.updateVehicleCheckInDetailsWithOutStatus(
											vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
											totalTravelDistance);
									log.info("InSide setStatus N" + vehicleCheck.get(0).getCheckInId());
								}
							}
							double monthlyFixedDistance = currentRouteVehicleCapacity.getMonthlyPendingFixedDistance()
									+ currentRouteDetails.get(0).getPlannedDistance();
							vehicleCheckIn.updateVehicleMonthlyDistance(currentRouteVehicleCapacity.getVehicleId(),
									monthlyFixedDistance, "A");
						} catch (Exception e) {
							log.info("Cab allocation updates");
						}
						// Auto Vehicle allocation end Code

						assignRouteBO.deleteParticularAssignRoute(currentRouteDetails.get(0).getAssignRouteId());
						vehicleCheckIn.update(currentRouteVehicleCapacity);
					} else
						vehicleCheckIn.update(currentRouteVehicleCapacity);
				}
				log.info("currentRouteDetails.get(0)" + currentRouteDetails.get(0).getAssignRouteId());
				log.info("selectedRouteDetails.get(0)" + selectedRouteDetails.get(0).getAssignRouteId());
				final PushNotificationService pushNotification = new PushNotificationService();
				Thread thread1 = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							String text = "";

							MessagingService messagingService = new MessagingService();
							if (selectedRouteDetails.get(0).getTripType().equalsIgnoreCase("PICKUP")
									&& currentRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")
									&& selectedRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")) {
								text = "Dear employee your cab is changed,\nthe new cab is\n"
										+ selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
												.getVehicleNumber()
										+ "\nYour schedule pickup time is\n"
										+ shiftTimeFormater.format(requestTripDetail.get(0)
												.geteFmFmEmployeeTravelRequest().getPickUpTime());
								messagingService.sendTripAsMessage(
										new String(Base64.getDecoder()
												.decode(requestTripDetail.get(0).geteFmFmEmployeeTravelRequest()
														.getEfmFmUserMaster().getMobileNumber())),
										text,
										requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
								try {
									if (requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
											.getDeviceType().contains("Android")) {
										pushNotification.notification(requestTripDetail.get(0)
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
												text);
									} else {
										pushNotification.iosPushNotification(requestTripDetail.get(0)
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
												text);
									}
								} catch (Exception e) {
									log.info("Cab Changed Msg" + e);
								}
							}
							if (selectedRouteDetails.get(0).getTripType().equalsIgnoreCase("PICKUP")
									&& currentRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("N")
									&& selectedRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")) {
								// text="Dear employee cab is allocated for your
								// pickup\n"+selectedRouteDetails.get(0).getShiftTime()+"
								// shift\nAllocated Cab number
								// is\n"+selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber()+"
								// \nyour pickup time is
								// "+requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime()+"\nVehicle
								// Type
								// "+selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel();
								text = "Vehicle number "
										+ selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
												.getVehicleNumber()
										+ " "
										+ selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
												.getVehicleModel()
										+ " has been assigned for your pickup for "
										+ shiftTimeFormater.format(selectedRouteDetails.get(0).getShiftTime())
										+ " shift.\nBoarding Time is "
										+ shiftTimeFormater.format(requestTripDetail.get(0)
												.geteFmFmEmployeeTravelRequest().getPickUpTime())
										+ "\nFor feedback write to us @"
										+ selectedRouteDetails.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();

								messagingService.sendTripAsMessage(
										new String(Base64.getDecoder()
												.decode(requestTripDetail.get(0).geteFmFmEmployeeTravelRequest()
														.getEfmFmUserMaster().getMobileNumber())),
										text,
										requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
								try {
									if (requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
											.getDeviceType().contains("Android")) {
										pushNotification.notification(requestTripDetail.get(0)
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
												text);
									} else {
										pushNotification.iosPushNotification(requestTripDetail.get(0)
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
												text);

									}
								} catch (Exception e) {
									log.info("Cab Changed Msg" + e);
								}
							}
							if (selectedRouteDetails.get(0).getTripType().equalsIgnoreCase("DROP")
									&& currentRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")
									&& selectedRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")) {
								// text="Dear employee your cab is changed,\nthe
								// new cab
								// is\n"+selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber();
								text = "Vehicle number "
										+ selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
												.getVehicleNumber()
										+ " "
										+ selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
												.getVehicleModel()
										+ " has been assigned for your drop for "
										+ shiftTimeFormater.format(selectedRouteDetails.get(0).getShiftTime())
										+ " shift.\nFor feedback write to us @"
										+ selectedRouteDetails.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();

								messagingService.sendTripAsMessage(
										new String(Base64.getDecoder()
												.decode(requestTripDetail.get(0).geteFmFmEmployeeTravelRequest()
														.getEfmFmUserMaster().getMobileNumber())),
										text,
										requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
								try {
									if (requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
											.getDeviceType().contains("Android")) {
										pushNotification.notification(requestTripDetail.get(0)
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
												text);
									} else {
										pushNotification.iosPushNotification(requestTripDetail.get(0)
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
												text);
									}
								} catch (Exception e) {
									log.info("Cab Changed Msg" + e);
								}
							}
							if (selectedRouteDetails.get(0).getTripType().equalsIgnoreCase("DROP")
									&& currentRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("N")
									&& selectedRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")) {
								// text="Dear employee cab is allocated for your
								// pickup\n"+selectedRouteDetails.get(0).getShiftTime()+"
								// shift\nAllocated Cab number
								// is\n"+selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber()+"
								// \nyour pickup time is
								// "+requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime()+"\nVehicle
								// Type
								// "+selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel();
								text = "Vehicle number "
										+ selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
												.getVehicleNumber()
										+ "."
										+ selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
												.getVehicleModel()
										+ " has been assigned for your drop for "
										+ selectedRouteDetails.get(0).getShiftTime()
										+ " shift.\nFor feedback write to us @"
										+ selectedRouteDetails.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();

								messagingService.sendTripAsMessage(
										new String(Base64.getDecoder()
												.decode(requestTripDetail.get(0).geteFmFmEmployeeTravelRequest()
														.getEfmFmUserMaster().getMobileNumber())),
										text,
										requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
								try {
									if (requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
											.getDeviceType().contains("Android")) {
										pushNotification.notification(requestTripDetail.get(0)
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
												text);
									} else {
										pushNotification.iosPushNotification(requestTripDetail.get(0)
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
												text);

									}
								} catch (Exception e) {
									log.info("Cab Changed Msg" + e);
								}
							}
						} catch (Exception e) {
						}
					}

				});
				thread1.start();
				List<EFmFmEmployeeTripDetailPO> tripEmployeesForCurrentRoute = iCabRequestBO
						.getParticularTripAllEmployees(currentRouteDetails.get(0).getAssignRouteId());
				List<EFmFmEmployeeTripDetailPO> tripEmployeesForSelectedRoute = iCabRequestBO
						.getParticularTripAllEmployees(selectedRouteDetails.get(0).getAssignRouteId());

				responce.put("currentRouteAvailableCapacity",
						(currentRouteVehicleCapacity.getCapacity() - 1) - tripEmployeesForCurrentRoute.size());
				responce.put("selectedRouteAvailableCapacity",
						(updateSelectedRouteVehicleCapacity.getCapacity() - 1) - tripEmployeesForSelectedRoute.size());

			}
		} else {
			final List<EFmFmEmployeeTripDetailPO> requestTripDetail = iCabRequestBO
					.getParticularTriprEmployeeBoardedStatus(assignRoutePO.getRequestId(),
							currentRouteDetails.get(0).getAssignRouteId());
			requestTripDetail.get(0).setEfmFmAssignRoute(selectedRouteDetails.get(0));
			// Updating selected route details
			EFmFmVehicleMasterPO selectedRouteVehicleCapacity = vehicleCheckIn.getParticularVehicleDetail(
					selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
			// selectedRouteVehicleCapacity.setAvailableSeat(selectedRouteVehicleCapacity.getAvailableSeat()-1);
			vehicleCheckIn.update(selectedRouteVehicleCapacity);
			iCabRequestBO.update(requestTripDetail.get(0));
			EFmFmVehicleMasterPO currentRouteVehicleCapacity = vehicleCheckIn.getParticularVehicleDetail(
					currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
			if (currentRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y") && new String(
					Base64.getDecoder().decode(
							requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),
					"utf-8").equalsIgnoreCase("Female")) {
				vehicleCheckIn.update(currentRouteVehicleCapacity);
				try {
					List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
							.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
									currentRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
					checkInEscortDetails.get(0).setStatus("Y");
					vehicleCheckIn.update(checkInEscortDetails.get(0));
					selectedRouteDetails.get(0).setVehicleStatus("A");
					selectedRouteDetails.get(0).setEscortRequredFlag("N");
					iCabRequestBO.update(selectedRouteDetails.get(0));

				} catch (Exception e) {
					selectedRouteDetails.get(0).setVehicleStatus("A");
					selectedRouteDetails.get(0).setEscortRequredFlag("N");
					iCabRequestBO.update(selectedRouteDetails.get(0));
				}
			}
			/*
			 * else{ currentRouteVehicleCapacity.setAvailableSeat(
			 * currentRouteVehicleCapacity.getAvailableSeat()+1);
			 * vehicleCheckIn.update(currentRouteVehicleCapacity);
			 * 
			 * }
			 */
			// Updating current route Details
			if (currentRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")
					&& currentRouteDetails.get(0).getTripType().equalsIgnoreCase("DROP")) {
				// List<EFmFmEmployeeTripDetailPO>
				// dropEmployeeSortedList=iCabRequestBO.getDropTripAllSortedEmployees(currentRouteDetails.get(0).getAssignRouteId());
				if (sortedDropEmployeeList.get(sortedDropEmployeeList.size() - 1).getEmpTripId() == requestTripDetail
						.get(0).getEmpTripId()) {
					// currentRouteVehicleCapacity.setAvailableSeat(currentRouteVehicleCapacity.getAvailableSeat()+2);
					vehicleCheckIn.update(currentRouteVehicleCapacity);
					currentRouteVehicleCapacity = vehicleCheckIn.getParticularVehicleDetail(
							currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
					if (currentRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
						try {
							List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
									.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
											currentRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
							checkInEscortDetails.get(0).setStatus("Y");
							vehicleCheckIn.update(checkInEscortDetails.get(0));
							currentRouteDetails.get(0).setVehicleStatus("A");
							currentRouteDetails.get(0).setEscortRequredFlag("N");
							iCabRequestBO.update(currentRouteDetails.get(0));

						} catch (Exception e) {
							currentRouteDetails.get(0).setVehicleStatus("A");
							currentRouteDetails.get(0).setEscortRequredFlag("N");
							iCabRequestBO.update(currentRouteDetails.get(0));
						}

					}
					if ((currentRouteVehicleCapacity.getCapacity()
							- sortedDropEmployeeList.size()) == (currentRouteVehicleCapacity.getCapacity() - 1)) {

						// currentRouteVehicleCapacity.setStatus("A");
						EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
						eFmFmVehicleCheckInPO.setCheckInTime(new Date());
						eFmFmVehicleCheckInPO
								.setCheckInId(currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId());
						List<EFmFmVehicleCheckInPO> vehicleCheck = vehicleCheckIn
								.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
						// vehicleCheck.get(0).setStatus("Y");
						// vehicleCheckIn.update(vehicleCheck.get(0));
						List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
								.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
										currentRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
						checkInEscortDetails.get(0).setStatus("Y");
						vehicleCheckIn.update(checkInEscortDetails.get(0));

						EFmFmDriverMasterPO particularDriverDetails = approvalBO
								.getParticularDriverDetail(currentRouteDetails.get(0).getEfmFmVehicleCheckIn()
										.getEfmFmDriverMaster().getDriverId());
						particularDriverDetails.setStatus("A");
						approvalBO.update(particularDriverDetails);
						List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckIn.getDeviceDetailsFromDeviceId(
								currentRouteDetails.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster()
										.getDeviceId(),
								new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),
										eFmFmClientBranchPO.getCombinedFacility()));
						deviceDetails.get(0).setStatus("Y");
						vehicleCheckIn.update(deviceDetails.get(0));
						// when trip will start then this code will work start
						List<EFmFmTripAlertsPO> allAlerts = iAlertBO.getParticularTripAlerts(
								currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
								currentRouteDetails.get(0).getAssignRouteId());
						if (!(allAlerts.isEmpty()) || allAlerts.size() != 0) {
							for (EFmFmTripAlertsPO tripAlertsPO : allAlerts) {
								iAlertBO.deleteAllAlerts(tripAlertsPO.getTripAlertsId());
							}
						}
						List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
								.getRouteDetailsFromAssignRouteId(
										currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
										currentRouteDetails.get(0).getAssignRouteId());
						if (!(actualRouteTravelled.isEmpty())) {
							for (EFmFmLiveRoutTravelledPO actulaTraveled : actualRouteTravelled) {
								assignRouteBO.deleteParticularActualTravelled(actulaTraveled.getLiveRouteTravelId());
							}
						}
						// End

						// Auto Vehicle allocation start Code
						List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
								currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
								currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId());
						try {
							if (vehicleCheck.get(0).getNumberOfTrips() != 0) {
								int numberOfTrips = vehicleCheck.get(0).getNumberOfTrips() - 1;
								long totalTravelTime = vehicleCheck.get(0).getTotalTravelTime()
										- currentRouteDetails.get(0).getPlannedTime();
								double totalTravelDistance = vehicleCheck.get(0).getTotalTravelDistance()
										- currentRouteDetails.get(0).getPlannedDistance();
								if (oldCheckRoutesCheck.size() == 1) {
									vehicleCheckIn.updateVehicleCheckInDetailsWithStatus(
											vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
											totalTravelDistance, "Y");
									log.info("InSide setStatus Y" + vehicleCheck.get(0).getCheckInId());
								} else {
									vehicleCheckIn.updateVehicleCheckInDetailsWithOutStatus(
											vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
											totalTravelDistance);
									log.info("InSide setStatus N" + vehicleCheck.get(0).getCheckInId());
								}
							}
							double monthlyFixedDistance = currentRouteVehicleCapacity.getMonthlyPendingFixedDistance()
									+ currentRouteDetails.get(0).getPlannedDistance();
							vehicleCheckIn.updateVehicleMonthlyDistance(currentRouteVehicleCapacity.getVehicleId(),
									monthlyFixedDistance, "A");
						} catch (Exception e) {
							log.info("Cab allocation updates");
						}
						// Auto Vehicle allocation end Code
						assignRouteBO.deleteParticularAssignRoute(currentRouteDetails.get(0).getAssignRouteId());
						// vehicleCheckIn.update(currentRouteVehicleCapacity);
					}

				} else {
					// currentRouteVehicleCapacity.setAvailableSeat(currentRouteVehicleCapacity.getAvailableSeat()+2);
					vehicleCheckIn.update(currentRouteVehicleCapacity);
					currentRouteVehicleCapacity = vehicleCheckIn.getParticularVehicleDetail(
							currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
					if (currentRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
						try {
							List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
									.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
											currentRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
							checkInEscortDetails.get(0).setStatus("Y");
							vehicleCheckIn.update(checkInEscortDetails.get(0));
							currentRouteDetails.get(0).setVehicleStatus("A");
							currentRouteDetails.get(0).setEscortRequredFlag("N");
							iCabRequestBO.update(currentRouteDetails.get(0));

						} catch (Exception e) {
							currentRouteDetails.get(0).setVehicleStatus("A");
							currentRouteDetails.get(0).setEscortRequredFlag("N");
							iCabRequestBO.update(currentRouteDetails.get(0));
						}

					}
					if ((currentRouteVehicleCapacity.getCapacity()
							- sortedDropEmployeeList.size()) == (currentRouteVehicleCapacity.getCapacity() - 1)) {
						// currentRouteVehicleCapacity.setStatus("A");
						EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
						eFmFmVehicleCheckInPO.setCheckInTime(new Date());
						eFmFmVehicleCheckInPO
								.setCheckInId(currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId());
						List<EFmFmVehicleCheckInPO> vehicleCheck = vehicleCheckIn
								.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
						// vehicleCheck.get(0).setStatus("Y");
						// vehicleCheckIn.update(vehicleCheck.get(0));
						List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
								.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
										currentRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
						checkInEscortDetails.get(0).setStatus("Y");
						vehicleCheckIn.update(checkInEscortDetails.get(0));

						EFmFmDriverMasterPO particularDriverDetails = approvalBO
								.getParticularDriverDetail(currentRouteDetails.get(0).getEfmFmVehicleCheckIn()
										.getEfmFmDriverMaster().getDriverId());
						particularDriverDetails.setStatus("A");
						approvalBO.update(particularDriverDetails);
						List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckIn.getDeviceDetailsFromDeviceId(
								currentRouteDetails.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster()
										.getDeviceId(),
								new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),
										eFmFmClientBranchPO.getCombinedFacility()));
						deviceDetails.get(0).setStatus("Y");
						vehicleCheckIn.update(deviceDetails.get(0));

						// when trip will start then this code will work start
						List<EFmFmTripAlertsPO> allAlerts = iAlertBO.getParticularTripAlerts(
								currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
								currentRouteDetails.get(0).getAssignRouteId());
						if (!(allAlerts.isEmpty()) || allAlerts.size() != 0) {
							for (EFmFmTripAlertsPO tripAlertsPO : allAlerts) {
								iAlertBO.deleteAllAlerts(tripAlertsPO.getTripAlertsId());
							}
						}
						List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
								.getRouteDetailsFromAssignRouteId(
										currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
										currentRouteDetails.get(0).getAssignRouteId());
						if (!(actualRouteTravelled.isEmpty())) {
							for (EFmFmLiveRoutTravelledPO actulaTraveled : actualRouteTravelled) {
								assignRouteBO.deleteParticularActualTravelled(actulaTraveled.getLiveRouteTravelId());
							}
						}
						// End

						// Auto Vehicle allocation start Code
						try {
							List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
									currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
									currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId());
							if (vehicleCheck.get(0).getNumberOfTrips() != 0) {
								int numberOfTrips = vehicleCheck.get(0).getNumberOfTrips() - 1;
								long totalTravelTime = vehicleCheck.get(0).getTotalTravelTime()
										- currentRouteDetails.get(0).getPlannedTime();
								double totalTravelDistance = vehicleCheck.get(0).getTotalTravelDistance()
										- currentRouteDetails.get(0).getPlannedDistance();
								if (oldCheckRoutesCheck.size() == 1) {
									vehicleCheckIn.updateVehicleCheckInDetailsWithStatus(
											vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
											totalTravelDistance, "Y");
									log.info("InSide setStatus Y" + vehicleCheck.get(0).getCheckInId());
								} else {
									vehicleCheckIn.updateVehicleCheckInDetailsWithOutStatus(
											vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
											totalTravelDistance);
									log.info("InSide setStatus N" + vehicleCheck.get(0).getCheckInId());
								}
							}
							double monthlyFixedDistance = currentRouteVehicleCapacity.getMonthlyPendingFixedDistance()
									+ currentRouteDetails.get(0).getPlannedDistance();
							vehicleCheckIn.updateVehicleMonthlyDistance(currentRouteVehicleCapacity.getVehicleId(),
									monthlyFixedDistance, "A");
						} catch (Exception e) {
							log.info("Cab allocation updates");
						}
						// Auto Vehicle allocation end Code

						assignRouteBO.deleteParticularAssignRoute(currentRouteDetails.get(0).getAssignRouteId());
						// vehicleCheckIn.update(currentRouteVehicleCapacity);
					}
				}
			} else if (currentRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")
					&& currentRouteDetails.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
				List<EFmFmEmployeeTripDetailPO> tripEmployeesForCurrentRoute = iCabRequestBO
						.getParticularTripAllEmployees(currentRouteDetails.get(0).getAssignRouteId());
				if (tripEmployeesForCurrentRoute.get(0).getEmpTripId() == requestTripDetail.get(0).getEmpTripId()) {
					// currentRouteVehicleCapacity.setAvailableSeat(currentRouteVehicleCapacity.getAvailableSeat()+2);
					vehicleCheckIn.update(currentRouteVehicleCapacity);
					currentRouteVehicleCapacity = vehicleCheckIn.getParticularVehicleDetail(
							currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
					if (currentRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
						try {
							List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
									.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
											currentRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
							checkInEscortDetails.get(0).setStatus("Y");
							vehicleCheckIn.update(checkInEscortDetails.get(0));
							currentRouteDetails.get(0).setVehicleStatus("A");
							currentRouteDetails.get(0).setEscortRequredFlag("N");
							iCabRequestBO.update(currentRouteDetails.get(0));

						} catch (Exception e) {
							currentRouteDetails.get(0).setVehicleStatus("A");
							currentRouteDetails.get(0).setEscortRequredFlag("N");
							iCabRequestBO.update(currentRouteDetails.get(0));
						}

					}
					if ((currentRouteVehicleCapacity.getCapacity()
							- sortedDropEmployeeList.size()) == (currentRouteVehicleCapacity.getCapacity() - 1)) {
						// currentRouteVehicleCapacity.setStatus("A");
						EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
						eFmFmVehicleCheckInPO.setCheckInTime(new Date());
						eFmFmVehicleCheckInPO
								.setCheckInId(currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId());
						List<EFmFmVehicleCheckInPO> vehicleCheck = vehicleCheckIn
								.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
						// vehicleCheck.get(0).setStatus("Y");
						// vehicleCheckIn.update(vehicleCheck.get(0));
						List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
								.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
										currentRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
						checkInEscortDetails.get(0).setStatus("Y");
						vehicleCheckIn.update(checkInEscortDetails.get(0));
						EFmFmDriverMasterPO particularDriverDetails = approvalBO
								.getParticularDriverDetail(currentRouteDetails.get(0).getEfmFmVehicleCheckIn()
										.getEfmFmDriverMaster().getDriverId());
						particularDriverDetails.setStatus("A");
						approvalBO.update(particularDriverDetails);
						List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckIn.getDeviceDetailsFromDeviceId(
								currentRouteDetails.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster()
										.getDeviceId(),
								new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),
										eFmFmClientBranchPO.getCombinedFacility()));
						deviceDetails.get(0).setStatus("Y");
						vehicleCheckIn.update(deviceDetails.get(0));

						// when trip will start then this code will work start
						List<EFmFmTripAlertsPO> allAlerts = iAlertBO.getParticularTripAlerts(
								currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
								currentRouteDetails.get(0).getAssignRouteId());
						if (!(allAlerts.isEmpty())) {
							for (EFmFmTripAlertsPO tripAlertsPO : allAlerts) {
								iAlertBO.deleteAllAlerts(tripAlertsPO.getTripAlertsId());
							}
						}
						List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
								.getRouteDetailsFromAssignRouteId(
										currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
										currentRouteDetails.get(0).getAssignRouteId());
						if (!(actualRouteTravelled.isEmpty())) {
							for (EFmFmLiveRoutTravelledPO actulaTraveled : actualRouteTravelled) {
								assignRouteBO.deleteParticularActualTravelled(actulaTraveled.getLiveRouteTravelId());
							}
						}
						// End
						// Auto Vehicle allocation start Code
						try {
							List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
									currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
									currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId());
							if (vehicleCheck.get(0).getNumberOfTrips() != 0) {
								int numberOfTrips = vehicleCheck.get(0).getNumberOfTrips() - 1;
								long totalTravelTime = vehicleCheck.get(0).getTotalTravelTime()
										- currentRouteDetails.get(0).getPlannedTime();
								double totalTravelDistance = vehicleCheck.get(0).getTotalTravelDistance()
										- currentRouteDetails.get(0).getPlannedDistance();
								if (oldCheckRoutesCheck.size() == 1) {
									vehicleCheckIn.updateVehicleCheckInDetailsWithStatus(
											vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
											totalTravelDistance, "Y");
									log.info("InSide setStatus Y" + vehicleCheck.get(0).getCheckInId());
								} else {
									vehicleCheckIn.updateVehicleCheckInDetailsWithOutStatus(
											vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
											totalTravelDistance);
									log.info("InSide setStatus N" + vehicleCheck.get(0).getCheckInId());
								}
							}
							double monthlyFixedDistance = currentRouteVehicleCapacity.getMonthlyPendingFixedDistance()
									+ currentRouteDetails.get(0).getPlannedDistance();
							vehicleCheckIn.updateVehicleMonthlyDistance(currentRouteVehicleCapacity.getVehicleId(),
									monthlyFixedDistance, "A");
						} catch (Exception e) {
							log.info("Cab allocation updates");
						}
						// Auto Vehicle allocation end Code

						assignRouteBO.deleteParticularAssignRoute(currentRouteDetails.get(0).getAssignRouteId());
						// vehicleCheckIn.update(currentRouteVehicleCapacity);
					}

				} else {

					// currentRouteVehicleCapacity.setAvailableSeat(currentRouteVehicleCapacity.getAvailableSeat()+2);
					vehicleCheckIn.update(currentRouteVehicleCapacity);
					currentRouteVehicleCapacity = vehicleCheckIn.getParticularVehicleDetail(
							currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
					if (currentRouteDetails.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
						try {
							List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
									.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
											currentRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
							checkInEscortDetails.get(0).setStatus("Y");
							vehicleCheckIn.update(checkInEscortDetails.get(0));
							currentRouteDetails.get(0).setVehicleStatus("A");
							currentRouteDetails.get(0).setEscortRequredFlag("N");
							iCabRequestBO.update(currentRouteDetails.get(0));

						} catch (Exception e) {
							currentRouteDetails.get(0).setVehicleStatus("A");
							currentRouteDetails.get(0).setEscortRequredFlag("N");
							iCabRequestBO.update(currentRouteDetails.get(0));
						}

					}
					if ((currentRouteVehicleCapacity.getCapacity()
							- sortedDropEmployeeList.size()) == (currentRouteVehicleCapacity.getCapacity() - 1)) {

						// currentRouteVehicleCapacity.setStatus("A");
						EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
						eFmFmVehicleCheckInPO.setCheckInTime(new Date());
						eFmFmVehicleCheckInPO
								.setCheckInId(currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId());
						List<EFmFmVehicleCheckInPO> vehicleCheck = vehicleCheckIn
								.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
						// vehicleCheck.get(0).setStatus("Y");
						// vehicleCheckIn.update(vehicleCheck.get(0));
						List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckIn
								.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
										currentRouteDetails.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
						checkInEscortDetails.get(0).setStatus("Y");
						vehicleCheckIn.update(checkInEscortDetails.get(0));

						EFmFmDriverMasterPO particularDriverDetails = approvalBO
								.getParticularDriverDetail(currentRouteDetails.get(0).getEfmFmVehicleCheckIn()
										.getEfmFmDriverMaster().getDriverId());
						particularDriverDetails.setStatus("A");
						approvalBO.update(particularDriverDetails);
						List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckIn.getDeviceDetailsFromDeviceId(
								currentRouteDetails.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster()
										.getDeviceId(),
								new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),
										eFmFmClientBranchPO.getCombinedFacility()));
						deviceDetails.get(0).setStatus("Y");
						vehicleCheckIn.update(deviceDetails.get(0));

						// when trip will start then this code will work start
						List<EFmFmTripAlertsPO> allAlerts = iAlertBO.getParticularTripAlerts(
								currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
								currentRouteDetails.get(0).getAssignRouteId());
						if (!(allAlerts.isEmpty())) {
							for (EFmFmTripAlertsPO tripAlertsPO : allAlerts) {
								iAlertBO.deleteAllAlerts(tripAlertsPO.getTripAlertsId());
							}
						}
						List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
								.getRouteDetailsFromAssignRouteId(
										currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
										currentRouteDetails.get(0).getAssignRouteId());
						if (!(actualRouteTravelled.isEmpty())) {
							for (EFmFmLiveRoutTravelledPO actulaTraveled : actualRouteTravelled) {
								assignRouteBO.deleteParticularActualTravelled(actulaTraveled.getLiveRouteTravelId());
							}
						}
						// End

						// Auto Vehicle allocation start Code
						try {
							List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
									currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
									currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId());
							if (vehicleCheck.get(0).getNumberOfTrips() != 0) {
								int numberOfTrips = vehicleCheck.get(0).getNumberOfTrips() - 1;
								long totalTravelTime = vehicleCheck.get(0).getTotalTravelTime()
										- currentRouteDetails.get(0).getPlannedTime();
								double totalTravelDistance = vehicleCheck.get(0).getTotalTravelDistance()
										- currentRouteDetails.get(0).getPlannedDistance();
								if (oldCheckRoutesCheck.size() == 1) {
									vehicleCheckIn.updateVehicleCheckInDetailsWithStatus(
											vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
											totalTravelDistance, "Y");
									log.info("InSide setStatus Y" + vehicleCheck.get(0).getCheckInId());
								} else {
									vehicleCheckIn.updateVehicleCheckInDetailsWithOutStatus(
											vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
											totalTravelDistance);
									log.info("InSide setStatus N" + vehicleCheck.get(0).getCheckInId());
								}
							}
							double monthlyFixedDistance = currentRouteVehicleCapacity.getMonthlyPendingFixedDistance()
									+ currentRouteDetails.get(0).getPlannedDistance();
							vehicleCheckIn.updateVehicleMonthlyDistance(currentRouteVehicleCapacity.getVehicleId(),
									monthlyFixedDistance, "A");
						} catch (Exception e) {
							log.info("Cab allocation updates");
						}
						// Auto Vehicle allocation end Code

						assignRouteBO.deleteParticularAssignRoute(currentRouteDetails.get(0).getAssignRouteId());
						// vehicleCheckIn.update(currentRouteVehicleCapacity);
					}

				}

			} else {
				// currentRouteVehicleCapacity.setAvailableSeat(currentRouteVehicleCapacity.getAvailableSeat()+1);
				if ((currentRouteVehicleCapacity.getCapacity()
						- sortedDropEmployeeList.size()) == (currentRouteVehicleCapacity.getCapacity() - 1)) {

					// currentRouteVehicleCapacity.setStatus("A");
					EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
					eFmFmVehicleCheckInPO.setCheckInTime(new Date());
					eFmFmVehicleCheckInPO
							.setCheckInId(currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId());
					List<EFmFmVehicleCheckInPO> vehicleCheck = vehicleCheckIn
							.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
					// vehicleCheck.get(0).setStatus("Y");
					// vehicleCheckIn.update(vehicleCheck.get(0));
					EFmFmDriverMasterPO particularDriverDetails = approvalBO.getParticularDriverDetail(
							currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
					particularDriverDetails.setStatus("A");
					approvalBO.update(particularDriverDetails);
					List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckIn.getDeviceDetailsFromDeviceId(
							currentRouteDetails.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),
							new MultifacilityService().combinedBranchIdDetails(eFmFmClientBranchPO.getUserId(),
									eFmFmClientBranchPO.getCombinedFacility()));
					deviceDetails.get(0).setStatus("Y");
					vehicleCheckIn.update(deviceDetails.get(0));

					// when trip will start then this code will work start
					List<EFmFmTripAlertsPO> allAlerts = iAlertBO.getParticularTripAlerts(
							currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
							currentRouteDetails.get(0).getAssignRouteId());
					if (!(allAlerts.isEmpty()) || allAlerts.size() != 0) {
						for (EFmFmTripAlertsPO tripAlertsPO : allAlerts) {
							iAlertBO.deleteAllAlerts(tripAlertsPO.getTripAlertsId());
						}
					}
					List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
							.getRouteDetailsFromAssignRouteId(
									currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId(),
									currentRouteDetails.get(0).getAssignRouteId());
					if (!(actualRouteTravelled.isEmpty())) {
						for (EFmFmLiveRoutTravelledPO actulaTraveled : actualRouteTravelled) {
							assignRouteBO.deleteParticularActualTravelled(actulaTraveled.getLiveRouteTravelId());
						}
					}
					// End

					// Auto Vehicle allocation start Code
					try {
						List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
								currentRouteDetails.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
								currentRouteDetails.get(0).geteFmFmClientBranchPO().getBranchId());
						if (vehicleCheck.get(0).getNumberOfTrips() != 0) {
							int numberOfTrips = vehicleCheck.get(0).getNumberOfTrips() - 1;
							long totalTravelTime = vehicleCheck.get(0).getTotalTravelTime()
									- currentRouteDetails.get(0).getPlannedTime();
							double totalTravelDistance = vehicleCheck.get(0).getTotalTravelDistance()
									- currentRouteDetails.get(0).getPlannedDistance();
							if (oldCheckRoutesCheck.size() == 1) {
								vehicleCheckIn.updateVehicleCheckInDetailsWithStatus(vehicleCheck.get(0).getCheckInId(),
										numberOfTrips, totalTravelTime, totalTravelDistance, "Y");
								log.info("InSide setStatus Y" + vehicleCheck.get(0).getCheckInId());
							} else {
								vehicleCheckIn.updateVehicleCheckInDetailsWithOutStatus(
										vehicleCheck.get(0).getCheckInId(), numberOfTrips, totalTravelTime,
										totalTravelDistance);
								log.info("InSide setStatus N" + vehicleCheck.get(0).getCheckInId());
							}
						}
						double monthlyFixedDistance = currentRouteVehicleCapacity.getMonthlyPendingFixedDistance()
								+ currentRouteDetails.get(0).getPlannedDistance();
						vehicleCheckIn.updateVehicleMonthlyDistance(currentRouteVehicleCapacity.getVehicleId(),
								monthlyFixedDistance, "A");
					} catch (Exception e) {
						log.info("Cab allocation updates");
					}
					// Auto Vehicle allocation end Code

					assignRouteBO.deleteParticularAssignRoute(currentRouteDetails.get(0).getAssignRouteId());
					// vehicleCheckIn.update(currentRouteVehicleCapacity);
				} else {
					vehicleCheckIn.update(currentRouteVehicleCapacity);
				}

			}
			final PushNotificationService pushNotification = new PushNotificationService();
			log.info("currentRouteDetails.get(0)DOWN" + currentRouteDetails.get(0).getAssignRouteId() + "Id"
					+ sortedDropEmployeeList.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getUserId());
			log.info("selectedRouteDetails.get(0)down" + selectedRouteDetails.get(0).getAssignRouteId() + "Id"
					+ sortedDropEmployeeList.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getUserId());
			log.info("requestTripDetail"
					+ requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
			Thread thread1 = new Thread(new Runnable() {
				@Override
				public void run() {

					try {
						String text = "";

						MessagingService messagingService = new MessagingService();
						if (selectedRouteDetails.get(0).getTripType().equalsIgnoreCase("PICKUP")
								&& currentRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")
								&& selectedRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")) {
							text = "Dear employee your cab is changed,\nthe new cab is\n"
									+ selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
											.getVehicleNumber()
									+ "\nYour schedule pickup time is\n"
									+ requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime();
							messagingService.sendTripAsMessage(
									new String(Base64.getDecoder()
											.decode(requestTripDetail.get(0).geteFmFmEmployeeTravelRequest()
													.getEfmFmUserMaster().getMobileNumber())),
									text, requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
							try {
								if (requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getDeviceType().contains("Android")) {
									pushNotification.notification(requestTripDetail.get(0)
											.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
											text);
								} else {
									pushNotification.notification(requestTripDetail.get(0)
											.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
											text);
								}
							} catch (Exception e) {
								log.info("Cab Changed Msg" + e);
							}
						}
						if (selectedRouteDetails.get(0).getTripType().equalsIgnoreCase("PICKUP")
								&& currentRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("N")
								&& selectedRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")) {
							text = "Vehicle number "
									+ selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
											.getVehicleNumber()
									+ "."
									+ selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
											.getVehicleModel()
									+ " has been assigned for your pickup for "
									+ shiftTimeFormater.format(selectedRouteDetails.get(0).getShiftTime())
									+ " shift.\nFor feedback write to us @"
									+ selectedRouteDetails.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();

							messagingService.sendTripAsMessage(
									new String(Base64.getDecoder()
											.decode(requestTripDetail.get(0).geteFmFmEmployeeTravelRequest()
													.getEfmFmUserMaster().getMobileNumber())),
									text, requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
							try {
								if (requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getDeviceType().contains("Android")) {
									pushNotification.notification(requestTripDetail.get(0)
											.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
											text);
								} else {
									pushNotification.iosPushNotification(requestTripDetail.get(0)
											.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
											text);
								}
							} catch (Exception e) {
								log.info("Cab Changed Msg" + e);
							}
						}
						if (selectedRouteDetails.get(0).getTripType().equalsIgnoreCase("DROP")
								&& currentRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")
								&& selectedRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")) {
							text = "Dear employee your cab is changed,\nthe new cab is\n" + selectedRouteDetails.get(0)
									.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber();
							messagingService.sendTripAsMessage(
									new String(Base64.getDecoder()
											.decode(requestTripDetail.get(0).geteFmFmEmployeeTravelRequest()
													.getEfmFmUserMaster().getMobileNumber())),
									text, requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
							try {
								if (requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getDeviceType().contains("Android")) {
									pushNotification.notification(requestTripDetail.get(0)
											.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
											text);
								} else {
									pushNotification.iosPushNotification(requestTripDetail.get(0)
											.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
											text);
								}
							} catch (Exception e) {
								log.info("Cab Changed Msg" + e);
							}
						}
						if (selectedRouteDetails.get(0).getTripType().equalsIgnoreCase("DROP")
								&& currentRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("N")
								&& selectedRouteDetails.get(0).getAllocationMsg().equalsIgnoreCase("Y")) {
							// text="Dear employee cab is allocated for your
							// pickup\n"+selectedRouteDetails.get(0).getShiftTime()+"
							// shift\nAllocated Cab number
							// is\n"+selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber()+"
							// \nyour pickup time is
							// "+requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime()+"\nVehicle
							// Type
							// "+selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel();
							text = "Vehicle number "
									+ selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
											.getVehicleNumber()
									+ "."
									+ selectedRouteDetails.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
											.getVehicleModel()
									+ " has been assigned for your drop for "
									+ selectedRouteDetails.get(0).getShiftTime() + " shift.\nFor feedback write to us @"
									+ selectedRouteDetails.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();

							messagingService.sendTripAsMessage(
									new String(Base64.getDecoder()
											.decode(requestTripDetail.get(0).geteFmFmEmployeeTravelRequest()
													.getEfmFmUserMaster().getMobileNumber())),
									text, requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
							try {
								if (requestTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getDeviceType().contains("Android")) {
									pushNotification.notification(requestTripDetail.get(0)
											.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
											text);
								} else {
									pushNotification.iosPushNotification(requestTripDetail.get(0)
											.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
											text);
								}
							} catch (Exception e) {
								log.info("Cab allocated Msg" + e);
							}
						}
					} catch (Exception e) {
					}
				}

			});
			thread1.start();

			List<EFmFmEmployeeTripDetailPO> tripEmployeesForCurrentRoute = iCabRequestBO
					.getParticularTripAllEmployees(currentRouteDetails.get(0).getAssignRouteId());
			List<EFmFmEmployeeTripDetailPO> tripEmployeesForSelectedRoute = iCabRequestBO
					.getParticularTripAllEmployees(selectedRouteDetails.get(0).getAssignRouteId());
			responce.put("currentRouteAvailableCapacity",
					(currentRouteVehicleCapacity.getCapacity() - 1) - tripEmployeesForCurrentRoute.size());
			responce.put("selectedRouteAvailableCapacity",
					(selectedRouteVehicleCapacity.getCapacity() - 1) - tripEmployeesForSelectedRoute.size());
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	// Get all Checked In vehicles,drivers,devices and Escorts On click on edit
	// bucket
	@POST
	@Path("/checkedinentity")
	public Response checkedInVehicleDriverDeviceAndEscort(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorMasterPO.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
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
		// IApprovalBO approvalBO = (IApprovalBO)
		// ContextLoader.getContext().getBean("IApprovalBO");

		Map<String, Object> vehicleCheckInList = new HashMap<String, Object>();
		EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId());
		EFmFmAssignRoutePO eFmFmAssignRoutePO = new EFmFmAssignRoutePO();
		eFmFmAssignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		eFmFmAssignRoutePO.setAssignRouteId(eFmFmVendorMasterPO.getAssignRouteId());
		List<EFmFmVehicleCheckInPO> listOfCheckedInVehicle = iVehicleCheckInBO
				.getAllCheckedInVehicleDetailsForEditBucket(eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId());
		List<EFmFmAssignRoutePO> assignRoutes = assignRouteBO.closeParticularTrips(eFmFmAssignRoutePO);
		assignRoutes.get(0).setVehicleStatus("A");
		assignRoutes.get(0).setBucketStatus("N");
		assignRouteBO.update(assignRoutes.get(0));
		/*
		 * EFmFmDriverMasterPO
		 * particularDriverDetails=approvalBO.getParticularDriverDetail(
		 * assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().
		 * getDriverId()); particularDriverDetails.setStatus("A");
		 * approvalBO.update(particularDriverDetails); List<EFmFmDeviceMasterPO>
		 * deviceDetails=
		 * iVehicleCheckInBO.getDeviceDetailsFromDeviceId(assignRoutes.get(0).
		 * getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),
		 * assignRoutes.get(0).geteFmFmClientBranchPO().getBranchId());
		 * deviceDetails.get(0).setStatus("Y");
		 * iVehicleCheckInBO.update(deviceDetails.get(0));
		 */
		List<Map<String, Object>> checkInList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> escortCheckInList = new ArrayList<Map<String, Object>>();
		if (!(listOfCheckedInVehicle.isEmpty())) {
			for (EFmFmVehicleCheckInPO vehicleDetails : listOfCheckedInVehicle) {
				Map<String, Object> checkCombinationDetails = new HashMap<String, Object>();
				checkCombinationDetails.put("vehicleId", vehicleDetails.getEfmFmVehicleMaster().getVehicleId());
				checkCombinationDetails.put("checkInId", vehicleDetails.getCheckInId());
				checkCombinationDetails.put("vehicleNumber", vehicleDetails.getEfmFmVehicleMaster().getVehicleNumber());
				checkCombinationDetails.put("vendorId",
						vehicleDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorId());
				checkCombinationDetails.put("driverId", vehicleDetails.getEfmFmDriverMaster().getDriverId());
				checkCombinationDetails.put("driverName", vehicleDetails.getEfmFmDriverMaster().getFirstName());
				checkCombinationDetails.put("mobileNumber", vehicleDetails.getEfmFmDriverMaster().getMobileNumber());
				checkCombinationDetails.put("deviceId", vehicleDetails.geteFmFmDeviceMaster().getDeviceId());
				checkCombinationDetails.put("deviceNumber", vehicleDetails.geteFmFmDeviceMaster().getMobileNumber());
				checkInList.add(checkCombinationDetails);
			}
		}
		Map<String, Object> checkCombinationDetails = new HashMap<String, Object>();
		checkCombinationDetails.put("vehicleId",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
		checkCombinationDetails.put("checkInId", assignRoutes.get(0).getEfmFmVehicleCheckIn().getCheckInId());
		checkCombinationDetails.put("vehicleNumber",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
		checkCombinationDetails.put("vendorId", assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
				.getEfmFmVendorMaster().getVendorId());
		checkCombinationDetails.put("driverId",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
		checkCombinationDetails.put("driverName",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
		checkCombinationDetails.put("mobileNumber",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
		checkCombinationDetails.put("deviceId",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId());
		checkCombinationDetails.put("deviceNumber",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getMobileNumber());
		checkInList.add(checkCombinationDetails);
		List<EFmFmEscortCheckInPO> escortList = iVehicleCheckInBO
				.getAllCheckedInEscort(eFmFmVendorMasterPO.getCombinedFacility());
		if (!(escortList.isEmpty())) {
			for (EFmFmEscortCheckInPO escorts : escortList) {
				Map<String, Object> escortsDetails = new HashMap<String, Object>();
				escortsDetails.put("escortCheckInId", escorts.getEscortCheckInId());
				escortsDetails.put("escortName", escorts.geteFmFmEscortMaster().getFirstName());
				escortCheckInList.add(escortsDetails);
			}
		}
		if (assignRoutes.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
			Map<String, Object> escortsDetails = new HashMap<String, Object>();
			try {
				int escortId = assignRoutes.get(0).geteFmFmEscortCheckIn().getEscortCheckInId();
				log.info("escortId" + escortId);
				escortsDetails.put("escortName",
						assignRoutes.get(0).geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
				escortsDetails.put("escortCheckInId", assignRoutes.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
				escortCheckInList.add(escortsDetails);
			} catch (Exception e) {
			}
		}
		vehicleCheckInList.put("escortDetails", escortCheckInList);
		vehicleCheckInList.put("checkInList", checkInList);

		vehicleCheckInList.put("vehicleStatus", "A");
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(vehicleCheckInList, MediaType.APPLICATION_JSON).build();
	}

	// Get all Checked In vehicles,drivers,devices and Escorts On click on edit
	// bucket
	@POST
	@Path("/fieldAppCheckedinentity")
	public Response fieldAppCheckedinentity(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		// log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){
		//
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		Map<String, Object> vehicleCheckInList = new HashMap<String, Object>();
		EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId());
		EFmFmAssignRoutePO eFmFmAssignRoutePO = new EFmFmAssignRoutePO();
		eFmFmAssignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		eFmFmAssignRoutePO.setAssignRouteId(eFmFmVendorMasterPO.getAssignRouteId());
		List<EFmFmVehicleCheckInPO> listOfCheckedInVehicle = iVehicleCheckInBO
				.getAllCheckedInVehicleDetailsForEditBucketByvendorId(
						eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId(), eFmFmVendorMasterPO.getVendorId());
		List<EFmFmAssignRoutePO> assignRoutes = assignRouteBO.closeParticularTrips(eFmFmAssignRoutePO);
		assignRoutes.get(0).setVehicleStatus("A");
		assignRoutes.get(0).setBucketStatus("N");
		assignRouteBO.update(assignRoutes.get(0));
		/*
		 * EFmFmDriverMasterPO
		 * particularDriverDetails=approvalBO.getParticularDriverDetail(
		 * assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().
		 * getDriverId()); particularDriverDetails.setStatus("A");
		 * approvalBO.update(particularDriverDetails); List<EFmFmDeviceMasterPO>
		 * deviceDetails=
		 * iVehicleCheckInBO.getDeviceDetailsFromDeviceId(assignRoutes.get(0).
		 * getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),
		 * assignRoutes.get(0).geteFmFmClientBranchPO().getBranchId());
		 * deviceDetails.get(0).setStatus("Y");
		 * iVehicleCheckInBO.update(deviceDetails.get(0));
		 */
		List<Map<String, Object>> checkInList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> escortCheckInList = new ArrayList<Map<String, Object>>();
		if (!(listOfCheckedInVehicle.isEmpty())) {
			for (EFmFmVehicleCheckInPO vehicleDetails : listOfCheckedInVehicle) {
				Map<String, Object> checkCombinationDetails = new HashMap<String, Object>();
				checkCombinationDetails.put("vehicleId", vehicleDetails.getEfmFmVehicleMaster().getVehicleId());
				checkCombinationDetails.put("checkInId", vehicleDetails.getCheckInId());
				checkCombinationDetails.put("vehicleNumber", vehicleDetails.getEfmFmVehicleMaster().getVehicleNumber());
				checkCombinationDetails.put("vendorId",
						vehicleDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorId());
				checkCombinationDetails.put("driverId", vehicleDetails.getEfmFmDriverMaster().getDriverId());
				checkCombinationDetails.put("driverName", vehicleDetails.getEfmFmDriverMaster().getFirstName());
				checkCombinationDetails.put("mobileNumber", vehicleDetails.getEfmFmDriverMaster().getMobileNumber());
				checkCombinationDetails.put("deviceId", vehicleDetails.geteFmFmDeviceMaster().getDeviceId());
				checkCombinationDetails.put("deviceNumber", vehicleDetails.geteFmFmDeviceMaster().getMobileNumber());
				checkInList.add(checkCombinationDetails);
			}
		}
		Map<String, Object> checkCombinationDetails = new HashMap<String, Object>();
		checkCombinationDetails.put("vehicleId",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
		checkCombinationDetails.put("checkInId", assignRoutes.get(0).getEfmFmVehicleCheckIn().getCheckInId());
		checkCombinationDetails.put("vehicleNumber",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
		checkCombinationDetails.put("vendorId", assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
				.getEfmFmVendorMaster().getVendorId());
		checkCombinationDetails.put("driverId",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
		checkCombinationDetails.put("driverName",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
		checkCombinationDetails.put("mobileNumber",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
		checkCombinationDetails.put("deviceId",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId());
		checkCombinationDetails.put("deviceNumber",
				assignRoutes.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getMobileNumber());
		checkInList.add(checkCombinationDetails);
		List<EFmFmEscortCheckInPO> escortList = iVehicleCheckInBO
				.getAllCheckedInEscort(eFmFmVendorMasterPO.getCombinedFacility());
		if (!(escortList.isEmpty())) {
			for (EFmFmEscortCheckInPO escorts : escortList) {
				Map<String, Object> escortsDetails = new HashMap<String, Object>();
				escortsDetails.put("escortCheckInId", escorts.getEscortCheckInId());
				escortsDetails.put("escortName", escorts.geteFmFmEscortMaster().getFirstName());
				escortCheckInList.add(escortsDetails);
			}
		}
		if (assignRoutes.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
			Map<String, Object> escortsDetails = new HashMap<String, Object>();
			try {
				int escortId = assignRoutes.get(0).geteFmFmEscortCheckIn().getEscortCheckInId();
				log.info("escortId" + escortId);
				escortsDetails.put("escortName",
						assignRoutes.get(0).geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
				escortsDetails.put("escortCheckInId", assignRoutes.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
				escortCheckInList.add(escortsDetails);
			} catch (Exception e) {
			}
		}
		vehicleCheckInList.put("escortDetails", escortCheckInList);
		vehicleCheckInList.put("checkInList", checkInList);

		vehicleCheckInList.put("vehicleStatus", "A");
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(vehicleCheckInList, MediaType.APPLICATION_JSON).build();
	}

	// get all available vehicles,driver and Devices for Changing the entites in
	@POST
	@Path("/checkedindriver")
	public Response getAllCheckedInDrivers(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorMasterPO.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
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
		Map<String, Object> vehicleCheckInList = new HashMap<String, Object>();
		EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
		assignRoutePO.setAssignRouteId(eFmFmVendorMasterPO.getAssignRouteId());
		EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId());
		assignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		List<EFmFmVehicleCheckInPO> allDriverByVendor = iVehicleCheckInBO.getAllCheckedInDriversByVendor(
				eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId(), eFmFmVendorMasterPO.getVendorId());
		List<Map<String, Object>> checkInList = new ArrayList<Map<String, Object>>();
		if (!(allDriverByVendor.isEmpty()) || allDriverByVendor.size() != 0) {
			for (EFmFmVehicleCheckInPO driverDetails : allDriverByVendor) {
				Map<String, Object> driverList = new HashMap<String, Object>();
				driverList.put("driverId", driverDetails.getEfmFmDriverMaster().getDriverId());
				driverList.put("driverName", driverDetails.getEfmFmDriverMaster().getFirstName());
				driverList.put("mobileNumber", driverDetails.getEfmFmDriverMaster().getMobileNumber());
				driverList.put("vendorId", driverDetails.getEfmFmDriverMaster().getEfmFmVendorMaster().getVendorId());
				checkInList.add(driverList);
			}
		}
		/*
		 * Map<String, Object> driverList= new HashMap<String, Object>();
		 * driverList.put("driverId",
		 * assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().
		 * getDriverId()); driverList.put("driverName",
		 * assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().
		 * getFirstName()); driverList.put("mobileNumber",
		 * assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().
		 * getMobileNumber()); driverList.put("vendorName",
		 * assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().
		 * getEfmFmVendorMaster().getVendorName()); driverList.put("vendorId",
		 * assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().
		 * getEfmFmVendorMaster().getVendorId()); checkInList.add(driverList);
		 */

		vehicleCheckInList.put("driverDetails", checkInList);
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(vehicleCheckInList, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/checkedinvehicle")
	public Response getAllCheckedInVehicles(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmVendorMasterPO.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
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
		Map<String, Object> vehicleCheckInList = new HashMap<String, Object>();
		EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
		assignRoutePO.setAssignRouteId(eFmFmVendorMasterPO.getAssignRouteId());
		EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId());
		assignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		List<EFmFmVehicleCheckInPO> allVehicleByVendor = iVehicleCheckInBO.getAllCheckedInVehiclesByVendor(
				eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId(), eFmFmVendorMasterPO.getVendorId());
		List<Map<String, Object>> checkInList = new ArrayList<Map<String, Object>>();
		if (!(allVehicleByVendor.isEmpty()) || allVehicleByVendor.size() != 0) {
			for (EFmFmVehicleCheckInPO vehicleDetails : allVehicleByVendor) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("vehicleId", vehicleDetails.getEfmFmVehicleMaster().getVehicleId());
				vehicleList.put("vehicleNumber", vehicleDetails.getEfmFmVehicleMaster().getVehicleNumber());
				vehicleList.put("vendorName",
						vehicleDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				vehicleList.put("vendorId",
						vehicleDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorId());
				checkInList.add(vehicleList);
			}
		}
		/*
		 * Map<String, Object> vehicleList= new HashMap<String, Object>();
		 * vehicleList.put("vehicleId",
		 * assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().
		 * getVehicleId()); vehicleList.put("vehicleNumber",
		 * assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().
		 * getVehicleNumber()); vehicleList.put("vendorName",
		 * assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().
		 * getEfmFmVendorMaster().getVendorName()); vehicleList.put("vendorId",
		 * assignRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().
		 * getEfmFmVendorMaster().getVendorId()); checkInList.add(vehicleList);
		 */

		vehicleCheckInList.put("vehicleDetails", checkInList);
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(vehicleCheckInList, MediaType.APPLICATION_JSON).build();
	}

	// Get all Checked In vehicles,drivers,devices and zones.
	@POST
	@Path("/createbucket")
	public Response createDummyBucket(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));

		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){
		//
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmVendorMasterPO.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		List<EFmFmClientRouteMappingPO> allRoutes = iRouteDetailBO
				.getAllRoutesOfParticularClient(eFmFmVendorMasterPO.getCombinedFacility());
		DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
		List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO.listOfShiftTime(new MultifacilityService()
				.combinedBranchIdDetails(eFmFmVendorMasterPO.getUserId(), eFmFmVendorMasterPO.getCombinedFacility()));
		Map<String, Object> vehicleCheckInList = new HashMap<String, Object>();
		EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId());
		EFmFmAssignRoutePO eFmFmAssignRoutePO = new EFmFmAssignRoutePO();
		eFmFmAssignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		eFmFmAssignRoutePO.setAssignRouteId(eFmFmVendorMasterPO.getAssignRouteId());
		List<Map<String, Object>> shitTimings = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> allRoutesData = new ArrayList<Map<String, Object>>();
		if (!(shiftTimeDetails.isEmpty())) {
			for (EFmFmTripTimingMasterPO shiftiming : shiftTimeDetails) {
				Map<String, Object> shifList = new HashMap<String, Object>();
				shifList.put("shiftTime", shiftTimeFormater.format(shiftiming.getShiftTime()));
				shitTimings.add(shifList);
			}

		}
		if (!(allRoutes.isEmpty())) {
			for (EFmFmClientRouteMappingPO routeDetails : allRoutes) {
				Map<String, Object> routeName = new HashMap<String, Object>();
				routeName.put("routeName", routeDetails.geteFmFmZoneMaster().getZoneName());
				routeName.put("routeId", routeDetails.geteFmFmZoneMaster().getZoneId());
				allRoutesData.add(routeName);
			}
		}
		/*
		 * if((!(listOfCheckedInVehicle.isEmpty())) ||
		 * listOfCheckedInVehicle.size() !=0){ for(EFmFmVehicleCheckInPO
		 * vehicleDetails:listOfCheckedInVehicle){ Map<String, Object>
		 * vehicleList= new HashMap<String, Object>();
		 * vehicleList.put("vehicleId",
		 * vehicleDetails.getEfmFmVehicleMaster().getVehicleId());
		 * vehicleList.put("vehicleNumber",
		 * vehicleDetails.getEfmFmVehicleMaster().getVehicleNumber());
		 * vehicleList.put("vendorName",
		 * vehicleDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().
		 * getVendorName()); vehicleList.put("vendorId",
		 * vehicleDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().
		 * getVendorId()); checkInList.add(vehicleList); } }
		 * if((!(listOfCheckedInVehicle.isEmpty())) ||
		 * listOfCheckedInVehicle.size() !=0){ for(EFmFmVehicleCheckInPO
		 * driverDetails:listOfCheckedInVehicle){ Map<String, Object>
		 * driverList= new HashMap<String, Object>(); driverList.put("driverId",
		 * driverDetails.getEfmFmDriverMaster().getDriverId());
		 * driverList.put("driverName",
		 * driverDetails.getEfmFmDriverMaster().getFirstName());
		 * driverList.put("mobileNumber",
		 * driverDetails.getEfmFmDriverMaster().getMobileNumber());
		 * driverList.put("vendorName",
		 * driverDetails.getEfmFmDriverMaster().getEfmFmVendorMaster().
		 * getVendorName()); driverList.put("vendorId",
		 * driverDetails.getEfmFmDriverMaster().getEfmFmVendorMaster().
		 * getVendorId()); driverCheckInList.add(driverList); } }
		 * if((!(listOfCheckedInVehicle.isEmpty())) ||
		 * listOfCheckedInVehicle.size() !=0){ for(EFmFmVehicleCheckInPO
		 * deviceDetails:listOfCheckedInVehicle){ Map<String, Object>
		 * deviceList= new HashMap<String, Object>(); deviceList.put("deviceId",
		 * deviceDetails.geteFmFmDeviceMaster().getDeviceId());
		 * deviceList.put("mobileNumber",
		 * deviceDetails.geteFmFmDeviceMaster().getMobileNumber());
		 * deviceCheckInList.add(deviceList); } }
		 */
		vehicleCheckInList.put("shiftTimings", shitTimings);
		vehicleCheckInList.put("routesData", allRoutesData);
		// vehicleCheckInList.put("vehicleDetails", checkInList);
		// vehicleCheckInList.put("driverDetails", driverCheckInList);
		// vehicleCheckInList.put("deviceDetails", deviceCheckInList);
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(vehicleCheckInList, MediaType.APPLICATION_JSON).build();
	}

	// Create new bucket save click.
	@POST
	@Path("/savecreatebucket")
	public Response createNewBucketSaveClick(EFmFmAssignRoutePO assignRoutePO) throws ParseException, IOException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("deviceDetails" + assignRoutePO.getCombinedFacility());

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		Map<String, Object> requests = new HashMap<String, Object>();

		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");

		// Query for fetching the Available checked in dummy entity
		List<EFmFmVehicleCheckInPO> checkedInEntity = iVehicleCheckInBO
				.getAllCheckedInVehicleForCreatingNewBucket(assignRoutePO.geteFmFmClientBranchPO().getBranchId());
		if (checkedInEntity.isEmpty()) {
			responce.put("status", "fail");
			log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		log.info("to date" + assignRoutePO.getToDate());
		// Creating new dummy route with out employees
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormat.parse(assignRoutePO.getToDate());
		String requestDate = requestDateFormat.format(date);
		DateFormat requestDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String requestDateAndTime = requestDate + " " + assignRoutePO.getTime();

		String shiftDate = assignRoutePO.getTime();
		Date shift = shiftFormate.parse(shiftDate);
		java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
		EFmFmAssignRoutePO assignRoute = new EFmFmAssignRoutePO();
		EFmFmVehicleCheckInPO vehicleCheckInPO = new EFmFmVehicleCheckInPO();
		vehicleCheckInPO.setCheckInId(checkedInEntity.get(0).getCheckInId());
		checkedInEntity.get(0).setStatus("N");
		log.info("assignRouteId" + assignRoutePO.getSelectedAssignRouteId());
		assignRoute.setEfmFmVehicleCheckIn(vehicleCheckInPO);
		EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
		List<EFmFmRouteAreaMappingPO> allZoneAreas = iRouteDetailBO
				.getAllAreasFromZoneId(assignRoutePO.getSelectedAssignRouteId());
		if (allZoneAreas.isEmpty()) {
			EFmFmZoneMasterPO eFmFmZoneMaster = new EFmFmZoneMasterPO();
			eFmFmZoneMaster.setZoneId(assignRoutePO.getSelectedAssignRouteId());
			EFmFmAreaMasterPO efmFmAreaMaster = new EFmFmAreaMasterPO();
			efmFmAreaMaster.setAreaId(1);
			EFmFmAreaNodalMasterPO eFmFmNodalAreaMaster = new EFmFmAreaNodalMasterPO();
			eFmFmNodalAreaMaster.setNodalPointId(1);
			EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping = new EFmFmRouteAreaMappingPO();
			eFmFmRouteAreaMapping.setEfmFmAreaMaster(efmFmAreaMaster);
			eFmFmRouteAreaMapping.seteFmFmZoneMaster(eFmFmZoneMaster);
			eFmFmRouteAreaMapping.seteFmFmNodalAreaMaster(eFmFmNodalAreaMaster);
			iRouteDetailBO.save(eFmFmRouteAreaMapping);
			allZoneAreas = iRouteDetailBO.getAllAreasFromZoneId(assignRoutePO.getSelectedAssignRouteId());
		}
		routeAreaMapping.setRouteAreaId(allZoneAreas.get(0).getRouteAreaId());
		assignRoute.seteFmFmRouteAreaMapping(routeAreaMapping);
		assignRoute.setEscortRequredFlag("N");
		assignRoute.setAllocationMsg("N");
		assignRoute.setShiftTime(shiftTime);
		assignRoute.setBucketStatus("N");
		assignRoute.setLocationFlg("N");
		assignRoute.setPlannedReadFlg("Y");
		assignRoute.setScheduleReadFlg("Y");
		assignRoute.setRemarksForEditingTravelledDistance("NO");
		assignRoute.setEditDistanceApproval("NO");
		assignRoute.setCreatedDate(new Date());
		assignRoute.setTripStatus("allocated");
		assignRoute.setTripConfirmationFromDriver("Not Delivered");
		assignRoute.setIsToll("No");

		assignRoute.setRoutingCompletionStatus("Started");
        assignRoute.setSelectedBaseFacility(assignRoutePO.geteFmFmClientBranchPO().getBranchId());

		assignRoute.setAssignedVendorName("NA");

		assignRoute.setIsBackTwoBack("N");
		assignRoute.setDistanceUpdationFlg("Y");
		StringBuffer allwayPoints = new StringBuffer();
		if (assignRoutePO.getRouteGenerationType().equalsIgnoreCase("nodalAdhoc")) {
			log.info("getNodalPoints" + assignRoutePO.getNodalPoints());
			StringTokenizer stringTokenizer = new StringTokenizer(assignRoutePO.getNodalPoints(), ",");
			String nodalPointId;
			while (stringTokenizer.hasMoreElements()) {
				nodalPointId = (String) stringTokenizer.nextElement();
				List<EFmFmAreaNodalMasterPO> nodalRoutes = iRouteDetailBO
						.getParticularNodalPointNameByNodalId(Integer.valueOf(nodalPointId));
				allwayPoints.append(nodalRoutes.get(0).getNodalPoints() + "|");
			}
		}
		assignRoute.setNodalPoints(allwayPoints.toString());
		assignRoute.setRouteGenerationType(assignRoutePO.getRouteGenerationType());
		assignRoute.setTripAssignDate(requestDateTimeFormat.parse(requestDateAndTime));
		assignRoute.setTripUpdateTime(requestDateTimeFormat.parse(requestDateAndTime));

		assignRoute.setVehicleStatus("A");
		EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(assignRoutePO.geteFmFmClientBranchPO().getBranchId());
		assignRoute.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		assignRoute.setTripType(assignRoutePO.getTripType());
		iVehicleCheckInBO.update(checkedInEntity.get(0));
		assignRouteBO.save(assignRoute);
		List<EFmFmAssignRoutePO> assignRouteDetail = iCabRequestBO.getLastRouteDetails(
				checkedInEntity.get(0).getCheckInId(), assignRoutePO.geteFmFmClientBranchPO().getBranchId(),
				assignRoutePO.getTripType());
		requests.put("escortRequired", "N");
		requests.put("shiftTime", shiftDate);
		requests.put("vehicleStatus", "A");
		requests.put("bucketStatus", "N");
		requests.put("driverName", checkedInEntity.get(0).getEfmFmDriverMaster().getFirstName());
		requests.put("driverNumber", checkedInEntity.get(0).getEfmFmDriverMaster().getMobileNumber());
		requests.put("vehicleNumber", checkedInEntity.get(0).getEfmFmVehicleMaster().getVehicleNumber());
		requests.put("vendorId", checkedInEntity.get(0).getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorId());
		requests.put("driverId", checkedInEntity.get(0).getEfmFmDriverMaster().getDriverId());
		requests.put("vehicleId", checkedInEntity.get(0).getEfmFmVehicleMaster().getVehicleId());
		requests.put("vehicleAvailableCapacity", checkedInEntity.get(0).getEfmFmVehicleMaster().getCapacity() - 1);
		requests.put("capacity", checkedInEntity.get(0).getEfmFmVehicleMaster().getCapacity());
		requests.put("deviceNumber", checkedInEntity.get(0).geteFmFmDeviceMaster().getDeviceId());
		requests.put("routeName", allZoneAreas.get(0).geteFmFmZoneMaster().getZoneName());
		requests.put("escortName", "Not Required");
		requests.put("tripStatus", "allocated");
		requests.put("tripType", assignRoutePO.getTripType());
		requests.put("routeId", assignRouteDetail.get(assignRouteDetail.size() - 1).getAssignRouteId());
		requests.put("tripActualAssignDate",
				dateFormat.format(assignRouteDetail.get(assignRouteDetail.size() - 1).getTripAssignDate()));
		requests.put("zoneId", assignRouteDetail.get(assignRouteDetail.size() - 1).geteFmFmRouteAreaMapping()
				.geteFmFmZoneMaster().getZoneId());

		EFmFmVehicleMasterPO vehicleMaster = iCabRequestBO
				.getVehicleDetail(checkedInEntity.get(0).getEfmFmVehicleMaster().getVehicleId());
		vehicleMaster.setStatus("allocated");
		iVehicleCheckInBO.update(vehicleMaster);
		EFmFmDriverMasterPO particularDriverDetails = approvalBO
				.getParticularDriverDetail(checkedInEntity.get(0).getEfmFmDriverMaster().getDriverId());
		particularDriverDetails.setStatus("allocated");
		approvalBO.update(particularDriverDetails);
		List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceIdAndBranchId(
				checkedInEntity.get(0).geteFmFmDeviceMaster().getDeviceId(),
				assignRouteDetail.get(0).geteFmFmClientBranchPO().getBranchId());
		log.info(checkedInEntity.get(0).geteFmFmDeviceMaster().getDeviceId() + "deviceDetails" + deviceDetails.size());
		deviceDetails.get(0).setStatus("allocated");
		iVehicleCheckInBO.update(deviceDetails.get(0));
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	// Search employees inside the all routes employeeId based.
	@POST
	@Path("/employeesearchinroute")
	public Response employeeSearchInsideBucket(EFmFmAssignRoutePO assignRoutePO) throws ParseException, IOException {
		IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		Map<String, Object> requests = new HashMap<String, Object>();
		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		log.info(assignRoutePO.getCombinedFacility() + "serviceStart -UserId :" + assignRoutePO.getUserId());
		try {
			if (assignRoutePO.getSearchType().equalsIgnoreCase("vehicle")) {
				List<EFmFmVehicleMasterPO> eFmFmVehicleMaster = iVehicleCheckInBO.getVehicleDetailsFromVehicleNumber(
						assignRoutePO.getEmployeeId(), assignRoutePO.getCombinedFacility());
				if (!(eFmFmVehicleMaster.isEmpty())) {
					List<EFmFmVehicleCheckInPO> vehicleCheckIn = iVehicleCheckInBO.getParticularCheckedInVehicles(
							assignRoutePO.getCombinedFacility(), eFmFmVehicleMaster.get(0).getVehicleId());
					if (!(vehicleCheckIn.isEmpty())) {
						List<EFmFmAssignRoutePO> activeRouteDetail = iRouteDetailBO.getVehicleDetailFromVehicleId(
								assignRoutePO.geteFmFmClientBranchPO().getBranchId(),
								vehicleCheckIn.get(0).getCheckInId());
						if (!(activeRouteDetail.isEmpty())) {
							List<EFmFmAssignRoutePO> routesInZone = assignRouteBO.getAllRoutesInsideZone(
									assignRoutePO.geteFmFmClientBranchPO().getBranchId(), activeRouteDetail.get(0)
											.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
							requests.put("routeName", activeRouteDetail.get(0).geteFmFmRouteAreaMapping()
									.geteFmFmZoneMaster().getZoneName());
							requests.put("NumberOfRoutes", routesInZone.size());
							requests.put("routeId", activeRouteDetail.get(0).geteFmFmRouteAreaMapping()
									.geteFmFmZoneMaster().getZoneId());
							allRoutes.add(requests);
							allRoutesSingleObj.put("totalNumberOfRoutes", routesInZone.size());
						}
					}
				}
			} else {
				List<EFmFmUserMasterPO> requestEmployeeIdExitCheck = iEmployeeDetailBO
						.getParticularEmpDetailsFromEmployeeId(assignRoutePO.getEmployeeId(),
								assignRoutePO.getCombinedFacility());
				List<EFmFmEmployeeTripDetailPO> allocatedEmployeeDetail = iCabRequestBO.getAllocatedEmployeeDetail(
						requestEmployeeIdExitCheck.get(0).getUserId(), assignRoutePO.getCombinedFacility(), new Date());
				if (!(allocatedEmployeeDetail.isEmpty())) {
					assignRoutePO
							.setAssignRouteId(allocatedEmployeeDetail.get(0).getEfmFmAssignRoute().getAssignRouteId());
					List<EFmFmAssignRoutePO> assignRouteDetail = iCabRequestBO
							.getParticularDriverAssignTripDetail(assignRoutePO);
					List<EFmFmAssignRoutePO> routesInZone = assignRouteBO.getAllRoutesInsideZone(
							assignRoutePO.geteFmFmClientBranchPO().getBranchId(),
							assignRouteDetail.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
					requests.put("routeName",
							assignRouteDetail.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					requests.put("NumberOfRoutes", routesInZone.size());
					requests.put("routeId",
							assignRouteDetail.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
					allRoutes.add(requests);
					allRoutesSingleObj.put("totalNumberOfRoutes", assignRouteDetail.size());

				}
			}
		} catch (Exception e) {
			log.error("Error" + e);
		}
		allRoutesSingleObj.put("routeDetails", allRoutes);
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	// Search vehicle inside the all routes .

	@POST
	@Path("/vehiclesearchinroute")
	public Response vehicleSearchInsideBucket(EFmFmAssignRoutePO assignRoutePO) throws ParseException, IOException {
		IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		Map<String, Object> requests = new HashMap<String, Object>();
		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		try {
			List<EFmFmUserMasterPO> requestEmployeeIdExitCheck = iEmployeeDetailBO
					.getParticularEmpDetailsFromEmployeeId(assignRoutePO.getEmployeeId(),
							assignRoutePO.getCombinedFacility());
			List<EFmFmEmployeeTripDetailPO> allocatedEmployeeDetail = iCabRequestBO.getAllocatedEmployeeDetail(
					requestEmployeeIdExitCheck.get(0).getUserId(), assignRoutePO.getCombinedFacility(), new Date());
			if (!(allocatedEmployeeDetail.isEmpty())) {
				assignRoutePO.setAssignRouteId(allocatedEmployeeDetail.get(0).getEfmFmAssignRoute().getAssignRouteId());
				List<EFmFmAssignRoutePO> assignRouteDetail = iCabRequestBO
						.getParticularDriverAssignTripDetail(assignRoutePO);
				List<EFmFmAssignRoutePO> routesInZone = assignRouteBO.getAllRoutesInsideZone(
						assignRoutePO.geteFmFmClientBranchPO().getBranchId(),
						assignRouteDetail.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
				requests.put("routeName",
						assignRouteDetail.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				requests.put("NumberOfRoutes", routesInZone.size());
				requests.put("routeId",
						assignRouteDetail.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
				allRoutes.add(requests);
				allRoutesSingleObj.put("totalNumberOfRoutes", assignRouteDetail.size());
			}
		} catch (Exception e) {
			log.info("Error" + e);
		}
		allRoutesSingleObj.put("routeDetails", allRoutes);
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	// Trip type and Shift Time search inside the all routes
	@POST
	@Path("/triptypesearchinroute")
	public Response tripTypeAndShiftTimeSearch(EFmFmAssignRoutePO assignRoutePO) throws ParseException, IOException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<EFmFmAssignRoutePO> assignRouteDetail = new ArrayList<EFmFmAssignRoutePO>();
		List<String> zones = new ArrayList<String>();
		List<String> vendorName = new ArrayList<String>();

		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> vendorDetails = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> tripAlerts = new ArrayList<Map<String, Object>>();
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		DateFormat timeformate = new SimpleDateFormat("HH:mm:ss");
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String shiftDate = assignRoutePO.getTime();
		Date shift = timeformate.parse(shiftDate);
		java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
		assignRoutePO.setShiftTime(shiftTime);
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = dateFormat.parse(assignRoutePO.getToDate());
		String requestDate = requestDateFormat.format(date);
		assignRoutePO.setToDate(requestDate);
		if (assignRoutePO.getSearchType().equalsIgnoreCase("All")) {
			log.info("assignRoutePO"+assignRoutePO.getCombinedFacility());
			assignRouteDetail = assignRouteBO.getAllRoutesBasedOnTripTypeAndShiftTime(assignRoutePO);
		} else if (assignRoutePO.getSearchType().equalsIgnoreCase("Close")) {
			assignRouteDetail = assignRouteBO.getAllBucketClosedRoutes(assignRoutePO);
		} else if (assignRoutePO.getSearchType().equalsIgnoreCase("Started")) {

			assignRouteDetail = assignRouteBO.getAllStartedRoutes(assignRoutePO);
		} else if (assignRoutePO.getSearchType().equalsIgnoreCase("open")) {

			assignRouteDetail = assignRouteBO.getAllOpenBucketRoutes(assignRoutePO);
		} else if (assignRoutePO.getSearchType().equalsIgnoreCase("closeNotStarted")) {

			assignRouteDetail = assignRouteBO.getAllBucketClosedAndNotStartedRoutes(assignRoutePO);
		} else if (assignRoutePO.getSearchType().equalsIgnoreCase("backToBack")) {
			assignRouteDetail = assignRouteBO.getAllBackToRoutesForParticularShift(assignRoutePO);
		} else if (assignRoutePO.getSearchType().equalsIgnoreCase("Delivered")) {
			assignRouteDetail = assignRouteBO.getAllDeliveredRoutesOnTripTypeAndShiftTime(assignRoutePO);
		} else if (assignRoutePO.getSearchType().equalsIgnoreCase("Not Delivered")) {
			assignRouteDetail = assignRouteBO.getAllNotDeliveredRoutesOnTripTypeAndShiftTime(assignRoutePO);
		} else if (assignRoutePO.getSearchType().equalsIgnoreCase("googleSequence")) {
			assignRouteDetail = assignRouteBO.getAllRoutesBasedOnTripTypeAndShiftTime(assignRoutePO);
		}
		
		log.info("assignRoutePO"+assignRouteDetail.size());

		
		int totalNumberOfEmployee = 0;
		int totalNumberMaleEmployees = 0;
		int totalNumberFemaleEmployees = 0;
		int prgnentLady = 0;
		int physicallyChallaged = 0;
		int injured = 0;
		int isVIP = 0;

		int escortRequired = 0;
		int onBoard = 0;

		/// String vendorName="";
		if (!(assignRouteDetail.isEmpty())) {
			for (EFmFmAssignRoutePO routeDetail : assignRouteDetail) {
				vendorName.add(routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster()
						.getVendorName());
				if (!(zones.contains(routeDetail.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName()))) {
					Map<String, Object> requests = new HashMap<String, Object>();
					zones.add(routeDetail.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					assignRoutePO.setZoneId(routeDetail.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
					List<EFmFmAssignRoutePO> routesInZone = assignRouteBO.getAllRoutesCountInsideZone(assignRoutePO);
					requests.put("routeName",
							routeDetail.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					requests.put("locationFlg", routeDetail.getLocationFlg());
					requests.put("NumberOfRoutes", routesInZone.size());
					requests.put("routeId", routeDetail.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
					List<EFmFmEmployeeTripDetailPO> employeeTripDetail = iCabRequestBO
							.getParticularTripAllEmployees(routeDetail.getAssignRouteId());
					if (!(employeeTripDetail.isEmpty())) {
						totalNumberOfEmployee = totalNumberOfEmployee + employeeTripDetail.size();
						for (EFmFmEmployeeTripDetailPO tripDetail : employeeTripDetail) {

							if (tripDetail.getEmployeeOnboardStatus().equalsIgnoreCase("OnBoard")) {
								onBoard++;
							}

							if (new String(Base64.getDecoder().decode(
									tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()))
											.equalsIgnoreCase("Male")) {
								totalNumberMaleEmployees++;
							} else {
								totalNumberFemaleEmployees++;
							}

							if (new String(Base64.getDecoder().decode(
									tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getPragnentLady()))
											.equalsIgnoreCase("Yes")) {
								prgnentLady++;
							}
							if (new String(Base64.getDecoder().decode(
									tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getIsInjured()))
											.equalsIgnoreCase("Yes")) {
								injured++;

							}
							if (new String(Base64.getDecoder().decode(tripDetail.geteFmFmEmployeeTravelRequest()
									.getEfmFmUserMaster().getPhysicallyChallenged())).equalsIgnoreCase("Yes")) {
								physicallyChallaged++;
							}
							if (null != tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getIsVIP())
								if (new String(Base64.getDecoder().decode(
										tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getIsVIP()))
												.equalsIgnoreCase("Yes")) {
									isVIP++;
								}

						}
					}

					requests.put("physicallyChallenged", physicallyChallaged);
					requests.put("isInjured", injured);
					requests.put("pragnentLady", prgnentLady);
					requests.put("isVIP", isVIP);

					allRoutes.add(requests);
				} else {
					List<EFmFmEmployeeTripDetailPO> employeeTripDetail = iCabRequestBO
							.getParticularTripAllEmployees(routeDetail.getAssignRouteId());
					if (!(employeeTripDetail.isEmpty())) {
						totalNumberOfEmployee = totalNumberOfEmployee + employeeTripDetail.size();
						for (EFmFmEmployeeTripDetailPO tripDetail : employeeTripDetail) {

							if (tripDetail.getEmployeeOnboardStatus().equalsIgnoreCase("OnBoard")) {
								onBoard++;
							}

							if (new String(Base64.getDecoder().decode(
									tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()))
											.equalsIgnoreCase("Male")) {
								totalNumberMaleEmployees++;
							} else {
								totalNumberFemaleEmployees++;
							}

						}

					}

				}
				// only after route close we can see the escort count
				if (routeDetail.getEscortRequredFlag().equalsIgnoreCase("Y")) {
					escortRequired++;
				}

			}

			// Route Summary vendor wise details(Route devision)
			Set<String> mySet = new HashSet<String>(vendorName);
			for (String s : mySet) {
				Map<String, Object> vendorNameCount = new HashMap<String, Object>();
				vendorNameCount.put(s, Collections.frequency(vendorName, s));
				vendorDetails.add(vendorNameCount);
			}

			// Get the routes feedbacks given by the Employees
			// Last 30 days alerts
			try {
				Date fromDate = new Date(minusDayFromDate(date, -30));
				log.info("to Date" + date);
				log.info("from Date" + fromDate);

				List<EFmFmTripAlertsPO> alertDetail = iAlertBO.getAllTripTypeAndShiftAlertsAndFeedbacks(fromDate, date,
						assignRoutePO.getCombinedFacility(), assignRouteDetail.get(0).getTripType(), shiftTime,
						"routing");
				log.info("total alerts" + alertDetail.size());
				if (!alertDetail.isEmpty()) {
					for (EFmFmTripAlertsPO alerts : alertDetail) {
						List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(alerts.getUserId());
						List<EFmFmAlertTypeMasterPO> alertType = iAlertBO
								.getlertDetailFromAlertIds(alerts.getEfmFmAlertTypeMaster().getAlertId());
						Map<String, Object> alert = new HashMap<String, Object>();
						alert.put("alertId", alerts.getTripAlertsId());
						alert.put("alertStatus", alerts.getAlertOpenStatus());
						try {
							alert.put("empId", userDetail.get(0).getEmployeeId());
							alert.put("empName",
									new String(Base64.getDecoder().decode(userDetail.get(0).getFirstName()), "utf-8"));
						} catch (Exception e) {
							alert.put("empId", "NA");
							alert.put("empName", "NA");
							log.info("error" + e);
						}
						alert.put("alertType", alertType.get(0).getAlertType());
						alert.put("alertTitle", alertType.get(0).getAlertTitle());
						alert.put("creationTime", dateFormatter.format(alerts.getCreationTime()));

						try {
							alert.put("alertDate", dateFormatter.format(alerts.getFeadBackSelectedDateTime()));
						} catch (Exception e) {
							alert.put("alertDate", "NA");
							log.info("error" + e);
						}

						alert.put("raiting", alerts.getDriverRaiting());

						alert.put("empDescription", alerts.getEmployeeComments());
						alert.put("tripType", alerts.getTripType());

						alert.put("remarks", alerts.getAlertClosingDescription());
						alert.put("updationDateTime", dateFormatter.format(alerts.getUpdatedTime()));
						try {
							alert.put("shiftTime", timeformate.format(alerts.getShiftTime()));
						} catch (Exception e) {
							alert.put("shiftTime", "NA");
							log.info("error" + e);
						}

						tripAlerts.add(alert);
					}

				}
			} catch (Exception e) {
				log.info("Error" + e);
			}

		}

		allRoutesSingleObj.put("onBoard", onBoard);
		allRoutesSingleObj.put("yetToBoard", totalNumberOfEmployee - onBoard);
		allRoutesSingleObj.put("escortRequired", escortRequired);
		allRoutesSingleObj.put("totalNumberOfRoutes", assignRouteDetail.size());
		allRoutesSingleObj.put("totalNumberMaleEmployees", totalNumberMaleEmployees);
		allRoutesSingleObj.put("totalNumberFemaleEmployees", totalNumberFemaleEmployees);

		allRoutesSingleObj.put("totalNumberOfEmployee", totalNumberOfEmployee);
		allRoutesSingleObj.put("routeDetails", allRoutes);
		allRoutesSingleObj.put("vendorDetails", vendorDetails);
		allRoutesSingleObj.put("feedBacks", tripAlerts);

		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	// Get all Non Nodal Routes of selected routes.
	@POST
	@Path("/nonNodelroutes")
	public Response getAllNonNodalRoutes(EFmFmZoneMasterPO eFmFmZoneMasterPO) throws ParseException, IOException {
		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmZoneMasterPO.getUserId());
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmZoneMasterPO.getUserId()))){
		//
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmZoneMasterPO.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		List<EFmFmZoneMasterPO> allZoneAreas = iRouteDetailBO
				.getAllNonNodalZoneNames(eFmFmZoneMasterPO.getCombinedFacility());
		try {
			if (!(allZoneAreas.isEmpty())) {
				for (EFmFmZoneMasterPO zoneDetail : allZoneAreas) {
					Map<String, Object> requests = new HashMap<String, Object>();
					requests.put("routeName", zoneDetail.getZoneName());
					requests.put("routeId", zoneDetail.getZoneId());
					allRoutes.add(requests);
				}
			}
		} catch (Exception e) {
			log.info("Error" + e);
		}
		allRoutesSingleObj.put("routeDetails", allRoutes);
		log.info("serviceEnd -UserId :" + eFmFmZoneMasterPO.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	// Creating Nodal Routes of selected routes.
	@POST
	@Path("/createNodalRoutes")
	public Response createNodalRoutes(EFmFmAreaNodalMasterPO eFmFmNodalAreaMaster) throws ParseException, IOException {
		Map<String, Object> requests = new HashMap<String, Object>();
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmNodalAreaMaster.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmNodalAreaMaster.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmNodalAreaMaster.getUserId());
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

		String status = "Input";
		StringBuffer temp = new StringBuffer("PleaseCheck");
		if (true) {
			if (eFmFmNodalAreaMaster.getRouteName() == null || eFmFmNodalAreaMaster.getRouteName().isEmpty()) {
				temp.append("::Route name cannot be empty");
				status = "Fail";
			} else {
				CharSequence routeName = eFmFmNodalAreaMaster.getRouteName();
				Matcher route = Validator.alphaNumSpecialCharacers(routeName);
				if (!route.matches() || eFmFmNodalAreaMaster.getRouteName().length() < 3
						|| eFmFmNodalAreaMaster.getRouteName().length() > 200) {
					temp.append("::Route Name should have min 3 max 200 characters");
					status = "Fail";
				}
			}

			if (eFmFmNodalAreaMaster.getAreaName() == null || eFmFmNodalAreaMaster.getAreaName().isEmpty()) {
				temp.append("::Area name cannot be empty");
				status = "Fail";
			} else {
				CharSequence areaName = eFmFmNodalAreaMaster.getAreaName();
				Matcher area = Validator.alphaSpace(areaName);
				if (!area.matches() || eFmFmNodalAreaMaster.getAreaName().length() < 3
						|| eFmFmNodalAreaMaster.getAreaName().length() > 200) {
					temp.append(
							"::Area name should contain only alphabet,space and range min 3 and max 200 characters");
					status = "Fail";
				}
			}

			if (eFmFmNodalAreaMaster.getNodalPointName() == null
					|| eFmFmNodalAreaMaster.getNodalPointName().isEmpty()) {
				temp.append("::Nodal Point name cannot be empty");
				status = "Fail";
			} else {
				CharSequence nodalPointName = eFmFmNodalAreaMaster.getNodalPointName();
				Matcher matcher = Validator.alphaSpace(nodalPointName);
				if (!matcher.matches() || eFmFmNodalAreaMaster.getNodalPointName().length() < 3
						|| eFmFmNodalAreaMaster.getNodalPointName().length() > 200) {
					temp.append("::Nodal Point name can contain Alphabet and space, range min 3 max 200 characters");
					status = "Fail";
				}
			}

			if (eFmFmNodalAreaMaster.getNodalPoints() == null || eFmFmNodalAreaMaster.getNodalPoints().isEmpty()) {
				temp.append("::Nodal Points cannot be empty");
				status = "Fail";
			} else {
				CharSequence nodalPoints = eFmFmNodalAreaMaster.getNodalPoints();
				Matcher matcher = Validator.numbDotComma(nodalPoints);
				if (!matcher.matches() || eFmFmNodalAreaMaster.getNodalPoints().length() > 200) {
					temp.append("::Nodal Points can contain numbers dot comma, range max 200 characters");
					status = "Fail";
				}
			}

			if (eFmFmNodalAreaMaster.getNodalPointsAddress() == null
					|| eFmFmNodalAreaMaster.getNodalPointsAddress().isEmpty()) {
				temp.append("::Nodal Point address cannot be empty");
				status = "Fail";
			} else {
				CharSequence nodalPointAddress = eFmFmNodalAreaMaster.getNodalPointsAddress();
				Matcher matcher = Validator.alphaNumSpecialCharacers(nodalPointAddress);
				if (!matcher.matches() || eFmFmNodalAreaMaster.getNodalPointsAddress().length() > 200) {
					temp.append("::Nodal Point address cannot be more than 200 characters");
					status = "Fail";
				}
			}

			if (status.equals("Fail")) {
				log.info("Invalid input:");
				requests.put("inputInvalid", temp);
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}
		}
		log.info("valid input:");
		
		
		StringTokenizer stringTokenizer = new StringTokenizer(eFmFmNodalAreaMaster.getCombinedFacility(), ",");
        String branchId = "";
		while (stringTokenizer.hasMoreElements()) {
			branchId = (String) stringTokenizer.nextElement();		 
	        
	        List<EFmFmClientRouteMappingPO> allRoutes = iRouteDetailBO.getParticularRouteDetailByClient(branchId,
	        		eFmFmNodalAreaMaster.getRouteName().toUpperCase().trim());			
			if (allRoutes.isEmpty()) {
//				EFmFmClientRouteMappingPO clientDetails=new EFmFmClientRouteMappingPO();
				List<EFmFmZoneMasterPO> newZoneDetail = iRouteDetailBO
						.getAllRouteName(eFmFmNodalAreaMaster.getRouteName().toUpperCase().trim());		
				if (newZoneDetail.isEmpty()){ 
					EFmFmZoneMasterPO eFmFmZoneMaster = new EFmFmZoneMasterPO();
					eFmFmZoneMaster.setZoneName(eFmFmNodalAreaMaster.getRouteName().toUpperCase().trim());
					eFmFmZoneMaster.setStatus("Y");
					eFmFmZoneMaster.setNodalRoute(true);
					eFmFmZoneMaster.setCreationTime(new Date());
					eFmFmZoneMaster.setUpdatedTime(new Date());
					iRouteDetailBO.saveRouteNameRecord(eFmFmZoneMaster);
					newZoneDetail = iRouteDetailBO.getAllRouteName(eFmFmNodalAreaMaster.getRouteName().toUpperCase().trim());
				}
				System.out.println("newZoneDetail"+newZoneDetail.get(0).isNodalRoute());
				List<EFmFmAreaNodalMasterPO> allNodalPoints = iRouteDetailBO
						.getNodalPointsFromNodalName(eFmFmNodalAreaMaster.getNodalPointName().toUpperCase());
				if(allNodalPoints.isEmpty()){
					EFmFmAreaNodalMasterPO eFmFmNodalArea = new EFmFmAreaNodalMasterPO();
					eFmFmNodalArea.setNodalPointDescription(eFmFmNodalAreaMaster.getNodalPointName());
					eFmFmNodalArea.setNodalPointName(eFmFmNodalAreaMaster.getNodalPointName().toUpperCase());
					eFmFmNodalArea.setNodalPointFlg("E");
					eFmFmNodalArea.setCreationTime(new Date());
					eFmFmNodalArea.setNodalPointsAddress(eFmFmNodalAreaMaster.getNodalPointsAddress());
					eFmFmNodalArea.setNodalPoints(eFmFmNodalAreaMaster.getNodalPoints());
					iRouteDetailBO.save(eFmFmNodalArea);
					allNodalPoints = iRouteDetailBO.getNodalPointsFromNodalName(eFmFmNodalAreaMaster.getNodalPointName());
				}	
				EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO = new EFmFmClientRouteMappingPO();
				EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
				eFmFmClientBranchPO.setBranchId(Integer.valueOf(branchId));
				eFmFmClientRouteMappingPO.seteFmFmZoneMaster(newZoneDetail.get(0));
				eFmFmClientRouteMappingPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
				iRouteDetailBO.saveClientRouteMapping(eFmFmClientRouteMappingPO);
				allRoutes = iRouteDetailBO.getParticularRouteDetailByClient(branchId,
		        		eFmFmNodalAreaMaster.getRouteName().toUpperCase().trim());	
			List<EFmFmAreaMasterPO> allAreas = iRouteDetailBO
					.getParticularAreaDeatilsFromAreaName(eFmFmNodalAreaMaster.getAreaName().toUpperCase());
		
			EFmFmRouteAreaMappingPO routeAreaMappingPO = new EFmFmRouteAreaMappingPO();
			log.info("allAreas.get(0)" + allAreas.get(0));
			log.info("allNodalPoints.get(0)" + allNodalPoints.get(0));
			log.info("allZoneRoutes.get(0)" + newZoneDetail.get(0));
			routeAreaMappingPO.setEfmFmAreaMaster(allAreas.get(0));
			routeAreaMappingPO.seteFmFmNodalAreaMaster(allNodalPoints.get(0));
			routeAreaMappingPO.seteFmFmZoneMaster(newZoneDetail.get(0));
			iRouteDetailBO.save(routeAreaMappingPO);
			requests.put("status", "success");

		}
			else{					
					if (allRoutes.get(0).geteFmFmZoneMaster().isNodalRoute() == false) {
						requests.put("status", "routeExistNormal");
						log.info("serviceEnd -UserId :" + eFmFmNodalAreaMaster.getUserId());
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
					if (allRoutes.get(0).geteFmFmZoneMaster().isNodalRoute() == true) {
						requests.put("status", "routeExistNodal");
						log.info("serviceEnd -UserId :" + eFmFmNodalAreaMaster.getUserId());
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
			}
		
		}
		

		log.info("serviceEnd -UserId :" + eFmFmNodalAreaMaster.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	// Get all Area from Area name from area Master based on faciliti ids.
	@POST
	@Path("/allAreas")
	public Response getAlRouteArea(EFmFmClientBranchPO eFmFmClientBranchPO) throws ParseException, IOException {
		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("allAreas..." + eFmFmClientBranchPO.getCombinedFacility());

		
		List<EFmFmClientAreaMappingPO> allAreas = iRouteDetailBO.getClientAreaMappaingDataFromFacilityIds(eFmFmClientBranchPO.getCombinedFacility());
		log.info("allAreas..." + allAreas.size());

		try {
			if (!(allAreas.isEmpty())) {
				for (EFmFmClientAreaMappingPO areaDetail : allAreas) {
					Map<String, Object> requests = new HashMap<String, Object>();
					requests.put("areaName", areaDetail.geteFmFmAreaMaster().getAreaName());
					requests.put("areaId", areaDetail.geteFmFmAreaMaster().getAreaId());
					allRoutes.add(requests);
				}
			}
		} catch (Exception e) {
			log.info("Error" + e);
		}
		allRoutesSingleObj.put("areaDetails", allRoutes);
		// log.info("serviceEnd -UserId :" + eFmFmNodalAreaMaster.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	// Get all Nodal Routes of selected routes.
	@POST
	@Path("/nodelroutes")
	public Response getAllNodelRoutes(EFmFmZoneMasterPO eFmFmZoneMasterPO) throws ParseException, IOException {
		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmZoneMasterPO.getUserId());
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmZoneMasterPO.getUserId()))){
		//
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmZoneMasterPO.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		List<EFmFmZoneMasterPO> allZoneAreas = iRouteDetailBO
				.getAllNodalZoneNames(eFmFmZoneMasterPO.getCombinedFacility());
		try {
			if (!(allZoneAreas.isEmpty())) {
				for (EFmFmZoneMasterPO zoneDetail : allZoneAreas) {
					Map<String, Object> requests = new HashMap<String, Object>();
					requests.put("routeName", zoneDetail.getZoneName());
					requests.put("routeId", zoneDetail.getZoneId());
					allRoutes.add(requests);
				}
			}
		} catch (Exception e) {
			log.info("Error" + e);
		}
		allRoutesSingleObj.put("routeDetails", allRoutes);
		log.info("serviceEnd -UserId :" + eFmFmZoneMasterPO.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	// Get all nodal points of selected routes.
	@POST
	@Path("/routeNodelPoints")
	public Response getNodelPointsFromRouteId(EFmFmZoneMasterPO eFmFmZoneMasterPO) throws ParseException, IOException {
		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		// IUserMasterBO userMasterBO = (IUserMasterBO)
		// ContextLoader.getContext().getBean("IUserMasterBO");
		// Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmZoneMasterPO.getUserId());
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmZoneMasterPO.getUserId()))){
		//
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmZoneMasterPO.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		log.info("zoneId" + eFmFmZoneMasterPO.getRouteId());
		log.info("branchId" + eFmFmZoneMasterPO.getBranchId());
		List<EFmFmRouteAreaMappingPO> allZoneAreas = iRouteDetailBO
				.getAllAreasFromZoneId(eFmFmZoneMasterPO.getRouteId());
		log.info("points" + allZoneAreas.size());
		List<String> zones = new ArrayList<String>();
		try {
			if (!(allZoneAreas.isEmpty())) {
				boolean routeName = true;
				for (EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO : allZoneAreas) {
					Map<String, Object> requests = new HashMap<String, Object>();
					if (!(eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPointName()
							.equalsIgnoreCase("default"))
							&& !(zones
									.contains(eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPointName()))) {
						zones.add(eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPointName());
						requests.put("nodalPoints", eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPoints());
						requests.put("nodalPointId",
								eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPointId());
						requests.put("nodalPointTitle",
								eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPointName());
						requests.put("nodalPointDescription",
								eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPointDescription());
						requests.put("nodalPointFlg",
								eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPointFlg());
						allRoutes.add(requests);
					} else {
						if (routeName && !(zones
								.contains(eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPointName()))) {
							routeName = false;
							requests.put("nodalPoints",
									eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPoints());
							requests.put("nodalPointId",
									eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPointId());
							requests.put("nodalPointTitle",
									eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPointName());
							requests.put("nodalPointDescription",
									eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPointDescription());
							requests.put("nodalPointFlg",
									eFmFmRouteAreaMappingPO.geteFmFmNodalAreaMaster().getNodalPointFlg());
							allRoutes.add(requests);
						}

					}

				}
			}
		} catch (Exception e) {
			log.info("Error" + e);
		}

		allRoutesSingleObj.put("routeDetails", allRoutes);
		log.info("serviceEnd -UserId :" + eFmFmZoneMasterPO.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	// updateZoneName all ZoneName from Zone Master.
	@POST
	@Path("/updateZoneName")
	public Response changeRouteName(EFmFmZoneMasterPO eFmFmZoneMasterPO) throws ParseException, IOException {
		Map<String, Object> responce = new HashMap<String, Object>();
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmZoneMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmZoneMasterPO.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmZoneMasterPO.getUserId());
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

		if (eFmFmZoneMasterPO.getZoneName() == null || eFmFmZoneMasterPO.getZoneName().equals("")) {
			responce.put("InputInvalid", "RouteName cannot be empty");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		} else {
			CharSequence zoneName = eFmFmZoneMasterPO.getZoneName();
			Matcher matcher = Validator.alphaNumSpecialCharacers(zoneName);
			if (!matcher.matches() || eFmFmZoneMasterPO.getZoneName().length() > 100) {
				responce.put("inputInvalid", "RouteName should have only ");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		}
		
		List<EFmFmClientRouteMappingPO> allRoutes = iRouteDetailBO.getParticularRouteDetailByClient(eFmFmZoneMasterPO.getCombinedFacility(),
				eFmFmZoneMasterPO.getZoneName().toUpperCase().trim());	
		if(!(allRoutes.isEmpty())){
			responce.put("status", "exist");
			log.info("serviceEnd -UserId :" + eFmFmZoneMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}
		
		
		List<EFmFmZoneMasterPO> zoneDetails = iRouteDetailBO
				.getParticularRouteFromRouteId(eFmFmZoneMasterPO.getZoneId());		
//		if(zoneDetails.get(0).isNodalRoute()){
//		if(iRouteDetailBO.zoneNameExistCheck(eFmFmZoneMasterPO.getZoneName().toUpperCase().trim())){
//			responce.put("status", "exist");
//			log.info("serviceEnd -UserId :" + eFmFmZoneMasterPO.getUserId());
//			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		}
//		}
//		else if(!(zoneDetails.get(0).isNodalRoute())){
//			if(iRouteDetailBO.nodalZoneNameExistCheck(eFmFmZoneMasterPO.getZoneName().toUpperCase().trim())){
//				responce.put("status", "exist");
//				log.info("serviceEnd -UserId :" + eFmFmZoneMasterPO.getUserId());
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
//			
//		}		
		try {
			if (!(zoneDetails.isEmpty()) ) {
				zoneDetails.get(0).setZoneName(eFmFmZoneMasterPO.getZoneName().toUpperCase().trim());
				iRouteDetailBO.update(zoneDetails.get(0));
				responce.put("status", "success");
			} else {
				responce.put("status", "fail");
			}

		} catch (Exception e) {
			log.info("Error" + e);
		}
		log.info("serviceEnd -UserId :" + eFmFmZoneMasterPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	// updateNodalRouteName all updateNodalRouteName from nodal Master table.
	@POST
	@Path("/updateNodalRouteName")
	public Response updateNodalRouteName(EFmFmZoneMasterPO eFmFmZoneMasterPO) throws ParseException, IOException {
		Map<String, Object> responce = new HashMap<String, Object>();
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmZoneMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmZoneMasterPO.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmZoneMasterPO.getUserId());
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
		if(iRouteDetailBO.nodalZoneNameExistCheck(eFmFmZoneMasterPO.getZoneName().toUpperCase().trim())){
			responce.put("status", "exist");
			log.info("serviceEnd -UserId :" + eFmFmZoneMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		
		
		List<EFmFmZoneMasterPO> zoneDetails = iRouteDetailBO
				.getParticularRouteFromRouteId(eFmFmZoneMasterPO.getZoneId());
		try {
			if (!(zoneDetails.isEmpty())) {
				zoneDetails.get(0).setZoneName(eFmFmZoneMasterPO.getZoneName().toUpperCase());
				iRouteDetailBO.update(zoneDetails.get(0));
				responce.put("status", "success");
			} else {
				responce.put("status", "fail");
			}

		} catch (Exception e) {
			log.info("Error" + e);
		}
		log.info("serviceEnd -UserId :" + eFmFmZoneMasterPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	// updateNodalRoutePointName all updateNodalRoutePointName from
	// NodalRoutePointName Master.
	@POST
	@Path("/updateNodalRoutePointName")
	public Response updateNodalRoutePointName(EFmFmAreaNodalMasterPO eFmFmAreaNodalMaster)
			throws ParseException, IOException {
		Map<String, Object> responce = new HashMap<String, Object>();
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		log.info("eFmFmAreaNodalMaster" + eFmFmAreaNodalMaster.getNodalPoints());

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmAreaNodalMaster.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmAreaNodalMaster.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmAreaNodalMaster.getUserId());
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

		String status = "Input";
		StringBuffer temp = new StringBuffer("PleaseCheck");
		if (true) {
			if (eFmFmAreaNodalMaster.getNodalPointDescription() == null
					|| eFmFmAreaNodalMaster.getNodalPointDescription().isEmpty()) {
				temp.append("::Nodal Point Description cannot be empty");
				status = "Fail";
			} else {
				CharSequence nodalDescription = eFmFmAreaNodalMaster.getNodalPointDescription();
				Matcher matcher = Validator.alphaNumSpecialCharacers(nodalDescription);
				if (!matcher.matches() || eFmFmAreaNodalMaster.getNodalPointDescription().length() > 200) {
					temp.append("::Nodal Point Description cannot be more than 200 characters");
					status = "Fail";
				}
			}

			if (eFmFmAreaNodalMaster.getNodalPointName() == null
					|| eFmFmAreaNodalMaster.getNodalPointName().isEmpty()) {
				temp.append("::Nodal Point name cannot be empty");
				status = "Fail";
			} else {
				CharSequence nodalPointName = eFmFmAreaNodalMaster.getNodalPointName();
				Matcher matcher = Validator.alphaSpace(nodalPointName);
				if (!matcher.matches() || eFmFmAreaNodalMaster.getNodalPointName().length() > 200) {
					temp.append("::Nodal Point name can have alphabet and space and range max 200 characters");
					status = "Fail";
				}
			}

			if (status.equals("Fail")) {
				log.info("Invalid input:");
				responce.put("inputInvalid", temp);
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		}
		log.info("valid input:");

		List<EFmFmAreaNodalMasterPO> nodalPoinDetails = iRouteDetailBO
				.getParticularNodalPointDetailFromNodalPointId(eFmFmAreaNodalMaster.getNodalPointId());
		try {
			if (nodalPoinDetails.isEmpty()) {
				responce.put("status", "NodalRoute not exist");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			if (!(nodalPoinDetails.get(0).getNodalPointName()
					.equalsIgnoreCase(eFmFmAreaNodalMaster.getNodalPointName()))
					|| !(nodalPoinDetails.get(0).getNodalPointFlg()
							.equalsIgnoreCase(eFmFmAreaNodalMaster.getNodalPointFlg()))
					|| !(nodalPoinDetails.get(0).getNodalPoints()
							.equalsIgnoreCase(eFmFmAreaNodalMaster.getNodalPoints()))
					|| !(nodalPoinDetails.get(0).getNodalPointDescription()
							.equalsIgnoreCase(eFmFmAreaNodalMaster.getNodalPointDescription()))) {
				nodalPoinDetails.get(0).setNodalPointName(eFmFmAreaNodalMaster.getNodalPointName().toUpperCase());
				nodalPoinDetails.get(0).setNodalPoints(eFmFmAreaNodalMaster.getNodalPoints());
				nodalPoinDetails.get(0)
						.setNodalPointDescription(eFmFmAreaNodalMaster.getNodalPointDescription().toUpperCase());
				iRouteDetailBO.update(nodalPoinDetails.get(0));
				responce.put("status", "success");
			}

			else {
				responce.put("status", "exist");
			}

		} catch (Exception e) {
			log.info("Error" + e);
		}

		log.info("serviceEnd -UserId :" + eFmFmAreaNodalMaster.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	// public void getGoogleDistance(List<EFmFmAssignRoutePO>
	// assignRouteDetail){
	// ICabRequestBO iCabRequestBO = (ICabRequestBO)
	// ContextLoader.getContext().getBean("ICabRequestBO");
	// IUserMasterBO userMasterBO = (IUserMasterBO)
	// ContextLoader.getContext().getBean("IUserMasterBO");
	//
	// for (EFmFmAssignRoutePO routeDetail : assignRouteDetail) {
	// List<EFmFmEmployeeTripDetailPO> employeeTripDetail = iCabRequestBO
	// .getParticularTripAllEmployees(routeDetail.getAssignRouteId());
	// List<String> employeeIds = new ArrayList<String>();
	// List<EFmFmUserMasterPO> userDetails=null;
	//
	// if (!(employeeTripDetail.isEmpty())) {
	// for (EFmFmEmployeeTripDetailPO tripDetail : employeeTripDetail) {
	// employeeIds.add(tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
	// }
	// if(routeDetail.getTripType().equalsIgnoreCase("DROP")){
	// userDetails=userMasterBO.getNearestEmployeeDetails(employeeIds.toString());
	// }
	// else{
	// userDetails=userMasterBO.getFarthestEmployeeDetails(employeeIds.toString());
	// }
	//
	// }
	// }
	// }

	@POST
	@Path("/toll")
	public Response updateTollOnRouteIfItsThere(EFmFmAssignRoutePO assignRoutePO) {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO
				.getParticularRouteDetailFromAssignRouteId(assignRoutePO.getAssignRouteId());
		assignRoute.get(0).setIsToll(assignRoutePO.getIsToll());
		assignRouteBO.update(assignRoute.get(0));
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/addEmpInRoute")
	public Response addEmployeeDirectInRoutes(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getEmployeeId());
		List<EFmFmUserMasterPO> userDetail = userMasterBO.getEmployeeUserDetailFromEmployeeIdAndFacilityIds(
				assignRoutePO.getCombinedFacility(), assignRoutePO.getEmployeeId());
		if (userDetail.isEmpty()) {
			responce.put("status", "empIdNotExist");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),userDetail.get(0).getUserId()))){
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		//// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }

		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO
				.getParticularRouteDetailFromAssignRouteId(assignRoutePO.getAssignRouteId());
		EFmFmClientBranchPO eFmFmClientBranchPO= new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(assignRoute.get(0).geteFmFmClientBranchPO().getBranchId());

		
		
		List<EFmFmEmployeeTripDetailPO> employeeTripDetail = iCabRequestBO
				.getParticularTripAllEmployees(assignRoutePO.getAssignRouteId());
		if ((assignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()
				- 2) == employeeTripDetail.size() && assignRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")
				|| (assignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()
						- 1) == employeeTripDetail.size()) {
			responce.put("status", "noCapacity");
			log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}
		DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		DateFormat dateHypenFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date startDate = dateTimeFormate
				.parse(dateHypenFormat.format(assignRoute.get(0).getTripAssignDate()) + " " + "00:00");
		List<EFmFmEmployeeRequestMasterPO> employeeRequestMasterPickUp = iCabRequestBO
				.getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(startDate, assignRoute.get(0).getShiftTime(),
						assignRoutePO.getCombinedFacility(), userDetail.get(0).getUserId(),
						assignRoute.get(0).getTripType());
		if (employeeRequestMasterPickUp.isEmpty()) {
			EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMasterPO = new EFmFmEmployeeRequestMasterPO();
			if (assignRoute.get(0).getTripType().trim().equalsIgnoreCase("PICKUP")) {
				eFmFmEmployeeRequestMasterPO.setPickUpTime(assignRoute.get(0).getShiftTime());
			}
			eFmFmEmployeeRequestMasterPO.setReadFlg("N");
			eFmFmEmployeeRequestMasterPO.setRequestDate(startDate);
			eFmFmEmployeeRequestMasterPO.setRequestFrom("E");
			if (assignRoute.get(0).getRouteGenerationType().trim().equalsIgnoreCase("normal"))
				eFmFmEmployeeRequestMasterPO.setRequestType("normal");
			else {
				eFmFmEmployeeRequestMasterPO.setRequestType("nodal");
			}
			eFmFmEmployeeRequestMasterPO.setShiftTime(assignRoute.get(0).getShiftTime());
			eFmFmEmployeeRequestMasterPO.setStatus("N");
			eFmFmEmployeeRequestMasterPO.setTripRequestStartDate(startDate);
			eFmFmEmployeeRequestMasterPO.setTripRequestEndDate(startDate);
			
			eFmFmEmployeeRequestMasterPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
			eFmFmEmployeeRequestMasterPO.setTripType(assignRoute.get(0).getTripType());
			eFmFmEmployeeRequestMasterPO.setEfmFmUserMaster(userDetail.get(0));
			eFmFmEmployeeRequestMasterPO.seteFmFmRouteAreaMapping(assignRoute.get(0).geteFmFmRouteAreaMapping());
			if (assignRoute.get(0).getTripType().trim().equalsIgnoreCase("DROP")) {
				eFmFmEmployeeRequestMasterPO.setDropSequence(1);
			}
			iCabRequestBO.save(eFmFmEmployeeRequestMasterPO);
			employeeRequestMasterPickUp = iCabRequestBO.getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(startDate,
					assignRoute.get(0).getShiftTime(), assignRoute.get(0).getCombinedFacility(),
					userDetail.get(0).getUserId(), assignRoute.get(0).getTripType());
		}

		List<EFmFmEmployeeTravelRequestPO> travelRequestDetails = iCabRequestBO
				.getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(startDate, assignRoute.get(0).getShiftTime(),
						assignRoutePO.getCombinedFacility(), userDetail.get(0).getUserId(),
						assignRoute.get(0).getTripType().trim());
		if (travelRequestDetails.isEmpty()) {
			EFmFmEmployeeTravelRequestPO travelRequestPO = new EFmFmEmployeeTravelRequestPO();
			travelRequestPO.setApproveStatus("Y");
			travelRequestPO.setIsActive("Y");
			travelRequestPO.setReadFlg("Y");
			travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
			travelRequestPO.setRoutingAreaCreation("Y");
			travelRequestPO.setRequestDate(startDate);
			travelRequestPO.setRequestStatus("E");
			if (assignRoute.get(0).getRouteGenerationType().trim().equalsIgnoreCase("normal"))
				travelRequestPO.setRequestType("normal");
			else {
				travelRequestPO.setRequestType("nodal");
			}
			travelRequestPO.setShiftTime(assignRoute.get(0).getShiftTime());
			travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMasterPickUp.get(0));
			travelRequestPO.setEfmFmUserMaster(userDetail.get(0));
			if (assignRoute.get(0).getTripType().trim().equalsIgnoreCase("PICKUP")) {
				travelRequestPO.setPickUpTime(assignRoute.get(0).getShiftTime());
			}
			travelRequestPO.setTripType(assignRoute.get(0).getTripType());
			travelRequestPO.setCompletionStatus("N");
			if (assignRoute.get(0).getTripType().trim().equalsIgnoreCase("DROP")) {
				travelRequestPO.setDropSequence(1);
			}
			travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
			travelRequestPO.seteFmFmRouteAreaMapping(assignRoute.get(0).geteFmFmRouteAreaMapping());
			iCabRequestBO.save(travelRequestPO);
			travelRequestDetails = iCabRequestBO.getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(startDate,
					assignRoute.get(0).getShiftTime(), assignRoutePO.getCombinedFacility(),
					userDetail.get(0).getUserId(), assignRoute.get(0).getTripType().trim());
		} else {
			responce.put("status", "requestExist");
			log.info("serviceEnd -employeeId :" + assignRoutePO.getEmployeeId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		EFmFmEmployeeTripDetailPO employeeTripDetailPO = new EFmFmEmployeeTripDetailPO();
		if (assignRoute.get(0).getTripType().equalsIgnoreCase("DROP")) {
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
		employeeTripDetailPO.seteFmFmEmployeeTravelRequest(travelRequestDetails.get(0));
		employeeTripDetailPO.setEfmFmAssignRoute(assignRoute.get(0));
		employeeTripDetailPO.setCurrenETA("0");
		employeeTripDetailPO.setEmployeeStatus("allocated");
		employeeTripDetailPO.setComingStatus("Yet to confirm");
		employeeTripDetailPO.setEmployeeOnboardStatus("NO");
		iCabRequestBO.update(employeeTripDetailPO);
		travelRequestDetails.get(0).setRequestColor("yellow");
		travelRequestDetails.get(0).setReadFlg("R");
		iCabRequestBO.update(travelRequestDetails.get(0));

		responce.put("status", "success");
		log.info("serviceEnd -employeeId :" + assignRoutePO.getEmployeeId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	public long getDisableTime(int hours, int minutes, Date checkIndate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkIndate);
		calendar.add(Calendar.HOUR, hours);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTimeInMillis();
	}

	public long minusDayFromDate(Date date, int numDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, numDays);
		return calendar.getTimeInMillis();
	}
	
	private boolean escortProvideVerification(String gender,String escortWindowOpen,String escortRequireDetails,Date escortStartTime,Date escortEndTime
			,Date shiftTime,Date requestDate,List<EFmFmEmployeeTripDetailPO> finalTripEmployees) throws ParseException, UnsupportedEncodingException{
		/*
		 * This methord has used to verifay all the senario for checking Escort conditon .
		 */
//		System.out.println("shiftTime 1=="+shiftTime);
//		System.out.println("escortStartTime 1=="+escortStartTime);
//		System.out.println("escortEndTime 1=="+escortEndTime);
		
		int shiftStartHR=0;
		int shiftEndHR=0;
		int shiftHR=0;
		
		boolean femalepresent=false;
		boolean onefemale=false;
		boolean allfemale=true;
		int femailCount=0;
		for(EFmFmEmployeeTripDetailPO p:finalTripEmployees){
			if(new String(Base64.getDecoder().decode(p.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),"utf-8").trim().equalsIgnoreCase("Female")){
				femalepresent=true;
				femailCount++;
			}
			if(!(new String(Base64.getDecoder().decode(p.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),"utf-8").trim().equalsIgnoreCase("Female"))){
				allfemale=false;
				
			}
		}
		//For counting one femail ..
		if(femailCount==1){
			onefemale=true;
		}
		
		

		SimpleDateFormat sdfCalender =new SimpleDateFormat("dd/MM/YYYY");		
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		 
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(escortStartTime);
		 long millis = cal.getTimeInMillis();
		 java.sql.Time shiftTimeValueStartEscort = new java.sql.Time(millis);
		 shiftStartHR=cal.getTime().getHours();
	
//		 String CalenData=sdfCalender.format(Calendar.getInstance().getTime());
//		 CalenData=CalenData.concat(" ");
//		 CalenData=CalenData.concat(shiftTimeValue.toString());
		 
		 String CalenData=sdfCalender.format(requestDate);
		 CalenData=CalenData.concat(" ");
		 CalenData=CalenData.concat(shiftTimeValueStartEscort.toString());

		 escortStartTime=sdf.parse(CalenData);
		 
		 
		 
		 cal = Calendar.getInstance();
		 cal.setTime(escortEndTime);
		  millis = cal.getTimeInMillis();
		  java.sql.Time shiftTimeValueEndEscort = new java.sql.Time(millis);
		 shiftEndHR=cal.getTime().getHours();
		 
			Calendar c=Calendar.getInstance();
			c.setTime(requestDate);

			if(shiftStartHR>=shiftEndHR){			 
		 c.add(Calendar.DATE, 1);
		 CalenData=sdfCalender.format(c.getTime());
		 CalenData=CalenData.concat(" ");
		 CalenData=CalenData.concat(shiftTimeValueEndEscort.toString());		 
		 escortEndTime=sdf.parse(CalenData);			
			}else if(shiftStartHR<shiftEndHR) {			
		 CalenData=sdfCalender.format(c.getTime());
		 CalenData=CalenData.concat(" ");
		 CalenData=CalenData.concat(shiftTimeValueEndEscort.toString());		 
		 escortEndTime=sdf.parse(CalenData);
			}
			
			
		 cal = Calendar.getInstance();
		 cal.setTime(shiftTime);
		  millis = cal.getTimeInMillis();
		  java.sql.Time shiftTimeValue =  new java.sql.Time(millis);
		 
		 shiftHR=cal.getTime().getHours();//Calculating Shift timeing
		 
//		 System.out.println("Shift Date and current time=="+requestDate.equals(Calendar.getInstance().getTime()));
//		 System.out.println("sdfCalender.=="+sdfCalender.parse((sdfCalender.format(Calendar.getInstance().getTime()))));
//		 System.out.println("sdfCalender.=="+sdfCalender.parse((sdfCalender.format(requestDate))));
		 SimpleDateFormat s =new SimpleDateFormat("dd-MM-yyyy");
//		 DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		 String currentDateStr=s.format(Calendar.getInstance().getTime());
		 String requestDateStr=s.format(requestDate);
		 
//		 System.out.println("currentDateStr=="+currentDateStr);
//		 System.out.println("requestDateStr  =="+requestDateStr);
////		 SimpleDateFormat s =new SimpleDateFormat("dd/MM/YYYY");
//		 System.out.println("currentDateStr Date  =="+(Date)s.parse(currentDateStr));
//		 System.out.println("requestDateStr Date =="+s.parse(requestDateStr));
		 boolean bln=s.parse(currentDateStr).equals(s.parse(requestDateStr));
		 System.out.println("bln Date =="+bln);
		 if(shiftStartHR>shiftHR){		
			 
			 c=Calendar.getInstance();
			 c.setTime(requestDate);
			 System.out.println("Data for shift=="+c.getTime());
				
			 c.add(Calendar.DATE, -1);
			 System.out.println("Data for shift 1=="+c.getTime());
			 CalenData=sdfCalender.format(c.getTime());
			 CalenData=CalenData.concat(" ");
			 
			 CalenData=CalenData.concat(shiftTimeValueStartEscort.toString());
			 escortStartTime=sdf.parse(CalenData);
			 
			 c=Calendar.getInstance();
			 c.setTime(requestDate);
			 CalenData=sdfCalender.format(c.getTime());
			 CalenData=CalenData.concat(" ");
			 
			 CalenData=CalenData.concat(shiftTimeValueEndEscort.toString());		 
			 escortEndTime=sdf.parse(CalenData);			
			}
		 
		  c=Calendar.getInstance();
				 c.setTime(requestDate);
		 CalenData=sdfCalender.format(c.getTime());
		 CalenData=CalenData.concat(" ");
		 CalenData=CalenData.concat(shiftTimeValue.toString());

		 shiftTime=sdf.parse(CalenData);
		 System.out.println("shiftTime=="+shiftTime);
		 System.out.println("escortStartTime=="+escortStartTime);
		 System.out.println("escortEndTime=="+escortEndTime);
		 
		 System.out.println("shiftTime.equals(escortStartTime)=="+shiftTime.equals(escortStartTime));
		 System.out.println("shiftTime.after(escortStartTime)=="+shiftTime.after(escortStartTime));
		 System.out.println("shiftTime.equals(escortEndTime)=="+shiftTime.equals(escortEndTime));
		 System.out.println("shiftTime.before(escortEndTime)=="+shiftTime.before(escortEndTime));
		 
		if(escortRequireDetails.trim().equalsIgnoreCase("None")){
			return false;
		}
		
		else if(escortRequireDetails.trim().equalsIgnoreCase("Always")){
			if(escortWindowOpen.equalsIgnoreCase("Y")){
				if((shiftTime.equals(escortStartTime) || shiftTime.after(escortStartTime)) &&
						(shiftTime.equals(escortEndTime) || shiftTime.before(escortEndTime))){
					return true;
				}
				
		}else{
			return true;
		}
		
	}
		
		else if(escortRequireDetails.trim().equalsIgnoreCase("femalepresent")){
			if(femalepresent)
			if(escortWindowOpen.equalsIgnoreCase("Y")){				
				if((shiftTime.equals(escortStartTime) || shiftTime.after(escortStartTime)) &&
						(shiftTime.equals(escortEndTime) || shiftTime.before(escortEndTime))){
					return true;
				}
				
		}else{			
			return true;
		}
		
	}
		else if(escortRequireDetails.trim().equalsIgnoreCase("firstlastfemale")){
			if(gender.trim().equalsIgnoreCase("Female"))
			if(escortWindowOpen.equalsIgnoreCase("Y")){
				if((shiftTime.equals(escortStartTime) || shiftTime.after(escortStartTime)) &&
						(shiftTime.equals(escortEndTime) || shiftTime.before(escortEndTime))){
					return true;
				}
				
		}else{
			return true;
		}
		
	}
		
		
		else if(escortRequireDetails.trim().equalsIgnoreCase("allFemale")){
			if(allfemale)
			if(escortWindowOpen.equalsIgnoreCase("Y")){				
				if((shiftTime.equals(escortStartTime) || shiftTime.after(escortStartTime)) &&
						(shiftTime.equals(escortEndTime) || shiftTime.before(escortEndTime))){
					return true;
				}
				
		}else{			
			return true;
		}
		
	}
		
		
		else if(escortRequireDetails.trim().equalsIgnoreCase("femaleAlone")){
			if(onefemale)
			if(escortWindowOpen.equalsIgnoreCase("Y")){				
				if((shiftTime.equals(escortStartTime) || shiftTime.after(escortStartTime)) &&
						(shiftTime.equals(escortEndTime) || shiftTime.before(escortEndTime))){
					return true;
				}
				
		}else{			
			return true;
		}
		
	}

		return false;
	}

	private String errorMsgForEscort(String tripType,String escortVarType){
		String errorMsg="";
		if(escortVarType.trim().equalsIgnoreCase("Always")){
			errorMsg="Sorry you can not close this route because escort is mandatory.";
		}
		else if(escortVarType.trim().equalsIgnoreCase("femalepresent")){
			errorMsg="Sorry you can not close this route because female is present and escort is mandatory.";
		}
		else if(escortVarType.trim().equalsIgnoreCase("firstlastfemale")){
			if(tripType.trim().equalsIgnoreCase("Drop"))
			errorMsg="Sorry you can not close this route because last drop is female and escort is mandatory.";
			if(tripType.trim().equalsIgnoreCase("Pickup"))
				errorMsg="Sorry you can not close this bucket because first pickup is female and escort is mandatory.";
		}
		else if(escortVarType.trim().equalsIgnoreCase("allFemale")){
			errorMsg="Sorry you can not close this route because all employee is female and escort is mandatory.";
		}
		else if(escortVarType.trim().equalsIgnoreCase("femaleAlone")){
			errorMsg="Sorry you can not close this route because female is alone and escort is mandatory.";
		}
		
		return errorMsg;
	}
	
	

}