package com.newtglobal.eFmFmFleet.business.dao;

import java.util.Date;
import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverFeedbackPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;

public interface IVendorDetailsDAO {
	/*
	 * Start EFmFmVendorMasterPO master details 
	 */
	public void save(EFmFmVendorMasterPO eFmFmVendorMasterPO);
	public void update(EFmFmVendorMasterPO  eFmFmVendorMasterPO);
	public void delete(EFmFmVendorMasterPO  eFmFmVendorMasterPO);
	public void save(EFmFmDriverFeedbackPO driverFeedbackPO);

	/**
	* The getAllAreaName implements for
	* for validating and getting all area names from area master table. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/
	public List<EFmFmVendorMasterPO> getAllVendorName(String vendorName,String clientId);
	public EFmFmVendorMasterPO getParticularVendorDetail(int vendorId);
	/**
	* The getAllVendorsDetails implements 
	* for validating and getting vendor names from vendor master table. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-18 
	*/	
	public List<EFmFmVendorMasterPO> getAllVendorsDetails(EFmFmVendorMasterPO vendorMasterPO);
	
	/**
	* check Driver device Exist or not 
	* or with particular client
	*
	* @author  SARFRAZ Khan
	* 
	* @since   2015-05-27 
	*/	
	
	public boolean doesDriverDeviceExist(String deviceId,int clientId);
	public List<EFmFmDeviceMasterPO> getAllDeviceDetails(int clientId, Date todayDate);
	public List<EFmFmVendorMasterPO> getVendorTinNumber(String tinNumber, String branchId);
	public List<EFmFmVendorMasterPO> getVendorEmailId(String emailID, String branchId);
	public List<EFmFmVendorMasterPO> getVendorMobileNo(String vendorMobileNo,
			String branchId);
	public List<EFmFmDeviceMasterPO> getAllActiveDeviceDetails(int branchId);
	public List<EFmFmVendorMasterPO> getAllApprovedVendorsDetails(String branchId);
	public List<EFmFmDeviceMasterPO> getAllDeviceDetailsFromBranchId(String branchId);
	public List<EFmFmVendorMasterPO> getAllEnableVendorsDetails(String branchId);
	public List<EFmFmVendorMasterPO> getAllVendorDetailsByVendorId(String branchId,int vendorId);
	public List<EFmFmVehicleMasterPO> getAllActiveVehicleDetails(int vendorId);
	public List<EFmFmDriverMasterPO> getAllActiveDriverDetails(int vendorId);
		

}
