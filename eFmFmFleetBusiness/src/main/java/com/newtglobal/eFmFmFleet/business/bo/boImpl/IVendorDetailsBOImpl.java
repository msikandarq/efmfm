package com.newtglobal.eFmFmFleet.business.bo.boImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtglobal.eFmFmFleet.business.bo.IVendorDetailsBO;
import com.newtglobal.eFmFmFleet.business.dao.IVendorDetailsDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverFeedbackPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;

@Service("IVendorDetailsBO")
public class IVendorDetailsBOImpl implements IVendorDetailsBO {

    @Autowired
    private IVendorDetailsDAO iVendorDetailsDAO;

    public void setiVendorDetailsDAO(IVendorDetailsDAO iVendorDetailsDAO) {
        this.iVendorDetailsDAO = iVendorDetailsDAO;
    }

    @Override
    public void save(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
        iVendorDetailsDAO.save(eFmFmVendorMasterPO);

    }

    @Override
    public void update(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
        iVendorDetailsDAO.update(eFmFmVendorMasterPO);

    }

    @Override
    public void delete(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
        iVendorDetailsDAO.delete(eFmFmVendorMasterPO);
    }

    @Override
    public List<EFmFmVendorMasterPO> getAllVendorName(String vendorName, String clientId) {
        return iVendorDetailsDAO.getAllVendorName(vendorName, clientId);
    }

    @Override
    public EFmFmVendorMasterPO getParticularVendorDetail(int vendorId) {
        return iVendorDetailsDAO.getParticularVendorDetail(vendorId);
    }

    @Override
    public List<EFmFmVendorMasterPO> getAllVendorsDetails(EFmFmVendorMasterPO eFmFmVendorMasterPO) {
        return iVendorDetailsDAO.getAllVendorsDetails(eFmFmVendorMasterPO);
    }

    @Override
    public void save(EFmFmDriverFeedbackPO driverFeedbackPO) {
        iVendorDetailsDAO.save(driverFeedbackPO);
    }

    @Override
    public boolean doesDriverDeviceExist(String deviceId, int clientId) {
        return iVendorDetailsDAO.doesDriverDeviceExist(deviceId, clientId);
    }

    @Override
    public List<EFmFmDeviceMasterPO> getAllDeviceDetails(int clientId, Date todayDate) {
        return iVendorDetailsDAO.getAllDeviceDetails(clientId, todayDate);
    }

    @Override
    public List<EFmFmVendorMasterPO> getVendorTinNumber(String tinNumber, String branchId) {
        return iVendorDetailsDAO.getVendorTinNumber(tinNumber, branchId);
    }

    @Override
    public List<EFmFmVendorMasterPO> getVendorEmailId(String emailID, String branchId) {
        return iVendorDetailsDAO.getVendorEmailId(emailID, branchId);
    }

    @Override
    public List<EFmFmVendorMasterPO> getVendorMobileNo(String vendorMobileNo, String branchId) {
        return iVendorDetailsDAO.getVendorMobileNo(vendorMobileNo, branchId);
    }

    @Override
    public List<EFmFmDeviceMasterPO> getAllActiveDeviceDetails(int branchId) {
        return iVendorDetailsDAO.getAllActiveDeviceDetails(branchId);
    }

    @Override
    public List<EFmFmVendorMasterPO> getAllApprovedVendorsDetails(String branchId) {
        return iVendorDetailsDAO.getAllApprovedVendorsDetails(branchId);
    }

    @Override
    public List<EFmFmDeviceMasterPO> getAllDeviceDetailsFromBranchId(String branchId) {
        return iVendorDetailsDAO.getAllDeviceDetailsFromBranchId(branchId);
    }

	@Override
	public List<EFmFmVendorMasterPO> getAllEnableVendorsDetails(String branchId) {
		return iVendorDetailsDAO.getAllEnableVendorsDetails(branchId);
	}

	@Override
	public List<EFmFmVendorMasterPO> getAllVendorDetailsByVendorId(String branchId, int vendorId) {
		return iVendorDetailsDAO.getAllVendorDetailsByVendorId(branchId, vendorId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getAllActiveVehicleDetails(int vendorId) {
		return iVendorDetailsDAO.getAllActiveVehicleDetails(vendorId);
	}

	@Override
	public List<EFmFmDriverMasterPO> getAllActiveDriverDetails(int vendorId) {
		return iVendorDetailsDAO.getAllActiveDriverDetails(vendorId);
	}

}
