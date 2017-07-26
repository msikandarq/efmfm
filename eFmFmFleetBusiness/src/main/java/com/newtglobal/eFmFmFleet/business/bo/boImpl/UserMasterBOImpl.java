package com.newtglobal.eFmFmFleet.business.bo.boImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtglobal.eFmFmFleet.business.bo.IUserMasterBO;
import com.newtglobal.eFmFmFleet.business.dao.IUserMasterDAO;
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

@Service("IUserMasterBO")
public class UserMasterBOImpl implements IUserMasterBO {

    @Autowired
    IUserMasterDAO iUserMasterDAO;

    public void setIUserMasterDAO(IUserMasterDAO iUserMasterDAO) {
        this.iUserMasterDAO = iUserMasterDAO;
    }

    @Override
    public void save(EFmFmUserMasterPO eFmFmUserMasterPO) {
        iUserMasterDAO.save(eFmFmUserMasterPO);

    }

    @Override
    public void update(EFmFmUserMasterPO eFmFmUserMasterPO) {
        iUserMasterDAO.update(eFmFmUserMasterPO);
    }

    @Override
    public void delete(EFmFmUserMasterPO eFmFmUserMasterPO) {
        iUserMasterDAO.delete(eFmFmUserMasterPO);
    }
    
  
    @Override
    public void save(EFmFmEmployeeModuleMappingWithBranchPO eFmFmEmployeeModuleMappingWithBranchPO) {
        iUserMasterDAO.save(eFmFmEmployeeModuleMappingWithBranchPO);

    }

    @Override
    public void update(EFmFmEmployeeModuleMappingWithBranchPO eFmFmEmployeeModuleMappingWithBranchPO) {
        iUserMasterDAO.update(eFmFmEmployeeModuleMappingWithBranchPO);
    }

    @Override
    public void delete(EFmFmEmployeeModuleMappingWithBranchPO eFmFmEmployeeModuleMappingWithBranchPO) {
        iUserMasterDAO.delete(eFmFmEmployeeModuleMappingWithBranchPO);
    }
    
    

    @Override
    public void save(EFmFmClientMasterPO eFmFmClientMasterPO) {
        iUserMasterDAO.save(eFmFmClientMasterPO);

    }

    @Override
    public void update(EFmFmClientMasterPO eFmFmClientMasterPO) {
        iUserMasterDAO.update(eFmFmClientMasterPO);

    }

    @Override
    public List<EFmFmRoleMasterPO> getUserRoleByRoleId(int roleId) {
        return iUserMasterDAO.getUserRoleByRoleId(roleId);
    }

    @Override
    public EFmFmUserMasterPO getUserDetailByUserName(String userName) {
        return iUserMasterDAO.getUserDetailByUserName(userName);
    }

    @Override
    public void save(PersistentLoginPO persistentLoginPO) {
        iUserMasterDAO.save(persistentLoginPO);
    }

    @Override
    public void update(PersistentLoginPO persistentLoginPO) {
        iUserMasterDAO.update(persistentLoginPO);

    }

    @Override
    public void delete(PersistentLoginPO persistentLoginPO) {
        iUserMasterDAO.delete(persistentLoginPO);
    }

    @Override
    public String getUserNamebySeries(String series) {
        return iUserMasterDAO.getUserNamebySeries(series);
    }

    @Override
    public void updaetLastrequestTimebyuserName(String userName) {
        iUserMasterDAO.updaetLastrequestTimebyuserName(userName);
    }

    @Override
    public int isAleradyLoggedin(String userName) {
        return iUserMasterDAO.isAleradyLoggedin(userName);
    }

    @Override
    public void updatePersistentPO(PersistentLoginPO persistentLoginPO) {
        iUserMasterDAO.updatePersistentPO(persistentLoginPO);
    }

    @Override
    public void delteRecord(String ipAddress) {
        iUserMasterDAO.delteRecord(ipAddress);
    }

    @Override
    public List<PersistentLoginPO> getUserLoggedInDetail(String UserName) {
        return iUserMasterDAO.getUserLoggedInDetail(UserName);
    }

    @Override
    public PersistentLoginPO getAllLoggedUser(PersistentLoginPO persistentLoginPO) {
        return iUserMasterDAO.getAllLoggedUser(persistentLoginPO);
    }

    @Override
    public void save(EFmFmRoleMasterPO eFmFmRoleMasterPO) {
        iUserMasterDAO.save(eFmFmRoleMasterPO);
    }

    @Override
    public void update(EFmFmRoleMasterPO eFmFmRoleMasterPO) {
        iUserMasterDAO.update(eFmFmRoleMasterPO);

    }

    @Override
    public void delete(EFmFmRoleMasterPO eFmFmRoleMasterPO) {
        iUserMasterDAO.delete(eFmFmRoleMasterPO);
    }

    @Override
    public List<EFmFmUserMasterPO> getLoggedInUserDetailFromClientIdAndUserId(EFmFmUserMasterPO eFmFmUserMaster) {
        return iUserMasterDAO.getLoggedInUserDetailFromClientIdAndUserId(eFmFmUserMaster);
    }

    @Override
    public List<EFmFmUserMasterPO> getUsersFromClientId(EFmFmUserMasterPO userMasterPO) {
        return iUserMasterDAO.getUsersFromClientId(userMasterPO);
    }

    @Override
    public List<EFmFmClientBranchPO> getClientDetails(String clientId) {
        return iUserMasterDAO.getClientDetails(clientId);
    }

    @Override
    public void save(EFmFmClientBranchPO eFmFmClientMasterPO) {
        iUserMasterDAO.save(eFmFmClientMasterPO);
    }

    @Override
    public void update(EFmFmClientBranchPO eFmFmClientMasterPO) {
        iUserMasterDAO.update(eFmFmClientMasterPO);
    }

    @Override
    public List<EFmFmClientUserRolePO> getUserRoleByClientId(int clientId) {
        return iUserMasterDAO.getUserRoleByClientId(clientId);
    }

    @Override
    public List<EFmFmRoleMasterPO> getRoleId(String roleName) {
        return iUserMasterDAO.getRoleId(roleName);
    }

    @Override
    public void save(EFmFmClientUserRolePO eFmFmClientUserRolePO) {
        iUserMasterDAO.save(eFmFmClientUserRolePO);
    }

    @Override
    public void update(EFmFmClientUserRolePO eFmFmClientUserRolePO) {
        iUserMasterDAO.update(eFmFmClientUserRolePO);
    }

    @Override
    public List<EFmFmClientUserRolePO> getUserRolesFromUserIdAndBranchId(int userId) {
        return iUserMasterDAO.getUserRolesFromUserIdAndBranchId(userId);
    }

    @Override
    public PersistentLoginPO PersistentLoginPODettail(String series) {
        return iUserMasterDAO.PersistentLoginPODettail(series);
    }

    @Override
    public List<EFmFmUserMasterPO> getAllUsersBelogsProject(int branchId, int projectId) {
        return iUserMasterDAO.getAllUsersBelogsProject(branchId, projectId);
    }

    @Override
    public List<EFmFmUserMasterPO> getUsersRoleExist(String branchId, String clientProjectId, String role) {
        return iUserMasterDAO.getUsersRoleExist(branchId, clientProjectId, role);
    }

    @Override
    public void deleteAnEmployeeFromData(int userId) {
        iUserMasterDAO.deleteAnEmployeeFromData(userId);
    }

    @Override
    public List<EFmFmUserMasterPO> getEmployeeUserDetailFromEmployeeId(int branchId, String employeeId) {
        return iUserMasterDAO.getEmployeeUserDetailFromEmployeeId(branchId, employeeId);
    }

    @Override
    public List<EFmFmUserMasterPO> getEmployeeUserDetailFromMobileNumber(int branchId, String mobileNumber) {
        return iUserMasterDAO.getEmployeeUserDetailFromMobileNumber(branchId, mobileNumber);
    }

    @Override
    public List<EFmFmUserMasterPO> getRegisterEmployeeDetailFromBranchIdAndUserId(int branchId, int userId) {
        return iUserMasterDAO.getRegisterEmployeeDetailFromBranchIdAndUserId(branchId, userId);
    }

    @Override
    public List<EFmFmClientUserRolePO> getUserModulesByUserIdBranchIdAndModuleId(int userId,
            int moduleId) {
        return iUserMasterDAO.getUserModulesByUserIdBranchIdAndModuleId(userId, moduleId);
    }

    @Override
    public List<EFmFmClientBranchConfigurationMappingPO> getBranchMappingDetailsByBranchIdAndModuleId(int branchId,
            int moduleId) {
        return iUserMasterDAO.getBranchMappingDetailsByBranchIdAndModuleId(branchId, moduleId);
    }

    @Override
    public List<EFmFmClientBranchConfigurationMappingPO> getAllBranchMappingDetailsByBranchId(String branchId) {
        return iUserMasterDAO.getAllBranchMappingDetailsByBranchId(branchId);
    }

    @Override
    public List<EFmFmClientBranchSubConfigurationPO> getSubModulesOfMainModuleByModuleId(int moduleId) {
        return iUserMasterDAO.getSubModulesOfMainModuleByModuleId(moduleId);
    }

    @Override
    public List<EFmFmClientUserRolePO> getUserSubModulesByUserIdBranchIdAndSubModuleId(int userId,
            int moduleId) {
        return iUserMasterDAO.getUserSubModulesByUserIdBranchIdAndSubModuleId(userId, moduleId);
    }

    @Override
    public void removeARole(int userRoleId) {
        iUserMasterDAO.removeARole(userRoleId);
    }

    @Override
    public List<EFmFmUserMasterPO> getLoggedInUserDetailFromClientIdAndEmployeeId(EFmFmUserMasterPO eFmFmUserMaster) {
        return iUserMasterDAO.getLoggedInUserDetailFromClientIdAndEmployeeId(eFmFmUserMaster);
    }

    @Override
    public List<EFmFmClientBranchPO> getBranchDetailsFromBranchName(String branchName) {
        return iUserMasterDAO.getBranchDetailsFromBranchName(branchName);
    }

    @Override
    public List<Integer> getAdminUserRoleByBranchId(int branchId) {
        return iUserMasterDAO.getAdminUserRoleByBranchId(branchId);
    }

    @Override
    public Integer getBranchDetailsFromBranchId(int branchId) {
        return iUserMasterDAO.getBranchDetailsFromBranchId(branchId);
    }

    @Override
    public List<EFmFmUserMasterPO> getSpecificUserDetailsByUserName(String userName) {
        return iUserMasterDAO.getSpecificUserDetailsByUserName(userName);
    }

    @Override
    public List<EFmFmClientUserRolePO> getAdminUserRoleByUserName(String userName) {
        return iUserMasterDAO.getAdminUserRoleByUserName(userName);
    }

    @Override
    public Integer getBranchInvoiceNumberDigitRangeFromBranchId(int branchId) {
        return iUserMasterDAO.getBranchInvoiceNumberDigitRangeFromBranchId(branchId);
    }

    @Override
    public List<EFmFmClientUserRolePO> getUserRoleByUserName(String userName) {
       
        return iUserMasterDAO.getUserRoleByUserName(userName);
    }

	@Override
	public List<EFmFmUserMasterPO> getUserDetailFromUserId(int userId) {
		return iUserMasterDAO.getUserDetailFromUserId(userId);
	}

	@Override
	public List<EFmFmUserMasterPO> getAppDownloadUsersFromBranchId(String branchId,int startPgNo,int endPgNo) {
		return iUserMasterDAO.getAppDownloadUsersFromBranchId(branchId,startPgNo,endPgNo);
	}

	@Override
	public List<EFmFmUserMasterPO> getWithOutAppDownloadUsersFromBranchId(String branchId,int startPgNo,int endPgNo) {
		return iUserMasterDAO.getWithOutAppDownloadUsersFromBranchId(branchId,startPgNo,endPgNo);
	}

	@Override
	public List<EFmFmUserMasterPO> getAppDownloadedButNoGeoCodedFromBranchId(String branchId,int startPgNo,int endPgNo) {
		return iUserMasterDAO.getAppDownloadedButNoGeoCodedFromBranchId(branchId,startPgNo,endPgNo);
	}

	@Override
	public List<EFmFmUserMasterPO> getAppDownloadedAndGeoCodedUserFromBranchId(String branchId,int startPgNo,int endPgNo) {
		return iUserMasterDAO.getAppDownloadedAndGeoCodedUserFromBranchId(branchId,startPgNo,endPgNo);
	}

	@Override
	public void delteRecordFromSeries(String series) {
		iUserMasterDAO.delteRecordFromSeries(series);
	}

	@Override
	public void delteRecordFromUserName(String userName) {
		iUserMasterDAO.delteRecordFromUserName(userName);
	}

	@Override
	public List<TokenDetails> getAuthorizationToken() {
		return iUserMasterDAO.getAuthorizationToken();
	}

	@Override
	public	boolean checkTokenValidOrNot(String existingtoken, int userId){
		return iUserMasterDAO.checkTokenValidOrNot(existingtoken, userId);
				
	}

	@Override
	public List<TokenDetails> getAuthorizationTokenForParticularUserFromUserId(int userId) {
		return iUserMasterDAO.getAuthorizationTokenForParticularUserFromUserId(userId);
	}
	
	@Override
	public List<EFmFmEmployeeProjectDetailsPO> getListOfProjectId(int branchId, int userId) {
		
		return iUserMasterDAO.getListOfProjectId(branchId, userId);
	}

	@Override
	public List<EFmFmRoleMasterPO> getUserRoleByUserId(int userId) {
		
		return iUserMasterDAO.getUserRoleByUserId(userId);
	}

	@Override
	public List<EFmFmClientProjectDetailsPO> getListOfProjectIdByAdhoc(int branchId) {
		
		return iUserMasterDAO.getListOfProjectIdByAdhoc(branchId);
	}

	@Override
	public List<EFmFmEmployeeProjectDetailsPO> getListOfRepMngByProjectId(int branchId, int projectId) {
		
		return iUserMasterDAO.getListOfRepMngByProjectId(branchId, projectId);
	}
	
	@Override
	public List<EFmFmUserMasterPO> getNearestEmployeeDetails(String employeeId) {
		return iUserMasterDAO.getNearestEmployeeDetails(employeeId);
	}

	@Override
	public List<EFmFmUserMasterPO> getFarthestEmployeeDetails(String employeeId) {
		return iUserMasterDAO.getFarthestEmployeeDetails(employeeId);
	}

	@Override
	public boolean checkTokenValidOrNotForMobile(String existingtoken, int userId) {
		return iUserMasterDAO.checkTokenValidOrNotForMobile(existingtoken, userId);
	}

	@Override
	public boolean checkEmployeeUserIdExistOrNot(int userId) {
		return iUserMasterDAO.checkEmployeeUserIdExistOrNot(userId);
	}

	@Override
	public List<EFmFmClientUserRolePO> getEmployeeDetailsByRole(String roleName, int branchId) {
		return iUserMasterDAO.getEmployeeDetailsByRole(roleName, branchId);
	}


	@Override
	public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromBranchId(int branchId) {

	return iUserMasterDAO.getAllEmployeeDetailsFromBranchId(branchId);
	}

	@Override
	public void save(EFmFmAdminCustomMessagePO eFmFmAdminCustomMessagePO) {
		iUserMasterDAO.save(eFmFmAdminCustomMessagePO);
		
	}

	@Override
	public List<EFmFmUserMasterPO> getEmployeeDetailsFromEmployeeIdAndBranchId(StringBuffer employeeIds,int branchId) {
		return iUserMasterDAO.getEmployeeDetailsFromEmployeeIdAndBranchId(employeeIds,branchId);
		
		
	}

	@Override
	public void save(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		iUserMasterDAO.save(eFmFmAdminSentSMSPO);
	}

	@Override
	public List<EFmFmUserMasterPO> getEmployeeDetailsFromMobileNumberAndBranchId(StringBuffer mobileNumber,int branchId) {
		return iUserMasterDAO.getEmployeeDetailsFromMobileNumberAndBranchId(mobileNumber,branchId);
  }

	@Override
	public List<EFmFmUserMasterPO> getAllGuestDetailsFromBranchId(int branchId) {
		return iUserMasterDAO.getAllGuestDetailsFromBranchId(branchId);
	}

	@Override
	public List<EFmFmAdminCustomMessagePO> getAllCustomMessagesFromBranchId(String branchId) {
		return iUserMasterDAO.getAllCustomMessagesFromBranchId(branchId);
	}

	@Override
	public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromShiftDate(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		return iUserMasterDAO.getAllEmployeeDetailsFromShiftDate(eFmFmAdminSentSMSPO);
	}

	@Override
	public List<EFmFmUserMasterPO> getAllEmployeeAndGuestDetailsFromShiftDate(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		return  iUserMasterDAO.getAllEmployeeAndGuestDetailsFromShiftDate(eFmFmAdminSentSMSPO);
	}

	@Override
	public void saveMessageSentByMobileNumber(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		iUserMasterDAO.saveMessageSentByMobileNumber(eFmFmAdminSentSMSPO);		
	}

	@Override
	public List<EFmFmAdminSentSMSPO> getAllSentSMSHistory(int branchId) {
		return	iUserMasterDAO.getAllSentSMSHistory(branchId);
	}

	@Override
	public Integer getBranchIdFromBranchName(String branchName) {
		return iUserMasterDAO.getBranchIdFromBranchName(branchName);
	}

	@Override
	public String getBranchNameFromBranchId(int branchId) {
		return iUserMasterDAO.getBranchNameFromBranchId(branchId);
	}

	@Override
	public List<EFmFmUserMasterPO> getEmployeeUserDetailFromEmployeeIdAndFacilityIds(String branchId,
			String employeeId) {
		return iUserMasterDAO.getEmployeeUserDetailFromEmployeeIdAndFacilityIds(branchId, employeeId);
	}

	@Override
	public List<EFmFmClientBranchPO> getEscortTimeDetails(String branchId) {
		return iUserMasterDAO.getEscortTimeDetails(branchId);
	}

	@Override
	public List<String> getBranchLocationFromBranchId(int branchId) {
		return iUserMasterDAO.getBranchLocationFromBranchId(branchId);
	}

	@Override
	public List<EFmFmAdminSentSMSPO> checkEmployeeDetailsByMobileNumber(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		return iUserMasterDAO.checkEmployeeDetailsByMobileNumber(eFmFmAdminSentSMSPO);
	}

	@Override
	public List<EFmFmAdminSentSMSPO> checkEmployeeDetailsByEmployeeId(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		return iUserMasterDAO.checkEmployeeDetailsByEmployeeId(eFmFmAdminSentSMSPO);
	}

    @Override
	public List<EFmFmClientBranchPO> getBranchDetailsFromBranchCode(String branchCode) {
		return iUserMasterDAO.getBranchDetailsFromBranchCode(branchCode);
	}

	@Override
	public List<EFmFmUserMasterPO> getAllGeoCodedEmployeesList(String branchId) {
		return iUserMasterDAO.getAllGeoCodedEmployeesList(branchId);
	}

	@Override
	public List<EFmFmUserMasterPO> getAllNonGeoCodedEmployeesList(String branchId) {
		return iUserMasterDAO.getAllNonGeoCodedEmployeesList(branchId);
	}

	@Override
	public List<EFmFmEmployeeModuleMappingWithBranchPO> getAllEmployeeModuleAccessFromBranchId(int branchId) {
		return iUserMasterDAO.getAllEmployeeModuleAccessFromBranchId(branchId);
	}

	@Override
	public List<EFmFmClientBranchConfigurationMappingPO> getAllBranchMappingDetailsByBranchIdAttchedToThatUser(
			int branchId) {
		return iUserMasterDAO.getAllBranchMappingDetailsByBranchIdAttchedToThatUser(branchId);
	}

	@Override
	public List<EFmFmUserMasterPO> getWithOutAppDownloadUsersButWebGeocodedFromBranchId(String branchId, int startPgNo,int endPgNo) {		
		return iUserMasterDAO.getWithOutAppDownloadUsersButWebGeocodedFromBranchId(branchId, startPgNo, endPgNo);
	}	

	@Override
	public List<EFmFmUserMasterPO> getAppDownloadUsersFromBranchId(String branchId) {
		return iUserMasterDAO.getAppDownloadUsersFromBranchId(branchId);
	}

	@Override
	public List<EFmFmUserMasterPO> getWithOutAppDownloadUsersFromBranchId(String branchId) {
		return iUserMasterDAO.getWithOutAppDownloadUsersFromBranchId(branchId);
	}

	@Override
	public List<EFmFmUserMasterPO> getAppDownloadedButNoGeoCodedFromBranchId(String branchId) {
		return iUserMasterDAO.getAppDownloadedButNoGeoCodedFromBranchId(branchId);
	}

	@Override
	public List<EFmFmUserMasterPO> getAppDownloadedAndGeoCodedUserFromBranchId(String branchId) {
		return iUserMasterDAO.getAppDownloadedAndGeoCodedUserFromBranchId(branchId);
	}

	@Override
	public List<EFmFmUserMasterPO> getWithOutAppDownloadUsersButWebGeocodedFromBranchId(String branchId) {
		return iUserMasterDAO.getWithOutAppDownloadUsersButWebGeocodedFromBranchId(branchId);
	}

	@Override
	public List<EFmFmUserMasterPO> getAllNonGeoCodedEmployeesList(String branchId, int startPgNo, int endPgNo) {
		return iUserMasterDAO.getAllNonGeoCodedEmployeesList(branchId, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmUserMasterPO> getAllGeoCodedEmployeesList(String branchId, int startPgNo, int endPgNo) {
		return iUserMasterDAO.getAllGeoCodedEmployeesList(branchId, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmClientBranchPO> getBranchConfigurationDetailsFromBranchId(int branchId) {
		return iUserMasterDAO.getBranchConfigurationDetailsFromBranchId(branchId);
	}

	@Override
	public List<EFmFmUserMasterPO> getAllGeoCodedDiffEmployeesList(String branchId, int startPgNo, int endPgNo) {
		// TODO Auto-generated method stub
		return iUserMasterDAO.getAllGeoCodedDiffEmployeesList(branchId, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmUserMasterPO> getAllGeoCodedDiffEmployeesList(String branchId) {
		// TODO Auto-generated method stub
		return iUserMasterDAO.getAllGeoCodedDiffEmployeesList(branchId);
	}



}
