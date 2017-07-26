package com.newtglobal.eFmFmFleet.business.dao.daoImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

@Repository("IUserMasterDAO")
public class UserMasterDAOImpl implements IUserMasterDAO {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager _entityManager) {
		this.entityManager = _entityManager;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmClientBranchPO eFmFmClientMasterPO) {
		entityManager.persist(eFmFmClientMasterPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmUserMasterPO eFmFmUserMasterPO) {
		entityManager.persist(eFmFmUserMasterPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmClientBranchPO eFmFmClientMasterPO) {
		entityManager.merge(eFmFmClientMasterPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmUserMasterPO eFmFmUserMasterPO) {
		entityManager.merge(eFmFmUserMasterPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(EFmFmUserMasterPO eFmFmUserMasterPO) {
		entityManager.remove(eFmFmUserMasterPO);
	}

	/*
	 * Client User Role
	 * 
	 */

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmClientUserRolePO eFmFmClientUserRolePO) {
		entityManager.merge(eFmFmClientUserRolePO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmClientUserRolePO eFmFmClientUserRolePO) {
		entityManager.merge(eFmFmClientUserRolePO);
	}

	@Override
	public void save(PersistentLoginPO persistentLoginPO) {
		entityManager.persist(persistentLoginPO);

	}

	@Override
	public void update(PersistentLoginPO persistentLoginPO) {
		entityManager.merge(persistentLoginPO);
	}

	@Override
	public void delete(PersistentLoginPO persistentLoginPO) {
		entityManager.remove(persistentLoginPO);
	}

	@Override
	public void save(EFmFmClientMasterPO eFmFmClientMasterPO) {
		entityManager.persist(eFmFmClientMasterPO);

	}
	
	
	
	
	@Override
	public void update(EFmFmEmployeeModuleMappingWithBranchPO eFmFmEmployeeModuleMappingWithBranchPO) {
		entityManager.merge(eFmFmEmployeeModuleMappingWithBranchPO);
	}

	@Override
	public void delete(EFmFmEmployeeModuleMappingWithBranchPO eFmFmEmployeeModuleMappingWithBranchPO) {
		entityManager.remove(eFmFmEmployeeModuleMappingWithBranchPO);
	}

	@Override
	public void save(EFmFmEmployeeModuleMappingWithBranchPO eFmFmEmployeeModuleMappingWithBranchPO) {
		entityManager.persist(eFmFmEmployeeModuleMappingWithBranchPO);

	}

	
	
	

	@Override
	public void update(EFmFmClientMasterPO eFmFmClientMasterPO) {
		entityManager.merge(eFmFmClientMasterPO);
	}

	@Override
	public void save(EFmFmRoleMasterPO eFmFmRoleMasterPO) {
		entityManager.persist(eFmFmRoleMasterPO);
	}

	@Override
	public void update(EFmFmRoleMasterPO eFmFmRoleMasterPO) {
		entityManager.merge(eFmFmRoleMasterPO);
	}

	@Override
	public void delete(EFmFmRoleMasterPO eFmFmRoleMasterPO) {
		entityManager.remove(eFmFmRoleMasterPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteAnEmployeeFromData(int userId) {
		Query query = entityManager.createQuery("DELETE EFmFmUserMasterPO where userId = '" + userId + "' ");
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public EFmFmUserMasterPO getUserDetailByUserName(String userName) {
		List<EFmFmUserMasterPO> userDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmUserMasterPO as b where b.userName='" + userName + "'");
		userDetail = query.getResultList();
		if (userDetail.isEmpty() || userDetail.size() == 0)
			return null;
		return userDetail.get(0);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getSpecificUserDetailsByUserName(String userName) {
		List<EFmFmUserMasterPO> userDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmUserMasterPO as b where b.userName='" + userName + "'");
		userDetail = query.getResultList();
		return userDetail;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String getUserNamebySeries(String series) {
		List<String> userNameList = new ArrayList<String>();
		Query query = entityManager.createNativeQuery("SELECT userName FROM persistent_logins WHERE series = ?1");
		query.setParameter(1, series);
		try {
			userNameList = query.getResultList();
		} catch (Exception e) {

		}
		if (userNameList.isEmpty()) {
			return null;
		}
		return userNameList.get(0);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public PersistentLoginPO PersistentLoginPODettail(String series) {
		List<PersistentLoginPO> userDetail = new ArrayList<PersistentLoginPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM PersistentLoginPO as b where b.series='" + series + "'  ORDER BY b.lastUsed DESC");
		userDetail = query.getResultList();
		if (userDetail.isEmpty() || userDetail.size() == 0) {
			return null;
		}
		return userDetail.get(0);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updaetLastrequestTimebyuserName(String userName) {
		Query query = entityManager
				.createQuery("UPDATE PersistentLoginPO set lastUsed=NOW() WHERE userName = '" + userName + "'");
		query.executeUpdate();

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int isAleradyLoggedin(String userName) {
		String s = entityManager
				.createQuery("SELECT COUNT(p) FROM PersistentLoginPO as p WHERE p.userName = '" + userName + "' ")
				.getSingleResult().toString();
		return Integer.valueOf(s);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updatePersistentPO(PersistentLoginPO persistentLoginPO) {
		entityManager.merge(persistentLoginPO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delteRecord(String ipAddress) {
		Query query = entityManager
				.createQuery("DELETE FROM PersistentLoginPO c WHERE c.ipAddress='" + ipAddress + "'");
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delteRecordFromUserName(String userName) {
		Query query = entityManager
				.createQuery("DELETE FROM PersistentLoginPO c WHERE  c.userName = '" + userName + "' ");
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delteRecordFromSeries(String series) {
		Query query = entityManager.createQuery("DELETE FROM PersistentLoginPO c WHERE c.series='" + series + "'");
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<PersistentLoginPO> getUserLoggedInDetail(String UserName) {
		List<PersistentLoginPO> userDetail = new ArrayList<PersistentLoginPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM PersistentLoginPO as b where b.userName='" + UserName + "'  ORDER BY b.lastUsed DESC");
		userDetail = query.getResultList();
		return userDetail;
	}

	// Method for checking branchName exist or not
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientBranchPO> getBranchDetailsFromBranchName(String branchName) {
		List<EFmFmClientBranchPO> clientDetail = new ArrayList<EFmFmClientBranchPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmClientBranchPO as b where UPPER(REPLACE(b.branchName,' ',''))=TRIM(UPPER(REPLACE('"+branchName+"',' ','')))");
		clientDetail = query.getResultList();
		return clientDetail;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientBranchPO> getBranchDetailsFromBranchCode(String branchCode) {
		List<EFmFmClientBranchPO> clientDetail = new ArrayList<EFmFmClientBranchPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmClientBranchPO as b where UPPER(REPLACE(b.branchCode,' ',''))=TRIM(UPPER(REPLACE('"+branchCode+"',' ','')))");
		clientDetail = query.getResultList();
		return clientDetail;
	}
	
	
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public PersistentLoginPO getAllLoggedUser(PersistentLoginPO persistentLoginPO) {
		List<PersistentLoginPO> userDetails = new ArrayList<PersistentLoginPO>();
		Query query = entityManager.createQuery("SELECT b FROM PersistentLoginPO as b ORDER BY b.lastUsed DESC");
		userDetails = query.getResultList();
		return userDetails.get(0);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmRoleMasterPO> getUserRoleByRoleId(int roleId) {
		List<EFmFmRoleMasterPO> roleDetail = new ArrayList<EFmFmRoleMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT r FROM EFmFmClientUserRolePO as r JOIN r.eFmFmUserMaster u where u.userId='" + roleId + "'");
		roleDetail = query.getResultList();
		return roleDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientUserRolePO> getUserRoleByClientId(int branchId) {
		List<EFmFmClientUserRolePO> roleDetail = new ArrayList<EFmFmClientUserRolePO>();
		Query query = entityManager
				.createQuery("SELECT r FROM EFmFmClientUserRolePO as r JOIN r.eFmFmClientBranchPO c where c.branchId='"
						+ branchId + "'");
		roleDetail = query.getResultList();
		return roleDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Integer> getAdminUserRoleByBranchId(int branchId) {
		List<Integer> adminCount = new ArrayList<Integer>();
		Query query = entityManager.createQuery(
				"SELECT distinct(u.userId) FROM EFmFmClientUserRolePO as r JOIN r.eFmFmClientBranchPO c JOIN r.efmFmRoleMaster rl JOIN r.efmFmUserMaster u where c.branchId='"
						+ branchId + "' and rl.role='admin' ");
		adminCount = query.getResultList();
		return adminCount;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientUserRolePO> getAdminUserRoleByUserName(String userName) {
		List<EFmFmClientUserRolePO> userDetais = new ArrayList<EFmFmClientUserRolePO>();
		Query query = entityManager.createQuery(
				"SELECT r FROM EFmFmClientUserRolePO as r  JOIN r.efmFmRoleMaster rl JOIN r.efmFmUserMaster u where rl.role='admin' and u.userName='"
						+ userName + "' ");
		userDetais = query.getResultList();
		return userDetais;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientUserRolePO> getUserRoleByUserName(String userName) {
		List<EFmFmClientUserRolePO> userDetais = new ArrayList<EFmFmClientUserRolePO>();
		Query query = entityManager
				.createQuery("SELECT r FROM EFmFmClientUserRolePO as r JOIN r.efmFmUserMaster u where u.userName='"
						+ userName + "' ");
		userDetais = query.getResultList();
		return userDetais;
	}

	// Method for checking number of adminstrators exist or not
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Integer getBranchDetailsFromBranchId(int branchId) {
		List<Integer> clientDetail = new ArrayList<Integer>();
		Query query = entityManager.createQuery(
				"SELECT b.numberOfAdministarator FROM EFmFmClientBranchPO as b where b.branchId='" + branchId + "' ");
		clientDetail = query.getResultList();
		return clientDetail.get(0);
	}

	// Method for getting authorizationToken
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<TokenDetails> getAuthorizationToken() {
		Query query = entityManager.createQuery(
				"SELECT b.authorizationToken,b.takenGenrationTime,b.branchCode FROM EFmFmClientBranchPO as b ");
		List<Object[]> clientDetail = query.getResultList();
		List<TokenDetails> tokenDetail = new ArrayList<TokenDetails>();

		if (!(clientDetail.isEmpty())) {
			TokenDetails token = new TokenDetails();
			token.setAuthorizationToken((String) clientDetail.get(0)[0]);
			token.setTakenGenrationTime((Date) clientDetail.get(0)[1]);
			token.setBranchCode((String) clientDetail.get(0)[2]);
			tokenDetail.add(token);
		}
		return tokenDetail;
	}

	// Method for getting authorizationToken from usermaster
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<TokenDetails> getAuthorizationTokenForParticularUserFromUserId(int userId) {
		Query query = entityManager.createQuery(
				"SELECT b.authorizationToken,b.tokenGenerationTime FROM EFmFmUserMasterPO as b where b.userId='"
						+ userId + "' ");
		List<Object[]> clientDetail = query.getResultList();
		List<TokenDetails> tokenDetail = new ArrayList<TokenDetails>();

		if (!(clientDetail.isEmpty())) {
			TokenDetails token = new TokenDetails();
			token.setAuthorizationToken((String) clientDetail.get(0)[0]);
			token.setTakenGenrationTime((Date) clientDetail.get(0)[1]);
			tokenDetail.add(token);
		}
		return tokenDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean checkTokenValidOrNot(String existingtoken, int userId) {
		List<EFmFmUserMasterPO> tokenDetails = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery("SELECT b FROM EFmFmUserMasterPO as b where b.authorizationToken='"
				+ existingtoken + "' AND b.userId='" + userId + "'  ");
		tokenDetails = query.getResultList();
		if (!(tokenDetails.isEmpty())) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean checkEmployeeUserIdExistOrNot(int userId) {
		List<EFmFmUserMasterPO> tokenDetails = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmUserMasterPO as b where b.userId='" + userId + "'  ");
		tokenDetails = query.getResultList();
		if (!(tokenDetails.isEmpty())) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean checkTokenValidOrNotForMobile(String existingtoken, int userId) {
		List<EFmFmUserMasterPO> tokenDetails = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery("SELECT b FROM EFmFmUserMasterPO as b where b.mobAuthorizationToken='"
				+ existingtoken + "' AND b.userId='" + userId + "'  ");
		tokenDetails = query.getResultList();
		if (!(tokenDetails.isEmpty())) {
			return true;
		}
		return false;
	}

	// Method for checking invoiceNumberDigitRange number
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Integer getBranchInvoiceNumberDigitRangeFromBranchId(int branchId) {
		List<Integer> clientDetail = new ArrayList<Integer>();
		Query query = entityManager.createQuery(
				"SELECT b.invoiceNumberDigitRange FROM EFmFmClientBranchPO as b where b.branchId='" + branchId + "' ");
		clientDetail = query.getResultList();
		return clientDetail.get(0);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientUserRolePO> getUserRolesFromUserIdAndBranchId(int userId) {
		List<EFmFmClientUserRolePO> roleDetail = new ArrayList<EFmFmClientUserRolePO>();
		Query query = entityManager.createQuery(
				"SELECT r FROM EFmFmClientUserRolePO as r JOIN r.efmFmUserMaster u where u.userId='" + userId + "' ")
				.setMaxResults(200);
		roleDetail = query.getResultList();
		return roleDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientUserRolePO> getUserModulesByUserIdBranchIdAndModuleId(int userId, int moduleId) {
		List<EFmFmClientUserRolePO> roleDetail = new ArrayList<EFmFmClientUserRolePO>();
		Query query = entityManager.createQuery(
				"SELECT r FROM EFmFmClientUserRolePO as r JOIN r.efmFmUserMaster u JOIN r.eFmFmClientBranchSubConfiguration cn JOIN cn.eFmFmClientBranchConfiguration cc where u.userId='"
						+ userId + "' AND cc.clientBranchConfigurationId='" + moduleId + "' ");
		roleDetail = query.getResultList();
		return roleDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void removeARole(int userRoleId) {
		Query query = entityManager
				.createQuery("DELETE EFmFmClientUserRolePO where userRoleId = '" + userRoleId + "' ");
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientUserRolePO> getUserSubModulesByUserIdBranchIdAndSubModuleId(int userId, int moduleId) {
		List<EFmFmClientUserRolePO> roleDetail = new ArrayList<EFmFmClientUserRolePO>();
		Query query = entityManager.createQuery(
				"SELECT r FROM EFmFmClientUserRolePO as r JOIN r.efmFmUserMaster u JOIN r.eFmFmClientBranchSubConfiguration cc where u.userId='"
						+ userId + "' AND cc.clientBranchSubConfigurationId='" + moduleId + "' ");
		roleDetail = query.getResultList();
		return roleDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientBranchConfigurationMappingPO> getBranchMappingDetailsByBranchIdAndModuleId(int branchId,
			int moduleId) {
		List<EFmFmClientBranchConfigurationMappingPO> mappingDetail = new ArrayList<EFmFmClientBranchConfigurationMappingPO>();
		Query query = entityManager.createQuery(
				"SELECT r FROM EFmFmClientBranchConfigurationMappingPO as r JOIN r.eFmFmClientBranchConfiguration cn where cn.clientBranchConfigurationId='"
						+ moduleId + "' AND c.branchId='" + branchId + "'");
		mappingDetail = query.getResultList();
		return mappingDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientBranchSubConfigurationPO> getSubModulesOfMainModuleByModuleId(int moduleId) {
		List<EFmFmClientBranchSubConfigurationPO> subModulesDetail = new ArrayList<EFmFmClientBranchSubConfigurationPO>();
		Query query = entityManager.createQuery(
				"SELECT r FROM EFmFmClientBranchSubConfigurationPO as r JOIN r.eFmFmClientBranchConfiguration cn where cn.clientBranchConfigurationId='"
						+ moduleId + "' ");
		subModulesDetail = query.getResultList();
		return subModulesDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientBranchConfigurationMappingPO> getAllBranchMappingDetailsByBranchId(String branchId) {
		List<EFmFmClientBranchConfigurationMappingPO> mappingDetail = new ArrayList<EFmFmClientBranchConfigurationMappingPO>();
		Query query = entityManager.createQuery(
				"SELECT r FROM EFmFmClientBranchConfigurationMappingPO as r JOIN r.eFmFmClientBranchPO c where c.branchId in ("
						+ branchId + ")");
		mappingDetail = query.getResultList();
		return mappingDetail;
	}

	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientBranchConfigurationMappingPO> getAllBranchMappingDetailsByBranchIdAttchedToThatUser(int branchId) {
		List<EFmFmClientBranchConfigurationMappingPO> mappingDetail = new ArrayList<EFmFmClientBranchConfigurationMappingPO>();
		Query query = entityManager.createQuery(
				"SELECT r FROM EFmFmClientBranchConfigurationMappingPO as r JOIN r.eFmFmClientBranchPO c where c.branchId ="+ branchId + " ");
		mappingDetail = query.getResultList();
		return mappingDetail;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getLoggedInUserDetailFromClientIdAndUserId(EFmFmUserMasterPO eFmFmUserMaster) {
		List<EFmFmUserMasterPO> loginUserDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmUserMasterPO b  where b.userId='" + eFmFmUserMaster.getUserId() + "'");
		loginUserDetail = query.getResultList();
		return loginUserDetail;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getLoggedInUserDetailFromClientIdAndEmployeeId(EFmFmUserMasterPO eFmFmUserMaster) {
		List<EFmFmUserMasterPO> loginUserDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
						+ eFmFmUserMaster.geteFmFmClientBranchPO().getBranchId() + "' AND b.employeeId='"
						+ eFmFmUserMaster.getEmployeeId() + "'");
		loginUserDetail = query.getResultList();
		return loginUserDetail;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getFarthestEmployeeDetails(String employeeId) {
		List<EFmFmUserMasterPO> loginUserDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery("SELECT b FROM EFmFmUserMasterPO b  where  b.employeeId in ("
				+ employeeId + ") ORDER BY distance DESC");
		loginUserDetail = query.getResultList();
		return loginUserDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getNearestEmployeeDetails(String employeeId) {
		List<EFmFmUserMasterPO> loginUserDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b  where  b.employeeId in (" + employeeId + ") ORDER BY distance ASC");
		loginUserDetail = query.getResultList();
		return loginUserDetail;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getRegisterEmployeeDetailFromBranchIdAndUserId(int branchId, int userId) {
		List<EFmFmUserMasterPO> loginUserDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
						+ branchId + "' AND b.userId='" + userId + "'");
		loginUserDetail = query.getResultList();
		return loginUserDetail;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getEmployeeUserDetailFromEmployeeId(int branchId, String employeeId) {
		List<EFmFmUserMasterPO> loginUserDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
						+ branchId + "' AND b.employeeId='" + employeeId + "'");
		loginUserDetail = query.getResultList();
		return loginUserDetail;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getEmployeeUserDetailFromEmployeeIdAndFacilityIds(String branchId,
			String employeeId) {
		List<EFmFmUserMasterPO> loginUserDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("
						+ branchId + ") AND b.employeeId='" + employeeId + "'");
		loginUserDetail = query.getResultList();
		return loginUserDetail;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getEmployeeUserDetailFromMobileNumber(int branchId, String mobileNumber) {
		List<EFmFmUserMasterPO> loginUserDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
						+ branchId + "' AND b.mobileNumber='" + mobileNumber + "'");
		loginUserDetail = query.getResultList();
		return loginUserDetail;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getUsersFromClientId(EFmFmUserMasterPO userMasterPO) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("
						+ userMasterPO.getCombinedFacility() + ") AND b.status='Y' ")
				.setMaxResults(200);
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}

	
	
//	 @Override
//	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
//	    public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromBranchId(String branchId,int startPgNo,int endPgNo) {
//	        List<EFmFmUserMasterPO> employeeDetails = new ArrayList<EFmFmUserMasterPO>();
//	        log.info("branchId"+branchId+"startPgNo"+startPgNo+"endPgNo"+endPgNo);
//	        Query query = entityManager.createQuery(
//	                "SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.status='Y'")
//	                .setFirstResult(startPgNo)
//	                .setMaxResults(endPgNo); 
//	        log.info("Hibernate Cache This object");
//	        employeeDetails = query.getResultList();
//	        return employeeDetails;
//	    }
//	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAppDownloadUsersFromBranchId(String branchId,int startPgNo,int endPgNo) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where b.deviceId !='NO' AND c.branchId in ("
						+ branchId + ") AND b.status='Y' ").setFirstResult(startPgNo).setMaxResults(endPgNo); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getWithOutAppDownloadUsersFromBranchId(String branchId,int startPgNo,int endPgNo) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where b.deviceId ='NO' AND c.branchId in ("
						+ branchId + ") AND  b.status='Y' ").setFirstResult(startPgNo).setMaxResults(endPgNo); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAppDownloadedButNoGeoCodedFromBranchId(String branchId,int startPgNo,int endPgNo) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where b.deviceId !='NO' AND c.branchId in ("
						+ branchId + ") AND b.locationStatus='N' AND  b.status='Y' ").setFirstResult(startPgNo).setMaxResults(endPgNo); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAllGeoCodedEmployeesList(String branchId) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("
						+ branchId + ") AND b.locationStatus='Y' AND  b.status='Y' "); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAllNonGeoCodedEmployeesList(String branchId) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("
						+ branchId + ") AND b.locationStatus='N' AND  b.status='Y' "); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}

	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAppDownloadedAndGeoCodedUserFromBranchId(String branchId,int startPgNo,int endPgNo) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where b.deviceId !='NO' AND c.branchId in ("
						+ branchId + ") AND b.locationStatus='Y' AND  b.status='Y' ").setFirstResult(startPgNo).setMaxResults(endPgNo); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientBranchPO> getClientDetails(String branchId) {
		List<EFmFmClientBranchPO> clientDetails = new ArrayList<EFmFmClientBranchPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmClientBranchPO b   where b.branchId in (" + branchId + ") AND b.status='Y' ");
		clientDetails = query.getResultList();
		return clientDetails;
	}

	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientBranchPO> getBranchConfigurationDetailsFromBranchId(int branchId) {
		List<EFmFmClientBranchPO> clientDetails = new ArrayList<EFmFmClientBranchPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmClientBranchPO b   where b.branchId ='" + branchId + " ' ");
		clientDetails = query.getResultList();
		return clientDetails;
	}

	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmRoleMasterPO> getRoleId(String roleName) {
		List<EFmFmRoleMasterPO> eFmFmRoleMasterPO = new ArrayList<EFmFmRoleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmRoleMasterPO b  where UPPER(b.role)='" + roleName + "'");
		eFmFmRoleMasterPO = query.getResultList();
		return eFmFmRoleMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAllUsersBelogsProject(int branchId, int projectId) {
		List<EFmFmUserMasterPO> loginUserDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmClientProjectDetails p  where c.branchId='"
						+ branchId + "' AND b.status='Y' AND p.projectId='" + projectId + "'");
		loginUserDetail = query.getResultList();
		return loginUserDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getUsersRoleExist(String branchId, String clientProjectId, String role) {
		List<EFmFmUserMasterPO> loginUserDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c JOIN b.eFmFmClientProjectDetails p JOIN b.efmFmClientUserRoles r JOIN r.efmFmRoleMaster m where TRIM(UPPER(m.role))='"
						+ role + "' AND c.branchId in (" + branchId
						+ ") AND b.status='Y' AND TRIM(UPPER(p.clientProjectId))='" + clientProjectId + "'");
		loginUserDetail = query.getResultList();
		return loginUserDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getUserDetailFromUserId(int userId) {
		List<EFmFmUserMasterPO> loginUserDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery("SELECT b FROM EFmFmUserMasterPO b where b.userId='" + userId + "'");
		loginUserDetail = query.getResultList();
		return loginUserDetail;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeProjectDetailsPO> getListOfProjectId(int branchId, int userId) {
		List<EFmFmEmployeeProjectDetailsPO> projectDetail = new ArrayList<EFmFmEmployeeProjectDetailsPO>();
		Query query = entityManager.createQuery(
				" SELECT r FROM EFmFmUserMasterPO  u JOIN u.eFmFmEmployeeProjectDetailsPO r  JOIN u.eFmFmClientBranchPO c "
						+ "	where u.userId='" + userId + "' AND c.branchId='" + branchId + "' AND r.isActive='Y'");
		projectDetail = query.getResultList();
		return projectDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmRoleMasterPO> getUserRoleByUserId(int userId) {
		List<EFmFmRoleMasterPO> roleDetail = new ArrayList<EFmFmRoleMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT r FROM EFmFmRoleMasterPO as r JOIN r.efmFmClientUserRoles rl JOIN rl.efmFmUserMaster u where u.userId='"
						+ userId + "'");
		roleDetail = query.getResultList();
		return roleDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientProjectDetailsPO> getListOfProjectIdByAdhoc(int branchId) {
		List<EFmFmClientProjectDetailsPO> projectDetail = new ArrayList<EFmFmClientProjectDetailsPO>();
		Query query = entityManager
				.createQuery(" SELECT r FROM EFmFmClientProjectDetailsPO r  JOIN r.eFmFmClientBranchPO c "
						+ "	where c.branchId='" + branchId + "' AND r.isActive='A'");
		projectDetail = query.getResultList();
		return projectDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeProjectDetailsPO> getListOfRepMngByProjectId(int branchId, int projectId) {
		List<EFmFmEmployeeProjectDetailsPO> projectDetail = new ArrayList<EFmFmEmployeeProjectDetailsPO>();
		Query query = entityManager.createQuery(
				" SELECT p FROM EFmFmEmployeeProjectDetailsPO p JOIN p.eFmFmClientProjectDetails r JOIN r.eFmFmClientBranchPO c "
						+ "	where r.projectId='" + projectId + "' AND c.branchId='" + branchId
						+ "' AND p.isActive='Y' group by p.reportingManagerUserId ");
		projectDetail = query.getResultList();
		return projectDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientUserRolePO> getEmployeeDetailsByRole(String roleName, int branchId) {
		List<EFmFmClientUserRolePO> roleDetail = new ArrayList<EFmFmClientUserRolePO>();
		Query query = entityManager.createQuery(" SELECT ur FROM EFmFmClientUserRolePO ur JOIN ur.efmFmRoleMaster r "
				+ " JOIN ur.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c " + " where r.role in (" + roleName
				+ ") AND c.branchId='" + branchId + "' ");
		roleDetail = query.getResultList();
		return roleDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromBranchId(int branchId) {
		List<EFmFmUserMasterPO> employeeDetails = new ArrayList<EFmFmUserMasterPO>();

		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where b.userType='employee' AND c.branchId='"
						+ branchId + "' AND b.status='Y'");
		employeeDetails = query.getResultList();
		return employeeDetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmAdminCustomMessagePO eFmFmAdminCustomMessagePO) {
		entityManager.persist(eFmFmAdminCustomMessagePO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getEmployeeDetailsFromEmployeeIdAndBranchId(StringBuffer employeeIds, int branchId) {
		List<EFmFmUserMasterPO> employeeDetails = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where b.employeeId in("
						+ employeeIds + ") AND c.branchId='" + branchId + "' AND b.status='Y'");
		employeeDetails = query.getResultList();
		return employeeDetails;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		EFmFmUserMasterPO eFmFmUserMasterPO = entityManager.find(EFmFmUserMasterPO.class,
				eFmFmAdminSentSMSPO.getEfmFmUserMaster().getUserId());
		eFmFmAdminSentSMSPO.setEfmFmUserMaster(eFmFmUserMasterPO);

		EFmFmClientBranchPO eFmFmClientBranchPo = entityManager.find(EFmFmClientBranchPO.class,
				eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId());
		eFmFmAdminSentSMSPO.seteFmFmClientBranchPO(eFmFmClientBranchPo);
		entityManager.persist(eFmFmAdminSentSMSPO);
		entityManager.flush();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getEmployeeDetailsFromMobileNumberAndBranchId(StringBuffer mobileNumber,
			int branchId) {
		List<EFmFmUserMasterPO> employeeDetails = new ArrayList<EFmFmUserMasterPO>();
		List<EFmFmUserMasterPO> employeeDetail = new ArrayList<EFmFmUserMasterPO>();

		String[] mobileNumbers = mobileNumber.toString().split(",");
		for (String mobileNos : mobileNumbers) {
			Query query = entityManager.createQuery(
					"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where b.mobileNumber='" + mobileNos
							+ "' AND c.branchId='" + branchId + "' AND b.status='Y'");
			employeeDetails = query.getResultList();
			employeeDetail.addAll(employeeDetails);

		}
		return employeeDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAllGuestDetailsFromBranchId(int branchId) {
		List<EFmFmUserMasterPO> employeeDetails = new ArrayList<EFmFmUserMasterPO>();

		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where b.userType='guest' AND c.branchId='"
						+ branchId + "' AND b.status='Y'");
		employeeDetails = query.getResultList();
		return employeeDetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromShiftDate(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		List<EFmFmUserMasterPO> employeeDetails = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmEmployeeTravelRequestPO t JOIN b.eFmFmClientBranchPO c  where t.tripType='"
						+ eFmFmAdminSentSMSPO.getTripType() + "' AND t.shiftTime='" + eFmFmAdminSentSMSPO.getShiftTime()
						+ "' AND t.requestDate='" + eFmFmAdminSentSMSPO.getShiftDate() + "' AND c.branchId='"
						+ eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId()
						+ "' AND b.userType='employee'AND b.status='Y'");
		employeeDetails = query.getResultList();
		return employeeDetails;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAllEmployeeAndGuestDetailsFromShiftDate(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		List<EFmFmUserMasterPO> employeeDetails = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmEmployeeTravelRequestPO t JOIN b.eFmFmClientBranchPO c  where t.tripType='"
						+ eFmFmAdminSentSMSPO.getTripType() + "' AND t.shiftTime='" + eFmFmAdminSentSMSPO.getShiftTime()
						+ "' AND t.requestDate='" + eFmFmAdminSentSMSPO.getShiftDate() + "' AND c.branchId='"
						+ eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId() + "' AND b.status='Y'");
		employeeDetails = query.getResultList();
		return employeeDetails;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAdminCustomMessagePO> getAllCustomMessagesFromBranchId(String branchId) {

		List<EFmFmAdminCustomMessagePO> eFmFmAdminCustomMessagePO = new ArrayList<EFmFmAdminCustomMessagePO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmAdminCustomMessagePO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.isActive='Y'");
		eFmFmAdminCustomMessagePO = query.getResultList();

		return eFmFmAdminCustomMessagePO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveMessageSentByMobileNumber(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		entityManager.persist(eFmFmAdminSentSMSPO);
		entityManager.flush();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAdminSentSMSPO> getAllSentSMSHistory(int branchId) {
		List<EFmFmAdminSentSMSPO> eFmFmAdminSentSMSPo = new ArrayList<EFmFmAdminSentSMSPO>();

		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmAdminSentSMSPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
						+ branchId + "'");
		eFmFmAdminSentSMSPo = query.getResultList();

		return eFmFmAdminSentSMSPo;

	}

	// Method for checking number of adminstrators exist or not
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String getBranchNameFromBranchId(int branchId) {
		List<String> branchDetail = new ArrayList<String>();
		Query query = entityManager
				.createQuery("SELECT b.branchName FROM EFmFmClientBranchPO as b where b.branchId='" + branchId + "' ");
		branchDetail = query.getResultList();
		return branchDetail.get(0);
	}

	// Method for getting branchId From BranchName
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Integer getBranchIdFromBranchName(String branchName) {
		System.out.println("branchName"+branchName);
		List<Integer> clientDetail = new ArrayList<Integer>();
		Query query = entityManager.createQuery(
				"SELECT b.branchId FROM EFmFmClientBranchPO as b where b.branchName='" + branchName + "' ");
		clientDetail = query.getResultList();
		return clientDetail.get(0);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> getBranchLocationFromBranchId(int branchId) {
		List<String> latitudeLongitude = new ArrayList<String>();
		Query query = entityManager.createQuery(
				"SELECT b.latitudeLongitude FROM EFmFmClientBranchPO as b where b.branchId='" + branchId + "' ");
		latitudeLongitude = query.getResultList();
		return latitudeLongitude;
	}

	@Override
	public List<EFmFmClientBranchPO> getEscortTimeDetails(String branchId) {

		List<EFmFmClientBranchPO> userDetail = new ArrayList<EFmFmClientBranchPO>();
		EFmFmClientBranchPO po = new EFmFmClientBranchPO();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmClientBranchPO as b where b.branchId in ("+branchId+") ");
		userDetail = query.getResultList();

		return userDetail;
	}

	@Override
	public List<EFmFmAdminSentSMSPO> checkEmployeeDetailsByMobileNumber(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {
		List<EFmFmAdminSentSMSPO> eFmFmAdminSentSMSPo = new ArrayList<EFmFmAdminSentSMSPO>();
		Query query = entityManager.createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c "
				+ " where b.mobileNumber='" + eFmFmAdminSentSMSPO.getMobileNumber() + "' AND c.branchId='"
				+ eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId() + "' AND b.status='Y'");
		eFmFmAdminSentSMSPo = query.getResultList();

		return eFmFmAdminSentSMSPo;
	}

	@Override
	public List<EFmFmAdminSentSMSPO> checkEmployeeDetailsByEmployeeId(EFmFmAdminSentSMSPO eFmFmAdminSentSMSPO) {

		List<EFmFmAdminSentSMSPO> eFmFmAdminSentSMSPo = new ArrayList<EFmFmAdminSentSMSPO>();
		Query query = entityManager.createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c "
				+ " where b.employeeId='" + eFmFmAdminSentSMSPO.getEmployeeId() + "' AND c.branchId='"
				+ eFmFmAdminSentSMSPO.geteFmFmClientBranchPO().getBranchId() + "' AND b.status='Y'");
		eFmFmAdminSentSMSPo = query.getResultList();

		return eFmFmAdminSentSMSPo;
	}
	
	
	
	
	@Override
	public List<EFmFmEmployeeModuleMappingWithBranchPO> getAllEmployeeModuleAccessFromBranchId(int branchId) {

		List<EFmFmEmployeeModuleMappingWithBranchPO> userDetail = new ArrayList<EFmFmEmployeeModuleMappingWithBranchPO>();
		EFmFmClientBranchPO po = new EFmFmClientBranchPO();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmEmployeeModuleMappingWithBranchPO as b  JOIN b.eFmFmClientBranchPO c where c.branchId ='" + branchId + "' ");
		userDetail = query.getResultList();

		return userDetail;
	}

	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getWithOutAppDownloadUsersButWebGeocodedFromBranchId(String branchId, int startPgNo,
			int endPgNo) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where b.deviceId ='NO' "
				+ " AND b.locationStatus='Y' AND c.branchId in ("
						+ branchId + ") AND  b.status='Y' ").setFirstResult(startPgNo).setMaxResults(endPgNo); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}

	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAppDownloadUsersFromBranchId(String branchId) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where b.deviceId !='NO' AND c.branchId in ("
						+ branchId + ") AND b.status='Y' ");
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAppDownloadedAndGeoCodedUserFromBranchId(String branchId) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where b.deviceId !='NO' AND c.branchId in ("
						+ branchId + ") AND b.locationStatus='Y' AND  b.status='Y' "); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getWithOutAppDownloadUsersFromBranchId(String branchId) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where b.deviceId ='NO' AND c.branchId in ("
						+ branchId + ") AND  b.status='Y' "); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAppDownloadedButNoGeoCodedFromBranchId(String branchId) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where b.deviceId !='NO' AND c.branchId in ("
						+ branchId + ") AND b.locationStatus='N' AND  b.status='Y' "); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getWithOutAppDownloadUsersButWebGeocodedFromBranchId(String branchId) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where b.deviceId ='NO' "
				+ " AND b.locationStatus='Y' AND c.branchId in ("
						+ branchId + ") AND  b.status='Y' "); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAllNonGeoCodedEmployeesList(String branchId,int startPgNo,int endPgNo) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("
						+ branchId + ") AND b.locationStatus='N' AND  b.status='Y' ").setFirstResult(startPgNo).setMaxResults(endPgNo);
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAllGeoCodedEmployeesList(String branchId,int startPgNo,int endPgNo) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("
						+ branchId + ") AND b.locationStatus='Y' AND  b.status='Y' ").setFirstResult(startPgNo).setMaxResults(endPgNo); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAllGeoCodedDiffEmployeesList(String branchId,int startPgNo,int endPgNo) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("
						+ branchId + ") AND b.geoCodeVariationDistance > 1  AND  b.status='Y' ").setFirstResult(startPgNo).setMaxResults(endPgNo); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmUserMasterPO> getAllGeoCodedDiffEmployeesList(String branchId) {
		List<EFmFmUserMasterPO> allUsersDetail = new ArrayList<EFmFmUserMasterPO>();
		Query query = entityManager.createQuery(
				"SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("
						+ branchId + ") AND b.geoCodeVariationDistance > 1 AND  b.status='Y' "); 
		allUsersDetail = query.getResultList();
		return allUsersDetail;
	}
	

}
