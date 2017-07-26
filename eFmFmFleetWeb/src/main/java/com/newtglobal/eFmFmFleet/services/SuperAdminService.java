package com.newtglobal.eFmFmFleet.services;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/clients")
@Consumes("application/json")
@Produces("application/json")
public class SuperAdminService {
	private static Log log = LogFactory.getLog(SuperAdminService.class);

	/*
	 * this service will respond you all the clients details, and all master
	 * modules
	 * 
	 */
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	
	@POST
	@Path("/clientsDetail")
	public Response getAllClientsDetail() throws IOException {
		List<Map<String, Object>> allClientsDetail = new ArrayList<Map<String, Object>>();
		String data = "";
		InputStream inputStream = getClass().getResourceAsStream("/clientConfiguration.json");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
//		try{
//			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken")))){
//
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}}catch(Exception e){
//				log.info("authentication error"+e);
//				responce.put("status", "invalidRequest");
//				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
//			}
		InputStreamReader reader = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(reader);
		String line;
		while ((line = br.readLine()) != null) {
			data += line + "\n";
		}
		JSONObject clientJsonObject = new JSONObject(data);
		log.info("clientJsonObject" + clientJsonObject);
		JSONArray jsonarray = clientJsonObject.getJSONArray("clientsData");
		for (int i = 0; i < jsonarray.length(); i++) {
			JSONObject jsonobject = jsonarray.getJSONObject(i);
			Map<String, Object> requests = new HashMap<String, Object>();
			requests.put("branchId", jsonobject.getString("branchId"));
			requests.put("branchName", jsonobject.getString("branchName"));
			requests.put("branchCode", jsonobject.getString("branchCode"));
			requests.put("branchUri", jsonobject.getString("branchUri"));
			allClientsDetail.add(requests);
		}
		 //log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok(allClientsDetail, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * this service will respond you all the particular clients web and employee
	 * module details details, and all master modules
	 * 
	 */
	@POST
	@Path("/particularClientDetail")
	public Response getParticularClientDetailFromClientId(EFmFmClientBranchPO eFmFmClientBranchPO) throws IOException {
		List<Map<String, Object>> webModuleData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> empModuleData = new ArrayList<Map<String, Object>>();
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
		Map<String, Object> moduleData = new HashMap<String, Object>();
		String string = "";
		String moduleFile = "/webModuleAccessTo" + eFmFmClientBranchPO.getBranchName() + ".json";
		InputStream inputStream = getClass().getResourceAsStream(moduleFile);
		InputStreamReader reader = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(reader);
		String line;
		while ((line = br.readLine()) != null) {
			string += line + "\n";
		}
		JSONObject clientJsonObject = new JSONObject(string);
		log.info("clientJsonObject" + clientJsonObject);
		JSONArray jsonarray = clientJsonObject.getJSONArray("moduleData");
		for (int i = 0; i < jsonarray.length(); i++) {
			JSONObject jsonobject = jsonarray.getJSONObject(i);
			Map<String, Object> requests = new HashMap<String, Object>();
			requests.put("moduleName", jsonobject.getString("moduleName"));
			requests.put("isActive", jsonobject.getString("isActive"));
			webModuleData.add(requests);
		}
		String empData = "";
		String empInputStream = "/employeeModuleAccessTo" + eFmFmClientBranchPO.getBranchName() + ".json";
		InputStream inputStreamEmp = getClass().getResourceAsStream(empInputStream);
		InputStreamReader empReader = new InputStreamReader(inputStreamEmp);
		BufferedReader bufferReader = new BufferedReader(empReader);
		String empLine;
		while ((empLine = bufferReader.readLine()) != null) {
			empData += empLine + "\n";
		}
		JSONObject jsonObject = new JSONObject(empData);
		log.info("clientJsonObject" + jsonObject);
		JSONArray moduleArray = jsonObject.getJSONArray("employeeModuleData");
		for (int i = 0; i < moduleArray.length(); i++) {
			JSONObject jsonobject = moduleArray.getJSONObject(i);
			Map<String, Object> requests = new HashMap<String, Object>();
			requests.put("moduleName", jsonobject.getString("moduleName"));
			requests.put("isActive", jsonobject.getString("isActive"));
			empModuleData.add(requests);
		}
		moduleData.put("webModules", webModuleData);
		moduleData.put("empModules", empModuleData);
		 log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(moduleData, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * this service will respond you enabling and disabling the selected web
	 * features,
	 * 
	 */
	@POST
	@Path("/clientWebModulesSave")
	public Response enablingAndDisablingWebModules(EFmFmClientBranchPO eFmFmClientBranchPO) throws IOException {
		String moduleFile = "webModuleAccessTo" + eFmFmClientBranchPO.getBranchName() + ".json";
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
		Map<String, Object> requests = new HashMap<String, Object>();
		System.out.println("eFmFmClientBranchPO.getModules().toString()" + eFmFmClientBranchPO.getModules().toString());
		requests.put("status", "success");
		String fileName = "/Users/sarfrazkhan/Workspace/maven.1462264319473/eFmFmFleet/eFmFmFleetWeb/src/main/resources";
		FileWriter fileOut = new FileWriter(fileName + "/" + moduleFile);
		fileOut.write("");
		fileOut.close();
		FileWriter filemoduleWriter = new FileWriter(fileName + "/" + moduleFile);
		filemoduleWriter
				.write("{" + '"' + "moduleData" + '"' + ":" + eFmFmClientBranchPO.getModules().toString() + "}");
		filemoduleWriter.close();
		 log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * this service will respond you enabling and disabling the selected
	 * employee features,
	 * 
	 */
	@POST
	@Path("/clientEmployeeModulesSave")
	public Response enablingAndDisablingEmployeeModules(EFmFmClientBranchPO eFmFmClientBranchPO) throws IOException {
		Map<String, Object> requests = new HashMap<String, Object>();
		 log.info("serviceStart -UserId :" + eFmFmClientBranchPO.getUserId());
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
		requests.put("status", "success");
		String empInputFile = "employeeModuleAccessTo" + eFmFmClientBranchPO.getBranchName() + ".json";
		String fileName = "/Users/sarfrazkhan/Workspace/maven.1462264319473/eFmFmFleet/eFmFmFleetWeb/src/main/resources";
		FileWriter fileOut = new FileWriter(fileName + "/" + empInputFile);
		fileOut.write("");
		fileOut.close();
		FileWriter filemoduleWriter = new FileWriter(fileName + "/" + empInputFile);
		filemoduleWriter.write(
				"{" + '"' + "employeeModuleData" + '"' + ":" + eFmFmClientBranchPO.getModules().toString() + "}");
		filemoduleWriter.close();
		 log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * add a client and branch in to the system
	 * 
	 */
	@POST
	@Path("/saveClient")
	public Response saveClientDetails(EFmFmClientBranchPO eFmFmClientBranchPO) throws IOException {
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
		Map<String, Object> requests = new HashMap<String, Object>();	
		List<EFmFmClientBranchPO> clientDetails=userMasterBO.getBranchDetailsFromBranchName(eFmFmClientBranchPO.getBranchName());
		if(clientDetails.isEmpty()){
			requests.put("status", "branchExist");
			 log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
			return Response.ok(requests, MediaType.APPLICATION_JSON).build();	
		}
		if(eFmFmClientBranchPO.getBranchId()==0){
			EFmFmClientMasterPO eFmFmClientMasterPO=new EFmFmClientMasterPO();
			eFmFmClientMasterPO.setAddress(eFmFmClientBranchPO.getAddress());
			eFmFmClientMasterPO.setBranchStartDate(new Date());
			eFmFmClientMasterPO.setCityName(eFmFmClientBranchPO.getCityName());
			eFmFmClientMasterPO.setClientName(eFmFmClientBranchPO.getBranchName());
			eFmFmClientMasterPO.setContactNumber(eFmFmClientBranchPO.getMobileNumber());
//			eFmFmClientMasterPO.setCountryCode(countryCode);
//			eFmFmClientMasterPO.setCreatedBy(createdBy);
			eFmFmClientMasterPO.setCreationTime(new Date());
			eFmFmClientMasterPO.setEmailId(eFmFmClientBranchPO.getEmailId());
			eFmFmClientMasterPO.setLatitudeAndLongitude(eFmFmClientBranchPO.getLatitudeLongitude());
			eFmFmClientMasterPO.setStateName(eFmFmClientBranchPO.getStateName());
			eFmFmClientMasterPO.setStatus("Y");
			eFmFmClientMasterPO.setUpdatedTime(new Date());
			userMasterBO.save(eFmFmClientMasterPO);
		}
		eFmFmClientBranchPO.setCreationTime(new Date());
		eFmFmClientBranchPO.setStartDate(new Date());
		eFmFmClientBranchPO.setUpdatedTime(new Date());
		eFmFmClientBranchPO.setStatus("Y");
		userMasterBO.save(eFmFmClientBranchPO);
		//creating master Webmodule and employee module JSON files
	    //start writing the json data in to the new client files for web module
		String filePath = "/Users/sarfrazkhan/Workspace/maven.1462264319473/eFmFmFleet/eFmFmFleetWeb/src/main/resources";
		String data = "";
		InputStream inputStream = getClass().getResourceAsStream("/webModuleMaster.json");
		InputStreamReader reader = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(reader);
		String line;
		while ((line = br.readLine()) != null) {
			data += line + "\n";
		}
		JSONObject clientJsonObject = new JSONObject(data);
		String moduleFile = "webModuleAccessTo" + eFmFmClientBranchPO.getBranchName() + ".json";
		FileWriter filemoduleWriter = new FileWriter(filePath + "/" + moduleFile);
		filemoduleWriter.write(clientJsonObject.toString());
		filemoduleWriter.close();
       //end writing the json data in to the new client files for web module
		
	    //start writing the json data in to the new client files for web module
		String webData = "";
		InputStream inputStreamData = getClass().getResourceAsStream("/employeeModuleMaster.json");
		InputStreamReader inputReader = new InputStreamReader(inputStreamData);
		BufferedReader bufferreader = new BufferedReader(inputReader);
		String lineData;
		while ((lineData = bufferreader.readLine()) != null) {
			webData += lineData + "\n";
		}
		JSONObject employeeJsonObject = new JSONObject(webData);	
		String empInputFile = "employeeModuleAccessTo" + eFmFmClientBranchPO.getBranchName() + ".json";
		FileWriter empModuleWriter = new FileWriter(filePath + "/" + empInputFile);
		empModuleWriter.write(employeeJsonObject.toString());
		empModuleWriter.close();
	     //end writing the json data in to the new client files for web module
		requests.put("status", "success");
		 log.info("serviceEnd -UserId :" + eFmFmClientBranchPO.getUserId());
		return Response.ok(requests, MediaType.APPLICATION_JSON).build();
	}

}