package com.newtglobal.eFmFmFleet.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IEmployeeDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IFacilityBO;
import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.CalculateDistance;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.PasswordEncryption;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaNodalMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchSubConfigurationPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientRouteMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientUserRolePO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeRequestMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFacilityToFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRoleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserPasswordPO;
import com.newtglobal.eFmFmFleet.model.EFmFmZoneMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;
 
@Component
@Path("/xlUtilityEmployeeUpload")
public class EmployeeImportExcel {
    /*
     * @Reading employee details from employee_master xl utility.
     * 
     * @Stored all the values on ArrayList.
     * 
     * @author Rajan R
     * 
     * @since 2015-05-12
     */
    private static Log log = LogFactory.getLog(EmployeeImportExcel.class);
    
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();


    @POST
    @Path("/employeeRecord")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response upload(@FormDataParam("filename") InputStream uploadedInputStream,
    		@FormDataParam("filename") InputStream uploadedSizeInputStream,
            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
            @QueryParam("combinedFacility") String combinedFacility,
            @Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException, InvalidKeyException,
                    NoSuchAlgorithmException, URISyntaxException {        
        String status = "success"; 
        System.out.println("combinedFacility"+combinedFacility);
        log.info("serviceStart -UserId :" +userId);
        int noOfcolumn=29; 
        List<Map<String, Object>> response =new ArrayList<Map<String, Object>>();
 	   Map<String, Object>  empReqExcelValidation= new HashMap<String, Object>();
 	  IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
 	 Map<String, Object> responce = new HashMap<String, Object>();
 	 		
 	 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
 	 try{
 	 	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),userId))){
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

        try {
        	Map<Integer, Object>  empMasterDetails= new HashMap<Integer, Object>();
	           XSSFWorkbook workbook = new XSSFWorkbook(uploadedInputStream);
	           XSSFSheet sheet = workbook.getSheetAt(0);
	   		if (!((fileDetail.getFileName().toLowerCase().endsWith(".xlsx")))){
				log.info("File Exctension");
				status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
				empReqExcelValidation.put("IssueStatus",status);
          		response.add(empReqExcelValidation);
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
		        }
	           int bytes=getBytes(uploadedSizeInputStream).length;
		   		double kilobytes = (bytes / 1000);
		   		double megabytes = (kilobytes / 1000);
		     		log.info("bytes :" +bytes);
		     		log.info("kilobytes :" +Math.round(kilobytes));
		     		log.info("megabytes :" +Math.round(megabytes));
		           if(megabytes > 5){
		           	log.info("Inside Excel bigSize");
					status="Kinldy check the file Size,System will except a excel only maximum size 5MB.";  	                    		
					empReqExcelValidation.put("IssueStatus",status);
	          		response.add(empReqExcelValidation);
	                return Response.ok(response, MediaType.APPLICATION_JSON).build();
		           }
	           
	           int rowId=0;	          
	           for (int rowNum = 0; rowNum <=sheet.getLastRowNum(); rowNum++) {
	        	   Map<String, Object>  empReqExcel= new HashMap<String, Object>();
	        	   ArrayList<Object> columnValue = new ArrayList<Object>();	                
	               rowId++;
	              Row rowValues = sheet.getRow(rowNum);
	              if (rowValues == null) {	     
	                 continue;
	              }
	              for (int cellNo = 0; cellNo < noOfcolumn; cellNo++) {
	                 Cell cell = rowValues.getCell(cellNo, Row.RETURN_BLANK_AS_NULL);	                 
	                 if (cell == null) {	                	
	                	 columnValue.add("CELL_TYPE_BLANK");
	                  }else{              	              
		                    switch (cell.getCellType()) {
		                    case Cell.CELL_TYPE_BOOLEAN:
		                        columnValue.add(cell.getBooleanCellValue());
		                        break;
		                    case Cell.CELL_TYPE_BLANK:
		                    	 columnValue.add("CELL_TYPE_BLANK");	
		             			break;                   		
		                    case Cell.CELL_TYPE_NUMERIC:
		                        if (DateUtil.isCellDateFormatted(cell)) {
		                            Date date = DateUtil.getJavaDate((double) cell.getNumericCellValue());
		                            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
		                            columnValue.add(df.format(date));
		                        }else {
		                            cell.setCellType(Cell.CELL_TYPE_STRING);
		                            columnValue.add(cell.getStringCellValue());
		                        }
		                        break;
		                    case Cell.CELL_TYPE_STRING:
		                        columnValue.add(cell.getStringCellValue().toString());
		                        break;
		                    case Cell.CELL_TYPE_FORMULA:
		                        columnValue.add("");
		                        break;
		                    default:
		                        columnValue.add("");
		                        break;
		                    }
	                 }
	              }
	              columnValue.removeAll(Arrays.asList(null, ""));                
	                if(!columnValue.isEmpty()){	                	
	                	if(rowId>1){ 
                    		if(columnValue.size()>=29){ 
	                    		if(!empMasterExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,combinedFacility).isEmpty()){                		
	                    			response.addAll(empMasterExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,combinedFacility));	                    			
	                    		}else{	                    			
	                    				 empMasterDetails.put(rowId, columnValue);                    		
	                    		}
	                    	}else{
	                    		status="Kinldy check the no -of Column -29 , uploaded Column No:"+columnValue.size();
	                    		empReqExcel.put("RNo",Integer.toString(rowId));
  	                    		empReqExcel.put("IssueStatus",status);
	                    		response.add(empReqExcel);
	                    	}
                    	}else{
                    		  if(columnValue.size()<29){                			              			
                    			    status="Kinldy check the no -of Column's ";
                    			    empReqExcel.put("RNo",Integer.toString(rowId));
	  	                    		empReqExcel.put("IssueStatus",status);
	  	                    		response.add(empReqExcel);
                    		}else{
                    			//Need to configure below Text some where
                    			if(!"EmployeeId".equalsIgnoreCase(columnValue.get(0).toString().replace(" ","").trim()) 
                    					|| !"EmployeeFirstName".equalsIgnoreCase(columnValue.get(1).toString().replace(" ","").trim()) 
                    					|| !"EmployeeMiddleName".equalsIgnoreCase(columnValue.get(2).toString().replace(" ","").trim()) 
                    					|| !"EmployeeLastName".equalsIgnoreCase(columnValue.get(3).toString().replace(" ","").trim())
                    					|| !"EmployeeGender".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim())
                    					|| !"EmployeeBusinessUnit".equalsIgnoreCase(columnValue.get(5).toString().replace(" ","").trim()) 
                            			|| !"EmployeeDepartment".equalsIgnoreCase(columnValue.get(6).toString().replace(" ","").trim()) 
                            			|| !"EmployeeDomain".equalsIgnoreCase(columnValue.get(7).toString().replace(" ","").trim()) 
                            			|| !"EmployeemobileNumber".equalsIgnoreCase(columnValue.get(8).toString().replace(" ","").trim()) 
                            			|| !"EmployeeemailId".equalsIgnoreCase(columnValue.get(9).toString().replace(" ","").trim())
                            			|| !"EmployeeDestinationArea".equalsIgnoreCase(columnValue.get(10).toString().replace(" ","").trim())
                    					|| !"State".equalsIgnoreCase(columnValue.get(11).toString().replace(" ","").trim()) 
                    					|| !"CityName".equalsIgnoreCase(columnValue.get(12).toString().replace(" ","").trim()) 
                    					|| !"Pincode".equalsIgnoreCase(columnValue.get(13).toString().replace(" ","").trim())
                    					|| !"EmployeeCurrentAdddress".equalsIgnoreCase(columnValue.get(14).toString().replace(" ","").trim())
                    					|| !"EmployeeDesignation".equalsIgnoreCase(columnValue.get(15).toString().replace(" ","").trim()) 
                    					|| !"EmployeeprojectId".equalsIgnoreCase(columnValue.get(16).toString().replace(" ","").trim()) 
                    					|| !"EmployeeprojectName".equalsIgnoreCase(columnValue.get(17).toString().replace(" ","").trim()) 
                    					|| !"EmployeeprojectAllocationStarDate".equalsIgnoreCase(columnValue.get(18).toString().replace(" ","").trim()) 
                    					|| !"EmployeeprojectAllocationEndDate".equalsIgnoreCase(columnValue.get(19).toString().replace(" ","").trim())
                    					|| !"PhysicallyChallenged".equalsIgnoreCase(columnValue.get(20).toString().replace(" ","").trim())                			
                    					|| !"Role".equalsIgnoreCase(columnValue.get(21).toString().replace(" ","").trim()) 
                    					|| !"LoginUserName".equalsIgnoreCase(columnValue.get(22).toString().replace(" ","").trim()) 
                    					|| !"ZoneName".equalsIgnoreCase(columnValue.get(23).toString().replace(" ","").trim())
                    					|| !"WeekOff".equalsIgnoreCase(columnValue.get(24).toString().replace(" ","").trim())
                    					|| !"EmployeeGeoCodes".equalsIgnoreCase(columnValue.get(25).toString().replace(" ","").trim()) 
                    					|| !"NodalPointname".equalsIgnoreCase(columnValue.get(26).toString().replace(" ","").trim())
                    					|| !"SpocEmployeeId".equalsIgnoreCase(columnValue.get(27).toString().replace(" ","").trim())
                					    || !"Facility".equalsIgnoreCase(columnValue.get(28).toString().replace(" ","").trim())){                		                    				    status="Kinldy check the Column Names & No of Column's"+columnValue.size();
                    				    empReqExcel.put("RNo",Integer.toString(rowId));
    	  	                    		empReqExcel.put("IssueStatus",status);
    	  	                    		response.add(empReqExcel);
    	  	                    		log.info("serviceEnd -UserId :" +userId);
    	  	                    		return Response.ok(response, MediaType.APPLICATION_JSON).build();
                    				    
                    			}       			                			
                    		}
                    	}
    				}              
                }
                log.info("Size"+empMasterDetails.size());
    			if(response.isEmpty()){
    				if(!empMasterDetails.isEmpty()){
    					response=employeeRecord(empMasterDetails,branchId,userId,combinedFacility);	
    				}
    			}

            } catch (Exception e) {
            	log.info(" Error :" +e);
      		try{
            	if(e.getCause().toString().contains("InvalidFormatException")){
    				status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
    				empReqExcelValidation.put("IssueStatus",status);
              		response.add(empReqExcelValidation);
              		log.info(e+" serviceEnd -UserId :" +userId);
                    return Response.ok(response, MediaType.APPLICATION_JSON).build();
            	}
          		}catch(Exception e1){
              		log.info(" Error :" +e1);
              		e1.printStackTrace();
          		}
    			status="Kinldy check the excel for any empty column,System will except only excel format without formula and special symbols.";  	                    		
    			empReqExcelValidation.put("IssueStatus",status);
          		response.add(empReqExcelValidation);
          		log.info(e+" serviceEnd -UserId :" +userId);
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
                }
        	log.info("serviceEnd -UserId :" +userId);
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        
    /*
     * excel upload for guest..this function will give
     * 
     */

    @POST
    @Path("/guestExcelUpload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response guestExcelUploadRequest(@FormDataParam("filename") InputStream uploadedInputStream,
			@FormDataParam("filename") InputStream uploadedSizeInputStream,
            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
            @QueryParam("combinedFacility") String combinedFacility,
            @Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException, InvalidKeyException,
                    NoSuchAlgorithmException, URISyntaxException {
    	 String status = "success-";
    	 log.info("serviceStart -UserId :" +userId);
    	 int noOfcolumn=15; 
         List<Map<String, Object>> response =new ArrayList<Map<String, Object>>();
     	Map<String, Object>  empReqExcel= new HashMap<String, Object>(); 
     	Map<Integer, Object>  guestExcelDetails= new HashMap<Integer, Object>();
     	IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
     	Map<String, Object> responce = new HashMap<String, Object>();
     			
     	log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
     	try{
     	 	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),userId))){

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
        try {
	           XSSFWorkbook workbook = new XSSFWorkbook(uploadedInputStream);
	           XSSFSheet sheet = workbook.getSheetAt(0);
	           
		   		if (!((fileDetail.getFileName().toLowerCase().endsWith(".xlsx")))){
					log.info("File Exctension");
					status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
	          		empReqExcel.put("IssueStatus",status);
	          		response.add(empReqExcel);
	                return Response.ok(response, MediaType.APPLICATION_JSON).build();
			        }
	           
	           int bytes=getBytes(uploadedSizeInputStream).length;
		   		double kilobytes = (bytes / 1000);
		   		double megabytes = (kilobytes / 1000);
		     		log.info("bytes :" +bytes);
		     		log.info("kilobytes :" +Math.round(kilobytes));
		     		log.info("megabytes :" +Math.round(megabytes));
		           if(megabytes > 5){
		           	log.info("Inside Excel bigSize");
					status="Kinldy check the file Size,System will except a excel only maximum size 5MB.";  	                    		
	          		empReqExcel.put("IssueStatus",status);
	          		response.add(empReqExcel);
	                return Response.ok(response, MediaType.APPLICATION_JSON).build();
		           }
		           
	           int rowId=0;	          
	           for (int rowNum = 0; rowNum <=sheet.getLastRowNum(); rowNum++) {
	        	   ArrayList<Object> columnValue = new ArrayList<Object>();	                
	               rowId++;
	              Row rowValues = sheet.getRow(rowNum);
	              if (rowValues == null) {	     
	                 continue;
	              }
	              for (int cellNo = 0; cellNo < noOfcolumn; cellNo++) {
	                 Cell cell = rowValues.getCell(cellNo, Row.RETURN_BLANK_AS_NULL);	                 
	                 if (cell == null) {	                	
	                	 columnValue.add("CELL_TYPE_BLANK");
	                  }else{              	              
		                    switch (cell.getCellType()) {
		                    case Cell.CELL_TYPE_BOOLEAN:
		                        columnValue.add(cell.getBooleanCellValue());
		                        break;
		                    case Cell.CELL_TYPE_BLANK:
		                    	 columnValue.add("CELL_TYPE_BLANK");	
		             			break;                   		
		                    case Cell.CELL_TYPE_NUMERIC:
		                        if (DateUtil.isCellDateFormatted(cell)) {
		                            Date date = DateUtil.getJavaDate((double) cell.getNumericCellValue());
		                            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
		                            columnValue.add(df.format(date));
		                        }else {
		                            cell.setCellType(Cell.CELL_TYPE_STRING);
		                            columnValue.add(cell.getStringCellValue());
		                        }
		                        break;
		                    case Cell.CELL_TYPE_STRING:
		                        columnValue.add(cell.getStringCellValue().toString());
		                        break;
		                    case Cell.CELL_TYPE_FORMULA:
		                        columnValue.add("");
		                        break;
		                    default:
		                        columnValue.add("");
		                        break;
		                    }
	                 }
	              }
	              columnValue.removeAll(Arrays.asList(null, ""));                
	                if(!columnValue.isEmpty()){	                	
	                	if(rowId>1){                		
	                	if(columnValue.size()>=15){   
		                		if(!empGuestExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,combinedFacility).isEmpty()){                		
		                			response.addAll(empGuestExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,combinedFacility));
		                		}else{
		                			guestExcelDetails.put(rowId, columnValue);
		                		}
	                		}else{
	                				status="Kinldy check the no -of Column -15 Entered Column No:"+columnValue.size();                				
	                				empReqExcel.put("RNo",Integer.toString(rowId));
	  	                    		empReqExcel.put("IssueStatus",status);
		                    		response.add(empReqExcel);
	                	}               		
                		
                	}else{
                		  if(columnValue.size()<15){                			              			  			    			
  	                    		status="Kinldy check the no -of Column's";
  	                    		empReqExcel.put("RNo",Integer.toString(rowId));
  	                    		empReqExcel.put("IssueStatus",status);
  	                    		response.add(empReqExcel);
  	                    	
                		}else{
                			if(!"EmployeeId".equalsIgnoreCase(columnValue.get(0).toString().replace(" ","").trim()) 
                					
                					|| !"EmployeeFirstName".equalsIgnoreCase(columnValue.get(1).toString().replace(" ","").trim()) 
                					
                					|| !"EmployeeGender".equalsIgnoreCase(columnValue.get(2).toString().replace(" ","").trim())
                					
                        			|| !"EmployeemobileNumber".equalsIgnoreCase(columnValue.get(3).toString().replace(" ","").trim()) 
                        			
                        			|| !"EmployeeemailId".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim())
                        			
                					|| !"EmployeeCurrentAdddress".equalsIgnoreCase(columnValue.get(5).toString().replace(" ","").trim())
                					
                					|| !"LoginUserName".equalsIgnoreCase(columnValue.get(6).toString().replace(" ","").trim()) 
                					
                					|| !"ZoneName".equalsIgnoreCase(columnValue.get(7).toString().replace(" ","").trim())
                					
                					|| !"WeekOff".equalsIgnoreCase(columnValue.get(8).toString().replace(" ","").trim())
                					
                					|| !"EmployeeGeoCodes".equalsIgnoreCase(columnValue.get(9).toString().replace(" ","").trim()) 
                					
                					|| !"RequestStartDate".equalsIgnoreCase(columnValue.get(10).toString().replace(" ","").trim())
                					
                					|| !"RequestEndDate".equalsIgnoreCase(columnValue.get(11).toString().replace(" ","").trim())
                					
                					|| !"PickUpTime".equalsIgnoreCase(columnValue.get(12).toString().replace(" ","").trim()) 
                					
                					|| !"TripType".equalsIgnoreCase(columnValue.get(13).toString().replace(" ","").trim())
                					
                					|| !"HostNumber".equalsIgnoreCase(columnValue.get(14).toString().replace(" ","").trim())){                				               				       
                				    status="Kinldy check the Column Names & No of Column's "+columnValue.size();
                				    empReqExcel.put("RNo",Integer.toString(rowId));
	  	                    		empReqExcel.put("IssueStatus",status);
	  	                    		response.add(empReqExcel);
	  	                    		log.info("serviceEnd -UserId :" +userId);
	  	                    		return Response.ok(response, MediaType.APPLICATION_JSON).build();
                			}
                		}
                	}
				}              
            }
            log.info("Size"+guestExcelDetails.size());
			if(response.isEmpty()){
				if(!guestExcelDetails.isEmpty()){
					response=guestRecord(guestExcelDetails,branchId,combinedFacility);	
				}
			}

        } catch (Exception e) {log.info(" Error :" +e);
      		try{
        	if(e.getCause().toString().contains("InvalidFormatException")){
				status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
          		empReqExcel.put("IssueStatus",status);
          		response.add(empReqExcel);
          		log.info(e+" serviceEnd -UserId :" +userId);
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
        	}
      		}catch(Exception e1){
          		log.info(" Error :" +e1);
      		}
			status="Kinldy check the excel for any empty column,System will except only excel format without formula and special symbols.";  	                    		
      		empReqExcel.put("IssueStatus",status);
      		response.add(empReqExcel);
      		log.info(e+" serviceEnd -UserId :" +userId);
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
}
        log.info("serviceEnd -UserId :" +userId);
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    /*
     * @Reading employee Trip Request details from employee_request xl utility.
     * 
     * @Stored all the values on Arraylist.
     * 
     * @author Rajan R
     * 
     * @since 2015-05-12
     */
    @POST
    @Path("/employeeTravelTripRequest")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response employeeTravelRequest(@FormDataParam("filename") InputStream uploadedInputStream,
			@FormDataParam("filename") InputStream uploadedSizeInputStream,
            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
            @QueryParam("combinedFacility") String combinedFacility,
            @QueryParam("profileId") int profileId, @QueryParam("userRole") String userRole,
            @Context HttpServletRequest request) throws ParseException, IOException {
        String status = "success-";
        log.info("serviceStart -UserId :" +profileId);
        int noOfcolumn=12; 
        List<Map<String, Object>> response =new ArrayList<Map<String, Object>>();
    	Map<Integer, Object>  empReqDetails= new HashMap<Integer, Object>();
    	Map<String, Object>  empReqExcel= new HashMap<String, Object>();   
    	IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
    	Map<String, Object> responce = new HashMap<String, Object>();
    			
    	log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
    	try{
     	 	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),profileId))){
    			responce.put("status", "invalidRequest");
    			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    		}    	 	
     	 	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(profileId);
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
        try {
	           XSSFWorkbook workbook = new XSSFWorkbook(uploadedInputStream);
	           XSSFSheet sheet = workbook.getSheetAt(0);
	           
		   		if (!((fileDetail.getFileName().toLowerCase().endsWith(".xlsx")))){
					log.info("File Exctension");
					status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
	          		empReqExcel.put("IssueStatus",status);
	          		response.add(empReqExcel);
	                return Response.ok(response, MediaType.APPLICATION_JSON).build();
			        }
	           
		   		int bytes=getBytes(uploadedSizeInputStream).length;
		   		double kilobytes = (bytes / 1000);
		   		double megabytes = (kilobytes / 1000);
		     		log.info("bytes :" +bytes);
		     		log.info("kilobytes :" +Math.round(kilobytes));
		     		log.info("megabytes :" +Math.round(megabytes));
		           if(megabytes > 5){
		           	log.info("Inside Excel bigSize");
					status="Kinldy check the file Size,System will except a excel only maximum size 5MB.";  	                    		
	          		empReqExcel.put("IssueStatus",status);
	          		response.add(empReqExcel);
	                return Response.ok(response, MediaType.APPLICATION_JSON).build();
		           }	  

	           
	           int rowId=0;	          
	           for (int rowNum = 0; rowNum <=sheet.getLastRowNum(); rowNum++) {
	        	   ArrayList<Object> columnValue = new ArrayList<Object>();	                
	               rowId++;
	              Row rowValues = sheet.getRow(rowNum);
	              if (rowValues == null) {	     
	                 continue;
	              }
	              for (int cellNo = 0; cellNo < noOfcolumn; cellNo++) {
	                 Cell cell = rowValues.getCell(cellNo, Row.RETURN_BLANK_AS_NULL);	                 
	                 if (cell == null) {	                	
	                	 columnValue.add("CELL_TYPE_BLANK");
	                  }else{              	              
		                    switch (cell.getCellType()) {
		                    case Cell.CELL_TYPE_BOOLEAN:
		                        columnValue.add(cell.getBooleanCellValue());
		                        break;
		                    case Cell.CELL_TYPE_BLANK:
		                    	 columnValue.add("CELL_TYPE_BLANK");	
		             			break;                   		
		                    case Cell.CELL_TYPE_NUMERIC:
		                        if (DateUtil.isCellDateFormatted(cell)) {
		                            Date date = DateUtil.getJavaDate((double) cell.getNumericCellValue());
		                            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
		                            columnValue.add(df.format(date));
		                        }else {
		                            cell.setCellType(Cell.CELL_TYPE_STRING);
		                            columnValue.add(cell.getStringCellValue());
		                        }
		                        break;
		                    case Cell.CELL_TYPE_STRING:
		                        columnValue.add(cell.getStringCellValue().toString());
		                        break;
		                    case Cell.CELL_TYPE_FORMULA:
		                        columnValue.add("");
		                        break;
		                    default:
		                        columnValue.add("");
		                        break;
		                    }
	                 }
	              }
	              columnValue.removeAll(Arrays.asList(null, ""));                
	                if(!columnValue.isEmpty()){	                	
	                	if(rowId>1){                 		
                	if(columnValue.size()>=12){   
								if(!reqExcelValidator(Integer.toString(rowId),columnValue,branchId,combinedFacility).isEmpty()){                		
								response.addAll(reqExcelValidator(Integer.toString(rowId),columnValue,branchId,combinedFacility));
							}else{
								empReqDetails.put(rowId, columnValue);
							}
                		}else{
                				status="Kinldy check the no -of Column -12 - Entered Column No:"+columnValue.size();
                				empReqExcel.put("RNo",Integer.toString(rowId));
  	                    		empReqExcel.put("IssueStatus",status);
	                    		response.add(empReqExcel);
                	}             		
                		
                	}else{
                		  if(columnValue.size()<12){                			              		              			   	
  	                    		status="Kinldy check the no -of Column's ";
  	                    		empReqExcel.put("RNo",Integer.toString(rowId));
  	                    		empReqExcel.put("IssueStatus",status);
  	                    		response.add(empReqExcel);
  	                    	
                		}else{
								if(!"EmployeeId".equalsIgnoreCase(columnValue.get(0).toString().replace(" ","").trim()) 
                					|| !"EmployeeName".equalsIgnoreCase(columnValue.get(1).toString().replace(" ","").trim()) 
                					|| !"Gender".equalsIgnoreCase(columnValue.get(2).toString().replace(" ","").trim()) 
                					|| !"address".equalsIgnoreCase(columnValue.get(3).toString().replace(" ","").trim())
                					|| !"Areaname".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim())
                					|| !"PickUpTime/Sequencing".equalsIgnoreCase(columnValue.get(5).toString().replace(" ","").trim()) 
                        			|| !"TripType".equalsIgnoreCase(columnValue.get(6).toString().replace(" ","").trim()) 
                        			|| !"RouteName".equalsIgnoreCase(columnValue.get(7).toString().replace(" ","").trim()) 
                        			|| !"RequestDate".equalsIgnoreCase(columnValue.get(8).toString().replace(" ","").trim()) 
                        			|| !"ShiftTime".equalsIgnoreCase(columnValue.get(9).toString().replace(" ","").trim())
                        			|| !"NodalPointname".equalsIgnoreCase(columnValue.get(10).toString().replace(" ","").trim())
            					    || !"Facility".equalsIgnoreCase(columnValue.get(11).toString().replace(" ","").trim())){                		                    				    
                				    status="Kinldy check the Column Names & No of Column's"+columnValue.size();  	                    		
	  	                    		empReqExcel.put("RNo",Integer.toString(rowId));
	  	                    		empReqExcel.put("IssueStatus",status);
	  	                    		response.add(empReqExcel);
	  	                    		log.info("serviceEnd -UserId :" +profileId);
	  	                    		 return Response.ok(response, MediaType.APPLICATION_JSON).build();
                			}
                		}
                	}
				}              
            }
            log.info("Size"+empReqDetails.size());
			if(response.isEmpty()){
				if(!empReqDetails.isEmpty()){
					response=employeeTravelRequest(empReqDetails,branchId,profileId,combinedFacility);
				}
			}

        } catch (Exception e) {
      		log.info(" Error :" +e);
      		try{
        	if(e.getCause().toString().contains("InvalidFormatException")){
				status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
          		empReqExcel.put("IssueStatus",status);
          		response.add(empReqExcel);
          		log.info(e+" serviceEnd -UserId :" +profileId);
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
        	}
      		}catch(Exception e1){
          		log.info(" Error :" +e1);
      		}
			status="Kinldy check the excel for any empty column,System will except only excel format without formula and special symbols.";  	                    		
      		empReqExcel.put("IssueStatus",status);
      		response.add(empReqExcel);
      		log.info(e+" serviceEnd -UserId :" +profileId);
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        
        }
        log.info("serviceEnd -UserId :" +profileId);
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }
    
    
    
    /*
     * Request Creataion for Servion Client
     */
    
    @POST
    @Path("/travelRequestByProjectDetails")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response travelRequestByProjectDetails(@FormDataParam("filename") InputStream uploadedInputStream,
			@FormDataParam("filename") InputStream uploadedSizeInputStream,
            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
            @QueryParam("combinedFacility") String combinedFacility,
            @QueryParam("profileId") int profileId, @QueryParam("userRole") String userRole,
            @Context HttpServletRequest request) throws ParseException, IOException {
        String status = "success-";
        log.info("serviceStart -UserId :" +profileId);
        int noOfcolumn=13; 
        List<Map<String, Object>> response =new ArrayList<Map<String, Object>>();
    	Map<Integer, Object>  empReqDetails= new HashMap<Integer, Object>();
    	Map<String, Object>  empReqExcel= new HashMap<String, Object>();   
    	IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
    	Map<String, Object> responce = new HashMap<String, Object>();
    			
    	log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
    	try{
     	 	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),profileId))){

    			responce.put("status", "invalidRequest");
    			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    		}
     	 	
     	 	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(profileId);
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
        try {
	           XSSFWorkbook workbook = new XSSFWorkbook(uploadedInputStream);
	           XSSFSheet sheet = workbook.getSheetAt(0);
	           
		   		if (!((fileDetail.getFileName().toLowerCase().endsWith(".xlsx")))){
					log.info("File Exctension");
					status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
	          		empReqExcel.put("IssueStatus",status);
	          		response.add(empReqExcel);
	                return Response.ok(response, MediaType.APPLICATION_JSON).build();
			        }

	           
		   		int bytes=getBytes(uploadedSizeInputStream).length;
		   		double kilobytes = (bytes / 1000);
		   		double megabytes = (kilobytes / 1000);
		     		log.info("bytes :" +bytes);
		     		log.info("kilobytes :" +Math.round(kilobytes));
		     		log.info("megabytes :" +Math.round(megabytes));
		           if(megabytes > 5){
		           	log.info("Inside Excel bigSize");
					status="Kinldy check the file Size,System will except a excel only maximum size 5MB.";  	                    		
	          		empReqExcel.put("IssueStatus",status);
	          		response.add(empReqExcel);
	                return Response.ok(response, MediaType.APPLICATION_JSON).build();
		           }	  

	           
	           int rowId=0;	          
	           for (int rowNum = 0; rowNum <=sheet.getLastRowNum(); rowNum++) {
	        	   ArrayList<Object> columnValue = new ArrayList<Object>();	                
	               rowId++;
	              Row rowValues = sheet.getRow(rowNum);
	              if (rowValues == null) {	     
	                 continue;
	              }
	              for (int cellNo = 0; cellNo < noOfcolumn; cellNo++) {
	                 Cell cell = rowValues.getCell(cellNo, Row.RETURN_BLANK_AS_NULL);	                 
	                 if (cell == null) {	                	
	                	 columnValue.add("CELL_TYPE_BLANK");
	                  }else{              	              
		                    switch (cell.getCellType()) {
		                    case Cell.CELL_TYPE_BOOLEAN:
		                        columnValue.add(cell.getBooleanCellValue());
		                        break;
		                    case Cell.CELL_TYPE_BLANK:
		                    	 columnValue.add("CELL_TYPE_BLANK");	
		             			break;                   		
		                    case Cell.CELL_TYPE_NUMERIC:
		                        if (DateUtil.isCellDateFormatted(cell)) {
		                            Date date = DateUtil.getJavaDate((double) cell.getNumericCellValue());
		                            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
		                            columnValue.add(df.format(date));
		                        }else {
		                            cell.setCellType(Cell.CELL_TYPE_STRING);
		                            columnValue.add(cell.getStringCellValue());
		                        }
		                        break;
		                    case Cell.CELL_TYPE_STRING:
		                        columnValue.add(cell.getStringCellValue().toString());
		                        break;
		                    case Cell.CELL_TYPE_FORMULA:
		                        columnValue.add("");
		                        break;
		                    default:
		                        columnValue.add("");
		                        break;
		                    }
	                 }
	              }
	              columnValue.removeAll(Arrays.asList(null, ""));                
	                if(!columnValue.isEmpty()){	                	
	                	if(rowId>1){                 		
                	if(columnValue.size()>=13){   
								if(!reqProjectExcelValidator(Integer.toString(rowId),columnValue,branchId,profileId,combinedFacility).isEmpty()){                		
								response.addAll(reqProjectExcelValidator(Integer.toString(rowId),columnValue,branchId,profileId,combinedFacility));
							}else{
								empReqDetails.put(rowId, columnValue);
							}
                		}else{
                				status="Kinldy check the no -of Column -13 - Entered Column No:"+columnValue.size();
                				empReqExcel.put("RNo",Integer.toString(rowId));
  	                    		empReqExcel.put("IssueStatus",status);
	                    		response.add(empReqExcel);
                	}             		
                		
                	}else{
                		  if(columnValue.size()<13){                			              		              			   	
  	                    		status="Kinldy check the no -of Column's ";
  	                    		empReqExcel.put("RNo",Integer.toString(rowId));
  	                    		empReqExcel.put("IssueStatus",status);
  	                    		response.add(empReqExcel);
  	                    	
                		}else{
								if(!"EmployeeId".equalsIgnoreCase(columnValue.get(0).toString().replace(" ","").trim()) 
                					|| !"EmployeeName".equalsIgnoreCase(columnValue.get(1).toString().replace(" ","").trim()) 
                					|| !"Gender".equalsIgnoreCase(columnValue.get(2).toString().replace(" ","").trim()) 
                					|| !"address".equalsIgnoreCase(columnValue.get(3).toString().replace(" ","").trim())
                					|| !"Areaname".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim())
                					|| !"PickUpTime/Sequencing".equalsIgnoreCase(columnValue.get(5).toString().replace(" ","").trim()) 
                        			|| !"TripType".equalsIgnoreCase(columnValue.get(6).toString().replace(" ","").trim()) 
                        			|| !"RouteName".equalsIgnoreCase(columnValue.get(7).toString().replace(" ","").trim()) 
                        			|| !"RequestDate".equalsIgnoreCase(columnValue.get(8).toString().replace(" ","").trim()) 
                        			|| !"ShiftTime".equalsIgnoreCase(columnValue.get(9).toString().replace(" ","").trim())
                        			|| !"ReportingManagerEmployeeId".equalsIgnoreCase(columnValue.get(10).toString().replace(" ","").trim())
                        			|| !"ProjectId".equalsIgnoreCase(columnValue.get(11).toString().replace(" ","").trim())
                        			|| !"NodalPointname".equalsIgnoreCase(columnValue.get(12).toString().replace(" ","").trim())){   				
                			                   				                				       
                				    status="Kinldy check the Column Names & No of Column's"+columnValue.size();  	                    		
	  	                    		empReqExcel.put("RNo",Integer.toString(rowId));
	  	                    		empReqExcel.put("IssueStatus",status);
	  	                    		response.add(empReqExcel);
	  	                    		log.info("serviceEnd -UserId :" +profileId);
	  	                    		 return Response.ok(response, MediaType.APPLICATION_JSON).build();
                			}
                		}
                	}
				}              
            }
            log.info("Size"+empReqDetails.size());
			if(response.isEmpty()){
				if(!empReqDetails.isEmpty()){
					response=travelRequestByProjectDetails(empReqDetails,branchId,combinedFacility);
				}
			}

        } catch (Exception e) {

      		log.info(" Error :" +e);
      		try{
        	if(e.getCause().toString().contains("InvalidFormatException")){
				status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
          		empReqExcel.put("IssueStatus",status);
          		response.add(empReqExcel);
          		log.info(e+" serviceEnd -UserId :" +profileId);
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
        	}
      		}catch(Exception e1){
          		log.info(" Error :" +e1);
      		}
			status="Kinldy check the excel for any empty column,System will except only excel format without formula and special symbols.";  	                    		
      		empReqExcel.put("IssueStatus",status);
      		response.add(empReqExcel);
      		log.info(e+" serviceEnd -UserId :" +profileId);
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        
        }
        log.info("serviceEnd -UserId :" +profileId);
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }
    
    
    
    /*
     * @Reading employee Trip Request details from employee_request xl utility for genpact client.
     * 
     * @Stored all the values on Arraylist.
     * 
     * @author Sarfraz Khan
     * 
     * @since 2016-05-12
     */
    @POST
    @Path("/employeeTravelTrip")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response employeeTravelRequestForGenpact(@FormDataParam("filename") InputStream uploadedInputStream,
			@FormDataParam("filename") InputStream uploadedSizeInputStream,
            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
            @QueryParam("combinedFacility") String combinedFacility,
            @QueryParam("profileId") int profileId, @QueryParam("userRole") String userRole,
            @Context HttpServletRequest request) throws ParseException, IOException {
        String status = "success-";
        log.info("serviceStart -UserId :" +profileId);
        int noOfcolumn=8; 
        List<Map<String, Object>> response =new ArrayList<Map<String, Object>>();    
    	Map<Integer, Object>  empReqDetails= new HashMap<Integer, Object>();
    	Map<String, Object>  empReqExcel= new HashMap<String, Object>();
    	IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
    	Map<String, Object> responce = new HashMap<String, Object>();
    			
    	log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
    	try{
     	 	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),profileId))){
    			responce.put("status", "invalidRequest");
    			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
    		}
     	 	
     	 	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(profileId);
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
        try {
        	 XSSFWorkbook workbook = new XSSFWorkbook(uploadedInputStream);   
	         XSSFSheet sheet = workbook.getSheetAt(0);
	         
	         if (!((fileDetail.getFileName().toLowerCase().endsWith(".xlsx")))){
					log.info("File Exctension");
					status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
	          		empReqExcel.put("IssueStatus",status);
	          		response.add(empReqExcel);
	                return Response.ok(response, MediaType.APPLICATION_JSON).build();
			        }
	         
	         
	   		int bytes=getBytes(uploadedSizeInputStream).length;
	   		double kilobytes = (bytes / 1000);
	   		double megabytes = (kilobytes / 1000);
	     		log.info("bytes :" +bytes);
	     		log.info("kilobytes :" +Math.round(kilobytes));
	     		log.info("megabytes :" +Math.round(megabytes));
	           if(megabytes > 5){
	           	log.info("Inside Excel bigSize");
				status="Kinldy check the file Size,System will except a excel only maximum size 5MB.";  	                    		
          		empReqExcel.put("IssueStatus",status);
          		response.add(empReqExcel);
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
	           }	  
	   			           
	           int rowId=0;	          
	           for (int rowNum = 0; rowNum <=sheet.getLastRowNum(); rowNum++) {
	        	   ArrayList<Object> columnValue = new ArrayList<Object>();	                
	               rowId++;
	              Row rowValues = sheet.getRow(rowNum);
	              if (rowValues == null) {	     
	                 continue;
	              }
	              for (int cellNo = 0; cellNo < noOfcolumn; cellNo++) {
	                 Cell cell = rowValues.getCell(cellNo, Row.RETURN_BLANK_AS_NULL);	                 
	                 if (cell == null) {	                	
	                	 columnValue.add("CELL_TYPE_BLANK");
	                  }else{              	              
		                    switch (cell.getCellType()) {
		                    case Cell.CELL_TYPE_BOOLEAN:
		                        columnValue.add(cell.getBooleanCellValue());
		                        break;
		                    case Cell.CELL_TYPE_BLANK:
		                    	 columnValue.add("CELL_TYPE_BLANK");	
		             			break;                   		
		                    case Cell.CELL_TYPE_NUMERIC:
		                        if (DateUtil.isCellDateFormatted(cell)) {
		                            Date date = DateUtil.getJavaDate((double) cell.getNumericCellValue());
		                            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
		                            columnValue.add(df.format(date));
		                        }else {
		                            cell.setCellType(Cell.CELL_TYPE_STRING);
		                            columnValue.add(cell.getStringCellValue());
		                        }
		                        break;
		                    case Cell.CELL_TYPE_STRING:
		                        columnValue.add(cell.getStringCellValue().toString());
		                        break;
		                    case Cell.CELL_TYPE_FORMULA:
		                        columnValue.add("");
		                        break;
		                    default:
		                        columnValue.add("");
		                        break;
		                    }
	                 }
	              }
	              columnValue.removeAll(Arrays.asList(null, ""));                
	                if(!columnValue.isEmpty()){	                	
	                	if(rowId>1){                 		
                	if(columnValue.size()>=8){   
								if(!genpactExcelValidator(Integer.toString(rowId),columnValue,branchId,combinedFacility).isEmpty()){                		
								response.addAll(genpactExcelValidator(Integer.toString(rowId),columnValue,branchId,combinedFacility));
							}else{
								empReqDetails.put(rowId, columnValue);
							}
                		}else{
                				status="Kinldy check the no -of Column -8 - Entered Column No:"+columnValue.size();
                				empReqExcel.put("RNo",Integer.toString(rowId));
  	                    		empReqExcel.put("IssueStatus",status);
	                    		response.add(empReqExcel);
                	}             		
                		
                	}else{
                		  if(columnValue.size()<8){                			              		              			   	
  	                    		status="Kinldy check the no -of Column's ";
  	                    		empReqExcel.put("RNo",Integer.toString(rowId));
  	                    		empReqExcel.put("IssueStatus",status);
  	                    		response.add(empReqExcel);
  	                    	
                		}else{
								if(!"EmployeeId".equalsIgnoreCase(columnValue.get(0).toString().replace(" ","").trim()) 
                					|| !"EmployeeName".equalsIgnoreCase(columnValue.get(1).toString().replace(" ","").trim()) 
                					|| !"address".equalsIgnoreCase(columnValue.get(2).toString().replace(" ","").trim())
                					|| !"PickUpTime/Sequencing".equalsIgnoreCase(columnValue.get(3).toString().replace(" ","").trim()) 
                        			|| !"TripType".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim()) 
                        			|| !"RequestDate".equalsIgnoreCase(columnValue.get(5).toString().replace(" ","").trim()) 
                        			|| !"ShiftTime".equalsIgnoreCase(columnValue.get(6).toString().replace(" ","").trim())
            					    || !"Facility".equalsIgnoreCase(columnValue.get(7).toString().replace(" ","").trim())){                		                    				    
									status="Kinldy check the Column Names & No of Column's"+columnValue.size();  	                    		
	  	                    		empReqExcel.put("RNo",Integer.toString(rowId));
	  	                    		empReqExcel.put("IssueStatus",status);
	  	                    		response.add(empReqExcel);
	  	                    		log.info("serviceEnd -UserId :" +profileId);
	  	                    		 return Response.ok(response, MediaType.APPLICATION_JSON).build();
                			}
                		}
                	}
				}              
            }
            log.info("Size"+empReqDetails.size());
			if(response.isEmpty()){
				if(!empReqDetails.isEmpty()){
					response=genpactEmployeeTravelRequest(empReqDetails,branchId,combinedFacility,profileId);
				}
			}

        } catch (Exception e) {
      		log.info(" Error :" +e);
      		try{
        	if(e.getCause().toString().contains("InvalidFormatException")){
				status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
          		empReqExcel.put("IssueStatus",status);
          		response.add(empReqExcel);
          		log.info(e+" serviceEnd -UserId :" +profileId);
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
        	}
      		}catch(Exception e1){
          		log.info(" Error :" +e1);
      		}
			status="Kinldy check the excel for any empty column,System will except only excel format without formula and special symbols.";  	                    		
      		empReqExcel.put("IssueStatus",status);
      		response.add(empReqExcel);
      		log.info(e+" serviceEnd -UserId :" +profileId);
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        log.info("serviceEnd -UserId :" +profileId);
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }
    
    private List<Map<String, Object>> genpactExcelValidator(String rowId,ArrayList<Object> columnValue,int branchId,String combinedFacility){
	    IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");		   
	    List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();    	        	
	    DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	    DateFormat dateHypenFormat = new SimpleDateFormat("dd-MM-yyyy");
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	    DateFormat shifTimeFormate = new SimpleDateFormat("HH:mm");	
	    String regex ="[^&%$#@!~]*";
		
	    if (columnValue.get(0).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(0).toString() ==null || columnValue.get(0).toString().replace(".0","").trim().equalsIgnoreCase("") 
					|| columnValue.get(0).toString().isEmpty()) {
	    	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();	
				String status="kindly check the employee Id : "+columnValue.get(0).toString();
				empReqExcelRows.put("RNo",rowId.concat(",0"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
			}else{
				List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO.getEmpDetailsFromEmployeeId(columnValue.get(0).toString().trim());
			    if (employeeDetails.isEmpty()) {
			    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			    	String status = "Employee Id is not available or remove space as well from employeeId -" + columnValue.get(0).toString().trim();
			    	empReqExcelRows.put("RNo",rowId.concat(",0"));
			    	empReqExcelRows.put("IssueStatus",status);	
			    	childRowResponse.add(empReqExcelRows);
			    } 
			}
		
//	    if (columnValue.get(2).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(2).toString() ==null || columnValue.get(2).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
//					|| columnValue.get(2).toString().isEmpty() ) {	
//	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
//				String status="kindly check the employee gender : "+columnValue.get(2).toString();
//				empReqExcelRows.put("RNo",rowId.concat(",2"));
//		    	empReqExcelRows.put("IssueStatus",status);
//		    	childRowResponse.add(empReqExcelRows);
//			}else if (!("Female".equalsIgnoreCase(columnValue.get(2).toString().trim()) || "Male".equalsIgnoreCase(columnValue.get(2).toString().trim()))){
//				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
//				String	status="kindly check the employee gender value : "+columnValue.get(2).toString();
//				empReqExcelRows.put("RNo",rowId.concat(",2"));
//		    	empReqExcelRows.put("IssueStatus",status);
//		    	childRowResponse.add(empReqExcelRows);
//			}
		
//	    if (columnValue.get(4).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(4).toString() ==null || columnValue.get(4).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
//					|| columnValue.get(4).toString().isEmpty() ) {
//	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
//				String status="kindly check the AreaName : "+columnValue.get(4).toString();
//				empReqExcelRows.put("RNo",rowId.concat(",4"));
//		    	empReqExcelRows.put("IssueStatus",status);
//		    	childRowResponse.add(empReqExcelRows);
//			}
			
	    if (columnValue.get(4).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(4).toString() ==null || columnValue.get(4).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(4).toString().isEmpty() || !("PICKUP".equalsIgnoreCase(columnValue.get(4).toString().trim()) || "DROP".equalsIgnoreCase(columnValue.get(4).toString().trim()))) {
	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the TripType : "+columnValue.get(4).toString();
				empReqExcelRows.put("RNo",rowId.concat(",4"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
			}	
						
	    if (columnValue.get(3).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(3).toString() ==null || columnValue.get(3).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(3).toString().isEmpty() ) {
	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the employee pickup Time /Drop sequence Number : "+columnValue.get(3).toString();
				empReqExcelRows.put("RNo",rowId.concat(",3"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);				
			}else{ 			
				if("PICKUP".equalsIgnoreCase(columnValue.get(4).toString().trim())){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();	
					try{
						String shiftPickUpTime = columnValue.get(3).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());						
					}catch (Exception e){
						log.info("Error"+e);
						String status="kindly check the employee Pickup Time format:'HH:MM:SS' ex:16:00:00"+e;
						empReqExcelRows.put("RNo",rowId.concat(",3"));
				    	empReqExcelRows.put("IssueStatus",status);
				    	childRowResponse.add(empReqExcelRows);
					}						
				}else if("DROP".equalsIgnoreCase(columnValue.get(4).toString().trim())){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					if (!columnValue.get(3).toString().matches(regex)){							
						String status="kindly check the employee drop Sequence like 1,2,3,4 etc";
						empReqExcelRows.put("RNo",rowId.concat(",3"));
				    	empReqExcelRows.put("IssueStatus",status);
				    	childRowResponse.add(empReqExcelRows);
					}	
				}						
			}	
//	    if (columnValue.get(7).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(7).toString() ==null || columnValue.get(7).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
//					|| columnValue.get(7).toString().isEmpty() ) {
//	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
//				String status="kindly check the routeName : "+columnValue.get(7).toString().trim();
//				empReqExcelRows.put("RNo",rowId.concat(",7"));
//		    	empReqExcelRows.put("IssueStatus",status);
//		    	childRowResponse.add(empReqExcelRows);
//			}
//	    else if (!columnValue.get(10).toString().trim().equalsIgnoreCase("default")){					   					    
//				    routeExistDetail = iRouteDetailBO.getNonNodalRouteNameFromClientIdAndRouteName(branchId,
//				                columnValue.get(7).toString().trim());					   			
//				    if (routeExistDetail.isEmpty()
//				            && (columnValue.get(10).toString().trim().equalsIgnoreCase("default"))) {
//				    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
//				    	String status = "Route Name not available -" + columnValue.get(7).toString().trim();					       
//				    	empReqExcelRows.put("RNo",rowId.concat(",7"));
//				    	empReqExcelRows.put("IssueStatus",status);
//				    	childRowResponse.add(empReqExcelRows);
//				    }
//			}
			
	    if (columnValue.get(5).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(5).toString() ==null || columnValue.get(5).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(5).toString().isEmpty() ) {
	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the RequestDate: "+columnValue.get(5).toString();
				empReqExcelRows.put("RNo",rowId.concat(",5"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
				
			}else{
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				try{						
					    String shiftTimeInDateTimeFormate = columnValue.get(5).toString();
					    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
						String shiftTimeInDate = columnValue.get(6).toString();
					    String shifAfterTimeSplit[] = shiftTimeInDate.split("\\s+");
					    java.sql.Time finalShiftTime = new java.sql.Time(shifTimeFormate.parse(shifAfterTimeSplit[1]).getTime());					    					   
					    Date requestDate = dateFormat.parse(shifTimeSplit[0]);						    
					    String reqDate = dateHypenFormat.format(requestDate) + " " + shifTimeFormate.format(finalShiftTime);
						if (dateTimeFormate.parse(reqDate).getTime() <= new Date().getTime()) {
							String status ="RequestDate Should not less than todayDate and ShifTime" + columnValue.get(5).toString();
							empReqExcelRows.put("RNo",rowId.concat(",5"));
					    	empReqExcelRows.put("IssueStatus",status);
					    	childRowResponse.add(empReqExcelRows);
					    }	
					}catch (Exception e){
						log.info("Error"+e);

						String status="kindly check the employee request Date format 'DD-MM-YYYY' "+e;
						empReqExcelRows.put("RNo",rowId.concat(",5"));
				    	empReqExcelRows.put("IssueStatus",status);
				    	childRowResponse.add(empReqExcelRows);
					}
			}				
	    if (columnValue.get(6).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(6).toString() ==null || columnValue.get(6).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(6).toString().isEmpty() ) {
	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the shiftTime : "+columnValue.get(6).toString();
				empReqExcelRows.put("RNo",rowId.concat(",7"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
			}else{
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				try{
					String shiftTimeInDateTimeFormate = columnValue.get(6).toString();
				    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
				    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());					    
					}catch (Exception e){
						log.info("Error"+e);
						String status="kindly check the employee shiftTime Time format:'HH:MM:SS' ex:16:00:00"+e;
						empReqExcelRows.put("RNo",rowId.concat(",6"));
				    	empReqExcelRows.put("IssueStatus",status);
				    	childRowResponse.add(empReqExcelRows);
					}
			}
	    
//	    if (columnValue.get(8).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(8).toString() ==null || columnValue.get(8).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
//					|| columnValue.get(8).toString().isEmpty() ) {
//	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
//				String status="kindly check the NodalPointName : "+columnValue.get(8).toString();
//				empReqExcelRows.put("RNo",rowId.concat(",8"));
//		    	empReqExcelRows.put("IssueStatus",status);
//		    	childRowResponse.add(empReqExcelRows);
//			}else if (!columnValue.get(8).toString().trim().equalsIgnoreCase("default")){
//				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
//				 List<EFmFmAreaNodalMasterPO> nodalPointExistDetail = iRouteDetailBO
//				            .getParticularNodalPointNameDetails(columnValue.get(8).toString().trim());
//				 
//				    if (nodalPointExistDetail.isEmpty()) {
//				    	String status = "Nodal Point not available-" + columnValue.get(8).toString().trim();
//				       	empReqExcelRows.put("RNo",rowId.concat(",8"));
//				    	empReqExcelRows.put("IssueStatus",status);
//				    	childRowResponse.add(empReqExcelRows);
//				        
//				    }
//			}
	    
			return childRowResponse;
    }
    
    private List<Map<String, Object>> reqExcelValidator(String rowId,ArrayList<Object> columnValue,int branchId,String combinedFacility){
    	    IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");		   
		    IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
			 IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
		        IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		    List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();    	        	
    	    DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		    DateFormat dateHypenFormat = new SimpleDateFormat("dd-MM-yyyy");
		    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		    DateFormat shifTimeFormate = new SimpleDateFormat("HH:mm");	
		    List<EFmFmZoneMasterPO> routeExistDetail;    	  
		    String regex ="[^&%$#@!~]*";
		    
		    
		    if (columnValue.get(0).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(0).toString() ==null || columnValue.get(0).toString().replace(".0","").trim().equalsIgnoreCase("") 
						|| columnValue.get(0).toString().isEmpty()) {
		    	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();	
					String status="kindly check the employee Id : "+columnValue.get(0).toString();
					empReqExcelRows.put("RNo",rowId.concat(",0"));
			    	empReqExcelRows.put("IssueStatus",status);
			    	childRowResponse.add(empReqExcelRows);
				}else{
					
			    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO.getEmpDetailsFromEmployeeId(columnValue.get(0).toString().trim());
					 if (employeeDetails.isEmpty()) {
					    	String status = "Employee Id is not available or remove space as well from employeeId -" + columnValue.get(0).toString().trim();
					    	empReqExcelRows.put("RNo",rowId.concat(",0"));
					    	empReqExcelRows.put("IssueStatus",status);	
					    	childRowResponse.add(empReqExcelRows);
					    } 				   
				}
			
		    if (columnValue.get(2).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(2).toString() ==null || columnValue.get(2).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(2).toString().isEmpty() ) {	
		    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the employee sex : "+columnValue.get(2).toString();
					empReqExcelRows.put("RNo",rowId.concat(",2"));
			    	empReqExcelRows.put("IssueStatus",status);
			    	childRowResponse.add(empReqExcelRows);
				}else if (!("Female".equalsIgnoreCase(columnValue.get(2).toString().trim()) || "Male".equalsIgnoreCase(columnValue.get(2).toString().trim()))){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String	status="kindly check the employee sex value : "+columnValue.get(2).toString();
					empReqExcelRows.put("RNo",rowId.concat(",2"));
			    	empReqExcelRows.put("IssueStatus",status);
			    	childRowResponse.add(empReqExcelRows);
				}
			
		    if (columnValue.get(4).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(4).toString() ==null || columnValue.get(4).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(4).toString().isEmpty() ) {
		    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the AreaName : "+columnValue.get(4).toString();
					empReqExcelRows.put("RNo",rowId.concat(",4"));
			    	empReqExcelRows.put("IssueStatus",status);
			    	childRowResponse.add(empReqExcelRows);
				}
				
		    if (columnValue.get(6).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(6).toString() ==null || columnValue.get(6).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(6).toString().isEmpty() || !("PICKUP".equalsIgnoreCase(columnValue.get(6).toString().trim()) || "DROP".equalsIgnoreCase(columnValue.get(6).toString().trim()))) {
		    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the TripType : "+columnValue.get(6).toString();
					empReqExcelRows.put("RNo",rowId.concat(",6"));
			    	empReqExcelRows.put("IssueStatus",status);
			    	childRowResponse.add(empReqExcelRows);
				}	
							
		    if (columnValue.get(5).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(5).toString() ==null || columnValue.get(5).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(5).toString().isEmpty() ) {
		    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the employee pickup Time /Drop sequence Number : "+columnValue.get(5).toString();
					empReqExcelRows.put("RNo",rowId.concat(",5"));
			    	empReqExcelRows.put("IssueStatus",status);
			    	childRowResponse.add(empReqExcelRows);				
				}else{ 			
					if("PICKUP".equalsIgnoreCase(columnValue.get(6).toString().trim())){
						Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();	
						try{
							String shiftPickUpTime = columnValue.get(5).toString();
				            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
				            java.sql.Time pickUpTime = new java.sql.Time(shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());						
						}catch (Exception e){
							log.info("Error"+e);
							String status="kindly check the employee Pickup Time format:'HH:MM:SS' ex:16:00:00"+e;
							empReqExcelRows.put("RNo",rowId.concat(",5"));
					    	empReqExcelRows.put("IssueStatus",status);
					    	childRowResponse.add(empReqExcelRows);
						}						
					}else if("DROP".equalsIgnoreCase(columnValue.get(6).toString().trim())){
						Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
						if (!columnValue.get(5).toString().matches(regex)){							
							String status="kindly check the employee drop Sequence like 1,2,3,4 etc";
							empReqExcelRows.put("RNo",rowId.concat(",5"));
					    	empReqExcelRows.put("IssueStatus",status);
					    	childRowResponse.add(empReqExcelRows);
						}	
					}						
				}	
		    if (columnValue.get(7).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(7).toString() ==null || columnValue.get(7).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(7).toString().isEmpty() ) {
		    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the routeName : "+columnValue.get(7).toString().trim();
					empReqExcelRows.put("RNo",rowId.concat(",7"));
			    	empReqExcelRows.put("IssueStatus",status);
			    	childRowResponse.add(empReqExcelRows);
				}else if (!columnValue.get(10).toString().trim().equalsIgnoreCase("default")){					   					    
					    routeExistDetail = iRouteDetailBO.getNonNodalRouteNameFromClientIdAndRouteName(combinedFacility,
					                columnValue.get(7).toString().trim());					   			
					    if (routeExistDetail.isEmpty()
					            && (columnValue.get(10).toString().trim().equalsIgnoreCase("default"))) {
					    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					    	String status = "Route Name not available -" + columnValue.get(7).toString().trim();					       
					    	empReqExcelRows.put("RNo",rowId.concat(",7"));
					    	empReqExcelRows.put("IssueStatus",status);
					    	childRowResponse.add(empReqExcelRows);
					    }
				}
				
		    if (columnValue.get(8).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(8).toString() ==null || columnValue.get(8).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(8).toString().isEmpty() ) {
		    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the RequestDate: "+columnValue.get(8).toString();
					empReqExcelRows.put("RNo",rowId.concat(",8"));
			    	empReqExcelRows.put("IssueStatus",status);
			    	childRowResponse.add(empReqExcelRows);
					
				}else{
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					try{						
//						    String shiftTimeInDateTimeFormate = columnValue.get(9).toString();
//						    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
//						    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());
//						  
//						    Date requestDate = dateFormat.parse(columnValue.get(8).toString());						   
//						    String reqDate = dateHypenFormat.format(requestDate) + " " + shifTimeFormate.format(shiftTime);	
						
						
						    String shiftTimeInDateTimeFormate = columnValue.get(8).toString();
						    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
						    
							String shiftTimeInDate = columnValue.get(9).toString();
						    String shifAfterTimeSplit[] = shiftTimeInDate.split("\\s+");
						    java.sql.Time finalShiftTime = new java.sql.Time(shifTimeFormate.parse(shifAfterTimeSplit[1]).getTime());					    						   
						    Date requestDate = dateFormat.parse(shifTimeSplit[0]);							    
						    String reqDate = dateHypenFormat.format(requestDate) + " " + shifTimeFormate.format(finalShiftTime);
						    
						    
						    
							if (dateTimeFormate.parse(reqDate).getTime() <= new Date().getTime()) {
								String status ="RequestDate Should not less than todayDate and ShifTime" + columnValue.get(8).toString();
								empReqExcelRows.put("RNo",rowId.concat(",8"));
						    	empReqExcelRows.put("IssueStatus",status);
						    	childRowResponse.add(empReqExcelRows);
						    }	
						}catch (Exception e){
							log.info("Error"+e);

							String status="kindly check the employee request Date format 'DD-MM-YYYY' "+e;
							empReqExcelRows.put("RNo",rowId.concat(",8"));
					    	empReqExcelRows.put("IssueStatus",status);
					    	childRowResponse.add(empReqExcelRows);
						}
				}				
		    if (columnValue.get(9).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(9).toString() ==null || columnValue.get(9).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(9).toString().isEmpty() ) {
		    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the shiftTime : "+columnValue.get(9).toString();
					empReqExcelRows.put("RNo",rowId.concat(",9"));
			    	empReqExcelRows.put("IssueStatus",status);
			    	childRowResponse.add(empReqExcelRows);
				}else{
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					try{
						String shiftTimeInDateTimeFormate = columnValue.get(9).toString();
					    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
					    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());					    
						}catch (Exception e){
							log.info("Error"+e);
							String status="kindly check the employee shiftTime Time format:'HH:MM:SS' ex:16:00:00"+e;
							empReqExcelRows.put("RNo",rowId.concat(",9"));
					    	empReqExcelRows.put("IssueStatus",status);
					    	childRowResponse.add(empReqExcelRows);
						}
				}			
		    if (columnValue.get(10).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(10).toString() ==null || columnValue.get(10).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(10).toString().isEmpty() ) {
		    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the NodalPointName : "+columnValue.get(10).toString();
					empReqExcelRows.put("RNo",rowId.concat(",10"));
			    	empReqExcelRows.put("IssueStatus",status);
			    	childRowResponse.add(empReqExcelRows);
				}else if (!columnValue.get(10).toString().trim().equalsIgnoreCase("default")){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					 List<EFmFmAreaNodalMasterPO> nodalPointExistDetail = iRouteDetailBO
					            .getParticularNodalPointNameDetails(columnValue.get(10).toString().trim());
					 
					    if (nodalPointExistDetail.isEmpty()) {
					    	String status = "Nodal Point not available-" + columnValue.get(10).toString().trim();
					       	empReqExcelRows.put("RNo",rowId.concat(",10"));
					    	empReqExcelRows.put("IssueStatus",status);
					    	childRowResponse.add(empReqExcelRows);
					        
					    }
				}
				return childRowResponse;
    }
    
    /*
     * Servion request Master Data validation
     */
    
    private List<Map<String, Object>> reqProjectExcelValidator(String rowId,ArrayList<Object> columnValue,int branchId,int reportingManagerUserId,String combinedFacility){
	    IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");		   
	    IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		List<EFmFmClientBranchPO> clientBranch = userMasterBO.getClientDetails(String.valueOf(branchId));
	    List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();    	        	
	    DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	    DateFormat dateHypenFormat = new SimpleDateFormat("dd-MM-yyyy");
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	    DateFormat shifTimeFormate = new SimpleDateFormat("HH:mm");	
	    List<EFmFmZoneMasterPO> routeExistDetail;    	  
	    String regex ="[^&%$#@!~]*";
		int repUserId=0,empUserId=0;
	    if (columnValue.get(0).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(0).toString() ==null || columnValue.get(0).toString().replace(".0","").trim().equalsIgnoreCase("") 
					|| columnValue.get(0).toString().isEmpty()) {
	    	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();	
				String status="kindly check the employee Id : "+columnValue.get(0).toString();
				empReqExcelRows.put("RNo",rowId.concat(",0"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
			}else{
				List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO.getEmpDetailsFromEmployeeId(columnValue.get(0).toString().trim());
			    if (employeeDetails.isEmpty()) {
			    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			    	String status = "Employee Id is not available or remove space as well from employeeId -" + columnValue.get(0).toString().trim();
			    	empReqExcelRows.put("RNo",rowId.concat(",0"));
			    	empReqExcelRows.put("IssueStatus",status);	
			    	childRowResponse.add(empReqExcelRows);
			    }else{
			    	empUserId=employeeDetails.get(0).getUserId();
			    }
			    
			}
		
	    if (columnValue.get(2).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(2).toString() ==null || columnValue.get(2).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(2).toString().isEmpty() ) {	
	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the employee sex : "+columnValue.get(2).toString();
				empReqExcelRows.put("RNo",rowId.concat(",2"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
			}else if (!("Female".equalsIgnoreCase(columnValue.get(2).toString().trim()) || "Male".equalsIgnoreCase(columnValue.get(2).toString().trim()))){
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String	status="kindly check the employee sex value : "+columnValue.get(2).toString();
				empReqExcelRows.put("RNo",rowId.concat(",2"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
			}
		
	    if (columnValue.get(4).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(4).toString() ==null || columnValue.get(4).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(4).toString().isEmpty() ) {
	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the AreaName : "+columnValue.get(4).toString();
				empReqExcelRows.put("RNo",rowId.concat(",4"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
			}
			
	    if (columnValue.get(6).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(6).toString() ==null || columnValue.get(6).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(6).toString().isEmpty() || !("PICKUP".equalsIgnoreCase(columnValue.get(6).toString().trim()) || "DROP".equalsIgnoreCase(columnValue.get(6).toString().trim()))) {
	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the TripType : "+columnValue.get(6).toString();
				empReqExcelRows.put("RNo",rowId.concat(",6"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
			}	
						
	    if (columnValue.get(5).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(5).toString() ==null || columnValue.get(5).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(5).toString().isEmpty() ) {
	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the employee pickup Time /Drop sequence Number : "+columnValue.get(5).toString();
				empReqExcelRows.put("RNo",rowId.concat(",5"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);				
			}else{ 			
				if("PICKUP".equalsIgnoreCase(columnValue.get(6).toString().trim())){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();	
					try{
						String shiftPickUpTime = columnValue.get(5).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());						
					}catch (Exception e){
						log.info("Error"+e);
						String status="kindly check the employee Pickup Time format:'HH:MM:SS' ex:16:00:00"+e;
						empReqExcelRows.put("RNo",rowId.concat(",5"));
				    	empReqExcelRows.put("IssueStatus",status);
				    	childRowResponse.add(empReqExcelRows);
					}						
				}else if("DROP".equalsIgnoreCase(columnValue.get(6).toString().trim())){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					if (!columnValue.get(5).toString().matches(regex)){							
						String status="kindly check the employee drop Sequence like 1,2,3,4 etc";
						empReqExcelRows.put("RNo",rowId.concat(",5"));
				    	empReqExcelRows.put("IssueStatus",status);
				    	childRowResponse.add(empReqExcelRows);
					}	
				}						
			}	
	    if (columnValue.get(7).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(7).toString() ==null || columnValue.get(7).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(7).toString().isEmpty() ) {
	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the routeName : "+columnValue.get(7).toString().trim();
				empReqExcelRows.put("RNo",rowId.concat(",7"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
			}else if (!columnValue.get(10).toString().trim().equalsIgnoreCase("default")){					   					    
				    routeExistDetail = iRouteDetailBO.getNonNodalRouteNameFromClientIdAndRouteName(combinedFacility,
				                columnValue.get(7).toString().trim());					   			
				    if (routeExistDetail.isEmpty()
				            && (columnValue.get(10).toString().trim().equalsIgnoreCase("default"))) {
				    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				    	String status = "Route Name not available -" + columnValue.get(7).toString().trim();					       
				    	empReqExcelRows.put("RNo",rowId.concat(",7"));
				    	empReqExcelRows.put("IssueStatus",status);
				    	childRowResponse.add(empReqExcelRows);
				    }
			}
			
	    if (columnValue.get(8).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(8).toString() ==null || columnValue.get(8).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(8).toString().isEmpty() ) {
	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the RequestDate: "+columnValue.get(8).toString();
				empReqExcelRows.put("RNo",rowId.concat(",8"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
				
			}else{
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				try{						
//					    String shiftTimeInDateTimeFormate = columnValue.get(9).toString();
//					    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
//					    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());
//					  
//					    Date requestDate = dateFormat.parse(columnValue.get(8).toString());						   
//					    String reqDate = dateHypenFormat.format(requestDate) + " " + shifTimeFormate.format(shiftTime);	
					
					
					    String shiftTimeInDateTimeFormate = columnValue.get(8).toString();
					    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
					    
						String shiftTimeInDate = columnValue.get(9).toString();
					    String shifAfterTimeSplit[] = shiftTimeInDate.split("\\s+");
					    java.sql.Time finalShiftTime = new java.sql.Time(shifTimeFormate.parse(shifAfterTimeSplit[1]).getTime());					    						   
					    Date requestDate = dateFormat.parse(shifTimeSplit[0]);							    
					    String reqDate = dateHypenFormat.format(requestDate) + " " + shifTimeFormate.format(finalShiftTime);
					    
					    
					    
						if (dateTimeFormate.parse(reqDate).getTime() <= new Date().getTime()) {
							String status ="RequestDate Should not less than todayDate and ShifTime" + columnValue.get(8).toString();
							empReqExcelRows.put("RNo",rowId.concat(",8"));
					    	empReqExcelRows.put("IssueStatus",status);
					    	childRowResponse.add(empReqExcelRows);
					    }	
					}catch (Exception e){
						log.info("Error"+e);

						String status="kindly check the employee request Date format 'DD-MM-YYYY' "+e;
						empReqExcelRows.put("RNo",rowId.concat(",8"));
				    	empReqExcelRows.put("IssueStatus",status);
				    	childRowResponse.add(empReqExcelRows);
					}
			}				
	    if (columnValue.get(9).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(9).toString() ==null || columnValue.get(9).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(9).toString().isEmpty() ) {
	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the shiftTime : "+columnValue.get(9).toString();
				empReqExcelRows.put("RNo",rowId.concat(",9"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
			}else{
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				try{
					String shiftTimeInDateTimeFormate = columnValue.get(9).toString();
				    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
				    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());					    
					}catch (Exception e){
						log.info("Error"+e);
						String status="kindly check the employee shiftTime Time format:'HH:MM:SS' ex:16:00:00"+e;
						empReqExcelRows.put("RNo",rowId.concat(",9"));
				    	empReqExcelRows.put("IssueStatus",status);
				    	childRowResponse.add(empReqExcelRows);
					}
			}
	    
	    if (columnValue.get(10).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(10).toString() ==null || columnValue.get(10).toString().replace(".0","").trim().equalsIgnoreCase("") 
				|| columnValue.get(10).toString().isEmpty()) {
    	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();	
			String status="kindly check the Reporting Manager employee Id : "+columnValue.get(10).toString();
			empReqExcelRows.put("RNo",rowId.concat(",10"));
	    	empReqExcelRows.put("IssueStatus",status);
	    	childRowResponse.add(empReqExcelRows);
		}else{
			List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO.getEmpDetailsFromEmployeeId(columnValue.get(10).toString().trim());
		    if (employeeDetails.isEmpty()) {
		    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
		    	String status = " Reporting ManagerEmployee Id is not available or remove space as well from employeeId -" + columnValue.get(10).toString().trim();
		    	empReqExcelRows.put("RNo",rowId.concat(",10"));
		    	empReqExcelRows.put("IssueStatus",status);	
		    	childRowResponse.add(empReqExcelRows);
		    }else{
		    	repUserId=employeeDetails.get(0).getUserId();
		    }
		}
	    
		    if (columnValue.get(11).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(11).toString() ==null || columnValue.get(11).toString().replace(".0","").trim().equalsIgnoreCase("") 
					|| columnValue.get(11).toString().isEmpty()) {
	    	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();	
				String status="kindly check the Project Id : "+columnValue.get(11).toString();
				empReqExcelRows.put("RNo",rowId.concat(",11"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
			}else{
				List<EFmFmClientProjectDetailsPO> projectDetails = iEmployeeDetailBO.getProjectDetails(columnValue.get(11).toString().trim(),combinedFacility);
			    if (projectDetails.isEmpty()) {
			    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			    	String status = "Project Id is not available or remove space as well from ProjectId -" + columnValue.get(11).toString().trim();
			    	empReqExcelRows.put("RNo",rowId.concat(",11"));
			    	empReqExcelRows.put("IssueStatus",status);	
			    	childRowResponse.add(empReqExcelRows);
			    }else{
			    	if(repUserId >0 && empUserId>0){		    	
			    	List<EFmFmEmployeeProjectDetailsPO> projectIdCombination =iEmployeeDetailBO.getClientProjectIdByMangerAndEmployee
			    			(repUserId, combinedFacility, projectDetails.get(0).getProjectId(),empUserId);
				    	if(projectIdCombination.isEmpty()){
				    		Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					    	String status = " Kinldy check the combination of Project Id  & EmployeeId & Reporting Manager Employee Id-" + columnValue.get(11).toString().trim();
					    	empReqExcelRows.put("RNo",rowId.concat(",11"));
					    	empReqExcelRows.put("IssueStatus",status);	
					    	childRowResponse.add(empReqExcelRows);
				    	}
			    	}
			    }
			}
	    
	    
	    	if (columnValue.get(12).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(12).toString() ==null || columnValue.get(12).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(12).toString().isEmpty() ) {
	    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the NodalPointName : "+columnValue.get(12).toString();
				empReqExcelRows.put("RNo",rowId.concat(",12"));
		    	empReqExcelRows.put("IssueStatus",status);
		    	childRowResponse.add(empReqExcelRows);
			}else if (!columnValue.get(12).toString().trim().equalsIgnoreCase("default")){
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				 List<EFmFmAreaNodalMasterPO> nodalPointExistDetail = iRouteDetailBO
				            .getParticularNodalPointNameDetails(columnValue.get(12).toString().trim());				 
				    if (nodalPointExistDetail.isEmpty()) {
				    	String status = "Nodal Point not available-" + columnValue.get(12).toString().trim();
				       	empReqExcelRows.put("RNo",rowId.concat(",12"));
				    	empReqExcelRows.put("IssueStatus",status);
				    	childRowResponse.add(empReqExcelRows);
				        
				    }
			}
			return childRowResponse;
}
    
    
	private List<Map<String, Object>> genpactEmployeeTravelRequest(Map<Integer, Object> empReqDetails, int branchId, String combinedFacility,int userId){   	
	 IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
	            .getBean("IEmployeeDetailBO");
	    ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		 IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
	     IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

	    DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	    DateFormat dateHypenFormat = new SimpleDateFormat("dd-MM-yyyy");
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	    DateFormat shifTimeFormate = new SimpleDateFormat("HH:mm");
	    List<Map<String, Object>> empRequestExcel = new ArrayList<Map<String, Object>>();
	    Map<String, Object> issueList = new HashMap<String, Object>();
	    log.info("Emp_ID");	
	    String status="";;
		try{							
			for (Entry<Integer, Object> entry : empReqDetails.entrySet()) {
					issueList = new HashMap<String, Object>();                
					ArrayList columnValue = (ArrayList) entry.getValue();		
		   
		    String shiftTimeInDateTimeFormate = columnValue.get(6).toString();
		    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
		    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());
		    
           Date requestDate = dateFormat.parse(columnValue.get(5).toString());
		    log.info("Date" + requestDate);
		    log.info("shiftTime" + shiftTime);
		    log.info("CurrentTime" + new Date());
		    String reqDate = dateHypenFormat.format(requestDate) + " " + shifTimeFormate.format(shiftTime);
		    log.info("reqDatereqDate" + reqDate);
		    log.info(requestDate.getTime() + shiftTime.getTime() + "ColumnValue:-" + columnValue);
		    log.info("reqq time.." + dateTimeFormate.parse(reqDate).getTime());
		    
		    
			if (dateTimeFormate.parse(reqDate).getTime() <= new Date().getTime()) {
		        status ="kinldy check the requestDate-" + columnValue.get(5).toString() + "EmpId "
		                + columnValue.get(0).toString();
		        issueList.put("RNo","");
		        issueList.put("IssueStatus",status);
		        empRequestExcel.add(issueList);
		        return empRequestExcel;
		        //return Response.ok(status, MediaType.APPLICATION_JSON).build();			        
		    }
		    List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO
		            .getEmpDetailsFromEmployeeId(columnValue.get(0).toString().trim());
		    if (employeeDetails.isEmpty()) {
		        status = "Employee Id is not available or remove space as well from employeeId -" + columnValue.get(0).toString().trim();
		        issueList.put("RNo","");
		        issueList.put("IssueStatus",status);
		        empRequestExcel.add(issueList);
		        return empRequestExcel;
		        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
		    }
		    if (!(employeeDetails.isEmpty()) && employeeDetails.get(0).getStatus().equalsIgnoreCase("N")) {
		        status = "Employee Id Already Disabled-" + columnValue.get(0).toString().trim();
		        issueList.put("RNo","");
		        issueList.put("IssueStatus",status);
		        empRequestExcel.add(issueList);
		        return empRequestExcel;
		        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
		    }			
		    
		    EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
		    
		    List<EFmFmFacilityToFacilityMappingPO> branchDetails=facilityBO.
		    		getParticularFacilityDetailFromBranchName(columnValue.get(7).toString().trim());
	        if(branchDetails.isEmpty()){
	            status = "Please check-" + columnValue.get(7).toString().trim()+" facility not exist.";
	            issueList.put("RNo","");
				issueList.put("IssueStatus",status);
				empRequestExcel.add(issueList);
	            return empRequestExcel;
	        }else if(!(branchDetails.isEmpty()) && branchDetails.get(0).getFacilityStatus().equalsIgnoreCase("N") ){
	            status = "Please check-" + columnValue.get(7).toString().trim()+" facility is  disable.";
	            issueList.put("RNo","");
				issueList.put("IssueStatus",status);
				empRequestExcel.add(issueList);
	            return empRequestExcel;
	        }
	        
	        if(!branchDetails.isEmpty()){  
	      if(branchDetails.get(0).geteFmFmClientBranchPO().isMultiFacility()){
		    int baseBranchId=iUserMasterBO.getBranchIdFromBranchName(columnValue.get(7).toString().trim());
		    
		    eFmFmClientBranch.setBranchId(baseBranchId);
	       if(!(facilityBO.checkFacilityAccess(userId, baseBranchId))){
	           status = "Please check looks like you don't have access of -" + columnValue.get(11).toString().trim()+" facility";
	           issueList.put("RNo","");
				issueList.put("IssueStatus",status);
				empRequestExcel.add(issueList);
	           return empRequestExcel; 
	       }
	       
	       if(!(facilityBO.checkFacilityAccess(employeeDetails.get(0).getUserId(), baseBranchId))){
	           status = "Please check looks like employeeId " +columnValue.get(0).toString().trim()+" don't have access of -" + columnValue.get(7).toString().trim()+" facility";
	           issueList.put("RNo","");
				issueList.put("IssueStatus",status);
				empRequestExcel.add(issueList);
	           return empRequestExcel; 
	        }			    
	        }else{
	        	
			    eFmFmClientBranch.setBranchId(branchDetails.get(0).geteFmFmClientBranchPO().getBranchId());
	        }
	        }
		    
		    
		    
           Date startDate = dateTimeFormate.parse(dateHypenFormat.format(requestDate) + " " + "00:00");   
           
           log.info("startDate"+startDate);

           List<EFmFmEmployeeRequestMasterPO> employeeRequestMasterPickUp = iCabRequestBO
                   .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(startDate, shiftTime,
                		   combinedFacility,
                   		employeeDetails.get(0).getUserId(), columnValue.get(4).toString().toUpperCase().trim());
		    
		    EFmFmUserMasterPO userMaster = new EFmFmUserMasterPO();

		    userMaster.setUserId(employeeDetails.get(0).getUserId());			
		    if (employeeRequestMasterPickUp.isEmpty()) {
		        EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMasterPO = new EFmFmEmployeeRequestMasterPO();
		        Date endDate = dateTimeFormate
		                .parse(dateHypenFormat.format(requestDate) + " " + "23:59");
		        if (columnValue.get(4).toString().trim().equalsIgnoreCase("PICKUP")) {
		            String shiftPickUpTime = columnValue.get(3).toString();
		            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
		            java.sql.Time pickUpTime = new java.sql.Time(
		                    shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
		            eFmFmEmployeeRequestMasterPO.setPickUpTime(pickUpTime);
		        }
		        eFmFmEmployeeRequestMasterPO.setReadFlg("N");
		        eFmFmEmployeeRequestMasterPO.setRequestDate(requestDate);
		        eFmFmEmployeeRequestMasterPO.setRequestFrom("E");
		            eFmFmEmployeeRequestMasterPO.setRequestType("normal");
		        eFmFmEmployeeRequestMasterPO.setShiftTime(shiftTime);
		        eFmFmEmployeeRequestMasterPO.setStatus("N");
		        eFmFmEmployeeRequestMasterPO.setTripRequestStartDate(requestDate);
		        eFmFmEmployeeRequestMasterPO.setTripRequestEndDate(endDate);
		        eFmFmEmployeeRequestMasterPO.setTripType(columnValue.get(4).toString().trim().toUpperCase());
		        eFmFmEmployeeRequestMasterPO.setEfmFmUserMaster(userMaster);
	            EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping=new EFmFmRouteAreaMappingPO();
	            eFmFmRouteAreaMapping.setRouteAreaId(employeeDetails.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
	            eFmFmEmployeeRequestMasterPO.seteFmFmClientBranchPO(eFmFmClientBranch);
		        eFmFmEmployeeRequestMasterPO.seteFmFmRouteAreaMapping(eFmFmRouteAreaMapping);
		        if (columnValue.get(4).toString().trim().equalsIgnoreCase("DROP")) {
		            eFmFmEmployeeRequestMasterPO
		                    .setDropSequence(Integer.parseInt(columnValue.get(3).toString().trim()));
		        }
		        iCabRequestBO.save(eFmFmEmployeeRequestMasterPO);
		        
//		        employeeRequestMasterPickUp = iCabRequestBO.getParticularRequestDetailFromUserIdAndTripType(
//		                employeeDetails.get(0).getUserId(), branchId, columnValue.get(6).toString().trim());
		        
		        employeeRequestMasterPickUp = iCabRequestBO
	                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(startDate, shiftTime,
	                    		combinedFacility,
	                    		employeeDetails.get(0).getUserId(), columnValue.get(4).toString().toUpperCase().trim());    
		     		        
		    }
		
		    List<EFmFmEmployeeRequestMasterPO> employeeRequestMasterDetailForDrop = null;
		    log.info(employeeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeDiffPickToDrop()+"Auto Drop type:-"+employeeDetails.get(0).geteFmFmClientBranchPO().getAutoDropRoster());			    
		    if (columnValue.get(4).toString().trim().equalsIgnoreCase("PICKUP") && employeeDetails.get(0).geteFmFmClientBranchPO().getAutoDropRoster().equalsIgnoreCase("Yes")) {
		        
//		    	
//		    	employeeRequestMasterDetailForDrop = iCabRequestBO
//		                .getParticularRequestDetailFromUserIdAndTripType(employeeDetails.get(0).getUserId(),
//		                        branchId, "DROP");
//		    	
	            long dropRequestDate = getDisableTime(employeeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeDiffPickToDrop(), 0, dateTimeFormate.parse(reqDate));
	            Date requestDateForDrop = new Date(dropRequestDate);
	            Date endDateForDrop = dateTimeFormate
	                    .parse(dateHypenFormat.format(requestDateForDrop) + " " + "23:59");
	            String dropShiftTime = requestDateForDrop.getHours() + ":"
	                    + requestDateForDrop.getMinutes();
	            String dropRequestAndStart = dateFormat.format(requestDateForDrop);
	
	            Date dropRequestAndStartDate = dateFormat.parse(dropRequestAndStart);
	            java.sql.Time dropShiftTimings = new java.sql.Time(
	                    shifTimeFormate.parse(dropShiftTime).getTime());

		    	employeeRequestMasterDetailForDrop = iCabRequestBO
	                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(dropRequestAndStartDate, dropShiftTimings,
	                    		combinedFacility,
	                    		employeeDetails.get(0).getUserId(), "DROP");
		        
		        
		        if (employeeRequestMasterDetailForDrop.isEmpty()) {
		            EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMaster = new EFmFmEmployeeRequestMasterPO();

		            eFmFmEmployeeRequestMaster.setReadFlg("N");
		            eFmFmEmployeeRequestMaster.setRequestDate(dropRequestAndStartDate);
		            eFmFmEmployeeRequestMaster.setRequestFrom("E");
		    //        if (columnValue.get(10).toString().trim().equalsIgnoreCase("default"))
		                eFmFmEmployeeRequestMaster.setRequestType("normal");
		   //         else {
		    //            eFmFmEmployeeRequestMaster.setRequestType("nodal");
		   //         }
		            eFmFmEmployeeRequestMaster.setShiftTime(dropShiftTimings);
		            eFmFmEmployeeRequestMaster.setStatus("N");
		            eFmFmEmployeeRequestMaster.setTripRequestStartDate(dropRequestAndStartDate);
		            eFmFmEmployeeRequestMaster.setTripRequestEndDate(endDateForDrop);
		            eFmFmEmployeeRequestMaster.setTripType("DROP");
		            eFmFmEmployeeRequestMaster.setDropSequence(1);
		            EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping=new EFmFmRouteAreaMappingPO();
		            eFmFmRouteAreaMapping.setRouteAreaId(employeeDetails.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
		            eFmFmEmployeeRequestMaster.seteFmFmRouteAreaMapping(eFmFmRouteAreaMapping);
		            eFmFmEmployeeRequestMaster.setEfmFmUserMaster(userMaster);
		            eFmFmEmployeeRequestMaster.seteFmFmClientBranchPO(eFmFmClientBranch);

		            if (columnValue.get(4).toString().trim().equalsIgnoreCase("DROP")) {
		                eFmFmEmployeeRequestMaster
		                        .setDropSequence(Integer.parseInt(columnValue.get(3).toString().trim()));
		            }
		            iCabRequestBO.save(eFmFmEmployeeRequestMaster);
		            
//		            employeeRequestMasterDetailForDrop = iCabRequestBO
//		                    .getParticularRequestDetailFromUserIdAndTripType(employeeDetails.get(0).getUserId(),
//		                            branchId, "DROP");
		            
			    	employeeRequestMasterDetailForDrop = iCabRequestBO
		                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(dropRequestAndStartDate, dropShiftTimings,
		                    		combinedFacility,
		                    		employeeDetails.get(0).getUserId(), "DROP");
		
		        }
		
		    }
		
		    List<EFmFmEmployeeTravelRequestPO> travelRequestDetails = iCabRequestBO
		            .getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(requestDate, shiftTime, combinedFacility,
		                    employeeDetails.get(0).getUserId(), columnValue.get(4).toString().trim());
		    if (travelRequestDetails.isEmpty()) {
		        EFmFmEmployeeTravelRequestPO travelRequestPO = new EFmFmEmployeeTravelRequestPO();
		        travelRequestPO.setApproveStatus("Y");
		        travelRequestPO.setIsActive("Y");
		        travelRequestPO.setReadFlg("Y");
		        travelRequestPO.setRoutingAreaCreation("Y");
		        travelRequestPO.setRequestDate(requestDate);
		        travelRequestPO.setRequestStatus("E");
		   //     if (columnValue.get(10).toString().trim().equalsIgnoreCase("default"))
		            travelRequestPO.setRequestType("normal");
		    //    else {
		    //        travelRequestPO.setRequestType("nodal");
		     //   }
		        travelRequestPO.setShiftTime(shiftTime);
		        travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMasterPickUp.get(0));
		        travelRequestPO.setEfmFmUserMaster(userMaster);
		        if (columnValue.get(4).toString().trim().equalsIgnoreCase("PICKUP")) {
		            String shiftPickUpTime = columnValue.get(3).toString();
		            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
		            java.sql.Time pickUpTime = new java.sql.Time(shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
		            travelRequestPO.setPickUpTime(pickUpTime);
		        }
		        travelRequestPO.setTripType(columnValue.get(4).toString().trim().toUpperCase());
		        travelRequestPO.setCompletionStatus("N");
		        if (columnValue.get(4).toString().trim().equalsIgnoreCase("DROP")) {
		            travelRequestPO.setDropSequence(Integer.parseInt(columnValue.get(3).toString().trim()));
		        }
	            EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping=new EFmFmRouteAreaMappingPO();
	            eFmFmRouteAreaMapping.setRouteAreaId(employeeDetails.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
			        travelRequestPO.seteFmFmRouteAreaMapping(eFmFmRouteAreaMapping);
			        travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranch);
		        iCabRequestBO.save(travelRequestPO);
		  //      }
		    }
		    if (!(travelRequestDetails.isEmpty())) {		            
	            if (columnValue.get(4).toString().trim().equalsIgnoreCase("DROP")) {
	            	travelRequestDetails.get(0).setDropSequence(Integer.parseInt(columnValue.get(3).toString().trim()));
		        }
	            else{
		            String shiftPickUpTime = columnValue.get(3).toString();
		            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
		            java.sql.Time pickUpTime = new java.sql.Time(shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
	            travelRequestDetails.get(0).setPickUpTime(pickUpTime);
	            }
	            EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping=new EFmFmRouteAreaMappingPO();
	            eFmFmRouteAreaMapping.setRouteAreaId(employeeDetails.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
	            	travelRequestDetails.get(0).seteFmFmRouteAreaMapping(eFmFmRouteAreaMapping);
		            iCabRequestBO.update(travelRequestDetails.get(0));
//		            }
		    }
		    if (columnValue.get(4).toString().trim().equalsIgnoreCase("PICKUP") && employeeDetails.get(0).geteFmFmClientBranchPO().getAutoDropRoster().equalsIgnoreCase("Yes")) {
		        long dropRequestDate = getDisableTime(employeeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeDiffPickToDrop(), 0, dateTimeFormate.parse(reqDate));
		        Date requestDateForDrop = new Date(dropRequestDate);
		        String dropShiftTime = requestDateForDrop.getHours() + ":" + requestDateForDrop.getMinutes();
		        java.sql.Time dropShiftTimings = new java.sql.Time(
		                shifTimeFormate.parse(dropShiftTime).getTime());
		        String dropRequestAndStart = dateFormat.format(requestDateForDrop);
		        Date dropRequestAndStartDate = dateFormat.parse(dropRequestAndStart);
		        List<EFmFmEmployeeTravelRequestPO> travelRequestDetailsForDrop = iCabRequestBO
		                .getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(dropRequestAndStartDate,
		                        dropShiftTimings, combinedFacility, employeeDetails.get(0).getUserId(), "DROP");
		        if (travelRequestDetailsForDrop.isEmpty()) {
		            EFmFmEmployeeTravelRequestPO travelRequestPO = new EFmFmEmployeeTravelRequestPO();
		            travelRequestPO.setApproveStatus("Y");
		            travelRequestPO.setIsActive("Y");
		            travelRequestPO.setReadFlg("Y");
			        travelRequestPO.setRoutingAreaCreation("Y");
		            travelRequestPO.setRequestDate(dropRequestAndStartDate);
		            travelRequestPO.setRequestStatus("E");
		            // adding pickup time for getting default sequence
		            // apposit to pickup.
		            String shiftPickUpTime = columnValue.get(3).toString();
		            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
		            java.sql.Time pickUpTime = new java.sql.Time(
		                    shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
		            travelRequestPO.setPickUpTime(pickUpTime);
		//            if (columnValue.get(10).toString().trim().equalsIgnoreCase("default"))
		                travelRequestPO.setRequestType("normal");
		//            else {
		 //               travelRequestPO.setRequestType("nodal");
		  //          }
		            travelRequestPO.setShiftTime(dropShiftTimings);
						if (!(employeeRequestMasterDetailForDrop.isEmpty())) {
							travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMasterDetailForDrop.get(0));
						}
		            travelRequestPO.setEfmFmUserMaster(userMaster);
		            travelRequestPO.setTripType("DROP");
		            travelRequestPO.setCompletionStatus("N");
		            travelRequestPO.setDropSequence(1);
			        travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranch);
		            EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping=new EFmFmRouteAreaMappingPO();
		            eFmFmRouteAreaMapping.setRouteAreaId(employeeDetails.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
			            travelRequestPO.seteFmFmRouteAreaMapping(eFmFmRouteAreaMapping);
			            iCabRequestBO.save(travelRequestPO);
//			            }
		        }
		    }
		 }
			
		}catch(Exception e){
			issueList.put("RNo","");				
			issueList.put("IssueStatus","Please check employee data,should not contain  blank columns and special symbols like ',@ etc.");
			empRequestExcel.add(issueList);
           log.info("ImportEmploye Request Exception" + e);
	        return empRequestExcel;
		}
		return empRequestExcel;
   
}
	
	
	/*
	 * 
	 * Servion ProjectDetails Integrated
	 */
	private List<Map<String, Object>> travelRequestByProjectDetails(Map<Integer, Object> empReqDetails, int branchId,String combinedFacility){   	
   	 IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
		            .getBean("IEmployeeDetailBO");
		    ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		    IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext()
		            .getBean("IRouteDetailBO");
		    DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		    DateFormat dateHypenFormat = new SimpleDateFormat("dd-MM-yyyy");
		    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		    DateFormat shifTimeFormate = new SimpleDateFormat("HH:mm");
		    List<Map<String, Object>> empRequestExcel = new ArrayList<Map<String, Object>>();
		    Map<String, Object> issueList = new HashMap<String, Object>();
		    log.info("Emp_ID");	
		    String status="";;
   		try{							
				for (Entry<Integer, Object> entry : empReqDetails.entrySet()) {
						issueList = new HashMap<String, Object>();                
						ArrayList columnValue = (ArrayList) entry.getValue();		
			   
			    String shiftTimeInDateTimeFormate = columnValue.get(9).toString();
			    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
			    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());
			    
               Date requestDate = dateFormat.parse(columnValue.get(8).toString());
			    log.info("Date" + requestDate);
			    log.info("shiftTime" + shiftTime);
			    log.info("CurrentTime" + new Date());
			    String reqDate = dateHypenFormat.format(requestDate) + " " + shifTimeFormate.format(shiftTime);
			    log.info("reqDatereqDate" + reqDate);
			    log.info(requestDate.getTime() + shiftTime.getTime() + "ColumnValue:-" + columnValue);
			    log.info("reqq time.." + dateTimeFormate.parse(reqDate).getTime());
			    
			    
				if (dateTimeFormate.parse(reqDate).getTime() <= new Date().getTime()) {
			        status ="kinldy check the requestDate-" + columnValue.get(8).toString() + "EmpId "
			                + columnValue.get(0).toString();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();			        
			    }
			    List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO
			            .getEmpDetailsFromEmployeeId(columnValue.get(0).toString().trim());
			    if (employeeDetails.isEmpty()) {
			        status = "Employee Id is not available or remove space as well from employeeId -" + columnValue.get(0).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }
			    
			    List<EFmFmUserMasterPO> repEmployeeDetails = iEmployeeDetailBO
			            .getEmpDetailsFromEmployeeId(columnValue.get(10).toString().trim());
			    if (employeeDetails.isEmpty()) {
			        status = "Rep Employee Id is not available or remove space as well from Rep employeeId -" + columnValue.get(10).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }
			    
				List<EFmFmClientProjectDetailsPO> projectDetails = iEmployeeDetailBO.getProjectDetails(columnValue.get(11).toString().trim(),combinedFacility);
			    if (projectDetails.isEmpty()) {			    	
			    	 status = "Project Id is not available or remove space as well from ProjectId -" + columnValue.get(11).toString().trim();
			    	 issueList.put("RNo","");
				     issueList.put("IssueStatus",status);
				     empRequestExcel.add(issueList);
				     return empRequestExcel;
			    }
			    
			    
			    
			    if (!(employeeDetails.isEmpty()) && employeeDetails.get(0).getStatus().equalsIgnoreCase("N")) {
			        status = "Employee Id Already Disabled-" + columnValue.get(0).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }			
			    List<EFmFmZoneMasterPO> routeExistDetail;
			    System.out.println("columnValue.get(12).toString().trim()"+columnValue.get(12).toString().trim());
			    if (columnValue.get(12).toString().trim().equalsIgnoreCase("default")) {
			        routeExistDetail = iRouteDetailBO.getNonNodalRouteNameFromClientIdAndRouteName(combinedFacility,
			        		columnValue.get(7).toString().trim());
			    } else {
			        routeExistDetail = iRouteDetailBO.getNodalRouteNameFromClientIdAndRouteName(combinedFacility,
			                columnValue.get(7).toString().trim());
			    }			
			    if (routeExistDetail.isEmpty()
			            && (columnValue.get(12).toString().trim().equalsIgnoreCase("default"))) {
			        status = "Normal Route Name not available -" + columnValue.get(7).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }
			    if (routeExistDetail.isEmpty()
			            && !(columnValue.get(12).toString().trim().equalsIgnoreCase("default"))) {
				    System.out.println("columnValue.get(12).toString().trim()"+columnValue.get(12).toString().trim());

			        status = "Nodal Route Name not available-" + columnValue.get(7).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }
			    List<EFmFmAreaNodalMasterPO> nodalPointExistDetail = iRouteDetailBO
			            .getParticularNodalPointNameDetails(columnValue.get(12).toString().trim());
			    if (nodalPointExistDetail.isEmpty()) {
			        status = "Nodal Point not available-" + columnValue.get(12).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }
			    List<EFmFmAreaMasterPO> areaId = iRouteDetailBO
			            .getParticularAreaNameDetails(columnValue.get(4).toString().trim());
			    if (areaId.isEmpty()) {
			        EFmFmAreaMasterPO efmFmAreaMaster = new EFmFmAreaMasterPO();
			        efmFmAreaMaster.setAreaDescription(columnValue.get(4).toString().trim());
			        efmFmAreaMaster.setAreaName(columnValue.get(4).toString().trim());
			        iRouteDetailBO.saveAreaRecord(efmFmAreaMaster);
			        areaId = iRouteDetailBO.getParticularAreaNameDetails(columnValue.get(4).toString().trim());
			    }
			    List<EFmFmRouteAreaMappingPO> routeAreaId =new ArrayList<EFmFmRouteAreaMappingPO>();
			    if (!(routeExistDetail.isEmpty())){
			    routeAreaId = iRouteDetailBO
			    		.getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(areaId.get(0).getAreaId(), combinedFacility,
			                    routeExistDetail.get(0).getZoneId(),
			                    nodalPointExistDetail.get(0).getNodalPointId());
			   
			    if (routeAreaId.isEmpty()) {
			        EFmFmRouteAreaMappingPO routeAreaMappingPO = new EFmFmRouteAreaMappingPO();
			        routeAreaMappingPO.setEfmFmAreaMaster(areaId.get(0));
			        routeAreaMappingPO.seteFmFmZoneMaster(routeExistDetail.get(0));
			        routeAreaMappingPO.seteFmFmNodalAreaMaster(nodalPointExistDetail.get(0));
			        iRouteDetailBO.saveRouteMappingDetails(routeAreaMappingPO);
			        routeAreaId = iRouteDetailBO.getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(
			                areaId.get(0).getAreaId(), combinedFacility, routeExistDetail.get(0).getZoneId(),
			                nodalPointExistDetail.get(0).getNodalPointId());
			    }
				}
//			    List<EFmFmEmployeeRequestMasterPO> employeeRequestMasterPickUp = iCabRequestBO
//			            .getParticularRequestDetailFromUserIdAndTripType(employeeDetails.get(0).getUserId(),
//			                    branchId, columnValue.get(6).toString().trim());
	            
	            Date startDate = dateTimeFormate.parse(dateHypenFormat.format(requestDate) + " " + "00:00");   
	            
	            log.info("startDate"+startDate);

	            List<EFmFmEmployeeRequestMasterPO> employeeRequestMasterPickUp = iCabRequestBO
	                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(startDate, shiftTime,
	                    		combinedFacility,
	                    		employeeDetails.get(0).getUserId(), columnValue.get(6).toString().toUpperCase().trim());
			    
			    EFmFmUserMasterPO userMaster = new EFmFmUserMasterPO();
			    userMaster.setUserId(employeeDetails.get(0).getUserId());			
			    if (employeeRequestMasterPickUp.isEmpty()) {
			        EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMasterPO = new EFmFmEmployeeRequestMasterPO();
			        Date endDate = dateTimeFormate
			                .parse(dateHypenFormat.format(requestDate) + " " + "23:59");
			        if (columnValue.get(6).toString().trim().equalsIgnoreCase("PICKUP")) {
			            String shiftPickUpTime = columnValue.get(5).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(
			                    shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
			            eFmFmEmployeeRequestMasterPO.setPickUpTime(pickUpTime);
			        }
			        eFmFmEmployeeRequestMasterPO.setReadFlg("N");
			        eFmFmEmployeeRequestMasterPO.setRequestDate(requestDate);
			        eFmFmEmployeeRequestMasterPO.setRequestFrom("E");
			        if (columnValue.get(12).toString().trim().equalsIgnoreCase("default"))
			            eFmFmEmployeeRequestMasterPO.setRequestType("normal");
			        else {
			            eFmFmEmployeeRequestMasterPO.setRequestType("nodal");
			        }
			        eFmFmEmployeeRequestMasterPO.setShiftTime(shiftTime);
			        eFmFmEmployeeRequestMasterPO.setStatus("N");
			        eFmFmEmployeeRequestMasterPO.setTripRequestStartDate(requestDate);
			        eFmFmEmployeeRequestMasterPO.setTripRequestEndDate(endDate);			        
			        eFmFmEmployeeRequestMasterPO.setReportingManagerUserId(String.valueOf(repEmployeeDetails.get(0).getUserId()));
			        eFmFmEmployeeRequestMasterPO.setProjectId(projectDetails.get(0).getProjectId());
			        eFmFmEmployeeRequestMasterPO.setReqApprovalStatus("Y");
			        eFmFmEmployeeRequestMasterPO.setLocationFlg("N");
			        eFmFmEmployeeRequestMasterPO.setLocationWaypointsIds("0");			        
			        
			        eFmFmEmployeeRequestMasterPO.setTripType(columnValue.get(6).toString().trim().toUpperCase());
			        eFmFmEmployeeRequestMasterPO.setEfmFmUserMaster(userMaster);
			        eFmFmEmployeeRequestMasterPO.seteFmFmRouteAreaMapping(routeAreaId.get(0));
			        if (columnValue.get(6).toString().trim().equalsIgnoreCase("DROP")) {
			            eFmFmEmployeeRequestMasterPO
			                    .setDropSequence(Integer.parseInt(columnValue.get(5).toString().trim()));
			        }
			        iCabRequestBO.save(eFmFmEmployeeRequestMasterPO);
			        
//			        employeeRequestMasterPickUp = iCabRequestBO.getParticularRequestDetailFromUserIdAndTripType(
//			                employeeDetails.get(0).getUserId(), branchId, columnValue.get(6).toString().trim());
			        
			        employeeRequestMasterPickUp = iCabRequestBO
		                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(startDate, shiftTime,
		                    		combinedFacility,
		                    		employeeDetails.get(0).getUserId(), columnValue.get(6).toString().toUpperCase().trim());    
			     		        
			    }
			
			    List<EFmFmEmployeeRequestMasterPO> employeeRequestMasterDetailForDrop = null;
			    log.info(employeeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeDiffPickToDrop()+"Auto Drop type:-"+employeeDetails.get(0).geteFmFmClientBranchPO().getAutoDropRoster());			    
			    if (columnValue.get(6).toString().trim().equalsIgnoreCase("PICKUP") && employeeDetails.get(0).geteFmFmClientBranchPO().getAutoDropRoster().equalsIgnoreCase("Yes")) {
			        
//			    	
//			    	employeeRequestMasterDetailForDrop = iCabRequestBO
//			                .getParticularRequestDetailFromUserIdAndTripType(employeeDetails.get(0).getUserId(),
//			                        branchId, "DROP");
//			    	
		            long dropRequestDate = getDisableTime(employeeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeDiffPickToDrop(), 0, dateTimeFormate.parse(reqDate));
		            Date requestDateForDrop = new Date(dropRequestDate);
		            Date endDateForDrop = dateTimeFormate
		                    .parse(dateHypenFormat.format(requestDateForDrop) + " " + "23:59");
		            String dropShiftTime = requestDateForDrop.getHours() + ":"
		                    + requestDateForDrop.getMinutes();
		            String dropRequestAndStart = dateFormat.format(requestDateForDrop);
		
		            Date dropRequestAndStartDate = dateFormat.parse(dropRequestAndStart);
		            java.sql.Time dropShiftTimings = new java.sql.Time(
		                    shifTimeFormate.parse(dropShiftTime).getTime());

			    	employeeRequestMasterDetailForDrop = iCabRequestBO
		                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(dropRequestAndStartDate, dropShiftTimings,
		                    		combinedFacility,
		                    		employeeDetails.get(0).getUserId(), "DROP");
			        
			        
			        if (employeeRequestMasterDetailForDrop.isEmpty()) {
			            EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMaster = new EFmFmEmployeeRequestMasterPO();
			            eFmFmEmployeeRequestMaster.setReadFlg("N");
			            eFmFmEmployeeRequestMaster.setRequestDate(dropRequestAndStartDate);
			            eFmFmEmployeeRequestMaster.setRequestFrom("E");
			            if (columnValue.get(12).toString().trim().equalsIgnoreCase("default"))
			                eFmFmEmployeeRequestMaster.setRequestType("normal");
			            else {
			                eFmFmEmployeeRequestMaster.setRequestType("nodal");
			            }
			            eFmFmEmployeeRequestMaster.setShiftTime(dropShiftTimings);
			            eFmFmEmployeeRequestMaster.setStatus("N");
			            eFmFmEmployeeRequestMaster.setTripRequestStartDate(dropRequestAndStartDate);
			            eFmFmEmployeeRequestMaster.setTripRequestEndDate(endDateForDrop);
			            eFmFmEmployeeRequestMaster.setTripType("DROP");
			            eFmFmEmployeeRequestMaster.setDropSequence(1);
			            eFmFmEmployeeRequestMaster.setReportingManagerUserId(String.valueOf(repEmployeeDetails.get(0).getUserId()));
			            eFmFmEmployeeRequestMaster.setProjectId(projectDetails.get(0).getProjectId());
			            eFmFmEmployeeRequestMaster.setReqApprovalStatus("Y");
			            eFmFmEmployeeRequestMaster.setLocationFlg("N");
			            eFmFmEmployeeRequestMaster.setLocationWaypointsIds("0");
			            eFmFmEmployeeRequestMaster.seteFmFmRouteAreaMapping(routeAreaId.get(0));
			            eFmFmEmployeeRequestMaster.setEfmFmUserMaster(userMaster);
			            if (columnValue.get(6).toString().trim().equalsIgnoreCase("DROP")) {
			                eFmFmEmployeeRequestMaster
			                        .setDropSequence(Integer.parseInt(columnValue.get(5).toString().trim()));
			            }
			            iCabRequestBO.save(eFmFmEmployeeRequestMaster);
			            
//			            employeeRequestMasterDetailForDrop = iCabRequestBO
//			                    .getParticularRequestDetailFromUserIdAndTripType(employeeDetails.get(0).getUserId(),
//			                            branchId, "DROP");
			            
				    	employeeRequestMasterDetailForDrop = iCabRequestBO
			                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(dropRequestAndStartDate, dropShiftTimings,
			                    		combinedFacility,
			                    		employeeDetails.get(0).getUserId(), "DROP");
    
			            
			
			        }
			
			    }
			
			    List<EFmFmEmployeeTravelRequestPO> travelRequestDetails = iCabRequestBO
			            .getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(requestDate, shiftTime, combinedFacility,
			                    employeeDetails.get(0).getUserId(), columnValue.get(6).toString().trim());
			    if (travelRequestDetails.isEmpty()) {
			        EFmFmEmployeeTravelRequestPO travelRequestPO = new EFmFmEmployeeTravelRequestPO();
			        travelRequestPO.setApproveStatus("Y");
			        travelRequestPO.setIsActive("Y");
			        travelRequestPO.setReadFlg("Y");
			        travelRequestPO.setRoutingAreaCreation("Y");
			        travelRequestPO.setRequestDate(requestDate);
			        travelRequestPO.setRequestStatus("E");
			        travelRequestPO.setReportingManagerUserId(String.valueOf(repEmployeeDetails.get(0).getUserId()));
			        travelRequestPO.setProjectId(projectDetails.get(0).getProjectId());
			        travelRequestPO.setReqApprovalStatus("Y");
			        travelRequestPO.setLocationFlg("N");
			        travelRequestPO.setLocationWaypointsIds("0");
			        if (columnValue.get(12).toString().trim().equalsIgnoreCase("default"))
			            travelRequestPO.setRequestType("normal");
			        else {
			            travelRequestPO.setRequestType("nodal");
			        }
			        travelRequestPO.setShiftTime(shiftTime);
			        travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMasterPickUp.get(0));
			        travelRequestPO.setEfmFmUserMaster(userMaster);
			        if (columnValue.get(6).toString().trim().equalsIgnoreCase("PICKUP")) {
			            String shiftPickUpTime = columnValue.get(5).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
			            travelRequestPO.setPickUpTime(pickUpTime);
			        }
			        travelRequestPO.setTripType(columnValue.get(6).toString().trim().toUpperCase());
			        travelRequestPO.setCompletionStatus("N");
			        if (columnValue.get(6).toString().trim().equalsIgnoreCase("DROP")) {
			            travelRequestPO.setDropSequence(Integer.parseInt(columnValue.get(5).toString().trim()));
			        }
			        if(!routeAreaId.isEmpty()){
				        travelRequestPO.seteFmFmRouteAreaMapping(routeAreaId.get(0));
			        iCabRequestBO.save(travelRequestPO);
			        }
			    }
			    if (!(travelRequestDetails.isEmpty())) {		            
		            if (columnValue.get(6).toString().trim().equalsIgnoreCase("DROP")) {
		            	travelRequestDetails.get(0).setDropSequence(Integer.parseInt(columnValue.get(5).toString().trim()));
			        }
		            else{
			            String shiftPickUpTime = columnValue.get(5).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
		            travelRequestDetails.get(0).setPickUpTime(pickUpTime);
		            }
		            if(!(routeAreaId.isEmpty())){
		            	travelRequestDetails.get(0).seteFmFmRouteAreaMapping(routeAreaId.get(0));
			            iCabRequestBO.update(travelRequestDetails.get(0));
			            }
			    }
			    if (columnValue.get(6).toString().trim().equalsIgnoreCase("PICKUP") && employeeDetails.get(0).geteFmFmClientBranchPO().getAutoDropRoster().equalsIgnoreCase("Yes")) {
			        long dropRequestDate = getDisableTime(employeeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeDiffPickToDrop(), 0, dateTimeFormate.parse(reqDate));
			        Date requestDateForDrop = new Date(dropRequestDate);
			        String dropShiftTime = requestDateForDrop.getHours() + ":" + requestDateForDrop.getMinutes();
			        java.sql.Time dropShiftTimings = new java.sql.Time(
			                shifTimeFormate.parse(dropShiftTime).getTime());
			        String dropRequestAndStart = dateFormat.format(requestDateForDrop);
			        Date dropRequestAndStartDate = dateFormat.parse(dropRequestAndStart);
			        List<EFmFmEmployeeTravelRequestPO> travelRequestDetailsForDrop = iCabRequestBO
			                .getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(dropRequestAndStartDate,
			                        dropShiftTimings, combinedFacility, employeeDetails.get(0).getUserId(), "DROP");
			        if (travelRequestDetailsForDrop.isEmpty()) {
			            EFmFmEmployeeTravelRequestPO travelRequestPO = new EFmFmEmployeeTravelRequestPO();
			            travelRequestPO.setApproveStatus("Y");
			            travelRequestPO.setIsActive("Y");
			            travelRequestPO.setReadFlg("Y");
				        travelRequestPO.setRoutingAreaCreation("Y");
				        travelRequestPO.setReportingManagerUserId(String.valueOf(repEmployeeDetails.get(0).getUserId()));
				        travelRequestPO.setProjectId(projectDetails.get(0).getProjectId());
				        travelRequestPO.setReqApprovalStatus("Y");
				        travelRequestPO.setLocationFlg("N");
				        travelRequestPO.setLocationWaypointsIds("0");
			            travelRequestPO.setRequestDate(dropRequestAndStartDate);
			            travelRequestPO.setRequestStatus("E");
			            // adding pickup time for getting default sequence
			            // apposit to pickup.
			            String shiftPickUpTime = columnValue.get(5).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(
			                    shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
			            travelRequestPO.setPickUpTime(pickUpTime);
			            if (columnValue.get(12).toString().trim().equalsIgnoreCase("default"))
			                travelRequestPO.setRequestType("normal");
			            else {
			                travelRequestPO.setRequestType("nodal");
			            }
			            travelRequestPO.setShiftTime(dropShiftTimings);
			            travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMasterDetailForDrop.get(0));
			            travelRequestPO.setEfmFmUserMaster(userMaster);
			            travelRequestPO.setTripType("DROP");
			            travelRequestPO.setCompletionStatus("N");
			            travelRequestPO.setDropSequence(1);
			            if(!(routeAreaId.isEmpty())){
				            travelRequestPO.seteFmFmRouteAreaMapping(routeAreaId.get(0));
				            iCabRequestBO.save(travelRequestPO);
				            }
			        }
			    }
			 }
				
			}catch(Exception e){
				log.info("Error"+e);
				issueList.put("RNo","");				
				issueList.put("IssueStatus","Please check employee data,should not contain  blank columns and special symbols like ',@ etc.");
				empRequestExcel.add(issueList);
	            log.info("ImportEmploye Request Exception" + e);
		        return empRequestExcel;
			}
			return empRequestExcel;
   }
	
	private List<Map<String, Object>> employeeTravelRequest(Map<Integer, Object> empReqDetails, int branchId,int userId,String combinedFacility){   	
    	 IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
		            .getBean("IEmployeeDetailBO");
		    ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		    IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext()
		            .getBean("IRouteDetailBO");
			 IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
		     IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		    DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		    DateFormat dateHypenFormat = new SimpleDateFormat("dd-MM-yyyy");
		    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		    DateFormat shifTimeFormate = new SimpleDateFormat("HH:mm");
		    List<Map<String, Object>> empRequestExcel = new ArrayList<Map<String, Object>>();
		    Map<String, Object> issueList = new HashMap<String, Object>();
		    log.info("Emp_ID");	
		    String status="";
    		try{							
				for (Entry<Integer, Object> entry : empReqDetails.entrySet()) {
						issueList = new HashMap<String, Object>();                
						ArrayList columnValue = (ArrayList) entry.getValue();		
			   
			    String shiftTimeInDateTimeFormate = columnValue.get(9).toString();
			    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
			    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());
			    
                Date requestDate = dateFormat.parse(columnValue.get(8).toString());
			    log.info("Date" + requestDate);
			    log.info("shiftTime" + shiftTime);
			    log.info("CurrentTime" + new Date());
			    String reqDate = dateHypenFormat.format(requestDate) + " " + shifTimeFormate.format(shiftTime);
			    log.info("reqDatereqDate" + reqDate);
			    log.info(requestDate.getTime() + shiftTime.getTime() + "ColumnValue:-" + columnValue);
			    log.info("reqq time.." + dateTimeFormate.parse(reqDate).getTime());
			    
			    
				if (dateTimeFormate.parse(reqDate).getTime() <= new Date().getTime()) {
			        status ="kinldy check the requestDate-" + columnValue.get(8).toString() + "EmpId "
			                + columnValue.get(0).toString();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();			        
			    }
			    List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO
			            .getEmpDetailsFromEmployeeId(columnValue.get(0).toString().trim());
			    if (employeeDetails.isEmpty()) {
			        status = "Employee Id is not available or remove space as well from employeeId -" + columnValue.get(0).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }
			    if (!(employeeDetails.isEmpty()) && employeeDetails.get(0).getStatus().equalsIgnoreCase("N")) {
			        status = "Employee Id Already Disabled-" + columnValue.get(0).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }	
			    EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
			    
			    List<EFmFmFacilityToFacilityMappingPO> branchDetails=facilityBO.
			    		getParticularFacilityDetailFromBranchName(columnValue.get(11).toString().trim());
		        if(branchDetails.isEmpty()){
		            status = "Please check-" + columnValue.get(11).toString().trim()+" facility not exist.";
		            issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					empRequestExcel.add(issueList);
		            return empRequestExcel;
		        }else if(!(branchDetails.isEmpty()) && branchDetails.get(0).getFacilityStatus().equalsIgnoreCase("N") ){
		            status = "Please check-" + columnValue.get(11).toString().trim()+" facility is  disable.";
		            issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					empRequestExcel.add(issueList);
		            return empRequestExcel;
		        }
		        
		        if(!branchDetails.isEmpty()){  
		      if(branchDetails.get(0).geteFmFmClientBranchPO().isMultiFacility()){
			    int baseBranchId=iUserMasterBO.getBranchIdFromBranchName(columnValue.get(11).toString().trim());
			    
			    eFmFmClientBranch.setBranchId(baseBranchId);
		       if(!(facilityBO.checkFacilityAccess(userId, baseBranchId))){
		           status = "Please check looks like you don't have access of -" + columnValue.get(11).toString().trim()+" facility";
		           issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					empRequestExcel.add(issueList);
		           return empRequestExcel; 
		       }
		       
		       if(!(facilityBO.checkFacilityAccess(employeeDetails.get(0).getUserId(), baseBranchId))){
		           status = "Please check looks like employeeId " +columnValue.get(0).toString().trim()+" don't have access of -" + columnValue.get(11).toString().trim()+" facility";
		           issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					empRequestExcel.add(issueList);
		           return empRequestExcel; 
		        }			    
		        }else{
		        	
				    eFmFmClientBranch.setBranchId(branchDetails.get(0).geteFmFmClientBranchPO().getBranchId());
		        }
		        }	
		        
			    
			    List<EFmFmZoneMasterPO> routeExistDetail;
			    log.info("columnValue.get(10).toString().trim()"+columnValue.get(10).toString().trim());
			    if (columnValue.get(10).toString().trim().equalsIgnoreCase("default")) {
			        routeExistDetail = iRouteDetailBO.getNonNodalRouteNameFromClientIdAndRouteName(combinedFacility,
			        		columnValue.get(7).toString().trim());
			    } else {
			        routeExistDetail = iRouteDetailBO.getNodalRouteNameFromClientIdAndRouteName(combinedFacility,
			                columnValue.get(7).toString().trim());
			    }			
			    if (routeExistDetail.isEmpty()
			            && (columnValue.get(10).toString().trim().equalsIgnoreCase("default"))) {
			        status = "Normal Route Name not available -" + columnValue.get(7).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }
			    if (routeExistDetail.isEmpty()
			            && !(columnValue.get(10).toString().trim().equalsIgnoreCase("default"))) {
			        status = "Nodal Route Name not available-" + columnValue.get(7).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }
			    List<EFmFmAreaNodalMasterPO> nodalPointExistDetail = iRouteDetailBO
			            .getParticularNodalPointNameDetails(columnValue.get(10).toString().trim());
			    if (nodalPointExistDetail.isEmpty()) {
			        status = "Nodal Point not available-" + columnValue.get(10).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }
			    List<EFmFmAreaMasterPO> areaId = iRouteDetailBO
			            .getParticularAreaNameDetails(columnValue.get(4).toString().trim());
			    if (areaId.isEmpty()) {
			        EFmFmAreaMasterPO efmFmAreaMaster = new EFmFmAreaMasterPO();
			        efmFmAreaMaster.setAreaDescription(columnValue.get(4).toString().trim());
			        efmFmAreaMaster.setAreaName(columnValue.get(4).toString().trim());
			        iRouteDetailBO.saveAreaRecord(efmFmAreaMaster);
			        areaId = iRouteDetailBO.getParticularAreaNameDetails(columnValue.get(4).toString().trim());
			    }
			    List<EFmFmRouteAreaMappingPO> routeAreaId =new ArrayList<EFmFmRouteAreaMappingPO>();
			    if (!(routeExistDetail.isEmpty())){
			    routeAreaId = iRouteDetailBO
			    		.getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(areaId.get(0).getAreaId(), combinedFacility,
			                    routeExistDetail.get(0).getZoneId(),
			                    nodalPointExistDetail.get(0).getNodalPointId());
			   
			    if (routeAreaId.isEmpty()) {
			        EFmFmRouteAreaMappingPO routeAreaMappingPO = new EFmFmRouteAreaMappingPO();
			        routeAreaMappingPO.setEfmFmAreaMaster(areaId.get(0));
			        routeAreaMappingPO.seteFmFmZoneMaster(routeExistDetail.get(0));
			        routeAreaMappingPO.seteFmFmNodalAreaMaster(nodalPointExistDetail.get(0));
			        iRouteDetailBO.saveRouteMappingDetails(routeAreaMappingPO);
			        routeAreaId = iRouteDetailBO.getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(
			                areaId.get(0).getAreaId(), combinedFacility, routeExistDetail.get(0).getZoneId(),
			                nodalPointExistDetail.get(0).getNodalPointId());
			    }
				}
//			    List<EFmFmEmployeeRequestMasterPO> employeeRequestMasterPickUp = iCabRequestBO
//			            .getParticularRequestDetailFromUserIdAndTripType(employeeDetails.get(0).getUserId(),
//			                    branchId, columnValue.get(6).toString().trim());
	            
	            Date startDate = dateTimeFormate.parse(dateHypenFormat.format(requestDate) + " " + "00:00");   
	            
	            log.info("startDate"+startDate);

	            List<EFmFmEmployeeRequestMasterPO> employeeRequestMasterPickUp = iCabRequestBO
	                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(startDate, shiftTime,
	                    		combinedFacility,
	                    		employeeDetails.get(0).getUserId(), columnValue.get(6).toString().toUpperCase().trim());
			    
			    EFmFmUserMasterPO userMaster = new EFmFmUserMasterPO();
			    userMaster.setUserId(employeeDetails.get(0).getUserId());			
			    if (employeeRequestMasterPickUp.isEmpty()) {
			        EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMasterPO = new EFmFmEmployeeRequestMasterPO();
			        Date endDate = dateTimeFormate
			                .parse(dateHypenFormat.format(requestDate) + " " + "23:59");
			        if (columnValue.get(6).toString().trim().equalsIgnoreCase("PICKUP")) {
			            String shiftPickUpTime = columnValue.get(5).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(
			                    shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
			            eFmFmEmployeeRequestMasterPO.setPickUpTime(pickUpTime);
			        }
			        eFmFmEmployeeRequestMasterPO.setReadFlg("N");
			        eFmFmEmployeeRequestMasterPO.setLocationFlg("N");
			        eFmFmEmployeeRequestMasterPO.setRequestDate(requestDate);
			        eFmFmEmployeeRequestMasterPO.setRequestFrom("E");
			        if (columnValue.get(10).toString().trim().equalsIgnoreCase("default"))
			            eFmFmEmployeeRequestMasterPO.setRequestType("normal");
			        else {
			            eFmFmEmployeeRequestMasterPO.setRequestType("nodal");
			        }
			        eFmFmEmployeeRequestMasterPO.setShiftTime(shiftTime);
			        eFmFmEmployeeRequestMasterPO.setStatus("N");
			        eFmFmEmployeeRequestMasterPO.setBranchName(columnValue.get(11).toString().trim().toUpperCase());			        
			        eFmFmEmployeeRequestMasterPO.seteFmFmClientBranchPO(eFmFmClientBranch);			        
			        eFmFmEmployeeRequestMasterPO.setTripRequestStartDate(requestDate);
			        eFmFmEmployeeRequestMasterPO.setTripRequestEndDate(endDate);
			        eFmFmEmployeeRequestMasterPO.setTripType(columnValue.get(6).toString().trim().toUpperCase());
			        eFmFmEmployeeRequestMasterPO.setEfmFmUserMaster(userMaster);
			        eFmFmEmployeeRequestMasterPO.seteFmFmRouteAreaMapping(routeAreaId.get(0));
			        if (columnValue.get(6).toString().trim().equalsIgnoreCase("DROP")) {
			            eFmFmEmployeeRequestMasterPO
			                    .setDropSequence(Integer.parseInt(columnValue.get(5).toString().trim()));
			        }
			        iCabRequestBO.save(eFmFmEmployeeRequestMasterPO);
			        
//			        employeeRequestMasterPickUp = iCabRequestBO.getParticularRequestDetailFromUserIdAndTripType(
//			                employeeDetails.get(0).getUserId(), branchId, columnValue.get(6).toString().trim());
			        
			        employeeRequestMasterPickUp = iCabRequestBO
		                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(startDate, shiftTime,
		                    		combinedFacility,
		                    		employeeDetails.get(0).getUserId(), columnValue.get(6).toString().toUpperCase().trim());    
			     		        
			    }
			
			    List<EFmFmEmployeeRequestMasterPO> employeeRequestMasterDetailForDrop = null;
			    log.info(employeeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeDiffPickToDrop()+"Auto Drop type:-"+employeeDetails.get(0).geteFmFmClientBranchPO().getAutoDropRoster());			    
			    if (columnValue.get(6).toString().trim().equalsIgnoreCase("PICKUP") && employeeDetails.get(0).geteFmFmClientBranchPO().getAutoDropRoster().equalsIgnoreCase("Yes")) {
			        
//			    	
//			    	employeeRequestMasterDetailForDrop = iCabRequestBO
//			                .getParticularRequestDetailFromUserIdAndTripType(employeeDetails.get(0).getUserId(),
//			                        branchId, "DROP");
//			    	
		            long dropRequestDate = getDisableTime(employeeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeDiffPickToDrop(), 0, dateTimeFormate.parse(reqDate));
		            Date requestDateForDrop = new Date(dropRequestDate);
		            Date endDateForDrop = dateTimeFormate
		                    .parse(dateHypenFormat.format(requestDateForDrop) + " " + "23:59");
		            String dropShiftTime = requestDateForDrop.getHours() + ":"
		                    + requestDateForDrop.getMinutes();
		            String dropRequestAndStart = dateFormat.format(requestDateForDrop);
		
		            Date dropRequestAndStartDate = dateFormat.parse(dropRequestAndStart);
		            java.sql.Time dropShiftTimings = new java.sql.Time(
		                    shifTimeFormate.parse(dropShiftTime).getTime());

			    	employeeRequestMasterDetailForDrop = iCabRequestBO
		                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(dropRequestAndStartDate, dropShiftTimings,
		                    		combinedFacility,
		                    		employeeDetails.get(0).getUserId(), "DROP");
			        
			        
			        if (employeeRequestMasterDetailForDrop.isEmpty()) {
			            EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMaster = new EFmFmEmployeeRequestMasterPO();
			            eFmFmEmployeeRequestMaster.setReadFlg("N");
			            eFmFmEmployeeRequestMaster.setRequestDate(dropRequestAndStartDate);
			            eFmFmEmployeeRequestMaster.setRequestFrom("E");
			            if (columnValue.get(10).toString().trim().equalsIgnoreCase("default"))
			                eFmFmEmployeeRequestMaster.setRequestType("normal");
			            else {
			                eFmFmEmployeeRequestMaster.setRequestType("nodal");
			            }
			            eFmFmEmployeeRequestMaster.setShiftTime(dropShiftTimings);
			            eFmFmEmployeeRequestMaster.setStatus("N");
				        eFmFmEmployeeRequestMaster.setBranchName(columnValue.get(11).toString().trim().toUpperCase());
				        eFmFmEmployeeRequestMaster.seteFmFmClientBranchPO(eFmFmClientBranch);

			            eFmFmEmployeeRequestMaster.setTripRequestStartDate(dropRequestAndStartDate);
			            eFmFmEmployeeRequestMaster.setTripRequestEndDate(endDateForDrop);
			            eFmFmEmployeeRequestMaster.setTripType("DROP");
			            eFmFmEmployeeRequestMaster.setDropSequence(1);
			            eFmFmEmployeeRequestMaster.seteFmFmRouteAreaMapping(routeAreaId.get(0));
			            eFmFmEmployeeRequestMaster.setEfmFmUserMaster(userMaster);
			            if (columnValue.get(6).toString().trim().equalsIgnoreCase("DROP")) {
			                eFmFmEmployeeRequestMaster
			                        .setDropSequence(Integer.parseInt(columnValue.get(5).toString().trim()));
			            }
			            iCabRequestBO.save(eFmFmEmployeeRequestMaster);
			            
//			            employeeRequestMasterDetailForDrop = iCabRequestBO
//			                    .getParticularRequestDetailFromUserIdAndTripType(employeeDetails.get(0).getUserId(),
//			                            branchId, "DROP");
			            
				    	employeeRequestMasterDetailForDrop = iCabRequestBO
			                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(dropRequestAndStartDate, dropShiftTimings,
			                    		combinedFacility,
			                    		employeeDetails.get(0).getUserId(), "DROP");
     
			            
			
			        }
			
			    }
			
			    List<EFmFmEmployeeTravelRequestPO> travelRequestDetails = iCabRequestBO
			            .getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(requestDate, shiftTime, combinedFacility,
			                    employeeDetails.get(0).getUserId(), columnValue.get(6).toString().trim());
			    if (travelRequestDetails.isEmpty()) {
			        EFmFmEmployeeTravelRequestPO travelRequestPO = new EFmFmEmployeeTravelRequestPO();
			        travelRequestPO.setApproveStatus("Y");
			        travelRequestPO.setIsActive("Y");
			        travelRequestPO.setReadFlg("Y");
			        travelRequestPO.setTripSheetStatus("NOCHANGES");
			        travelRequestPO.setLocationFlg("N");	
			        travelRequestPO.setBranchName(columnValue.get(11).toString().trim().toUpperCase());
			        travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranch);

			        travelRequestPO.setRoutingAreaCreation("Y");
			        travelRequestPO.setRequestDate(requestDate);
			        travelRequestPO.setRequestStatus("E");
			        if (columnValue.get(10).toString().trim().equalsIgnoreCase("default"))
			            travelRequestPO.setRequestType("normal");
			        else {
			            travelRequestPO.setRequestType("nodal");
			        }
			        travelRequestPO.setShiftTime(shiftTime);
			        travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMasterPickUp.get(0));
			        travelRequestPO.setEfmFmUserMaster(userMaster);
			        if (columnValue.get(6).toString().trim().equalsIgnoreCase("PICKUP")) {
			            String shiftPickUpTime = columnValue.get(5).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
			            travelRequestPO.setPickUpTime(pickUpTime);
			        }
			        travelRequestPO.setTripType(columnValue.get(6).toString().trim().toUpperCase());
			        travelRequestPO.setCompletionStatus("N");
			        if (columnValue.get(6).toString().trim().equalsIgnoreCase("DROP")) {
			            travelRequestPO.setDropSequence(Integer.parseInt(columnValue.get(5).toString().trim()));
			        }
			        if(!routeAreaId.isEmpty()){
				        travelRequestPO.seteFmFmRouteAreaMapping(routeAreaId.get(0));
			        iCabRequestBO.save(travelRequestPO);
			        }
			    }
			    if (!(travelRequestDetails.isEmpty())) {		            
		            if (columnValue.get(6).toString().trim().equalsIgnoreCase("DROP")) {
		            	travelRequestDetails.get(0).setDropSequence(Integer.parseInt(columnValue.get(5).toString().trim()));
			        }
		            else{
			            String shiftPickUpTime = columnValue.get(5).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
		            travelRequestDetails.get(0).setPickUpTime(pickUpTime);
		            }
		            if(!(routeAreaId.isEmpty())){
		            	travelRequestDetails.get(0).seteFmFmRouteAreaMapping(routeAreaId.get(0));
			            iCabRequestBO.update(travelRequestDetails.get(0));
			            }
			    }
			    if (columnValue.get(6).toString().trim().equalsIgnoreCase("PICKUP") && employeeDetails.get(0).geteFmFmClientBranchPO().getAutoDropRoster().equalsIgnoreCase("Yes")) {
			        long dropRequestDate = getDisableTime(employeeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeDiffPickToDrop(), 0, dateTimeFormate.parse(reqDate));
			        Date requestDateForDrop = new Date(dropRequestDate);
			        String dropShiftTime = requestDateForDrop.getHours() + ":" + requestDateForDrop.getMinutes();
			        java.sql.Time dropShiftTimings = new java.sql.Time(
			                shifTimeFormate.parse(dropShiftTime).getTime());
			        String dropRequestAndStart = dateFormat.format(requestDateForDrop);
			        Date dropRequestAndStartDate = dateFormat.parse(dropRequestAndStart);
			        List<EFmFmEmployeeTravelRequestPO> travelRequestDetailsForDrop = iCabRequestBO
			                .getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(dropRequestAndStartDate,
			                        dropShiftTimings, combinedFacility, employeeDetails.get(0).getUserId(), "DROP");
			        if (travelRequestDetailsForDrop.isEmpty()) {
			            EFmFmEmployeeTravelRequestPO travelRequestPO = new EFmFmEmployeeTravelRequestPO();
			            travelRequestPO.setApproveStatus("Y");
			            travelRequestPO.setIsActive("Y");
			            travelRequestPO.setReadFlg("Y");
			            travelRequestPO.setTripSheetStatus("NOCHANGES");
			            travelRequestPO.setLocationFlg("N");
				        travelRequestPO.setBranchName(columnValue.get(11).toString().trim().toUpperCase());
				        travelRequestPO.seteFmFmClientBranchPO(eFmFmClientBranch);

				        travelRequestPO.setRoutingAreaCreation("Y");
			            travelRequestPO.setRequestDate(dropRequestAndStartDate);
			            travelRequestPO.setRequestStatus("E");
			            // adding pickup time for getting default sequence
			            // apposit to pickup.
			            String shiftPickUpTime = columnValue.get(5).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(
			                    shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
			            travelRequestPO.setPickUpTime(pickUpTime);
			            if (columnValue.get(10).toString().trim().equalsIgnoreCase("default"))
			                travelRequestPO.setRequestType("normal");
			            else {
			                travelRequestPO.setRequestType("nodal");
			            }
			            travelRequestPO.setShiftTime(dropShiftTimings);
			            travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMasterDetailForDrop.get(0));
			            travelRequestPO.setEfmFmUserMaster(userMaster);
			            travelRequestPO.setTripType("DROP");
			            travelRequestPO.setCompletionStatus("N");
			            travelRequestPO.setDropSequence(1);
			            if(!(routeAreaId.isEmpty())){
				            travelRequestPO.seteFmFmRouteAreaMapping(routeAreaId.get(0));
				            iCabRequestBO.save(travelRequestPO);
				            }
			        }
			    }
			 }
				
			}catch(Exception e){
				log.info("Error"+e);
				issueList.put("RNo","");				
				issueList.put("IssueStatus","Please check employee data,should not contain  blank columns and special symbols like ',@ etc.");
				empRequestExcel.add(issueList);
	            log.info("ImportEmploye Request Exception" + e);
		        return empRequestExcel;
			}
			return empRequestExcel;
    }
	
	
	
	
	/*
	 * EmployeeMaster Uplaod
	 */
    private List<Map<String, Object>> empMasterExcelValidator(String rowId,ArrayList<Object> columnValue,int branchId,int noOfcolumn,String combinedFacility) throws UnsupportedEncodingException{
	    IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");		   
	    IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
	    log.info("Validation in process...."+columnValue.get(0));
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

	    DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	    DateFormat dateHypenFormat = new SimpleDateFormat("dd-MM-yyyy");
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	    DateFormat shifTimeFormate = new SimpleDateFormat("HH:mm");	
	    List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();
	    List<EFmFmZoneMasterPO> routeExistDetail;
	    String validInt="^[0-9]*$";
	    String charAt="^[a-zA-Z]*$";
	    
	    try{
	    if (columnValue.get(0).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(0).toString() ==null || columnValue.get(0).toString().replace(".0","").trim().equalsIgnoreCase("") 
					|| columnValue.get(0).toString().isEmpty()) {
	    	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
	    		String status="kindly check the employee Id : "+columnValue.get(0).toString();
				empReqExcelRows.put("RNo",rowId.concat(",0"));
				empReqExcelRows.put("IssueStatus",status);	
				childRowResponse.add(empReqExcelRows);
			}else{
				List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO.getEmpDetailsFromEmployeeId(columnValue.get(0).toString().trim());
			    if (!employeeDetails.isEmpty()) {
			    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			    	String status = "Employee Id already available -" + columnValue.get(0).toString().trim();
			    	empReqExcelRows.put("RNo",rowId.concat(",0"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
			    } 
			}
			
	    if (columnValue.get(1).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(1).toString() ==null || columnValue.get(1).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(1).toString().isEmpty() ) {
	    		Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the employee Name column value : "+columnValue.get(1).toString();
				empReqExcelRows.put("RNo",rowId.concat(",1"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
	    if (columnValue.get(4).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(4).toString() ==null || columnValue.get(4).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(4).toString().isEmpty() || !columnValue.get(4).toString().replace(" ", "").trim().matches(charAt)) {
	    		Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the employee sex : "+columnValue.get(4).toString();
				empReqExcelRows.put("RNo",rowId.concat(",4"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
				
			}else if (!("Female".equalsIgnoreCase(columnValue.get(4).toString().trim()) || "Male".equalsIgnoreCase(columnValue.get(4).toString().trim()))){
				String	status="kindly check the employee sex : "+columnValue.get(4).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",4"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
	    if (columnValue.get(5).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(5).toString() ==null || columnValue.get(5).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(5).toString().isEmpty()) {
				String status="kindly check the EmployeeBusinessUnit : "+columnValue.get(5).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",5"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
			
	    if (columnValue.get(6).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(6).toString() ==null || columnValue.get(6).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(6).toString().isEmpty()) {
				String status="kindly check the Employee Department : "+columnValue.get(6).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",6"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
	    if (columnValue.get(7).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(7).toString().trim() ==null||columnValue.get(7).toString().replace(".0"," ").trim().equalsIgnoreCase("") 
					|| columnValue.get(7).toString().replace(" ", "").trim().isEmpty()){
						String status="kindly check the Employee Domain : "+columnValue.get(7).toString();
						Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
						empReqExcelRows.put("RNo",rowId.concat(",7"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
			}		

	    if (columnValue.get(8).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(8).toString() ==null || columnValue.get(8).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(8).toString().isEmpty() || !columnValue.get(8).toString().replace(" ", "").trim().matches(validInt)) {
				String status="kindly check the Mobile Number and Mobile Should be Integer : "+columnValue.get(8).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",8"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else if(columnValue.get(8).toString().trim().length()>5 && columnValue.get(8).toString().trim().length()<=18){							
					 List<EFmFmUserMasterPO> employeeMobileNo = iEmployeeDetailBO.getParticularEmpDetailsFromMobileNumber(
                             Base64.getEncoder().encodeToString(columnValue.get(8).toString().trim().getBytes("utf-8")));
					 if(!employeeMobileNo.isEmpty()){
						 String status="Mobile Number already available on database  : "+columnValue.get(8).toString();
						 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
						 empReqExcelRows.put("RNo",rowId.concat(",8"));
							empReqExcelRows.put("IssueStatus",status);
							childRowResponse.add(empReqExcelRows);
					 }
				
			}else{
				 String status="kindly check the Mobile Number length(ex:India -12 digit),length should be country Code with MobileNumber  : "+columnValue.get(8).toString();
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();	
				 empReqExcelRows.put("RNo",rowId.concat(",8"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
			}
	    if (columnValue.get(9).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(9).toString() ==null || columnValue.get(9).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(9).toString().isEmpty()) {
				String status="kindly check the Email Id : "+columnValue.get(9).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",9"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else {	
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();						  
						    	List<EFmFmUserMasterPO> employeeEmailId = iEmployeeDetailBO.getParticularEmployeeDetailsFromEmailId(
                                        Base64.getEncoder().encodeToString(columnValue.get(9).toString().trim().getBytes("utf-8")));
							      if(!employeeEmailId.isEmpty()){					    	  
										 String status="Email Id already available on database  : "+columnValue.get(9).toString();
										 empReqExcelRows.put("RNo",rowId.concat(",9"));
											empReqExcelRows.put("IssueStatus",status);
											childRowResponse.add(empReqExcelRows);
									 } 
						    		  				
			}	
			
	    if (columnValue.get(10).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(10).toString() ==null || columnValue.get(10).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(10).toString().isEmpty()) {
				String status="kindly check the AreaName : "+columnValue.get(10).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",10"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
	    if (columnValue.get(11).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(11).toString() ==null || columnValue.get(11).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(11).toString().isEmpty() || !columnValue.get(11).toString().replace(" ", "").trim().matches(charAt) ) {
				String status="kindly check the State Name : "+columnValue.get(11).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",11"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
	    if (columnValue.get(12).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(12).toString() ==null || columnValue.get(12).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(12).toString().isEmpty() || !columnValue.get(12).toString().replace(" ", "").trim().matches(charAt) ) {
				String status="kindly check the City Name : "+columnValue.get(12).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",12"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
	    if (columnValue.get(13).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(13).toString() ==null || columnValue.get(13).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(13).toString().isEmpty() || !columnValue.get(13).toString().replace(" ", "").trim().matches(validInt)) {
				String status="kindly check the Pincode : "+columnValue.get(13).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",13"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
	    if (columnValue.get(14).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(14).toString() ==null || columnValue.get(14).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(14).toString().isEmpty() ) {
				String status="kindly check the Current Address : "+columnValue.get(14).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",14"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
	    if (columnValue.get(15).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(15).toString() ==null || columnValue.get(15).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(15).toString().isEmpty()) {
				String status="kindly check the Employee Designation : "+columnValue.get(15).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",15"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}			
			
	    if (columnValue.get(16).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(16).toString() ==null || columnValue.get(16).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
				|| columnValue.get(16).toString().isEmpty() ) {
			String status="kindly check the Project Id : "+columnValue.get(16).toString();
			Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			empReqExcelRows.put("RNo",rowId.concat(",16"));
			empReqExcelRows.put("IssueStatus",status);
			childRowResponse.add(empReqExcelRows);
		}else if (!columnValue.get(27).toString().trim().equalsIgnoreCase("default")){
			List<EFmFmClientProjectDetailsPO> projectDetails = iEmployeeDetailBO.getProjectDetails(columnValue.get(16).toString().trim(),String.valueOf(branchId));
		    if (projectDetails.isEmpty()) {
		    	EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO = new EFmFmClientProjectDetailsPO();
		        eFmFmClientProjectDetailsPO.setClientProjectId(columnValue.get(16).toString());
		        eFmFmClientProjectDetailsPO.setEmployeeProjectName(columnValue.get(17).toString().trim());
		        eFmFmClientProjectDetailsPO.setProjectAllocationStarDate(dateFormat.parse(columnValue.get(18).toString().trim()));
		        eFmFmClientProjectDetailsPO.setProjectAllocationEndDate(dateFormat.parse(columnValue.get(19).toString().trim()));
		        EFmFmClientBranchPO eFmFmClientBranch=new EFmFmClientBranchPO();
		        eFmFmClientBranch.setBranchId(branchId);			                
		    	eFmFmClientProjectDetailsPO.seteFmFmClientBranchPO(eFmFmClientBranch);
		        eFmFmClientProjectDetailsPO.setIsActive("A");
		        eFmFmClientProjectDetailsPO.setCreatedDate(new Date());
		    	iEmployeeDetailBO.save(eFmFmClientProjectDetailsPO);			    	
		    	/*Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
		    	String status = "Project Id is not available or remove Spaces & try again" + columnValue.get(16).toString().trim();
		    	empReqExcelRows.put("RNo",rowId.concat(",16"));
		    	empReqExcelRows.put("IssueStatus",status);	
		    	childRowResponse.add(empReqExcelRows);*/
		    	
		    }
		}
			
			
			
	    if (columnValue.get(17).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(17).toString() ==null || columnValue.get(17).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(17).toString().isEmpty() ) {
				String status="kindly check the Employee projectName  : "+columnValue.get(17).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",17"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
	    if (columnValue.get(18).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(18).toString() ==null || columnValue.get(18).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(18).toString().isEmpty() ) {
				String status="kindly check the Employee projectAllocationStarDate  : "+columnValue.get(18).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",18"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				try{
					Date projectAllocationStarDate=dateFormat.parse(columnValue.get(18).toString());					
				}catch(Exception e){
					log.info("Error"+e);
					String status="kindly check the Employee projectAllocationStarDate  : "+e;
					empReqExcelRows.put("RNo",rowId.concat(",18"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}
			}
			
	    if (columnValue.get(19).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(19).toString() ==null || columnValue.get(19).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(19).toString().isEmpty() ) {
				String status="kindly check the Employee projectAllocationEndDate  : "+columnValue.get(19).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",19"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				try{
					Date projectAllocationEndDate=dateFormat.parse(columnValue.get(19).toString());						
					Date projectAllocationStartDate=dateFormat.parse(columnValue.get(18).toString());
					if(projectAllocationEndDate.before(projectAllocationStartDate)){
						String status="projectAllocationEndDate should not lessthan projectAllocationStarDate : "+columnValue.get(19).toString();
						empReqExcelRows.put("RNo",rowId.concat(",19"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
					}				
					
				}catch(Exception e){
					log.info("Error"+e);
					String status="kindly check the Employee projectAllocationStarDate  : "+e;
					empReqExcelRows.put("RNo",rowId.concat(",19"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}
				
				
			}
			
	    if (columnValue.get(20).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(20).toString() ==null || columnValue.get(20).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(20).toString().isEmpty()) {						
				String status="kindly check the Physically Challenged : "+columnValue.get(20).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",20"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else if (!"YES".equalsIgnoreCase(columnValue.get(20).toString()) && !"NO".equalsIgnoreCase(columnValue.get(20).toString())){
				String	status="kindly check the Physically Challenged column,please use (YES/NO): "+columnValue.get(20).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",20"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
			
	    if (columnValue.get(21).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(21).toString() ==null || columnValue.get(21).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(21).toString().isEmpty() || !columnValue.get(21).toString().replace(" ", "").trim().matches(charAt)) {						
				String status="kindly check the Role : "+columnValue.get(21).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",21"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else if (!"webuser".equalsIgnoreCase(columnValue.get(21).toString())){
				String	status="kindly check the Role : "+columnValue.get(21).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",21"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
	    if (columnValue.get(22).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(22).toString() ==null || columnValue.get(22).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(22).toString().isEmpty()) {						
				String status="kindly check the Login UserName : "+columnValue.get(22).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",22"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{			
					    	List<EFmFmUserMasterPO> employeeUserId = userMasterBO.getSpecificUserDetailsByUserName(columnValue.get(22).toString().trim());
						    if (!employeeUserId.isEmpty()) {
								Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();						  
									 String status="Login UserName already available on database  : "+columnValue.get(22).toString();
										empReqExcelRows.put("RNo",rowId.concat(",22"));
										empReqExcelRows.put("IssueStatus",status);
										childRowResponse.add(empReqExcelRows);
								 } 
					    		  				
		
			}
			
			
	    if (columnValue.get(23).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(23).toString() ==null || columnValue.get(23).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(23).toString().isEmpty()) {						
				String status="kindly check the Zone Name : "+columnValue.get(23).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",23"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				 List<EFmFmClientRouteMappingPO> ZonerouteExist = iRouteDetailBO.getParticularRouteDetailByClient(combinedFacility,columnValue.get(23).toString().trim());
		            if (ZonerouteExist.isEmpty()) {
//		            	log.info("combinedFacility"+combinedFacility);
		            	String status="Zone Name  not available on database: "+columnValue.get(23).toString();
		            	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
		            	empReqExcelRows.put("RNo",rowId.concat(",23"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
		            }
			}
			
			
	    if (columnValue.get(24).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(24).toString() ==null || columnValue.get(24).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(24).toString().isEmpty()) {						
				String status="kindly check the Week Off : "+columnValue.get(24).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",24"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				//Week Off Validation
			}
			
			//need to test
			
	    if (columnValue.get(25).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(25).toString() ==null || columnValue.get(25).toString().isEmpty()) {						
				String status="kindly check the EmployeeGeoCodes : "+columnValue.get(25).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",25"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				//EmployeeGeoCodes Validation
			}
			
	    	if (columnValue.get(26).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(26).toString() ==null || columnValue.get(26).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(26).toString().isEmpty() ) {
				String status="kindly check the NodalPointName : "+columnValue.get(26).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",26"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else if (!columnValue.get(26).toString().trim().equalsIgnoreCase("default")){
				 List<EFmFmAreaNodalMasterPO> nodalPointExistDetail = iRouteDetailBO
				            .getParticularNodalPointNameDetails(columnValue.get(26).toString().trim());				 
				    if (nodalPointExistDetail.isEmpty()) {
				    	String status = "Nodal Point not available-" + columnValue.get(26).toString().trim();
				    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				    	empReqExcelRows.put("RNo",rowId.concat(",26"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
				        
				    }
			}
	    	if(noOfcolumn !=32){
	    	
		    	if (columnValue.get(27).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(27).toString() ==null || columnValue.get(27).toString().replace(".0","").trim().equalsIgnoreCase("") 
						|| columnValue.get(27).toString().isEmpty()) {
		    	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
		    		String status="kindly check the Spoc employee Id : "+columnValue.get(27).toString();
					empReqExcelRows.put("RNo",rowId.concat(",27"));
					empReqExcelRows.put("IssueStatus",status);	
					childRowResponse.add(empReqExcelRows);
				}else if (!columnValue.get(27).toString().trim().equalsIgnoreCase("default")){
						List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO.getEmpDetailsFromEmployeeId(columnValue.get(27).toString().trim());
					    if (employeeDetails.isEmpty()) {
					    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					    	String status = " Spoc Employee Id is not available,kinldy make sure Spoc Id Details before uploading EmployeeId details. " + columnValue.get(27).toString().trim();
					    	empReqExcelRows.put("RNo",rowId.concat(",27"));
							empReqExcelRows.put("IssueStatus",status);
							childRowResponse.add(empReqExcelRows);
					    } 
					}    	
	    	}
	    	
			
			if(noOfcolumn==32){
				
				Date requestFromDate=null;
			if("pickup".equalsIgnoreCase(columnValue.get(30).toString().trim())  || "drop".equalsIgnoreCase(columnValue.get(30).toString().trim())){				
				
				if (columnValue.get(27).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(27).toString() ==null || columnValue.get(27).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(27).toString().isEmpty() ) {
					String status="kindly check the Request Start Date: "+columnValue.get(27).toString();
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					empReqExcelRows.put("RNo",rowId.concat(",27"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
					
				}else{	
					if("pickup".equalsIgnoreCase(columnValue.get(30).toString().trim())){
						Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
						try{						
							    String shiftTimeInDateTimeFormate = columnValue.get(29).toString();
							    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
							    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());
							    requestFromDate = dateFormat.parse(columnValue.get(27).toString());						   
							    String reqFromDate = dateHypenFormat.format(requestFromDate) + " " + shifTimeFormate.format(shiftTime);	    
								if (dateTimeFormate.parse(reqFromDate).getTime() <= new Date().getTime()) {
									
									String status ="Could not able to raise the back dated request ,kindly check the request Start Date & shift time-" + columnValue.get(27).toString();
									empReqExcelRows.put("RNo",rowId.concat(",27"));
									empReqExcelRows.put("IssueStatus",status);
									childRowResponse.add(empReqExcelRows);
							    }	
							}catch (Exception e){
								log.info("Error"+e);
								String status="kindly check the employee request Start Date format 'DD-MM-YYYY' "+e;
								empReqExcelRows.put("RNo",rowId.concat(",27"));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
							}
						}else{
							Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
							try{		
							    requestFromDate = dateFormat.parse(columnValue.get(27).toString().trim());						 	    
								if (requestFromDate.before(new Date())) {
									String status ="kinldy check the request Start Date-" + columnValue.get(27).toString();
									empReqExcelRows.put("RNo",rowId.concat(",27"));
									empReqExcelRows.put("IssueStatus",status);	
									childRowResponse.add(empReqExcelRows);
							    }	
							}catch (Exception e){
								log.info("Error"+e);
								String status="kindly check the employee request Start Date format 'DD-MM-YYYY' "+e;
								empReqExcelRows.put("RNo",rowId.concat(",27"));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
							}
							
						}
				}	
				if (columnValue.get(28).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(28).toString() ==null || columnValue.get(28).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(28).toString().isEmpty() ) {
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the RequestEndDate: "+columnValue.get(28).toString();
					empReqExcelRows.put("RNo",rowId.concat(",28"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
					
				}else{	
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					try{		    						    						    
						    Date requestEndDate = dateFormat.parse(columnValue.get(28).toString().trim());						   			
							if (requestEndDate.before(requestFromDate)) {
								String status ="kinldy check the request end Date,should not less than Request start Date" + columnValue.get(28).toString();
								empReqExcelRows.put("RNo",rowId.concat(",28"));
								empReqExcelRows.put("IssueStatus",status);	
								childRowResponse.add(empReqExcelRows);
						    }	
						}catch (Exception e){
							log.info("Error"+e);
							String status="kindly check the employee request End Date format 'DD-MM-YYYY' "+e;
							 empReqExcelRows.put("RNo",rowId.concat(",28"));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
						}
				}	
				
				if("pickup".equalsIgnoreCase(columnValue.get(30).toString().trim())){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
						if (columnValue.get(29).toString() ==null || columnValue.get(29).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
								|| columnValue.get(29).toString().isEmpty() ) {
							String status="kindly check the shiftTime : "+columnValue.get(29).toString();
							 empReqExcelRows.put("RNo",rowId.concat(",29"));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
						}else{
							try{
								String shiftTimeInDateTimeFormate = columnValue.get(29).toString();
							    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
							    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());					    
								}catch (Exception e){
									log.info("Error"+e);
									String status="kindly check the employee shiftTime Time format:'HH:MM:SS' ex:16:00:00"+e;
									 empReqExcelRows.put("RNo",rowId.concat(",29"));
										empReqExcelRows.put("IssueStatus",status);
										childRowResponse.add(empReqExcelRows);
								}
						}
				}else if("drop".equalsIgnoreCase(columnValue.get(30).toString().trim())){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
						if (columnValue.get(29).toString() ==null || columnValue.get(29).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
								|| columnValue.get(29).toString().isEmpty() || !columnValue.get(29).toString().replace(" ", "").trim().matches(validInt) ) {
							String status="kindly check the Drop sequence : "+columnValue.get(29).toString();
							 empReqExcelRows.put("RNo",rowId.concat(",29"));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
						}					
							
				}else{	
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Trip Type : "+columnValue.get(30).toString();
					 empReqExcelRows.put("RNo",rowId.concat(",30"));
						empReqExcelRows.put("IssueStatus",status);	
						childRowResponse.add(empReqExcelRows);
				}				
				if (columnValue.get(31).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(31).toString() ==null || columnValue.get(31).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(31).toString().isEmpty() || !columnValue.get(31).toString().replace(" ", "").trim().matches(validInt)) {
					String status="kindly check the Host Mobile Number and Mobile Should be Integer : "+columnValue.get(31).toString();
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					 empReqExcelRows.put("RNo",rowId.concat(",31"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
				}else if(columnValue.get(31).toString().trim().length()<5 || columnValue.get(31).toString().trim().length()>18 ){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				    String status="kindly check the Host Mobile Number length(ex:India -12 digit),length should be country Code with MobileNumber  : "+columnValue.get(31).toString();
				    empReqExcelRows.put("RNo",rowId.concat(",31"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
					/*	
					List<EFmFmUserMasterPO> employeeMobileNo = iEmployeeDetailBO.getEmpMobileNoDetails(columnValue.get(31).toString().trim(), branchId);
						 if(!employeeMobileNo.isEmpty()){
							 String status="Mobile Number already available on database  : "+columnValue.get(31).toString();
								empReqExcelRows.put(rowId.concat(",31"), status);
						 }
					*/
					
				}/*else{
						Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					    String status="kindly check the Host Mobile Number length(ex:India -12 digit),length should be country Code with MobileNumber  : "+columnValue.get(31).toString();
					    empReqExcelRows.put("RNo",rowId.concat(",31"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
				}*/
				
				}	
			}
    }catch(Exception e){
  	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
	    String status="Please check employee data,should not contain  blank columns and special symbols like ',@ etc.Please check zoneName and other columns For the same";
		empReqExcelRows.put("IssueStatus",status);
		childRowResponse.add(empReqExcelRows);
        log.info("ImportEmploye empReqExcelRows Exception" + e); 
        return childRowResponse;

    }
   		return childRowResponse;
	
}
    
    
    
    /*
	 * EmployeeMaster guest Uplaod
	 */
    private List<Map<String, Object>> empGuestExcelValidator(String rowId,ArrayList<Object> columnValue,int branchId,int noOfcolumn,String combinedFacility) throws UnsupportedEncodingException{
	    IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");		   
	    IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

	    DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	    DateFormat dateHypenFormat = new SimpleDateFormat("dd-MM-yyyy");
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	    DateFormat shifTimeFormate = new SimpleDateFormat("HH:mm");	
	    List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();
	    List<EFmFmZoneMasterPO> routeExistDetail;
	    String validInt="^[0-9]*$";
	    String charAt="^[a-zA-Z]*$";
	    
	    try{
		
	    if (columnValue.get(0).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(0).toString() ==null || columnValue.get(0).toString().replace(".0","").trim().equalsIgnoreCase("") 
					|| columnValue.get(0).toString().isEmpty()) {
	    	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
	    		String status="kindly check the employee Id : "+columnValue.get(0).toString();
				empReqExcelRows.put("RNo",rowId.concat(",0"));
				empReqExcelRows.put("IssueStatus",status);	
				childRowResponse.add(empReqExcelRows);
			}else{
				List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO.getEmpDetailsFromEmployeeId(columnValue.get(0).toString().trim());
			    if (!employeeDetails.isEmpty()) {
			    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			    	String status = "Employee Id already available -" + columnValue.get(0).toString().trim();
			    	empReqExcelRows.put("RNo",rowId.concat(",0"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
			    } 
			}
			
	    if (columnValue.get(1).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(1).toString() ==null || columnValue.get(1).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(1).toString().isEmpty() ) {
	    		Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the employee Name column value : "+columnValue.get(1).toString();
				empReqExcelRows.put("RNo",rowId.concat(",1"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
	    if (columnValue.get(2).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(2).toString() ==null || columnValue.get(2).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(2).toString().isEmpty() || !columnValue.get(2).toString().replace(" ", "").trim().matches(charAt)) {
	    		Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the employee sex : "+columnValue.get(3).toString();
				empReqExcelRows.put("RNo",rowId.concat(",2"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
				
			}else if (!("Female".equalsIgnoreCase(columnValue.get(2).toString().trim()) || "Male".equalsIgnoreCase(columnValue.get(2).toString().trim()))){
				String	status="kindly check the employee sex : "+columnValue.get(2).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",2"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
		    if (columnValue.get(3).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(3).toString() ==null || columnValue.get(3).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(3).toString().isEmpty() || !columnValue.get(3).toString().replace(" ", "").trim().matches(validInt)) {
				String status="kindly check the Mobile Number and Mobile Should be Integer : "+columnValue.get(3).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",3"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else if(columnValue.get(3).toString().trim().length()>5 && columnValue.get(4).toString().trim().length()<=18){							
					 List<EFmFmUserMasterPO> employeeMobileNo = iEmployeeDetailBO.getParticularEmpDetailsFromMobileNumber(
                             Base64.getEncoder().encodeToString(columnValue.get(3).toString().trim().getBytes("utf-8")));
					 if(!employeeMobileNo.isEmpty()){
						 String status="Mobile Number already available on database  : "+columnValue.get(3).toString();
						 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
						 empReqExcelRows.put("RNo",rowId.concat(",3"));
							empReqExcelRows.put("IssueStatus",status);
							childRowResponse.add(empReqExcelRows);
					 }
				
			}else{
				 String status="kindly check the Mobile Number length(ex:India -12 digit),length should be country Code with MobileNumber  : "+columnValue.get(3).toString();
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();	
				 empReqExcelRows.put("RNo",rowId.concat(",3"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
			}
	    if (columnValue.get(4).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(4).toString() ==null || columnValue.get(4).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(4).toString().isEmpty()) {
				String status="kindly check the Email Id : "+columnValue.get(4).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",4"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else {	
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();						  
						    	List<EFmFmUserMasterPO> employeeEmailId = iEmployeeDetailBO.getParticularEmployeeDetailsFromEmailId(
                                        Base64.getEncoder().encodeToString(columnValue.get(4).toString().trim().getBytes("utf-8")));
							      if(!employeeEmailId.isEmpty()){					    	  
										 String status="Email Id already available on database  : "+columnValue.get(4).toString();
										 empReqExcelRows.put("RNo",rowId.concat(",4"));
											empReqExcelRows.put("IssueStatus",status);
											childRowResponse.add(empReqExcelRows);
									 } 
						    		  				
			}	
			
	   
			
	    if (columnValue.get(5).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(5).toString() ==null || columnValue.get(5).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(5).toString().isEmpty() ) {
				String status="kindly check the Current Address : "+columnValue.get(5).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",5"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
		
				
			
	    if (columnValue.get(6).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(6).toString() ==null || columnValue.get(6).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(6).toString().isEmpty()) {						
				String status="kindly check the Login UserName : "+columnValue.get(6).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",6"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
	    else{			
	    	List<EFmFmUserMasterPO> employeeUserId = userMasterBO.getSpecificUserDetailsByUserName(columnValue.get(6).toString().trim());
		    if (!employeeUserId.isEmpty()) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();						  
					 String status="Login UserName already available on database  : "+columnValue.get(6).toString();
						empReqExcelRows.put("RNo",rowId.concat(",22"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
				 } 
	    		  				

}
			
			
	    if (columnValue.get(7).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(7).toString() ==null || columnValue.get(7).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(8).toString().isEmpty()) {						
				String status="kindly check the Zone Name : "+columnValue.get(7).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",7"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				 List<EFmFmClientRouteMappingPO> ZonerouteExist = iRouteDetailBO.getParticularRouteDetailByClient(combinedFacility,columnValue.get(7).toString().trim());
		            if (ZonerouteExist.isEmpty()) {
		            	String status="Zone Name  not available on database: "+columnValue.get(7).toString();
		            	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
		            	empReqExcelRows.put("RNo",rowId.concat(",7"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
		            }
			}
			
			
	    if (columnValue.get(8).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(9).toString() ==null || columnValue.get(8).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(8).toString().isEmpty()) {						
				String status="kindly check the Week Off : "+columnValue.get(8).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",8"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				//Week Off Validation
			}
			
			//need to test
			
	    if (columnValue.get(9).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(9).toString() ==null || columnValue.get(9).toString().isEmpty()) {						
				String status="kindly check the EmployeeGeoCodes : "+columnValue.get(9).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",9"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				//EmployeeGeoCodes Validation
			}
			    
	    
			
			if(noOfcolumn==15){				
				Date requestFromDate=null;
			if("pickup".equalsIgnoreCase(columnValue.get(13).toString().trim())  || "drop".equalsIgnoreCase(columnValue.get(13).toString().trim())){								
				if (columnValue.get(10).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(10).toString() ==null || columnValue.get(10).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(10).toString().isEmpty() ) {
					String status="kindly check the Request Start Date: "+columnValue.get(10).toString();
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					empReqExcelRows.put("RNo",rowId.concat(",10"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
					
				}else{	
					if("pickup".equalsIgnoreCase(columnValue.get(13).toString().trim())){
						Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
						try{						
							    String shiftTimeInDateTimeFormate = columnValue.get(12).toString();
							    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
							    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());
							    requestFromDate = dateFormat.parse(columnValue.get(10).toString());						   
							    String reqFromDate = dateHypenFormat.format(requestFromDate) + " " + shifTimeFormate.format(shiftTime);	    
								if (dateTimeFormate.parse(reqFromDate).getTime() <= new Date().getTime()) {				
									String status ="Could not able to raise the back dated request ,kindly check the request Start Date & shift time-" + columnValue.get(10).toString();
									empReqExcelRows.put("RNo",rowId.concat(",10"));
									empReqExcelRows.put("IssueStatus",status);
									childRowResponse.add(empReqExcelRows);
							    }	
							}catch (Exception e){
								log.info("Error"+e);
								String status="kindly check the employee request Start Date format 'DD-MM-YYYY' "+e;
								empReqExcelRows.put("RNo",rowId.concat(",10"));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
							}
						}else{
							Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
							try{		
							    requestFromDate = dateFormat.parse(columnValue.get(10).toString().trim());						 	    
								if (requestFromDate.before(new Date())) {
									String status ="kinldy check the request Start Date-" + columnValue.get(10).toString();
									empReqExcelRows.put("RNo",rowId.concat(",10"));
									empReqExcelRows.put("IssueStatus",status);	
									childRowResponse.add(empReqExcelRows);
							    }	
							}catch (Exception e){
								log.info("Error"+e);
								String status="kindly check the employee request Start Date format 'DD-MM-YYYY' "+e;
								empReqExcelRows.put("RNo",rowId.concat(",10"));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
							}
							
						}
				}	
				if (columnValue.get(11).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(11).toString() ==null || columnValue.get(11).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(11).toString().isEmpty() ) {
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the RequestEndDate: "+columnValue.get(11).toString();
					empReqExcelRows.put("RNo",rowId.concat(",11"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
					
				}else{	
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					try{		    						    						    
						    Date requestEndDate = dateFormat.parse(columnValue.get(11).toString().trim());						   			
							if (requestEndDate.before(requestFromDate)) {
								String status ="kinldy check the request end Date,should not less than Request start Date" + columnValue.get(11).toString();
								empReqExcelRows.put("RNo",rowId.concat(",11"));
								empReqExcelRows.put("IssueStatus",status);	
								childRowResponse.add(empReqExcelRows);
						    }	
						}catch (Exception e){
							log.info("Error"+e);
							String status="kindly check the employee request End Date format 'DD-MM-YYYY' "+e;
							 empReqExcelRows.put("RNo",rowId.concat(",11"));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
						}
				}	
				
				if("pickup".equalsIgnoreCase(columnValue.get(13).toString().trim())){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					
						if (columnValue.get(12).toString() ==null || columnValue.get(12).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
								|| columnValue.get(12).toString().isEmpty() ) {
							String status="kindly check the shiftTime : "+columnValue.get(12).toString();
							 empReqExcelRows.put("RNo",rowId.concat(",12"));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
						}else{
							try{
								String shiftTimeInDateTimeFormate = columnValue.get(12).toString();
							    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
							    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());					    
								}catch (Exception e){
									log.info("Error"+e);
									String status="kindly check the employee shiftTime Time format:'HH:MM:SS' ex:16:00:00"+e;
									 empReqExcelRows.put("RNo",rowId.concat(",12"));
										empReqExcelRows.put("IssueStatus",status);
										childRowResponse.add(empReqExcelRows);
								}
						}
				}else if("drop".equalsIgnoreCase(columnValue.get(13).toString().trim())){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
						if (columnValue.get(12).toString() ==null || columnValue.get(12).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
								|| columnValue.get(12).toString().isEmpty() || !columnValue.get(12).toString().replace(" ", "").trim().matches(validInt) ) {
							String status="kindly check the Drop sequence : "+columnValue.get(12).toString();
							 empReqExcelRows.put("RNo",rowId.concat(",12"));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
						}					
							
				}else{	
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Trip Type : "+columnValue.get(13).toString();
					 empReqExcelRows.put("RNo",rowId.concat(",13"));
						empReqExcelRows.put("IssueStatus",status);	
						childRowResponse.add(empReqExcelRows);
				}				
				if (columnValue.get(14).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(14).toString() ==null || columnValue.get(14).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(14).toString().isEmpty() || !columnValue.get(14).toString().replace(" ", "").trim().matches(validInt)) {
					String status="kindly check the Host Mobile Number and Mobile Should be Integer : "+columnValue.get(14).toString();
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					 empReqExcelRows.put("RNo",rowId.concat(",14"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
				}else if(columnValue.get(14).toString().trim().length()<5 || columnValue.get(14).toString().trim().length()>18 ){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				    String status="kindly check the Host Mobile Number length(ex:India -12 digit),length should be country Code with MobileNumber  : "+columnValue.get(14).toString();
				    empReqExcelRows.put("RNo",rowId.concat(",14"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}				
				}	
			}
    }catch(Exception e){
  	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
	    String status="Please check employee data,should not contain  blank columns and special symbols like ',@ etc.Please check zoneName and other columns For the same";
		empReqExcelRows.put("IssueStatus",status);
		childRowResponse.add(empReqExcelRows);
        log.info("ImportEmploye empReqExcelRows Exception" + e); 
        return childRowResponse;

    }
   		return childRowResponse;
	
}
    

    /*
     * @xl utility row values are inserted into employee master table table.
     * validation also be handle here. Fetching the AreaId from based on the
     * employee areaName from efmfmareamaster Table.
     * 
     * @author Rajan R
     * 
     * @since 2015-05-12
     */
    private List<Map<String, Object>> employeeRecord(Map<Integer, Object> empMasterRecords, int branchId ,int profileId,String combinedFacility)
            throws ParseException, InvalidKeyException, NoSuchAlgorithmException, IOException, URISyntaxException {
        IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
		 IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
			ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");

        IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        List<Map<String, Object>> empMasterExcel = new ArrayList<Map<String, Object>>();
	    Map<String, Object> issueList = new HashMap<String, Object>();
        log.info("importing data");
        String status = "success-";
        try{							
			for (Entry<Integer, Object> entry : empMasterRecords.entrySet()) {
		        log.info("data enrty");
					issueList = new HashMap<String, Object>();                
					ArrayList empMasterDetails = (ArrayList) entry.getValue();	
        List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO.getEmpDetailsFromEmployeeId(empMasterDetails.get(0).toString().trim());
        if (!(employeeDetails.isEmpty()) && employeeDetails.get(0).getStatus().equalsIgnoreCase("N")) {
            status = "empIdAlreadyDisable-" + empMasterDetails.get(0).toString().trim();
            issueList.put("RNo","");
			issueList.put("IssueStatus",status);
            empMasterExcel.add(issueList);
            return empMasterExcel;
        }

        try {
            log.info("Geo codes" + empMasterDetails.get(25).toString());
        } catch (Exception e) {
            status = "fail-";
            log.info("Geo codes" + status);
            issueList.put("RNo","");
			issueList.put("IssueStatus",status);
            empMasterExcel.add(issueList);
            return empMasterExcel;
        }
        try {

            log.info("Geo codes2" + empMasterDetails.get(26).toString().trim());
        } catch (Exception e) {
            status = "fail-";
            log.info("Geo codes2" + status);
            issueList.put("RNo","");
			issueList.put("IssueStatus",status);
            empMasterExcel.add(issueList);
            return empMasterExcel;

        }
        
        
        List<EFmFmFacilityToFacilityMappingPO> branchDetails=facilityBO.getParticularFacilityDetailFromBranchName(empMasterDetails.get(28).toString().trim());
        if(branchDetails.isEmpty()){
            status = "Please check-" + empMasterDetails.get(28).toString().trim()+" facility not exist.";
            issueList.put("RNo","");
			issueList.put("IssueStatus",status);
            empMasterExcel.add(issueList);
            return empMasterExcel;
        }
        else if(!(branchDetails.isEmpty()) && branchDetails.get(0).getFacilityStatus().equalsIgnoreCase("N") ){
            status = "Please check-" + empMasterDetails.get(28).toString().trim()+" facility is  disable.";
            issueList.put("RNo","");
			issueList.put("IssueStatus",status);
            empMasterExcel.add(issueList);
            return empMasterExcel;
        }
        
	    int baseBranchId=iUserMasterBO.getBranchIdFromBranchName(empMasterDetails.get(28).toString().trim());
		    EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
		    eFmFmClientBranch.setBranchId(baseBranchId);
	       if(!(facilityBO.checkFacilityAccess(profileId, baseBranchId))){
	           status = "Please check looks like you don't have access of -" + empMasterDetails.get(28).toString().trim()+" facility";
	           issueList.put("RNo","");
				issueList.put("IssueStatus",status);
	           empMasterExcel.add(issueList);
	           return empMasterExcel; 
	       }
        
		//New Changes   
        try {
            List<EFmFmClientRouteMappingPO> routeExistDetail = iRouteDetailBO.getParticularRouteDetailByClient(combinedFacility,
                    empMasterDetails.get(23).toString().trim());
            if (routeExistDetail.isEmpty()) {
                status = "routeNotExist-" + empMasterDetails.get(23).toString().trim();
                issueList.put("RNo","");
				issueList.put("IssueStatus",status);
                empMasterExcel.add(issueList);
                return empMasterExcel;
            }
            List<EFmFmAreaNodalMasterPO> nodalPointExistDetail = iRouteDetailBO
                    .getParticularNodalPointNameDetails(empMasterDetails.get(26).toString().trim());
            if (nodalPointExistDetail.isEmpty()) {
                status = "nodalPointNotExist-" + empMasterDetails.get(26).toString().trim();
                issueList.put("RNo","");
				issueList.put("IssueStatus",status);
                empMasterExcel.add(issueList);
                return empMasterExcel;
            }
            EFmFmUserMasterPO employeeDetailsPO = new EFmFmUserMasterPO();
            EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO = new EFmFmClientProjectDetailsPO();
            employeeDetailsPO.setEmployeeId(empMasterDetails.get(0).toString().trim());
            try{
            List<EFmFmUserMasterPO> employeeUserName = iEmployeeDetailBO
                    .getParticularEmpDetailsFromEmployeeId(empMasterDetails.get(22).toString(),combinedFacility); 
            if(!(employeeUserName.isEmpty())){
                status = "UseName-" + empMasterDetails.get(22).toString().trim()+" already exist.";
                issueList.put("RNo","");
    			issueList.put("IssueStatus",status);
                empMasterExcel.add(issueList);
                return empMasterExcel;
            }}catch(Exception e){
            	log.info("userName error"+e);
            }
            
            
            employeeDetailsPO.setUserName(empMasterDetails.get(22).toString().trim());           
            PasswordEncryption passwordEncryption= new PasswordEncryption();
            employeeDetailsPO.setPassword(passwordEncryption.PasswordEncoderGenerator(empMasterDetails.get(0).toString().trim()));           
            employeeDetailsPO.setFirstName(Base64.getEncoder().encodeToString(empMasterDetails.get(1).toString().trim().getBytes("utf-8")));
            if(empMasterDetails.get(2).toString().equalsIgnoreCase("CELL_TYPE_BLANK"))
            employeeDetailsPO.setMiddleName("");
            else
            employeeDetailsPO.setMiddleName(Base64.getEncoder().encodeToString(empMasterDetails.get(2).toString().trim().getBytes("utf-8")));
            if(empMasterDetails.get(3).toString().equalsIgnoreCase("CELL_TYPE_BLANK"))
            employeeDetailsPO.setLastName("");
            else
            employeeDetailsPO.setLastName(Base64.getEncoder().encodeToString(empMasterDetails.get(3).toString().trim().getBytes("utf-8")));	
            employeeDetailsPO.setGender(Base64.getEncoder().encodeToString(empMasterDetails.get(4).toString().trim().getBytes("utf-8")));
            employeeDetailsPO.setEmployeeBusinessUnit(Base64.getEncoder().encodeToString(empMasterDetails.get(5).toString().trim().getBytes("utf-8")));
            employeeDetailsPO.setEmployeeDepartment(Base64.getEncoder().encodeToString(empMasterDetails.get(6).toString().trim().getBytes("utf-8")));
            employeeDetailsPO.setEmployeeDomain(Base64.getEncoder().encodeToString(empMasterDetails.get(7).toString().trim().getBytes("utf-8")));
            employeeDetailsPO.setMobileNumber(Base64.getEncoder().encodeToString(empMasterDetails.get(8).toString().trim().getBytes("utf-8")));
            employeeDetailsPO.setEmailId(Base64.getEncoder().encodeToString(empMasterDetails.get(9).toString().trim().getBytes("utf-8")));
            employeeDetailsPO.setStateName(Base64.getEncoder().encodeToString(empMasterDetails.get(11).toString().trim().getBytes("utf-8")));
            employeeDetailsPO.setCityName(Base64.getEncoder().encodeToString(empMasterDetails.get(12).toString().trim().getBytes("utf-8")));
            employeeDetailsPO.setDeviceId("NO");
            // employeeDetailsPO.setPinCode(Integer.parseInt(columnValue.get(13).toString()));
            employeeDetailsPO.setAddress(
                    Base64.getEncoder().encodeToString(empMasterDetails.get(14).toString().trim().getBytes("utf-8")));
            employeeDetailsPO.setEmployeeDesignation(
                    Base64.getEncoder().encodeToString(empMasterDetails.get(15).toString().trim().getBytes("utf-8")));
            employeeDetailsPO.setPhysicallyChallenged(
                    Base64.getEncoder().encodeToString(empMasterDetails.get(20).toString().trim().getBytes("utf-8")));
            employeeDetailsPO.setIsInjured(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
            employeeDetailsPO.setPragnentLady(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
            
            employeeDetailsPO.setIsVIP(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
            employeeDetailsPO.setHostMobileNumber(Base64.getEncoder().encodeToString("N/A".getBytes("utf-8")));
            employeeDetailsPO.setOHRId(empMasterDetails.get(22).toString().trim());
            employeeDetailsPO.setCountry("India");
            employeeDetailsPO.setCreationTime(new Date());
            employeeDetailsPO.setUpdatedTime(new Date());
            eFmFmClientProjectDetailsPO.setClientProjectId(empMasterDetails.get(16).toString());
            eFmFmClientProjectDetailsPO.setEmployeeProjectName(empMasterDetails.get(17).toString().trim());
            eFmFmClientProjectDetailsPO
                    .setProjectAllocationStarDate(dateFormat.parse(empMasterDetails.get(18).toString().trim()));
            eFmFmClientProjectDetailsPO
                    .setProjectAllocationEndDate(dateFormat.parse(empMasterDetails.get(19).toString().trim()));
            try{
            	String weekOff=empMasterDetails.get(24).toString().trim();
            	log.info(weekOff.toString());
                employeeDetailsPO.setWeekOffDays(empMasterDetails.get(24).toString().trim());
            }
            catch(Exception e){
                employeeDetailsPO.setWeekOffDays("Saturday,Sunday");	
            }
            
            employeeDetailsPO.seteFmFmClientBranchPO(eFmFmClientBranch);
            employeeDetailsPO.setDeviceId("NO");
            employeeDetailsPO.setPanicNumber(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
            employeeDetailsPO.setSecondaryPanicNumber(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
            employeeDetailsPO.setTempPassWordChange(false);
            employeeDetailsPO.setEmployeeProfilePic("NO");
            employeeDetailsPO.setStatus("Y");
            employeeDetailsPO.setLocationStatus("N");
            employeeDetailsPO.setLastLoginTime(new Date());
            employeeDetailsPO.setWrongPassAttempt(0);
            employeeDetailsPO.setUserType("employee");
            employeeDetailsPO.setPasswordChangeDate(new Date());
            
            
            
            
            employeeDetailsPO.setLatitudeLongitude(empMasterDetails.get(25).toString().trim().replaceAll("\\s+", "")); 
            
            try {
	                CalculateDistance empDistance=new CalculateDistance();                
	                String geoCodepoints=empDistance.generateLatLngByaddress(empMasterDetails.get(14).toString().trim().replaceAll("\\s+", ""));
	                if(geoCodepoints.equalsIgnoreCase("unknown")){
	                	 employeeDetailsPO.setHomeGeoCodePoints(empMasterDetails.get(25).toString().trim().replaceAll("\\s+", ""));
	                }else{
	                	employeeDetailsPO.setLatitudeLongitude(geoCodepoints.trim().replaceAll("\\s+", ""));
	                	employeeDetailsPO.setHomeGeoCodePoints(geoCodepoints.trim().replaceAll("\\s+", ""));
	                }                
	            	List<EFmFmClientBranchPO> branchdetail=iCabRequestBO.getBranchDetails(baseBranchId);
	            	employeeDetailsPO.setDistance(empDistance.employeeDistanceCalculation(branchdetail.get(0).getLatitudeLongitude(),
	            			empMasterDetails.get(25).toString().trim().replaceAll("\\s+", "")));
            } catch (Exception e) {
				log.info("Distance ccalculation error"+e);
            }                      	              
            
              /*
             * Generating latitute & longitute based on the Address
             */
            eFmFmClientProjectDetailsPO.seteFmFmClientBranchPO(eFmFmClientBranch);
            eFmFmClientProjectDetailsPO.setIsActive("A");
            eFmFmClientProjectDetailsPO.setCreatedDate(new Date());
            EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO=new EFmFmEmployeeProjectDetailsPO();

            /*
             * Project Id Already Exit
             * 
             */
            List<EFmFmClientProjectDetailsPO> projectIdExist = iEmployeeDetailBO
                    .getProjectDetails(eFmFmClientProjectDetailsPO.getClientProjectId().toUpperCase(), combinedFacility);
            if (projectIdExist.isEmpty()) {            	
                iEmployeeDetailBO.save(eFmFmClientProjectDetailsPO);
                List<EFmFmClientProjectDetailsPO> projectDetails = iEmployeeDetailBO
                        .getProjectDetails(eFmFmClientProjectDetailsPO.getClientProjectId().toUpperCase(), combinedFacility);
                if (!(projectDetails.isEmpty())) {
                    employeeDetailsPO.seteFmFmClientBranchPO(eFmFmClientBranch);
                    employeeDetailsPO.seteFmFmClientProjectDetails(projectDetails.get(0));
                }          
            }else {          	
            	if(empMasterDetails.get(27).toString().trim().equalsIgnoreCase("default")){
	                employeeDetailsPO.seteFmFmClientBranchPO(eFmFmClientBranch);	              
	                employeeDetailsPO.seteFmFmClientProjectDetails(projectIdExist.get(0));      
	                
            	}   
            	List<EFmFmUserMasterPO> repEmployeeDetails = iEmployeeDetailBO.getEmpDetailsFromEmployeeId
            			(empMasterDetails.get(27).toString().trim());
			    if (!repEmployeeDetails.isEmpty()){     	
	            	eFmFmEmployeeProjectDetailsPO.setReportingManagerUserId(String.valueOf(repEmployeeDetails.get(0).getUserId()));
			    	employeeDetailsPO.setReportingManagerUserId(repEmployeeDetails.get(0).getUserId());
			    }            	
            }
            
            

            List<EFmFmAreaMasterPO> areaId = iRouteDetailBO
                    .getParticularAreaNameDetails(empMasterDetails.get(10).toString().trim());
            if (areaId.isEmpty()) {
                EFmFmAreaMasterPO efmFmAreaMaster = new EFmFmAreaMasterPO();
                efmFmAreaMaster.setAreaDescription(empMasterDetails.get(10).toString().trim());
                efmFmAreaMaster.setAreaName(empMasterDetails.get(10).toString().trim());
                iRouteDetailBO.saveAreaRecord(efmFmAreaMaster);
                areaId = iRouteDetailBO.getParticularAreaNameDetails(empMasterDetails.get(10).toString().trim());		 	      
                EFmFmClientAreaMappingPO clientAreaMapping=new EFmFmClientAreaMappingPO();
			 	  clientAreaMapping.seteFmFmAreaMaster(areaId.get(0));
			 	  clientAreaMapping.setFacilityWiseDistance(0);
			 	  clientAreaMapping.seteFmFmClientBranchPO(eFmFmClientBranch);
			 	 iRouteDetailBO.save(clientAreaMapping);			 	
            }
            List<EFmFmRouteAreaMappingPO> routeAreaId = iRouteDetailBO
                    .getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(areaId.get(0).getAreaId(), combinedFacility,
                            routeExistDetail.get(0).geteFmFmZoneMaster().getZoneId(),
                            nodalPointExistDetail.get(0).getNodalPointId());
            if (routeAreaId.isEmpty()) {
                EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
                routeAreaMapping.setEfmFmAreaMaster(areaId.get(0));
                routeAreaMapping.seteFmFmZoneMaster(routeExistDetail.get(0).geteFmFmZoneMaster());
                routeAreaMapping.seteFmFmNodalAreaMaster(nodalPointExistDetail.get(0));
                iRouteDetailBO.save(routeAreaMapping);
                routeAreaId = iRouteDetailBO.getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(
                        areaId.get(0).getAreaId(), combinedFacility, routeExistDetail.get(0).geteFmFmZoneMaster().getZoneId(),
                        nodalPointExistDetail.get(0).getNodalPointId());
            }
   //         log.info("routeAreaId" + routeAreaId);
            employeeDetailsPO.seteFmFmRouteAreaMapping(routeAreaId.get(0));
            /*
             * employee Record already existing on table.
             */
            // String userRole=columnValue.get(21).toString().toUpperCase();
            String userRole = "webuser";
            if (!empMasterDetails.get(0).toString().trim().isEmpty() && empMasterDetails.get(0).toString().trim() != null
                    && empMasterDetails.get(0).toString().trim() != "" && empMasterDetails.get(9).toString().trim() != ""
                    && !empMasterDetails.get(9).toString().trim().isEmpty() && empMasterDetails.get(8).toString().trim() != ""
                    && !empMasterDetails.get(8).toString().trim().isEmpty()) {
                List<EFmFmUserMasterPO> employeeIdExist = iEmployeeDetailBO
                        .getParticularEmpDetailsFromEmployeeId(empMasterDetails.get(0).toString().trim(),combinedFacility);
                if (employeeIdExist.isEmpty()) {
                    List<EFmFmUserMasterPO> employeeMobileNo = iEmployeeDetailBO
                            .getEmpMobileNoDetails(
                                    Base64.getEncoder().encodeToString(empMasterDetails.get(8).toString().trim().getBytes("utf-8")),combinedFacility);
                    if (employeeMobileNo.isEmpty()) {
                        List<EFmFmUserMasterPO> employeeEmailId = iEmployeeDetailBO
                                .getParticularEmployeeDetailsFromEmailId(
                                        Base64.getEncoder().encodeToString(empMasterDetails.get(9).toString().trim().getBytes("utf-8")));
                        if (employeeEmailId.isEmpty()) {
                            if (userRole.equalsIgnoreCase("manager")) {
                                List<EFmFmUserMasterPO> roleExist = iUserMasterBO.getUsersRoleExist(combinedFacility,
                                        employeeDetailsPO.geteFmFmClientProjectDetails().getClientProjectId(),
                                        userRole.trim());
                                if (!(roleExist.isEmpty())) {
                                    userRole = "webuser";
                                    iEmployeeDetailBO.save(employeeDetailsPO);
                                } else {
                                    iEmployeeDetailBO.save(employeeDetailsPO);
                                }
                            } else {
                                iEmployeeDetailBO.save(employeeDetailsPO);
                            }
                           List<EFmFmUserMasterPO> employeeIdDetails = iEmployeeDetailBO
                                    .getParticularEmpDetailsFromEmployeeId(empMasterDetails.get(0).toString().trim(),combinedFacility);                                               
                            if (!(employeeIdDetails.isEmpty())) {
                            	
                                List<EFmFmFacilityToFacilityMappingPO> facilityDetails= facilityBO.getAllAttachedActiveFacilities(branchDetails.get(0).geteFmFmClientBranchPO().getBranchId());  
                                if(!(branchDetails.isEmpty())){
                        			for(EFmFmFacilityToFacilityMappingPO facility:facilityDetails){
                                	EFmFmClientBranchPO eFmFmClient=new EFmFmClientBranchPO();
                                	eFmFmClient.setBranchId(iUserMasterBO.getBranchIdFromBranchName(facility.getBranchName()));
                       				 EFmFmUserFacilityMappingPO  eFmFmUserFacilityMapping=new EFmFmUserFacilityMappingPO();          				 
                    				 eFmFmUserFacilityMapping.setUserFacilityStatus("Y");
                    				 eFmFmUserFacilityMapping.setEfmFmUserMaster(employeeIdDetails.get(0));
                    				 eFmFmUserFacilityMapping.seteFmFmClientBranchPO(eFmFmClient);
                    				 facilityBO.save(eFmFmUserFacilityMapping);	
                        			}
                                }
                            	
                            	
                            	
                                EFmFmUserPasswordPO userPassword = new EFmFmUserPasswordPO();
                                // userPassword.setCreatedBy(createdBy);
                                userPassword.setCreationTime(new Date());
                                userPassword.setEfmFmUserMaster(employeeIdDetails.get(0));
                                userPassword.setPassword(passwordEncryption
                                        .PasswordEncoderGenerator(employeeIdDetails.get(0).toString().trim()));
                                userPassword.seteFmFmClientBranchPO(eFmFmClientBranch);
                                iEmployeeDetailBO.save(userPassword);

                                List<EFmFmRoleMasterPO> getRoleDetails = iUserMasterBO.getRoleId(userRole);
                                if (!(getRoleDetails.isEmpty())) {
                                    //adding default request module
                                    EFmFmClientUserRolePO eFmFmClientUserRolePO = new EFmFmClientUserRolePO();
                                    eFmFmClientUserRolePO.setEfmFmUserMaster(employeeIdDetails.get(0));
                                    eFmFmClientUserRolePO.setEfmFmRoleMaster(getRoleDetails.get(0));
                                    eFmFmClientUserRolePO.seteFmFmClientBranchPO(eFmFmClientBranch);
                                    EFmFmClientBranchSubConfigurationPO eFmFmClientBranchSubConfiguration = new EFmFmClientBranchSubConfigurationPO();
                                    eFmFmClientBranchSubConfiguration.setClientBranchSubConfigurationId(81);
                                    eFmFmClientUserRolePO.seteFmFmClientBranchSubConfiguration(eFmFmClientBranchSubConfiguration);
                                    iUserMasterBO.save(eFmFmClientUserRolePO);    
                                    //adding default Profile access
                                     EFmFmClientUserRolePO eFmFmClientUserRoleProfileMap = new EFmFmClientUserRolePO();
                                    eFmFmClientUserRoleProfileMap.setEfmFmUserMaster(employeeIdDetails.get(0));
                                    eFmFmClientUserRoleProfileMap.setEfmFmRoleMaster(getRoleDetails.get(0));
                                    eFmFmClientUserRoleProfileMap.seteFmFmClientBranchPO(eFmFmClientBranch);
                                    EFmFmClientBranchSubConfigurationPO eFmFmClientBranchSubConfigurationProfileMap = new EFmFmClientBranchSubConfigurationPO();
                                    eFmFmClientBranchSubConfigurationProfileMap.setClientBranchSubConfigurationId(73);
                                    eFmFmClientUserRoleProfileMap.seteFmFmClientBranchSubConfiguration(eFmFmClientBranchSubConfigurationProfileMap);
                                    iUserMasterBO.save(eFmFmClientUserRoleProfileMap);                                 
                                    
                                }
                            }
                        } else {
                            status = "emailAlreadyExist-"+empMasterDetails.get(9).toString();
                            issueList.put("RNo","");
							issueList.put("IssueStatus",status);
                            empMasterExcel.add(issueList);
                            return empMasterExcel;
                        }
                    } else {
                        status = "mobileNumAlreadyExist-"+empMasterDetails.get(8).toString();
                        issueList.put("RNo","");
						issueList.put("IssueStatus",status);
                        empMasterExcel.add(issueList);
                        return empMasterExcel;
                    }
                }else{
                	 status = "Employee Id Already Exist on Database -"+empMasterDetails.get(0).toString().trim();
                     issueList.put("RNo","");
						issueList.put("IssueStatus",status);
                     empMasterExcel.add(issueList);
                     return empMasterExcel;
                }
              
                /*
                 * Employee Project allocation Entry
                 */
            	if (!projectIdExist.isEmpty()) {	            	
	            	eFmFmEmployeeProjectDetailsPO.setCreatedBy(String.valueOf(profileId));            	
	            	eFmFmEmployeeProjectDetailsPO.setCreatedDate(new Date());            	
	            	eFmFmEmployeeProjectDetailsPO.setIsActive("Y");	            	
	            	Date projectAllocationStarDate = dateFormat.parse(empMasterDetails.get(18).toString());
	            	eFmFmEmployeeProjectDetailsPO.setProjectAllocationStarDate(projectAllocationStarDate);	 
	            	Date projectAllocationEndDate = dateFormat.parse(empMasterDetails.get(19).toString());
	            	eFmFmEmployeeProjectDetailsPO.setProjectAllocationEndDate(projectAllocationEndDate);
	            	eFmFmEmployeeProjectDetailsPO.seteFmFmClientProjectDetails(projectIdExist.get(0));
	            	eFmFmEmployeeProjectDetailsPO.setEfmFmUserMaster(employeeDetailsPO);
	            	iEmployeeDetailBO.addEmployeeProjectDetails(eFmFmEmployeeProjectDetailsPO);
            	}
            	
            	

            } else {
                status = "fail-";
            }
        } catch (Exception e) {
            status = "fail-";
            log.info("ImportEmploye Data Exception" + e);
			issueList.put("IssueStatus","Please check employee data,should not contain blank columns and  special symbols like ',@ etc.");
            empMasterExcel.add(issueList);
            return empMasterExcel;
        }
			}
		}catch (Exception e) {
	            status = "fail-";
				issueList.put("IssueStatus","Please check employee data,should not contain  blank columns and special symbols like ',@ etc.");
	            empMasterExcel.add(issueList);
	            log.info("ImportEmploye Data Exception" + e);
	            e.printStackTrace();
	            return empMasterExcel;
	    }
        return empMasterExcel;
    }

    /*
     * Function for creating a bulk request for Guest
     * 
     * @author Sarfraz Khan
     * 
     * @since 2015-05-05
     */
    private List<Map<String, Object>>  guestRecord(Map<Integer, Object> guestExcelRecord, int branchId,String combinedFacility)
            throws ParseException, InvalidKeyException, NoSuchAlgorithmException, IOException, URISyntaxException {
        IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");
        IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        List<Map<String, Object>> guestRecordExcel = new ArrayList<Map<String, Object>>();
	    Map<String, Object> issueList = new HashMap<String, Object>();
        log.info("importing data");
        String status = "success-";
        DateFormat shifTimeFormate = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try{							
			for (Entry<Integer, Object> entry : guestExcelRecord.entrySet()) {
					issueList = new HashMap<String, Object>();                
					ArrayList guestExcelDetails = (ArrayList) entry.getValue();	
					
        String shiftTimeInDateTimeFormate = guestExcelDetails.get(12).toString();
        String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
        java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());

        Date startDate = dateFormat.parse(guestExcelDetails.get(10).toString());
        Date endDate = dateFormat.parse(guestExcelDetails.get(11).toString());
        log.info("importing data for Guest");
       
        String backEndDateCheck = dateFormat.format(endDate) + " " + shifTimeSplit[1];
        Date backendDate = dateFormat.parse(backEndDateCheck);
        if (backendDate.getTime() < new Date().getTime()) {
            status = "backDateRequest-";
            log.info("importing data for Guest backDateRequest");
            issueList.put("RNo","");
			issueList.put("IssueStatus",status);
            guestRecordExcel.add(issueList);
            return guestRecordExcel;
        }

        List<EFmFmUserMasterPO> guestIdExistsCheck = iEmployeeDetailBO
                .getEmpDetailsFromEmployeeId(guestExcelDetails.get(0).toString().trim());
        if (!(guestIdExistsCheck.isEmpty())) {
            if (guestIdExistsCheck.get(0).getUserType().equalsIgnoreCase("employee")) {
                status = "alreadyRegisterAsEmp-";
                log.info("importing data for Guest alreadyRegisterAsEmp");
                issueList.put("RNo","");
				issueList.put("IssueStatus",status);
                guestRecordExcel.add(issueList);
                return guestRecordExcel;
            }
            List<EFmFmEmployeeRequestMasterPO> existRequestsInReqMaster = iCabRequestBO
                    .getEmplyeeRequestsForSameDateAndShiftTime(startDate, shiftTime, String.valueOf(branchId),
                            guestIdExistsCheck.get(0).getUserId(), guestExcelDetails.get(13).toString().trim().toUpperCase());
            if (!(existRequestsInReqMaster.isEmpty())) {
                status = "alreadyRaised-";
                log.info("importing data for Guest alreadyRaised");
                issueList.put("RNo","");
				issueList.put("IssueStatus",status);
                guestRecordExcel.add(issueList);
                return guestRecordExcel;
            }
            log.info("importing data for guest already register employeeId");
            // Emp detail From UserId
            EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO = new EFmFmClientProjectDetailsPO();
            guestIdExistsCheck.get(0).setEmployeeId(guestExcelDetails.get(0).toString().trim());

            guestIdExistsCheck.get(0).setUserName(guestExcelDetails.get(6).toString().trim());
            PasswordEncryption passwordEncryption = new PasswordEncryption();
            guestIdExistsCheck.get(0)
                    .setPassword(passwordEncryption.PasswordEncoderGenerator(guestExcelDetails.get(0).toString().trim()));
            
            guestIdExistsCheck.get(0).setHostMobileNumber(
                    Base64.getEncoder().encodeToString(guestExcelDetails.get(14).toString().trim().getBytes("utf-8")));
            
            guestIdExistsCheck.get(0).setFirstName(
                    Base64.getEncoder().encodeToString(guestExcelDetails.get(1).toString().trim().getBytes("utf-8")));
            
            guestIdExistsCheck.get(0).setGender(
                    Base64.getEncoder().encodeToString(guestExcelDetails.get(2).toString().trim().getBytes("utf-8")));
            guestIdExistsCheck.get(0).setMobileNumber(
                    Base64.getEncoder().encodeToString(guestExcelDetails.get(3).toString().trim().getBytes("utf-8")));
            guestIdExistsCheck.get(0).setEmailId(
                    Base64.getEncoder().encodeToString(guestExcelDetails.get(4).toString().trim().getBytes("utf-8")));
            guestIdExistsCheck.get(0).setAddress(
                    Base64.getEncoder().encodeToString(guestExcelDetails.get(5).toString().trim().getBytes("utf-8")));

            guestIdExistsCheck.get(0).setLastLoginTime(new Date());
            guestIdExistsCheck.get(0).setStatus("Y");
            guestIdExistsCheck.get(0).setUpdatedTime(new Date());
            EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
            eFmFmClientBranch.setBranchId(branchId);

            guestIdExistsCheck.get(0).seteFmFmClientBranchPO(eFmFmClientBranch);
            eFmFmClientProjectDetailsPO.setProjectId(1);
            guestIdExistsCheck.get(0).seteFmFmClientProjectDetails(eFmFmClientProjectDetailsPO);
            guestIdExistsCheck.get(0)
                    .setLatitudeLongitude(guestExcelDetails.get(9).toString().trim().replaceAll("\\s+", ""));
            // List<EFmFmClientBranchPO>
            // clientBranchDetails=userMasterBO.getClientDetails(branchId);
            EFmFmRouteAreaMappingPO routeAreaDetails = new EFmFmRouteAreaMappingPO();

             CalculateDistance empDistance=new CalculateDistance();
            try {
                 guestIdExistsCheck.get(0).setDistance(empDistance.employeeDistanceCalculation(guestIdExistsCheck.get(0).geteFmFmClientBranchPO().getLatitudeLongitude(),
                		 guestExcelDetails.get(9).toString().trim().replaceAll("\\s+", "")));
            } catch (Exception e) {
				log.info("Error"+e);
            }
            routeAreaDetails.setRouteAreaId(1);
            guestIdExistsCheck.get(0).seteFmFmRouteAreaMapping(routeAreaDetails);
            iUserMasterBO.update(guestIdExistsCheck.get(0));

            // Emp detail From EmployeeId
            List<EFmFmUserMasterPO> employeeDetailFromEmpId = iEmployeeDetailBO
                    .getParticularEmpDetailsFromEmployeeId(guestExcelDetails.get(0).toString().trim(),combinedFacility);

            EFmFmClientUserRolePO eFmFmClientUserRolePO = new EFmFmClientUserRolePO();
            EFmFmRoleMasterPO efmFmRoleMaster = new EFmFmRoleMasterPO();
            efmFmRoleMaster.setRoleId(4);
            eFmFmClientUserRolePO.setEfmFmUserMaster(employeeDetailFromEmpId.get(0));
            eFmFmClientUserRolePO.setEfmFmRoleMaster(efmFmRoleMaster);
            eFmFmClientUserRolePO.seteFmFmClientBranchPO(eFmFmClientBranch);
            EFmFmClientBranchSubConfigurationPO eFmFmClientBranchSubConfiguration = new EFmFmClientBranchSubConfigurationPO();
            eFmFmClientBranchSubConfiguration.setClientBranchSubConfigurationId(81);
            eFmFmClientUserRolePO.seteFmFmClientBranchSubConfiguration(eFmFmClientBranchSubConfiguration);
            iUserMasterBO.save(eFmFmClientUserRolePO);
            EFmFmUserMasterPO efmFmUserMaster = new EFmFmUserMasterPO();
            EFmFmEmployeeRequestMasterPO eFmFmEmployeeReqMasterPO = new EFmFmEmployeeRequestMasterPO();
            efmFmUserMaster.setUserId(employeeDetailFromEmpId.get(employeeDetailFromEmpId.size() - 1).getUserId());
            EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO = new EFmFmRouteAreaMappingPO();
            eFmFmRouteAreaMappingPO.setRouteAreaId(employeeDetailFromEmpId.get(employeeDetailFromEmpId.size() - 1)
                    .geteFmFmRouteAreaMapping().getRouteAreaId());
            eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
            eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);
            eFmFmEmployeeReqMasterPO.setStatus("Y");
            eFmFmEmployeeReqMasterPO.setRequestType("guest");
            eFmFmEmployeeReqMasterPO.setReadFlg("Y");
            eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
            eFmFmEmployeeReqMasterPO.setDropSequence(1);
            eFmFmEmployeeReqMasterPO.setRequestFrom("W");
            eFmFmEmployeeReqMasterPO.setRequestDate(new Date());
            eFmFmEmployeeReqMasterPO.setTripType(guestExcelDetails.get(13).toString().trim().toUpperCase());
            eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
            eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
            eFmFmEmployeeReqMasterPO.seteFmFmRouteAreaMapping(eFmFmRouteAreaMappingPO);
            iCabRequestBO.save(eFmFmEmployeeReqMasterPO);
            log.info("importing data for Guest emp success register if part");
            issueList.put("RNo","");
			issueList.put("IssueStatus",status);
            guestRecordExcel.add(issueList);
            return guestRecordExcel;
        } else {

            try {
                log.info("Geo codes" + guestExcelDetails.get(9).toString());
            } catch (Exception e) {
				log.info("Error"+e);
                status = "fail-";
                issueList.put("RNo","");
				issueList.put("IssueStatus",status);
                guestRecordExcel.add(issueList);
                return guestRecordExcel;
            }
            try {

                log.info("Geo codes2" + guestExcelDetails.get(9).toString().trim());
            } catch (Exception e) {
				log.info("Error"+e);
                status = "fail-";
                issueList.put("RNo","");
				issueList.put("IssueStatus",status);
                guestRecordExcel.add(issueList);
                return guestRecordExcel;

                // log.info("Geo codes2"+columnValue.get(26).toString().trim());
            }

            try {
                List<EFmFmClientRouteMappingPO> routeExistDetail = iRouteDetailBO
                        .getParticularRouteDetailByClient(combinedFacility, guestExcelDetails.get(7).toString().trim());
                if (routeExistDetail.isEmpty()) {
                    status = "routeNotExist-" + guestExcelDetails.get(7).toString().trim();
                    log.info("importing data for Guest routeNotExist");
                    issueList.put("RNo","");
					issueList.put("IssueStatus",status);
                    guestRecordExcel.add(issueList);
                    return guestRecordExcel;
                }
                EFmFmUserMasterPO employeeDetailsPO = new EFmFmUserMasterPO();
                EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO = new EFmFmClientProjectDetailsPO();
                employeeDetailsPO.setEmployeeId(guestExcelDetails.get(0).toString().trim());
                PasswordEncryption passwordEncryption = new PasswordEncryption();
                employeeDetailsPO
                        .setPassword(passwordEncryption.PasswordEncoderGenerator(guestExcelDetails.get(0).toString().trim()));
                employeeDetailsPO.setUserName(guestExcelDetails.get(6).toString().trim());
                employeeDetailsPO.setFirstName(
                        Base64.getEncoder().encodeToString(guestExcelDetails.get(1).toString().trim().getBytes("utf-8")));
                employeeDetailsPO.setGender(
                        Base64.getEncoder().encodeToString(guestExcelDetails.get(2).toString().trim().getBytes("utf-8")));
                employeeDetailsPO.setMobileNumber(
                        Base64.getEncoder().encodeToString(guestExcelDetails.get(3).toString().trim().getBytes("utf-8")));
                employeeDetailsPO.setEmailId(
                        Base64.getEncoder().encodeToString(guestExcelDetails.get(4).toString().trim().getBytes("utf-8")));
                employeeDetailsPO.setAddress(
                        Base64.getEncoder().encodeToString(guestExcelDetails.get(5).toString().trim().getBytes("utf-8")));
                employeeDetailsPO.setEmployeeDesignation(
                        Base64.getEncoder().encodeToString("Guest".getBytes("utf-8")));
                employeeDetailsPO.setPhysicallyChallenged(
                        Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));  
                employeeDetailsPO.setIsInjured(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
                employeeDetailsPO.setPragnentLady(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));

                employeeDetailsPO.setCreationTime(new Date());
                employeeDetailsPO.setUpdatedTime(new Date());
                eFmFmClientProjectDetailsPO.setClientProjectId("1");
                eFmFmClientProjectDetailsPO.setEmployeeProjectName("efmfm");
                eFmFmClientProjectDetailsPO
                        .setProjectAllocationStarDate(new Date());
                eFmFmClientProjectDetailsPO
                        .setProjectAllocationEndDate(new Date());
                employeeDetailsPO.setWeekOffDays(guestExcelDetails.get(8).toString());
                EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
                eFmFmClientBranch.setBranchId(branchId);
                employeeDetailsPO.seteFmFmClientBranchPO(eFmFmClientBranch);
                employeeDetailsPO.setPanicNumber(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
                employeeDetailsPO.setSecondaryPanicNumber(Base64.getEncoder().encodeToString("NO".getBytes("utf-8")));
                employeeDetailsPO.setLastLoginTime(new Date());
                employeeDetailsPO.setStatus("Y");
				employeeDetailsPO.setPasswordChangeDate(new Date());
                employeeDetailsPO.setLocationStatus("N");
                employeeDetailsPO.setUserType("guest");
                employeeDetailsPO.setHostMobileNumber(
                        Base64.getEncoder().encodeToString(guestExcelDetails.get(14).toString().trim().getBytes("utf-8")));
                employeeDetailsPO.setLatitudeLongitude(guestExcelDetails.get(9).toString().trim().replaceAll("\\s+", ""));
                employeeDetailsPO.setDeviceId("NO");
                employeeDetailsPO.setEmployeeProfilePic("NO");
                employeeDetailsPO.setWrongPassAttempt(0);
                employeeDetailsPO.setTempPassWordChange(false);
                /*
                 * Generating latitute & longitute based on the Address
                 */
                eFmFmClientProjectDetailsPO.seteFmFmClientBranchPO(eFmFmClientBranch);

                /*
                 * Project Id Already Exit
                 * 
                 */
                List<EFmFmClientProjectDetailsPO> projectIdExist = iEmployeeDetailBO
                        .getProjectDetails(eFmFmClientProjectDetailsPO.getClientProjectId().toUpperCase(), combinedFacility);
                if (projectIdExist.isEmpty()) {
                    iEmployeeDetailBO.save(eFmFmClientProjectDetailsPO);
                    List<EFmFmClientProjectDetailsPO> projectDetails = iEmployeeDetailBO.getProjectDetails(
                            eFmFmClientProjectDetailsPO.getClientProjectId().toUpperCase(), combinedFacility);
                    if (!(projectDetails.isEmpty())) {
                        // eFmFmClientProjectDetailsPO.setProjectId(projectDetails.get(0).getProjectId());
                        employeeDetailsPO.seteFmFmClientBranchPO(eFmFmClientBranch);
                        employeeDetailsPO.seteFmFmClientProjectDetails(projectDetails.get(0));
                    }
                } else {
                    // eFmFmClientProjectDetailsPO.setProjectId(projectIdExist.get(0).getProjectId());
                    employeeDetailsPO.seteFmFmClientBranchPO(eFmFmClientBranch);
                    employeeDetailsPO.seteFmFmClientProjectDetails(projectIdExist.get(0));
                }
               log.info("routeId"+routeExistDetail.get(0).geteFmFmZoneMaster().getZoneId());
 
               List<EFmFmRouteAreaMappingPO> routeAreaId = iRouteDetailBO
                        .getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(1, combinedFacility,
                                routeExistDetail.get(0).geteFmFmZoneMaster().getZoneId(),
                                1);
                
                if (routeAreaId.isEmpty()) {
                    EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
                    EFmFmAreaMasterPO efmFmAreaMaster=new EFmFmAreaMasterPO();
                    efmFmAreaMaster.setAreaId(1);
                    routeAreaMapping.setEfmFmAreaMaster(efmFmAreaMaster);
                    routeAreaMapping.seteFmFmZoneMaster(routeExistDetail.get(0).geteFmFmZoneMaster());
                    EFmFmAreaNodalMasterPO eFmFmNodalAreaMaster=new EFmFmAreaNodalMasterPO();
                    eFmFmNodalAreaMaster.setNodalPointId(1);
                    routeAreaMapping.seteFmFmNodalAreaMaster(eFmFmNodalAreaMaster);
                    iRouteDetailBO.save(routeAreaMapping);
                    routeAreaId = iRouteDetailBO
                            .getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(1, combinedFacility,
                                    routeExistDetail.get(0).geteFmFmZoneMaster().getZoneId(),
                                    1);
                }

               
                log.info("routeAreaId" + routeAreaId);
                employeeDetailsPO.seteFmFmRouteAreaMapping(routeAreaId.get(0));
                /*
                 * employee Record already existing on table.
                 */
                // String userRole=columnValue.get(21).toString().toUpperCase();
                String userRole = "webuser";
                if (!employeeDetailsPO.getEmployeeId().isEmpty() && employeeDetailsPO.getEmployeeId() != null
                        && employeeDetailsPO.getEmployeeId() != "" && employeeDetailsPO.getEmailId() != ""
                        && !employeeDetailsPO.getEmailId().isEmpty() && employeeDetailsPO.getMobileNumber() != ""
                        && !employeeDetailsPO.getMobileNumber().isEmpty()) {
                    List<EFmFmUserMasterPO> employeeIdExist = iEmployeeDetailBO
                            .getParticularEmpDetailsFromEmployeeId(employeeDetailsPO.getEmployeeId(),combinedFacility);
                    if (employeeIdExist.isEmpty()) {
                        if (userRole.equalsIgnoreCase("manager")) {
                            List<EFmFmUserMasterPO> roleExist = iUserMasterBO.getUsersRoleExist(combinedFacility,
                                    employeeDetailsPO.geteFmFmClientProjectDetails().getClientProjectId(),
                                    userRole.trim());
                            if (!(roleExist.isEmpty())) {
                                userRole = "webuser";
                                iEmployeeDetailBO.save(employeeDetailsPO);
                            } else {
                                iEmployeeDetailBO.save(employeeDetailsPO);
                            }
                        } else {
                            iEmployeeDetailBO.save(employeeDetailsPO);
                        }
                        List<EFmFmUserMasterPO> employeeIdDetails = iEmployeeDetailBO
                                .getParticularEmpDetailsFromEmployeeId(employeeDetailsPO.getEmployeeId(),combinedFacility);
                        if (!(employeeIdDetails.isEmpty())) {
                            List<EFmFmRoleMasterPO> getRoleDetails = iUserMasterBO.getRoleId(userRole);
                            if (!(getRoleDetails.isEmpty())) {
                                EFmFmClientUserRolePO eFmFmClientUserRolePO = new EFmFmClientUserRolePO();
                                eFmFmClientUserRolePO.setEfmFmUserMaster(employeeIdDetails.get(0));
                                eFmFmClientUserRolePO.setEfmFmRoleMaster(getRoleDetails.get(0));
                                eFmFmClientUserRolePO.seteFmFmClientBranchPO(eFmFmClientBranch);
                                EFmFmClientBranchSubConfigurationPO eFmFmClientBranchSubConfiguration = new EFmFmClientBranchSubConfigurationPO();
                                eFmFmClientBranchSubConfiguration.setClientBranchSubConfigurationId(81);
                                eFmFmClientUserRolePO
                                        .seteFmFmClientBranchSubConfiguration(eFmFmClientBranchSubConfiguration);
                                iUserMasterBO.save(eFmFmClientUserRolePO);
                            }
                        }
                    }
                       }
                EFmFmUserMasterPO efmFmUserMaster = new EFmFmUserMasterPO();
                List<EFmFmUserMasterPO> employeeIdData = iEmployeeDetailBO
                        .getParticularEmpDetailsFromEmployeeId(employeeDetailsPO.getEmployeeId(),combinedFacility);
                EFmFmEmployeeRequestMasterPO eFmFmEmployeeReqMasterPO = new EFmFmEmployeeRequestMasterPO();
                efmFmUserMaster.setUserId(employeeIdData.get(0).getUserId());
                EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO = new EFmFmRouteAreaMappingPO();
                eFmFmRouteAreaMappingPO
                        .setRouteAreaId(employeeIdData.get(0).geteFmFmRouteAreaMapping().getRouteAreaId());
                eFmFmEmployeeReqMasterPO.setShiftTime(shiftTime);
                eFmFmEmployeeReqMasterPO.setEfmFmUserMaster(efmFmUserMaster);
                eFmFmEmployeeReqMasterPO.setStatus("Y");
                eFmFmEmployeeReqMasterPO.setRequestType("guest");
                eFmFmEmployeeReqMasterPO.setReadFlg("Y");
                eFmFmEmployeeReqMasterPO.setPickUpTime(shiftTime);
                eFmFmEmployeeReqMasterPO.setDropSequence(1);
                eFmFmEmployeeReqMasterPO.setRequestFrom("W");
                eFmFmEmployeeReqMasterPO.setRequestDate(new Date());
                eFmFmEmployeeReqMasterPO.setTripType(guestExcelDetails.get(13).toString().trim().toUpperCase());
                eFmFmEmployeeReqMasterPO.setTripRequestStartDate(startDate);
                eFmFmEmployeeReqMasterPO.setTripRequestEndDate(endDate);
                eFmFmEmployeeReqMasterPO.seteFmFmRouteAreaMapping(eFmFmRouteAreaMappingPO);
                iCabRequestBO.save(eFmFmEmployeeReqMasterPO);
                log.info("importing data for Guest else success");

            } catch (Exception e) {
                log.info("ImportEmploye Data Exception" + e);
                issueList.put("RNo","");
				issueList.put("IssueStatus",e);
                guestRecordExcel.add(issueList);
                return guestRecordExcel;
            }
          
        }
        }           
        }catch (Exception e) {
		            //status = "fail-";
		            log.info("ImportEmploye Data Exception" + e);
		            issueList.put("RNo","");
					issueList.put("IssueStatus",e);
		            guestRecordExcel.add(issueList);
		            return guestRecordExcel;
		}  
        
        
	    return guestRecordExcel;
	  }

    /*
     * Calculating working day's without Saturday & Sunday.
     * 
     * @return the end Date.
     * 
     * @author Rajan R
     * 
     * @since 2015-05-12
     */
    public static Date getWorkingDays(Date startDate, int dayCount) {
        Calendar startCal;
        startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        int noOfDays = 2;
        do {
            int dayOfWeek = startCal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.FRIDAY) {
                startCal.add(Calendar.DATE, 2);
            } else {
                startCal.add(Calendar.DATE, 1);
                noOfDays = noOfDays + 1;
            }
        } while (dayCount >= noOfDays);

        return startCal.getTime();
    }

   /* public long getDisableTime(int hours, int minutes, Date checkIndate) {
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
      public long getBackDateTime(int hours, int minutes, Date checkIndate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkIndate);
        calendar.add(Calendar.HOUR, -hours);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTimeInMillis();
    }
      public static byte[] getBytes(InputStream is) throws IOException {
  	    int len;
  	    int size = 1024;
  	    byte[] buf;

  	    if (is instanceof ByteArrayInputStream) {
  	      size = is.available();
  	      System.out.println("fun"+size);
  	      buf = new byte[size];
  	      len = is.read(buf, 0, size);
  	    } else {
  	      ByteArrayOutputStream bos = new ByteArrayOutputStream();
  	      buf = new byte[size];
  	      while ((len = is.read(buf, 0, size)) != -1)
  	        bos.write(buf, 0, len);
  	      buf = bos.toByteArray();
  	    }
  	    return buf;
  	  }

}