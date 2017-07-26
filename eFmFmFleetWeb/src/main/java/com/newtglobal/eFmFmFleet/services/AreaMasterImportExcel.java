package com.newtglobal.eFmFmFleet.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

import com.newtglobal.eFmFmFleet.business.bo.IFacilityBO;
import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaNodalMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientRouteMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFacilityToFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmZoneMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/xlUtilityAreaUpload")
public class AreaMasterImportExcel {	
	/*
	 * @Reading vendor details from vendor_master xl utility.
	 * @Stored all the values on Arraylist.
	 */
	private static Log log = LogFactory.getLog(AreaMasterImportExcel.class);	
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	
	@POST
	@Path("/areaRecord")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
    public Response upload(@FormDataParam("filename") InputStream uploadedInputStream,
    		@FormDataParam("filename") InputStream uploadedSizeInputStream,
            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
            @QueryParam("combinedFacility") String combinedFacility,
            @Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException {
   	 String status = "success-";
   	 log.info("serviceStart -UserId :" +userId);
   	 int noOfcolumn=5; 
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
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(userId);
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
                	if(columnValue.size()>=5){   
	                		if(!areaMasterExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn).isEmpty()){                		
	                			response.addAll(areaMasterExcelValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn));
	                		}else{
	                			vendorExcelDetails.put(rowId, columnValue);
	                		}
                		}else{
                				status="Kinldy check the no -of Column -5 "+columnValue.size();                				
                				vendorReqExcel.put("RNo",Integer.toString(rowId));
                				vendorReqExcel.put("IssueStatus","Kinldy check the Column Names sequence ");
	                    		response.add(vendorReqExcel);
                	}              		
                	}else{
                		  if(columnValue.size()<5){                			              			
                			  status="Kinldy fill the all the Column's except AreaDescription";
                			  vendorReqExcel.put("RNo",Integer.toString(rowId));
              				  vendorReqExcel.put("IssueStatus","Kinldy check the Column Names sequence ");
  	                    		response.add(vendorReqExcel);
  	                    	
                		}else{
                			//Need to configure below Text some where               			
                			if(!"AreaName".equalsIgnoreCase(columnValue.get(0).toString().replace(" ","").trim()) 
                					|| !"AreaDescription".equalsIgnoreCase(columnValue.get(1).toString().replace(" ","").trim()) 
                					|| !"RouteName".equalsIgnoreCase(columnValue.get(2).toString().replace(" ","").trim())
            					    || !"Facility".equalsIgnoreCase(columnValue.get(3).toString().replace(" ","").trim())
                				    || !"Distance".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim())){                		                    				    
                				status="Kinldy check the Column Names & No of Column's"+columnValue.size();
                				vendorReqExcel.put("RNo",Integer.toString(rowId));
                				vendorReqExcel.put("IssueStatus","Kinldy check the Column Names & No of Column's ");
  	                    		response.add(vendorReqExcel); 
  	                    		log.info("serviceEnd -UserId :" +userId);
  	                    		return Response.ok(response, MediaType.APPLICATION_JSON).build();
                			}
                		}
                	}
				}              
            }
            log.info("Status"+log+"Size"+vendorExcelDetails.size());
			if(response.isEmpty()){
				if(!vendorExcelDetails.isEmpty()){
					response=areaRecord(vendorExcelDetails,branchId,userId,combinedFacility);	
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
            return Response.ok(response, MediaType.APPLICATION_JSON).build();}
       	log.info("serviceEnd -UserId :" +userId);
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }
    
	/*
	 * @Reading details from nodal master xl utility.
	 * @Stored all the values on Arraylist.
	 */
	@POST
	@Path("/nodalRecord")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
    public Response uploadNodalRoutes(@FormDataParam("filename") InputStream uploadedInputStream,
    		@FormDataParam("filename") InputStream uploadedSizeInputStream,
            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
            @QueryParam("combinedFacility") String combinedFacility, 
            @Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException {
	log.info("serviceStart -UserId :" +userId);
   	 String status = "success-";
   	 int noOfcolumn=5; 
        List<Map<String, Object>> response =new ArrayList<Map<String, Object>>();
       	Map<String, Object>  nodalReqExcel= new HashMap<String, Object>(); 
       	Map<Integer, Object>  nodalExcelDetails= new HashMap<Integer, Object>();
       	IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
		 	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),userId))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(userId);
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
		 
       try {
        XSSFWorkbook workbook = new XSSFWorkbook(uploadedInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        
        if (!((fileDetail.getFileName().toLowerCase().endsWith(".xlsx")))){
   			log.info("File Exctension");
			status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
			nodalReqExcel.put("IssueStatus",status);
      		response.add(nodalReqExcel);
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
			nodalReqExcel.put("IssueStatus",status);
      		response.add(nodalReqExcel);
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
                	if(columnValue.size()>=5){   
	                		if(!nodalPointValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn).isEmpty()){                		
	                			response.addAll(nodalPointValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn));
	                		}else{
	                			nodalExcelDetails.put(rowId, columnValue);
	                		}
                		}else{
                				status="Kinldy check the no -of Column -5 "+columnValue.size();                				
                				nodalReqExcel.put("RNo",Integer.toString(rowId));
                				nodalReqExcel.put("IssueStatus","Kinldy check the Column Names sequence ");
	                    		response.add(nodalReqExcel);
                	}              		
                	}else{
                		  if(columnValue.size()<5){                			              			
                			  status="Kinldy fill the all the Column's";
                			  nodalReqExcel.put("RNo",Integer.toString(rowId));
                			  nodalReqExcel.put("IssueStatus","Kinldy check the Column Names sequence ");
  	                    		response.add(nodalReqExcel);
  	                    	
                		}else{
                			//Need to configure below Text some where               			
                			if(!"AreaName".equalsIgnoreCase(columnValue.get(0).toString().replace(" ","").trim()) 
                					|| !"NodalPointName".equalsIgnoreCase(columnValue.get(1).toString().replace(" ","").trim()) 
                					|| !"NodalRouteName".equalsIgnoreCase(columnValue.get(2).toString().replace(" ","").trim())
                					|| !"NodalPointGeoCodes".equalsIgnoreCase(columnValue.get(3).toString().replace(" ","").trim())
            					    || !"Facility".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim())){                		                    				    
                				status="Kinldy check the no -of Columns and column Names"+columnValue.size();
                				nodalReqExcel.put("RNo",Integer.toString(rowId));
                				nodalReqExcel.put("IssueStatus","Kinldy check the Column Names & No of Column's");
  	                    		response.add(nodalReqExcel);  
  	                    		log.info("serviceEnd -UserId :" +userId);
  	                    		return Response.ok(response, MediaType.APPLICATION_JSON).build();
                			}
                		}
                	}
				}              
            }
            log.info("Status"+log+"Size"+nodalExcelDetails.size());
			if(response.isEmpty()){
				if(!nodalExcelDetails.isEmpty()){
					response=insertNodalRouteRecords(nodalExcelDetails,branchId,userId,combinedFacility);	
				}
			}

        } catch (Exception e) {
        	log.info(" Error :" +e);
  		try{
        	if(e.getCause().toString().contains("InvalidFormatException")){
				status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
				nodalReqExcel.put("IssueStatus",status);
          		response.add(nodalReqExcel);
          		log.info(e+" serviceEnd -UserId :" +userId);
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
        	}
      		}catch(Exception e1){
          		log.info(" Error :" +e1);
      		}
			status="Kinldy check the excel for any empty column,System will except only excel format without formula and special symbols.";  	                    		
			nodalReqExcel.put("IssueStatus",status);
      		response.add(nodalReqExcel);
      		log.info(e+" serviceEnd -UserId :" +userId);
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
            }
       log.info("serviceEnd -UserId :" +userId);
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    } 
	
	  private List<Map<String, Object>> nodalPointValidator(String rowId,ArrayList<Object> columnValue,int branchId,int noOfcolumn){	   		   
	    	List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();	   	
		    if (columnValue.get(0).toString().equalsIgnoreCase("CELL_TYPE_BLANK") ||columnValue.get(0).toString() ==null || columnValue.get(0).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(0).toString().isEmpty() ) {
		    	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the Area Name value : "+columnValue.get(0).toString();
					empReqExcelRows.put("RNo",rowId.concat(",0"));
					empReqExcelRows.put("IssueStatus",status);
					 childRowResponse.add(empReqExcelRows);
				}
		    if (columnValue.get(1).toString().equalsIgnoreCase("CELL_TYPE_BLANK") ||columnValue.get(1).toString() ==null || columnValue.get(1).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(1).toString().isEmpty()) {
	    	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the NodalPoint Name value  : "+columnValue.get(0).toString();
				empReqExcelRows.put("RNo",rowId.concat(",0"));
				empReqExcelRows.put("IssueStatus",status);
				 childRowResponse.add(empReqExcelRows);
			}
		  if (columnValue.get(2).toString().equalsIgnoreCase("CELL_TYPE_BLANK") ||columnValue.get(2).toString() ==null || columnValue.get(2).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(2).toString().isEmpty()) {
					 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					String status="kindly check the NodalRoute Name value : "+columnValue.get(2).toString();
					empReqExcelRows.put("RNo",rowId.concat(",2"));
					empReqExcelRows.put("IssueStatus",status);
					 childRowResponse.add(empReqExcelRows);
				}
		  if (columnValue.get(3).toString().equalsIgnoreCase("CELL_TYPE_BLANK") ||columnValue.get(3).toString() ==null || columnValue.get(3).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(3).toString().isEmpty()) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the NodalPointGeoCodes value  : "+columnValue.get(3).toString();
				empReqExcelRows.put("RNo",rowId.concat(",3"));
				empReqExcelRows.put("IssueStatus",status);
				 childRowResponse.add(empReqExcelRows);
			}
				return childRowResponse;	
	}
	    
    
    private List<Map<String, Object>> areaMasterExcelValidator(String rowId,ArrayList<Object> columnValue,int branchId,int noOfcolumn){	   		   
    	List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();	   	
	    if (columnValue.get(0).toString().equalsIgnoreCase("CELL_TYPE_BLANK") ||columnValue.get(0).toString() ==null || columnValue.get(0).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(0).toString().isEmpty() ) {
	    	 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Area Name value  : "+columnValue.get(0).toString();
				empReqExcelRows.put("RNo",rowId.concat(",0"));
				empReqExcelRows.put("IssueStatus",status);
				 childRowResponse.add(empReqExcelRows);
			}
			
			/*if (columnValue.get(1).toString() !=null || !columnValue.get(1).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| !columnValue.get(1).toString().isEmpty()) {				
				if(!columnValue.get(1).toString().replace(" ", "").trim().matches(alphanumaric)){
					String status="kindly check the Area Name Description : "+columnValue.get(1).toString();
					empReqExcelRows.put(rowId.concat(",1"), status);
				}
			}*/
			
			if (columnValue.get(2).toString().equalsIgnoreCase("CELL_TYPE_BLANK") ||columnValue.get(2).toString() ==null || columnValue.get(2).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(2).toString().isEmpty()) {
				 Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Route Name value : "+columnValue.get(2).toString();
				empReqExcelRows.put("RNo",rowId.concat(",2"));
				empReqExcelRows.put("IssueStatus",status);
				 childRowResponse.add(empReqExcelRows);
			}
			return childRowResponse;	
}
    
    /*
	 * @xl utility row values are inserted into Area master table  & route master table. 
	 * validation also be handle here.
	 * 
	 */
	private List<Map<String, Object>> insertNodalRouteRecords(Map<Integer, Object> areaRecordExcelDetails,int branchId,int userId,String combinedFacility) throws ParseException {				
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		 IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
	     IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 String status="success-";
	     List<Map<String, Object>> areaMasterExcel = new ArrayList<Map<String, Object>>();
		    Map<String, Object> issueList = new HashMap<String, Object>();
	        try {
	        	for (Entry<Integer, Object> entry : areaRecordExcelDetails.entrySet()) {
					issueList = new HashMap<String, Object>();                
					ArrayList areaExcelDetails = (ArrayList) entry.getValue();
			 if(areaExcelDetails.get(0).toString().isEmpty() || areaExcelDetails.get(1).toString().isEmpty() || areaExcelDetails.get(2).toString().isEmpty() || areaExcelDetails.get(3).toString().isEmpty()){
				 	 status="AreaName,Nodal PointName,Nodal RouteName and NodalPointGeoCodes should not be blank";
				 	 issueList.put("RNo","");
					 issueList.put("IssueStatus",status);
					 areaMasterExcel.add(issueList);
					 return areaMasterExcel;
				}
			 
			 List<EFmFmFacilityToFacilityMappingPO> branchDetails=facilityBO.getParticularFacilityDetailFromBranchName(areaExcelDetails.get(4).toString().trim());
		        if(branchDetails.isEmpty()){
		            status = "Please check-" + areaExcelDetails.get(4).toString().trim()+" facility not exist.";
		            issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					areaMasterExcel.add(issueList);
		            return areaMasterExcel;
		        }
		        else if(!(branchDetails.isEmpty()) && branchDetails.get(0).getFacilityStatus().equalsIgnoreCase("N") ){
		            status = "Please check-" + areaExcelDetails.get(4).toString().trim()+" facility is  disable.";
		            issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					areaMasterExcel.add(issueList);
		            return areaMasterExcel;
		        }
		        
			    int baseBranchId=iUserMasterBO.getBranchIdFromBranchName(areaExcelDetails.get(4).toString().trim());
			    EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
			    eFmFmClientBranch.setBranchId(baseBranchId);
		       if(!(facilityBO.checkFacilityAccess(userId, baseBranchId))){
		           status = "Please check looks like you don't have access of -" + areaExcelDetails.get(4).toString().trim()+" facility";
		           issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					areaMasterExcel.add(issueList);
		           return areaMasterExcel; 
		       }
			 
			 
			 
			    EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO=new EFmFmRouteAreaMappingPO();
			 	EFmFmAreaMasterPO areaMasterPO = new EFmFmAreaMasterPO();
			 	areaMasterPO.setAreaName(areaExcelDetails.get(0).toString().toUpperCase().trim());	
			 	areaMasterPO.setAreaDescription(areaExcelDetails.get(0).toString().toUpperCase().trim());
				EFmFmZoneMasterPO routeMasterPO = new EFmFmZoneMasterPO();
				routeMasterPO.setZoneName(areaExcelDetails.get(2).toString().toUpperCase().trim());
				routeMasterPO.setStatus("Y");
				routeMasterPO.setCreationTime(new Date());
				routeMasterPO.setUpdatedTime(new Date());
				routeMasterPO.setNodalRoute(true);
				EFmFmAreaNodalMasterPO eFmFmNodalAreaMaster=new EFmFmAreaNodalMasterPO();				
				eFmFmNodalAreaMaster.setNodalPointName(areaExcelDetails.get(1).toString().toUpperCase().trim());
				eFmFmNodalAreaMaster.setNodalPoints(areaExcelDetails.get(3).toString().trim().replaceAll("\\s+",""));
				eFmFmNodalAreaMaster.setNodalPointsAddress(areaExcelDetails.get(1).toString().toUpperCase().trim());
				eFmFmNodalAreaMaster.setNodalPointDescription(areaExcelDetails.get(1).toString().toUpperCase().trim());
				eFmFmNodalAreaMaster.setNodalPointFlg("E");
			 	if(!areaMasterPO.getAreaName().isEmpty() && areaMasterPO.getAreaName().trim() !="" && !areaExcelDetails.get(2).toString().trim().isEmpty() && areaExcelDetails.get(2).toString().trim()!=""){
			 		List<EFmFmAreaMasterPO> areaExist=iRouteDetailBO.getAllAreaName(areaMasterPO.getAreaName().trim().toUpperCase().replaceAll("\\s",""));    
			 	    if(areaExist.isEmpty()){    
			 	     iRouteDetailBO.saveAreaRecord(areaMasterPO);
			 	      areaExist=iRouteDetailBO.getAllAreaName(areaMasterPO.getAreaName().trim().toUpperCase().replaceAll("\\s",""));
			 	    }		 	     			 	   		
				//validating: route already existing on table.
				List<EFmFmZoneMasterPO> routeExist=iRouteDetailBO.getAllRouteName(routeMasterPO.getZoneName().trim().toUpperCase());				
				if(routeExist.isEmpty()){						
					iRouteDetailBO.saveRouteNameRecord(routeMasterPO);	
					routeExist=iRouteDetailBO.getAllRouteName(routeMasterPO.getZoneName().trim().toUpperCase());
				}
				
		        List<EFmFmAreaNodalMasterPO> allNodalPoints = iRouteDetailBO
		                .getNodalPointsFromNodalName(areaExcelDetails.get(1).toString().toUpperCase().trim());
		        if (allNodalPoints.isEmpty()) {
			 	     iRouteDetailBO.save(eFmFmNodalAreaMaster);   
			 	    allNodalPoints = iRouteDetailBO
			                .getNodalPointsFromNodalName(areaExcelDetails.get(1).toString().toUpperCase().trim());
		        }
				eFmFmRouteAreaMappingPO.setEfmFmAreaMaster(areaExist.get(0));
				eFmFmRouteAreaMappingPO.seteFmFmZoneMaster(routeExist.get(0));	
				eFmFmRouteAreaMappingPO.seteFmFmNodalAreaMaster(allNodalPoints.get(0));
				
				EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO=new EFmFmClientRouteMappingPO();
				eFmFmClientRouteMappingPO.seteFmFmZoneMaster(routeExist.get(0));
				EFmFmClientBranchPO eFmFmbranchMaster=new EFmFmClientBranchPO();
				eFmFmbranchMaster.setCombinedFacility(combinedFacility);	
				eFmFmbranchMaster.setBranchId(baseBranchId);				
				eFmFmClientRouteMappingPO.seteFmFmClientBranchPO(eFmFmbranchMaster);
				eFmFmClientRouteMappingPO.setCombinedFacility(combinedFacility);
				List<EFmFmClientRouteMappingPO> clientRouteMappingPO=iRouteDetailBO.clientRouteMappaingAlreadyExist(eFmFmClientRouteMappingPO);
				if(clientRouteMappingPO.isEmpty()){				
					iRouteDetailBO.saveClientRouteMapping(eFmFmClientRouteMappingPO);
					iRouteDetailBO.saveRouteMappingDetails(eFmFmRouteAreaMappingPO);
				}
				
			 	}
			 	
         }
	        }
         catch(Exception e){
 			status="Kinldy check the file format,System will except only excel format without formula and special symbols.";  	                    		
		 	 issueList.put("RNo","");
			 issueList.put("IssueStatus",status);
			 areaMasterExcel.add(issueList);
        	 log.info("Error nodal "+e);
         }		
		 return areaMasterExcel;
	}
    
    
	/*
	 * @xl utility row values are inserted into Area master table  & route master table. 
	 * validation also be handle here.
	 * 
	 */
	private List<Map<String, Object>> areaRecord(Map<Integer, Object> areaRecordExcelDetails,int branchId,int profileId,String combinedFacility) throws ParseException {				
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");	
		 IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
	        IUserMasterBO iUserMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 String status="success-";
	     List<Map<String, Object>> areaMasterExcel = new ArrayList<Map<String, Object>>();
		    Map<String, Object> issueList = new HashMap<String, Object>();
	        try {
	        	for (Entry<Integer, Object> entry : areaRecordExcelDetails.entrySet()) {
					issueList = new HashMap<String, Object>();                
					ArrayList areaExcelDetails = (ArrayList) entry.getValue();
			 if(areaExcelDetails.get(0).toString().isEmpty() || areaExcelDetails.get(2).toString().isEmpty()){
				 	 status="AreaName and Zone Name should not be blank";
				 	 issueList.put("RNo","");
					 issueList.put("IssueStatus",status);
					 areaMasterExcel.add(issueList);
					 return areaMasterExcel;
				}

			 
			 List<EFmFmFacilityToFacilityMappingPO> branchDetails=facilityBO.getParticularFacilityDetailFromBranchName(areaExcelDetails.get(3).toString().trim());
		        if(branchDetails.isEmpty()){
		            status = "Please check-" + areaExcelDetails.get(3).toString().trim()+" facility not exist.";
		            issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					areaMasterExcel.add(issueList);
		            return areaMasterExcel;
		        }
		        else if(!(branchDetails.isEmpty()) && branchDetails.get(0).getFacilityStatus().equalsIgnoreCase("N") ){
		            status = "Please check-" + areaExcelDetails.get(3).toString().trim()+" facility is  disable.";
		            issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					areaMasterExcel.add(issueList);
		            return areaMasterExcel;
		        }
		        
			    int baseBranchId=iUserMasterBO.getBranchIdFromBranchName(areaExcelDetails.get(3).toString().trim());
			    EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
			    eFmFmClientBranch.setBranchId(baseBranchId);
		       if(!(facilityBO.checkFacilityAccess(profileId, baseBranchId))){
		           status = "Please check looks like you don't have access of -" + areaExcelDetails.get(3).toString().trim()+" facility";
		           issueList.put("RNo","");
					issueList.put("IssueStatus",status);
					areaMasterExcel.add(issueList);
		           return areaMasterExcel; 
		       }
			 
			    EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO=new EFmFmRouteAreaMappingPO();
			 	EFmFmAreaMasterPO areaMasterPO = new EFmFmAreaMasterPO();
				EFmFmClientBranchPO eFmFmbranchMaster=new EFmFmClientBranchPO();				
				eFmFmbranchMaster.setBranchId(baseBranchId);				

			 	areaMasterPO.setAreaName(areaExcelDetails.get(0).toString().toUpperCase().trim());	
			 	if(areaExcelDetails.get(1).toString().equalsIgnoreCase("CELL_TYPE_BLANK")){
			 	 	areaMasterPO.setAreaDescription("");
				 }else{				 
					 areaMasterPO.setAreaDescription(areaExcelDetails.get(1).toString().trim());
				 }
			 	if(!areaMasterPO.getAreaName().isEmpty() && areaMasterPO.getAreaName() !="" && !areaExcelDetails.get(2).toString().trim().isEmpty() && areaExcelDetails.get(2).toString().trim()!=""){
			 		List<EFmFmAreaMasterPO> areaExist=iRouteDetailBO.getAllAreaName(areaMasterPO.getAreaName().trim().toUpperCase().replaceAll("\\s",""));    
			 	    if(areaExist.isEmpty()){    
			 	     iRouteDetailBO.saveAreaRecord(areaMasterPO);   
			 	     
			 	    }		 	     
			 	    List<EFmFmAreaMasterPO> eFmFmAreaMasterPO=iRouteDetailBO.getAllAreaName(areaMasterPO.getAreaName().trim().toUpperCase().replaceAll("\\s",""));
			 	    if(!(eFmFmAreaMasterPO.isEmpty())){
			 	      areaMasterPO.setAreaId(eFmFmAreaMasterPO.get(0).getAreaId());					 	      
			 	     List<EFmFmClientAreaMappingPO> eFmFmClientAreaMappingPO=iRouteDetailBO.getClientAreaMappaingData(eFmFmAreaMasterPO.get(0).getAreaId(),baseBranchId);
			 	    	if(eFmFmClientAreaMappingPO.isEmpty()){
					 	      EFmFmClientAreaMappingPO clientAreaMapping=new EFmFmClientAreaMappingPO();
						 	  clientAreaMapping.seteFmFmAreaMaster(areaMasterPO);
						 	  clientAreaMapping.setFacilityWiseDistance(Double.parseDouble(areaExcelDetails.get(4).toString().trim()));
						 	  clientAreaMapping.seteFmFmClientBranchPO(eFmFmbranchMaster);
						 	 iRouteDetailBO.save(clientAreaMapping);			 	
			 	    	}			 	    		 
			 	    }
				EFmFmZoneMasterPO routeMasterPO = new EFmFmZoneMasterPO();
				routeMasterPO.setZoneName(areaExcelDetails.get(2).toString().toUpperCase().trim());
				routeMasterPO.setStatus("Y");
				routeMasterPO.setCreationTime(new Date());
				routeMasterPO.setUpdatedTime(new Date());
				//validating: route already existing on table.
				List<EFmFmZoneMasterPO> routeExist=iRouteDetailBO.getAllRouteName(routeMasterPO.getZoneName().trim().toUpperCase());				
				if(routeExist.isEmpty()){					
					iRouteDetailBO.saveRouteNameRecord(routeMasterPO);					
				}
				List<EFmFmZoneMasterPO> eFmFmRouteMasterPO=iRouteDetailBO.getAllRouteName(routeMasterPO.getZoneName().trim().toUpperCase());
				if(!(eFmFmRouteMasterPO.isEmpty())){
						routeMasterPO.setZoneId(eFmFmRouteMasterPO.get(0).getZoneId());
				}
				EFmFmAreaNodalMasterPO eFmFmNodalAreaMaster=new EFmFmAreaNodalMasterPO();
				eFmFmNodalAreaMaster.setNodalPointId(1);
				eFmFmRouteAreaMappingPO.setEfmFmAreaMaster(areaMasterPO);
				eFmFmRouteAreaMappingPO.seteFmFmZoneMaster(routeMasterPO);	
				eFmFmRouteAreaMappingPO.seteFmFmNodalAreaMaster(eFmFmNodalAreaMaster);
								
				List<EFmFmClientRouteMappingPO> clientRouteMappingPO=iRouteDetailBO.getClientRouteMappaingAlreadyExistCheck(routeMasterPO.getZoneId(),baseBranchId);
				if(clientRouteMappingPO.isEmpty()){	
					EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO=new EFmFmClientRouteMappingPO();
					eFmFmClientRouteMappingPO.seteFmFmZoneMaster(routeMasterPO);
					eFmFmClientRouteMappingPO.seteFmFmClientBranchPO(eFmFmbranchMaster);
					iRouteDetailBO.saveClientRouteMapping(eFmFmClientRouteMappingPO);
					iRouteDetailBO.saveRouteMappingDetails(eFmFmRouteAreaMappingPO);
				}
				
			 	}
			 	
         }
	        }
         catch(Exception e){
        	 log.info("Error"+e);
         }		
		 return areaMasterExcel;
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
