package com.newtglobal.eFmFmFleet.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.CalculateDistance;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/backTwoBack")
@Consumes("application/json")
@Produces("application/json")
public class BackToBackService {

	private static Log log = LogFactory.getLog(BackToBackService.class);	
	DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();

	
	
	/**
	*   Get Back to back after completing the routing
	* 
	*
	* @author  Sarfraz Khan
	* 
	* @since  2017-01-24
	* 
	* @return success/failure details.
	 * @throws ParseException 
	*/	
	
	@POST
	@Path("/routingComplete")
	public Response getB2bAfterRoutingComplete(EFmFmEmployeeTravelRequestPO travelRequestPO) throws ParseException{		
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
		
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
		 	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),travelRequestPO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(travelRequestPO.getUserId());
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

        ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
        IAssignRouteBO iAssignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
        log.info("serviceStart -UserId :" + travelRequestPO.getUserId());
        DateFormat shiftTimeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat shiftTimeFormate = new SimpleDateFormat("HH:mm");
        String shiftTime = travelRequestPO.getTime();
        Date shift = shiftTimeFormate.parse(shiftTime);
        java.sql.Time dateShiftTime = new java.sql.Time(shift.getTime());
        Date excutionDate = dateformate.parse(travelRequestPO.getExecutionDate());
		CalculateDistance calculateDistance = new CalculateDistance();
        List<EFmFmAssignRoutePO> activeRoutes = iAssignRouteBO.getAllRoutesForPrintForParticularShift(excutionDate, excutionDate,
                travelRequestPO.getTripType(), shiftTimeFormat.format(dateShiftTime), travelRequestPO.getCombinedFacility());
        log.info("Shift Size"+activeRoutes.size());
        if (!(activeRoutes.isEmpty())) {     	
        	for(EFmFmAssignRoutePO assignRoute:activeRoutes){
        		try{
				List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = null;
 				StringBuffer empWayPoints = new StringBuffer();
 				if (assignRoute.getTripType().equalsIgnoreCase("PICKUP")) {
					employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployees(assignRoute.getAssignRouteId());
				} else {
					employeeTripDetailPO = iCabRequestBO.getDropTripAllSortedEmployees(assignRoute.getAssignRouteId());
				}

 				if (!(employeeTripDetailPO.isEmpty())) {
					for (EFmFmEmployeeTripDetailPO employeeTripDetail : employeeTripDetailPO) {
			        	empWayPoints.append(employeeTripDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude()+"|"); 
					}
 				} 
 				String plannedETAAndDistance ="";
 				if (assignRoute.getTripType().equalsIgnoreCase("PICKUP")) {
 					plannedETAAndDistance= calculateDistance.getPlannedDistanceaAndETAForRoute(
 	        				assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude(),
 	        				assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude(), empWayPoints.toString().replaceAll("\\s+",""));		        		
 				}

 				else{
 					plannedETAAndDistance= calculateDistance.getPlannedDistanceaAndETAForRoute(
 	        				assignRoute.geteFmFmClientBranchPO().getLatitudeLongitude(),
 	        				employeeTripDetailPO.get(employeeTripDetailPO.size()-1).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude(), empWayPoints.toString().replaceAll("\\s+",""));		        			
 				}
 				
        		log.info("plannedETAAndDistance"+plannedETAAndDistance+" "+empWayPoints.toString().replaceAll("\\s+","")+"assignRoute Id"+assignRoute.getAssignRouteId());
                try{
        		assignRoute.setPlannedTravelledDistance(Math.round(Double.parseDouble(plannedETAAndDistance.split("-")[0])+(assignRoute.geteFmFmClientBranchPO().getAddingGeoFenceDistanceIntrip())));
                assignRoute.setPlannedDistance(Math.round(Double.parseDouble(plannedETAAndDistance.split("-")[0])+(assignRoute.geteFmFmClientBranchPO().getAddingGeoFenceDistanceIntrip())));
                assignRoute.setPlannedTime(Math.round(Long.parseLong(plannedETAAndDistance.split("-")[1])));		
                }catch(Exception e){
            	log.info("plannedETAAndDistance"+plannedETAAndDistance+" "+empWayPoints.toString().replaceAll("\\s+","")+"assignRoute Id"+assignRoute.getAssignRouteId());
                 log.info("Error"+e);
                 
                }
                assignRoute.setPlannedReadFlg("N");	
                assignRoute.setRoutingCompletionStatus("completed");
                assignRoute.setScheduleReadFlg("Y");  
 				if (assignRoute.getTripType().equalsIgnoreCase("DROP")) {
 					try{
 						if(assignRoute.getIsBackTwoBack().equalsIgnoreCase("N")){
 						//Back to back check any pickup is there with this route end drop location. 
 				        List<EFmFmAssignRoutePO> b2bPickupDetails=iAssignRouteBO.getBackToBackTripDetailFromTripTypeANdShiftTime("PICKUP", assignRoute.getShiftTime(),assignRoute.getCombinedFacility());					
 				        log.info("b2bDetails"+b2bPickupDetails.size());
 			        	if(!(b2bPickupDetails.isEmpty())){
 						for (EFmFmAssignRoutePO assignPickupRoute : b2bPickupDetails) {
 	 				        List<EFmFmAssignRoutePO> b2bPickupDetailsAlreadyDone=iAssignRouteBO.getBackToBackTripDetailFromb2bId(assignPickupRoute.getAssignRouteId(), "DROP",assignRoute.getCombinedFacility());				
 							if(b2bPickupDetailsAlreadyDone.isEmpty()){							
 							//get first pickup employee
 				        	List<EFmFmEmployeeTripDetailPO> employeeTripData = iCabRequestBO.getParticularTripAllEmployees(assignPickupRoute.getAssignRouteId());  				        	
 				        	if(!(employeeTripData.isEmpty())){
 					        	String lastDropLattilongi=employeeTripDetailPO.get(employeeTripDetailPO.size()-1).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude();					        	        	
 					        	String firstPickupLattilongi=employeeTripData.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude();        	
 					        	log.info("lastDropLattilongi"+lastDropLattilongi);
 					        	log.info("firstPickupLattilongi"+firstPickupLattilongi);
 					        	CalculateDistance empDistance=new CalculateDistance();
 					        	if(assignRoute.geteFmFmClientBranchPO().getSelectedB2bType().equalsIgnoreCase("distance") && (empDistance.employeeDistanceCalculation(lastDropLattilongi, firstPickupLattilongi) < (assignRoute.geteFmFmClientBranchPO().getB2bByTravelDistanceInKM()))){		        		
 					        		//Current Drop B2B route Assign route Date
 					        		String currentDropRouteAssignDate=dateFormat.format(assignRoute.getTripAssignDate());
 					        		Date currentDropRouteDate=dateFormat.parse(currentDropRouteAssignDate);
 					        		long totalCurrentDropAssignDateAndShiftTime=getDisableTime(assignRoute.getShiftTime().getHours(), assignRoute.getShiftTime().getMinutes(), currentDropRouteDate);	        		
 					        		
 					        		//Drop Route Complete Time
 					        		long totalRouteTime=totalCurrentDropAssignDateAndShiftTime+TimeUnit.SECONDS.toMillis(assignRoute.getPlannedTime());
 					        		
 					        		//Total time for Drop back2back trip
 					        		long totalB2bTimeForCuurentDrop=totalRouteTime+(TimeUnit.HOURS.toMillis(assignRoute.geteFmFmClientBranchPO().getDriverWaitingTimeAtLastLocation().getHours())+TimeUnit.MINUTES.toMillis(assignRoute.geteFmFmClientBranchPO().getDriverWaitingTimeAtLastLocation().getMinutes())+TimeUnit.SECONDS.toMillis(assignRoute.geteFmFmClientBranchPO().getDriverWaitingTimeAtLastLocation().getSeconds()));
 					        		
 					        		//Pickup B2B route Assign route Date
 					        		String pickupRouteCurrentAssignDate=dateFormat.format(assignPickupRoute.getTripAssignDate());
 					        		Date pickupRouteCurrentAssign=dateFormat.parse(pickupRouteCurrentAssignDate);
 					        			
 					        		//Pickup backtoback Route PickUpTime 
 					        		long totalAssignDateAndPickUpTime=getDisableTime(employeeTripData.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime().getHours(),employeeTripData.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime().getMinutes(), pickupRouteCurrentAssign);	        		
 					        		
 					        		if((totalRouteTime < totalAssignDateAndPickUpTime) && totalB2bTimeForCuurentDrop > totalAssignDateAndPickUpTime){		        			
 					        			assignRoute.setIsBackTwoBack("Y");
 					        			assignRoute.setBackTwoBackRouteId(assignPickupRoute.getAssignRouteId());
 					        			iAssignRouteBO.update(assignRoute);							
 					        			break;
 					        		}
 					        		else{
 					                   iAssignRouteBO.update(assignRoute);

 					        		}
 					        	}
 					        	else if(assignRoute.geteFmFmClientBranchPO().getSelectedB2bType().equalsIgnoreCase("time") && (TimeUnit.SECONDS.toMillis(empDistance.employeeETACalculation(lastDropLattilongi, firstPickupLattilongi)) < (TimeUnit.HOURS.toMillis(assignRoute.geteFmFmClientBranchPO().getB2bByTravelTime().getHours())+TimeUnit.MINUTES.toMillis(assignRoute.geteFmFmClientBranchPO().getB2bByTravelTime().getMinutes())+TimeUnit.SECONDS.toMillis(assignRoute.geteFmFmClientBranchPO().getB2bByTravelTime().getSeconds())))){		        		
 					        		//Current Drop B2B route Assign route Date
 					        		String currentDropRouteAssignDate=dateFormat.format(assignRoute.getTripAssignDate());
 					        		Date currentDropRouteDate=dateFormat.parse(currentDropRouteAssignDate);
 					        		long totalCurrentDropAssignDateAndShiftTime=getDisableTime(assignRoute.getShiftTime().getHours(),assignRoute.getShiftTime().getMinutes(), currentDropRouteDate);	        		
 					        		
 					        		//Drop Route Complete Time
 					        		long totalRouteTime=totalCurrentDropAssignDateAndShiftTime+TimeUnit.SECONDS.toMillis(assignRoute.getPlannedTime());
 					        		
 					        		//Total time for Drop back2back trip
 					        		long totalB2bTimeForCuurentDrop=totalRouteTime+(TimeUnit.HOURS.toMillis(assignRoute.geteFmFmClientBranchPO().getDriverWaitingTimeAtLastLocation().getHours())+TimeUnit.MINUTES.toMillis(assignRoute.geteFmFmClientBranchPO().getDriverWaitingTimeAtLastLocation().getMinutes())+TimeUnit.SECONDS.toMillis(assignRoute.geteFmFmClientBranchPO().getDriverWaitingTimeAtLastLocation().getSeconds()));
 					        		
 					        		//Pickup B2B route Assign route Date
 					        		String pickupRouteCurrentAssignDate=dateFormat.format(assignPickupRoute.getTripAssignDate());
 					        		Date pickupRouteCurrentAssign=dateFormat.parse(pickupRouteCurrentAssignDate);
 					        			
 					        		//Pickup backtoback Route PickUpTime 
 					        		long totalAssignDateAndPickUpTime=getDisableTime(employeeTripData.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime().getHours(),employeeTripData.get(0).geteFmFmEmployeeTravelRequest().getPickUpTime().getMinutes(), pickupRouteCurrentAssign);	        		
 					        		
 					        		log.info("totalRouteTime"+new Date(totalRouteTime));
 					        		
 					        		log.info("totalAssignDateAndPickUpTime"+new Date(totalAssignDateAndPickUpTime));
 					        		log.info("totalB2bTimeForCuurentDrop"+new Date(totalB2bTimeForCuurentDrop));

 					        		
 					        		if((totalRouteTime < totalAssignDateAndPickUpTime) && totalB2bTimeForCuurentDrop > totalAssignDateAndPickUpTime){		        			
 					        			assignRoute.setIsBackTwoBack("Y");
 					        			assignRoute.setBackTwoBackRouteId(assignPickupRoute.getAssignRouteId());
 					        			iAssignRouteBO.update(assignRoute);							
 					        			break;
 					        		}	
 					        		else{
  					                   iAssignRouteBO.update(assignRoute);

  					        		}
 					        	}
 					        	else{
 					                iAssignRouteBO.update(assignRoute);
 					        	}
 				        	}
 						}
 							}
 						
			                iAssignRouteBO.update(assignRoute);
 						}
 			        	
 			        	else{
 			                iAssignRouteBO.update(assignRoute);
 			        	}
 			        	}
 						}catch(Exception e){
 							log.info("Error"+e);
 						}
 				}
 				else{
 	                iAssignRouteBO.update(assignRoute);
 				}
        	 }catch(Exception e){
           		 log.info("error - :" + e);
                 assignRoute.setPlannedReadFlg("N");	
                 assignRoute.setRoutingCompletionStatus("completed");
                 assignRoute.setScheduleReadFlg("Y");  
	              iAssignRouteBO.update(assignRoute);
                }   
                
        	}   
               
        }
		 log.info("serviceEnd -UserId :" + travelRequestPO.getUserId());
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
		
	}
	

	/**
	* Get Back two back reports
	* for Genpact it can 
	*
	* @author  Sarfraz Khan
	* 
	* @since  2017-01-24
	* 
	* @return success/failure details.
	 * @throws ParseException 
	*/	
	
	@POST
	@Path("/b2bReports")
	public Response getB2bReportsData(EFmFmAssignRoutePO assignRoutePO) throws ParseException{		
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		 Map<String, Object> responce = new HashMap<String, Object>();
		 		
		 log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 try{
			 	if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),assignRoutePO.getUserId()))){

		 		responce.put("status", "invalidRequest");
		 		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
		 	}}catch(Exception e){
		 		log.info("authentication error"+e);
				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();

		 	}
		 List<EFmFmUserMasterPO> userDetailToken = userMasterBO.getUserDetailFromUserId(assignRoutePO.getUserId());
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
		 
		 log.info("serviceStart -UserId :" + assignRoutePO.getUserId());
		 log.info("serviceEnd -UserId :" + assignRoutePO.getUserId());
		return Response.ok("Success", MediaType.APPLICATION_JSON).build();
	}
	
	public long getDisableTime(int hours, int minutes, Date checkIndate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(checkIndate);
		calendar.add(Calendar.HOUR, hours);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTimeInMillis();
	}
}
