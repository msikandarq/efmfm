package com.newtglobal.eFmFmFleet.business.dao.daoImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newtglobal.eFmFmFleet.business.dao.ISessionManagementDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmSessionManagementPO;

@Repository("ISessionManagementDAO")
public class SessionManagementDAOImp implements ISessionManagementDAO{
	
	private static Log log = LogFactory.getLog(SessionManagementDAOImp.class);
	private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
	
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveSessionData(EFmFmSessionManagementPO eFmFmSessionManagementPO) {
    	entityManager.persist(eFmFmSessionManagementPO);		
	}
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmSessionManagementPO eFmFmSessionManagementPO) {
        entityManager.merge(eFmFmSessionManagementPO);
		
	}
    
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void logOutAllSession(int userId,int branchId){
    	Query query = entityManager.createQuery("UPDATE EFmFmSessionManagementPO SET sessionActiveStatus='N' "
    			+ "WHERE userId= '" + userId + "' and branchId='"+branchId+"'");
        query.executeUpdate();
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void logOutIndividualSession(int userId,String sessionId,int branchId){
    	Query query = entityManager.createQuery("UPDATE EFmFmSessionManagementPO SET SessionActiveStatus='N' "
    			+ "WHERE userId= '" + userId + "' and sessionId= "+sessionId+ "' and branchId='"+branchId+"'");
        query.executeUpdate();
    }
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void logOutAllSessionExceptCurrent(int userId,String sessionId,int branchId){
    	Query query = entityManager.createQuery("UPDATE EFmFmSessionManagementPO SET SessionActiveStatus='N' "
    			+ "WHERE userId= '" + userId + "' and sessionId<> "+sessionId+ "' and branchId='"+branchId+"'");
        query.executeUpdate();
    }
    @Override    
    public List<String> getSessionDetails(EFmFmSessionManagementPO EFmFmSessionManagementPe) {
        List<EFmFmSessionManagementPO> sessionDetails = new ArrayList<EFmFmSessionManagementPO>();
        String sessionValid="Invalid";
        String loginStatus="NotLogin";
        List<String> returnList=new ArrayList<String>();
        
        Query query = entityManager
                .createQuery("select ss from EFmFmSessionManagementPO ss JOIN ss.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
                		+ "where ss.sessionActiveStatus='Y' and u.userId='"+EFmFmSessionManagementPe.getEfmFmUserMaster().getUserId()+
                		"' and c.branchId='"+EFmFmSessionManagementPe.geteFmFmClientBranchPO().getBranchId()+"' ORDER BY ss.sessionStartTime DESC LIMIT 1");
        log.info("Hibernate Cache This object");
        sessionDetails = query.getResultList();
        
        
        if(sessionDetails.size()>0){
        	sessionValid="Valid";
        	if((!sessionDetails.get(0).getUserIPAddress().equalsIgnoreCase(EFmFmSessionManagementPe.getUserIPAddress()))
        			|| (!sessionDetails.get(0).getUserAgent().equalsIgnoreCase(EFmFmSessionManagementPe.getUserAgent()))){
        		loginStatus="AllreadyLogin";
        	}
        }
        returnList.add(sessionValid);
        returnList.add(loginStatus);
        return returnList;
    }
    
    
    @Override    
    public String getSessionValidityCheck(int userId,int branchId,String userAgent,String userIPAddress) {
        List<EFmFmSessionManagementPO> sessionDetails = new ArrayList<EFmFmSessionManagementPO>();
        String sessionValid="Invalid";
        Query query = entityManager
                .createQuery("select s from EFmFmSessionManagementPO s JOIN s.efmFmUserMaster u "
                		+ " where s.sessionActiveStatus='Y' and u.userId=' "+userId+"' and s.userAgent='"+userAgent+"' and s.userIPAddress='"+userIPAddress+"'");                		
        sessionDetails = query.getResultList();
        if(!(sessionDetails.isEmpty())){
        	sessionValid="Valid";        	
        }   
//        System.out.println("userId"+userId);
//        System.out.println("branchId"+branchId);
//        System.out.println("userAgent"+userAgent);
//        System.out.println("userIPAddress"+userIPAddress);
//        System.out.println("sessionDetails"+sessionValid);
        return sessionValid;
    }
    
    @Override    
    public List<EFmFmSessionManagementPO> getAllBrowserSessionsFromUserAgent(int userId,int branchId,String userAgent,String userIPAddress) {
        List<EFmFmSessionManagementPO> sessionDetails = new ArrayList<EFmFmSessionManagementPO>();
        Query query = entityManager
                .createQuery("select s from EFmFmSessionManagementPO s JOIN s.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c "
                		+ " where s.sessionActiveStatus='Y' and u.userId=' "+userId+"' and c.branchId='"+branchId+"' and s.userAgent='"+userAgent+"' and s.userIPAddress='"+userIPAddress+"' ORDER BY s.sessionStartTime desc");                		
        sessionDetails = query.getResultList();             
        return sessionDetails;
    }
    
    
    
    @Override    
    public List<EFmFmSessionManagementPO> getAllBrowserSessionsFromUserAgentAndUserId(int userId,String userAgent,String userIPAddress) {
        List<EFmFmSessionManagementPO> sessionDetails = new ArrayList<EFmFmSessionManagementPO>();
        Query query = entityManager
                .createQuery("select s from EFmFmSessionManagementPO s JOIN s.efmFmUserMaster u "
                		+ " where s.sessionActiveStatus='Y' and u.userId=' "+userId+"' and s.userAgent='"+userAgent+"' and s.userIPAddress='"+userIPAddress+"' ORDER BY s.sessionStartTime desc");                		
        sessionDetails = query.getResultList();             
        return sessionDetails;
    }
    
    
    @Override    
    public List<EFmFmSessionManagementPO> OnPasswordChangeInvalidateAllTheSessions(int userId) {
        List<EFmFmSessionManagementPO> sessionDetails = new ArrayList<EFmFmSessionManagementPO>();
        Query query = entityManager
                .createQuery("select s from EFmFmSessionManagementPO s JOIN s.efmFmUserMaster u  "
                		+ " where s.sessionActiveStatus='Y' and u.userId=' "+userId+"' ");                		
        sessionDetails = query.getResultList();             
        return sessionDetails;
    }
	
}
