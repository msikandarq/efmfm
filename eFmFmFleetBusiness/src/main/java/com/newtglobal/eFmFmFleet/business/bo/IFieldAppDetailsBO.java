package com.newtglobal.eFmFmFleet.business.bo;

import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmFmEscortDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFieldAppConfigMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmSupervisorMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserPasswordPO;

public interface IFieldAppDetailsBO {
	/*
	 * Start EFmFmVendorMasterPO master details 
	 */
	public void save(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO);
	public void update(EFmFmSupervisorMasterPO  eFmFmSupervisorMasterPO);
	public void delete(EFmFmSupervisorMasterPO  eFmFmSupervisorMasterPO);
	public List<EFmFmSupervisorMasterPO> getSupervisorDetails(int supervisorId, String branchId);
	public List<EFmFmFieldAppConfigMasterPO> getAllValuesUsedByConfigType(String branchId, String configType);
	public List<EFmFmSupervisorMasterPO> getAllSupervisorByVendorDetail(int vendorId, String branchId,String isActive);
	public List<EFmFmSupervisorMasterPO> getSupervisorMobileNumberDetails(String mobileNumber, String branchId);
	public List<EFmFmSupervisorMasterPO> getAllSupervisorDetails(String branchId, String isActive);
	public List<EFmFmSupervisorMasterPO> getSupervisorMobileNumberStatus(String mobileNumber, String branchId);
	public List<EFmFmSupervisorMasterPO> getSupervisorMobileNumberWithTokenDetails(String mobileNumber, String tempCode,String branchId);
	public List<EFmFmSupervisorMasterPO> getSupervisorPasswordDetailsFromSupervisorAndBranchId(int supervisorId, String branchId);
	public List<EFmFmEscortDocsPO> getEscortuploadFileDetails(int escortId,String docType);
	public int getSupervisorId(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO);
	

}
