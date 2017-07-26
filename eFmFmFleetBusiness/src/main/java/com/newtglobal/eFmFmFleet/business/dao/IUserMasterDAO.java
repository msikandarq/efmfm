package com.newtglobal.eFmFmFleet.business.dao;

import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmFmAdminCustomMessagePO;
import com.newtglobal.eFmFmFleet.model.EFmFmAdminSentSMSPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchConfigurationMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchSubConfigurationPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientUserRolePO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeModuleMappingWithBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRoleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.PersistentLoginPO;
import com.newtglobal.eFmFmFleet.model.TokenDetails;

public interface IUserMasterDAO {
	/*
	 * start PersistentLoginPO  master details 
	 */
	public void save(PersistentLoginPO  persistentLoginPO);
	public void update(PersistentLoginPO  persistentLoginPO);
	public void delete(PersistentLoginPO  persistentLoginPO);
	//save and update methods for client master
	public void save(EFmFmClientMasterPO  eFmFmClientMasterPO);
	public void update(EFmFmClientMasterPO  eFmFmClientMasterPO);

	public String getUserNamebySeries(String series);
	public PersistentLoginPO PersistentLoginPODettail(String series);
	public void updaetLastrequestTimebyuserName(String userName);
	public int isAleradyLoggedin(String userName);
	public void updatePersistentPO(PersistentLoginPO persistentLoginPO);
	public void delteRecord(String ipAddress);
	public List<PersistentLoginPO> getUserLoggedInDetail(String UserName);
	public PersistentLoginPO getAllLoggedUser(PersistentLoginPO persistentLoginPO);

	/*
	 * end Role master details 
	 */
	/*
	 * start Role master details 
	 */
	public void save(EFmFmRoleMasterPO eFmFmRoleMasterPO);
	public void update(EFmFmRoleMasterPO eFmFmRoleMasterPO);
	public void delete(EFmFmRoleMasterPO eFmFmRoleMasterPO);
	/*
	 * end Role master details 
	 */
	/*
	 * start use master details 
	 */
    public void save(EFmFmUserMasterPO eFmFmUserMasterPO);
	public void update(EFmFmUserMasterPO eFmFmUserMasterPO);
	public void delete(EFmFmUserMasterPO eFmFmUserMasterPO);
	public EFmFmUserMasterPO getUserDetailByUserName(String userName);
	public List<EFmFmRoleMasterPO> getUserRoleByRoleId(int roleId);
	
	/*
	 * start Role EFmFmEmployeeModuleMappingWithBranchPO details 
	 */
	public void save(EFmFmEmployeeModuleMappingWithBranchPO eFmFmEmployeeModuleMappingWithBranchPO);
	public void update(EFmFmEmployeeModuleMappingWithBranchPO eFmFmEmployeeModuleMappingWithBranchPO);
	public void delete(EFmFmEmployeeModuleMappingWithBranchPO eaFmFmEmployeeModuleMappingWithBranchPO);
	/*
	 * end Role EFmFmEmployeeModuleMappingWithBranchPO details 
	 */
	
	/**
	* Get the current Logged in user details
	* 
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-05-28 
	*/
	public List<EFmFmUserMasterPO> getLoggedInUserDetailFromClientIdAndUserId(
			EFmFmUserMasterPO eFmFmUserMaster);
	/*
	 * end Role master details 
	 */
	
	/**
	* Get all the user details of a particular client
	* 
	*
	* @author  Sarfraz Khan
	* 
	* @since   2015-05-28 
	*/
	public List<EFmFmUserMasterPO> getUsersFromClientId(EFmFmUserMasterPO userMasterPO);
	/*
	 * end Role master details 
	 */
	
	public void save(EFmFmClientBranchPO eFmFmClientMasterPO);
	public void update(EFmFmClientBranchPO eFmFmClientMasterPO);
	public List<EFmFmClientUserRolePO> getUserRoleByClientId(int clientId);
	public List<EFmFmRoleMasterPO> getRoleId(String roleName);
	public void save(EFmFmClientUserRolePO eFmFmClientUserRolePO);
	public void update(EFmFmClientUserRolePO eFmFmClientUserRolePO);
	public List<EFmFmClientUserRolePO> getUserRolesFromUserIdAndBranchId(int userId);
	public List<EFmFmUserMasterPO> getAllUsersBelogsProject(int branchId, int projectId);
	public List<EFmFmUserMasterPO> getUsersRoleExist(String branchId,
			String clientProjectId, String role);
	public void deleteAnEmployeeFromData(int userId);
	public List<EFmFmUserMasterPO> getEmployeeUserDetailFromEmployeeId(int branchId,
			String employeeId);
	public List<EFmFmUserMasterPO> getEmployeeUserDetailFromMobileNumber(int branchId,
			String mobileNumber);
	public List<EFmFmUserMasterPO> getRegisterEmployeeDetailFromBranchIdAndUserId(
			int branchId, int userId);
	
	public List<EFmFmClientUserRolePO> getUserModulesByUserIdBranchIdAndModuleId(int userId,int moduleId);
	public List<EFmFmClientBranchConfigurationMappingPO> getBranchMappingDetailsByBranchIdAndModuleId(int branchId,
			int moduleId);
	public List<EFmFmClientBranchConfigurationMappingPO> getAllBranchMappingDetailsByBranchId(String branchId);
	
	public List<EFmFmClientBranchSubConfigurationPO> getSubModulesOfMainModuleByModuleId(int moduleId);
	public List<EFmFmClientUserRolePO> getUserSubModulesByUserIdBranchIdAndSubModuleId(int userId,int moduleId);
	public void removeARole(int userRoleId);
	public List<EFmFmUserMasterPO> getLoggedInUserDetailFromClientIdAndEmployeeId(EFmFmUserMasterPO eFmFmUserMaster);
	public List<EFmFmClientBranchPO> getBranchDetailsFromBranchName(String branchName);
	public List<Integer> getAdminUserRoleByBranchId(int branchId);
	public Integer getBranchDetailsFromBranchId(int branchId);
	public List<EFmFmUserMasterPO> getSpecificUserDetailsByUserName(String userName);
	public List<EFmFmClientUserRolePO> getAdminUserRoleByUserName(String userName);
	public Integer getBranchInvoiceNumberDigitRangeFromBranchId(int branchId);
    public List<EFmFmClientUserRolePO> getUserRoleByUserName(String userName);
	public List<EFmFmUserMasterPO> getUserDetailFromUserId(int userId);
	public List<EFmFmUserMasterPO> getAppDownloadUsersFromBranchId(String branchId,int startPgNo,int endPgNo);
	public List<EFmFmUserMasterPO> getWithOutAppDownloadUsersFromBranchId(String branchId,int startPgNo,int endPgNo);
	public List<EFmFmUserMasterPO> getAppDownloadedButNoGeoCodedFromBranchId(String branchId,int startPgNo,int endPgNo);
	public List<EFmFmUserMasterPO> getAppDownloadedAndGeoCodedUserFromBranchId(String branchId,int startPgNo,int endPgNo);
	public void delteRecordFromSeries(String series);
	public void delteRecordFromUserName(String userName);
	public List<TokenDetails> getAuthorizationToken();	
	public List<TokenDetails> getAuthorizationTokenForParticularUserFromUserId(int userId);
	boolean checkTokenValidOrNot(String existingtoken, int userId);
	public List<EFmFmEmployeeProjectDetailsPO> getListOfProjectId(int branchId, int userId);
	public List<EFmFmRoleMasterPO> getUserRoleByUserId(int userId);
	public List<EFmFmClientProjectDetailsPO> getListOfProjectIdByAdhoc(int branchId);
	public List<EFmFmEmployeeProjectDetailsPO> getListOfRepMngByProjectId(int branchId, int projectId);
	public List<EFmFmUserMasterPO> getNearestEmployeeDetails(String employeeId);
	public List<EFmFmUserMasterPO> getFarthestEmployeeDetails(String employeeId);
	public boolean checkTokenValidOrNotForMobile(String existingtoken, int userId);
	public boolean checkEmployeeUserIdExistOrNot(int userId);
	public List<EFmFmClientUserRolePO> getEmployeeDetailsByRole(String roleName, int branchId);
	
	public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromBranchId(int branchId);
    public void save(EFmFmAdminCustomMessagePO eFmFmAdminCustomMessagePO);
    public void saveMessageSentByMobileNumber(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO);
    public List<EFmFmUserMasterPO> getEmployeeDetailsFromEmployeeIdAndBranchId(StringBuffer employeeIds,int branchId);
    public void save(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO);
    public List<EFmFmUserMasterPO> getEmployeeDetailsFromMobileNumberAndBranchId(StringBuffer mobileNumber,int branchId);
    public List<EFmFmUserMasterPO> getAllGuestDetailsFromBranchId(int branchId);
    public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromShiftDate(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO);
    public List<EFmFmUserMasterPO> getAllEmployeeAndGuestDetailsFromShiftDate(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO);
    public List<EFmFmAdminCustomMessagePO> getAllCustomMessagesFromBranchId(String branchId);
	public List<EFmFmAdminSentSMSPO> getAllSentSMSHistory(int branchId);
	public Integer getBranchIdFromBranchName(String branchName);
	public String getBranchNameFromBranchId(int branchId);
	public List<EFmFmUserMasterPO> getEmployeeUserDetailFromEmployeeIdAndFacilityIds(String branchId, String employeeId);
	public List<EFmFmClientBranchPO> getEscortTimeDetails(String branchId);
	public List<String> getBranchLocationFromBranchId(int branchId);
	public List<EFmFmClientBranchPO> getClientDetails(String branchId);
	public List<EFmFmAdminSentSMSPO> checkEmployeeDetailsByMobileNumber(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO);
	public List<EFmFmAdminSentSMSPO> checkEmployeeDetailsByEmployeeId(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO);
	public List<EFmFmClientBranchPO> getBranchDetailsFromBranchCode(String branchCode);
	public List<EFmFmUserMasterPO> getAllGeoCodedEmployeesList(String branchId);
	public List<EFmFmUserMasterPO> getAllNonGeoCodedEmployeesList(String branchId);
	public List<EFmFmEmployeeModuleMappingWithBranchPO> getAllEmployeeModuleAccessFromBranchId(int branchId);
	public List<EFmFmClientBranchConfigurationMappingPO> getAllBranchMappingDetailsByBranchIdAttchedToThatUser(int branchId);	
	public List<EFmFmUserMasterPO> getWithOutAppDownloadUsersButWebGeocodedFromBranchId(String branchId,int startPgNo,int endPgNo);	
	public List<EFmFmUserMasterPO> getAppDownloadUsersFromBranchId(String branchId);
	public List<EFmFmUserMasterPO> getWithOutAppDownloadUsersFromBranchId(String branchId);
	public List<EFmFmUserMasterPO> getAppDownloadedButNoGeoCodedFromBranchId(String branchId);
	public List<EFmFmUserMasterPO> getAppDownloadedAndGeoCodedUserFromBranchId(String branchId);
	public List<EFmFmUserMasterPO> getWithOutAppDownloadUsersButWebGeocodedFromBranchId(String branchId);
	public List<EFmFmUserMasterPO> getAllNonGeoCodedEmployeesList(String branchId, int startPgNo, int endPgNo);
	public List<EFmFmUserMasterPO> getAllGeoCodedEmployeesList(String branchId, int startPgNo, int endPgNo);
	public List<EFmFmClientBranchPO> getBranchConfigurationDetailsFromBranchId(int branchId);
	public List<EFmFmUserMasterPO> getAllGeoCodedDiffEmployeesList(String branchId, int startPgNo, int endPgNo);
	public List<EFmFmUserMasterPO> getAllGeoCodedDiffEmployeesList(String branchId);
	
}
