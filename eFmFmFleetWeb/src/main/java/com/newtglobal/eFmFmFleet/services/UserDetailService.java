package com.newtglobal.eFmFmFleet.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IEmployeeDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.ISessionManagementBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.CalculateDistance;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.eFmFmFleet.PasswordEncryption;
import com.newtglobal.eFmFmFleet.eFmFmFleet.Validator;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaNodalMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchConfigurationMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchConfigurationPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchSubConfigurationPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientRouteMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientUserRolePO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeRequestMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmIndicationMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRoleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmSessionManagementPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripTimingMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserPasswordPO;
import com.newtglobal.eFmFmFleet.model.EFmFmZoneMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/user")
@Consumes("application/json")
@Produces("application/json")
public class UserDetailService {
	private static Log log = LogFactory.getLog(UserDetailService.class);

	@Context
	private HttpServletRequest httpRequest;
	JwtTokenGenerator token = new JwtTokenGenerator();

	/**
	 * 
	 * @param Particular
	 *            login user details
	 * @return it will returns all the logged in user details
	 * @throws UnsupportedEncodingException
	 * 
	 */

	@POST
	@Path("/loginuser")
	public Response loggedInUserDetails(EFmFmUserMasterPO eFmFmUserMaster) throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		Map<String, Object> responce = new HashMap<String, Object>();
		System.out.println("Profile not coming");
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmUserMaster.getUserId());
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMaster.getUserId()))){
		//
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmUserMaster.getUserId());
		// if (!(userDetail.isEmpty())) {
		// String jwtToken = "";
		// try {
		// JwtTokenGenerator token = new JwtTokenGenerator();
		// jwtToken = token.generateToken();
		// userDetail.get(0).setAuthorizationToken(jwtToken);
		// userDetail.get(0).setTokenGenerationTime(new Date());
		// userMasterBO.update(userDetail.get(0));
		// } catch (Exception e) {
		// log.info("error" + e);
		// }
		// }
		//
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }

		Map<String, Object> userDetail = new HashMap<String, Object>();
		List<EFmFmUserMasterPO> loggedInUserDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMaster.getUserId());
		List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
		if (!(loggedInUserDetail.isEmpty())) {
			userDetail.put("address",
					new String(Base64.getDecoder().decode(loggedInUserDetail.get(0).getAddress()), "utf-8"));
			userDetail.put("userName", loggedInUserDetail.get(0).getUserName());
			userDetail.put("firstName",
					new String(Base64.getDecoder().decode(loggedInUserDetail.get(0).getFirstName()), "utf-8"));
			log.info("EmpId" + loggedInUserDetail.get(0).getEmployeeId());
			try {
				userDetail.put("lastName",
						new String(Base64.getDecoder().decode(loggedInUserDetail.get(0).getLastName()), "utf-8"));
			} catch (Exception e) {
				log.info("error" + e);
			}
			userDetail.put("designation", new String(
					Base64.getDecoder().decode(loggedInUserDetail.get(0).getEmployeeDesignation()), "utf-8"));
			userDetail.put("number",
					new String(Base64.getDecoder().decode(loggedInUserDetail.get(0).getMobileNumber()), "utf-8"));
			try {
				userDetail.put("interest",
						new String(Base64.getDecoder().decode(loggedInUserDetail.get(0).getEmployeeDomain()), "utf-8"));
			} catch (Exception e) {
				userDetail.put("interest", "");

				log.info("error" + e);
			}
			try {
				userDetail.put("country",
						new String(Base64.getDecoder().decode(loggedInUserDetail.get(0).getCountry()), "utf-8"));
			} catch (Exception e) {
				userDetail.put("country", "");
				log.info("Error" + e);
			}
			userDetail.put("mobileNumber",
					new String(Base64.getDecoder().decode(loggedInUserDetail.get(0).getMobileNumber()), "utf-8"));
			userDetail.put("emailId",
					new String(Base64.getDecoder().decode(loggedInUserDetail.get(0).getEmailId()), "utf-8"));
			userDetail.put("weekOffs", loggedInUserDetail.get(0).getWeekOffDays());

			userDetail.put("clientName", loggedInUserDetail.get(0).geteFmFmClientBranchPO().getBranchName());
			requests.add(userDetail);
		}
		log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param Update
	 *            Particular user pickup and drop address with latitude
	 *            longitude and distance
	 * 
	 */

	@POST
	@Path("/updateuseraddress")
	public Response updateAddressAndLatiLongi(EFmFmUserMasterPO eFmFmUserMaster) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -getEmployeeId :" + eFmFmUserMaster.getEmployeeId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					Integer.parseInt(eFmFmUserMaster.getEmployeeId())))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(Integer.parseInt(eFmFmUserMaster.getEmployeeId()));
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		Map<String, Object> userDetail = new HashMap<String, Object>();
		List<EFmFmUserMasterPO> loggedInUserDetail = userMasterBO
				.getLoggedInUserDetailFromClientIdAndUserId(eFmFmUserMaster);
		log.info("Geo Code Updation" + eFmFmUserMaster.getLatitudeLongitude());
		userDetail.put("status", "success");
		if (!(loggedInUserDetail.isEmpty())) {
			List<EFmFmClientBranchPO> clientBranchDetails = userMasterBO
					.getClientDetails(String.valueOf(eFmFmUserMaster.geteFmFmClientBranchPO().getBranchId()));
			CalculateDistance empDistance = new CalculateDistance();
			try {
				loggedInUserDetail.get(0).setLatitudeLongitude(eFmFmUserMaster.getLatitudeLongitude());
				loggedInUserDetail.get(0).setDistance(empDistance.employeeDistanceCalculation(
						clientBranchDetails.get(0).getLatitudeLongitude(), eFmFmUserMaster.getLatitudeLongitude()));
				userMasterBO.update(loggedInUserDetail.get(0));
			} catch (Exception e) {
				userDetail.put("status", "fail");
			}

		}
		log.info("serviceEnd -getEmployeeId :" + eFmFmUserMaster.getEmployeeId());
		return Response.ok(userDetail, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * Uodate the login User profile details
	 * 
	 * 
	 */
	@POST
	@Path("/updateprofile")
	public Response updateUserProfile(EFmFmUserMasterPO eFmFmUserMaster)
			throws ParseException, UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmUserMaster.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmUserMaster.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMaster.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		// Client Input Code ***
		Map<String, Object> request = new HashMap<String, Object>();
		String status = "Input";
		StringBuffer temp = new StringBuffer("PleaseCheck::");
		if (true) {

			if (eFmFmUserMaster.getUserName() == null || eFmFmUserMaster.getUserName().equals("")) {
				temp.append("::UserName cannot be empty");
				status = "Fail";
			} else {
				CharSequence userName = eFmFmUserMaster.getUserName();
				Matcher name = Validator.alphaNumeric(userName);
				if (!name.matches()) {
					temp.append("::please enter only alphabet or number for username");
					status = "Fail";
				}
			}
			// firstname@@@@@@@@@@@@@@@@@@@@@@
			if (eFmFmUserMaster.getFirstName() == null || eFmFmUserMaster.getFirstName().equals("")) {
				temp.append("::FirstName cannot be empty");
				status = "Fail";
			} else {
				CharSequence FirstName = eFmFmUserMaster.getFirstName();
				Matcher name = Validator.alphaSpace(FirstName);
				if (!name.matches()) {
					temp.append("::please enter only alphabet and space for firstname");
					status = "Fail";
				}

			}
			// lastname@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
			if (eFmFmUserMaster.getLastName() == null || eFmFmUserMaster.getLastName().equals("")) {
				temp.append("::LastName cannot be empty");
				status = "Fail";
			} else {
				CharSequence LastName = eFmFmUserMaster.getLastName();
				Matcher name = Validator.alpha(LastName);
				if (!name.matches()) {
					temp.append("::please enter only alphabet for lastname");
					status = "Fail";
				}

			}
			// mobile@@@@@@@@@@@@@@@@@@@@@@
			if (eFmFmUserMaster.getMobileNumber() == null || eFmFmUserMaster.getMobileNumber().equals("")) {
				temp.append("::MobileNumber cannot be empty");
				status = "Fail";
			} else {
				CharSequence MobileNumber = eFmFmUserMaster.getMobileNumber();
				Matcher name = Validator.mobNumber(MobileNumber);
				if (!name.matches()) {
					temp.append("::please enter only number length between 6-18 for mobile number");
					status = "Fail";
				}

			}
			// email@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@22
			if (eFmFmUserMaster.getEmailId() == null || eFmFmUserMaster.getEmailId().equals("")) {
				temp.append("::EmailId cannot be empty");
				status = "Fail";
			} else {
				CharSequence EmailId = eFmFmUserMaster.getEmailId();
				Matcher mail = Validator.email(EmailId);
				if (!mail.matches()) {
					temp.append("::please enter valid email");
					status = "Fail";
				}

			}
			// designation@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
			if (eFmFmUserMaster.getEmployeeDesignation() == null
					|| eFmFmUserMaster.getEmployeeDesignation().equals("")) {
				temp.append("::EmployeeDesignation cannot be empty");
				status = "Fail";
			} else {
				CharSequence Designation = eFmFmUserMaster.getEmployeeDesignation();
				Matcher match = Validator.alphaSpace(Designation);
				if (!match.matches()) {
					temp.append("::please enter only character or space for designation");
					status = "Fail";
				}

			}
			// department
			// if(eFmFmUserMaster.getEmployeeDepartment()==null||eFmFmUserMaster.getEmployeeDepartment().equals("")){
			// temp.append("::EmployeeDepartment cannot be empty");
			// status="Fail";
			// }
			// else{
			// CharSequence Department =
			// eFmFmUserMaster.getEmployeeDepartment();
			// Matcher match= Validator.alphaSpace(Department);
			// if(!match.matches())
			// {temp.append("::please enter only character or space for
			// Department");
			// status="Fail";
			// }
			//
			// }
			// domain@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//			if (eFmFmUserMaster.getEmployeeDomain() == null || eFmFmUserMaster.getEmployeeDomain().equals("")) {
//				temp.append("::EmployeeDomain cannot be empty");
//				status = "Fail";
//			} else {
//				CharSequence EmployeeDomain = eFmFmUserMaster.getEmployeeDomain();
//				Matcher match = Validator.alphaSpace(EmployeeDomain);
//				if (!match.matches()) {
//					temp.append("::please enter only character or space for employeeDomain");
//
//					status = "Fail";
//				}
//			}

			if (status.equals("Fail")) {
				log.info("Invalid input:");
				request.put("status", status);
				request.put("invalidInput", temp);
				return Response.ok(request, MediaType.APPLICATION_JSON).build();
			}
		}
		log.info("valid input:");
		List<EFmFmUserMasterPO> loggedInUserDetail = userMasterBO
				.getLoggedInUserDetailFromClientIdAndUserId(eFmFmUserMaster);
		if (!(loggedInUserDetail.isEmpty())) {
			loggedInUserDetail.get(0).setUserName(eFmFmUserMaster.getUserName());
			loggedInUserDetail.get(0)
					.setFirstName(Base64.getEncoder().encodeToString(eFmFmUserMaster.getFirstName().getBytes("utf-8")));
			loggedInUserDetail.get(0)
					.setLastName(Base64.getEncoder().encodeToString(eFmFmUserMaster.getLastName().getBytes("utf-8")));
			loggedInUserDetail.get(0).setMobileNumber(
					Base64.getEncoder().encodeToString(eFmFmUserMaster.getMobileNumber().getBytes("utf-8")));
			loggedInUserDetail.get(0)
					.setEmailId(Base64.getEncoder().encodeToString(eFmFmUserMaster.getEmailId().getBytes("utf-8")));
			loggedInUserDetail.get(0).setEmployeeDesignation(
					Base64.getEncoder().encodeToString(eFmFmUserMaster.getEmployeeDesignation().getBytes("utf-8")));
			try{
			loggedInUserDetail.get(0)
					.setCountry(Base64.getEncoder().encodeToString(eFmFmUserMaster.getCountry().getBytes("utf-8")));
			}catch(Exception e){
				log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
				log.info("error" + e);
			}
			loggedInUserDetail.get(0).setEmployeeDomain(
					Base64.getEncoder().encodeToString(eFmFmUserMaster.getEmployeeDomain().getBytes("utf-8")));
			userMasterBO.update(loggedInUserDetail.get(0));
		}
		request.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * change login user password from device
	 * 
	 * 
	 */
	@POST
	@Path("/changeuserpassword")
	public Response changeLoggedInUserPassword(EFmFmUserMasterPO eFmFmUserMaster) throws NoSuchAlgorithmException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		ISessionManagementBO iSessionManagementBO = (ISessionManagementBO) ContextLoader.getContext()
				.getBean("ISessionManagementBO");

		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmUserMaster.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmUserMaster.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMaster.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

		PasswordEncryption passwordEncryption = new PasswordEncryption();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		log.info("password" + eFmFmUserMaster.getNewPassword());
		if (!(eFmFmUserMaster.getNewPassword()
				.matches(ContextLoader.getContext().getMessage("strong.password", null, "password", null)))) {
			responce.put("status", "wrongPattern");
			log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmUserMasterPO> employeeDetails = userMasterBO
				.getLoggedInUserDetailFromClientIdAndUserId(eFmFmUserMaster);

		if (encoder.matches(eFmFmUserMaster.getPassword(), employeeDetails.get(0).getPassword().trim())) {
			boolean passexist = false;
			List<EFmFmUserPasswordPO> passDetails = employeeDetailBO
					.getUserPasswordDetailsFromUserIdAndBranchId(employeeDetails.get(0).getUserId());
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
					if (encoder.matches(eFmFmUserMaster.getNewPassword(), pass.getPassword())) {
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
				log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			employeeDetails.get(0)
					.setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmUserMaster.getNewPassword()));
			employeeDetails.get(0).setPasswordChangeDate(new Date());
			employeeDetails.get(0).setLastLoginTime(new Date());
			employeeDetails.get(0).setTempPassWordChange(false);
			employeeDetailBO.update(employeeDetails.get(0));
			EFmFmUserPasswordPO userPassword = new EFmFmUserPasswordPO();
			EFmFmClientBranchPO clientDetail = new EFmFmClientBranchPO();
			clientDetail.setBranchId(eFmFmUserMaster.geteFmFmClientBranchPO().getBranchId());
			// userPassword.setCreatedBy(createdBy);
			userPassword.setCreationTime(new Date());
			userPassword.setEfmFmUserMaster(employeeDetails.get(0));
			userPassword.setPassword(passwordEncryption.PasswordEncoderGenerator(eFmFmUserMaster.getNewPassword()));
			userPassword.seteFmFmClientBranchPO(clientDetail);
			employeeDetailBO.save(userPassword);

			if (!(passDetails.isEmpty())) {
				if (passDetails.size() == employeeDetails.get(0).geteFmFmClientBranchPO()
						.getLastPassCanNotCurrentPass()) {
					employeeDetailBO.deleteLastPasswordForParticularEmployeCrossingDefineLimit(
							passDetails.get(0).getPasswordId());
				}
			}

			try {

				if (!(employeeDetails.isEmpty())) {
					List<EFmFmSessionManagementPO> sessionDetail = iSessionManagementBO
							.OnPasswordChangeInvalidateAllTheSessions(employeeDetails.get(0).getUserId());
					for (EFmFmSessionManagementPO session : sessionDetail) {
						session.setSessionActiveStatus("N");
						session.setSessionEndTime(new Date());
						iSessionManagementBO.update(session);
					}
				}
			} catch (Exception e) {
				log.info("Error :-" + e);

			}

			responce.put("status", "success");
			log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}
		responce.put("status", "wrong");
		log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param eFmFmUserMaster
	 *            get all the users of the particular client
	 * @return all the users and the client type
	 * @throws UnsupportedEncodingException
	 */
	@POST
	@Path("/allusers")
	public Response getAllUsers(EFmFmUserMasterPO eFmFmUserMaster) throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info(eFmFmUserMaster.getCombinedFacility() + "serviceStart -UserId :"
				+ eFmFmUserMaster.geteFmFmClientBranchPO().getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmUserMaster.geteFmFmClientBranchPO().getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmUserMaster.geteFmFmClientBranchPO().getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmUserMasterPO> clientUserDetails = userMasterBO.getUsersFromClientId(eFmFmUserMaster);
		log.info(eFmFmUserMaster.getCombinedFacility() + "clientUserDetails" + clientUserDetails.size());
		List<Map<String, Object>> allusers = new ArrayList<Map<String, Object>>();
		Map<String, Object> allUserDetail = new HashMap<String, Object>();
		if (!(clientUserDetails.isEmpty())) {
			for (EFmFmUserMasterPO users : clientUserDetails) {
				Map<String, Object> requests = new HashMap<String, Object>();
				// This query will give you all the users in your master table
				// including newt super adimn details
				// log.info("hh
				// UserId"+users.getUserId()+"branchId"+users.geteFmFmClientBranchPO().getBranchId());
				List<EFmFmClientUserRolePO> roleMaster = userMasterBO
						.getUserRolesFromUserIdAndBranchId(users.getUserId());
				if (!(roleMaster.isEmpty())) {
					requests.put("userRole", roleMaster.get(0).getEfmFmRoleMaster().getRole());
				}
				requests.put("userId", users.getUserId());
				requests.put("employeeId", users.getEmployeeId());
				requests.put("facilityName", users.geteFmFmClientBranchPO().getBranchName());

				requests.put("emailId", new String(Base64.getDecoder().decode(users.getEmailId()), "utf-8"));
				requests.put("userName", users.getUserName());
				requests.put("employeeName", new String(Base64.getDecoder().decode(users.getFirstName()), "utf-8"));
				requests.put("mobileNumber", new String(Base64.getDecoder().decode(users.getMobileNumber()), "utf-8"));
				allusers.add(requests);
			}
		}
		allUserDetail.put("users", allusers);
		log.info("serviceEnd -UserId :" + eFmFmUserMaster.geteFmFmClientBranchPO().getUserId());
		return Response.ok(allUserDetail, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * getting list of Project Details
	 */

	@POST
	@Path("/listOfProjectId")
	public Response listOfProjectId(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO)
			throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		/*
		 * log.info("Logged In User IP Adress"
		 * +token.getClientIpAddr(httpRequest)); try{
		 * if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader(
		 * "authenticationToken")))){ responce.put("status", "invalidRequest");
		 * return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 * }}catch(Exception e){ log.info("authenticationToken error"+e); }
		 */
		log.info("serviceStart -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
		List<EFmFmEmployeeProjectDetailsPO> userProjectDetails = userMasterBO.getListOfProjectId(
				eFmFmEmployeeProjectDetailsPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
				eFmFmEmployeeProjectDetailsPO.getUserId());
		List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>>();
		if (!(userProjectDetails.isEmpty())) {
			for (EFmFmEmployeeProjectDetailsPO projectDetails : userProjectDetails) {
				Map<String, Object> listOfProject = new HashMap<String, Object>();
				listOfProject.put("projectId", projectDetails.geteFmFmClientProjectDetails().getProjectId());
				listOfProject.put("ClientprojectId",
						projectDetails.geteFmFmClientProjectDetails().getClientProjectId());
				listOfProject.put("projectName",
						projectDetails.geteFmFmClientProjectDetails().getEmployeeProjectName());
				listOfProject.put("repUserId", projectDetails.getReportingManagerUserId());
				projectList.add(listOfProject);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
		return Response.ok(projectList, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/listOfProjectIdByClient")
	public Response listOfProjectIdByClient(EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO)
			throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		/*
		 * log.info("Logged In User IP Adress"
		 * +token.getClientIpAddr(httpRequest)); try{
		 * if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader(
		 * "authenticationToken")))){ responce.put("status", "invalidRequest");
		 * return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 * }}catch(Exception e){ log.info("authenticationToken error"+e); }
		 */
		log.info("serviceStart -UserId :" + eFmFmClientProjectDetailsPO.getUserId());
		List<EFmFmClientProjectDetailsPO> userProjectDetails = userMasterBO
				.getListOfProjectIdByAdhoc(eFmFmClientProjectDetailsPO.geteFmFmClientBranchPO().getBranchId());
		List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>>();
		if (!(userProjectDetails.isEmpty())) {
			for (EFmFmClientProjectDetailsPO projectDetails : userProjectDetails) {
				Map<String, Object> listOfProject = new HashMap<String, Object>();
				listOfProject.put("projectId", projectDetails.getProjectId());
				listOfProject.put("ClientprojectId", projectDetails.getClientProjectId());
				listOfProject.put("projectName", projectDetails.getEmployeeProjectName());
				projectList.add(listOfProject);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmClientProjectDetailsPO.getUserId());
		return Response.ok(projectList, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/listReportingMngByProjectId")
	public Response listReportingMngByProjectId(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO)
			throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		/*
		 * log.info("Logged In User IP Adress"
		 * +token.getClientIpAddr(httpRequest)); try{
		 * if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader(
		 * "authenticationToken")))){ responce.put("status", "invalidRequest");
		 * return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 * }}catch(Exception e){ log.info("authenticationToken error"+e); }
		 */

		log.info("serviceStart -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
		List<EFmFmEmployeeProjectDetailsPO> repManagerDetails = userMasterBO.getListOfRepMngByProjectId(
				eFmFmEmployeeProjectDetailsPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
				eFmFmEmployeeProjectDetailsPO.geteFmFmClientProjectDetails().getProjectId());
		List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>>();
		if (!(repManagerDetails.isEmpty())) {
			for (EFmFmEmployeeProjectDetailsPO projectDetails : repManagerDetails) {
				Map<String, Object> listOfRepMng = new HashMap<String, Object>();
				listOfRepMng.put("repUserId", projectDetails.getReportingManagerUserId());
				List<EFmFmUserMasterPO> empDetails = employeeDetailBO.getParticularEmpDetailsFromUserId(
						Integer.parseInt(projectDetails.getReportingManagerUserId().trim()),
						eFmFmEmployeeProjectDetailsPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
				if (!empDetails.isEmpty()) {
					listOfRepMng.put("repEmployeeId", empDetails.get(0).getEmployeeId() + " | "
							+ new String(Base64.getDecoder().decode(empDetails.get(0).getFirstName()), "utf-8"));
				} else {
					listOfRepMng.put("repEmployeeId", "NA");
				}
				projectList.add(listOfRepMng);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmEmployeeProjectDetailsPO.getUserId());
		return Response.ok(projectList, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param eFmFmUserMaster
	 *            save application setting of the client
	 * @return application setting saved by client
	 */

	@POST
	@Path("/updatebranch")
	public Response saveApplicationSetting(EFmFmClientBranchPO clientDetails) throws ParseException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + clientDetails.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					clientDetails.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(clientDetails.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		Map<String, Object> requests = new HashMap<String, Object>();
        log.info("clientDetails.getCombinedFacility()"+clientDetails.getCombinedFacility());
		StringTokenizer stringTokenizer = new StringTokenizer(clientDetails.getCombinedFacility(), ",");
		String branchId = "";
		while (stringTokenizer.hasMoreElements()) {
			branchId = (String) stringTokenizer.nextElement();
			List<EFmFmClientBranchPO> particularBranchDetails = userMasterBO.getBranchConfigurationDetailsFromBranchId(Integer.parseInt(branchId));

			DateFormat timeformate = new SimpleDateFormat("HH:mm");
			DateFormat dateTimeFormate = new SimpleDateFormat("dd-MM-yyyy");
			// Date escortWinStartTime =
			// timeformate.parse(clientDetails.getEscortStartTimePickup());
			// Date escortWinEndTime =
			// timeformate.parse(clientDetails.getEscortEndTimePickup());
			Date escortWinStartTime = timeformate.parse("00:00");
			Date escortWinEndTime = timeformate.parse("00:00");
			Date escortStartTimeDrop = timeformate.parse("00:00");
			Date escortEndTimeDrop = timeformate.parse("00:00");
			escortWinStartTime = timeformate.parse(clientDetails.getEscortStartTimeForPickup());
			escortWinEndTime = timeformate.parse(clientDetails.getEscortEndTimeForPickup());
			escortStartTimeDrop = timeformate.parse(clientDetails.getEscortStartTimeForDrop());
			escortEndTimeDrop = timeformate.parse(clientDetails.getEscortEndTimeForDrop());

			Date otpDisableTime = timeformate.parse(clientDetails.getOtpDisableTime());

			java.sql.Time oTpTime = new java.sql.Time(otpDisableTime.getTime());

			Date tripDelayTime = timeformate.parse(clientDetails.getTripDelayTime());

			java.sql.Time delayTime = new java.sql.Time(tripDelayTime.getTime());

			Date driverWaitingTimeAtLast = timeformate.parse(clientDetails.getDriverLastLocWaitingTime());
			Date b2bByTravelTime = timeformate.parse(clientDetails.getTravelTimeFromDropToFirstPickUp());

			Date pickUpNotification = timeformate.parse(clientDetails.getNotificationCutoffTimePickup());
			Date dropNotification = timeformate.parse(clientDetails.getNotificationCutoffTimeDrop());

			Date pickupCutOffTime = timeformate.parse(clientDetails.getPickupCutOffTime());
			Date dropCutOffTime = timeformate.parse(clientDetails.getDropCutOffTime());
			Date pickupDropReq = timeformate.parse(clientDetails.getDropPickupReqCutOfTime());

			Date pickupCancel = timeformate.parse(clientDetails.getPickupCancelCutOffTime());
			Date dropCancel = timeformate.parse(clientDetails.getDropCancelCutOffTime());

			Date driverAutoCheckOut = timeformate.parse(clientDetails.getDriverAutoCheckOut());

			Date reschedulePickupTime = timeformate.parse(clientDetails.getReschedulePickupTime());

			Date rescheduleDropTime = timeformate.parse(clientDetails.getRescheduleDropTime());

			java.sql.Time pickupCutOffValue = new java.sql.Time(pickupCutOffTime.getTime());
			java.sql.Time dropCutOffValue = new java.sql.Time(dropCutOffTime.getTime());
			java.sql.Time pickupDropReqValue = new java.sql.Time(pickupDropReq.getTime());

			java.sql.Time pickupCancelCutOffValue = new java.sql.Time(pickupCancel.getTime());
			java.sql.Time dropCancelCutOffValue = new java.sql.Time(dropCancel.getTime());

			java.sql.Time driverAutoCheckOutValue = new java.sql.Time(driverAutoCheckOut.getTime());
			java.sql.Time reschedulePickupCutOffTime = new java.sql.Time(reschedulePickupTime.getTime());
			java.sql.Time rescheduleDropCutOffTime = new java.sql.Time(rescheduleDropTime.getTime());

			// B2b Setting
			java.sql.Time driverWaitingTime = new java.sql.Time(driverWaitingTimeAtLast.getTime());
			java.sql.Time b2bByTravelTimeIn = new java.sql.Time(b2bByTravelTime.getTime());

			// Before routing Setting
			java.sql.Time pickUpShiftNotification = new java.sql.Time(pickUpNotification.getTime());
			java.sql.Time dropShiftNotification = new java.sql.Time(dropNotification.getTime());

			// escort Setting
			java.sql.Time escortAllocationStartTime = new java.sql.Time(escortWinStartTime.getTime());
			java.sql.Time escortAllocationEndTime = new java.sql.Time(escortWinEndTime.getTime());
			java.sql.Time escortAllocationStartTimeDrop = new java.sql.Time(escortStartTimeDrop.getTime());
			java.sql.Time escortAllocationEndTimeDrop = new java.sql.Time(escortEndTimeDrop.getTime());

			List<Integer> userDetails = userMasterBO.getAdminUserRoleByBranchId(Integer.parseInt(branchId));
			if (!(userDetails.isEmpty())) {
				if (userDetails.size() > clientDetails.getNumberOfAdministarator()) {
					requests.put("numberOfAdmin", userDetails.size());
					requests.put("status", "fail");
					log.info("serviceEnd -UserId :" + clientDetails.getUserId());
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();

				}

			}

			log.info("shiftSelectType" + clientDetails.getShiftSelectType());
			if ((clientDetails.getShiftSelectType()).equalsIgnoreCase("ShiftWise")) {
				ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
				DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
				String shiftDate = clientDetails.getTime();
				Date shift = shiftFormate.parse(shiftDate);
				java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
				List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO
						.getShiftTimeDetailFromShiftTimeAndTripType(branchId, shiftTime, clientDetails.getTripType());
				log.info("shiftTimeDetails" + shiftTimeDetails.size());
				if (!(shiftTimeDetails.isEmpty())) {
					shiftTimeDetails.get(0).setAreaGeoFenceRegion(clientDetails.getAreaGeoFenceRegion());
					shiftTimeDetails.get(0).setClusterGeoFenceRegion(clientDetails.getClusterGeoFenceRegion());
					shiftTimeDetails.get(0).setRouteGeoFenceRegion(clientDetails.getRouteGeoFenceRegion());
					iCabRequestBO.update(shiftTimeDetails.get(0));
				}

			}

			particularBranchDetails.get(0).setEscortRequired(clientDetails.getEscortRequired());
			particularBranchDetails.get(0).setMangerApprovalRequired(clientDetails.getMangerApprovalRequired());
			particularBranchDetails.get(0).setEtaSMS(clientDetails.getEtaSMS());
			particularBranchDetails.get(0)
					.setEmployeeAddressgeoFenceArea(clientDetails.getEmployeeAddressgeoFenceArea());
			particularBranchDetails.get(0)
					.setShiftTimePlusOneHourrAfterSMSContact(clientDetails.getShiftTimePlusOneHourrAfterSMSContact());
			particularBranchDetails.get(0)
					.setShiftTimePlusTwoHourrAfterSMSContact(clientDetails.getShiftTimePlusTwoHourrAfterSMSContact());
			particularBranchDetails.get(0).setMaxSpeed(clientDetails.getMaxSpeed());
			particularBranchDetails.get(0).setDriverAutoCheckedoutTime(driverAutoCheckOutValue);
			particularBranchDetails.get(0).setDelayMessageTime(clientDetails.getDelayMessageTime());
			particularBranchDetails.get(0).setEmployeeWaitingTime(clientDetails.getEmployeeWaitingTime());
			particularBranchDetails.get(0).setEmergencyContactNumber(clientDetails.getEmergencyContactNumber());
			particularBranchDetails.get(0)
					.setMaxTravelTimeEmployeeWiseInMin(clientDetails.getMaxTravelTimeEmployeeWiseInMin());
			particularBranchDetails.get(0)
					.setMaxRadialDistanceEmployeeWiseInKm(clientDetails.getMaxRadialDistanceEmployeeWiseInKm());
			particularBranchDetails.get(0).setMaxRouteLengthInKm(clientDetails.getMaxRouteLengthInKm());
			particularBranchDetails.get(0).setMaxRouteDeviationInKm(clientDetails.getMaxRouteDeviationInKm());
			particularBranchDetails.get(0)
					.setTransportContactNumberForMsg(clientDetails.getTransportContactNumberForMsg());
			particularBranchDetails.get(0).setAutoClustering(clientDetails.getAutoClustering());
			particularBranchDetails.get(0)
					.setStartTripGeoFenceAreaInMeter(clientDetails.getStartTripGeoFenceAreaInMeter());
			particularBranchDetails.get(0).setEndTripGeoFenceAreaInMeter(clientDetails.getEndTripGeoFenceAreaInMeter());
			particularBranchDetails.get(0).setFeedBackEmailId(clientDetails.getFeedBackEmailId());
			particularBranchDetails.get(0).setClusterSize(clientDetails.getClusterSize());
			particularBranchDetails.get(0).setEmpDeviceDriverImage(clientDetails.getEmpDeviceDriverImage());
			particularBranchDetails.get(0)
					.setEmpDeviceDriverMobileNumber(clientDetails.getEmpDeviceDriverMobileNumber());
			particularBranchDetails.get(0).setEmpDeviceDriverName(clientDetails.getEmpDeviceDriverName());
			particularBranchDetails.get(0)
					.setDriverDeviceDriverProfilePicture(clientDetails.getDriverDeviceDriverProfilePicture());
			particularBranchDetails.get(0).setDriverDeviceAutoCallAndsms(clientDetails.getDriverDeviceAutoCallAndsms());
			particularBranchDetails.get(0).setTripType(clientDetails.getTripType());
			particularBranchDetails.get(0).setNumberOfAdministarator(clientDetails.getNumberOfAdministarator());
			particularBranchDetails.get(0)
					.setInactiveAdminAccountAfterNumOfDays(clientDetails.getInactiveAdminAccountAfterNumOfDays());

			particularBranchDetails.get(0)
					.setPasswordResetPeriodForAdminInDays(clientDetails.getPasswordResetPeriodForAdminInDays());
			particularBranchDetails.get(0)
					.setPasswordResetPeriodForUserInDays(clientDetails.getPasswordResetPeriodForUserInDays());
			particularBranchDetails.get(0)
					.setTwoFactorAuthenticationRequired(clientDetails.getTwoFactorAuthenticationRequired());
			particularBranchDetails.get(0).setNumberOfAttemptsWrongPass(clientDetails.getNumberOfAttemptsWrongPass());
			particularBranchDetails.get(0).setSessionTimeoutInMinutes(clientDetails.getSessionTimeoutInMinutes());
			particularBranchDetails.get(0).setInvoiceNumberDigitRange(clientDetails.getInvoiceNumberDigitRange());
			particularBranchDetails.get(0).setAdhocTimePickerForEmployee(clientDetails.getAdhocTimePickerForEmployee());
			/*
			 * java.sql.Time pickupCutOffValue = new
			 * java.sql.Time(pickupCutOffTime.getTime()); java.sql.Time
			 * dropCutOffValue = new java.sql.Time(dropCutOffTime.getTime());
			 * java.sql.Time pickupDropReqValue = new
			 * java.sql.Time(pickupDropReq.getTime());
			 */
			/*
			 * particularBranchDetails.get(0).setDropPriorTimePeriod(
			 * clientDetails. getDropPriorTimePeriod());
			 * particularBranchDetails.get(0).setPickupPriorTimePeriod(
			 * clientDetails .getPickupPriorTimePeriod());
			 */
			particularBranchDetails.get(0).setDropPriorTimePeriod(dropCutOffValue);
			particularBranchDetails.get(0).setPickupPriorTimePeriod(pickupCutOffValue);

			particularBranchDetails.get(0).setDropCancelTimePeriod(dropCancelCutOffValue);
			particularBranchDetails.get(0).setPickupCancelTimePeriod(pickupCancelCutOffValue);
			particularBranchDetails.get(0)
					.setEmployeeSecondPickUpRequest(clientDetails.getEmployeeSecondPickUpRequest());
			particularBranchDetails.get(0).setEmployeeSecondDropRequest(clientDetails.getEmployeeSecondDropRequest());
			particularBranchDetails.get(0).setPanicAlertNeeded(clientDetails.getPanicAlertNeeded());
			particularBranchDetails.get(0).setLastPassCanNotCurrentPass(clientDetails.getLastPassCanNotCurrentPass());
			particularBranchDetails.get(0).setAutoDropRoster(clientDetails.getAutoDropRoster());
			/*
			 * particularBranchDetails.get(0).setShiftTimeDiffPickToDrop(
			 * clientDetails.getShiftTimeDiffPickToDrop());
			 */
			particularBranchDetails.get(0).setShiftTimeDiffPickToDrop(pickupDropReqValue);
			particularBranchDetails.get(0).setCutOffTime(clientDetails.getCutOffTime());
			particularBranchDetails.get(0)
					.setAddingGeoFenceDistanceIntrip(clientDetails.getAddingGeoFenceDistanceIntrip());
			particularBranchDetails.get(0).setInvoiceGenDate(clientDetails.getInvoiceGenDate());
			particularBranchDetails.get(0).setInvoiceNoOfDWorkingDays(clientDetails.getInvoiceNoOfDWorkingDays());
			particularBranchDetails.get(0).setReschedulePickupCutOffTime(reschedulePickupCutOffTime);
			particularBranchDetails.get(0).setRescheduleDropCutOffTime(rescheduleDropCutOffTime);
			particularBranchDetails.get(0).setDistanceFlg(clientDetails.getDistanceFlg());
			particularBranchDetails.get(0)
					.setAutoVehicleAllocationStatus(clientDetails.getAutoVehicleAllocationStatus());
			particularBranchDetails.get(0).setDriverAutoCheckoutStatus(clientDetails.getDriverAutoCheckoutStatus());
			Date requestCutoffDate = dateTimeFormate.parse(clientDetails.getRequestToDateCutOff());
			particularBranchDetails.get(0).setRequestCutOffDate(requestCutoffDate);
			Date requestCutoffFromDate = dateTimeFormate.parse(clientDetails.getRequestFromDateCutOff());
			particularBranchDetails.get(0).setRequestCutOffFromDate(requestCutoffFromDate);
			particularBranchDetails.get(0).setRequestCutOffNoOfDays(clientDetails.getRequestCutOffNoOfDays());
			particularBranchDetails.get(0).setRequestType(clientDetails.getRequestType());
			particularBranchDetails.get(0).setEarlyRequestDate(clientDetails.getEarlyRequestDate());
			particularBranchDetails.get(0).setMonthOrDays(clientDetails.getMonthOrDays());
			particularBranchDetails.get(0).setOccurrenceFlg(clientDetails.getOccurrenceFlg());
			particularBranchDetails.get(0).setDaysRequest(clientDetails.getDaysRequest());
			// B2b
			particularBranchDetails.get(0).setDriverWaitingTimeAtLastLocation(driverWaitingTime);
			particularBranchDetails.get(0).setB2bByTravelTime(b2bByTravelTimeIn);

			particularBranchDetails.get(0).setNotificationCutoffTimeForPickup(pickUpShiftNotification);
			particularBranchDetails.get(0).setNotificationCutoffTimeForDrop(dropShiftNotification);
			particularBranchDetails.get(0).setPersonalDeviceViaSms(clientDetails.getPersonalDeviceViaSms());
			particularBranchDetails.get(0).setAssignRoutesToVendor(clientDetails.getAssignRoutesToVendor());
			particularBranchDetails.get(0).setDriverCallToEmployee(clientDetails.getDriverCallToEmployee());
			particularBranchDetails.get(0).setDriverCallToTransportDesk(clientDetails.getDriverCallToTransportDesk());
			particularBranchDetails.get(0).setEmployeeCallToDriver(clientDetails.getEmployeeCallToDriver());
			particularBranchDetails.get(0).setEmployeeCallToTransport(clientDetails.getEmployeeCallToTransport());

			particularBranchDetails.get(0).setB2bByTravelDistanceInKM(clientDetails.getB2bByTravelDistanceInKM());
			particularBranchDetails.get(0).setSelectedB2bType(clientDetails.getSelectedB2bType());
			particularBranchDetails.get(0).setLocationVisible(clientDetails.getLocationVisible());
			particularBranchDetails.get(0).setInvoiceGenType(clientDetails.getInvoiceGenType());
			particularBranchDetails.get(0).setDestinationPointLimit(clientDetails.getDestinationPointLimit());

			// escort Constraints
			if (clientDetails.getEscortStartTimeForPickup() != null) {
				particularBranchDetails.get(0).setEscortStartTimePickup(escortAllocationStartTime);
				particularBranchDetails.get(0).setEscortEndTimePickup(escortAllocationEndTime);
			}
			if (clientDetails.getEscortStartTimeForDrop() != null) {
				particularBranchDetails.get(0).setEscortStartTimeDrop(escortAllocationStartTimeDrop);
				particularBranchDetails.get(0).setEscortEndTimeDrop(escortAllocationEndTimeDrop);
			}
			particularBranchDetails.get(0).setEscortTimeWindowEnable(clientDetails.getEscortTimeWindowEnable());

			// Driver Vehicle
			try {
				if (!(clientDetails.getLicenseExpiryDay() >= 0
						&& clientDetails.getLicenseExpiryDay() >= clientDetails.getLicenseRepeatAlertsEvery())) {
					requests.put("licenseExpiry",
							"RepeatAlert Should not lessthan licenseExpiryDay & lessthan Zero Value not allowed");
					requests.put("status", "fail");
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				} else if (clientDetails.getLicenceNotificationType().equalsIgnoreCase("email")) {
					if (clientDetails.getLicenseEmailId() == null || clientDetails.getLicenseEmailId().isEmpty()
							|| clientDetails.getLicenseEmailId() == "") {
						requests.put("licenseExpiry", "Please check the License Emaild Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				} else if (clientDetails.getLicenceNotificationType().equalsIgnoreCase("sms")) {
					if (clientDetails.getLicenseSMSNumber() == null || clientDetails.getLicenseSMSNumber().isEmpty()
							|| clientDetails.getLicenseSMSNumber() == "") {
						requests.put("licenseExpiry", "Please check the License SMS Number");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}

				} else if (clientDetails.getLicenceNotificationType().equalsIgnoreCase("both")) {
					if (clientDetails.getLicenseEmailId() == null || clientDetails.getLicenseEmailId().isEmpty()
							|| clientDetails.getLicenseEmailId() == "" || clientDetails.getLicenseSMSNumber() == null
							|| clientDetails.getLicenseSMSNumber().isEmpty()
							|| clientDetails.getLicenseSMSNumber() == "") {
						requests.put("licenseExpiry", "Please check the License SMS Number /Email Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				}

			} catch (Exception e) {
				requests.put("licenseExpiry", "Empty values are not allowed incase of  selected Email /sms/both");
				requests.put("status", "fail");
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}

			particularBranchDetails.get(0).setLicenseExpiryDay(clientDetails.getLicenseExpiryDay());
			particularBranchDetails.get(0).setLicenseRepeatAlertsEvery(clientDetails.getLicenseRepeatAlertsEvery());
			particularBranchDetails.get(0).setLicenceNotificationType(clientDetails.getLicenceNotificationType());
			particularBranchDetails.get(0).setLicenseSMSNumber(clientDetails.getLicenseSMSNumber());
			particularBranchDetails.get(0).setLicenseEmailId(clientDetails.getLicenseEmailId());

			try {
				if (!(clientDetails.getMedicalFitnessExpiryDay() >= 0 && clientDetails
						.getMedicalFitnessExpiryDay() >= clientDetails.getMedicalFitnessRepeatAlertsEvery())) {
					requests.put("medicalFitnessExpiry",
							"RepeatAlert Should not lessthan MedicalFitnessExpiryDay & lessthan Zero Value not allowed");
					requests.put("status", "fail");
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				} else if (clientDetails.getMedicalFitnessNotificationType().equalsIgnoreCase("email")) {
					if (clientDetails.getMedicalFitnessEmailId() == null
							|| clientDetails.getMedicalFitnessEmailId().isEmpty()
							|| clientDetails.getMedicalFitnessEmailId() == "") {
						requests.put("medicalFitnessExpiry", "Please check the MedicalFitness Emaild Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				} else if (clientDetails.getMedicalFitnessNotificationType().equalsIgnoreCase("sms")) {
					if (clientDetails.getMedicalFitnessSMSNumber() == null
							|| clientDetails.getMedicalFitnessSMSNumber().isEmpty()
							|| clientDetails.getMedicalFitnessSMSNumber() == "") {
						requests.put("medicalFitnessExpiry", "Please check the MedicalFitness SMS Number");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}

				} else if (clientDetails.getMedicalFitnessNotificationType().equalsIgnoreCase("both")) {
					if (clientDetails.getMedicalFitnessEmailId() == null
							|| clientDetails.getMedicalFitnessEmailId().isEmpty()
							|| clientDetails.getMedicalFitnessEmailId() == ""
							|| clientDetails.getMedicalFitnessSMSNumber() == null
							|| clientDetails.getMedicalFitnessSMSNumber().isEmpty()
							|| clientDetails.getMedicalFitnessSMSNumber() == "") {
						requests.put("medicalFitnessExpiry", "Please check the MedicalFitness SMS Number /Email Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				}

			} catch (Exception e) {
				requests.put("medicalFitnessExpiry",
						"Empty values are not allowed incase of  selected Email /sms/both");
				requests.put("status", "fail");
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}

			particularBranchDetails.get(0).setMedicalFitnessExpiryDay(clientDetails.getMedicalFitnessExpiryDay());
			particularBranchDetails.get(0)
					.setMedicalFitnessRepeatAlertsEvery(clientDetails.getMedicalFitnessRepeatAlertsEvery());
			particularBranchDetails.get(0)
					.setMedicalFitnessNotificationType(clientDetails.getMedicalFitnessNotificationType());
			particularBranchDetails.get(0).setMedicalFitnessSMSNumber(clientDetails.getMedicalFitnessSMSNumber());
			particularBranchDetails.get(0).setMedicalFitnessEmailId(clientDetails.getMedicalFitnessEmailId());

			try {
				if (!(clientDetails.getPoliceVerificationExpiryDay() >= 0 && clientDetails
						.getPoliceVerificationExpiryDay() >= clientDetails.getPoliceVerificationRepeatAlertsEvery())) {
					requests.put("policeVerificationExpiry",
							"RepeatAlert Should not lessthan policeVerificationExpiryDay & lessthan Zero Value not allowed");
					requests.put("status", "fail");
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				} else if (clientDetails.getPoliceVerificationNotificationType().equalsIgnoreCase("email")) {
					if (clientDetails.getPoliceVerificationEmailId() == null
							|| clientDetails.getPoliceVerificationEmailId().isEmpty()
							|| clientDetails.getPoliceVerificationEmailId() == "") {
						requests.put("policeVerificationExpiry", "Please check the policeVerification Emaild Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				} else if (clientDetails.getPoliceVerificationNotificationType().equalsIgnoreCase("sms")) {
					if (clientDetails.getPoliceVerificationSMSNumber() == null
							|| clientDetails.getPoliceVerificationSMSNumber().isEmpty()
							|| clientDetails.getPoliceVerificationSMSNumber() == "") {
						requests.put("policeVerificationExpiry", "Please check the policeVerification SMS Number");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}

				} else if (clientDetails.getPoliceVerificationNotificationType().equalsIgnoreCase("both")) {
					if (clientDetails.getPoliceVerificationEmailId() == null
							|| clientDetails.getPoliceVerificationEmailId().isEmpty()
							|| clientDetails.getPoliceVerificationEmailId() == ""
							|| clientDetails.getPoliceVerificationSMSNumber() == null
							|| clientDetails.getPoliceVerificationSMSNumber().isEmpty()
							|| clientDetails.getPoliceVerificationSMSNumber() == "") {
						requests.put("policeVerificationExpiry",
								"Please check the policeVerification SMS Number /Email Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				}

			} catch (Exception e) {
				requests.put("policeVerificationExpiry",
						"Empty values are not allowed incase of  selected Email /sms/both");
				requests.put("status", "fail");
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}

			particularBranchDetails.get(0)
					.setPoliceVerificationExpiryDay(clientDetails.getPoliceVerificationExpiryDay());
			particularBranchDetails.get(0)
					.setPoliceVerificationRepeatAlertsEvery(clientDetails.getPoliceVerificationRepeatAlertsEvery());
			particularBranchDetails.get(0)
					.setPoliceVerificationNotificationType(clientDetails.getPoliceVerificationNotificationType());
			particularBranchDetails.get(0)
					.setPoliceVerificationSMSNumber(clientDetails.getPoliceVerificationSMSNumber());
			particularBranchDetails.get(0).setPoliceVerificationEmailId(clientDetails.getPoliceVerificationEmailId());

			try {
				if (!(clientDetails.getDdTrainingExpiryDay() >= 0
						&& clientDetails.getDdTrainingExpiryDay() >= clientDetails.getDdTrainingRepeatAlertsEvery())) {
					requests.put("ddTrainingExpiry",
							"RepeatAlert Should not lessthan DdTrainingExpiryDay & lessthan Zero Value not allowed");
					requests.put("status", "fail");
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				} else if (clientDetails.getDdTrainingNotificationType().equalsIgnoreCase("email")) {
					if (clientDetails.getDdTrainingEmailId() == null || clientDetails.getDdTrainingEmailId().isEmpty()
							|| clientDetails.getDdTrainingEmailId() == "") {
						requests.put("ddTrainingExpiry", "Please check the ddTraining Emaild Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				} else if (clientDetails.getDdTrainingNotificationType().equalsIgnoreCase("sms")) {
					if (clientDetails.getDdTrainingSMSNumber() == null
							|| clientDetails.getDdTrainingSMSNumber().isEmpty()
							|| clientDetails.getDdTrainingSMSNumber() == "") {
						requests.put("ddTrainingExpiry", "Please check the ddTraining SMS Number");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}

				} else if (clientDetails.getDdTrainingNotificationType().equalsIgnoreCase("both")) {
					if (clientDetails.getDdTrainingEmailId() == null || clientDetails.getDdTrainingEmailId().isEmpty()
							|| clientDetails.getDdTrainingEmailId() == ""
							|| clientDetails.getDdTrainingSMSNumber() == null
							|| clientDetails.getDdTrainingSMSNumber().isEmpty()
							|| clientDetails.getDdTrainingSMSNumber() == "") {
						requests.put("ddTrainingExpiry", "Please check the ddTraining SMS Number /Email Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				}

			} catch (Exception e) {
				requests.put("ddTrainingExpiry", "Empty values are not allowed incase of  selected Email /sms/both");
				requests.put("status", "fail");
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}
			particularBranchDetails.get(0).setDdTrainingExpiryDay(clientDetails.getDdTrainingExpiryDay());
			particularBranchDetails.get(0)
					.setDdTrainingRepeatAlertsEvery(clientDetails.getDdTrainingRepeatAlertsEvery());
			particularBranchDetails.get(0).setDdTrainingNotificationType(clientDetails.getDdTrainingNotificationType());
			particularBranchDetails.get(0).setDdTrainingSMSNumber(clientDetails.getDdTrainingSMSNumber());
			particularBranchDetails.get(0).setDdTrainingEmailId(clientDetails.getDdTrainingSMSNumber());
			try {
				if (!(clientDetails.getPollutionDueExpiryDay() >= 0 && clientDetails
						.getPollutionDueExpiryDay() >= clientDetails.getPollutionDueRepeatAlertsEvery())) {
					requests.put("pollutionExpiry",
							"Repeat alert Should not less than pollution ExpiryDay & less than Zero Value not allowed");
					requests.put("status", "fail");
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				} else if (clientDetails.getPollutionDueNotificationType().equalsIgnoreCase("email")) {
					if (clientDetails.getPollutionDueEmailId() == null
							|| clientDetails.getPollutionDueEmailId().isEmpty()
							|| clientDetails.getPollutionDueEmailId() == "") {
						requests.put("pollutionExpiry", "Please check the pollution Emaild Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				} else if (clientDetails.getPollutionDueNotificationType().equalsIgnoreCase("sms")) {
					if (clientDetails.getPollutionDueSMSNumber() == null
							|| clientDetails.getPollutionDueSMSNumber().isEmpty()
							|| clientDetails.getPollutionDueSMSNumber() == "") {
						requests.put("pollutionExpiry", "Please check the pollution SMS Number");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}

				} else if (clientDetails.getPollutionDueNotificationType().equalsIgnoreCase("both")) {
					if (clientDetails.getPollutionDueEmailId() == null
							|| clientDetails.getPollutionDueEmailId().isEmpty()
							|| clientDetails.getPollutionDueEmailId() == ""
							|| clientDetails.getPollutionDueSMSNumber() == null
							|| clientDetails.getPollutionDueSMSNumber().isEmpty()
							|| clientDetails.getPollutionDueSMSNumber() == "") {
						requests.put("pollutionExpiry", "Please check the pollution SMS Number /Email Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				}

			} catch (Exception e) {
				requests.put("pollutionExpiry", "Empty values are not allowed incase of  selected Email /sms/both");
				requests.put("status", "fail");
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}

			particularBranchDetails.get(0).setPollutionDueExpiryDay(clientDetails.getPollutionDueExpiryDay());
			particularBranchDetails.get(0)
					.setPollutionDueRepeatAlertsEvery(clientDetails.getPollutionDueRepeatAlertsEvery());
			particularBranchDetails.get(0)
					.setPollutionDueNotificationType(clientDetails.getPoliceVerificationNotificationType());
			particularBranchDetails.get(0).setPollutionDueSMSNumber(clientDetails.getPollutionDueSMSNumber());
			particularBranchDetails.get(0).setPollutionDueEmailId(clientDetails.getPollutionDueEmailId());

			try {
				if (!(clientDetails.getInsuranceDueExpiryDay() >= 0 && clientDetails
						.getInsuranceDueExpiryDay() >= clientDetails.getInsuranceDueRepeatAlertsEvery())) {
					requests.put("insuranceExpiry",
							"RepeatAlert Should not lessthan InsuranceExpiryDay & lessthan Zero Value not allowed");
					requests.put("status", "fail");
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				} else if (clientDetails.getInsuranceDueNotificationType().equalsIgnoreCase("email")) {
					if (clientDetails.getInsuranceDueEmailId() == null
							|| clientDetails.getInsuranceDueEmailId().isEmpty()
							|| clientDetails.getInsuranceDueEmailId() == "") {
						requests.put("insuranceExpiry", "Please check the insurance Emaild Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				} else if (clientDetails.getInsuranceDueNotificationType().equalsIgnoreCase("sms")) {
					if (clientDetails.getInsuranceDueSMSNumber() == null
							|| clientDetails.getInsuranceDueSMSNumber().isEmpty()
							|| clientDetails.getInsuranceDueSMSNumber() == "") {
						requests.put("insuranceExpiry", "Please check the insurance SMS Number");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}

				} else if (clientDetails.getInsuranceDueNotificationType().equalsIgnoreCase("both")) {
					if (clientDetails.getInsuranceDueEmailId() == null
							|| clientDetails.getInsuranceDueEmailId().isEmpty()
							|| clientDetails.getInsuranceDueEmailId() == ""
							|| clientDetails.getInsuranceDueSMSNumber() == null
							|| clientDetails.getInsuranceDueSMSNumber().isEmpty()
							|| clientDetails.getInsuranceDueSMSNumber() == "") {
						requests.put("insuranceExpiry", "Please check the insurance SMS Number /Email Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				}

			} catch (Exception e) {
				requests.put("insuranceExpiry", "Empty values are not allowed incase of  selected Email /sms/both");
				requests.put("status", "fail");
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}

			particularBranchDetails.get(0).setInsuranceDueExpiryDay(clientDetails.getInsuranceDueExpiryDay());
			particularBranchDetails.get(0)
					.setInsuranceDueRepeatAlertsEvery(clientDetails.getInsuranceDueRepeatAlertsEvery());
			particularBranchDetails.get(0)
					.setInsuranceDueNotificationType(clientDetails.getInsuranceDueNotificationType());
			particularBranchDetails.get(0).setInsuranceDueSMSNumber(clientDetails.getInsuranceDueSMSNumber());
			particularBranchDetails.get(0).setInsuranceDueEmailId(clientDetails.getInsuranceDueEmailId());

			try {
				if (!(clientDetails.getTaxCertificateExpiryDay() >= 0 && clientDetails
						.getTaxCertificateExpiryDay() >= clientDetails.getTaxCertificateRepeatAlertsEvery())) {
					requests.put("taxCertificateExpiry",
							"RepeatAlert Should not lessthan taxCertificateExpiryDay & lessthan Zero Value not allowed");
					requests.put("status", "fail");
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				} else if (clientDetails.getTaxCertificateNotificationType().equalsIgnoreCase("email")) {
					if (clientDetails.getTaxCertificateEmailId() == null
							|| clientDetails.getTaxCertificateEmailId().isEmpty()
							|| clientDetails.getTaxCertificateEmailId() == "") {
						requests.put("taxCertificateExpiry", "Please check the taxCertificate Emaild Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				} else if (clientDetails.getTaxCertificateNotificationType().equalsIgnoreCase("sms")) {
					if (clientDetails.getTaxCertificateSMSNumber() == null
							|| clientDetails.getTaxCertificateSMSNumber().isEmpty()
							|| clientDetails.getTaxCertificateSMSNumber() == "") {
						requests.put("taxCertificateExpiry", "Please check the taxCertificate SMS Number");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}

				} else if (clientDetails.getTaxCertificateNotificationType().equalsIgnoreCase("both")) {
					if (clientDetails.getTaxCertificateEmailId() == null
							|| clientDetails.getTaxCertificateEmailId().isEmpty()
							|| clientDetails.getTaxCertificateEmailId() == ""
							|| clientDetails.getTaxCertificateSMSNumber() == null
							|| clientDetails.getTaxCertificateSMSNumber().isEmpty()
							|| clientDetails.getTaxCertificateSMSNumber() == "") {
						requests.put("taxCertificateExpiry", "Please check the taxCertificate SMS Number /Email Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				}

			} catch (Exception e) {
				requests.put("taxCertificateExpiry",
						"Empty values are not allowed incase of  selected Email /sms/both");
				requests.put("status", "fail");
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}

			particularBranchDetails.get(0).setTaxCertificateExpiryDay(clientDetails.getTaxCertificateExpiryDay());
			particularBranchDetails.get(0)
					.setTaxCertificateRepeatAlertsEvery(clientDetails.getTaxCertificateRepeatAlertsEvery());
			particularBranchDetails.get(0)
					.setTaxCertificateNotificationType(clientDetails.getTaxCertificateNotificationType());
			particularBranchDetails.get(0).setTaxCertificateSMSNumber(clientDetails.getTaxCertificateSMSNumber());
			particularBranchDetails.get(0).setTaxCertificateEmailId(clientDetails.getTaxCertificateEmailId());

			try {
				if (!(clientDetails.getPermitDueExpiryDay() >= 0
						&& clientDetails.getPermitDueExpiryDay() >= clientDetails.getPermitDueRepeatAlertsEvery())) {
					requests.put("permitExpiry",
							"RepeatAlert Should not lessthan permitExpiryDay & lessthan Zero Value not allowed");
					requests.put("status", "fail");
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				} else if (clientDetails.getPermitDueNotificationType().equalsIgnoreCase("email")) {
					if (clientDetails.getPermitDueEmailId() == null || clientDetails.getPermitDueEmailId().isEmpty()
							|| clientDetails.getPermitDueEmailId() == "") {
						requests.put("permitExpiry", "Please check the permit Emaild Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				} else if (clientDetails.getPermitDueNotificationType().equalsIgnoreCase("sms")) {
					if (clientDetails.getPermitDueSMSNumber() == null || clientDetails.getPermitDueSMSNumber().isEmpty()
							|| clientDetails.getPermitDueSMSNumber() == "") {
						requests.put("permitExpiry", "Please check the permit SMS Number");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}

				} else if (clientDetails.getPermitDueNotificationType().equalsIgnoreCase("both")) {
					if (clientDetails.getPermitDueEmailId() == null || clientDetails.getPermitDueEmailId().isEmpty()
							|| clientDetails.getPermitDueEmailId() == ""
							|| clientDetails.getPermitDueSMSNumber() == null
							|| clientDetails.getPermitDueSMSNumber().isEmpty()
							|| clientDetails.getPermitDueSMSNumber() == "") {
						requests.put("permitExpiry", "Please check the permit SMS Number /Email Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				}

			} catch (Exception e) {
				requests.put("permitExpiry", "Empty values are not allowed incase of  selected Email /sms/both");
				requests.put("status", "fail");
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}

			particularBranchDetails.get(0).setPermitDueExpiryDay(clientDetails.getPermitDueExpiryDay());
			particularBranchDetails.get(0).setPermitDueRepeatAlertsEvery(clientDetails.getPermitDueRepeatAlertsEvery());
			particularBranchDetails.get(0).setPermitDueNotificationType(clientDetails.getPermitDueNotificationType());
			particularBranchDetails.get(0).setPermitDueSMSNumber(clientDetails.getPermitDueSMSNumber());
			particularBranchDetails.get(0).setPermitDueEmailId(clientDetails.getPermitDueEmailId());

			try {
				if (!(clientDetails.getVehicelMaintenanceExpiryDay() >= 0 && clientDetails
						.getVehicelMaintenanceExpiryDay() >= clientDetails.getVehicelMaintenanceRepeatAlertsEvery())) {
					requests.put("vehicelMaintenanceExpiry",
							"RepeatAlert Should not lessthan VehicelMaintenanceExpiryDay & lessthan Zero Value not allowed");
					requests.put("status", "fail");
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				} else if (clientDetails.getVehicelMaintenanceNotificationType().equalsIgnoreCase("email")) {
					if (clientDetails.getVehicelMaintenanceEmailId() == null
							|| clientDetails.getVehicelMaintenanceEmailId().isEmpty()
							|| clientDetails.getVehicelMaintenanceEmailId() == "") {
						requests.put("vehicelMaintenanceExpiry", "Please check the VehicelMaintenance Emaild Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				} else if (clientDetails.getVehicelMaintenanceNotificationType().equalsIgnoreCase("sms")) {
					if (clientDetails.getVehicelMaintenanceSMSNumber() == null
							|| clientDetails.getVehicelMaintenanceSMSNumber().isEmpty()
							|| clientDetails.getVehicelMaintenanceSMSNumber() == "") {
						requests.put("vehicelMaintenanceExpiry", "Please check the VehicelMaintenance SMS Number");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}

				} else if (clientDetails.getVehicelMaintenanceNotificationType().equalsIgnoreCase("both")) {
					if (clientDetails.getVehicelMaintenanceEmailId() == null
							|| clientDetails.getVehicelMaintenanceEmailId().isEmpty()
							|| clientDetails.getVehicelMaintenanceEmailId() == ""
							|| clientDetails.getVehicelMaintenanceSMSNumber() == null
							|| clientDetails.getVehicelMaintenanceSMSNumber().isEmpty()
							|| clientDetails.getVehicelMaintenanceSMSNumber() == "") {
						requests.put("vehicelMaintenanceExpiry",
								"Please check the VehicelMaintenance SMS Number /Email Id");
						requests.put("status", "fail");
						return Response.ok(requests, MediaType.APPLICATION_JSON).build();
					}
				}

			} catch (Exception e) {
				requests.put("vehicelMaintenanceExpiry",
						"Empty values are not allowed incase of  selected Email /sms/both");
				requests.put("status", "fail");
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}

			particularBranchDetails.get(0)
					.setVehicelMaintenanceExpiryDay(clientDetails.getVehicelMaintenanceExpiryDay());
			particularBranchDetails.get(0)
					.setVehicelMaintenanceRepeatAlertsEvery(clientDetails.getVehicelMaintenanceRepeatAlertsEvery());
			particularBranchDetails.get(0)
					.setVehicelMaintenanceNotificationType(clientDetails.getVehicelMaintenanceNotificationType());
			particularBranchDetails.get(0)
					.setVehicelMaintenanceSMSNumber(clientDetails.getVehicelMaintenanceSMSNumber());
			particularBranchDetails.get(0).setVehicelMaintenanceEmailId(clientDetails.getVehicelMaintenanceEmailId());

			particularBranchDetails.get(0)
					.setEmployeeCheckOutGeofenceRegion(clientDetails.getEmployeeCheckOutGeofenceRegion());
			particularBranchDetails.get(0).setEmployeeFeedbackEmail(clientDetails.getEmployeeFeedbackEmail());
			particularBranchDetails.get(0).setEmployeeFeedbackEmailId(clientDetails.getEmployeeFeedbackEmailId());
			particularBranchDetails.get(0).setEmployeeAppReportBug(clientDetails.getEmployeeAppReportBug());
			particularBranchDetails.get(0).setReportBugEmailIds(clientDetails.getReportBugEmailIds());
			particularBranchDetails.get(0).setToEmployeeFeedBackEmail(clientDetails.getToEmployeeFeedBackEmail());

			particularBranchDetails.get(0).setDissableTimeOTP(oTpTime);
			particularBranchDetails.get(0).setMaxTimeOTP(clientDetails.getMaxTimeOTP());
			particularBranchDetails.get(0).setApprovalProcess(clientDetails.getApprovalProcess());
			particularBranchDetails.get(0).setPostApproval(clientDetails.getPostApproval());
			particularBranchDetails.get(0).setRequestWithProject(clientDetails.getRequestWithProject());
			particularBranchDetails.get(0).setShiftTimeGenderPreference(clientDetails.getShiftTimeGenderPreference());
			particularBranchDetails.get(0).setVehiceCheckList(clientDetails.getVehiceCheckList());
			particularBranchDetails.get(0).setDriverTripHistory(clientDetails.getDriverTripHistory());
			particularBranchDetails.get(0).setMinimumDestCount(clientDetails.getMinimumDestCount());
			particularBranchDetails.get(0).setManagerReqCreateProcess(clientDetails.getManagerReqCreateProcess());
			particularBranchDetails.get(0).setGpsKmModification(clientDetails.getGpsKmModification());
			particularBranchDetails.get(0).setPlaCardPrint(clientDetails.getPlaCardPrint());
			particularBranchDetails.get(0).setTripConsiderDelayAfter(delayTime);
			particularBranchDetails.get(0).setOnPickUpNoShowCancelDrop(clientDetails.getOnPickUpNoShowCancelDrop());
			particularBranchDetails.get(0).setNumberOfConsecutiveNoShow(clientDetails.getNumberOfConsecutiveNoShow());		
			particularBranchDetails.get(0).setMobileLoginUrl(clientDetails.getMobileLoginUrl());
			particularBranchDetails.get(0).setWebPageCount(clientDetails.getWebPageCount());
			particularBranchDetails.get(0).setMobilePageCount(clientDetails.getMobilePageCount());
			particularBranchDetails.get(0).setMobileLoginVia(clientDetails.getMobileLoginVia());
			particularBranchDetails.get(0).setSsoLoginUrl(clientDetails.getSsoLoginUrl());
			particularBranchDetails.get(0).setMultiFacility(clientDetails.isMultiFacility());			
			userMasterBO.update(particularBranchDetails.get(0));
		}

		requests.put("status", "success");
		log.info("serviceEnd -UserId :" + clientDetails.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/indicationDetails")
	public Response indicationDetails(EFmFmIndicationMasterPO eFmFmIndicationMasterPO) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		responce.put("status", "failed");
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		/*
		 * log.info("serviceStart -UserId :" + assignRoutePO.getUserId()); try{
		 * if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader(
		 * "authenticationToken"),assignRoutePO.getUserId()))){
		 * responce.put("status", "invalidRequest"); return
		 * Response.ok(responce, MediaType.APPLICATION_JSON).build(); }
		 * List<EFmFmUserMasterPO> userDetail =
		 * userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId()); if
		 * (!(userDetail.isEmpty())) { String jwtToken = ""; try {
		 * JwtTokenGenerator token = new JwtTokenGenerator(); jwtToken =
		 * token.generateToken();
		 * userDetail.get(0).setAuthorizationToken(jwtToken);
		 * userDetail.get(0).setTokenGenerationTime(new Date());
		 * userMasterBO.update(userDetail.get(0)); } catch (Exception e) {
		 * log.info("error" + e); } } }catch(Exception e){ log.info(
		 * "authenticationToken error"+e); }
		 */

		List<Map<String, Object>> tripDetails = new ArrayList<Map<String, Object>>();
		try {
			List<EFmFmIndicationMasterPO> data = eFmFmIndicationMasterPO.getLevelStatus();
			for (EFmFmIndicationMasterPO values : data) {
				List<EFmFmIndicationMasterPO> indicationIdDetails = iCabRequestBO.getAllIndicationById(
						eFmFmIndicationMasterPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
						values.getIndicationId());
				if (indicationIdDetails.isEmpty()) {
					List<EFmFmIndicationMasterPO> indicationDetailsExist = iCabRequestBO.getIndicationDetailsExist(
							eFmFmIndicationMasterPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
							values.getAlertTypeRequest(), values.getAlertFunctionlityType(), values.getLevelType(),
							values.getTime(), values.getEfmFmUserMaster().getUserId());
					if (indicationDetailsExist.isEmpty()) {
						EFmFmIndicationMasterPO eFmFmIndicationMaster = new EFmFmIndicationMasterPO();
						EFmFmUserMasterPO userMaster = new EFmFmUserMasterPO();
						userMaster.setUserId(values.getUserId());
						String tigTime = values.getTime();
						Date time = shiftFormate.parse(tigTime);
						java.sql.Time triggerTime = new java.sql.Time(time.getTime());
						eFmFmIndicationMaster.setTiggerTime(triggerTime);
						eFmFmIndicationMaster.setAlertFunctionlityType(values.getAlertFunctionlityType());
						eFmFmIndicationMaster.setEfmFmUserMaster(userMaster);
						eFmFmIndicationMaster.setLevelType(values.getLevelType());
						eFmFmIndicationMaster.setAlertTypeRequest(values.getAlertTypeRequest());
						iCabRequestBO.save(eFmFmIndicationMaster);
						responce.put("status", "success");
					}
				} else {
					EFmFmUserMasterPO userMaster = new EFmFmUserMasterPO();
					userMaster.setUserId(values.getUserId());
					indicationIdDetails.get(0).setEfmFmUserMaster(userMaster);
					String tigTime = values.getTime();
					Date time = shiftFormate.parse(tigTime);
					java.sql.Time triggerTime = new java.sql.Time(time.getTime());
					indicationIdDetails.get(0).setTiggerTime(triggerTime);
					indicationIdDetails.get(0).setAlertFunctionlityType(values.getAlertFunctionlityType());
					indicationIdDetails.get(0).setEfmFmUserMaster(userMaster);
					indicationIdDetails.get(0).setLevelType(values.getLevelType());
					indicationIdDetails.get(0).setAlertTypeRequest(values.getAlertTypeRequest());
					iCabRequestBO.update(indicationIdDetails.get(0));
				}
			}
		} catch (Exception e) {
			log.info("error" + e);
			responce.put("status", "failed");
		}
		log.info("serviceEnd -UserId :" + eFmFmIndicationMasterPO.getUserId());
		return Response.ok(tripDetails, MediaType.APPLICATION_JSON).build();
	}

	public List<Map<String, Object>> indicationAllDetails(int branchId) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");

		List<Map<String, Object>> indicationData = new ArrayList<Map<String, Object>>();
		try {
			List<EFmFmIndicationMasterPO> indicationDetails = iCabRequestBO.getAllAlertFunctionlityType(branchId);
			for (EFmFmIndicationMasterPO funAlertType : indicationDetails) {
				Map<String, Object> funAlertTypeList = new HashMap<String, Object>();
				funAlertTypeList.put("alertFunctionlityType", funAlertType.getAlertFunctionlityType());
				List<EFmFmIndicationMasterPO> leveTypeDetails = iCabRequestBO.getAllLevelType(branchId,
						funAlertType.getAlertFunctionlityType());
				List<Map<String, Object>> leveTypeListDetails = new ArrayList<Map<String, Object>>();
				for (EFmFmIndicationMasterPO groupOfLevel : leveTypeDetails) {
					Map<String, Object> groupOfLevelList = new HashMap<String, Object>();
					groupOfLevelList.put("levelType", groupOfLevel.getLevelType());
					List<Map<String, Object>> leveTypeData = new ArrayList<Map<String, Object>>();
					List<EFmFmIndicationMasterPO> levelTypeDetails = iCabRequestBO
							.getAllAlertFunctionlityTypeByLevelType(branchId, funAlertType.getAlertFunctionlityType(),
									groupOfLevel.getLevelType());
					for (EFmFmIndicationMasterPO levelTypeList : levelTypeDetails) {
						Map<String, Object> levelType = new HashMap<String, Object>();
						levelType.put("indicationId", levelTypeList.getIndicationId());
						levelType.put("alertTypeRequest", levelTypeList.getAlertTypeRequest());
						levelType.put("userId", levelTypeList.getEfmFmUserMaster().getUserId());
						levelType.put("levelType", levelTypeList.getLevelType());
						if (levelTypeList.getAlertTypeRequest().equalsIgnoreCase("Both")) {
							levelType
									.put("mobileNumber",
											new String(
													Base64.getDecoder().decode(
															levelTypeList.getEfmFmUserMaster().getMobileNumber()),
													"utf-8"));
							levelType.put("emailId",
									new String(
											Base64.getDecoder().decode(levelTypeList.getEfmFmUserMaster().getEmailId()),
											"utf-8"));
						} else if (levelTypeList.getAlertTypeRequest().equalsIgnoreCase("SMS")) {
							levelType
									.put("mobileNumber",
											new String(
													Base64.getDecoder().decode(
															levelTypeList.getEfmFmUserMaster().getMobileNumber()),
													"utf-8"));
						} else {
							levelType.put("emailId",
									new String(
											Base64.getDecoder().decode(levelTypeList.getEfmFmUserMaster().getEmailId()),
											"utf-8"));
						}
						leveTypeData.add(levelType);
					}
					groupOfLevelList.put("levelTypeDetails", leveTypeData);
					leveTypeListDetails.add(groupOfLevelList);
				}
				funAlertTypeList.put("alertFunctionlityDetails", leveTypeListDetails);
				indicationData.add(funAlertTypeList);
			}
		} catch (Exception e) {
			log.info("error" + e);

		}

		return indicationData;

	}

	/**
	 * 
	 * @param eFmFmUserMaster
	 *            delete employee from transport data
	 * @return disable employee from batch details
	 */

	@POST
	@Path("/empBatchdelete")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response empBatchdelete(@FormDataParam("filename") InputStream uploadedInputStream,
			@FormDataParam("filename") FormDataContentDisposition fileDetail, @QueryParam("branchId") int branchId,
			@Context HttpServletRequest request, @QueryParam("profileId") int userId) throws ParseException,
					IOException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
		log.info("serviceStart -UserId :" + userId);
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));

		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"), userId))) {

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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		// log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		String status = "";
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(uploadedInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();
			int distance = 0;
			int lastVal = 0;
			ArrayList<Object> columnValue = new ArrayList<Object>();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						columnValue.add(cell.getStringCellValue().toString().trim());
						break;
					default:
						columnValue.add("");
						break;
					}
				}
				if (!(columnValue.isEmpty())) {
					// lastVal=Integer.parseInt(columnValue.get(0).toString().trim());
					// log.info("lastVal"+lastVal);

					// log.info("current"+columnValue.get(0).toString().trim());
				}

			}
			System.out.println("columnValue" + columnValue);
			int j = 0;
			int sum = 0;

			for (int i = 0; i < columnValue.size() - 1; i++) {
				System.out.println("columnValue First" + columnValue.get(i));
				j++;
				System.out.println("columnValue Second" + columnValue.get(j));
				CalculateDistance cal = new CalculateDistance();
				// Serverson sine Distance
				sum += cal.distance(Double.parseDouble(columnValue.get(i).toString().split(",")[0]),
						Double.parseDouble(columnValue.get(i).toString().split(",")[1]),
						Double.parseDouble(columnValue.get(j).toString().split(",")[0]),
						Double.parseDouble(columnValue.get(j).toString().split(",")[1]), 'm');

				// Google Distance matrix
				// sum+=cal.employeeDistanceCalculation(columnValue.get(i).toString().split(",")[0].toString(),columnValue.get(j).toString().split(",")[1].toString());

				System.out.println("sum" + sum);
			}
			System.out.println("TotalSum" + sum);

		} catch (Exception e) {
			log.info("Exception" + e);
		}
		// log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		log.info("serviceEnd -UserId :" + userId);
		return Response.ok(status, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param eFmFmUserMaster
	 *            delete employee from transport data
	 * @return delete employee from employee details
	 */

	@POST
	@Path("/deleteemployee")
	public Response deleteEmployeeFromEmployeeDetails(EFmFmUserMasterPO eFmFmUserMaster) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		log.info("serviceStart -EmployeeId :" + eFmFmUserMaster.getEmployeeId());
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -deleteemployee :" + eFmFmUserMaster.getEmployeeId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					Integer.parseInt(eFmFmUserMaster.getEmployeeId())))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(Integer.parseInt(eFmFmUserMaster.getEmployeeId()));
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		Map<String, Object> requests = new HashMap<String, Object>();
		log.info("UserDetailService" + eFmFmUserMaster.getButtonType());

		List<EFmFmUserMasterPO> loggedInUserDetail = userMasterBO
				.getLoggedInUserDetailFromClientIdAndUserId(eFmFmUserMaster);

		if (!(loggedInUserDetail.isEmpty())) {
			if (eFmFmUserMaster.getButtonType().equalsIgnoreCase("Disable")) {
				loggedInUserDetail.get(0).setStatus("N");
				userMasterBO.update(loggedInUserDetail.get(0));
				List<EFmFmEmployeeRequestMasterPO> requestDetails = iCabRequestBO
						.getAllRequestFromRequestMasterFprParticularEmployee(eFmFmUserMaster.getUserId(),
								eFmFmUserMaster.getCombinedFacility());
				if (!(requestDetails.isEmpty())) {
					for (EFmFmEmployeeRequestMasterPO requestMasterPO : requestDetails) {
						requestMasterPO.setStatus("N");
						requestMasterPO.setReadFlg("N");
						iCabRequestBO.update(requestMasterPO);
					}
				}
			} else {
				loggedInUserDetail.get(0).setStatus("Y");
				userMasterBO.update(loggedInUserDetail.get(0));
				List<EFmFmEmployeeRequestMasterPO> requestDetails = iCabRequestBO
						.getAllRequestFromRequestMasterFprParticularEmployee(eFmFmUserMaster.getUserId(),
								eFmFmUserMaster.getCombinedFacility());
				if (!(requestDetails.isEmpty())) {
					for (EFmFmEmployeeRequestMasterPO requestMasterPO : requestDetails) {
						requestMasterPO.setStatus("N");
						requestMasterPO.setReadFlg("N");
						iCabRequestBO.update(requestMasterPO);
					}
				}
			}
			requests.put("status", loggedInUserDetail.get(0).getStatus());
		}
		log.info("service end -EmployeeId :" + eFmFmUserMaster.getEmployeeId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param eFmFmUserMaster
	 *            employee GeoCodes enabling and disabling from usermaster data
	 * @return employee GeoCodes from employee details
	 */

	@POST
	@Path("/empGeoCodeStatus")
	public Response updateEmployeeGeoCodeStatus(EFmFmUserMasterPO eFmFmUserMaster) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		System.out.println(eFmFmUserMaster.getUserId());
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmUserMaster.getEmployeeId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					Integer.parseInt(eFmFmUserMaster.getEmployeeId())))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(Integer.parseInt(eFmFmUserMaster.getEmployeeId()));
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		log.info("serviceStart -employeeId :" + eFmFmUserMaster.getEmployeeId());
		Map<String, Object> requests = new HashMap<String, Object>();
		log.info("UserDetailService" + eFmFmUserMaster.getButtonType());

		List<EFmFmUserMasterPO> loggedInUserDetail = userMasterBO
				.getLoggedInUserDetailFromClientIdAndUserId(eFmFmUserMaster);

		if (!(loggedInUserDetail.isEmpty())) {
			if (loggedInUserDetail.get(0).getStatus().equalsIgnoreCase("N")) {
				requests.put("status", "disable");
				log.info("disable serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}
			if (eFmFmUserMaster.getButtonType().equalsIgnoreCase("Y")) {
				log.info("enable on device ready to update");
				// enable on device ready to update
				loggedInUserDetail.get(0).setLocationStatus("N");
			} else {
				log.info("else on device ready to update");
				loggedInUserDetail.get(0).setLocationStatus("Y");
			}
			userMasterBO.update(loggedInUserDetail.get(0));
			requests.put("status", loggedInUserDetail.get(0).getLocationStatus());
		}
		log.info(" end serviceEnd -employeeId :" + eFmFmUserMaster.getEmployeeId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param eFmFmUserMaster
	 *            save changeUserRole setting of the client
	 * @return application setting saved bye client
	 */

	@POST
	@Path("/allmodules")
	public Response changeUserRole(EFmFmClientUserRolePO eFmFmClientUserRolePO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmClientUserRolePO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmClientUserRolePO.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmClientUserRolePO.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<Map<String, Object>> allAccessDetails = new ArrayList<Map<String, Object>>();
		List<EFmFmClientUserRolePO> userDetails = userMasterBO
				.getUserRolesFromUserIdAndBranchId(eFmFmClientUserRolePO.getEfmFmUserMaster().getUserId());
		log.info("Total Number of  accessable modules...." + userDetails.size());
		
		if (!(userDetails.isEmpty())) {
			List<EFmFmClientBranchConfigurationMappingPO> userMainModules = userMasterBO
					.getAllBranchMappingDetailsByBranchIdAttchedToThatUser(userDetails.get(0).geteFmFmClientBranchPO().getBranchId());
			boolean supervisorAccess = true;
			for (EFmFmClientBranchConfigurationMappingPO mainModule : userMainModules) {
				List<Map<String, Object>> allAccessSubModulesDetails = new ArrayList<Map<String, Object>>();
				if (supervisorAccess) {
					supervisorAccess = false;
					Map<String, Object> requests = new HashMap<String, Object>();
					if (userMainModules.size() == userDetails.size()) {
						requests.put("isActive", true);
					} else {
						requests.put("isActive", false);

					}
					requests.put("moduleName", "Super Admin");
					requests.put("moduleId", 0);
					requests.put("subModules", allAccessSubModulesDetails);
					allAccessDetails.add(requests);
				}
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("moduleName", mainModule.geteFmFmClientBranchConfiguration().getModuleName());
				requests.put("moduleId",
						mainModule.geteFmFmClientBranchConfiguration().getClientBranchConfigurationId());
				List<EFmFmClientUserRolePO> moduleExistCheck = userMasterBO.getUserModulesByUserIdBranchIdAndModuleId(
						eFmFmClientUserRolePO.getEfmFmUserMaster().getUserId(),
						mainModule.geteFmFmClientBranchConfiguration().getClientBranchConfigurationId());
				if (moduleExistCheck.isEmpty()) {
					requests.put("isActive", false);
				} else {
					requests.put("isActive", true);
				}
				List<EFmFmClientBranchSubConfigurationPO> subModulesOfModule = userMasterBO
						.getSubModulesOfMainModuleByModuleId(
								mainModule.geteFmFmClientBranchConfiguration().getClientBranchConfigurationId());
				for (EFmFmClientBranchSubConfigurationPO subModules : subModulesOfModule) {
					if (!(mainModule.geteFmFmClientBranchConfiguration().getModuleName().trim()
							.equalsIgnoreCase(subModules.getSubModuleName().trim()))) {
						Map<String, Object> subModulesOfMain = new HashMap<String, Object>();
						subModulesOfMain.put("subModuleName", subModules.getSubModuleName());
						subModulesOfMain.put("subModuleId", subModules.getClientBranchSubConfigurationId());
						List<EFmFmClientUserRolePO> subModuleExistCheck = userMasterBO
								.getUserSubModulesByUserIdBranchIdAndSubModuleId(
										eFmFmClientUserRolePO.getEfmFmUserMaster().getUserId(),
										subModules.getClientBranchSubConfigurationId());
						if (subModuleExistCheck.isEmpty()) {
							subModulesOfMain.put("isActive", false);
						} else {
							subModulesOfMain.put("isActive", true);
						}
						allAccessSubModulesDetails.add(subModulesOfMain);
					}

				}
				requests.put("subModules", allAccessSubModulesDetails);
				allAccessDetails.add(requests);
			}

		}
		log.info("serviceEnd -UserId :" + eFmFmClientUserRolePO.getUserId());
		return Response.ok(allAccessDetails, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param eFmFmUserMaster
	 *            save changeUserRole setting of the client
	 * @return application setting saved bye client
	 */

	@POST
	@Path("/addOrRemoveModules")
	public Response addOrRemoveModules(EFmFmClientBranchConfigurationPO eFmFmClientBranchConfigurationPO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -profileId :" + eFmFmClientBranchConfigurationPO.getProfileId());

		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmClientBranchConfigurationPO.getProfileId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmClientBranchConfigurationPO.getProfileId());

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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		Map<String, Object> requests = new HashMap<String, Object>();
		List<EFmFmUserMasterPO> userIdDetail = userMasterBO
				.getUserDetailFromUserId(eFmFmClientBranchConfigurationPO.getUserId());

		int numberOfAdmin = 0;
		if (eFmFmClientBranchConfigurationPO.isAdminAccess()) {
			List<Integer> userDetails = userMasterBO
					.getAdminUserRoleByBranchId(userIdDetail.get(0).geteFmFmClientBranchPO().getBranchId());
			if (!(userDetails.isEmpty())) {
				log.info("total admin" + userDetails.size());
				try {
					numberOfAdmin = userMasterBO
							.getBranchDetailsFromBranchId(userIdDetail.get(0).geteFmFmClientBranchPO().getBranchId());
					log.info("number of admin" + numberOfAdmin);
				} catch (Exception e) {
					log.info("error in Number of admin function" + e);
				}
				if (userDetails.size() >= numberOfAdmin) {
					requests.put("status", "noadmin");
					log.info("serviceEnd -UserId :" + eFmFmClientBranchConfigurationPO.getUserId());
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				}
			}
		}

		if (eFmFmClientBranchConfigurationPO.getClientBranchConfigurationId() == 0) {
			if (eFmFmClientBranchConfigurationPO.isActive()) {
				List<EFmFmClientBranchConfigurationMappingPO> allModules = userMasterBO
						.getAllBranchMappingDetailsByBranchId(eFmFmClientBranchConfigurationPO.getCombinedFacility());
				if (!(allModules.isEmpty())) {
					for (EFmFmClientBranchConfigurationMappingPO eFmFmClientBranchConfigurationMapping : allModules) {
						List<EFmFmClientUserRolePO> userAccessDetails = userMasterBO
								.getUserSubModulesByUserIdBranchIdAndSubModuleId(
										eFmFmClientBranchConfigurationPO.getUserId(),
										eFmFmClientBranchConfigurationMapping.geteFmFmClientBranchConfiguration()
												.getClientBranchConfigurationId());
						if (userAccessDetails.isEmpty()) {
							log.info("adding a row");
							EFmFmClientUserRolePO eFmFmClientUserRolePO1 = new EFmFmClientUserRolePO();
							EFmFmClientBranchPO eFmFmClientBranchPO1 = new EFmFmClientBranchPO();
							eFmFmClientBranchPO1
									.setBranchId(userIdDetail.get(0).geteFmFmClientBranchPO().getBranchId());
							eFmFmClientUserRolePO1.seteFmFmClientBranchPO(eFmFmClientBranchPO1);
							EFmFmClientBranchSubConfigurationPO eFmFmClientBranchSubConfiguration1 = new EFmFmClientBranchSubConfigurationPO();
							eFmFmClientBranchSubConfiguration1
									.setClientBranchSubConfigurationId(eFmFmClientBranchConfigurationMapping
											.geteFmFmClientBranchConfiguration().getClientBranchConfigurationId());
							eFmFmClientUserRolePO1
									.seteFmFmClientBranchSubConfiguration(eFmFmClientBranchSubConfiguration1);
							EFmFmRoleMasterPO efmFmRoleMaster1 = new EFmFmRoleMasterPO();
							if (eFmFmClientBranchConfigurationPO.isAdminAccess()) {
								efmFmRoleMaster1.setRoleId(1);
							} else {
								efmFmRoleMaster1.setRoleId(4);
							}
							eFmFmClientUserRolePO1.setEfmFmRoleMaster(efmFmRoleMaster1);
							EFmFmUserMasterPO eFmFmUserMasterPO1 = new EFmFmUserMasterPO();
							eFmFmUserMasterPO1.setUserId(eFmFmClientBranchConfigurationPO.getUserId());
							eFmFmClientUserRolePO1.setEfmFmUserMaster(eFmFmUserMasterPO1);
							userMasterBO.save(eFmFmClientUserRolePO1);

						} else {
							if (eFmFmClientBranchConfigurationPO.isAdminAccess()) {
								EFmFmRoleMasterPO efmFmRoleMaster1 = new EFmFmRoleMasterPO();
								efmFmRoleMaster1.setRoleId(1);
								userAccessDetails.get(0).setEfmFmRoleMaster(efmFmRoleMaster1);
							} else {
								EFmFmRoleMasterPO efmFmRoleMaster1 = new EFmFmRoleMasterPO();
								efmFmRoleMaster1.setRoleId(4);
								userAccessDetails.get(0).setEfmFmRoleMaster(efmFmRoleMaster1);

							}
							userMasterBO.update(userAccessDetails.get(0));

						}
					}
				}
			} else {
				List<EFmFmClientBranchConfigurationMappingPO> allModules = userMasterBO
						.getAllBranchMappingDetailsByBranchId(eFmFmClientBranchConfigurationPO.getCombinedFacility());
				if (!(allModules.isEmpty())) {
					for (EFmFmClientBranchConfigurationMappingPO eFmFmClientBranchConfigurationMapping : allModules) {
						List<EFmFmClientUserRolePO> userAccessDetails = userMasterBO
								.getUserSubModulesByUserIdBranchIdAndSubModuleId(
										eFmFmClientBranchConfigurationPO.getUserId(),
										eFmFmClientBranchConfigurationMapping.geteFmFmClientBranchConfiguration()
												.getClientBranchConfigurationId());
						if (!(userAccessDetails.isEmpty())) {
							if (!(userAccessDetails.get(0).geteFmFmClientBranchSubConfiguration().getSubModuleName()
									.equalsIgnoreCase("Request Details"))) {
								userMasterBO.removeARole(userAccessDetails.get(0).getUserRoleId());
							} else {
								EFmFmRoleMasterPO efmFmRoleMaster1 = new EFmFmRoleMasterPO();
								efmFmRoleMaster1.setRoleId(4);
								userAccessDetails.get(0).setEfmFmRoleMaster(efmFmRoleMaster1);
								userMasterBO.update(userAccessDetails.get(0));
							}
						}
					}
				}

			}
		} else {
			List<EFmFmClientUserRolePO> userAccess = userMasterBO.getUserSubModulesByUserIdBranchIdAndSubModuleId(
					eFmFmClientBranchConfigurationPO.getUserId(),
					eFmFmClientBranchConfigurationPO.getClientBranchConfigurationId());
			if (userAccess.isEmpty() && eFmFmClientBranchConfigurationPO.isActive()
					&& eFmFmClientBranchConfigurationPO.getClientBranchConfigurationId() != 0) {
				log.info("adding a row");
				EFmFmClientUserRolePO eFmFmClientUserRolePO = new EFmFmClientUserRolePO();
				EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
				eFmFmClientBranchPO.setBranchId(userIdDetail.get(0).geteFmFmClientBranchPO().getBranchId());
				eFmFmClientUserRolePO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
				EFmFmClientBranchSubConfigurationPO eFmFmClientBranchSubConfiguration = new EFmFmClientBranchSubConfigurationPO();
				eFmFmClientBranchSubConfiguration.setClientBranchSubConfigurationId(
						eFmFmClientBranchConfigurationPO.getClientBranchConfigurationId());
				eFmFmClientUserRolePO.seteFmFmClientBranchSubConfiguration(eFmFmClientBranchSubConfiguration);
				EFmFmRoleMasterPO efmFmRoleMaster = new EFmFmRoleMasterPO();
				if (eFmFmClientBranchConfigurationPO.isAdminAccess()) {
					efmFmRoleMaster.setRoleId(1);
				} else {
					efmFmRoleMaster.setRoleId(4);
				}
				eFmFmClientUserRolePO.setEfmFmRoleMaster(efmFmRoleMaster);
				EFmFmUserMasterPO eFmFmUserMaster = new EFmFmUserMasterPO();
				eFmFmUserMaster.setUserId(eFmFmClientBranchConfigurationPO.getUserId());
				eFmFmClientUserRolePO.setEfmFmUserMaster(eFmFmUserMaster);
				userMasterBO.save(eFmFmClientUserRolePO);
			} else if (!(userAccess.isEmpty()) && !(eFmFmClientBranchConfigurationPO.isActive())) {
				log.info("deleting a row");
				userMasterBO.removeARole(userAccess.get(0).getUserRoleId());
				if (userAccess.get(0).getUserRoleId() == 1) {
					for (EFmFmClientUserRolePO eFmFmClientUserRole : userAccess) {
						EFmFmRoleMasterPO efmFmRoleMaster = new EFmFmRoleMasterPO();
						efmFmRoleMaster.setRoleId(4);
						eFmFmClientUserRole.setEfmFmRoleMaster(efmFmRoleMaster);
						userMasterBO.update(eFmFmClientUserRole);

					}
				}
			}
		}

		requests.put("status", "success");
		log.info("serviceEnd -addOrRemoveModules :" + eFmFmClientBranchConfigurationPO.getProfileId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param eFmFmUserMaster
	 *            save application setting of the client
	 * @return application setting saved bye client
	 * 
	 * 
	 * @POST @Path("/changeuserrole") public Response
	 *       changeUserRole(EFmFmClientUserRolePO eFmFmClientUserRolePO){
	 *       IUserMasterBO userMasterBO = (IUserMasterBO)
	 *       ContextLoader.getContext().getBean("IUserMasterBO"); Map<String,
	 *       Object> requests = new HashMap<String, Object>();
	 *       if(eFmFmClientUserRolePO.getEfmFmRoleMaster().getRole().
	 *       equalsIgnoreCase("manager")){ EFmFmClientBranchPO
	 *       eFmFmClientBranchPO=new EFmFmClientBranchPO();
	 *       eFmFmClientBranchPO.setBranchId(eFmFmClientUserRolePO.
	 *       geteFmFmClientBranchPO().getBranchId()); EFmFmUserMasterPO
	 *       eFmFmUserMaster=new EFmFmUserMasterPO();
	 *       eFmFmUserMaster.setUserId(eFmFmClientUserRolePO.getEfmFmUserMaster(
	 *       ).getUserId());
	 *       eFmFmUserMaster.seteFmFmClientBranchPO(eFmFmClientBranchPO); List
	 *       <EFmFmUserMasterPO> userDetail=userMasterBO.
	 *       getLoggedInUserDetailFromClientIdAndUserId(eFmFmUserMaster); List
	 *       <EFmFmUserMasterPO> allProjectEmployees=userMasterBO.
	 *       getAllUsersBelogsProject(eFmFmClientUserRolePO.
	 *       geteFmFmClientBranchPO().getBranchId(),
	 *       userDetail.get(0).geteFmFmClientProjectDetails().getProjectId());
	 *       for(EFmFmUserMasterPO userMasterPO:allProjectEmployees){ List
	 *       <EFmFmClientUserRolePO> userClientRoleMaster =
	 *       userMasterBO.getUserRolesFromUserIdAndBranchId(userMasterPO.
	 *       getUserId(),eFmFmClientUserRolePO.geteFmFmClientBranchPO().
	 *       getBranchId());
	 *       if(userClientRoleMaster.get(0).getEfmFmRoleMaster().getRole().
	 *       equalsIgnoreCase("manager")){
	 *       requests.put("name",userClientRoleMaster.get(0).getEfmFmUserMaster(
	 *       ).getFirstName() ); requests.put("status","exist"); }
	 * 
	 *       } return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	 *       } List<EFmFmRoleMasterPO> eFmFmRoleMasterPO=userMasterBO.getRoleId(
	 *       eFmFmClientUserRolePO.getEfmFmRoleMaster().getRole()); List
	 *       <EFmFmClientUserRolePO> userClientRoleMaster =
	 *       userMasterBO.getUserRolesFromUserIdAndBranchId(
	 *       eFmFmClientUserRolePO.getEfmFmUserMaster().getUserId(),
	 *       eFmFmClientUserRolePO.geteFmFmClientBranchPO().getBranchId());
	 *       userClientRoleMaster.get(0).setEfmFmRoleMaster(eFmFmRoleMasterPO.
	 *       get(0)); requests.put("status", "success");
	 *       userMasterBO.update(userClientRoleMaster.get(0)); return
	 *       Response.ok(requests, MediaType.APPLICATION_JSON).build(); }
	 */

	@POST
	@Path("/changenormaluserrole")
	public Response changeNormalUserRole(EFmFmClientUserRolePO eFmFmClientUserRolePO) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmClientUserRolePO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmClientUserRolePO.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmClientUserRolePO.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		Map<String, Object> requests = new HashMap<String, Object>();
		if (eFmFmClientUserRolePO.getEfmFmRoleMaster().getRole().equalsIgnoreCase("manager")) {
			EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
			eFmFmClientBranchPO.setBranchId(eFmFmClientUserRolePO.geteFmFmClientBranchPO().getBranchId());
			EFmFmUserMasterPO eFmFmUserMaster = new EFmFmUserMasterPO();
			eFmFmUserMaster.setUserId(eFmFmClientUserRolePO.getEfmFmUserMaster().getUserId());
			eFmFmUserMaster.seteFmFmClientBranchPO(eFmFmClientBranchPO);
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getLoggedInUserDetailFromClientIdAndUserId(eFmFmUserMaster);
			List<EFmFmUserMasterPO> allProjectEmployees = userMasterBO.getAllUsersBelogsProject(
					eFmFmClientUserRolePO.geteFmFmClientBranchPO().getBranchId(),
					userDetail.get(0).geteFmFmClientProjectDetails().getProjectId());
			for (EFmFmUserMasterPO userMasterPO : allProjectEmployees) {
				List<EFmFmClientUserRolePO> userClientRoleMaster = userMasterBO
						.getUserRolesFromUserIdAndBranchId(userMasterPO.getUserId());
				if (userClientRoleMaster.get(0).getEfmFmRoleMaster().getRole().equalsIgnoreCase("manager")) {
					List<EFmFmRoleMasterPO> eFmFmRoleMasterPO = userMasterBO
							.getRoleId(eFmFmClientUserRolePO.getEfmFmRoleMaster().getRole());
					eFmFmRoleMasterPO.get(0).setRoleId(4);
					userClientRoleMaster.get(0).setEfmFmRoleMaster(eFmFmRoleMasterPO.get(0));
					userMasterBO.update(userClientRoleMaster.get(0));

				}

			}
		}
		List<EFmFmRoleMasterPO> eFmFmRoleMasterPO = userMasterBO
				.getRoleId(eFmFmClientUserRolePO.getEfmFmRoleMaster().getRole());
		List<EFmFmClientUserRolePO> userClientRoleMaster = userMasterBO
				.getUserRolesFromUserIdAndBranchId(eFmFmClientUserRolePO.getEfmFmUserMaster().getUserId());
		userClientRoleMaster.get(0).setEfmFmRoleMaster(eFmFmRoleMasterPO.get(0));
		requests.put("status", "success");
		userMasterBO.update(userClientRoleMaster.get(0));
		log.info("serviceEnd -UserId :" + eFmFmClientUserRolePO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param users
	 *            branch Details details
	 * @return it will returns branch details
	 * @throws ParseException
	 * 
	 */

	@POST
	@Path("/branchdetail")
	public Response getBranchDetails(EFmFmClientBranchPO clientDetails) throws ParseException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + clientDetails.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					clientDetails.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(clientDetails.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmClientBranchPO> particularBranchDetails = userMasterBO
				.getClientDetails(String.valueOf(clientDetails.getCombinedFacility()));
		Map<String, Object> requests = new HashMap<String, Object>();
		DateFormat shiftDateFormater = new SimpleDateFormat("yyyy-MM-dd");
		try{
		requests.put("escortRequired", particularBranchDetails.get(0).getEscortRequired());
		}
		catch(Exception e){
			requests.put("escortRequired", "firstlastfemale");	
		}
		requests.put("managerApproval", particularBranchDetails.get(0).getMangerApprovalRequired());
		requests.put("etaSMS", particularBranchDetails.get(0).getEtaSMS());
		requests.put("employeeAddressGeoFenceArea", particularBranchDetails.get(0).getEmployeeAddressgeoFenceArea());
		requests.put("shiftTimePlusOneHourrAfterSMSContact",
				particularBranchDetails.get(0).getShiftTimePlusOneHourrAfterSMSContact());
		requests.put("shiftTimePlusTwoHourrAfterSMSContact",
				particularBranchDetails.get(0).getShiftTimePlusTwoHourrAfterSMSContact());
		requests.put("maxSpeed", particularBranchDetails.get(0).getMaxSpeed());
		requests.put("driverAutoCheckedoutTime", particularBranchDetails.get(0).getDriverAutoCheckedoutTime());
		requests.put("delayMessage", particularBranchDetails.get(0).getDelayMessageTime());
		requests.put("employeeWaitingTime", particularBranchDetails.get(0).getEmployeeWaitingTime());
		requests.put("emrgContactNumber", particularBranchDetails.get(0).getEmergencyContactNumber());
		requests.put("maxTravelTimeEmployeeWiseInMin",
				particularBranchDetails.get(0).getMaxTravelTimeEmployeeWiseInMin());
		requests.put("maxRadialDistanceEmployeeWiseInKm",
				particularBranchDetails.get(0).getMaxRadialDistanceEmployeeWiseInKm());
		requests.put("maxRouteLengthInKm", particularBranchDetails.get(0).getMaxRouteLengthInKm());
		requests.put("maxRouteDeviationInKm", particularBranchDetails.get(0).getMaxRouteDeviationInKm());
		requests.put("transportContactNumberForMsg", particularBranchDetails.get(0).getTransportContactNumberForMsg());
		requests.put("autoClustering", particularBranchDetails.get(0).getAutoClustering());
		requests.put("startTripGeoFenceArea", particularBranchDetails.get(0).getStartTripGeoFenceAreaInMeter());
		requests.put("endTripGeoFenceArea", particularBranchDetails.get(0).getEndTripGeoFenceAreaInMeter());
		requests.put("feedBackEmailId", particularBranchDetails.get(0).getFeedBackEmailId());
		requests.put("clusterSize", particularBranchDetails.get(0).getClusterSize());

		requests.put("dissableTimeOTP", particularBranchDetails.get(0).getDissableTimeOTP());
		requests.put("maxTimeOTP", particularBranchDetails.get(0).getMaxTimeOTP());

		requests.put("driverImageForEmployee", particularBranchDetails.get(0).getEmpDeviceDriverImage());
		requests.put("driverMobileNumberForEmployee", particularBranchDetails.get(0).getEmpDeviceDriverMobileNumber());
		requests.put("driverNameForEmployee", particularBranchDetails.get(0).getEmpDeviceDriverName());
		requests.put("driverProfilePicOnDriverDevice",
				particularBranchDetails.get(0).getDriverDeviceDriverProfilePicture());
		requests.put("autoCallAndSMSOnDriverDevice", particularBranchDetails.get(0).getDriverDeviceAutoCallAndsms());
		requests.put("tripType", particularBranchDetails.get(0).getTripType());
		requests.put("numberOfAdmin", particularBranchDetails.get(0).getNumberOfAdministarator());
		requests.put("inactiveAdminAccountAfterNumOfDays",
				particularBranchDetails.get(0).getInactiveAdminAccountAfterNumOfDays());

		requests.put("passwordResetPeriodForAdmin",
				particularBranchDetails.get(0).getPasswordResetPeriodForAdminInDays());
		requests.put("passwordResetPeriodForUser",
				particularBranchDetails.get(0).getPasswordResetPeriodForUserInDays());
		requests.put("twoFactorAuthenticationRequired",
				particularBranchDetails.get(0).getTwoFactorAuthenticationRequired());
		requests.put("numberOfWrongPassAttempt", particularBranchDetails.get(0).getNumberOfAttemptsWrongPass());
		requests.put("sessionTimeOut", particularBranchDetails.get(0).getSessionTimeoutInMinutes());
		requests.put("sessionTimeOutNotificationInSec", particularBranchDetails.get(0).getSessionNotificationTime());

		requests.put("invoiceNumberDigits", particularBranchDetails.get(0).getInvoiceNumberDigitRange());

		requests.put("adhocTimePicker", particularBranchDetails.get(0).getAdhocTimePickerForEmployee());
		requests.put("dropPriorTime", particularBranchDetails.get(0).getDropPriorTimePeriod());
		requests.put("pickupPriorTime", particularBranchDetails.get(0).getPickupPriorTimePeriod());
		requests.put("employeeSecondPickUpRequest", particularBranchDetails.get(0).getEmployeeSecondPickUpRequest());
		requests.put("employeeSecondDropRequest", particularBranchDetails.get(0).getEmployeeSecondDropRequest());
		requests.put("panicAlertNeeded", particularBranchDetails.get(0).getPanicAlertNeeded());
		requests.put("lastPassCanNotCurrentPass", particularBranchDetails.get(0).getLastPassCanNotCurrentPass());
		requests.put("autoDropRoster", particularBranchDetails.get(0).getAutoDropRoster());
		requests.put("shiftTimeDiffPickToDrop", particularBranchDetails.get(0).getShiftTimeDiffPickToDrop());
		requests.put("addingGeoFenceDistanceIntrip", particularBranchDetails.get(0).getAddingGeoFenceDistanceIntrip());
		requests.put("cutOffTimeFlg", particularBranchDetails.get(0).getCutOffTime());
		requests.put("invoiceNoOfDays", particularBranchDetails.get(0).getInvoiceNoOfDWorkingDays());
		requests.put("invoiceGenDate", particularBranchDetails.get(0).getInvoiceGenDate());
		requests.put("pickupCancelCutOffTime", particularBranchDetails.get(0).getPickupCancelTimePeriod());
		requests.put("dropCancelCutOffTime", particularBranchDetails.get(0).getDropCancelTimePeriod());
		requests.put("reschedulePickupCutOffTime", particularBranchDetails.get(0).getReschedulePickupCutOffTime());
		requests.put("rescheduleDropCutOffTime", particularBranchDetails.get(0).getRescheduleDropCutOffTime());
		requests.put("driverAutoCheckoutStatus", particularBranchDetails.get(0).getDriverAutoCheckoutStatus());
		requests.put("autoVehicleAllocationStatus", particularBranchDetails.get(0).getAutoVehicleAllocationStatus());
		requests.put("distanceFlg", particularBranchDetails.get(0).getDistanceFlg());

		requests.put("notificationCutoffTimePickup",
				particularBranchDetails.get(0).getNotificationCutoffTimeForPickup());
		requests.put("notificationCutoffTimeDrop", particularBranchDetails.get(0).getNotificationCutoffTimeForDrop());
		requests.put("personalDeviceViaSms", particularBranchDetails.get(0).getPersonalDeviceViaSms());
		requests.put("assignRoutesToVendor", particularBranchDetails.get(0).getAssignRoutesToVendor());
		requests.put("driverCallToEmployee", particularBranchDetails.get(0).getDriverCallToEmployee());
		requests.put("driverCallToTransportDesk", particularBranchDetails.get(0).getDriverCallToTransportDesk());
		requests.put("employeeCallToDriver", particularBranchDetails.get(0).getEmployeeCallToDriver());
		requests.put("employeeCallToTransport", particularBranchDetails.get(0).getEmployeeCallToTransport());

		requests.put("employeeCheckOutGeofenceRegion",
				particularBranchDetails.get(0).getEmployeeCheckOutGeofenceRegion());
		requests.put("employeeFeedbackEmail", particularBranchDetails.get(0).getEmployeeFeedbackEmail());
		requests.put("employeeFeedbackEmailId", particularBranchDetails.get(0).getEmployeeFeedbackEmailId());
		requests.put("employeeAppReportBug", particularBranchDetails.get(0).getEmployeeAppReportBug());
		requests.put("reportBugEmailIds", particularBranchDetails.get(0).getReportBugEmailIds());
		requests.put("toEmployeeFeedBackEmail", particularBranchDetails.get(0).getToEmployeeFeedBackEmail());

		requests.put("escortTimeWindowEnable", particularBranchDetails.get(0).getEscortTimeWindowEnable());
		// if(particularBranchDetails.get(0).getEscortTripType().trim().equalsIgnoreCase("PICKUP")){
		if (particularBranchDetails.get(0).getEscortStartTimePickup() != null
				&& particularBranchDetails.get(0).getEscortEndTimePickup() != null) {
			requests.put("escortStartTimePickup", particularBranchDetails.get(0).getEscortStartTimePickup());
			requests.put("escortEndTimePickup", particularBranchDetails.get(0).getEscortEndTimePickup());
		}
		// if(particularBranchDetails.get(0).getEscortTripType().trim().equalsIgnoreCase("DROP")){
		if (particularBranchDetails.get(0).getEscortStartTimeDrop() != null
				&& particularBranchDetails.get(0).getEscortEndTimeDrop() != null) {
			requests.put("escortStartTimeDrop", particularBranchDetails.get(0).getEscortStartTimeDrop());
			requests.put("escortEndTimeDrop", particularBranchDetails.get(0).getEscortEndTimeDrop());
		}

		requests.put("imageUploadSize", particularBranchDetails.get(0).getImageUploadSize());

		// driver and vehicle compliance Array /*Vehicle Governance*/
		Map<String, Object> complinceDetail = new HashMap<String, Object>();
		complinceDetail.put("licenseExpiryDay", particularBranchDetails.get(0).getLicenseExpiryDay());
		complinceDetail.put("licenseRepeatAlertsEvery", particularBranchDetails.get(0).getLicenseRepeatAlertsEvery());
		complinceDetail.put("licenceNotificationType", particularBranchDetails.get(0).getLicenceNotificationType());
		complinceDetail.put("licenseSMSNumber", particularBranchDetails.get(0).getLicenseSMSNumber());
		complinceDetail.put("licenseEmailId", particularBranchDetails.get(0).getLicenseEmailId());

		complinceDetail.put("medicalFitnessExpiryDay", particularBranchDetails.get(0).getMedicalFitnessExpiryDay());
		complinceDetail.put("medicalFitnessRepeatAlertsEvery",
				particularBranchDetails.get(0).getMedicalFitnessRepeatAlertsEvery());
		complinceDetail.put("medicalFitnessNotificationType",
				particularBranchDetails.get(0).getMedicalFitnessNotificationType());
		complinceDetail.put("medicalFitnessSMSNumber", particularBranchDetails.get(0).getMedicalFitnessSMSNumber());
		complinceDetail.put("medicalFitnessEmailId", particularBranchDetails.get(0).getMedicalFitnessEmailId());

		complinceDetail.put("policeVerificationExpiryDay",
				particularBranchDetails.get(0).getPoliceVerificationExpiryDay());
		complinceDetail.put("policeVerificationRepeatAlertsEvery",
				particularBranchDetails.get(0).getPoliceVerificationRepeatAlertsEvery());
		complinceDetail.put("policeVerificationNotificationType",
				particularBranchDetails.get(0).getPoliceVerificationNotificationType());
		complinceDetail.put("policeVerificationSMSNumber",
				particularBranchDetails.get(0).getPoliceVerificationSMSNumber());
		complinceDetail.put("policeVerificationEmailId", particularBranchDetails.get(0).getPoliceVerificationEmailId());

		complinceDetail.put("DDTrainingExpiryDay", particularBranchDetails.get(0).getDdTrainingExpiryDay());
		complinceDetail.put("ddTrainingRepeatAlertsEvery",
				particularBranchDetails.get(0).getDdTrainingRepeatAlertsEvery());
		complinceDetail.put("DDTrainingNotificationType",
				particularBranchDetails.get(0).getDdTrainingNotificationType());
		complinceDetail.put("DDTrainingSMSNumber", particularBranchDetails.get(0).getDdTrainingSMSNumber());
		complinceDetail.put("DDTrainingEmailId", particularBranchDetails.get(0).getDdTrainingSMSNumber());

		/* Vehicle Governance */
		complinceDetail.put("pollutionDueExpiryDay", particularBranchDetails.get(0).getPollutionDueExpiryDay());
		complinceDetail.put("pollutionDueRepeatAlertsEvery",
				particularBranchDetails.get(0).getPoliceVerificationRepeatAlertsEvery());
		complinceDetail.put("pollutionDueNotificationType",
				particularBranchDetails.get(0).getPollutionDueNotificationType());
		complinceDetail.put("pollutionDueSMSNumber", particularBranchDetails.get(0).getPollutionDueSMSNumber());
		complinceDetail.put("pollutionDueEmailId", particularBranchDetails.get(0).getPollutionDueEmailId());

		complinceDetail.put("insuranceDueExpiryDay", particularBranchDetails.get(0).getInsuranceDueExpiryDay());
		complinceDetail.put("insuranceDueRepeatAlertsEvery",
				particularBranchDetails.get(0).getInsuranceDueRepeatAlertsEvery());
		complinceDetail.put("insuranceDueNotificationType",
				particularBranchDetails.get(0).getInsuranceDueNotificationType());
		complinceDetail.put("insuranceDueSMSNumber", particularBranchDetails.get(0).getInsuranceDueSMSNumber());
		complinceDetail.put("insuranceDueEmailId", particularBranchDetails.get(0).getInsuranceDueEmailId());

		complinceDetail.put("taxCertificateExpiryDay", particularBranchDetails.get(0).getTaxCertificateExpiryDay());
		complinceDetail.put("taxCertificateRepeatAlertsEvery",
				particularBranchDetails.get(0).getTaxCertificateRepeatAlertsEvery());
		complinceDetail.put("taxCertificateNotificationType",
				particularBranchDetails.get(0).getTaxCertificateNotificationType());
		complinceDetail.put("taxCertificateSMSNumber", particularBranchDetails.get(0).getTaxCertificateSMSNumber());
		complinceDetail.put("taxCertificateEmailId", particularBranchDetails.get(0).getTaxCertificateEmailId());

		complinceDetail.put("permitDueExpiryDay", particularBranchDetails.get(0).getPermitDueExpiryDay());
		complinceDetail.put("permitDueRepeatAlertsEvery",
				particularBranchDetails.get(0).getPermitDueRepeatAlertsEvery());
		complinceDetail.put("permitDueNotificationType", particularBranchDetails.get(0).getPermitDueNotificationType());
		complinceDetail.put("permitDueSMSNumber", particularBranchDetails.get(0).getPermitDueSMSNumber());
		complinceDetail.put("permitDueEmailId", particularBranchDetails.get(0).getPermitDueEmailId());

		complinceDetail.put("vehicelMaintenanceExpiryDay",
				particularBranchDetails.get(0).getVehicelMaintenanceExpiryDay());
		complinceDetail.put("vehicelMaintenanceRepeatAlertsEvery",
				particularBranchDetails.get(0).getVehicelMaintenanceRepeatAlertsEvery());
		complinceDetail.put("vehicelMaintenanceNotificationType",
				particularBranchDetails.get(0).getVehicelMaintenanceNotificationType());
		complinceDetail.put("vehicelMaintenanceSMSNumber",
				particularBranchDetails.get(0).getVehicelMaintenanceSMSNumber());
		complinceDetail.put("vehicelMaintenanceEmailId", particularBranchDetails.get(0).getVehicelMaintenanceEmailId());

		if (particularBranchDetails.get(0).getRequestCutOffDate() != null) {
			requests.put("requestToDateCutOff",
					shiftDateFormater.format(particularBranchDetails.get(0).getRequestCutOffDate()));
		} else {
			requests.put("requestToDateCutOff", "NA");
		}
		if (particularBranchDetails.get(0).getRequestCutOffFromDate() != null) {
			requests.put("requestDateCutOffFromDate",
					shiftDateFormater.format(particularBranchDetails.get(0).getRequestCutOffFromDate()));
		} else {
			requests.put("requestDateCutOffFromDate", "NA");
		}
		requests.put("requestCutOffNoOfDays", particularBranchDetails.get(0).getRequestCutOffNoOfDays());
		requests.put("requestType", particularBranchDetails.get(0).getRequestType());
		requests.put("earlyRequestDate", particularBranchDetails.get(0).getEarlyRequestDate());
		requests.put("monthOrDays", particularBranchDetails.get(0).getMonthOrDays());
		requests.put("occurrenceFlg", particularBranchDetails.get(0).getOccurrenceFlg());
		requests.put("daysRequestByString", particularBranchDetails.get(0).getDaysRequest());

		requests.put("driverWaitingTime", particularBranchDetails.get(0).getDriverWaitingTimeAtLastLocation());
		requests.put("backToBackType", particularBranchDetails.get(0).getSelectedB2bType());
		requests.put("b2bByTravelDistanceInKM", particularBranchDetails.get(0).getB2bByTravelDistanceInKM());
		requests.put("b2bByTravelTimeInMinutes", particularBranchDetails.get(0).getB2bByTravelTime());

		if (particularBranchDetails.get(0).getDaysRequest().equalsIgnoreCase("All")
				|| particularBranchDetails.get(0).getDaysRequest().trim().equalsIgnoreCase("")
				|| particularBranchDetails.get(0).getDaysRequest() == null) {
			requests.put("daysRequest", 0);
		} else {
			requests.put("daysRequest", Integer.parseInt(particularBranchDetails.get(0).getDaysRequest()));
		}

		requests.put("locationVisible", particularBranchDetails.get(0).getLocationVisible());
		requests.put("invoiceGenType", particularBranchDetails.get(0).getInvoiceGenType());
		requests.put("destinationPointLimit", particularBranchDetails.get(0).getDestinationPointLimit());

		requests.put("requestWithProject", particularBranchDetails.get(0).getRequestWithProject());
		requests.put("vehiceCheckList", particularBranchDetails.get(0).getVehiceCheckList());
		requests.put("driverHistory", particularBranchDetails.get(0).getDriverTripHistory());
		requests.put("approvalProcess", particularBranchDetails.get(0).getApprovalProcess());
		requests.put("postApproval", particularBranchDetails.get(0).getPostApproval());
		requests.put("shiftTimeGenderPreference", particularBranchDetails.get(0).getShiftTimeGenderPreference());
		requests.put("minimumDestCount", particularBranchDetails.get(0).getMinimumDestCount());
		requests.put("managerReqCreateProcess", particularBranchDetails.get(0).getManagerReqCreateProcess());
		requests.put("gpsKmModification", particularBranchDetails.get(0).getGpsKmModification());
		requests.put("plaCardPrint", particularBranchDetails.get(0).getPlaCardPrint());
		requests.put("LevelHierarchy", indicationAllDetails(clientDetails.getBranchId()));
		requests.put("autoCancelDrop", particularBranchDetails.get(0).getOnPickUpNoShowCancelDrop());
		requests.put("consecutiveNoShowCount", particularBranchDetails.get(0).getNumberOfConsecutiveNoShow());
		requests.put("tripConsiderDelayAfter", particularBranchDetails.get(0).getTripConsiderDelayAfter());
		requests.put("onPickUpNoShowCancelDrop", particularBranchDetails.get(0).getOnPickUpNoShowCancelDrop());
		requests.put("mobileLoginUrl", particularBranchDetails.get(0).getMobileLoginUrl());
		requests.put("webPageCount", particularBranchDetails.get(0).getWebPageCount());
		requests.put("mobilePageCount", particularBranchDetails.get(0).getMobilePageCount());
		requests.put("mobileLoginVia", particularBranchDetails.get(0).getMobileLoginVia());
		requests.put("ssoLoginUrl", particularBranchDetails.get(0).getSsoLoginUrl());
		requests.put("isMultiFacility", particularBranchDetails.get(0).isMultiFacility());
		
		
		/*
		 * servionImpl
		 */
		// requests.put("shiftimeGenderPreference",particularBranchDetails.get(0).getShiftTimeGenderPreference());

		requests.put("status", "success");
		log.info("serviceEnd -UserId :" + clientDetails.getUserId());
		requests.put("compliance", complinceDetail);
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param get
	 *            all Request Master Details On the client basis
	 * @return it will returns all the request details
	 * 
	 */

	@POST
	@Path("/allrequestdetail")
	public Response getAllRequestDetails(EFmFmEmployeeRequestMasterPO travelRequest) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + travelRequest.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					travelRequest.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(travelRequest.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmEmployeeRequestMasterPO> allRequestDetails = iCabRequestBO
				.getAllRequestDetailsFromRequestMasterFromBranchId(
						travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
		List<Map<String, Object>> allRequestsDetails = new ArrayList<Map<String, Object>>();
		for (EFmFmEmployeeRequestMasterPO requestMasterPO : allRequestDetails) {
			Map<String, Object> requests = new HashMap<String, Object>();
			requests.put("userId", requestMasterPO.getEfmFmUserMaster().getUserId());
			requests.put("tripId", requestMasterPO.getTripId());
			requests.put("employeeId", requestMasterPO.getEfmFmUserMaster().getEmployeeId());
			if (requestMasterPO.getTripType().equalsIgnoreCase("PICKUP"))
				requests.put("pickupTime", requestMasterPO.getPickUpTime());
			else
				requests.put("pickupTime", "Not Required");
			requests.put("requestType", requestMasterPO.getRequestType());
			requests.put("status", requestMasterPO.getStatus());
			requests.put("address", requestMasterPO.getEfmFmUserMaster().getAddress());
			requests.put("routeName",
					requestMasterPO.getEfmFmUserMaster().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
			requests.put("areaName",
					requestMasterPO.getEfmFmUserMaster().geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
			requests.put("shiftTime", requestMasterPO.getShiftTime());
			allRequestsDetails.add(requests);
		}
		log.info("serviceEnd -UserId :" + travelRequest.getUserId());
		return Response.ok(allRequestsDetails, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param get
	 *            particular shift time from branch id
	 * @return it will returns specific shift time detials
	 * 
	 */

	@POST
	@Path("/disableShiftTime")
	public Response disableingShiftTime(EFmFmTripTimingMasterPO eFmFmTripTimingMasterPO) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmTripTimingMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmTripTimingMasterPO.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmTripTimingMasterPO.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		String shiftDate = eFmFmTripTimingMasterPO.getTime();
		Date shift = shiftFormate.parse(shiftDate);
		java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
		Map<String, Object> requests = new HashMap<String, Object>();
		List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO.getShiftTimeDetailFromShiftTimeAndTripType(
				new MultifacilityService().combinedBranchIdDetails(eFmFmTripTimingMasterPO.getUserId(),
						eFmFmTripTimingMasterPO.getCombinedFacility()),
				shiftTime, eFmFmTripTimingMasterPO.getTripType());
		if (!(shiftTimeDetails.isEmpty())) {
			shiftTimeDetails.get(0).setIsActive("N");
			iCabRequestBO.update(shiftTimeDetails.get(0));
		}
		requests.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmTripTimingMasterPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/getShiftDetail")
	public Response getShiftDetailByTripTypeAndBranchId(EFmFmTripTimingMasterPO eFmFmTripTimingMasterPO)
			throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmTripTimingMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmTripTimingMasterPO.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmTripTimingMasterPO.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		String shiftDate = eFmFmTripTimingMasterPO.getTime();
		Date shift = shiftFormate.parse(shiftDate);
		Map<String, Object> requests = new HashMap<String, Object>();
		java.sql.Time shiftTime = new java.sql.Time(shift.getTime());
		List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO.getShiftTimeDetailFromShiftTimeAndTripType(
				new MultifacilityService().combinedBranchIdDetails(eFmFmTripTimingMasterPO.getUserId(),
						eFmFmTripTimingMasterPO.getCombinedFacility()),
				shiftTime, eFmFmTripTimingMasterPO.getTripType());
		if (!(shiftTimeDetails.isEmpty())) {
			requests.put("areaGeofence", shiftTimeDetails.get(0).getAreaGeoFenceRegion());
			requests.put("clusterGeofence", shiftTimeDetails.get(0).getClusterGeoFenceRegion());
			requests.put("routeGeofence", shiftTimeDetails.get(0).getRouteGeoFenceRegion());
		}
		requests.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmTripTimingMasterPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/updateCutOffTime")
	public Response cutOffTimeValidation(EFmFmTripTimingMasterPO eFmFmTripTimingMasterPO) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmTripTimingMasterPO.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmTripTimingMasterPO.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO
					.getUserDetailFromUserId(eFmFmTripTimingMasterPO.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		String status = "Input";
		StringBuffer temp = new StringBuffer("PleaseCheck::");
		if (true) {
			if (eFmFmTripTimingMasterPO.getTripType() == null || eFmFmTripTimingMasterPO.getTripType().isEmpty()) {
				temp.append("::Trip Type cannot be empty");
				status = "Fail";
			} else {
				CharSequence tripType = eFmFmTripTimingMasterPO.getTripType();
				Matcher matcher = Validator.pickUpOrDrop(tripType);
				if (!matcher.matches()) {
					temp.append("::Trip Type can be only be pickup or drop");
					status = "Fail";
				}
			}

			if (eFmFmTripTimingMasterPO.getShiftBufferTime() % 5 != 0
					|| eFmFmTripTimingMasterPO.getShiftBufferTime() > 30
					|| eFmFmTripTimingMasterPO.getShiftBufferTime() <= 0) {
				temp.append("::Shift Buffer time should be multiple of 5 and max buffer time can be 30 minutes");
				status = "Fail";
			}

			if (eFmFmTripTimingMasterPO.getTime() == null) {
				temp.append("::shift time cannot be empty");
				status = "Fail";
			} else {
				CharSequence shiftTime = eFmFmTripTimingMasterPO.getTime();
				Matcher matcher = Validator.timeHHMM(shiftTime);
				if (!matcher.matches()) {
					temp.append("::shit time should be in HH:MM");
					status = "Fail";
				}
			}

			if (eFmFmTripTimingMasterPO.getCeilingNo() < 0) {
				temp.append("::Ceiling number should be positive Integer");
				status = "Fail";
			}

			if (eFmFmTripTimingMasterPO.getCancel_Cut_Off_Time() == null) {
				temp.append("::Cancel cut off time cannot be empty");
				status = "Fail";
			} else {
				CharSequence cancelCutOff = (CharSequence) eFmFmTripTimingMasterPO.getCancel_Cut_Off_Time();
				Matcher matcher = Validator.timeHHMM(cancelCutOff);
				if (!matcher.matches()) {
					temp.append("::cancel cut off time should be in HH:MM format");
					status = "Fail";
				}
			}
			if (eFmFmTripTimingMasterPO.getCut_Off_Time() == null) {
				temp.append("::Cut off time cannot be empty");
				status = "Fail";
			} else {
				CharSequence cutOffTime = (CharSequence) eFmFmTripTimingMasterPO.getCut_Off_Time();
				Matcher matcher = Validator.timeHHMM(cutOffTime);
				if (!matcher.matches()) {
					temp.append(":: cut off time should be in HH:MM");
					status = "Fail";
				}
			}

			if (eFmFmTripTimingMasterPO.getReschedule_Cut_Off_Time() == null) {
				temp.append("::Reschedule cut off time cannot be empty");
				status = "Fail";
			} else {
				CharSequence rescheduleCutOffTime = (CharSequence) eFmFmTripTimingMasterPO.getReschedule_Cut_Off_Time();
				Matcher matcher = Validator.timeHHMM(rescheduleCutOffTime);
				if (!matcher.matches()) {
					temp.append(":: RescheduleCutOffTime should be in HH:MM");
					status = "Fail";
				}
			}

			if (status.equals("Fail")) {
				log.info("Invalid input:");
				responce.put("invalidInput", temp);
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}

			if (eFmFmTripTimingMasterPO.getGenderPreference() == null
					|| eFmFmTripTimingMasterPO.getGenderPreference().isEmpty()) {
				temp.append("::Gender Preference cannot be empty");
				status = "Fail";
			} else {
				CharSequence genderPreference = eFmFmTripTimingMasterPO.getGenderPreference();
				Matcher matcher = Validator.genderPreference(genderPreference);
				if (!matcher.matches()) {
					temp.append("::Gender Preference can be only be Y -Yes or N- No");
					status = "Fail";
				}
			}
		}

		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		final String cutOffTime = eFmFmTripTimingMasterPO.getCut_Off_Time();

		Date shift = shiftFormate.parse(cutOffTime);
		java.sql.Time cut_Off_Time = new java.sql.Time(shift.getTime());

		Date cancelShift = shiftFormate.parse(eFmFmTripTimingMasterPO.getCancel_Cut_Off_Time());
		java.sql.Time CancelcutOffTime = new java.sql.Time(cancelShift.getTime());

		Date rescheduleCutOffTimeValue = shiftFormate.parse(eFmFmTripTimingMasterPO.getReschedule_Cut_Off_Time());
		java.sql.Time rescheduleCutOffTime = new java.sql.Time(rescheduleCutOffTimeValue.getTime());

		List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO.getParticularShiftTimeDetailByShiftId(
				eFmFmTripTimingMasterPO.geteFmFmClientBranchPO().getBranchId(), eFmFmTripTimingMasterPO.getShiftId());
		if (!(shiftTimeDetails.isEmpty())) {
			shiftTimeDetails.get(0).setCutOffTime(cut_Off_Time);
			shiftTimeDetails.get(0).setCancelCutoffTime(CancelcutOffTime);
			shiftTimeDetails.get(0).setRescheduleCutOffTime(rescheduleCutOffTime);
			shiftTimeDetails.get(0).setShiftBufferTime(eFmFmTripTimingMasterPO.getShiftBufferTime());
			shiftTimeDetails.get(0).setMobileVisibleFlg(eFmFmTripTimingMasterPO.getMobileVisibleFlg());
			shiftTimeDetails.get(0).setCeilingFlg(eFmFmTripTimingMasterPO.getCeilingFlg());
			shiftTimeDetails.get(0).setGenderPreference(eFmFmTripTimingMasterPO.getGenderPreference());
			shiftTimeDetails.get(0).setCeilingNo(eFmFmTripTimingMasterPO.getCeilingNo());
			shiftTimeDetails.get(0).setBufferCeilingNo(eFmFmTripTimingMasterPO.getBufferCeilingNo());
			iCabRequestBO.update(shiftTimeDetails.get(0));
			responce.put("status", "success");

		} else {
			responce.put("status", "failed");
		}
		log.info("serviceEnd -UserId :" + eFmFmTripTimingMasterPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param get
	 *            all Request Master Details On the client basis
	 * @return it will returns all the request details According to the trip
	 *         type
	 * @throws UnsupportedEncodingException
	 * 
	 */

	@POST
	@Path("/allrequestdetailtype")
	public Response getAllRequestDetailsByTripType(EFmFmEmployeeRequestMasterPO travelRequest)
			throws ParseException, UnsupportedEncodingException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + travelRequest.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					travelRequest.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(travelRequest.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmEmployeeRequestMasterPO> allRequestDetails = iCabRequestBO
				.getAllActiveRequestDetailsFromRequestMasterdByBranchIdAndTripType(travelRequest.getCombinedFacility(),
						travelRequest.getTripType());

		log.info("size" + allRequestDetails.size());

		List<Map<String, Object>> allRequestsDetails = new ArrayList<Map<String, Object>>();
		DateFormat shiftDateFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		for (EFmFmEmployeeRequestMasterPO requestMasterPO : allRequestDetails) {
			if (requestMasterPO.getTripRequestEndDate().getTime() > new Date().getTime()) {
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("userId", requestMasterPO.getEfmFmUserMaster().getUserId());
				requests.put("tripId", requestMasterPO.getTripId());
				requests.put("employeeId", requestMasterPO.getEfmFmUserMaster().getEmployeeId());
				if (requestMasterPO.getTripType().equalsIgnoreCase("PICKUP"))
					requests.put("pickupTime", requestMasterPO.getPickUpTime());
				else
					requests.put("pickupTime", requestMasterPO.getDropSequence());
				requests.put("requestType", requestMasterPO.getRequestType());
				requests.put("status", requestMasterPO.getStatus());

				requests.put("address", new String(
						Base64.getDecoder().decode(requestMasterPO.getEfmFmUserMaster().getAddress()), "utf-8"));
				requests.put("routeName",
						requestMasterPO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
				requests.put("areaName", requestMasterPO.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
				requests.put("requestDate", shiftDateFormater.format(requestMasterPO.getRequestDate()));
				requests.put("shiftTime", requestMasterPO.getShiftTime());
				requests.put("requestStartDate", shiftDateFormater.format(requestMasterPO.getTripRequestStartDate()));
				requests.put("requestEndDate", shiftDateFormater.format(requestMasterPO.getTripRequestEndDate()));
				allRequestsDetails.add(requests);
			}
		}
		log.info("serviceEnd -UserId :" + travelRequest.getUserId());
		return Response.ok(allRequestsDetails, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/employeeRequestDetails")
	public Response getEmployeeRequestDetails(EFmFmEmployeeRequestMasterPO travelRequest)
			throws ParseException, UnsupportedEncodingException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + travelRequest.getUserId());
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));

		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequest.getUserId()))){
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }}catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }

		List<EFmFmEmployeeRequestMasterPO> allRequestDetails = iCabRequestBO.getEmployeeRequestDetails(
				travelRequest.getCombinedFacility(),travelRequest.getUserId());
		List<Map<String, Object>> allRequestsDetails = new ArrayList<Map<String, Object>>();
		DateFormat shiftDateFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		for (EFmFmEmployeeRequestMasterPO requestMasterPO : allRequestDetails) {
			/*if (requestMasterPO.getTripRequestEndDate().getTime() > new Date().getTime()) {*/
				Map<String, Object> requests = new HashMap<String, Object>();
				requests.put("userId", requestMasterPO.getEfmFmUserMaster().getUserId());
				requests.put("tripId", requestMasterPO.getTripId());
				requests.put("facilityName", requestMasterPO.geteFmFmClientBranchPO().getBranchName());
				requests.put("facilityId", requestMasterPO.geteFmFmClientBranchPO().getBranchId());		
				requests.put("employeeId", requestMasterPO.getEfmFmUserMaster().getEmployeeId());
				if (requestMasterPO.getTripType().equalsIgnoreCase("PICKUP"))
					requests.put("pickupTime", requestMasterPO.getPickUpTime());
				else
					requests.put("pickupTime", requestMasterPO.getDropSequence());
				requests.put("requestType", requestMasterPO.getRequestType());
				requests.put("tripType", requestMasterPO.getTripType());
				requests.put("status", requestMasterPO.getStatus());

			requests.put("address",
					new String(Base64.getDecoder().decode(requestMasterPO.getEfmFmUserMaster().getAddress()), "utf-8"));
			requests.put("routeName", requestMasterPO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneName());
			requests.put("areaName", requestMasterPO.geteFmFmRouteAreaMapping().getEfmFmAreaMaster().getAreaName());
			requests.put("requestDate", shiftDateFormater.format(requestMasterPO.getRequestDate()));
			requests.put("shiftTime", requestMasterPO.getShiftTime());
			if (requestMasterPO.getLocationFlg() == null) {
				requests.put("locationFlg", "N");
			} else {
				requests.put("locationFlg", requestMasterPO.getLocationFlg());
			}
			try {
				if (requestMasterPO.getLocationFlg().equalsIgnoreCase("M")) {

					Map<String, Object> areaList = new CabRequestService().listOfPointsForMultipleLocation(
							requestMasterPO.getLocationWaypointsIds(),
							requestMasterPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
							requestMasterPO.getEfmFmUserMaster().getUserId(), travelRequest.getCombinedFacility());
					if (areaList.size() > 0) {
						requests.put("employeeWaypoints", areaList);
					} else {
						requests.put("employeeWaypoints", "NA");
					}
				}
			} catch (Exception e) {
				log.debug("location Details are not updated_Location_flg is null");
			}
			requests.put("requestStartDate", shiftDateFormater.format(requestMasterPO.getTripRequestStartDate()));
			requests.put("requestEndDate", shiftDateFormater.format(requestMasterPO.getTripRequestEndDate()));
			allRequestsDetails.add(requests);
			/* } */
		}
		log.info("serviceEnd -UserId :" + travelRequest.getUserId());
		return Response.ok(allRequestsDetails, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param get
	 *            all Request Master Details On the client basis
	 * @return it will returns all the request details
	 * 
	 */

	@POST
	@Path("/updaterequestdetail")
	public Response updateRequestMasterDetail(EFmFmEmployeeRequestMasterPO travelRequest) throws ParseException {
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + travelRequest.getUserId());
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));

		try {
			if (!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),
					travelRequest.getUserId()))) {
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}

			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(travelRequest.getUserId());
			if (!(userDetail.isEmpty())) {
				String jwtToken = "";
				try {
					JwtTokenGenerator token = new JwtTokenGenerator();
					jwtToken = token.generateToken();
					userDetail.get(0).setMobAuthorizationToken(jwtToken);
					userDetail.get(0).setMobTokenGenerationTime(new Date());
					userMasterBO.update(userDetail.get(0));
				} catch (Exception e) {
					log.info("error" + e);
				}
				responce.put("token", jwtToken);
			}

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmEmployeeRequestMasterPO> requestDetails = iCabRequestBO
				.getRequestFromRequestMaster(travelRequest.getTripId(), travelRequest.getCombinedFacility());
		requestDetails.get(0).setStatus(travelRequest.getStatus());
		requestDetails.get(0).setReadFlg(travelRequest.getReadFlg());
		iCabRequestBO.update(requestDetails.get(0));
		if (travelRequest.getStatus().equalsIgnoreCase("N")) {
			List<EFmFmEmployeeTravelRequestPO> currentRequestDetails = iCabRequestBO.deleteCurentRequestfromTraveldesk(
					travelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId(),
					travelRequest.getTripId());
			if (!(currentRequestDetails.isEmpty())) {
				for (EFmFmEmployeeTravelRequestPO employeeRequest : currentRequestDetails) {
					iCabRequestBO.deleteParticularRequest(employeeRequest.getRequestId());
				}
			}
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + travelRequest.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param update
	 *            eFmFmUserMaster employee details
	 * @return update employee from employee details
	 * @throws UnsupportedEncodingException
	 */

	@POST
	@Path("/updateempdetails")
	public Response updateEmployeeDetails(EFmFmUserMasterPO eFmFmUserMaster) throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		IRouteDetailBO routeDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
        IEmployeeDetailBO iEmployeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");

		Map<String, Object> responce = new HashMap<String, Object>();
		Map<String, Object> requests = new HashMap<String, Object>();
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + eFmFmUserMaster.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					eFmFmUserMaster.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMaster.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}

		// *** Client Input Validation Code ****
		String status = "Input";
		StringBuffer temp = new StringBuffer("PleaseCheck");

		if (true) {
			if (eFmFmUserMaster.getMobileNumber() == null || eFmFmUserMaster.getMobileNumber().equals("")) {
				temp.append("::Employee mobile number cannot not be empty");
				status = "Fail";
			} else {
				CharSequence empMobileNumb = eFmFmUserMaster.getMobileNumber();
				Matcher matcher = Validator.mobNumber(empMobileNumb);
				if (!matcher.matches() || eFmFmUserMaster.getMobileNumber().length() < 6
						|| eFmFmUserMaster.getMobileNumber().length() > 18) {
					temp.append("::Employee mobile number should have only numbers and range min 6 and max 18");
					status = "Fail";
				}
			}

			if (eFmFmUserMaster.getWeekOffDays() == null || eFmFmUserMaster.getWeekOffDays().equals("")) {
				// loggedInUserDetail.get(0).setWeekOffDays(eFmFmUserMaster.getWeekOffDays());
				temp.append("::Employee weekoffs cannot be empty");
				status = "Fail";
			}

			if (eFmFmUserMaster.getFirstName() == null || eFmFmUserMaster.getFirstName().equals("")) {
				temp.append("::Employee First name cannot be empty");
				status = "Fail";
			} else {
				CharSequence empFirstName = eFmFmUserMaster.getFirstName();
				Matcher matcher = Validator.alphaNumericSpaceDot(empFirstName);
				if (!matcher.matches() || eFmFmUserMaster.getFirstName().length() < 3
						|| eFmFmUserMaster.getFirstName().length() > 50) {
					temp.append(
							"::Employee first name should have only characters space, dot and range min 3 and max 50");
					status = "Fail";
				}
			}

			if (eFmFmUserMaster.getEmployeeDesignation() == null
					|| eFmFmUserMaster.getEmployeeDesignation().equals("")) {
				temp.append("::Employee designation cannot be empty");
				status = "Fail";
			} else {
				CharSequence empDesignation = eFmFmUserMaster.getEmployeeDesignation();
				Matcher matcher = Validator.alphaNumericSpaceDot(empDesignation);
				if (!matcher.matches() || eFmFmUserMaster.getEmployeeDesignation().length() > 100) {
					temp.append("::Employee designation should have only characters ");
					status = "Fail";
				}
			}

			if (eFmFmUserMaster.getAddress() == null || eFmFmUserMaster.getAddress().equals("")
					|| eFmFmUserMaster.getAddress().length() > 250) {
				temp.append("::Employee address cannot be empty and range max 250 letters");
				status = "Fail";
			}

			if (eFmFmUserMaster.getEmailId() == null || eFmFmUserMaster.getEmailId().equals("")
					|| eFmFmUserMaster.getEmailId().isEmpty()) {
				temp.append("::Employee email cannot be empty");
				status = "Fail";
			} else {
				CharSequence empEmail = eFmFmUserMaster.getEmailId();
				Matcher matcher = Validator.email(empEmail);
				if (!matcher.matches()) {
					temp.append("::Employee email should be valid");
					status = "Fail";
				}
			}

			if (eFmFmUserMaster.getEmployeeDepartment() == null || eFmFmUserMaster.getEmployeeDepartment().equals("")) {
				temp.append("::Employee Department cannot be empty");
				status = "Fail";
			} else {
				CharSequence empDepartment = eFmFmUserMaster.getEmployeeDepartment();
				Matcher matcher = Validator.alphaNumericSpaceDot(empDepartment);
				if (!matcher.matches() || eFmFmUserMaster.getEmployeeDepartment().length() > 50) {
					temp.append("::Employee Department can be alphanumeric only ");
					status = "Fail";
				}
			}
			if (eFmFmUserMaster.getLatitudeLongitude() == null || eFmFmUserMaster.getLatitudeLongitude().equals("")) {
				temp.append("::Employee Latitide and Longitude cannot be empty");
				status = "Fail";
			} else {
				CharSequence latiLongi = eFmFmUserMaster.getLatitudeLongitude();
				Matcher matcher = Validator.numbDotComma(latiLongi);
				if (!matcher.matches() || eFmFmUserMaster.getLatitudeLongitude().length() > 100) {
					temp.append("::Employee Latitude and Longitude should contain only numbers coma and dot");
					status = "Fail";
				}

			}

			if (eFmFmUserMaster.getPhysicallyChallenged() == null
					|| eFmFmUserMaster.getPhysicallyChallenged().equals("")) {
				temp.append("::Physically challenged field cannot be empty");
				status = "Fail";
			} else {
				CharSequence physicalChallenged = eFmFmUserMaster.getPhysicallyChallenged();
				Matcher matcher = Validator.yesOrNo(physicalChallenged);
				if (!matcher.matches()) {
					temp.append("::Physically challenged field Yes or no ");
					status = "Fail";
				}
			}

			if (eFmFmUserMaster.getIsInjured() == null || eFmFmUserMaster.getIsInjured().equals("")) {
				temp.append("::Injured field cannot be empty");
				status = "Fail";
			} else {
				CharSequence isInjured = eFmFmUserMaster.getIsInjured();
				Matcher matcher = Validator.yesOrNo(isInjured);
				if (!matcher.matches()) {
					temp.append("::Injured field Yes or no ");
					status = "Fail";
				}
			}

			if (eFmFmUserMaster.getIsVIP() == null || eFmFmUserMaster.getIsVIP().equals("")) {
				temp.append("::VIP field cannot be empty");
				status = "Fail";
			} else {
				CharSequence isInjured = eFmFmUserMaster.getIsVIP();
				Matcher matcher = Validator.yesOrNo(isInjured);
				if (!matcher.matches()) {
					temp.append("::VIP field YES or NO ");
					status = "Fail";
				}
			}

			if (status.equals("Fail")) {
				log.info("Invalid input:");
				requests.put("status", status);
				requests.put("inputInvalid", temp);
				return Response.ok(requests, MediaType.APPLICATION_JSON).build();
			}
		}
		log.info("valid input:");

		IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
				.getBean("IEmployeeDetailBO");
		log.info("Geo Code Updation inside updateempdetails" + eFmFmUserMaster.getLatitudeLongitude());
		log.info("AreaId" + eFmFmUserMaster.getAreaId() + "branch" + eFmFmUserMaster.getBranchId() + "RouteId"
				+ eFmFmUserMaster.getRouteId() + "nodalPoint" + eFmFmUserMaster.getNodalPointId());

		List<EFmFmZoneMasterPO> routeExistDetail = routeDetailBO
				.getParticularRouteFromRouteId(eFmFmUserMaster.getRouteId());
		if (!(routeExistDetail.isEmpty()) && !(routeExistDetail.get(0).isNodalRoute())
				&& eFmFmUserMaster.getNodalPointId() != 1) {
			requests.put("status", "changeNodalPoint");
			log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
		}
		if (!(routeExistDetail.isEmpty()) && routeExistDetail.get(0).isNodalRoute()
				&& eFmFmUserMaster.getNodalPointId() == 1) {
			requests.put("status", "changeToDefault");
			log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();
		}

		List<EFmFmRouteAreaMappingPO> assignRouteDetails = routeDetailBO
				.getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(eFmFmUserMaster.getAreaId(),
						eFmFmUserMaster.getCombinedFacility(), eFmFmUserMaster.getRouteId(),
						eFmFmUserMaster.getNodalPointId());
		EFmFmClientBranchPO eFmFmClientBranchPO=new EFmFmClientBranchPO();
		eFmFmClientBranchPO.setBranchId(Integer.parseInt(eFmFmUserMaster.getCombinedFacility()));
		log.info("Integer.parseInt(eFmFmUserMaster.getCombinedFacility())"+Integer.parseInt(eFmFmUserMaster.getCombinedFacility()));
		if (assignRouteDetails.isEmpty()) {
			EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO = new EFmFmRouteAreaMappingPO();
			EFmFmZoneMasterPO eFmFmZoneMaster = new EFmFmZoneMasterPO();
			eFmFmZoneMaster.setZoneId(eFmFmUserMaster.getRouteId());
			EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO=new EFmFmClientRouteMappingPO();
			eFmFmClientRouteMappingPO.seteFmFmZoneMaster(eFmFmZoneMaster);
			eFmFmClientRouteMappingPO.setCombinedFacility(eFmFmUserMaster.getCombinedFacility());

			List<EFmFmClientRouteMappingPO> routeClientExistCheck=routeDetailBO.clientRouteMappaingAlreadyExist(eFmFmClientRouteMappingPO);
			if(routeClientExistCheck.isEmpty()){
				eFmFmClientRouteMappingPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
				routeDetailBO.saveClientRouteMapping(eFmFmClientRouteMappingPO);
			}
		
			EFmFmAreaMasterPO efmFmAreaMaster = new EFmFmAreaMasterPO();
			efmFmAreaMaster.setAreaId(eFmFmUserMaster.getAreaId());
	 	     List<EFmFmClientAreaMappingPO> eFmFmClientAreaMappingPO=routeDetailBO.getClientAreaMappaingData(eFmFmUserMaster.getAreaId(),Integer.parseInt(eFmFmUserMaster.getCombinedFacility()));
	 	    	if(eFmFmClientAreaMappingPO.isEmpty()){
			 	      EFmFmClientAreaMappingPO clientAreaMapping=new EFmFmClientAreaMappingPO();
				 	  clientAreaMapping.seteFmFmAreaMaster(efmFmAreaMaster);
				 	  clientAreaMapping.setFacilityWiseDistance(0);
				 	  clientAreaMapping.seteFmFmClientBranchPO(eFmFmClientBranchPO);
				 	 routeDetailBO.save(clientAreaMapping);			 	
	 	    	}			 	    		 
			EFmFmAreaNodalMasterPO eFmFmNodalAreaMaster = new EFmFmAreaNodalMasterPO();
			eFmFmNodalAreaMaster.setNodalPointId(eFmFmUserMaster.getNodalPointId());
			eFmFmRouteAreaMappingPO.setEfmFmAreaMaster(efmFmAreaMaster);
			eFmFmRouteAreaMappingPO.seteFmFmZoneMaster(eFmFmZoneMaster);
			eFmFmRouteAreaMappingPO.seteFmFmNodalAreaMaster(eFmFmNodalAreaMaster);			
			routeDetailBO.save(eFmFmRouteAreaMappingPO);
			assignRouteDetails = routeDetailBO.getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(
					eFmFmUserMaster.getAreaId(), eFmFmUserMaster.getCombinedFacility(), eFmFmUserMaster.getRouteId(),
					eFmFmUserMaster.getNodalPointId());
		}
		List<EFmFmUserPasswordPO> userPassDetail=iEmployeeDetailBO.getUserPasswordDetailsFromUserIdAndBranchId(eFmFmUserMaster.getSelectedEmployeeUserId());
    	if(!(userPassDetail.isEmpty())){
    		for(EFmFmUserPasswordPO pass:userPassDetail){
    			pass.seteFmFmClientBranchPO(eFmFmClientBranchPO);
    			iEmployeeDetailBO.update(pass);
    		}
    	}
    List<EFmFmClientUserRolePO> roleMasterPO = userMasterBO.getUserRolesFromUserIdAndBranchId(eFmFmUserMaster.getSelectedEmployeeUserId());
	if(!(roleMasterPO.isEmpty())){
    		for(EFmFmClientUserRolePO roleDetail:roleMasterPO){
    			roleDetail.seteFmFmClientBranchPO(eFmFmClientBranchPO);
    			userMasterBO.update(roleDetail);
    		}
    	}
		
		List<EFmFmUserMasterPO> loggedInUserDetail = userMasterBO
				.getUserDetailFromUserId(eFmFmUserMaster.getSelectedEmployeeUserId());
		if (!(loggedInUserDetail.isEmpty())) {
			if (!(loggedInUserDetail.get(0).getMobileNumber().equalsIgnoreCase(
					Base64.getEncoder().encodeToString(eFmFmUserMaster.getMobileNumber().getBytes("utf-8"))))) {
				List<EFmFmUserMasterPO> userDetailsByMobileNum = employeeDetailBO
						.getParticularEmpDetailsFromMobileNumber(Base64.getEncoder()
								.encodeToString(eFmFmUserMaster.getMobileNumber().getBytes("utf-8")));
				if (!(userDetailsByMobileNum.isEmpty())) {
					requests.put("status", "mExist");
					requests.put("employeeId", userDetailsByMobileNum.get(0).getEmployeeId());
					log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				}
			}
			if (!(loggedInUserDetail.get(0).getEmailId().equalsIgnoreCase(
					Base64.getEncoder().encodeToString(eFmFmUserMaster.getEmailId().getBytes("utf-8"))))) {
				List<EFmFmUserMasterPO> employeeEmailId = employeeDetailBO.getParticularEmployeeDetailsFromEmailId(
						Base64.getEncoder().encodeToString(eFmFmUserMaster.getEmailId().getBytes("utf-8")));
				if (!(employeeEmailId.isEmpty())) {
					requests.put("status", "eExist");
					requests.put("employeeId", employeeEmailId.get(0).getEmployeeId());
					log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				}
			}
			
			if (!(loggedInUserDetail.get(0).getUserName().equalsIgnoreCase(eFmFmUserMaster.getUserName()))) {
				List<EFmFmUserMasterPO> userDetails= employeeDetailBO.getParticularEmployeeDetailsFromUserName(eFmFmUserMaster.getUserName());
				if (!(userDetails.isEmpty())) {
					requests.put("status", "uExist");
					requests.put("employeeId", userDetails.get(0).getEmployeeId());
					log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
					return Response.ok(requests, MediaType.APPLICATION_JSON).build();
				}
			}
			loggedInUserDetail.get(0).setMobileNumber(
					Base64.getEncoder().encodeToString(eFmFmUserMaster.getMobileNumber().getBytes("utf-8")));
			loggedInUserDetail.get(0).setWeekOffDays(eFmFmUserMaster.getWeekOffDays());
			if (Integer.valueOf(eFmFmUserMaster.getGender()) == 1) {
				loggedInUserDetail.get(0).setGender(Base64.getEncoder().encodeToString("Male".getBytes("utf-8")));
			} else {
				loggedInUserDetail.get(0).setGender(Base64.getEncoder().encodeToString("Female".getBytes("utf-8")));
			}
			loggedInUserDetail.get(0)
					.setFirstName(Base64.getEncoder().encodeToString(eFmFmUserMaster.getFirstName().getBytes("utf-8")));
			loggedInUserDetail.get(0)
					.setAddress(Base64.getEncoder().encodeToString(eFmFmUserMaster.getAddress().getBytes("utf-8")));
			loggedInUserDetail.get(0).setEmployeeDesignation(
					Base64.getEncoder().encodeToString(eFmFmUserMaster.getEmployeeDesignation().getBytes("utf-8")));
			loggedInUserDetail.get(0)
					.setEmailId(Base64.getEncoder().encodeToString(eFmFmUserMaster.getEmailId().getBytes("utf-8")));
			loggedInUserDetail.get(0).setEmployeeDepartment(
					Base64.getEncoder().encodeToString(eFmFmUserMaster.getEmployeeDepartment().getBytes("utf-8")));
			try {
				if (!(eFmFmUserMaster.getLatitudeLongitude() == null
						|| eFmFmUserMaster.getLatitudeLongitude().equalsIgnoreCase("null"))) {
					if (!(eFmFmUserMaster.getLatitudeLongitude().toString().trim().replaceAll("\\s+", "")
							.equalsIgnoreCase(loggedInUserDetail.get(0).getLatitudeLongitude()))) {
						try {
							CalculateDistance empDistance = new CalculateDistance();
							loggedInUserDetail.get(0)
									.setDistance(empDistance.employeeDistanceCalculation(loggedInUserDetail.get(0)
											.geteFmFmClientBranchPO().getLatitudeLongitude(),
									eFmFmUserMaster.getLatitudeLongitude().toString().trim().replaceAll("\\s+", "")));
						} catch (Exception e) {
							log.info("Distance update calculation error" + e);
						}
					}
					loggedInUserDetail.get(0).setLatitudeLongitude(
							eFmFmUserMaster.getLatitudeLongitude().toString().trim().replaceAll("\\s+", ""));
					loggedInUserDetail.get(0).setGeoCodedAddress(eFmFmUserMaster.getGeoCodedAddress());
					
				}
			} catch (Exception e) {
				if (!(eFmFmUserMaster.getLatitudeLongitude() == null
						|| eFmFmUserMaster.getLatitudeLongitude().equalsIgnoreCase("null"))) {
					loggedInUserDetail.get(0).setLatitudeLongitude(
							eFmFmUserMaster.getLatitudeLongitude().toString().trim().replaceAll("\\s+", ""));
				}
				log.info("geocode error " + e);
			}
			loggedInUserDetail.get(0).setHostMobileNumber(
					Base64.getEncoder().encodeToString(eFmFmUserMaster.getHostMobileNumber().getBytes("utf-8")));

			loggedInUserDetail.get(0).setPhysicallyChallenged(
					Base64.getEncoder().encodeToString(eFmFmUserMaster.getPhysicallyChallenged().getBytes("utf-8")));
			loggedInUserDetail.get(0)
					.setIsInjured(Base64.getEncoder().encodeToString(eFmFmUserMaster.getIsInjured().getBytes("utf-8")));

			loggedInUserDetail.get(0)
					.setIsVIP(Base64.getEncoder().encodeToString(eFmFmUserMaster.getIsVIP().getBytes("utf-8")));
			loggedInUserDetail.get(0).setPragnentLady(
					Base64.getEncoder().encodeToString(eFmFmUserMaster.getPragnentLady().getBytes("utf-8")));
			loggedInUserDetail.get(0).seteFmFmRouteAreaMapping(assignRouteDetails.get(0));
			
			loggedInUserDetail.get(0).setTripType(eFmFmUserMaster.getTripType());
			loggedInUserDetail.get(0).setUserName(eFmFmUserMaster.getUserName());

			loggedInUserDetail.get(0).seteFmFmClientBranchPO(eFmFmClientBranchPO);
			userMasterBO.update(loggedInUserDetail.get(0));
		}
		requests.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param update
	 *            EFmFmEmployeeRequestMasterPO employee details
	 * @return update EFmFmEmployeeRequestMasterPO from employee details
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */

	@POST
	@Path("/updateMasterRequestDetails")
	public Response updateEmployeeRequestDetails(EFmFmEmployeeRequestMasterPO requestDetails)
			throws UnsupportedEncodingException, ParseException {
		IRouteDetailBO routeDetailBO = (IRouteDetailBO) ContextLoader.getContext().getBean("IRouteDetailBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + requestDetails.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					requestDetails.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(requestDetails.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		DateFormat shiftFormate = new SimpleDateFormat("HH:mm");
		Map<String, Object> requests = new HashMap<String, Object>();
		List<EFmFmEmployeeRequestMasterPO> masterRequestDetails = iCabRequestBO
				.getParticularEmployeeMasterRequestDetails(requestDetails.getBranchId(), requestDetails.getTripId());
		log.info("EFmFmEmployeeRequestMasterPO size" + masterRequestDetails.size());
		if (!(masterRequestDetails.isEmpty())) {
			List<EFmFmAreaMasterPO> areaId = routeDetailBO.getParticularAreaNameDetails(requestDetails.getAreaName());
			List<EFmFmZoneMasterPO> routeId = routeDetailBO.getAllRouteName(requestDetails.getRouteName().trim());
			List<EFmFmRouteAreaMappingPO> assignRouteDetails = routeDetailBO
					.getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(areaId.get(0).getAreaId(),
							requestDetails.getCombinedFacility(), routeId.get(0).getZoneId(), 1);
			if (assignRouteDetails.isEmpty()) {
				EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO = new EFmFmRouteAreaMappingPO();
				EFmFmAreaNodalMasterPO eFmFmNodalAreaMaster = new EFmFmAreaNodalMasterPO();
				eFmFmNodalAreaMaster.setNodalPointId(1);
				eFmFmRouteAreaMappingPO.setEfmFmAreaMaster(areaId.get(0));
				eFmFmRouteAreaMappingPO.seteFmFmZoneMaster(routeId.get(0));
				eFmFmRouteAreaMappingPO.seteFmFmNodalAreaMaster(eFmFmNodalAreaMaster);
				routeDetailBO.save(eFmFmRouteAreaMappingPO);
				assignRouteDetails = routeDetailBO.getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(
						areaId.get(0).getAreaId(), requestDetails.getCombinedFacility(), routeId.get(0).getZoneId(), 1);
			}
			if (masterRequestDetails.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
				String pickTime = requestDetails.getTime();
				Date pickUpTime = shiftFormate.parse(pickTime);
				java.sql.Time picTime = new java.sql.Time(pickUpTime.getTime());
				masterRequestDetails.get(0).setPickUpTime(picTime);
			} else {
				masterRequestDetails.get(0).setDropSequence(Integer.parseInt(requestDetails.getTime()));
			}
			masterRequestDetails.get(0).seteFmFmRouteAreaMapping(assignRouteDetails.get(0));
			iCabRequestBO.update(masterRequestDetails.get(0));
		}

		requests.put("status", "success");
		log.info("serviceEnd -UserId :" + requestDetails.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * @param update
	 *            eFmFmUserMaster employee lattitude and longitude
	 * @return update employee from employee table
	 */

	@POST
	@Path("/updateEmpGeoCodes")
	public Response updateEmployeeGeoCodesFromDevice(EFmFmUserMasterPO eFmFmUserMaster) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + eFmFmUserMaster.getUserId());
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));

//		try {
//			if (!(userMasterBO.checkTokenValidOrNotForMobile(httpRequest.getHeader("authenticationToken"),
//					eFmFmUserMaster.getUserId()))) {
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
//
//			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmUserMaster.getUserId());
//			if (!(userDetail.isEmpty())) {
//				String jwtToken = "";
//				try {
//					JwtTokenGenerator token = new JwtTokenGenerator();
//					jwtToken = token.generateToken();
//					userDetail.get(0).setMobAuthorizationToken(jwtToken);
//					userDetail.get(0).setMobTokenGenerationTime(new Date());
//					userMasterBO.update(userDetail.get(0));
//				} catch (Exception e) {
//					log.info("error" + e);
//				}
//				responce.put("token", jwtToken);
//			}
//
//		} catch (Exception e) {
//			log.info("authentication error" + e);
//			responce.put("status", "invalidRequest");
//			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//		}

		try {
			List<EFmFmUserMasterPO> loggedInUserDetail = userMasterBO.getEmployeeUserDetailFromEmployeeId(
					eFmFmUserMaster.getBranchId(), eFmFmUserMaster.getEmployeeId());
			if (!(loggedInUserDetail.isEmpty())) {
				try {
					if (!(eFmFmUserMaster.getLatitudeLongitude() == null
							|| eFmFmUserMaster.getLatitudeLongitude().equalsIgnoreCase("null"))) {
						if (!(eFmFmUserMaster.getLatitudeLongitude().toString().trim().replaceAll("\\s+", "")
								.equalsIgnoreCase(loggedInUserDetail.get(0).getLatitudeLongitude()))) {
							try {
								CalculateDistance empDistance = new CalculateDistance();
								loggedInUserDetail.get(0)
										.setDistance(empDistance.employeeDistanceCalculation(
												loggedInUserDetail.get(0).geteFmFmClientBranchPO()
														.getLatitudeLongitude(),
												eFmFmUserMaster.getLatitudeLongitude().toString().trim()
														.replaceAll("\\s+", "")));
							} catch (Exception e) {
								log.info("Distance update geode caoturing via geo code calculation error" + e);
							}
						}
						loggedInUserDetail.get(0).setLatitudeLongitude(
								eFmFmUserMaster.getLatitudeLongitude().toString().trim().replaceAll("\\s+", ""));
					}
				} catch (Exception e) {
					if (!(eFmFmUserMaster.getLatitudeLongitude() == null
							|| eFmFmUserMaster.getLatitudeLongitude().equalsIgnoreCase("null"))) {
						loggedInUserDetail.get(0).setLatitudeLongitude(
								eFmFmUserMaster.getLatitudeLongitude().toString().trim().replaceAll("\\s+", ""));
					}
					log.info("geocode error " + e);
				}

				// loggedInUserDetail.get(0).setLatitudeLongitude(eFmFmUserMaster.getLatitudeLongitude());
				loggedInUserDetail.get(0).setLocationStatus("Y");
				userMasterBO.update(loggedInUserDetail.get(0));
			}
		} catch (Exception e) {
			responce.put("status", "fail");
			log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	public static byte[] getBytes(InputStream is) throws IOException {
		int len;
		int size = 1024;
		byte[] buf;

		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			System.out.println("fun" + size);
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
	
	
	
	
	/**
	 * 
	 * @param update
	 *            eFmFmUserMaster employee lattitude and longitude
	 * @return update employee from employee table
	 */

	@POST
	@Path("/findEmpGeoCodesDiffAndUpdate")
	public Response findEmpGeoCodesDiffAndUpdate(EFmFmUserMasterPO eFmFmUserMaster) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + eFmFmUserMaster.getUserId());
		try {
			List<EFmFmUserMasterPO> loggedInUserDetail = userMasterBO.getEmployeeUserDetailFromEmployeeId(eFmFmUserMaster.getBranchId(),eFmFmUserMaster.getEmployeeId());
			if (!(loggedInUserDetail.isEmpty())) {
				try {						
							if (!(eFmFmUserMaster.getLatitudeLongitude() == null
									|| eFmFmUserMaster.getLatitudeLongitude().equalsIgnoreCase("null"))) {
								if (!(eFmFmUserMaster.getLatitudeLongitude().toString().trim().replaceAll("\\s+", "")
										.equalsIgnoreCase(loggedInUserDetail.get(0).getLatitudeLongitude()))) {
									try {
										CalculateDistance empDistance = new CalculateDistance();
										loggedInUserDetail.get(0)
												.setDistance(empDistance.employeeDistanceCalculation(
														loggedInUserDetail.get(0).geteFmFmClientBranchPO()
																.getLatitudeLongitude(),
														eFmFmUserMaster.getLatitudeLongitude().toString().trim()
																.replaceAll("\\s+", "")));								
										if(eFmFmUserMaster.getGeoCodeVariationDistance()==0){											
											double diffDistance=empDistance.employeeDistanceCalculation(eFmFmUserMaster.getLatitudeLongitude().toString().trim().replaceAll("\\s+", ""), loggedInUserDetail.get(0).getHomeGeoCodePoints().toString().trim().replaceAll("\\s+", ""));
											loggedInUserDetail.get(0).setGeoCodeVariationDistance(diffDistance/1000);									
											if((diffDistance/1000)>1){
												responce.put("status", "geoCodeWarn");
												responce.put("geoCodeWarnDist",diffDistance/1000);
												log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
												return Response.ok(responce, MediaType.APPLICATION_JSON).build();	
											}											
										}else{
											loggedInUserDetail.get(0).setGeoCodeVariationDistance(eFmFmUserMaster.getGeoCodeVariationDistance());
										}
									 }catch (Exception e) {
										log.info("Distance update geode caoturing via geo code calculation error" + e);
									}
								}
						   }						
					if(null !=eFmFmUserMaster.getGeoCodedAddress()){
							loggedInUserDetail.get(0).setGeoCodedAddress(Base64.getEncoder().encodeToString(eFmFmUserMaster.getGeoCodedAddress().getBytes("utf-8")));
							loggedInUserDetail.get(0).setHomeGeoCodePoints(eFmFmUserMaster.getLatitudeLongitude().toString().trim().replaceAll("\\s+", ""));
					}
					loggedInUserDetail.get(0).setLatitudeLongitude(eFmFmUserMaster.getLatitudeLongitude().toString().trim().replaceAll("\\s+", ""));			
				} catch (Exception e) {
					if (!(eFmFmUserMaster.getLatitudeLongitude() == null
							|| eFmFmUserMaster.getLatitudeLongitude().equalsIgnoreCase("null"))) {
						loggedInUserDetail.get(0).setLatitudeLongitude(
								eFmFmUserMaster.getLatitudeLongitude().toString().trim().replaceAll("\\s+", ""));
					}
					log.info("geocode error " + e);
				}

				// loggedInUserDetail.get(0).setLatitudeLongitude(eFmFmUserMaster.getLatitudeLongitude());
				loggedInUserDetail.get(0).setLocationStatus("Y");
				userMasterBO.update(loggedInUserDetail.get(0));
			}
		} catch (Exception e) {
			responce.put("status", "fail");
			e.printStackTrace();
			log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmUserMaster.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}	
	

	/**
	 * 
	 * @param users
	 *            listOfBranchDetails Details details
	 * @return it will returns branch details
	 * 
	 */

	@POST
	@Path("/listOfBranchDetails")
	public Response getListOfBranchDetails(EFmFmClientBranchPO clientDetails) {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();

		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		log.info("serviceStart -UserId :" + clientDetails.getUserId());
		try {
			if (!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),
					clientDetails.getUserId()))) {

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(clientDetails.getUserId());
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

		} catch (Exception e) {
			log.info("authentication error" + e);
			responce.put("status", "invalidRequest");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		List<EFmFmClientBranchPO> particularBranchDetails = userMasterBO
				.getClientDetails(String.valueOf(clientDetails.getBranchId()));
		Map<String, Object> requests = new HashMap<String, Object>();
		DateFormat shiftDateFormater = new SimpleDateFormat("yyyy-MM-dd");

		requests.put("managerApproval", particularBranchDetails.get(0).getMangerApprovalRequired());
		requests.put("tripType", particularBranchDetails.get(0).getTripType());
		requests.put("cutOffTimeFlg", particularBranchDetails.get(0).getCutOffTime());
		if (particularBranchDetails.get(0).getRequestCutOffDate() != null) {
			requests.put("requestToDateCutOff",
					shiftDateFormater.format(particularBranchDetails.get(0).getRequestCutOffDate()));
		} else {
			requests.put("requestToDateCutOff", "NA");
		}
		if (particularBranchDetails.get(0).getRequestCutOffFromDate() != null) {
			requests.put("requestDateCutOffFromDate",
					shiftDateFormater.format(particularBranchDetails.get(0).getRequestCutOffFromDate()));
		} else {
			requests.put("requestDateCutOffFromDate", "NA");
		}
		requests.put("requestCutOffNoOfDays", particularBranchDetails.get(0).getRequestCutOffNoOfDays());
		requests.put("requestType", particularBranchDetails.get(0).getRequestType());
		requests.put("earlyRequestDate", particularBranchDetails.get(0).getEarlyRequestDate());
		requests.put("monthOrDays", particularBranchDetails.get(0).getMonthOrDays());
		requests.put("occurrenceFlg", particularBranchDetails.get(0).getOccurrenceFlg());
		requests.put("daysRequestByString", particularBranchDetails.get(0).getDaysRequest());
		if (particularBranchDetails.get(0).getDaysRequest().equalsIgnoreCase("All")
				|| particularBranchDetails.get(0).getDaysRequest().trim().equalsIgnoreCase("")
				|| particularBranchDetails.get(0).getDaysRequest() == null) {
			requests.put("daysRequest", 0);
		} else {
			requests.put("daysRequest", Integer.parseInt(particularBranchDetails.get(0).getDaysRequest()));
		}
		requests.put("locationVisible", particularBranchDetails.get(0).getLocationVisible());
		requests.put("destinationPointLimit", particularBranchDetails.get(0).getDestinationPointLimit());
		requests.put("requestWithProject", particularBranchDetails.get(0).getRequestWithProject());
		requests.put("approvalProcess", particularBranchDetails.get(0).getApprovalProcess());
		requests.put("postApproval", particularBranchDetails.get(0).getPostApproval());
		requests.put("shiftTimeGenderPreference", particularBranchDetails.get(0).getShiftTimeGenderPreference());
		requests.put("minimumDestCount", particularBranchDetails.get(0).getMinimumDestCount());
		requests.put("managerReqCreateProcess", particularBranchDetails.get(0).getManagerReqCreateProcess());
		requests.put("status", "success");
		log.info("serviceEnd -UserId :" + clientDetails.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}
}
