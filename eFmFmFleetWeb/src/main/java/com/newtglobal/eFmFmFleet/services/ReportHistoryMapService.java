package com.newtglobal.eFmFmFleet.services;

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
import org.springframework.stereotype.Component;

import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmActualRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/history")
@Consumes("application/json")
@Produces("application/json")
public class ReportHistoryMapService {	
	private static Log log = LogFactory.getLog(ReportHistoryMapService.class);	
	
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	
	/**
	*  method for implemented route history map.
	* after completing the trip.   
	*
	* @author  Sarfraz Khan
	* 
	* @since   28-Jan-2016
	* 
	* @return success/failure details.
	*/	
	
	@POST
	@Path("/historyMap")
	public Response routeLattiLongiAfterCompletion(EFmFmActualRoutTravelledPO eFmFmActualRoutTravelledPO){		
		IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),eFmFmActualRoutTravelledPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(eFmFmActualRoutTravelledPO.getUserId());
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
		List<Map<String, Object>> routeHistoryCodes = new ArrayList<Map<String, Object>>();
		List<EFmFmActualRoutTravelledPO> routeHistory=iAssignRouteBO.getRouteLattiLongiFromAssignRouteId(eFmFmActualRoutTravelledPO.getRouteId());
		log.info("LatiLongi Size"+routeHistory.size());
		if(!(routeHistory.isEmpty())){
		for(EFmFmActualRoutTravelledPO routeDetail:routeHistory){
			Map<String, Object> routeGeoCodes = new HashMap<String, Object>();
			routeGeoCodes.put("Lattitude", routeDetail.getLatitudeLongitude().split(",")[0]);
			routeGeoCodes.put("Longitude", routeDetail.getLatitudeLongitude().split(",")[1]);
			routeHistoryCodes.add(routeGeoCodes);		
		}
		}
		return Response.ok(routeHistoryCodes, MediaType.APPLICATION_JSON).build();
	}
	
}



