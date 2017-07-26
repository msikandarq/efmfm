package com.newtglobal.eFmFmFleet.business.dao;

import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLocationMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserPasswordPO;

public interface IEmployeeDetailDAO {
	/*
	 * Start IEmployeeDetailBO  master details 
	 */
	public void save(EFmFmUserMasterPO eFmFmUserMasterPO);
	public void update(EFmFmUserMasterPO eFmFmUserMasterPO);
	public void delete(EFmFmUserMasterPO eFmFmUserMasterPO);
	public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromClientId(String branchId);
	public boolean doesDeviceExist(String deviceId, int clientId);
	public EFmFmUserMasterPO getParticularDeviceDetails(String deviceId);
	public List<EFmFmUserMasterPO> loginEmployeeDetails(String employeeId,String password);
	public List<EFmFmClientBranchPO> doesClientCodeExist(String branchCode);
	public boolean doesEmailIdExist(String emailId, int clientId);
	public List<EFmFmUserMasterPO> getParticularEmployeeDetailsFromEmailId(
			String emailId);
	public List<EFmFmUserMasterPO> getParticularEmpDetailsFromEmployeeId(
			String employeeId,String branchId);
	public List<EFmFmUserMasterPO> getParticularEmpDetailsFromUserId(int userId,int branchId);
	/*
	 * End IEmployeeDetailBO  master details 
	 */
	public void save (EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO);	
	public List<EFmFmClientProjectDetailsPO> getProjectDetails(String string,String combinedFacility);
	public List<EFmFmUserMasterPO> getEmpMobileNoDetails(String mobileNo,String branchId);
	public List<EFmFmUserMasterPO> getEmpMobileNumberCheck(String mobileNumber,
			int branchId);
	public List<EFmFmUserMasterPO> getParticularEmpDetailsFromEmployeeIdForGuest(
			String employeeId, int branchId);
	public List<EFmFmUserMasterPO> getParticularGuestDetailsFromEmployeeId(
			String employeeId, int branchId);
	public List<EFmFmUserMasterPO> getEmpDetailsFromEmployeeId(String employeeId);
	public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromBranchId(String branchId,int startPgNo,int endPgNo);
	public List<EFmFmUserMasterPO> getEmpDetailsFromEmployeeIdAndBranchId(
			String employeeId, String branchId);
	public List<EFmFmUserMasterPO> getParticularEmpDetailsFromEmployeeIdAndbranchIdAndToken(
			String mobileNumber, String tempCode, int branchId);
	public List<EFmFmUserMasterPO> getParticularEmpDetailsFromUserIdWithOutStatus(int userId, String branchId); 
	public List<EFmFmUserMasterPO> loginEmployeeDetailsFromMobileNumber(String mobileNumber, String password);
	public List<EFmFmUserMasterPO> getEmpDetailsFromMobileNumberAndBranchId(String mobileNumber, String branchId);
	public List<EFmFmUserMasterPO> getParticularEmployeeDetailsFromDeviceIdAndMobileNumber(String deviceId, int branchId,
			String mobileNumber);
	public List<EFmFmUserMasterPO> getParticularEmpDetailsFromMobileNumberAndBranhId(String mobileNumber, int branchId);
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromMobileNumber(String mobileNumber);
    public void save(EFmFmUserPasswordPO eFmFmUserPasswordPO);
    public void update(EFmFmUserPasswordPO eFmFmUserPasswordPO);
    public List<EFmFmUserPasswordPO> getUserPasswordDetailsFromUserIdAndBranchId(int userId);
    public void deleteLastPasswordForParticularEmployeCrossingDefineLimit(int passwordId);
    public List<EFmFmUserMasterPO> getEmployeeTypeDetailsByBranchId(String userType, String branchId);
	public List<EFmFmUserMasterPO> getParticularEmpDetailsFromUserName(String userName, int branchId);
	
	public void saveLocationMaster(EFmFmLocationMasterPO eFmFmLocationMasterPO);
	public void updateLocationMaster(EFmFmLocationMasterPO eFmFmLocationMasterPO);
	public List<EFmFmLocationMasterPO> getLocationNameExist(String locationName,String branchId);	
	public List<EFmFmLocationMasterPO> getMultipleLocation(String locationId,String branchId);	
	
	public List<EFmFmLocationMasterPO> getAllActiveLocation(String isActive, String branchId);
	
	public List<EFmFmEmployeeProjectDetailsPO> getListOfProjectIdByUserId(int userId,String branchId);
	
	public List<EFmFmEmployeeProjectDetailsPO> getListOfUserByreportingManager(int reportingManagerUserId, String branchId,int userId);
	public List<EFmFmEmployeeProjectDetailsPO> getAllProjectUserByrepManager(int reportingManagerUserId, int branchId);
	public List<EFmFmEmployeeProjectDetailsPO> getAllProjectUserByrepManagerWithProjectId(int reportingManagerUserId,
			String branchId, int projectId);
	public List<EFmFmEmployeeProjectDetailsPO> getClientProjectIdByMangerAndEmployee(int reportingManagerUserId, String branchId,
			int projectId, int userId);
	
	public void addEmployeeProjectDetails(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO);
	public void updateEmployeeProjectDetails(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO);	
	public List<EFmFmClientProjectDetailsPO> getListOfProjectDetails(String activeStatus,String branchId);
	public List<EFmFmClientProjectDetailsPO> getParticularProjectDetails(int projectId,String branchId);
	public void updateClientProject(EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO);	
	public List<EFmFmEmployeeProjectDetailsPO> getAllEmployeeByProjectId(int branchId,int projectId);
	public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromEmailId(String emailId, String branchId);
	public List<EFmFmUserMasterPO> getListOfEmployeeDetailsByBranch(String branchId);
	public List<EFmFmEmployeeProjectDetailsPO> getDeligatedUserDetails(String branchId, int repMngUserId);
	public List<EFmFmEmployeeProjectDetailsPO> getAllDeligatedUserDetails(String branchId);
	public boolean doesCommanUseNameExist(String userName);
	public List<EFmFmEmployeeProjectDetailsPO> getDeligatedUserDetailsByReportingManager(int branchId, int delegatedBy,
			int repManagerId, int projectId);
	public List<EFmFmEmployeeProjectDetailsPO> getParticularProjectAllocation(int delegatedId);
	public boolean doesCommanEmployeeIdExist(String employeeId);
	public boolean doesCommanMobileNumberExist(String mobileNumber);
	public boolean doesCommanEmailIdExist(String emailId);
	public List<EFmFmLocationMasterPO> getAllActiveAndPendingLocation(int branchId);
	public List<EFmFmUserMasterPO> getAllEmployeeDetailsByPagination(String branchId, int startPgNo, int endPgNo);
	public List<EFmFmUserMasterPO> getParticularEmployeeDetailsFromUserName(String userName);
	
}
