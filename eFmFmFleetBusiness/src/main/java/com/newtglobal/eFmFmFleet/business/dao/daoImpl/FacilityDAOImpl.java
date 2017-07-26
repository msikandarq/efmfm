package com.newtglobal.eFmFmFleet.business.dao.daoImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newtglobal.eFmFmFleet.business.dao.IFacilityDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmFacilityToFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;


@Repository("IFacilityDAO")
public class FacilityDAOImpl implements IFacilityDAO {
	
	 private EntityManager entityManager;

	    @PersistenceContext
	    public void setEntityManager(EntityManager entityManager) {
	        this.entityManager = entityManager;
	    }

	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public void save(EFmFmFacilityToFacilityMappingPO  eFmFmFacilityToFacilityMappingPO) {
	        entityManager.persist(eFmFmFacilityToFacilityMappingPO);

	    }

	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public void update(EFmFmFacilityToFacilityMappingPO  eFmFmFacilityToFacilityMappingPO) {
	        entityManager.merge(eFmFmFacilityToFacilityMappingPO);

	    }
	    
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public void save(EFmFmUserFacilityMappingPO  eFmFmUserFacilityMappingPO) {
	        entityManager.persist(eFmFmUserFacilityMappingPO);

	    }

	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public void update(EFmFmUserFacilityMappingPO  eFmFmUserFacilityMappingPO) {
	        entityManager.merge(eFmFmUserFacilityMappingPO);

	    }

	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public void delete(EFmFmFacilityToFacilityMappingPO eFmFmFacilityToFacilityMappingPO) {
	        entityManager.remove(eFmFmFacilityToFacilityMappingPO);

	    }
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public void delete(EFmFmUserFacilityMappingPO eFmFmUserFacilityMappingPO) {
	        entityManager.remove(eFmFmUserFacilityMappingPO);

	    }
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmFacilityToFacilityMappingPO> getAllAttachedFacilities(int branchId) {
	        List<EFmFmFacilityToFacilityMappingPO> eFmFmFacilityToFacilityMappingPO = new ArrayList<EFmFmFacilityToFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmFacilityToFacilityMappingPO a JOIN a.eFmFmClientBranchPO c where c.branchId='"
	                        + branchId + "' ");
	        eFmFmFacilityToFacilityMappingPO = query.getResultList();
	        return eFmFmFacilityToFacilityMappingPO;
	    }	
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmFacilityToFacilityMappingPO> getAllActiveFacilities() {
	        List<EFmFmFacilityToFacilityMappingPO> eFmFmFacilityToFacilityMappingPO = new ArrayList<EFmFmFacilityToFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmFacilityToFacilityMappingPO a where a.facilityStatus='Y' ");
	        eFmFmFacilityToFacilityMappingPO = query.getResultList();
	        return eFmFmFacilityToFacilityMappingPO;
	    }	
	    
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmFacilityToFacilityMappingPO> getAllInActiveFacilities() {
	        List<EFmFmFacilityToFacilityMappingPO> eFmFmFacilityToFacilityMappingPO = new ArrayList<EFmFmFacilityToFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmFacilityToFacilityMappingPO a where a.facilityStatus='N' ");
	        eFmFmFacilityToFacilityMappingPO = query.getResultList();
	        return eFmFmFacilityToFacilityMappingPO;
	    }	
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmFacilityToFacilityMappingPO> getAllAttachedActiveFacilities(int branchId) {
	        List<EFmFmFacilityToFacilityMappingPO> eFmFmFacilityToFacilityMappingPO = new ArrayList<EFmFmFacilityToFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmFacilityToFacilityMappingPO a JOIN a.eFmFmClientBranchPO c where c.branchId='"
	                        + branchId + "' AND a.facilityStatus='Y' ");
	        eFmFmFacilityToFacilityMappingPO = query.getResultList();
	        return eFmFmFacilityToFacilityMappingPO;
	    }	
	    
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmUserFacilityMappingPO> getAllFacilitiesAttachedToUser(int userId) {
	        List<EFmFmUserFacilityMappingPO> eFmFmUserFacilityMappingPO = new ArrayList<EFmFmUserFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmUserFacilityMappingPO a JOIN a.efmFmUserMaster c where c.userId='"
	                        + userId + "' AND a.userFacilityStatus='Y' ");
	        eFmFmUserFacilityMappingPO = query.getResultList();
	        return eFmFmUserFacilityMappingPO;
	    }
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmUserFacilityMappingPO> getAllFacilitiesAttachedToParticularUser(int userId) {
	        List<EFmFmUserFacilityMappingPO> eFmFmUserFacilityMappingPO = new ArrayList<EFmFmUserFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmUserFacilityMappingPO a JOIN a.efmFmUserMaster c where c.userId='"
	                        + userId + "' ");
	        eFmFmUserFacilityMappingPO = query.getResultList();
	        return eFmFmUserFacilityMappingPO;
	    }
	    
	    
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmUserFacilityMappingPO> getAttachedParticularFacilityDetail(int userId,int branchId) {
	        List<EFmFmUserFacilityMappingPO> eFmFmUserFacilityMappingPO = new ArrayList<EFmFmUserFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmUserFacilityMappingPO a JOIN a.efmFmUserMaster u JOIN a.eFmFmClientBranchPO c where u.userId='"
	                        + userId + "' AND c.branchId='"
	                        + branchId + "' ");
	        eFmFmUserFacilityMappingPO = query.getResultList();
	        return eFmFmUserFacilityMappingPO;
	    }
	    
	    
	    	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmFacilityToFacilityMappingPO> getAllBaseClientIdFromBranchName(String branchName) {
	    	List<EFmFmFacilityToFacilityMappingPO> eFmFmFacilityToFacilityMappingPO = new ArrayList<EFmFmFacilityToFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmFacilityToFacilityMappingPO a where a.branchName='"+branchName+"' AND a.facilityStatus='Y' " );
	        eFmFmFacilityToFacilityMappingPO = query.getResultList();       
			return eFmFmFacilityToFacilityMappingPO;   			
	    }    
	    	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public boolean facilityToFacilityCheck(int branchId,String branchName) {
	    	List<EFmFmFacilityToFacilityMappingPO> eFmFmFacilityToFacilityMappingPO = new ArrayList<EFmFmFacilityToFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmFacilityToFacilityMappingPO a JOIN a.eFmFmClientBranchPO c where c.branchId='"
	                        + branchId + "' AND a.branchName='"+branchName+"'  AND a.facilityStatus='Y' ");
	        eFmFmFacilityToFacilityMappingPO = query.getResultList();
	        if(!(eFmFmFacilityToFacilityMappingPO.isEmpty())){
				return true;
			}
			return false;   
			}
	   
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public boolean checkFacilityAccess(int userId,int branchId) {
	        List<EFmFmUserFacilityMappingPO> eFmFmUserFacilityMappingPO = new ArrayList<EFmFmUserFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmUserFacilityMappingPO a JOIN a.efmFmUserMaster u JOIN a.eFmFmClientBranchPO c where u.userId='"
	                        + userId + "' AND c.branchId='"
	                        + branchId + "' AND a.userFacilityStatus='Y'");
	        eFmFmUserFacilityMappingPO = query.getResultList();
	        if(!(eFmFmUserFacilityMappingPO.isEmpty())){
				return true;
			}
			return false;   
			}
	   
	    
	    
	    
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmFacilityToFacilityMappingPO> getParticularFacilityDetail(int facilityToFacilityId) {
	        List<EFmFmFacilityToFacilityMappingPO> eFmFmFacilityToFacilityMappingPO = new ArrayList<EFmFmFacilityToFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmFacilityToFacilityMappingPO a where  a.facilityToFacilityId='"+facilityToFacilityId+"' ");
	        eFmFmFacilityToFacilityMappingPO = query.getResultList();
	        return eFmFmFacilityToFacilityMappingPO;
	    }
	    
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmFacilityToFacilityMappingPO> getParticularFacilityDetailFromBranchName(String branchName) {
	        List<EFmFmFacilityToFacilityMappingPO> eFmFmFacilityToFacilityMappingPO = new ArrayList<EFmFmFacilityToFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmFacilityToFacilityMappingPO a where  a.branchName='"+branchName+"' ");
	        eFmFmFacilityToFacilityMappingPO = query.getResultList();
	        return eFmFmFacilityToFacilityMappingPO;
	    }
	    
	    
	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmFacilityToFacilityMappingPO> getAllInactiveFacilities() {
	        List<EFmFmFacilityToFacilityMappingPO> eFmFmFacilityToFacilityMappingPO = new ArrayList<EFmFmFacilityToFacilityMappingPO>();
	        Query query = entityManager
	                .createQuery("SELECT a FROM EFmFmFacilityToFacilityMappingPO a where  a.facilityStatus='N' ");
	        eFmFmFacilityToFacilityMappingPO = query.getResultList();
	        return eFmFmFacilityToFacilityMappingPO;
	    }
	    
	    
	    // Method for getting all the available branches  
//	    @Override
//	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
//	    public List<EFmFmFacilityJSON> getAllAvailableBranches(){
//	        Query query = entityManager.createQuery(
//	                "SELECT b.branchName,b.branchId FROM EFmFmClientBranchPO as b ");
//			List<Object[]> branchDetail = query.getResultList();
//	        List<EFmFmFacilityJSON> allBranchedDetail = new ArrayList<EFmFmFacilityJSON>();
//
//	        if(!(clientDetail.isEmpty())){
//	        	EFmFmFacilityJSON facilityDetail=new EFmFmFacilityJSON();
//	        	facilityDetail.g
//	        	token.setAuthorizationToken((String) clientDetail.get(0)[0]);
//	        	token.setTakenGenrationTime((Date) clientDetail.get(0)[1]);
//	        	branchDetail.setBranchCode((String) clientDetail.get(0)[2]);
//	        	allBranchedDetail.add(branchDetail);
//	        }
//	        return tokenDetail;
//	    }
//	    
	    
	    
	     @Override
	     @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	     public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromEmailId(String emailId) {
	         List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
	         Query query = entityManager
	                 .createQuery("SELECT b FROM EFmFmUserMasterPO b where b.emailId = '" + emailId + "' ");
	         eFmFmEmployeeMasterPO = query.getResultList();
	         return eFmFmEmployeeMasterPO;
	     }


	     
	     @Override
	     @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	     public List<EFmFmUserMasterPO> getEmpMobileNoDetails(String mobileNo) {
	         List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
	         Query query = entityManager
	                 .createQuery("SELECT b FROM EFmFmUserMasterPO b where  b.mobileNumber = '" + mobileNo + "' ");
	         eFmFmEmployeeMasterPO = query.getResultList();
	         return eFmFmEmployeeMasterPO;
	     }
	     

	  @Override
	     @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	     public List<EFmFmUserMasterPO> getEmpDetailsFromEmployeeIdAndBranchId(String employeeId) {
	         List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
	         Query query = entityManager
	                 .createQuery("SELECT b FROM EFmFmUserMasterPO b where b.employeeId = '" + employeeId + "'");
	         eFmFmEmployeeMasterPO = query.getResultList();
	         return eFmFmEmployeeMasterPO;
	     }

	    
	    
	
}
