package com.newtglobal.eFmFmFleet.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/device")
@Consumes("application/json")
@Produces("application/json")
public class DeviceDetailService {
	
	private static Log log = LogFactory.getLog(DeviceDetailService.class);
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	
	
	@POST
	@Path("/deviceRecord")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response deviceUpload(
			@FormDataParam("filename") InputStream uploadedInputStream,
			@FormDataParam("filename") FormDataContentDisposition fileDetail,
			@QueryParam("branchId") int branchId,			
			@Context HttpServletRequest request,@QueryParam("profileId") int userId) throws ParseException, IOException {
		log.info("serviceStart -UserId :" +userId);
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
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();			
			while (rowIterator.hasNext()) {
				ArrayList<Object> columnValue = new ArrayList<Object>();
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {					
					Cell cell = cellIterator.next();					
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						columnValue.add(cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_BLANK:
						columnValue.add(cell.getStringCellValue());
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
				if(columnValue.size()>1){
					deviceRecord(columnValue,branchId);
				}
			
					
			}

		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		log.info("serviceEnd -UserId :" +userId);
		return Response.ok("SUCCEES", MediaType.APPLICATION_JSON).build();
	}

	
	private void deviceRecord(ArrayList<Object> columnValue, int branchId) {		
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		EFmFmDeviceMasterPO eFmFmDeviceMasterPO=new EFmFmDeviceMasterPO();
		eFmFmDeviceMasterPO.setDeviceModel(columnValue.get(0).toString());
		eFmFmDeviceMasterPO.setDeviceOs(columnValue.get(1).toString());
		eFmFmDeviceMasterPO.setImeiNumber(columnValue.get(2).toString().trim());
		eFmFmDeviceMasterPO.setMobileNumber(columnValue.get(3).toString().trim());
		EFmFmClientBranchPO eFmFmClientBranchPO=new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(branchId);		
		eFmFmDeviceMasterPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		eFmFmDeviceMasterPO.setStatus("Y");
		eFmFmDeviceMasterPO.setDeviceType("Android");
		eFmFmDeviceMasterPO.setIsActive("Y");		
		if(eFmFmDeviceMasterPO.getImeiNumber() !=null && !eFmFmDeviceMasterPO.getImeiNumber().isEmpty() && eFmFmDeviceMasterPO.getImeiNumber() !=""
				&& eFmFmDeviceMasterPO.getMobileNumber() !=null && !eFmFmDeviceMasterPO.getMobileNumber().isEmpty() && eFmFmDeviceMasterPO.getMobileNumber() !=""){
			List<EFmFmDeviceMasterPO> mobileExist=iVehicleCheckInBO.deviceNumberExistsCheck(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getMobileNumber());
			if(mobileExist.isEmpty()){
				List<EFmFmDeviceMasterPO> deviceExist=iVehicleCheckInBO.deviceImeiNumberExistsCheck(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getImeiNumber());
				if(deviceExist.isEmpty()){
					iVehicleCheckInBO.save(eFmFmDeviceMasterPO);
				}
			}
		}
	}


		/*
	 *  get all the active devices
	 */
		@POST
		@Path("/alldevices")
		public Response getAllActiveDevices(EFmFmDeviceMasterPO eFmFmDeviceMasterPO){					
			IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
			IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
			Map<String, Object> responce = new HashMap<String, Object>();
			log.info("serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
		
			log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
			try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){
					responce.put("status", "invalidRequest");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
				
				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
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
			
			List<EFmFmDeviceMasterPO> allActiveDeviceDetails=iVehicleCheckInBO.getListOfAllActiveDevices(eFmFmDeviceMasterPO.getCombinedFacility());
			List<Map<String, Object>> allDevices = new ArrayList<Map<String, Object>>();	
			for(EFmFmDeviceMasterPO deviceDetail:allActiveDeviceDetails){
				Map<String, Object>  device = new HashMap<String, Object>();	
				device.put("mobileNumber", deviceDetail.getMobileNumber());
				device.put("imeiNumber", deviceDetail.getImeiNumber());
				device.put("driverType", deviceDetail.getDeviceType());
				device.put("deviceModel", deviceDetail.getDeviceModel());
				device.put("deviceStatus", deviceDetail.getIsActive());
				device.put("deviceId", deviceDetail.getDeviceId());
				device.put("deviceStatus", deviceDetail.getIsActive());
				device.put("simOperator", deviceDetail.getSimOperator());
				device.put("deviceOs", deviceDetail.getDeviceOs());
				device.put("facilityName", deviceDetail.geteFmFmClientBranchPO().getBranchName());
//				log.info("APProval calllled.......................................................");
				allDevices.add(device);			
			}
			log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
			return Response.ok(allDevices, MediaType.APPLICATION_JSON).build();
		}
		/*
		 *  enabling and disabling the devices
		 */
			@POST
			@Path("/devicestatus")
			public Response makeADeviceEnableOrDisable(EFmFmDeviceMasterPO eFmFmDeviceMasterPO){					
				IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
				IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
				Map<String, Object> responce = new HashMap<String, Object>();
						
				log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
				try{
					if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmDeviceMasterPO.getUserId()))){

						responce.put("status", "invalidRequest");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
					List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmDeviceMasterPO.getUserId());
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
				 log.info("serviceStart -UserId :" + eFmFmDeviceMasterPO.getUserId());
				Map<String, Object>  request = new HashMap<String, Object>();	
				List<EFmFmDeviceMasterPO> updateDeviceStatus=iVehicleCheckInBO.deviceImeiNumberExistsCheck(eFmFmDeviceMasterPO.getCombinedFacility(),eFmFmDeviceMasterPO.getImeiNumber());
				updateDeviceStatus.get(0).setIsActive(eFmFmDeviceMasterPO.getIsActive());
				iVehicleCheckInBO.update(updateDeviceStatus.get(0));
				request.put("status", "success");
				 log.info("serviceEnd -UserId :" + eFmFmDeviceMasterPO.getUserId());
				return Response.ok(request, MediaType.APPLICATION_JSON).build();
			}
		
}
