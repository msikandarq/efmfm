package com.newtglobal.eFmFmFleet.business.dao.daoImpl;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newtglobal.eFmFmFleet.business.dao.RoutingDAO;
import com.newtglobal.eFmFmFleet.model.AreaEmpClusterMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlgoRoutesPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRoutingAreaPO;

@Repository("RoutingDAO")
public class RoutingDAOImpl implements RoutingDAO {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmRoutingAreaPO eFmFmRoutingAreaPO) {
		entityManager.persist(eFmFmRoutingAreaPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmRoutingAreaPO eFmFmRoutingAreaPO) {
		entityManager.merge(eFmFmRoutingAreaPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(EFmFmRoutingAreaPO eFmFmRoutingAreaPO) {
		entityManager.remove(eFmFmRoutingAreaPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(AreaEmpClusterMappingPO areaEmpClusterMappingPO) {
		entityManager.persist(areaEmpClusterMappingPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(AreaEmpClusterMappingPO areaEmpClusterMappingPO) {
		entityManager.merge(areaEmpClusterMappingPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(AreaEmpClusterMappingPO areaEmpClusterMappingPO) {
		entityManager.remove(areaEmpClusterMappingPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO) {
		entityManager.persist(eFmFmAlgoRoutesPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO) {
		entityManager.merge(eFmFmAlgoRoutesPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(EFmFmAlgoRoutesPO eFmFmAlgoRoutesPO) {
		entityManager.remove(eFmFmAlgoRoutesPO);
	}

	// Routing logic
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteAllRoutesCreatedFromCluster() {
        Query query = entityManager
                .createQuery("DELETE EFmFmAlgoRoutesPO");
        query.executeUpdate();
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteAllClustersCreatedFromAreas() {
        Query query = entityManager
                .createQuery("DELETE AreaEmpClusterMappingPO");
        query.executeUpdate();
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteAllAreasCreatedFromEmployees() {
        Query query = entityManager
                .createQuery("DELETE EFmFmRoutingAreaPO");
        query.executeUpdate();
    }
    
    
    @Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForRouting(int branchId, String requestDate,
			Time shiftTime,String tripType) {
		List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u where b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='"
						+ requestDate + "' AND b.shiftTime='" + shiftTime
						+ "' AND (b.requestType!='guest'  AND b.requestType!='AdhocRequest' AND b.requestType!='nodal') AND b.tripType='"+tripType+"' AND b.routingAreaCreation='Y' ORDER BY u.distance DESC");
		employeeTravelRequestPO = query.getResultList();
		return employeeTravelRequestPO;
	}
    
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForPickupRouting(int branchId, String requestDate,
			Time shiftTime,String tripType) {
		List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u where b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='"
						+ requestDate + "' AND b.shiftTime='" + shiftTime
						+ "' AND (b.requestType!='guest'  AND b.requestType!='AdhocRequest' AND b.requestType!='nodal') AND b.tripType='"+tripType+"' AND b.routingAreaCreation='Y' ORDER BY u.distance DESC");
		employeeTravelRequestPO = query.getResultList();
		return employeeTravelRequestPO;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForDropRouting(int branchId, String requestDate,
			Time shiftTime,String tripType) {
		List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u where b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='"
						+ requestDate + "' AND b.shiftTime='" + shiftTime
						+ "' AND (b.requestType!='guest'  AND b.requestType!='AdhocRequest' AND b.requestType!='nodal') AND b.tripType='"+tripType+"' AND b.routingAreaCreation='Y' ORDER BY u.distance ASC");
		employeeTravelRequestPO = query.getResultList();
		return employeeTravelRequestPO;
	}
	
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmRoutingAreaPO> getLongestArea(int branchId) {
		List<EFmFmRoutingAreaPO> eFmFmRoutingAreaPO = new ArrayList<EFmFmRoutingAreaPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmRoutingAreaPO b JOIN b.eFmFmClientBranchPO c where c.branchId='" + branchId
						+ "' AND b.routingAreaStatus='Y' AND b.empsAreaToCluster=false AND b.isOregion=true ORDER BY b.distance DESC ");
		eFmFmRoutingAreaPO = query.getResultList();
		return eFmFmRoutingAreaPO;
	}
	
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmRoutingAreaPO> getParticularEmployeeAreaDetail(int routingAreaId) {
		List<EFmFmRoutingAreaPO> eFmFmRoutingAreaPO = new ArrayList<EFmFmRoutingAreaPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmRoutingAreaPO b  where b.routingAreaId='" + routingAreaId+ "'  ");
		eFmFmRoutingAreaPO = query.getResultList();
		return eFmFmRoutingAreaPO;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<AreaEmpClusterMappingPO> getParticularEmployeeClusterDetail(int clusterId) {
		List<AreaEmpClusterMappingPO> eFmFmRoutingAreaPO = new ArrayList<AreaEmpClusterMappingPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM AreaEmpClusterMappingPO b  where b.clusterId='" + clusterId+ "'  ");
		eFmFmRoutingAreaPO = query.getResultList();
		return eFmFmRoutingAreaPO;
	}	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmRoutingAreaPO> getAllEmployeesOfGivenArea(int branchId, String areaName) {
		List<EFmFmRoutingAreaPO> eFmFmRoutingAreaPO = new ArrayList<EFmFmRoutingAreaPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmRoutingAreaPO b JOIN b.eFmFmClientBranchPO c where c.branchId='" + branchId
						+ "' AND b.routingAreaStatus='Y' AND b.empsAreaToCluster=false AND  b.routingAreaName='"
						+ areaName + "' ORDER BY b.distance DESC");
		eFmFmRoutingAreaPO = query.getResultList();
		return eFmFmRoutingAreaPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmRoutingAreaPO> getNextUnassignedEmployeesOfNextArea(int branchId) {
		List<EFmFmRoutingAreaPO> eFmFmRoutingAreaPO = new ArrayList<EFmFmRoutingAreaPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmRoutingAreaPO b JOIN b.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND b.routingAreaStatus='Y' AND b.empsAreaToCluster=false ORDER BY b.distance DESC");
		eFmFmRoutingAreaPO = query.getResultList();
		return eFmFmRoutingAreaPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<AreaEmpClusterMappingPO> getLongestClusterForRoutes(int branchId) {
		List<AreaEmpClusterMappingPO> eFmFmRoutingAreaPO = new ArrayList<AreaEmpClusterMappingPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM AreaEmpClusterMappingPO b JOIN b.eFmFmClientBranchPO c where c.branchId='" + branchId
						+ "' AND b.clusterToRouteStatus=false ORDER BY b.distance DESC ");
		eFmFmRoutingAreaPO = query.getResultList();
		return eFmFmRoutingAreaPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<AreaEmpClusterMappingPO> getAllEmployeesOfGivenClusterAndArea(int branchId, String clusterName) {
		List<AreaEmpClusterMappingPO> eFmFmRoutingAreaPO = new ArrayList<AreaEmpClusterMappingPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM AreaEmpClusterMappingPO b JOIN b.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND b.clusterToRouteStatus=false AND  b.clusterName='" + clusterName + "' ORDER BY b.distance DESC ");
		eFmFmRoutingAreaPO = query.getResultList();
		return eFmFmRoutingAreaPO;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<AreaEmpClusterMappingPO> getAllEmployeesOfGivenCluster(int branchId, String clusterName) {
		List<AreaEmpClusterMappingPO> eFmFmRoutingAreaPO = new ArrayList<AreaEmpClusterMappingPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM AreaEmpClusterMappingPO b JOIN b.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND b.clusterToRouteStatus=true AND  b.clusterName='" + clusterName + "' ORDER BY b.distance DESC  ");
		eFmFmRoutingAreaPO = query.getResultList();
		return eFmFmRoutingAreaPO;
	}
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAlgoRoutesPO> getAllEmployeesOfGivenRoute(int branchId, String routeName) {
		List<EFmFmAlgoRoutesPO> eFmFmRoutingAreaPO = new ArrayList<EFmFmAlgoRoutesPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAlgoRoutesPO b JOIN b.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND  b.routeName='" + routeName + "' ORDER BY b.distance DESC ");
		eFmFmRoutingAreaPO = query.getResultList();
		return eFmFmRoutingAreaPO;
	}	
	
	
	 @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public void deleteAnLastRouteEntry(int algoRouteId) {
	        Query query = entityManager
	                .createQuery("DELETE EFmFmAlgoRoutesPO where algoRouteId = '" + algoRouteId + "' ");
	        query.executeUpdate();
	    }
	
	
	
}
