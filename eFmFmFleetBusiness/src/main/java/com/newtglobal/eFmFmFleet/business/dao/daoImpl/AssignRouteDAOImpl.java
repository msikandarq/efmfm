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
import javax.persistence.TemporalType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newtglobal.eFmFmFleet.business.dao.IAssignRouteDAO;
import com.newtglobal.eFmFmFleet.model.EFmCheckInVehicleTrackingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmActualRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLiveRoutTravelledPO;

@Repository("IAssignRouteDAO")
public class AssignRouteDAOImpl implements IAssignRouteDAO {

	private static Log log = LogFactory.getLog(AssignRouteDAOImpl.class);

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager _entityManager) {
		this.entityManager = _entityManager;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmAssignRoutePO eFmFmAssignRoutePO) {
		entityManager.merge(eFmFmAssignRoutePO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmAssignRoutePO eFmFmAssignRoutePO) {
		entityManager.merge(eFmFmAssignRoutePO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmActualRoutTravelledPO actualRoutTravelledPO) {
		entityManager.persist(actualRoutTravelledPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmActualRoutTravelledPO actualRoutTravelledPO) {
		entityManager.merge(actualRoutTravelledPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmLiveRoutTravelledPO eFmFmLiveRoutTravelledPO) {
		entityManager.persist(eFmFmLiveRoutTravelledPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmLiveRoutTravelledPO eFmFmLiveRoutTravelledPO) {
		entityManager.merge(eFmFmLiveRoutTravelledPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteParticularAssignRoute(int assignRouteId) {
		Query query = entityManager
				.createQuery("DELETE EFmFmAssignRoutePO where assignRouteId = '" + assignRouteId + "' ");
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteParticularActualTravelled(int liveRouteTravelId) {
		Query query = entityManager
				.createQuery("DELETE EFmFmLiveRoutTravelledPO where liveRouteTravelId = '" + liveRouteTravelId + "' ");
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTodaysTrips(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		try {
			Query query = entityManager.createQuery(
					"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c  JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId='"
							+ assignRoutePO.geteFmFmClientBranchPO().getBranchId()
							+ "'AND b.tripStatus !='completed' ORDER BY z.zoneName ASC");
			allTrips = query.getResultList();
		} catch (Exception e) {
			log.info("Zone error" + e);
			return allTrips;

		}
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllStartedTripsFromAssignRoute(String branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		try {
			Query query = entityManager.createQuery(
					"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripStatus='Started' AND c.branchId in ("+ branchId + ") ORDER BY b.shiftTime ASC");
			allTrips = query.getResultList();
		} catch (Exception e) {
			log.info("Zone error" + e);
			return allTrips;

		}
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTodaysTripsWithOutDummyVehicle(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		try {
			Query query = entityManager.createQuery(
					"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c  JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z JOIN b.efmFmVehicleCheckIn cn JOIN cn.efmFmVehicleMaster vm where c.branchId in ("+ assignRoutePO.getCombinedFacility() + ") AND b.tripStatus !='completed' AND b.plannedReadFlg='N' AND b.scheduleReadFlg='N' ORDER BY z.zoneName ASC");
			allTrips = query.getResultList();
		} catch (Exception e) {
			log.info("Zone error" + e);
			return allTrips;

		}
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> closeParticularTrips(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b where b.assignRouteId='"
						+ assignRoutePO.getAssignRouteId() + "' ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getParticularRouteDetailFromAssignRouteId(int assignRouteId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b  where b.assignRouteId='" + assignRouteId + "' ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateAssignVendorToRoute(int assignRouteId, String assignedVendorName) {
		Query query = entityManager.createQuery("UPDATE EFmFmAssignRoutePO set assignedVendorName='"
				+ assignedVendorName + "' where 	assignRouteId='" + assignRouteId + "' ");
		query.executeUpdate();

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getLastCompletedTripByDriver(int checkInId, int branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn cn where  c.branchId='"
						+ branchId + "' AND cn.checkInId='" + checkInId + "' AND b.bucketStatus='Y' ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getLastTripFromCheckInIdAndTripType(int checkInId, String tripType) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.efmFmVehicleCheckIn cn where cn.checkInId='"
						+ checkInId + "' AND b.tripType='" + tripType + "' AND b.bucketStatus='Y'  ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getCheckInTripAllocationCheckAfterTripCompletion(int checkInId, int branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn cn where  c.branchId='"
						+ branchId + "' AND cn.checkInId='" + checkInId + "' AND b.tripStatus !='completed'");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getDuplicateTripAllocationCheck(int checkInId, String branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn cn where  c.branchId in ("+ branchId + ") AND cn.checkInId='" + checkInId
						+ "' AND b.bucketStatus='Y' AND b.tripStatus !='completed'");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getTripAllocatedRoute(int checkInId, int branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn cn where  c.branchId='"
						+ branchId + "' AND cn.checkInId='" + checkInId + "'  AND b.tripStatus !='completed'");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmCheckInVehicleTrackingPO> vehicleTrackingAfterCheckIn(String branchId, int checkInId) {
		List<EFmCheckInVehicleTrackingPO> trackVehicles = new ArrayList<EFmCheckInVehicleTrackingPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmCheckInVehicleTrackingPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn ch where  c.branchId in ("+ branchId + ") AND ch.checkInId='" + checkInId + "'");
		trackVehicles = query.getResultList();
		return trackVehicles;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllLiveTrips(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripStatus='Started' AND b.tripStartTime!=NULL "
				+ " AND c.branchId in ("+ assignRoutePO.getCombinedFacility()+ ") ORDER BY tripAssignDate ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllLiveTripsByTripypeDateAndShiftTime(String branchId, String tripType,
			Time ShiftTime) {
		log.info("branchId"+branchId);
		log.info("tripType"+tripType);
		log.info("ShiftTime"+ShiftTime);

		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripStatus='Started' AND b.tripStartTime!=NULL AND c.branchId in ("+ branchId + ") AND b.shiftTime='" + ShiftTime + "' AND b.tripType='" + tripType
						+ "' ORDER BY tripAssignDate ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllLiveTripsByShiftTime(int branchId, String tripType, Time ShiftTime,
			String createdDate) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where  b.tripStatus !='completed' AND c.branchId='"
						+ branchId + "' AND DATE(b.createdDate)='" + createdDate + "' AND b.shiftTime='" + ShiftTime
						+ "' AND b.tripType='" + tripType + "' ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllRoutesOfParticularZone(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.tripStatus !='completed'  AND c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "' AND z.zoneId='"
						+ assignRoutePO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId()
						+ "' ORDER BY tripAssignDate ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllRoutesOfParticularZoneWithOutDummyVehicles(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z JOIN b.efmFmVehicleCheckIn cn JOIN cn.efmFmVehicleMaster vm where b.tripStatus !='completed'  AND c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "' AND z.zoneId='"
						+ assignRoutePO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId()
						+ "' AND b.plannedReadFlg='N' AND b.scheduleReadFlg='N' ORDER BY tripAssignDate ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllRoutesOfParticularZoneWithOrOutDummyVehicles(
			EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z JOIN b.efmFmVehicleCheckIn cn JOIN cn.efmFmVehicleMaster vm where b.tripStatus !='completed'  AND c.branchId in ("+ assignRoutePO.getCombinedFacility() + ") AND z.zoneId='"
						+ assignRoutePO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId()
						+ "'   ORDER BY createdDate ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	// @Override
	// @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	// public List<EFmFmAssignRoutePO>
	// getAllRoutesOfParticularZoneSelectedShiftWise(EFmFmAssignRoutePO
	// assignRoutePO) {
	// List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
	// Query query = entityManager.createQuery(
	// "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN
	// b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.tripStatus
	// !='completed' AND c.branchId='"
	// + assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "' AND AND
	// b.shiftTime='" + assignRoutePO.getShiftTime()+"' AND
	// b.tripStatus='"+assignRoutePO.getTripStatus()+"' AND z.zoneId='"
	// +
	// assignRoutePO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId()
	// + "' ORDER BY tripAssignDate ASC");
	// allTrips = query.getResultList();
	// return allTrips;
	// }

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllRoutesInsideZone(int branchId, int zoneId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.tripStatus !='completed'  AND c.branchId='"
						+ branchId + "' AND z.zoneId='" + zoneId + "' ORDER BY z.zoneName ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllRoutesCountInsideZone(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.tripType='"
						+ assignRoutePO.getTripType() + "' AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "' AND b.tripStatus !='completed' AND c.branchId in ("+ assignRoutePO.getCombinedFacility() + ") AND z.zoneId='" + assignRoutePO.getZoneId() + "' ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllOnlyAssignedTrips(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a  where b.tripStatus !='completed' AND b.vehicleStatus!='F'  AND b.tripType='"
						+ assignRoutePO.getTripType() + "' AND c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "' AND a.routeAreaId='"
						+ assignRoutePO.geteFmFmRouteAreaMapping().getRouteAreaId() + "' ORDER BY tripAssignDate ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmActualRoutTravelledPO> getEtaAndDistanceFromAssignRouteId(
			EFmFmActualRoutTravelledPO actualRouteTravelledPO) {
		List<EFmFmActualRoutTravelledPO> routedetails = new ArrayList<EFmFmActualRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmActualRoutTravelledPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a  where c.branchId='"
						+ actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId() + "' AND a.assignRouteId='"
						+ actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId()
						+ "' ORDER BY travelledTime ASC");
		routedetails = query.getResultList();
		return routedetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmActualRoutTravelledPO> getRouteLattiLongiFromAssignRouteId(int assignRouteId) {
		List<EFmFmActualRoutTravelledPO> routedetails = new ArrayList<EFmFmActualRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmActualRoutTravelledPO b JOIN b.efmFmAssignRoute a  where a.assignRouteId='"
						+ assignRouteId + "' ORDER BY travelledTime ASC");
		routedetails = query.getResultList();
		return routedetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmLiveRoutTravelledPO> getRouteDetailsFromAssignRouteId(int branchId, int assignRouteId) {
		List<EFmFmLiveRoutTravelledPO> routedetails = new ArrayList<EFmFmLiveRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmLiveRoutTravelledPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a  where c.branchId='"
						+ branchId + "' AND a.assignRouteId='" + assignRouteId + "' ORDER BY liveTravelledTime ASC");
		routedetails = query.getResultList();
		return routedetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmLiveRoutTravelledPO> getRouteLastEtaAndDistanceFromAssignRouteId(int branchId, int assignRouteId) {
		List<EFmFmLiveRoutTravelledPO> routedetails = new ArrayList<EFmFmLiveRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmLiveRoutTravelledPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a  where c.branchId='"
						+ branchId + "' AND a.assignRouteId='" + assignRouteId + "' ORDER BY  b.liveRouteTravelId DESC")
				.setMaxResults(1);
		routedetails = query.getResultList();
		return routedetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmLiveRoutTravelledPO> getCompletedRouteDataFromAssignRouteId(int branchId, int assignRouteId) {
		List<EFmFmLiveRoutTravelledPO> routedetails = new ArrayList<EFmFmLiveRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmLiveRoutTravelledPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a  where c.branchId='"
						+ branchId + "' AND a.assignRouteId='" + assignRouteId + "' ");
		routedetails = query.getResultList();
		return routedetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public EFmFmLiveRoutTravelledPO getRouteLastEntryFromAssignRouteId(int branchId, int assignRouteId) {
		EFmFmLiveRoutTravelledPO routedetails = new EFmFmLiveRoutTravelledPO();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmLiveRoutTravelledPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a  where c.branchId='"
						+ branchId + "' AND a.assignRouteId='" + assignRouteId + "' ORDER BY  b.liveRouteTravelId ASC")
				.setMaxResults(1);
		routedetails = (EFmFmLiveRoutTravelledPO) query.getSingleResult();
		return routedetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmLiveRoutTravelledPO> getRouteLastEtaAndDistanceFromAssignRouteIdForCabLastLocation(int branchId,
			int assignRouteId) {
		List<EFmFmLiveRoutTravelledPO> routedetails = new ArrayList<EFmFmLiveRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmLiveRoutTravelledPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a  where c.branchId='"
						+ branchId + "' AND a.assignRouteId='" + assignRouteId + "' ORDER BY  b.liveRouteTravelId ASC")
				.setMaxResults(1);
		routedetails = query.getResultList();
		return routedetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getTripCountByDate(Date fromDate, Date toDate, String tripType, int branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.format(fromDate);
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripType='"
						+ tripType + "' AND DATE(b.tripAssignDate)>=TRIM('" + formatter.format(fromDate)
						+ "') AND DATE(b.tripAssignDate)<=TRIM('" + formatter.format(toDate)
						+ "') AND b.tripStatus ='completed' AND c.branchId='" + branchId
						+ "' group by b.tripAssignDate");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTodaysCompletedTrips(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripType='"
						+ assignRoutePO.getTripType() + "'  AND b.tripStatus ='completed' AND c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "'");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllRoutesBasedOnTripTypeAndShiftTime(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.tripType='"
						+ assignRoutePO.getTripType() + "' AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "' AND b.tripStatus !='completed' AND c.branchId in ("+ assignRoutePO.getCombinedFacility() + ") ORDER BY z.zoneName ASC");
		allTrips = query.getResultList();
		return allTrips;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllDeliveredRoutesOnTripTypeAndShiftTime(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.tripType='"
						+ assignRoutePO.getTripType() + "' AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "' AND b.tripStatus !='completed' AND b.tripConfirmationFromDriver ='Delivered' AND c.branchId in ("+ assignRoutePO.getCombinedFacility() + ") ORDER BY z.zoneName ASC");
		allTrips = query.getResultList();
		return allTrips;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllNotDeliveredRoutesOnTripTypeAndShiftTime(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.tripType='"
						+ assignRoutePO.getTripType() + "' AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "' AND b.tripStatus !='completed' AND b.tripConfirmationFromDriver ='Not Delivered' AND c.branchId in ("+ assignRoutePO.getCombinedFacility() + ") ORDER BY z.zoneName ASC");
		allTrips = query.getResultList();
		return allTrips;
	}
	
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllRoutesBasedOnTripTypeAndShiftTimeAdvanceSearch(
			EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.tripType='"
						+ assignRoutePO.getTripType() + "' AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "' AND b.tripStatus !='completed' AND c.branchId in ("+ assignRoutePO.getCombinedFacility() + ") AND z.zoneId='"
						+ assignRoutePO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId()
						+ "' ORDER BY z.zoneName ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllRoutesBasedOnTripTypeShiftTimeAndDate(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.tripType='"
						+ assignRoutePO.getTripType() + "' AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime() + "' AND b.tripStatus ='completed'");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllBucketClosedRoutesOnAdvanceSearch(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.bucketStatus='Y' AND b.tripType='"
						+ assignRoutePO.getTripType() + "' AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "'AND b.tripStatus!='completed' AND c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "' AND z.zoneId='"
						+ assignRoutePO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId()
						+ "' ORDER BY z.zoneName ASC ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllStartedRoutesOnAdvanceSearch(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.tripStatus='Started' AND b.tripStartTime!=NULL AND b.tripType='"
						+ assignRoutePO.getTripType() + "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "' AND c.branchId='" + assignRoutePO.geteFmFmClientBranchPO().getBranchId()
						+ "' AND z.zoneId='" + assignRoutePO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId()
						+ "'ORDER BY z.zoneName ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllOpenBucketRoutesOnAdvanceSearch(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.bucketStatus='N' AND b.tripType='"
						+ assignRoutePO.getTripType() + "' AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "'AND b.tripStatus!='completed' AND c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "' AND z.zoneId='"
						+ assignRoutePO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId()
						+ "' ORDER BY z.zoneName ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllBucketClosedAndNotStartedRoutesAdvanceSearch(
			EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.bucketStatus='Y' AND b.tripType='"
						+ assignRoutePO.getTripType() + "'AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "'AND b.tripStatus='allocated' AND c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "' AND z.zoneId='"
						+ assignRoutePO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId()
						+ "' ORDER BY z.zoneName ASC ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllStartedRoutes(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.tripStatus='Started' AND b.tripStartTime!=NULL AND b.tripType='"
						+ assignRoutePO.getTripType() + "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "' AND c.branchId in ("+ assignRoutePO.getCombinedFacility() + ") ORDER BY z.zoneName ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllBucketClosedRoutes(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.bucketStatus='Y' AND b.tripType='"
						+ assignRoutePO.getTripType() + "' AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "'AND b.tripStatus!='completed' AND c.branchId in ("+ assignRoutePO.getCombinedFacility() + ") ORDER BY z.zoneName ASC ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllBucketClosedAndNotStartedRoutes(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.bucketStatus='Y' AND b.tripType='"
						+ assignRoutePO.getTripType() + "'AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "'AND b.tripStatus='allocated' AND c.branchId in ("+ assignRoutePO.getCombinedFacility() + ") ORDER BY z.zoneName ASC ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllBackToRoutesForParticularShift(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.tripType='"
						+ assignRoutePO.getTripType() + "'AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "'AND b.tripStatus!='completed' AND b.isBackTwoBack='Y' AND c.branchId in ("+ assignRoutePO.getCombinedFacility() + ") ORDER BY z.zoneName ASC ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllOpenBucketRoutes(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.bucketStatus='N' AND b.tripType='"
						+ assignRoutePO.getTripType() + "' AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
						+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime()
						+ "'AND b.tripStatus!='completed' AND c.branchId in ("+ assignRoutePO.getCombinedFacility() + ") ORDER BY z.zoneName ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllClosedRoutes(String tripType, Time shiftTime, int branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.bucketStatus='Y' AND b.tripType='"
						+ tripType + "' AND b.shiftTime='" + shiftTime
						+ "'AND b.tripStatus!='completed' AND c.branchId='" + branchId + "' ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripDetails(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripStatus ='completed' AND c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "'");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripByDate(Date fromDate, Date toDate, String branchId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  "
				+ " AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId+")  ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// get the edited travelled distance by the branch id
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllEdiTripByBranchId(String branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripStatus='completed' AND c.branchId in ("+ branchId + ") AND b.editDistanceApproval ='NO' AND b.remarksForEditingTravelledDistance !='NO' ORDER BY tripAssignDate ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	// Get route wise travel time report
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripForTravelTimeReports(Date fromDate, Date toDate, String tripType,
			String branchId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  "
				+ " AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+ branchId + ")"
						+ " and t.tripType='" + tripType + "'  ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripByDateByDeriverId(Date fromDate, Date toDate, int branchId,
			int driverId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmDriverMaster dm WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND dm.driverId='" + driverId + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByDate(Date fromDate, Date toDate, String branchId) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1 "
				+ "  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +") ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getAllMessageEmployeesMessageDetailsByDate(Date fromDate, Date toDate,
			String branchId) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE date(a.tripAssignDate) >= ?1  AND  date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +") ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsTravelledAndPlannedDistanceByDateAndVehicle(Date fromDate, Date toDate,
			int branchId, int vehicleId) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster d JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' "
				+ "AND DATE(t.tripAssignDate) >='" + formatter.format(fromDate) + "' AND DATE(t.tripAssignDate)<='"
				+ formatter.format(toDate) + "' AND c.branchId='" + branchId + "' " + "AND d.vehicleId='" + vehicleId
				+ "' " + "group by d.vehicleId";
		Query q = entityManager.createQuery(query);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsTravelledAndPlannedDistanceByDate(Date fromDate, Date toDate,
			String branchId, int vendorId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2 "
				+ "  AND c.branchId in ("+branchId +")  AND f.vendorId=" + vendorId + "  ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsTravelledAndPlannedDistanceByAllVendor(Date fromDate, Date toDate,
			String branchId) {

		String query = "SELECT t  FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c  JOIN t.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f  WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2 "
				+ " AND c.branchId in ("+branchId +")   ORDER BY tripAssignDate,f.vendorName ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsVehicleDetailsByVehicleNumber(Date fromDate, Date toDate, String branchId,
			String vehicleNumber) {
		String query = "SELECT t  FROM EFmFmAssignRoutePO t JOIN t.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster d JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND d.vehicleNumber='" + vehicleNumber + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// Getting KM shift wice report
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsVehicleKMDetailsByShiftTime(Date fromDate, Date toDate, String branchId,
			Time shiftTime) {
		String query = "SELECT t  FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' "
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND t.shiftTime='" + shiftTime + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String getVendorNameTravelledAndPlannedDistanceByAllVendor(Date fromDate, Date toDate, int branchId) {
		String query = "SELECT t.distinct(vendorId,vendorName) FROM EFmFmAssignRoutePO t JOIN t.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "'  ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList().toString();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllActiveTrips(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripStatus !='completed'  AND c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "'");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllRoutesOfParticularBranch(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where  c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "'");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmActualRoutTravelledPO> getCabLocationFromAssignRouteId(
			EFmFmActualRoutTravelledPO actualRouteTravelledPO) {
		List<EFmFmActualRoutTravelledPO> routedetails = new ArrayList<EFmFmActualRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmActualRoutTravelledPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a  where c.branchId='"
						+ actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId() + "' AND a.assignRouteId='"
						+ actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId()
						+ "' AND b.currentCabLocation is not null");
		routedetails = query.getResultList();
		return routedetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmActualRoutTravelledPO> getLastEtaFromAssignRouteId(
			EFmFmActualRoutTravelledPO actualRouteTravelledPO) {
		List<EFmFmActualRoutTravelledPO> routedetails = new ArrayList<EFmFmActualRoutTravelledPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmActualRoutTravelledPO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a  where c.branchId='"
						+ actualRouteTravelledPO.geteFmFmClientBranchPO().getBranchId() + "' AND a.assignRouteId='"
						+ actualRouteTravelledPO.getEfmFmAssignRoute().getAssignRouteId()
						+ "' AND b.currentEta is not null");
		routedetails = query.getResultList();
		return routedetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getPickupVehiclesOnRoadCounter(int branchId) {
		Query query = entityManager.createQuery(
				"SELECT count(b) FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripStatus='Started' AND b.tripStartTime!=NULL AND b.tripType='PICKUP' AND c.branchId='"
						+ branchId + "'");
		long pickupVehicleOnRoad = (long) query.getSingleResult();
		return pickupVehicleOnRoad;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getDropVehiclesOnRoadCounter(int branchId) {
		Query query = entityManager.createQuery(
				"SELECT count(b) FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripStatus='Started' AND b.tripStartTime!=NULL AND b.tripType='DROP' AND c.branchId='"
						+ branchId + "'");
		long pickupVehicleOnRoad = (long) query.getSingleResult();
		return pickupVehicleOnRoad;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllVehiclesOnRoadCounter(String branchId) {
		Query query = entityManager.createQuery(
				"SELECT count(b) FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripStatus='Started' AND b.tripStartTime!=NULL AND c.branchId in ("+ branchId + ")");
		long pickupVehicleOnRoad = (long) query.getSingleResult();
		return pickupVehicleOnRoad;
	}

	// total trip distinct date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Date> getAllTripsDistinctDates(Date fromDate, Date toDate, String branchId, String tripType) {
		String query = "SELECT Distinct DATE(t.tripAssignDate) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Date> tripDates = (List<Date>) q.getResultList();
		return tripDates;
	}

	// total trip counts shift wise date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Time> getAllTripsByShift(Date fromDate, Date toDate, int branchId, String tripType, Time shiftTime) {
		String query = "SELECT Distinct Time(t.shiftTime) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' AND t.shiftTime='" + shiftTime
				+ "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Time> tripDates = (List<Time>) q.getResultList();
		return tripDates;

	}

	// total trip counts by shift wise date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Time> getAllTripsByAllShifts(Date fromDate, Date toDate, int branchId, String tripType) {
		String query = "SELECT Distinct Time(t.shiftTime) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "'  ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Time> tripDates = (List<Time>) q.getResultList();
		return tripDates;

	}

	// total trip counts by shift wise on the bais of vendorid
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Time> getAllTripsByAllShiftsForVendor(Date fromDate, Date toDate, int branchId, String tripType,
			int vendorId) {
		String query = "SELECT Distinct Time(t.shiftTime) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' AND vem.vendorId='" + vendorId
				+ "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Time> tripDates = (List<Time>) q.getResultList();
		return tripDates;
	}

	// total trip counts shift wise plus vendorId date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Date> getAllTripsByShiftByVendor(Date fromDate, Date toDate, int branchId, String tripType,
			Time shiftTime, int vendorId) {
		String query = "SELECT Distinct DATE(t.tripAssignDate) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' AND t.shiftTime='" + shiftTime
				+ "' AND vem.vendorId='" + vendorId + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Date> tripDates = (List<Date>) q.getResultList();
		return tripDates;
	}

	// total trip counts by vendorId date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Date> getAllTripsByByVendorId(Date fromDate, Date toDate, int branchId, String tripType, int vendorId) {
		String query = "SELECT Distinct DATE(t.tripAssignDate) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' AND vem.vendorId='" + vendorId
				+ "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Date> tripDates = (List<Date>) q.getResultList();
		return tripDates;
	}

	// total trip counts by vendorId date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getUsedFleetByByVendorId(Date fromDate, Date toDate, int branchId, String tripType, int vendorId) {
		String query = "SELECT count(t) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' AND vem.vendorId='" + vendorId
				+ "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long tripDates = (long) q.getSingleResult();
		return tripDates;
	}

	// total trip counts by vendorId date counts and shift wise
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getUsedFleetByByVendorIdByShift(Date fromDate, Date toDate, int branchId, String tripType, int vendorId,
			Time shiftTime) {
		String query = "SELECT count(t) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' AND vem.vendorId='" + vendorId + "' AND t.shiftTime='"
				+ shiftTime + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long tripDates = (long) q.getSingleResult();
		return tripDates;
	}

	// total trip count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllTripsCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {
		String query = "SELECT count(t) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long tripDates = (long) q.getSingleResult();
		return tripDates;
	}

	// total Delaytrips count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllDelayTripsCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {
		String query = "SELECT count(t) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long tripDates = (long) q.getSingleResult();
		return tripDates;
	}

	// total Delaytrips Beyond Login time count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllDelayTripsBeyondLoginTimeCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {
		String query = "SELECT count(t) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long tripDates = (long) q.getSingleResult();
		return tripDates;
	}

	// total Employees traveled count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllEmployeesCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c WHERE a.tripStatus ='completed' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripType='" + tripType + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total Employees traveled count by date By vendorId
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllEmployeesCountByDateByVendorId(Date fromDate, Date toDate, int branchId, String tripType,
			int vendorId) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE a.tripStatus ='completed'  AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripType='" + tripType + "' AND vem.vendorId='" + vendorId + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total No Show Employees count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getNoShowEmployeesCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c WHERE a.tripStatus ='completed' AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripType='" + tripType + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total No Show Employees count by date by vendor
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getNoShowEmployeesCountByDateByVendor(Date fromDate, Date toDate, int branchId, String tripType,
			int vendorId) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE a.tripStatus ='completed'  AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripType='" + tripType + "' AND vem.vendorId='" + vendorId + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total PickedUp Employees traveled count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getPickedUpEmployeesCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c WHERE a.tripStatus ='completed' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripType='" + tripType + "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total PickedUp Employees traveled count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getPickedUpOrDroppedEmployeesCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c WHERE  date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripStatus ='completed' AND a.tripType='" + tripType
				+ "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total PickedUp Employees traveled count by date by vendor
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getPickedUpEmployeesCountByDateByVendor(Date fromDate, Date toDate, int branchId, String tripType,
			int vendorId) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE  a.tripStatus ='completed'  AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripType='" + tripType + "' AND vem.vendorId='" + vendorId
				+ "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;

	}

	// total Delaytrips count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsByDate(Date fromDate, Date toDate, int branchId, String tripType) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllTripsCountByShift(Date fromDate, Date toDate, int branchId, String tripType, Time shiftTime) {
		String query = "SELECT count(t) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' AND t.shiftTime='" + shiftTime
				+ "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long tripDates = (long) q.getSingleResult();
		return tripDates;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsDetailsByShift(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' "
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2 "
				+ "  AND c.branchId in ("+branchId +")  "
						+ " AND t.tripType='" + tripType + "' AND t.shiftTime='" + shiftTime
				+ "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// Vendorwise trip details
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsDetailsByShiftByVendor(Date fromDate, Date toDate, int branchId,
			String tripType, Time shiftTime, int vendorId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' AND t.shiftTime='" + shiftTime
				+ "' AND vem.vendorId='" + vendorId + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// Vendorwise details for date wice.
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsDetailsByVendorWiseOnly(Date fromDate, Date toDate, int branchId,
			String tripType, int vendorId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' AND vem.vendorId='" + vendorId
				+ "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllEmployeesCountByShift(Date fromDate, Date toDate, int branchId, String tripType, Time shiftTime) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c WHERE a.tripStatus ='completed'  AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllEmployeesCountByShiftByVendorId(Date fromDate, Date toDate, int branchId, String tripType,
			Time shiftTime, int vendorId) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE a.tripStatus ='completed' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime
				+ "' AND vem.vendorId='" + vendorId + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getNoShowEmployeesCountByShift(Date fromDate, Date toDate, int branchId, String tripType,
			Time shiftTime) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c WHERE a.tripStatus ='completed'  AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// No show EmployeeCount By Shift wise and Vendor wise
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getNoShowEmployeesCountByShiftByVendor(Date fromDate, Date toDate, int branchId, String tripType,
			Time shiftTime, int vendorId) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE a.tripStatus ='completed' AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime
				+ "' AND vem.vendorId='" + vendorId + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getPickedUpEmployeesCountByShift(Date fromDate, Date toDate, int branchId, String tripType,
			Time shiftTime) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c WHERE a.tripStatus ='completed'  AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime
				+ "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// Total dropped and picked up employees by shift and by vendor
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getPickedUpEmployeesCountByShiftByVendor(Date fromDate, Date toDate, int branchId, String tripType,
			Time shiftTime, int vendorId) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE  a.tripStatus ='completed' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime
				+ "' AND vem.vendorId='" + vendorId + "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total trip counts shift wise plus vendorId date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Time> getAllTripsByShiftByVendorId(Date fromDate, Date toDate, int branchId, String tripType,
			int vendorId, Time shiftTime) {
		String query = "SELECT Distinct Time(t.shiftTime) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' AND t.tripType='" + tripType + "' AND t.shiftTime='" + shiftTime
				+ "' AND vem.vendorId='" + vendorId + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Time> tripDates = (List<Time>) q.getResultList();
		return tripDates;
	}

	// No show Reports employeeId and employee name wise
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByIdAndName(Date fromDate, Date toDate, int branchId,
			String tripType, Time shiftTime) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c WHERE a.tripStatus ='completed'  AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' and a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime
				+ "' ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// No show Reports by Date wice and employee name wise
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByDateWise(Date fromDate, Date toDate, int branchId,
			String tripType) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c WHERE a.tripStatus ='completed'  AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' and a.tripType='" + tripType + "' ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// No show Reports employeeId and employee name wise from EmployeeId
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByEmployeeId(Date fromDate, Date toDate, int branchId,
			String tripType, Time shiftTime, String employeeId) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN t.eFmFmEmployeeTravelRequest tr JOIN tr.efmFmUserMaster u WHERE a.tripStatus ='completed'  AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' and a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime
				+ "' AND u.employeeId='" + employeeId + "' ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// No show Reports employeeId and employee name wise from EmployeeId
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByEmployeeIdDateWise(Date fromDate, Date toDate,
			String branchId, String tripType, String employeeId) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " JOIN t.eFmFmEmployeeTravelRequest tr JOIN tr.efmFmUserMaster u "
				+ " WHERE a.tripStatus ='completed'  AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  "
				+ " AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  and a.tripType='" + tripType + "' AND u.employeeId='" + employeeId
				+ "' ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// No show Reports employeeId and employee name wise from EmployeeId
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getParticuarEmployeesTravelledDetailByEmployeeId(Date fromDate, Date toDate,
			int branchId, String tripType, String employeeId) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN t.eFmFmEmployeeTravelRequest tr JOIN tr.efmFmUserMaster u WHERE a.tripStatus ='completed' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' and a.tripType='" + tripType + "' AND u.employeeId='" + employeeId
				+ "' ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// employee detail Reports from employeeId and employee name wise from
	// EmployeeId
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getParticuarEmployeesTravelledDetailByEmployeeIdAndBranchId(Date fromDate,
			Date toDate, String branchId, String employeeId) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN t.eFmFmEmployeeTravelRequest tr JOIN tr.efmFmUserMaster u WHERE a.tripStatus ='completed' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2 "
				+ " AND c.branchId in ("+branchId +")  AND u.employeeId='" + employeeId + "' ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getAllFemaleEmployeesTravelledDetailByDate(Date fromDate,
			Date toDate) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN t.eFmFmEmployeeTravelRequest tr JOIN tr.efmFmUserMaster u "
				+ " WHERE a.tripStatus ='completed' AND date(a.tripAssignDate) >= ?1  "
				+ " AND date(a.tripAssignDate) <=?2 "				
				+ " ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getTodayTripByShift(Date fromDate, Date toDate, String tripType, String ShifTime,
			int branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.format(fromDate);
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.efmFmEmployeeTripDetails t JOIN t.eFmFmEmployeeTravelRequest r JOIN b.eFmFmClientBranchPO c where b.shiftTime='"
						+ ShifTime + "' AND b.tripType='" + tripType + "' AND DATE(r.requestDate)>=TRIM('"
						+ formatter.format(fromDate) + "') AND DATE(r.requestDate)<=TRIM('" + formatter.format(toDate)
						+ "') AND b.tripStatus ='allocated' AND c.branchId='" + branchId + "'");
		allTrips = query.getResultList();
		return allTrips;
	}

	/*
	 * @Override
	 * 
	 * @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 * public List<EFmFmAssignRoutePO> getExportTodayTrips(Date fromDate,Date
	 * toDate,String tripType,String ShifTime,int branchId) { List
	 * <EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
	 * Format formatter; formatter = new SimpleDateFormat("yyyy-MM-dd");
	 * formatter.format(fromDate); Query query=entityManager.createQuery(
	 * "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.shiftTime='"
	 * +ShifTime+"' AND b.tripType='"+tripType+
	 * "' AND DATE(b.tripAssignDate)>=TRIM('"+formatter.format(fromDate)+
	 * "') AND DATE(b.tripAssignDate)<=TRIM('"+formatter.format(toDate)+
	 * "') AND b.tripStatus ='allocated' AND c.branchId='"+branchId+"'");
	 * allTrips=query.getResultList(); return allTrips; }
	 */

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getExportTodayTrips(Date fromDate, Date toDate, String tripType, String ShifTime,
			String branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.format(fromDate);
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.shiftTime='"
						+ ShifTime + "' AND b.tripType='" + tripType + "' AND DATE(b.tripAssignDate)>=TRIM('"
						+ formatter.format(fromDate) + "') AND DATE(b.tripAssignDate)<=TRIM('"
						+ formatter.format(toDate)
						+ "') AND b.tripStatus ='allocated' AND b.bucketStatus='N' AND c.branchId in ("+ branchId + ")");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllRoutesForPrintForParticularShift(Date fromDate, Date toDate, String tripType,
			String ShifTime, String branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.format(fromDate);
		Query query = entityManager.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.shiftTime='" + ShifTime
				+ "' AND b.tripType='" + tripType + "' AND c.branchId in ("+ branchId + ") AND DATE(b.tripAssignDate)>=TRIM('" + formatter.format(fromDate)
				+ "') AND DATE(b.tripAssignDate)<=TRIM('" + formatter.format(toDate)
				+ "') AND b.tripStatus !='completed'");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getOpenRouteDetailFromAssignRouteId(Date fromDate, Date toDate, String tripType,
			String ShifTime, String branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.format(fromDate);
		Query query = entityManager.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.shiftTime='" + ShifTime
				+ "' AND b.tripType='" + tripType + "' AND c.branchId in ("+ branchId + ") AND DATE(b.tripAssignDate)>=TRIM('" + formatter.format(fromDate)
				+ "') AND DATE(b.tripAssignDate)<=TRIM('" + formatter.format(toDate)
				+ "') AND b.tripStatus !='completed' AND b.bucketStatus='N'");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getParticularDateShiftTrips(Date fromDate, Date toDate, String tripType,
			String ShifTime, int branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.format(fromDate);
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.shiftTime='"
						+ ShifTime + "' AND b.tripType='" + tripType + "' AND DATE(b.tripAssignDate)>=TRIM('"
						+ formatter.format(fromDate) + "') AND DATE(b.tripAssignDate)<=TRIM('"
						+ formatter.format(toDate) + "') AND b.tripStatus !='completed'  AND c.branchId='" + branchId
						+ "'");
		allTrips = query.getResultList();
		return allTrips;
	}

	// total trip distinct date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Date> getAllTripsByDistinctDates(Date fromDate, Date toDate, String branchId) {
		String query = "SELECT Distinct DATE(t.tripAssignDate) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  "
				+ " AND   date(t.tripAssignDate) <=?2 "
				+ "  AND c.branchId  in ("+branchId +")  ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Date> tripDates = (List<Date>) q.getResultList();
		return tripDates;
	}

	// total trip distinct date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ArrayList> getAllTripsByDistinctDatesAndDeriverId(Date fromDate, Date toDate, int branchId) {
		String query = "SELECT Distinct (t.tripAssignDate,dm.driverId) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmDriverMaster dm WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "'  ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<ArrayList> tripDates = (List<ArrayList>) q.getResultList();
		return tripDates;
	}

	// total trip distinct date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsByDatesAndDriverId(Date fromDate, Date toDate, String branchId,
			int driverId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmDriverMaster dm WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  And dm.driverId='" + driverId + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllEscortRequiredTripsByDate(Date fromDate, Date toDate, String branchId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  AND t.escortRequredFlag='Y'"
				+ "  AND c.branchId in ("+branchId +")   ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// Expense Report
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllVehicleDistance(Date fromDate, Date toDate, int branchId, int vehicleId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster d JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' "
				+ "AND date(t.tripAssignDate) >=?1  " + "AND date(t.tripAssignDate) <=?2  " + "AND c.branchId='"
				+ branchId + "' " + "AND d.vehicleId='" + vehicleId + "' "
				+ "group by d.vehicleId  ORDER BY t.tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();

	}

	/* OTA $ report */

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsDetailsByShiftOTA(Date fromDate, Date toDate, String branchId,
			String tripType, Time shiftTime, String requestType) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  " + " AND   date(t.tripAssignDate) <=?2 "
						+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' AND t.shiftTime='" + shiftTime
				+ "'group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// total trip counts shift wise date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Time> getAllTripsByShiftOTA(Date fromDate, Date toDate, String branchId, String tripType, Time shiftTime,
			String requestType) {
		String query = "SELECT Distinct Time(t.shiftTime) FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' AND t.shiftTime='" + shiftTime + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Time> tripDates = (List<Time>) q.getResultList();
		return tripDates;

	}

	// total trip counts by vendorId date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Date> getAllTripsByByVendorIdOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId, String requestType) {
		String query = "SELECT Distinct DATE(t.tripAssignDate) FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' AND vem.vendorId='" + vendorId + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Date> tripDates = (List<Date>) q.getResultList();
		return tripDates;
	}

	// total trip distinct date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Date> getAllTripsDistinctDatesOTA(Date fromDate, Date toDate, String branchId, String tripType,
			String requestType) {
		String query = "SELECT Distinct DATE(t.tripAssignDate) FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  " + " AND   date(t.tripAssignDate) <=?2  "
						+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Date> tripDates = (List<Date>) q.getResultList();
		return tripDates;
	}

	// total trip counts by vendorId date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getUsedFleetByByVendorIdOTA(Date fromDate, Date toDate, String branchId, String tripType, int vendorId,
			String requestType) {
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' AND vem.vendorId='" + vendorId
				+ "' group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmAssignRoutePO = q.getResultList();
		long tripDates = (long) eFmFmAssignRoutePO.size();
		return tripDates;
	}

	// total trip counts by vendorId date counts and shift wise
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getUsedFleetByByVendorIdByShiftOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId, Time shiftTime, String requestType) {
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' AND vem.vendorId='" + vendorId + "' AND t.shiftTime='"
				+ shiftTime + "' group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmAssignRoutePO = q.getResultList();
		long tripDates = (long) eFmFmAssignRoutePO.size();
		return tripDates;
	}

	// total trip count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllTripsCountByDateOTA(Date fromDate, Date toDate, String branchId, String tripType,
			String requestType) {
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s "
				+ " JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  " + " AND date(t.tripAssignDate) <=?2  "
						+ " AND c.branchId in ("+branchId +") AND t.tripType='" + tripType
				+ "' group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmAssignRoutePO = q.getResultList();
		long tripDates = (long) eFmFmAssignRoutePO.size();
		return tripDates;
	}

	// total Delaytrips count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllDelayTripsCountByDateOTA(Date fromDate, Date toDate, int branchId, String tripType,
			String requestType) {
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  " + " AND   date(t.tripAssignDate) <=?2  " + " "
						+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmAssignRoutePO = q.getResultList();
		long tripDates = (long) eFmFmAssignRoutePO.size();
		return tripDates;
	}

	// total Delaytrips Beyond Login time count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllDelayTripsBeyondLoginTimeCountByDateOTA(Date fromDate, Date toDate, int branchId, String tripType,
			String requestType) {
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  " + " AND   date(t.tripAssignDate) <=?2  "
						+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmAssignRoutePO = q.getResultList();
		long tripDates = (long) eFmFmAssignRoutePO.size();
		return tripDates;
	}

	// total Employees traveled count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllEmployeesCountByDateOTA(Date fromDate, Date toDate, String branchId, String tripType,
			String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(a.tripAssignDate) >= ?1  " + " AND   date(a.tripAssignDate) <=?2  " + " "
						+ " AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total trip counts shift wise plus vendorId date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Time> getAllTripsByShiftByVendorIdOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId, Time shiftTime, String requestType) {
		String query = "SELECT Distinct Time(t.shiftTime) FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' AND t.shiftTime='" + shiftTime + "' AND vem.vendorId='"
				+ vendorId + "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Time> tripDates = (List<Time>) q.getResultList();
		return tripDates;
	}

	// total Employees traveled count by date By vendorId
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllEmployeesCountByDateByVendorIdOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId, String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE a.tripStatus ='completed'  " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND vem.vendorId='" + vendorId + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total No Show Employees count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getNoShowEmployeesCountByDateOTA(Date fromDate, Date toDate, String branchId, String tripType,
			String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId+")  AND a.tripType='" + tripType + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total No Show Employees count by date by vendor
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getNoShowEmployeesCountByDateByVendorOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId, String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE a.tripStatus ='completed'  " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId+")  AND a.tripType='" + tripType + "' AND vem.vendorId='" + vendorId + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total PickedUp Employees traveled count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getPickedUpEmployeesCountByDateOTA(Date fromDate, Date toDate, String branchId, String tripType,
			String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2 "
				+ "  AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total PickedUp Employees traveled count by date by vendor
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getPickedUpEmployeesCountByDateByVendorOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId, String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE  a.tripStatus ='completed'  " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND vem.vendorId='" + vendorId
				+ "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;

	}

	// total Delaytrips count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsByDateOTA(Date fromDate, Date toDate, String branchId, String tripType,
			String requestType) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllTripsCountByShiftOTA(Date fromDate, Date toDate, String branchId, String tripType, Time shiftTime,
			String requestType) {
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' AND t.shiftTime='" + shiftTime
				+ "' group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmAssignRoutePO = q.getResultList();
		long tripDates = (long) eFmFmAssignRoutePO.size();
		return tripDates;
	}

	// Vendorwise trip details
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsDetailsByShiftByVendorOTA(Date fromDate, Date toDate, String branchId,
			String tripType, Time shiftTime, int vendorId, String requestType) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' AND t.shiftTime='" + shiftTime + "' AND vem.vendorId='"
				+ vendorId + "' group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// Vendorwise details for date wice.
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsDetailsByVendorWiseOnlyOTA(Date fromDate, Date toDate, String branchId,
			String tripType, int vendorId, String requestType) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2 "
				+ "  AND c.branchId in ("+branchId +") AND t.tripType='" + tripType + "' AND vem.vendorId='" + vendorId
				+ "' group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllEmployeesCountByShiftOTA(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime, String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed'  " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2 "
				+ "  AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getAllEmployeesCountByShiftByVendorIdOTA(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime, int vendorId, String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE a.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime + "' AND vem.vendorId='"
				+ vendorId + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getNoShowEmployeesCountByShiftOTA(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime, String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed'  " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2 "
				+ "  AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// No show EmployeeCount By Shift wise and Vendor wise
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getNoShowEmployeesCountByShiftByVendorOTA(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime, int vendorId, String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE a.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2 "
				+ "  AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime
				+ "' AND vem.vendorId='" + vendorId + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getPickedUpEmployeesCountByShiftOTA(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime, String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed'  " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime
				+ "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// Total dropped and picked up employees by shift and by vendor
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getPickedUpEmployeesCountByShiftByVendorOTA(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime, int vendorId, String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE  a.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime + "' AND vem.vendorId='"
				+ vendorId + "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total No Show Employees count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getNoShowEmpCountByDateOTAView(Date fromDate, Date toDate, String branchId,
			String tripType, String requestType) {
		List<EFmFmEmployeeTripDetailPO> eFmFmEmployeeTripDetail = new ArrayList<EFmFmEmployeeTripDetailPO>();
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);

		eFmFmEmployeeTripDetail = q.getResultList();
		return eFmFmEmployeeTripDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getNoShowEmployeesCountByShiftOTAView(Date fromDate, Date toDate,
			String branchId, String tripType, Time shiftTime, String requestType) {
		List<EFmFmEmployeeTripDetailPO> eFmFmEmployeeTripDetail = new ArrayList<EFmFmEmployeeTripDetailPO>();
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed'  " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmEmployeeTripDetail = q.getResultList();
		return eFmFmEmployeeTripDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getNoShowEmployeesCountByShiftByVendorOTAView(Date fromDate, Date toDate,
			String branchId, String tripType, Time shiftTime, int vendorId, String requestType) {
		List<EFmFmEmployeeTripDetailPO> eFmFmEmployeeTripDetail = new ArrayList<EFmFmEmployeeTripDetailPO>();
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE a.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime
				+ "' AND vem.vendorId='" + vendorId + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmEmployeeTripDetail = q.getResultList();
		return eFmFmEmployeeTripDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getNoShowEmployeesCountByVendorWiseOTAView(Date fromDate, Date toDate,
			String branchId, String tripType, int vendorId, String requestType) {
		List<EFmFmEmployeeTripDetailPO> eFmFmEmployeeTripDetail = new ArrayList<EFmFmEmployeeTripDetailPO>();
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN a.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE a.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2 "
				+ "  AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND vem.vendorId='" + vendorId + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmEmployeeTripDetail = q.getResultList();
		return eFmFmEmployeeTripDetail;
	}

	// total trip counts by shift wise date counts
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Time> getAllTripsByAllShiftsOTA(Date fromDate, Date toDate, String branchId, String tripType,
			String requestType) {
		String query = "SELECT Distinct Time(t.shiftTime) FROM EFmFmAssignRoutePO t "
				+ " JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "'  ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Time> tripDates = (List<Time>) q.getResultList();
		return tripDates;

	}

	// total trip counts by shift wise on the bais of vendorid
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Time> getAllTripsByAllShiftsForVendorOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId, String requestType) {
		String query = "SELECT Distinct Time(t.shiftTime) FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster vem "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  " + " AND   date(t.tripAssignDate) <=?2  "
						+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' AND vem.vendorId='" + vendorId
				+ "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Time> tripDates = (List<Time>) q.getResultList();
		return tripDates;
	}

	// Trip sheet
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripByDateEmporGuest(Date fromDate, Date toDate, String branchId,
			String requestType) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r "
				+ " JOIN r.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  " + " AND   date(t.tripAssignDate) <=?2  " + " "
						+ " AND c.branchId in ("+branchId+")   " + " group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// Trip sheet
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripByDateForGuest(Date fromDate, Date toDate, int branchId,String requestType) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s "
				+ " JOIN s.eFmFmEmployeeTravelRequest r JOIN r.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType ='guest'"
				+ " AND date(t.tripAssignDate) >= ?1  " + " AND   date(t.tripAssignDate) <=?2  " + " "
						+ " AND c.branchId='"+branchId +"' group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// Noshow Report
	// No show Reports by Date wice and employee name wise
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByDateWiseNoShow(Date fromDate, Date toDate,
			String branchId, String tripType, String requestType) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed'  " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  "
				+ " AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId+")  and a.tripType='"+tripType+"' ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// No show Reports employeeId and employee name wise
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByIdAndNameNoShow(Date fromDate, Date toDate,
			String branchId, String tripType, Time shiftTime, String requestType) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r  JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed'  " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +") and a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime
				+ "' ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// No show Reports employeeId and employee name wise from EmployeeId
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripByShiftTimeAndTripType(Date fromDate, Date toDate, String branchId,
			String tripType, String shiftTime) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND t.tripType ='" + tripType + "'" + " AND t.shiftTime ='"
				+ shiftTime + "'" + " AND date(t.tripAssignDate) >= ?1  " + " AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +")  " + " group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// No show Reports employeeId and employee name wise from EmployeeId
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByEmployeeIdNoShow(Date fromDate, Date toDate,
			String branchId, String tripType, Time shiftTime, String employeeId, String requestType) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r  JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN t.eFmFmEmployeeTravelRequest tr JOIN tr.efmFmUserMaster u "
				+ " WHERE a.tripStatus ='completed'  " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +") and a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime
				+ "' AND u.employeeId='" + employeeId + "' ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// No show Reports employeeId and employee name wise from EmployeeId
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByEmployeeIdDateWiseNoShow(Date fromDate, Date toDate,
			int branchId, String tripType, String employeeId, String requestType) {
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c JOIN t.eFmFmEmployeeTravelRequest tr JOIN tr.efmFmUserMaster u "
				+ " WHERE a.tripStatus ='completed'  " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  AND c.branchId='"
				+ branchId + "' and a.tripType='" + tripType + "' AND u.employeeId='" + employeeId
				+ "' ORDER BY actualTime ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	// total No Show Employees count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getNoShowEmployeesCountByDateNoShowView(Date fromDate, Date toDate,
			String branchId, String tripType, String requestType) {
		List<EFmFmEmployeeTripDetailPO> eFmFmEmployeeTripDetailPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND t.boardedFlg ='NO' AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2 "
				+ " AND c.branchId  in ("+branchId +") AND a.tripType='" + tripType + "'";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmEmployeeTripDetailPO = q.getResultList();
		return eFmFmEmployeeTripDetailPO;
	}

	// total PickedUp Employees traveled count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getPickedUpEmployeesCountByDateView(Date fromDate, Date toDate, String branchId,
			String tripType, String requestType) {
		List<EFmFmEmployeeTripDetailPO> eFmFmEmployeeTripDetailPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId +") AND a.tripType='" + tripType + "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmEmployeeTripDetailPO = q.getResultList();
		return eFmFmEmployeeTripDetailPO;
	}

	// total trip count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsCountByDateNoShowView(Date fromDate, Date toDate, String branchId,
			String tripType, String requestType) {
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s "
				+ " JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  " + " AND date(t.tripAssignDate) <=?2  "
						+ " AND c.branchId in ("+branchId +") " + " AND t.tripType='" + tripType
				+ "' group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmAssignRoutePO = q.getResultList();
		return eFmFmAssignRoutePO;
	}

	// total PickedUp Employees traveled count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public long getPickedUpOrDroppedEmployeesCountByDateSeatUtil(Date fromDate, Date toDate, String branchId,
			String tripType, String requestType) {
		String query = "SELECT count(t) FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r  JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE  date(a.tripAssignDate) >= ?1  " + " AND r.requestType in (" + requestType + ")"
				+ " AND   date(a.tripAssignDate) <=?2  " + " "
						+ " AND c.branchId in ("+branchId +")  AND a.tripStatus ='completed' AND a.tripType='" + tripType
				+ "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		long empCountByTripDate = (long) q.getSingleResult();
		return empCountByTripDate;
	}

	// total PickedUp Employees traveled count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getPickedUpOrDroppedEmployeesCountByDateSeatview(Date fromDate, Date toDate,
			String branchId, String tripType, String requestType) {
		List<EFmFmEmployeeTripDetailPO> eFmFmEmployeeTripDetailPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE  date(a.tripAssignDate) >= ?1  " + " AND r.requestType in (" + requestType + ")"
				+ " AND   date(a.tripAssignDate) <=?2  " + " "
						+ " AND c.branchId in ("+branchId +")  AND a.tripStatus ='completed' AND a.tripType='" + tripType
				+ "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmEmployeeTripDetailPO = q.getResultList();
		return eFmFmEmployeeTripDetailPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTripDetailPO> getPickedUpEmployeesCountByShiftSeatUtilView(Date fromDate, Date toDate,
			String branchId, String tripType, Time shiftTime, String requestType) {
		List<EFmFmEmployeeTripDetailPO> eFmFmEmployeeTripDetailPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
		String query = "SELECT t FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
				+ " WHERE a.tripStatus ='completed'  " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(a.tripAssignDate) >= ?1  AND   date(a.tripAssignDate) <=?2 "
				+ " AND c.branchId in ("+branchId +")  AND a.tripType='" + tripType + "' AND a.shiftTime='" + shiftTime
				+ "' AND (t.boardedFlg ='B' OR  t.boardedFlg ='D')";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmEmployeeTripDetailPO = q.getResultList();
		return eFmFmEmployeeTripDetailPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsCountByShiftSeatView(Date fromDate, Date toDate, String branchId,
			String tripType, Time shiftTime, String requestType) {
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType in (" + requestType + ")"
				+ " AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2 "
				+ " AND c.branchId in ("+branchId +")  AND t.tripType='" + tripType + "' AND t.shiftTime='" + shiftTime
				+ "' group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		eFmFmAssignRoutePO = q.getResultList();
		return eFmFmAssignRoutePO;
	}

	// total employee count by date
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getEmployeeCountByTrips(int assignRouteId, int branchId) {
		List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
		String query = " SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s "
				+ "  JOIN t.eFmFmClientBranchPO c " + " WHERE t.assignRouteId ='" + assignRouteId + "' AND c.branchId='"
				+ branchId + "' ";
		Query q = entityManager.createQuery(query);
		eFmFmAssignRoutePO = q.getResultList();
		return eFmFmAssignRoutePO;
	}

	// Dynamic Report guest Details
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllGuestDynamicDetails(Date fromDate, Date toDate, String branchId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType ='guest'"
				+ " AND date(t.tripAssignDate) >= ?1  " + " AND   date(t.tripAssignDate) <=?2  " + " "
						+ " AND c.branchId in ("+branchId+")  " + " group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Date> getAllTripsByDistinctDatesByVendor(Date fromDate, Date toDate, String branchId, String vendorId) {
		String query = "SELECT Distinct DATE(t.tripAssignDate) FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmDriverMaster dm JOIN dm.efmFmVendorMaster ven WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in ("+branchId+") AND ven.vendorId in (" + vendorId + ") ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		List<Date> tripDates = (List<Date>) q.getResultList();
		return tripDates;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripByDateByVendor(Date fromDate, Date toDate, String branchId,
			String vendorId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmDriverMaster dm JOIN dm.efmFmVendorMaster ven WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2  "
				+ " AND c.branchId in("+branchId+") AND ven.vendorId in (" + vendorId + ")  ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripsByDatesAndDriverIdByVendor(Date fromDate, Date toDate, String branchId,
			int driverId, String vendorId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.eFmFmClientBranchPO c JOIN t.efmFmVehicleCheckIn ch JOIN ch.efmFmDriverMaster dm JOIN dm.efmFmVendorMaster ven WHERE t.tripStatus ='completed' AND date(t.tripAssignDate) >= ?1  AND   date(t.tripAssignDate) <=?2 "
				+ " AND c.branchId in ("+branchId+")  AND ven.vendorId in (" + vendorId + ") AND dm.driverId='" + driverId
				+ "' ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllEmployeeDynamicDetails(Date fromDate, Date toDate, String branchId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s JOIN s.eFmFmEmployeeTravelRequest r JOIN t.eFmFmClientBranchPO c "
				+ " WHERE t.tripStatus ='completed' " + " AND r.requestType !='guest'"
				+ " AND date(t.tripAssignDate) >= ?1  " + " AND   date(t.tripAssignDate) <=?2  " + " "
						+ " AND c.branchId  in ("+ branchId + ")  " + " group by t.assignRouteId ORDER BY tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAssignRouteDetailsByDate(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripType='"
				+ assignRoutePO.getTripType() + "' AND c.branchId in ("+assignRoutePO.getCombinedFacility() +") AND DATE(b.tripAssignDate)='" + assignRoutePO.getToDate()
				+ "' AND b.shiftTime='" + assignRoutePO.getShiftTime() + "' AND b.tripStatus ='completed' ");
		allTrips = query.getResultList();
		return allTrips;
	}

	// Expense Report
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllTripDetailsByVehicleId(Date fromDate, Date toDate, String branchId,
			int vehicleId) {
		String query = "SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmVehicleCheckIn v JOIN v.efmFmVehicleMaster d JOIN t.eFmFmClientBranchPO c WHERE t.tripStatus ='completed' "
				+ " AND date(t.tripAssignDate) >=?1  " + " AND date(t.tripAssignDate) <=?2  " + ""
						+ " AND c.branchId in ("+branchId +") AND d.vehicleId='" + vehicleId + "' " + " ORDER BY t.tripAssignDate ASC";
		Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
				toDate, TemporalType.TIMESTAMP);
		return q.getResultList();

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllActiveTripsAfterRouteClose(String tripType) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripType='"
						+ tripType + "' AND b.bucketStatus='Y'  AND b.tripStatus !='completed' ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getBackToBackTripDetailFromb2bId(int backTwoBackRouteId, String tripType,String branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.tripType='" + tripType
						+ "'  AND c.branchId in ("+ branchId + ") AND b.backTwoBackRouteId='" + backTwoBackRouteId + "'  AND b.tripStatus !='completed' ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getBackToBackTripDetailFromTripTypeANdShiftTime(String tripType, Time shiftTime,String branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c  where b.tripType='" + tripType
				+ "'  AND b.shiftTime > '" + shiftTime
				+ "' AND b.isBackTwoBack='N' AND b.routingCompletionStatus='completed' AND c.branchId in ("+ branchId + ") AND b.tripStatus !='completed' ");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllOnRoadVehicleByVendors(int vendorId, int branchId) {
		List<EFmFmAssignRoutePO> onroadVehicleDetails = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT a FROM EFmFmAssignRoutePO a JOIN a.efmFmVehicleCheckIn b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where g.branchId='"
						+ branchId + "' AND f.vendorId='" + vendorId
						+ "' AND a.tripStatus='Started' AND a.tripStartTime!=NULL " + " and b.checkOutTime is null "
						+ " and d.vehicleNumber NOT LIKE '%DUMMY%' ORDER BY a.tripAssignDate ASC ");
		onroadVehicleDetails = query.getResultList();
		return onroadVehicleDetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllZoneByVendor(int vendorId, int branchId) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		try {
			Query query = entityManager.createQuery(
					" SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c  JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z "
							+ " JOIN b.efmFmVehicleCheckIn cn JOIN cn.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster f "
							+ " where c.branchId='" + branchId + "' " + " AND f.vendorId='" + vendorId + "' "
							+ " AND b.tripStatus !='completed'  " + " AND b.plannedReadFlg='N' "
							+ " AND b.scheduleReadFlg='N' ORDER BY z.zoneName ASC");
			allTrips = query.getResultList();
		} catch (Exception e) {
			log.info("Zone error" + e);
			return allTrips;

		}
		return allTrips;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllRoutesAllocatedToVendor(String assignedVendorName) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		try {			
			Query query = entityManager.createQuery(
					"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c  JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where b.assignedVendorName='" + assignedVendorName + "'  AND b.tripStatus !='completed'  ORDER BY z.zoneName ASC ");		
			allTrips = query.getResultList();
		} catch (Exception e) {
			log.info("Vendor Routes error" + e);
			return allTrips;

		}
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllRoutesOfParticularZoneByVendor(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z JOIN b.efmFmVehicleCheckIn cn JOIN cn.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster f where b.tripStatus !='completed'  AND c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "' AND z.zoneId='"
						+ assignRoutePO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId()
						+ "' AND f.vendorId='" + assignRoutePO.getVendorId()
						+ "' AND b.plannedReadFlg='N' AND b.scheduleReadFlg='N' ORDER BY tripAssignDate ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getListOfRoutesByVendor(EFmFmAssignRoutePO assignRoutePO) {
		List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z JOIN b.efmFmVehicleCheckIn cn JOIN cn.efmFmVehicleMaster vm JOIN vm.efmFmVendorMaster f where b.tripStatus !='completed'  AND c.branchId='"
						+ assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "' AND z.zoneId='"
						+ assignRoutePO.geteFmFmRouteAreaMapping().geteFmFmZoneMaster().getZoneId()
						+ "' AND f.vendorId='" + assignRoutePO.getVendorId() + "'  ORDER BY createdDate ASC");
		allTrips = query.getResultList();
		return allTrips;
	}

							@Override
							@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
							public List<EFmFmAssignRoutePO> getListOfProjectIdsByDate(Date fromDate, Date toDate, String branchId) {
								List <EFmFmAssignRoutePO> projectIdDetails = new ArrayList<EFmFmAssignRoutePO>();		
								String query="SELECT a FROM EFmFmAssignRoutePO a JOIN a.eFmFmClientBranchPO c "
										+ " where a.tripStatus='completed' "
										+ " AND date(a.tripAssignDate) >= ?1  "
										+ " AND date(a.tripAssignDate) <=?2  "
										+ " AND c.branchId in ("+branchId +")  ";									
								Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
										toDate, TemporalType.TIMESTAMP);						
								projectIdDetails= q.getResultList();							
								return projectIdDetails;
							}
	
						
						@Override
						@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
						public List<EFmFmAssignRoutePO> getListOfDatesByProjectIds(Date fromDate, Date toDate, int branchId,int projectId) {
							List <EFmFmAssignRoutePO> projectIdDetails = new ArrayList<EFmFmAssignRoutePO>();		
							String query="SELECT a FROM EFmFmAssignRoutePO a JOIN a.efmFmEmployeeTripDetails r "
									+ " JOIN r.eFmFmEmployeeTravelRequest t JOIN a.eFmFmClientBranchPO c "
									+ " where a.tripStatus='completed' "
									+ " AND date(a.tripAssignDate) >= ?1  "
									+ " AND date(a.tripAssignDate) <=?2 "
									+ " AND c.branchId='"+branchId+"' "
									+ " AND t.projectId='"+projectId+"'group by DATE(a.tripAssignDate) order by a.tripAssignDate";
							Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
									toDate, TemporalType.TIMESTAMP);						
							projectIdDetails= q.getResultList();							
							return projectIdDetails;
						}
						
						
						@Override
						@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
						public List<EFmFmAssignRoutePO> getListOfRoutesByProjectIds(Date assignDate,int branchId,int projectId) {
							List <EFmFmAssignRoutePO> projectIdDetails = new ArrayList<EFmFmAssignRoutePO>();
							Format formatter;
							formatter = new SimpleDateFormat("yyyy-MM-dd");							
							String query="SELECT a FROM EFmFmAssignRoutePO a JOIN a.efmFmEmployeeTripDetails r "
									+ " JOIN r.eFmFmEmployeeTravelRequest t JOIN a.eFmFmClientBranchPO c "
									+ " where a.tripStatus='completed' "
									+ " AND date(a.tripAssignDate) ='"+formatter.format(assignDate)+"'  "									
									+ " AND c.branchId='"+branchId+"' "
									+ " AND t.projectId='"+projectId+"' group by a.assignRouteId order by a.assignRouteId";
							Query q = entityManager.createQuery(query);						
							projectIdDetails= q.getResultList();							
							return projectIdDetails;
						}
						
						@Override
						@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
						public List<EFmFmEmployeeTripDetailPO> getListOfEmployeesByProjectIds(int projectId,int assignRouteId) {
							List <EFmFmEmployeeTripDetailPO> projectIdDetails = new ArrayList<EFmFmEmployeeTripDetailPO>();		
							String query="SELECT r FROM EFmFmAssignRoutePO a JOIN a.efmFmEmployeeTripDetails r "
									+ " JOIN r.eFmFmEmployeeTravelRequest t JOIN t.efmFmUserMaster u"
									+ " where a.assignRouteId='"+assignRouteId+"' "						
									+ " AND t.projectId='"+projectId+"'";
							Query q = entityManager.createQuery(query);						
							projectIdDetails= q.getResultList();							
							return projectIdDetails;
						}
						
						@Override
						@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
						public List<EFmFmClientProjectDetailsPO> getProjectDetails(int projectId) {
							List <EFmFmClientProjectDetailsPO> projectIdDetails = new ArrayList<EFmFmClientProjectDetailsPO>();		
							String query="SELECT a FROM EFmFmClientProjectDetailsPO a where a.projectId='"+projectId+"'";							
							Query q = entityManager.createQuery(query);						
							projectIdDetails= q.getResultList();							
							return projectIdDetails;
						}
						
						 // total employee count by date
						@Override
						@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
						public List<EFmFmAssignRoutePO> getTravelledEmployeeCountByTrips(int assignRouteId, String branchId) {
							List<EFmFmAssignRoutePO> eFmFmAssignRoutePO = new ArrayList<EFmFmAssignRoutePO>();
							String query = " SELECT t FROM EFmFmAssignRoutePO t JOIN t.efmFmEmployeeTripDetails s "
									+ " JOIN t.eFmFmClientBranchPO c " + " "
									+ " WHERE t.assignRouteId ='" + assignRouteId + "' "
									+ " AND (s.boardedFlg ='B' OR  s.boardedFlg ='D') "
									+ " AND c.branchId  in ("+branchId +")  ";
							Query q = entityManager.createQuery(query);
							eFmFmAssignRoutePO = q.getResultList();
							return eFmFmAssignRoutePO;
						}

						
						@Override
						@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
						public List<EFmFmAssignRoutePO> getListOfTripsByProjectIdWithDate(Date fromDate, Date toDate, String branchId,int projectId) {
							List <EFmFmAssignRoutePO> projectIdDetails = new ArrayList<EFmFmAssignRoutePO>();		
							String query="SELECT a FROM EFmFmEmployeeTripDetailPO t JOIN t.eFmFmEmployeeTravelRequest r "
									+ " JOIN t.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c "
									+ " where a.tripStatus='completed' "
									+ " AND date(a.tripAssignDate) >= ?1  "
									+ " AND date(a.tripAssignDate) <=?2  "
									+ " AND r.projectId='"+projectId+"'  "
									+ " AND (r.requestStatus not like '%C%' OR r.requestStatus !='R') AND  r.readFlg='R' "
									+ " AND c.branchId in ("+branchId +")  "
											+ " group by a.assignRouteId order by DATE(a.tripAssignDate)";
							Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
									toDate, TemporalType.TIMESTAMP);						
							projectIdDetails= q.getResultList();							
							return projectIdDetails;
						}

						
						
						
						
	
	

}