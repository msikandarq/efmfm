package com.newtglobal.eFmFmFleet.business.dao.daoImpl;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newtglobal.eFmFmFleet.business.dao.ICabRequestDAO;
import com.newtglobal.eFmFmFleet.model.EFmCheckInVehicleTrackingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeRequestMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmIndicationMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmMultipleLocationTvlReqPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripTimingMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;

@Repository("ICabRequestDAO")
public class CabRequestDAOImpl implements ICabRequestDAO {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(EFmCheckInVehicleTrackingPO eFmCheckInVehicleTrackingPO) {
        entityManager.persist(eFmCheckInVehicleTrackingPO);
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        entityManager.persist(eFmFmEmployeeTravelRequestPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(EFmFmTripTimingMasterPO eFmFmTripTimingMasterPO) {
        entityManager.persist(eFmFmTripTimingMasterPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void update(EFmCheckInVehicleTrackingPO eFmCheckInVehicleTrackingPO) {
        entityManager.merge(eFmCheckInVehicleTrackingPO);
    }   
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void update(EFmFmTripTimingMasterPO eFmFmTripTimingMasterPO) {
        entityManager.merge(eFmFmTripTimingMasterPO);
    }
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(EFmFmAssignRoutePO eFmFmAssignRoutePO) {
        entityManager.persist(eFmFmAssignRoutePO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(EFmFmEmployeeRequestMasterPO employeeRequestMasterPO) {
        entityManager.persist(employeeRequestMasterPO);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void update(EFmFmAssignRoutePO eFmFmAssignRoutePO) {
        entityManager.merge(eFmFmAssignRoutePO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void update(EFmFmEmployeeTripDetailPO employeeTripDetailPO) {
        entityManager.merge(employeeTripDetailPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(EFmFmEmployeeTripDetailPO employeeTripDetailPO) {
        entityManager.persist(employeeTripDetailPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteParticularTripDetail(int empTripId) {
        Query query = entityManager
                .createQuery("DELETE EFmFmEmployeeTripDetailPO where empTripId = '" + empTripId + "' ");
        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteParticularRequest(int requestId) {
        Query query = entityManager
                .createQuery("DELETE EFmFmEmployeeTravelRequestPO where requestId = '" + requestId + "' ");
        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteParticularRequestFromRequestMaster(int tripId) {
        Query query = entityManager.createQuery("DELETE EFmFmEmployeeRequestMasterPO where tripId = '" + tripId + "' ");
        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void update(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        entityManager.merge(eFmFmEmployeeTravelRequestPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void update(EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMasterPO) {
        entityManager.merge(eFmFmEmployeeRequestMasterPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void delete(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        entityManager.remove(eFmFmEmployeeTravelRequestPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> travelRequestExist(String employeeId, String tripType, int branchId,
            String requestType) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c  where u.employeeId='"
                        + employeeId + "' and b.tripType='" + tripType + "' AND c.branchId='" + branchId
                        + "' AND b.requestType='" + requestType + "' AND b.status='Y' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getRequestFromRequestMaster(int tripId, String branchId) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c  where b.tripId='"
                        + tripId + "'  AND c.branchId in ("+ branchId + ") ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getAllRequestFromRequestMasterFprParticularEmployee(int userId,
            String branchId) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c  where u.userId='"
                        + userId + "'  AND c.branchId in ("+ branchId + ") AND b.readFlg='Y' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }

    @Override
    public List<EFmFmEmployeeRequestMasterPO> getEmplyeeRequestsForSameDateAndShiftTime(Date date, Time siftTime,
            String branchId, int userId, String tripType) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        String todayDate;
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = formatter.format(date);
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u   where u.userId='"
                        + userId + "' and b.tripRequestStartDate='" + todayDate + "' and b.tripType='" + tripType
                        + "' and b.shiftTime='" + siftTime + "'  and b.status='Y' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
    
    @Override
    public List<EFmFmEmployeeRequestMasterPO> getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(Date date, Time siftTime,
            String branchId, int userId, String tripType) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        String todayDate;
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = formatter.format(date);
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c  where u.userId='"
                        + userId + "' and b.tripRequestStartDate='" + todayDate + "' and b.tripType='" + tripType
                        + "' and b.shiftTime='" + siftTime + "'");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
    
    @Override
    public List<EFmFmEmployeeRequestMasterPO> getActiveEmplyeeRequest(int branchId, int userId, String tripType) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u where u.userId='"
                        + userId + "' and b.tripType='" + tripType
                        + "' and b.status='Y' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(Date date,
            Time siftTime, String branchId, int userId, String tripType) {
        List<EFmFmEmployeeTravelRequestPO> employeeRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        String todayDate;
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = formatter.format(date);
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u where u.userId='"
                        + userId + "' and b.requestDate='" + todayDate + "' and  b.tripType='" + tripType
                        + "' and b.shiftTime='" + siftTime + "' and ( b.requestStatus='E' OR b.requestStatus='RM' OR b.requestStatus='RW' OR b.requestStatus='M' OR b.requestStatus='W' OR b.requestStatus='Y') ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllTodaysActiveRequests(int branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId='"
                        + branchId + "' AND b.isActive='Y'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllActiveRequests(int branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId='"
                        + branchId + "' AND b.isActive='Y' AND b.completionStatus='N' ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequest(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r JOIN r.eFmFmClientBranchPO c where c.branchId='"
                        + eFmFmEmployeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
                                .geteFmFmClientBranchPO().getBranchId()
                        + "' AND b.approveStatus='Y' AND b.readFlg='Y'  ORDER BY pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> listOfAdhocAndGuestTravelRequests(int branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  where c.branchId='"
                        + branchId
                        + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND b.requestType!='normal' AND b.requestType!='AdhocRequest' ORDER BY requestDate,z.zoneName,pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> listOfGuestTravelRequests(int branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  where c.branchId='"
                        + branchId
                        + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND b.requestType!='normal' AND b.requestType!='AdhocRequest' AND b.requestType!='nodal' ORDER BY requestDate,z.zoneName,pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> listOfGuestAndAdhocRequestTravelRequests(int branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  where c.branchId='"
                        + branchId
                        + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND (b.requestType='guest' OR b.requestType='AdhocRequest')  ORDER BY requestDate,z.zoneName,pickUpTime ASC").setMaxResults(200);
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getGuestAndAdhocTravelRequestsForGivendate(String branchId,String requestDate) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  where c.branchId in ("+ branchId + ") AND b.approveStatus='Y' AND DATE(b.requestDate)='" + requestDate + "' AND b.readFlg='Y' AND (b.requestType='guest' OR b.requestType='AdhocRequest')  ORDER BY requestDate,z.zoneName,pickUpTime ASC").setMaxResults(200);
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> listOfAdhocReservationsForGuestTravelRequests(int branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b  JOIN u.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  where c.branchId='"
                        + branchId
                        + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND b.requestType='AdhocRequest' ORDER BY requestDate,z.zoneName,pickUpTime ASC").setMaxResults(200);
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Time> listOfAllShiftTimesFromTravelDesk(String branchId, String tripType) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT Distinct Time(b.shiftTime) FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  where c.branchId in ("+ branchId + ") AND b.tripType='" + tripType
                        + "' AND b.approveStatus='Y' AND b.readFlg='Y' ORDER BY b.shiftTime ASC").setMaxResults(200);
        List<Time> tripShiftTimes = (List<Time>) query.getResultList();
        return tripShiftTimes;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> deleteCurentRequestfromTraveldesk(int branchId, int tripId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r  JOIN r.eFmFmClientBranchPO c where c.branchId='"
                        + branchId + "' AND r.tripId='" + tripId + "' AND b.approveStatus='Y' AND b.readFlg='Y' ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequestByShiftWice(int branchId,String requestDate,Time shiftTime) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId='"
                        + branchId + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' AND b.shiftTime='" + shiftTime
                        + "' AND b.requestType='normal' ORDER BY requestDate,z.zoneName,pickUpTime ASC").setMaxResults(200);
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequestByShiftWiceForNormalAndAdhocRequests(int branchId,String requestDate,Time shiftTime) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b  JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId='"
                        + branchId + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' AND b.shiftTime='" + shiftTime
                        + "' AND b.requestType!='guest' AND b.requestType!='AdhocRequest' ORDER BY requestDate,z.zoneName,pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForNormalAndAdhoc(String branchId,String requestDate,Time shiftTime,String tripType) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' AND b.shiftTime='" + shiftTime
                        + "' AND b.requestType!='guest' AND b.requestType!='AdhocRequest' AND b.tripType='"+tripType+"' ORDER BY requestDate,z.zoneName,pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhoc(String branchId,String requestDate,String tripType) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' AND  b.requestType!='guest' AND b.requestType!='AdhocRequest' AND b.tripType='"+tripType+"' ORDER BY requestDate,z.zoneName,pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
	
	 @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllNormalAndAdhocRequest(int branchId,String requestDate,String tripType) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId='"
                        + branchId + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' "
                        		+ " AND b.requestType!='guest' AND b.requestType!='AdhocRequest' AND b.tripType='"+tripType+"' ORDER BY requestDate,z.zoneName,pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceBothPickAndDropForNormalAndAdhoc(String branchId,String requestDate) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' AND  b.requestType!='guest' AND b.requestType!='AdhocRequest'  ORDER BY requestDate,z.zoneName,pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllocatedCountReuestsByShiftWice(int branchId,String requestDate,Time shiftTime,String tripType) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId='"
                        + branchId + "' AND b.approveStatus='Y' AND b.readFlg='R' AND DATE(b.requestDate)='" + requestDate + "' AND b.shiftTime='" + shiftTime
                        + "' AND b.tripType='"+tripType+"' ");
        long numberOfPickUpRequest = (long) query.getSingleResult();
        return numberOfPickUpRequest;
    }
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequestForAdminShiftWise(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId='"
                        + eFmFmEmployeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
                                .geteFmFmClientBranchPO().getBranchId()
                        + "' AND b.shiftTime='" + eFmFmEmployeeTravelRequestPO.getShiftTime()
                        + "' AND b.approveStatus='Y' AND b.readFlg='Y'  ORDER BY pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> assignCabRequestToParticularShiftOrRouteEmployees(String requestDate,String branchId,
            String tripType, Time siftTime, int zoneId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  "
                + " where c.branchId in ("+branchId+") and DATE(b.requestDate)='" + requestDate + "'  and b.tripType='" + tripType + "' and b.shiftTime='" + siftTime
                        + "' and z.zoneId='" + zoneId + "'and b.approveStatus='Y' and b.readFlg='Y' ORDER BY c.branchId ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> assignCabRequestToParticularShiftEmployees(int branchId, String tripType,
            Time siftTime) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
                        + branchId + "' and b.tripType='" + tripType + "' and b.shiftTime='" + siftTime
                        + "' and  b.approveStatus='Y' and b.readFlg='Y'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> assignCabToParticularShiftOrDateEmployees(String requestDate,
            int branchId, String tripType, Time siftTime) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b  JOIN b.eFmFmClientBranchPO c   where c.branchId='"
                        + branchId + "' and DATE(b.requestDate)='" + requestDate + "' and b.tripType='" + tripType
                        + "' and b.shiftTime='" + siftTime + "' and  b.approveStatus='Y' and b.readFlg='Y'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> assignCabToPickupShiftOrDateEmployees(String requestDate,
            String branchId, String tripType, Time siftTime) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c  "
                + " where c.branchId in ("+branchId+") and DATE(b.requestDate)='" + requestDate + "' and b.tripType='" + tripType
                        + "' and b.shiftTime='" + siftTime + "' and  b.approveStatus='Y' and b.readFlg='Y' ORDER BY c.branchId,b.pickUpTime DESC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> assignCabToDropShiftOrDateEmployees(String requestDate,
            String branchId, String tripType, Time siftTime) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c   "
                + " where c.branchId in ("+branchId+") and DATE(b.requestDate)='" + requestDate + "' and b.tripType='" + tripType
                        + "' and b.shiftTime='" + siftTime + "' and  b.approveStatus='Y' and b.readFlg='Y' ORDER BY c.branchId,b.dropSequence ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    
    @Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForPickupRouting(String requestDate,
            String branchId, String tripType, Time shiftTime) {
		List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+branchId+") AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='"
						+ requestDate + "' AND b.shiftTime='" + shiftTime
						+ "' AND (b.requestType!='guest'  AND b.requestType!='AdhocRequest' AND b.requestType!='nodal') AND b.tripType='"+tripType+"' ORDER BY u.distance DESC");
		employeeTravelRequestPO = query.getResultList();
		return employeeTravelRequestPO;
	}
    
    
    @Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForDropRouting(String requestDate,
            String branchId, String tripType, Time shiftTime) {
		List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+branchId+") AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='"
						+ requestDate + "' AND b.shiftTime='" + shiftTime
						+ "' AND (b.requestType!='guest'  AND b.requestType!='AdhocRequest' AND b.requestType!='nodal') AND b.tripType='"+tripType+"' ORDER BY u.distance ASC");
		employeeTravelRequestPO = query.getResultList();
		return employeeTravelRequestPO;
	}
    
    
    
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> particularDateRequestForEmployees(Date date,int branchId, int userId, String tripType) {
        List<EFmFmEmployeeTravelRequestPO> employeeRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        String todayDate;
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = formatter.format(date);
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u  where u.userId='"
                        + userId + "' and b.requestDate='" + todayDate + "' and  b.tripType='" + tripType
                        + "' and b.readFlg='Y' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> assignCabToParticularShiftDateOrRouteEmployees(String requestDate,
            String branchId, String tripType, Time siftTime, int zoneId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        System.out.println("requestDate"+requestDate+"branchId"+branchId+"tripType"+tripType+"siftTime"+siftTime+"zoneId"+zoneId);
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b  JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  "
                + " where c.branchId in ("+branchId+") and DATE(b.requestDate)='" + requestDate + "' and b.tripType='" + tripType
                        + "' and b.shiftTime='" + siftTime + "' and z.zoneId='" + zoneId
                        + "'and b.approveStatus='Y' and b.readFlg='Y' ORDER BY c.branchId ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripTimingMasterPO> listOfShiftTime(String branchId) {
        List<EFmFmTripTimingMasterPO> shiftTime = new ArrayList<EFmFmTripTimingMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmTripTimingMasterPO b JOIN b.eFmFmClientBranchPO  c "
                		+ " where c.branchId in("+branchId+") AND b.isActive='Y'  ORDER BY b.shiftTime ASC ");
        shiftTime = query.getResultList();
        return shiftTime;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripTimingMasterPO> listOfShiftTimeForEmployees(String branchId) {
        List<EFmFmTripTimingMasterPO> shiftTime = new ArrayList<EFmFmTripTimingMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmTripTimingMasterPO b JOIN b.eFmFmClientBranchPO  c "
                		+ " where c.branchId   in ("+branchId+")  AND b.isActive='Y' AND mobileVisibleFlg='E' ORDER BY b.shiftTime ASC ");
        shiftTime = query.getResultList();
        return shiftTime;
    }
    
  

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripTimingMasterPO> getShiftTimeDetailFromShiftTimeAndTripType(String branchId, Time shiftTime,
            String tripType) {
        List<EFmFmTripTimingMasterPO> shiftTimeDetail = new ArrayList<EFmFmTripTimingMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmTripTimingMasterPO b JOIN b.eFmFmClientBranchPO  c"
                		+ "  where c.branchId in ("+ branchId + ") AND b.tripType='" + tripType + "' AND b.shiftTime='" + shiftTime
                        + "' AND b.isActive='Y' ");
        shiftTimeDetail = query.getResultList();
        return shiftTimeDetail;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripTimingMasterPO> listOfShiftTimeByTripType(String branchId, String tripType) {
        List<EFmFmTripTimingMasterPO> shiftTime = new ArrayList<EFmFmTripTimingMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmTripTimingMasterPO b JOIN b.eFmFmClientBranchPO  c where c.branchId in ("+ branchId + ") AND b.tripType='" + tripType + "' AND b.isActive='Y' ORDER BY b.shiftTime ASC ");
        shiftTime = query.getResultList();
        return shiftTime;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripTimingMasterPO> listOfShiftTimeByTripTypeForEmployees(int branchId, String tripType) {
        List<EFmFmTripTimingMasterPO> shiftTime = new ArrayList<EFmFmTripTimingMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmTripTimingMasterPO b JOIN b.eFmFmClientBranchPO  c where c.branchId='" + branchId
                        + "' AND b.tripType='" +tripType+"'"
                        		+ " AND (b.mobileVisibleFlg=('E') OR b.mobileVisibleFlg=('B'))"
                        		+ " AND b.isActive='Y' ORDER BY b.shiftTime ASC");
        shiftTime = query.getResultList();        
        return shiftTime;
    }
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripTimingMasterPO> getParticularShiftTimeDetail(int branchId, Time shiftTime) {
        List<EFmFmTripTimingMasterPO> shiftTimeDetail = new ArrayList<EFmFmTripTimingMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmTripTimingMasterPO b JOIN b.eFmFmClientBranchPO  c where c.branchId='"
                        + branchId + "' AND b.shiftTime='" + shiftTime + "' AND b.isActive='Y' ");
        shiftTimeDetail = query.getResultList();
        return shiftTimeDetail;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripTimingMasterPO> getParticularShiftTimeDetailByTripType(String branchId, Time shiftTime,
            String tripType) {
        List<EFmFmTripTimingMasterPO> shiftTimeDetail = new ArrayList<EFmFmTripTimingMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmTripTimingMasterPO b JOIN b.eFmFmClientBranchPO  c where c.branchId in ("+ branchId + ") AND b.shiftTime='" + shiftTime + "' AND b.tripType='" + tripType
                        + "' AND b.isActive='Y' ");
        shiftTimeDetail = query.getResultList();
        return shiftTimeDetail;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllResheduleRequests(int projectId, int branchId) {
        List<EFmFmEmployeeTravelRequestPO> resheduleRequests = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r JOIN r.efmFmUserMaster u JOIN u.eFmFmClientBranchPO  c JOIN u.eFmFmClientProjectDetails d where  b.isActive='N' AND b.approveStatus='N' AND  b.readFlg='N' AND d.projectId='"
                        + projectId + "' AND c.branchId='" + branchId
                        + "' AND  (b.requestStatus='CW' OR b.requestStatus='CM' OR b.requestStatus='RM' OR b.requestStatus='RW' OR b.requestStatus='M' OR b.requestStatus='W') ");
        resheduleRequests = query.getResultList();
        return resheduleRequests;
    }
    
    
    //Request For Reshedule for employee
    @Override
    public List<EFmFmEmployeeTravelRequestPO> getTodayRequestForParticularEmployee(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {
        List<EFmFmEmployeeTravelRequestPO> todayrequests = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b  JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where c.branchId='"
                        + eFmFmEmployeeTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
                                .geteFmFmClientBranchPO().getBranchId()
                        + "' AND u.userId='" + eFmFmEmployeeTravelRequest.getEfmFmUserMaster().getUserId()
                        + "' AND ( b.requestStatus='RM' OR b.requestStatus='RW' OR b.requestStatus='M' OR b.requestStatus='W' OR b.requestStatus='Y' OR b.requestStatus='E') ORDER BY b.requestDate ASC");
        todayrequests = query.getResultList();
        return todayrequests;
    }

    //Employee Todays Trip Service based on UserId
    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllTodaysRequestForParticularEmployee(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {
        List<EFmFmEmployeeTravelRequestPO> todayrequests = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where c.branchId='"
                        + eFmFmEmployeeTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
                                .geteFmFmClientBranchPO().getBranchId()
                        + "' AND u.userId='" + eFmFmEmployeeTravelRequest.getEfmFmUserMaster().getUserId()
                        + "' AND b.readFlg='Y' ORDER BY b.requestDate ASC ");
        todayrequests = query.getResultList();
        return todayrequests;
    }
    
    
    //Employee Todays Trip Service based on mobileNumber
    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllRequestForParticularEmployeeBasedOnMobileNumber(String branchId,String mobileNumber,int startPgNo,int endPgNo) {
        List<EFmFmEmployeeTravelRequestPO> todayrequests = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN b.eFmFmClientBranchPO c "
                + " where c.branchId in ("+branchId+") AND u.mobileNumber='" +mobileNumber+ "' "
                		+ " AND b.readFlg='Y' ORDER BY b.requestDate ASC ").setFirstResult(startPgNo)
                .setMaxResults(endPgNo); 
        todayrequests = query.getResultList();
        return todayrequests;
    }
    
    
    
    
    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllUpComingRequestForParticularEmployeeTripBased(int branchId,int userId,String tripType) {
        List<EFmFmEmployeeTravelRequestPO> todayrequests = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where c.branchId='"
                        + branchId
                        + "' AND u.userId='" + userId
                        + "' AND b.tripType='"+tripType+"' AND b.requestType='normal' AND b.readFlg='Y' ");
        todayrequests = query.getResultList();
        return todayrequests;
    }
    
    
    
    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllUpComingRequestForParticularEmployee(int branchId,int userId,String tripType) {
        List<EFmFmEmployeeTravelRequestPO> todayrequests = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where c.branchId='"
                        + branchId
                        + "' AND u.userId='" + userId
                        + "' AND b.tripType='"+tripType+"' AND b.readFlg='Y' ");
        todayrequests = query.getResultList();
        return todayrequests;
    }

    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getparticularRequestDetail(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b where b.requestId='"
                        + eFmFmEmployeeTravelRequestPO.getRequestId() + "' ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getParticularRequestDetailOnTripComplete(int requestId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b  where b.requestId='"
                        + requestId + "' ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getParticularApproveRequestDetail(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        /*
         * String todayDate; Format formatter; formatter = new
         * SimpleDateFormat("yyyy-MM-dd"); todayDate =
         * formatter.format(eFmFmEmployeeTravelRequestPO.getRequestDate());
         */
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r JOIN r.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where b.approveStatus='Y' and u.userId='"
                        + eFmFmEmployeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().getUserId()
                        + "' AND c.branchId='"
                        + eFmFmEmployeeTravelRequestPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId()
                        + "'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAnotherActiveRequestDetail(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r JOIN r.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where b.approveStatus='Y' AND b.readFlg='Y' AND b.isActive='Y' AND b.requestType='"
                        + eFmFmEmployeeTravelRequestPO.getRequestType() + "' AND b.tripType='"
                        + eFmFmEmployeeTravelRequestPO.getTripType() + "' AND u.userId='"
                        + eFmFmEmployeeTravelRequestPO.getEfmFmUserMaster().getUserId() + "' AND c.branchId='"
                        + eFmFmEmployeeTravelRequestPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId()
                        + "'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAnotherActiveRequestForNextDate(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Date todayDate = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(todayDate);
        cal.add(Calendar.DATE, 1);
        Date currentDate = cal.getTime();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String nextDate = formatter.format(currentDate);
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r JOIN r.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where DATE(b.requestDate)=TRIM('"
                        + nextDate
                        + "') AND b.approveStatus='Y' AND b.readFlg='Y' AND b.isActive='Y' AND b.requestType='"
                        + eFmFmEmployeeTravelRequestPO.getRequestType() + "' AND b.tripType='"
                        + eFmFmEmployeeTravelRequestPO.getTripType() + "' AND u.userId='"
                        + eFmFmEmployeeTravelRequestPO.getEfmFmUserMaster().getUserId() + "' AND c.branchId='"
                        + eFmFmEmployeeTravelRequestPO.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId()
                        + "'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getParticularRequestFromRequestDateAndUserId(Date todayDate, String tripType,int userId,int branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Calendar cal = new GregorianCalendar();
        cal.setTime(todayDate);
        Date currentDate = cal.getTime();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String nextDate = formatter.format(currentDate);
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where DATE(b.requestDate)=TRIM('"
                        + nextDate
                        + "')  AND b.requestType='normal' AND b.tripType='"
                        + tripType + "' AND u.userId='"
                        + userId + "' AND c.branchId='"
                        + branchId
                        + "'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getParticularEmployeeLastRequestFromUserId(String tripType,int userId,int branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where  b.requestType='normal' AND b.tripType='"
                        + tripType + "' AND u.userId='"
                        + userId + "' AND c.branchId='"
                        + branchId
                        + "' and ((b.readFlg='Y' and b.completionStatus='N')  OR (b.readFlg='R' and b.completionStatus='N')) ORDER BY b.requestDate ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    } 
    
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getParticularRequestDetail(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager
                .createQuery(
                        "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r JOIN r.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where b.employeeId='"
                                + eFmFmEmployeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster()
                                        .getUserId()
                                + "' AND  b.tripType='"
                                + eFmFmEmployeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getTripType()
                                + "' AND c.branchId='" + eFmFmEmployeeTravelRequestPO.geteFmFmEmployeeRequestMaster()
                                        .getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId()
                                + "' ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAssignRoutePO> gettripForParticularDriver(EFmFmAssignRoutePO assignRoutePO) {
        List<EFmFmAssignRoutePO> tripDtails = new ArrayList<EFmFmAssignRoutePO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn ch where ch.checkInId='"
                        + assignRoutePO.getEfmFmVehicleCheckIn().getCheckInId()
                        + "' AND b.tripStatus!='completed' AND c.branchId='"
                        + assignRoutePO.geteFmFmClientBranchPO().getBranchId() + "'");
        tripDtails = query.getResultList();
        return tripDtails;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAssignRoutePO> getLastRouteDetails(int checkInId, int branchId, String tripType) {
        List<EFmFmAssignRoutePO> tripDtails = new ArrayList<EFmFmAssignRoutePO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn ch where ch.checkInId='"
                        + checkInId + "' AND b.tripType='" + tripType
                        + "' AND b.tripStatus='allocated' AND c.branchId='" + branchId + "'");
        tripDtails = query.getResultList();
        return tripDtails;
    }

    // Trip on driver device after closing the bucket only
    @Override
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
    
    // Trip on driver device after closing the bucket means all started vehicles
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAssignRoutePO> getStartedVehicleDetailFromVehicleNumber(int checkInId, String branchId) {
        List<EFmFmAssignRoutePO> tripDtails = new ArrayList<EFmFmAssignRoutePO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn ch where ch.checkInId='"
                        + checkInId + "' AND b.tripStatus='Started' AND b.bucketStatus='Y' AND c.branchId in ("+ branchId + ") ORDER BY b.tripAssignDate ASC ");
        tripDtails = query.getResultList();
        return tripDtails;
    }
       

    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getDropTripAllSortedEmployees(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
                        + assignRouteId + "' ORDER BY t.dropSequence ASC");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    
    
    
    

    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getLastDropEmployeeDetail(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
                        + assignRouteId + "' AND b.boardedFlg='D' ORDER BY t.dropSequence DESC");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getDropTripAllSortedNonDropEmployees(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where b.boardedFlg='N' and d.assignRouteId='"
                        + assignRouteId + "' ORDER BY t.dropSequence ASC");
        tripemployees = query.getResultList();
        return tripemployees;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getNonDropTripAllSortedEmployees(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where b.boardedFlg='N' AND d.assignRouteId='"
                        + assignRouteId + "' ORDER BY t.dropSequence ASC");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAlgoDropRequestEmployees(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where b.boardedFlg='N' AND d.assignRouteId='"
                        + assignRouteId + "' ORDER BY t.pickUpTime ASC");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getParticularTripNonDropEmployeesDetails(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripEmployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where b.boardedFlg='N'  AND d.assignRouteId='"
                        + assignRouteId + "' ORDER BY t.pickUpTime ASC");
        tripEmployees = query.getResultList();
        return tripEmployees;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAssignRoutePO> getParticularDriverAssignTripDetail(EFmFmAssignRoutePO assignRoutePO) {
        List<EFmFmAssignRoutePO> tripDtails = new ArrayList<EFmFmAssignRoutePO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmAssignRoutePO b where  b.assignRouteId='"
                        + assignRoutePO.getAssignRouteId() + "' ");
        tripDtails = query.getResultList();
        return tripDtails;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getParticularTripParticularEmployees(int employeeId, int assignRouteId,
            int branchId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c where d.assignRouteId='"
                        + assignRouteId + "' AND b.employeeId='" + employeeId + "' AND c.branchId='" + branchId + "'");
        tripemployees = query.getResultList();
        return tripemployees;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getTodayTripEmployeesDetail(int userId, int branchId, Date todayDate) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN b.eFmFmEmployeeTravelRequest r JOIN r.efmFmUserMaster u where  u.userId='"
                        + userId + "'  AND c.branchId='" + branchId + "'");
        tripemployees = query.getResultList();
        return tripemployees;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getEmployeeLiveTripDetailFromUserId(int userId, int branchId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN b.eFmFmEmployeeTravelRequest r JOIN r.efmFmUserMaster u where  u.userId='"
                        + userId + "'  AND c.branchId='" + branchId + "' AND d.tripStatus='Started' ");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    
    
    
   

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllocatedEmployeeDetail(int userId, String branchId, Date todayDate) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN b.eFmFmEmployeeTravelRequest r JOIN r.efmFmUserMaster u where  u.userId='"
                        + userId + "'  AND c.branchId in ("+ branchId + ") AND d.tripStatus!='completed' ");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllocatedTripDetails(int userId, String branchId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN b.eFmFmEmployeeTravelRequest r JOIN r.efmFmUserMaster u where  u.userId='"
                        + userId + "'  AND c.branchId in ("+ branchId + ") AND d.bucketStatus='Y' AND d.tripStatus!='completed' ");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllocatedEmployeeDetailYetToBoard(int userId, int branchId, Date todayDate) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN b.eFmFmEmployeeTravelRequest r JOIN r.efmFmUserMaster u where  u.userId='"
                        + userId + "'  AND c.branchId='" + branchId + "' AND d.tripStatus!='completed' AND b.boardedFlg='N'");
        tripemployees = query.getResultList();
        return tripemployees;
    }

    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getEmployeeLiveTripDetailFromUserIdBeforeOnBoard(int userId, int branchId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN b.eFmFmEmployeeTravelRequest r JOIN r.efmFmUserMaster u where  u.userId='"
                        + userId + "'  AND c.branchId='" + branchId + "' AND d.tripStatus='Started' AND b.boardedFlg='N'");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllocatedEmployeeDetailFromUserIdAndBranchId(int userId, int branchId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN b.eFmFmEmployeeTravelRequest r JOIN r.efmFmUserMaster u where  u.userId='"
                        + userId + "'  AND c.branchId='" + branchId
                        + "' AND d.tripStatus!='completed' AND b.boardedFlg='N'");
        tripemployees = query.getResultList();
        return tripemployees;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllTodayTripDetails(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {
        List<EFmFmEmployeeTripDetailPO> travelRequestDetails = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Format formatter;
        String todaysDate = "";
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        todaysDate = formatter.format(new Date());
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmEmployeeRequestMaster r JOIN t.efmFmUserMaster d JOIN d.eFmFmClientBranchPO c where DATE(b.actualTime)='"
                        + todaysDate + "' AND c.branchId='"
                        + eFmFmEmployeeTravelRequest.getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId() + "'");
        travelRequestDetails = query.getResultList();
        return travelRequestDetails;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getParticularTriprEmployeeBoardedStatus(int requestId, int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute a JOIN b.eFmFmEmployeeTravelRequest r where a.assignRouteId='"
                        + assignRouteId + "' AND r.requestId='" + requestId + "'");
        tripemployees = query.getResultList();
        return tripemployees;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getParticularTripEmployeeRequestDetails(int empTripId, int requestId,
            int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute a JOIN b.eFmFmEmployeeTravelRequest r where a.assignRouteId='"
                        + assignRouteId + "' AND b.empTripId='" + empTripId + "' AND r.requestId='" + requestId + "'");
        tripemployees = query.getResultList();
        return tripemployees;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getrequestStatusFromBranchIdAndRequestId(int branchId, int requestId) {
        List<EFmFmEmployeeTripDetailPO> travelRequestDetails = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmEmployeeRequestMaster r JOIN t.efmFmUserMaster d JOIN d.eFmFmClientBranchPO c where c.branchId='"
                        + branchId + "' AND t.requestId='" + requestId + "'");
        travelRequestDetails = query.getResultList();
        return travelRequestDetails;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getAllRequestDetailsFromRequestMasterFromBranchId(int branchId) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
                        + branchId + "' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getActiveRequestDetailsFromBranchIdAndUserId(int branchId, int userId) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c  where c.branchId='"
                        + branchId + "' and u.userId='" + userId + "' ORDER BY  b.tripRequestStartDate ASC");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getActiveRequestDetailsFromBranchIdAndUserIdForAnEmployee(String branchId, int userId) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.status='Y' AND b.readFlg='Y' AND u.userId='" + userId + "' ORDER BY  b.tripRequestStartDate ASC");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getAllRequestDetailsFromRequestMasterFromBranchIdByTripType(int branchId,
            String tripType) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
                        + branchId + "' AND b.tripType='" + tripType + "'ORDER BY  b.tripRequestStartDate ASC")
                .setMaxResults(100);
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getAllActiveRequestDetailsFromRequestMasterdByBranchIdAndTripType(String branchId,String tripType) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.status='Y' AND b.readFlg='Y' AND b.tripType='" + tripType + "'ORDER BY  b.tripRequestStartDate ASC")
                .setMaxResults(100);
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }


    // All allocation methods
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getParticularEmployeeMasterRequestDetails(int branchId, int tripId) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequest = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.eFmFmClientBranchPO  c where b.tripId='"
                        + tripId + "' AND  c.branchId='" + branchId + "' ");
        employeeRequest = query.getResultList();
        return employeeRequest;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllParticularRouteRequest(int branchId, int zoneId, Time shiftTime) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm where c.branchId='"
                        + branchId + "' AND  vm.zoneId='" + zoneId + "' AND b.requestDate='"
                        + formatter.format(new Date())
                        + "' AND b.isActive='Y' AND b.approveStatus='Y' AND b.readFlg='Y' AND b.shiftTime='" + shiftTime
                        + "' AND (b.requestStatus!='C' OR b.requestStatus!='R')");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllParticularRouteRequestByDate(String requestDate, int branchId,
            int zoneId, Time shiftTime) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm where c.branchId='"
                        + branchId + "' AND DATE(b.requestDate)='" + requestDate + "' AND  vm.zoneId='" + zoneId
                        + "' AND b.isActive='Y' AND b.approveStatus='Y' AND b.readFlg='Y' AND b.shiftTime='" + shiftTime
                        + "' AND (b.requestStatus!='C' OR b.requestStatus!='R')");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleLessCapacity(String branchId, int capacity) {
        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where d.capacity<='"
                        + capacity + "' and d.vehicleNumber like '%DUMMY%' and b.status='Y' "
                        		+ " and g.branchId in (" + branchId+ ") ");
        vehicleCheckIn = query.getResultList();
        return vehicleCheckIn;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehiclesForSpecificCapacity(String branchId, int capacity) {
        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where d.capacity='"
                        + capacity + "' and d.vehicleNumber like '%DUMMY%' and b.status='Y' "
                        		+ " and g.branchId in ("+branchId+") ").setMaxResults(1);
        vehicleCheckIn = query.getResultList();
        return vehicleCheckIn;

    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EFmFmVehicleCheckInPO getCheckedInSingleVehicleForSpecificCapacity(String branchId, int capacity) {
		List<EFmFmVehicleCheckInPO> vehicleDetail = new ArrayList<EFmFmVehicleCheckInPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where d.capacity='"
                        + capacity + "' and d.vehicleNumber like '%DUMMY%' and b.status='Y' "
                        		+ " and g.branchId in ("+branchId+") ");
        try {
			vehicleDetail = (List<EFmFmVehicleCheckInPO>) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		if (vehicleDetail.isEmpty()) {
			return null;
		} 
		return vehicleDetail.get(0);

        }

   

    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EFmFmVehicleCheckInPO getCheckedInSingleVehicleForSpecifiLessCapacity(String branchId, int capacity) {
		List<EFmFmVehicleCheckInPO> vehicleDetail = new ArrayList<EFmFmVehicleCheckInPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where d.capacity<"
                        + capacity + " and d.vehicleNumber like '%DUMMY%' and b.status='Y' "
                        		+ " and g.branchId in (" + branchId+ ") ");
        try {
			vehicleDetail = (List<EFmFmVehicleCheckInPO>) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		if (vehicleDetail.isEmpty()) {
			return null;
		} 
		return vehicleDetail.get(0);

        }

   
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EFmFmVehicleCheckInPO getCheckedInSingleVehicleForSpecificGreatestCapacity(String branchId, int capacity) {
		List<EFmFmVehicleCheckInPO> vehicleDetail = new ArrayList<EFmFmVehicleCheckInPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where  d.vehicleNumber like '%DUMMY%' and b.status='Y' "
                        		+ " and g.branchId in ("+branchId+") AND d.capacity >'" + capacity + "' ORDER BY d.capacity ASC");
        try {
			vehicleDetail = (List<EFmFmVehicleCheckInPO>) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		if (vehicleDetail.isEmpty()) {
			return null;
		} 
		return vehicleDetail.get(0);

        }

    
    
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleLargeCapacity(String branchId, int capacity) {
        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where d.capacity>='"
                        + capacity + "'  and d.vehicleNumber like '%DUMMY%' "
                        		+ " and g.branchId in ("+branchId+ ") and b.status='Y'");
        vehicleCheckIn = query.getResultList();
        return vehicleCheckIn;

    }
    
    
    
    
    
    @Override  
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmVehicleCheckInPO> getAllCheckedInDummyVehicles(String branchId) {
        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmVehicleCheckInPO b  where b.checkInType='Dummy' and b.status='N'");
        vehicleCheckIn = query.getResultList();
        return vehicleCheckIn;

    }
    

    @Override
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

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteFromCheckInIdByDate(String tripAssignDate, int branchId,
            int zoneId, String reqType, Time shiftTime, int checkInId) {
        List<EFmFmAssignRoutePO> assignVehicleDetail = new ArrayList<EFmFmAssignRoutePO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn vc where b.vehicleStatus='A' AND b.tripStatus='allocated' AND DATE(b.tripAssignDate)='"
                        + tripAssignDate + "' AND  c.branchId ='"+branchId+"' AND vm.zoneId='" + zoneId
                        + "' AND b.tripType='" + reqType + "' AND b.shiftTime='" + shiftTime + "'  AND vc.checkInId='"
                        + checkInId + "'");
        assignVehicleDetail = query.getResultList();
        return assignVehicleDetail;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteForAutoRouting(String tripAssignDate, int branchId, String reqType, Time shiftTime, int checkInId) {
        List<EFmFmAssignRoutePO> assignVehicleDetail = new ArrayList<EFmFmAssignRoutePO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn vc where b.vehicleStatus='A' AND b.tripStatus='allocated' AND DATE(b.tripAssignDate)='"
                        + tripAssignDate + "' AND  c.branchId ='"+branchId+"' AND b.tripType='" + reqType + "' AND b.shiftTime='" + shiftTime + "'  AND vc.checkInId='"
                        + checkInId + "'");
        assignVehicleDetail = query.getResultList();
        return assignVehicleDetail;
    }
    
    

    @Override
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

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteByDate(String tripAssignDate, int branchId, int zoneId,
            String reqType, Time shiftTime) {
        List<EFmFmAssignRoutePO> assignVehicleDetail = new ArrayList<EFmFmAssignRoutePO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn vc  JOIN vc.efmFmVehicleMaster vem where b.vehicleStatus='A' AND b.tripStatus='allocated' AND DATE(b.tripAssignDate)='"
                        + tripAssignDate + "' AND c.branchId ='" + branchId + "' AND vm.zoneId='" + zoneId
                        + "' AND b.tripType='" + reqType + "' AND b.shiftTime='" + shiftTime
                        + "'  ORDER BY vem.availableSeat DESC ");
        assignVehicleDetail = query.getResultList();
        return assignVehicleDetail;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEscortCheckInPO> getAllCheckedInEscort(String branchId) {
        List<EFmFmEscortCheckInPO> escortMasterPO = new ArrayList<EFmFmEscortCheckInPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEscortCheckInPO b JOIN b.eFmFmEscortMaster e JOIN e.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.status='Y' AND b.escortCheckOutTime is null "
                + "AND  c.branchId in ("+branchId+")");
        escortMasterPO = query.getResultList();
        return escortMasterPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmVehicleMasterPO> getVehicleDetailsFromClientIdForSmallerRoute(int branchId, String vehicleId) {
        List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster d JOIN d.eFmFmClientBranchPO c where c.branchId='"
                        + branchId + "' AND b.status='A' AND b.vehicleId in (" + vehicleId + ") ORDER BY capacity ASC");
        vehicleMasterPO = query.getResultList();
        return vehicleMasterPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getParticularTripAllEmployees(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
                        + assignRouteId + "' ORDER BY t.pickUpTime ASC");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getRouteEmployeeStatus(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
                        + assignRouteId + "' AND b.boardedFlg ='N' ");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllWithOutNoShowDropTripEmployees(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
                        + assignRouteId + "' AND b.boardedFlg ='N' ORDER BY t.dropSequence ASC");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    


 @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllWithOutNoShowPickupTripEmployees(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
                        + assignRouteId + "'  AND b.boardedFlg ='N' ORDER BY t.pickUpTime ASC");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getCoPassenger(int assignRouteId, int requestId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
                        + assignRouteId + "' AND t.requestId !='" + requestId + "' ORDER BY t.pickUpTime ASC");
        tripemployees = query.getResultList();
        return tripemployees;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getTripAllEmployeesFromAssignRouteIdBranchId(int assignRouteId,
            int branchId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c where d.assignRouteId='"
                        + assignRouteId + "' AND c.branchId='" + branchId + "' ");
        tripemployees = query.getResultList();
        return tripemployees;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getRequestStatusFromBranchIdAndRequestId(String branchId, int requestId) {
        List<EFmFmEmployeeTripDetailPO> travelRequestDetails = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmEmployeeRequestMaster r JOIN t.efmFmUserMaster d JOIN d.eFmFmClientBranchPO c "
                + " where  c.branchId in ("+branchId+") AND t.requestId='" + requestId + "'");
        travelRequestDetails = query.getResultList();
        return travelRequestDetails;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteParticularRequestFromEmployeeTripDetail(int empTripId) {
        Query query = entityManager
                .createQuery("DELETE EFmFmEmployeeTripDetailPO where empTripId = '" + empTripId + "' ");
        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EFmFmVehicleMasterPO getVehicleDetail(int vehicleId) {
        List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmVehicleMasterPO as b  where b.vehicleId='" + vehicleId + "'");
        vehicleDetail = query.getResultList();
        return vehicleDetail.get(0);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActivePickUpRequestCounter(int branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId='"
                        + branchId
                        + "' AND b.isActive='Y' AND b.tripType='PICKUP' AND b.completionStatus='N' AND b.requestType!='guest' AND b.readFlg='Y' ");
        long numberOfPickUpRequest = (long) query.getSingleResult();
        return numberOfPickUpRequest;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActiveDropRequestCounter(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y' AND b.tripType='DROP' AND b.completionStatus='N' AND b.requestType!='guest' AND b.readFlg='Y' ");
        long numberOfDropRequest = (long) query.getSingleResult();
        return numberOfDropRequest;

    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActivePickUpRequestCounterForTodays(String branchId,String todayDate) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y' AND b.tripType='PICKUP' AND DATE(b.requestDate)='" + todayDate + "'  AND b.completionStatus='N' AND b.requestType!='guest' AND b.readFlg='Y' ");
        long numberOfPickUpRequest = (long) query.getSingleResult();
        return numberOfPickUpRequest;

    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActiveDropRequestCounterForTodays(String branchId,String todayDate) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y' AND b.tripType='DROP' AND DATE(b.requestDate)='" + todayDate + "'  AND b.completionStatus='N' AND b.requestType!='guest' AND b.readFlg='Y' ");
        long numberOfDropRequest = (long) query.getSingleResult();
        return numberOfDropRequest;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActiveDropOrPickupRequestCounterForGuest(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y'  AND b.completionStatus='N' AND b.requestType='guest' AND (b.readFlg='Y' OR b.readFlg='R')");
        long numberOfDropRequest = (long) query.getSingleResult();
        return numberOfDropRequest;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActiveBoardedEmployeeRequestCounter(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND b.boardedFlg='B' AND a.tripStatus!='completed' AND a.tripType='PICKUP' AND t.requestType!='guest'");
        long boardedEmployee = (long) query.getSingleResult();
        return boardedEmployee;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActiveBoardedGuestRequestCounter(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND (b.boardedFlg='B' OR b.boardedFlg='D') AND a.tripStatus!='completed' AND t.requestType='guest' ");
        long boardedEmployee = (long) query.getSingleResult();
        return boardedEmployee;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActiveNoShowEmployeeRequestCounter(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND b.boardedFlg='NO' AND a.tripStatus!='completed' AND a.tripType='PICKUP' AND t.requestType!='guest'");
        long noShowEmployee = (long) query.getSingleResult();
        return noShowEmployee;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActiveNoShowGuestRequestCounter(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND b.boardedFlg='NO' AND a.tripStatus!='completed' AND t.requestType='guest'");
        long noShowEmployee = (long) query.getSingleResult();
        return noShowEmployee;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActivePickupInProgressEmployeeRequestCounter(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND b.boardedFlg='N' AND a.tripStatus!='completed' AND a.tripType='PICKUP' AND t.requestType!='guest'");
        long pickUpInProgress = (long) query.getSingleResult();
        return pickUpInProgress;

    }

    // Drop employee list
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActiveDropedEmployeeRequestCounter(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND b.boardedFlg='D' AND a.tripStatus!='completed' AND a.tripType='DROP' AND t.requestType!='guest'");
        long droppedEmployee = (long) query.getSingleResult();
        return droppedEmployee;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActiveDropNoShowEmployeeRequestCounter(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND b.boardedFlg='NO' AND a.tripStatus!='completed' AND a.tripType='DROP' AND t.requestType!='guest'");
        long noShowDrop = (long) query.getSingleResult();
        return noShowDrop;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllActiveDropInProgressEmployeeRequestCounter(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND b.boardedFlg='N' AND a.tripStatus!='completed' AND a.tripType='DROP' AND t.requestType!='guest'");
        long dropUpInProgress = (long) query.getSingleResult();
        return dropUpInProgress;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllDropScheduleActiveRequests(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y' AND b.tripType='DROP' AND b.readFlg='R' AND b.completionStatus='N' AND b.requestType!='guest'");
        long dropScheduled = (long) query.getSingleResult();
        return dropScheduled;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllPickupScheduleActiveRequests(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y' AND b.tripType='PICKUP' AND b.readFlg='R' AND b.completionStatus='N' AND b.requestType!='guest' ");
        long dropScheduled = (long) query.getSingleResult();
        return dropScheduled;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllScheduleActiveRequestsForGuest(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y' AND b.readFlg='R' AND b.completionStatus='N' AND b.requestType='guest' ");
        long dropScheduled = (long) query.getSingleResult();
        return dropScheduled;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> particularEmployeeRequestFromEmpId(String branchId, String employeeId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND u.employeeId='" + employeeId
                        + "' AND b.approveStatus='Y' AND b.readFlg='Y'  ORDER BY b.requestDate,pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> particularEmployeePickupRequestFromUserId(int branchId, int userId,
            String tripType) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where c.branchId='"
                        + branchId + "' AND u.userId='" + userId
                        + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND b.tripType='" + tripType
                        + "' ORDER BY pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    // Drop template detail quiries
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllActiveDropRequestDetails(int branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId='"
                        + branchId
                        + "' AND b.isActive='Y' AND b.tripType='DROP' AND b.completionStatus='N' AND b.requestType!='guest' AND b.readFlg='Y'  ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllActiveGuestRequestsDetails(String branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y'  AND b.completionStatus='N' AND b.requestType='guest' AND (b.readFlg='Y' OR b.readFlg='R') ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllDropScheduleActiveRequestsDetails(String branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y' AND b.tripType='DROP' AND b.readFlg='R' AND b.completionStatus='N' AND b.requestType!='guest'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllScheduleActiveGuestRequestsDetails(String branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y'  AND b.readFlg='R' AND b.completionStatus='N' AND b.requestType='guest'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllActiveDropedEmployeeRequestsDetails(String branchId) {
        List<EFmFmEmployeeTripDetailPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND b.boardedFlg='D' AND a.tripStatus!='completed' AND a.tripType='DROP' AND t.requestType!='guest'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllActiveDropNoShowEmployeeRequestsDetails(String branchId) {
        List<EFmFmEmployeeTripDetailPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND b.boardedFlg='NO' AND a.tripStatus!='completed' AND a.tripType='DROP' AND t.requestType!='guest'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    // Pickup template detail quiries

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllActivePickUpRequestDetails(int branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId='"
                        + branchId
                        + "' AND b.isActive='Y' AND b.tripType='PICKUP' AND b.completionStatus='N' AND b.requestType!='guest' AND b.readFlg='Y'  ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;

    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllActivePickUpRequestDetailsForToday(String branchId,String requestDate) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y' AND b.tripType='PICKUP'  AND DATE(b.requestDate)='" + requestDate + "' AND b.completionStatus='N' AND b.requestType!='guest' AND b.readFlg='Y'  ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;

    }
    
    // Drop template detail quiries
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllActiveDropRequestDetailsForToday(String branchId,String requestDate) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y' AND b.tripType='DROP' AND DATE(b.requestDate)='" + requestDate + "' AND b.completionStatus='N' AND b.requestType!='guest' AND b.readFlg='Y'  ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;

    }
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getAllPickupScheduleActiveRequestsDetails(String branchId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.isActive='Y' AND b.tripType='PICKUP' AND b.readFlg='R' AND b.completionStatus='N' AND b.requestType!='guest'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllActivePickupBoardedEmployeeRequestsDetails(String branchId) {
        List<EFmFmEmployeeTripDetailPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND b.boardedFlg='B' AND a.tripStatus!='completed' AND a.tripType='PICKUP' AND t.requestType!='guest'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllActiveBoardedOrDroppedEmployeeRequestsDetailsForGuest(String branchId) {
        List<EFmFmEmployeeTripDetailPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND (b.boardedFlg='B' OR b.boardedFlg='D') AND a.tripStatus!='completed'  AND t.requestType='guest'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllActivePickupNoShowEmployeeRequestsDetails(String branchId) {
        List<EFmFmEmployeeTripDetailPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND b.boardedFlg='NO' AND a.tripStatus!='completed' AND a.tripType='PICKUP' AND t.requestType!='guest'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getAllActiveGuestNoShowRequestsDetails(String branchId) {
        List<EFmFmEmployeeTripDetailPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.eFmFmClientBranchPO c JOIN b.efmFmAssignRoute a where c.branchId in ("+ branchId + ") AND b.boardedFlg='NO' AND a.tripStatus!='completed' AND t.requestType='guest'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequestByShiftTripType(int branchId, Time shiftTime,
            String tripType, String todaysDate) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId='"
                        + branchId + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='"
                        + todaysDate + "' AND b.shiftTime='" + shiftTime
                        + "' AND u.latitudeLongitude is not null  AND b.tripType='" + tripType
                        + "' ORDER BY b.requestDate,z.zoneName,pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmClientBranchPO> getBranchDetails(int branchId) {
        List<EFmFmClientBranchPO> eFmFmClientBranchPO = new ArrayList<EFmFmClientBranchPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmClientBranchPO b where b.branchId='" + branchId + "' and b.status='Y'");
        eFmFmClientBranchPO = query.getResultList();
        return eFmFmClientBranchPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getparticularRequestwithShiftTime(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery("SELECT b FROM EFmFmEmployeeTravelRequestPO b where b.requestId='"+ eFmFmEmployeeTravelRequestPO.getRequestId() + "'  ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAssignRoutePO> getCreatedAssignRoute(int branchId, String reqType, Time shiftTime) {
        List<EFmFmAssignRoutePO> assignVehicleDetail = new ArrayList<EFmFmAssignRoutePO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c where b.vehicleStatus='A' AND b.tripStatus='allocated' AND c.branchId ='"
                        + branchId + "' AND b.tripType='" + reqType + "' AND b.shiftTime='" + shiftTime
                        + "'  ORDER BY b.assignRouteId DESC ");
        assignVehicleDetail = query.getResultList();
        return assignVehicleDetail;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getDropTripByAlgoRouteSortedEmployees(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN t.efmFmUserMaster u JOIN b.efmFmAssignRoute d where d.assignRouteId='"
                        + assignRouteId + "'");
        tripemployees = query.getResultList();
        return tripemployees;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteParticularTripDetailByRouteId(int routeId) {
        List<EFmFmEmployeeTripDetailPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute a  where a.assignRouteId ='"
                        + routeId + "'");
        employeeTravelRequestPO = query.getResultList();
        for (EFmFmEmployeeTripDetailPO delete : employeeTravelRequestPO) {
            entityManager.remove(delete);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getparticularEmployeeRequest(String todaysDate, String employeeId,
            String tripType, int branchId, String shiftTime) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where u.employeeId='"
                        + employeeId + "' AND c.branchId='" + branchId + "' AND b.shiftTime='" + shiftTime
                        + "' AND b.tripType='" + tripType + "' AND DATE(b.requestDate)='" + todaysDate
                        + "' order by b.requestId desc");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EFmFmEmployeeTripDetailPO ParticularTripDetail(int empTripId) {
        List<EFmFmEmployeeTripDetailPO> eFmFmEmployeeTripDetailPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager
                .createQuery("SELECT b EFmFmEmployeeTripDetailPO b where b.empTripId = '" + empTripId + "' ");
        eFmFmEmployeeTripDetailPO = query.getResultList();
        return eFmFmEmployeeTripDetailPO.get(0);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getparticularEmployeeTripDetails(String todaysDate, String employeeId,
            String tripType, int branchId, String shiftTime) {
        List<EFmFmEmployeeTripDetailPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT r FROM EFmFmEmployeeTripDetailPO r JOIN r.eFmFmEmployeeTravelRequest b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where u.employeeId='"
                        + employeeId + "' AND c.branchId='" + branchId + "' AND b.shiftTime='" + shiftTime
                        + "' AND b.tripType='" + tripType + "' AND DATE(b.requestDate)='" + todaysDate + "'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getParticularRequestDetailFromUserIdAndTripType(int userId, int branchId,
            String tripType) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c  where u.userId='"
                        + userId + "'  AND c.branchId='" + branchId + "' AND b.tripType='" + tripType + "' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripTimingMasterPO> getParticularShiftTimeDetailByShiftId(int branchId,int shiftId ) {
        List<EFmFmTripTimingMasterPO> shiftTimeDetail = new ArrayList<EFmFmTripTimingMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmTripTimingMasterPO b "
                		+ " where b.shiftId='" + shiftId + "' AND b.isActive='Y' ");
        shiftTimeDetail = query.getResultList();
        return shiftTimeDetail;
	}
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripTimingMasterPO> getCeilingShiftTimeDetailByShiftId(int branchId,int shiftId ) {
        List<EFmFmTripTimingMasterPO> shiftTimeDetail = new ArrayList<EFmFmTripTimingMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmTripTimingMasterPO b JOIN b.eFmFmClientBranchPO  c where b.shiftId='" + shiftId + "' AND b.ceilingFlg='Y' AND b.isActive='Y' ");
        shiftTimeDetail = query.getResultList();
        return shiftTimeDetail;
	}
    
    @Override
    public List<EFmFmEmployeeTravelRequestPO> getNextWeekRequestForParticularEmployee(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {
        List<EFmFmEmployeeTravelRequestPO> todayrequests = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Format formatter= new SimpleDateFormat("yyyy-MM-dd");       
        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE,10);         
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
                + " where c.branchId='"+ eFmFmEmployeeTravelRequest.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId()+ "' "
                  + " AND u.userId='" + eFmFmEmployeeTravelRequest.getEfmFmUserMaster().getUserId()+ "' "
                    + " AND b.readFlg='Y' AND DATE(b.requestDate) >='"+formatter.format(new Date())+"' AND DATE(b.requestDate) <='"+formatter.format(cal.getTime())+"' ORDER BY b.requestDate ASC ");
        todayrequests = query.getResultList();
        return todayrequests;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripTimingMasterPO> getShiftIdUsingshiftTime(int branchId, String tripType, Time siftTime) {          
        List<EFmFmTripTimingMasterPO> eFmFmTripTimingMasterPO = new ArrayList<EFmFmTripTimingMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmTripTimingMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId='"
                        + branchId + "' and b.tripType='" + tripType + "' and b.shiftTime='" + siftTime
                        + "' and  b.isActive='Y' ");
        eFmFmTripTimingMasterPO = query.getResultList();
        return eFmFmTripTimingMasterPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteByRouteName(String tripAssignDate, int branchId, int zoneId,
            String reqType, Time shiftTime,String routeName) {
        List<EFmFmAssignRoutePO> assignVehicleDetail = new ArrayList<EFmFmAssignRoutePO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn vc  JOIN vc.efmFmVehicleMaster vem where b.vehicleStatus='A' AND b.tripStatus='allocated' AND DATE(b.tripAssignDate)='"
                        + tripAssignDate + "' AND c.branchId ='" + branchId + "' AND vm.zoneId='" + zoneId
                        + "' AND b.tripType='" + reqType + "' and b.routeName='"+routeName+"' AND b.shiftTime='" + shiftTime
                        + "'  ORDER BY vem.availableSeat DESC ");
        assignVehicleDetail = query.getResultList();
        return assignVehicleDetail;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getParticularTripAllEmployeesDesc(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
                        + assignRouteId + "' ORDER BY t.pickUpTime DESC");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTripDetailPO> getParticularTripEmployees(int assignRouteId) {
        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.efmFmAssignRoute d where d.assignRouteId='"
                        + assignRouteId + "' ORDER BY b.empTripId DESC");
        tripemployees = query.getResultList();
        return tripemployees;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestValidation(Date date,Date toDate,int branchId, int userId, String tripType) {
        List<EFmFmEmployeeTravelRequestPO> employeeRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        String todayDate;
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = formatter.format(date);
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u where u.userId='"
                        + userId + "' and DATE(b.requestDate)>='"+todayDate+"' and DATE(b.requestDate)<='"+ formatter.format(toDate)+"' "
                        		+ " and  b.tripType='" + tripType+ "' AND b.readFlg='Y' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getEmployeeRequestDetails(String branchId,int UserId) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u JOIN b.eFmFmClientBranchPO c  "
                + " where c.branchId in ("+branchId+") AND b.status='Y' AND b.readFlg='Y' AND u.userId='" + UserId + "'ORDER BY  b.tripRequestStartDate ASC");                
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
   /* @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getEmployeeRequestCeiling(int branchId,String requestDate,Time shiftTime,String tripType) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery( 	
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId='"
                        + branchId + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' AND b.shiftTime='" + shiftTime
                        + "' AND b.requestType!='guest' AND b.requestType!='AdhocRequest'  "
                        + "	 AND b.requestType !='nodalAdhoc' AND b.tripType='"+tripType+"'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }*/
  
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> getEmployeeRequestCeilingCount(int branchId,String requestDate,String shiftTime,String tripType) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery( 	
                " SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r  "
                + " JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
                + " where c.branchId='"+ branchId + "' AND b.approveStatus='Y' AND b.readFlg='Y' "
                + " AND DATE(b.requestDate)='" + requestDate + "' AND b.shiftTime='" + shiftTime + "' "
                		+ " AND b.requestType!='guest' AND b.requestType!='AdhocRequest'  "
                + "	 AND b.requestType !='nodalAdhoc' AND b.tripType='"+tripType+"' ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getEmployeeRequestCeiling(int branchId,String requestDate,String shiftTime,String tripType) {
        List<EFmFmEmployeeRequestMasterPO> eFmFmEmployeeRequestMaster = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery( 	
                "  SELECT f FROM EFmFmEmployeeRequestMasterPO f WHERE f.tripId not in ( SELECT r.tripId FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r  JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
                + " where c.branchId='"+ branchId + "' AND b.approveStatus='Y' AND b.readFlg='Y' "
                + " AND DATE(b.requestDate)='" + requestDate + "' AND b.shiftTime='" + shiftTime + "' "
                + " AND b.requestType!='guest' AND b.requestType!='AdhocRequest'  "
                + "	 AND b.requestType !='nodalAdhoc' AND b.tripType='"+tripType+"' ) AND f.tripType='"+tripType+"' AND f.status='Y' AND f.shiftTime='" + shiftTime + "'");
        eFmFmEmployeeRequestMaster = query.getResultList();
        return eFmFmEmployeeRequestMaster;
    }
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getParticularRequestDetailFromUserIdByRead(int userId, int branchId,String tripType) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u   where u.userId='"
                        + userId + "'  AND b.tripType='" + tripType + "' AND b.status='Y'");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> pastRequestByTravelMaster(int tripId) {
        List<EFmFmEmployeeTravelRequestPO> employeeRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();    
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster f "
                + " where b.readFlg='N' AND f.tripId='"+tripId+"' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
   
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeRequestMasterPO> getRequestMasterDetailsByTripId(int tripId) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequest = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b where b.tripId='"
                        + tripId + "' ");
        employeeRequest = query.getResultList();
        return employeeRequest;
    }
    
    //Employee Todays Trip Service based on UserId and trip type
    @Override
    public List<EFmFmEmployeeTravelRequestPO> getParticularActiveRequestDetail(int userId,String tripType,String requestDate,Time shiftTime) {
        List<EFmFmEmployeeTravelRequestPO> todayrequests = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u where u.userId='" + userId
                        + "' AND b.readFlg='Y' and b.shiftTime='" + shiftTime + "' and b.tripType='"+tripType+"' AND DATE(b.requestDate)='" + requestDate + "'");
        todayrequests = query.getResultList();
        return todayrequests;
    }
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> employeeTravelledRequestValidation(Date date,Date toDate,int branchId, int userId, String tripType) {
        List<EFmFmEmployeeTravelRequestPO> employeeRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        String todayDate;
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = formatter.format(date);
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where u.userId='"
                        + userId + "' and DATE(b.requestDate)>='"+todayDate+"' and DATE(b.requestDate)<='"+ formatter.format(toDate)+"' "
                        		+ " and  b.tripType='" + tripType+ "' AND b.readFlg='R' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> assignCabByLocationFlg(String requestDate,
            String branchId, String tripType, Time siftTime) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c  "
                + " where  c.branchId in ("+branchId+") and DATE(b.requestDate)='" + requestDate + "' and b.tripType='" + tripType
                        + "' and b.shiftTime='" + siftTime + "' and  b.approveStatus='Y' and b.readFlg='Y' AND b.locationFlg in ('S','M') ORDER BY b.locationFlg");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> routingByAreaSequencing(String requestDate,
            int branchId, String tripType, Time siftTime) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.efmFmAreaMaster z where c.branchId='"
                        + branchId + "' and DATE(b.requestDate)='" + requestDate + "' and b.tripType='" + tripType
                        + "' and b.shiftTime='" + siftTime + "' and  b.approveStatus='Y' and b.readFlg='Y'  ORDER BY z.areaId ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> assignCabToParticularShiftDateByLocationFlg(String requestDate,String branchId, String tripType, Time siftTime, int zoneId,String locationFlg) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        System.out.println("requestDate"+requestDate+"branchId"+branchId+"tripType"+tripType+"siftTime"+siftTime+"zoneId"+zoneId+"locationFlg"+locationFlg);

        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  where c.branchId in ("+ branchId + ") and DATE(b.requestDate)='" + requestDate + "' and b.tripType='" + tripType
                        + "' and b.shiftTime='" + siftTime + "' and z.zoneId='" + zoneId
                        + "'and b.approveStatus='Y' and b.readFlg='Y' AND b.locationFlg='"+locationFlg+"' ORDER BY b.locationFlg,c.branchId ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long assignCabCountToParticularShiftDateByLocationFlg(String requestDate,String branchId, String tripType, Time siftTime, int zoneId,String locationFlg) {
        System.out.println("requestDate"+requestDate+"branchId"+branchId+"tripType"+tripType+"siftTime"+siftTime+"zoneId"+zoneId+"locationFlg"+locationFlg);

        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  where c.branchId in ("+ branchId + ") and DATE(b.requestDate)='" + requestDate + "' and b.tripType='" + tripType
                        + "' and b.shiftTime='" + siftTime + "' and z.zoneId='" + zoneId
                        + "'and b.approveStatus='Y' and b.readFlg='Y' AND b.locationFlg='"+locationFlg+"' ORDER BY b.locationFlg,c.branchId ASC");        
        long requestDetail =(long) query.getSingleResult();
        return requestDetail;
    }
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long assignCabCountToParticularShiftDateOrRouteEmployees(String requestDate,
            String branchId, String tripType, Time siftTime, int zoneId) {

        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmEmployeeTravelRequestPO b  JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  "
                + " where c.branchId in ("+branchId+") and DATE(b.requestDate)='" + requestDate + "' and b.tripType='" + tripType
                        + "' and b.shiftTime='" + siftTime + "' and z.zoneId='" + zoneId
                        + "'and b.approveStatus='Y' and b.readFlg='Y' ORDER BY c.branchId ASC");
        long requestDetail =(long) query.getSingleResult();
        return requestDetail;
    }
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteByLocationFlg(String tripAssignDate, int branchId, int zoneId,
            String reqType, Time shiftTime,String locationFlg) {
        List<EFmFmAssignRoutePO> assignVehicleDetail = new ArrayList<EFmFmAssignRoutePO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn vc  JOIN vc.efmFmVehicleMaster vem where b.vehicleStatus='A' AND b.tripStatus='allocated' AND DATE(b.tripAssignDate)='"
                        + tripAssignDate + "' AND b.locationFlg='"+locationFlg+"' AND  c.branchId ='"+ branchId + "' AND vm.zoneId='" + zoneId
                        + "' AND b.tripType='" + reqType + "' AND b.shiftTime='" + shiftTime
                        + "'  ORDER BY vem.availableSeat DESC ");
        assignVehicleDetail = query.getResultList();
        return assignVehicleDetail;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteByDateWithNormalFlg(String tripAssignDate, int branchId, int zoneId,
            String reqType, Time shiftTime) {
        List<EFmFmAssignRoutePO> assignVehicleDetail = new ArrayList<EFmFmAssignRoutePO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmRouteAreaMapping v  JOIN v.eFmFmZoneMaster vm JOIN b.eFmFmClientBranchPO c JOIN b.efmFmVehicleCheckIn vc  JOIN vc.efmFmVehicleMaster vem where b.vehicleStatus='A' AND b.tripStatus='allocated' AND DATE(b.tripAssignDate)='"
                        + tripAssignDate + "' AND b.locationFlg='N' AND  c.branchId ='"+branchId+"' AND vm.zoneId='" + zoneId
                        + "' AND b.tripType='" + reqType + "' AND b.shiftTime='" + shiftTime
                        + "'  ORDER BY vem.availableSeat DESC ");
        assignVehicleDetail = query.getResultList();
        return assignVehicleDetail;
    }

	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void save(EFmFmMultipleLocationTvlReqPO eFmFmMultipleLocationTvlReqPO) {
		entityManager.persist(eFmFmMultipleLocationTvlReqPO);
		
	}

	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void modify(EFmFmMultipleLocationTvlReqPO eFmFmMultipleLocationTvlReqPO) {
		entityManager.merge(eFmFmMultipleLocationTvlReqPO);
		
	}
	
	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public List<EFmFmMultipleLocationTvlReqPO> getLocationTravelDetails(int locTvlId, int branchId) {
		 List<EFmFmMultipleLocationTvlReqPO> eFmFmMultipleLocationTvlReqPO = new ArrayList<EFmFmMultipleLocationTvlReqPO>();
		    Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmMultipleLocationTvlReqPO b "
	                + " where b.multipleReqId='"+ locTvlId + "' AND b.locationStatus='Y'");
		    eFmFmMultipleLocationTvlReqPO = query.getResultList();
	        return eFmFmMultipleLocationTvlReqPO;
	}

	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public List<EFmFmMultipleLocationTvlReqPO> getLocationTravelDetailsByRequestId(int requestId) {
		List<EFmFmMultipleLocationTvlReqPO> eFmFmMultipleLocationTvlReqPO = new ArrayList<EFmFmMultipleLocationTvlReqPO>();
	    Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmMultipleLocationTvlReqPO b JOIN b.eFmFmEmployeeTravelRequest t "
                + " where t.requestId='"+requestId+"' AND b.travelledStatus in ('allocated','completed') ORDER BY b.multipleReqId");
	    eFmFmMultipleLocationTvlReqPO = query.getResultList();
        return eFmFmMultipleLocationTvlReqPO;
	}
	
	 @Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public int getRequestId(EFmFmEmployeeRequestMasterPO employeeRequestMasterPO) {
	     entityManager.persist(employeeRequestMasterPO);
	     entityManager.flush();
	     return employeeRequestMasterPO.getTripId();
	 }
	 
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTripDetailPO> getlistOfRequestByAssignedId(int assignRouteId){
	        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
	                        + assignRouteId + "'");
	        tripemployees = query.getResultList();
	        return tripemployees;
	    }
    
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> getEmpMultipleTravelledRequest(int branchId,String requestDate) {
	        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b  JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  where c.branchId='"
	                        + branchId
	                        + "' AND b.approveStatus='Y' AND DATE(b.requestDate)='" + requestDate + "' "
	                        		+ " AND b.readFlg='Y' AND  b.locationFlg='M' ORDER BY requestDate,z.zoneName,pickUpTime ASC");
	        employeeTravelRequestPO = query.getResultList();
	        return employeeTravelRequestPO;
	    }
	    
	   
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> assignCabRequestToParticularShiftOrRouteEmployeesByLocationFlg(String requestDate,String branchId,
	            String tripType, Time siftTime, int zoneId) {
	        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  "
	                + " where  c.branchId in ("+branchId+") and DATE(b.requestDate)='" + requestDate + "'  and b.tripType='" + tripType + "' and b.shiftTime='" + siftTime
	                        + "' and z.zoneId='" + zoneId + "'and b.approveStatus='Y' and b.readFlg='Y' AND b.locationFlg in ('S','M') ORDER BY b.locationFlg");
	        employeeTravelRequestPO = query.getResultList();
	        return employeeTravelRequestPO;
	    }

	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTravelRequestPO> getListOfApprovalPendingRequestForUser(String branchId, int userId,
			String approvalFlg) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND u.userId='" + userId
                        + "' AND b.approveStatus='Y' AND b.readFlg='R' AND b.reqApprovalStatus='"+approvalFlg+"'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }

	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTravelRequestPO> getListOfApprovalPendingRequest(String branchId, int approvalUserId,
			String approvalFlg) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.reportingManagerUserId='"+approvalUserId
                        + "' AND b.requestStatus NOT LIKE '%C%' AND b.reqApprovalStatus='"+approvalFlg+"'");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
	
	 
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeRequestMasterPO> getListOfBulkApprovalPendingRequest(int branchId, int approvalUserId,
			String approvalFlg) {
        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN u.eFmFmClientBranchPO c  "
                + " where b.reportingManagerUserId='"+ approvalUserId + "'  AND c.branchId='" + branchId + "' AND b.reqApprovalStatus='"+approvalFlg+"' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    } 
    
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> employeeRequestFromEmpIdByLocationFlg(String branchId, String employeeId) {
        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND u.employeeId='" + employeeId
                        + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND b.locationFlg='M'  ORDER BY b.requestDate,pickUpTime ASC");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
	 @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForLocationFlg(int branchId,String requestDate,Time shiftTime,String tripType) {
	        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId='"
	                        + branchId + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' AND b.shiftTime='" + shiftTime
	                        + "' AND b.locationFlg='M' AND b.tripType='"+tripType+"' ORDER BY requestDate,z.zoneName,pickUpTime ASC");
	        employeeTravelRequestPO = query.getResultList();
	        return employeeTravelRequestPO;
	    }
	
	 @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public void save(EFmFmIndicationMasterPO eFmFmIndicationMasterPO) {
	        entityManager.persist(eFmFmIndicationMasterPO);
	    }

	
    

	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmIndicationMasterPO> getIndicationDetailsExist(int branchId, String alertTypeRequest,
			String alertFunctionlityType, String levelType, String tiggerTime, int userId) {
        List<EFmFmIndicationMasterPO> eFmFmIndicationMaster= new ArrayList<EFmFmIndicationMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmIndicationMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
                + " where c.branchId='"+ branchId + "' AND u.userId='"+userId+ "' "
                		+ " AND b.alertTypeRequest='"+alertTypeRequest+"' AND b.alertFunctionlityType='"+alertFunctionlityType+"'"
                        + " AND b.levelType='"+levelType+"' AND b.tiggerTime='"+tiggerTime+"' "
                        + " AND b.isActive='Y");
        eFmFmIndicationMaster = query.getResultList();
        return eFmFmIndicationMaster;
    }

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmIndicationMasterPO> getAllIndicationDetails(int branchId, String isActive) {
		 List<EFmFmIndicationMasterPO> eFmFmIndicationMaster= new ArrayList<EFmFmIndicationMasterPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmIndicationMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
	                + " where c.branchId='"+ branchId + "' AND b.isActive='"+isActive+"'");
	        eFmFmIndicationMaster = query.getResultList();
	        return eFmFmIndicationMaster;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmIndicationMasterPO> getAllIndicationById(int branchId, int indicationId) {
		 List<EFmFmIndicationMasterPO> eFmFmIndicationMaster= new ArrayList<EFmFmIndicationMasterPO>();
	        Query query = entityManager.createQuery(
	                " SELECT b FROM EFmFmIndicationMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
	                + " where c.branchId='"+ branchId + "' AND b.isActive='Y'  AND b.indicationId='"+indicationId+"' ");
	        eFmFmIndicationMaster = query.getResultList();
	        return eFmFmIndicationMaster;
	}
	
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmIndicationMasterPO> getAllAlertFunctionlityType(int branchId) {
		 List<EFmFmIndicationMasterPO> eFmFmIndicationMaster= new ArrayList<EFmFmIndicationMasterPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmIndicationMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
	                + " where c.branchId='"+ branchId + "' AND b.isActive='Y' group by b.alertFunctionlityType ");
	        eFmFmIndicationMaster = query.getResultList();
	        return eFmFmIndicationMaster;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmIndicationMasterPO> getAllLevelType(int branchId, String getAllAlertFunctionlityType) {
		 List<EFmFmIndicationMasterPO> eFmFmIndicationMaster= new ArrayList<EFmFmIndicationMasterPO>();
	        Query query = entityManager.createQuery(
	                " SELECT b FROM EFmFmIndicationMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
	                + " where c.branchId='"+ branchId + "' "
	                		+ " AND b.isActive='Y' "
	                		+ " AND b.alertFunctionlityType='"+getAllAlertFunctionlityType+"' group by b.levelType");
	        eFmFmIndicationMaster = query.getResultList();
	        return eFmFmIndicationMaster;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmIndicationMasterPO> getAllAlertFunctionlityTypeByLevelType(int branchId,
			String getAllAlertFunctionlityType, String levelType) {
		 List<EFmFmIndicationMasterPO> eFmFmIndicationMaster= new ArrayList<EFmFmIndicationMasterPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmIndicationMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
	                + " where c.branchId='"+ branchId + "' AND b.levelType='"+levelType+"'  AND b.isActive='Y' "
	                		+ " AND b.alertFunctionlityType='"+getAllAlertFunctionlityType+"' ");
	        eFmFmIndicationMaster = query.getResultList();
	        return eFmFmIndicationMaster;
	}
	   @Override
	   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public void update(EFmFmIndicationMasterPO eFmFmIndicationMasterPO) {
	        entityManager.merge(eFmFmIndicationMasterPO);
	    } 
	
	
	   
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceBothPickAndDropForNormalAndAdhocByManager(String branchId,int approvalUserId,String requestDate) {
	        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.reqApprovalStatus='Y' AND b.reportingManagerUserId='"+approvalUserId+ "' AND b.approveStatus='Y' AND b.readFlg='Y' "
	                        		+ " AND DATE(b.requestDate)='" + requestDate + "' AND  b.requestType!='guest' "
	                        				+ " AND b.requestType!='AdhocRequest'  ORDER BY requestDate,z.zoneName,pickUpTime ASC");
	        employeeTravelRequestPO = query.getResultList();
	        return employeeTravelRequestPO;
	    }
	   
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhocByManager(String branchId,int approvalUserId,String requestDate,String tripType) {
	        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.reqApprovalStatus='Y' AND b.reportingManagerUserId='"+approvalUserId+ "' AND b.approveStatus='Y' AND b.readFlg='Y' "
	                        		+ " AND DATE(b.requestDate)='" + requestDate + "' AND b.tripType='"+tripType+"' AND  b.requestType!='guest' "
	                        				+ " AND b.requestType!='AdhocRequest'  ORDER BY requestDate,z.zoneName,pickUpTime ASC");
	        employeeTravelRequestPO = query.getResultList();
	        return employeeTravelRequestPO;
	    }
	   
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForNormalAndAdhocByManager(int approvalUserId,String branchId,String requestDate,Time shiftTime,String tripType) {
	        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.reqApprovalStatus='Y' AND b.reportingManagerUserId='"+approvalUserId+ "' AND b.approveStatus='Y' AND b.readFlg='Y' "
	                        		+ " AND DATE(b.requestDate)='" + requestDate + "'  AND b.shiftTime='" + shiftTime+ "' AND b.tripType='"+tripType+"' AND  b.requestType!='guest' "
	                        				+ " AND b.requestType!='AdhocRequest'  ORDER BY requestDate,z.zoneName,pickUpTime ASC");
	        employeeTravelRequestPO = query.getResultList();
	        return employeeTravelRequestPO;
	    }
	   
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeRequestMasterPO> getParticularRequestDetailFromUserIdByShiftTime(int userId, int branchId,String tripType,Time shiftTime) {
	        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c  where u.userId='"
	                        + userId + "' AND b.shiftTime='" + shiftTime+ "' AND b.tripType='" + tripType + "' AND b.status='Y'");
	        employeeRequestPO = query.getResultList();
	        return employeeRequestPO;
	    }
	   
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestValidationByGuest(Date date,Date toDate,int branchId, int userId, String tripType,Time shiftTime) {
	        List<EFmFmEmployeeTravelRequestPO> employeeRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        String todayDate;
	        Format formatter;
	        formatter = new SimpleDateFormat("yyyy-MM-dd");
	        todayDate = formatter.format(date);
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where u.userId='"
	                        + userId + "' and DATE(b.requestDate)>='"+todayDate+"' and DATE(b.requestDate)<='"+ formatter.format(toDate)+"' "
	                        		+ " and  b.tripType='" + tripType+ "' AND b.shiftTime='" + shiftTime+ "' AND b.readFlg='Y'");
	        employeeRequestPO = query.getResultList();
	        return employeeRequestPO;
	    }
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> employeeTravelledRequestValidationByGuest(Date date,Date toDate,int branchId, int userId, String tripType,Time shiftTime) {
	        List<EFmFmEmployeeTravelRequestPO> employeeRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        String todayDate;
	        Format formatter;
	        formatter = new SimpleDateFormat("yyyy-MM-dd");
	        todayDate = formatter.format(date);
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where u.userId='"
	                        + userId + "' and DATE(b.requestDate)>='"+todayDate+"' and DATE(b.requestDate)<='"+ formatter.format(toDate)+"' "
	                        		+ " and  b.tripType='" + tripType+ "' AND b.shiftTime='" + shiftTime+ "' AND b.readFlg='R'");
	        employeeRequestPO = query.getResultList();
	        return employeeRequestPO;
	    }
	   
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> getRequestByShiftWiceForNormalAndAdhocWithoutTripType(String branchId,String requestDate,Time shiftTime) {
	        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' AND b.shiftTime='" + shiftTime
	                        + "' AND b.requestType!='guest' AND b.requestType!='AdhocRequest' ORDER BY requestDate,z.zoneName,pickUpTime ASC");
	        employeeTravelRequestPO = query.getResultList();
	        return employeeTravelRequestPO;
	    }
	   
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhocByManagerWithoutTripType(String branchId,int approvalUserId,String requestDate,Time shiftTime) {
	        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.reqApprovalStatus='Y' AND b.reportingManagerUserId='"+approvalUserId+ "' AND b.approveStatus='Y' AND b.readFlg='Y' "
	                        		+ " AND DATE(b.requestDate)='" + requestDate + "' AND b.shiftTime='"+shiftTime+"' AND  b.requestType!='guest' "
	                        				+ " AND b.requestType!='AdhocRequest'  ORDER BY requestDate,z.zoneName,pickUpTime ASC");
	        employeeTravelRequestPO = query.getResultList();
	        return employeeTravelRequestPO;
	    }
	   
	   
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> tripRequestDetailsByTravelMaster(int requestId) {
	        List<EFmFmEmployeeTravelRequestPO> employeeRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();    
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b "
	                + " where b.requestId='"+requestId+"' ");
	        employeeRequestPO = query.getResultList();
	        return employeeRequestPO;
	    }
	   
	   // Method for checking number of capacity exist or not
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public Integer getBiggestVehicleTypeCapacity(String branchId) {
	        List<Integer> clientDetail = new ArrayList<Integer>();	        
	        Query query = entityManager.createQuery(
	                "SELECT b.capacity FROM EFmFmVehicleCapacityMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.status='Y' ORDER BY b.capacity DESC");
	        clientDetail = query.getResultList();
	        return clientDetail.get(0);
	    }
	    
	    // Method for checking number of capacity exist or not
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public Integer getLowestVehicleTypeCapacity(String branchId) {
	        List<Integer> clientDetail = new ArrayList<Integer>();	        
	        Query query = entityManager.createQuery(
	                "SELECT b.capacity FROM EFmFmVehicleCapacityMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.status='Y' ORDER BY b.capacity ASC");
	        clientDetail = query.getResultList();
	        return clientDetail.get(0);
	    }
	
	    // Method for checking number of capacity exist or not
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public Integer getGreatestVehicleTypeCapacity(String branchId,int capacity) {
	        List<Integer> clientDetail = new ArrayList<Integer>();	        
	        Query query = entityManager.createQuery(
	                "SELECT b.capacity FROM EFmFmVehicleCapacityMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.status='Y' AND b.capacity <='" + capacity + "' ORDER BY b.capacity ASC");
	        clientDetail = query.getResultList();
	        return clientDetail.get(0);
	    }
	    
	    // Method for checking number of capacity exist or not
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public Integer getLowestVehicleTypeCapacityLessThan(String branchId,int capacity) {
	        List<Integer> clientDetail = new ArrayList<Integer>();	        
	        Query query = entityManager.createQuery(
	                "SELECT b.capacity FROM EFmFmVehicleCapacityMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.status='Y' AND b.capacity <='" + capacity + "' ORDER BY b.capacity ASC");
	        clientDetail = query.getResultList();
	        return clientDetail.get(0);
	    }
	   
	   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTripDetailPO> getParticularTripModificationDetails(int assignRouteId) {
	        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t "
	                + " JOIN b.efmFmAssignRoute d where d.assignRouteId='"
	                        + assignRouteId + "' ORDER BY t.pickUpTime DESC");
	        tripemployees = query.getResultList();
	        return tripemployees;
	    }
	    
	    
	   
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmVehicleCheckInPO> getAllDummyCheckedInVehiclesForLessCapacity(String branchId, int capacity) {
	        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where  d.vehicleNumber like '%DUMMY%' and b.status='Y' "
	                        		+ " and g.branchId in ("+branchId+") AND d.capacity <='" + capacity + "' ORDER BY d.capacity DESC").setMaxResults(1);
	        vehicleCheckIn = query.getResultList();
	        return vehicleCheckIn;

	    }

	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmVehicleCheckInPO> getAllDummyCheckedInVehiclesForGreatestCapacity(String branchId, int capacity) {
	        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where  d.vehicleNumber like '%DUMMY%' and b.status='Y' "
	                        		+ " and g.branchId in ("+branchId+") AND d.capacity >='" + capacity + "' ORDER BY d.capacity ASC").setMaxResults(1);
	        vehicleCheckIn = query.getResultList();
	        return vehicleCheckIn;

	    }
	    
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmVehicleCheckInPO> getParticularCapacityDummyCheckedInVehicle(String branchId, int capacity) {
	        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where  d.vehicleNumber like '%DUMMY%' and g.branchId in ("+branchId+") AND d.capacity ='" + capacity + "' ");
	        vehicleCheckIn = query.getResultList();
	        return vehicleCheckIn;

	    }
	    
	    
	    
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<Integer> getAllCapacitiesOfTheVehicles(String branchId) {
	        List<Integer> clientDetail = new ArrayList<Integer>();	        
	        Query query = entityManager.createQuery(
	                "SELECT distinct(b.capacity) FROM EFmFmVehicleCapacityMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.status='Y'  ORDER BY b.capacity ASC");
	        clientDetail = query.getResultList();
	        return clientDetail;
	    }
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeRequestMasterPO> getBulkRequestByTripId(int tripId) {
	        List<EFmFmEmployeeRequestMasterPO> employeeRequestPO = new ArrayList<EFmFmEmployeeRequestMasterPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeRequestMasterPO b  where b.tripId='"+ tripId + "'  ");
	        employeeRequestPO = query.getResultList();
	        return employeeRequestPO;
	    }
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForNormalAndAdhoc(String branchId,String 
	    		requestDate,Time shiftTime,String tripType, int startPgNo, int endPgNo) {
	        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' AND b.shiftTime='" + shiftTime
	                        + "' AND b.requestType!='guest' AND b.requestType!='AdhocRequest' AND b.tripType='"+tripType+"' ORDER BY requestDate,z.zoneName,pickUpTime ASC").setFirstResult(startPgNo).setMaxResults(endPgNo); 
	        employeeTravelRequestPO = query.getResultList();
	        return employeeTravelRequestPO;
	    }
		
		
		   @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhoc(String branchId,String requestDate,String tripType, int startPgNo, int endPgNo) {
	        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c "
	                + " JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") "
	                		+ " AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' "
	                				+ " AND  b.requestType!='guest' AND b.requestType!='AdhocRequest' AND b.tripType='"+tripType+"' "
	                						+ " ORDER BY requestDate,z.zoneName,pickUpTime ASC").setFirstResult(startPgNo).setMaxResults(endPgNo);
	        employeeTravelRequestPO = query.getResultList(); 
	        return employeeTravelRequestPO;
	    }
		
		 @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceBothPickAndDropForNormalAndAdhoc(String branchId,String requestDate, int startPgNo, int endPgNo) {
	        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' AND  b.requestType!='guest' AND b.requestType!='AdhocRequest'  ORDER BY requestDate,z.zoneName,pickUpTime ASC").setFirstResult(startPgNo).setMaxResults(endPgNo); 
	        employeeTravelRequestPO = query.getResultList();
	        return employeeTravelRequestPO;
	    }
		
		 @Override
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		    public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhocByManager(String branchId,int approvalUserId,String requestDate,String tripType, int startPgNo, int endPgNo) {
		        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		        Query query = entityManager.createQuery(
		                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.reqApprovalStatus='Y' AND b.reportingManagerUserId='"+approvalUserId+ "' AND b.approveStatus='Y' AND b.readFlg='Y' "
		                        		+ " AND DATE(b.requestDate)='" + requestDate + "' AND b.tripType='"+tripType+"' AND  b.requestType!='guest' "
		                        				+ " AND b.requestType!='AdhocRequest'  ORDER BY requestDate,z.zoneName,pickUpTime ASC").setFirstResult(startPgNo).setMaxResults(endPgNo); 
		        employeeTravelRequestPO = query.getResultList();
		        return employeeTravelRequestPO;
		    }
			
			
			  @Override
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		    public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceBothPickAndDropForNormalAndAdhocByManager(String branchId,int approvalUserId,String requestDate, int startPgNo, int endPgNo) {
		        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		        Query query = entityManager.createQuery(
		                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.reqApprovalStatus='Y' AND b.reportingManagerUserId='"+approvalUserId+ "' AND b.approveStatus='Y' AND b.readFlg='Y' "
		                        		+ " AND DATE(b.requestDate)='" + requestDate + "' AND  b.requestType!='guest' "
		                        				+ " AND b.requestType!='AdhocRequest'  ORDER BY requestDate,z.zoneName,pickUpTime ASC").setFirstResult(startPgNo).setMaxResults(endPgNo); 
		        employeeTravelRequestPO = query.getResultList();
		        return employeeTravelRequestPO;
		    }
			
			
			 @Override
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		    public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForNormalAndAdhocByManager(int approvalUserId,String branchId,String requestDate,Time shiftTime,String tripType, int startPgNo, int endPgNo) {
		        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		        Query query = entityManager.createQuery(
		                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId in ("+ branchId + ") AND b.reqApprovalStatus='Y' AND b.reportingManagerUserId='"+approvalUserId+ "' AND b.approveStatus='Y' AND b.readFlg='Y' "
		                        		+ " AND DATE(b.requestDate)='" + requestDate + "'  AND b.shiftTime='" + shiftTime+ "' AND b.tripType='"+tripType+"' AND  b.requestType!='guest' "
		                        				+ " AND b.requestType!='AdhocRequest'  ORDER BY requestDate,z.zoneName,pickUpTime ASC").setFirstResult(startPgNo).setMaxResults(endPgNo); 
		        employeeTravelRequestPO = query.getResultList();
		        return employeeTravelRequestPO;
		    }

			
			
			@Override
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		    public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForLocationFlg(String branchId,String requestDate,
		    		Time shiftTime,String tripType,int startPgNo, int endPgNo) {
		        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		        Query query = entityManager.createQuery(
		                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z "
		                + " where c.branchId in ("+ branchId+") AND b.approveStatus='Y' AND b.readFlg='Y' AND DATE(b.requestDate)='" + requestDate + "' AND b.shiftTime='" + shiftTime
		                        + "' AND b.locationFlg='M' AND b.tripType='"+tripType+"' ORDER BY requestDate,z.zoneName,pickUpTime ASC").setFirstResult(startPgNo).setMaxResults(endPgNo);
		        employeeTravelRequestPO = query.getResultList();
		        return employeeTravelRequestPO;
		    }			
			@Override
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
			    public List<EFmFmEmployeeTravelRequestPO> getEmpMultipleTravelledRequest(String branchId,String requestDate,int startPgNo, int endPgNo) {
			        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
			        Query query = entityManager.createQuery(
			                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  "
			                + " where c.branchId in ("+ branchId+") AND b.approveStatus='Y' AND DATE(b.requestDate)='" + requestDate + "' "
			                        		+ " AND b.readFlg='Y' AND  b.locationFlg='M' ORDER BY requestDate,z.zoneName,pickUpTime ASC").setFirstResult(startPgNo).setMaxResults(endPgNo);
			        employeeTravelRequestPO = query.getResultList();
			        return employeeTravelRequestPO;
			}			
			@Override
			@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
			public List<EFmFmEmployeeTravelRequestPO> getGuestAndAdhocTravelRequestsForGivendate(String branchId,
						String requestDate, int startPgNo, int endPgNo) {
			        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
			        Query query = entityManager.createQuery(
			                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  where c.branchId in ("+ branchId + ") AND b.approveStatus='Y' AND DATE(b.requestDate)='" + requestDate + "' AND b.readFlg='Y' AND (b.requestType='guest' OR b.requestType='AdhocRequest')  "
			                		+ " ORDER BY requestDate,z.zoneName,pickUpTime ASC").setFirstResult(startPgNo).setMaxResults(endPgNo);
			        employeeTravelRequestPO = query.getResultList();
			        return employeeTravelRequestPO;
		    }		
			@Override
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)		
			public List<EFmFmEmployeeTravelRequestPO> getListOfApprovalPendingRequest(String branchId,
					int approvalUserId, String approvalFlg, int startPgNo, int endPgNo) {
		        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		        Query query = entityManager.createQuery(
		                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.reportingManagerUserId='"+approvalUserId
		                        + "' AND b.requestStatus NOT LIKE '%C%' AND b.reqApprovalStatus='"+approvalFlg+"'").setFirstResult(startPgNo).setMaxResults(endPgNo);
		        employeeTravelRequestPO = query.getResultList();
		        return employeeTravelRequestPO;
		    }

			
			 
		    @Override
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		    public List<EFmFmEmployeeTravelRequestPO> listOfAdhocReservationsForGuestTravelRequests(String branchId,
					int startPgNo, int endPgNo) {
		        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		        Query query = entityManager.createQuery(
		                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z  where c.branchId='"
		                        + branchId
		                        + "' AND b.approveStatus='Y' AND b.readFlg='Y' AND b.requestType='AdhocRequest' ORDER BY requestDate,z.zoneName,pickUpTime ASC").setFirstResult(startPgNo).setMaxResults(endPgNo);
		        employeeTravelRequestPO = query.getResultList();
		        return employeeTravelRequestPO;
		    }

	
		
}
