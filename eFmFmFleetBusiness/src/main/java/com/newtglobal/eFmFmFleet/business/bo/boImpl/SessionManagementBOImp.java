package com.newtglobal.eFmFmFleet.business.bo.boImpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtglobal.eFmFmFleet.business.bo.ISessionManagementBO;
import com.newtglobal.eFmFmFleet.business.dao.ISessionManagementDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmSessionManagementPO;

@Service("ISessionManagementBO")
public class SessionManagementBOImp implements ISessionManagementBO{

	@Autowired
	private ISessionManagementDAO iSessionManagementDAO;
	

	public void setiSessionManagementDAO(ISessionManagementDAO iSessionManagementDAO) {
		this.iSessionManagementDAO = iSessionManagementDAO;
	}



	@Override
	public void saveSessionData(EFmFmSessionManagementPO eFmFmSessionManagementPO) {
		iSessionManagementDAO.saveSessionData(eFmFmSessionManagementPO);		
	}

	@Override
	public void update(EFmFmSessionManagementPO eFmFmSessionManagementPO) {
		iSessionManagementDAO.update(eFmFmSessionManagementPO);
	}


	@Override
	public List<String> getSessionDetails(EFmFmSessionManagementPO eFmFmSessionManagementPO) {
		
		return iSessionManagementDAO.getSessionDetails(eFmFmSessionManagementPO);
	}



	@Override
	public void logOutAllSession(int userId, int branchId) {
		iSessionManagementDAO.logOutAllSession(userId,branchId);
		
	}



	@Override
	public void logOutIndividualSession(int userId, String sessionId, int branchId) {
		iSessionManagementDAO.logOutIndividualSession(userId,sessionId,branchId);
		
	}



	@Override
	public void logOutAllSessionExceptCurrent(int userId, String sessionId, int branchId) {
		iSessionManagementDAO.logOutAllSessionExceptCurrent(userId,sessionId,branchId);
		
	}
	
	@Override
	public String getSessionValidityCheck(int userId,int branchId,String userAgent,String userIPAddress) {
		return iSessionManagementDAO.getSessionValidityCheck(userId,branchId,userAgent,userIPAddress);
		
	}



	@Override
	public List<EFmFmSessionManagementPO> getAllBrowserSessionsFromUserAgent(int userId, int branchId, String userAgent,
			String userIPAddress) {
		return iSessionManagementDAO.getAllBrowserSessionsFromUserAgent(userId, branchId, userAgent, userIPAddress);
	}



	@Override
	public List<EFmFmSessionManagementPO> OnPasswordChangeInvalidateAllTheSessions(int userId) {
		return iSessionManagementDAO.OnPasswordChangeInvalidateAllTheSessions(userId);
	}



	@Override
	public List<EFmFmSessionManagementPO> getAllBrowserSessionsFromUserAgentAndUserId(int userId, String userAgent,
			String userIPAddress) {
		return iSessionManagementDAO.getAllBrowserSessionsFromUserAgentAndUserId(userId, userAgent, userIPAddress);
	}


}
