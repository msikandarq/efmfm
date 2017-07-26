package com.newtglobal.eFmFmFleet.business.bo.boImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newtglobal.eFmFmFleet.business.bo.IFieldAppDetailsBO;
import com.newtglobal.eFmFmFleet.business.dao.IFieldAppDetailsDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFieldAppConfigMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmSupervisorMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmUserPasswordPO;

@Service("IFieldAppDetailsBO")
public class IFieldAppDetailsBOImpl implements IFieldAppDetailsBO {

    @Autowired
    private IFieldAppDetailsDAO iFieldAppDetailsDAO;

    public void setiVendorDetailsDAO(IFieldAppDetailsDAO iFieldAppDetailsDAO) {
        this.iFieldAppDetailsDAO = iFieldAppDetailsDAO;
    }

    @Override
    public void save(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) {
    	iFieldAppDetailsDAO.save(eFmFmSupervisorMasterPO);

    }

    @Override
    public void update(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) {
    	iFieldAppDetailsDAO.update(eFmFmSupervisorMasterPO);

    }

    @Override
    public void delete(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) {
    	iFieldAppDetailsDAO.delete(eFmFmSupervisorMasterPO);
    }

	@Override
	public List<EFmFmSupervisorMasterPO> getSupervisorDetails(int supervisorId, String branchId) {		
		return iFieldAppDetailsDAO.getSupervisorDetails(supervisorId, branchId);
	}

	@Override
	public List<EFmFmSupervisorMasterPO> getAllSupervisorByVendorDetail(int vendorId, String branchId, String isActive) {
		return iFieldAppDetailsDAO.getAllSupervisorByVendorDetail(vendorId, branchId,isActive);
	}

	@Override
	public List<EFmFmFieldAppConfigMasterPO> getAllValuesUsedByConfigType(String branchId,String configType) {
		return iFieldAppDetailsDAO.getAllValuesUsedByConfigType(branchId, configType);
	}

	@Override
	public List<EFmFmSupervisorMasterPO> getSupervisorMobileNumberDetails(String mobileNumber, String branchId) {
		return iFieldAppDetailsDAO.getSupervisorMobileNumberDetails(mobileNumber, branchId);
	}

	@Override
	public List<EFmFmSupervisorMasterPO> getAllSupervisorDetails(String branchId, String isActive) {
		// TODO Auto-generated method stub
		return iFieldAppDetailsDAO.getAllSupervisorDetails(branchId, isActive);
	}

	@Override
	public List<EFmFmSupervisorMasterPO> getSupervisorMobileNumberStatus(String mobileNumber, String branchId) {
		// TODO Auto-generated method stub
		return iFieldAppDetailsDAO.getSupervisorMobileNumberStatus(mobileNumber, branchId);
	}

	@Override
	public List<EFmFmSupervisorMasterPO> getSupervisorMobileNumberWithTokenDetails(String mobileNumber, String tempCode,
			String branchId) {		
		return iFieldAppDetailsDAO.getSupervisorMobileNumberWithTokenDetails(mobileNumber, tempCode, branchId);
	}

	@Override
	public List<EFmFmSupervisorMasterPO> getSupervisorPasswordDetailsFromSupervisorAndBranchId(int supervisorId,
			String branchId) {
		return iFieldAppDetailsDAO.getSupervisorPasswordDetailsFromSupervisorAndBranchId(supervisorId, branchId);
	}

	@Override
	public List<EFmFmEscortDocsPO> getEscortuploadFileDetails(int escortId, String docType) {
		// TODO Auto-generated method stub
		return iFieldAppDetailsDAO.getEscortuploadFileDetails(escortId, docType);
	}

	@Override
	public int getSupervisorId(EFmFmSupervisorMasterPO eFmFmSupervisorMasterPO) {
		// TODO Auto-generated method stub
		return iFieldAppDetailsDAO.getSupervisorId(eFmFmSupervisorMasterPO);
	}

	

   
}
