package com.newtglobal.eFmFmFleet.services;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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

import com.newtglobal.eFmFmFleet.business.bo.IEmployeeDetailBO;
import com.newtglobal.eFmFmFleet.business.bo.IFacilityBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFacilityToFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/facility")
@Consumes("application/json")
@Produces("application/json")
public class FacilityService {

	private static Log log = LogFactory.getLog(FacilityService.class);

	@Context
	private HttpServletRequest httpRequest;
	JwtTokenGenerator token = new JwtTokenGenerator();

	/**
	 * 
	 * get Facility details
	 * 
	 * @author Sarfraz Khan
	 * 
	 * @since 2017-05-26
	 * 
	 * @return success/failure details.
	 */

	@POST
	@Path("/facilityDetails")
	public Response getFacilityDetail(EFmFmFacilityToFacilityMappingPO eFmFmFacilityToFacilityMappingPO) {
		log.info("serviceStart -UserId :" + eFmFmFacilityToFacilityMappingPO.getUserId());
		IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
		// Map<String, Object> responce = new HashMap<String, Object>();
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));

		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }}catch(Exception e){
		// log.info("authenticationToken error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
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
		List<Map<String, Object>> attacheFacility = new ArrayList<Map<String, Object>>();
		List<EFmFmFacilityToFacilityMappingPO> facilityDetails = facilityBO.getAllActiveFacilities();
		log.info("facilityDetails" + facilityDetails.size());
		if (!(facilityDetails.isEmpty())) {
			for (EFmFmFacilityToFacilityMappingPO facility : facilityDetails) {
				Map<String, Object> responce = new HashMap<String, Object>();
				responce.put("branchId", facility.geteFmFmClientBranchPO().getBranchId());
				responce.put("branchName", facility.getBranchName());
				responce.put("branchType", facility.getBranchType());
				responce.put("facilityToFacilityId", facility.getFacilityToFacilityId());
				if (facility.getFacilityStatus().equalsIgnoreCase("Y")) {
					responce.put("ticked", true);
				} else {
					responce.put("ticked", false);
				}
				attacheFacility.add(responce);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmFacilityToFacilityMappingPO.getUserId());
		return Response.ok(attacheFacility, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * Attache The Facility details
	 * 
	 * @author Sarfraz Khan
	 * 
	 * @since 2017-05-29
	 * 
	 * @return success/failure details.
	 */

	@POST
	@Path("/updateFacility")
	public Response updateFacilityDetail(EFmFmFacilityToFacilityMappingPO eFmFmFacilityToFacilityMappingPO) {
		log.info("serviceStart -UserId :" + eFmFmFacilityToFacilityMappingPO.getUserId());
		IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));

		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }}catch(Exception e){
		// log.info("authenticationToken error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
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

		StringTokenizer token = new StringTokenizer(eFmFmFacilityToFacilityMappingPO.getBranchName(), ",");
		while (token.hasMoreElements()) {
			String branchName = (String) token.nextElement();
			List<EFmFmFacilityToFacilityMappingPO> facilityDetails = facilityBO
					.getParticularFacilityDetailFromBranchName(branchName);
			if (!(facilityDetails.isEmpty())) {
				// if(facilityDetails.get(0).geteFmFmClientBranchPO().getBranchId()==eFmFmFacilityToFacilityMappingPO.getBranchId()){
				// responce.put("status", "fail");
				// log.info("serviceEnd -UserId :" +
				// eFmFmFacilityToFacilityMappingPO.getUserId());
				// return Response.ok(responce,
				// MediaType.APPLICATION_JSON).build();
				// }
				EFmFmClientBranchPO branchDeatis = new EFmFmClientBranchPO();
				branchDeatis.setBranchId(eFmFmFacilityToFacilityMappingPO.getBranchId());
				facilityDetails.get(0).seteFmFmClientBranchPO(branchDeatis);
				facilityDetails.get(0).setFacilityStatus("Y");
				facilityBO.update(facilityDetails.get(0));
			} else {
				responce.put("status", "fNotExist");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmFacilityToFacilityMappingPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * Get all Inactive Facilities details
	 * 
	 * @author Sarfraz Khan
	 * 
	 * @since 2017-05-29
	 * 
	 * @return success/failure details.
	 */

	@POST
	@Path("/inActiveFacilities")
	public Response getAllInactiveFacilitiesDetails(EFmFmFacilityToFacilityMappingPO eFmFmFacilityToFacilityMappingPO) {
		log.info("serviceStart -UserId :" + eFmFmFacilityToFacilityMappingPO.getUserId());
		IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
		// Map<String, Object> responce = new HashMap<String, Object>();
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }}catch(Exception e){
		// log.info("authenticationToken error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
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

		List<Map<String, Object>> attacheFacility = new ArrayList<Map<String, Object>>();
		List<EFmFmFacilityToFacilityMappingPO> facilityDetails = facilityBO.getAllInactiveFacilities();
		log.info("facilityDetails" + facilityDetails.size());
		if (!(facilityDetails.isEmpty())) {
			for (EFmFmFacilityToFacilityMappingPO facility : facilityDetails) {
				Map<String, Object> responce = new HashMap<String, Object>();
				responce.put("branchId", facility.geteFmFmClientBranchPO().getBranchId());
				responce.put("branchName", facility.getBranchName());
				responce.put("branchType", facility.getBranchType());
				attacheFacility.add(responce);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmFacilityToFacilityMappingPO.getUserId());
		return Response.ok(attacheFacility, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * disable Facility details
	 * 
	 * @author Sarfraz Khan
	 * 
	 * @since 2017-05-26
	 * 
	 * @return success/failure details.
	 */

	@POST
	@Path("/disableFacility")
	public Response disableParticularFacility(EFmFmFacilityToFacilityMappingPO eFmFmFacilityToFacilityMappingPO) {
		log.info("serviceStart -UserId :" + eFmFmFacilityToFacilityMappingPO.getUserId());
		IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));

		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }}catch(Exception e){
		// log.info("authenticationToken error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
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

		List<EFmFmFacilityToFacilityMappingPO> facilityDetails = facilityBO
				.getParticularFacilityDetail(eFmFmFacilityToFacilityMappingPO.getFacilityToFacilityId());
		if (!(facilityDetails.isEmpty())) {
			facilityDetails.get(0).setFacilityStatus("N");
			facilityBO.update(facilityDetails.get(0));
		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmFacilityToFacilityMappingPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * edit Facility details
	 * 
	 * @author Sarfraz Khan
	 * 
	 * @since 2017-05-26
	 * 
	 * @return success/failure details.
	 */

	@POST
	@Path("/editFacilityDetails")
	public Response getInActiveFacilityDetail(EFmFmFacilityToFacilityMappingPO eFmFmFacilityToFacilityMappingPO) {
		log.info("serviceStart -UserId :" + eFmFmFacilityToFacilityMappingPO.getUserId());
		IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
		// Map<String, Object> responce = new HashMap<String, Object>();
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));

		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }}catch(Exception e){
		// log.info("authenticationToken error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
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
		List<Map<String, Object>> attacheFacility = new ArrayList<Map<String, Object>>();
		List<EFmFmFacilityToFacilityMappingPO> facilityDetails = facilityBO.getAllActiveFacilities();
		log.info("facilityDetails" + facilityDetails.size());
		if (!(facilityDetails.isEmpty())) {
			for (EFmFmFacilityToFacilityMappingPO facility : facilityDetails) {
				Map<String, Object> responce = new HashMap<String, Object>();
				responce.put("branchId", facility.geteFmFmClientBranchPO().getBranchId());
				responce.put("branchName", facility.getBranchName());
				responce.put("branchType", facility.getBranchType());
				responce.put("facilityToFacilityId", facility.getFacilityToFacilityId());

				if (facility.getFacilityStatus().equalsIgnoreCase("Y")) {
					responce.put("ticked", true);
				} else {
					responce.put("ticked", false);
				}
				attacheFacility.add(responce);
			}
		}
		log.info("serviceEnd -UserId :" + eFmFmFacilityToFacilityMappingPO.getUserId());
		return Response.ok(attacheFacility, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * update get Facility details
	 * 
	 * @author Sarfraz Khan
	 * 
	 * @since 2017-05-26
	 * 
	 * @return success/failure details.
	 */

	// @POST
	// @Path("/updateFacilityDetails")
	// public Response combileFacilityDetail(EFmFmFacilityToFacilityMappingPO
	// eFmFmFacilityToFacilityMappingPO){
	// log.info("serviceStart -UserId :" +
	// eFmFmFacilityToFacilityMappingPO.getUserId());
	// IFacilityBO facilityBO = (IFacilityBO)
	// ContextLoader.getContext().getBean("IFacilityBO");
	// Map<String, Object> responce = new HashMap<String, Object>();
	// log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
	//
	//// try{
	//// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){
	//// responce.put("status", "invalidRequest");
	//// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	//// }}catch(Exception e){
	//// log.info("authenticationToken error"+e);
	//// responce.put("status", "invalidRequest");
	//// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	//// }
	//// List<EFmFmUserMasterPO> userDetail =
	// userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
	//// if (!(userDetail.isEmpty())) {
	//// String jwtToken = "";
	//// try {
	//// JwtTokenGenerator token = new JwtTokenGenerator();
	//// jwtToken = token.generateToken();
	//// userDetail.get(0).setAuthorizationToken(jwtToken);
	//// userDetail.get(0).setTokenGenerationTime(new Date());
	//// userMasterBO.update(userDetail.get(0));
	//// } catch (Exception e) {
	//// log.info("error" + e);
	//// }
	//// }
	//
	// StringTokenizer token = new
	// StringTokenizer(eFmFmFacilityToFacilityMappingPO.getBranchName(),",");
	// EFmFmClientBranchPO eFmFmClientBranchPO =new EFmFmClientBranchPO();
	// eFmFmClientBranchPO.setBranchId(eFmFmFacilityToFacilityMappingPO.getBranchId());
	// while (token.hasMoreElements()) {
	// String branchName=token.nextElement().toString();
	// if(!(facilityBO.facilityToFacilityCheck(eFmFmFacilityToFacilityMappingPO.getBranchId(),
	// branchName))){
	// EFmFmFacilityToFacilityMappingPO facilityToFacility=new
	// EFmFmFacilityToFacilityMappingPO();
	// facilityToFacility.setBranchName(branchName);
	// facilityToFacility.setFacilityStatus("Y");
	// facilityToFacility.seteFmFmClientBranchPO(eFmFmClientBranchPO);
	// facilityBO.save(eFmFmFacilityToFacilityMappingPO);
	// }
	//
	// }
	// responce.put("status", "success");
	// log.info("serviceEnd -UserId :" +
	// eFmFmFacilityToFacilityMappingPO.getUserId());
	// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	// }

	/**
	 * 
	 * get Facility user eFmFmUserMaster details
	 *
	 * @author Sarfraz Khan
	 * 
	 * @since 2017-05-26
	 * 
	 * @return success/failure details.
	 */

	/**
	 * 
	 * @param Check
	 *            EmployeeIdOrMoblieOrEmail in eFmFmUserMaster Exist Or not
	 * @return employee detail
	 * @throws UnsupportedEncodingException
	 */

	@POST
	@Path("/empSearchByIdMobOrEmail")
	public Response getEmployeeAllFacilities(EFmFmUserMasterPO eFmFmUserMasterPO) throws UnsupportedEncodingException {
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
        IEmployeeDetailBO employeeDetailBO = (IEmployeeDetailBO) ContextLoader.getContext()
                .getBean("IEmployeeDetailBO");


		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("serviceStart -UserId :" + eFmFmUserMasterPO.getUserId());
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));
		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmUserMasterPO.getUserId()))){
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmUserMasterPO.getUserId());
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
		// }catch(Exception e){
		// log.info("authentication error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }

		List<EFmFmUserMasterPO> employeeDetail = null;
		log.info("eFmFmUserMasterPO.getCombinedFacility()"+eFmFmUserMasterPO.getCombinedFacility());
		if(eFmFmUserMasterPO.getSearchType().equalsIgnoreCase("mobile")){
			log.info("mobile");
        	employeeDetail = employeeDetailBO.getEmpMobileNoDetails(Base64.getEncoder().encodeToString(eFmFmUserMasterPO.getEmployeeId().getBytes("utf-8")),eFmFmUserMasterPO.getCombinedFacility());
        }
        else if(eFmFmUserMasterPO.getSearchType().equalsIgnoreCase("email")){
			log.info("email");
        	employeeDetail = employeeDetailBO.getAllEmployeeDetailsFromEmailId(Base64.getEncoder().encodeToString(eFmFmUserMasterPO.getEmployeeId().getBytes("utf-8")),eFmFmUserMasterPO.getCombinedFacility());
        }
       else if(eFmFmUserMasterPO.getSearchType().equalsIgnoreCase("empId")){
			log.info("empId");
       	employeeDetail = employeeDetailBO.getEmpDetailsFromEmployeeIdAndBranchId(
                eFmFmUserMasterPO.getEmployeeId(),eFmFmUserMasterPO.getCombinedFacility());
        }	
	 else {
			responce.put("status", "invalidSearch");
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		}
		log.info("employeeDetail size" + employeeDetail.size());
		if (!(employeeDetail.isEmpty())) {
			List<EFmFmUserFacilityMappingPO> facilityDetails = facilityBO
					.getAllFacilitiesAttachedToUser(employeeDetail.get(0).getUserId());
			log.info("User facilityDetails" + facilityDetails.size());
			List<Map<String, Object>> attacheFacility = new ArrayList<Map<String, Object>>();
			List<String> zones = new ArrayList<String>();
			if (!(facilityDetails.isEmpty())) {
				for (EFmFmUserFacilityMappingPO facility : facilityDetails) {
					zones.add(userMasterBO.getBranchNameFromBranchId(facility.geteFmFmClientBranchPO().getBranchId()));
					if (!(facility.geteFmFmClientBranchPO().getBranchName()
							.equalsIgnoreCase(employeeDetail.get(0).geteFmFmClientBranchPO().getBranchName()))) {
						Map<String, Object> facilities = new HashMap<String, Object>();
						facilities.put("branchId", facility.geteFmFmClientBranchPO().getBranchId());
						facilities.put("name", userMasterBO
								.getBranchNameFromBranchId(facility.geteFmFmClientBranchPO().getBranchId()));
						facilities.put("ticked", true);
						attacheFacility.add(facilities);
					}
				}
				List<EFmFmFacilityToFacilityMappingPO> activefacilityDetails = facilityBO.getAllActiveFacilities();
				for (EFmFmFacilityToFacilityMappingPO activeFacility : activefacilityDetails) {
					if (!(zones.contains(activeFacility.getBranchName()))) {
						zones.add(activeFacility.getBranchName());
						Map<String, Object> activefacilities = new HashMap<String, Object>();
						activefacilities.put("name", activeFacility.getBranchName());
						activefacilities.put("branchId",
								userMasterBO.getBranchIdFromBranchName(activeFacility.getBranchName()));
						activefacilities.put("ticked", false);
						attacheFacility.add(activefacilities);
					}
				}
			}
			responce.put("baseFacility", employeeDetail.get(0).geteFmFmClientBranchPO().getBranchName());
			responce.put("status", "success");
			responce.put("data", attacheFacility);

			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		} else {
			responce.put("status", "notExist");
		}
		log.info("serviceEnd -UserId :" + eFmFmUserMasterPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * 
	 * update get updateUserFacility details
	 * 
	 * @author Sarfraz Khan
	 * 
	 * @since 2017-05-26
	 * 
	 * @return success/failure details.
	 * @throws UnsupportedEncodingException
	 */

	@POST
	@Path("/updateUserFacilityDetails")
	public Response combileUserFacilityDetail(EFmFmUserFacilityMappingPO eFmFmUserFacilityMappingPO)
			throws UnsupportedEncodingException {
		log.info("serviceStart -UserId :" + eFmFmUserFacilityMappingPO.getUserId());
		IFacilityBO facilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");

		Map<String, Object> responce = new HashMap<String, Object>();
		log.info("Logged In User IP Adress" + token.getClientIpAddr(httpRequest));

		// try{
		// if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmAlertTxnPO.getUserId()))){
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }}catch(Exception e){
		// log.info("authenticationToken error"+e);
		// responce.put("status", "invalidRequest");
		// return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		// }
		// List<EFmFmUserMasterPO> userDetail =
		// userMasterBO.getUserDetailFromUserId(eFmFmAlertTxnPO.getUserId());
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

		List<EFmFmUserMasterPO> employeeDetail = null;
		if (eFmFmUserFacilityMappingPO.getSearchType().equalsIgnoreCase("mobile")) {
			log.info("mobile");
			employeeDetail = facilityBO.getEmpMobileNoDetails(
					Base64.getEncoder().encodeToString(eFmFmUserFacilityMappingPO.getEmployeeId().getBytes("utf-8")));
		} else if (eFmFmUserFacilityMappingPO.getSearchType().equalsIgnoreCase("email")) {
			log.info("email");
			employeeDetail = facilityBO.getAllEmployeeDetailsFromEmailId(
					Base64.getEncoder().encodeToString(eFmFmUserFacilityMappingPO.getEmployeeId().getBytes("utf-8")));
		} else if (eFmFmUserFacilityMappingPO.getSearchType().equalsIgnoreCase("empId")) {
			log.info("empId" + eFmFmUserFacilityMappingPO.getEmployeeId());
			employeeDetail = facilityBO
					.getEmpDetailsFromEmployeeIdAndBranchId(eFmFmUserFacilityMappingPO.getEmployeeId());
		}
		if (employeeDetail.isEmpty()) {
			responce.put("status", "empNotExist");
			log.info("serviceEnd -UserId :" + eFmFmUserFacilityMappingPO.getUserId());
			return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		}
		if (!(employeeDetail.isEmpty())) {
			List<EFmFmUserFacilityMappingPO> multipleFacility = facilityBO
					.getAllFacilitiesAttachedToParticularUser(employeeDetail.get(0).getUserId());
			log.info("number of facilities" + multipleFacility.size());
			if (!(multipleFacility.isEmpty())) {
				for (EFmFmUserFacilityMappingPO useFacility : multipleFacility) {
					useFacility.setUserFacilityStatus("N");
					facilityBO.update(useFacility);

				}
			}
		}

		System.out.println("eFmFmUserFacilityMappingPO.getFacilityIds()" + eFmFmUserFacilityMappingPO.getFacilityIds());

		StringTokenizer token = new StringTokenizer(eFmFmUserFacilityMappingPO.getFacilityIds(), ",");
		System.out.println("eFmFmUserFacilityMappingPO.getFacilityIds()" + eFmFmUserFacilityMappingPO.getFacilityIds());
		while (token.hasMoreElements()) {
			String branchId = (String) token.nextElement();
			List<EFmFmFacilityToFacilityMappingPO> facilityCheck = facilityBO.getAllBaseClientIdFromBranchName(
					userMasterBO.getBranchNameFromBranchId(Integer.parseInt(branchId)));
			if (facilityCheck.isEmpty()) {
				// Facility Not Exist Or disable please check with It Team
				responce.put("status", "notExist");
				log.info("serviceEnd -UserId :" + eFmFmUserFacilityMappingPO.getUserId());
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmFacilityToFacilityMappingPO> facilityDetails = facilityBO
					.getParticularFacilityDetailFromBranchName(
							userMasterBO.getBranchNameFromBranchId(Integer.parseInt(branchId)));
			if (!(facilityDetails.isEmpty())) {

				List<EFmFmUserFacilityMappingPO> userFacilityMapping = facilityBO.getAttachedParticularFacilityDetail(
						employeeDetail.get(0).getUserId(), Integer.parseInt(branchId));
				log.info(branchId + "userFacilityMapping" + userFacilityMapping.size());

				if (userFacilityMapping.isEmpty()) {
					EFmFmClientBranchPO branchDeatis = new EFmFmClientBranchPO();
					branchDeatis.setBranchId(Integer.parseInt(branchId));
					EFmFmUserFacilityMappingPO eFmFmUserFacilityMapping = new EFmFmUserFacilityMappingPO();
					eFmFmUserFacilityMapping.setUserFacilityStatus("Y");
					eFmFmUserFacilityMapping.setEfmFmUserMaster(employeeDetail.get(0));
					eFmFmUserFacilityMapping.seteFmFmClientBranchPO(branchDeatis);
					facilityBO.save(eFmFmUserFacilityMapping);
				}
				if (!(userFacilityMapping.isEmpty())
						&& userFacilityMapping.get(0).getUserFacilityStatus().equalsIgnoreCase("N")) {
					userFacilityMapping.get(0).setUserFacilityStatus("Y");
					facilityBO.update(userFacilityMapping.get(0));
				}

			} else {
				responce.put("status", "notExist");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}

		}
		responce.put("status", "success");
		log.info("serviceEnd -UserId :" + eFmFmUserFacilityMappingPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

	public List<Map<String, Object>> getfacilityDetailsByUserId(int userId) {
		IFacilityBO iFacilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
		List<Map<String, Object>> facilityByData = new ArrayList<Map<String, Object>>();
		List<EFmFmUserFacilityMappingPO> multipleFacility = iFacilityBO.getAllFacilitiesAttachedToUser(userId);
		if (!(multipleFacility.isEmpty())) {
			for (EFmFmUserFacilityMappingPO faciltyName : multipleFacility) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("facilityId", faciltyName.geteFmFmClientBranchPO().getBranchId());
				data.put("facilityName", faciltyName.geteFmFmClientBranchPO().getBranchName());
				facilityByData.add(data);
			}
		}
		return facilityByData;
	}

	public List<String> getListfacilityDetailsByUserId(int userId) {
		IFacilityBO iFacilityBO = (IFacilityBO) ContextLoader.getContext().getBean("IFacilityBO");
		List<String> facilityByData = new ArrayList<String>();
		List<EFmFmUserFacilityMappingPO> multipleFacility = iFacilityBO.getAllFacilitiesAttachedToUser(userId);
		if (!(multipleFacility.isEmpty())) {
			for (EFmFmUserFacilityMappingPO faciltyName : multipleFacility) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("facilityId", faciltyName.geteFmFmClientBranchPO().getBranchId());
				data.put("facilityName", faciltyName.geteFmFmClientBranchPO().getBranchName());
				facilityByData.add(String.valueOf(faciltyName.geteFmFmClientBranchPO().getBranchId()));
				// facilityByData.add(faciltyName.geteFmFmClientBranchPO().getBranchId());
			}
		}
		return facilityByData;
	}

}