package com.newtglobal.eFmFmFleet.business.dao;

import java.sql.Time;
import java.util.List;

import com.newtglobal.eFmFmFleet.model.AreaEmpClusterMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlgoRoutesPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRoutingAreaPO;

public interface RoutingDAO {

	
	/*
	 * Method For creating the new routing areas
	 * 
	 * author Sarfraz Khan
	 * 
	 * 2 Sept 2016
	 * 
	 */
	public void save(EFmFmRoutingAreaPO eFmFmRoutingAreaPO);
	public void update(EFmFmRoutingAreaPO eFmFmRoutingAreaPO);
	public void delete(EFmFmRoutingAreaPO  eFmFmRoutingAreaPO);
	
	public void save(AreaEmpClusterMappingPO areaEmpClusterMappingPO);
	public void update(AreaEmpClusterMappingPO areaEmpClusterMappingPO);
	public void delete(AreaEmpClusterMappingPO  areaEmpClusterMappingPO);
	
	public void save(EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO);
	public void update(EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO);
	public void delete(EFmFmAlgoRoutesPO  eFmFmAlgoRoutesPO);
	
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForRouting(int branchId, String requestDate,
			Time shiftTime,String tripType);
	public List<EFmFmRoutingAreaPO> getLongestArea(int branchId);
	public List<EFmFmRoutingAreaPO> getAllEmployeesOfGivenArea(int branchId, String areaName);
	public List<EFmFmRoutingAreaPO> getNextUnassignedEmployeesOfNextArea(int branchId);
	public List<AreaEmpClusterMappingPO> getLongestClusterForRoutes(int branchId);
	public List<AreaEmpClusterMappingPO> getAllEmployeesOfGivenClusterAndArea(int branchId, String clusterName);
	public List<EFmFmRoutingAreaPO> getParticularEmployeeAreaDetail(int routingAreaId);
	public List<AreaEmpClusterMappingPO> getAllEmployeesOfGivenCluster(int branchId, String clusterName);
	public List<EFmFmAlgoRoutesPO> getAllEmployeesOfGivenRoute(int branchId, String routeName);
	public List<AreaEmpClusterMappingPO> getParticularEmployeeClusterDetail(int clusterId);
	public void deleteAllRoutesCreatedFromCluster();
	public void deleteAllClustersCreatedFromAreas();
	public void deleteAllAreasCreatedFromEmployees();
	public void deleteAnLastRouteEntry(int algoRouteId);
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForPickupRouting(int branchId, String requestDate,
			Time shiftTime, String tripType);
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForDropRouting(int branchId, String requestDate,
			Time shiftTime, String tripType);

}
