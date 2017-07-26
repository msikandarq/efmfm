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

import com.newtglobal.eFmFmFleet.business.dao.IEmployeeDetailDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLocationMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserPasswordPO;

@Repository("IEmployeeDetailDAO")
public class EmployeeDetailDAOImpl implements IEmployeeDetailDAO {

    private static Log log = LogFactory.getLog(EmployeeDetailDAOImpl.class);
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager _entityManager) {
        this.entityManager = _entityManager;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(EFmFmUserPasswordPO eFmFmUserPasswordPO) {
        entityManager.persist(eFmFmUserPasswordPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void update(EFmFmUserPasswordPO eFmFmUserPasswordPO) {
        entityManager.merge(eFmFmUserPasswordPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(EFmFmUserMasterPO eFmFmUserMasterPO) {
        entityManager.persist(eFmFmUserMasterPO);
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

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteLastPasswordForParticularEmployeCrossingDefineLimit(int passwordId) {
        Query query = entityManager.createQuery("DELETE EFmFmUserPasswordPO where passwordId = '" + passwordId + "' ");
        query.executeUpdate();
    }

       
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromClientId(String branchId) {
        List<EFmFmUserMasterPO> employeeDetails = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.status='Y'");
        log.info("Hibernate Cache This object");
        employeeDetails = query.getResultList();
        return employeeDetails;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromBranchId(String branchId,int startPgNo,int endPgNo) {
        List<EFmFmUserMasterPO> employeeDetails = new ArrayList<EFmFmUserMasterPO>();
        log.info("branchId"+branchId+"startPgNo"+startPgNo+"endPgNo"+endPgNo);
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.status='Y'")
                .setFirstResult(startPgNo)
                .setMaxResults(endPgNo); 
        log.info("Hibernate Cache This object");
        employeeDetails = query.getResultList();
        return employeeDetails;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> loginEmployeeDetails(String employeeId, String password) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery("SELECT b FROM EFmFmUserMasterPO b   where b.userName = '" + employeeId
                + "' AND b.password='" + password + "' AND b.status='Y' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> loginEmployeeDetailsFromMobileNumber(String mobileNumber, String password) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery("SELECT b FROM EFmFmUserMasterPO b   where b.mobileNumber = '"
                + mobileNumber + "' AND b.password='" + password + "' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    /**
     * The getParticularEmployeeDetails implements for getting particular
     * employees details from mobile number and branch id.
     *
     * @author Sarfraz Khan
     * 
     * @since 2016-05-20
     */

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromMobileNumberAndBranhId(String mobileNumber,
            int branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
                        + branchId + "' AND b.mobileNumber = '" + mobileNumber + "' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    /**
     * The getParticularEmployeeDetails implements for getting particular
     * employees details from mobile number.
     *
     * @author Sarfraz Khan
     * 
     * @since 2016-06-17
     */

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromMobileNumber(String mobileNumber) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b  where  b.mobileNumber = '"
                        + mobileNumber + "' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    /**
     * The getParticularEmployeeDetails implements for getting particular
     * employees details.
     *
     * @author Rajan R
     * 
     * @since 2015-05-05
     */

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromUserId(int userId, int branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
                        + branchId + "' AND b.userId = '" + userId + "' AND b.status='Y' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    // Get Employee details from UserId
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromUserIdWithOutStatus(int userId, String branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b where b.userId = '" + userId + "' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    /*
     * get Active Employees Details By EmployeeId.
     * 
     * @see com.newtglobal.eFmFmFleet.business.dao.IEmployeeDetailDAO#
     * getParticularEmpDetailsFromEmployeeId(java.lang.String, int)
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromEmployeeId(String employeeId,String branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b where  b.employeeId = '" + employeeId + "' AND b.status='Y' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }
    
    
    
    /*
     * get Active Employees Details By UserName.
     * 
     * @see com.newtglobal.eFmFmFleet.business.dao.IEmployeeDetailDAO#
     * getParticularEmpDetailsFromEmployeeId(java.lang.String, int)
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromUserName(String userName, int branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
                        + branchId + "' AND b.userName = '" + userName + "'");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }
    
    
    /*
     * get Active Employees Details By EmployeeId and branch Id for token
     * Confirmation.
     * 
     * @see com.newtglobal.eFmFmFleet.business.dao.IEmployeeDetailDAO#
     * getParticularEmpDetailsFromEmployeeId(java.lang.String, int)
     */

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromEmployeeIdAndbranchIdAndToken(String mobileNumber,
            String tempCode, int branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='" + branchId
                        + "' AND b.tempCode='" + tempCode + "' AND b.mobileNumber = '" + mobileNumber + "'  ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    /*
     * get Active and non Active Employees Details By mobileNumber.
     * 
     * @see com.newtglobal.eFmFmFleet.business.dao.IEmployeeDetailDAO#
     * getEmpDetailsFromMobileNumberAndBranchId(java.lang.String, int)
     */

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getEmpDetailsFromMobileNumberAndBranchId(String mobileNumber, String branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  "
                		+ " where c.branchId in ("+branchId+")"
                				+ " AND b.mobileNumber = '" + mobileNumber + "'");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    /*
     * get Active and non Active Employees Details By EmployeeId.
     * 
     * @see com.newtglobal.eFmFmFleet.business.dao.IEmployeeDetailDAO#
     * getParticularEmpDetailsFromEmployeeId(java.lang.String, int)
     */

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getEmpDetailsFromEmployeeIdAndBranchId(String employeeId, String branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.employeeId = '" + employeeId + "'");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }
    
    

    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromEmailId(String emailId, String branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.emailId = '" + emailId + "' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }


    /*
     * get Employees Details By EmployeeId.
     * 
     * @see com.newtglobal.eFmFmFleet.business.dao.IEmployeeDetailDAO#
     * getParticularEmpDetailsFromEmployeeIdForGuest(java.lang.String, int)
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getEmpDetailsFromEmployeeId(String employeeId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b  where  b.employeeId = '" + employeeId + "' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    // For Guest EmployeeId Check.
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromEmployeeIdForGuest(String employeeId, int branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='" + branchId
                        + "' AND b.employeeId = '" + employeeId + "' AND b.userType='employee' AND b.status='Y' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }
    
    
    
 // For Guest MobileNumber Check.
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getEmployeeTypeDetailsByBranchId(String userType, String branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.userType = '" + userType + "'");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    // For Guest MobileNumber Check.
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getEmpMobileNumberCheck(String mobileNumber, int branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='" + branchId
                        + "' AND b.mobileNumber = '" + mobileNumber + "' AND b.userType='employee' AND b.status='Y' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    // For Guest EmployeeId Check Exist Check.
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularGuestDetailsFromEmployeeId(String employeeId, int branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='" + branchId
                        + "' AND b.employeeId = '" + employeeId + "' AND b.userType='guest' AND b.status='Y' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularEmployeeDetailsFromEmailId(String emailId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b  where b.emailId = '" + emailId + "' AND b.status='Y' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }
    
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularEmployeeDetailsFromUserName(String userName) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b  where b.userName = '" + userName + "' AND b.status='Y' ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }
    
    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getParticularEmployeeDetailsFromDeviceIdAndMobileNumber(String deviceId,
            int branchId, String mobileNumber) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='" + branchId
                        + "' AND b.deviceId = '" + deviceId + "' AND b.mobileNumber = '" + mobileNumber + "'");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EFmFmUserMasterPO getParticularDeviceDetails(String deviceId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = null;
        Query query = entityManager
                .createQuery("SELECT s FROM EFmFmUserMasterPO as s WHERE s.deviceId = '" + deviceId + "' ");
        try {
            eFmFmEmployeeMasterPO = query.getResultList();
        } catch (Exception e) {
        }
        if (eFmFmEmployeeMasterPO == null || eFmFmEmployeeMasterPO.isEmpty()) {
            return null;
        }
        return eFmFmEmployeeMasterPO.get(0);
    }

    @Override
    public boolean doesDeviceExist(String deviceId, int branchId) {
        List<EFmFmUserMasterPO> deviceCheck = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT s.deviceId FROM EFmFmUserMasterPO  s JOIN s.eFmFmClientBranchPO c WHERE s.deviceId = '"
                        + deviceId + "' AND c.branchId='" + branchId + "'");
        deviceCheck = query.getResultList();
        if (deviceCheck.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean doesEmailIdExist(String emailId, int branchId) {
        List<EFmFmUserMasterPO> emailCheck = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT s.emailId FROM EFmFmUserMasterPO  s JOIN s.eFmFmClientBranchPO c WHERE s.emailId = '" + emailId
                        + "' AND c.branchId='" + branchId + "'");
        emailCheck = query.getResultList();
        if (emailCheck.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<EFmFmClientBranchPO> doesClientCodeExist(String branchCode) {
        List<EFmFmClientBranchPO> branchCodeCheck = new ArrayList<EFmFmClientBranchPO>();
        Query query = entityManager.createQuery("SELECT s FROM EFmFmClientBranchPO  s WHERE TRIM(s.branchCode)='"+branchCode+"'");
        branchCodeCheck = query.getResultList();
        return branchCodeCheck;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO) {
        entityManager.persist(eFmFmClientProjectDetailsPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmClientProjectDetailsPO> getProjectDetails(String projectId, String branchId) {
        List<EFmFmClientProjectDetailsPO> eFmFmClientProjectDetailsPO = new ArrayList<EFmFmClientProjectDetailsPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmClientProjectDetailsPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND UPPER(REPLACE(b.clientProjectId,' ',''))=TRIM(UPPER(REPLACE('"+projectId+"',' ','')))  AND b.isActive='A' ");       
        eFmFmClientProjectDetailsPO = query.getResultList();
        return eFmFmClientProjectDetailsPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getEmpMobileNoDetails(String mobileNo,String branchId) {
        List<EFmFmUserMasterPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c where b.mobileNumber = '" + mobileNo + "' AND c.branchId in ("+ branchId + ") ");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserPasswordPO> getUserPasswordDetailsFromUserIdAndBranchId(int userId) {
        List<EFmFmUserPasswordPO> eFmFmEmployeeMasterPO = new ArrayList<EFmFmUserPasswordPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmUserPasswordPO b JOIN b.efmFmUserMaster u  where  u.userId = '" + userId + "'");
        eFmFmEmployeeMasterPO = query.getResultList();
        return eFmFmEmployeeMasterPO;
    }


	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveLocationMaster(EFmFmLocationMasterPO eFmFmLocationMasterPO) {
        entityManager.persist(eFmFmLocationMasterPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateLocationMaster(EFmFmLocationMasterPO eFmFmLocationMasterPO) {
        entityManager.merge(eFmFmLocationMasterPO);
    }	

	@Override
	public List<EFmFmLocationMasterPO> getLocationNameExist(String locationName, String branchId) {
	    List<EFmFmLocationMasterPO> eFmFmLocationMasterPO = new ArrayList<EFmFmLocationMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmLocationMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND UPPER(REPLACE(b.locationName,' ',''))=TRIM(UPPER(REPLACE('"+locationName+"',' ','')))");
        eFmFmLocationMasterPO = query.getResultList();
        return eFmFmLocationMasterPO;
	}
	
		
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmLocationMasterPO> getAllActiveLocation(String isActive, String branchId) {
        List<EFmFmLocationMasterPO> eFmFmLocationMasterPO = new ArrayList<EFmFmLocationMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmLocationMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.isActive='"+isActive+"' ");
        eFmFmLocationMasterPO = query.getResultList();
        return eFmFmLocationMasterPO;
    }
	
	

	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmLocationMasterPO> getMultipleLocation(String locationId, String branchId) {
		 List<EFmFmLocationMasterPO> eFmFmLocationMasterPO = new ArrayList<EFmFmLocationMasterPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmLocationMasterPO b where b.isActive='A' AND b.locationId IN ("+ locationId + ")");
	        eFmFmLocationMasterPO = query.getResultList();
	        return eFmFmLocationMasterPO;
	}


	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeProjectDetailsPO> getListOfProjectIdByUserId(int userId, String branchId) {
        List<EFmFmEmployeeProjectDetailsPO> employeeDetails = new ArrayList<EFmFmEmployeeProjectDetailsPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmEmployeeProjectDetailsPO p JOIN p.efmFmUserMaster b JOIN b.eFmFmClientBranchPO c  "
                		+ " where c.branchId in ("+ branchId + ") AND b.userId='"+userId+"' AND p.isActive='Y'");        
        employeeDetails = query.getResultList();
        return employeeDetails;
    }
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeProjectDetailsPO> getListOfUserByreportingManager(int reportingManagerUserId, String branchId,int userId) {
        List<EFmFmEmployeeProjectDetailsPO> employeeDetails = new ArrayList<EFmFmEmployeeProjectDetailsPO>();
        Query query = entityManager
                .createQuery("SELECT p FROM EFmFmEmployeeProjectDetailsPO p JOIN p.efmFmUserMaster b JOIN b.eFmFmClientBranchPO c  "
                		+ " where c.branchId in ("+ branchId + ") AND p.reportingManagerUserId='"+reportingManagerUserId+"' AND b.userId='"+userId+"' "
                						+ " AND p.isActive='Y'");        
        employeeDetails = query.getResultList();
        return employeeDetails;
    }
	
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeProjectDetailsPO> getAllProjectUserByrepManager(int reportingManagerUserId, int branchId) {
        List<EFmFmEmployeeProjectDetailsPO> employeeDetails = new ArrayList<EFmFmEmployeeProjectDetailsPO>();
        Query query = entityManager
                .createQuery("SELECT p FROM EFmFmEmployeeProjectDetailsPO p JOIN p.efmFmUserMaster b JOIN b.eFmFmClientBranchPO c  "
                		+ " where c.branchId='"+ branchId + "' "
                				+ " AND p.reportingManagerUserId='"+reportingManagerUserId+"'"
                						+ " AND p.isActive='Y'");        
        employeeDetails = query.getResultList();
        return employeeDetails;
    }
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeProjectDetailsPO> getAllProjectUserByrepManagerWithProjectId(int reportingManagerUserId, String branchId,int projectId) {
        List<EFmFmEmployeeProjectDetailsPO> employeeDetails = new ArrayList<EFmFmEmployeeProjectDetailsPO>();
        Query query = entityManager
                .createQuery("SELECT p FROM EFmFmEmployeeProjectDetailsPO p JOIN p.efmFmUserMaster b "
                		+ "	JOIN b.eFmFmClientBranchPO c JOIN p.eFmFmClientProjectDetails s  "
                		+ " where c.branchId in ("+ branchId + ") AND p.reportingManagerUserId='"+reportingManagerUserId+"'"
                						+ " AND p.isActive='Y'  AND s.projectId='"+projectId+"' ");        
        employeeDetails = query.getResultList();
        return employeeDetails;
    }
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeProjectDetailsPO> getClientProjectIdByMangerAndEmployee(int reportingManagerUserId, String branchId,int projectId,int userId) {
        List<EFmFmEmployeeProjectDetailsPO> employeeDetails = new ArrayList<EFmFmEmployeeProjectDetailsPO>();
        Query query = entityManager
                .createQuery("SELECT p FROM EFmFmEmployeeProjectDetailsPO p JOIN p.efmFmUserMaster b "
                		+ "	JOIN b.eFmFmClientBranchPO c JOIN p.eFmFmClientProjectDetails s  "
                		+ " where c.branchId in ("+ branchId + ") AND b.userId='"+userId+"' "
                				+ " AND p.reportingManagerUserId='"+reportingManagerUserId+"'"
                						+ " AND p.isActive='Y'  AND s.projectId='"+projectId+"' ");        
        employeeDetails = query.getResultList();
        return employeeDetails;
    }

	 @Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public void addEmployeeProjectDetails(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO) {
	        entityManager.persist(eFmFmEmployeeProjectDetailsPO);
	 }

	@Override
	public List<EFmFmClientProjectDetailsPO> getListOfProjectDetails(String activeStatus, String branchId) {
	   List<EFmFmClientProjectDetailsPO> eFmFmClientProjectDetailsPO = new ArrayList<EFmFmClientProjectDetailsPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmClientProjectDetailsPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.isActive='"+activeStatus+"' ");       
	        eFmFmClientProjectDetailsPO = query.getResultList();
	        return eFmFmClientProjectDetailsPO;
	}

	@Override
	public List<EFmFmClientProjectDetailsPO> getParticularProjectDetails(int projectId, String branchId) {
		 List<EFmFmClientProjectDetailsPO> eFmFmClientProjectDetailsPO = new ArrayList<EFmFmClientProjectDetailsPO>();
	        Query query = entityManager.createQuery(
	                " SELECT b FROM EFmFmClientProjectDetailsPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.projectId ='"+projectId+"' AND b.isActive='A' ");       
	        eFmFmClientProjectDetailsPO = query.getResultList();
	        return eFmFmClientProjectDetailsPO;
	}


	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateEmployeeProjectDetails(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO) {
        entityManager.merge(eFmFmEmployeeProjectDetailsPO);
    }

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateClientProject(EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO) {
		  entityManager.merge(eFmFmClientProjectDetailsPO);
	}

	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeProjectDetailsPO> getAllEmployeeByProjectId(int branchId, int projectId) {
        List<EFmFmEmployeeProjectDetailsPO> employeeDetails = new ArrayList<EFmFmEmployeeProjectDetailsPO>();
        Query query = entityManager
                .createQuery("SELECT p FROM EFmFmEmployeeProjectDetailsPO p JOIN p.eFmFmClientProjectDetails s"
                		+ "	JOIN s.eFmFmClientBranchPO c "
                		+ " where c.branchId='"+ branchId + "' AND p.isActive='Y'  AND s.projectId='"+projectId+"' ");        
        employeeDetails = query.getResultList();
        return employeeDetails;
    }
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getListOfEmployeeDetailsByBranch(String branchId) {
        List<EFmFmUserMasterPO> employeeDetails = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId in ("+ branchId + ") AND b.status='Y' AND b.userType ='employee' ");
        log.info("Hibernate Cache This object");
        employeeDetails = query.getResultList();
        return employeeDetails;
    }
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeProjectDetailsPO> getDeligatedUserDetails(String branchId, int repMngUserId) {
        List<EFmFmEmployeeProjectDetailsPO> employeeDetails = new ArrayList<EFmFmEmployeeProjectDetailsPO>();
        Query query = entityManager
                .createQuery("SELECT p FROM EFmFmEmployeeProjectDetailsPO p JOIN p.eFmFmClientProjectDetails s"
                		+ "	JOIN s.eFmFmClientBranchPO c "
                		+ " where c.branchId in ("+ branchId + ") AND p.isActive='Y'  AND p.delegatedBy='"+repMngUserId+"' ");        
        employeeDetails = query.getResultList();
        return employeeDetails;
    }
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeProjectDetailsPO> getAllDeligatedUserDetails(String branchId) {
        List<EFmFmEmployeeProjectDetailsPO> employeeDetails = new ArrayList<EFmFmEmployeeProjectDetailsPO>();
        Query query = entityManager
                .createQuery("SELECT p FROM EFmFmEmployeeProjectDetailsPO p JOIN p.eFmFmClientProjectDetails s"
                		+ "	JOIN s.eFmFmClientBranchPO c "
                		+ " where c.branchId in ("+ branchId + ") AND p.isActive='Y' and p.isDelegatedUser =1");        
        employeeDetails = query.getResultList();
        return employeeDetails;
    }
	
	//Code for master data entry comman master data for all locations.
	
	@Override
    public boolean doesCommanEmailIdExist(String emailId) {
        List<EFmFmUserMasterPO> emailCheck = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT s.emailId FROM EFmFmUserMasterPO  s WHERE s.emailId = '" + emailId+ "'");
        emailCheck = query.getResultList();
        if (!(emailCheck.isEmpty())) {
            return true;
        }
        return false;
    }
	
	@Override
    public boolean doesCommanMobileNumberExist(String mobileNumber) {
        List<EFmFmUserMasterPO> emailCheck = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT s.emailId FROM EFmFmUserMasterPO  s WHERE s.mobileNumber = '" + mobileNumber+ "'");
        emailCheck = query.getResultList();
        if (!(emailCheck.isEmpty())) {
            return true;
        }
        return false;
    }
	
	@Override
    public boolean doesCommanEmployeeIdExist(String employeeId) {
        List<EFmFmUserMasterPO> emailCheck = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT s.emailId FROM EFmFmUserMasterPO  s WHERE s.employeeId = '" + employeeId+ "'");
        emailCheck = query.getResultList();
        if (!(emailCheck.isEmpty())) {
            return true;
        }
        return false;
    }
	
	@Override
    public boolean doesCommanUseNameExist(String userName) {
        List<EFmFmUserMasterPO> emailCheck = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT s.emailId FROM EFmFmUserMasterPO  s WHERE s.userName = '" + userName+ "'");
        emailCheck = query.getResultList();
        if (!(emailCheck.isEmpty())) {
            return true;
        }
        return false;
    }
	
		@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeProjectDetailsPO> getDeligatedUserDetailsByReportingManager(int branchId, int delegatedBy,int repManagerId,int projectId) {
        List<EFmFmEmployeeProjectDetailsPO> employeeDetails = new ArrayList<EFmFmEmployeeProjectDetailsPO>();
        Query query = entityManager
                .createQuery("SELECT p FROM EFmFmEmployeeProjectDetailsPO p JOIN p.eFmFmClientProjectDetails s"
                		+ "	JOIN s.eFmFmClientBranchPO c "
                		+ " where c.branchId='"+ branchId + "' AND p.isActive='Y' "
                				+ " AND p.isDelegatedUser =1 AND p.delegatedBy='"+delegatedBy+"' "
                						+ " AND s.projectId='"+ projectId+"' "
                								+ " AND p.reportingManagerUserId ='"+repManagerId+"' ");        
        employeeDetails = query.getResultList();
        return employeeDetails;
    }
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeProjectDetailsPO> getParticularProjectAllocation(int delegatedId) {
        List<EFmFmEmployeeProjectDetailsPO> employeeDetails = new ArrayList<EFmFmEmployeeProjectDetailsPO>();
        Query query = entityManager
                .createQuery("SELECT p FROM EFmFmEmployeeProjectDetailsPO p "
                		+ " where p.isActive='Y' AND p.empProjectId='"+delegatedId+"'");        
        employeeDetails = query.getResultList();
        return employeeDetails;
    }	
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmLocationMasterPO> getAllActiveAndPendingLocation(int branchId) {
        List<EFmFmLocationMasterPO> eFmFmLocationMasterPO = new ArrayList<EFmFmLocationMasterPO>();
        Query query = entityManager.createQuery(
                "SELECT b FROM EFmFmLocationMasterPO b JOIN b.eFmFmClientBranchPO c  where c.branchId='"
                        + branchId + "'");
        eFmFmLocationMasterPO = query.getResultList();
        return eFmFmLocationMasterPO;
    }
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmUserMasterPO> getAllEmployeeDetailsByPagination(String branchId,int startPgNo,int endPgNo) {
        List<EFmFmUserMasterPO> employeeDetails = new ArrayList<EFmFmUserMasterPO>();
        Query query = entityManager
                .createQuery("SELECT b FROM EFmFmUserMasterPO b JOIN b.eFmFmClientBranchPO c  "
                		+ " where c.branchId in ("+ branchId + ") AND b.status='Y' order by b.userId asc ")
                .setFirstResult(startPgNo)
                .setMaxResults(endPgNo);       
        employeeDetails = query.getResultList();
        return employeeDetails;
    }
	
	
	
	

}
