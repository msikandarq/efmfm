package com.newtglobal.eFmFmFleet.services;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Validator;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDynamicVehicleCheckListPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFixedDistanceContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCapacityMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleInspectionPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/vehicle")
@Consumes("application/json")
@Produces("application/json")
public class VehicleService {

	/**
	 * The vehicleDetails method implemented for getting the list of vehicle
	 * details and driver will allocate dynamically.
	 * 
	 * @author Rajan R
	 * 
	 * @since 2015-05-18
	 * 
	 * @return vehicle details.
	 */
	private static Log log = LogFactory.getLog(VehicleService.class);

	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	
	@POST
	@Path("/AllVehicleDetails")
	public Response vehicleDetails(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader
				.getContext().getBean("IVendorDetailsBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
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
		
		List<Map<String, Object>> vehicleCheckIn = new ArrayList<Map<String, Object>>();
		int deviceCheckIn = 0;
		List<EFmFmVehicleCheckInPO> checkInDriver = iVehicleCheckInBO
				.getLastCheckInEntitiesDetails(eFmFmVendorMasterPO
						.geteFmFmClientBranchPO().getBranchId());
		for (EFmFmVehicleCheckInPO listOfCheckIn : checkInDriver) {
			Map<String, Object> checkInDetails = new HashMap<String, Object>();
			if (listOfCheckIn.geteFmFmDeviceMaster().getIsActive()
					.equalsIgnoreCase("Y")
					&& listOfCheckIn.getEfmFmVehicleMaster().getStatus()
							.equalsIgnoreCase("A")
					&& listOfCheckIn.getEfmFmDriverMaster().getStatus()
							.equalsIgnoreCase("A")
					&& listOfCheckIn.getStatus().equalsIgnoreCase("N")) {
				checkInDetails.put("vehicleId", listOfCheckIn
						.getEfmFmVehicleMaster().getVehicleId());
				checkInDetails.put("vendorId", listOfCheckIn
						.getEfmFmVehicleMaster().getEfmFmVendorMaster()
						.getVendorId());
				checkInDetails.put("vehicleMake", listOfCheckIn
						.getEfmFmVehicleMaster().getVehicleMake());
				checkInDetails.put("isReplacement", listOfCheckIn
						.getEfmFmVehicleMaster().getReplaceMentVehicleNum());
				checkInDetails.put("capacity", listOfCheckIn
						.getEfmFmVehicleMaster().getCapacity());
				checkInDetails.put("vehicleNumber", listOfCheckIn
						.getEfmFmVehicleMaster().getVehicleNumber());
				List<EFmFmVehicleCheckInPO> checkInVehicleDetail = iVehicleCheckInBO
						.getParticularCheckedInVehicles(eFmFmVendorMasterPO.getCombinedFacility(),
								listOfCheckIn.getEfmFmVehicleMaster()
										.getVehicleId());
				List<EFmFmVehicleCheckInPO> checkInDriverDetail = iVehicleCheckInBO
						.getParticularCheckedInDriverDetails(
								eFmFmVendorMasterPO.geteFmFmClientBranchPO()
										.getBranchId(), listOfCheckIn
										.getEfmFmDriverMaster().getDriverId());
				List<EFmFmVehicleCheckInPO> checkInDeviceDetail = iVehicleCheckInBO
						.getParticularCheckedInDeviceDetails(
								eFmFmVendorMasterPO.geteFmFmClientBranchPO()
										.getBranchId(), listOfCheckIn
										.geteFmFmDeviceMaster().getDeviceId());
				if (checkInDeviceDetail.isEmpty() && checkInVehicleDetail.isEmpty() && checkInDriverDetail.isEmpty()) {
					checkInDetails.put("DriverName", listOfCheckIn
							.getEfmFmDriverMaster().getFirstName());
					checkInDetails.put("MobileNumber", listOfCheckIn
							.getEfmFmDriverMaster().getMobileNumber());
					checkInDetails.put("driverId", listOfCheckIn
							.getEfmFmDriverMaster().getDriverId());
					checkInDetails.put("deviceId", listOfCheckIn
							.geteFmFmDeviceMaster().getDeviceId());
					checkInDetails.put("deviceNumber", listOfCheckIn
							.geteFmFmDeviceMaster().getMobileNumber());
					vehicleCheckIn.add(checkInDetails);
				}
			}
		}
		List<EFmFmDeviceMasterPO> allDevice = iVendorDetailsBO
				.getAllActiveDeviceDetails(eFmFmVendorMasterPO
						.geteFmFmClientBranchPO().getBranchId());
		List<EFmFmVendorMasterPO> allVendor = iVendorDetailsBO
				.getAllVendorsDetails(eFmFmVendorMasterPO);
		if (!(allVendor.isEmpty())) {
			for (EFmFmVendorMasterPO vendorList : allVendor) {
				List<EFmFmVehicleMasterPO> allVehicleByVendor = iVehicleCheckInBO
						.getAllActiveVehicleDetails(vendorList.getVendorId());
				List<EFmFmDriverMasterPO> allDriverByVendor = iVehicleCheckInBO
						.getAllActiveDriverDetails(vendorList.getVendorId());
				int driverCount = 0;
				if (!(allVehicleByVendor.isEmpty())) {
					for (EFmFmVehicleMasterPO allVehicleListList : allVehicleByVendor) {
						Map<String, Object> checkInDetails = new HashMap<String, Object>();
						checkInDetails.put("vehicleId",
								allVehicleListList.getVehicleId());
						checkInDetails.put("vehicleNumber",
								allVehicleListList.getVehicleNumber());
						checkInDetails.put("vehicleMake",
								allVehicleListList.getVehicleMake());
						checkInDetails.put("capacity",
								allVehicleListList.getCapacity());
						checkInDetails.put("replaceMentVehicle", allVehicleListList
								.getReplaceMentVehicleNum());
						if ((allDriverByVendor.size() - 1) >= driverCount) {
							checkInDetails.put("DriverName", allDriverByVendor
									.get(driverCount).getFirstName());
							checkInDetails.put("driverId", allDriverByVendor
									.get(driverCount).getDriverId());
							checkInDetails.put("vendorId", allDriverByVendor
									.get(driverCount).getEfmFmVendorMaster()
									.getVendorId());
							checkInDetails.put("MobileNumber",
									allDriverByVendor.get(driverCount)
											.getMobileNumber());
							if ((allDevice.size() - 1) >= deviceCheckIn) {
								checkInDetails.put("deviceId",
										allDevice.get(deviceCheckIn)
												.getDeviceId());
								checkInDetails.put("deviceNumber", allDevice
										.get(deviceCheckIn).getMobileNumber());
								deviceCheckIn++;
								List<EFmFmVehicleCheckInPO> checkInVehicleDetail = iVehicleCheckInBO
										.getParticularCheckedInVehicles(
												eFmFmVendorMasterPO.getCombinedFacility(),
												allVehicleListList
														.getVehicleId());
								List<EFmFmVehicleCheckInPO> checkInDriverDetail = iVehicleCheckInBO
										.getParticularCheckedInDriverDetails(
												eFmFmVendorMasterPO
														.geteFmFmClientBranchPO()
														.getBranchId(),
												allDriverByVendor.get(
														driverCount)
														.getDriverId());
								List<EFmFmVehicleCheckInPO> checkInDeviceDetail = iVehicleCheckInBO
										.getParticularCheckedInDeviceDetails(
												eFmFmVendorMasterPO
														.geteFmFmClientBranchPO()
														.getBranchId(),
												allDevice.get(deviceCheckIn)
														.getDeviceId());
				                if (checkInDeviceDetail.isEmpty() && checkInVehicleDetail.isEmpty() && checkInDriverDetail.isEmpty()) {
									vehicleCheckIn.add(checkInDetails);
								}
							}
							driverCount++;
						}

					}

				}
			}
		}
		 log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(vehicleCheckIn, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * The driverCheckIn method implemented. for creating check in details for
	 * vehicle and drivers.
	 * 
	 * @author Rajan R
	 * 
	 * @since 2015-05-20
	 * 
	 * @return driver checkIn details.
	 */
	@POST
	@Path("/driverCheckIn")
	public Response driverCheckIn(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleCheckInPO.getUserId());
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleCheckInPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleCheckInPO.getUserId());
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
		String response = "alreadyExist";
		List<EFmFmVehicleCheckInPO> alreadyCheckInVehicleDetail = iVehicleCheckInBO
				.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
		
		EFmFmDriverMasterPO efmFmDriverMaster = new EFmFmDriverMasterPO();
		EFmFmVehicleMasterPO eFmFmVehicleMaster = new EFmFmVehicleMasterPO();
		EFmFmDeviceMasterPO eFmFmDeviceMasterPO = new EFmFmDeviceMasterPO();
		efmFmDriverMaster.setDriverId(eFmFmVehicleCheckInPO
				.getEfmFmDriverMaster().getDriverId());
		eFmFmVehicleMaster.setVehicleId(eFmFmVehicleCheckInPO
				.getEfmFmVehicleMaster().getVehicleId());
		eFmFmDeviceMasterPO.setDeviceId(eFmFmVehicleCheckInPO
				.geteFmFmDeviceMaster().getDeviceId());

		List<EFmFmVehicleCheckInPO> checkInVehicleDetail = iVehicleCheckInBO
				.getParticularCheckedInVehicles(eFmFmVehicleCheckInPO
						.getCombinedFacility(), eFmFmVehicleCheckInPO
						.getEfmFmVehicleMaster().getVehicleId());
		
		if (!(checkInVehicleDetail.isEmpty())) {
			EFmFmVehicleMasterPO alreadyCheckInVehicle = new EFmFmVehicleMasterPO();
			alreadyCheckInVehicle.setVehicleId(alreadyCheckInVehicleDetail
					.get(0).getEfmFmVehicleMaster().getVehicleId());
			checkInVehicleDetail.get(0).setEfmFmVehicleMaster(
					alreadyCheckInVehicle);
			iVehicleCheckInBO.update(checkInVehicleDetail.get(0));
		}

		List<EFmFmVehicleCheckInPO> checkInDriverDetail = iVehicleCheckInBO
				.getParticularCheckedInDriverDetails(eFmFmVehicleCheckInPO
						.getBranchId(), eFmFmVehicleCheckInPO
						.getEfmFmDriverMaster().getDriverId());
		
		if (!(checkInDriverDetail.isEmpty())) {
			EFmFmDriverMasterPO alreadyCheckInDriver = new EFmFmDriverMasterPO();
			alreadyCheckInDriver.setDriverId(alreadyCheckInVehicleDetail.get(0)
					.getEfmFmDriverMaster().getDriverId());
			checkInDriverDetail.get(0).setEfmFmDriverMaster(
					alreadyCheckInDriver);
			iVehicleCheckInBO.update(checkInDriverDetail.get(0));
		}

		List<EFmFmVehicleCheckInPO> checkInDeviceDetail = iVehicleCheckInBO
				.getParticularCheckedInDeviceDetails(eFmFmVehicleCheckInPO
						.getBranchId(), eFmFmVehicleCheckInPO
						.geteFmFmDeviceMaster().getDeviceId());
		
		if (!(checkInDeviceDetail.isEmpty())) {
			EFmFmDeviceMasterPO alreadyCheckInDevice = new EFmFmDeviceMasterPO();
			alreadyCheckInDevice.setDeviceId(alreadyCheckInVehicleDetail.get(0)
					.geteFmFmDeviceMaster().getDeviceId());
			checkInDeviceDetail.get(0).seteFmFmDeviceMaster(
					alreadyCheckInDevice);
			iVehicleCheckInBO.update(checkInDeviceDetail.get(0));

		}
		
		alreadyCheckInVehicleDetail.get(0).setEfmFmDriverMaster(
				efmFmDriverMaster);
		alreadyCheckInVehicleDetail.get(0).setEfmFmVehicleMaster(
				eFmFmVehicleMaster);
		alreadyCheckInVehicleDetail.get(0).seteFmFmDeviceMaster(
				eFmFmDeviceMasterPO);
		iVehicleCheckInBO.update(alreadyCheckInVehicleDetail.get(0));

		response = "success";
		 log.info("serviceEnd -UserId :" + eFmFmVehicleCheckInPO.getUserId());
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}

	// driver,vehicle or device changing

	@POST
	@Path("/changeCheckInEntity")
	public Response changingCheckInEntities(
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleCheckInPO.getUserId());
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleCheckInPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleCheckInPO.getUserId());
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
		String response = "alreadyExist";
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * update vehicle capacity and type
	 * 
	 * 
	 * @author Sarfraz khan
	 * 
	 * @since 2015-06-24
	 * 
	 * @return vehicle type configuration
	 */

	@POST
	@Path("/addVehicleTypeConf")
	public Response addVehicleTypeConfiguration(EmployeeListJSONBO vehicleType) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + vehicleType.getUserId());
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vehicleType.getUserId()))){
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(vehicleType.getUserId());
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
		vehicleType.setCombinedFacility(vehicleType.getCombinedFacility());
		StringTokenizer stringTokenizer = new StringTokenizer(vehicleType.getCombinedFacility(), ",");
        String branchId = "";
		while (stringTokenizer.hasMoreElements()) {
			branchId = (String) stringTokenizer.nextElement();	
		List<EFmFmVehicleCapacityMasterPO> vehicleTypeConf = vehicleType.getVehicleTypeConf();
		if (!(vehicleTypeConf.isEmpty())) {
			List<EFmFmVehicleCapacityMasterPO> emptyVehicle = iVehicleCheckInBO.getVehicleTypeBranchWise(branchId);
			if(!emptyVehicle.isEmpty()){
				for(EFmFmVehicleCapacityMasterPO vehicle:emptyVehicle){
					iVehicleCheckInBO.deleteVehicleTypeBranchId(vehicle.getVehicleCapacityId());					
				}
			}
			
			for (EFmFmVehicleCapacityMasterPO vehicleCapType : vehicleTypeConf) {
				EFmFmClientBranchPO eFmFmClientBranchPO =new EFmFmClientBranchPO();
				eFmFmClientBranchPO.setBranchId(Integer.parseInt(branchId));
				EFmFmVehicleCapacityMasterPO vehicleDetail = new EFmFmVehicleCapacityMasterPO();
				vehicleDetail.setCapacity(vehicleCapType.getSelectCapacity());
				vehicleDetail.setVehicleType(vehicleCapType.getVehicleType());
				vehicleDetail.setAvailableSeat(vehicleCapType.getSelectCapacity()-1);
				vehicleDetail.setCreationDate(new Date());
				vehicleDetail.setStatus("Y");
				vehicleDetail.setUpdatedTime(new Date());
				vehicleDetail.seteFmFmClientBranchPO(eFmFmClientBranchPO);
				iVehicleCheckInBO.save(vehicleDetail);
			}
		}
		}
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + vehicleType.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	
	/**
	 * get vehicle capacity and type
	 * 
	 * 
	 * @author Sarfraz khan
	 * 
	 * @since 2017-06-25
	 * 
	 * @return vehicle type configuration
	 */

	@POST
	@Path("/getVehicleCapacityType")
	public Response getVehicleTypeConfiguration(EmployeeListJSONBO vehicleType) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		List<Map<String, Object>> vehcielTypeData = new ArrayList<Map<String, Object>>();
	
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + vehicleType.getUserId());
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vehicleType.getUserId()))){
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
//				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(vehicleType.getUserId());
//				   if (!(userDetail.isEmpty())) {
//					String jwtToken = "";
//					try {
//					 JwtTokenGenerator token = new JwtTokenGenerator();
//					 jwtToken = token.generateToken();
//					 userDetail.get(0).setAuthorizationToken(jwtToken);
//					 userDetail.get(0).setTokenGenerationTime(new Date());
//					 userMasterBO.update(userDetail.get(0));
//					} catch (Exception e) {
//					 log.info("error" + e);
//					}
//			   }
//			}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
		vehicleType.setCombinedFacility("1,2");
		List<EFmFmVehicleCapacityMasterPO> vehicleTypeConf = iVehicleCheckInBO.getVehicleTypeBranchWise(vehicleType.getCombinedFacility());
			for (EFmFmVehicleCapacityMasterPO vehicleCapType : vehicleTypeConf) {
				Map<String, Object> vehileDetail = new HashMap<String, Object>();
				vehileDetail.put("vehicleCapacityId", vehicleCapType.getVehicleCapacityId());
				vehileDetail.put("vehicleType", vehicleCapType.getVehicleType());
				vehileDetail.put("selectCapacity", vehicleCapType.getCapacity());
				vehcielTypeData.add(vehileDetail);	
				
			}
		responce.put("status", "success");
		responce.put("vehicleTypeData", vehcielTypeData);
		 log.info("serviceEnd -UserId :" + vehicleType.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	

	/**
	 * remove a  vehicle capacity and type
	 * 
	 * 
	 * @author Sarfraz khan
	 * 
	 * @since 2017-06-25
	 * 
	 * @return vehicle type configuration
	 */

	@POST
	@Path("/removeVehicleType")
	public Response removeVehicleTypeFromConf(EmployeeListJSONBO vehicleType) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		List<Map<String, Object>> vehcielTypeData = new ArrayList<Map<String, Object>>();
	
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + vehicleType.getUserId());
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),vehicleType.getUserId()))){
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
//				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(vehicleType.getUserId());
//				   if (!(userDetail.isEmpty())) {
//					String jwtToken = "";
//					try {
//					 JwtTokenGenerator token = new JwtTokenGenerator();
//					 jwtToken = token.generateToken();
//					 userDetail.get(0).setAuthorizationToken(jwtToken);
//					 userDetail.get(0).setTokenGenerationTime(new Date());
//					 userMasterBO.update(userDetail.get(0));
//					} catch (Exception e) {
//					 log.info("error" + e);
//					}
//			   }
//			}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
		
		iVehicleCheckInBO.deleteVehicleTypeBranchId(vehicleType.getVehicleCapacityId());					
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + vehicleType.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	
	
	/**
	 * Method for getting listOfCheckedInVehicles entities including dummy
	 * 
	 * 
	 * @author Sarfraz khan
	 * 
	 * @since 2015-05-21
	 * 
	 * @return checkedIn vehicle details.
	 */

	@POST
	@Path("/listOfCheckedInVehicles")
	public Response listOfCheckedInVehicles(
			EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
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
		List<Map<String, Object>> listOfVehicle = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleCheckInPO> listOfCheckedInVehicle = iVehicleCheckInBO
				.getAllCheckedInVehicleDetails(new MultifacilityService().combinedBranchIdDetails(eFmFmVendorMasterPO.getUserId(),eFmFmVendorMasterPO.getCombinedFacility()));
		if (!(listOfCheckedInVehicle.isEmpty())) {
			for (EFmFmVehicleCheckInPO vehicleDetails : listOfCheckedInVehicle) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("checkInId", vehicleDetails.getCheckInId());
				vehicleList.put("driverName", vehicleDetails
						.getEfmFmDriverMaster().getFirstName());
				vehicleList.put("driverNumber", vehicleDetails
						.getEfmFmDriverMaster().getMobileNumber());
				vehicleList.put("driverId", vehicleDetails
						.getEfmFmDriverMaster().getDriverId());
				vehicleList.put("DriverName", vehicleDetails
						.getEfmFmDriverMaster().getFirstName());
				vehicleList.put("MobileNumber", vehicleDetails
						.getEfmFmDriverMaster().getMobileNumber());
				vehicleList.put("deviceNumber", vehicleDetails
						.geteFmFmDeviceMaster().getMobileNumber());
				vehicleList.put("deviceId", vehicleDetails
						.geteFmFmDeviceMaster().getDeviceId());
				vehicleList.put("vehicleMake", vehicleDetails
						.getEfmFmVehicleMaster().getVehicleMake());
				vehicleList.put("capacity", vehicleDetails
						.getEfmFmVehicleMaster().getCapacity());
				vehicleList.put("vehicleNumber", vehicleDetails
						.getEfmFmVehicleMaster().getVehicleNumber());
				vehicleList.put("vehicleId", vehicleDetails
						.getEfmFmVehicleMaster().getVehicleId());
				vehicleList.put("vendorId", vehicleDetails
						.getEfmFmVehicleMaster().getEfmFmVendorMaster()
						.getVendorId());
				vehicleList.put("vendorName", vehicleDetails
						.getEfmFmVehicleMaster().getEfmFmVendorMaster()
						.getVendorName());
				vehicleList.put("capacity", vehicleDetails
						.getEfmFmVehicleMaster().getCapacity());
				vehicleList.put("mileage", vehicleDetails.getEfmFmVehicleMaster().getMileage());
				vehicleList.put("status", vehicleDetails.getStatus());
				vehicleList.put("facilityName", vehicleDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				listOfVehicle.add(vehicleList);

			}
		}
		 log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(listOfVehicle, MediaType.APPLICATION_JSON).build();
	}
	
	
	
	
	
	

	/**
	 * Method for getting Available entities Excluding dummy
	 * 
	 * 
	 * @author Sarfraz khan
	 * 
	 * @since 2015-05-21
	 * 
	 * @return checkedIn vehicle details.
	 */

	@POST
	@Path("/listOfAvailableVehicles")
	public Response listOfCheckedInAvailableVehiclesWithoutDummy(
			EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader
				.getContext().getBean("IAssignRouteBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
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
		List<Map<String, Object>> listOfVehicle = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleCheckInPO> listOfCheckedInVehicle = iVehicleCheckInBO
				.getCheckedInVehicleDetailsWithOutDummyVehicles(new MultifacilityService().combinedBranchIdDetails(eFmFmVendorMasterPO.getUserId(),eFmFmVendorMasterPO.getCombinedFacility()));
		if(!(listOfCheckedInVehicle.isEmpty())) {
			log.info("listOfCheckedInVehicle"+listOfCheckedInVehicle.size());
			for (EFmFmVehicleCheckInPO vehicleDetails : listOfCheckedInVehicle) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("checkInId", vehicleDetails.getCheckInId());
				vehicleList.put("driverName", vehicleDetails
						.getEfmFmDriverMaster().getFirstName());
				vehicleList.put("driverNumber", vehicleDetails
						.getEfmFmDriverMaster().getMobileNumber());
				vehicleList.put("driverId", vehicleDetails
						.getEfmFmDriverMaster().getDriverId());
				vehicleList.put("DriverName", vehicleDetails
						.getEfmFmDriverMaster().getFirstName());
				vehicleList.put("MobileNumber", vehicleDetails
						.getEfmFmDriverMaster().getMobileNumber());
				vehicleList.put("deviceNumber", vehicleDetails
						.geteFmFmDeviceMaster().getMobileNumber());
				vehicleList.put("deviceId", vehicleDetails
						.geteFmFmDeviceMaster().getDeviceId());
				vehicleList.put("vehicleMake", vehicleDetails
						.getEfmFmVehicleMaster().getVehicleMake());
				vehicleList.put("capacity", vehicleDetails
						.getEfmFmVehicleMaster().getCapacity());
				vehicleList.put("vehicleNumber", vehicleDetails
						.getEfmFmVehicleMaster().getVehicleNumber());
				vehicleList.put("vehicleId", vehicleDetails
						.getEfmFmVehicleMaster().getVehicleId());
				vehicleList.put("vendorId", vehicleDetails
						.getEfmFmVehicleMaster().getEfmFmVendorMaster()
						.getVendorId());
				vehicleList.put("vendorName", vehicleDetails
						.getEfmFmVehicleMaster().getEfmFmVendorMaster()
						.getVendorName());
				vehicleList.put("capacity", vehicleDetails
						.getEfmFmVehicleMaster().getCapacity());
				vehicleList.put("status", vehicleDetails.getStatus());
				vehicleList.put("facilityName", vehicleDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				listOfVehicle.add(vehicleList);

			}
		}
		
		try{
			List<EFmFmVehicleCheckInPO> listOfAssignedVehicles = iVehicleCheckInBO
					.getCheckedInVehicleDetailsWithOutDummyAllocatedToRoutes(eFmFmVendorMasterPO
							.geteFmFmClientBranchPO().getBranchId());
	         if(!listOfAssignedVehicles.isEmpty()){
	 			log.info("listOfCheckedInVehicleNotInAssignTable"+listOfAssignedVehicles.size());
	        	 for(EFmFmVehicleCheckInPO vehicleDetails:listOfAssignedVehicles){
	        		 List<EFmFmAssignRoutePO> vehiclesExistCheck = assignRouteBO.getCheckInTripAllocationCheckAfterTripCompletion(vehicleDetails.getCheckInId(), eFmFmVendorMasterPO
								.geteFmFmClientBranchPO().getBranchId());
	 				Map<String, Object> vehicleList = new HashMap<String, Object>();
	 				if(vehiclesExistCheck.isEmpty()){
	 				vehicleList.put("checkInId", vehicleDetails.getCheckInId());
	 				vehicleList.put("driverName", vehicleDetails
	 						.getEfmFmDriverMaster()
	 						.getFirstName());
	 				vehicleList.put("driverNumber", vehicleDetails
	 						.getEfmFmDriverMaster()
	 						.getMobileNumber());
	 				vehicleList.put("driverId", vehicleDetails
	 						.getEfmFmDriverMaster()
	 						.getDriverId());
	 				vehicleList.put("DriverName", vehicleDetails
	 						.getEfmFmDriverMaster()
	 						.getFirstName());
	 				vehicleList.put("MobileNumber", vehicleDetails
	 						.getEfmFmDriverMaster()
	 						.getMobileNumber());
	 				vehicleList.put("deviceNumber", vehicleDetails
	 						.geteFmFmDeviceMaster()
	 						.getMobileNumber());
	 				vehicleList.put("deviceId", vehicleDetails
	 						.geteFmFmDeviceMaster()
	 						.getDeviceId());
	 				vehicleList.put("vehicleMake", vehicleDetails
	 						.getEfmFmVehicleMaster()
	 						.getVehicleMake());
	 				vehicleList.put("capacity", vehicleDetails
	 						.getEfmFmVehicleMaster()
	 						.getCapacity());
	 				vehicleList.put("vehicleNumber", vehicleDetails
	 						.getEfmFmVehicleMaster()
	 						.getVehicleNumber());
	 				vehicleList.put("vehicleId", vehicleDetails
	 						.getEfmFmVehicleMaster()
	 						.getVehicleId());
	 				vehicleList.put("vendorId", vehicleDetails
	 						.getEfmFmVehicleMaster()
	 						.getEfmFmVendorMaster().getVendorId());
	 				vehicleList.put("vendorName", vehicleDetails
	 						.getEfmFmVehicleMaster()
	 						.getEfmFmVendorMaster().getVendorName());
	 				vehicleList.put("capacity", vehicleDetails
	 						.getEfmFmVehicleMaster()
	 						.getCapacity());
	 				vehicleList.put("status", vehicleDetails
	 						.getStatus());
	 				vehicleList.put("facilityName", vehicleDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
	 				listOfVehicle.add(vehicleList);
	 				}
	 			}
	         }
			}catch(Exception e){
	 			log.info("Error"+e);
			}
		
		
		 log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		
		return Response.ok(listOfVehicle, MediaType.APPLICATION_JSON).build();
	}
	
	/*
	 * 
	 *  Not yet vehicle checkin Details
	 */

	
	
	@POST
	@Path("/listOfYetToCheckedInVehicles")
	public Response listOfCheckedInVehicles(EFmFmClientBranchPO eFmFmClientBranchPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
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
		List<Map<String, Object>> listOfVehicle = new ArrayList<Map<String, Object>>();		
		DateFormat shiftDateFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		List<EFmFmVehicleMasterPO> listOfCheckedInVehicle = iVehicleCheckInBO.getlistOfYetToCheckInVehicleDetails(eFmFmClientBranchPO.getBranchId());
		if (!(listOfCheckedInVehicle.isEmpty())) {
			for (EFmFmVehicleMasterPO vehicleDetails : listOfCheckedInVehicle) {
				log.info("listOfCheckedInVehicle"+listOfCheckedInVehicle.size());
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("vendorName", vehicleDetails.getEfmFmVendorMaster().getVendorName());
				vehicleList.put("vehicleId", vehicleDetails.getVehicleId());
				vehicleList.put("vehicleNumber", vehicleDetails.getVehicleNumber());				
				vehicleList.put("capcity", vehicleDetails.getCapacity());
				vehicleList.put("vehicleMake", vehicleDetails.getVehicleModel());
				vehicleList.put("facilityName", vehicleDetails.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());

				List<EFmFmVehicleCheckInPO> vehicleDetailsList = iVehicleCheckInBO.
						getLastCheckInVehicleIdDetails(eFmFmClientBranchPO.getBranchId(),vehicleDetails.getVehicleId());
				List<Map<String, Object>> listOfVehicleList = new ArrayList<Map<String, Object>>();
				vehicleList.put("driverLength", vehicleDetailsList.size());
				if(!vehicleDetailsList.isEmpty()){				
						for(EFmFmVehicleCheckInPO vehicleDriverDetails : vehicleDetailsList) {							
							Map<String, Object> vehicleDriverDetailsList = new HashMap<String, Object>();							
							vehicleDriverDetailsList.put("checkInTime",shiftDateFormater.format(vehicleDriverDetails.getCheckInTime()));
							vehicleDriverDetailsList.put("checkInOut", shiftDateFormater.format(vehicleDriverDetails.getCheckOutTime()));							
							vehicleDriverDetailsList.put("driverId", vehicleDriverDetails.getEfmFmDriverMaster().getDriverId());
							vehicleDriverDetailsList.put("driverName", vehicleDriverDetails.getEfmFmDriverMaster().getFirstName());
							vehicleDriverDetailsList.put("deviceId", vehicleDriverDetails.geteFmFmDeviceMaster().getDeviceId());
							listOfVehicleList.add(vehicleDriverDetailsList);					
						}
						vehicleList.put("driverDetails", listOfVehicleList);
				}
				listOfVehicle.add(vehicleList);	
				
			}
		}
		 log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(listOfVehicle, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * The listOfOnRoadVehicles method implemented. for getting the list of
	 * checkedIn vehicle details.
	 * 
	 * @author Rajan R
	 * 
	 * @since 2015-05-21
	 * 
	 * @return OnRoad vehicle details.
	 */
	@POST
	@Path("/vehicleonroad")
	public Response vehiclesOnRoad(EFmFmAssignRoutePO assignRoutePO) {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader
				.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
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
		List<Map<String, Object>> listOfVehicle = new ArrayList<Map<String, Object>>();
		assignRoutePO.setCombinedFacility(new MultifacilityService().combinedBranchIdDetails(assignRoutePO.getUserId(),assignRoutePO.getCombinedFacility()));
		List<EFmFmAssignRoutePO> vehiclesOnRoad = assignRouteBO
				.getAllLiveTrips(assignRoutePO);
		if (!(vehiclesOnRoad.isEmpty())) {
			for (EFmFmAssignRoutePO vehicleDetails : vehiclesOnRoad) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("checkInId", vehicleDetails
						.getEfmFmVehicleCheckIn().getCheckInId());
				vehicleList.put("driverName", vehicleDetails
						.getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
						.getFirstName());
				vehicleList.put("driverNumber", vehicleDetails
						.getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
						.getMobileNumber());
				vehicleList.put("driverId", vehicleDetails
						.getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
						.getDriverId());
				vehicleList.put("DriverName", vehicleDetails
						.getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
						.getFirstName());
				vehicleList.put("MobileNumber", vehicleDetails
						.getEfmFmVehicleCheckIn().getEfmFmDriverMaster()
						.getMobileNumber());
				vehicleList.put("deviceNumber", vehicleDetails
						.getEfmFmVehicleCheckIn().geteFmFmDeviceMaster()
						.getMobileNumber());
				vehicleList.put("deviceId", vehicleDetails
						.getEfmFmVehicleCheckIn().geteFmFmDeviceMaster()
						.getDeviceId());
				vehicleList.put("vehicleMake", vehicleDetails
						.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getVehicleMake());
				vehicleList.put("capacity", vehicleDetails
						.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getCapacity());
				vehicleList.put("vehicleNumber", vehicleDetails
						.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getVehicleNumber());
				vehicleList.put("vehicleId", vehicleDetails
						.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getVehicleId());
				vehicleList.put("vendorId", vehicleDetails
						.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorId());
				vehicleList.put("vendorName", vehicleDetails
						.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorName());
				vehicleList.put("capacity", vehicleDetails
						.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getCapacity());
				vehicleList.put("status", vehicleDetails
						.getEfmFmVehicleCheckIn().getStatus());
				vehicleList.put("facilityName", vehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				listOfVehicle.add(vehicleList);
			}
		}
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(listOfVehicle, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * The addingVehicleInspection method implemented. for Adding vehicle inspection details.
	 * 
	 * @author Sarfraz KHan
	 * 
	 * @since 2016-02-13
	 * 
	 * @return added status.
	 */
	@POST
	@Path("/addingVehicleInspection")
	public Response addingVehicleIncepaction(EFmFmVehicleInspectionPO eFmFmVehicleInspectionPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleInspectionPO.getUserId());
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleInspectionPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleInspectionPO.getUserId());
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
		log.info("Inspection service"+eFmFmVehicleInspectionPO.toString());
		log.info("driverName"+eFmFmVehicleInspectionPO.getDriverName());
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		eFmFmVehicleInspectionPO.setInspectionDate(new Date());
		eFmFmVehicleInspectionPO.setStatus("Y");
		iVehicleCheckInBO.save(eFmFmVehicleInspectionPO);
		 log.info("serviceEnd -UserId :" + eFmFmVehicleInspectionPO.getUserId());
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	}
	
	
	/**
	 * The getting All VehicleInspection method implemented. Basis Of DATE.
	 * 
	 * @author Sarfraz Khan
	 * 
	 * @since 2016-02-13
	 * 
	 * @return Return all the VehicleInspection Date vice.
	 * @throws ParseException 
	 * @throws UnsupportedEncodingException 
	 */
	@POST
	@Path("/getVehicleInspection")
	public Response getAllVehicleIncepactionByDate(EFmFmVehicleInspectionPO eFmFmVehicleInspectionPO) throws ParseException, UnsupportedEncodingException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleInspectionPO.getUserId());
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleInspectionPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleInspectionPO.getUserId());
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
		List<Map<String, Object>> listOfVehicle = new ArrayList<Map<String, Object>>();	
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = formatter.parse(eFmFmVehicleInspectionPO.getFromDate());
		Date toDate = formatter.parse(eFmFmVehicleInspectionPO.getToDate());
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

		log.info("Get Inspection detail fromDate"+fromDate);
		log.info("Get Inspection detail toDate"+toDate);
		List<EFmFmVehicleInspectionPO> inspectionDetails=null;		
		if(eFmFmVehicleInspectionPO.getVendorId()==0){
			inspectionDetails=iVehicleCheckInBO.getAllVendorInspectionsByBranchIdAndDate(fromDate, toDate, eFmFmVehicleInspectionPO.getBranchId());
			
		}else if(eFmFmVehicleInspectionPO.getVendorId() !=0 && eFmFmVehicleInspectionPO.getEfmFmVehicleMaster().getVehicleId()==0){
			inspectionDetails=iVehicleCheckInBO.
					getVendorInspectionsByBranchIdAndDate(fromDate, toDate, eFmFmVehicleInspectionPO.getBranchId(),eFmFmVehicleInspectionPO.getVendorId());
		}else{			
			inspectionDetails=iVehicleCheckInBO.
					getAllVehicleInspectionsByBranchIdVehicleIdAndDate(fromDate, toDate, eFmFmVehicleInspectionPO.getBranchId(), eFmFmVehicleInspectionPO.getEfmFmVehicleMaster().getVehicleId());
		}
		if (!(inspectionDetails.isEmpty())){
		 for (EFmFmVehicleInspectionPO vehicleDetails : inspectionDetails) {
			 Map<String, Object> inspectiondocuments = new HashMap<String, Object>();

			 Map<String, Object> inspectionlist = new HashMap<String, Object>();
			 try{
			 if(vehicleDetails.getVehiclePhotoPathFromFront()!=null){
				 inspectiondocuments.put("vehicleInsepectionFrontImage",vehicleDetails.getVehiclePhotoPathFromFront().substring(vehicleDetails.getVehiclePhotoPathFromFront().indexOf("upload")-1));
			 }}catch(Exception e){}
			 try{
			 if(vehicleDetails.getVehiclePhotoPathFromBack()!=null){
				 inspectiondocuments.put("vehicleInsepectionBackImage",vehicleDetails.getVehiclePhotoPathFromBack().substring(vehicleDetails.getVehiclePhotoPathFromBack().indexOf("upload")-1));
			 }
			 }catch(Exception e){}
			 try{
			 if(vehicleDetails.getVehiclePhotoPathFromLeft()!=null){
				 inspectiondocuments.put("vehicleInsepectionLeftImage",vehicleDetails.getVehiclePhotoPathFromLeft().substring(vehicleDetails.getVehiclePhotoPathFromLeft().indexOf("upload")-1));
			 }
			 }catch(Exception e){}
			 try{
			 if(vehicleDetails.getVehiclePhotoPathFromRight()!=null){
				 inspectiondocuments.put("vehicleInsepectionRightImage",vehicleDetails.getVehiclePhotoPathFromRight().substring(vehicleDetails.getVehiclePhotoPathFromRight().indexOf("upload")-1));
			 }
			 }catch(Exception e){}
			 
			 inspectionlist.put("vehicleNumber",vehicleDetails.getEfmFmVehicleMaster().getVehicleNumber());
			 inspectionlist.put("driverName",vehicleDetails.getDriverName());
			 
			 inspectionlist.put("inspectionId",vehicleDetails.getInspectionId());
			 inspectionlist.put("inspectionDate",dateFormatter.format(vehicleDetails.getInspectionDate()));
			 inspectionlist.put("documentRc",vehicleDetails.isDocumentRc());
			 inspectionlist.put("documentInsurance",vehicleDetails.isDocumentInsurance());
			 inspectionlist.put("documentDriverLicence",vehicleDetails.isDocumentDriverLicence());
			 inspectionlist.put("documentUpdateJmp",vehicleDetails.isDocumentUpdateJmp());
			 inspectionlist.put("firstAidKit",vehicleDetails.isFirstAidKit());
			 inspectionlist.put("fireExtingusher",vehicleDetails.isFireExtingusher());
			 inspectionlist.put("allSeatBeltsWorking",vehicleDetails.isAllSeatBeltsWorking());
			 inspectionlist.put("placard",vehicleDetails.isPlacard());
			 inspectionlist.put("mosquitoBat",vehicleDetails.isMosquitoBat());
			 inspectionlist.put("panicButton",vehicleDetails.isPanicButton());
			 inspectionlist.put("walkyTalky",vehicleDetails.isWalkyTalky());
			 inspectionlist.put("gps",vehicleDetails.isGps());
			 inspectionlist.put("driverUniform",vehicleDetails.isDriverUniform());
			 inspectionlist.put("stephney",vehicleDetails.isStephney());
			 inspectionlist.put("umbrella",vehicleDetails.isUmbrella());
			 inspectionlist.put("torch",vehicleDetails.isTorch());
			 inspectionlist.put("toolkit",vehicleDetails.isToolkit());
			 inspectionlist.put("seatUpholtseryCleanNotTorn",vehicleDetails.isSeatUpholtseryCleanNotTorn());
			 inspectionlist.put("vehcileRoofUpholtseryCleanNotTorn",vehicleDetails.isVehcileRoofUpholtseryCleanNotTorn());
			 inspectionlist.put("vehcileFloorUpholtseryCleanNotTorn",vehicleDetails.isVehcileFloorUpholtseryCleanNotTorn());
			 inspectionlist.put("vehcileDashboardClean",vehicleDetails.isVehcileDashboardClean());
			 inspectionlist.put("vehicleGlassCleanStainFree",vehicleDetails.isVehicleGlassCleanStainFree());
			 inspectionlist.put("vehicleInteriorLightBrightWorking",vehicleDetails.isVehicleInteriorLightBrightWorking());
			 inspectionlist.put("bolsterSeperatingLastTwoSeats",vehicleDetails.isBolsterSeperatingLastTwoSeats());
			 inspectionlist.put("exteriorScratches",vehicleDetails.isExteriorScratches());
			 inspectionlist.put("noPatchWork",vehicleDetails.isNoPatchWork());
			 inspectionlist.put("pedalBrakeWorking",vehicleDetails.isPedalBrakeWorking());
			 inspectionlist.put("handBrakeWorking",vehicleDetails.isHandBrakeWorking());
			 inspectionlist.put("tyresNoDentsTrimWheel",vehicleDetails.isTyresNoDentsTrimWheel());
			 inspectionlist.put("tyresGoodCondition",vehicleDetails.isTyresGoodCondition());
			 inspectionlist.put("allTyresAndStephneyInflate",vehicleDetails.isAllTyresAndStephneyInflate());
			 inspectionlist.put("jackAndTool",vehicleDetails.isJackAndTool());
			 inspectionlist.put("numberofPunctruresdone",vehicleDetails.getNumberofPunctruresdone());
			 inspectionlist.put("wiperWorking",vehicleDetails.isWiperWorking());
			 inspectionlist.put("acCoolingIn5mins",vehicleDetails.isAcCoolingIn5mins());
			 inspectionlist.put("noSmellInAC",vehicleDetails.isNoSmellInAC());
			 inspectionlist.put("acVentsClean",vehicleDetails.isAcVentsClean());
			 inspectionlist.put("engionOilChangeIndicatorOn",vehicleDetails.isEnginOilChangeIndicatorOn());
			 inspectionlist.put("coolantIndicatorOn",vehicleDetails.isCoolantIndicatorOn());
			 inspectionlist.put("brakeClutchIndicatorOn",vehicleDetails.isBrakeClutchIndicatorOn());
			 inspectionlist.put("highBeamWorking",vehicleDetails.isHighBeamWorking());
			 inspectionlist.put("lowBeamWorking",vehicleDetails.isLowBeamWorking());			 
			 inspectionlist.put("rightFrontIndicatorWorking",vehicleDetails.isRightFromtIndicatorWorking());
			 inspectionlist.put("leftFrontIndicatorWorking",vehicleDetails.isLeftFrontIndicatorWorking());
			 inspectionlist.put("parkingLightsWorking",vehicleDetails.isParkingLightsWorking());
			 inspectionlist.put("ledDayTimeRunningLightWorking",vehicleDetails.isLedDayTimeRunningLightWorking());
			 inspectionlist.put("rightRearIndicatorWorking",vehicleDetails.isRightRearIndicatorWorking());
			 inspectionlist.put("leftRearIndicatorWorking",vehicleDetails.isLeftRearIndicatorWorking());
			 inspectionlist.put("brakeLightsOn",vehicleDetails.isBrakeLightsOn());
			 inspectionlist.put("reverseLightsOn",vehicleDetails.isReverseLightsOn());
			 inspectionlist.put("diesel",vehicleDetails.getDiesel());
			 inspectionlist.put("hornWorking",vehicleDetails.isHornWorking());
			 inspectionlist.put("reflectionSignBoard",vehicleDetails.isReflectionSignBoard());
			 inspectionlist.put("audioWorkingOrNot",vehicleDetails.isAudioWorkingOrNot());
	
			 inspectionlist.put("vehicleInspector",new String(Base64.getDecoder().decode(vehicleDetails.getEfmFmUserMaster().getFirstName()), "utf-8"));
			 
			 inspectionlist.put("vehicleInspectorUserId",vehicleDetails.getEfmFmUserMaster().getUserId());	
			 
			 inspectionlist.put("fogLights",vehicleDetails.isFogLights());
			 inspectionlist.put("driverCheckInDidOrNot",vehicleDetails.isDriverCheckInDidOrNot());
			 inspectionlist.put("feedback",vehicleDetails.isFeedback());

			 //Vehicle Remarks			 
			inspectionlist.put("documentRcRemarks",vehicleDetails.getDocumentRcRemarks());
			inspectionlist.put("documentInsuranceRemarks",vehicleDetails.getDocumentInsuranceRemarks());
			inspectionlist.put("documentDriverLicenceRemarks",vehicleDetails.getDocumentDriverLicenceRemarks());
			inspectionlist.put("documentUpdateJmpRemarks",vehicleDetails.getDocumentUpdateJmpRemarks());
			inspectionlist.put("firstAidKitRemarks",vehicleDetails.getFirstAidKitRemarks());
			inspectionlist.put("fireExtingusherRemarks",vehicleDetails.getFireExtingusherRemarks());
			inspectionlist.put("allSeatBeltsWorkingRemarks",vehicleDetails.getAllSeatBeltsWorkingRemarks());
			inspectionlist.put("placardRemarks",vehicleDetails.getPlacardRemarks());
			inspectionlist.put("mosquitoBatRemarks",vehicleDetails.getMosquitoBatRemarks());
			inspectionlist.put("panicButtonRemarks",vehicleDetails.getPanicButtonRemarks());
			inspectionlist.put("walkyTalkyRemarks",vehicleDetails.getWalkyTalkyRemarks());
			inspectionlist.put("gpsRemarks",vehicleDetails.getGpsRemarks());
			inspectionlist.put("driverUniformRemarks",vehicleDetails.getDriverUniformRemarks());
			inspectionlist.put("stephneyRemarks",vehicleDetails.getStephneyRemarks());
			inspectionlist.put("umbrellaRemarks",vehicleDetails.getUmbrellaRemarks());
			inspectionlist.put("torchRemarks",vehicleDetails.getTorchRemarks());
			inspectionlist.put("toolkitRemarks",vehicleDetails.getToolkitRemarks());
			inspectionlist.put("seatUpholtseryCleanNotTornRemarks",vehicleDetails.getSeatUpholtseryCleanNotTornRemarks());
			inspectionlist.put("vehcileRoofUpholtseryCleanNotTornRemarks",vehicleDetails.getVehcileRoofUpholtseryCleanNotTornRemarks());
			inspectionlist.put("vehcileFloorUpholtseryCleanNotTornRemarks",vehicleDetails.getVehcileFloorUpholtseryCleanNotTornRemarks());
			inspectionlist.put("vehcileDashboardCleanRemarks",vehicleDetails.getVehcileDashboardCleanRemarks());
			inspectionlist.put("vehicleGlassCleanStainFreeRemarks",vehicleDetails.getVehicleGlassCleanStainFreeRemarks());
			inspectionlist.put("vehicleInteriorLightBrightWorkingRemarks",vehicleDetails.getVehicleInteriorLightBrightWorkingRemarks());
			inspectionlist.put("bolsterSeperatingLastTwoSeatsRemarks",vehicleDetails.getBolsterSeperatingLastTwoSeatsRemarks());
			inspectionlist.put("exteriorScratchesRemarks",vehicleDetails.getExteriorScratchesRemarks());
			inspectionlist.put("noPatchWorkRemarks",vehicleDetails.getNoPatchWorkRemarks());
			inspectionlist.put("pedalBrakeWorkingRemarks",vehicleDetails.getPedalBrakeWorkingRemarks());
			inspectionlist.put("handBrakeWorkingRemarks",vehicleDetails.getHandBrakeWorkingRemarks());
			inspectionlist.put("tyresNoDentsTrimWheelRemarks",vehicleDetails.getTyresNoDentsTrimWheelRemarks());
			inspectionlist.put("tyresGoodConditionRemarks",vehicleDetails.getTyresGoodConditionRemarks());
			inspectionlist.put("allTyresAndStephneyInflateRemarks",vehicleDetails.getAllTyresAndStephneyInflateRemarks());
			inspectionlist.put("jackAndToolRemarks",vehicleDetails.getJackAndToolRemarks());
			inspectionlist.put("wiperWorkingRemarks",vehicleDetails.getWiperWorkingRemarks());
			inspectionlist.put("acCoolingIn5minsRemarks",vehicleDetails.getAcCoolingIn5minsRemarks());
			inspectionlist.put("noSmellInACRemarks",vehicleDetails.getNoSmellInACRemarks());
			inspectionlist.put("acVentsCleanRemarks",vehicleDetails.getAcVentsCleanRemarks());
			inspectionlist.put("enginOilChangeIndicatorOnRemarks",vehicleDetails.getEnginOilChangeIndicatorOnRemarks());
			inspectionlist.put("coolantIndicatorOnRemarks",vehicleDetails.getCoolantIndicatorOnRemarks());
			inspectionlist.put("brakeClutchIndicatorOnRemarks",vehicleDetails.getBrakeClutchIndicatorOnRemarks());
			inspectionlist.put("highBeamWorkingRemarks",vehicleDetails.getHighBeamWorkingRemarks());
			inspectionlist.put("lowBeamWorkingRemarks",vehicleDetails.getLowBeamWorkingRemarks());			
			inspectionlist.put("rightFromtIndicatorWorkingRemarks",vehicleDetails.getRightFromtIndicatorWorkingRemarks());
			inspectionlist.put("leftFrontIndicatorWorkingRemarks",vehicleDetails.getLeftFrontIndicatorWorkingRemarks());
			inspectionlist.put("parkingLightsWorkingRemarks",vehicleDetails.getParkingLightsWorkingRemarks());
			inspectionlist.put("ledDayTimeRunningLightWorkingRemarks",vehicleDetails.getLedDayTimeRunningLightWorkingRemarks());
			inspectionlist.put("rightRearIndicatorWorkingRemarks",vehicleDetails.getRightRearIndicatorWorkingRemarks());
			inspectionlist.put("leftRearIndicatorWorkingRemarks",vehicleDetails.getLeftRearIndicatorWorkingRemarks());
			inspectionlist.put("brakeLightsOnRemarks",vehicleDetails.getBrakeLightsOnRemarks());
			inspectionlist.put("reverseLightsOnRemarks",vehicleDetails.getReverseLightsOnRemarks());
			inspectionlist.put("hornWorkingRemarks",vehicleDetails.getHornWorkingRemarks());
			inspectionlist.put("reflectionSignBoardRemarks",vehicleDetails.getReflectionSignBoardRemarks());
			 inspectionlist.put("audioWorkingOrNotRemarks",vehicleDetails.getAudioWorkingOrNotRemarks());
			 
			 
			 inspectionlist.put("fogLightsRemarks",vehicleDetails.getFogLightsRemarks());
			 inspectionlist.put("remarks",vehicleDetails.getFeedbackRemarks());
			 inspectionlist.put("driverCheckInDidOrNotRemarks",vehicleDetails.getDriverCheckInDidOrNotRemarks());

			 
			 

		    inspectionlist.put("status", "success");
			inspectionlist.put("InspDocs", inspectiondocuments);
		    listOfVehicle.add(inspectionlist);
			}
		}
		 log.info("serviceEnd -UserId :" + eFmFmVehicleInspectionPO.getUserId());
		return Response.ok(listOfVehicle, MediaType.APPLICATION_JSON).build();
	}
	

	/**
	 * The addingVendorDetails method implemented. for Adding vendor details.
	 * 
	 * @author Rajan R
	 * 
	 * @since 2015-05-19
	 * 
	 * @return added status.
	 */
	@POST
	@Path("/addingVendorDetails")
	public Response addingVendorDetails(
			EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
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
		iVehicleCheckInBO.save(eFmFmVehicleMasterPO);
		 log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	}

	/**
	 * The modifyVendorDetails method implemented. for Modifying vehicle
	 * details.
	 * 
	 * @author Rajan R
	 * 
	 * @since 2015-05-19
	 * 
	 * @return Modified Status.
	 * @throws ParseException
	 */
	@POST
	@Path("/modifyVehicleDetails")
	public Response modifyVehicleDetails(
			EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		Map<String, Object> requests = new HashMap<String, Object>();	
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
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
			
		String status="Input";
    	StringBuffer temp=new StringBuffer("PleaseCheck");
         if(true){
        	 
        	 if(eFmFmVehicleMasterPO.getVehicleMake()==null||eFmFmVehicleMasterPO.getVehicleMake().isEmpty())
         	{temp.append("::Vehicle make cannot be empty");
   	      status="Fail";        		
         	}
         	else{
     	CharSequence vehicleMake = eFmFmVehicleMasterPO.getVehicleMake();
 		Matcher vMake= Validator.alphaNumSpecialCharacers(vehicleMake);
 		if(!vMake.matches()||eFmFmVehicleMasterPO.getVehicleMake().length()>20)		 
 	     {temp.append("::Vehicle make should not be more than 20 letters");
 	      status="Fail";
 	     }}
        if(eFmFmVehicleMasterPO.getVehicleModel()==null||eFmFmVehicleMasterPO.getVehicleModel().isEmpty())
         	{temp.append("::Vehicle model cannot be empty");
       	      status="Fail";
         	}
         	else{	
         		CharSequence vehicleModel = eFmFmVehicleMasterPO.getVehicleModel();
 		Matcher vModel= Validator.alphaNumSpecialCharacers(vehicleModel);
 		if(!vModel.matches()||eFmFmVehicleMasterPO.getVehicleModel().length()>20)		 
 	     {temp.append("::Vehicle model cannot be more than 30 letters");
 	      status="Fail";
 	     }}
         	
         	
         	
 		if(eFmFmVehicleMasterPO.getVehicleModelYear()==null||eFmFmVehicleMasterPO.getVehicleModelYear().equals(""))
 		{temp.append("::Vehicle model year cannot be empty");
 	      status="Fail";			
 		}
 		else{
 		CharSequence modelYear = eFmFmVehicleMasterPO.getVehicleModelYear();
 		Matcher vModelYear= Validator.dateYear(modelYear);
 		if(!vModelYear.matches())		 
 	     {temp.append("::Vehicle model year should be in year format");
 	      status="Fail";
 	     }
 		}
 		
		
 		if(eFmFmVehicleMasterPO.getVehicleNumber()==null||eFmFmVehicleMasterPO.getVehicleNumber().isEmpty())
		{temp.append("::Vehicle  number cannot be empty");
	      status="Fail";			
		}
		else{
		CharSequence vehicleNum = eFmFmVehicleMasterPO.getVehicleNumber();
		Matcher vehicleNumber= Validator.alphaNumeric(vehicleNum);
		if(!vehicleNumber.matches()||eFmFmVehicleMasterPO.getVehicleNumber().length()>20)		 
	     {temp.append("::Vehicle number can be alpha numberic and not more than 20 letters");
	      status="Fail";
	     }}
		
		
		
		if(eFmFmVehicleMasterPO.getVehicleEngineNumber()==null||eFmFmVehicleMasterPO.getVehicleEngineNumber().isEmpty())
		{temp.append("::Vehicle Engine number cannot be empty");
	      status="Fail";			
		}
		else{
		CharSequence vehicleEngNumber = eFmFmVehicleMasterPO.getVehicleEngineNumber();
		Matcher vehicleEngNo= Validator.alphaNumeric(vehicleEngNumber);
		if(!vehicleEngNo.matches()||eFmFmVehicleMasterPO.getVehicleEngineNumber().length()>15)		 
	     {temp.append("::Vehicle Engine number can be alpha numberic and not more than 15 letters");
	      status="Fail";
	     }}
		

		if(eFmFmVehicleMasterPO.getCapacity()<=0||eFmFmVehicleMasterPO.getCapacity()>99)
		{temp.append("::Vehicle capacity is mandatory and should be positive number, max range 99");
	      status="Fail";			
		}
		
		
		if(eFmFmVehicleMasterPO.getRegistartionCertificateNumber()==null||eFmFmVehicleMasterPO.getRegistartionCertificateNumber().equals(""))
		{temp.append("::Vehicle RegistrationCertificate number cannot be empty");
	      status="Fail";			
		}
		else{
		CharSequence VehRegistrationNo = eFmFmVehicleMasterPO.getRegistartionCertificateNumber();
		Matcher vRegNum= Validator.alphaNumSpecialCharacers(VehRegistrationNo);
		if(!vRegNum.matches()||eFmFmVehicleMasterPO.getRegistartionCertificateNumber().length()>30)		 
	     {temp.append("::Vehicle Registration Certification number can be alphanumeric and not more than 30 letters");
	      status="Fail";
	     }
		}
		
		if(eFmFmVehicleMasterPO.getReplaceMentVehicleNum()==null||eFmFmVehicleMasterPO.getReplaceMentVehicleNum().equals(""))
		{temp.append("::Vehicle Replacement field number cannot be empty");
	      status="Fail";
		}
		else{
		CharSequence vehReplacement = eFmFmVehicleMasterPO.getReplaceMentVehicleNum();
		Matcher vReplaceNum= Validator.alphaNumSpecialCharacers(vehReplacement);
		if(!vReplaceNum.matches())
		{temp.append("::Vehicle Replacement can be alphanumeric");
	      status="Fail";				
		}	
         }
		
		/*if(eFmFmVehicleMasterPO.getMileage()<=0)
		{temp.append("::Vehicle Mileage field should be positive");
	      status="Fail";			
		}*/
		
		if(eFmFmVehicleMasterPO.getVehicleMaintenaneDate()==null||eFmFmVehicleMasterPO.getVehicleMaintenaneDate().isEmpty())
		{temp.append("::Vehicle MaintenaneDate cannot be empty");
		 status="Fail";
		}	
			
			
		if(eFmFmVehicleMasterPO.getRegistrationValid()==null)
		{temp.append("::Vehicle Registration Date cannot be empty");
		status="Fail";			
		}	
		
		if(eFmFmVehicleMasterPO.getPolutionDate()==null)
		{temp.append("::Vehicle Polution Expiry date Date cannot be empty");
		status="Fail";			
		}
		
		if(eFmFmVehicleMasterPO.getInsuranceValid()==null)
		{temp.append("::Vehicle Insurance Expiry date Date cannot be empty");
		status="Fail";			
		}
		if(eFmFmVehicleMasterPO.getTaxValidDate()==null)
		{temp.append("::Vehicle Tax Expiry  Date cannot be empty");
		status="Fail";			
		}
		
		if(eFmFmVehicleMasterPO.getPermitValid()==null)
		{temp.append("Vehicle permit Date cannot be empty");
		status="Fail";			
		}
		
		/*if(eFmFmVehicleMasterPO.getVehicleFitnessDate()==null)
		{temp.append("Vehicle Fitness Date cannot be empty");
		status="Fail";			
		}
		*/
		if(status.equals("Fail"))
		{
		log.info("Invalid input:");
		
		requests.put("inputInvalid", temp);
	    return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	   	}
         }
      log.info("valid input:");
         
		
		
		
		
		EFmFmVehicleMasterPO vehicleDetails = iVehicleCheckInBO.getParticularVehicleDetail(eFmFmVehicleMasterPO.getVehicleId());
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");		
		Date permitValidDate = formatter.parse(eFmFmVehicleMasterPO.getPermitValid());
		Date insuranceValidDate = formatter.parse(eFmFmVehicleMasterPO.getInsuranceValid());
		Date registrationValidDate = formatter.parse(eFmFmVehicleMasterPO.getRegistrationValid());
		Date maintenanceValidDate = formatter.parse(eFmFmVehicleMasterPO	.getMaintenanceValid());
		Date taxValidDate = formatter.parse(eFmFmVehicleMasterPO.getTaxValidDate());
		Date polutionValidDate = formatter.parse(eFmFmVehicleMasterPO.getPolutionDate());		
		Date vehicleMaintenance = formatter.parse(eFmFmVehicleMasterPO.getVehicleMaintenaneDate());
		vehicleDetails.setPermitValidDate(permitValidDate);
		vehicleDetails.setInsuranceValidDate(insuranceValidDate);
		vehicleDetails.setRegistrationDate(registrationValidDate);
		vehicleDetails.setVehicleFitnessDate(maintenanceValidDate);
		vehicleDetails.setTaxCertificateValid(taxValidDate);
		vehicleDetails.setPolutionValid(polutionValidDate);		
		vehicleDetails.setVehicleMaintenanceDate(vehicleMaintenance);
		vehicleDetails.setMileage(eFmFmVehicleMasterPO.getMileage());
		vehicleDetails.setCapacity(eFmFmVehicleMasterPO.getCapacity());
		vehicleDetails.setVehicleOwnerName(eFmFmVehicleMasterPO.getVehicleOwnerName());
		vehicleDetails.setVehicleNumber(eFmFmVehicleMasterPO.getVehicleNumber());
		vehicleDetails.setVehicleModel(eFmFmVehicleMasterPO.getVehicleModel());
		vehicleDetails.setVehicleMake(eFmFmVehicleMasterPO.getVehicleMake());
		vehicleDetails.setVehicleEngineNumber(eFmFmVehicleMasterPO.getVehicleEngineNumber());
		vehicleDetails.setReplaceMentVehicleNum(eFmFmVehicleMasterPO.getReplaceMentVehicleNum());
		vehicleDetails.setRegistartionCertificateNumber(eFmFmVehicleMasterPO.getRegistartionCertificateNumber());
		vehicleDetails.setCapacity(eFmFmVehicleMasterPO.getCapacity());
		EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO = new EFmFmFixedDistanceContractDetailPO();
		eFmFmFixedDistanceContractDetailPO.setDistanceContractId(eFmFmVehicleMasterPO.getContractDetailId());
		vehicleDetails.seteFmFmContractDetails(eFmFmFixedDistanceContractDetailPO);		
		iVehicleCheckInBO.update(vehicleDetails);
		log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	}

	/**
	 * The removeVehicleDetails method implemented. for Modifying vehicle
	 * details.
	 * 
	 * @author Rajan R
	 * 
	 * @since 2015-05-19
	 * 
	 * @return deleted Status.
	 */
	@POST
	@Path("/removeVehicleDetails")
	public Response removeVehicleDetails(
			EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
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
		EFmFmVehicleMasterPO vehicleDetails = iVehicleCheckInBO
				.getParticularVehicleDetail(eFmFmVehicleMasterPO.getVehicleId());
		vehicleDetails.setStatus("D");
		iVehicleCheckInBO.update(vehicleDetails);
		 log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	}

	// edit entity button click......all driver,vehicle and device according to
	// the vendor.

	@POST
	@Path("/listOfActiveEntity")
	public Response checkInVehicleAndDevice(
			EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		// IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO)
		// ContextLoader.getContext().getBean("IVendorDetailsBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
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
		Map<String, Object> vehicleCheckInList = new HashMap<String, Object>();
		List<EFmFmDeviceMasterPO> allDevice = iVehicleCheckInBO
				.getAllActiveDeviceDetails(eFmFmVendorMasterPO
						.geteFmFmClientBranchPO().getBranchId());
		List<EFmFmVehicleMasterPO> allVehicleByVendor = iVehicleCheckInBO
				.getAllActiveVehicleDetails(eFmFmVendorMasterPO.getVendorId());
		List<EFmFmDriverMasterPO> allDriverByVendor = iVehicleCheckInBO
				.getAllActiveDriverDetails(eFmFmVendorMasterPO.getVendorId());
		List<Map<String, Object>> checkInList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> driverCheckInList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> deviceCheckInList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> escortCheckInList = new ArrayList<Map<String, Object>>();
		if (!(allVehicleByVendor.isEmpty())) {
			for (EFmFmVehicleMasterPO vehicleDetails : allVehicleByVendor) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("vehicleId", vehicleDetails.getVehicleId());
				vehicleList.put("vehicleNumber",
						vehicleDetails.getVehicleNumber());
				vehicleList.put("vendorName", vehicleDetails
						.getEfmFmVendorMaster().getVendorName());
				vehicleList.put("vendorId", vehicleDetails
						.getEfmFmVendorMaster().getVendorId());
				checkInList.add(vehicleList);
			}
		}
		vehicleCheckInList.put("vehicleDetails", checkInList);
		if (!(allDriverByVendor.isEmpty())) {
			for (EFmFmDriverMasterPO driverDetails : allDriverByVendor) {
				Map<String, Object> driverList = new HashMap<String, Object>();
				driverList.put("driverId", driverDetails.getDriverId());
				driverList.put("driverName", driverDetails.getFirstName());
				driverList.put("mobileNumber", driverDetails.getMobileNumber());
				driverList.put("DriverName", driverDetails.getFirstName());
				driverList.put("MobileNumber", driverDetails.getMobileNumber());

				driverList.put("vendorName", driverDetails
						.getEfmFmVendorMaster().getVendorName());
				driverList.put("vendorId", driverDetails.getEfmFmVendorMaster()
						.getVendorId());
				driverCheckInList.add(driverList);
			}
		}
		vehicleCheckInList.put("driverDetails", driverCheckInList);
		if (!(allDevice.isEmpty())) {
			for (EFmFmDeviceMasterPO deviceDetails : allDevice) {
				Map<String, Object> deviceList = new HashMap<String, Object>();
				deviceList.put("deviceId", deviceDetails.getDeviceId());
				deviceList.put("mobileNumber", deviceDetails.getMobileNumber());
				deviceCheckInList.add(deviceList);
			}
		}
		vehicleCheckInList.put("deviceDetails", deviceCheckInList);
		List<EFmFmEscortCheckInPO> escortList = iVehicleCheckInBO
				.getAllCheckedInEscort(eFmFmVendorMasterPO.getCombinedFacility());
		if (!(escortList.isEmpty())) {
			for (EFmFmEscortCheckInPO escorts : escortList) {
				Map<String, Object> escortsDetails = new HashMap<String, Object>();
				escortsDetails.put("escortCheckInId",
						escorts.getEscortCheckInId());
				escortsDetails.put("escortName", escorts.geteFmFmEscortMaster()
						.getFirstName());
				escortCheckInList.add(escortsDetails);
			}
		}
		vehicleCheckInList.put("escortDetails", escortCheckInList);
		 log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(vehicleCheckInList, MediaType.APPLICATION_JSON)
				.build();
	}

	// get all available vehicles,driver and Devices for Changing the entites in
	@POST
	@Path("/listOfActiveDriver")
	public Response listOfActiveDriver(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
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
		Map<String, Object> vehicleCheckInList = new HashMap<String, Object>();
		// List<EFmFmDriverMasterPO> allDriverByVendor
		// =iVehicleCheckInBO.randomDriverDetails(eFmFmVendorMasterPO.getVendorId(),new
		// Date());
		List<EFmFmDriverMasterPO> allDriverByVendor = iVehicleCheckInBO
				.getAllActiveDriverDetails(eFmFmVendorMasterPO.getVendorId());

		List<Map<String, Object>> checkInList = new ArrayList<Map<String, Object>>();
		if (!(allDriverByVendor.isEmpty())) {
			for (EFmFmDriverMasterPO driverDetails : allDriverByVendor) {
				Map<String, Object> driverList = new HashMap<String, Object>();
				driverList.put("driverId", driverDetails.getDriverId());
				driverList.put("driverName", driverDetails.getFirstName());
				driverList.put("mobileNumber", driverDetails.getMobileNumber());
				driverList.put("vendorId", driverDetails.getEfmFmVendorMaster()
						.getVendorId());
				checkInList.add(driverList);
			}
		}
		vehicleCheckInList.put("driverDetails", checkInList);
		 log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(vehicleCheckInList, MediaType.APPLICATION_JSON)
				.build();
	}

	@POST
	@Path("/listOfActiveVehicle")
	public Response listOfActiveVehicle(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
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
		Map<String, Object> vehicleCheckInList = new HashMap<String, Object>();
		// List<EFmFmVehicleMasterPO>
		// allVehicleByVendor=iVehicleCheckInBO.getAvailableVehicleDetails(eFmFmVendorMasterPO.getVendorId(),new
		// Date());
		List<EFmFmVehicleMasterPO> allVehicleByVendor = iVehicleCheckInBO
				.getAllActiveVehicleDetails(eFmFmVendorMasterPO.getVendorId());

		List<Map<String, Object>> checkInList = new ArrayList<Map<String, Object>>();
		if (!(allVehicleByVendor.isEmpty())) {
			for (EFmFmVehicleMasterPO vehicleDetails : allVehicleByVendor) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("vehicleId", vehicleDetails.getVehicleId());
				vehicleList.put("vehicleNumber",
						vehicleDetails.getVehicleNumber());
				vehicleList.put("vendorName", vehicleDetails
						.getEfmFmVendorMaster().getVendorName());
				vehicleList.put("vendorId", vehicleDetails
						.getEfmFmVendorMaster().getVendorId());
				checkInList.add(vehicleList);
			}
		}
		vehicleCheckInList.put("vehicleDetails", checkInList);
		 log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(vehicleCheckInList, MediaType.APPLICATION_JSON)
				.build();
	}

	@POST
	@Path("/listOfContractType")
	public Response listOfContractType(EFmFmClientBranchPO eFmFmClientBranchPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
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
		Map<String, Object> listOfContractType = new HashMap<String, Object>();
		List<EFmFmVendorContractTypeMasterPO> allContractType = iVehicleCheckInBO
				.getAllContractType(eFmFmClientBranchPO.getBranchId());
		List<Map<String, Object>> checkInList = new ArrayList<Map<String, Object>>();
		if (!(allContractType.isEmpty())) {
			for (EFmFmVendorContractTypeMasterPO contractTypeList : allContractType) {
				Map<String, Object> contractList = new HashMap<String, Object>();
				contractList.put("contractTypeID",
						contractTypeList.getContractTypeId());
				contractList.put("contractType",
						contractTypeList.getContractType());
				checkInList.add(contractList);
			}
			listOfContractType.put("vehicleDetails", checkInList);
		}
		 log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(listOfContractType, MediaType.APPLICATION_JSON)
				.build();
	}

	/**
	 * 
	 * 
	 * @param with
	 *            a particular vehicle and device from Web
	 * @return return Success
	 */

	@POST
	@Path("/devicecheckin")
	public Response driverCheckInFromDevice(
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleCheckInPO.getUserId());
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleCheckInPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleCheckInPO.getUserId());
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
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext()
				.getBean("IApprovalBO");
	
		
		List<EFmFmDriverMasterPO> particularDriverDetails = approvalBO
				.getParticularDriverDetailFromDeriverId(eFmFmVehicleCheckInPO
						.getDriverId());
		if (!(particularDriverDetails.isEmpty())){
			if (particularDriverDetails.get(0).getStatus()
					.equalsIgnoreCase("P")) {
				responce.put("status", "pending");
				 log.info("serviceEnd -UserId :" + eFmFmVehicleCheckInPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON)
						.build();
			}
		} else {
			responce.put("status", "fail");
			 log.info("serviceEnd -UserId :" + eFmFmVehicleCheckInPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		eFmFmVehicleCheckInPO.setCheckInTime(new Date());
		List<EFmFmVehicleCheckInPO> vehicleCheckInPO = vehicleCheckInBO
				.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
		if (vehicleCheckInPO.get(0).getStatus().equalsIgnoreCase("N")) {
			responce.put("status", "fail");
			 log.info("serviceEnd -UserId :" + eFmFmVehicleCheckInPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		vehicleCheckInPO.get(vehicleCheckInPO.size() - 1).setStatus("N");
		vehicleCheckInPO.get(vehicleCheckInPO.size() - 1).setCheckOutTime(
				new Date());
		vehicleCheckInBO
				.update(vehicleCheckInPO.get(vehicleCheckInPO.size() - 1));
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + eFmFmVehicleCheckInPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * 
	 * @param driverMaster
	 *            checkout the particular driver with a particular vehicle and
	 *            device from Web
	 * @return return Success
	 */

	@POST
	@Path("/driverCheckOut")
	public Response driverCheckOutFromDevice(
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleCheckInPO.getUserId());
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleCheckInPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
//				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleCheckInPO.getUserId());
//				   if (!(userDetail.isEmpty())) {
//					String jwtToken = "";
//					try {
//					 JwtTokenGenerator token = new JwtTokenGenerator();
//					 jwtToken = token.generateToken();
//					 userDetail.get(0).setAuthorizationToken(jwtToken);
//					 userDetail.get(0).setTokenGenerationTime(new Date());
//					 userMasterBO.update(userDetail.get(0));
//					} catch (Exception e) {
//					 log.info("error" + e);
//					}
//			   }
//			}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		log.info("CheckOutService");	
		try{
			List<EFmFmAssignRoutePO> todayRoutesCheck=assignRouteBO.getDuplicateTripAllocationCheck(eFmFmVehicleCheckInPO.getCheckInId(), eFmFmVehicleCheckInPO.getCombinedFacility());
			if(!(todayRoutesCheck.isEmpty())){
			
				//Vehicle already up and running so can't do a check out.
				responce.put("status", "duplicat");
				 log.info("serviceEnd -UserId :" + eFmFmVehicleCheckInPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
			}
			}catch(Exception e){
				log.info("allready allocated" + e);
			}
		
		eFmFmVehicleCheckInPO.setCheckInTime(new Date());
		List<EFmFmVehicleCheckInPO> vehicleCheckInPO = vehicleCheckInBO
				.getCheckedInVehicleDetailsFromChecInAndBranchId(eFmFmVehicleCheckInPO.getCheckInId(), eFmFmVehicleCheckInPO.getBranchId());
	
		vehicleCheckInPO.get(vehicleCheckInPO.size() - 1).setStatus("N");
        vehicleCheckInPO.get(vehicleCheckInPO.size() - 1).setCheckOutTime(
				new Date());
        vehicleCheckInPO.get(vehicleCheckInPO.size() - 1).setCheckOutRemarks(eFmFmVehicleCheckInPO.getCheckOutRemarks());
		vehicleCheckInBO
				.update(vehicleCheckInPO.get(vehicleCheckInPO.size() - 1));
		responce.put("status", "success");
		 log.info("serviceEnd -UserId :" + eFmFmVehicleCheckInPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * 
	 * @param driverMaster
	 *            checkout the particular driver with a particular vehicle and
	 *            device from Web
	 * @return return Success
	 */

	@POST
	@Path("/actualvehiclelist")
	public Response getAllActualVehicles(
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		IVehicleCheckInBO vehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmVehicleCheckInPO.getUserId());
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleCheckInPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
				List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmVehicleCheckInPO.getUserId());
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
		List<EFmFmVehicleMasterPO> allVehicleDetail = vehicleCheckInBO
				.getAllApprovedVehiclesByVendorId(eFmFmVehicleCheckInPO.getVendorId(), eFmFmVehicleCheckInPO.getBranchId());
		List<Map<String, Object>> vehicleList = new ArrayList<Map<String, Object>>();
		if (!(allVehicleDetail.isEmpty())) {
			for (EFmFmVehicleMasterPO vehicleDetail : allVehicleDetail) {
				if (!(vehicleDetail.getVehicleNumber().contains("DUMMY"))) {
					Map<String, Object> vehicle = new HashMap<String, Object>();
					vehicle.put("vehicleNum", vehicleDetail.getVehicleNumber());
					vehicle.put("vehicleId", vehicleDetail.getVehicleId());
					vehicleList.add(vehicle);
				}
			}
		}
		 log.info("serviceEnd -UserId :" + eFmFmVehicleCheckInPO.getUserId());
		return Response.ok(vehicleList, MediaType.APPLICATION_JSON).build();
	}
	
	
	
	@POST
	@Path("/getDynamicCheckList")
	public Response getDynamicCheckList(EFmFmDynamicVehicleCheckListPO eFmFmDynamicVehicleCheckListPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
	/*	log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken")))){
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}}catch(Exception e){
				log.info("authenticationToken error"+e);
			}
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		*/
	
		List<EFmFmDynamicVehicleCheckListPO> allCheckList = iVehicleCheckInBO
				.getAllcheckListDetails(eFmFmDynamicVehicleCheckListPO.geteFmFmClientBranchPO().getBranchId(), 
						eFmFmDynamicVehicleCheckListPO.getCheckListType());

		List<Map<String, Object>> checkInList = new ArrayList<Map<String, Object>>();
		if (!(allCheckList.isEmpty())) {
			for (EFmFmDynamicVehicleCheckListPO checkListDetails : allCheckList) {
				Map<String, Object> checkList = new HashMap<String, Object>();
				checkList.put("checkListId", checkListDetails.getCheckListId());
				checkList.put("checkListLable", checkListDetails.getCheckListName());
				checkList.put("checkListColumn", checkListDetails.getCheckListCoulum());				
				checkInList.add(checkList);
			}
		}else{
			responce.put("failed","NORECORD");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		 log.info("serviceEnd -UserId :" + eFmFmDynamicVehicleCheckListPO.getUserId());
		return Response.ok(checkInList, MediaType.APPLICATION_JSON).build();
	}
	
	/*
	 * Getting the past trip details for vehicle.
	 */
		@POST
		@Path("/driverTripHistory")
		public Response listOfActiveDriver(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
			IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext().getBean("IVehicleCheckInBO");
			IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
			Map<String, Object> responce = new HashMap<String, Object>();		
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date fromDate = null;
		    Date toDate = null;			
			if(eFmFmVehicleMasterPO.getActionType().equalsIgnoreCase("date")){				
				fromDate = formatter.parse(eFmFmVehicleMasterPO.getFromDate());
				toDate = formatter.parse(eFmFmVehicleMasterPO.getToDate());
			}else{	
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				toDate = cal.getTime();
				cal.add(Calendar.DATE,-(eFmFmVehicleMasterPO.getTripHistoryCount()-1));				
				fromDate = cal.getTime();			
				log.info("fromDate"+formatter.format(fromDate) +":toDate"+formatter.format(toDate));											
			}					
		/*	log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
			try{
				if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken")))){
					responce.put("status", "invalidRequest");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}}catch(Exception e){
					log.info("authenticationToken error"+e);
				}	*/		
		
			List<EFmFmAssignRoutePO> allTripDetails = iVehicleCheckInBO.getPastTripDetailsforDriver
					(fromDate, toDate, eFmFmVehicleMasterPO.getBranchId(),eFmFmVehicleMasterPO.getDriverId());

			List<Map<String, Object>> allTirpList = new ArrayList<Map<String, Object>>();
			if (!(allTripDetails.isEmpty())) {
				for (EFmFmAssignRoutePO driverDetails : allTripDetails) {
					Map<String, Object> tripList = new HashMap<String, Object>();
					tripList.put("tripId", driverDetails.getAssignRouteId());
					tripList.put("tripAssignDate", formatter.format(driverDetails.getTripAssignDate()));
					tripList.put("shifTime", driverDetails.getShiftTime());
					tripList.put("TripType", driverDetails.getTripType());
					tripList.put("VehicleNumber", driverDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());					
					allTirpList.add(tripList);
				}
			}
			 
			 log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
			return Response.ok(allTirpList, MediaType.APPLICATION_JSON).build();
		}
	
	

}