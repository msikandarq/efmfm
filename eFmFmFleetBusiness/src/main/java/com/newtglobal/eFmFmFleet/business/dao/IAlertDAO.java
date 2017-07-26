package com.newtglobal.eFmFmFleet.business.dao;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmFmAlertTxnPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAlertTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmSmsAlertMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;

public interface IAlertDAO {
	
	public void save(EFmFmAlertTxnPO eFmFmAlertTxnPO);
	public void update(EFmFmAlertTxnPO eFmFmAlertTxnPO);
	public void delete(EFmFmAlertTxnPO eFmFmAlertTxnPO);
	public List<EFmFmAlertTxnPO> getAllAlertDetails(EFmFmAlertTxnPO eFmFmAlertTxnPO);		
	public void update(EFmFmTripAlertsPO tripAlertsPO);

	/**
	* The getParticularAlert method implemented.
	* for getting particular row details.  
	*
	* @author  Rajan R
	* 
	* @since   2015-05-28 
	* 
	* @return transaction details.
	*/
	public EFmFmAlertTxnPO getParticularAlert(EFmFmAlertTxnPO eFmFmAlertTxnPO);
	
	/**
	* The getAlertTypeIdDetails method implemented.
	* for getting particular alertId details from alert type master 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-28 
	* 
	* @return alert details.
	*/
	
	public EFmFmAlertTypeMasterPO getAlertTypeIdDetails(EFmFmAlertTypeMasterPO EFmFmAlertTypeMasterPO);
	
	/**
	* The getAllAlertTypeDetails method implemented.
	* for getting list of Alert Details. 
	*
	* @author  Rajan R
	* 
	* @since   2015-06-02 
	* 
	* @return alertType details.
	*/
	
	public List<EFmFmAlertTypeMasterPO> getAllAlertTypeDetails(EFmFmAlertTypeMasterPO EFmFmAlertTypeMasterPO);
	public void save(EFmFmAlertTypeMasterPO eFmFmAlertTypeMasterPO);
	public List<EFmFmAlertTxnPO> getCreatedAlertsByDate(Date fromDate,
			Date toDate, int clientId);
	
	/**
	* storing all the trip alerts.
	*  
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-06-30 
	* 
	* @return trip alert .
	*/
	public void save(EFmFmTripAlertsPO eFmFmTripAlertsPO);
	public List<EFmFmTripAlertsPO> getAllTodaysTripAlerts(EFmFmTripAlertsPO eFmFmTripAlertsPO);
	public List<EFmFmTripAlertsPO> getAllTripAlerts(String branchId);
	public List<EFmFmTripAlertsPO> getParticularTripAlerts(int branchId,
			int assignRouteId);
	public void deleteAllAlerts(int tripAlertsId);
	public long getNumberOfSosAlertCount(int branchId);
	public long getNumberOfRoadAlertCount(int branchId);
	public List<EFmFmTripAlertsPO> getAllTodaysTripSOSAlerts(String branchId);
	public List<EFmFmTripAlertsPO> getAllTodaysTripRoadAlerts(String branchId);
	public List<EFmFmTripAlertsPO> getAllTodaysTripOpenAlerts(String branchId);
	public List<EFmFmTripAlertsPO> getAllTodaysTripCloseAlerts(String branchId);
	public long getAllTodaysTripSOSAlertsCount(String branchId);
	public long getAllTodaysTripRoadAlertsCount(String branchId);
	public long getAllTodaysTripOpenAlertsCount(String branchId);
	public long getAllTodaysTripCloseAlertsCount(String branchId);
	public List<EFmFmTripAlertsPO> getParticuarAlertDetailFromAlertId(int branchId,
			int alertId, int assignRouteId);
	public List<EFmFmTripAlertsPO> getAllTripAlertsForSelectedDates(Date fromDate,
			Date toDate, String combinedBranchId);
	public List<EFmFmTripAlertsPO> getAllTripAlertsForSelectedDatesByVehicle(
			Date fromDate, Date toDate, String combinedBranchId, int vehicleId);
	public List<EFmFmTripAlertsPO> getAllTripAlertsForSelectedDatesByVendor(
			Date fromDate, Date toDate, String combinedBranchId, int vendorId);
	public List<EFmFmTripAlertsPO> getAllUnReadOpenAlerts(int branchId);
	
	public void addSMSAlertDetails(EFmFmSmsAlertMasterPO eFmFmSmsAlertMasterPO);	
	public List<EFmFmSmsAlertMasterPO> getSMSRecordExist(int branchId, int uniqueId, String userType,String alertType);
	
	public List<EFmFmAlertTypeMasterPO> getAlertIdFromAlertTitleAndBranchId(String alertTitle, int branchId);
	
	public List<EFmFmTripAlertsPO> getAllTripAlertsAndFeedbacksBasedOnSelectedDates(Date fromDate, Date toDate, int branchId,
			String alertOpenStatus);
	public List<EFmFmAlertTypeMasterPO> getlertDetailFromAlertIds(int alertId);
	public List<EFmFmTripAlertsPO> getAllTripAlertsAndFeedbacks(Date fromDate, Date toDate, int branchId);
	public List<EFmFmTripAlertsPO> getParticularAlertDetailFromAlertId(int tripAlertsId);
    public List<EFmFmTripAlertsPO> getAllTripTypeAndShiftAlertsAndFeedbacks(Date fromDate, Date toDate, String branchId,
			String tripType, Time shiftTime, String assignFeedbackTo);
}
