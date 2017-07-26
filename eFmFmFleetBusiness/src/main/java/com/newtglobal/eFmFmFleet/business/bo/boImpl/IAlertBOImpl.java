package com.newtglobal.eFmFmFleet.business.bo.boImpl;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtglobal.eFmFmFleet.business.bo.IAlertBO;
import com.newtglobal.eFmFmFleet.business.dao.IAlertDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlertTxnPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlertTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmSmsAlertMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;

@Service("IAlertBO")
public class IAlertBOImpl implements IAlertBO {

    @Autowired
    IAlertDAO iAlertDAO;

    public void setiAlertDAO(IAlertDAO iAlertDAO) {
        this.iAlertDAO = iAlertDAO;
    }

    @Override
    public void save(EFmFmAlertTxnPO eFmFmAlertTxnPO) {
        iAlertDAO.save(eFmFmAlertTxnPO);

    }

    @Override
    public void update(EFmFmAlertTxnPO eFmFmAlertTxnPO) {
        iAlertDAO.update(eFmFmAlertTxnPO);
    }

    @Override
    public void delete(EFmFmAlertTxnPO eFmFmAlertTxnPO) {
        iAlertDAO.delete(eFmFmAlertTxnPO);
    }

    @Override
    public List<EFmFmAlertTxnPO> getAllAlertDetails(EFmFmAlertTxnPO eFmFmAlertTxnPO) {
        return iAlertDAO.getAllAlertDetails(eFmFmAlertTxnPO);
    }

    @Override
    public EFmFmAlertTxnPO getParticularAlert(EFmFmAlertTxnPO eFmFmAlertTxnPO) {
        return iAlertDAO.getParticularAlert(eFmFmAlertTxnPO);
    }

    @Override
    public EFmFmAlertTypeMasterPO getAlertTypeIdDetails(EFmFmAlertTypeMasterPO EFmFmAlertTypeMasterPO) {
        return iAlertDAO.getAlertTypeIdDetails(EFmFmAlertTypeMasterPO);
    }

    @Override
    public List<EFmFmAlertTypeMasterPO> getAllAlertTypeDetails(EFmFmAlertTypeMasterPO EFmFmAlertTypeMasterPO) {
        return iAlertDAO.getAllAlertTypeDetails(EFmFmAlertTypeMasterPO);
    }

    @Override
    public void save(EFmFmAlertTypeMasterPO eFmFmAlertTypeMasterPO) {
        iAlertDAO.save(eFmFmAlertTypeMasterPO);
    }

    @Override
    public List<EFmFmAlertTxnPO> getCreatedAlertsByDate(Date fromDate, Date toDate, int clientId) {
        return iAlertDAO.getCreatedAlertsByDate(fromDate, toDate, clientId);
    }

    @Override
    public void save(EFmFmTripAlertsPO eFmFmTripAlertsPO) {
        iAlertDAO.save(eFmFmTripAlertsPO);
    }

    @Override
    public List<EFmFmTripAlertsPO> getAllTodaysTripAlerts(EFmFmTripAlertsPO eFmFmTripAlertsPO) {
        return iAlertDAO.getAllTodaysTripAlerts(eFmFmTripAlertsPO);
    }

    @Override
    public List<EFmFmTripAlertsPO> getAllTripAlerts(String branchId) {
        return iAlertDAO.getAllTripAlerts(branchId);
    }

    @Override
    public List<EFmFmTripAlertsPO> getParticularTripAlerts(int branchId, int assignRouteId) {
        return iAlertDAO.getParticularTripAlerts(branchId, assignRouteId);
    }

    @Override
    public void update(EFmFmTripAlertsPO tripAlertsPO) {
        iAlertDAO.update(tripAlertsPO);
    }

    @Override
    public void deleteAllAlerts(int tripAlertsId) {
        iAlertDAO.deleteAllAlerts(tripAlertsId);
    }

    @Override
    public long getNumberOfSosAlertCount(int branchId) {
        return iAlertDAO.getNumberOfSosAlertCount(branchId);
    }

    @Override
    public long getNumberOfRoadAlertCount(int branchId) {
        return iAlertDAO.getNumberOfRoadAlertCount(branchId);
    }

    @Override
    public List<EFmFmTripAlertsPO> getAllTodaysTripSOSAlerts(String branchId) {
        return iAlertDAO.getAllTodaysTripSOSAlerts(branchId);
    }

    @Override
    public List<EFmFmTripAlertsPO> getAllTodaysTripRoadAlerts(String branchId) {
        return iAlertDAO.getAllTodaysTripRoadAlerts(branchId);
    }

    @Override
    public List<EFmFmTripAlertsPO> getAllTodaysTripOpenAlerts(String branchId) {
        return iAlertDAO.getAllTodaysTripOpenAlerts(branchId);
    }

    @Override
    public List<EFmFmTripAlertsPO> getAllTodaysTripCloseAlerts(String branchId) {
        return iAlertDAO.getAllTodaysTripCloseAlerts(branchId);
    }

    @Override
    public long getAllTodaysTripSOSAlertsCount(String branchId) {
        return iAlertDAO.getAllTodaysTripSOSAlertsCount(branchId);
    }

    @Override
    public long getAllTodaysTripRoadAlertsCount(String branchId) {
        return iAlertDAO.getAllTodaysTripRoadAlertsCount(branchId);
    }

    @Override
    public long getAllTodaysTripOpenAlertsCount(String branchId) {
        return iAlertDAO.getAllTodaysTripOpenAlertsCount(branchId);
    }

    @Override
    public long getAllTodaysTripCloseAlertsCount(String branchId) {
        return iAlertDAO.getAllTodaysTripCloseAlertsCount(branchId);
    }

    @Override
    public List<EFmFmTripAlertsPO> getParticuarAlertDetailFromAlertId(int branchId, int alertId, int assignRouteId) {
        return iAlertDAO.getParticuarAlertDetailFromAlertId(branchId, alertId, assignRouteId);
    }

    @Override
    public List<EFmFmTripAlertsPO> getAllTripAlertsForSelectedDates(Date fromDate, Date toDate, String branchId) {
        return iAlertDAO.getAllTripAlertsForSelectedDates(fromDate, toDate, branchId);
    }

    @Override
    public List<EFmFmTripAlertsPO> getAllTripAlertsForSelectedDatesByVehicle(Date fromDate, Date toDate, String branchId,
            int vehicleId) {
        return iAlertDAO.getAllTripAlertsForSelectedDatesByVehicle(fromDate, toDate, branchId, vehicleId);
    }

    @Override
    public List<EFmFmTripAlertsPO> getAllTripAlertsForSelectedDatesByVendor(Date fromDate, Date toDate, String branchId,
            int vendorId) {
        return iAlertDAO.getAllTripAlertsForSelectedDatesByVendor(fromDate, toDate, branchId, vendorId);
    }

    @Override
    public List<EFmFmTripAlertsPO> getAllUnReadOpenAlerts(int branchId) {
        return iAlertDAO.getAllUnReadOpenAlerts(branchId);
    }

	@Override
	public void addSMSAlertDetails(EFmFmSmsAlertMasterPO eFmFmSmsAlertMasterPO) {
		iAlertDAO.addSMSAlertDetails(eFmFmSmsAlertMasterPO);
		
	}

	@Override
	public List<EFmFmSmsAlertMasterPO> getSMSRecordExist(int branchId, int uniqueId, String userType,String alertType) {		
		return iAlertDAO.getSMSRecordExist(branchId, uniqueId, userType,alertType);
	}

	@Override
	public List<EFmFmAlertTypeMasterPO> getAlertIdFromAlertTitleAndBranchId(String alertTitle, int branchId) {
		return iAlertDAO.getAlertIdFromAlertTitleAndBranchId(alertTitle, branchId);
	}

	@Override
	public List<EFmFmTripAlertsPO> getAllTripAlertsAndFeedbacksBasedOnSelectedDates(Date fromDate, Date toDate,
			int branchId, String alertOpenStatus) {
		return iAlertDAO.getAllTripAlertsAndFeedbacksBasedOnSelectedDates(fromDate, toDate, branchId, alertOpenStatus);
	}

	@Override
	public List<EFmFmAlertTypeMasterPO> getlertDetailFromAlertIds(int alertId) {
		return iAlertDAO.getlertDetailFromAlertIds(alertId);
	}

	@Override
	public List<EFmFmTripAlertsPO> getAllTripAlertsAndFeedbacks(Date fromDate, Date toDate, int branchId) {
		return iAlertDAO.getAllTripAlertsAndFeedbacks(fromDate, toDate, branchId);
	}

	@Override
	public List<EFmFmTripAlertsPO> getParticularAlertDetailFromAlertId(int tripAlertsId) {
		return iAlertDAO.getParticularAlertDetailFromAlertId(tripAlertsId);
	}

	@Override
	public List<EFmFmTripAlertsPO> getAllTripTypeAndShiftAlertsAndFeedbacks(Date fromDate, Date toDate, String branchId,
			String tripType, Time shiftTime, String assignFeedbackTo) {
		return iAlertDAO.getAllTripTypeAndShiftAlertsAndFeedbacks(fromDate, toDate, branchId, tripType, shiftTime, assignFeedbackTo);
	}

	
	

}
