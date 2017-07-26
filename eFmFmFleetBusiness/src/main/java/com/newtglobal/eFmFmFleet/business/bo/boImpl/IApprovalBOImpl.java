package com.newtglobal.eFmFmFleet.business.bo.boImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtglobal.eFmFmFleet.business.bo.IApprovalBO;
import com.newtglobal.eFmFmFleet.business.dao.IApprovalDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorMasterPO;

@Service("IApprovalBO")
public class IApprovalBOImpl implements IApprovalBO {

    @Autowired
    IApprovalDAO iApprovalDAO;

    public void setIUserMasterDAO(IApprovalDAO iApprovalDAO) {
        this.iApprovalDAO = iApprovalDAO;
    }

    @Override
    public void save(EFmFmDriverMasterPO eFmFmDriverMasterPO) {
        iApprovalDAO.save(eFmFmDriverMasterPO);
    }

    @Override
    public void update(EFmFmDriverMasterPO eFmFmDriverMasterPO) {
        iApprovalDAO.update(eFmFmDriverMasterPO);
    }

    @Override
    public void delete(EFmFmDriverMasterPO eFmFmDriverMasterPO) {
        iApprovalDAO.delete(eFmFmDriverMasterPO);
    }

    @Override
    public List<EFmFmDriverMasterPO> getAllUnapprovedDrivers(String clientId) {
        return iApprovalDAO.getAllUnapprovedDrivers(clientId);
    }

    @Override
    public List<EFmFmDriverMasterPO> getAllApprovedDrivers(int clientId) {
        return iApprovalDAO.getAllApprovedDrivers(clientId);
    }

    @Override
    public List<EFmFmDriverMasterPO> getAllInActiveDrivers(String clientId) {
        return iApprovalDAO.getAllInActiveDrivers(clientId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getAllUnapprovedVehicles(String clientId) {
        return iApprovalDAO.getAllUnapprovedVehicles(clientId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getAllApprovedVehicles(String clientId) {
        return iApprovalDAO.getAllApprovedVehicles(clientId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getAllInActiveVehicles(String clientId) {
        return iApprovalDAO.getAllInActiveVehicles(clientId);
    }

    @Override
    public List<EFmFmVendorMasterPO> getAllUnapprovedVendors(String clientId) {
        return iApprovalDAO.getAllUnapprovedVendors(clientId);
    }

    @Override
    public List<EFmFmVendorMasterPO> getAllApprovedVendors(String clientId) {
        return iApprovalDAO.getAllApprovedVendors(clientId);
    }

    @Override
    public List<EFmFmVendorMasterPO> getAllInActiveVendors(String clientId) {
        return iApprovalDAO.getAllInActiveVendors(clientId);
    }

    @Override
    public EFmFmDriverMasterPO getParticularDriverDetail(int driverId) {
        return iApprovalDAO.getParticularDriverDetail(driverId);
    }

    @Override
    public void deleteParticularDriver(int driverId) {
        iApprovalDAO.deleteParticularDriver(driverId);
    }

    @Override
    public void deleteParticularVehicle(int vehicleId) {
        iApprovalDAO.deleteParticularVehicle(vehicleId);
    }

    @Override
    public void deleteParticularVendor(int vendorId) {
        iApprovalDAO.deleteParticularVendor(vendorId);
    }

    @Override
    public void saveRouteTripDetails(EFmFmEmployeeTripDetailPO eFmFmEmployeeTripDetailPO) {
        iApprovalDAO.saveRouteTripDetails(eFmFmEmployeeTripDetailPO);
    }

    @Override
    public List<EFmFmDriverMasterPO> getParticularDriverDeviceDetails(String mobileNo, String clientId) {
        return iApprovalDAO.getParticularDriverDeviceDetails(mobileNo, clientId);
    }

    @Override
    public List<EFmFmDriverMasterPO> getParticularDriverDetailsFromDeviceToken(String deviceId, int clientId) {
        return iApprovalDAO.getParticularDriverDetailsFromDeviceToken(deviceId, clientId);
    }

    @Override
    public List<EFmFmDriverMasterPO> getParticularDriverDetailFromDeriverId(int driverId) {
        return iApprovalDAO.getParticularDriverDetailFromDeriverId(driverId);
    }
  
    @Override
	public List<EFmFmDriverDocsPO> getAlluploadFileDetails(int driverId) {		
		return iApprovalDAO.getAlluploadFileDetails(driverId);
	}

	@Override
	public void addUploadDetails(EFmFmDriverDocsPO eFmFmDriverDocsPO) {
		iApprovalDAO.addUploadDetails(eFmFmDriverDocsPO);
		
	}

	@Override
	public List<EFmFmEscortDocsPO> getAllEscortuploadFileDetails(int escortId) {
		return iApprovalDAO.getAllEscortuploadFileDetails(escortId);
	}

	@Override
	public void addUploadDetails(EFmFmEscortDocsPO eFmFmEscortDocsPO) {
		iApprovalDAO.addUploadDetails(eFmFmEscortDocsPO);		
	}

	@Override
	public List<EFmFmDriverMasterPO> getAllApprovedDriversWithOutDummy(String branchId) {
		return iApprovalDAO.getAllApprovedDriversWithOutDummy(branchId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getAllApprovedVehiclesWithOutDummy(String branchId) {
		return iApprovalDAO.getAllApprovedVehiclesWithOutDummy(branchId);
	}

}
