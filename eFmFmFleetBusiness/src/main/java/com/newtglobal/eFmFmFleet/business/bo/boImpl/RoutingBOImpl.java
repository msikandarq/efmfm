package com.newtglobal.eFmFmFleet.business.bo.boImpl;

import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtglobal.eFmFmFleet.business.bo.RoutingBO;
import com.newtglobal.eFmFmFleet.business.dao.RoutingDAO;
import com.newtglobal.eFmFmFleet.model.AreaEmpClusterMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlgoRoutesPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRoutingAreaPO;


@Service("RoutingBO")
public class RoutingBOImpl implements RoutingBO {
	
	
	 @Autowired
	 RoutingDAO routingDAO;

	    public void setIUserMasterDAO(RoutingDAO routingDAO) {
	        this.routingDAO = routingDAO;
	    }


	@Override
	public void save(EFmFmRoutingAreaPO eFmFmRoutingAreaPO) {
		routingDAO.save(eFmFmRoutingAreaPO);
	}

	@Override
	public void update(EFmFmRoutingAreaPO eFmFmRoutingAreaPO) {
		routingDAO.update(eFmFmRoutingAreaPO);		
	}

	@Override
	public void delete(EFmFmRoutingAreaPO eFmFmRoutingAreaPO) {
		routingDAO.delete(eFmFmRoutingAreaPO);		
	}
	
	@Override
	public void save(AreaEmpClusterMappingPO areaEmpClusterMappingPO) {
		routingDAO.save(areaEmpClusterMappingPO);
	}

	@Override
	public void update(AreaEmpClusterMappingPO areaEmpClusterMappingPO) {
		routingDAO.update(areaEmpClusterMappingPO);		
	}

	@Override
	public void delete(AreaEmpClusterMappingPO areaEmpClusterMappingPO) {
		routingDAO.delete(areaEmpClusterMappingPO);		
	}

	
	@Override
	public void save(EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO) {
		routingDAO.save(eFmFmAlgoRoutesPO);
	}

	@Override
	public void update(EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO) {
		routingDAO.update(eFmFmAlgoRoutesPO);		
	}

	@Override
	public void delete(EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO) {
		routingDAO.delete(eFmFmAlgoRoutesPO);		
	}
	
	

	@Override
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForRouting(int branchId, String requestDate,
			Time shiftTime,String tripType) {
		return routingDAO.listOfEmployeeByShiftWiseForRouting(branchId, requestDate, shiftTime,tripType);
	}


	@Override
	public List<EFmFmRoutingAreaPO> getLongestArea(int branchId) {
		return routingDAO.getLongestArea(branchId);
	}


	@Override
	public List<EFmFmRoutingAreaPO> getAllEmployeesOfGivenArea(int branchId, String areaName) {
		return routingDAO.getAllEmployeesOfGivenArea(branchId, areaName);
	}


	@Override
	public List<EFmFmRoutingAreaPO> getNextUnassignedEmployeesOfNextArea(int branchId) {
		return routingDAO.getNextUnassignedEmployeesOfNextArea(branchId);
	}


	@Override
	public List<AreaEmpClusterMappingPO> getLongestClusterForRoutes(int branchId) {
		return routingDAO.getLongestClusterForRoutes(branchId);
	}


	@Override
	public List<AreaEmpClusterMappingPO> getAllEmployeesOfGivenClusterAndArea(int branchId, String clusterName) {
		return routingDAO.getAllEmployeesOfGivenClusterAndArea(branchId, clusterName);
				
	}


	@Override
	public List<EFmFmRoutingAreaPO> getParticularEmployeeAreaDetail(int routingAreaId) {
		return routingDAO.getParticularEmployeeAreaDetail(routingAreaId);
	}


	@Override
	public List<AreaEmpClusterMappingPO> getAllEmployeesOfGivenCluster(int branchId, String clusterName) {
		return routingDAO.getAllEmployeesOfGivenCluster(branchId, clusterName);
	}


	@Override
	public List<EFmFmAlgoRoutesPO> getAllEmployeesOfGivenRoute(int branchId, String routeName) {
		return routingDAO.getAllEmployeesOfGivenRoute(branchId, routeName);
	}


	@Override
	public List<AreaEmpClusterMappingPO> getParticularEmployeeClusterDetail(int clusterId) {
		return routingDAO.getParticularEmployeeClusterDetail(clusterId);
	}


	@Override
	public void deleteAllRoutesCreatedFromCluster() {
		routingDAO.deleteAllRoutesCreatedFromCluster();
	}


	@Override
	public void deleteAllClustersCreatedFromAreas() {
		routingDAO.deleteAllClustersCreatedFromAreas();
	}


	@Override
	public void deleteAllAreasCreatedFromEmployees() {
		routingDAO.deleteAllAreasCreatedFromEmployees();
	}


	@Override
	public void deleteAnLastRouteEntry(int algoRouteId) {
		routingDAO.deleteAnLastRouteEntry(algoRouteId);
	}


	@Override
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForPickupRouting(int branchId,
			String requestDate, Time shiftTime, String tripType) {
		return routingDAO.listOfEmployeeByShiftWiseForPickupRouting(branchId, requestDate, shiftTime, tripType);
	}


	@Override
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForDropRouting(int branchId, String requestDate,
			Time shiftTime, String tripType) {
		return routingDAO.listOfEmployeeByShiftWiseForDropRouting(branchId, requestDate, shiftTime, tripType);
	}


}
