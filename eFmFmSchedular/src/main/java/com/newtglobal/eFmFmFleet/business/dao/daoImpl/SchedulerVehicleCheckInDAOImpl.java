package com.newtglobal.eFmFmFleet.business.dao.daoImpl;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newtglobal.eFmFmFleet.model.EFmCheckInVehicleTrackingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmActualRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientUserRolePO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeRequestMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLiveRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;

@SuppressWarnings("unchecked")
@Repository("IVehicleCheckInDAO")
public class SchedulerVehicleCheckInDAOImpl {

	private EntityManager entityManager;

	public SchedulerVehicleCheckInDAOImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
		entityManager.persist(eFmFmVehicleMasterPO);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmEmployeeTripDetailPO eFmFmEmployeeTripDetailPO) {
		entityManager.persist(eFmFmEmployeeTripDetailPO);

	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
		entityManager.merge(eFmFmVehicleMasterPO);

	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmCheckInVehicleTrackingPO eFmCheckInVehicleTrackingPO) {
		entityManager.merge(eFmCheckInVehicleTrackingPO);

	}


	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetails(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		Query query = entityManager.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b where DATE(b.checkInTime)=TRIM('"
				+ formatter.format(eFmFmVehicleCheckInPO.getCheckInTime()) + "')) ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleLessCapacity(int branchId, int capacity) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where d.capacity<='"
						+ capacity + "' and d.vehicleNumber like '%DUMMY%' and b.status='Y' and g.branchId='" + branchId
						+ "' ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;

	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleLargeCapacity(int branchId, int capacity) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where d.capacity>='"
						+ capacity + "'  and d.vehicleNumber like '%DUMMY%' and g.branchId='" + branchId
						+ "' and b.status='Y'");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleDetails(int branchId, Date todayDate) {
		List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where g.branchId='"
						+ branchId + "'  ");
		vehicleCheckIn = query.getResultList();
		return vehicleCheckIn;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromClientIdForSmallerRoute(int branchId, String vehicleId) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND b.status='A' AND b.vehicleId in (" + vehicleId + ") ORDER BY capacity ASC");
		vehicleMasterPO = query.getResultList();
		return vehicleMasterPO;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromClientIdForLargerRoute(int branchId, String vehicleId) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND b.status='A' AND b.vehicleId in (" + vehicleId
						+ ") ORDER BY capacity DESC ");
		vehicleMasterPO = query.getResultList();
		return vehicleMasterPO;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getLessTravelledVehicle(int branchId, String vehicleId) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND b.status='A' AND b.vehicleId in (" + vehicleId
						+ ") ORDER BY b.pendingKM DESC ");
		vehicleMasterPO = query.getResultList();
		return vehicleMasterPO;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeRequestMasterPO> getParticularEmployeeMasterRequestDetails(int branchId, int tripId) {
		List<EFmFmEmployeeRequestMasterPO> employeeRequest = new ArrayList<EFmFmEmployeeRequestMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO  c where b.tripId='"
						+ tripId + "' AND  c.branchId='" + branchId + "' ");
		employeeRequest = query.getResultList();
		return employeeRequest;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getParticularAssignRouteDetail(int branchId, int checkInId) {
		List<EFmFmAssignRoutePO> assignVehicleDetail = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.efmFmVehicleCheckIn v  JOIN v.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vd JOIN vd.eFmFmClientBranchPO c where c.branchId ='"
						+ branchId + "' and v.checkInId='" + checkInId + "' ");
		assignVehicleDetail = query.getResultList();
		return assignVehicleDetail;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteFromCheckInId(int branchId, int zoneId, String reqType,
			Time shiftTime, int checkInId) {
		List<EFmFmAssignRoutePO> assignVehicleDetail = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn vc where b.vehicleStatus='A' AND b.tripStatus='allocated' AND c.branchId ='"
						+ branchId + "' AND vm.zoneId='" + zoneId + "' AND b.tripType='" + reqType
						+ "' AND b.shiftTime='" + shiftTime + "'  AND vc.checkInId='" + checkInId + "'");
		assignVehicleDetail = query.getResultList();
		return assignVehicleDetail;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRoute(int branchId, int zoneId, String reqType,
			Time shiftTime) {
		List<EFmFmAssignRoutePO> assignVehicleDetail = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn vc  JOIN vc.efmFmVehicleMaster vem where b.vehicleStatus='A' AND b.tripStatus='allocated' AND c.branchId ='"
						+ branchId + "' AND vm.zoneId='" + zoneId + "' AND b.tripType='" + reqType
						+ "' AND b.shiftTime='" + shiftTime + "'  ORDER BY vem.availableSeat DESC ");
		assignVehicleDetail = query.getResultList();
		return assignVehicleDetail;
	}

	/*
	 * @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 * public List<EFmFmAssignRoutePO> getAssignRouteForBucketClose(int
	 * branchId,int zoneId,String reqType,Time shiftTime) { List
	 * <EFmFmAssignRoutePO> assignVehicleDetail = new
	 * ArrayList<EFmFmAssignRoutePO>(); Query query=entityManager.createQuery(
	 * "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm JOIN b.eFmFmClientBranchPO c  where b.vehicleStatus='A' AND b.tripStatus='allocated' AND c.branchId ='"
	 * +branchId+"' AND vm.zoneId='"+zoneId+"' AND b.tripType='"+reqType+
	 * "' AND b.shiftTime='"+shiftTime+"'");
	 * assignVehicleDetail=query.getResultList(); return assignVehicleDetail; }
	 */

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveRouteTripDetails(EFmFmEmployeeTripDetailPO eFmFmEmployeeTripDetailPO) {
		entityManager.merge(eFmFmEmployeeTripDetailPO);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public EFmFmVehicleMasterPO getVehicleDetail(int vehicleId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO as b  where b.vehicleId='" + vehicleId + "'");
		vehicleDetail = query.getResultList();
		return vehicleDetail.get(0);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getParticularTripAllEmployees(int assignRouteId) {
		List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmEmployeeRequestMaster r JOIN b.efmFmAssignRoute d where d.assignRouteId='"
						+ assignRouteId + "' ORDER BY r.pickUpTime ASC");
		tripemployees = query.getResultList();
		return tripemployees;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getDropTripAllSortedEmployees(int assignRouteId) {
		List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.efmFmUserMaster u JOIN b.efmFmAssignRoute d where d.assignRouteId='"
						+ assignRouteId + "' ORDER BY t.dropSequence ASC");
		tripemployees = query.getResultList();
		return tripemployees;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteParticularRequest(int empTripId) {
		Query query = entityManager
				.createQuery("DELETE EFmFmEmployeeTripDetailPO where empTripId = '" + empTripId + "' ");
		query.executeUpdate();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getRequestStatusFromBranchIdAndRequestId(int branchId, int requestId) {
		List<EFmFmEmployeeTripDetailPO> travelRequestDetails = new ArrayList<EFmFmEmployeeTripDetailPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmEmployeeRequestMaster r JOIN t.efmFmUserMaster d JOIN d.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND t.requestId='" + requestId + "'");
		travelRequestDetails = query.getResultList();
		return travelRequestDetails;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTravelRequestPO> getAllParticularRouteRequest(int branchId, int zoneId, Time shiftTime) {
		List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r JOIN r.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c JOIN u.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm where c.branchId='"
						+ branchId + "' AND  vm.zoneId=" + zoneId
						+ " AND b.isActive='Y' AND b.approveStatus='Y' AND b.readFlg='Y' AND b.shiftTime='" + shiftTime
						+ "' AND (b.requestStatus!='C' OR b.requestStatus!='R')  ");
		employeeTravelRequestPO = query.getResultList();
		return employeeTravelRequestPO;
	}

	/*
	 * @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 * public List<EFmFmEmployeeTravelRequestPO>
	 * getAllAreaFromParticularZone(int branchId,int zoneId) { List
	 * <EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new
	 * ArrayList<EFmFmEmployeeTravelRequestPO>(); Query
	 * query=entityManager.createQuery(
	 * "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r JOIN r.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c JOIN u.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm where c.branchId='"
	 * +branchId+"' AND  vm.zoneId="+zoneId+
	 * " AND b.isActive='Y' AND b.approveStatus='Y' AND b.readFlg='Y'");
	 * employeeTravelRequestPO=query.getResultList(); return
	 * employeeTravelRequestPO; }
	 */

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEscortCheckInPO> getAllCheckedInEscort(int branchId) {
		List<EFmFmEscortCheckInPO> escortMasterPO = new ArrayList<EFmFmEscortCheckInPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmEscortCheckInPO b JOIN b.eFmFmEscortMaster e JOIN e.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.status='Y' AND b.escortCheckOutTime is null AND c.branchId='"
						+ branchId + "'");
		escortMasterPO = query.getResultList();
		return escortMasterPO;
	}

	/*
	 * @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 * public List<EFmFmVehicleCheckInPO> getCheckedInVehicleAndDriver(int
	 * driverId,int deviceId,int vehicleId,int branchId) { List
	 * <EFmFmVehicleCheckInPO> driverMasterPO = new
	 * ArrayList<EFmFmVehicleCheckInPO>(); Query
	 * query=entityManager.createQuery(
	 * "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster v JOIN v.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c JOIN b.efmFmDriverMaster dr JOIN b.eFmFmDeviceMaster dm where v.vehicleId='"
	 * +vehicleId+"' AND dm.deviceId='"+deviceId+"' AND c.branchId='"+branchId+
	 * "'  AND c.driverId!='"+driverId+
	 * "'  AND b.checkOutTime is not null order by b.checkOutTime asc ");
	 * driverMasterPO=query.getResultList(); return driverMasterPO; }
	 */

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleAndDriver(int driverId, int vehicleId, int branchId) {
		List<EFmFmVehicleCheckInPO> driverMasterPO = new ArrayList<EFmFmVehicleCheckInPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster v JOIN v.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c JOIN b.efmFmDriverMaster dr where  dr.driverId!='"
						+ driverId + "' AND v.vehicleId='" + vehicleId + "'  AND c.branchId='" + branchId
						+ "' AND b.checkOutTime is not null order by b.checkOutTime desc ");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}

	// Sending msg once device stop
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmActualRoutTravelledPO> getLastUpdatedValueFromDevice(int assignRouteId, int branchId) {
		List<EFmFmActualRoutTravelledPO> routedetails = new ArrayList<EFmFmActualRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmActualRoutTravelledPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a  where c.branchId='"
						+ branchId + "' AND a.assignRouteId='" + assignRouteId + "' ");
		routedetails = query.getResultList();
		return routedetails;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmLiveRoutTravelledPO> getRouteLastEtaAndDistanceFromAssignRouteId(int assignRouteId) {
		List<EFmFmLiveRoutTravelledPO> routedetails = new ArrayList<EFmFmLiveRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmLiveRoutTravelledPO b JOIN b.efmFmAssignRoute a  where a.assignRouteId='" + assignRouteId + "' ORDER BY  b.liveRouteTravelId DESC")
				.setMaxResults(1);
		routedetails = query.getResultList();
		return routedetails;
	}

	// Get all the user Details On roleBased
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientUserRolePO> getAllUserDetailsByRoleAndbranchId(int branchId, String role) {
		List<EFmFmClientUserRolePO> roleDetail = new ArrayList<EFmFmClientUserRolePO>();
		Query query = entityManager.createQuery(
				"SELECT r FROM EFmFmClientUserRolePO as r JOIN r.efmFmRoleMaster ro JOIN r.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND ro.role='" + role + "'");
		roleDetail = query.getResultList();
		return roleDetail;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getAllVehicleDetailsFromBranchId(int branchId) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "' AND b.vehicleNumber NOT LIKE '%DUMMY%' AND (b.status='A' OR  b.status='allocated') ");
		vehicleMasterPO = query.getResultList();
		return vehicleMasterPO;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getParticularVehicleDetailsFromVehicleId(int vehicleId) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmVehicleMasterPO b where b.vehicleId='"
						+ vehicleId + "'  ");
		vehicleMasterPO = query.getResultList();
		return vehicleMasterPO;
	}
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
	
	 // Trip on driver device after closing the bucket only
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAssignRoutePO> closeVehicleCapacity(int checkInId, int branchId) {
        List<EFmFmAssignRoutePO> tripDtails = new ArrayList<EFmFmAssignRoutePO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn ch where ch.checkInId='"
                        + checkInId + "' AND b.tripStatus!='completed' AND b.bucketStatus='Y' AND c.branchId='"
                        + branchId + "' ORDER BY b.allocationMsgDeliveryDate ASC ");
        tripDtails = query.getResultList();
        return tripDtails;
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmCheckInVehicleTrackingPO> vehicleTrackingAfterCheckIn(int branhId,int checkInId) {
        List<EFmCheckInVehicleTrackingPO> trackVehicles = new ArrayList<EFmCheckInVehicleTrackingPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmCheckInVehicleTrackingPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn ch where  c.branchId='"
                        + branhId + "' AND ch.checkInId='"+checkInId+"'");
        trackVehicles = query.getResultList();
        return trackVehicles;
    }
    // Get Employee details from UserId
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromUserIdWithOutStatus(int userId, int branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
                        + branchId + "' AND b.userId = '" + userId + "' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }
    
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> closeParticularTrips(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where  c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "' AND b.assignRouteId='"
						+ assignRoutePO.getAssignRouteId() + "' ");
		allTrips = query.getResultList();
		return allTrips;
	}
	
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getParticularTripNonDropEmployeesDetails(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripEmployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where b.boardedFlg='N'  AND d.assignRouteId='"
                        + assignRouteId + "' ORDER BY t.pickUpTime ASC");
        tripEmployees = query.getResultList();
        return tripEmployees;
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getDropTripAllSortedNonDropEmployees(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.efmFmUserMaster u JOIN b.efmFmAssignRoute d where b.boardedFlg='N' and d.assignRouteId='"
                        + assignRouteId + "' ORDER BY t.dropSequence ASC");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    
    
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getreachedEmployeesList(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute d where b.reachedFlg='Y' and d.assignRouteId='"
                        + assignRouteId + "'");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    
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
	
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getTripAllocatedRoute(int checkInId,int branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn cn where  c.branchId='"
						+ branchId + "' AND cn.checkInId='"
						+ checkInId + "'  AND b.tripStatus !='completed'");
		allTrips = query.getResultList();
		return allTrips;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmLiveRoutTravelledPO> getRouteLastEtaAndDistanceFromAssignRouteIdForCabLastLocation(int branchId,int assignRouteId) {
		List<EFmFmLiveRoutTravelledPO> routedetails = new ArrayList<EFmFmLiveRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmLiveRoutTravelledPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a  where c.branchId='"
						+ branchId + "' AND a.assignRouteId='"
						+ assignRouteId+ "' ORDER BY  b.liveRouteTravelId ASC").setMaxResults(1);
		routedetails = query.getResultList();
		return routedetails;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public EFmFmDriverMasterPO getParticularDriverDetail(int driverId) {
		List<EFmFmDriverMasterPO> driverDetail = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO as b  where b.driverId='" + driverId + "'");
		driverDetail = query.getResultList();
		return driverDetail.get(0);
	}	
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDeviceMasterPO> getDeviceDetailsFromDeviceId(int deviceId,
			int branchId) {
		List<EFmFmDeviceMasterPO> deviceDetail = new ArrayList<EFmFmDeviceMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO c where b.deviceId='"
						+ deviceId + "' AND c.branchId='" + branchId + "' ");
		deviceDetail = query.getResultList();
		return deviceDetail;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEscortCheckInPO> getParticulaEscortDetailFromEscortId(
			int branchId, int escortCheckInId) {
		List<EFmFmEscortCheckInPO> escortMasterPO = new ArrayList<EFmFmEscortCheckInPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmEscortCheckInPO b JOIN b.eFmFmEscortMaster e JOIN e.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.escortCheckOutTime is null AND c.branchId='"
						+ branchId
						+ "'  AND b.escortCheckInId='"
						+ escortCheckInId + "' ");
		escortMasterPO = query.getResultList();
		return escortMasterPO;
	}
	
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getParticularRequestDetailOnTripComplete(int requestId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b  where b.requestId='"
                        + requestId + "' ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public EFmFmVehicleMasterPO getParticularVehicleDetail(int vehicleId) {
		List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO as b  where b.vehicleId='"
						+ vehicleId + "'");
		vehicleDetail = query.getResultList();
		return vehicleDetail.get(0);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmLiveRoutTravelledPO> getCompletedRouteDataFromAssignRouteId(int branchId,int assignRouteId) {
		List<EFmFmLiveRoutTravelledPO> routedetails = new ArrayList<EFmFmLiveRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmLiveRoutTravelledPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a  where c.branchId='"
						+ branchId + "' AND a.assignRouteId='"
						+ assignRouteId+ "' ");
		routedetails = query.getResultList();
		return routedetails;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteParticularActualTravelled(int liveRouteTravelId) {
		Query query = entityManager
				.createQuery("DELETE EFmFmLiveRoutTravelledPO where liveRouteTravelId = '" + liveRouteTravelId + "' ");
		query.executeUpdate();
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getCompletedTripDetail(int checkInId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.efmFmVehicleCheckIn cn where cn.checkInId='"+ checkInId + "'  AND b.distanceUpdationFlg= 'Y' AND b.tripStatus ='completed'").setMaxResults(1);
		allTrips = query.getResultList();
		return allTrips;
	}
	
	
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmActualRoutTravelledPO> getFirstEntryFromActualRouteByAssignRouteId(int assignRouteId) {
		List<EFmFmActualRoutTravelledPO> routedetails = new ArrayList<EFmFmActualRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmActualRoutTravelledPO b JOIN b.efmFmAssignRoute a  where a.assignRouteId='"
						+ assignRouteId + "' ORDER BY travelId ASC").setMaxResults(1);
		routedetails = query.getResultList();
		return routedetails;
	}
	
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmActualRoutTravelledPO> getLastEntryFromActualRouteByAssignRouteId(int assignRouteId) {
		List<EFmFmActualRoutTravelledPO> routedetails = new ArrayList<EFmFmActualRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmActualRoutTravelledPO b JOIN b.efmFmAssignRoute a  where a.assignRouteId='"
						+ assignRouteId + "' ORDER BY travelId DESC").setMaxResults(1);
		routedetails = query.getResultList();
		return routedetails;
	}
	    
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTripDetailPO> getFirstPickEmployeeDetail(int assignRouteId) {
	        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
	                        + assignRouteId + "' ORDER BY t.pickUpTime ASC");
	        tripemployees = query.getResultList();
	        return tripemployees;
	    }
	    
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTripDetailPO> getFirstDropEmployeeDetail(int assignRouteId) {
	        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
	                        + assignRouteId + "' AND b.boardedFlg='D' ORDER BY t.dropSequence ASC");
	        tripemployees = query.getResultList();
	        return tripemployees;
	    }
	    
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehiclesForSpecificCapacity(int capacity,long totalTravelTime) {
	        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d where d.capacity='"
	                        + capacity + "' and d.vehicleNumber not like '%DUMMY%' and b.status='Y' and b.totalTravelTime='"+totalTravelTime+"' and b.checkOutTime is  null order by d.monthlyPendingFixedDistance DESC ").setMaxResults(1);
	        vehicleCheckIn = query.getResultList();
	        return vehicleCheckIn;

	    }
}
