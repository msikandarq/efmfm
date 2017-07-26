package com.newtglobal.eFmFmFleet.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IAlertBO;
import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IEmployeeDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.CalculateDistance;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Geocode;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.MessagingService;
import com.newtglobal.eFmFmFleet.eFmFmFleet.PasswordEncryption;
import com.newtglobal.eFmFmFleet.eFmFmFleet.PushNotificationService;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Validator;
import com.newtglobal.eFmFmFleet.model.EFmCheckInVehicleTrackingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmActualRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlertTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaNodalMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchSubConfigurationPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientRouteMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientUserRolePO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeRequestMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLiveRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLocationMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmMultipleLocationTvlReqPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRoleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripTimingMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmZoneMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/trip")
@Consumes("application/json")
@Produces("application/json")
public class CabRequestService {
	DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
	private static Log log = LogFactory.getLog(CabRequestService.class);
	DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
	DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	
	/**
	 * The employeeTravelRequstDetails method implemented. for getting the list
	 * of travel request from employeeTravelREquestTable.
	 *
	 * @author Rajan R
	 * @since 2015-05-13
	 * 
	 * @return Employee Travel Desk Module.
	 */
	private static byte[] key;
	private static String keyString = "iI1tzzodgnjqhMQq6zIyPJ9iy40=";

	@POST
	@Path("/employeeTravelRequest")
	public Response employeeTravelRequestDetails(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException, UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		
		Map<String, Object> responce = new HashMap<String, Object>();
		if(employeeTravelRequestPO.getStartPgNo()==0){
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
		if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}

		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
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
		   }
		}
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
		List<Map<String, Object>> shitTimings = new ArrayList<Map<String, Object>>();
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftDateFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		DateFormat shiftTimeFormaters = new SimpleDateFormat("HH:mm:ss");
		employeeTravelRequestPO.setRequestDate(new Date());
		String requestDate = requestDateFormat.format(new Date());
		List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO.listOfShiftTime(employeeTravelRequestPO.getCombinedFacility());
		if (!(shiftTimeDetails.isEmpty())) {
			for (EFmFmTripTimingMasterPO shiftiming : shiftTimeDetails) {
				Map<String, Object> requestList = new HashMap<String, Object>();
				requestList.put("shiftTime", shiftTimeFormater.format(shiftiming.getShiftTime()));
				shitTimings.add(requestList);
			}
		}
	
		List<EFmFmRoleMasterPO> userRole=userMasterBO.getUserRoleByUserId(employeeTravelRequestPO.getUserId());
		List<EFmFmEmployeeTravelRequestPO> travelDetails=null;		
		 if(userRole.get(0).getRole().equalsIgnoreCase("manager")){			 
			 travelDetails =  iCabRequestBO.getListOfApprovalPendingRequest(employeeTravelRequestPO.getCombinedFacility(),
					 employeeTravelRequestPO.getUserId(),"Y",employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());			 
		 }else{
		     travelDetails = iCabRequestBO
					.getGuestAndAdhocTravelRequestsForGivendate(employeeTravelRequestPO.getCombinedFacility(),requestDate,employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
		 }	
		List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
		if (!(travelDetails.isEmpty())) {
			for (EFmFmEmployeeTravelRequestPO allTravelRequest : travelDetails) {
				Map<String, Object> requestList = new HashMap<String, Object>();
				requestList.put("employeeId", allTravelRequest.getEfmFmUserMaster().getEmployeeId());
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
				requestList.put("tripDate", formatter.format(allTravelRequest.getRequestDate()));
				requestList.put("weekOffs", allTravelRequest.getEfmFmUserMaster().getWeekOffDays());
				requestList.put("facilityName", allTravelRequest.geteFmFmClientBranchPO().getBranchName());
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
				if(null==allTravelRequest.getLocationFlg()){
					requestList.put("locationFlg","N");
				}else{
					requestList.put("locationFlg", allTravelRequest.getLocationFlg());
				}				
				requestList.put("tripTime", shiftTimeFormaters.format(allTravelRequest.getShiftTime()));
				requestList.put("employeeRouteName",
						allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				requestList.put("employeeRouteId",
						allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
				requestList.put("employeeAreaName",
						allTravelRequest.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
				requestList.put("employeeAreaId",
						allTravelRequest.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaId());
				requestList.put("employeePickUpTime", allTravelRequest.getPickUpTime());
				requestList.put("employeeName", new String(
						Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
				log.info("EmployeeId" + allTravelRequest.getEfmFmUserMaster().getEmployeeId());
				requestList.put("employeeAddress", new String(
						Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
				requestList.put("employeeWaypoints", allTravelRequest.getEfmFmUserMaster().getLatitudeLongitude());
				travelRequestList.add(requestList);
			}

		}
		responce.put("shifts", shitTimings);
		responce.put("requests", travelRequestList);
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	// delete shiftRequest from employee Travel Requests
	@POST
	@Path("/shiftRoster")
	@Produces("application/vnd.ms-excel")
	public Response downloadParticularShiftRoster(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException, UnsupportedEncodingException {
		
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
		   if (!(userDetailToken.isEmpty())) {
		    String jwtToken = "";
		    try {
		     JwtTokenGenerator token = new JwtTokenGenerator();
		     jwtToken = token.generateToken();
		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
		     userDetailToken.get(0).setTokenGenerationTime(new Date());
		     userMasterBO.update(userDetailToken.get(0));
		    } catch (Exception e) {
		    	e.printStackTrace();
		     log.info("error" + e);
		    }
		   }
		
		
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
		DateFormat requestFormat = new SimpleDateFormat("dd/MM/yyyy");
		List<EFmFmEmployeeTravelRequestPO> travelDetails=null;		
		List<EFmFmRoleMasterPO> userRole=userMasterBO.getUserRoleByUserId(employeeTravelRequestPO.getUserId());
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date =null; 
		String requestDate="";
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		String shiftDate="";
		Date shift=null;
		java.sql.Time shiftTime=null;
		 if(null !=employeeTravelRequestPO.getEmployeeId()){		 
				 try {
						if(employeeTravelRequestPO.getLocationFlg().equalsIgnoreCase("M")){
							travelDetails = iCabRequestBO
									.employeeRequestFromEmpIdByLocationFlg(employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getEmployeeId());
						}else{	
							travelDetails = iCabRequestBO
									.particularEmployeeRequestFromEmpId(employeeTravelRequestPO.getCombinedFacility(), employeeTravelRequestPO.getEmployeeId());
						}			
				}catch(Exception e) {
					travelDetails = iCabRequestBO
							.particularEmployeeRequestFromEmpId(employeeTravelRequestPO.getCombinedFacility(), employeeTravelRequestPO.getEmployeeId());
				}	
		 }else{	
			    date=dateFormat.parse(employeeTravelRequestPO.getResheduleDate());
			    requestDate = requestDateFormat.format(date);
			  
			 if(employeeTravelRequestPO.getTripType().equalsIgnoreCase("BOTH") && employeeTravelRequestPO.getTime().equalsIgnoreCase("All")){
				 if(userRole.get(0).getRole().equalsIgnoreCase("manager")){				
						travelDetails = iCabRequestBO
								.getRequestByDayWiceBothPickAndDropForNormalAndAdhocByManager(employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getUserId(),
										requestDate);
					}else{
						travelDetails = iCabRequestBO
								.getRequestByDayWiceBothPickAndDropForNormalAndAdhoc(employeeTravelRequestPO.getCombinedFacility(),
										requestDate);
					}
			 }
			 else if(employeeTravelRequestPO.getTripType().equalsIgnoreCase("BOTH") && !(employeeTravelRequestPO.getTime().equalsIgnoreCase("All"))){			 
				  shiftDate = employeeTravelRequestPO.getTime();
				  shift =  shiftFormate.parse(shiftDate);
				  shiftTime = new java.sql.Time(shift.getTime());
					if(userRole.get(0).getRole().equalsIgnoreCase("manager")){	
						travelDetails = iCabRequestBO
								.getRequestByDayWiceForNormalAndAdhocByManagerWithoutTripType(employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getUserId(),
										requestDate,shiftTime);
					}else{
						travelDetails = iCabRequestBO
								.getRequestByShiftWiceForNormalAndAdhocWithoutTripType(employeeTravelRequestPO.getCombinedFacility(),
										requestDate, shiftTime);
					}
			 }
					 else if(!(employeeTravelRequestPO.getTripType().equalsIgnoreCase("BOTH")) && (employeeTravelRequestPO.getTime().equalsIgnoreCase("All"))){
						 
						 if(userRole.get(0).getRole().equalsIgnoreCase("manager")){				
								travelDetails = iCabRequestBO
										.getRequestByDayWiceForNormalAndAdhocByManager(employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getUserId(),
												requestDate,employeeTravelRequestPO.getTripType());
							}else{
								travelDetails = iCabRequestBO
										.getRequestByDayWiceForNormalAndAdhoc(employeeTravelRequestPO.getCombinedFacility(),
												requestDate, employeeTravelRequestPO.getTripType());
							}
				
				 
			 }
			
			else{
				
				  shiftDate = employeeTravelRequestPO.getTime();
				  shift =  shiftFormate.parse(shiftDate);
				  shiftTime = new java.sql.Time(shift.getTime());
				if(userRole.get(0).getRole().equalsIgnoreCase("manager")){				
					travelDetails = iCabRequestBO
							.getRewquestByShiftWiceForNormalAndAdhocByManager(employeeTravelRequestPO.getUserId(),employeeTravelRequestPO.getCombinedFacility(),
									requestDate,shiftTime,employeeTravelRequestPO.getTripType());
				}else{
					travelDetails = iCabRequestBO
							.getRewquestByShiftWiceForNormalAndAdhoc(employeeTravelRequestPO.getCombinedFacility(),
									requestDate, shiftTime, employeeTravelRequestPO.getTripType());
				}
			}
			 
			 
		 }
		 
		 
		 
		log.info("route excel requestDate" + requestDate + "shiftTime" + shiftTime + "Request Size"
				+ travelDetails.size());
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Sheet1");
		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		font.setColor(new XSSFColor(new java.awt.Color(255, 255, 255)));// color
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 82, 128)));
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(font);
		int rownum = 0;
		Row OutSiderow = sheet.createRow(rownum++);
		for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
			Cell columnCol = OutSiderow.createCell(columnIndex);
			columnCol.setCellStyle(style);

		}
		;
		Cell zerothCol = OutSiderow.createCell(0);
		zerothCol.setCellValue("Employee Id");
		zerothCol.setCellStyle(style);

		Cell firstCol = OutSiderow.createCell(1);
		firstCol.setCellValue("Employee Name");
		firstCol.setCellStyle(style);

		Cell secondCol = OutSiderow.createCell(2);
		secondCol.setCellValue("Gender");
		secondCol.setCellStyle(style);

		Cell thirdCol = OutSiderow.createCell(3);
		thirdCol.setCellValue("address");
		thirdCol.setCellStyle(style);

		Cell fourthCol = OutSiderow.createCell(4);
		fourthCol.setCellValue("Area Name");
		fourthCol.setCellStyle(style);

		Cell fifthCol = OutSiderow.createCell(5);
		fifthCol.setCellValue("Pick/Drop Sequence");
		fifthCol.setCellStyle(style);

		Cell sixthCol = OutSiderow.createCell(6);
		sixthCol.setCellValue("Trip Type");
		sixthCol.setCellStyle(style);

		Cell seventhCol = OutSiderow.createCell(7);
		seventhCol.setCellValue("Route Name");
		seventhCol.setCellStyle(style);

		Cell eighthCol = OutSiderow.createCell(8);
		eighthCol.setCellValue("Request Date");
		eighthCol.setCellStyle(style);

		Cell nineCol = OutSiderow.createCell(9);
		nineCol.setCellValue("Shift Time");
		nineCol.setCellStyle(style);

		Cell tenthhCol = OutSiderow.createCell(10);
		tenthhCol.setCellValue("Nodal Point name");
		tenthhCol.setCellStyle(style);

		 Cell leventhCol = OutSiderow.createCell(11);
		  leventhCol.setCellValue("Facilty Name");
		  leventhCol.setCellStyle(style);

		
		if(travelDetails.isEmpty()){			
			responce.put("status", "No record found");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
		}		
		// iterating r number of rows
		for (int r = 1; r <= travelDetails.size(); r++) {
			OutSiderow = sheet.createRow(rownum++);

			Cell employeeId = OutSiderow.createCell(0);
			employeeId.setCellValue(travelDetails.get(r - 1).getEfmFmUserMaster().getEmployeeId());

			Cell employeeName = OutSiderow.createCell(1);
			employeeName.setCellValue(new String(
					Base64.getDecoder().decode(travelDetails.get(r - 1).getEfmFmUserMaster().getFirstName()), "utf-8"));

			Cell gender = OutSiderow.createCell(2);
			gender.setCellValue(new String(
					Base64.getDecoder().decode(travelDetails.get(r - 1).getEfmFmUserMaster().getGender()), "utf-8"));

			Cell address = OutSiderow.createCell(3);
			address.setCellValue(new String(
					Base64.getDecoder().decode(travelDetails.get(r - 1).getEfmFmUserMaster().getAddress()), "utf-8"));

			Cell areaName = OutSiderow.createCell(4);
			areaName.setCellValue(
					travelDetails.get(r - 1).geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());

			Cell time = OutSiderow.createCell(5);
			if (travelDetails.get(r - 1).getTripType().equalsIgnoreCase("DROP")) {
				time.setCellValue(travelDetails.get(r - 1).getDropSequence());
			} else {
				time.setCellValue((String) shiftFormate.format(travelDetails.get(r - 1).getPickUpTime()));
			}

			Cell tripType = OutSiderow.createCell(6);
			tripType.setCellValue(travelDetails.get(r - 1).getTripType());

			Cell routeName = OutSiderow.createCell(7);
			routeName.setCellValue(
					travelDetails.get(r - 1).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());

			Cell requestDateCol = OutSiderow.createCell(8);
			requestDateCol.setCellValue(requestFormat.format(travelDetails.get(r - 1).getRequestDate()));

			Cell shiftTimeCol = OutSiderow.createCell(9);
			shiftTimeCol.setCellValue(shiftFormate.format(travelDetails.get(r - 1).getShiftTime()));

			Cell nodalPoints = OutSiderow.createCell(10);
			nodalPoints.setCellValue(
					travelDetails.get(r - 1).geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());
			
			Cell combinedFacility = OutSiderow.createCell(11);
			combinedFacility.setCellValue(
			 travelDetails.get(r - 1).geteFmFmClientBranchPO().getBranchName());


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
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return response.build();
	}

	// delete shiftRequest from employee Travel Requests

	@POST
	@Path("/deleteShiftRequest")
	public Response deleteParticularShiftEmployees(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException, UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getEfmFmUserMaster().getUserId());

  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getEfmFmUserMaster().getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}

		
		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
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
		   }
		
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//Date date = dateFormat.parse(employeeTravelRequestPO.getResheduleDate());
		//String requestDate = requestDateFormat.format(date);
		//employeeTravelRequestPO.setRequestDate(new Date());
		//DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		//String shiftDate = employeeTravelRequestPO.getTime();
		List<EFmFmEmployeeTravelRequestPO> travelDetails=null;
		List<EFmFmRoleMasterPO> userRole=userMasterBO.getUserRoleByUserId(employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
		Date date =null; 
		String requestDate="";
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		String shiftDate="";
		Date shift=null;
		java.sql.Time shiftTime=null;
		
		 if(null !=employeeTravelRequestPO.getEmployeeId()){		 
				 try {
						if(employeeTravelRequestPO.getLocationFlg().equalsIgnoreCase("M")){
							travelDetails = iCabRequestBO
									.employeeRequestFromEmpIdByLocationFlg(employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getEmployeeId());
						}else{	
							travelDetails = iCabRequestBO
									.particularEmployeeRequestFromEmpId(employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getEmployeeId());
						}			
				}catch(Exception e) {
					travelDetails = iCabRequestBO
							.particularEmployeeRequestFromEmpId(employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getEmployeeId());
				}	
		 }else{
				
			    date=dateFormat.parse(employeeTravelRequestPO.getResheduleDate());
			    requestDate = requestDateFormat.format(date);
			  //  employeeTravelRequestPO.setRequestDate(new Date());
				if(!(employeeTravelRequestPO.getTime().equalsIgnoreCase("All"))){
					
							  shiftDate = employeeTravelRequestPO.getTime();
							  shift =  shiftFormate.parse(shiftDate);
							  shiftTime = new java.sql.Time(shift.getTime());
							 
				}	
				if(employeeTravelRequestPO.getTripType().equalsIgnoreCase("BOTH") && employeeTravelRequestPO.getTime().equalsIgnoreCase("All")){
					
						if(userRole.get(0).getRole().equalsIgnoreCase("manager")){				
							travelDetails = iCabRequestBO
									.getRequestByDayWiceBothPickAndDropForNormalAndAdhocByManager(
											employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getUserId(),
											requestDate);
						}else{
							travelDetails = iCabRequestBO
									.getRequestByDayWiceBothPickAndDropForNormalAndAdhoc(
											employeeTravelRequestPO.getCombinedFacility(),
											requestDate);
						}
					
				}else if(!(employeeTravelRequestPO.getTripType().equalsIgnoreCase("BOTH")) && employeeTravelRequestPO.getTime().equalsIgnoreCase("All")){
					
							if(userRole.get(0).getRole().equalsIgnoreCase("manager")){				
								travelDetails = iCabRequestBO
										.getRequestByDayWiceForNormalAndAdhocByManager(
												employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getUserId(),
												requestDate,employeeTravelRequestPO.getTripType());
							}else{
								travelDetails = iCabRequestBO
										.getRequestByDayWiceForNormalAndAdhoc(
												employeeTravelRequestPO.getCombinedFacility(),
												requestDate,employeeTravelRequestPO.getTripType());	
							}
							
				}else if(employeeTravelRequestPO.getTripType().equalsIgnoreCase("BOTH") && !(employeeTravelRequestPO.getTime().equalsIgnoreCase("All"))){
					
						if(userRole.get(0).getRole().equalsIgnoreCase("manager")){				
							travelDetails = iCabRequestBO
									.getRequestByDayWiceForNormalAndAdhocByManagerWithoutTripType(
											employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getUserId(),
											requestDate,shiftTime);
							
							
						}else{					
							travelDetails = iCabRequestBO
									.getRequestByShiftWiceForNormalAndAdhocWithoutTripType(
											employeeTravelRequestPO.getCombinedFacility(),
											requestDate,shiftTime);
						}
				}else{
				
				 
				if(userRole.get(0).getRole().equalsIgnoreCase("manager")){				
					travelDetails = iCabRequestBO
							.getRewquestByShiftWiceForNormalAndAdhocByManager(employeeTravelRequestPO.getUserId(),employeeTravelRequestPO.getCombinedFacility(),
									requestDate,shiftTime,employeeTravelRequestPO.getTripType());
				}else{
					travelDetails = iCabRequestBO
							.getRewquestByShiftWiceForNormalAndAdhoc(employeeTravelRequestPO.getCombinedFacility(),
									requestDate, shiftTime, employeeTravelRequestPO.getTripType());
				}
			}
			 
			 
		 
			 
		 }
	
		
	//	if(employeeTravelRequestPO.getTripType().equalsIgnoreCase("BOTH")){
			/*travelDetails = iCabRequestBO
					.getRequestByDayWiceBothPickAndDropForNormalAndAdhoc(
							employeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
									.geteFmFmClientBranchPO().getBranchId(),
							requestDate);	*/
//		}
//		else if(employeeTravelRequestPO.getTime().equalsIgnoreCase("All")){
//			travelDetails = iCabRequestBO
//					.getRequestByDayWiceForNormalAndAdhoc(
//							employeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
//									.geteFmFmClientBranchPO().getBranchId(),
//							requestDate,employeeTravelRequestPO.getTripType());	
//				}
////		else{
//			Date shift = shiftFormate.parse(shiftDate);
//			java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
//			travelDetails = iCabRequestBO
//					.getRewquestByShiftWiceForNormalAndAdhoc(
//							employeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
//									.geteFmFmClientBranchPO().getBranchId(),
//							requestDate, shiftTime, employeeTravelRequestPO.getTripType());
//		}
		
		log.info("requestDate" + requestDate  + "Request Size" + travelDetails.size());		
		if (!(travelDetails.isEmpty())) {
			for (EFmFmEmployeeTravelRequestPO allTravelRequest : travelDetails) {
				allTravelRequest.setReadFlg("N");
				allTravelRequest.setIsActive("Y");
				allTravelRequest.setRequestStatus("C");
				iCabRequestBO.update(allTravelRequest);
				responce.put("status", "success");
			}

		}else{
			responce.put("status", "failed");
		}
		
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	// Get shift wise employee requests
	@POST
	@Path("/employeeshiftwiserequest")
	public Response employeeShiftWiseequestDetails(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException, UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		if(employeeTravelRequestPO.getStartPgNo()==0){
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
		if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
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
		   }
		}
	ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftDateFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		DateFormat shiftTimeFormaters = new SimpleDateFormat("HH:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		System.out.println("employeeTravelRequestPO.getResheduleDate()"+employeeTravelRequestPO.getResheduleDate());
//		System.out.println("employeeTravelRequestPO.getTime()"+employeeTravelRequestPO.getTime());
		Date date = dateFormat.parse(employeeTravelRequestPO.getResheduleDate());
		String requestDate = requestDateFormat.format(date);
		employeeTravelRequestPO.setRequestDate(new Date());
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		String shiftDate = employeeTravelRequestPO.getTime();
		List<EFmFmEmployeeTravelRequestPO> travelDetails=null;
		
		 List<EFmFmRoleMasterPO> userRole=userMasterBO.getUserRoleByUserId(employeeTravelRequestPO.getUserId());
		 
		if(employeeTravelRequestPO.getTripType().equalsIgnoreCase("BOTH")){
			if(userRole.get(0).getRole().equalsIgnoreCase("manager")){				
				travelDetails = iCabRequestBO
						.getRequestByDayWiceBothPickAndDropForNormalAndAdhocByManager(
								employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getUserId(),
								requestDate,employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
			}else{
				travelDetails = iCabRequestBO
						.getRequestByDayWiceBothPickAndDropForNormalAndAdhoc(
								employeeTravelRequestPO.getCombinedFacility(),
								requestDate,employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
			}
		}
		else if(employeeTravelRequestPO.getTime().equalsIgnoreCase("All")){
			
					if(userRole.get(0).getRole().equalsIgnoreCase("manager")){				
						travelDetails = iCabRequestBO
								.getRequestByDayWiceForNormalAndAdhocByManager(
										employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getUserId(),
										requestDate,employeeTravelRequestPO.getTripType(),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
					}else{
						travelDetails = iCabRequestBO
								.getRequestByDayWiceForNormalAndAdhoc(
										employeeTravelRequestPO.getCombinedFacility(),
										requestDate,employeeTravelRequestPO.getTripType(),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());	
					}
				}
		else{
			
			Date shift = shiftFormate.parse(shiftDate);
			java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
			if(userRole.get(0).getRole().equalsIgnoreCase("manager")){				
				travelDetails = iCabRequestBO
						.getRewquestByShiftWiceForNormalAndAdhocByManager(employeeTravelRequestPO.getUserId(),employeeTravelRequestPO.getCombinedFacility(),
								requestDate,shiftTime,employeeTravelRequestPO.getTripType(),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
			}else{
				travelDetails = iCabRequestBO
						.getRewquestByShiftWiceForNormalAndAdhoc(employeeTravelRequestPO.getCombinedFacility(),
								requestDate, shiftTime, employeeTravelRequestPO.getTripType(),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
			}
		}
		
		log.info("requestDate" + requestDate  + "Request Size" + travelDetails.size());
		List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
		if (!(travelDetails.isEmpty())) {
			for (EFmFmEmployeeTravelRequestPO allTravelRequest : travelDetails) {
				Map<String, Object> requestList = new HashMap<String, Object>();
				requestList.put("employeeId",
						allTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getEmployeeId());
				requestList.put("requestId", allTravelRequest.getRequestId());
				String cabAvailableTime = formatter.format(allTravelRequest.getRequestDate()) + " "
						+ allTravelRequest.getShiftTime();
				if (allTravelRequest.geteFmFmEmployeeRequestMaster().getTripType().equalsIgnoreCase("DROP")) {
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
				requestList.put("tripDate", formatter.format(allTravelRequest.getRequestDate()));
				requestList.put("weekOffs", allTravelRequest.getEfmFmUserMaster().getWeekOffDays());
				requestList.put("facilityName", allTravelRequest.geteFmFmClientBranchPO().getBranchName());
				requestList.put("nodalPoints",
						allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints());
				requestList.put("nodalPointId",
						allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointId());
				requestList.put("nodalPointTitle",
						allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());
				requestList.put("nodalPointDescription", allTravelRequest.geteFmFmRouteAreaMapping()
						.geteFmFmNodalAreaMaster().getNodalPointDescription());

				requestList.put("tripType", allTravelRequest.geteFmFmEmployeeRequestMaster().getTripType());
				requestList.put("requestType", allTravelRequest.getRequestType());
				requestList.put("tripTime", shiftTimeFormaters.format(allTravelRequest.getShiftTime()));
				requestList.put("employeeRouteName",
						allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				requestList.put("employeeRouteId",
						allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
				requestList.put("employeeAreaName",
						allTravelRequest.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
				requestList.put("employeeAreaId",
						allTravelRequest.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaId());
				requestList.put("employeePickUpTime", allTravelRequest.geteFmFmEmployeeRequestMaster().getPickUpTime());
				requestList.put("employeeWaypoints",
						allTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getLatitudeLongitude());
				requestList.put("employeeName", new String(
						Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
				requestList.put("employeeAddress", new String(
						Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
				travelRequestList.add(requestList);
			}

		}
		responce.put("requests", travelRequestList);
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Search particular employee master request entry from employeeId
	 */

	@POST
	@Path("/emplyeeMasteRequestSearch")
	public Response searchEmployeeBulkRequestFromEmpId(EFmFmEmployeeTravelRequestPO travelRequest)
			throws ParseException, UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequest.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}
		
		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(travelRequest.getUserId());
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
		   }

		
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + travelRequest.getUserId());
		travelRequest.setRequestDate(new Date());
		List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
		DateFormat shiftDateFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		List<EFmFmUserMasterPO> guestIdExistsCheck = iEmployeeDetailBO
				.getEmpDetailsFromEmployeeId(travelRequest.getEmployeeId());
		if ((!(guestIdExistsCheck.isEmpty()))) {
			List<EFmFmEmployeeRequestMasterPO> allRequestDetails = iCabRequestBO
					.getActiveRequestDetailsFromBranchIdAndUserIdForAnEmployee(travelRequest.getCombinedFacility(),
							guestIdExistsCheck.get(0).getUserId());
			if ((!(allRequestDetails.isEmpty()))) {
				for (EFmFmEmployeeRequestMasterPO requestMasterPO : allRequestDetails) {
					Map<String, Object> requests = new HashMap<String, Object>();
					requests.put("userId", requestMasterPO.getEfmFmUserMaster().getUserId());
					requests.put("tripId", requestMasterPO.getTripId());
					requests.put("employeeId", requestMasterPO.getEfmFmUserMaster().getEmployeeId());
					requests.put("requestDate", shiftDateFormater.format(requestMasterPO.getRequestDate()));

					if (requestMasterPO.getTripType().equalsIgnoreCase("PICKUP"))
						requests.put("pickupTime", requestMasterPO.getPickUpTime());
					else
						requests.put("pickupTime", requestMasterPO.getDropSequence());
					requests.put("requestType", requestMasterPO.getRequestType());
					requests.put("status", requestMasterPO.getStatus());
					if(null==requestMasterPO.getEfmFmUserMaster().getAddress()){
							requests.put("address","");
						}else{
							requests.put("address", new String(
									Base64.getDecoder().decode(requestMasterPO.getEfmFmUserMaster().getAddress()), "utf-8"));
						}
				
					requests.put("routeName",
							requestMasterPO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					requests.put("areaName",
							requestMasterPO.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
					requests.put("shiftTime", requestMasterPO.getShiftTime());
					requests.put("requestStartDate",
							shiftDateFormater.format(requestMasterPO.getTripRequestStartDate()));
					requests.put("requestEndDate", shiftDateFormater.format(requestMasterPO.getTripRequestEndDate()));
					travelRequestList.add(requests);
				}
			}
		}
		responce.put("requests", travelRequestList);
		 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Search particular employee request from employeeId
	 */

	@POST
	@Path("/emplyeerequestsearch")
	public Response searchEmployeeRequestFromEmpId(EFmFmEmployeeTravelRequestPO travelRequest)
			throws ParseException, UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequest.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}
		
		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(travelRequest.getUserId());
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
		   }

		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + travelRequest.getUserId());
		travelRequest.setRequestDate(new Date());
		List<EFmFmEmployeeTravelRequestPO> employeeTravelRequest = null;	
		try {
				if(travelRequest.getLocationFlg().equalsIgnoreCase("M")){
					employeeTravelRequest = iCabRequestBO
							.employeeRequestFromEmpIdByLocationFlg(travelRequest.getCombinedFacility(),travelRequest.getEmployeeId());
				}else{	
					employeeTravelRequest = iCabRequestBO
							.particularEmployeeRequestFromEmpId(travelRequest.getCombinedFacility(), travelRequest.getEmployeeId());
				}			
		}catch(Exception e) {
			employeeTravelRequest = iCabRequestBO
					.particularEmployeeRequestFromEmpId(travelRequest.getCombinedFacility(), travelRequest.getEmployeeId());
		}		
		List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftDateFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		DateFormat shiftTimeFormaters = new SimpleDateFormat("HH:mm:ss");
		if (!(employeeTravelRequest.isEmpty())) {
			for (EFmFmEmployeeTravelRequestPO allTravelRequest : employeeTravelRequest) {
				Map<String, Object> requestList = new HashMap<String, Object>();
				requestList.put("employeeId",
						allTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getEmployeeId());
				requestList.put("requestId", allTravelRequest.getRequestId());
				String cabAvailableTime = formatter.format(allTravelRequest.getRequestDate()) + " "
						+ allTravelRequest.getShiftTime();
				if (allTravelRequest.geteFmFmEmployeeRequestMaster().getTripType().equalsIgnoreCase("DROP")) {
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
				requestList.put("tripDate", formatter.format(allTravelRequest.getRequestDate()));
				requestList.put("weekOffs", allTravelRequest.getEfmFmUserMaster().getWeekOffDays());
				requestList.put("facilityName", allTravelRequest.geteFmFmClientBranchPO().getBranchName());
				requestList.put("nodalPoints",
						allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints());
				requestList.put("nodalPointId",
						allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointId());
				requestList.put("nodalPointTitle",
						allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());
				requestList.put("nodalPointDescription", allTravelRequest.geteFmFmRouteAreaMapping()
						.geteFmFmNodalAreaMaster().getNodalPointDescription());

				requestList.put("tripType", allTravelRequest.geteFmFmEmployeeRequestMaster().getTripType());
				requestList.put("requestType", allTravelRequest.getRequestType());
				requestList.put("tripTime", shiftTimeFormaters.format(allTravelRequest.getShiftTime()));
				requestList.put("employeeRouteName",
						allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				requestList.put("employeeRouteId",
						allTravelRequest.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
				requestList.put("employeeAreaName",
						allTravelRequest.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
				requestList.put("employeeAreaId",
						allTravelRequest.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaId());
				requestList.put("employeePickUpTime", allTravelRequest.geteFmFmEmployeeRequestMaster().getPickUpTime());
				requestList.put("employeeName", new String(
						Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
				requestList.put("employeeAddress", new String(
						Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));
				// requestList.put("employeeWaypoints",
				// allTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getLatitudeLongitude());
				travelRequestList.add(requestList);
			}
		}
		responce.put("requests", travelRequestList);
		 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * The shifTime method implemented. for getting the list of shift timing for
	 * travel desk .
	 *
	 * @author Sarfraz Khan
	 * 
	 */
	@POST
	@Path("/shiftTimeForTraveDesk")
	public Response shifTimeForTravelDeskEditPop(EFmFmEmployeeTravelRequestPO employeeRequest) {
		
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
	
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeRequest.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}
		
		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeRequest.getUserId());
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
		   }

		
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + employeeRequest.getUserId());
		employeeRequest.setRequestDate(new Date());
		List<Map<String, Object>> shitTimings = new ArrayList<Map<String, Object>>();
		List<Time> shiftTimeDetails = iCabRequestBO.listOfAllShiftTimesFromTravelDesk(employeeRequest.getCombinedFacility(),
				employeeRequest.getTripType());
		if (!(shiftTimeDetails.isEmpty())) {
			for (Time shiftiming : shiftTimeDetails) {
				Map<String, Object> shifList = new HashMap<String, Object>();
				shifList.put("shiftTime", shiftTimeFormater.format(shiftiming));
				shifList.put("tripType", employeeRequest.getTripType());
				shitTimings.add(shifList);
			}
		}
		responce.put("status", "success");
		responce.put("shift", shitTimings);
		 log.info("serviceEnd -UserId :" + employeeRequest.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * update Shift time value for Adhoc and guest requests
	 */

	@POST
	@Path("/updateShiftTimeForRequest")
	public Response updateRouteSquencing(EFmFmAssignRoutePO assignRoutePO) throws ParseException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}
		
		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
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
		   }

		
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		List<EFmFmEmployeeTravelRequestPO> cabRequestDetail = iCabRequestBO.getParticularRequestDetailOnTripComplete(assignRoutePO.getRequestId());
		DateFormat timeformate = new SimpleDateFormat("HH:mm");
		String pickUpTime = assignRoutePO.getTime();
		Date changePickUpTime = timeformate.parse(pickUpTime);
		java.sql.Time shiftTime = new java.sql.Time(changePickUpTime.getTime());
		if (!(cabRequestDetail.isEmpty())) {
			cabRequestDetail.get(0).setShiftTime(shiftTime);
			iCabRequestBO.update(cabRequestDetail.get(0));
		}
		List<EFmFmEmployeeRequestMasterPO> requestDetails = iCabRequestBO.getRequestFromRequestMaster(
				cabRequestDetail.get(0).geteFmFmEmployeeRequestMaster().getTripId(),
				assignRoutePO.getCombinedFacility());
		if (!(requestDetails.isEmpty())) {
			requestDetails.get(0).setShiftTime(shiftTime);
			iCabRequestBO.update(requestDetails.get(0));
		}
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/**
	 * The shifTime method implemented. for getting the list of shif timing from
	 * shift time table.
	 *
	 * @author Sarfraz Khan
	 * 
	 */
	@POST
	@Path("/shiftime")
	public Response shifTime(EFmFmEmployeeTravelRequestPO employeeRequest) {
		
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info(employeeRequest.getCombinedFacility()+"Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeRequest.getUserId()))){
//
//			responce.put("status", "invalidRequest");
//			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		}}catch(Exception e){
//			log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		}
//		
//		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeRequest.getUserId());
//		   if (!(userDetailToken.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
//		     userDetailToken.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetailToken.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }

		
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + employeeRequest.getUserId());
		employeeRequest.setRequestDate(new Date());
		List<Map<String, Object>> shitTimings = new ArrayList<Map<String, Object>>();
		DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
		List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO
				.listOfShiftTime(employeeRequest.getCombinedFacility());
		if (!(shiftTimeDetails.isEmpty())) {
			for (EFmFmTripTimingMasterPO shiftiming : shiftTimeDetails) {
				Map<String, Object> shifList = new HashMap<String, Object>();
				shifList.put("shiftTime", shiftTimeFormater.format(shiftiming.getShiftTime()));
				shifList.put("shiftId", shiftiming.getShiftId());
				shifList.put("shiftType", shiftiming.getShiftType());
				shifList.put("tripType", shiftiming.getTripType());
				shifList.put("facilityName",shiftiming.geteFmFmClientBranchPO().getBranchName());

				shifList.put("bufferTime", shiftiming.getShiftBufferTime());
				shifList.put("cutOffTime", shiftTimeFormater.format(shiftiming.getCutOffTime()));
				shifList.put("CancelCutOffTime", shiftTimeFormater.format(shiftiming.getCancelCutoffTime()));
				shifList.put("RescheduleCutOffTime", shiftTimeFormater.format(shiftiming.getRescheduleCutOffTime()));
				shifList.put("mobileVisibleFlg", shiftiming.getMobileVisibleFlg());				
				shifList.put("ceilingFlg", shiftiming.getCeilingFlg());
				shifList.put("ceilingNo", shiftiming.getCeilingNo());
				shifList.put("bufferCeilingNo", shiftiming.getBufferCeilingNo());
				shifList.put("genderPreference", shiftiming.getGenderPreference());
				shitTimings.add(shifList);
			}
		}
		responce.put("status", "success");
		responce.put("shift", shitTimings);
		 log.info("serviceEnd -UserId :" + employeeRequest.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * The shifTime method implemented. for getting the list of shift timing
	 * from shift time table according to the Trip Type DropDown
	 *
	 * @author Sarfraz Khan
	 * 
	 */
	@POST
	@Path("/tripshiftime")
	public Response shifTimeByTripType(EFmFmEmployeeTravelRequestPO employeeRequest) {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + employeeRequest.getUserId());
		Map<String, Object> responce = new HashMap<String, Object>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");				 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeRequest.getUserId()))){
//
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}
//		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeRequest.getUserId());
//		   if (!(userDetailToken.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
//		     userDetailToken.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetailToken.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
		 
		employeeRequest.setRequestDate(new Date());
		List<Map<String, Object>> shitTimings = new ArrayList<Map<String, Object>>();
		DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
		List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO.listOfShiftTimeByTripType(employeeRequest.getCombinedFacility(),
				employeeRequest.getTripType());
		if (!(shiftTimeDetails.isEmpty())) {
			for (EFmFmTripTimingMasterPO shiftiming : shiftTimeDetails) {
				Map<String, Object> shifList = new HashMap<String, Object>();
				shifList.put("shiftTime", shiftTimeFormater.format(shiftiming.getShiftTime()));
				shifList.put("tripType", shiftiming.getTripType());
				shifList.put("bufferTime", shiftiming.getShiftBufferTime());
				shifList.put("shiftId", shiftiming.getShiftId());
				shifList.put("bufferTime", shiftiming.getShiftBufferTime());
				shifList.put("cutOffTime", shiftTimeFormater.format(shiftiming.getCutOffTime()));
				shitTimings.add(shifList);
			}
		}
		responce.put("status", "success");
		responce.put("shift", shitTimings);
		 log.info("serviceEnd -UserId :" + employeeRequest.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	// cutOffTime functionality handled here
	public String cutOffTimeValidation(String tripType, String StartDate, Time shiftTime, int branchId,
			String CutOffFlag, Time cutoffTime) throws ParseException {
		String response = "notValidShiftTime";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date empRequestDate = dateFormat.parse(StartDate);
		Calendar todayDateTime = Calendar.getInstance();
		todayDateTime.setTime(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(shiftTime);
		Calendar ShiftDateTime = Calendar.getInstance();
		ShiftDateTime.setTime(dateFormat.parse(dateFormat.format(empRequestDate)));
		ShiftDateTime.add(Calendar.HOUR, calendar.get(Calendar.HOUR_OF_DAY));
		ShiftDateTime.add(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
		ShiftDateTime.add(Calendar.SECOND, calendar.get(Calendar.SECOND));
		if ("S".equalsIgnoreCase(CutOffFlag)) {
			calendar.setTime(cutoffTime);
			ShiftDateTime.add(Calendar.HOUR, -calendar.get(Calendar.HOUR_OF_DAY));
			ShiftDateTime.add(Calendar.MINUTE, -calendar.get(Calendar.MINUTE));
			ShiftDateTime.add(Calendar.SECOND, -calendar.get(Calendar.SECOND));
		} else if ("T".equalsIgnoreCase(CutOffFlag)) {
			if (tripType.equalsIgnoreCase("DROP")) {
				ShiftDateTime.add(Calendar.HOUR, -cutoffTime.getHours());
				ShiftDateTime.add(Calendar.MINUTE, -cutoffTime.getMinutes());
			} else if (tripType.equalsIgnoreCase("PICKUP")) {
				ShiftDateTime.add(Calendar.HOUR, -cutoffTime.getHours());
				ShiftDateTime.add(Calendar.MINUTE, -cutoffTime.getMinutes());
			}
		}
		log.info("request b4 that " + dateTimeFormat.format(ShiftDateTime.getTime()));
		if (!dateFormat.parse(StartDate).before(dateFormat.parse(dateFormat.format(new Date())))) {
			if (dateFormat.parse(StartDate).after(dateFormat.parse(dateFormat.format(new Date())))
					|| dateFormat.parse(StartDate).equals(dateFormat.parse(dateFormat.format(new Date())))) {
				if (todayDateTime.getTime().before(ShiftDateTime.getTime())
						|| todayDateTime.getTime().equals(ShiftDateTime.getTime())) {
					log.info("valid time to rise the request");
					response = "validShiftTime";
				} else {
					response = "notValidShiftTime";
				}
			}
		} else {
			response = "notValidShiftTime";
		}		 
		return response;
	}

	/**
	 * The shifTime method implemented. adding shift timings time table.
	 *
	 * @author Sarfraz Khan
	 * @throws ParseException
	 * 
	 */
	@POST
	@Path("/addshiftime")
	public Response addShifTime(EFmFmTripTimingMasterPO shiftTimingDetail) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + shiftTimingDetail.getUserId());
		Map<String, Object> responce = new HashMap<String, Object>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
	 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),shiftTimingDetail.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(shiftTimingDetail.getUserId());
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
		   }
		   
		   String status="Input";
		     StringBuffer temp=new StringBuffer("PleaseCheck::");
		         if(true)
		         {
		        	 if(shiftTimingDetail.getTripType()==null||shiftTimingDetail.getTripType().isEmpty())
		        	 { temp.append("::Trip Type cannot be empty");
		 	           status="Fail";	        		 
		        	 }
		        	 else{
		        		 CharSequence tripType=shiftTimingDetail.getTripType();
		    	         Matcher matcher= Validator.pickUpOrDrop(tripType);
		        		 if(!matcher.matches())
		        		 {temp.append("::Trip Type can be only be pickup or drop");
			 	           status="Fail";	        			 
		        		 }
		    	        }
		        	 
		        	 if(shiftTimingDetail.getShiftBufferTime()%5!=0||shiftTimingDetail.getShiftBufferTime()>30||shiftTimingDetail.getShiftBufferTime()<=0)
		        	 {temp.append("::Shift Buffer time should be multiple of 5 and max buffer time can be 30 minutes");
		 	           status="Fail";	        		 
		        	 }
		        	 
		        	 if(shiftTimingDetail.getTime()==null)
		        	 {temp.append("::shift time cannot be empty");
		 	           status="Fail";	        		 
		        	 } 
		        	 else{CharSequence shiftTime=shiftTimingDetail.getTime();
	    	         Matcher matcher= Validator.time(shiftTime);
	    	         if(!matcher.matches())
	    	         {temp.append("::shit time should be in HH:MM:00 format(seconds should be 00)");
		 	           status="Fail";
	    	        }
		        	}
		        	 
		        	 if(shiftTimingDetail.getCeilingNo()<0)
		        	 {temp.append("::Ceiling number should be positive Integer");
		 	           status="Fail";
		             }    
		        	 if(shiftTimingDetail.getBufferCeilingNo()<0)
		        	 {temp.append(":: Awaited passenger Ceiling number should be positive Integer");
		 	           status="Fail";
		             }  
		        	 
		        	
//		        	 if(shiftTimingDetail.getCancel_Cut_Off_Time()==null)
//		        	 {temp.append("::Cancel cut off time cannot be empty");
//		 	           status="Fail";	        		 
//		        	 }	 
//		        	 else{CharSequence cancelCutOff=(CharSequence) shiftTimingDetail.getCancel_Cut_Off_Time();
//	    	         Matcher matcher= Validator.time(cancelCutOff);
//	    	         if(!matcher.matches())
//	    	         {temp.append("::cancel cut off time should be in HH:MM:00 format(seconds should be 00)");
//		 	           status="Fail";
//	    	        }
//		        	 }
		        	 
		            if(shiftTimingDetail.getCut_Off_Time()==null)
		        	 {temp.append("::Cut off time cannot be empty");
		 	           status="Fail";	        		 
		        	 }	
		            else{CharSequence cutOffTime=(CharSequence) shiftTimingDetail.getCut_Off_Time();
	   	         Matcher matcher= Validator.time(cutOffTime);
	   	         if(!matcher.matches())
	   	         {temp.append(":: cut off time should be in HH:MM:00 format(seconds should be 00)");
		 	           status="Fail";
	   	        }
		        	 }
		            
		        	 if(shiftTimingDetail.getReschedule_Cut_Off_Time()==null)
		        	 {temp.append("::Reschedule cut off time cannot be empty");
		 	           status="Fail";	        		 
		        	 }	
		        	 else{CharSequence rescheduleCutOffTime=(CharSequence) shiftTimingDetail.getReschedule_Cut_Off_Time();
		   	         Matcher matcher= Validator.time(rescheduleCutOffTime);
		   	         if(!matcher.matches())
		   	         {temp.append(":: RescheduleCutOffTime should be in HH:MM:00 format(seconds should be 00)");
			 	           status="Fail";
		   	        }
			        	 }
		        	 if(status.equals("Fail"))
		        	    {
		        	    log.info("Invalid input:");
		        	    responce.put("invalidInput", temp);
		        	       return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		        	       }
		        	 
		        	 
		        	 if(shiftTimingDetail.getGenderPreference()==null||shiftTimingDetail.getGenderPreference().isEmpty())
		        	 { temp.append("::Gender Preference cannot be empty");
		 	           status="Fail";	        		 
		        	 }
		        	 else{
		        		 CharSequence genderPreference=shiftTimingDetail.getGenderPreference();
		    	         Matcher matcher= Validator.genderPreference(genderPreference);
		        		 if(!matcher.matches())
		        		 {temp.append("::Gender Preference can be only be Y -Yes or N- No ");
			 	           status="Fail";	        			 
		        		 }
		    	        }
		        }
		   
		 
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		String shiftDate = shiftTimingDetail.getTime();
		Date shift = shiftFormate.parse(shiftDate);
		java.sql.Time shiftTime = new java.sql.Time(shift.getTime());

		Date cutOffTime = shiftFormate.parse(shiftTimingDetail.getCut_Off_Time());
		java.sql.Time cut_Off_Time = new java.sql.Time(cutOffTime.getTime());

		Date CancelCutOffTime = shiftFormate.parse(shiftTimingDetail.getCancel_Cut_Off_Time());
		java.sql.Time Can_cut_Off_Time = new java.sql.Time(CancelCutOffTime.getTime());

		Date reschedule_Cut_Off_Time = shiftFormate.parse(shiftTimingDetail.getReschedule_Cut_Off_Time());
		java.sql.Time Reschedule_Cut_Off_Time = new java.sql.Time(reschedule_Cut_Off_Time.getTime());

		StringTokenizer stringTokenizer = new StringTokenizer(shiftTimingDetail.getCombinedFacility(), ",");
        String branchId = "";
		while (stringTokenizer.hasMoreElements()) {
			branchId = (String) stringTokenizer.nextElement();
			List<EFmFmTripTimingMasterPO> shiftTimeDetail = iCabRequestBO.getParticularShiftTimeDetailByTripType(branchId, shiftTime, shiftTimingDetail.getTripType());
		if (shiftTimeDetail.isEmpty()) {
		EFmFmClientBranchPO eFmFmClientBranchPO=new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(Integer.valueOf(branchId));
		
		EFmFmTripTimingMasterPO shiftDetails=new EFmFmTripTimingMasterPO();		
		shiftDetails.setTripType(shiftTimingDetail.getTripType());
		shiftDetails.setShiftBufferTime(shiftTimingDetail.getShiftBufferTime());
		shiftDetails.setShiftTime(shiftTime);
		shiftDetails.setCutOffTime(cut_Off_Time);
		shiftDetails.setCancelCutoffTime(Can_cut_Off_Time);
		shiftDetails.setRescheduleCutOffTime(Reschedule_Cut_Off_Time);
		shiftDetails.setAreaGeoFenceRegion(0);
		shiftDetails.setClusterGeoFenceRegion(2000);
		shiftDetails.setRouteGeoFenceRegion(2000);
		shiftDetails.setShiftType(shiftTimingDetail.getShiftType());
		shiftDetails.setGenderPreference(shiftTimingDetail.getGenderPreference());
		shiftDetails.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		shiftDetails.setCeilingFlg(shiftTimingDetail.getCeilingFlg());
		shiftDetails.setMobileVisibleFlg(shiftTimingDetail.getMobileVisibleFlg());
		shiftDetails.setIsActive("Y");
		iCabRequestBO.save(shiftDetails);
		 log.info("serviceEnd -UserId :" + shiftTimingDetail.getUserId());
		}
		}
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + shiftTimingDetail.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	
		
	/*
	 * Guest request for cab from Web console
	 * 
	 * 
	 */

	@POST
	@Path("/requestforguest")
	public Response cabRequestForGuest(EFmFmEmployeeRequestMasterPO travelRequest) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		if ( null == travelRequest.getCombinedFacility()) {
			responce.put("status", "combinedFacilityShould be Empty");
			log.info("backDateRequest");
			 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}	
		List<EFmFmClientBranchPO> clientBranch = userMasterBO
				.getClientDetails(travelRequest.getCombinedFacility());
		if(clientBranch.isEmpty()){
			responce.put("status", "combinedFacility is not available");
			log.info("backDateRequest");
			 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}else if(clientBranch.size()>1){
			responce.put("status", "multiple combinedFacility not allowed");
			log.info("backDateRequest");
			 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
/*		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequest.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(travelRequest.getUserId());
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
		   }
		 */
		     
		 
		 log.info("serviceStart -UserId :" + travelRequest.getUserId());
		 String reportMngEmailId="",clientProjectId=" ";
		 String shiftDate ="";					
		 java.sql.Time shiftTime = null;
		 Date shift =null,startDate=null,endDate=null;
		 
		List<EFmFmUserMasterPO> guestIdExistsCheck = iEmployeeDetailBO.getEmpDetailsFromEmployeeId(
				travelRequest.getEmployeeId());
		if (!(guestIdExistsCheck.isEmpty())) {
			if (guestIdExistsCheck.get(0).getUserType().equalsIgnoreCase("employee")) {
				responce.put("status", "alreadyRegisterAsEmp");
				 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		}
		EFmFmRouteAreaMappingPO routeAreaDetails = new EFmFmRouteAreaMappingPO();
		
		try {
			DateFormat timeformate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
			if(travelRequest.getMultipleRequestFlg()==0){
				try {
					shiftDate = travelRequest.getTime();
					shift = shiftFormate.parse(shiftDate);
					shiftTime = new java.sql.Time(shift.getTime());
					final java.sql.Time mailShiftTime =new java.sql.Time(shift.getTime());
					startDate = timeformate.parse(travelRequest.getStartDate() + " " + "00:00");
					endDate = timeformate.parse(travelRequest.getEndDate() + " " + "23:59");
				} catch (Exception e) {
					log.debug("basic shiftTimeLogic");
				}
				if(travelRequest.getTripType().equalsIgnoreCase("BOTH") ){
					String requestDateShiftTime = travelRequest.getStartDate() + " " + travelRequest.getPickupTime();
					Date resheduleDateAndTime = timeformate.parse(requestDateShiftTime);
					if (resheduleDateAndTime.getTime() < new Date().getTime()) {
						responce.put("status", "backDateRequest");
						log.info("backDateRequest");
						 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
				}else{
					String requestDateShiftTime = travelRequest.getStartDate() + " " + travelRequest.getTime();
					Date resheduleDateAndTime = timeformate.parse(requestDateShiftTime);
					if (resheduleDateAndTime.getTime() < new Date().getTime()) {
						responce.put("status", "backDateRequest");
						log.info("backDateRequest");
						 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
				}
				
				
			}
			if (!(guestIdExistsCheck.isEmpty())) {
				
				List<EFmFmEmployeeRequestMasterPO> existRequestsInReqMaster = iCabRequestBO
						.getEmplyeeRequestsForSameDateAndShiftTime(startDate, shiftTime,
								travelRequest.getCombinedFacility(),
								guestIdExistsCheck.get(0).getUserId(), travelRequest.getTripType());
				if (!(existRequestsInReqMaster.isEmpty())) {
					responce.put("status", "alreadyRaised");
					 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}

				// Emp detail From UserId
				EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO = new EFmFmClientProjectDetailsPO();
				guestIdExistsCheck.get(0).setEmployeeId(travelRequest.getEmployeeId());

				PasswordEncryption passwordEncryption = new PasswordEncryption();
				guestIdExistsCheck.get(0)
						.setPassword(passwordEncryption.PasswordEncoderGenerator(travelRequest.getEmployeeId()));
				guestIdExistsCheck.get(0).setUserName(travelRequest.getEmployeeId());
				guestIdExistsCheck.get(0).setHostMobileNumber(
						Base64.getEncoder().encodeToString(travelRequest.getHostMobileNumber().getBytes("utf-8")));
				if(null!=travelRequest.getGuestMiddleLoc()){
				guestIdExistsCheck.get(0).setGuestMiddleLoc(
						Base64.getEncoder().encodeToString(travelRequest.getGuestMiddleLoc().getBytes("utf-8")));
				}
				guestIdExistsCheck.get(0).setFirstName(
						Base64.getEncoder().encodeToString(travelRequest.getFirstName().getBytes("utf-8")));
				guestIdExistsCheck.get(0)
						.setLastName(Base64.getEncoder().encodeToString(travelRequest.getLastName().getBytes("utf-8")));
				guestIdExistsCheck.get(0).setGender(
						Base64.getEncoder().encodeToString(travelRequest.getGender().toUpperCase().getBytes("utf-8")));
				guestIdExistsCheck.get(0).setMobileNumber(
						Base64.getEncoder().encodeToString(travelRequest.getMobileNumber().getBytes("utf-8")));
				guestIdExistsCheck.get(0)
						.setEmailId(Base64.getEncoder().encodeToString(travelRequest.getEmailId().getBytes("utf-8")));
				if(null!=travelRequest.getAddress())
				guestIdExistsCheck.get(0)
						.setAddress(Base64.getEncoder().encodeToString(travelRequest.getAddress().getBytes("utf-8")));

				guestIdExistsCheck.get(0).setStatus("Y");
				guestIdExistsCheck.get(0).setUpdatedTime(new Date());
				guestIdExistsCheck.get(0).setLastLoginTime(new Date());
				EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
				eFmFmClientBranch.setBranchId(Integer.parseInt(travelRequest.getCombinedFacility()));
				guestIdExistsCheck.get(0).seteFmFmClientBranchPO(eFmFmClientBranch);
				eFmFmClientProjectDetailsPO.setProjectId(1);
				guestIdExistsCheck.get(0).seteFmFmClientProjectDetails(eFmFmClientProjectDetailsPO);
				if(null!=travelRequest.getLatitudeLongitude())
				guestIdExistsCheck.get(0).setLatitudeLongitude(travelRequest.getLatitudeLongitude());
				List<EFmFmClientBranchPO> clientBranchDetails = userMasterBO
						.getClientDetails(travelRequest.getCombinedFacility());
				CalculateDistance empDistance = new CalculateDistance();
				if(null!=travelRequest.getLatitudeLongitude()){
					try {
						guestIdExistsCheck.get(0).setDistance(empDistance.employeeDistanceCalculation(
								clientBranchDetails.get(0).getLatitudeLongitude(), travelRequest.getLatitudeLongitude()));
					} catch (Exception e) {
					}
				}
				//Shell changes
				if(travelRequest.getRouteAreaId() >0){
					routeAreaDetails.setRouteAreaId(travelRequest.getRouteAreaId());
				}else{
					routeAreaDetails.setRouteAreaId(1);
				}
				
				guestIdExistsCheck.get(0).seteFmFmRouteAreaMapping(routeAreaDetails);
				userMasterBO.update(guestIdExistsCheck.get(0));

				// Emp detail From EmployeeId
				List<EFmFmUserMasterPO> employeeDetailFromEmpId = iEmployeeDetailBO
						.getParticularEmpDetailsFromEmployeeId(travelRequest.getEmployeeId(),travelRequest.getCombinedFacility());

				travelRequest
						.setEmployeeId(employeeDetailFromEmpId.get(employeeDetailFromEmpId.size() - 1).getEmployeeId());
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
				efmFmUserMaster.setUserId(employeeDetailFromEmpId.get(0).getUserId());
				EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO = new EFmFmRouteAreaMappingPO();
				if(travelRequest.getRouteAreaId() >0){
					eFmFmRouteAreaMappingPO.setRouteAreaId(travelRequest.getRouteAreaId());
				}else{
					eFmFmRouteAreaMappingPO.setRouteAreaId(employeeDetailFromEmpId.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
				}
				eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);
				eFmFmEmployeeReqMasterPO.setStatus("Y");
				eFmFmEmployeeReqMasterPO.setRequestType("guest");
				eFmFmEmployeeReqMasterPO.setReadFlg("Y");								
				eFmFmEmployeeReqMasterPO.seteFmFmClientBranchPO(eFmFmClientBranch);
				eFmFmEmployeeReqMasterPO.setRequestDate(new Date());
				
				if(travelRequest.getMultipleRequestFlg()==1){					
									
					travelRequest.setLocationFlg("M");								
					try {
						List<EFmFmEmployeeRequestMasterPO> tripRequestdata = travelRequest.getTripRequests();		   	 
					   	 for(EFmFmEmployeeRequestMasterPO values:tripRequestdata){
					   		eFmFmEmployeeReqMasterPO = new EFmFmEmployeeRequestMasterPO();
							eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);
					   		 if(values.getTripType().equalsIgnoreCase("BOTH") 
					   				 || values.getTripType().equalsIgnoreCase("PICKUP") || values.getTripType().equalsIgnoreCase("DROP")){
								if(values.getTripType().equalsIgnoreCase("BOTH")){
									for(int shiftId=0; shiftId<=1; shiftId++){
										eFmFmEmployeeReqMasterPO = new EFmFmEmployeeRequestMasterPO();
										eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);
										if(0==shiftId){								
											shiftDate = values.getPickupTime();																	
											eFmFmEmployeeReqMasterPO.setTripType("PICKUP");
											travelRequest.setLocationWaypointsIds(values.getStartPickupLocation()+","+values.getEndPickupLocation());
										}else{
											shiftDate = values.getDropTime();											
											eFmFmEmployeeReqMasterPO.setTripType("DROP");
											travelRequest.setLocationWaypointsIds(values.getStartDropLocation()+","+values.getEndDropLocation());
										}
										shift = shiftFormate.parse(shiftDate);
										shiftTime = new java.sql.Time(shift.getTime());										
										startDate = timeformate.parse(values.getStartDate() + " " + "00:00");
										endDate = timeformate.parse(values.getEndDate() + " " + "23:59");											
										eFmFmEmployeeReqMasterPO.setDropSequence(1);
										eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
										eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
										eFmFmEmployeeReqMasterPO.setTimeFlg(values.getTimeFlg());
										eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
										eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
										String reqCreationStatus=requestmasterReqCreation(eFmFmEmployeeReqMasterPO,travelRequest,
												eFmFmRouteAreaMappingPO.getRouteAreaId(),String.valueOf(clientBranch.get(0).getBranchId()));
										if(!(reqCreationStatus.equalsIgnoreCase("success"))){
										 responce.put("status",reqCreationStatus);
										 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
										 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
										}			
									}									
								}else{
										
									if(values.getTripType().equalsIgnoreCase("PICKUP") || values.getTripType().equalsIgnoreCase("DROP")){
										if(values.getTripType().equalsIgnoreCase("PICKUP")){
											shiftDate = values.getPickupTime();
											travelRequest.setLocationWaypointsIds(values.getStartPickupLocation()+","+values.getEndPickupLocation());
										}else{	
											shiftDate = values.getDropTime();
											travelRequest.setLocationWaypointsIds(values.getStartDropLocation()+","+values.getEndDropLocation());
										}										
										shift = shiftFormate.parse(shiftDate);
										shiftTime = new java.sql.Time(shift.getTime());
										startDate = timeformate.parse(values.getStartDate() + " " + "00:00");
										endDate = timeformate.parse(values.getEndDate() + " " + "23:59");
										eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
										eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
										eFmFmEmployeeReqMasterPO.setTripType(values.getTripType());
										eFmFmEmployeeReqMasterPO.setTimeFlg(values.getTimeFlg());
										eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
										eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
										String reqCreationStatus=requestmasterReqCreation(eFmFmEmployeeReqMasterPO,travelRequest,
												eFmFmRouteAreaMappingPO.getRouteAreaId(),travelRequest.getCombinedFacility());
										if(!(reqCreationStatus.equalsIgnoreCase("success"))){
										 responce.put("status",reqCreationStatus);
										 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
										 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
										}			
									}
								}
								
					   	 	
					   	 }else{
					   		 responce.put("status","NOTVALIDMULTIREQTRIPTYPE");
							 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
							 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					   	 }
					   	}
			    	 } catch (Exception e) {	           
					     log.debug("tripRequestdata"+e);     
			        }				
				}else{
					 if(travelRequest.getTripType().equalsIgnoreCase("BOTH")){	
						 
							for(int shiftId=0; shiftId<=1; shiftId++){
								eFmFmEmployeeReqMasterPO = new EFmFmEmployeeRequestMasterPO();
								eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);
								if(0==shiftId){								
									shiftDate = travelRequest.getPickupTime();																	
									eFmFmEmployeeReqMasterPO.setTripType("PICKUP");
									
								}else{
									shiftDate = travelRequest.getDropTime();											
									eFmFmEmployeeReqMasterPO.setTripType("DROP");
									
								}
								shift = shiftFormate.parse(shiftDate);
								shiftTime = new java.sql.Time(shift.getTime());										
								startDate = timeformate.parse(travelRequest.getStartDate() + " " + "00:00");
								endDate = timeformate.parse(travelRequest.getEndDate() + " " + "23:59");											
								eFmFmEmployeeReqMasterPO.setDropSequence(1);
								eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
								eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
								eFmFmEmployeeReqMasterPO.setTimeFlg(travelRequest.getTimeFlg());
								eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
								eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
								String reqCreationStatus=requestmasterReqCreation(eFmFmEmployeeReqMasterPO,travelRequest,
										eFmFmRouteAreaMappingPO.getRouteAreaId(),travelRequest.getCombinedFacility());
								if(!(reqCreationStatus.equalsIgnoreCase("success"))){
									 responce.put("status",reqCreationStatus);
									 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
									 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
								}		
							}	 	
			   	        }else{					
						    eFmFmEmployeeReqMasterPO.setTripType(travelRequest.getTripType());
						    eFmFmEmployeeReqMasterPO.setTimeFlg(travelRequest.getTimeFlg());
						    eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
						    eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
						    eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
						    eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
							String reqCreationStatus=requestmasterReqCreation(eFmFmEmployeeReqMasterPO,travelRequest,
									eFmFmRouteAreaMappingPO.getRouteAreaId(),travelRequest.getCombinedFacility());
							if(!(reqCreationStatus.equalsIgnoreCase("success"))){
							 responce.put("status",reqCreationStatus);
							 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
							 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}	
			   	        }
				}
				
			} else {
				// Emp detail From UserId
				EFmFmUserMasterPO employeeDetailsPO = new EFmFmUserMasterPO();
				EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO = new EFmFmClientProjectDetailsPO();
				employeeDetailsPO.setEmployeeId(travelRequest.getEmployeeId());
				PasswordEncryption passwordEncryption = new PasswordEncryption();
				employeeDetailsPO
						.setPassword(passwordEncryption.PasswordEncoderGenerator(travelRequest.getEmployeeId()));
				employeeDetailsPO.setUserName(travelRequest.getEmployeeId());
				employeeDetailsPO.setHostMobileNumber(
						Base64.getEncoder().encodeToString(travelRequest.getHostMobileNumber().getBytes("utf-8")));
				if(null!=travelRequest.getGuestMiddleLoc()){
					employeeDetailsPO.setGuestMiddleLoc(
						Base64.getEncoder().encodeToString(travelRequest.getGuestMiddleLoc().getBytes("utf-8")));
				}
				
				employeeDetailsPO.setFirstName(
						Base64.getEncoder().encodeToString(travelRequest.getFirstName().getBytes("utf-8")));
				employeeDetailsPO
						.setLastName(Base64.getEncoder().encodeToString(travelRequest.getLastName().getBytes("utf-8")));
				employeeDetailsPO.setGender(
						Base64.getEncoder().encodeToString(travelRequest.getGender().toUpperCase().getBytes("utf-8")));
				employeeDetailsPO.setMobileNumber(
						Base64.getEncoder().encodeToString(travelRequest.getMobileNumber().getBytes("utf-8")));
				employeeDetailsPO
						.setEmailId(Base64.getEncoder().encodeToString(travelRequest.getEmailId().getBytes("utf-8")));
				if(null!=travelRequest.getAddress()){
					employeeDetailsPO.setAddress(Base64.getEncoder().encodeToString(travelRequest.getAddress().getBytes("utf-8")));
				}else{
					employeeDetailsPO.setAddress(Base64.getEncoder().encodeToString(clientBranch.get(0).getAddress().getBytes("utf-8")));
				}
				employeeDetailsPO.setPhysicallyChallenged(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
				employeeDetailsPO.setEmployeeDesignation(Base64.getEncoder().encodeToString("Shell".getBytes("utf-8")));
				employeeDetailsPO.setPanicNumber(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
				employeeDetailsPO.setIsInjured(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
				employeeDetailsPO.setPragnentLady(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));				
				employeeDetailsPO.setIsVIP(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
				employeeDetailsPO.setHostMobileNumber(Base64.getEncoder().encodeToString("N/A".getBytes("utf-8")));
				
				employeeDetailsPO.setCreationTime(new Date());
				employeeDetailsPO.setUpdatedTime(new Date());
				employeeDetailsPO.setDeviceId("NO");
				employeeDetailsPO.setEmployeeProfilePic("NO");
				employeeDetailsPO.setLastLoginTime(new Date());
				employeeDetailsPO.setTempPassWordChange(false);
				employeeDetailsPO.setWrongPassAttempt(0);
				employeeDetailsPO.setPasswordChangeDate(new Date());
				EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
				eFmFmClientBranch
						.setBranchId(clientBranch.get(0).getBranchId());
				employeeDetailsPO.seteFmFmClientBranchPO(eFmFmClientBranch);
				employeeDetailsPO.setStatus("Y");
				employeeDetailsPO.setLocationStatus("N");
				eFmFmClientProjectDetailsPO.setProjectId(1);
				employeeDetailsPO.seteFmFmClientProjectDetails(eFmFmClientProjectDetailsPO);
				if(null !=travelRequest.getLatitudeLongitude())
				employeeDetailsPO.setLatitudeLongitude(travelRequest.getLatitudeLongitude());
				employeeDetailsPO.setWeekOffDays("Sunday");
				employeeDetailsPO.setUserType("guest");

				List<EFmFmClientBranchPO> clientBranchDetails = userMasterBO
						.getClientDetails(String.valueOf(clientBranch.get(0).getBranchId()));
				CalculateDistance empDistance = new CalculateDistance();
				if(null !=travelRequest.getLatitudeLongitude()){
					try {
						employeeDetailsPO.setDistance(empDistance.employeeDistanceCalculation(
								clientBranchDetails.get(0).getLatitudeLongitude(), travelRequest.getLatitudeLongitude()));
					} catch (Exception e) {
					}
				}
				if(travelRequest.getRouteAreaId() >0){
					routeAreaDetails.setRouteAreaId(travelRequest.getRouteAreaId());
					employeeDetailsPO.seteFmFmRouteAreaMapping(routeAreaDetails);
				}else{
					routeAreaDetails.setRouteAreaId(1);
					employeeDetailsPO.seteFmFmRouteAreaMapping(routeAreaDetails);
				}
				userMasterBO.save(employeeDetailsPO);
				// Emp detail From EmployeeId
				List<EFmFmUserMasterPO> employeeDetailFromEmpId = iEmployeeDetailBO
						.getParticularEmpDetailsFromEmployeeId(travelRequest.getEmployeeId(),String.valueOf(clientBranch.get(0).getBranchId()));
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
				if(travelRequest.getRouteAreaId() >0){
					eFmFmRouteAreaMappingPO.setRouteAreaId(travelRequest.getRouteAreaId());
				}else{
					eFmFmRouteAreaMappingPO.setRouteAreaId(employeeDetailFromEmpId.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
				}				
				eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);				
				eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);	
				eFmFmEmployeeReqMasterPO.seteFmFmClientBranchPO(eFmFmClientBranch);
				if(travelRequest.getMultipleRequestFlg()==1){					
					
					travelRequest.setLocationFlg("M");								
					try {
						List<EFmFmEmployeeRequestMasterPO> tripRequestdata = travelRequest.getTripRequests();		   	 
					   	 for(EFmFmEmployeeRequestMasterPO values:tripRequestdata){	
					   		eFmFmEmployeeReqMasterPO = new EFmFmEmployeeRequestMasterPO();
							eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);
					   		 if(values.getTripType().equalsIgnoreCase("BOTH") 
					   				 || values.getTripType().equalsIgnoreCase("PICKUP") || values.getTripType().equalsIgnoreCase("DROP")){
								if(values.getTripType().equalsIgnoreCase("BOTH")){
									for(int shiftId=0; shiftId<=1; shiftId++){
										eFmFmEmployeeReqMasterPO = new EFmFmEmployeeRequestMasterPO();
										eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);
										if(0==shiftId){								
											shiftDate = values.getPickupTime();																	
											eFmFmEmployeeReqMasterPO.setTripType("PICKUP");
											travelRequest.setLocationWaypointsIds(values.getStartPickupLocation()+","+values.getEndPickupLocation());
										}else{
											shiftDate = values.getDropTime();											
											eFmFmEmployeeReqMasterPO.setTripType("DROP");
											travelRequest.setLocationWaypointsIds(values.getStartDropLocation()+","+values.getEndDropLocation());
										}
										shift = shiftFormate.parse(shiftDate);
										shiftTime = new java.sql.Time(shift.getTime());										
										startDate = timeformate.parse(values.getStartDate() + " " + "00:00");
										endDate = timeformate.parse(values.getEndDate() + " " + "23:59");											
										eFmFmEmployeeReqMasterPO.setDropSequence(1);
										eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
										eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
										eFmFmEmployeeReqMasterPO.setTimeFlg(values.getTimeFlg());
										eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
										eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
										log.debug("areaId"+eFmFmRouteAreaMappingPO.getRouteAreaId());
										log.debug("clientBranch.get(0).getBranchId()"+clientBranch.get(0).getBranchId());
										String reqCreationStatus=requestmasterReqCreation(eFmFmEmployeeReqMasterPO,travelRequest,
												eFmFmRouteAreaMappingPO.getRouteAreaId(),
												travelRequest.getCombinedFacility());
										if(!(reqCreationStatus.equalsIgnoreCase("success"))){
											 responce.put("status",reqCreationStatus);
											 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
											 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
										}			
									}									
								}else{
										
									if(values.getTripType().equalsIgnoreCase("PICKUP") || values.getTripType().equalsIgnoreCase("DROP")){										
										if(values.getTripType().equalsIgnoreCase("PICKUP")){
											shiftDate = values.getPickupTime();
											travelRequest.setLocationWaypointsIds(values.getStartPickupLocation()+","+values.getEndPickupLocation());
										}else{	
											shiftDate = values.getDropTime();
											travelRequest.setLocationWaypointsIds(values.getStartDropLocation()+","+values.getEndDropLocation());
										}										
										shift = shiftFormate.parse(shiftDate);
										shiftTime = new java.sql.Time(shift.getTime());
										startDate = timeformate.parse(values.getStartDate() + " " + "00:00");
										endDate = timeformate.parse(values.getEndDate() + " " + "23:59");
										eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
										eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
										eFmFmEmployeeReqMasterPO.setTimeFlg(values.getTimeFlg());
										eFmFmEmployeeReqMasterPO.setTripType(values.getTripType());
										eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
										eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
										String reqCreationStatus=requestmasterReqCreation(eFmFmEmployeeReqMasterPO,travelRequest,
												eFmFmRouteAreaMappingPO.getRouteAreaId(),travelRequest.getCombinedFacility());
										if(!(reqCreationStatus.equalsIgnoreCase("success"))){
										 responce.put("status",reqCreationStatus);
										 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
										 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
										}			
									}
								}
								
					   	 	
					   	 }else{
					   		 responce.put("status","NOTVALIDMULTIREQTRIPTYPE");
							 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
							 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					   	 }
					   	}
			    	 } catch (Exception e) {	           
					     log.debug("tripRequestdata"+e);     
					     e.printStackTrace();
			        }				
				}else{
					 if(travelRequest.getTripType().equalsIgnoreCase("BOTH")){	
						 
							for(int shiftId=0; shiftId<=1; shiftId++){
								eFmFmEmployeeReqMasterPO = new EFmFmEmployeeRequestMasterPO();
								eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);
								if(0==shiftId){								
									shiftDate = travelRequest.getPickupTime();																	
									eFmFmEmployeeReqMasterPO.setTripType("PICKUP");
									
								}else{
									shiftDate = travelRequest.getDropTime();											
									eFmFmEmployeeReqMasterPO.setTripType("DROP");
									
								}
								shift = shiftFormate.parse(shiftDate);
								shiftTime = new java.sql.Time(shift.getTime());										
								startDate = timeformate.parse(travelRequest.getStartDate() + " " + "00:00");
								endDate = timeformate.parse(travelRequest.getEndDate() + " " + "23:59");											
								eFmFmEmployeeReqMasterPO.setDropSequence(1);
								eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
								eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
								eFmFmEmployeeReqMasterPO.setTimeFlg(travelRequest.getTimeFlg());
								eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
								eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
								String reqCreationStatus=requestmasterReqCreation(eFmFmEmployeeReqMasterPO,travelRequest,
										eFmFmRouteAreaMappingPO.getRouteAreaId(),travelRequest.getCombinedFacility());
								if(!(reqCreationStatus.equalsIgnoreCase("success"))){
									 responce.put("status",reqCreationStatus);
									 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
									 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
								}		
							}	 	
			   	        }else{					
						    eFmFmEmployeeReqMasterPO.setTripType(travelRequest.getTripType());
						    eFmFmEmployeeReqMasterPO.setTimeFlg(travelRequest.getTimeFlg());
						    eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
						    eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
						    eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
						    eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
							String reqCreationStatus=requestmasterReqCreation(eFmFmEmployeeReqMasterPO,travelRequest,
									eFmFmRouteAreaMappingPO.getRouteAreaId(),travelRequest.getCombinedFacility());
							if(!(reqCreationStatus.equalsIgnoreCase("success"))){
							 responce.put("status",reqCreationStatus);
							 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
							 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}	
			   	        }
				}		
			}
			
			if(clientBranch.get(0).getApprovalProcess().equalsIgnoreCase("Yes") && reportMngEmailId !=""){	
				
				final java.sql.Time mailShiftTime =new java.sql.Time(shift.getTime());
				List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO
						.getParticularEmpDetailsFromUserIdWithOutStatus(travelRequest.getUserId(),
								travelRequest.getCombinedFacility());
				try {
					String toMailId = new String(
							Base64.getDecoder().decode(reportMngEmailId),
							"utf-8");
					log.info("create request  mail Approval confirmation");
					Thread thread1 = new Thread(new Runnable() {
						@Override
						public void run() {
							SendMailBySite mailSender = new SendMailBySite();
//									mailSender.createRequestMailConfirmation(toMailId, "team", "");
							mailSender.approvalMailTemplate(toMailId, shiftFormate.format(mailShiftTime),
									travelRequest.getTripType(),
									travelRequest.getStartDate(), travelRequest.getEndDate(),
									clientBranch.get(0).getFeedBackEmailId(),
									employeeDetails.get(0).getEmployeeId(),travelRequest.getProjectName());												
						}
					});
					thread1.start();
				} catch (Exception e) {
					log.info("create request  mail Approval confirmation" + e);
				}								
			}			
			responce.put("status", "success");
			
		} catch (Exception e) {
			log.info("Inside error blog" + e);
			e.printStackTrace();
		}
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	public String requestmasterReqCreation(EFmFmEmployeeRequestMasterPO eFmFmEmployeeReqMasterPO,
			EFmFmEmployeeRequestMasterPO travelRequest,int routeAreaId,String branchId){
		IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		EFmFmClientBranchPO eFmFmClientBranchPO=new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(Integer.parseInt(branchId));
		List<EFmFmClientBranchPO> clientBranch = userMasterBO
				.getClientDetails(branchId);
		String response="success",reportMngEmailId="";
		eFmFmEmployeeReqMasterPO.setDropSequence(1);
		eFmFmEmployeeReqMasterPO.setStatus("Y");
		eFmFmEmployeeReqMasterPO.setRequestType("guest");
		eFmFmEmployeeReqMasterPO.setReadFlg("Y");
		eFmFmEmployeeReqMasterPO.setRequestDate(new Date());
		eFmFmEmployeeReqMasterPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		eFmFmEmployeeReqMasterPO.setRequestFrom(travelRequest.getRequestFrom());
		
		
		String requestvalidatonstatus=cabAdminRequestValidation(eFmFmEmployeeReqMasterPO.getTripRequestStartDate(), 
				eFmFmEmployeeReqMasterPO.getTripRequestEndDate(), travelRequest.getTripType(),
				eFmFmEmployeeReqMasterPO.getEfmFmUserMaster().getUserId(), eFmFmEmployeeReqMasterPO.getShiftTime(),
				travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
		if(requestvalidatonstatus.equalsIgnoreCase("failed")){
				response ="requestExist";							
		}else if(requestvalidatonstatus.equalsIgnoreCase("alreadyTravelled")){
			  response ="Already travelled for this requested Date & TripType";						
		}
		
		EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping = new EFmFmRouteAreaMappingPO();
		if(travelRequest.getRouteAreaId() >0){
			eFmFmRouteAreaMapping.setRouteAreaId(travelRequest.getRouteAreaId());
		}else{
			eFmFmRouteAreaMapping.setRouteAreaId(routeAreaId);
		}			
		eFmFmEmployeeReqMasterPO.seteFmFmRouteAreaMapping(eFmFmRouteAreaMapping);
		/*Multiple Destination Logic*/
		try {
			if(!(travelRequest.getLocationFlg().equalsIgnoreCase("N") 
					&& travelRequest.getLocationFlg().equalsIgnoreCase(""))){
				if(travelRequest.getLocationWaypointsIds().replace(",","").isEmpty()){
						response= "NOTVALIDLOCATIONID";					
				}else{
					eFmFmEmployeeReqMasterPO.setLocationFlg(travelRequest.getLocationFlg());				
					if (travelRequest.getLocationWaypointsIds() != null 
							&& travelRequest.getLocationWaypointsIds().length() > 0 
							&& travelRequest.getLocationWaypointsIds().
							charAt(travelRequest.getLocationWaypointsIds().length()-1)==',') {
						eFmFmEmployeeReqMasterPO.setLocationWaypointsIds(travelRequest.getLocationWaypointsIds().substring(0, travelRequest.getLocationWaypointsIds().length()-1));	
					}else{
						eFmFmEmployeeReqMasterPO.setLocationWaypointsIds(travelRequest.getLocationWaypointsIds());	
					}
				}
			}
		} catch (Exception e) {
			log.debug("log"+e);
		}
		
		/*
		 * servion Changes
		 */
		if(clientBranch.get(0).getApprovalProcess().equalsIgnoreCase("Yes")){
			/*List<EFmFmEmployeeTravelRequestPO> approvalCount = iCabRequestBO
					.getListOfApprovalPendingRequestForUser(travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
							employeeDetailFromEmpId.get(0).getUserId(),"N");					
					if(clientBranch.get(0).getPostApproval() < approvalCount.size()){
						responce.put("status", "WithOutApprovalReqExceeded");
						 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}*/
					List<EFmFmUserMasterPO> approvalManagerDetails=iEmployeeDetailBO.getParticularEmpDetailsFromUserId
							(Integer.parseInt(travelRequest.getReportingManagerUserId()),
									clientBranch.get(0).getBranchId());								
					if(approvalManagerDetails.isEmpty()){
								response= "notValidApprovalManagerId";								
					}
					reportMngEmailId=approvalManagerDetails.get(0).getEmailId();			
		}else{
			eFmFmEmployeeReqMasterPO.setReqApprovalStatus("Y");
		}
		if(clientBranch.get(0).getRequestWithProject().equalsIgnoreCase("Yes")){
			eFmFmEmployeeReqMasterPO.setReportingManagerUserId(travelRequest.getReportingManagerUserId());
			eFmFmEmployeeReqMasterPO.setProjectId(travelRequest.getProjectId());	
		}
		//eFmFmEmployeeReqMasterPO.setReqApprovalStatus("N");			
		/*
		 * servion Changes
		 */	
		iCabRequestBO.save(eFmFmEmployeeReqMasterPO);
		log.debug("trip"+eFmFmEmployeeReqMasterPO.getTripId());
		try {									
			log.info("request creation");
			Thread guestRequestCreation = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						travelRequestCreation(eFmFmEmployeeReqMasterPO.getTripId());
					} catch (UnsupportedEncodingException | ParseException e) {
						log.info("Error" + e);
					}
				}
			});
			guestRequestCreation.start();
		} catch (Exception e) {
			log.info("request creation" + e);
		}
		return response;
	}
	
	/*
	 * validating employee trip request 
	 * before adding Request Master Table
	 */	
		
	private String cabRequestValidation(Date startDate, Date toDate, String tripType, String employeeId, int userId,
			Time shiftTime, int branchId) {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		String response = "success";
		try {
			List<EFmFmEmployeeRequestMasterPO> existRequestsReqMaster = iCabRequestBO
					.getParticularRequestDetailFromUserIdByRead(userId, branchId, tripType);
			if (!(existRequestsReqMaster.isEmpty())) {
				for (EFmFmEmployeeRequestMasterPO empMasterReqDetails : existRequestsReqMaster) {							
								if ((empMasterReqDetails.getTripRequestStartDate().getTime() >= startDate.getTime() 
									&& (empMasterReqDetails.getTripRequestStartDate().getTime() <= toDate.getTime() 
									|| empMasterReqDetails.getTripRequestEndDate().getTime()<= toDate.getTime()))							
									|| (empMasterReqDetails.getTripRequestStartDate().getTime() <= startDate.getTime() 
									&& empMasterReqDetails.getTripRequestEndDate().getTime() >= toDate.getTime()) 
									|| (empMasterReqDetails.getTripRequestEndDate().getTime() <= toDate.getTime() 
									&& empMasterReqDetails.getTripRequestEndDate().getTime() >= startDate.getTime())){																		
										List<EFmFmEmployeeTravelRequestPO> travelRequestRead = iCabRequestBO.
												employeeTravelRequestValidation(startDate, toDate, branchId, userId, tripType);
												if (!travelRequestRead.isEmpty()) {
															response = "failed";
																								
												}else{													
													List<EFmFmEmployeeTravelRequestPO> pastTravelledRequest = iCabRequestBO
															.pastRequestByTravelMaster(empMasterReqDetails.getTripId());
													if (pastTravelledRequest.isEmpty()) {
																response = "failed";
													}						
												}												
												List<EFmFmEmployeeTravelRequestPO> travelledRequestRead = iCabRequestBO.
														employeeTravelledRequestValidation(startDate, toDate, branchId, userId, tripType);
														if (!travelledRequestRead.isEmpty()) {
																response = "alreadyTravelled";
																										
												}		
								}
							
				        }
				
			}else{
				List<EFmFmEmployeeTravelRequestPO> travelRequestRead = iCabRequestBO.
						employeeTravelRequestValidation(startDate, toDate, branchId, userId, tripType);
						if (!travelRequestRead.isEmpty()) {
									response = "failed";
																		
						}						
						List<EFmFmEmployeeTravelRequestPO> travelledRequestRead = iCabRequestBO.
								employeeTravelledRequestValidation(startDate, toDate, branchId, userId, tripType);
								if (!travelledRequestRead.isEmpty()) {
										response = "alreadyTravelled";
																				
						}						
			}
		} catch (Exception e) {
			log.info("error" + e);
		}
		return response;
	}
	
	
	private String cabAdminRequestValidation(Date startDate, Date toDate, String tripType,int userId,
			Time shiftTime, int branchId) {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		String response = "success";
		try {
			List<EFmFmEmployeeRequestMasterPO> existRequestsReqMaster = iCabRequestBO
					.getParticularRequestDetailFromUserIdByShiftTime(userId,branchId,tripType,shiftTime);
			if (!(existRequestsReqMaster.isEmpty())){
				for (EFmFmEmployeeRequestMasterPO empMasterReqDetails : existRequestsReqMaster) {							
								if ((empMasterReqDetails.getTripRequestStartDate().getTime() >= startDate.getTime() 
									&& (empMasterReqDetails.getTripRequestStartDate().getTime() <= toDate.getTime() 
									|| empMasterReqDetails.getTripRequestEndDate().getTime()<= toDate.getTime()))							
									|| (empMasterReqDetails.getTripRequestStartDate().getTime() <= startDate.getTime() 
									&& empMasterReqDetails.getTripRequestEndDate().getTime() >= toDate.getTime()) 
									|| (empMasterReqDetails.getTripRequestEndDate().getTime() <= toDate.getTime() 
									&& empMasterReqDetails.getTripRequestEndDate().getTime() >= startDate.getTime())){																		
										List<EFmFmEmployeeTravelRequestPO> travelRequestRead = iCabRequestBO.
												employeeTravelRequestValidationByGuest(startDate, toDate, branchId, userId, tripType,shiftTime);
												if (!travelRequestRead.isEmpty()) {
															response = "failed";
																								
												}else{													
													List<EFmFmEmployeeTravelRequestPO> pastTravelledRequest = iCabRequestBO
															.pastRequestByTravelMaster(empMasterReqDetails.getTripId());
													if (pastTravelledRequest.isEmpty()) {
																response = "failed";
													}						
												}												
												List<EFmFmEmployeeTravelRequestPO> travelledRequestRead = iCabRequestBO.
														employeeTravelledRequestValidationByGuest(startDate, toDate, branchId, userId, tripType,shiftTime);
														if (!travelledRequestRead.isEmpty()) {
																response = "alreadyTravelled";
																										
												}		
								}
							
				        }
				
			}else{
				List<EFmFmEmployeeTravelRequestPO> travelRequestRead = iCabRequestBO.
						employeeTravelRequestValidationByGuest(startDate, toDate, branchId, userId, tripType,shiftTime);
						if (!travelRequestRead.isEmpty()) {
									response = "failed";
																		
						}						
						List<EFmFmEmployeeTravelRequestPO> travelledRequestRead = iCabRequestBO.
								employeeTravelledRequestValidationByGuest(startDate, toDate, branchId, userId, tripType,shiftTime);
								if (!travelledRequestRead.isEmpty()) {
										response = "alreadyTravelled";
																				
						}						
			}
		} catch (Exception e) {
			log.info("error" + e);
		}
		return response;
	}
	
	/**
	 * 
	 * @param travelRequest
	 * @return
	 * @throws ParseException
	 */

	
	public String DayStringValues(String days){
			String reponseDays="All";
			String weekDay="All";
			StringBuffer daysBuffer = new StringBuffer(); 		
			if(!days.equalsIgnoreCase("All")){
					for (char day:days.toCharArray()) {						
						switch (String.valueOf(day)) {				
						case "1":	
							weekDay="SUNDAY";
							break;
						case "2":
							 weekDay="MONDAY";
							break;
						case "3":	
							weekDay="TUESDAY";
							break;
						case "4":	
							weekDay="WEDNESDAY";
							break;
						case "5":
							 weekDay="THURSDAY";
							break;
						case "6":	
							weekDay="FRIDAY";
							break;
						case "7":	
							weekDay="SATURDAY";
							break;
						default:
							break;
						}														
						daysBuffer.append(weekDay).append(",");						
						reponseDays = daysBuffer.toString().substring(0, daysBuffer.toString().length()-1);	
					
					}					
			}
			return reponseDays;
	
	}
	

	/*
	 * Cab request from Employee device as well as employee web console
	 * 
	 * 
	 */

	@POST
	@Path("/devicerequest")
	public Response cabRequestFromDevice(final EFmFmEmployeeRequestMasterPO travelRequest) throws ParseException {
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 log.info("serviceStart -UserId :" + travelRequest.getUserId());
		Map<String, Object> responce = new HashMap<String, Object>();		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 String tripType="";
		 int ceilingAlertFlag=0;
		if(travelRequest.getRequestFrom().equalsIgnoreCase("M")){
		 try{
	 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),travelRequest.getUserId()))){
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(travelRequest.getUserId());
	 			if (!(userDetail.isEmpty())) {
	 				String jwtToken = "";
	 				try {
	 					JwtTokenGenerator token = new JwtTokenGenerator();
	 					jwtToken = token.generateToken();
	 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
	 					userDetail.get(0).setMobTokenGenerationTime(new Date());
	 					userMasterBO.update(userDetail.get(0));
	 				} catch (Exception e) {
	 					log.info("error" + e);
	 				}
	                responce.put("token", jwtToken);
	 			}
	 		
	 		}catch(Exception e){
	 				log.info("authentication error"+e);
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
		}
		
		if(travelRequest.getRequestFrom().equalsIgnoreCase("W")){
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
		                responce.put("token", jwtToken);
		 			}
		 		
		 		}catch(Exception e){
		 				log.info("authentication error"+e);
		 				responce.put("status", "invalidRequest");
		 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 			}
		}
		 
		 String response = "failed",reportMngEmailId ="",userEmployeeId="",managerMailTriggerFlg="N";
		StringBuilder listOfEmpId=new StringBuilder();				
		int[] userIds = null;
		
		if(travelRequest.getMultipleEmpIds()>0){
			try {	
				    if(travelRequest.getMultipleProjectEmpIds().replace(",","").isEmpty()){
						 responce.put("status", "NOTVALIDLOCATIONID");
						 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
						 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}else{
						travelRequest.setReqApprovalStatus("Y");
						if (travelRequest.getMultipleProjectEmpIds() != null 
								&& travelRequest.getMultipleProjectEmpIds().length() > 0 
								&& travelRequest.getMultipleProjectEmpIds().
								charAt(travelRequest.getMultipleProjectEmpIds().length()-1)==',') {
							userIds = Arrays.asList(travelRequest.getMultipleProjectEmpIds().split(","))
				                      .stream()
				                      .map(String::trim)
				                      .mapToInt(Integer::parseInt).toArray();							
							//travelRequest.getMultipleProjectEmpIds().substring(0,travelRequest.getMultipleProjectEmpIds().length()-1);
							
						}else{							
					    	//travelRequest.getMultipleProjectEmpIds();							
							userIds = Arrays.asList(travelRequest.getMultipleProjectEmpIds().split(","))
				                      .stream()
				                      .map(String::trim)
				                      .mapToInt(Integer::parseInt).toArray();
						}
					}
				
			} catch (Exception e) {
				log.debug("log"+e);
			}
		}else{
			travelRequest.setReqApprovalStatus("N");
			userIds = new int[]{travelRequest.getUserId()};                   
		}	
		tripType=travelRequest.getTripType();
		List<EFmFmClientBranchPO> particularBranchDetails = userMasterBO.getClientDetails
				(new MultifacilityService().combinedBranchIdDetails(travelRequest.getUserId(),travelRequest.getCombinedFacility()));
		try {			
			for(int userId :userIds){				
			travelRequest.setUserId(userId);			

			/*
			 * Both Logic Need to Implement
			 */
			int exeCount=0;
			if(tripType.equalsIgnoreCase("BOTH")){
				exeCount=1;
			}		
		    for(int tripTypeloop=0; tripTypeloop<=exeCount; tripTypeloop++){    	
		    
		    	
			EFmFmEmployeeRequestMasterPO eFmFmEmployeeReqMasterPO = new EFmFmEmployeeRequestMasterPO();			
			DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			DateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat todayFormat = new SimpleDateFormat("dd-MM-yyyy");			
			DateFormat dbDateFormate = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dateTime = new SimpleDateFormat("dd-MM-yyyy");
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
			
			String shiftDate = travelRequest.getTime();
			Date shift = shiftFormate.parse(shiftDate);
			java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
			log.info("Start Date" + travelRequest.getStartDate());
			Date startDate = dateTimeFormate.parse(travelRequest.getStartDate() + " " + "00:00");
			String reqDate = dateFormat.format(startDate);
			Date endDate = dateTimeFormate.parse(travelRequest.getEndDate() + " " + "23:59");
			Date endDateValid = dateFormate.parse(travelRequest.getEndDate());
			String requestDateShiftTime = travelRequest.getStartDate() + " " + travelRequest.getTime();
			Date resheduleDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
			if(!(tripType.equalsIgnoreCase("BOTH"))){
				if (resheduleDateAndTime.getTime() < new Date().getTime()) {
					responce.put("status", "backDateRequest");
					log.info("backDateRequest");
					 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
			}
			
			try {
						if(tripType.equalsIgnoreCase("BOTH")){						
							String pickupShiftTime_String = travelRequest.getPickupTime();							
							Date pickupShiftTime_Date = dateTimeFormate.parse(travelRequest.getStartDate()+" "+pickupShiftTime_String);						
							String dropShiftTime_String = travelRequest.getDropTime();
							Date dropShiftTime_Date = dateTimeFormate.parse(travelRequest.getStartDate()+" "+dropShiftTime_String);							
							startDate = dateTimeFormate.parse(travelRequest.getStartDate() + " " + "00:00");
							endDate = dateTimeFormate.parse(travelRequest.getEndDate() + " " + "23:59");
							eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
							eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);							
							requestDateShiftTime = travelRequest.getStartDate() + " " + travelRequest.getPickupTime();
							resheduleDateAndTime = dateTimeFormate.parse(requestDateShiftTime);						
							if (resheduleDateAndTime.getTime() < new Date().getTime()) {
									responce.put("status", "backDateRequest");
									log.info("backDateRequest");
									 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
									return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}												
							boolean dateChangeFlg=false;	
							if(pickupShiftTime_Date.getTime() < dropShiftTime_Date.getTime()){
								long shiftTimeDiff=getDisableTime(particularBranchDetails.get(0).getShiftTimeDiffPickToDrop(),0,pickupShiftTime_Date);							
								if(shiftTimeDiff > dropShiftTime_Date.getTime()){								
									responce.put("status", " kindly make sure the cut off Time: "+shiftFormate.format(particularBranchDetails.get(0).getShiftTimeDiffPickToDrop())+" "
											+ " difference between pickup & drop");										
									log.info("serviceEnd -UserId :" + travelRequest.getUserId());
									return Response.ok(responce, MediaType.APPLICATION_JSON).build();
								}								
							}else if(pickupShiftTime_Date.getTime() > dropShiftTime_Date.getTime()){								
								long shiftTimeDiff=getDisableTime(particularBranchDetails.get(0).getShiftTimeDiffPickToDrop(),0,pickupShiftTime_Date);
								Calendar nextDay=Calendar.getInstance();
								nextDay.setTime(dropShiftTime_Date);
								nextDay.add(Calendar.DATE,1);
								if(shiftTimeDiff > nextDay.getTimeInMillis()){								
									responce.put("status", " kindly make sure the cut off Time: "+shiftFormate.format(particularBranchDetails.get(0).getShiftTimeDiffPickToDrop())+" "
													+ " difference between pickup & drop");								
									log.info("serviceEnd -UserId :" + travelRequest.getUserId());
									return Response.ok(responce, MediaType.APPLICATION_JSON).build();
								}else{
									dateChangeFlg=true;
								}								
							}							
							if(0==tripTypeloop){								
								shiftDate = travelRequest.getPickupTime();																	
								eFmFmEmployeeReqMasterPO.setTripType("PICKUP");
								travelRequest.setTripType(eFmFmEmployeeReqMasterPO.getTripType());
								eFmFmEmployeeReqMasterPO.setShiftId(travelRequest.getPickupShiftId());							
							}else{								
								shiftDate = travelRequest.getDropTime();											
								eFmFmEmployeeReqMasterPO.setTripType("DROP");
								travelRequest.setTripType(eFmFmEmployeeReqMasterPO.getTripType());
								eFmFmEmployeeReqMasterPO.setShiftId(travelRequest.getDropShiftId());
								if(dateChangeFlg){
									Calendar noOfDays=Calendar.getInstance();
									noOfDays.setTime(startDate);
									noOfDays.add(Calendar.DATE,1);
									eFmFmEmployeeReqMasterPO.setTripRequestStartDate(noOfDays.getTime());
									travelRequest.setStartDate(dateTime.format(noOfDays.getTime()));
									noOfDays.setTime(endDate);
									noOfDays.add(Calendar.DATE,1);
									eFmFmEmployeeReqMasterPO.setTripRequestEndDate(noOfDays.getTime());
									travelRequest.setEndDate(dateTime.format(noOfDays.getTime()));
								}
									
							}
							shift = shiftFormate.parse(shiftDate);
							shiftTime = new java.sql.Time(shift.getTime());																	
							eFmFmEmployeeReqMasterPO.setDropSequence(1);
							eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
							eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
							eFmFmEmployeeReqMasterPO.setTimeFlg(travelRequest.getTimeFlg());												
					}else{
							eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
							eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
							eFmFmEmployeeReqMasterPO.setTripType(travelRequest.getTripType());
							eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
							eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
					}
			} catch (Exception e) {
				log.debug("notValidShiftTime"+e);
			}	
			
			int diffDays=0;
		    try {
		    	Date sysDate = todayFormat.parse(todayFormat.format(new Date()));   	
		    	long millisecond=endDateValid.getTime()-sysDate.getTime();
				log.info("Days: " + TimeUnit.DAYS.convert(millisecond, TimeUnit.MILLISECONDS));
				diffDays=(int) TimeUnit.DAYS.convert(millisecond, TimeUnit.MILLISECONDS);
				Calendar noOfDays=Calendar.getInstance();
				noOfDays.setTime(sysDate);
				log.info("Date: " + sysDate);
				int noOfDaysInDay=noOfDays.get(Calendar.DAY_OF_WEEK);
				log.info("noOfDaysInDay: " + noOfDaysInDay);	
				if(particularBranchDetails.get(0).getRequestType().equalsIgnoreCase("Days")){
					diffDays=diffDays+1;					
					if(particularBranchDetails.get(0).getDaysRequest().equalsIgnoreCase("All") || particularBranchDetails.get(0).getDaysRequest().contains(String.valueOf(noOfDaysInDay))){						
						if((particularBranchDetails.get(0).getRequestCutOffNoOfDays()<diffDays)){
							//if((particularBranchDetails.get(0).getRequestCutOffNoOfDays()<diffDays) || dateFormate.format(startDate).equals(dateFormate.format(sysDate))){								
							if(particularBranchDetails.get(0).getDaysRequest().equalsIgnoreCase("All")){								
								responce.put("status","As per Transport policy request will enabled for " + 
										DayStringValues(particularBranchDetails.get(0).getDaysRequest())
										+ " Days for " +particularBranchDetails.get(0).getRequestCutOffNoOfDays() +" Days from today" );
							}else{
								responce.put("status","As per Transport policy request will enabled only on " + 
										DayStringValues(particularBranchDetails.get(0).getDaysRequest())
										+ "  for " +particularBranchDetails.get(0).getRequestCutOffNoOfDays() +" Days from today" );
							}
							 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
					}else{						
						if(particularBranchDetails.get(0).getDaysRequest().equalsIgnoreCase("All")){
							
							responce.put("status","As per Transport policy request will enabled for " + 
									DayStringValues(particularBranchDetails.get(0).getDaysRequest())
									+ " Days for next " +particularBranchDetails.get(0).getRequestCutOffNoOfDays() +" Days" );
						}else{
							responce.put("status","As per Transport policy request will enabled only on " + 
									DayStringValues(particularBranchDetails.get(0).getDaysRequest())
									+ "  for next " +particularBranchDetails.get(0).getRequestCutOffNoOfDays() +" Days" );
						}
						 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
				}else if(particularBranchDetails.get(0).getRequestType().equalsIgnoreCase("Date")){	
					noOfDays.setTime(sysDate);
					noOfDays.set(Calendar.DATE,noOfDays.getActualMaximum(Calendar.DATE)-(particularBranchDetails.get(0).getEarlyRequestDate()-1));
					Date EarlyRequestDate=noOfDays.getTime();
					log.info("maxDate-earlyRequestDate"+new Date()+""+particularBranchDetails.get(0).getEarlyRequestDate()+"==="+dateTime.format(noOfDays.getTime()));
					if(particularBranchDetails.get(0).getOccurrenceFlg().equalsIgnoreCase("Y") 
							&& (particularBranchDetails.get(0).getMonthOrDays().equalsIgnoreCase("everymonthlastdate"))){
						
						    Date fromMonthlyDate=null;
					        Date toMonthlyDate=null;				
							noOfDays.setTime(sysDate);
							noOfDays.add(Calendar.MONTH,1);
							noOfDays.set(Calendar.DATE, noOfDays.getActualMaximum(Calendar.DATE));							
							toMonthlyDate=noOfDays.getTime();
							noOfDays.set(Calendar.DATE, noOfDays.getActualMinimum(Calendar.DATE));	
							fromMonthlyDate=noOfDays.getTime();								
							log.info("fromMonthlyDate" +dateTime.format(fromMonthlyDate.getTime())+"====="+ 
							dateTime.format(endDateValid.getTime())+ "===" + dateTime.format(startDate.getTime())+ "===" + 
									dateTime.format(toMonthlyDate.getTime()));							
							log.info("fromMonthlyDate" +fromMonthlyDate.getTime()+"====="+ endDateValid.getTime()+ "===" +startDate.getTime()+ "===" + toMonthlyDate.getTime());
							
								if (startDate.getTime() < fromMonthlyDate.getTime() && 
										endDateValid.getTime() >= fromMonthlyDate.getTime() &&
										(endDateValid.getTime() >=toMonthlyDate.getTime() ||
										endDateValid.getTime() <= toMonthlyDate.getTime())) {									
									log.info("maxDate-earlyRequestDate" +dateTime.format(startDate.getTime())+"====="+ dateTime.format(endDateValid.getTime())
											+ "===" + dateTime.format(toMonthlyDate.getTime()));									
									responce.put("status","As per Transport policy user can create the request for "
											+dateTime.format(fromMonthlyDate.getTime())+" to  "+dateTime.format(toMonthlyDate.getTime())
											+" from " +dateTime.format(EarlyRequestDate.getTime())+" Onwards..");	
									 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
									return Response.ok(responce, MediaType.APPLICATION_JSON).build();
								}else if( startDate.getTime() >= fromMonthlyDate.getTime() && endDateValid.getTime() > toMonthlyDate.getTime()){						
										log.info("maxDate-earlyRequestDate" + dateTime.format(endDateValid.getTime()) + "===" + dateTime.format(toMonthlyDate.getTime()));
										responce.put("status","As per Transport policy user can create the request for "
										+dateTime.format(fromMonthlyDate.getTime())+" to  "+dateTime.format(toMonthlyDate.getTime())
										+" from "+dateTime.format(EarlyRequestDate.getTime())+" Onwards..");
										 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
										return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
									
								}else if( startDate.getTime() >= fromMonthlyDate.getTime() && endDateValid.getTime() <= toMonthlyDate.getTime()){									
									if (sysDate.getTime()< EarlyRequestDate.getTime()) {										
										log.info("maxDate-earlyRequestDate" + dateTime.format(endDateValid.getTime()) + "===" + dateTime.format(toMonthlyDate.getTime()));
										responce.put("status","As per Transport policy user can create the request for "
										+dateTime.format(fromMonthlyDate.getTime())+" to  "+dateTime.format(toMonthlyDate.getTime())
										+" from "+dateTime.format(EarlyRequestDate.getTime())+" Onwards..");
										 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
										return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
										
									}
								}					
					}else if(particularBranchDetails.get(0).getMonthOrDays().equalsIgnoreCase("custdate")){
							if (particularBranchDetails.get(0).getDaysRequest().equalsIgnoreCase("All") || 
								particularBranchDetails.get(0).getDaysRequest().contains(String.valueOf(noOfDaysInDay))) {							
							noOfDays.setTime(particularBranchDetails.get(0).getRequestCutOffFromDate());
							noOfDays.add(Calendar.DATE,-(particularBranchDetails.get(0).getEarlyRequestDate()-1));	
							log.info("maxDate-noOfDays"+dateTime.format(noOfDays.getTime()));
							if(particularBranchDetails.get(0).getOccurrenceFlg().equalsIgnoreCase("N")){
								if ( startDate.getTime() < particularBranchDetails.get(0).getRequestCutOffFromDate().getTime() && 
										endDateValid.getTime() >= particularBranchDetails.get(0).getRequestCutOffFromDate().getTime() &&
										(endDateValid.getTime() >= particularBranchDetails.get(0).getRequestCutOffDate().getTime() ||
										endDateValid.getTime() <= particularBranchDetails.get(0).getRequestCutOffDate().getTime())) {
									
									log.info("maxDate-earlyRequestDate" +dateTime.format(startDate.getTime())+"====="+ dateTime.format(endDateValid.getTime())
											+ "===" + dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()));
									if (particularBranchDetails.get(0).getDaysRequest().equalsIgnoreCase("All")){ 
										responce.put("status","As per Transport policy user can create the request for "
										+dateTime.format(particularBranchDetails.get(0).getRequestCutOffFromDate())+
										" to "+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()) 
										+" from "+dateTime.format(noOfDays.getTime())+" Onwards..");
									}else{
										responce.put("status","As per Transport policy user can create the request for "
												+dateTime.format(particularBranchDetails.get(0).getRequestCutOffFromDate())+
												" to "+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()) 
												+" from "+dateTime.format(noOfDays.getTime())+" Onwards.. & Booking allowed only on "+DayStringValues(particularBranchDetails.get(0).getDaysRequest()));
									}
									 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
									return Response.ok(responce, MediaType.APPLICATION_JSON).build();
									
								}else if( startDate.getTime() >= particularBranchDetails.get(0).getRequestCutOffFromDate().getTime() 
										&& endDateValid.getTime() > particularBranchDetails.get(0).getRequestCutOffDate().getTime()){																		
										log.info("maxDate-earlyRequestDate" + dateTime.format(endDateValid.getTime()) + "===" + dateTime.format(noOfDays.getTime()));
										if ( particularBranchDetails.get(0).getDaysRequest().equalsIgnoreCase("All")){ 
											responce.put("status","As per Transport policy user can create the request for "
													+dateTime.format(particularBranchDetails.get(0).getRequestCutOffFromDate())+
													" to "+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()) 
													+" from "+dateTime.format(noOfDays.getTime())+" Onwards..");
										}else{
											responce.put("status","As per Transport policy user can create the request for "
													+dateTime.format(particularBranchDetails.get(0).getRequestCutOffFromDate())+
													" to "+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()) 
													+" from "+dateTime.format(noOfDays.getTime())+" Onwards.. & Booking allowed only on "+DayStringValues(particularBranchDetails.get(0).getDaysRequest()));
										}
										 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
										return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
									
								}else if( startDate.getTime() >= particularBranchDetails.get(0).getRequestCutOffFromDate().getTime() 
										&& endDateValid.getTime() <= particularBranchDetails.get(0).getRequestCutOffDate().getTime()){									
									if (sysDate.getTime()< noOfDays.getTimeInMillis()) {										
										log.info("maxDate-earlyRequestDate" + dateTime.format(endDateValid.getTime()) + "===" + dateTime.format(noOfDays.getTime()));
										if ( particularBranchDetails.get(0).getDaysRequest().equalsIgnoreCase("All")){ 
											responce.put("status","As per Transport policy user can create the request for "
													+dateTime.format(particularBranchDetails.get(0).getRequestCutOffFromDate())+
													" to "+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()) 
													+" from "+dateTime.format(noOfDays.getTime())+" Onwards..");
										}else{
											responce.put("status","As per Transport policy user can create the request for "
													+dateTime.format(particularBranchDetails.get(0).getRequestCutOffFromDate())+
													" to "+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()) 
													+" from "+dateTime.format(noOfDays.getTime())+" Onwards.. & Booking allowed only on "+DayStringValues(particularBranchDetails.get(0).getDaysRequest()));
										}
										 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
										return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
										
									}
								}									
							}else if(particularBranchDetails.get(0).getOccurrenceFlg().equalsIgnoreCase("Y")){
								
								long cuttMilliSeconds=particularBranchDetails.get(0).getRequestCutOffDate().getTime()-sysDate.getTime();
								log.info("Days: " + TimeUnit.DAYS.convert(cuttMilliSeconds, TimeUnit.MILLISECONDS));
								int monthDaysDiff=(int) TimeUnit.DAYS.convert(cuttMilliSeconds, TimeUnit.MILLISECONDS);								
								if(sysDate.getTime()>particularBranchDetails.get(0).getRequestCutOffDate().getTime()){									
								  	long millisecCustDate=particularBranchDetails.get(0).getRequestCutOffDate().getTime()
								  			-particularBranchDetails.get(0).getRequestCutOffFromDate().getTime();
								  	noOfDays.setTime(particularBranchDetails.get(0).getRequestCutOffDate());
								  	noOfDays.add(Calendar.DATE,1);
									log.info("maxDate-earlyRequestDate" + dateTime.format(noOfDays.getTime()) 
									+ "==="+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate()));
									particularBranchDetails.get(0).setRequestCutOffFromDate(noOfDays.getTime());		  	
									log.info("Days: " + TimeUnit.DAYS.convert(millisecCustDate, TimeUnit.MILLISECONDS));	
									noOfDays.setTime(particularBranchDetails.get(0).getRequestCutOffFromDate());									
									noOfDays.add(Calendar.DATE,(int) TimeUnit.DAYS.convert(millisecCustDate, TimeUnit.MILLISECONDS));									
									log.info("maxDate-earlyRequestDate" + dateTime.format(noOfDays.getTime()) + "==="+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate()));
									particularBranchDetails.get(0).setRequestCutOffDate(noOfDays.getTime());									
									userMasterBO.update(particularBranchDetails.get(0));
									noOfDays.setTime(particularBranchDetails.get(0).getRequestCutOffFromDate());
									noOfDays.add(Calendar.DATE,-(particularBranchDetails.get(0).getEarlyRequestDate()-1));
								}else if(monthDaysDiff<=particularBranchDetails.get(0).getEarlyRequestDate()){
									
									long millisecCustDate=particularBranchDetails.get(0).getRequestCutOffDate().getTime()
								  			-particularBranchDetails.get(0).getRequestCutOffFromDate().getTime();
								  	noOfDays.setTime(particularBranchDetails.get(0).getRequestCutOffDate());
								  	noOfDays.add(Calendar.DATE,1);
									log.info("maxDate-earlyRequestDate" + dateTime.format(noOfDays.getTime()) 
									+ "==="+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate()));
									particularBranchDetails.get(0).setRequestCutOffFromDate(noOfDays.getTime());		  	
									log.info("Days: " + TimeUnit.DAYS.convert(millisecCustDate, TimeUnit.MILLISECONDS));	
									noOfDays.setTime(particularBranchDetails.get(0).getRequestCutOffFromDate());									
									noOfDays.add(Calendar.DATE,(int) TimeUnit.DAYS.convert(millisecCustDate, TimeUnit.MILLISECONDS));
									
									log.info("maxDate-earlyRequestDate" + dateTime.format(noOfDays.getTime()) 
									+ "==="+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate()));
									
									particularBranchDetails.get(0).setRequestCutOffDate(noOfDays.getTime());									
									//userMasterBO.update(particularBranchDetails.get(0));
									noOfDays.setTime(particularBranchDetails.get(0).getRequestCutOffFromDate());
									noOfDays.add(Calendar.DATE,-(particularBranchDetails.get(0).getEarlyRequestDate()-1));
									
								}
								if ( startDate.getTime() < particularBranchDetails.get(0).getRequestCutOffFromDate().getTime() && 
										endDateValid.getTime() >= particularBranchDetails.get(0).getRequestCutOffFromDate().getTime() &&
										(endDateValid.getTime() >= particularBranchDetails.get(0).getRequestCutOffDate().getTime() ||
										endDateValid.getTime() <= particularBranchDetails.get(0).getRequestCutOffDate().getTime())) {									
									log.info("maxDate-earlyRequestDate" +dateTime.format(startDate.getTime())+"====="+ dateTime.format(endDateValid.getTime())
											+ "===" + dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()));
									if ( particularBranchDetails.get(0).getDaysRequest().equalsIgnoreCase("All")){ 
										responce.put("status","As per Transport policy user can create the request for "
												+dateTime.format(particularBranchDetails.get(0).getRequestCutOffFromDate())+
												" to "+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()) 
												+" from "+dateTime.format(noOfDays.getTime())+" Onwards..");
									}else{
										responce.put("status","As per Transport policy user can create the request for "
												+dateTime.format(particularBranchDetails.get(0).getRequestCutOffFromDate())+
												" to "+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()) 
												+" from "+dateTime.format(noOfDays.getTime())+" Onwards.. & Booking allowed only on "+DayStringValues(particularBranchDetails.get(0).getDaysRequest()));
									}
									 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
									return Response.ok(responce, MediaType.APPLICATION_JSON).build();
								}else if( startDate.getTime() >= particularBranchDetails.get(0).getRequestCutOffFromDate().getTime() 
										&& endDateValid.getTime() > particularBranchDetails.get(0).getRequestCutOffDate().getTime()){																		
										log.info("maxDate-earlyRequestDate" + dateTime.format(endDateValid.getTime()) + "===" + dateTime.format(noOfDays.getTime()));
										if ( particularBranchDetails.get(0).getDaysRequest().equalsIgnoreCase("All")){ 
											responce.put("status","As per Transport policy user can create the request for "
													+dateTime.format(particularBranchDetails.get(0).getRequestCutOffFromDate())+
													" to "+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()) 
													+" from "+dateTime.format(noOfDays.getTime())+" Onwards..");
										}else{
											responce.put("status","As per Transport policy user can create the request for "
													+dateTime.format(particularBranchDetails.get(0).getRequestCutOffFromDate())+
													" to "+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()) 
													+" from "+dateTime.format(noOfDays.getTime())+" Onwards.. & Booking allowed only on "+DayStringValues(particularBranchDetails.get(0).getDaysRequest()));
										}
										 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
										return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
									
								}else if( startDate.getTime() >= particularBranchDetails.get(0).getRequestCutOffFromDate().getTime() 
										&& endDateValid.getTime() <= particularBranchDetails.get(0).getRequestCutOffDate().getTime()){									
									if (sysDate.getTime()< noOfDays.getTimeInMillis()) {										
										log.info("maxDate-earlyRequestDate" + dateTime.format(endDateValid.getTime()) + "===" + dateTime.format(noOfDays.getTime()));
										if ( particularBranchDetails.get(0).getDaysRequest().equalsIgnoreCase("All")){ 
											responce.put("status","As per Transport policy user can create the request for "
													+dateTime.format(particularBranchDetails.get(0).getRequestCutOffFromDate())+
													" to "+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()) 
													+" from "+dateTime.format(noOfDays.getTime())+" Onwards..");
										}else{
											responce.put("status","As per Transport policy user can create the request for "
													+dateTime.format(particularBranchDetails.get(0).getRequestCutOffFromDate())+
													" to "+ dateTime.format(particularBranchDetails.get(0).getRequestCutOffDate().getTime()) 
													+" from "+dateTime.format(noOfDays.getTime())+" Onwards.. & Booking allowed only on "+DayStringValues(particularBranchDetails.get(0).getDaysRequest()));
										}
										 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
										return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
										
									}
								}		
							}
							
						} else {
							log.info("maxDate-earlyRequestDate" + dateTime.format(endDateValid.getTime()) + "==="
									+ dateTime.format(noOfDays.getTime()));
							responce.put("status", "As per transport policy user can raise the request only  on "+DayStringValues(particularBranchDetails.get(0).getDaysRequest()));
							 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
						
					}			
				}
			} catch (Exception e) {
				responce.put("status", "exception");
				log.info("kindly configure the application Setting");
				 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		    
		    String reqEndDate = dateFormat.format(endDate);
			Date requestDate = dateFormat.parse(reqDate);
			Date requestEndDate = dateFormat.parse(reqEndDate);
			// Emp detail From UserId
			List<EFmFmUserMasterPO> employeeDetailsFromUserId = iEmployeeDetailBO
					.getParticularEmpDetailsFromUserIdWithOutStatus(travelRequest.getUserId(),
							new MultifacilityService().combinedBranchIdDetails(travelRequest.getUserId(),travelRequest.getCombinedFacility()));
			// Emp detail From EmployeeId
			String employeeId = "";
			if (travelRequest.getEmployeeId().equalsIgnoreCase("NO")) {
				employeeId = employeeDetailsFromUserId.get(0).getEmployeeId();
			} else {
				employeeId = travelRequest.getEmployeeId();
			}
			log.info("employeeId" + employeeId);
			List<EFmFmUserMasterPO> requestEmployeeIdExitCheck = iEmployeeDetailBO
					.getEmpDetailsFromEmployeeIdAndBranchId(employeeId,
							travelRequest.getCombinedFacility());
			if (requestEmployeeIdExitCheck.isEmpty()) {
				responce.put("status", "empNotExist");
				 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			if (!(requestEmployeeIdExitCheck.isEmpty())
					&& requestEmployeeIdExitCheck.get(0).getStatus().equalsIgnoreCase("N")) {
				responce.put("status", "empDisable");
				 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			final List<EFmFmUserMasterPO> employeeDetailFromEmpId = iEmployeeDetailBO
					.getParticularEmpDetailsFromEmployeeId(employeeId,travelRequest.getCombinedFacility());
			List<EFmFmEmployeeRequestMasterPO> existRequestsReqMaster = iCabRequestBO
					.getEmplyeeRequestsForSameDateAndShiftTime(startDate, shiftTime,
							travelRequest.getCombinedFacility(),
							employeeDetailFromEmpId.get(0).getUserId(), travelRequest.getTripType());
			if (!(existRequestsReqMaster.isEmpty())) {				
				List<EFmFmEmployeeTravelRequestPO> travelRequestDetails = iCabRequestBO
						.particularDateRequestForEmployees(requestDate,
								travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
								employeeDetailFromEmpId.get(0).getUserId(), travelRequest.getTripType());
				if (travelRequestDetails.isEmpty()) {
					responce.put("status", "dayRequestExist");
					 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				} else{ 
					if (!(travelRequestDetails.get(travelRequestDetails.size() - 1).getRequestStatus()
							.equalsIgnoreCase("CW")
							|| travelRequestDetails.get(travelRequestDetails.size() - 1).getRequestStatus()
									.equalsIgnoreCase("CM")
							|| travelRequestDetails.get(travelRequestDetails.size() - 1).getRequestStatus()
									.equalsIgnoreCase("C"))) {
						responce.put("status", "dayRequestExist");
						 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
				}
				
			}			
			String requestvalidatonstatus=cabRequestValidation(startDate, endDateValid, travelRequest.getTripType(), employeeId,
					travelRequest.getUserId(), shiftTime,
					travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
			if(requestvalidatonstatus.equalsIgnoreCase("failed")){
				responce.put("status", "requestExist");
				 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();				
			}else if(requestvalidatonstatus.equalsIgnoreCase("alreadyTravelled")){
				responce.put("status", "Already travelled for this requested Date & TripType");
				 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();				
			}
			
			// try{
			// List<EFmFmEmployeeTravelRequestPO> travelRequestDetails =
			// iCabRequestBO
			// .particularDateRequestForEmployees(requestDate,travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
			// employeeDetailFromEmpId.get(0).getUserId(),
			// travelRequest.getTripType());
			// if(!(travelRequestDetails.isEmpty())){
			//
			// if(!(!(travelRequestDetails.get(0).getReadFlg().equalsIgnoreCase("Y"))
			// &&
			// (travelRequestDetails.get(0).getRequestStatus().equalsIgnoreCase("CW")
			// ||
			// travelRequestDetails.get(0).getRequestStatus().equalsIgnoreCase("CM")))){
			// responce.put("status", "dayRequestExist");
			// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			// }
			//
			//
			// }
			// }catch(Exception e){
			// log.info("one request a day error"+e);
			// }
			//

			boolean requestStatus = true;
			List<EFmFmEmployeeRequestMasterPO> existRequestsInReqMaster = iCabRequestBO.getActiveEmplyeeRequest(
					employeeDetailFromEmpId.get(0).geteFmFmClientBranchPO().getBranchId(),
					employeeDetailFromEmpId.get(0).getUserId(), travelRequest.getTripType());

			List<EFmFmEmployeeTravelRequestPO> travelRequestDetails = iCabRequestBO.particularDateRequestForEmployees(
					requestDate, employeeDetailFromEmpId.get(0).geteFmFmClientBranchPO().getBranchId(),
					employeeDetailFromEmpId.get(0).getUserId(), travelRequest.getTripType());

			List<EFmFmEmployeeTravelRequestPO> travelRequestEndDate = iCabRequestBO.particularDateRequestForEmployees(
					requestEndDate, employeeDetailFromEmpId.get(0).geteFmFmClientBranchPO().getBranchId(),
					employeeDetailFromEmpId.get(0).getUserId(), travelRequest.getTripType());

			if (!(travelRequestDetails.isEmpty()) && !(travelRequestDetails.get(travelRequestDetails.size() - 1)
					.getRequestStatus().equalsIgnoreCase("CW")
					|| travelRequestDetails.get(travelRequestDetails.size() - 1).getRequestStatus()
							.equalsIgnoreCase("CM")
					|| travelRequestDetails.get(travelRequestDetails.size() - 1).getRequestStatus()
							.equalsIgnoreCase("C"))) {
				responce.put("status", "dayRequestExist");
				 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}

			if (!(travelRequestEndDate.isEmpty()) && !(travelRequestEndDate.get(travelRequestEndDate.size() - 1)
					.getRequestStatus().equalsIgnoreCase("CW")
					|| travelRequestEndDate.get(travelRequestEndDate.size() - 1).getRequestStatus()
							.equalsIgnoreCase("CM")
					|| travelRequestDetails.get(travelRequestDetails.size() - 1).getRequestStatus()
							.equalsIgnoreCase("C"))) {
				responce.put("status", "dayRequestExist");
				 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			EFmFmUserMasterPO efmFmUserMaster = new EFmFmUserMasterPO();
			
			//Split-1
			
		
			
			efmFmUserMaster.setUserId(employeeDetailFromEmpId.get(employeeDetailFromEmpId.size() - 1).getUserId());
			EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO = new EFmFmRouteAreaMappingPO();
			
			
			
			
			
		  /*Multiple Destination Logic*/
			try {
				if(!(travelRequest.getLocationFlg().equalsIgnoreCase("N") 
						&& travelRequest.getLocationFlg().equalsIgnoreCase(""))){
					if(travelRequest.getLocationWaypointsIds().replace(",","").isEmpty()){
						 responce.put("status", "NOTVALIDLOCATIONID");
						 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
						 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}else{
						eFmFmEmployeeReqMasterPO.setLocationFlg(travelRequest.getLocationFlg());				
						if (travelRequest.getLocationWaypointsIds() != null 
								&& travelRequest.getLocationWaypointsIds().length() > 0 
								&& travelRequest.getLocationWaypointsIds().
								charAt(travelRequest.getLocationWaypointsIds().length()-1)==',') {
							eFmFmEmployeeReqMasterPO.setLocationWaypointsIds(travelRequest.getLocationWaypointsIds().substring(0, travelRequest.getLocationWaypointsIds().length()-1));	
						}else{
							eFmFmEmployeeReqMasterPO.setLocationWaypointsIds(travelRequest.getLocationWaypointsIds());	
						}
					}
				}
			} catch (Exception e) {
				log.debug("log"+e);
			}
			
			if(travelRequest.getRouteAreaId()>0){
				eFmFmRouteAreaMappingPO.setRouteAreaId(travelRequest.getRouteAreaId());
			}else{
				eFmFmRouteAreaMappingPO.setRouteAreaId(employeeDetailFromEmpId.get(employeeDetailFromEmpId.size() - 1)
						.geteFmFmRouteAreaMapping().getRouteAreaId());
			}
			
			log.info("else if" + travelRequestDetails.size());
			/*ceiling functionalities*/
			
			if(travelRequest.getShiftId() !=0){				
				List<EFmFmTripTimingMasterPO> ceilingTimeDetails = iCabRequestBO.
						getCeilingShiftTimeDetailByShiftId(employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getBranchId(),
								travelRequest.getShiftId());
				if (!ceilingTimeDetails.isEmpty()) {					
					if(ceilingTimeDetails.get(0).getCeilingFlg().equalsIgnoreCase("Y")){
						ceilingAlertFlag=1;
						if(ceilingTimeDetails.get(0).getCeilingNo() !=0){							
							long ceilingDaysDiff=endDateValid.getTime()-startDate.getTime();
							int ceilingDays=(int) TimeUnit.DAYS.convert(ceilingDaysDiff, TimeUnit.MILLISECONDS);
							log.info("ceilingDays" + ceilingDays);							
						  	 if(startDate.equals(endDateValid)){		
							  List<EFmFmEmployeeTravelRequestPO> ceilingRequestCount =iCabRequestBO.getEmployeeRequestCeilingCount(
									  employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getBranchId(),	
									  dbDateFormate.format(startDate), travelRequest.getTime(), travelRequest.getTripType());							  
							  int requestTripCount=ceilingRequestCount.size();							
							  List<EFmFmEmployeeRequestMasterPO> ceilingRequestMasterCount =iCabRequestBO.getEmployeeRequestCeiling
									  (employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getBranchId(),dbDateFormate.format(startDate), 
											  travelRequest.getTime(), travelRequest.getTripType());
							  	int totalCeilingCount=0;
							  	if(!ceilingRequestMasterCount.isEmpty() && ceilingRequestMasterCount.size()>0){							  		
							  		for(EFmFmEmployeeRequestMasterPO ceilingRequestMasterList:ceilingRequestMasterCount){
							  			if(startDate.getTime()>=ceilingRequestMasterList.getTripRequestStartDate().getTime() 
							  	              && startDate.getTime()<=ceilingRequestMasterList.getTripRequestEndDate().getTime()){             
							  	             totalCeilingCount++;             
							  	            }						  			
							  		}
							  		requestTripCount=requestTripCount+totalCeilingCount;
							  	}							  	
							  	if((ceilingTimeDetails.get(0).getCeilingNo()+ ceilingTimeDetails.get(0).getBufferCeilingNo()) <= requestTripCount ){
							  		responce.put("status", "You Could not able to create the request because Ceiling Limit Exceeded " 
							  				+(ceilingTimeDetails.get(0).getCeilingNo()+ceilingTimeDetails.get(0).getBufferCeilingNo()));
							  		 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
									return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							  	}else if(ceilingTimeDetails.get(0).getCeilingNo() <= requestTripCount ){
							  		if(!((ceilingTimeDetails.get(0).getCeilingNo() + ceilingTimeDetails.get(0).getBufferCeilingNo()) 
							  				<= requestTripCount )){
							  			ceilingAlertFlag=1;
							  		}							
							  	}
						  	 }else{
						  		Calendar celingDate=Calendar.getInstance();
						  		celingDate.setTime(startDate);						  		
						  		log.info("celingDate" + dbDateFormate.format(celingDate.getTime()));						  		
						  		 for(int cNo=0; cNo <=ceilingDays; cNo++){						  			
						  			celingDate.add(Calendar.DATE,cNo);
						  			log.info("celingDate" + dbDateFormate.format(celingDate.getTime()) +"cNo:"+cNo);
						  			List<EFmFmEmployeeTravelRequestPO> ceilingRequestCount =iCabRequestBO.getEmployeeRequestCeilingCount(
											  employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getBranchId(),	
											  dbDateFormate.format(celingDate.getTime()),travelRequest.getTime(),
											  travelRequest.getTripType());							  
									  int requestTripCount=ceilingRequestCount.size();
										log.info("requestTripCount" + requestTripCount);
									  List<EFmFmEmployeeRequestMasterPO> ceilingRequestMasterCount =iCabRequestBO.getEmployeeRequestCeiling
											  (employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getBranchId(),
													  dbDateFormate.format(celingDate.getTime()),travelRequest.getTime(),
													  travelRequest.getTripType());
									  	int totalCeilingCount=0;
									  	if(!ceilingRequestMasterCount.isEmpty() && ceilingRequestMasterCount.size()>0){							  		
									  		for(EFmFmEmployeeRequestMasterPO ceilingRequestMasterList:ceilingRequestMasterCount){
									  			if(startDate.getTime()>=ceilingRequestMasterList.getTripRequestStartDate().getTime() 
									  	              && startDate.getTime()<=ceilingRequestMasterList.getTripRequestEndDate().getTime()){             
									  	             totalCeilingCount++;             
									  	            }									  			
									  		}
									  		requestTripCount=requestTripCount+totalCeilingCount;
									  		log.info("requestTripCountbyloop" + requestTripCount  +"reqMasCount"+totalCeilingCount);
									  	}							  	
									  	if((ceilingTimeDetails.get(0).getCeilingNo()+ceilingTimeDetails.get(0).getBufferCeilingNo()) <= requestTripCount ){
									  		responce.put("status", "You Could not able to create the request because Ceiling Limit Exceeded " +(ceilingTimeDetails.get(0).getCeilingNo()+ceilingTimeDetails.get(0).getBufferCeilingNo()));
									  		log.info("ceilingCountExceeded" + requestTripCount  +"reqMasCount"+totalCeilingCount);
									  		 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
											return Response.ok(responce, MediaType.APPLICATION_JSON).build();
									  	}else if(ceilingTimeDetails.get(0).getCeilingNo() <= requestTripCount ){
									  		if(!((ceilingTimeDetails.get(0).getCeilingNo() + ceilingTimeDetails.get(0).getBufferCeilingNo()) 
									  				<= requestTripCount )){
									  			ceilingAlertFlag=1;
									  		}							
									  	}		
						  		 }
						  		 
						  		 
						  	 }					
						}								
					}
				}
			}								
		

			
			eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);
			eFmFmEmployeeReqMasterPO.setStatus("Y");
			eFmFmEmployeeReqMasterPO.setRequestType("adhoc");
			eFmFmEmployeeReqMasterPO.setDropSequence(1);
			eFmFmEmployeeReqMasterPO.setReadFlg("Y");
			eFmFmEmployeeReqMasterPO.setRequestDate(new Date());			
			eFmFmEmployeeReqMasterPO.setRequestFrom(travelRequest.getRequestFrom());
			eFmFmEmployeeReqMasterPO.setTimeFlg(travelRequest.getTimeFlg());
			EFmFmClientBranchPO eFmFmClientBranchPO=new EFmFmClientBranchPO();
			eFmFmClientBranchPO.setBranchId(Integer.parseInt(new MultifacilityService().combinedBranchIdDetails(travelRequest.getUserId(),travelRequest.getCombinedFacility())));
			eFmFmEmployeeReqMasterPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
			EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping = new EFmFmRouteAreaMappingPO();
			if(travelRequest.getRouteAreaId()>0){				
				eFmFmRouteAreaMapping.setRouteAreaId(travelRequest.getRouteAreaId());				
				if(requestEmployeeIdExitCheck.get(0).getUserType().equalsIgnoreCase("guest")){
					eFmFmEmployeeReqMasterPO.setRequestType("guest");					
				}		
			}else{
				eFmFmRouteAreaMapping.setRouteAreaId(employeeDetailFromEmpId.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
			}
			
			eFmFmEmployeeReqMasterPO.seteFmFmRouteAreaMapping(eFmFmRouteAreaMapping);
			eFmFmEmployeeReqMasterPO.setReqApprovalStatus(travelRequest.getReqApprovalStatus());
			/*
			 * Servion Changes
			 */
			if(particularBranchDetails.get(0).getApprovalProcess().equalsIgnoreCase("Yes")){
				if(travelRequest.getProjectId()>0){
					List<EFmFmClientProjectDetailsPO> projectDetails=iAssignRouteBO.
							getProjectDetails(travelRequest.getProjectId());
					if(!(projectDetails.isEmpty())){
						travelRequest.setProjectName(projectDetails.get(0).getClientProjectId());
					}
				}else{
					travelRequest.setProjectName("NA");
				}
				List<EFmFmEmployeeTravelRequestPO> approvalCount = iCabRequestBO
						.getListOfApprovalPendingRequestForUser(travelRequest.getCombinedFacility(),
								employeeDetailFromEmpId.get(0).getUserId(),"N");					
						if(particularBranchDetails.get(0).getPostApproval() <= approvalCount.size()){
							responce.put("status", "WithOutApprovalReqExceeded");
							 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
								try {
									String toMailId = new String(
											Base64.getDecoder().decode(approvalCount.get(0).getEfmFmUserMaster().getEmailId()),
											"utf-8");
									log.info("create request  mail Approval confirmation");
									Thread thread1 = new Thread(new Runnable() {
										@Override
										public void run() {
											SendMailBySite mailSender = new SendMailBySite();
//													mailSender.createRequestMailConfirmation(toMailId, "team", "");
											mailSender.approvalAndAcknowledement(toMailId,										
													shiftTimeFormater.format(approvalCount.get(0).getShiftTime()),approvalCount.get(0).getTripType(), 
													dateFormatter.format(approvalCount.get(0).getRequestDate()),"LimitExceeded", approvalCount.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId()
													,approvalCount.get(0).getEfmFmUserMaster().getEmployeeId(),approvalCount.get(0).getRequestId(),"",travelRequest.getProjectName());													
										}
									});
									thread1.start();
								} catch (Exception e) {
									log.info("create request  mail Approval confirmation" + e);
								}
							 
							 
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}						
						List<EFmFmUserMasterPO> approvalManagerDetails=iEmployeeDetailBO.getParticularEmpDetailsFromUserId
								(Integer.parseInt(travelRequest.getReportingManagerUserId()),
										employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getBranchId());								
						if(approvalManagerDetails.isEmpty()){
									responce.put("status", "notValidApprovalManagerId");
									 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
									return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
						reportMngEmailId=approvalManagerDetails.get(0).getEmailId();			
			}else{
				eFmFmEmployeeReqMasterPO.setReqApprovalStatus("Y");
			}
			eFmFmEmployeeReqMasterPO.setBranchId(Integer.valueOf((travelRequest.getCombinedFacility())));
			
			if(particularBranchDetails.get(0).getRequestWithProject().equalsIgnoreCase("Yes")){
				eFmFmEmployeeReqMasterPO.setReportingManagerUserId(travelRequest.getReportingManagerUserId());
				eFmFmEmployeeReqMasterPO.setProjectId(travelRequest.getProjectId());				    	
			   List<EFmFmEmployeeProjectDetailsPO> projectIdCombination =iEmployeeDetailBO.getClientProjectIdByMangerAndEmployee
			   			(Integer.parseInt(travelRequest.getReportingManagerUserId()), employeeDetailsFromUserId.get(0).getCombinedFacility(),
			   					travelRequest.getProjectId(),requestEmployeeIdExitCheck.get(0).getUserId());
				   	if(projectIdCombination.isEmpty()){
				   		responce.put("status", "NOTVALIDPROJECTIDCOMBINATION");
						 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				 	}			
			}			
			log.info("requestStatus" + requestStatus);
			/* Rajan Modified for TripType and ShiftTime Based Functionlity */
			if ((travelRequest.getRequestFrom().equalsIgnoreCase("W")
					|| travelRequest.getRequestFrom().equalsIgnoreCase("M"))
					&& (travelRequest.getRequestType().equalsIgnoreCase("N")
							|| travelRequest.getRequestType().equalsIgnoreCase("normal"))) {
				if (employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getCutOffTime().equalsIgnoreCase("T")) {
					// trip based pickup and drop time handled
					if (eFmFmEmployeeReqMasterPO.getTripType().equalsIgnoreCase("DROP")) {
						if ("validShiftTime".equalsIgnoreCase(cutOffTimeValidation(
								eFmFmEmployeeReqMasterPO.getTripType(), travelRequest.getStartDate(), shiftTime,
								employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getBranchId(),
								employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getCutOffTime(),
								employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getDropPriorTimePeriod()))) {
							if (requestStatus) {
								// create request mail Confirmation code.
								java.sql.Time mngshiftTime = new java.sql.Time(shift.getTime());
								try {
									String toMailId = new String(
											Base64.getDecoder().decode(employeeDetailsFromUserId.get(0).getEmailId()),
											"utf-8");
									log.info("create request  mail confirmation");
									Thread thread1 = new Thread(new Runnable() {
										@Override
										public void run() {
											SendMailBySite mailSender = new SendMailBySite();
											if(travelRequest.getMultipleEmpIds()>0){
												mailSender.createRequestMailByManager(toMailId, shiftFormate.format(mngshiftTime), eFmFmEmployeeReqMasterPO.getTripType(), reqDate, reqEndDate, employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getFeedBackEmailId(),"","");	
											}else{
												mailSender.createRequestMailTemplate(toMailId, shiftFormate.format(mngshiftTime), eFmFmEmployeeReqMasterPO.getTripType(), reqDate, reqEndDate, employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getFeedBackEmailId());
											}
										}
									});
									thread1.start();
								} catch (Exception e) {
									log.info("create request  mail confirmation" + e);
								}

								iCabRequestBO.save(eFmFmEmployeeReqMasterPO);	
								log.debug("trip"+eFmFmEmployeeReqMasterPO.getTripId());
								try {									
									log.info("request creation");
									Thread requestCreation = new Thread(new Runnable() {
										@Override
										public void run() {
											try {
												travelRequestCreation(eFmFmEmployeeReqMasterPO.getTripId());
											} catch (UnsupportedEncodingException | ParseException e) {
												log.info("Error" + e);
											}
										}
									});
									requestCreation.start();
								} catch (Exception e) {
									log.info("request creation" + e);
								}
								
							}

							responce.put("status", "success");
						} else {
							responce.put("status", "notValidShiftTime");
							 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
					} else if (eFmFmEmployeeReqMasterPO.getTripType().equalsIgnoreCase("PICKUP")) {
						if ("validShiftTime".equalsIgnoreCase(cutOffTimeValidation(
								eFmFmEmployeeReqMasterPO.getTripType(), travelRequest.getStartDate(), shiftTime,
								employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getBranchId(),
								employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getCutOffTime(),
								employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO()
										.getPickupPriorTimePeriod()))) {
							if (requestStatus) {
								// create request mail Confirmation code.
								java.sql.Time mngshiftTime = new java.sql.Time(shift.getTime());
								try {
									String toMailId = new String(
											Base64.getDecoder().decode(employeeDetailsFromUserId.get(0).getEmailId()),
											"utf-8");
									log.info("create request  mail confirmation");
									Thread thread1 = new Thread(new Runnable() {
										@Override
										public void run() {
											SendMailBySite mailSender = new SendMailBySite();
		//									mailSender.createRequestMailConfirmation(toMailId, "team", "");
											if(travelRequest.getMultipleEmpIds()>0){
												mailSender.createRequestMailByManager(toMailId, shiftFormate.format(mngshiftTime), eFmFmEmployeeReqMasterPO.getTripType(), reqDate, reqEndDate, employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getFeedBackEmailId(),"","");	
											}else{
												mailSender.createRequestMailTemplate(toMailId, shiftFormate.format(mngshiftTime), eFmFmEmployeeReqMasterPO.getTripType(), reqDate, reqEndDate, employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getFeedBackEmailId());
											}			
																								
										}
									});
									thread1.start();
								} catch (Exception e) {
									log.info("create request  mail confirmation" + e);
								}

								iCabRequestBO.save(eFmFmEmployeeReqMasterPO);
								log.debug("trip"+eFmFmEmployeeReqMasterPO.getTripId());
								try {									
									log.info("request creation");
									Thread requestCreation1 = new Thread(new Runnable() {
										@Override
										public void run() {
											try {
												travelRequestCreation(eFmFmEmployeeReqMasterPO.getTripId());
											} catch (UnsupportedEncodingException | ParseException e) {
												log.info("Error" + e);
											}
										}
									});
									requestCreation1.start();
								} catch (Exception e) {
									log.info("request creation" + e);
								}
							}
							responce.put("status", "success");
						} else {
							responce.put("status", "notValidShiftTime");
							 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
					}
				} else if (employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getCutOffTime()
						.equalsIgnoreCase("S")) {
					List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO
							.getParticularShiftTimeDetailByShiftId(
									employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getBranchId(),
									travelRequest.getShiftId());
					if (!shiftTimeDetails.isEmpty()) {
						// shift based pickup time handled
						if ("validShiftTime"
								.equalsIgnoreCase(cutOffTimeValidation(eFmFmEmployeeReqMasterPO.getTripType(),
										travelRequest.getStartDate(), shiftTimeDetails.get(0).getShiftTime(),
										employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getBranchId(),
										employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getCutOffTime(),
										shiftTimeDetails.get(0).getCutOffTime()))) {
							if (requestStatus) {
								java.sql.Time mngshiftTime = new java.sql.Time(shift.getTime());
								// create request mail Confirmation code.
								try {
									String toMailId = new String(
											Base64.getDecoder().decode(employeeDetailsFromUserId.get(0).getEmailId()),
											"utf-8");
									log.info("create request  mail confirmation");
									Thread thread1 = new Thread(new Runnable() {
										@Override
										public void run() {
											SendMailBySite mailSender = new SendMailBySite();
	//										mailSender.createRequestMailConfirmation(toMailId, "team", "");
											if(travelRequest.getMultipleEmpIds()>0){
												mailSender.createRequestMailByManager(toMailId, shiftFormate.format(mngshiftTime), eFmFmEmployeeReqMasterPO.getTripType(), reqDate, reqEndDate, employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getFeedBackEmailId(),"","");	
											}else{
												mailSender.createRequestMailTemplate(toMailId, shiftFormate.format(mngshiftTime), eFmFmEmployeeReqMasterPO.getTripType(), reqDate, reqEndDate, employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getFeedBackEmailId());
											}
																								
										}
									});
									thread1.start();
								} catch (Exception e) {
									log.info("create request  mail confirmation" + e);
								}

								iCabRequestBO.save(eFmFmEmployeeReqMasterPO);
								log.debug("trip"+eFmFmEmployeeReqMasterPO.getTripId());
								try {									
									log.info("request creation");
									Thread requestCreation2 = new Thread(new Runnable() {
										@Override
										public void run() {
											try {
												travelRequestCreation(eFmFmEmployeeReqMasterPO.getTripId());
											} catch (Exception e) {
												log.info("Error" + e);
											}
										}
									});
									requestCreation2.start();
								} catch (Exception e) {
									log.info("request creation" + e);
								}
							}
							responce.put("status", "success");
						} else {
							responce.put("status", "notValidShiftTime");
							 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
					}
				}
			} else if ((travelRequest.getRequestFrom().equalsIgnoreCase("W")
					|| travelRequest.getRequestFrom().equalsIgnoreCase("M"))
					&& (travelRequest.getRequestType().equalsIgnoreCase("A")
							|| travelRequest.getRequestType().equalsIgnoreCase("adhoc"))) {
				// create request mail Confirmation code.
				try {
					java.sql.Time mngshiftTime = new java.sql.Time(shift.getTime());
					String toMailId = new String(
							Base64.getDecoder().decode(employeeDetailsFromUserId.get(0).getEmailId()), "utf-8");
					log.info("create request  mail confirmation");
					Thread thread1 = new Thread(new Runnable() {
						@Override
						public void run() {
							SendMailBySite mailSender = new SendMailBySite();
	//						mailSender.createRequestMailConfirmation(toMailId, "team", "");
							mailSender.createRequestMailTemplate(toMailId, shiftFormate.format(mngshiftTime), eFmFmEmployeeReqMasterPO.getTripType(), reqDate, reqEndDate, employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getFeedBackEmailId());													
						}
					});
					thread1.start();
				} catch (Exception e) {
					log.info("create request  mail confirmation" + e);
				}
				iCabRequestBO.save(eFmFmEmployeeReqMasterPO);
				log.debug("trip"+eFmFmEmployeeReqMasterPO.getTripId());
				try {									
					log.info("request creation");
					Thread requestCreation3 = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								travelRequestCreation(eFmFmEmployeeReqMasterPO.getTripId());
							} catch (UnsupportedEncodingException | ParseException e) {
								log.info("Error" + e);
							}
						}
					});
					requestCreation3.start();
				} catch (Exception e) {
					log.info("request creation" + e);
				}

				responce.put("status", "success");
			} else {
				log.info("option or wrong");
				responce.put("status", "failure");
				 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			/*
			 * Servion approval Mail Process 
			 */										
				if(particularBranchDetails.get(0).getApprovalProcess().equalsIgnoreCase("Yes") && reportMngEmailId !=""){	
					
					if(travelRequest.getProjectId()>0){
						List<EFmFmClientProjectDetailsPO> projectDetails=iAssignRouteBO.
								getProjectDetails(travelRequest.getProjectId());
						if(!(projectDetails.isEmpty())){
							travelRequest.setProjectName(projectDetails.get(0).getClientProjectId());
						}
					}else{
						travelRequest.setProjectName("NA");
					}
					
					List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO
							.getParticularEmpDetailsFromUserIdWithOutStatus(travelRequest.getUserId(),
									travelRequest.getCombinedFacility());
					if(travelRequest.getMultipleEmpIds()==0){
						java.sql.Time mngshiftTime = new java.sql.Time(shift.getTime());
					try {
						String toMailId = new String(
								Base64.getDecoder().decode(reportMngEmailId),
								"utf-8");
						log.info("create request  mail Approval confirmation");
						Thread thread1 = new Thread(new Runnable() {
							@Override
							public void run() {
								SendMailBySite mailSender = new SendMailBySite();											
									mailSender.approvalMailTemplate(toMailId, shiftFormate.format(mngshiftTime),
											eFmFmEmployeeReqMasterPO.getTripType(), reqDate, reqEndDate, 
											employeeDetailsFromUserId.get(0).geteFmFmClientBranchPO().getFeedBackEmailId(),
											employeeDetails.get(0).getEmployeeId(),travelRequest.getProjectName());																			
							}
						});
						thread1.start();
					} catch (Exception e) {
						log.info("create request  mail Approval confirmation" + e);
					}			
					}else{
						listOfEmpId.append(employeeDetails.get(0).getEmployeeId()+ ",");
						managerMailTriggerFlg="Y";
					}		
				}
			}
		}
			
			if(managerMailTriggerFlg.equalsIgnoreCase("Y")){
				try {
						String toMailId =particularBranchDetails.get(0).getTranportCommunicationMailId();
						log.info("create request  mail Approval confirmation");
						Thread thread3 = new Thread(new Runnable() {
							@Override
							public void run() {
								SendMailBySite mailSender = new SendMailBySite();			
									mailSender.createRequestMailByManager(toMailId,travelRequest.getTime(), travelRequest.getTripType(), travelRequest.getStartDate(), travelRequest.getEndDate(), 
											particularBranchDetails.get(0).getFeedBackEmailId(),"admin",listOfEmpId.toString().substring(0, listOfEmpId.toString().length()-1));

									}
						});
						thread3.start();
					} catch (Exception e) {
						log.info("create request  mail Approval confirmation" + e);
				}						
				
			}
			
		} catch (Exception e) {
			log.info("Inside error blog" + e);
			e.printStackTrace();
			responce.put("status", response);
		}
		// responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
		 
		 if(ceilingAlertFlag==1){
			 responce.put("status", "Your trip request booked as chance passenger");
		 }	 
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Reshedule Request From Device
	 */

	@POST
	@Path("/reshedulerequestfromdevice")
	public Response resheduleRequestFromDevice(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException {
		int ceilingAlertFlag=0;
		Map<String, Object> responce = new HashMap<String, Object>();
		try {
			ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
			IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
			 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
			 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));

			 try{
		 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){
		 				responce.put("status", "invalidRequest");
		 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 			}
		 		
		 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
		 			if (!(userDetail.isEmpty())) {
		 				String jwtToken = "";
		 				try {
		 					JwtTokenGenerator token = new JwtTokenGenerator();
		 					jwtToken = token.generateToken();
		 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
		 					userDetail.get(0).setMobTokenGenerationTime(new Date());
		 					userMasterBO.update(userDetail.get(0));
		 				} catch (Exception e) {
		 					log.info("error" + e);
		 				}
		                responce.put("token", jwtToken);
		 			}
		 		
		 		}catch(Exception e){
		 				log.info("authentication error"+e);
		 				responce.put("status", "invalidRequest");
		 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 			}
		 		 
		 		 
		 		 
			 
			DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
			DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			Date rescheduleDate = dateFormatter.parse(employeeTravelRequestPO.getResheduleDate());
			DateFormat timeformate = new SimpleDateFormat("HH:mm");
			String shiftDate = employeeTravelRequestPO.getTime();
			String requestDateShiftTime = employeeTravelRequestPO.getResheduleDate() + " "
					+ employeeTravelRequestPO.getTime();
			Date resheduleDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
			if (resheduleDateAndTime.getTime() < new Date().getTime()) {
				responce.put("status", "backDateRequest");
				log.info("backDateRequest");
				 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			Date shift = timeformate.parse(shiftDate);
			java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
			List<EFmFmEmployeeTravelRequestPO> employeeTravelRequest = iCabRequestBO
					.getparticularRequestDetail(employeeTravelRequestPO);
			if (!(employeeTravelRequest.isEmpty())) {
				/*
				 * Rajan Modified for TripType and ShiftTime Based Functionlity
				 */
				if ((employeeTravelRequest.get(0).geteFmFmEmployeeRequestMaster().getRequestFrom().equalsIgnoreCase("W")
						|| employeeTravelRequest.get(0).geteFmFmEmployeeRequestMaster().getRequestFrom()
								.equalsIgnoreCase("M"))
						&& (employeeTravelRequestPO.getRequestType().equalsIgnoreCase("N")
								|| employeeTravelRequestPO.getRequestType().equalsIgnoreCase("normal"))) {
					if ("T".equalsIgnoreCase(employeeTravelRequestPO.getCutOffTimeFlg())) {
						// trip based pickup and drop time handled
						if (employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("DROP")) {
							if ("validShiftTime"
									.equalsIgnoreCase(cutOffTimeValidation(employeeTravelRequest.get(0).getTripType(),
											employeeTravelRequestPO.getResheduleDate(), shiftTime,
											employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
													.getBranchId(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getCutOffTime(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getRescheduleDropCutOffTime()))) {
								responce.put("status", "success");
							} else {
								responce.put("status", "notValidShiftTime");
								 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
								return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}
						} else if (employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
							if ("validShiftTime"
									.equalsIgnoreCase(cutOffTimeValidation(employeeTravelRequest.get(0).getTripType(),
											employeeTravelRequestPO.getResheduleDate(), shiftTime,
											employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
													.getBranchId(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getCutOffTime(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getReschedulePickupCutOffTime()))) {
								responce.put("status", "success");
							} else {
								responce.put("status", "notValidShiftTime");
								 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
								return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}
						}
					} else if ("S".equalsIgnoreCase(employeeTravelRequestPO.getCutOffTimeFlg())) {
						List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO
								.getParticularShiftTimeDetailByShiftId(employeeTravelRequest.get(0).getEfmFmUserMaster()
										.geteFmFmClientBranchPO().getBranchId(), employeeTravelRequestPO.getShiftId());
						if (!shiftTimeDetails.isEmpty()) {
							// shift based pickup time handled
							if ("validShiftTime".equalsIgnoreCase(cutOffTimeValidation(
									employeeTravelRequest.get(0).getTripType(),
									employeeTravelRequestPO.getResheduleDate(), shiftTimeDetails.get(0).getShiftTime(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getBranchId(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getCutOffTime(),
									shiftTimeDetails.get(0).getCutOffTime()))) {
								responce.put("status", "success");
							} else {

								responce.put("status", "notValidShiftTime");
								 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
								return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}
						}
					}
				}
				
				/*ceilingFunctionlity*/
				DateFormat dbDateFormate = new SimpleDateFormat("yyyy-MM-dd");
				if(employeeTravelRequestPO.getShiftId() !=0){				
					List<EFmFmTripTimingMasterPO> ceilingTimeDetails = iCabRequestBO.
							getCeilingShiftTimeDetailByShiftId(employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
									employeeTravelRequestPO.getShiftId());
					if (!ceilingTimeDetails.isEmpty()) {					
						if(ceilingTimeDetails.get(0).getCeilingFlg().equalsIgnoreCase("Y")){						
							if(ceilingTimeDetails.get(0).getCeilingNo() !=0){		
								  List<EFmFmEmployeeTravelRequestPO> ceilingRequestCount =iCabRequestBO.getEmployeeRequestCeilingCount(
										  employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),	
										  dbDateFormate.format(rescheduleDate),employeeTravelRequestPO.getTime(), employeeTravelRequest.get(0).getTripType());							  
								  int requestTripCount=ceilingRequestCount.size();							
								  List<EFmFmEmployeeRequestMasterPO> ceilingRequestMasterCount =iCabRequestBO.getEmployeeRequestCeiling
										  (employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),dbDateFormate.format(rescheduleDate), 
												  employeeTravelRequestPO.getTime(), employeeTravelRequest.get(0).getTripType());
								  	int totalCeilingCount=0;
								  	if(!ceilingRequestMasterCount.isEmpty() && ceilingRequestMasterCount.size()>0){							  		
								  		for(EFmFmEmployeeRequestMasterPO ceilingRequestMasterList:ceilingRequestMasterCount){
								  			
								  			if(rescheduleDate.getTime()>=ceilingRequestMasterList.getTripRequestStartDate().getTime() 
								  	              && rescheduleDate.getTime()<=ceilingRequestMasterList.getTripRequestEndDate().getTime()){             
								  	             totalCeilingCount++;             
								  	            }
								  		}
								  		requestTripCount=requestTripCount+totalCeilingCount;
								  	}							  	
								  	if((ceilingTimeDetails.get(0).getCeilingNo()+ceilingTimeDetails.get(0).getBufferCeilingNo()) <= requestTripCount ){								  		
								  		responce.put("status", "You Could not able to create the request because Ceiling Limit Exceeded " +(ceilingTimeDetails.get(0).getCeilingNo()+ceilingTimeDetails.get(0).getBufferCeilingNo()));
								  		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
										return Response.ok(responce, MediaType.APPLICATION_JSON).build();
								  	}else if(ceilingTimeDetails.get(0).getCeilingNo() <= requestTripCount ){
								  		if(!((ceilingTimeDetails.get(0).getCeilingNo() + ceilingTimeDetails.get(0).getBufferCeilingNo()) 
								  				<= requestTripCount )){
								  			ceilingAlertFlag=1;
								  		}							
								  	}					  	
							  	 					
							}								
						}
					}
				}

				String equalResheduleDate = dateFormatter.format(rescheduleDate);
				String oldResheduleDate = dateFormatter.format(employeeTravelRequest.get(0).getRequestDate());
				if (equalResheduleDate.equalsIgnoreCase(oldResheduleDate)) {
					employeeTravelRequest.get(0).setRequestStatus("RM");
					if (employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
							.getMangerApprovalRequired().equalsIgnoreCase("Yes")) {
						employeeTravelRequest.get(0).setIsActive("N");
					} else {
						employeeTravelRequest.get(0).setIsActive("Y");
					}
					Time oldShifTime=employeeTravelRequest.get(0).getShiftTime();
					employeeTravelRequest.get(0).setShiftTime(shiftTime);
					iCabRequestBO.update(employeeTravelRequest.get(0));
					responce.put("status", "success");
					log.info("equaldate");
					// Reshedule mail Confirmation code.
					try {
						String toMailId = new String(Base64.getDecoder()
								.decode(employeeTravelRequest.get(0).getEfmFmUserMaster().getEmailId()), "utf-8");						
						String tripType=employeeTravelRequest.get(0).getTripType();
						String feedBackMailId=employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId();
						log.info("mobile reshedule  mail confirmation");
						Thread thread1 = new Thread(new Runnable() {
							@Override
							public void run() {
								SendMailBySite mailSender = new SendMailBySite();						
								mailSender.resheduleRequestMailTemplate(toMailId, timeformate.format(oldShifTime), tripType, timeformate.format(shiftTime), dateFormatter.format(rescheduleDate),feedBackMailId);
							}
						});
						thread1.start();
					} catch (Exception e) {
						log.info("mobile reshedule  mail confirmation" + e);
					}
					 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				} else {
					List<EFmFmEmployeeTravelRequestPO> employeeCancelRequest = employeeTravelRequest = iCabRequestBO
							.particularDateRequestForEmployees(rescheduleDate,
									employeeTravelRequestPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().getUserId(),
									employeeTravelRequest.get(0).getTripType());
					if (employeeCancelRequest.isEmpty()) {
						
						employeeTravelRequest = iCabRequestBO
								.getparticularRequestDetail(employeeTravelRequestPO);
						
						
						if(employeeCancelRequest.size()>=2 && employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("DROP")){
							responce.put("status", "dayRequestExist");
							 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
						else if (employeeCancelRequest.size()<2 && employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("DROP")){
						employeeTravelRequest.get(0).setRequestStatus("RW");
						if (employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
								.getMangerApprovalRequired().equalsIgnoreCase("Yes")) {
							employeeTravelRequest.get(0).setIsActive("N");
						} else {
							employeeTravelRequest.get(0).setIsActive("Y");
						}						
						if(!(employeeCancelRequest.isEmpty())){
							if(employeeCancelRequest.get(0).getShiftTime().getTime() ==shiftTime.getTime()){
								 responce.put("status", "dayRequestExist");
								 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
								 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}	
						}
						
						
						employeeTravelRequest.get(0).setRequestDate(rescheduleDate);
						Time oldShifTime=employeeTravelRequest.get(0).getShiftTime();
						employeeTravelRequest.get(0).setShiftTime(shiftTime);
						iCabRequestBO.update(employeeTravelRequest.get(0));
						responce.put("status", "success");
						// Reshedule mail Confirmation code.
						try {
							String toMailId = new String(Base64.getDecoder()
									.decode(employeeTravelRequest.get(0).getEfmFmUserMaster().getEmailId()), "utf-8");
							String tripType=employeeTravelRequest.get(0).getTripType();
							String feedBackMailId=employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId();
							log.info("web reshedule  mail confirmation feedBackMailId");
							Thread thread1 = new Thread(new Runnable() {
								@Override
								public void run() {
									SendMailBySite mailSender = new SendMailBySite();
									mailSender.resheduleRequestMailTemplate(toMailId, timeformate.format(oldShifTime), tripType, timeformate.format(shiftTime), dateFormatter.format(rescheduleDate),feedBackMailId);
								}
							});
							thread1.start();
						} catch (Exception e) {
							log.info("web reshedule  mail confirmation" + e);
						}
						log.info("request Not available");
						 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
						}
						else{
							responce.put("status", "dayRequestExist");
							 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
					}
					if (!(employeeCancelRequest.isEmpty()) && (employeeCancelRequest
							.get(employeeCancelRequest.size() - 1).getRequestStatus().equalsIgnoreCase("CW")
							|| employeeCancelRequest.get(employeeCancelRequest.size() - 1).getRequestStatus()
									.equalsIgnoreCase("CM")
							|| employeeCancelRequest.get(employeeCancelRequest.size() - 1).getRequestStatus()
									.equalsIgnoreCase("C"))) {
						log.info("reshedule a request on cancel request behalf");
						employeeCancelRequest.get(0).setRequestStatus("RM");
						if (employeeCancelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
								.getMangerApprovalRequired().equalsIgnoreCase("Yes")) {
							employeeCancelRequest.get(0).setIsActive("N");
						} else {
							employeeCancelRequest.get(0).setIsActive("Y");
						}
						employeeCancelRequest.get(0).setRequestDate(rescheduleDate);
						employeeCancelRequest.get(0).setShiftTime(shiftTime);
						iCabRequestBO.update(employeeCancelRequest.get(0));
						employeeTravelRequest.get(0).setRequestStatus("CM");
						employeeTravelRequest.get(0).setIsActive("N");
						employeeTravelRequest.get(0).setApproveStatus("Y");
						employeeTravelRequest.get(0).setReadFlg("N");
						iCabRequestBO.update(employeeTravelRequest.get(0));
						responce.put("status", "success");
						 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();

					} else {
						
						employeeTravelRequest = iCabRequestBO
								.getparticularRequestDetail(employeeTravelRequestPO);
						if(employeeCancelRequest.size()>=2 && employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("DROP")){
							responce.put("status", "dayRequestExist");
							 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
						else if (employeeCancelRequest.size()<2 && employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("DROP")){
						employeeTravelRequest.get(0).setRequestStatus("RW");
						if (employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
								.getMangerApprovalRequired().equalsIgnoreCase("Yes")) {
							employeeTravelRequest.get(0).setIsActive("N");
						} else {
							employeeTravelRequest.get(0).setIsActive("Y");
						}
						if(!(employeeCancelRequest.isEmpty())){
							if(employeeCancelRequest.get(0).getShiftTime().getTime() ==shiftTime.getTime()){
								 responce.put("status", "dayRequestExist");
								 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
								 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}	
						}
						employeeTravelRequest.get(0).setRequestDate(rescheduleDate);
						Time oldShifTime=employeeTravelRequest.get(0).getShiftTime();
						employeeTravelRequest.get(0).setShiftTime(shiftTime);
						iCabRequestBO.update(employeeTravelRequest.get(0));
						responce.put("status", "success");
						// Reshedule mail Confirmation code.
						try {
							String toMailId = new String(Base64.getDecoder()
									.decode(employeeTravelRequest.get(0).getEfmFmUserMaster().getEmailId()), "utf-8");
							String tripType=employeeTravelRequest.get(0).getTripType();
							String feedBackMailId=employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId();

							log.info("web reshedule  mail confirmation feedBackMailId");
							Thread thread1 = new Thread(new Runnable() {
								@Override
								public void run() {
									SendMailBySite mailSender = new SendMailBySite();
									mailSender.resheduleRequestMailTemplate(toMailId, timeformate.format(oldShifTime), tripType, timeformate.format(shiftTime), dateFormatter.format(rescheduleDate),feedBackMailId);
								}
							});
							thread1.start();
						} catch (Exception e) {
							log.info("web reshedule  mail confirmation" + e);
						}
						log.info("request Not available");
						 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
						}
						else{
							responce.put("status", "dayRequestExist");
							 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
					
					}

				}
			}

		} catch (Exception e) {
			responce.put("status", "failed");
			 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		if(ceilingAlertFlag==1){
			responce.put("status", "Your trip request booked as chance passenger");
			
		}
		
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Reschedule Request From Employee web console
	 */

	@POST
	@Path("/reshedulerequestfromweb")
	public Response resheduleRequestFromEmolyeeConsole(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException {
		int ceilingAlertFlag=0;
		Map<String, Object> responce = new HashMap<String, Object>();
		try {
			ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
			IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
			 
			 		
			 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
			 try{
				 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){

			 		responce.put("status", "invalidRequest");
			 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			 	}}catch(Exception e){
			 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

			 	}
			 
			 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
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
			   }
			 
			 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
			DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
			DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			log.info("requestDateShiftTime" + employeeTravelRequestPO.getResheduleDate());

			Date rescheduleDate = dateFormatter.parse(employeeTravelRequestPO.getResheduleDate());
			DateFormat timeformate = new SimpleDateFormat("HH:mm");
			String shiftDate = employeeTravelRequestPO.getTime();
			String requestDateShiftTime = employeeTravelRequestPO.getResheduleDate() + " "
					+ employeeTravelRequestPO.getTime();
			log.info("requestDateShiftTime" + requestDateShiftTime);
			Date resheduleDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
			if (resheduleDateAndTime.getTime() < new Date().getTime()) {
				responce.put("status", "backDateRequest");
				log.info("backDateRequest");
				 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			Date shift = timeformate.parse(shiftDate);
			java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
			List<EFmFmEmployeeTravelRequestPO> employeeTravelRequest = iCabRequestBO
					.getparticularRequestDetail(employeeTravelRequestPO);
			if (!(employeeTravelRequest.isEmpty())) {
				// validation
				if ((employeeTravelRequestPO.getRequestStatus().equalsIgnoreCase("W")
						|| employeeTravelRequest.get(0).getRequestStatus().equalsIgnoreCase("M"))
						&& (employeeTravelRequestPO.getRequestType().equalsIgnoreCase("N")
								|| employeeTravelRequestPO.getRequestType().equalsIgnoreCase("normal"))) {
					if (employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getCutOffTime()
							.equalsIgnoreCase("T")) {
						// trip based pickup and drop time handled
						if (employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("DROP")) {
							if ("validShiftTime"
									.equalsIgnoreCase(cutOffTimeValidation(employeeTravelRequest.get(0).getTripType(),
											employeeTravelRequestPO.getResheduleDate(), shiftTime,
											employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
													.getBranchId(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getCutOffTime(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getRescheduleDropCutOffTime()))) {
								responce.put("status", "success");
							} else {
								responce.put("status", "notValidShiftTime");
								 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
								return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}
						} else if (employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
							if ("validShiftTime"
									.equalsIgnoreCase(cutOffTimeValidation(employeeTravelRequest.get(0).getTripType(),
											employeeTravelRequestPO.getResheduleDate(), shiftTime,
											employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
													.getBranchId(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getCutOffTime(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getReschedulePickupCutOffTime()))) {
								responce.put("status", "success");
							} else {
								responce.put("status", "notValidShiftTime");
								 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
								return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}
						}
					} else if (employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
							.getCutOffTime().equalsIgnoreCase("S")) {
						List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO
								.getParticularShiftTimeDetailByShiftId(employeeTravelRequest.get(0).getEfmFmUserMaster()
										.geteFmFmClientBranchPO().getBranchId(), employeeTravelRequestPO.getShiftId());
						if (!shiftTimeDetails.isEmpty()) {
							// shift based pickup time handled
							if ("validShiftTime".equalsIgnoreCase(cutOffTimeValidation(
									employeeTravelRequest.get(0).getTripType(),
									employeeTravelRequestPO.getResheduleDate(), shiftTimeDetails.get(0).getShiftTime(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getBranchId(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getCutOffTime(),
									shiftTimeDetails.get(0).getRescheduleCutOffTime()))) {
								responce.put("status", "success");
							} else {
								responce.put("status", "notValidShiftTime");
								 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
								return Response.ok(responce, MediaType.APPLICATION_JSON).build();

							}
						}
					}
				}
				
				
				/*ceilingFunctionlity*/
				DateFormat dbDateFormate = new SimpleDateFormat("yyyy-MM-dd");
				if(employeeTravelRequestPO.getShiftId() !=0){				
					List<EFmFmTripTimingMasterPO> ceilingTimeDetails = iCabRequestBO.
							getCeilingShiftTimeDetailByShiftId(employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
									employeeTravelRequestPO.getShiftId());
					if (!ceilingTimeDetails.isEmpty()) {					
						if(ceilingTimeDetails.get(0).getCeilingFlg().equalsIgnoreCase("Y")){						
							if(ceilingTimeDetails.get(0).getCeilingNo() !=0){		
								  List<EFmFmEmployeeTravelRequestPO> ceilingRequestCount =iCabRequestBO.getEmployeeRequestCeilingCount(employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
										  dbDateFormate.format(rescheduleDate),
										  employeeTravelRequestPO.getTime(),employeeTravelRequest.get(0).getTripType());							  
								  int requestTripCount=ceilingRequestCount.size();							
								  List<EFmFmEmployeeRequestMasterPO> ceilingRequestMasterCount =iCabRequestBO.getEmployeeRequestCeiling(employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),dbDateFormate.format(rescheduleDate),
										  employeeTravelRequestPO.getTime(), employeeTravelRequest.get(0).getTripType());
								  	int totalCeilingCount=0;
								  	if(!ceilingRequestMasterCount.isEmpty() && ceilingRequestMasterCount.size()>0){							  		
								  		for(EFmFmEmployeeRequestMasterPO ceilingRequestMasterList:ceilingRequestMasterCount){								  			
								  			if(rescheduleDate.getTime()>=ceilingRequestMasterList.getTripRequestStartDate().getTime() 
								  	              && rescheduleDate.getTime()<=ceilingRequestMasterList.getTripRequestEndDate().getTime()){             
								  	             totalCeilingCount++;             
								  	            }
								  		}
								  		requestTripCount=requestTripCount+totalCeilingCount;
								  	}							  	
								  	if((ceilingTimeDetails.get(0).getCeilingNo()+ceilingTimeDetails.get(0).getBufferCeilingNo()) <= requestTripCount ){
								  		responce.put("status", "You Could not able to create the request because Ceiling Limit Exceeded " +(ceilingTimeDetails.get(0).getCeilingNo()+ceilingTimeDetails.get(0).getBufferCeilingNo()));
								  		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
										return Response.ok(responce, MediaType.APPLICATION_JSON).build();
								  	}else if(ceilingTimeDetails.get(0).getCeilingNo() <= requestTripCount ){
								  		if(!((ceilingTimeDetails.get(0).getCeilingNo() + ceilingTimeDetails.get(0).getBufferCeilingNo()) 
								  				<= requestTripCount )){
								  			ceilingAlertFlag=1;
								  		}							
								  	}					  	
							  	 					
							}								
						}
					}
				}	
				
				String equalResheduleDate = dateFormatter.format(rescheduleDate);
				String oldResheduleDate = dateFormatter.format(employeeTravelRequest.get(0).getRequestDate());
				if (equalResheduleDate.equalsIgnoreCase(oldResheduleDate)) {
					employeeTravelRequest.get(0).setRequestStatus("RW");
					if (employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
							.getMangerApprovalRequired().equalsIgnoreCase("Yes")) {
						employeeTravelRequest.get(0).setIsActive("N");
					} else {
						employeeTravelRequest.get(0).setIsActive("Y");
					}
					Time oldShifTime=employeeTravelRequest.get(0).getShiftTime();
					employeeTravelRequest.get(0).setShiftTime(shiftTime);

					iCabRequestBO.update(employeeTravelRequest.get(0));
					responce.put("status", "success");
					// Reshedule mail Confirmation code.
					try {
						String toMailId = new String(Base64.getDecoder()
								.decode(employeeTravelRequest.get(0).getEfmFmUserMaster().getEmailId()), "utf-8");
						String tripType=employeeTravelRequest.get(0).getTripType();
						String feedBackMailId=employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId();

						log.info("web reshedule  mail confirmation");
						Thread thread1 = new Thread(new Runnable() {
							@Override
							public void run() {
								SendMailBySite mailSender = new SendMailBySite();
								mailSender.resheduleRequestMailTemplate(toMailId, timeformate.format(oldShifTime), tripType, timeformate.format(shiftTime), dateFormatter.format(rescheduleDate),feedBackMailId);
							}
						});
						thread1.start();
					} catch (Exception e) {
						log.info("web reshedule  mail confirmation" + e);
					}

					log.info("equaldate");
					 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				} else {
					List<EFmFmEmployeeTravelRequestPO> employeeCancelRequest = employeeTravelRequest = iCabRequestBO
							.particularDateRequestForEmployees(rescheduleDate,
									employeeTravelRequestPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
									employeeTravelRequest.get(0).getEfmFmUserMaster().getUserId(),
									employeeTravelRequest.get(0).getTripType());
					// request not exist for this date please do a new create
					// request
					if (employeeCancelRequest.isEmpty()) {	
						employeeTravelRequest = iCabRequestBO
								.getparticularRequestDetail(employeeTravelRequestPO);
						if(employeeCancelRequest.size()>=2 && employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("DROP")){
							responce.put("status", "dayRequestExist");
							 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
						else if (employeeCancelRequest.size()<2 && employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("DROP")){
						employeeTravelRequest.get(0).setRequestStatus("RW");
						if (employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
								.getMangerApprovalRequired().equalsIgnoreCase("Yes")) {
							employeeTravelRequest.get(0).setIsActive("N");
						} else {
							employeeTravelRequest.get(0).setIsActive("Y");
						}
						employeeTravelRequest.get(0).setRequestDate(rescheduleDate);
						Time oldShifTime=employeeTravelRequest.get(0).getShiftTime();
						employeeTravelRequest.get(0).setShiftTime(shiftTime);
						log.info("employeeCancelRequest"+employeeCancelRequest.size());
						if(!(employeeCancelRequest.isEmpty()) &&  (employeeCancelRequest.get(0).getShiftTime().getTime() ==shiftTime.getTime())){
							 responce.put("status", "dayRequestExist");
							 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
							 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
						
						
						iCabRequestBO.update(employeeTravelRequest.get(0));
						responce.put("status", "success");
						// Reshedule mail Confirmation code.
						try {
							String toMailId = new String(Base64.getDecoder()
									.decode(employeeTravelRequest.get(0).getEfmFmUserMaster().getEmailId()), "utf-8");
							String tripType=employeeTravelRequest.get(0).getTripType();
							String feedBackMailId=employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId();

							log.info("web reshedule  mail confirmation feedBackMailId");
							Thread thread1 = new Thread(new Runnable() {
								@Override
								public void run() {
									SendMailBySite mailSender = new SendMailBySite();
									mailSender.resheduleRequestMailTemplate(toMailId, timeformate.format(oldShifTime), tripType, timeformate.format(shiftTime), dateFormatter.format(rescheduleDate),feedBackMailId);
								}
							});
							thread1.start();
						} catch (Exception e) {
							log.info("web reshedule  mail confirmation" + e);
						}
						log.info("request Not available");
						 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
						}
						else{
							responce.put("status", "dayRequestExist");
							 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
					}
					if (!(employeeCancelRequest.isEmpty()) && (employeeCancelRequest
							.get(employeeCancelRequest.size() - 1).getRequestStatus().equalsIgnoreCase("CW")
							|| employeeCancelRequest.get(employeeCancelRequest.size() - 1).getRequestStatus()
									.equalsIgnoreCase("CM")
							|| employeeCancelRequest.get(employeeCancelRequest.size() - 1).getRequestStatus()
									.equalsIgnoreCase("C"))) {
						log.info("reshedule a request on cancel request behalf");
						employeeCancelRequest.get(0).setRequestStatus("RW");
						if (employeeCancelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
								.getMangerApprovalRequired().equalsIgnoreCase("Yes")) {
							employeeCancelRequest.get(0).setIsActive("N");
						} else {
							employeeCancelRequest.get(0).setIsActive("Y");
						}
						employeeCancelRequest.get(0).setRequestDate(rescheduleDate);
						employeeCancelRequest.get(0).setShiftTime(shiftTime);
						iCabRequestBO.update(employeeCancelRequest.get(0));
						employeeTravelRequest.get(0).setRequestStatus("CW");
						employeeTravelRequest.get(0).setIsActive("N");
						employeeTravelRequest.get(0).setApproveStatus("Y");
						employeeTravelRequest.get(0).setReadFlg("N");
						iCabRequestBO.update(employeeTravelRequest.get(0));
						responce.put("status", "success");
						 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					} else {						
						employeeTravelRequest = iCabRequestBO
								.getparticularRequestDetail(employeeTravelRequestPO);
						if(employeeCancelRequest.size()>=2 && employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("DROP")){
							responce.put("status", "dayRequestExist");
							 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
						else if (employeeCancelRequest.size()<2 && employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("DROP")){
						employeeTravelRequest.get(0).setRequestStatus("RW");
						if (employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
								.getMangerApprovalRequired().equalsIgnoreCase("Yes")) {
							employeeTravelRequest.get(0).setIsActive("N");
						} else {
							employeeTravelRequest.get(0).setIsActive("Y");
						}
						employeeTravelRequest.get(0).setRequestDate(rescheduleDate);
						Time oldShifTime=employeeTravelRequest.get(0).getShiftTime();
						employeeTravelRequest.get(0).setShiftTime(shiftTime);
						iCabRequestBO.update(employeeTravelRequest.get(0));	
						if(employeeCancelRequest.get(0).getShiftTime().getTime() ==shiftTime.getTime()){
							 responce.put("status", "dayRequestExist");
							 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
							 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
						responce.put("status", "success");
						// Reshedule mail Confirmation code.
						try {
							String toMailId = new String(Base64.getDecoder()
									.decode(employeeTravelRequest.get(0).getEfmFmUserMaster().getEmailId()), "utf-8");
							String tripType=employeeTravelRequest.get(0).getTripType();
							String feedBackMailId=employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId();

							log.info("web reshedule  mail confirmation feedBackMailId");
							Thread thread1 = new Thread(new Runnable() {
								@Override
								public void run() {
									SendMailBySite mailSender = new SendMailBySite();
									mailSender.resheduleRequestMailTemplate(toMailId, timeformate.format(oldShifTime), tripType, timeformate.format(shiftTime), dateFormatter.format(rescheduleDate),feedBackMailId);
								}
							});
							thread1.start();
						} catch (Exception e) {
							log.info("web reshedule  mail confirmation" + e);
						}
						log.info("request Not available");
						 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
						}
						else{
							responce.put("status", "dayRequestExist");
							 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
					
					}
				}
			}
		} catch (Exception e) {
			log.info("error"+e);
			e.printStackTrace();
			responce.put("status", "failed");
			 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		 if(ceilingAlertFlag==1){
			 responce.put("status", "Your trip request booked as chance passenger");
		 }
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * employee cancel Request from employee web console
	 */

	@POST
	@Path("/cancelrequestfrmweb")
	public Response cancelRequestFromEmployeeWebConsole(EFmFmEmployeeTravelRequestPO travelRequest) {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + travelRequest.getUserId());
		travelRequest.setRequestDate(new Date());
		List<EFmFmEmployeeTravelRequestPO> employeeTravelRequest = iCabRequestBO
				.getparticularRequestDetail(travelRequest);
		Map<String, Object> responce = new HashMap<String, Object>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequest.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(travelRequest.getUserId());
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
		   }
		 
		// if
		// (employeeTravelRequest.get(0).getRequestType().equalsIgnoreCase("adhoc"))
		// {
		// iCabRequestBO.deleteParticularRequest(employeeTravelRequest.get(0).getRequestId());
		// iCabRequestBO.deleteParticularRequestFromRequestMaster(
		// employeeTravelRequest.get(0).geteFmFmEmployeeRequestMaster().getTripId());
		// responce.put("status", "success");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		//
		// }
		employeeTravelRequest.get(0).setRequestStatus("CW");
		employeeTravelRequest.get(0).setIsActive("N");
		employeeTravelRequest.get(0).setApproveStatus("Y");
		employeeTravelRequest.get(0).setReadFlg("N");
		iCabRequestBO.update(employeeTravelRequest.get(0));
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + travelRequest.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * cancel button inside request from device
	 */

	@POST
	@Path("/cancelrequest")
	public Response cancelRequestFromDevice(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
		employeeTravelRequestPO.setRequestDate(new Date());
		List<EFmFmEmployeeTravelRequestPO> employeeTravelRequest = iCabRequestBO
				.getparticularRequestDetail(employeeTravelRequestPO);
		Map<String, Object> responce = new HashMap<String, Object>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));

		 
		 
		  try{
	 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
	 			if (!(userDetail.isEmpty())) {
	 				String jwtToken = "";
	 				try {
	 					JwtTokenGenerator token = new JwtTokenGenerator();
	 					jwtToken = token.generateToken();
	 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
	 					userDetail.get(0).setMobTokenGenerationTime(new Date());
	 					userMasterBO.update(userDetail.get(0));
	 				} catch (Exception e) {
	 					log.info("error" + e);
	 				}
	                responce.put("token", jwtToken);
	 			}
	 		
	 		}catch(Exception e){
	 				log.info("authentication error"+e);
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		 
		 
		 
		// if
		// (employeeTravelRequest.get(0).getRequestType().equalsIgnoreCase("adhoc"))
		// {
		// iCabRequestBO.deleteParticularRequest(employeeTravelRequest.get(0).getRequestId());
		// iCabRequestBO.deleteParticularRequestFromRequestMaster(
		// employeeTravelRequest.get(0).geteFmFmEmployeeRequestMaster().getTripId());
		// responce.put("status", "success");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		//
		// }
		employeeTravelRequest.get(0).setRequestStatus("CM");
		employeeTravelRequest.get(0).setIsActive("N");
		employeeTravelRequest.get(0).setApproveStatus("Y");
		employeeTravelRequest.get(0).setReadFlg("N");
		// responce.put("status", "success");
		String value = "";
		if (employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getCutOffTime()
				.equalsIgnoreCase("T")) {
			if (employeeTravelRequest.get(0).getTripType().equalsIgnoreCase("DROP")) {
				value = cutOffTimeCancelValidation(employeeTravelRequest.get(0).getTripType(),
						employeeTravelRequest.get(0).getRequestDate(), employeeTravelRequest.get(0).getShiftTime(),
						employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
						employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
								.getDropCancelTimePeriod());
			} else {
				value = cutOffTimeCancelValidation(employeeTravelRequest.get(0).getTripType(),
						employeeTravelRequest.get(0).getRequestDate(), employeeTravelRequest.get(0).getShiftTime(),
						employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
						employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
								.getPickupCancelTimePeriod());
			}
		} else {
			List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO.getShiftIdUsingshiftTime(
					employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
					employeeTravelRequest.get(0).getTripType(), employeeTravelRequest.get(0).getShiftTime());
			if (!shiftDetails.isEmpty()) {
				value = cutOffTimeCancelValidation(employeeTravelRequest.get(0).getTripType(),
						employeeTravelRequest.get(0).getRequestDate(), employeeTravelRequest.get(0).getShiftTime(),
						employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
						shiftDetails.get(0).getCancelCutoffTime());
			}
		}
		if (value.equalsIgnoreCase("validCancelTime")) {
			try {
				String toMailId = new String(
						Base64.getDecoder().decode(employeeTravelRequest.get(0).getEfmFmUserMaster().getEmailId()),
						"utf-8");
				Time shifTime=employeeTravelRequest.get(0).getShiftTime();
				String tripType=employeeTravelRequest.get(0).getTripType();
				String feedBackMailId=employeeTravelRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId();
                Date requestDate=employeeTravelRequest.get(0).getRequestDate();
				
				log.info("mobile cancel  mail confirmation"+toMailId);
				Thread thread1 = new Thread(new Runnable() {
					@Override
					public void run() {
						SendMailBySite mailSender = new SendMailBySite();
						mailSender.cancelRequestMailTemplate(toMailId, shiftTimeFormater.format(shifTime), tripType, dateFormatter.format(requestDate), feedBackMailId);
					}
				});
				thread1.start();
			} catch (Exception e) {
				log.info("mobile cancel  mail confirmation" + e);
			}

			iCabRequestBO.update(employeeTravelRequest.get(0));
			responce.put("status", "success");
		} else {
			responce.put("status", "failure");
		}
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	// cutOffTime functionality handled here
	public String cutOffTimeCancelValidation(String tripType, Date empRequestDate, Time shiftTime, int branchId,
			Time CutOffTime) throws ParseException {
		String response = "notValidShiftTime";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		// Date empRequestDate = dateFormat.parse(requestDate);
		Calendar todayDateTime = Calendar.getInstance();
		todayDateTime.setTime(new Date());
		Calendar ShiftDateTime = Calendar.getInstance();
		ShiftDateTime.setTime(dateFormat.parse(dateFormat.format(empRequestDate)));
		ShiftDateTime.add(Calendar.HOUR, shiftTime.getHours());
		ShiftDateTime.add(Calendar.MINUTE, shiftTime.getMinutes());
		ShiftDateTime.add(Calendar.SECOND, shiftTime.getSeconds());

		todayDateTime.add(Calendar.HOUR, CutOffTime.getHours());
		todayDateTime.add(Calendar.MINUTE, CutOffTime.getMinutes());
		todayDateTime.add(Calendar.SECOND, CutOffTime.getSeconds());

		if (todayDateTime.getTime().before(ShiftDateTime.getTime())
				|| todayDateTime.getTime().equals(ShiftDateTime.getTime())) {
			response = "validCancelTime";
		} else {
			response = "notValidCancelTime";
		}
		return response;
	}

	/*
	 * Request approve from web console From manager page
	 */

	@POST
	@Path("/approvereshedulerequest")
	public Response approveResheduleRequest(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO) {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
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
		   }
		 
		employeeTravelRequestPO.setRequestDate(new Date());
		List<EFmFmEmployeeTravelRequestPO> employeeTravelRequest = iCabRequestBO
				.getparticularRequestDetail(employeeTravelRequestPO);
		employeeTravelRequest.get(employeeTravelRequest.size() - 1).setApproveStatus("Y");
		employeeTravelRequest.get(employeeTravelRequest.size() - 1).setIsActive("Y");
		employeeTravelRequest.get(employeeTravelRequest.size() - 1).setReadFlg("Y");

		// }
		iCabRequestBO.update(employeeTravelRequest.get(0));
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok("success", MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Request Reject from web console by particular manager
	 */

	@POST
	@Path("/rejectreshedulerequest")
	public Response rejectResheduleRequest(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO) {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
		employeeTravelRequestPO.setRequestDate(new Date());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
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
		   }
		 
		List<EFmFmEmployeeTravelRequestPO> employeeTravelRequest = iCabRequestBO
				.getparticularRequestDetail(employeeTravelRequestPO);
		employeeTravelRequest.get(0).setApproveStatus("R");
		employeeTravelRequest.get(0).setReadFlg("N");
		employeeTravelRequest.get(0).setIsActive("N");
		iCabRequestBO.update(employeeTravelRequest.get(0));
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok("success", MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Get All Todays Request call from device
	 * 
	 */

	@POST
	@Path("/employeetodayrequest")
	public Response employeeTodaysRequestDetail(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException, UnsupportedEncodingException {
		Map<String, Object> responce = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 log.info("serviceStart -UserId :" +employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 
		 if(employeeTravelRequestPO.getStartPgNo()==0){
			 
		 try{
	 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getEfmFmUserMaster().getUserId()))){
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
	 			if (!(userDetail.isEmpty())) {
	 				String jwtToken = "";
	 				try {
	 					JwtTokenGenerator token = new JwtTokenGenerator();
	 					jwtToken = token.generateToken();
	 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
	 					userDetail.get(0).setMobTokenGenerationTime(new Date());
	 					userMasterBO.update(userDetail.get(0));
	 				} catch (Exception e) {
	 					log.info("error" + e);
	 				}
	                responce.put("token", jwtToken);
	 			}
	 		
	 		}catch(Exception e){
	 				log.info("authentication error"+e);
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		 
	 		 
		 }
		 
		 List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
		employeeTravelRequestPO.setRequestDate(new Date());
		log.info("userId"+employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
		int count = 0;
		List<EFmFmEmployeeTravelRequestPO> employeeDetail = iCabRequestBO
				.getAllTodaysRequestForParticularEmployee(employeeTravelRequestPO);
		if (!employeeDetail.isEmpty()) {
			List<EFmFmEmployeeTravelRequestPO> todayrequests = iCabRequestBO
					.getAllRequestForParticularEmployeeBasedOnMobileNumber(
							new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getEfmFmUserMaster().getUserId()
									,employeeTravelRequestPO.getCombinedFacility()),
							employeeDetail.get(0).getEfmFmUserMaster().getMobileNumber(),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
			log.info("size" + todayrequests.size());
			if (!(todayrequests.isEmpty())) {
				outer: for (EFmFmEmployeeTravelRequestPO allTravelRequest : todayrequests) {
					String currentDay = new SimpleDateFormat("EEEE").format(allTravelRequest.getRequestDate());
					if (allTravelRequest.getEfmFmUserMaster().getWeekOffDays().contains(currentDay)) {
						// Only For Shell Chennai Requirements or who ever have
						// drop on saturday morning
						if (!(currentDay.equalsIgnoreCase("Saturday")
								&& allTravelRequest.getTripType().equalsIgnoreCase("DROP")
								&& (allTravelRequest.getShiftTime().getHours() == 1
										|| allTravelRequest.getShiftTime().getHours() == 3
										|| allTravelRequest.getShiftTime().getHours() == 5
										|| allTravelRequest.getShiftTime().getHours() == 7))) {
							continue outer;
						}
					}
					count++;
					if (count <= 11) {
						// if (shiftDateAndTime.getTime() > new
						// Date().getTime()) {
						Map<String, Object> requestList = new HashMap<String, Object>();
						requestList.put("userId", allTravelRequest.getEfmFmUserMaster().getUserId());
						requestList.put("requestId", allTravelRequest.getRequestId());
						requestList.put("facilityName", allTravelRequest.geteFmFmClientBranchPO().getBranchName());
						requestList.put("facilityId", allTravelRequest.geteFmFmClientBranchPO().getBranchId());
						requestList.put("tripType", allTravelRequest.getTripType());
						responce.put("startPgNo", (employeeTravelRequestPO.getStartPgNo()+employeeTravelRequestPO.getEndPgNo()));		
						requestList.put("address",
								new String(
										Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()),
										"utf-8"));
						requestList.put("employeeAddress",
								new String(
										Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()),
										"utf-8"));
						requestList.put("requestType", allTravelRequest.getRequestType());
						requestList.put("requestDate", dateFormatter.format(allTravelRequest.getRequestDate()));
						requestList.put("tripTime", shiftTimeFormater.format(allTravelRequest.getShiftTime()));	
						
						if(allTravelRequest.getReqApprovalStatus()==null){
							requestList.put("reqApprovalStatus","N");	
						}else{
						 requestList.put("reqApprovalStatus",allTravelRequest.getReqApprovalStatus());
						}				
				
						if(allTravelRequest.getRequestRemarks()==null){
							requestList.put("requestRemarks","N");	
						}else{
						 requestList.put("requestRemarks", allTravelRequest.getRequestRemarks());
						}					
						
						if(allTravelRequest.getLocationFlg()==null){
							requestList.put("locationFlg","N");
						}else{
							requestList.put("locationFlg",allTravelRequest.getLocationFlg());
						}
						try {
							if(allTravelRequest.getLocationFlg().equalsIgnoreCase("M")){
								Map<String, Object> areaList = listOfPointsForMultipleLocation(
										allTravelRequest.getLocationWaypointsIds(),
										allTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),allTravelRequest.getEfmFmUserMaster().getUserId(),employeeTravelRequestPO.getCombinedFacility());
								if (areaList.size() > 0) {
									requestList.put("employeeWaypoints", areaList);
								} else {
									requestList.put("employeeWaypoints", "NA");
								}
							}
						} catch (Exception e) {
							log.debug("location Details are not updated_Location_flg is null");
						}		
						travelRequestList.add(requestList);
					}
				}

			}
		}
		responce.put("requests", travelRequestList);
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	
	
	
	
	/*
	 * Get All Todays Request call from Web Console
	 * 
	 */

	@POST
	@Path("/employeetodayWebRequest")
	public Response employeeTodaysRequestDetailForWebUser(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException, UnsupportedEncodingException {
		Map<String, Object> responce = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 log.info("serviceStart -UserId :" +employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 if(employeeTravelRequestPO.getStartPgNo()==0){
		 try{
	 			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getEfmFmUserMaster().getUserId()))){
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
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
	                responce.put("token", jwtToken);
	 			}
	 		
	 		}catch(Exception e){
	 				log.info("authentication error"+e);
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		 
	 		 
	}
		 
		 List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
		employeeTravelRequestPO.setRequestDate(new Date());
		log.info("userId"+employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
		int count = 0;
		List<EFmFmEmployeeTravelRequestPO> employeeDetail = iCabRequestBO
				.getAllTodaysRequestForParticularEmployee(employeeTravelRequestPO);
		if (!employeeDetail.isEmpty()) {
			List<EFmFmEmployeeTravelRequestPO> todayrequests = iCabRequestBO
					.getAllRequestForParticularEmployeeBasedOnMobileNumber(
							new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getEfmFmUserMaster().getUserId()
									,employeeTravelRequestPO.getCombinedFacility()),
							employeeDetail.get(0).getEfmFmUserMaster().getMobileNumber(),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
			log.info("size" + todayrequests.size());
			if (!(todayrequests.isEmpty())) {
				outer: for (EFmFmEmployeeTravelRequestPO allTravelRequest : todayrequests) {
					String currentDay = new SimpleDateFormat("EEEE").format(allTravelRequest.getRequestDate());
					if (allTravelRequest.getEfmFmUserMaster().getWeekOffDays().contains(currentDay)) {
						// Only For Shell Chennai Requirements or who ever have
						// drop on saturday morning
						if (!(currentDay.equalsIgnoreCase("Saturday")
								&& allTravelRequest.getTripType().equalsIgnoreCase("DROP")
								&& (allTravelRequest.getShiftTime().getHours() == 1
										|| allTravelRequest.getShiftTime().getHours() == 3
										|| allTravelRequest.getShiftTime().getHours() == 5
										|| allTravelRequest.getShiftTime().getHours() == 7))) {
							continue outer;
						}
					}
					count++;
					if (count <= 11) {
						// if (shiftDateAndTime.getTime() > new
						// Date().getTime()) {
						Map<String, Object> requestList = new HashMap<String, Object>();
						requestList.put("userId", allTravelRequest.getEfmFmUserMaster().getUserId());
						requestList.put("requestId", allTravelRequest.getRequestId());
						requestList.put("tripType", allTravelRequest.getTripType());
						requestList.put("address",
								new String(
										Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()),
										"utf-8"));
						requestList.put("employeeAddress",
								new String(
										Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()),
										"utf-8"));
						requestList.put("requestType", allTravelRequest.getRequestType());
						requestList.put("requestDate", dateFormatter.format(allTravelRequest.getRequestDate()));
						requestList.put("tripTime", shiftTimeFormater.format(allTravelRequest.getShiftTime()));	
						responce.put("startPgNo", (employeeTravelRequestPO.getStartPgNo()+employeeTravelRequestPO.getEndPgNo()));
						
						if(allTravelRequest.getReqApprovalStatus()==null){
							requestList.put("reqApprovalStatus","N");	
						}else{
						 requestList.put("reqApprovalStatus",allTravelRequest.getReqApprovalStatus());
						}				
				
						if(allTravelRequest.getRequestRemarks()==null){
							requestList.put("requestRemarks","N");	
						}else{
						 requestList.put("requestRemarks", allTravelRequest.getRequestRemarks());
						}					
						
						if(allTravelRequest.getLocationFlg()==null){
							requestList.put("locationFlg","N");
						}else{
							requestList.put("locationFlg",allTravelRequest.getLocationFlg());
						}
						try {
							if(allTravelRequest.getLocationFlg().equalsIgnoreCase("M")){
								Map<String, Object> areaList = listOfPointsForMultipleLocation(
										allTravelRequest.getLocationWaypointsIds(),
										allTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),allTravelRequest.getEfmFmUserMaster().getUserId(),employeeTravelRequestPO.getCombinedFacility());
								if (areaList.size() > 0) {
									requestList.put("employeeWaypoints", areaList);
								} else {
									requestList.put("employeeWaypoints", "NA");
								}
							}
						} catch (Exception e) {
							log.debug("location Details are not updated_Location_flg is null");
						}		
						travelRequestList.add(requestList);
					}
				}

			}
		}
		responce.put("requests", travelRequestList);
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	
	
	

	/*
	 * Reschedule Request call only from device 
	 * 
	 */

	@POST
	@Path("/requestsforreshedule")
	public Response getEmployeeRequestsForReshedule(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws UnsupportedEncodingException, ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");		 
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getEfmFmUserMaster().getUserId());	 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 if(employeeTravelRequestPO.getStartPgNo()==0){
		 try{
	 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getEfmFmUserMaster().getUserId()))){
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
	 			if (!(userDetail.isEmpty())) {
	 				String jwtToken = "";
	 				try {
	 					JwtTokenGenerator token = new JwtTokenGenerator();
	 					jwtToken = token.generateToken();
	 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
	 					userDetail.get(0).setMobTokenGenerationTime(new Date());
	 					userMasterBO.update(userDetail.get(0));
	 				} catch (Exception e) {
	 					log.info("error" + e);
	 				}
	                responce.put("token", jwtToken);
	 			}
	 		
	 		}catch(Exception e){
	 				log.info("authentication error"+e);
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
		 
	}
	 		 
	 		 
		 List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> shitTimings = new ArrayList<Map<String, Object>>();
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
		int count = 0;
		List<EFmFmEmployeeTravelRequestPO> employeeDetail = iCabRequestBO
				.getAllTodaysRequestForParticularEmployee(employeeTravelRequestPO);
		if (!employeeDetail.isEmpty()) {

			List<EFmFmEmployeeTravelRequestPO> todayrequests = iCabRequestBO
					.getAllRequestForParticularEmployeeBasedOnMobileNumber(
							new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getEfmFmUserMaster().getUserId()
									,employeeTravelRequestPO.getCombinedFacility()),
							employeeDetail.get(0).getEfmFmUserMaster().getMobileNumber(),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
			log.info("size" + todayrequests.size());
			List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO
					.listOfShiftTimeForEmployees(new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getEfmFmUserMaster().getUserId()
							,employeeTravelRequestPO.getCombinedFacility()));
			if (!(todayrequests.isEmpty())) {
				if (!(shiftTimeDetails.isEmpty())) {
					for (EFmFmTripTimingMasterPO shiftiming : shiftTimeDetails) {
						Map<String, Object> shifList = new HashMap<String, Object>();
						shifList.put("shiftTime", shiftTimeFormater.format(shiftiming.getShiftTime()));
						shifList.put("shiftId", shiftiming.getShiftId());
						shifList.put("cutOffTime", shiftTimeFormater.format(shiftiming.getCutOffTime()));
						shifList.put("rescheduleCutOffTime",
								shiftTimeFormater.format(shiftiming.getRescheduleCutOffTime()));
						shifList.put("cutOffTimeFlg", shiftiming.geteFmFmClientBranchPO().getCutOffTime());
						shitTimings.add(shifList);
					}
				}
			}
			if (!(todayrequests.isEmpty())) {
				outer: for (EFmFmEmployeeTravelRequestPO allTravelRequest : todayrequests) {
					String currentDay = new SimpleDateFormat("EEEE").format(allTravelRequest.getRequestDate());
					if (allTravelRequest.getEfmFmUserMaster().getWeekOffDays().contains(currentDay)) {
						// Only For Shell Chennai Requirements or who ever have
						// drop on saturday morning
						if (!(currentDay.equalsIgnoreCase("Saturday")
								&& allTravelRequest.getTripType().equalsIgnoreCase("DROP")
								&& (allTravelRequest.getShiftTime().getHours() == 1
										|| allTravelRequest.getShiftTime().getHours() == 3
										|| allTravelRequest.getShiftTime().getHours() == 5
										|| allTravelRequest.getShiftTime().getHours() == 7))) {
							continue outer;
						}
					}
					count++;
					if (count <= 11) {
						Map<String, Object> requestList = new HashMap<String, Object>();
						requestList.put("employeeId",
								allTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getEmployeeId());
						requestList.put("userId",
								allTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getUserId());
						requestList.put("requestId", allTravelRequest.getRequestId());
						if (allTravelRequest.geteFmFmEmployeeRequestMaster().getTripType().equalsIgnoreCase("DROP")) {
							requestList.put("rescheduleCutOffTime", allTravelRequest.getEfmFmUserMaster()
									.geteFmFmClientBranchPO().getRescheduleDropCutOffTime());
						} else {
							requestList.put("rescheduleCutOffTime", allTravelRequest.getEfmFmUserMaster()
									.geteFmFmClientBranchPO().getReschedulePickupCutOffTime());
						}
						requestList.put("tripType", allTravelRequest.geteFmFmEmployeeRequestMaster().getTripType());
						requestList.put("address",
								new String(
										Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()),
										"utf-8"));
						requestList.put("requestStatus", allTravelRequest.getRequestStatus());
						requestList.put("requestDate", formatter.format(allTravelRequest.getRequestDate()));
						requestList.put("requestType", allTravelRequest.getRequestType());
						requestList.put("facilityName", allTravelRequest.geteFmFmClientBranchPO().getBranchName());
						requestList.put("facilityId", allTravelRequest.geteFmFmClientBranchPO().getBranchId());
						requestList.put("approveStatus", allTravelRequest.getApproveStatus());
						requestList.put("activeStatus", allTravelRequest.getIsActive());
						requestList.put("tripTime", shiftTimeFormater.format(allTravelRequest.getShiftTime()));
						responce.put("startPgNo", (employeeTravelRequestPO.getStartPgNo()+employeeTravelRequestPO.getEndPgNo()));
						if(allTravelRequest.getLocationFlg()==null){
							requestList.put("locationFlg","N");
						}else{
							requestList.put("locationFlg",allTravelRequest.getLocationFlg());
						}
						try {
							if(allTravelRequest.getLocationFlg().equalsIgnoreCase("M")){
								Map<String, Object> areaList = listOfPointsForMultipleLocation(
										allTravelRequest.getLocationWaypointsIds(),
										allTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),allTravelRequest.getEfmFmUserMaster().getUserId(),employeeTravelRequestPO.getCombinedFacility());
								if (areaList.size() > 0) {
									requestList.put("employeeWaypoints", areaList);
								} else {
									requestList.put("employeeWaypoints", "NA");
								}
							}
						} catch (Exception e) {
							log.debug("location Details are not updated_Location_flg is null");
						}
						travelRequestList.add(requestList);
					}
				}
			}
		}
		responce.put("requests", travelRequestList);
		responce.put("shifts", shitTimings);
		responce.put("status", "success");

		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	
	/*
	 * Reschedule Request call only from Web 
	 * 
	 */

	@POST
	@Path("/requestsforWebreshedule")
	public Response getEmployeeRequestsForResheduleForWeb(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws UnsupportedEncodingException, ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");		 
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getEfmFmUserMaster().getUserId());	 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 if(employeeTravelRequestPO.getStartPgNo()==0){
		 try{
	 			if(!(userMasterBO.checkTokenValidOrNot	(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getEfmFmUserMaster().getUserId()))){
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
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
	                responce.put("token", jwtToken);
	 			}
	 		
	 		}catch(Exception e){
	 				log.info("authentication error"+e);
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	      } 
	 		 
		 List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> shitTimings = new ArrayList<Map<String, Object>>();
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
		int count = 0;
		List<EFmFmEmployeeTravelRequestPO> employeeDetail = iCabRequestBO
				.getAllTodaysRequestForParticularEmployee(employeeTravelRequestPO);
		if (!employeeDetail.isEmpty()) {

			List<EFmFmEmployeeTravelRequestPO> todayrequests = iCabRequestBO
					.getAllRequestForParticularEmployeeBasedOnMobileNumber(
							new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getEfmFmUserMaster().getUserId()
									,employeeTravelRequestPO.getCombinedFacility()),
							employeeDetail.get(0).getEfmFmUserMaster().getMobileNumber(),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
			log.info("size" + todayrequests.size());
			List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO
					.listOfShiftTimeForEmployees(new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getEfmFmUserMaster().getUserId()
							,employeeTravelRequestPO.getCombinedFacility()));
			if (!(todayrequests.isEmpty())) {
				if (!(shiftTimeDetails.isEmpty())) {
					for (EFmFmTripTimingMasterPO shiftiming : shiftTimeDetails) {
						Map<String, Object> shifList = new HashMap<String, Object>();
						shifList.put("shiftTime", shiftTimeFormater.format(shiftiming.getShiftTime()));
						shifList.put("shiftId", shiftiming.getShiftId());
						shifList.put("cutOffTime", shiftTimeFormater.format(shiftiming.getCutOffTime()));
						shifList.put("rescheduleCutOffTime",
								shiftTimeFormater.format(shiftiming.getRescheduleCutOffTime()));
						shifList.put("cutOffTimeFlg", shiftiming.geteFmFmClientBranchPO().getCutOffTime());
						shitTimings.add(shifList);
					}
				}
			}
			if (!(todayrequests.isEmpty())) {
				outer: for (EFmFmEmployeeTravelRequestPO allTravelRequest : todayrequests) {
					String currentDay = new SimpleDateFormat("EEEE").format(allTravelRequest.getRequestDate());
					if (allTravelRequest.getEfmFmUserMaster().getWeekOffDays().contains(currentDay)) {
						// Only For Shell Chennai Requirements or who ever have
						// drop on saturday morning
						if (!(currentDay.equalsIgnoreCase("Saturday")
								&& allTravelRequest.getTripType().equalsIgnoreCase("DROP")
								&& (allTravelRequest.getShiftTime().getHours() == 1
										|| allTravelRequest.getShiftTime().getHours() == 3
										|| allTravelRequest.getShiftTime().getHours() == 5
										|| allTravelRequest.getShiftTime().getHours() == 7))) {
							continue outer;
						}
					}
					count++;
					if (count <= 11) {
						Map<String, Object> requestList = new HashMap<String, Object>();
						requestList.put("employeeId",
								allTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getEmployeeId());
						requestList.put("userId",
								allTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getUserId());
						requestList.put("requestId", allTravelRequest.getRequestId());
						if (allTravelRequest.geteFmFmEmployeeRequestMaster().getTripType().equalsIgnoreCase("DROP")) {
							requestList.put("rescheduleCutOffTime", allTravelRequest.getEfmFmUserMaster()
									.geteFmFmClientBranchPO().getRescheduleDropCutOffTime());
						} else {
							requestList.put("rescheduleCutOffTime", allTravelRequest.getEfmFmUserMaster()
									.geteFmFmClientBranchPO().getReschedulePickupCutOffTime());
						}
						requestList.put("tripType", allTravelRequest.geteFmFmEmployeeRequestMaster().getTripType());
						requestList.put("address",
								new String(
										Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()),
										"utf-8"));
						requestList.put("requestStatus", allTravelRequest.getRequestStatus());
						requestList.put("requestDate", formatter.format(allTravelRequest.getRequestDate()));
						requestList.put("requestType", allTravelRequest.getRequestType());
						requestList.put("approveStatus", allTravelRequest.getApproveStatus());
						requestList.put("activeStatus", allTravelRequest.getIsActive());
						requestList.put("tripTime", shiftTimeFormater.format(allTravelRequest.getShiftTime()));
						responce.put("startPgNo", (employeeTravelRequestPO.getStartPgNo()+employeeTravelRequestPO.getEndPgNo()));
						if(allTravelRequest.getLocationFlg()==null){
							requestList.put("locationFlg","N");
						}else{
							requestList.put("locationFlg",allTravelRequest.getLocationFlg());
						}
						try {
							if(allTravelRequest.getLocationFlg().equalsIgnoreCase("M")){
								Map<String, Object> areaList = listOfPointsForMultipleLocation(
										allTravelRequest.getLocationWaypointsIds(),
										allTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),allTravelRequest.getEfmFmUserMaster().getUserId(),employeeTravelRequestPO.getCombinedFacility());
								if (areaList.size() > 0) {
									requestList.put("employeeWaypoints", areaList);
								} else {
									requestList.put("employeeWaypoints", "NA");
								}
							}
						} catch (Exception e) {
							log.debug("location Details are not updated_Location_flg is null");
						}
						travelRequestList.add(requestList);
					}
				}
			}
		}
		responce.put("requests", travelRequestList);
		responce.put("shifts", shitTimings);
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Cancel Request call from device only  
	 * 
	 */

	@POST
	@Path("/requestsforcancel")
	public Response getEmployeeRequestsForCancellation(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException, UnsupportedEncodingException {
		Map<String, Object> responce = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getEfmFmUserMaster().getUserId());		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 if(employeeTravelRequestPO.getStartPgNo()==0){
		 try{
	 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getEfmFmUserMaster().getUserId()))){
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
	 			if (!(userDetail.isEmpty())) {
	 				String jwtToken = "";
	 				try {
	 					JwtTokenGenerator token = new JwtTokenGenerator();
	 					jwtToken = token.generateToken();
	 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
	 					userDetail.get(0).setMobTokenGenerationTime(new Date());
	 					userMasterBO.update(userDetail.get(0));
	 				} catch (Exception e) {
	 					log.info("error" + e);
	 				}
	                responce.put("token", jwtToken);
	 			}
	 		
	 		}catch(Exception e){
	 				log.info("authentication error"+e);
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		 
	 		 
	}
		 
		 int count = 0;
		List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
		List<EFmFmEmployeeTravelRequestPO> employeeDetail = iCabRequestBO
				.getAllTodaysRequestForParticularEmployee(employeeTravelRequestPO);
		if (!employeeDetail.isEmpty()) {
			List<EFmFmEmployeeTravelRequestPO> todayrequests = iCabRequestBO
					.getAllRequestForParticularEmployeeBasedOnMobileNumber(
							new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getEfmFmUserMaster().getUserId()
									,employeeTravelRequestPO.getCombinedFacility()),
							employeeDetail.get(0).getEfmFmUserMaster().getMobileNumber(),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
			log.info("size" + todayrequests.size());
			if (!(todayrequests.isEmpty())) {
				outer: for (EFmFmEmployeeTravelRequestPO allTravelRequest : todayrequests) {
					String currentDay = new SimpleDateFormat("EEEE").format(allTravelRequest.getRequestDate());
					if (allTravelRequest.getEfmFmUserMaster().getWeekOffDays().contains(currentDay)) {
						// Only For Shell Chennai Requirements or who ever have
						// drop on saturday morning
						if (!(currentDay.equalsIgnoreCase("Saturday")
								&& allTravelRequest.getTripType().equalsIgnoreCase("DROP")
								&& (allTravelRequest.getShiftTime().getHours() == 1
										|| allTravelRequest.getShiftTime().getHours() == 3
										|| allTravelRequest.getShiftTime().getHours() == 5
										|| allTravelRequest.getShiftTime().getHours() == 7))) {
							continue outer;
						}
					}
					count++;
					if (count <= 11) {
						Map<String, Object> requestList = new HashMap<String, Object>();
						requestList.put("userId", allTravelRequest.getEfmFmUserMaster().getUserId());
						requestList.put("requestId", allTravelRequest.getRequestId());
						requestList.put("tripType", allTravelRequest.getTripType());
						requestList.put("employeeAddress",
								new String(
										Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()),
										"utf-8"));
						requestList.put("address",
								new String(
										Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()),
										"utf-8"));
						requestList.put("requestType", allTravelRequest.getRequestType());
						
						if(allTravelRequest.getLocationFlg()==null){
							requestList.put("locationFlg","N");
						}else{
							requestList.put("locationFlg",allTravelRequest.getLocationFlg());
						}
						try {
							if(allTravelRequest.getLocationFlg().equalsIgnoreCase("M")){
								Map<String, Object> areaList = listOfPointsForMultipleLocation(
										allTravelRequest.getLocationWaypointsIds(),
										allTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),allTravelRequest.getEfmFmUserMaster().getUserId(),employeeTravelRequestPO.getCombinedFacility());
								if (areaList.size() > 0) {
									requestList.put("employeeWaypoints", areaList);
								} else {
									requestList.put("employeeWaypoints", "NA");
								}
							}
						} catch (Exception e) {
							log.debug("location Details are not updated_Location_flg is null");
						}						
						requestList.put("requestDate", dateFormatter.format(allTravelRequest.getRequestDate()));
						requestList.put("tripTime", shiftTimeFormater.format(allTravelRequest.getShiftTime()));
						requestList.put("facilityName", allTravelRequest.geteFmFmClientBranchPO().getBranchName());
						requestList.put("facilityId", allTravelRequest.geteFmFmClientBranchPO().getBranchId());
						responce.put("startPgNo", (employeeTravelRequestPO.getStartPgNo()+employeeTravelRequestPO.getEndPgNo()));
						requestList.put("cutOffFlg",
								allTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getCutOffTime());
						if (allTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getCutOffTime()
								.equalsIgnoreCase("T")) {
							if (allTravelRequest.getTripType().equalsIgnoreCase("DROP")) {
								requestList.put("dropCancelTime", allTravelRequest.getEfmFmUserMaster()
										.geteFmFmClientBranchPO().getDropCancelTimePeriod());
							} else {
								requestList.put("pickupCancelTime", allTravelRequest.getEfmFmUserMaster()
										.geteFmFmClientBranchPO().getPickupCancelTimePeriod());
							}
						} else {
							List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO.getShiftIdUsingshiftTime(
									allTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
									allTravelRequest.getTripType(), allTravelRequest.getShiftTime());
							if (!shiftDetails.isEmpty()) {
								if (allTravelRequest.getTripType().equalsIgnoreCase("DROP")) {
									requestList.put("dropCancelTime", shiftDetails.get(0).getCancelCutoffTime());
								} else {
									requestList.put("pickupCancelTime", shiftDetails.get(0).getCancelCutoffTime());
								}
							}

						}
						travelRequestList.add(requestList);
					}
				}

			}
		}
		responce.put("requests", travelRequestList);
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Cancel Request call from Web only  
	 * 
	 */

	@POST
	@Path("/requestsforWebcancel")
	public Response getEmployeeRequestsForCancellationFromWeb(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException, UnsupportedEncodingException {
		Map<String, Object> responce = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getEfmFmUserMaster().getUserId());		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 if(employeeTravelRequestPO.getStartPgNo()==0){
		 try{
	 			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getEfmFmUserMaster().getUserId()))){
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getEfmFmUserMaster().getUserId());
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
	                responce.put("token", jwtToken);
	 			}
	 		
	 		}catch(Exception e){
	 				log.info("authentication error"+e);
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		 
	}
	 		 
		 
		 int count = 0;
		List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
		List<EFmFmEmployeeTravelRequestPO> employeeDetail = iCabRequestBO
				.getAllTodaysRequestForParticularEmployee(employeeTravelRequestPO);
		if (!employeeDetail.isEmpty()) {
			List<EFmFmEmployeeTravelRequestPO> todayrequests = iCabRequestBO
					.getAllRequestForParticularEmployeeBasedOnMobileNumber(
							new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getEfmFmUserMaster().getUserId()
									,employeeTravelRequestPO.getCombinedFacility()),
							employeeDetail.get(0).getEfmFmUserMaster().getMobileNumber(),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
			log.info("size" + todayrequests.size());
			if (!(todayrequests.isEmpty())) {
				outer: for (EFmFmEmployeeTravelRequestPO allTravelRequest : todayrequests) {
					String currentDay = new SimpleDateFormat("EEEE").format(allTravelRequest.getRequestDate());
					if (allTravelRequest.getEfmFmUserMaster().getWeekOffDays().contains(currentDay)) {
						// Only For Shell Chennai Requirements or who ever have
						// drop on saturday morning
						if (!(currentDay.equalsIgnoreCase("Saturday")
								&& allTravelRequest.getTripType().equalsIgnoreCase("DROP")
								&& (allTravelRequest.getShiftTime().getHours() == 1
										|| allTravelRequest.getShiftTime().getHours() == 3
										|| allTravelRequest.getShiftTime().getHours() == 5
										|| allTravelRequest.getShiftTime().getHours() == 7))) {
							continue outer;
						}
					}
					count++;
					if (count <= 11) {
						Map<String, Object> requestList = new HashMap<String, Object>();
						requestList.put("userId", allTravelRequest.getEfmFmUserMaster().getUserId());
						requestList.put("requestId", allTravelRequest.getRequestId());
						requestList.put("tripType", allTravelRequest.getTripType());
						requestList.put("employeeAddress",
								new String(
										Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()),
										"utf-8"));
						requestList.put("address",
								new String(
										Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()),
										"utf-8"));
						requestList.put("requestType", allTravelRequest.getRequestType());
						
						if(allTravelRequest.getLocationFlg()==null){
							requestList.put("locationFlg","N");
						}else{
							requestList.put("locationFlg",allTravelRequest.getLocationFlg());
						}
						try {
							if(allTravelRequest.getLocationFlg().equalsIgnoreCase("M")){
								Map<String, Object> areaList = listOfPointsForMultipleLocation(
										allTravelRequest.getLocationWaypointsIds(),
										allTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),allTravelRequest.getEfmFmUserMaster().getUserId(),employeeTravelRequestPO.getCombinedFacility());
								if (areaList.size() > 0) {
									requestList.put("employeeWaypoints", areaList);
								} else {
									requestList.put("employeeWaypoints", "NA");
								}
							}
						} catch (Exception e) {
							log.debug("location Details are not updated_Location_flg is null");
						}						
						requestList.put("requestDate", dateFormatter.format(allTravelRequest.getRequestDate()));
						requestList.put("tripTime", shiftTimeFormater.format(allTravelRequest.getShiftTime()));
						responce.put("startPgNo", (employeeTravelRequestPO.getStartPgNo()+employeeTravelRequestPO.getEndPgNo()));
						requestList.put("cutOffFlg",
								allTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getCutOffTime());
						if (allTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getCutOffTime()
								.equalsIgnoreCase("T")) {
							if (allTravelRequest.getTripType().equalsIgnoreCase("DROP")) {
								requestList.put("dropCancelTime", allTravelRequest.getEfmFmUserMaster()
										.geteFmFmClientBranchPO().getDropCancelTimePeriod());
							} else {
								requestList.put("pickupCancelTime", allTravelRequest.getEfmFmUserMaster()
										.geteFmFmClientBranchPO().getPickupCancelTimePeriod());
							}
						} else {
							List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO.getShiftIdUsingshiftTime(
									allTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
									allTravelRequest.getTripType(), allTravelRequest.getShiftTime());
							if (!shiftDetails.isEmpty()) {
								if (allTravelRequest.getTripType().equalsIgnoreCase("DROP")) {
									requestList.put("dropCancelTime", shiftDetails.get(0).getCancelCutoffTime());
								} else {
									requestList.put("pickupCancelTime", shiftDetails.get(0).getCancelCutoffTime());
								}
							}

						}
						travelRequestList.add(requestList);
					}
				}

			}
		}
		responce.put("requests", travelRequestList);
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	
	/*
	 * Reshedule Request call from device only
	 * 
	 */

	@POST
	@Path("/resheduleRequestForDevice")
	public Response getEmployeeRequestsForResheduleForDevice(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException {
		Map<String, Object> responce = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		if(employeeTravelRequestPO.getStartPgNo()==0){
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
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
		   }
		   
	}
		   
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
		// IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO)
		// ContextLoader.getContext().getBean("IEmployeeDetailBO");
		List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
		DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");

		List<EFmFmEmployeeTravelRequestPO> employeeDetail = iCabRequestBO
				.getAllTodaysRequestForParticularEmployee(employeeTravelRequestPO);
		if (!(employeeDetail.isEmpty())) {
			List<EFmFmEmployeeTravelRequestPO> todayrequests = iCabRequestBO
					.getAllRequestForParticularEmployeeBasedOnMobileNumber(
							new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getEfmFmUserMaster().getUserId()
									,employeeTravelRequestPO.getCombinedFacility()),
							employeeDetail.get(0).getEfmFmUserMaster().getMobileNumber(),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
			log.info("size" + todayrequests.size());

			if (!(todayrequests.isEmpty())) {
				for (EFmFmEmployeeTravelRequestPO allTravelRequest : todayrequests) {
					String requestDate = dateFormatter.format(allTravelRequest.getRequestDate());
					String requestDateShiftTime = requestDate + " " + allTravelRequest.getShiftTime();
					Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
					if (shiftDateAndTime.getTime() > new Date().getTime()) {
						Map<String, Object> requestList = new HashMap<String, Object>();
						requestList.put("userId", allTravelRequest.getEfmFmUserMaster().getUserId());
						requestList.put("requestId", allTravelRequest.getRequestId());
						requestList.put("tripType", allTravelRequest.getTripType());
						responce.put("startPgNo", (employeeTravelRequestPO.getStartPgNo()+employeeTravelRequestPO.getEndPgNo()));
						// requestList.put("requestStatus",
						// allTravelRequest.getRequestStatus());
						requestList.put("requestType", allTravelRequest.getRequestType());
						// requestList.put("approveStatus",
						// allTravelRequest.getApproveStatus());
						// requestList.put("activeStatus",
						// allTravelRequest.getIsActive());
						requestList.put("requestDate", dateFormatter.format(allTravelRequest.getRequestDate()));
						requestList.put("tripTime", shiftTimeFormater.format(allTravelRequest.getShiftTime()));
						// List<EFmFmUserMasterPO>
						// employeeDetails=iEmployeeDetailBO.getParticularEmpDetailsFromUserId(employeeTravelRequestPO.getEfmFmUserMaster().getUserId(),employeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
						// if(!(employeeDetails.isEmpty())){
						// for(EFmFmUserMasterPO userMasterPO:employeeDetails){
						// requestList.put("employeeId",
						// userMasterPO.getEmployeeId());
						// requestList.put("employeeName",
						// userMasterPO.getFirstName());
						// requestList.put("employeeAddress",
						// userMasterPO.getAddress());
						// requestList.put("employeeWaypoints",
						// userMasterPO.getLatitudeLongitude());
						// }
						// }
						travelRequestList.add(requestList);
					}
				}

			}
		}
		responce.put("requests", travelRequestList);
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	/*
	 * list of Reschedule requests for browser
	 * 
	 */

	@POST
	@Path("/reshedulerequest")
	public Response getAllResheduleRequestsDetails(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws UnsupportedEncodingException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
		IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
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
		   }
		 
		List<Map<String, Object>> resheduleRequestList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		employeeTravelRequestPO.setRequestDate(new Date());
		List<EFmFmUserMasterPO> loggedInUserDetail = iEmployeeDetailBO.getParticularEmpDetailsFromUserId(
				employeeTravelRequestPO.getEfmFmUserMaster().getUserId(), employeeTravelRequestPO
						.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
		List<EFmFmEmployeeTravelRequestPO> travelDetails = iCabRequestBO.getAllResheduleRequests(
				loggedInUserDetail.get(0).geteFmFmClientProjectDetails().getProjectId(), employeeTravelRequestPO
						.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
		if ((!(travelDetails.isEmpty())) || travelDetails.size() != 0) {
			for (EFmFmEmployeeTravelRequestPO allTravelRequest : travelDetails) {
				Map<String, Object> requestList = new HashMap<String, Object>();
				requestList.put("employeeId",
						allTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getEmployeeId());
				requestList.put("requestId", allTravelRequest.getRequestId());
				requestList.put("tripType", allTravelRequest.geteFmFmEmployeeRequestMaster().getTripType());
				requestList.put("requestStatus", allTravelRequest.getRequestStatus());
				requestList.put("tripTime", timeFormat.format(allTravelRequest.getShiftTime()));
				List<EFmFmUserMasterPO> userDetails = iEmployeeDetailBO.getParticularEmpDetailsFromUserId(
						allTravelRequest.getEfmFmUserMaster().getUserId(),
						employeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
								.geteFmFmClientBranchPO().getBranchId());
				if ((!(userDetails.isEmpty())) || userDetails.size() != 0) {
					for (EFmFmUserMasterPO employeeMasterPO : userDetails) {
						requestList.put("employeeName",
								new String(Base64.getDecoder().decode(employeeMasterPO.getFirstName()), "utf-8"));
						requestList.put("employeeNumber",
								new String(Base64.getDecoder().decode(employeeMasterPO.getMobileNumber()), "utf-8"));
						requestList.put("emailId",
								new String(Base64.getDecoder().decode(employeeMasterPO.getEmailId()), "utf-8"));
						requestList.put("employeeAddress",
								new String(Base64.getDecoder().decode(employeeMasterPO.getAddress()), "utf-8"));
						requestList.put("employeeWaypoints", employeeMasterPO.getLatitudeLongitude());
					}
				}
				resheduleRequestList.add(requestList);
			}

		}
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(resheduleRequestList, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Employee cancel request from web from employee login page
	 * 
	 * @throws ParseException
	 * 
	 */
	@POST
	@Path("/employeerequestdelete")
	public Response employeeCancelRequestFromWeb(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO)
			throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + eFmFmEmployeeTravelRequestPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEmployeeTravelRequestPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmEmployeeTravelRequestPO.getUserId());
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
		   }
		 
		eFmFmEmployeeTravelRequestPO.setRequestDate(new Date());
		
		List<EFmFmEmployeeTravelRequestPO> cabRequest = iCabRequestBO
				.getparticularRequestDetail(eFmFmEmployeeTravelRequestPO);
		cabRequest.get(cabRequest.size() - 1).setIsActive("N");
		cabRequest.get(cabRequest.size() - 1).setRequestStatus("CW");
		cabRequest.get(cabRequest.size() - 1).setApproveStatus("Y");
		cabRequest.get(cabRequest.size() - 1).setReadFlg("N");
		String value = "";
		if (cabRequest.get(cabRequest.size() - 1).getEfmFmUserMaster().geteFmFmClientBranchPO().getCutOffTime()
				.equalsIgnoreCase("T")) {
			if (cabRequest.get(cabRequest.size() - 1).getTripType().equalsIgnoreCase("DROP")) {
				value = cutOffTimeCancelValidation(cabRequest.get(cabRequest.size() - 1).getTripType(),
						cabRequest.get(cabRequest.size() - 1).getRequestDate(),
						cabRequest.get(cabRequest.size() - 1).getShiftTime(),
						cabRequest.get(cabRequest.size() - 1).getEfmFmUserMaster().geteFmFmClientBranchPO()
								.getBranchId(),
						cabRequest.get(cabRequest.size() - 1).getEfmFmUserMaster().geteFmFmClientBranchPO()
								.getDropCancelTimePeriod());
			} else {
				value = cutOffTimeCancelValidation(cabRequest.get(cabRequest.size() - 1).getTripType(),
						cabRequest.get(cabRequest.size() - 1).getRequestDate(),
						cabRequest.get(cabRequest.size() - 1).getShiftTime(),
						cabRequest.get(cabRequest.size() - 1).getEfmFmUserMaster().geteFmFmClientBranchPO()
								.getBranchId(),
						cabRequest.get(cabRequest.size() - 1).getEfmFmUserMaster().geteFmFmClientBranchPO()
								.getPickupCancelTimePeriod());
			}
		} else {
			List<EFmFmTripTimingMasterPO> shiftDetails = iCabRequestBO.getShiftIdUsingshiftTime(
					cabRequest.get(cabRequest.size() - 1).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
					cabRequest.get(cabRequest.size() - 1).getTripType(),
					cabRequest.get(cabRequest.size() - 1).getShiftTime());
			if (!shiftDetails.isEmpty()) {
				value = cutOffTimeCancelValidation(cabRequest.get(cabRequest.size() - 1).getTripType(),
						cabRequest.get(cabRequest.size() - 1).getRequestDate(),
						cabRequest.get(cabRequest.size() - 1).getShiftTime(), cabRequest.get(cabRequest.size() - 1)
								.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
						shiftDetails.get(0).getCancelCutoffTime());
			}
		}
		if (value.equalsIgnoreCase("validCancelTime")) {
			try {
				String toMailId = new String(Base64.getDecoder()
						.decode(cabRequest.get(cabRequest.size() - 1).getEfmFmUserMaster().getEmailId()), "utf-8");
				Time shifTime=cabRequest.get(0).getShiftTime();
				String tripType=cabRequest.get(0).getTripType();
				String feedBackMailId=cabRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId();
                Date requestDate=cabRequest.get(0).getRequestDate();

				log.info("web cancel  mail confirmation");
				Thread thread1 = new Thread(new Runnable() {
					@Override
					public void run() {
						SendMailBySite mailSender = new SendMailBySite();
						mailSender.cancelRequestMailTemplate(toMailId, shiftTimeFormater.format(shifTime), tripType, dateFormatter.format(requestDate), feedBackMailId);
					}
				});
				thread1.start();
			} catch (Exception e) {
				log.info("Error web cancel  mail confirmation" + e);
			}

			iCabRequestBO.update(cabRequest.get(cabRequest.size() - 1));
			responce.put("status", "success");
		} else {
			responce.put("status", "failure");
		}
		/*
		 * iCabRequestBO.update(cabRequest.get(cabRequest.size() - 1));
		 * responce.put("status", "success");
		 */
		 log.info("serviceEnd -UserId :" + eFmFmEmployeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/deleteRequestTravelDesk")
	public Response requestRemovedFromTravelDesk(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO)
			throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + eFmFmEmployeeTravelRequestPO.getUserId());
		Map<String, Object> responce = new HashMap<String, Object>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEmployeeTravelRequestPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmEmployeeTravelRequestPO.getUserId());
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
		   }
		 
		log.info("delete button click on travel request");
		try {
			List<EFmFmEmployeeTravelRequestPO> employeeTravelRequest = iCabRequestBO
					.getParticularRequestDetailOnTripComplete(eFmFmEmployeeTravelRequestPO.getRequestId());
			employeeTravelRequest.get(0).setRequestStatus("C");
			employeeTravelRequest.get(0).setIsActive("N");
			employeeTravelRequest.get(0).setApproveStatus("Y");
			employeeTravelRequest.get(0).setReadFlg("N");
			iCabRequestBO.update(employeeTravelRequest.get(0));
		} catch (Exception e) {
			log.info("delete button click on travel request" + e);
		}
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + eFmFmEmployeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	// delete shiftRequest from employee Travel Requests
		@POST
		@Path("/shiftRosterForMultipleLocation")
		@Produces("application/vnd.ms-excel")
		public Response shiftRosterForMultipleLocation(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
				throws ParseException, UnsupportedEncodingException {
			
			IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
			Map<String, Object> responce = new HashMap<String, Object>();
			
	  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
			try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}}catch(Exception e){
				log.info("authentication error"+e);
					responce.put("status", "invalidRequest");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();

			}

			List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
			   if (!(userDetailToken.isEmpty())) {
			    String jwtToken = "";
			    try {
			     JwtTokenGenerator token = new JwtTokenGenerator();
			     jwtToken = token.generateToken();
			     userDetailToken.get(0).setAuthorizationToken(jwtToken);
			     userDetailToken.get(0).setTokenGenerationTime(new Date());
			     userMasterBO.update(userDetailToken.get(0));
			    } catch (Exception e) {
			    	e.printStackTrace();
			     log.info("error" + e);
			    }
			   }
			
			   XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("Sheet1");
				XSSFCellStyle style = workbook.createCellStyle();
				XSSFFont font = workbook.createFont();
				font.setFontHeightInPoints((short) 12);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				style.setAlignment(CellStyle.ALIGN_LEFT);
				font.setColor(new XSSFColor(new java.awt.Color(255, 255, 255)));// color
				style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 82, 128)));
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				style.setFont(font);
				int rownum = 0;
				Row OutSiderow = sheet.createRow(rownum++);
				for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
					sheet.autoSizeColumn(columnIndex);
					Cell columnCol = OutSiderow.createCell(columnIndex);
					columnCol.setCellStyle(style);

				}
				
				Cell zerothCol = OutSiderow.createCell(0);
				zerothCol.setCellValue("Employee Id");
				zerothCol.setCellStyle(style);

				Cell firstCol = OutSiderow.createCell(1);
				firstCol.setCellValue("Employee Name");
				firstCol.setCellStyle(style);

				Cell secondCol = OutSiderow.createCell(2);
				secondCol.setCellValue("Gender");
				secondCol.setCellStyle(style);

				Cell thirdCol = OutSiderow.createCell(3);
				thirdCol.setCellValue("address");
				thirdCol.setCellStyle(style);

				Cell fourthCol = OutSiderow.createCell(4);
				fourthCol.setCellValue("Area Name");
				fourthCol.setCellStyle(style);

				Cell fifthCol = OutSiderow.createCell(5);
				fifthCol.setCellValue("Pick/Drop Sequence");
				fifthCol.setCellStyle(style);

				Cell sixthCol = OutSiderow.createCell(6);
				sixthCol.setCellValue("Trip Type");
				sixthCol.setCellStyle(style);

				Cell seventhCol = OutSiderow.createCell(7);
				seventhCol.setCellValue("Route Name");
				seventhCol.setCellStyle(style);

				Cell eighthCol = OutSiderow.createCell(8);
				eighthCol.setCellValue("Request Date");
				eighthCol.setCellStyle(style);

				Cell nineCol = OutSiderow.createCell(9);
				nineCol.setCellValue("Shift Time");
				nineCol.setCellStyle(style);

				Cell tenthhCol = OutSiderow.createCell(10);
				tenthhCol.setCellValue("Nodal Point name");
				tenthhCol.setCellStyle(style);
				
			ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
			 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
			DateFormat requestFormat = new SimpleDateFormat("dd/MM/yyyy");
			List<EFmFmEmployeeTravelRequestPO> travelDetails=null;		
			DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date =null; 
			String requestDate="";
			DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
			String shiftDate="";
			Date shift=null;
			java.sql.Time shiftTime=null;
			List<Map<String, Object>> shitTimings = new ArrayList<Map<String, Object>>();		
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");		
			employeeTravelRequestPO.setRequestDate(new Date());
			requestDate = requestDateFormat.format(new Date());		
			if(null !=employeeTravelRequestPO.getResheduleDate() && employeeTravelRequestPO.getResheduleDate()!=""){
				date = formatter.parse(employeeTravelRequestPO.getResheduleDate());
				requestDate = requestDateFormat.format(date);	
			}	
			
			
			//List<EFmFmEmployeeTravelRequestPO> travelDetails=null;
			
			 if(null !=employeeTravelRequestPO.getEmployeeId()){		 
				 try {
						if(employeeTravelRequestPO.getLocationFlg().equalsIgnoreCase("M")){
							travelDetails = iCabRequestBO
									.employeeRequestFromEmpIdByLocationFlg(employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getEmployeeId());
						}else{	
							travelDetails = iCabRequestBO
									.particularEmployeeRequestFromEmpId(employeeTravelRequestPO.getCombinedFacility(), employeeTravelRequestPO.getEmployeeId());
						}			
				}catch(Exception e) {
					e.printStackTrace();
					travelDetails = iCabRequestBO
							.particularEmployeeRequestFromEmpId(employeeTravelRequestPO.getCombinedFacility(), employeeTravelRequestPO.getEmployeeId());
				}	
		 }else{	
			
		
			if(employeeTravelRequestPO.getTypeExecution().equalsIgnoreCase("search") && !(employeeTravelRequestPO.getTime().equalsIgnoreCase("All"))){			
				shiftDate = employeeTravelRequestPO.getTime();
				shift = shiftFormate.parse(shiftDate);
			    shiftTime = new java.sql.Time(shift.getTime());			
				travelDetails = iCabRequestBO
						.getRewquestByShiftWiceForLocationFlg(
								employeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
										.geteFmFmClientBranchPO().getBranchId(),
								requestDate, shiftTime, employeeTravelRequestPO.getTripType());
				log.info("requestDate" + requestDate + "shiftTime" + shiftTime + "Request Size" + travelDetails.size());
			}else{
					
				travelDetails = iCabRequestBO
						.getEmpMultipleTravelledRequest(employeeTravelRequestPO.geteFmFmEmployeeRequestMaster()
								.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),requestDate);
			}
		 }
			
			
			
			
			log.info("route excel requestDate" + requestDate + "shiftTime" + shiftTime + "Request Size"
					+ travelDetails.size());
			
			try {			
		
				if(!(travelDetails.isEmpty())){			
				
						
				// iterating r number of rows
				for (int r = 1; r <= travelDetails.size(); r++) {
					OutSiderow = sheet.createRow(rownum++);

					Cell employeeId = OutSiderow.createCell(0);
					employeeId.setCellValue(travelDetails.get(r - 1).getEfmFmUserMaster().getEmployeeId());

					Cell employeeName = OutSiderow.createCell(1);
					employeeName.setCellValue(new String(
							Base64.getDecoder().decode(travelDetails.get(r - 1).getEfmFmUserMaster().getFirstName()), "utf-8"));

					Cell gender = OutSiderow.createCell(2);
					gender.setCellValue(new String(
							Base64.getDecoder().decode(travelDetails.get(r - 1).getEfmFmUserMaster().getGender()), "utf-8"));

					Cell address = OutSiderow.createCell(3);
					address.setCellValue(new String(
							Base64.getDecoder().decode(travelDetails.get(r - 1).getEfmFmUserMaster().getAddress()), "utf-8"));

					Cell areaName = OutSiderow.createCell(4);
					areaName.setCellValue(
							travelDetails.get(r - 1).geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());

					Cell time = OutSiderow.createCell(5);
					if (travelDetails.get(r - 1).getTripType().equalsIgnoreCase("DROP")) {
						time.setCellValue(travelDetails.get(r - 1).getDropSequence());
					} else {
						time.setCellValue((String) shiftFormate.format(travelDetails.get(r - 1).getPickUpTime()));
					}

					Cell tripType = OutSiderow.createCell(6);
					tripType.setCellValue(travelDetails.get(r - 1).getTripType());

					Cell routeName = OutSiderow.createCell(7);
					routeName.setCellValue(
							travelDetails.get(r - 1).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());

					Cell requestDateCol = OutSiderow.createCell(8);
					requestDateCol.setCellValue(requestFormat.format(travelDetails.get(r - 1).getRequestDate()));

					Cell shiftTimeCol = OutSiderow.createCell(9);
					shiftTimeCol.setCellValue(shiftFormate.format(travelDetails.get(r - 1).getShiftTime()));

					Cell nodalPoints = OutSiderow.createCell(10);
					nodalPoints.setCellValue(
							travelDetails.get(r - 1).geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());

				  }
				}			
			} catch (Exception e) {
				e.printStackTrace();
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
			 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
			 return response.build();
		}

	@POST
	@Path("/requestdelete")
	public Response requestRemoved(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + eFmFmEmployeeTravelRequestPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEmployeeTravelRequestPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmEmployeeTravelRequestPO.getUserId());
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
		   }
		 
		eFmFmEmployeeTravelRequestPO.setRequestDate(new Date());
	
		List<EFmFmEmployeeTravelRequestPO> cabRequest = iCabRequestBO
				.getparticularRequestDetail(eFmFmEmployeeTravelRequestPO);
		cabRequest.get(0).setRequestStatus("CW");
		cabRequest.get(0).setIsActive("N");
		cabRequest.get(0).setApproveStatus("Y");
		cabRequest.get(0).setReadFlg("N");
		iCabRequestBO.update(cabRequest.get(0));
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + eFmFmEmployeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/tripRequestVerified")
	public Response tripRequestVerified(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + eFmFmEmployeeTravelRequestPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEmployeeTravelRequestPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmEmployeeTravelRequestPO.getUserId());
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
		   }
		 
		eFmFmEmployeeTravelRequestPO.setRequestStatus("AL");
		iCabRequestBO.update(eFmFmEmployeeTravelRequestPO);
		 log.info("serviceEnd -UserId :" + eFmFmEmployeeTravelRequestPO.getUserId());
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/drivertrip")
	public Response triptoDriverDevice(EFmFmDeviceMasterPO eFmFmDeviceMasterPO)
			throws InvalidKeyException, NoSuchAlgorithmException, IOException, URISyntaxException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
//
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}
//		 
//		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
//		   if (!(userDetailToken.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
//		     userDetailToken.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetailToken.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
		 
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		 log.info("serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
		Map<String, Object> requests = new HashMap<String, Object>();
		Map<String, Object> tripSheet = new HashMap<String, Object>();
		List<Map<String, Object>> tripAllDetails = new ArrayList<Map<String, Object>>();
		
		List<EFmFmDeviceMasterPO> deviceDetail = iVehicleCheckInBO.deviceImeiNumberExistsCheck(String.valueOf(eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId()),
				eFmFmDeviceMasterPO.getImeiNumber());
		
		EFmFmVehicleCheckInPO vehicleCheckInPO = new EFmFmVehicleCheckInPO();
		EFmFmDeviceMasterPO deviceMasterPO = new EFmFmDeviceMasterPO();
		deviceMasterPO.setDeviceId(deviceDetail.get(0).getDeviceId());
		vehicleCheckInPO.seteFmFmDeviceMaster(deviceMasterPO);
		vehicleCheckInPO.setCheckInTime(new Date());
		List<EFmFmVehicleCheckInPO> checkInDetail = iVehicleCheckInBO.getParticularCheckedInDeviceDetails(
				eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId(), deviceDetail.get(0).getDeviceId());
		log.info("Device check in details" + checkInDetail.size());
		if (!(checkInDetail.isEmpty())) {
			EFmFmVehicleCheckInPO checkIn = new EFmFmVehicleCheckInPO();
			checkIn.setCheckInId(checkInDetail.get(0).getCheckInId());
			EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
			EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
			eFmFmClientBranch.setBranchId(eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId());
			assignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranch);
			assignRoutePO.setEfmFmVehicleCheckIn(checkIn);
			assignRoutePO.setTripAssignDate(new Date());
			// Method for geting the trip after bucket close only
			List<EFmFmAssignRoutePO> assignRoute = iCabRequestBO.closeVehicleCapacity(
					checkInDetail.get(0).getCheckInId(), eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId());
			log.info(
					"Size" + assignRoute.size() + "LatitudeAnd Longitude" + eFmFmDeviceMasterPO.getLatitudeLongitude());
			if (assignRoute.isEmpty()) {
				List<EFmCheckInVehicleTrackingPO> trackVehicleDetails = assignRouteBO.vehicleTrackingAfterCheckIn(String.valueOf(eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId()),
						checkInDetail.get(0).getCheckInId());
				if (!(trackVehicleDetails.isEmpty())) {
					String lastLatLong = trackVehicleDetails.get(trackVehicleDetails.size() - 1).getLatitudeLongitude();
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
					String currentLatLong = eFmFmDeviceMasterPO.getLatitudeLongitude();
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
					currentLatLong = currentLatFistDigit + "." + currentLat + "," + currentLongiFistDigit + "."
							+ currentLong;
					if (currentLatLong.equalsIgnoreCase(lastLatLong)) {
						log.info("inside equals geocode function");
						trackVehicleDetails.get(trackVehicleDetails.size() - 1).setCurrentTime(new Date());
						iCabRequestBO.update(trackVehicleDetails.get(trackVehicleDetails.size() - 1));
						 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
						return Response.ok(tripSheet, MediaType.APPLICATION_JSON).build();
					}
				}
				EFmCheckInVehicleTrackingPO eFmCheckInVehicleTracking = new EFmCheckInVehicleTrackingPO();
				eFmCheckInVehicleTracking.setLatitudeLongitude(eFmFmDeviceMasterPO.getLatitudeLongitude());
				eFmCheckInVehicleTracking.setCurrentTime(new Date());
				eFmCheckInVehicleTracking.setSpeed(eFmFmDeviceMasterPO.getSpeed());
				eFmCheckInVehicleTracking.setStatus("Y");
				eFmCheckInVehicleTracking.setTrackingType("mobile");
				eFmCheckInVehicleTracking.setCurrentCabLocation("NO");
				eFmCheckInVehicleTracking.setEfmFmVehicleCheckIn(checkInDetail.get(0));
				eFmCheckInVehicleTracking.seteFmFmClientBranchPO(eFmFmClientBranch);
				iCabRequestBO.save(eFmCheckInVehicleTracking);
				log.info("inside insert geocode function");
				 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
				return Response.ok(tripSheet, MediaType.APPLICATION_JSON).build();
			}
			if ((!(assignRoute.isEmpty()))) {
				if (!(checkInDetail.isEmpty()) && checkInDetail.get(0).getStatus().equalsIgnoreCase("Y")) {
					checkInDetail.get(0).setStatus("N");
					iVehicleCheckInBO.update(checkInDetail.get(0));
				}
				log.info("ClientLattiLongi" + assignRoute.get(0).geteFmFmClientBranchPO().getLatitudeLongitude());
				double totalDistance = Geocode.distance(
						new Geocode(assignRoute.get(0).geteFmFmClientBranchPO().getLatitudeLongitude()),
						new Geocode(eFmFmDeviceMasterPO.getLatitudeLongitude()));
				log.info("Distance ..." + totalDistance);
				// GeoFence on trip stated when vehicle out from office
				if (totalDistance > (assignRoute.get(0).geteFmFmClientBranchPO().getStartTripGeoFenceAreaInMeter())
						&& assignRoute.get(0).getTripStatus().equalsIgnoreCase("allocated")) {
					// Geo Fencing the client Office auto
//					PolygonalGeofence fence = new PolygonalGeofence();
//					StringTokenizer stringTokenizer = new StringTokenizer(
//							assignRoute.get(0).geteFmFmClientBranchPO().getGeoCodesForGeoFence(), "|");
//					String vertex = "";
//					while (stringTokenizer.hasMoreElements()) {
//						vertex = (String) stringTokenizer.nextElement();
//						fence.addVertex(new Geocode(vertex));
//					}
//					fence.setVehicleLocation(new Geocode(eFmFmDeviceMasterPO.getLatitudeLongitude()));
//					log.info("Inside auto trip start M Distance" + totalDistance);
//					if (!(fence.isInGeofence())) {
						log.info("Inside Geo Fence Reason" + totalDistance);
						try {
							assignRoutePO.setTripAssignDate(new Date());
							List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
							if (assignRoute.get(0).getTripType().equalsIgnoreCase("DROP")) {
								employeeTripDetailPO = iCabRequestBO
										.getDropTripAllSortedEmployees(assignRoutePO.getAssignRouteId());
							} else {
								employeeTripDetailPO = iCabRequestBO
										.getParticularTripAllEmployees(assignRoutePO.getAssignRouteId());
							}
							// start auto trip on start service
							if (!(employeeTripDetailPO.isEmpty())
									&& assignRoute.get(0).getRouteGenerationType().equalsIgnoreCase("AdhocRequest")) {
								for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
									String wayPointsAdhocRequest = "";
									if (employeeTripDetail.getEfmFmAssignRoute().getRouteGenerationType()
											.equalsIgnoreCase("AdhocRequest")) {
										wayPointsAdhocRequest = employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmEmployeeRequestMaster().getOriginLattitudeLongitude() + "|";
										if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmEmployeeRequestMaster()
												.getDestination1AddressLattitudeLongitude().equalsIgnoreCase("N")) {
											wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
													.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
													.getDestination1AddressLattitudeLongitude() + "|";
											if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
													.geteFmFmEmployeeRequestMaster()
													.getDestination2AddressLattitudeLongitude().equalsIgnoreCase("N")) {
												wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetail
														.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
														.getDestination2AddressLattitudeLongitude() + "|";
												if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
														.geteFmFmEmployeeRequestMaster()
														.getDestination3AddressLattitudeLongitude()
														.equalsIgnoreCase("N")) {
													wayPointsAdhocRequest = wayPointsAdhocRequest
															+ employeeTripDetail.geteFmFmEmployeeTravelRequest()
																	.geteFmFmEmployeeRequestMaster()
																	.getDestination3AddressLattitudeLongitude()
															+ "|";
													if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
															.geteFmFmEmployeeRequestMaster()
															.getDestination4AddressLattitudeLongitude()
															.equalsIgnoreCase("N")) {
														wayPointsAdhocRequest = wayPointsAdhocRequest
																+ employeeTripDetail.geteFmFmEmployeeTravelRequest()
																		.geteFmFmEmployeeRequestMaster()
																		.getDestination4AddressLattitudeLongitude()
																+ "|";
														if (!employeeTripDetail.geteFmFmEmployeeTravelRequest()
																.geteFmFmEmployeeRequestMaster()
																.getDestination5AddressLattitudeLongitude()
																.equalsIgnoreCase("N")) {
															wayPointsAdhocRequest = wayPointsAdhocRequest
																	+ employeeTripDetail.geteFmFmEmployeeTravelRequest()
																			.geteFmFmEmployeeRequestMaster()
																			.getDestination5AddressLattitudeLongitude()
																	+ "|";
														}
													}
												}
											}
										}
									}
								}
							}
							assignRoute.get(0).setTripStartTime(new Date());
							assignRoute.get(0).setOdometerStartKm("0");
							assignRoute.get(0).setTripStatus("Started");
                            assignRoute.get(0).setTripConfirmationFromDriver("Delivered");
							iCabRequestBO.update(assignRoute.get(0));
						} catch (Exception e) {
							log.info("Inside auto start" + e);
						}
//					}
				}
			}
			StringBuffer waypoints = new StringBuffer();
			StringBuffer multipleWaypoints = new StringBuffer();
			if (!(assignRoute.isEmpty())) {
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
				if (assignRoute.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
					employeeTripDetailPO = iCabRequestBO
							.getParticularTripAllEmployees(assignRoute.get(0).getAssignRouteId());
				} else {
					employeeTripDetailPO = iCabRequestBO
							.getDropTripAllSortedEmployees(assignRoute.get(0).getAssignRouteId());
				}
				if (!(employeeTripDetailPO.isEmpty())) {
					for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
						Map<String, Object> employeeDetails = new HashMap<String, Object>();
						List<EFmFmUserMasterPO> employeeDetail = employeeDetailBO
								.getParticularEmpDetailsFromUserIdWithOutStatus(
										employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getUserId(),
												String.valueOf(eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId()));
						employeeDetails.put("name",
								new String(Base64.getDecoder().decode(employeeDetail.get(0).getFirstName()), "utf-8"));
						employeeDetails.put("locationStatus", employeeDetail.get(0).getLocationStatus());
						employeeDetails.put("requestId",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestId());
						employeeDetails.put("requestType",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestType());
						employeeDetails.put("employeeId", employeeTripDetail.geteFmFmEmployeeTravelRequest()
								.getEfmFmUserMaster().getEmployeeId());
						employeeDetails.put("tripTime", shiftTimeFormater.format(assignRoute.get(0).getShiftTime()));
						employeeDetails.put("boardedFlg", employeeTripDetail.getBoardedFlg());
						employeeDetails.put("reachedFlg", employeeTripDetail.getReachedFlg());

						if (assignRoute.get(0).getRouteGenerationType().contains("nodal")) {
							employeeDetails.put("latLongi", employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints());
							employeeDetails.put("address", employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());
						} else {
							employeeDetails.put("address", new String(
									Base64.getDecoder().decode(employeeDetail.get(0).getAddress()), "utf-8"));
							employeeDetails.put("latLongi", employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.getEfmFmUserMaster().getLatitudeLongitude());
						}

						if (assignRoute.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
							employeeDetails.put("pickUpTime", shiftTimeFormater
									.format(employeeTripDetail.geteFmFmEmployeeTravelRequest().getPickUpTime()));
							try {
								    if(null!=employeeTripDetail.geteFmFmEmployeeTravelRequest().getModifiedPickUpTime()){
									 employeeDetails.put("modPickUpTime", shiftTimeFormater.format(employeeTripDetail.geteFmFmEmployeeTravelRequest().getModifiedPickUpTime()));
									}
								    if(null!=employeeTripDetail.geteFmFmEmployeeTravelRequest().getTripSheetStatus()){
										 employeeDetails.put("tripSheetStatus",employeeTripDetail.geteFmFmEmployeeTravelRequest().getModifiedPickUpTime());
										}
							} catch (Exception e) {
								log.debug("pickupTimeUpdated");
							}							
						}
						
						try {
							if (assignRoute.get(0).getLocationFlg().equalsIgnoreCase("M")) {
														
								Map<String,Object> waypointsList=multilocationlistOfWayPointsAfterAllocated(employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestId(),
										employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getUserId(),String.valueOf(eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId()));								
								if(!(waypointsList.isEmpty())){									
									for( Map.Entry<String,Object> listofValues:waypointsList.entrySet()){
										if(listofValues.getKey().equalsIgnoreCase("wayPointsList")){
											multipleWaypoints.append(listofValues.getValue());
										}else{
											employeeDetails.put("waypointsList",listofValues.getValue());
										}
										
									}				
								}				
							}
						} catch (Exception e) {
							log.debug("multilocation Value not updated");
						}
						
						
						
						tripAllDetails.add(employeeDetails);
					}
					if (assignRoute.get(0).getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoute.get(0).getNodalPoints());
					}

					if (!(tripAllDetails.isEmpty())
							&& !(assignRoute.get(0).getRouteGenerationType().contains("nodalAdhoc"))) {
						for (int i = 0; i <= tripAllDetails.size() - 1; i++) {
							if(assignRoute.get(0).getRouteGenerationType().equalsIgnoreCase("nodal")){
								if(!(waypoints.toString().contains(tripAllDetails.get(i).get("latLongi").toString()))){
									waypoints.append(tripAllDetails.get(i).get("latLongi") + "|");
								}
							}
							else{
							   waypoints.append(tripAllDetails.get(i).get("latLongi") + "|");
							}
						}
					}

				} else {
					if (assignRoute.get(0).getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoute.get(0).getNodalPoints());
					}

				}

				if (assignRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
					try {
						int a = assignRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId();
						requests.put("escortName",
								assignRoute.get(0).geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
					} catch (Exception e) {
						requests.put("escortId", "N");
						requests.put("escortName", "Escort Required But Not Available");

					}
				} else {
					requests.put("escortName", "Not Required");
				}
				requests.put("tripType", assignRoute.get(0).getTripType());
				requests.put("routeType", assignRoute.get(0).getRouteGenerationType());
				requests.put("tripStatus", assignRoute.get(0).getTripStatus());
				requests.put("locationFlg", assignRoute.get(0).getLocationFlg());
				requests.put("tripUpdateTime", assignRoute.get(0).getTripUpdateTime().getTime());
				requests.put("startKm", assignRoute.get(0).getOdometerStartKm());
				requests.put("baseLatLong", assignRoute.get(0).geteFmFmClientBranchPO().getLatitudeLongitude());
				if (assignRoute.get(0).getLocationFlg().equalsIgnoreCase("M")) {					
					requests.put("waypoints", multipleWaypoints);
				}else{
					requests.put("waypoints", waypoints);
				}
				
				requests.put("routeName",
						assignRoute.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				requests.put("assignRouteId", assignRoute.get(0).getAssignRouteId());
				requests.put("empDetails", tripAllDetails);
				requests.put("status", "success");
				tripSheet.put("data", requests);
			}
		} else {
			List<EFmFmVehicleCheckInPO> lastCheckInDetail = iVehicleCheckInBO.getLastCheckedOutInDeviceDetails(
					eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId(), deviceDetail.get(0).getDeviceId());
			log.info("deviceDetail.get(0).getDeviceId()" + deviceDetail.get(0).getDeviceId());
			if (!(lastCheckInDetail.isEmpty())) {
				requests.put("status", "fail");
				requests.put("vehicleNum",
						lastCheckInDetail.get(lastCheckInDetail.size() - 1).getEfmFmVehicleMaster().getVehicleNumber());
				tripSheet.put("data", requests);
			}
		}
		 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
		return Response.ok(tripSheet, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/tripstartkm")
	public Response tripStartKm(final EFmFmAssignRoutePO assignRoutePO) {
		 log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		Map<String, Object> requests = new HashMap<String, Object>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
//
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}
//		 
//		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
//		   if (!(userDetailToken.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
//		     userDetailToken.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetailToken.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
		 
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				try {
					Thread.sleep(3000);
					ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
					DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					String startTime = assignRoutePO.getTime();
					log.info("tripstartkm service called");
					Date rescheduleDate = dateTimeFormate.parse(startTime);
					assignRoutePO.setCombinedFacility(assignRoutePO.getCombinedFacility());
					assignRoutePO.setTripAssignDate(new Date());
					List<EFmFmAssignRoutePO> assignRoute = iCabRequestBO
							.getParticularDriverAssignTripDetail(assignRoutePO);
					// end auto trip on start service
					assignRoute.get(0).setTripStartTime(rescheduleDate);
					assignRoute.get(0).setOdometerStartKm(assignRoutePO.getOdometerStartKm());
					assignRoute.get(0).setTripStatus("Started");
                    assignRoute.get(0).setTripConfirmationFromDriver("Delivered");
					iCabRequestBO.update(assignRoute.get(0));
				} catch (Exception e) {

					log.info("Inside error blog" + e);
				}
			}
		});
		thread1.start();
		requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Employee status is basically..Employee trip status it a drop,pickup or no
	 * show
	 * 
	 */
	@POST
	@Path("/employeestatus")
	public Response employeeStatusFromDevice(EFmFmEmployeeTripDetailPO employeeTripDetailPO) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		final IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");	
		 Map<String, Object> responce = new HashMap<String, Object>();		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTripDetailPO.getUserId()))){
//
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}
//		 
//		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeTripDetailPO.getUserId());
//		   if (!(userDetailToken.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
//		     userDetailToken.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetailToken.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
		 
		 log.info("serviceStart -UserId :" + employeeTripDetailPO.getUserId());
		final List<EFmFmEmployeeTripDetailPO> empDetails = iCabRequestBO.getParticularTriprEmployeeBoardedStatus(
				employeeTripDetailPO.geteFmFmEmployeeTravelRequest().getRequestId(),
				employeeTripDetailPO.getEfmFmAssignRoute().getAssignRouteId());
		DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");

		Map<String, Object> requests = new HashMap<String, Object>();
		/// Cab Has Left Message........
		final PushNotificationService pushNotification = new PushNotificationService();
		String startTime = employeeTripDetailPO.getTime();
		Date pickupOrNoShowTime = dateTimeFormate.parse(startTime);
		if (employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("NO")
				&& empDetails.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")
				&& !(empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestType().equalsIgnoreCase("guest"))) {
			Thread thread1 = new Thread(new Runnable() {
				@Override
				public synchronized void run() {
					try {
						String text = "";
						MessagingService messaging = new MessagingService();
						text = "Sorry you missed us.\nYour ride "
								+ empDetails.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
										.getEfmFmVehicleMaster().getVehicleNumber()
								+ " has left your " + empDetails.get(0).geteFmFmEmployeeTravelRequest().getTripType()
								+ " point.\nFor feedback write to us @"
								+ empDetails.get(0).getEfmFmAssignRoute().geteFmFmClientBranchPO().getFeedBackEmailId();

						messaging.cabHasLeftMessageForSch(
								new String(Base64.getDecoder()
										.decode(empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
												.getMobileNumber()),
										"utf-8"),
								text, empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
						log.debug("Time taken by cab left message from gate way and button click for trip Id: "
								+ empDetails.get(0).getEmpTripId());
						try {
							if (empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceType()
									.contains("Android")) {
								pushNotification.notification(empDetails.get(0).geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getDeviceToken(), text);
							} else {
								pushNotification.iosPushNotification(empDetails.get(0).geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getDeviceToken(), text);
							}
							// }
						} catch (Exception e) {
							log.info("PushStatus employeestatus" + e);
						}
						Thread.currentThread().stop();

					} catch (Exception e) {
						try {
							log.info("Error Cab has left Message Triggered  for First employee from button click "
									+ new String(Base64.getDecoder().decode(empDetails.get(0)
											.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber()),
											"utf-8"));
						} catch (Exception e1) {
							log.info("error" + e);
						}
						Thread.currentThread().stop();
					}
				}
			});
			thread1.start();
			empDetails.get(0).setCabstartFromDestination(pickupOrNoShowTime.getTime());
		} else {
			empDetails.get(0).setPickedUpDateAndTime(pickupOrNoShowTime.getTime());
		}

		empDetails.get(0).setBoardedFlg(employeeTripDetailPO.getBoardedFlg());
		empDetails.get(0).setEmployeeStatus("completed");
		// Auto Drop Cancelattion basically converting in 23:50(Shift Time) code
		// iCabRequestBO.update(empDetails.get(0));
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
		// long dropRequestDate =
		// getDisableTime(empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
		// .geteFmFmClientBranchPO().getShiftTimeDiffPickToDrop(), 0,
		// dateTimeFormate.parse(reqDate));
		// Date requestDateForDrop = new Date(dropRequestDate);
		// String dropShiftTime = requestDateForDrop.getHours() + ":" +
		// requestDateForDrop.getMinutes();
		// java.sql.Time dropShift = new
		// java.sql.Time(shifTimeFormate.parse(dropShiftTime).getTime());
		// String dropRequestAndStart = dateFormat.format(requestDateForDrop);
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
		final List<EFmFmUserMasterPO> hostDetails = userMasterBO.getEmployeeUserDetailFromMobileNumber(
				empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().geteFmFmClientBranchPO()
						.getBranchId(),
				empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getHostMobileNumber());
		if (empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestType().equalsIgnoreCase("guest")
				&& (employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("B")
						|| employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("D"))) {
			empDetails.get(0).setEmployeeOnboardStatus("OnBoard");

			Thread thread1 = new Thread(new Runnable() {
				@Override
				public synchronized void run() {
					try {
						String hostText = "";
						MessagingService messagingService = new MessagingService();
						if (empDetails.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
							hostText = "Dear Host,Your guest "
									+ new String(
											Base64.getDecoder()
													.decode(empDetails.get(0).geteFmFmEmployeeTravelRequest()
															.getEfmFmUserMaster().getFirstName()),
											"utf-8")
									+ " is Dropped.";
						} else {
							hostText = "Dear Host,Your guest "
									+ new String(
											Base64.getDecoder()
													.decode(empDetails.get(0).geteFmFmEmployeeTravelRequest()
															.getEfmFmUserMaster().getFirstName()),
											"utf-8")
									+ " is picked up.";
						}
						StringTokenizer token = new StringTokenizer(
								new String(Base64.getDecoder().decode(empDetails.get(0).geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getHostMobileNumber()), "utf-8"),
								",");
						while (token.hasMoreElements()) {
							messagingService.sendTripAsMessage(token.nextElement().toString(), hostText,
									empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
						}
						try {
							if (!(hostDetails.isEmpty())) {
								if (hostDetails.get(0).getDeviceType().contains("Android")) {
									pushNotification.notification(hostDetails.get(0).getDeviceToken(), hostText);
								} else {
									pushNotification.iosPushNotification(hostDetails.get(0).getDeviceToken(), hostText);
								}
							}
						} catch (Exception e) {
							log.info("PushStatus employeestatus" + e);
						}
						Thread.currentThread().stop();
					} catch (Exception e) {
						log.info("Inside error blog" + e);
					}
				}
			});
			thread1.start();
		}
		if (employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("NO")) {
			empDetails.get(0).setCabstartFromDestination(new Date().getTime());
		}
		iCabRequestBO.update(empDetails.get(0));
		requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + employeeTripDetailPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}
	
	/*
	 * Employee status is basically..employeeBoardingFromStatus  it a drop,pickup or no
	 * show
	 * 
	 */
	@POST
	@Path("/employeeBoardingFromStatus")
	public Response employeeStatusFromEmployeeDevice(EFmFmEmployeeTripDetailPO employeeTripDetailPO) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + employeeTripDetailPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
	 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),employeeTripDetailPO.getUserId()))){
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTripDetailPO.getUserId());
	 			if (!(userDetail.isEmpty())) {
	 				String jwtToken = "";
	 				try {
	 					JwtTokenGenerator token = new JwtTokenGenerator();
	 					jwtToken = token.generateToken();
	 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
	 					userDetail.get(0).setMobTokenGenerationTime(new Date());
	 					userMasterBO.update(userDetail.get(0));
	 				} catch (Exception e) {
	 					log.info("error" + e);
	 				}
	                responce.put("token", jwtToken);
	 			}
	 		
	 		}catch(Exception e){
	 				log.info("authentication error"+e);
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
		 
		 final List<EFmFmEmployeeTripDetailPO> empDetails = iCabRequestBO.getParticularTriprEmployeeBoardedStatus(
				employeeTripDetailPO.geteFmFmEmployeeTravelRequest().getRequestId(),
				employeeTripDetailPO.getEfmFmAssignRoute().getAssignRouteId());
		DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");

		if(empDetails.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")){
		try{
			CalculateDistance distance=new CalculateDistance();
			String lineLattitude=employeeTripDetailPO.getGeoCodes().split(",")[0];
			String lineLongitude=employeeTripDetailPO.getGeoCodes().split(",")[1];	
			double routePointDistance=distance.distance(Double.parseDouble(lineLattitude), Double.parseDouble(lineLongitude), Double.parseDouble(empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude().split(",")[0]), Double.parseDouble(empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude().split(",")[1]), 'm');			
	        if(routePointDistance > empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().geteFmFmClientBranchPO().getEmployeeAddressgeoFenceArea()){
	        	responce.put("status", "wrong");
	    		 log.info("serviceEnd -UserId :" + employeeTripDetailPO.getUserId());
	    		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	        }
			}catch(Exception e){
				log.info("Employee status update "+e);
			}
		}
		
		/// Cab Has Left Message........
		String startTime = employeeTripDetailPO.getTime();
		Date pickupOrNoShowTime = dateTimeFormate.parse(startTime);
		if (employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("NO")
				&& empDetails.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")
				&& !(empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestType().equalsIgnoreCase("guest"))) {
  			  empDetails.get(0).setCabstartFromDestination(pickupOrNoShowTime.getTime());
		} else {
			empDetails.get(0).setPickedUpDateAndTime(pickupOrNoShowTime.getTime());
		}

		empDetails.get(0).setBoardedFlg(employeeTripDetailPO.getBoardedFlg());
		empDetails.get(0).setEmployeeStatus("completed");
		if (empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestType().equalsIgnoreCase("guest")
				&& (employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("B")
						|| employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("D"))) {
			empDetails.get(0).setEmployeeOnboardStatus("OnBoard");
		}
		if (empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestType().equalsIgnoreCase("guest")
				&& (employeeTripDetailPO.getBoardedFlg().equalsIgnoreCase("NO"))) {
			empDetails.get(0).setCabstartFromDestination(new Date().getTime());
		}
		iCabRequestBO.update(empDetails.get(0));
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + employeeTripDetailPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	

	
	
	/*
	 * Employee coming status update from employee device.
	 *
	 * 
	 */
	@POST
	@Path("/employeecomingstatus")
	public Response employeeComingStatus(EFmFmEmployeeTripDetailPO employeeTripDetailPO) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		 log.info("serviceStart -UserId :" + employeeTripDetailPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));



	        try{
	 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),employeeTripDetailPO.getUserId()))){
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTripDetailPO.getUserId());
	 			if (!(userDetail.isEmpty())) {
	 				String jwtToken = "";
	 				try {
	 					JwtTokenGenerator token = new JwtTokenGenerator();
	 					jwtToken = token.generateToken();
	 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
	 					userDetail.get(0).setMobTokenGenerationTime(new Date());
	 					userMasterBO.update(userDetail.get(0));
	 				} catch (Exception e) {
	 					log.info("error" + e);
	 				}
	                responce.put("token", jwtToken);
	 			}
	 		
	 		}catch(Exception e){
	 				log.info("authentication error"+e);
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
	 		 
	 		 
		 
		final List<EFmFmEmployeeTripDetailPO> empDetails = iCabRequestBO.getParticularTripEmployeeRequestDetails(
				employeeTripDetailPO.getEmpTripId(), employeeTripDetailPO.getRequestId(),
				employeeTripDetailPO.getAssignRouteId());
		log.info("Employee status update "+empDetails.size());
		responce.put("status", "success");
		if (!(empDetails.isEmpty())) {
			EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
			assignRoutePO.setAssignRouteId(employeeTripDetailPO.getAssignRouteId());
			EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
			eFmFmClientBranchPO.setBranchId(employeeTripDetailPO.getBranchId());
			assignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
			List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
			if (employeeTripDetailPO.getComingStatus().equalsIgnoreCase("Confirmed")) {
				empDetails.get(0).setComingStatus(employeeTripDetailPO.getComingStatus());
			} else {
				empDetails.get(0).setComingStatus(employeeTripDetailPO.getComingStatus());
	  			  empDetails.get(0).setCabstartFromDestination(new Date().getTime());
				empDetails.get(0).setBoardedFlg("NO");
				empDetails.get(0).setEmployeeStatus("completed");
			}
			iCabRequestBO.update(empDetails.get(0));
			if (!(assignRoute.isEmpty())) {
				assignRoute.get(0).setTripUpdateTime(new Date());
				assignRouteBO.update(assignRoute.get(0));
			}
		}
		 log.info("serviceEnd -UserId :" + employeeTripDetailPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/liveemployeetrip")
	public Response tripStatusForEmployee(EFmFmEmployeeTripDetailPO tripdetail) {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		 log.info("serviceStart -UserId :" + tripdetail.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
	 			if(!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),tripdetail.getUserId()))){	 				
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			} 		
	 			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(tripdetail.getUserId());
	 			if (!(userDetail.isEmpty())) {
	 				String jwtToken = "";
	 				try {
	 					JwtTokenGenerator token = new JwtTokenGenerator();
	 					jwtToken = token.generateToken();
	 					userDetail.get(0).setMobAuthorizationToken(jwtToken);
	 					userDetail.get(0).setMobTokenGenerationTime(new Date());
	 					userMasterBO.update(userDetail.get(0));
	 				} catch (Exception e) {
	 					log.info("error" + e);
	 				}
	                responce.put("token", jwtToken);
	 			}
	 		
	 		}catch(Exception e){
	 				log.info("authentication error"+e);
	 				responce.put("status", "invalidRequest");
	 				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	 			}
		 
		 List<Map<String, Object>> emloyeeLiveTrips = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> emloyeeTrips = new ArrayList<Map<String, Object>>();
		Map<String, Object> liveResponce = new HashMap<String, Object>();	
		List<EFmFmEmployeeTripDetailPO> empDetails = iCabRequestBO.getAllocatedEmployeeDetailYetToBoard(tripdetail.getUserId(),
				tripdetail.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchId(), new Date());
			log.info("empDetails.size ! completed and boarded=N"+empDetails.size());

		List<EFmFmEmployeeTripDetailPO> empLiveTripDetails = iCabRequestBO.getEmployeeLiveTripDetailFromUserIdBeforeOnBoard(
				tripdetail.getUserId(), tripdetail.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchId());
		String status = "success";
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		CabRequestService cabRequestService= new CabRequestService();
		if (!(empLiveTripDetails.isEmpty())) {
			liveResponce.put("tripStatus", empLiveTripDetails.get(0).getEfmFmAssignRoute().getTripStatus());
			liveResponce.put("tripType", empLiveTripDetails.get(0).getEfmFmAssignRoute().getTripType());
			liveResponce.put("driverName", empLiveTripDetails.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
					.getEfmFmDriverMaster().getFirstName());
			liveResponce.put("driverNumber", empLiveTripDetails.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
					.getEfmFmDriverMaster().getMobileNumber());
			liveResponce.put("VehicleNumber", empLiveTripDetails.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
					.getEfmFmVehicleMaster().getVehicleNumber());
			liveResponce.put("shiftTime", empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest().getShiftTime());
			liveResponce.put("assignRouteId", empLiveTripDetails.get(0).getEfmFmAssignRoute().getAssignRouteId());
			liveResponce.put("empTripId", empLiveTripDetails.get(0).getEmpTripId());
			liveResponce.put("requestId", empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestId());
			liveResponce.put("empComingStatus", empLiveTripDetails.get(0).getComingStatus());
			liveResponce.put("OnBoardStatus", empLiveTripDetails.get(0).getEmployeeOnboardStatus());
			liveResponce.put("facilityName", empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest().geteFmFmClientBranchPO().getBranchName());
			liveResponce.put("facilityId", empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest().geteFmFmClientBranchPO().getBranchId());
			if(empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest().getLocationFlg()==null){
				liveResponce.put("locationFlg","N");
			}else{
				liveResponce.put("locationFlg",empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest().getLocationFlg());
			}	
			try {
				if (empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest().getLocationFlg().equalsIgnoreCase("M")) {
					StringBuffer multiLocationWaypoints = new StringBuffer();						
					Map<String,Object> waypointsList=cabRequestService.multilocationlistOfWayPointsAfterAllocated(empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestId(),tripdetail.getUserId(),tripdetail.getCombinedFacility());				
					if(!(waypointsList.isEmpty())){									
						for( Map.Entry<String,Object> listofValues:waypointsList.entrySet()){
							if(listofValues.getKey().equalsIgnoreCase("wayPointsList")){
								multiLocationWaypoints.append(listofValues.getValue());
							}else{
								liveResponce.put("waypointsList",listofValues.getValue());
							}
							
						}
						if(multiLocationWaypoints.length()>0){
							liveResponce.put("multiWayPoints",multiLocationWaypoints);							
						}
					}				
				}
			} catch (Exception e) {
				log.debug("multilocation Value not updated");
			}			
			try {
			if(empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("SBOBNG")){
				liveResponce.put("boardingButtonStatus", "Y");
			}else{
				liveResponce.put("boardingButtonStatus", "N");
			}
				String profilePicPath = "";
				if (empLiveTripDetails.get(0).getEfmFmAssignRoute().geteFmFmClientBranchPO()
						.getDriverDeviceDriverProfilePicture().equalsIgnoreCase("Yes")) {
					profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
							null);
				} else {
					profilePicPath = ContextLoader.getContext().getMessage("default.profilePic", null, "profilePic",
							null);
				}
				liveResponce
						.put("tripDriverPic",
								profilePicPath
										+ empLiveTripDetails.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
												.getEfmFmDriverMaster().getProfilePicPath()
												.substring(empLiveTripDetails.get(0).getEfmFmAssignRoute()
														.getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
														.getProfilePicPath().indexOf("upload") - 1));
			} catch (Exception e) {
				String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
						"profilePic", null);
				liveResponce.put("tripDriverPic", defaultProfilePic);
			}
			liveResponce.put("empLatLong", empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest()
					.getEfmFmUserMaster().getLatitudeLongitude());
			liveResponce.put("Eta", empLiveTripDetails.get(0).getCurrenETA());
			if (empLiveTripDetails.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
				liveResponce.put("pickupTime",
						timeFormat.format(empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime()));
			}
			if (empLiveTripDetails.get(0).getEfmFmAssignRoute().getTripStatus().equalsIgnoreCase("Started")) {
				EFmFmAssignRoutePO assignRoute = new EFmFmAssignRoutePO();
				assignRoute.setAssignRouteId(empLiveTripDetails.get(0).getEfmFmAssignRoute().getAssignRouteId());
				List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
						.getRouteLastEtaAndDistanceFromAssignRouteId(
								empLiveTripDetails.get(0).getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchId(),
								empLiveTripDetails.get(0).getEfmFmAssignRoute().getAssignRouteId());
				log.info("actualRouteTravelled size"
						+ actualRouteTravelled.size());
				if (!(actualRouteTravelled.isEmpty())) {
					liveResponce.put("driverLatLong",
							actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
					liveResponce.put("cabLocation",
							actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation());
				} else {
					liveResponce.put("driverLatLong", empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest()
							.getEfmFmUserMaster().geteFmFmClientBranchPO().getLatitudeLongitude());
					liveResponce.put("cabLocation", empLiveTripDetails.get(0).geteFmFmEmployeeTravelRequest()
							.getEfmFmUserMaster().geteFmFmClientBranchPO().getAddress());
				}
			}
			status = "success";
			if (!(empLiveTripDetails.get(0).getComingStatus().equalsIgnoreCase("Reject"))) {
				emloyeeLiveTrips.add(liveResponce);
			}
		}
		if (!(empDetails.isEmpty())) {
			EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
			EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
			eFmFmClientBranch
					.setBranchId(empDetails.get(0).getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchId());
			assignRoutePO.setAssignRouteId(empDetails.get(0).getEfmFmAssignRoute().getAssignRouteId());
			assignRoutePO.seteFmFmClientBranchPO(eFmFmClientBranch);
			List<EFmFmAssignRoutePO> routeDetail = assignRouteBO.closeParticularTrips(assignRoutePO);
			EFmFmEmployeeRequestMasterPO requestMasterPO = new EFmFmEmployeeRequestMasterPO();
			requestMasterPO.setTripType(routeDetail.get(0).getTripType());
			requestMasterPO.setRequestDate(new Date());
			if (!(routeDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber()
					.contains("DUMMY"))) {
				liveResponce.put("tripStatus", routeDetail.get(0).getTripStatus());
				liveResponce.put("tripType", routeDetail.get(0).getTripType());
				liveResponce.put("driverName", empDetails.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
						.getEfmFmDriverMaster().getFirstName());
				liveResponce.put("driverNumber", empDetails.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
						.getEfmFmDriverMaster().getMobileNumber());
				liveResponce.put("VehicleNumber", empDetails.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
						.getEfmFmVehicleMaster().getVehicleNumber());
				liveResponce.put("shiftTime", empDetails.get(0).geteFmFmEmployeeTravelRequest().getShiftTime());
				liveResponce.put("assignRouteId", routeDetail.get(0).getAssignRouteId());
				liveResponce.put("empTripId", empDetails.get(0).getEmpTripId());
				liveResponce.put("requestId", empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestId());
				liveResponce.put("empComingStatus", empDetails.get(0).getComingStatus());
				liveResponce.put("OnBoardStatus", empDetails.get(0).getEmployeeOnboardStatus());

				if(empDetails.get(0).geteFmFmEmployeeTravelRequest().getLocationFlg() ==null){
					liveResponce.put("locationFlg","N");
				}else{
					liveResponce.put("locationFlg",empDetails.get(0).geteFmFmEmployeeTravelRequest().getLocationFlg());
				}
				
				
				try {
					if(!(empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("SBOCHN"))){

					liveResponce.put("boardingButtonStatus", "Y");
				}else{
					liveResponce.put("boardingButtonStatus", "N");
				}
				
				try {
					if (empDetails.get(0).geteFmFmEmployeeTravelRequest().getLocationFlg().equalsIgnoreCase("M")) {
						StringBuffer multiLocationWaypoints = new StringBuffer();						
						Map<String,Object> waypointsList=cabRequestService.multilocationlistOfWayPointsAfterAllocated(empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestId(),tripdetail.getUserId(),tripdetail.getCombinedFacility());				
						if(!(waypointsList.isEmpty())){									
							for( Map.Entry<String,Object> listofValues:waypointsList.entrySet()){
								if(listofValues.getKey().equalsIgnoreCase("wayPointsList")){
									multiLocationWaypoints.append(listofValues.getValue());
								}else{
									liveResponce.put("waypointsList",listofValues.getValue());
								}
								
							}
							if(multiLocationWaypoints.length()>0){
								liveResponce.put("multiWayPoints",multiLocationWaypoints);
							}
							
						}				
					}
				} catch (Exception e) {
					log.debug("multilocation Value not updated");
				}

				
				
					String profilePicPath = "";
					if (routeDetail.get(0).geteFmFmClientBranchPO().getDriverDeviceDriverProfilePicture()
							.equalsIgnoreCase("Yes")) {
						profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
								null);
					} else {
						profilePicPath = ContextLoader.getContext().getMessage("default.profilePic", null, "profilePic",
								null);
					}
					liveResponce.put("tripDriverPic",
							profilePicPath + routeDetail.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
									.getProfilePicPath().substring(routeDetail.get(0).getEfmFmVehicleCheckIn()
											.getEfmFmDriverMaster().getProfilePicPath().indexOf("upload") - 1));
				} catch (Exception e) {
					String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
							"profilePic", null);
					liveResponce.put("tripDriverPic", defaultProfilePic);
				}
				liveResponce.put("empLatLong",
						empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude());
				liveResponce.put("Eta", empDetails.get(0).getCurrenETA());
				if (routeDetail.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
					liveResponce.put("pickupTime",
							timeFormat.format(empDetails.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime()));
				}
				if (routeDetail.get(0).getTripStatus().equalsIgnoreCase("Started")) {
					EFmFmAssignRoutePO assignRoute = new EFmFmAssignRoutePO();
					assignRoute.setAssignRouteId(empDetails.get(0).getEfmFmAssignRoute().getAssignRouteId());
					List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
							.getRouteLastEtaAndDistanceFromAssignRouteId(
									tripdetail.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchId(),
									empLiveTripDetails.get(0).getEfmFmAssignRoute().getAssignRouteId());
					log.info("actualRouteTravelled size"
							+ actualRouteTravelled.size());
					if (!(actualRouteTravelled.isEmpty())) {
						liveResponce.put("driverLatLong",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());
						liveResponce.put("cabLocation",
								actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveCurrentCabLocation());
					} else {
						liveResponce.put("driverLatLong", empDetails.get(0).geteFmFmEmployeeTravelRequest()
								.getEfmFmUserMaster().geteFmFmClientBranchPO().getLatitudeLongitude());
						liveResponce.put("cabLocation", empDetails.get(0).geteFmFmEmployeeTravelRequest()
								.getEfmFmUserMaster().geteFmFmClientBranchPO().getAddress());
					}
				}
				if (!(empDetails.get(0).getComingStatus().equalsIgnoreCase("Reject"))) {
					emloyeeLiveTrips.add(liveResponce);
				}
			}

			else {
				status = "allocated-" + routeDetail.get(0).getTripType().toLowerCase();

			}
		}

		if (!(emloyeeLiveTrips.isEmpty())) {
			emloyeeTrips.add(emloyeeLiveTrips.get(0));
			responce.put("data", emloyeeTrips);
		} else {
			responce.put("data", emloyeeLiveTrips);
		}
		responce.put("status", status);
		 log.info("serviceEnd -UserId :" + tripdetail.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	// Cab reached msg on reached click inside driver app
	@POST
	@Path("/cabreached")
	public Response cabReachedAtEmployeeDestination(final EFmFmEmployeeTripDetailPO employeeTripDetailPO) {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTripDetailPO.getUserId()))){
//
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}
//		 
//		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeTripDetailPO.getUserId());
//		   if (!(userDetailToken.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
//		     userDetailToken.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetailToken.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
		 
		 log.info("serviceStart -UserId :" + employeeTripDetailPO.getUserId());
		final List<EFmFmEmployeeTripDetailPO> empDetails = iCabRequestBO.getParticularTriprEmployeeBoardedStatus(
				employeeTripDetailPO.geteFmFmEmployeeTravelRequest().getRequestId(),
				employeeTripDetailPO.getEfmFmAssignRoute().getAssignRouteId());
		
		log.info("Reached Button click");

		final PushNotificationService pushNotification = new PushNotificationService();
		try {
			List<EFmFmUserMasterPO> loggedInUserDetail = employeeDetailBO
					.getParticularEmpDetailsFromUserIdWithOutStatus(
							empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getUserId(),
							employeeTripDetailPO.getCombinedFacility());
			if (loggedInUserDetail.get(0).getLocationStatus().equalsIgnoreCase("N")) {
				loggedInUserDetail.get(0).setDeviceLatitudeLongitude(employeeTripDetailPO.getGeoCodes());
				loggedInUserDetail.get(0).setLocationStatus("Y");
				employeeDetailBO.update(loggedInUserDetail.get(0));
			}
			if (empDetails.get(0).getReachedFlg().equalsIgnoreCase("N")) {
				DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				String startTime = employeeTripDetailPO.getTime();
				Date reachedDate = dateTimeFormate.parse(startTime);
				double totalDistance = 0;

				List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
						.getRouteLastEtaAndDistanceFromAssignRouteId(
								empDetails.get(0).getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchId(),
								empDetails.get(0).getEfmFmAssignRoute().getAssignRouteId());
				if (!(actualRouteTravelled.isEmpty())) {
					totalDistance = Geocode.distance(
							new Geocode(empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
									.getLatitudeLongitude()),
							new Geocode(actualRouteTravelled.get(actualRouteTravelled.size() - 1)
									.geteFmFmClientBranchPO().getLatitudeLongitude()));
				}
				log.info("Reached Button distance" + totalDistance);
				// if (totalDistance <=
				// empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().geteFmFmClientBranchPO().getEmployeeAddressgeoFenceArea()){
				Thread thread1 = new Thread(new Runnable() {
					@Override
					public synchronized void run() {
						MessagingService messagingService = new MessagingService();
						String text = "";
						try {
							long l1 = System.currentTimeMillis();
							if (empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestType()
									.equalsIgnoreCase("guest")) {
								text = "Dear guest your cab\n"
										+ empDetails.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
												.getEfmFmVehicleMaster().getVehicleNumber()
										+ "\n has arrived at your pickup point.\nFor feedback write to us @"
										+ empDetails.get(0).getEfmFmAssignRoute().geteFmFmClientBranchPO()
												.getFeedBackEmailId();
							} else {
								text = "Your ride "
										+ empDetails.get(0).getEfmFmAssignRoute().getEfmFmVehicleCheckIn()
												.getEfmFmVehicleMaster().getVehicleNumber()
										+ " has arrived at your pickup point.\nFor feedback write to us @"
										+ empDetails.get(0).getEfmFmAssignRoute().geteFmFmClientBranchPO()
												.getFeedBackEmailId();
							}
							try {
								if (empDetails.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
										.getDeviceType().contains("Android")) {
									pushNotification.notification(empDetails.get(0).geteFmFmEmployeeTravelRequest()
											.getEfmFmUserMaster().getDeviceToken(), text);
								} else {
									pushNotification.iosPushNotification(empDetails.get(0)
											.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getDeviceToken(),
											text);
								}

							} catch (Exception e) {
								log.info("Cab Reached Msg" + e);
							}
							messagingService.cabReachedMessage(
									new String(Base64.getDecoder()
											.decode(String.valueOf(empDetails.get(0).geteFmFmEmployeeTravelRequest()
													.getEfmFmUserMaster().getMobileNumber())),
											"utf-8"),
									text, empDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestType());
							long l2 = System.currentTimeMillis();
							log.debug("Time taken by cab reached message from gate way from button click for trip Id: "
									+ empDetails.get(0).getEmpTripId() + " Time " + (l2 - l1) + "ms");

						} catch (Exception e) {
							log.info("error" + e);
						}
					}
				});
				thread1.start();
				empDetails.get(0).setReachedMessageDeliveryDate(reachedDate);
				// }
				empDetails.get(0).setReachedFlg("Y");
				empDetails.get(0).setCabRecheddestinationTime(reachedDate.getTime());
				iCabRequestBO.update(empDetails.get(0));
			}
		} catch (Exception e) {
			log.info("Error in reached function" + e);
		}
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + employeeTripDetailPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/tripcomplete")
	public Response completeTrip(final EFmFmAssignRoutePO assignRoutePO) {
		final ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 log.info("tripcomplete_serviceStart -UserId :" + assignRoutePO.getUserId());
		 log.info("tripcomplete_serviceStart -UserId :" + assignRoutePO.getAssignRouteId());
 		 
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
//			 		log.info("inside if ");
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}
//		 
//		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
//		   if (!(userDetailToken.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
//		     userDetailToken.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetailToken.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
		 
		Map<String, Object> requests = new HashMap<String, Object>();
		assignRoutePO.setTripAssignDate(new Date());
		final List<EFmFmAssignRoutePO> assignRoute = iCabRequestBO.getParticularDriverAssignTripDetail(assignRoutePO);
		//log.info("EndKM" + assignRoutePO.getOdometerEndKm());
		//log.info("startKm" + assignRoute.get(0).getOdometerStartKm());
		if(null !=assignRoute.get(0).getOdometerStartKm()){
			if (Double.parseDouble(assignRoute.get(0).getOdometerStartKm()) > Double
					.parseDouble(assignRoutePO.getOdometerEndKm())) {
				requests.put("status", "fail");
				 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}
		}
		
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				try {
					IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
					IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext()
							.getBean("IAssignRouteBO");
					DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					String startTime = assignRoutePO.getTime();
					Date tripCompleteTime = dateTimeFormate.parse(startTime);
					IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
							.getBean("IVehicleCheckInBO");
					EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
					eFmFmVehicleCheckInPO.setCheckInTime(new Date());
					eFmFmVehicleCheckInPO.setCheckInId(assignRoute.get(0).getEfmFmVehicleCheckIn().getCheckInId());
					List<EFmFmVehicleCheckInPO> vehicleCheckIn = iVehicleCheckInBO
							.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
					EFmFmVehicleMasterPO vehicleMasterDetail = iVehicleCheckInBO
							.getParticularVehicleDetail(vehicleCheckIn.get(0).getEfmFmVehicleMaster().getVehicleId());
					List<EFmFmEmployeeTripDetailPO> allRequests = null;
					if (assignRoute.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
						allRequests = iCabRequestBO.getParticularTripAllEmployees(assignRoutePO.getAssignRouteId());
					} else {
						allRequests = iCabRequestBO.getDropTripAllSortedEmployees(assignRoutePO.getAssignRouteId());
					}

					List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO.getTripAllocatedRoute(
							vehicleCheckIn.get(0).getCheckInId(),
							assignRoute.get(0).geteFmFmClientBranchPO().getBranchId());
					if (oldCheckRoutesCheck.size() == 1) {
						vehicleCheckIn.get(0).setStatus("Y");
						vehicleMasterDetail.setStatus("A");
					}

					// vehicleMasterDetail.setAvailableSeat(vehicleMasterDetail.getCapacity()-1);
					
					//Get vehicle Last Location
					assignRoute.get(0).setTripCompleteTime(tripCompleteTime);
					assignRoute.get(0).setTripStatus("completed");
					List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO
							.getRouteLastEtaAndDistanceFromAssignRouteId(
									assignRoutePO.geteFmFmClientBranchPO().getBranchId(),
									assignRoute.get(0).getAssignRouteId());
					//Get vehicle First  Location
					List<EFmFmLiveRoutTravelledPO> routeFirstLocation = assignRouteBO
							.getRouteLastEtaAndDistanceFromAssignRouteIdForCabLastLocation(
									assignRoutePO.geteFmFmClientBranchPO().getBranchId(),
									assignRoute.get(0).getAssignRouteId());
					
					vehicleCheckIn.get(0)
							.setTotalTravelTime((vehicleCheckIn.get(0).getTotalTravelTime()
									- assignRoute.get(0).getPlannedTime())
									+ ((tripCompleteTime.getTime() - assignRoute.get(0).getTripStartTime().getTime())
											/ 1000));
					double plannedDis = vehicleMasterDetail.getMonthlyPendingFixedDistance()
							+ assignRoute.get(0).getPlannedDistance();
					iVehicleCheckInBO.update(vehicleMasterDetail);
					double totalTravelledDis = 0;
					if (!(actualRouteTravelled.isEmpty())) {
						StringBuffer empWayPoints = new StringBuffer();
						CalculateDistance calculateDistance = new CalculateDistance();
						if (assignRoute.get(0).getRouteGenerationType().contains("nodalAdhoc")) {
							empWayPoints.append(assignRoute.get(0).getNodalPoints());
						} else {
							if (!(allRequests.isEmpty())) {
								for (EFmFmEmployeeTripDetailPO employeeTripDetail : allRequests) {
									String wayPointsAdhocRequest = "";
									if (assignRoute.get(0).getRouteGenerationType().contains("nodal")) {
										if(!(empWayPoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()))){
											empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
													.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()
													+ "|");
										}
									} else if (assignRoute.get(0).getRouteGenerationType()
											.equalsIgnoreCase("AdhocRequest")) {
										empWayPoints.append(wayPointsAdhocRequest + employeeTripDetail
												.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getEndDestinationAddressLattitudeLongitude() + "|");
									} else {
										empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
												.getEfmFmUserMaster().getLatitudeLongitude() + "|");
									}
								}

							}
						}
						
						try {
							String plannedDistance = calculateDistance.getPlannedDistanceByMapApi(
									routeFirstLocation.get(0).getLivelatitudeLongitude(), actualRouteTravelled
											.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude(),
									empWayPoints.toString());
							if(assignRoute.get(0).getLocationFlg().equalsIgnoreCase("M")){												
								plannedDistance=multipleDestPlannedDistance
										(assignRoute.get(0).getAssignRouteId(), 
												routeFirstLocation.get(0).getLivelatitudeLongitude(), 
												actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());													
							}							
							totalTravelledDis = assignRoute.get(0).getTravelledDistance()
									+ (assignRoute.get(0).geteFmFmClientBranchPO().getAddingGeoFenceDistanceIntrip());
							
							if(!(assignRoute.get(0).geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP"))){
							assignRoute.get(0).setPlannedDistance(Double.parseDouble(plannedDistance)
									+ (assignRoute.get(0).geteFmFmClientBranchPO().getAddingGeoFenceDistanceIntrip()));		
							}
							
							

						} catch (Exception e) {
							log.info("trip complete planned distance by button click" + e);
						}
					} else {
						if(!(assignRoute.get(0).geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP"))){
						assignRoute.get(0).setPlannedDistance(assignRoute.get(0).getPlannedDistance()
								+ (assignRoute.get(0).geteFmFmClientBranchPO().getAddingGeoFenceDistanceIntrip()));
						}
						
						totalTravelledDis = assignRoute.get(0).getTravelledDistance()
								+ (assignRoute.get(0).geteFmFmClientBranchPO().getAddingGeoFenceDistanceIntrip());
					}
					// end auto trip complete by geo fence and updation of
					// planned distance
					assignRoute.get(0).setOdometerEndKm(assignRoutePO.getOdometerEndKm());
					assignRoute.get(0).setTravelledDistance(totalTravelledDis);					
					if(!(assignRoute.get(0).geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("GNPTJP"))){
					assignRoute.get(0).setPlannedTravelledDistance(totalTravelledDis);
					}
					
					assignRoute.get(0).setPlannedTime(
							(tripCompleteTime.getTime() - assignRoute.get(0).getTripStartTime().getTime()) / 1000);
					iCabRequestBO.update(assignRoute.get(0));
					if (oldCheckRoutesCheck.size() == 1) {
						EFmFmDriverMasterPO particularDriverDetails = approvalBO
								.getParticularDriverDetail(vehicleCheckIn.get(0).getEfmFmDriverMaster().getDriverId());
						particularDriverDetails.setStatus("A");
						approvalBO.update(particularDriverDetails);
						List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
								vehicleCheckIn.get(0).geteFmFmDeviceMaster().getDeviceId(),
								new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
						deviceDetails.get(0).setStatus("Y");
						iVehicleCheckInBO.update(deviceDetails.get(0));
					}
					try {
						vehicleCheckIn.get(0).setTotalTravelDistance((vehicleCheckIn.get(0).getTotalTravelDistance()
								- assignRoute.get(0).getPlannedDistance()) + totalTravelledDis);
						// vehicleCheckIn.get(0).setNumberOfTrips(vehicleCheckIn.get(0).getNumberOfTrips()+1);
					} catch (Exception e) {
						log.info("Error trip complete button updating the travelled and number of trips" + e);
					}
					iVehicleCheckInBO.update(vehicleCheckIn.get(0));
					if (assignRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
						try {
							int a = assignRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId();
							List<EFmFmEscortCheckInPO> escortDetails = iVehicleCheckInBO
									.getParticulaEscortDetailFromEscortId(assignRoutePO.getCombinedFacility(),
											assignRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId());
							escortDetails.get(0).setStatus("Y");
							iVehicleCheckInBO.update(escortDetails.get(0));
						} catch (Exception e) {
							log.info("escort complete button click exception " + e);
						}
					}
					log.info("allRequests" + allRequests.size());
					vehicleMasterDetail.setMonthlyPendingFixedDistance(plannedDis - totalTravelledDis);
					iVehicleCheckInBO.update(vehicleMasterDetail);

					if (!(allRequests.isEmpty())) {
						for (EFmFmEmployeeTripDetailPO requestDetail : allRequests) {
							List<EFmFmEmployeeTravelRequestPO> activerequest = iCabRequestBO
									.getParticularRequestDetailOnTripComplete(requestDetail.geteFmFmEmployeeTravelRequest().getRequestId());
							activerequest.get(0).setCompletionStatus("Y");
							iCabRequestBO.update(activerequest.get(0));
							if (!(requestDetail.getEmployeeStatus().equalsIgnoreCase("completed"))) {
								requestDetail.setEmployeeStatus("completed");
								iCabRequestBO.update(requestDetail);
							}

						}
					}
				} catch (Exception e) {
					log.info("trip complete exception " + e);
				}
			}
		});
		thread1.start();
		log.info("First Thread tripcompleted End");
		try{
		List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO.getCompletedRouteDataFromAssignRouteId(
				assignRoutePO.geteFmFmClientBranchPO().getBranchId(), assignRoutePO.getAssignRouteId());
		if(!(actualRouteTravelled.isEmpty())){
		EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		EFmFmAssignRoutePO efmFmAssignRoute = new EFmFmAssignRoutePO();
		eFmFmClientBranchPO.setBranchId(actualRouteTravelled.get(0).geteFmFmClientBranchPO().getBranchId());
		efmFmAssignRoute.setAssignRouteId(actualRouteTravelled.get(0).getEfmFmAssignRoute().getAssignRouteId());
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (EFmFmLiveRoutTravelledPO liveRouteDetail : actualRouteTravelled) {
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
					assignRouteBO.save(actualRoute);
					assignRouteBO.deleteParticularActualTravelled(liveRouteDetail.getLiveRouteTravelId());					
				}
			}
		});
		thread2.start();
		}}catch(Exception e){
			log.info("table purging"+e);
		}
		log.info("Second Thread tripcompleted End");
		requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	// Optimized drop route

	@POST
	@Path("/optimizeroute")
	public Response OptimozedDropTrip(EFmFmAssignRoutePO assignRoutePO) throws IOException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		Map<String, Object> requests = new HashMap<String, Object>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
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
		   }
		 
		assignRoutePO.setTripAssignDate(new Date());
		String baseLatLng = "";
		List<EFmFmAssignRoutePO> assignRoute = iCabRequestBO.getParticularDriverAssignTripDetail(assignRoutePO);
		List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO
				.getParticularTripAllEmployees(assignRoute.get(0).getAssignRouteId());
		String wayPoints = "";
		StringBuffer allwayPoints = new StringBuffer();
		StringBuffer ids = new StringBuffer();
		baseLatLng = assignRoute.get(0).geteFmFmClientBranchPO().getLatitudeLongitude();
		allwayPoints.append(baseLatLng + "|");
		if (!(employeeTripDetailPO.isEmpty())) {
			for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
				if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("N")) {
					ids.append(employeeTripDetail.getEmpTripId() + ",");
					allwayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
							.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getLatitudeLongitude() + "|");
				}
			}
		}
		wayPoints = allwayPoints.toString();
		String urlLocation = "";
		String directionApi = ContextLoader.getContext().getMessage("google.directionApi", null, "directionApi", null);
		urlLocation = directionApi + baseLatLng + "&destination=" + baseLatLng + "&waypoints=optimize:true|" + wayPoints
				+ "&sensor=true";
		log.info("urlLocation" + urlLocation);
		URL geocodeURL;
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
			JSONArray routes = new JSONObject(data).getJSONArray("routes");
			JSONArray elements = routes.getJSONObject(0).getJSONArray("waypoint_order");
			int indexOfOpenBracket = elements.toString().toString().indexOf(",");
			int indexOfLastBracket = elements.toString().toString().lastIndexOf("]");
			StringTokenizer stringTokenizer = new StringTokenizer(
					elements.toString().substring(indexOfOpenBracket + 1, indexOfLastBracket), ",");
			String empId = "";
			int i = 0;
			while (stringTokenizer.hasMoreElements()) {
				empId = (String) stringTokenizer.nextElement();
				employeeTripDetailPO.get(i).setOrderId(Integer.parseInt(empId));
				iCabRequestBO.update(employeeTripDetailPO.get(i));
				i++;

			}
		}
		requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	// Previously i am using employee id need to change with request Id
	@POST
	@Path("/travelrequest")
	public Response employeeTravelRequestFromWeb(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO) {
		Map<String, Object> requests = new HashMap<String, Object>();
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
		int indexOfOpenBracket = employeeTravelRequestPO.getEmployeeId().toString().indexOf("[");
		int indexOfLastBracket = employeeTravelRequestPO.getEmployeeId().toString().lastIndexOf("]");
		StringTokenizer stringTokenizer = new StringTokenizer(employeeTravelRequestPO.getEmployeeId().toString()
				.substring(indexOfOpenBracket + 1, indexOfLastBracket), ",");
		String requestId = "";
		while (stringTokenizer.hasMoreElements()) {
			requestId = (String) stringTokenizer.nextElement();
			List<EFmFmVehicleCheckInPO> listOfCheckedInVehicle = iVehicleCheckInBO
					.getCheckedInVehicleDetails(employeeTravelRequestPO.getCombinedFacility(), new Date());
			employeeTravelRequestPO.setRequestDate(new Date());
			employeeTravelRequestPO.setRequestId(Integer.valueOf(requestId));
			EFmFmUserMasterPO efmFmUserMaster = new EFmFmUserMasterPO();
			EFmFmClientBranchPO clientBranch = new EFmFmClientBranchPO();
			clientBranch.setBranchId(employeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
					.geteFmFmClientBranchPO().getBranchId());
			efmFmUserMaster.seteFmFmClientBranchPO(clientBranch);
			employeeTravelRequestPO.setEfmFmUserMaster(efmFmUserMaster);
			List<EFmFmEmployeeTravelRequestPO> requestDetails = iCabRequestBO
					.getparticularRequestDetail(employeeTravelRequestPO);
			if (!(listOfCheckedInVehicle.isEmpty())) {
				requestDetails.get(0).setReadFlg("Y");
				iCabRequestBO.update(requestDetails.get(0));
				requests.put("status", "success");
				 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}
			if (listOfCheckedInVehicle.isEmpty()) {
				List<EFmFmVehicleCheckInPO> listAssignedVehicles = iVehicleCheckInBO
						.getAssignedVehicleDetails(employeeTravelRequestPO.geteFmFmEmployeeRequestMaster()
								.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(), new Date());
				if (listAssignedVehicles.isEmpty()) {
					requests.put("status", "fail");
					 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				} else {
					IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext()
							.getBean("IAssignRouteBO");
					List<EFmFmUserMasterPO> empDetails = iEmployeeDetailBO.getParticularEmpDetailsFromUserId(
							requestDetails.get(0).geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getUserId(),
							employeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
									.geteFmFmClientBranchPO().getBranchId());
					EFmFmAssignRoutePO assignRoute = new EFmFmAssignRoutePO();
					assignRoute.setTripAssignDate(new Date());
					EFmFmClientBranchPO clientBranchId = new EFmFmClientBranchPO();
					clientBranchId.setBranchId(employeeTravelRequestPO.geteFmFmEmployeeRequestMaster()
							.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
					EFmFmRouteAreaMappingPO routeAreaMappingPO = new EFmFmRouteAreaMappingPO();
					routeAreaMappingPO.setRouteAreaId(empDetails.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
					assignRoute.seteFmFmClientBranchPO(clientBranchId);
					assignRoute.seteFmFmRouteAreaMapping(routeAreaMappingPO);
					assignRoute.setTripType(requestDetails.get(0).geteFmFmEmployeeRequestMaster().getTripType());
					List<EFmFmAssignRoutePO> routeDetail = assignRouteBO.getAllOnlyAssignedTrips(assignRoute);
					if (routeDetail.size() > 0) {
						requestDetails.get(0).setReadFlg("Y");
						iCabRequestBO.update(requestDetails.get(0));
						requests.put("status", "success");
						 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
					requests.put("status", "fail");
					 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();

				}

			}
		}
		requests.put("status", "fail");
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/updatedriverloc")
	public Response driverUpdateLoc(final EFmFmActualRoutTravelledPO actualRouteTravelledPO)
		throws IOException, ParseException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
		final Map<String, Object> requests = new HashMap<String, Object>();
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");		
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		log.info("serviceStart -UserId :" + actualRouteTravelledPO.getUserId());		 		
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));		
		List<Map<String, Object>> tripChangedStatus = new ArrayList<Map<String, Object>>();
		final CabRequestService signer = new CabRequestService();
		String eta = "calculating...";
		try {
			
			EFmFmClientBranchPO clientBranchPO = new EFmFmClientBranchPO();
			clientBranchPO.setBranchId(actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId());
			actualRouteTravelledPO.seteFmFmClientBranchPO(clientBranchPO);
			eta = signer.locationUpdater(actualRouteTravelledPO);	
			List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;			
			employeeTripDetailPO = iCabRequestBO.getParticularTripModificationDetails(actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
			
			if (!(employeeTripDetailPO.isEmpty())) {
				String emplyeeId="";
				for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {					
					if(actualRouteTravelledPO.getSysUpdatedStatus().equalsIgnoreCase("NOCHANGES")){
					    Map<String, Object> employeeDetails = new HashMap<String, Object>();
						try {
							 employeeDetails.put("sysUpdatedStatus","RECEIVED");							
						    if(null !=employeeTripDetail.getEfmFmAssignRoute().getTripSheetModifiedStatus() 
										&& !(employeeTripDetail.getEfmFmAssignRoute().getTripSheetModifiedStatus().equalsIgnoreCase("NOCHANGES")) 
										&& !(employeeTripDetail.getEfmFmAssignRoute().getTripSheetModifiedStatus().equals(emplyeeId))){
						    	 employeeDetails.put("updatedFrom","W");
						    	 employeeDetails.put("employeeId", employeeTripDetail.getEfmFmAssignRoute().getTripSheetModifiedStatus());
								 employeeDetails.put("tripSheetStatus","removed");
								 emplyeeId= employeeTripDetail.getEfmFmAssignRoute().getTripSheetModifiedStatus();
								 tripChangedStatus.add(employeeDetails);
							}else if(null !=employeeTripDetail.geteFmFmEmployeeTravelRequest().getTripSheetStatus()
									&& !(employeeTripDetail.geteFmFmEmployeeTravelRequest().getTripSheetStatus().equalsIgnoreCase("NOCHANGES"))){						
								 employeeDetails.put("updatedFrom", employeeTripDetail.geteFmFmEmployeeTravelRequest().getTripStatusUpdatedFrom());								
								 employeeDetails.put("tripSheetStatus",employeeTripDetail.geteFmFmEmployeeTravelRequest().getTripSheetStatus());
								 employeeDetails.put("employeeId", employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getEmployeeId());
								   if(employeeTripDetail.geteFmFmEmployeeTravelRequest().getTripSheetStatus() .equalsIgnoreCase("PICKTIMECHANGED")){
									   employeeDetails.put("pickUpTime", shiftTimeFormater.format(employeeTripDetail.geteFmFmEmployeeTravelRequest().getPickUpTime()));						  
									   employeeDetails.put("modPickUpTime", shiftTimeFormater.format(employeeTripDetail.geteFmFmEmployeeTravelRequest().getModifiedPickUpTime()));											
									}else{
										 employeeDetails.put("tripSheetStatus",employeeTripDetail.geteFmFmEmployeeTravelRequest().getTripSheetStatus());
										 
									}
								   tripChangedStatus.add(employeeDetails);				   
							}					
						} catch (Exception e) {
							log.debug("pickupTimeUpdated");
							e.printStackTrace();
						}
					 }else{						 
						 requests.put("sysUpdatedStatus", "NOCHANGES");						 
						 List<EFmFmEmployeeTravelRequestPO> tripStatusUpdate= iCabRequestBO.tripRequestDetailsByTravelMaster
								 (employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestId());						 
						 tripStatusUpdate.get(0).setTripSheetStatus("NOCHANGES");
						 iCabRequestBO.update(tripStatusUpdate.get(0));		
						List<EFmFmAssignRoutePO> updatedDetails=iAssignRouteBO.getParticularRouteDetailFromAssignRouteId(actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
						 if(!(updatedDetails.isEmpty())){
								if(null !=updatedDetails.get(0).getTripSheetModifiedStatus() 
										&& !(updatedDetails.get(0).getTripSheetModifiedStatus().equals("NOCHANGES"))){
									updatedDetails.get(0).setTripSheetModifiedStatus("NOCHANGES");
									iAssignRouteBO.update(updatedDetails.get(0));							
								}	
						 }
					 }			
				}
				requests.put("tripChanges", tripChangedStatus);			 
			}
		 } catch (Exception e) {
			log.info("Error" + e);
		}
		log.info("driverUpdate" + eta);
		requests.put("eta", eta);
		requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + actualRouteTravelledPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}
	
	
	@POST
	@Path("/updateDriverGeocode")
	public Response updateDriverGeocode(EFmFmActualRoutTravelledPO actualRouteTravelledPO)
			throws IOException, ParseException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
		final Map<String, Object> requests = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" + actualRouteTravelledPO.getUserId());		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));		
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		 
		final CabRequestService signer = new CabRequestService();
		EFmFmClientBranchPO clientBranchPO = new EFmFmClientBranchPO();

		String eta = "calculating...";
		try {
			clientBranchPO.setBranchId(actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId());
//			actualRouteTravelledPO.seteFmFmClientBranchPO(clientBranchPO);
//			eta = signer.locationUpdater(actualRouteTravelledPO);
		} catch (Exception e) {
			log.info("Error" + e);
		}
		
		
		 
		EFmFmLiveRoutTravelledPO liveRouteDetail = new EFmFmLiveRoutTravelledPO();
	// saving speed alerts...
	try {
		DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String travelTimeFromDevice = actualRouteTravelledPO.getTravelTime();
		Date travelTime = dateTimeFormate.parse(travelTimeFromDevice);
		liveRouteDetail.setLiveTravelledTime(travelTime);
	} catch (Exception e) {
		log.info("travelTime Error" + e);
		liveRouteDetail.setLiveTravelledTime(new Date());
	}
	try{
	liveRouteDetail.setLiveTravellesDistance(String.valueOf(actualRouteTravelledPO.getTravelledDistance()));
	}catch(Exception e){
		log.info("live route distance error"+e);
	}
	
	EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
	assignRoutePO.setAssignRouteId(actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
	
	liveRouteDetail.setEfmFmAssignRoute(assignRoutePO);
	liveRouteDetail.seteFmFmClientBranchPO(clientBranchPO);
	liveRouteDetail.setLivelatitudeLongitude(actualRouteTravelledPO.getLatitudeLongitude());
//	liveRouteDetail.setLiveEtaInSeconds(etaInSeconds);
	liveRouteDetail.setLiveSpeed(actualRouteTravelledPO.getSpeed());
//	liveRouteDetail.setLiveEta(driverEta);
//	if (cabCurrentLocation.length() != 0 && (!(cabCurrentLocation.isEmpty())))
//		liveRouteDetail.setLiveCurrentCabLocation(cabCurrentLocation);
	assignRouteBO.save(liveRouteDetail);
 		
		
		log.info("driverUpdate" + eta);
		requests.put("eta", eta);
		requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + actualRouteTravelledPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	public String locationUpdater(EFmFmActualRoutTravelledPO actualRouteTravelledPO)
			throws IOException, ParseException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAlertBO iAlertBO = (IAlertBO) ContextLoader.getContext().getBean("IAlertBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");

		final PushNotificationService pushNotification = new PushNotificationService();
		EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
		EFmFmClientBranchPO clientBranch = new EFmFmClientBranchPO();
		clientBranch.setBranchId(actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId());
		assignRoutePO.setAssignRouteId(actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
		assignRoutePO.seteFmFmClientBranchPO(clientBranch);
		List<EFmFmAssignRoutePO> liveRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		String currentLatLong = "";
		if (liveRoute.get(0).getTripStatus().equalsIgnoreCase("completed")) {
			// Trip Completed....
			return "N";
		} else if (actualRouteTravelledPO.getTripUpdateTime() != liveRoute.get(0).getTripUpdateTime().getTime()) {
			// Trip Changed....
			return "C";
		} else if (liveRoute.get(0).getBucketStatus().equalsIgnoreCase("N")) {
			// Processing....
			return "P";
		}
		final List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO
				.getParticularTripNonDropEmployeesDetails(
						actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
		List<EFmFmEmployeeTripDetailPO> dropEmployeeTripDetail = iCabRequestBO
				.getDropTripAllSortedNonDropEmployees(actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());

		List<EFmFmEmployeeTripDetailPO> baseLatiLongi = iCabRequestBO
				.getParticularTripAllEmployees(actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
		final MessagingService messaging = new MessagingService();
		log.info("assign routeId" + actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
		List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO.getRouteLastEtaAndDistanceFromAssignRouteId(
				actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId(),
				actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
		List<EFmFmEmployeeTripDetailPO> allRequests = null;
		StringBuffer allwayPoints = new StringBuffer();
		
		
		DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String travelTimeFromDevice = actualRouteTravelledPO.getTravelTime();
		Date travelTime = dateTimeFormate.parse(travelTimeFromDevice);

		
		
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
			currentLatLong = actualRouteTravelledPO.getLatitudeLongitude();
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
			log.info("new LatitudeLongi" + actualRouteTravelledPO.getLatitudeLongitude());
			if (currentLatLong.equalsIgnoreCase(lastLatLong)) {
				
				
				actualRouteTravelled.get(actualRouteTravelled.size() - 1).setLiveTravelledTime(travelTime);
				assignRouteBO.update(actualRouteTravelled.get(actualRouteTravelled.size() - 1));
				return actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta();
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
		int totalDistance = 0;
		int etaInSeconds = 0;
		String allpoints = allwayPoints.toString();
		String cabCurrentLocation = "";

		if (!(baseLatiLongi.isEmpty())
				&& baseLatiLongi.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
			allRequests = iCabRequestBO.getParticularTripAllEmployees(assignRoutePO.getAssignRouteId());
		} else {
			allRequests = iCabRequestBO.getDropTripAllSortedEmployees(assignRoutePO.getAssignRouteId());
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
			String distanceApi = ContextLoader.getContext().getMessage("google.distanceApi", null, "distanceApi", null);
			String apiConfiguration = ContextLoader.getContext().getMessage("google.apiConfiguration", null,
					"apiConfiguration", null);
			urlLocation = distanceApi + actualRouteTravelledPO.getLatitudeLongitude() + "&destinations=" + allpoints
					+ apiConfiguration;
			log.info("urlLocation" + urlLocation);
			URL geocodeURL;
			URL url = new URL(urlLocation);
			CabRequestService signer = new CabRequestService();
			signer.passingKey(keyString);
			String request = signer.signRequest(url.getPath(), url.getQuery());
			urlLocation = url.getProtocol() + "://" + url.getHost() + request;
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
						totalDistance += elements.getJSONObject(i).getJSONObject("distance").getInt("value");
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
						log.info("Distance ..." + totalDistance);
						if (!(baseLatiLongi.isEmpty())) {
							// GeoFence on trip complete when vehicle reaches
							// back to office
							if (totalDistance < (liveRoute.get(0).geteFmFmClientBranchPO()
									.getEndTripGeoFenceAreaInMeter()) && employeeTripDetailPO.isEmpty()
									&& (!(liveRoute.get(0).getRouteGenerationType().equalsIgnoreCase("nodalAdhoc")))) {
								// Geo Fencing the client Office auto
//								PolygonalGeofence fence = new PolygonalGeofence();
//								StringTokenizer stringTokenizer = new StringTokenizer(
//										liveRoute.get(0).geteFmFmClientBranchPO().getGeoCodesForGeoFence(), "|");
//								String vertex = "";
//								while (stringTokenizer.hasMoreElements()) {
//									vertex = (String) stringTokenizer.nextElement();
//									fence.addVertex(new Geocode(vertex));
//								}
//								fence.setVehicleLocation(new Geocode(actualRouteTravelledPO.getLatitudeLongitude()));
//								log.info("Inside auto trip complete M Distance" + totalDistance);
//								if (fence.isInGeofence()) {
									try {
										EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
										eFmFmVehicleCheckInPO.setCheckInTime(new Date());
										eFmFmVehicleCheckInPO
												.setCheckInId(liveRoute.get(0).getEfmFmVehicleCheckIn().getCheckInId());
										List<EFmFmVehicleCheckInPO> vehicleCheckIn = iVehicleCheckInBO
												.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
										EFmFmVehicleMasterPO vehicleMasterDetail = iVehicleCheckInBO
												.getParticularVehicleDetail(
														vehicleCheckIn.get(0).getEfmFmVehicleMaster().getVehicleId());
										List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO
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
										iVehicleCheckInBO.update(vehicleMasterDetail);
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
											List<EFmFmLiveRoutTravelledPO> routeFirstLocation = assignRouteBO
													.getRouteLastEtaAndDistanceFromAssignRouteIdForCabLastLocation(
															actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId(),
															actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
											try {
												String plannedDistance = calculateDistance.getPlannedDistanceByMapApi(
														routeFirstLocation.get(0).getLivelatitudeLongitude(),
														actualRouteTravelled.get(actualRouteTravelled.size() - 1)
																.getLivelatitudeLongitude(),
														empWayPoints.toString());

												if(actualRouteTravelledPO.getEfmFmAssignRoute().getLocationFlg().equalsIgnoreCase("M")){												
													plannedDistance=multipleDestPlannedDistance
															(actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId(), 
																	routeFirstLocation.get(0).getLivelatitudeLongitude(), 
																	actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());													
												}						
												
												totalTravelledDis = liveRoute.get(0).getTravelledDistance()
														+ (liveRoute.get(0).geteFmFmClientBranchPO()
																.getAddingGeoFenceDistanceIntrip());
												liveRoute.get(0)
														.setPlannedDistance(Double.parseDouble(plannedDistance)
																+ (liveRoute.get(0).geteFmFmClientBranchPO()
																		.getAddingGeoFenceDistanceIntrip()));
												liveRoute.get(0).setTravelledDistance(totalTravelledDis);

											} catch (Exception e) {
												log.info("auto trip complete auto drop" + e);
											}

										}
										// start auto trip complete by geo fence
										// and updation of planned distance

										liveRoute.get(0).setOdometerEndKm(assignRoutePO.getOdometerEndKm());
										liveRoute.get(0).setPlannedTravelledDistance(totalTravelledDis);
										liveRoute.get(0).setPlannedTime(
												(new Date().getTime() - liveRoute.get(0).getTripStartTime().getTime())
														/ 1000);
										iCabRequestBO.update(liveRoute.get(0));
										if (oldCheckRoutesCheck.size() == 1) {
											EFmFmDriverMasterPO particularDriverDetails = approvalBO
													.getParticularDriverDetail(
															vehicleCheckIn.get(0).getEfmFmDriverMaster().getDriverId());
											particularDriverDetails.setStatus("A");
											approvalBO.update(particularDriverDetails);
											log.info("Inside GeoFence Area" + vehicleMasterDetail.getVehicleNumber());
											List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO
													.getDeviceDetailsFromDeviceId(
															vehicleCheckIn.get(0).geteFmFmDeviceMaster().getDeviceId(),
															new MultifacilityService().combinedBranchIdDetails(actualRouteTravelledPO.getUserId(),actualRouteTravelledPO.getCombinedFacility()));
											deviceDetails.get(0).setStatus("Y");
											iVehicleCheckInBO.update(deviceDetails.get(0));
										}
										try {
											vehicleCheckIn.get(0).setTotalTravelDistance(
													(vehicleCheckIn.get(0).getTotalTravelDistance()
															- liveRoute.get(0).getPlannedDistance())
															+ totalTravelledDis);
										} catch (Exception e) {
											log.info("Error updating the travelled and number of trips" + e);
										}
										iVehicleCheckInBO.update(vehicleCheckIn.get(0));
										if (liveRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
											try {
												int a = liveRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId();
												List<EFmFmEscortCheckInPO> escortDetails = iVehicleCheckInBO
														.getParticulaEscortDetailFromEscortId(actualRouteTravelledPO.getCombinedFacility(),
																liveRoute.get(0).geteFmFmEscortCheckIn()
																		.getEscortCheckInId());
												// escortDetails.get(0).setEscortCheckOutTime(new
												// Date());
												escortDetails.get(0).setStatus("Y");
												iVehicleCheckInBO.update(escortDetails.get(0));
											} catch (Exception e) {
												log.info("Escort Blog Exception" + e);
											}
										}

										vehicleMasterDetail
												.setMonthlyPendingFixedDistance(plannedDis - totalTravelledDis);
										iVehicleCheckInBO.update(vehicleMasterDetail);

										log.info("allRequests" + allRequests.size());
										if (!(allRequests.isEmpty())) {
											for (EFmFmEmployeeTripDetailPO requestDetail : allRequests) {
												List<EFmFmEmployeeTravelRequestPO> activerequest = iCabRequestBO
														.getParticularRequestDetailOnTripComplete(requestDetail.geteFmFmEmployeeTravelRequest()
																		.getRequestId());
												activerequest.get(0).setCompletionStatus("Y");
												iCabRequestBO.update(activerequest.get(0));
												if (!(requestDetail.getEmployeeStatus()
														.equalsIgnoreCase("completed"))) {
													requestDetail.setEmployeeStatus("completed");
													iCabRequestBO.update(requestDetail);
												}

											}
										}
									} catch (Exception e) {
										log.info("Inside auto tripComplete" + e);
									}
									log.info("First Thread tripcompleted End");
									try{
									List<EFmFmLiveRoutTravelledPO> liveTravelledRoute = assignRouteBO.getCompletedRouteDataFromAssignRouteId(
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
												assignRouteBO.save(actualRoute);
												assignRouteBO.deleteParticularActualTravelled(liveRouteDetail.getLiveRouteTravelId());					
											}
										}
									});
									thread2.start();
								}catch(Exception e){
									log.info("table purging"+e);
								}
									log.info("Second Thread tripcompleted End");
//								}
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
		if (!(baseLatiLongi.isEmpty())
				&& baseLatiLongi.get(0).getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
			String directionApi = ContextLoader.getContext().getMessage("google.directionApi", null, "directionApi",
					null);
			urlLocation = directionApi + actualRouteTravelledPO.getLatitudeLongitude() + "&destination="
					+ baseLatiLongi.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().geteFmFmClientBranchPO()
							.getLatitudeLongitude()
					+ "&waypoints=optimize:false|" + allwayPoints.toString()
					+ "&mode=driving&language=en-EN&sensor=false&client=gme-newtglobalindiaprivate";
			URL geocodeURL;
			URL url = new URL(urlLocation);
			CabRequestService signer = new CabRequestService();
			signer.passingKey(keyString);
			String request = signer.signRequest(url.getPath(), url.getQuery());
			urlLocation = url.getProtocol() + "://" + url.getHost() + request;
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
						totalDistance += elements.getJSONObject(i).getJSONObject("distance").getInt("value");
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
						log.info("Distance ..." + totalDistance);
						if (!(baseLatiLongi.isEmpty())) {
							// GeoFence on trip complete when vehicle reaches
							// back to office
							if (totalDistance < (liveRoute.get(0).geteFmFmClientBranchPO()
									.getEndTripGeoFenceAreaInMeter()) && employeeTripDetailPO.isEmpty()
									&& (!(liveRoute.get(0).getRouteGenerationType().equalsIgnoreCase("nodalAdhoc")))) {
								// Geo Fencing the client Office auto
//								PolygonalGeofence fence = new PolygonalGeofence();
//								StringTokenizer stringTokenizer = new StringTokenizer(
//										liveRoute.get(0).geteFmFmClientBranchPO().getGeoCodesForGeoFence(), "|");
//								String vertex = "";
//								while (stringTokenizer.hasMoreElements()) {
//									vertex = (String) stringTokenizer.nextElement();
//									fence.addVertex(new Geocode(vertex));
//								}
//								fence.setVehicleLocation(new Geocode(actualRouteTravelledPO.getLatitudeLongitude()));
//								log.info("Inside auto trip complete M Distance" + totalDistance);
//								if (fence.isInGeofence()) {
									try {
										EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
										eFmFmVehicleCheckInPO.setCheckInTime(new Date());
										eFmFmVehicleCheckInPO
												.setCheckInId(liveRoute.get(0).getEfmFmVehicleCheckIn().getCheckInId());
										List<EFmFmVehicleCheckInPO> vehicleCheckIn = iVehicleCheckInBO
												.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
										EFmFmVehicleMasterPO vehicleMasterDetail = iVehicleCheckInBO
												.getParticularVehicleDetail(
														vehicleCheckIn.get(0).getEfmFmVehicleMaster().getVehicleId());

										List<EFmFmAssignRoutePO> oldCheckRoutesCheck = assignRouteBO
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
										iVehicleCheckInBO.update(vehicleMasterDetail);

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
											List<EFmFmLiveRoutTravelledPO> routeFirstLocation = assignRouteBO
													.getRouteLastEtaAndDistanceFromAssignRouteIdForCabLastLocation(
															actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId(),
															actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());

											try {
												String plannedDistance = calculateDistance.getPlannedDistanceByMapApi(
														routeFirstLocation.get(0).getLivelatitudeLongitude(),
														actualRouteTravelled.get(actualRouteTravelled.size() - 1)
																.getLivelatitudeLongitude(),
														empWayPoints.toString());
												
												if(actualRouteTravelledPO.getEfmFmAssignRoute().getLocationFlg().equalsIgnoreCase("M")){												
													plannedDistance=multipleDestPlannedDistance
															(actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId(), 
																	routeFirstLocation.get(0).getLivelatitudeLongitude(), 
																	actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude());													
												}
												
												
												totalTravelledDis = liveRoute.get(0).getTravelledDistance()
														+ (liveRoute.get(0).geteFmFmClientBranchPO()
																.getAddingGeoFenceDistanceIntrip());
												liveRoute.get(0)
														.setPlannedDistance(Double.parseDouble(plannedDistance)
																+ (liveRoute.get(0).geteFmFmClientBranchPO()
																		.getAddingGeoFenceDistanceIntrip()));
												liveRoute.get(0).setTravelledDistance(totalTravelledDis);

											} catch (Exception e) {
												log.info("auto trip complete planned distance by button click" + e);
											}

										}

										// end auto trip complete by geo fence
										// and updation of planned distance
										liveRoute.get(0).setPlannedTravelledDistance(totalTravelledDis);
										liveRoute.get(0).setPlannedTime(
												(new Date().getTime() - liveRoute.get(0).getTripStartTime().getTime())
														/ 1000);
										liveRoute.get(0).setOdometerEndKm(assignRoutePO.getOdometerEndKm());
										iCabRequestBO.update(liveRoute.get(0));
										if (oldCheckRoutesCheck.size() == 1) {
											EFmFmDriverMasterPO particularDriverDetails = approvalBO
													.getParticularDriverDetail(
															vehicleCheckIn.get(0).getEfmFmDriverMaster().getDriverId());
											particularDriverDetails.setStatus("A");
											approvalBO.update(particularDriverDetails);
											log.info("Inside GeoFence Area" + vehicleMasterDetail.getVehicleNumber());
											List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO
													.getDeviceDetailsFromDeviceId(
															vehicleCheckIn.get(0).geteFmFmDeviceMaster().getDeviceId(),
															new MultifacilityService().combinedBranchIdDetails(actualRouteTravelledPO.getUserId(),actualRouteTravelledPO.getCombinedFacility()));
											deviceDetails.get(0).setStatus("Y");
											iVehicleCheckInBO.update(deviceDetails.get(0));
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

										iVehicleCheckInBO.update(vehicleCheckIn.get(0));
										iVehicleCheckInBO.update(vehicleMasterDetail);
										if (liveRoute.get(0).getEscortRequredFlag().equalsIgnoreCase("Y")) {
											try {
												int a = liveRoute.get(0).geteFmFmEscortCheckIn().getEscortCheckInId();
												List<EFmFmEscortCheckInPO> escortDetails = iVehicleCheckInBO
														.getParticulaEscortDetailFromEscortId(actualRouteTravelledPO.getCombinedFacility(),
																liveRoute.get(0).geteFmFmEscortCheckIn()
																		.getEscortCheckInId());
												// escortDetails.get(0).setEscortCheckOutTime(new
												// Date());
												escortDetails.get(0).setStatus("Y");
												iVehicleCheckInBO.update(escortDetails.get(0));
											} catch (Exception e) {
												log.info("Escort Blog Exception" + e);
											}
										}

										vehicleMasterDetail
												.setMonthlyPendingFixedDistance(plannedDis - totalTravelledDis);
										iVehicleCheckInBO.update(vehicleMasterDetail);
										log.info("allRequests" + allRequests.size());
										if (!(allRequests.isEmpty())) {
											for (EFmFmEmployeeTripDetailPO requestDetail : allRequests) {
												List<EFmFmEmployeeTravelRequestPO> activerequest = iCabRequestBO
														.getParticularRequestDetailOnTripComplete(requestDetail.geteFmFmEmployeeTravelRequest()
																		.getRequestId());
												activerequest.get(0).setCompletionStatus("Y");
												iCabRequestBO.update(activerequest.get(0));
												if (!(requestDetail.getEmployeeStatus()
														.equalsIgnoreCase("completed"))) {
													requestDetail.setEmployeeStatus("completed");
													iCabRequestBO.update(requestDetail);
												}

											}
										}
									} catch (Exception e) {
										log.info("Inside auto tripComplete" + e);
									}
									log.info("First Thread tripcompleted End");
									try{
									List<EFmFmLiveRoutTravelledPO> liveTravelledRoute = assignRouteBO.getCompletedRouteDataFromAssignRouteId(
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
												assignRouteBO.save(actualRoute);
												assignRouteBO.deleteParticularActualTravelled(liveRouteDetail.getLiveRouteTravelId());					
											}
										}
									});
									thread2.start();
								}catch(Exception e){
									log.info("table purging"+e);
								}
									log.info("Second Thread tripcompleted End");
//								}
							}
							if (etaFlag) {
								etaFlag = false;
								etaInSeconds = eta;
								driverEta = etaInSec;
								List<EFmFmEmployeeTripDetailPO> employeeTrips = iCabRequestBO
										.getParticularTripNonDropEmployeesDetails(
												actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
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
									employeeTripDetailPO.get(i).setCabstartFromDestination(new Date().getTime());
									// liveRoute.get(0).setTripUpdateTime(new
									// Date());
									iCabRequestBO.update(liveRoute.get(0));
								}
								final int k = i;
								final String employeeMsgEta = etaInSec;
								// geofence reached message
								if (employeeTripDetailPO.get(k).getReachedFlg().equalsIgnoreCase("N")
										&& totalDistance < (liveRoute.get(0).geteFmFmClientBranchPO()
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
									iCabRequestBO.update(liveRoute.get(0));
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
													// StringTokenizer token=new
													// StringTokenizer(new
													// String(Base64.getDecoder().decode(employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
													// .getHostMobileNumber()),"utf-8"),",");
													// while(token.hasMoreElements()){
													// messaging.etaMessagesForGuest(
													// new
													// String(Base64.getDecoder().decode(String.valueOf(employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
													// .getFirstName())),
													// "utf-8"),
													//
													// employeeTripDetailPO.get(k).getEfmFmAssignRoute()
													// .getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													// .getVehicleNumber(),
													// employeeMsgEta,token.nextElement().toString(),
													// employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
													// .getRequestType());
													// }
													// messaging.etaMessagesForGuest(
													// new
													// String(Base64.getDecoder().decode(String.valueOf(employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
													// .getFirstName())),
													// "utf-8"),
													//
													// employeeTripDetailPO.get(k).getEfmFmAssignRoute()
													// .getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													// .getVehicleNumber(),
													// employeeMsgEta,
													// new
													// String(Base64.getDecoder().decode(String.valueOf(employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
													// .getMobileNumber())),
													// "utf-8"),
													// employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
													// .getRequestType());
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
														// messaging.etaMessagesForGuestWhenCabDelay(
														// new
														// String(Base64.getDecoder().decode(String.valueOf(employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
														// .getFirstName())),
														// "utf-8"),
														// employeeTripDetailPO.get(k).getEfmFmAssignRoute()
														// .getEfmFmVehicleCheckIn()
														// .getEfmFmVehicleMaster().getVehicleNumber(),
														// employeeMsgEta,
														// new
														// String(Base64.getDecoder().decode(String.valueOf(employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
														// .getMobileNumber())),
														// "utf-8"),
														//
														// employeeTripDetailPO.get(k)
														// .geteFmFmEmployeeTravelRequest()
														// .getRequestType());
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
										&& totalDistance < (liveRoute.get(0).geteFmFmClientBranchPO()
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
												// text="Dear employee your
												// point";
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
									iCabRequestBO.update(liveRoute.get(0));
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
													// messaging.etaMessagesForGuest(
													// new
													// String(Base64.getDecoder().decode(String.valueOf(employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
													// .getFirstName())),
													// "utf-8"),
													// employeeTripDetailPO.get(k).getEfmFmAssignRoute()
													// .getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
													// .getVehicleNumber(),
													// employeeMsgEta,
													// new
													// String(Base64.getDecoder().decode(String.valueOf(employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
													// .getMobileNumber())),
													// "utf-8"),
													//
													// employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest()
													// .getRequestType());
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
														// messaging.etaMessagesForGuestWhenCabDelay(
														// new
														// String(Base64.getDecoder().decode(String.valueOf(employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
														// .getFirstName())),
														// "utf-8"),
														// employeeTripDetailPO.get(k).getEfmFmAssignRoute()
														// .getEfmFmVehicleCheckIn()
														// .getEfmFmVehicleMaster().getVehicleNumber(),
														// employeeMsgEta,
														// new
														// String(Base64.getDecoder().decode(String.valueOf(employeeTripDetailPO.get(k).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
														// .getMobileNumber())),
														// "utf-8"),
														//
														// employeeTripDetailPO.get(k)
														// .geteFmFmEmployeeTravelRequest()
														// .getRequestType());
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
							iCabRequestBO.update(employeeTripDetailPO.get(i));
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

		List<EFmFmLiveRoutTravelledPO> avaoidingDuplicateValues = assignRouteBO
				.getRouteLastEtaAndDistanceFromAssignRouteId(
						actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId(),
						actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
		if (!(avaoidingDuplicateValues.isEmpty()) && avaoidingDuplicateValues.get(avaoidingDuplicateValues.size() - 1)
				.getLivelatitudeLongitude().equalsIgnoreCase(actualRouteTravelledPO.getLatitudeLongitude())) {
			
			
			avaoidingDuplicateValues.get(avaoidingDuplicateValues.size() - 1).setLiveTravelledTime(travelTime);
			assignRouteBO.update(avaoidingDuplicateValues.get(actualRouteTravelled.size() - 1));
			return actualRouteTravelled.get(avaoidingDuplicateValues.size() - 1).getLiveEta();
		}
		if (Double.parseDouble(
				actualRouteTravelledPO.getSpeed()) > (liveRoute.get(0).geteFmFmClientBranchPO().getMaxSpeed())) {
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
			eFmFmTripAlertsPO.setCurrentSpeed(actualRouteTravelledPO.getSpeed());
			eFmFmTripAlertsPO.setStatus("Y");
			iAlertBO.save(eFmFmTripAlertsPO);
		}
		EFmFmLiveRoutTravelledPO liveRouteDetail = new EFmFmLiveRoutTravelledPO();
		try {
			liveRouteDetail.setLiveTravelledTime(travelTime);
		} catch (Exception e) {
			log.info("travelTime Error" + e);
			liveRouteDetail.setLiveTravelledTime(new Date());
		}
		try{
		liveRouteDetail.setLiveTravellesDistance(String.valueOf(actualRouteTravelledPO.getTravelledDistance()));
		liveRoute.get(0)
				.setTravelledDistance(liveRoute.get(0).getTravelledDistance() + Double.parseDouble(actualRouteTravelledPO.getTravelledDistance()));
		assignRouteBO.update(liveRoute.get(0));
		}catch(Exception e){
			log.info("live route distance error"+e);
		}
		liveRouteDetail.setEfmFmAssignRoute(assignRoutePO);
		liveRouteDetail.seteFmFmClientBranchPO(clientBranch);
		liveRouteDetail.setLivelatitudeLongitude(actualRouteTravelledPO.getLatitudeLongitude());
		liveRouteDetail.setLiveEtaInSeconds(etaInSeconds);
		liveRouteDetail.setLiveSpeed(actualRouteTravelledPO.getSpeed());
		liveRouteDetail.setLiveEta(driverEta);
		if (cabCurrentLocation.length() != 0 && (!(cabCurrentLocation.isEmpty())))
			liveRouteDetail.setLiveCurrentCabLocation(cabCurrentLocation);
		assignRouteBO.save(liveRouteDetail);
		return driverEta;
	}

	/**
	 * The shifTime method implemented. for getting the list of shif timing from
	 * shift time table.
	 *
	 * @author Sarfraz Khan
	 * 
	 */
	@POST
	@Path("/editemployeetravelrequest")
	public Response editEmployeeDetailsFromTravelDesk(EFmFmEmployeeTravelRequestPO employeeRequest) {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" +employeeRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getUserId());
	
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getUserId()))){
//
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}
//		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getUserId());
//		   if (!(userDetailToken.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
//		     userDetailToken.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetailToken.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
		 
		 log.info("serviceStart -UserId :" + employeeRequest.getUserId());
		
		employeeRequest.setRequestDate(new Date());
		// List<EFmFmClientUserRolePO>
		// userDetail=userMasterBO.getUserRolesFromUserIdAndBranchId(employeeRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getUserId(),
		// employeeRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
		List<Map<String, Object>> shitTimings = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> allRoutesData = new ArrayList<Map<String, Object>>();

		List<EFmFmClientRouteMappingPO> allRoutes = iRouteDetailBO.getAllRoutesOfParticularClient(String.valueOf(employeeRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId()));
		DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
		List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO
				.listOfShiftTime(new MultifacilityService().combinedBranchIdDetails(employeeRequest.getUserId(),employeeRequest.getCombinedFacility()));
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
		responce.put("status", "success");
		responce.put("shiftTimings", shitTimings);
		responce.put("routesData", allRoutesData);
		 log.info("serviceEnd -UserId :" + employeeRequest.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/allzone")
	public Response getAllZonesUsingClientId(EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO) {
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" + eFmFmClientRouteMappingPO.geteFmFmClientBranchPO().getUserId()); 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientRouteMappingPO.geteFmFmClientBranchPO().getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmClientRouteMappingPO.geteFmFmClientBranchPO().getUserId());
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
		   }
		
		List<Map<String, Object>> allRoutesData = new ArrayList<Map<String, Object>>();
		List<EFmFmClientRouteMappingPO> allRoutes = iRouteDetailBO
				.getAllRoutesOfParticularClientFromBranchId(eFmFmClientRouteMappingPO.getCombinedFacility());
		if (!(allRoutes.isEmpty())) {
			for (EFmFmClientRouteMappingPO routeDetails : allRoutes) {
				Map<String, Object> routeName = new HashMap<String, Object>();
				routeName.put("routeName", routeDetails.geteFmFmZoneMaster().getZoneName());
				routeName.put("facilityName", routeDetails.geteFmFmClientBranchPO().getBranchName());
				routeName.put("routeId", routeDetails.geteFmFmZoneMaster().getZoneId());
				allRoutesData.add(routeName);
			}

		}
		responce.put("status", "success");
		responce.put("zones", allRoutesData);
		 log.info("serviceEnd -UserId :" + eFmFmClientRouteMappingPO.geteFmFmClientBranchPO().getUserId()); 
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	/*
	 * Function for getting all nodal Routes
	 */

	@POST
	@Path("/allNodalzone")
	public Response getAllNodalZonesUsingClientId(EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO) {
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" + eFmFmClientRouteMappingPO.geteFmFmClientBranchPO().getUserId());
	
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientRouteMappingPO.geteFmFmClientBranchPO().getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmClientRouteMappingPO.geteFmFmClientBranchPO().getUserId());
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
		   }
		 
		List<Map<String, Object>> allRoutesData = new ArrayList<Map<String, Object>>();
		List<EFmFmClientRouteMappingPO> allRoutes = iRouteDetailBO
				.getAllNodalRoutesOfParticularClient(eFmFmClientRouteMappingPO.geteFmFmClientBranchPO().getBranchId());
		if (!(allRoutes.isEmpty())) {
			for (EFmFmClientRouteMappingPO routeDetails : allRoutes) {
				Map<String, Object> routeName = new HashMap<String, Object>();
				routeName.put("routeName", routeDetails.geteFmFmZoneMaster().getZoneName());
				routeName.put("branchName", routeDetails.geteFmFmClientBranchPO().getBranchName());
				routeName.put("routeId", routeDetails.geteFmFmZoneMaster().getZoneId());
				allRoutesData.add(routeName);
			}

		}
		responce.put("status", "success");
		responce.put("zones", allRoutesData);
		 log.info("serviceEnd -UserId :" + eFmFmClientRouteMappingPO.geteFmFmClientBranchPO().getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Function for getting all nodal Areas
	 */

	@POST
	@Path("/allNodalZoneAreas")
	public Response getAllNodalZonesAreasUsingClientIdAndRouteId(EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO) {
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientRouteMappingPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmClientRouteMappingPO.getUserId());
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
		   }
		 

		 log.info("serviceStart -UserId :" + eFmFmClientRouteMappingPO.getUserId());
		List<Map<String, Object>> allRoutesData = new ArrayList<Map<String, Object>>();
		List<EFmFmRouteAreaMappingPO> allRoutesAreas = iRouteDetailBO
				.getAllAreasFromZoneId(eFmFmClientRouteMappingPO.getRouteId());
		if (!(allRoutesAreas.isEmpty())) {
			for (EFmFmRouteAreaMappingPO routeDetails : allRoutesAreas) {
				Map<String, Object> routeName = new HashMap<String, Object>();
				routeName.put("areaName", routeDetails.getEfmFmAreaMaster().getAreaName());
				routeName.put("areaId", routeDetails.getEfmFmAreaMaster().getAreaId());
				allRoutesData.add(routeName);
			}
		}
		responce.put("status", "success");
		responce.put("zones", allRoutesData);
		 log.info("serviceEnd -UserId :" + eFmFmClientRouteMappingPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Function for getting all nodal point of all areas
	 */

	@POST
	@Path("/allNodalPointsAreas")
	public Response getAllNodalPointAreaWise(EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO) {
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientRouteMappingPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
	
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmClientRouteMappingPO.getUserId());
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
		   }
		 
		 log.info("serviceStart -UserId :" + eFmFmClientRouteMappingPO.getUserId());
		List<Map<String, Object>> allNodelData = new ArrayList<Map<String, Object>>();
		List<EFmFmRouteAreaMappingPO> allNodelPoints = iRouteDetailBO
				.getAllAreasFromZoneId(eFmFmClientRouteMappingPO.getRouteId());
		if (!(allNodelPoints.isEmpty())) {
			for (EFmFmRouteAreaMappingPO points : allNodelPoints) {
				Map<String, Object> nodelPoints = new HashMap<String, Object>();
				nodelPoints.put("nodelPointName", points.geteFmFmNodalAreaMaster().getNodalPointName());
				nodelPoints.put("nodalPointDescription", points.geteFmFmNodalAreaMaster().getNodalPointDescription());
				nodelPoints.put("nodalPointId", points.geteFmFmNodalAreaMaster().getNodalPointId());
				nodelPoints.put("nodalPoints", points.geteFmFmNodalAreaMaster().getNodalPoints());
				nodelPoints.put("pointAddress", points.geteFmFmNodalAreaMaster().getNodalPointsAddress());
				allNodelData.add(nodelPoints);
			}
		}
		responce.put("status", "success");
		responce.put("zones", allNodelData);
		 log.info("serviceEnd -UserId :" + eFmFmClientRouteMappingPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	
	
	/*
	 * Function for getting all nodal Points By BranchId
	 */

	@POST
	@Path("/allNodalPoints")
	public Response getAllNodalPointsByClientId(EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO) {
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientRouteMappingPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmClientRouteMappingPO.getUserId());
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
		   }
		 
		 log.info("serviceStart -UserId :" + eFmFmClientRouteMappingPO.getUserId());
		List<Map<String, Object>> allNodelData = new ArrayList<Map<String, Object>>();
		List<EFmFmAreaNodalMasterPO> allNodelPoints = iRouteDetailBO
				.getAllNodalPointsByBranchId(eFmFmClientRouteMappingPO.geteFmFmClientBranchPO().getBranchId());
		if (!(allNodelPoints.isEmpty())) {
			for (EFmFmAreaNodalMasterPO points : allNodelPoints) {
				Map<String, Object> nodelPoints = new HashMap<String, Object>();
				nodelPoints.put("nodelPointName", points.getNodalPointName());
				nodelPoints.put("nodalPointDescription", points.getNodalPointDescription());
				nodelPoints.put("nodalPointId", points.getNodalPointId());
				nodelPoints.put("nodalPoints", points.getNodalPoints());
				nodelPoints.put("pointAddress", points.getNodalPointsAddress());
				allNodelData.add(nodelPoints);
			}
		}
		responce.put("status", "success");
		responce.put("zones", allNodelData);
		 log.info("serviceEnd -UserId :" + eFmFmClientRouteMappingPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	
	

	@POST
	@Path("/addzone")
	public Response addNewZone(EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO) {
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientRouteMappingPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmClientRouteMappingPO.getUserId());
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
		   }
		 
		
		 log.info("serviceStart -UserId :" + eFmFmClientRouteMappingPO.getUserId());		 
			StringTokenizer stringTokenizer = new StringTokenizer(eFmFmClientRouteMappingPO.getCombinedFacility(), ",");
	        String branchId = "";
			while (stringTokenizer.hasMoreElements()) {
				branchId = (String) stringTokenizer.nextElement();		 
		List<EFmFmClientRouteMappingPO> allRoutes = iRouteDetailBO.getParticularRouteDetailByClient(branchId,
				eFmFmClientRouteMappingPO.getRouteName());
//		if (!(allRoutes.isEmpty())) {
	//		responce.put("status", "fail");
//			 log.info("serviceEnd -UserId :" + eFmFmClientRouteMappingPO.getUserId());
//			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		}
		
		if (allRoutes.isEmpty()) {
			EFmFmClientRouteMappingPO clientDetails=new EFmFmClientRouteMappingPO();

			List<EFmFmZoneMasterPO> newZoneDetail = iRouteDetailBO
					.getAllRouteName(eFmFmClientRouteMappingPO.getRouteName());		
			if (newZoneDetail.isEmpty()){ 
				EFmFmZoneMasterPO eFmFmZoneMaster = new EFmFmZoneMasterPO();
				eFmFmZoneMaster.setZoneName(eFmFmClientRouteMappingPO.getRouteName().toUpperCase());
				eFmFmZoneMaster.setStatus("Y");
				eFmFmZoneMaster.setNodalRoute(false);
				eFmFmZoneMaster.setCreationTime(new Date());
				eFmFmZoneMaster.setUpdatedTime(new Date());
				iRouteDetailBO.saveRouteNameRecord(eFmFmZoneMaster);
				newZoneDetail = iRouteDetailBO.getAllRouteName(eFmFmClientRouteMappingPO.getRouteName());
			}
//	 		List<EFmFmAreaMasterPO> areaExist=iRouteDetailBO.getAllAreaName(eFmFmClientRouteMappingPO.getRouteName().toUpperCase().trim());    
//			if (areaExist.isEmpty()) {
//				EFmFmAreaMasterPO eFmFmAreaMaster = new EFmFmAreaMasterPO();
//				eFmFmAreaMaster.setAreaDescription(eFmFmClientRouteMappingPO.getRouteName());
//				eFmFmAreaMaster.setAreaName(eFmFmClientRouteMappingPO.getRouteName().toUpperCase());
//				iRouteDetailBO.saveAreaRecord(eFmFmAreaMaster);
//				areaExist=iRouteDetailBO.getAllAreaName(eFmFmClientRouteMappingPO.getRouteName().toUpperCase().trim());  
//			}		
			    clientDetails.seteFmFmZoneMaster(newZoneDetail.get(0));
				EFmFmClientBranchPO eFmFmClientBranchPO=new EFmFmClientBranchPO();
				eFmFmClientBranchPO.setBranchId(Integer.parseInt(branchId));
				clientDetails.seteFmFmClientBranchPO(eFmFmClientBranchPO);
				iRouteDetailBO.saveClientRouteMapping(clientDetails);
			}
	}
		
//		responce.put("routeId", newZoneDetail.get(0).getZoneId());
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + eFmFmClientRouteMappingPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/zonevicearea")
	public Response getAllAreasOfAParticularZone(EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO) {
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" + eFmFmClientRouteMappingPO.getUserId());		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientRouteMappingPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmClientRouteMappingPO.getUserId());
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
		   }
		 
		List<Map<String, Object>> allAreasData = new ArrayList<Map<String, Object>>();
		List<EFmFmRouteAreaMappingPO> allZoneAreas = iRouteDetailBO
				.getAllAreasFromZoneId(eFmFmClientRouteMappingPO.geteFmFmZoneMaster().getZoneId());
		for (EFmFmRouteAreaMappingPO routeAreaMappingPO : allZoneAreas) {
			Map<String, Object> areaName = new HashMap<String, Object>();
			areaName.put("areaName", routeAreaMappingPO.getEfmFmAreaMaster().getAreaName());
			areaName.put("areaId", routeAreaMappingPO.getEfmFmAreaMaster().getAreaId());
			allAreasData.add(areaName);
		}
		responce.put("status", "success");
		responce.put("areas", allAreasData);
		 log.info("serviceEnd -UserId :" + eFmFmClientRouteMappingPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/updatetravelrequestdata")
	public Response updateTravelRequestData(EFmFmEmployeeTravelRequestPO employeeRequest) throws ParseException {
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");		
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeRequest.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(employeeRequest.getUserId());
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
		   }
		 
		 log.info("serviceStart -UserId :" + employeeRequest.getUserId());
		log.info("Nodal Point Id" + employeeRequest.getNodalPointId());
		log.info("Area Id" + employeeRequest.getAreaId());

		EFmFmAreaMasterPO efmFmAreaMaster = new EFmFmAreaMasterPO();
		
		efmFmAreaMaster.setAreaId(employeeRequest.getAreaId());
		EFmFmZoneMasterPO eFmFmZoneMaster = new EFmFmZoneMasterPO();
		EFmFmAreaNodalMasterPO eFmFmNodalAreaMaster = new EFmFmAreaNodalMasterPO();
		eFmFmNodalAreaMaster.setNodalPointId(employeeRequest.getNodalPointId());
		eFmFmZoneMaster.setZoneId(employeeRequest.getZoneId());
		
		List<EFmFmEmployeeTravelRequestPO> employeeRequestDetails = iCabRequestBO
				.getparticularRequestDetail(employeeRequest);
		EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping = new EFmFmRouteAreaMappingPO();
		eFmFmRouteAreaMapping.seteFmFmZoneMaster(eFmFmZoneMaster);
		eFmFmRouteAreaMapping.setEfmFmAreaMaster(efmFmAreaMaster);
		eFmFmRouteAreaMapping.seteFmFmNodalAreaMaster(eFmFmNodalAreaMaster);
		DateFormat timeformate = new SimpleDateFormat("HH:mm");
		String shiftDate = employeeRequest.getTime();
		Date shift = timeformate.parse(shiftDate);
		java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
		log.info(employeeRequest.getCombinedFacility()+"employeeRequest.getUpdateRegularRequest().equalsIgnoreCase"
				+ employeeRequest.getUpdateRegularRequest());
		List<EFmFmRouteAreaMappingPO> routeAreaId = null;
		routeAreaId = iRouteDetailBO.getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(efmFmAreaMaster.getAreaId(),employeeRequest.getCombinedFacility(),
				eFmFmZoneMaster.getZoneId(), eFmFmNodalAreaMaster.getNodalPointId());
		if (routeAreaId.isEmpty()) {
			iRouteDetailBO.saveRouteMappingDetails(eFmFmRouteAreaMapping);
			routeAreaId = iRouteDetailBO.getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(
					efmFmAreaMaster.getAreaId(),employeeRequest.getCombinedFacility(),
					eFmFmZoneMaster.getZoneId(), eFmFmNodalAreaMaster.getNodalPointId());
		}
		if (employeeRequest.getUpdateRegularRequest().equalsIgnoreCase("N")) {
			if (employeeRequestDetails.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
				String pickUpTime = employeeRequest.getPickTime();
				Date changePickUpTime = timeformate.parse(pickUpTime);
				java.sql.Time pickTime = new java.sql.Time(changePickUpTime.getTime());
				employeeRequestDetails.get(0).setPickUpTime(pickTime);
			} else {
				String pickUpTime = employeeRequest.getPickTime();
				employeeRequestDetails.get(0).setDropSequence(Integer.parseInt(pickUpTime));
			}
			employeeRequestDetails.get(0).setShiftTime(shiftTime);
			employeeRequestDetails.get(0).seteFmFmRouteAreaMapping(routeAreaId.get(0));
			iCabRequestBO.update(employeeRequestDetails.get(0));
		}

		else {
			List<EFmFmEmployeeTravelRequestPO> employeeUpcomingRequests = iCabRequestBO
					.getAllUpComingRequestForParticularEmployee(
							employeeRequestDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
							employeeRequestDetails.get(0).getEfmFmUserMaster().getUserId(),
							employeeRequestDetails.get(0).getTripType());
			log.info("employeeUpcomingRequests" + employeeUpcomingRequests.size());
			if (!employeeUpcomingRequests.isEmpty()) {
				try {
					List<EFmFmEmployeeRequestMasterPO> masterRequestDetails = iCabRequestBO
							.getParticularEmployeeMasterRequestDetails(
									employeeRequestDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO()
											.getBranchId(),
									employeeUpcomingRequests.get(0).geteFmFmEmployeeRequestMaster().getTripId());
					if (!masterRequestDetails.isEmpty()) {
						if (masterRequestDetails.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
							String pickUpTime = employeeRequest.getPickTime();
							Date changePickUpTime = timeformate.parse(pickUpTime);
							java.sql.Time pickTime = new java.sql.Time(changePickUpTime.getTime());
							masterRequestDetails.get(0).setPickUpTime(pickTime);
						} else {
							masterRequestDetails.get(0)
									.setDropSequence(Integer.parseInt(employeeRequest.getPickTime()));
						}
						masterRequestDetails.get(0).seteFmFmRouteAreaMapping(routeAreaId.get(0));
						iCabRequestBO.update(masterRequestDetails.get(0));
					}
				} catch (Exception e) {
					log.info("Error updating master entry" + e);
				}
				DateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
				DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				String requestDate = dateformate.format(employeeRequestDetails.get(0).getRequestDate());
				String requestDateShiftTime = requestDate + " " + employeeRequestDetails.get(0).getShiftTime();
				Date shiftDateAndTime = dateTimeFormate.parse(requestDateShiftTime);
				for (EFmFmEmployeeTravelRequestPO travelDetail : employeeUpcomingRequests) {
					String currentRequestDate = dateformate.format(travelDetail.getRequestDate());
					String currentRequestDateShiftTime = currentRequestDate + " " + travelDetail.getShiftTime();
					Date currentshiftDateAndTime = dateTimeFormate.parse(currentRequestDateShiftTime);
					if (shiftDateAndTime.getTime() <= currentshiftDateAndTime.getTime()) {
						if (travelDetail.getTripType().equalsIgnoreCase("PICKUP")) {
							String pickUpTime = employeeRequest.getPickTime();
							Date changePickUpTime = timeformate.parse(pickUpTime);
							java.sql.Time pickTime = new java.sql.Time(changePickUpTime.getTime());
							travelDetail.setPickUpTime(pickTime);
						} else {
							String pickUpTime = employeeRequest.getPickTime();
							travelDetail.setDropSequence(Integer.parseInt(pickUpTime));
						}
						travelDetail.setShiftTime(shiftTime);
						travelDetail.seteFmFmRouteAreaMapping(routeAreaId.get(0));
						iCabRequestBO.update(travelDetail);
					}
				}
			}
		}
		EFmFmUserMasterPO eFmFmUserMaster = new EFmFmUserMasterPO();
		EFmFmClientBranchPO eFmFmClientBranchPO1 = new EFmFmClientBranchPO();
		eFmFmClientBranchPO1.setBranchId(employeeRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
		eFmFmUserMaster.seteFmFmClientBranchPO(eFmFmClientBranchPO1);
		eFmFmUserMaster.setUserId(employeeRequestDetails.get(0).getEfmFmUserMaster().getUserId());
		List<EFmFmUserMasterPO> userDetail = userMasterBO.getLoggedInUserDetailFromClientIdAndUserId(eFmFmUserMaster);
		userDetail.get(0).setWeekOffDays(employeeRequest.getWeekOffs());
		userMasterBO.update(userDetail.get(0));

		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + employeeRequest.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/devicedbcall")
	public Response latiLongiFromDeviceDataBase(EFmFmActualRoutTravelledPO actualRouteTravelledPO)
			throws IOException, ParseException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		 log.info("serviceStart -UserId :" + actualRouteTravelledPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),actualRouteTravelledPO.getUserId()))){
//
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}
//		 
//		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(actualRouteTravelledPO.getUserId());
//		   if (!(userDetailToken.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
//		     userDetailToken.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetailToken.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
		log.info("offline sevice call");
		String startTime = actualRouteTravelledPO.getTime();
		Date travelledDateTime = dateTimeFormate.parse(startTime);
		Map<String, Object> requests = new HashMap<String, Object>();
		EFmFmAssignRoutePO assignRoutePO = new EFmFmAssignRoutePO();
		EFmFmClientBranchPO clientBranch = new EFmFmClientBranchPO();
		clientBranch.setBranchId(actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId());
		assignRoutePO.setAssignRouteId(actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
		assignRoutePO.seteFmFmClientBranchPO(clientBranch);
		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO.getRouteLastEtaAndDistanceFromAssignRouteId(
				actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId(),
				actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
		if (!(actualRouteTravelled.isEmpty())) {
			CalculateDistance dist = new CalculateDistance();
			log.info("Distance" + actualRouteTravelledPO.getTravelledDistance());
			String etaCabLocation = dist.googleEtaCalculation(
					actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLivelatitudeLongitude(),
					actualRouteTravelledPO.getLatitudeLongitude());
			actualRouteTravelledPO.setTravelledDistance(actualRouteTravelledPO.getTravelledDistance());
			actualRouteTravelledPO.setCurrentCabLocation(etaCabLocation.split("-")[1]);
			actualRouteTravelledPO.setCurrentEta(etaCabLocation.split("-")[0]);
		}
		if (!(assignRoute.isEmpty())) {
			assignRoute.get(0)
					.setTravelledDistance(assignRoute.get(0).getTravelledDistance() + Double.valueOf(actualRouteTravelledPO.getTravelledDistance()));
			assignRouteBO.update(assignRoute.get(0));
		}	
		actualRouteTravelledPO.setEfmFmAssignRoute(assignRoutePO);
		actualRouteTravelledPO.seteFmFmClientBranchPO(clientBranch);
		actualRouteTravelledPO.setLatitudeLongitude(actualRouteTravelledPO.getLatitudeLongitude());
		actualRouteTravelledPO.setTravelledTime(travelledDateTime);
		assignRouteBO.save(actualRouteTravelledPO);
		requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + actualRouteTravelledPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/drivereta")
	public Response etaForDriver(EFmFmActualRoutTravelledPO actualRouteTravelledPO) throws IOException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		 log.info("serviceStart -UserId :" + actualRouteTravelledPO.getUserId());
	//	 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
	//	 Map<String, Object> responce = new HashMap<String, Object>();
		 		
//		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),actualRouteTravelledPO.getUserId()))){
//
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}
//		 
//		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(actualRouteTravelledPO.getUserId());
//		   if (!(userDetailToken.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
//		     userDetailToken.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetailToken.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
		 
		Map<String, Object> requests = new HashMap<String, Object>();
		List<EFmFmLiveRoutTravelledPO> actualRouteTravelled = assignRouteBO.getRouteLastEtaAndDistanceFromAssignRouteId(
				actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId(),
				actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId());
		if (!(actualRouteTravelled.isEmpty())) {
			requests.put("eta", actualRouteTravelled.get(actualRouteTravelled.size() - 1).getLiveEta());
		} else {
			requests.put("eta", "0");
		}
		requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + actualRouteTravelledPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * get all the driver read by the scheduler .
	 *
	 * @author Sarfraz Khan
	 * 
	 * @since 17 march 2015
	 * 
	 * @return EFmFmVehicleCheckInPO details.
	 * @throws ParseException
	 */

	@POST
	@Path("/unreadCheckInDrivers")
	public Response getAllUnReadAlerts(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) throws IOException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
//		 log.info("serviceStart -UserId :" + eFmFmVehicleCheckInPO.getUserId());
		List<EFmFmVehicleCheckInPO> unReadCheckInDrivers = iVehicleCheckInBO
				.getUnreadCheckedInDrivers(eFmFmVehicleCheckInPO.getBranchId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();		 		
//		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 
		 
		Map<String, Object> requests = new HashMap<String, Object>();
		List<Map<String, Object>> allUnreadDrivers = new ArrayList<Map<String, Object>>();
		if (!(unReadCheckInDrivers.isEmpty())) {
			requests.put("driverId", unReadCheckInDrivers.get(0).getEfmFmDriverMaster().getDriverId());
			requests.put("driverName", unReadCheckInDrivers.get(0).getEfmFmDriverMaster().getFirstName());
			requests.put("vehicleNumber", unReadCheckInDrivers.get(0).getEfmFmVehicleMaster().getVehicleNumber());
			requests.put("vendorName",
					unReadCheckInDrivers.get(0).getEfmFmDriverMaster().getEfmFmVendorMaster().getVendorName());
			requests.put("vendorId",
					unReadCheckInDrivers.get(0).getEfmFmDriverMaster().getEfmFmVendorMaster().getVendorId());
			allUnreadDrivers.add(requests);
			unReadCheckInDrivers.get(0).setReadFlg("R");
			iVehicleCheckInBO.update(unReadCheckInDrivers.get(0));
		}
//		 log.info("serviceEnd -UserId :" + eFmFmVehicleCheckInPO.getUserId());
		return Response.ok(allUnreadDrivers, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/tripTypes")
	public Response getAllUnReadAlerts(EFmFmClientBranchPO eFmFmClientBranchPO) throws IOException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
		Map<String, Object> request = new HashMap<String, Object>();
		Map<String, Object> requestType = new HashMap<String, Object>();
		Map<String, Object> bothReqType = new HashMap<String, Object>();
		
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientBranchPO.getUserId()))){
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}
		List<Map<String, Object>> tripTypes = new ArrayList<Map<String, Object>>();
		List<EFmFmClientBranchPO> clientDetails = userMasterBO.getClientDetails(eFmFmClientBranchPO.getCombinedFacility());
		if (!(clientDetails.isEmpty())) {
			 if(clientDetails.get(0).getTripType().equalsIgnoreCase("All")) {
				request.put("tripType", "PICKUP");
				requestType.put("tripType", "DROP");
				bothReqType.put("tripType", "BOTH");
				tripTypes.add(request);
				tripTypes.add(requestType);
				tripTypes.add(bothReqType);
			}else if (clientDetails.get(0).getTripType().equalsIgnoreCase("Both")) {
				request.put("tripType", "PICKUP");
				requestType.put("tripType", "DROP");	
				tripTypes.add(request);
				tripTypes.add(requestType);				
			} else {
				requestType.put("tripType", clientDetails.get(0).getTripType());
				tripTypes.add(requestType);
			}
		
		}
		 log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(tripTypes, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/pendingApprovalRequest")
	public Response pendingApprovalRequest(EFmFmClientBranchPO eFmFmClientBranchPO) throws IOException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
		List<Map<String, Object>> approvalRequest = new ArrayList<Map<String, Object>>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		/* try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientBranchPO.getUserId()))){

					responce.put("status", "invalidRequest");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}}catch(Exception e){
					log.info("authentication error"+e);
						responce.put("status", "invalidRequest");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();

				}*/
		List<EFmFmEmployeeTravelRequestPO> pendingApprovalRequest = iCabRequestBO.getListOfApprovalPendingRequest
				(eFmFmClientBranchPO.getCombinedFacility(), eFmFmClientBranchPO.getUserId(),"N");				
		if (!(pendingApprovalRequest.isEmpty())) {
			for (EFmFmEmployeeTravelRequestPO listOfRequest : pendingApprovalRequest) {
				Map<String, Object> request = new HashMap<String, Object>();
				request.put("employeeId", listOfRequest.getEfmFmUserMaster().getEmployeeId());
				request.put("employeeName", new String(Base64.getDecoder().decode(listOfRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
				request.put("requestId", listOfRequest.getRequestId());
				request.put("requestDate", dateFormatter.format(listOfRequest.getRequestDate()));
				request.put("shiftTime", shiftTimeFormater.format(listOfRequest.getShiftTime()));		
				request.put("tripType", listOfRequest.getTripType());			
				approvalRequest.add(request);
			}
		}
		 log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(approvalRequest, MediaType.APPLICATION_JSON).build();
	}
	
	
	@POST
	@Path("/approveAndRejectRequest")
	public Response approveAndRejectRequest(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) throws IOException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		 log.info("serviceStart -UserId :" + eFmFmEmployeeTravelRequestPO.getUserId());
		List<Map<String, Object>> approvalRequest = new ArrayList<Map<String, Object>>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		/* try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEmployeeTravelRequestPO.getUserId()))){

					responce.put("status", "invalidRequest");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}}catch(Exception e){
					log.info("authentication error"+e);
						responce.put("status", "invalidRequest");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();

				}*/
		 int[] requestIds = null;
		 try { 
			 
				if(!(eFmFmEmployeeTravelRequestPO.getMultipleRequestId().equalsIgnoreCase("0"))){
					try {	
						    if(eFmFmEmployeeTravelRequestPO.getMultipleRequestId().replace(",","").isEmpty()){
								 responce.put("status", "NOTVALIDREQUESTID");								 
								 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}else{								
								if (eFmFmEmployeeTravelRequestPO.getMultipleRequestId() != null 
										&& eFmFmEmployeeTravelRequestPO.getMultipleRequestId().length() > 0 
										&& eFmFmEmployeeTravelRequestPO.getMultipleRequestId().
										charAt(eFmFmEmployeeTravelRequestPO.getMultipleRequestId().length()-1)==',') {
									requestIds = Arrays.asList(eFmFmEmployeeTravelRequestPO.getMultipleRequestId().split(","))
						                      .stream()
						                      .map(String::trim)
						                      .mapToInt(Integer::parseInt).toArray();									
								}else{							    							
									requestIds = Arrays.asList(eFmFmEmployeeTravelRequestPO.getMultipleRequestId().split(","))
						                      .stream()
						                      .map(String::trim)
						                      .mapToInt(Integer::parseInt).toArray();
								}
							}
						
					} catch (Exception e) {
						log.debug("log"+e);
					}
				}else{					
					requestIds = new int[]{eFmFmEmployeeTravelRequestPO.getRequestId()};                   
				}
				
			    for(int requestId :requestIds){			    	
				eFmFmEmployeeTravelRequestPO.setRequestId(requestId);
				List<EFmFmEmployeeTravelRequestPO> pendingApprovalRequest = iCabRequestBO.getParticularRequestDetailOnTripComplete(eFmFmEmployeeTravelRequestPO.getRequestId());								
				if (!(pendingApprovalRequest.isEmpty())) {	
					if(eFmFmEmployeeTravelRequestPO.getReqApprovalStatus().equalsIgnoreCase("Y")){
						if(pendingApprovalRequest.get(0).getProjectId()>0){
							List<EFmFmClientProjectDetailsPO> projectDetails=iAssignRouteBO.
									getProjectDetails(pendingApprovalRequest.get(0).getProjectId());
							if(!(projectDetails.isEmpty())){
								eFmFmEmployeeTravelRequestPO.setProjectName(projectDetails.get(0).getClientProjectId());
							}
						}else{
							eFmFmEmployeeTravelRequestPO.setProjectName("NA");
						}
						
						pendingApprovalRequest.get(0).setReqApprovalStatus(eFmFmEmployeeTravelRequestPO.getReqApprovalStatus());
						pendingApprovalRequest.get(0).setStatusUpdatedTime(new Date());
						pendingApprovalRequest.get(0).setApproveStatus(eFmFmEmployeeTravelRequestPO.getReqApprovalStatus());
						  try {
								String toMailId = new String(
										Base64.getDecoder().decode(pendingApprovalRequest.get(0).getEfmFmUserMaster().getEmailId()),
										"utf-8");
								log.info("create request  AckMail Approval confirmation");
								Thread thread1 = new Thread(new Runnable() {
									@Override
									public void run() {
										SendMailBySite mailSender = new SendMailBySite();
										mailSender.approvalAndAcknowledement(toMailId,										
												shiftTimeFormater.format(pendingApprovalRequest.get(0).getShiftTime()),pendingApprovalRequest.get(0).getTripType(), 
												dateFormatter.format(pendingApprovalRequest.get(0).getRequestDate()),"AckMail", pendingApprovalRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId()
												,pendingApprovalRequest.get(0).getEfmFmUserMaster().getEmployeeId(),pendingApprovalRequest.get(0).getRequestId(),"",eFmFmEmployeeTravelRequestPO.getProjectName());													
									}
								});
								thread1.start();
							} catch (Exception e) {
								log.info("create request  mail Approval confirmation" + e);
							}
						  
						  try {
								/*String toMailId = new String(
										Base64.getDecoder().decode(pendingApprovalRequest.get(0).getEfmFmUserMaster().getEmailId()),
										"utf-8");*/
								String toMailId = pendingApprovalRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getTranportCommunicationMailId();
								log.info("create request  confirmationMail Approval confirmation");
								Thread thread2 = new Thread(new Runnable() {
									@Override
									public void run() {
										SendMailBySite mailSender = new SendMailBySite();
										mailSender.approvalAndAcknowledement(toMailId,										
												shiftTimeFormater.format(pendingApprovalRequest.get(0).getShiftTime()),pendingApprovalRequest.get(0).getTripType(), 
												dateFormatter.format(pendingApprovalRequest.get(0).getRequestDate()),"confirmationMail", pendingApprovalRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId()
												,pendingApprovalRequest.get(0).getEfmFmUserMaster().getEmployeeId(),pendingApprovalRequest.get(0).getRequestId(),"",eFmFmEmployeeTravelRequestPO.getProjectName());													
									}
								});
								thread2.start();
							} catch (Exception e) {
								log.info("create request  mail Approval confirmation mail to transport Team" + e);
							}				
					}else{
						pendingApprovalRequest.get(0).setApproveStatus(eFmFmEmployeeTravelRequestPO.getReqApprovalStatus());
						pendingApprovalRequest.get(0).setStatusUpdatedTime(new Date());						
						pendingApprovalRequest.get(0).setCompletionStatus("Rejected");
						pendingApprovalRequest.get(0).setReqApprovalStatus(eFmFmEmployeeTravelRequestPO.getReqApprovalStatus());
						pendingApprovalRequest.get(0).setRequestRemarks(eFmFmEmployeeTravelRequestPO.getRequestRemarks());
						try {
							String toMailId = new String(
									Base64.getDecoder().decode(pendingApprovalRequest.get(0).getEfmFmUserMaster().getEmailId()),
									"utf-8");
							log.info("create request  AckMail Approval confirmation");
							Thread thread1 = new Thread(new Runnable() {
								@Override
								public void run() {
									SendMailBySite mailSender = new SendMailBySite();
									mailSender.approvalAndAcknowledement(toMailId,										
											shiftTimeFormater.format(pendingApprovalRequest.get(0).getShiftTime()),pendingApprovalRequest.get(0).getTripType(), 
											dateFormatter.format(pendingApprovalRequest.get(0).getRequestDate()),"RejectAckMail", pendingApprovalRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId()
											,pendingApprovalRequest.get(0).getEfmFmUserMaster().getEmployeeId(),pendingApprovalRequest.get(0).getRequestId(),eFmFmEmployeeTravelRequestPO.getRequestRemarks(),eFmFmEmployeeTravelRequestPO.getProjectName());													
								}
							});
							thread1.start();
						} catch (Exception e) {
							log.info("create request  mail Approval confirmation" + e);
						}
					  
					  try {
							/*String toMailId = new String(
									Base64.getDecoder().decode(pendingApprovalRequest.get(0).getEfmFmUserMaster().getEmailId()),
									"utf-8");*/
							String toMailId = pendingApprovalRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getTranportCommunicationMailId();
							log.info("create request  confirmationMail Approval confirmation");
							Thread thread2 = new Thread(new Runnable() {
								@Override
								public void run() {
									SendMailBySite mailSender = new SendMailBySite();
									mailSender.approvalAndAcknowledement(toMailId,										
											shiftTimeFormater.format(pendingApprovalRequest.get(0).getShiftTime()),pendingApprovalRequest.get(0).getTripType(), 
											dateFormatter.format(pendingApprovalRequest.get(0).getRequestDate()),"RejectConfirmationMail", pendingApprovalRequest.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getFeedBackEmailId()
											,pendingApprovalRequest.get(0).getEfmFmUserMaster().getEmployeeId(),pendingApprovalRequest.get(0).getRequestId(),eFmFmEmployeeTravelRequestPO.getRequestRemarks(),eFmFmEmployeeTravelRequestPO.getProjectName());													
								}
							});
							thread2.start();
						} catch (Exception e) {
							log.info("create request  mail Approval confirmation mail to transport Team" + e);
						}				
					}
					iCabRequestBO.update(pendingApprovalRequest.get(0));						
				}
				}
				
			} catch (Exception e) {
				log.info("approval and Disable issue" + e);
			}	
		
		 log.info("serviceEnd -UserId :" + eFmFmEmployeeTravelRequestPO.getUserId());
		return Response.ok(approvalRequest, MediaType.APPLICATION_JSON).build();
	}
	
	

	@POST
	@Path("/shiftTimeByTripType")
	public Response getAllShiftTimeByTripType(EFmFmClientBranchPO eFmFmClientBranchPO) throws IOException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
		List<Map<String, Object>> shiftTimes = new ArrayList<Map<String, Object>>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));		 
		 List<EFmFmUserMasterPO> employeeDetails=userMasterBO.
				 getRegisterEmployeeDetailFromBranchIdAndUserId(eFmFmClientBranchPO.getBranchId(), eFmFmClientBranchPO.getUserId());
		 String gender="N";
		 if(!employeeDetails.isEmpty()){
			 if(new String(Base64.getDecoder().decode(employeeDetails.get(0).getGender()), "utf-8").equalsIgnoreCase("FEMALE")){
				 gender="F"; 
			 }else if(new String(Base64.getDecoder().decode(employeeDetails.get(0).getGender()), "utf-8").equalsIgnoreCase("MALE")){
				 gender="M";
			 }		 
		 }
		 List<EFmFmRoleMasterPO> userRole=userMasterBO.getUserRoleByUserId(eFmFmClientBranchPO.getUserId());
		 List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO.listOfShiftTimeByTripTypeForEmployees(
				eFmFmClientBranchPO.getBranchId(),eFmFmClientBranchPO.getTripType());
		if (!(shiftTimeDetails.isEmpty())) {
			for (EFmFmTripTimingMasterPO shiftiming : shiftTimeDetails) {
				Map<String, Object> request = new HashMap<String, Object>();
				request.put("shiftTime", shiftTimeFormater.format(shiftiming.getShiftTime()));
				request.put("shiftId", shiftiming.getShiftId());
				request.put("tripType", shiftiming.getTripType());
				request.put("bufferTime", shiftiming.getShiftBufferTime());
				request.put("cutOffTime", shiftTimeFormater.format(shiftiming.getCutOffTime()));
				request.put("rescheduleCutOffTime", shiftTimeFormater.format(shiftiming.getRescheduleCutOffTime()));
				request.put("genderPreference",shiftiming.getGenderPreference());
				//shiftTimes.add(request);
		/*	    if(shiftTimeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeGenderPreference().equalsIgnoreCase("Yes") 
			    		&& !(new String(Base64.getDecoder().decode(employeeDetails.get(0).getEmployeeDepartment()),"utf-8").equalsIgnoreCase("transportteam"))){
					if(shiftiming.getGenderPreference().equalsIgnoreCase(gender) || shiftiming.getGenderPreference().equalsIgnoreCase("B")){
						shiftTimes.add(request);
					}			
				}else{
					shiftTimes.add(request);
				}*/					
			    if(shiftTimeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeGenderPreference().equalsIgnoreCase("Yes") 
			    		&& !(new String(Base64.getDecoder().decode(employeeDetails.get(0).getEmployeeDepartment()),
			    				"utf-8").equalsIgnoreCase("transportteam")  
			    				|| userRole.get(0).getRole().equalsIgnoreCase("manager")
			    				|| userRole.get(0).getRole().equalsIgnoreCase("admin")
			    				|| userRole.get(0).getRole().equalsIgnoreCase("supervisor")) ){
					if(shiftiming.getGenderPreference().equalsIgnoreCase(gender) || shiftiming.getGenderPreference().equalsIgnoreCase("B")){
						shiftTimes.add(request);
					}			
				}else{
					shiftTimes.add(request);
				}		
			}
		}
		 log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(shiftTimes, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param Check
	 *            EmployeeId exist in eFmFmUserMaster or not from adhoc nodal
	 *            Request from device
	 * @return employee detail
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */

	@POST
	@Path("/empIdCheck")
	public Response employeeDetailsFromEmployeeId(EFmFmAssignRoutePO assignRoutePO)
			throws UnsupportedEncodingException, ParseException {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
//
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}
//		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
//		   if (!(userDetailToken.isEmpty())) {
//		    String jwtToken = "";
//		    try {
//		     JwtTokenGenerator token = new JwtTokenGenerator();
//		     jwtToken = token.generateToken();
//		     userDetailToken.get(0).setAuthorizationToken(jwtToken);
//		     userDetailToken.get(0).setTokenGenerationTime(new Date());
//		     userMasterBO.update(userDetailToken.get(0));
//		    } catch (Exception e) {
//		     log.info("error" + e);
//		    }
//		   }
		 log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		List<EFmFmUserMasterPO> employeeMasterData = employeeDetailBO.getEmpDetailsFromEmployeeIdAndBranchId(
				assignRoutePO.getEmployeeId(), assignRoutePO.getCombinedFacility());
		Map<String, Object> request = new HashMap<String, Object>();
		if (!(employeeMasterData.isEmpty()) && employeeMasterData.get(0).getStatus().equalsIgnoreCase("N")) {
			request.put("status", "disable");
			 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
			return Response.ok(request, MediaType.APPLICATION_JSON).build();
		}
		if (employeeMasterData.isEmpty()) {
			request.put("status", "fail");
			 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
			return Response.ok(request, MediaType.APPLICATION_JSON).build();
		}
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.closeParticularTrips(assignRoutePO);
		List<EFmFmEmployeeTripDetailPO> employeeTripDetail = iCabRequestBO
				.getParticularTripAllEmployees(assignRoute.get(0).getAssignRouteId());
		if (!(employeeTripDetail.isEmpty()) && employeeTripDetail
				.size() == (assignRoute.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity() - 1)) {
			request.put("status", "capacityFull");
			 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
			return Response.ok(request, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmEmployeeRequestMasterPO> employeeRequestMasterPickUp = iCabRequestBO
				.getParticularRequestDetailFromUserIdAndTripType(employeeMasterData.get(0).getUserId(),
						assignRoutePO.geteFmFmClientBranchPO().getBranchId(), assignRoute.get(0).getTripType());
		EFmFmUserMasterPO userMaster = new EFmFmUserMasterPO();
		userMaster.setUserId(employeeMasterData.get(0).getUserId());
		if (employeeRequestMasterPickUp.isEmpty()) {
			EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMasterPO = new EFmFmEmployeeRequestMasterPO();
			if (assignRoute.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
				eFmFmEmployeeRequestMasterPO.setPickUpTime(assignRoute.get(0).getShiftTime());
			}
			eFmFmEmployeeRequestMasterPO.setReadFlg("N");
			eFmFmEmployeeRequestMasterPO.setRequestDate(new Date());
			eFmFmEmployeeRequestMasterPO.setRequestFrom("E");
			eFmFmEmployeeRequestMasterPO.setRequestType("nodalAdhoc");
			eFmFmEmployeeRequestMasterPO.setShiftTime(assignRoute.get(0).getShiftTime());
			eFmFmEmployeeRequestMasterPO.setStatus("N");
			eFmFmEmployeeRequestMasterPO.setTripRequestStartDate(new Date());
			eFmFmEmployeeRequestMasterPO.setTripRequestEndDate(new Date());
			eFmFmEmployeeRequestMasterPO.setTripType(assignRoute.get(0).getTripType());
			eFmFmEmployeeRequestMasterPO.setEfmFmUserMaster(userMaster);
			if (assignRoute.get(0).getTripType().equalsIgnoreCase("DROP")) {
				eFmFmEmployeeRequestMasterPO.setDropSequence(1);
			}
			iCabRequestBO.save(eFmFmEmployeeRequestMasterPO);
			employeeRequestMasterPickUp = iCabRequestBO.getParticularRequestDetailFromUserIdAndTripType(
					employeeMasterData.get(0).getUserId(), assignRoutePO.geteFmFmClientBranchPO().getBranchId(),
					assignRoute.get(0).getTripType());
		}
		DateFormat dateHypenFormat = new SimpleDateFormat("dd-MM-yyyy");
		String requestDateFromexcel = dateHypenFormat.format(new Date());
		Date requestDate = dateHypenFormat.parse(requestDateFromexcel);

		List<EFmFmEmployeeTravelRequestPO> travelRequestDetails = iCabRequestBO
				.getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(requestDate, assignRoute.get(0).getShiftTime(),
						assignRoutePO.getCombinedFacility(), employeeMasterData.get(0).getUserId(),
						assignRoute.get(0).getTripType());

		if (travelRequestDetails.isEmpty()) {
			EFmFmEmployeeTravelRequestPO travelRequestPO = new EFmFmEmployeeTravelRequestPO();
			travelRequestPO.setApproveStatus("Y");
			travelRequestPO.setIsActive("Y");
			travelRequestPO.setReadFlg("R");
			travelRequestPO.setRequestDate(requestDate);
			travelRequestPO.setRequestStatus("Y");
			travelRequestPO.setRequestType("nodalAdhoc");
			travelRequestPO.setShiftTime(assignRoute.get(0).getShiftTime());
			travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMasterPickUp.get(0));
			travelRequestPO.setEfmFmUserMaster(userMaster);
			if (assignRoute.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
				travelRequestPO.setPickUpTime(assignRoute.get(0).getShiftTime());
			}
			travelRequestPO.seteFmFmRouteAreaMapping(assignRoute.get(0).geteFmFmRouteAreaMapping());
			travelRequestPO.setTripType(assignRoute.get(0).getTripType());
			travelRequestPO.setCompletionStatus("N");
			if (assignRoute.get(0).getTripType().equalsIgnoreCase("DROP")) {
				travelRequestPO.setDropSequence(1);
			}
			iCabRequestBO.save(travelRequestPO);
			travelRequestDetails = iCabRequestBO.getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(requestDate,
					assignRoute.get(0).getShiftTime(), assignRoutePO.getCombinedFacility(),
					employeeMasterData.get(0).getUserId(), assignRoute.get(0).getTripType());
		} else {
			request.put("status", "requestExist");
			 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
			return Response.ok(request, MediaType.APPLICATION_JSON).build();
		}
		EFmFmEmployeeTripDetailPO employeeTripDetailPO = new EFmFmEmployeeTripDetailPO();
		employeeTripDetailPO.setTenMinuteMessageStatus("Y");
		employeeTripDetailPO.setTwoMinuteMessageStatus("Y");
		employeeTripDetailPO.setReachedFlg("Y");
		employeeTripDetailPO.setCabDelayMsgStatus("Y");
		employeeTripDetailPO.setActualTime(new Date());
		employeeTripDetailPO.setPickedUpDateAndTime(new Date().getTime());
		employeeTripDetailPO.setGoogleEta(0);
		if (assignRoute.get(0).getTripType().equalsIgnoreCase("DROP")) {
			employeeTripDetailPO.setBoardedFlg("D");
		}
		if (assignRoute.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
			employeeTripDetailPO.setBoardedFlg("B");
		}
		employeeTripDetailPO.seteFmFmEmployeeTravelRequest(travelRequestDetails.get(0));
		employeeTripDetailPO.setEfmFmAssignRoute(assignRoute.get(0));
		employeeTripDetailPO.setCurrenETA("0");
		employeeTripDetailPO.setEmployeeStatus("completed");
		employeeTripDetailPO.setComingStatus("Yet to confirm");
		employeeTripDetailPO.setEmployeeOnboardStatus("NO");
		
		iCabRequestBO.update(employeeTripDetailPO);
		request.put("name", new String(Base64.getDecoder().decode(employeeMasterData.get(0).getFirstName()), "utf-8"));
		request.put("tripTime", shiftTimeFormater.format(assignRoute.get(0).getShiftTime()));
		request.put("address", new String(Base64.getDecoder().decode(employeeMasterData.get(0).getAddress()), "utf-8"));
		request.put("tripType", assignRoute.get(0).getTripType());
		request.put("boardedFlg", employeeTripDetailPO.getBoardedFlg());
		request.put("reachedFlg", employeeTripDetailPO.getReachedFlg());
		if (assignRoute.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
			request.put("pickUpTime", shiftTimeFormater.format(assignRoute.get(0).getShiftTime()));
		}
		request.put("status", "success");
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * public long getDisableTime(int hours, int minutes, Date checkIndate) {
	 * Calendar calendar = Calendar.getInstance();
	 * calendar.setTime(checkIndate); calendar.add(Calendar.HOUR, hours);
	 * calendar.add(Calendar.MINUTE, minutes); return
	 * calendar.getTimeInMillis(); }
	 */

	public long getDisableTime(Time hours, int minutes, Date checkIndate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkIndate);
		calendar.add(Calendar.HOUR, hours.getHours());
		calendar.add(Calendar.MINUTE, hours.getMinutes());
		return calendar.getTimeInMillis();
	}

	public void passingKey(String keyString) throws IOException {
		// Convert the key from 'web safe' base 64 to binary
		keyString = keyString.replace('-', '+');
		keyString = keyString.replace('_', '/');
		System.out.println("Key: " + keyString);
		// this.key = Base64.decode(keyString);
		CabRequestService.key = Base64.getDecoder().decode(keyString);
	}

	public String signRequest(String path, String query)
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
	
	@POST
	@Path("/updateTravelledLocation")
	public Response updateTravelledLocation(EFmFmMultipleLocationTvlReqPO eFmFmMultipleLocationTvlReqPO) throws IOException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");		
		Map<String, Object> response = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" + eFmFmMultipleLocationTvlReqPO.getUserId());
			/*    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
			try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken")))){
				response.put("status", "invalidRequest");
				return Response.ok(response, MediaType.APPLICATION_JSON).build();
			}}catch(Exception e){
				log.info("authenticationToken error"+e);
			}*/
	
		List<EFmFmMultipleLocationTvlReqPO> locationDetailsDetails = iCabRequestBO.
				getLocationTravelDetails(eFmFmMultipleLocationTvlReqPO.getMultipleReqId(),
						eFmFmMultipleLocationTvlReqPO.geteFmFmLocationMaster().geteFmFmClientBranchPO().getBranchId());			
				if(!(locationDetailsDetails.isEmpty())){					
					if(eFmFmMultipleLocationTvlReqPO.getActionFlg().equalsIgnoreCase("C")){
						locationDetailsDetails.get(0).setTravelledStatus("cancelled");
						locationDetailsDetails.get(0).setLocationStatus("N");
						iCabRequestBO.modify(locationDetailsDetails.get(0));
						response.put("status", "success");
					}else if(eFmFmMultipleLocationTvlReqPO.getActionFlg().equalsIgnoreCase("M")){
						locationDetailsDetails.get(0).setRequestUpdateDate(new Date());
						locationDetailsDetails.get(0).setLocationStatus("Y");
						EFmFmLocationMasterPO eFmFmLocationMaster=new EFmFmLocationMasterPO();
						eFmFmLocationMaster.setLocationId(eFmFmMultipleLocationTvlReqPO.geteFmFmLocationMaster().getLocationId());
						locationDetailsDetails.get(0).seteFmFmLocationMaster(eFmFmLocationMaster);
						iCabRequestBO.modify(locationDetailsDetails.get(0));
						response.put("status", "success");
					}else if(eFmFmMultipleLocationTvlReqPO.getActionFlg().equalsIgnoreCase("R")){
						locationDetailsDetails.get(0).setTravelledStatus("completed");
						locationDetailsDetails.get(0).setRequestUpdateDate(new Date());											
						iCabRequestBO.modify(locationDetailsDetails.get(0));
						response.put("status", "success");
					}else if(eFmFmMultipleLocationTvlReqPO.getActionFlg().equalsIgnoreCase("D")){
						locationDetailsDetails.get(0).setTravelledStatus("deleted");
						locationDetailsDetails.get(0).setRequestUpdateDate(new Date());											
						iCabRequestBO.modify(locationDetailsDetails.get(0));
						response.put("status", "success");
					}else{
						response.put("status", "NOTVALIDFLG");
					}					
				}else{
					response.put("status", "NORECORD");
				}
				
			 log.info("serviceEnd -UserId :" + eFmFmMultipleLocationTvlReqPO.getUserId());
			 return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}	
	public Map<String,Object> multilocationlistOfWayPointsAfterAllocated(int requestId,int userId,String combinedFacility){		
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");	
			Map<String,Object> areaList=new HashMap<String, Object>();
			List<EFmFmMultipleLocationTvlReqPO> locationDetailsDetails = iCabRequestBO.getLocationTravelDetailsByRequestId(requestId);			
			if(!(locationDetailsDetails.isEmpty())){
				List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
				StringBuffer waypointsList=new StringBuffer();				
				for(EFmFmMultipleLocationTvlReqPO wayPoints:locationDetailsDetails){
					Map<String, Object> requestList = new HashMap<String, Object>();
					requestList.put("LocationId", wayPoints.geteFmFmLocationMaster().getLocationId());
					requestList.put("LocationName", wayPoints.geteFmFmLocationMaster().getLocationName());
					requestList.put("LatiLng", wayPoints.geteFmFmLocationMaster().getLocationLatLng());					
					if(wayPoints.geteFmFmLocationMaster().getLocationName().replaceAll(" ","").toLowerCase().equalsIgnoreCase("homelocation")){
	                	List<EFmFmUserMasterPO> userDetails =iEmployeeDetailBO.getParticularEmpDetailsFromUserIdWithOutStatus(userId,combinedFacility);                 
	                	requestList.put("LatiLng", userDetails.get(0).getLatitudeLongitude());	 
	                	requestList.put("travelledStatus",wayPoints.getTravelledStatus());
						travelRequestList.add(requestList);
						waypointsList.append(userDetails.get(0).getLatitudeLongitude()+"|");
	                }else{				
	                	requestList.put("travelledStatus",wayPoints.getTravelledStatus());
						travelRequestList.add(requestList);
						waypointsList.append(wayPoints.geteFmFmLocationMaster().getLocationLatLng()+"|");
	                }					
				}				
				areaList.put("wayPoints", travelRequestList);
				areaList.put("wayPointsList", waypointsList.toString());
			}
			return areaList;
	}
	
	
	public Map<String,Object> listOfPointsForMultipleLocation(String multipleWayPointsId,int branchId,int userId,String combinedFacility){
		IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");	
		
			Map<String,Object> areaList=new HashMap<String, Object>();
			if(!(multipleWayPointsId.replace(",","").isEmpty())){
				String[] locationArray=multipleWayPointsId.split(",");
				List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
				StringBuffer waypointsList=new StringBuffer();
				for(String locationId:locationArray){
					List<EFmFmLocationMasterPO> wayPoints=iEmployeeDetailBO.getMultipleLocation(locationId,combinedFacility);
					if(!(wayPoints.isEmpty())){	
						Map<String, Object> requestList = new HashMap<String, Object>();
						requestList.put("LocationId", wayPoints.get(0).getLocationId());
						requestList.put("LocationName", wayPoints.get(0).getLocationName());
						requestList.put("LocationAddress",wayPoints.get(0).getLocationAddress());
						requestList.put("LatiLng", wayPoints.get(0).getLocationLatLng());						
						 if(wayPoints.get(0).getLocationName().replaceAll(" ","").toLowerCase().equalsIgnoreCase("homelocation")){
			                	List<EFmFmUserMasterPO> userDetails =iEmployeeDetailBO.getParticularEmpDetailsFromUserIdWithOutStatus(userId, combinedFacility);                 
			                	requestList.put("LatiLng", userDetails.get(0).getLatitudeLongitude());	 
			                	travelRequestList.add(requestList);
								waypointsList.append(userDetails.get(0).getLatitudeLongitude()+"|");
			                }else{				
								travelRequestList.add(requestList);
								waypointsList.append(wayPoints.get(0).getLocationLatLng()+"|");
			                }
					}					
				}	
				areaList.put("wayPoints", travelRequestList);
				areaList.put("wayPointsList", waypointsList.toString());
			}
			return areaList;
	}
	
	@POST
	@Path("/updateLocationWayPoints")
	public Response updateLocationWayPoints(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException, UnsupportedEncodingException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());	
		Map<String, Object> responce = new HashMap<String, Object>();
		/*    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
			try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken")))){
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}}catch(Exception e){
				log.info("authenticationToken error"+e);
			}*/
		List<EFmFmEmployeeTravelRequestPO> travelDetails = iCabRequestBO
					.getParticularRequestDetailOnTripComplete(employeeTravelRequestPO.getRequestId());
		if (!(travelDetails.isEmpty())) {
			if(!(employeeTravelRequestPO.getLocationWaypointsIds().replace(",","").isEmpty() && employeeTravelRequestPO.getLocationWaypointsIds().isEmpty() && employeeTravelRequestPO.getLocationWaypointsIds()=="")){
				travelDetails.get(0).setLocationWaypointsIds(employeeTravelRequestPO.getLocationWaypointsIds());
				iCabRequestBO.update(travelDetails.get(0));	
				responce.put("status","success");
			}else{
				responce.put("status","failed");
			}

		}	
		log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/employeeMultipleTravelRequest")
	public Response employeeMultipleTravelRequest(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException, UnsupportedEncodingException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 log.info("serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
		List<Map<String, Object>> shitTimings = new ArrayList<Map<String, Object>>();
		Map<String, Object> responce = new HashMap<String, Object>();
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat shiftDateFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		DateFormat shiftTimeFormaters = new SimpleDateFormat("HH:mm:ss");
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		employeeTravelRequestPO.setRequestDate(new Date());
		String requestDate = requestDateFormat.format(new Date());
		Date date=null;
		if(null !=employeeTravelRequestPO.getResheduleDate() && employeeTravelRequestPO.getResheduleDate()!=""){
			date = formatter.parse(employeeTravelRequestPO.getResheduleDate());
			requestDate = requestDateFormat.format(date);	
		}	
  	/*    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
		if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken")))){
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authenticationToken error"+e);
		}*/
		
		
		List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO.listOfShiftTime(new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getUserId(),employeeTravelRequestPO.getCombinedFacility()));
		if (!(shiftTimeDetails.isEmpty())) {
			for (EFmFmTripTimingMasterPO shiftiming : shiftTimeDetails) {
				Map<String, Object> requestList = new HashMap<String, Object>();
				requestList.put("shiftTime", shiftTimeFormater.format(shiftiming.getShiftTime()));
				shitTimings.add(requestList);
			}
		}
		List<EFmFmEmployeeTravelRequestPO> travelDetails=null;
	
		if(employeeTravelRequestPO.getTypeExecution().equalsIgnoreCase("search") && !(employeeTravelRequestPO.getTime().equalsIgnoreCase("All"))){			
			String shiftDate = employeeTravelRequestPO.getTime();
			Date shift = shiftFormate.parse(shiftDate);
			java.sql.Time shiftTime = new java.sql.Time(shift.getTime());			
			travelDetails = iCabRequestBO
					.getRewquestByShiftWiceForLocationFlg(
							new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getUserId(),employeeTravelRequestPO.getCombinedFacility()),
							requestDate, shiftTime, employeeTravelRequestPO.getTripType(),employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
			log.info("requestDate" + requestDate + "shiftTime" + shiftTime + "Request Size" + travelDetails.size());
		}else{
				
			travelDetails = iCabRequestBO
					.getEmpMultipleTravelledRequest(new MultifacilityService().combinedBranchIdDetails(employeeTravelRequestPO.getUserId(),employeeTravelRequestPO.getCombinedFacility()),
							requestDate,employeeTravelRequestPO.getStartPgNo(),employeeTravelRequestPO.getEndPgNo());
		}
		
		
		List<Map<String, Object>> travelRequestList = new ArrayList<Map<String, Object>>();
		if (!(travelDetails.isEmpty())) {
			for (EFmFmEmployeeTravelRequestPO allTravelRequest : travelDetails) {
				Map<String, Object> requestList = new HashMap<String, Object>();
				requestList.put("employeeId", allTravelRequest.getEfmFmUserMaster().getEmployeeId());
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
				requestList.put("tripDate", formatter.format(allTravelRequest.getRequestDate()));
				requestList.put("weekOffs", allTravelRequest.getEfmFmUserMaster().getWeekOffDays());
				requestList.put("facilityName", allTravelRequest.geteFmFmClientBranchPO().getBranchName());
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
				requestList.put("employeeAreaId",
						allTravelRequest.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaId());
				requestList.put("employeePickUpTime", allTravelRequest.getPickUpTime());
				requestList.put("employeeName", new String(
						Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getFirstName()), "utf-8"));
				log.info("EmployeeId" + allTravelRequest.getEfmFmUserMaster().getEmployeeId());
				requestList.put("employeeAddress", new String(
						Base64.getDecoder().decode(allTravelRequest.getEfmFmUserMaster().getAddress()), "utf-8"));	
				try {
					if (allTravelRequest.getLocationFlg().equalsIgnoreCase("M")) {
						Map<String, Object> areaList = listOfPointsForMultipleLocation(
								allTravelRequest.getLocationWaypointsIds(),
								employeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
										.geteFmFmClientBranchPO().getBranchId(),allTravelRequest.getEfmFmUserMaster().getUserId(),employeeTravelRequestPO.getCombinedFacility());
						if (areaList.size() > 0) {
							requestList.put("employeeWaypoints", areaList);
						} else {
							requestList.put("employeeWaypoints", "NA");
						}
					}
				} catch (Exception e) {
					log.debug("location Details are not updated_Location_flg is null");
				}
				
				travelRequestList.add(requestList);
			}

		}
		responce.put("shifts", shitTimings);
		responce.put("requests", travelRequestList);
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	public void travelRequestCreation(int tripId)
			throws ParseException, UnsupportedEncodingException {				
			ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");	
			SimpleDateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
			List<EFmFmEmployeeRequestMasterPO>  requestDetails=iCabRequestBO.getBulkRequestByTripId(tripId);			
			LocalDate start = requestDetails.get(0).getTripRequestStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate end = requestDetails.get(0).getTripRequestEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();			
			for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
			    // Do your job here with `date`.
			    System.out.println("Date==="+date);			    
			    EFmFmEmployeeTravelRequestPO travelRequestPO = new EFmFmEmployeeTravelRequestPO();
				EFmFmUserMasterPO userMaster = new EFmFmUserMasterPO();
				EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping = new EFmFmRouteAreaMappingPO();
				eFmFmRouteAreaMapping.setRouteAreaId(requestDetails.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
				userMaster.setUserId(requestDetails.get(0).getEfmFmUserMaster().getUserId());
				travelRequestPO.setEfmFmUserMaster(userMaster);
				travelRequestPO.seteFmFmRouteAreaMapping(eFmFmRouteAreaMapping);			
				travelRequestPO.setCompletionStatus("N");
				if (requestDetails.get(0).getTripType().equalsIgnoreCase("DROP")) {
					travelRequestPO.setDropSequence(requestDetails.get(0).getDropSequence());
				}
				travelRequestPO.seteFmFmEmployeeRequestMaster(requestDetails.get(0));
				EFmFmClientMasterPO eFmFmClientMasterPO = new EFmFmClientMasterPO();
				eFmFmClientMasterPO.setClientId(requestDetails.get(0).geteFmFmClientBranchPO().getBranchId());
				travelRequestPO.setRequestType(requestDetails.get(0).getRequestType());
				travelRequestPO.setShiftTime(requestDetails.get(0).getShiftTime());
				EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
				eFmFmClientBranchPO.setBranchId(requestDetails.get(0).geteFmFmClientBranchPO().getBranchId());
				Date requestDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
				travelRequestPO.setRequestDate(requestDate);			
				EFmFmEmployeeRequestMasterPO employeeRequestMaster = new EFmFmEmployeeRequestMasterPO();
				employeeRequestMaster.setTripId(requestDetails.get(0).getTripId());
				travelRequestPO.setShiftTime(requestDetails.get(0).getShiftTime());
				travelRequestPO.setPickUpTime(requestDetails.get(0).getPickUpTime());
				travelRequestPO.setApproveStatus("Y");
				travelRequestPO.setRequestType(requestDetails.get(0).getRequestType());
				travelRequestPO.setTripType(requestDetails.get(0).getTripType());
				travelRequestPO.setRequestStatus(requestDetails.get(0).getRequestFrom());								
				travelRequestPO.setLocationFlg(requestDetails.get(0).getLocationFlg());
				travelRequestPO.setLocationWaypointsIds(requestDetails.get(0).getLocationWaypointsIds());								
				travelRequestPO.setReadFlg("Y");
				travelRequestPO.setTripSheetStatus("NOCHANGES");
				travelRequestPO.setRoutingAreaCreation("Y");						        
				travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);					        
				travelRequestPO.setIsActive("Y");								
				travelRequestPO.setProjectId(requestDetails.get(0).getProjectId());								
				travelRequestPO.setReportingManagerUserId(requestDetails.get(0).getReportingManagerUserId());
				travelRequestPO.setReqApprovalStatus(requestDetails.get(0).getReqApprovalStatus());								
				travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMaster);	
				
				if(requestDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getApprovalProcess().equalsIgnoreCase("Yes")){
					List<EFmFmEmployeeTravelRequestPO> approvalCount = iCabRequestBO
							.getListOfApprovalPendingRequestForUser(String.valueOf(requestDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId()),
									requestDetails.get(0).getEfmFmUserMaster().getUserId(),"N");					
					 if((requestDetails.get(0).getReqApprovalStatus().equalsIgnoreCase("N") 
							 && requestDetails.get(0).getRequestType().equalsIgnoreCase("guest")) ||  requestDetails.get(0).getTimeFlg().equalsIgnoreCase("A")){	
						    travelRequestPO.setApproveStatus("N");	
							travelRequestPO.setReqApprovalStatus("N");
							iCabRequestBO.save(travelRequestPO);
					 }else if(requestDetails.get(0).getReqApprovalStatus().equalsIgnoreCase("Y")){												
						 travelRequestPO.setReqApprovalStatus("Y");
						 iCabRequestBO.save(travelRequestPO);
					}else if(!(requestDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size())){
						
						iCabRequestBO.save(travelRequestPO);
					}else if(requestDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getPostApproval() <= approvalCount.size()){									
						if(approvalCount.contains(dateformate.format(requestDetails.get(0).getRequestDate()))){
							travelRequestPO.setApproveStatus("Y");	
							travelRequestPO.setReqApprovalStatus("N");
							iCabRequestBO.save(travelRequestPO);
						}else{
							travelRequestPO.setApproveStatus("N");	
							travelRequestPO.setReqApprovalStatus("N");
							iCabRequestBO.save(travelRequestPO);
						}									
					}else{
						travelRequestPO.setApproveStatus("N");	
						travelRequestPO.setReqApprovalStatus("N");
						iCabRequestBO.save(travelRequestPO);
					}							
				}else{
					iCabRequestBO.save(travelRequestPO);
				}
				
							    
			}	
			
			requestDetails.get(0).setStatus("N");
			requestDetails.get(0).setReadFlg("N");
			iCabRequestBO.update(requestDetails.get(0));
			
	}
	
   public String multipleDestPlannedDistance(int assignRouteId,String origin,String destination){		
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		List<EFmFmEmployeeTripDetailPO>  tripDetails=iCabRequestBO.getlistOfRequestByAssignedId(assignRouteId);
		String plannedDistance="0";
		if(!(tripDetails.isEmpty())){			
				List<EFmFmMultipleLocationTvlReqPO>  locationDetails=iCabRequestBO.
						getLocationTravelDetailsByRequestId(tripDetails.get(0).geteFmFmEmployeeTravelRequest().getRequestId());
				StringBuffer waypoints = new StringBuffer();
				String destinationPoints="0";
				for(EFmFmMultipleLocationTvlReqPO locationList:locationDetails){
					waypoints.append(locationList.geteFmFmLocationMaster().getLocationLatLng()+"|");					
					destinationPoints=locationList.geteFmFmLocationMaster().getLocationLatLng().trim();
				}				
				if(destination.equalsIgnoreCase("N")){
					destination=destinationPoints;
				}				
				plannedDistance=new CalculateDistance().getPlannedDistanceByMapApi(origin,destination,waypoints.toString());				
			
		}		
		return plannedDistance;
	}
   
   
   
	
	
	
	
	
	
	
}