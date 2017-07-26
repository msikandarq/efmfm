package com.newtglobal.eFmFmFleet.services;

import java.io.IOException;
import java.io.OutputStream;
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
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/report")
@Consumes("application/json")
@Produces("application/json")
public class DynamicReportService {
	private static Log log = LogFactory.getLog(DynamicReportService.class);
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	/**
	 *  @param Get
	 *  Get dynamic report based on the User Option
	 *  *@return
	 *  @author Rajan R
	 *  @throws ParseException
	 * @throws UnsupportedEncodingException 
	 */
	@POST
	@Path("/dynamicReport")
	public Response dynamicReport(EFmFmAssignRoutePO assignRoute) throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");					
		List<Map<String, Object>> dynamicReport = new ArrayList<Map<String, Object>>();	
		 log.info("serviceStart -UserId :" + assignRoute.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
	/*	 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
		 		if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoute.getUserId()))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 		
		 		List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(assignRoute.getUserId());
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
				}*/
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");	
		DateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		Date fromDate = formatter.parse(assignRoute.getFromDate());
		Date toDate = formatter.parse(assignRoute.getToDate());
		List<EFmFmAssignRoutePO> allEmployeeTraveledDetails=null;		
		if(assignRoute.getSearchType().equalsIgnoreCase("employee") || assignRoute.getSearchType().equalsIgnoreCase("guest")){
			if(assignRoute.getSearchType().equalsIgnoreCase("employee")){		
			allEmployeeTraveledDetails = iAssignRouteBO.getAllEmployeeDynamicDetails(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()));
		}else if(assignRoute.getSearchType().equalsIgnoreCase("guest")){
			allEmployeeTraveledDetails = iAssignRouteBO.getAllGuestDynamicDetails(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()));
		}		 
		if(!allEmployeeTraveledDetails.isEmpty() && allEmployeeTraveledDetails !=null){
			for(EFmFmAssignRoutePO tripDetails:allEmployeeTraveledDetails){
				Map<String, Object> tripList = new HashMap<String, Object>();				
				/*List<EFmFmEmployeeTripDetailPO> employeeDetails=iCabRequestBO.getParticularTripAllEmployees(tripDetails.getAssignRouteId());*/
				/*if(assignRoute.getRouteIdFlg()==1)*/
				tripList.put("routeId", tripDetails.getAssignRouteId());
				if(assignRoute.getAssignDateFlg()==1)
				tripList.put("assignDate",formatter.format(tripDetails.getCreatedDate()));
				if(assignRoute.getRouteCloseTimeFlg()==1)
				tripList.put("routeCloseTime",dateTimeFormatter.format(tripDetails.getCreatedDate()));
							
				
				if(assignRoute.getEscortIdFlg()==1){
					if(tripDetails.getEscortRequredFlag().equalsIgnoreCase("Y")){
						if(tripDetails.geteFmFmEscortCheckIn() !=null){
							tripList.put("escortId",tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getEscortId());
						}else{
							tripList.put("escortId","required but NA");
						}
					}else{
						tripList.put("escortId","NA");
					}
				}				
				if(assignRoute.getEscortNameFlg()==1){					
					if(tripDetails.getEscortRequredFlag().equalsIgnoreCase("Y")){	
						if(tripDetails.geteFmFmEscortCheckIn() !=null){
							tripList.put("escortName",tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
						}else{
							tripList.put("escortName","required but NA");
						}
					}else{
						tripList.put("escortName","NA");
					}
					
				}
				if(assignRoute.getEscortAddressFlg()==1){
					if(tripDetails.getEscortRequredFlag().equalsIgnoreCase("Y")){
						if(tripDetails.geteFmFmEscortCheckIn() !=null){
							tripList.put("escortAddress",tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getAddress());	
						}else{
							tripList.put("escortAddress","required but NA");
						}
					}else{
						tripList.put("escortAddress","NA");
					}
				}
				if(assignRoute.getEscortMobileNoFlg()==1){
				 if(tripDetails.getEscortRequredFlag().equalsIgnoreCase("Y")){	
					if(tripDetails.geteFmFmEscortCheckIn() !=null){
						tripList.put("escortMobileNo",tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getMobileNumber());
					}else{
						tripList.put("escortMobileNo","required but NA");
					}
					}else{
						tripList.put("escortMobileNo","NA");
					}
				}				
				
				if(assignRoute.getPlannedDistanceFlg()==1)
					tripList.put("plannedDistance", tripDetails.getPlannedDistance());
				
				if(assignRoute.getGpsFlg()==1){					
					try {					
						if (tripDetails.getTravelledDistance() == 0) {
							tripList.put("gpsDistance", "NA");		
						} else {
							String extensionRemoved = Double.toString(tripDetails.getTravelledDistance())
									.split("\\.")[1];
							if (!(extensionRemoved.equalsIgnoreCase("0"))) {
								tripList.put("gpsDistance",
										Math.round((double) tripDetails.getTravelledDistance()));						
							} else {
								tripList.put("gpsDistance", tripDetails.getTravelledDistance());				
							}
						}
					} catch (Exception e) {
						log.info("TripSheet Error" + e);
					}
				}		
				
				if(assignRoute.getShiftTimeFlg()==1)
				tripList.put("shiftTime", tripDetails.getShiftTime() );
				if(assignRoute.getTripTypeFlg()==1)
				tripList.put("tripType", tripDetails.getTripType());
				if(assignRoute.getDriverNameFlg()==1)
				tripList.put("driverName", tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName() );
				if(assignRoute.getVehicleNumberFlg()==1)
				tripList.put("vehicleNumber", tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				if(assignRoute.getRouteNameFlg()==1)
				tripList.put("routeName",tripDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				if(assignRoute.getDriverMobileNumberFlg()==1)
				tripList.put("driverNumber", tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				if(assignRoute.getVendorNameFlg()==1)
					tripList.put("vendorName", tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());					
				if(assignRoute.getDriverIdFlg()==1)
						tripList.put("driverId",tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());		
				if(assignRoute.getCheckInTimeFlg()==1)
					tripList.put("checkInTime", dateTimeFormatter.format(tripDetails.getEfmFmVehicleCheckIn().getCheckInTime()));
				if(assignRoute.getCheckoutTimeFlg()==1){
					if(tripDetails.getEfmFmVehicleCheckIn().getCheckOutTime()==null){
						tripList.put("checkoutTime","Not yet Checked Out");
					}else{
						tripList.put("checkoutTime",dateTimeFormatter.format(tripDetails.getEfmFmVehicleCheckIn().getCheckOutTime()));
					}
				}	
				if(assignRoute.getTotalWorkingHoursFlg()==1){	
					if(tripDetails.getEfmFmVehicleCheckIn().getCheckOutTime() !=null){				
					long workingHoursValue = tripDetails.getEfmFmVehicleCheckIn().getCheckOutTime().getTime() - tripDetails.getEfmFmVehicleCheckIn().getCheckInTime().getTime();
					String workingHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(workingHoursValue),
							TimeUnit.MILLISECONDS.toMinutes(workingHoursValue)
									- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(workingHoursValue)),
							TimeUnit.MILLISECONDS.toSeconds(workingHoursValue)
									- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(workingHoursValue)));
						tripList.put("totalWorkingHours", workingHours);
					}else{
						tripList.put("totalWorkingHours","Not CalCulated");
					}					
				}
				
				if(assignRoute.getTripStartTimeFlg()==1){
					if(tripDetails.getTripStartTime()!=null){
						tripList.put("tripStartTime",dateTimeFormatter.format(tripDetails.getTripStartTime()));
					}else{
						tripList.put("tripStartTime","NA");
					}
				}
				if(assignRoute.getTripEndTimeFlg()==1){
					if(tripDetails.getTripCompleteTime()!=null){
						tripList.put("tripEndTime",dateTimeFormatter.format(tripDetails.getTripCompleteTime()));
					}else{
						tripList.put("tripEndTime","NA");
					}
				}
				if(assignRoute.getDriverDrivingHoursPerTripFlg()==1){
					if(tripDetails.getTripCompleteTime()!=null &&  tripDetails.getTripStartTime() !=null ){
						long millisRouteMilles = tripDetails.getTripCompleteTime().getTime()- tripDetails.getTripStartTime().getTime();
						log.info("millisRouteMilles" + millisRouteMilles);
						String routeTravellHours = String.format("%02d:%02d:%02d",TimeUnit.MILLISECONDS.toHours(millisRouteMilles),
								TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles) - TimeUnit.HOURS
								.toMinutes(TimeUnit.MILLISECONDS.toHours(millisRouteMilles)),
								TimeUnit.MILLISECONDS.toSeconds(millisRouteMilles) - TimeUnit.MINUTES
								.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles)));
						log.info("routeTravellHours" + routeTravellHours);	
						tripList.put("driverDrivingHoursPerTrip", routeTravellHours);
					}else{
						tripList.put("driverDrivingHoursPerTrip","NA");
					}
				}				
				if(assignRoute.getRemarksFlg()==1){
					try{
					  if(tripDetails.getEfmFmVehicleCheckIn().getCheckOutRemarks()!=null && !tripDetails.getEfmFmVehicleCheckIn().getCheckOutRemarks().isEmpty()){
						  tripList.put("remarks",tripDetails.getEfmFmVehicleCheckIn().getCheckOutRemarks());
						}else{
							tripList.put("remarks", "Not given");
						}
					}catch(Exception e){
						tripList.put("remarks", "Not given");
					}
				}				
				/*if(assignRoute.getAllocationMsgFlg()==1){
					if(tripDetails.getAllocationMsgDeliveryDate() !=null){
						tripList.put("allocationMsg", dateTimeFormatter.format(tripDetails.getAllocationMsgDeliveryDate()));
					}else{
						tripList.put("allocationMsg","NA");
					}
				}*/				
				//List<Map<String, Object>> employeeDetailsList = new ArrayList<Map<String, Object>>();
	/*			if(!employeeDetails.isEmpty()){
				for(EFmFmEmployeeTripDetailPO tripEmpDetails:employeeDetails){
					Map<String, Object> employeeList = new HashMap<String, Object>();						
					if(assignRoute.getEmployeeIdFlg()==1)
						employeeList.put("employeeId", tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
					if(assignRoute.getEmployeeNameFlg()==1)
					employeeList.put("empName",new String(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),"utf-8"));
					if(assignRoute.getHostMobileNumberFlg()==1){
						if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber() !=null){
							try {
								employeeList.put("hostMobileNumber",new String
										(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber()),"utf-8"));
							} catch (Exception e) {
								employeeList.put("hostMobileNumber",tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber());
							}
							
						}else{
							employeeList.put("hostMobileNumber","");
						}
					}					
					if(assignRoute.getEmailIdFlg()==1){
						if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmailId() !=null){
							try {
								employeeList.put("empEmailId",new String
										(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmailId()),"utf-8"));
							} catch (Exception e) {
								employeeList.put("empEmailId",tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmailId());
							}
							
						}else{
							employeeList.put("empEmailId","");
						}
					}					
					if(assignRoute.getEmployeeMobileNoFlg()==1){
						if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber() !=null){
							try {
								employeeList.put("mobileNumber",new String
										(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber()),"utf-8"));
							} catch (Exception e) {
								employeeList.put("mobileNumber",tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber());
							}
							
						}else{
							employeeList.put("mobileNumber","");
						}
					}						
					if(assignRoute.getEmpLoacationFlg()==1)
						employeeList.put("employeeLocation", tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude());
					
					if(assignRoute.getFifteenMinuteMsgFlg()==1){
						if(tripEmpDetails.getTenMinuteMessageDeliveryDate()!=null){
							tripList.put("fifteenMsg", dateTimeFormatter.format(tripEmpDetails.getTenMinuteMessageDeliveryDate()));
						}else{
							tripList.put("fifteenMsg","NA");
						}
						
					}					
					if(assignRoute.getNoshowMsgFlg()==1){
						if (!(tripEmpDetails.getCabstartFromDestination() == 0)) {
								tripList.put("noShowMsg",dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
							}						
					}
					if(assignRoute.getReachedMsgFlg()==1){
						if(tripEmpDetails.getReachedMessageDeliveryDate()!=null){
							tripList.put("reachedMsg", dateTimeFormatter.format(tripEmpDetails.getReachedMessageDeliveryDate()));
						}else{
							tripList.put("reachedMsg","NA");
						}						
					}
					
					if(assignRoute.getCabDelayMsgFlg()==1){
						if(tripEmpDetails.getCabDelayMsgDeliveryDate()!=null){
							tripList.put("cabDelayMsg", dateTimeFormatter.format(tripEmpDetails.getCabDelayMsgDeliveryDate()));
						}else{
							tripList.put("cabDelayMsg","NA");
						}
						
					}	
					if(assignRoute.getCabReachedTimeFlg()==1){						
						if (!(tripEmpDetails.getCabstartFromDestination() == 0)) {
							tripList.put("cabReachedTime",
									dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
							}						
					}
					if(assignRoute.getBoardingStatusFlg()==1){						
						if(tripEmpDetails.getBoardedFlg().equalsIgnoreCase("N")){
							tripList.put("boardingStatus","Allocated");
						}else if(tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO")){
							tripList.put("boardingStatus","Allocated");
						}else if(tripEmpDetails.getBoardedFlg().equalsIgnoreCase("B")){
							tripList.put("boardingStatus","Allocated");
						}else if(tripEmpDetails.getBoardedFlg().equalsIgnoreCase("D")){
							tripList.put("boardingStatus","dropped");
						}						
					}
					if(assignRoute.getBoardingTimeFlg()==1){												
							if (tripDetails.getTripType().equalsIgnoreCase("PICKUP")) {
								if(assignRoute.getPickUpTimeFlg()==1)
									tripList.put("pickUpTime", tripEmpDetails.geteFmFmEmployeeTravelRequest().getPickUpTime());	
								try {									
									if (!(tripEmpDetails.getCabstartFromDestination() == 0)
											|| !(tripEmpDetails.getPickedUpDateAndTime() == 0)) {
										if (!(tripEmpDetails.getCabstartFromDestination() == 0)
												&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO"))) {
											tripList.put("boardingTime",
													dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
										}
										if (!(tripEmpDetails.getPickedUpDateAndTime() == 0)
												&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("B"))) {
											tripList.put("boardingTime",
													dateFormatter.format(new Date(tripEmpDetails.getPickedUpDateAndTime())));
										}
									} else {
										tripList.put("boardingTime", "0");
	
									}
								} catch (Exception e) {
									tripList.put("boardingTime", "0");						
									
								}
							}
							if (tripDetails.getTripType().equalsIgnoreCase("DROP")) {
								try {
									if (!(tripEmpDetails.getCabstartFromDestination() == 0)
											|| !(tripEmpDetails.getPickedUpDateAndTime() == 0)) {
										if (!(tripEmpDetails.getCabstartFromDestination() == 0)
												&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO"))) {
											tripList.put("boardingTime",
													dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
										}
										if (!(tripEmpDetails.getPickedUpDateAndTime() == 0)
												&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("D"))) {
											tripList.put("boardingTime",
													dateFormatter.format(new Date(tripEmpDetails.getPickedUpDateAndTime())));
										}
									} else {
										tripList.put("boardingTime", "0");
	
									}
								} catch (Exception e) {
									tripList.put("boardingTime", "0");
									log.info("Error in drop type" + e);
								}
							}				
					}									
					employeeDetailsList.add(employeeList);
					tripList.put("employeeDetails",employeeDetailsList);
				}
			}*/
					dynamicReport.add(tripList);
			}
		}		
		}else if(assignRoute.getSearchType().equalsIgnoreCase("driverDetails")){					
				List<Date> selectedDates = new ArrayList<Date>();
				if(assignRoute.getVendorIdSelected().equalsIgnoreCase("All")){
					selectedDates = iAssignRouteBO.getAllTripsByDistinctDates(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()));
				}else{
					selectedDates = iAssignRouteBO.getAllTripsByDistinctDatesByVendor(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()),assignRoute.getVendorIdSelected());
				}				
				if ((!(selectedDates.isEmpty())) || selectedDates.size() != 0) {
					for (Date tripdates : selectedDates) {
						List<EFmFmAssignRoutePO> allTripDetails=null;
						if(assignRoute.getVendorIdSelected().equalsIgnoreCase("All")){
							allTripDetails = iAssignRouteBO.getAllTripByDate(tripdates, tripdates,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()));
						}else{
							allTripDetails = iAssignRouteBO.getAllTripByDateByVendor(tripdates, tripdates,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()),assignRoute.getVendorIdSelected());
						}
						
						if ((!(allTripDetails.isEmpty())) || allTripDetails.size() != 0) {
							List<Object> dateAndDriverId = new ArrayList<Object>();
							for (EFmFmAssignRoutePO trips : allTripDetails) {
								List<Map<String, Object>> driverDrivingReportList = new ArrayList<Map<String, Object>>();
								Map<String, Object> driverReport = new HashMap<String, Object>();
								if (!(dateAndDriverId.contains(dateFormatter.format(trips.getTripAssignDate()))
										&& dateAndDriverId.contains(
												trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId()))) {
									List<EFmFmAssignRoutePO> allTripDetailsByDriverId=null;
									if(assignRoute.getVendorIdSelected().equalsIgnoreCase("All")){
											allTripDetailsByDriverId = iAssignRouteBO.getAllTripsByDatesAndDriverId(tripdates, tripdates,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()),trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
									}else{
										allTripDetailsByDriverId = iAssignRouteBO.getAllTripsByDatesAndDriverIdByVendor(tripdates, tripdates,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()),trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId(),assignRoute.getVendorIdSelected());
									}
									System.out.println("size" + allTripDetailsByDriverId.size() + "DATE"
											+ dateFormatter.format(allTripDetailsByDriverId.get(0).getTripAssignDate()));
									long millis = 0;
									for (EFmFmAssignRoutePO drivertrips : allTripDetailsByDriverId) {
										Map<String, Object> driverDrivingReport = new HashMap<String, Object>();
										dateAndDriverId.add(dateFormatter.format(drivertrips.getTripAssignDate()));
										dateAndDriverId
												.add(drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
										if(assignRoute.getAssignDateFlg()==1)
										driverDrivingReport.put("assignDate", formatter.format(drivertrips.getTripAssignDate()));
												
										if(assignRoute.getTripStartTimeFlg()==1){
											if(drivertrips.getTripStartTime()!=null){
												driverDrivingReport.put("tripStartTime",dateTimeFormatter.format(drivertrips.getTripStartTime()));
											}else{
												driverDrivingReport.put("tripStartTime","NA");
											}
										}
										if(assignRoute.getTripEndTimeFlg()==1){
											if(drivertrips.getTripCompleteTime()!=null){
												driverDrivingReport.put("tripEndTime",dateTimeFormatter.format(drivertrips.getTripCompleteTime()));
											}else{
												driverDrivingReport.put("tripEndTime","NA");
											}
										}		
																
										if(assignRoute.getCheckInTimeFlg()==1){
											driverDrivingReport.put("checkInTime", dateFormatter.format(drivertrips.getEfmFmVehicleCheckIn().getCheckInTime()));
											driverReport.put("checkInTime", dateFormatter.format(drivertrips.getEfmFmVehicleCheckIn().getCheckInTime()));
										}
										if(assignRoute.getRemarksFlg()==1){
											try{
											  if(drivertrips.getEfmFmVehicleCheckIn().getCheckOutRemarks()!=null && !drivertrips.getEfmFmVehicleCheckIn().getCheckOutRemarks().isEmpty()){
												  driverDrivingReport.put("remarks",drivertrips.getEfmFmVehicleCheckIn().getCheckOutRemarks());
												   driverReport.put("remarks",drivertrips.getEfmFmVehicleCheckIn().getCheckOutRemarks());
												}else{
													driverDrivingReport.put("remarks", "Not given");
													driverReport.put("remarks", "Not given");
													
												}
											}catch(Exception e){
												driverDrivingReport.put("remarks", "Not given");
												driverReport.put("remarks", "Not given");
											}
										}	
										
										if(assignRoute.getCheckoutTimeFlg()==1){
											if(drivertrips.getEfmFmVehicleCheckIn().getCheckOutTime()==null){
												driverDrivingReport.put("checkoutTime","Not yet Checked Out");
												driverReport.put("checkoutTime","Not yet Checked Out");
											}else{
												driverDrivingReport.put("checkoutTime", dateFormatter.format(drivertrips.getEfmFmVehicleCheckIn().getCheckOutTime()));
												driverReport.put("checkoutTime", dateFormatter.format(drivertrips.getEfmFmVehicleCheckIn().getCheckOutTime()));
											}
										}	
										if(assignRoute.getTotalWorkingHoursFlg()==1){
											if(drivertrips.getEfmFmVehicleCheckIn().getCheckOutTime()!=null){
												long workingHoursValue = drivertrips.getEfmFmVehicleCheckIn().getCheckOutTime().getTime() - drivertrips.getEfmFmVehicleCheckIn().getCheckInTime().getTime();
												String workingHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(workingHoursValue),
														TimeUnit.MILLISECONDS.toMinutes(workingHoursValue)
																- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(workingHoursValue)),
														TimeUnit.MILLISECONDS.toSeconds(workingHoursValue)
																- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(workingHoursValue)));										
												driverReport.put("totalWorkingHours", workingHours);
												driverDrivingReport.put("totalWorkingHours", workingHours);
											}else{
												driverReport.put("totalWorkingHours","Not yetChecked Out");
												driverDrivingReport.put("totalWorkingHours","Not yetChecked Out");												
											}
										}										
										if(assignRoute.getVehicleNumberFlg()==1)
										driverDrivingReport.put("vehicleNumber", drivertrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
										millis += drivertrips.getTripCompleteTime().getTime()- drivertrips.getTripStartTime().getTime();
										long millisRouteMilles = drivertrips.getTripCompleteTime().getTime()- drivertrips.getTripStartTime().getTime();
										log.info("millisRouteMilles" + millisRouteMilles);
										String routeTravellHours = String.format("%02d:%02d:%02d",TimeUnit.MILLISECONDS.toHours(millisRouteMilles),
												TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles) - TimeUnit.HOURS
												.toMinutes(TimeUnit.MILLISECONDS.toHours(millisRouteMilles)),
												TimeUnit.MILLISECONDS.toSeconds(millisRouteMilles) - TimeUnit.MINUTES
												.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles)));
										log.info("routeTravellHours" + routeTravellHours);
										if(assignRoute.getDriverDrivingHoursPerTripFlg()==1)
										driverDrivingReport.put("driverDrivingHoursPerTrip", routeTravellHours);
										if(assignRoute.getVendorNameFlg()==1)
										driverDrivingReport.put("vendorName", drivertrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
										if(assignRoute.getDriverNameFlg()==1)
										driverDrivingReport.put("driverName",drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
										if(assignRoute.getDriverIdFlg()==1)
										driverDrivingReport.put("driverId",drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
										if(assignRoute.getTripTypeFlg()==1)
										driverDrivingReport.put("tripType", drivertrips.getTripType());
										if(assignRoute.getRouteNameFlg()==1)										
										driverDrivingReport.put("routeName",drivertrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
										if(assignRoute.getRouteIdFlg()==1)
										driverDrivingReport.put("routeId",drivertrips.getAssignRouteId());

										driverDrivingReportList.add(driverDrivingReport);
									}									
									String travellHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
											TimeUnit.MILLISECONDS.toMinutes(millis)
													- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
											TimeUnit.MILLISECONDS.toSeconds(millis)
													- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
									driverReport.put("tripCount", allTripDetailsByDriverId.size());
									if(assignRoute.getTotalDrivingHoursFlg()==1)
									driverReport.put("totalDrivingHours", travellHours);
									driverReport.put("tripsDetails", driverDrivingReportList);
									dynamicReport.add(driverReport);
								}

							}
						}
					}

				}			
		}
		 log.info("serviceEnd -UserId :" + assignRoute.getUserId());
		return Response.ok(dynamicReport, MediaType.APPLICATION_JSON).build();
	}
	
	
	@POST
	@Path("/dynamicReportEmployeeDetails")
	public Response dynamicReportEmployeeDetails(EFmFmAssignRoutePO assignRoute) throws ParseException, UnsupportedEncodingException {		
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoute.getUserId()))){
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(assignRoute.getUserId());
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
		 log.info("serviceStart -UserId :" + assignRoute.getUserId());
		DateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		List<EFmFmEmployeeTripDetailPO> employeeDetails=iCabRequestBO.getParticularTripAllEmployees(assignRoute.getAssignRouteId());				
		List<Map<String, Object>> employeeDetailsList = new ArrayList<Map<String, Object>>();
		if(!employeeDetails.isEmpty()){
		for(EFmFmEmployeeTripDetailPO tripEmpDetails:employeeDetails){
			Map<String, Object> employeeList = new HashMap<String, Object>();						
			if(assignRoute.getEmployeeIdFlg()==1)
				employeeList.put("employeeId",tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
			if(assignRoute.getEmployeeNameFlg()==1)
			employeeList.put("empName",new String(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),"utf-8"));
			if(assignRoute.getHostMobileNumberFlg()==1){
				if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber() !=null){
					try {
						employeeList.put("hostMobileNumber",new String
								(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber()),"utf-8"));
					} catch (Exception e) {
						employeeList.put("hostMobileNumber",tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber());
					}
					
				}else{
					employeeList.put("hostMobileNumber","NA");
				}
			}					
			if(assignRoute.getEmailIdFlg()==1){
				if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmailId() !=null){
					try {
						employeeList.put("empEmailId",new String
								(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmailId()),"utf-8"));
					} catch (Exception e) {
						employeeList.put("empEmailId",tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmailId());
					}
					
				}else{
					employeeList.put("empEmailId","NA");
				}
			}					
			if(assignRoute.getEmployeeMobileNoFlg()==1){
				if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber() !=null){
					try {
						employeeList.put("mobileNumber",new String
								(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber()),"utf-8"));
					} catch (Exception e) {
						employeeList.put("mobileNumber",tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber());
					}
					
				}else{
					employeeList.put("mobileNumber","NA");
				}
			}
				
			if(assignRoute.getEmpLoacationFlg()==1){
				if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude()!=null){
					employeeList.put("employeeLocation", tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude());
				}else{
					employeeList.put("employeeLocation","NA");
				}
			}			
			if(assignRoute.getFifteenMinuteMsgFlg()==1){
				if(tripEmpDetails.getTenMinuteMessageDeliveryDate()!=null){
					employeeList.put("fifteenMsg", dateTimeFormatter.format(tripEmpDetails.getTenMinuteMessageDeliveryDate()));
				}else{
					employeeList.put("fifteenMsg","NA");
				}
				
			}					
			if(assignRoute.getNoshowMsgFlg()==1){
				if (!(tripEmpDetails.getCabstartFromDestination() == 0)) {
					employeeList.put("noShowMsg",dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
					}						
			}
			if(assignRoute.getReachedMsgFlg()==1){
				if(tripEmpDetails.getReachedMessageDeliveryDate()!=null){
					employeeList.put("reachedMsg", dateTimeFormatter.format(tripEmpDetails.getReachedMessageDeliveryDate()));
				}else{
					employeeList.put("reachedMsg","NA");
				}						
			}			
			if(assignRoute.getAllocationMsgFlg()==1){
				if(tripEmpDetails.getEfmFmAssignRoute().getAllocationMsgDeliveryDate() !=null){
					employeeList.put("allocationMsg", dateTimeFormatter.format(tripEmpDetails.getEfmFmAssignRoute().getAllocationMsgDeliveryDate()));
				}else{
					employeeList.put("allocationMsg","NA");
				}
			}			
			if(assignRoute.getCabDelayMsgFlg()==1){
				if(tripEmpDetails.getCabDelayMsgDeliveryDate()!=null){
					employeeList.put("cabDelayMsg", dateTimeFormatter.format(tripEmpDetails.getCabDelayMsgDeliveryDate()));
				}else{
					employeeList.put("cabDelayMsg","NA");
				}				
			}	
			if(assignRoute.getCabReachedTimeFlg()==1){						
				if (!(tripEmpDetails.getCabstartFromDestination() == 0)) {
					employeeList.put("cabReachedTime",
							dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
					}						
			}		
			if(assignRoute.getBoardingStatusFlg()==1){
				
				if (tripEmpDetails.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {					
					if (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("B")) {
						employeeList.put("boardingStatus", "Boarded");
					} else if (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO")) {
						employeeList.put("boardingStatus", "No Show");
					} else if (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("N")) {
						employeeList.put("boardingStatus", "Yet to PickUp");
					}
				}
				if (tripEmpDetails.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
					if (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("D")) {
						employeeList.put("boardingStatus", "Dropped");
					} else if (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO")) {
						employeeList.put("boardingStatus", "No Show");
					} else if (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("N")) {
						employeeList.put("boardingStatus", "Yet to Drop");
					}
				}
				
			}			
			if(assignRoute.getBoardingTimeFlg()==1){												
					if (tripEmpDetails.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
						if(assignRoute.getPickUpTimeFlg()==1)
							employeeList.put("pickUpTime", tripEmpDetails.geteFmFmEmployeeTravelRequest().getPickUpTime());	
						try {									
							if (!(tripEmpDetails.getCabstartFromDestination() == 0)
									|| !(tripEmpDetails.getPickedUpDateAndTime() == 0)) {
								if (!(tripEmpDetails.getCabstartFromDestination() == 0)
										&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO"))) {
									employeeList.put("boardingTime",
											dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
								}
								if (!(tripEmpDetails.getPickedUpDateAndTime() == 0)
										&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("B"))) {
									employeeList.put("boardingTime",
											dateFormatter.format(new Date(tripEmpDetails.getPickedUpDateAndTime())));
								}
							} else {
								employeeList.put("boardingTime", "0");

							}
						} catch (Exception e) {
							employeeList.put("boardingTime", "0");						
							
						}
					}
					if (tripEmpDetails.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
						try {
							if (!(tripEmpDetails.getCabstartFromDestination() == 0)
									|| !(tripEmpDetails.getPickedUpDateAndTime() == 0)) {
								if (!(tripEmpDetails.getCabstartFromDestination() == 0)
										&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO"))) {
									employeeList.put("boardingTime",
											dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
								}
								if (!(tripEmpDetails.getPickedUpDateAndTime() == 0)
										&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("D"))) {
									employeeList.put("boardingTime",
											dateFormatter.format(new Date(tripEmpDetails.getPickedUpDateAndTime())));
								}
							} else {
								employeeList.put("boardingTime", "0");

							}
						} catch (Exception e) {
							employeeList.put("boardingTime", "0");
							log.info("Error in drop type" + e);
						}
					}				
			}									
			employeeDetailsList.add(employeeList);			
		}
	}
		 log.info("serviceEnd -UserId :" + assignRoute.getUserId());
		return Response.ok(employeeDetailsList, MediaType.APPLICATION_JSON).build();
	}
	
	
	
	/*Dont USe*/
	/*@POST
	@Path("/dynamicReport")
	public Response dynamicReport(EFmFmAssignRoutePO assignRoute) throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");		
		List<Map<String, Object>> dynamicReport = new ArrayList<Map<String, Object>>();		
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");	
		DateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		Date fromDate = formatter.parse(assignRoute.getFromDate());
		Date toDate = formatter.parse(assignRoute.getToDate());
		List<EFmFmAssignRoutePO> allEmployeeTraveledDetails=null;		
		if(assignRoute.getSearchType().equalsIgnoreCase("employee")){		
			allEmployeeTraveledDetails = iAssignRouteBO.getAllEmployeeDynamicDetails(fromDate, toDate,assignRoute.geteFmFmClientBranchPO().getBranchId());
		}else if(assignRoute.getSearchType().equalsIgnoreCase("guest")){
			allEmployeeTraveledDetails = iAssignRouteBO.getAllGuestDynamicDetails(fromDate, toDate,assignRoute.geteFmFmClientBranchPO().getBranchId());
		}		 
		if(!allEmployeeTraveledDetails.isEmpty() && allEmployeeTraveledDetails !=null){
			for(EFmFmAssignRoutePO tripDetails:allEmployeeTraveledDetails){
				Map<String, Object> tripList = new HashMap<String, Object>();				
				List<EFmFmEmployeeTripDetailPO> employeeDetails=iCabRequestBO.getParticularTripAllEmployees(tripDetails.getAssignRouteId());
				if(assignRoute.getRouteIdFlg()==1)
				tripList.put("routeId", tripDetails.getAssignRouteId());
				if(assignRoute.getAssignDateFlg()==1)
				tripList.put("assignDate",formatter.format(tripDetails.getCreatedDate()));
				if(assignRoute.getRouteCloseTimeFlg()==1)
				tripList.put("routeCloseTime",dateTimeFormatter.format(tripDetails.getTripAssignDate()));
				if(assignRoute.getTripStartTimeFlg()==1)
				tripList.put("tripStartTime",dateTimeFormatter.format(tripDetails.getTripStartTime()));	
				if(assignRoute.getTripEndTimeFlg()==1)
				tripList.put("tripEndTime",dateTimeFormatter.format(tripDetails.getTripCompleteTime()));				
				if(assignRoute.getEscortFlg()==1){
					if(assignRoute.getEscortRequredFlag().equalsIgnoreCase("Y")){
						tripList.put("escortDetails",tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getEscortId() +"::"+tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
					}
				}			
				if(assignRoute.getPlannedDistanceFlg()==1)
					tripList.put("plannedDistance", tripDetails.getPlannedDistance());
				
				if(assignRoute.getGpsFlg()==1){					
					try {					
						if (tripDetails.getTravelledDistance() == 0) {
							tripList.put("gpsDistance", "NA");		
						} else {
							String extensionRemoved = Double.toString(tripDetails.getTravelledDistance())
									.split("\\.")[1];
							if (!(extensionRemoved.equalsIgnoreCase("0"))) {
								tripList.put("gpsDistance",
										Math.round((double) tripDetails.getTravelledDistance()));						
							} else {
								tripList.put("gpsDistance", tripDetails.getTravelledDistance());				
							}
						}
					} catch (Exception e) {
						log.info("TripSheet Error" + e);
					}
				}		
				
				if(assignRoute.getShiftTimeFlg()==1)
				tripList.put("shiftTime", tripDetails.getShiftTime() );
				if(assignRoute.getTripTypeFlg()==1)
				tripList.put("tripType", tripDetails.getTripType());
				if(assignRoute.getDriverNameFlg()==1)
				tripList.put("driverName", tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName() );
				if(assignRoute.getVehicleNumberFlg()==1)
				tripList.put("vehicleNumber", tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				if(assignRoute.getRouteNameFlg()==1)
				tripList.put("routeName",tripDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				if(assignRoute.getDriverMobileNumberFlg()==1)
				tripList.put("driverNumber", tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				if(assignRoute.getVendorNumberFlg()==1)
					tripList.put("vendorName", tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());					
				if(assignRoute.getDriverIdFlg()==1)
						tripList.put("driverId",tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());		
				if(assignRoute.getCheckInTimeFlg()==1)
					tripList.put("checkInTime", dateTimeFormatter.format(tripDetails.getEfmFmVehicleCheckIn().getCheckInTime()));
				if(assignRoute.getCheckoutTimeFlg()==1){
					if(tripDetails.getEfmFmVehicleCheckIn().getCheckOutTime()==null){
						tripList.put("checkoutTime","Not yet Checked Out");
					}else{
						tripList.put("checkoutTime",dateTimeFormatter.format(tripDetails.getEfmFmVehicleCheckIn().getCheckOutTime()));
					}
				}	
				if(assignRoute.getTotalWorkingHours()==1){	
					if(tripDetails.getEfmFmVehicleCheckIn().getCheckOutTime() !=null){				
					long workingHoursValue = tripDetails.getEfmFmVehicleCheckIn().getCheckOutTime().getTime() - tripDetails.getEfmFmVehicleCheckIn().getCheckInTime().getTime();
					String workingHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(workingHoursValue),
							TimeUnit.MILLISECONDS.toMinutes(workingHoursValue)
									- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(workingHoursValue)),
							TimeUnit.MILLISECONDS.toSeconds(workingHoursValue)
									- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(workingHoursValue)));
						tripList.put("totalWorkingHours", workingHours);
					}else{
						tripList.put("totalWorkingHours","Not CalCulated");
					}					
				}
				if(assignRoute.getRemarksFlg()==1){
					try{
						tripList.put("remarks",tripDetails.getEfmFmVehicleCheckIn().getCheckOutRemarks());
					}catch(Exception e){
						tripList.put("remarks", "Not given");
					}
				}				
				if(assignRoute.getAllocationMsgFlg()==1){
					if(tripDetails.getAllocationMsgDeliveryDate() !=null){
						tripList.put("allocationMsg", dateTimeFormatter.format(tripDetails.getAllocationMsgDeliveryDate()));
					}else{
						tripList.put("allocationMsg","NA");
					}
				}				
				List<Map<String, Object>> employeeDetailsList = new ArrayList<Map<String, Object>>();
				if(!employeeDetails.isEmpty()){
				for(EFmFmEmployeeTripDetailPO tripEmpDetails:employeeDetails){
					Map<String, Object> employeeList = new HashMap<String, Object>();						
					if(assignRoute.getEmployeeIdFlg()==1)
						employeeList.put("employeeId", tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
					if(assignRoute.getEmployeeNameFlg()==1)
					employeeList.put("empName",new String(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),"utf-8"));
					if(assignRoute.getHostMobileNumberFlg()==1){
						if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber() !=null){
							try {
								employeeList.put("hostMobileNumber",new String
										(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber()),"utf-8"));
							} catch (Exception e) {
								employeeList.put("hostMobileNumber",tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber());
							}
							
						}else{
							employeeList.put("hostMobileNumber","");
						}
					}					
					if(assignRoute.getEmailIdFlg()==1){
						if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmailId() !=null){
							try {
								employeeList.put("empEmailId",new String
										(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmailId()),"utf-8"));
							} catch (Exception e) {
								employeeList.put("empEmailId",tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmailId());
							}
							
						}else{
							employeeList.put("empEmailId","");
						}
					}					
					if(assignRoute.getEmployeeMobileNoFlg()==1){
						if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber() !=null){
							try {
								employeeList.put("mobileNumber",new String
										(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber()),"utf-8"));
							} catch (Exception e) {
								employeeList.put("mobileNumber",tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber());
							}
							
						}else{
							employeeList.put("mobileNumber","");
						}
					}
						
					if(assignRoute.getEmpLoacationFlg()==1)
						employeeList.put("employeeLocation", tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude());
					
					if(assignRoute.getFifteenMinuteMsgFlg()==1){
						if(tripEmpDetails.getTenMinuteMessageDeliveryDate()!=null){
							tripList.put("fifteenMsg", dateTimeFormatter.format(tripEmpDetails.getTenMinuteMessageDeliveryDate()));
						}else{
							tripList.put("fifteenMsg","NA");
						}
						
					}					
					if(assignRoute.getNoshowMsgFlg()==1){
						if (!(tripEmpDetails.getCabstartFromDestination() == 0)) {
								tripList.put("noShowMsg",dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
							}						
					}
					if(assignRoute.getReachedMsgFlg()==1){
						if(tripEmpDetails.getReachedMessageDeliveryDate()!=null){
							tripList.put("reachedMsg", dateTimeFormatter.format(tripEmpDetails.getReachedMessageDeliveryDate()));
						}else{
							tripList.put("reachedMsg","NA");
						}						
					}
					
					if(assignRoute.getCabDelayMsgFlg()==1){
						if(tripEmpDetails.getCabDelayMsgDeliveryDate()!=null){
							tripList.put("cabDelayMsg", dateTimeFormatter.format(tripEmpDetails.getCabDelayMsgDeliveryDate()));
						}else{
							tripList.put("cabDelayMsg","NA");
						}
						
					}	
					if(assignRoute.getCabReachedTimeFlg()==1){						
						if (!(tripEmpDetails.getCabstartFromDestination() == 0)) {
							tripList.put("cabReachedTime",
									dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
							}						
					}
					if(assignRoute.getBoardingStatusFlg()==1)
						tripList.put("boardingStatus", tripEmpDetails.getBoardedFlg());
					if(assignRoute.getBoardingTimeFlg()==1){												
							if (tripDetails.getTripType().equalsIgnoreCase("PICKUP")) {
								if(assignRoute.getPickUpTimeFlg()==1)
									tripList.put("pickUpTime", tripEmpDetails.geteFmFmEmployeeTravelRequest().getPickUpTime());	
								try {									
									if (!(tripEmpDetails.getCabstartFromDestination() == 0)
											|| !(tripEmpDetails.getPickedUpDateAndTime() == 0)) {
										if (!(tripEmpDetails.getCabstartFromDestination() == 0)
												&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO"))) {
											tripList.put("boardingTime",
													dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
										}
										if (!(tripEmpDetails.getPickedUpDateAndTime() == 0)
												&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("B"))) {
											tripList.put("boardingTime",
													dateFormatter.format(new Date(tripEmpDetails.getPickedUpDateAndTime())));
										}
									} else {
										tripList.put("boardingTime", "0");
	
									}
								} catch (Exception e) {
									tripList.put("boardingTime", "0");						
									
								}
							}
							if (tripDetails.getTripType().equalsIgnoreCase("DROP")) {
								try {
									if (!(tripEmpDetails.getCabstartFromDestination() == 0)
											|| !(tripEmpDetails.getPickedUpDateAndTime() == 0)) {
										if (!(tripEmpDetails.getCabstartFromDestination() == 0)
												&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO"))) {
											tripList.put("boardingTime",
													dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
										}
										if (!(tripEmpDetails.getPickedUpDateAndTime() == 0)
												&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("D"))) {
											tripList.put("boardingTime",
													dateFormatter.format(new Date(tripEmpDetails.getPickedUpDateAndTime())));
										}
									} else {
										tripList.put("boardingTime", "0");
	
									}
								} catch (Exception e) {
									tripList.put("boardingTime", "0");
									log.info("Error in drop type" + e);
								}
							}				
					}									
					employeeDetailsList.add(employeeList);
					tripList.put("employeeDetails",employeeDetailsList);
				}
			}else{
				tripList.put("employeeDetails","empty");
			}
					dynamicReport.add(tripList);
			}
		}		
		if(assignRoute.getSearchType().equalsIgnoreCase("driverDetails")){					
				List<Date> selectedDates = new ArrayList<Date>();
				selectedDates = iAssignRouteBO.getAllTripsByDistinctDates(fromDate, toDate,
						assignRoute.geteFmFmClientBranchPO().getBranchId());				
				if ((!(selectedDates.isEmpty())) || selectedDates.size() != 0) {
					for (Date tripdates : selectedDates) {
						List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllTripByDate(tripdates, tripdates,
								assignRoute.geteFmFmClientBranchPO().getBranchId());						
						if ((!(allTripDetails.isEmpty())) || allTripDetails.size() != 0) {
							List<Object> dateAndDriverId = new ArrayList<Object>();
							for (EFmFmAssignRoutePO trips : allTripDetails) {
								List<Map<String, Object>> driverDrivingReportList = new ArrayList<Map<String, Object>>();
								Map<String, Object> driverReport = new HashMap<String, Object>();
								if (!(dateAndDriverId.contains(dateFormatter.format(trips.getTripAssignDate()))
										&& dateAndDriverId.contains(
												trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId()))) {
									List<EFmFmAssignRoutePO> allTripDetailsByDriverId = iAssignRouteBO
											.getAllTripsByDatesAndDriverId(tripdates, tripdates,
													assignRoute.geteFmFmClientBranchPO().getBranchId(),
													trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
									System.out.println("size" + allTripDetailsByDriverId.size() + "DATE"
											+ dateFormatter.format(allTripDetailsByDriverId.get(0).getTripAssignDate()));
									long millis = 0;
									for (EFmFmAssignRoutePO drivertrips : allTripDetailsByDriverId) {
										Map<String, Object> driverDrivingReport = new HashMap<String, Object>();
										dateAndDriverId.add(dateFormatter.format(drivertrips.getTripAssignDate()));
										dateAndDriverId
												.add(drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
										if(assignRoute.getAssignDateFlg()==1)
										driverDrivingReport.put("assignDate", formatter.format(drivertrips.getTripAssignDate()));
										if(assignRoute.getTripStartTimeFlg()==1)
										driverDrivingReport.put("tripStartTime",
												dateFormatter.format(drivertrips.getTripStartTime()));
										if(assignRoute.getTripEndTimeFlg()==1)
										driverDrivingReport.put("tripEndTime",dateFormatter.format(drivertrips.getTripCompleteTime()));
										
										if(assignRoute.getCheckInTimeFlg()==1)
										driverDrivingReport.put("checkInTime", dateFormatter.format(drivertrips.getEfmFmVehicleCheckIn().getCheckInTime()));
										if(assignRoute.getRemarksFlg()==1){
											try{
												driverDrivingReport.put("remarks",drivertrips.getEfmFmVehicleCheckIn().getCheckOutRemarks());
											}catch(Exception e){
												driverDrivingReport.put("remarks", "Not given");
											}
										}
										
										if(assignRoute.getCheckoutTimeFlg()==1){
											if(drivertrips.getEfmFmVehicleCheckIn().getCheckOutTime()==null){
												driverDrivingReport.put("checkoutTime","Not yet Checked Out");
											}else{
												driverDrivingReport.put("checkoutTime", dateFormatter.format(drivertrips.getEfmFmVehicleCheckIn().getCheckOutTime()));
											}
										}							
										
										
										long workingHoursValue = drivertrips.getEfmFmVehicleCheckIn().getCheckOutTime().getTime() - drivertrips.getEfmFmVehicleCheckIn().getCheckInTime().getTime();
										String workingHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(workingHoursValue),
												TimeUnit.MILLISECONDS.toMinutes(workingHoursValue)
														- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(workingHoursValue)),
												TimeUnit.MILLISECONDS.toSeconds(workingHoursValue)
														- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(workingHoursValue)));
										if(assignRoute.getTotalWorkingHours()==1)
										driverDrivingReport.put("totalWorkingHours", workingHours);	
										if(assignRoute.getVehicleNumberFlg()==1)
										driverDrivingReport.put("vehicleNumber", drivertrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
										millis += drivertrips.getTripCompleteTime().getTime()- drivertrips.getTripStartTime().getTime();
										long millisRouteMilles = drivertrips.getTripCompleteTime().getTime()- drivertrips.getTripStartTime().getTime();
										log.info("millisRouteMilles" + millisRouteMilles);
										String routeTravellHours = String.format("%02d:%02d:%02d",TimeUnit.MILLISECONDS.toHours(millisRouteMilles),
												TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles) - TimeUnit.HOURS
												.toMinutes(TimeUnit.MILLISECONDS.toHours(millisRouteMilles)),
												TimeUnit.MILLISECONDS.toSeconds(millisRouteMilles) - TimeUnit.MINUTES
												.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles)));
										log.info("routeTravellHours" + routeTravellHours);
										if(assignRoute.getDriverDrivingHoursPerTrip()==1)
										driverDrivingReport.put("driverDrivingHoursPerTrip", routeTravellHours);
										if(assignRoute.getVendorNumberFlg()==1)
										driverDrivingReport.put("vendorName", drivertrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
										if(assignRoute.getDriverNameFlg()==1)
										driverDrivingReport.put("driverName",drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
										if(assignRoute.getDriverIdFlg()==1)
										driverDrivingReport.put("driverId",drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
										if(assignRoute.getTripTypeFlg()==1)
										driverDrivingReport.put("tripType", drivertrips.getTripType());
										if(assignRoute.getRouteNameFlg()==1)										
										driverDrivingReport.put("routeName",drivertrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
										if(assignRoute.getRouteIdFlg()==1)
										driverDrivingReport.put("routeId",drivertrips.getAssignRouteId());

										driverDrivingReportList.add(driverDrivingReport);
									}
									
									String travellHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
											TimeUnit.MILLISECONDS.toMinutes(millis)
													- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
											TimeUnit.MILLISECONDS.toSeconds(millis)
													- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
									driverReport.put("tripCount", allTripDetailsByDriverId.size());
									if(assignRoute.getTotalDrivingHours()==1)
									driverReport.put("totalDrivingHours", travellHours);
									driverReport.put("tripsDetails", driverDrivingReportList);
									dynamicReport.add(driverReport);
								}

							}
						}
					}

				}			
		}
		return Response.ok(dynamicReport, MediaType.APPLICATION_JSON).build();
	}*/
	
	@POST
	@Path("/dynamicReportDownload")
	public Response dynamicReportDownload(EFmFmAssignRoutePO assignRoute) throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");	
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");		
		List<Map<String, Object>> dynamicReport = new ArrayList<Map<String, Object>>();	
		 log.info("serviceStart -UserId :" + assignRoute.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
/*		 try{
		 		if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoute.getUserId()))){
		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}
		 		
		 		List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(assignRoute.getUserId());
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
				}*/
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");	
		DateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		Date fromDate = formatter.parse(assignRoute.getFromDate());
		Date toDate = formatter.parse(assignRoute.getToDate());
		
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");
        XSSFCellStyle style = workbook.createCellStyle();      
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		font.setColor(new XSSFColor(new java.awt.Color(255,255,255)));//color
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0,82,128)));
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(font);  
        int assignDateColIndex=0,routeCloseTimeColIndex=0,escortIdColIndex=0,
        		escortNameColIndex=0,escortAddressColIndex=0,escortMobileNoColIndex=0,
        		plannedDistanceColIndex=0,gpskmColIndex=0,shiftTimeColIndex=0,tripTypeColIndex=0,
        		driverNameColIndex=0,vehicleNumberColIndex=0,routeNameColIndex=0,
        		driverMobileNumberColIndex=0,vendorNameColIndex=0,driverIdColIndex=0,
        		checkInTimeColIndex=0,checkoutTimeColIndex=0,totalWorkingHoursColIndex=0,
        		tripStartTimeColIndex=0,tripEndTimeColIndex=0,driverDrivingHoursPerTripColIndex=0,remarksColIndex=0,TotalDrivingHoursFlgColIndex=0,EmployeeIdFlgColIndex=0,EmployeeNameColIndex=0,HostMobileNumberFlgColIndex=0,
        				EmailIdColIndex=0,EmployeeMobileNoFlgColIndex=0,EmpLoacationFlgColIndex=0,
        				FifteenMinuteMsgColIndex=0,NoshowMsgFlgColIndex=0,ReachedMsgColIndex=0,
        				AllocationMsgColIndex=0,CabDelayMsgFlgColIndex=0,CabReachedTimeFlgColIndex=0,
        				BoardingStatusFlgColIndex=0,PickUpTimeFlgColIndex=0,BoardingTimeFlgColIndex=0;
        
        int rownum = 0, noOfRoute = 0;
        Row insideRow = sheet.createRow(rownum++); 
        //columnIndexLength
        for (int columnIndex = 0; columnIndex < 32; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
		
        int celId=0;
		Cell routeId= insideRow.createCell(celId);
		routeId.setCellValue("RouteId");
		routeId.setCellStyle(style);	
		
		if(assignRoute.getAssignDateFlg()==1){
		    celId=celId+1;
		    assignDateColIndex=celId;
			Cell assignDate= insideRow.createCell(celId);
			assignDate.setCellValue("Assign Date");
			assignDate.setCellStyle(style);
			
		}				
		if(assignRoute.getRouteCloseTimeFlg()==1){
			celId=celId+1;
			routeCloseTimeColIndex=celId;
			Cell routeCloseTime= insideRow.createCell(celId);
			routeCloseTime.setCellValue("Route Close Time");
			routeCloseTime.setCellStyle(style);
		}		
		
		if(assignRoute.getEscortIdFlg()==1){
			celId=celId+1;
			escortIdColIndex=celId;
			Cell escortId= insideRow.createCell(celId);
			escortId.setCellValue("EscortId");
			escortId.setCellStyle(style);
			
		}				
		if(assignRoute.getEscortNameFlg()==1){
			celId=celId+1;
		    escortNameColIndex=celId;
			Cell escortName= insideRow.createCell(celId);
			escortName.setCellValue("EscortName");
			escortName.setCellStyle(style);								
		}
		if(assignRoute.getEscortAddressFlg()==1){
			celId=celId+1;
			escortAddressColIndex=celId;
			Cell escortAddress= insideRow.createCell(celId);
			escortAddress.setCellValue("Escort Address");
			escortAddress.setCellStyle(style);
		}
		if(assignRoute.getEscortMobileNoFlg()==1){
			celId=celId+1;
			escortMobileNoColIndex=celId;
			Cell escortMobileNo= insideRow.createCell(celId);
			escortMobileNo.setCellValue("Escort MobileNo");
			escortMobileNo.setCellStyle(style);
		}				
		
		if(assignRoute.getPlannedDistanceFlg()==1){
			celId=celId+1;
			plannedDistanceColIndex=celId;
			Cell plannedDistance= insideRow.createCell(celId);
			plannedDistance.setCellValue("Planned Distance");
			plannedDistance.setCellStyle(style);
		}
			
		
		if(assignRoute.getGpsFlg()==1){					
			celId=celId+1;
			gpskmColIndex=celId;
			Cell gpskm= insideRow.createCell(celId);
			gpskm.setCellValue("Gps Km");
			gpskm.setCellStyle(style);
		}		
		
		if(assignRoute.getShiftTimeFlg()==1){
			celId=celId+1;
			shiftTimeColIndex=celId;
			Cell shiftTime= insideRow.createCell(celId);
			shiftTime.setCellValue("Shift Time");
			shiftTime.setCellStyle(style);
		}
		
		if(assignRoute.getTripTypeFlg()==1){
			celId=celId+1;
			tripTypeColIndex=celId;
			Cell tripType= insideRow.createCell(celId);
			tripType.setCellValue("Trip Type");
			tripType.setCellStyle(style);
		}
		
		if(assignRoute.getDriverNameFlg()==1){
			celId=celId+1;
			driverNameColIndex=celId;
			Cell driverName= insideRow.createCell(celId);
			driverName.setCellValue("Driver Name");
			driverName.setCellStyle(style);
		}
		
		if(assignRoute.getVehicleNumberFlg()==1){
			celId=celId+1;
			vehicleNumberColIndex=celId;
			Cell vehicleNumber= insideRow.createCell(celId);
			vehicleNumber.setCellValue("Vehicle Number");
			vehicleNumber.setCellStyle(style);
		}
		
		if(assignRoute.getRouteNameFlg()==1){
			celId=celId+1;
			routeNameColIndex=celId;
			Cell routeName= insideRow.createCell(celId);
			routeName.setCellValue("Route Name");
			routeName.setCellStyle(style);
		}
		
		if(assignRoute.getDriverMobileNumberFlg()==1){
			celId=celId+1;
			driverMobileNumberColIndex=celId;
			Cell driverMobileNumber= insideRow.createCell(celId);
			driverMobileNumber.setCellValue("Driver MobileNumber");
			driverMobileNumber.setCellStyle(style);
		}
		
		if(assignRoute.getVendorNameFlg()==1){
			celId=celId+1;
			vendorNameColIndex=celId;
			Cell vendorName= insideRow.createCell(celId);
			vendorName.setCellValue("Vendor Name");
			vendorName.setCellStyle(style);
		}
			
		if(assignRoute.getDriverIdFlg()==1){
			celId=celId+1;
			driverIdColIndex=celId;
			Cell driverId= insideRow.createCell(celId);
			driverId.setCellValue("Driver Id");
			driverId.setCellStyle(style);
		
		}
				
		if(assignRoute.getCheckInTimeFlg()==1){
			celId=celId+1;
			checkInTimeColIndex=celId;
			Cell checkInTime= insideRow.createCell(celId);
			checkInTime.setCellValue("Check-InTime");
			checkInTime.setCellStyle(style);
		}				
			
		if(assignRoute.getCheckoutTimeFlg()==1){
			celId=celId+1;
			checkoutTimeColIndex=celId;
			Cell checkoutTime= insideRow.createCell(celId);
			checkoutTime.setCellValue("CheckoutTime");
			checkoutTime.setCellStyle(style);
		}	
		if(assignRoute.getTotalWorkingHoursFlg()==1){	
			celId=celId+1;
			totalWorkingHoursColIndex=celId;
			Cell totalWorkingHours= insideRow.createCell(celId);
			totalWorkingHours.setCellValue("TotalWorkingHours");
			totalWorkingHours.setCellStyle(style);
		}
		
		if(assignRoute.getTripStartTimeFlg()==1){
			celId=celId+1;
			tripStartTimeColIndex=celId;
			Cell tripStartTime= insideRow.createCell(celId);
			tripStartTime.setCellValue("TripStartTime");
			tripStartTime.setCellStyle(style);
		}
		if(assignRoute.getTripEndTimeFlg()==1){
			celId=celId+1;
			tripEndTimeColIndex=celId;
			Cell tripEndTime= insideRow.createCell(celId);
			tripEndTime.setCellValue("TripEndTime");
			tripEndTime.setCellStyle(style);
		}
		if(assignRoute.getDriverDrivingHoursPerTripFlg()==1){
			celId=celId+1;
			driverDrivingHoursPerTripColIndex=celId;
			Cell driverDrivingHoursPerTrip= insideRow.createCell(celId);
			driverDrivingHoursPerTrip.setCellValue("DriverDrivingHoursPerTrip");
			driverDrivingHoursPerTrip.setCellStyle(style);
		}				
		if(assignRoute.getRemarksFlg()==1){
			celId=celId+1;
			remarksColIndex=celId;
			Cell remarks= insideRow.createCell(celId);
			remarks.setCellValue("Remarks");
			remarks.setCellStyle(style);
		}	
		
		if(assignRoute.getTotalDrivingHoursFlg()==1){
			celId=celId+1;
			TotalDrivingHoursFlgColIndex=celId;
			Cell TotalDrivingHoursFlg= insideRow.createCell(celId);
			TotalDrivingHoursFlg.setCellValue("TotalDrivingHours");
			TotalDrivingHoursFlg.setCellStyle(style);
		}
		

		if(assignRoute.getEmployeeIdFlg()==1){	
			celId=celId+1;
			EmployeeIdFlgColIndex=celId;
			Cell EmployeeIdFlg= insideRow.createCell(celId);
			EmployeeIdFlg.setCellValue("EmployeeId");
			EmployeeIdFlg.setCellStyle(style);					
		}			
		if(assignRoute.getEmployeeNameFlg()==1){
			celId=celId+1;
			EmployeeNameColIndex=celId;
			Cell EmployeeNameFlg= insideRow.createCell(celId);
			EmployeeNameFlg.setCellValue("Employee Name");
			EmployeeNameFlg.setCellStyle(style);
		}					
		if(assignRoute.getHostMobileNumberFlg()==1){
			celId=celId+1;
			HostMobileNumberFlgColIndex=celId;
			Cell HostMobileNumberFlg= insideRow.createCell(celId);
			HostMobileNumberFlg.setCellValue("HostMobileNumber");
			HostMobileNumberFlg.setCellStyle(style);					
		}					
		if(assignRoute.getEmailIdFlg()==1){		
			celId=celId+1;
			EmailIdColIndex=celId;
			Cell EmailIdFlg= insideRow.createCell(celId);
			EmailIdFlg.setCellValue("EmailId");
			EmailIdFlg.setCellStyle(style);
		}					
		if(assignRoute.getEmployeeMobileNoFlg()==1){
			celId=celId+1;
			EmployeeMobileNoFlgColIndex=celId;
			Cell EmployeeMobileNoFlg= insideRow.createCell(celId);
			EmployeeMobileNoFlg.setCellValue("EmployeeMobileNo");
			EmployeeMobileNoFlg.setCellStyle(style);
		}						
		if(assignRoute.getEmpLoacationFlg()==1){	
			celId=celId+1;
			EmpLoacationFlgColIndex=celId;
			Cell EmpLoacationFlg= insideRow.createCell(celId);
			EmpLoacationFlg.setCellValue("EmpLoacation");
			EmpLoacationFlg.setCellStyle(style);
		}			
		if(assignRoute.getFifteenMinuteMsgFlg()==1){
			celId=celId+1;
			FifteenMinuteMsgColIndex=celId;
			Cell FifteenMinuteMsgFlg= insideRow.createCell(celId);
			FifteenMinuteMsgFlg.setCellValue("FifteenMinuteMsg");
			FifteenMinuteMsgFlg.setCellStyle(style);					
		}					
		if(assignRoute.getNoshowMsgFlg()==1){	
			celId=celId+1;
			NoshowMsgFlgColIndex=celId;
			Cell NoshowMsgFlg= insideRow.createCell(celId);
			NoshowMsgFlg.setCellValue("NoshowMsg");
			NoshowMsgFlg.setCellStyle(style);					
		}
		if(assignRoute.getReachedMsgFlg()==1){	
			celId=celId+1;
			ReachedMsgColIndex=celId;
			Cell ReachedMsgFlg= insideRow.createCell(celId);
			ReachedMsgFlg.setCellValue("ReachedMsg");
			ReachedMsgFlg.setCellStyle(style);
		}			
		if(assignRoute.getAllocationMsgFlg()==1){
			celId=celId+1;
			AllocationMsgColIndex=celId;
			Cell AllocationMsgFlg= insideRow.createCell(celId);
			AllocationMsgFlg.setCellValue("AllocationMsg");
			AllocationMsgFlg.setCellStyle(style);
		}			
		if(assignRoute.getCabDelayMsgFlg()==1){	
			celId=celId+1;
			CabDelayMsgFlgColIndex=celId;
			Cell CabDelayMsgFlg= insideRow.createCell(celId);
			CabDelayMsgFlg.setCellValue("CabDelayMsg");
			CabDelayMsgFlg.setCellStyle(style);
		}	
		if(assignRoute.getCabReachedTimeFlg()==1){	
			celId=celId+1;
			CabReachedTimeFlgColIndex=celId;
			Cell CabReachedTimeFlg= insideRow.createCell(celId);
			CabReachedTimeFlg.setCellValue("CabReachedTime");
			CabReachedTimeFlg.setCellStyle(style);
		}		
		if(assignRoute.getBoardingStatusFlg()==1){
			celId=celId+1;
			BoardingStatusFlgColIndex=celId;
			Cell BoardingStatusFlg= insideRow.createCell(celId);
			BoardingStatusFlg.setCellValue("BoardingStatus");
			BoardingStatusFlg.setCellStyle(style);
		}
		if(assignRoute.getPickUpTimeFlg()==1){
			celId=celId+1;
			PickUpTimeFlgColIndex=celId;
			Cell PickUpTimeFlg= insideRow.createCell(celId);
			PickUpTimeFlg.setCellValue("PickUpTime");
			PickUpTimeFlg.setCellStyle(style);
		}					
		if(assignRoute.getBoardingTimeFlg()==1){	
			celId=celId+1;
			BoardingTimeFlgColIndex=celId;
			Cell BoardingTimeFlg= insideRow.createCell(celId);
			BoardingTimeFlg.setCellValue("BoardingTime");
			BoardingTimeFlg.setCellStyle(style);					
		}	
		
		List<EFmFmAssignRoutePO> allEmployeeTraveledDetails=null;		
		if(assignRoute.getSearchType().equalsIgnoreCase("employee") || assignRoute.getSearchType().equalsIgnoreCase("guest")){
			if(assignRoute.getSearchType().equalsIgnoreCase("employee")){		
			allEmployeeTraveledDetails = iAssignRouteBO.getAllEmployeeDynamicDetails(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()));
		}else if(assignRoute.getSearchType().equalsIgnoreCase("guest")){
			allEmployeeTraveledDetails = iAssignRouteBO.getAllGuestDynamicDetails(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()));
		}		 
		if(!allEmployeeTraveledDetails.isEmpty() && allEmployeeTraveledDetails !=null){
			for(EFmFmAssignRoutePO tripDetails:allEmployeeTraveledDetails){
				Map<String, Object> tripList = new HashMap<String, Object>();
				insideRow = sheet.createRow(rownum++); 
				tripList.put("routeId", tripDetails.getAssignRouteId());				
				assignRoute.setAssignRouteId(tripDetails.getAssignRouteId());				
				Cell tripIdCell = insideRow.createCell(0);
				tripIdCell.setCellValue(tripDetails.getAssignRouteId());
				
				if(assignRoute.getAssignDateFlg()==1){
				  tripList.put("assignDate",formatter.format(tripDetails.getCreatedDate()));
				  Cell assignDateCol = insideRow.createCell(assignDateColIndex);
					assignDateCol.setCellValue(formatter.format(tripDetails.getCreatedDate()));
				}
				if(assignRoute.getRouteCloseTimeFlg()==1){
					tripList.put("routeCloseTime",dateTimeFormatter.format(tripDetails.getCreatedDate()));
					Cell routeCloseTimeCol = insideRow.createCell(routeCloseTimeColIndex);
					routeCloseTimeCol.setCellValue(dateTimeFormatter.format(tripDetails.getCreatedDate()));
				}
							
				
				if(assignRoute.getEscortIdFlg()==1){
					String escortId="NA";
					if(tripDetails.getEscortRequredFlag().equalsIgnoreCase("Y")){
						
						if(tripDetails.geteFmFmEscortCheckIn() !=null){
							escortId=String.valueOf(tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getEscortId());
						}else{
							escortId="required but NA";
						}
					}
					Cell escortIdCol = insideRow.createCell(escortIdColIndex);
					escortIdCol.setCellValue(escortId);
				}				
				if(assignRoute.getEscortNameFlg()==1){		
					String escortName="NA";
					if(tripDetails.getEscortRequredFlag().equalsIgnoreCase("Y")){	
						if(tripDetails.geteFmFmEscortCheckIn() !=null){
							escortName=tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName();
						}else{
							escortName="required but NA";
						}
					}
					Cell escortNameCol = insideRow.createCell(escortNameColIndex);
					escortNameCol.setCellValue(escortName);
					
				}
				if(assignRoute.getEscortAddressFlg()==1){
					String escortAddress="NA";
					if(tripDetails.getEscortRequredFlag().equalsIgnoreCase("Y")){
						if(tripDetails.geteFmFmEscortCheckIn() !=null){
							escortAddress=tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getAddress();	
						}else{
							escortAddress="required but NA";
						}
					}
					Cell escortAddressCol = insideRow.createCell(escortAddressColIndex);
					escortAddressCol.setCellValue(escortAddress);
				}
				if(assignRoute.getEscortMobileNoFlg()==1){
					String escortMobileNo="NA";
					 if(tripDetails.getEscortRequredFlag().equalsIgnoreCase("Y")){	
						if(tripDetails.geteFmFmEscortCheckIn() !=null){
							escortMobileNo=tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getMobileNumber();
						}else{
							escortMobileNo="required but NA";
						}
						}
					     Cell  escortMobileNoCol= insideRow.createCell(escortMobileNoColIndex);
						escortMobileNoCol.setCellValue(escortMobileNo);
				}				
				
				if(assignRoute.getPlannedDistanceFlg()==1){
					tripList.put("plannedDistance", tripDetails.getPlannedDistance());
					
					Cell plannedDistanceCol = insideRow.createCell(plannedDistanceColIndex);
					plannedDistanceCol.setCellValue(tripDetails.getPlannedDistance());
					
				}
				
				if(assignRoute.getGpsFlg()==1){	
					double gpsDistance=0;					
					try {					
						if (tripDetails.getTravelledDistance() == 0) {
							tripList.put("gpsDistance", "NA");		
						} else {
							String extensionRemoved = Double.toString(tripDetails.getTravelledDistance())
									.split("\\.")[1];
							if (!(extensionRemoved.equalsIgnoreCase("0"))) {
								gpsDistance=Math.round((double) tripDetails.getTravelledDistance());						
							} else {
								gpsDistance=tripDetails.getTravelledDistance();				
							}
						}
						Cell gpskmCol = insideRow.createCell(gpskmColIndex);
						gpskmCol.setCellValue(gpsDistance);
					} catch (Exception e) {
						log.info("TripSheet Error" + e);
					}
				}		
				
				if(assignRoute.getShiftTimeFlg()==1){
				tripList.put("shiftTime", tripDetails.getShiftTime() );

				Cell shiftTimeCol = insideRow.createCell(shiftTimeColIndex);
				shiftTimeCol.setCellValue(tripDetails.getShiftTime());
				}
				if(assignRoute.getTripTypeFlg()==1){
				tripList.put("tripType", tripDetails.getTripType());
				Cell tripTypeCol = insideRow.createCell(tripTypeColIndex);
				tripTypeCol.setCellValue(tripDetails.getTripType());
				}
				if(assignRoute.getDriverNameFlg()==1){
				tripList.put("driverName", tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName() );
				Cell driverNameCol = insideRow.createCell(driverNameColIndex);
				driverNameCol.setCellValue(tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				}
				if(assignRoute.getVehicleNumberFlg()==1){
				tripList.put("vehicleNumber", tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				Cell vehicleNumberCol = insideRow.createCell(vehicleNumberColIndex);
				vehicleNumberCol.setCellValue(tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				}
				if(assignRoute.getRouteNameFlg()==1){
				tripList.put("routeName",tripDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				Cell routeNameCol = insideRow.createCell(routeNameColIndex);
				routeNameCol.setCellValue(tripDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				}
				if(assignRoute.getDriverMobileNumberFlg()==1){
				tripList.put("driverNumber", tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				Cell driverMobileNumberCol = insideRow.createCell(driverMobileNumberColIndex);
				driverMobileNumberCol.setCellValue(tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				}
				if(assignRoute.getVendorNameFlg()==1){
					tripList.put("vendorName", tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());

					Cell vendorNameCol = insideRow.createCell(vendorNameColIndex);
					vendorNameCol.setCellValue(tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				}
				if(assignRoute.getDriverIdFlg()==1){
						tripList.put("driverId",tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
						Cell driverIdCol = insideRow.createCell(driverIdColIndex);			
						driverIdCol.setCellValue(tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				}
				if(assignRoute.getCheckInTimeFlg()==1){
					
					tripList.put("checkInTime", dateTimeFormatter.format(tripDetails.getEfmFmVehicleCheckIn().getCheckInTime()));
					Cell checkInTimeCol = insideRow.createCell(checkInTimeColIndex);
					checkInTimeCol.setCellValue(dateTimeFormatter.format(tripDetails.getEfmFmVehicleCheckIn().getCheckInTime()));
				}
				if(assignRoute.getCheckoutTimeFlg()==1){
					String CheckoutTimeFlg="Not yet Checked Out";
					if(tripDetails.getEfmFmVehicleCheckIn().getCheckOutTime()==null){
						tripList.put("checkoutTime","Not yet Checked Out");
					}else{
						CheckoutTimeFlg=dateTimeFormatter.format(tripDetails.getEfmFmVehicleCheckIn().getCheckOutTime());
					}
					Cell checkoutTimeCol = insideRow.createCell(checkoutTimeColIndex);
					checkoutTimeCol.setCellValue(CheckoutTimeFlg);
				}	
				if(assignRoute.getTotalWorkingHoursFlg()==1){
					String workingHours="Not CalCulated";
					if(tripDetails.getEfmFmVehicleCheckIn().getCheckOutTime() !=null){				
					long workingHoursValue = tripDetails.getEfmFmVehicleCheckIn().getCheckOutTime().getTime() - tripDetails.getEfmFmVehicleCheckIn().getCheckInTime().getTime();
					 workingHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(workingHoursValue),
							TimeUnit.MILLISECONDS.toMinutes(workingHoursValue)
									- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(workingHoursValue)),
							TimeUnit.MILLISECONDS.toSeconds(workingHoursValue)
									- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(workingHoursValue)));
						tripList.put("totalWorkingHours", workingHours);
					}
					Cell totalWorkingHoursCol = insideRow.createCell(totalWorkingHoursColIndex);
					totalWorkingHoursCol.setCellValue(workingHours);
				}
				
				if(assignRoute.getTripStartTimeFlg()==1){
					String tripStartTime="NA";
					if(tripDetails.getTripStartTime()!=null){
						tripStartTime=dateTimeFormatter.format(tripDetails.getTripStartTime());
					}
					Cell tripStartTimeCol = insideRow.createCell(tripStartTimeColIndex);
					tripStartTimeCol.setCellValue(tripStartTime);
				}
				if(assignRoute.getTripEndTimeFlg()==1){
					String tripEndTime="NA";
					if(tripDetails.getTripCompleteTime()!=null){
						tripEndTime =dateTimeFormatter.format(tripDetails.getTripCompleteTime());
					}
					Cell tripEndTimeCol = insideRow.createCell(tripEndTimeColIndex);
					tripEndTimeCol.setCellValue(tripEndTime);
				}
				if(assignRoute.getDriverDrivingHoursPerTripFlg()==1){
					String routeTravellHours="NA";
					if(tripDetails.getTripCompleteTime()!=null &&  tripDetails.getTripStartTime() !=null ){
						long millisRouteMilles = tripDetails.getTripCompleteTime().getTime()- tripDetails.getTripStartTime().getTime();
						log.info("millisRouteMilles" + millisRouteMilles);
						 routeTravellHours = String.format("%02d:%02d:%02d",TimeUnit.MILLISECONDS.toHours(millisRouteMilles),
								TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles) - TimeUnit.HOURS
								.toMinutes(TimeUnit.MILLISECONDS.toHours(millisRouteMilles)),
								TimeUnit.MILLISECONDS.toSeconds(millisRouteMilles) - TimeUnit.MINUTES
								.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles)));
						log.info("routeTravellHours" + routeTravellHours);	
						tripList.put("driverDrivingHoursPerTrip", routeTravellHours);
					}
					Cell driverDrivingHoursPerTripCol = insideRow.createCell(driverDrivingHoursPerTripColIndex);
					driverDrivingHoursPerTripCol.setCellValue(routeTravellHours);
				}				
				if(assignRoute.getRemarksFlg()==1){
					String remarks="Not given";
					try{
					  if(tripDetails.getEfmFmVehicleCheckIn().getCheckOutRemarks()!=null && !tripDetails.getEfmFmVehicleCheckIn().getCheckOutRemarks().isEmpty()){
						  remarks=tripDetails.getEfmFmVehicleCheckIn().getCheckOutRemarks();
						}
					}catch(Exception e){
						remarks="Not given";
					}
					Cell remarksCol = insideRow.createCell(remarksColIndex);
					remarksCol.setCellValue(remarks);
				}
				/*
				 * EmployeeDetails
				 */
			
				List<EFmFmEmployeeTripDetailPO> employeeDetails=iCabRequestBO.getParticularTripAllEmployees(assignRoute.getAssignRouteId());				
				List<Map<String, Object>> employeeDetailsList = new ArrayList<Map<String, Object>>();
				if(!employeeDetails.isEmpty()){
				for(EFmFmEmployeeTripDetailPO tripEmpDetails:employeeDetails){
					Map<String, Object> employeeList = new HashMap<String, Object>();						
						if(assignRoute.getEmployeeIdFlg()==1){
							employeeList.put("employeeId",tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
							 Cell EmployeeIdFlgCol = insideRow.createCell(EmployeeIdFlgColIndex);
							 EmployeeIdFlgCol.setCellValue(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						}
						if(assignRoute.getEmployeeNameFlg()==1){
							employeeList.put("empName",new String(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),"utf-8"));
							Cell EmployeeNameCol = insideRow.createCell(EmployeeNameColIndex);
						    EmployeeNameCol.setCellValue(new String(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),"utf-8"));
						}
						
						if(assignRoute.getHostMobileNumberFlg()==1){
							String hostMobileNumberFlgCol="NA";
							if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber() !=null){
								try {
									employeeList.put("hostMobileNumber",new String
											(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber()),"utf-8"));
									hostMobileNumberFlgCol=new String
											(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber()),"utf-8");

								} catch (Exception e) {
									hostMobileNumberFlgCol="NA";
								}
								
							}
							
							Cell HostMobileNumberFlgCol = insideRow.createCell(HostMobileNumberFlgColIndex);
						    HostMobileNumberFlgCol.setCellValue(hostMobileNumberFlgCol);
						}					
						if(assignRoute.getEmailIdFlg()==1){
							String emailIdCol="NA";
							if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmailId() !=null){
								try {
									employeeList.put("empEmailId",new String
											(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmailId()),"utf-8"));
									emailIdCol=new String
											(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmailId()),"utf-8");
								} catch (Exception e) {
									emailIdCol="NA";
								}
								
							}
							
							Cell EmailIdCol = insideRow.createCell(EmailIdColIndex);
						    EmailIdCol.setCellValue(emailIdCol);
						}					
						if(assignRoute.getEmployeeMobileNoFlg()==1){
							String employeeMobileNoFlgCol="NA";
							if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber() !=null){
								try {
									employeeList.put("mobileNumber",new String
											(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber()),"utf-8"));
									employeeMobileNoFlgCol=new String
											(Base64.getDecoder().decode(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber()),"utf-8");
								} catch (Exception e) {
									employeeMobileNoFlgCol="NA";
								}								
							}
							
							Cell EmployeeMobileNoFlgCol = insideRow.createCell(EmployeeMobileNoFlgColIndex);
						    EmployeeMobileNoFlgCol.setCellValue(employeeMobileNoFlgCol);
						}
							
						if(assignRoute.getEmpLoacationFlg()==1){
							String empLoacationFlgCol="NA";
							if(tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude()!=null){
								employeeList.put("employeeLocation", tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude());
								empLoacationFlgCol=tripEmpDetails.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude();
							}							
							Cell EmpLoacationFlgCol = insideRow.createCell(EmpLoacationFlgColIndex);
						    EmpLoacationFlgCol.setCellValue(empLoacationFlgCol);
						}			
						if(assignRoute.getFifteenMinuteMsgFlg()==1){
							String fifteenMinuteMsgCol="NA";
							if(tripEmpDetails.getTenMinuteMessageDeliveryDate()!=null){
								employeeList.put("fifteenMsg", dateTimeFormatter.format(tripEmpDetails.getTenMinuteMessageDeliveryDate()));
								fifteenMinuteMsgCol=dateTimeFormatter.format(tripEmpDetails.getTenMinuteMessageDeliveryDate());
							}							
							Cell FifteenMinuteMsgCol = insideRow.createCell(FifteenMinuteMsgColIndex);
						    FifteenMinuteMsgCol.setCellValue(fifteenMinuteMsgCol);	
							
						}					
						if(assignRoute.getNoshowMsgFlg()==1){
							String noshowMsgFlgCol="NA";
							if (!(tripEmpDetails.getCabstartFromDestination() == 0)) {
								employeeList.put("noShowMsg",dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
								    noshowMsgFlgCol=dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination()));
								}	
							
							Cell NoshowMsgFlgCol = insideRow.createCell(NoshowMsgFlgColIndex);
						    NoshowMsgFlgCol.setCellValue(noshowMsgFlgCol);	
						}
						if(assignRoute.getReachedMsgFlg()==1){
							String reachedMsgCol="NA";
							if(tripEmpDetails.getReachedMessageDeliveryDate()!=null){
								employeeList.put("reachedMsg", dateTimeFormatter.format(tripEmpDetails.getReachedMessageDeliveryDate()));
								reachedMsgCol=dateTimeFormatter.format(tripEmpDetails.getReachedMessageDeliveryDate());
							}	
							
							Cell ReachedMsgCol = insideRow.createCell(ReachedMsgColIndex);
						    ReachedMsgCol.setCellValue(reachedMsgCol);	
						}			
						if(assignRoute.getAllocationMsgFlg()==1){
							String allocationMsgCol="NA";
							if(tripEmpDetails.getEfmFmAssignRoute().getAllocationMsgDeliveryDate() !=null){
								employeeList.put("allocationMsg", dateTimeFormatter.format(tripEmpDetails.getEfmFmAssignRoute().getAllocationMsgDeliveryDate()));
								allocationMsgCol=dateTimeFormatter.format(tripEmpDetails.getEfmFmAssignRoute().getAllocationMsgDeliveryDate());
							}							
							Cell AllocationMsgCol = insideRow.createCell(AllocationMsgColIndex);
						    AllocationMsgCol.setCellValue(allocationMsgCol);
						}			
						if(assignRoute.getCabDelayMsgFlg()==1){
							String cabDelayMsgFlgCol="NA";
							if(tripEmpDetails.getCabDelayMsgDeliveryDate()!=null){
								employeeList.put("cabDelayMsg", dateTimeFormatter.format(tripEmpDetails.getCabDelayMsgDeliveryDate()));
								cabDelayMsgFlgCol=dateTimeFormatter.format(tripEmpDetails.getCabDelayMsgDeliveryDate());
							}	
							
							Cell CabDelayMsgFlgCol = insideRow.createCell(CabDelayMsgFlgColIndex);
						    CabDelayMsgFlgCol.setCellValue(cabDelayMsgFlgCol);
						}	
						if(assignRoute.getCabReachedTimeFlg()==1){	
							String cabReachedTimeFlgCol="NA";
							if (!(tripEmpDetails.getCabstartFromDestination() == 0)) {
								employeeList.put("cabReachedTime",
										dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
								 cabReachedTimeFlgCol=dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination()));
								}	
							
							Cell CabReachedTimeFlgCol = insideRow.createCell(CabReachedTimeFlgColIndex);
						    CabReachedTimeFlgCol.setCellValue(cabReachedTimeFlgCol);
						}		
						if(assignRoute.getBoardingStatusFlg()==1){
							String  boardingStatusFlgCol="NA";
							if (tripEmpDetails.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {					
								if (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("B")) {
									employeeList.put("boardingStatus", "Boarded");
									boardingStatusFlgCol="Boarded";
								} else if (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO")) {
									employeeList.put("boardingStatus", "No Show");
									boardingStatusFlgCol="No Show";
								} else if (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("N")) {
									employeeList.put("boardingStatus", "Yet to PickUp");
									boardingStatusFlgCol="Yet to PickUp";
								}
							}
							if (tripEmpDetails.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
								if (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("D")) {
									employeeList.put("boardingStatus", "Dropped");
									boardingStatusFlgCol="Dropped";
								} else if (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO")) {
									employeeList.put("boardingStatus", "No Show");
									boardingStatusFlgCol="No Show";
								} else if (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("N")) {
									employeeList.put("boardingStatus", "Yet to Drop");
									boardingStatusFlgCol="Yet to Drop";
								}
							}
							
							Cell BoardingStatusFlgCol= insideRow.createCell(BoardingStatusFlgColIndex);
						    BoardingStatusFlgCol.setCellValue(boardingStatusFlgCol);
							
						}	
						if(assignRoute.getPickUpTimeFlg()==1){
							String pickUpTimeFlgCol="NA";
							if (tripEmpDetails.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {							
								employeeList.put("pickUpTime", tripEmpDetails.geteFmFmEmployeeTravelRequest().getPickUpTime());	
								pickUpTimeFlgCol=dateFormatter.format(tripEmpDetails.geteFmFmEmployeeTravelRequest().getPickUpTime());								
							}
							Cell PickUpTimeFlgCol= insideRow.createCell(PickUpTimeFlgColIndex);
						    PickUpTimeFlgCol.setCellValue(pickUpTimeFlgCol);
						}				    
						if(assignRoute.getBoardingTimeFlg()==1){	
							String boardingTimeFlgCol="NA";
								if (tripEmpDetails.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
									if(assignRoute.getPickUpTimeFlg()==1){
										employeeList.put("pickUpTime",tripEmpDetails.geteFmFmEmployeeTravelRequest().getPickUpTime());	
									}
									try {									
										if (!(tripEmpDetails.getCabstartFromDestination() == 0)
												|| !(tripEmpDetails.getPickedUpDateAndTime() == 0)) {
											if (!(tripEmpDetails.getCabstartFromDestination() == 0)
													&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO"))) {
												employeeList.put("boardingTime",
														dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
												boardingTimeFlgCol=dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination()));
											}
											if (!(tripEmpDetails.getPickedUpDateAndTime() == 0)
													&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("B"))) {
												employeeList.put("boardingTime",
														dateFormatter.format(new Date(tripEmpDetails.getPickedUpDateAndTime())));
												boardingTimeFlgCol=dateFormatter.format(new Date(tripEmpDetails.getPickedUpDateAndTime()));
											}
										} else {
											employeeList.put("boardingTime", "0");
											boardingTimeFlgCol="0";
	
										}
									} catch (Exception e) {
										employeeList.put("boardingTime", "0");
										boardingTimeFlgCol="0";
										
									}
								}
								if (tripEmpDetails.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
									try {
										if (!(tripEmpDetails.getCabstartFromDestination() == 0)
												|| !(tripEmpDetails.getPickedUpDateAndTime() == 0)) {
											if (!(tripEmpDetails.getCabstartFromDestination() == 0)
													&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("NO"))) {
												employeeList.put("boardingTime",
														dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination())));
												boardingTimeFlgCol=dateFormatter.format(new Date(tripEmpDetails.getCabstartFromDestination()));
											}
											if (!(tripEmpDetails.getPickedUpDateAndTime() == 0)
													&& (tripEmpDetails.getBoardedFlg().equalsIgnoreCase("D"))) {
												employeeList.put("boardingTime",
														dateFormatter.format(new Date(tripEmpDetails.getPickedUpDateAndTime())));
												boardingTimeFlgCol=dateFormatter.format(new Date(tripEmpDetails.getPickedUpDateAndTime()));
											}
										} else {
											employeeList.put("boardingTime", "0");
											boardingTimeFlgCol="0";
	
										}
									} catch (Exception e) {
										employeeList.put("boardingTime", "0");
										boardingTimeFlgCol="0";
										log.info("Error in drop type" + e);
									}
								}								
								Cell BoardingTimeFlgCol= insideRow.createCell(BoardingTimeFlgColIndex);
							    BoardingTimeFlgCol.setCellValue(boardingTimeFlgCol);	
					}									
					employeeDetailsList.add(employeeList);			
				}
			}
				/*
				 * End -Employee Details
				 */

					dynamicReport.add(tripList);
			}
		}		
		}else if(assignRoute.getSearchType().equalsIgnoreCase("driverDetails")){					
				List<Date> selectedDates = new ArrayList<Date>();
				if(assignRoute.getVendorIdSelected().equalsIgnoreCase("All")){
					selectedDates = iAssignRouteBO.getAllTripsByDistinctDates(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()));
				}else{
					selectedDates = iAssignRouteBO.getAllTripsByDistinctDatesByVendor(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()),assignRoute.getVendorIdSelected());
				}				
				if ((!(selectedDates.isEmpty())) || selectedDates.size() != 0) {
					for (Date tripdates : selectedDates) {
						List<EFmFmAssignRoutePO> allTripDetails=null;
						if(assignRoute.getVendorIdSelected().equalsIgnoreCase("All")){
							allTripDetails = iAssignRouteBO.getAllTripByDate(tripdates, tripdates,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()));
						}else{
							allTripDetails = iAssignRouteBO.getAllTripByDateByVendor(tripdates, tripdates,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()),assignRoute.getVendorIdSelected());
						}
						
						if ((!(allTripDetails.isEmpty())) || allTripDetails.size() != 0) {
							List<Object> dateAndDriverId = new ArrayList<Object>();
							for (EFmFmAssignRoutePO trips : allTripDetails) {
								List<Map<String, Object>> driverDrivingReportList = new ArrayList<Map<String, Object>>();
								Map<String, Object> driverReport = new HashMap<String, Object>();
								if (!(dateAndDriverId.contains(dateFormatter.format(trips.getTripAssignDate()))
										&& dateAndDriverId.contains(
												trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId()))) {
									List<EFmFmAssignRoutePO> allTripDetailsByDriverId=null;
									if(assignRoute.getVendorIdSelected().equalsIgnoreCase("All")){
											allTripDetailsByDriverId = iAssignRouteBO.getAllTripsByDatesAndDriverId(tripdates, tripdates,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()),trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
									}else{
										allTripDetailsByDriverId = iAssignRouteBO.getAllTripsByDatesAndDriverIdByVendor(tripdates, tripdates,new MultifacilityService().combinedBranchIdDetails(assignRoute.getUserId(),assignRoute.getCombinedFacility()),trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId(),assignRoute.getVendorIdSelected());
									}
									System.out.println("size" + allTripDetailsByDriverId.size() + "DATE"
											+ dateFormatter.format(allTripDetailsByDriverId.get(0).getTripAssignDate()));
									long millis = 0;
									for (EFmFmAssignRoutePO drivertrips : allTripDetailsByDriverId) {
										Map<String, Object> driverDrivingReport = new HashMap<String, Object>();
										dateAndDriverId.add(dateFormatter.format(drivertrips.getTripAssignDate()));
										dateAndDriverId.add(drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
										if(assignRoute.getAssignDateFlg()==1){
											driverDrivingReport.put("assignDate", formatter.format(drivertrips.getTripAssignDate()));
											 Cell assignDateCol = insideRow.createCell(assignDateColIndex);
											 assignDateCol.setCellValue(formatter.format(drivertrips.getTripAssignDate()));
										}
												
										if(assignRoute.getTripStartTimeFlg()==1){
											String tripStartTime="NA";
											if(drivertrips.getTripStartTime()!=null){
												tripStartTime=dateTimeFormatter.format(drivertrips.getTripStartTime());
											}
											Cell tripStartTimeCol = insideRow.createCell(tripStartTimeColIndex);
											tripStartTimeCol.setCellValue(tripStartTime);
										}
										
										
										
										if(assignRoute.getTripEndTimeFlg()==1){
											String tripEndTime="NA";
											if(drivertrips.getTripCompleteTime()!=null){
												tripEndTime=dateTimeFormatter.format(drivertrips.getTripCompleteTime());
											}											
											Cell tripEndTimeCol = insideRow.createCell(tripEndTimeColIndex);
											tripEndTimeCol.setCellValue(tripEndTime);
										}		
																
										if(assignRoute.getCheckInTimeFlg()==1){
											String driverCheckInTime=dateFormatter.format(drivertrips.getEfmFmVehicleCheckIn().getCheckInTime());											
											Cell checkInTimeCol = insideRow.createCell(checkInTimeColIndex);
											checkInTimeCol.setCellValue(dateTimeFormatter.format(driverCheckInTime));
										}
										if(assignRoute.getRemarksFlg()==1){
											String remarks="Not given";
											try{
											  if(drivertrips.getEfmFmVehicleCheckIn().getCheckOutRemarks()!=null && !drivertrips.getEfmFmVehicleCheckIn().getCheckOutRemarks().isEmpty()){
												  remarks=drivertrips.getEfmFmVehicleCheckIn().getCheckOutRemarks();												  
												}
											}catch(Exception e){
												driverDrivingReport.put("remarks", "Not given");
												driverReport.put("remarks", "Not given");
											}
											Cell remarksCol = insideRow.createCell(remarksColIndex);
											remarksCol.setCellValue(remarks);
										}	
										
										if(assignRoute.getCheckoutTimeFlg()==1){
											String CheckoutTimeFlg="Not yet Checked Out";
											if(drivertrips.getEfmFmVehicleCheckIn().getCheckOutTime()==null){
												driverDrivingReport.put("checkoutTime","Not yet Checked Out");
												driverReport.put("checkoutTime","Not yet Checked Out");
											}else{
												CheckoutTimeFlg= dateFormatter.format(drivertrips.getEfmFmVehicleCheckIn().getCheckOutTime());												
											}											
											Cell checkoutTimeCol = insideRow.createCell(checkoutTimeColIndex);
											checkoutTimeCol.setCellValue(CheckoutTimeFlg);
										}	
										if(assignRoute.getTotalWorkingHoursFlg()==1){
											String workingHours="Not yetChecked Out";
											if(drivertrips.getEfmFmVehicleCheckIn().getCheckOutTime()!=null){
												long workingHoursValue = drivertrips.getEfmFmVehicleCheckIn().getCheckOutTime().getTime() - drivertrips.getEfmFmVehicleCheckIn().getCheckInTime().getTime();
												workingHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(workingHoursValue),
														TimeUnit.MILLISECONDS.toMinutes(workingHoursValue)
																- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(workingHoursValue)),
														TimeUnit.MILLISECONDS.toSeconds(workingHoursValue)
																- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(workingHoursValue)));											
											}else{
												driverReport.put("totalWorkingHours","Not yetChecked Out");
												driverDrivingReport.put("totalWorkingHours","Not yetChecked Out");												
											}
											
											Cell totalWorkingHoursCol = insideRow.createCell(totalWorkingHoursColIndex);
											totalWorkingHoursCol.setCellValue(workingHours);
										}										
										if(assignRoute.getVehicleNumberFlg()==1){											
											Cell vehicleNumberCol = insideRow.createCell(vehicleNumberColIndex);
											vehicleNumberCol.setCellValue(drivertrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
										}
										millis += drivertrips.getTripCompleteTime().getTime()- drivertrips.getTripStartTime().getTime();
										long millisRouteMilles = drivertrips.getTripCompleteTime().getTime()- drivertrips.getTripStartTime().getTime();
										log.info("millisRouteMilles" + millisRouteMilles);
										if(assignRoute.getDriverDrivingHoursPerTripFlg()==1){
											String routeTravellHours="NA";
										    routeTravellHours = String.format("%02d:%02d:%02d",TimeUnit.MILLISECONDS.toHours(millisRouteMilles),
												TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles) - TimeUnit.HOURS
												.toMinutes(TimeUnit.MILLISECONDS.toHours(millisRouteMilles)),
												TimeUnit.MILLISECONDS.toSeconds(millisRouteMilles) - TimeUnit.MINUTES
												.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles)));
										    log.info("routeTravellHours" + routeTravellHours);
										
											driverDrivingReport.put("driverDrivingHoursPerTrip", routeTravellHours);
											Cell driverDrivingHoursPerTripCol = insideRow.createCell(driverDrivingHoursPerTripColIndex);
											driverDrivingHoursPerTripCol.setCellValue(routeTravellHours);
										}
										if(assignRoute.getVendorNameFlg()==1){
											Cell vendorNameCol = insideRow.createCell(vendorNameColIndex);
											vendorNameCol.setCellValue( drivertrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
										}
										if(assignRoute.getDriverNameFlg()==1){
											driverDrivingReport.put("driverName",drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
											Cell driverNameCol = insideRow.createCell(driverNameColIndex);
											driverNameCol.setCellValue(drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
										}
										if(assignRoute.getDriverIdFlg()==1){
											driverDrivingReport.put("driverId",drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
											Cell driverIdCol = insideRow.createCell(driverIdColIndex);			
											driverIdCol.setCellValue(drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
										}
										if(assignRoute.getTripTypeFlg()==1){
											driverDrivingReport.put("tripType", drivertrips.getTripType());
											Cell tripTypeCol = insideRow.createCell(tripTypeColIndex);
											tripTypeCol.setCellValue(drivertrips.getTripType());
										}
										if(assignRoute.getRouteNameFlg()==1){									
											driverDrivingReport.put("routeName",drivertrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
											Cell routeNameCol = insideRow.createCell(routeNameColIndex);
											routeNameCol.setCellValue(drivertrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
										}
										if(assignRoute.getRouteIdFlg()==1){
											driverDrivingReport.put("routeId",drivertrips.getAssignRouteId());
											Cell tripIdCell = insideRow.createCell(0);
											tripIdCell.setCellValue(drivertrips.getAssignRouteId());
										}

										driverDrivingReportList.add(driverDrivingReport);
									}									
									String travellHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
											TimeUnit.MILLISECONDS.toMinutes(millis)
													- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
											TimeUnit.MILLISECONDS.toSeconds(millis)
													- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
									driverReport.put("tripCount", allTripDetailsByDriverId.size());
									if(assignRoute.getTotalDrivingHoursFlg()==1){
										driverReport.put("totalDrivingHours", travellHours);
										Cell routeCloseTimeCol = insideRow.createCell(TotalDrivingHoursFlgColIndex);
										routeCloseTimeCol.setCellValue(travellHours);
									}
									driverReport.put("tripsDetails", driverDrivingReportList);
									dynamicReport.add(driverReport);
								}

							}
						}
					}

			}
			// allTrips.put("data", trip);
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
	
	/*below service used for the dynamic report Structure of vendors*/
	
    @POST
    @Path("/dynamicVendor")
    public Response vendorByVehicleDetails(EFmFmVendorMasterPO efmFmVendorMaster) {
        IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
        IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),efmFmVendorMaster.getUserId()))){
        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(efmFmVendorMaster.getUserId());
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
   	    log.info("serviceStart -UserId :" + efmFmVendorMaster.getUserId());
        Map<String, Object> headerDetails = new HashMap<String, Object>();                 
        List<Map<String, Object>> vendorByVehicle = new ArrayList<Map<String, Object>>();  
        List<EFmFmVendorMasterPO> listOfvendor = iVendorDetailsBO.getAllEnableVendorsDetails(efmFmVendorMaster.getCombinedFacility());        
        if (!(listOfvendor.isEmpty())) {
        	headerDetails = new HashMap<String, Object>(); 
        	headerDetails.put("name","<strong>Vendor Details - Click Here To Select All</strong>");
        	headerDetails.put("msGroup",true);
        	vendorByVehicle.add(headerDetails);
        	headerDetails = new HashMap<String, Object>(); 
            headerDetails.put("category","Vendor Details");
            headerDetails.put("dynamicDataFlg","all");
            headerDetails.put("ticked",false);
            headerDetails.put("name","All");
            headerDetails.put("vendorId","All"); 
            vendorByVehicle.add(headerDetails);
            for (EFmFmVendorMasterPO vendorList : listOfvendor) {
                Map<String, Object> vendorDetails = new HashMap<String, Object>();                
                vendorDetails.put("category","Vendor Details");
                vendorDetails.put("dynamicDataFlg",vendorList.getVendorName().replaceAll(" ",""));                
                vendorDetails.put("name", vendorList.getVendorName());
                vendorDetails.put("vendorId", vendorList.getVendorId());                
                vendorDetails.put("ticked",false);
                vendorByVehicle.add(vendorDetails);
            }

            
            headerDetails = new HashMap<String, Object>(); 
            headerDetails.put("msGroup",false);
            vendorByVehicle.add(headerDetails);
           
        }
   	    log.info("serviceEnd -UserId :" + efmFmVendorMaster.getUserId());
        return Response.ok(vendorByVehicle, MediaType.APPLICATION_JSON).build();
    }
	
	
	
	
	

}