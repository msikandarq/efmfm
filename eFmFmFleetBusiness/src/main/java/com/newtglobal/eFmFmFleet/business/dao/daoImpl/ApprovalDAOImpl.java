package com.newtglobal.eFmFmFleet.business.dao.daoImpl;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Transactional;

import com.newtglobal.eFmFmFleet.business.dao.IApprovalDAO;
import com.newtglobal.eFmFmFleet.business.dao.IApprovalDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;

@Repository("IApprovalDAO")
public class ApprovalDAOImpl implements IApprovalDAO {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager _entityManager) {
		this.entityManager = _entityManager;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmDriverMasterPO eFmFmDriverMasterPO) {
		entityManager.persist(eFmFmDriverMasterPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmDriverMasterPO eFmFmDriverMasterPO) {
		entityManager.merge(eFmFmDriverMasterPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(EFmFmDriverMasterPO eFmFmDriverMasterPO) {

		entityManager.remove(eFmFmDriverMasterPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteParticularDriver(int driverId) {
		Query query = entityManager.createQuery("DELETE EFmFmDriverMasterPO where driverId = '" + driverId + "' ");
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteParticularVehicle(int vehicleId) {
		Query query = entityManager.createQuery("DELETE EFmFmVehicleMasterPO where vehicleId = '" + vehicleId + "' ");
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteParticularVendor(int vendorId) {
		Query query = entityManager.createQuery("DELETE EFmFmVendorMasterPO where vendorId = '" + vendorId + "' ");
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getAllUnapprovedDrivers(String branchId) {
		List<EFmFmDriverMasterPO> allUnApprovedDrivers = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmDriverMasterPO  b JOIN b.efmFmVendorMaster  v JOIN v.eFmFmClientBranchPO c "
				+ " where  c.branchId in ("+branchId+") AND b.status='P' ");
		allUnApprovedDrivers = query.getResultList();
		return allUnApprovedDrivers;
	}

	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getAllApprovedDriversWithOutDummy(String branchId) {
		List<EFmFmDriverMasterPO> allApprovedDrivers = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmDriverMasterPO  b JOIN b.efmFmVendorMaster  v JOIN v.eFmFmClientBranchPO c where  c.branchId in ("+ branchId + ") AND b.firstName NOT LIKE '%DUMMY%' AND ((b.status='A' OR b.status='allocated') AND b.status!='R') ");
		allApprovedDrivers = query.getResultList();
		return allApprovedDrivers;
	}

	
	@Override
	public List<EFmFmVehicleMasterPO> getAllApprovedVehiclesWithOutDummy(String branchId) {
		List<EFmFmVehicleMasterPO> allApprovedVehicles = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleMasterPO  b JOIN b.efmFmVendorMaster  v JOIN v.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.vehicleNumber NOT LIKE '%DUMMY%' AND ((b.status='A' OR b.status='allocated') AND b.status!='R') ");
		allApprovedVehicles = query.getResultList();
		return allApprovedVehicles;
	}
	
	
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getAllApprovedDrivers(int branchId) {
		List<EFmFmDriverMasterPO> allApprovedDrivers = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmDriverMasterPO  b JOIN b.efmFmVendorMaster  v JOIN v.eFmFmClientBranchPO c where  c.branchId='"
						+ branchId + "' AND b.status='A' ");
		allApprovedDrivers = query.getResultList();
		return allApprovedDrivers;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getParticularDriverDetailFromDeriverId(int driverId) {
		List<EFmFmDriverMasterPO> driverDetails = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO  b  where  b.driverId='" + driverId + "'");
		driverDetails = query.getResultList();
		return driverDetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getAllInActiveDrivers(String branchId) {
		List<EFmFmDriverMasterPO> allInactiveDrivers = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmDriverMasterPO  b JOIN b.efmFmVendorMaster  v JOIN v.eFmFmClientBranchPO c"
				+ "  where  c.branchId in ("+branchId+")  AND b.status='R' ");
		allInactiveDrivers = query.getResultList();
		return allInactiveDrivers;
	}

	@Override
	public List<EFmFmVehicleMasterPO> getAllUnapprovedVehicles(String branchId) {
		List<EFmFmVehicleMasterPO> allUnApprovedVehicles = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleMasterPO  b JOIN b.efmFmVendorMaster  v JOIN v.eFmFmClientBranchPO c "
				+ " where  c.branchId in ("+branchId+")  AND b.status='P'");
		allUnApprovedVehicles = query.getResultList();
		return allUnApprovedVehicles;

	}

	@Override
	public List<EFmFmVehicleMasterPO> getAllApprovedVehicles(String branchId) {
		List<EFmFmVehicleMasterPO> allApprovedVehicles = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleMasterPO  b JOIN b.efmFmVendorMaster  v JOIN v.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.status='A' ");
		allApprovedVehicles = query.getResultList();
		return allApprovedVehicles;
	}

	@Override
	public List<EFmFmVehicleMasterPO> getAllInActiveVehicles(String branchId) {
		List<EFmFmVehicleMasterPO> allUnApprovedDrivers = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleMasterPO  b JOIN b.efmFmVendorMaster  v JOIN v.eFmFmClientBranchPO c "
				+ " where  c.branchId in ("+branchId+")  AND b.status='R' ");
		allUnApprovedDrivers = query.getResultList();
		return allUnApprovedDrivers;
	}

	@Override
	public List<EFmFmVendorMasterPO> getAllUnapprovedVendors(String branchId) {
		List<EFmFmVendorMasterPO> allUnApprovedVendors = new ArrayList<EFmFmVendorMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVendorMasterPO b JOIN b.eFmFmClientBranchPO c  "
						+ " where c.branchId in ("+branchId+")  AND b.status='P' ");
		allUnApprovedVendors = query.getResultList();
		return allUnApprovedVendors;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorMasterPO> getAllApprovedVendors(String branchId) {
		List<EFmFmVendorMasterPO> allApprovedVendors = new ArrayList<EFmFmVendorMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVendorMasterPO b JOIN b.eFmFmClientBranchPO c  "
						+ " where c.branchId in ("+branchId+")  AND b.status='A' AND b.vendorName NOT LIKE '%DUMMY%'");
		allApprovedVendors = query.getResultList();
		return allApprovedVendors;
	}

	@Override
	public List<EFmFmVendorMasterPO> getAllInActiveVendors(String branchId) {
		List<EFmFmVendorMasterPO> allUnApprovedVendors = new ArrayList<EFmFmVendorMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVendorMasterPO b JOIN b.eFmFmClientBranchPO c  "
						+ " where c.branchId in ("+branchId+")  AND b.status='R' AND b.vendorName NOT LIKE '%DUMMY%'");
		allUnApprovedVendors = query.getResultList();
		return allUnApprovedVendors;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public EFmFmDriverMasterPO getParticularDriverDetail(int driverId) {
		List<EFmFmDriverMasterPO> driverDetail = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO as b  where b.driverId='" + driverId + "'");
		driverDetail = query.getResultList();
		return driverDetail.get(0);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveRouteTripDetails(EFmFmEmployeeTripDetailPO eFmFmEmployeeTripDetailPO) {
		entityManager.merge(eFmFmEmployeeTripDetailPO);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getParticularDriverDeviceDetails(String mobileNo, String branchId) {
		List<EFmFmDriverMasterPO> driverMasterPO = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.mobileNo = '" + mobileNo + "'");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getParticularDriverDetailsFromDeviceToken(String deviceId, int branchId) {
		List<EFmFmDriverMasterPO> driverMasterPO = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c  where c.branchId='"
						+ branchId + "' AND b.deviceId = '" + deviceId + "' ");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverDocsPO> getAlluploadFileDetails(int driverId) {
		List<EFmFmDriverDocsPO> uploadFileDetails = new ArrayList<EFmFmDriverDocsPO>();
		Query query = entityManager
				.createQuery("SELECT a FROM EFmFmDriverDocsPO a JOIN a.efmFmDriverMaster  b  where  b.driverId='" + driverId + "' and a.status='Y' order by a.driverDocId desc");
		uploadFileDetails = query.getResultList();
		return uploadFileDetails;
	}
		
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addUploadDetails(EFmFmDriverDocsPO eFmFmDriverDocsPO) {
		entityManager.persist(eFmFmDriverDocsPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEscortDocsPO> getAllEscortuploadFileDetails(int escortId) {
		List<EFmFmEscortDocsPO> uploadFileDetails = new ArrayList<EFmFmEscortDocsPO>();
		Query query = entityManager
				.createQuery("SELECT a FROM EFmFmEscortDocsPO a JOIN a.eFmFmEscortMaster  b  where  b.escortId='" +escortId+"' and a.status='Y' order by a.escortDocId desc ");
		uploadFileDetails = query.getResultList();
		return uploadFileDetails;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addUploadDetails(EFmFmEscortDocsPO eFmFmEscortDocsPO) {
		entityManager.persist(eFmFmEscortDocsPO);		
	}

}