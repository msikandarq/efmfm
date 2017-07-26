package com.newtglobal.eFmFmFleet.services;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IFacilityBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Validator;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;


@Component
@Path("/escort")
@Consumes("application/json")
@Produces("application/json")
public class EscortService {	
	
    private static final String SERVER_UPLOAD_LINUX_LOCATION_FOLDER  = ContextLoader.getContext().getMessage("upload.docsLinux", null, "docsLinux", null);
    private static final String SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER  = ContextLoader.getContext().getMessage("upload.docsWindows", null, "docsWindows", null);

     private static Log log = LogFactory.getLog(EscortService.class);	
    
     
	 @Context
	 private HttpServletRequest httpRequest;
	 JwtTokenGenerator token=new JwtTokenGenerator();


	
	/*
	 * @Reading Escort details from escort Master xl utility.
	 * @Stored all the values on Arraylist.
	 * @author  Rajan R
	 * @since   2015-05-28 
	 */
	@POST
	@Path("/escortDetails")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	  public Response upload(@FormDataParam("filename") InputStream uploadedInputStream,
				@FormDataParam("filename") InputStream uploadedSizeInputStream,
	            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
	            @QueryParam("combinedFacility") String combinedFacility,
	            @Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException {
	   	 String status = "success-";
	   	log.info("serviceStart -UserId :" +userId);
	   	 int noOfcolumn=18; 
	       List<Map<String, Object>> response =new ArrayList<Map<String, Object>>();
	       	Map<String, Object>  escortReqExcel= new HashMap<String, Object>(); 
	       	Map<Integer, Object>  escortExcelDetails= new HashMap<Integer, Object>();
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
				escortReqExcel.put("IssueStatus",status);
          		response.add(escortReqExcel);
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
	   	        }
	           
	           int bytes=getBytes(uploadedSizeInputStream).length;
		   		double kilobytes = (bytes / 1000);
		   		double megabytes = (kilobytes / 1000);
		           if(megabytes > 5){
		           	log.info("Inside Excel bigSize");
					status="Kinldy check the file Size,System will except a excel only maximum size 5MB.";  	                    		
					escortReqExcel.put("IssueStatus",status);
	          		response.add(escortReqExcel);
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
	                		 if(columnValue.size()>=18){
				                if(!escortMasterExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,userId,combinedFacility).isEmpty()){                		
				                		response.addAll(escortMasterExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,userId,combinedFacility));
				                	}else{
				                		escortExcelDetails.put(rowId, columnValue);
				                }
	                		 }else{
	                			 status="Kinldy fill the all the Column's -18 : Entrered column :"+columnValue.size();            			 		                			 	
	                			 	escortReqExcel.put("RNo",Integer.toString(rowId));
	                			 	escortReqExcel.put("IssueStatus",status);	                			 	
	  	                    		response.add(escortReqExcel);    			 
	                		 }
	                	}else{
	                		  if(columnValue.size()<18){      			       			
	                				status="Kinldy fill the all the Column's";
	                				escortReqExcel.put("RNo",Integer.toString(rowId));
	                			 	escortReqExcel.put("IssueStatus",status);
	  	                    		response.add(escortReqExcel);	  	                    	
	                		}else{
	                			//Need to configure below Text some where
	                			if(!"EscortID".equalsIgnoreCase(columnValue.get(0).toString().replace(" ","").trim()) 
	                					|| !"EscortFirstName".equalsIgnoreCase(columnValue.get(1).toString().replace(" ","").trim()) 
	                					|| !"EscortMiddleName".equalsIgnoreCase(columnValue.get(2).toString().replace(" ","").trim()) 
	                					|| !"EscortLastName".equalsIgnoreCase(columnValue.get(3).toString().replace(" ","").trim())
	                					|| !"FatherName".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim())
	                					|| !"Gender".equalsIgnoreCase(columnValue.get(5).toString().replace(" ","").trim())
	                					|| !"MobileNumber".equalsIgnoreCase(columnValue.get(6).toString().replace(" ","").trim()) 
	                        			|| !"EmailID".equalsIgnoreCase(columnValue.get(7).toString().replace(" ","").trim()) 
	                        			|| !"State".equalsIgnoreCase(columnValue.get(8).toString().replace(" ","").trim()) 
	                        			|| !"CityName".equalsIgnoreCase(columnValue.get(9).toString().replace(" ","").trim()) 
	                        			|| !"Address".equalsIgnoreCase(columnValue.get(10).toString().replace(" ","").trim())	                        			
	                        			|| !"PinCode".equalsIgnoreCase(columnValue.get(11).toString().replace(" ","").trim()) 
	                					|| !"PoliceVerification".equalsIgnoreCase(columnValue.get(12).toString().replace(" ","").trim())
	                					|| !"Designation".equalsIgnoreCase(columnValue.get(13).toString().replace(" ","").trim())
	                					|| !"DateOfBirth".equalsIgnoreCase(columnValue.get(14).toString().replace(" ","").trim()) 
	                        			|| !"PhysicallyChallenged".equalsIgnoreCase(columnValue.get(15).toString().replace(" ","").trim()) 
	                        			|| !"VendorName".equalsIgnoreCase(columnValue.get(16).toString().replace(" ","").trim())
	                        			|| !"PermanentAddress".equalsIgnoreCase(columnValue.get(17).toString().replace(" ","").trim())){
	                					escortReqExcel.put(Integer.toString(rowId),"Kinldy check the Column Name & No of Column's ");
		  	                    		response.add(escortReqExcel);
		  	                    		log.info("serviceEnd -UserId :" +userId);
		  	                    		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	                			}
	                		}
	                	
	                	}
					}              
	            }
	            log.info("Size"+escortExcelDetails.size());
				if(response.isEmpty()){
					if(!escortExcelDetails.isEmpty()){
						response=addEscortRecord(escortExcelDetails,branchId,userId,combinedFacility);	
					}
				}

	        } catch (Exception e) {
	        	log.info(" Error :" +e);
      		try{
            	if(e.getCause().toString().contains("InvalidFormatException")){
    				status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
    				escortReqExcel.put("IssueStatus",status);
              		response.add(escortReqExcel);
              		log.info(e+" serviceEnd -UserId :" +userId);
                    return Response.ok(response, MediaType.APPLICATION_JSON).build();
            	}
          		}catch(Exception e1){
              		log.info(" Error :" +e1);
          		}
    			status="Kinldy check the excel for any empty column,System will except only excel format without formula and special symbols.";  	                    		
    			escortReqExcel.put("IssueStatus",status);
          		response.add(escortReqExcel);
          		log.info(e+" serviceEnd -UserId :" +userId);
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
                
}
	       log.info("serviceEnd -UserId :" +userId);
	        return Response.ok(response, MediaType.APPLICATION_JSON).build();
	    }
	    
	    
	    
	    private  List<Map<String, Object>> escortMasterExcelValidator(String rowId,ArrayList<Object> columnValue,int branchId,int noOfcolumn,int userId,String combinedFacility){
	    	
	    	IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");				   
		    IVendorDetailsBO iVendorMasterBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");		    
		    List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();
		    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		    String validInt="^[0-9]*$";
		    String alphanumaric="^[a-zA-Z0-9]*$";
		    String charAt="^[a-zA-Z]*$";
			
		    if (columnValue.get(0).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(0).toString() ==null || columnValue.get(0).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(0).toString().isEmpty() || !columnValue.get(0).toString().replace(" ", "").trim().matches(alphanumaric)) {
				String status="kindly check the EscortID : "+columnValue.get(0).toString();	
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",(rowId.concat(",0")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				List<EFmFmEscortMasterPO> escortIdExist=iVehicleCheckInBO.getEscortDetailsByEmpId(columnValue.get(0).toString().trim(),combinedFacility);			 	
				if(!escortIdExist.isEmpty()){
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			    	String status = "Escort Employee Id already available -" + columnValue.get(0).toString().trim();			    	
			    	empReqExcelRows.put("RNo",(rowId.concat(",0")));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
			    } 
			}
			
		    if (columnValue.get(1).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(1).toString() ==null || columnValue.get(1).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(1).toString().isEmpty() || !columnValue.get(1).toString().replace(" ", "").trim().matches(charAt)) {						
				String status="kindly check the Escort Name ,special characters are not allowed : "+columnValue.get(1).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",(rowId.concat(",1")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			/*if (columnValue.get(2).toString() !=null || !columnValue.get(2).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| !columnValue.get(2).toString().isEmpty()) {	
				
				if(!columnValue.get(2).toString().replace(" ","").trim().matches(charAt)){
					String status="kindly check the Escort middle Name ,special characters are not allowed : "+columnValue.get(2).toString();
					empReqExcelRows.put(rowId.concat(",2"), status);
				}
				
			}
			
			if (columnValue.get(3).toString() !=null || !columnValue.get(3).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| !columnValue.get(3).toString().isEmpty()) {	
				if(!columnValue.get(3).toString().replace(" ", "").trim().matches(charAt)){
				String status="kindly check the Escort Last Name ,special characters are not allowed : "+columnValue.get(3).toString();
				empReqExcelRows.put(rowId.concat(",3"), status);
				}
			}*/
			
			if (columnValue.get(4).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(4).toString() ==null || columnValue.get(4).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(4).toString().isEmpty() || !columnValue.get(4).toString().replace(" ", "").trim().matches(charAt)) {	
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Father Name : "+columnValue.get(4).toString();				
				empReqExcelRows.put("RNo",(rowId.concat(",4")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			if (columnValue.get(5).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(5).toString() ==null || columnValue.get(5).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(5).toString().isEmpty() || !columnValue.get(5).toString().replace(" ", "").trim().matches(charAt)) {	
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Escort sex : "+columnValue.get(5).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",5")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else if (!"FEMALE".equalsIgnoreCase(columnValue.get(5).toString()) && !"MALE".equalsIgnoreCase(columnValue.get(5).toString())){
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String	status="kindly check the Escort sex : "+columnValue.get(5).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",5")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
			if (columnValue.get(6).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(6).toString() ==null || columnValue.get(6).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(6).toString().isEmpty() || !columnValue.get(6).toString().replace(" ", "").trim().matches(validInt)) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Mobile Number and Mobile Should be Integer : "+columnValue.get(6).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",6")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else if(columnValue.get(6).toString().length()<=18 && columnValue.get(6).toString().length()>5){
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				EFmFmEscortMasterPO escortMasterPO=new EFmFmEscortMasterPO();
				escortMasterPO.setMobileNumber(columnValue.get(6).toString());
				escortMasterPO.setCombinedFacility(combinedFacility);
		    	List<EFmFmEscortMasterPO> escortMobileNoExist=iVehicleCheckInBO.getMobileNoDetails(escortMasterPO);
		    		if(!escortMobileNoExist.isEmpty()){
						 String status="Mobile Number already available on database  : "+columnValue.get(6).toString();
						 empReqExcelRows.put("RNo",(rowId.concat(",6")));
							empReqExcelRows.put("IssueStatus",status);
							childRowResponse.add(empReqExcelRows);
					 }
				
				
			}else{
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				 String status="kindly check the Mobile Number length(ex:India -12 digit),length should be country Code with MobileNumber  : "+columnValue.get(6).toString();
				 empReqExcelRows.put("RNo",(rowId.concat(",6")));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
			}
			if (columnValue.get(7).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(7).toString() ==null || columnValue.get(7).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(7).toString().isEmpty()) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Email Id : "+columnValue.get(7).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",7")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else if(!columnValue.get(7).toString().equalsIgnoreCase("NA") ){	
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
							String emailIdRegex = "^[A-Za-z0-9+_.-]+@(.+)$";						 
					   		Pattern pattern = Pattern.compile(emailIdRegex);
					   		Matcher matcher = pattern.matcher(columnValue.get(7).toString());				   
						    if(matcher.matches()){
						    	//validate
						    	
						    	/*List<EFmFmUserMasterPO> employeeEmailId = iEmployeeDetailBO.getParticularEmployeeDetailsFromEmailId(columnValue.get(7).toString().trim(), branchId);
							      if(!employeeEmailId.isEmpty()){
										 String status="Email Id already available on database  : "+columnValue.get(7).toString();
											empReqExcelRows.put(rowId.concat(",7"), status);
									 }*/ 
						    }else{
						    	String status="kindly check the Email Id : "+columnValue.get(7).toString();
						    	empReqExcelRows.put("RNo",(rowId.concat(",7")));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
						    }			  				
			}	
			
				
			if (columnValue.get(8).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(8).toString() ==null || columnValue.get(8).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(8).toString().isEmpty() || !columnValue.get(8).toString().replace(" ", "").trim().matches(charAt) ) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the State Name : "+columnValue.get(8).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",8")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
			if (columnValue.get(9).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(9).toString() ==null || columnValue.get(9).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(9).toString().isEmpty() || !columnValue.get(9).toString().replace(" ", "").trim().matches(charAt) ) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the City Name : "+columnValue.get(9).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",9")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			if (columnValue.get(10).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(10).toString() ==null || columnValue.get(10).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(10).toString().isEmpty() ) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Current Address : "+columnValue.get(10).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",10")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
			if (columnValue.get(11).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(11).toString() ==null || columnValue.get(11).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(11).toString().isEmpty() || !columnValue.get(11).toString().replace(" ", "").trim().matches(validInt)) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Pincode : "+columnValue.get(11).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",11")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}			
			
			if (columnValue.get(12).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(12).toString() ==null || columnValue.get(12).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(12).toString().isEmpty() || !columnValue.get(12).toString().replace(" ", "").trim().matches(charAt) ) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Police Verification : "+columnValue.get(12).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",12")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			if (columnValue.get(13).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(13).toString() ==null || columnValue.get(13).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(13).toString().isEmpty() || !columnValue.get(13).toString().replace(" ", "").trim().matches(charAt)) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Employee Designation : "+columnValue.get(13).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",13")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}				
			
			if (columnValue.get(14).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(14).toString() ==null || columnValue.get(14).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(14).toString().isEmpty() ) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Escort Date Of Birth  : "+columnValue.get(14).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",14")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				try{
					Date escortDOB=dateFormat.parse(columnValue.get(14).toString());
					if(escortDOB.after(new Date())){						
						String status="Escort DOB should not greater than Date today Date : "+columnValue.get(14).toString();
						empReqExcelRows.put("RNo",(rowId.concat(",14")));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
					}	
					
				}catch(Exception e){
					String status="kindly check the Escort DOB   : "+e;
					empReqExcelRows.put("RNo",(rowId.concat(",14")));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}
			}
						
			if (columnValue.get(15).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(15).toString() ==null || columnValue.get(15).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(15).toString().isEmpty() || !columnValue.get(15).toString().replace(" ", "").trim().matches(charAt)) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Physically Challenged : "+columnValue.get(15).toString();
				empReqExcelRows.put(rowId.concat(",15"), status);
			}else if (!"Y".equalsIgnoreCase(columnValue.get(15).toString()) && !"N".equalsIgnoreCase(columnValue.get(15).toString())){
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String	status="kindly check the Physically Challenged : "+columnValue.get(15).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",15")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			if (columnValue.get(16).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(16).toString() ==null || columnValue.get(16).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(16).toString().isEmpty()) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();					
				String status="kindly check the Vendor Name : "+columnValue.get(16).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",16")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				List<EFmFmVendorMasterPO> venderNameExist = iVendorMasterBO.getAllVendorName(columnValue.get(16).toString().trim(),combinedFacility);
				if (venderNameExist.isEmpty()) {
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			    	String status = " Vendor Name not available -" + columnValue.get(16).toString().trim();
			    	empReqExcelRows.put("RNo",(rowId.concat(",16")));
					empReqExcelRows.put("IssueStatus",status);	
					 childRowResponse.add(empReqExcelRows);
			    } 
				
			}			

			if (columnValue.get(17).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(17).toString() ==null || columnValue.get(17).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(17).toString().isEmpty() ) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Permanent Address : "+columnValue.get(17).toString();
				empReqExcelRows.put("RNo",(rowId.concat(",17")));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			return childRowResponse;	
}
	
	
	/*
	 * @xl utility row values are inserted into escort master table. 
	 * validation also be handle here.	 * 
	 * @author  Rajan R
	 * @since   2015-05-12 
	 */
	private List<Map<String, Object>> addEscortRecord(Map<Integer, Object> escortRecordDetails,int branchId,int userId,String combinedFacility) throws ParseException {	
		IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");	
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
	    IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
		 List<Map<String, Object>> escortMasterExcel = new ArrayList<Map<String, Object>>();
		 Map<String, Object> issueList = new HashMap<String, Object>();
		 String status="";
		try {			
			for (Entry<Integer, Object> entry : escortRecordDetails.entrySet()) {
				issueList = new HashMap<String, Object>();                
				ArrayList escortExcelDetails = (ArrayList) entry.getValue();			
			 if(escortExcelDetails.get(16).toString().toUpperCase()!="" && !escortExcelDetails.get(16).toString().toUpperCase().isEmpty()){
			 	EFmFmEscortMasterPO escortMasterPO = new EFmFmEscortMasterPO();	
			 	escortMasterPO.setEscortEmployeeId(escortExcelDetails.get(0).toString());
			 	escortMasterPO.setFirstName(escortExcelDetails.get(1).toString());
			 	if(escortExcelDetails.get(2).toString().equalsIgnoreCase("CELL_TYPE_BLANK"))             	
			 		escortMasterPO.setMiddleName("");
			 	else
        			escortMasterPO.setMiddleName(escortExcelDetails.get(2).toString());    	
			 	if(escortExcelDetails.get(3).toString().equalsIgnoreCase("CELL_TYPE_BLANK"))	 		
    			escortMasterPO.setLastName("");	
    			else        			
    			escortMasterPO.setLastName(escortExcelDetails.get(3).toString());        		
			 	escortMasterPO.setFatherName(escortExcelDetails.get(4).toString());
			 	escortMasterPO.setGender(escortExcelDetails.get(5).toString());
			 	escortMasterPO.setMobileNumber(escortExcelDetails.get(6).toString());
			 	escortMasterPO.setEmailId(escortExcelDetails.get(7).toString());			 	
			 	escortMasterPO.setStateName(escortExcelDetails.get(8).toString());			 	
			 	escortMasterPO.setCityName(escortExcelDetails.get(9).toString());
			 	escortMasterPO.setAddress(escortExcelDetails.get(10).toString());			 	
			 	escortMasterPO.setPincode(Integer.parseInt(escortExcelDetails.get(11).toString()));			 	
			 	escortMasterPO.setPoliceVerification(escortExcelDetails.get(12).toString());
			 	escortMasterPO.setDesignation(escortExcelDetails.get(13).toString());
			 	escortMasterPO.setDateOfBirth(dateFormat.parse(escortExcelDetails.get(14).toString()));		 	
			 	escortMasterPO.setPhysicallyChallenged(escortExcelDetails.get(15).toString());
			 	escortMasterPO.setPermanentAddress(escortExcelDetails.get(17).toString());
			 	escortMasterPO.setIsActive("Y");
			 	escortMasterPO.setUserName(escortExcelDetails.get(0).toString());			 				
			 	escortMasterPO.setWrongPassAttempt(0);
			 	escortMasterPO.setTempPassWordChange(false);
			 	escortMasterPO.setLoggedIn(false);		
			 	escortMasterPO.setImeiNumber("NO");			 
			 	escortMasterPO.setPoliceVerification("NO");		 	
			 	List<EFmFmVendorMasterPO> vendorDetails=iVendorDetailsBO.getAllVendorName(escortExcelDetails.get(16).toString().toUpperCase(),combinedFacility);
			 	if(!(vendorDetails.isEmpty())){	
			 			escortMasterPO.setEfmFmVendorMaster(vendorDetails.get(0));
			 	}
			 	
				/* 
				 * Fetching AreaId from efmfmareamaster table.
				 
				EFmFmAreaMasterPO areaDetails = iRouteDetailBO.getAreaId(columnValue.get(5).toString().trim());
				if (areaDetails !=null) {					
					  escortMasterPO.setEfmFmAreaMaster(areaDetails);
				}*/
			 	
				/*
				 * escort Record already existing on table.
				 */
			 	if((!escortMasterPO.getEscortEmployeeId().isEmpty() && escortMasterPO.getEscortEmployeeId() !="" && escortMasterPO.getEscortEmployeeId() !=null) &&
			 			(escortMasterPO.getMobileNumber() !="" && !escortMasterPO.getMobileNumber().isEmpty() && escortMasterPO.getMobileNumber()!="") && vendorDetails.size() >0){
					List<EFmFmEscortMasterPO> escortIdExist=iVehicleCheckInBO.getEscortDetailsByEmpId(escortMasterPO.getEscortEmployeeId(),combinedFacility);			 	
					if(escortIdExist.isEmpty()){
						escortMasterPO.setCombinedFacility(combinedFacility);
						List<EFmFmEscortMasterPO> escortMobileNoExist=iVehicleCheckInBO.getMobileNoDetails(escortMasterPO);										 	
						if(escortMobileNoExist.isEmpty()){
							iVehicleCheckInBO.saveEscortDetails(escortMasterPO);
						}else{
							 status="MobileNumber already available on database"+escortMasterPO.getMobileNumber();
							 issueList.put("RNo","");
							 issueList.put("IssueStatus",status);
							 escortMasterExcel.add(issueList);
							 return escortMasterExcel;							
						 }
					 }else{
						 status="escortId already available on database"+escortMasterPO.getMobileNumber();
						 issueList.put("RNo","");
						 issueList.put("IssueStatus",status);
						 escortMasterExcel.add(issueList);
						 return escortMasterExcel;
					 }
				}
			 }
         }
		}
         catch(Exception e){
        	 log.info("Error"+e);
         }
		
		return escortMasterExcel;		
		
	}
	
	
	
	
	
	
	/**
	* The listOfEscort method implemented.
	* for getting the list of Escort details based on the escort.   
	*
	* @author  Rajan R
	* 
	* @since   2015-05-25 
	* 
	* @return escort details.
	*/
	
	
	@POST
	@Path("/listOfEscort")
	public Response listOfEscort(EFmFmVendorMasterPO eFmFmVendorMasterPO){		
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");			
		List<Map<String, Object>> listOfEscortDetails= new ArrayList<Map<String, Object>>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){

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
		
		}catch(Exception e){
				log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		
		
		
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		List<EFmFmEscortMasterPO> listOfEscort=iVehicleCheckInBO.getAllEscortDetails(eFmFmVendorMasterPO.getCombinedFacility());
		
		
		if(!(listOfEscort.isEmpty())){				
			for(EFmFmEscortMasterPO escortDetails:listOfEscort){				
					Map<String, Object>  escortList= new HashMap<String, Object>();					
					escortList.put("escortId",escortDetails.getEscortId());
					escortList.put("escortName",escortDetails.getFirstName());
					escortList.put("escortAddress",escortDetails.getAddress());
					escortList.put("permanentAddress",escortDetails.getPermanentAddress());
					escortList.put("escortMobileNumber",escortDetails.getMobileNumber());						
					escortList.put("escortFatherName",escortDetails.getFatherName());
					escortList.put("escortGender",escortDetails.getGender());
					escortList.put("escortBatchNum",escortDetails.getEscortEmployeeId());
					escortList.put("escortVendorName",escortDetails.getEfmFmVendorMaster().getVendorName());
					escortList.put("escortPincode",escortDetails.getPincode());
					escortList.put("remarks",escortDetails.getRemarks());
					escortList.put("facilityName",escortDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
					try{
					escortList.put("escortdob",dateFormat.format(escortDetails.getDateOfBirth()));
					}
					catch(Exception e){
						escortList.put("escortdob","");
						log.info("Error"+e);
					}
					listOfEscortDetails.add(escortList);			
				}			
			}
		 log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(listOfEscortDetails, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	* The escortDetails method implemented.
	* for getting the escort details based on the escortId.   
	*
	* @author  Rajan R
	* 
	* @since   2015-05-25 
	* 
	* @return escort details.
	*/	
	@POST
	@Path("/escortallDetails")
	public Response escortDetails(EFmFmEscortMasterPO eFmFmEscortMasterPO){		
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");				
		List<Map<String, Object>> listOfEscortDetails= new ArrayList<Map<String, Object>>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEscortMasterPO.getUserId()))){
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmEscortMasterPO.getUserId());
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
		log.info("serviceStart -UserId :" + eFmFmEscortMasterPO.getUserId());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		List<EFmFmEscortMasterPO> listOfEscort=iVehicleCheckInBO.getParticularEscortDetails(eFmFmEscortMasterPO.getEscortId());		 
			if(!(listOfEscort.isEmpty())){
					Map<String, Object>  escortList= new HashMap<String, Object>();					
					escortList.put("escortId",listOfEscort.get(0).getEscortId());
					escortList.put("escortName",listOfEscort.get(0).getFirstName());
					escortList.put("escortAddress",listOfEscort.get(0).getAddress());
					escortList.put("permanentAddress",listOfEscort.get(0).getPermanentAddress());
					escortList.put("escortMobileNumber",listOfEscort.get(0).getMobileNumber());
					escortList.put("escortFatherName",listOfEscort.get(0).getFatherName());
					escortList.put("escortGender",listOfEscort.get(0).getGender());
					escortList.put("escortPincode",listOfEscort.get(0).getPincode());
					escortList.put("remarks",listOfEscort.get(0).getRemarks());
					escortList.put("escortdob",dateFormat.format(listOfEscort.get(0).getDateOfBirth()));
					escortList.put("escortBatchNum",listOfEscort.get(0).getEscortEmployeeId());
					escortList.put("escortVendorName",listOfEscort.get(0).getEfmFmVendorMaster().getVendorName());
					listOfEscortDetails.add(escortList);			
			 }
			 log.info("serviceEnd -UserId :" + eFmFmEscortMasterPO.getUserId());
		return Response.ok(listOfEscortDetails, MediaType.APPLICATION_JSON).build();
	}
	/**
	* The escortDetails method implemented.
	* for getting the escort details based on the escortId.   
	*
	* @author  Rajan R
	* 
	* @since   2015-05-25 
	* 
	* @return escort details.
	*/	
	@POST
	@Path("/disableEscortallDetails")
	public Response disableEscortDetails(EFmFmEscortMasterPO eFmFmEscortMasterPO){		
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");				
		List<Map<String, Object>> listOfEscortDetails= new ArrayList<Map<String, Object>>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + eFmFmEscortMasterPO.getUserId());		
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEscortMasterPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmEscortMasterPO.getUserId());
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		List<EFmFmEscortMasterPO> listOfEscortList=iVehicleCheckInBO.getAllDisableEscortDetails(eFmFmEscortMasterPO.getCombinedFacility());		 
			if(!(listOfEscortList.isEmpty())){
				for(EFmFmEscortMasterPO listOfEscort:listOfEscortList){
					Map<String, Object>  escortList= new HashMap<String, Object>();					
					escortList.put("escortId",listOfEscort.getEscortId());
					escortList.put("escortName",listOfEscort.getFirstName());
					escortList.put("escortAddress",listOfEscort.getAddress());
					escortList.put("permanentAddress",listOfEscort.getPermanentAddress());
					escortList.put("escortMobileNumber",listOfEscort.getMobileNumber());
					escortList.put("escortFatherName",listOfEscort.getFatherName());
					escortList.put("escortGender",listOfEscort.getGender());
					escortList.put("escortPincode",listOfEscort.getPincode());
					escortList.put("remarks",listOfEscort.getRemarks());
					escortList.put("escortdob",dateFormat.format(listOfEscort.getDateOfBirth()));
					escortList.put("escortBatchNum",listOfEscort.getEscortEmployeeId());
					escortList.put("escortVendorName",listOfEscort.getEfmFmVendorMaster().getVendorName());
					listOfEscortDetails.add(escortList);
				}
			 }
			 log.info("serviceEnd -UserId :" + eFmFmEscortMasterPO.getUserId());
		return Response.ok(listOfEscortDetails, MediaType.APPLICATION_JSON).build();
	}
	/**
	* The escortCheckInDetails method implemented.
	* for getting the escort details based on the escortId.   
	*
	* @author  Rajan R
	* 
	* @since   2015-05-25 
	* 
	* @return escort details.
	*/	
	
	@POST
	@Path("/escortCheckInDetails")
	@Consumes("application/json")
	@Produces("application/json")
	public Response escortCheckInDetails(EFmFmClientBranchPO  eFmFmClientBranchPO){			
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");				
		List<Map<String, Object>> listOfEscortDetails= new ArrayList<Map<String, Object>>();	
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());			
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientBranchPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmClientBranchPO.getUserId());
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
		List<EFmFmEscortMasterPO> listOfEscort=iVehicleCheckInBO.getAllCheckInEscort(eFmFmClientBranchPO.getCombinedFacility());		 
		if(!(listOfEscort.isEmpty())){				
			for(EFmFmEscortMasterPO escortDetails:listOfEscort){				
					Map<String, Object>  escortList= new HashMap<String, Object>();					
					escortList.put("escortId",escortDetails.getEscortId());
					escortList.put("escortName",escortDetails.getFirstName());
					escortList.put("escortAddress",escortDetails.getAddress());
					escortList.put("escortMobileNumber",escortDetails.getMobileNumber());	
					escortList.put("escortFatherName",escortDetails.getFatherName());
					escortList.put("escortGender",escortDetails.getGender());
					escortList.put("escortPincode",escortDetails.getPincode());
					escortList.put("escortBatchNum",escortDetails.getEscortEmployeeId());
					escortList.put("escortVendorName",escortDetails.getEfmFmVendorMaster().getVendorName());
					escortList.put("remarks",escortDetails.getRemarks());
					escortList.put("facilityName",escortDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
					listOfEscortDetails.add(escortList);			
				}			
			}
		 log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(listOfEscortDetails, MediaType.APPLICATION_JSON).build();
	}
	
	
	/*
	 * Below Method used to check in the escort. 
	 */
	@POST
	@Path("/checkInEscort")
	@Consumes("application/json")
	@Produces("application/json")	
	public Response escortCheckIn(EFmFmEscortMasterPO eFmFmEscortMasterPO){
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEscortMasterPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmEscortMasterPO.getUserId());
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
		log.info("serviceStart -UserId :" + eFmFmEscortMasterPO.getUserId());
		String[] listOfEscortId =eFmFmEscortMasterPO.getEscortIds().split(",");
		
		for(String ids:listOfEscortId){
			EFmFmEscortCheckInPO eFmFmEscortCheckInPO=new EFmFmEscortCheckInPO();
			EFmFmEscortMasterPO eFmFmEscortMaster=new EFmFmEscortMasterPO();
			eFmFmEscortMaster.setEscortId(Integer.parseInt(ids));
			eFmFmEscortCheckInPO.seteFmFmEscortMaster(eFmFmEscortMaster);
			eFmFmEscortCheckInPO.setEscortCheckInTime(new Date());
			eFmFmEscortCheckInPO.setStatus("Y");				
			iVehicleCheckInBO.saveEscortCheckIn(eFmFmEscortCheckInPO);	
		}
		
	
		 log.info("serviceEnd -UserId :" + eFmFmEscortMasterPO.getUserId());
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	}
	
	
	
	
	/**
	 * 
	 * @param eFmFmEscortMasterPO
	 * 
	 * @return Success after checkout the Escort
	 */
	@POST
	@Path("/checkOutEscort")
	@Consumes("application/json")
	@Produces("application/json")	
	public Response escortCheckOutFunction(EFmFmEscortMasterPO eFmFmEscortMasterPO){
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEscortMasterPO.getUserId()))){
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmEscortMasterPO.getUserId());
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
		log.info("serviceStart -UserId :" + eFmFmEscortMasterPO.getUserId());
		List<EFmFmEscortCheckInPO> checkInDetails=iVehicleCheckInBO.
				getParticulaEscortDetailFromEscortId(eFmFmEscortMasterPO.getCombinedFacility(), eFmFmEscortMasterPO.getEscortCheckInId());
		checkInDetails.get(0).setStatus("N");
		checkInDetails.get(0).setEscortCheckOutTime(new Date());
		iVehicleCheckInBO.update(checkInDetails.get(0));
		 log.info("serviceEnd -UserId :" + eFmFmEscortMasterPO.getUserId());
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/availableEscortDetails")
	@Consumes("application/json")
	@Produces("application/json")
	public Response availableEscortDetails(EFmFmClientBranchPO  eFmFmClientBranchPO){		
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmClientBranchPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmClientBranchPO.getUserId());
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
		log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
		List<Map<String, Object>> listOfEscortDetails= new ArrayList<Map<String, Object>>();	
		
		List<EFmFmEscortCheckInPO> listOfEscort=iVehicleCheckInBO.getAllCheckedInEscort(eFmFmClientBranchPO.getCombinedFacility());		 
		if(!(listOfEscort.isEmpty())){				
			for(EFmFmEscortCheckInPO escortDetails:listOfEscort){				
					Map<String, Object>  escortList= new HashMap<String, Object>();					
					escortList.put("escortId",escortDetails.geteFmFmEscortMaster().getEscortId());
					escortList.put("escortCheckId",escortDetails.getEscortCheckInId());
					escortList.put("escortName",escortDetails.geteFmFmEscortMaster().getFirstName());					
					escortList.put("escortMobileNumber",escortDetails.geteFmFmEscortMaster().getMobileNumber());
					escortList.put("escortAddress",escortDetails.geteFmFmEscortMaster().getAddress());
					escortList.put("escortBatchNum",escortDetails.geteFmFmEscortMaster().getEscortEmployeeId());
					escortList.put("escortVendorName",escortDetails.geteFmFmEscortMaster().getEfmFmVendorMaster().getVendorName());
					escortList.put("facilityName",escortDetails.geteFmFmEscortMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

					listOfEscortDetails.add(escortList);			
				}			
			}
		 log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(listOfEscortDetails, MediaType.APPLICATION_JSON).build();
	}
	
	
	
	/**
	* The modifyEscortDetails method implemented.
	* for Modifying Escort details.   
	*
	* @author  Rajan R
	* 
	* @since   2015-05-29 
	* 
	* @return modified Status.
	*/	
	@POST
	@Path("/modifyEscortDetails")
	@Consumes("application/json")
	@Produces("application/json")	
	public Response modifyEscortDetails(EFmFmEscortMasterPO eFmFmEscortMasterPO){
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + eFmFmEscortMasterPO.getUserId());
		
		
		Map<String, Object>  request = new HashMap<String, Object>();
		
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEscortMasterPO.getUserId()))){
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmEscortMasterPO.getUserId());
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
		
		
		
		String status="Input";
    	StringBuffer temp=new StringBuffer("PleaseCheck");
        
			if(true){
				
			if(eFmFmEscortMasterPO.getFirstName()==null||eFmFmEscortMasterPO.getFirstName().equals(""))
			{temp.append("::Escort Name cannot not be empty");
		    status="Fail";	
			}	
			else{
			CharSequence escortName = eFmFmEscortMasterPO.getFirstName();
			Matcher matcher= Validator.alphaSpaceDot(escortName);
			if(!matcher.matches()||eFmFmEscortMasterPO.getFirstName().length()>20)
			{temp.append("::Escort Name can have only alphabets, space, dot and range max 20 letters");
			    status="Fail";	
			}
			}
			
			
			
			
			if(eFmFmEscortMasterPO.getMobileNumber()==null||eFmFmEscortMasterPO.getMobileNumber().equals(""))
			{status="Fail";
			temp.append("::mobile number cannot be empty");
			}
			else{			
			CharSequence escortMobile = eFmFmEscortMasterPO.getMobileNumber();
			Matcher matcher1= Validator.mobNumber(escortMobile);
			if(!matcher1.matches()||eFmFmEscortMasterPO.getMobileNumber().length()<6||eFmFmEscortMasterPO.getMobileNumber().length()>18)
			{temp.append("::Escort mobile number sholud have numbers only and range min 6 max 18 digits");
			 status="Fail";
			}}
			
			
			
			if(eFmFmEscortMasterPO.getEscortEmployeeId()==null||eFmFmEscortMasterPO.getEscortEmployeeId().equals(""))
			{temp.append("::Escort Badge number cannot be empty");
			status="Fail";				
			}
			else{
			CharSequence escortBadgeNumber = eFmFmEscortMasterPO.getEscortEmployeeId();
			Matcher matcher2= Validator.alphaNumeric(escortBadgeNumber);
			if(!matcher2.matches())
			{temp.append("::Escort Badge number should have charaters and numbers only");
			status="Fail";
			}
			}
			
			
			if(eFmFmEscortMasterPO.getDateOfBirth()==null||eFmFmEscortMasterPO.getDateOfBirth().equals(""))
			{temp.append("::Escort date of birth cannot be empty");
			status="Fail";
			}
			
			if(eFmFmEscortMasterPO.getAddress()==null||eFmFmEscortMasterPO.getAddress().equals("")||eFmFmEscortMasterPO.getAddress().length()<10||eFmFmEscortMasterPO.getAddress().length()>200)
			{temp.append("::Escort address should not be less than 10 and more than 200 characters");
			status="Fail";
			}
			
			if(eFmFmEscortMasterPO.getPermanentAddress()==null||eFmFmEscortMasterPO.getPermanentAddress().equals("")||eFmFmEscortMasterPO.getPermanentAddress().length()<10||eFmFmEscortMasterPO.getPermanentAddress().length()>200)
			{temp.append("::Escort Permanent address should not be less than 10 and more than 200 characters");
			status="Fail";
			}
			
			if(status.equals("Fail"))
			{log.info("Invalid input:");
			request.put("InputInvalid", temp);
			return Response.ok(request, MediaType.APPLICATION_JSON).build();
			}
            }
			log.info("valid input:");
		
		
		List<EFmFmEscortMasterPO> escortMasterPO=iVehicleCheckInBO.getEscortDetailsByActiveFlg(eFmFmEscortMasterPO.getEscortId());
		request.put("status", "failed");
		if(!escortMasterPO.isEmpty()){
		    escortMasterPO.get(0).setFirstName(eFmFmEscortMasterPO.getFirstName());
		    escortMasterPO.get(0).setAddress(eFmFmEscortMasterPO.getAddress());	    
		    escortMasterPO.get(0).setMobileNumber(eFmFmEscortMasterPO.getMobileNumber());
		    escortMasterPO.get(0).setEscortEmployeeId(eFmFmEscortMasterPO.getEscortEmployeeId());
		    escortMasterPO.get(0).setDateOfBirth(eFmFmEscortMasterPO.getDateOfBirth());
		    escortMasterPO.get(0).setPermanentAddress(eFmFmEscortMasterPO.getPermanentAddress());
		    escortMasterPO.get(0).setIsActive(eFmFmEscortMasterPO.getIsActive());
		    escortMasterPO.get(0).setPincode(eFmFmEscortMasterPO.getPincode());
		    escortMasterPO.get(0).setFatherName(eFmFmEscortMasterPO.getFatherName());
		    iVehicleCheckInBO.updateEscortDetails(escortMasterPO.get(0));
		    request.put("status", "success");
		 }
		 log.info("serviceEnd -UserId :" + eFmFmEscortMasterPO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}	
	
	
	
	/**
	* The modifyEscortDetails method implemented.
	* for Modifying Escort details.   
	*
	* @author  Rajan R
	* 
	* @since   2015-05-29 
	* 
	* @return modified Status.
	*/	
	@POST
	@Path("/disableAndEnableEscortStatus")
	@Consumes("application/json")
	@Produces("application/json")	
	public Response disableAndEnableEscortStatus(EFmFmEscortMasterPO eFmFmEscortMasterPO){
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + eFmFmEscortMasterPO.getUserId());
		
		
		Map<String, Object>  request = new HashMap<String, Object>();
		
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEscortMasterPO.getUserId()))){
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmEscortMasterPO.getUserId());
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
		List<EFmFmEscortMasterPO> escortMasterPO=iVehicleCheckInBO.getEscortDetailsByActiveFlg(eFmFmEscortMasterPO.getEscortId());
		request.put("status", "failed");
		if(!escortMasterPO.isEmpty()){		 
		    escortMasterPO.get(0).setIsActive(eFmFmEscortMasterPO.getIsActive());
		    iVehicleCheckInBO.updateEscortDetails(escortMasterPO.get(0));
		    request.put("status", "success");
		 }
		 log.info("serviceEnd -UserId :" + eFmFmEscortMasterPO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}	
	/**
	* The addEscortDetails method implemented.
	* for add Escort details.   
	*
	* @author  Rajan R
	* 
	* @since   2015-05-29 
	* 
	* @return add Status.
	*/	
	@POST
	@Path("/addEscortDetails")
	@Consumes("application/json")
	@Produces("application/json")	
	public Response addEscortDetails(EFmFmEscortMasterPO eFmFmEscortMasterPO){
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		IVendorDetailsBO iVendorMasterBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + eFmFmEscortMasterPO.getUserId());		
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		Map<String, Object> request = new HashMap<String, Object>();

		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEscortMasterPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmEscortMasterPO.getUserId());
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
		
		
		
		
		String status="Input";
    	StringBuffer temp=new StringBuffer("PleaseCheck");
        
			if(true){
				
           if(eFmFmEscortMasterPO.getFirstName()==null||eFmFmEscortMasterPO.getFirstName().equals(""))
           {temp.append("::Escort name cannot be empty");
		    status="Fail";        	   
           }
           else{
			CharSequence escortName=eFmFmEscortMasterPO.getFirstName();
			Matcher matcher= Validator.alphaSpaceDot(escortName);
			if(!matcher.matches()||eFmFmEscortMasterPO.getFirstName().length()>20)
			{temp.append("::Escort name should contain only alphabets space and dot and range max 20 letters");
			    status="Fail";	
			}}
           
           if(eFmFmEscortMasterPO.getMobileNumber()==null || eFmFmEscortMasterPO.getMobileNumber().equals(""))
           {temp.append("::Escort mobile number cannot be empty");
		    status="Fail";          	   
           }else{
           CharSequence escortMobile=eFmFmEscortMasterPO.getMobileNumber();
			Matcher matcher= Validator.mobNumber(escortMobile);
			if(!matcher.matches()||eFmFmEscortMasterPO.getMobileNumber().length()<6||eFmFmEscortMasterPO.getMobileNumber().length()>18)
			{temp.append("::Escort mobile number should contain only numbers and range min 6 max 18 letters");
			    status="Fail";	
			}
           }
		
           if(eFmFmEscortMasterPO.getVendorName()==null||eFmFmEscortMasterPO.getVendorName().equals(""))
           {  temp.append("::Escort Vendor name cannot be empty");
   		    status="Fail"; 
           }
           else{
           CharSequence vendorName=eFmFmEscortMasterPO.getVendorName();
			Matcher matcher= Validator.alphaSpaceDot(vendorName);
			if(!matcher.matches()||eFmFmEscortMasterPO.getVendorName().length()>50)
			{temp.append("::Escort Vendor namer should contain only characters space dot and range max 50 characters");
		    status="Fail";				
			}}
			
           if(eFmFmEscortMasterPO.getEscortEmployeeId()==null||eFmFmEscortMasterPO.getEscortEmployeeId().equals(""))
			{temp.append("::Escort Badge number cannot be empty");
			status="Fail";				
			}
			else{
			CharSequence escortBadgeNumber = eFmFmEscortMasterPO.getEscortEmployeeId();
			Matcher matcher2= Validator.alphaNumeric(escortBadgeNumber);
			if(!matcher2.matches())
			{temp.append("::Escort Badge number should have charaters and numbers only");
			status="Fail";
			}
			}
           
           if(eFmFmEscortMasterPO.getDateOfBirth()==null||eFmFmEscortMasterPO.getDateOfBirth().equals(""))
			{temp.append("::Escort date of birth cannot be empty");
			status="Fail";
			}
			
			if(eFmFmEscortMasterPO.getAddress()==null||eFmFmEscortMasterPO.getAddress().equals("")||eFmFmEscortMasterPO.getAddress().length()<10||eFmFmEscortMasterPO.getAddress().length()>200)
			{temp.append("::Escort address should not be less than 10 and more than 200 characters");
			status="Fail";
			}
			
			if(eFmFmEscortMasterPO.getPermanentAddress()==null||eFmFmEscortMasterPO.getPermanentAddress().equals("")||eFmFmEscortMasterPO.getPermanentAddress().length()<10||eFmFmEscortMasterPO.getPermanentAddress().length()>200)
			{temp.append("::Escort Permanent address should not be less than 10 and more than 200 characters");
			status="Fail";
			}
			
			if(status.equals("Fail"))
			{log.info("Invalid input:");
			request.put("inputInvalid", temp);
			return Response.ok(request, MediaType.APPLICATION_JSON).build();
			}
            }
			
          log.info("valid input:");
		
		
		List<EFmFmVendorMasterPO> venderExist=iVendorMasterBO.getAllVendorName(eFmFmEscortMasterPO.getVendorName(),eFmFmEscortMasterPO.getCombinedFacility());				
		if(venderExist.isEmpty()){
			request.put("status", "vnotExist");
			 log.info("serviceEnd -UserId :" + eFmFmEscortMasterPO.getUserId());
    		return Response.ok(request, MediaType.APPLICATION_JSON).build();	
        }	
		eFmFmEscortMasterPO.setCombinedFacility(eFmFmEscortMasterPO.getCombinedFacility());
		List<EFmFmEscortMasterPO> escortDetail=iVehicleCheckInBO.getMobileNoDetails(eFmFmEscortMasterPO);
		if(!(escortDetail.isEmpty())){
			request.put("status", "mExist");
			 log.info("serviceEnd -UserId :" + eFmFmEscortMasterPO.getUserId());
			return Response.ok(request, MediaType.APPLICATION_JSON).build();
		}
		log.info(eFmFmEscortMasterPO.getMobileNumber());
		eFmFmEscortMasterPO.setIsActive("Y");
		eFmFmEscortMasterPO.setMobileNumber(eFmFmEscortMasterPO.getMobileNumber());
		eFmFmEscortMasterPO.setPoliceVerification("DONE");
		eFmFmEscortMasterPO.setGender("MALE");		
		eFmFmEscortMasterPO.setUserName(eFmFmEscortMasterPO.getEscortEmployeeId());			 				
		eFmFmEscortMasterPO.setWrongPassAttempt(0);
		eFmFmEscortMasterPO.setTempPassWordChange(false);
		eFmFmEscortMasterPO.setLoggedIn(false);		
		eFmFmEscortMasterPO.setImeiNumber("NO");			 
		eFmFmEscortMasterPO.setPoliceVerification("NO");		
		eFmFmEscortMasterPO.setEfmFmVendorMaster(venderExist.get(0));
	    iVehicleCheckInBO.saveEscortDetails(eFmFmEscortMasterPO);	
	    
	    try {
            Thread thread1 = new Thread(new Runnable() {
                @Override
				public void run() {
                    SendMailBySite mailSender=new SendMailBySite();
                    String toMailId = ContextLoader.getContext().getMessage("user.toMailSender", null, "toMailSender", null);
                    mailSender.mailForEscortRegistration(toMailId, "team", eFmFmEscortMasterPO.getFirstName());
                	}
            });
            thread1.start();
        } catch (Exception e) {
            log.info("vendor excel registration mail" + e);
        } 
  	eFmFmEscortMasterPO.setCombinedFacility(eFmFmEscortMasterPO.getCombinedFacility());
		List<EFmFmEscortMasterPO> lastescortDetail=iVehicleCheckInBO.getMobileNoDetails(eFmFmEscortMasterPO);
		if(!(lastescortDetail.isEmpty())){
			request.put("escortId",lastescortDetail.get(0).getEscortId());
			request.put("escortName",lastescortDetail.get(0).getFirstName());					
			request.put("escortMobileNumber",lastescortDetail.get(0).getMobileNumber());
			request.put("escortAddress",lastescortDetail.get(0).getAddress());
			request.put("escortBatchNum",lastescortDetail.get(0).getEscortEmployeeId());
			request.put("escortVendorName",lastescortDetail.get(0).getEfmFmVendorMaster().getVendorName());
		}
	    request.put("status", "success");
	    log.info("serviceEnd -UserId :" + eFmFmEscortMasterPO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}
	
	/*
	 * 
	 * Upload Document Details
	 */
	@POST
	@Path("/listOfDocumentDetails")
	public Response listOfDocumentDetails(EFmFmEscortMasterPO eFmFmEscortMasterPO){		
		IApprovalBO iApprovalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmEscortMasterPO.getUserId()))){
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmEscortMasterPO.getUserId());
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
		List<Map<String, Object>> listOfEscortDetails= new ArrayList<Map<String, Object>>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		log.info("serviceStart -UserId :" + eFmFmEscortMasterPO.getUserId());
		List<EFmFmEscortDocsPO> listOfEscort=iApprovalBO.getAllEscortuploadFileDetails(eFmFmEscortMasterPO.getEscortId());		 
		if(!(listOfEscort.isEmpty())){				
			for(EFmFmEscortDocsPO escortDetails:listOfEscort){				
					Map<String, Object>  escortList= new HashMap<String, Object>();					
					escortList.put("escortId",escortDetails.geteFmFmEscortMaster().getEscortId());
					escortList.put("creationDate",dateFormat.format(escortDetails.getCreationTime()));
					escortList.put("documentName",escortDetails.getDocumentName());
					escortList.put("escortDocId",escortDetails.getEscortDocId());						
					escortList.put("pathName",escortDetails.getUploadpath()
		                    .substring(escortDetails.getUploadpath().indexOf("upload") - 1));				
					listOfEscortDetails.add(escortList);					
				}			
			}
		 log.info("serviceEnd -UserId :" + eFmFmEscortMasterPO.getUserId());
		return Response.ok(listOfEscortDetails, MediaType.APPLICATION_JSON).build();
	}	
	
	
	
	@POST
	@Path("/escortDocument")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")		
   public Response uploadFile(
				@FormDataParam("filename") InputStream uploadedInputStream,
				@FormDataParam("filename") InputStream uploadedSizeInputStream,
				@FormDataParam("filename") InputStream uploadedImageInputStream,
				@FormDataParam("filename") FormDataContentDisposition fileDetail,
				@QueryParam("branchId") int branchId,
				@QueryParam("escortId") int escortId,	
				@QueryParam("typeOfUploadDoc") String typeOfUploadDoc,	
				@Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException {		
		boolean  OsName =System.getProperty("os.name").startsWith("Windows");
		String status="";
		String filePath;
		log.info(branchId+"service"+escortId+"Start -UserId :" +userId);
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
		if(OsName){			
			filePath =SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER+ fileDetail.getFileName();			
			File pathExist = new File(SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER);			
			if(!pathExist.exists()){
				new File(SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER).mkdir();
			}			
		}else{
			filePath  =SERVER_UPLOAD_LINUX_LOCATION_FOLDER+ fileDetail.getFileName();
			File pathExist = new File(SERVER_UPLOAD_LINUX_LOCATION_FOLDER);			
			if(!pathExist.exists()){
				new File(SERVER_UPLOAD_LINUX_LOCATION_FOLDER).mkdir();
			}
		}		
		IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");		
		IApprovalBO iApprovalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");			
		List<EFmFmEscortMasterPO> escortDetails=vehicleCheckInBO.getParticularEscortDetails(escortId);		
		EFmFmEscortDocsPO eFmFmEscortDocsPO=new EFmFmEscortDocsPO();
		File fileExist = new File(filePath);
		
		if (!((fileDetail.getFileName().toLowerCase().endsWith(".png")) || (fileDetail.getFileName().toLowerCase().endsWith(".jpg")) || (fileDetail.getFileName().toLowerCase().endsWith(".jpeg")))){
			log.info("notAnImage");
			status="notAnImage";
	        return Response.ok(status, MediaType.APPLICATION_JSON).build();
	        }
		
		boolean oneCondition=true;
		if (ImageIO.read(uploadedImageInputStream) == null && oneCondition) {
			oneCondition=false;
			status="notAnImage";
			log.info("notAnImage");
			return Response.ok(status, MediaType.APPLICATION_JSON).build();
		}
		
		if(oneCondition){
		int bytes=getBytes(uploadedSizeInputStream).length;
		double kilobytes = (bytes / 1000);
		double megabytes = (kilobytes / 1000);
  		log.info("bytes :" +bytes);
  		log.info("kilobytes :" +Math.round(kilobytes));
  		log.info("megabytes :" +Math.round(megabytes));
        if(megabytes > (escortDetails.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO().getImageUploadSize())){
        	log.info("Inside bigSize");
			status="bigSize";			
//			requests.put("status", "bigSize-"+escortDetails.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO().getImageUploadSize());
			return Response.ok(status, MediaType.APPLICATION_JSON).build();
        }	  
		}
        
		
		
		if(!fileExist.exists() && !fileExist.isDirectory()){			
	//		requests.put("status", "success");
			status="success";			

			// save the file to the server	
			saveFile(uploadedInputStream, filePath);
		}else{
			log.info("serviceEnd -UserId :" +userId);
			status="exist";			
			return Response.ok(status, MediaType.APPLICATION_JSON).build();
		}	
		eFmFmEscortDocsPO.setStatus("Y");
		eFmFmEscortDocsPO.setCreationTime(new Date());
		eFmFmEscortDocsPO.setUploadpath(filePath);
		eFmFmEscortDocsPO.setDocumentName(typeOfUploadDoc);
		eFmFmEscortDocsPO.seteFmFmEscortMaster(escortDetails.get(0));
		iApprovalBO.addUploadDetails(eFmFmEscortDocsPO);
		log.info("serviceEnd -UserId :" +userId);
		return Response.ok(status, MediaType.APPLICATION_JSON).build();
	}
	// save uploaded file to a defined location on the server
	 void saveFile(InputStream uploadedInputStream, String serverLocation) {

		try {
			OutputStream outpuStream = new FileOutputStream(new File(
					serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			outpuStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
			outpuStream.flush();
			outpuStream.close();
		} catch (IOException e) {
			log.info("Error"+e);
		}

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