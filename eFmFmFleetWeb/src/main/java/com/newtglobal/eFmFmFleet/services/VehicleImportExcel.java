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

import com.google.zxing.WriterException;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.QRCodeGenarator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Validator;
import com.newtglobal.eFmFmFleet.model.EFmFmFixedDistanceContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleInspectionPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/xlUtilityVehicleUpload")
public class VehicleImportExcel {

    private static final String SERVER_UPLOAD_LINUX_LOCATION_FOLDER  = ContextLoader.getContext().getMessage("upload.docsLinux", null, "docsLinux", null);
    private static final String SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER  = ContextLoader.getContext().getMessage("upload.docsWindows", null, "docsWindows", null);

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();


	/*
	 * @Reading Driver details from vehicle_master xl utility.
	 * 
	 * @Stored all the values on Arraylist.
	 */
	private static Log log = LogFactory.getLog(VehicleImportExcel.class);

	@POST
	@Path("/vehicleRecord")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	  public Response upload(@FormDataParam("filename") InputStream uploadedInputStream,
			  @FormDataParam("filename") InputStream uploadedSizeInputStream,
	            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
	            @QueryParam("combinedFacility") String combinedFacility,
	            @Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException {
	   	 String status = "success-";
	   	log.info("serviceStart -UserId :" +userId);
	   	log.info("serviceStart -UserId :" +fileDetail.getFileName());
	   	 int noOfcolumn=18; 
	        List<Map<String, Object>> response =new ArrayList<Map<String, Object>>();
	       	Map<String, Object>  vendorReqExcel= new HashMap<String, Object>(); 
	       	Map<Integer, Object>  vendorExcelDetails= new HashMap<Integer, Object>();

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
	           System.out.println(sheet.getLastRowNum());
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
		                		if(!vehicleMasterExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,userId,combinedFacility).isEmpty()){                		
		                			response.addAll(vehicleMasterExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,userId,combinedFacility));
		                		}else{
		                			vendorExcelDetails.put(rowId, columnValue);
		                		}
	                		}else{
	                				status="Kinldy check the no -of Column :18";	                				
	                				vendorReqExcel.put("RNo",Integer.toString(rowId));
	                				vendorReqExcel.put("IssueStatus",status);
		                    		response.add(vendorReqExcel);
	                	}              		
	                	}else{
	                		  if(columnValue.size()<18){                			              			
	                			       			
	                			  	status="Kinldy fill the all the Column's";
	                			  	vendorReqExcel.put("RNo",Integer.toString(rowId));
	                				vendorReqExcel.put("IssueStatus",status);
	  	                    		response.add(vendorReqExcel);
	  	                    	
	                		}else{
	                			//Need to configure below Text some where
	                			if(!"VehicleNumber".equalsIgnoreCase(columnValue.get(0).toString().replace(" ","").trim()) 
	                					|| !"RegistrationCertificateNumber".equalsIgnoreCase(columnValue.get(1).toString().replace(" ","").trim()) 
	                					|| !"Capacity".equalsIgnoreCase(columnValue.get(2).toString().replace(" ","").trim()) 
	                					|| !"VehicleEngineNumber".equalsIgnoreCase(columnValue.get(3).toString().replace(" ","").trim())
	                					|| !"VehicleMake".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim())
	                					|| !"VehicleModel".equalsIgnoreCase(columnValue.get(5).toString().replace(" ","").trim()) 
	                        			|| !"VendorName".equalsIgnoreCase(columnValue.get(6).toString().replace(" ","").trim()) 
	                        			|| !"Taxcertificatevalidity".equalsIgnoreCase(columnValue.get(7).toString().replace(" ","").trim()) 
	                        			|| !"Permitvalidity".equalsIgnoreCase(columnValue.get(8).toString().replace(" ","").trim()) 
	                        			|| !"Polutionvalidity".equalsIgnoreCase(columnValue.get(9).toString().replace(" ","").trim())	                        			
	                        			|| !"Insurancevalidity".equalsIgnoreCase(columnValue.get(10).toString().replace(" ","").trim()) 
	                					|| !"ModelYear".equalsIgnoreCase(columnValue.get(11).toString().replace(" ","").trim())
	                					|| !"Remarks".equalsIgnoreCase(columnValue.get(12).toString().replace(" ","").trim())
	                					|| !"VehicleAC/NONAC".equalsIgnoreCase(columnValue.get(13).toString().replace(" ","").trim()) 
	                        			|| !"VehicleGPSFitted".equalsIgnoreCase(columnValue.get(14).toString().replace(" ","").trim()) 
	                        			|| !"RFIDFitted".equalsIgnoreCase(columnValue.get(15).toString().replace(" ","").trim()) 
	                        			|| !"ContractType".equalsIgnoreCase(columnValue.get(16).toString().replace(" ","").trim()) 
	                        			|| !"ContractTariffID".equalsIgnoreCase(columnValue.get(17).toString().replace(" ","").trim())){    
	                				      
	                				    vendorReqExcel.put("RNo",Integer.toString(rowId));
		                				vendorReqExcel.put("IssueStatus","Kinldy check the Column Names & No of Column's ");
		  	                    		response.add(vendorReqExcel);
		  	                    		log.info("serviceStart -UserId :" +userId);
		  	                    		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	                			}
	                		}
	                	}
					}              
	            }
	            log.info("Size"+vendorExcelDetails.size());
				if(response.isEmpty()){
					if(!vendorExcelDetails.isEmpty()){
						response=vehicleRecord(vendorExcelDetails,branchId,userId,combinedFacility);	
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
	        log.info("serviceStart -UserId :" +userId);
	        return Response.ok(response, MediaType.APPLICATION_JSON).build();
	    }
	    private List<Map<String, Object>> vehicleMasterExcelValidator(String rowId,ArrayList<Object> columnValue,int branchId,int noOfcolumn,int userId,String combinedFacility){	    	
	    	IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");				   
		    IVendorDetailsBO iVendorMasterBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		    List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();		   
		    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		    String validInt="^[0-9]*$";
		    String alphanumaric="^[a-zA-Z0-9]*$";
		    String charAt="^[a-zA-Z]*$";
			
			List<EFmFmVendorMasterPO> venderNameExist = iVendorMasterBO.getAllVendorName(columnValue.get(6).toString().trim(),combinedFacility);
		    if (columnValue.get(6).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(6).toString() ==null || columnValue.get(6).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(6).toString().isEmpty()) {	
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Vendor Name : "+columnValue.get(6).toString();
					empReqExcelRows.put("RNo",rowId.concat(",6"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}else{
					if (venderNameExist.isEmpty()) {
						 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				    	String status = " Vendor Name is not available -" + columnValue.get(6).toString().trim();
				    	empReqExcelRows.put("RNo",rowId.concat(",6"));
						empReqExcelRows.put("IssueStatus",status);	
						childRowResponse.add(empReqExcelRows);
				    } 
					
				}	
			
		    
		    if (columnValue.get(0).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(0).toString() ==null || columnValue.get(0).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(0).toString().isEmpty() || !columnValue.get(0).toString().replace(" ", "").trim().matches(alphanumaric)) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Vehicle Number : "+columnValue.get(0).toString();
					empReqExcelRows.put("RNo",rowId.concat(",0"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}else{
					if(!(venderNameExist.isEmpty())){
					List<EFmFmVehicleMasterPO> vehicleNoExist = iVehicleCheckInBO.getParticularVehicleDetailsByVehicleNumber(columnValue.get(0).toString().trim(),venderNameExist.get(0).getVendorId(),combinedFacility);
	                 if (!vehicleNoExist.isEmpty()) {
	                	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
	                	 String status = " Vehicle Number already available -" + columnValue.get(0).toString().trim();
	                	 empReqExcelRows.put("RNo",rowId.concat(",0"));
	 					empReqExcelRows.put("IssueStatus",status);
	 					childRowResponse.add(empReqExcelRows);
				    } 
					}
				}				
				
		    if (columnValue.get(1).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(1).toString() ==null || columnValue.get(1).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(1).toString().isEmpty() || !columnValue.get(1).toString().replace(" ", "").trim().matches(alphanumaric)) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Registration Certificate Number : "+columnValue.get(1).toString();
					empReqExcelRows.put("RNo",rowId.concat(",1"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}else{	
					if(!(venderNameExist.isEmpty())){
					 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();				 
					List<EFmFmVehicleMasterPO> vehicleRcExist = iVehicleCheckInBO.getRcNumberDetails(columnValue.get(1).toString(), venderNameExist.get(0).getVendorId(),combinedFacility);
	                 if (!vehicleRcExist.isEmpty()) {
				    	String status = " Registration Certificate Number already available -" + columnValue.get(1).toString().trim();
				    	empReqExcelRows.put("RNo",rowId.concat(",1"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
				    } 
					}
				}
				
				
		    if (columnValue.get(2).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(2).toString() ==null || columnValue.get(2).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(2).toString().isEmpty() || !columnValue.get(2).toString().replace(" ", "").trim().matches(validInt)) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Capacity : "+columnValue.get(2).toString();
					empReqExcelRows.put("RNo",rowId.concat(",2"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}
				
				
		    if (columnValue.get(3).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(3).toString() ==null || columnValue.get(3).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(3).toString().isEmpty() || !columnValue.get(3).toString().replace(" ", "").trim().matches(alphanumaric)) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Vehicle Engine Number : "+columnValue.get(3).toString();
					empReqExcelRows.put("RNo",rowId.concat(",3"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}else{
					if(!(venderNameExist.isEmpty())){
					List<EFmFmVehicleMasterPO> vehicleEngineNoExist = iVehicleCheckInBO.getEngineNoDetails(columnValue.get(3).toString(), venderNameExist.get(0).getVendorId(),combinedFacility);
	                 if (!vehicleEngineNoExist.isEmpty()) {
	                	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				    	String status = "Vehicle Engine Number already available -" + columnValue.get(3).toString().trim();
				    	empReqExcelRows.put("RNo",rowId.concat(",3"));
						empReqExcelRows.put("IssueStatus",status);	
						childRowResponse.add(empReqExcelRows);
				    } 
				}
				}
				
		    if (columnValue.get(4).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(4).toString() ==null || columnValue.get(4).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(4).toString().isEmpty() || !columnValue.get(4).toString().replace(" ", "").trim().matches(alphanumaric)) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Vehicle Make: "+columnValue.get(4).toString();
					empReqExcelRows.put("RNo",rowId.concat(",4"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}
				
				
		    if (columnValue.get(5).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(5).toString() ==null || columnValue.get(5).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(5).toString().isEmpty() || !columnValue.get(5).toString().replace(" ", "").trim().matches(alphanumaric)) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Vehicle Model: "+columnValue.get(5).toString();
					empReqExcelRows.put("RNo",rowId.concat(",5"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}
				
		    if (columnValue.get(7).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(7).toString() ==null || columnValue.get(7).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(7).toString().isEmpty() ) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Tax certificate validity  : "+columnValue.get(7).toString();
					empReqExcelRows.put("RNo",rowId.concat(",7"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}else{
					 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					try{
						Date taxValidity=dateFormat.parse(columnValue.get(7).toString());			
						if(taxValidity.before(new Date())){
							
							String status="Tax certificate validity should not less than toDay Date : "+columnValue.get(7).toString();
							empReqExcelRows.put("RNo",rowId.concat(",7"));
							empReqExcelRows.put("IssueStatus",status);
							childRowResponse.add(empReqExcelRows);
						}				
						
					}catch(Exception e){
						String status="kindly check the Tax certificate validity formate   : "+e;
						empReqExcelRows.put("RNo",rowId.concat(",7"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
					}	
				}
				
				
				
		    if (columnValue.get(8).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(8).toString() ==null || columnValue.get(8).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(8).toString().isEmpty() ) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Permit  validity  : "+columnValue.get(8).toString();
					empReqExcelRows.put("RNo",rowId.concat(",8"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}else{	
					 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					try{
						Date permitValidity=dateFormat.parse(columnValue.get(8).toString());			
						if(permitValidity.before(new Date())){
							String status="Permit  validity should not less than toDay Date : "+columnValue.get(8).toString();
							empReqExcelRows.put("RNo",rowId.concat(",8"));
							empReqExcelRows.put("IssueStatus",status);
							childRowResponse.add(empReqExcelRows);
						}				
						
					}catch(Exception e){
						String status="kindly check the Permit  validity formate   : "+e;
						empReqExcelRows.put("RNo",rowId.concat(",8"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
					}	
				}
				
		    if (columnValue.get(9).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(9).toString() ==null || columnValue.get(9).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(9).toString().isEmpty() ) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Polution validity Date  : "+columnValue.get(9).toString();
					empReqExcelRows.put("RNo",rowId.concat(",9"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}else{	
					 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					try{
						Date polutionIssued=dateFormat.parse(columnValue.get(9).toString());
						System.out.println(new Date());
						if(polutionIssued.before(new Date())){
							String status="Polution validity Date should not less than toDay Date : "+columnValue.get(9).toString()+"todat date "+new Date();
							empReqExcelRows.put("RNo",rowId.concat(",9"));
							empReqExcelRows.put("IssueStatus",status);
							childRowResponse.add(empReqExcelRows);
						}				
						
					}catch(Exception e){
						String status="kindly check the Polution validity Date formate   : "+e;
						empReqExcelRows.put("RNo",rowId.concat(",9"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
					}	
				}
				
				
		    if (columnValue.get(10).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(10).toString() ==null || columnValue.get(10).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(10).toString().isEmpty() ) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Insurance validity  : "+columnValue.get(10).toString();
					empReqExcelRows.put("RNo",rowId.concat(",10"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}else{
					 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					try{
						Date permitValidity=dateFormat.parse(columnValue.get(10).toString().trim());			
						if(permitValidity.before(new Date())){
							String status="Insurance validity should not lessthan toDay Date : "+columnValue.get(10).toString();
							empReqExcelRows.put("RNo",rowId.concat(",10"));
							empReqExcelRows.put("IssueStatus",status);
							childRowResponse.add(empReqExcelRows);
						}		
					}catch(Exception e){
						String status="kindly check the Insurance validity formate   : "+e;
						empReqExcelRows.put("RNo",rowId.concat(",10"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
					}	
				}
				
				
		    if (columnValue.get(11).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(11).toString() ==null || columnValue.get(11).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(11).toString().isEmpty() || !columnValue.get(11).toString().replace(" ", "").trim().matches(validInt)) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Model Year : "+columnValue.get(11).toString();
					empReqExcelRows.put("RNo",rowId.concat(",11"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}
				

		    if (columnValue.get(12).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(12).toString() ==null || columnValue.get(12).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(12).toString().isEmpty() ) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Remarks : "+columnValue.get(12).toString();
					empReqExcelRows.put("RNo",rowId.concat(",12"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}
				
				
				
		    if (columnValue.get(13).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(13).toString() ==null || columnValue.get(13).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(13).toString().isEmpty() || !columnValue.get(13).toString().replace(" ", "").trim().matches(charAt)) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Vehicle AC/NON AC : "+columnValue.get(13).toString();
					empReqExcelRows.put("RNo",rowId.concat(",13"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}
				
		    if (columnValue.get(14).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(14).toString() ==null || columnValue.get(14).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(14).toString().isEmpty() || !columnValue.get(14).toString().replace(" ", "").trim().matches(charAt)) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Vehicle GPS Fitted : "+columnValue.get(14).toString();
					empReqExcelRows.put("RNo",rowId.concat(",14"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}
				
				
				
				
		    if (columnValue.get(15).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(15).toString() ==null || columnValue.get(15).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(15).toString().isEmpty() || !columnValue.get(15).toString().replace(" ", "").trim().matches(charAt)) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the RFID Fitted : "+columnValue.get(15).toString();
					empReqExcelRows.put("RNo",rowId.concat(",15"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}
				
		    if (columnValue.get(16).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(16).toString() ==null || columnValue.get(16).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(16).toString().isEmpty() || !columnValue.get(16).toString().replace(" ", "").trim().matches(charAt)) {
		    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Contract Type : "+columnValue.get(16).toString();
					empReqExcelRows.put("RNo",rowId.concat(",16"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}else{
					List<EFmFmVendorContractTypeMasterPO> contractType = iVehicleCheckInBO.getContractTypeDetails(columnValue.get(16).toString(),branchId);
	                 if (contractType.isEmpty()) {
	                	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				    	String status = "Contract Typp not available on available -" + columnValue.get(16).toString().trim();
				    	empReqExcelRows.put("RNo",rowId.concat(",16"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
				    } 
				}
		    if (columnValue.get(17).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(17).toString() ==null || columnValue.get(17).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(17).toString().isEmpty() || !columnValue.get(17).toString().replace(" ", "").trim().matches(validInt)) {
		    	 	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Contract Tariff ID : "+columnValue.get(17).toString();
					empReqExcelRows.put("RNo",rowId.concat(",17"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}else{
					//validate
					List<EFmFmFixedDistanceContractDetailPO> contractDetails = iVehicleCheckInBO.getFixedDistanceDetails(Integer.parseInt(columnValue.get(17).toString().trim()),branchId);
					if(contractDetails.isEmpty()){
						Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
						String status="kindly Enter Valid Contract Tariff Id: "+columnValue.get(17).toString();
						empReqExcelRows.put("RNo",rowId.concat(",17"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
					}
				}
				return childRowResponse;
		
	}

	/*
	 * Add new vehicle here we can add new vehicle one bye one
	 */

	@POST
	@Path("/addnewvehicle")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addNewVehicle(EFmFmVehicleMasterPO vehicleMasterPO) throws WriterException, IOException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		log.info(vehicleMasterPO.getContractDetailId()+"serviceStart -UserId :" + vehicleMasterPO.getUserId());
		IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
    	Map<String, Object> requests = new HashMap<String, Object>();
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vehicleMasterPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(vehicleMasterPO.getUserId());
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
        	if(vehicleMasterPO.getVehicleMake()==null||vehicleMasterPO.getVehicleMake().isEmpty())
        	{temp.append("::Vehicle make cannot be empty");
  	      status="Fail";        		
        	}
        	else{
    	CharSequence vehicleMake = vehicleMasterPO.getVehicleMake();
		Matcher vMake= Validator.alphaNumSpecialCharacers(vehicleMake);
		if(!vMake.matches()||vehicleMasterPO.getVehicleMake().length()>20)		 
	     {temp.append("::Vehicle make should not be more than 20 letters");
	      status="Fail";
	     }}
       if(vehicleMasterPO.getVehicleModel()==null||vehicleMasterPO.getVehicleModel().isEmpty())
        	{temp.append("::Vehicle model cannot be empty");
      	      status="Fail";
        	}
        	else{	
        		CharSequence vehicleModel = vehicleMasterPO.getVehicleModel();
		Matcher vModel= Validator.alphaNumSpecialCharacers(vehicleModel);
		if(!vModel.matches()||vehicleMasterPO.getVehicleModel().length()>20)		 
	     {temp.append("::Vehicle model cannot be more than 30 letters");
	      status="Fail";
	     }}
        	
        	
        	
		if(vehicleMasterPO.getVehicleModelYear()==null||vehicleMasterPO.getVehicleModelYear().equals(""))
		{temp.append("::Vehicle model year cannot be empty");
	      status="Fail";			
		}
		else{
		CharSequence modelYear = vehicleMasterPO.getVehicleModelYear();
		Matcher vModelYear= Validator.dateYear(modelYear);
		if(!vModelYear.matches())		 
	     {temp.append("::Vehicle model year should be in year format");
	      status="Fail";
	     }
		}
		
		if(vehicleMasterPO.getVehicleNumber()==null||vehicleMasterPO.getVehicleNumber().isEmpty())
		{temp.append("::Vehicle  number cannot be empty");
	      status="Fail";			
		}
		else{
		CharSequence vehicleNum = vehicleMasterPO.getVehicleNumber();
		Matcher vehicleNumber= Validator.alphaNumeric(vehicleNum);
		if(!vehicleNumber.matches()||vehicleMasterPO.getVehicleNumber().length()>20)		 
	     {temp.append("::Vehicle number can be alpha numberic and not more than 20 letters");
	      status="Fail";
	     }}
		
		
		
		if(vehicleMasterPO.getVehicleEngineNumber()==null||vehicleMasterPO.getVehicleEngineNumber().isEmpty())
		{temp.append("::Vehicle Engine number cannot be empty");
	      status="Fail";			
		}
		else{
		CharSequence vehicleEngNumber = vehicleMasterPO.getVehicleEngineNumber();
		Matcher vehicleEngNo= Validator.alphaNumeric(vehicleEngNumber);
		if(!vehicleEngNo.matches()||vehicleMasterPO.getVehicleEngineNumber().length()>15)		 
	     {temp.append("::Vehicle Engine number can be alpha numberic and not more than 15 letters");
	      status="Fail";
	     }}
		
		if(vehicleMasterPO.getCapacity()<=0||vehicleMasterPO.getCapacity()>99)
		{temp.append("::Vehicle capacity is mandatory and should be positive number, max range 99");
	      status="Fail";			
		}
		
		
		if(vehicleMasterPO.getRegistartionCertificateNumber()==null||vehicleMasterPO.getRegistartionCertificateNumber().equals(""))
		{temp.append("::Vehicle RegistrationCertificate number cannot be empty");
	      status="Fail";			
		}
		else{
		CharSequence VehRegistrationNo = vehicleMasterPO.getRegistartionCertificateNumber();
		Matcher vRegNum= Validator.alphaNumSpecialCharacers(VehRegistrationNo);
		if(!vRegNum.matches()||vehicleMasterPO.getRegistartionCertificateNumber().length()>30)		 
	     {temp.append("::Vehicle Registration Certification number can be alphanumeric and not more than 30 letters");
	      status="Fail";
	     }
		}
		
		if(vehicleMasterPO.getVehicleOwnerName()==null||vehicleMasterPO.getVehicleOwnerName().equals(""))
		{temp.append("::Vehicle contact name cannot be empty");
	      status="Fail";			
		}
		CharSequence VehicleContactPerson = vehicleMasterPO.getVehicleOwnerName();
		Matcher vehcontactPerson= Validator.alphaSpaceDot(VehicleContactPerson);
		if(!vehcontactPerson.matches()||vehicleMasterPO.getVehicleOwnerName().length()<3||vehicleMasterPO.getVehicleOwnerName().length()>20)		 
	     {temp.append("::Vehicle contact name can have only characers with space and dot, range min 3 max 20");
	      status="Fail";
	     }
		
		
		/*CharSequence vehContractType = vehicleMasterPO.getContractType();
		Matcher contractType= Validator.alphaNumeric(vehContractType);
		if(!contractType.matches()||vehicleMasterPO.getContractType().length()>10)		 
	     {temp.append("::Vehicle contract Type can have only characters with max 10 letters");
	      status="Fail";
	     }*/
		
		if(vehicleMasterPO.getContractDetailId()<=0)
		{temp.append("::Vehicle contract TarrifID can be only positive number ");
	      status="Fail";			
		}
		else{
			int contractId=vehicleMasterPO.getContractDetailId();
			String contract=Integer.toString(contractId);
			if(contract.length()>3)
			{temp.append("::Vehicle contract TarrifID can be max 3 digits");
		      status="Fail";}
		}
		
		
				
		if(vehicleMasterPO.getRegistrationDate()==null)
		{temp.append("Vehicle Registration Date cannot be empty");
		status="Fail";			
		}	
		
		if(vehicleMasterPO.getPolutionValid()==null)
		{temp.append("Vehicle Polution Expiry date Date cannot be empty");
		status="Fail";			
		}
		
		if(vehicleMasterPO.getInsuranceValidDate()==null)
		{temp.append("Vehicle Insurance Expiry date Date cannot be empty");
		status="Fail";			
		}
		
		if(vehicleMasterPO.getTaxCertificateValid()==null)
		{temp.append("Vehicle Tax Expiry date Date cannot be empty");
		status="Fail";			
		}
		
		if(vehicleMasterPO.getPermitValidDate()==null)
		{temp.append("Vehicle permit Date cannot be empty");
		status="Fail";			
		}
		
		if(vehicleMasterPO.getVehicleFitnessDate()==null)
		{temp.append("Vehicle Fitness Date cannot be empty");
		status="Fail";			
		}
		
		
		if(status.equals("Fail"))
		{
		log.info("Invalid input:");
		requests.put("inputInvalid", temp);
	    return Response.ok(requests, MediaType.APPLICATION_JSON).build();	   	}
         }
      log.info("valid input:");
      
		List<EFmFmVendorMasterPO> vendorDetails = iVendorDetailsBO.getAllVendorName(vehicleMasterPO.getEfmFmVendorMaster().getVendorName(),vehicleMasterPO.getCombinedFacility());
		System.out.println(vehicleMasterPO.getCombinedFacility()+"vendor detail"+vendorDetails.size());
		int branchId = vehicleMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId();
		// getVehicleNumber Record already existing on table.
		List<EFmFmVehicleMasterPO> vehicleNoExist = iVehicleCheckInBO
				.getParticularVehicleDetailsByVehicleNumber(vehicleMasterPO.getVehicleNumber(),vendorDetails.get(0).getVendorId(),vehicleMasterPO.getCombinedFacility());
		if (!(vehicleNoExist.isEmpty())) {
			requests.put("status", "vNumExist");
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();

		}
		// getVehicleEngineNumber Record already existing on table.
		List<EFmFmVehicleMasterPO> vehicleEngineNoExist = iVehicleCheckInBO
				.getEngineNoDetails(vehicleMasterPO.getVehicleEngineNumber().toUpperCase(),vendorDetails.get(0).getVendorId(),vehicleMasterPO.getCombinedFacility());
		if (!(vehicleEngineNoExist.isEmpty())) {
			requests.put("status", "vEngineNumExist");
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();

		}
		
		// getRegistartionCertificateNumber Record already existing on table.
		List<EFmFmVehicleMasterPO> vehicleRcExist = iVehicleCheckInBO
				.getRcNumberDetails(vehicleMasterPO.getRegistartionCertificateNumber(), vendorDetails.get(0).getVendorId(),
						vehicleMasterPO.getCombinedFacility());
		if (!(vehicleRcExist.isEmpty())) {
			requests.put("status", "vRcNumExist");
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
		}
		vehicleMasterPO.setrFIDFitted("N");
		vehicleMasterPO.setVehicleGPSFitted("N");
		vehicleMasterPO.setVehicleACFitted("Y");
		vehicleMasterPO.setReplaceMentVehicleNum("NO");
		if (!(vendorDetails.isEmpty())) {
			vehicleMasterPO.setEfmFmVendorMaster(vendorDetails.get(0));
		}
		iVehicleCheckInBO.save(vehicleMasterPO);
		requests.put("status", "success");
		List<EFmFmVehicleMasterPO> vehicleNumExist = iVehicleCheckInBO
				.getParticularVehicleDetailsByVehicleNumber(vehicleMasterPO.getVehicleNumber(),vendorDetails.get(0).getVendorId(),vehicleMasterPO.getCombinedFacility());
		if (!(vehicleNumExist.isEmpty())) {
			try {
				QRCodeGenarator qRCode = new QRCodeGenarator();
				qRCode.createQRCode(vehicleMasterPO.getVehicleNumber(), SERVER_UPLOAD_LINUX_LOCATION_FOLDER);
				EFmFmVehicleDocsPO eFmFmVehicleDocsPO = new EFmFmVehicleDocsPO();
				eFmFmVehicleDocsPO.setCreationTime(new Date());
				eFmFmVehicleDocsPO.setDocumentName(vehicleMasterPO.getVehicleNumber() + "QRCode.png");
				eFmFmVehicleDocsPO.setStatus("Y");
				eFmFmVehicleDocsPO.setUploadpath(
						SERVER_UPLOAD_LINUX_LOCATION_FOLDER + vehicleMasterPO.getVehicleNumber() + "QRCode.png");
				eFmFmVehicleDocsPO.seteFmFmVehicleMaster(vehicleNumExist.get(0));
				iVehicleCheckInBO.addVehicleUploadDetails(eFmFmVehicleDocsPO);
			} catch (Exception e) {
				log.info("QR Code Error:" + e);
			}
			requests.put("vehicleId", vehicleNumExist.get(0).getVehicleId());
			requests.put("vehicleNumber", vehicleNumExist.get(0).getVehicleNumber());
			requests.put("capacity", vehicleNumExist.get(0).getCapacity());
			requests.put("vendorName", vehicleNumExist.get(0).getEfmFmVendorMaster().getVendorName());
			requests.put("InsuranceDate", dateFormat.format(vehicleNumExist.get(0).getInsuranceValidDate()));
			requests.put("PermitValid", dateFormat.format(vehicleNumExist.get(0).getPermitValidDate()));
			requests.put("isReplacement", vehicleNumExist.get(0).getReplaceMentVehicleNum());
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();

		}
		 log.info("serviceEnd -UserId :" + vehicleMasterPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * @xl utility row values are inserted into efmfm_vehicle_master table
	 * table. validation also be handle here.
	 * 
	 */
	private List<Map<String, Object>> vehicleRecord(Map<Integer, Object> vehicleRecordDetails, int branchId,int userId,String combinedFacility) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String status = "";
		 List<Map<String, Object>> vehicleMasterExcel = new ArrayList<Map<String, Object>>();
		 Map<String, Object> issueList = new HashMap<String, Object>();
		try {
			
			for (Entry<Integer, Object> entry : vehicleRecordDetails.entrySet()) {
				issueList = new HashMap<String, Object>();                
				ArrayList vehicleExcelDetails = (ArrayList) entry.getValue();	
			
			EFmFmVehicleMasterPO vehicleMasterPO = new EFmFmVehicleMasterPO();
			if (vehicleExcelDetails.get(6).toString().toUpperCase() != null
					&& !vehicleExcelDetails.get(6).toString().toUpperCase().isEmpty()
					&& vehicleExcelDetails.get(6).toString().toUpperCase() != "") {
				vehicleMasterPO.setVehicleNumber(vehicleExcelDetails.get(0).toString().trim().replaceAll("\\s+", ""));
				vehicleMasterPO.setRegistartionCertificateNumber(vehicleExcelDetails.get(1).toString());
				vehicleMasterPO.setCapacity(Integer.parseInt(vehicleExcelDetails.get(2).toString()));
				vehicleMasterPO.setVehicleEngineNumber(vehicleExcelDetails.get(3).toString());
				vehicleMasterPO.setVehicleMake((vehicleExcelDetails.get(4).toString()));
				vehicleMasterPO.setVehicleModel((vehicleExcelDetails.get(5).toString()));
				vehicleMasterPO.setReplaceMentVehicleNum("NO");
				vehicleMasterPO.setTaxCertificateValid(dateFormat.parse(vehicleExcelDetails.get(7).toString()));
				vehicleMasterPO.setPermitValidDate(dateFormat.parse(vehicleExcelDetails.get(8).toString()));
				vehicleMasterPO.setPolutionValid(dateFormat.parse(vehicleExcelDetails.get(9).toString()));
				vehicleMasterPO.setInsuranceValidDate(dateFormat.parse(vehicleExcelDetails.get(10).toString()));
				vehicleMasterPO.setVehicleModelYear(vehicleExcelDetails.get(11).toString());
				vehicleMasterPO.setRemarks(vehicleExcelDetails.get(12).toString());
				vehicleMasterPO.setVehicleACFitted(vehicleExcelDetails.get(13).toString());
				vehicleMasterPO.setVehicleGPSFitted(vehicleExcelDetails.get(14).toString());
				vehicleMasterPO.setrFIDFitted(vehicleExcelDetails.get(15).toString());
				vehicleMasterPO.setUpdatedTime(new Date());
				vehicleMasterPO.setRegistrationDate(new Date());
				vehicleMasterPO.setVehicleFitnessDate(new Date());
				vehicleMasterPO.setStatus("P");
				List<EFmFmVendorMasterPO> vendorDetails = iVendorDetailsBO
						.getAllVendorName(vehicleExcelDetails.get(6).toString().toUpperCase(), combinedFacility);
				if (!(vendorDetails.isEmpty())) {
					vehicleMasterPO.setEfmFmVendorMaster(vendorDetails.get(0));
					if (vehicleMasterPO.getVehicleNumber() != "" && !vehicleMasterPO.getVehicleNumber().isEmpty()) {
						
						List<EFmFmVehicleMasterPO> vehicleNoExist = iVehicleCheckInBO
								.getParticularVehicleDetailsByVehicleNumber(vehicleMasterPO.getVehicleNumber(),vendorDetails.get(0).getVendorId(),
										combinedFacility);
						
						if (vehicleNoExist.isEmpty()) {
							List<EFmFmVehicleMasterPO> vehicleRcExist = iVehicleCheckInBO
									.getRcNumberDetails(vehicleMasterPO.getRegistartionCertificateNumber(), vendorDetails.get(0).getVendorId(),
											combinedFacility);
							if (vehicleRcExist.isEmpty()) {
								List<EFmFmVehicleMasterPO> vehicleEngineNoExist = iVehicleCheckInBO.getEngineNoDetails(
										vehicleMasterPO.getVehicleEngineNumber().toUpperCase(),vendorDetails.get(0).getVendorId(),
										combinedFacility);
								if (vehicleEngineNoExist.isEmpty()) {
									List<EFmFmVendorContractTypeMasterPO> contractType = iVehicleCheckInBO
											.getContractTypeDetails(vehicleExcelDetails.get(16).toString().toLowerCase().trim(),
													branchId);
									if (!(contractType.isEmpty())) {
										//vehicleMasterPO.seteFmFmVendorContractTypeMaster(contractType.get(0));
										EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO=new EFmFmFixedDistanceContractDetailPO();
										eFmFmFixedDistanceContractDetailPO.setDistanceContractId(Integer.parseInt(vehicleExcelDetails.get(17).toString().trim()));																	
										vehicleMasterPO.seteFmFmContractDetails(eFmFmFixedDistanceContractDetailPO);
										status = "success-";
										iVehicleCheckInBO.save(vehicleMasterPO);										
										List<EFmFmVehicleMasterPO> vehicleNumExist = iVehicleCheckInBO
												.getParticularVehicleDetailsByVehicleNumber(vehicleMasterPO.getVehicleNumber(), vendorDetails.get(0).getVendorId(),
														combinedFacility);
										if (!(vehicleNumExist.isEmpty())) {
										try {
											QRCodeGenarator qRCode = new QRCodeGenarator();
											qRCode.createQRCode(vehicleMasterPO.getVehicleNumber(), SERVER_UPLOAD_LINUX_LOCATION_FOLDER);
											EFmFmVehicleDocsPO eFmFmVehicleDocsPO = new EFmFmVehicleDocsPO();
											eFmFmVehicleDocsPO.setCreationTime(new Date());
											eFmFmVehicleDocsPO.setDocumentName(vehicleMasterPO.getVehicleNumber() + "QRCode.png");
											eFmFmVehicleDocsPO.setStatus("Y");
											eFmFmVehicleDocsPO.setUploadpath(
													SERVER_UPLOAD_LINUX_LOCATION_FOLDER + vehicleMasterPO.getVehicleNumber() + "QRCode.png");
											eFmFmVehicleDocsPO.seteFmFmVehicleMaster(vehicleNumExist.get(0));
											iVehicleCheckInBO.addVehicleUploadDetails(eFmFmVehicleDocsPO);
										} catch (Exception e) {
											log.info("QR Code Error:" + e);
										}
										
									}
									} else {
										status = "contractTypeNotExist-" + vehicleExcelDetails.get(16).toString();
										issueList.put("RNo","");
										issueList.put("IssueStatus",status);
										 vehicleMasterExcel.add(issueList);
									}

								} else {
									status = "engineNumberExist-" + vehicleExcelDetails.get(3).toString();
									issueList.put("RNo","");
									issueList.put("IssueStatus",status);
									 vehicleMasterExcel.add(issueList);
								}
							} else {
								status = "vehicleRcExist-" + vehicleExcelDetails.get(1).toString();
								issueList.put("RNo","");
								issueList.put("IssueStatus",status);
								 vehicleMasterExcel.add(issueList);
							}
						} else {
							status = "vehicleNumberExist-" + vehicleExcelDetails.get(0).toString();
							issueList.put("RNo","");
							issueList.put("IssueStatus",status);
							 vehicleMasterExcel.add(issueList);
						}
					} else {
						status = "fail-";
						issueList.put("RNo","");
						issueList.put("IssueStatus",status);
						 vehicleMasterExcel.add(issueList);
					}
				} else {
					status = "vendorNotExist-" + vehicleExcelDetails.get(6).toString();
					issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					 vehicleMasterExcel.add(issueList);
				}
			} else {
				status = "fail-";
				issueList.put("RNo","");
				issueList.put("IssueStatus",status);
				 vehicleMasterExcel.add(issueList);
			}
			}
		} catch (IndexOutOfBoundsException e) {
			log.info("Error" + e);
			issueList.put("RNo","");
			issueList.put("IssueStatus",e);
			 vehicleMasterExcel.add(issueList);

		}
		return vehicleMasterExcel;

	}
	
	/*
	 * 
	 * Upload Document Details
	 */
	@POST
	@Path("/listOfDocumentDetails")
	public Response listOfDocumentDetails(EFmFmVehicleMasterPO eFmFmVehicleMasterPO){		
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleMasterPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleMasterPO.getUserId());
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
		List<Map<String, Object>> listOfVehicleDetails= new ArrayList<Map<String, Object>>();
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		List<EFmFmVehicleDocsPO> listOfVehicle=iVehicleCheckInBO.getAlluploadVehicleFileDetails(eFmFmVehicleMasterPO.getVehicleId());		 
		if(!(listOfVehicle.isEmpty())){				
			for(EFmFmVehicleDocsPO vehicleDetails:listOfVehicle){				
					Map<String, Object>  driverList= new HashMap<String, Object>();					
					driverList.put("vehicleId",vehicleDetails.geteFmFmVehicleMaster().getVehicleId());
					driverList.put("creationDate",dateFormat.format(vehicleDetails.getCreationTime()));
					driverList.put("documentName",vehicleDetails.getDocumentName());
					driverList.put("driverDocId",vehicleDetails.getVehicleDocId());
					driverList.put("pathName",vehicleDetails.getUploadpath()
		                    .substring(vehicleDetails.getUploadpath().indexOf("upload") - 1));				
					listOfVehicleDetails.add(driverList);					
				}			
			}	
		 log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok(listOfVehicleDetails, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Service for Uploading the vehicle documents like Vehicle
	 * Insurance,Vehicle Registration,Polution Certificate,Vehicle Tax Permit
	 */

	@POST
	@Path("/vehicledocument")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces("application/json")
	public Response uploadFile(@FormDataParam("filename") InputStream uploadedInputStream,
			@FormDataParam("filename") InputStream uploadedSizeInputStream,
			@FormDataParam("filename") InputStream uploadedImageInputStream,
			@FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
			@QueryParam("vehicleId") int vehicleId, @QueryParam("typeOfUploadDoc") String typeOfUploadDoc,
			@Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException {
		log.info("serviceStart -UserId :" +userId);
		String status = "";
		String name = "os.name", filePath = "";
		boolean OsName = System.getProperty(name).startsWith("Windows");
		EFmFmVehicleDocsPO eFmFmVehicleDocsPO =new EFmFmVehicleDocsPO();
		//log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
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

		if (OsName) {
			filePath = SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER + fileDetail.getFileName();
			File pathExist = new File(SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER);
			if (!pathExist.exists()) {
				new File(SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER).mkdir();
			}
		} else {
			filePath = SERVER_UPLOAD_LINUX_LOCATION_FOLDER + fileDetail.getFileName();
			File pathExist = new File(SERVER_UPLOAD_LINUX_LOCATION_FOLDER);
			if (!pathExist.exists()) {
				new File(SERVER_UPLOAD_LINUX_LOCATION_FOLDER).mkdir();
			}
		}
		IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		EFmFmVehicleMasterPO vehicleDetails = vehicleCheckInBO.getParticularVehicleDetail(vehicleId);
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
        if(megabytes > (vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getImageUploadSize())){
        	log.info("Inside bigSize");
			status="bigSize-"+vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getImageUploadSize();
			return Response.ok(status, MediaType.APPLICATION_JSON).build();
        }	  
		}
		
		
		if (!fileExist.exists() && !fileExist.isDirectory()) {
        	log.info("success");
			status = "success";
			// save the file to the server
			saveFile(uploadedInputStream, filePath);
		} else {
        	log.info("exist");
			status = "exist";
			return Response.ok(status, MediaType.APPLICATION_JSON).build();
		}
		eFmFmVehicleDocsPO.setCreationTime(new Date());
		eFmFmVehicleDocsPO.setDocumentName(typeOfUploadDoc);
		eFmFmVehicleDocsPO.setStatus("Y");
		eFmFmVehicleDocsPO.setUploadpath(filePath);
		eFmFmVehicleDocsPO.seteFmFmVehicleMaster(vehicleDetails);
		
		if (typeOfUploadDoc.equalsIgnoreCase("Insurance")) {
			vehicleDetails.setInsurancePath(filePath);
		} else if (typeOfUploadDoc.equalsIgnoreCase("Registration")) {
			vehicleDetails.setRegistartionCertificatePath(filePath);
		} else if (typeOfUploadDoc.equalsIgnoreCase("pollution")) {
			vehicleDetails.setPoluctionCertificatePath(filePath);

		} else {
			vehicleDetails.setTaxCertificatePath(filePath);
		}
		vehicleCheckInBO.update(vehicleDetails);
		vehicleCheckInBO.addVehicleUploadDetails(eFmFmVehicleDocsPO);
		 //log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		log.info("serviceEnd -UserId :" +userId);
		return Response.ok(status, MediaType.APPLICATION_JSON).build();
	}


	/**
	 * Service for Uploading the vehicle documents like front image,left side
	 * image,right side image and back image
	 */

	@POST
	@Path("/vehicleInspectiondocument")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces("application/json")
	public Response uploadvehicleInspectiondocument(@FormDataParam("filename") InputStream uploadedInputStream,
			@FormDataParam("filename") InputStream uploadedSizeInputStream,
			@FormDataParam("filename") InputStream uploadedImageInputStream,
			@FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
			@QueryParam("inspectionId") int inspectionId, @QueryParam("typeOfUploadDoc") String typeOfUploadDoc,
			@Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException {
		//log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		String status = "";
		log.info("serviceStart -UserId :" +userId);
		String name = "os.name", filePath = "";
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
		boolean OsName = System.getProperty(name).startsWith("Windows");
		if (OsName) {
			filePath = SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER + fileDetail.getFileName();
			File pathExist = new File(SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER);
			if (!pathExist.exists()) {
				new File(SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER).mkdir();
			}
		} else {
			filePath = SERVER_UPLOAD_LINUX_LOCATION_FOLDER + fileDetail.getFileName();
			File pathExist = new File(SERVER_UPLOAD_LINUX_LOCATION_FOLDER);
			if (!pathExist.exists()) {
				new File(SERVER_UPLOAD_LINUX_LOCATION_FOLDER).mkdir();
			}
		}
		IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		List<EFmFmVehicleInspectionPO> inspectionDetails = vehicleCheckInBO
				.getParticularVehicleInspectionDetailByInspectedId(inspectionId, branchId);
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
        if(megabytes > (inspectionDetails.get(0).getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getImageUploadSize())){
        	log.info("Inside bigSize");
			status="bigSize-"+inspectionDetails.get(0).getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getImageUploadSize();
			return Response.ok(status, MediaType.APPLICATION_JSON).build();
        }	  
		}
		
		if (!fileExist.exists() && !fileExist.isDirectory()) {
			status = "success";
			// save the file to the server
			saveFile(uploadedInputStream, filePath);
		} else {
			status = "exist";
		}
		if (typeOfUploadDoc.equalsIgnoreCase("vehiclePhotoFromFront")) {
			inspectionDetails.get(0).setVehiclePhotoPathFromFront(filePath);
		} else if (typeOfUploadDoc.equalsIgnoreCase("vehiclePhotoFromBack")) {
			inspectionDetails.get(0).setVehiclePhotoPathFromBack(filePath);

		} else if (typeOfUploadDoc.equalsIgnoreCase("vehiclePhotoFromLeftSide")) {
			inspectionDetails.get(0).setVehiclePhotoPathFromLeft(filePath);

		} else if (typeOfUploadDoc.equalsIgnoreCase("vehiclePhotoFromRightSide")) {
			inspectionDetails.get(0).setVehiclePhotoPathFromRight(filePath);

		}
		vehicleCheckInBO.update(inspectionDetails.get(0));
		// log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		log.info("serviceEnd -UserId :" +userId);
		return Response.ok(status, MediaType.APPLICATION_JSON).build();
	}

	// save uploaded file to a defined location on the server
	void saveFile(InputStream uploadedInputStream, String serverLocation) {

		try {
			OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			// byte[] data ;
			outpuStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}

			outpuStream.flush();
			outpuStream.close();
		} catch (Exception e) {
			log.info("error" + e);
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
