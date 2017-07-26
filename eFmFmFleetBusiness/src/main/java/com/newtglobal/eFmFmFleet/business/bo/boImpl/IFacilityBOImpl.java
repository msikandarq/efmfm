package com.newtglobal.eFmFmFleet.business.bo.boImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtglobal.eFmFmFleet.business.bo.IFacilityBO;
import com.newtglobal.eFmFmFleet.business.dao.IFacilityDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmFacilityToFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;

@Service("IFacilityBO")
public class IFacilityBOImpl implements IFacilityBO {	
	
	    @Autowired
	    IFacilityDAO iFacilityDAO;

	    public void setiFacilityDAO(IFacilityDAO iFacilityDAO) {
	        this.iFacilityDAO = iFacilityDAO;
	    }

	    @Override
	    public void save(EFmFmFacilityToFacilityMappingPO  eFmFmFacilityToFacilityMappingPO) {
	    	iFacilityDAO.save(eFmFmFacilityToFacilityMappingPO);

	    }
	
	    @Override
	    public void update(EFmFmFacilityToFacilityMappingPO  eFmFmFacilityToFacilityMappingPO) {
	    	iFacilityDAO.update(eFmFmFacilityToFacilityMappingPO);
	    }

	    @Override
	    public void delete(EFmFmFacilityToFacilityMappingPO  eFmFmFacilityToFacilityMappingPO) {
	    	iFacilityDAO.delete(eFmFmFacilityToFacilityMappingPO);
	    }	    

	    @Override
	    public void save(EFmFmUserFacilityMappingPO  eFmFmUserFacilityMappingPO) {
	    	iFacilityDAO.save(eFmFmUserFacilityMappingPO);

	    }
	
	    @Override
	    public void update(EFmFmUserFacilityMappingPO  eFmFmUserFacilityMappingPO) {
	    	iFacilityDAO.update(eFmFmUserFacilityMappingPO);
	    }

	    @Override
	    public void delete(EFmFmUserFacilityMappingPO  eFmFmUserFacilityMappingPO) {
	    	iFacilityDAO.delete(eFmFmUserFacilityMappingPO);
	    }

		@Override
		public List<EFmFmFacilityToFacilityMappingPO> getAllAttachedFacilities(int branchId) {
			return iFacilityDAO.getAllAttachedFacilities(branchId);
		}

		@Override
		public List<EFmFmUserFacilityMappingPO> getAllFacilitiesAttachedToUser(int userId) {
			return iFacilityDAO.getAllFacilitiesAttachedToUser(userId);
		}

		@Override
		public boolean facilityToFacilityCheck(int branchId, String branchName) {
			return iFacilityDAO.facilityToFacilityCheck(branchId, branchName);
		}

		@Override
		public List<EFmFmFacilityToFacilityMappingPO> getAllBaseClientIdFromBranchName(String branchName) {
			return iFacilityDAO.getAllBaseClientIdFromBranchName(branchName);
		}

		@Override
		public List<EFmFmFacilityToFacilityMappingPO> getAllAttachedActiveFacilities(int branchId) {
			return iFacilityDAO.getAllAttachedActiveFacilities(branchId);
		}

		@Override
		public List<EFmFmFacilityToFacilityMappingPO> getAllActiveFacilities() {
			return iFacilityDAO.getAllActiveFacilities();
		}

		@Override
		public List<EFmFmFacilityToFacilityMappingPO> getAllInActiveFacilities() {
			return iFacilityDAO.getAllInActiveFacilities();
		}

		@Override
		public List<EFmFmFacilityToFacilityMappingPO> getParticularFacilityDetail(int facilityToFacilityoId) {
			return iFacilityDAO.getParticularFacilityDetail(facilityToFacilityoId);
		}

		@Override
		public List<EFmFmFacilityToFacilityMappingPO> getParticularFacilityDetailFromBranchName(String branchName) {
			return iFacilityDAO.getParticularFacilityDetailFromBranchName(branchName);
		}

		@Override
		public List<EFmFmFacilityToFacilityMappingPO> getAllInactiveFacilities() {
			return iFacilityDAO.getAllInactiveFacilities();
		}

		@Override
		public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromEmailId(String emailId) {
			return iFacilityDAO.getAllEmployeeDetailsFromEmailId(emailId);
		}

		@Override
		public List<EFmFmUserMasterPO> getEmpDetailsFromEmployeeIdAndBranchId(String employeeId) {
			return iFacilityDAO.getEmpDetailsFromEmployeeIdAndBranchId(employeeId);
		}

		@Override
		public List<EFmFmUserMasterPO> getEmpMobileNoDetails(String mobileNo) {
			return iFacilityDAO.getEmpMobileNoDetails(mobileNo);
		}

		@Override
		public List<EFmFmUserFacilityMappingPO> getAttachedParticularFacilityDetail(int userId, int branchId) {
			return iFacilityDAO.getAttachedParticularFacilityDetail(userId, branchId);
		}

		@Override
		public boolean checkFacilityAccess(int userId, int branchId) {
			return iFacilityDAO.checkFacilityAccess(userId, branchId);
		}

		@Override
		public List<EFmFmUserFacilityMappingPO> getAllFacilitiesAttachedToParticularUser(int userId) {
			return iFacilityDAO.getAllFacilitiesAttachedToParticularUser(userId);
		}
	
}

