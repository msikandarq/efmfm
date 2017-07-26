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

import com.newtglobal.eFmFmFleet.business.dao.IVendorDetailsDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverFeedbackPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;

@Repository("IVendorDetailsDAO")
public class VendorDetailsDAOImpl implements IVendorDetailsDAO{

	@PersistenceContext
	private EntityManager entityManager;	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		entityManager.persist(eFmFmVendorMasterPO);
		
	}
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		entityManager.merge(eFmFmVendorMasterPO);
		
	}
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
		entityManager.remove(eFmFmVendorMasterPO);
		
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmDriverFeedbackPO driverFeedbackPO) {
		entityManager.persist(driverFeedbackPO);

	}
	/**
	* The getAllVendorName implements for
	* for validating and getting all vendor names from vendor master table. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorMasterPO> getAllVendorName(String vendorName,String branchId){
		List <EFmFmVendorMasterPO> eFmFmVendorMasterPO = new ArrayList<EFmFmVendorMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmVendorMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+branchId+") and TRIM(UPPER(b.vendorName))=TRIM(UPPER('"+vendorName+"'))");		
		eFmFmVendorMasterPO=query.getResultList();	
		return eFmFmVendorMasterPO;
		
	}
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public EFmFmVendorMasterPO getParticularVendorDetail(int vendorId) {
		List<EFmFmVendorMasterPO> vendorDetail  =new ArrayList<EFmFmVendorMasterPO>();
    	Query query=entityManager.createQuery("SELECT b FROM EFmFmVendorMasterPO as b  where b.vendorId='"+vendorId+"'");
    	vendorDetail=query.getResultList();
		return vendorDetail.get(0);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorMasterPO> getAllVendorsDetails(EFmFmVendorMasterPO vendorMasterPO) {
		List <EFmFmVendorMasterPO> eFmFmVendorMasterPO = new ArrayList<EFmFmVendorMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmVendorMasterPO b JOIN b.eFmFmClientBranchPO  c where c.branchId='"+vendorMasterPO.geteFmFmClientBranchPO().getBranchId()+"' ");		
		eFmFmVendorMasterPO=query.getResultList();	
		entityManager.close();
		entityManager.clear();
		entityManager.flush();
		return eFmFmVendorMasterPO;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorMasterPO> getAllEnableVendorsDetails(String branchId) {
		List <EFmFmVendorMasterPO> eFmFmVendorMasterPO = new ArrayList<EFmFmVendorMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmVendorMasterPO b JOIN b.eFmFmClientBranchPO  c "
				+ " where c.branchId in ("+branchId+") "
						+ " AND (b.status='A' OR b.status='allocated' OR b.status='Y') "
						+ " AND b.vendorName NOT LIKE '%DUMMY%' ");		
		eFmFmVendorMasterPO=query.getResultList();	
		return eFmFmVendorMasterPO;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorMasterPO> getAllApprovedVendorsDetails(String branchId) {
		List <EFmFmVendorMasterPO> eFmFmVendorMasterPO = new ArrayList<EFmFmVendorMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmVendorMasterPO b JOIN b.eFmFmClientBranchPO  c "
				+ " where c.branchId in ("+branchId+") AND b.status='A' AND b.vendorName NOT LIKE '%DUMMY%' ");		
		eFmFmVendorMasterPO=query.getResultList();	
		return eFmFmVendorMasterPO;
	}

	@Override
	public boolean doesDriverDeviceExist(String deviceId,int branchId) {
		List<EFmFmDriverMasterPO> deviceCheck = new ArrayList<EFmFmDriverMasterPO>();
    	Query query=entityManager.createQuery("SELECT s.deviceId FROM EFmFmDriverMasterPO  s "
    			+ " JOIN s.efmFmVendorMaster v "
    			+ " WHERE s.deviceId = '" + deviceId + "' ");
    	deviceCheck=query.getResultList();
    	if(deviceCheck.size()>0){
    		return true;
    	}
    		return false ;
	}

	@Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmDeviceMasterPO> getAllDeviceDetails(int branchId,Date todayDate) {
	  List <EFmFmDeviceMasterPO> eFmFmDeviceMasterPO = new ArrayList<EFmFmDeviceMasterPO>();
	  Query query=entityManager.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO c WHERE NOT EXISTS (SELECT c from b.efmFmVehicleCheckIns v where DATE(v.checkOutTime) is null ) and c.branchId='"+branchId+"' AND  b.status='Y' ");  
	  eFmFmDeviceMasterPO=query.getResultList(); 
	  return eFmFmDeviceMasterPO;
	 }
	
	 @Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmDeviceMasterPO> getAllDeviceDetailsFromBranchId(String branchId) {
		  List <EFmFmDeviceMasterPO> eFmFmDeviceMasterPO = new ArrayList<EFmFmDeviceMasterPO>();
		  Query query=entityManager.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO c "
		  		+ " WHERE c.branchId in ("+branchId+") AND  b.status='Y' ");  
		  eFmFmDeviceMasterPO=query.getResultList(); 
		  return eFmFmDeviceMasterPO;
	 }

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorMasterPO> getVendorMobileNo(String vendorMobileNo, String branchId) {
		List <EFmFmVendorMasterPO> eFmFmVendorMasterPO = new ArrayList<EFmFmVendorMasterPO>();
	    Query query=entityManager.createQuery("SELECT b FROM EFmFmVendorMasterPO b JOIN b.eFmFmClientBranchPO c "
	    		+ " where c.branchId in ("+branchId+") and TRIM(b.vendorMobileNo)=TRIM('"+vendorMobileNo+"')");		
		eFmFmVendorMasterPO=query.getResultList();	
		return eFmFmVendorMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorMasterPO> getVendorTinNumber(String tinNumber, String branchId) {
		List <EFmFmVendorMasterPO> eFmFmVendorMasterPO = new ArrayList<EFmFmVendorMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmVendorMasterPO b "
				+ " JOIN b.eFmFmClientBranchPO c where c.branchId in("+branchId+") and TRIM(UPPER(b.tinNumber))=TRIM(UPPER('"+tinNumber+"'))");		
		eFmFmVendorMasterPO=query.getResultList();	
		return eFmFmVendorMasterPO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorMasterPO> getVendorEmailId(String emailID, String branchId) {
		List <EFmFmVendorMasterPO> eFmFmVendorMasterPO = new ArrayList<EFmFmVendorMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmVendorMasterPO b JOIN b.eFmFmClientBranchPO c "
				+ " where c.branchId in ("+branchId+") and TRIM(b.emailId)=TRIM('"+emailID+"')");		
		eFmFmVendorMasterPO=query.getResultList();	
		return eFmFmVendorMasterPO;
	}

	@Override
	  @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmDeviceMasterPO> getAllActiveDeviceDetails(int branchId) {
	   List <EFmFmDeviceMasterPO> eFmFmDeviceMasterPO = new ArrayList<EFmFmDeviceMasterPO>();  
	   Query query=entityManager.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO c WHERE NOT EXISTS (SELECT c from b.efmFmVehicleCheckIns v JOIN v.efmFmDriverMaster d JOIN v.efmFmVehicleMaster s where s.status='A' and d.status='A' and b.isActive='Y' ) and c.branchId='"+branchId+"' AND  b.isActive='Y' ");  
	   eFmFmDeviceMasterPO=query.getResultList(); 
	   return eFmFmDeviceMasterPO;
	   
	  }

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVendorMasterPO> getAllVendorDetailsByVendorId(String branchId, int vendorId) {
		List <EFmFmVendorMasterPO> eFmFmVendorMasterPO = new ArrayList<EFmFmVendorMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmVendorMasterPO b JOIN b.eFmFmClientBranchPO c "
				+ " where c.branchId in ("+branchId+") and b.vendorId='"+vendorId+"'");		
		eFmFmVendorMasterPO=query.getResultList();	
		return eFmFmVendorMasterPO;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmVehicleMasterPO> getAllActiveVehicleDetails(int vendorId) {
		List<EFmFmVehicleMasterPO> vehicleMasterPO = new ArrayList<EFmFmVehicleMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmVehicleMasterPO b JOIN b.efmFmVendorMaster c  where c.vendorId='"
						+vendorId + "' and b.status !='D' ");
		vehicleMasterPO = query.getResultList();
		return vehicleMasterPO;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getAllActiveDriverDetails(int vendorId) {
		List<EFmFmDriverMasterPO> driverMasterPO = new ArrayList<EFmFmDriverMasterPO>();
		Query query = entityManager
				.createQuery("SELECT b FROM EFmFmDriverMasterPO b JOIN b.efmFmVendorMaster c JOIN c.eFmFmClientBranchPO d where c.vendorId='"
						+ vendorId + "' and b.status !='D' ");
		driverMasterPO = query.getResultList();
		return driverMasterPO;
	}
	
	
	
	
}
