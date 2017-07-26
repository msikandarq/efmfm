package com.newtglobal.eFmFmFleet.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.newtglobal.eFmFmFleet.business.dao.daoImpl.SchedulerCabRequestDAOImpl;
import com.newtglobal.eFmFmFleet.business.dao.daoImpl.SchedulerVehicleCheckInDAOImpl;
import com.newtglobal.eFmFmFleet.eFmFmFleet.CalculateDistance;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Geocode;
import com.newtglobal.eFmFmFleet.eFmFmFleet.MessagingService;
import com.newtglobal.eFmFmFleet.eFmFmFleet.PushNotificationService;
import com.newtglobal.eFmFmFleet.model.EFmFmActualRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlertTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeRequestMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLiveRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;

public class SchedulingService {

	private static final int DELAY_THROTTLE = 0;
	private static Log log = LogFactory.getLog(SchedulingService.class);
	private static byte[] key;
	private static String keyString = "iI1tzzodgnjqhMQq6zIyPJ9iy40=";

	private final EntityManagerFactory entityMangerFactory;
	final SchedularMessagingService messaging = new SchedularMessagingService();

	public SchedulingService(EntityManagerFactory _entityMangerFactory) {
		entityMangerFactory = _entityMangerFactory;
	}

	public void updateTravelRequest(EFmFmEmployeeRequestMasterPO travelRequest)
			throws ParseException, IOException, Exception {
		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}

		try {
			long l1 = System.currentTimeMillis();
			DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			SimpleDateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
			SchedulerCabRequestDAOImpl requestDAOImpl = new SchedulerCabRequestDAOImpl(em);
			DateFormat startDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			if(travelRequest.getTripRequestEndDate().getTime()>new Date().getTime()){				
			int daysDiff = 0;
			long diff = travelRequest.getTripRequestEndDate().getTime()
					- travelRequest.getTripRequestStartDate().getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			daysDiff = (int) diffDays;
			if (daysDiff > 12) {
				daysDiff = 12;
			}
			for (int i = 0; i <= daysDiff; i++) {
				String reqDate="";
				
				if(travelRequest.getTripRequestStartDate().getTime()<new Date().getTime()){
					reqDate = startDateFormat.format(new Date());
				}
				else{
					reqDate = startDateFormat.format(travelRequest.getTripRequestStartDate());
				}
				Date startRequestDate = startDateFormat.parse(reqDate);
				Calendar currentStartDate = new GregorianCalendar();
				currentStartDate.setTime(startRequestDate);
				currentStartDate.add(Calendar.DATE, i);
				Date finalRequestDate = currentStartDate.getTime();	
				
				List<EFmFmEmployeeTravelRequestPO> employeeTravelRequest =null;
				
				if(travelRequest.getRequestType().equalsIgnoreCase("guest")){				
					   employeeTravelRequest = requestDAOImpl
						.particularDateRequestForEmployeesByShiftTime(finalRequestDate,
								travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
								travelRequest.getEfmFmUserMaster().getUserId(), travelRequest.getTripType(),travelRequest.getShiftTime());
				}else{
					  employeeTravelRequest = requestDAOImpl
								.particularDateRequestForEmployees(finalRequestDate,
										travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
										travelRequest.getEfmFmUserMaster().getUserId(), travelRequest.getTripType());
				}			
				if (!(employeeTravelRequest.isEmpty())) {
					//This code is just checking for the given trip Id request is canceled or not	
					String cancelStatus="N";
					String requestStatus="N";
					boolean cancelFlg=false;
					for(EFmFmEmployeeTravelRequestPO existTravelRequest:employeeTravelRequest){
						if(existTravelRequest.getReadFlg().equalsIgnoreCase("Y") || existTravelRequest.getReadFlg().equalsIgnoreCase("R")){
							requestStatus="Y";
						}
						else if (!(existTravelRequest.getReadFlg().equalsIgnoreCase("Y"))
								&& (existTravelRequest.getRequestStatus().equalsIgnoreCase("CW")
										|| existTravelRequest.getRequestStatus().equalsIgnoreCase("CM"))
								||existTravelRequest.getRequestStatus().equalsIgnoreCase("C")) {
							
							
							if(existTravelRequest.geteFmFmEmployeeRequestMaster().getTripId()== travelRequest.getTripId()){	
								daysDiff++;
								//EN refer to Cancel request is there for this trip ID so don't create a request for same day
								cancelStatus="E";
								cancelFlg=true;
								break;
							}
							//EN refer to Cancel request is there for this trip ID but again created a request for same day
							cancelStatus="EN";
							
						}
					}			
					if(cancelStatus.equalsIgnoreCase("EN") && !(cancelFlg) && (!(requestStatus.equalsIgnoreCase("Y")))){
						employeeTravelRequest.clear();	
					}
	
				}
				if (dateformate.format(travelRequest.getTripRequestEndDate()).equalsIgnoreCase(dateformate.format(finalRequestDate))) {
					//Request Disable at request master
					travelRequest.setStatus("N");
					travelRequest.setReadFlg("N");
					em.merge(travelRequest);
				}
				if (employeeTravelRequest.isEmpty()   && (travelRequest.getTripRequestEndDate().getTime()>finalRequestDate.getTime())) {
					EFmFmEmployeeTravelRequestPO travelRequestPO = new EFmFmEmployeeTravelRequestPO();
					EFmFmUserMasterPO userMaster = new EFmFmUserMasterPO();
					EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping = new EFmFmRouteAreaMappingPO();
					eFmFmRouteAreaMapping.setRouteAreaId(travelRequest.geteFmFmRouteAreaMapping().getRouteAreaId());
					userMaster.setUserId(travelRequest.getEfmFmUserMaster().getUserId());
					travelRequestPO.setEfmFmUserMaster(userMaster);
					travelRequestPO.seteFmFmRouteAreaMapping(eFmFmRouteAreaMapping);
					
					travelRequestPO.setCompletionStatus("N");
					if (travelRequest.getTripType().equalsIgnoreCase("DROP")) {
						travelRequestPO.setDropSequence(travelRequest.getDropSequence());
					}
					travelRequestPO.seteFmFmEmployeeRequestMaster(travelRequest);
					Date todayDate = new Date();
					Calendar cal = new GregorianCalendar();
					cal.setTime(todayDate);
					cal.add(Calendar.DATE, 1);
					Date currentDate = cal.getTime();
					Calendar weekEnd = new GregorianCalendar();
					weekEnd.setTime(todayDate);
					weekEnd.add(Calendar.DATE, 2);
					Date weekDate = weekEnd.getTime();

					Format formatter = new SimpleDateFormat("dd-MM-yyyy");
					String todaDate = formatter.format(currentDate);
					String weekEndDate = formatter.format(weekDate);
					log.info("Trip Id:" + travelRequest.getTripId());
					if(travelRequest.getTripId()==25032){
						log.info(travelRequest.getTripRequestEndDate()+"Trip Id:" + travelRequest.getTripId());
					}
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					if (travelRequest.getTripRequestEndDate().getTime() > (new Date()).getTime() && travelRequest
							.getTripRequestStartDate().getTime() == dateFormat.parse(todaDate).getTime())
						travelRequestPO.setRequestDate(finalRequestDate);
					else
						travelRequestPO.setRequestDate(finalRequestDate);

					EFmFmClientMasterPO eFmFmClientMasterPO = new EFmFmClientMasterPO();
					eFmFmClientMasterPO
							.setClientId(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
					travelRequestPO.setRequestType(travelRequest.getRequestType());
					travelRequestPO.setShiftTime(travelRequest.getShiftTime());
					EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
					eFmFmClientBranchPO.setBranchId(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
					
					if (!employeeTravelRequest.isEmpty()) {
						String weekOffs = employeeTravelRequest.get(employeeTravelRequest.size() - 1)
								.getEfmFmUserMaster().getWeekOffDays();
						String currentDay = new SimpleDateFormat("EEEE").format(currentDate);
						String requestDateTime = (new StringBuilder(String.valueOf(dateFormat
								.format(employeeTravelRequest.get(employeeTravelRequest.size() - 1).getRequestDate())
								.toString()))).append(" ").append(
										employeeTravelRequest.get(employeeTravelRequest.size() - 1).getShiftTime())
										.toString();
						String requestDateOnly = dateFormat
								.format(employeeTravelRequest.get(employeeTravelRequest.size() - 1).getRequestDate());
						Date todayDateTimeOnly = dateFormat.parse(requestDateOnly);
						String currentDateOnly = dateFormat.format(new Date());
						Date currentDateForTest = dateFormat.parse(currentDateOnly);
						Date todayDateTime = dateTimeFormate.parse(requestDateTime);
						if (todayDateTime.getTime() < (new Date()).getTime()) {
							if (!(dateformate.format(new Date()).equalsIgnoreCase(dateformate.format(
									employeeTravelRequest.get(employeeTravelRequest.size() - 1).getRequestDate())))) {
								// employeeTravelRequest.get(employeeTravelRequest.size()
								// - 1)
								// .setReadFlg("N");
								// employeeTravelRequest.get(employeeTravelRequest.size()
								// - 1)
								// .setIsActive("Y");
								// employeeTravelRequest.get(employeeTravelRequest.size()
								// - 1)
								// .setRequestStatus("C");
								// em.merge((EFmFmEmployeeTravelRequestPO)
								// employeeTravelRequest
								// .get(employeeTravelRequest.size() - 1));
								// em.getTransaction().commit();
								// em.close();
							}
							String weekOffDays[] = employeeTravelRequest.get(employeeTravelRequest.size() - 1)
									.getEfmFmUserMaster().getWeekOffDays().split(",");
							if (weekOffDays[weekOffDays.length - 1]
									.equalsIgnoreCase((new SimpleDateFormat("EEEE")).format(new Date()))) {
								travelRequestPO.setRequestDate(dateFormat.parse(todaDate));
								if (weekOffDays[weekOffDays.length - 1]
										.equalsIgnoreCase((new SimpleDateFormat("EEEE")).format(new Date()))
										&& (employeeTravelRequest.get(employeeTravelRequest.size() - 1).getShiftTime()
												.getHours() == 1
												|| employeeTravelRequest.get(employeeTravelRequest.size() - 1)
														.getShiftTime().getHours() == 3)) {
									travelRequestPO.setRequestDate(dateFormat.parse(weekEndDate));
								}
								EFmFmEmployeeRequestMasterPO employeeRequestMaster = new EFmFmEmployeeRequestMasterPO();
								employeeRequestMaster.setTripId(travelRequest.getTripId());
								travelRequestPO.setShiftTime(travelRequest.getShiftTime());
								travelRequestPO.setPickUpTime(travelRequest.getPickUpTime());
								travelRequestPO.setApproveStatus("Y");
								travelRequestPO.setRequestType(travelRequest.getRequestType());
								travelRequestPO.setTripType(travelRequest.getTripType());
								travelRequestPO.setRequestStatus(travelRequest.getRequestFrom());								
								travelRequestPO.setLocationFlg(travelRequest.getLocationFlg());
								travelRequestPO.setTripSheetStatus("NOCHANGES");								
								travelRequestPO.setLocationWaypointsIds(travelRequest.getLocationWaypointsIds());								
								travelRequestPO.setReadFlg("Y");
						        travelRequestPO.setRoutingAreaCreation("Y");						        
						        travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);					        
								travelRequestPO.setIsActive("Y");								
								travelRequestPO.setProjectId(travelRequest.getProjectId());								
								travelRequestPO.setReportingManagerUserId(travelRequest.getReportingManagerUserId());
								travelRequestPO.setReqApprovalStatus(travelRequest.getReqApprovalStatus());								
								travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMaster);
								
								if (!(weekOffs.contains(currentDay))){
									if(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getApprovalProcess().equalsIgnoreCase("Yes")){
										List<String> approvalCount = requestDAOImpl
												.getListOfApprovalPendingRequestForUser(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
														travelRequest.getEfmFmUserMaster().getUserId(),"N");					
										 if((travelRequest.getReqApprovalStatus().equalsIgnoreCase("N") 
												 && travelRequest.getRequestType().equalsIgnoreCase("guest")) ||  travelRequest.getTimeFlg().equalsIgnoreCase("A")){	
											    travelRequestPO.setApproveStatus("N");	
												travelRequestPO.setReqApprovalStatus("N");
												em.merge(travelRequestPO);
										 }else if(travelRequest.getReqApprovalStatus().equalsIgnoreCase("Y")){												
											 travelRequestPO.setReqApprovalStatus("Y");
											 em.merge(travelRequestPO);
										}else if(!(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size())){
											em.merge(travelRequestPO);
										}else if(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size()){									
											if(approvalCount.contains(dateformate.format(travelRequestPO.getRequestDate()))){
												travelRequestPO.setApproveStatus("Y");	
												travelRequestPO.setReqApprovalStatus("N");
												em.merge(travelRequestPO);
											}else{
												travelRequestPO.setApproveStatus("N");	
												travelRequestPO.setReqApprovalStatus("N");
												em.merge(travelRequestPO);
											}									
										}else{
											travelRequestPO.setApproveStatus("N");	
											travelRequestPO.setReqApprovalStatus("N");
											em.merge(travelRequestPO);
										}	
									}else{
										em.merge(travelRequestPO);
									}
								}
							//	em.getTransaction().commit();
							//	em.close();
								return;
							}
							if (todayDateTimeOnly.getTime() < currentDateForTest.getTime() && !(dateFormat
									.format(employeeTravelRequest.get(employeeTravelRequest.size() - 1)
											.getRequestDate())
									.toString().equalsIgnoreCase(dateFormat.format(new Date()).toString()))) {
								if (todayDateTime.getTime() < (new Date()).getTime()) {
									travelRequestPO.setRequestDate(dateFormat.parse(dateFormat.format(new Date())));
									EFmFmEmployeeRequestMasterPO employeeRequestMaster = new EFmFmEmployeeRequestMasterPO();
									if (weekOffDays[weekOffDays.length - 1]
											.equalsIgnoreCase(
													(new SimpleDateFormat("EEEE"))
															.format(currentDate))
											&& employeeTravelRequest.get(employeeTravelRequest.size() - 1)
													.getRequestType().equalsIgnoreCase("normal")
											&& (employeeTravelRequest.get(employeeTravelRequest.size() - 1)
													.getShiftTime().getHours() == 1
													|| employeeTravelRequest.get(employeeTravelRequest.size() - 1)
															.getShiftTime().getHours() == 3)) {
										travelRequestPO.setRequestDate(dateFormat.parse(todaDate));
									}
									employeeRequestMaster.setTripId(travelRequest.getTripId());
									travelRequestPO.setShiftTime(travelRequest.getShiftTime());
									travelRequestPO.setPickUpTime(travelRequest.getPickUpTime());
									travelRequestPO.setApproveStatus("Y");
									travelRequestPO.setRequestType(travelRequest.getRequestType());
									travelRequestPO.setTripType(travelRequest.getTripType());
									travelRequestPO.setRequestStatus(travelRequest.getRequestFrom());
									travelRequestPO.setLocationFlg(travelRequest.getLocationFlg());
									travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
									travelRequestPO.setTripSheetStatus("NOCHANGES");
									travelRequestPO.setLocationWaypointsIds(travelRequest.getLocationWaypointsIds());
									travelRequestPO.setReadFlg("Y");
							        travelRequestPO.setRoutingAreaCreation("Y");
									travelRequestPO.setIsActive("Y");
									travelRequestPO.setProjectId(travelRequest.getProjectId());								
									travelRequestPO.setReportingManagerUserId(travelRequest.getReportingManagerUserId());
									travelRequestPO.setReqApprovalStatus(travelRequest.getReqApprovalStatus());	
									travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMaster);
									if(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getApprovalProcess().equalsIgnoreCase("Yes")){
										List<String> approvalCount = requestDAOImpl
												.getListOfApprovalPendingRequestForUser(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
														travelRequest.getEfmFmUserMaster().getUserId(),"N");					
										 if((travelRequest.getReqApprovalStatus().equalsIgnoreCase("N") 
												 && travelRequest.getRequestType().equalsIgnoreCase("guest")) ||  travelRequest.getTimeFlg().equalsIgnoreCase("A")){	
											    travelRequestPO.setApproveStatus("N");	
												travelRequestPO.setReqApprovalStatus("N");
												em.merge(travelRequestPO);
										 }else if(travelRequest.getReqApprovalStatus().equalsIgnoreCase("Y")){												
											 travelRequestPO.setReqApprovalStatus("Y");
											 em.merge(travelRequestPO);
										}else if(!(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size())){
											
											em.merge(travelRequestPO);
										}else if(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size()){									
											if(approvalCount.contains(dateformate.format(travelRequestPO.getRequestDate()))){
												travelRequestPO.setApproveStatus("Y");	
												travelRequestPO.setReqApprovalStatus("N");
												em.merge(travelRequestPO);
											}else{
												travelRequestPO.setApproveStatus("N");	
												travelRequestPO.setReqApprovalStatus("N");
												em.merge(travelRequestPO);
											}									
										}else{
											travelRequestPO.setApproveStatus("N");	
											travelRequestPO.setReqApprovalStatus("N");
											em.merge(travelRequestPO);
										}							
									}else{
										em.merge(travelRequestPO);
									}
									
									
								//	em.getTransaction().commit();
								//	em.close();
									return;
								}
							}
						}
						// }
					}

					// List<EFmFmEmployeeTravelRequestPO>
					// newemployeeTravelRequest = requestDAOImpl
					// .getParticularRequestDetail(travelRequestPO);
					if (employeeTravelRequest.isEmpty()) {
						// List<EFmFmEmployeeTravelRequestPO>
						// readRequestFromTravelRequest = requestDAOImpl
						// .getParticularReadRequestDetail(travelRequestPO);

						// if (!readRequestFromTravelRequest.isEmpty() ||
						// readRequestFromTravelRequest.size() != 0) {
						// Date requestDate =
						// dateFormat.parse(dateFormat.format(readRequestFromTravelRequest
						// .get(readRequestFromTravelRequest.size() -
						// 1).getRequestDate()));
						// Date requestEndDate =
						// dateFormat.parse(dateFormat.format(travelRequest.getTripRequestEndDate()));
						// if (requestEndDate.getTime() > requestDate.getTime())
						// {
						// travelRequestPO.setRequestDate(dateFormat.parse(todaDate));
						// } else {
						// em.getTransaction().commit();
						// em.close();
						// return;
						// }
						// }
						EFmFmEmployeeRequestMasterPO employeeRequestMaster = new EFmFmEmployeeRequestMasterPO();
						employeeRequestMaster.setTripId(travelRequest.getTripId());
						travelRequestPO.setShiftTime(travelRequest.getShiftTime());
						travelRequestPO.setApproveStatus("Y");
						travelRequestPO.setPickUpTime(travelRequest.getPickUpTime());
						travelRequestPO.setRequestType(travelRequest.getRequestType());
						travelRequestPO.setTripType(travelRequest.getTripType());
						travelRequestPO.setRequestStatus(travelRequest.getRequestFrom());
						travelRequestPO.setLocationFlg(travelRequest.getLocationFlg());
						travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
							travelRequestPO.setTripSheetStatus("NOCHANGES");
						travelRequestPO.setLocationWaypointsIds(travelRequest.getLocationWaypointsIds());
						travelRequestPO.setReadFlg("Y");
				        travelRequestPO.setRoutingAreaCreation("Y");
						travelRequestPO.setIsActive("Y");
						travelRequestPO.setProjectId(travelRequest.getProjectId());								
						travelRequestPO.setReportingManagerUserId(travelRequest.getReportingManagerUserId());
						travelRequestPO.setReqApprovalStatus(travelRequest.getReqApprovalStatus());	
						travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMaster);
						if(travelRequestPO.getRequestDate().getTime()<travelRequest.getTripRequestEndDate().getTime()){
							if(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getApprovalProcess().equalsIgnoreCase("Yes")){
								
								List<String> approvalCount = requestDAOImpl.getListOfApprovalPendingRequestForUser(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
												travelRequest.getEfmFmUserMaster().getUserId(),"N");								
							 //normal requestCreation	
								 if((travelRequest.getReqApprovalStatus().equalsIgnoreCase("N") 
										 && travelRequest.getRequestType().equalsIgnoreCase("guest")) ||  travelRequest.getTimeFlg().equalsIgnoreCase("A")){	
									    travelRequestPO.setApproveStatus("N");	
										travelRequestPO.setReqApprovalStatus("N");
										em.merge(travelRequestPO);
								 }else if(travelRequest.getReqApprovalStatus().equalsIgnoreCase("Y")){												
									 travelRequestPO.setReqApprovalStatus("Y");
									 em.merge(travelRequestPO);
								}else if(!(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size())){							
									em.merge(travelRequestPO);		
								}else if(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size()){									
									if(approvalCount.contains(dateformate.format(travelRequestPO.getRequestDate()))){
										travelRequestPO.setApproveStatus("Y");	
										travelRequestPO.setReqApprovalStatus("N");
										em.merge(travelRequestPO);
									}else{
										travelRequestPO.setApproveStatus("N");	
										travelRequestPO.setReqApprovalStatus("N");
										em.merge(travelRequestPO);
									}									
								}else{
									travelRequestPO.setApproveStatus("N");	
									travelRequestPO.setReqApprovalStatus("N");
									em.merge(travelRequestPO);
								}					
							}else{
								em.merge(travelRequestPO);
							}						
						}
			//			em.getTransaction().commit();
			//			em.close();
					} else if (!employeeTravelRequest.get(employeeTravelRequest.size() - 1).getReadFlg()
							.equalsIgnoreCase("Y")
							&& dateFormat
									.format(employeeTravelRequest.get(employeeTravelRequest.size() - 1)
											.getRequestDate())
									.toString().equalsIgnoreCase(dateFormat.format(new Date()).toString())) {
						String weekOffs = employeeTravelRequest.get(employeeTravelRequest.size() - 1)
								.getEfmFmUserMaster().getWeekOffDays();
						String currentDay = new SimpleDateFormat("EEEE").format(currentDate);
						String requestDateTime = (new StringBuilder(String.valueOf(dateFormat
								.format(employeeTravelRequest.get(employeeTravelRequest.size() - 1).getRequestDate())
								.toString()))).append(" ").append(
										employeeTravelRequest.get(employeeTravelRequest.size() - 1).getShiftTime())
										.toString();
						Date todayDateTime = dateTimeFormate.parse(requestDateTime);
						if (travelRequest.getTripRequestEndDate().getTime() < dateFormat.parse(todaDate).getTime()) {
						}
						if (todayDateTime.getTime() > (new Date()).getTime()) {
							travelRequestPO.setRequestDate(dateFormat.parse(todaDate));
							travelRequestPO.setShiftTime(travelRequest.getShiftTime());
							travelRequestPO.setPickUpTime(travelRequest.getPickUpTime());
							travelRequestPO.setApproveStatus("Y");
							travelRequestPO.setRequestType(travelRequest.getRequestType());
							travelRequestPO.setTripType(travelRequest.getTripType());
							travelRequestPO.setRequestStatus(travelRequest.getRequestFrom());
							travelRequestPO.setLocationFlg(travelRequest.getLocationFlg());
							travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
								travelRequestPO.setTripSheetStatus("NOCHANGES");
							travelRequestPO.setLocationWaypointsIds(travelRequest.getLocationWaypointsIds());
							travelRequestPO.setReadFlg("Y");
					        travelRequestPO.setRoutingAreaCreation("Y");
							travelRequestPO.setIsActive("Y");
							travelRequestPO.setProjectId(travelRequest.getProjectId());								
							travelRequestPO.setReportingManagerUserId(travelRequest.getReportingManagerUserId());
							travelRequestPO.setReqApprovalStatus(travelRequest.getReqApprovalStatus());	
							if (!(weekOffs.contains(currentDay)) && travelRequestPO.getRequestDate().getTime()<travelRequest.getTripRequestEndDate().getTime()){
								if(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getApprovalProcess().equalsIgnoreCase("Y")){
									List<String> approvalCount = requestDAOImpl
											.getListOfApprovalPendingRequestForUser(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
													travelRequest.getEfmFmUserMaster().getUserId(),"N");					
									 if((travelRequest.getReqApprovalStatus().equalsIgnoreCase("N") 
											 && travelRequest.getRequestType().equalsIgnoreCase("guest")) ||  travelRequest.getTimeFlg().equalsIgnoreCase("A")){	
										    travelRequestPO.setApproveStatus("N");	
											travelRequestPO.setReqApprovalStatus("N");
											em.merge(travelRequestPO);
									 }else if(travelRequest.getReqApprovalStatus().equalsIgnoreCase("Y")){												
										 travelRequestPO.setReqApprovalStatus("Y");
										 em.merge(travelRequestPO);
									}else if(!(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size())){
										em.merge(travelRequestPO);
									}else if(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size()){									
										if(approvalCount.contains(dateformate.format(travelRequestPO.getRequestDate()))){
											travelRequestPO.setApproveStatus("Y");	
											travelRequestPO.setReqApprovalStatus("N");
											em.merge(travelRequestPO);
										}else{
											travelRequestPO.setApproveStatus("N");	
											travelRequestPO.setReqApprovalStatus("N");
											em.merge(travelRequestPO);
										}									
									}else{
										travelRequestPO.setApproveStatus("N");	
										travelRequestPO.setReqApprovalStatus("N");
										em.merge(travelRequestPO);
									}					
								}else{
									em.merge(travelRequestPO);
								}
								
								//em.merge(travelRequestPO);
					//		em.getTransaction().commit();
					//		em.close();
							}

						} else {
							String weekOffDays[] = employeeTravelRequest.get(employeeTravelRequest.size() - 1)
									.getEfmFmUserMaster().getWeekOffDays().split(",");
							if (!employeeTravelRequest.get(employeeTravelRequest.size() - 1).getEfmFmUserMaster()
									.getWeekOffDays().contains((new SimpleDateFormat("EEEE")).format(currentDate))) {
								if (travelRequest.getTripRequestEndDate().getTime() < dateFormat.parse(todaDate)
										.getTime()) {
									em.getTransaction().commit();
									em.close();
									return;
								}
								travelRequestPO.setRequestDate(dateFormat.parse(todaDate));
								EFmFmEmployeeRequestMasterPO employeeRequestMaster = new EFmFmEmployeeRequestMasterPO();
								employeeRequestMaster.setTripId(travelRequest.getTripId());
								travelRequestPO.setShiftTime(travelRequest.getShiftTime());
								travelRequestPO.setPickUpTime(travelRequest.getPickUpTime());
								travelRequestPO.setApproveStatus("Y");
								travelRequestPO.setRequestType(travelRequest.getRequestType());
								travelRequestPO.setTripType(travelRequest.getTripType());
								travelRequestPO.setRequestStatus(travelRequest.getRequestFrom());
								travelRequestPO.setLocationFlg(travelRequest.getLocationFlg());
								travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
								travelRequestPO.setTripSheetStatus("NOCHANGES");
								travelRequestPO.setLocationWaypointsIds(travelRequest.getLocationWaypointsIds());
								travelRequestPO.setReadFlg("Y");
						        travelRequestPO.setRoutingAreaCreation("Y");
								travelRequestPO.setIsActive("Y");
								travelRequestPO.setProjectId(travelRequest.getProjectId());								
								travelRequestPO.setReportingManagerUserId(travelRequest.getReportingManagerUserId());
								travelRequestPO.setReqApprovalStatus(travelRequest.getReqApprovalStatus());	
								travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMaster);
								if (!(weekOffs.contains(currentDay)) && 
										travelRequestPO.getRequestDate().getTime()<travelRequest.getTripRequestEndDate().getTime()){
									//em.merge(travelRequestPO);
									if(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getApprovalProcess().equalsIgnoreCase("Yes")){
										List<String> approvalCount = requestDAOImpl
												.getListOfApprovalPendingRequestForUser(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
														travelRequest.getEfmFmUserMaster().getUserId(),"N");					
										 if((travelRequest.getReqApprovalStatus().equalsIgnoreCase("N") 
												 && travelRequest.getRequestType().equalsIgnoreCase("guest")) ||  travelRequest.getTimeFlg().equalsIgnoreCase("A")){	
											    travelRequestPO.setApproveStatus("N");	
												travelRequestPO.setReqApprovalStatus("N");
												em.merge(travelRequestPO);
										 }else if(travelRequest.getReqApprovalStatus().equalsIgnoreCase("Y")){												
											 travelRequestPO.setReqApprovalStatus("Y");
											 em.merge(travelRequestPO);
										}else if(!(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size())){
											em.merge(travelRequestPO);
										}else if(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size()){									
											if(approvalCount.contains(dateformate.format(travelRequestPO.getRequestDate()))){
												travelRequestPO.setApproveStatus("Y");	
												travelRequestPO.setReqApprovalStatus("N");
												em.merge(travelRequestPO);
											}else{
												travelRequestPO.setApproveStatus("N");	
												travelRequestPO.setReqApprovalStatus("N");
												em.merge(travelRequestPO);
											}									
										}else{
											travelRequestPO.setApproveStatus("N");	
											travelRequestPO.setReqApprovalStatus("N");
											em.merge(travelRequestPO);
										}	
										
									}else{
										em.merge(travelRequestPO);
									}
							//	em.getTransaction().commit();
							//	em.close();
								}
							}
							// Check For First Off day for employee will check
							// the shift
							// drop shiftTime only.Right Now we Have 2 Night
							// Drops only
							else if (weekOffDays[0].equalsIgnoreCase((new SimpleDateFormat("EEEE")).format(currentDate))
									&& (employeeTravelRequest.get(employeeTravelRequest.size() - 1).getShiftTime()
											.getHours() == 1
											|| employeeTravelRequest.get(employeeTravelRequest.size() - 1)
													.getShiftTime().getHours() == 3)) {
								if (travelRequest.getTripRequestEndDate().getTime() < dateFormat.parse(todaDate)
										.getTime()) {
									em.getTransaction().commit();
									em.close();
									return;
								}
								travelRequestPO.setRequestDate(dateFormat.parse(todaDate));
								EFmFmEmployeeRequestMasterPO employeeRequestMaster = new EFmFmEmployeeRequestMasterPO();
								employeeRequestMaster.setTripId(travelRequest.getTripId());
								travelRequestPO.setShiftTime(travelRequest.getShiftTime());
								travelRequestPO.setPickUpTime(travelRequest.getPickUpTime());
								travelRequestPO.setApproveStatus("Y");
								travelRequestPO.setRequestType(travelRequest.getRequestType());
								travelRequestPO.setTripType(travelRequest.getTripType());
								travelRequestPO.setRequestStatus(travelRequest.getRequestFrom());
								travelRequestPO.setLocationFlg(travelRequest.getLocationFlg());
								travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
								travelRequestPO.setTripSheetStatus("NOCHANGES");
								travelRequestPO.setLocationWaypointsIds(travelRequest.getLocationWaypointsIds());
								travelRequestPO.setReadFlg("Y");
						        travelRequestPO.setRoutingAreaCreation("Y");
								travelRequestPO.setIsActive("Y");
								travelRequestPO.setProjectId(travelRequest.getProjectId());								
								travelRequestPO.setReportingManagerUserId(travelRequest.getReportingManagerUserId());
								travelRequestPO.setReqApprovalStatus(travelRequest.getReqApprovalStatus());	
								travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMaster);
								if (!(weekOffs.contains(currentDay)) && travelRequestPO.getRequestDate().getTime()<travelRequest.getTripRequestEndDate().getTime()){
									//em.merge(travelRequestPO);
									if(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getApprovalProcess().equalsIgnoreCase("Yes")){
										List<String> approvalCount = requestDAOImpl
												.getListOfApprovalPendingRequestForUser(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
														travelRequest.getEfmFmUserMaster().getUserId(),"N");					
										 if((travelRequest.getReqApprovalStatus().equalsIgnoreCase("N") 
												 && travelRequest.getRequestType().equalsIgnoreCase("guest")) ||  travelRequest.getTimeFlg().equalsIgnoreCase("A")){	
											    travelRequestPO.setApproveStatus("N");	
												travelRequestPO.setReqApprovalStatus("N");
												em.merge(travelRequestPO);
										 }else if(travelRequest.getReqApprovalStatus().equalsIgnoreCase("Y")){												
											 travelRequestPO.setReqApprovalStatus("Y");
											 em.merge(travelRequestPO);
										}else if(!(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size())){
											em.merge(travelRequestPO);
										}else if(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size()){									
											if(approvalCount.contains(dateformate.format(travelRequestPO.getRequestDate()))){
												travelRequestPO.setApproveStatus("Y");	
												travelRequestPO.setReqApprovalStatus("N");
												em.merge(travelRequestPO);
											}else{
												travelRequestPO.setApproveStatus("N");	
												travelRequestPO.setReqApprovalStatus("N");
												em.merge(travelRequestPO);
											}									
										}else{
											travelRequestPO.setApproveStatus("N");	
											travelRequestPO.setReqApprovalStatus("N");
											em.merge(travelRequestPO);
										}						
									}else{
										em.merge(travelRequestPO);
									}	
						//		em.getTransaction().commit();
						//		em.close();
								}
							}
						}
					}
//					em.getTransaction().commit();
//					em.close();
					long l2 = System.currentTimeMillis();
					log.debug("Creating the request from request master table : " + (l2 - l1) + "ms for Employee: "
							+ travelRequest.getEfmFmUserMaster().getEmployeeId());
					

				}
			}
			em.getTransaction().commit();
			em.close();
		}
		} catch (Exception e) {
			log.info("TripId"+travelRequest.getTripId());
			log.info("Error" + e);
		}
	}

	public void disableActiveRequests(EFmFmEmployeeRequestMasterPO travelRequest)
			throws ParseException, IOException, Exception {
		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}
		long l1 = System.currentTimeMillis();
		travelRequest.setStatus("N");
		travelRequest.setReadFlg("N");
		em.merge(travelRequest);
		long l2 = System.currentTimeMillis();
		em.getTransaction().commit();
		em.close();
		log.debug("Inside Disable Active Request from request master Fucntion Execution Time: " + (l2 - l1) + "ms");
	}

	public void disableRequestsFromTravelRequest(EFmFmEmployeeTravelRequestPO travelRequestPO)
			throws ParseException, IOException, Exception {
		long l1 = System.currentTimeMillis();
		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}
		SchedulerCabRequestDAOImpl requestDAOImpl = new SchedulerCabRequestDAOImpl(em);

		List<EFmFmEmployeeTravelRequestPO> employeeTravelRequest = requestDAOImpl.disableBackDateRequest(travelRequestPO.getRequestDate(),
				travelRequestPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(), travelRequestPO.getEfmFmUserMaster().getUserId());
		if(!(employeeTravelRequest.isEmpty())){
			for(EFmFmEmployeeTravelRequestPO request:employeeTravelRequest){
				request.setReadFlg("N");
				request.setIsActive("Y");
				request.setRequestStatus("C");
			   em.merge(request);
			}
		}
		
		if(travelRequestPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getApprovalProcess().equalsIgnoreCase("Yes")){
			try {				
				List<String> approvalCount = requestDAOImpl.getListOfApprovalPendingRequestForUserByDate(travelRequestPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
						travelRequestPO.getEfmFmUserMaster().getUserId(),travelRequestPO.getRequestDate());					
				 if(!((travelRequestPO.getReqApprovalStatus().equalsIgnoreCase("N") 
						 && travelRequestPO.getRequestType().equalsIgnoreCase("guest")) 
						 || travelRequestPO.geteFmFmEmployeeRequestMaster().getTimeFlg().equalsIgnoreCase("A"))){			 
					    if(!(travelRequestPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size())){						    	
					    	int reqCount=travelRequestPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval()- approvalCount.size();
					    	List<EFmFmEmployeeTravelRequestPO> listOfDate = requestDAOImpl.getListOfApprovalPendingApprovalForUserByDate(travelRequestPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
											travelRequestPO.getEfmFmUserMaster().getUserId(),reqCount);
					    	if(!(listOfDate.isEmpty())){					    		
					    		for(EFmFmEmployeeTravelRequestPO listOfRequesDate:listOfDate){					    			
					    		 	List<EFmFmEmployeeTravelRequestPO> listofRequest = requestDAOImpl.getListOfApprovalPendingApprovalForUserRequest(listOfRequesDate.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
					    		 			listOfRequesDate.getEfmFmUserMaster().getUserId(),listOfRequesDate.getRequestDate());
					    		 	if(!(listofRequest.isEmpty())){
							    		for(EFmFmEmployeeTravelRequestPO listofRequestList:listofRequest){							    			
							    			listofRequestList.setApproveStatus("Y");	
							    			listofRequestList.setReqApprovalStatus("N");
											em.merge(listofRequestList);
							    		}
					    		 	} 
					    		 	
					    		}					    		
					    	}							
						}
				 }
				
			} catch (Exception e) {
				log.debug("EnableRequest");
				e.printStackTrace();
			}			 
		}	
		travelRequestPO.setReadFlg("N");
		travelRequestPO.setIsActive("Y");
		travelRequestPO.setRequestStatus("C");
		em.merge(travelRequestPO);
		long l2 = System.currentTimeMillis();
		em.getTransaction().commit();
		em.close();
		log.debug("disableTravelRequest Execution Time: " + (l2 - l1) + "ms");
	}

	public void sendingAutoMsgOnDeviceStop(final EFmFmEmployeeTripDetailPO eFmFmEmployeeTripDetailPO)
			throws ParseException, IOException, Exception {
		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}
		long l1 = System.currentTimeMillis();
		SchedulerVehicleCheckInDAOImpl vehicleCheckIn = new SchedulerVehicleCheckInDAOImpl(em);
		List<EFmFmLiveRoutTravelledPO> actualRoutTravelledPO=vehicleCheckIn.getRouteLastEtaAndDistanceFromAssignRouteId(eFmFmEmployeeTripDetailPO.getEfmFmAssignRoute().getAssignRouteId());		
		if (!actualRoutTravelledPO.isEmpty()) {
			DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
			DateFormat dateTimeformate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date currentDate = new Date();
			String currentDateStr = dateformate.format(currentDate) + " "
					+ eFmFmEmployeeTripDetailPO.geteFmFmEmployeeTravelRequest().getPickUpTime();
			long pickupTime = dateTimeformate.parse(currentDateStr).getTime();
			log.info("currentDate: " + currentDateStr + "currenttime: " + pickupTime);
			int lastEta = eFmFmEmployeeTripDetailPO.getGoogleEta();
			// 15 minute ETA meesage
			int lastEtaInMs = lastEta * 1000;
			long currentTime = System.currentTimeMillis();
			long gracePeriodForMessage = 120000;
			// 15 min - 2 minute in msec, in case we get it exactly at 15min.
			long timeCalculationFor15Min = ((actualRoutTravelledPO.get(actualRoutTravelledPO.size() - 1)
					.getLiveTravelledTime().getTime()) + (lastEtaInMs)) - (900000 - gracePeriodForMessage);
			// Cab Arrived Message + 2 min more - in case Device reaches and
			// triggers before automatic Arrived message
			long timeCalculationForArrived = ((actualRoutTravelledPO.get(actualRoutTravelledPO.size() - 1)
					.getLiveTravelledTime().getTime()) + (lastEtaInMs)) + gracePeriodForMessage;
			if (eFmFmEmployeeTripDetailPO.getReachedFlg().equalsIgnoreCase("N")
					&& eFmFmEmployeeTripDetailPO.getTenMinuteMessageStatus().equalsIgnoreCase("N")
					&& (currentTime >= timeCalculationFor15Min)) {

			}
			if (eFmFmEmployeeTripDetailPO.getReachedFlg().equalsIgnoreCase("N")
					&& (currentTime >= timeCalculationForArrived)) {
			}
			// Cab Has Left
			long timeMax = Math.max(eFmFmEmployeeTripDetailPO.getCabRecheddestinationTime(), pickupTime);
			// timeMax = Math.max(timeCalculationForArrived, timeMax);
			log.info("schedular service");
			if (eFmFmEmployeeTripDetailPO.getReachedFlg().equalsIgnoreCase("Y")
					&& !(eFmFmEmployeeTripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType()
							.equalsIgnoreCase("guest"))
					&& currentTime >= (timeMax + (eFmFmEmployeeTripDetailPO.getEfmFmAssignRoute()
							.geteFmFmClientBranchPO().getEmployeeWaitingTime() * 60 * 1000))) {
				log.info("Sending Cab Left  Message for Trip for sch: " + eFmFmEmployeeTripDetailPO.getEmpTripId());
				Thread thread1 = new Thread(new Runnable() {
					@Override
					synchronized public void run() {
						try {
							long l1 = System.currentTimeMillis();
							String text = "";
							text = "Sorry you missed us.\nYour ride "
									+ eFmFmEmployeeTripDetailPO.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
											.getEfmFmVehicleMaster().getVehicleNumber()
									+ " has left your " + eFmFmEmployeeTripDetailPO.getEfmFmAssignRoute().getTripType()
									+ " point.\nFor feedback write to us @" + eFmFmEmployeeTripDetailPO
											.getEfmFmAssignRoute().geteFmFmClientBranchPO().getFeedBackEmailId();
							messaging
									.cabHasLeftMessageForSch(
											new String(
													Base64.getDecoder()
															.decode(String.valueOf(eFmFmEmployeeTripDetailPO
																	.geteFmFmEmployeeTravelRequest()
																	.getEfmFmUserMaster().getMobileNumber())),
													"utf-8"),
											text,
											eFmFmEmployeeTripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType());
							long l2 = System.currentTimeMillis();
							log.debug(
									"Time Taken by no show Message from gate way: " + (l2 - l1)
											+ "ms for mobile number "
											+ (new String(
													Base64.getDecoder()
															.decode(String.valueOf(eFmFmEmployeeTripDetailPO
																	.geteFmFmEmployeeTravelRequest()
																	.getEfmFmUserMaster().getMobileNumber())),
											"utf-8")));
							Thread.currentThread().stop();
						} catch (Exception e) {
							log.error("Time Taken by no show Message from gate way: "
									+ eFmFmEmployeeTripDetailPO.getEmpTripId(), e);
							Thread.currentThread().stop();
						}
					}
				});
				thread1.start();
				eFmFmEmployeeTripDetailPO.setBoardedFlg("NO");
				eFmFmEmployeeTripDetailPO.setEmployeeStatus("completed");
				eFmFmEmployeeTripDetailPO.setCabstartFromDestination(currentTime);
				em.merge(eFmFmEmployeeTripDetailPO);
			}
			String currentPickUpTime = currentDateStr + " "
					+ eFmFmEmployeeTripDetailPO.geteFmFmEmployeeTravelRequest().getPickUpTime();
			long newPickupTime = dateTimeformate.parse(currentPickUpTime).getTime();
			// 5minutes delay time
			if (eFmFmEmployeeTripDetailPO.getReachedFlg().equalsIgnoreCase("N")
					&& eFmFmEmployeeTripDetailPO.getCabDelayMsgStatus().equalsIgnoreCase("N")
					&& currentTime >= (newPickupTime + (eFmFmEmployeeTripDetailPO.getEfmFmAssignRoute()
							.geteFmFmClientBranchPO().getDelayMessageTime() * 60 * 1000))) {
				log.info("Sending Cab running behind schedule for Trip: " + eFmFmEmployeeTripDetailPO.getEmpTripId());
				Thread thread1 = new Thread(new Runnable() {
					@Override
					synchronized public void run() {
						try {
							long l1 = System.currentTimeMillis();
							String text = "";
							if (eFmFmEmployeeTripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType()
									.equalsIgnoreCase("guest")) {

							} else {

								text = "Your ride "
										+ eFmFmEmployeeTripDetailPO.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
												.getEfmFmVehicleMaster().getVehicleNumber()
										+ " is running behind schedule and has been delayed.\nRegret the inconvenience caused if any.\nFor feedback write to us @"
										+ eFmFmEmployeeTripDetailPO.getEfmFmAssignRoute().geteFmFmClientBranchPO()
												.getFeedBackEmailId();
								messaging
										.cabHasLeftMessageForSch(
												new String(
														Base64.getDecoder()
																.decode(String.valueOf(eFmFmEmployeeTripDetailPO
																		.geteFmFmEmployeeTravelRequest()
																		.getEfmFmUserMaster().getMobileNumber())),
										"utf-8"), text,
										eFmFmEmployeeTripDetailPO.geteFmFmEmployeeTravelRequest().getRequestType());
							}
							long l2 = System.currentTimeMillis();
							log.debug("Time Taken by delay Message from gate way: " + (l2 - l1)
									+ "ms for mobile number " + eFmFmEmployeeTripDetailPO
											.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber());
							Thread.currentThread().stop();
						} catch (Exception e) {
							log.error("Error Sending Cas delay  Message for Trip: "
									+ eFmFmEmployeeTripDetailPO.getEmpTripId(), e);
							Thread.currentThread().stop();
						}
					}
				});
				thread1.start();
				eFmFmEmployeeTripDetailPO.setCabDelayMsgStatus("Y");
				eFmFmEmployeeTripDetailPO.setCabDelayMsgDeliveryDate(new Date());
				em.merge(eFmFmEmployeeTripDetailPO);
			}
		}
		long l2 = System.currentTimeMillis();
		em.getTransaction().commit();
		em.close();
		log.debug("Inside the Message sending code of schedular: " + (l2 - l1) + "ms EmpTripId"
				+ eFmFmEmployeeTripDetailPO.getEmpTripId());
	}

	public void drivetAutoCheckout(EFmFmVehicleCheckInPO driverCheckIn)
			throws ParseException, IOException, InterruptedException {
		log.info("Inside Driver CheckOut Function");
		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}
		driverCheckIn.setCheckOutTime(new Date());
		driverCheckIn.setStatus("N");
		driverCheckIn.setReadFlg("N");
		driverCheckIn.setCheckOutRemarks("Auto checkOut");
		em.merge(driverCheckIn);
		em.getTransaction().commit();
		em.close();

	}

	public void sendingMailToAdmin(final EFmFmVehicleCheckInPO driverCheckIn)
			throws ParseException, IOException, InterruptedException {
		log.info("Inside Driver CheckOut Function");
		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}
		log.info("Inside Driver CheckOut Function2");
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				String text = "Dear SuperAdmin,\nPlease find checkIn drivers details after "
						+ ((driverCheckIn.getEfmFmDriverMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO()
								.getDriverAutoCheckedoutTime().getHours()+2)+":"+(driverCheckIn.getEfmFmDriverMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO()
								.getDriverAutoCheckedoutTime().getMinutes()))
						+ " hours\nDriver Name:- " + driverCheckIn.getEfmFmDriverMaster().getFirstName()
						+ "\nVehicle Num: " + driverCheckIn.getEfmFmVehicleMaster().getVehicleNumber()
						+ "\nFor feedback write to us @" + driverCheckIn.getEfmFmDriverMaster().getEfmFmVendorMaster()
								.geteFmFmClientBranchPO().getFeedBackEmailId();
				try {
					messaging.cabReachedMessageForSch(driverCheckIn.getEfmFmDriverMaster().getEfmFmVendorMaster()
							.geteFmFmClientBranchPO().getShiftTimePlusTwoHourrAfterSMSContact(), text, "no");
					log.info("checking  Message Sent to disney");

				} catch (Exception e) {
					log.info("check Message Sent to disney  Exception" + e);

				}

			}
		});
		thread1.start();
		driverCheckIn.setAdminMailTriggerStatus(true);
		driverCheckIn.setAdminMailTriggerTime(new Date());
		em.merge(driverCheckIn);
		em.getTransaction().commit();
		em.close();

	}

	public void sendingMailToSuperVisor(final EFmFmVehicleCheckInPO driverCheckIn)
			throws ParseException, IOException, InterruptedException {
		log.info("Inside Driver CheckOut Function");
		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}
		log.info("Inside Driver CheckOut Function2");
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {

				String text = "Dear Admin,\nPlease find checkIn drivers details after "
						+ ((driverCheckIn.getEfmFmDriverMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO()
								.getDriverAutoCheckedoutTime().getHours()+1)+":"+(driverCheckIn.getEfmFmDriverMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO()
								.getDriverAutoCheckedoutTime().getMinutes()))
						+ " hours\nDriver Name:- " + driverCheckIn.getEfmFmDriverMaster().getFirstName()
						+ "\nVehicle Num: " + driverCheckIn.getEfmFmVehicleMaster().getVehicleNumber()
						+ "\nFor feedback write to us @" + driverCheckIn.getEfmFmDriverMaster().getEfmFmVendorMaster()
								.geteFmFmClientBranchPO().getFeedBackEmailId();
				try {
					messaging.cabReachedMessageForSch(driverCheckIn.getEfmFmDriverMaster().getEfmFmVendorMaster()
							.geteFmFmClientBranchPO().getShiftTimePlusOneHourrAfterSMSContact(), text, "no");
					log.info("checking  Message Sent to sujata");

				} catch (Exception e) {
					log.info("check Message Sent to sujata  Exception" + e);

				}
			}

		});
		thread1.start();
		driverCheckIn.setSupervisorMailTriggerStatus(true);
		driverCheckIn.setSupervisorMailTriggerTime(new Date());
		em.merge(driverCheckIn);
		em.getTransaction().commit();
		em.close();

	}

	public void routePlanning(EFmFmAssignRoutePO assignRoute) throws ParseException, IOException, InterruptedException {
		log.info("Inside routePlanning Function");
		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}
		log.info("Inside routePlanning calculating planned ETA and distance");
		SchedulerCabRequestDAOImpl requestDAOImpl = new SchedulerCabRequestDAOImpl(em);
		List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
		if (assignRoute.getTripType().equalsIgnoreCase("PICKUP")) {
			employeeTripDetailPO = requestDAOImpl.getParticularTripAllEmployees(assignRoute.getAssignRouteId());
		} else {
			employeeTripDetailPO = requestDAOImpl.getDropTripAllSortedEmployees(assignRoute.getAssignRouteId());
		}

		StringBuffer empWayPoints = new StringBuffer();
		CalculateDistance calculateDistance = new CalculateDistance();
        if(assignRoute.getRouteGenerationType().contains("nodalAdhoc")){
        	empWayPoints.append(assignRoute.getNodalPoints()); 
        }                                                       
        else{
        	if (!(employeeTripDetailPO.isEmpty())) {
                for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
                	String wayPointsAdhocRequest="";
                	if(assignRoute.getRouteGenerationType().contains("nodal")){
                		log.info("EmployeeId Id"+employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
                		if(!(empWayPoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()))){
                        	empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()+"|");
                		}
					}
                	else if (assignRoute.getRouteGenerationType()
							.equalsIgnoreCase("AdhocRequest")) {
                		log.info("EmployeeId Id"+employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
                		try{
                		empWayPoints.append(wayPointsAdhocRequest+employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
								.getEndDestinationAddressLattitudeLongitude()+"|");	
                		}
                		catch(Exception e){
                			log.info("AdhocRequest LattiLongi"+e);
                		}
                    }
                	else{
                		log.info("EmployeeId Id"+employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
                    empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
                            .getEfmFmUserMaster().getLatitudeLongitude() + "|");
                	}
                }

            }
		
        }
		String plannedETAAndDistance = calculateDistance.getPlannedDistanceaAndETAForRoute(
				assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude(),
				assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude(), empWayPoints.toString().replaceAll("\\s+",""));	
		log.info(empWayPoints.toString().replaceAll("\\s+","")+"assignRoute Id"+assignRoute.getAssignRouteId());
        assignRoute.setPlannedTravelledDistance(Math.round(Double.parseDouble(plannedETAAndDistance.split("-")[0])+(assignRoute.geteFmFmClientBranchPO().getAddingGeoFenceDistanceIntrip())));
        assignRoute.setPlannedDistance(Math.round(Double.parseDouble(plannedETAAndDistance.split("-")[0])+(assignRoute.geteFmFmClientBranchPO().getAddingGeoFenceDistanceIntrip())));
        assignRoute.setPlannedTime(Math.round(Long.parseLong(plannedETAAndDistance.split("-")[1])));		
		if(Double.parseDouble(plannedETAAndDistance.split("-")[0])+(assignRoute.geteFmFmClientBranchPO().getAddingGeoFenceDistanceIntrip())!=0)
        assignRoute.setPlannedReadFlg("N");		
//	    assignRoute.setScheduleReadFlg("N");
		em.merge(assignRoute);
		em.getTransaction().commit();
		em.close();

	}
	
	
	
	
	
	public void distance(EFmFmAssignRoutePO assignRoute) throws ParseException, IOException, InterruptedException {
		log.info("Inside routePlanning Function");
		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}
		log.info("Inside routePlanning calculating planned ETA and distance");
		SchedulerCabRequestDAOImpl requestDAOImpl = new SchedulerCabRequestDAOImpl(em);
		List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
		if (assignRoute.getTripType().equalsIgnoreCase("PICKUP")) {
			employeeTripDetailPO = requestDAOImpl.getParticularTripAllEmployees(assignRoute.getAssignRouteId());
		} else {
			employeeTripDetailPO = requestDAOImpl.getDropTripAllSortedEmployees(assignRoute.getAssignRouteId());
		}

		StringBuffer empWayPoints = new StringBuffer();
		
		CalculateDistance calculateDistance = new CalculateDistance();
        if(assignRoute.getRouteGenerationType().contains("nodalAdhoc")){
        	empWayPoints.append(assignRoute.getNodalPoints()); 
        }                                                       
        else{
        	if (!(employeeTripDetailPO.isEmpty())) {
                for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
                	String wayPointsAdhocRequest="";
                	if(assignRoute.getRouteGenerationType().contains("nodal")){
                		log.info("EmployeeId Id"+employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
                		if(!(empWayPoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()))){
                        	empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()+"|");
                		}
					}
                	else if (assignRoute.getRouteGenerationType()
							.equalsIgnoreCase("AdhocRequest")) {
                		log.info("EmployeeId Id"+employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
                		try{
                		empWayPoints.append(wayPointsAdhocRequest+employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
								.getEndDestinationAddressLattitudeLongitude()+"|");	
                		}
                		catch(Exception e){
                			log.info("AdhocRequest LattiLongi"+e);
                		}
                    }
                	else{
                		log.info("EmployeeId Id"+employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
                    empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
                            .getEfmFmUserMaster().getLatitudeLongitude() + "|");
                	}
                }

            }
		
        }
		String plannedETAAndDistance = calculateDistance.getPlannedDistanceaAndETAForRoute(
				assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude(),
				assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude(), empWayPoints.toString().replaceAll("\\s+",""));	
		log.info(empWayPoints.toString().replaceAll("\\s+","")+"assignRoute Id"+assignRoute.getAssignRouteId());
        assignRoute.setPlannedTravelledDistance(Math.round(Double.parseDouble(plannedETAAndDistance.split("-")[0])+(assignRoute.geteFmFmClientBranchPO().getAddingGeoFenceDistanceIntrip())));
        assignRoute.setPlannedDistance(Math.round(Double.parseDouble(plannedETAAndDistance.split("-")[0])+(assignRoute.geteFmFmClientBranchPO().getAddingGeoFenceDistanceIntrip())));
        assignRoute.setPlannedTime(Math.round(Long.parseLong(plannedETAAndDistance.split("-")[1])));		
		if(Double.parseDouble(plannedETAAndDistance.split("-")[0])+(assignRoute.geteFmFmClientBranchPO().getAddingGeoFenceDistanceIntrip())!=0)
        assignRoute.setPlannedReadFlg("N");		
//	    assignRoute.setScheduleReadFlg("N");
		em.merge(assignRoute);
		em.getTransaction().commit();
		em.close();

	}

	
	public void updateVehicleDistanceInDiffCasesEarlyStartAndComplete(int checkInId) throws ParseException, IOException,
			InterruptedException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}
		SchedulerVehicleCheckInDAOImpl vehicleDAOImpl = new SchedulerVehicleCheckInDAOImpl(em);
		List<EFmFmAssignRoutePO> assignList = vehicleDAOImpl.getCompletedTripDetail(checkInId);
		
		if ((!(assignList.isEmpty()) && assignList.get(0).getTripType().equalsIgnoreCase("DROP"))) {
			// Get First entry from travelled route table ASC.
			List<EFmFmActualRoutTravelledPO> actualRouteFirstDetails = vehicleDAOImpl
					.getFirstEntryFromActualRouteByAssignRouteId(assignList.get(0).getAssignRouteId());
			if (!(actualRouteFirstDetails.isEmpty())) {
				log.error("travelledId" + actualRouteFirstDetails.get(0).getTravelId());
				double totalDistance = Geocode.distance(
						new Geocode(assignList.get(0).geteFmFmClientBranchPO().getLatitudeLongitude()),
						new Geocode(actualRouteFirstDetails.get(0).getLatitudeLongitude()));
				log.info("Distance ..." + totalDistance);
				if (totalDistance > 1000) {
					List<EFmFmEmployeeTripDetailPO> routeTripDetails = vehicleDAOImpl
							.getFirstDropEmployeeDetail(assignList.get(0).getAssignRouteId());
					double tripDis = 0;
					CalculateDistance distance = new CalculateDistance();
					StringBuffer empWayPoints = new StringBuffer();
					String wayPointsAdhocRequest = "";
					if (!(routeTripDetails.isEmpty())) {
						for (EFmFmEmployeeTripDetailPO employeeTripDetail : routeTripDetails) {
							if (assignList.get(0).getRouteGenerationType().contains("nodal")
									&& (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("D"))
									&& (employeeTripDetail.getPickedUpDateAndTime() < actualRouteFirstDetails.get(0)
											.getTravelledTime().getTime())) {
								log.info("EmployeeId Id" + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getEmployeeId());
								if (!(empWayPoints.toString()
										.contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster()
												.getNodalPoints()))) {
									empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()
											+ "|");
								}
							} 
							if (assignList.get(0).getRouteGenerationType().contains("nodal")
									&& (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO"))
									&& (employeeTripDetail.getCabstartFromDestination() < actualRouteFirstDetails.get(0)
											.getTravelledTime().getTime())) {
								log.info("EmployeeId Id" + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getEmployeeId());
								if (!(empWayPoints.toString()
										.contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster()
												.getNodalPoints()))) {
									empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()
											+ "|");
								}
							}
							
							else if (assignList.get(0).getRouteGenerationType().equalsIgnoreCase("AdhocRequest")
									&& (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("D"))
									&& (employeeTripDetail.getPickedUpDateAndTime() < actualRouteFirstDetails.get(0)
											.getTravelledTime().getTime())) {
								log.info("EmployeeId Id" + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getEmployeeId());
								try {
									empWayPoints.append(wayPointsAdhocRequest + employeeTripDetail
											.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getEndDestinationAddressLattitudeLongitude() + "|");
								} catch (Exception e) {
									log.info("AdhocRequest LattiLongi" + e);
								}
							} 
							else if (assignList.get(0).getRouteGenerationType().equalsIgnoreCase("AdhocRequest")
									&& (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO"))
									&& (employeeTripDetail.getCabstartFromDestination() < actualRouteFirstDetails.get(0)
											.getTravelledTime().getTime())) {
								log.info("EmployeeId Id" + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getEmployeeId());
								try {
									empWayPoints.append(wayPointsAdhocRequest + employeeTripDetail
											.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getEndDestinationAddressLattitudeLongitude() + "|");
								} catch (Exception e) {
									log.info("AdhocRequest LattiLongi" + e);
								}
							}
							
							else if ((employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO")) &&
									employeeTripDetail.getCabstartFromDestination() < actualRouteFirstDetails.get(0)
									.getTravelledTime().getTime()) {
								log.info("EmployeeId Id" + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getEmployeeId());
								empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getLatitudeLongitude() + "|");
							}
							
							else if ((employeeTripDetail.getBoardedFlg().equalsIgnoreCase("D")) &&
									employeeTripDetail.getPickedUpDateAndTime() < actualRouteFirstDetails.get(0)
									.getTravelledTime().getTime()) {
								log.info("EmployeeId Id" + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getEmployeeId());
								empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getLatitudeLongitude() + "|");
							}
						}

					}
					if (empWayPoints.length() == 0) {
						tripDis = distance.employeeDistanceCalculation(
								assignList.get(0).geteFmFmClientBranchPO().getLatitudeLongitude(),
								actualRouteFirstDetails.get(0).getLatitudeLongitude());
					} else {
						String plannedETAAndDistance = distance.getPlannedDistanceaAndETAForRoute(
								assignList.get(0).geteFmFmClientBranchPO().getLatitudeLongitude(),
								actualRouteFirstDetails.get(0).getLatitudeLongitude(),
								empWayPoints.toString().replaceAll("\\s+", ""));
						tripDis = Double.parseDouble(plannedETAAndDistance.split("-")[0]);
					}
					log.info(empWayPoints.toString().replaceAll("\\s+", "") + "assignRoute Id"
							+ assignList.get(0).getAssignRouteId());
					EFmFmVehicleMasterPO vehicleList = vehicleDAOImpl.getParticularVehicleDetail(
							assignList.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
					vehicleList.setMonthlyPendingFixedDistance(vehicleList.getMonthlyPendingFixedDistance() - tripDis);
					assignList.get(0).setDistanceUpdationFlg("LateTripStart");
					assignList.get(0).setTravelledDistance(assignList.get(0).getTravelledDistance() + tripDis);

					if(!(assignList.get(0).geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP"))){
					assignList.get(0)
							.setPlannedTravelledDistance(assignList.get(0).getPlannedTravelledDistance() + tripDis);
					}
					
					em.merge(assignList.get(0));
					em.merge(vehicleList);
					em.getTransaction().commit();
					em.close();
				} else {
					assignList.get(0).setDistanceUpdationFlg("StartOnTime");
					em.merge(assignList.get(0));
					em.getTransaction().commit();
					em.close();

				}
			} else {
				assignList.get(0).setDistanceUpdationFlg("NoDevice");
				em.merge(assignList.get(0));
				em.getTransaction().commit();
				em.close();
			}

		}
		if ((!(assignList.isEmpty()) && assignList.get(0).getTripType().equalsIgnoreCase("PICKUP"))) {
			// Get Last entry from travelled route table DESC.
			List<EFmFmActualRoutTravelledPO> actualRouteLastEntry = vehicleDAOImpl
					.getLastEntryFromActualRouteByAssignRouteId(assignList.get(0).getAssignRouteId());
			if (!(actualRouteLastEntry.isEmpty())) {
				log.error("travelledId" + actualRouteLastEntry.get(0).getTravelId());
				double totalDistance = Geocode.distance(
						new Geocode(assignList.get(0).geteFmFmClientBranchPO().getLatitudeLongitude()),
						new Geocode(actualRouteLastEntry.get(0).getLatitudeLongitude()));
				log.info("Distance ..." + totalDistance);
				if (totalDistance > 1000) {
					CalculateDistance distance = new CalculateDistance();
					List<EFmFmEmployeeTripDetailPO> routeTripDetails = vehicleDAOImpl
							.getFirstPickEmployeeDetail(assignList.get(0).getAssignRouteId());
					double tripDis = 0;
					StringBuffer empWayPoints = new StringBuffer();
					String wayPointsAdhocRequest = "";
					log.info("Route Id"+assignList.get(0).getAssignRouteId());
					if(assignList.get(0).getAssignRouteId()==34561){
						System.out.println("ll");
					}
					if (!(routeTripDetails.isEmpty())) {
						for (EFmFmEmployeeTripDetailPO employeeTripDetail : routeTripDetails) {
							if (assignList.get(0).getRouteGenerationType().contains("nodal")
									&& (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("B"))
									&& (employeeTripDetail.getPickedUpDateAndTime() > actualRouteLastEntry.get(0)
											.getTravelledTime().getTime())) {
								log.info("EmployeeId Id" + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getEmployeeId());
								if (!(empWayPoints.toString()
										.contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster()
												.getNodalPoints()))) {
									empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()
											+ "|");
								}
							} else if (assignList.get(0).getRouteGenerationType().contains("nodal")
									&& (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO"))
									&& (employeeTripDetail.getCabstartFromDestination() > actualRouteLastEntry.get(0)
											.getTravelledTime().getTime())) {
								log.info("EmployeeId Id" + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getEmployeeId());
								if (!(empWayPoints.toString()
										.contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster()
												.getNodalPoints()))) {
									empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
											.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()
											+ "|");
								}
							}

							else if (assignList.get(0).getRouteGenerationType().equalsIgnoreCase("AdhocRequest")
									&& (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("B"))
									&& (employeeTripDetail.getPickedUpDateAndTime() > actualRouteLastEntry.get(0)
											.getTravelledTime().getTime())) {
								log.info("EmployeeId Id" + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getEmployeeId());
								try {
									empWayPoints.append(wayPointsAdhocRequest + employeeTripDetail
											.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getEndDestinationAddressLattitudeLongitude() + "|");
								} catch (Exception e) {
									log.info("AdhocRequest error boarded" + e);
								}
							}

							else if (assignList.get(0).getRouteGenerationType().equalsIgnoreCase("AdhocRequest")
									&& (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO"))
									&& (employeeTripDetail.getCabstartFromDestination() > actualRouteLastEntry.get(0)
											.getTravelledTime().getTime())) {
								log.info("EmployeeId Id" + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getEmployeeId());
								try {
									empWayPoints.append(wayPointsAdhocRequest + employeeTripDetail
											.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getEndDestinationAddressLattitudeLongitude() + "|");
								} catch (Exception e) {
									log.info("AdhocRequest error noshow" + e);
								}
							}

							else if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("B")
									&& (employeeTripDetail.getPickedUpDateAndTime() > actualRouteLastEntry.get(0)
											.getTravelledTime().getTime())) {
								log.info("EmployeeId Id" + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getEmployeeId());
								empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getLatitudeLongitude() + "|");
							}

							else if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO")
									&& (employeeTripDetail.getCabstartFromDestination() > actualRouteLastEntry.get(0)
											.getTravelledTime().getTime())) {
								log.info("EmployeeId Id" + employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getEmployeeId());
								empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getLatitudeLongitude() + "|");
							}
						}

					}
					if (empWayPoints.length() == 0) {
						tripDis = distance.employeeDistanceCalculation(
								actualRouteLastEntry.get(0).getLatitudeLongitude(),
								assignList.get(0).geteFmFmClientBranchPO().getLatitudeLongitude());
					} else {
						String plannedETAAndDistance = distance.getPlannedDistanceaAndETAForRoute(
								actualRouteLastEntry.get(0).getLatitudeLongitude(),
								assignList.get(0).geteFmFmClientBranchPO().getLatitudeLongitude(),
								empWayPoints.toString().replaceAll("\\s+", ""));
						tripDis = Double.parseDouble(plannedETAAndDistance.split("-")[0]);
					}
					log.info(empWayPoints.toString().replaceAll("\\s+", "") + "assignRoute Id"
							+ assignList.get(0).getAssignRouteId());
					EFmFmVehicleMasterPO vehicleList = vehicleDAOImpl.getParticularVehicleDetail(
							assignList.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
					vehicleList.setMonthlyPendingFixedDistance(vehicleList.getMonthlyPendingFixedDistance() - tripDis);
					assignList.get(0).setDistanceUpdationFlg("EarlyTripComplete");
					assignList.get(0).setTravelledDistance(assignList.get(0).getTravelledDistance() + tripDis);

					if(!(assignList.get(0).geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP"))){
						assignList.get(0)
								.setPlannedTravelledDistance(assignList.get(0).getPlannedTravelledDistance() + tripDis);
						}
					em.merge(assignList.get(0));
					em.merge(vehicleList);
					em.getTransaction().commit();
					em.close();
				} else {
					assignList.get(0).setDistanceUpdationFlg("StartOnTime");
					em.merge(assignList.get(0));
					em.getTransaction().commit();
					em.close();

				}
			} else {
				assignList.get(0).setDistanceUpdationFlg("NoDevice");
				em.merge(assignList.get(0));
				em.getTransaction().commit();
				em.close();
			}
		}
	}
	

	public void cabScheduling(EFmFmAssignRoutePO assignRoute) throws ParseException, IOException, InterruptedException {
		log.info("Inside cabScheduling Function");
		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}
		SchedulerVehicleCheckInDAOImpl vehicleDAOImpl = new SchedulerVehicleCheckInDAOImpl(em);

		log.info("Inside cabScheduling calculating planned ETA and distance");
		SchedulerCabRequestDAOImpl requestDAOImpl = new SchedulerCabRequestDAOImpl(em);
		try{
	//		SchedulerCabRequestDAOImpl employeeList = new SchedulerCabRequestDAOImpl(em);
			
//			if(!(routeDetails.isEmpty()) && ){
//				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
				List<EFmFmVehicleCheckInPO> allCheckInVehicles = null;

//				if (assignRoute.getTripType().equalsIgnoreCase("PICKUP")) {
//					employeeTripDetailPO = employeeList.getParticularTripAllEmployees(assignRoute.getAssignRouteId());
//				} else {
//					employeeTripDetailPO = employeeList.getDropTripAllSortedEmployees(assignRoute.getAssignRouteId());
//				}

				// Get CheckedIn vehicles with Zero trips and pending contract
				// KM is
				// more give him longest route first.
			
				
				
//				if (employeeTripDetailPO.size() < 6) {
					allCheckInVehicles = vehicleDAOImpl.getAllCheckedInVehiclesForSpecificCapacity(assignRoute.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity(),0);
					if (!(allCheckInVehicles.isEmpty())) {		
						double monthlyPendingFixedDistance=Math.round(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getMonthlyPendingFixedDistance()-assignRoute.getPlannedTravelledDistance());
						requestDAOImpl.updateVehicleCheckInDetailsWithOutStatus(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId(),monthlyPendingFixedDistance,"allocated");		
						
						EFmFmDriverMasterPO particularDriverDetails = requestDAOImpl.getParticularDriverDetail(
								allCheckInVehicles.get(0).getEfmFmDriverMaster().getDriverId());
						particularDriverDetails.setStatus("allocated");
						em.merge(particularDriverDetails);
						
						List<EFmFmDeviceMasterPO> deviceDetails = requestDAOImpl.getDeviceDetailsFromDeviceId(
								allCheckInVehicles.get(0).geteFmFmDeviceMaster().getDeviceId(),
								assignRoute.geteFmFmClientBranchPO().getBranchId());
						deviceDetails.get(0).setStatus("allocated");
						em.merge(deviceDetails.get(0));
						
						allCheckInVehicles.get(0).setStatus("N");
						allCheckInVehicles.get(0).setTotalTravelDistance(allCheckInVehicles.get(0).getTotalTravelDistance()+assignRoute.getPlannedTravelledDistance());    
						allCheckInVehicles.get(0).setTotalTravelTime(allCheckInVehicles.get(0).getTotalTravelTime()+assignRoute.getPlannedTime());	
						allCheckInVehicles.get(0).setNumberOfTrips(allCheckInVehicles.get(0).getNumberOfTrips()+1);
						
						List<EFmFmVehicleCheckInPO> oldCheckInVehicleDetail=requestDAOImpl.getParticularCheckedInVehicleDetails(assignRoute.getEfmFmVehicleCheckIn().getCheckInId());
						if(!(oldCheckInVehicleDetail.isEmpty())){
							oldCheckInVehicleDetail.get(0).setStatus("Y");
							oldCheckInVehicleDetail.get(0).setTotalTravelDistance(0);    
							oldCheckInVehicleDetail.get(0).setTotalTravelTime(0);	
							oldCheckInVehicleDetail.get(0).setNumberOfTrips(0);
							em.merge(oldCheckInVehicleDetail.get(0));	
							
							EFmFmVehicleMasterPO updateOldVehicleStatus = requestDAOImpl
									.getVehicleDetail(oldCheckInVehicleDetail.get(0).getEfmFmVehicleMaster().getVehicleId());
							updateOldVehicleStatus.setStatus("A");
							em.merge(updateOldVehicleStatus);
							
							
							EFmFmDriverMasterPO oldDriverDetails = requestDAOImpl.getParticularDriverDetail(
									oldCheckInVehicleDetail.get(0).getEfmFmDriverMaster().getDriverId());
							oldDriverDetails.setStatus("A");
							em.merge(oldDriverDetails);
							
							
							List<EFmFmDeviceMasterPO> oldDeviceDetails = requestDAOImpl.getDeviceDetailsFromDeviceId(
									oldCheckInVehicleDetail.get(0).geteFmFmDeviceMaster().getDeviceId(),
									assignRoute.geteFmFmClientBranchPO().getBranchId());
							oldDeviceDetails.get(0).setStatus("Y");
							
							
							em.merge(oldDeviceDetails.get(0));
							
							
							
						}								
						assignRoute.setEfmFmVehicleCheckIn(allCheckInVehicles.get(0));
						assignRoute.setScheduleReadFlg("N");
		//				em.merge(updateVehicleStatus);

						em.merge(assignRoute);
						em.merge(allCheckInVehicles.get(0));
						em.getTransaction().commit();
						em.close();
						
					} else if (allCheckInVehicles.isEmpty()) {
						//This check is in for those vehicles which are already completed the trip
						allCheckInVehicles = requestDAOImpl.getAllCheckedInVehiclesForSpecificCapacityByTravelTime(assignRoute.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity(),1,32400);
						if (!(allCheckInVehicles.isEmpty())) {
							
//								EFmFmVehicleMasterPO updateVehicleStatus = requestDAOImpl
//										.getVehicleDetail(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId());
//								updateVehicleStatus.setStatus("allocated");							
//								updateVehicleStatus.setMonthlyPendingFixedDistance(Math.round(updateVehicleStatus.getMonthlyPendingFixedDistance()-assignRoute.getPlannedTravelledDistance()));
							
								double monthlyPendingFixedDistance=Math.round(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getMonthlyPendingFixedDistance()-assignRoute.getPlannedTravelledDistance());
								requestDAOImpl.updateVehicleCheckInDetailsWithOutStatus(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId(),monthlyPendingFixedDistance,"allocated");		

								EFmFmDriverMasterPO particularDriverDetails = requestDAOImpl
										.getParticularDriverDetail(allCheckInVehicles.get(0).getEfmFmDriverMaster().getDriverId());
								particularDriverDetails.setStatus("allocated");
								em.merge(particularDriverDetails);
								
								List<EFmFmDeviceMasterPO> deviceDetails = requestDAOImpl.getDeviceDetailsFromDeviceId(
										allCheckInVehicles.get(0).geteFmFmDeviceMaster().getDeviceId(),
										assignRoute.geteFmFmClientBranchPO().getBranchId());
								deviceDetails.get(0).setStatus("allocated");
								em.merge(deviceDetails.get(0));
								
								allCheckInVehicles.get(0).setStatus("N");
								allCheckInVehicles.get(0).setTotalTravelDistance(allCheckInVehicles.get(0).getTotalTravelDistance()+assignRoute.getPlannedTravelledDistance());    
								allCheckInVehicles.get(0).setTotalTravelTime(allCheckInVehicles.get(0).getTotalTravelTime()+assignRoute.getPlannedTime());						
								allCheckInVehicles.get(0).setNumberOfTrips(allCheckInVehicles.get(0).getNumberOfTrips()+1);
								List<EFmFmVehicleCheckInPO> oldCheckInVehicleDetail=requestDAOImpl.getParticularCheckedInVehicleDetails(assignRoute.getEfmFmVehicleCheckIn().getCheckInId());
								if(!(oldCheckInVehicleDetail.isEmpty())){
									
									oldCheckInVehicleDetail.get(0).setStatus("Y");
									oldCheckInVehicleDetail.get(0).setTotalTravelDistance(0);    
									oldCheckInVehicleDetail.get(0).setTotalTravelTime(0);	
									oldCheckInVehicleDetail.get(0).setNumberOfTrips(0);
									em.merge(oldCheckInVehicleDetail.get(0));	

									EFmFmVehicleMasterPO updateOldVehicleStatus = requestDAOImpl
											.getVehicleDetail(oldCheckInVehicleDetail.get(0).getEfmFmVehicleMaster().getVehicleId());
									updateOldVehicleStatus.setStatus("A");
									em.merge(updateOldVehicleStatus);
									EFmFmDriverMasterPO oldDriverDetails = requestDAOImpl.getParticularDriverDetail(
											oldCheckInVehicleDetail.get(0).getEfmFmDriverMaster().getDriverId());
									oldDriverDetails.setStatus("A");
									em.merge(oldDriverDetails);
									List<EFmFmDeviceMasterPO> oldDeviceDetails = requestDAOImpl.getDeviceDetailsFromDeviceId(
											oldCheckInVehicleDetail.get(0).geteFmFmDeviceMaster().getDeviceId(),
											assignRoute.geteFmFmClientBranchPO().getBranchId());
									oldDeviceDetails.get(0).setStatus("Y");
									em.merge(oldDeviceDetails.get(0));
								}								
								assignRoute.setEfmFmVehicleCheckIn(allCheckInVehicles.get(0));
								assignRoute.setScheduleReadFlg("N");
//								em.merge(updateVehicleStatus);
								em.merge(assignRoute);
								em.merge(allCheckInVehicles.get(0));
								em.getTransaction().commit();
								em.close();
						}
						else{
							//This check is for those vehicles which are already in field
							allCheckInVehicles = requestDAOImpl.getAllRunningCheckedInVehiclesForSpecificCapacity(assignRoute.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity(),1,32400);
							if (!(allCheckInVehicles.isEmpty())) {
								log.info("AllRunningCheckedInVehicles"+allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleNumber());
									// Per day max travel time per driver is 9 hours as of now in shell and it is in Seconds.
//								EFmFmVehicleMasterPO updateVehicleStatus = requestDAOImpl.getVehicleDetail(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId());
//								updateVehicleStatus.setMonthlyPendingFixedDistance(Math.round(updateVehicleStatus.getMonthlyPendingFixedDistance()-assignRoute.getPlannedTravelledDistance()));
								double monthlyPendingFixedDistance=Math.round(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getMonthlyPendingFixedDistance()-assignRoute.getPlannedTravelledDistance());
								requestDAOImpl.updateVehicleCheckInDetailsWithOutStatus(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId(),monthlyPendingFixedDistance,"allocated");		
 
								
								        allCheckInVehicles.get(0).setTotalTravelDistance(allCheckInVehicles.get(0).getTotalTravelDistance()+assignRoute.getPlannedTravelledDistance());    
										allCheckInVehicles.get(0).setTotalTravelTime(allCheckInVehicles.get(0).getTotalTravelTime()+assignRoute.getPlannedTime());						
										allCheckInVehicles.get(0).setNumberOfTrips(allCheckInVehicles.get(0).getNumberOfTrips()+1);
										List<EFmFmVehicleCheckInPO> oldCheckInVehicleDetail=requestDAOImpl.getParticularCheckedInVehicleDetails(assignRoute.getEfmFmVehicleCheckIn().getCheckInId());
										if(!(oldCheckInVehicleDetail.isEmpty())){
											oldCheckInVehicleDetail.get(0).setStatus("Y");
											oldCheckInVehicleDetail.get(0).setTotalTravelDistance(0);    
											oldCheckInVehicleDetail.get(0).setTotalTravelTime(0);	
											oldCheckInVehicleDetail.get(0).setNumberOfTrips(0);
											em.merge(oldCheckInVehicleDetail.get(0));	
											
											EFmFmVehicleMasterPO updateOldVehicleStatus = requestDAOImpl
													.getVehicleDetail(oldCheckInVehicleDetail.get(0).getEfmFmVehicleMaster().getVehicleId());
											updateOldVehicleStatus.setStatus("A");
											em.merge(updateOldVehicleStatus);
											
											EFmFmDriverMasterPO oldDriverDetails = requestDAOImpl.getParticularDriverDetail(
													oldCheckInVehicleDetail.get(0).getEfmFmDriverMaster().getDriverId());
											oldDriverDetails.setStatus("A");
											em.merge(oldDriverDetails);
											
											List<EFmFmDeviceMasterPO> oldDeviceDetails = requestDAOImpl.getDeviceDetailsFromDeviceId(
													oldCheckInVehicleDetail.get(0).geteFmFmDeviceMaster().getDeviceId(),
													assignRoute.geteFmFmClientBranchPO().getBranchId());
											oldDeviceDetails.get(0).setStatus("Y");
											em.merge(oldDeviceDetails.get(0));
											
										}								
										assignRoute.setEfmFmVehicleCheckIn(allCheckInVehicles.get(0));
										assignRoute.setScheduleReadFlg("N");
//										em.merge(updateVehicleStatus);
										em.merge(assignRoute);
										em.merge(allCheckInVehicles.get(0));
										em.getTransaction().commit();
										em.close();
							}

						}

					} 

//				} 
	}catch(Exception e){
					log.info("scheduling Exception "+e);
					em.close();
					return;
				}
				
	}
	
	public void updateVehicleMonthlyContractedKmAndDate(int branchId)
			throws ParseException, IOException, InterruptedException {
		log.info("Inside Monthly Contracted KM");
		DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");

		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}
		log.info("Inside Monthly Contracted KM Updation");
		SchedulerVehicleCheckInDAOImpl vehicleDAOImpl = new SchedulerVehicleCheckInDAOImpl(em);
		List<EFmFmVehicleMasterPO> vehicleList=vehicleDAOImpl.getAllVehicleDetailsFromBranchId(branchId);
		if(!vehicleList.isEmpty()){
			for(EFmFmVehicleMasterPO vehicleDeatil:vehicleList){				
				vehicleDeatil.setMonthlyPendingFixedDistance(4000);
				String requestDate =  dateformate.format(new Date());			
				Date contractUpdateDate = dateformate.parse(requestDate);
				vehicleDeatil.setMonthlyPendingKmUpdateDate(contractUpdateDate);
				em.merge(vehicleDeatil);
			}
			em.getTransaction().commit();
			em.close();
			log.info("Monthly Contracted KM Updated");
		}

	}

	public void  driverTripServiceCalled(EFmFmVehicleCheckInPO checkInDetail) throws InterruptedException, IOException, ParseException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException{				
		if (DELAY_THROTTLE > 0)
			Thread.sleep(DELAY_THROTTLE);
		EntityManager em = entityMangerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			em.close();
			return;
		}
		try{
		URL url = new URL("http://mmthinkbiz.com/Mobileservice.aspx?method=GetVehicleList&userId=genpact&pwd=genpact123&VNO="+(checkInDetail.getEfmFmVehicleMaster().getVehicleNumber().trim().replaceAll("\\s+", "")));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}			
		BufferedReader br = new BufferedReader(new InputStreamReader(
		(conn.getInputStream())));
		String vehicleDeatil="";
		Date trackingTime = null;
		String lattitudeLongitude="";
		String speed="";	
		String address="";	
	//	int checkInId=checkInDetail.getCheckInId();		
		if(checkInDetail.getEfmFmVehicleMaster().getVehicleNumber().equalsIgnoreCase("RJ14TB4255")){
			System.out.println(".......");
		}
		
		while ((vehicleDeatil = br.readLine()) != null) {
			JSONObject jsonObject = new JSONObject(vehicleDeatil); // removing brackets  
		    JSONArray lineItems = jsonObject.getJSONArray("data");
		    
			lattitudeLongitude=(lineItems.getJSONObject(0).get("Longitude")+","+lineItems.getJSONObject(0).get("Latitude"));
			speed=lineItems.getJSONObject(0).get("Speed").toString();
			address=lineItems.getJSONObject(0).get("Address").toString();
			String trackingDate = lineItems.getJSONObject(0).get("Trackingtime").toString();
		    DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy hh:mma"); 	
	        DateFormat dateFormate1 = new SimpleDateFormat("dd-MM-yyyy HH:mm a");		        
		    Date date1;
		    String newDateFormate="";
		    try {
		    	date1 = dateFormat.parse(trackingDate);		        
		         newDateFormate = dateFormate1.format(date1);
		        System.out.println(newDateFormate);
		    } catch (ParseException e) {
		    	newDateFormate = dateFormate1.format(new Date());
		    }
			DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			trackingTime = dateTimeFormate.parse(newDateFormate);				
		}	
				
		conn.disconnect();
		int branchId=1;
		log.info("Inside Gps device drivertrip service call With out trip");
		SchedulerVehicleCheckInDAOImpl vehicleCheckInBO = new SchedulerVehicleCheckInDAOImpl(em);		
			EFmFmVehicleCheckInPO checkIn = new EFmFmVehicleCheckInPO();
			checkIn.setCheckInId(checkInDetail.getCheckInId());
			EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
			eFmFmClientBranch.setBranchId(branchId);
			// Method for geting the trip after bucket close only
			List<EFmFmAssignRoutePO> assignRoute = vehicleCheckInBO.closeVehicleCapacity(checkInDetail.getCheckInId(), branchId);
			log.info(
					"Size" + assignRoute.size() + "LatitudeAnd Longitude" + lattitudeLongitude);
			if ((!(assignRoute.isEmpty()))) {
				if (checkInDetail.getStatus().equalsIgnoreCase("Y")) {
					checkInDetail.setStatus("N");
					em.merge(checkInDetail);
				}
				log.info("ClientLattiLongi" + assignRoute.get(0).geteFmFmClientBranchPO().getLatitudeLongitude());
				double totalDistance = Geocode.distance(
						new Geocode(assignRoute.get(0).geteFmFmClientBranchPO().getLatitudeLongitude()),
						new Geocode(lattitudeLongitude));
				
				log.info("Distance ..." + totalDistance);
				// GeoFence on trip stated when vehicle out from office
				if (totalDistance > (assignRoute.get(0).geteFmFmClientBranchPO().getStartTripGeoFenceAreaInMeter())
						&& assignRoute.get(0).getTripStatus().equalsIgnoreCase("allocated") && assignRoute.get(0).getBucketStatus().equalsIgnoreCase("Y")) {
					// Geo Fencing the client Office auto
//					PolygonalGeofence fence = new PolygonalGeofence();
//					StringTokenizer stringTokenizer = new StringTokenizer(
//							assignRoute.get(0).geteFmFmClientBranchPO().getGeoCodesForGeoFence(), "|");
//					String vertex = "";
//					while (stringTokenizer.hasMoreElements()) {
//						vertex = (String) stringTokenizer.nextElement();
//						fence.addVertex(new Geocode(vertex));
//					}
//					fence.setVehicleLocation(new Geocode(lattitudeLongitude));
//					log.info("Inside auto trip start M Distance" + totalDistance);
//					if (!(fence.isInGeofence())) {
						log.info("Inside Geo Fence Reason" + totalDistance);
						try {
						//	assignRoutePO.setTripAssignDate(new Date());
							// start auto trip on start service							
							assignRoute.get(0).setTripStartTime(new Date());
							assignRoute.get(0).setOdometerStartKm("0");
							assignRoute.get(0).setTripStatus("Started");
							em.merge(assignRoute.get(0));
							em.getTransaction().commit();
							em.close();
						} catch (Exception e) {
							log.info("Inside auto start" + e);
//						}
					}
				}else if(!(assignRoute.get(0).getTripStatus().equalsIgnoreCase("allocated")) && assignRoute.get(0).getBucketStatus().equalsIgnoreCase("Y")){
					//Start of Location Updater Service Call					
					final PushNotificationService pushNotification = new PushNotificationService();
					EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
					EFmFmClientBranchPO clientBranch = new EFmFmClientBranchPO();
					clientBranch.setBranchId(branchId);
					assignRoutePO.setAssignRouteId(assignRoute.get(0).getAssignRouteId());
					assignRoutePO.seteFmFmClientBranchPO(clientBranch);			
					List<EFmFmAssignRoutePO> liveRoute = vehicleCheckInBO.closeParticularTrips(assignRoutePO);			
					String currentLatLong = "";
					if (liveRoute.get(0).getTripStatus().equalsIgnoreCase("completed")) {
						// Trip Completed.... in mobile device we are sending return status as N
						return ;
					} 
					final List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = vehicleCheckInBO.getParticularTripNonDropEmployeesDetails(assignRoute.get(0).getAssignRouteId());
				
					List<EFmFmEmployeeTripDetailPO> dropEmployeeTripDetail = vehicleCheckInBO.getDropTripAllSortedNonDropEmployees(liveRoute.get(0).getAssignRouteId());

					List<EFmFmEmployeeTripDetailPO> baseLatiLongi = vehicleCheckInBO
							.getParticularTripAllEmployees(liveRoute.get(0).getAssignRouteId());
					final MessagingService messaging = new MessagingService();
					
					log.info("assign routeId" + liveRoute.get(0).getAssignRouteId());
					List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = vehicleCheckInBO.getRouteLastEtaAndDistanceFromAssignRouteId(
							liveRoute.get(0).getAssignRouteId());
					List<EFmFmEmployeeTripDetailPO> allRequests = null;
					StringBuffer allwayPoints = new StringBuffer();
					if (!(actualRouteTravelled.isEmpty()) && !(baseLatiLongi.isEmpty())) {
						String lastLatLong = actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude();
						String lastLat = lastLatLong.split(",")[0];
						String lastLong = lastLatLong.split(",")[1];
						String LatFistDigit = lastLat.split("\\.")[0];
						String LongiFistDigit = lastLong.split("\\.")[0];
						if (lastLat.split("\\.")[1].length() > 4) {
							lastLat = lastLat.split("\\.")[1].substring(0, 4);
						}
						if (lastLong.split("\\.")[1].length() > 4) {
							lastLong = lastLong.split("\\.")[1].substring(0, 4);
						}
						lastLatLong = LatFistDigit + "." + lastLat + "," + LongiFistDigit + "." + lastLong;
						currentLatLong = lattitudeLongitude;
						String currentLat = currentLatLong.split(",")[0];
						String currentLong = currentLatLong.split(",")[1];
						String currentLatFistDigit = currentLat.split("\\.")[0];
						String currentLongiFistDigit = currentLong.split("\\.")[0];
						if (currentLat.split("\\.")[1].length() > 4) {
							currentLat = currentLat.split("\\.")[1].substring(0, 4);
						}
						if (currentLong.split("\\.")[1].length() > 4) {
							currentLong = currentLong.split("\\.")[1].substring(0, 4);
						}
						currentLatLong = currentLatFistDigit + "." + currentLat + "," + currentLongiFistDigit + "." + currentLong;
						log.info("stored LatitudeLongi" + lastLatLong);
						log.info("new LatitudeLongi" + lattitudeLongitude);
						//Mobile or GPS Lattitude and longitude are equals than it will go in this block and return back to eta
						if (currentLatLong.equalsIgnoreCase(lastLatLong)) {
							actualRouteTravelled.get(actualRouteTravelled.size() - 1).setLiveTravelledTime(new Date());						
							em.merge(actualRouteTravelled.get(actualRouteTravelled.size() - 1));	
							em.getTransaction().commit();
							em.close();
							return;
						}
					}

					if (!(employeeTripDetailPO.isEmpty())
							&& !(liveRoute.get(0).getRouteGenerationType().equalsIgnoreCase("nodalAdhoc"))
							&& baseLatiLongi.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
						for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
							if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("N")) {
								allwayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getLatitudeLongitude() + "|");
							}
						}
					}
					if (!(employeeTripDetailPO.isEmpty())
							&& liveRoute.get(0).getRouteGenerationType().equalsIgnoreCase("nodalAdhoc")
							&& baseLatiLongi.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
						for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
							if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("N")) {
								allwayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping()
										.geteFmFmNodalAreaMaster().getNodalPoints() + "|");
							}
						}
					}

					String urlLocation = "", etaInSec = "", driverEta = "";
					int eta = 0;
					int totalDistance1 = 0;
					int etaInSeconds = 0;
					String allpoints = allwayPoints.toString();
					String cabCurrentLocation = "";

					if (!(baseLatiLongi.isEmpty())
							&& baseLatiLongi.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
						allRequests = vehicleCheckInBO.getParticularTripAllEmployees(assignRoutePO.getAssignRouteId());
					} else {
						allRequests = vehicleCheckInBO.getDropTripAllSortedEmployees(assignRoutePO.getAssignRouteId());
					}
					if (!(baseLatiLongi.isEmpty())
							&& baseLatiLongi.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
						if (!(dropEmployeeTripDetail.isEmpty())) {
							allpoints = dropEmployeeTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
									.getLatitudeLongitude();
						} else {
							allpoints = baseLatiLongi.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
									.geteFmFmClientBranchPO().getLatitudeLongitude();
						}
						String distanceApi = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
	
						String apiConfiguration = "&mode=driving&units=metric&sensor=true&client=gme-newtglobalindiaprivate";
	
						urlLocation = distanceApi + lattitudeLongitude + "&destinations=" + allpoints
								+ apiConfiguration;
						log.info("urlLocation" + urlLocation);
						URL geocodeURL;
						URL url1 = new URL(urlLocation);
						SchedulingService.passingKey(keyString);
						String request = SchedulingService.signRequest(url1.getPath(), url1.getQuery());
						urlLocation = url1.getProtocol() + "://" + url1.getHost() + request;
						geocodeURL = new URL(urlLocation);
						URLConnection connection = geocodeURL.openConnection();
						connection.connect();
						BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						String data = "";
						String line = "";
						while ((line = reader.readLine()) != null) {
							data += line.trim();
						}
						JSONObject status = new JSONObject(data);
						String objstatus = status.getString("status");
						if (objstatus.equals("OK")) {
							cabCurrentLocation = status.getJSONArray("origin_addresses").toString();
							JSONArray rows = status.getJSONArray("rows");
							JSONArray elements = rows.getJSONObject(0).getJSONArray("elements");
							if (elements.getJSONObject(0).getString("status").equalsIgnoreCase("OK")) {
								for (int i = 0; i < elements.length();) {
									eta = elements.getJSONObject(i).getJSONObject("duration").getInt("value");
									totalDistance1 += elements.getJSONObject(i).getJSONObject("distance").getInt("value");
									int hours = eta / 3600;
									int minutes = (eta % 3600) / 60;
									int seconds = eta % 60;
									if (hours != 0) {
										etaInSec = hours + " h " + minutes + " min" + seconds + " sec";
									} else if (minutes != 0) {
										etaInSec = minutes + " min " + seconds + " sec";
									} else {
										etaInSec = seconds + " sec";
									}
									driverEta = etaInSec;
									etaInSeconds = eta;
									log.info("Distance ..." + totalDistance1);
									if (!(baseLatiLongi.isEmpty())) {
										// GeoFence on trip complete when vehicle reaches
										// Last drop location
										if (dropEmployeeTripDetail.isEmpty() || (!(dropEmployeeTripDetail.isEmpty()) && (Geocode.distance(
												new Geocode(dropEmployeeTripDetail.get(dropEmployeeTripDetail.size()-1).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude()),
												new Geocode(lattitudeLongitude)))<(liveRoute.get(0).geteFmFmClientBranchPO().getEndTripGeoFenceAreaInMeter()))   && (!(liveRoute.get(0).getRouteGenerationType().equalsIgnoreCase("nodalAdhoc")))) {											
											if(!(dropEmployeeTripDetail.isEmpty())){
												for (EFmFmEmployeeTripDetailPO empStatusDetail : dropEmployeeTripDetail) {
													empStatusDetail.setBoardedFlg("NO");
													empStatusDetail.setEmployeeStatus("completed");
													empStatusDetail.setCabstartFromDestination(new Date().getTime());
													em.merge(empStatusDetail);
												}
											}
											
											
											// Geo Fencing the client Office auto
//											PolygonalGeofence fence = new PolygonalGeofence();
//											StringTokenizer stringTokenizer = new StringTokenizer(
//													liveRoute.get(0).geteFmFmClientBranchPO().getGeoCodesForGeoFence(), "|");
//											String vertex = "";
//											while (stringTokenizer.hasMoreElements()) {
//												vertex = (String) stringTokenizer.nextElement();
//												fence.addVertex(new Geocode(vertex));
//											}
//											fence.setVehicleLocation(new Geocode(lattitudeLongitude));
											if(liveRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")){
												try{
													List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckInBO
															.getParticulaEscortDetailFromEscortId(assignRoute.get(0).geteFmFmClientBranchPO().getBranchId(),
																	assignRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
													checkInEscortDetails.get(0).setStatus("Y");
													em.merge(checkInEscortDetails.get(0));
												}catch(Exception e){
													log.info("Error"+e);	
												}
											
											}
											
											log.info("Inside auto trip complete M Distance" + totalDistance1);
	//										if (fence.isInGeofence()) {
												try {
													EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
													eFmFmVehicleCheckInPO.setCheckInTime(new Date());
													eFmFmVehicleCheckInPO
															.setCheckInId(liveRoute.get(0).getEfmFmVehicleCheckIn().getCheckInId());
													List<EFmFmVehicleCheckInPO> vehicleCheckIn = vehicleCheckInBO
															.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
													EFmFmVehicleMasterPO vehicleMasterDetail = vehicleCheckInBO
															.getParticularVehicleDetail(
																	vehicleCheckIn.get(0).getEfmFmVehicleMaster().getVehicleId());
													List<EFmFmAssignRoutePO> oldCheckRoutesCheck = vehicleCheckInBO
															.getTripAllocatedRoute(vehicleCheckIn.get(0).getCheckInId(),
																	liveRoute.get(0).geteFmFmClientBranchPO().getBranchId());
													if (oldCheckRoutesCheck.size() == 1) {
														vehicleCheckIn.get(0).setStatus("Y");
														vehicleMasterDetail.setStatus("A");
													}
													liveRoute.get(0).setTripCompleteTime(new Date());
													liveRoute.get(0).setTripStatus("completed");
													vehicleCheckIn.get(0)
															.setTotalTravelTime((vehicleCheckIn.get(0).getTotalTravelTime()
																	- liveRoute.get(0).getPlannedTime())
																	+ ((new Date().getTime()
																			- liveRoute.get(0).getTripStartTime().getTime())
																			/ 1000));
													double plannedDis = vehicleMasterDetail.getMonthlyPendingFixedDistance()
															+ liveRoute.get(0).getPlannedDistance();
													em.merge(vehicleMasterDetail);
													double totalTravelledDis = 0;
													// start auto trip complete by geo fence
													// and updation of planned distance
													double lastDropToOfficeDistance =0;
													if (!(actualRouteTravelled.isEmpty())) {

														StringBuffer empWayPoints = new StringBuffer();
														CalculateDistance calculateDistance = new CalculateDistance();
														if (liveRoute.get(0).getRouteGenerationType().contains("nodalAdhoc")) {
															empWayPoints.append(liveRoute.get(0).getNodalPoints());
														} else {
															if (!(allRequests.isEmpty())) {
																for (EFmFmEmployeeTripDetailPO employeeTripDetail : allRequests) {
																	String wayPointsAdhocRequest = "";
																	if (liveRoute.get(0).getRouteGenerationType()
																			.contains("nodal")) {
																		if(!(empWayPoints.toString().contains(
																				employeeTripDetail.geteFmFmEmployeeTravelRequest()
																				.geteFmFmRouteAreaMapping()
																				.geteFmFmNodalAreaMaster().getNodalPoints()))){
																			empWayPoints.append(
																					employeeTripDetail.geteFmFmEmployeeTravelRequest()
																							.geteFmFmRouteAreaMapping()
																							.geteFmFmNodalAreaMaster().getNodalPoints()
																							+ "|");	
																			
																		}
																	} else if (liveRoute.get(0).getRouteGenerationType()
																			.equalsIgnoreCase("AdhocRequest")) {
																		empWayPoints.append(wayPointsAdhocRequest
																				+ employeeTripDetail.geteFmFmEmployeeTravelRequest()
																						.geteFmFmEmployeeRequestMaster()
																						.getEndDestinationAddressLattitudeLongitude()
																				+ "|");
																	} else {
																		empWayPoints.append(
																				employeeTripDetail.geteFmFmEmployeeTravelRequest()
																						.getEfmFmUserMaster().getLatitudeLongitude()
																						+ "|");
																	}
																}

															}
														}
														//Get vehicle First  Location
//														List<EFmFmLiveRoutTravelledPO> routeFirstLocation = vehicleCheckInBO
//																.getRouteLastEtaAndDistanceFromAssignRouteIdForCabLastLocation(
//																		branchId,
//																		liveRoute.get(0).getAssignRouteId());														
														try {
															lastDropToOfficeDistance = calculateDistance.employeeDistanceCalculation(lattitudeLongitude,liveRoute.get(0).geteFmFmClientBranchPO().getLatitudeLongitude());
															
															totalTravelledDis = liveRoute.get(0).getTravelledDistance()
																	+ (liveRoute.get(0).geteFmFmClientBranchPO()
																			.getAddingGeoFenceDistanceIntrip()+lastDropToOfficeDistance);
//															liveRoute.get(0)
//																	.setPlannedDistance(Double.parseDouble(plannedDistance)
//																			+ (liveRoute.get(0).geteFmFmClientBranchPO()
//																					.getAddingGeoFenceDistanceIntrip()));
															
															liveRoute.get(0).setTravelledDistance(totalTravelledDis);

														} catch (Exception e) {
															log.info("auto trip complete auto drop" + e);
														}

													}
													// start auto trip complete by geo fence
													// and updation of planned distance

													liveRoute.get(0).setOdometerEndKm(assignRoutePO.getOdometerEndKm());
													
//													if(!(liveRoute.get(0).geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP"))){
//													liveRoute.get(0).setPlannedTravelledDistance(totalTravelledDis);
//													liveRoute.get(0).setPlannedDistance(liveRoute.get(0).getPlannedDistance()+lastDropToOfficeDistance);
//													}
//													
													liveRoute.get(0).setPlannedTime(
															(new Date().getTime() - liveRoute.get(0).getTripStartTime().getTime())
																	/ 1000);
													
													em.merge(liveRoute.get(0));
													
													if (oldCheckRoutesCheck.size() == 1) {
														EFmFmDriverMasterPO particularDriverDetails = vehicleCheckInBO
																.getParticularDriverDetail(
																		vehicleCheckIn.get(0).getEfmFmDriverMaster().getDriverId());
														particularDriverDetails.setStatus("A");
														em.merge(particularDriverDetails);
														log.info("Inside GeoFence Area" + vehicleMasterDetail.getVehicleNumber());
														List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckInBO
																.getDeviceDetailsFromDeviceId(
																		vehicleCheckIn.get(0).geteFmFmDeviceMaster().getDeviceId(),
																		branchId);
														deviceDetails.get(0).setStatus("Y");
														em.merge(deviceDetails.get(0));
													}
													try {
														vehicleCheckIn.get(0).setTotalTravelDistance(
																(vehicleCheckIn.get(0).getTotalTravelDistance()
																		- liveRoute.get(0).getPlannedDistance())
																		+ totalTravelledDis);
													} catch (Exception e) {
														log.info("Error updating the travelled and number of trips" + e);
													}
													em.merge(vehicleCheckIn.get(0));
													if (liveRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
														try {
															int a = liveRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId();
															List<EFmFmEscortCheckInPO> escortDetails = vehicleCheckInBO
																	.getParticulaEscortDetailFromEscortId(branchId,
																			liveRoute.get(0).geteFmFmEscortCheckIn()
																					.getEscortCheckInId());
															// escortDetails.get(0).setEscortCheckOutTime(new
															// Date());
															escortDetails.get(0).setStatus("Y");
															em.merge(escortDetails.get(0));
														} catch (Exception e) {
															log.info("Escort Blog Exception" + e);
														}
													}

													vehicleMasterDetail
															.setMonthlyPendingFixedDistance(plannedDis - totalTravelledDis);
													em.merge(vehicleMasterDetail);

													log.info("allRequests" + allRequests.size());
													if (!(allRequests.isEmpty())) {
														for (EFmFmEmployeeTripDetailPO requestDetail : allRequests) {
															List<EFmFmEmployeeTravelRequestPO> activerequest = vehicleCheckInBO
																	.getParticularRequestDetailOnTripComplete(requestDetail.geteFmFmEmployeeTravelRequest()
																					.getRequestId());
															activerequest.get(0).setCompletionStatus("Y");
															em.merge(activerequest.get(0));
															if (!(requestDetail.getEmployeeStatus()
																	.equalsIgnoreCase("completed"))) {
																requestDetail.setEmployeeStatus("completed");
																em.merge(requestDetail);
															}

														}
													}
												} catch (Exception e) {
													log.info("Inside auto tripComplete" + e);
												}
												log.info("First Thread tripcompleted End");
												try{
												List<EFmFmLiveRoutTravelledPO> liveTravelledRoute = vehicleCheckInBO.getCompletedRouteDataFromAssignRouteId(
														assignRoutePO.geteFmFmClientBranchPO().getBranchId(), assignRoutePO.getAssignRouteId());
												EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
												EFmFmAssignRoutePO efmFmAssignRoute = new EFmFmAssignRoutePO();
												eFmFmClientBranchPO.setBranchId(liveTravelledRoute.get(0).geteFmFmClientBranchPO().getBranchId());
												efmFmAssignRoute.setAssignRouteId(liveTravelledRoute.get(0).getEfmFmAssignRoute().getAssignRouteId());
												Thread thread2 = new Thread(new Runnable() {
													@Override
													public void run() {
														for (EFmFmLiveRoutTravelledPO liveRouteDetail : liveTravelledRoute) {
															EFmFmActualRoutTravelledPO actualRoute = new EFmFmActualRoutTravelledPO();
															actualRoute.setCurrentCabLocation(liveRouteDetail.getLiveCurrentCabLocation());
															actualRoute.setCurrentEta(liveRouteDetail.getLiveEta());
															actualRoute.setEfmFmAssignRoute(efmFmAssignRoute);
															actualRoute.seteFmFmClientBranchPO(eFmFmClientBranchPO);
															actualRoute.setEtaInSeconds(liveRouteDetail.getLiveEtaInSeconds());
															actualRoute.setLatitudeLongitude(liveRouteDetail.getLivelatitudeLongitude());
															actualRoute.setSpeed(liveRouteDetail.getLiveSpeed());
															actualRoute.setTravelledDistance(liveRouteDetail.getLiveTravellesDistance());
															actualRoute.setTravelledTime(liveRouteDetail.getLiveTravelledTime());
															em.merge(actualRoute);
															vehicleCheckInBO.deleteParticularActualTravelled(liveRouteDetail.getLiveRouteTravelId());					
														}
													}
												});
												thread2.start();
											}catch(Exception e){
												log.info("table purging"+e);
											}
												log.info("Second Thread tripcompleted End");
//											}
										}

										if (driverEta.length() == 0 && driverEta.isEmpty()) {
											if (!(actualRouteTravelled.isEmpty())) {
												driverEta = actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta();
											} else {
												driverEta = "Calculating...";
											}
										}
										break;
									}
								}
							}
						}
					}
					//trip Type drop End

					//trip Type Pickup start
					if (!(baseLatiLongi.isEmpty())
							&& baseLatiLongi.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
						String directionApi ="http://maps.googleapis.com/maps/api/directions/json?origin=";
						urlLocation = directionApi + lattitudeLongitude + "&destination="
								+ baseLatiLongi.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().geteFmFmClientBranchPO()
										.getLatitudeLongitude()
								+ "&waypoints=optimize:false|" + allwayPoints.toString()
								+ "&mode=driving&language=en-EN&sensor=false&client=gme-newtglobalindiaprivate";
						URL geocodeURL;
						URL url1 = new URL(urlLocation);
						SchedulingService.passingKey(keyString);
						String request = SchedulingService.signRequest(url1.getPath(), url1.getQuery());
						urlLocation = url1.getProtocol() + "://" + url1.getHost() + request;
						log.info("URL"+urlLocation);
						geocodeURL = new URL(urlLocation);
						URLConnection connection = geocodeURL.openConnection();
						connection.connect();
						BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						String data = "";
						String line = "";
						JSONArray cabLocation = null;
						while ((line = reader.readLine()) != null) {
							data += line.trim();
						}
						JSONObject status = new JSONObject(data);
						String objstatus = status.getString("status");
						if (objstatus.equals("OK")) {
							cabLocation = new JSONObject(data).getJSONArray("routes");
							DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
							DateFormat dateTimeformate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
							String currentDate = dateformate.format(new Date());
							boolean etaFlag = true;
							long currentTime = System.currentTimeMillis();
							JSONArray elements = cabLocation.getJSONObject(0).getJSONArray("legs");
							cabCurrentLocation = elements.getJSONObject(0).get("start_address").toString();
							for (int i = 0; i <= elements.length() - 1; i++) {
								try {
									eta += elements.getJSONObject(i).getJSONObject("duration").getInt("value");
									totalDistance1 += elements.getJSONObject(i).getJSONObject("distance").getInt("value");
									int hours = eta / 3600;
									int minutes = (eta % 3600) / 60;
									int seconds = eta % 60;
									hours = eta / 3600;
									minutes = (eta % 3600) / 60;
									seconds = eta % 60;
									if (hours != 0) {
										etaInSec = hours + " h " + minutes + " min " + seconds + " sec";
									} else if (minutes != 0) {
										etaInSec = minutes + " min " + seconds + " sec";
									} else {
										etaInSec = seconds + " sec";
									}
									log.info("Distance ..." + totalDistance1);
									if (!(baseLatiLongi.isEmpty())) {
										// GeoFence on trip complete when vehicle reaches
										// back to office
										List<EFmFmEmployeeTripDetailPO> rechedEmpList = new ArrayList<EFmFmEmployeeTripDetailPO>();
										try{										
										rechedEmpList= vehicleCheckInBO.getreachedEmployeesList(liveRoute.get(0).getAssignRouteId());
										
										}catch(Exception e){
											log.info("rechedEmpList ..." + e);

										}
										log.info("rechedEmpList ..." + rechedEmpList.size());

											if((Geocode.distance(
												new Geocode(liveRoute.get(0).geteFmFmClientBranchPO().getLatitudeLongitude()),
												new Geocode(lattitudeLongitude)))<(liveRoute.get(0).geteFmFmClientBranchPO()
												.getEndTripGeoFenceAreaInMeter()) && (!(rechedEmpList.isEmpty()))											
												&& (!(liveRoute.get(0).getRouteGenerationType().equalsIgnoreCase("nodalAdhoc")))) {
												
												
												for (EFmFmEmployeeTripDetailPO empStatusDetail : allRequests) {
													empStatusDetail.setBoardedFlg("NO");
													empStatusDetail.setEmployeeStatus("completed");
													empStatusDetail.setCabstartFromDestination(new Date().getTime());
													em.merge(empStatusDetail);
												}
												
												
												if(liveRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")){
													try{
														List<EFmFmEscortCheckInPO> checkInEscortDetails = vehicleCheckInBO
																.getParticulaEscortDetailFromEscortId(assignRoute.get(0).geteFmFmClientBranchPO().getBranchId(),
																		assignRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
														checkInEscortDetails.get(0).setStatus("Y");
														em.merge(checkInEscortDetails.get(0));
													}catch(Exception e){
														log.info("Error"+e);	
													}
												
												}
												
											// Geo Fencing the client Office auto
//											PolygonalGeofence fence = new PolygonalGeofence();
//											StringTokenizer stringTokenizer = new StringTokenizer(
//													liveRoute.get(0).geteFmFmClientBranchPO().getGeoCodesForGeoFence(), "|");
//											String vertex = "";
//											while (stringTokenizer.hasMoreElements()) {
//												vertex = (String) stringTokenizer.nextElement();
//												fence.addVertex(new Geocode(vertex));
//											}
//											fence.setVehicleLocation(new Geocode(lattitudeLongitude));
//											log.info("Inside auto trip complete M Distance" + totalDistance1);
//											if (fence.isInGeofence()) {
												try {
													EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
													eFmFmVehicleCheckInPO.setCheckInTime(new Date());
													eFmFmVehicleCheckInPO
															.setCheckInId(liveRoute.get(0).getEfmFmVehicleCheckIn().getCheckInId());
													List<EFmFmVehicleCheckInPO> vehicleCheckIn = vehicleCheckInBO
															.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
													EFmFmVehicleMasterPO vehicleMasterDetail = vehicleCheckInBO
															.getParticularVehicleDetail(
																	vehicleCheckIn.get(0).getEfmFmVehicleMaster().getVehicleId());

													List<EFmFmAssignRoutePO> oldCheckRoutesCheck = vehicleCheckInBO
															.getTripAllocatedRoute(
																	liveRoute.get(0).getEfmFmVehicleCheckIn().getCheckInId(),
																	liveRoute.get(0).geteFmFmClientBranchPO().getBranchId());
													if (oldCheckRoutesCheck.size() == 1) {
														vehicleCheckIn.get(0).setStatus("Y");
														vehicleMasterDetail.setStatus("A");
													}
													liveRoute.get(0).setTripCompleteTime(new Date());
													liveRoute.get(0).setTripStatus("completed");
													vehicleCheckIn.get(0)
															.setTotalTravelTime((vehicleCheckIn.get(0).getTotalTravelTime()
																	- liveRoute.get(0).getPlannedTime())
																	+ ((new Date().getTime()
																			- liveRoute.get(0).getTripStartTime().getTime())
																			/ 1000));
													double plannedDis = vehicleMasterDetail.getMonthlyPendingFixedDistance()
															+ liveRoute.get(0).getPlannedDistance();
													em.merge(vehicleMasterDetail);
													double totalTravelledDis = 0;
													// start auto trip complete by geo fence
													// and updation of planned distance
													if (!(actualRouteTravelled.isEmpty())) {
														StringBuffer empWayPoints = new StringBuffer();
														CalculateDistance calculateDistance = new CalculateDistance();
														if (liveRoute.get(0).getRouteGenerationType().contains("nodalAdhoc")) {
															empWayPoints.append(liveRoute.get(0).getNodalPoints());
														} else {
															if (!(allRequests.isEmpty())) {
																for (EFmFmEmployeeTripDetailPO employeeTripDetail : allRequests) {
																	String wayPointsAdhocRequest = "";
																	if (liveRoute.get(0).getRouteGenerationType()
																			.contains("nodal")) {
																		if(!(empWayPoints.toString().contains(
																				employeeTripDetail.geteFmFmEmployeeTravelRequest()
																				.geteFmFmRouteAreaMapping()
																				.geteFmFmNodalAreaMaster().getNodalPoints()))){
																			empWayPoints.append(
																					employeeTripDetail.geteFmFmEmployeeTravelRequest()
																							.geteFmFmRouteAreaMapping()
																							.geteFmFmNodalAreaMaster().getNodalPoints()
																							+ "|");
																			
																		}
																	
																	} else if (liveRoute.get(0).getRouteGenerationType()
																			.equalsIgnoreCase("AdhocRequest")) {
																		empWayPoints.append(wayPointsAdhocRequest
																				+ employeeTripDetail.geteFmFmEmployeeTravelRequest()
																						.geteFmFmEmployeeRequestMaster()
																						.getEndDestinationAddressLattitudeLongitude()
																				+ "|");
																	} else {
																		empWayPoints.append(
																				employeeTripDetail.geteFmFmEmployeeTravelRequest()
																						.getEfmFmUserMaster().getLatitudeLongitude()
																						+ "|");
																	}
																}

															}
														}
														//Get vehicle First  Location
//														List<EFmFmLiveRoutTravelledPO> routeFirstLocation = vehicleCheckInBO
//																.getRouteLastEtaAndDistanceFromAssignRouteIdForCabLastLocation(branchId,
//																		liveRoute.get(0).getAssignRouteId());
																												
//														
														 double lastDropToOfficeDistance = calculateDistance.employeeDistanceCalculation(liveRoute.get(0).geteFmFmClientBranchPO().getLatitudeLongitude(),baseLatiLongi.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude());
														try {
															
//															String plannedDistance = calculateDistance.getPlannedDistanceByMapApi(
//																	routeFirstLocation.get(0).getLivelatitudeLongitude(),
//																	actualRouteTravelled.get(actualRouteTravelled.size() - 1)
//																			.getLivelatitudeLongitude(),
//																	empWayPoints.toString());
//															

															totalTravelledDis = liveRoute.get(0).getTravelledDistance()
																	+ (liveRoute.get(0).geteFmFmClientBranchPO()
																			.getAddingGeoFenceDistanceIntrip()+lastDropToOfficeDistance);
//															liveRoute.get(0)
//																	.setPlannedDistance(Double.parseDouble(plannedDistance)
//																			+ (liveRoute.get(0).geteFmFmClientBranchPO()
//																					.getAddingGeoFenceDistanceIntrip()));
															
															liveRoute.get(0).setTravelledDistance(totalTravelledDis);

														} catch (Exception e) {
															log.info("auto trip complete planned distance by button click" + e);
														}

													}

													// end auto trip complete by geo fence
													// and updation of planned distance
													
//													if(!(liveRoute.get(0).geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP"))){
//													liveRoute.get(0).setPlannedTravelledDistance(totalTravelledDis);
//													}
													liveRoute.get(0).setPlannedTime(
															(new Date().getTime() - liveRoute.get(0).getTripStartTime().getTime())
																	/ 1000);
													liveRoute.get(0).setOdometerEndKm(assignRoutePO.getOdometerEndKm());
													em.merge(liveRoute.get(0));
													if (oldCheckRoutesCheck.size() == 1) {
														EFmFmDriverMasterPO particularDriverDetails = vehicleCheckInBO
																.getParticularDriverDetail(
																		vehicleCheckIn.get(0).getEfmFmDriverMaster().getDriverId());
														particularDriverDetails.setStatus("A");
														em.merge(particularDriverDetails);
														log.info("Inside GeoFence Area" + vehicleMasterDetail.getVehicleNumber());
														List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckInBO
																.getDeviceDetailsFromDeviceId(
																		vehicleCheckIn.get(0).geteFmFmDeviceMaster().getDeviceId(),
																		branchId);
														deviceDetails.get(0).setStatus("Y");
														em.merge(deviceDetails.get(0));
													}

													try {
														vehicleCheckIn.get(0).setTotalTravelDistance(
																(vehicleCheckIn.get(0).getTotalTravelDistance()
																		- liveRoute.get(0).getPlannedDistance())
																		+ totalTravelledDis);
														// vehicleCheckIn.get(0).setNumberOfTrips(vehicleCheckIn.get(0).getNumberOfTrips()+1);
													} catch (Exception e) {
														log.info("updating the travelled and number of trips" + e);
													}

													em.merge(vehicleCheckIn.get(0));
													em.merge(vehicleMasterDetail);
													if (liveRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
														try {
															int a = liveRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId();
															List<EFmFmEscortCheckInPO> escortDetails = vehicleCheckInBO
																	.getParticulaEscortDetailFromEscortId(branchId,
																			liveRoute.get(0).geteFmFmEscortCheckIn()
																					.getEscortCheckInId());
															// escortDetails.get(0).setEscortCheckOutTime(new
															// Date());
															escortDetails.get(0).setStatus("Y");
															em.merge(escortDetails.get(0));
														} catch (Exception e) {
															log.info("Escort Blog Exception" + e);
														}
													}

													vehicleMasterDetail
															.setMonthlyPendingFixedDistance(plannedDis - totalTravelledDis);
													em.merge(vehicleMasterDetail);
													log.info("allRequests" + allRequests.size());
													if (!(allRequests.isEmpty())) {
														for (EFmFmEmployeeTripDetailPO requestDetail : allRequests) {
															List<EFmFmEmployeeTravelRequestPO> activerequest = vehicleCheckInBO
																	.getParticularRequestDetailOnTripComplete(requestDetail.geteFmFmEmployeeTravelRequest()
																					.getRequestId());
															activerequest.get(0).setCompletionStatus("Y");
															em.merge(activerequest.get(0));
															if (!(requestDetail.getEmployeeStatus()
																	.equalsIgnoreCase("completed"))) {
																requestDetail.setEmployeeStatus("completed");
																em.merge(requestDetail);
															}

														}
													}
												} catch (Exception e) {
													log.info("Inside auto tripComplete" + e);
												}
												log.info("First Thread tripcompleted End");
												try{
												List<EFmFmLiveRoutTravelledPO> liveTravelledRoute = vehicleCheckInBO.getCompletedRouteDataFromAssignRouteId(
														assignRoutePO.geteFmFmClientBranchPO().getBranchId(), assignRoutePO.getAssignRouteId());
												EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
												EFmFmAssignRoutePO efmFmAssignRoute = new EFmFmAssignRoutePO();
												eFmFmClientBranchPO.setBranchId(liveTravelledRoute.get(0).geteFmFmClientBranchPO().getBranchId());
												efmFmAssignRoute.setAssignRouteId(liveTravelledRoute.get(0).getEfmFmAssignRoute().getAssignRouteId());
												Thread thread2 = new Thread(new Runnable() {
													@Override
													public void run() {
														for (EFmFmLiveRoutTravelledPO liveRouteDetail : liveTravelledRoute) {
															EFmFmActualRoutTravelledPO actualRoute = new EFmFmActualRoutTravelledPO();
															actualRoute.setCurrentCabLocation(liveRouteDetail.getLiveCurrentCabLocation());
															actualRoute.setCurrentEta(liveRouteDetail.getLiveEta());
															actualRoute.setEfmFmAssignRoute(efmFmAssignRoute);
															actualRoute.seteFmFmClientBranchPO(eFmFmClientBranchPO);
															actualRoute.setEtaInSeconds(liveRouteDetail.getLiveEtaInSeconds());
															actualRoute.setLatitudeLongitude(liveRouteDetail.getLivelatitudeLongitude());
															actualRoute.setSpeed(liveRouteDetail.getLiveSpeed());
															actualRoute.setTravelledDistance(liveRouteDetail.getLiveTravellesDistance());
															actualRoute.setTravelledTime(liveRouteDetail.getLiveTravelledTime());
															em.merge(actualRoute);
															vehicleCheckInBO.deleteParticularActualTravelled(liveRouteDetail.getLiveRouteTravelId());					
														}
													}
												});
												thread2.start();
											}catch(Exception e){
												log.info("table purging"+e);
											}
												log.info("Second Thread tripcompleted End");
//											}
										}
										if (etaFlag) {
											etaFlag = false;
											etaInSeconds = eta;
											driverEta = etaInSec;
											List<EFmFmEmployeeTripDetailPO> employeeTrips = vehicleCheckInBO
													.getParticularTripNonDropEmployeesDetails(liveRoute.get(0).getAssignRouteId());
											if (employeeTrips.isEmpty() || employeeTrips.get(0).getEfmFmAssignRoute().getTripType()
													.equalsIgnoreCase("DROP") || employeeTrips.size() == i) {
												break;
											}
											String currentDateStr = currentDate + " "
													+ employeeTripDetailPO.get(i).geteFmFmEmployeeTravelRequest().getPickUpTime();
											long pickupTime = dateTimeformate.parse(currentDateStr).getTime();
											long timeMax = Math.max(employeeTripDetailPO.get(i).getCabRecheddestinationTime(),
													pickupTime);
											// no show message after pickuptime plus three
											// minutes.
											if (employeeTripDetailPO.get(i).getReachedFlg().equalsIgnoreCase("Y")
													&& !(employeeTripDetailPO.get(i).geteFmFmEmployeeTravelRequest()
															.getRequestType().equalsIgnoreCase("guest"))
													&& currentTime >= (timeMax
															+ (liveRoute.get(0).geteFmFmClientBranchPO().getEmployeeWaitingTime()
																	* 60 * 1000))) {
												log.info(
														"Cab has left Message Triggered  for First employee"
																+ new String(
																		Base64.getDecoder()
																				.decode(employeeTripDetailPO.get(i)
																						.geteFmFmEmployeeTravelRequest()
																						.getEfmFmUserMaster().getMobileNumber()),
																		"utf-8"));
												final int k = i;
												Thread thread1 = new Thread(new Runnable() {
													@Override
													public synchronized void run() {
														try {
															long l1 = System.currentTimeMillis();
															String text = "";
															text = "Sorry you missed us.\nYour ride "
																	+ employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																			.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
																			.getVehicleNumber()
																	+ " has left your "
																	+ employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																			.getTripType()
																	+ " point.\nFor feedback write to us @"
																	+ employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																			.geteFmFmClientBranchPO().getFeedBackEmailId();
															messaging.cabHasLeftMessageForSch(
																	new String(Base64.getDecoder()
																			.decode(String.valueOf(employeeTripDetailPO.get(k)
																					.geteFmFmEmployeeTravelRequest()
																					.getEfmFmUserMaster().getMobileNumber())),
																	"utf-8"), text,
																	employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																			.getRequestType());

															try {
																if (employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																		.getEfmFmUserMaster().getDeviceType().contains("Android")) {
																	pushNotification.notification(employeeTripDetailPO.get(k)
																			.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
																			.getDeviceToken(), text);
																} else {
																	pushNotification.iosPushNotification(employeeTripDetailPO.get(k)
																			.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
																			.getDeviceToken(), text);
																}

															} catch (Exception e) {
																log.info("Cab Left at your pickup point" + e);
															}

															Thread.currentThread().stop();
															long l2 = System.currentTimeMillis();
															log.debug("Time taken by cab left message from gate way for trip Id: "
																	+ employeeTripDetailPO.get(k).getEmpTripId() + " Time "
																	+ (l2 - l1) + "ms");
														} catch (Exception e) {
															try {
																log.info("Error Cab has left Message Triggered  for First employee"
																		+ new String(
																				Base64.getDecoder().decode(employeeTripDetailPO
																						.get(k).geteFmFmEmployeeTravelRequest()
																						.getEfmFmUserMaster().getMobileNumber()),
																				"utf-8"));
															} catch (UnsupportedEncodingException e1) {
																log.info("UnsupportedEncodingException");
															}
															Thread.currentThread().stop();
														}
													}
												});
												thread1.start();
												employeeTripDetailPO.get(i).setBoardedFlg("NO");
												employeeTripDetailPO.get(i).setEmployeeStatus("completed");
												employeeTripDetailPO.get(i).setCabstartFromDestination(new Date().getTime());
												// liveRoute.get(0).setTripUpdateTime(new
												// Date());
												em.merge(liveRoute.get(0));
											}
											final int k = i;
											final String employeeMsgEta = etaInSec;
											// geofence reached message
											if (employeeTripDetailPO.get(k).getReachedFlg().equalsIgnoreCase("N")
													&& totalDistance1 < (liveRoute.get(0).geteFmFmClientBranchPO()
															.getEmployeeAddressgeoFenceArea())) {
												log.info("Sending Reached ETA Message for Trip: "
														+ employeeTripDetailPO.get(k).getEmpTripId());
												Thread thread1 = new Thread(new Runnable() {
													@Override
													public synchronized void run() {
														try {
															String text = "";
															long l1 = System.currentTimeMillis();
															text = "Your ride "
																	+ employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																			.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
																			.getVehicleNumber()
																	+ " has arrived at your pickup point.\nFor feedback write to us @"
																	+ employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																			.geteFmFmClientBranchPO().getFeedBackEmailId();
															messaging.cabReachedMessage(
																	new String(Base64.getDecoder()
																			.decode(String.valueOf(employeeTripDetailPO.get(k)
																					.geteFmFmEmployeeTravelRequest()
																					.getEfmFmUserMaster().getMobileNumber())),
																	"utf-8"), text,
																	employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																			.getRequestType());
															long l2 = System.currentTimeMillis();
															log.debug(
																	"Time taken by Sending Reached ETA Message from gate way for trip Id: "
																			+ employeeTripDetailPO.get(k).getEmpTripId() + " Time "
																			+ (l2 - l1) + "ms");
															try {
																if (employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																		.getEfmFmUserMaster().getDeviceType().contains("Android")) {
																	pushNotification.notification(employeeTripDetailPO.get(k)
																			.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
																			.getDeviceToken(), text);
																} else {
																	pushNotification.iosPushNotification(employeeTripDetailPO.get(k)
																			.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
																			.getDeviceToken(), text);
																}

															} catch (Exception e) {
																log.info("Sending Reached" + e);
															}
															Thread.currentThread().stop();
														} catch (Exception e) {
															log.error("Error Sending Reached ETA Message for Trip: "
																	+ employeeTripDetailPO.get(k).getEmpTripId(), e);
															Thread.currentThread().stop();

														}
													}
												});
												thread1.start();
												employeeTripDetailPO.get(i).setReachedFlg("Y");
												employeeTripDetailPO.get(i).setCabRecheddestinationTime(currentTime);
												employeeTripDetailPO.get(i).setReachedMessageDeliveryDate(new Date());
												employeeTripDetailPO.get(i).setTwoMinuteMessageStatus("Y");
												employeeTripDetailPO.get(i).setTwoMinuteMessageDeliveryDate(new Date());
												liveRoute.get(0).setTripUpdateTime(new Date());
												em.merge(liveRoute.get(0));
											}
											if (employeeTripDetailPO.get(k).getTenMinuteMessageStatus().equalsIgnoreCase("N")
													&& employeeTripDetailPO.get(k).getReachedFlg().equalsIgnoreCase("N")
													&& eta < (liveRoute.get(0).geteFmFmClientBranchPO().getEtaSMS()) * 60) {
												log.info(
														"15 minute Message Triggered from Server for First employee" + new String(
																Base64.getDecoder()
																		.decode(String.valueOf(employeeTripDetailPO.get(k)
																				.geteFmFmEmployeeTravelRequest()
																				.getEfmFmUserMaster().getMobileNumber())),
														"utf-8"));
												Thread thread1 = new Thread(new Runnable() {
													@Override
													public synchronized void run() {
														try {
															long l1 = System.currentTimeMillis();
															if (employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																	.getRequestType().equalsIgnoreCase("guest")) {
															} else {
																messaging.etaMessage(
																		new String(Base64.getDecoder()
																				.decode(String.valueOf(employeeTripDetailPO.get(k)
																						.geteFmFmEmployeeTravelRequest()
																						.getEfmFmUserMaster().getFirstName())),
																		"utf-8"),
																		employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																				.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
																				.getVehicleNumber(),
																		employeeMsgEta,
																		new String(Base64.getDecoder()
																				.decode(String.valueOf(employeeTripDetailPO.get(k)
																						.geteFmFmEmployeeTravelRequest()
																						.getEfmFmUserMaster().getMobileNumber())),
																				"utf-8"),

																employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																		.getRequestType());
															}
															long l2 = System.currentTimeMillis();
															log.debug(
																	"Time taken by Sending 15 minute Message from gate way for first employee trip Id: "
																			+ employeeTripDetailPO.get(k).getEmpTripId() + " Time "
																			+ (l2 - l1) + "ms");
															try {
																String text = "Your ride "
																		+ employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																				.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
																				.getVehicleNumber()
																		+ " will be at your pickup point in the next "
																		+ employeeMsgEta + "\nFor feedback write to us @"
																		+ employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																				.geteFmFmClientBranchPO().getFeedBackEmailId();
																if (employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																		.getEfmFmUserMaster().getDeviceType().contains("Android")) {
																	pushNotification.notification(employeeTripDetailPO.get(k)
																			.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
																			.getDeviceToken(), text);
																} else {
																	pushNotification.iosPushNotification(employeeTripDetailPO.get(k)
																			.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
																			.getDeviceToken(), text);
																}

															} catch (Exception e) {
																log.info("Cab Left at your pickup point" + e);
															}

															Thread.currentThread().stop();
														} catch (Exception e) {
															try {
																log.error(
																		"Error 15 Minute Message Triggering from Server for First Employee"
																				+ new String(
																						Base64.getDecoder()
																								.decode(String.valueOf(
																										employeeTripDetailPO.get(k)
																												.geteFmFmEmployeeTravelRequest()
																												.getEfmFmUserMaster()
																												.getMobileNumber())),
																						"utf-8"));
															} catch (UnsupportedEncodingException e1) {
																log.info("Error" + e);
															}
															Thread.currentThread().stop();

														}
													}
												});
												thread1.start();
												employeeTripDetailPO.get(i).setTenMinuteMessageStatus("Y");
												employeeTripDetailPO.get(i).setTenMinuteMessageDeliveryDate(new Date());
											}

											if (!(actualRouteTravelled.isEmpty())) {
												long delayCalculationTime = pickupTime
														+ (liveRoute.get(0).geteFmFmClientBranchPO().getDelayMessageTime() * 60
																* 1000);
												// after eta 5 minutes in delay
												if (employeeTripDetailPO.get(k).getTenMinuteMessageStatus().equalsIgnoreCase("Y")
														&& employeeTripDetailPO.get(k).getCabDelayMsgStatus().equalsIgnoreCase("N")
														&& employeeTripDetailPO.get(k).getReachedFlg().equalsIgnoreCase("N")
														&& (delayCalculationTime < (new Date().getTime()))) {
													log.info(
															"Cab Delay Message Triggered for First employee from web for second employee"
																	+ new String(Base64.getDecoder()
																			.decode(String.valueOf(employeeTripDetailPO.get(k)
																					.geteFmFmEmployeeTravelRequest()
																					.getEfmFmUserMaster().getMobileNumber())),
																			"utf-8"));
													Thread thread1 = new Thread(new Runnable() {
														@Override
														public synchronized void run() {
															try {
																long l1 = System.currentTimeMillis();
																if (employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																		.getRequestType().equalsIgnoreCase("guest")) {
																} else {
																	messaging.etaMessageWhenCabDelay(
																			new String(Base64.getDecoder()
																					.decode(String.valueOf(employeeTripDetailPO
																							.get(k).geteFmFmEmployeeTravelRequest()
																							.getEfmFmUserMaster().getFirstName())),
																			"utf-8"),
																			employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																					.getEfmFmVehicleCheckIn()
																					.getEfmFmVehicleMaster().getVehicleNumber(),
																			employeeMsgEta,
																			new String(Base64.getDecoder()
																					.decode(String.valueOf(employeeTripDetailPO
																							.get(k).geteFmFmEmployeeTravelRequest()
																							.getEfmFmUserMaster()
																							.getMobileNumber())),
																					"utf-8"),

																	employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																			.getRequestType());
																}
																long l2 = System.currentTimeMillis();
																log.debug(
																		"Time taken by Sending Cab Delay Message from gate way for first employee trip Id: "
																				+ employeeTripDetailPO.get(k).getEmpTripId()
																				+ " Time " + (l2 - l1) + "ms");
																Thread.currentThread().stop();
															} catch (Exception e) {
																try {
																	log.error(
																			"Error Cab Delay Message Triggered for First employee from web for second employee"
																					+ new String(
																							Base64.getDecoder()
																									.decode(String.valueOf(
																											employeeTripDetailPO
																													.get(k)
																													.geteFmFmEmployeeTravelRequest()
																													.getEfmFmUserMaster()
																													.getMobileNumber())),
																							"utf-8"));
																} catch (UnsupportedEncodingException e1) {

																	log.info("error" + e);
																}
																Thread.currentThread().stop();

															}
														}
													});
													thread1.start();
													employeeTripDetailPO.get(i).setCabDelayMsgStatus("Y");
													employeeTripDetailPO.get(i).setCabDelayMsgDeliveryDate(new Date());
												}
											}
										} else {
											if (employeeTripDetailPO.get(i).getReachedFlg().equalsIgnoreCase("N")
													&& totalDistance1 < (liveRoute.get(0).geteFmFmClientBranchPO()
															.getEmployeeAddressgeoFenceArea())) {
												final int k = i;
												log.info("Sending Reached ETA Message for Trip: "
														+ employeeTripDetailPO.get(k).getEmpTripId());
												Thread thread1 = new Thread(new Runnable() {
													@Override
													public synchronized void run() {
														try {
															String text = "";
															long l1 = System.currentTimeMillis();
															text = "Your ride "
																	+ employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																			.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
																			.getVehicleNumber()
																	+ " has arrived at your pickup point.\nFor feedback write to us @"
																	+ employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																			.geteFmFmClientBranchPO().getFeedBackEmailId();
															messaging.cabReachedMessage(
																	new String(Base64.getDecoder()
																			.decode(String.valueOf(employeeTripDetailPO.get(k)
																					.geteFmFmEmployeeTravelRequest()
																					.getEfmFmUserMaster().getMobileNumber())),
																	"utf-8"), text,
																	employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																			.getRequestType());
															long l2 = System.currentTimeMillis();
															log.debug(
																	"Time taken by Sending Reached ETA Message from gate way for trip Id: "
																			+ employeeTripDetailPO.get(k).getEmpTripId() + " Time "
																			+ (l2 - l1) + "ms");
															try {
																if (employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																		.getEfmFmUserMaster().getDeviceType().contains("Android")) {
																	pushNotification.notification(employeeTripDetailPO.get(k)
																			.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
																			.getDeviceToken(), text);
																} else {
																	pushNotification.iosPushNotification(employeeTripDetailPO.get(k)
																			.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
																			.getDeviceToken(), text);
																}

															} catch (Exception e) {
																log.info("Sending Reached" + e);
															}
															Thread.currentThread().stop();
														} catch (Exception e) {
															log.error("Error Sending Reached ETA Message for Trip: "
																	+ employeeTripDetailPO.get(k).getEmpTripId(), e);
															Thread.currentThread().stop();

														}
													}
												});
												thread1.start();
												employeeTripDetailPO.get(i).setReachedFlg("Y");
												employeeTripDetailPO.get(i).setCabRecheddestinationTime(currentTime);
												employeeTripDetailPO.get(i).setReachedMessageDeliveryDate(new Date());
												employeeTripDetailPO.get(i).setTwoMinuteMessageStatus("Y");
												employeeTripDetailPO.get(i).setTwoMinuteMessageDeliveryDate(new Date());
												liveRoute.get(0).setTripUpdateTime(new Date());
												em.merge(liveRoute.get(0));
											}
											final String employeeMsgEta = etaInSec;
											final int k = i;
											if (employeeTripDetailPO.get(i).getTenMinuteMessageStatus().equalsIgnoreCase("N")
													&& employeeTripDetailPO.get(i).getReachedFlg().equalsIgnoreCase("N")
													&& eta < (liveRoute.get(0).geteFmFmClientBranchPO().getEtaSMS()) * 60) {
												log.info(
														"15 minute Message Triggered  for second employee" + new String(
																Base64.getDecoder()
																		.decode(String.valueOf(employeeTripDetailPO.get(k)
																				.geteFmFmEmployeeTravelRequest()
																				.getEfmFmUserMaster().getMobileNumber())),
														"utf-8"));
												Thread thread1 = new Thread(new Runnable() {
													@Override
													public synchronized void run() {

														try {
															long l1 = System.currentTimeMillis();
															MessagingService messaging = new MessagingService();
															if (employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																	.getRequestType().equalsIgnoreCase("guest")) {
															} else {
																messaging.etaMessage(new String(
																		Base64.getDecoder()
																				.decode(String.valueOf(employeeTripDetailPO.get(k)
																						.geteFmFmEmployeeTravelRequest()
																						.getEfmFmUserMaster().getFirstName())),
																		"utf-8"),

																employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																		.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
																		.getVehicleNumber(),
																		employeeMsgEta,
																		new String(Base64.getDecoder()
																				.decode(String.valueOf(employeeTripDetailPO.get(k)
																						.geteFmFmEmployeeTravelRequest()
																						.getEfmFmUserMaster().getMobileNumber())),
																				"utf-8"),

																employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																		.getRequestType());
															}
															try {
																String text = "Your ride "
																		+ employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																				.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
																				.getVehicleNumber()
																		+ " will be at your pickup point in the next "
																		+ employeeMsgEta + "\nFor feedback write to us @"
																		+ employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																				.geteFmFmClientBranchPO().getFeedBackEmailId();

																if (employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																		.getEfmFmUserMaster().getDeviceType().contains("Android")) {
																	pushNotification.notification(employeeTripDetailPO.get(k)
																			.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
																			.getDeviceToken(), text);
																} else {
																	pushNotification.iosPushNotification(employeeTripDetailPO.get(k)
																			.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
																			.getDeviceToken(), text);
																}

															} catch (Exception e) {
																log.info("15 minute Message" + e);
															}
															Thread.currentThread().stop();
															long l2 = System.currentTimeMillis();
															log.debug(
																	"Time taken by Sending 15 minute Message from gate way for next employee trip Id: "
																			+ employeeTripDetailPO.get(k).getEmpTripId() + " Time "
																			+ (l2 - l1) + "ms");

														} catch (Exception e) {
															try {
																log.error(
																		"Error 15 Minute Message Triggering from Server for second employee"
																				+ new String(
																						Base64.getDecoder()
																								.decode(String.valueOf(
																										employeeTripDetailPO.get(k)
																												.geteFmFmEmployeeTravelRequest()
																												.getEfmFmUserMaster()
																												.getMobileNumber())),
																						"utf-8"));
															} catch (UnsupportedEncodingException e1) {

																log.info("error" + e1);
															}
															Thread.currentThread().stop();
														}
													}
												});
												thread1.start();
												employeeTripDetailPO.get(i).setTenMinuteMessageStatus("Y");
												employeeTripDetailPO.get(i).setTenMinuteMessageDeliveryDate(new Date());
											}
											if (!(actualRouteTravelled.isEmpty())) {
												String currentDateStr = currentDate + " " + employeeTripDetailPO.get(k)
														.geteFmFmEmployeeTravelRequest().getPickUpTime();
												long pickupTime = dateTimeformate.parse(currentDateStr).getTime();

												long delayCalculationTime = pickupTime
														+ (liveRoute.get(0).geteFmFmClientBranchPO().getDelayMessageTime() * 60
																* 1000);
												if (employeeTripDetailPO.get(k).getTenMinuteMessageStatus().equalsIgnoreCase("Y")
														&& employeeTripDetailPO.get(k).getCabDelayMsgStatus().equalsIgnoreCase("N")
														&& employeeTripDetailPO.get(k).getReachedFlg().equalsIgnoreCase("N")
														&& (delayCalculationTime < (new Date().getTime()))) {
													log.info(
															"Cab Delay Message Triggered for second employee from web for second employee"
																	+ new String(Base64.getDecoder()
																			.decode(String.valueOf(employeeTripDetailPO.get(k)
																					.geteFmFmEmployeeTravelRequest()
																					.getEfmFmUserMaster().getMobileNumber())),
																			"utf-8"));
													Thread thread1 = new Thread(new Runnable() {
														@Override
														public synchronized void run() {
															try {
																long l1 = System.currentTimeMillis();
																MessagingService messaging = new MessagingService();
																if (employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																		.getRequestType().equalsIgnoreCase("guest")) {
																} else {
																	messaging.etaMessageWhenCabDelay(new String(
																			Base64.getDecoder()
																					.decode(String.valueOf(employeeTripDetailPO
																							.get(k).geteFmFmEmployeeTravelRequest()
																							.getEfmFmUserMaster().getFirstName())),
																			"utf-8"),

																	employeeTripDetailPO.get(k).getEfmFmAssignRoute()
																			.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
																			.getVehicleNumber(), employeeMsgEta,

																	new String(Base64.getDecoder()
																			.decode(String.valueOf(employeeTripDetailPO.get(k)
																					.geteFmFmEmployeeTravelRequest()
																					.getEfmFmUserMaster().getMobileNumber())),
																			"utf-8"),

																	employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
																			.getRequestType());
																}
																long l2 = System.currentTimeMillis();
																log.debug(
																		"Time taken by Sending Cab Delay Message from gate way for next employee trip Id: "
																				+ employeeTripDetailPO.get(k).getEmpTripId()
																				+ " Time " + (l2 - l1) + "ms");
																Thread.currentThread().stop();
															} catch (Exception e) {
																try {
																	log.info(
																			"Error Cab Delay Message Triggered for second employee from web for second employee"
																					+ new String(
																							Base64.getDecoder()
																									.decode(String.valueOf(
																											employeeTripDetailPO
																													.get(k)
																													.geteFmFmEmployeeTravelRequest()
																													.getEfmFmUserMaster()
																													.getMobileNumber())),
																							"utf-8"));
																} catch (UnsupportedEncodingException e1) {

																	log.info("error" + e1);
																}

																Thread.currentThread().stop();

															}
														}
													});
													thread1.start();
													employeeTripDetailPO.get(i).setCabDelayMsgStatus("Y");
													employeeTripDetailPO.get(i).setCabDelayMsgDeliveryDate(new Date());
												}
											}

										}
										employeeTripDetailPO.get(i).setCurrenETA(etaInSec);
										employeeTripDetailPO.get(i).setGoogleEta(eta);
										em.merge(employeeTripDetailPO.get(i));
									}
								} catch (Exception e) {
									log.info("Error" + e);
								}
							}
						}

						if (driverEta.length() == 0 && driverEta.isEmpty()) {
							if (!(actualRouteTravelled.isEmpty())) {
								driverEta = actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta();
							} else {
								driverEta = "calculating...";
							}
						}
					}					
					//trip Type Pickup if End
					
					List<EFmFmLiveRoutTravelledPO> avaoidingDuplicateValues = vehicleCheckInBO
							.getRouteLastEtaAndDistanceFromAssignRouteId(liveRoute.get(0).getAssignRouteId());
					if (!(avaoidingDuplicateValues.isEmpty()) && avaoidingDuplicateValues.get(avaoidingDuplicateValues.size() - 1)
							.getLivelatitudeLongitude().equalsIgnoreCase(lattitudeLongitude)) {
						avaoidingDuplicateValues.get(avaoidingDuplicateValues.size() - 1).setLiveTravelledTime(new Date());
						em.merge(avaoidingDuplicateValues.get(actualRouteTravelled.size() - 1));
						em.getTransaction().commit();
						em.close();
						return;
					}
					if (Double.parseDouble(speed) > (liveRoute.get(0).geteFmFmClientBranchPO().getMaxSpeed())) {
						EFmFmAlertTypeMasterPO alertTypeMasterPO = new EFmFmAlertTypeMasterPO();
						EFmFmTripAlertsPO eFmFmTripAlertsPO = new EFmFmTripAlertsPO();
						alertTypeMasterPO.setAlertId(10);
						eFmFmTripAlertsPO.setEfmFmAlertTypeMaster(alertTypeMasterPO);
						eFmFmTripAlertsPO.setCreationTime(new Date());
						eFmFmTripAlertsPO.setUpdatedTime(new Date());
						eFmFmTripAlertsPO.setReadFlg("N");
						eFmFmTripAlertsPO.setEfmFmAssignRoute(assignRoutePO);
						eFmFmTripAlertsPO.setUserType("Driver");
						eFmFmTripAlertsPO.setAlertOpenStatus("open");
						eFmFmTripAlertsPO.setAlertClosingDescription("No Action");
						eFmFmTripAlertsPO.setCurrentSpeed(speed);
						eFmFmTripAlertsPO.setStatus("Y");
						em.merge(eFmFmTripAlertsPO);
					}
					EFmFmLiveRoutTravelledPO liveRouteDetail = new EFmFmLiveRoutTravelledPO();
					// saving speed alerts...
					try {
						liveRouteDetail.setLiveTravelledTime(trackingTime);												
					} catch (Exception e) {
						log.info("travelTime Error" + e);
						liveRouteDetail.setLiveTravelledTime(new Date());
					}
					log.info(liveRoute.get(0).getAssignRouteId()+"live route Total distance error*************route travelled Distnace"+liveRoute.get(0).getTravelledDistance());

					// saving speed alerts...
			        if(!(actualRouteTravelled.isEmpty())){
			        	double legWiseDistance=0;
			        	try{
			        		if(liveRoute.get(0).getTripType().equalsIgnoreCase("DROP")){
			        	CalculateDistance dist=new CalculateDistance();
			        	log.info("DROP old LattiLongi");
			        	log.info("DROP new LattiLongi");
			        	legWiseDistance = dist.distance(Double.parseDouble(actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude().split(",")[0]),
			        		Double.parseDouble(actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude().split(",")[1]),
			        		Double.parseDouble(lattitudeLongitude.split(",")[0]),
			                		Double.parseDouble(lattitudeLongitude.split(",")[1]),'K');
			            log.info("DROP Distance"+String.valueOf(legWiseDistance));
			            
			            liveRouteDetail.setLiveTravellesDistance(String.valueOf(legWiseDistance));
			          liveRoute.get(0).setTravelledDistance(liveRoute.get(0).getTravelledDistance()+ Double.valueOf(legWiseDistance));			          
			          em.merge(liveRoute.get(0));
			        		}		        		
			        		// GeoFence on trip Start when vehicle reaches
							// at First Pickup location			        		
			        		else if(liveRoute.get(0).getTripType().equalsIgnoreCase("PICKUP") && (!(employeeTripDetailPO.isEmpty()) && (Geocode.distance(
									new Geocode(employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude()),
									new Geocode(lattitudeLongitude)))<(liveRoute.get(0).geteFmFmClientBranchPO().getEmployeeAddressgeoFenceArea())) || (liveRoute.get(0).getTripType().equalsIgnoreCase("PICKUP") && (liveRoute.get(0).getTravelledDistance()!=0))){
					        	CalculateDistance dist=new CalculateDistance();
					        	log.info("PICKUP old LattiLongi");
					        	log.info("PICKUP new LattiLongi");
					        	legWiseDistance = dist.distance(Double.parseDouble(actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude().split(",")[0]),
					        		Double.parseDouble(actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude().split(",")[1]),
					        		Double.parseDouble(lattitudeLongitude.split(",")[0]),
					                		Double.parseDouble(lattitudeLongitude.split(",")[1]),'K');
					            log.info("PICKUP Distance"+String.valueOf(legWiseDistance));
					            
					            liveRouteDetail.setLiveTravellesDistance(String.valueOf(legWiseDistance));
					          liveRoute.get(0).setTravelledDistance(liveRoute.get(0).getTravelledDistance()+ Double.valueOf(legWiseDistance));			          
					          em.merge(liveRoute.get(0));
					        		
			        		}
			        		
			        	}catch(Exception e){
			        	log.info("Distance Error"+legWiseDistance);
			        	}
			        }
					liveRouteDetail.setEfmFmAssignRoute(assignRoutePO);
					liveRouteDetail.seteFmFmClientBranchPO(clientBranch);
					liveRouteDetail.setLivelatitudeLongitude(lattitudeLongitude);
					liveRouteDetail.setLiveEtaInSeconds(etaInSeconds);
					liveRouteDetail.setLiveSpeed(speed);
					liveRouteDetail.setLiveEta(driverEta);
					if (cabCurrentLocation.length() != 0 && (!(cabCurrentLocation.isEmpty()))){
						liveRouteDetail.setLiveCurrentCabLocation(cabCurrentLocation);
					}else{
						liveRouteDetail.setLiveCurrentCabLocation(address);
					}
					em.merge(liveRouteDetail);				
					em.getTransaction().commit();
					em.close();
					return;					
				//End of location Updater Service Call	
				}
			}
	}catch(Exception e){
    	log.info("Error"+e);
	}
	}
		
	public long driverCheckOutTime(Time hours, int minutes, Date checkIndate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkIndate);
		calendar.add(Calendar.HOUR, hours.getHours());
		calendar.add(Calendar.MINUTE, hours.getMinutes());
		return calendar.getTimeInMillis();
	}

	public static void passingKey(String keyString) throws IOException {
		// Convert the key from 'web safe' base 64 to binary
		keyString = keyString.replace('-', '+');
		keyString = keyString.replace('_', '/');
		System.out.println("Key: " + keyString);
		// this.key = Base64.decode(keyString);
		SchedulingService.key = Base64.getDecoder().decode(keyString);
	}

	public static String signRequest(String path, String query)
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, URISyntaxException {
		// Retrieve the proper URL components to sign
		String resource = path + '?' + query;
		// Get an HMAC-SHA1 signing key from the raw key bytes
		SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
		// Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1
		// key
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(sha1Key);
		// compute the binary signature for the request
		byte[] sigBytes = mac.doFinal(resource.getBytes());
		// base 64 encode the binary signature
		// String signature = Base64.encode(sigBytes);
		String signature = Base64.getEncoder().encodeToString(sigBytes);
		// convert the signature to 'web safe' base 64
		signature = signature.replace('+', '-');
		signature = signature.replace('/', '_');
		return resource + "&signature=" + signature;
	}
	
}