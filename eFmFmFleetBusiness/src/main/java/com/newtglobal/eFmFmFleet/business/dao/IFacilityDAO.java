package com.newtglobal.eFmFmFleet.business.dao;

import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmFmFacilityToFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;

public interface IFacilityDAO {
	
	public void save(EFmFmFacilityToFacilityMappingPO  eFmFmFacilityToFacilityMappingPO);
	public void update(EFmFmFacilityToFacilityMappingPO  eFmFmFacilityToFacilityMappingPO);
	public void delete(EFmFmFacilityToFacilityMappingPO  eFmFmFacilityToFacilityMappingPO);
	
	public void save(EFmFmUserFacilityMappingPO  eFmFmUserFacilityMappingPO);
	public void update(EFmFmUserFacilityMappingPO  eFmFmUserFacilityMappingPO);
	public void delete(EFmFmUserFacilityMappingPO  eFmFmUserFacilityMappingPO);
	public List<EFmFmFacilityToFacilityMappingPO> getAllAttachedFacilities(int branchId);
	public List<EFmFmUserFacilityMappingPO> getAllFacilitiesAttachedToUser(int userId);
	public boolean facilityToFacilityCheck(int branchId, String branchName);
	public List<EFmFmFacilityToFacilityMappingPO> getAllBaseClientIdFromBranchName(String branchName);
	public List<EFmFmFacilityToFacilityMappingPO> getAllAttachedActiveFacilities(int branchId);
	public List<EFmFmFacilityToFacilityMappingPO> getAllActiveFacilities();
	public List<EFmFmFacilityToFacilityMappingPO> getAllInActiveFacilities();
	public List<EFmFmFacilityToFacilityMappingPO> getParticularFacilityDetail(int facilityToFacilityoId);
	public List<EFmFmFacilityToFacilityMappingPO> getParticularFacilityDetailFromBranchName(String branchName);
	public List<EFmFmFacilityToFacilityMappingPO> getAllInactiveFacilities();
	
	public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromEmailId(String emailId);
	public List<EFmFmUserMasterPO> getEmpDetailsFromEmployeeIdAndBranchId(String employeeId);
	public List<EFmFmUserMasterPO> getEmpMobileNoDetails(String mobileNo);
	public List<EFmFmUserFacilityMappingPO> getAttachedParticularFacilityDetail(int userId, int branchId);
	public boolean checkFacilityAccess(int userId, int branchId);
	public List<EFmFmUserFacilityMappingPO> getAllFacilitiesAttachedToParticularUser(int userId);

}
