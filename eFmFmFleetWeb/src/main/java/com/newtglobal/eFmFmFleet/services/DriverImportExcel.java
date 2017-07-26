package com.newtglobal.eFmFmFleet.services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Validator;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/xlUtilityDriverUpload")
public class DriverImportExcel {	
    private static final String SERVER_UPLOAD_LINUX_LOCATION_FOLDER  = ContextLoader.getContext().getMessage("upload.docsLinux", null, "docsLinux", null);
    private static final String SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER  = ContextLoader.getContext().getMessage("upload.docsWindows", null, "docsWindows", null);
	private static Log log = LogFactory.getLog(DriverImportExcel.class);
	DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	/*
	 * @Reading Driver details from driver_master xl utility.
	 * @Stored all the values on Arraylist.
	 */	
	@POST
	@Path("/driverRecord")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	  public Response upload(@FormDataParam("filename") InputStream uploadedInputStream,
			  @FormDataParam("filename") InputStream uploadedSizeInputStream,
	            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
	            @QueryParam("combinedFacility") String combinedFacility,
	            @Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException {
	   	 String status = "success-";
	   	 log.info("serviceStart -UserId :" +userId);
	   	 int noOfcolumn=20; 
	        List<Map<String, Object>> response =new ArrayList<Map<String, Object>>();
	       	Map<String, Object>  vendorReqExcel= new HashMap<String, Object>(); 
	       	Map<Integer, Object>  driverExcelDetails= new HashMap<Integer, Object>();
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
    				vendorReqExcel.put("IssueStatus",status);
              		response.add(vendorReqExcel);
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
					vendorReqExcel.put("IssueStatus",status);
	          		response.add(vendorReqExcel);
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
		                	if(columnValue.size()>=20){   
			                		if(!driverMasterExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,userId,combinedFacility).isEmpty()){                		
			                			response.addAll(driverMasterExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,userId,combinedFacility));
			                		}else{
			                			driverExcelDetails.put(rowId, columnValue);
			                		}
		                		}else{
		                				status="Kinldy check the no -of Column :20 - Entered Column No:"+columnValue.size();	
		                				vendorReqExcel.put("RNo",Integer.toString(rowId));
		                				vendorReqExcel.put("IssueStatus",status);
			                    		response.add(vendorReqExcel);
		                	}              		
	                	}else{
	                		  if(columnValue.size()<20){                			              			
	                			  status="Kinldy fill the all the Column's";
	                			   vendorReqExcel.put("RNo",Integer.toString(rowId));
	                				vendorReqExcel.put("IssueStatus",status);
	  	                    		response.add(vendorReqExcel);
	  	                    	
	                		}else{
	                			//Need to configure below Text some where
	                			if(!"VendorName".equalsIgnoreCase(columnValue.get(0).toString().replace(" ","").trim()) 
	                					|| !"DriverFirstName".equalsIgnoreCase(columnValue.get(1).toString().replace(" ","").trim()) 
	                					|| !"DriverMiddleName".equalsIgnoreCase(columnValue.get(2).toString().replace(" ","").trim()) 
	                					|| !"DriverLastName".equalsIgnoreCase(columnValue.get(3).toString().replace(" ","").trim())
	                					|| !"FatherName".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim())
	                					|| !"Gender".equalsIgnoreCase(columnValue.get(5).toString().replace(" ","").trim())
	                					|| !"MobileNumber".equalsIgnoreCase(columnValue.get(6).toString().replace(" ","").trim()) 
	                        			|| !"DriverLicenseNumber".equalsIgnoreCase(columnValue.get(7).toString().replace(" ","").trim())	                        			
	                        			|| !"Licensevalidity".equalsIgnoreCase(columnValue.get(8).toString().replace(" ","").trim())	                        				                        			
	                        			|| !"MedicalFitnesscertificatevalidity".equalsIgnoreCase(columnValue.get(9).toString().replace(" ","").trim()) 			
	                        			|| !"PoliceVerification".equalsIgnoreCase(columnValue.get(10).toString().replace(" ","").trim())
	                        			|| !"State".equalsIgnoreCase(columnValue.get(11).toString().replace(" ","").trim()) 
	                        			|| !"CityName".equalsIgnoreCase(columnValue.get(12).toString().replace(" ","").trim())                        				                        			
	                        			|| !"PinCode".equalsIgnoreCase(columnValue.get(13).toString().replace(" ","").trim())
	                        			|| !"Address".equalsIgnoreCase(columnValue.get(14).toString().replace(" ","").trim())	                				
	                					|| !"PermanentAddress".equalsIgnoreCase(columnValue.get(15).toString().replace(" ","").trim())
										|| !"BadgeNumber".equalsIgnoreCase(columnValue.get(16).toString().replace(" ","").trim())
										|| !"BadgeDate".equalsIgnoreCase(columnValue.get(17).toString().replace(" ","").trim())
										|| !"BadgeValidity".equalsIgnoreCase(columnValue.get(18).toString().replace(" ","").trim())
										|| !"JoinDate".equalsIgnoreCase(columnValue.get(19).toString().replace(" ","").trim())){										       				   
		                				    vendorReqExcel.put("RNo",Integer.toString(rowId));
			                				vendorReqExcel.put("IssueStatus","Kinldy check the Column Name & No of Columns ");
			  	                    		response.add(vendorReqExcel);
			  	                    		log.info("serviceEnd -UserId :" +userId);
			  	                    		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	                			}
	                		}
	                	}
					}              
	            }
	            log.info("SizedriverExcelDetails"+driverExcelDetails.size());
				if(response.isEmpty()){
					if(!driverExcelDetails.isEmpty()){
						response=driverRecord(driverExcelDetails,branchId,userId,combinedFacility);	
					}
				}

	        } catch (Exception e) {
	        	log.info(" Error :" +e);
      		try{
            	if(e.getCause().toString().contains("InvalidFormatException")){
    				status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
    				vendorReqExcel.put("IssueStatus",status);
              		response.add(vendorReqExcel);
              		log.info(e+" serviceEnd -UserId :" +userId);
                    return Response.ok(response, MediaType.APPLICATION_JSON).build();
            	}
          		}catch(Exception e1){
              		log.info(" Error :" +e1);
          		}
    			status="Kinldy check the excel for any empty column,System will except only excel format without formula and special symbols.";  	                    		
    			vendorReqExcel.put("IssueStatus",status);
          		response.add(vendorReqExcel);
          		log.info(e+" serviceEnd -UserId :" +userId);
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
                
}
	       log.info("serviceEnd -UserId :" +userId);
	        return Response.ok(response, MediaType.APPLICATION_JSON).build();
	    }
	    
	    
	    
	    private  List<Map<String, Object>> driverMasterExcelValidator(String rowId,ArrayList<Object> columnValue,int branchId,int noOfcolumn,int userId,String combinedFacility) throws ParseException{
	    	IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");	    	
		    IVendorDetailsBO iVendorMasterBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		    List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();		   
		    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		    String currentDate=dateFormat.format(new Date());
			Date sytemCurrentDate=dateFormat.parse(currentDate);					
			log.info("driverMasterExcelValidator"+combinedFacility);

		    String validInt="^[0-9]*$";
		    String alphanumaric="^[a-zA-Z0-9]*$";
		    String charAt="^[a-zA-Z]*$";
			List<EFmFmVendorMasterPO> venderNameExist = iVendorMasterBO.getAllVendorName(columnValue.get(0).toString().trim(),combinedFacility);
 		    if (columnValue.get(0).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(0).toString() ==null || columnValue.get(0).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(0).toString().isEmpty()) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Vendor Name : "+columnValue.get(0).toString();				
				empReqExcelRows.put("RNo",rowId.concat(",0"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				if (venderNameExist.isEmpty()) {
					 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			    	String status = " Vendor Name not available -" + columnValue.get(0).toString().trim();
			    	empReqExcelRows.put("RNo",rowId.concat(",0"));
					empReqExcelRows.put("IssueStatus",status);	
					childRowResponse.add(empReqExcelRows);
			    } 
				
			}		
			
		    if (columnValue.get(1).toString().equalsIgnoreCase("CELL_TYPE_BLANK")|| columnValue.get(1).toString() ==null || columnValue.get(1).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(1).toString().isEmpty() || !columnValue.get(1).toString().replace(" ", "").trim().matches(charAt)) {	
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Driver Name ,special characters are not allowed : "+columnValue.get(1).toString();
				empReqExcelRows.put("RNo",rowId.concat(",1"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			/*if (columnValue.get(2).toString() !=null || !columnValue.get(2).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| !columnValue.get(2).toString().isEmpty()) {				
				if(!columnValue.get(2).toString().replace(" ", "").trim().matches(charAt)){
					String status="kindly check the Driver middle Name ,special characters are not allowed : "+columnValue.get(2).toString();
					empReqExcelRows.put(rowId.concat(",2"), status);
				}
			}
			
			if (columnValue.get(3).toString() !=null || !columnValue.get(3).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| !columnValue.get(3).toString().isEmpty()) {	
				if(!columnValue.get(3).toString().replace(" ", "").trim().matches(charAt)){
				String status="kindly check the Driver Last Name ,special characters are not allowed : "+columnValue.get(3).toString();
				empReqExcelRows.put(rowId.concat(",3"), status);
				}
			}*/
			
			if (columnValue.get(4).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(4).toString() ==null || columnValue.get(4).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(4).toString().isEmpty() || !columnValue.get(4).toString().replace(" ", "").trim().matches(charAt)) {	
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Father Name : "+columnValue.get(4).toString();
				empReqExcelRows.put("RNo",rowId.concat(",4"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			if (columnValue.get(5).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(5).toString() ==null || columnValue.get(5).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(5).toString().isEmpty() || !columnValue.get(5).toString().replace(" ", "").trim().matches(charAt)) {	
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Driver sex : "+columnValue.get(5).toString();
				empReqExcelRows.put("RNo",rowId.concat(",5"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else if (!"FEMALE".equalsIgnoreCase(columnValue.get(5).toString()) && !"MALE".equalsIgnoreCase(columnValue.get(5).toString())){
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String	status="kindly check the Driver sex : "+columnValue.get(5).toString();
				empReqExcelRows.put("RNo",rowId.concat(","));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
			if (columnValue.get(6).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(6).toString() ==null || columnValue.get(6).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(6).toString().isEmpty() || !columnValue.get(6).toString().replace(" ", "").trim().matches(validInt)) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Mobile Number and Mobile Should be Integer : "+columnValue.get(6).toString();
				empReqExcelRows.put("RNo",rowId.concat(",6"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else if((!(venderNameExist.isEmpty())) && columnValue.get(6).toString().length()<=18 && columnValue.get(6).toString().length()>5){	
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					List<EFmFmDriverMasterPO> mobileNoExist=iRouteDetailBO.getValidMobileNumberCheck(columnValue.get(6).toString().trim(),venderNameExist.get(0).getVendorId(),combinedFacility);				
					if(!mobileNoExist.isEmpty()){	    	
						 String status="Mobile Number already available on database  : "+columnValue.get(6).toString();
						 empReqExcelRows.put("RNo",rowId.concat(",6"));
							empReqExcelRows.put("IssueStatus",status);
							childRowResponse.add(empReqExcelRows);
					 }
				
				
			}else{
				if((!(venderNameExist.isEmpty()))){
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				 String status="kindly check the Mobile Number length(ex:India -12 digit),length should be country Code with MobileNumber  : "+columnValue.get(6).toString();
				 empReqExcelRows.put("RNo",rowId.concat(",6"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
			}
			}
			if (columnValue.get(7).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(7).toString() ==null || columnValue.get(7).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(7).toString().isEmpty() || !columnValue.get(7).toString().replace(" ", "").trim().matches(alphanumaric)) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Driver License Number : "+columnValue.get(7).toString();
				empReqExcelRows.put("RNo",rowId.concat(",7"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				if(!(venderNameExist.isEmpty())){
				List<EFmFmDriverMasterPO> licenseNoExist=iRouteDetailBO.getValidLicenseNumber(columnValue.get(7).toString().trim(),venderNameExist.get(0).getVendorId(),combinedFacility);				
				if(!licenseNoExist.isEmpty()){
					 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="Driver License Number already available on database  : "+columnValue.get(7).toString();
					empReqExcelRows.put("RNo",rowId.concat(",7"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}
			}
			}			
			if (columnValue.get(8).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(8).toString() ==null || columnValue.get(8).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(8).toString().isEmpty() ) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the License validity Date : "+columnValue.get(8).toString();
				empReqExcelRows.put("RNo",rowId.concat(",8"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				try{
					Date licensedValidity=dateFormat.parse(columnValue.get(8).toString());			
					if(licensedValidity.getTime() < sytemCurrentDate.getTime()){
						log.info(licensedValidity+"Driver Name:-"+columnValue.get(1).toString());
						String status=currentDate+"License validity should not less than toDay Date : "+columnValue.get(8).toString();
						empReqExcelRows.put("RNo",rowId.concat(",8"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
					}				
					
				}catch(Exception e){
					String status="kindly check the License validity Date formate   : "+e;
					empReqExcelRows.put("RNo",rowId.concat(",8"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}	
			}
	
			
			if (columnValue.get(9).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(9).toString() ==null || columnValue.get(9).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(9).toString().isEmpty() ) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Medical Fitness certificate validity : "+columnValue.get(9).toString();
				empReqExcelRows.put("RNo",rowId.concat(",9"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				try{
					Date midicalValidity=dateFormat.parse(columnValue.get(9).toString());				
					if(midicalValidity.getTime() < sytemCurrentDate.getTime()){
						log.info(midicalValidity+"Driver Name:-"+columnValue.get(1).toString());
						String status=currentDate+"Medical Fitness certificate validity should not less than today's date : "+columnValue.get(9).toString();
						empReqExcelRows.put("RNo",rowId.concat(",9"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
					}				
					
				}catch(Exception e){
					String status="kindly check the Medical Fitness certificate validity Date formate   : "+e;
					empReqExcelRows.put("RNo",rowId.concat(",9"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}	
			}
			
			
			
			if (columnValue.get(10).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(10).toString() ==null || columnValue.get(10).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(10).toString().isEmpty() || !columnValue.get(10).toString().replace(" ", "").trim().matches(charAt) ) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Police Verification Value ex:(Done or InProgress ): "+columnValue.get(10).toString();
				empReqExcelRows.put("RNo",rowId.concat(",10"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			if (columnValue.get(11).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(11).toString() ==null || columnValue.get(11).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(11).toString().isEmpty() || !columnValue.get(11).toString().replace(" ", "").trim().matches(charAt) ) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the State Name : "+columnValue.get(11).toString();
				empReqExcelRows.put("RNo",rowId.concat(",11"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
			if (columnValue.get(12).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(12).toString() ==null || columnValue.get(12).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(12).toString().isEmpty() || !columnValue.get(12).toString().replace(" ", "").trim().matches(charAt) ) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the City Name : "+columnValue.get(12).toString();
				empReqExcelRows.put("RNo",rowId.concat(",12"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
			if (columnValue.get(13).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(13).toString() ==null || columnValue.get(13).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(13).toString().isEmpty() || !columnValue.get(13).toString().replace(" ", "").trim().matches(validInt)) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Pincode : "+columnValue.get(13).toString();
				empReqExcelRows.put("RNo",rowId.concat(",13"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
				
			}
			
			
			if (columnValue.get(14).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(14).toString() ==null || columnValue.get(14).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(14).toString().isEmpty() ) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Current Address : "+columnValue.get(14).toString();
				empReqExcelRows.put("RNo",rowId.concat(",14"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			if (columnValue.get(15).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(15).toString() ==null || columnValue.get(15).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(15).toString().isEmpty() ) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Permanent Address : "+columnValue.get(15).toString();
				empReqExcelRows.put("RNo",rowId.concat(",15"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			if (columnValue.get(16).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(16).toString() ==null || columnValue.get(16).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(16).toString().isEmpty() ) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Badge Number : "+columnValue.get(16).toString();
				empReqExcelRows.put("RNo",rowId.concat(",16"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{	
				if(!(venderNameExist.isEmpty())){
					 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
						List<EFmFmDriverMasterPO> badgeNoExist=iRouteDetailBO.getValidUniqueBatchNumber(columnValue.get(16).toString().trim(),venderNameExist.get(0).getVendorId(),combinedFacility);				
						if(!badgeNoExist.isEmpty()){	    	
							 String status="Badge Number already available on database  : "+columnValue.get(16).toString();
							 empReqExcelRows.put("RNo",rowId.concat(",16"));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
						 }
					
			}		
			}
			if (columnValue.get(17).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(17).toString() ==null || columnValue.get(17).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(17).toString().isEmpty() ) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Badge Date : "+columnValue.get(17).toString();
				empReqExcelRows.put("RNo",rowId.concat(",17"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				try{
					Date batchValidity=dateFormat.parse(columnValue.get(17).toString());					
					if(batchValidity.getTime() > sytemCurrentDate.getTime()){
						log.info(batchValidity+"Driver Name:-"+columnValue.get(1).toString());
						String status=currentDate+"Badge Date should not greater than today's date : "+columnValue.get(17).toString();
						empReqExcelRows.put("RNo",rowId.concat(",17"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
					}				
					
				}catch(Exception e){
					String status="kindly check the Badge Date formate   : "+e;
					empReqExcelRows.put("RNo",rowId.concat(",17"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}	
			}			
			
			if (columnValue.get(18).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(18).toString() ==null || columnValue.get(18).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(18).toString().isEmpty() ) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Badge validity : "+columnValue.get(18).toString();
				empReqExcelRows.put("RNo",rowId.concat(",18"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				try{
					Date batchValidity=dateFormat.parse(columnValue.get(18).toString());
					
					if(batchValidity.getTime() < sytemCurrentDate.getTime()){
						log.info(batchValidity+"Driver Name:-"+columnValue.get(1).toString());
						String status=currentDate+"Badge validity should not less than today's date : "+columnValue.get(18).toString();
						empReqExcelRows.put("RNo",rowId.concat(",18"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
					}				
					
				}catch(Exception e){
					String status="kindly check the Badge validity Date formate   : "+e;
					empReqExcelRows.put("RNo",rowId.concat(",18"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}	
			}			
			if (columnValue.get(19).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(19).toString() ==null || columnValue.get(19).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(19).toString().isEmpty() ) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Date Of Joining : "+columnValue.get(19).toString();
				empReqExcelRows.put("RNo",rowId.concat(",19"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				try{
				}catch(Exception e){
					String status="kindly check the Date Of Joining Date formate   : "+e;
					empReqExcelRows.put("RNo",rowId.concat(",19"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}	
			}							
			return childRowResponse;	
}	
	/**
	 * 
	 * @param add new driver records
	 * @return success after adding the driver
	 * @throws ParseException 
	 */
	
	@POST
	@Path("/addnewdriver")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addNewDriver(EFmFmDriverMasterPO driverMasterPO) throws ParseException{
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
			
		 log.info("serviceStart -UserId :" + driverMasterPO.getUserId());
		Map<String, Object> requests = new HashMap<String, Object>();

		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),driverMasterPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(driverMasterPO.getUserId());
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
        	 
        if(driverMasterPO.getFirstName()==null||driverMasterPO.getFirstName().equals("")||driverMasterPO.getFirstName().isEmpty())
        {temp.append("::Driver name cannot be empty");
	      status="Fail";        	
        }
        else{
    	CharSequence driverName = driverMasterPO.getFirstName();
		Matcher name= Validator.alphaSpaceDot(driverName);
		if(!name.matches())		 
	     {temp.append("::Driver name should contain only characters");
	      status="Fail";
	     }}
		
        
        
        if(driverMasterPO.getMobileNumber()==null||driverMasterPO.getMobileNumber().equals("")||driverMasterPO.getMobileNumber().isEmpty())
        {temp.append("::Driver number cannot be empty");
	      status="Fail";        	
        }
        else{
		CharSequence driverMobile = driverMasterPO.getMobileNumber();
		Matcher mobile= Validator.mobNumber(driverMobile);
		if(!mobile.matches())		 
	     {temp.append("::Driver mobile number should contain only digits range between 6-18");
	      status="Fail";
	     }}
		
        if(driverMasterPO.getBatchNumber()==null||driverMasterPO.getBatchNumber().equals("")||driverMasterPO.getBatchNumber().isEmpty())
        {temp.append("::Driver batch number cannot be empty");
	      status="Fail";}
        else{
		CharSequence batchNumber = driverMasterPO.getBatchNumber();
		Matcher batch= Validator.alphaNumeric(batchNumber);
		if(!batch.matches())		 
	     {temp.append("::Driver batch number should contain characters and digits only");
	      status="Fail";
	     }}
		
        
        if(driverMasterPO.getLicenceNumber()==null||driverMasterPO.getLicenceNumber().equals("")||driverMasterPO.getLicenceNumber().isEmpty())
        {temp.append("::Driver license number cannot be empty");
	      status="Fail";        	
        }
        else{
		CharSequence licenseNo = driverMasterPO.getLicenceNumber();
		Matcher license= Validator.alphaNumeric(licenseNo);
		if(!license.matches())		 
	     {temp.append("::Driver License number should contain characters and digits only");
	      status="Fail";
	     }}
		
		if(driverMasterPO.getDob()==null)
		{temp.append("::Driver Date of Birth should not be empty ");
		status="Fail";
		}
		
		if(driverMasterPO.getDdtValidDate()==null)
		{temp.append("::Driver DDT should not be empty ");
		status="Fail";
		}
		
		if(driverMasterPO.getLicenceValid()==null)
		{temp.append("::Driver License valid date should not be empty ");
		status="Fail";
		}
		
		if(driverMasterPO.getBatchDate()==null)
		{temp.append("::Driver Batch valid date should not be empty ");
		status="Fail";
		}
		
		if(driverMasterPO.getPoliceVerificationValid()==null)
		{temp.append("::Driver PoliceVerification valid date should not be empty ");
		status="Fail";
		}
		
		if(driverMasterPO.getMedicalFitnessCertificateValid()==null)
		{temp.append("::Driver medical Fitness valid date should not be empty ");
		status="Fail";
		}
		
		if(driverMasterPO.getDriverAntiExpiry()==null)
		{temp.append("::Driver Anti expiry date should not be empty ");
		status="Fail";
		}
		
		if(driverMasterPO.getDateOfJoining()==null)
		{temp.append("::Driver Joining date should not be empty ");
		status="Fail";
		}
		if(driverMasterPO.getBadgeValidity()==null)
		{temp.append("::Driver Badge validity date should not be empty ");
		status="Fail";
		}
		
		if(driverMasterPO.getAddress()==null||driverMasterPO.getAddress().isEmpty()||driverMasterPO.getAddress().equals("")||driverMasterPO.getAddress().length()<10 ||driverMasterPO.getAddress().length()>250)
		{temp.append("::Driver address should not be less than 10 and more than 250 characters");
		status="Fail";
		}
		
		if(driverMasterPO.getPermanentAddress()==null||driverMasterPO.getPermanentAddress().isEmpty()||driverMasterPO.getPermanentAddress().equals("")||driverMasterPO.getPermanentAddress().length()<10 ||driverMasterPO.getPermanentAddress().length()>250)
		{temp.append("::Driver Permanent address should not be less than 10 and more than 250 characters");
		status="Fail";
		}
		if(status.equals("Fail"))
		{log.info("Invalid input:");
		requests.put("inputInvalid", temp);
	    return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	   	}
		 
         }
         log.info("valid input:");
 		List<EFmFmVendorMasterPO> vendorDetails=iVendorDetailsBO.getAllVendorName(driverMasterPO.getEfmFmVendorMaster().getVendorName(),driverMasterPO.getCombinedFacility());
		try{
        //Mobile number check need to add
		List<EFmFmDriverMasterPO> mobileNoExist=iRouteDetailBO.getValidMobileNumberCheck(driverMasterPO.getMobileNumber(),vendorDetails.get(0).getVendorId(),driverMasterPO.getCombinedFacility());	
	 	if(!(mobileNoExist.isEmpty())){
			requests.put("status", "mexist");
			log.info("serviceEnd -UserId :" + driverMasterPO.getUserId());
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	 	}
		
	 	//Licence check is dere
	 	List<EFmFmDriverMasterPO> licenseNoExist=iRouteDetailBO.getValidLicenseNumber(driverMasterPO.getLicenceNumber(),vendorDetails.get(0).getVendorId(),driverMasterPO.getCombinedFacility());	
	 	if(!(licenseNoExist.isEmpty())){
			requests.put("status", "lexist");
			log.info("serviceEnd -UserId :" + driverMasterPO.getUserId());
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	 	}
			driverMasterPO.setStatus("P");
			driverMasterPO.setGender("MALE");
			driverMasterPO.setMobileNumber(driverMasterPO.getMobileNumber());
		 	driverMasterPO.setMedicalFitnessCertificateIssued(new Date());
		 	driverMasterPO.setPoliceVerification("Done");	
		 	if(!(vendorDetails.isEmpty())){			 		
	 			driverMasterPO.setEfmFmVendorMaster(vendorDetails.get(0));
	 	}
			iRouteDetailBO.saveDriverRecord(driverMasterPO);			
			 try {
                 Thread thread1 = new Thread(new Runnable() {
                     @Override
 					public void run() {
                         SendMailBySite mailSender=new SendMailBySite();
                         String toMailId = ContextLoader.getContext().getMessage("user.toMailSender", null, "toMailSender", null);
                         mailSender.mailForNewDriverRegistration(toMailId, "team", driverMasterPO.getFirstName());
                     	}
                 });
                 thread1.start();
             } catch (Exception e) {
                 log.info("driver registration via form" + e);
             } 
			
			requests.put("status", "success");	
		}catch(Exception e){
			requests.put("status", "fail");	
			log.info("serviceEnd -UserId :" + driverMasterPO.getUserId());
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmDriverMasterPO> mobileNoExist=iRouteDetailBO.getValidMobileNumberCheck(driverMasterPO.getMobileNumber(),vendorDetails.get(0).getVendorId(),driverMasterPO.getCombinedFacility());	
	 	if(!(mobileNoExist.isEmpty())){
	 		requests.put("driverId", mobileNoExist.get(0).getDriverId());
	 		requests.put("driverName",  mobileNoExist.get(0).getFirstName());
	 		requests.put("mobileNumber",  mobileNoExist.get(0).getMobileNumber());
	 		requests.put("address",  mobileNoExist.get(0).getAddress());
	 		requests.put("licenceNumber",  mobileNoExist.get(0).getLicenceNumber());
	 		requests.put("licenceExp",  formatter.format(mobileNoExist.get(0).getLicenceValid()));
	 		requests.put("medicalExp",  formatter.format(mobileNoExist.get(0).getMedicalFitnessCertificateValid()));
	 		log.info("serviceEnd -UserId :" + driverMasterPO.getUserId());
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	 	}
		
		 log.info("serviceEnd -UserId :" + driverMasterPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();

	}	
	/*
	 * @xl utility row values are inserted into efmfm_driver_master table table. 
	 * validation also be handle here.
	 * 
	 */
	private List<Map<String, Object>> driverRecord(Map<Integer, Object> driverRecordDetails,int branchId,int userId,String combinedFacility) throws ParseException {				
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
		log.info("driverRecord");
		 List<Map<String, Object>> driverMasterExcel = new ArrayList<Map<String, Object>>();
		 Map<String, Object> issueList = new HashMap<String, Object>();
		 String status="";
		try {			
			for (Entry<Integer, Object> entry : driverRecordDetails.entrySet()) {
				issueList = new HashMap<String, Object>();                
				ArrayList driverExcelDetails = (ArrayList) entry.getValue();
			 	EFmFmDriverMasterPO driverMasterPO = new EFmFmDriverMasterPO();
			 	
			 if(driverExcelDetails.get(0).toString().toUpperCase()!=null && !driverExcelDetails.get(0).toString().toUpperCase().isEmpty() && driverExcelDetails.get(0).toString().toUpperCase()!=""){
			 	driverMasterPO.setFirstName(driverExcelDetails.get(1).toString());			 	
			 	if(driverExcelDetails.get(2).toString().equalsIgnoreCase("CELL_TYPE_BLANK"))             	
			 		driverMasterPO.setMiddleName("");
			 	else
			 		driverMasterPO.setMiddleName(driverExcelDetails.get(2).toString());    	
			 	if(driverExcelDetails.get(3).toString().equalsIgnoreCase("CELL_TYPE_BLANK"))	 		
			 		driverMasterPO.setLastName("");	
    			else			 				 	
			 	driverMasterPO.setLastName(driverExcelDetails.get(3).toString());			 	
			 	driverMasterPO.setFatherName(driverExcelDetails.get(4).toString());
			 	driverMasterPO.setGender(driverExcelDetails.get(5).toString());			 	
			 	driverMasterPO.setMobileNumber(driverExcelDetails.get(6).toString());			 	
			 	driverMasterPO.setLicenceNumber(driverExcelDetails.get(7).toString());			 	
			 	/*driverMasterPO.setLicenceIssued((Date)dateFormat.parse(driverExcelDetails.get(8).toString()));*/
			 	driverMasterPO.setLicenceValid(dateFormat.parse(driverExcelDetails.get(8).toString()));
			 /*	driverMasterPO.setMedicalFitnessCertificateIssued((Date)dateFormat.parse(driverExcelDetails.get(10).toString()));*/
			 	driverMasterPO.setMedicalFitnessCertificateValid(dateFormat.parse(driverExcelDetails.get(9).toString()));
			 	driverMasterPO.setPoliceVerification(driverExcelDetails.get(10).toString());
			 	driverMasterPO.setStateName(driverExcelDetails.get(11).toString());
			 	driverMasterPO.setCityName(driverExcelDetails.get(12).toString());
			 	driverMasterPO.setPinCode(Integer.parseInt(driverExcelDetails.get(13).toString()));				 	
			 	driverMasterPO.setAddress(driverExcelDetails.get(14).toString());			 	
				driverMasterPO.setPermanentAddress(driverExcelDetails.get(15).toString());
				driverMasterPO.setBatchNumber(driverExcelDetails.get(16).toString());
				driverMasterPO.setBatchDate(dateFormat.parse(driverExcelDetails.get(17).toString()));
				driverMasterPO.setBadgeValidity(dateFormat.parse(driverExcelDetails.get(18).toString()));								
			 	driverMasterPO.setStatus("P");
			 	driverMasterPO.setDateOfJoining(dateFormat.parse(driverExcelDetails.get(19).toString()));
			 	//driverMasterPO.setBatchDate(new Date());
			 	driverMasterPO.setDdtValidDate(new Date());
			 	driverMasterPO.setPoliceVerificationValid(new Date());
			 	driverMasterPO.setDob(new Date());		 		 	
				List<EFmFmVendorMasterPO> vendorDetails=iVendorDetailsBO.getAllVendorName(driverExcelDetails.get(0).toString().toUpperCase(),combinedFacility);
			 	if(!(vendorDetails.isEmpty())){			 		
			 			driverMasterPO.setEfmFmVendorMaster(vendorDetails.get(0));
			 		//Driver Record already existing on table.
			 		if(!driverMasterPO.getLicenceNumber().isEmpty() && driverMasterPO.getLicenceNumber() !="" && !driverMasterPO.getMobileNumber().isEmpty() && driverMasterPO.getMobileNumber() !="" ){
					 	List<EFmFmDriverMasterPO> licenseNoExist=iRouteDetailBO.getValidLicenseNumber(driverMasterPO.getLicenceNumber().trim().toUpperCase(),vendorDetails.get(0).getVendorId(),combinedFacility);				
						if(licenseNoExist.isEmpty()){
							List<EFmFmDriverMasterPO> mobileNoExist=iRouteDetailBO.getValidMobileNumberCheck(driverMasterPO.getMobileNumber().toString().trim(),vendorDetails.get(0).getVendorId(),combinedFacility);				
							if(mobileNoExist.isEmpty()){
								status = "success-";
								List<EFmFmDriverMasterPO> badgeNoExist=iRouteDetailBO.getValidUniqueBatchNumber(driverExcelDetails.get(16).toString().trim(),vendorDetails.get(0).getVendorId(),combinedFacility);				
								if(badgeNoExist.isEmpty()){
									iRouteDetailBO.saveDriverRecord(driverMasterPO);
									 try {
						                 Thread thread1 = new Thread(new Runnable() {
						                     @Override
						 					public void run() {
						                         SendMailBySite mailSender=new SendMailBySite();
						                         String toMailId = ContextLoader.getContext().getMessage("user.toMailSender", null, "toMailSender", null);
						                         mailSender.mailForNewDriverRegistration(toMailId, "team", driverMasterPO.getFirstName());
						                     	}
						                 });
						                 thread1.start();
						             } catch (Exception e) {
						                 log.info("driver registration via Excel" + e);
						             } 
									
									
									
								}else{
									status = "BadgeNumberExist-"+driverExcelDetails.get(16).toString();								
									issueList.put("RNo","");
									issueList.put("IssueStatus",status);
									driverMasterExcel.add(issueList);
								}
							}
							else{
								status = "mobileNumberExist-"+driverExcelDetails.get(6).toString();								
								issueList.put("RNo","");
								issueList.put("IssueStatus",status);
								driverMasterExcel.add(issueList);
								return driverMasterExcel;
							}
						}
						else{
							status = "licenceExist-"+driverExcelDetails.get(7).toString();
							issueList.put("RNo","");
							issueList.put("IssueStatus",status);
							driverMasterExcel.add(issueList);
							 return driverMasterExcel;
						}
					}		 		
			 		else{
						status = "fail-";
						issueList.put("RNo","");
						issueList.put("IssueStatus",status);
						driverMasterExcel.add(issueList);
						 return driverMasterExcel;
						
					}
			 		
			 	}
			 	else{
					status = "vendorNotExist-" +driverExcelDetails.get(0).toString();
					issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					driverMasterExcel.add(issueList);
					 return driverMasterExcel;
				}
			 	}
			 else{
					status = "fail-";
					issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					driverMasterExcel.add(issueList);
					 return driverMasterExcel;
				}
				
         }
		}
         catch(Exception e){
        	 log.info("Exception"+e);
         }	
		 return driverMasterExcel;
		
	}
	
	
	/**
	* upload documents for driver.
	*    
	*
	* @author  Sarfraz Khan 
	* 
	* @since   2015-06-20
	* 
	* @return uploadFile
	*/	

	@POST
	@Path("/documentupload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	//@Produces("application/json")		
   public Response uploadFile(
				@FormDataParam("filename") InputStream uploadedInputStream,
				@FormDataParam("filename") FormDataContentDisposition fileDetail,
				@FormDataParam("filename") InputStream uploadedSizeInputStream,
				@FormDataParam("filename") InputStream uploadedImageInputStream,
				@QueryParam("branchId") int branchId,
				@QueryParam("driverId") int driverId,
				@QueryParam("typeOfUploadDoc") String typeOfUploadDoc,
				@Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException {
		VehicleImportExcel vehicleService=new VehicleImportExcel();
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" +userId);

		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),userId))){
				log.info("inside if");
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
		EFmFmDriverMasterPO driverDetails=approvalBO.getParticularDriverDetail(driverId);		
		String status="";
		String name="os.name",filePath="";		 	
		boolean  OsName =System.getProperty(name).startsWith("Windows");
		EFmFmDriverDocsPO eFmFmDriverDocsPO=new EFmFmDriverDocsPO();		
		if(OsName){			
			filePath =SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER+ fileDetail.getFileName();			
			File pathExist = new File(SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER);			
			if(!pathExist.exists()){
				new File(SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER).mkdir();
			}			
		}else{
			filePath =SERVER_UPLOAD_LINUX_LOCATION_FOLDER+ fileDetail.getFileName();
			File pathExist = new File(SERVER_UPLOAD_LINUX_LOCATION_FOLDER);			
			if(!pathExist.exists()){
				new File(SERVER_UPLOAD_LINUX_LOCATION_FOLDER).mkdir();
			}
		}	
		eFmFmDriverDocsPO.setStatus("Y");
		eFmFmDriverDocsPO.setCreationTime(new Date());
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
        if(megabytes > (driverDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getImageUploadSize())){
        	log.info("Inside bigSize");
			status="bigSize";
			return Response.ok(status, MediaType.APPLICATION_JSON).build();
        }	  
		}
		log.info("success :"+filePath);
		File fileExist = new File(filePath);	
		if(!fileExist.exists() && !fileExist.isDirectory()){	
	  		log.info("success :");
			status="success";
			vehicleService.saveFile(uploadedInputStream, filePath);
		}else{
	  		log.info("exist :");
			status="exist";
			return Response.ok(status, MediaType.APPLICATION_JSON).build();
		}
		eFmFmDriverDocsPO.setUploadpath(filePath);
		eFmFmDriverDocsPO.setDocumentName(typeOfUploadDoc);
		
		if(typeOfUploadDoc.equalsIgnoreCase("License")){
		driverDetails.setLicenceDocPath(filePath);	
		}
		else if(typeOfUploadDoc.equalsIgnoreCase("medical")){
			driverDetails.setMedicalDocPath(filePath);			
		}
		else if(typeOfUploadDoc.equalsIgnoreCase("profilePic")){
			driverDetails.setProfilePicPath(filePath);			
		}
		
		eFmFmDriverDocsPO.setEfmFmDriverMaster(driverDetails);
		
		approvalBO.update(driverDetails);
		approvalBO.addUploadDetails(eFmFmDriverDocsPO);
		log.info("serviceEnd -UserId :" +userId);
		return Response.ok(status, MediaType.APPLICATION_JSON).build();
	}
	
	/*
	 * 
	 * Upload Document Details
	 */	
	@POST
	@Path("/listOfDocumentDetails")
	public Response listOfDocumentDetails(EFmFmDriverMasterPO eFmFmDriverMasterPO){		
		IApprovalBO iApprovalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDriverMasterPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDriverMasterPO.getUserId());
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
		 log.info("serviceStart -UserId :" + eFmFmDriverMasterPO.getUserId());
		List<Map<String, Object>> listOfDriverDetails= new ArrayList<Map<String, Object>>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		List<EFmFmDriverDocsPO> listOfDriver=iApprovalBO.getAlluploadFileDetails(eFmFmDriverMasterPO.getDriverId());		 
		if(!(listOfDriver.isEmpty())){				
			for(EFmFmDriverDocsPO driverDetails:listOfDriver){				
					Map<String, Object>  driverList= new HashMap<String, Object>();					
					driverList.put("driverId",driverDetails.getEfmFmDriverMaster().getDriverId());
					driverList.put("creationDate",dateFormat.format(driverDetails.getCreationTime()));
					driverList.put("documentName",driverDetails.getDocumentName());
					driverList.put("driverDocId",driverDetails.getDriverDocId());
					driverList.put("pathName",driverDetails.getUploadpath()
		                    .substring(driverDetails.getUploadpath().indexOf("upload") - 1));				
					listOfDriverDetails.add(driverList);					
				}			
			}	
		 log.info("serviceEnd -UserId :" + eFmFmDriverMasterPO.getUserId());
		return Response.ok(listOfDriverDetails, MediaType.APPLICATION_JSON).build();
	}
	
	public void clickToCallDriver(String GET_URL) throws IOException{
		final String USER_AGENT = "Mozilla/5.0";        
		        URL obj = new URL(GET_URL);
		        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		        con.setRequestMethod("GET");
		        con.setRequestProperty("User-Agent", USER_AGENT);
		        int responseCode = con.getResponseCode();
		        StringBuffer response = new StringBuffer();
		        if (responseCode == HttpURLConnection.HTTP_OK) { // success
		            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		            String inputLine;
		            while ((inputLine = in.readLine()) != null) {
		                response.append(inputLine);
		            }
		            in.close();
		            System.out.println("Message solutionsinfini Response From GateWay: " + response.toString() + " for Mobile: "
		                    + response.toString()+"responseCode"+responseCode);
		        } else {
		        	System.out.println(response.toString()+"responseCode"+responseCode);
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
