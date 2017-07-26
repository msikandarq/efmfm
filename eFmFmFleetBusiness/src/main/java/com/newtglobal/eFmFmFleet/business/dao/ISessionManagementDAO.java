package com.newtglobal.eFmFmFleet.business.dao;

import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmFmSessionManagementPO;

public interface ISessionManagementDAO {
	public void saveSessionData(EFmFmSessionManagementPO eFmFmSessionManagementPO);
	public void update(EFmFmSessionManagementPO eFmFmSessionManagementPO);

	public List<String> getSessionDetails(EFmFmSessionManagementPO eFmFmSessionManagementPO);
	public void logOutAllSession(int userId,int branchId);
	public void logOutIndividualSession(int userId,String sessionId,int branchId);
	public void logOutAllSessionExceptCurrent(int userId,String sessionId,int branchId);
	public String getSessionValidityCheck(int userId,int branchId,String userAgent,String userIPAddress);
	public List<EFmFmSessionManagementPO> getAllBrowserSessionsFromUserAgent(int userId, int branchId, String userAgent,
			String userIPAddress);
	public List<EFmFmSessionManagementPO> OnPasswordChangeInvalidateAllTheSessions(int userId);
	public List<EFmFmSessionManagementPO> getAllBrowserSessionsFromUserAgentAndUserId(int userId, String userAgent,
			String userIPAddress);
}
