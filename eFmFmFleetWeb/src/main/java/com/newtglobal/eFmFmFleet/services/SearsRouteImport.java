package com.newtglobal.eFmFmFleet.services;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.junit.internal.matchers.CombinableMatcher;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IEmployeeDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaNodalMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeRequestMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripTimingMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmZoneMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/searsxlImport")
public class SearsRouteImport {
	private static Log log = LogFactory.getLog(SearsRouteImport.class);
	DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

  
   /*
     * Sears Development
     */
    

    @POST
    @Path("/searsEmpRoaster")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response upload(@FormDataParam("filename") InputStream uploadedInputStream,
            @FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
            @QueryParam("combinedFacility") String combinedFacility,
            @Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException, InvalidKeyException,
                    NoSuchAlgorithmException, URISyntaxException {        
        String status = "success"; 
        log.info("serviceStart -UserId :" +userId);
        int noOfcolumn=12; 
        List<Map<String, Object>> response =new ArrayList<Map<String, Object>>(); 
        Map<String, Object>  empSearsOutSideExcel= new HashMap<String, Object>();
        SimpleDateFormat todayFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat shifTimeFormate = new SimpleDateFormat("HH:mm");
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
        List<String> vehicleNumber =new ArrayList<String>();        
        List<String> ShiftTime =new ArrayList<String>(); 
        List<String> tripType =new ArrayList<String>(); 
        List<String> driverMobileNumber =new ArrayList<String>(); 
        
        
        try {
        	Map<Integer, Object>  searEmpDetails= new HashMap<Integer, Object>();
	           XSSFWorkbook workbook = new XSSFWorkbook(uploadedInputStream);
	           XSSFSheet sheet = workbook.getSheetAt(0);
	           int rowId=0;	          
	           for (int rowNum = 0; rowNum <=sheet.getLastRowNum(); rowNum++) {
	        	   Map<String, Object>  empSearsExcel= new HashMap<String, Object>();
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
	                    		if(!searsEmpRoasterValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,userId,String.valueOf(branchId)).isEmpty()){                		
	                    			response.addAll(searsEmpRoasterValidator(Integer.toString(rowId),columnValue,branchId,noOfcolumn,userId,String.valueOf(branchId)));	                    			
	                    		}else{	                    			
	                    				if(!vehicleNumber.contains(columnValue.get(0).toString().trim())){
	                    					vehicleNumber.add(columnValue.get(0).toString().trim());
	                    				}
	                    				if(!ShiftTime.contains(columnValue.get(4).toString().trim())){
	                    					ShiftTime.add(columnValue.get(4).toString().trim());
	                    				}	                    				
	                    				if(!driverMobileNumber.contains(columnValue.get(8).toString().trim())){
	                    					driverMobileNumber.add(columnValue.get(8).toString().trim());
	                    				} 
	                    				
	                    				if(!tripType.contains(columnValue.get(9).toString().trim())){
	                    					tripType.add(columnValue.get(9).toString().trim());
	                    				}        				
	                    				
	                    				searEmpDetails.put(rowId, columnValue);                    		
	                    		}
	                    	}else{
	                    		status="Kinldy check the no -of Column -12 Entered Column No:"+columnValue.size();
	                    		empSearsExcel.put("RNo",Integer.toString(rowId));
	                    		empSearsExcel.put("IssueStatus",status);
	                    		response.add(empSearsExcel);
	                    	}
                    	}else{
                    		  if(columnValue.size()<12){                			              			
                    			    status="Kinldy check the no -of Column's ";
                    			    empSearsExcel.put("RNo",Integer.toString(rowId));
                    			    empSearsExcel.put("IssueStatus",status);
	  	                    		response.add(empSearsExcel);
                    		}else{
                    			//Need to configure below Text some where
                    			if(!"CabNo".equalsIgnoreCase(columnValue.get(0).toString().replace(" ","").trim()) 
                    					|| !"Vendor".equalsIgnoreCase(columnValue.get(1).toString().replace(" ","").trim()) 
                    					|| !"EMPID".equalsIgnoreCase(columnValue.get(2).toString().replace(" ","").trim())
                    					|| !"EmpName".equalsIgnoreCase(columnValue.get(3).toString().replace(" ","").trim()) 
                    					|| !("ShiftEndsOn".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim()) || "ShiftStartOn".equalsIgnoreCase(columnValue.get(4).toString().replace(" ","").trim()) ) 
                    					|| !"Address".equalsIgnoreCase(columnValue.get(5).toString().replace(" ","").trim()) 
                            			|| !"Area".equalsIgnoreCase(columnValue.get(6).toString().replace(" ","").trim()) 
                            			|| !"NodalPoint".equalsIgnoreCase(columnValue.get(7).toString().replace(" ","").trim()) 
                            			|| !"ContactNo.".equalsIgnoreCase(columnValue.get(8).toString().replace(" ","").trim())
                            			|| !"Type".equalsIgnoreCase(columnValue.get(9).toString().replace(" ","").trim())
                    					|| !"Process".equalsIgnoreCase(columnValue.get(10).toString().replace(" ","").trim()) 
                    					|| !"G".equalsIgnoreCase(columnValue.get(11).toString().replace(" ","").trim())){ 
                    				
                    				    status="Kinldy check the Column Names & No of Column's"+columnValue.size();
                    				    empSearsExcel.put("RNo",Integer.toString(rowId));
                    				    empSearsExcel.put("IssueStatus",status);
    	  	                    		response.add(empSearsExcel);
    	  	                    		log.info("serviceEnd -UserId :" +userId);
    	  	                    		return Response.ok(response, MediaType.APPLICATION_JSON).build();
                    				    
                    			}       			                			
                    		}
                    	}
    				}              
                }
               // log.info("Size"+searEmpDetails.size());
    			if(response.isEmpty()){
    				if(!searEmpDetails.isEmpty()){
    					response=employeeTravelRequest(searEmpDetails,branchId,combinedFacility);  
    					if(response.isEmpty()){					
	    			        if((vehicleNumber.size()>0) && (ShiftTime.size()==1) && (driverMobileNumber.size()>0) && (tripType.size()==1)){    			        	
	    			        	EFmFmEmployeeTravelRequestPO travelRequestPO=new EFmFmEmployeeTravelRequestPO();
	    			        	travelRequestPO.setExecutionDate(todayFormat.format(new Date()));
	    			        	travelRequestPO.setBranchId(branchId);
	    			        	travelRequestPO.setZoneId(0);	    			        	
	    						String shiftTimeInDateTimeFormate =ShiftTime.get(0);
	    					    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
	    					    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());	    			        	
	    			        	travelRequestPO.setTime(shifTimeFormate.format(shiftTime));	    			        	
	    			        	log.info("Sifttime"+travelRequestPO.getTime());
	    			        	if(tripType.get(0).toUpperCase().trim().equalsIgnoreCase("Dp")){    			            	
	    			            	travelRequestPO.setTripType("DROP");
	    			            }else{
	    			            	travelRequestPO.setTripType("PICKUP");    			            
	    			            }    			        	
	    			        	response=getAllRequestOfParticularShiftAndRouteForAllocation(travelRequestPO);
	    			        	/*if(response.isEmpty()){*/
	    			        	vehicleAllocation(vehicleNumber,driverMobileNumber,branchId,combinedFacility);
	    			        	/*}*/
	    			        }else{
	    			        	 if(vehicleNumber.size() != driverMobileNumber.size()){	    			        		 
	    			        		 status=" Vehilce Count and Driver Count should be equal, Vehicle Count is "+ 
	    			        		 vehicleNumber.size() +" & Driver Count is "+ driverMobileNumber.size();
	    			        	 }else if (tripType.size() != ShiftTime.size()) {
	    			        		 status=" TripType Count and ShifTime Count should be equal, ShifTime Count is "+ 
	    			        				 ShiftTime.size() +" & Trip Type Count is "+ tripType.size();
	    			        	 }	    			        	 
	    			        	 empSearsOutSideExcel.put("RNo",Integer.toString(rowId));
	    			        	 empSearsOutSideExcel.put("IssueStatus",status);
	  	                    	 response.add(empSearsOutSideExcel);
	    			        }
    				   }
    			     }
    			}

            } catch (Exception e) {
            	e.printStackTrace();
                log.info("Exception In side Excel Upload" + e); 
                log.info("serviceEnd -UserId :" +userId);
                return Response.ok(response, MediaType.APPLICATION_JSON).build();

            }
        	log.info("serviceEnd -UserId :" +userId);
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        
    
    /*
	 * searsMaster Uplaod
	 */
    private List<Map<String, Object>> searsEmpRoasterValidator(String rowId,ArrayList<Object> columnValue,int branchId,int noOfcolumn,int userId ,String combinedFacility) throws UnsupportedEncodingException{
	    IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext().getBean("IEmployeeDetailBO");
	    IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
	    ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
	    IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
	    //Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();   	
	    DateFormat shifTimeFormate = new SimpleDateFormat("HH:mm");	
	    List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();
	    List<EFmFmZoneMasterPO> routeExistDetail;
	    String validInt="^[0-9]*$";
	    String charAt="^[a-zA-Z]*$";	    
	    try{		
	    if (columnValue.get(0).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(0).toString() ==null || columnValue.get(0).toString().replace(".0","").trim().equalsIgnoreCase("") 
					|| columnValue.get(0).toString().isEmpty()) {
	    	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
	    		String status="kindly check the cab Number : "+columnValue.get(0).toString();
				empReqExcelRows.put("RNo",rowId.concat(",0"));
				empReqExcelRows.put("IssueStatus",status);	
				childRowResponse.add(empReqExcelRows);
			}else{
				//cab No validation
				List<EFmFmVehicleMasterPO> vehicleDetails = vehicleCheckInBO.getVehicleDetailsFromVehicleNumber(columnValue.get(0).toString().trim(),combinedFacility);				
			    if (vehicleDetails.isEmpty()) {
			    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			    	String status = "Cab Number not available -" + columnValue.get(0).toString().trim();
			    	empReqExcelRows.put("RNo",rowId.concat(",0"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
			    } 
			    
			}
			
	    if (columnValue.get(1).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(1).toString() ==null || columnValue.get(1).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(1).toString().isEmpty() ) {
	    		Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the Vendor Name column value : "+columnValue.get(1).toString();
				empReqExcelRows.put("RNo",rowId.concat(",1"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}else{
				//vendor Name validation				
			    
			}
			
	    if (columnValue.get(2).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(2).toString() ==null || columnValue.get(2).toString().replace(".0","").trim().equalsIgnoreCase("") 
					|| columnValue.get(2).toString().isEmpty()) {
	    	    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
	    		String status="kindly check the Employee Id : "+columnValue.get(2).toString();
				empReqExcelRows.put("RNo",rowId.concat(",2"));
				empReqExcelRows.put("IssueStatus",status);	
				childRowResponse.add(empReqExcelRows);
			}else{		
				List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO.getEmpDetailsFromEmployeeId(columnValue.get(2).toString().trim());
			    if (employeeDetails.isEmpty()) {
			    	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			    	String status = "Employee Id not available -" + columnValue.get(2).toString().trim();
			    	empReqExcelRows.put("RNo",rowId.concat(",2"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
			    } 
			    
			}
	    
	    
	    if (columnValue.get(3).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(1).toString() ==null || columnValue.get(3).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
				|| columnValue.get(3).toString().isEmpty() ) {
    		Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			String status="kindly check the Employee Name column value : "+columnValue.get(3).toString();
			empReqExcelRows.put("RNo",rowId.concat(",3"));
			empReqExcelRows.put("IssueStatus",status);
			childRowResponse.add(empReqExcelRows);
		}
	
	    
	    if (columnValue.get(4).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(4).toString() ==null || columnValue.get(4).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(4).toString().isEmpty()) {
	    		Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String status="kindly check the shiftTime : "+columnValue.get(4).toString();
				empReqExcelRows.put("RNo",rowId.concat(",4"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
				
			}else{
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				String tripType="";	                     	
	            	if(columnValue.get(9).toString().toUpperCase().trim().equalsIgnoreCase("Dp") || columnValue.get(9).toString().toUpperCase().trim().equalsIgnoreCase("DROP")){ 
	            		tripType="DROP";
	            		
	            	}else if (columnValue.get(9).toString().toUpperCase().trim().equalsIgnoreCase("Pu") || columnValue.get(9).toString().toUpperCase().trim().equalsIgnoreCase("PICKUP")){
	            		tripType="PICKUP";
	            	}else{
	            		String status="kindly check the tripType : "+columnValue.get(9).toString();
						empReqExcelRows.put("RNo",rowId.concat(",9"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows); 
	            	}
	            
	            try{
					String shiftTimeInDateTimeFormate = columnValue.get(4).toString();
				    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
				    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());	
				    List<EFmFmTripTimingMasterPO>   shifTimeCount=iCabRequestBO.getShiftTimeDetailFromShiftTimeAndTripType
				    		(new MultifacilityService().combinedBranchIdDetails(userId,combinedFacility),shiftTime,tripType);
				    if(shifTimeCount.isEmpty()){				 
						String status="The Combination of ShiftTime & TripType is not available  : "+tripType +" : "+shiftTime ;
						empReqExcelRows.put("RNo",rowId.concat(",4"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);  
				    }			    
					}catch (Exception e){
						log.info("Error"+e);
						String status="kindly check the employee shiftTime Time format:'HH:MM:SS' ex:16:00:00"+e;
						empReqExcelRows.put("RNo",rowId.concat(",4"));
				    	empReqExcelRows.put("IssueStatus",status);
				    	childRowResponse.add(empReqExcelRows);
					}	
			}
			
			
	    if (columnValue.get(5).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(5).toString() ==null || columnValue.get(5).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(5).toString().isEmpty()) {
				String status="kindly check the employeeAddress : "+columnValue.get(5).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",5"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
			
	    if (columnValue.get(6).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(6).toString() ==null || columnValue.get(6).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(6).toString().isEmpty()) {
				String status="kindly check the AreaName : "+columnValue.get(6).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",6"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
	    }
	    
	    if (columnValue.get(7).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(7).toString().trim() ==null||columnValue.get(7).toString().replace(".0"," ").trim().equalsIgnoreCase("") 
					|| columnValue.get(7).toString().replace(" ", "").trim().isEmpty()){
						String status="kindly check the Employee Nodal Point : "+columnValue.get(7).toString();
						Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
						empReqExcelRows.put("RNo",rowId.concat(",7"));
						empReqExcelRows.put("IssueStatus",status);
						childRowResponse.add(empReqExcelRows);
			}	
	    
	    
	    List<EFmFmZoneMasterPO> routeAreaExistDetail;	    
	    if (columnValue.get(7).toString().trim().equalsIgnoreCase("default")) {	    	
	    	routeAreaExistDetail = iRouteDetailBO.getNonNodalRouteNameFromClientIdAndRouteName(combinedFacility,columnValue.get(7).toString().trim());
	    } else {
	    	routeAreaExistDetail = iRouteDetailBO.getNodalRouteNameFromClientIdAndRouteName(combinedFacility,columnValue.get(6).toString().trim());
	    }			
	    if (routeAreaExistDetail.isEmpty()
	            && (columnValue.get(7).toString().trim().equalsIgnoreCase("default"))) {
	        String  status = "Normal Route Name not available -" + columnValue.get(6).toString().trim();
			Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			empReqExcelRows.put("RNo",rowId.concat(",6"));
			empReqExcelRows.put("IssueStatus",status);
			childRowResponse.add(empReqExcelRows);
	    }	    
	    if (routeAreaExistDetail.isEmpty()
	            && !(columnValue.get(7).toString().trim().equalsIgnoreCase("default"))) {	        
	        String   status = "Nodal Route Name not available-" + columnValue.get(6).toString().trim();
	     			Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
	     			empReqExcelRows.put("RNo",rowId.concat(",6"));
	     			empReqExcelRows.put("IssueStatus",status);
	     			childRowResponse.add(empReqExcelRows);

	    }
	    List<EFmFmAreaNodalMasterPO> nodalPointExistDetail = iRouteDetailBO
	            .getParticularNodalPointNameDetails(columnValue.get(7).toString().trim());
	    if (nodalPointExistDetail.isEmpty()) {	       
	        String status = "Nodal Point not available-" + columnValue.get(7).toString().trim();
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
					 //driver Mobile Number Validation	
				List<EFmFmDriverMasterPO> driverDetails = vehicleCheckInBO.getParticularDriverDetailsFromMobileNum
						(columnValue.get(8).toString().trim(),branchId);
					if(driverDetails.isEmpty()){
						String status="kindly check the Mobile Number and Mobile Number length "+columnValue.get(8).toString();
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
				String status="kindly check the Trip Type : "+columnValue.get(9).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",9"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
			
	    if (columnValue.get(10).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(10).toString() ==null || columnValue.get(10).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
					|| columnValue.get(10).toString().isEmpty()) {
				String status="kindly check the State Name : "+columnValue.get(10).toString();
				Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
				empReqExcelRows.put("RNo",rowId.concat(",10"));
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
			}
			
				if (columnValue.get(11).toString().equalsIgnoreCase("CELL_TYPE_BLANK")||columnValue.get(11).toString() ==null || columnValue.get(11).toString().replace(".0", " ").trim().equalsIgnoreCase("") 
						|| columnValue.get(11).toString().isEmpty() || !columnValue.get(11).toString().replace(" ", "").trim().matches(charAt)) {
										Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
										String status="kindly check the employee sex  : "+columnValue.get(11).toString();
										empReqExcelRows.put("RNo",rowId.concat(",11"));
										empReqExcelRows.put("IssueStatus",status);
										childRowResponse.add(empReqExcelRows);
					
				}else if (!("F".equalsIgnoreCase(columnValue.get(11).toString().trim()) || "M".equalsIgnoreCase(columnValue.get(11).toString().trim()))){
					String	status="kindly check the employee sex : "+columnValue.get(11).toString();
					Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
					empReqExcelRows.put("RNo",rowId.concat(",11"));
					empReqExcelRows.put("IssueStatus",status);
					childRowResponse.add(empReqExcelRows);
				}			
				
			
    }catch(Exception e){
		  	Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();
			    String status="Please check Sear employee data,should not contain  blank columns and special symbols like ',@ etc.Please check zoneName and other columns For the same";
				empReqExcelRows.put("IssueStatus",status);
				childRowResponse.add(empReqExcelRows);
		        log.info("ImportEmploye empReqExcelRows Exception" + e); 
		        return childRowResponse;

    }
   return childRowResponse;
	
}
    
    
private List<Map<String, Object>> employeeTravelRequest(Map<Integer, Object> empReqDetails, int branchId,String combinedFacility){
	IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
		            .getBean("IEmployeeDetailBO");
		    ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		    IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext()
		            .getBean("IRouteDetailBO");
		    DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			SimpleDateFormat todayFormat = new SimpleDateFormat("dd-MM-yyyy");
		    DateFormat dateHypenFormat = new SimpleDateFormat("dd-MM-yyyy");
		    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		    DateFormat shifTimeFormate = new SimpleDateFormat("HH:mm");
		    List<Map<String, Object>> empRequestExcel = new ArrayList<Map<String, Object>>();
		    Map<String, Object> issueList = new HashMap<String, Object>();
		    log.info("Emp_ID");	
		    String status="";
		    int dropseqenceOrder=0;
		    
   		try{							
			  for (Entry<Integer, Object> entry : empReqDetails.entrySet()) {
						issueList = new HashMap<String, Object>();                
						ArrayList columnValue = (ArrayList) entry.getValue();		
				dropseqenceOrder=dropseqenceOrder+1;
			    String shiftTimeInDateTimeFormate = columnValue.get(4).toString();
			    String shifTimeSplit[] = shiftTimeInDateTimeFormate.split("\\s+");
			    java.sql.Time shiftTime = new java.sql.Time(shifTimeFormate.parse(shifTimeSplit[1]).getTime());			    
                Date requestDate = todayFormat.parse(todayFormat.format(new Date())); 
                
			    log.info("Date" + requestDate);
			    log.info("shiftTime" + shiftTime);
			    log.info("CurrentTime" + new Date());
			    String reqDate = dateHypenFormat.format(requestDate) + " " + shifTimeFormate.format(shiftTime);
			    log.info("reqDatereqDate" + reqDate);
			    log.info(requestDate.getTime() + shiftTime.getTime() + "ColumnValue:-" + columnValue);
			    log.info("reqq time.." + dateTimeFormate.parse(reqDate).getTime());
			    
			    
				if (dateTimeFormate.parse(reqDate).getTime() <= new Date().getTime()) {
			        status ="kinldy check the requestDate-" + new Date()+ "EmpId "
			                + columnValue.get(2).toString();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();			        
			    }
			    List<EFmFmUserMasterPO> employeeDetails = iEmployeeDetailBO
			            .getEmpDetailsFromEmployeeId(columnValue.get(2).toString().trim());
			    if (employeeDetails.isEmpty()) {
			        status = "Employee Id is not available -" + columnValue.get(2).toString().trim();
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
			    List<EFmFmZoneMasterPO> routeExistDetail;
			    
			    if (columnValue.get(7).toString().trim().equalsIgnoreCase("default")) {
			        routeExistDetail = iRouteDetailBO.getNonNodalRouteNameFromClientIdAndRouteName(combinedFacility,
			        		columnValue.get(7).toString().trim());
			    } else {
			        routeExistDetail = iRouteDetailBO.getNodalRouteNameFromClientIdAndRouteName(combinedFacility,columnValue.get(6).toString().trim());
			    }			
			    if (routeExistDetail.isEmpty()
			            && (columnValue.get(7).toString().trim().equalsIgnoreCase("default"))) {
			        status = "Normal Route Name not available -" + columnValue.get(7).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }
			    
			    if (routeExistDetail.isEmpty()
			            && !(columnValue.get(7).toString().trim().equalsIgnoreCase("default"))) {
			        status = "Nodal Route Name not available-" + columnValue.get(6).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }
			    List<EFmFmAreaNodalMasterPO> nodalPointExistDetail = iRouteDetailBO
			            .getParticularNodalPointNameDetails(columnValue.get(7).toString().trim());
			    if (nodalPointExistDetail.isEmpty()) {
			        status = "Nodal Point not available-" + columnValue.get(7).toString().trim();
			        issueList.put("RNo","");
			        issueList.put("IssueStatus",status);
			        empRequestExcel.add(issueList);
			        return empRequestExcel;
			        //return Response.ok(status, MediaType.APPLICATION_JSON).build();
			    }
			    List<EFmFmAreaMasterPO> areaId = iRouteDetailBO
			            .getParticularAreaNameDetails(columnValue.get(6).toString().trim());
			    if (areaId.isEmpty()) {
			        EFmFmAreaMasterPO efmFmAreaMaster = new EFmFmAreaMasterPO();
			        efmFmAreaMaster.setAreaDescription(columnValue.get(6).toString().trim());
			        efmFmAreaMaster.setAreaName(columnValue.get(6).toString().trim());
			        iRouteDetailBO.saveAreaRecord(efmFmAreaMaster);
			        areaId = iRouteDetailBO.getParticularAreaNameDetails(columnValue.get(6).toString().trim());
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
//	columnValue.get(9).toString().toUpperCase().trim() ""
	            String tripType="";
	            if(columnValue.get(9).toString().toUpperCase().trim().equalsIgnoreCase("Dp")){
	            	tripType="DROP";
	            }else{
	            	tripType="PICKUP";
	            }
	            List<EFmFmEmployeeRequestMasterPO> employeeRequestMasterPickUp = iCabRequestBO
	                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(startDate, shiftTime,
	                    		combinedFacility,
	                    		employeeDetails.get(0).getUserId(),tripType);
			    
			    EFmFmUserMasterPO userMaster = new EFmFmUserMasterPO();
			    userMaster.setUserId(employeeDetails.get(0).getUserId());			
			    if (employeeRequestMasterPickUp.isEmpty()) {
			        EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMasterPO = new EFmFmEmployeeRequestMasterPO();
			        Date endDate = dateTimeFormate.parse(dateHypenFormat.format(requestDate) + " " + "23:59");
			        if (tripType.trim().equalsIgnoreCase("PICKUP")) {
			            String shiftPickUpTime = columnValue.get(4).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
			            eFmFmEmployeeRequestMasterPO.setPickUpTime(pickUpTime);
			        }
			        eFmFmEmployeeRequestMasterPO.setReadFlg("N");
			        eFmFmEmployeeRequestMasterPO.setRequestDate(requestDate);
			        eFmFmEmployeeRequestMasterPO.setRequestFrom("E");
			        if (columnValue.get(7).toString().trim().equalsIgnoreCase("default"))
			            eFmFmEmployeeRequestMasterPO.setRequestType("normal");
			        else {
			            eFmFmEmployeeRequestMasterPO.setRequestType("nodal");
			        }
			        eFmFmEmployeeRequestMasterPO.setShiftTime(shiftTime);
			        eFmFmEmployeeRequestMasterPO.setStatus("N");
			        eFmFmEmployeeRequestMasterPO.setTripRequestStartDate(requestDate);
			        eFmFmEmployeeRequestMasterPO.setTripRequestEndDate(endDate);
			        eFmFmEmployeeRequestMasterPO.setTripType(tripType);
			        eFmFmEmployeeRequestMasterPO.setEfmFmUserMaster(userMaster);
			        eFmFmEmployeeRequestMasterPO.seteFmFmRouteAreaMapping(routeAreaId.get(0));
			        if (tripType.equalsIgnoreCase("DROP")) {
			            eFmFmEmployeeRequestMasterPO
			                    .setDropSequence(dropseqenceOrder);//No Drop Sequence
			        }
			        iCabRequestBO.save(eFmFmEmployeeRequestMasterPO);
			        
//			        employeeRequestMasterPickUp = iCabRequestBO.getParticularRequestDetailFromUserIdAndTripType(
//			                employeeDetails.get(0).getUserId(), branchId, columnValue.get(6).toString().trim());
			        
			        employeeRequestMasterPickUp = iCabRequestBO
		                    .getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(startDate, shiftTime,
		                    		combinedFacility,
		                    		employeeDetails.get(0).getUserId(),tripType);    
			     		        
			    }
			
			    List<EFmFmEmployeeRequestMasterPO> employeeRequestMasterDetailForDrop = null;
			    //log.info(employeeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeDiffPickToDrop()+"Auto Drop type:-"+employeeDetails.get(0).geteFmFmClientBranchPO().getAutoDropRoster());			    
			    if (tripType.trim().equalsIgnoreCase("PICKUP") && employeeDetails.get(0).geteFmFmClientBranchPO().getAutoDropRoster().equalsIgnoreCase("Yes")) {
			        
//			    	
//			    	employeeRequestMasterDetailForDrop = iCabRequestBO
//			                .getParticularRequestDetailFromUserIdAndTripType(employeeDetails.get(0).getUserId(),
//			                        branchId, "DROP");
//			    	
		            long dropRequestDate = getDisableTime(employeeDetails.get(0).geteFmFmClientBranchPO().getShiftTimeDiffPickToDrop(), 0,startDate);
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
			            if (columnValue.get(7).toString().trim().equalsIgnoreCase("default"))
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
			            eFmFmEmployeeRequestMaster.seteFmFmRouteAreaMapping(routeAreaId.get(0));
			            eFmFmEmployeeRequestMaster.setEfmFmUserMaster(userMaster);
			            if (tripType.trim().equalsIgnoreCase("DROP")) {
			                eFmFmEmployeeRequestMaster
			                        .setDropSequence(1);//drop sequence
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
			            .getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(startDate,shiftTime, combinedFacility,
			                    employeeDetails.get(0).getUserId(),tripType);
			    if (travelRequestDetails.isEmpty()) {
			        EFmFmEmployeeTravelRequestPO travelRequestPO = new EFmFmEmployeeTravelRequestPO();
			        travelRequestPO.setApproveStatus("Y");
			        travelRequestPO.setIsActive("Y");
			        travelRequestPO.setReadFlg("Y");
			        travelRequestPO.setRoutingAreaCreation("Y");
			        travelRequestPO.setRequestDate(requestDate);
			        travelRequestPO.setRequestStatus("E");
			        travelRequestPO.setRoutingAreaCreation(columnValue.get(0).toString().trim());
			        if (columnValue.get(7).toString().trim().equalsIgnoreCase("default"))
			            travelRequestPO.setRequestType("normal");
			        else {
			            travelRequestPO.setRequestType("nodal");
			        }
			        travelRequestPO.setShiftTime(shiftTime);
			        travelRequestPO.setRoutingAreaCreation(columnValue.get(0).toString().trim());
			        travelRequestPO.seteFmFmEmployeeRequestMaster(employeeRequestMasterPickUp.get(0));
			        travelRequestPO.setEfmFmUserMaster(userMaster);
			        if (tripType.trim().equalsIgnoreCase("PICKUP")) {
			            String shiftPickUpTime = columnValue.get(4).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
			            travelRequestPO.setPickUpTime(pickUpTime);
			        }
			        travelRequestPO.setTripType(tripType.trim().toUpperCase());
			        travelRequestPO.setCompletionStatus("N");
			        if (tripType.trim().equalsIgnoreCase("DROP")) {
			            travelRequestPO.setDropSequence(dropseqenceOrder);//drop sequence
			        }
			        if(!routeAreaId.isEmpty()){
				        travelRequestPO.seteFmFmRouteAreaMapping(routeAreaId.get(0));
			        iCabRequestBO.save(travelRequestPO);
			        }
			    }
			    if (!(travelRequestDetails.isEmpty())) {		            
		            if (tripType.trim().equalsIgnoreCase("DROP")) {
		            	travelRequestDetails.get(0).setDropSequence(dropseqenceOrder);//drop sequence
			        }
		            else{
			            String shiftPickUpTime = columnValue.get(4).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
			            travelRequestDetails.get(0).setPickUpTime(pickUpTime);
		            }
		            if(!(routeAreaId.isEmpty())){
		            	travelRequestDetails.get(0).seteFmFmRouteAreaMapping(routeAreaId.get(0));
			            iCabRequestBO.update(travelRequestDetails.get(0));
			            }
			    }
			    if (tripType.equalsIgnoreCase("PICKUP") && employeeDetails.get(0).geteFmFmClientBranchPO().getAutoDropRoster().equalsIgnoreCase("Yes")) {
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
			            travelRequestPO.setRoutingAreaCreation(columnValue.get(0).toString().trim());
			            // adding pickup time for getting default sequence
			            // apposit to pickup.
			            String shiftPickUpTime = columnValue.get(4).toString();
			            String shifPickUpTimeSplit[] = shiftPickUpTime.split("\\s+");
			            java.sql.Time pickUpTime = new java.sql.Time(
			                    shifTimeFormate.parse(shifPickUpTimeSplit[1]).getTime());
			            travelRequestPO.setPickUpTime(pickUpTime);
			            if (columnValue.get(7).toString().trim().equalsIgnoreCase("default"))
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
				e.printStackTrace();
				issueList.put("RNo","");				
				issueList.put("IssueStatus","Please check employee data,should not contain  blank columns and special symbols like ',@ etc.");
				empRequestExcel.add(issueList);
	            log.info("ImportEmploye Request Exception" + e);
		        return empRequestExcel;
			}
			return empRequestExcel;
   }
    
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
    
    

public List<Map<String, Object>> getAllRequestOfParticularShiftAndRouteForAllocation(EFmFmEmployeeTravelRequestPO travelRequestPO)
				throws ParseException {
			ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
			IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
			IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
					.getBean("IVehicleCheckInBO");
			 List<Map<String, Object>> childRowResponse =new ArrayList<Map<String, Object>>();	    
			 log.info("serviceStart -UserId :" + travelRequestPO.getUserId());
			DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
			log.info("Zone Id " + travelRequestPO.getZoneId());
			log.info("Selected Shift Time Id " + travelRequestPO.getTime());
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = dateFormat.parse(travelRequestPO.getExecutionDate());
			DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat requestDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			String requestDate = requestDateFormat.format(date);
			String requestDateAndTime = requestDate + " " + travelRequestPO.getTime();
			String shiftTime = travelRequestPO.getTime();
			Date shift = shiftFormate.parse(shiftTime);
			java.sql.Time dateShiftTime = new java.sql.Time(shift.getTime());
			List<EFmFmEmployeeTravelRequestPO> travelDetails = null;
			String response="failure";
			if (travelRequestPO.getZoneId() == 0) {
				if(travelRequestPO.getTripType().equalsIgnoreCase("PICKUP")){
					travelDetails = iCabRequestBO.assignCabToPickupShiftOrDateEmployees(requestDate,
							new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), travelRequestPO.getTripType(), dateShiftTime);
				}
				else{
					travelDetails = iCabRequestBO.assignCabToDropShiftOrDateEmployees(requestDate,
							new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), travelRequestPO.getTripType(), dateShiftTime);

				}
			} else {
				if(travelRequestPO.getTripType().equalsIgnoreCase("PICKUP")){
					travelDetails=iCabRequestBO.assignCabRequestToParticularShiftOrRouteEmployees(requestDate,new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()),travelRequestPO.getTripType(),dateShiftTime, travelRequestPO.getZoneId());		
				}
				else{
					travelDetails=iCabRequestBO.assignCabRequestToParticularShiftOrRouteEmployees(requestDate,new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()),travelRequestPO.getTripType(),dateShiftTime, travelRequestPO.getZoneId());		
				}
			}
		
			log.info("traveler count " + travelRequestPO.getZoneId());
			if (!(travelDetails.isEmpty())) {
				outer: for (EFmFmEmployeeTravelRequestPO cabRequests : travelDetails) {
					List<EFmFmAssignRoutePO> assignRoutePO;
					List<EFmFmEmployeeTravelRequestPO> particularRouteEmplyees = iCabRequestBO
							.assignCabToParticularShiftDateOrRouteEmployees(requestDate,
									new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()),
									travelRequestPO.getTripType(), dateShiftTime,
									cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
	               log.info("total employee"+particularRouteEmplyees.size());
					List<EFmFmVehicleCheckInPO> allCheckInVehicles = null;
					
					 if (particularRouteEmplyees.size() <= 4 && (travelDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchName().equalsIgnoreCase("SearsPune") || travelDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchName().equalsIgnoreCase("GNPTJP"))) {
		                    allCheckInVehicles = iCabRequestBO.getAllCheckedInVehiclesForSpecificCapacity(
		                    		new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), 5);
		                } else if (particularRouteEmplyees.size() <= 6 && travelDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchName().equalsIgnoreCase("SearsPune")) {
		                    allCheckInVehicles = iCabRequestBO.getAllCheckedInVehiclesForSpecificCapacity(
		                    		new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), 7);
		                } 
		                else if (particularRouteEmplyees.size() <= 7 && travelDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchName().equalsIgnoreCase("GNPTJP")) {
		                    allCheckInVehicles = iCabRequestBO.getAllCheckedInVehiclesForSpecificCapacity(
		                    		new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), 8);
		                } 
		                else if (particularRouteEmplyees.size() >= 8 && travelDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchName().equalsIgnoreCase("GNPTJP")) {
		                    allCheckInVehicles = iCabRequestBO.getAllCheckedInVehiclesForSpecificCapacity(
		                    		new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), 9);
		                } 
		                else if (particularRouteEmplyees.size() > 6 && travelDetails.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchName().equalsIgnoreCase("ShellManila")) {
		                    allCheckInVehicles = iCabRequestBO.getAllCheckedInVehiclesForSpecificCapacity(
		                    		new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), 6);
		                } 
		                else if (particularRouteEmplyees.size() < 6) {
							allCheckInVehicles = iCabRequestBO.getAllCheckedInVehiclesForSpecificCapacity(
									new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), 6);
						}  else {
							allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLargeCapacity(
									new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), 13);
						}
					log.info("CheckIn Vehicle Details=" + particularRouteEmplyees.size());
					if (!allCheckInVehicles.isEmpty()) {
						EFmFmVehicleMasterPO updateVehicleStatus = iCabRequestBO
								.getVehicleDetail(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getVehicleId());
						updateVehicleStatus.setStatus("A");
						iVehicleCheckInBO.update(updateVehicleStatus);
						EFmFmDriverMasterPO particularDriverDetails = approvalBO
								.getParticularDriverDetail(allCheckInVehicles.get(0).getEfmFmDriverMaster().getDriverId());
						particularDriverDetails.setStatus("A");
						approvalBO.update(particularDriverDetails);
						List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
								allCheckInVehicles.get(0).geteFmFmDeviceMaster().getDeviceId(),
								new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()));
						deviceDetails.get(0).setStatus("Y");
						iVehicleCheckInBO.update(deviceDetails.get(0));
					}
					assignRoutePO = iCabRequestBO.getHalfCompletedAssignRouteByDate(requestDate,
							cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
							cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
							cabRequests.getTripType(), cabRequests.getShiftTime());

					EFmFmEmployeeTripDetailPO employeeTripDetailPO = new EFmFmEmployeeTripDetailPO();
					EFmFmAssignRoutePO eAssignRoutePO = new EFmFmAssignRoutePO();
					log.info("checkInVehicleSize"+allCheckInVehicles.size());
					if (assignRoutePO.isEmpty() || !assignRoutePO.get(0).geteFmFmRouteAreaMapping()
							.geteFmFmZoneMaster().getZoneName()
							.equalsIgnoreCase(cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName())) {
						if (!(allCheckInVehicles.isEmpty())) {
							EFmFmAssignRoutePO assignRoute = new EFmFmAssignRoutePO();
							EFmFmVehicleCheckInPO vehicleCheckInPO = new EFmFmVehicleCheckInPO();
							vehicleCheckInPO
									.setCheckInId(allCheckInVehicles.get(allCheckInVehicles.size() - 1).getCheckInId());
							assignRoute.setEfmFmVehicleCheckIn(vehicleCheckInPO);
							EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
							routeAreaMapping.setRouteAreaId(cabRequests.geteFmFmRouteAreaMapping().getRouteAreaId());
							assignRoute.seteFmFmRouteAreaMapping(routeAreaMapping);
							assignRoute.setEscortRequredFlag("N");
							assignRoute.setAllocationMsg("N");
							assignRoute.setShiftTime(cabRequests.getShiftTime());
							assignRoute.setTripStatus("allocated");
							assignRoute.setPlannedReadFlg("Y");
							assignRoute.setScheduleReadFlg("Y");
							assignRoute.setRemarksForEditingTravelledDistance("NO");
							assignRoute.setEditDistanceApproval("NO");
							if (cabRequests.getRequestType().equalsIgnoreCase("AdhocRequest")) {
								assignRoute.setRouteGenerationType("AdhocRequest");
							} else if (cabRequests.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName()
									.equalsIgnoreCase("default")) {
								assignRoute.setRouteGenerationType("normal");
							} else {
								assignRoute.setRouteGenerationType("nodal");
							}
							assignRoute.setCreatedDate(new Date());
							assignRoute.setTripAssignDate(requestDateTimeFormat.parse(requestDateAndTime));
							assignRoute.setTripUpdateTime(requestDateTimeFormat.parse(requestDateAndTime));
							assignRoute.setVehicleStatus("A");
							assignRoute.setBucketStatus("N");
							EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
							eFmFmClientBranchPO
									.setBranchId(cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
							assignRoute.seteFmFmClientBranchPO(eFmFmClientBranchPO);
							assignRoute.setTripType(cabRequests.getTripType());
							allCheckInVehicles.get(allCheckInVehicles.size() - 1).setStatus("N");
							if (cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
									.equalsIgnoreCase("Always")
									|| cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
											.equalsIgnoreCase("femalepresent")
											&& cabRequests.getEfmFmUserMaster().getGender().equalsIgnoreCase("Female")) {
								List<EFmFmEscortCheckInPO> escortList = iCabRequestBO
										.getAllCheckedInEscort(new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()));
								if (!escortList.isEmpty()) {
									EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
									checkInEscort.setEscortCheckInId(
											escortList.get(0).getEscortCheckInId());
									assignRoute.seteFmFmEscortCheckIn(checkInEscort);
									escortList.get(0).setStatus("N");
									iVehicleCheckInBO.update(escortList.get(0));
								}
								assignRoute.setEscortRequredFlag("Y");
							}
							iVehicleCheckInBO
									.update(allCheckInVehicles.get(allCheckInVehicles.size() - 1));
							iCabRequestBO.update(assignRoute);
							cabRequests.setRequestColor("yellow");
							cabRequests.setReadFlg("R");
							iCabRequestBO.update(cabRequests);

							assignRoutePO = iCabRequestBO.getHalfCompletedAssignRouteFromCheckInIdByDate(requestDate,cabRequests.geteFmFmClientBranchPO().getBranchId(),
									cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
									cabRequests.getTripType(), cabRequests.getShiftTime(),
									allCheckInVehicles.get(allCheckInVehicles.size() - 1).getCheckInId());

							EFmFmVehicleMasterPO vehicleMaster = iCabRequestBO
									.getVehicleDetail(assignRoutePO.get(0).getEfmFmVehicleCheckIn()
											.getEfmFmVehicleMaster().getVehicleId());
							vehicleMaster.setStatus("allocated");
							iVehicleCheckInBO.update(vehicleMaster);
							EFmFmDriverMasterPO particularDriverDetails = approvalBO.getParticularDriverDetail(
									assignRoutePO.get(0).getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
							particularDriverDetails.setStatus("allocated");
							approvalBO.update(particularDriverDetails);

							List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO.getDeviceDetailsFromDeviceId(
									assignRoutePO.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),
									new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()));
							deviceDetails.get(0).setStatus("allocated");
							iVehicleCheckInBO.update(deviceDetails.get(0));
							eAssignRoutePO.setAssignRouteId(assignRoutePO.get(0).getAssignRouteId());
							if (assignRoutePO.get(0).getTripType().equalsIgnoreCase("DROP")) {
								employeeTripDetailPO.setTenMinuteMessageStatus("Y");
								employeeTripDetailPO.setTwoMinuteMessageStatus("Y");
								employeeTripDetailPO.setReachedFlg("Y");
								employeeTripDetailPO.setCabDelayMsgStatus("Y");
							} else {
								employeeTripDetailPO.setTenMinuteMessageStatus("N");
								employeeTripDetailPO.setTwoMinuteMessageStatus("N");
								employeeTripDetailPO.setReachedFlg("N");
								employeeTripDetailPO.setCabDelayMsgStatus("N");
							}
							employeeTripDetailPO.setActualTime(new Date());
							employeeTripDetailPO.setGoogleEta(0);
							employeeTripDetailPO.setBoardedFlg("N");
							employeeTripDetailPO.seteFmFmEmployeeTravelRequest(cabRequests);
							employeeTripDetailPO.setEfmFmAssignRoute(eAssignRoutePO);
							employeeTripDetailPO.setCurrenETA("0");
							employeeTripDetailPO.setEmployeeStatus("allocated");
							employeeTripDetailPO.setComingStatus("Yet to confirm");
    						employeeTripDetailPO.setEmployeeOnboardStatus("NO");
							iCabRequestBO.update(employeeTripDetailPO);
						}
					} else {
						EFmFmVehicleMasterPO vehicleMaster = iCabRequestBO
								.getVehicleDetail(assignRoutePO.get(0).getEfmFmVehicleCheckIn()
										.getEfmFmVehicleMaster().getVehicleId());
						assignRoutePO.get(0).getAssignRouteId();
						List<EFmFmEmployeeTripDetailPO> allEmployees = iCabRequestBO.getParticularTripAllEmployees(
								assignRoutePO.get(0).getAssignRouteId());

						int availableCapacity = 0;
						availableCapacity = (vehicleMaster.getCapacity() - 1) - allEmployees.size();
						if (availableCapacity > 1
								&& assignRoutePO.get(0).geteFmFmClientBranchPO().getEscortRequired()
										.equalsIgnoreCase("femalepresent")
								&& cabRequests.getEfmFmUserMaster().getGender().equalsIgnoreCase("Female")
								&& assignRoutePO.get(0).getEscortRequredFlag()
										.equalsIgnoreCase("N")) {
							List<EFmFmEscortCheckInPO> escortList = iCabRequestBO.getAllCheckedInEscort(
									new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()));
							if (!(escortList.isEmpty())) {
								EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
								checkInEscort.setEscortCheckInId(
										escortList.get(0).getEscortCheckInId());
								assignRoutePO.get(0).seteFmFmEscortCheckIn(checkInEscort);
								escortList.get(0).setStatus("N");
								iVehicleCheckInBO.update(escortList.get(0));
							}
							assignRoutePO.get(0).setEscortRequredFlag("Y");
							assignRoutePO.get(0).setVehicleStatus("F");
							iCabRequestBO.update(assignRoutePO.get(0));
							// availableCapacity = vehicleMaster.getAvailableSeat()
						}
						if (availableCapacity == 1
								&& assignRoutePO.get(0).geteFmFmClientBranchPO().getEscortRequired()
										.equalsIgnoreCase("femalepresent")
								&& cabRequests.getEfmFmUserMaster().getGender().equalsIgnoreCase("Female")
								&& assignRoutePO.get(0).getEscortRequredFlag().equalsIgnoreCase("N"))
							continue outer;
						if (cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
								.equalsIgnoreCase("firstlastfemale")
								&& cabRequests.geteFmFmEmployeeRequestMaster().getTripType().equalsIgnoreCase("PICKUP")
								&& availableCapacity == 1) {
							boolean requestAddingFlg = true;
							for (EFmFmAssignRoutePO assignRouteDetails : assignRoutePO) {
								EFmFmEmployeeTripDetailPO employeeTripDetail = new EFmFmEmployeeTripDetailPO();
								eAssignRoutePO.setAssignRouteId(assignRouteDetails.getAssignRouteId());
								if (assignRouteDetails.getTripType().equalsIgnoreCase("DROP")) {
									employeeTripDetail.setTenMinuteMessageStatus("Y");
									employeeTripDetail.setTwoMinuteMessageStatus("Y");
									employeeTripDetail.setReachedFlg("Y");
									employeeTripDetail.setCabDelayMsgStatus("Y");

								} else {
									employeeTripDetail.setTenMinuteMessageStatus("N");
									employeeTripDetail.setTwoMinuteMessageStatus("N");
									employeeTripDetail.setReachedFlg("N");
									employeeTripDetail.setCabDelayMsgStatus("N");

								}

								employeeTripDetail.setActualTime(new Date());
								employeeTripDetail.setGoogleEta(0);
								employeeTripDetail.setBoardedFlg("N");
								employeeTripDetail.setCurrenETA("0");
								employeeTripDetail.seteFmFmEmployeeTravelRequest(cabRequests);
								employeeTripDetail.setEfmFmAssignRoute(eAssignRoutePO);
								employeeTripDetail.setEmployeeStatus("allocated");
								employeeTripDetail.setComingStatus("Yet to confirm");
	    						employeeTripDetail.setEmployeeOnboardStatus("NO");

								iCabRequestBO.save(employeeTripDetail);
								if (allEmployees.get(0).geteFmFmEmployeeTravelRequest()
										.getEfmFmUserMaster().getGender().equalsIgnoreCase("Female")
										&& (allEmployees.get(0)
												.geteFmFmEmployeeTravelRequest().getShiftTime().getHours() >= 20
												|| allEmployees.get(0)
														.geteFmFmEmployeeTravelRequest().getShiftTime().getHours() == 0
												|| allEmployees.get(0)
														.geteFmFmEmployeeTravelRequest().getShiftTime().getHours() <= 7)) {
									List<EFmFmEmployeeTripDetailPO> tripId = iCabRequestBO
											.getRequestStatusFromBranchIdAndRequestId(
													new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()),
													cabRequests.getRequestId());
									iCabRequestBO.deleteParticularRequestFromEmployeeTripDetail(
											tripId.get(0).getEmpTripId());
								} else {
									assignRoutePO.get(0).setVehicleStatus("F");
									iCabRequestBO.update(assignRoutePO.get(0));
									iVehicleCheckInBO.update(vehicleMaster);
									cabRequests.setRequestColor("yellow");
									cabRequests.setReadFlg("R");
									iCabRequestBO.update(cabRequests);
									requestAddingFlg = false;
									continue outer;
								}
							}

							if (requestAddingFlg) {
								if (particularRouteEmplyees.size() <= 6)
									allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLessCapacity(
											new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), 10);
								else
									allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLessCapacity(
											new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), 20);
								if (!allCheckInVehicles.isEmpty() && allCheckInVehicles.size() != 0) {
									EFmFmAssignRoutePO assignRoute1 = new EFmFmAssignRoutePO();
									EFmFmVehicleCheckInPO vehicleCheckInPO = new EFmFmVehicleCheckInPO();
									vehicleCheckInPO.setCheckInId(
											allCheckInVehicles.get(allCheckInVehicles.size() - 1)
													.getCheckInId());
									assignRoute1.setEfmFmVehicleCheckIn(vehicleCheckInPO);
									EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
									routeAreaMapping
											.setRouteAreaId(cabRequests.geteFmFmRouteAreaMapping().getRouteAreaId());
									assignRoute1.seteFmFmRouteAreaMapping(routeAreaMapping);
									assignRoute1.setEscortRequredFlag("N");
									assignRoute1.setAllocationMsg("N");
									assignRoute1.setTripStatus("allocated");
									assignRoute1.setPlannedReadFlg("Y");
									assignRoute1.setScheduleReadFlg("Y");

									assignRoute1.setRemarksForEditingTravelledDistance("NO");
									assignRoute1.setEditDistanceApproval("NO");
									if (cabRequests.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName()
											.equalsIgnoreCase("default")) {
										assignRoute1.setRouteGenerationType("normal");
									} else {
										assignRoute1.setRouteGenerationType("nodal");
									}
									assignRoute1.setTripUpdateTime(requestDateFormat.parse(requestDate));
									assignRoute1.setCreatedDate(new Date());
									assignRoute1.setTripAssignDate(requestDateFormat.parse(requestDate));
									assignRoute1.setShiftTime(cabRequests.getShiftTime());
									assignRoute1.setVehicleStatus("A");
									assignRoute1.setBucketStatus("N");

									EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
									eFmFmClientBranchPO.setBranchId(
											cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
									assignRoute1.seteFmFmClientBranchPO(eFmFmClientBranchPO);
									assignRoute1.setTripType(cabRequests.getTripType());
									allCheckInVehicles.get(allCheckInVehicles.size() - 1)
											.setStatus("N");
									if (cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
											.equalsIgnoreCase("Always")
											|| cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
													.equalsIgnoreCase("femalepresent")
													&& cabRequests.getEfmFmUserMaster().getGender()
															.equalsIgnoreCase("Female")) {
										List<EFmFmEscortCheckInPO> escortList = iCabRequestBO.getAllCheckedInEscort(
												new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()));
										if (!escortList.isEmpty() || escortList.size() != 0) {
											EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
											checkInEscort.setEscortCheckInId(
													escortList.get(0).getEscortCheckInId());
											assignRoute1.seteFmFmEscortCheckIn(checkInEscort);
											escortList.get(0).setStatus("N");
											iVehicleCheckInBO.update(escortList.get(0));
										}
										assignRoute1.setEscortRequredFlag("Y");
									}

									iCabRequestBO.update(assignRoute1);
									iVehicleCheckInBO.update(
											allCheckInVehicles.get(allCheckInVehicles.size() - 1));
									cabRequests.setRequestColor("yellow");
									cabRequests.setReadFlg("R");
									iCabRequestBO.update(cabRequests);
									List<EFmFmAssignRoutePO> assignRoutePO1 = iCabRequestBO
											.getHalfCompletedAssignRouteFromCheckInIdByDate(requestDate,cabRequests.geteFmFmClientBranchPO().getBranchId(),
													cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
													cabRequests.getTripType(), cabRequests.getShiftTime(),
													allCheckInVehicles
															.get(allCheckInVehicles.size() - 1).getCheckInId());
									EFmFmVehicleMasterPO vehicleMaster1 = iCabRequestBO
											.getVehicleDetail(assignRoutePO1.get(0)
													.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
									// vehicleMaster1.setAvailableSeat(vehicleMaster1.getAvailableSeat()
									// - 1);
									vehicleMaster1.setStatus("allocated");
									iVehicleCheckInBO.update(vehicleMaster1);
									EFmFmDriverMasterPO particularDriverDetails1 = approvalBO
											.getParticularDriverDetail(assignRoutePO1.get(0).getEfmFmVehicleCheckIn()
													.getEfmFmDriverMaster().getDriverId());
									particularDriverDetails1.setStatus("allocated");
									approvalBO.update(particularDriverDetails1);
									List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO
											.getDeviceDetailsFromDeviceId(assignRoutePO1.get(0).getEfmFmVehicleCheckIn()
													.geteFmFmDeviceMaster().getDeviceId(), new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()));
									deviceDetails.get(0).setStatus("allocated");
									iVehicleCheckInBO.update(deviceDetails.get(0));
									EFmFmEmployeeTripDetailPO employeeTripDetailPO1 = new EFmFmEmployeeTripDetailPO();
									eAssignRoutePO.setAssignRouteId(
											assignRoutePO1.get(0).getAssignRouteId());
									if (assignRoutePO1.get(0).getTripType()
											.equalsIgnoreCase("DROP")) {
										employeeTripDetailPO1.setTenMinuteMessageStatus("Y");
										employeeTripDetailPO1.setTwoMinuteMessageStatus("Y");
										employeeTripDetailPO1.setReachedFlg("Y");
										employeeTripDetailPO1.setCabDelayMsgStatus("Y");
									} else {
										employeeTripDetailPO1.setTenMinuteMessageStatus("N");
										employeeTripDetailPO1.setTwoMinuteMessageStatus("N");
										employeeTripDetailPO1.setReachedFlg("N");
										employeeTripDetailPO1.setCabDelayMsgStatus("N");

									}
									employeeTripDetailPO1.setActualTime(new Date());
									employeeTripDetailPO1.setGoogleEta(0);
									employeeTripDetailPO1.setBoardedFlg("N");
									employeeTripDetailPO1.seteFmFmEmployeeTravelRequest(cabRequests);
									employeeTripDetailPO1.setEfmFmAssignRoute(eAssignRoutePO);
									employeeTripDetailPO1.setEmployeeStatus("allocated");
									employeeTripDetailPO1.setComingStatus("Yet to confirm");
		    						employeeTripDetailPO1.setEmployeeOnboardStatus("NO");

									employeeTripDetailPO1.setCurrenETA("0");
									iCabRequestBO.update(employeeTripDetailPO1);
									continue outer;
								} else {
									continue outer;
								}
							}
						}
						if (cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
								.equalsIgnoreCase("firstlastfemale")
								&& cabRequests.geteFmFmEmployeeRequestMaster().getTripType().equalsIgnoreCase("DROP")
								&& availableCapacity == 1) {
							boolean requestAddingFlg = true;
							for (EFmFmAssignRoutePO assignRouteDetails : assignRoutePO) {
								EFmFmEmployeeTripDetailPO employeeTripDetail = new EFmFmEmployeeTripDetailPO();
								eAssignRoutePO.setAssignRouteId(assignRouteDetails.getAssignRouteId());
								if (assignRouteDetails.getTripType().equalsIgnoreCase("DROP")) {
									employeeTripDetail.setTenMinuteMessageStatus("Y");
									employeeTripDetail.setTwoMinuteMessageStatus("Y");
									employeeTripDetail.setReachedFlg("Y");
									employeeTripDetail.setCabDelayMsgStatus("Y");

								} else {
									employeeTripDetail.setTenMinuteMessageStatus("N");
									employeeTripDetail.setTwoMinuteMessageStatus("N");
									employeeTripDetail.setReachedFlg("N");
									employeeTripDetail.setCabDelayMsgStatus("N");
								}
								employeeTripDetail.setActualTime(new Date());
								employeeTripDetail.setGoogleEta(0);
								employeeTripDetail.setBoardedFlg("N");
								employeeTripDetail.setCurrenETA("0");
								employeeTripDetail.seteFmFmEmployeeTravelRequest(cabRequests);
								employeeTripDetail.setEfmFmAssignRoute(eAssignRoutePO);
								employeeTripDetail.setEmployeeStatus("allocated");
								employeeTripDetail.setComingStatus("Yet to confirm");
	    						employeeTripDetail.setEmployeeOnboardStatus("NO");

								iCabRequestBO.save(employeeTripDetail);
								List<EFmFmEmployeeTripDetailPO> allSortedEmployees = iCabRequestBO
										.getDropTripAllSortedEmployees(assignRouteDetails.getAssignRouteId());
								if (allSortedEmployees.get(allSortedEmployees.size() - 1)
										.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()
										.equalsIgnoreCase("Female")
										&& (allSortedEmployees
												.get(allSortedEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
														.getShiftTime().getHours() >= 19
												|| allSortedEmployees
														.get(allSortedEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
																.getShiftTime().getHours() == 0
												|| allSortedEmployees
														.get(allSortedEmployees.size() - 1).geteFmFmEmployeeTravelRequest()
																.getShiftTime().getHours() < 7)) {
									List<EFmFmEmployeeTripDetailPO> tripId = iCabRequestBO
											.getRequestStatusFromBranchIdAndRequestId(
													new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()),
													cabRequests.getRequestId());
									iCabRequestBO.deleteParticularRequestFromEmployeeTripDetail(
											tripId.get(0).getEmpTripId());
								} else {
									assignRoutePO.get(0).setVehicleStatus("F");
									iCabRequestBO.update(assignRoutePO.get(0));
									iVehicleCheckInBO.update(vehicleMaster);
									cabRequests.setRequestColor("yellow");
									cabRequests.setReadFlg("R");
									iCabRequestBO.update(cabRequests);
									requestAddingFlg = false;
									continue outer;
								}
							}
							if (requestAddingFlg) {
								if (particularRouteEmplyees.size() <= 6)
									allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLessCapacity(
											new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), 10);
								else
									allCheckInVehicles = iCabRequestBO.getAllCheckedInVehicleLessCapacity(
											new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()), 20);
								if (!allCheckInVehicles.isEmpty() && allCheckInVehicles.size() != 0) {
									EFmFmAssignRoutePO assignRoute1 = new EFmFmAssignRoutePO();
									EFmFmVehicleCheckInPO vehicleCheckInPO = new EFmFmVehicleCheckInPO();
									vehicleCheckInPO.setCheckInId(
											allCheckInVehicles.get(allCheckInVehicles.size() - 1)
													.getCheckInId());
									assignRoute1.setEfmFmVehicleCheckIn(vehicleCheckInPO);
									EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();
									routeAreaMapping
											.setRouteAreaId(cabRequests.geteFmFmRouteAreaMapping().getRouteAreaId());
									assignRoute1.seteFmFmRouteAreaMapping(routeAreaMapping);
									assignRoute1.setEscortRequredFlag("N");
									assignRoute1.setAllocationMsg("N");
									assignRoute1.setTripStatus("allocated");
									assignRoute1.setPlannedReadFlg("Y");
									assignRoute1.setScheduleReadFlg("Y");

									assignRoute1.setRemarksForEditingTravelledDistance("NO");
									assignRoute1.setEditDistanceApproval("NO");
									if (cabRequests.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName()
											.equalsIgnoreCase("default")) {
										assignRoute1.setRouteGenerationType("normal");
									} else {
										assignRoute1.setRouteGenerationType("nodal");
									}

									assignRoute1.setTripAssignDate(requestDateFormat.parse(requestDate));
									assignRoute1.setTripUpdateTime(requestDateFormat.parse(requestDate));
									assignRoute1.setCreatedDate(new Date());
									assignRoute1.setShiftTime(cabRequests.getShiftTime());
									assignRoute1.setVehicleStatus("A");
									assignRoute1.setBucketStatus("N");

									EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
									eFmFmClientBranchPO.setBranchId(cabRequests.geteFmFmEmployeeRequestMaster()
											.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
									assignRoute1.seteFmFmClientBranchPO(eFmFmClientBranchPO);
									assignRoute1.setTripType(cabRequests.getTripType());
									allCheckInVehicles.get(allCheckInVehicles.size() - 1)
											.setStatus("N");
									if (cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
											.equalsIgnoreCase("Always")
											|| cabRequests.getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
													.equalsIgnoreCase("femalepresent")
													&& cabRequests.getEfmFmUserMaster().getGender()
															.equalsIgnoreCase("Female")) {
										List<EFmFmEscortCheckInPO> escortList = iCabRequestBO.getAllCheckedInEscort(
												new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()));
										if (!escortList.isEmpty() || escortList.size() != 0) {
											EFmFmEscortCheckInPO checkInEscort = new EFmFmEscortCheckInPO();
											checkInEscort.setEscortCheckInId(
													escortList.get(0).getEscortCheckInId());
											assignRoute1.seteFmFmEscortCheckIn(checkInEscort);
											escortList.get(0).setStatus("N");
											iVehicleCheckInBO.update(escortList.get(0));
										}
										assignRoute1.setEscortRequredFlag("Y");
									}
									iCabRequestBO.update(assignRoute1);
									iVehicleCheckInBO.update(
											allCheckInVehicles.get(allCheckInVehicles.size() - 1));
									cabRequests.setRequestColor("yellow");
									cabRequests.setReadFlg("R");
									iCabRequestBO.update(cabRequests);
									List<EFmFmAssignRoutePO> assignRoutePO1 = iCabRequestBO
											.getHalfCompletedAssignRouteFromCheckInIdByDate(requestDate,cabRequests.geteFmFmClientBranchPO().getBranchId(),
													cabRequests.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
													cabRequests.getTripType(), cabRequests.getShiftTime(),
													allCheckInVehicles
															.get(allCheckInVehicles.size() - 1).getCheckInId());
									EFmFmVehicleMasterPO vehicleMaster1 = iCabRequestBO
											.getVehicleDetail(assignRoutePO1.get(0)
													.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
									vehicleMaster1.setStatus("allocated");
									iVehicleCheckInBO.update(vehicleMaster1);
									EFmFmDriverMasterPO particularDriverDetails1 = approvalBO
											.getParticularDriverDetail(assignRoutePO1.get(0).getEfmFmVehicleCheckIn()
													.getEfmFmDriverMaster().getDriverId());
									particularDriverDetails1.setStatus("allocated");
									approvalBO.update(particularDriverDetails1);
									List<EFmFmDeviceMasterPO> deviceDetails = iVehicleCheckInBO
											.getDeviceDetailsFromDeviceId(assignRoutePO1.get(0).getEfmFmVehicleCheckIn()
													.geteFmFmDeviceMaster().getDeviceId(), new MultifacilityService().combinedBranchIdDetails(travelRequestPO.getUserId(),travelRequestPO.getCombinedFacility()));
									deviceDetails.get(0).setStatus("allocated");
									iVehicleCheckInBO.update(deviceDetails.get(0));

									EFmFmEmployeeTripDetailPO employeeTripDetailPO1 = new EFmFmEmployeeTripDetailPO();
									eAssignRoutePO.setAssignRouteId(
											assignRoutePO1.get(0).getAssignRouteId());
									if (assignRoutePO1.get(0).getTripType()
											.equalsIgnoreCase("DROP")) {
										employeeTripDetailPO1.setTenMinuteMessageStatus("Y");
										employeeTripDetailPO1.setTwoMinuteMessageStatus("Y");
										employeeTripDetailPO1.setReachedFlg("Y");
										employeeTripDetailPO1.setCabDelayMsgStatus("Y");
									} else {
										employeeTripDetailPO1.setTenMinuteMessageStatus("N");
										employeeTripDetailPO1.setTwoMinuteMessageStatus("N");
										employeeTripDetailPO1.setReachedFlg("N");
										employeeTripDetailPO1.setCabDelayMsgStatus("N");
									}
									employeeTripDetailPO1.setActualTime(new Date());
									employeeTripDetailPO1.setGoogleEta(0);
									employeeTripDetailPO1.setBoardedFlg("N");
									employeeTripDetailPO1.seteFmFmEmployeeTravelRequest(cabRequests);
									employeeTripDetailPO1.setEfmFmAssignRoute(eAssignRoutePO);
									employeeTripDetailPO1.setEmployeeStatus("allocated");
									employeeTripDetailPO1.setComingStatus("Yet to confirm");
									employeeTripDetailPO1.setCurrenETA("0");
		    						employeeTripDetailPO1.setEmployeeOnboardStatus("NO");
									iCabRequestBO.update(employeeTripDetailPO1);
									continue outer;
								}
								continue outer;
							}
						}
						if (availableCapacity == 1) {
							assignRoutePO.get(0).setVehicleStatus("F");
							iCabRequestBO.update(assignRoutePO.get(0));
						}
						// vehicleMaster.setAvailableSeat(availableCapacity);
						iVehicleCheckInBO.update(vehicleMaster);
						cabRequests.setRequestColor("yellow");
						cabRequests.setReadFlg("R");
						iCabRequestBO.update(cabRequests);
						eAssignRoutePO.setAssignRouteId(assignRoutePO.get(0).getAssignRouteId());
						if (assignRoutePO.get(0).getTripType().equalsIgnoreCase("DROP")) {
							employeeTripDetailPO.setTenMinuteMessageStatus("Y");
							employeeTripDetailPO.setTwoMinuteMessageStatus("Y");
							employeeTripDetailPO.setReachedFlg("Y");
							employeeTripDetailPO.setCabDelayMsgStatus("Y");

						} else {
							employeeTripDetailPO.setTenMinuteMessageStatus("N");
							employeeTripDetailPO.setTwoMinuteMessageStatus("N");
							employeeTripDetailPO.setReachedFlg("N");
							employeeTripDetailPO.setCabDelayMsgStatus("N");
						}
						employeeTripDetailPO.setActualTime(new Date());
						employeeTripDetailPO.setGoogleEta(0);
						employeeTripDetailPO.setBoardedFlg("N");
						employeeTripDetailPO.setCurrenETA("0");
						employeeTripDetailPO.seteFmFmEmployeeTravelRequest(cabRequests);
						employeeTripDetailPO.setEfmFmAssignRoute(eAssignRoutePO);
						employeeTripDetailPO.setEmployeeStatus("allocated");
						employeeTripDetailPO.setComingStatus("Yet to confirm");
						employeeTripDetailPO.setEmployeeOnboardStatus("NO");
						iCabRequestBO.save(employeeTripDetailPO);
					}
					response="success";					
				    Map<String, Object>  empReqExcelRows= new HashMap<String, Object>();			
					empReqExcelRows.put("Status",response);	
					childRowResponse.add(empReqExcelRows);
					
				}
			}
			
			return childRowResponse;
		}
		public String vehicleAllocation( List<String> vehicleNumber, List<String> driverMobileNumber, int branchId,String combinedFacility) {
			IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
			IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
			 IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
			String response="failure";			
			if(vehicleNumber.size() == driverMobileNumber.size()){				
				for(int vehId=0; vehId<vehicleNumber.size(); vehId++){					
					List<EFmFmVehicleMasterPO> vehicleDetails = vehicleCheckInBO.getVehicleDetailsFromVehicleNumber(vehicleNumber.get(vehId),combinedFacility);										
					List<EFmFmDriverMasterPO> driverDetails = vehicleCheckInBO.getParticularDriverDetailsFromMobileNum(driverMobileNumber.get(vehId),branchId);
					List<EFmFmDeviceMasterPO> deviceDetails = vehicleCheckInBO.getListOfAllavailableDevices(branchId);
					List<EFmFmEmployeeTripDetailPO> assignRouteDetails = vehicleCheckInBO.getparticularEmployeeTripDetails(vehicleDetails.get(0).getVehicleNumber(),branchId);
					if(!assignRouteDetails.isEmpty()){
						for(EFmFmEmployeeTripDetailPO  assignIdList:assignRouteDetails){
							List<EFmFmAssignRoutePO> assignedRouteId = assignRouteBO.closeParticularTrips(assignIdList.getEfmFmAssignRoute());
							List<EFmFmVehicleCheckInPO> vehiceCheckInDetails = vehicleCheckInBO.getParticularCheckedInVehicles(combinedFacility,vehicleDetails.get(0).getVehicleId());
							if(!vehiceCheckInDetails.isEmpty()){
								  assignedRouteId.get(0).setEfmFmVehicleCheckIn(vehiceCheckInDetails.get(0));								  
								  assignRouteBO.update(assignedRouteId.get(0));
								  vehicleDetails.get(0).setStatus("allocated");
								  vehicleCheckInBO.update(vehicleDetails.get(0));
								  List<EFmFmDriverMasterPO> driverCheckinDetails = vehicleCheckInBO.
										  getParticularDriverDetailsFromMobileNum(vehiceCheckInDetails.get(0).getEfmFmDriverMaster().getMobileNumber(),branchId);
								  driverCheckinDetails.get(0).setStatus("allocated");
								  approvalBO.update(driverCheckinDetails.get(0));
								  
								  response="success";
							}else{
								EFmFmDeviceMasterPO eFmFmDeviceMasterPO=new EFmFmDeviceMasterPO();
								EFmFmClientBranchPO eFmFmClientBranchPO=new EFmFmClientBranchPO();
								eFmFmClientBranchPO.setBranchId(branchId);
								eFmFmDeviceMasterPO.setVehicleNum(vehicleDetails.get(0).getVehicleNumber().trim());
								eFmFmDeviceMasterPO.setDriverId(driverDetails.get(0).getDriverId());
								eFmFmDeviceMasterPO.setImeiNumber(deviceDetails.get(0).getImeiNumber());								
								eFmFmDeviceMasterPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
								driverCheckInFromDevice(eFmFmDeviceMasterPO,combinedFacility);
								List<EFmFmVehicleCheckInPO> newVehicleCheckIn = vehicleCheckInBO.getParticularCheckedInVehicles(combinedFacility,vehicleDetails.get(0).getVehicleId());
								if(!newVehicleCheckIn.isEmpty()){
										  assignedRouteId.get(0).setEfmFmVehicleCheckIn(newVehicleCheckIn.get(0));								  
										  assignRouteBO.update(assignedRouteId.get(0));										  
										  vehicleDetails.get(0).setStatus("allocated");
										  vehicleCheckInBO.update(vehicleDetails.get(0));							  
										  deviceDetails.get(0).setStatus("allocated");
										  vehicleCheckInBO.update(deviceDetails.get(0));										  
										  driverDetails.get(0).setStatus("allocated");
										  approvalBO.update(driverDetails.get(0));										  
										  response="success";
								}
								response="success";
							}							
							
						}
					}		
				}		
			}		
			return response;			
		}
    
		public String driverCheckInFromDevice(EFmFmDeviceMasterPO eFmFmDeviceMasterPO,String combinedFacility) {
		        IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
		                .getBean("IVehicleCheckInBO");
		        String response="failure";
		        IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");

		        Map<String, Object> responce = new HashMap<String, Object>();
		        EFmFmVehicleMasterPO vehicleDetails = vehicleCheckInBO.getParticularVehicleDetails(
		                eFmFmDeviceMasterPO.getVehicleNum(), eFmFmDeviceMasterPO.geteFmFmClientBranchPO().getBranchId());
		        List<EFmFmDriverMasterPO> driverDetails = approvalBO
		                .getParticularDriverDetailFromDeriverId(eFmFmDeviceMasterPO.getDriverId());
		         if (vehicleDetails != null) {
		        	 if(vehicleDetails.getEfmFmVendorMaster().getStatus().equalsIgnoreCase("P")){
		                 responce.put("status", "vendorNotApproved");		            	
		                
		             }
		            if (vehicleDetails.getStatus().equalsIgnoreCase("P") || vehicleDetails.getStatus().equalsIgnoreCase("R")) {
		                responce.put("status", "VNA");		           	 
		                
		            }
		        } else if (vehicleDetails == null) {
		            responce.put("status", "Vwrong");		       	 
		           
		        }
		        if (!(driverDetails.isEmpty())) {
		            if (driverDetails.get(0).getStatus().equalsIgnoreCase("P")
		                    || driverDetails.get(0).getStatus().equalsIgnoreCase("R")) {
		                responce.put("status", "DNA");		           	 
		                
		            }
		        } else if (driverDetails.isEmpty()) {
		            responce.put("status", "Dwrong");		       	 
		           
		        }
		        EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
		        EFmFmVehicleMasterPO vehicleMaster = new EFmFmVehicleMasterPO();
		        vehicleMaster.setVehicleId(vehicleDetails.getVehicleId());
		        EFmFmDriverMasterPO driverMaster = new EFmFmDriverMasterPO();
		        driverMaster.setDriverId(driverDetails.get(0).getDriverId());
		        eFmFmVehicleCheckInPO.setCheckInTime(new Date());
		        eFmFmVehicleCheckInPO.setReadFlg("Y");
		        eFmFmVehicleCheckInPO.setCheckInType("mobile");
		        eFmFmVehicleCheckInPO.setAdminMailTriggerStatus(false);
		        eFmFmVehicleCheckInPO.setSupervisorMailTriggerStatus(false);
		        eFmFmVehicleCheckInPO.setEfmFmVehicleMaster(vehicleMaster);
		        List<EFmFmVehicleCheckInPO> checkInVehicle = vehicleCheckInBO.getParticularCheckedInVehicles(combinedFacility, vehicleDetails.getVehicleId());
		        if (!(checkInVehicle.isEmpty())) {
		            responce.put("status", "VcheckedIn");
		       
		            
		        }
		        eFmFmVehicleCheckInPO.setEfmFmDriverMaster(driverMaster);
		        List<EFmFmVehicleCheckInPO> checkInDriver = vehicleCheckInBO
		                .getParticularCheckedInDriver(eFmFmVehicleCheckInPO);
		        if (!(checkInDriver.isEmpty())) {
		            responce.put("status", "DcheckedIn");
		       	
		        }
		 		List<EFmFmDeviceMasterPO> deviceDetail = vehicleCheckInBO.deviceImeiNumberExistsCheck(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getImeiNumber());
		        if (!(driverDetails.get(0).getEfmFmVendorMaster().getVendorId() == vehicleDetails.getEfmFmVendorMaster()
		                .getVendorId())) {
		            responce.put("status", "mismatch");

		        }
		        EFmFmDeviceMasterPO deviceMaster = new EFmFmDeviceMasterPO();
		        deviceMaster.setDeviceId(deviceDetail.get(0).getDeviceId());
		        eFmFmVehicleCheckInPO.seteFmFmDeviceMaster(deviceMaster);
		        eFmFmVehicleCheckInPO.setStatus("Y");
		        vehicleCheckInBO.vehicleDriverCheckIn(eFmFmVehicleCheckInPO);
		        
		        response="success";
				return response;
		    }
    
}