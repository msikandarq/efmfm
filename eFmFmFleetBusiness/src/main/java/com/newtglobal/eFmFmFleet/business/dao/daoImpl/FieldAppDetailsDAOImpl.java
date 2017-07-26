package com.newtglobal.eFmFmFleet.business.dao.daoImpl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.newtglobal.eFmFmFleet.business.dao.IFieldAppDetailsDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFieldAppConfigMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmSupervisorMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserPasswordPO;

@Repository("IFieldAppDetailsDAO")
public class FieldAppDetailsDAOImpl implements IFieldAppDetailsDAO{

	@PersistenceContext
	private EntityManager entityManager;	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) {
		entityManager.persist(eFmFmSupervisorMasterPO);
		
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int getSupervisorId(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) {	
		entityManager.persist(eFmFmSupervisorMasterPO);
		entityManager.flush();
		return eFmFmSupervisorMasterPO.getSupervisorId();
		
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) {
		entityManager.merge(eFmFmSupervisorMasterPO);
		
	}
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) {
		entityManager.remove(eFmFmSupervisorMasterPO);
		
	}


	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmSupervisorMasterPO> getSupervisorDetails(int supervisorId,String branchId){
		List <EFmFmSupervisorMasterPO> eFmFmSupervisorMasterPO = new ArrayList<EFmFmSupervisorMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmSupervisorMasterPO b  "
				+ " JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c "
				+ " where c.branchId in ("+branchId+") and b.supervisorId='"+supervisorId+"' AND b.isActive='A' ");		
		eFmFmSupervisorMasterPO=query.getResultList();	
		return eFmFmSupervisorMasterPO;
		
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmSupervisorMasterPO> getSupervisorMobileNumberDetails(String mobileNumber,String branchId){
		List <EFmFmSupervisorMasterPO> eFmFmSupervisorMasterPO = new ArrayList<EFmFmSupervisorMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmSupervisorMasterPO b "
				+ " JOIN b.efmFmVendorMaster v JOIN  v.eFmFmClientBranchPO c "
				+ " where c.branchId in ("+branchId+") AND TRIM(b.mobileNumber)=TRIM('"+mobileNumber+"') AND b.isActive='A' ");		
		eFmFmSupervisorMasterPO=query.getResultList();	
		return eFmFmSupervisorMasterPO;
		
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmSupervisorMasterPO> getSupervisorMobileNumberStatus(String mobileNumber,String branchId){
		List <EFmFmSupervisorMasterPO> eFmFmSupervisorMasterPO = new ArrayList<EFmFmSupervisorMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmSupervisorMasterPO b "
				+ " JOIN b.efmFmVendorMaster v JOIN  v.eFmFmClientBranchPO c where c.branchId in ("+branchId+")"
						+ "  AND TRIM(b.mobileNumber)=TRIM('"+mobileNumber+"') ");		
		eFmFmSupervisorMasterPO=query.getResultList();	
		return eFmFmSupervisorMasterPO;
		
	}
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmSupervisorMasterPO> getAllSupervisorByVendorDetail(int vendorId, String branchId,String isActive) {
		List<EFmFmSupervisorMasterPO> eFmFmSupervisorMasterPO  =new ArrayList<EFmFmSupervisorMasterPO>();
    	Query query=entityManager.createQuery("SELECT b FROM EFmFmSupervisorMasterPO as b JOIN b.efmFmVendorMaster v "
    			+ "JOIN v.eFmFmClientBranchPO c where c.branchId in ("+branchId+") and  v.vendorId='"+vendorId+"' "
    			+ " AND b.isActive='"+isActive+"' ");
    	eFmFmSupervisorMasterPO=query.getResultList();
		return eFmFmSupervisorMasterPO;
	}

	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmFieldAppConfigMasterPO> getAllValuesUsedByConfigType(String branchId,String configType) {
		List <EFmFmFieldAppConfigMasterPO> eFmFmFieldAppConfigMasterPO = new ArrayList<EFmFmFieldAppConfigMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmFieldAppConfigMasterPO b "
				+ " JOIN b.eFmFmClientBranchPO  c where c.branchId in ("+branchId+") AND b.configType='"+configType+"' AND b.IsActive=1 ");		
		eFmFmFieldAppConfigMasterPO=query.getResultList();	
		return eFmFmFieldAppConfigMasterPO;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmSupervisorMasterPO> getAllSupervisorDetails(String branchId,String isActive) {
		List<EFmFmSupervisorMasterPO> eFmFmSupervisorMasterPO  =new ArrayList<EFmFmSupervisorMasterPO>();
    	Query query=entityManager.createQuery("SELECT b FROM EFmFmSupervisorMasterPO as b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where c.branchId in ("+branchId+") AND b.isActive='"+isActive+"' ");
    	eFmFmSupervisorMasterPO=query.getResultList();
		return eFmFmSupervisorMasterPO;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmSupervisorMasterPO> getSupervisorMobileNumberWithTokenDetails(String mobileNumber,String tempCode,String branchId){
		List <EFmFmSupervisorMasterPO> eFmFmSupervisorMasterPO = new ArrayList<EFmFmSupervisorMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmSupervisorMasterPO b JOIN b.efmFmVendorMaster v "
				+ " JOIN  v.eFmFmClientBranchPO c where c.branchId in ("+branchId+") AND TRIM(b.mobileNumber)=TRIM('"+mobileNumber+"')  AND b.tempCode='" + tempCode + "' ");		
		eFmFmSupervisorMasterPO=query.getResultList();	
		return eFmFmSupervisorMasterPO;
		
	}

	    @Override
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmSupervisorMasterPO> getSupervisorPasswordDetailsFromSupervisorAndBranchId(int supervisorId, String branchId) {
	        List<EFmFmSupervisorMasterPO> eFmFmSupervisorMasterPO = new ArrayList<EFmFmSupervisorMasterPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmSupervisorMasterPO b JOIN b.efmFmVendorMaster v JOIN  v.eFmFmClientBranchPO c  "
	                + " where c.branchId in ("+branchId+") "
	                		+ " AND b.supervisorId = '" + supervisorId + "'");
	        eFmFmSupervisorMasterPO = query.getResultList();
	        return eFmFmSupervisorMasterPO;
	    }

		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmEscortDocsPO> getEscortuploadFileDetails(int escortId, String docType) {
			List<EFmFmEscortDocsPO> uploadFileDetails = new ArrayList<EFmFmEscortDocsPO>();
			Query query = entityManager
					.createQuery("SELECT a FROM EFmFmEscortDocsPO a JOIN a.eFmFmEscortMaster  b  where  b.escortId='" +escortId+"' and a.documentName=TRIM('"+docType+"') and a.status='Y' order by a.escortDocId desc ");
			uploadFileDetails = query.getResultList();
			return uploadFileDetails;
		}
	 
	 
	 
	 
	
}
