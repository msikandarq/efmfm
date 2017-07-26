package com.newtglobal.eFmFmFleet.services;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/approval")
@Consumes("application/json")
@Produces("application/json")
public class ApprovalService {
    private static Log log = LogFactory.getLog(ApprovalService.class);

    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();


    /*
     * starting driver approval services for pending,register and unregister
     * drivers
     */
    @POST
    @Path("/unapproveddriver")
    public Response allUnapprovedDrivers(EFmFmDriverMasterPO driverMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),driverMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(driverMasterPO.getUserId());
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
    	
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        log.info("serviceStart -UserId : " + driverMasterPO.getUserId());
        List<EFmFmDriverMasterPO> unapprovedDrivers = approvalBO
                .getAllUnapprovedDrivers(driverMasterPO.getCombinedFacility());
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();        
        String command = ContextLoader.getContext().getMessage("user.firstName", null, "firstName", null);
        log.info("command" + command);
        if (unapprovedDrivers != null) {
            for (EFmFmDriverMasterPO pending : unapprovedDrivers) {
                Map<String, Object> allPendingRequests = new HashMap<String, Object>();
                allPendingRequests.put("mobileNumber", pending.getMobileNumber());
                allPendingRequests.put("vendorName", pending.getEfmFmVendorMaster().getVendorName());
                allPendingRequests.put("name", pending.getFirstName());
                allPendingRequests.put("driverId", pending.getDriverId());
                allPendingRequests.put("facilityName", pending.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
                requests.add(allPendingRequests);
            }
        }
        log.info("serviceEnd -UserId :  : " + driverMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/approveddriver")
    public Response allApprovedDrivers(EFmFmDriverMasterPO driverMasterPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),driverMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(driverMasterPO.getUserId());
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
		
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        log.info("serviceStart -UserId : " + driverMasterPO.getUserId());
        List<EFmFmDriverMasterPO> approvedDrivers = approvalBO
                .getAllApprovedDriversWithOutDummy(new MultifacilityService()
        				.combinedBranchIdDetails(driverMasterPO.getUserId(), driverMasterPO.getCombinedFacility()));
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();        
        if (approvedDrivers != null) {
            for (EFmFmDriverMasterPO pending : approvedDrivers) {
                Map<String, Object> allPendingRequests = new HashMap<String, Object>();
                allPendingRequests.put("mobileNumber", pending.getMobileNumber());
                allPendingRequests.put("vendorName", pending.getEfmFmVendorMaster().getVendorName());
                allPendingRequests.put("facilityName", pending.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

                allPendingRequests.put("name", pending.getFirstName());
                try{
                    allPendingRequests.put("joinDate", formatter.format(pending.getDateOfJoining()));
                    }catch(Exception e){
                        allPendingRequests.put("joinDate", "Not Given");
                    	log.info("joinDate Error"+e);
                    }
                
                allPendingRequests.put("driverId", pending.getDriverId());
                requests.add(allPendingRequests);
            }
        }
        log.info("serviceEnd -UserId :  : " + driverMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/inactivedriver")
    public Response allInActiveDrivers(EFmFmDriverMasterPO driverMasterPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),driverMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(driverMasterPO.getUserId());
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
    	
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        log.info("serviceStart -UserId : " + driverMasterPO.getUserId());
        List<EFmFmDriverMasterPO> inactiveDrivers = approvalBO
                .getAllInActiveDrivers(driverMasterPO.getCombinedFacility());
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
        if (inactiveDrivers != null) {
            for (EFmFmDriverMasterPO pending : inactiveDrivers) {
                Map<String, Object> allPendingRequests = new HashMap<String, Object>();
                allPendingRequests.put("mobileNumber", pending.getMobileNumber());
                allPendingRequests.put("vendorName", pending.getEfmFmVendorMaster().getVendorName());
                allPendingRequests.put("name", pending.getFirstName());
                allPendingRequests.put("facilityName", pending.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
                allPendingRequests.put("remarks", pending.getRemarks()); 
                try{
                	if(pending.getDateOfJoining() == null){
                		allPendingRequests.put("joinDate","");
                	}else{
                		allPendingRequests.put("joinDate", formatter.format(pending.getDateOfJoining()));
                	}                
                    }catch(Exception e){
                        allPendingRequests.put("joinDate", "Not Given");
                    	log.info("joinDate Error"+e);
                    }
                try{
                	if(pending.getDateOfRelease()==null){
                		allPendingRequests.put("relievingDate","");
                	}else{                	
                		allPendingRequests.put("relievingDate", formatter.format(pending.getDateOfRelease()));
                	}
                }catch(Exception e){
                    allPendingRequests.put("relievingDate", "Not Given");
                	log.info("relievingDate Error"+e);
                }
                allPendingRequests.put("driverId", pending.getDriverId());
                requests.add(allPendingRequests);
            }
        }
        log.info("serviceEnd -UserId  : " + driverMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    /*
     * Donwload the excel sheet
     */
    
    @POST
    @Path("/inactivedriverDownload")
    public Response allInActiveDriversDwn(EFmFmDriverMasterPO driverMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),driverMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(driverMasterPO.getUserId());
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
    	
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        log.info("serviceStart -UserId  : " + driverMasterPO.getUserId());
        List<EFmFmDriverMasterPO> inactiveDrivers = approvalBO
                .getAllInActiveDrivers(driverMasterPO.getCombinedFacility());
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();  
        XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Route Details");
		XSSFCellStyle style = workbook.createCellStyle();	
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		font.setColor(new XSSFColor(new java.awt.Color(255,255,255)));//color
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0,82,128)));
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(font);     
		int rownum = 0;
		Row OutSiderow = sheet.createRow(rownum++); 
		for (int columnIndex = 0; columnIndex < 15; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
		}
				
				
				Cell zerothCol = OutSiderow.createCell(0);
				zerothCol.setCellValue("Vendor Name");
				zerothCol.setCellStyle(style);
				
				Cell firstCol = OutSiderow.createCell(1);
				firstCol.setCellValue("DriverId");
				firstCol.setCellStyle(style);
				
				Cell secondCol = OutSiderow.createCell(2);
				secondCol.setCellValue("Driver Name");
				secondCol.setCellStyle(style);
				
				Cell thirdCol = OutSiderow.createCell(3);
				thirdCol.setCellValue("Mobile Number");
				thirdCol.setCellStyle(style);
				
				Cell fourthCol = OutSiderow.createCell(4);
				fourthCol.setCellValue("Remarks");
				fourthCol.setCellStyle(style);
				
        if (inactiveDrivers != null) {
            for (EFmFmDriverMasterPO pending : inactiveDrivers) {
            	
                Map<String, Object> allPendingRequests = new HashMap<String, Object>();
                allPendingRequests.put("mobileNumber", pending.getMobileNumber());
                allPendingRequests.put("vendorName", pending.getEfmFmVendorMaster().getVendorName());
                allPendingRequests.put("name", pending.getFirstName());
                allPendingRequests.put("remarks", pending.getRemarks());                
                allPendingRequests.put("driverId", pending.getDriverId());
                requests.add(allPendingRequests);
                OutSiderow = sheet.createRow(rownum++);
				Cell vendorName = OutSiderow.createCell(0);
				vendorName.setCellValue(pending.getEfmFmVendorMaster().getVendorName());
				Cell driverId = OutSiderow.createCell(1);
				driverId.setCellValue(pending.getDriverId());
				Cell name = OutSiderow.createCell(2);
				name.setCellValue(pending.getFirstName());
				Cell mobileNumber = OutSiderow.createCell(3);
				mobileNumber.setCellValue(pending.getMobileNumber());
				Cell remarks = OutSiderow.createCell(4);
				remarks.setCellValue(pending.getRemarks());
            }
        }
		StreamingOutput streamOutput = new StreamingOutput() {
			@Override
			public void write(OutputStream out) throws IOException, WebApplicationException {
				workbook.write(out);
			}			
		};

		 log.info("serviceEnd -UserId :  : " + driverMasterPO.getUserId());
		ResponseBuilder response = Response.ok(streamOutput,
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.header("content-disposition", "attachment; filename=\"Report-" + 1 + "\".xlsx");
		return response.build();   
        //return Response.ok("SUCCEES", MediaType.APPLICATION_JSON).build();
}
    
    @POST
    @Path("/approvedriver")
    public Response approveParticularDriver(EFmFmDriverMasterPO driverMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),driverMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(driverMasterPO.getUserId());
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
    	
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        log.info("serviceStart -UserId :  : " + driverMasterPO.getUserId());
        EFmFmDriverMasterPO approveDriver = approvalBO.getParticularDriverDetail(driverMasterPO.getDriverId());
        if (!(approveDriver.getEfmFmVendorMaster().getStatus().equalsIgnoreCase("A"))) {
            return Response.ok("notapproved", MediaType.APPLICATION_JSON).build();
        }
        approveDriver.setStatus("A");
        approvalBO.update(approveDriver);
      	 log.info("serviceEnd -UserId  : " + driverMasterPO.getUserId());
        return Response.ok("success", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/removedriver")
    public Response rejectParticularDriver(EFmFmDriverMasterPO driverMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),driverMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(driverMasterPO.getUserId());
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
		

        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        log.info("serviceStart -UserId :  : " + driverMasterPO.getUserId());
        List<EFmFmVehicleCheckInPO> checkInDetails = iVehicleCheckInBO.getParticularDriverCheckedInTableEntry(driverMasterPO.getCombinedFacility(),
                driverMasterPO.getDriverId()); 	 
        if (checkInDetails.isEmpty()) {        	
	        		List<EFmFmDriverDocsPO> driverDetails=iVehicleCheckInBO.getAlluploadFileDetails(driverMasterPO.getDriverId());
	        		if(!(driverDetails.isEmpty())){
		        		for (EFmFmDriverDocsPO driverList:driverDetails){
		        			iVehicleCheckInBO.deleteDriverUploadDetails(driverList.getDriverDocId());
		        		}
	        		}
            	approvalBO.deleteParticularDriver(driverMasterPO.getDriverId());
        } else {
            EFmFmDriverMasterPO approveDriver = approvalBO.getParticularDriverDetail(driverMasterPO.getDriverId());
            approveDriver.setStatus("R");
            approvalBO.update(approveDriver);
        }
        log.info("serviceEnd -UserId   : " + driverMasterPO.getUserId());
        return Response.ok("success", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/rejectdriver")
    public Response removeParticularDriver(EFmFmDriverMasterPO driverMasterPO) throws ParseException {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),driverMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(driverMasterPO.getUserId());
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
    	
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
   	 	log.info("serviceStart -UserId :  : " + driverMasterPO.getUserId());
        EFmFmDriverMasterPO approveDriver = approvalBO.getParticularDriverDetail(driverMasterPO.getDriverId());  
        String relievingDate = driverMasterPO.getRelievingDate();      
        approveDriver.setDateOfRelease(formatter.parse(relievingDate));
        approveDriver.setRemarks(driverMasterPO.getRemarks());
        approveDriver.setStatus("R");
        approvalBO.update(approveDriver);
        try {
            Thread thread1 = new Thread(new Runnable() {
                @Override
				public void run() {
                    SendMailBySite mailSender=new SendMailBySite();
                    String toMailId = ContextLoader.getContext().getMessage("user.toMailSender", null, "toMailSender", null);
                    mailSender.mailForDriverRemoval(toMailId, "team", approveDriver.getFirstName());
                	}
            });
            thread1.start();
        } catch (Exception e) {
            log.info("driver removal from approval" + e);
        } 
    	 log.info("serviceEnd -UserId   : " + driverMasterPO.getUserId());
        return Response.ok("success", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/addagaindriver")
    public Response addAgainParticularDriver(EFmFmDriverMasterPO driverMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),driverMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(driverMasterPO.getUserId());
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
		
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
   	 	log.info("serviceStart -UserId   : " + driverMasterPO.getUserId());  
        EFmFmDriverMasterPO approveDriver = approvalBO.getParticularDriverDetail(driverMasterPO.getDriverId());
        approveDriver.setStatus("P");
        approvalBO.update(approveDriver);
    	 log.info("serviceEnd -UserId  : " + driverMasterPO.getUserId());
        return Response.ok("success", MediaType.APPLICATION_JSON).build();
    }

    /*
     * starting Particular Deriver Detail service for
     */

    @POST
    @Path("/driverDetail")
    public Response getParticularDriverDetail(EFmFmDriverMasterPO driverMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),driverMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(driverMasterPO.getUserId());
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
		
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
  	 	log.info("serviceStart -UserId  : " + driverMasterPO.getUserId());
        EFmFmDriverMasterPO driverDetails = approvalBO.getParticularDriverDetail(driverMasterPO.getDriverId()); 
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        List<Map<String, Object>> docs = new ArrayList<Map<String, Object>>();
        Map<String, Object> medicalDoc = new HashMap<String, Object>();
        Map<String, Object> licenceDoc = new HashMap<String, Object>();
        Map<String, Object> profilePicDocs = new HashMap<String, Object>();
        
        if (driverDetails != null) {
            Map<String, Object> allPendingRequests = new HashMap<String, Object>();
            allPendingRequests.put("mobileNumber", driverDetails.getMobileNumber());
            allPendingRequests.put("name", driverDetails.getFirstName());
            allPendingRequests.put("vendorName", driverDetails.getEfmFmVendorMaster().getVendorName());
            allPendingRequests.put("dob", formatter.format(driverDetails.getDob()));
            allPendingRequests.put("driverId", driverDetails.getDriverId());
            allPendingRequests.put("address", driverDetails.getAddress());
            try {
                if (driverDetails.getLicenceDocPath() != null) {
                    licenceDoc.put("type", "Licence");
                    licenceDoc.put("location", driverDetails.getLicenceDocPath()
                            .substring(driverDetails.getLicenceDocPath().indexOf("upload") - 1));
                    docs.add(licenceDoc);
                }
            } catch (Exception e) {
            }
            try {
                if (driverDetails.getMedicalDocPath() != null) {
                    medicalDoc.put("type", "MedicalCertficate");
                    medicalDoc.put("location", driverDetails.getMedicalDocPath()
                            .substring(driverDetails.getMedicalDocPath().indexOf("upload") - 1));
                    docs.add(medicalDoc);
                }
            } catch (Exception e) {
            }
            try {
                if (driverDetails.getProfilePicPath() != null) {
                    profilePicDocs.put("type", "ProfilePic");
                    profilePicDocs.put("location", driverDetails.getProfilePicPath()
                            .substring(driverDetails.getProfilePicPath().indexOf("upload") - 1));
                    docs.add(profilePicDocs);
                }
            } catch (Exception e) {
            }
            allPendingRequests.put("licenceNum", driverDetails.getLicenceNumber());
            allPendingRequests.put("licenceValid", formatter.format(driverDetails.getLicenceValid()));
            allPendingRequests.put("medicalValid", formatter.format(driverDetails.getMedicalFitnessCertificateValid()));
            allPendingRequests.put("policeVerification", driverDetails.getPoliceVerification());
            allPendingRequests.put("vendorName", driverDetails.getEfmFmVendorMaster().getVendorName());
            allPendingRequests.put("fatherName", driverDetails.getFatherName());
            allPendingRequests.put("documents", docs);
            requests.add(allPendingRequests);
        }
   	 	log.info("serviceEnd -UserId   : " + driverMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    /*
     * ending driver approval services for pending,register and unregister
     * drivers
     */

    /*
     * starting vehicle approval service for pending,register and unregister
     * vehicle
     */
    @POST
    @Path("/unapprovedvehicle")
    public Response allUnapprovedVehicles(EFmFmVehicleMasterPO vehicleMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vehicleMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vehicleMasterPO.getUserId());
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
		
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
 	 	log.info("serviceStart -UserId   : " + vehicleMasterPO.getUserId());  
        List<EFmFmVehicleMasterPO> unapprovedVehicles = approvalBO.getAllUnapprovedVehicles(vehicleMasterPO.getCombinedFacility());   
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
        if (unapprovedVehicles != null) {
            for (EFmFmVehicleMasterPO pending : unapprovedVehicles) {
                Map<String, Object> allPendingRequests = new HashMap<String, Object>();
                allPendingRequests.put("vehicleNumber", pending.getVehicleNumber());
                allPendingRequests.put("name", pending.getEfmFmVendorMaster().getVendorName());
                allPendingRequests.put("vehicleId", pending.getVehicleId());
                allPendingRequests.put("facilityName", pending.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
                // log.info("APProval
                // calllled.......................................................");
                requests.add(allPendingRequests);
            }
        }
        log.info("serviceEnd -UserId : " + vehicleMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/approvedvehicle")
    public Response allApprovedVehicle(EFmFmVehicleMasterPO vehicleMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vehicleMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vehicleMasterPO.getUserId());
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
    	
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        log.info("serviceStart -UserId : " + vehicleMasterPO.getUserId());
        List<EFmFmVehicleMasterPO> approvedVehicles = approvalBO
                .getAllApprovedVehiclesWithOutDummy(vehicleMasterPO.getCombinedFacility()); 
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
        if (approvedVehicles != null) {
            for (EFmFmVehicleMasterPO pending : approvedVehicles) {
                Map<String, Object> allPendingRequests = new HashMap<String, Object>();
                allPendingRequests.put("vehicleNumber", pending.getVehicleNumber());
                allPendingRequests.put("name", pending.getEfmFmVendorMaster().getVendorName());
                allPendingRequests.put("vehicleId", pending.getVehicleId());
                allPendingRequests.put("facilityName", pending.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
                requests.add(allPendingRequests);
            }
        }
    	 log.info("serviceEnd -UserId : " + vehicleMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/inactivevehicle")
    public Response allInActiveVehicle(EFmFmVehicleMasterPO vehicleMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vehicleMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vehicleMasterPO.getUserId());
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
		
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        log.info("serviceStart -UserId : " + vehicleMasterPO.getUserId());
        List<EFmFmVehicleMasterPO> inactiveVehicles = approvalBO
                .getAllInActiveVehicles(vehicleMasterPO.getCombinedFacility());
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
        if (inactiveVehicles != null) {
            for (EFmFmVehicleMasterPO pending : inactiveVehicles) {
                Map<String, Object> allPendingRequests = new HashMap<String, Object>();
                allPendingRequests.put("vehicleNumber", pending.getVehicleNumber());
                allPendingRequests.put("name", pending.getEfmFmVendorMaster().getVendorName());
                allPendingRequests.put("vehicleId", pending.getVehicleId());
                allPendingRequests.put("facilityName", pending.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

                requests.add(allPendingRequests);
            }
        }
      	 log.info("serviceEnd -UserId : " + vehicleMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/approvevehicle")
    public Response approveParticularVehicle(EFmFmVehicleMasterPO vehicleMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vehicleMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vehicleMasterPO.getUserId());
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
		
        IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
   	 	log.info("serviceStart -UserId : " + vehicleMasterPO.getUserId());
        EFmFmVehicleMasterPO approvevehicle = vehicleCheckInBO
                .getParticularVehicleDetail(vehicleMasterPO.getVehicleId());
        if (!(approvevehicle.getEfmFmVendorMaster().getStatus().equalsIgnoreCase("A"))) {
            return Response.ok("notapproved", MediaType.APPLICATION_JSON).build();
        }
        approvevehicle.setStatus("A");
        vehicleCheckInBO.update(approvevehicle);
      	 log.info("serviceEnd -UserId : " + vehicleMasterPO.getUserId());
        return Response.ok("success", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/rejectvehicle")
    public Response rejectParticularVehicle(EFmFmVehicleMasterPO vehicleMasterPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vehicleMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vehicleMasterPO.getUserId());
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
		
    	
        IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
   	 	log.info("serviceStart -UserId : " + vehicleMasterPO.getUserId());
        EFmFmVehicleMasterPO rejectvehicle = vehicleCheckInBO
                .getParticularVehicleDetail(vehicleMasterPO.getVehicleId());
        rejectvehicle.setStatus("R");
        vehicleCheckInBO.update(rejectvehicle);
      	 log.info("serviceEnd -UserId : " + vehicleMasterPO.getUserId());
        return Response.ok("success", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/addagainvehicle")
    public Response addAgainParticularVehicle(EFmFmVehicleMasterPO vehicleMasterPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vehicleMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vehicleMasterPO.getUserId());
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
		
		
    	
        IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        log.info("serviceStart -UserId : " + vehicleMasterPO.getUserId()); 
        EFmFmVehicleMasterPO addAgainvehicle = vehicleCheckInBO
                .getParticularVehicleDetail(vehicleMasterPO.getVehicleId());
        addAgainvehicle.setStatus("P");
        vehicleCheckInBO.update(addAgainvehicle);
     	log.info("serviceEnd -UserId : " + vehicleMasterPO.getUserId());
        return Response.ok("success", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/removevehicle")
    public Response removeParticularVehicle(EFmFmVehicleMasterPO vehicleMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vehicleMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vehicleMasterPO.getUserId());
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
		
		
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
   	 	log.info("serviceStart -UserId : " + vehicleMasterPO.getUserId());
  
        List<EFmFmVehicleCheckInPO> checkInDetails = iVehicleCheckInBO.getParticularVehicleCheckedInTableEntry(vehicleMasterPO.getCombinedFacility(),
                vehicleMasterPO.getVehicleId());
        if (checkInDetails.isEmpty()) {
        	List<EFmFmVehicleDocsPO>  vehicleDetails=iVehicleCheckInBO.getAlluploadVehicleFileDetails(vehicleMasterPO.getVehicleId());
	        	if(!(vehicleDetails.isEmpty())){
		        	for (EFmFmVehicleDocsPO vehicleList:vehicleDetails){        		
		        		iVehicleCheckInBO.deleteVehicleUploadDetails(vehicleList.getVehicleDocId());
		        	} 
	        	 }
	            approvalBO.deleteParticularVehicle(vehicleMasterPO.getVehicleId());
        	
        } else {
            EFmFmVehicleMasterPO vehicleDetail = iVehicleCheckInBO
                    .getParticularVehicleDetail(vehicleMasterPO.getVehicleId());
            vehicleDetail.setStatus("R");
            iVehicleCheckInBO.update(vehicleDetail);
        }
    	 log.info("serviceEnd -UserId : " + vehicleMasterPO.getUserId());
        return Response.ok("success", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/vehicleDetail")
    public Response getParticularVehicleDetail(EFmFmVehicleMasterPO vehicleMasterPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vehicleMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vehicleMasterPO.getUserId());
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
		
		
        IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
   	 	log.info("serviceStart -UserId : " + vehicleMasterPO.getUserId()); 
        EFmFmVehicleMasterPO vehicleDetail = vehicleCheckInBO
                .getParticularVehicleDetail(vehicleMasterPO.getVehicleId());
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
        Map<String, Object> insuranceDoc = new HashMap<String, Object>();
        Map<String, Object> polutionDoc = new HashMap<String, Object>();
        Map<String, Object> registrationDocs = new HashMap<String, Object>();
        Map<String, Object> taxDocs = new HashMap<String, Object>();
        List<Map<String, Object>> docs = new ArrayList<Map<String, Object>>();
        if (vehicleDetail != null) {
            Map<String, Object> allPendingRequests = new HashMap<String, Object>();
            allPendingRequests.put("vehicleNumber", vehicleDetail.getVehicleNumber());
            allPendingRequests.put("vehicleOwnerName", vehicleDetail.getEfmFmVendorMaster().getVendorName());
            allPendingRequests.put("vehicleId", vehicleDetail.getVehicleId());
            allPendingRequests.put("vehicleCapacity", vehicleDetail.getCapacity());
            allPendingRequests.put("insuranceValid", formatter.format(vehicleDetail.getInsuranceValidDate()));
            allPendingRequests.put("permitValid", formatter.format(vehicleDetail.getPermitValidDate()));
            allPendingRequests.put("facilityName", vehicleDetail.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

            try {
                if (vehicleDetail.getInsurancePath() != null) {
                    insuranceDoc.put("type", "Insurance");
                    insuranceDoc.put("location", vehicleDetail.getInsurancePath()
                            .substring(vehicleDetail.getInsurancePath().indexOf("upload") - 1));
                    docs.add(insuranceDoc);
                }
            } catch (Exception e) {
            }
            try {
                if (vehicleDetail.getPoluctionCertificatePath() != null) {
                    polutionDoc.put("type", "PolutionCertificate");
                    polutionDoc.put("location", vehicleDetail.getPoluctionCertificatePath()
                            .substring(vehicleDetail.getPoluctionCertificatePath().indexOf("upload") - 1));
                    docs.add(polutionDoc);
                }
            } catch (Exception e) {
            }
            try {
                if (vehicleDetail.getRegistartionCertificatePath() != null) {
                    registrationDocs.put("type", "RegistrationCertificate");
                    registrationDocs.put("location", vehicleDetail.getRegistartionCertificatePath()
                            .substring(vehicleDetail.getRegistartionCertificatePath().indexOf("upload") - 1));
                    docs.add(registrationDocs);
                }
            } catch (Exception e) {
            }
            try {
                if (vehicleDetail.getTaxCertificatePath() != null) {
                    taxDocs.put("type", "TaxCertificate");
                    taxDocs.put("location", vehicleDetail.getTaxCertificatePath()
                            .substring(vehicleDetail.getTaxCertificatePath().indexOf("upload") - 1));
                    docs.add(taxDocs);
                }
            } catch (Exception e) {
            }
            allPendingRequests.put("polutionValid", formatter.format(vehicleDetail.getPolutionValid()));
            allPendingRequests.put("taxCertificate", formatter.format(vehicleDetail.getTaxCertificateValid()));
            allPendingRequests.put("vehicleEngineNum", vehicleDetail.getVehicleEngineNumber());
            allPendingRequests.put("vehicleModel", vehicleDetail.getVehicleModel());
            allPendingRequests.put("vendorName", vehicleDetail.getEfmFmVendorMaster().getVendorName());
            allPendingRequests.put("vehicleMake", vehicleDetail.getVehicleMake());
            allPendingRequests.put("vehicleRegistrationNum", vehicleDetail.getRegistartionCertificateNumber());
            allPendingRequests.put("documents", docs);
            // log.info("APProval
            // calllled.......................................................");
            requests.add(allPendingRequests);
        }
     	 log.info("serviceEnd -UserId : " + vehicleMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    /*
     * ending vehicle approval services for pending,register and unregister
     * vendors
     */

    /*
     * starting vendor approval services for pending,register and unregister
     * vendors
     */
    @POST
    @Path("/unapprovedvendor")
    public Response allUnapprovedVendor(EFmFmVendorMasterPO vendorMasterPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vendorMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

    	
		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vendorMasterPO.getUserId());
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
		
		
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
   		log.info("serviceStart -UserId : " + vendorMasterPO.getUserId());
        List<EFmFmVendorMasterPO> unapprovedDrivers = approvalBO
                .getAllUnapprovedVendors(vendorMasterPO.getCombinedFacility());
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
        if (unapprovedDrivers != null) {
            for (EFmFmVendorMasterPO pending : unapprovedDrivers) {
                Map<String, Object> allPendingRequests = new HashMap<String, Object>();
                allPendingRequests.put("mobileNumber", pending.getVendorMobileNo());
                allPendingRequests.put("name", pending.getVendorName());
                allPendingRequests.put("vendorId", pending.getVendorId());
                allPendingRequests.put("facilityName", pending.geteFmFmClientBranchPO().getBranchName());
                // log.info("APProval
                // calllled.......................................................");
                requests.add(allPendingRequests);
            }
        }
      	 log.info("serviceEnd -UserId : " + vendorMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/approvedvendor")
    public Response allApprovedVendor(EFmFmVendorMasterPO vendorMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vendorMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vendorMasterPO.getUserId());
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
		
		
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        log.info("serviceStart -UserId : " + vendorMasterPO.getUserId());
        List<EFmFmVendorMasterPO> approvedVendors = approvalBO
                .getAllApprovedVendors(vendorMasterPO.getCombinedFacility());
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
        if (approvedVendors != null) {
            for (EFmFmVendorMasterPO pending : approvedVendors) {
                Map<String, Object> allPendingRequests = new HashMap<String, Object>();
                allPendingRequests.put("mobileNumber", pending.getVendorMobileNo());
                allPendingRequests.put("name", pending.getVendorName());
                allPendingRequests.put("vendorId", pending.getVendorId());
                allPendingRequests.put("facilityName", pending.geteFmFmClientBranchPO().getBranchName());

                requests.add(allPendingRequests);
            }
        }
      	log.info("serviceEnd -UserId : " + vendorMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/inactivevendor")
    public Response allInActiveVendor(EFmFmVendorMasterPO vendorMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vendorMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vendorMasterPO.getUserId());
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
		
		
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        log.info("serviceStart -UserId : " + vendorMasterPO.getUserId());
        List<EFmFmVendorMasterPO> inactiveVendors = approvalBO
                .getAllInActiveVendors(vendorMasterPO.getCombinedFacility());   
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
        if (inactiveVendors != null) {
            for (EFmFmVendorMasterPO pending : inactiveVendors) {
                Map<String, Object> allPendingRequests = new HashMap<String, Object>();
                allPendingRequests.put("mobileNumber", pending.getVendorMobileNo());
                allPendingRequests.put("name", pending.getVendorName());
                allPendingRequests.put("vendorId", pending.getVendorId());
                allPendingRequests.put("facilityName", pending.geteFmFmClientBranchPO().getBranchName());
                requests.add(allPendingRequests);
            }
        }
      	log.info("serviceEnd -UserId : " + vendorMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/vendorDetail")
    public Response getParticularVendorDetail(EFmFmVendorMasterPO vendorMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vendorMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vendorMasterPO.getUserId());
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
		
        IVendorDetailsBO vendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
        log.info("serviceStart -UserId : " + vendorMasterPO.getUserId());
        EFmFmVendorMasterPO vendorDetails = vendorDetailsBO.getParticularVendorDetail(vendorMasterPO.getVendorId());   
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
        if (vendorDetails != null) {
            Map<String, Object> allPendingRequests = new HashMap<String, Object>();
            allPendingRequests.put("mobileNumber", vendorDetails.getVendorMobileNo());
            allPendingRequests.put("name", vendorDetails.getVendorName());
            allPendingRequests.put("vendorId", vendorDetails.getVendorId());
            allPendingRequests.put("address", vendorDetails.getAddress());
            allPendingRequests.put("emailId", vendorDetails.getEmailId());
            allPendingRequests.put("tinNum", vendorDetails.getTinNumber());
            allPendingRequests.put("facilityName", vendorDetails.geteFmFmClientBranchPO().getBranchName());

            requests.add(allPendingRequests);
        }
        log.info("serviceEnd -UserId : " + vendorMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/approvevendor")
    public Response approveParticularVendor(EFmFmVendorMasterPO vendorMasterPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vendorMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		
		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vendorMasterPO.getUserId());
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

        IVendorDetailsBO vendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
	 	log.info("serviceStart -UserId : " + vendorMasterPO.getUserId());
        EFmFmVendorMasterPO approvevendor = vendorDetailsBO.getParticularVendorDetail(vendorMasterPO.getVendorId());   
        approvevendor.setStatus("A");
        vendorDetailsBO.update(approvevendor);
      	 log.info("serviceEnd -UserId : " + vendorMasterPO.getUserId());
        return Response.ok("success", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/rejectvendor")
    public Response rejectParticularVendor(EFmFmVendorMasterPO vendorMasterPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vendorMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vendorMasterPO.getUserId());
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
		
        IVendorDetailsBO vendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
     	log.info("serviceStart -UserId : " + vendorMasterPO.getUserId());
        EFmFmVendorMasterPO rejectvendor = vendorDetailsBO.getParticularVendorDetail(vendorMasterPO.getVendorId());
        log.info("rejectvendor service calling");
        rejectvendor.setStatus("R");
        vendorDetailsBO.update(rejectvendor);
        try {
            Thread thread1 = new Thread(new Runnable() {
                @Override
				public void run() {
                    SendMailBySite mailSender=new SendMailBySite();
                    String toMailId = ContextLoader.getContext().getMessage("user.toMailSender", null, "toMailSender", null);
                    mailSender.mailForVendorRemoval(toMailId, "team", rejectvendor.getVendorName());
                	}
            });
            thread1.start();
        } catch (Exception e) {
            log.info("vendor excel registration mail" + e);
        }         
      	log.info("serviceEnd -UserId : " + vendorMasterPO.getUserId());
        return Response.ok("success", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/removevendor")
    public Response removeParticularVendor(EFmFmVendorMasterPO vendorMasterPO) {
    	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vendorMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vendorMasterPO.getUserId());
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
		
        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
        IVendorDetailsBO vendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
        IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
                .getBean("IVehicleCheckInBO");
        log.info("removevendor service calling");
        log.info("serviceStart -UserId : " + vendorMasterPO.getUserId());

        List<EFmFmVehicleCheckInPO> checkInDetails = iVehicleCheckInBO.getParticularVendorCheckedInTableEntry(vendorMasterPO.getCombinedFacility(), vendorMasterPO.getVendorId());
        try {
            if (checkInDetails.isEmpty()) {
                approvalBO.deleteParticularVendor(vendorMasterPO.getVendorId());
            } else {

                EFmFmVendorMasterPO vendorDetails = vendorDetailsBO
                        .getParticularVendorDetail(vendorMasterPO.getVendorId());
                vendorDetails.setStatus("R");
                vendorDetailsBO.update(vendorDetails);

            }
        } catch (Exception e) {
            EFmFmVendorMasterPO vendorDetails = vendorDetailsBO.getParticularVendorDetail(vendorMasterPO.getVendorId());
            vendorDetails.setStatus("R");
            vendorDetailsBO.update(vendorDetails);

        }
      	log.info("serviceEnd -UserId : " + vendorMasterPO.getUserId());
        return Response.ok("success", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/addagainvendor")
    public Response addAgainParticularVendor(EFmFmVendorMasterPO vendorMasterPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vendorMasterPO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(vendorMasterPO.getUserId());
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
		
        IVendorDetailsBO vendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
        log.info("serviceStart -UserId : " + vendorMasterPO.getUserId());
        EFmFmVendorMasterPO addAgainVendor = vendorDetailsBO.getParticularVendorDetail(vendorMasterPO.getVendorId());
        addAgainVendor.setStatus("P");
        vendorDetailsBO.update(addAgainVendor);
      	log.info("serviceEnd -UserId : " + vendorMasterPO.getUserId());
        return Response.ok("success", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/tripSheetForApproval")
    public Response getTripSheetForApproval(EFmFmAssignRoutePO eFmFmAssignRoutePO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAssignRoutePO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmAssignRoutePO.getUserId());
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
		
        IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
        log.info("serviceStart -UserId : " + eFmFmAssignRoutePO.getUserId());
        List<EFmFmAssignRoutePO> editedTrips = iAssignRouteBO.getAllEdiTripByBranchId(eFmFmAssignRoutePO.getCombinedFacility());
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
        if (!(editedTrips.isEmpty())) {
            for (EFmFmAssignRoutePO tripDetails : editedTrips) {
                Map<String, Object> detailTripList = new HashMap<String, Object>();
                detailTripList.put("assignRouteId", tripDetails.getAssignRouteId());
                detailTripList.put("routeName",
                        tripDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
                detailTripList.put("vehicleNumber",
                        tripDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
                detailTripList.put("plannedDistance", tripDetails.getPlannedDistance());

                try {
                    if (tripDetails.getTravelledDistance() == 0) {
                        detailTripList.put("travelledDistance", "NA");
                    } else {
                        String extensionRemoved = Double.toString(tripDetails.getTravelledDistance()).split("\\.")[1];
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
                if(tripDetails.getEditDistanceType()==null){
                	detailTripList.put("editDistanceType","N");
                }else{
                	detailTripList.put("editDistanceType",tripDetails.getEditDistanceType());
                }                
                detailTripList.put("editDistance", tripDetails.getEditedTravelledDistance());
                detailTripList.put("tripEditRemarks", tripDetails.getRemarksForEditingTravelledDistance());
                detailTripList.put("tripStartDate", dateFormatter.format(tripDetails.getTripStartTime()));
                detailTripList.put("tripAssignDate", dateFormatter.format(tripDetails.getTripAssignDate()));
                detailTripList.put("tripCompleteDate", dateFormatter.format(tripDetails.getTripCompleteTime()));
                detailTripList.put("tripType", tripDetails.getTripType());
                detailTripList.put("facilityName", tripDetails.geteFmFmClientBranchPO().getBranchName());
                requests.add(detailTripList);
            }
        }
      	log.info("serviceEnd -UserId : " + eFmFmAssignRoutePO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

    
    @POST
    @Path("/editDistanceWithRemarks")
    public Response editDistanceWithRemarks(EFmFmAssignRoutePO eFmFmAssignRoutePO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAssignRoutePO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmAssignRoutePO.getUserId());
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
		
        IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
   	 	log.info("serviceStart -UserId : " + eFmFmAssignRoutePO.getUserId());
        List<EFmFmAssignRoutePO> editedTrips = iAssignRouteBO.closeParticularTrips(eFmFmAssignRoutePO);
        Map<String, Object> requests = new HashMap<String, Object>();
        log.info("editTripByAdminApproval" + editedTrips);
        requests.put("status", "success");
        if (!(editedTrips.isEmpty())) {
        	if(eFmFmAssignRoutePO.getEditDistanceType().equalsIgnoreCase("S")){
        		if(!(editedTrips.get(0).getTravelledDistance()>=eFmFmAssignRoutePO.getEditedTravelledDistance())){
            		requests.put("failed","HIGHSUBVALUE");
            		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
            	}
        	}else if(eFmFmAssignRoutePO.getEditDistanceType().equalsIgnoreCase("A")){
        		if(eFmFmAssignRoutePO.getEditedTravelledDistance()<=0){
            		requests.put("failed","LOWADDVALUE");
            		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
            	}
        	}      	
            editedTrips.get(0).setEditedTravelledDistance(eFmFmAssignRoutePO.getEditedTravelledDistance());
            editedTrips.get(0).setEditDistanceApproval("NO");
            editedTrips.get(0).setEditDistanceType(eFmFmAssignRoutePO.getEditDistanceType());
            editedTrips.get(0).setRemarksForEditingTravelledDistance(eFmFmAssignRoutePO.getRemarksForEditingTravelledDistance());
            iAssignRouteBO.update(editedTrips.get(0));            
            try {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
    				public void run() {
                        SendMailBySite mailSender=new SendMailBySite();
                        String toMailId = ContextLoader.getContext().getMessage("user.toMailSender", null, "toMailSender", null);
                        mailSender.mailForManualDistanceApproval(toMailId,editedTrips.get(0).geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName(), editedTrips.get(0).getAssignRouteId());
                    	}
                });
                thread1.start();
            } catch (Exception e) {
                log.info("driver removal from approval" + e);
            } 
            
            
        }
      	 log.info("serviceEnd -UserId : " + eFmFmAssignRoutePO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }
    
    
    @POST
    @Path("/editDistanceByAdmin")
    public Response getTripSheetApproval(EFmFmAssignRoutePO eFmFmAssignRoutePO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAssignRoutePO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmAssignRoutePO.getUserId());
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
		
        IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
   	 	log.info("serviceStart -UserId : " + eFmFmAssignRoutePO.getUserId());
        List<EFmFmAssignRoutePO> editedTrips = iAssignRouteBO.closeParticularTrips(eFmFmAssignRoutePO);
        Map<String, Object> requests = new HashMap<String, Object>();
        log.info("editTripByAdminApproval" + editedTrips);
        requests.put("status", "success");
        if (!(editedTrips.isEmpty())) {
	        	if(eFmFmAssignRoutePO.getEditDistanceType().equalsIgnoreCase("S")){
	        		if(!(editedTrips.get(0).getTravelledDistance()>=eFmFmAssignRoutePO.getEditedTravelledDistance())){
	            		requests.put("failed","HIGHSUBVALUE");
	            		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	            	}
	        	}else if(eFmFmAssignRoutePO.getEditDistanceType().equalsIgnoreCase("A")){
	        		if(eFmFmAssignRoutePO.getEditedTravelledDistance()<=0){
	            		requests.put("failed","LOWADDVALUE");
	            		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	            	}
	        	}  
	        	try {
	        		if(eFmFmAssignRoutePO.getApprovalFlg().equalsIgnoreCase("Y")){ 
	    	        	//This block will execute when admin will editing the GPS Km
	    	        	   editedTrips.get(0).setEditedTravelledDistance(eFmFmAssignRoutePO.getEditedTravelledDistance());
	    	               editedTrips.get(0).setEditDistanceType(eFmFmAssignRoutePO.getEditDistanceType());
	    	               
	    	               if(editedTrips.get(0).getEditDistanceType().equalsIgnoreCase("A")){
	    	           		editedTrips.get(0).setTravelledDistance(
	    	                           editedTrips.get(0).getTravelledDistance() + editedTrips.get(0).getEditedTravelledDistance());	           		
	    	           	}else if(editedTrips.get(0).getEditDistanceType().equalsIgnoreCase("S")){
	    	           		editedTrips.get(0).setTravelledDistance(
	    	                           editedTrips.get(0).getTravelledDistance() - editedTrips.get(0).getEditedTravelledDistance());	           		
	    	           	}else{
	    	           		requests.put("failed", "WrongEditDistanceType");
	    	           		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	    	           	}         
	    	               editedTrips.get(0).setEditDistanceApproval("Yes");
	    	               editedTrips.get(0).setApprovedBy(String.valueOf(eFmFmAssignRoutePO.getUserId()));
	    	               editedTrips.get(0).setApprovedDate(new Date());
	    	               iAssignRouteBO.update(editedTrips.get(0));
	    	               requests.put("updatedDistance",Math.round(editedTrips.get(0).getTravelledDistance()*100)/100.00);
	    	               log.info("serviceEnd -UserId : " + eFmFmAssignRoutePO.getUserId());
	    	               return Response.ok(requests, MediaType.APPLICATION_JSON).build();	               
	    	          }
				} catch (Exception e) {
					log.info("approvalFlg is empty");
				}        	
            editedTrips.get(0).setEditedTravelledDistance(eFmFmAssignRoutePO.getEditedTravelledDistance());
            editedTrips.get(0).setEditDistanceType(eFmFmAssignRoutePO.getEditDistanceType());
            editedTrips.get(0).setEditDistanceApproval("NO");

            iAssignRouteBO.update(editedTrips.get(0));
        }
      	 log.info("serviceEnd -UserId : " + eFmFmAssignRoutePO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }
    
    
    @POST
    @Path("/distanceApproval")
    public Response distanceApprovalByAdmin(EFmFmAssignRoutePO eFmFmAssignRoutePO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
  	    log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAssignRoutePO.getUserId()))){

			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}}catch(Exception e){
			log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(eFmFmAssignRoutePO.getUserId());
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
		
        IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
   	 	log.info("serviceStart -UserId : " + eFmFmAssignRoutePO.getUserId());
        List<EFmFmAssignRoutePO> editedTrips = iAssignRouteBO.closeParticularTrips(eFmFmAssignRoutePO);
        Map<String, Object> requests = new HashMap<String, Object>();
        log.info("trip data" + editedTrips);
        requests.put("status", "success");
        if (!(editedTrips.isEmpty())) {
        	if(editedTrips.get(0).getEditDistanceType().equalsIgnoreCase("A")){
        		editedTrips.get(0).setTravelledDistance(
                        editedTrips.get(0).getTravelledDistance() + editedTrips.get(0).getEditedTravelledDistance());
        		
        	}else if(editedTrips.get(0).getEditDistanceType().equalsIgnoreCase("S")){
        		editedTrips.get(0).setTravelledDistance(
                        editedTrips.get(0).getTravelledDistance() - editedTrips.get(0).getEditedTravelledDistance());
        		
        	}else{
        		requests.put("failed", "WrongEditDistanceType");
        		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
        	}         
            editedTrips.get(0).setEditDistanceApproval("Yes");
            editedTrips.get(0).setApprovedBy(String.valueOf(eFmFmAssignRoutePO.getUserId()));
            editedTrips.get(0).setApprovedDate(new Date());
            iAssignRouteBO.update(editedTrips.get(0));
        }
      	log.info("serviceEnd -UserId : " + eFmFmAssignRoutePO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();
    }

}
