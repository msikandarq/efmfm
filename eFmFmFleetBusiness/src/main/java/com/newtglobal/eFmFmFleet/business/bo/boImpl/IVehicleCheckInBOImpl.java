package com.newtglobal.eFmFmFleet.business.bo.boImpl;

import java.util.Date;
import java.util.Date;
import java.util.List;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.business.bo.IVehicleCheckInBO;
import com.newtglobal.eFmFmFleet.business.dao.IVehicleCheckInDAO;
import com.newtglobal.eFmFmFleet.business.dao.IVehicleCheckInDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDynamicVehicleCheckListPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDynamicVehicleCheckListPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFixedDistanceContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFixedDistanceContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFuelChargesPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFuelChargesPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripAlertsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripBasedContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripBasedContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCapacityMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleDocsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleInspectionPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleInspectionPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractInvoicePO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractInvoicePO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorContractTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorFuelContractTypeMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVendorFuelContractTypeMasterPO;

@Service("IVehicleCheckInBO")
public class IVehicleCheckInBOImpl implements IVehicleCheckInBO {

    @Autowired
    private IVehicleCheckInDAO iVehicleCheckInDAO;

    public void setiVehicleCheckInDAO(IVehicleCheckInDAO iVehicleCheckInDAO) {
        this.iVehicleCheckInDAO = iVehicleCheckInDAO;
    }

    @Override
    public void save(EFmFmVehicleInspectionPO eFmFmVehicleInspectionPO) {
        iVehicleCheckInDAO.save(eFmFmVehicleInspectionPO);
    }

    @Override
    public void save(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) {
        iVehicleCheckInDAO.save(eFmFmDeviceMasterPO);
    }

    @Override
    public void update(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) {
        iVehicleCheckInDAO.update(eFmFmDeviceMasterPO);
    }

    @Override
    public void update(EFmFmVehicleInspectionPO eFmFmVehicleInspectionPO) {
        iVehicleCheckInDAO.update(eFmFmVehicleInspectionPO);
    }

    @Override
    public void delete(EFmFmDeviceMasterPO eFmFmDeviceMasterPO) {
        iVehicleCheckInDAO.delete(eFmFmDeviceMasterPO);
    }

    @Override
    public void save(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
        iVehicleCheckInDAO.save(eFmFmVehicleMasterPO);

    }

    @Override
    public void update(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
        iVehicleCheckInDAO.update(eFmFmVehicleMasterPO);

    }

    @Override
    public void delete(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
        iVehicleCheckInDAO.delete(eFmFmVehicleMasterPO);

    }

    @Override
    public void save(EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO) {
        iVehicleCheckInDAO.save(eFmFmFixedDistanceContractDetailPO);

    }

    @Override
    public void update(EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO) {
        iVehicleCheckInDAO.update(eFmFmFixedDistanceContractDetailPO);

    }
    
    
    @Override
	public void save(EFmFmVehicleCapacityMasterPO eFmFmVehicleCapacityMasterPO) {
        iVehicleCheckInDAO.save(eFmFmVehicleCapacityMasterPO);

	}

	@Override
	public void update(EFmFmVehicleCapacityMasterPO eFmFmVehicleCapacityMasterPO) {
        iVehicleCheckInDAO.update(eFmFmVehicleCapacityMasterPO);

	}

	@Override
	public void delete(EFmFmVehicleCapacityMasterPO eFmFmVehicleCapacityMasterPO) {
        iVehicleCheckInDAO.delete(eFmFmVehicleCapacityMasterPO);		
	}
   

    @Override
    public EFmFmVehicleMasterPO getParticularVehicleDetails(String vehicleNumber, int branchId) {
        return iVehicleCheckInDAO.getParticularVehicleDetails(vehicleNumber, branchId);
    }

    @Override
    public EFmFmVehicleMasterPO getParticularVehicleDetail(int vehicleId) {
        return iVehicleCheckInDAO.getParticularVehicleDetail(vehicleId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getAllVehicleDetails(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {
        return iVehicleCheckInDAO.getAllVehicleDetails(eFmFmVehicleMasterPO);
    }

    @Override
    public List<EFmFmDriverMasterPO> getAllDriverDetails(EFmFmDriverMasterPO eFmFmDriverMasterPO) {
        return iVehicleCheckInDAO.getAllDriverDetails(eFmFmDriverMasterPO);
    }

    @Override
    public List<EFmFmDriverMasterPO> randomDriverDetails(int vendorId, Date todayDate) {
        return iVehicleCheckInDAO.randomDriverDetails(vendorId, todayDate);
    }

    @Override
    public void vehicleDriverCheckIn(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
        iVehicleCheckInDAO.vehicleDriverCheckIn(eFmFmVehicleCheckInPO);

    }

    @Override
    public List<EFmFmVehicleMasterPO> getAvailableVehicleDetails(int vendorId, Date todayDate) {
        return iVehicleCheckInDAO.getAvailableVehicleDetails(vendorId, todayDate);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetails(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
        return iVehicleCheckInDAO.getCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
    }

   

    @Override
    public void saveEscortDetails(EFmFmEscortMasterPO eFmFmEscortMasterPO) {
        iVehicleCheckInDAO.saveEscortDetails(eFmFmEscortMasterPO);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetails(String branchId, Date date) {
        return iVehicleCheckInDAO.getCheckedInVehicleDetails(branchId, date);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getParticulaCheckedInVehicleDetails(
            EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
        return iVehicleCheckInDAO.getParticulaCheckedInVehicleDetails(eFmFmVehicleCheckInPO);
    }

    @Override
    public int getNumberOfVehiclesFromClientId(int branchId) {
        return iVehicleCheckInDAO.getNumberOfVehiclesFromClientId(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getAssignedVehicleDetails(int branchId, Date todayDate) {
        return iVehicleCheckInDAO.getAssignedVehicleDetails(branchId, todayDate);
    }

    @Override
    public void update(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
        iVehicleCheckInDAO.update(eFmFmVehicleCheckInPO);
    }

   
    @Override
    public void saveEscortCheckIn(EFmFmEscortCheckInPO eFmFmEscortCheckInPO) {
        iVehicleCheckInDAO.saveEscortCheckIn(eFmFmEscortCheckInPO);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> checkedOutParticularDriver(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
        return iVehicleCheckInDAO.checkedOutParticularDriver(eFmFmVehicleCheckInPO);
    }

    @Override
    public void updateEscortDetails(EFmFmEscortMasterPO eFmFmEscortMasterPO) {
        iVehicleCheckInDAO.updateEscortDetails(eFmFmEscortMasterPO);
    }

    @Override
    public EFmFmVehicleCheckInPO getCheckInDriverDetails(int driverId) {
        return iVehicleCheckInDAO.getCheckInDriverDetails(driverId);
    }

    @Override
    public List<EFmFmDeviceMasterPO> deviceNumberExistsCheck(String mobileNumber,String branchId) {
        return iVehicleCheckInDAO.deviceNumberExistsCheck(mobileNumber,branchId);
    }

    @Override
    public List<EFmFmDeviceMasterPO> deviceImeiNumberExistsCheck(String branchId,String imeiNumber) {
        return iVehicleCheckInDAO.deviceImeiNumberExistsCheck(branchId,imeiNumber);
    }

    @Override
    public List<EFmFmDriverMasterPO> getParticularDriverDetailsFromMobileNum(String mobileNumber, int branchId) {
        return iVehicleCheckInDAO.getParticularDriverDetailsFromMobileNum(mobileNumber, branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getParticularCheckedInDriver(EFmFmVehicleCheckInPO eFmFmVehicleCheckInPO) {
        return iVehicleCheckInDAO.getParticularCheckedInDriver(eFmFmVehicleCheckInPO);
    }

    @Override
    public List<EFmFmTripAlertsPO> getGroupByDriver(int branchId, Date fromDate, Date toDate) {
        return iVehicleCheckInDAO.getGroupByDriver(branchId, fromDate, toDate);
    }

    @Override
    public List<EFmFmTripAlertsPO> getScoreCardDriver(int branchId, int driverId, Date fromDate, Date toDate) {
        return iVehicleCheckInDAO.getScoreCardDriver(branchId, driverId, fromDate, toDate);
    }

    @Override
    public List<EFmFmAssignRoutePO> getDriverAssignedTrip(int branchId, int driverId, Date fromDate, Date toDate) {
        return iVehicleCheckInDAO.getDriverAssignedTrip(branchId, driverId, fromDate, toDate);
    }

    @Override
    public List<EFmFmTripAlertsPO> getGroupByVehicle(int branchId, Date fromDate, Date toDate) {
        return iVehicleCheckInDAO.getGroupByVehicle(branchId, fromDate, toDate);
    }

    @Override
    public List<EFmFmTripAlertsPO> getScoreCardVehicle(int branchId, int driverId, Date fromDate, Date toDate) {
        return iVehicleCheckInDAO.getScoreCardVehicle(branchId, driverId, fromDate, toDate);
    }

    @Override
    public List<EFmFmAssignRoutePO> getVehicleAssignedVehicleTrip(int branchId, int driverId, Date fromDate,
            Date toDate) {
        return iVehicleCheckInDAO.getVehicleAssignedVehicleTrip(branchId, driverId, fromDate, toDate);
    }

  

    @Override
    public List<EFmFmEscortMasterPO> getMobileNoDetails(EFmFmEscortMasterPO eFmFmEscortMasterPO) {
        return iVehicleCheckInDAO.getMobileNoDetails(eFmFmEscortMasterPO);
    }

    @Override
    public List<EFmFmDeviceMasterPO> getListOfAllActiveDevices(String branchId) {
        return iVehicleCheckInDAO.getListOfAllActiveDevices(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getCheckInDetails(int branchId) {
        return iVehicleCheckInDAO.getCheckInDetails(branchId);
    }

    @Override
    public List<EFmFmDriverMasterPO> getAllActiveDriverDetails(int vendorId) {
        return iVehicleCheckInDAO.getAllActiveDriverDetails(vendorId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getAllActiveVehicleDetails(int vendorId) {
        return iVehicleCheckInDAO.getAllActiveVehicleDetails(vendorId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> alreadyCheckInExist(int vehicleId) {
        return iVehicleCheckInDAO.alreadyCheckInExist(vehicleId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getVendorBasedTripSheet(Date fromDate, Date toDate, int branchId, int vendorId) {
        return iVehicleCheckInDAO.getVendorBasedTripSheet(fromDate, toDate, branchId, vendorId);
    }

    @Override
    public List<EFmFmTripBasedContractDetailPO> getTripDistanceDetails(
            EFmFmTripBasedContractDetailPO eFmFmTripBasedContractDetailPO) {
        return iVehicleCheckInDAO.getTripDistanceDetails(eFmFmTripBasedContractDetailPO);
    }

   /* @Override
    public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceDetails(
            EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO) {
        return iVehicleCheckInDAO.getFixedDistanceDetails(eFmFmFixedDistanceContractDetailPO);
    }*/

    @Override
    public List<EFmFmVehicleMasterPO> getNoOfWorkingDays(Date fromDate, Date toDate, int branchId, int vehicleId,
            String contractType, int contractDetailId) {
        return iVehicleCheckInDAO.getNoOfWorkingDays(fromDate, toDate, branchId, vehicleId, contractType,
                contractDetailId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getVendorBasedVehicleDetails(Date fromDate, Date toDate, int branchId, int vendorId,
            String contractType, int contractDetailsId) {
        return iVehicleCheckInDAO.getVendorBasedVehicleDetails(fromDate, toDate, branchId, vendorId, contractType,
                contractDetailsId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getVehicleBasedTripSheet(Date fromDate, Date toDate, int branchId, int vehicleId) {
        return iVehicleCheckInDAO.getVehicleBasedTripSheet(fromDate, toDate, branchId, vehicleId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getTripBasedNoOfWorkingDays(Date fromDate, Date toDate, int branchId,
            int vehicleId, String contractType, int contractDetailId) {
        return iVehicleCheckInDAO.getTripBasedNoOfWorkingDays(fromDate, toDate, branchId, vehicleId, contractType,
                contractDetailId);
    }

    @Override
    public void save(EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO) {
        iVehicleCheckInDAO.save(eFmFmVendorContractInvoicePO);

    }

    @Override
    public void update(EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO) {
        iVehicleCheckInDAO.update(eFmFmVendorContractInvoicePO);

    }

    @Override
    public void delete(EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO) {
        iVehicleCheckInDAO.delete(eFmFmVendorContractInvoicePO);

    }

    @Override
    public List<EFmFmVendorContractInvoicePO> getInvoiceforVendor(Date fromDate, Date toDate, int branchId,
            int vendorId, String invoiceType) {
        return iVehicleCheckInDAO.getInvoiceforVendor(fromDate, toDate, branchId, vendorId, invoiceType);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getAllVehicleDetails(int branchId) {
        return iVehicleCheckInDAO.getAllVehicleDetails(branchId);
    }

    @Override
    public List<EFmFmDriverMasterPO> getAllDriverDetails(int branchId) {
        return iVehicleCheckInDAO.getAllDriverDetails(branchId);
    }

   

    @Override
    public void update(EFmFmEscortCheckInPO eFmFmEscortCheckInPO) {
        iVehicleCheckInDAO.update(eFmFmEscortCheckInPO);
    }

    @Override
    public List<EFmFmVendorContractInvoicePO> getListOfInvoiceNumbers(int branchId) {
        return iVehicleCheckInDAO.getListOfInvoiceNumbers(branchId);
    }

    @Override
    public List<EFmFmVendorContractInvoicePO> getInvoiceDetails(int branchId, int InvoiceNumber) {
        return iVehicleCheckInDAO.getInvoiceDetails(branchId, InvoiceNumber);
    }

  

    @Override
    public List<EFmFmVehicleMasterPO> getSumOfTotalKmByVehicle(Date fromDate, Date toDate, int branchId, int vehicleId,
            String contractType, int contractDetailId) {
        return iVehicleCheckInDAO.getSumOfTotalKmByVehicle(fromDate, toDate, branchId, vehicleId, contractType,
                contractDetailId);
    }

    @Override
    public List<EFmFmVendorContractInvoicePO> getInvoiceTripBasedVehicle(Date fromDate, Date toDate, int branchId,
            int vehicleId) {
        return iVehicleCheckInDAO.getInvoiceTripBasedVehicle(fromDate, toDate, branchId, vehicleId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getTripBasedVehicleDetails(Date fromDate, Date toDate, int branchId, int vendorId,
            String contractType, int contractDetailsId) {
        return iVehicleCheckInDAO.getTripBasedVehicleDetails(fromDate, toDate, branchId, vendorId, contractType,
                contractDetailsId);
    }

    @Override
    public List<EFmFmVendorContractInvoicePO> getInvoiceByVehicleFixedDistance(Date fromDate, Date toDate, int branchId,
            int vendorId, int vehicleId,String disatanceFlg) {
        return iVehicleCheckInDAO.getInvoiceByVehicleFixedDistance(fromDate, toDate, branchId, vendorId, vehicleId,disatanceFlg);
    }

    @Override
    public List<EFmFmDeviceMasterPO> getDeviceDetailsFromDeviceId(int deviceId, String branchId) {
        return iVehicleCheckInDAO.getDeviceDetailsFromDeviceId(deviceId, branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehiclesByVendor(int branchId, int vendorId) {
        return iVehicleCheckInDAO.getAllCheckedInVehiclesByVendor(branchId, vendorId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getAllCheckedInDriversByVendor(int branchId, int vendorId) {
        return iVehicleCheckInDAO.getAllCheckedInDriversByVendor(branchId, vendorId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getParticularCheckedInDriverDetails(int branchId, int driverId) {
        return iVehicleCheckInDAO.getParticularCheckedInDriverDetails(branchId, driverId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getParticularCheckedInDeviceDetails(int branchId, int deviceId) {
        return iVehicleCheckInDAO.getParticularCheckedInDeviceDetails(branchId, deviceId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getParticularCheckedInVehicles(String branchId, int vehicleId) {
        return iVehicleCheckInDAO.getParticularCheckedInVehicles(branchId, vehicleId);
    }

    @Override
    public List<EFmFmVendorContractTypeMasterPO> getContractTypeDetails(String contractType, int branchId) {
        return iVehicleCheckInDAO.getContractTypeDetails(contractType, branchId);
    }

    @Override
    public List<EFmFmVendorContractTypeMasterPO> getAllContractType(int branchId) {
        return iVehicleCheckInDAO.getAllContractType(branchId);
    }

    @Override
    public void deleteParticularCheckInEntryFromDeviceVehicleDriver(int checkInId) {
        iVehicleCheckInDAO.deleteParticularCheckInEntryFromDeviceVehicleDriver(checkInId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> alreadyCheckInDriverExistence(int driverId) {
        return iVehicleCheckInDAO.alreadyCheckInDriverExistence(driverId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getDriverCheckInDetails(int branchId) {
        return iVehicleCheckInDAO.getDriverCheckInDetails(branchId);
    }

    @Override
    public List<EFmFmDriverMasterPO> getAllApprovedDriversByVendorId(int vendorId, int branchId) {
        return iVehicleCheckInDAO.getAllApprovedDriversByVendorId(vendorId, branchId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getAllApprovedVehiclesByVendorId(int vendorId, int branchId) {
        return iVehicleCheckInDAO.getAllApprovedVehiclesByVendorId(vendorId, branchId);
    }

    @Override
    public List<EFmFmDeviceMasterPO> getAllApprovedDevices(String branchId) {
        return iVehicleCheckInDAO.getAllApprovedDevices(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> alreadyCheckInDeviceExistence(int deviceId, int branchId) {
        return iVehicleCheckInDAO.alreadyCheckInDeviceExistence(deviceId, branchId);
    }

    @Override
    public List<EFmFmDeviceMasterPO> getAllActiveDeviceDetails(int branchId) {
        return iVehicleCheckInDAO.getAllActiveDeviceDetails(branchId);
    }

    @Override
    public void deleteExtraCheckInEntry(int checkInId) {
        iVehicleCheckInDAO.deleteExtraCheckInEntry(checkInId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsForEditBucket(int branchId) {
        return iVehicleCheckInDAO.getCheckedInVehicleDetailsForEditBucket(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getLastCheckInEntitiesDetails(int branchId) {
        return iVehicleCheckInDAO.getLastCheckInEntitiesDetails(branchId);
    }

    @Override
    public long getAllCheckedInVehicleCount(int branchId) {
        return iVehicleCheckInDAO.getAllCheckedInVehicleCount(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getLastCheckedOutInDeviceDetails(int branchId, int deviceId) {
        return iVehicleCheckInDAO.getLastCheckedOutInDeviceDetails(branchId, deviceId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getCheckedInVehiclesOnRoad(int branchId) {
        return iVehicleCheckInDAO.getCheckedInVehiclesOnRoad(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsFromChecInId(int checkInId) {
        return iVehicleCheckInDAO.getCheckedInVehicleDetailsFromChecInId(checkInId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getVehicleDetailsFromVehicleNumber(String vehicleNumber, String branchId) {
        return iVehicleCheckInDAO.getVehicleDetailsFromVehicleNumber(vehicleNumber, branchId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getAllActualVehicleDetailsFromBranchId(int branchId) {
        return iVehicleCheckInDAO.getAllActualVehicleDetailsFromBranchId(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleForCreatingNewBucket(int branchId) {
        return iVehicleCheckInDAO.getAllCheckedInVehicleForCreatingNewBucket(branchId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getCheckInVehicle(int branchId, Date requestedDate) {
        return iVehicleCheckInDAO.getCheckInVehicle(branchId, requestedDate);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getAllVehicleModel(int branchId) {
        return iVehicleCheckInDAO.getAllVehicleModel(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsFromChecInAndBranchId(int checkInId, int branchId) {
        return iVehicleCheckInDAO.getCheckedInVehicleDetailsFromChecInAndBranchId(checkInId, branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getVehicleAndDriverAttendence(Date fromDate, Date toDate, String branchId) {
        return iVehicleCheckInDAO.getVehicleAndDriverAttendence(fromDate, toDate, branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getVehicleAndDriverAttendenceByVehicleId(Date fromDate, Date toDate,
            int branchId, int vehicleId) {
        return iVehicleCheckInDAO.getVehicleAndDriverAttendenceByVehicleId(fromDate, toDate, branchId, vehicleId);
    }

    @Override
    public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceActiveContractDetails(int branchId) {
        return iVehicleCheckInDAO.getFixedDistanceActiveContractDetails(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleDetailsForEditBucket(int branchId) {
        return iVehicleCheckInDAO.getAllCheckedInVehicleDetailsForEditBucket(branchId);
    }

    @Override
    public List<EFmFmDriverMasterPO> getAllDriverDetailsByBranchIdAndVendorId(int branchId, int vendorId) {
        return iVehicleCheckInDAO.getAllDriverDetailsByBranchIdAndVendorId(branchId, vendorId);
    }

    @Override
    public List<EFmFmVehicleInspectionPO> getAllVehicleInspectionsByBranchIdVehicleIdAndDate(Date fromDate, Date toDate,
            int branchId, int vehicleId) {
        return iVehicleCheckInDAO.getAllVehicleInspectionsByBranchIdVehicleIdAndDate(fromDate, toDate, branchId,
                vehicleId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getVehiclesDistinctCapacityASC(int branchId) {
        return iVehicleCheckInDAO.getVehiclesDistinctCapacityASC(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getUnreadCheckedInDrivers(int branchId) {
        return iVehicleCheckInDAO.getUnreadCheckedInDrivers(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getAllCheckedInDriversByVendorIdAndVehicleNum(int branchId, int vendorId,
            int vehicleId) {
        return iVehicleCheckInDAO.getAllCheckedInDriversByVendorIdAndVehicleNum(branchId, vendorId, vehicleId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getAllPresentDriverAndVehicles(String branchId) {
        return iVehicleCheckInDAO.getAllPresentDriverAndVehicles(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsWithOutDummyVehicles(String branchId) {
        return iVehicleCheckInDAO.getCheckedInVehicleDetailsWithOutDummyVehicles(branchId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getParticularDriverCheckedInTableEntry(String branchId, int driverId) {
        return iVehicleCheckInDAO.getParticularDriverCheckedInTableEntry(branchId, driverId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getParticularVehicleCheckedInTableEntry(String branchId, int vehicleId) {
        return iVehicleCheckInDAO.getParticularVehicleCheckedInTableEntry(branchId, vehicleId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getParticularVendorCheckedInTableEntry(String branchId, int vendorId) {
        return iVehicleCheckInDAO.getParticularVendorCheckedInTableEntry(branchId, vendorId);
    }

    @Override
    public void invoiceUpdate(EFmFmVendorContractInvoicePO eFmFmVendorContractInvoicePO) {
        iVehicleCheckInDAO.invoiceUpdate(eFmFmVendorContractInvoicePO);
    }

    @Override
    public List<EFmFmVendorContractInvoicePO> getInvoiceByContractInvoiceId(int contractInvoiceId, int branchId) {
        return iVehicleCheckInDAO.getInvoiceByContractInvoiceId(contractInvoiceId, branchId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getSumOfTravelledKm(Date fromDate, Date toDate, int branchId, int vehicleId,
            String contractType, int contractDetailId) {
        return iVehicleCheckInDAO.getSumOfTravelledKm(fromDate, toDate, branchId, vehicleId, contractType,
                contractDetailId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getVehicleWorkingDays(Date fromDate, Date toDate, int branchId, int vehicleId) {
        return iVehicleCheckInDAO.getVehicleWorkingDays(fromDate, toDate, branchId, vehicleId);
    }

    @Override
    public List<EFmFmVehicleInspectionPO> getParticularVehicleInspectionDetailByInspectedId(int inspectionId,
            int branchId) {
        return iVehicleCheckInDAO.getParticularVehicleInspectionDetailByInspectedId(inspectionId, branchId);
    }

    

    @Override
    public List<EFmFmFixedDistanceContractDetailPO> getFixedModifyDistanceDetails(
            EFmFmFixedDistanceContractDetailPO eFmFmFixedDistanceContractDetailPO) {
        
        return iVehicleCheckInDAO.getFixedModifyDistanceDetails(eFmFmFixedDistanceContractDetailPO);
    }

    @Override
    public List<EFmFmVendorContractInvoicePO> getInvoiceDetailsById(int branchId, int invoiceId) {
        
        return iVehicleCheckInDAO.getInvoiceDetailsById(branchId, invoiceId);
    }

	@Override
	public List<EFmFmEscortMasterPO> getEscortDetailsByEmpId(String escortEmpId, String branchId) {
		
		return iVehicleCheckInDAO.getEscortDetailsByEmpId(escortEmpId, branchId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getKmDetailsByVehicleId(Date fromDate, Date toDate, int branchId, int vehicleId,
			String contractType, int contractDetailId) {
		
		return iVehicleCheckInDAO.getKmDetailsByVehicleId(fromDate, toDate, branchId, vehicleId, contractType, contractDetailId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getAllContractDetailsByVendorId(int vendorId, String contractType, int branchId) {

		return iVehicleCheckInDAO.getAllContractDetailsByVendorId(vendorId, contractType, branchId);
	}

	@Override
	public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceDetails(int contrctDetailsId, int branchId) {
			return iVehicleCheckInDAO.getFixedDistanceDetails(contrctDetailsId,branchId);
	}

	@Override
	public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceDetailsByCloneId(int cloneId, int branchId) {
		
		return iVehicleCheckInDAO.getFixedDistanceDetailsByCloneId(cloneId, branchId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> bulkUpdateContractId(int vendorId, int contractDetailsId,
			int oldContractDetailsId, int branchId) {
		
		return iVehicleCheckInDAO.bulkUpdateContractId(vendorId, contractDetailsId, oldContractDetailsId, branchId);
	}

	   @Override
	   public void save(EFmFmFuelChargesPO eFmFmFuelChargesPO) {
	        iVehicleCheckInDAO.save(eFmFmFuelChargesPO);
	    }

	    @Override
	    public void update(EFmFmFuelChargesPO eFmFmFuelChargesPO) {
	        iVehicleCheckInDAO.update(eFmFmFuelChargesPO);
	    }
	    
	    @Override
	    public void delete(EFmFmFuelChargesPO eFmFmFuelChargesPO) {
	        iVehicleCheckInDAO.delete(eFmFmFuelChargesPO);
	    }	
	

	@Override
	public List<EFmFmFuelChargesPO> getFuelDetails(int fuelTypeId, int branchId) {
		
		return iVehicleCheckInDAO.getFuelDetails(fuelTypeId, branchId);
	}

	@Override
	public List<EFmFmFuelChargesPO> getAllFuelDetails(int branchId) {
		
		return iVehicleCheckInDAO.getAllFuelDetails(branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getTotalKmByVehicle(Date fromDate, Date toDate, int branchId, int vehicleId,
			String contractType, int contractDetailsId) {
		
		return iVehicleCheckInDAO.getTotalKmByVehicle(fromDate, toDate, branchId, vehicleId, contractType, contractDetailsId);
	}

	@Override
	public List<EFmFmVendorFuelContractTypeMasterPO> getAllContractFuelType(int branchId) {
		
		return iVehicleCheckInDAO.getAllContractFuelType(branchId);
	}

	@Override
	public List<EFmFmVendorFuelContractTypeMasterPO> getFuelContractTypeDetails(int fuelTypeId, int branchId) {
		
		return iVehicleCheckInDAO.getFuelContractTypeDetails(fuelTypeId, branchId);
	}

	@Override
	public List<EFmFmVendorContractInvoicePO> getNonApprovalList(String branchId, String statusFlg, String invoiceflag) {
		
		return iVehicleCheckInDAO.getNonApprovalList(branchId, statusFlg, invoiceflag);
	}

	@Override
	public List<EFmFmVendorContractInvoicePO> getModifiedAmountDetails(int branchId, int InvoiceNumber) {
		
		return iVehicleCheckInDAO.getModifiedAmountDetails(branchId, InvoiceNumber);
	}

	@Override
	public List<EFmFmFuelChargesPO> getFuelDetailsByDate(Date fromDate, int fuelTypeId, int branchId) {
		
		return iVehicleCheckInDAO.getFuelDetailsByDate(fromDate, fuelTypeId, branchId);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleDetails(String branchId) {
		return iVehicleCheckInDAO.getAllCheckedInVehicleDetails(branchId);
	}
	
	@Override
	public List<EFmFmVehicleDocsPO> getAlluploadVehicleFileDetails(int vehicleId) {		
		return iVehicleCheckInDAO.getAlluploadVehicleFileDetails(vehicleId);
	}

	@Override
	public void addVehicleUploadDetails(EFmFmVehicleDocsPO eFmFmVehicleDocsPO) {
		iVehicleCheckInDAO.addVehicleUploadDetails(eFmFmVehicleDocsPO);
		
	}

	@Override
	public List<EFmFmEscortMasterPO> getAllDisableEscortDetails(String branchId) {
		return iVehicleCheckInDAO.getAllDisableEscortDetails(branchId);
	}

	

	@Override
	public int getAllNonRemovedVehicleCountByBranchIdAndVendorId(int vendorId, String branchId) {
		return iVehicleCheckInDAO.getAllNonRemovedVehicleCountByBranchIdAndVendorId(vendorId, branchId);
	}

	@Override
	public int getAllNonRemovedDriverCountByBranchIdAndVendorId(int vendorId, String branchId) {
		return iVehicleCheckInDAO.getAllNonRemovedDriverCountByBranchIdAndVendorId(vendorId, branchId);
	}

	@Override
	public List<EFmFmDriverMasterPO> getAllRemovedDriverDetailsByBranchIdAndVendorId(String branchId, int vendorId) {
		return iVehicleCheckInDAO.getAllRemovedDriverDetailsByBranchIdAndVendorId(branchId, vendorId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getAllUnRemovedVehicleDetailsByBranchIdAndVendorId(String branchId, int vendorId) {
		return iVehicleCheckInDAO.getAllUnRemovedVehicleDetailsByBranchIdAndVendorId(branchId, vendorId);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInDriverDetailsByBranchIdAndVendorId(String branchId,
			int vendorId) {
		return iVehicleCheckInDAO.getParticularCheckedInDriverDetailsByBranchIdAndVendorId(branchId, vendorId);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getCheckedInVehicleDetailsWithOutDummyAllocatedToRoutes(int branchId) {
		return iVehicleCheckInDAO.getCheckedInVehicleDetailsWithOutDummyAllocatedToRoutes(branchId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getAllVehicleDetailsByBranchId(int branchId) {
		return iVehicleCheckInDAO.getAllVehicleDetailsByBranchId(branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getOngoingVehicleDetails(Date fromDate, Date toDate, int branchId, int vehicleId) {
		return iVehicleCheckInDAO.getOngoingVehicleDetails(fromDate, toDate, branchId, vehicleId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromVehicleNumberAndVendorIdBranchId(String vehicleNumber,
			int vendorId) {
		return iVehicleCheckInDAO.getVehicleDetailsFromVehicleNumberAndVendorIdBranchId(vehicleNumber, vendorId);
	}

	@Override
	public List<EFmFmDriverMasterPO> getDriverDetailsFromDriverNumberAndVendorId(String mobileNumber, int vendorId) {
		return iVehicleCheckInDAO.getDriverDetailsFromDriverNumberAndVendorId(mobileNumber, vendorId);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getParticularDriverCheckedInDetails(int driverId) {
		return iVehicleCheckInDAO.getParticularDriverCheckedInDetails(driverId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromVehicleNumberAndVendorId(String vehicleNumber,
			int vendorId) {
		return iVehicleCheckInDAO.getVehicleDetailsFromVehicleNumberAndVendorId(vehicleNumber, vendorId);
	}

	@Override
	public List<EFmFmDriverMasterPO> getDriverDetailsFromDriverMobilrNumber(String mobileNumber) {
		return iVehicleCheckInDAO.getDriverDetailsFromDriverMobilrNumber(mobileNumber);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromVehicleNumber(String vehicleNumber) {
		return iVehicleCheckInDAO.getVehicleDetailsFromVehicleNumber(vehicleNumber);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getParticularDeviceCheckedInDetails(int deviceId) {
		return iVehicleCheckInDAO.getParticularDeviceCheckedInDetails(deviceId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getSumOfTotalKmByVehicleOdometer(Date fromDate, Date toDate, int branchId,
			int vehicleId, String contractType, int contractDetailId) {
		return iVehicleCheckInDAO.getSumOfTotalKmByVehicleOdometer(fromDate, toDate, branchId, vehicleId, contractType, contractDetailId);
	}

	@Override
	public List<EFmFmVendorContractInvoicePO> deleteInvoiceData(Date fromDate, Date toDate, int branchId,
			int vendorId) {
		return iVehicleCheckInDAO.deleteInvoiceData(fromDate, toDate, branchId, vendorId);
	}

	@Override
	public List<EFmFmVendorContractInvoicePO> getInvoiceforVendorByGroupDistance(Date fromDate, Date toDate, int branchId,
			int vendorId, String distanceFlg) {
		return iVehicleCheckInDAO.getInvoiceforVendorByGroupDistance(fromDate, toDate, branchId, vendorId, distanceFlg);
	}

	@Override
	public EFmFmVehicleCheckInPO getCheckedInEntityDetailFromChecInId(int checkInId) {
		return iVehicleCheckInDAO.getCheckedInEntityDetailFromChecInId(checkInId);
	}

	@Override
	public void updateVehicleCheckInDetailsWithStatus(int checkInId, int numberOfTrips, long totalTravelTime,
			double totalTravelDistance,String status) {
	 iVehicleCheckInDAO.updateVehicleCheckInDetailsWithStatus(checkInId, numberOfTrips, totalTravelTime, totalTravelDistance,status);
	}

	@Override
	public void updateVehicleCheckInDetailsWithOutStatus(int checkInId, int numberOfTrips, long totalTravelTime,
			double totalTravelDistance) {
		 iVehicleCheckInDAO.updateVehicleCheckInDetailsWithOutStatus(checkInId, numberOfTrips, totalTravelTime, totalTravelDistance);
	}

	@Override
	public void updateVehicleMonthlyDistance(int vehicleId, double monthlyPendingFixedDistance,String status) {
		iVehicleCheckInDAO.updateVehicleMonthlyDistance(vehicleId, monthlyPendingFixedDistance,status);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getLastCheckInVehicleIdDetails(int branchId, int vehicleId) {
		return iVehicleCheckInDAO.getLastCheckInVehicleIdDetails(branchId, vehicleId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getlistOfYetToCheckInVehicleDetails(int branchId) {
		return iVehicleCheckInDAO.getlistOfYetToCheckInVehicleDetails(branchId);
	}

	@Override
	public List<EFmFmVehicleInspectionPO> getAllVendorInspectionsByBranchIdAndDate(Date fromDate, Date toDate,
			int branchId) {
		return iVehicleCheckInDAO.getAllVendorInspectionsByBranchIdAndDate(fromDate, toDate, branchId);
	}

	@Override
	public List<EFmFmVehicleInspectionPO> getVendorInspectionsByBranchIdAndDate(Date fromDate, Date toDate,
			int branchId, int vendorId) {
		return iVehicleCheckInDAO.getVendorInspectionsByBranchIdAndDate(fromDate, toDate, branchId, vendorId);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getparticularEmployeeTripDetails(String vehicleNumber, int branchId) {
		return iVehicleCheckInDAO.getparticularEmployeeTripDetails(vehicleNumber, branchId);
	}

	@Override
	public List<EFmFmDeviceMasterPO> getListOfAllavailableDevices(int branchId) {
		return iVehicleCheckInDAO.getListOfAllavailableDevices(branchId);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getParticularCheckedInVehileDetailsByBranchIdAndVendorId(int branchId,
			int vendorId) {		
		return iVehicleCheckInDAO.getParticularCheckedInVehileDetailsByBranchIdAndVendorId(branchId, vendorId);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleByVendors(int vendorId, int branchId) {
		return iVehicleCheckInDAO.getAllCheckedInVehicleByVendors(vendorId, branchId);
	}

	@Override
	public List<EFmFmEscortMasterPO> getEscostMobileNoDetails(String mobileNumber, String branchId) {
		return iVehicleCheckInDAO.getEscostMobileNoDetails(mobileNumber, branchId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getAllVehicleVendorsDetails(int branchId) {
		return iVehicleCheckInDAO.getAllVehicleVendorsDetails(branchId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getAllAvailableVehiclesByVendor(int branchId, int vendorId) {
		return iVehicleCheckInDAO.getAllAvailableVehiclesByVendor(branchId, vendorId);
	}

	@Override
	public List<EFmFmDriverMasterPO> getAllAvailableDriverByVendor(int branchId, int vendorId) {
	
		return iVehicleCheckInDAO.getAllAvailableDriverByVendor(branchId, vendorId);
	}

	@Override
	public List<EFmFmDeviceMasterPO> getAllAvailableDeviceWithOutDummy(int branchId) {		
		return iVehicleCheckInDAO.getAllAvailableDeviceWithOutDummy(branchId);
	}

	@Override
	public List<EFmFmEscortMasterPO> getEscostMobileNoWithTokenDetails(String mobileNumber, String tempCode,
			String branchId) {		
		return iVehicleCheckInDAO.getEscostMobileNoWithTokenDetails(mobileNumber, tempCode, branchId);
	}

	@Override
	public List<EFmFmEscortMasterPO> getEscortPasswordDetailsFromEscortAndBranchId(int escortId, int branchId) {		
		return iVehicleCheckInDAO.getEscortPasswordDetailsFromEscortAndBranchId(escortId, branchId);
	}

	@Override
	public List<EFmFmVendorContractInvoicePO> getListOfGeneratedInvoiceDetails(int branchId, String invoiceStatus) {
	
		return iVehicleCheckInDAO.getListOfGeneratedInvoiceDetails(branchId, invoiceStatus);
	}

	@Override
	public List<EFmFmVendorContractInvoicePO> getListOfInvoiceDetailsByGroupInvoiceNumber(int branchId,
			String invoiceStatus) {		
		return iVehicleCheckInDAO.getListOfInvoiceDetailsByGroupInvoiceNumber(branchId, invoiceStatus);
	}

	@Override
	public void updateVehicleContractInvoiceStatus(int invoiceNumber, String invoiceStatus) {
		iVehicleCheckInDAO.updateVehicleContractInvoiceStatus(invoiceNumber, invoiceStatus);
		
	}

	@Override
	public int saveVehicleRecordWithValue(EFmFmVehicleMasterPO eFmFmVehicleMasterPO) {	
		return iVehicleCheckInDAO.saveVehicleRecordWithValue(eFmFmVehicleMasterPO);
	}

	@Override
	public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceDummyDetails(int branchId) {
		return iVehicleCheckInDAO.getFixedDistanceDummyDetails(branchId);
	}

	@Override
	public void save(EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMasterPO) {
		iVehicleCheckInDAO.save(eFmFmVendorContractTypeMasterPO);		
	}

	@Override
	public void update(EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMasterPO) {
		iVehicleCheckInDAO.update(eFmFmVendorContractTypeMasterPO);
	}

	@Override
	public List<EFmFmVendorContractTypeMasterPO> getVendorContractTypeDetails(int branchId) {		
		return iVehicleCheckInDAO.getVendorContractTypeDetails(branchId);
	}

	@Override
	public List<EFmFmVendorContractTypeMasterPO> validateVendorContractTypeDetails(String contrctType, int branchId) {
		return iVehicleCheckInDAO.validateVendorContractTypeDetails(contrctType, branchId);
	}

	@Override
	public List<EFmFmFixedDistanceContractDetailPO> getFixedContractDetailsValidation(Date fromDate, Date toDate,
			int branchId, int vendorId) {
		return iVehicleCheckInDAO.getFixedContractDetailsValidation(fromDate, toDate, branchId, vendorId);
	}

	@Override
	public List<EFmFmFixedDistanceContractDetailPO> getFixedContractDetailsVehicleValidation(Date fromDate, Date toDate,
			int branchId, int vehicleId) {
		return iVehicleCheckInDAO.getFixedContractDetailsVehicleValidation(fromDate, toDate, branchId, vehicleId);
	}

	@Override
	public void deleteVehicleUploadDetails(int driverDocId) {
		iVehicleCheckInDAO.deleteVehicleUploadDetails(driverDocId);
		
	}

	@Override
	public List<EFmFmDriverDocsPO> getAlluploadFileDetails(int driverId) {
		return iVehicleCheckInDAO.getAlluploadFileDetails(driverId);
	}

	@Override
	public void deleteDriverUploadDetails(int driverDocId) {
		iVehicleCheckInDAO.deleteDriverUploadDetails(driverDocId);		
	}

	@Override
	public List<EFmFmFixedDistanceContractDetailPO> getAllVehicleContractDetails(Date fromDate, Date toDate,
			int branchId, int vendorId) {
		return iVehicleCheckInDAO.getAllVehicleContractDetails(fromDate, toDate, branchId, vendorId);
	}

	@Override
	public List<EFmFmVendorContractTypeMasterPO> getContractTypeIdDetails(int contractTypeId, int branchId) {
		return iVehicleCheckInDAO.getContractTypeIdDetails(contractTypeId, branchId);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleDetailsForEditBucketByvendorId(int branchId,
			int vendorId) {
		return iVehicleCheckInDAO.getAllCheckedInVehicleDetailsForEditBucketByvendorId(branchId, vendorId);
	}

	@Override
	public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceContrctTitleExistDetails(String contractTitle,
			int branchId) {
		return iVehicleCheckInDAO.getFixedDistanceContrctTitleExistDetails(contractTitle, branchId);
	}

	@Override
	public List<EFmFmVendorFuelContractTypeMasterPO> getDummyFuelType(String FuelType, int branchId) {
		return iVehicleCheckInDAO.getDummyFuelType(FuelType, branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getPresentDaysBasedOnTrips(Date fromDate, Date toDate, int branchId,
			int vehicleId) {		
		return iVehicleCheckInDAO.getPresentDaysBasedOnTrips(fromDate, toDate, branchId, vehicleId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getMaxTimePresentDaysBasedOnTrips(Date travelledDate, int branchId, int vehicleId) {
		return iVehicleCheckInDAO.getMaxTimePresentDaysBasedOnTrips(travelledDate, branchId, vehicleId);
	}

	@Override
	public int getAllNonRemovedSupervisorCountByBranchIdAndVendorId(int vendorId, String branchId) {
		return iVehicleCheckInDAO.getAllNonRemovedSupervisorCountByBranchIdAndVendorId(vendorId, branchId);
	}

	@Override
	public List<EFmFmDriverMasterPO> getAllDriversWithOutStatus(int branchId, int vendorId) {
		return iVehicleCheckInDAO.getAllDriversWithOutStatus(branchId, vendorId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getAllVehiclesWithOutStatus(int branchId, int vendorId) {
		return iVehicleCheckInDAO.getAllVehiclesWithOutStatus(branchId, vendorId);
	}

	@Override
	public List<EFmFmDynamicVehicleCheckListPO> getAllcheckListDetails(int branchId, String checkListType) {
		return iVehicleCheckInDAO.getAllcheckListDetails(branchId, checkListType);
	}

	@Override
	public List<EFmFmAssignRoutePO> getPastTripDetailsforDriver(Date fromDate, Date toDate, int branchId,
			int driverId) {		
		return iVehicleCheckInDAO.getPastTripDetailsforDriver(fromDate, toDate, branchId, driverId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getVehicleExistDetailsByVehicleNumberVendorNameAndBranchId(String vehicleNumber,
			String vendorName, String branchId) {
		return iVehicleCheckInDAO.getVehicleExistDetailsByVehicleNumberVendorNameAndBranchId(vehicleNumber, vendorName, branchId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getParticularVehicleDetailsByVehicleNumber(String vehicleNumber, int vendorId,
			String branchId) {
		return iVehicleCheckInDAO.getParticularVehicleDetailsByVehicleNumber(vehicleNumber, vendorId, branchId);
	}

	

	@Override
	public List<EFmFmVehicleMasterPO> getEngineNoDetails(String engineNo, int vendorId, String branchId) {
		return iVehicleCheckInDAO.getEngineNoDetails(engineNo, vendorId, branchId);
	}

	@Override
	public List<EFmFmVehicleMasterPO> getRcNumberDetails(String rcNumber, int vendorId, String branchId) {
		return iVehicleCheckInDAO.getRcNumberDetails(rcNumber, vendorId, branchId);
	}

	@Override
	public List<EFmFmEscortMasterPO> getAllEscortDetails(String branchId) {
		return iVehicleCheckInDAO.getAllEscortDetails(branchId);
	}

	@Override
	public List<EFmFmEscortMasterPO> getParticularEscortDetails(int escortId) {
		return iVehicleCheckInDAO.getParticularEscortDetails(escortId);
	}

	@Override
	public List<EFmFmEscortCheckInPO> getAllCheckedInEscort(String branchId) {
		return iVehicleCheckInDAO.getAllCheckedInEscort(branchId);
	}

	@Override
	public List<EFmFmEscortMasterPO> getAllCheckInEscort(String branchId) {
		return iVehicleCheckInDAO.getAllCheckInEscort(branchId);
	}

	@Override
	public List<EFmFmEscortCheckInPO> getParticulaEscortDetailFromEscortId(String branchId, int escortId) {
		return iVehicleCheckInDAO.getParticulaEscortDetailFromEscortId(branchId, escortId);
	}

	@Override
	public List<EFmFmEscortMasterPO> getEscortDetailsByActiveFlg(int escortId) {
		return iVehicleCheckInDAO.getEscortDetailsByActiveFlg(escortId);
	}

	@Override
	public List<EFmFmDeviceMasterPO> getDeviceDetailsFromDeviceIdAndBranchId(int deviceId, int branchId) {
		return iVehicleCheckInDAO.getDeviceDetailsFromDeviceIdAndBranchId(deviceId, branchId);
	}

	@Override
	public List<EFmFmVehicleCapacityMasterPO> getVehicleTypeBranchWise(String branchId) {
		return iVehicleCheckInDAO.getVehicleTypeBranchWise(branchId);
	}

	@Override
	public void deleteVehicleTypeBranchId(int vehicleCapacityId) {
		iVehicleCheckInDAO.deleteVehicleTypeBranchId(vehicleCapacityId);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getVehicleAlreadyCheckInOrNot(int vehicleId) {
		return iVehicleCheckInDAO.getVehicleAlreadyCheckInOrNot(vehicleId);
	}

	@Override
	public List<EFmFmDeviceMasterPO> getDummyDeviceDetailsFromDeviceId(String branchId) {
		return iVehicleCheckInDAO.getDummyDeviceDetailsFromDeviceId(branchId);
	}

	
}