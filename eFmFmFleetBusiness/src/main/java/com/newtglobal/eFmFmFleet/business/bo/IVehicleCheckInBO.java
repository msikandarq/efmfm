package com.newtglobal.eFmFmFleet.business.bo;

import java.util.Date;
import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDynamicVehicleCheckListPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFixedDistanceContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFuelChargesPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripBasedContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCapacityMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleInspectionPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractInvoicePO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorFuelContractTypeMasterPO;


public interface IVehicleCheckInBO {
	
	/*
	 * Start EFmFmDeviceMasterPO master details 
	 */
	public void save(EFmFmDeviceMasterPO eFmFmDeviceMasterPO);
	public void update(EFmFmDeviceMasterPO  eFmFmDeviceMasterPO);
	public void delete(EFmFmDeviceMasterPO  eFmFmDeviceMasterPO);
	public void save(EFmFmFuelChargesPO eFmFmFuelChargesPO);
	public void update(EFmFmFuelChargesPO eFmFmFuelChargesPO);
	public void delete(EFmFmFuelChargesPO eFmFmFuelChargesPO);
	/*
	 * Start EFmFmVehicleMasterPO master details 
	 */
	public void save(EFmFmVehicleMasterPO eFmFmVehicleMasterPO);
	public void update(EFmFmVehicleMasterPO  eFmFmVehicleMasterPO);
	public void delete(EFmFmVehicleMasterPO  eFmFmVehicleMasterPO);
	
	
	public void save(
			EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO);
	public void update(
			EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO);
	public void update(EFmFmVehicleInspectionPO eFmFmVehicleInspectionPO);
	
	
	public void save(EFmFmVehicleCapacityMasterPO eFmFmVehicleCapacityMasterPO);
	public void update(EFmFmVehicleCapacityMasterPO eFmFmVehicleCapacityMasterPO);
	public void delete(EFmFmVehicleCapacityMasterPO eFmFmVehicleCapacityMasterPO);


	/**
	* The getParticularVehicleDetails implements for
	* Getting particular vehicle details based on vehicle Number. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/
	public EFmFmVehicleMasterPO getParticularVehicleDetails(String vehicleNumber,int clientId);
	public EFmFmVehicleMasterPO getParticularVehicleDetail(int vehicleId);
	/**
	* The getAllVehicleDetails implements for
	* Getting alla Vehicle details based on vendor & client Id. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-18 
	*/
	public List<EFmFmVehicleMasterPO> getAllVehicleDetails(EFmFmVehicleMasterPO eFmFmVehicleMasterPO);
	/**
	* The getAllDriverDetails implements for
	* Getting all driver details based on vendor & client Id. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-19 
	*/
	public List<EFmFmDriverMasterPO> getAllDriverDetails(EFmFmDriverMasterPO eFmFmDriverMasterPO);
	/**
	* The randomDriveDetails implements for
	* Getting all random driver details based on vendor. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-20 
	*/
	public List<EFmFmDriverMasterPO> randomDriverDetails(int vendorId,Date todayDate);
	/**
	* The getCheckInDriverDetails implements for
	* Getting previously allocated deriver details. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-20 
	*/
	public EFmFmVehicleCheckInPO getCheckInDriverDetails(int driverId);
	/**
	* The vehicleDriverCheckIn implements for
	* Check In  the vehicle Id along with driver Id. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-20 
	*/
	
	public void vehicleDriverCheckIn(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO);
	/**
	* The getAvailableVehicleDetails implements for
	* Getting all available Vehicle details based on vendorId. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-20 
	*/
	public List<EFmFmVehicleMasterPO> getAvailableVehicleDetails(int vendorId,Date todayDate);
	
	/**
	* The getCheckedInVehicleDetails implements for
	* Getting all checkedIn Vehicle details based on vendorId. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-21 
	*/
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetails(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO);
	
	/**
	 * The getCheckedInVehicleDetails implements for
	 * Getting all checkedIn Vehicle details based on vendorId. 
	 *
	 * @author  Rajan R
	 * 
	 * @since   2015-05-21 
	 */ 
	 public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetails(String clientId,Date date);
	
	/**
	* The getAllEscortDetails implements for
	* Getting all Escort details based on client Id. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-25 
	*/
	public List<EFmFmEscortMasterPO> getAllEscortDetails(String branchId);
	/**
	* The getParticularEscortDetails implements for
	* Getting Escort details based on escort Id. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-25 
	*/
	
	public List<EFmFmEscortMasterPO> getParticularEscortDetails(int escortId);
	
	/**
	* The saveEscortDetails implements for
	* storing escort details. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-28 
	*/
	public void saveEscortDetails(EFmFmEscortMasterPO eFmFmEscortMasterPO);
	public List<EFmFmVehicleCheckInPO> getParticulaCheckedInVehicleDetails(
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO);
	public int getNumberOfVehiclesFromClientId(int clientId);
	public List<EFmFmVehicleCheckInPO> getAssignedVehicleDetails(int clientId,
			Date todayDate);
	public void update(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO);
	public List<EFmFmEscortCheckInPO> getAllCheckedInEscort(String  branchId);
	public List<EFmFmEscortMasterPO> getAllCheckInEscort(String branchId);
	public void saveEscortCheckIn(EFmFmEscortCheckInPO eFmFmEscortCheckInPO);
	public List<EFmFmVehicleCheckInPO> checkedOutParticularDriver(
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO);
	public void updateEscortDetails(EFmFmEscortMasterPO eFmFmEscortMasterPO);
	public List<EFmFmDeviceMasterPO> deviceImeiNumberExistsCheck(String branchId,String imeiNumber);
	public List<EFmFmDriverMasterPO> getParticularDriverDetailsFromMobileNum(
			String mobileNumber, int clientId);
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInDriver(
			EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO);
	public List<EFmFmTripAlertsPO> getGroupByDriver(int clientId, Date fromDate,
			Date toDate);
	public List<EFmFmTripAlertsPO> getScoreCardDriver(int clientId, int driverId,
			Date fromDate, Date toDate);
	public List<EFmFmAssignRoutePO> getDriverAssignedTrip(int clientId, int driverId,
			Date fromDate, Date toDate);
	public List<EFmFmTripAlertsPO> getGroupByVehicle(int clientId, Date fromDate,
			Date toDate);
	public List<EFmFmTripAlertsPO> getScoreCardVehicle(int clientId, int driverId,
			Date fromDate, Date toDate);
	public List<EFmFmAssignRoutePO> getVehicleAssignedVehicleTrip(int clientId,
			int driverId, Date fromDate, Date toDate);
	public List<EFmFmEscortMasterPO> getMobileNoDetails(
			EFmFmEscortMasterPO eFmFmEscortMasterPO);
	public List<EFmFmDeviceMasterPO> getListOfAllActiveDevices(String branchId);
	public List<EFmFmVehicleCheckInPO> getCheckInDetails(int branchId);
	public List<EFmFmDriverMasterPO> getAllActiveDriverDetails(int vendorId);
	public List<EFmFmVehicleMasterPO> getAllActiveVehicleDetails(int vendorId);
	public List<EFmFmVehicleCheckInPO> alreadyCheckInExist(int vehicleId);	
	//public List<EFmFmVendorContractInvoicePO> getInvoiceforVehicle(Date fromDate, Date toDate, int branchId, int vehicleId);
	
	public List<EFmFmDriverMasterPO> getAllDriverDetails(int branchId);
	public List<EFmFmEscortCheckInPO> getParticulaEscortDetailFromEscortId(
			String branchId, int escortId);
	public void update(EFmFmEscortCheckInPO eFmFmEscortCheckInPO);	
	//public List<EFmFmVendorContractInvoicePO> getInvoiceDetailsVendor(Date fromDate, Date toDate, int branchId, int vendorId);
	//public List<EFmFmVendorContractInvoicePO> getvendorInvoiceFixedDistanceSummary(Date fromDate, Date toDate, int branchId,int vendorId);
	
	public List<EFmFmDeviceMasterPO> getDeviceDetailsFromDeviceId(int deviceId,
			String combinedBranchId);
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehiclesByVendor(int branchId,
			int vendorId);
	public List<EFmFmVehicleCheckInPO> getAllCheckedInDriversByVendor(int branchId,
			int vendorId);
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInDriverDetails(
			int branchId, int driverId);
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInDeviceDetails(
			int branchId, int deviceId);
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInVehicles(String branchId,
			int vehicleId);	
	/*Invoice*/
	public List<EFmFmVehicleMasterPO> getVendorBasedTripSheet(Date fromDate, Date toDate, int branchId, int vendorId);
	public List<EFmFmVehicleMasterPO> getNoOfWorkingDays(Date fromDate, Date toDate, int branchId, int vehicleId,String contractType, int contractDetailId);
	public List<EFmFmTripBasedContractDetailPO> getTripDistanceDetails(EFmFmTripBasedContractDetailPO eFmFmTripBasedContractDetailPO);
//	public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceDetails(EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO);
	public List<EFmFmAssignRoutePO> getVendorBasedVehicleDetails(Date fromDate, Date toDate, int branchId, int vendorId,String contractType,int contractDetailsId);
	public List<EFmFmAssignRoutePO> getVehicleBasedTripSheet(Date fromDate, Date toDate, int branchId,int vehicleId);
	public List<EFmFmVehicleMasterPO> getTripBasedNoOfWorkingDays(Date fromDate, Date toDate, int branchId, int vehicleId,String contractType, int contractDetailId);
	public void save(EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO);
	public void update(EFmFmVendorContractInvoicePO  eFmFmVendorContractInvoicePO);
	public void delete(EFmFmVendorContractInvoicePO  eFmFmVendorContractInvoicePO);
	public List<EFmFmVendorContractInvoicePO> getInvoiceforVendor(Date fromDate, Date toDate, int branchId, int vendorId, String invoiceType);	
	public List<EFmFmVehicleMasterPO> getAllVehicleDetails(int branchId);	
	public List<EFmFmVendorContractInvoicePO> getListOfInvoiceNumbers(int branchId);
	public List<EFmFmVendorContractInvoicePO> getInvoiceDetails(int branchId,int InvoiceNumber);	
	public List<EFmFmVehicleMasterPO> getSumOfTotalKmByVehicle(Date fromDate, Date toDate, int branchId, int vehicleId,String contractType, int contractDetailId);
	public List<EFmFmVendorContractInvoicePO> getInvoiceTripBasedVehicle(Date fromDate, Date toDate, int branchId,int vehicleId);
	public List<EFmFmAssignRoutePO> getTripBasedVehicleDetails(Date fromDate, Date toDate, int branchId, int vendorId,String contractType, int contractDetailsId);
	public List<EFmFmVendorContractInvoicePO> getInvoiceByVehicleFixedDistance(Date fromDate, Date toDate, int branchId,int vendorId, int vehicleId,String distaceFlg);
	public List<EFmFmVendorContractTypeMasterPO> getContractTypeDetails(String contractType,int branchId);
	public List<EFmFmVendorContractTypeMasterPO> getAllContractType(int branchId);
	public void deleteParticularCheckInEntryFromDeviceVehicleDriver(int checkInId);
	public List<EFmFmVehicleCheckInPO> alreadyCheckInDriverExistence(int driverId);
	public List<EFmFmVehicleCheckInPO> getDriverCheckInDetails(int branchId);
	public List<EFmFmDriverMasterPO> getAllApprovedDriversByVendorId(int vendorId,
			int branchId);
	public List<EFmFmVehicleMasterPO> getAllApprovedVehiclesByVendorId(int vendorId,
			int branchId);
	public List<EFmFmDeviceMasterPO> getAllApprovedDevices(String branchId);
	public List<EFmFmVehicleCheckInPO> alreadyCheckInDeviceExistence(int deviceId,
			int branchId);
	public List<EFmFmDeviceMasterPO> getAllActiveDeviceDetails(int branchId);
	public void deleteExtraCheckInEntry(int checkInId);
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsForEditBucket(
			int branchId);
	public List<EFmFmVehicleCheckInPO> getLastCheckInEntitiesDetails(int branchId);
	public long getAllCheckedInVehicleCount(int branchId);
	public List<EFmFmVehicleCheckInPO> getLastCheckedOutInDeviceDetails(int branchId,
			int deviceId);
	public List<EFmFmVehicleCheckInPO> getCheckedInVehiclesOnRoad(int branchId);
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsFromChecInId(
			int checkInId);
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromVehicleNumber(
			String vehicleNumber, String branchId);
	public List<EFmFmVehicleMasterPO> getAllActualVehicleDetailsFromBranchId(
			int branchId);
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleForCreatingNewBucket(
			int branchId);
				public List<EFmFmVehicleMasterPO> getCheckInVehicle(int branchId, Date requestedDate);
	public List<EFmFmVehicleMasterPO> getAllVehicleModel(int branchId);
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsFromChecInAndBranchId(
			int checkInId, int branchId);
	public List<EFmFmVehicleCheckInPO> getVehicleAndDriverAttendence(Date fromDate,
			Date toDate, String combinedBranchId);
	public List<EFmFmVehicleCheckInPO> getVehicleAndDriverAttendenceByVehicleId(
			Date fromDate, Date toDate, int branchId, int vehicleId);
	public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceActiveContractDetails(
			int branchId);
	public void save(EFmFmVehicleInspectionPO eFmFmVehicleInspectionPO);
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleDetailsForEditBucket(
			int branchId);
	public List<EFmFmDriverMasterPO> getAllDriverDetailsByBranchIdAndVendorId(
			int branchId, int vendorId);
	public List<EFmFmVehicleInspectionPO> getAllVehicleInspectionsByBranchIdVehicleIdAndDate(
			Date fromDate, Date toDate, int branchId, int vehicleId);
	public List<EFmFmVehicleCheckInPO> getVehiclesDistinctCapacityASC(int branchId);
	public List<EFmFmVehicleCheckInPO> getUnreadCheckedInDrivers(int branchId);
	public List<EFmFmVehicleCheckInPO> getAllCheckedInDriversByVendorIdAndVehicleNum(int branchId, int vendorId,
			int vehicleId);
	public List<EFmFmVehicleCheckInPO> getAllPresentDriverAndVehicles(String combinedBranchId);
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsWithOutDummyVehicles(String branchId);
	public List<EFmFmVehicleCheckInPO> getParticularDriverCheckedInTableEntry(String combinedBranchId, int driverId);
	public List<EFmFmVehicleCheckInPO> getParticularVehicleCheckedInTableEntry(String combinedBranchId, int vehicleId);
	public List<EFmFmVehicleCheckInPO> getParticularVendorCheckedInTableEntry(String combinedBranchId, int vendorId);
	public void invoiceUpdate(EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO);
	public List<EFmFmVendorContractInvoicePO> getInvoiceByContractInvoiceId(int contractInvoiceId, int branchId);
	public List<EFmFmVehicleMasterPO> getSumOfTravelledKm(Date fromDate, Date toDate, int branchId, int vehicleId,
			String contractType, int contractDetailId);
	public List<EFmFmVehicleCheckInPO> getVehicleWorkingDays(Date fromDate, Date toDate, int branchId, int vehicleId);
	public List<EFmFmVehicleInspectionPO> getParticularVehicleInspectionDetailByInspectedId(int inspectionId, int branchId);
	
    public List<EFmFmFixedDistanceContractDetailPO> getFixedModifyDistanceDetails(
            EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO);
    public List<EFmFmVendorContractInvoicePO> getInvoiceDetailsById(int branchId, int invoiceId);
    public List<EFmFmEscortMasterPO> getEscortDetailsByEmpId(String escortEmpId, String branchId);
    
    public List<EFmFmVehicleMasterPO> getKmDetailsByVehicleId(Date fromDate, Date toDate, int branchId, int vehicleId,String contractType, int contractDetailId);
    public List<EFmFmVehicleMasterPO> getAllContractDetailsByVendorId(int vendorId, String contractType, int branchId);
    public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceDetails(int contrctDetailsId, int branchId);
    public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceDetailsByCloneId(int cloneId,int branchId);
    public List<EFmFmVehicleMasterPO> bulkUpdateContractId(int vendorId, int contractDetailsId, int oldContractDetailsId,int branchId);   
    public List<EFmFmFuelChargesPO> getAllFuelDetails(int branchId);
	public List<EFmFmAssignRoutePO> getTotalKmByVehicle(Date fromDate, Date toDate, int branchId, int vehicleId,
			String contractType, int contractDetailsId);
	public List<EFmFmFuelChargesPO> getFuelDetails(int fuelTypeId, int branchId);
	public List<EFmFmFuelChargesPO> getFuelDetailsByDate(Date fromDate,int fuelTypeId, int branchId);
	public List<EFmFmVendorFuelContractTypeMasterPO> getAllContractFuelType(int branchId);
	public List<EFmFmVendorFuelContractTypeMasterPO> getFuelContractTypeDetails(int fuelTypeId,int branchId);
	public List<EFmFmVendorContractInvoicePO> getNonApprovalList(String branchId, String statusFlg, String invoiceflag);
	public List<EFmFmVendorContractInvoicePO> getModifiedAmountDetails(int branchId,int InvoiceNumber);
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleDetails(String branchId);
	public List<EFmFmVehicleDocsPO> getAlluploadVehicleFileDetails(int vehicleId);
	public void addVehicleUploadDetails(EFmFmVehicleDocsPO eFmFmVehicleDocsPO);
	public List<EFmFmEscortMasterPO> getAllDisableEscortDetails(String  branchId);
	public List<EFmFmEscortMasterPO> getEscortDetailsByActiveFlg(int escortId);
	public int getAllNonRemovedVehicleCountByBranchIdAndVendorId(int vendorId, String branchId);
	public int getAllNonRemovedDriverCountByBranchIdAndVendorId(int vendorId, String branchId);
	public List<EFmFmDriverMasterPO> getAllRemovedDriverDetailsByBranchIdAndVendorId(String branchId,int vendorId);
	public List<EFmFmVehicleMasterPO> getAllUnRemovedVehicleDetailsByBranchIdAndVendorId(String branchId,int vendorId);
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInDriverDetailsByBranchIdAndVendorId(String branchId, int vendorId);
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsWithOutDummyAllocatedToRoutes(int branchId);
	public List<EFmFmVehicleMasterPO> getAllVehicleDetailsByBranchId(int branchId);
	public List<EFmFmAssignRoutePO> getOngoingVehicleDetails(Date fromDate, Date toDate, int branchId, int vehicleId);
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromVehicleNumberAndVendorIdBranchId(String vehicleNumber,
			int vendorId);
	public List<EFmFmDriverMasterPO> getDriverDetailsFromDriverNumberAndVendorId(String mobileNumber, int vendorId);
	public List<EFmFmVehicleCheckInPO> getParticularDriverCheckedInDetails(int driverId);
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromVehicleNumberAndVendorId(String vehicleNumber, int vendorId);
	public List<EFmFmDriverMasterPO> getDriverDetailsFromDriverMobilrNumber(String mobileNumber);
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromVehicleNumber(String vehicleNumber);
	public List<EFmFmVehicleCheckInPO> getParticularDeviceCheckedInDetails(int deviceId);
	public List<EFmFmVehicleMasterPO> getSumOfTotalKmByVehicleOdometer(Date fromDate, Date toDate, int branchId, int vehicleId,
			String contractType, int contractDetailId);
	public List<EFmFmVendorContractInvoicePO> deleteInvoiceData(Date fromDate, Date toDate, int branchId, int vendorId);
	public List<EFmFmVendorContractInvoicePO> getInvoiceforVendorByGroupDistance(Date fromDate, Date toDate, int branchId,
			int vendorId, String distanceFlg);
	public EFmFmVehicleCheckInPO getCheckedInEntityDetailFromChecInId(int checkInId);
	public void updateVehicleCheckInDetailsWithStatus(int checkInId, int numberOfTrips, long totalTravelTime,
			double totalTravelDistance, String status);
	public void updateVehicleCheckInDetailsWithOutStatus(int checkInId, int numberOfTrips, long totalTravelTime,
			double totalTravelDistance);
	public void updateVehicleMonthlyDistance(int vehicleId, double monthlyPendingFixedDistance,String status);
	public List<EFmFmVehicleCheckInPO> getLastCheckInVehicleIdDetails(int branchId, int vehicleId);
	public List<EFmFmVehicleMasterPO> getlistOfYetToCheckInVehicleDetails(int branchId);
	public List<EFmFmVehicleInspectionPO> getAllVendorInspectionsByBranchIdAndDate(Date fromDate, Date toDate, int branchId);
	public List<EFmFmVehicleInspectionPO> getVendorInspectionsByBranchIdAndDate(Date fromDate, Date toDate, int branchId,
			int vendorId);
	public List<EFmFmEmployeeTripDetailPO> getparticularEmployeeTripDetails(String vehicleNumber, int branchId);
	public List<EFmFmDeviceMasterPO> getListOfAllavailableDevices(int branchId);
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInVehileDetailsByBranchIdAndVendorId(int branchId, int vendorId);
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleByVendors(int vendorId, int branchId);
	public List<EFmFmEscortMasterPO> getEscostMobileNoDetails(String mobileNumber,String branchId);
	
	public List<EFmFmVehicleMasterPO> getAllVehicleVendorsDetails(int branchId);
	public List<EFmFmVehicleMasterPO> getAllAvailableVehiclesByVendor(int branchId, int vendorId);
	public List<EFmFmDriverMasterPO> getAllAvailableDriverByVendor(int branchId, int vendorId);
	public List<EFmFmDeviceMasterPO> getAllAvailableDeviceWithOutDummy(int branchId);
	public List<EFmFmEscortMasterPO> getEscostMobileNoWithTokenDetails(String mobileNumber, String tempCode, String branchId);
	public List<EFmFmEscortMasterPO> getEscortPasswordDetailsFromEscortAndBranchId(int escortId, int branchId);
	public List<EFmFmVendorContractInvoicePO> getListOfGeneratedInvoiceDetails(int branchId, String invoiceStatus);
	public List<EFmFmVendorContractInvoicePO> getListOfInvoiceDetailsByGroupInvoiceNumber(int branchId, String invoiceStatus);
	public void updateVehicleContractInvoiceStatus(int invoiceNumber, String invoiceStatus);
	public int saveVehicleRecordWithValue(EFmFmVehicleMasterPO eFmFmVehicleMasterPO);
	public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceDummyDetails(int branchId);
	public void save(EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMasterPO);
	public void update(EFmFmVendorContractTypeMasterPO  eFmFmVendorContractTypeMasterPO);
	public List<EFmFmVendorContractTypeMasterPO> getVendorContractTypeDetails(int branchId);
	public List<EFmFmVendorContractTypeMasterPO> validateVendorContractTypeDetails(String contrctType, int branchId);
	public List<EFmFmFixedDistanceContractDetailPO> getFixedContractDetailsValidation(Date fromDate, Date toDate, int branchId,
			int vendorId);
	public List<EFmFmFixedDistanceContractDetailPO> getFixedContractDetailsVehicleValidation(Date fromDate, Date toDate,
			int branchId, int vehicleId);	
	
	public void deleteVehicleUploadDetails(int vehicleDocId);
	public void deleteDriverUploadDetails(int driverDocId);
	public List<EFmFmDriverDocsPO> getAlluploadFileDetails(int driverId);
	public List<EFmFmFixedDistanceContractDetailPO> getAllVehicleContractDetails(Date fromDate, Date toDate, int branchId,
			int vendorId);
	
	public List<EFmFmVendorContractTypeMasterPO> getContractTypeIdDetails(int contractTypeId,int branchId);
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleDetailsForEditBucketByvendorId(int branchId, int vendorId);
	public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceContrctTitleExistDetails(String contractTitle,int branchId);	
	public List<EFmFmVendorFuelContractTypeMasterPO> getDummyFuelType(String FuelType, int branchId);
	public List<EFmFmAssignRoutePO> getPresentDaysBasedOnTrips(Date fromDate, Date toDate, int branchId, int vehicleId);
	public List<EFmFmAssignRoutePO> getMaxTimePresentDaysBasedOnTrips(Date travelledDate, int branchId, int vehicleId);
	public int getAllNonRemovedSupervisorCountByBranchIdAndVendorId(int vendorId, String branchId);
	public List<EFmFmDriverMasterPO> getAllDriversWithOutStatus(int branchId, int vendorId);
	public List<EFmFmVehicleMasterPO> getAllVehiclesWithOutStatus(int branchId, int vendorId);
	public List<EFmFmDynamicVehicleCheckListPO> getAllcheckListDetails(int branchId, String checkListType);
	public List<EFmFmAssignRoutePO> getPastTripDetailsforDriver(Date fromDate, Date toDate, int branchId, int driverId);
	public List<EFmFmVehicleMasterPO> getVehicleExistDetailsByVehicleNumberVendorNameAndBranchId(String vehicleNumber,
			String vendorName, String branchId);
	public List<EFmFmVehicleMasterPO> getParticularVehicleDetailsByVehicleNumber(String vehicleNumber, int vendorId,
			String branchId);
	public List<EFmFmVehicleMasterPO> getEngineNoDetails(String engineNo, int vendorId, String branchId);
	public List<EFmFmVehicleMasterPO> getRcNumberDetails(String rcNumber, int vendorId, String branchId);
	public List<EFmFmDeviceMasterPO> deviceNumberExistsCheck(String mobileNumber, String branchId);
	public List<EFmFmDeviceMasterPO> getDeviceDetailsFromDeviceIdAndBranchId(int deviceId, int branchId);
	public List<EFmFmVehicleCapacityMasterPO> getVehicleTypeBranchWise(String branchId);
	public void deleteVehicleTypeBranchId(int vehicleCapacityId);
	public List<EFmFmVehicleCheckInPO> getVehicleAlreadyCheckInOrNot(int vehicleId);
	public List<EFmFmDeviceMasterPO> getDummyDeviceDetailsFromDeviceId(String branchId);

	}
