package com.newtglobal.eFmFmFleet.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
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

import com.newtglobal.eFmFmFleet.business.bo.IAlertBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmActualRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripTimingMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/report")
@Consumes("application/json")
@Produces("application/json")
public class ReportService {

	private static Log log = LogFactory.getLog(ReportService.class);
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();


	/**
	 * The tripSheet Details method implemented. for getting the list of trip
	 * sheet Report
	 *  
	 * @author Rajan R
	 * 
	 * @since 2015-05-06
	 * 
	 * @return tripsheet details.
	 * @throws ParseException
	 */

	/**
	 * @param assignRoutePO
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	
	/*
	 * ExcelImport
	 */
	
	@POST
	@Path("/tripSheetDownload")
	public Response tripSheetDownload(EFmFmAssignRoutePO assignRoutePO) throws ParseException, UnsupportedEncodingException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
		 
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		Date fromDate = (Date) formatter.parse(assignRoutePO.getFromDate());
		Date toDate = (Date) formatter.parse(assignRoutePO.getToDate());
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
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
        
        
        int rownum = 0, noOfRoute = 0;
        Row insideRow = sheet.createRow(rownum++); 
        for (int columnIndex = 0; columnIndex < 17; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
		Cell tripId= insideRow.createCell(0);
		tripId.setCellValue("Trip Id");		
		tripId.setCellStyle(style);
		
		Cell tripAssignTime= insideRow.createCell(1);
		tripAssignTime.setCellValue("Trip Assign Time");
		tripAssignTime.setCellStyle(style);
		
		Cell tripStartTime= insideRow.createCell(2);
		tripStartTime.setCellValue("Trip Start Time");
		tripStartTime.setCellStyle(style);
		
		Cell tripCompletedTimeHeader= insideRow.createCell(3);
		tripCompletedTimeHeader.setCellValue("Trip Completed Time");
		tripCompletedTimeHeader.setCellStyle(style);
		
		Cell shiftTime= insideRow.createCell(4);
		shiftTime.setCellValue("Shift Time");
		shiftTime.setCellStyle(style);
		
		Cell routeName= insideRow.createCell(5);
		routeName.setCellValue("Route Name");
		routeName.setCellStyle(style);
		
		Cell vehicleNo= insideRow.createCell(6);
		vehicleNo.setCellValue("Vehicle No");
		vehicleNo.setCellStyle(style);
		
		Cell driverName= insideRow.createCell(7);
		driverName.setCellValue("Driver Name");
		driverName.setCellStyle(style);
		
		Cell deviceId= insideRow.createCell(8);
		deviceId.setCellValue("Device Id");
		deviceId.setCellStyle(style);
		
		Cell escortHeader= insideRow.createCell(9);
		escortHeader.setCellValue("Escort");
		escortHeader.setCellStyle(style);
		
		Cell typeHeader= insideRow.createCell(10);
		typeHeader.setCellValue("Type");
		typeHeader.setCellStyle(style);
		
		Cell empDetails= insideRow.createCell(11);
		empDetails.setCellValue("Emp Details");
		empDetails.setCellStyle(style);
		
		Cell plannedDistance= insideRow.createCell(12);
		plannedDistance.setCellValue("Planned Distance (KM)");
		plannedDistance.setCellStyle(style);
		
		Cell GPSTraveledDistance= insideRow.createCell(13);
		GPSTraveledDistance.setCellValue("GPS Traveled Distance (KM)");
		GPSTraveledDistance.setCellStyle(style);
		
		Cell odoMeterDistanceRow= insideRow.createCell(14);
		odoMeterDistanceRow.setCellValue("Odometer Distance (KM)");
		odoMeterDistanceRow.setCellStyle(style);
		
		Cell editableDistanceRow= insideRow.createCell(15);
		editableDistanceRow.setCellValue("Editable Distance (KM)");
		editableDistanceRow.setCellStyle(style);
		
		Cell vendorName= insideRow.createCell(16);
		vendorName.setCellValue("Vendor Name");
		vendorName.setCellStyle(style);
		
		Cell cabType= insideRow.createCell(17);
		cabType.setCellValue("Cab Type");
		cabType.setCellStyle(style);
		
		Cell facilityName= insideRow.createCell(18);
		facilityName.setCellValue("Facility Name");
		facilityName.setCellStyle(style);

		
		
		
	List<EFmFmAssignRoutePO> allTripDetails=null;
		if(assignRoutePO.getRequestType().equalsIgnoreCase("All")){
			allTripDetails = iAssignRouteBO.getAllTripByDate(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		}else{
			allTripDetails = iAssignRouteBO.getAllTripByDateEmporGuest(fromDate,toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),assignRoutePO.getRequestType());
		}

		List<Map<String, Object>> trip = new ArrayList<Map<String, Object>>();
		log.info("sizeof the Report" + allTripDetails.size() +"fromDate"+fromDate+"toDate"+toDate+"---"+assignRoutePO.getRequestType());
//		List<String> dateList = new ArrayList<String>();
//		String date = "";
		boolean firstRow=false;
		if (!(allTripDetails.isEmpty())) {
			if(allTripDetails.get(0).geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP")){
				Cell suggestiveDistance	= insideRow.createCell(18);
				suggestiveDistance.setCellValue("Suggestive Distance");
				suggestiveDistance.setCellStyle(style);
				}
			/*outer: for (EFmFmAssignRoutePO trips : allTripDetails) {
				if (!(dateList.contains(formatter.format(trips.getTripAssignDate())))) {
					date = formatter.format(trips.getTripAssignDate());
					dateList.add(formatter.format(trips.getTripAssignDate()));
				} else {
					continue outer;
				}*/
			
				Map<String, Object> detailTrip = new HashMap<String, Object>();
				List<Map<String, Object>> tripList = new ArrayList<Map<String, Object>>();
				for (EFmFmAssignRoutePO tripDetails : allTripDetails) {
					insideRow = sheet.createRow(rownum++);                    
					//if (date.equalsIgnoreCase(formatter.format(tripDetails.getTripAssignDate()))) {
						Map<String, Object> detailTripList = new HashMap<String, Object>();
						detailTripList.put("routeId", tripDetails.getAssignRouteId());
						
						Cell tripIdCell = insideRow.createCell(0);
						tripIdCell.setCellValue(tripDetails.getAssignRouteId());
						
						detailTripList.put("routeName",
								tripDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());						
						Cell routeNameCell = insideRow.createCell(5);
						routeNameCell.setCellValue(tripDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());					
						detailTripList.put("plannedDistance", tripDetails.getPlannedDistance());
						Cell plannedDistanceCell = insideRow.createCell(12);
						plannedDistanceCell.setCellValue(tripDetails.getPlannedDistance());
						detailTripList.put("editedDistance", tripDetails.getEditedTravelledDistance());
						Cell odoMeterDistanceCell = insideRow.createCell(14);												
						if(tripDetails.getOdometerEndKm()!=null ){
							odoMeterDistanceCell.setCellValue((Double.parseDouble(tripDetails.getOdometerEndKm())-Double.parseDouble(tripDetails.getOdometerStartKm())));
						}else{
							odoMeterDistanceCell.setCellValue("0");
						}						
						Cell editedDistanceCell = insideRow.createCell(15);
						editedDistanceCell.setCellValue(tripDetails.getEditedTravelledDistance());
						try {
							Cell travelledDistanceCell = insideRow.createCell(13);
							if (tripDetails.getTravelledDistance() == 0) {
								detailTripList.put("travelledDistance", "NA");							
								travelledDistanceCell.setCellValue("NA");
							} else {
								String extensionRemoved = Double.toString(tripDetails.getTravelledDistance())
										.split("\\.")[1];
								if (!(extensionRemoved.equalsIgnoreCase("0"))) {
									detailTripList.put("travelledDistance",
											Math.round((double) tripDetails.getTravelledDistance()));									
									travelledDistanceCell.setCellValue(Math.round((double) tripDetails.getTravelledDistance()));
								} else {
									detailTripList.put("travelledDistance", tripDetails.getTravelledDistance());									
									travelledDistanceCell.setCellValue(tripDetails.getTravelledDistance());
								}
							}
						} catch (Exception e) {
							log.info("TripSheet Error" + e);
						}
						detailTripList.put("vehicleNumber",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						
						Cell vehicleNumberCell = insideRow.createCell(6);
						vehicleNumberCell.setCellValue(tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						detailTripList.put("driverName",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						
						Cell driverNameCell = insideRow.createCell(7);
						driverNameCell.setCellValue(tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());								
						detailTripList.put("deviceId",
								tripDetails.getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId());
						
						Cell vendorNameCell = insideRow.createCell(16);
						vendorNameCell.setCellValue(tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getEfmFmVendorMaster().getVendorName());
						detailTripList.put("vendorName",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getEfmFmVendorMaster().getVendorName());			
						
						Cell cabTypeCell = insideRow.createCell(17);
						cabTypeCell.setCellValue(tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
						detailTripList.put("cabType",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());

						Cell deviceIdCell = insideRow.createCell(8);
						deviceIdCell.setCellValue(tripDetails.getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId());						
						detailTripList.put("escortRequired", "NotRequired");
						
						Cell facilityNameCell = insideRow.createCell(18);
						facilityNameCell.setCellValue(tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
						
						
						detailTripList.put("facilityName",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

						
						Cell escortRequiredCell = insideRow.createCell(9);						
						escortRequiredCell.setCellValue("NotRequired");	
						
						if (tripDetails.getEscortRequredFlag().equalsIgnoreCase("Y")) {
							try {
								int a = tripDetails.geteFmFmEscortCheckIn().getEscortCheckInId();
								detailTripList.put("escortRequired",tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());								
							   escortRequiredCell.setCellValue(tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName()+"|"+tripDetails.geteFmFmEscortCheckIn().getEscortCheckInId());								
								detailTripList.put("escortId",tripDetails.geteFmFmEscortCheckIn().getEscortCheckInId());								
								log.info("checkInId" + a);
							} catch (Exception e) {
								detailTripList.put("escortRequired", "Escort Required But Not Available");
								log.info("escortRequired" + e);
							}
						} else {
							detailTripList.put("escortRequired", "Not Required");
						}
						try{
						detailTripList.put("tripStartDate", dateFormatter.format(tripDetails.getTripStartTime()));
						}catch(Exception e){
							log.info("tripStartDate Error" + e);
//							detailTripList.put("tripStartDate", dateFormatter.format(tripDetails.getTripStartTime()));
						}						
						Cell tripStartDateCell = insideRow.createCell(2);						
						try{
							tripStartDateCell.setCellValue(dateFormatter.format(tripDetails.getTripStartTime()));
							}catch(Exception e){
								log.info("tripStartDateCell Error" + e);
//								detailTripList.put("tripStartDate", dateFormatter.format(tripDetails.getTripStartTime()));
							}						
//						detailTripList.put("tripAssignDate", dateFormatter.format(tripDetails.getTripAssignDate()));
						detailTripList.put("tripAssignDate",dateFormatter.format(tripDetails.getCreatedDate()));

						Cell tripAssignDateCell = insideRow.createCell(1);
						tripAssignDateCell.setCellValue( dateFormatter.format(tripDetails.getCreatedDate()));
						
						detailTripList.put("tripCreateDate", dateFormatter.format(tripDetails.getCreatedDate()));
						detailTripList.put("tripCompleteDate", dateFormatter.format(tripDetails.getTripCompleteTime()));
						Cell tripCompleteDateCell = insideRow.createCell(3);
						tripCompleteDateCell.setCellValue(dateFormatter.format(tripDetails.getTripCompleteTime()));
						
						detailTripList.put("tripType", tripDetails.getTripType());
						
						Cell tripTypeCell = insideRow.createCell(10);
						tripTypeCell.setCellValue(tripDetails.getTripType());		
						
						List<Map<String, Object>> pickupDetails = new ArrayList<Map<String, Object>>();
						List<EFmFmEmployeeTripDetailPO> employeeDetails = iCabRequestBO
								.getParticularTripAllEmployees(tripDetails.getAssignRouteId());
						if (!(employeeDetails.isEmpty())) {
							firstRow=false;
							detailTripList.put("shitTime", timeFormat.format(tripDetails.getShiftTime()));							
							Cell shitTimeCells = insideRow.createCell(4);
							shitTimeCells.setCellValue(timeFormat.format(tripDetails.getShiftTime()));	
							if(tripDetails.geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP")){
							long suggestiveDistanceExcel=0;
							if (tripDetails.getTripType().equalsIgnoreCase("PICKUP")) {
								
							    suggestiveDistanceExcel=(((employeeDetails.get(0).geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaWiseDistance())*2)+((employeeDetails.size()-1)*2));								
							}
							else{
					        	List<EFmFmEmployeeTripDetailPO> employeeTripData = iCabRequestBO.getLastDropEmployeeDetail(tripDetails.getAssignRouteId());					        	
							    log.info("size"+employeeTripData.size());
							    if(!employeeTripData.isEmpty()){
							    	suggestiveDistanceExcel=(((employeeTripData.get(0).geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaWiseDistance())*2)+((employeeTripData.size()-1)*2));
							    }
							}
														
							Cell suggestiveDistanceCell = insideRow.createCell(18);
							suggestiveDistanceCell.setCellValue(suggestiveDistanceExcel);
							detailTripList.put("suggestiveDistanceCell",
									suggestiveDistanceExcel);
							}
							
							for (EFmFmEmployeeTripDetailPO employeeList : employeeDetails) {
								Map<String, Object> empList = new HashMap<String, Object>();
								if(firstRow){
									insideRow = sheet.createRow(rownum++); 
								}
								
								empList.put("empId", employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getEmployeeId());
								Cell empIdCell = insideRow.createCell(11);
								
							
								
								log.info("EmpId" + employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getEmployeeId());
								empList.put("empName",
										new String(Base64.getDecoder().decode(employeeList
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
										"utf-8"));
								if (new String(
										Base64.getDecoder()
												.decode(employeeList.geteFmFmEmployeeTravelRequest()
														.getEfmFmUserMaster().getGender()),
										"utf-8").equalsIgnoreCase("Male")) {
									empList.put("empGender", 1);
								} else {
									empList.put("empGender", 2);
								}								
								if(tripDetails.getRouteGenerationType().equalsIgnoreCase("nodal")){
									empList.put("empAddress",
											employeeList
													.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());
									empIdCell.setCellValue("Id-"+employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
											.getEmployeeId()+"|Name-"+new String(Base64.getDecoder().decode(employeeList
													.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
											"utf-8")+"|Address"+employeeList
											.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());	
								}
								else{
									empList.put("empAddress",
											new String(Base64.getDecoder().decode(employeeList
													.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()),
											"utf-8"));	
									
									empIdCell.setCellValue("Id-"+employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
											.getEmployeeId()+"|Name-"+new String(Base64.getDecoder().decode(employeeList
													.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
											"utf-8")+"|Address"+new String(Base64.getDecoder().decode(employeeList
													.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()),
											"utf-8"));										
									
								}								
								empList.put("shiftTime", shiftFormate
										.format(employeeDetails.get(0).getEfmFmAssignRoute().getShiftTime()));
								empList.put("scheduleTime", timeFormat.format(employeeList.getActualTime()));
								if (tripDetails.getTripType().equalsIgnoreCase("PICKUP")) {
									try {
										if (!(employeeList.getCabstartFromDestination() == 0)
												|| !(employeeList.getPickedUpDateAndTime() == 0)) {
											if (!(employeeList.getCabstartFromDestination() == 0)
													&& (employeeList.getBoardedFlg().equalsIgnoreCase("NO"))) {
												empList.put("travelTime",
														timeFormat.format(employeeList.getCabstartFromDestination()));
											}
											if (!(employeeList.getPickedUpDateAndTime() == 0)
													&& (employeeList.getBoardedFlg().equalsIgnoreCase("B"))) {
												empList.put("travelTime",
														timeFormat.format(employeeList.getPickedUpDateAndTime()));
											}
										} else {
											empList.put("travelTime", "0");

										}
									} catch (Exception e) {
										empList.put("travelTime", "0");
										log.info("Error in drop type" + e);
									}
								}
								if (tripDetails.getTripType().equalsIgnoreCase("DROP")) {
									try {
										if (!(employeeList.getCabstartFromDestination() == 0)
												|| !(employeeList.getPickedUpDateAndTime() == 0)) {
											if (!(employeeList.getCabstartFromDestination() == 0)
													&& (employeeList.getBoardedFlg().equalsIgnoreCase("NO"))) {
												empList.put("travelTime",
														timeFormat.format(employeeList.getCabstartFromDestination()));
											}
											if (!(employeeList.getPickedUpDateAndTime() == 0)
													&& (employeeList.getBoardedFlg().equalsIgnoreCase("D"))) {
												empList.put("travelTime",
														timeFormat.format(employeeList.getPickedUpDateAndTime()));
											}
										} else {
											empList.put("travelTime", "0");

										}
									} catch (Exception e) {
										empList.put("travelTime", "0");
										e.printStackTrace();
										log.info("Error in drop type" + e);
									}
								}
								if (tripDetails.getTripType().equalsIgnoreCase("PICKUP")) {
									empList.put("scheduleTime", timeFormat
											.format(employeeList.geteFmFmEmployeeTravelRequest().getPickUpTime()));
									if (employeeList.getBoardedFlg().equalsIgnoreCase("B")) {
										empList.put("travelStatus", "Boarded");
									} else if (employeeList.getBoardedFlg().equalsIgnoreCase("NO")) {
										empList.put("travelStatus", "No Show");
									} else if (employeeList.getBoardedFlg().equalsIgnoreCase("N")) {
										empList.put("travelStatus", "Yet to PickUp");
									}
								}
								if (tripDetails.getTripType().equalsIgnoreCase("DROP")) {
									if (employeeList.getBoardedFlg().equalsIgnoreCase("D")) {
										empList.put("travelStatus", "Dropped");
									} else if (employeeList.getBoardedFlg().equalsIgnoreCase("NO")) {
										empList.put("travelStatus", "No Show");
									} else if (employeeList.getBoardedFlg().equalsIgnoreCase("N")) {
										empList.put("travelStatus", "Yet to Drop");
									}
								}
								pickupDetails.add(empList);
								firstRow=true;
							}

						}
						detailTripList.put("empDetails", pickupDetails);
						tripList.add(detailTripList);
					//}
					detailTrip.put("tripDetail", tripList);
					detailTrip.put("tripAssignDate", formatter.format(tripDetails.getTripAssignDate()));

				}
				trip.add(detailTrip);

			//}
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
	
	@POST
	@Path("/tripSheet")
	public Response tripSheet(EFmFmAssignRoutePO assignRoutePO) throws ParseException, UnsupportedEncodingException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}
		 
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		log.info("assignRoutePO.getRequestType()="+assignRoutePO.getRequestType());
		List<EFmFmAssignRoutePO> allTripDetails =null;
		if(assignRoutePO.getRequestType().equalsIgnoreCase("All")){
			allTripDetails = iAssignRouteBO.getAllTripByDate(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		}else{		
			allTripDetails = iAssignRouteBO.getAllTripByDateEmporGuest(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),assignRoutePO.getRequestType());
		}
		
		log.info("Route Size"+allTripDetails.size());
		List<Map<String, Object>> trip = new ArrayList<Map<String, Object>>();
		List<String> dateList = new ArrayList<String>();
		String date = "";
		if (!(allTripDetails.isEmpty())) {
			outer: for (EFmFmAssignRoutePO trips : allTripDetails) {
				if (!(dateList.contains(formatter.format(trips.getTripAssignDate())))) {
					date = formatter.format(trips.getTripAssignDate());
					dateList.add(formatter.format(trips.getTripAssignDate()));
				} else {
					continue outer;
				}
				Map<String, Object> detailTrip = new HashMap<String, Object>();
				List<Map<String, Object>> tripList = new ArrayList<Map<String, Object>>();
				for (EFmFmAssignRoutePO tripDetails : allTripDetails) {
					if (date.equalsIgnoreCase(formatter.format(tripDetails.getTripAssignDate()))) {
						Map<String, Object> detailTripList = new HashMap<String, Object>();
						detailTripList.put("routeId", tripDetails.getAssignRouteId());
						detailTripList.put("routeName",
								tripDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
						detailTripList.put("endKm", tripDetails.getOdometerEndKm());
						detailTripList.put("startKm", tripDetails.getOdometerStartKm());
						detailTripList.put("gpsKmModification",tripDetails.geteFmFmClientBranchPO().getGpsKmModification());
						log.info("AssignRoute Id"+tripDetails.getOdometerStartKm() +"--"+tripDetails.getOdometerEndKm());
						
						if(tripDetails.getOdometerEndKm()!=null && !tripDetails.getOdometerEndKm().equalsIgnoreCase("0")){
							detailTripList.put("odoDistance",(Double.parseDouble(tripDetails.getOdometerEndKm())-Double.parseDouble(tripDetails.getOdometerStartKm())));
						}else{
							detailTripList.put("odoDistance","0");
						}
						detailTripList.put("plannedDistance", tripDetails.getPlannedDistance());
						detailTripList.put("editedDistance", tripDetails.getEditedTravelledDistance());
						try {
							if (tripDetails.getTravelledDistance() == 0) {
								detailTripList.put("travelledDistance", "NA");
							} else {
								String extensionRemoved = Double.toString(tripDetails.getTravelledDistance())
										.split("\\.")[1];
								if (!(extensionRemoved.equalsIgnoreCase("0"))) {
									detailTripList.put("travelledDistance",
											Math.round(tripDetails.getTravelledDistance()));
								} else {
									detailTripList.put("travelledDistance", tripDetails.getTravelledDistance());
								}
							}
						} catch (Exception e) {
							log.info("TripSheet Error" + e);
						}
						detailTripList.put("vehicleNumber",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						detailTripList.put("driverName",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						detailTripList.put("deviceNumber",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
						detailTripList.put("deviceId",
								tripDetails.getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId());
						detailTripList.put("facilityName",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
						
						detailTripList.put("escortRequired", "NotRequired");
						if (tripDetails.getEscortRequredFlag().equalsIgnoreCase("Y")) {
							try {
								int a = tripDetails.geteFmFmEscortCheckIn().getEscortCheckInId();
								detailTripList.put("escortRequired",
										tripDetails.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
								detailTripList.put("escortId",
										tripDetails.geteFmFmEscortCheckIn().getEscortCheckInId());
								log.info("checkInId" + a);
							} catch (Exception e) {
								detailTripList.put("escortRequired", "Escort Required But Not Available");
								log.info("escortRequired" + e);
							}
						} else {
							detailTripList.put("escortRequired", "Not Required");
						}
						
						log.info("AssignRoute Id"+tripDetails.getAssignRouteId());
						try{
						detailTripList.put("tripStartDate", dateFormatter.format(tripDetails.getTripStartTime()));
						}catch(Exception e){
//							detailTripList.put("tripStartDate", dateFormatter.format(tripDetails.getTripAssignDate()));
							log.info("start time error"+e);
						}
				//		detailTripList.put("tripAssignDate", dateFormatter.format(tripDetails.getTripAssignDate()));
						detailTripList.put("tripAssignDate",dateFormatter.format(tripDetails.getCreatedDate()));
						detailTripList.put("tripCreateDate", dateFormatter.format(tripDetails.getCreatedDate()));
						try{						
						detailTripList.put("routeCloseDate", dateFormatter.format(tripDetails.getAllocationMsgDeliveryDate()));
						}catch(Exception e){
							detailTripList.put("routeCloseDate", "Not Closed");
							log.info("Error in routeCloseDate"+e);
						}

						detailTripList.put("tripCompleteDate", dateFormatter.format(tripDetails.getTripCompleteTime()));
						detailTripList.put("tripType", tripDetails.getTripType());
						List<Map<String, Object>> pickupDetails = new ArrayList<Map<String, Object>>();
						List<EFmFmEmployeeTripDetailPO> employeeDetails = iCabRequestBO
								.getParticularTripAllEmployees(tripDetails.getAssignRouteId());
						if (!(employeeDetails.isEmpty())) {
							detailTripList.put("shitTime", timeFormat.format(tripDetails.getShiftTime()));

							for (EFmFmEmployeeTripDetailPO employeeList : employeeDetails) {
								Map<String, Object> empList = new HashMap<String, Object>();
								empList.put("empId", employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getEmployeeId());
								log.info("EmpId" + employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getEmployeeId());
								empList.put("empName",
										new String(Base64.getDecoder().decode(employeeList
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
										"utf-8"));
								if (new String(
										Base64.getDecoder()
												.decode(employeeList.geteFmFmEmployeeTravelRequest()
														.getEfmFmUserMaster().getGender()),
										"utf-8").equalsIgnoreCase("Male")) {
									empList.put("empGender", 1);
								} else {
									empList.put("empGender", 2);
								}
								if(tripDetails.getRouteGenerationType().equalsIgnoreCase("nodal")){
									empList.put("empAddress",
											employeeList
													.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());
								}
								else{
									empList.put("empAddress",
											new String(Base64.getDecoder().decode(employeeList
													.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()),
											"utf-8"));	
								}
								
								empList.put("shiftTime", shiftFormate
										.format(employeeDetails.get(0).getEfmFmAssignRoute().getShiftTime()));
								empList.put("scheduleTime", timeFormat.format(employeeList.getActualTime()));
								if (tripDetails.getTripType().equalsIgnoreCase("PICKUP")) {
									try {
										if (!(employeeList.getCabstartFromDestination() == 0)
												|| !(employeeList.getPickedUpDateAndTime() == 0)) {
											if (!(employeeList.getCabstartFromDestination() == 0)
													&& (employeeList.getBoardedFlg().equalsIgnoreCase("NO"))) {
												empList.put("travelTime",
														timeFormat.format(employeeList.getCabstartFromDestination()));
											}
											if (!(employeeList.getPickedUpDateAndTime() == 0)
													&& (employeeList.getBoardedFlg().equalsIgnoreCase("B"))) {
												empList.put("travelTime",
														timeFormat.format(employeeList.getPickedUpDateAndTime()));
											}
										} else {
											empList.put("travelTime", "0");

										}
									} catch (Exception e) {
										empList.put("travelTime", "0");
										log.info("Error in drop type" + e);
									}
								}
								if (tripDetails.getTripType().equalsIgnoreCase("DROP")) {
									try {
										if (!(employeeList.getCabstartFromDestination() == 0)
												|| !(employeeList.getPickedUpDateAndTime() == 0)) {
											if (!(employeeList.getCabstartFromDestination() == 0)
													&& (employeeList.getBoardedFlg().equalsIgnoreCase("NO"))) {
												empList.put("travelTime",
														timeFormat.format(employeeList.getCabstartFromDestination()));
											}
											if (!(employeeList.getPickedUpDateAndTime() == 0)
													&& (employeeList.getBoardedFlg().equalsIgnoreCase("D"))) {
												empList.put("travelTime",
														timeFormat.format(employeeList.getPickedUpDateAndTime()));
											}
										} else {
											empList.put("travelTime", "0");

										}
									} catch (Exception e) {
										empList.put("travelTime", "0");
										log.info("Error in drop type" + e);
									}
								}
								if (tripDetails.getTripType().equalsIgnoreCase("PICKUP")) {
									empList.put("scheduleTime", timeFormat
											.format(employeeList.geteFmFmEmployeeTravelRequest().getPickUpTime()));
									if (employeeList.getBoardedFlg().equalsIgnoreCase("B")) {
										empList.put("travelStatus", "Boarded");
									} else if (employeeList.getBoardedFlg().equalsIgnoreCase("NO")) {
										empList.put("travelStatus", "No Show");
									} else if (employeeList.getBoardedFlg().equalsIgnoreCase("N")) {
										empList.put("travelStatus", "Yet to PickUp");
									}
								}
								if (tripDetails.getTripType().equalsIgnoreCase("DROP")) {
									if (employeeList.getBoardedFlg().equalsIgnoreCase("D")) {
										empList.put("travelStatus", "Dropped");
									} else if (employeeList.getBoardedFlg().equalsIgnoreCase("NO")) {
										empList.put("travelStatus", "No Show");
									} else if (employeeList.getBoardedFlg().equalsIgnoreCase("N")) {
										empList.put("travelStatus", "Yet to Drop");
									}
								}
								pickupDetails.add(empList);
							}
							long suggestiveDistance=0;
							if (tripDetails.getTripType().equalsIgnoreCase("PICKUP")) {
								suggestiveDistance=(((employeeDetails.get(0).geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaWiseDistance())*2)+((employeeDetails.size()-1)*2));								
							    log.info("suggestiveDistance"+suggestiveDistance);
							}
							else{
					        	List<EFmFmEmployeeTripDetailPO> employeeTripData = iCabRequestBO.getLastDropEmployeeDetail(tripDetails.getAssignRouteId());					        	
							    log.info("size"+employeeTripData.size());
							    if(!employeeTripData.isEmpty()){
							    	suggestiveDistance=(((employeeTripData.get(0).geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaWiseDistance())*2)+((employeeTripData.size()-1)*2));
							    }
							}
							detailTripList.put("suggestiveDistance", suggestiveDistance);
						}else{
							detailTripList.put("suggestiveDistance", "0");
						}
						detailTripList.put("empDetails", pickupDetails);
						tripList.add(detailTripList);
					}
					detailTrip.put("tripDetail", tripList);
					detailTrip.put("tripAssignDate", date);

				}
				trip.add(detailTrip);

			}
			// allTrips.put("data", trip);
		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(trip, MediaType.APPLICATION_JSON).build();
	}
	
	
	@POST
	@Path("/tripDetailsforUpdate")
	public Response tripDetailsforUpdate(EFmFmAssignRoutePO assignRoutePO) throws ParseException, UnsupportedEncodingException {		
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}
		
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		 DateFormat shiftTimeFormat = new SimpleDateFormat("HH:mm:ss");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());	
	    String shiftTime = assignRoutePO.getTime();
        Date shift = timeFormat.parse(shiftTime);
        java.sql.Time dateShiftTime = new java.sql.Time(shift.getTime());
        List<Map<String, Object>> trip = new ArrayList<Map<String, Object>>();
        List<EFmFmAssignRoutePO> allTripDetails =null;
        if(assignRoutePO.getVehicleId()==0){        	  
        	  allTripDetails = iAssignRouteBO.getAllTripByShiftTimeAndTripType(fromDate,toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),assignRoutePO.getTripType(),shiftTimeFormat.format(dateShiftTime));
        }else{
        	  allTripDetails = iAssignRouteBO.getAllTripDetailsByVehicleId(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),assignRoutePO.getVehicleId());
        }			
		List<String> dateList = new ArrayList<String>();
		String date = "";
		if (!(allTripDetails.isEmpty())) {
			outer: for (EFmFmAssignRoutePO trips : allTripDetails) {
				if (!(dateList.contains(formatter.format(trips.getTripAssignDate())))) {
					date = formatter.format(trips.getTripAssignDate());
					dateList.add(formatter.format(trips.getTripAssignDate()));
				} else {
					continue outer;
				}				
				for (EFmFmAssignRoutePO tripDetails : allTripDetails) {
					if (date.equalsIgnoreCase(formatter.format(tripDetails.getTripAssignDate()))) {
						Map<String, Object> detailTripList = new HashMap<String, Object>();
						detailTripList.put("routeId", tripDetails.getAssignRouteId());
						detailTripList.put("routeName",tripDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
						detailTripList.put("endKm", tripDetails.getOdometerEndKm());
						detailTripList.put("startKm", tripDetails.getOdometerStartKm());
						detailTripList.put("tripType", tripDetails.getTripType());
						detailTripList.put("shiftTime", tripDetails.getShiftTime());											
						if(tripDetails.getOdometerStartKm()==null ){
							detailTripList.put("startKm", "0");
						}						
						if(tripDetails.getOdometerEndKm()==null ){
							detailTripList.put("endKm", "0");
						}						
						log.info("AssignRoute Id"+tripDetails.getOdometerStartKm() +"--"+tripDetails.getOdometerEndKm());						
						if(tripDetails.getOdometerEndKm()!=null && !tripDetails.getOdometerEndKm().equalsIgnoreCase("0")){
							detailTripList.put("odoDistance",(Double.parseDouble(tripDetails.getOdometerEndKm())-Double.parseDouble(tripDetails.getOdometerStartKm())));
						}else{
							detailTripList.put("odoDistance","0");
						}
						detailTripList.put("plannedDistance", tripDetails.getPlannedDistance());
						detailTripList.put("editedDistance", tripDetails.getEditedTravelledDistance());
						try {
							if (tripDetails.getTravelledDistance() == 0) {
									detailTripList.put("travelledDistance", "0");
							} else {
								String extensionRemoved = Double.toString(tripDetails.getTravelledDistance())
										.split("\\.")[1];
								if (!(extensionRemoved.equalsIgnoreCase("0"))) {
									detailTripList.put("travelledDistance",
											Math.round(tripDetails.getTravelledDistance()));
								} else {
									detailTripList.put("travelledDistance", tripDetails.getTravelledDistance());
								}
							}
						} catch (Exception e) {
							log.info("TripSheet Error" + e);
						}
						detailTripList.put("vehicleNumber",tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						detailTripList.put("driverName",tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						detailTripList.put("facilityName",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
						trip.add(detailTripList);

					}
			
				}
			}
		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(trip, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/updateOdometerReading")
	public Response updateOdometerReading(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();		
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");			
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
				log.info("authenticationToken error"+e);
			}
		
		String responseValue="failure";
		
		
		
		
		if(assignRoutePO.getOdometerEndKm()!=null && !assignRoutePO.getOdometerEndKm().equalsIgnoreCase("0")){
			List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
			if (!(assignRoute.isEmpty())) {
				assignRoute.get(0).setOdometerStartKm(assignRoutePO.getOdometerStartKm());
				assignRoute.get(0).setOdometerEndKm(assignRoutePO.getOdometerEndKm());
				assignRouteBO.update(assignRoute.get(0));
				 responseValue="success";
			}
		}
		responce.put("status",responseValue);
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}	
	
	
	@POST
	@Path("/bulkUpdateOdometerReading")
	public Response bulkUpdateOdometerReading(EFmFmAssignRoutePO assignRoutePO) throws ParseException {			
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
				log.info("authenticationToken error"+e);
			}
	
		List<Map<String, Object>> tripDetails = new ArrayList<Map<String, Object>>();		
		try {
				List<EFmFmAssignRoutePO> data = assignRoutePO.getTripValues();		   	 
			   	 for(EFmFmAssignRoutePO values:data){
			   		Map<String, Object> detailTripList = new HashMap<String, Object>();
			   		detailTripList.put("assignRouteId", values.getAssignRouteId());
	   	 			detailTripList.put("odometerStartKm", values.getOdometerStartKm());
	   	 			detailTripList.put("odometerEndKm", values.getOdometerEndKm());
			   	 		assignRoutePO.setAssignRouteId(values.getAssignRouteId());	
			   	 	if(values.getOdometerEndKm()!=null && values.getOdometerStartKm()!=null && values.getOdometerEndKm()!="" && values.getOdometerStartKm()!=""){
			   	 		if(Double.parseDouble(values.getOdometerEndKm()) >=  Double.parseDouble(values.getOdometerStartKm())){
					   		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
			        		if (!(assignRoute.isEmpty())) {
				        			assignRoute.get(0).setOdometerStartKm(values.getOdometerStartKm());
				        			assignRoute.get(0).setOdometerEndKm(values.getOdometerEndKm());
				        			assignRouteBO.update(assignRoute.get(0));
				        			detailTripList.put("reason","Added successfully");
				        			detailTripList.put("status","success");
					   	 			tripDetails.add(detailTripList);
			        		}
			   	 		}else{				   	 			
			   	 			detailTripList.put("reason","EndKm should be greaterthan startKm" );
			   	 			detailTripList.put("status","failure");
			   	 			tripDetails.add(detailTripList);
			   	 		}
		        		
			   	 	}else{
				   	 	detailTripList.put("reason","EndKm & startKm should not be Null values" );
				   	 	detailTripList.put("status","failure");
		   	 			tripDetails.add(detailTripList);
			   	 	}
			   	 }	   	 
	    	 } catch (Exception e) {	           
			            Map<String, Object> detailTripList = new HashMap<String, Object>();
			            detailTripList.put("reason","exception"+e );
			            detailTripList.put("status","failure");
   	 			tripDetails.add(detailTripList);
	        }  
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(tripDetails, MediaType.APPLICATION_JSON).build();
	}
		/**
	 * @param Get
	 *            all trips Kilometer by selected date range...
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/kmreports")
	public Response kiloMeterReportsByVehicleAndByVendor(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}
	
		log.info("Service called");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		List<Map<String, Object>> allVehicleDetails = new ArrayList<Map<String, Object>>();
		if (assignRoutePO.getSearchType().equalsIgnoreCase("vehicle")) {
			/*
			 * List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO
			 * .getAllTripsTravelledAndPlannedDistanceByDateAndVehicle(
			 * fromDate, toDate, assignRoutePO
			 * .geteFmFmClientBranchPO().getBranchId(),
			 * assignRoutePO.getVehicleId());
			 */
			List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllTripsTravelledAndPlannedDistanceByAllVendor(
					fromDate, toDate, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
			log.info(allTripDetails.size() + "Inside Vehicle Fun" + assignRoutePO.getSearchType());
			if (!(allTripDetails.isEmpty())) {
				for (EFmFmAssignRoutePO assignRouteDetail : allTripDetails) {
					Map<String, Object> vehicleList = new HashMap<String, Object>();
					vehicleList.put("date", formatter.format(assignRouteDetail.getTripAssignDate()));
					vehicleList.put("shiftTime", timeFormat.format(assignRouteDetail.getShiftTime()));
					//List<EFmFmAssignRoutePO> empcount =iAssignRouteBO.getEmployeeCountByTrips(assignRouteDetail.getAssignRouteId(), new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
					//vehicleList.put("totalApportunity",empcount.size());
					vehicleList.put("totalApportunity","154");
					vehicleList.put("plannedDistance", assignRouteDetail.getPlannedDistance());
					vehicleList.put("travelledDistance", assignRouteDetail.getTravelledDistance());
					vehicleList.put("vehicleType",
							assignRouteDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
					vehicleList.put("vehicleNumber",
							assignRouteDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
					vehicleList.put("vendorName", assignRouteDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
							.getEfmFmVendorMaster().getVendorName());
					vehicleList.put("facilityName",
							assignRouteDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
					allVehicleDetails.add(vehicleList);
				}
			}
		}
		// all Vendor Wice KM reports....First Template
		else if (assignRoutePO.getVendorId() == 0 && assignRoutePO.getTime().equalsIgnoreCase("1")) {
			List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllTripsTravelledAndPlannedDistanceByAllVendor(
					fromDate, toDate, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
			log.info("trips:" + allTripDetails.size());
			if (!(allTripDetails.isEmpty())) {
				List<String> vehicleNumber = new ArrayList<String>();
				for (EFmFmAssignRoutePO routeDetail : allTripDetails) {
					Map<String, Object> vehicleList = new HashMap<String, Object>();
					String tripDate = formatter.format(routeDetail.getTripAssignDate());
					// if(!(tripDates.contains(tripDate))){
					if (!(vehicleNumber
							.contains(routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber()
									+ tripDate))) {
						vehicleNumber
								.add(routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber()
										+ tripDate);
						Date tripDateMonths = formatter.parse(tripDate);
						List<EFmFmAssignRoutePO> vehicleDetails = iAssignRouteBO
								.getAllTripsVehicleDetailsByVehicleNumber(tripDateMonths, tripDateMonths,
										new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), routeDetail
												.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						log.info("vehicleDetails..firsttemplete:" + vehicleDetails.size());
						int plannedDistance = 0;
						int travelledDistance = 0;
						for (EFmFmAssignRoutePO vehicle : vehicleDetails) {
							plannedDistance += vehicle.getPlannedDistance();
							travelledDistance += vehicle.getTravelledDistance();
						}
						vehicleList.put("date", formatter.format(routeDetail.getTripAssignDate()));
					//	List<EFmFmAssignRoutePO> empcount =iAssignRouteBO.getEmployeeCountByTrips(routeDetail.getAssignRouteId(), new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
						//vehicleList.put("totalApportunity",empcount.size());
						vehicleList.put("totalApportunity", "154");
						vehicleList.put("plannedDistance", plannedDistance);
						vehicleList.put("travelledDistance", travelledDistance);
						vehicleList.put("vehicleType",
								routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
						vehicleList.put("vehicleNumber",
								routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						vehicleList.put("vendorName", routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
								.getEfmFmVendorMaster().getVendorName());
						vehicleList.put("facilityName",
								routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
						allVehicleDetails.add(vehicleList);
					}
					// }
				}
			}
		}
		// Shfit Wice Second template
		else if (assignRoutePO.getTime().equalsIgnoreCase("0") && assignRoutePO.getVendorId() == 0) {
			List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllTripsTravelledAndPlannedDistanceByAllVendor(
					fromDate, toDate, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
			log.info("trips:" + allTripDetails.size());
			if (!(allTripDetails.isEmpty())) {
				List<String> tripDates = new ArrayList<String>();
				for (EFmFmAssignRoutePO routeDetail : allTripDetails) {
					Map<String, Object> vehicleList = new HashMap<String, Object>();
					String tripDate = formatter.format(routeDetail.getTripAssignDate());
					// if(!(tripDates.contains(tripDate))){
					if (!(tripDates.contains(tripDate + routeDetail.getShiftTime()))) {
						tripDates.add(tripDate + routeDetail.getShiftTime());
						Date tripDateMonths = formatter.parse(tripDate);
						List<EFmFmAssignRoutePO> vehicleDetails = iAssignRouteBO.getAllTripsVehicleKMDetailsByShiftTime(
								tripDateMonths, tripDateMonths, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),
								routeDetail.getShiftTime());
						log.info("Shfit Wice Second:" + vehicleDetails.size());
						int plannedDistance = 0;
						int travelledDistance = 0;
						for (EFmFmAssignRoutePO vehicle : vehicleDetails) {
							plannedDistance += vehicle.getPlannedDistance();
							travelledDistance += vehicle.getTravelledDistance();
						}
						vehicleList.put("date", formatter.format(routeDetail.getTripAssignDate()));
						vehicleList.put("shiftTime", timeFormat.format(routeDetail.getShiftTime()));
						//List<EFmFmAssignRoutePO> empcount =iAssignRouteBO.getEmployeeCountByTrips(routeDetail.getAssignRouteId(), new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
						//vehicleList.put("totalApportunity",empcount.size());
						vehicleList.put("totalApportunity","154");
						vehicleList.put("plannedDistance", plannedDistance);
						vehicleList.put("travelledDistance", travelledDistance);
						vehicleList.put("vehicleType",
								routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
						vehicleList.put("vehicleNumber",
								routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						vehicleList.put("vendorName", routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
								.getEfmFmVendorMaster().getVendorName());						
						vehicleList.put("facilityName",
								routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());					
						allVehicleDetails.add(vehicleList);
					}
					// }
				}
			}

		} else {
			// Vendor wice KM reports last template
			List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllTripsTravelledAndPlannedDistanceByDate(
					fromDate, toDate, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),
					assignRoutePO.getVendorId());
			if (!(allTripDetails.isEmpty())) {
				List<String> tripDates = new ArrayList<String>();
				for (EFmFmAssignRoutePO routeDetail : allTripDetails) {
					Map<String, Object> vehicleList = new HashMap<String, Object>();
					String tripDate = formatter.format(routeDetail.getTripAssignDate());
					// if(!(tripDates.contains(tripDate))){
					if (!(tripDates.contains(tripDate))) {
						tripDates.add(tripDate);
						Date tripDateMonths = formatter.parse(tripDate);
						List<EFmFmAssignRoutePO> vehicleDetails = iAssignRouteBO
								.getAllTripsTravelledAndPlannedDistanceByDate(tripDateMonths, tripDateMonths,
										new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),
										assignRoutePO.getVendorId());
						log.info("Vendor wice KM:" + vehicleDetails.size());
						int plannedDistance = 0;
						int travelledDistance = 0;
						for (EFmFmAssignRoutePO vehicle : vehicleDetails) {
							plannedDistance += vehicle.getPlannedDistance();
							travelledDistance += vehicle.getTravelledDistance();
						}
						vehicleList.put("date", formatter.format(routeDetail.getTripAssignDate()));
						vehicleList.put("totalApportunity", "2729");
						vehicleList.put("plannedDistance", plannedDistance);
						vehicleList.put("travelledDistance", travelledDistance);
						vehicleList.put("vehicleType",
								routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
						vehicleList.put("facilityName",
								routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
						vehicleList.put("vehicleNumber",
								routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						vehicleList.put("vendorName", routeDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
								.getEfmFmVendorMaster().getVendorName());
						allVehicleDetails.add(vehicleList);
					}
					// }
				}
			}
		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allVehicleDetails, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * @param Get
	 *            all trips particular vendors vehicles kilometer by selected
	 *            date range...
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/vendorvehiclekm")
	public Response getParticularVendorsAllVehiclesDetailAtGivenDateRange(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}

		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		List<Map<String, Object>> allVehicleDetails = new ArrayList<Map<String, Object>>();
		List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllTripsTravelledAndPlannedDistanceByDate(fromDate,
				toDate, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getVendorId());
		if (!(allTripDetails.isEmpty())) {
			for (EFmFmAssignRoutePO assignRouteDetail : allTripDetails) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("vehicleNum",
						assignRouteDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				vehicleList.put("vehicleId",
						assignRouteDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				vehicleList.put("vendorName", assignRouteDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorName());
				vehicleList.put("facilityName",
						assignRouteDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				vehicleList.put("plannedDistance", assignRouteDetail.getPlannedDistance());
				try {
					if (assignRouteDetail.getTravelledDistance() == 0) {
						vehicleList.put("travelledDistance", "NA");
					} else {
						String extensionRemoved = Double.toString(assignRouteDetail.getTravelledDistance())
								.split("\\.")[1];
						if (!(extensionRemoved.equalsIgnoreCase("0"))) {
							vehicleList.put("travelledDistance",
									Math.round(assignRouteDetail.getTravelledDistance()));
						} else {
							vehicleList.put("travelledDistance", assignRouteDetail.getTravelledDistance());
						}
					}
				} catch (Exception e) {
				}
				vehicleList.put("travelledDate", formatter.format(assignRouteDetail.getTripAssignDate()));
				vehicleList.put("shiftTime", timeFormat.format(assignRouteDetail.getShiftTime()));
				vehicleList.put("tripType", assignRouteDetail.getTripType());
				
				allVehicleDetails.add(vehicleList);
			}
		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allVehicleDetails, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * @param Get
	 *            all No show employees by selected date range...
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	@POST
	@Path("/noshowemp")
	public Response getAllNoShowEmployeesDetailAtGivenDateRange(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}

		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		List<Map<String, Object>> allVehicleDetails = new ArrayList<Map<String, Object>>();
		List<EFmFmEmployeeTripDetailPO> allTripDetails = iAssignRouteBO.getAllNoShowEmployeesByDate(fromDate, toDate,
				new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));

		Map<String, Object> noShowList = new HashMap<String, Object>();
		if (!(allTripDetails.isEmpty())) {
			for (EFmFmEmployeeTripDetailPO employeeDetail : allTripDetails) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("employeeId",
						employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
				vehicleList.put("employeeAddress",
						new String(Base64.getDecoder().decode(
								employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()),
						"utf-8"));
				vehicleList.put("routeName", employeeDetail.getEfmFmAssignRoute().geteFmFmRouteAreaMapping()
						.geteFmFmZoneMaster().getZoneName());
				vehicleList.put("travelledDate", formatter.format(employeeDetail.getActualTime()));
				vehicleList.put("shiftTime", timeFormat.format(employeeDetail.getEfmFmAssignRoute().getShiftTime()));
				vehicleList.put("tripType", employeeDetail.getEfmFmAssignRoute().getTripType());				
				vehicleList.put("facilityName",
						 employeeDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				allVehicleDetails.add(vehicleList);
			}
			noShowList.put("noShowCount", allTripDetails.size());
			noShowList.put("noShowEmployees", allVehicleDetails);
		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(noShowList, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * @param Get
	 *            all employees SMS report by selected date range...
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	@POST
	@Path("/smsreport")
	public Response getAllEmployeesSMSDetailAtGivenDateRange(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}
	
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		List<Map<String, Object>> allVehicleDetails = new ArrayList<Map<String, Object>>();
		List<EFmFmEmployeeTripDetailPO> allTripDetails = iAssignRouteBO.getAllMessageEmployeesMessageDetailsByDate(
				fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		if (!(allTripDetails.isEmpty())) {
			for (EFmFmEmployeeTripDetailPO employeeDetail : allTripDetails) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				log.info("employeeId"+employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
				vehicleList.put("employeeId",
						employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
				vehicleList.put("employeeAddress",
						new String(Base64.getDecoder().decode(
								employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()),
						"utf-8"));
				vehicleList.put("routeName", employeeDetail.getEfmFmAssignRoute().geteFmFmRouteAreaMapping()
						.geteFmFmZoneMaster().getZoneName());
				vehicleList.put("employeeNumber",
						new String(Base64.getDecoder().decode(
								employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber()),
						"utf-8"));
				vehicleList.put("travelledDate", formatter.format(employeeDetail.getActualTime()));
				vehicleList.put("shiftTime", timeFormat.format(employeeDetail.getEfmFmAssignRoute().getShiftTime()));
				vehicleList.put("tripType", employeeDetail.getEfmFmAssignRoute().getTripType());
				vehicleList.put("facilityName",
						employeeDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				try {
					vehicleList.put("allocationMsgDeliveryDate",
							dateFormatter.format(employeeDetail.getEfmFmAssignRoute().getAllocationMsgDeliveryDate()));
				} catch (Exception e) {
					vehicleList.put("allocationMsgDeliveryDate", "NO");
				}
				try {
					vehicleList.put("eat15MinuteMsgDeliveryDate",
							dateFormatter.format(employeeDetail.getTenMinuteMessageDeliveryDate()));
				} catch (Exception e) {
					vehicleList.put("eat15MinuteMsgDeliveryDate", "NO");
				}
				try {
					vehicleList.put("reachedMsgDeliveryDate",
							dateFormatter.format(employeeDetail.getReachedMessageDeliveryDate()));
				} catch (Exception e) {
					vehicleList.put("reachedMsgDeliveryDate", "NO");
				}
				try {
					vehicleList.put("cabDelayMsgDeliveryDate",
							dateFormatter.format(employeeDetail.getCabDelayMsgDeliveryDate()));
				} catch (Exception e) {
					vehicleList.put("cabDelayMsgDeliveryDate", "NO");

				}
				
				try {
					if(employeeDetail.getEmpTripId()==155104){
						System.out.println("hit");
					}
					if(employeeDetail.getCabstartFromDestination()==0){
						vehicleList.put("noShowMsgDeliveryDate", "NO");
					}else{
						log.info("cab"+employeeDetail.getCabstartFromDestination());
					vehicleList.put("noShowMsgDeliveryDate",
							dateFormatter.format(new Date(employeeDetail.getCabstartFromDestination())));
					log.info("noShowMsgDeliveryDate"+dateFormatter.format(new Date(employeeDetail.getCabstartFromDestination())));
					}
				} 
					catch (Exception e) {
					vehicleList.put("noShowMsgDeliveryDate", "NO");
				}
				allVehicleDetails.add(vehicleList);
			}
		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allVehicleDetails, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/noshow")
	public Response getNoShowTripData(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, UnsupportedEncodingException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}

		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		// DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

		List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllTripByDateEmporGuest(fromDate, toDate,
				new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),assignRoutePO.getRequestType());
		List<Map<String, Object>> trip = new ArrayList<Map<String, Object>>();

		List<String> dateList = new ArrayList<String>();
		String date = "";
		if (!(allTripDetails.isEmpty())) {
			outer: for (EFmFmAssignRoutePO trips : allTripDetails) {
				if (!(dateList.contains(formatter.format(trips.getTripAssignDate())))) {
					date = formatter.format(trips.getTripAssignDate());
					dateList.add(formatter.format(trips.getTripAssignDate()));
				} else {
					continue outer;
				}
				Map<String, Object> detailTrip = new HashMap<String, Object>();
				List<Map<String, Object>> tripList = new ArrayList<Map<String, Object>>();
				for (EFmFmAssignRoutePO tripDetails : allTripDetails) {
					if (date.equalsIgnoreCase(formatter.format(tripDetails.getTripAssignDate()))) {
						Map<String, Object> detailTripList = new HashMap<String, Object>();
						detailTripList.put("routeId", tripDetails.getAssignRouteId());
						detailTripList.put("routeName",
								tripDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
						detailTripList.put("vehicleNumber",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						detailTripList.put("driverName",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						detailTripList.put("tripAssignDate", formatter.format(tripDetails.getTripAssignDate()));
						detailTripList.put("tripCompleteDate", dateFormatter.format(tripDetails.getTripAssignDate()));
						detailTripList.put("facilityName",
								tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
						detailTripList.put("tripType", tripDetails.getTripType());
						detailTripList.put("ShitTime", "test");
						List<Map<String, Object>> pickupDetails = new ArrayList<Map<String, Object>>();
						int noShowCount = 0;
						List<EFmFmEmployeeTripDetailPO> employeeDetails = iCabRequestBO
								.getParticularTripAllEmployees(tripDetails.getAssignRouteId());

						if (!(employeeDetails.isEmpty())) {
							for (EFmFmEmployeeTripDetailPO employeeList : employeeDetails) {
								Map<String, Object> empList = new HashMap<String, Object>();
								if (employeeList.getBoardedFlg().equals("NO")) {
									noShowCount++;
									empList.put("empId", employeeList.geteFmFmEmployeeTravelRequest()
											.getEfmFmUserMaster().getEmployeeId());
									empList.put("empName",
											new String(Base64.getDecoder()
													.decode(employeeList.geteFmFmEmployeeTravelRequest()
															.getEfmFmUserMaster().getFirstName()),
													"utf-8"));
									if (new String(
											Base64.getDecoder()
													.decode(employeeList.geteFmFmEmployeeTravelRequest()
															.getEfmFmUserMaster().getGender()),
											"utf-8").equalsIgnoreCase("Male")) {
										empList.put("empGender", 1);
									} else {
										empList.put("empGender", 2);
									}
									empList.put("empAddress",
											new String(Base64.getDecoder().decode(employeeList
													.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()),
											"utf-8"));
									empList.put("shiftTime", dateFormatter
											.format(employeeList.geteFmFmEmployeeTravelRequest().getShiftTime()));
									empList.put("scheduleTime", timeFormat.format(employeeList.getActualTime()));
									empList.put("travelStatus", "noShow");
									pickupDetails.add(empList);
								}
							}

						}
						detailTripList.put("noshowCounter", noShowCount);
						detailTripList.put("empDetails", pickupDetails);
						tripList.add(detailTripList);

					}
					detailTrip.put("tripDetail", tripList);
					detailTrip.put("tripAssignDate", date);

				}
				trip.add(detailTrip);

			}
			allTrips.put("data", trip);

		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allTrips, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * driverscorecard
	 */

	@POST
	@Path("/driverscorecard")
	public Response getDriverScoreCardReports(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, UnsupportedEncodingException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}

		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		// DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");

		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

		List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllTripByDate(fromDate, toDate,
				new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		List<Map<String, Object>> tripList = new ArrayList<Map<String, Object>>();
		if (!(allTripDetails.isEmpty())) {
			for (EFmFmAssignRoutePO tripDetails : allTripDetails) {
				Map<String, Object> detailTripList = new HashMap<String, Object>();
				detailTripList.put("routeId", tripDetails.getAssignRouteId());
				detailTripList.put("routeName",
						tripDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				detailTripList.put("vehicleNumber",
						tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				detailTripList.put("driverName",
						tripDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				detailTripList.put("tripAssignDate", dateFormatter.format(tripDetails.getTripAssignDate()));
				detailTripList.put("tripCompleteDate", dateFormatter.format(tripDetails.getTripCompleteTime()));
				detailTripList.put("tripType", tripDetails.getTripType());
				detailTripList.put("facilityName",
						tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				detailTripList.put("ShitTime", "test");
				List<Map<String, Object>> pickupDetails = new ArrayList<Map<String, Object>>();
				int noShowCount = 0;
				List<EFmFmEmployeeTripDetailPO> employeeDetails = iCabRequestBO
						.getParticularTripAllEmployees(tripDetails.getAssignRouteId());
				if (!(employeeDetails.isEmpty())) {
					for (EFmFmEmployeeTripDetailPO employeeList : employeeDetails) {
						Map<String, Object> empList = new HashMap<String, Object>();
						if (employeeList.getBoardedFlg().equals("NO")) {
							noShowCount++;
							empList.put("empId",
									employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
							empList.put("empName", new String(Base64.getDecoder().decode(
									employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
									"utf-8"));
							if (new String(Base64.getDecoder().decode(
									employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),
									"utf-8").equalsIgnoreCase("Male")) {
								empList.put("empGender", 1);
							} else {
								empList.put("empGender", 2);
							}
							empList.put("empAddress", new String(Base64.getDecoder().decode(
									employeeList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()),
									"utf-8"));

							empList.put("shiftTime",
									dateFormatter.format(employeeList.geteFmFmEmployeeTravelRequest().getShiftTime()));
							empList.put("scheduleTime", timeFormat.format(employeeList.getActualTime()));
							empList.put("travelStatus", "noShow");
							pickupDetails.add(empList);
						}
					}

				}
				detailTripList.put("noshowCounter", noShowCount);
				detailTripList.put("empDetails", pickupDetails);
				tripList.add(detailTripList);
			}
			allTrips.put("tripDetail", tripList);

		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allTrips, MediaType.APPLICATION_JSON).build();
	}

	// Shell new reports 18 Dec 2015

	/*
	 * On Time Arrival Reports
	 */

	@POST
	@Path("/ontimearrival")
	public Response getOnTimeArrivalReports(EFmFmAssignRoutePO assignRoutePO) throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
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
				log.info("authenticationToken error"+e);
			}

		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();		
		log.info("Time......" + assignRoutePO.getTime());
		log.info("vendorId......" + assignRoutePO.getVendorId());
		List<Date> selectedDates = new ArrayList<Date>();
		List<Time> selectedShiftTimes = new ArrayList<Time>();
		if (assignRoutePO.getTime().equalsIgnoreCase("0") && assignRoutePO.getVendorId() == 0) {
			selectedDates = iAssignRouteBO.getAllTripsDistinctDatesOTA(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),assignRoutePO.getTripType(), assignRoutePO.getRequestType());
			log.info("all zero" + selectedDates.size());
			List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
			log.info("Dates" + selectedDates.size());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			log.info("request Type" + assignRoutePO.getRequestType());
			// if (allTripDetails.size() > 0) {
			for (Date tripDetails : selectedDates) {			
				Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
				String date = formatter.format(tripDetails);
				Date tripDates = formatter.parse(date);
				onTimeReportDetail.put("tripDates", formatter.format(tripDetails));
				onTimeReportDetail.put("totalTrips", iAssignRouteBO.getAllTripsCountByDateOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));
				onTimeReportDetail.put("totalUsedVehicles", iAssignRouteBO.getAllTripsCountByDateOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));
				List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsByDateOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				
				onTimeReportDetail.put("totalAllocatedEmployeesCount",
						iAssignRouteBO.getAllEmployeesCountByDateOTA(tripDates, tripDates,
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),
										assignRoutePO.getCombinedFacility()),
								        assignRoutePO.getTripType(),
								        assignRoutePO.getRequestType()));

				onTimeReportDetail.put("totalEmployeesNoShowCount",
						iAssignRouteBO.getNoShowEmployeesCountByDateOTA(tripDates, tripDates,
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));	
				
				List<EFmFmEmployeeTripDetailPO>   totalEmployeesNoShowList=iAssignRouteBO.getNoShowEmpCountByDateOTAView(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				List<Map<String, Object>> noShowViewList = new ArrayList<Map<String, Object>>();				
				if(!totalEmployeesNoShowList.isEmpty()){
					for(EFmFmEmployeeTripDetailPO listvalue:totalEmployeesNoShowList){
						Map<String, Object> viewValues = new HashMap<String, Object>();
						viewValues.put("vehicleNumber", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						viewValues.put("driverName", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						viewValues.put("employeeId", listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						viewValues.put("employeeName",new String(Base64.getDecoder().decode(listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
						viewValues.put("routeName",listvalue.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());

						noShowViewList.add(viewValues);
					}
				}				
				onTimeReportDetail.put("totalEmployeesPickedDropCount",
						iAssignRouteBO.getPickedUpEmployeesCountByDateOTA(tripDates, tripDates,
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));

				int delayTripCount = 0, delayBeyondTimeCount = 0, onTimeCount = 0;
				DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
				DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				// DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy
				// hh:mm:ss a");
				List<Map<String, Object>> delayViewList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> onTimeViewList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> delayTripViewList = new ArrayList<Map<String, Object>>();
				
				for (EFmFmAssignRoutePO delayTrips : trips) {
					// 15 minutes
					List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO
							.getShiftTimeDetailFromShiftTimeAndTripType(
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), delayTrips.getShiftTime(),
									delayTrips.getTripType());
					long onTime = 0;
					if (!(shiftDetails.isEmpty())) {
						onTime = TimeUnit.SECONDS.toMillis(shiftDetails.get(0).getShiftBufferTime() * 60L);
					} else {
						onTime = TimeUnit.SECONDS.toMillis(10 * 60L);
					}
					if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
						String requestDate="";
						try{
						 requestDate = dateformate.format(delayTrips.getTripStartTime());
						}catch(Exception e){
						 requestDate = dateformate.format(delayTrips.getCreatedDate());
						}
						
						String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
						Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
						
						if ((shiftDateAndTime.getTime() + onTime) >= delayTrips.getTripStartTime().getTime()) {
							Map<String, Object> onTimeValues = new HashMap<String, Object>();
							onTimeValues.put("tripId", delayTrips.getAssignRouteId());
							onTimeValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							onTimeValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							onTimeValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
							try{
							onTimeValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
							onTimeValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
							onTimeValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
							}catch(Exception e){
								log.info("log error"+e);
							}
							onTimeViewList.add(onTimeValues);
							onTimeCount++;
						}
						else {
							delayBeyondTimeCount++;
							Map<String, Object> delayValues = new HashMap<String, Object>();							
							delayValues.put("tripId", delayTrips.getAssignRouteId());
							delayValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							delayValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							delayValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
							try{
							delayValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
							delayValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
							delayValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
							}catch(Exception e){
								log.info("log error"+e);
							}
							delayViewList.add(delayValues);
						}

					} else {
						String requestDate = dateformate.format(delayTrips.getTripCompleteTime());
						String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
						Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
						if ((shiftDateAndTime.getTime() - onTime) >= delayTrips.getTripCompleteTime().getTime()) {							
							onTimeCount++;
							Map<String, Object> onTimeValues = new HashMap<String, Object>();
							onTimeValues.put("tripId", delayTrips.getAssignRouteId());
							onTimeValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							onTimeValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							onTimeValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
							try{
							onTimeValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
							onTimeValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
							onTimeValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
							}catch(Exception e){
								log.info("log error"+e);
							}
							onTimeViewList.add(onTimeValues);
						} else if (shiftDateAndTime.getTime() >= delayTrips.getTripCompleteTime().getTime()) {
							delayTripCount++;
							Map<String, Object> delayTripValues = new HashMap<String, Object>();							
							delayTripValues.put("tripId", delayTrips.getAssignRouteId());
							delayTripValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							delayTripValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							delayTripValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
						try{
							delayTripValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
							delayTripValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
							delayTripValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
						}catch(Exception e){
							log.info("log error"+e);
						}
							delayTripViewList.add(delayTripValues);
						} else {							
							delayBeyondTimeCount++;
							Map<String, Object> delayValues = new HashMap<String, Object>();							
							delayValues.put("tripId", delayTrips.getAssignRouteId());
							delayValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							delayValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							delayValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
							try{
							delayValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
							delayValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
							delayValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
							}catch(Exception e){
								log.info("trip sheet"+e);
							}
							delayViewList.add(delayValues);
						}

					}

				}
				log.info("onTimeCount" + onTimeCount);
				onTimeReportDetail.put("delayTripsPercentage", (onTimeCount * 100) / trips.size());
				onTimeReportDetail.put("totalDelayTrips", delayTripCount);
				onTimeReportDetail.put("totalDelayTripsBeyondLogin", delayBeyondTimeCount);
				onTimeReportDetail.put("onTimeTrips", onTimeCount);
				onTimeReportDetail.put("vendorName", "All Vendors");
				onTimeReportDetail.put("noShowView",noShowViewList);	
				onTimeReportDetail.put("delayBeyondTime",delayViewList);
				onTimeReportDetail.put("delayTripView",delayTripViewList);
				onTimeReportDetail.put("onTimeview",onTimeViewList);
				onTimeReport.add(onTimeReportDetail);
			}
			allTrips.put("tripDetail", onTimeReport);
		}
		// shift wise and vendor wise
		else if (!(assignRoutePO.getTime().equalsIgnoreCase("0")) && !(assignRoutePO.getVendorId() == 0)) {
			if (assignRoutePO.getTime().equalsIgnoreCase("1")) {
				selectedShiftTimes = iAssignRouteBO.getAllTripsByAllShiftsForVendorOTA(fromDate, toDate,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
						assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
				log.info("Both not null" + selectedShiftTimes.size());
				List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
				log.info("Dates" + selectedShiftTimes.size());
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("From Date" + assignRoutePO.getToDate());
				// if (allTripDetails.size() > 0) {
				
				
				
				
				
				for (Time shiftTimeDetails : selectedShiftTimes) {
					Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
					onTimeReportDetail.put("tripDates", shiftFormate.format(shiftTimeDetails));

					long totalUsedVehiclesVendor = iAssignRouteBO.getUsedFleetByByVendorIdByShiftOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
							assignRoutePO.getVendorId(), shiftTimeDetails,assignRoutePO.getRequestType());
					onTimeReportDetail.put("totalTrips", totalUsedVehiclesVendor);
					onTimeReportDetail.put("totalUsedVehicles", totalUsedVehiclesVendor);
					List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsDetailsByShiftByVendorOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
							shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
					onTimeReportDetail.put("totalAllocatedEmployeesCount",
							iAssignRouteBO.getAllEmployeesCountByShiftByVendorIdOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));

					onTimeReportDetail.put("totalEmployeesNoShowCount",
							iAssignRouteBO.getNoShowEmployeesCountByShiftByVendorOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
					List<EFmFmEmployeeTripDetailPO>   totalEmployeesNoShowList=iAssignRouteBO.getNoShowEmployeesCountByShiftByVendorOTAView(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
					List<Map<String, Object>> noShowViewList = new ArrayList<Map<String, Object>>();				
					if(!totalEmployeesNoShowList.isEmpty()){
						for(EFmFmEmployeeTripDetailPO listvalue:totalEmployeesNoShowList){
							Map<String, Object> viewValues = new HashMap<String, Object>();
							viewValues.put("vehicleNumber", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							viewValues.put("driverName", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							viewValues.put("employeeId", listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
							viewValues.put("employeeName",new String(Base64.getDecoder().decode(listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
							viewValues.put("routeName",listvalue.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
							noShowViewList.add(viewValues);
						}
					}			
					onTimeReportDetail.put("totalEmployeesPickedDropCount",
							iAssignRouteBO.getPickedUpEmployeesCountByShiftByVendorOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
					onTimeReportDetail.put("vendorName", trips.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
							.getEfmFmVendorMaster().getVendorName());
					int delayTripCount = 0, delayBeyondTimeCount = 0, onTimeCount = 0;
					DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
					DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");

					// DateFormat dateTimeFormate = new
					// SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
					List<Map<String, Object>> delayViewList = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> onTimeViewList = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> delayTripViewList = new ArrayList<Map<String, Object>>();
					for (EFmFmAssignRoutePO delayTrips : trips) {
						List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO
								.getShiftTimeDetailFromShiftTimeAndTripType(
										new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), delayTrips.getShiftTime(),
										delayTrips.getTripType());
						long onTime = 0;
						if (!(shiftDetails.isEmpty())) {
							onTime = TimeUnit.SECONDS.toMillis(shiftDetails.get(0).getShiftBufferTime() * 60L);
						} else {
							onTime = TimeUnit.SECONDS.toMillis(10 * 60L);
						}
						if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
							String requestDate ="";
							try{
								 requestDate = dateformate.format(delayTrips.getTripStartTime());
								}catch(Exception e){
								 requestDate = dateformate.format(delayTrips.getCreatedDate());
								}
							
							
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() + onTime) >= delayTrips.getTripStartTime().getTime()) {
								onTimeCount++;
								Map<String, Object> onTimeValues = new HashMap<String, Object>();
								onTimeValues.put("tripId", delayTrips.getAssignRouteId());
								onTimeValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								onTimeValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								onTimeValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								onTimeValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								onTimeValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								onTimeValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log.error"+e);
								}
								onTimeViewList.add(onTimeValues);
							}

							else {
								delayBeyondTimeCount++;
								Map<String, Object> delayValues = new HashMap<String, Object>();							
								delayValues.put("tripId", delayTrips.getAssignRouteId());
								delayValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								delayValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								delayValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								delayValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								delayValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								delayValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								delayViewList.add(delayValues);
							}

						} else {
							String requestDate = dateformate.format(delayTrips.getTripCompleteTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() - onTime) >= delayTrips.getTripCompleteTime().getTime()) {
								onTimeCount++;
								Map<String, Object> onTimeValues = new HashMap<String, Object>();
								onTimeValues.put("tripId", delayTrips.getAssignRouteId());
								onTimeValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								onTimeValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								onTimeValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								onTimeValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								onTimeValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								onTimeValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								onTimeViewList.add(onTimeValues);
							} else if (shiftDateAndTime.getTime() >= delayTrips.getTripCompleteTime().getTime()) {
								delayTripCount++;
								Map<String, Object> delayTripValues = new HashMap<String, Object>();							
								delayTripValues.put("tripId", delayTrips.getAssignRouteId());
								delayTripValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								delayTripValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								delayTripValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								delayTripValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								delayTripValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								delayTripValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								delayTripViewList.add(delayTripValues);
							} else {
								delayBeyondTimeCount++;
								Map<String, Object> delayValues = new HashMap<String, Object>();							
								delayValues.put("tripId", delayTrips.getAssignRouteId());
								delayValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								delayValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								delayValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								delayValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								delayValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								delayValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								delayViewList.add(delayValues);
							}

						}
					}
					onTimeReportDetail.put("delayTripsPercentage", (onTimeCount * 100) / trips.size());
					onTimeReportDetail.put("totalDelayTrips", delayTripCount);
					onTimeReportDetail.put("totalDelayTripsBeyondLogin", delayBeyondTimeCount);
					onTimeReportDetail.put("onTimeTrips", onTimeCount);
					onTimeReportDetail.put("noShowView",noShowViewList);	
					onTimeReportDetail.put("delayBeyondTime",delayViewList);
					onTimeReportDetail.put("delayTripView",delayTripViewList);
					onTimeReportDetail.put("onTimeview",onTimeViewList);
					onTimeReport.add(onTimeReportDetail);
				}
				allTrips.put("tripDetail", onTimeReport);

			} else {
				
				//shif Time with particullar vendor
				String shiftDate = assignRoutePO.getTime();
				Date shift = shiftFormate.parse(shiftDate);
				java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
				selectedShiftTimes = iAssignRouteBO.getAllTripsByShiftByVendorIdOTA(fromDate, toDate,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
						assignRoutePO.getVendorId(), shiftTime,assignRoutePO.getRequestType());
				log.info("Both not null" + selectedShiftTimes.size());
				List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
				log.info("Dates" + selectedShiftTimes.size());
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("From Date" + assignRoutePO.getToDate());
				// if (allTripDetails.size() > 0) {
				for (Time shiftTimeDetails : selectedShiftTimes) {
					Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
					onTimeReportDetail.put("tripDates", shiftFormate.format(shiftTimeDetails));
					long totalUsedVehiclesVendor = iAssignRouteBO.getUsedFleetByByVendorIdByShiftOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
							assignRoutePO.getVendorId(), shiftTimeDetails,assignRoutePO.getRequestType());
					onTimeReportDetail.put("totalTrips", totalUsedVehiclesVendor);
					onTimeReportDetail.put("totalUsedVehicles", totalUsedVehiclesVendor);

					List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsDetailsByShiftByVendorOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
							shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
					onTimeReportDetail.put("totalAllocatedEmployeesCount",
							iAssignRouteBO.getAllEmployeesCountByShiftByVendorIdOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));

					onTimeReportDetail.put("totalEmployeesNoShowCount",
							iAssignRouteBO.getNoShowEmployeesCountByShiftByVendorOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
					
					List<EFmFmEmployeeTripDetailPO>   totalEmployeesNoShowList=iAssignRouteBO.getNoShowEmployeesCountByShiftByVendorOTAView(fromDate, toDate
							,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
					List<Map<String, Object>> noShowViewList = new ArrayList<Map<String, Object>>();				
					if(!totalEmployeesNoShowList.isEmpty()){
						for(EFmFmEmployeeTripDetailPO listvalue:totalEmployeesNoShowList){
							Map<String, Object> viewValues = new HashMap<String, Object>();
							viewValues.put("vehicleNumber", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							viewValues.put("driverName", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							viewValues.put("employeeId", listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
							viewValues.put("employeeName",new String(Base64.getDecoder().decode(listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
							viewValues.put("routeName",listvalue.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
							noShowViewList.add(viewValues);
						}
					}
					onTimeReportDetail.put("totalEmployeesPickedDropCount",
							iAssignRouteBO.getPickedUpEmployeesCountByShiftByVendorOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
					onTimeReportDetail.put("vendorName", trips.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
							.getEfmFmVendorMaster().getVendorName());
					int delayTripCount = 0, delayBeyondTimeCount = 0, onTimeCount = 0;
					DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
					DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					// DateFormat dateTimeFormate = new
					// SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
					List<Map<String, Object>> delayViewList = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> onTimeViewList = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> delayTripViewList = new ArrayList<Map<String, Object>>();
					for (EFmFmAssignRoutePO delayTrips : trips) {
						List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO
								.getShiftTimeDetailFromShiftTimeAndTripType(
										new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), delayTrips.getShiftTime(),
										delayTrips.getTripType());
						long onTime = 0;
						if (!(shiftDetails.isEmpty())) {
							onTime = TimeUnit.SECONDS.toMillis(shiftDetails.get(0).getShiftBufferTime() * 60L);
						} else {
							onTime = TimeUnit.SECONDS.toMillis(10 * 60L);
						}
						if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
							String requestDate ="";
							try{
								 requestDate = dateformate.format(delayTrips.getTripStartTime());
								}catch(Exception e){
								 requestDate = dateformate.format(delayTrips.getCreatedDate());
								}
							
							
							
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() + onTime) >= delayTrips.getTripStartTime().getTime()) {
								onTimeCount++;
								Map<String, Object> onTimeValues = new HashMap<String, Object>();
								onTimeValues.put("tripId", delayTrips.getAssignRouteId());
								onTimeValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								onTimeValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								onTimeValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								onTimeValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								onTimeValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								onTimeValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								onTimeViewList.add(onTimeValues);
							} else {
								delayBeyondTimeCount++;
								Map<String, Object> delayValues = new HashMap<String, Object>();							
								delayValues.put("tripId", delayTrips.getAssignRouteId());
								delayValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								delayValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								delayValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								delayValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								delayValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								delayValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								delayViewList.add(delayValues);
							}

						} else {
							String requestDate = dateformate.format(delayTrips.getTripCompleteTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() - onTime) >= delayTrips.getTripCompleteTime().getTime()) {
								onTimeCount++;
								Map<String, Object> onTimeValues = new HashMap<String, Object>();
								onTimeValues.put("tripId", delayTrips.getAssignRouteId());
								onTimeValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								onTimeValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								onTimeValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								onTimeValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								onTimeValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								onTimeValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								onTimeViewList.add(onTimeValues);
							} else if (shiftDateAndTime.getTime() >= delayTrips.getTripCompleteTime().getTime()) {
								delayTripCount++;
								Map<String, Object> delayTripValues = new HashMap<String, Object>();							
								delayTripValues.put("tripId", delayTrips.getAssignRouteId());
								delayTripValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								delayTripValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								delayTripValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								delayTripValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								delayTripValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								delayTripValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								delayTripViewList.add(delayTripValues);
							} else {
								delayBeyondTimeCount++;
								Map<String, Object> delayValues = new HashMap<String, Object>();							
								delayValues.put("tripId", delayTrips.getAssignRouteId());
								delayValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								delayValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								delayValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								delayValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								delayValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								delayValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								delayViewList.add(delayValues);
							}
						}
					}
					onTimeReportDetail.put("delayTripsPercentage", (onTimeCount * 100) / trips.size());
					onTimeReportDetail.put("totalDelayTrips", delayTripCount);
					onTimeReportDetail.put("totalDelayTripsBeyondLogin", delayBeyondTimeCount);					
					onTimeReportDetail.put("onTimeTrips", onTimeCount);
					
					onTimeReportDetail.put("noShowView",noShowViewList);	
					onTimeReportDetail.put("delayBeyondTime",delayViewList);
					onTimeReportDetail.put("delayTripView",delayTripViewList);
					onTimeReportDetail.put("onTimeview",onTimeViewList);
					onTimeReport.add(onTimeReportDetail);
				}
				allTrips.put("tripDetail", onTimeReport);
			}
		}
		// shift wise
		else if (!(assignRoutePO.getTime().equalsIgnoreCase("0")) && assignRoutePO.getVendorId() == 0) {
			if (assignRoutePO.getTime().equalsIgnoreCase("1")) {
				selectedShiftTimes = iAssignRouteBO.getAllTripsByAllShiftsOTA(fromDate, toDate,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				log.info("time not 0 and vendor 0" + selectedShiftTimes.size());
				List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
				log.info("Dates" + selectedShiftTimes.size());
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("From Date" + assignRoutePO.getToDate());
				// if (allTripDetails.size() > 0) {
				for (Time shiftTimeDetails : selectedShiftTimes) {
					Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
					onTimeReportDetail.put("tripDates", shiftFormate.format(shiftTimeDetails));
					onTimeReportDetail.put("totalTrips",
							iAssignRouteBO.getAllTripsCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));
					onTimeReportDetail.put("totalUsedVehicles",
							iAssignRouteBO.getAllTripsCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));

					List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsDetailsByShiftOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
							shiftTimeDetails,assignRoutePO.getRequestType());
					onTimeReportDetail.put("totalAllocatedEmployeesCount",
							iAssignRouteBO.getAllEmployeesCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));

					onTimeReportDetail.put("totalEmployeesNoShowCount",
							iAssignRouteBO.getNoShowEmployeesCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));
					
					List<EFmFmEmployeeTripDetailPO>   totalEmployeesNoShowList=iAssignRouteBO.getNoShowEmployeesCountByShiftOTAView(fromDate, toDate
							,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),shiftTimeDetails,assignRoutePO.getRequestType());
					List<Map<String, Object>> noShowViewList = new ArrayList<Map<String, Object>>();				
					if(!totalEmployeesNoShowList.isEmpty()){
						for(EFmFmEmployeeTripDetailPO listvalue:totalEmployeesNoShowList){
							Map<String, Object> viewValues = new HashMap<String, Object>();
							viewValues.put("vehicleNumber", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							viewValues.put("driverName", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							viewValues.put("employeeId", listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
							viewValues.put("employeeName",new String(Base64.getDecoder().decode(listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
							viewValues.put("routeName",listvalue.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
							noShowViewList.add(viewValues);
						}
					}
					
					onTimeReportDetail.put("totalEmployeesPickedDropCount",
							iAssignRouteBO.getPickedUpEmployeesCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));

					int delayTripCount = 0, delayBeyondTimeCount = 0, onTimeCount = 0;
					DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
					DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					// DateFormat dateTimeFormate = new
					// SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
					List<Map<String, Object>> delayViewList = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> onTimeViewList = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> delayTripViewList = new ArrayList<Map<String, Object>>();
					for (EFmFmAssignRoutePO delayTrips : trips) {
						List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO
								.getShiftTimeDetailFromShiftTimeAndTripType(
										new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), delayTrips.getShiftTime(),
										delayTrips.getTripType());
						long onTime = 0;
						if (!(shiftDetails.isEmpty())) {
							onTime = TimeUnit.SECONDS.toMillis(shiftDetails.get(0).getShiftBufferTime() * 60L);
						} else {
							onTime = TimeUnit.SECONDS.toMillis(10 * 60L);
						}
						if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
							String requestDate = dateformate.format(delayTrips.getTripStartTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() + onTime) > delayTrips.getTripStartTime().getTime()) {
								Map<String, Object> onTimeValues = new HashMap<String, Object>();
								onTimeValues.put("tripId", delayTrips.getAssignRouteId());
								onTimeValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								onTimeValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								onTimeValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
							try{
								onTimeValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								onTimeValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								onTimeValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
							}catch(Exception e){
								log.info("log error"+e);
							}
								onTimeViewList.add(onTimeValues);
								onTimeCount++;
							} else {
								delayBeyondTimeCount++;
								Map<String, Object> delayValues = new HashMap<String, Object>();							
								delayValues.put("tripId", delayTrips.getAssignRouteId());
								delayValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								delayValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								delayValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
							try{
								delayValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								delayValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								delayValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
							}catch(Exception e){
								log.info("log error"+e);
							}
								delayViewList.add(delayValues);
							}

						} else {
							String requestDate = dateformate.format(delayTrips.getTripCompleteTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() - onTime) >= delayTrips.getTripCompleteTime().getTime()) {
								onTimeCount++;

								Map<String, Object> onTimeValues = new HashMap<String, Object>();
											onTimeValues.put("tripId", delayTrips.getAssignRouteId());
											onTimeValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
											onTimeValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
											onTimeValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
											try{
											onTimeValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
											onTimeValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
											onTimeValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
											}catch(Exception e){
												log.info("log error"+e);
											}
											onTimeViewList.add(onTimeValues);
							} else if (shiftDateAndTime.getTime() >= delayTrips.getTripCompleteTime().getTime()) {
								delayTripCount++;
								Map<String, Object> delayTripValues = new HashMap<String, Object>();							
								delayTripValues.put("tripId", delayTrips.getAssignRouteId());
								delayTripValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								delayTripValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								delayTripValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								delayTripValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								delayTripValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								delayTripValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								delayTripViewList.add(delayTripValues);
							} else {
								delayBeyondTimeCount++;
								Map<String, Object> delayValues = new HashMap<String, Object>();							
								delayValues.put("tripId", delayTrips.getAssignRouteId());
								delayValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								delayValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								delayValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								delayValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								delayValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								delayValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								delayViewList.add(delayValues);
							}

						}

					}
					onTimeReportDetail.put("delayTripsPercentage", (onTimeCount * 100) / trips.size());
					onTimeReportDetail.put("totalDelayTrips", delayTripCount);
					onTimeReportDetail.put("totalDelayTripsBeyondLogin", delayBeyondTimeCount);
					onTimeReportDetail.put("onTimeTrips", onTimeCount);
					onTimeReportDetail.put("noShowView",noShowViewList);	
					onTimeReportDetail.put("delayBeyondTime",delayViewList);
					onTimeReportDetail.put("delayTripView",delayTripViewList);
					onTimeReportDetail.put("onTimeview",onTimeViewList);
					onTimeReportDetail.put("vendorName", "All Vendors");
					onTimeReport.add(onTimeReportDetail);
				}
				allTrips.put("tripDetail", onTimeReport);
			} else {
				
				//individual shift Time
				String shiftDate = assignRoutePO.getTime();
				Date shift = shiftFormate.parse(shiftDate);
				java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
				selectedShiftTimes = iAssignRouteBO.getAllTripsByShiftOTA(fromDate, toDate,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(), shiftTime,assignRoutePO.getRequestType());
				log.info("time not 0 and vendor 0" + selectedShiftTimes.size());
				List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
				log.info("Dates" + selectedShiftTimes.size());
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("From Date" + assignRoutePO.getToDate());
				// if (allTripDetails.size() > 0) {
				for (Time shiftTimeDetails : selectedShiftTimes) {
					Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
					onTimeReportDetail.put("tripDates", shiftFormate.format(shiftTimeDetails));
					onTimeReportDetail.put("totalTrips",
							iAssignRouteBO.getAllTripsCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));
					onTimeReportDetail.put("totalUsedVehicles",
							iAssignRouteBO.getAllTripsCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));

					List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsDetailsByShiftOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
							shiftTimeDetails,assignRoutePO.getRequestType());
					onTimeReportDetail.put("totalAllocatedEmployeesCount",
							iAssignRouteBO.getAllEmployeesCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));

					onTimeReportDetail.put("totalEmployeesNoShowCount",
							iAssignRouteBO.getNoShowEmployeesCountByShiftOTA(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),shiftTimeDetails,assignRoutePO.getRequestType()));
					
					
					
					List<EFmFmEmployeeTripDetailPO>   totalEmployeesNoShowListview=iAssignRouteBO.getNoShowEmployeesCountByShiftOTAView(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),shiftTimeDetails,assignRoutePO.getRequestType());
					List<Map<String, Object>> noShowViewList = new ArrayList<Map<String, Object>>();				
					if(!totalEmployeesNoShowListview.isEmpty()){
						for(EFmFmEmployeeTripDetailPO listvalue:totalEmployeesNoShowListview){
							Map<String, Object> viewValues = new HashMap<String, Object>();
							viewValues.put("vehicleNumber", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							viewValues.put("driverName", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							viewValues.put("employeeId", listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
							viewValues.put("employeeName",new String(Base64.getDecoder().decode(listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
							viewValues.put("routeName",listvalue.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
							noShowViewList.add(viewValues);
						}
					}			
				
					onTimeReportDetail.put("totalEmployeesPickedDropCount",
							iAssignRouteBO.getPickedUpEmployeesCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));

					int delayTripCount = 0, delayBeyondTimeCount = 0, onTimeCount = 0;
					DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
					DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");

					// DateFormat dateTimeFormate = new
					// SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
					List<Map<String, Object>> delayViewList = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> onTimeViewList = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> delayTripViewList = new ArrayList<Map<String, Object>>();
					for (EFmFmAssignRoutePO delayTrips : trips) {
						List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO
								.getShiftTimeDetailFromShiftTimeAndTripType(
										new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), delayTrips.getShiftTime(),
										delayTrips.getTripType());
						long onTime = 0;
						if (!(shiftDetails.isEmpty())) {
							onTime = TimeUnit.SECONDS.toMillis(shiftDetails.get(0).getShiftBufferTime() * 60L);
						} else {
							onTime = TimeUnit.SECONDS.toMillis(10 * 60L);
						}

						if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
							String requestDate = dateformate.format(delayTrips.getTripStartTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() + onTime) >= delayTrips.getTripStartTime().getTime()) {
								onTimeCount++;

								Map<String, Object> onTimeValues = new HashMap<String, Object>();
											onTimeValues.put("tripId", delayTrips.getAssignRouteId());
											onTimeValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
											onTimeValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
											onTimeValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
										try{
											onTimeValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
											onTimeValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
											onTimeValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
										}catch(Exception e){
											log.info("log error"+e);
										}
											onTimeViewList.add(onTimeValues);
							}

							else {
								delayBeyondTimeCount++;
								Map<String, Object> delayValues = new HashMap<String, Object>();							
								delayValues.put("tripId", delayTrips.getAssignRouteId());
								delayValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								delayValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								delayValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								delayValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								delayValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								delayValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								delayViewList.add(delayValues);
							}

						} else {
							String requestDate = dateformate.format(delayTrips.getTripCompleteTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() - onTime) >= delayTrips.getTripCompleteTime().getTime()) {
								onTimeCount++;
								Map<String, Object> onTimeValues = new HashMap<String, Object>();
								onTimeValues.put("tripId", delayTrips.getAssignRouteId());
								onTimeValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								onTimeValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								onTimeValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								onTimeValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								onTimeValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								onTimeValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								onTimeViewList.add(onTimeValues);
								
							} else if (shiftDateAndTime.getTime() >= delayTrips.getTripCompleteTime().getTime()) {
								delayTripCount++;
								Map<String, Object> delayTripValues = new HashMap<String, Object>();							
								delayTripValues.put("tripId", delayTrips.getAssignRouteId());
								delayTripValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								delayTripValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								delayTripValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								delayTripValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								delayTripValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								delayTripValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								delayTripViewList.add(delayTripValues);
							} else {
								delayBeyondTimeCount++;
								Map<String, Object> delayValues = new HashMap<String, Object>();							
								delayValues.put("tripId", delayTrips.getAssignRouteId());
								delayValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								delayValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								delayValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
								try{
								delayValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
								delayValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
								delayValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
								}catch(Exception e){
									log.info("log error"+e);
								}
								delayViewList.add(delayValues);
							}

						}

					}
					onTimeReportDetail.put("delayTripsPercentage", (onTimeCount * 100) / trips.size());
					onTimeReportDetail.put("totalDelayTrips", delayTripCount);
					onTimeReportDetail.put("totalDelayTripsBeyondLogin", delayBeyondTimeCount);
					onTimeReportDetail.put("onTimeTrips", onTimeCount);
					onTimeReportDetail.put("noShowView",noShowViewList);	
					onTimeReportDetail.put("delayBeyondTime",delayViewList);
					onTimeReportDetail.put("delayTripView",delayTripViewList);
					onTimeReportDetail.put("onTimeview",onTimeViewList);
					onTimeReportDetail.put("vendorName", "All Vendors");
					onTimeReport.add(onTimeReportDetail);
				}
				allTrips.put("tripDetail", onTimeReport);
			}

		} else if (assignRoutePO.getTime().equalsIgnoreCase("0") && !(assignRoutePO.getVendorId() == 0)) {
			selectedDates = iAssignRouteBO.getAllTripsByByVendorIdOTA(fromDate, toDate,
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
					assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
			log.info("time 0 and vendor not 0" + selectedDates.size());
			List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
			log.info("Dates" + selectedDates.size());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			// if (allTripDetails.size() > 0) {
			for (Date tripDetails : selectedDates) {
				Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
				String date = formatter.format(tripDetails);
				Date tripDates = formatter.parse(date);
				onTimeReportDetail.put("tripDates", formatter.format(tripDetails));
				long totalUsedVehiclesVendor = iAssignRouteBO.getUsedFleetByByVendorIdOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
						assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
				onTimeReportDetail.put("totalTrips", totalUsedVehiclesVendor);
				onTimeReportDetail.put("totalUsedVehicles", totalUsedVehiclesVendor);
				List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsDetailsByVendorWiseOnlyOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
						assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
				
				onTimeReportDetail.put("totalAllocatedEmployeesCount",
						iAssignRouteBO.getAllEmployeesCountByDateByVendorIdOTA(tripDates, tripDates,
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
								assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
				onTimeReportDetail.put("totalEmployeesNoShowCount",
						iAssignRouteBO.getNoShowEmployeesCountByDateByVendorOTA(tripDates, tripDates,
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
								assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
				List<EFmFmEmployeeTripDetailPO>   totalEmployeesNoShowList=iAssignRouteBO.getNoShowEmployeesCountByVendorWiseOTAView(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
				List<Map<String, Object>> noShowViewList = new ArrayList<Map<String, Object>>();				
				if(!totalEmployeesNoShowList.isEmpty()){
					for(EFmFmEmployeeTripDetailPO listvalue:totalEmployeesNoShowList){
						Map<String, Object> viewValues = new HashMap<String, Object>();
						viewValues.put("vehicleNumber", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						viewValues.put("driverName", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						viewValues.put("employeeId", listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						viewValues.put("employeeName",new String(Base64.getDecoder().decode(listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
						viewValues.put("routeName",listvalue.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
						noShowViewList.add(viewValues);
					}
				}				

				
				
				
				onTimeReportDetail.put("totalEmployeesPickedDropCount",
						iAssignRouteBO.getPickedUpEmployeesCountByDateByVendorOTA(tripDates, tripDates,
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
								assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
				onTimeReportDetail.put("vendorName", trips.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorName());
				int delayTripCount = 0, delayBeyondTimeCount = 0, onTimeCount = 0;
				DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
				DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");				
				List<Map<String, Object>> delayViewList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> onTimeViewList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> delayTripViewList = new ArrayList<Map<String, Object>>();
				for (EFmFmAssignRoutePO delayTrips : trips) {
					List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO
							.getShiftTimeDetailFromShiftTimeAndTripType(
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), delayTrips.getShiftTime(),
									delayTrips.getTripType());
					long onTime = 0;
					if (!(shiftDetails.isEmpty())) {
						onTime = TimeUnit.SECONDS.toMillis(shiftDetails.get(0).getShiftBufferTime() * 60L);
					} else {
						onTime = TimeUnit.SECONDS.toMillis(10 * 60L);
					}
					if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
						String requestDate = dateformate.format(delayTrips.getTripStartTime());
						String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
						Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
						if ((shiftDateAndTime.getTime() + onTime) >= delayTrips.getTripStartTime().getTime()) {
							onTimeCount++;
							Map<String, Object> onTimeValues = new HashMap<String, Object>();
							onTimeValues.put("tripId", delayTrips.getAssignRouteId());
							onTimeValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							onTimeValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							onTimeValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
							try{
							onTimeValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
							onTimeValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
							onTimeValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
							}catch(Exception e){
								log.info("log error"+e);
							}
							onTimeViewList.add(onTimeValues);
						}
						else {
							delayBeyondTimeCount++;
							Map<String, Object> delayValues = new HashMap<String, Object>();							
							delayValues.put("tripId", delayTrips.getAssignRouteId());
							delayValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							delayValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							delayValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
							try{
							delayValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
							delayValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
							delayValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
							}catch(Exception e){
								log.info("log error"+e);
							}
							delayViewList.add(delayValues);

						}
					} else {
						String requestDate = dateformate.format(delayTrips.getTripCompleteTime());
						String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
						Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
						if ((shiftDateAndTime.getTime() - onTime) >= delayTrips.getTripCompleteTime().getTime()) {
							onTimeCount++;
							Map<String, Object> onTimeValues = new HashMap<String, Object>();
							onTimeValues.put("tripId", delayTrips.getAssignRouteId());
							onTimeValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							onTimeValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							onTimeValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
							try{
							onTimeValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
							onTimeValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
							onTimeValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
							}catch(Exception e){
								log.info("log error"+e);
							}
							onTimeViewList.add(onTimeValues);
						} else if (shiftDateAndTime.getTime() >= delayTrips.getTripCompleteTime().getTime()) {
							delayTripCount++;
							Map<String, Object> delayTripValues = new HashMap<String, Object>();							
							delayTripValues.put("tripId", delayTrips.getAssignRouteId());
							delayTripValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							delayTripValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							delayTripValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
							try{
							delayTripValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
							delayTripValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
							delayTripValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
							}catch(Exception e){
								log.info("log error"+e);
							}
							delayTripViewList.add(delayTripValues);
						} else {
							delayBeyondTimeCount++;
							Map<String, Object> delayValues = new HashMap<String, Object>();							
							delayValues.put("tripId", delayTrips.getAssignRouteId());
							delayValues.put("vehicleNumber", delayTrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
							delayValues.put("driverName", delayTrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
							delayValues.put("routeName",delayTrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());							
							try{
							delayValues.put("tripCloseTime",dateTimeFormate.format(delayTrips.getAllocationMsgDeliveryDate()));
							delayValues.put("tripStartTime",dateTimeFormate.format(delayTrips.getTripStartTime()));
							delayValues.put("tripEndTime",dateTimeFormate.format(delayTrips.getTripCompleteTime()));
						}catch(Exception e){
							log.info("log error"+e);
						}
									delayViewList.add(delayValues);
						}
					}

				}
				onTimeReportDetail.put("delayTripsPercentage", (onTimeCount * 100) / trips.size());
				onTimeReportDetail.put("totalDelayTrips", delayTripCount);
				onTimeReportDetail.put("totalDelayTripsBeyondLogin", delayBeyondTimeCount);
				onTimeReportDetail.put("onTimeTrips", onTimeCount);
				onTimeReportDetail.put("noShowView",noShowViewList);	
				onTimeReportDetail.put("delayBeyondTime",delayViewList);
				onTimeReportDetail.put("delayTripView",delayTripViewList);
				onTimeReportDetail.put("onTimeview",onTimeViewList);
				onTimeReport.add(onTimeReportDetail);
			}
			allTrips.put("tripDetail", onTimeReport);
		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allTrips, MediaType.APPLICATION_JSON).build();
	}

	
	
	//Download
	@POST
	@Path("/ontimeArrivalDownload")
	public Response getOnTimeArrivalReportsDwn(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
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
				log.info("authenticationToken error"+e);
			}

		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		
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
		int rownum = 0, noOfRoute = 0;
		Row OutSiderow = sheet.createRow(rownum++); 
		for (int columnIndex = 0; columnIndex < 11; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
		}
							
				
				Cell zerothCol = OutSiderow.createCell(0);
                zerothCol.setCellValue("Date");
                zerothCol.setCellStyle(style);
				
				Cell firstCol = OutSiderow.createCell(1);
				firstCol.setCellValue("Vendor Name");
				firstCol.setCellStyle(style);
				
				Cell secondCol = OutSiderow.createCell(2);
                secondCol.setCellValue("Actual Users");
                secondCol.setCellStyle(style);
				
				Cell thirdCol = OutSiderow.createCell(3);
                thirdCol.setCellValue("Total Fleets of the Day");
                thirdCol.setCellStyle(style);
				if(assignRoutePO.getTripType().equalsIgnoreCase("DROP")){					
					Cell fourthCol = OutSiderow.createCell(4);
	                fourthCol.setCellValue("Drop Pax");
	                fourthCol.setCellStyle(style);
					
					Cell fifthCol = OutSiderow.createCell(5);
	                fifthCol.setCellValue("Drop Trips");
	                fifthCol.setCellStyle(style);
				}else{
					Cell fourthCol = OutSiderow.createCell(4);
	                fourthCol.setCellValue("Pickup Pax");
	                fourthCol.setCellStyle(style);
					
					Cell fifthCol = OutSiderow.createCell(5);
	                fifthCol.setCellValue("Pickup Trips");
	                fifthCol.setCellStyle(style);
				}
				
				Cell sixthCol = OutSiderow.createCell(6);
                sixthCol.setCellValue("OTA Trips (15 Minutes)");
                sixthCol.setCellStyle(style);
				
				
				Cell seventhCol = OutSiderow.createCell(7);
                seventhCol.setCellValue("OTA in %");
                seventhCol.setCellStyle(style);
				
				Cell eighthCol = OutSiderow.createCell(8);
                eighthCol.setCellValue("Delay Trips");
                eighthCol.setCellStyle(style);
				
				Cell ninethCol = OutSiderow.createCell(9);
                ninethCol.setCellValue("Beyond Login Time");
                ninethCol.setCellStyle(style);
				
				Cell tenthCol = OutSiderow.createCell(10);
                tenthCol.setCellValue("No Show");
                tenthCol.setCellStyle(style);
                
		Date fromDate = (Date) formatter.parse(assignRoutePO.getFromDate());
		Date toDate = (Date) formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		log.info("Time......" + assignRoutePO.getTime());
		log.info("vendorId......" + assignRoutePO.getVendorId());
		List<Date> selectedDates = new ArrayList<Date>();
		List<Time> selectedShiftTimes = new ArrayList<Time>();
		if (assignRoutePO.getTime().equalsIgnoreCase("0") && assignRoutePO.getVendorId() == 0) {
			selectedDates = iAssignRouteBO.getAllTripsDistinctDatesOTA(fromDate, toDate,
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
			log.info("all zero" + selectedDates.size());
			List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
			//OutSiderow = sheet.createRow(rownum++);
			log.info("Dates" + selectedDates.size());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			// if (allTripDetails.size() > 0) {
			for (Date tripDetails : selectedDates) {
				OutSiderow = sheet.createRow(rownum++);
				Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
				String date = formatter.format(tripDetails);
				Date tripDates = (Date) formatter.parse(date);	
				onTimeReportDetail.put("tripDates", formatter.format(tripDetails));
				Cell tripDatesCell = OutSiderow.createCell(0);
				tripDatesCell.setCellValue(formatter.format(tripDetails));
				
				onTimeReportDetail.put("totalTrips", iAssignRouteBO.getAllTripsCountByDateOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));
				
				Cell totalTripsCell = OutSiderow.createCell(5);
				totalTripsCell.setCellValue(iAssignRouteBO.getAllTripsCountByDateOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));
				
				onTimeReportDetail.put("totalUsedVehicles", iAssignRouteBO.getAllTripsCountByDateOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));
				
				Cell totalUsedVehiclesCell = OutSiderow.createCell(3);
				totalUsedVehiclesCell.setCellValue(iAssignRouteBO.getAllTripsCountByDateOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));
				
				List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsByDateOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				onTimeReportDetail.put("totalAllocatedEmployeesCount",
						iAssignRouteBO.getAllEmployeesCountByDateOTA(tripDates, tripDates,
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));
				
				
				Cell totalAllocatedEmployeesCountCell = OutSiderow.createCell(2);
				totalAllocatedEmployeesCountCell.setCellValue(iAssignRouteBO.getAllEmployeesCountByDateOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));

				onTimeReportDetail.put("totalEmployeesNoShowCount",
						iAssignRouteBO.getNoShowEmployeesCountByDateOTA(tripDates, tripDates,
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));
				
				Cell totalEmployeesNoShowCountCell = OutSiderow.createCell(10);
				totalEmployeesNoShowCountCell.setCellValue(iAssignRouteBO.getNoShowEmployeesCountByDateOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));
				
				onTimeReportDetail.put("totalEmployeesPickedDropCount",
						iAssignRouteBO.getPickedUpEmployeesCountByDateOTA(tripDates, tripDates,
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));
				Cell totalEmployeesPickedDropCountCell = OutSiderow.createCell(4);
				totalEmployeesPickedDropCountCell.setCellValue(iAssignRouteBO.getPickedUpEmployeesCountByDateOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));

				int delayTripCount = 0, delayBeyondTimeCount = 0, onTimeCount = 0;
				DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
				DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				// DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy
				// hh:mm:ss a");
				for (EFmFmAssignRoutePO delayTrips : trips) {
					// 15 minutes
					List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO
							.getShiftTimeDetailFromShiftTimeAndTripType(
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), delayTrips.getShiftTime(),
									delayTrips.getTripType());
					long onTime = 0;
					if (!(shiftDetails.isEmpty())) {
						onTime = TimeUnit.SECONDS.toMillis(shiftDetails.get(0).getShiftBufferTime() * 60L);
					} else {
						onTime = TimeUnit.SECONDS.toMillis(10 * 60L);
					}
					if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
						String requestDate = dateformate.format(delayTrips.getTripStartTime());
						String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
						Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
						if ((shiftDateAndTime.getTime() + onTime) >= delayTrips.getTripStartTime().getTime()) {
							onTimeCount++;
						}

						else {
							delayBeyondTimeCount++;
						}

					} else {
						
						String requestDate = dateformate.format(delayTrips.getTripCompleteTime());
						String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
						Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
						if ((shiftDateAndTime.getTime() - onTime) >= delayTrips.getTripCompleteTime().getTime()) {
							onTimeCount++;
						} else if (shiftDateAndTime.getTime() >= delayTrips.getTripCompleteTime().getTime()) {
							delayTripCount++;
						} else {
							delayBeyondTimeCount++;
						}

					}

				}
				log.info("onTimeCount" + onTimeCount);
				log.info("trips.size()" + trips.size() + (onTimeCount * 100) / trips.size());
				onTimeReportDetail.put("delayTripsPercentage", (onTimeCount * 100) / trips.size());
				
				Cell delayTripsPercentageCell = OutSiderow.createCell(7);
				delayTripsPercentageCell.setCellValue((onTimeCount * 100) / trips.size());
				
				onTimeReportDetail.put("totalDelayTrips", delayTripCount);
				
				Cell totalDelayTripsCell = OutSiderow.createCell(8);
				totalDelayTripsCell.setCellValue(delayTripCount);
				
				onTimeReportDetail.put("totalDelayTripsBeyondLogin", delayBeyondTimeCount);
				Cell totalDelayTripsBeyondLogin = OutSiderow.createCell(9);
				totalDelayTripsBeyondLogin.setCellValue(delayBeyondTimeCount);
				
				onTimeReportDetail.put("onTimeTrips", onTimeCount);
				Cell onTimeTrips = OutSiderow.createCell(6);
				onTimeTrips.setCellValue(onTimeCount);
				Cell vendorCell = OutSiderow.createCell(1);
				vendorCell.setCellValue("All Vendors");
				onTimeReportDetail.put("vendorName", "All Vendors");
				onTimeReport.add(onTimeReportDetail);
			}
			allTrips.put("tripDetail", onTimeReport);
		}
		// shift wise and vendor wise
		else if (!(assignRoutePO.getTime().equalsIgnoreCase("0")) && !(assignRoutePO.getVendorId() == 0)) {
			if (assignRoutePO.getTime().equalsIgnoreCase("1")) {
				selectedShiftTimes = iAssignRouteBO.getAllTripsByAllShiftsForVendorOTA(fromDate, toDate,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
						assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
				log.info("Both not null" + selectedShiftTimes.size());
				List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
				log.info("Dates" + selectedShiftTimes.size());
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("From Date" + assignRoutePO.getToDate());
				// if (allTripDetails.size() > 0) {
				for (Time shiftTimeDetails : selectedShiftTimes) {
					OutSiderow = sheet.createRow(rownum++);
					Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
					onTimeReportDetail.put("tripDates", shiftFormate.format(shiftTimeDetails));
					Cell tripDatesCell = OutSiderow.createCell(0);
					tripDatesCell.setCellValue(shiftFormate.format(shiftTimeDetails));

					long totalUsedVehiclesVendor = iAssignRouteBO.getUsedFleetByByVendorIdByShiftOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
							assignRoutePO.getVendorId(), shiftTimeDetails,assignRoutePO.getRequestType());
					onTimeReportDetail.put("totalTrips", totalUsedVehiclesVendor);
					Cell totalTripsCell = OutSiderow.createCell(5);
					totalTripsCell.setCellValue(totalUsedVehiclesVendor);
					
					onTimeReportDetail.put("totalUsedVehicles", totalUsedVehiclesVendor);
					Cell totalUsedVehiclesCell = OutSiderow.createCell(3);
					totalUsedVehiclesCell.setCellValue(totalUsedVehiclesVendor);
					
					List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsDetailsByShiftByVendorOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
							shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
					onTimeReportDetail.put("totalAllocatedEmployeesCount",
							iAssignRouteBO.getAllEmployeesCountByShiftByVendorIdOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
					
					Cell totalAllocatedEmployeesCountCell = OutSiderow.createCell(2);
					totalAllocatedEmployeesCountCell.setCellValue(
							iAssignRouteBO.getAllEmployeesCountByShiftByVendorIdOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));

					onTimeReportDetail.put("totalEmployeesNoShowCount",
							iAssignRouteBO.getNoShowEmployeesCountByShiftByVendorOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
					Cell totalEmployeesNoShowCountCell = OutSiderow.createCell(10);
					totalEmployeesNoShowCountCell.setCellValue(iAssignRouteBO.getNoShowEmployeesCountByShiftByVendorOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
					Cell totalEmployeesPickedDropCountCell = OutSiderow.createCell(4);
					totalEmployeesPickedDropCountCell.setCellValue(iAssignRouteBO.getPickedUpEmployeesCountByShiftByVendorOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
					
					onTimeReportDetail.put("vendorName", trips.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
							.getEfmFmVendorMaster().getVendorName());
					Cell vendorNameCell = OutSiderow.createCell(1);
					vendorNameCell.setCellValue(trips.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
							.getEfmFmVendorMaster().getVendorName());
					int delayTripCount = 0, delayBeyondTimeCount = 0, onTimeCount = 0;
					DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
					DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");

					// DateFormat dateTimeFormate = new
					// SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

					for (EFmFmAssignRoutePO delayTrips : trips) {
						List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO
								.getShiftTimeDetailFromShiftTimeAndTripType(
										new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), delayTrips.getShiftTime(),
										delayTrips.getTripType());
						long onTime = 0;
						if (!(shiftDetails.isEmpty())) {
							onTime = TimeUnit.SECONDS.toMillis(shiftDetails.get(0).getShiftBufferTime() * 60L);
						} else {
							onTime = TimeUnit.SECONDS.toMillis(10 * 60L);
						}
						if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
							String requestDate = dateformate.format(delayTrips.getTripStartTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() + onTime) >= delayTrips.getTripStartTime().getTime()) {
								onTimeCount++;
							}

							else {
								delayBeyondTimeCount++;
							}

						} else {
							String requestDate = dateformate.format(delayTrips.getTripCompleteTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() - onTime) >= delayTrips.getTripCompleteTime().getTime()) {
								onTimeCount++;
							} else if (shiftDateAndTime.getTime() >= delayTrips.getTripCompleteTime().getTime()) {
								delayTripCount++;
							} else {
								delayBeyondTimeCount++;
							}

						}
					}
					onTimeReportDetail.put("delayTripsPercentage", (onTimeCount * 100) / trips.size());
					Cell delayTripsPercentageCell = OutSiderow.createCell(7);
					delayTripsPercentageCell.setCellValue((onTimeCount * 100) /trips.size());	
					onTimeReportDetail.put("totalDelayTrips", delayTripCount);
					Cell totalDelayTripsCell = OutSiderow.createCell(8);
					totalDelayTripsCell.setCellValue(delayTripCount);
					onTimeReportDetail.put("totalDelayTripsBeyondLogin", delayBeyondTimeCount);
					Cell totalDelayTripsBeyondLogin = OutSiderow.createCell(9);
					totalDelayTripsBeyondLogin.setCellValue(delayBeyondTimeCount);
					onTimeReportDetail.put("onTimeTrips", onTimeCount);
					Cell onTimeTrips = OutSiderow.createCell(6);
					onTimeTrips.setCellValue(onTimeCount);
					onTimeReport.add(onTimeReportDetail);
				}
				allTrips.put("tripDetail", onTimeReport);

			} else {
				//SHIF TIME
				String shiftDate = assignRoutePO.getTime();
				Date shift = (Date) shiftFormate.parse(shiftDate);
				java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
				selectedShiftTimes = iAssignRouteBO.getAllTripsByShiftByVendorIdOTA(fromDate, toDate,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
						assignRoutePO.getVendorId(), shiftTime,assignRoutePO.getRequestType());
				log.info("Both not null" + selectedShiftTimes.size());
				List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
				log.info("Dates" + selectedShiftTimes.size());
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("From Date" + assignRoutePO.getToDate());
				// if (allTripDetails.size() > 0) {
				for (Time shiftTimeDetails : selectedShiftTimes) {
					Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
					OutSiderow = sheet.createRow(rownum++);
					onTimeReportDetail.put("tripDates", shiftFormate.format(shiftTimeDetails));
					Cell tripDatesCell = OutSiderow.createCell(0);
					tripDatesCell.setCellValue(shiftFormate.format(shiftTimeDetails));
					
					long totalUsedVehiclesVendor = iAssignRouteBO.getUsedFleetByByVendorIdByShiftOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
							assignRoutePO.getVendorId(), shiftTimeDetails,assignRoutePO.getRequestType());
					onTimeReportDetail.put("totalTrips", totalUsedVehiclesVendor);
					onTimeReportDetail.put("totalUsedVehicles", totalUsedVehiclesVendor);
					Cell totalTripsCell = OutSiderow.createCell(5);
					totalTripsCell.setCellValue(totalUsedVehiclesVendor);
					Cell totalUsedVehiclesCell = OutSiderow.createCell(3);
					totalUsedVehiclesCell.setCellValue(totalUsedVehiclesVendor);
					

					List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsDetailsByShiftByVendorOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
							shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
					/*onTimeReportDetail.put("totalAllocatedEmployeesCount",
							iAssignRouteBO.getAllEmployeesCountByShiftByVendorId(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId()));
					
					onTimeReportDetail.put("totalEmployeesNoShowCount",
							iAssignRouteBO.getNoShowEmployeesCountByShiftByVendor(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId()));
					onTimeReportDetail.put("totalEmployeesPickedDropCount",
							iAssignRouteBO.getPickedUpEmployeesCountByShiftByVendor(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId()));
					onTimeReportDetail.put("vendorName", trips.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
							.getEfmFmVendorMaster().getVendorName());
					*/
				
					
					Cell totalAllocatedEmployeesCountCell = OutSiderow.createCell(2);
					totalAllocatedEmployeesCountCell.setCellValue(iAssignRouteBO.getAllEmployeesCountByShiftByVendorIdOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
					Cell totalEmployeesNoShowCountCell = OutSiderow.createCell(10);
					totalEmployeesNoShowCountCell.setCellValue(iAssignRouteBO.getNoShowEmployeesCountByShiftByVendorOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
					Cell totalEmployeesPickedDropCountCell = OutSiderow.createCell(4);
					totalEmployeesPickedDropCountCell.setCellValue(iAssignRouteBO.getPickedUpEmployeesCountByShiftByVendorOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails, assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
					Cell vendorNameCell = OutSiderow.createCell(1);
					vendorNameCell.setCellValue(trips.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
							.getEfmFmVendorMaster().getVendorName());
					
					
					
					
					int delayTripCount = 0, delayBeyondTimeCount = 0, onTimeCount = 0;
					DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
					DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					// DateFormat dateTimeFormate = new
					// SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

					for (EFmFmAssignRoutePO delayTrips : trips) {
						List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO
								.getShiftTimeDetailFromShiftTimeAndTripType(
										new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), delayTrips.getShiftTime(),
										delayTrips.getTripType());
						long onTime = 0;
						if (!(shiftDetails.isEmpty())) {
							onTime = TimeUnit.SECONDS.toMillis(shiftDetails.get(0).getShiftBufferTime() * 60L);
						} else {
							onTime = TimeUnit.SECONDS.toMillis(10 * 60L);
						}
						if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
							String requestDate = dateformate.format(delayTrips.getTripStartTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() + onTime) >= delayTrips.getTripStartTime().getTime()) {
								onTimeCount++;
							} else {
								delayBeyondTimeCount++;
							}

						} else {
							String requestDate = dateformate.format(delayTrips.getTripCompleteTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() - onTime) >= delayTrips.getTripCompleteTime().getTime()) {
								onTimeCount++;
							} else if (shiftDateAndTime.getTime() >= delayTrips.getTripCompleteTime().getTime()) {
								delayTripCount++;
							} else {
								delayBeyondTimeCount++;
							}

						}
					}
					onTimeReportDetail.put("delayTripsPercentage", (onTimeCount * 100) / trips.size());
					Cell delayTripsPercentageCell = OutSiderow.createCell(7);
					delayTripsPercentageCell.setCellValue((onTimeCount * 100) / trips.size());
					onTimeReportDetail.put("totalDelayTrips", delayTripCount);
					Cell totalDelayTripsCell = OutSiderow.createCell(8);
					totalDelayTripsCell.setCellValue(delayTripCount);
					onTimeReportDetail.put("totalDelayTripsBeyondLogin", delayBeyondTimeCount);
					Cell totalDelayTripsBeyondLogin = OutSiderow.createCell(9);
					totalDelayTripsBeyondLogin.setCellValue(delayBeyondTimeCount);
					onTimeReportDetail.put("onTimeTrips", onTimeCount);
					Cell onTimeTrips = OutSiderow.createCell(6);
					onTimeTrips.setCellValue(onTimeCount);
					onTimeReport.add(onTimeReportDetail);
				}
				allTrips.put("tripDetail", onTimeReport);
			}
		}
		// shift wise
		else if (!(assignRoutePO.getTime().equalsIgnoreCase("0")) && assignRoutePO.getVendorId() == 0) {
			if (assignRoutePO.getTime().equalsIgnoreCase("1")) {
				selectedShiftTimes = iAssignRouteBO.getAllTripsByAllShiftsOTA(fromDate, toDate,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				log.info("time not 0 and vendor 0" + selectedShiftTimes.size());
				List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
				log.info("Dates" + selectedShiftTimes.size());
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("From Date" + assignRoutePO.getToDate());
				// if (allTripDetails.size() > 0) {
				for (Time shiftTimeDetails : selectedShiftTimes) {
					Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
					OutSiderow = sheet.createRow(rownum++);
					Cell tripDatesCell = OutSiderow.createCell(0);
					tripDatesCell.setCellValue(shiftFormate.format(shiftTimeDetails));
					/*onTimeReportDetail.put("totalTrips",*/
					Cell totalTripsCell = OutSiderow.createCell(5);
					totalTripsCell.setCellValue(iAssignRouteBO.getAllTripsCountByShiftOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));
					/*onTimeReportDetail.put("totalUsedVehicles",*/
					Cell totalUsedVehiclesCell = OutSiderow.createCell(3);
					totalUsedVehiclesCell.setCellValue(iAssignRouteBO.getAllTripsCountByShiftOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));

					List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsDetailsByShiftOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
							shiftTimeDetails,assignRoutePO.getRequestType());
				/*	onTimeReportDetail.put("totalAllocatedEmployeesCount",*/
					Cell totalAllocatedEmployeesCountCell = OutSiderow.createCell(2);
							totalAllocatedEmployeesCountCell.setCellValue(iAssignRouteBO.getAllEmployeesCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));

					/*onTimeReportDetail.put("totalEmployeesNoShowCount",*/
							Cell totalEmployeesNoShowCountCell = OutSiderow.createCell(10);
							totalEmployeesNoShowCountCell.setCellValue(iAssignRouteBO.getNoShowEmployeesCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));
					/*onTimeReportDetail.put("totalEmployeesPickedDropCount",*/
							Cell totalEmployeesPickedDropCountCell = OutSiderow.createCell(4);
							totalEmployeesPickedDropCountCell.setCellValue(iAssignRouteBO.getPickedUpEmployeesCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));

					int delayTripCount = 0, delayBeyondTimeCount = 0, onTimeCount = 0;
					DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
					DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					// DateFormat dateTimeFormate = new
					// SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

					for (EFmFmAssignRoutePO delayTrips : trips) {
						List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO
								.getShiftTimeDetailFromShiftTimeAndTripType(
										new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), delayTrips.getShiftTime(),
										delayTrips.getTripType());
						long onTime = 0;
						if (!(shiftDetails.isEmpty())) {
							onTime = TimeUnit.SECONDS.toMillis(shiftDetails.get(0).getShiftBufferTime() * 60L);
						} else {
							onTime = TimeUnit.SECONDS.toMillis(10 * 60L);
						}
						if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
							String requestDate = dateformate.format(delayTrips.getTripStartTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() + onTime) > delayTrips.getTripStartTime().getTime()) {
								onTimeCount++;
							} else {
								delayBeyondTimeCount++;
							}

						} else {
							String requestDate = dateformate.format(delayTrips.getTripCompleteTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() - onTime) >= delayTrips.getTripCompleteTime().getTime()) {
								onTimeCount++;
							} else if (shiftDateAndTime.getTime() >= delayTrips.getTripCompleteTime().getTime()) {
								delayTripCount++;
							} else {
								delayBeyondTimeCount++;
							}

						}

					}
					onTimeReportDetail.put("delayTripsPercentage", (onTimeCount * 100) / trips.size());
					Cell delayTripsPercentageCell = OutSiderow.createCell(7);
					delayTripsPercentageCell.setCellValue((onTimeCount * 100) / trips.size());	
					onTimeReportDetail.put("totalDelayTrips", delayTripCount);
					Cell totalDelayTripsCell = OutSiderow.createCell(8);
					totalDelayTripsCell.setCellValue(delayTripCount);
					onTimeReportDetail.put("totalDelayTripsBeyondLogin", delayBeyondTimeCount);
					Cell totalDelayTripsBeyondLogin = OutSiderow.createCell(9);
					totalDelayTripsBeyondLogin.setCellValue(delayBeyondTimeCount);
					onTimeReportDetail.put("onTimeTrips", onTimeCount);
					Cell onTimeTrips = OutSiderow.createCell(6);
					onTimeTrips.setCellValue(onTimeCount);
					Cell vendorCell = OutSiderow.createCell(1);
					vendorCell.setCellValue("All Vendors");
					onTimeReportDetail.put("vendorName", "All Vendors");
					onTimeReport.add(onTimeReportDetail);
				}
				allTrips.put("tripDetail", onTimeReport);
			} else {
				String shiftDate = assignRoutePO.getTime();
				Date shift = (Date) shiftFormate.parse(shiftDate);
				java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
				selectedShiftTimes = iAssignRouteBO.getAllTripsByShiftOTA(fromDate, toDate,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(), shiftTime,assignRoutePO.getRequestType());
				log.info("time not 0 and vendor 0" + selectedShiftTimes.size());
				List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
				log.info("Dates" + selectedShiftTimes.size());
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("From Date" + assignRoutePO.getToDate());
				// if (allTripDetails.size() > 0) {
				for (Time shiftTimeDetails : selectedShiftTimes) {
					OutSiderow = sheet.createRow(rownum++);
					Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
					Cell tripDatesCell = OutSiderow.createCell(0);
					tripDatesCell.setCellValue(shiftFormate.format(shiftTimeDetails));
					/*onTimeReportDetail.put("totalTrips",*/
					Cell totalTripsCell = OutSiderow.createCell(5);
							totalTripsCell.setCellValue(iAssignRouteBO.getAllTripsCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));
					/*onTimeReportDetail.put("totalUsedVehicles",*/
							Cell totalUsedVehiclesCell = OutSiderow.createCell(3);
							totalUsedVehiclesCell.setCellValue(iAssignRouteBO.getAllTripsCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));

					List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsDetailsByShiftOTA(fromDate, toDate,
							new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
							shiftTimeDetails,assignRoutePO.getRequestType());
					/*onTimeReportDetail.put("totalAllocatedEmployeesCount",*/
					Cell totalAllocatedEmployeesCountCell = OutSiderow.createCell(2);
							totalAllocatedEmployeesCountCell.setCellValue(iAssignRouteBO.getAllEmployeesCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));

					/*onTimeReportDetail.put("totalEmployeesNoShowCount",*/
							Cell totalEmployeesNoShowCountCell = OutSiderow.createCell(10);
							totalEmployeesNoShowCountCell.setCellValue(iAssignRouteBO.getNoShowEmployeesCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));
					/*onTimeReportDetail.put("totalEmployeesPickedDropCount",*/
							Cell totalEmployeesPickedDropCountCell = OutSiderow.createCell(4);
							totalEmployeesPickedDropCountCell.setCellValue(iAssignRouteBO.getPickedUpEmployeesCountByShiftOTA(fromDate, toDate,
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
									shiftTimeDetails,assignRoutePO.getRequestType()));

					int delayTripCount = 0, delayBeyondTimeCount = 0, onTimeCount = 0;
					DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
					DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");

					// DateFormat dateTimeFormate = new
					// SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

					for (EFmFmAssignRoutePO delayTrips : trips) {
						List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO
								.getShiftTimeDetailFromShiftTimeAndTripType(
										new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), delayTrips.getShiftTime(),
										delayTrips.getTripType());
						long onTime = 0;
						if (!(shiftDetails.isEmpty())) {
							onTime = TimeUnit.SECONDS.toMillis(shiftDetails.get(0).getShiftBufferTime() * 60L);
						} else {
							onTime = TimeUnit.SECONDS.toMillis(10 * 60L);
						}

						if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
							String requestDate = dateformate.format(delayTrips.getTripStartTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() + onTime) >= delayTrips.getTripStartTime().getTime()) {
								onTimeCount++;
							}

							else {
								delayBeyondTimeCount++;
							}

						} else {
							String requestDate = dateformate.format(delayTrips.getTripCompleteTime());
							String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
							Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
							if ((shiftDateAndTime.getTime() - onTime) >= delayTrips.getTripCompleteTime().getTime()) {
								onTimeCount++;
							} else if (shiftDateAndTime.getTime() >= delayTrips.getTripCompleteTime().getTime()) {
								delayTripCount++;
							} else {
								delayBeyondTimeCount++;
							}

						}

					}
					onTimeReportDetail.put("delayTripsPercentage", (onTimeCount * 100) / trips.size());
					Cell delayTripsPercentageCell = OutSiderow.createCell(7);
					delayTripsPercentageCell.setCellValue((onTimeCount * 100) / trips.size());	
					onTimeReportDetail.put("totalDelayTrips", delayTripCount);
					Cell totalDelayTripsCell = OutSiderow.createCell(8);
					totalDelayTripsCell.setCellValue(delayTripCount);
					onTimeReportDetail.put("totalDelayTripsBeyondLogin", delayBeyondTimeCount);
					Cell totalDelayTripsBeyondLogin = OutSiderow.createCell(9);
					totalDelayTripsBeyondLogin.setCellValue(delayBeyondTimeCount);
					onTimeReportDetail.put("onTimeTrips", onTimeCount);
					Cell onTimeTrips = OutSiderow.createCell(6);
					onTimeTrips.setCellValue(onTimeCount);
					Cell vendorCell = OutSiderow.createCell(1);
					vendorCell.setCellValue("All Vendors");
					onTimeReportDetail.put("vendorName", "All Vendors");
					onTimeReport.add(onTimeReportDetail);
				}
				allTrips.put("tripDetail", onTimeReport);
			}

		} else if (assignRoutePO.getTime().equalsIgnoreCase("0") && !(assignRoutePO.getVendorId() == 0)) {
			selectedDates = iAssignRouteBO.getAllTripsByByVendorIdOTA(fromDate, toDate,
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
					assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
			log.info("time 0 and vendor not 0" + selectedDates.size());
			List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
			log.info("Dates" + selectedDates.size());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			// if (allTripDetails.size() > 0) {
			for (Date tripDetails : selectedDates) {
				Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
				OutSiderow = sheet.createRow(rownum++);
				String date = formatter.format(tripDetails);
				Date tripDates = (Date) formatter.parse(date);
				Cell tripDatesCell = OutSiderow.createCell(0);
				tripDatesCell.setCellValue(formatter.format(tripDetails));
				long totalUsedVehiclesVendor = iAssignRouteBO.getUsedFleetByByVendorIdOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
						assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
				Cell totalTripsCell = OutSiderow.createCell(5);
				totalTripsCell.setCellValue(totalUsedVehiclesVendor);
				Cell totalUsedVehiclesCell = OutSiderow.createCell(3);
				totalUsedVehiclesCell.setCellValue(totalUsedVehiclesVendor);
				List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsDetailsByVendorWiseOnlyOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
						assignRoutePO.getVendorId(),assignRoutePO.getRequestType());
				/*onTimeReportDetail.put("totalAllocatedEmployeesCount",*/
				Cell totalAllocatedEmployeesCountCell = OutSiderow.createCell(2);
				totalAllocatedEmployeesCountCell.setCellValue(iAssignRouteBO.getAllEmployeesCountByDateByVendorIdOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
								assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));

				/*onTimeReportDetail.put("totalEmployeesNoShowCount",*/
				Cell totalEmployeesNoShowCountCell = OutSiderow.createCell(10);
						totalEmployeesNoShowCountCell.setCellValue(iAssignRouteBO.getNoShowEmployeesCountByDateByVendorOTA(tripDates, tripDates,
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
								assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
				/*onTimeReportDetail.put("totalEmployeesPickedDropCount",*/
						Cell totalEmployeesPickedDropCountCell = OutSiderow.createCell(4);
						totalEmployeesPickedDropCountCell.setCellValue(iAssignRouteBO.getPickedUpEmployeesCountByDateByVendorOTA(tripDates, tripDates,
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
								assignRoutePO.getVendorId(),assignRoutePO.getRequestType()));
						Cell vendorNameCell = OutSiderow.createCell(1);
						vendorNameCell.setCellValue( trips.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorName());
				
				int delayTripCount = 0, delayBeyondTimeCount = 0, onTimeCount = 01;
				DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
				DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				// DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy
				// hh:mm:ss a");

				for (EFmFmAssignRoutePO delayTrips : trips) {
					List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO
							.getShiftTimeDetailFromShiftTimeAndTripType(
									new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), delayTrips.getShiftTime(),
									delayTrips.getTripType());
					long onTime = 0;
					if (!(shiftDetails.isEmpty())) {
						onTime = TimeUnit.SECONDS.toMillis(shiftDetails.get(0).getShiftBufferTime() * 60L);
					} else {
						onTime = TimeUnit.SECONDS.toMillis(10 * 60L);
					}

					if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
						String requestDate = dateformate.format(delayTrips.getTripStartTime());
						String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
						Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
						if ((shiftDateAndTime.getTime() + onTime) >= delayTrips.getTripStartTime().getTime()) {
							onTimeCount++;
						}

						else {
							delayBeyondTimeCount++;
						}

					} else {
						String requestDate = dateformate.format(delayTrips.getTripCompleteTime());
						String requestDateShiftTime = requestDate + " " + delayTrips.getShiftTime();
						Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
						if ((shiftDateAndTime.getTime() - onTime) >= delayTrips.getTripCompleteTime().getTime()) {
							onTimeCount++;
						} else if (shiftDateAndTime.getTime() >= delayTrips.getTripCompleteTime().getTime()) {
							delayTripCount++;
						} else {
							delayBeyondTimeCount++;
						}
					}

				}
				onTimeReportDetail.put("delayTripsPercentage", (onTimeCount * 100) / trips.size());
				Cell delayTripsPercentageCell = OutSiderow.createCell(7);
				delayTripsPercentageCell.setCellValue((onTimeCount * 100) / trips.size());	
				onTimeReportDetail.put("totalDelayTrips", delayTripCount);
				Cell totalDelayTripsCell = OutSiderow.createCell(8);
				totalDelayTripsCell.setCellValue(delayTripCount);
				onTimeReportDetail.put("totalDelayTripsBeyondLogin", delayBeyondTimeCount);
				Cell totalDelayTripsBeyondLogin = OutSiderow.createCell(9);
				totalDelayTripsBeyondLogin.setCellValue(delayBeyondTimeCount);	
				onTimeReportDetail.put("onTimeTrips", onTimeCount);
				Cell onTimeTrips = OutSiderow.createCell(6);
				onTimeTrips.setCellValue(onTimeCount);
				onTimeReport.add(onTimeReportDetail);
			}
			allTrips.put("tripDetail", onTimeReport);
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
	 * Seat Utilization Reports
	 */

	@POST
	@Path("/seatutilization")
	public Response getOnTimeArrivalReportsShiftWice(EFmFmAssignRoutePO assignRoutePO) throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}
		
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
		List<Time> selectedShiftTimes = new ArrayList<Time>();
		log.info("time" + assignRoutePO.getTime());
		if (assignRoutePO.getTime().equalsIgnoreCase("0")) {
			List<Date> selectedDates = iAssignRouteBO.getAllTripsDistinctDatesOTA(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
			log.info("Dates" + selectedDates.size());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			for (Date tripDetails : selectedDates) {
				Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
				log.info("type" + assignRoutePO.getTripType());
				onTimeReportDetail.put("tripDates", formatter.format(tripDetails));
				onTimeReportDetail.put("totalUsedVehicles",
						iAssignRouteBO.getAllTripsCountByDateOTA(tripDetails, tripDetails,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));
				long totalPickupPax = iAssignRouteBO.getPickedUpOrDroppedEmployeesCountByDateSeatUtil(tripDetails, tripDetails,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				log.info("pickupCount" + totalPickupPax);				
				List<EFmFmAssignRoutePO>   totalUsedVehiclesView=iAssignRouteBO.getAllTripsCountByDateNoShowView(tripDetails, tripDetails,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				List<Map<String, Object>> totalUsedVehiclesList = new ArrayList<Map<String, Object>>();				
				if(!totalUsedVehiclesView.isEmpty()){
					for(EFmFmAssignRoutePO listvalue:totalUsedVehiclesView){
						Map<String, Object> viewValues = new HashMap<String, Object>();
						viewValues.put("vehicleNumber", listvalue.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						viewValues.put("vehicleModel", listvalue.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
						viewValues.put("driverName", listvalue.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());						
						totalUsedVehiclesList.add(viewValues);
					}
				}				
				onTimeReportDetail.put("VehiclesView", totalUsedVehiclesList);				
				List<EFmFmEmployeeTripDetailPO>   totalEmployeesPicked=iAssignRouteBO.getPickedUpOrDroppedEmployeesCountByDateSeatview(tripDetails, tripDetails,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				List<Map<String, Object>> noShowViewList = new ArrayList<Map<String, Object>>();				
				if(!totalEmployeesPicked.isEmpty()){
					for(EFmFmEmployeeTripDetailPO listvalue:totalEmployeesPicked){
						Map<String, Object> viewValues = new HashMap<String, Object>();
						viewValues.put("vehicleNumber", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						viewValues.put("employeeId", listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						viewValues.put("employeeName",new String(Base64.getDecoder().decode(listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
						noShowViewList.add(viewValues);
					}
				}
				onTimeReportDetail.put("PickupPaxView", noShowViewList);
				onTimeReportDetail.put("totalEmployeesPickedDropCount", totalPickupPax);
				List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsByDateOTA(tripDetails, tripDetails,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());			
				int count = 0;
				for (EFmFmAssignRoutePO tripDetail : trips) {
					count += (tripDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()-1);
				}
				onTimeReportDetail.put("totalVehicleCapacity", count);
				onTimeReportDetail.put("utilizedSeatPercentage", (totalPickupPax * 100) / count);
				onTimeReport.add(onTimeReportDetail);
			}
		} else if (assignRoutePO.getTime().equalsIgnoreCase("1")) {
			selectedShiftTimes = iAssignRouteBO.getAllTripsByAllShiftsOTA(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
			log.info("time not 0 and vendor 0" + selectedShiftTimes.size());
			log.info("Dates" + selectedShiftTimes.size());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			for (Time shiftTimeDetails : selectedShiftTimes) {
				Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
				onTimeReportDetail.put("tripDates", shiftFormate.format(shiftTimeDetails));
				onTimeReportDetail.put("totalUsedVehicles",iAssignRouteBO.getAllTripsCountByShiftOTA(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),shiftTimeDetails,assignRoutePO.getRequestType()));				
				List<EFmFmAssignRoutePO>   totalUsedVehiclesView=iAssignRouteBO.getAllTripsCountByShiftSeatView(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),shiftTimeDetails,assignRoutePO.getRequestType());
				List<Map<String, Object>> totalUsedVehiclesList = new ArrayList<Map<String, Object>>();				
				if(!totalUsedVehiclesView.isEmpty()){
					for(EFmFmAssignRoutePO listvalue:totalUsedVehiclesView){
						Map<String, Object> viewValues = new HashMap<String, Object>();
						viewValues.put("vehicleNumber", listvalue.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						viewValues.put("vehicleModel", listvalue.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
						viewValues.put("driverName", listvalue.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());						
						totalUsedVehiclesList.add(viewValues);
					}
				}				
				onTimeReportDetail.put("VehiclesView", totalUsedVehiclesList);	
				List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsDetailsByShiftOTA(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),shiftTimeDetails,assignRoutePO.getRequestType());
				long totalPickupPax = iAssignRouteBO.getPickedUpEmployeesCountByShiftOTA(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),shiftTimeDetails,assignRoutePO.getRequestType());

				List<EFmFmEmployeeTripDetailPO>   totalPickupPaxView=iAssignRouteBO.getPickedUpEmployeesCountByShiftSeatUtilView(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),shiftTimeDetails,assignRoutePO.getRequestType());
				List<Map<String, Object>> totalPickupPaxList = new ArrayList<Map<String, Object>>();				
				if(!totalPickupPaxView.isEmpty()){
					for(EFmFmEmployeeTripDetailPO listvalue:totalPickupPaxView){
						Map<String, Object> viewValues = new HashMap<String, Object>();						
						viewValues.put("vehicleNumber", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						viewValues.put("employeeId", listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						viewValues.put("employeeName",new String(Base64.getDecoder().decode(listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
						totalPickupPaxList.add(viewValues);
					}
				}				
				onTimeReportDetail.put("PickupPaxView", totalPickupPaxList);				
				onTimeReportDetail.put("totalEmployeesPickedDropCount", totalPickupPax);
				int count = 0;
				for (EFmFmAssignRoutePO tripDetail : trips) {
					count += (tripDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()-1);
				}
				onTimeReportDetail.put("totalVehicleCapacity", count);
				onTimeReportDetail.put("utilizedSeatPercentage", (totalPickupPax * 100) / count);
				onTimeReport.add(onTimeReportDetail);
			}
		} else {
			String shiftDate = assignRoutePO.getTime();
			Date shift = shiftFormate.parse(shiftDate);
			java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
			selectedShiftTimes = iAssignRouteBO.getAllTripsByShiftOTA(fromDate, toDate,
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(), shiftTime,assignRoutePO.getRequestType());
			log.info("time not 0 and vendor 0" + selectedShiftTimes.size());
			log.info("Dates" + selectedShiftTimes.size());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			for (Time shiftTimeDetails : selectedShiftTimes) {
				Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
				onTimeReportDetail.put("tripDates", shiftFormate.format(shiftTimeDetails));
				onTimeReportDetail.put("totalUsedVehicles",
						iAssignRouteBO.getAllTripsCountByShiftOTA(fromDate, toDate,
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
								shiftTimeDetails,assignRoutePO.getRequestType()));
				List<EFmFmAssignRoutePO>   totalUsedVehiclesView=iAssignRouteBO.getAllTripsCountByShiftSeatView
						(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
								shiftTimeDetails,assignRoutePO.getRequestType());
				List<Map<String, Object>> totalUsedVehiclesList = new ArrayList<Map<String, Object>>();				
				if(!totalUsedVehiclesView.isEmpty()){
					for(EFmFmAssignRoutePO listvalue:totalUsedVehiclesView){
						Map<String, Object> viewValues = new HashMap<String, Object>();
						viewValues.put("vehicleNumber", listvalue.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						viewValues.put("vehicleModel", listvalue.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
						viewValues.put("driverName", listvalue.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());						
						totalUsedVehiclesList.add(viewValues);
					}
				}	
				onTimeReportDetail.put("VehiclesView", totalUsedVehiclesList);
				List<EFmFmAssignRoutePO> trips = iAssignRouteBO.getAllTripsDetailsByShift(fromDate, toDate,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
						shiftTimeDetails);
				long totalPickupPax = iAssignRouteBO.getPickedUpEmployeesCountByShiftOTA(fromDate, toDate,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
						shiftTimeDetails,assignRoutePO.getRequestType());
				List<EFmFmEmployeeTripDetailPO>   totalPickupPaxView=iAssignRouteBO.getPickedUpEmployeesCountByShiftSeatUtilView(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),shiftTimeDetails,assignRoutePO.getRequestType());
				List<Map<String, Object>> totalPickupPaxList = new ArrayList<Map<String, Object>>();				
				if(!totalPickupPaxView.isEmpty()){
					for(EFmFmEmployeeTripDetailPO listvalue:totalPickupPaxView){
						Map<String, Object> viewValues = new HashMap<String, Object>();						
						viewValues.put("vehicleNumber", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						viewValues.put("employeeId", listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						viewValues.put("employeeName",new String(Base64.getDecoder().decode(listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
						totalPickupPaxList.add(viewValues);
					}
				}				
				onTimeReportDetail.put("PickupPaxView", totalPickupPaxList);
				onTimeReportDetail.put("totalEmployeesPickedDropCount", totalPickupPax);
				int count = 0;
				for (EFmFmAssignRoutePO tripDetail : trips) {
					count += (tripDetail.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()-1);
				}
				onTimeReportDetail.put("totalVehicleCapacity", count);
				onTimeReportDetail.put("utilizedSeatPercentage", (totalPickupPax * 100) / count);
				onTimeReport.add(onTimeReportDetail);
			}
		}
		allTrips.put("tripDetail", onTimeReport);
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allTrips, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * No Show Report as given by shell
	 */

	@POST
	@Path("/noshowreport")
	public Response getNoShowReportsByDate(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}
	
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		List<Map<String, Object>> onTimeReport = new ArrayList<Map<String, Object>>();
		List<Time> selectedShiftTimes = new ArrayList<Time>();
		log.info("time" + assignRoutePO.getTime());
		if (assignRoutePO.getTime().equalsIgnoreCase("0") && assignRoutePO.getEmployeeId().equalsIgnoreCase("0")) {
			List<Date> selectedDates = iAssignRouteBO.getAllTripsDistinctDates(fromDate, toDate,
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType());
			log.info("Dates" + selectedDates.size());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			for (Date tripDates : selectedDates) {
				Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
				onTimeReportDetail.put("tripDates", formatter.format(tripDates));
				onTimeReportDetail.put("actualTravelledDate", formatter.format(tripDates));

				onTimeReportDetail.put("totalUsedVehicles", iAssignRouteBO.getAllTripsCountByDateOTA(tripDates, tripDates,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));
				List<EFmFmAssignRoutePO>   totalUsedVehiclesList=iAssignRouteBO.getAllTripsCountByDateNoShowView(tripDates, tripDates,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				List<Map<String, Object>> totalUsedVehicles = new ArrayList<Map<String, Object>>();				
				if(!totalUsedVehiclesList.isEmpty()){
					for(EFmFmAssignRoutePO listvalue:totalUsedVehiclesList){
						Map<String, Object> viewValues = new HashMap<String, Object>();
						viewValues.put("vehicleNumber", listvalue.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						viewValues.put("driverName", listvalue.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
						viewValues.put("vehicleModel", listvalue.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());				
						totalUsedVehicles.add(viewValues);
					}
				}			
				onTimeReportDetail.put("totalUsedVehiclesView",totalUsedVehicles);
				long noShowCount = iAssignRouteBO.getNoShowEmployeesCountByDateOTA(tripDates, tripDates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				
				List<EFmFmEmployeeTripDetailPO>   totalEmployeesNoShowList=iAssignRouteBO.getNoShowEmployeesCountByDateNoShowView(tripDates, tripDates,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				List<Map<String, Object>> noShowViewList = new ArrayList<Map<String, Object>>();				
				if(!totalEmployeesNoShowList.isEmpty()){
					for(EFmFmEmployeeTripDetailPO listvalue:totalEmployeesNoShowList){
						Map<String, Object> viewValues = new HashMap<String, Object>();
						viewValues.put("vehicleNumber", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						viewValues.put("employeeId", listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						viewValues.put("employeeName",new String(Base64.getDecoder().decode(listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
						noShowViewList.add(viewValues);
					}
				}				
				onTimeReportDetail.put("noShowView",noShowViewList);
				onTimeReportDetail.put("totalEmployeesNoShowCount", noShowCount);
				onTimeReportDetail.put("totalEmployeesPickedDropCount",
						iAssignRouteBO.getPickedUpEmployeesCountByDateOTA(tripDates, tripDates,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType()));		
				List<EFmFmEmployeeTripDetailPO>   totalEmployeesPickedDropCountView=iAssignRouteBO.getPickedUpEmployeesCountByDateView(tripDates, tripDates,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				List<Map<String, Object>> PickedDropCountViewList = new ArrayList<Map<String, Object>>();				
				if(!totalEmployeesPickedDropCountView.isEmpty()){
					for(EFmFmEmployeeTripDetailPO listvalue:totalEmployeesPickedDropCountView){
						Map<String, Object> viewValues = new HashMap<String, Object>();
						viewValues.put("vehicleNumber", listvalue.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
						viewValues.put("employeeId", listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
						viewValues.put("employeeName",new String(Base64.getDecoder().decode(listvalue.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()), "utf-8"));
						PickedDropCountViewList.add(viewValues);
					}
				}
				onTimeReportDetail.put("PickedDropCountView",PickedDropCountViewList);
				if (noShowCount > 0) {
					onTimeReport.add(onTimeReportDetail);
				}
			}
		} else if (!(assignRoutePO.getTime().equalsIgnoreCase("0"))
				&& assignRoutePO.getEmployeeId().equalsIgnoreCase("0")) {
			if (assignRoutePO.getTime().equalsIgnoreCase("1")) {
				List<EFmFmEmployeeTripDetailPO> allTripDetails = iAssignRouteBO.getAllNoShowEmployeesByDateWiseNoShow(
						fromDate, toDate, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),
						assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				log.info("time not 0 and emp 0" + selectedShiftTimes.size());
				log.info("Dates" + selectedShiftTimes.size());
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("From Date" + assignRoutePO.getToDate());
				// for (Time shiftTimeDetails : selectedShiftTimes) {
				for (EFmFmEmployeeTripDetailPO employeeDetail : allTripDetails) {
					Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
					onTimeReportDetail.put("tripDates",
							shiftFormate.format(employeeDetail.getEfmFmAssignRoute().getShiftTime()));
					onTimeReportDetail.put("actualTravelledDate",
							formatter.format(employeeDetail.getEfmFmAssignRoute().getTripAssignDate()));

					// totalUsedVehicles...means EmployeeId
					onTimeReportDetail.put("totalUsedVehicles",
							employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
					// totalEmployeesNoShowCount......Means employee name

					onTimeReportDetail.put("totalEmployeesPickedDropCount",
							new String(Base64.getDecoder().decode(
									employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
							"utf-8"));
					// totalEmployeesPickedDropCount No show value
					onTimeReportDetail.put("totalEmployeesNoShowCount", employeeDetail.getBoardedFlg());

					onTimeReport.add(onTimeReportDetail);
				}

			} else {
				String shiftDate = assignRoutePO.getTime();
				Date shift = shiftFormate.parse(shiftDate);
				java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
				List<EFmFmEmployeeTripDetailPO> allTripDetails = iAssignRouteBO.getAllNoShowEmployeesByIdAndNameNoShow(
						fromDate, toDate, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),
						assignRoutePO.getTripType(), shiftTime,assignRoutePO.getRequestType());
				log.info("time not 0 and emp 0" + selectedShiftTimes.size());
				log.info("Dates" + selectedShiftTimes.size());
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("From Date" + assignRoutePO.getToDate());
				// for (Time shiftTimeDetails : selectedShiftTimes) {
				for (EFmFmEmployeeTripDetailPO employeeDetail : allTripDetails) {
					Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
					onTimeReportDetail.put("tripDates",
							shiftFormate.format(employeeDetail.getEfmFmAssignRoute().getShiftTime()));
					onTimeReportDetail.put("actualTravelledDate",
							formatter.format(employeeDetail.getEfmFmAssignRoute().getTripAssignDate()));

					// totalUsedVehicles...means EmployeeId
					onTimeReportDetail.put("totalUsedVehicles",
							employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
					// totalEmployeesNoShowCount......Means employee name

					onTimeReportDetail.put("totalEmployeesPickedDropCount",
							new String(Base64.getDecoder().decode(
									employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
							"utf-8"));
					// totalEmployeesPickedDropCount No show value
					onTimeReportDetail.put("totalEmployeesNoShowCount", employeeDetail.getBoardedFlg());

					onTimeReport.add(onTimeReportDetail);
				}
			}
		} else if (!(assignRoutePO.getTime().equalsIgnoreCase("0"))
				&& !(assignRoutePO.getEmployeeId().equalsIgnoreCase("0"))) {
			if (assignRoutePO.getTime().equalsIgnoreCase("1")) {
				List<EFmFmEmployeeTripDetailPO> allTripDetails = iAssignRouteBO.getAllNoShowEmployeesByDateWiseNoShow(
						fromDate, toDate, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),
						assignRoutePO.getTripType(),assignRoutePO.getRequestType());
				log.info("time not 0 and emp not 0" + selectedShiftTimes.size());
				log.info("Dates" + selectedShiftTimes.size());
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("From Date" + assignRoutePO.getToDate());
				// for (Time shiftTimeDetails : selectedShiftTimes) {
				for (EFmFmEmployeeTripDetailPO employeeDetail : allTripDetails) {
					Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
					onTimeReportDetail.put("tripDates",
							shiftFormate.format(employeeDetail.getEfmFmAssignRoute().getShiftTime()));
					onTimeReportDetail.put("actualTravelledDate",
							formatter.format(employeeDetail.getEfmFmAssignRoute().getTripAssignDate()));

					onTimeReportDetail.put("shiftTime",
							shiftFormate.format(employeeDetail.getEfmFmAssignRoute().getShiftTime()));
					// totalUsedVehicles...means EmployeeId
					onTimeReportDetail.put("totalUsedVehicles",
							employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
					// totalEmployeesNoShowCount......Means employee name
					onTimeReportDetail.put("totalEmployeesPickedDropCount",
							new String(Base64.getDecoder().decode(
									employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
							"utf-8"));
					// totalEmployeesPickedDropCount No show value
					onTimeReportDetail.put("totalEmployeesNoShowCount", employeeDetail.getBoardedFlg());

					onTimeReport.add(onTimeReportDetail);
				}

			} else {
				String shiftDate = assignRoutePO.getTime();
				Date shift = shiftFormate.parse(shiftDate);
				java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
				List<EFmFmEmployeeTripDetailPO> allTripDetails = iAssignRouteBO.getAllNoShowEmployeesByEmployeeIdNoShow(
						fromDate, toDate, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),
						assignRoutePO.getTripType(), shiftTime, assignRoutePO.getEmployeeId(),assignRoutePO.getRequestType());
				log.info("time not 0 and emp not 0" + selectedShiftTimes.size());
				log.info("Dates" + selectedShiftTimes.size());
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("From Date" + assignRoutePO.getToDate());
				// for (Time shiftTimeDetails : selectedShiftTimes) {
				for (EFmFmEmployeeTripDetailPO employeeDetail : allTripDetails) {
					Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();
					onTimeReportDetail.put("tripDates",
							shiftFormate.format(employeeDetail.getEfmFmAssignRoute().getShiftTime()));
					onTimeReportDetail.put("actualTravelledDate",
							formatter.format(employeeDetail.getEfmFmAssignRoute().getTripAssignDate()));

					onTimeReportDetail.put("shiftTime",
							shiftFormate.format(employeeDetail.getEfmFmAssignRoute().getShiftTime()));
					// totalUsedVehicles...means EmployeeId
					onTimeReportDetail.put("totalUsedVehicles",
							employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
					// totalEmployeesNoShowCount......Means employee name
					onTimeReportDetail.put("totalEmployeesPickedDropCount",
							new String(Base64.getDecoder().decode(
									employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
							"utf-8"));
					// totalEmployeesPickedDropCount No show value
					onTimeReportDetail.put("totalEmployeesNoShowCount", employeeDetail.getBoardedFlg());

					onTimeReport.add(onTimeReportDetail);
				}
			}
		} else if (assignRoutePO.getTime().equalsIgnoreCase("0")
				&& !(assignRoutePO.getEmployeeId().equalsIgnoreCase("0"))) {
			List<EFmFmEmployeeTripDetailPO> allTripDetails = iAssignRouteBO.getAllNoShowEmployeesByEmployeeIdDateWise(
					fromDate, toDate, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getTripType(),
					assignRoutePO.getEmployeeId());
			log.info("time  0 and emp not 0" + selectedShiftTimes.size());
			log.info("Dates" + selectedShiftTimes.size());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			// for (Time shiftTimeDetails : selectedShiftTimes) {
			for (EFmFmEmployeeTripDetailPO employeeDetail : allTripDetails) {
				Map<String, Object> onTimeReportDetail = new HashMap<String, Object>();

				onTimeReportDetail.put("tripDates", formatter.format(employeeDetail.getActualTime()));
				onTimeReportDetail.put("actualTravelledDate",
						formatter.format(employeeDetail.getEfmFmAssignRoute().getTripAssignDate()));

				onTimeReportDetail.put("shiftTime",
						shiftFormate.format(employeeDetail.getEfmFmAssignRoute().getShiftTime()));
				// totalUsedVehicles...means EmployeeId
				onTimeReportDetail.put("totalUsedVehicles",
						employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
				// totalEmployeesNoShowCount......Means employee name
				onTimeReportDetail.put("totalEmployeesPickedDropCount",
						new String(Base64.getDecoder().decode(
								employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
						"utf-8"));
				// totalEmployeesPickedDropCount No show value
				onTimeReportDetail.put("totalEmployeesNoShowCount", employeeDetail.getBoardedFlg());

				onTimeReport.add(onTimeReportDetail);
			}

		}

		allTrips.put("tripDetail", onTimeReport);
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allTrips, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * @param Get
	 *            escort Reports get all the reports which is required Escort...
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	@POST
	@Path("/escortReport")
	public Response getEscortRequiredReport(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, UnsupportedEncodingException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}

		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat time = new SimpleDateFormat("HH:mm:ss");
		List<Map<String, Object>> escortReportList = new ArrayList<Map<String, Object>>();
		List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllEscortRequiredTripsByDate(fromDate, toDate,
				new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		log.info("From Date" + assignRoutePO.getFromDate());
		log.info("From Date" + assignRoutePO.getToDate());
		if ((!(allTripDetails.isEmpty())) || allTripDetails.size() != 0) {
			for (EFmFmAssignRoutePO trips : allTripDetails) {
				Map<String, Object> escortReport = new HashMap<String, Object>();
				escortReport.put("tripStartDate", formatter.format(trips.getTripStartTime()));
				escortReport.put("tripAssignDate", formatter.format(trips.getTripAssignDate()));
				escortReport.put("tripCompleteDate", formatter.format(trips.getTripCompleteTime()));
				escortReport.put("shiftTime", timeFormat.format(trips.getShiftTime()));
				escortReport.put("tripType", trips.getTripType());
				escortReport.put("vehicleNumber",
						trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				escortReport.put("vendorName",
						trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				escortReport.put("driverName", trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				escortReport.put("driverId", trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				escortReport.put("facilityName",
						trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

				try {
					int a = trips.geteFmFmEscortCheckIn().getEscortCheckInId();
					escortReport.put("escortName", trips.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
					escortReport.put("escortId", trips.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getEscortId());
					log.info("checkInId" + a);
				} catch (Exception e) {
					escortReport.put("escortName", "Escort Required But Not Available");
					escortReport.put("escortId", "NA");
				}
				escortReport.put("routeName", trips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");		
			
				if (trips.getTripType().equalsIgnoreCase("DROP")) {
					employeeTripDetailPO = iCabRequestBO.getDropTripAllSortedEmployees(trips.getAssignRouteId());
					if(employeeTripDetailPO.get(employeeTripDetailPO.size()- 1).getCabRecheddestinationTime()==0){						
						escortReport.put("pickOrDropTime","Not Calculated");
					}else{
						Date cabRecheddestinationTime=new Date(employeeTripDetailPO.get(employeeTripDetailPO.size()-1).getCabRecheddestinationTime());							
						Date destinationTime = format.parse(time.format(cabRecheddestinationTime.getTime()));						
						escortReport.put("pickOrDropTime",timeFormat.format(destinationTime.getTime()));
					}
				} else {
					employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployees(trips.getAssignRouteId());
					if(employeeTripDetailPO.get(0).getCabstartFromDestination()==0){						
						escortReport.put("pickOrDropTime","Not Calculated");
					}else{
						Date cabStartDestination=new Date(employeeTripDetailPO.get(0).getCabstartFromDestination());							
						Date destinationTime = format.parse(time.format(cabStartDestination.getTime()));						
						escortReport.put("pickOrDropTime",timeFormat.format(destinationTime.getTime()));
					}
				}
				escortReport
						.put("employeeName",
								new String(
										Base64.getDecoder().decode(employeeTripDetailPO.get(0)
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
								"utf-8"));
				escortReport.put("employeeId", employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
						.getEfmFmUserMaster().getEmployeeId());
				escortReportList.add(escortReport);
			}
		}
		allTrips.put("tripDetail", escortReportList);
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allTrips, MediaType.APPLICATION_JSON).build();
	}


	/**
	 * @param Get
	 *            escort Reports get all the reports which is required Escort...
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	@POST
	@Path("/escortReportDownload")
	public Response getEscortRequiredReportDownload(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, UnsupportedEncodingException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}

		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = (Date) formatter.parse(assignRoutePO.getFromDate());
		Date toDate = (Date) formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");

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
        int rownum = 0, noOfRoute = 0;
        Row OutSiderow = sheet.createRow(rownum++); 
        for (int columnIndex = 0; columnIndex < 15; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
				
				
				Cell zerothCol = OutSiderow.createCell(0);
                zerothCol.setCellValue("Date");
                zerothCol.setCellStyle(style);
				
				Cell firstCol = OutSiderow.createCell(1);
				firstCol.setCellValue("Vehicle Number");
                firstCol.setCellStyle(style);
				
				Cell secondCol = OutSiderow.createCell(2);
                secondCol.setCellValue("Driver Name	");
                secondCol.setCellStyle(style);
				
				Cell thirdCol = OutSiderow.createCell(3);
                thirdCol.setCellValue("Employee Name/Id");
                thirdCol.setCellStyle(style);
				
				Cell fourthCol = OutSiderow.createCell(4);
                fourthCol.setCellValue("ShiftTime");
                fourthCol.setCellStyle(style);
				
				Cell fifthCol = OutSiderow.createCell(5);
                fifthCol.setCellValue("Escort id");
                fifthCol.setCellStyle(style);
				
				Cell sixthCol = OutSiderow.createCell(6);
                sixthCol.setCellValue("Escort Name");
                sixthCol.setCellStyle(style);
				
				
				Cell seventhCol = OutSiderow.createCell(7);
                seventhCol.setCellValue("Time of Drop/Pickup");
                seventhCol.setCellStyle(style);
				
				Cell eighthCol = OutSiderow.createCell(8);
                eighthCol.setCellValue("Route");
                eighthCol.setCellStyle(style);
				
				Cell ninethCol = OutSiderow.createCell(9);
                ninethCol.setCellValue("Trip Type");
                ninethCol.setCellStyle(style);
                
                Cell facilityName= OutSiderow.createCell(10);
        		facilityName.setCellValue("Facility Name");
        		facilityName.setCellStyle(style);
                
		List<Map<String, Object>> escortReportList = new ArrayList<Map<String, Object>>();
		List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllEscortRequiredTripsByDate(fromDate, toDate,
				new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		log.info("From Date" + assignRoutePO.getFromDate());
		log.info("From Date" + assignRoutePO.getToDate());
		if ((!(allTripDetails.isEmpty())) || allTripDetails.size() != 0) {
			for (EFmFmAssignRoutePO trips : allTripDetails) {
				Map<String, Object> escortReport = new HashMap<String, Object>();
				OutSiderow = sheet.createRow(rownum++);
				Cell tripStartDate = OutSiderow.createCell(0);
				Cell vehicleNumber = OutSiderow.createCell(1);
				Cell driverName = OutSiderow.createCell(2);
				Cell employeeId = OutSiderow.createCell(3);
				Cell shiftTime = OutSiderow.createCell(4);
				Cell escortId = OutSiderow.createCell(5);
				Cell escortName = OutSiderow.createCell(6);
				Cell pickOrDropTime = OutSiderow.createCell(7);
				Cell routeName = OutSiderow.createCell(8);
				
				Cell tripType = OutSiderow.createCell(9);
				
				tripStartDate.setCellValue(formatter.format(trips.getTripStartTime()));
				
				escortReport.put("tripAssignDate", formatter.format(trips.getTripAssignDate()));
				escortReport.put("tripCompleteDate", formatter.format(trips.getTripCompleteTime()));
				shiftTime.setCellValue(timeFormat.format(trips.getShiftTime()));
				tripType.setCellValue(trips.getTripType());
				vehicleNumber.setCellValue(trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				escortReport.put("vendorName",
						trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				driverName.setCellValue( trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				escortReport.put("driverId", trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				
				Cell facilityNameCell = OutSiderow.createCell(10);
				facilityNameCell.setCellValue(trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

				try {
					int a = trips.geteFmFmEscortCheckIn().getEscortCheckInId();
					escortName.setCellValue( trips.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
					escortId.setCellValue( trips.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getEscortId());
					log.info("checkInId" + a);
				} catch (Exception e) {
					escortReport.put("escortName", "Escort Required But Not Available");
					escortReport.put("escortId", "NA");
					escortName.setCellValue("Escort Required But Not Available");
					escortId.setCellValue("NA");
					
				}
				routeName.setCellValue(trips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
							
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");		
				DateFormat time = new SimpleDateFormat("HH:mm:ss");				
				if (trips.getTripType().equalsIgnoreCase("DROP")) {
					employeeTripDetailPO = iCabRequestBO.getDropTripAllSortedEmployees(trips.getAssignRouteId());
					if(employeeTripDetailPO.get(employeeTripDetailPO.size()- 1).getCabRecheddestinationTime()==0){						
						pickOrDropTime.setCellValue("Not Calculated");
					}else{
						Date cabRecheddestinationTime=new Date(employeeTripDetailPO.get(employeeTripDetailPO.size()-1).getCabRecheddestinationTime());							
						Date destinationTime = format.parse(time.format(cabRecheddestinationTime.getTime()));						
						pickOrDropTime.setCellValue(timeFormat.format(destinationTime.getTime()));
					}
				} else {
					employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployees(trips.getAssignRouteId());
					if(employeeTripDetailPO.get(0).getCabstartFromDestination()==0){						
						pickOrDropTime.setCellValue("Not Calculated");
					}else{
						Date cabStartDestination=new Date(employeeTripDetailPO.get(0).getCabstartFromDestination());							
						Date destinationTime = format.parse(time.format(cabStartDestination.getTime()));						
						pickOrDropTime.setCellValue(timeFormat.format(destinationTime.getTime()));
					}
				}			
				escortReport
						.put("employeeName",
								new String(
										Base64.getDecoder().decode(employeeTripDetailPO.get(0)
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
								"utf-8"));
				escortReport.put("employeeId", employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
						.getEfmFmUserMaster().getEmployeeId());
				
				employeeId.setCellValue(new String(Base64.getDecoder().decode(employeeTripDetailPO.get(0)
								.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
				"utf-8")+""+employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
						.getEfmFmUserMaster().getEmployeeId());
				escortReportList.add(escortReport);
			}
		}
		allTrips.put("tripDetail", escortReportList);		
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

	/**
	 * @param Get
	 *            Get all getVehicleAndDriverAttendenceReport details method
	 *            will return all the checkin and Checkout details...
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/attendenceReport")
	public Response getVehicleAndDriverAttendenceReport(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat formatterParser = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		 log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		 		log.info("authenticationToken error"+e);
		 	}

		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		String currentDate = formatter.format(new Date());
		List<Map<String, Object>> attendenceReportList = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleCheckInPO> allTripDetails = null;
		if (currentDate.equalsIgnoreCase(assignRoutePO.getFromDate())) {
			allTripDetails = iVehicleCheckInBO
					.getAllPresentDriverAndVehicles(new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
			log.info("inside currentDate condition" + assignRoutePO.getToDate() + "size" + allTripDetails.size());

		} else {
			allTripDetails = iVehicleCheckInBO.getVehicleAndDriverAttendence(fromDate, toDate,
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
			log.info("inside date condition" + assignRoutePO.getToDate() + "size" + allTripDetails.size());

		}
		log.info("From Date" + assignRoutePO.getFromDate());
		log.info("to Date" + assignRoutePO.getToDate());
		if (!(allTripDetails.isEmpty())) {
			for (EFmFmVehicleCheckInPO trips : allTripDetails) {
				Map<String, Object> attendenceReport = new HashMap<String, Object>();
				if (currentDate.equalsIgnoreCase(assignRoutePO.getFromDate())) {
					attendenceReport.put("checkInDate", formatterParser.format(trips.getCheckInTime()));
					attendenceReport.put("checkOutDate", "..");
					if (trips.isAdminMailTriggerStatus()) {
						attendenceReport.put("adminMailTriggeredTime",
								formatterParser.format(trips.getAdminMailTriggerTime()));
					} else {
						attendenceReport.put("adminMailTriggeredTime", "NO");

					}
					if (trips.isSupervisorMailTriggerStatus()) {
						attendenceReport.put("supervisorMailTriggerStatus",
								formatterParser.format(trips.getSupervisorMailTriggerTime()));
					} else {
						attendenceReport.put("supervisorMailTriggerStatus", "NO");

					}

				} else {
					attendenceReport.put("checkOutDate", formatterParser.format(trips.getCheckOutTime()));
					attendenceReport.put("checkInDate", formatterParser.format(trips.getCheckInTime()));
					if (trips.isAdminMailTriggerStatus()) {
						attendenceReport.put("adminMailTriggeredTime",
								formatterParser.format(trips.getAdminMailTriggerTime()));
					} else {
						attendenceReport.put("adminMailTriggeredTime", "NO");

					}
					if (trips.isSupervisorMailTriggerStatus()) {
						attendenceReport.put("supervisorMailTriggerStatus",
								formatterParser.format(trips.getSupervisorMailTriggerTime()));
					} else {
						attendenceReport.put("supervisorMailTriggerStatus", "NO");

					}
				}
				attendenceReport.put("status", "Present");
				attendenceReport.put("vehicleNumber", trips.getEfmFmVehicleMaster().getVehicleNumber());
				attendenceReport.put("vendorName",
						trips.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				attendenceReport.put("driverName", trips.getEfmFmDriverMaster().getFirstName());
				attendenceReport.put("driverId", trips.getEfmFmDriverMaster().getDriverId());
				attendenceReport.put("facilityName",
						trips.getEfmFmDriverMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				attendenceReportList.add(attendenceReport);
			}
		}
		allTrips.put("tripDetail", attendenceReportList);
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allTrips, MediaType.APPLICATION_JSON).build();
	}
	
	
	/**
	 * @param Get
	 *            Get all getVehicleAndDriverAttendenceReport details method
	 *            will return all the checkin and Checkout details...
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/attendenceReportDownload")
	public Response getVehicleAndDriverAttendenceReportDownload(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		 log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		 		log.info("authenticationToken error"+e);
		 	}
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat formatterParser = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
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
		int rownum = 0, noOfRoute = 0;
		Row OutSiderow = sheet.createRow(rownum++); 
		for (int columnIndex = 0; columnIndex < 15; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
		}
				
				
				Cell zerothCol = OutSiderow.createCell(0);
				zerothCol.setCellValue("CheckInDate");
				zerothCol.setCellStyle(style);
				
				Cell firstCol = OutSiderow.createCell(1);
				firstCol.setCellValue("CheckOutDate");
				firstCol.setCellStyle(style);
				
				Cell secondCol = OutSiderow.createCell(2);
				secondCol.setCellValue("AdminMailTriggerStatus");
				secondCol.setCellStyle(style);
				
				Cell thirdCol = OutSiderow.createCell(3);
				thirdCol.setCellValue("SupervisorMailTriggerStatus");
				thirdCol.setCellStyle(style);
				
				Cell fourthCol = OutSiderow.createCell(4);
				fourthCol.setCellValue("Vendor Name");
				fourthCol.setCellStyle(style);
				
				Cell fifthCol = OutSiderow.createCell(5);
				fifthCol.setCellValue("Vehicle Number");
				fifthCol.setCellStyle(style);
				
				Cell sixthCol = OutSiderow.createCell(6);
				sixthCol.setCellValue("Driver Id");
				sixthCol.setCellStyle(style);
				
				
				Cell seventhCol = OutSiderow.createCell(7);
				seventhCol.setCellValue("Driver Name");
				seventhCol.setCellStyle(style);
				
				Cell eighthCol = OutSiderow.createCell(8);
				eighthCol.setCellValue("Status");
				eighthCol.setCellStyle(style);
				
				Cell facilityName= OutSiderow.createCell(9);
				facilityName.setCellValue("Facility Name");
				facilityName.setCellStyle(style);
				
		Date fromDate = (Date) formatter.parse(assignRoutePO.getFromDate());
		Date toDate = (Date) formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		String currentDate = formatter.format(new Date());
		List<Map<String, Object>> attendenceReportList = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleCheckInPO> allTripDetails = null;
		if (currentDate.equalsIgnoreCase(assignRoutePO.getFromDate())) {
			allTripDetails = iVehicleCheckInBO
					.getAllPresentDriverAndVehicles(new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
			log.info("inside currentDate condition" + assignRoutePO.getToDate() + "size" + allTripDetails.size());

		} else {
			allTripDetails = iVehicleCheckInBO.getVehicleAndDriverAttendence(fromDate, toDate,
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
			log.info("inside date condition" + assignRoutePO.getToDate() + "size" + allTripDetails.size());

		}
		log.info("From Date" + assignRoutePO.getFromDate());
		log.info("to Date" + assignRoutePO.getToDate());
		if (!(allTripDetails.isEmpty())) {
			for (EFmFmVehicleCheckInPO trips : allTripDetails) {
				OutSiderow = sheet.createRow(rownum++);


				Cell checkInDateCell = OutSiderow.createCell(0);	
				Cell checkOutDateCell = OutSiderow.createCell(1);
				Cell adminMailTriggeredTime = OutSiderow.createCell(2);
				Cell supervisorMailTriggerStatus = OutSiderow.createCell(3);
				Map<String, Object> attendenceReport = new HashMap<String, Object>();
				if (currentDate.equalsIgnoreCase(assignRoutePO.getFromDate())) {
										
					attendenceReport.put("checkInDate", formatterParser.format(trips.getCheckInTime()));					
					checkInDateCell.setCellValue(formatterParser.format(trips.getCheckInTime()));
					attendenceReport.put("checkOutDate", "..");
					
					checkOutDateCell.setCellValue("..");				
					
					
					if (trips.isAdminMailTriggerStatus()) {
						attendenceReport.put("adminMailTriggeredTime",formatterParser.format(trips.getAdminMailTriggerTime()));
						 adminMailTriggeredTime.setCellValue(formatterParser.format(trips.getAdminMailTriggerTime()));
					} else {
						attendenceReport.put("adminMailTriggeredTime", "NO");
						 adminMailTriggeredTime.setCellValue("NO");
					}
					
					if (trips.isSupervisorMailTriggerStatus()) {
						attendenceReport.put("supervisorMailTriggerStatus",formatterParser.format(trips.getSupervisorMailTriggerTime()));
						supervisorMailTriggerStatus.setCellValue(formatterParser.format(trips.getSupervisorMailTriggerTime()));
					} else {
						attendenceReport.put("supervisorMailTriggerStatus", "NO");
						supervisorMailTriggerStatus.setCellValue("NO");

					}

				} else {
					attendenceReport.put("checkOutDate", formatterParser.format(trips.getCheckOutTime()));
					checkOutDateCell.setCellValue(formatterParser.format(trips.getCheckOutTime()));					
					attendenceReport.put("checkInDate", formatterParser.format(trips.getCheckInTime()));
					checkInDateCell.setCellValue(formatterParser.format(trips.getCheckInTime()));
					if (trips.isAdminMailTriggerStatus()) {
						attendenceReport.put("adminMailTriggeredTime",
								formatterParser.format(trips.getAdminMailTriggerTime()));
						adminMailTriggeredTime.setCellValue(formatterParser.format(trips.getAdminMailTriggerTime()));
					} else {
						attendenceReport.put("adminMailTriggeredTime", "NO");
						adminMailTriggeredTime.setCellValue("NO");

					}
					if (trips.isSupervisorMailTriggerStatus()) {
						attendenceReport.put("supervisorMailTriggerStatus",
								formatterParser.format(trips.getSupervisorMailTriggerTime()));
						supervisorMailTriggerStatus.setCellValue(formatterParser.format(trips.getSupervisorMailTriggerTime()));
					} else {
						attendenceReport.put("supervisorMailTriggerStatus", "NO");
						supervisorMailTriggerStatus.setCellValue("NO");

					}
				}		
				attendenceReport.put("status", "Present");
				Cell status = OutSiderow.createCell(8);
				status.setCellValue( "Present");
				attendenceReport.put("vehicleNumber", trips.getEfmFmVehicleMaster().getVehicleNumber());
				Cell vehicleNumber = OutSiderow.createCell(5);
				vehicleNumber.setCellValue(trips.getEfmFmVehicleMaster().getVehicleNumber());
				attendenceReport.put("vendorName",trips.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				Cell vendorName = OutSiderow.createCell(4);
				vendorName.setCellValue(trips.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				attendenceReport.put("driverName", trips.getEfmFmDriverMaster().getFirstName());
				Cell driverName = OutSiderow.createCell(7);
				driverName.setCellValue(trips.getEfmFmDriverMaster().getFirstName());
				attendenceReport.put("driverId", trips.getEfmFmDriverMaster().getDriverId());
				Cell driverId = OutSiderow.createCell(6);
				driverId.setCellValue(trips.getEfmFmDriverMaster().getDriverId());
				Cell facilityNameCell = OutSiderow.createCell(9);
				facilityNameCell.setCellValue(trips.getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				attendenceReportList.add(attendenceReport);
			}
		}
		allTrips.put("tripDetail", attendenceReportList);		
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
	/**
	 * @param Get
	 *            Get all getDriverWorkingHours details method will return all
	 *            the checkin and Checkout details... Basically deriver working
	 *            ours details
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/driverWorkinHoursReport")
	public Response getDriverWorkingHours(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		 log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		 		log.info("authenticationToken error"+e);
		 	}
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		List<Map<String, Object>> driverWorkingReportList = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleCheckInPO> allTripDetails = iVehicleCheckInBO.getVehicleAndDriverAttendence(fromDate, toDate,
				new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		log.info("From Date" + assignRoutePO.getFromDate());
		log.info("From Date" + assignRoutePO.getToDate());
		log.info("data size" + allTripDetails.size());

		if(!(allTripDetails.isEmpty())) {
			for (EFmFmVehicleCheckInPO trips : allTripDetails) {
				Map<String, Object> driverWorkingReport = new HashMap<String, Object>();
				driverWorkingReport.put("date", formatter.format(trips.getCheckOutTime()));
				driverWorkingReport.put("loginTime", dateFormatter.format(trips.getCheckInTime()));
				try{
				driverWorkingReport.put("remarks", trips.getCheckOutRemarks());
				}catch(Exception e){
					driverWorkingReport.put("remarks", "Not given");
				}
				driverWorkingReport.put("logOutTime", dateFormatter.format(trips.getCheckOutTime()));
				long millis = trips.getCheckOutTime().getTime() - trips.getCheckInTime().getTime();
				String workingHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
						TimeUnit.MILLISECONDS.toMinutes(millis)
								- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
						TimeUnit.MILLISECONDS.toSeconds(millis)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
				driverWorkingReport.put("totalWorkingHours", workingHours);
				driverWorkingReport.put("vehicleNumber", trips.getEfmFmVehicleMaster().getVehicleNumber());
				driverWorkingReport.put("vendorName",
						trips.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				driverWorkingReport.put("driverName", trips.getEfmFmDriverMaster().getFirstName());
				driverWorkingReport.put("driverId", trips.getEfmFmDriverMaster().getDriverId());
				driverWorkingReport.put("facilityName",
						trips.getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				driverWorkingReportList.add(driverWorkingReport);
			}
		}
		allTrips.put("tripDetail", driverWorkingReportList);
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allTrips, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * @param Get
	 *            Get all getDriverDrivingHours details method will return all
	 *            the driving hours details... Basically deriver driving hours
	 *            details
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/driverDrivingHoursReport")
	public Response getDriverDrivingHours(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}
		
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		DateFormat drivingHoursFormate = new SimpleDateFormat("HH:mm");
		List<Date> selectedDates = new ArrayList<Date>();
		// List<Object> dateAndDriverId=new ArrayList<Object>();
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		selectedDates = iAssignRouteBO.getAllTripsByDistinctDates(fromDate, toDate,
				new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		log.info("all zero" + selectedDates.size());
		log.info("Dates" + selectedDates.size());
		log.info("From Date" + assignRoutePO.getFromDate());
		log.info("From Date" + assignRoutePO.getToDate());
		// Map<String, Object> allTrips = new HashMap<String, Object>();
		List<Map<String, Object>> allTripsDetailsData = new ArrayList<Map<String, Object>>();
		if ((!(selectedDates.isEmpty())) || selectedDates.size() != 0) {
			for (Date tripdates : selectedDates) {
				List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllTripByDate(tripdates, tripdates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
				System.out.println("size" + allTripDetails.size() + "DATE"
						+ dateFormatter.format(allTripDetails.get(0).getTripAssignDate()));
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("To Date" + assignRoutePO.getToDate());
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
											new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),
											trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
							System.out.println("size" + allTripDetailsByDriverId.size() + "DATE"
									+ dateFormatter.format(allTripDetailsByDriverId.get(0).getTripAssignDate()));
							long millis = 0;
							Time timeAdd = null;
							for (EFmFmAssignRoutePO drivertrips : allTripDetailsByDriverId) {
								Map<String, Object> driverDrivingReport = new HashMap<String, Object>();
								dateAndDriverId.add(dateFormatter.format(drivertrips.getTripAssignDate()));
								dateAndDriverId
										.add(drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
								driverDrivingReport.put("tripAssignedDate", formatter.format(drivertrips.getTripAssignDate()));
								driverDrivingReport.put("assignRouteId",drivertrips.getAssignRouteId());
								if(drivertrips.getTripStartTime() !=null){
								driverDrivingReport.put("tripStartDate",
										dateFormatter.format(drivertrips.getTripStartTime()));
								}else{
									driverDrivingReport.put("tripStartDate","");
								}
								if(drivertrips.getTripCompleteTime()!=null){
									driverDrivingReport.put("tripCompleteDate",
											dateFormatter.format(drivertrips.getTripCompleteTime()));
								}else{
									driverDrivingReport.put("tripCompleteDate","");
								}								
								driverDrivingReport.put("vehicleNumber", drivertrips.getEfmFmVehicleCheckIn()
										.getEfmFmVehicleMaster().getVehicleNumber());
								if(drivertrips.getDrivingHours()==null){
									if(drivertrips.getTripCompleteTime() ==null || drivertrips.getTripStartTime() ==null){
										millis +=0;
									}else{
										millis += drivertrips.getTripCompleteTime().getTime()- drivertrips.getTripStartTime().getTime();
									}
									
								}else{
									//millis += drivertrips.getDrivingHours().getTime();
									if(timeAdd==null){
										log.info("Time_Add" + drivertrips.getDrivingHours());
										timeAdd=drivertrips.getDrivingHours();
										log.info("Time_Add_1" + timeAdd);
									}else{
										timeAdd =SumOfMinutes(timeAdd, drivertrips.getDrivingHours());
									}
								}

								
								
								
							if(drivertrips.getDrivingHours()==null){
								long millisRouteMilles=0;
								if(drivertrips.getTripCompleteTime() ==null || drivertrips.getTripStartTime() ==null){
									millisRouteMilles=0;
								}else{
									millisRouteMilles = drivertrips.getTripCompleteTime().getTime()- drivertrips.getTripStartTime().getTime();
								}
									log.info("millisRouteMilles" + millisRouteMilles);
									String routeTravellHours = String.format("%02d:%02d:%02d",
											TimeUnit.MILLISECONDS.toHours(millisRouteMilles),
											TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles) - TimeUnit.HOURS
													.toMinutes(TimeUnit.MILLISECONDS.toHours(millisRouteMilles)),
											TimeUnit.MILLISECONDS.toSeconds(millisRouteMilles) - TimeUnit.MINUTES
													.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles)));
									log.info("routeTravellHours" + routeTravellHours);
									driverDrivingReport.put("driverDrivingHoursPerTrip", routeTravellHours);
									driverDrivingReport.put("driverDrivingHoursPerTripFlg","N");
								}else{
									driverDrivingReport.put("driverDrivingHoursPerTripFlg","M");
									driverDrivingReport.put("driverDrivingHoursPerTrip", timeFormat.format(drivertrips.getDrivingHours()));
								}						
								driverDrivingReport.put("vendorName", drivertrips.getEfmFmVehicleCheckIn()
										.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
								driverDrivingReport.put("driverName",
										drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								driverDrivingReport.put("driverId",
										drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
								driverDrivingReport.put("tripType", drivertrips.getTripType());
								driverDrivingReport.put("routeName",
										drivertrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
								driverDrivingReport.put("assignRouteId",
										drivertrips.getAssignRouteId());
								driverDrivingReport.put("facilityName",
										drivertrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
								driverDrivingReportList.add(driverDrivingReport);
							}
							String travellHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
									TimeUnit.MILLISECONDS.toMinutes(millis)
											- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
									TimeUnit.MILLISECONDS.toSeconds(millis)
											- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
							
							String drivingHoursTime = travellHours;
							Date shift = drivingHoursFormate.parse(drivingHoursTime);
							java.sql.Time drivingHours = new java.sql.Time(shift.getTime());
							if(timeAdd!=null){								
								driverReport.put("totalDrivingHours", timeFormat.format(SumOfMinutes(timeAdd, drivingHours)));
							}else{
								driverReport.put("totalDrivingHours", travellHours);
							}	
							driverReport.put("tripCount", allTripDetailsByDriverId.size());							
							driverReport.put("tripsDetails", driverDrivingReportList);
							allTripsDetailsData.add(driverReport);
						}

					}
				}
			}

		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allTripsDetailsData, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Updateing drivers driving hours 
	 * 
	 */
	


	@POST
	@Path("/updateDriverDrivingHours")
	public Response updateDriverDrivingHours(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		
				
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
				log.info("authenticationToken error"+e);
			}
		 
		DateFormat drivingHoursFormate = new SimpleDateFormat("HH:mm");
		String drivingHoursTime = assignRoutePO.getTime();
		Date shift = drivingHoursFormate.parse(drivingHoursTime);
		java.sql.Time drivingHours = new java.sql.Time(shift.getTime());
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		if (!(assignRoute.isEmpty())) {
			assignRoute.get(0).setDrivingHours(drivingHours);
			assignRouteBO.update(assignRoute.get(0));
		}
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/**
	 * @param Get
	 *            Get all getDriverWorkingHours details method will return all
	 *            the checkin and Checkout details... Basically deriver working
	 *            ours details
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/driverWorkinHoursReportDownload")
	public Response getDriverWorkingHoursDownload(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
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
				log.info("authenticationToken error"+e);
			}
		
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
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
        int rownum = 0, noOfRoute = 0;
        Row OutSiderow = sheet.createRow(rownum++); 
        for (int columnIndex = 0; columnIndex < 15; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
				
				
				Cell zerothCol = OutSiderow.createCell(0);
                zerothCol.setCellValue("Date");
                zerothCol.setCellStyle(style);
				
				Cell firstCol = OutSiderow.createCell(1);
                firstCol.setCellValue("Vendor Name");
                firstCol.setCellStyle(style);
				
				Cell secondCol = OutSiderow.createCell(2);
                secondCol.setCellValue("Vehicle Number");
                secondCol.setCellStyle(style);
				
				Cell thirdCol = OutSiderow.createCell(3);
                thirdCol.setCellValue("Driver Id");
                thirdCol.setCellStyle(style);
				
				Cell fourthCol = OutSiderow.createCell(4);
                fourthCol.setCellValue("Driver Name	");
                fourthCol.setCellStyle(style);
				
				Cell fifthCol = OutSiderow.createCell(5);
                fifthCol.setCellValue("Login time");
                fifthCol.setCellStyle(style);
				
				Cell sixthCol = OutSiderow.createCell(6);
                sixthCol.setCellValue("Logout time");
                sixthCol.setCellStyle(style);				
				
				Cell seventhCol = OutSiderow.createCell(7);
                seventhCol.setCellValue("Total Hours");
                seventhCol.setCellStyle(style);
                Cell facilityName= OutSiderow.createCell(8);
        		facilityName.setCellValue("Facility Name");
        		facilityName.setCellStyle(style);
                
		Date fromDate = (Date) formatter.parse(assignRoutePO.getFromDate());
		Date toDate = (Date) formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		List<Map<String, Object>> driverWorkingReportList = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleCheckInPO> allTripDetails = iVehicleCheckInBO.getVehicleAndDriverAttendence(fromDate, toDate,
				new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		log.info("From Date" + assignRoutePO.getFromDate());
		log.info("From Date" + assignRoutePO.getToDate());
		if ((!(allTripDetails.isEmpty())) || allTripDetails.size() != 0) {
			for (EFmFmVehicleCheckInPO trips : allTripDetails) {
				OutSiderow = sheet.createRow(rownum++);


				
				
				Map<String, Object> driverWorkingReport = new HashMap<String, Object>();
				driverWorkingReport.put("date", formatter.format(trips.getCheckOutTime()));
				Cell date = OutSiderow.createCell(0);
				date.setCellValue(formatter.format(trips.getCheckOutTime()));
				
				driverWorkingReport.put("loginTime", dateFormatter.format(trips.getCheckInTime()));
				Cell loginTime = OutSiderow.createCell(5);
				loginTime.setCellValue(dateFormatter.format(trips.getCheckInTime()));
				driverWorkingReport.put("logOutTime", dateFormatter.format(trips.getCheckOutTime()));
				Cell logOutTime = OutSiderow.createCell(6);
				logOutTime.setCellValue(dateFormatter.format(trips.getCheckOutTime()));
				long millis = trips.getCheckOutTime().getTime() - trips.getCheckInTime().getTime();
				String workingHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
						TimeUnit.MILLISECONDS.toMinutes(millis)
								- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
						TimeUnit.MILLISECONDS.toSeconds(millis)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
				driverWorkingReport.put("totalWorkingHours", workingHours);
				
				Cell totalWorkingHours = OutSiderow.createCell(7);
				totalWorkingHours.setCellValue(workingHours);
				
				driverWorkingReport.put("vehicleNumber", trips.getEfmFmVehicleMaster().getVehicleNumber());
				Cell vehicleNumber = OutSiderow.createCell(2);
				vehicleNumber.setCellValue(trips.getEfmFmVehicleMaster().getVehicleNumber());
				
				driverWorkingReport.put("vendorName",
						trips.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				Cell vendorName = OutSiderow.createCell(1);
				vendorName.setCellValue(trips.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				
				driverWorkingReport.put("driverName", trips.getEfmFmDriverMaster().getFirstName());
				Cell driverName = OutSiderow.createCell(4);
				driverName.setCellValue(trips.getEfmFmDriverMaster().getFirstName());
				
				driverWorkingReport.put("driverId", trips.getEfmFmDriverMaster().getDriverId());
				
				Cell driverId = OutSiderow.createCell(3);
				driverId.setCellValue(trips.getEfmFmDriverMaster().getDriverId());
				driverWorkingReportList.add(driverWorkingReport);
				Cell facilityNameCell = OutSiderow.createCell(8);
				facilityNameCell.setCellValue(trips.getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
			}
		}
		allTrips.put("tripDetail", driverWorkingReportList);
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

	/**
	 * @param Get
	 *            Get all getDriverDrivingHours details method will return all
	 *            the driving hours details... Basically deriver driving hours
	 *            details
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/driverDrivingHoursReportDownload")
	public Response getDriverDrivingHoursDownload(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
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
				log.info("authenticationToken error"+e);
			}
	
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		DateFormat drivingHoursFormate = new SimpleDateFormat("HH:mm");
		List<Date> selectedDates = new ArrayList<Date>();
		// List<Object> dateAndDriverId=new ArrayList<Object>();
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
        int rownum = 0, noOfRoute = 0;
        Row OutSiderow = sheet.createRow(rownum++); 
        for (int columnIndex = 0; columnIndex < 15; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
				
				
				Cell zerothCol = OutSiderow.createCell(0);
                zerothCol.setCellValue("Date");
                zerothCol.setCellStyle(style);
				
				Cell firstCol = OutSiderow.createCell(1);
                firstCol.setCellValue("Vendor Name");
                firstCol.setCellStyle(style);
				
				Cell secondCol = OutSiderow.createCell(2);
                secondCol.setCellValue("Vehicle Number");
                secondCol.setCellStyle(style);
				
				Cell thirdCol = OutSiderow.createCell(3);
                thirdCol.setCellValue("Driver Id");
                thirdCol.setCellStyle(style);
				
				Cell fourthCol = OutSiderow.createCell(4);
                fourthCol.setCellValue("Driver Name	");
                fourthCol.setCellStyle(style);
				
				Cell fifthCol = OutSiderow.createCell(5);
                fifthCol.setCellValue("Trip Type");
                fifthCol.setCellStyle(style);
				
				Cell sixthCol = OutSiderow.createCell(6);
                sixthCol.setCellValue("Route Name");
                sixthCol.setCellStyle(style);				
				
				Cell seventhCol = OutSiderow.createCell(7);
                seventhCol.setCellValue("Trip start time");
                seventhCol.setCellStyle(style);
                
                Cell eighthCol = OutSiderow.createCell(8);
                eighthCol.setCellValue("Trip End time");
                eighthCol.setCellStyle(style);
				
				Cell ninethCol = OutSiderow.createCell(9);
                ninethCol.setCellValue("Driving hrs/trip");
                ninethCol.setCellStyle(style);
				
				Cell tenthCol = OutSiderow.createCell(10);
                tenthCol.setCellValue("Total driving hrs");
                tenthCol.setCellStyle(style);
                
                Cell facilityName= OutSiderow.createCell(11);
        		facilityName.setCellValue("Facility Name");
        		facilityName.setCellStyle(style);

		Date fromDate = (Date) formatter.parse(assignRoutePO.getFromDate());
		Date toDate = (Date) formatter.parse(assignRoutePO.getToDate());
		selectedDates = iAssignRouteBO.getAllTripsByDistinctDates(fromDate, toDate,
				new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		log.info("all zero" + selectedDates.size());
		log.info("Dates" + selectedDates.size());
		log.info("From Date" + assignRoutePO.getFromDate());
		log.info("From Date" + assignRoutePO.getToDate());
		// Map<String, Object> allTrips = new HashMap<String, Object>();
		List<Map<String, Object>> allTripsDetailsData = new ArrayList<Map<String, Object>>();
		if ((!(selectedDates.isEmpty())) || selectedDates.size() != 0) {
			for (Date tripdates : selectedDates) {
				List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllTripByDate(tripdates, tripdates,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
				System.out.println("size" + allTripDetails.size() + "DATE"
						+ dateFormatter.format(allTripDetails.get(0).getTripAssignDate()));
				log.info("From Date" + assignRoutePO.getFromDate());
				log.info("To Date" + assignRoutePO.getToDate());
				if ((!(allTripDetails.isEmpty())) || allTripDetails.size() != 0) {
					List<Object> dateAndDriverId = new ArrayList<Object>();
					for (EFmFmAssignRoutePO trips : allTripDetails) {
						//OutSiderow = sheet.createRow(rownum++);				
						List<Map<String, Object>> driverDrivingReportList = new ArrayList<Map<String, Object>>();
						Map<String, Object> driverReport = new HashMap<String, Object>();
						if (!(dateAndDriverId.contains(dateFormatter.format(trips.getTripAssignDate()))
								&& dateAndDriverId.contains(
										trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId()))) {
							List<EFmFmAssignRoutePO> allTripDetailsByDriverId = iAssignRouteBO
									.getAllTripsByDatesAndDriverId(tripdates, tripdates,
											new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),
											trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
							System.out.println("size" + allTripDetailsByDriverId.size() + "DATE"
									+ dateFormatter.format(allTripDetailsByDriverId.get(0).getTripAssignDate()));
							long millis = 0;
							Time timeAdd = null;
							for (EFmFmAssignRoutePO drivertrips : allTripDetailsByDriverId) {
								OutSiderow = sheet.createRow(rownum++);	
								Map<String, Object> driverDrivingReport = new HashMap<String, Object>();
								dateAndDriverId.add(dateFormatter.format(drivertrips.getTripAssignDate()));
								dateAndDriverId
										.add(drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
								driverDrivingReport.put("date", formatter.format(drivertrips.getTripAssignDate()));
								Cell dateCell = OutSiderow.createCell(0);
								dateCell.setCellValue(formatter.format(drivertrips.getTripAssignDate()));
								if(drivertrips.getTripStartTime() !=null){
									driverDrivingReport.put("tripStartDate",dateFormatter.format(drivertrips.getTripStartTime()));
								}else{
									driverDrivingReport.put("tripStartDate","");
								}
								Cell tripStartDateCell = OutSiderow.createCell(7);
								if(drivertrips.getTripStartTime() !=null){
									tripStartDateCell.setCellValue(dateFormatter.format(drivertrips.getTripStartTime()));
								}else{
									tripStartDateCell.setCellValue("N/A");
								}
								if(drivertrips.getTripCompleteTime()!=null){
									driverDrivingReport.put("tripCompleteDate",
											dateFormatter.format(drivertrips.getTripCompleteTime()));
								}else{
									driverDrivingReport.put("tripCompleteDate","");
								}
								Cell tripCompleteDateCell = OutSiderow.createCell(8);				
								if(drivertrips.getTripCompleteTime()!=null){
									tripCompleteDateCell.setCellValue(dateFormatter.format(drivertrips.getTripCompleteTime()));
								}else{
									tripStartDateCell.setCellValue("N/A");
								}				
								
								Cell facilityNameCell = OutSiderow.createCell(11);
								facilityNameCell.setCellValue(drivertrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
								driverDrivingReport.put("vehicleNumber", drivertrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
								Cell vehicleNumberCell = OutSiderow.createCell(2);
								vehicleNumberCell.setCellValue(drivertrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());								
								if(drivertrips.getDrivingHours()==null){
									if(drivertrips.getTripCompleteTime() ==null || drivertrips.getTripStartTime() ==null){
										millis +=0;
									}else{
										millis += drivertrips.getTripCompleteTime().getTime()- drivertrips.getTripStartTime().getTime();
									}
									
								}else{
									if(timeAdd==null){								
										timeAdd=drivertrips.getDrivingHours();										
									}else{
										timeAdd =SumOfMinutes(timeAdd, drivertrips.getDrivingHours());
									}
								}

								if(drivertrips.getDrivingHours()==null){
									long millisRouteMilles=0;
									if(drivertrips.getTripCompleteTime() ==null || drivertrips.getTripStartTime() ==null){
										millisRouteMilles=0;
									}else{
										millisRouteMilles = drivertrips.getTripCompleteTime().getTime()- drivertrips.getTripStartTime().getTime();
									}
									log.info("millisRouteMilles" + millisRouteMilles);
									String routeTravellHours = String.format("%02d:%02d:%02d",
											TimeUnit.MILLISECONDS.toHours(millisRouteMilles),
											TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles) - TimeUnit.HOURS
													.toMinutes(TimeUnit.MILLISECONDS.toHours(millisRouteMilles)),
											TimeUnit.MILLISECONDS.toSeconds(millisRouteMilles) - TimeUnit.MINUTES
													.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisRouteMilles)));
									log.info("routeTravellHours" + routeTravellHours);
									driverDrivingReport.put("driverDrivingHoursPerTrip", routeTravellHours);
									driverDrivingReport.put("driverDrivingHoursPerTripFlg","N");
									Cell driverDrivingHoursPerTripCell = OutSiderow.createCell(9);
									driverDrivingHoursPerTripCell.setCellValue(routeTravellHours);
								}else{
									driverDrivingReport.put("driverDrivingHoursPerTripFlg","M");								
									Cell driverDrivingHoursPerTripCell = OutSiderow.createCell(9);
									driverDrivingHoursPerTripCell.setCellValue(timeFormat.format(drivertrips.getDrivingHours()));
								}		
								
								driverDrivingReport.put("vendorName", drivertrips.getEfmFmVehicleCheckIn()
										.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
								Cell vendorNameCell = OutSiderow.createCell(1);
								vendorNameCell.setCellValue(drivertrips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
								driverDrivingReport.put("driverName",drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								Cell driverNameCell = OutSiderow.createCell(4);
								driverNameCell.setCellValue(drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
								driverDrivingReport.put("driverId",drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
								
								Cell driverIdCell = OutSiderow.createCell(3);
								driverIdCell.setCellValue(drivertrips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
								driverDrivingReport.put("tripType", drivertrips.getTripType());
								Cell tripTypeCell = OutSiderow.createCell(5);
								tripTypeCell.setCellValue(drivertrips.getTripType());
								
								driverDrivingReport.put("routeName",drivertrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
								Cell routeNameCell = OutSiderow.createCell(6);
								routeNameCell.setCellValue(drivertrips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
								driverDrivingReportList.add(driverDrivingReport);
							}
							String travellHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
									TimeUnit.MILLISECONDS.toMinutes(millis)
											- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
									TimeUnit.MILLISECONDS.toSeconds(millis)
											- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
							
							String drivingHoursTime = travellHours;
							Date shift = drivingHoursFormate.parse(drivingHoursTime);
							java.sql.Time drivingHours = new java.sql.Time(shift.getTime());
							Cell totalDrivingHoursCell = OutSiderow.createCell(10);
							if(timeAdd!=null){						
								totalDrivingHoursCell.setCellValue(timeFormat.format(SumOfMinutes(timeAdd, drivingHours)));
							}else{								
								totalDrivingHoursCell.setCellValue(travellHours);
							}
							driverReport.put("tripCount", allTripDetailsByDriverId.size());		
							driverReport.put("tripsDetails", driverDrivingReportList);
							allTripsDetailsData.add(driverReport);
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
	
	
	/**
	 * @param Get
	 *            Get all getSpeedAlertVehicleAndVendorWise details method will
	 *            return speed alert of the selected vehicle...
	 * 
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/speedReport")
	public Response getSpeedAlertVehicleAndVendorWise(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		 log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		 		log.info("authenticationToken error"+e);
		 	}
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		List<Map<String, Object>> vehicleSpeedAlertsList = new ArrayList<Map<String, Object>>();
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		log.info("assignRoutePO.getVehicleId()" + assignRoutePO.getVehicleId());
		log.info("assignRoutePO.getVendorId()" + assignRoutePO.getVendorId());
		if (assignRoutePO.getVehicleId() == 0 && assignRoutePO.getVendorId() == 0) {
			List<EFmFmTripAlertsPO> allSpeedAlerts = iAlertBO.getAllTripAlertsForSelectedDates(fromDate, toDate,
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			if ((!(allSpeedAlerts.isEmpty())) || allSpeedAlerts.size() != 0) {
				for (EFmFmTripAlertsPO tripsAlerts : allSpeedAlerts) {
					Map<String, Object> vehicleSpeedAlerts = new HashMap<String, Object>();
					vehicleSpeedAlerts.put("dateTime", dateFormatter.format(tripsAlerts.getCreationTime()));
					vehicleSpeedAlerts.put("shiftTime",
							timeFormat.format(tripsAlerts.getEfmFmAssignRoute().getShiftTime()));
					vehicleSpeedAlerts.put("vehicleNumber", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmVehicleMaster().getVehicleNumber());
					vehicleSpeedAlerts.put("vendorName", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					vehicleSpeedAlerts.put("driverName", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmDriverMaster().getFirstName());
					vehicleSpeedAlerts.put("driverId", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmDriverMaster().getDriverId());
					vehicleSpeedAlerts.put("area", tripsAlerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping()
							.geteFmFmZoneMaster().getZoneName());
					vehicleSpeedAlerts.put("speed", tripsAlerts.getCurrentSpeed());
					vehicleSpeedAlerts.put("facilityName",
							tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
					vehicleSpeedAlertsList.add(vehicleSpeedAlerts);
				}
			}
		} else if (assignRoutePO.getVehicleId() == 0 && (!(assignRoutePO.getVendorId() == 0))) {

			List<EFmFmTripAlertsPO> allSpeedAlerts = iAlertBO.getAllTripAlertsForSelectedDatesByVendor(fromDate, toDate,
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getVendorId());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			if ((!(allSpeedAlerts.isEmpty())) || allSpeedAlerts.size() != 0) {
				for (EFmFmTripAlertsPO tripsAlerts : allSpeedAlerts) {
					Map<String, Object> vehicleSpeedAlerts = new HashMap<String, Object>();
					vehicleSpeedAlerts.put("dateTime", dateFormatter.format(tripsAlerts.getCreationTime()));
					vehicleSpeedAlerts.put("shiftTime",
							timeFormat.format(tripsAlerts.getEfmFmAssignRoute().getShiftTime()));
					vehicleSpeedAlerts.put("vehicleNumber", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmVehicleMaster().getVehicleNumber());
					vehicleSpeedAlerts.put("vendorName", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					vehicleSpeedAlerts.put("driverName", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmDriverMaster().getFirstName());
					vehicleSpeedAlerts.put("driverId", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmDriverMaster().getDriverId());
					vehicleSpeedAlerts.put("area", tripsAlerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping()
							.geteFmFmZoneMaster().getZoneName());
					vehicleSpeedAlerts.put("speed", tripsAlerts.getCurrentSpeed());
					vehicleSpeedAlerts.put("facilityName",
							tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
					vehicleSpeedAlertsList.add(vehicleSpeedAlerts);
				}
			}
		}

		else {
			List<EFmFmTripAlertsPO> allSpeedAlerts = iAlertBO.getAllTripAlertsForSelectedDatesByVehicle(fromDate,
					toDate, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getVehicleId());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			if ((!(allSpeedAlerts.isEmpty())) || allSpeedAlerts.size() != 0) {
				for (EFmFmTripAlertsPO tripsAlerts : allSpeedAlerts) {
					Map<String, Object> vehicleSpeedAlerts = new HashMap<String, Object>();
					vehicleSpeedAlerts.put("dateTime", dateFormatter.format(tripsAlerts.getCreationTime()));
					vehicleSpeedAlerts.put("shiftTime",
							timeFormat.format(tripsAlerts.getEfmFmAssignRoute().getShiftTime()));
					vehicleSpeedAlerts.put("vehicleNumber", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmVehicleMaster().getVehicleNumber());
					vehicleSpeedAlerts.put("vendorName", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					vehicleSpeedAlerts.put("driverName", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmDriverMaster().getFirstName());
					vehicleSpeedAlerts.put("driverId", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmDriverMaster().getDriverId());
					vehicleSpeedAlerts.put("area", tripsAlerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping()
							.geteFmFmZoneMaster().getZoneName());
					vehicleSpeedAlerts.put("speed", tripsAlerts.getCurrentSpeed());
					vehicleSpeedAlerts.put("facilityName",
							tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
					vehicleSpeedAlertsList.add(vehicleSpeedAlerts);
				}
			}

		}
		allTrips.put("tripDetail", vehicleSpeedAlertsList);
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allTrips, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * @param Get
	 *            Get all route details start time and end time details...
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	@POST
	@Path("/routeWiceReport")
	public Response getRouteWiseTravelTime(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
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
				log.info("authenticationToken error"+e);
			}

		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");		
		DateFormat time = new SimpleDateFormat("HH:mm:ss");		
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		List<Map<String, Object>> routeWiseReportList = new ArrayList<Map<String, Object>>();
		List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllTripForTravelTimeReports(fromDate, toDate,
				assignRoutePO.getTripType(), new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		log.info("From Date" + assignRoutePO.getFromDate());
		log.info("From Date" + assignRoutePO.getToDate());
		log.info("total trips size " + allTripDetails.size());
		if(!(allTripDetails.isEmpty())) {
			for (EFmFmAssignRoutePO trips : allTripDetails) {
				Map<String, Object> routeReport = new HashMap<String, Object>();
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO
						.getParticularTripAllEmployees(trips.getAssignRouteId());
				List<Map<String, Object>> routeWiseTravelTime = new ArrayList<Map<String, Object>>();
				if (!(employeeTripDetailPO.isEmpty())) {
					String travellHoursPerEmp = "";
					for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
						long millis=0;;
						Map<String, Object> employeeDetails = new HashMap<String, Object>();
						SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");					
						if (employeeTripDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
							if(employeeTripDetail.getCabRecheddestinationTime()==0){
								travellHoursPerEmp="Not Calculated";//Not calculated								
							}else{
								Date cabRecheddestinationTime=new Date(employeeTripDetail.getCabRecheddestinationTime());							
								Date date1 = format.parse(time.format(cabRecheddestinationTime.getTime()));
								Date date2 = format.parse(time.format(employeeTripDetail.getEfmFmAssignRoute().getTripStartTime().getTime()));							
								//millis = cabRecheddestinationTime.getTime()- employeeTripDetail.getEfmFmAssignRoute().getTripStartTime().getTime();
								millis=date1.getTime()-date2.getTime();
							}							
						} else {							
							Date cabstartFromDestination=new Date(employeeTripDetail.getCabstartFromDestination());
							if(employeeTripDetail.getCabstartFromDestination()==0){
								travellHoursPerEmp="Not Calculated";
							}else{							
								//millis = employeeTripDetail.getEfmFmAssignRoute().getTripCompleteTime().getTime()- cabstartFromDestination.getTime();							
								Date date1 = format.parse(time.format(cabstartFromDestination.getTime()));
								Date date2 = format.parse(time.format(employeeTripDetail.getEfmFmAssignRoute().getTripCompleteTime().getTime()));
								millis=date2.getTime()-date1.getTime();
							}
						}	
						if(!travellHoursPerEmp.equalsIgnoreCase("Not Calculated")){
								travellHoursPerEmp = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
											TimeUnit.MILLISECONDS.toMinutes(millis)
													- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
											TimeUnit.MILLISECONDS.toSeconds(millis)
													- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));	
						}
						employeeDetails.put("employeeId", employeeTripDetail.geteFmFmEmployeeTravelRequest()
								.getEfmFmUserMaster().getEmployeeId());
						employeeDetails
								.put("empName",
										new String(Base64.getDecoder().decode(employeeTripDetail
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
								"utf-8"));
						employeeDetails.put("travelTime", travellHoursPerEmp);
						routeWiseTravelTime.add(employeeDetails);
					}
				}
				routeReport.put("empTravelDetails", routeWiseTravelTime);
				routeReport.put("tripStartDate", dateFormatter.format(trips.getTripStartTime()));
				routeReport.put("tripAssignDate", formatter.format(trips.getTripAssignDate()));
				routeReport.put("tripCompleteDate", dateFormatter.format(trips.getTripCompleteTime()));
				long millis = trips.getTripCompleteTime().getTime() - trips.getTripStartTime().getTime();
				String travellHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
						TimeUnit.MILLISECONDS.toMinutes(millis)
								- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
						TimeUnit.MILLISECONDS.toSeconds(millis)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
				routeReport.put("totalRouteTravelledTime", travellHours);
				routeReport.put("shiftTime", timeFormat.format(trips.getShiftTime()));
				routeReport.put("vehicleNumber",
						trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				routeReport.put("vendorName",
						trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				routeReport.put("driverName", trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				routeReport.put("driverId", trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				routeReport.put("routeName", trips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				routeReport.put("facilityName",
						trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				routeWiseReportList.add(routeReport);
			}
		}
		allTrips.put("tripDetail", routeWiseReportList);
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allTrips, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * @param Get
	 *            Get particular employeeWisereport details by employee id and
	 *            trip type...
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/employeeWiseReport")
	public Response employeeWisereport(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
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
				log.info("authenticationToken error"+e);
			}
	
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		List<EFmFmEmployeeTripDetailPO> employeeDeatis = assignRouteBO
				.getParticuarEmployeesTravelledDetailByEmployeeIdAndBranchId(fromDate, toDate,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getEmployeeId());
		List<Map<String, Object>> routeWiseReportList = new ArrayList<Map<String, Object>>();
		log.info("employeeDeatil" + employeeDeatis);
		if (!(employeeDeatis.isEmpty())) {
			for (EFmFmEmployeeTripDetailPO empDetail : employeeDeatis) {
				Map<String, Object> routeReport = new HashMap<String, Object>();
				routeReport.put("assignDate", dateFormatter.format(empDetail.getActualTime()));
				routeReport.put("shiftTime", shiftFormate.format(empDetail.getEfmFmAssignRoute().getShiftTime()));
				routeReport.put("tripType", empDetail.getEfmFmAssignRoute().getTripType());
				routeReport.put("vehicleNumber", empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
						.getEfmFmVehicleMaster().getVehicleNumber());
				routeReport.put("driverName",
						empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				routeReport.put("driverNumber", empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
						.getEfmFmDriverMaster().getMobileNumber());
				routeReport.put("routeName",
						empDetail.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				routeReport.put("facilityName",
						empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

				if (empDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
					try {
						routeReport.put("plannedPickUpTime", empDetail.geteFmFmEmployeeTravelRequest().getPickUpTime());
						if (!(empDetail.getCabstartFromDestination() == 0)
								|| !(empDetail.getPickedUpDateAndTime() == 0)) {
							if (!(empDetail.getCabstartFromDestination() == 0)
									&& (empDetail.getBoardedFlg().equalsIgnoreCase("NO"))) {
								routeReport.put("boardingTime",
										dateFormatter.format(new Date(empDetail.getCabstartFromDestination())));
							}
							if (!(empDetail.getPickedUpDateAndTime() == 0)
									&& (empDetail.getBoardedFlg().equalsIgnoreCase("B"))) {
								routeReport.put("boardingTime",
										dateFormatter.format(new Date(empDetail.getPickedUpDateAndTime())));
							}
						} else {
							routeReport.put("boardingTime", "0");

						}
					} catch (Exception e) {
						routeReport.put("boardingTime", "0");
						routeReport.put("boardingTime", "0");
						log.info("Error in drop type" + e);
					}
				}
				if (empDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
					try {
						if (!(empDetail.getCabstartFromDestination() == 0)
								|| !(empDetail.getPickedUpDateAndTime() == 0)) {
							if (!(empDetail.getCabstartFromDestination() == 0)
									&& (empDetail.getBoardedFlg().equalsIgnoreCase("NO"))) {
								routeReport.put("boardingTime",
										dateFormatter.format(new Date(empDetail.getCabstartFromDestination())));
							}
							if (!(empDetail.getPickedUpDateAndTime() == 0)
									&& (empDetail.getBoardedFlg().equalsIgnoreCase("D"))) {
								routeReport.put("boardingTime",
										dateFormatter.format(new Date(empDetail.getPickedUpDateAndTime())));
							}
						} else {
							routeReport.put("boardingTime", "0");

						}
					} catch (Exception e) {
						routeReport.put("boardingTime", "0");
						log.info("Error in drop type" + e);
					}
				}
				try {
					if (!(empDetail.getCabRecheddestinationTime() == 0)) {
						routeReport.put("cabReachedTime",
								dateFormatter.format(new Date(empDetail.getCabRecheddestinationTime())));
					} else {
						routeReport.put("cabReachedTime", "0");

					}

				} catch (Exception e) {
					log.info("error in cabReachedTime" + e);
					routeReport.put("cabReachedTime", "0");
				}
				// routeReport.put("boardingTime",
				// dateFormatter.format(empDetail.getCabstartFromDestination()));

				try {
					routeReport.put("vehicleReachTime",
							dateFormatter.format(empDetail.getReachedMessageDeliveryDate()));
				} catch (Exception e) {
					routeReport.put("vehicleReachTime", "NO");
					log.info("error in vehicleReachTime" + e);

				}
				try {
					routeReport.put("routeCloseTime",
							dateFormatter.format(empDetail.getEfmFmAssignRoute().getAllocationMsgDeliveryDate()));
				} catch (Exception e) {
					routeReport.put("routeCloseTime", "Manual");
					log.info("error in routeCloseTime" + e);
				}
				routeReport.put("tripStartTime",
						dateFormatter.format(empDetail.getEfmFmAssignRoute().getTripStartTime()));
				routeReport.put("tripEndTime",
						dateFormatter.format(empDetail.getEfmFmAssignRoute().getTripCompleteTime()));
				if (empDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
					if (empDetail.getBoardedFlg().equalsIgnoreCase("D")) {
						routeReport.put("boardingStatus", "Dropped");
					} else if (empDetail.getBoardedFlg().equalsIgnoreCase("NO")) {
						routeReport.put("boardingStatus", "No Show");
					} else {
						routeReport.put("boardingStatus", "Yet to dropped");
					}

				} else if (empDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
					if (empDetail.getBoardedFlg().equalsIgnoreCase("B")) {
						routeReport.put("boardingStatus", "PickedUp");
					} else if (empDetail.getBoardedFlg().equalsIgnoreCase("NO")) {
						routeReport.put("boardingStatus", "No Show");
					} else {
						routeReport.put("boardingStatus", "Yet to picked up");
					}
				}
				routeWiseReportList.add(routeReport);
			}
		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(routeWiseReportList, MediaType.APPLICATION_JSON).build();
	}
	
	
	
	
	/**
	 * @param Get
	 *            Get particular femaleEmployeeWiseReport details by selecting date and
	 *            trip type...
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/femaleEmployeeWiseReport")
	public Response femaleEmployeeWisereport(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
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
				log.info("authenticationToken error"+e);
			}
	
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		List<EFmFmEmployeeTripDetailPO> employeeDeatis = assignRouteBO
				.getAllFemaleEmployeesTravelledDetailByDate(fromDate, toDate);
		List<Map<String, Object>> routeWiseReportList = new ArrayList<Map<String, Object>>();
		log.info("employeeDeatil" + employeeDeatis.size());
		if (!(employeeDeatis.isEmpty())) {
			for (EFmFmEmployeeTripDetailPO empDetail : employeeDeatis) {
				if("female".equalsIgnoreCase(new String(Base64.getDecoder().decode
						(empDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender())))){
				
				Map<String, Object> routeReport = new HashMap<String, Object>();
				routeReport.put("employeeId", empDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
				routeReport.put("empName", new String(Base64.getDecoder().decode(empDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName())));
				routeReport.put("empNumber", new String(Base64.getDecoder().decode(empDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber())));

				routeReport.put("assignDate", dateFormatter.format(empDetail.getActualTime()));
				routeReport.put("shiftTime", shiftFormate.format(empDetail.getEfmFmAssignRoute().getShiftTime()));
				routeReport.put("tripType", empDetail.getEfmFmAssignRoute().getTripType());
				routeReport.put("vehicleNumber", empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
						.getEfmFmVehicleMaster().getVehicleNumber());
				routeReport.put("driverName",
						empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				routeReport.put("driverNumber", empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
						.getEfmFmDriverMaster().getMobileNumber());
				routeReport.put("routeName",
						empDetail.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				routeReport.put("facilityName",
						empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

				if (empDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
					try {
						routeReport.put("plannedPickUpTime", empDetail.geteFmFmEmployeeTravelRequest().getPickUpTime());
						if (!(empDetail.getCabstartFromDestination() == 0)
								|| !(empDetail.getPickedUpDateAndTime() == 0)) {
							if (!(empDetail.getCabstartFromDestination() == 0)
									&& (empDetail.getBoardedFlg().equalsIgnoreCase("NO"))) {
								routeReport.put("boardingTime",
										dateFormatter.format(new Date(empDetail.getCabstartFromDestination())));
							}
							if (!(empDetail.getPickedUpDateAndTime() == 0)
									&& (empDetail.getBoardedFlg().equalsIgnoreCase("B"))) {
								routeReport.put("boardingTime",
										dateFormatter.format(new Date(empDetail.getPickedUpDateAndTime())));
							}
						} else {
							routeReport.put("boardingTime", "0");

						}
					} catch (Exception e) {
						routeReport.put("boardingTime", "0");
						routeReport.put("boardingTime", "0");
						log.info("Error in drop type" + e);
					}
				}
				if (empDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
					try {
						if (!(empDetail.getCabstartFromDestination() == 0)
								|| !(empDetail.getPickedUpDateAndTime() == 0)) {
							if (!(empDetail.getCabstartFromDestination() == 0)
									&& (empDetail.getBoardedFlg().equalsIgnoreCase("NO"))) {
								routeReport.put("boardingTime",
										dateFormatter.format(new Date(empDetail.getCabstartFromDestination())));
							}
							if (!(empDetail.getPickedUpDateAndTime() == 0)
									&& (empDetail.getBoardedFlg().equalsIgnoreCase("D"))) {
								routeReport.put("boardingTime",
										dateFormatter.format(new Date(empDetail.getPickedUpDateAndTime())));
							}
						} else {
							routeReport.put("boardingTime", "0");

						}
					} catch (Exception e) {
						routeReport.put("boardingTime", "0");
						log.info("Error in drop type" + e);
					}
				}
				try {
					if (!(empDetail.getCabRecheddestinationTime() == 0)) {
						routeReport.put("cabReachedTime",
								dateFormatter.format(new Date(empDetail.getCabRecheddestinationTime())));
					} else {
						routeReport.put("cabReachedTime", "0");

					}

				} catch (Exception e) {
					log.info("error in cabReachedTime" + e);
					routeReport.put("cabReachedTime", "0");
				}
				// routeReport.put("boardingTime",
				// dateFormatter.format(empDetail.getCabstartFromDestination()));

				try {
					routeReport.put("vehicleReachTime",
							dateFormatter.format(empDetail.getReachedMessageDeliveryDate()));
				} catch (Exception e) {
					routeReport.put("vehicleReachTime", "NO");
					log.info("error in vehicleReachTime" + e);

				}
				try {
					routeReport.put("routeCloseTime",
							dateFormatter.format(empDetail.getEfmFmAssignRoute().getAllocationMsgDeliveryDate()));
				} catch (Exception e) {
					routeReport.put("routeCloseTime", "Manual");
					log.info("error in routeCloseTime" + e);
				}
				routeReport.put("tripStartTime",
						dateFormatter.format(empDetail.getEfmFmAssignRoute().getTripStartTime()));
				routeReport.put("tripEndTime",
						dateFormatter.format(empDetail.getEfmFmAssignRoute().getTripCompleteTime()));
				if (empDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
					if (empDetail.getBoardedFlg().equalsIgnoreCase("D")) {
						routeReport.put("boardingStatus", "Dropped");
					} else if (empDetail.getBoardedFlg().equalsIgnoreCase("NO")) {
						routeReport.put("boardingStatus", "No Show");
					} else {
						routeReport.put("boardingStatus", "Yet to dropped");
					}

				} else if (empDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
					if (empDetail.getBoardedFlg().equalsIgnoreCase("B")) {
						routeReport.put("boardingStatus", "PickedUp");
					} else if (empDetail.getBoardedFlg().equalsIgnoreCase("NO")) {
						routeReport.put("boardingStatus", "No Show");
					} else {
						routeReport.put("boardingStatus", "Yet to picked up");
					}
				}
				routeWiseReportList.add(routeReport);
				
			 }
			}
		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(routeWiseReportList, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * @param Get
	 *            Get all getSpeedAlertVehicleAndVendorWise details method will
	 *            return speed alert of the selected vehicle...
	 * 
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/speedReportDownload")
	public Response getSpeedAlertVehicleAndVendorWiseDownload(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		 log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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
		 		log.info("authenticationToken error"+e);
		 	}
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
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
        int rownum = 0, noOfRoute = 0;
        Row OutSiderow = sheet.createRow(rownum++); 
        for (int columnIndex = 0; columnIndex < 15; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
				
				
				Cell zerothCol = OutSiderow.createCell(0);
                zerothCol.setCellValue("DateTime");
                zerothCol.setCellStyle(style);
				
				Cell firstCol = OutSiderow.createCell(1);
                firstCol.setCellValue("Vendor Name");
                firstCol.setCellStyle(style);
				
				Cell secondCol = OutSiderow.createCell(2);
                secondCol.setCellValue("Driver Id");
                secondCol.setCellStyle(style);
				
				Cell thirdCol = OutSiderow.createCell(3);
                thirdCol.setCellValue("Driver Name");
                thirdCol.setCellStyle(style);
				
				Cell fourthCol = OutSiderow.createCell(4);
                fourthCol.setCellValue("Vehicle Number");
                fourthCol.setCellStyle(style);
				
				Cell fifthCol = OutSiderow.createCell(5);
                fifthCol.setCellValue("Route Name");
                fifthCol.setCellStyle(style);
				
				Cell sixthCol = OutSiderow.createCell(6);
                sixthCol.setCellValue("Shift Time");
                sixthCol.setCellStyle(style);
				
				
				Cell seventhCol = OutSiderow.createCell(7);
                seventhCol.setCellValue("Speed");
                seventhCol.setCellStyle(style);
                
                Cell facilityName= OutSiderow.createCell(8);
        		facilityName.setCellValue("Facility Name");
        		facilityName.setCellStyle(style);
                
		Date fromDate = (Date) formatter.parse(assignRoutePO.getFromDate());
		Date toDate = (Date) formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		List<Map<String, Object>> vehicleSpeedAlertsList = new ArrayList<Map<String, Object>>();
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		log.info("assignRoutePO.getVehicleId()" + assignRoutePO.getVehicleId());
		log.info("assignRoutePO.getVendorId()" + assignRoutePO.getVendorId());
		if (assignRoutePO.getVehicleId() == 0 && assignRoutePO.getVendorId() == 0) {
			List<EFmFmTripAlertsPO> allSpeedAlerts = iAlertBO.getAllTripAlertsForSelectedDates(fromDate, toDate,
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			if ((!(allSpeedAlerts.isEmpty())) || allSpeedAlerts.size() != 0) {
				for (EFmFmTripAlertsPO tripsAlerts : allSpeedAlerts) {
					OutSiderow = sheet.createRow(rownum++);
					Cell dateTimeCell = OutSiderow.createCell(0);
					Cell vendorNameCell = OutSiderow.createCell(1);
					Cell driverIdCell = OutSiderow.createCell(2);
					Cell driverNameCell = OutSiderow.createCell(3);
					Cell vehicleNumberCell = OutSiderow.createCell(4);
					Cell routeNameCell = OutSiderow.createCell(5);
					Cell shiftTimeCell = OutSiderow.createCell(6);
					Cell speedCell = OutSiderow.createCell(7);
					Map<String, Object> vehicleSpeedAlerts = new HashMap<String, Object>();
					vehicleSpeedAlerts.put("dateTime", dateFormatter.format(tripsAlerts.getCreationTime()));
					vehicleSpeedAlerts.put("shiftTime",timeFormat.format(tripsAlerts.getEfmFmAssignRoute().getShiftTime()));
					vehicleSpeedAlerts.put("vehicleNumber", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
					vehicleSpeedAlerts.put("vendorName", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					vehicleSpeedAlerts.put("driverName", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
					vehicleSpeedAlerts.put("driverId", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
					vehicleSpeedAlerts.put("area", tripsAlerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					vehicleSpeedAlerts.put("speed", tripsAlerts.getCurrentSpeed());
					
					Cell facilityNameCell = OutSiderow.createCell(8);
					facilityNameCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
					
					dateTimeCell.setCellValue(dateFormatter.format(tripsAlerts.getCreationTime()));
					vendorNameCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					driverIdCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
					driverNameCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
					vehicleNumberCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
					routeNameCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					shiftTimeCell.setCellValue(timeFormat.format(tripsAlerts.getEfmFmAssignRoute().getShiftTime()));
					speedCell.setCellValue(tripsAlerts.getCurrentSpeed());
									
					vehicleSpeedAlertsList.add(vehicleSpeedAlerts);
				}
			}
		} else if (assignRoutePO.getVehicleId() == 0 && (!(assignRoutePO.getVendorId() == 0))) {

			List<EFmFmTripAlertsPO> allSpeedAlerts = iAlertBO.getAllTripAlertsForSelectedDatesByVendor(fromDate, toDate,
					new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getVendorId());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			if ((!(allSpeedAlerts.isEmpty())) || allSpeedAlerts.size() != 0) {
				for (EFmFmTripAlertsPO tripsAlerts : allSpeedAlerts) {
					OutSiderow = sheet.createRow(rownum++);
					Cell dateTimeCell = OutSiderow.createCell(0);
					Cell vendorNameCell = OutSiderow.createCell(1);
					Cell driverIdCell = OutSiderow.createCell(2);
					Cell driverNameCell = OutSiderow.createCell(3);
					Cell vehicleNumberCell = OutSiderow.createCell(4);
					Cell routeNameCell = OutSiderow.createCell(5);
					Cell shiftTimeCell = OutSiderow.createCell(6);
					Cell speedCell = OutSiderow.createCell(7);
					Map<String, Object> vehicleSpeedAlerts = new HashMap<String, Object>();
					vehicleSpeedAlerts.put("dateTime", dateFormatter.format(tripsAlerts.getCreationTime()));
					vehicleSpeedAlerts.put("shiftTime",
							timeFormat.format(tripsAlerts.getEfmFmAssignRoute().getShiftTime()));
					vehicleSpeedAlerts.put("vehicleNumber", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmVehicleMaster().getVehicleNumber());
					vehicleSpeedAlerts.put("vendorName", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					vehicleSpeedAlerts.put("driverName", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmDriverMaster().getFirstName());
					vehicleSpeedAlerts.put("driverId", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmDriverMaster().getDriverId());
					vehicleSpeedAlerts.put("area", tripsAlerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping()
							.geteFmFmZoneMaster().getZoneName());
					vehicleSpeedAlerts.put("speed", tripsAlerts.getCurrentSpeed());
					Cell facilityNameCell = OutSiderow.createCell(8);
					facilityNameCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
					vehicleSpeedAlertsList.add(vehicleSpeedAlerts);
					dateTimeCell.setCellValue(dateFormatter.format(tripsAlerts.getCreationTime()));
					vendorNameCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					driverIdCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
					driverNameCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
					vehicleNumberCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
					routeNameCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					shiftTimeCell.setCellValue(timeFormat.format(tripsAlerts.getEfmFmAssignRoute().getShiftTime()));
					speedCell.setCellValue(tripsAlerts.getCurrentSpeed());
				}
			}
		}

		else {
			List<EFmFmTripAlertsPO> allSpeedAlerts = iAlertBO.getAllTripAlertsForSelectedDatesByVehicle(fromDate,
					toDate, new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getVehicleId());
			log.info("From Date" + assignRoutePO.getFromDate());
			log.info("From Date" + assignRoutePO.getToDate());
			if ((!(allSpeedAlerts.isEmpty())) || allSpeedAlerts.size() != 0) {
				for (EFmFmTripAlertsPO tripsAlerts : allSpeedAlerts) {
					OutSiderow = sheet.createRow(rownum++);
					Cell dateTimeCell = OutSiderow.createCell(0);
					Cell vendorNameCell = OutSiderow.createCell(1);
					Cell driverIdCell = OutSiderow.createCell(2);
					Cell driverNameCell = OutSiderow.createCell(3);
					Cell vehicleNumberCell = OutSiderow.createCell(4);
					Cell routeNameCell = OutSiderow.createCell(5);
					Cell shiftTimeCell = OutSiderow.createCell(6);
					Cell speedCell = OutSiderow.createCell(7);
					Map<String, Object> vehicleSpeedAlerts = new HashMap<String, Object>();
					vehicleSpeedAlerts.put("dateTime", dateFormatter.format(tripsAlerts.getCreationTime()));
					vehicleSpeedAlerts.put("shiftTime",
							timeFormat.format(tripsAlerts.getEfmFmAssignRoute().getShiftTime()));
					vehicleSpeedAlerts.put("vehicleNumber", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmVehicleMaster().getVehicleNumber());
					vehicleSpeedAlerts.put("vendorName", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					vehicleSpeedAlerts.put("driverName", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmDriverMaster().getFirstName());
					vehicleSpeedAlerts.put("driverId", tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
							.getEfmFmDriverMaster().getDriverId());
					vehicleSpeedAlerts.put("area", tripsAlerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping()
							.geteFmFmZoneMaster().getZoneName());
					vehicleSpeedAlerts.put("speed", tripsAlerts.getCurrentSpeed());
					Cell facilityNameCell = OutSiderow.createCell(8);
					facilityNameCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
					vehicleSpeedAlertsList.add(vehicleSpeedAlerts);
					dateTimeCell.setCellValue(dateFormatter.format(tripsAlerts.getCreationTime()));
					vendorNameCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
					driverIdCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
					driverNameCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
					vehicleNumberCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
					routeNameCell.setCellValue(tripsAlerts.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					shiftTimeCell.setCellValue(timeFormat.format(tripsAlerts.getEfmFmAssignRoute().getShiftTime()));
					speedCell.setCellValue(tripsAlerts.getCurrentSpeed());
				}
			}

		}
		allTrips.put("tripDetail", vehicleSpeedAlertsList);
					StreamingOutput streamOutput = new StreamingOutput() {
						@Override
						public void write(OutputStream out) throws IOException, WebApplicationException {
							workbook.write(out);
						}			
					};
					ResponseBuilder response = Response.ok(streamOutput,
							"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					response.header("content-disposition", "attachment; filename=\"Report-" + 1 + "\".xlsx");
					 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
					return response.build();	
			}

	/**
	 * @param Get
	 *            Get all route details start time and end time details...
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	@POST
	@Path("/routeWiceReportDownload")
	public Response getRouteWiseTravelTimeDownload(EFmFmAssignRoutePO assignRoutePO)
			throws ParseException, UnsupportedEncodingException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
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
				log.info("authenticationToken error"+e);
			}
		
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		DateFormat time = new SimpleDateFormat("HH:mm:ss");	
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
		int rownum = 0, noOfRoute = 0;
		Row OutSiderow = sheet.createRow(rownum++); 
		for (int columnIndex = 0; columnIndex < 15; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
		}
				
				
				Cell zerothCol = OutSiderow.createCell(0);
				zerothCol.setCellValue("Travelled Date");
				zerothCol.setCellStyle(style);
				
				Cell firstCol = OutSiderow.createCell(1);
				firstCol.setCellValue("Vendor Name");
				firstCol.setCellStyle(style);
				
				Cell secondCol = OutSiderow.createCell(2);
				secondCol.setCellValue("Vehicle Number");
				secondCol.setCellStyle(style);
				
				Cell thirdCol = OutSiderow.createCell(3);
				thirdCol.setCellValue("Driver Id");
				thirdCol.setCellStyle(style);
				
				Cell fourthCol = OutSiderow.createCell(4);
				fourthCol.setCellValue("Driver name");
				fourthCol.setCellStyle(style);
				
				Cell fifthCol = OutSiderow.createCell(5);
				fifthCol.setCellValue("Shift Time");
				fifthCol.setCellStyle(style);
				
				Cell sixthCol = OutSiderow.createCell(6);
				sixthCol.setCellValue("Employee Detail");
				sixthCol.setCellStyle(style);
				
				
				Cell seventhCol = OutSiderow.createCell(7);
				seventhCol.setCellValue("Route Name");
				seventhCol.setCellStyle(style);
				
				Cell eighthCol = OutSiderow.createCell(8);
				eighthCol.setCellValue("Start time");
				eighthCol.setCellStyle(style);
				
				Cell ninethCol = OutSiderow.createCell(9);
				ninethCol.setCellValue("End time");
				ninethCol.setCellStyle(style);
				
				Cell tenthCol = OutSiderow.createCell(10);
				tenthCol.setCellValue("Total travel time");
				tenthCol.setCellStyle(style);
				
				Cell facilityName= OutSiderow.createCell(11);
				facilityName.setCellValue("Facility Name");
				facilityName.setCellStyle(style);
				
		Date fromDate = (Date) formatter.parse(assignRoutePO.getFromDate());
		Date toDate = (Date) formatter.parse(assignRoutePO.getToDate());
		Map<String, Object> allTrips = new HashMap<String, Object>();
		List<Map<String, Object>> routeWiseReportList = new ArrayList<Map<String, Object>>();
		List<EFmFmAssignRoutePO> allTripDetails = iAssignRouteBO.getAllTripForTravelTimeReports(fromDate, toDate,
				assignRoutePO.getTripType(), new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		log.info("From Date" + assignRoutePO.getFromDate());
		log.info("From Date" + assignRoutePO.getToDate());
		log.info("total trips size " + allTripDetails.size());
		boolean firstRow=false;
		if ((!(allTripDetails.isEmpty())) || allTripDetails.size() != 0) {
			
			for (EFmFmAssignRoutePO trips : allTripDetails) {
				firstRow=false;
				OutSiderow = sheet.createRow(rownum++);
				Map<String, Object> routeReport = new HashMap<String, Object>();
				routeReport.put("tripStartDate", dateFormatter.format(trips.getTripStartTime()));				
				Cell tripStartDateCell = OutSiderow.createCell(8);
				tripStartDateCell.setCellValue(dateFormatter.format(trips.getTripStartTime()));
				routeReport.put("tripAssignDate", formatter.format(trips.getTripAssignDate()));
				Cell tripAssignDateCell = OutSiderow.createCell(0);
				tripAssignDateCell.setCellValue(formatter.format(trips.getTripAssignDate()));
				routeReport.put("tripCompleteDate", dateFormatter.format(trips.getTripCompleteTime()));
				Cell tripCompleteDateCell = OutSiderow.createCell(9);
				tripCompleteDateCell.setCellValue(dateFormatter.format(trips.getTripCompleteTime()));
				long millisTravel = trips.getTripCompleteTime().getTime() - trips.getTripStartTime().getTime();
				String travellHours = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisTravel),
						TimeUnit.MILLISECONDS.toMinutes(millisTravel)
								- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisTravel)),
						TimeUnit.MILLISECONDS.toSeconds(millisTravel)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisTravel)));
				routeReport.put("totalRouteTravelledTime", travellHours);
				Cell totalRouteTravelledTimeCell = OutSiderow.createCell(10);
				totalRouteTravelledTimeCell.setCellValue(travellHours);
				routeReport.put("shiftTime", timeFormat.format(trips.getShiftTime()));
				Cell shiftTimeCell = OutSiderow.createCell(5);
				shiftTimeCell.setCellValue(timeFormat.format(trips.getShiftTime()));
				routeReport.put("vehicleNumber",trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				Cell vehicleNumberCell = OutSiderow.createCell(2);
				vehicleNumberCell.setCellValue(trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				routeReport.put("vendorName",trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				Cell vendorNameCell = OutSiderow.createCell(1);
				vendorNameCell.setCellValue(trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				routeReport.put("driverName", trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				Cell driverNameCell = OutSiderow.createCell(4);
				driverNameCell.setCellValue(trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				routeReport.put("driverId", trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				Cell driverIdCell = OutSiderow.createCell(3);
				driverIdCell.setCellValue(trips.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				routeReport.put("routeName", trips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				Cell facilityNameCell = OutSiderow.createCell(11);
				facilityNameCell.setCellValue(trips.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				Cell routeNameCell = OutSiderow.createCell(7);
				routeNameCell.setCellValue(trips.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployees(trips.getAssignRouteId());				
				List<Map<String, Object>> routeWiseTravelTime = new ArrayList<Map<String, Object>>();
				if (!(employeeTripDetailPO.isEmpty())) {
					String travellHoursPerEmp = "";					
					for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
						long millis = 0;					
						if(firstRow){
							OutSiderow = sheet.createRow(rownum++);
						}
						Cell employeeDetailsCell = OutSiderow.createCell(6);											
						Map<String, Object> employeeDetails = new HashMap<String, Object>();
						
						SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");					
						if (employeeTripDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
							if(employeeTripDetail.getCabRecheddestinationTime()==0){
								travellHoursPerEmp="Not Calculated";
							}else{
								Date cabRecheddestinationTime=new Date(employeeTripDetail.getCabRecheddestinationTime());							
								Date date1 = format.parse(time.format(cabRecheddestinationTime.getTime()));
								Date date2 = format.parse(time.format(employeeTripDetail.getEfmFmAssignRoute().getTripStartTime().getTime()));							
								//millis = cabRecheddestinationTime.getTime()- employeeTripDetail.getEfmFmAssignRoute().getTripStartTime().getTime();
								millis=date1.getTime()-date2.getTime();
							}
							
						} else {
							
							if(employeeTripDetail.getCabstartFromDestination()==0){
								travellHoursPerEmp="Not Calculated";
							}else{
								Date cabstartFromDestination=new Date(employeeTripDetail.getCabstartFromDestination());
								//millis = employeeTripDetail.getEfmFmAssignRoute().getTripCompleteTime().getTime()- cabstartFromDestination.getTime();							
								Date date1 = format.parse(time.format(cabstartFromDestination.getTime()));
								Date date2 = format.parse(time.format(employeeTripDetail.getEfmFmAssignRoute().getTripCompleteTime().getTime()));
								millis=date2.getTime()-date1.getTime();
							}
						}
						if(!travellHoursPerEmp.equalsIgnoreCase("Not Calculated")){
							travellHoursPerEmp = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
									TimeUnit.MILLISECONDS.toMinutes(millis)
											- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
									TimeUnit.MILLISECONDS.toSeconds(millis)
											- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));	
						}
						
						employeeDetails.put("employeeId", employeeTripDetail.geteFmFmEmployeeTravelRequest()
								.getEfmFmUserMaster().getEmployeeId());
						employeeDetails
								.put("empName",
										new String(Base64.getDecoder().decode(employeeTripDetail
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
								"utf-8"));
						employeeDetails.put("travelTime", travellHoursPerEmp);
						employeeDetailsCell.setCellValue(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId()
								+"-"+new String(Base64.getDecoder().decode(employeeTripDetail
										.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()))+"-"+travellHoursPerEmp);						
						routeWiseTravelTime.add(employeeDetails);
						firstRow=true;					
					}
				}
				routeReport.put("empTravelDetails", routeWiseTravelTime);			
				routeWiseReportList.add(routeReport);
			}
		}
		allTrips.put("tripDetail", routeWiseReportList);	
		StreamingOutput streamOutput = new StreamingOutput() {
			@Override
			public void write(OutputStream out) throws IOException, WebApplicationException {
				workbook.write(out);
			}			
		};
		ResponseBuilder response = Response.ok(streamOutput,
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.header("content-disposition", "attachment; filename=\"Report-" + 1 + "\".xlsx");
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return response.build();   
		
	}

	/**
	 * @param Get
	 *            Get particular employeeWisereport details by employee id and
	 *            trip type...
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/employeeWiseReportDownload")
	public Response employeeWisereportDownload(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
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
				log.info("authenticationToken error"+e);
			}

		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
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
		int rownum = 0, noOfRoute = 0;
		Row OutSiderow = sheet.createRow(rownum++); 
		for (int columnIndex = 0; columnIndex < 15; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
		}		
				
				Cell zerothCol = OutSiderow.createCell(0);
				zerothCol.setCellValue("Assign Date");
				zerothCol.setCellStyle(style);
				
				Cell secondCol = OutSiderow.createCell(1);
				secondCol.setCellValue("Route Close Time");
				secondCol.setCellStyle(style);
				
				Cell firstCol = OutSiderow.createCell(2);
				firstCol.setCellValue("Trip Start Time");
				firstCol.setCellStyle(style);			
				
				Cell thirdCol = OutSiderow.createCell(3);
				thirdCol.setCellValue("Cab Reached Time");
				thirdCol.setCellStyle(style);
				
				Cell fourthCol = OutSiderow.createCell(4);
				fourthCol.setCellValue("Boarding Time");
				fourthCol.setCellStyle(style);
				
				Cell fifthCol = OutSiderow.createCell(5);
				fifthCol.setCellValue("Trip End Time");
				fifthCol.setCellStyle(style);
				
				Cell sixthCol = OutSiderow.createCell(6);
				sixthCol.setCellValue("PickUp Time");
				sixthCol.setCellStyle(style);
				
				
				Cell seventhCol = OutSiderow.createCell(7);
				seventhCol.setCellValue("Shift Time");
				seventhCol.setCellStyle(style);
				
				Cell eighthCol = OutSiderow.createCell(8);
				eighthCol.setCellValue("Trip Type");
				eighthCol.setCellStyle(style);
				
				Cell ninethCol = OutSiderow.createCell(9);
				ninethCol.setCellValue("Driver Name");
				ninethCol.setCellStyle(style);
				
				Cell tenthCol = OutSiderow.createCell(10);
				tenthCol.setCellValue("Driver Number");
				tenthCol.setCellStyle(style);
				
				Cell eleventhCol = OutSiderow.createCell(11);
				eleventhCol.setCellValue("Vehicle Number");
				eleventhCol.setCellStyle(style);
				
				Cell twelthCol = OutSiderow.createCell(12);
				twelthCol.setCellValue("Route Name");
				twelthCol.setCellStyle(style);
				
				Cell thirteenCol = OutSiderow.createCell(13);
				thirteenCol.setCellValue("Boarding Status");
				thirteenCol.setCellStyle(style);
				
				Cell facilityName= OutSiderow.createCell(14);
				facilityName.setCellValue("Facility Name");
				facilityName.setCellStyle(style);
				
		Date fromDate = (Date) formatter.parse(assignRoutePO.getFromDate());
		Date toDate = (Date) formatter.parse(assignRoutePO.getToDate());
		List<EFmFmEmployeeTripDetailPO> employeeDeatis = assignRouteBO
				.getParticuarEmployeesTravelledDetailByEmployeeIdAndBranchId(fromDate, toDate,
						new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()), assignRoutePO.getEmployeeId());
		List<Map<String, Object>> routeWiseReportList = new ArrayList<Map<String, Object>>();
		log.info("employeeDeatil" + employeeDeatis);
		if (!(employeeDeatis.isEmpty())) {
			for (EFmFmEmployeeTripDetailPO empDetail : employeeDeatis) {
				OutSiderow = sheet.createRow(rownum++);
				Map<String, Object> routeReport = new HashMap<String, Object>();
				
				routeReport.put("assignDate", dateFormatter.format(empDetail.getActualTime()));
				Cell assignDateCell = OutSiderow.createCell(0);
				assignDateCell.setCellValue(dateFormatter.format(empDetail.getActualTime()));
				routeReport.put("shiftTime", shiftFormate.format(empDetail.getEfmFmAssignRoute().getShiftTime()));
				
				Cell shiftTimeCell = OutSiderow.createCell(7);
				shiftTimeCell.setCellValue(shiftFormate.format(empDetail.getEfmFmAssignRoute().getShiftTime()));
				
				routeReport.put("tripType", empDetail.getEfmFmAssignRoute().getTripType());
				
				Cell tripTypeCell = OutSiderow.createCell(8);
				tripTypeCell.setCellValue(empDetail.getEfmFmAssignRoute().getTripType());
				
				routeReport.put("vehicleNumber", empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				Cell vehicleNumberCell = OutSiderow.createCell(11);
				vehicleNumberCell.setCellValue(empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				routeReport.put("driverName",empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				
				Cell driverNameCell = OutSiderow.createCell(9);
				driverNameCell.setCellValue(empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				
				routeReport.put("driverNumber", empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				Cell driverNumberCell = OutSiderow.createCell(10);
				
				Cell facilityNameCell = OutSiderow.createCell(14);
				facilityNameCell.setCellValue(empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				driverNumberCell.setCellValue(empDetail.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				routeReport.put("routeName",empDetail.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				
				Cell routeNameCell = OutSiderow.createCell(12);
				routeNameCell.setCellValue(empDetail.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				Cell plannedPickUpTimeCell = OutSiderow.createCell(6);
				Cell boardingTimeCell = OutSiderow.createCell(4);
				if (empDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
					try {
						routeReport.put("plannedPickUpTime", empDetail.geteFmFmEmployeeTravelRequest().getPickUpTime());
						plannedPickUpTimeCell.setCellValue(shiftFormate.format(empDetail.geteFmFmEmployeeTravelRequest().getPickUpTime()));
						if (!(empDetail.getCabstartFromDestination() == 0)
								|| !(empDetail.getPickedUpDateAndTime() == 0)) {
							if (!(empDetail.getCabstartFromDestination() == 0)
									&& (empDetail.getBoardedFlg().equalsIgnoreCase("NO"))) {
								routeReport.put("boardingTime",dateFormatter.format(new Date(empDetail.getCabstartFromDestination())));
								boardingTimeCell.setCellValue(dateFormatter.format(new Date(empDetail.getCabstartFromDestination())));
							}
							if (!(empDetail.getPickedUpDateAndTime() == 0)
									&& (empDetail.getBoardedFlg().equalsIgnoreCase("B"))) {
								routeReport.put("boardingTime",
										dateFormatter.format(new Date(empDetail.getPickedUpDateAndTime())));
								boardingTimeCell.setCellValue(dateFormatter.format(new Date(empDetail.getPickedUpDateAndTime())));
							}
						} else {
							routeReport.put("boardingTime", "0");
							boardingTimeCell.setCellValue("0");

						}
					} catch (Exception e) {
						routeReport.put("boardingTime", "0");
						boardingTimeCell.setCellValue("0");
						routeReport.put("boardingTime", "0");
						log.info("Error in drop type" + e);
					}
				}
				if (empDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
					
					try {						
						if (!(empDetail.getCabstartFromDestination() == 0)
								|| !(empDetail.getPickedUpDateAndTime() == 0)) {
							if (!(empDetail.getCabstartFromDestination() == 0)
									&& (empDetail.getBoardedFlg().equalsIgnoreCase("NO"))) {
								routeReport.put("boardingTime",dateFormatter.format(new Date(empDetail.getCabstartFromDestination())));
								boardingTimeCell.setCellValue(dateFormatter.format(new Date(empDetail.getCabstartFromDestination())));
							}
							if (!(empDetail.getPickedUpDateAndTime() == 0)
									&& (empDetail.getBoardedFlg().equalsIgnoreCase("D"))) {
								routeReport.put("boardingTime",dateFormatter.format(new Date(empDetail.getPickedUpDateAndTime())));
								boardingTimeCell.setCellValue(dateFormatter.format(new Date(empDetail.getPickedUpDateAndTime())));
							}
						} else {
							routeReport.put("boardingTime", "0");
							boardingTimeCell.setCellValue("0");

						}
					} catch (Exception e) {
						routeReport.put("boardingTime", "0");
						boardingTimeCell.setCellValue("0");
						log.info("Error in drop type" + e);
					}
				}
				
				Cell cabReachedTimeCell = OutSiderow.createCell(3);
				try {
					
					if (!(empDetail.getCabRecheddestinationTime() == 0)) {
						routeReport.put("cabReachedTime",dateFormatter.format(new Date(empDetail.getCabRecheddestinationTime())));					
						
						cabReachedTimeCell.setCellValue(dateFormatter.format(new Date(empDetail.getCabRecheddestinationTime())));
					} else {
						routeReport.put("cabReachedTime", "0");						
						cabReachedTimeCell.setCellValue("0");

					}

				} catch (Exception e) {
					log.info("error in cabReachedTime" + e);
					routeReport.put("cabReachedTime", "0");
					cabReachedTimeCell.setCellValue("0");					
				}
				// routeReport.put("boardingTime",
				// dateFormatter.format(empDetail.getCabstartFromDestination()));

				try {
					routeReport.put("vehicleReachTime",
							dateFormatter.format(empDetail.getReachedMessageDeliveryDate()));
					cabReachedTimeCell.setCellValue(dateFormatter.format(empDetail.getReachedMessageDeliveryDate()));
				} catch (Exception e) {
					routeReport.put("vehicleReachTime", "NO");
					cabReachedTimeCell.setCellValue("NO");
					log.info("error in vehicleReachTime" + e);

				}
				Cell routeCloseTimeCell = OutSiderow.createCell(1);
				try {
					routeReport.put("routeCloseTime",dateFormatter.format(empDetail.getEfmFmAssignRoute().getAllocationMsgDeliveryDate()));	
					routeCloseTimeCell.setCellValue(dateFormatter.format(empDetail.getEfmFmAssignRoute().getAllocationMsgDeliveryDate()));	
				} catch (Exception e) {
					routeReport.put("routeCloseTime", "Manual");
					routeCloseTimeCell.setCellValue("Manual");
					log.info("error in routeCloseTime" + e);
				}
				routeReport.put("tripStartTime",dateFormatter.format(empDetail.getEfmFmAssignRoute().getTripStartTime()));
				Cell tripStartTimeCell = OutSiderow.createCell(2);
				tripStartTimeCell.setCellValue(dateFormatter.format(empDetail.getActualTime()));
				routeReport.put("tripEndTime",dateFormatter.format(empDetail.getEfmFmAssignRoute().getTripCompleteTime()));
				Cell tripEndTimeCell = OutSiderow.createCell(5);
				tripEndTimeCell.setCellValue(dateFormatter.format(empDetail.getEfmFmAssignRoute().getTripCompleteTime()));
				Cell boardingStatusCell = OutSiderow.createCell(13);				
				if (empDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
					if (empDetail.getBoardedFlg().equalsIgnoreCase("D")) {
						routeReport.put("boardingStatus", "Dropped");
						boardingStatusCell.setCellValue("Dropped");
					} else if (empDetail.getBoardedFlg().equalsIgnoreCase("NO")) {
						routeReport.put("boardingStatus", "No Show");
						boardingStatusCell.setCellValue("No Show");
					} else {
						routeReport.put("boardingStatus", "Yet to dropped");
						boardingStatusCell.setCellValue("Yet to dropped");
					}

				} else if (empDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
					if (empDetail.getBoardedFlg().equalsIgnoreCase("B")) {
						routeReport.put("boardingStatus", "PickedUp");
						boardingStatusCell.setCellValue( "PickedUp");
					} else if (empDetail.getBoardedFlg().equalsIgnoreCase("NO")) {
						routeReport.put("boardingStatus", "No Show");
						boardingStatusCell.setCellValue("No Show");
					} else {
						routeReport.put("boardingStatus", "Yet to picked up");
						boardingStatusCell.setCellValue("Yet to dropped");
					}
				}
				routeWiseReportList.add(routeReport);
				
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
				 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return response.build();   
		      
		}

	/**
	 * @param Get
	 *            Get particular route history start time and end time and cab
	 *            locations details...
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/routeHistory")
	public Response getRouteHistory(EFmFmAssignRoutePO assignRoute) throws ParseException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 log.info("serviceStart -UserId :" + assignRoute.getUserId());
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
				log.info("authenticationToken error"+e);
			}
	
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		List<Map<String, Object>> routeWiseReportList = new ArrayList<Map<String, Object>>();
		EFmFmActualRoutTravelledPO actualRoutTravelled = new EFmFmActualRoutTravelledPO();
		actualRoutTravelled.setTravelledTime(new Date());
		EFmFmClientBranchPO clientBranchPO = new EFmFmClientBranchPO();
		clientBranchPO.setBranchId(assignRoute.geteFmFmClientBranchPO().getBranchId());
		clientBranchPO.setCombinedFacility(new MultifacilityService().combinedBranchIdDetails
				(assignRoute.getUserId(),assignRoute.getCombinedFacility()));
		
		actualRoutTravelled.seteFmFmClientBranchPO(clientBranchPO);
		actualRoutTravelled.setEfmFmAssignRoute(assignRoute);
		List<EFmFmActualRoutTravelledPO> actualRouteTravelled = iAssignRouteBO
				.getEtaAndDistanceFromAssignRouteId(actualRoutTravelled);
		log.info("Travelled Report"+actualRouteTravelled.size());
		if(!(actualRouteTravelled.isEmpty())){
		for (EFmFmActualRoutTravelledPO eFmFmActualRoutTravelledPO : actualRouteTravelled) {
			Map<String, Object> routeReport = new HashMap<String, Object>();
			routeReport.put("cabTravelledTime", dateFormatter.format(eFmFmActualRoutTravelledPO.getTravelledTime()));
			routeReport.put("cabLocation", eFmFmActualRoutTravelledPO.getCurrentCabLocation());
			routeReport.put("cabEta", eFmFmActualRoutTravelledPO.getCurrentEta());
			routeWiseReportList.add(routeReport);
		}
		}
		 log.info("serviceEnd -UserId :" + assignRoute.getUserId());
		return Response.ok(routeWiseReportList, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * @param Get
	 *            Get particular route history start time and end time and cab
	 *            locations details...
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("/routeHistoryDownload")
	public Response getRouteHistoryDwn(EFmFmAssignRoutePO assignRoute) throws ParseException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 log.info("serviceStart -UserId :" + assignRoute.getUserId());
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
				log.info("authenticationToken error"+e);
			}
		
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		List<Map<String, Object>> routeWiseReportList = new ArrayList<Map<String, Object>>();
		EFmFmActualRoutTravelledPO actualRoutTravelled = new EFmFmActualRoutTravelledPO();
		actualRoutTravelled.setTravelledTime(new Date());
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
		int rownum = 0, noOfRoute = 0;
		Row firstRow = sheet.createRow(rownum++); 
		for (int columnIndex = 0; columnIndex < 3; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
			Cell columnCol = firstRow.createCell(columnIndex);
			columnCol.setCellStyle(style);
			
		}
		Cell tripId = firstRow.createCell(0);
		tripId.setCellValue("Trip Id");
		tripId.setCellStyle(style);				
		
		Cell assignDateCol = firstRow.createCell(1);
		assignDateCol.setCellValue("Assign Date ");
		assignDateCol.setCellStyle(style);
		
		Row SecondRow = sheet.createRow(rownum++); 
		Cell startDate = SecondRow.createCell(0);
		startDate.setCellValue("Start Date ");
		startDate.setCellStyle(style);
		
		Cell endDateCol = SecondRow.createCell(1);
		endDateCol.setCellValue("End Date ");
		endDateCol.setCellStyle(style);
		
		Row OutSiderow = sheet.createRow(rownum++); 
		for (int columnIndex = 0; columnIndex <3 ; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
			Cell columnCol = OutSiderow.createCell(columnIndex);
			columnCol.setCellStyle(style);
			
		}		
				Cell zerothCol = OutSiderow.createCell(0);
				zerothCol.setCellValue("Cab Location");
				zerothCol.setCellStyle(style);
				
				Cell secondCol = OutSiderow.createCell(1);
				secondCol.setCellValue("Cab ETA");
				secondCol.setCellStyle(style);
				
				Cell firstCol = OutSiderow.createCell(2);
				firstCol.setCellValue("Trip Id");
				firstCol.setCellStyle(style);			
				
		EFmFmClientBranchPO clientBranchPO = new EFmFmClientBranchPO();
		clientBranchPO.setBranchId(assignRoute.geteFmFmClientBranchPO().getBranchId());
		clientBranchPO.setCombinedFacility(new MultifacilityService().combinedBranchIdDetails
				(assignRoute.getUserId(),assignRoute.getCombinedFacility()));
		actualRoutTravelled.seteFmFmClientBranchPO(clientBranchPO);
		actualRoutTravelled.setEfmFmAssignRoute(assignRoute);
		List<EFmFmActualRoutTravelledPO> actualRouteTravelled = iAssignRouteBO
				.getEtaAndDistanceFromAssignRouteId(actualRoutTravelled);
		for (EFmFmActualRoutTravelledPO eFmFmActualRoutTravelledPO : actualRouteTravelled) {
			OutSiderow = sheet.createRow(rownum++);
			Map<String, Object> routeReport = new HashMap<String, Object>();
			routeReport.put("cabTravelledTime", dateFormatter.format(eFmFmActualRoutTravelledPO.getTravelledTime()));
			routeReport.put("cabLocation", eFmFmActualRoutTravelledPO.getCurrentCabLocation());
			routeReport.put("cabEta", eFmFmActualRoutTravelledPO.getCurrentEta());
			routeWiseReportList.add(routeReport);
			Cell cabTravelledTime = OutSiderow.createCell(2);
			cabTravelledTime.setCellValue(dateFormatter.format(eFmFmActualRoutTravelledPO.getTravelledTime()));
			Cell cabLocation = OutSiderow.createCell(0);
			cabLocation.setCellValue(eFmFmActualRoutTravelledPO.getCurrentCabLocation());
			Cell cabEta = OutSiderow.createCell(1);
			cabEta.setCellValue(eFmFmActualRoutTravelledPO.getCurrentEta());
			
			Cell assignId = firstRow.createCell(0);
			assignId.setCellValue("Trip Id : " +eFmFmActualRoutTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
			assignId.setCellStyle(style);
			
			Cell assignDate = firstRow.createCell(2);
			assignDate.setCellValue(dateFormatter.format(eFmFmActualRoutTravelledPO.getEfmFmAssignRoute().getTripAssignDate()));
			assignDate.setCellStyle(style);
			Cell startTripDate = SecondRow.createCell(0);
			startTripDate.setCellValue("Start Date :" + dateFormatter.format(eFmFmActualRoutTravelledPO.getEfmFmAssignRoute().getTripStartTime()));
			startTripDate.setCellStyle(style);
			Cell EndDate = SecondRow.createCell(2);
			EndDate.setCellValue(dateFormatter.format(eFmFmActualRoutTravelledPO.getEfmFmAssignRoute().getTripCompleteTime()));
			EndDate.setCellStyle(style);
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
		log.info("serviceEnd -UserId :" + assignRoute.getUserId());
		return response.build();   
      
}
	
	@POST
	@Path("/costReport")
	@Consumes("application/json")
	@Produces("application/json")	  
	public Response costReport(EFmFmAssignRoutePO assignRoutePO) throws ParseException, UnsupportedEncodingException {	
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");		
		Map<String, Object> responce = new HashMap<String, Object>();
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		  List<Map<String, Object>> costReport= new ArrayList<Map<String, Object>>();
		  
		  EFmFmClientBranchPO clientBranchPO=new EFmFmClientBranchPO();
		  clientBranchPO.setBranchId(assignRoutePO.geteFmFmClientBranchPO().getBranchId());
		  clientBranchPO.setCombinedFacility(new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));		
		    //list of projectId's
		  List<String> dateList = new ArrayList<String>();
		  String date = "";
		  List<EFmFmAssignRoutePO> allTripDetails =null;
		  
		  if(assignRoutePO.getProjectId()==0){
			  allTripDetails = iAssignRouteBO.
						 getListOfProjectIdsByDate(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		  }else{
			  allTripDetails = iAssignRouteBO.
						getListOfTripsByProjectIdWithDate(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),assignRoutePO.getProjectId());
			  	  
		  }	  	
		  if(!allTripDetails.isEmpty()){			
				outer: for (EFmFmAssignRoutePO trips : allTripDetails) {
					if (!(dateList.contains(formatter.format(trips.getTripAssignDate())))) {
						date = formatter.format(trips.getTripAssignDate());
						dateList.add(formatter.format(trips.getTripAssignDate()));
					} else {
						continue outer;
					}
				    List<Map<String, Object>> tripDetailsList= new ArrayList<Map<String, Object>>();
					Map<String, Object> detailTrip = new HashMap<String, Object>();
					for(EFmFmAssignRoutePO listOfRoutes:allTripDetails){
						if (date.equalsIgnoreCase(formatter.format(listOfRoutes.getTripAssignDate()))) {
		 					List<EFmFmEmployeeTripDetailPO> listRoutes = iAssignRouteBO.
		 							getListOfEmployeesByProjectIds(assignRoutePO.getProjectId(),
		 									listOfRoutes.getAssignRouteId());
		 					if(!listRoutes.isEmpty()){
		 						double tripAmount=0.0,travelledKm=0.0,userKm=0.0,empCount=0.0,utilization=0.0,empCostTax=0.0;
	 							ArrayList<Double> tripDetails=null;
	 							tripDetails=tripAmountCalculation(listOfRoutes.getAssignRouteId(),new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
	 							if(!tripDetails.isEmpty()){
	 								tripAmount=tripDetails.get(0);
	 								userKm=tripDetails.get(1);
	 								empCount=tripDetails.get(2);
	 							}
		 						List<Map<String, Object>> listRoutesDetails= new ArrayList<Map<String, Object>>();
		 						for(EFmFmEmployeeTripDetailPO tripList:listRoutes){		 									 							
		 							Map<String, Object>  routeDetails = new HashMap<String, Object>();
		 							routeDetails.put("tripDate", formatter.format(listOfRoutes.getTripAssignDate()));
		 							List<EFmFmClientProjectDetailsPO> projectDetails=iAssignRouteBO.
		 									getProjectDetails(tripList.geteFmFmEmployeeTravelRequest().getProjectId());
		 							routeDetails.put("projectId", projectDetails.get(0).getClientProjectId());
		 							routeDetails.put("clientProjectId", projectDetails.get(0).getProjectId());
		 							routeDetails.put("projectName", projectDetails.get(0).getEmployeeProjectName());
		 							routeDetails.put("tripDate", formatter.format(tripList.getEfmFmAssignRoute().getTripAssignDate()));
		 							routeDetails.put("tripId",tripList.getEfmFmAssignRoute().getAssignRouteId());
		 							routeDetails.put("tripType",tripList.getEfmFmAssignRoute().getTripType());
		 							routeDetails.put("vehicleNumber",tripList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
		 							routeDetails.put("vehicleType",tripList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
		 							routeDetails.put("contractDetails",tripList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractDescription());
		 							routeDetails.put("routeName",tripList.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
		 							routeDetails.put("shiftTime",tripList.getEfmFmAssignRoute().getShiftTime());
		 							if(tripList.getEfmFmAssignRoute().getEscortRequredFlag().equalsIgnoreCase("Y")){
		 								routeDetails.put("escortRequired",tripList.getEfmFmAssignRoute().getEscortRequredFlag());
		 								routeDetails.put("escort",tripList.getEfmFmAssignRoute().geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
		 								routeDetails.put("escortId",tripList.getEfmFmAssignRoute().geteFmFmEscortCheckIn().geteFmFmEscortMaster().getEscortId());
		 								routeDetails.put("escortName",tripList.getEfmFmAssignRoute().geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
		 								
		 							}else{
		 								routeDetails.put("escort","Not Required");
		 							}		 							
		 							utilization=empCount/tripList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getAvailableSeat() * 100;
		 							//Tax Need to configure
		 							empCostTax=tripAmount+((tripAmount*6)/100);		 							
		 							routeDetails.put("utilization",Math.round(utilization*100)/100.00);		 							
		 							routeDetails.put("empCostWithTax",Math.round(empCostTax*100)/100.00);
		 							
		 							if(tripList.getEfmFmAssignRoute().getTripStartTime()==null){
		 								routeDetails.put("startTime","NA");
		 							}else{
		 								routeDetails.put("startTime",dateFormatter.format(tripList.getEfmFmAssignRoute().getTripStartTime()));
		 							}
		 							if(tripList.getEfmFmAssignRoute().geteFmFmClientBranchPO().getDistanceFlg().equalsIgnoreCase("GPS")){
		 								travelledKm=tripList.getEfmFmAssignRoute().getTravelledDistance();		 								
		 							}else if (tripList.getEfmFmAssignRoute().geteFmFmClientBranchPO().getDistanceFlg().equalsIgnoreCase("Odometer")) {
		 								if(Double.parseDouble(tripList.getEfmFmAssignRoute().getOdometerEndKm()) 
		 										>=Double.parseDouble(tripList.getEfmFmAssignRoute().getOdometerStartKm())){
		 									travelledKm=(Double.parseDouble(tripList.getEfmFmAssignRoute().getOdometerEndKm())
		 											-Double.parseDouble(tripList.getEfmFmAssignRoute().getOdometerStartKm()));
		 								}					
		 							}	 							
		 							        routeDetails.put("distance",Math.round(travelledKm*100)/100.00);				
		 								    routeDetails.put("employeeId",tripList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());		 									
		 									routeDetails.put("employeeName",new String(Base64.getDecoder().decode(tripList.geteFmFmEmployeeTravelRequest().
		 				 											getEfmFmUserMaster().getFirstName()),"utf-8"));		
		 									routeDetails.put("employeeDepartment",new String(Base64.getDecoder().decode(tripList.geteFmFmEmployeeTravelRequest().
			 											getEfmFmUserMaster().getEmployeeDepartment()),"utf-8"));	
		 									routeDetails.put("mobileNumber",new String(Base64.getDecoder().decode(tripList.geteFmFmEmployeeTravelRequest().
		 											getEfmFmUserMaster().getMobileNumber()),"utf-8"));
		 									routeDetails.put("employeeCost",Math.round(tripAmount*100)/100.00);
		 									routeDetails.put("userKm",Math.round(userKm*100)/100.00);
		 									tripDetailsList.add(routeDetails);
		 									
		 							}	
		 					
		 						}
							}
							detailTrip.put("tripDetail", tripDetailsList);
							detailTrip.put("tripAssignDate", date);
						}
					   costReport.add(detailTrip);
					}
		 				
		 		}else{
					responce.put("failed","TRIPNOTFOUND");
					return Response.ok(responce,MediaType.APPLICATION_JSON).build();				
			     }		 		  
	  	return Response.ok(costReport,MediaType.APPLICATION_JSON).build();  
	}
	
	
	@POST
	@Path("/costReportDownload")
	@Consumes("application/json")
	@Produces("application/json")	  
	public Response costReportDownload(EFmFmAssignRoutePO assignRoutePO) throws ParseException, UnsupportedEncodingException {	
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");		
		Map<String, Object> responce = new HashMap<String, Object>();
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date fromDate = formatter.parse(assignRoutePO.getFromDate());
		Date toDate = formatter.parse(assignRoutePO.getToDate());
		
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("VehicleCostReport");
        XSSFCellStyle style = workbook.createCellStyle();      
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		font.setColor(new XSSFColor(new java.awt.Color(255,255,255)));//color
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0,82,128)));
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(font);  
        
        
        int rownum = 0, noOfRoute = 0;
        Row insideRow = sheet.createRow(rownum++); 
        for (int columnIndex = 0; columnIndex < 25; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
		Cell tripAssignDate= insideRow.createCell(0);
		tripAssignDate.setCellValue("Date");		
		tripAssignDate.setCellStyle(style);
		
		
		
		Cell tripId= insideRow.createCell(1);
		tripId.setCellValue("Trip Id");
		tripId.setCellStyle(style);
		
		Cell userId= insideRow.createCell(2);
		userId.setCellValue("User ID");
		userId.setCellStyle(style);
		
		Cell userName= insideRow.createCell(3);
		userName.setCellValue("User Name");
		userName.setCellStyle(style);
		
		Cell empDepartment= insideRow.createCell(4);
		empDepartment.setCellValue("Team / Department");
		empDepartment.setCellStyle(style);
		
		Cell projectId= insideRow.createCell(5);
		projectId.setCellValue("Project Id");
		projectId.setCellStyle(style);
		
		Cell projectName= insideRow.createCell(6);
		projectName.setCellValue("Project Name");
		projectName.setCellStyle(style);
		
		Cell guestMobileNum= insideRow.createCell(7);
		guestMobileNum.setCellValue("Guest mobile no");
		guestMobileNum.setCellStyle(style);
		
		Cell location= insideRow.createCell(8);
		location.setCellValue("Usage Location`s");
		location.setCellStyle(style);
		
		Cell escortHeader= insideRow.createCell(9);
		escortHeader.setCellValue("Tariff Type");
		escortHeader.setCellStyle(style);
		
		Cell carType= insideRow.createCell(10);
		carType.setCellValue("Car Type");
		carType.setCellStyle(style);
		
		Cell vehicleRegNo= insideRow.createCell(11);
		vehicleRegNo.setCellValue("Vehicle Reg No");
		vehicleRegNo.setCellStyle(style);
		
		Cell cabReqTime= insideRow.createCell(12);
		cabReqTime.setCellValue("Cab Req Time");
		cabReqTime.setCellStyle(style);
		
		Cell cabDepTime= insideRow.createCell(13);
		cabDepTime.setCellValue("Cab Dep Time");
		cabDepTime.setCellStyle(style);
		
		Cell distanceByKm= insideRow.createCell(14);
		distanceByKm.setCellValue("Trip Distance KM");
		distanceByKm.setCellStyle(style);
		
		Cell distancePerUser= insideRow.createCell(15);
		distancePerUser.setCellValue("Distance per user");
		distancePerUser.setCellStyle(style);
		
		Cell utilizationPerUser= insideRow.createCell(16);
		utilizationPerUser.setCellValue("Utilization");
		utilizationPerUser.setCellStyle(style);
		
		Cell travelsCharges= insideRow.createCell(17);
		travelsCharges.setCellValue("Travels Charges");
		travelsCharges.setCellStyle(style);		
		
		Cell travelsChargesInTax= insideRow.createCell(18);
		travelsChargesInTax.setCellValue("Travels Charges incl Tax");
		travelsChargesInTax.setCellStyle(style);
		
		Cell bookedBy= insideRow.createCell(19);
		bookedBy.setCellValue("Booked By");
		bookedBy.setCellStyle(style);	
		
		Cell escort= insideRow.createCell(20);
		escort.setCellValue("Escort Confirm");
		escort.setCellStyle(style);
		
		Cell tripType= insideRow.createCell(21);
		tripType.setCellValue("Trip Type");
		tripType.setCellStyle(style);



		  List<Map<String, Object>> costReport= new ArrayList<Map<String, Object>>();
		  
		  EFmFmClientBranchPO clientBranchPO=new EFmFmClientBranchPO();
		  clientBranchPO.setBranchId(assignRoutePO.getBranchId());
		  clientBranchPO.setCombinedFacility(new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));		
		    //list of projectId's
		  List<EFmFmAssignRoutePO> allTripDetails =null;
		  
		  if(assignRoutePO.getProjectId()==0){
			  allTripDetails = iAssignRouteBO.
						 getListOfProjectIdsByDate(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		  }else{
			  allTripDetails = iAssignRouteBO.
						getListOfTripsByProjectIdWithDate(fromDate, toDate,new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()),assignRoutePO.getProjectId());
			  	  
		  }	  	
		  if(!allTripDetails.isEmpty()){		 		
		 		for(EFmFmAssignRoutePO listOfRoutes:allTripDetails){
		 			 
		 					List<EFmFmEmployeeTripDetailPO> listRoutes = iAssignRouteBO.
		 							getListOfEmployeesByProjectIds(assignRoutePO.getProjectId(),
		 									listOfRoutes.getAssignRouteId());
		 					if(!listRoutes.isEmpty()){
		 						double tripAmount=0.0,travelledKm=0.0,userKm=0.0,empCount=0.0,utilization=0.0,empCostTax=0.0;
	 							ArrayList<Double> tripDetails=null;
	 							tripDetails=tripAmountCalculation(listOfRoutes.getAssignRouteId(),new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
	 							if(!tripDetails.isEmpty()){
	 								tripAmount=tripDetails.get(0);
	 								userKm=tripDetails.get(1);
	 								empCount=tripDetails.get(2);
	 							}
		 						List<Map<String, Object>> listRoutesDetails= new ArrayList<Map<String, Object>>();
		 						for(EFmFmEmployeeTripDetailPO tripList:listRoutes){	
		 							insideRow = sheet.createRow(rownum++); 
		 							Map<String, Object>  routeDetails = new HashMap<String, Object>();
		 							routeDetails.put("tripDate", formatter.format(tripList.getEfmFmAssignRoute().getTripAssignDate()));
			 						Cell tripAssignDateCell = insideRow.createCell(0);
			 						tripAssignDateCell.setCellValue(formatter.format(tripList.getEfmFmAssignRoute().getTripAssignDate()));
		 							List<EFmFmClientProjectDetailsPO> projectDetails=iAssignRouteBO.
		 							getProjectDetails(tripList.geteFmFmEmployeeTravelRequest().getProjectId());
		 							routeDetails.put("clientProjectId", projectDetails.get(0).getClientProjectId());
		 							Cell clientProjectIdCell = insideRow.createCell(5);
		 							clientProjectIdCell.setCellValue(projectDetails.get(0).getClientProjectId());		 					
		 							routeDetails.put("projectName", projectDetails.get(0).getEmployeeProjectName());
		 							Cell clientProjectNameCell = insideRow.createCell(6);
		 							clientProjectNameCell.setCellValue( projectDetails.get(0).getEmployeeProjectName());	 							
		 							routeDetails.put("tripId",tripList.getEfmFmAssignRoute().getAssignRouteId());		 							
			 						Cell tripIdCell = insideRow.createCell(1);
			 						tripIdCell.setCellValue(tripList.getEfmFmAssignRoute().getAssignRouteId());
		 							routeDetails.put("tripType",tripList.getEfmFmAssignRoute().getTripType());
		 							Cell tripTypeCell = insideRow.createCell(21);
		 							tripTypeCell.setCellValue(tripList.getEfmFmAssignRoute().getTripType());
		 								
		 							routeDetails.put("vehicleNumber",tripList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
		 							Cell vehicleNumberCell = insideRow.createCell(11);
		 							vehicleNumberCell.setCellValue(tripList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
		 							routeDetails.put("vehicleType",tripList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
		 							Cell vehicleTypeCell = insideRow.createCell(10);
		 							vehicleTypeCell.setCellValue(tripList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
		 							routeDetails.put("tariffType",tripList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractDescription());
		 							Cell tariffType = insideRow.createCell(9);
		 							tariffType.setCellValue(tripList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().geteFmFmContractDetails().geteFmFmVendorContractTypeMaster().getContractDescription());
		 							routeDetails.put("routeName",tripList.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
		 							Cell routeName = insideRow.createCell(8);
		 							routeName.setCellValue(tripList.getEfmFmAssignRoute().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
		 							
		 							routeDetails.put("shiftTime",tripList.getEfmFmAssignRoute().getShiftTime());
		 							Cell shiftTime = insideRow.createCell(12);
		 							shiftTime.setCellValue(tripList.getEfmFmAssignRoute().getShiftTime());
		 							
		 							
		 							Cell escortRequired = insideRow.createCell(20);
		 							
		 							if(tripList.getEfmFmAssignRoute().getEscortRequredFlag().equalsIgnoreCase("Y")){
		 								routeDetails.put("escortRequired",tripList.getEfmFmAssignRoute().getEscortRequredFlag());
		 								routeDetails.put("escortId",tripList.getEfmFmAssignRoute().geteFmFmEscortCheckIn().geteFmFmEscortMaster().getEscortId());
		 								routeDetails.put("escortName",tripList.getEfmFmAssignRoute().geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
		 								escortRequired.setCellValue(tripList.getEfmFmAssignRoute().geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
		 								
		 							}else{
		 								routeDetails.put("escort","Not Required");
		 								escortRequired.setCellValue("Not Required");
		 							}
		 							
		 							utilization=empCount/tripList.getEfmFmAssignRoute().getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getAvailableSeat() * 100;
		 							//Tax Need to configure
		 							empCostTax=tripAmount+((tripAmount*6)/100);		 							
		 							routeDetails.put("utilization",Math.round(utilization*100)/100.00);	
		 							Cell utilizationCell = insideRow.createCell(16);
		 							utilizationCell.setCellValue(Math.round(utilization*100)/100.00);	
		 							routeDetails.put("empCostWithTax",Math.round(empCostTax*100)/100.00);		 							
		 							Cell empCostWithTax = insideRow.createCell(18);
		 							empCostWithTax.setCellValue(Math.round(empCostTax*100)/100.00);		 							
		 							Cell startTime = insideRow.createCell(13);		 							
		 							if(tripList.getEfmFmAssignRoute().getTripStartTime()==null){
		 								routeDetails.put("startTime","NA");
		 								startTime.setCellValue("NA");
		 							}else{
		 								routeDetails.put("startTime",dateFormatter.format(tripList.getEfmFmAssignRoute().getTripStartTime()));
		 								startTime.setCellValue(dateFormatter.format(tripList.getEfmFmAssignRoute().getTripStartTime()));
		 							}
		 							if(tripList.getEfmFmAssignRoute().geteFmFmClientBranchPO().getDistanceFlg().equalsIgnoreCase("GPS")){
		 								travelledKm=tripList.getEfmFmAssignRoute().getTravelledDistance();		 								
		 							}else if (tripList.getEfmFmAssignRoute().geteFmFmClientBranchPO().getDistanceFlg().equalsIgnoreCase("Odometer")) {
		 								if(Double.parseDouble(tripList.getEfmFmAssignRoute().getOdometerEndKm()) 
		 										>=Double.parseDouble(tripList.getEfmFmAssignRoute().getOdometerStartKm())){
		 									travelledKm=(Double.parseDouble(tripList.getEfmFmAssignRoute().getOdometerEndKm())
		 											-Double.parseDouble(tripList.getEfmFmAssignRoute().getOdometerStartKm()));
		 								}					
		 							}	 							
		 							        routeDetails.put("distance",Math.round(travelledKm*100)/100.00);		 							        
		 							        Cell distance = insideRow.createCell(14);
		 							        distance.setCellValue(Math.round(travelledKm*100)/100.00);
		 							        
		 								    routeDetails.put("employeeId",tripList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
		 								    Cell employeeId = insideRow.createCell(2);
		 								    employeeId.setCellValue(tripList.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
		 									routeDetails.put("employeeName",new String(Base64.getDecoder().decode(tripList.geteFmFmEmployeeTravelRequest().
		 				 											getEfmFmUserMaster().getFirstName()),"utf-8"));	
		 									 Cell employeeName = insideRow.createCell(3);
		 									 employeeName.setCellValue(new String(Base64.getDecoder().decode(tripList.geteFmFmEmployeeTravelRequest().
				 											getEfmFmUserMaster().getFirstName()),"utf-8"));	
		 									 
		 									routeDetails.put("employeeDepartment",new String(Base64.getDecoder().decode(tripList.geteFmFmEmployeeTravelRequest().
			 											getEfmFmUserMaster().getEmployeeDepartment()),"utf-8"));
		 									Cell employeeDepartment = insideRow.createCell(4);
		 									employeeDepartment.setCellValue(new String(Base64.getDecoder().decode(tripList.geteFmFmEmployeeTravelRequest().
			 											getEfmFmUserMaster().getEmployeeDepartment()),"utf-8"));		 									
		 									routeDetails.put("mobileNumber",new String(Base64.getDecoder().decode(tripList.geteFmFmEmployeeTravelRequest().
		 											getEfmFmUserMaster().getMobileNumber()),"utf-8"));
		 									 Cell mobileNumber = insideRow.createCell(7);
		 									 mobileNumber.setCellValue(new String(Base64.getDecoder().decode(tripList.geteFmFmEmployeeTravelRequest().
		 											getEfmFmUserMaster().getMobileNumber()),"utf-8"));
		 									routeDetails.put("employeeCost",Math.round(tripAmount*100)/100.00);
		 									Cell employeeCost = insideRow.createCell(17);
		 									employeeCost.setCellValue(Math.round(tripAmount*100)/100.00);
		 									routeDetails.put("userKm",Math.round(userKm*100)/100.00);
		 									Cell userUsedKm = insideRow.createCell(15);
		 									userUsedKm.setCellValue(Math.round(userKm*100)/100.00);
		 									costReport.add(routeDetails);		 													
		 						}	
		 					
		 					}					
		 				}
		 				
		 		}else{
					responce.put("failed","TRIPNOTFOUND");
					return Response.ok(responce,MediaType.APPLICATION_JSON).build();				
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
	
	public ArrayList<Double> tripAmountCalculation(int assignRouteId,String combinedBranchId){
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		List<EFmFmAssignRoutePO> listOfRoutes=iAssignRouteBO.getTravelledEmployeeCountByTrips(assignRouteId, combinedBranchId);
		double travelCost=0.0;
		ArrayList<Double> values=new ArrayList<Double>();
		if(!listOfRoutes.isEmpty()){
			int employeeCount=listOfRoutes.size();
			double travelledKm=0.0,perkmAmount=0.0,totalAmt=0.0,userDistance=0.0;
			String distanceFlg = listOfRoutes.get(0).geteFmFmClientBranchPO().getDistanceFlg();			
				try {
					if(distanceFlg.equalsIgnoreCase("GPS")){
						travelledKm=listOfRoutes.get(0).getTravelledDistance();
					}else if (distanceFlg.equalsIgnoreCase("Odometer")) {
						if(Double.parseDouble(listOfRoutes.get(0).getOdometerEndKm()) 
								>=Double.parseDouble(listOfRoutes.get(0).getOdometerStartKm())){
							travelledKm=(Double.parseDouble(listOfRoutes.get(0).getOdometerEndKm())
									-Double.parseDouble(listOfRoutes.get(0).getOdometerStartKm()));
						}					
					}					
					if(listOfRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().geteFmFmContractDetails().
							geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("FDC")){
							perkmAmount=listOfRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().geteFmFmContractDetails().getFixedDistanceChargeRate()
									/listOfRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().geteFmFmContractDetails().getFixedDistanceMonthly();
								travelCost=(travelledKm*perkmAmount)/employeeCount;
								values.add(travelCost);
								userDistance=travelledKm/employeeCount;
								values.add(userDistance);
								values.add(new Double(employeeCount));
							}else if(listOfRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().geteFmFmContractDetails().
									geteFmFmVendorContractTypeMaster().getContractType().equalsIgnoreCase("TDC")){
								if(travelledKm >listOfRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().geteFmFmContractDetails().getFixedDistancePrDay()){
									 totalAmt=(travelledKm -listOfRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().
											geteFmFmContractDetails().getFixedDistancePrDay()) * listOfRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().
											geteFmFmContractDetails().getPerKmCost();
									totalAmt=totalAmt+listOfRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().
											geteFmFmContractDetails().getPerDayCost();
									travelCost=totalAmt/employeeCount;
									values.add(travelCost);
									userDistance=travelledKm/employeeCount;
									values.add(userDistance);
								}else{
									totalAmt=listOfRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().
											geteFmFmContractDetails().getPerDayCost();
									travelCost=totalAmt/employeeCount;
									values.add(travelCost);
									userDistance=travelledKm/employeeCount;
									values.add(userDistance);
									
								}								
							}else{
								totalAmt=listOfRoutes.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().
										geteFmFmContractDetails().getPerDayCost();
								travelCost=totalAmt/employeeCount;
								values.add(travelCost);
								userDistance=travelledKm/employeeCount;
								values.add(userDistance);
								values.add(new Double(employeeCount));
								
							}	
				} catch (Exception e) {
					log.debug("Null Km values");
				}						
		}
		return values;		
	}
	
	 @POST
	    @Path("/getGeocodedDistanceVariation")
	    public Response getAppDownloadedButNoGeoCodeUsers(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) throws UnsupportedEncodingException {  	
			IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
			 Map<String, Object> responce = new HashMap<String, Object>();
			 if(eFmFmDeviceMasterPO.getStartPgNo()==0){		
			 /*log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
			 try{
					if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
			 		responce.put("status", "invalidRequest");
			 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			 	}}catch(Exception e){
			 		log.info("authentication error"+e);
					responce.put("status", "invalidRequest");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			 	}
			 
			 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
			   if (!(userDetailToken.isEmpty())) {
			    String jwtToken = "";
			    try {
			     JwtTokenGenerator token = new JwtTokenGenerator();
			     jwtToken = token.generateToken();
			     userDetailToken.get(0).setAuthorizationToken(jwtToken);
			     userDetailToken.get(0).setTokenGenerationTime(new Date());
			     userMasterBO.update(userDetailToken.get(0));
			    } catch (Exception e) {
			     log.info("error" + e);
			    }
			   }*/
			   
			 }
	   	    log.info("GoeCode serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
			List<Map<String, Object>> allusers = new ArrayList<Map<String, Object>>();		
	        List<EFmFmUserMasterPO> userDetail = userMasterBO.getAllGeoCodedDiffEmployeesList(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getStartPgNo(),eFmFmDeviceMasterPO.getEndPgNo());
	         int totalCount=0;
			 List<EFmFmUserMasterPO> userDetailCount=null;
			 if(eFmFmDeviceMasterPO.getStartPgNo()==0){
			  userDetailCount= userMasterBO.getAllGeoCodedDiffEmployeesList(eFmFmDeviceMasterPO.getCombinedFacility());
			  totalCount=userDetailCount.size();
			 }
	        log.info("userDetail"+userDetail.size());
	        if (!(userDetail.isEmpty())) {
	        	for(EFmFmUserMasterPO user:userDetail){
					Map<String, Object> requests = new HashMap<String, Object>();
					requests.put("userId", user.getUserId());
					requests.put("employeeId", user.getEmployeeId());
					requests.put("emailId", new String(Base64.getDecoder().decode(user.getEmailId()), "utf-8"));
					requests.put("userName", user.getUserName());
					requests.put("employeeAddress", new String(Base64.getDecoder().decode(user.getAddress()), "utf-8"));
					if(null!=user.getGeoCodedAddress()){
						requests.put("geoCodedAddress", new String(Base64.getDecoder().decode(user.getGeoCodedAddress()), "utf-8"));
					}else{
						requests.put("geoCodedAddress","");
					}
					requests.put("employeeLatiLongi",user.getLatitudeLongitude());
					requests.put("homeGeoCodePoints",user.getHomeGeoCodePoints());
					requests.put("distanceVariation",user.getGeoCodeVariationDistance());
					requests.put("totalCount",userDetail.size());
					requests.put("totalRecordCount",totalCount);
					requests.put("employeeName", new String(Base64.getDecoder().decode(user.getFirstName()), "utf-8"));
					requests.put("mobileNumber", new String(Base64.getDecoder().decode(user.getMobileNumber()), "utf-8"));
					allusers.add(requests);
	        	}        	
	        }
	        log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
	        return Response.ok(allusers, MediaType.APPLICATION_JSON).build();
	    }
	
	public Time SumOfMinutes(Time time, Time timeToAdd) { 
	    // Get the hour values
	    int timeHours = time.getHours();
	    int AddHours = timeToAdd.getHours();
	    // Get the minute values
	    int timeMinutes = time.getMinutes();
	    int AddMinutes = timeToAdd.getMinutes();
	    timeHours += AddHours;
	    timeMinutes += AddMinutes;
	    // Set the hours & minutes
	    time.setHours(timeHours);
	    time.setMinutes(timeMinutes);

	    return time;
	}
	
	

}