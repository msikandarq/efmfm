package com.newtglobal.eFmFmFleet.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IEmployeeDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IFieldAppDetailsBO;
import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.MessagingService;
import com.newtglobal.eFmFmFleet.eFmFmFleet.PasswordEncryption;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFieldAppConfigMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFixedDistanceContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmSupervisorMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserPasswordPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmZoneMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/fieldApp")
@Consumes("application/json")
@Produces("application/json")
public class FieldAppService {

	private static final String SERVER_UPLOAD_LINUX_LOCATION_FOLDER = ContextLoader.getContext()
			.getMessage("upload.docsLinux", null, "docsLinux", null);
	private static final String SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER = ContextLoader.getContext()
			.getMessage("upload.docsWindows", null, "docsWindows", null);
	String result = "";
	private static Log log = LogFactory.getLog(FieldAppService.class);
	DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	/*
	 * fieldAppRegistration Service for Supervisor,Escort,employee.
	 */

	@POST
	@Path("/fieldAppRegistration")
	public Response fieldAppRegistration(final EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO)
			throws UnsupportedEncodingException {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
//		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmSupervisorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		
		log.info("device token" + eFmFmSupervisorMasterPO.getDeviceToken());
		log.info("device id" + eFmFmSupervisorMasterPO.getImeiNumber());
		log.info("device Type" + eFmFmSupervisorMasterPO.getDeviceType());
		
		List<EFmFmClientBranchPO> clientMasterDetail = employeeDetailBO
				.doesClientCodeExist(eFmFmSupervisorMasterPO.getBranchCode());
		eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(clientMasterDetail.get(0).getBranchId()));
		if (!(clientMasterDetail.isEmpty())) {
			List<EFmFmSupervisorMasterPO> supervisorDetail = iFieldAppDetailsBO.getSupervisorMobileNumberStatus(
					eFmFmSupervisorMasterPO.getMobileNumber(), new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
			List<EFmFmEscortMasterPO> eFmFmEscortMasterPO = iVehicleCheckInBO.getEscostMobileNoDetails(
					eFmFmSupervisorMasterPO.getMobileNumber(), new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
			List<EFmFmUserMasterPO> userMasterDetail = employeeDetailBO.getEmpDetailsFromMobileNumberAndBranchId(
					Base64.getEncoder().encodeToString(eFmFmSupervisorMasterPO.getMobileNumber().getBytes("utf-8")),
					new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
			ArrayList<Integer> statusStored = new ArrayList<Integer>();
			int dupCounts=0;
			if (!supervisorDetail.isEmpty() || !eFmFmEscortMasterPO.isEmpty() || !userMasterDetail.isEmpty()) {
				
				statusStored.add(supervisorDetail.size());
				statusStored.add(eFmFmEscortMasterPO.size());
				statusStored.add(userMasterDetail.size());				
				
				for (int val:statusStored){
					if(val >0){
						dupCounts++;
					}
				}
				
				if(dupCounts>1){ 
						  responce.put("status", "DUPMOBNO");
						  log.info("dupCounts :" + dupCounts);
						  return Response.ok(responce, MediaType.APPLICATION_JSON).build(); 
				}
				
				
				if (!supervisorDetail.isEmpty()) {
					if (supervisorDetail.size() == 1) {
						if (supervisorDetail.get(0).getIsActive().equalsIgnoreCase("P")) {
							responce.put("status", "PENDING");
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
						responce = supervisorLoginRegistration(eFmFmSupervisorMasterPO, supervisorDetail);
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					} else {
						responce.put("status", "DUPMOBNO");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
				}
				if (!eFmFmEscortMasterPO.isEmpty()) {
					if (eFmFmEscortMasterPO.size() == 1) {
						if (eFmFmEscortMasterPO.get(0).getIsActive().equalsIgnoreCase("N")) {
							responce.put("status", "PENDING");
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
						responce = escortLoginRegistration(eFmFmSupervisorMasterPO, eFmFmEscortMasterPO);
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					} else {
						responce.put("status", "DUPMOBNO");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
				}
				if (!userMasterDetail.isEmpty()) {
					if (userMasterDetail.size() == 1) {
						if (userMasterDetail.get(0).getStatus().equalsIgnoreCase("N")) {
							responce.put("status", "PENDING");
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						} else if (new String(
								Base64.getDecoder().decode(userMasterDetail.get(0).getEmployeeDepartment()), "utf-8")
										.equalsIgnoreCase("transportteam")) {
							responce = transportLoginRegistration(eFmFmSupervisorMasterPO, userMasterDetail);
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						} else {
							responce.put("status", "TRANUSERONLYALLOWED");
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}

					} else {
						responce.put("status", "DUPMOBNO");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
				}
			}
			responce.put("status", "NOTEXIST");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		responce.put("status", "CODEERROR");
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	private Map<String, Object> transportLoginRegistration(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO,
			List<EFmFmUserMasterPO> userMasterDetail) {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		Map<String, Object> response = new HashMap<String, Object>();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		for (int i = 0; i < 4; i++) {
			result += numbers.get(i).toString();
		}
		if (!(userMasterDetail.isEmpty())) {
			if (userMasterDetail.get(0).getStatus().equalsIgnoreCase("N")) {
				response.put("status", "DISABLE");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return response;
			}
			try {
				if (userMasterDetail.get(0).getDeviceId().equalsIgnoreCase(eFmFmSupervisorMasterPO.getImeiNumber())) {
					response.put("branchId", userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchId());
					response.put("status", "ALREADY");
					log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
					return response;
				}
			} catch (Exception e) {
				log.info("register exception" + e);
				response.put("status", "FAILED");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return response;
			}
			try {
				if (!(userMasterDetail.get(0).getDeviceId().equalsIgnoreCase("NO"))) {
					userMasterDetail.get(0).setLoggedIn(false);
					userMasterDetail.get(0).setDeviceToken(eFmFmSupervisorMasterPO.getDeviceToken());
					userMasterDetail.get(0).setDeviceType(eFmFmSupervisorMasterPO.getDeviceType());
					userMasterDetail.get(0).setDeviceId(eFmFmSupervisorMasterPO.getImeiNumber());
					employeeDetailBO.update(userMasterDetail.get(0));
					response.put("branchId", userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchId());
					response.put("status", "ALREADY");
					log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
					return response;
				}
			} catch (Exception e) {
				log.info("Exception Inside the after registration changing device");
				response.put("status", "failed");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return response;
			}
			userMasterDetail.get(0).setLoggedIn(false);
			userMasterDetail.get(0).setDeviceToken(eFmFmSupervisorMasterPO.getDeviceToken());
			userMasterDetail.get(0).setDeviceType(eFmFmSupervisorMasterPO.getDeviceType());
			userMasterDetail.get(0).setDeviceId(eFmFmSupervisorMasterPO.getImeiNumber());
			try {
				Thread thread1 = new Thread(new Runnable() {
					@Override
					public void run() {

						MessagingService messaging = new MessagingService();
						String text = "Dear user,Please find login credentials for eFmFm application.\nUsername: "
								+ new String(Base64.getDecoder().decode(userMasterDetail.get(0).getMobileNumber()))
								+ "\nPassword: " + result + "\nFor feedback write to us @"
								+ userMasterDetail.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();
						try {
							messaging
									.cabHasLeftMessageForSch(
											new String(Base64.getDecoder()
													.decode(userMasterDetail.get(0).getMobileNumber()), "utf-8"),
											text, "no");
							log.info("Register Message Sent" + new String(
									Base64.getDecoder().decode(userMasterDetail.get(0).getMobileNumber())));

						} catch (Exception e) {
							log.info("Register Message Sent Exception" + e);

						}
					}
				});
				thread1.start();
			} catch (Exception e) {
				log.info("Register Password mail" + e);
			}
			PasswordEncryption passwordEncryption = new PasswordEncryption();
			userMasterDetail.get(0).setPassword(passwordEncryption.PasswordEncoderGenerator(result));
			userMasterDetail.get(0).setPasswordChangeDate(new Date());
			userMasterDetail.get(0).setLastLoginTime(new Date());
			userMasterDetail.get(0).setTempPassWordChange(true);
			employeeDetailBO.update(userMasterDetail.get(0));
			response.put("branchId", userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchId());
			response.put("status", "SUCCESS");
			log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
			return response;
		} else {
			response.put("status", "FAILED");
			log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
			return response;
		}

	}

	private Map<String, Object> escortLoginRegistration(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO,
			List<EFmFmEscortMasterPO> eFmFmEscortMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		for (int i = 0; i < 4; i++) {
			result += numbers.get(i).toString();
		}
		if (!(eFmFmEscortMasterPO.isEmpty())) {
			if (eFmFmEscortMasterPO.get(0).getIsActive().equalsIgnoreCase("N")) {
				responce.put("status", "DISABLE");
				return responce;
			}
			try {
				if (eFmFmSupervisorMasterPO.getImeiNumber() != null) {
					try {
						if (eFmFmEscortMasterPO.get(0).getImeiNumber().equalsIgnoreCase(eFmFmSupervisorMasterPO.getImeiNumber())) {
							responce.put("status", "ALREADY");
							responce.put("branchId", eFmFmEscortMasterPO.get(0).getEfmFmVendorMaster()
									.geteFmFmClientBranchPO().getBranchId());
							return responce;
						}
					} catch (Exception e) {
						log.info("register exception Device Details are not updated");
					}
					
				}
			} catch (Exception e) {
				log.info("register exception" + e);
				e.printStackTrace();
				responce.put("status", "FAILED");
				return responce;
			}
			try {
				if (!(eFmFmEscortMasterPO.get(0).getImeiNumber().equalsIgnoreCase("NO"))) {
					eFmFmEscortMasterPO.get(0).setLoggedIn(false);
					eFmFmEscortMasterPO.get(0).setDeviceToken(eFmFmSupervisorMasterPO.getDeviceToken());
					eFmFmEscortMasterPO.get(0).setDeviceType(eFmFmSupervisorMasterPO.getDeviceType());
					eFmFmEscortMasterPO.get(0).setImeiNumber(eFmFmSupervisorMasterPO.getImeiNumber());
					iVehicleCheckInBO.updateEscortDetails(eFmFmEscortMasterPO.get(0));
					responce.put("status", "ALREADY");
					responce.put("branchId",
							eFmFmEscortMasterPO.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
					return responce;
				}
			} catch (Exception e) {
				log.info("Exception Inside the after registration changing device");
				responce.put("status", "FAILED");
				return responce;
			}
			eFmFmEscortMasterPO.get(0).setLoggedIn(false);
			eFmFmEscortMasterPO.get(0).setDeviceToken(eFmFmSupervisorMasterPO.getDeviceToken());
			eFmFmEscortMasterPO.get(0).setDeviceType(eFmFmSupervisorMasterPO.getDeviceType());
			eFmFmEscortMasterPO.get(0).setImeiNumber(eFmFmSupervisorMasterPO.getImeiNumber());
			try {
				Thread thread1 = new Thread(new Runnable() {
					public void run() {

						MessagingService messaging = new MessagingService();
						String text = "Dear user,Please find login credentials for eFmFm application.\nUsername: "
								+ eFmFmEscortMasterPO.get(0).getMobileNumber() + "\nPassword: " + result
								+ "\nFor feedback write to us @" + eFmFmEscortMasterPO.get(0).getEfmFmVendorMaster()
										.geteFmFmClientBranchPO().getFeedBackEmailId();
						try {
							messaging.cabHasLeftMessageForSch(eFmFmEscortMasterPO.get(0).getMobileNumber(), text, "no");
							log.info("Register Message Sent" + eFmFmEscortMasterPO.get(0).getMobileNumber());

						} catch (Exception e) {
							log.info("Register Message Sent Exception" + e);
						}
					}
				});
				thread1.start();
			} catch (Exception e) {
				log.info("Register Password mail" + e);
			}
			PasswordEncryption passwordEncryption = new PasswordEncryption();
			eFmFmEscortMasterPO.get(0).setPassword(passwordEncryption.PasswordEncoderGenerator(result));
			eFmFmEscortMasterPO.get(0).setPasswordChangeDate(new Date());
			eFmFmEscortMasterPO.get(0).setLastLoginTime(new Date());
			eFmFmEscortMasterPO.get(0).setTempPassWordChange(true);
			iVehicleCheckInBO.updateEscortDetails(eFmFmEscortMasterPO.get(0));
			responce.put("status", "SUCCESS");
			responce.put("branchId",
					eFmFmEscortMasterPO.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
			return responce;
		} else {
			responce.put("status", "FAILED");
			return responce;
		}

	}

	public Map<String, Object> supervisorLoginRegistration(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO,
			List<EFmFmSupervisorMasterPO> supervisorDetail) throws UnsupportedEncodingException {
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		for (int i = 0; i < 4; i++) {
			result += numbers.get(i).toString();
		}
		if (!(supervisorDetail.isEmpty())) {
			if (supervisorDetail.get(0).getIsActive().equalsIgnoreCase("N")) {
				responce.put("status", "DISABLE");
				return responce;
			}else if (supervisorDetail.get(0).getIsActive().equalsIgnoreCase("R")) {
				responce.put("status", "REJECT");
				return responce;
			}
			try {
				if (eFmFmSupervisorMasterPO.getImeiNumber() != null) {
					if (supervisorDetail.get(0).getImeiNumber()
							.equalsIgnoreCase(eFmFmSupervisorMasterPO.getImeiNumber())) {
						responce.put("status", "ALREADY");
						responce.put("branchId",
								supervisorDetail.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
						return responce;
					}
				}
			} catch (Exception e) {
				log.info("register exception" + e);
				e.printStackTrace();
				responce.put("status", "FAILED");
				return responce;
			}
			try {
				if (!(supervisorDetail.get(0).getImeiNumber().equalsIgnoreCase("NO"))) {
					supervisorDetail.get(0).setLoggedIn(false);
					supervisorDetail.get(0).setDeviceToken(eFmFmSupervisorMasterPO.getDeviceToken());
					supervisorDetail.get(0).setDeviceType(eFmFmSupervisorMasterPO.getDeviceType());
					supervisorDetail.get(0).setImeiNumber(eFmFmSupervisorMasterPO.getImeiNumber());
					iFieldAppDetailsBO.update(supervisorDetail.get(0));
					responce.put("status", "ALREADY");
					responce.put("branchId",
							supervisorDetail.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
					return responce;
				}
			} catch (Exception e) {
				log.info("Exception Inside the after registration changing device");
				responce.put("status", "FAILED");
				return responce;
			}
			supervisorDetail.get(0).setLoggedIn(false);
			supervisorDetail.get(0).setDeviceToken(eFmFmSupervisorMasterPO.getDeviceToken());
			supervisorDetail.get(0).setDeviceType(eFmFmSupervisorMasterPO.getDeviceType());
			supervisorDetail.get(0).setImeiNumber(eFmFmSupervisorMasterPO.getImeiNumber());
			try {
				Thread thread1 = new Thread(new Runnable() {
					public void run() {

						MessagingService messaging = new MessagingService();
						String text = "Dear user,Please find login credentials for eFmFm application.\nUsername: "
								+ supervisorDetail.get(0).getMobileNumber() + "\nPassword: " + result
								+ "\nFor feedback write to us @" + supervisorDetail.get(0).getEfmFmVendorMaster()
										.geteFmFmClientBranchPO().getFeedBackEmailId();
						try {
							messaging.cabHasLeftMessageForSch(supervisorDetail.get(0).getMobileNumber(), text, "no");
							log.info("Register Message Sent" + supervisorDetail.get(0).getMobileNumber());

						} catch (Exception e) {
							log.info("Register Message Sent Exception" + e);
						}
					}
				});
				thread1.start();
			} catch (Exception e) {
				log.info("Register Password mail" + e);
			}
			PasswordEncryption passwordEncryption = new PasswordEncryption();
			supervisorDetail.get(0).setPassword(passwordEncryption.PasswordEncoderGenerator(result));
			supervisorDetail.get(0).setPasswordChangeDate(new Date());
			supervisorDetail.get(0).setLastLoginTime(new Date());
			supervisorDetail.get(0).setTempPassWordChange(true);
			iFieldAppDetailsBO.update(supervisorDetail.get(0));
			responce.put("status", "SUCCESS");
			responce.put("branchId",
					supervisorDetail.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId());
			return responce;
		} else {
			responce.put("status", "FAILED");
			return responce;
		}

	}

	@POST
	@Path("/fieldAppLogin")
	public Response fieldAppLogin(final EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) throws IOException {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
//		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmSupervisorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		
//		log.info("device token" + eFmFmSupervisorMasterPO.getDeviceToken());
//		log.info("device id" + eFmFmSupervisorMasterPO.getImeiNumber());
//		log.info("device Type" + eFmFmSupervisorMasterPO.getDeviceType());
		
		eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
		List<EFmFmSupervisorMasterPO> supervisorDetail = iFieldAppDetailsBO.getSupervisorMobileNumberDetails(
				eFmFmSupervisorMasterPO.getMobileNumber(), new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
		List<EFmFmEscortMasterPO> eFmFmEscortMasterPO = iVehicleCheckInBO.getEscostMobileNoDetails(
				eFmFmSupervisorMasterPO.getMobileNumber(), new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
		List<EFmFmUserMasterPO> userMasterDetail = employeeDetailBO.getEmpDetailsFromMobileNumberAndBranchId(
				Base64.getEncoder().encodeToString(eFmFmSupervisorMasterPO.getMobileNumber().getBytes("utf-8")),
				new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
		ArrayList<Integer> statusStored = new ArrayList<Integer>();
		int dupCounts=0;
		if (!supervisorDetail.isEmpty() || !eFmFmEscortMasterPO.isEmpty() || !userMasterDetail.isEmpty()) {
			statusStored.add(supervisorDetail.size());
			statusStored.add(eFmFmEscortMasterPO.size());
			statusStored.add(userMasterDetail.size());
				for (int val:statusStored){
					if(val >0){
						dupCounts++;
					}
				}				
			if(dupCounts>1){ 
					  responce.put("status", "DUPMOBNO"); 
					  log.info("dupCounts :" + dupCounts);
					  return Response.ok(responce, MediaType.APPLICATION_JSON).build(); 
			}
			 
			if (!supervisorDetail.isEmpty()) {
				if (supervisorDetail.size() == 1) {
					if (supervisorDetail.get(0).getIsActive().equalsIgnoreCase("P")) {
						responce.put("status", "PENDING");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
					try {
						if (supervisorDetail.get(0).getImeiNumber().equalsIgnoreCase("NO")){
							responce.put("status", "NOTREGISTER");
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}else{					
							responce = supervisorLogin(eFmFmSupervisorMasterPO, supervisorDetail);
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
					} catch (Exception e) {
						responce.put("status", "NOTREGISTER");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
					
				} else {
					responce.put("status", "DUPMOBNO");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
			}

			if (!eFmFmEscortMasterPO.isEmpty()) {
				if (eFmFmEscortMasterPO.size() == 1) {
					if (eFmFmEscortMasterPO.get(0).getIsActive().equalsIgnoreCase("N")) {
						responce.put("status", "PENDING");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
					try {
						if (eFmFmEscortMasterPO.get(0).getImeiNumber().equalsIgnoreCase("NO")) {
							responce.put("status", "NOTREGISTER");
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}else{
							responce = escortLogin(eFmFmSupervisorMasterPO, eFmFmEscortMasterPO);
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
					} catch (Exception e) {
						responce.put("status", "NOTREGISTER");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
										
					
				} else {
					responce.put("status", "DUPMOBNO");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
			}

			if (!userMasterDetail.isEmpty()) {
				if (userMasterDetail.size() == 1) {
					if (userMasterDetail.get(0).getStatus().equalsIgnoreCase("N")) {
						responce.put("status", "PENDING");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					} else if (new String(Base64.getDecoder().decode(userMasterDetail.get(0).getEmployeeDepartment()),
							"utf-8").equalsIgnoreCase("transportteam")) {
						
						try {
							if (userMasterDetail.get(0).getDeviceId().equalsIgnoreCase("NO")) {
								responce.put("status", "NOTREGISTER");
								return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}else{			
								responce = employeeLogin(eFmFmSupervisorMasterPO, userMasterDetail);
								return Response.ok(responce, MediaType.APPLICATION_JSON).build();
							}
						} catch (Exception e) {
							responce.put("status", "NOTREGISTER");
							return Response.ok(responce, MediaType.APPLICATION_JSON).build();
						}
						
						
					} else {
						responce.put("status", "TRANUSERONLYALLOWED");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
				} else {
					responce.put("status", "DUPMOBNO");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}

			}

		}
		responce.put("status", "NOTEXIST");
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	public Map<String, Object> escortLogin(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO,
			List<EFmFmEscortMasterPO> userMasterDetail) throws IOException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		IApprovalBO iApprovalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		log.info("userDetail" + userMasterDetail.size());
		Map<String, Object> responce = new HashMap<String, Object>();
		if (!(userMasterDetail.isEmpty())) {
			if (!(encoder.matches(eFmFmSupervisorMasterPO.getPassword(),userMasterDetail.get(0).getPassword().trim()))) {
				if (!(userMasterDetail.isEmpty())
						&& (userMasterDetail.get(0).getWrongPassAttempt() == userMasterDetail.get(0)
								.getEfmFmVendorMaster().geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass())
						&& (getDisableTime(24, 0, userMasterDetail.get(0).getWrongPassAttemptDate()) > new Date()
								.getTime())) {
					responce.put("status", "passDisable");
					return responce;
				} else if (!(userMasterDetail.isEmpty())
						&& (userMasterDetail.get(0).getWrongPassAttempt() == userMasterDetail.get(0)
								.getEfmFmVendorMaster().geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass())
						&& (getDisableTime(24, 0, userMasterDetail.get(0).getWrongPassAttemptDate()) < new Date()
								.getTime())) {
					userMasterDetail.get(0).setWrongPassAttempt(1);
					iVehicleCheckInBO.updateEscortDetails(userMasterDetail.get(0));
					responce.put("status", "fail");
					return responce;
				} else if (!(userMasterDetail.isEmpty())
						&& (userMasterDetail.get(0).getWrongPassAttempt() == (userMasterDetail.get(0)
								.getEfmFmVendorMaster().geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass() - 1))) {
					userMasterDetail.get(0).setWrongPassAttempt(userMasterDetail.get(0).getWrongPassAttempt() + 1);
					userMasterDetail.get(0).setWrongPassAttemptDate(new Date());
					iVehicleCheckInBO.updateEscortDetails(userMasterDetail.get(0));
					responce.put("status", "passDisable");
					return responce;
				} else if (!(userMasterDetail.isEmpty())
						&& (userMasterDetail.get(0).getWrongPassAttempt() == (userMasterDetail.get(0)
								.getEfmFmVendorMaster().geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass() - 2))) {
					userMasterDetail.get(0).setWrongPassAttempt(userMasterDetail.get(0).getWrongPassAttempt() + 1);
					iVehicleCheckInBO.updateEscortDetails(userMasterDetail.get(0));
					responce.put("status", "lastAttempt");
					return responce;
				} else {
					userMasterDetail.get(0).setWrongPassAttempt(userMasterDetail.get(0).getWrongPassAttempt() + 1);
					iVehicleCheckInBO.updateEscortDetails(userMasterDetail.get(0));
					responce.put("status", "fail");
					return responce;
				}
			}

			// List<EFmFmClientUserRolePO> userDetail =
			// userMasterBO.getAdminUserRoleByUserName(userMasterDetail.get(0).getUserName());

			if (!(userMasterDetail.isEmpty())
					&& (userMasterDetail.get(0).getWrongPassAttempt() == userMasterDetail.get(0).getEfmFmVendorMaster()
							.geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass())
					&& (getDisableTime(24, 0, userMasterDetail.get(0).getWrongPassAttemptDate()) > new Date()
							.getTime())) {
				responce.put("status", "passDisable");
				return responce;
			}
			/*
			 * else if (!(userMasterDetail.isEmpty()) &&
			 * (userMasterDetail.get(0).isTempPassWordChange())) {
			 * responce.put("status", "tempPassChange"); return responce; } else
			 * if ((!userDetail.isEmpty()) &&
			 * (passwordChangeDate(userDetail.get(0).getEfmFmUserMaster().
			 * getPasswordChangeDate(),
			 * userDetail.get(0).geteFmFmClientBranchPO().
			 * getPasswordResetPeriodForAdminInDays()) < new Date() .getTime())
			 * &&
			 * userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase
			 * ("admin")) { responce.put("status", "passReset");
			 * 
			 * return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			 * } else if ((!userDetail.isEmpty()) &&
			 * (passwordChangeDate(userDetail.get(0).getEfmFmUserMaster().
			 * getPasswordChangeDate(),
			 * userDetail.get(0).geteFmFmClientBranchPO().
			 * getPasswordResetPeriodForUserInDays()) < new Date() .getTime())
			 * && !(userDetail.get(0).getEfmFmRoleMaster().getRole().
			 * equalsIgnoreCase("admin"))) { responce.put("status",
			 * "passReset");
			 * 
			 * return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			 * } else if
			 * (passwordChangeDate(userMasterDetail.get(0).getLastLoginTime(),
			 * userMasterDetail.get(0).getEfmFmVendorMaster().
			 * geteFmFmClientBranchPO().getInactiveAdminAccountAfterNumOfDays())
			 * < new Date().getTime() &&
			 * userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase
			 * ("admin")){ responce.put("status", "inactive");
			 * 
			 * return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			 * }
			 */

			if (userMasterDetail.get(0).getIsActive().equalsIgnoreCase("N")) {
				responce.put("status", "disable");

				return responce;
			}
			List<Map<String, Object>> userAccessData = new ArrayList<Map<String, Object>>();
			responce.put("appLoginUserType", "escort");
			responce.put("name", userMasterDetail.get(0).getFirstName());
			List<EFmFmEscortDocsPO> eFmFmEscortDocsPO = iFieldAppDetailsBO
					.getEscortuploadFileDetails(userMasterDetail.get(0).getEscortId(), "profilePath");
			if (!(eFmFmEscortDocsPO.isEmpty())) {
				try {
					String profilePicPath = "";
					if (eFmFmEscortDocsPO.get(0).getUploadpath() != null
							&& eFmFmEscortDocsPO.get(0).getUploadpath() != "") {
						profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
								null);
					} else {
						profilePicPath = ContextLoader.getContext().getMessage("default.profilePic", null, "profilePic",
								null);
					}
					responce.put("profilePic", profilePicPath + eFmFmEscortDocsPO.get(0).getUploadpath()
							.substring(eFmFmEscortDocsPO.get(0).getUploadpath().indexOf("upload") - 1));
				} catch (Exception e) {
					String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
							"profilePic", null);
					responce.put("profilePic", defaultProfilePic);
				}
			} else {
				String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
						"profilePic", null);
				responce.put("profilePic", defaultProfilePic);
			}
			responce.put("mobileNo", userMasterDetail.get(0).getMobileNumber());
			responce.put("userId", userMasterDetail.get(0).getEscortId());
			responce.put("vendorId", userMasterDetail.get(0).getEfmFmVendorMaster().getVendorId());
			responce.put("vendorName", userMasterDetail.get(0).getEfmFmVendorMaster().getVendorName());
			/*
			 * responce.put("vehicleTotalCount",iVehicleCheckInBO.
			 * getAllNonRemovedVehicleCountByBranchIdAndVendorId(
			 * userMasterDetail.get(0).getEfmFmVendorMaster().getVendorId(),
			 * eFmFmSupervisorMasterPO.getBranchId()));
			 * responce.put("driverTotalCount",iVehicleCheckInBO.
			 * getAllNonRemovedDriverCountByBranchIdAndVendorId(userMasterDetail
			 * .get(0).getEfmFmVendorMaster().getVendorId(),
			 * eFmFmSupervisorMasterPO.getBranchId()));
			 */
			/*
			 * responce.put("locationStatus",
			 * userMasterDetail.get(0).getLocationStatus());
			 * responce.put("panicStatus",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getPanicAlertNeeded()); responce.put("branchId",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchId());
			 * responce.put("branchCode",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchCode())
			 * ; responce.put("adhocTimePicker",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getAdhocTimePickerForEmployee());
			 * responce.put("dropPriorTimePeriod",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getDropPriorTimePeriod()); responce.put("pickupPriorTimePeriod",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getPickupPriorTimePeriod()); responce.put("driverName",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getEmpDeviceDriverName()); responce.put("driverMobile",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getEmpDeviceDriverMobileNumber());
			 * responce.put("driverProfilePic",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getDriverDeviceDriverProfilePicture());
			 * responce.put("employeeId",
			 * userMasterDetail.get(0).getEmployeeId());
			 * responce.put("tempPassChange",
			 * userMasterDetail.get(0).isTempPassWordChange());
			 * responce.put("cutOffTimeFlg",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().getCutOffTime())
			 * ; //Tk8= means check for NO if
			 * (userMasterDetail.get(0).getPanicNumber().equalsIgnoreCase("Tk8="
			 * )) { responce.put("empPanicNumber", new
			 * String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getPanicNumber()))); } else { responce.put("empPanicNumber", "+"
			 * + new String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getPanicNumber()))); } responce.put("emergencyNum", "+" +
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getEmergencyContactNumber()); responce.put("emailId", new
			 * String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getEmailId()), "utf-8")); responce.put("designation", new
			 * String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getEmployeeDesignation()))); responce.put("mobileNumber", new
			 * String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getMobileNumber()))); try { String profilePicPath =
			 * ContextLoader.getContext().getMessage("change.profilePic", null,
			 * "profilePic", null); responce.put("profilePic", profilePicPath +
			 * userMasterDetail.get(0).getEmployeeProfilePic()
			 * .substring(userMasterDetail.get(0).getEmployeeProfilePic().
			 * indexOf("upload") - 1)); } catch (Exception e) { String
			 * defaultProfilePic =
			 * ContextLoader.getContext().getMessage("default.profilePic", null,
			 * "profilePic", null); responce.put("profilePic",
			 * defaultProfilePic); }
			 */
			responce.put("status", "success");
			userMasterDetail.get(0).setLastLoginTime(new Date());
			userMasterDetail.get(0).setWrongPassAttempt(0);
			userMasterDetail.get(0).setLoggedIn(true);
			// userMasterDetail.get(0).setTempPassWordChange(true);
			iVehicleCheckInBO.updateEscortDetails(userMasterDetail.get(0));
			List<Map<String, Object>> listOfConfigDetails = new ArrayList<Map<String, Object>>();
			eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
			
			List<EFmFmFieldAppConfigMasterPO> listOfConfigType = iFieldAppDetailsBO.getAllValuesUsedByConfigType(
					new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()), eFmFmSupervisorMasterPO.getConfigType());
			if (!(listOfConfigType.isEmpty())) {
				for (EFmFmFieldAppConfigMasterPO configDetails : listOfConfigType) {
					Map<String, Object> listOfConfig = new HashMap<String, Object>();
					listOfConfig.put("configId", configDetails.getConfigId());
					listOfConfig.put("isActive", configDetails.getIsActive());
					listOfConfig.put("configValues", configDetails.getConfigDescription());
					listOfConfigDetails.add(listOfConfig);
				}
			}
			responce.put("moduleData", listOfConfigDetails);
			return responce;

		} else {
			responce.put("status", "fail");
		}

		return responce;
	}

	public Map<String, Object> supervisorLogin(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO,
			List<EFmFmSupervisorMasterPO> userMasterDetail) throws IOException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
//		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		log.info("userDetail" + userMasterDetail.size());
		Map<String, Object> responce = new HashMap<String, Object>();
		if (!(userMasterDetail.isEmpty())) {
			if (!(encoder.matches(eFmFmSupervisorMasterPO.getPassword(),
					userMasterDetail.get(0).getPassword().trim()))) {
				if (!(userMasterDetail.isEmpty())
						&& (userMasterDetail.get(0).getWrongPassAttempt() == userMasterDetail.get(0)
								.getEfmFmVendorMaster().geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass())
						&& (getDisableTime(24, 0, userMasterDetail.get(0).getWrongPassAttemptDate()) > new Date()
								.getTime())) {
					responce.put("status", "passDisable");
					return responce;
				} else if (!(userMasterDetail.isEmpty())
						&& (userMasterDetail.get(0).getWrongPassAttempt() == userMasterDetail.get(0)
								.getEfmFmVendorMaster().geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass())
						&& (getDisableTime(24, 0, userMasterDetail.get(0).getWrongPassAttemptDate()) < new Date()
								.getTime())) {
					userMasterDetail.get(0).setWrongPassAttempt(1);
					iFieldAppDetailsBO.update(userMasterDetail.get(0));
					responce.put("status", "fail");
					return responce;
				} else if (!(userMasterDetail.isEmpty())
						&& (userMasterDetail.get(0).getWrongPassAttempt() == (userMasterDetail.get(0)
								.getEfmFmVendorMaster().geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass() - 1))) {
					userMasterDetail.get(0).setWrongPassAttempt(userMasterDetail.get(0).getWrongPassAttempt() + 1);
					userMasterDetail.get(0).setWrongPassAttemptDate(new Date());
					iFieldAppDetailsBO.update(userMasterDetail.get(0));
					responce.put("status", "passDisable");
					return responce;
				} else if (!(userMasterDetail.isEmpty())
						&& (userMasterDetail.get(0).getWrongPassAttempt() == (userMasterDetail.get(0)
								.getEfmFmVendorMaster().geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass() - 2))) {
					userMasterDetail.get(0).setWrongPassAttempt(userMasterDetail.get(0).getWrongPassAttempt() + 1);
					iFieldAppDetailsBO.update(userMasterDetail.get(0));
					responce.put("status", "lastAttempt");
					return responce;
				} else {
					userMasterDetail.get(0).setWrongPassAttempt(userMasterDetail.get(0).getWrongPassAttempt() + 1);
					iFieldAppDetailsBO.update(userMasterDetail.get(0));
					responce.put("status", "fail");
					return responce;
				}
			}

			// List<EFmFmClientUserRolePO> userDetail =
			// userMasterBO.getAdminUserRoleByUserName(userMasterDetail.get(0).getUserName());

			if (!(userMasterDetail.isEmpty())
					&& (userMasterDetail.get(0).getWrongPassAttempt() == userMasterDetail.get(0).getEfmFmVendorMaster()
							.geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass())
					&& (getDisableTime(24, 0, userMasterDetail.get(0).getWrongPassAttemptDate()) > new Date()
							.getTime())) {
				responce.put("status", "passDisable");
				return responce;
			}
			/*
			 * else if (!(userMasterDetail.isEmpty()) &&
			 * (userMasterDetail.get(0).isTempPassWordChange())) {
			 * responce.put("status", "tempPassChange"); return responce; } else
			 * if ((!userDetail.isEmpty()) &&
			 * (passwordChangeDate(userDetail.get(0).getEfmFmUserMaster().
			 * getPasswordChangeDate(),
			 * userDetail.get(0).geteFmFmClientBranchPO().
			 * getPasswordResetPeriodForAdminInDays()) < new Date() .getTime())
			 * &&
			 * userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase
			 * ("admin")) { responce.put("status", "passReset");
			 * 
			 * return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			 * } else if ((!userDetail.isEmpty()) &&
			 * (passwordChangeDate(userDetail.get(0).getEfmFmUserMaster().
			 * getPasswordChangeDate(),
			 * userDetail.get(0).geteFmFmClientBranchPO().
			 * getPasswordResetPeriodForUserInDays()) < new Date() .getTime())
			 * && !(userDetail.get(0).getEfmFmRoleMaster().getRole().
			 * equalsIgnoreCase("admin"))) { responce.put("status",
			 * "passReset");
			 * 
			 * return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			 * } else if
			 * (passwordChangeDate(userMasterDetail.get(0).getLastLoginTime(),
			 * userMasterDetail.get(0).getEfmFmVendorMaster().
			 * geteFmFmClientBranchPO().getInactiveAdminAccountAfterNumOfDays())
			 * < new Date().getTime() &&
			 * userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase
			 * ("admin")){ responce.put("status", "inactive");
			 * 
			 * return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			 * }
			 */

			if (userMasterDetail.get(0).getIsActive().equalsIgnoreCase("N")) {
				responce.put("status", "disable");
				return responce;
			}
			List<Map<String, Object>> userAccessData = new ArrayList<Map<String, Object>>();
			responce.put("appLoginUserType", "supervisor");
			responce.put("name", userMasterDetail.get(0).getFirstName());
			responce.put("mobileNo", userMasterDetail.get(0).getMobileNumber());
			responce.put("userId", userMasterDetail.get(0).getSupervisorId());
			try {
				String profilePicPath = "";
				if (userMasterDetail.get(0).getProfilePicPath() != null
						&& userMasterDetail.get(0).getProfilePicPath() != "") {
					profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
							null);
				} else {
					profilePicPath = ContextLoader.getContext().getMessage("default.profilePic", null, "profilePic",
							null);
				}
				responce.put("profilePic", profilePicPath + userMasterDetail.get(0).getProfilePicPath()
						.substring(userMasterDetail.get(0).getProfilePicPath().indexOf("upload") - 1));
			} catch (Exception e) {
				String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
						"profilePic", null);
				responce.put("profilePic", defaultProfilePic);
			}

			responce.put("vendorId", userMasterDetail.get(0).getEfmFmVendorMaster().getVendorId());
			responce.put("vendorName", userMasterDetail.get(0).getEfmFmVendorMaster().getVendorName());
			eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
			responce.put("vehicleTotalCount",
					iVehicleCheckInBO.getAllNonRemovedVehicleCountByBranchIdAndVendorId(
							userMasterDetail.get(0).getEfmFmVendorMaster().getVendorId(),
							new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility())));
			responce.put("driverTotalCount",
					iVehicleCheckInBO.getAllNonRemovedDriverCountByBranchIdAndVendorId(
							userMasterDetail.get(0).getEfmFmVendorMaster().getVendorId(),
							new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility())));
			/*
			 * responce.put("locationStatus",
			 * userMasterDetail.get(0).getLocationStatus());
			 * responce.put("panicStatus",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getPanicAlertNeeded()); responce.put("branchId",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchId());
			 * responce.put("branchCode",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchCode())
			 * ; responce.put("adhocTimePicker",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getAdhocTimePickerForEmployee());
			 * responce.put("dropPriorTimePeriod",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getDropPriorTimePeriod()); responce.put("pickupPriorTimePeriod",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getPickupPriorTimePeriod()); responce.put("driverName",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getEmpDeviceDriverName()); responce.put("driverMobile",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getEmpDeviceDriverMobileNumber());
			 * responce.put("driverProfilePic",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getDriverDeviceDriverProfilePicture());
			 * responce.put("employeeId",
			 * userMasterDetail.get(0).getEmployeeId());
			 * responce.put("tempPassChange",
			 * userMasterDetail.get(0).isTempPassWordChange());
			 * responce.put("cutOffTimeFlg",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().getCutOffTime())
			 * ; //Tk8= means check for NO if
			 * (userMasterDetail.get(0).getPanicNumber().equalsIgnoreCase("Tk8="
			 * )) { responce.put("empPanicNumber", new
			 * String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getPanicNumber()))); } else { responce.put("empPanicNumber", "+"
			 * + new String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getPanicNumber()))); } responce.put("emergencyNum", "+" +
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getEmergencyContactNumber()); responce.put("emailId", new
			 * String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getEmailId()), "utf-8")); responce.put("designation", new
			 * String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getEmployeeDesignation()))); responce.put("mobileNumber", new
			 * String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getMobileNumber()))); try { String profilePicPath =
			 * ContextLoader.getContext().getMessage("change.profilePic", null,
			 * "profilePic", null); responce.put("profilePic", profilePicPath +
			 * userMasterDetail.get(0).getEmployeeProfilePic()
			 * .substring(userMasterDetail.get(0).getEmployeeProfilePic().
			 * indexOf("upload") - 1)); } catch (Exception e) { String
			 * defaultProfilePic =
			 * ContextLoader.getContext().getMessage("default.profilePic", null,
			 * "profilePic", null); responce.put("profilePic",
			 * defaultProfilePic); }
			 */
			responce.put("status", "success");
			userMasterDetail.get(0).setLastLoginTime(new Date());
			userMasterDetail.get(0).setWrongPassAttempt(0);
			userMasterDetail.get(0).setLoggedIn(true);
			// userMasterDetail.get(0).setTempPassWordChange(true);
			iFieldAppDetailsBO.update(userMasterDetail.get(0));
			List<Map<String, Object>> listOfConfigDetails = new ArrayList<Map<String, Object>>();
			eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
			List<EFmFmFieldAppConfigMasterPO> listOfConfigType = iFieldAppDetailsBO.getAllValuesUsedByConfigType(
					new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()), eFmFmSupervisorMasterPO.getConfigType());
			if (!(listOfConfigType.isEmpty())) {
				for (EFmFmFieldAppConfigMasterPO configDetails : listOfConfigType) {
					Map<String, Object> listOfConfig = new HashMap<String, Object>();
					listOfConfig.put("configId", configDetails.getConfigId());
					listOfConfig.put("isActive", configDetails.getIsActive());
					listOfConfig.put("configValues", configDetails.getConfigDescription());
					listOfConfigDetails.add(listOfConfig);
				}
			}
			responce.put("moduleData", listOfConfigDetails);
			return responce;

		} else {
			responce.put("status", "fail");
		}

		return responce;
	}

	public Map<String, Object> employeeLogin(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO,
			List<EFmFmUserMasterPO> userMasterDetail) throws IOException {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		log.info("userDetail" + userMasterDetail.size());
		Map<String, Object> responce = new HashMap<String, Object>();
		if (!(userMasterDetail.isEmpty())) {
			if (!(encoder.matches(eFmFmSupervisorMasterPO.getPassword(),
					userMasterDetail.get(0).getPassword().trim()))) {
				if (!(userMasterDetail.isEmpty())
						&& (userMasterDetail.get(0).getWrongPassAttempt() == userMasterDetail.get(0)
								.geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass())
						&& (getDisableTime(24, 0, userMasterDetail.get(0).getWrongPassAttemptDate()) > new Date()
								.getTime())) {
					responce.put("status", "passDisable");
					return responce;
				} else if (!(userMasterDetail.isEmpty())
						&& (userMasterDetail.get(0).getWrongPassAttempt() == userMasterDetail.get(0)
								.geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass())
						&& (getDisableTime(24, 0, userMasterDetail.get(0).getWrongPassAttemptDate()) < new Date()
								.getTime())) {
					userMasterDetail.get(0).setWrongPassAttempt(1);
					employeeDetailBO.update(userMasterDetail.get(0));
					responce.put("status", "fail");
					return responce;
				} else if (!(userMasterDetail.isEmpty())
						&& (userMasterDetail.get(0).getWrongPassAttempt() == (userMasterDetail.get(0)
								.geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass() - 1))) {
					userMasterDetail.get(0).setWrongPassAttempt(userMasterDetail.get(0).getWrongPassAttempt() + 1);
					userMasterDetail.get(0).setWrongPassAttemptDate(new Date());
					employeeDetailBO.update(userMasterDetail.get(0));
					responce.put("status", "passDisable");
					return responce;
				} else if (!(userMasterDetail.isEmpty())
						&& (userMasterDetail.get(0).getWrongPassAttempt() == (userMasterDetail.get(0)
								.geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass() - 2))) {
					userMasterDetail.get(0).setWrongPassAttempt(userMasterDetail.get(0).getWrongPassAttempt() + 1);
					employeeDetailBO.update(userMasterDetail.get(0));
					responce.put("status", "lastAttempt");
					return responce;
				} else {
					userMasterDetail.get(0).setWrongPassAttempt(userMasterDetail.get(0).getWrongPassAttempt() + 1);
					employeeDetailBO.update(userMasterDetail.get(0));
					responce.put("status", "fail");
					return responce;
				}
			}

			// List<EFmFmClientUserRolePO> userDetail =
			// userMasterBO.getAdminUserRoleByUserName(userMasterDetail.get(0).getUserName());

			if (!(userMasterDetail.isEmpty())
					&& (userMasterDetail.get(0).getWrongPassAttempt() == userMasterDetail.get(0)
							.geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass())
					&& (getDisableTime(24, 0, userMasterDetail.get(0).getWrongPassAttemptDate()) > new Date()
							.getTime())) {
				responce.put("status", "passDisable");
				return responce;
			}
			/*
			 * else if (!(userMasterDetail.isEmpty()) &&
			 * (userMasterDetail.get(0).isTempPassWordChange())) {
			 * responce.put("status", "tempPassChange"); return responce; }
			 * 
			 * else if ((!userDetail.isEmpty()) &&
			 * (passwordChangeDate(userDetail.get(0).getEfmFmUserMaster().
			 * getPasswordChangeDate(),
			 * userDetail.get(0).geteFmFmClientBranchPO().
			 * getPasswordResetPeriodForAdminInDays()) < new Date() .getTime())
			 * &&
			 * userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase
			 * ("admin")) { responce.put("status", "passReset");
			 * 
			 * return responce; } else if ((!userDetail.isEmpty()) &&
			 * (passwordChangeDate(userDetail.get(0).getEfmFmUserMaster().
			 * getPasswordChangeDate(),
			 * userDetail.get(0).geteFmFmClientBranchPO().
			 * getPasswordResetPeriodForUserInDays()) < new Date() .getTime())
			 * && !(userDetail.get(0).getEfmFmRoleMaster().getRole().
			 * equalsIgnoreCase("admin"))) { responce.put("status",
			 * "passReset");
			 * 
			 * return responce; } else if
			 * (passwordChangeDate(userMasterDetail.get(0).getLastLoginTime(),
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getInactiveAdminAccountAfterNumOfDays()) < new Date().getTime()
			 * &&
			 * userDetail.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase
			 * ("admin")){ responce.put("status", "inactive"); return responce;
			 * }
			 */

			if (userMasterDetail.get(0).getStatus().equalsIgnoreCase("N")) {
				responce.put("status", "disable");
				return responce;
			}
			List<Map<String, Object>> userAccessData = new ArrayList<Map<String, Object>>();
			responce.put("appLoginUserType", "transport");
			responce.put("name",
					new String(Base64.getDecoder().decode(userMasterDetail.get(0).getFirstName()), "utf-8"));
			responce.put("mobileNo",
					new String(Base64.getDecoder().decode(userMasterDetail.get(0).getMobileNumber()), "utf-8"));
			responce.put("userId", userMasterDetail.get(0).getUserId());
			/*
			 * responce.put("vendorId",userMasterDetail.get(0).
			 * getEfmFmVendorMaster().getVendorId()); responce.put("vendorName",
			 * userMasterDetail.get(0).getEfmFmVendorMaster().getVendorName());
			 */
			// responce.put("vehicleTotalCount",iVehicleCheckInBO.getAllNonRemovedVehicleCountByBranchIdAndVendorId(userMasterDetail.get(0).getEfmFmVendorMaster().getVendorId(),
			// eFmFmSupervisorMasterPO.getBranchId()));
			// responce.put("driverTotalCount",iVehicleCheckInBO.getAllNonRemovedDriverCountByBranchIdAndVendorId(userMasterDetail.get(0).getEfmFmVendorMaster().getVendorId(),
			// eFmFmSupervisorMasterPO.getBranchId()));
			/*
			 * responce.put("locationStatus",
			 * userMasterDetail.get(0).getLocationStatus());
			 * responce.put("panicStatus",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getPanicAlertNeeded()); responce.put("branchId",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchId());
			 * responce.put("branchCode",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().getBranchCode())
			 * ; responce.put("adhocTimePicker",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getAdhocTimePickerForEmployee());
			 * responce.put("dropPriorTimePeriod",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getDropPriorTimePeriod()); responce.put("pickupPriorTimePeriod",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getPickupPriorTimePeriod()); responce.put("driverName",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getEmpDeviceDriverName()); responce.put("driverMobile",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getEmpDeviceDriverMobileNumber());
			 * responce.put("driverProfilePic",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getDriverDeviceDriverProfilePicture());
			 * responce.put("employeeId",
			 * userMasterDetail.get(0).getEmployeeId());
			 * responce.put("tempPassChange",
			 * userMasterDetail.get(0).isTempPassWordChange());
			 * responce.put("cutOffTimeFlg",
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().getCutOffTime())
			 * ; //Tk8= means check for NO if
			 * (userMasterDetail.get(0).getPanicNumber().equalsIgnoreCase("Tk8="
			 * )) { responce.put("empPanicNumber", new
			 * String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getPanicNumber()))); } else { responce.put("empPanicNumber", "+"
			 * + new String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getPanicNumber()))); } responce.put("emergencyNum", "+" +
			 * userMasterDetail.get(0).geteFmFmClientBranchPO().
			 * getEmergencyContactNumber()); responce.put("emailId", new
			 * String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getEmailId()), "utf-8")); responce.put("designation", new
			 * String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getEmployeeDesignation()))); responce.put("mobileNumber", new
			 * String(Base64.getDecoder().decode(userMasterDetail.get(0).
			 * getMobileNumber())));
			 */

			try {
				String profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
						null);
				responce.put("profilePic", profilePicPath + userMasterDetail.get(0).getEmployeeProfilePic()
						.substring(userMasterDetail.get(0).getEmployeeProfilePic().indexOf("upload") - 1));
			} catch (Exception e) {
				String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
						"profilePic", null);
				responce.put("profilePic", defaultProfilePic);
			}
			responce.put("status", "success");
			userMasterDetail.get(0).setLastLoginTime(new Date());
			userMasterDetail.get(0).setWrongPassAttempt(0);
			userMasterDetail.get(0).setLoggedIn(true);
			userMasterDetail.get(0).setTempPassWordChange(true);
			employeeDetailBO.update(userMasterDetail.get(0));
			eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
			List<Map<String, Object>> listOfConfigDetails = new ArrayList<Map<String, Object>>();
			List<EFmFmFieldAppConfigMasterPO> listOfConfigType = iFieldAppDetailsBO.getAllValuesUsedByConfigType(
					new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()), eFmFmSupervisorMasterPO.getConfigType());
			if (!(listOfConfigType.isEmpty())) {
				for (EFmFmFieldAppConfigMasterPO configDetails : listOfConfigType) {
					Map<String, Object> listOfConfig = new HashMap<String, Object>();
					listOfConfig.put("configId", configDetails.getConfigId());
					listOfConfig.put("isActive", configDetails.getIsActive());
					listOfConfig.put("configValues", configDetails.getConfigDescription());
					listOfConfigDetails.add(listOfConfig);
				}
			}
			responce.put("moduleData", listOfConfigDetails);
			return responce;

		} else {
			responce.put("status", "fail");
		}

		return responce;
	}

	/**
	 * The listOfConfigTypes method implemented. for getting the list of
	 * listOfConfigTypes.
	 *
	 * @author Rajan R
	 * 
	 * @since 2017-01-13
	 * 
	 * @return Supervisor details.
	 */

	@POST
	@Path("/listOfConfigTypes")
	public Response listOfConfigTypes(EFmFmFieldAppConfigMasterPO eFmFmFieldAppConfigMasterPO) {
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmFieldAppConfigMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		List<Map<String, Object>> listOfConfigDetails = new ArrayList<Map<String, Object>>();
		eFmFmFieldAppConfigMasterPO.setCombinedFacility(String.valueOf(eFmFmFieldAppConfigMasterPO.geteFmFmClientBranchPO().getBranchId()));
		
		List<EFmFmFieldAppConfigMasterPO> listOfConfigType = iFieldAppDetailsBO.getAllValuesUsedByConfigType(
				new MultifacilityService().combinedBranchIdDetails(eFmFmFieldAppConfigMasterPO.getUserId(),
						eFmFmFieldAppConfigMasterPO.getCombinedFacility()),
				eFmFmFieldAppConfigMasterPO.getConfigType());
		if (!(listOfConfigType.isEmpty())) {
			for (EFmFmFieldAppConfigMasterPO configDetails : listOfConfigType) {
				Map<String, Object> listOfConfig = new HashMap<String, Object>();
				listOfConfig.put("configId", configDetails.getConfigId());
				// listOfConfig.put("configType",
				// configDetails.getConfigType());
				listOfConfig.put("configValues", configDetails.getConfigDescription());
				listOfConfigDetails.add(listOfConfig);
			}
		}
		return Response.ok(listOfConfigDetails, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/activeAndInActiveVehicle")
	public Response activeAndInActiveVehicle(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		
		List<Map<String, Object>> listOfVehicleDetails = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleMasterPO> listOfVehicle = iVehicleCheckInBO.getAllVehiclesWithOutStatus(eFmFmVendorMasterPO.getBranchId(),eFmFmVendorMasterPO.getVendorId());
		if (!(listOfVehicle.isEmpty())) {
			for (EFmFmVehicleMasterPO vehicleListDetails : listOfVehicle) {
				Map<String, Object> listOfVehicles = new HashMap<String, Object>();
				listOfVehicles.put("vehicleId", vehicleListDetails.getVehicleId());
				listOfVehicles.put("vehicleNumber", vehicleListDetails.getVehicleNumber());
				listOfVehicles.put("vehicleModel", vehicleListDetails.getVehicleModel());
				if (vehicleListDetails.getStatus().equalsIgnoreCase(eFmFmVendorMasterPO.getStatus())
						|| (vehicleListDetails.getStatus().equalsIgnoreCase("A") && vehicleListDetails.getStatus().equalsIgnoreCase("allocated"))) {
					listOfVehicleDetails.add(listOfVehicles);
				}
			}
		}
		return Response.ok(listOfVehicleDetails, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/activeAndInActiveDrivers")
	public Response activeAndInActiveDrivers(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		List<Map<String, Object>> listOfDriverDetails = new ArrayList<Map<String, Object>>();
		List<EFmFmDriverMasterPO> listOfDriver = iVehicleCheckInBO.getAllDriversWithOutStatus(
				eFmFmVendorMasterPO.getBranchId(), eFmFmVendorMasterPO.getVendorId());
		if (!(listOfDriver.isEmpty())) {
			for (EFmFmDriverMasterPO driverListDetails : listOfDriver) {
				Map<String, Object> listOfDrivers = new HashMap<String, Object>();
				listOfDrivers.put("driverId", driverListDetails.getDriverId());
				listOfDrivers.put("driverFirstName", driverListDetails.getFirstName());
				listOfDrivers.put("mobileNumber", driverListDetails.getMobileNumber());
				try {
					String profilePicPath = "";
					if (driverListDetails.getProfilePicPath() != null && driverListDetails.getProfilePicPath() != "") {
						profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
								null);
					} else {
						profilePicPath = ContextLoader.getContext().getMessage("default.profilePic", null, "profilePic",
								null);
					}
					listOfDrivers.put("profilePic", profilePicPath + driverListDetails.getProfilePicPath()
							.substring(driverListDetails.getProfilePicPath().indexOf("upload") - 1));
				} catch (Exception e) {
					String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
							"profilePic", null);
					listOfDrivers.put("profilePic", defaultProfilePic);
				}

				if (driverListDetails.getStatus().equalsIgnoreCase(eFmFmVendorMasterPO.getStatus()) 
						|| (eFmFmVendorMasterPO.getStatus().equalsIgnoreCase("A") && driverListDetails.getStatus().equalsIgnoreCase("allocated"))) {
					listOfDriverDetails.add(listOfDrivers);
				}
			}
		}
		return Response.ok(listOfDriverDetails, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/checkedInVehicleByVendor")
	public Response checkedInVehicleByVendor(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		List<Map<String, Object>> listOfVehicleDetails = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleCheckInPO> listOfVehicle = iVehicleCheckInBO
				.getParticularCheckedInDriverDetailsByBranchIdAndVendorId(eFmFmVendorMasterPO.getCombinedFacility(),
						eFmFmVendorMasterPO.getVendorId());
		if (!(listOfVehicle.isEmpty())) {
			for (EFmFmVehicleCheckInPO vehicleListDetails : listOfVehicle) {
				Map<String, Object> listOfVehicles = new HashMap<String, Object>();
				listOfVehicles.put("vehicleId", vehicleListDetails.getEfmFmVehicleMaster().getVehicleId());
				listOfVehicles.put("vehicleNumber", vehicleListDetails.getEfmFmVehicleMaster().getVehicleNumber());
				listOfVehicles.put("vehicleModel", vehicleListDetails.getEfmFmVehicleMaster().getVehicleModel());
				listOfVehicles.put("driverName", vehicleListDetails.getEfmFmDriverMaster().getFirstName());
				listOfVehicles.put("mobileNumber", vehicleListDetails.getEfmFmDriverMaster().getMobileNumber());
				listOfVehicles.put("deviceId", vehicleListDetails.geteFmFmDeviceMaster().getDeviceId());
				listOfVehicles.put("checkInId", vehicleListDetails.getCheckInId());
				listOfVehicleDetails.add(listOfVehicles);

			}
		}
		return Response.ok(listOfVehicleDetails, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * The addSupervisorDetails method implemented. for adding new Supervisor
	 * based on the vendor.
	 *
	 * @author Rajan R
	 * 
	 * @since 2017-01-13
	 * 
	 * @return add Status.
	 * @throws ParseException
	 * @throws UnsupportedEncodingException 
	 */

	@POST
	@Path("/addSupervisorDetails")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addSupervisorDetails(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) throws ParseException, UnsupportedEncodingException {
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IVendorDetailsBO iVendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmSupervisorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		Map<String, Object> request = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
		List<EFmFmVendorMasterPO> venodorExist = iVendorDetailsBO.getAllVendorDetailsByVendorId(
				new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()), eFmFmSupervisorMasterPO.getEfmFmVendorMaster().getVendorId());
		if (!venodorExist.isEmpty()) {
			if (venodorExist.get(0).getStatus().equalsIgnoreCase("P")) {
				request.put("status", "pending");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return Response.ok(request, MediaType.APPLICATION_JSON).build();
			} else if (venodorExist.get(0).getStatus().equalsIgnoreCase("R")) {
				request.put("status", "rejected");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return Response.ok(request, MediaType.APPLICATION_JSON).build();
			} else if (venodorExist.get(0).getStatus().equalsIgnoreCase("D")) {
				request.put("status", "deleted");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return Response.ok(request, MediaType.APPLICATION_JSON).build();
			}
		} else {
			request.put("status", "notExist");
			log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
			return Response.ok(request, MediaType.APPLICATION_JSON).build();
		}
		eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
		List<EFmFmSupervisorMasterPO> supervisorDetail = iFieldAppDetailsBO.getSupervisorMobileNumberStatus(
				eFmFmSupervisorMasterPO.getMobileNumber(), new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
		List<EFmFmEscortMasterPO> eFmFmEscortMasterPO = iVehicleCheckInBO.getEscostMobileNoDetails(
				eFmFmSupervisorMasterPO.getMobileNumber(), new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
		List<EFmFmUserMasterPO> userMasterDetail = employeeDetailBO.getEmpDetailsFromMobileNumberAndBranchId(
				Base64.getEncoder().encodeToString(eFmFmSupervisorMasterPO.getMobileNumber().getBytes("utf-8")),new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
		ArrayList<Integer> statusStored = new ArrayList<Integer>();
		int dupCounts=0;
		if (!supervisorDetail.isEmpty() || !eFmFmEscortMasterPO.isEmpty() || !userMasterDetail.isEmpty()) {			
			statusStored.add(supervisorDetail.size());
			statusStored.add(eFmFmEscortMasterPO.size());
			statusStored.add(userMasterDetail.size());
			for (int val:statusStored){
				if(val >0){
					dupCounts++;
				}
			}			
			if(dupCounts>0){ 
				request.put("status", "DUPMOBNO");
					  log.info("dupCounts :" + dupCounts);
					  return Response.ok(request, MediaType.APPLICATION_JSON).build(); 
			}	
		}
		Date dateOfBirth = dateFormat.parse(eFmFmSupervisorMasterPO.getDobDate());
		eFmFmSupervisorMasterPO.setDateOfBirth(dateOfBirth);
		eFmFmSupervisorMasterPO.setIsActive(eFmFmSupervisorMasterPO.getIsActive());
		eFmFmSupervisorMasterPO.setUserName(eFmFmSupervisorMasterPO.getSupervisorEmpId());
		eFmFmSupervisorMasterPO.setUserType("Supervisor");
		eFmFmSupervisorMasterPO.setWrongPassAttempt(0);
		eFmFmSupervisorMasterPO.setTempPassWordChange(false);
		eFmFmSupervisorMasterPO.setLoggedIn(false);
		eFmFmSupervisorMasterPO.setImeiNumber("NO");
		eFmFmSupervisorMasterPO.setEfmFmVendorMaster(eFmFmSupervisorMasterPO.getEfmFmVendorMaster());
		eFmFmSupervisorMasterPO.setPoliceVerification("NO");
		int supervisorId=iFieldAppDetailsBO.getSupervisorId(eFmFmSupervisorMasterPO);
		if(supervisorId !=0){
			request.put("status", "success");
			request.put("supervisorId",supervisorId);
			return Response.ok(request, MediaType.APPLICATION_JSON).build();
		}
		/*
		 * try { Thread thread1 = new Thread(new Runnable() {
		 * 
		 * @Override public void run() { SendMailBySite mailSender=new
		 * SendMailBySite(); String toMailId =
		 * ContextLoader.getContext().getMessage("user.toMailSender", null,
		 * "toMailSender", null); mailSender.mailForEscortRegistration(toMailId,
		 * "team", eFmFmEscortMasterPO.getFirstName()); } }); thread1.start(); }
		 * catch (Exception e) { log.info("vendor excel registration mail" + e);
		 * }
		 */

		request.put("status", "failed");
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}
	
	
	@POST
	@Path("/uniqueMobileNumberCheck")
	@Consumes("application/json")
	@Produces("application/json")
	public Response uniqueMobileNumberCheck(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) throws ParseException, UnsupportedEncodingException {
		Map<String, Object> request = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmSupervisorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		String response=uniqueMobileNumberValidation(eFmFmSupervisorMasterPO.getMobileNumber(), String.valueOf(eFmFmSupervisorMasterPO.getBranchId()),eFmFmSupervisorMasterPO.getUserId());
		request.put("status",response);
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}
	
	public String uniqueMobileNumberValidation(String mobileNumber,String branchId,int userId) throws UnsupportedEncodingException{		
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");		
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		String response="success";
		List<EFmFmSupervisorMasterPO> supervisorDetail = iFieldAppDetailsBO.getSupervisorMobileNumberStatus(mobileNumber,new MultifacilityService().combinedBranchIdDetails(userId,String.valueOf(branchId)));
		List<EFmFmEscortMasterPO> eFmFmEscortMasterPO = iVehicleCheckInBO.getEscostMobileNoDetails(mobileNumber, new MultifacilityService().combinedBranchIdDetails(userId,String.valueOf(branchId)));
		List<EFmFmUserMasterPO> userMasterDetail = employeeDetailBO.getEmpDetailsFromMobileNumberAndBranchId(Base64.getEncoder().encodeToString(mobileNumber.getBytes("utf-8")),new MultifacilityService().combinedBranchIdDetails(userId,String.valueOf(branchId)));
		ArrayList<Integer> statusStored = new ArrayList<Integer>();
		int dupCounts=0;
		if (!supervisorDetail.isEmpty() || !eFmFmEscortMasterPO.isEmpty() || !userMasterDetail.isEmpty()) {			
			statusStored.add(supervisorDetail.size());
			statusStored.add(eFmFmEscortMasterPO.size());
			statusStored.add(userMasterDetail.size());
			for (int val:statusStored){
				if(val >0){
					dupCounts++;
				}
			}			
			if(dupCounts>0){ 
				response="DUPMOBNO";			 
			}	
		}		
		return response;		
	}
	
	

	/**
	 * The getAllSupervisorDetailsByVendor method implemented. for getting
	 * Active/In-Active Supervisor based on the vendor.
	 *
	 * @author Rajan R
	 * 
	 * @since 2017-01-13
	 * 
	 * @return list of active/in-active Status.
	 */
	@POST
	@Path("/getAllSupDetailsByVendor")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getAllSupervisorDetailsByVendor(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) {
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmSupervisorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		
		eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchId()));
		List<Map<String, Object>> listOfSupervisorDetails = new ArrayList<Map<String, Object>>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		List<EFmFmSupervisorMasterPO> supervisorDetails = iFieldAppDetailsBO.getAllSupervisorByVendorDetail(
				eFmFmSupervisorMasterPO.getEfmFmVendorMaster().getVendorId(),
				new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()),
				eFmFmSupervisorMasterPO.getIsActive());
		if (!supervisorDetails.isEmpty()) {
			for (EFmFmSupervisorMasterPO listOfSupervisor : supervisorDetails) {
				Map<String, Object> supvisorList = new HashMap<String, Object>();
				supvisorList.put("supervisorId", listOfSupervisor.getSupervisorId());
				supvisorList.put("supervisorName", listOfSupervisor.getFirstName());
				supvisorList.put("supervisorlastName", listOfSupervisor.getLastName());
				supvisorList.put("supervisorMobileNumber", listOfSupervisor.getMobileNumber());
				supvisorList.put("supervisorAddress", listOfSupervisor.getPermanentAddress());
				supvisorList.put("supervisorFatherName", listOfSupervisor.getFatherName());
				supvisorList.put("supervisorGender", listOfSupervisor.getGender());
				supvisorList.put("supervisorEmployeeId", listOfSupervisor.getSupervisorEmpId());
				supvisorList.put("vendorName", listOfSupervisor.getEfmFmVendorMaster().getVendorName());
				supvisorList.put("vendorId", listOfSupervisor.getEfmFmVendorMaster().getVendorId());
				supvisorList.put("emailId", listOfSupervisor.getEmailId());
				if(listOfSupervisor.getRemarks()==null){
					supvisorList.put("remarks","NA");
				}else{
					supvisorList.put("remarks", listOfSupervisor.getRemarks());
				}
				supvisorList.put("supervisordob", dateFormat.format(listOfSupervisor.getDateOfBirth()));
				supvisorList.put("designation", listOfSupervisor.getDesignation());
				supvisorList.put("presentAddress", listOfSupervisor.getPresentAddress());
				supvisorList.put("physicallyChallenged", listOfSupervisor.getPhysicallyChallenged());
				supvisorList.put("facilityName", listOfSupervisor.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				try {
					String profilePicPath = "";
					if (listOfSupervisor.getProfilePicPath() != null && listOfSupervisor.getProfilePicPath() != "") {
						profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
								null);
					} else {
						profilePicPath = ContextLoader.getContext().getMessage("default.profilePic", null, "profilePic",
								null);
					}
					supvisorList.put("profilePic", profilePicPath + listOfSupervisor.getProfilePicPath()
							.substring(listOfSupervisor.getProfilePicPath().indexOf("upload") - 1));
				} catch (Exception e) {
					String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
							"profilePic", null);
					supvisorList.put("profilePic", defaultProfilePic);
				}
				listOfSupervisorDetails.add(supvisorList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return Response.ok(listOfSupervisorDetails, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/vehicleonroad")
	public Response vehiclesOnRoad(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		List<Map<String, Object>> listOfVehicle = new ArrayList<Map<String, Object>>();
		List<EFmFmAssignRoutePO> vehiclesOnRoad = assignRouteBO
				.getAllOnRoadVehicleByVendors(eFmFmVendorMasterPO.getVendorId(), eFmFmVendorMasterPO.getBranchId());
		if (!(vehiclesOnRoad.isEmpty())) {
			for (EFmFmAssignRoutePO vehicleDetails : vehiclesOnRoad) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("checkInId", vehicleDetails.getEfmFmVehicleCheckIn().getCheckInId());
				vehicleList.put("driverName",
						vehicleDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				vehicleList.put("driverId",
						vehicleDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				vehicleList.put("MobileNumber",
						vehicleDetails.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				vehicleList.put("deviceId",
						vehicleDetails.getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId());
				vehicleList.put("vehicleNumber",
						vehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				vehicleList.put("vehicleId",
						vehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				vehicleList.put("vendorId", vehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorId());
				vehicleList.put("vendorName", vehicleDetails.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorName());
				vehicleList.put("RouteName",
						vehicleDetails.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				listOfVehicle.add(vehicleList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(listOfVehicle, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/checkInVehicleByVendor")
	public Response checkInVehicleByVendor(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		List<Map<String, Object>> listOfVehicle = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleCheckInPO> listOfCheckedInVehicle = iVehicleCheckInBO
				.getAllCheckedInVehicleByVendors(eFmFmVendorMasterPO.getVendorId(), eFmFmVendorMasterPO.getBranchId());
		if (!(listOfCheckedInVehicle.isEmpty())) {
			for (EFmFmVehicleCheckInPO vehicleDetails : listOfCheckedInVehicle) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("checkInId", vehicleDetails.getCheckInId());
				vehicleList.put("driverName", vehicleDetails.getEfmFmDriverMaster().getFirstName());
				vehicleList.put("driverNumber", vehicleDetails.getEfmFmDriverMaster().getMobileNumber());
				vehicleList.put("driverId", vehicleDetails.getEfmFmDriverMaster().getDriverId());
				vehicleList.put("deviceId", vehicleDetails.geteFmFmDeviceMaster().getDeviceId());
				vehicleList.put("vehicleNumber", vehicleDetails.getEfmFmVehicleMaster().getVehicleNumber());
				vehicleList.put("vehicleId", vehicleDetails.getEfmFmVehicleMaster().getVehicleId());
				vehicleList.put("vendorId",
						vehicleDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorId());
				vehicleList.put("vendorName",
						vehicleDetails.getEfmFmVehicleMaster().getEfmFmVendorMaster().getVendorName());
				listOfVehicle.add(vehicleList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(listOfVehicle, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/allroutes")
	public Response getAllRoutesOfParticulaZone(EFmFmAssignRoutePO assignRoutePO)
			throws UnsupportedEncodingException, ParseException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		assignRoutePO.setTripAssignDate(new Date());
		DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
		List<EFmFmAssignRoutePO> assignRoute = null;
		if (assignRoutePO.getAdvanceFlag().equalsIgnoreCase("false")) {
			assignRoute = assignRouteBO.getListOfRoutesByVendor(assignRoutePO);
		}
		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<Map<String, Object>> allRoutes = new ArrayList<Map<String, Object>>();
		if (!(assignRoute.isEmpty())) {
			for (EFmFmAssignRoutePO assignRoutes : assignRoute) {
				List<Map<String, Object>> tripAllDetails = new ArrayList<Map<String, Object>>();
				Map<String, Object> requests = new HashMap<String, Object>();
				StringBuffer waypoints = new StringBuffer();
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
				if (assignRoutes.getTripType().equalsIgnoreCase("PICKUP")) {
					employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployees(assignRoutes.getAssignRouteId());
				} else {
					employeeTripDetailPO = iCabRequestBO.getDropTripAllSortedEmployees(assignRoutes.getAssignRouteId());
				}
				if (!(employeeTripDetailPO.isEmpty())) {
					for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
						String wayPointsAdhocRequest = "";
						Map<String, Object> employeeDetails = new HashMap<String, Object>();
						employeeDetails
								.put("name",
										new String(Base64.getDecoder().decode(employeeTripDetail
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getFirstName()),
												"utf-8"));
						employeeDetails.put("employeeId", employeeTripDetail.geteFmFmEmployeeTravelRequest()
								.getEfmFmUserMaster().getEmployeeId());
						employeeDetails.put("requestId",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestId());
						employeeDetails.put("requestType",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestType());
						employeeDetails.put("tripDetailId", employeeTripDetail.getEmpTripId());
						employeeDetails
								.put("physicallyChallenged",
										new String(
												Base64.getDecoder()
														.decode(employeeTripDetail.geteFmFmEmployeeTravelRequest()
																.getEfmFmUserMaster().getPhysicallyChallenged()),
												"utf-8"));
						employeeDetails
								.put("isInjured",
										new String(Base64.getDecoder().decode(employeeTripDetail
												.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getIsInjured()),
												"utf-8"));
						employeeDetails.put("pragnentLady", new String(Base64.getDecoder().decode(employeeTripDetail
								.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getPragnentLady()), "utf-8"));
						if (employeeTripDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("DROP")) {
							if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("D")) {
								employeeDetails.put("boardedStatus", "Dropped");
							} else if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO")) {
								employeeDetails.put("boardedStatus", "No Show");
							} else {
								employeeDetails.put("boardedStatus", "Yet to dropped");
							}

						} else if (employeeTripDetail.getEfmFmAssignRoute().getTripType().equalsIgnoreCase("PICKUP")) {
							if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("B")) {
								employeeDetails.put("boardedStatus", "PickedUp");
							} else if (employeeTripDetail.getBoardedFlg().equalsIgnoreCase("NO")) {
								employeeDetails.put("boardedStatus", "No Show");
							} else {
								employeeDetails.put("boardedStatus", "Yet to picked up");
							}
						}
						if (assignRoutes.getRouteGenerationType().equalsIgnoreCase("AdhocRequest")) {
							wayPointsAdhocRequest = employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
									.geteFmFmEmployeeRequestMaster().getOriginLattitudeLongitude() + "|";
							if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
									.geteFmFmEmployeeRequestMaster().getDestination1AddressLattitudeLongitude()
									.equalsIgnoreCase("N")) {
								wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
										.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
										.getDestination1AddressLattitudeLongitude() + "|";
								if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
										.geteFmFmEmployeeRequestMaster().getDestination2AddressLattitudeLongitude()
										.equalsIgnoreCase("N")) {
									wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
											.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
											.getDestination2AddressLattitudeLongitude() + "|";
									if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
											.geteFmFmEmployeeRequestMaster().getDestination3AddressLattitudeLongitude()
											.equalsIgnoreCase("N")) {
										wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
												.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
												.getDestination3AddressLattitudeLongitude() + "|";
										if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
												.geteFmFmEmployeeRequestMaster()
												.getDestination4AddressLattitudeLongitude().equalsIgnoreCase("N")) {
											wayPointsAdhocRequest = wayPointsAdhocRequest + employeeTripDetailPO.get(0)
													.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
													.getDestination4AddressLattitudeLongitude() + "|";
											if (!employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
													.geteFmFmEmployeeRequestMaster()
													.getDestination5AddressLattitudeLongitude().equalsIgnoreCase("N")) {
												wayPointsAdhocRequest = wayPointsAdhocRequest
														+ employeeTripDetailPO.get(0).geteFmFmEmployeeTravelRequest()
																.geteFmFmEmployeeRequestMaster()
																.getDestination5AddressLattitudeLongitude()
														+ "|";
											}
										}
									}
								}
							}
							waypoints.append(wayPointsAdhocRequest + employeeTripDetailPO.get(0)
									.geteFmFmEmployeeTravelRequest().geteFmFmEmployeeRequestMaster()
									.getEndDestinationAddressLattitudeLongitude() + "|");
							employeeDetails.put("address",
									new String(
											Base64.getDecoder().decode(employeeTripDetail
													.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()),
											"utf-8"));

						}

						else if (assignRoutes.getRouteGenerationType().contains("nodal")) {
							if (!(waypoints.toString().contains(employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints()))) {
								waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest()
										.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPoints() + "|");
							}
							employeeDetails.put("address", employeeTripDetail.geteFmFmEmployeeTravelRequest()
									.geteFmFmRouteAreaMapping().geteFmFmNodalAreaMaster().getNodalPointName());

						} else {
							waypoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
									.getLatitudeLongitude() + "|");
							employeeDetails.put("address",
									new String(
											Base64.getDecoder().decode(employeeTripDetail
													.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getAddress()),
											"utf-8"));
						}
						employeeDetails.put("employeeNumber", new String(Base64.getDecoder().decode(employeeTripDetail
								.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getMobileNumber()), "utf-8"));
						employeeDetails.put("empComingStatus", employeeTripDetail.getComingStatus());
						employeeDetails.put("requestColor",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getRequestColor());

						employeeDetails.put("tripType", assignRoutes.getTripType());
						employeeDetails.put("empArea", employeeTripDetail.geteFmFmEmployeeTravelRequest()
								.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
						if (new String(Base64.getDecoder().decode(
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender()),
								"utf-8").equalsIgnoreCase("Male")) {
							employeeDetails.put("gender", 1);
						} else {
							employeeDetails.put("gender", 2);
						}
						employeeDetails.put("boardedFlg", employeeTripDetail.getBoardedFlg());
						employeeDetails.put("tripTime",
								employeeTripDetail.geteFmFmEmployeeTravelRequest().getShiftTime());
						if (assignRoutes.getTripType().equalsIgnoreCase("PICKUP")) {
							employeeDetails.put("pickUpTime",
									employeeTripDetail.geteFmFmEmployeeTravelRequest().getPickUpTime());
						} else {
							employeeDetails.put("pickUpTime",
									employeeTripDetail.geteFmFmEmployeeTravelRequest().getDropSequence());
						}
						requests.put("boardedFlg", employeeTripDetail.getBoardedFlg());
						tripAllDetails.add(employeeDetails);

					}
					if (assignRoutes.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoute.get(0).getNodalPoints());
					}

				} else {
					if (assignRoutes.getRouteGenerationType().contains("nodalAdhoc")) {
						waypoints.append(assignRoute.get(0).getNodalPoints());
					}
				}
				requests.put("assignRouteId", assignRoutes.getAssignRouteId());
				requests.put("tripType", assignRoutes.getTripType());
				if (assignRoutes.getEscortRequredFlag().equalsIgnoreCase("Y")) {
					try {
						int escortId = assignRoutes.geteFmFmEscortCheckIn().getEscortCheckInId();
						log.info("escortId" + escortId);
						requests.put("escortName",
								assignRoutes.geteFmFmEscortCheckIn().geteFmFmEscortMaster().getFirstName());
						requests.put("escortId", assignRoutes.geteFmFmEscortCheckIn().getEscortCheckInId());
					} catch (Exception e) {
						requests.put("escortId", "N");
						requests.put("escortName", "Escort Required But Not Available");
					}
				} else {
					requests.put("escortName", "Not Required");
				}
				requests.put("escortRequired", assignRoutes.getEscortRequredFlag());

				// Driver Maximum hours per day basis.now taking 32400 in
				// seconds as a standard 9 hours
				if ((assignRoutes.getEfmFmVehicleCheckIn().getTotalTravelTime()) >= 32400) {
					requests.put("perdayWorkingHours", "Yes");
				} else {
					requests.put("perdayWorkingHours", "No");
				}
				try {
					if (assignRoutes.getTripType().equalsIgnoreCase("PICKUP") && (assignRoutes.getEfmFmVehicleCheckIn()
							.getEfmFmVehicleMaster().getVehicleNumber().contains("DUMMY"))) {
						int shiftHours = assignRoutes.getShiftTime().getHours();
						String actualShiftTimeForB2b = "";
						requests.put("suggestiveVehicleNumber", "No");
						if (!(shiftHours == 1 || shiftHours == 2)) {
							log.info("getAssignRouteId" + assignRoutes.getAssignRouteId());
							int backShiftTime = assignRoutes.getShiftTime().getHours() - 3;
							actualShiftTimeForB2b = backShiftTime + ":" + "10" + ":00";
							if (actualShiftTimeForB2b.length() != 2) {
								actualShiftTimeForB2b = "0" + backShiftTime + ":" + "10" + ":00";
							}
						} else {
							if (shiftHours == 1) {
								actualShiftTimeForB2b = "22" + ":" + "10" + ":00";
							} else if (shiftHours == 2) {
								actualShiftTimeForB2b = "23" + ":" + "10" + ":00";
							}
							DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
							log.info("requestDaterequestDaterequestDate" + assignRoutes.getTripAssignDate());
							int n = 1;
							Date dateBefore = new Date(
									(assignRoutes.getTripAssignDate().getTime() - n * 3 * 3600 * 1000)
											- n * 3 * 3600 * 1000);
							String requestDate = requestDateFormat.format(dateBefore);
							assignRoutePO.setToDate(requestDate);
						}
						Date shift = shiftTimeFormater.parse(actualShiftTimeForB2b);
						java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
						assignRoutePO.setShiftTime(shiftTime);
						assignRoutePO.setTripType("DROP");
						log.info("Back2backShiftTime" + shiftTime);
						List<EFmFmAssignRoutePO> b2bDetails = assignRouteBO
								.getAllRoutesBasedOnTripTypeShiftTimeAndDate(assignRoutePO);
						log.info("b2bDetails" + b2bDetails.size());
						if (!(b2bDetails.isEmpty())) {
							for (EFmFmAssignRoutePO b2bDetail : b2bDetails) {
								// back 2 back route employee details by
								// employeeId
								List<EFmFmEmployeeTripDetailPO> employeeTripData = iCabRequestBO
										.getLastDropEmployeeDetail(b2bDetail.getAssignRouteId());
								log.info("back 2 back route employee details" + employeeTripData.size());
								// current route employee details by employeeId
								List<EFmFmEmployeeTripDetailPO> currentRouteTripData = iCabRequestBO
										.getDropTripAllSortedEmployees(assignRoutes.getAssignRouteId());
								log.info("current route employee details" + currentRouteTripData.size());

								if (!(employeeTripData.isEmpty()) && !(currentRouteTripData.isEmpty())) {
									if (employeeTripData.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
											.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName()
											.equalsIgnoreCase(currentRouteTripData.get(currentRouteTripData.size() - 1)
													.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster()
													.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName())) {
										requests.put("suggestiveVehicleNumber", b2bDetail.getEfmFmVehicleCheckIn()
												.getEfmFmVehicleMaster().getVehicleNumber());
									}

								}
							}
						}
					} else {
						requests.put("suggestiveVehicleNumber", "No");

					}
				} catch (Exception e) {
					log.info("Back2backShiftTime" + e);
				}
				requests.put("routeType", assignRoutes.getRouteGenerationType());
				requests.put("vehicleType",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleModel());
				requests.put("transportNumber",
						assignRoutes.geteFmFmClientBranchPO().getTransportContactNumberForMsg());
				requests.put("tripActualAssignDate", dateFormat.format(assignRoutes.getTripAssignDate()));
				requests.put("shiftTime", shiftTimeFormater.format(assignRoutes.getShiftTime()));
				requests.put("vehicleStatus", assignRoutes.getVehicleStatus());
				requests.put("bucketStatus", assignRoutes.getBucketStatus());
				requests.put("tripStatus", assignRoutes.getTripStatus());
				requests.put("waypoints", waypoints);
				requests.put("baseLatLong", assignRoutes.geteFmFmClientBranchPO().getLatitudeLongitude());
				requests.put("routeId", assignRoutes.getAssignRouteId());
				requests.put("driverName", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getFirstName());
				requests.put("driverNumber",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getMobileNumber());
				requests.put("vehicleNumber",
						assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleNumber());
				requests.put("vendorId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster()
						.getEfmFmVendorMaster().getVendorId());
				requests.put("driverId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmDriverMaster().getDriverId());
				requests.put("checkInId", assignRoutes.getEfmFmVehicleCheckIn().getCheckInId());
				requests.put("vehicleId", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getVehicleId());
				requests.put("vehicleAvailableCapacity",
						(assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity() - 1)
								- employeeTripDetailPO.size());
				requests.put("capacity", assignRoutes.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity());
				requests.put("deviceNumber",
						assignRoutes.getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId());
				requests.put("empDetails", tripAllDetails);
				requests.put("routeName", assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				allRoutes.add(requests);
			}
			allRoutesSingleObj.put("routeDetails", allRoutes);
			allRoutesSingleObj.put("totalRoutes", assignRoute.size());
		}
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/allZonesByVendor")
	public Response getAllZonesAndRoutesDetails(EFmFmAssignRoutePO assignRoutePO) {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
        IVendorDetailsBO vendorDetailsBO = (IVendorDetailsBO) ContextLoader.getContext().getBean("IVendorDetailsBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		assignRoutePO.setTripAssignDate(new Date());
		List<String> zones = new ArrayList<String>();
			
//		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.getAllZoneByVendor(assignRoutePO.getVendorId(),
//				assignRoutePO.geteFmFmClientBranchPO().getBranchId());
//		
		
        EFmFmVendorMasterPO VendorDetails=vendorDetailsBO.getParticularVendorDetail(assignRoutePO.getVendorId());       
		List<EFmFmAssignRoutePO> assignRoute = assignRouteBO.getAllRoutesAllocatedToVendor(VendorDetails.getVendorName());	
		Map<String, Object> allRoutesSingleObj = new HashMap<String, Object>();
		List<TreeMap<String, Object>> allRoutes = new ArrayList<TreeMap<String, Object>>();
		log.info("total Routes v" + assignRoute.size());
		if (!(assignRoute.isEmpty())) {
			for (EFmFmAssignRoutePO assignRoutes : assignRoute) {
				if (!(zones.contains(assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName()))) {
					TreeMap<String, Object> requests = new TreeMap<String, Object>();
					zones.add(assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					EFmFmRouteAreaMappingPO routeId = new EFmFmRouteAreaMappingPO();
					routeId.setRouteAreaId(assignRoutes.geteFmFmRouteAreaMapping().getRouteAreaId());
					EFmFmZoneMasterPO eFmFmZoneMaster = new EFmFmZoneMasterPO();
					eFmFmZoneMaster.setZoneId(assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
					routeId.seteFmFmZoneMaster(eFmFmZoneMaster);
					assignRoutePO.seteFmFmRouteAreaMapping(routeId);
					List<EFmFmAssignRoutePO> routesInZone = assignRouteBO
							.getAllRoutesOfParticularZoneByVendor(assignRoutePO);
					requests.put("routeId", assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId());
					requests.put("routeName",
							assignRoutes.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
					requests.put("NumberOfRoutes", routesInZone.size());
					List<EFmFmEmployeeTripDetailPO> employeeTripDetail = iCabRequestBO
							.getParticularTripAllEmployees(assignRoutes.getAssignRouteId());
					int prgnentLady = 0;
					int physicallyChallaged = 0;
					int injured = 0;
					if (!(employeeTripDetail.isEmpty())) {
						for (EFmFmEmployeeTripDetailPO tripDetail : employeeTripDetail) {
							if (new String(Base64.getDecoder().decode(
									tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getPragnentLady()))
											.equalsIgnoreCase("Yes")) {
								prgnentLady++;
							}
							if (new String(Base64.getDecoder().decode(
									tripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getIsInjured()))
											.equalsIgnoreCase("Yes")) {
								injured++;

							}
							if (new String(Base64.getDecoder().decode(tripDetail.geteFmFmEmployeeTravelRequest()
									.getEfmFmUserMaster().getPhysicallyChallenged())).equalsIgnoreCase("Yes")) {
								physicallyChallaged++;
							}

						}
					}
					requests.put("physicallyChallenged", physicallyChallaged);
					requests.put("isInjured", injured);
					requests.put("pragnentLady", prgnentLady);
					allRoutes.add(requests);
				}
			}
		}
		allRoutesSingleObj.put("totalNumberOfRoutes", assignRoute.size());
		allRoutesSingleObj.put("routeDetails", allRoutes);
		log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allRoutesSingleObj, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * The modifySupervisorDetails method implemented. for Modifying Supervisor
	 * details.
	 *
	 * @author Rajan R
	 * 
	 * @since 2017-01-13
	 * 
	 * @return modified Status.
	 * @throws ParseException
	 */
	@POST
	@Path("/modifySupervisorDetails")
	@Consumes("application/json")
	@Produces("application/json")
	public Response modifySupervisorDetails(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) throws ParseException {
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmSupervisorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
		List<EFmFmSupervisorMasterPO> supervisorExist = iFieldAppDetailsBO
				.getSupervisorDetails(eFmFmSupervisorMasterPO.getSupervisorId(),new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("status", "notAvailable");
		if (!supervisorExist.isEmpty()) {
			supervisorExist.get(0).setFirstName(eFmFmSupervisorMasterPO.getFirstName());
			supervisorExist.get(0).setFatherName(eFmFmSupervisorMasterPO.getFatherName());
			supervisorExist.get(0).setLastName(eFmFmSupervisorMasterPO.getLastName());
			supervisorExist.get(0).setMiddleName(eFmFmSupervisorMasterPO.getMiddleName());
			supervisorExist.get(0).setDesignation(eFmFmSupervisorMasterPO.getDesignation());
			supervisorExist.get(0).setGender(eFmFmSupervisorMasterPO.getGender());
			supervisorExist.get(0).setPhysicallyChallenged(eFmFmSupervisorMasterPO.getPhysicallyChallenged());
			supervisorExist.get(0).setEmailId(eFmFmSupervisorMasterPO.getEmailId());
			supervisorExist.get(0).setPermanentAddress(eFmFmSupervisorMasterPO.getPermanentAddress());
			supervisorExist.get(0).setPermanentAddress(eFmFmSupervisorMasterPO.getPermanentAddress());
			supervisorExist.get(0).setMobileNumber(eFmFmSupervisorMasterPO.getMobileNumber());
			supervisorExist.get(0).setSupervisorEmpId(eFmFmSupervisorMasterPO.getSupervisorEmpId());
			Date dateOfBirth = dateFormat.parse(eFmFmSupervisorMasterPO.getDobDate());
			supervisorExist.get(0).setDateOfBirth(dateOfBirth);
			supervisorExist.get(0).setIsActive(eFmFmSupervisorMasterPO.getIsActive());
			supervisorExist.get(0).setEfmFmVendorMaster(eFmFmSupervisorMasterPO.getEfmFmVendorMaster());
			iFieldAppDetailsBO.update(supervisorExist.get(0));
			request.put("status", "success");
		}
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/supervisorStatusModified")
	@Consumes("application/json")
	@Produces("application/json")
	public Response supervisorStatusModified(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) throws ParseException {
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmSupervisorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
		List<EFmFmSupervisorMasterPO> supervisorExist = iFieldAppDetailsBO
				.getSupervisorPasswordDetailsFromSupervisorAndBranchId(eFmFmSupervisorMasterPO.getSupervisorId(),
						new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("status", "notModified");
		if (!supervisorExist.isEmpty()) {
			if(eFmFmSupervisorMasterPO.getIsActive().equalsIgnoreCase("N")){
				supervisorExist.get(0).setRemarks(eFmFmSupervisorMasterPO.getRemarks());
			}
			supervisorExist.get(0).setIsActive(eFmFmSupervisorMasterPO.getIsActive());
			iFieldAppDetailsBO.update(supervisorExist.get(0));
			request.put("status", "success");
		}
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/getAllSupervisorByStatus")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getAllSupervisorByStatus(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) throws ParseException {
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmSupervisorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			};
		List<Map<String, Object>> listOfSupervisorDetails = new ArrayList<Map<String, Object>>();
		
		List<EFmFmSupervisorMasterPO> supervisorDetails = iFieldAppDetailsBO
				.getAllSupervisorDetails(new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()), eFmFmSupervisorMasterPO.getIsActive());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		if (!supervisorDetails.isEmpty()) {
			for (EFmFmSupervisorMasterPO listOfSupervisor : supervisorDetails) {
				Map<String, Object> supvisorList = new HashMap<String, Object>();
				supvisorList.put("supervisorId", listOfSupervisor.getSupervisorId());
				supvisorList.put("supervisorName", listOfSupervisor.getFirstName());
				supvisorList.put("supervisorlastName", listOfSupervisor.getLastName());
				supvisorList.put("supervisorMobileNumber", listOfSupervisor.getMobileNumber());
				supvisorList.put("supervisorAddress", listOfSupervisor.getPermanentAddress());
				supvisorList.put("supervisorFatherName", listOfSupervisor.getFatherName());
				supvisorList.put("supervisorGender", listOfSupervisor.getGender());
				supvisorList.put("supervisorEmployeeId", listOfSupervisor.getSupervisorEmpId());
				supvisorList.put("vendorName", listOfSupervisor.getEfmFmVendorMaster().getVendorName());
				supvisorList.put("vendorId", listOfSupervisor.getEfmFmVendorMaster().getVendorId());
				supvisorList.put("emailId", listOfSupervisor.getEmailId());
				supvisorList.put("remarks", listOfSupervisor.getRemarks());
				supvisorList.put("supervisordob", dateFormat.format(listOfSupervisor.getDateOfBirth()));
				supvisorList.put("designation", listOfSupervisor.getDesignation());
				supvisorList.put("presentAddress", listOfSupervisor.getPresentAddress());
				supvisorList.put("physicallyChallenged", listOfSupervisor.getPhysicallyChallenged());
				supvisorList.put("facilityName", listOfSupervisor.getEfmFmVendorMaster().geteFmFmClientBranchPO().getBranchName());
				try {
					String profilePicPath = "";
					if (listOfSupervisor.getProfilePicPath() != null && listOfSupervisor.getProfilePicPath() != "") {
						profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
								null);
					} else {
						profilePicPath = ContextLoader.getContext().getMessage("default.profilePic", null, "profilePic",
								null);
					}
					supvisorList.put("profilePic", profilePicPath + listOfSupervisor.getProfilePicPath()
							.substring(listOfSupervisor.getProfilePicPath().indexOf("upload") - 1));
				} catch (Exception e) {
					String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
							"profilePic", null);
					supvisorList.put("profilePic", defaultProfilePic);
				}
				listOfSupervisorDetails.add(supvisorList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return Response.ok(listOfSupervisorDetails, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/vendorDetailsByVehicle")
	public Response vendorDetailsByVehicle(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		List<Map<String, Object>> listOfvendor = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleMasterPO> listOfCheckedInVehicle = iVehicleCheckInBO
				.getAllVehicleVendorsDetails(eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId());
		if (!(listOfCheckedInVehicle.isEmpty())) {
			for (EFmFmVehicleMasterPO vendorDetails : listOfCheckedInVehicle) {
				Map<String, Object> vendorList = new HashMap<String, Object>();
				vendorList.put("vendorId", vendorDetails.getEfmFmVendorMaster().getVendorId());
				vendorList.put("vendorName", vendorDetails.getEfmFmVendorMaster().getVendorName());
				listOfvendor.add(vendorList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(listOfvendor, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/vehicleByVendorDetails")
	public Response vehicleByVendorDetails(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		List<Map<String, Object>> listOfVehicle = new ArrayList<Map<String, Object>>();
		List<EFmFmVehicleMasterPO> listOfCheckedInVehicle = iVehicleCheckInBO.getAllAvailableVehiclesByVendor(
				eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId(), eFmFmVendorMasterPO.getVendorId());
		if (!(listOfCheckedInVehicle.isEmpty())) {
			for (EFmFmVehicleMasterPO vehicleDetails : listOfCheckedInVehicle) {
				Map<String, Object> vehicleList = new HashMap<String, Object>();
				vehicleList.put("vehicleNumber", vehicleDetails.getVehicleNumber());
				vehicleList.put("vehicleId", vehicleDetails.getVehicleId());
				vehicleList.put("vendorId", vehicleDetails.getEfmFmVendorMaster().getVendorId());
				vehicleList.put("vendorName", vehicleDetails.getEfmFmVendorMaster().getVendorName());
				listOfVehicle.add(vehicleList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(listOfVehicle, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/driverByVendorDetails")
	public Response driverByVendorDetails(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		List<Map<String, Object>> listOfDrivers = new ArrayList<Map<String, Object>>();
		List<EFmFmDriverMasterPO> listOfCheckedInDrivers = iVehicleCheckInBO.getAllAvailableDriverByVendor(
				eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId(), eFmFmVendorMasterPO.getVendorId());
		if (!(listOfCheckedInDrivers.isEmpty())) {
			for (EFmFmDriverMasterPO driverDetails : listOfCheckedInDrivers) {
				Map<String, Object> driverList = new HashMap<String, Object>();
				driverList.put("driverName", driverDetails.getFirstName());
				driverList.put("driverMobileNo", driverDetails.getMobileNumber());
				driverList.put("driverId", driverDetails.getDriverId());
				try {
					String profilePicPath = "";
					if (driverDetails.getProfilePicPath() != null && driverDetails.getProfilePicPath() != "") {
						profilePicPath = ContextLoader.getContext().getMessage("change.profilePic", null, "profilePic",
								null);
					} else {
						profilePicPath = ContextLoader.getContext().getMessage("default.profilePic", null, "profilePic",
								null);
					}
					driverList.put("profilePic", profilePicPath + driverDetails.getProfilePicPath()
							.substring(driverDetails.getProfilePicPath().indexOf("upload") - 1));
				} catch (Exception e) {
					String defaultProfilePic = ContextLoader.getContext().getMessage("default.profilePic", null,
							"profilePic", null);
					driverList.put("profilePic", defaultProfilePic);
				}
				listOfDrivers.add(driverList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(listOfDrivers, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/activeDeviceDetails")
	public Response activeDeviceDetails(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		log.info("serviceStart -UserId :" + eFmFmVendorMasterPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVendorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		List<Map<String, Object>> listOfDevice = new ArrayList<Map<String, Object>>();
		List<EFmFmDeviceMasterPO> listOfCheckedInDevice = iVehicleCheckInBO
				.getAllAvailableDeviceWithOutDummy(eFmFmVendorMasterPO.geteFmFmClientBranchPO().getBranchId());
		if (!(listOfCheckedInDevice.isEmpty())) {
			for (EFmFmDeviceMasterPO deviceDetails : listOfCheckedInDevice) {
				Map<String, Object> deviceList = new HashMap<String, Object>();
				deviceList.put("deviceMobileNo", deviceDetails.getMobileNumber());
				deviceList.put("deviceId", deviceDetails.getDeviceId());
				listOfDevice.add(deviceList);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmVendorMasterPO.getUserId());
		return Response.ok(listOfDevice, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/fieldAppForgotpassword")
	public Response fieldAppForgotpassword(final EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) throws IOException {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmSupervisorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
//		
		log.info("device token" + eFmFmSupervisorMasterPO.getDeviceToken());
		log.info("device id" + eFmFmSupervisorMasterPO.getImeiNumber());
		log.info("device Type" + eFmFmSupervisorMasterPO.getDeviceType());
		eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
		List<EFmFmSupervisorMasterPO> supervisorDetail = iFieldAppDetailsBO.getSupervisorMobileNumberDetails(
				eFmFmSupervisorMasterPO.getMobileNumber(), new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
		List<EFmFmEscortMasterPO> eFmFmEscortMasterPO = iVehicleCheckInBO.getEscostMobileNoDetails(
				eFmFmSupervisorMasterPO.getMobileNumber(), new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
		List<EFmFmUserMasterPO> userMasterDetail = employeeDetailBO.getParticularEmpDetailsFromMobileNumberAndBranhId(
				Base64.getEncoder().encodeToString(eFmFmSupervisorMasterPO.getMobileNumber().getBytes("utf-8")),
				eFmFmSupervisorMasterPO.getBranchId());
		ArrayList<Integer> statusStored = new ArrayList<Integer>();
		int dupCounts=0;
		if (!supervisorDetail.isEmpty() || !eFmFmEscortMasterPO.isEmpty() || !userMasterDetail.isEmpty()) {
			statusStored.add(supervisorDetail.size());
			statusStored.add(eFmFmEscortMasterPO.size());
			statusStored.add(userMasterDetail.size());
			for (int val:statusStored){
				if(val >0){
					dupCounts++;
				}
			}				
			if(dupCounts>1){ 
					  responce.put("status", "DUPMOBNO"); 
					  log.info("dupCounts :" + dupCounts);
					  return Response.ok(responce, MediaType.APPLICATION_JSON).build(); 
			}
			if (!supervisorDetail.isEmpty()) {
				if (supervisorDetail.size() == 1) {
					if (supervisorDetail.get(0).getIsActive().equalsIgnoreCase("P")) {
						responce.put("status", "PENDING");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
					responce = supervisorForgetPassword(eFmFmSupervisorMasterPO, supervisorDetail);
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				} else {
					responce.put("status", "DUPMOBNO");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
			}
			if (!eFmFmEscortMasterPO.isEmpty()) {
				if (eFmFmEscortMasterPO.size() == 1) {
					if (eFmFmEscortMasterPO.get(0).getIsActive().equalsIgnoreCase("N")) {
						responce.put("status", "PENDING");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
					responce = escortForgetPassword(eFmFmSupervisorMasterPO, eFmFmEscortMasterPO);
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				} else {
					responce.put("status", "DUPMOBNO");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
			}
			if (!userMasterDetail.isEmpty()) {
				if (userMasterDetail.size() == 1) {
					if (userMasterDetail.get(0).getStatus().equalsIgnoreCase("N")) {
						responce.put("status", "PENDING");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					} else if (new String(Base64.getDecoder().decode(userMasterDetail.get(0).getEmployeeDepartment()),
							"utf-8").equalsIgnoreCase("transportteam")) {
						responce = employeeForgetPassword(eFmFmSupervisorMasterPO, userMasterDetail);
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					} else {
						responce.put("status", "TRANUSERONLYALLOWED");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
				} else {
					responce.put("status", "DUPMOBNO");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
			}
		}
		responce.put("status", "NOTEXIST");
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	public Map<String, Object> supervisorForgetPassword(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO,
			List<EFmFmSupervisorMasterPO> supervisorDetail) throws UnsupportedEncodingException {
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		log.info("forgot pass from device");
		Map<String, Object> responce = new HashMap<String, Object>();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		for (int i = 0; i < 4; i++) {
			result += numbers.get(i).toString();
		}
		if (!(supervisorDetail.isEmpty())) {
			if ((supervisorDetail.get(0).getWrongPassAttempt() == supervisorDetail.get(0).getEfmFmVendorMaster()
					.geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass())
					&& (getDisableTime(24, 0, supervisorDetail.get(0).getWrongPassAttemptDate()) > new Date()
							.getTime())) {
				responce.put("status", "accountDisableTwentyFourHours");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return responce;
			}
			if (supervisorDetail.get(0).getIsActive().equalsIgnoreCase("N")) {
				responce.put("status", "disable");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return responce;
			}
			Thread thread1 = new Thread(new Runnable() {
				@Override
				public void run() {
					MessagingService messaging = new MessagingService();
					String text = "Please find your temporary code for eFmFm application =" + result
							+ "\nFor feedback write to us @" + supervisorDetail.get(0).getEfmFmVendorMaster()
									.geteFmFmClientBranchPO().getFeedBackEmailId();
					try {
						messaging.cabHasLeftMessageForSch(supervisorDetail.get(0).getMobileNumber(), text, "no");
						log.info("Register Message Sent" + supervisorDetail.get(0).getMobileNumber());

					} catch (Exception e) {
						log.info("Register Message Sent Exception" + e);

					}
				}
			});
			thread1.start();
			supervisorDetail.get(0).setTempCode(result);
			supervisorDetail.get(0).setLoggedIn(false);
			iFieldAppDetailsBO.update(supervisorDetail.get(0));
			responce.put("status", "success");
			log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
			return responce;
		}
		responce.put("status", "invalid");
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return responce;

	}

	public Map<String, Object> escortForgetPassword(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO,
			List<EFmFmEscortMasterPO> eFmFmEscortMasterPO) throws UnsupportedEncodingException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		log.info("forgot pass from device");
		Map<String, Object> responce = new HashMap<String, Object>();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		for (int i = 0; i < 4; i++) {
			result += numbers.get(i).toString();
		}
		if (!(eFmFmEscortMasterPO.isEmpty())) {
			if ((eFmFmEscortMasterPO.get(0).getWrongPassAttempt() == eFmFmEscortMasterPO.get(0).getEfmFmVendorMaster()
					.geteFmFmClientBranchPO().getNumberOfAttemptsWrongPass())
					&& (getDisableTime(24, 0, eFmFmEscortMasterPO.get(0).getWrongPassAttemptDate()) > new Date()
							.getTime())) {
				responce.put("status", "accountDisableTwentyFourHours");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return responce;
			}
			if (eFmFmEscortMasterPO.get(0).getIsActive().equalsIgnoreCase("N")) {
				responce.put("status", "disable");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return responce;
			}
			Thread thread1 = new Thread(new Runnable() {
				@Override
				public void run() {

					MessagingService messaging = new MessagingService();
					String text = "Please find your temporary code for eFmFm application =" + result
							+ "\nFor feedback write to us @" + eFmFmEscortMasterPO.get(0).getEfmFmVendorMaster()
									.geteFmFmClientBranchPO().getFeedBackEmailId();
					try {
						messaging.cabHasLeftMessageForSch(eFmFmEscortMasterPO.get(0).getMobileNumber(), text, "no");
						log.info("Register Message Sent" + eFmFmEscortMasterPO.get(0).getMobileNumber());

					} catch (Exception e) {
						log.info("Register Message Sent Exception" + e);

					}
				}
			});
			thread1.start();
			eFmFmEscortMasterPO.get(0).setTempCode(result);
			eFmFmEscortMasterPO.get(0).setLoggedIn(false);
			iVehicleCheckInBO.updateEscortDetails(eFmFmEscortMasterPO.get(0));
			responce.put("status", "success");
			log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
			return responce;
		}
		responce.put("status", "invalid");
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return responce;

	}

	/*
	 * temp code Forgot password service call form mobile .
	 * 
	 */

	public Map<String, Object> employeeForgetPassword(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO,
			List<EFmFmUserMasterPO> empDetails) throws UnsupportedEncodingException {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		log.info("forgot pass from device");
		Map<String, Object> responce = new HashMap<String, Object>();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		for (int i = 0; i < 4; i++) {
			result += numbers.get(i).toString();
		}
		if (!(empDetails.isEmpty())) {
			if ((empDetails.get(0).getWrongPassAttempt() == empDetails.get(0).geteFmFmClientBranchPO()
					.getNumberOfAttemptsWrongPass())
					&& (getDisableTime(24, 0, empDetails.get(0).getWrongPassAttemptDate()) > new Date().getTime())) {
				responce.put("status", "accountDisableTwentyFourHours");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return responce;
			}
			if (empDetails.get(0).getStatus().equalsIgnoreCase("N")) {
				responce.put("status", "disable");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return responce;
			}
			Thread thread1 = new Thread(new Runnable() {
				@Override
				public void run() {

					MessagingService messaging = new MessagingService();
					String text = "Please find your temporary code for eFmFm application =" + result
							+ "\nFor feedback write to us @"
							+ empDetails.get(0).geteFmFmClientBranchPO().getFeedBackEmailId();
					try {
						messaging.cabHasLeftMessageForSch(
								new String(Base64.getDecoder().decode(empDetails.get(0).getMobileNumber()), "utf-8"),
								text, "no");
						log.info("Register Message Sent"
								+ new String(Base64.getDecoder().decode(empDetails.get(0).getMobileNumber()), "utf-8"));

					} catch (Exception e) {
						log.info("Register Message Sent Exception" + e);

					}
				}
			});
			thread1.start();
			empDetails.get(0).setTempCode(result);
			empDetails.get(0).setLoggedIn(false);
			employeeDetailBO.update(empDetails.get(0));
			responce.put("status", "success");
			log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
			return responce;
		}
		responce.put("status", "invalid");
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return responce;

	}

	@POST
	@Path("/fieldAppChangeForgotPassword")
	public Response fieldAppChangeForgotPassword(final EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO)
			throws IOException, ParseException {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmSupervisorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		
		log.info("device token" + eFmFmSupervisorMasterPO.getDeviceToken());
		log.info("device id" + eFmFmSupervisorMasterPO.getImeiNumber());
		log.info("device Type" + eFmFmSupervisorMasterPO.getDeviceType());
		eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
		List<EFmFmSupervisorMasterPO> supervisorDetail = iFieldAppDetailsBO.getSupervisorMobileNumberWithTokenDetails(
				eFmFmSupervisorMasterPO.getMobileNumber(), eFmFmSupervisorMasterPO.getTempCode(),
				new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
		List<EFmFmEscortMasterPO> eFmFmEscortMasterPO = iVehicleCheckInBO.getEscostMobileNoWithTokenDetails(
				eFmFmSupervisorMasterPO.getMobileNumber(), eFmFmSupervisorMasterPO.getTempCode(),
				new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
		final List<EFmFmUserMasterPO> userMasterDetail = employeeDetailBO
				.getParticularEmpDetailsFromEmployeeIdAndbranchIdAndToken(
						Base64.getEncoder().encodeToString(eFmFmSupervisorMasterPO.getMobileNumber().getBytes("utf-8")),
						eFmFmSupervisorMasterPO.getTempCode(), eFmFmSupervisorMasterPO.getBranchId());
		ArrayList<Integer> statusStored = new ArrayList<Integer>();
		int dupCounts=0;
		if (!supervisorDetail.isEmpty() || !eFmFmEscortMasterPO.isEmpty() || !userMasterDetail.isEmpty()) {
			statusStored.add(supervisorDetail.size());
			statusStored.add(eFmFmEscortMasterPO.size());
			statusStored.add(userMasterDetail.size());
				for (int val:statusStored){
					if(val >0){
						dupCounts++;
					}
				}				
			if(dupCounts>1){ 
					  responce.put("status", "DUPMOBNO"); 
					  log.info("dupCounts :" + dupCounts);
					  return Response.ok(responce, MediaType.APPLICATION_JSON).build(); 
			}
			if (!supervisorDetail.isEmpty()) {
				if (supervisorDetail.size() == 1) {
					if (supervisorDetail.get(0).getIsActive().equalsIgnoreCase("P")) {
						responce.put("status", "PENDING");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
					responce = supervisorChangeForgotPassword(eFmFmSupervisorMasterPO, supervisorDetail);
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				} else {
					responce.put("status", "DUPMOBNO");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
			}
			if (!eFmFmEscortMasterPO.isEmpty()) {
				if (eFmFmEscortMasterPO.size() == 1) {
					if (eFmFmEscortMasterPO.get(0).getIsActive().equalsIgnoreCase("N")) {
						responce.put("status", "PENDING");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
					responce = escortChangeForgotPassword(eFmFmSupervisorMasterPO, eFmFmEscortMasterPO);
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				} else {
					responce.put("status", "DUPMOBNO");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
			}
			if (!userMasterDetail.isEmpty()) {
				if (userMasterDetail.size() == 1) {
					if (userMasterDetail.get(0).getStatus().equalsIgnoreCase("N")) {
						responce.put("status", "PENDING");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}else if (new String(Base64.getDecoder().decode(userMasterDetail.get(0).getEmployeeDepartment()),
							"utf-8").equalsIgnoreCase("transportteam")) {
						responce = employeeChangeForgotPassword(eFmFmSupervisorMasterPO, userMasterDetail);
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					} else {
						responce.put("status", "TRANUSERONLYALLOWED");
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
				} else {
					responce.put("status", "DUPMOBNO");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
			}
		}
		responce.put("status", "NOTEXIST");
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	public Map<String, Object> supervisorChangeForgotPassword(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO,
			List<EFmFmSupervisorMasterPO> supervisorDetail) throws ParseException, UnsupportedEncodingException {
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		Map<String, Object> responce = new HashMap<String, Object>();
		PasswordEncryption passwordEncryption = new PasswordEncryption();
		/*
		 * DateFormat dateFormatter = new SimpleDateFormat(
		 * "dd-MM-yyyy hh:mm:ss a");
		 */
		if (!(eFmFmSupervisorMasterPO.getNewPassword()
				.matches(ContextLoader.getContext().getMessage("strong.password", null, "password", null)))) {
			responce.put("status", "wrongPattern");
			log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
			return responce;
		}
		if (!(supervisorDetail.isEmpty())) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			boolean passexist = false;
			eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
			List<EFmFmSupervisorMasterPO> passDetails = iFieldAppDetailsBO
					.getSupervisorPasswordDetailsFromSupervisorAndBranchId(supervisorDetail.get(0).getSupervisorId(),
							new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
			if (!(passDetails.isEmpty())) {
				log.info("passDetails.size()" + passDetails.size());
				for (EFmFmSupervisorMasterPO pass : passDetails) {
					if (encoder.matches(eFmFmSupervisorMasterPO.getNewPassword(), pass.getPassword())) {
						passexist = true;
						break;
					}

				}
			}
			if (passexist) {
				if (passDetails.size() <= supervisorDetail.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO()
						.getLastPassCanNotCurrentPass())
					responce.put("numberOfPasswords", passDetails.size());
				else {
					responce.put("numberOfPasswords", supervisorDetail.get(0).getEfmFmVendorMaster()
							.geteFmFmClientBranchPO().getLastPassCanNotCurrentPass());
				}
				responce.put("status", "oldpass");
				// responce.put("lastChangeDateTime",dateFormatter.format(passDetails.get(passDetails.size()
				// - 1).getCreationTime()));
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return responce;
			}

			if (supervisorDetail.get(0).getIsActive().equalsIgnoreCase("N")) {
				responce.put("status", "disable");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return responce;
			}
			supervisorDetail.get(0)
					.setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmSupervisorMasterPO.getNewPassword()));
			supervisorDetail.get(0).setPasswordChangeDate(new Date());
			supervisorDetail.get(0).setLastLoginTime(new Date());

			supervisorDetail.get(0).setWrongPassAttempt(0);
			supervisorDetail.get(0).setTempCode(null);
			supervisorDetail.get(0).setTempPassWordChange(false);
			iFieldAppDetailsBO.update(supervisorDetail.get(0));

			responce.put("status", "success");
			log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
			return responce;
		}
		responce.put("status", "fail");
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return responce;
	}

	public Map<String, Object> escortChangeForgotPassword(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO,
			List<EFmFmEscortMasterPO> eFmFmEscortMasterPO) throws ParseException, UnsupportedEncodingException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		Map<String, Object> responce = new HashMap<String, Object>();
		PasswordEncryption passwordEncryption = new PasswordEncryption();
		/*
		 * DateFormat dateFormatter = new SimpleDateFormat(
		 * "dd-MM-yyyy hh:mm:ss a");
		 */
		if (!(eFmFmSupervisorMasterPO.getNewPassword()
				.matches(ContextLoader.getContext().getMessage("strong.password", null, "password", null)))) {
			responce.put("status", "wrongPattern");
			log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
			return responce;
		}
		if (!(eFmFmEscortMasterPO.isEmpty())) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			boolean passexist = false;
			List<EFmFmEscortMasterPO> passDetails = iVehicleCheckInBO.getEscortPasswordDetailsFromEscortAndBranchId(
					eFmFmEscortMasterPO.get(0).getEscortId(), eFmFmSupervisorMasterPO.getBranchId());
			if (!(passDetails.isEmpty())) {
				log.info("passDetails.size()" + passDetails.size());
				for (EFmFmEscortMasterPO pass : passDetails) {
					if (encoder.matches(eFmFmSupervisorMasterPO.getNewPassword(), pass.getPassword())) {
						passexist = true;
						break;
					}

				}
			}
			if (passexist) {
				if (passDetails.size() <= eFmFmEscortMasterPO.get(0).getEfmFmVendorMaster().geteFmFmClientBranchPO()
						.getLastPassCanNotCurrentPass())
					responce.put("numberOfPasswords", passDetails.size());
				else {
					responce.put("numberOfPasswords", eFmFmEscortMasterPO.get(0).getEfmFmVendorMaster()
							.geteFmFmClientBranchPO().getLastPassCanNotCurrentPass());
				}
				responce.put("status", "oldpass");
				// responce.put("lastChangeDateTime",dateFormatter.format(passDetails.get(passDetails.size()
				// - 1).getCreationTime()));
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return responce;
			}

			if (eFmFmEscortMasterPO.get(0).getIsActive().equalsIgnoreCase("N")) {
				responce.put("status", "disable");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return responce;
			}
			eFmFmEscortMasterPO.get(0)
					.setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmSupervisorMasterPO.getNewPassword()));
			eFmFmEscortMasterPO.get(0).setPasswordChangeDate(new Date());
			eFmFmEscortMasterPO.get(0).setLastLoginTime(new Date());
			eFmFmEscortMasterPO.get(0).setWrongPassAttempt(0);
			eFmFmEscortMasterPO.get(0).setTempCode(null);
			eFmFmEscortMasterPO.get(0).setTempPassWordChange(false);
			iVehicleCheckInBO.updateEscortDetails(eFmFmEscortMasterPO.get(0));

			responce.put("status", "success");
			log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
			return responce;
		}
		responce.put("status", "fail");
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return responce;
	}

	public Map<String, Object> employeeChangeForgotPassword(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO,
			List<EFmFmUserMasterPO> employeeDetails) throws ParseException, UnsupportedEncodingException {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		Map<String, Object> responce = new HashMap<String, Object>();
		PasswordEncryption passwordEncryption = new PasswordEncryption();
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		if (!(eFmFmSupervisorMasterPO.getNewPassword()
				.matches(ContextLoader.getContext().getMessage("strong.password", null, "password", null)))) {
			responce.put("status", "wrongPattern");
			log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
			return responce;
		}
		if (!(employeeDetails.isEmpty())) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			boolean passexist = false;
			List<EFmFmUserPasswordPO> passDetails = employeeDetailBO.getUserPasswordDetailsFromUserIdAndBranchId(
					employeeDetails.get(0).getUserId());
			if (!(passDetails.isEmpty())) {
				if (passDetails.size() > employeeDetails.get(0).geteFmFmClientBranchPO()
						.getLastPassCanNotCurrentPass()) {
					for (int i = 0; i < ((passDetails.size())
							- employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass()); i++) {
						employeeDetailBO.deleteLastPasswordForParticularEmployeCrossingDefineLimit(
								passDetails.get(i).getPasswordId());
					}
				}
				log.info("passDetails.size()" + passDetails.size());
				for (EFmFmUserPasswordPO pass : passDetails) {
					if (encoder.matches(eFmFmSupervisorMasterPO.getNewPassword(), pass.getPassword())) {
						passexist = true;
						break;
					}

				}
			}
			if (passexist) {
				if (passDetails.size() <= employeeDetails.get(0).geteFmFmClientBranchPO()
						.getLastPassCanNotCurrentPass())
					responce.put("numberOfPasswords", passDetails.size());
				else {
					responce.put("numberOfPasswords",
							employeeDetails.get(0).geteFmFmClientBranchPO().getLastPassCanNotCurrentPass());
				}
				responce.put("status", "oldpass");
				responce.put("lastChangeDateTime",
						dateFormatter.format(passDetails.get(passDetails.size() - 1).getCreationTime()));
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return responce;
			}

			if (employeeDetails.get(0).getStatus().equalsIgnoreCase("N")) {
				responce.put("status", "disable");
				log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
				return responce;
			}
			employeeDetails.get(0)
					.setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmSupervisorMasterPO.getNewPassword()));
			employeeDetails.get(0).setPasswordChangeDate(new Date());
			employeeDetails.get(0).setLastLoginTime(new Date());

			employeeDetails.get(0).setWrongPassAttempt(0);
			employeeDetails.get(0).setTempCode(null);
			employeeDetails.get(0).setTempPassWordChange(false);

			employeeDetailBO.update(employeeDetails.get(0));
			EFmFmUserPasswordPO userPassword = new EFmFmUserPasswordPO();
			EFmFmClientBranchPO clientDetail = new EFmFmClientBranchPO();
			clientDetail.setBranchId(eFmFmSupervisorMasterPO.getBranchId());
			// userPassword.setCreatedBy(createdBy);
			userPassword.setCreationTime(new Date());
			userPassword.setEfmFmUserMaster(employeeDetails.get(0));
			userPassword
					.setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmSupervisorMasterPO.getNewPassword()));
			userPassword.seteFmFmClientBranchPO(clientDetail);
			employeeDetailBO.save(userPassword);

			if (!(passDetails.isEmpty())) {
				if (passDetails.size() == employeeDetails.get(0).geteFmFmClientBranchPO()
						.getLastPassCanNotCurrentPass()) {
					employeeDetailBO.deleteLastPasswordForParticularEmployeCrossingDefineLimit(
							passDetails.get(0).getPasswordId());
				}
			}
			responce.put("status", "success");
			log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
			return responce;
		}
		responce.put("status", "fail");
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return responce;
	}

	/*
	 * Employee Logout Service
	 */

	@POST
	@Path("/logout")
	public Response employeeLogout(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) {
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IFieldAppDetailsBO iFieldAppDetailsBO = (IFieldAppDetailsBO) ContextLoader.getContext()
				.getBean("IFieldAppDetailsBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmSupervisorMasterPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		log.info("serviceStart -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		eFmFmSupervisorMasterPO.setCombinedFacility(String.valueOf(eFmFmSupervisorMasterPO.getBranchId()));
		if (eFmFmSupervisorMasterPO.getAppLoginUserType().equalsIgnoreCase("transport")) {
			List<EFmFmUserMasterPO> userMasterDetail = employeeDetailBO.getParticularEmpDetailsFromUserId(
					eFmFmSupervisorMasterPO.getUserId(), eFmFmSupervisorMasterPO.getBranchId());
			userMasterDetail.get(0).setLoggedIn(false);
			employeeDetailBO.update(userMasterDetail.get(0));
			responce.put("status", "success");
		} else if (eFmFmSupervisorMasterPO.getAppLoginUserType().equalsIgnoreCase("supervisor")) {
			List<EFmFmSupervisorMasterPO> supervisorMasterDetail = iFieldAppDetailsBO
					.getSupervisorPasswordDetailsFromSupervisorAndBranchId(eFmFmSupervisorMasterPO.getUserId(),
							new MultifacilityService().combinedBranchIdDetails(eFmFmSupervisorMasterPO.getUserId(),eFmFmSupervisorMasterPO.getCombinedFacility()));
			supervisorMasterDetail.get(0).setLoggedIn(false);
			iFieldAppDetailsBO.update(supervisorMasterDetail.get(0));
			responce.put("status", "success");
		} else if (eFmFmSupervisorMasterPO.getAppLoginUserType().equalsIgnoreCase("escort")) {
			List<EFmFmEscortMasterPO> escortMasterDetail = iVehicleCheckInBO
					.getEscortPasswordDetailsFromEscortAndBranchId(eFmFmSupervisorMasterPO.getUserId(),
							eFmFmSupervisorMasterPO.getBranchId());
			escortMasterDetail.get(0).setLoggedIn(false);
			iVehicleCheckInBO.updateEscortDetails(escortMasterDetail.get(0));
			responce.put("status", "success");
		} else {
			responce.put("status", "WRONGUSERTYPE");
		}

		// responce.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmSupervisorMasterPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Path("/addingAdhocDetails")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addingAdhocDetails(@FormDataParam("documents") List<FormDataBodyPart> documents,
			@FormDataParam("documents") FormDataContentDisposition fileDetails, FormDataMultiPart map)
			throws ParseException, IOException {
		EFmFmVehicleMasterPO eFmFmVehicleMasterPO = new EFmFmVehicleMasterPO();
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),userId))){
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		IRouteDetailBO iRouteDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		log.info("addingAdhocDetails" + eFmFmVehicleMasterPO.getActionType());
		VehicleImportExcel vehicleService = new VehicleImportExcel();
		String name = "os.name", filePath = "";
		boolean OsName = System.getProperty(name).startsWith("Windows");
		EFmFmDriverDocsPO eFmFmDriverDocsPO = new EFmFmDriverDocsPO();
		EFmFmVehicleDocsPO eFmFmVehicleDocsPO = new EFmFmVehicleDocsPO();
		String vehicleFileDetails = "", driverFileDetails = "";
		InputStream driverDocuments = null;
		InputStream vehicleDocuments = null;
		boolean vehicleExist = false;
		boolean driverExist = false;
		List<EFmFmVehicleMasterPO> vehicleExistDetail = null;
		List<EFmFmDriverMasterPO> driverExistDetail = null;
		

		Map<String, List<FormDataBodyPart>> fieldsByName = map.getFields();

		for (List<FormDataBodyPart> fields : fieldsByName.values()) {
			for (FormDataBodyPart field : fields) {
				String fileName = field.getName();
				String myString = IOUtils.toString(field.getValueAs(InputStream.class), "UTF-8");

				if (fileName.equalsIgnoreCase("firstName")) {
					eFmFmVehicleMasterPO.setFirstName(myString);
				}
				if (fileName.equalsIgnoreCase("mobileNumber")) {
					eFmFmVehicleMasterPO.setMobileNumber(myString);
				}
				if (fileName.equalsIgnoreCase("licenceNumber")) {
					eFmFmVehicleMasterPO.setLicenceNumber(myString);
				}
				if (fileName.equalsIgnoreCase("licenceValidDate")) {
					eFmFmVehicleMasterPO.setLicenceValidDate(myString);
				}
				if (fileName.equalsIgnoreCase("addDriver")) {
					eFmFmVehicleMasterPO.setAddDriver(myString);
				}
				if (fileName.equalsIgnoreCase("addVehicle")) {
					eFmFmVehicleMasterPO.setAddVehicle(myString);
				}
				if (fileName.equalsIgnoreCase("userId")) {
					eFmFmVehicleMasterPO.setUserId(Integer.valueOf(myString));
				}
				if (fileName.equalsIgnoreCase("vendorId")) {
					eFmFmVehicleMasterPO.setVendorId(Integer.valueOf(myString));
				}
				if (fileName.equalsIgnoreCase("branchId")) {
					eFmFmVehicleMasterPO.setBranchId(Integer.valueOf(myString));
				}
				if (fileName.equalsIgnoreCase("driverUploadDoc")) {
					eFmFmVehicleMasterPO.setDriverUploadDoc(myString);
				}
				if (fileName.equalsIgnoreCase("vehicleNumber")) {
					eFmFmVehicleMasterPO.setVehicleNumber(myString);
				}
				if (fileName.equalsIgnoreCase("vehicleModel")) {
					eFmFmVehicleMasterPO.setVehicleModel(myString);
				}
				if (fileName.equalsIgnoreCase("capacity")) {
					eFmFmVehicleMasterPO.setCapacity(Integer.parseInt(myString));
				}
				if (fileName.equalsIgnoreCase("polutionDate")) {
					eFmFmVehicleMasterPO.setPolutionDate(myString);
				}
				if (fileName.equalsIgnoreCase("insuranceValid")) {
					eFmFmVehicleMasterPO.setInsuranceValid(myString);
				}
				if (fileName.equalsIgnoreCase("permitValid")) {
					eFmFmVehicleMasterPO.setPermitValid(myString);
				}
				if (fileName.equalsIgnoreCase("taxValidDate")) {
					eFmFmVehicleMasterPO.setTaxValidDate(myString);
				}
				if (fileName.equalsIgnoreCase("vehicleUploadDoc")) {
					eFmFmVehicleMasterPO.setVehicleUploadDoc(myString);
				}
				if (fileName.equalsIgnoreCase("actionType")) {
					eFmFmVehicleMasterPO.setActionType(myString);
				}
				System.out.println("fileName" + fileName);
				System.out.println("values" + myString);

			}
		}	
				
		if (eFmFmVehicleMasterPO.getAddVehicle().equalsIgnoreCase("Y")) {
			BodyPartEntity vehicleBodyDocuments = (BodyPartEntity) documents.get(1).getEntity();
			vehicleFileDetails = documents.get(1).getContentDisposition().getFileName();
			System.out.println("ImageFileName" + vehicleFileDetails);
			vehicleDocuments = vehicleBodyDocuments.getInputStream();
			if (OsName) {
				filePath = SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER + vehicleFileDetails;
				File pathExist = new File(SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER);
				if (!pathExist.exists()) {
					new File(SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER).mkdir();
				}
			} else {
				filePath = SERVER_UPLOAD_LINUX_LOCATION_FOLDER + vehicleFileDetails;
				File pathExist = new File(SERVER_UPLOAD_LINUX_LOCATION_FOLDER);
				if (!pathExist.exists()) {
					new File(SERVER_UPLOAD_LINUX_LOCATION_FOLDER).mkdir();
				}
			}

			vehicleExistDetail = iVehicleCheckInBO.getVehicleDetailsFromVehicleNumberAndVendorId(
					eFmFmVehicleMasterPO.getVehicleNumber(), eFmFmVehicleMasterPO.getVendorId());
			if (!(vehicleExistDetail.isEmpty())) {
				vehicleExist = true;
				responce.put("status", "vehicleExist");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			} else {
				EFmFmVendorMasterPO eFmFmVendorMasterPO = new EFmFmVendorMasterPO();
				eFmFmVendorMasterPO.setVendorId(eFmFmVehicleMasterPO.getVendorId());

				EFmFmVehicleMasterPO vehicleMasterPO = new EFmFmVehicleMasterPO();
				vehicleMasterPO.setVehicleNumber(eFmFmVehicleMasterPO.getVehicleNumber());
				vehicleMasterPO.setRegistartionCertificateNumber(eFmFmVehicleMasterPO.getVehicleNumber());
				vehicleMasterPO.setCapacity(eFmFmVehicleMasterPO.getCapacity());
				vehicleMasterPO.setVehicleEngineNumber(eFmFmVehicleMasterPO.getVehicleNumber());
				vehicleMasterPO.setVehicleMake("adhoc");
				vehicleMasterPO.setVehicleModel(eFmFmVehicleMasterPO.getVehicleModel());
				vehicleMasterPO.setReplaceMentVehicleNum("NO");

				vehicleMasterPO.setTaxCertificateValid(dateFormat.parse(eFmFmVehicleMasterPO.getTaxValidDate()));
				vehicleMasterPO.setPermitValidDate(dateFormat.parse(eFmFmVehicleMasterPO.getPermitValid()));
				vehicleMasterPO.setPolutionValid(dateFormat.parse(eFmFmVehicleMasterPO.getPolutionDate()));
				vehicleMasterPO.setInsuranceValidDate(dateFormat.parse(eFmFmVehicleMasterPO.getInsuranceValid()));

				vehicleMasterPO.setVehicleModelYear("2016");
				vehicleMasterPO.setRemarks("adhoc");
				vehicleMasterPO.setVehicleACFitted("NO");
				vehicleMasterPO.setVehicleGPSFitted("NO");
				vehicleMasterPO.setrFIDFitted("NO");				
				List<EFmFmFixedDistanceContractDetailPO> contractDetails = iVehicleCheckInBO
						.getFixedDistanceDummyDetails(eFmFmVehicleMasterPO.getBranchId());
				vehicleMasterPO.seteFmFmContractDetails(contractDetails.get(0));
				vehicleMasterPO.setUpdatedTime(new Date());
				vehicleMasterPO.setEfmFmVendorMaster(eFmFmVendorMasterPO);
				vehicleMasterPO.setRegistrationDate(new Date());
				vehicleMasterPO.setVehicleFitnessDate(new Date());
				vehicleMasterPO.setStatus("P");

				File fileExist = new File(filePath);
				if (!fileExist.exists() && !fileExist.isDirectory()) {
					responce.put("status", "success");
					log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());							
				} else {
					responce.put("status", "vehicleDocsExist");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}

				eFmFmVehicleDocsPO.setCreationTime(new Date());
				eFmFmVehicleDocsPO.setDocumentName(eFmFmVehicleMasterPO.getVehicleUploadDoc());
				eFmFmVehicleDocsPO.setStatus("Y");
				eFmFmVehicleDocsPO.setCreatedBy(String.valueOf(eFmFmVehicleMasterPO.getUserId()));
				eFmFmVehicleDocsPO.setUploadpath(filePath);

				if (eFmFmVehicleMasterPO.getVehicleUploadDoc().equalsIgnoreCase("Insurance")) {
					vehicleMasterPO.setInsurancePath(filePath);
				} else if (eFmFmVehicleMasterPO.getVehicleUploadDoc().equalsIgnoreCase("Registration")) {
					vehicleMasterPO.setRegistartionCertificatePath(filePath);
				} else if (eFmFmVehicleMasterPO.getVehicleUploadDoc().equalsIgnoreCase("pollution")) {
					vehicleMasterPO.setPoluctionCertificatePath(filePath);
				} else {
					vehicleMasterPO.setTaxCertificatePath(filePath);
				}
				int vehicleId = iVehicleCheckInBO.saveVehicleRecordWithValue(vehicleMasterPO);
				vehicleMasterPO.setVehicleId(vehicleId);
				eFmFmVehicleDocsPO.seteFmFmVehicleMaster(vehicleMasterPO);
				iVehicleCheckInBO.addVehicleUploadDetails(eFmFmVehicleDocsPO);
				vehicleService.saveFile(vehicleDocuments, filePath);
			}

		}

		if (eFmFmVehicleMasterPO.getAddDriver().equalsIgnoreCase("Y")) {
			eFmFmVehicleMasterPO.setCombinedFacility(String.valueOf(eFmFmVehicleMasterPO.getBranchId()));
		
			String uniqueStatus=uniqueMobileNumberValidation(eFmFmVehicleMasterPO.getMobileNumber(),new MultifacilityService().combinedBranchIdDetails(eFmFmVehicleMasterPO.getUserId(),eFmFmVehicleMasterPO.getCombinedFacility()),eFmFmVehicleMasterPO.getUserId());
			if(uniqueStatus.equalsIgnoreCase("DUPMOBNO")){
				responce.put("status",uniqueStatus);
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}

			BodyPartEntity driverBodyDocuments = (BodyPartEntity) documents.get(0).getEntity();
			driverFileDetails = documents.get(0).getContentDisposition().getFileName();
			driverDocuments = driverBodyDocuments.getInputStream();

			if (OsName) {
				filePath = SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER + driverFileDetails;
				File pathExist = new File(SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER);
				if (!pathExist.exists()) {
					new File(SERVER_UPLOAD_WINDOWS_LOCATION_FOLDER).mkdir();
				}
			} else {
				filePath = SERVER_UPLOAD_LINUX_LOCATION_FOLDER + driverFileDetails;
				File pathExist = new File(SERVER_UPLOAD_LINUX_LOCATION_FOLDER);
				if (!pathExist.exists()) {
					new File(SERVER_UPLOAD_LINUX_LOCATION_FOLDER).mkdir();
				}
			}
			driverExistDetail = iVehicleCheckInBO.getDriverDetailsFromDriverNumberAndVendorId(
					eFmFmVehicleMasterPO.getMobileNumber(), eFmFmVehicleMasterPO.getVendorId());
			if (!(driverExistDetail.isEmpty())) {
				driverExist = true;
				responce.put("status", "driverExist");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			} else {
				EFmFmVendorMasterPO eFmFmVendorMasterPO = new EFmFmVendorMasterPO();
				eFmFmVendorMasterPO.setVendorId(eFmFmVehicleMasterPO.getVendorId());
				EFmFmDriverMasterPO driverMasterPO = new EFmFmDriverMasterPO();
				driverMasterPO.setFirstName(eFmFmVehicleMasterPO.getFirstName());
				driverMasterPO.setLastName("");
				driverMasterPO.setMedicalFitnessCertificateValid(new Date());
				driverMasterPO.setGender("MALE");
				driverMasterPO.setMobileNumber(eFmFmVehicleMasterPO.getMobileNumber());
				driverMasterPO.setLicenceNumber(eFmFmVehicleMasterPO.getLicenceNumber());
				driverMasterPO.setAddress("");
				driverMasterPO.setPermanentAddress("");
				driverMasterPO.setBatchNumber("adhoc");
				driverMasterPO.setBatchDate(new Date());
				driverMasterPO.setBadgeValidity(new Date());
				driverMasterPO.setDateOfJoining(new Date());
				driverMasterPO.setDdtValidDate(new Date());
				driverMasterPO.setDob(new Date());
				driverMasterPO.setPoliceVerification("notdone");
				driverMasterPO.setStatus("P");
				driverMasterPO.setEfmFmVendorMaster(eFmFmVendorMasterPO);

				File fileExist = new File(filePath);
				if (!fileExist.exists() && !fileExist.isDirectory()) {
					responce.put("status", "success");
					log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());					
				} else {
					responce.put("status", "driverDocsExist");
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}

				if (eFmFmVehicleMasterPO.getDriverUploadDoc().equalsIgnoreCase("License")) {
					driverMasterPO.setLicenceDocPath(filePath);
				} else if (eFmFmVehicleMasterPO.getDriverUploadDoc().equalsIgnoreCase("medical")) {
					driverMasterPO.setMedicalDocPath(filePath);
				} else if (eFmFmVehicleMasterPO.getDriverUploadDoc().equalsIgnoreCase("profilePic")) {
					driverMasterPO.setProfilePicPath(filePath);
				}
				eFmFmDriverDocsPO.setStatus("Y");
				eFmFmDriverDocsPO.setCreationTime(new Date());
				eFmFmDriverDocsPO.setUploadpath(filePath);
				eFmFmDriverDocsPO.setCreatedBy(String.valueOf(eFmFmVehicleMasterPO.getUserId()));
				eFmFmDriverDocsPO.setDocumentName(eFmFmVehicleMasterPO.getDriverUploadDoc());
				try {
					driverMasterPO.setLicenceValid(dateFormat.parse(eFmFmVehicleMasterPO.getLicenceValidDate()));
				} catch (Exception e) {
					driverMasterPO.setLicenceValid(new Date());
					log.info("Error" + e);
				}
				driverMasterPO.setPoliceVerificationValid(new Date());
				int driverId = iRouteDetailBO.saveDriverRecordWithValue(driverMasterPO);
				System.out.println("driverId"+driverId);
				driverMasterPO.setDriverId(driverId);
				eFmFmDriverDocsPO.setEfmFmDriverMaster(driverMasterPO);
				approvalBO.addUploadDetails(eFmFmDriverDocsPO);
				vehicleService.saveFile(driverDocuments, filePath);
			}

		}
		if (driverExist && vehicleExist && !(eFmFmVehicleMasterPO.getActionType().equalsIgnoreCase("withoutDevice"))) {
			responce.put("status", "bothExists");
			log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		} else {
			if (eFmFmVehicleMasterPO.getAddVehicle().equalsIgnoreCase("Y")) {
				if (eFmFmVehicleMasterPO.getActionType().equalsIgnoreCase("withoutDevice")) {
					vehicleExistDetail = iVehicleCheckInBO.getVehicleDetailsFromVehicleNumberAndVendorId(
							eFmFmVehicleMasterPO.getVehicleNumber(), eFmFmVehicleMasterPO.getVendorId());
					driverExistDetail = iVehicleCheckInBO.getDriverDetailsFromDriverNumberAndVendorId(
							eFmFmVehicleMasterPO.getMobileNumber(), eFmFmVehicleMasterPO.getVendorId());
					List<EFmFmVehicleCheckInPO> checkInVehicle = iVehicleCheckInBO.getParticularCheckedInVehicles(
							eFmFmVehicleMasterPO.getCombinedFacility(), vehicleExistDetail.get(0).getVehicleId());
					System.out.println("checkInVehicle" + checkInVehicle.size());
					if (!(checkInVehicle.isEmpty())) {
						responce.put("status", "VcheckedIn");
						log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
					List<EFmFmVehicleCheckInPO> checkInDriver = iVehicleCheckInBO
							.getParticularDriverCheckedInDetails(driverExistDetail.get(0).getDriverId());
					if (!(checkInDriver.isEmpty())) {
						responce.put("status", "DcheckedIn");
						log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}
					if (driverExistDetail.get(0).getEfmFmVendorMaster().getVendorId() != vehicleExistDetail.get(0)
							.getEfmFmVendorMaster().getVendorId()) {
						responce.put("status", "mismatch");
						log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
						return Response.ok(responce, MediaType.APPLICATION_JSON).build();
					}

					EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
					EFmFmVehicleMasterPO vehicleMaster = new EFmFmVehicleMasterPO();
					vehicleMaster.setVehicleId(vehicleExistDetail.get(0).getVehicleId());

					EFmFmDriverMasterPO driverMaster = new EFmFmDriverMasterPO();
					driverMaster.setDriverId(driverExistDetail.get(0).getDriverId());

					eFmFmVehicleCheckInPO.setCheckInTime(new Date());
					eFmFmVehicleCheckInPO.setReadFlg("Y");
					eFmFmVehicleCheckInPO.setAdminMailTriggerStatus(false);
					eFmFmVehicleCheckInPO.setSupervisorMailTriggerStatus(false);
					eFmFmVehicleCheckInPO.setEfmFmVehicleMaster(vehicleMaster);
					eFmFmVehicleCheckInPO.setEfmFmDriverMaster(driverMaster);

					EFmFmDeviceMasterPO deviceMaster = new EFmFmDeviceMasterPO();
					deviceMaster.setDeviceId(1);
					eFmFmVehicleCheckInPO.seteFmFmDeviceMaster(deviceMaster);
					eFmFmVehicleCheckInPO.setStatus("Y");
					iVehicleCheckInBO.vehicleDriverCheckIn(eFmFmVehicleCheckInPO);
					responce.put("status", "checkedIn");
					log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
					return Response.ok(responce, MediaType.APPLICATION_JSON).build();
				}
			}
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}
	
	/*
	 * Vehicle and driver checkedIn by System with dummy device and Actual
	 * device.
	 * 
	 */

	@POST
	@Path("/vehicleDriverCheckIn")
	public Response vehicleAndDriverCheckedIn(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) throws ParseException {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		log.info("Adhoc vehicleDriverCheckIn");
		 log.info("serviceStart -UserId :" + eFmFmVehicleMasterPO.getUserId());
		 IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		 try{
//			 if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleMasterPO.getUserId()))){
//
//		 		responce.put("status", "invalidRequest");
//		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		 	}}catch(Exception e){
//		 		log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//		 	}

		List<EFmFmVehicleMasterPO> vehicleExistDetail = iVehicleCheckInBO
				.getVehicleDetailsFromVehicleNumberAndVendorId(eFmFmVehicleMasterPO.getVehicleNumber(),
						eFmFmVehicleMasterPO.getVendorId());
		if (!(vehicleExistDetail.isEmpty())) {
			if (vehicleExistDetail.get(0).getStatus().equalsIgnoreCase("P")) {
				responce.put("status", "vehicleNotApproved");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			} else if (vehicleExistDetail.get(0).getStatus().equalsIgnoreCase("R")) {
				responce.put("status", "vehicleRejected");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		}
		else{
			responce.put("status", "vehicleNotRegister");
			log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmDriverMasterPO> driverExistDetail = iVehicleCheckInBO.getDriverDetailsFromDriverNumberAndVendorId(
				eFmFmVehicleMasterPO.getMobileNumber(), eFmFmVehicleMasterPO.getVendorId());
		if (!(driverExistDetail.isEmpty())) {
			if (driverExistDetail.get(0).getStatus().equalsIgnoreCase("P")) {
				responce.put("status", "driverNotApproved");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			} else if (driverExistDetail.get(0).getStatus().equalsIgnoreCase("R")) {
				responce.put("status", "driverRejected");
				log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		}else{
			responce.put("status", "driverNotRegister");
			log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
        if (driverExistDetail.get(0).getEfmFmVendorMaster().getVendorId() != vehicleExistDetail.get(0).getEfmFmVendorMaster()
                .getVendorId()) {
            responce.put("status", "mismatch");
            log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        
        List<EFmFmVehicleCheckInPO> checkInVehicle = iVehicleCheckInBO.getParticularCheckedInVehicles(eFmFmVehicleMasterPO.getCombinedFacility(), vehicleExistDetail.get(0).getVehicleId());
        System.out.println("checkInVehicle"+checkInVehicle.size());
        if (!(checkInVehicle.isEmpty())) {
            responce.put("status", "VcheckedIn");
            log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        List<EFmFmVehicleCheckInPO> checkInDriver = iVehicleCheckInBO
                .getParticularDriverCheckedInDetails(driverExistDetail.get(0).getDriverId());
        if (!(checkInDriver.isEmpty())) {
            responce.put("status", "DcheckedIn");
            log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
            return Response.ok(responce, MediaType.APPLICATION_JSON).build();
        }
        EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
        EFmFmVehicleMasterPO vehicleMaster = new EFmFmVehicleMasterPO();
        vehicleMaster.setVehicleId(vehicleExistDetail.get(0).getVehicleId());     
        EFmFmDriverMasterPO driverMaster = new EFmFmDriverMasterPO();
        driverMaster.setDriverId(driverExistDetail.get(0).getDriverId());      
        eFmFmVehicleCheckInPO.setCheckInTime(new Date());
        eFmFmVehicleCheckInPO.setReadFlg("Y");
        eFmFmVehicleCheckInPO.setAdminMailTriggerStatus(false);
        eFmFmVehicleCheckInPO.setSupervisorMailTriggerStatus(false);		        
        eFmFmVehicleCheckInPO.setEfmFmVehicleMaster(vehicleMaster);
        eFmFmVehicleCheckInPO.setEfmFmDriverMaster(driverMaster);		        
       log.info("eFmFmVehicleMasterPO.getDeviceId()"+eFmFmVehicleMasterPO.getDeviceId());
        EFmFmDeviceMasterPO deviceMaster = new EFmFmDeviceMasterPO();
        if(eFmFmVehicleMasterPO.getDeviceId().equalsIgnoreCase("NO Device")){
	        deviceMaster.setDeviceId(1);
	        eFmFmVehicleCheckInPO.setCheckInType("NoDevice");
        }
       else if(eFmFmVehicleMasterPO.getDeviceId().equalsIgnoreCase("GPS Device")){
            deviceMaster.setDeviceId(1);
            eFmFmVehicleCheckInPO.setCheckInType("GPSDevice");
            }
        else{
        	//Code for checkIng about device
        	try{
        	 List<EFmFmVehicleCheckInPO> checkInDevice = iVehicleCheckInBO
                     .getParticularDeviceCheckedInDetails(Integer.parseInt(eFmFmVehicleMasterPO.getDeviceId()));
             if (!(checkInDevice.isEmpty())) {
                 responce.put("status", "deviceCheckedIn");
                 log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
                 return Response.ok(responce, MediaType.APPLICATION_JSON).build();
             }
        	}catch(Exception e){
        		log.info("deviceCheckedIn error"+e);
        	}
            deviceMaster.setDeviceId(Integer.parseInt(eFmFmVehicleMasterPO.getDeviceId()));
            eFmFmVehicleCheckInPO.setCheckInType("mobile");
        }
        eFmFmVehicleCheckInPO.seteFmFmDeviceMaster(deviceMaster);
        eFmFmVehicleCheckInPO.setStatus("Y");
        
        iVehicleCheckInBO.vehicleDriverCheckIn(eFmFmVehicleCheckInPO);
        responce.put("status", "success");
        log.info("serviceEnd -UserId :" + eFmFmVehicleMasterPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}   
	

	@POST
	@Path("/driverCheckIn")
	public Response driverCheckIn(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader
				.getContext().getBean("IVehicleCheckInBO");
		log.info("serviceStart -UserId :" + eFmFmVehicleCheckInPO.getUserId());
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmVehicleCheckInPO.getUserId()))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//
//			}
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("status", "alreadyExist");		
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
		response.put("status", "success");		
		 log.info("serviceEnd -UserId :" + eFmFmVehicleCheckInPO.getUserId());
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}




	public long getDisableTime(int hours, int minutes, Date checkIndate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkIndate);
		calendar.add(Calendar.HOUR, hours);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTimeInMillis();
	}

	public long passwordChangeDate(Date pastchangePassDate, int numDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pastchangePassDate);
		calendar.add(Calendar.DAY_OF_YEAR, numDays);
		return calendar.getTimeInMillis();
	}

}