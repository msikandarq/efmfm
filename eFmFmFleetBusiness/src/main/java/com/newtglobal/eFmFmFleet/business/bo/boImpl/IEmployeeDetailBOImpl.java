package com.newtglobal.eFmFmFleet.business.bo.boImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtglobal.eFmFmFleet.business.bo.IEmployeeDetailBO;
import com.newtglobal.eFmFmFleet.business.dao.IEmployeeDetailDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLocationMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserPasswordPO;

@Service("IEmployeeDetailBO")
public class IEmployeeDetailBOImpl implements IEmployeeDetailBO {

    @Autowired
    IEmployeeDetailDAO employeeDetailDAO;

    public void setIUserMasterDAO(IEmployeeDetailDAO employeeDetailDAO) {
        this.employeeDetailDAO = employeeDetailDAO;
    }

    @Override
    public void save(EFmFmUserMasterPO eFmFmUserMasterPO) {
        employeeDetailDAO.save(eFmFmUserMasterPO);
    }

    @Override
    public void update(EFmFmUserMasterPO eFmFmUserMasterPO) {
        employeeDetailDAO.update(eFmFmUserMasterPO);
    }

    @Override
    public void delete(EFmFmUserMasterPO eFmFmUserMasterPO) {
        employeeDetailDAO.delete(eFmFmUserMasterPO);
    }

    @Override
    public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromClientId(String branchId) {
        return employeeDetailDAO.getAllEmployeeDetailsFromClientId(branchId);
    }

    @Override
    public boolean doesDeviceExist(String deviceId, int clientId) {
        return employeeDetailDAO.doesDeviceExist(deviceId, clientId);
    }

    @Override
    public EFmFmUserMasterPO getParticularDeviceDetails(String deviceId) {
        return employeeDetailDAO.getParticularDeviceDetails(deviceId);
    }

    @Override
    public List<EFmFmUserMasterPO> loginEmployeeDetails(String employeeId, String password) {
        return employeeDetailDAO.loginEmployeeDetails(employeeId, password);
    }

    @Override
    public List<EFmFmClientBranchPO> doesClientCodeExist(String branchCode) {
        return employeeDetailDAO.doesClientCodeExist(branchCode);
    }

    @Override
    public boolean doesEmailIdExist(String emailId, int clientId) {
        return employeeDetailDAO.doesEmailIdExist(emailId, clientId);
    }

    @Override
    public List<EFmFmUserMasterPO> getParticularEmployeeDetailsFromEmailId(String emailId) {
        return employeeDetailDAO.getParticularEmployeeDetailsFromEmailId(emailId);
    }

    @Override
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromEmployeeId(String employeeId,String branchId) {
        return employeeDetailDAO.getParticularEmpDetailsFromEmployeeId(employeeId,branchId);
    }

    @Override
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromUserId(int userId, int branchId) {
        return employeeDetailDAO.getParticularEmpDetailsFromUserId(userId, branchId);
    }

    @Override
    public void save(EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO) {
        employeeDetailDAO.save(eFmFmClientProjectDetailsPO);
    }

    @Override
    public List<EFmFmClientProjectDetailsPO> getProjectDetails(String projectId, String combinedFacility) {
        return employeeDetailDAO.getProjectDetails(projectId, combinedFacility);
    }

    @Override
    public List<EFmFmUserMasterPO> getEmpMobileNoDetails(String mobileNo,String branchId) {
        return employeeDetailDAO.getEmpMobileNoDetails(mobileNo,branchId);
    }

    @Override
    public List<EFmFmUserMasterPO> getEmpMobileNumberCheck(String mobileNumber, int branchId) {
        return employeeDetailDAO.getEmpMobileNumberCheck(mobileNumber, branchId);
    }

    @Override
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromEmployeeIdForGuest(String employeeId, int branchId) {
        return employeeDetailDAO.getParticularEmpDetailsFromEmployeeIdForGuest(employeeId, branchId);
    }

    @Override
    public List<EFmFmUserMasterPO> getParticularGuestDetailsFromEmployeeId(String employeeId, int branchId) {
        return employeeDetailDAO.getParticularGuestDetailsFromEmployeeId(employeeId, branchId);
    }

    @Override
    public List<EFmFmUserMasterPO> getEmpDetailsFromEmployeeId(String employeeId) {
        return employeeDetailDAO.getEmpDetailsFromEmployeeId(employeeId);
    }

    @Override
    public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromBranchId(String branchId,int startPgNo,int endPgNo) {
        return employeeDetailDAO.getAllEmployeeDetailsFromBranchId(branchId,startPgNo,endPgNo);
    }

    @Override
    public List<EFmFmUserMasterPO> getEmpDetailsFromEmployeeIdAndBranchId(String employeeId, String branchId) {
        return employeeDetailDAO.getEmpDetailsFromEmployeeIdAndBranchId(employeeId, branchId);
    }

    @Override
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromEmployeeIdAndbranchIdAndToken(String mobileNumber,
            String tempCode, int branchId) {
        return employeeDetailDAO.getParticularEmpDetailsFromEmployeeIdAndbranchIdAndToken(mobileNumber, tempCode,
                branchId);
    }

    @Override
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromUserIdWithOutStatus(int userId, String branchId) {
        return employeeDetailDAO.getParticularEmpDetailsFromUserIdWithOutStatus(userId, branchId);
    }

    @Override
    public List<EFmFmUserMasterPO> loginEmployeeDetailsFromMobileNumber(String mobileNumber, String password) {
        return employeeDetailDAO.loginEmployeeDetailsFromMobileNumber(mobileNumber, password);
    }

    @Override
    public List<EFmFmUserMasterPO> getEmpDetailsFromMobileNumberAndBranchId(String mobileNumber, String branchId) {
        return employeeDetailDAO.getEmpDetailsFromMobileNumberAndBranchId(mobileNumber, branchId);
    }

    @Override
    public List<EFmFmUserMasterPO> getParticularEmployeeDetailsFromDeviceIdAndMobileNumber(String deviceId,
            int branchId, String mobileNumber) {
        return employeeDetailDAO.getParticularEmployeeDetailsFromDeviceIdAndMobileNumber(deviceId, branchId,
                mobileNumber);
    }

    @Override
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromMobileNumberAndBranhId(String mobileNumber,
            int branchId) {
        return employeeDetailDAO.getParticularEmpDetailsFromMobileNumberAndBranhId(mobileNumber, branchId);
    }

    @Override
    public List<EFmFmUserMasterPO> getParticularEmpDetailsFromMobileNumber(String mobileNumber) {
        
        return employeeDetailDAO.getParticularEmpDetailsFromMobileNumber(mobileNumber);
    }

    @Override
    public void save(EFmFmUserPasswordPO eFmFmUserPasswordPO) {
        
        employeeDetailDAO.save(eFmFmUserPasswordPO);
    }

    @Override
    public void update(EFmFmUserPasswordPO eFmFmUserPasswordPO) {
        
        employeeDetailDAO.update(eFmFmUserPasswordPO);
    }

    @Override
    public List<EFmFmUserPasswordPO> getUserPasswordDetailsFromUserIdAndBranchId(int userId) {
        
        return employeeDetailDAO.getUserPasswordDetailsFromUserIdAndBranchId(userId);
    }

    @Override
    public void deleteLastPasswordForParticularEmployeCrossingDefineLimit(int passwordId) {
        
        employeeDetailDAO.deleteLastPasswordForParticularEmployeCrossingDefineLimit(passwordId);
    }

    @Override
    public List<EFmFmUserMasterPO> getEmployeeTypeDetailsByBranchId(String userType, String branchId) {
        
        return employeeDetailDAO.getEmployeeTypeDetailsByBranchId(userType, branchId);
    }

	@Override
	public List<EFmFmUserMasterPO> getParticularEmpDetailsFromUserName(String userName, int branchId) {
		return employeeDetailDAO.getParticularEmpDetailsFromUserName(userName, branchId);
	}

	@Override
	public void saveLocationMaster(EFmFmLocationMasterPO eFmFmLocationMasterPO) {
		employeeDetailDAO.saveLocationMaster(eFmFmLocationMasterPO);
		
	}

	@Override
	public void updateLocationMaster(EFmFmLocationMasterPO eFmFmLocationMasterPO) {
		employeeDetailDAO.updateLocationMaster(eFmFmLocationMasterPO);
		
	}

	@Override
	public List<EFmFmLocationMasterPO> getLocationNameExist(String locationName, String branchId) {
		return employeeDetailDAO.getLocationNameExist(locationName, branchId);
	}


	@Override
	public List<EFmFmLocationMasterPO> getMultipleLocation(String locationId, String branchId) {
		return employeeDetailDAO.getMultipleLocation(locationId, branchId);
	}
	@Override
	public List<EFmFmLocationMasterPO> getAllActiveLocation(String isActive, String branchId) {		
		return employeeDetailDAO.getAllActiveLocation(isActive, branchId);
	}

	@Override
	public List<EFmFmEmployeeProjectDetailsPO> getListOfProjectIdByUserId(int userId, String branchId) {
		return employeeDetailDAO.getListOfProjectIdByUserId(userId, branchId);
	}

	@Override
	public List<EFmFmEmployeeProjectDetailsPO> getListOfUserByreportingManager(int reportingManagerUserId, String branchId,
			int userId) {
		return employeeDetailDAO.getListOfUserByreportingManager(reportingManagerUserId, branchId, userId);
	}

	@Override
	public List<EFmFmEmployeeProjectDetailsPO> getAllProjectUserByrepManager(int reportingManagerUserId, int branchId) {
		return employeeDetailDAO.getAllProjectUserByrepManager(reportingManagerUserId, branchId);
	}

	@Override
	public List<EFmFmEmployeeProjectDetailsPO> getAllProjectUserByrepManagerWithProjectId(int reportingManagerUserId,
			String branchId, int projectId) {
		return employeeDetailDAO.getAllProjectUserByrepManagerWithProjectId(reportingManagerUserId, branchId, projectId);
	}

	@Override
	public List<EFmFmEmployeeProjectDetailsPO> getClientProjectIdByMangerAndEmployee(int reportingManagerUserId,
			String branchId, int projectId, int userId) {
		return employeeDetailDAO.getClientProjectIdByMangerAndEmployee(reportingManagerUserId, branchId, projectId, userId);
	}

	@Override
	public void addEmployeeProjectDetails(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO) {
        employeeDetailDAO.addEmployeeProjectDetails(eFmFmEmployeeProjectDetailsPO);

	}

	@Override
	public List<EFmFmClientProjectDetailsPO> getListOfProjectDetails(String activeStatus, String branchId) {
		return employeeDetailDAO.getListOfProjectDetails(activeStatus, branchId);
	}

	@Override
	public List<EFmFmClientProjectDetailsPO> getParticularProjectDetails(int projectId, String branchId) {
		return employeeDetailDAO.getParticularProjectDetails(projectId, branchId);
	}

	@Override
	public void updateEmployeeProjectDetails(EFmFmEmployeeProjectDetailsPO eFmFmEmployeeProjectDetailsPO) {
		employeeDetailDAO.updateEmployeeProjectDetails(eFmFmEmployeeProjectDetailsPO);
		
	}

	@Override
	public void updateClientProject(EFmFmClientProjectDetailsPO eFmFmClientProjectDetailsPO) {
		employeeDetailDAO.updateClientProject(eFmFmClientProjectDetailsPO);
		
	}

	@Override
	public List<EFmFmEmployeeProjectDetailsPO> getAllEmployeeByProjectId(int branchId, int projectId) {
		return employeeDetailDAO.getAllEmployeeByProjectId(branchId, projectId);
	}
		public List<EFmFmUserMasterPO> getAllEmployeeDetailsFromEmailId(String emailId, String branchId) {
		return employeeDetailDAO.getAllEmployeeDetailsFromEmailId(emailId, branchId);
	}

		@Override
		public List<EFmFmUserMasterPO> getListOfEmployeeDetailsByBranch(String branchId) {
			return employeeDetailDAO.getListOfEmployeeDetailsByBranch(branchId);
		}

		@Override
		public List<EFmFmEmployeeProjectDetailsPO> getDeligatedUserDetails(String branchId, int repMngUserId) {
			return employeeDetailDAO.getDeligatedUserDetails(branchId, repMngUserId);
		}

		@Override
		public List<EFmFmEmployeeProjectDetailsPO> getAllDeligatedUserDetails(String branchId) {
			return employeeDetailDAO.getAllDeligatedUserDetails(branchId);
		}

		@Override
		public boolean doesCommanUseNameExist(String userName) {
			return employeeDetailDAO.doesCommanUseNameExist(userName);
		}

		@Override
		public boolean doesCommanEmployeeIdExist(String employeeId) {
			return employeeDetailDAO.doesCommanEmployeeIdExist(employeeId);
		}

		@Override
		public boolean doesCommanMobileNumberExist(String mobileNumber) {
			return employeeDetailDAO.doesCommanMobileNumberExist(mobileNumber);
		}

		@Override
		public boolean doesCommanEmailIdExist(String emailId) {
			return employeeDetailDAO.doesCommanEmailIdExist(emailId);
		}
			@Override
		public List<EFmFmEmployeeProjectDetailsPO> getDeligatedUserDetailsByReportingManager(int branchId,
				int delegatedBy, int repManagerId, int projectId) {
			return employeeDetailDAO.getDeligatedUserDetailsByReportingManager(branchId, delegatedBy, repManagerId, projectId);
		}
			@Override
		public List<EFmFmEmployeeProjectDetailsPO> getParticularProjectAllocation(int delegatedId) {
			return employeeDetailDAO.getParticularProjectAllocation(delegatedId);
		}

		@Override
		public List<EFmFmLocationMasterPO> getAllActiveAndPendingLocation(int branchId) {
			return employeeDetailDAO.getAllActiveAndPendingLocation(branchId);
		}

		@Override
		public List<EFmFmUserMasterPO> getAllEmployeeDetailsByPagination(String branchId, int startPgNo, int endPgNo) {
			return employeeDetailDAO.getAllEmployeeDetailsByPagination(branchId, startPgNo, endPgNo);
		}

		@Override
		public List<EFmFmUserMasterPO> getParticularEmployeeDetailsFromUserName(String userName) {
			return employeeDetailDAO.getParticularEmployeeDetailsFromUserName(userName);
		}
}
