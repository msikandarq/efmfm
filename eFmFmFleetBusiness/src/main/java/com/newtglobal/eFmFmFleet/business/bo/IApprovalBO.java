package com.newtglobal.eFmFmFleet.business.bo;

import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmFmDriverDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;

public interface IApprovalBO {	
	/*
	 * start EFmFmDriverMasterPO  master details 
	 */
	public void save(EFmFmDriverMasterPO  eFmFmDriverMasterPO);
	public void update(EFmFmDriverMasterPO  eFmFmDriverMasterPO);
	public void delete(EFmFmDriverMasterPO  eFmFmDriverMasterPO);
	public List<EFmFmDriverMasterPO> getAllUnapprovedDrivers(String combinedBranchId);
	public List<EFmFmDriverMasterPO> getAllApprovedDrivers(int clientId);
	public List<EFmFmDriverMasterPO> getAllInActiveDrivers(String combinedBranchId);
	public EFmFmDriverMasterPO getParticularDriverDetail(int driverId);
	public void deleteParticularDriver(int driverId);
	
	/**
	* Register Driver device and update the driver data
	* or with particular client
	*
	* @author  SARFRAZ Khan
	* 
	* @since   2015-05-27 
	*/	
	public List<EFmFmDriverMasterPO> getParticularDriverDeviceDetails(
			String mobileNo, String branchId);
	/*
	 * end EFmFmDriverMasterPO  master details 
	 */
	
	/*
	 * start Vehicle  approval details 
	 */
	public List<EFmFmVehicleMasterPO> getAllUnapprovedVehicles(String combinedBranchId);
	public List<EFmFmVehicleMasterPO> getAllApprovedVehicles(String branchId);
	public List<EFmFmVehicleMasterPO> getAllInActiveVehicles(String combinedBranchId);
	public void deleteParticularVehicle(int vehicleId);
	/*
	 * end Vehicle  approval details 
	 */
	/*
	 * start vendor  approval details 
	 */
	public List<EFmFmVendorMasterPO> getAllUnapprovedVendors(String combinedBranchId);
	public List<EFmFmVendorMasterPO> getAllApprovedVendors(String combinedBranchId);
	public List<EFmFmVendorMasterPO> getAllInActiveVendors(String combinedBranchId);
	public void deleteParticularVendor(int vendorId);
	/*
	 * end vendor  master details 
	 */
	public void saveRouteTripDetails(
			EFmFmEmployeeTripDetailPO eFmFmEmployeeTripDetailPO);
	public List<EFmFmDriverMasterPO> getParticularDriverDetailsFromDeviceToken(
			String deviceId, int clientId);
	public List<EFmFmDriverMasterPO> getParticularDriverDetailFromDeriverId(int driverId);
	public List<EFmFmDriverDocsPO> getAlluploadFileDetails(int driverId);
	public void addUploadDetails(EFmFmDriverDocsPO eFmFmDriverDocsPO);	
	public List<EFmFmEscortDocsPO> getAllEscortuploadFileDetails(int escortId);
	public void addUploadDetails(EFmFmEscortDocsPO eFmFmEscortDocsPO);
	
	public List<EFmFmDriverMasterPO> getAllApprovedDriversWithOutDummy(String branchId);
	public List<EFmFmVehicleMasterPO> getAllApprovedVehiclesWithOutDummy(String branchId);
	

	
	
}
