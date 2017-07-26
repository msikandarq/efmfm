package com.newtglobal.eFmFmFleet.business.dao.daoImpl;

import java.text.Format;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Date;
import java.util.Date;
import java.util.List;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TemporalType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Transactional;

import com.newtglobal.eFmFmFleet.business.dao.IVehicleCheckInDAO;
import com.newtglobal.eFmFmFleet.business.dao.IVehicleCheckInDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDynamicVehicleCheckListPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDynamicVehicleCheckListPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFixedDistanceContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFixedDistanceContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFuelChargesPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFuelChargesPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripBasedContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripBasedContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCapacityMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleInspectionPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleInspectionPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractInvoicePO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractInvoicePO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorFuelContractTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorFuelContractTypeMasterPO;

@SuppressWarnings("unchecked")
@Repository("IVehicleCheckInDAO")
public class VehicleCheckInDAOImpl implements IVehicleCheckInDAO {

    private static Log log = LogFactory.getLog(VehicleCheckInDAOImpl.class); 

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) {
		entityManager.persist(eFmFmDeviceMasterPO);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void invoiceUpdate(EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO) {
		entityManager.merge(eFmFmVendorContractInvoicePO);		
	}		
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmVehicleInspectionPO eFmFmVehicleInspectionPO) {
		entityManager.persist(eFmFmVehicleInspectionPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) {
		entityManager.merge(eFmFmDeviceMasterPO);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmVehicleInspectionPO eFmFmVehicleInspectionPO) {
		entityManager.merge(eFmFmVehicleInspectionPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) {
		entityManager.remove(eFmFmDeviceMasterPO);
	}

	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void vehicleDriverCheckIn(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		entityManager.persist(eFmFmVehicleCheckInPO);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		entityManager.merge(eFmFmVehicleCheckInPO);

	}

	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmEscortCheckInPO eFmFmEscortCheckInPO) {
		entityManager.merge(eFmFmEscortCheckInPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
		entityManager.persist(eFmFmVehicleMasterPO);

	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
		entityManager.merge(eFmFmVehicleMasterPO);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
		entityManager.remove(eFmFmVehicleMasterPO);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateEscortDetails(EFmFmEscortMasterPO eFmFmEscortMasterPO) {
		entityManager.merge(eFmFmEscortMasterPO);

	}
		
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmFixedDistanceContractDetailPO  eFmFmFixedDistanceContractDetailPO) {
		entityManager.persist(eFmFmFixedDistanceContractDetailPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmFixedDistanceContractDetailPO  eFmFmFixedDistanceContractDetailPO) {
		entityManager.merge(eFmFmFixedDistanceContractDetailPO);
	}

	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmVehicleCapacityMasterPO eFmFmVehicleCapacityMasterPO) {
		entityManager.persist(eFmFmVehicleCapacityMasterPO);

	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmVehicleCapacityMasterPO eFmFmVehicleCapacityMasterPO) {
		entityManager.merge(eFmFmVehicleCapacityMasterPO);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(EFmFmVehicleCapacityMasterPO eFmFmVehicleCapacityMasterPO) {
		entityManager.remove(eFmFmVehicleCapacityMasterPO);

	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteVehicleTypeBranchId(int vehicleCapacityId) {
		Query query = entityManager
				.createQuery("DELETE EFmFmVehicleCapacityMasterPO where vehicleCapacityId = '"+ vehicleCapacityId + "' ");
		query.executeUpdate();
	}

		
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteExtraCheckInEntry(int checkInId) {
		Query query = entityManager
				.createQuery("DELETE EFmFmVehicleCheckInPO where checkInId = '"
						+ checkInId + "' ");
		query.executeUpdate();
	}

	/**
	 * The getParticularVehicleDetails implements for Getting particular vehicle
	 * details based on vehicle Number.
	 * 
	 * @author Rajan R
	 * 
	 * @since 2015-05-06
	 */

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getParticularDriverDetailsFromMobileNum(
			String mobileNumber, int branchId) {
		List<EFmFmDriverMasterPO> driverDetail = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.mobileNumber = '"
						+ mobileNumber + "'  and c.branchId='" + branchId + "'");
		driverDetail = query.getResultList();
		return driverDetail;
	}

	/**
	 * The getParticularVehicleDetails implements for Getting particular vehicle
	 * details based on vehicle Number.
	 * 
	 * @author Rajan R
	 * 
	 * @since 2015-05-06
	 */

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public EFmFmVehicleMasterPO getParticularVehicleDetails(
			String vehicleNumber, int branchId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c "
						+ " where UPPER(REPLACE(b.vehicleNumber,' ',''))=TRIM(UPPER(REPLACE('"+vehicleNumber+"',' ',''))) and c.branchId='"+ branchId+ "'");
		try {
			vehicleDetail = query.getResultList();
		} catch (Exception e) {
		    log.info("vehicleDetail"+e);
		}
		if (vehicleDetail.isEmpty()) {
			return null;
		} else {
			return vehicleDetail.get(0);
		}

	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getVehicleExistDetailsByVehicleNumberVendorNameAndBranchId(
			String vehicleNumber,String vendorName,String branchId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.vehicleNumber = '"
						+ vehicleNumber
						+ "' AND v.vendorName='"+vendorName+"' AND c.branchId in ("+ branchId + ")");
		vehicleDetail=query.getResultList();
		return vehicleDetail;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getParticularVehicleDetailsByVehicleNumber(
			String vehicleNumber,int vendorId,String branchId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.vehicleNumber = '"
						+ vehicleNumber
						+ "' AND v.vendorId='"+vendorId+"' AND c.branchId in ("+ branchId + ")");
		vehicleDetail=query.getResultList();
		return vehicleDetail;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromVehicleNumber(
			String vehicleNumber, String branchId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.vehicleNumber like '%"
						+ vehicleNumber
						+ "%'  and c.branchId in ("+ branchId + ")");
		vehicleDetail = query.getResultList();
		return vehicleDetail;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromVehicleNumberAndVendorIdBranchId(String vehicleNumber,int vendorId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster v  where b.vehicleNumber like '%"
						+ vehicleNumber
						+ "' AND v.vendorId='"+vendorId+"'");
		vehicleDetail = query.getResultList();
		return vehicleDetail;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromVehicleNumberAndVendorId(String vehicleNumber,int vendorId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster v  where b.vehicleNumber ='"+ vehicleNumber
						+ "' AND v.vendorId='"+vendorId+"'");
		vehicleDetail = query.getResultList();
		return vehicleDetail;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromVehicleNumber(String vehicleNumber) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b  where b.vehicleNumber ='"+ vehicleNumber+"'");
		vehicleDetail = query.getResultList();
		return vehicleDetail;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getDriverDetailsFromDriverNumberAndVendorId(String mobileNumber,int vendorId) {
		List<EFmFmDriverMasterPO> driverDetails = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster v  where b.mobileNumber='"+mobileNumber+"'  AND v.vendorId='"+vendorId+"'");
		driverDetails = query.getResultList();
		return driverDetails;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getDriverDetailsFromDriverMobilrNumber(String mobileNumber) {
		List<EFmFmDriverMasterPO> driverDetails = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO b  where b.mobileNumber='"+mobileNumber+"' ");
		driverDetails = query.getResultList();
		return driverDetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteParticularCheckInEntryFromDeviceVehicleDriver(
			int checkInId) {
		Query query = entityManager
				.createQuery("DELETE EFmFmVehicleCheckInPO where checkInId = '"
						+ checkInId + "' ");
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public EFmFmVehicleMasterPO getParticularVehicleDetail(int vehicleId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO as b  where b.vehicleId='"
						+ vehicleId + "'");
		vehicleDetail = query.getResultList();
		return vehicleDetail.get(0);
	}
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public  List<EFmFmVehicleInspectionPO> getParticularVehicleInspectionDetailByInspectedId(int inspectionId,int branchId) {
		List<EFmFmVehicleInspectionPO> vehicleInspectionDetail = new ArrayList<EFmFmVehicleInspectionPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleInspectionPO as b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where b.inspectionId='"
						+ inspectionId + "' AND c.branchId='"+branchId+"' ");
		vehicleInspectionDetail = query.getResultList();
		return vehicleInspectionDetail;
	}
	

	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int getAllNonRemovedVehicleCountByBranchIdAndVendorId(int vendorId,String branchId) {
		String vehicleCount = entityManager
				.createQuery("SELECT Count(b) FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster c "
						+ " JOIN c.eFmFmClientBranchPO d where c.vendorId='"+vendorId + "' "
								+ " and d.branchId in ("+branchId+") and (b.status !='D' and b.status !='R')  ").getSingleResult().toString();
		return Integer.valueOf(vehicleCount);
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int getAllNonRemovedDriverCountByBranchIdAndVendorId(int vendorId,String branchId) {
		String driverCount = entityManager
				.createQuery("SELECT Count(b) FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster c "
						+ " JOIN c.eFmFmClientBranchPO d where c.vendorId='"+vendorId + "' "
								+ " and d.branchId in ("+branchId + ") and (b.status !='D' and b.status !='R')  ").getSingleResult().toString();
		return Integer.valueOf(driverCount);
	}
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getAllUnRemovedVehicleDetailsByBranchIdAndVendorId(String branchId,int vendorId) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster c "
						+ " JOIN c.eFmFmClientBranchPO d where c.vendorId='"+ vendorId+"' "
								+ " and d.branchId in ("+branchId + ") and (b.status !='D' and b.status !='R') ");
		vehicleMasterPO = query.getResultList();
		return vehicleMasterPO;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getAllRemovedDriverDetailsByBranchIdAndVendorId(String branchId,int vendorId) {
		List<EFmFmDriverMasterPO> driverMasterPO = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster c JOIN c.eFmFmClientBranchPO d where c.vendorId='"
						+vendorId+ "' and d.branchId in ("+branchId+") and (b.status !='D' and b.status !='R') ");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getAllVehicleDetails(
			EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster c  where c.vendorId='"
						+ eFmFmVehicleMasterPO.getEfmFmVendorMaster()
								.getVendorId() + "' and b.status !='D' ");
		vehicleMasterPO = query.getResultList();
		return vehicleMasterPO;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getAllVehicleDetailsByBranchId(int branchId) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster c JOIN c.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND b.vehicleNumber NOT LIKE '%DUMMY%' AND (b.status !='D' and b.status !='R') ");
		vehicleMasterPO = query.getResultList();
		return vehicleMasterPO;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getAllDriverDetailsByBranchIdAndVendorId(int branchId,int vendorId) {
		List<EFmFmDriverMasterPO> driverMasterPO = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster c JOIN c.eFmFmClientBranchPO d where c.vendorId='"
						+vendorId+ "' and d.branchId='"+branchId+"' and b.status !='D' ");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getAllDriverDetails(
			EFmFmDriverMasterPO eFmFmDriverMasterPO) {
		List<EFmFmDriverMasterPO> driverMasterPO = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster c JOIN c.eFmFmClientBranchPO d where c.vendorId='"
						+ eFmFmDriverMasterPO.getEfmFmVendorMaster()
								.getVendorId() + "' and b.status !='D' ");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> randomDriverDetails(int vendorId,
			Date todayDate) {
		List<EFmFmDriverMasterPO> driverMasterPO = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster d WHERE NOT EXISTS (SELECT c from b.efmFmVehicleCheckIns c where DATE(c.checkOutTime) is null AND c.status='Y') and d.vendorId='"
						+ vendorId + "' AND  b.status='A' ");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public EFmFmVehicleCheckInPO getCheckInDriverDetails(int deviceId) {
		List<EFmFmVehicleCheckInPO> driverMasterPO = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d where d.vehicleId='"
						+ deviceId + "' ORDER BY b.checkInTime desc");
		try {
			driverMasterPO = query.getResultList();
		} catch (ArrayIndexOutOfBoundsException e) {
	          log.info("driverMaster"+e);
		}
		if (!(driverMasterPO.isEmpty())) {
			return driverMasterPO.get(0);
		} else {
			return null;
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getAvailableVehicleDetails(int vendorId,
			Date todayDate) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b  JOIN b.efmFmVendorMaster d WHERE NOT EXISTS (SELECT c from b.efmFmVehicleCheckIns c where DATE(c.checkOutTime) IS NULL) and d.vendorId='"
						+ vendorId + "' AND b.status='A'");
		vehicleMasterPO = query.getResultList();
		return vehicleMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetails(
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b where b.status='Y' AND b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> checkedOutParticularDriver(
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.eFmFmDeviceMaster d where  d.deviceId='"
						+ eFmFmVehicleCheckInPO.geteFmFmDeviceMaster()
								.getDeviceId()
						+ "' AND b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}

	
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInDriverDetails(
			int branchId, int driverId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmDriverMaster d JOIN d.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where  d.driverId='"
						+ driverId
						+ "'AND c.branchId='"
						+ branchId
						+ "' AND b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInDriverDetailsByBranchIdAndVendorId(String branchId, int vendorId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmDriverMaster d JOIN d.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where  v.vendorId='"
						+ vendorId
						+ "'AND c.branchId in ("+ branchId + ") AND b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getParticularDriverCheckedInTableEntry(
			String branchId, int driverId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmDriverMaster d JOIN d.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where  d.driverId='"
						+ driverId
						+ "'AND c.branchId in ("+branchId+") ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getParticularVehicleCheckedInTableEntry(
			String branchId, int vehicleId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d "
						+ " JOIN d.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c "
						+ " where  d.vehicleId='"+ vehicleId+ "' AND c.branchId in ("+branchId+")  ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getParticularVendorCheckedInTableEntry(
			String branchId, int vendorId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where  v.vendorId='"
						+ vendorId
						+ "'AND c.branchId in ("+branchId+")  ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInDeviceDetails(
			int branchId, int deviceId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.eFmFmDeviceMaster d JOIN d.eFmFmClientBranchPO c where  d.deviceId='"
						+ deviceId
						+ "'AND c.branchId='"
						+ branchId
						+ "' AND b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getLastCheckedOutInDeviceDetails(
			int branchId, int deviceId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.eFmFmDeviceMaster d JOIN d.eFmFmClientBranchPO c where  d.deviceId='"
						+ deviceId
						+ "'AND c.branchId='"
						+ branchId
						+ "' AND b.checkOutTime is not null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInVehicles(
			String branchId, int vehicleId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where  d.vehicleId='"
						+ vehicleId
						+ "' AND c.branchId in ("+ branchId + ") AND b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInDriver(
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmDriverMaster d where  d.driverId='"
						+ eFmFmVehicleCheckInPO.getEfmFmDriverMaster()
								.getDriverId()
						+ "' AND b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getParticularDriverCheckedInDetails(int driverId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmDriverMaster d where  d.driverId='"
						+ driverId
						+ "' AND b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getParticularDeviceCheckedInDetails(int deviceId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.eFmFmDeviceMaster d where  d.deviceId='"
						+ deviceId
						+ "' AND b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}	
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetails(String branchId,
			Date todayDate) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where b.status='Y' and g.branchId in ("+ branchId + ") and b.checkOutTime is null and d.vehicleNumber NOT LIKE '%DUMMY%' ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleDetails(String branchId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g "
						+ " where g.branchId in ("+branchId+ ") and b.checkOutTime is null and d.vehicleNumber NOT LIKE '%DUMMY%' ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
		
	//Get Check in Available drivers.........
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsWithOutDummyVehicles(String branchId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where b.status='Y' and d.vehicleNumber NOT LIKE '%DUMMY%' "
						+ " and g.branchId in ("+ branchId + ") and b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
	
	 //Get Check in Available drivers allocated to routes.........
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsWithOutDummyAllocatedToRoutes(int branchId) {
			List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where b.status='N' and d.vehicleNumber NOT LIKE '%DUMMY%' and g.branchId='"
							+ branchId + "' and b.checkOutTime is null ");
			vehicleCheckIn = query.getResultList();
			return vehicleCheckIn;
		}
	
	
	//Getting all the Unread check in drivers	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getUnreadCheckedInDrivers(int branchId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO c where  c.branchId='"+ branchId + "' AND b.readFlg='N' AND b.checkOutTime is  not null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
	//Get all checkedIn entity,Which is not used for allocation till now.
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsForEditBucket(
			int branchId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where b.status='Y'  and g.branchId='"
						+ branchId + "' and b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}	
	
//Get all checkedIn entities allocated and non allocated
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleDetailsForEditBucket(
				int branchId) {
			List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where g.branchId='"
							+ branchId + "' and b.checkOutTime is null ");
			vehicleCheckIn = query.getResultList();
			return vehicleCheckIn;
		}
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmVehicleCheckInPO> getAllCheckedInDriversByVendorIdAndVehicleNum(int branchId, int vendorId,int vehicleId) {
			List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where d.vehicleId='"+vehicleId+"'and c.branchId='"
							+ branchId
							+ "' and v.vendorId='"
							+ vendorId
							+ "' and  b.checkOutTime is null ");
			vehicleCheckIn = query.getResultList();
			return vehicleCheckIn;
		}
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehiclesByVendor(
			int branchId, int vendorId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.status='Y' and c.branchId='"
						+ branchId
						+ "' and v.vendorId='"
						+ vendorId
						+ "' and  b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getAllCheckedInDriversByVendor(
			int branchId, int vendorId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmDriverMaster d JOIN d.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.status='Y' and c.branchId='"
						+ branchId
						+ "' and v.vendorId='"
						+ vendorId
						+ "' and  b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getAssignedVehicleDetails(int branchId,
			Date todayDate) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where g.branchId='"
						+ branchId + "' and b.status='N' ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEscortMasterPO> getAllEscortDetails(String branchId) {
		List<EFmFmEscortMasterPO> escortMasterPO = new ArrayList<EFmFmEscortMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmEscortMasterPO  b  "
						+ " JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c "
						+ " where b.isActive='Y' AND c.branchId in ("+branchId+")");
		escortMasterPO = query.getResultList();
		return escortMasterPO;
	}
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEscortMasterPO> getParticularEscortDetails(int escortId) {
		List<EFmFmEscortMasterPO> escortMasterPO = new ArrayList<EFmFmEscortMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmEscortMasterPO b  where b.escortId='"
						+ escortId
						+ "' and b.isActive='Y'");
		escortMasterPO = query.getResultList();
		return escortMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveEscortDetails(EFmFmEscortMasterPO eFmFmEscortMasterPO) {
		entityManager.persist(eFmFmEscortMasterPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getParticulaCheckedInVehicleDetails(
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b where b.checkInId='"
						+ eFmFmVehicleCheckInPO.getCheckInId() + "'");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsFromChecInId(
			int checkInId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b where b.checkInId='"
						+ checkInId + "'");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public EFmFmVehicleCheckInPO getCheckedInEntityDetailFromChecInId(int checkInId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b where b.checkInId='"
						+ checkInId + "'");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn.get(0);
	}
	
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int getNumberOfVehiclesFromClientId(int branchId) {
		String vehicleDetail = entityManager
				.createQuery(
						"SELECT count(b) FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where b.status='A' OR b.status='allocated' AND g.branchId='"
								+ branchId + "' ").getSingleResult().toString();
		return Integer.valueOf(vehicleDetail);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEscortCheckInPO> getAllCheckedInEscort(String branchId) {
		List<EFmFmEscortCheckInPO> escortMasterPO = new ArrayList<EFmFmEscortCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmEscortCheckInPO b JOIN b.eFmFmEscortMaster e JOIN e.efmFmVendorMaster v "
						+ " JOIN v.eFmFmClientBranchPO c where b.escortCheckOutTime is null "
						+ " AND c.branchId in ("+branchId+") AND b.status='Y'");
		escortMasterPO = query.getResultList();
		return escortMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEscortMasterPO> getAllCheckInEscort(String branchId) {
		List<EFmFmEscortMasterPO> escortMasterPO = new ArrayList<EFmFmEscortMasterPO>();		
		Query query = entityManager
				.createQuery("SELECT m FROM EFmFmEscortMasterPO m  JOIN m.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c "
						+ " WHERE NOT EXISTS (SELECT d from m.eFmFmEscortCheckIn d where d.escortCheckOutTime is null) "
						+ " AND c.branchId in ("+branchId+") AND m.isActive='Y'");
		escortMasterPO = query.getResultList();
		return escortMasterPO;
	}


	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveEscortCheckIn(EFmFmEscortCheckInPO eFmFmEscortCheckInPO) {
		entityManager.persist(eFmFmEscortCheckInPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDeviceMasterPO> deviceNumberExistsCheck(String mobileNumber,String branchId) {
		List<EFmFmDeviceMasterPO> deviceDetail = new ArrayList<EFmFmDeviceMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO c where b.mobileNumber='"
						+ mobileNumber
						+ "' AND c.branchId in ("+ branchId + ") ");
		deviceDetail = query.getResultList();
		return deviceDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDeviceMasterPO> getDeviceDetailsFromDeviceId(int deviceId,
			String branchId) {
		List<EFmFmDeviceMasterPO> deviceDetail = new ArrayList<EFmFmDeviceMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDeviceMasterPO b where b.deviceId='"
						+ deviceId + "' ");
		deviceDetail = query.getResultList();
		return deviceDetail;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDeviceMasterPO> getDummyDeviceDetailsFromDeviceId(String branchId) {
		List<EFmFmDeviceMasterPO> deviceDetail = new ArrayList<EFmFmDeviceMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO c where b.deviceType  LIKE '%Dummy%' AND c.branchId='"
						+ branchId + "' ").setMaxResults(1);
		deviceDetail = query.getResultList();
		return deviceDetail;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDeviceMasterPO> getDeviceDetailsFromDeviceIdAndBranchId(int deviceId,
			int branchId) {
		List<EFmFmDeviceMasterPO> deviceDetail = new ArrayList<EFmFmDeviceMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO c where b.deviceId='"
						+ deviceId + "' AND c.branchId='"
						+ branchId + "' ");
		deviceDetail = query.getResultList();
		return deviceDetail;
	}
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDeviceMasterPO> deviceImeiNumberExistsCheck(String branchId,String imeiNumber) {
		List<EFmFmDeviceMasterPO> deviceDetail = new ArrayList<EFmFmDeviceMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO c where b.imeiNumber='"
						+ imeiNumber
						+ "' AND c.branchId in ("+ branchId + ")");
		deviceDetail = query.getResultList();
		return deviceDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDeviceMasterPO> getListOfAllActiveDevices(String branchId) {
		List<EFmFmDeviceMasterPO> deviceDetail = new ArrayList<EFmFmDeviceMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO c "
						+ " where  c.branchId in("+ branchId+") AND b.deviceModel NOT LIKE '%Dummy%' ");
		deviceDetail = query.getResultList();
		return deviceDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDeviceMasterPO> getAllApprovedDevices(String branchId) {
		List<EFmFmDeviceMasterPO> deviceDetail = new ArrayList<EFmFmDeviceMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO c "
						+ " where  c.branchId in ("+branchId+ ") AND b.isActive='Y' AND b.status='Y' OR b.status='N'");
		deviceDetail = query.getResultList();
		return deviceDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmTripAlertsPO> getGroupByDriver(int branchId,
			Date fromDate, Date toDate) {
		List<EFmFmTripAlertsPO> eFmFmTripAlertsPO = new ArrayList<EFmFmTripAlertsPO>();
		Query query = entityManager
				.createQuery("SELECT t FROM EFmFmTripAlertsPO t JOIN t.efmFmAssignRoute a JOIN a.efmFmVehicleCheckIn v JOIN v.efmFmDriverMaster d JOIN a.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' group by d.driverId ");
		eFmFmTripAlertsPO = query.getResultList();
		return eFmFmTripAlertsPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmTripAlertsPO> getScoreCardDriver(int branchId,
			int driverId, Date fromDate, Date toDate) {
		List<EFmFmTripAlertsPO> eFmFmTripAlertsPO = new ArrayList<EFmFmTripAlertsPO>();
		Query query = entityManager
				.createQuery("SELECT t FROM EFmFmTripAlertsPO t JOIN t.efmFmAssignRoute a JOIN a.efmFmVehicleCheckIn v JOIN v.efmFmDriverMaster d JOIN a.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' and d.driverId='" + driverId + "'");
		eFmFmTripAlertsPO = query.getResultList();
		return eFmFmTripAlertsPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getDriverAssignedTrip(int branchId,
			int driverId, Date fromDate, Date toDate) {
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT a FROM EFmFmAssignRoutePO a JOIN a.efmFmVehicleCheckIn v JOIN v.efmFmDriverMaster d JOIN a.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' and d.driverId='" + driverId + "'");
		eFmFmAssignRoutePO = query.getResultList();
		return eFmFmAssignRoutePO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmTripAlertsPO> getGroupByVehicle(int branchId,
			Date fromDate, Date toDate) {
		List<EFmFmTripAlertsPO> eFmFmTripAlertsPO = new ArrayList<EFmFmTripAlertsPO>();
		Query query = entityManager
				.createQuery("SELECT t FROM EFmFmTripAlertsPO t JOIN t.efmFmAssignRoute a JOIN a.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster d JOIN a.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' group by d.vehicleId ");
		eFmFmTripAlertsPO = query.getResultList();
		return eFmFmTripAlertsPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmTripAlertsPO> getScoreCardVehicle(int branchId,
			int driverId, Date fromDate, Date toDate) {
		List<EFmFmTripAlertsPO> eFmFmTripAlertsPO = new ArrayList<EFmFmTripAlertsPO>();
		Query query = entityManager
				.createQuery("SELECT t FROM EFmFmTripAlertsPO t JOIN t.efmFmAssignRoute a JOIN a.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster d JOIN a.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' and d.vehicleId='" + driverId + "'");
		eFmFmTripAlertsPO = query.getResultList();
		return eFmFmTripAlertsPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getVehicleAssignedVehicleTrip(int branchId,
			int driverId, Date fromDate, Date toDate) {
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT a FROM EFmFmAssignRoutePO a JOIN a.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster d JOIN a.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' and d.vehicleId='" + driverId + "'");
		eFmFmAssignRoutePO = query.getResultList();
		return eFmFmAssignRoutePO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getRcNumberDetails(String rcNumber,
			int branchId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.registartionCertificateNumber = '"
						+ rcNumber + "'  and c.branchId='" + branchId + "'");
		vehicleDetail = query.getResultList();
		return vehicleDetail;

	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getVehiclesDistinctCapacityASC(int branchId) {
		List<EFmFmVehicleCheckInPO> vehicleMasterPO = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT distinct(v.capacity) FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster v JOIN v.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND b.checkOutTime is not null order by v.capacity ASC");
		List<Integer> resultList = query.getResultList();
		for (Integer listOfcapacity : resultList) {
			EFmFmVehicleMasterPO eFmFmVehicleMasterPO = new EFmFmVehicleMasterPO();
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
			int vehicleCapacity = listOfcapacity;
			eFmFmVehicleMasterPO.setCapacity(vehicleCapacity);
			eFmFmVehicleCheckInPO.setEfmFmVehicleMaster(eFmFmVehicleMasterPO);
			vehicleMasterPO.add(eFmFmVehicleCheckInPO);
		}
		return vehicleMasterPO;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getEngineNoDetails(String engineNo,int vendorId,String branchId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.vehicleEngineNumber = '"
						+ engineNo + "' and v.vendorId='"+vendorId+"' and c.branchId in ("+ branchId + ")");
		vehicleDetail = query.getResultList();
		return vehicleDetail;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getRcNumberDetails(String rcNumber,int vendorId,String branchId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.registartionCertificateNumber = '"
						+ rcNumber + "'  and v.vendorId='"+vendorId+"'and c.branchId in ("+ branchId + ")");
		vehicleDetail = query.getResultList();
		return vehicleDetail;

	}
	
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getAllApprovedVehiclesByVendorId(
			int vendorId, int branchId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where v.vendorId = '"
						+ vendorId
						+ "'  AND c.branchId='"
						+ branchId
						+ "' AND (b.status='A' OR b.status='allocated') ");
		vehicleDetail = query.getResultList();
		return vehicleDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getAllApprovedDriversByVendorId(
			int vendorId, int branchId) {
		List<EFmFmDriverMasterPO> driverDetails = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where v.vendorId = '"
						+ vendorId
						+ "'  AND c.branchId='"
						+ branchId
						+ "' AND b.status='A' ");
		driverDetails = query.getResultList();
		return driverDetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEscortMasterPO> getMobileNoDetails(
			EFmFmEscortMasterPO eFmFmEscortMasterPO) {
		List<EFmFmEscortMasterPO> escortMasterPO = new ArrayList<EFmFmEscortMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmEscortMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.mobileNumber='"
						+ eFmFmEscortMasterPO.getMobileNumber()
						+ "' and c.branchId in ("+ eFmFmEscortMasterPO.getCombinedFacility() + ")");
		escortMasterPO = query.getResultList();
		return escortMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getAllActiveVehicleDetails(int vendorId) {
		List<EFmFmVehicleMasterPO> nonCheckInVehicles = new ArrayList<EFmFmVehicleMasterPO>();
		List<EFmFmVehicleMasterPO> checkInVehicles = new ArrayList<EFmFmVehicleMasterPO>();
		List<EFmFmVehicleMasterPO> allVehicles = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b  JOIN b.efmFmVendorMaster d WHERE NOT EXISTS (SELECT c from b.efmFmVehicleCheckIns c JOIN c.eFmFmDeviceMaster n JOIN c.efmFmDriverMaster m where b.status='A' and m.status='A' and n.isActive='Y') and d.vendorId='"
						+ vendorId + "' AND b.status='A'");
		Query query1 = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b  JOIN b.efmFmVendorMaster d WHERE EXISTS (SELECT c from b.efmFmVehicleCheckIns c JOIN c.eFmFmDeviceMaster n JOIN c.efmFmDriverMaster m where b.status='A' and m.status='A' and n.isActive='Y' and c.status='Y' and (c.status='Y' and c.checkOutTime is  null) OR (c.status='N' and c.checkOutTime is not null)) and d.vendorId='"
						+ vendorId + "' AND b.status='A'");
		nonCheckInVehicles = query.getResultList();
		checkInVehicles = query1.getResultList();
		allVehicles.addAll(nonCheckInVehicles);
		allVehicles.addAll(checkInVehicles);
		return allVehicles;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getAllActiveDriverDetails(int vendorId) {
		List<EFmFmDriverMasterPO> nonCheckInDrivers = new ArrayList<EFmFmDriverMasterPO>();
		List<EFmFmDriverMasterPO> checkInDrivers = new ArrayList<EFmFmDriverMasterPO>();
		List<EFmFmDriverMasterPO> allDrivers = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster d WHERE NOT EXISTS (SELECT c from b.efmFmVehicleCheckIns c JOIN c.eFmFmDeviceMaster n JOIN c.efmFmVehicleMaster m where b.status='A' and m.status='A' and n.isActive='Y' ) and d.vendorId='"
						+ vendorId + "' AND  b.status='A' ");
		Query query1 = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster d WHERE EXISTS (SELECT c from b.efmFmVehicleCheckIns c JOIN c.eFmFmDeviceMaster n JOIN c.efmFmVehicleMaster m where b.status='A' and m.status='A' and n.isActive='Y' and (c.status='Y' and c.checkOutTime is  null) OR (c.status='N' and c.checkOutTime is not null) ) and d.vendorId='"
						+ vendorId + "' AND  b.status='A' ");

		nonCheckInDrivers = query.getResultList();
		checkInDrivers = query1.getResultList();
		allDrivers.addAll(nonCheckInDrivers);
		allDrivers.addAll(checkInDrivers);
		return allDrivers;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDeviceMasterPO> getAllActiveDeviceDetails(int branchId) {
		List<EFmFmDeviceMasterPO> nonCheckInDevice = new ArrayList<EFmFmDeviceMasterPO>();
		List<EFmFmDeviceMasterPO> checkInDevice = new ArrayList<EFmFmDeviceMasterPO>();
		List<EFmFmDeviceMasterPO> allDevices = new ArrayList<EFmFmDeviceMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO d WHERE NOT EXISTS (SELECT c from b.efmFmVehicleCheckIns c JOIN c.eFmFmDeviceMaster n JOIN c.efmFmVehicleMaster m where b.status='A' and m.status='A' and n.isActive='Y' ) and d.branchId='"
						+ branchId + "' and  b.status='Y' and b.isActive='Y' ");
		Query query1 = entityManager
				.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO d WHERE EXISTS (SELECT c from b.efmFmVehicleCheckIns c JOIN c.eFmFmDeviceMaster n JOIN c.efmFmVehicleMaster m where b.status='A' and m.status='A' and n.isActive='Y' and (c.status='Y' and c.checkOutTime is  null) OR (c.status='N' and c.checkOutTime is not null)) and d.branchId='"
						+ branchId + "' and  b.status='Y' and b.isActive='Y' ");
		nonCheckInDevice = query.getResultList();
		checkInDevice = query1.getResultList();
		allDevices.addAll(nonCheckInDevice);
		allDevices.addAll(checkInDevice);
		return allDevices;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getCheckInDetails(int branchId) {
		List<EFmFmVehicleCheckInPO> driverMasterPO = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT  distinct(v.vehicleId) FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster v JOIN v.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND b.checkOutTime is not null ");
		List<Integer> resultList = query.getResultList();
		for (Integer listOfVehicleIds : resultList) {
			EFmFmVehicleMasterPO eFmFmVehicleMasterPO = new EFmFmVehicleMasterPO();
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
			int vehicleId = listOfVehicleIds;
			eFmFmVehicleMasterPO.setVehicleId(vehicleId);
			eFmFmVehicleCheckInPO.setEfmFmVehicleMaster(eFmFmVehicleMasterPO);
			driverMasterPO.add(eFmFmVehicleCheckInPO);
		}
		return driverMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getLastCheckInEntitiesDetails(
			int branchId) {
		List<EFmFmVehicleCheckInPO> checkInDetail = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT  b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster v JOIN v.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c where c.branchId='"
						+ branchId
						+ "' AND b.checkOutTime is not null");
		/*
		 * Query
		 * query=entityManager.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b "
		 * +
		 * "JOIN b.efmFmVehicleMaster v JOIN v.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c "
		 * + "where c.branchId='"+branchId+"' and v.vehicleId in " +
		 * "(SELECT  distinct(v.vehicleId) " +
		 * "FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster v JOIN v.efmFmVendorMaster d "
		 * + "JOIN d.eFmFmClientBranchPO c JOIN b.efmFmDriverMaster m " +
		 * " where m.driverId in (SELECT  distinct(k.driverId) FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster v JOIN v.efmFmVendorMaster "
		 * +
		 * " d JOIN d.eFmFmClientBranchPO c JOIN b.efmFmDriverMaster k  JOIN b.eFmFmDeviceMaster h where c.branchId='"
		 * +branchId+"' " +
		 * " and h.deviceId in (SELECT  distinct(g.deviceId) FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster v JOIN v.efmFmVendorMaster d "
		 * +
		 * "JOIN d.eFmFmClientBranchPO c JOIN b.eFmFmDeviceMaster g where c.branchId='"
		 * +branchId+"'))) and b.checkOutTime is not null");
		 */
		checkInDetail = query.getResultList();
		return checkInDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getDriverCheckInDetails(int branchId) {
		List<EFmFmVehicleCheckInPO> driverMasterPO = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT  distinct(v.driverId) FROM EFmFmVehicleCheckInPO b JOIN b.efmFmDriverMaster v JOIN v.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND b.checkOutTime is not null ");
		List<Integer> resultList = query.getResultList();
		for (Integer listOfVehicleIds : resultList) {
			EFmFmDriverMasterPO eFmFmVehicleMasterPO = new EFmFmDriverMasterPO();
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO = new EFmFmVehicleCheckInPO();
			int vehicleId = listOfVehicleIds;
			eFmFmVehicleMasterPO.setDriverId(vehicleId);
			eFmFmVehicleCheckInPO.setEfmFmDriverMaster(eFmFmVehicleMasterPO);
			driverMasterPO.add(eFmFmVehicleCheckInPO);
		}
		return driverMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> alreadyCheckInExist(int vehicleId) {
		List<EFmFmVehicleCheckInPO> driverMasterPO = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster v JOIN v.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c where v.vehicleId='"
						+ vehicleId
						+ "' AND b.checkOutTime is not null order by b.checkOutTime desc ");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getVehicleAlreadyCheckInOrNot(int vehicleId) {
		List<EFmFmVehicleCheckInPO> driverMasterPO = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster v  where v.vehicleId='"+ vehicleId+ "' AND b.checkOutTime is  null AND b.status='Y' ");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}
	
	



	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> alreadyCheckInDriverExistence(
			int driverId) {
		List<EFmFmVehicleCheckInPO> driverMasterPO = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmDriverMaster v JOIN v.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c where v.driverId='"
						+ driverId
						+ "' AND b.checkOutTime is not null order by b.checkOutTime asc ");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> alreadyCheckInDeviceExistence(
			int deviceId, int branchId) {
		List<EFmFmVehicleCheckInPO> driverMasterPO = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.eFmFmDeviceMaster d.eFmFmClientBranchPO c where d.deviceId='"
						+ deviceId
						+ "' AND c.branchId='"
						+ branchId
						+ "' AND b.checkOutTime is not null order by b.checkOutTime asc ");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}

	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getAllDriverDetails(int branchId) {
		List<EFmFmDriverMasterPO> driverMasterPO = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster d WHERE NOT EXISTS (SELECT c from b.efmFmVehicleCheckIns c where DATE(c.checkOutTime) is null) and d.status='A' AND  b.status='A' ");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEscortCheckInPO> getParticulaEscortDetailFromEscortId(
			String branchId, int escortCheckInId) {
		List<EFmFmEscortCheckInPO> escortMasterPO = new ArrayList<EFmFmEscortCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmEscortCheckInPO b JOIN b.eFmFmEscortMaster e JOIN e.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.escortCheckOutTime is null AND c.branchId in ("+ branchId + ") AND b.escortCheckInId='"+escortCheckInId+"'");
		escortMasterPO = query.getResultList();
		return escortMasterPO;
	}

	/* Invoice */

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getVendorBasedTripSheet(Date fromDate,
			Date toDate, int branchId, int vendorId) {
		List<EFmFmVehicleMasterPO> eFmFmVehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		Query query = entityManager
				.createQuery("SELECT a.travelledDistance,COUNT(b.vehicleId),d.distanceContractId,c.contractTypeId,c.contractDescription,c.contractType "
						+ "	FROM EFmFmVehicleMasterPO b JOIN a.eFmFmContractDetails z JOIN b.efmFmVendorMaster d JOIN b.efmFmVehicleCheckIns v JOIN v.efmFmAssignRoutes a "
						+ " JOIN b.eFmFmContractDetails d JOIN z.eFmFmVendorContractTypeMaster c JOIN d.eFmFmClientBranchPO f WHERE d.vendorId='"
						+ vendorId
						+ "' AND a.tripStatus='completed' AND f.branchId='"
						+ branchId
						+ "'  AND c.contractStatus='Y' "
						+ " AND DATE(a.tripAssignDate)>='"+formatter.format(fromDate)+"' "
								+ " and DATE(a.tripAssignDate)<='"+formatter.format(toDate)+"' group by c.contractTypeId");
		List<Object[]> resultList = query.getResultList();
		for (Object[] result : resultList) {
			EFmFmVehicleMasterPO vehicleMasterPO = new EFmFmVehicleMasterPO();
			EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO = new EFmFmFixedDistanceContractDetailPO();
			vehicleMasterPO.setSumTravelledDistance(Integer.valueOf(result[0]
					.toString()));
			vehicleMasterPO.setNoOfVehicles(Integer.valueOf(result[1]
					.toString()));
			//vehicleMasterPO.setContractDetailId(Integer.valueOf(result[2].toString()));
			eFmFmFixedDistanceContractDetailPO.setDistanceContractId(Integer.valueOf(result[2].toString()));
			vehicleMasterPO.seteFmFmContractDetails(eFmFmFixedDistanceContractDetailPO);			
			/*
			contractTypeMasterPO.setContractTypeId(Integer.valueOf(result[3].toString()));
			contractTypeMasterPO.setContractDescription((String) result[4]);*/
			//vehicleMasterPO.seteFmFmVendorContractTypeMaster(contractTypeMasterPO);
			vehicleMasterPO.setContractType((String) result[5]);
			eFmFmVehicleMasterPO.add(vehicleMasterPO);
		}
		return eFmFmVehicleMasterPO;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getAllContractDetailsByVendorId(	int vendorId,String contractType, int branchId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.eFmFmContractDetails d JOIN d.eFmFmVendorContractTypeMaster f JOIN b.efmFmVendorMaster v "
						+ "JOIN v.eFmFmClientBranchPO c where v.vendorId = '"+ vendorId	+ "'  AND c.branchId='"
						+ branchId
						+ "' AND (b.status='A' OR b.status='allocated') AND f.contractStatus='Y' AND f.contractType = '"+ contractType	+ "' group by d.distanceContractId");
		vehicleDetail = query.getResultList();
		return vehicleDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceDetails(int contrctDetailsId, int branchId) {
		List<EFmFmFixedDistanceContractDetailPO> fixedDistanceContractDetailPO = new ArrayList<EFmFmFixedDistanceContractDetailPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmFixedDistanceContractDetailPO b JOIN b.eFmFmVendorContractTypeMaster cn JOIN cn.eFmFmClientBranchPO c where  c.branchId='"+branchId+ "' AND b.distanceContractId='"+contrctDetailsId+"' AND b.status='Y' AND cn.contractStatus='Y'");
	fixedDistanceContractDetailPO = query.getResultList();
		return fixedDistanceContractDetailPO;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceActiveContractDetails(int branchId) {
		List<EFmFmFixedDistanceContractDetailPO> fixedDistanceContractDetailPO = new ArrayList<EFmFmFixedDistanceContractDetailPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmFixedDistanceContractDetailPO b JOIN b.eFmFmVendorContractTypeMaster cn JOIN cn.eFmFmClientBranchPO c where  c.branchId='"+branchId+ "' AND b.status='Y' AND cn.contractStatus='Y'");
		fixedDistanceContractDetailPO = query.getResultList();
		return fixedDistanceContractDetailPO;
	}
	
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmTripBasedContractDetailPO> getTripDistanceDetails(
			EFmFmTripBasedContractDetailPO eFmFmTripBasedContractDetailPO) {
		List<EFmFmTripBasedContractDetailPO> tripBasedContractDetailPO = new ArrayList<EFmFmTripBasedContractDetailPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmTripBasedContractDetailPO b JOIN b.eFmFmClientBranchPO c where b.tripBasedContractId='"
						+ eFmFmTripBasedContractDetailPO
								.getTripBasedContractId()
						+ "' AND c.branchId='"
						+ eFmFmTripBasedContractDetailPO.getBranchId() + "'");
		tripBasedContractDetailPO = query.getResultList();
		return tripBasedContractDetailPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getVendorBasedVehicleDetails(Date fromDate,
			Date toDate, int branchId, int vendorId, String contractType,
			int contractDetailsId) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b  JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster a JOIN a.eFmFmContractDetails z JOIN a.efmFmVendorMaster d JOIN z.eFmFmVendorContractTypeMaster c JOIN d.eFmFmClientBranchPO f WHERE d.vendorId='"
						+ vendorId
						+ "' AND b.tripStatus='completed' AND f.branchId='"
						+ branchId
						+ "' AND DATE(b.tripAssignDate)>='"+formatter.format(fromDate)+"' and DATE(b.tripAssignDate)<='"+formatter.format(toDate)+"' AND c.contractStatus='Y'  AND c.contractType='"
						+ contractType
						+ "' and z.distanceContractId='"
						+ contractDetailsId + "' group by a.vehicleId");
		eFmFmAssignRoutePO = query.getResultList();
		return eFmFmAssignRoutePO;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getTripBasedVehicleDetails(Date fromDate,
			Date toDate, int branchId, int vendorId, String contractType,
			int contractDetailsId) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b  JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster a JOIN a.eFmFmContractDetails z JOIN a.efmFmVendorMaster d JOIN z.eFmFmVendorContractTypeMaster c JOIN d.eFmFmClientBranchPO f WHERE d.vendorId='"
						+ vendorId
						+ "' AND b.tripStatus='completed' AND f.branchId='"
						+ branchId
						+ "' AND DATE(b.tripAssignDate)>='"+formatter.format(fromDate)+"' and DATE(b.tripAssignDate)<='"+formatter.format(toDate)+"' AND c.contractStatus='Y' AND c.contractType='"
						+ contractType
						+ "' and z.distanceContractId='"
						+ contractDetailsId + "'");
		eFmFmAssignRoutePO = query.getResultList();
		return eFmFmAssignRoutePO;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getNoOfWorkingDays(Date fromDate,
			Date toDate, int branchId, int vehicleId, String contractType,
			int contractDetailId) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<EFmFmVehicleMasterPO> eFmFmVehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b  JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster a JOIN a.eFmFmContractDetails z JOIN a.efmFmVendorMaster d JOIN z.eFmFmVendorContractTypeMaster c JOIN d.eFmFmClientBranchPO f WHERE a.vehicleId='"
						+ vehicleId
						+ "' AND b.tripStatus='completed' AND f.branchId='"
						+ branchId
						+ "' AND DATE(b.tripAssignDate)>='"+formatter.format(fromDate)+"' and DATE(b.tripAssignDate)<='"+formatter.format(toDate)+"' "
								+ " AND c.contractStatus='Y' AND  z.distanceContractId='"
						+ contractDetailId + "' group by b.tripAssignDate");
		eFmFmVehicleMasterPO = query.getResultList();
		return eFmFmVehicleMasterPO;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getKmDetailsByVehicleId(Date fromDate,
			Date toDate, int branchId, int vehicleId, String contractType,
			int contractDetailId) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<EFmFmVehicleMasterPO> eFmFmVehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b  JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster a JOIN a.eFmFmContractDetails z JOIN a.efmFmVendorMaster d JOIN z.eFmFmVendorContractTypeMaster c JOIN d.eFmFmClientBranchPO f WHERE a.vehicleId='"
						+ vehicleId
						+ "' AND b.tripStatus='completed' AND f.branchId='"
						+ branchId
						+ "' AND DATE(b.tripAssignDate)>='"+formatter.format(fromDate)+"' and DATE(b.tripAssignDate)<='"+formatter.format(toDate)+"' AND c.contractStatus='Y' AND c.contractType='"
						+ contractType
						+ "' and z.distanceContractId='"
						+ contractDetailId + "'");
		eFmFmVehicleMasterPO = query.getResultList();
		
		return eFmFmVehicleMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getSumOfTotalKmByVehicle(Date fromDate,
			Date toDate, int branchId, int vehicleId, String contractType,
			int contractDetailId) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<EFmFmVehicleMasterPO> eFmFmVehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT SUM(b.travelledDistance),count(a.vehicleId) FROM EFmFmAssignRoutePO b  JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster a JOIN a.eFmFmContractDetails z JOIN a.efmFmVendorMaster d JOIN z.eFmFmVendorContractTypeMaster c JOIN d.eFmFmClientBranchPO f WHERE a.vehicleId='"
						+ vehicleId
						+ "' AND b.tripStatus='completed' AND f.branchId='"
						+ branchId
						+ "' AND DATE(b.tripAssignDate)>='"+formatter.format(fromDate)+"' and DATE(b.tripAssignDate)<='"+formatter.format(toDate)+"' AND c.contractStatus='Y' AND c.contractType='"
						+ contractType
						+ "' and z.distanceContractId='"
						+ contractDetailId + "' group by a.vehicleId ");
		List<Object[]> resultList = query.getResultList();
		if(!resultList.isEmpty()){
		for (Object[] result : resultList) {
			EFmFmVehicleMasterPO vehicleMasterPO = new EFmFmVehicleMasterPO();
			vehicleMasterPO.setSumTravelledDistance(Double.valueOf(result[0].toString()));
			vehicleMasterPO.setNoOfVehicles((Integer.valueOf(result[1].toString())));
			eFmFmVehicleMasterPO.add(vehicleMasterPO);
		}
		}
		return eFmFmVehicleMasterPO;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getSumOfTotalKmByVehicleOdometer(Date fromDate,
			Date toDate, int branchId, int vehicleId, String contractType,
			int contractDetailId) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<EFmFmVehicleMasterPO> eFmFmVehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT SUM(b.odometerEndKm-b.odometerStartKm),count(a.vehicleId) FROM EFmFmAssignRoutePO b  JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster a JOIN a.eFmFmContractDetails z JOIN a.efmFmVendorMaster d JOIN z.eFmFmVendorContractTypeMaster c JOIN d.eFmFmClientBranchPO f WHERE a.vehicleId='"
						+ vehicleId
						+ "' AND b.tripStatus='completed' AND f.branchId='"
						+ branchId
						+ "' AND DATE(b.tripAssignDate)>='"+formatter.format(fromDate)+"' and DATE(b.tripAssignDate)<='"+formatter.format(toDate)+"' AND c.contractStatus='Y' AND c.contractType='"
						+ contractType
						+ "' and z.distanceContractId='"
						+ contractDetailId + "' group by a.vehicleId ");
		List<Object[]> resultList = query.getResultList();
		if(!resultList.isEmpty()){
		for (Object[] result : resultList) {
			EFmFmVehicleMasterPO vehicleMasterPO = new EFmFmVehicleMasterPO();
			vehicleMasterPO.setSumTravelledDistance(Double.valueOf(result[0].toString()));
			vehicleMasterPO.setNoOfVehicles((Integer.valueOf(result[1].toString())));
			eFmFmVehicleMasterPO.add(vehicleMasterPO);
		}
		}
		return eFmFmVehicleMasterPO;
	}
	
	
	
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getVehicleBasedTripSheet(Date fromDate,
			Date toDate, int branchId, int vehicleId) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b  JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster a JOIN a.eFmFmContractDetails z JOIN a.efmFmVendorMaster d JOIN z.eFmFmVendorContractTypeMaster c JOIN d.eFmFmClientBranchPO f WHERE a.vehicleId='"
						+ vehicleId
						+ "' AND b.tripStatus='completed' AND f.branchId='"
						+ branchId
						+ "' AND DATE(b.tripAssignDate)>='"+formatter.format(fromDate)+"'  "
								+ " AND c.contractStatus='Y' and DATE(b.tripAssignDate)<='"+formatter.format(toDate)+"' "
								+ " order by c.contractType,b.tripAssignDate");
		eFmFmAssignRoutePO = query.getResultList();
		return eFmFmAssignRoutePO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getTripBasedNoOfWorkingDays(
			Date fromDate, Date toDate, int branchId, int vehicleId,
			String contractType, int contractDetailId) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<EFmFmVehicleMasterPO> eFmFmVehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT plannedDistance,COUNT(a.tripAssignDate) FROM EFmFmVehicleMasterPO b JOIN b.eFmFmContractDetails z JOIN b.efmFmVendorMaster d JOIN b.efmFmVehicleCheckIns v JOIN v.efmFmAssignRoutes a JOIN z.eFmFmVendorContractTypeMaster c JOIN d.eFmFmClientBranchPO f WHERE b.vehicleId='"
						+ vehicleId
						+ "' AND a.tripStatus='completed' AND f.branchId='"
						+ branchId
						+ "' AND DATE(a.tripAssignDate)>='"+formatter.format(fromDate)+"' and DATE(a.tripAssignDate)<='"+formatter.format(toDate)+"' "
								+ " AND c.contractStatus='Y'  AND c.contractType='"
						+ contractType
						+ "' and z.distanceContractId='"
						+ contractDetailId + "' group by a.tripAssignDate");
		List<Object[]> resultList = query.getResultList();
		for (Object[] result : resultList) {
			EFmFmVehicleMasterPO vehicleMasterPO = new EFmFmVehicleMasterPO();
			vehicleMasterPO.setSumTravelledDistance(Integer.valueOf(result[0]
					.toString()));
			vehicleMasterPO
					.setNoOfDays((Integer.valueOf(result[1].toString())));
			eFmFmVehicleMasterPO.add(vehicleMasterPO);
		}
		return eFmFmVehicleMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO) {
		entityManager.persist(eFmFmVendorContractInvoicePO);
	}

	@Override
	public void update(EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO) {
		entityManager.merge(eFmFmVendorContractInvoicePO);
	}

	@Override
	public void delete(EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO) {
		entityManager.remove(eFmFmVendorContractInvoicePO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorContractInvoicePO> getInvoiceforVendor(
			Date fromDate, Date toDate, int branchId, int vendorId,
			String invoiceType) {		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");		
		List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVendorContractInvoicePO b  JOIN b.efmFmVehicleMaster a JOIN a.efmFmVendorMaster d "
						+ " JOIN b.eFmFmClientBranchPO f WHERE d.vendorId='"+ vendorId + "' "
								+ " AND f.branchId='"+branchId+ "' AND DATE(b.invoiceStartDate)>='"+formatter.format(fromDate)+"'"
								+ " and DATE(b.invoiceEndDate)<='"+formatter.format(toDate)+"' AND b.invoiceStatus='A' AND TRIM(b.distanceFlg)=TRIM('"+invoiceType+"') group by a.vehicleId order by b.invoiceType");
		eFmFmVendorContractInvoicePO = query.getResultList();
		return eFmFmVendorContractInvoicePO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getAllVehicleDetails(int branchId) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b  JOIN b.efmFmVendorMaster d WHERE NOT EXISTS (SELECT c from b.efmFmVehicleCheckIns c where DATE(c.checkOutTime) IS NULL) and d.status='A' AND b.status='A'");
		vehicleMasterPO = query.getResultList();
		return vehicleMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorContractInvoicePO> getInvoiceByVehicleFixedDistance(
			Date fromDate, Date toDate, int branchId, int vendorId,
			int vehicleId,String disatanceFlg) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();		
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVendorContractInvoicePO b  JOIN b.efmFmVehicleMaster a  JOIN b.eFmFmClientBranchPO f WHERE a.vehicleId='"
						+ vehicleId
						+ "' AND b.invoiceStatus='A' AND b.distanceFlg='"+disatanceFlg+"' AND f.branchId='"+ branchId
						+ "' AND DATE(b.invoiceStartDate)>='"+formatter.format(fromDate)+"' "
								+ " and DATE(b.invoiceEndDate)<='"+formatter.format(toDate)+"' "
										+ " group by a.vehicleId order by b.invoiceType");
		eFmFmVendorContractInvoicePO = query.getResultList();
		return eFmFmVendorContractInvoicePO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorContractInvoicePO> getInvoiceTripBasedVehicle(
			Date fromDate, Date toDate, int branchId, int vehicleId) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVendorContractInvoicePO b  JOIN b.efmFmAssignRoute r JOIN  r.efmFmVehicleCheckIn g JOIN g.efmFmVehicleMaster a JOIN a.efmFmVendorMaster d JOIN b.eFmFmClientBranchPO f WHERE a.vehicleId='"
						+ vehicleId
						+ "'  AND b.invoiceStatus='A' AND f.branchId='"
						+ branchId
						+ "' AND DATE(b.invoiceStartDate)>='"+formatter.format(fromDate)+"' and DATE(b.invoiceEndDate)<='"+formatter.format(toDate)+"' "
								+ " order by r.tripAssignDate");
		eFmFmVendorContractInvoicePO = query.getResultList();
		return eFmFmVendorContractInvoicePO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorContractInvoicePO> getInvoiceforVendorByGroupDistance(
			Date fromDate, Date toDate, int branchId, int vendorId,String distanceFlg) {
		List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();	
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVendorContractInvoicePO b  JOIN b.efmFmVehicleMaster a JOIN a.efmFmVendorMaster d JOIN b.eFmFmClientBranchPO f WHERE d.vendorId='"
						+ vendorId
						+ "' AND b.invoiceStatus='A' AND f.branchId='" + branchId+ "' "
								+ " AND  DATE(b.invoiceStartDate)>='"+formatter.format(fromDate)+"' AND DATE(b.invoiceEndDate)<='"+formatter.format(toDate)+"' AND TRIM(b.distanceFlg)=TRIM('"+distanceFlg+"') group by b.invoiceType");
		eFmFmVendorContractInvoicePO = query.getResultList();
		return eFmFmVendorContractInvoicePO;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public  List<EFmFmVendorContractInvoicePO> deleteInvoiceData(Date fromDate, Date toDate, int branchId, int vendorId) {
		List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		Query query = entityManager
				.createQuery(" SELECT b FROM EFmFmVendorContractInvoicePO b  JOIN b.efmFmVehicleMaster a JOIN a.efmFmVendorMaster d JOIN b.eFmFmClientBranchPO f WHERE d.vendorId='"+ vendorId+ "' AND f.branchId='" + branchId+ "'"	+ " AND b.invoiceStatus='A' AND  DATE(b.invoiceStartDate)>='"+formatter.format(fromDate)+"' AND DATE(b.invoiceEndDate)<='"+formatter.format(toDate)+"'");
		eFmFmVendorContractInvoicePO = query.getResultList();
		return eFmFmVendorContractInvoicePO;	
	}
	
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorContractInvoicePO> getListOfInvoiceNumbers(
			int branchId) {
		List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVendorContractInvoicePO b  JOIN b.eFmFmClientBranchPO f WHERE f.branchId='"
						+ branchId + "' AND b.invoiceStatus='A' GROUP BY b.invoiceNumber ");
		eFmFmVendorContractInvoicePO = query.getResultList();
		return eFmFmVendorContractInvoicePO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorContractInvoicePO> getInvoiceDetails(int branchId,
			int InvoiceNumber) {
		List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVendorContractInvoicePO b JOIN b.efmFmVehicleMaster a JOIN b.eFmFmClientBranchPO f WHERE b.invoiceNumber='"
						+ InvoiceNumber
						+ "' AND b.invoiceStatus='A' AND f.branchId='"
						+ branchId
						+ "' group by a.vehicleId order by b.invoiceType");
		eFmFmVendorContractInvoicePO = query.getResultList();
		return eFmFmVendorContractInvoicePO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorContractTypeMasterPO> getContractTypeDetails(
			String contractType, int branchId) {
		List<EFmFmVendorContractTypeMasterPO> eFmFmVendorContractTypeMasterPO = new ArrayList<EFmFmVendorContractTypeMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVendorContractTypeMasterPO b  JOIN b.eFmFmClientBranchPO f WHERE f.branchId='"
						+ branchId
						+ "' and TRIM(UPPER(b.contractType))='"
						+ contractType + "' AND b.contractStatus='Y' ");
		eFmFmVendorContractTypeMasterPO = query.getResultList();
		return eFmFmVendorContractTypeMasterPO;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorContractTypeMasterPO> getAllContractType(int branchId) {
		List<EFmFmVendorContractTypeMasterPO> eFmFmVendorContractTypeMasterPO = new ArrayList<EFmFmVendorContractTypeMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVendorContractTypeMasterPO b  JOIN b.eFmFmClientBranchPO f WHERE f.branchId='"
						+ branchId + "' and b.contractStatus='Y'");
		eFmFmVendorContractTypeMasterPO = query.getResultList();
		return eFmFmVendorContractTypeMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllCheckedInVehicleCount(int branchId) {
		Query query = entityManager
				.createQuery("SELECT count(b) FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where b.status='Y' and g.branchId='"
						+ branchId + "' and b.checkOutTime is null ");
		long allCheckedInVehicleCount = (long) query.getSingleResult();
		return allCheckedInVehicleCount;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getCheckedInVehiclesOnRoad(int branchId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where b.status='N' and g.branchId='"
						+ branchId + "' and b.checkOutTime is null ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getAllActualVehicleDetailsFromBranchId(int branchId) {
		List<EFmFmVehicleMasterPO> vehicleDetail=new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.vehicleNumber NOT LIKE '%DUMMY%'  AND c.branchId='"+branchId+"' AND b.status='A' ");
		vehicleDetail=query.getResultList();	
		return vehicleDetail;	
	}
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleForCreatingNewBucket(int branchId) {
	  List <EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
	  Query query=entityManager.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where d.vehicleNumber like '%DUMMY%' and b.status='Y' and g.branchId='"+branchId+"' ");  
	  vehicleCheckIn=query.getResultList();  
	  return vehicleCheckIn;
	 
	 }
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getAllVehicleModel(int branchId) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster c JOIN c.eFmFmClientBranchPO d where d.branchId='"
						+ branchId
						+ "' and b.status ='A'  group by b.vehicleModel");
		vehicleMasterPO = query.getResultList();
		return vehicleMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getCheckInVehicle(int branchId,
			Date requestedDate) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT count(d.vehicleId),d.capacity FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where b.status='Y' and g.branchId='"
						+ branchId
						+ "' and b.checkOutTime is null and d.vehicleNumber LIKE '%DUMMY%'  group by d.capacity");
		List<Object[]> resultList = query.getResultList();
		for (Object[] result : resultList) {
			EFmFmVehicleMasterPO vehicleMaster = new EFmFmVehicleMasterPO();
			vehicleMaster
					.setNoOfVehicles((Integer.valueOf(result[0].toString())));
			vehicleMaster.setCapacity((Integer.valueOf(result[1].toString())));
			vehicleMasterPO.add(vehicleMaster);
		}
		return vehicleMasterPO;
	}
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsFromChecInAndBranchId(
			int checkInId,int branchId) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO c where b.checkInId='"
						+ checkInId + "' AND c.branchId='"+branchId+"' ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getVehicleAndDriverAttendence(Date fromDate, Date toDate,String branchId) {
		String query = "SELECT t FROM EFmFmVehicleCheckInPO t JOIN t.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO c "
				+ " WHERE   date(t.checkOutTime) >= ?1  AND   date(t.checkOutTime) <=?2  "
				+ " AND c.branchId in ("+branchId+")  ORDER BY checkOutTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2, toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}
	
	
   //Get all present drivers with vehicles
	@Override
			@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
			public List<EFmFmVehicleCheckInPO> getAllPresentDriverAndVehicles(String branchId) {
				List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
				Query query = entityManager
						.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where d.vehicleNumber NOT LIKE '%DUMMY%' "
								+ " AND g.branchId in ("+branchId+") AND b.checkOutTime is null ");
				vehicleCheckIn = query.getResultList();
				return vehicleCheckIn;
			}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getVehicleAndDriverAttendenceByVehicleId(Date fromDate, Date toDate,int branchId,int vehicleId) {
		List<EFmFmVehicleCheckInPO> eFmFmVehicleCheckInPO = new ArrayList<EFmFmVehicleCheckInPO>();
		Format formatter;  
		 formatter = new SimpleDateFormat("yyyy-MM-dd");
		Query query = entityManager
				.createQuery("SELECT t FROM EFmFmVehicleCheckInPO t JOIN t.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO c WHERE" +
						" DATE(t.checkInTime)>='"+formatter.format(fromDate)+"' AND DATE(t.checkInTime)<='"+formatter.format(toDate)+"' "
								+ " AND c.branchId='"+branchId+"' AND d.vehicleId='"+vehicleId+"' "
								+ " group by DATE(t.checkInTime) ORDER BY t.checkInTime ASC");
		eFmFmVehicleCheckInPO = query.getResultList();
		return eFmFmVehicleCheckInPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleInspectionPO> getAllVehicleInspectionsByBranchIdVehicleIdAndDate(Date fromDate, Date toDate,int branchId,int vehicleId) {
		String query = "SELECT t FROM EFmFmVehicleInspectionPO t JOIN t.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c JOIN t.efmFmVehicleMaster v WHERE t.status ='Y' AND date(t.inspectionDate) >= ?1  AND   date(t.inspectionDate) <=?2  AND c.branchId='"+branchId+"' AND v.vehicleId='"+vehicleId+"'  ORDER BY inspectionDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2, toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorContractInvoicePO> getInvoiceByContractInvoiceId(int contractInvoiceId,int branchId) {		
			List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVendorContractInvoicePO b JOIN b.efmFmVehicleMaster a JOIN b.eFmFmClientBranchPO f WHERE b.invoiceId='"
							+ contractInvoiceId
							+ "' AND b.invoiceStatus='A' AND f.branchId='"+ branchId + "'"); 
			eFmFmVendorContractInvoicePO = query.getResultList();
			return eFmFmVendorContractInvoicePO;		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getVehicleWorkingDays(Date fromDate, Date toDate, int branchId, int vehicleId) {
		List<EFmFmVehicleCheckInPO> eFmFmVehicleCheckInPO = new ArrayList<EFmFmVehicleCheckInPO>();		
		String query = "SELECT t FROM EFmFmVehicleCheckInPO t JOIN t.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO c WHERE" +
						" date(t.checkInTime)>=?1 AND date(t.checkInTime)<=?2 "
						+ "AND c.branchId='"+branchId+"' "
								+ "AND d.vehicleId='"+vehicleId+"' "
										+ "group by DATE(t.checkInTime) ORDER BY t.checkInTime ASC";		
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2, toDate, TemporalType.TIMESTAMP);
		eFmFmVehicleCheckInPO = q.getResultList();
		return eFmFmVehicleCheckInPO;
	}

		
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getSumOfTravelledKm(Date fromDate, Date toDate, int branchId, int vehicleId,String contractType, int contractDetailId) {
		List<EFmFmVehicleMasterPO> eFmFmVehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		 Format formatter;  
		 formatter = new SimpleDateFormat("yyyy-MM-dd");		   
		   Query query = entityManager.createQuery("SELECT SUM(b.travelledDistance),count(a.vehicleId) FROM EFmFmAssignRoutePO b  JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster a JOIN a.eFmFmContractDetails z JOIN a.efmFmVendorMaster d JOIN z.eFmFmVendorContractTypeMaster c JOIN d.eFmFmClientBranchPO f "
				+ "WHERE a.vehicleId='"+ vehicleId+ "' AND b.tripStatus='completed' AND f.branchId='"+ branchId+ "' "
								+ "AND date(b.tripAssignDate) >='"+formatter.format(fromDate)+"' AND date(b.tripAssignDate) <='"+formatter.format(toDate)+"' AND c.contractType='"+contractType+"' AND c.contractStatus='Y'  "
												+ "AND z.distanceContractId='"+ contractDetailId + "' group by a.vehicleId");		
		List<Object[]> resultList = query.getResultList();
		if(!resultList.isEmpty()){
		for (Object[] result : resultList) {
			EFmFmVehicleMasterPO vehicleMasterPO = new EFmFmVehicleMasterPO();
			vehicleMasterPO.setSumTravelledDistance(Double.valueOf(result[0].toString()));
			vehicleMasterPO.setNoOfVehicles((Integer.valueOf(result[1].toString())));
			eFmFmVehicleMasterPO.add(vehicleMasterPO);
		}
		}
		return eFmFmVehicleMasterPO;
	}
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmFixedDistanceContractDetailPO> getFixedModifyDistanceDetails(
            EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO) {
        List<EFmFmFixedDistanceContractDetailPO> fixedDistanceContractDetailPO = new ArrayList<EFmFmFixedDistanceContractDetailPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmFixedDistanceContractDetailPO b JOIN b.eFmFmVendorContractTypeMaster cn JOIN cn.eFmFmClientBranchPO c where  c.branchId='"+eFmFmFixedDistanceContractDetailPO.geteFmFmClientBranchPO().getBranchId() + "' AND b.distanceContractId='"+eFmFmFixedDistanceContractDetailPO.getDistanceContractId()+"' AND b.status='Y' AND cn.contractStatus='Y'");
    fixedDistanceContractDetailPO = query.getResultList();
        return fixedDistanceContractDetailPO;
    }
	@Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmVendorContractInvoicePO> getInvoiceDetailsById(int branchId, int invoiceId) {
	  List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();
	  Query query = entityManager
	    .createQuery("SELECT b FROM EFmFmVendorContractInvoicePO b JOIN b.efmFmVehicleMaster a JOIN b.eFmFmClientBranchPO f "
	      + "WHERE b.invoiceId='"+invoiceId+"' AND b.invoiceStatus='A' AND f.branchId='"+branchId+"' group by a.vehicleId order by b.invoiceType");
	  eFmFmVendorContractInvoicePO = query.getResultList();
	  return eFmFmVendorContractInvoicePO;
	 }

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEscortMasterPO> getEscortDetailsByEmpId(String escortEmployeeId, String branchId) {
		List<EFmFmEscortMasterPO> escortMasterPO = new ArrayList<EFmFmEscortMasterPO>();
		Query query = entityManager.createQuery("SELECT b FROM EFmFmEscortMasterPO b  JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.escortEmployeeId='"+escortEmployeeId+ "' and c.branchId in ("+ branchId + ")  and b.isActive='Y'");
		escortMasterPO = query.getResultList();
		return escortMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceDetailsByCloneId(int cloneId, int branchId) {	
			List<EFmFmFixedDistanceContractDetailPO> fixedDistanceContractDetailPO = new ArrayList<EFmFmFixedDistanceContractDetailPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmFixedDistanceContractDetailPO b JOIN b.eFmFmVendorContractTypeMaster cn JOIN cn.eFmFmClientBranchPO c where  c.branchId='"+branchId+ "' AND b.cloneId='"+cloneId+"' AND b.status='Y' AND cn.contractStatus='Y'");
		fixedDistanceContractDetailPO = query.getResultList();
			return fixedDistanceContractDetailPO;
		}	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> bulkUpdateContractId(int vendorId,int contractDetailsId,int oldContractDetailsId,int branchId) {
		List<EFmFmVehicleMasterPO> vehicleDetail=new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.eFmFmContractDetails d JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c "
				+ " where d.distanceContractId='"+oldContractDetailsId+"'  AND v.vendorId='"+vendorId+"' AND c.branchId='"+branchId+"' AND (b.status ='A' OR b.status ='allocated')");
		vehicleDetail=query.getResultList();	
		return vehicleDetail;	
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmFuelChargesPO eFmFmFuelChargesPO) {
		entityManager.persist(eFmFmFuelChargesPO);
		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmFuelChargesPO eFmFmFuelChargesPO) {
		entityManager.merge(eFmFmFuelChargesPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(EFmFmFuelChargesPO eFmFmFuelChargesPO) {
		entityManager.remove(eFmFmFuelChargesPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmFuelChargesPO> getFuelDetails(int fuelTypeId, int branchId) {
		List<EFmFmFuelChargesPO> eFmFmFuelChargesPO = new ArrayList<EFmFmFuelChargesPO>();
		Query query = entityManager
				.createQuery(" SELECT b FROM EFmFmFuelChargesPO b "
						+ " JOIN b.eFmFmVendorFuelContractTypeMaster c "
						+ " JOIN b.eFmFmClientBranchPO d "
						+ " where d.branchId='"+ branchId+ "'  "
								+ " AND c.fuelTypeId='"+ fuelTypeId+ "' AND c.contractStatus='Y' "
								+ " AND b.status ='Y' ORDER by b.fromDate ");
		eFmFmFuelChargesPO = query.getResultList();
		return eFmFmFuelChargesPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmFuelChargesPO> getAllFuelDetails(int branchId) {
		List<EFmFmFuelChargesPO> eFmFmFuelChargesPO = new ArrayList<EFmFmFuelChargesPO>();
		Query query = entityManager
				.createQuery(" SELECT b FROM EFmFmFuelChargesPO b "						
						+ " JOIN b.eFmFmClientBranchPO d "
						+ " where d.branchId='"+ branchId+ "'  "								
								+ " AND b.status ='Y' ");
		eFmFmFuelChargesPO = query.getResultList();
		return eFmFmFuelChargesPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getTotalKmByVehicle(Date fromDate, Date toDate, int branchId, int vehicleId,
			String contractType, int contractDetailsId) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
			Query query = entityManager.createQuery("SELECT b FROM EFmFmAssignRoutePO b  "
							+ " JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster a JOIN a.eFmFmContractDetails z JOIN a.efmFmVendorMaster d "
							+ " JOIN z.eFmFmVendorContractTypeMaster c JOIN d.eFmFmClientBranchPO f "
							+ " WHERE a.vehicleId='"+vehicleId+ "' "
									+ " AND b.tripStatus='completed' "
									+ " AND f.branchId='"+ branchId+ "' "
											+ " AND DATE(b.tripAssignDate)>='"+formatter.format(fromDate)+"'  "
											+ " and DATE(b.tripAssignDate)<='"+formatter.format(toDate)+"'  AND c.contractStatus='Y' AND c.contractType='"+ contractType+ "' "
															+ " and z.distanceContractId='"+ contractDetailsId + "' order by b.tripAssignDate");
			eFmFmAssignRoutePO = query.getResultList();			
			return eFmFmAssignRoutePO;
		}

	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorFuelContractTypeMasterPO> getAllContractFuelType(int branchId) {
			List<EFmFmVendorFuelContractTypeMasterPO> eFmFmVendorFuelContractTypeMasterPO = new ArrayList<EFmFmVendorFuelContractTypeMasterPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVendorFuelContractTypeMasterPO b  JOIN b.eFmFmClientBranchPO f WHERE f.branchId='"
							+ branchId + "' and b.contractStatus='Y'");
			eFmFmVendorFuelContractTypeMasterPO = query.getResultList();
			return eFmFmVendorFuelContractTypeMasterPO;
		}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorFuelContractTypeMasterPO> getFuelContractTypeDetails(int fuelTypeId, int branchId) {
		List<EFmFmVendorFuelContractTypeMasterPO> eFmFmVendorFuelContractTypeMasterPO = new ArrayList<EFmFmVendorFuelContractTypeMasterPO>();
		Query query = entityManager
				.createQuery(" SELECT b FROM EFmFmVendorFuelContractTypeMasterPO b "						
						+ " JOIN b.eFmFmClientBranchPO d "
						+ " where d.branchId='"+ branchId+ "'   AND b.fuelTypeId='"+ fuelTypeId+ "'"								
								+ " AND b.contractStatus ='Y' ");
		eFmFmVendorFuelContractTypeMasterPO = query.getResultList();
		return eFmFmVendorFuelContractTypeMasterPO;
	}
	
	@Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmVendorContractInvoicePO> getNonApprovalList(String branchId, String statusFlg, String invoiceflag) {
	  List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();
	  Query query = entityManager
	    .createQuery("SELECT b FROM EFmFmVendorContractInvoicePO b JOIN b.eFmFmClientBranchPO f "
	      + "WHERE b.approvalStatus='"+statusFlg+"' AND b.invoiceStatus='"+invoiceflag+"' AND f.branchId in ("+branchId+")");
	  eFmFmVendorContractInvoicePO = query.getResultList();
	  return eFmFmVendorContractInvoicePO;
	 }

	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorContractInvoicePO> getModifiedAmountDetails(int branchId, int InvoiceNumber) {
		List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();
		Query query = entityManager
				.createQuery("SELECT SUM(b.totalPenalityAmount),SUM(b.totalAmountPayable) FROM EFmFmVendorContractInvoicePO b JOIN b.efmFmVehicleMaster a JOIN b.eFmFmClientBranchPO f WHERE b.invoiceNumber='"
						+ InvoiceNumber
						+ "' AND b.invoiceStatus='A' AND f.branchId='"
						+ branchId
						+ "' group by a.vehicleId order by b.invoiceType");
		List<Object[]> resultList = query.getResultList();
		if(!resultList.isEmpty()){
		for (Object[] result : resultList) {
			EFmFmVendorContractInvoicePO vendorContractInvoicePO = new EFmFmVendorContractInvoicePO();
			vendorContractInvoicePO.setTotalPenalityAmount(Double.valueOf(result[0].toString()));
			vendorContractInvoicePO.setTotalAmountPayable((Double.valueOf(result[1].toString())));
			eFmFmVendorContractInvoicePO.add(vendorContractInvoicePO);
		}
		}
		return eFmFmVendorContractInvoicePO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmFuelChargesPO> getFuelDetailsByDate(Date fromDate, int fuelTypeId, int branchId) {
		List<EFmFmFuelChargesPO> eFmFmFuelChargesPO = new ArrayList<EFmFmFuelChargesPO>();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		Query query = entityManager
				.createQuery(" SELECT b FROM EFmFmFuelChargesPO b "
						+ " JOIN b.eFmFmVendorFuelContractTypeMaster c "
						+ " JOIN b.eFmFmClientBranchPO d "
						+ " where d.branchId='"+ branchId+ "'  "
								+ " AND DATE(b.fromDate)<='"+formatter.format(fromDate)+"'"
								+ " AND c.fuelTypeId='"+ fuelTypeId+ "' "
								+ " AND c.contractStatus='Y' AND b.status ='Y' ORDER by b.fromDate ");
		eFmFmFuelChargesPO = query.getResultList();
		return eFmFmFuelChargesPO;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleDocsPO> getAlluploadVehicleFileDetails(int vehicleId) {
		List<EFmFmVehicleDocsPO> uploadFileDetails = new ArrayList<EFmFmVehicleDocsPO>();
		Query query = entityManager
				.createQuery("SELECT a FROM EFmFmVehicleDocsPO a JOIN a.eFmFmVehicleMaster  b  where  b.vehicleId='" + vehicleId + "' order by a.vehicleDocId desc ");
		uploadFileDetails = query.getResultList();
		return uploadFileDetails;
	}
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addVehicleUploadDetails(EFmFmVehicleDocsPO eFmFmVehicleDocsPO) {
		entityManager.persist(eFmFmVehicleDocsPO);
	}
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEscortMasterPO> getAllDisableEscortDetails(String branchId) {
		List<EFmFmEscortMasterPO> escortMasterPO = new ArrayList<EFmFmEscortMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmEscortMasterPO b join b.efmFmVendorMaster v "
						+ " JOIN v.eFmFmClientBranchPO c where c.branchId in ("+branchId+") and  b.isActive !='Y'");
		escortMasterPO = query.getResultList();
		return escortMasterPO;
	}
	@Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmEscortMasterPO> getEscortDetailsByActiveFlg(int escortId) {
	  List<EFmFmEscortMasterPO> escortMasterPO = new ArrayList<EFmFmEscortMasterPO>();
	  Query query = entityManager
	    .createQuery("SELECT b FROM EFmFmEscortMasterPO b  where b.escortId='"+ escortId+ "'");
	  escortMasterPO = query.getResultList();
	  return escortMasterPO;
	 }
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getOngoingVehicleDetails(Date fromDate, Date toDate, int branchId, int vehicleId) {
			List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
			Query query = entityManager.createQuery("SELECT b FROM EFmFmAssignRoutePO b  "
							+ " JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster a JOIN a.efmFmVendorMaster d "
							+ " JOIN d.eFmFmClientBranchPO f "
							+ " WHERE a.vehicleId='"+vehicleId+ "' "
									+ " AND b.tripStatus='completed' "
									+ " AND f.branchId='"+ branchId+ "' "
											+ " AND DATE(b.tripAssignDate)>=?1 "
											+ " and DATE(b.tripAssignDate)<=?2 "
											+ " order by b.tripAssignDate").setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,toDate, TemporalType.TIMESTAMP);
			eFmFmAssignRoutePO = query.getResultList();			
			return eFmFmAssignRoutePO;
		}

	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateVehicleCheckInDetailsWithStatus(int checkInId,int numberOfTrips,long totalTravelTime,double totalTravelDistance,String status) {
        Query query = entityManager
                .createQuery("UPDATE EFmFmVehicleCheckInPO set numberOfTrips='"+numberOfTrips+"',totalTravelTime='"+totalTravelTime+"',totalTravelDistance='"+totalTravelDistance+"',status='"+status+"' WHERE checkInId = '" + checkInId + "'");
        query.executeUpdate();

    }
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateVehicleCheckInDetailsWithOutStatus(int checkInId,int numberOfTrips,long totalTravelTime,double totalTravelDistance) {
        Query query = entityManager
                .createQuery("UPDATE EFmFmVehicleCheckInPO set numberOfTrips='"+numberOfTrips+"',totalTravelTime='"+totalTravelTime+"',totalTravelDistance='"+totalTravelDistance+"' WHERE checkInId = '" + checkInId + "'");
        query.executeUpdate();

    }

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateVehicleMonthlyDistance(int vehicleId,double monthlyPendingFixedDistance,String status){
        Query query = entityManager
                .createQuery("UPDATE EFmFmVehicleMasterPO set monthlyPendingFixedDistance='"+monthlyPendingFixedDistance+"',status='"+status+"' where vehicleId='" + vehicleId + "'");
        query.executeUpdate();

    }

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getLastCheckInVehicleIdDetails(int branchId, int vehicleId) {
		List<EFmFmVehicleCheckInPO> eFmFmVehicleCheckInPO = new ArrayList<EFmFmVehicleCheckInPO>();
		List<EFmFmVehicleCheckInPO> eFmFmVehicleCheckIn= new ArrayList<EFmFmVehicleCheckInPO>();
		Format formatter;  
		formatter = new SimpleDateFormat("yyyy-MM-dd");		
		Query vehicleList = entityManager.createQuery(" SELECT t from EFmFmVehicleCheckInPO t JOIN t.efmFmVehicleMaster vm "
				+ " WHERE vm.vehicleId='"+vehicleId+"' "
						+ " group by DATE(t.checkInTime) order by t.checkInTime desc ");	
		eFmFmVehicleCheckIn =vehicleList.getResultList();	
		try{
			System.out.println("eFmFmVehicleCheckIn"+eFmFmVehicleCheckIn.get(0).getCheckInId());
		Query query = entityManager.createQuery(" SELECT t FROM EFmFmVehicleCheckInPO t JOIN t.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO c "
								+ " WHERE DATE(t.checkInTime)='"+formatter.format(eFmFmVehicleCheckIn.get(0).getCheckInTime())+"' "
								+ " AND c.branchId='"+branchId+"'" 
								+ " AND d.vehicleId='"+vehicleId+"'");	
				eFmFmVehicleCheckInPO = query.getResultList();	
		}catch(Exception e){
			log.info("Error"+e);
		}
				return eFmFmVehicleCheckInPO;
			}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getlistOfYetToCheckInVehicleDetails(int branchId) {
		List<EFmFmVehicleMasterPO> eFmFmVehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();		
		Query query = entityManager.createQuery(" SELECT f FROM EFmFmVehicleMasterPO f JOIN f.efmFmVendorMaster ven JOIN ven.eFmFmClientBranchPO cl "
				+ "	 where f.vehicleId NOT IN "
				+ " ( SELECT m.vehicleId FROM EFmFmVehicleCheckInPO r "
				+ "	 JOIN r.efmFmVehicleMaster m  WHERE m.vehicleId in "
				+ " (SELECT c.vehicleId FROM EFmFmVehicleCheckInPO t JOIN t.efmFmVehicleMaster c "
				+ " WHERE c.vehicleNumber not like '%DUMMY%' group by c.vehicleId )  "
				+ " AND r.checkOutTime is null and m.vehicleNumber not like '%DUMMY%' group by m.vehicleId ) "
				+ " AND f.vehicleNumber not like '%DUMMY%'  AND cl.branchId='"+branchId+"'");	
		eFmFmVehicleMasterPO = query.getResultList();
		return eFmFmVehicleMasterPO;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleInspectionPO> getAllVendorInspectionsByBranchIdAndDate(Date fromDate, Date toDate,int branchId) {
		String query = "SELECT t FROM EFmFmVehicleInspectionPO t JOIN t.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c WHERE t.status ='Y' AND date(t.inspectionDate) >= ?1  AND   date(t.inspectionDate) <=?2  AND c.branchId='"+branchId+"' ORDER BY inspectionDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2, toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleInspectionPO> getVendorInspectionsByBranchIdAndDate(Date fromDate, Date toDate,int branchId,int vendorId) {
		String query = "SELECT t FROM EFmFmVehicleInspectionPO t JOIN t.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c JOIN t.efmFmVehicleMaster v JOIN v.efmFmVendorMaster ven WHERE ven.vendorId='"+vendorId+"' AND t.status ='Y' AND date(t.inspectionDate) >= ?1  AND   date(t.inspectionDate) <=?2  AND c.branchId='"+branchId+"' ORDER BY inspectionDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2, toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	
	  @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTripDetailPO> getparticularEmployeeTripDetails(String vehicleNumber,int branchId) {
	        List<EFmFmEmployeeTripDetailPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
	        Query query = entityManager.createQuery(
	                "SELECT r FROM EFmFmEmployeeTripDetailPO r JOIN r.efmFmAssignRoute f JOIN r.eFmFmEmployeeTravelRequest b JOIN b.efmFmUserMaster u "
	                + " JOIN u.eFmFmClientBranchPO c where c.branchId='" + branchId + "' AND TRIM(b.routingAreaCreation)=TRIM('"+vehicleNumber+"') group by f.assignRouteId ");
	        employeeTravelRequestPO = query.getResultList();
	        return employeeTravelRequestPO;
	    }

	  @Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmDeviceMasterPO> getListOfAllavailableDevices(int branchId) {
			List<EFmFmDeviceMasterPO> deviceDetail = new ArrayList<EFmFmDeviceMasterPO>();
			Query query = entityManager.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO c where  "
					+ " c.branchId='"+branchId+"' AND b.deviceModel NOT LIKE '%Dummy%' AND b.status='Y' ");
			deviceDetail = query.getResultList();
			return deviceDetail;
		}
	  
	  @Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmVehicleCheckInPO> getParticularCheckedInVehileDetailsByBranchIdAndVendorId(int branchId, int vendorId) {
			List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where  v.vendorId='"
							+ vendorId
							+ "'AND c.branchId='"
							+ branchId
							+ "' AND b.checkOutTime is null ");
			vehicleCheckIn = query.getResultList();
			return vehicleCheckIn;
		}
	  
	  
	    @Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleByVendors(int vendorId ,int branchId) {
			List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where g.branchId='"
							+ branchId + "' AND f.vendorId='"+vendorId+"' and b.checkOutTime is null and d.vehicleNumber NOT LIKE '%DUMMY%' ");
			vehicleCheckIn = query.getResultList();
			return vehicleCheckIn;
		}

		

		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmEscortMasterPO> getEscostMobileNoDetails(String mobileNumber, String branchId) {
			List<EFmFmEscortMasterPO> escortMasterPO = new ArrayList<EFmFmEscortMasterPO>();
			Query query = entityManager.createQuery("SELECT b FROM EFmFmEscortMasterPO b "
					+ " JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c  "
					+ " where b.mobileNumber='"+mobileNumber+ "'  AND c.branchId in ("+branchId+") ");
			escortMasterPO = query.getResultList();
			return escortMasterPO;
		}
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmVehicleMasterPO> getAllVehicleVendorsDetails(int branchId) {
			List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVehicleMasterPO b  JOIN b.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c WHERE d.status='A' AND c.branchId='"+branchId+"' AND b.vehicleNumber NOT LIKE '%DUMMY%'  group by d.vendorId");
			vehicleMasterPO = query.getResultList();
			return vehicleMasterPO;
		}
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmVehicleMasterPO> getAllAvailableVehiclesByVendor(int branchId,int vendorId) {
			List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVehicleMasterPO b  JOIN b.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c WHERE NOT EXISTS "
							+ "  ( SELECT c from b.efmFmVehicleCheckIns c where c.checkOutTime is null) "
							+ " AND d.status='A' AND c.branchId='"+branchId+"' "
									+ " AND b.vehicleNumber NOT LIKE '%DUMMY%' AND d.vendorId='"+vendorId+"'");
			vehicleMasterPO = query.getResultList();
			return vehicleMasterPO;
		}
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmDriverMasterPO> getAllAvailableDriverByVendor(int branchId,int vendorId) {
			List<EFmFmDriverMasterPO> driverMasterPO = new ArrayList<EFmFmDriverMasterPO>();
			Query query = entityManager
					.createQuery(" SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c WHERE NOT EXISTS "
							+ " ( SELECT c from b.efmFmVehicleCheckIns c where c.checkOutTime is null ) "
							+ " and d.vendorId='"+ vendorId + "' AND  b.status='A' AND  b.firstName NOT LIKE '%DUMMY%' AND c.branchId='"+branchId+"' ");
			driverMasterPO = query.getResultList();
			return driverMasterPO;
		}
		
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmDeviceMasterPO> getAllAvailableDeviceWithOutDummy(int branchId) {
			List<EFmFmDeviceMasterPO> deviceDetail = new ArrayList<EFmFmDeviceMasterPO>();
			Query query = entityManager.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO d "
					+ " WHERE NOT EXISTS ( SELECT c from b.efmFmVehicleCheckIns c JOIN c.eFmFmDeviceMaster n where c.checkOutTime is null ) "
					+ " and d.branchId='"	+ branchId + "' and  b.status='Y' and b.isActive='Y' AND  b.deviceType NOT LIKE '%DUMMY%' ");
		
			deviceDetail = query.getResultList();
			return deviceDetail;
		}
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmEscortMasterPO> getEscostMobileNoWithTokenDetails(String mobileNumber,String tempCode, String branchId) {
			List<EFmFmEscortMasterPO> escortMasterPO = new ArrayList<EFmFmEscortMasterPO>();
			Query query = entityManager.createQuery("SELECT b FROM EFmFmEscortMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c  where b.mobileNumber='"+mobileNumber+ "' AND b.tempCode='" + tempCode + "' "
					+ " AND c.branchId in ("+branchId+") ");
			escortMasterPO = query.getResultList();
			return escortMasterPO;
		}
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmEscortMasterPO> getEscortPasswordDetailsFromEscortAndBranchId(int escortId,int branchId) {
			List<EFmFmEscortMasterPO> escortMasterPO = new ArrayList<EFmFmEscortMasterPO>();
			Query query = entityManager.createQuery("SELECT b FROM EFmFmEscortMasterPO b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c  where b.escortId='"+escortId+ "' AND c.branchId='"+branchId+"' ");
			escortMasterPO = query.getResultList();
			return escortMasterPO;
		}
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmVendorContractInvoicePO> getListOfGeneratedInvoiceDetails(int branchId,String invoiceStatus) {
			List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVendorContractInvoicePO b  JOIN b.eFmFmClientBranchPO f WHERE f.branchId='"
							+ branchId + "' AND b.invoiceStatus='"+invoiceStatus+"'");
			eFmFmVendorContractInvoicePO = query.getResultList();
			return eFmFmVendorContractInvoicePO;
		}
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmVendorContractInvoicePO> getListOfInvoiceDetailsByGroupInvoiceNumber(int branchId,String invoiceStatus) {
			List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoicePO = new ArrayList<EFmFmVendorContractInvoicePO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVendorContractInvoicePO b  JOIN b.eFmFmClientBranchPO f WHERE f.branchId='"
							+ branchId + "' AND b.invoiceStatus='"+invoiceStatus+"' GROUP BY b.invoiceNumber ");
			eFmFmVendorContractInvoicePO = query.getResultList();
			return eFmFmVendorContractInvoicePO;
		}
		
		@Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public void updateVehicleContractInvoiceStatus(int invoiceNumber,String invoiceStatus) {
	        Query query = entityManager
	                .createQuery("UPDATE EFmFmVendorContractInvoicePO set invoiceStatus='"+invoiceStatus+"' WHERE invoiceNumber ='"+invoiceNumber +"'");
	        query.executeUpdate();

	    }
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceDummyDetails(int branchId) {
			List<EFmFmFixedDistanceContractDetailPO> fixedDistanceContractDetailPO = new ArrayList<EFmFmFixedDistanceContractDetailPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmFixedDistanceContractDetailPO b JOIN b.eFmFmVendorContractTypeMaster cn JOIN cn.eFmFmClientBranchPO c where  c.branchId='"+branchId+ "' AND b.contractTitle='DUMMY'");
		fixedDistanceContractDetailPO = query.getResultList();
			return fixedDistanceContractDetailPO;
		}
		
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public int saveVehicleRecordWithValue(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {		
			entityManager.persist(eFmFmVehicleMasterPO);
			entityManager.flush();
			return eFmFmVehicleMasterPO.getVehicleId();
		}

			
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public void save(EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMasterPO) {
			entityManager.persist(eFmFmVendorContractTypeMasterPO);
		}
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public void update(EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMasterPO) {
			entityManager.merge(eFmFmVendorContractTypeMasterPO);		
		}	
		
						
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmVendorContractTypeMasterPO> getVendorContractTypeDetails(int branchId) {
			List<EFmFmVendorContractTypeMasterPO> eFmFmVendorContractTypeMasterPO = new ArrayList<EFmFmVendorContractTypeMasterPO>();
			Query query = entityManager
					.createQuery("SELECT b.contractStatus,b.contractTypeId,b.contractType,b.contractDescription,b.serviceTax,d.distanceContractId FROM EFmFmVendorContractTypeMasterPO b  LEFT JOIN b.eFmFmFixedDistanceContractDetail d JOIN b.eFmFmClientBranchPO c "
							+ " where  c.branchId='"+branchId+ "' "
									+ " AND b.contractStatus='Y' GROUP BY b.contractType");
			/*eFmFmVendorContractTypeMasterPO = query.getResultList();
			return eFmFmVendorContractTypeMasterPO;*/			
			List<Object[]> resultList = query.getResultList();
			if(!resultList.isEmpty()){
				for (Object[] result : resultList) { 
					EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMaster = new EFmFmVendorContractTypeMasterPO();	
					eFmFmVendorContractTypeMaster.setContractStatus(result[0].toString());
					eFmFmVendorContractTypeMaster.setContractTypeId((Integer.valueOf(result[1].toString())));					
					eFmFmVendorContractTypeMaster.setContractType(result[2].toString());
					eFmFmVendorContractTypeMaster.setContractDescription(result[3].toString());
					eFmFmVendorContractTypeMaster.setServiceTax((Double.valueOf(result[4].toString())));
					try {						
						eFmFmVendorContractTypeMaster.setEditOptionRequired(result[5].toString());	
						eFmFmVendorContractTypeMaster.setEditOptionRequired("N");	
					} catch (Exception e) {
						eFmFmVendorContractTypeMaster.setEditOptionRequired("Y");	
					}					
					eFmFmVendorContractTypeMasterPO.add(eFmFmVendorContractTypeMaster);
				}
			}
			return eFmFmVendorContractTypeMasterPO;
		}
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmVendorContractTypeMasterPO> validateVendorContractTypeDetails(String contrctType,int branchId) {
			List<EFmFmVendorContractTypeMasterPO> eFmFmVendorContractTypeMasterPO = new ArrayList<EFmFmVendorContractTypeMasterPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVendorContractTypeMasterPO b JOIN b.eFmFmClientBranchPO c where  c.branchId='"+branchId+ "' AND b.contractType='"+contrctType+"'");
			eFmFmVendorContractTypeMasterPO = query.getResultList();
			return eFmFmVendorContractTypeMasterPO;
		}
		
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmFixedDistanceContractDetailPO> getFixedContractDetailsValidation(Date fromDate, Date toDate, int branchId, int vendorId) {
				List<EFmFmFixedDistanceContractDetailPO> eFmFmFixedDistanceContractDetailPO = new ArrayList<EFmFmFixedDistanceContractDetailPO>();
				Query query = entityManager.createQuery("SELECT b FROM EFmFmFixedDistanceContractDetailPO b  "
								+ " JOIN b.efmFmVehicleMasters v JOIN v.efmFmVendorMaster d "
								+ " JOIN d.eFmFmClientBranchPO f "
								+ " WHERE d.vendorId='"+vendorId+ "' "
										+ " AND b.status='Y' "
										+ " AND f.branchId='"+ branchId+ "' "
												+ " AND DATE(b.fromDate)<=?1 "
												+ " and DATE(b.toDate)>=?2 ").setParameter(1,fromDate,TemporalType.TIMESTAMP).setParameter(2,toDate,TemporalType.TIMESTAMP);
				eFmFmFixedDistanceContractDetailPO = query.getResultList();			
				return eFmFmFixedDistanceContractDetailPO;
		}
		
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmFixedDistanceContractDetailPO> getFixedContractDetailsVehicleValidation(Date fromDate, Date toDate, int branchId, int vehicleId) {
				List<EFmFmFixedDistanceContractDetailPO> eFmFmFixedDistanceContractDetailPO = new ArrayList<EFmFmFixedDistanceContractDetailPO>();
				Query query = entityManager.createQuery("SELECT b FROM EFmFmFixedDistanceContractDetailPO b  "
								+ " JOIN b.efmFmVehicleMasters v JOIN v.efmFmVendorMaster d "
								+ " JOIN d.eFmFmClientBranchPO f "
								+ " WHERE v.vehicleId='"+vehicleId+ "' "
										+ " AND b.status='Y' "
										+ " AND f.branchId='"+ branchId+ "' "
												+ " AND DATE(b.fromDate)<=?1 "
												+ " and DATE(b.toDate)>=?2 ").setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,toDate, TemporalType.TIMESTAMP);
				eFmFmFixedDistanceContractDetailPO = query.getResultList();			
				return eFmFmFixedDistanceContractDetailPO;
		}
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public void deleteVehicleUploadDetails(int vehicleDocId) {		
			Query query = entityManager
					.createQuery("DELETE FROM EFmFmVehicleDocsPO b where  b.vehicleDocId='" + vehicleDocId + "'");
			query.executeUpdate();
			
		}
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public void deleteDriverUploadDetails(int driverDocId) {		
			Query query = entityManager
					.createQuery("DELETE FROM EFmFmDriverDocsPO b where  b.driverDocId='" + driverDocId + "'");
			query.executeUpdate();
			
		}
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmDriverDocsPO> getAlluploadFileDetails(int driverId) {
			List<EFmFmDriverDocsPO> uploadFileDetails = new ArrayList<EFmFmDriverDocsPO>();
			Query query = entityManager
					.createQuery("SELECT a FROM EFmFmDriverDocsPO a JOIN a.efmFmDriverMaster  b  where  b.driverId='" + driverId + "' order by a.driverDocId desc");
			uploadFileDetails = query.getResultList();
			return uploadFileDetails;
		}
		

		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmFixedDistanceContractDetailPO> getAllVehicleContractDetails(Date fromDate, Date toDate, int branchId, int vendorId) {
				List<EFmFmFixedDistanceContractDetailPO> eFmFmFixedDistanceContractDetailPO = new ArrayList<EFmFmFixedDistanceContractDetailPO>();
				Query query = entityManager.createQuery("SELECT b FROM EFmFmFixedDistanceContractDetailPO b  "
								+ " JOIN b.efmFmVehicleMasters v JOIN v.efmFmVendorMaster d "
								+ " JOIN d.eFmFmClientBranchPO f "
								+ " WHERE d.vendorId='"+vendorId+ "' "
										+ " AND b.status='Y' "
										+ " AND f.branchId='"+ branchId+ "' "
												+ " AND DATE(b.fromDate)<=?1 "
												+ " and DATE(b.toDate)>=?2 ").setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,toDate, TemporalType.TIMESTAMP);
				eFmFmFixedDistanceContractDetailPO = query.getResultList();			
				return eFmFmFixedDistanceContractDetailPO;
		}

		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmVendorContractTypeMasterPO> getContractTypeIdDetails(int contractTypeId, int branchId) {
			List<EFmFmVendorContractTypeMasterPO> eFmFmVendorContractTypeMasterPO = new ArrayList<EFmFmVendorContractTypeMasterPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmVendorContractTypeMasterPO b JOIN b.eFmFmClientBranchPO c "
							+ " where  c.branchId='"+branchId+ "' AND b.contractTypeId ='"+contractTypeId+"'"
									+ " AND b.contractStatus='Y'");
			eFmFmVendorContractTypeMasterPO = query.getResultList();
			return eFmFmVendorContractTypeMasterPO;
		}
		
		
		//Get all checkedIn entities allocated and non allocated
				@Override
				@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
				public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleDetailsForEditBucketByvendorId(int branchId,int vendorId) {
					List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
					Query query = entityManager
							.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g "
									+ " where g.branchId='"+ branchId + "'  AND f.vendorId='"+vendorId+"'"
											+ " and b.checkOutTime is null ");
					vehicleCheckIn = query.getResultList();
					return vehicleCheckIn;
				}
				
				
				
				
				@Override
				@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
				public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceContrctTitleExistDetails(String contractTitle,int branchId) {
					List<EFmFmFixedDistanceContractDetailPO> fixedDistanceContractDetailPO = new ArrayList<EFmFmFixedDistanceContractDetailPO>();
					Query query = entityManager
							.createQuery("SELECT b FROM EFmFmFixedDistanceContractDetailPO b JOIN b.eFmFmVendorContractTypeMaster cn JOIN cn.eFmFmClientBranchPO c "
									+ " where  c.branchId='"+branchId+ "' AND TRIM(UPPER(b.contractTitle))=UPPER('"+contractTitle+"')");
				fixedDistanceContractDetailPO = query.getResultList();
					return fixedDistanceContractDetailPO;
				}

				
				@Override
				@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
				public List<EFmFmVendorFuelContractTypeMasterPO> getDummyFuelType(String FuelType, int branchId) {
						List<EFmFmVendorFuelContractTypeMasterPO> eFmFmVendorFuelContractTypeMasterPO = new ArrayList<EFmFmVendorFuelContractTypeMasterPO>();
						Query query = entityManager
								.createQuery("SELECT b FROM EFmFmVendorFuelContractTypeMasterPO b  JOIN b.eFmFmClientBranchPO f WHERE f.branchId='"
										+ branchId + "' and b.contractType='"+FuelType+"'");
						eFmFmVendorFuelContractTypeMasterPO = query.getResultList();
						return eFmFmVendorFuelContractTypeMasterPO;
					}
		
		
				@Override
				@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
				public List<EFmFmAssignRoutePO> getPresentDaysBasedOnTrips(Date fromDate, Date toDate, int branchId, int vehicleId) {
						List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
						Query query = entityManager.createQuery("SELECT b FROM EFmFmAssignRoutePO b  "
										+ " JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster a JOIN a.efmFmVendorMaster d "
										+ " JOIN d.eFmFmClientBranchPO f "
										+ " WHERE a.vehicleId='"+vehicleId+ "' "
												+ " AND b.tripStatus='completed' "
												+ " AND f.branchId='"+ branchId+ "' "
														+ " AND DATE(b.tripAssignDate)>=?1 "
														+ " and DATE(b.tripAssignDate)<=?2 "
														+ " group by DATE(b.tripAssignDate) order by b.tripAssignDate").setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,toDate, TemporalType.TIMESTAMP);
						eFmFmAssignRoutePO = query.getResultList();			
						return eFmFmAssignRoutePO;
					}
				
				

				@Override
				@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
				public List<EFmFmAssignRoutePO> getMaxTimePresentDaysBasedOnTrips(Date travelledDate,int branchId, int vehicleId) {
						List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
						Query query = entityManager.createQuery("SELECT b FROM EFmFmAssignRoutePO b  "
										+ " JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster a JOIN a.efmFmVendorMaster d "
										+ " JOIN d.eFmFmClientBranchPO f "
										+ " WHERE a.vehicleId='"+vehicleId+ "' "
												+ " AND b.tripStatus='completed' "
												+ " AND f.branchId='"+ branchId+ "' "
														+ " AND DATE(b.tripAssignDate)=?1 "														
														+ "order by b.tripAssignDate desc").setParameter(1, travelledDate);
						eFmFmAssignRoutePO = query.getResultList();			
						return eFmFmAssignRoutePO;
				}
				
				
				@Override
				@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
				public int getAllNonRemovedSupervisorCountByBranchIdAndVendorId(int vendorId,String  branchId) {					
					String driverCount=entityManager.createQuery("SELECT Count(b) FROM EFmFmSupervisorMasterPO as b "
							+ " JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c "
							+ " where c.branchId in ("+branchId+") and  v.vendorId='"+vendorId+"' "
			    			+ " AND b.isActive='A' ").getSingleResult().toString();
					return Integer.valueOf(driverCount);
				}
				
				@Override
				@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
				public List<EFmFmDriverMasterPO> getAllDriversWithOutStatus(int branchId,int vendorId) {
					List<EFmFmDriverMasterPO> driverMasterPO = new ArrayList<EFmFmDriverMasterPO>();
					Query query = entityManager
							.createQuery("SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster c JOIN c.eFmFmClientBranchPO d where c.vendorId='"
									+vendorId+ "' and d.branchId='"+branchId+"'");
					driverMasterPO = query.getResultList();
					return driverMasterPO;
				}
				
				@Override
				@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
				public List<EFmFmVehicleMasterPO> getAllVehiclesWithOutStatus(int branchId,int vendorId) {
					List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
					Query query = entityManager
							.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster c JOIN c.eFmFmClientBranchPO d where c.vendorId='"+ vendorId+"' and d.branchId='"+branchId + "' ");
					vehicleMasterPO = query.getResultList();
					return vehicleMasterPO;
				}

				
				@Override
				@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
				public List<EFmFmDynamicVehicleCheckListPO> getAllcheckListDetails(int branchId, String checkListType) {
					List<EFmFmDynamicVehicleCheckListPO> cehckListDetails = new ArrayList<EFmFmDynamicVehicleCheckListPO>();
					Query query = entityManager
							.createQuery("SELECT b FROM EFmFmDynamicVehicleCheckListPO b JOIN b.eFmFmClientBranchPO d where b.checkListType='"
									+checkListType+ "' and d.branchId='"+branchId+"' and b.status='Y'");
					cehckListDetails = query.getResultList();
					return cehckListDetails;
				}
				
				@Override
				@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
				public List<EFmFmAssignRoutePO> getPastTripDetailsforDriver(Date fromDate, Date toDate, int branchId, int driverId) {
						List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
						Query query = entityManager.createQuery("SELECT b FROM EFmFmAssignRoutePO b  "
										+ " JOIN b.efmFmVehicleCheckIn v JOIN v.efmFmDriverMaster a JOIN a.efmFmVendorMaster d "
										+ " JOIN d.eFmFmClientBranchPO f "
										+ " WHERE a.driverId='"+driverId+ "' "
												+ " AND b.tripStatus='completed' "
												+ " AND f.branchId='"+ branchId+ "' "
														+ " AND DATE(b.tripAssignDate)>=?1 "
														+ " and DATE(b.tripAssignDate)<=?2 "
														+ " order by b.tripAssignDate").setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,toDate, TemporalType.TIMESTAMP);
						eFmFmAssignRoutePO = query.getResultList();			
						return eFmFmAssignRoutePO;
					}

				@Override
				public List<EFmFmVehicleMasterPO> getEngineNoDetails(String engineNo, int branchId) {
					return null;
				}

				
				
				@Override
				@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
				public List<EFmFmVehicleCapacityMasterPO> getVehicleTypeBranchWise(String branchId) {
					List<EFmFmVehicleCapacityMasterPO> deviceDetail = new ArrayList<EFmFmVehicleCapacityMasterPO>();
					Query query = entityManager
							.createQuery("SELECT b FROM EFmFmVehicleCapacityMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") ");
					deviceDetail = query.getResultList();
					return deviceDetail;
				}			
				
						
}