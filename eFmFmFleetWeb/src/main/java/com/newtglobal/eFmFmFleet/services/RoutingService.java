package com.newtglobal.eFmFmFleet.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.business.bo.RoutingBO;
import com.newtglobal.eFmFmFleet.eFmFmFleet.CalculateDistance;
import com.newtglobal.eFmFmFleet.eFmFmFleet.JwtTokenGenerator;
import com.newtglobal.eFmFmFleet.model.AreaEmpClusterMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlgoRoutesPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRoutingAreaPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripTimingMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.web.ContextLoader;

@Component
@Path("/algo")
@Consumes("application/json")
@Produces("application/json")
public class RoutingService {

	private static Log log = LogFactory.getLog(RoutingService.class);
    DateFormat shiftTimeFormater = new SimpleDateFormat("HH:mm");
    
	 @Context
	 private HttpServletRequest httpRequest;
	JwtTokenGenerator token=new JwtTokenGenerator();



	int areaNameCounter = 0;

	@POST
	@Path("/createArea")
	public Response employeeTravelRequestDetails(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException, InvalidKeyException, NoSuchAlgorithmException, IOException, URISyntaxException {
		Map<String, Object> responce = new HashMap<String, Object>();
		RoutingBO routingBO = (RoutingBO) ContextLoader.getContext().getBean("RoutingBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		
			
		employeeTravelRequestPO.setBaseFacility(1);
		
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		 log.info(employeeTravelRequestPO.getCombinedFacility()+"serviceStart -UserId :" + employeeTravelRequestPO.getUserId());
		
	
		log.info("create area service called"+employeeTravelRequestPO.getBaseFacility());
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				
		String newRequestDate = employeeTravelRequestPO.getResheduleDate();
		Date date = dateFormat.parse(newRequestDate);
		String requestDate = requestDateFormat.format(date);
		employeeTravelRequestPO.setRequestDate(new Date());
		String shiftDate=employeeTravelRequestPO.getTime();
		Date shift = shiftTimeFormater.parse(shiftDate);
		Time shiftTime = new Time(shift.getTime());
		//base facility Id is required.
        int branchId=employeeTravelRequestPO.getBaseFacility();
	   

		List<EFmFmEmployeeTravelRequestPO> travelDetails = null;
        if(employeeTravelRequestPO.getTripType().equalsIgnoreCase("PICKUP")){
            travelDetails = routingBO.listOfEmployeeByShiftWiseForPickupRouting(branchId, requestDate,
    				shiftTime,employeeTravelRequestPO.getTripType());

        }else{
            travelDetails = routingBO.listOfEmployeeByShiftWiseForDropRouting(branchId, requestDate,
    				shiftTime,employeeTravelRequestPO.getTripType());

        }
        
		log.info("travelDetails" + travelDetails.size() + "deleteShiftRequest requestDate" + requestDate + "shiftTime"
				+ shiftTime + "Request Size");
		if (!(travelDetails.isEmpty())) {
			//Routing based on base selected facility
	        List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO.getShiftTimeDetailFromShiftTimeAndTripType(String.valueOf(employeeTravelRequestPO.getBaseFacility()), shiftTime, employeeTravelRequestPO.getTripType());
			CalculateDistance distance=new CalculateDistance();
			RouteGeocodeService lineCordinates=new RouteGeocodeService();
			routingBO.deleteAllRoutesCreatedFromCluster();
			routingBO.deleteAllClustersCreatedFromAreas();
			StringBuffer empWayPoints = new StringBuffer();
			double onRouteDistanceSearch=shiftTimeDetails.get(0).getClusterGeoFenceRegion();			
			int clusterCount=1;
			try{
			for(int j=0;(j<travelDetails.size() && !(travelDetails.isEmpty()));){	
				boolean isClusterOrigion=true;
	//			firstIndex=true;
				List<String> coordinates=lineCordinates.getRoutePoints(travelDetails.get(j).getEfmFmUserMaster().getLatitudeLongitude(),travelDetails.get(j).geteFmFmClientBranchPO().getLatitudeLongitude(),empWayPoints.toString());							
				log.info(travelDetails.get(j).getEfmFmUserMaster().getEmployeeId()+"Number Of Cordinates"+coordinates.size());
				if(!coordinates.isEmpty()){
					for(int i=0;i<coordinates.size(); i++){
						String lineLattitude=coordinates.get(i).split(",")[0];
						String lineLongitude=coordinates.get(i).split(",")[1];
						for(int k=0;k<travelDetails.size();k++){				
							double routePointDistance=distance.distance(Double.parseDouble(lineLattitude), Double.parseDouble(lineLongitude), Double.parseDouble(travelDetails.get(k).getEfmFmUserMaster().getLatitudeLongitude().split(",")[0]), Double.parseDouble(travelDetails.get(k).getEfmFmUserMaster().getLatitudeLongitude().split(",")[1]), 'm');			
							if(routePointDistance<=onRouteDistanceSearch){	
								createClusterFromRequest(travelDetails.get(k),clusterCount,isClusterOrigion);
								travelDetails.remove(k);
								isClusterOrigion=false;
						}

					}
					
				}	
					
					
				clusterCount++;
			}
			
			
			}
		}catch(Exception e){
			log.info("error"+e);
		}
			
		
		//Start Vehicle Allocation For Chennai vehicle Type
//		if(shiftTimeDetails.get(0).geteFmFmClientBranchPO().getBranchCode().equalsIgnoreCase("SBOCHN")){
			autoRoutesForVehicleTypeSixAndThirteen(shiftTime, newRequestDate, branchId, employeeTravelRequestPO.getTripType(),employeeTravelRequestPO.getUserId(),employeeTravelRequestPO.getCombinedFacility(),employeeTravelRequestPO.getBaseFacility());	
//		}//End Vehicle Allocation For Chennai
		}
		 log.info("serviceEnd -UserId :" + employeeTravelRequestPO.getUserId());
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}	
	
	public void autoRoutesForVehicleTypeSixAndThirteen(Time shiftTime,String newRequestDate,int kk,String tripType,int userId,String combinedBranchId,int baseFacilityId) throws ParseException, InvalidKeyException, NoSuchAlgorithmException, IOException, URISyntaxException{
		RoutingBO routingBO = (RoutingBO) ContextLoader.getContext().getBean("RoutingBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
		.getBean("IVehicleCheckInBO");

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<EFmFmAssignRoutePO> assignRoutePO;
		log.info("baseFacilityId" + baseFacilityId);

		//Query For Just Getting the longest cluster order by cost desc in sequential not parallel.
		List<AreaEmpClusterMappingPO> clusterDetails = routingBO.getLongestClusterForRoutes(baseFacilityId);	
		RouteGeocodeService lineCordinates=new RouteGeocodeService();
		log.info("Longest Area" + clusterDetails.size());
		int routeCount=0;
		CalculateDistance distance=new CalculateDistance();
		String branchGeoCode="";
//		int innova=6;
//		int Tempo=13;	
		
		
		Date date = dateFormat.parse(newRequestDate);
		String requestDate = requestDateFormat.format(date);				
			if(!clusterDetails.isEmpty()){
				branchGeoCode=clusterDetails.get(0).geteFmFmClientBranchPO().getLatitudeLongitude();	
				
				
		        //Query for getting area geofance by shift wise
		        List<EFmFmTripTimingMasterPO> shiftTimeDetails = iCabRequestBO.getShiftTimeDetailFromShiftTimeAndTripType(String.valueOf(baseFacilityId), shiftTime, tripType);		

			double onRouteDistanceSearch=shiftTimeDetails.get(0).getRouteGeoFenceRegion();
			outer:	for(AreaEmpClusterMappingPO clusters:clusterDetails){
				List<AreaEmpClusterMappingPO> allAreaCluster = routingBO.getParticularEmployeeClusterDetail(clusters.getClusterId());	
				if(!(allAreaCluster.get(0).isClusterToRouteStatus())){		
					//clusterDetail.geteFmFmClientBranchPO().getBranchId()
			List<AreaEmpClusterMappingPO> pendingClusterDetails = routingBO.getLongestClusterForRoutes(1);	
			int empCount=0;
	          if(pendingClusterDetails.isEmpty()){
	        	  break; 
	          }
				int capacity=0;
				routeCount++;			
				//Query For  Getting all employees request attach to that cluster and putting them in Routes.
				List<AreaEmpClusterMappingPO> userAttachToCluster = routingBO.getAllEmployeesOfGivenClusterAndArea(baseFacilityId,pendingClusterDetails.get(0).getClusterName());	
				
				
				List<EFmFmVehicleCheckInPO> allCheckInVehicles = null;				
				List<Integer> capacities=iCabRequestBO.getAllCapacitiesOfTheVehicles(String.valueOf(baseFacilityId));	
				
				
				if(!(capacities.isEmpty())){
		       
				
               if(capacities.contains(userAttachToCluster.size())){
                   allCheckInVehicles = iCabRequestBO.getAllCheckedInVehiclesForSpecificCapacity(combinedBranchId,userAttachToCluster.size());
                   log.info(combinedBranchId+"userAttachToCluster"+userAttachToCluster.size()+"allCheckInVehicles size"+allCheckInVehicles.size());
                   if(allCheckInVehicles.isEmpty()){
                       List<EFmFmVehicleCheckInPO> dummyVehicleDetails=iCabRequestBO.getAllCheckedInDummyVehicles(String.valueOf(baseFacilityId));
               		log.info("allCheckInVehicles Details=" + dummyVehicleDetails.size());
               	   if(!(dummyVehicleDetails.isEmpty())){
               		 for(EFmFmVehicleCheckInPO checkInDetail:dummyVehicleDetails){
               			 checkInDetail.setStatus("Y");
               			 iVehicleCheckInBO.update(checkInDetail);
               		 }
               	   }		
 
                   }
                   if(!(allCheckInVehicles.isEmpty())){
				for(AreaEmpClusterMappingPO clusterDetail:userAttachToCluster){ 
					List<AreaEmpClusterMappingPO> cluster = routingBO.getParticularEmployeeClusterDetail(clusterDetail.getClusterId());
					if(!(cluster.get(0).isClusterToRouteStatus())){	
					//get checked in vehicle for cluster value 5
					assignRoutePO = iCabRequestBO.getHalfCompletedAssignRouteByRouteName(requestDate,baseFacilityId,
							clusterDetail.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
							clusterDetail.geteFmFmEmployeeTravelRequest().getTripType(), clusterDetail.geteFmFmEmployeeTravelRequest().getShiftTime(),"R"+routeCount);	
					
					if(assignRoutePO.isEmpty()){
	                 	EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO=new EFmFmAlgoRoutesPO();
	     				EFmFmEmployeeTravelRequestPO travelRequest=new EFmFmEmployeeTravelRequestPO();
	     				EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
	     				eFmFmClientBranchPO
	     						.setBranchId(clusterDetail.geteFmFmClientBranchPO().getBranchId());
	     				travelRequest.setRequestId(clusterDetail.geteFmFmEmployeeTravelRequest().getRequestId());				
	     				eFmFmAlgoRoutesPO.setAlgoRouteOregion(true);				
	     				eFmFmAlgoRoutesPO.setAreaEmpClusterMapping(clusterDetail);
	     				eFmFmAlgoRoutesPO.setDistance(clusterDetail.getDistance());
	     				eFmFmAlgoRoutesPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
	     				eFmFmAlgoRoutesPO.setVehicleType(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity());
	     				eFmFmAlgoRoutesPO.seteFmFmEmployeeTravelRequest(travelRequest);
	     				eFmFmAlgoRoutesPO.setRouteAllocationStatus(false);
	     				eFmFmAlgoRoutesPO.setRouteName("R"+routeCount);				
	     				routingBO.save(eFmFmAlgoRoutesPO);
	     				clusterDetail.setClusterToRouteStatus(true);
	     				routingBO.update(clusterDetail);
	     				String routeStatus=assignRouteForVehicle(clusterDetail, allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity(), clusterDetail.geteFmFmClientBranchPO().getBranchId(), allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity(), routeCount,eFmFmAlgoRoutesPO.getRouteName(),newRequestDate,userId,combinedBranchId,allCheckInVehicles);
						if(routeStatus.equalsIgnoreCase("full")){
							 continue outer;
						}

					}
					else{					
	                 	EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO=new EFmFmAlgoRoutesPO();
	     				EFmFmEmployeeTravelRequestPO travelRequest=new EFmFmEmployeeTravelRequestPO();
	     				EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
	     				eFmFmClientBranchPO
	     						.setBranchId(clusterDetail.geteFmFmClientBranchPO().getBranchId());
	     				travelRequest.setRequestId(clusterDetail.geteFmFmEmployeeTravelRequest().getRequestId());				
	     				eFmFmAlgoRoutesPO.setAlgoRouteOregion(true);				
	     				eFmFmAlgoRoutesPO.setAreaEmpClusterMapping(clusterDetail);
	     				eFmFmAlgoRoutesPO.setDistance(clusterDetail.getDistance());
	     				eFmFmAlgoRoutesPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
	     				eFmFmAlgoRoutesPO.setVehicleType(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity());
	     				eFmFmAlgoRoutesPO.seteFmFmEmployeeTravelRequest(travelRequest);
	     				eFmFmAlgoRoutesPO.setRouteAllocationStatus(false);
	     				eFmFmAlgoRoutesPO.setRouteName("R"+routeCount);				
	     				routingBO.save(eFmFmAlgoRoutesPO);
	     				clusterDetail.setClusterToRouteStatus(true);
	     				routingBO.update(clusterDetail);
						String routeStatus=updatingHalpCompletedRoutes(assignRoutePO.get(0), clusterDetail, clusterDetail.geteFmFmClientBranchPO().getBranchId(), allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity(), routeCount);
						if(routeStatus.equalsIgnoreCase("full")){
							 continue outer;
						}
					}
					assignRoutePO = iCabRequestBO.getHalfCompletedAssignRouteByRouteName(requestDate,clusterDetail.geteFmFmClientBranchPO().getBranchId(),
							clusterDetail.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
							clusterDetail.geteFmFmEmployeeTravelRequest().getTripType(), clusterDetail.geteFmFmEmployeeTravelRequest().getShiftTime(),"R"+routeCount);					
	 					assignRoutePO.get(0).setVehicleStatus("F");
	 					iCabRequestBO.update(assignRoutePO.get(0));
				}
	            }
               }
	            }    
  
               else{
					allCheckInVehicles = iCabRequestBO.getAllDummyCheckedInVehiclesForGreatestCapacity(combinedBranchId,userAttachToCluster.size());					
	                   log.info("allCheckInVehicles size"+allCheckInVehicles.size());
	                   if(allCheckInVehicles.isEmpty()){
	                       List<EFmFmVehicleCheckInPO> dummyVehicleDetails=iCabRequestBO.getAllCheckedInDummyVehicles(String.valueOf(baseFacilityId));
	               		log.info("allCheckInVehicles Details=" + dummyVehicleDetails.size());
	               	   if(!(dummyVehicleDetails.isEmpty())){
	               		 for(EFmFmVehicleCheckInPO checkInDetail:dummyVehicleDetails){
	               			 checkInDetail.setStatus("Y");
	               			 iVehicleCheckInBO.update(checkInDetail);
	               		 }
	               	   }		
	 
	                   }
	                   if(!(allCheckInVehicles.isEmpty())){
						for(AreaEmpClusterMappingPO clusterDetail:userAttachToCluster){    				
		    				List<AreaEmpClusterMappingPO> userClusters = routingBO.getParticularEmployeeClusterDetail(clusterDetail.getClusterId());
		    				if(!(userClusters.get(0).isClusterToRouteStatus())){	
		                 	EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO=new EFmFmAlgoRoutesPO();
		     				EFmFmEmployeeTravelRequestPO travelRequest=new EFmFmEmployeeTravelRequestPO();
		     				EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		     				eFmFmClientBranchPO
		     						.setBranchId(clusterDetail.geteFmFmClientBranchPO().getBranchId());
		     				travelRequest.setRequestId(clusterDetail.geteFmFmEmployeeTravelRequest().getRequestId());				
		     				eFmFmAlgoRoutesPO.setAlgoRouteOregion(true);				
		     				eFmFmAlgoRoutesPO.setAreaEmpClusterMapping(clusterDetail);
		     				eFmFmAlgoRoutesPO.setDistance(clusterDetail.getDistance());
		     				eFmFmAlgoRoutesPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		     				eFmFmAlgoRoutesPO.setVehicleType(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity());
		     				eFmFmAlgoRoutesPO.seteFmFmEmployeeTravelRequest(travelRequest);
		     				eFmFmAlgoRoutesPO.setRouteAllocationStatus(false);
		     				eFmFmAlgoRoutesPO.setRouteName("R"+routeCount);				
		     				routingBO.save(eFmFmAlgoRoutesPO);
		     				clusterDetail.setClusterToRouteStatus(true);
		     				routingBO.update(clusterDetail);   				
		    				assignRoutePO = iCabRequestBO.getHalfCompletedAssignRouteByRouteName(requestDate,clusterDetail.geteFmFmClientBranchPO().getBranchId(),
		    						clusterDetail.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
		    						clusterDetail.geteFmFmEmployeeTravelRequest().getTripType(), clusterDetail.geteFmFmEmployeeTravelRequest().getShiftTime(),eFmFmAlgoRoutesPO.getRouteName());	
		                      if(assignRoutePO.isEmpty()){
		                    	  String routeStatus=assignRouteForVehicle(clusterDetail, allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity(), clusterDetail.geteFmFmClientBranchPO().getBranchId(), allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity(), routeCount,eFmFmAlgoRoutesPO.getRouteName(),newRequestDate,userId,combinedBranchId,allCheckInVehicles);  
		      					if(routeStatus.equalsIgnoreCase("full")){
		   						 continue outer;
		   					}

		                      }
		                      else{
		                    	  String routeStatus=updatingHalpCompletedRoutes(assignRoutePO.get(0), clusterDetail, clusterDetail.geteFmFmClientBranchPO().getBranchId(), allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity(), routeCount); 					
		      					if(routeStatus.equalsIgnoreCase("full")){
		   						 continue outer;
		   					}

		                      }
		     				
		     					StringBuffer empWayPoints = new StringBuffer();     					
		     					List<EFmFmAlgoRoutesPO> attachToClusterUser = routingBO.getAllEmployeesOfGivenRoute(clusterDetail.geteFmFmClientBranchPO().getBranchId(),"R"+routeCount);	
		      	           if(!(attachToClusterUser.isEmpty())){
		    	            	empWayPoints.append(attachToClusterUser.get(attachToClusterUser.size()-1).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude()+"|");
		    					List<String> coordinates=lineCordinates.getRoutePoints(attachToClusterUser.get(attachToClusterUser.size()-1).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude(),branchGeoCode,empWayPoints.toString());

		     					//Once it found the nearest employee then it will look in to his/her geocode and  on the basis of that Geo code it will put it in to that route.
		     					if(!coordinates.isEmpty()){
		     					for(int i=0;i<coordinates.size(); i++){
		     						String lineLattitude=coordinates.get(i).split(",")[0];
		     						String lineLongitude=coordinates.get(i).split(",")[1];
		     						List<AreaEmpClusterMappingPO> pendingUserAttachToArea = routingBO.getLongestClusterForRoutes(1);	
		     						if(pendingUserAttachToArea.isEmpty()){
		     							break;
		     						}
		     						for(AreaEmpClusterMappingPO areaCluster:pendingUserAttachToArea){
		         						double routePointDistance=distance.distance(Double.parseDouble(lineLattitude), Double.parseDouble(lineLongitude), Double.parseDouble(areaCluster.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude().split(",")[0]), Double.parseDouble(areaCluster.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude().split(",")[1]), 'm');   					
		         						if(routePointDistance<=onRouteDistanceSearch){ 							
		         		    				assignRoutePO = iCabRequestBO.getHalfCompletedAssignRouteByRouteName(requestDate,clusterDetail.geteFmFmClientBranchPO().getBranchId(),
		         		    						clusterDetail.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
		         		    						clusterDetail.geteFmFmEmployeeTravelRequest().getTripType(), clusterDetail.geteFmFmEmployeeTravelRequest().getShiftTime(),eFmFmAlgoRoutesPO.getRouteName());	        						
		         		    				List<EFmFmEmployeeTripDetailPO> employeeTripDetail=iCabRequestBO.getParticularTripAllEmployeesDesc(assignRoutePO.get(0).getAssignRouteId());	    				
		         		    				if((assignRoutePO.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()-1)==employeeTripDetail.size()){
		         		     					assignRoutePO.get(0).setVehicleStatus("F");
		         		     					iCabRequestBO.update(assignRoutePO.get(0));
		         		     					 continue outer;
		         		     				}
		         		                 	EFmFmAlgoRoutesPO eFmFmAlgoRoutes=new EFmFmAlgoRoutesPO();
		         		     				EFmFmEmployeeTravelRequestPO travelRequestData=new EFmFmEmployeeTravelRequestPO();
		         		     				EFmFmClientBranchPO eFmFmClientBranch = new EFmFmClientBranchPO();
		         		     				eFmFmClientBranch
		         		     						.setBranchId(clusterDetail.geteFmFmClientBranchPO().getBranchId());
		         		     				travelRequestData.setRequestId(areaCluster.geteFmFmEmployeeTravelRequest().getRequestId());				
		         		     				eFmFmAlgoRoutes.setAlgoRouteOregion(true);				
		         		     				eFmFmAlgoRoutes.setAreaEmpClusterMapping(areaCluster);
		         		     				eFmFmAlgoRoutes.setDistance(areaCluster.getDistance());
		         		     				eFmFmAlgoRoutes.seteFmFmClientBranchPO(eFmFmClientBranch);
		         		     				eFmFmAlgoRoutes.setVehicleType(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity());
		         		     				eFmFmAlgoRoutes.seteFmFmEmployeeTravelRequest(travelRequestData);
		         		     				eFmFmAlgoRoutes.setRouteAllocationStatus(false);
		         		     				eFmFmAlgoRoutes.setRouteName("R"+routeCount);				
		         		     				routingBO.save(eFmFmAlgoRoutes);
		         		     				areaCluster.setClusterToRouteStatus(true);
		         		     				routingBO.update(areaCluster);
		         		    				assignRoutePO = iCabRequestBO.getHalfCompletedAssignRouteByRouteName(requestDate,clusterDetail.geteFmFmClientBranchPO().getBranchId(),
		         		    						clusterDetail.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
		         		    						clusterDetail.geteFmFmEmployeeTravelRequest().getTripType(), clusterDetail.geteFmFmEmployeeTravelRequest().getShiftTime(),eFmFmAlgoRoutesPO.getRouteName());	
		             			                  if(assignRoutePO.isEmpty()){
		             			                	 String routeStatus=assignRouteForVehicle(areaCluster, allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity(), clusterDetail.geteFmFmClientBranchPO().getBranchId(), allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity(), routeCount,eFmFmAlgoRoutesPO.getRouteName(),newRequestDate,userId,combinedBranchId,allCheckInVehicles); 
		             								if(routeStatus.equalsIgnoreCase("full")){
		             									 continue outer;
		             								}
		 
		             			                  }
		             			                   else{
		             			                	  String routeStatus=updatingHalpCompletedRoutes(assignRoutePO.get(0), areaCluster, clusterDetail.geteFmFmClientBranchPO().getBranchId(), allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity(), routeCount); 
		             			 					if(routeStatus.equalsIgnoreCase("full")){
		             									 continue outer;
		             								}

		             			                   }
		             							
		     						}

		     						}
		     					}
		     				}
		     					
		     					if(!(assignRoutePO.isEmpty())){
		     					assignRoutePO.get(0).setVehicleStatus("F");
		     					iCabRequestBO.update(assignRoutePO.get(0));
		     					 continue outer;
		     					}
		     					
		     				}
		     			}
		                 }

						 
					 }
					else{
						allCheckInVehicles = iCabRequestBO.getAllDummyCheckedInVehiclesForLessCapacity(combinedBranchId,userAttachToCluster.size());
		                   log.info("allCheckInVehicles size"+allCheckInVehicles.size());
		                   if(allCheckInVehicles.isEmpty()){
		                       List<EFmFmVehicleCheckInPO> dummyVehicleDetails=iCabRequestBO.getAllCheckedInDummyVehicles(String.valueOf(baseFacilityId));
		               		log.info("allCheckInVehicles Details=" + dummyVehicleDetails.size());
		               	   if(!(dummyVehicleDetails.isEmpty())){
		               		 for(EFmFmVehicleCheckInPO checkInDetail:dummyVehicleDetails){
		               			 checkInDetail.setStatus("Y");
		               			 iVehicleCheckInBO.update(checkInDetail);
		               		 }
		               	   }		
		 
		                   }
		                   if(!(allCheckInVehicles.isEmpty())){
						for(AreaEmpClusterMappingPO clusterDetail:userAttachToCluster){ 
							List<AreaEmpClusterMappingPO> cluster = routingBO.getParticularEmployeeClusterDetail(clusterDetail.getClusterId());
							if(!(cluster.get(0).isClusterToRouteStatus())){	
							//get checked in vehicle for cluster value 12
							assignRoutePO = iCabRequestBO.getHalfCompletedAssignRouteByRouteName(requestDate,clusterDetail.geteFmFmClientBranchPO().getBranchId(),
									clusterDetail.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId(),
									clusterDetail.geteFmFmEmployeeTravelRequest().getTripType(), clusterDetail.geteFmFmEmployeeTravelRequest().getShiftTime(),"R"+routeCount);	  				
							if(assignRoutePO.isEmpty()){
			                 	EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO=new EFmFmAlgoRoutesPO();
			     				EFmFmEmployeeTravelRequestPO travelRequest=new EFmFmEmployeeTravelRequestPO();
			     				EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
			     				eFmFmClientBranchPO
			     						.setBranchId(clusterDetail.geteFmFmClientBranchPO().getBranchId());
			     				travelRequest.setRequestId(clusterDetail.geteFmFmEmployeeTravelRequest().getRequestId());				
			     				eFmFmAlgoRoutesPO.setAlgoRouteOregion(true);				
			     				eFmFmAlgoRoutesPO.setAreaEmpClusterMapping(clusterDetail);
			     				eFmFmAlgoRoutesPO.setDistance(clusterDetail.getDistance());
			     				eFmFmAlgoRoutesPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
			    				eFmFmAlgoRoutesPO.setVehicleType(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity());
			     				eFmFmAlgoRoutesPO.seteFmFmEmployeeTravelRequest(travelRequest);
			     				eFmFmAlgoRoutesPO.setRouteAllocationStatus(false);
			     				eFmFmAlgoRoutesPO.setRouteName("R"+routeCount);				
			     				routingBO.save(eFmFmAlgoRoutesPO);
			     				clusterDetail.setClusterToRouteStatus(true);
			     				routingBO.update(clusterDetail);
			     				String routeStatus=assignRouteForVehicle(clusterDetail, allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity(), clusterDetail.geteFmFmClientBranchPO().getBranchId(), allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity(), routeCount,eFmFmAlgoRoutesPO.getRouteName(),newRequestDate,userId,combinedBranchId,allCheckInVehicles);
								if(routeStatus.equalsIgnoreCase("full")){
									 continue outer;
								}

							}
							else{					
			                 	EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO=new EFmFmAlgoRoutesPO();
			     				EFmFmEmployeeTravelRequestPO travelRequest=new EFmFmEmployeeTravelRequestPO();
			     				EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
			     				eFmFmClientBranchPO
			     						.setBranchId(clusterDetail.geteFmFmClientBranchPO().getBranchId());
			     				travelRequest.setRequestId(clusterDetail.geteFmFmEmployeeTravelRequest().getRequestId());				
			     				eFmFmAlgoRoutesPO.setAlgoRouteOregion(true);				
			     				eFmFmAlgoRoutesPO.setAreaEmpClusterMapping(clusterDetail);
			     				eFmFmAlgoRoutesPO.setDistance(clusterDetail.getDistance());
			     				eFmFmAlgoRoutesPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
			    				eFmFmAlgoRoutesPO.setVehicleType(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity());
			     				eFmFmAlgoRoutesPO.seteFmFmEmployeeTravelRequest(travelRequest);
			     				eFmFmAlgoRoutesPO.setRouteAllocationStatus(false);
			     				eFmFmAlgoRoutesPO.setRouteName("R"+routeCount);				
			     				routingBO.save(eFmFmAlgoRoutesPO);
			     				clusterDetail.setClusterToRouteStatus(true);
			     				routingBO.update(clusterDetail);
			     				String routeStatus=updatingHalpCompletedRoutes(assignRoutePO.get(0), clusterDetail, clusterDetail.geteFmFmClientBranchPO().getBranchId(), allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity(), routeCount);
								if(routeStatus.equalsIgnoreCase("full")){
									 continue outer;
								}

							}
			            	empCount++;
			 				capacity--;
								if(allCheckInVehicles.get(0).getEfmFmVehicleMaster().getCapacity()==empCount){
			     					assignRoutePO.get(0).setVehicleStatus("F");
			     					iCabRequestBO.update(assignRoutePO.get(0));
			    					 continue outer;
			    				}
			             }
			            }
					}

						
					}
            	   
            	   
            	   
               }
  	        } 
				}
	      
			}
		}
		//End of Route Creation	
		
		

	}
	

	
	
	
	
	public String  assignRouteForVehicle(AreaEmpClusterMappingPO clusterDetail,int capacity,int branchId,int vehicleType,int routeCount,String routeName,String newRequestDate,int userId,String combinedBranchId,List<EFmFmVehicleCheckInPO> allCheckInVehicles) throws ParseException{
		IApprovalBO approvalBO = (IApprovalBO) ContextLoader.getContext().getBean("IApprovalBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		RoutingBO routingBO = (RoutingBO) ContextLoader.getContext().getBean("RoutingBO");
		IVehicleCheckInBO iVehicleCheckInBO = (IVehicleCheckInBO) ContextLoader.getContext()
				.getBean("IVehicleCheckInBO");
		List<EFmFmEmployeeTravelRequestPO> requests=iCabRequestBO.getParticularRequestDetailOnTripComplete(clusterDetail.geteFmFmEmployeeTravelRequest().getRequestId());
		CalculateDistance calculate=new CalculateDistance();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat requestDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//		String newRequestDate = "19-09-2016";
		Date date = dateFormat.parse(newRequestDate);
		String requestDate = requestDateFormat.format(date);
		List<EFmFmAssignRoutePO> assignRoutePO;	
	EFmFmEmployeeTripDetailPO employeeTripDetailPO = new EFmFmEmployeeTripDetailPO();
	String requestDateAndTime = requestDate + " " + clusterDetail.geteFmFmEmployeeTravelRequest().getShiftTime();
	EFmFmEmployeeTravelRequestPO travelRequest=new EFmFmEmployeeTravelRequestPO();
	EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
	eFmFmClientBranchPO
			.setBranchId(branchId);
	travelRequest.setRequestId(clusterDetail.geteFmFmEmployeeTravelRequest().getRequestId());				
	EFmFmAssignRoutePO assignRoute = new EFmFmAssignRoutePO();
	EFmFmVehicleCheckInPO vehicleCheckInPO = new EFmFmVehicleCheckInPO();
	vehicleCheckInPO
			.setCheckInId(allCheckInVehicles.get(0).getCheckInId());
	assignRoute.setEfmFmVehicleCheckIn(vehicleCheckInPO);
	EFmFmRouteAreaMappingPO routeAreaMapping = new EFmFmRouteAreaMappingPO();	
	routeAreaMapping.setRouteAreaId(clusterDetail.geteFmFmEmployeeTravelRequest().geteFmFmRouteAreaMapping().getRouteAreaId());
	assignRoute.seteFmFmRouteAreaMapping(routeAreaMapping);
	assignRoute.setEscortRequredFlag("N");
	assignRoute.setAllocationMsg("N");
	assignRoute.setLocationFlg("N");
	assignRoute.setRouteName(routeName);
	assignRoute.setShiftTime(clusterDetail.geteFmFmEmployeeTravelRequest().getShiftTime());
	assignRoute.setTripStatus("allocated");
	assignRoute.setTripConfirmationFromDriver("Not Delivered");

	assignRoute.setIsToll("No");
    assignRoute.setRoutingCompletionStatus("Started");
    assignRoute.setSelectedBaseFacility(branchId);

    assignRoute.setAssignedVendorName("NA"); 
	assignRoute.setIsBackTwoBack("N");
	assignRoute.setDistanceUpdationFlg("Y");
	assignRoute.setPlannedReadFlg("Y");
	assignRoute.setScheduleReadFlg("Y");
	assignRoute.setRemarksForEditingTravelledDistance("NO");
	assignRoute.setEditDistanceApproval("NO");
	assignRoute.setRouteGenerationType("normal");
	assignRoute.setCreatedDate(new Date());
	assignRoute.setTripAssignDate(requestDateTimeFormat.parse(requestDateAndTime));
	assignRoute.setTripUpdateTime(requestDateTimeFormat.parse(requestDateAndTime));
	assignRoute.setVehicleStatus("A");
	assignRoute.setBucketStatus("N");
	assignRoute.seteFmFmClientBranchPO(eFmFmClientBranchPO);
	assignRoute.setTripType(clusterDetail.geteFmFmEmployeeTravelRequest().getTripType());
	allCheckInVehicles.get(0).setStatus("N");
	if (clusterDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
			.equalsIgnoreCase("Always")
			|| clusterDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().geteFmFmClientBranchPO().getEscortRequired()
					.equalsIgnoreCase("femalepresent")
					&& clusterDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getGender().equalsIgnoreCase("Female")) {
		List<EFmFmEscortCheckInPO> escortList = iCabRequestBO
				.getAllCheckedInEscort(combinedBranchId);
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
			.update(allCheckInVehicles.get(0));
	iCabRequestBO.update(assignRoute);
	assignRoutePO = iCabRequestBO.getHalfCompletedAssignRouteForAutoRouting(requestDate,branchId,
			clusterDetail.geteFmFmEmployeeTravelRequest().getTripType(), clusterDetail.geteFmFmEmployeeTravelRequest().getShiftTime(),
			allCheckInVehicles.get(0).getCheckInId());	
	
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
			assignRoutePO.get(0).getEfmFmVehicleCheckIn().geteFmFmDeviceMaster().getDeviceId(),eFmFmClientBranchPO.getCombinedFacility());
	deviceDetails.get(0).setStatus("allocated");
	iVehicleCheckInBO.update(deviceDetails.get(0));	
	try{	
//	if(assignRoutePO.get(0).getTripType().equalsIgnoreCase("PICKUP")){	
		long pickTimeInSec=0;
		long bufferTime=0;
		long minutesToSec = requests.get(0).getShiftTime().getMinutes() * 60 ;
		long hoursToSec = requests.get(0).getShiftTime().getHours() * 60 *60;
	try{
//		List<EFmFmTripTimingMasterPO> shiftDetails =iCabRequestBO.getShiftTimeDetailFromShiftTimeAndTripType(branchId, requests.get(0).getShiftTime(), requests.get(0).getTripType());
//		if(!(shiftDetails.isEmpty())){
//			bufferTime=shiftDetails.get(0).getShiftBufferTime();
//		}
		log.info("employeeWiseTime"+requests.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getMaxTravelTimeEmployeeWiseInMin());
		pickTimeInSec=(requests.get(0).getEfmFmUserMaster().geteFmFmClientBranchPO().getMaxTravelTimeEmployeeWiseInMin())*60;
	}catch(Exception e){
		log.info("Employee Maxtime Error"+e);
	pickTimeInSec = calculate.employeeETACalculation(assignRoutePO.get(0).geteFmFmClientBranchPO().getLatitudeLongitude(),requests.get(0).getEfmFmUserMaster().getLatitudeLongitude());		
	}
	long firstPicTime=(hoursToSec+minutesToSec)-pickTimeInSec;	
	log.info(minutesToSec+"min and Hours"+hoursToSec);

   log.info(pickTimeInSec+"firstPicTime"+firstPicTime);
   long hours=0;
   long min=0;
   if(firstPicTime>=3600){
	   hours= (firstPicTime/60)/60;
	    firstPicTime=firstPicTime-hours;
   }
   if(firstPicTime>=60){
	     min = (firstPicTime / 60)%60;
   }
    String pickTime=hours+":"+min;
    
    Date shift = shiftTimeFormater.parse(pickTime);
   java.sql.Time firstEmpPickUpTime = new java.sql.Time(shift.getTime());    
	requests.get(0).setPickUpTime(firstEmpPickUpTime);
//}
	}catch(Exception e){
		log.info("PickUptime error"+e);
	}
	requests.get(0).setReadFlg("R");
	requests.get(0).setRequestColor("yellow");
	requests.get(0).setRoutingAreaCreation("N");
	iCabRequestBO.update(requests.get(0));	
	
	assignRoute.setAssignRouteId(assignRoutePO.get(0).getAssignRouteId());
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
	employeeTripDetailPO.seteFmFmEmployeeTravelRequest(travelRequest);
	employeeTripDetailPO.setEfmFmAssignRoute(assignRoutePO.get(0));
	employeeTripDetailPO.setCurrenETA("0");
	employeeTripDetailPO.setEmployeeStatus("allocated");
	employeeTripDetailPO.setComingStatus("Yet to confirm");
	employeeTripDetailPO.setEmployeeOnboardStatus("NO");
	iCabRequestBO.update(employeeTripDetailPO);	
	
	List<EFmFmEmployeeTripDetailPO> employeeTripData= new ArrayList<EFmFmEmployeeTripDetailPO>();;
	employeeTripData=iCabRequestBO.getParticularTripNonDropEmployeesDetails(assignRoutePO.get(0).getAssignRouteId());
	if((employeeTripData.size())==(assignRoutePO.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity())){
		assignRoutePO.get(0).setVehicleStatus("F");
		iCabRequestBO.update(assignRoutePO.get(0));
		return "full";
 }
	List<AreaEmpClusterMappingPO> userAttachToCluster = routingBO.getAllEmployeesOfGivenClusterAndArea(branchId,clusterDetail.getClusterName());	
	if(!(userAttachToCluster.isEmpty())){
		log.info(userAttachToCluster.size());
		for(AreaEmpClusterMappingPO areaEmpClusterMappingPO:userAttachToCluster){
			List<AreaEmpClusterMappingPO> cluster = routingBO.getParticularEmployeeClusterDetail(areaEmpClusterMappingPO.getClusterId());
			if(!(cluster.get(0).isClusterToRouteStatus())){
			 requests=iCabRequestBO.getParticularRequestDetailOnTripComplete(areaEmpClusterMappingPO.geteFmFmEmployeeTravelRequest().getRequestId());
			    
				List<EFmFmEmployeeTripDetailPO> employeeTripDetail = new ArrayList<EFmFmEmployeeTripDetailPO>();;
				if (assignRoutePO.get(0).getTripType().equalsIgnoreCase("PICKUP")) {
	//				employeeTripDetail = iCabRequestBO.getParticularTripAllEmployeesDesc(assignRoutePO.get(0).getAssignRouteId());
					employeeTripDetail=iCabRequestBO.getParticularTripNonDropEmployeesDetails(assignRoutePO.get(0).getAssignRouteId());
				} else {
					employeeTripDetail = iCabRequestBO.getDropTripAllSortedEmployees(assignRoutePO.get(0).getAssignRouteId());
				}

				StringBuffer empWayPoints = new StringBuffer();
				if(!(employeeTripDetail.isEmpty())){
					for(EFmFmEmployeeTripDetailPO employeeDetail:employeeTripDetail){
			            	empWayPoints.append(employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude()+"|");
					}
	            	empWayPoints.append(requests.get(0).getEfmFmUserMaster().getLatitudeLongitude()+"|");
			//	}
				String plannedETAAndDistance = calculate.getPlannedDistanceaAndETAForRoute(
						employeeTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude(),
						assignRoutePO.get(0).geteFmFmClientBranchPO().getLatitudeLongitude(), empWayPoints.toString().replaceAll("\\s+",""));
		         long totalTravelTime=Long.parseLong(plannedETAAndDistance.split("-")[1]);	
		         if(totalTravelTime>(assignRoutePO.get(0).geteFmFmClientBranchPO().getMaxTravelTimeEmployeeWiseInMin()*60)){
	 					assignRoutePO.get(0).setVehicleStatus("F");
	 					iCabRequestBO.update(assignRoutePO.get(0));
	 					return "full";
		         }
				
				}	
				if((employeeTripDetail.size())==(assignRoutePO.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity())){
 					assignRoutePO.get(0).setVehicleStatus("F");
 					iCabRequestBO.update(assignRoutePO.get(0));
 					return "full";
				}
			    EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO=new EFmFmAlgoRoutesPO();
				eFmFmAlgoRoutesPO.setAlgoRouteOregion(true);				
				eFmFmAlgoRoutesPO.setAreaEmpClusterMapping(areaEmpClusterMappingPO);
				eFmFmAlgoRoutesPO.setDistance(areaEmpClusterMappingPO.getDistance());
				eFmFmAlgoRoutesPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
			    eFmFmAlgoRoutesPO.setVehicleType(assignRoutePO.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity());
				eFmFmAlgoRoutesPO.seteFmFmEmployeeTravelRequest(requests.get(0));
				eFmFmAlgoRoutesPO.setRouteAllocationStatus(false);
				eFmFmAlgoRoutesPO.setRouteName("R"+routeCount);				
				routingBO.save(eFmFmAlgoRoutesPO);
				areaEmpClusterMappingPO.setClusterToRouteStatus(true);
				routingBO.update(areaEmpClusterMappingPO);
				
//				List<EFmFmEmployeeTripDetailPO> employeeTripDetail=iCabRequestBO.getParticularTripAllEmployeesDesc(assignRoutePO.get(0).getAssignRouteId());
				EFmFmEmployeeTripDetailPO employeeTrip = new EFmFmEmployeeTripDetailPO();
			try{
	    		           //for getting total route travel time
				    	//	if(assignRoutePO.get(0).getTripType().equalsIgnoreCase("PICKUP")){	    	
					    		int pickTimeInSec = calculate.employeeETACalculation(employeeTripDetail.get(employeeTripDetail.size()-1).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude(), requests.get(0).getEfmFmUserMaster().getLatitudeLongitude());							    		
					    		long minutesToSec = employeeTripDetail.get(employeeTripDetail.size()-1).geteFmFmEmployeeTravelRequest().getPickUpTime().getMinutes() * 60 ;
				    			long hoursToSec = employeeTripDetail.get(employeeTripDetail.size()-1).geteFmFmEmployeeTravelRequest().getPickUpTime().getHours() * 60 *60;
				    		   long firstPicTime=(hoursToSec+minutesToSec)+pickTimeInSec;	
				    		   long hours=0;
				    		   long min=0;
				    		   if(firstPicTime>=3600){
				    			   hours= (firstPicTime/60)/60;
				    			    firstPicTime=firstPicTime-hours;
				    		   }
				    		   if(firstPicTime>=60){
				    			     min = (firstPicTime / 60)%60;
				    		   }
					    		
				    		    String pickTime=hours+":"+min;
				    	    
				    	    Date shift = shiftTimeFormater.parse(pickTime);
				    	   java.sql.Time firstEmpPickUpTime = new java.sql.Time(shift.getTime());    
				    		requests.get(0).setPickUpTime(firstEmpPickUpTime);
				    //	}				    		
			}catch(Exception e){
					log.info("PickUptime error"+e);
				}
				requests.get(0).setReadFlg("R");
				requests.get(0).setRequestColor("yellow");
				requests.get(0).setRoutingAreaCreation("N");
				iCabRequestBO.update(requests.get(0));					
				if (assignRoutePO.get(0).getTripType().equalsIgnoreCase("DROP")) {
					employeeTrip.setTenMinuteMessageStatus("Y");
					employeeTrip.setTwoMinuteMessageStatus("Y");
					employeeTrip.setReachedFlg("Y");
					employeeTrip.setCabDelayMsgStatus("Y");
				} else {
					employeeTrip.setTenMinuteMessageStatus("N");
					employeeTrip.setTwoMinuteMessageStatus("N");
					employeeTrip.setReachedFlg("N");
					employeeTrip.setCabDelayMsgStatus("N");

				}
				employeeTrip.setActualTime(new Date());
				employeeTrip.setGoogleEta(0);
				employeeTrip.setBoardedFlg("N");
				employeeTrip.seteFmFmEmployeeTravelRequest(requests.get(0));
				employeeTrip.setEfmFmAssignRoute(assignRoutePO.get(0));
				employeeTrip.setCurrenETA("0");
				employeeTrip.setEmployeeStatus("allocated");
				employeeTrip.setComingStatus("Yet to confirm");
				employeeTrip.setEmployeeOnboardStatus("NO");

				iCabRequestBO.update(employeeTrip);	

				if((employeeTripDetail.size())==(assignRoutePO.get(0).getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()-2)){
					assignRoutePO.get(0).setVehicleStatus("F");
					iCabRequestBO.update(assignRoutePO.get(0));
					return "full";
		     }

			}
			log.info("Outside the loop");
		}
	}
	return "ok";
	}
	
	public String updatingHalpCompletedRoutes(EFmFmAssignRoutePO assignRoutePO,AreaEmpClusterMappingPO clusterDetail,int branchId,int vehicleType,int routeCount){
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		RoutingBO routingBO = (RoutingBO) ContextLoader.getContext().getBean("RoutingBO");
		CalculateDistance calculate=new CalculateDistance();	
		List<EFmFmEmployeeTripDetailPO> employeeTripDetail = new ArrayList<EFmFmEmployeeTripDetailPO>();;

		if (assignRoutePO.getTripType().equalsIgnoreCase("PICKUP")) {
			employeeTripDetail=iCabRequestBO.getParticularTripNonDropEmployeesDetails(assignRoutePO.getAssignRouteId());
		} else {
			employeeTripDetail = iCabRequestBO.getDropTripAllSortedEmployees(assignRoutePO.getAssignRouteId());
		}	
        if((employeeTripDetail.size()-1)==(assignRoutePO.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()-1)){
				assignRoutePO.setVehicleStatus("F");
				iCabRequestBO.update(assignRoutePO);
				clusterDetail.setClusterToRouteStatus(false);
				routingBO.update(clusterDetail);
				List<EFmFmAlgoRoutesPO> attachToClusterUser = routingBO.getAllEmployeesOfGivenRoute(branchId,"R"+routeCount);	
				routingBO.deleteAnLastRouteEntry(attachToClusterUser.get(attachToClusterUser.size()-1).getAlgoRouteId());
				return "full";
        }

		if((assignRoutePO.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity())==employeeTripDetail.size()){
			assignRoutePO.setVehicleStatus("F");
			iCabRequestBO.update(assignRoutePO);
			clusterDetail.setClusterToRouteStatus(false);
			routingBO.update(clusterDetail);
			List<EFmFmAlgoRoutesPO> attachToClusterUser = routingBO.getAllEmployeesOfGivenRoute(branchId,"R"+routeCount);	
			routingBO.deleteAnLastRouteEntry(attachToClusterUser.get(attachToClusterUser.size()-1).getAlgoRouteId());
			return "full";
			}
		
		
		List<EFmFmEmployeeTravelRequestPO> requests=iCabRequestBO.getParticularRequestDetailOnTripComplete(clusterDetail.geteFmFmEmployeeTravelRequest().getRequestId());
		StringBuffer empWayPoints = new StringBuffer();
		if(!(employeeTripDetail.isEmpty())){
			for(EFmFmEmployeeTripDetailPO employeeDetail:employeeTripDetail){
	            	empWayPoints.append(employeeDetail.geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude()+"|");
			}
        	empWayPoints.append(requests.get(0).getEfmFmUserMaster().getLatitudeLongitude()+"|");
	//	}
		String plannedETAAndDistance = calculate.getPlannedDistanceaAndETAForRoute(
				employeeTripDetail.get(0).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude(),
				assignRoutePO.geteFmFmClientBranchPO().getLatitudeLongitude(), empWayPoints.toString().replaceAll("\\s+",""));
         long totalTravelTime=Long.parseLong(plannedETAAndDistance.split("-")[1]);		
       
         if((totalTravelTime>(assignRoutePO.geteFmFmClientBranchPO().getMaxTravelTimeEmployeeWiseInMin()*60))){
					assignRoutePO.setVehicleStatus("F");
					iCabRequestBO.update(assignRoutePO);
     				clusterDetail.setClusterToRouteStatus(false);
     				routingBO.update(clusterDetail);
 					List<EFmFmAlgoRoutesPO> attachToClusterUser = routingBO.getAllEmployeesOfGivenRoute(branchId,"R"+routeCount);	
 					routingBO.deleteAnLastRouteEntry(attachToClusterUser.get(attachToClusterUser.size()-1).getAlgoRouteId());
					return "full";
         }
		
		}		

		try{
    		//for getting total route travel time
    		int pickTimeInSec = calculate.employeeETACalculation(employeeTripDetail.get(employeeTripDetail.size()-1).geteFmFmEmployeeTravelRequest().getEfmFmUserMaster().getLatitudeLongitude(),requests.get(0).getEfmFmUserMaster().getLatitudeLongitude());		
    		
    		long minutesToSec = employeeTripDetail.get(employeeTripDetail.size()-1).geteFmFmEmployeeTravelRequest().getPickUpTime().getMinutes() * 60 ;
    		long hoursToSec = employeeTripDetail.get(employeeTripDetail.size()-1).geteFmFmEmployeeTravelRequest().getPickUpTime().getHours() * 60 *60;
    		long firstPicTime=(hoursToSec+minutesToSec)+pickTimeInSec;	
    		long hours=0;
    		long min=0;
    		   if(firstPicTime>=3600){
    			   hours= (firstPicTime/60)/60;
    			    firstPicTime=firstPicTime-hours;
    		   }
    		   if(firstPicTime>=60){
    			     min = (firstPicTime / 60)%60;
    		   }
    		    String pickTime=hours+":"+min;
    	    
    	    Date shift = shiftTimeFormater.parse(pickTime);
    	    java.sql.Time firstEmpPickUpTime = new java.sql.Time(shift.getTime());    
    		requests.get(0).setPickUpTime(firstEmpPickUpTime);
   // 	}
    		}catch(Exception e){
    			log.info("PickUptime error"+e);
    		}
    		requests.get(0).setReadFlg("R");
    		requests.get(0).setRequestColor("yellow");
    		requests.get(0).setRoutingAreaCreation("N");
    		iCabRequestBO.update(requests.get(0));	
		
		EFmFmEmployeeTravelRequestPO travelRequest=new EFmFmEmployeeTravelRequestPO();
		EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
		eFmFmClientBranchPO
				.setBranchId(branchId);
		travelRequest.setRequestId(clusterDetail.geteFmFmEmployeeTravelRequest().getRequestId());					
		EFmFmEmployeeTripDetailPO employeeTripDetailPO = new EFmFmEmployeeTripDetailPO();
		if (assignRoutePO.getTripType().equalsIgnoreCase("DROP")) {
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
		employeeTripDetailPO.seteFmFmEmployeeTravelRequest(travelRequest);
		employeeTripDetailPO.setEfmFmAssignRoute(assignRoutePO);
		employeeTripDetailPO.setCurrenETA("0");
		employeeTripDetailPO.setEmployeeStatus("allocated");
		employeeTripDetailPO.setComingStatus("Yet to confirm");
		employeeTripDetailPO.setEmployeeOnboardStatus("NO");
		iCabRequestBO.update(employeeTripDetailPO);	
		if((employeeTripDetail.size()-1)==(assignRoutePO.getEfmFmVehicleCheckIn().getEfmFmVehicleMaster().getCapacity()-2)){
			assignRoutePO.setVehicleStatus("F");
			iCabRequestBO.update(assignRoutePO);
//			return "full";
     }
		
		
		return "ok";
	}
		
	public void createClusterFromRequest(EFmFmEmployeeTravelRequestPO travel,int clusterCount,boolean isClusterOrigion){
		RoutingBO routingBO = (RoutingBO) ContextLoader.getContext().getBean("RoutingBO");
	//	ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
			AreaEmpClusterMappingPO areaEmpClusterMappingPO=new AreaEmpClusterMappingPO();
			EFmFmEmployeeTravelRequestPO travelRequest=new EFmFmEmployeeTravelRequestPO();
			EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
			eFmFmClientBranchPO
					.setBranchId(travel.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId());
			areaEmpClusterMappingPO.setClusterToRouteStatus(false);
			areaEmpClusterMappingPO.setClusterName("C"+clusterCount);
			areaEmpClusterMappingPO.setClusterOregion(isClusterOrigion);
			areaEmpClusterMappingPO.setDistance(travel.getEfmFmUserMaster().getDistance());
			areaEmpClusterMappingPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
			travelRequest.setRequestId(travel.getRequestId());
			areaEmpClusterMappingPO.seteFmFmEmployeeTravelRequest(travelRequest);
			routingBO.save(areaEmpClusterMappingPO);
	//		travel.setReadFlg("R");
	//		travel.setRequestColor("yellow");
	//		travel.setRoutingAreaCreation("N");
	//		iCabRequestBO.update(travel);	
}

	
	
	public void createClusterFromArea(String clusterName,int clusterCount,int branchId){
			RoutingBO routingBO = (RoutingBO) ContextLoader.getContext().getBean("RoutingBO");
			//Query For  Getting all employees request attach to that areas and putting them in cluster.
//			List<EFmFmRoutingAreaPO> userAttachToArea = routingBO.getAllEmployeesOfGivenArea(branchId,routingAreaName);				
	//		for (EFmFmRoutingAreaPO routingRequests : userAttachToArea) {
//			if(!(userAttachToArea.isEmpty())){
				AreaEmpClusterMappingPO areaEmpClusterMappingPO=new AreaEmpClusterMappingPO();
				EFmFmEmployeeTravelRequestPO travelRequest=new EFmFmEmployeeTravelRequestPO();
				EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
				eFmFmClientBranchPO
						.setBranchId(branchId);
				areaEmpClusterMappingPO.setClusterToRouteStatus(false);
				areaEmpClusterMappingPO.setClusterName("C"+clusterCount);
				if(clusterCount==0){
				areaEmpClusterMappingPO.setClusterOregion(true);
				}else{
					areaEmpClusterMappingPO.setClusterOregion(false);
				}
		//		areaEmpClusterMappingPO.setDistance(userAttachToArea.get(0).getDistance());
				areaEmpClusterMappingPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
		//		areaEmpClusterMappingPO.seteFmFmRoutingArea(userAttachToArea.get(0));
		//		travelRequest.setRequestId(userAttachToArea.get(0).geteFmFmEmployeeTravelRequest().getRequestId());
				areaEmpClusterMappingPO.seteFmFmEmployeeTravelRequest(travelRequest);
				routingBO.save(areaEmpClusterMappingPO);
		//		userAttachToArea.get(0).setEmpsAreaToCluster(true);
		//		routingBO.update(userAttachToArea.get(0));
//				clusterCount++;
//			}
	}
	
	public void createClusterFromAreaNonOrigion(String routingAreaName,int clusterCount,boolean clusterOrigionValue,int branchId){
//		boolean clusterOrigionFlag = true;
//		    int clustersCount=0;
			RoutingBO routingBO = (RoutingBO) ContextLoader.getContext().getBean("RoutingBO");
			//Query For  Getting all employees request attach to that areas and putting them in cluster.
			List<EFmFmRoutingAreaPO> userAttachToArea = routingBO.getAllEmployeesOfGivenArea(branchId,routingAreaName);				
			for (EFmFmRoutingAreaPO routingRequests : userAttachToArea) {
				AreaEmpClusterMappingPO areaEmpClusterMappingPO=new AreaEmpClusterMappingPO();
				EFmFmEmployeeTravelRequestPO travelRequest=new EFmFmEmployeeTravelRequestPO();
				EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
				eFmFmClientBranchPO
						.setBranchId(branchId);
				areaEmpClusterMappingPO.setClusterToRouteStatus(false);
				areaEmpClusterMappingPO.setClusterName("C"+clusterCount);
					areaEmpClusterMappingPO.setClusterOregion(clusterOrigionValue);
				areaEmpClusterMappingPO.setDistance(routingRequests.getDistance());
				areaEmpClusterMappingPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
				travelRequest.setRequestId(routingRequests.geteFmFmEmployeeTravelRequest().getRequestId());
				areaEmpClusterMappingPO.seteFmFmEmployeeTravelRequest(travelRequest);
				routingBO.save(areaEmpClusterMappingPO);
				routingRequests.setEmpsAreaToCluster(true);
				routingBO.update(routingRequests);
			}
	}
	
	
	public void areaCreation(String requestDate, Time shiftTime,String tripType,int branchId) throws InvalidKeyException, NoSuchAlgorithmException, IOException, URISyntaxException {
		RoutingBO routingBO = (RoutingBO) ContextLoader.getContext().getBean("RoutingBO");
		List<EFmFmEmployeeTravelRequestPO> travelDetails = routingBO.listOfEmployeeByShiftWiseForRouting(branchId, requestDate,
				shiftTime,tripType);
//		int loopEntStmt = travelDetails.size();
		for (int i = 0; i <  travelDetails.size(); i++) {
				EFmFmRoutingAreaPO eFmFmRoutingAreaPO = new EFmFmRoutingAreaPO();
				EFmFmClientBranchPO eFmFmClientBranchPO = new EFmFmClientBranchPO();
				EFmFmEmployeeTravelRequestPO empRequest=new EFmFmEmployeeTravelRequestPO();
				eFmFmClientBranchPO
						.setBranchId(branchId);
				empRequest.setRequestId(travelDetails.get(i).getRequestId());
				eFmFmRoutingAreaPO.setOregion(true);
				eFmFmRoutingAreaPO.setEmpsAreaToCluster(false);
				eFmFmRoutingAreaPO.seteFmFmEmployeeTravelRequest(empRequest);
				eFmFmRoutingAreaPO.setRoutingAreaName("A" + i);
				eFmFmRoutingAreaPO.setDistance(travelDetails.get(i).getEfmFmUserMaster().getDistance());
				eFmFmRoutingAreaPO.setRoutingAreaStatus("Y");
				eFmFmRoutingAreaPO.seteFmFmClientBranchPO(eFmFmClientBranchPO);
				routingBO.save(eFmFmRoutingAreaPO);
		}

	}
	@POST
	@Path("/dropSequence")
	public Response changeRoutingDropSequence(EFmFmEmployeeTravelRequestPO employeeTravelRequestPO)
			throws ParseException, InvalidKeyException, NoSuchAlgorithmException, IOException, URISyntaxException {
		IAssignRouteBO assignRouteBO = (IAssignRouteBO) ContextLoader.getContext().getBean("IAssignRouteBO");
		ICabRequestBO iCabRequestBO = (ICabRequestBO) ContextLoader.getContext().getBean("ICabRequestBO");
		IUserMasterBO userMasterBO = (IUserMasterBO) ContextLoader.getContext().getBean("IUserMasterBO");
		Map<String, Object> responce = new HashMap<String, Object>();
				
		log.info("Logged In User IP Adress"+token.getClientIpAddr(httpRequest));
		try{
			if(!(userMasterBO.checkTokenValidOrNot(httpRequest.getHeader("authenticationToken"),employeeTravelRequestPO.getUserId()))){

				responce.put("status", "invalidRequest");
				return Response.ok(responce, MediaType.APPLICATION_JSON).build();
			}
			List<EFmFmUserMasterPO> userDetail = userMasterBO.getUserDetailFromUserId(employeeTravelRequestPO.getUserId());
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
		log.info("dropSequence service called");
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		String newRequestDate=dateFormat.format(new Date());		
		Date date = dateFormat.parse(newRequestDate);
		String requestDate = requestDateFormat.format(date);				
		String shiftDate=employeeTravelRequestPO.getTime();
		Date shift = shiftTimeFormater.parse(shiftDate);
		Time shiftTime = new Time(shift.getTime());		
		 List<EFmFmAssignRoutePO> routeDetails=assignRouteBO.getAllLiveTripsByShiftTime(employeeTravelRequestPO.getBranchId(), employeeTravelRequestPO.getTripType(), shiftTime,requestDate);
		if(!(routeDetails.isEmpty())){
			for(EFmFmAssignRoutePO assignRouteDetail:routeDetails){	
				int count =0;
				if (!(assignRouteDetail.getTripType().equalsIgnoreCase("PICKUP"))) {
					List<EFmFmEmployeeTripDetailPO> employeeTripDetailPO = iCabRequestBO.getParticularTripAllEmployeesDesc(assignRouteDetail.getAssignRouteId());
					if(!(employeeTripDetailPO.isEmpty())){
						log.info("employeeTripDetailPO size"+employeeTripDetailPO.size());
					for(EFmFmEmployeeTripDetailPO tripDetail:employeeTripDetailPO){	
						log.info("RequestId"+tripDetail.geteFmFmEmployeeTravelRequest().getRequestId());
						count++;
						List<EFmFmEmployeeTravelRequestPO> requests=iCabRequestBO.getParticularRequestDetailOnTripComplete(tripDetail.geteFmFmEmployeeTravelRequest().getRequestId());
						requests.get(0).setDropSequence(count);
						iCabRequestBO.update(requests.get(0));
					}
				}
				
				}
				
				
			}
			
		}
		return Response.ok(responce, MediaType.APPLICATION_JSON).build();
	}

}
