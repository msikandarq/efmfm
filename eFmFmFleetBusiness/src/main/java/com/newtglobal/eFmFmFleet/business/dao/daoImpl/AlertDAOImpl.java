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

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newtglobal.eFmFmFleet.business.dao.IAlertDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlertTxnPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlertTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmSmsAlertMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;

@Repository("IAlertDAO")
public class AlertDAOImpl implements IAlertDAO {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(EFmFmAlertTxnPO eFmFmAlertTxnPO) {
        entityManager.persist(eFmFmAlertTxnPO);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(EFmFmAlertTypeMasterPO eFmFmAlertTypeMasterPO) {
        entityManager.persist(eFmFmAlertTypeMasterPO);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void update(EFmFmAlertTxnPO eFmFmAlertTxnPO) {
        entityManager.merge(eFmFmAlertTxnPO);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void update(EFmFmTripAlertsPO tripAlertsPO) {
        entityManager.merge(tripAlertsPO);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void delete(EFmFmAlertTxnPO eFmFmAlertTxnPO) {
        entityManager.remove(eFmFmAlertTxnPO);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAlertTxnPO> getAllAlertDetails(EFmFmAlertTxnPO eFmFmAlertTxnPO) {
        List<EFmFmAlertTxnPO> alertTxnPO = new ArrayList<EFmFmAlertTxnPO>();
        Query query = entityManager.createQuery(
                "SELECT a FROM EFmFmAlertTxnPO a JOIN a.efmFmAlertTypeMaster b JOIN b.eFmFmClientBranchPO c where c.branchId='"
                        + eFmFmAlertTxnPO.getEfmFmAlertTypeMaster().geteFmFmClientBranchPO().getBranchId()
                        + "' and DATE(a.startDate)=TRIM('" + eFmFmAlertTxnPO.getStartDate()
                        + "') and DATE(a.endDate)=TRIM('" + eFmFmAlertTxnPO.getEndDate() + "') and a.status='Y'");
        alertTxnPO = query.getResultList();
        return alertTxnPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EFmFmAlertTxnPO getParticularAlert(EFmFmAlertTxnPO eFmFmAlertTxnPO) {
        List<EFmFmAlertTxnPO> alertTxnPO = new ArrayList<EFmFmAlertTxnPO>();
        Query query = entityManager.createQuery(
                "SELECT a FROM EFmFmAlertTxnPO a JOIN a.efmFmAlertTypeMaster b JOIN b.eFmFmClientBranchPO c where a.id='"
                        + eFmFmAlertTxnPO.getId() + "'");
        try {
            alertTxnPO = query.getResultList();
        } catch (Exception e) {
        }
        if (alertTxnPO.isEmpty()) {
            return null;
        } else {
            return alertTxnPO.get(0);
        }

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EFmFmAlertTypeMasterPO getAlertTypeIdDetails(EFmFmAlertTypeMasterPO eFmFmAlertTypeMasterPO) {
        List<EFmFmAlertTypeMasterPO> alertTypeMater = new ArrayList<EFmFmAlertTypeMasterPO>();
        Query query = entityManager
                .createQuery("SELECT a FROM EFmFmAlertTypeMasterPO a JOIN a.eFmFmClientBranchPO where a.alertId='"
                        + eFmFmAlertTypeMasterPO.getAlertId() + "' and c.branchId='"
                        + eFmFmAlertTypeMasterPO.geteFmFmClientBranchPO().getBranchId() + "' ");
        try {
            alertTypeMater = query.getResultList();
        } catch (Exception e) {
        }
        if (alertTypeMater.isEmpty()) {
            return null;
        } else {
            return alertTypeMater.get(0);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAlertTypeMasterPO> getAllAlertTypeDetails(EFmFmAlertTypeMasterPO eFmFmAlertTypeMasterPO) {
        List<EFmFmAlertTypeMasterPO> eFmFmAlertTypeMaster = new ArrayList<EFmFmAlertTypeMasterPO>();
        Query query = entityManager
                .createQuery("SELECT a FROM EFmFmAlertTypeMasterPO a JOIN a.eFmFmClientBranchPO c where c.branchId='"
                        + eFmFmAlertTypeMasterPO.geteFmFmClientBranchPO().getBranchId() + "' AND status='Y' ");
        eFmFmAlertTypeMaster = query.getResultList();
        return eFmFmAlertTypeMaster;
    }
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAlertTypeMasterPO> getlertDetailFromAlertIds(int alertId) {
        List<EFmFmAlertTypeMasterPO> eFmFmAlertTypeMaster = new ArrayList<EFmFmAlertTypeMasterPO>();
        Query query = entityManager
                .createQuery("SELECT a FROM EFmFmAlertTypeMasterPO a  where a.alertId='"+alertId+"' ");
        eFmFmAlertTypeMaster = query.getResultList();
        return eFmFmAlertTypeMaster;
    }
    
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAlertTypeMasterPO> getAlertIdFromAlertTitleAndBranchId(String alertTitle,int branchId) {
        List<EFmFmAlertTypeMasterPO> eFmFmAlertTypeMaster = new ArrayList<EFmFmAlertTypeMasterPO>();
        Query query = entityManager
                .createQuery("SELECT a FROM EFmFmAlertTypeMasterPO a JOIN a.eFmFmClientBranchPO c where c.branchId='"
                        + branchId + "' AND a.alertTitle='"+alertTitle+"' ");
        eFmFmAlertTypeMaster = query.getResultList();
        return eFmFmAlertTypeMaster;
    }
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmAlertTxnPO> getCreatedAlertsByDate(Date fromDate, Date toDate, int branchId) {
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.format(fromDate);
        List<EFmFmAlertTxnPO> alerts = new ArrayList<EFmFmAlertTxnPO>();
        String query = "SELECT t FROM EFmFmAlertTxnPO t JOIN t.efmFmAlertTypeMaster ty JOIN ty.eFmFmClientBranchPO c WHERE t.status ='Y' AND t.endDate >= ?1  AND   t.endDate >= ?2  AND c.branchId='"
                + branchId + "'  ";
        Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
                toDate, TemporalType.TIMESTAMP);
        alerts = q.getResultList();
        return alerts;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllTodaysTripAlerts(EFmFmTripAlertsPO eFmFmTripAlertsPO) {
        List<EFmFmTripAlertsPO> allAlerts = new ArrayList<EFmFmTripAlertsPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c where b.status='Y' AND c.branchId='"
                        + eFmFmTripAlertsPO.getEfmFmAssignRoute().geteFmFmClientBranchPO().getBranchId() + "' ORDER BY b.creationTime DESC");
        allAlerts = query.getResultList();
        return allAlerts;
    }

    // Start Of Dash Board alerts
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllTodaysTripSOSAlerts(String branchId) {
        List<EFmFmTripAlertsPO> allAlerts = new ArrayList<EFmFmTripAlertsPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN b.efmFmAlertTypeMaster al where b.status='Y' AND al.alertType='sos' AND c.branchId in ("+ branchId + ") ORDER BY b.creationTime DESC" );
        allAlerts = query.getResultList();
        return allAlerts;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllTodaysTripSOSAlertsCount(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN b.efmFmAlertTypeMaster al where b.status='Y' AND al.alertType='sos' AND c.branchId in ("+ branchId + ")");
        long allAlerts = (long) query.getSingleResult();
        return allAlerts;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllTodaysTripRoadAlerts(String branchId) {
        List<EFmFmTripAlertsPO> allAlerts = new ArrayList<EFmFmTripAlertsPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN b.efmFmAlertTypeMaster al where b.status='Y' AND al.alertType='road' AND c.branchId in ("+ branchId + ") ORDER BY b.creationTime DESC");
        allAlerts = query.getResultList();
        return allAlerts;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllTodaysTripRoadAlertsCount(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN b.efmFmAlertTypeMaster al where b.status='Y' AND al.alertType='road' AND c.branchId in ("+ branchId + ")");
        long allAlerts = (long) query.getSingleResult();
        return allAlerts;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllTodaysTripOpenAlerts(String branchId) {
        List<EFmFmTripAlertsPO> allAlerts = new ArrayList<EFmFmTripAlertsPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c where b.status='Y' AND b.alertOpenStatus='open'  AND c.branchId in ("+ branchId + ") ORDER BY b.creationTime DESC");
        allAlerts = query.getResultList();
        return allAlerts;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllTodaysTripOpenAlertsCount(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c where b.status='Y' AND b.alertOpenStatus='open'  AND c.branchId in ("+ branchId + ")");
        long allAlerts = (long) query.getSingleResult();
        return allAlerts;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllUnReadOpenAlerts(int branchId) {
        List<EFmFmTripAlertsPO> allAlerts = new ArrayList<EFmFmTripAlertsPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c where b.status='Y' AND b.alertOpenStatus='open' AND b.readFlg='N' AND c.branchId='"
                        + branchId + "' ORDER BY b.creationTime DESC");
        allAlerts = query.getResultList();
        return allAlerts;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllTodaysTripCloseAlerts(String branchId) {
        List<EFmFmTripAlertsPO> allAlerts = new ArrayList<EFmFmTripAlertsPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c  where b.status='Y' AND b.alertOpenStatus='close' AND d.tripStatus !='completed' AND c.branchId in ("+ branchId + ") ORDER BY b.creationTime DESC");
        allAlerts = query.getResultList();
        return allAlerts;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getParticularAlertDetailFromAlertId(int tripAlertsId) {
        List<EFmFmTripAlertsPO> allAlerts = new ArrayList<EFmFmTripAlertsPO>();
        Query query = entityManager.createQuery("SELECT b FROM EFmFmTripAlertsPO b WHERE  b.tripAlertsId='"+tripAlertsId+"' ");
        allAlerts = query.getResultList();
        return allAlerts;
    }

    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getAllTodaysTripCloseAlertsCount(String branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c  where b.status='Y' AND b.alertOpenStatus='close' And d.tripStatus !='completed' AND c.branchId in ("+ branchId + ")");
        long allAlerts = (long) query.getSingleResult();
        return allAlerts;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllTripAlertsForSelectedDates(Date fromDate, Date toDate, String branchId) {
        String query = "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAlertTypeMaster ty JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c "
        		+ " WHERE  date(b.creationTime) >= ?1  AND date(b.creationTime) <=?2  "
        		+ " AND c.branchId in ("+branchId+") AND ty.alertId=10 ORDER BY b.creationTime DESC";
        Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
                toDate, TemporalType.TIMESTAMP);
        return q.getResultList();
    }
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllTripAlertsAndFeedbacksBasedOnSelectedDates(Date fromDate, Date toDate, int branchId,String alertOpenStatus) {
        String query = "SELECT b FROM EFmFmTripAlertsPO b  WHERE  date(b.creationTime) >= ?1  AND date(b.creationTime) <=?2  AND b.alertOpenStatus='"+alertOpenStatus+"'  ORDER BY b.creationTime DESC";
        Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
                toDate, TemporalType.TIMESTAMP);
        return q.getResultList();
    }

   
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllTripAlertsAndFeedbacks(Date fromDate, Date toDate, int branchId) {
        String query = "SELECT b FROM EFmFmTripAlertsPO b  WHERE  date(b.creationTime) >= ?1  AND date(b.creationTime) <=?2  ORDER BY b.creationTime DESC";
        Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
                toDate, TemporalType.TIMESTAMP);
        return q.getResultList();
    }
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllTripTypeAndShiftAlertsAndFeedbacks(Date fromDate, Date toDate, String branchId,String tripType,Time shiftTime,String assignFeedbackTo) {
       System.out.println("fromDate"+fromDate);
       System.out.println("toDate"+toDate);

    	String query = "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute a JOIN a.eFmFmClientBranchPO c WHERE  date(b.creationTime) >= ?1  AND date(b.creationTime) <=?2  AND b.tripType='"+tripType+"' AND b.shiftTime='"+shiftTime+"' AND c.branchId in ("+ branchId + ") AND  b.assignFeedbackTo='"+assignFeedbackTo+"' ORDER BY b.creationTime DESC";
        Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
                toDate, TemporalType.TIMESTAMP);
        return q.getResultList();
    }
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllTripAlertsForSelectedDatesByVehicle(Date fromDate, Date toDate, String branchId,
            int vehicleId) {
        String query = "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAlertTypeMaster ty JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN d.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster dm WHERE  date(b.creationTime) >= ?1  AND date(b.creationTime) <=?2 "
        		+ " AND c.branchId in ("+branchId+") AND dm.vehicleId='" + vehicleId + "' AND ty.alertId=10 ORDER BY b.creationTime DESC";
        Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
                toDate, TemporalType.TIMESTAMP);
        return q.getResultList();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllTripAlertsForSelectedDatesByVendor(Date fromDate, Date toDate, String branchId,
            int vendorId) {
        String query = "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAlertTypeMaster ty JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN d.efmFmVehicleCheckIn ch JOIN ch.efmFmVehicleMaster dm JOIN dm.efmFmVendorMaster vm WHERE  date(b.creationTime) >= ?1  AND date(b.creationTime) <=?2  "
        		+ " AND c.branchId in ("+branchId+") AND vm.vendorId='" + vendorId + "' AND ty.alertId=10 ORDER BY b.creationTime DESC";
        Query q = entityManager.createQuery(query).setParameter(1, fromDate, TemporalType.TIMESTAMP).setParameter(2,
                toDate, TemporalType.TIMESTAMP);
        return q.getResultList();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getAllTripAlerts(String branchId) {
        List<EFmFmTripAlertsPO> allAlerts = new ArrayList<EFmFmTripAlertsPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c where b.status='Y' AND c.branchId in ("+ branchId + ") ORDER BY b.creationTime DESC ");
        allAlerts = query.getResultList();
        return allAlerts;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getParticularTripAlerts(int branchId, int assignRouteId) {
        List<EFmFmTripAlertsPO> allAlerts = new ArrayList<EFmFmTripAlertsPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c where b.status='Y' AND c.branchId='"
                        + branchId + "' AND d.assignRouteId='" + assignRouteId + "' ORDER BY b.creationTime DESC");
        allAlerts = query.getResultList();
        return allAlerts;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteAllAlerts(int tripAlertsId) {
        Query query = entityManager
                .createQuery("DELETE EFmFmTripAlertsPO where tripAlertsId = '" + tripAlertsId + "' ");
        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(EFmFmTripAlertsPO eFmFmTripAlertsPO) {
        entityManager.persist(eFmFmTripAlertsPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getNumberOfSosAlertCount(int branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c JOIN b.efmFmAlertTypeMaster t where b.status='Y' AND t.alertType='sos' AND c.branchId='"
                        + branchId + "'");
        long sosAlertCount = (long) query.getSingleResult();
        return sosAlertCount;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long getNumberOfRoadAlertCount(int branchId) {
        Query query = entityManager.createQuery(
                "SELECT count(b) FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c  where b.status='Y' AND c.branchId='"
                        + branchId + "'");
        long sosAlertCount = (long) query.getSingleResult();
        return sosAlertCount;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmTripAlertsPO> getParticuarAlertDetailFromAlertId(int branchId, int alertId, int assignRouteId) {
        List<EFmFmTripAlertsPO> allAlerts = new ArrayList<EFmFmTripAlertsPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmTripAlertsPO b JOIN b.efmFmAssignRoute d JOIN d.eFmFmClientBranchPO c where b.tripAlertsId='"
                        + alertId + "'  AND d.assignRouteId='" + assignRouteId + "'AND c.branchId='" + branchId + "'");
        allAlerts = query.getResultList();
        return allAlerts;
    }

	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addSMSAlertDetails(EFmFmSmsAlertMasterPO eFmFmSmsAlertMasterPO) {
		entityManager.persist(eFmFmSmsAlertMasterPO);		
	}

	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmSmsAlertMasterPO> getSMSRecordExist(int branchId, int uniqueId, String userType,String alertType) {
		 List<EFmFmSmsAlertMasterPO> allAlerts = new ArrayList<EFmFmSmsAlertMasterPO>();
	        Query query = entityManager.createQuery("SELECT b FROM EFmFmSmsAlertMasterPO b JOIN b.eFmFmClientBranchPO c "
	        		+ "	where b.userType='"+userType+"' AND b.alertType='"+alertType+"' AND b.uniqueId='"+uniqueId+"' "
	        				+ " AND c.branchId='" + branchId + "' order by b.triggerId desc ");
	        allAlerts = query.getResultList();
	        return allAlerts;
	}

}
