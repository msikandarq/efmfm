package com.newtglobal.eFmFmFleet.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.newtglobal.eFmFmFleet.business.bo.IFacilityBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Validator;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFacilityToFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/xlUtilityVendorUpload")
public class VendorImportExcel {
    /*
     * @Reading vendor details from vendor_master xl utility.
     * 
     * @Stored all the values on Arraylist.
     */
    private static Log log = LogFactory.getLog(VendorImportExcel.class);
    
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();


    @POST
    @Path("/vendorRecord")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response upload(@FormDataParam("filename") InputStream uploadedInputStream,
    		@FormDataParam("filename") InputStream uploadedSizeInputStream,
            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
            @QueryParam("combinedFacility") String combinedFacility,
            @Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException {
   	 String status = "success-";
   	log.info("serviceStart -UserId :" +userId);
   	 int noOfcolumn=16; 
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
	                	if(columnValue.size()>=16){   
		                		if(!vendorMasterExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,userId,combinedFacility).isEmpty()){                		
		                			response.addAll(vendorMasterExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,userId,combinedFacility));
		                		}else{
		                			vendorExcelDetails.put(rowId, columnValue);
		                		}
	                		}else{
	                				status="Kinldy check the no -of Column - 16 - Entered Column No:"+columnValue.size();
	                				vendorReqExcel.put("RNo",Integer.toString(rowId));
	                				vendorReqExcel.put("IssueStatus",status);
		                    		response.add(vendorReqExcel);
	                	}              		
                	}else{
                		  if(columnValue.size()<16){                			              			
                			      			
  	                    		status="Kinldy fill the all the Column's";
  	                    		vendorReqExcel.put("RNo",Integer.toString(rowId));
                				vendorReqExcel.put("IssueStatus",status);
  	                    		response.add(vendorReqExcel);
  	                    	
                		}else{
                			//Need to configure below Text some where
                			if(!"tinnumber".equalsIgnoreCase(columnValue.get(0).toString().replace(" ","").trim()) 
                					|| !"VendorName".equalsIgnoreCase(columnValue.get(1).toString().replace(" ","").trim()) 
                					|| !"VendorContactName".equalsIgnoreCase(columnValue.get(2).toString().replace(" ","").trim()) 
                					|| !"MobileNumber".equalsIgnoreCase(columnValue.get(3).toString().replace(" ","").trim())
                					|| !"ContactOfficeNumber".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim())
                					|| !"EmailID".equalsIgnoreCase(columnValue.get(5).toString().replace(" ","").trim()) 
                        			|| !"State".equalsIgnoreCase(columnValue.get(6).toString().replace(" ","").trim()) 
                        			|| !"CityName".equalsIgnoreCase(columnValue.get(7).toString().replace(" ","").trim()) 
                        			|| !"Pincode".equalsIgnoreCase(columnValue.get(8).toString().replace(" ","").trim()) 
                        			|| !"VendorAddress".equalsIgnoreCase(columnValue.get(9).toString().replace(" ","").trim())                        			
                        			|| !"EmailIdLevel1".equalsIgnoreCase(columnValue.get(10).toString().replace(" ","").trim())
                        			|| !"EmailIdLevel2".equalsIgnoreCase(columnValue.get(11).toString().replace(" ","").trim())
                        			|| !"VendorContractID".equalsIgnoreCase(columnValue.get(12).toString().replace(" ","").trim())
                        			|| !"PanNumber".equalsIgnoreCase(columnValue.get(13).toString().replace(" ","").trim())
                        			|| !"ServiceTaxNumber".equalsIgnoreCase(columnValue.get(14).toString().replace(" ","").trim())
            					    || !"Facility".equalsIgnoreCase(columnValue.get(15).toString().replace(" ","").trim())){                		                    				    
                				    vendorReqExcel.put("RNo",Integer.toString(rowId));
	                				vendorReqExcel.put("IssueStatus","Kinldy check the Column Name & No of Column's ");
	  	                    		response.add(vendorReqExcel);
	  	                    		log.info("serviceEnd -UserId :" +userId);
	  	                    		return Response.ok(response, MediaType.APPLICATION_JSON).build();
                			}
                		}
                	}
				}              
            }
            log.info("Size"+vendorExcelDetails.size());
			if(response.isEmpty()){
				if(!vendorExcelDetails.isEmpty()){
					response=vendorRecord(vendorExcelDetails,branchId,userId,combinedFacility);	
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
        return Response.ok(response,MediaType.APPLICATION_JSON).build();
    }
    
    
    
    private List<Map<String, Object>> vendorMasterExcelValidator(String rowId,ArrayList<Object> columnValue,int branchId,int noOfcolumn,int userId,String combinedFacility){
	   		   
	    IVendorDetailsBO iVendorMasterBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
	    List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();	    
	    String validInt="^[0-9]*$";
	    String alphanumaric="^[a-zA-Z0-9]*$";
	    String charAt="^[a-zA-Z]*$";
		
			if (columnValue.get(0).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(0).toString() ==null || columnValue.get(0).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(0).toString().isEmpty() || !columnValue.get(0).toString().replace(" ", "").trim().matches(alphanumaric)) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the TIN Number : "+columnValue.get(0).toString();
				empReqExcelRows.put("RNo",rowId.concat(",0"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{				
				List<EFmFmVendorMasterPO> venderTinNumberExist = iVendorMasterBO.getVendorTinNumber(columnValue.get(0).toString(),combinedFacility);
                 if (!venderTinNumberExist.isEmpty()) {
                	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			    	String status = " TIN Number already available -" + columnValue.get(0).toString().trim();
			    	empReqExcelRows.put("RNo",rowId.concat(",0"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
			    } 
			}
			
			if (columnValue.get(1).toString().equalsIgnoreCase("CELL_TYPE_BLANK") || columnValue.get(1).toString() ==null || columnValue.get(1).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(1).toString().isEmpty()) {	
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Vendor Name : "+columnValue.get(1).toString();
				empReqExcelRows.put("RNo",rowId.concat(",1"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				List<EFmFmVendorMasterPO> venderNameExist = iVendorMasterBO.getAllVendorName(columnValue.get(1).toString().trim(),combinedFacility);
				if (!venderNameExist.isEmpty()) {
			    	String status = " Vendor Name already available -" + columnValue.get(1).toString().trim();
			    	empReqExcelRows.put("RNo",rowId.concat(",1"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
			    } 
				
			}
			
			if (columnValue.get(2).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(2).toString() ==null || columnValue.get(2).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(2).toString().isEmpty() || !columnValue.get(2).toString().replace(" ", "").trim().matches(charAt)) {	
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Vendor Contact Name : "+columnValue.get(2).toString();
				empReqExcelRows.put("RNo",rowId.concat(",2"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
				

			if (columnValue.get(3).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(3).toString() ==null || columnValue.get(3).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(3).toString().isEmpty() || !columnValue.get(3).toString().replace(" ", "").trim().matches(validInt)) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Mobile Number and Mobile Should be Integer : "+columnValue.get(3).toString();
				empReqExcelRows.put("RNo",rowId.concat(",3"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else if(columnValue.get(3).toString().length()<=18 && columnValue.get(3).toString().length()>5){
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					 List<EFmFmVendorMasterPO> venderMobileNoExist = iVendorMasterBO.getVendorMobileNo(columnValue.get(3).toString().trim(),combinedFacility);					 
					 if(!venderMobileNoExist.isEmpty()){
						 String status="Mobile Number already available on database  : "+columnValue.get(3).toString();
						 empReqExcelRows.put("RNo",rowId.concat(",3"));
							empReqExcelRows.put("IssueStatus",status);
							childRowResponse.add(empReqExcelRows);
					 }
				
				
			}else{
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				 String status="kindly check the Mobile Number length(ex:India -12 digit),length should be country Code with MobileNumber  : "+columnValue.get(3).toString();
				 empReqExcelRows.put("RNo",rowId.concat(",3"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
			}
			
			if (columnValue.get(4).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(4).toString() ==null || columnValue.get(4).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(4).toString().isEmpty() || !columnValue.get(4).toString().replace(" ", "").trim().matches(validInt)) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Contact Office Number and Mobile Should be Integer : "+columnValue.get(4).toString();
				empReqExcelRows.put("RNo",rowId.concat(",4"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
			if (columnValue.get(5).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(5).toString() ==null || columnValue.get(5).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(5).toString().isEmpty()) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Email Id : "+columnValue.get(5).toString();
				empReqExcelRows.put("RNo",rowId.concat(",5"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else {	
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
							String emailIdRegex = "^[A-Za-z0-9+_.-]+@(.+)$";						 
					   		Pattern pattern = Pattern.compile(emailIdRegex);
					   		Matcher matcher = pattern.matcher(columnValue.get(5).toString());				   
						    if(matcher.matches()){
						    	List<EFmFmVendorMasterPO> venderEmailIdExist = iVendorMasterBO.getVendorEmailId(columnValue.get(5).toString().trim(),combinedFacility);
							      if(!venderEmailIdExist.isEmpty()){
										 String status="Email Id already available on database  : "+columnValue.get(5).toString();
										 empReqExcelRows.put("RNo",rowId.concat(",5"));
											empReqExcelRows.put("IssueStatus",status);
											childRowResponse.add(empReqExcelRows);
									 } 
						    }else{
						    	String status="kindly check the Email Id : "+columnValue.get(5).toString();
						    	empReqExcelRows.put("RNo",rowId.concat(",5"));
								empReqExcelRows.put("IssueStatus",status);
								childRowResponse.add(empReqExcelRows);
						    }				  				
			}	
			
			
			
			if (columnValue.get(6).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(6).toString() ==null || columnValue.get(6).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(6).toString().isEmpty() || !columnValue.get(6).toString().replace(" ", "").trim().matches(charAt) ) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the State Name : "+columnValue.get(6).toString();
				empReqExcelRows.put("RNo",rowId.concat(",6"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
			if (columnValue.get(7).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(7).toString() ==null || columnValue.get(7).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(7).toString().isEmpty() || !columnValue.get(7).toString().replace(" ", "").trim().matches(charAt) ) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the City Name : "+columnValue.get(7).toString();
				empReqExcelRows.put("RNo",rowId.concat(",7"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
			if (columnValue.get(8).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(8).toString() ==null || columnValue.get(8).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(8).toString().isEmpty() || !columnValue.get(8).toString().replace(" ", "").trim().matches(validInt)) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Pincode : "+columnValue.get(8).toString();
				empReqExcelRows.put("RNo",rowId.concat(",8"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			if (columnValue.get(9).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(9).toString() ==null || columnValue.get(9).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(9).toString().isEmpty() ) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Current Address : "+columnValue.get(9).toString();
				empReqExcelRows.put("RNo",rowId.concat(",9"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}	
		/*	
			if (columnValue.get(10).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(10).toString() ==null || columnValue.get(10).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(10).toString().isEmpty()) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Email Id Level 1 : "+columnValue.get(10).toString();
				empReqExcelRows.put("RNo",rowId.concat(",10"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			if (columnValue.get(11).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(11).toString() ==null || columnValue.get(11).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(11).toString().isEmpty()) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Email Id Level 2: "+columnValue.get(11).toString();
				empReqExcelRows.put("RNo",rowId.concat(",11"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}*/
			
			
			if (columnValue.get(12).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(12).toString() ==null || columnValue.get(12).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(12).toString().isEmpty()) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Vendor Contract ID: "+columnValue.get(12).toString();
				empReqExcelRows.put("RNo",rowId.concat(",12"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			if (columnValue.get(13).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(13).toString() ==null || columnValue.get(13).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(13).toString().isEmpty()) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Pan Number: "+columnValue.get(13).toString();
				empReqExcelRows.put("RNo",rowId.concat(",13"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			if (columnValue.get(14).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(14).toString() ==null || columnValue.get(14).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(14).toString().isEmpty()) {
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Service TaxNumber : "+columnValue.get(14).toString();
				empReqExcelRows.put("RNo",rowId.concat(",14"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}		
			return childRowResponse;
	
}

    /**
     * 
     * @param add
     *            new vendor records
     * @return success after adding the vendor
     */

    @POST
    @Path("/addnewvendor")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addNewVendor(EFmFmVendorMasterPO vendorMasterPO) {
        IVendorDetailsBO iVendorMasterBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
        log.info("serviceStart -UserId :" + vendorMasterPO.getUserId());
        Map<String, Object> requests = new HashMap<String, Object>();
        vendorMasterPO.setStatus("P");
        IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        Map<String, Object> responce = new HashMap<String, Object>();
        		
        log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
        try{
        	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vendorMasterPO.getUserId()))){

        		responce.put("status", "invalidRequest");
        		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        	}
        	List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(vendorMasterPO.getUserId());
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
    		
    		if(vendorMasterPO.getVendorName()==null||vendorMasterPO.getVendorName().equals(""))
    		{temp.append("::Vendor name cannot be empty");
    		status="Fail";
    		}
    		else{
    		CharSequence vendorName =vendorMasterPO.getVendorName();
			Matcher matcher= Validator.alphaNumericSpaceDot(vendorName);
			if(!matcher.matches()||vendorMasterPO.getVendorName().length()<3||vendorMasterPO.getVendorName().length()>200)
			{temp.append("::Vendor name can be alphanumeric, space, dot and range min 3 max 200 letters");
			    status="Fail";
			}}
    		
    		if(vendorMasterPO.getVendorMobileNo()==null||vendorMasterPO.getVendorMobileNo().equals(""))
    		{temp.append("::Vendor mobile number cannot be empty");
    		status="Fail";    			
    		}
    		else{
        		CharSequence vendorMobile =vendorMasterPO.getVendorMobileNo();
    			Matcher matcher= Validator.mobNumber(vendorMobile);
    			if(!matcher.matches()||vendorMasterPO.getVendorMobileNo().length()<6||vendorMasterPO.getVendorMobileNo().length()>18)    			
    			{temp.append("::Vendor mobile number can be only numbers and range min 6 max 18 digits");
        		status="Fail";    				
    			}
    		}
			
			
    		if(vendorMasterPO.getEmailId()==null||vendorMasterPO.getEmailId().equals(""))
    		{temp.append("::Vendor email ID1 cannot be empty");
    		status="Fail";    			
    		}
    		else{CharSequence vendorEmail =vendorMasterPO.getEmailId();
			Matcher matcher= Validator.email(vendorEmail);
			if(!matcher.matches())
			{temp.append("::Vendor email ID1 should be in correct format");
    		status="Fail";  				
			}
    		}
    			
    		if(vendorMasterPO.getEmailIdLvl1()==null||vendorMasterPO.getEmailIdLvl1().isEmpty()){}
    		else{
			CharSequence vendorEmail1 =vendorMasterPO.getEmailIdLvl1();
			Matcher email1= Validator.email(vendorEmail1);
			if(!email1.matches())
			{temp.append("::Vendor emailID 2  ID should be in correct format");
    		status="Fail";  				
			}}
    		
			if(vendorMasterPO.getEmailIdLvl2()==null||vendorMasterPO.getEmailIdLvl2().isEmpty()){}
			else{
			CharSequence vendorEmail2 =vendorMasterPO.getEmailIdLvl2();
			Matcher email2= Validator.email(vendorEmail2);
			if(!email2.matches())
			{temp.append("::Vendor email ID 3 should be in correct format");
    		status="Fail";  				
			}}
    		
			if(vendorMasterPO.getPanNumber()==null||vendorMasterPO.getPanNumber().equals(""))
			{temp.append("::Vendor pan number cannot be empty");
    		status="Fail"; 				
			}
			else{
				CharSequence vendorPanNumber =vendorMasterPO.getPanNumber();
				Matcher matcher= Validator.alphaNumeric(vendorPanNumber);
				if(!matcher.matches()||vendorMasterPO.getPanNumber().length()<5||vendorMasterPO.getPanNumber().length()>15)
				{temp.append("::Vendor pan number can be alphabets and numbers, range min 5 max 15 ");
	    		status="Fail"; 					
				}			
			}
			if(vendorMasterPO.getAddress()==null||vendorMasterPO.getAddress().length()<5||vendorMasterPO.getAddress().length()>200)
			{temp.append("::Vendor Address range min 5 max 200 characters");
    		status="Fail";				
			}
			
			if(vendorMasterPO.getVendorContactName()==null||vendorMasterPO.getVendorContactName().equals(""))
			{temp.append("::Vendor Contact Name 1 cannot be empty");
    		status="Fail";				
			}
			else{
				CharSequence vendorContactName =vendorMasterPO.getVendorContactName();
				Matcher matcher= Validator.alphaSpaceDot(vendorContactName);
				if(!matcher.matches()||vendorMasterPO.getVendorContactName().length()<3||vendorMasterPO.getVendorContactName().length()>20)
				{temp.append("::Vendor ContactName1 can contain only alphabets and range min 3 max 20");
	    		status="Fail";					
				}
			}
			
			if(vendorMasterPO.getVendorContactName2()==null||vendorMasterPO.getVendorContactName2().isEmpty()){}							
			else{
			CharSequence vendorContactName2 =vendorMasterPO.getVendorContactName2();
			Matcher matcher= Validator.alphaSpaceDot(vendorContactName2);
			if(!matcher.matches()||vendorMasterPO.getVendorContactName2().length()<3||vendorMasterPO.getVendorContactName2().length()>20)
			{temp.append("::Vendor ContactName2 can contain only alphabets and range min 3 max 20");
    		status="Fail";					
			}}
			
			if(vendorMasterPO.getVendorContactName3()==null||vendorMasterPO.getVendorContactName3().isEmpty()){}
			else{
			CharSequence vendorContactName3 =vendorMasterPO.getVendorContactName3();
			Matcher contact3= Validator.alphaSpaceDot(vendorContactName3);
			if(!contact3.matches()||vendorMasterPO.getVendorContactName3().length()<3||vendorMasterPO.getVendorContactName3().length()>20)
			{temp.append("::Vendor ContactName3 can contain only alphabets and range min 3 max 20");
    		status="Fail";					
			}}
			
			if(vendorMasterPO.getVendorContactName4()==null||vendorMasterPO.getVendorContactName4().isEmpty()){}
			else{CharSequence vendorContactName4 =vendorMasterPO.getVendorContactName4();
			Matcher contact4= Validator.alphaSpaceDot(vendorContactName4);
			if(!contact4.matches()||vendorMasterPO.getVendorContactName4().length()<3||vendorMasterPO.getVendorContactName4().length()>20)
			{temp.append("::Vendor ContactName4 can contain only alphabets and range min 3 max 20");
    		status="Fail";					
			}}
			
    		if(vendorMasterPO.getVendorOfficeNo()==null||vendorMasterPO.getVendorOfficeNo().equals(""))	
    		{temp.append("::Vendor contact number1 cannot be empty");
    		status="Fail";
      		}
    		else{
    			CharSequence vendorContactNumb1 =vendorMasterPO.getVendorOfficeNo();
    			Matcher contactNumb1= Validator.mobNumber(vendorContactNumb1);
    			if(!contactNumb1.matches()||vendorMasterPO.getVendorOfficeNo().length()<6||vendorMasterPO.getVendorOfficeNo().length()>18)
    			{temp.append("::Vendor contactNumber1 can contain only numbers and range min 6 max 18");
        		status="Fail";    				
    			}
    		}
    		
    		if(vendorMasterPO.getVendorContactNumber2()==null||vendorMasterPO.getVendorContactNumber2().isEmpty()){}
    		else{CharSequence vendorContactNumb2 =vendorMasterPO.getVendorContactNumber2();
			Matcher contactNumb2= Validator.mobNumber(vendorContactNumb2);
			if(!contactNumb2.matches()||vendorMasterPO.getVendorContactNumber2().length()<6||vendorMasterPO.getVendorContactNumber2().length()>18)
			{temp.append("::Vendor contactNumber2 can contain only numbers and range min 6 max 18");
    		status="Fail";
			}
			}
    		
    		if(vendorMasterPO.getVendorContactNumber3()==null||vendorMasterPO.getVendorContactNumber3().isEmpty()){}
    		else{CharSequence vendorContactNumb3 =vendorMasterPO.getVendorContactNumber3();
			Matcher contactNumb3= Validator.mobNumber(vendorContactNumb3);
			if(!contactNumb3.matches()||vendorMasterPO.getVendorContactNumber3().length()<6||vendorMasterPO.getVendorContactNumber3().length()>18)
			{temp.append("::Vendor contactNumber3 can contain only numbers and range min 6 max 18");
    		status="Fail";
			}}
			
    		if(vendorMasterPO.getVendorContactNumber4()==null||vendorMasterPO.getVendorContactNumber4().isEmpty()){}
    		else{CharSequence vendorContactNumb4 =vendorMasterPO.getVendorContactNumber4();
			Matcher contactNumb4= Validator.mobNumber(vendorContactNumb4);
			if(!contactNumb4.matches()||vendorMasterPO.getVendorContactNumber4().length()<6||vendorMasterPO.getVendorContactNumber4().length()>18)
			{temp.append("::Vendor contactNumber4 can contain only numbers and range min 6 max 18");
    		status="Fail";				
			}}
    		
    		if(vendorMasterPO.getServiceTaxNumber()==null||vendorMasterPO.getServiceTaxNumber().equals(""))
    		{temp.append("::Vendor service tax number cannot be empty");
    		status="Fail";    			
    		} 
//    		else{CharSequence vendorServiceTaxNo =vendorMasterPO.getServiceTaxNumber();
//			Matcher serviceTax= Validator.validateNumber(vendorServiceTaxNo);
//    			if(!serviceTax.matches()||vendorMasterPO.getServiceTaxNumber().length()<5||vendorMasterPO.getServiceTaxNumber().length()>20)
//    			{temp.append("::Vendor service tax number can have numbers alphabets and range min 5 max 20");
//        		status="Fail"; 
//    			}
//    			}
    		if(status.equals("Fail"))
			{log.info("Invalid input:");
			requests.put("inputInvalid", temp);
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}
    			
    		}
    	log.info("Input valid");
        
        List<EFmFmVendorMasterPO> venderExist = iVendorMasterBO.getAllVendorName(vendorMasterPO.getVendorName(),vendorMasterPO.getCombinedFacility());
        if (venderExist.isEmpty()) {
            iVendorMasterBO.save(vendorMasterPO);
            try {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
					public void run() {
                        SendMailBySite mailSender=new SendMailBySite();
                        String toMailId = ContextLoader.getContext().getMessage("user.toMailSender", null, "toMailSender", null);
                        mailSender.mailForVendorRegistration(toMailId, "team", vendorMasterPO.getVendorName());
                    	}
                });
                thread1.start();
            } catch (Exception e) {
                log.info("vendor Form registration mail" + e);
            }
            requests.put("status", "success");
            return Response.ok(requests, MediaType.APPLICATION_JSON).build();
        }
        requests.put("status", "exist");
        log.info("serviceEnd -UserId :" + vendorMasterPO.getUserId());
        return Response.ok(requests, MediaType.APPLICATION_JSON).build();

    }

    /*
     * @xl utility row values are inserted into vendor master table table.
     * validation also be handle here.
     * 
     */
    private List<Map<String, Object>> vendorRecord(Map<Integer, Object> vendorRecordDetails, int branchId,int userId,String combinedFacility) throws ParseException {
        IVendorDetailsBO iVendorMasterBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		 IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
	        IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

        String status = "";
        boolean donotSave = false;
        List<Map<String, Object>> vendorMasterExcel = new ArrayList<Map<String, Object>>();
	    Map<String, Object> issueList = new HashMap<String, Object>();
        try {
        	for (Entry<Integer, Object> entry : vendorRecordDetails.entrySet()) {
				issueList = new HashMap<String, Object>();                
				ArrayList vendorExcelDetails = (ArrayList) entry.getValue();        	
            if (vendorExcelDetails.get(3).toString().length() <5 && vendorExcelDetails.get(3).toString().length() >18) {
                status = "mobileNumber-" + vendorExcelDetails.get(3).toString();
                issueList.put("RNo","");
                issueList.put("IssueStatus",status);
                vendorMasterExcel.add(issueList);
                donotSave = true;
                return vendorMasterExcel;
            }
            String regex = "^(.+)@(.+)$";
            if (vendorExcelDetails.get(0).toString().isEmpty() && vendorExcelDetails.get(1).toString().isEmpty()
                    && vendorExcelDetails.get(5).toString().isEmpty()) {
                status = "fail-";
                issueList.put("RNo","");
                issueList.put("IssueStatus",status);
                vendorMasterExcel.add(issueList);
                donotSave = true;
                return vendorMasterExcel;

            }
            if (!(vendorExcelDetails.get(5).toString().matches(regex))) {
                status = "invalidemailId-" + vendorExcelDetails.get(5).toString().trim();
                issueList.put("RNo","");
                issueList.put("IssueStatus",status);
                vendorMasterExcel.add(issueList);
                donotSave = true;
                return vendorMasterExcel;
            }
            
            List<EFmFmFacilityToFacilityMappingPO> branchDetails=facilityBO.getParticularFacilityDetailFromBranchName(vendorExcelDetails.get(15).toString().trim());
	        if(branchDetails.isEmpty()){
	            status = "Please check-" + vendorExcelDetails.get(15).toString().trim()+" facility not exist.";
	            issueList.put("RNo","");
				issueList.put("IssueStatus",status);
                vendorMasterExcel.add(issueList);
	            return vendorMasterExcel;
	        }
	        else if(!(branchDetails.isEmpty()) && branchDetails.get(0).getFacilityStatus().equalsIgnoreCase("N") ){
	            status = "Please check-" + vendorExcelDetails.get(15).toString().trim()+" facility is  disable.";
	            issueList.put("RNo","");
				issueList.put("IssueStatus",status);
                vendorMasterExcel.add(issueList);
	            return vendorMasterExcel;
	        }
	        
		    int baseBranchId=iUserMasterBO.getBranchIdFromBranchName(vendorExcelDetails.get(15).toString().trim());
		    EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
		    eFmFmClientBranch.setBranchId(baseBranchId);
	       if(!(facilityBO.checkFacilityAccess(userId, baseBranchId))){
	           status = "Please check looks like you don't have access of -" + vendorExcelDetails.get(15).toString().trim()+" facility";
	           issueList.put("RNo","");
				issueList.put("IssueStatus",status);
                vendorMasterExcel.add(issueList);
	           return vendorMasterExcel;
	       }
            
            
            EFmFmVendorMasterPO vendorMasterPO = new EFmFmVendorMasterPO();
            vendorMasterPO.setTinNumber(vendorExcelDetails.get(0).toString().trim());
            vendorMasterPO.setVendorName(vendorExcelDetails.get(1).toString().trim());
            vendorMasterPO.setVendorContactName(vendorExcelDetails.get(2).toString());
            vendorMasterPO.setVendorMobileNo(vendorExcelDetails.get(3).toString());
            vendorMasterPO.setVendorOfficeNo(vendorExcelDetails.get(4).toString());
            vendorMasterPO.setEmailId(vendorExcelDetails.get(5).toString());
            vendorMasterPO.setStateName(vendorExcelDetails.get(6).toString());
            vendorMasterPO.setCityName(vendorExcelDetails.get(7).toString());
            vendorMasterPO.setPinCode(Integer.parseInt(vendorExcelDetails.get(8).toString()));
            vendorMasterPO.setAddress(vendorExcelDetails.get(9).toString());  
            
            if(vendorExcelDetails.get(10).toString().equalsIgnoreCase("CELL_TYPE_BLANK")){
	            vendorMasterPO.setEmailIdLvl1("");
	            vendorMasterPO.setEmailIdLvl2("");
            }else{
            	vendorMasterPO.setEmailIdLvl1(vendorExcelDetails.get(10).toString());
	            vendorMasterPO.setEmailIdLvl2(vendorExcelDetails.get(11).toString());
            }            
            if(vendorExcelDetails.get(11).toString().equalsIgnoreCase("CELL_TYPE_BLANK")){	           
	            vendorMasterPO.setEmailIdLvl2("");
            }else{            	
	            vendorMasterPO.setEmailIdLvl2(vendorExcelDetails.get(11).toString());
	        }            
            vendorMasterPO.setVendorContractId(vendorExcelDetails.get(12).toString());
            vendorMasterPO.setPanNumber(vendorExcelDetails.get(13).toString());
            vendorMasterPO.setServiceTaxNumber(vendorExcelDetails.get(14).toString());            
            EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
            eFmFmClientBranchPO.setBranchId(baseBranchId);
            vendorMasterPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
            vendorMasterPO.setStatus("P");
            // vendor Record already existing on table.
            if (!vendorMasterPO.getVendorName().isEmpty() && vendorMasterPO.getVendorName() != null
                    && vendorMasterPO.getVendorName() != "") {
                List<EFmFmVendorMasterPO> venderNameExist = iVendorMasterBO
                        .getAllVendorName(vendorMasterPO.getVendorName(),combinedFacility);
                if (venderNameExist.isEmpty()) {
                    List<EFmFmVendorMasterPO> venderMobileNoExist = iVendorMasterBO
                            .getVendorMobileNo(vendorMasterPO.getVendorMobileNo(), combinedFacility);
                    if (venderMobileNoExist.isEmpty()) {
                        List<EFmFmVendorMasterPO> venderTinNumberExist = iVendorMasterBO
                                .getVendorTinNumber(vendorMasterPO.getTinNumber(),combinedFacility);
                        if (venderTinNumberExist.isEmpty()) {
                            List<EFmFmVendorMasterPO> venderEmailIdExist = iVendorMasterBO
                                    .getVendorEmailId(vendorMasterPO.getEmailId(),combinedFacility);
                            if (venderEmailIdExist.isEmpty() && !donotSave) {
                                status = "success-";
                                iVendorMasterBO.save(vendorMasterPO);                              
                                try {
                                    Thread thread1 = new Thread(new Runnable() {
                                        @Override
                    					public void run() {
                                            SendMailBySite mailSender=new SendMailBySite();
                                            String toMailId = ContextLoader.getContext().getMessage("user.toMailSender", null, "toMailSender", null);
                                            mailSender.mailForVendorRegistration(toMailId, "team", vendorMasterPO.getVendorName());
                                        	}
                                    });
                                    thread1.start();
                                } catch (Exception e) {
                                    log.info("vendor excel registration mail" + e);
                                } 
                                
                            }
                        } else {
                            status = "tinNumberexist-" + vendorExcelDetails.get(0).toString().trim();
                            issueList.put("RNo","");
                            issueList.put("IssueStatus",status);
                            vendorMasterExcel.add(issueList);
                            return vendorMasterExcel;
                        }
                    } else {
                        status = "mobilenumberexist-" + vendorExcelDetails.get(3).toString();
                        issueList.put("RNo","");
                        issueList.put("IssueStatus",status);
                        vendorMasterExcel.add(issueList);
                        return vendorMasterExcel;
                    }
                } else {
                    status = "vendorNameExist-" + vendorExcelDetails.get(1).toString().trim();
                    issueList.put("RNo","");
                    issueList.put("IssueStatus",status);
                    vendorMasterExcel.add(issueList);
                    return vendorMasterExcel;
                }
            }
        	}
        } catch (Exception e) {
			log.info("vendor upload"+e);    
			issueList.put("RNo","");
            issueList.put("IssueStatus",e);
            vendorMasterExcel.add(issueList);
            return vendorMasterExcel;
        }
        return vendorMasterExcel;
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
