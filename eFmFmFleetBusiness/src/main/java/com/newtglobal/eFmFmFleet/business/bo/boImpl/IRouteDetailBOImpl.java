package com.newtglobal.eFmFmFleet.business.bo.boImpl;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtglobal.eFmFmFleet.business.bo.IRouteDetailBO;
import com.newtglobal.eFmFmFleet.business.dao.IRouteDetailDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaNodalMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientRouteMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmZoneMasterPO;

@Service("IRouteDetailBO")
public class IRouteDetailBOImpl implements IRouteDetailBO {

    @Autowired
    IRouteDetailDAO iRouteDetailDAO;

    public void setiRouteDetailDAO(IRouteDetailDAO iRouteDetailDAO) {
        this.iRouteDetailDAO = iRouteDetailDAO;
    }

    @Override
	public void update(EFmFmZoneMasterPO eFmFmZoneMasterPO) {
		iRouteDetailDAO.update(eFmFmZoneMasterPO);
		
	}

    @Override
	public void update(EFmFmAreaNodalMasterPO eFmFmNodalAreaMaster) {
		iRouteDetailDAO.update(eFmFmNodalAreaMaster);
		
	}
    
    @Override
    public String getGeoCode(String address) {
        return iRouteDetailDAO.getGeoCode(address);
    }

    @Override
    public EFmFmAreaMasterPO getAreaId(String areaName) {
        return iRouteDetailDAO.getAreaId(areaName);
    }

    @Override
    public void save(EFmFmAreaNodalMasterPO eFmFmAreaNodalMasterPO) {

        iRouteDetailDAO.save(eFmFmAreaNodalMasterPO);
    }

    @Override
    public void saveDriverRecord(EFmFmDriverMasterPO eFmFmDriverMasterPO) {
        iRouteDetailDAO.saveDriverRecord(eFmFmDriverMasterPO);

    }

    @Override
    public void saveAreaRecord(EFmFmAreaMasterPO eFmFmAreaMasterPO) {
        iRouteDetailDAO.saveAreaRecord(eFmFmAreaMasterPO);

    }

    @Override
    public List<EFmFmAreaMasterPO> getAllAreaName(String areaName) {
        return iRouteDetailDAO.getAllAreaName(areaName);
    }

    @Override
    public void saveRouteNameRecord(EFmFmZoneMasterPO eFmFmZoneMasterPO) {
        iRouteDetailDAO.saveRouteNameRecord(eFmFmZoneMasterPO);
    }

    @Override
    public List<EFmFmZoneMasterPO> getAllRouteName(String routeName) {
        return iRouteDetailDAO.getAllRouteName(routeName);
    }

    @Override
    public void saveRouteMappingDetails(EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO) {
        iRouteDetailDAO.saveRouteMappingDetails(eFmFmRouteAreaMappingPO);
    }

    @Override
    public void save(EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO) {
        iRouteDetailDAO.save(eFmFmRouteAreaMappingPO);
    }

    @Override
    public List<EFmFmRouteAreaMappingPO> routeMappaingAlreadyExist(EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO) {
        return iRouteDetailDAO.routeMappaingAlreadyExist(eFmFmRouteAreaMappingPO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllOnRoadVehicleDetails(int clientId, Date todayDate) {
        return iRouteDetailDAO.getAllOnRoadVehicleDetails(clientId, todayDate);
    }

    @Override
    public List<EFmFmZoneMasterPO> getAllZoneNames(int clientId) {
        return iRouteDetailDAO.getAllZoneNames(clientId);
    }

    @Override
    public void modifyDriverRecord(EFmFmDriverMasterPO eFmFmDriverMasterPO) {
        iRouteDetailDAO.modifyDriverRecord(eFmFmDriverMasterPO);
    }

    @Override
    public void saveClientRouteMapping(EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO) {
        iRouteDetailDAO.saveClientRouteMapping(eFmFmClientRouteMappingPO);
    }

    @Override
    public List<EFmFmClientRouteMappingPO> clientRouteMappaingAlreadyExist(
            EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO) {
        return iRouteDetailDAO.clientRouteMappaingAlreadyExist(eFmFmClientRouteMappingPO);
    }

    @Override
    public List<EFmFmDriverMasterPO> getValidLicenseNumber(String licenseNumber,int vendorId,String branchId) {
        return iRouteDetailDAO.getValidLicenseNumber(licenseNumber,vendorId,branchId);
    }

    @Override
    public EFmFmRouteAreaMappingPO getRouteAreaId(String areaName, int branchId, String zoneName) {
        return iRouteDetailDAO.getRouteAreaId(areaName, branchId, zoneName);
    }

    @Override
    public List<EFmFmClientRouteMappingPO> getAllRoutesOfParticularClient(String branchId) {
        return iRouteDetailDAO.getAllRoutesOfParticularClient(branchId);
    }

    @Override
    public List<EFmFmRouteAreaMappingPO> getAllAreasFromZoneId(int zoneId) {
        return iRouteDetailDAO.getAllAreasFromZoneId(zoneId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getVehicleDetailFromVehicleId(int branchId, int checInId) {
        return iRouteDetailDAO.getVehicleDetailFromVehicleId(branchId, checInId);
    }

    @Override
    public List<EFmFmClientRouteMappingPO> getParticularRouteDetailByClient(String branchId, String routeName) {
        return iRouteDetailDAO.getParticularRouteDetailByClient(branchId, routeName);
    }

    @Override
    public List<EFmFmAreaMasterPO> getParticularAreaNameDetails(String areaName) {
        return iRouteDetailDAO.getParticularAreaNameDetails(areaName);
    }

    @Override
    public List<EFmFmClientRouteMappingPO> getAllNodalRoutesOfParticularClient(int branchId) {
        return iRouteDetailDAO.getAllNodalRoutesOfParticularClient(branchId);
    }

    @Override
    public List<EFmFmAreaNodalMasterPO> getAllAreasNodalPointsFromAreaId(int areaId) {
        return iRouteDetailDAO.getAllAreasNodalPointsFromAreaId(areaId);
    }

    @Override
    public List<EFmFmAreaNodalMasterPO> getParticularNodalPointNameDetails(String nodalPointName) {
        return iRouteDetailDAO.getParticularNodalPointNameDetails(nodalPointName);
    }

    @Override
    public List<EFmFmRouteAreaMappingPO> getRouteAreaIdFromAreaNameAndZoneNameForExcelUpload(String areaName,
            int branchId, String zoneName, String nodalPointName) {
        return iRouteDetailDAO.getRouteAreaIdFromAreaNameAndZoneNameForExcelUpload(areaName, branchId, zoneName,
                nodalPointName);
    }

    @Override
    public List<EFmFmAreaNodalMasterPO> getNodalPointsFromAreaIdAndNodalName(int areaId, String nodalPointName) {
        return iRouteDetailDAO.getNodalPointsFromAreaIdAndNodalName(areaId, nodalPointName);
    }

    @Override
    public List<EFmFmZoneMasterPO> getAllNodalZoneNames(String branchId) {
        return iRouteDetailDAO.getAllNodalZoneNames(branchId);
    }

    @Override
    public List<EFmFmRouteAreaMappingPO> getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(int areaId,
            String branchId, int zoneId, int nodalPointId) {
        return iRouteDetailDAO.getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(areaId, branchId, zoneId,
                nodalPointId);
    }

    @Override
    public List<EFmFmZoneMasterPO> getAllNonNodalZoneNames(String branchId) {
        return iRouteDetailDAO.getAllNonNodalZoneNames(branchId);
    }

    @Override
    public List<EFmFmAreaNodalMasterPO> getParticularNodalPointNameByNodalId(int nodalPointId) {
        return iRouteDetailDAO.getParticularNodalPointNameByNodalId(nodalPointId);
    }

    @Override
    public List<EFmFmAreaNodalMasterPO> getNodalPointsFromNodalName(String nodalPointName) {
        return iRouteDetailDAO.getNodalPointsFromNodalName(nodalPointName);
    }

   
   

    @Override
    public List<EFmFmAreaMasterPO> getParticularAreaDeatilsFromAreaName(String areaName) {
        return iRouteDetailDAO.getParticularAreaDeatilsFromAreaName(areaName);
    }

    @Override
    public List<EFmFmAreaMasterPO> getAllAreaNames() {
        return iRouteDetailDAO.getAllAreaNames();
    }

    @Override
    public List<EFmFmDriverMasterPO> getValidMobileNumberCheck(String mobileNumber,int vendorId,String branchId) {
        return iRouteDetailDAO.getValidMobileNumberCheck(mobileNumber,vendorId,branchId);
    }

    @Override
    public List<EFmFmRouteAreaMappingPO> getRouteAreaIdbyAreaName(String areaName) {
       
        return iRouteDetailDAO.getRouteAreaIdbyAreaName(areaName);
    }

	@Override
	public List<EFmFmZoneMasterPO> getParticularRouteFromRouteId(int zoneId) {
		return iRouteDetailDAO.getParticularRouteFromRouteId(zoneId);
	}

	@Override
	public List<EFmFmAreaNodalMasterPO> getParticularNodalPointDetailFromNodalPointId(int nodalPointId) {
		return iRouteDetailDAO.getParticularNodalPointDetailFromNodalPointId(nodalPointId);
	}

	@Override
	public List<EFmFmDriverMasterPO> getValidUniqueBatchNumber(String badgeNumber,int vendorId,String branchId) {
		return iRouteDetailDAO.getValidUniqueBatchNumber(badgeNumber,vendorId,branchId);
	}

	@Override
	public List<EFmFmAreaNodalMasterPO> getAllNodalPointsByBranchId(int branchId) {
		return iRouteDetailDAO.getAllNodalPointsByBranchId(branchId);
	}

	@Override
	public int saveDriverRecordWithValue(EFmFmDriverMasterPO eFmFmDriverMasterPO) {
		return iRouteDetailDAO.saveDriverRecordWithValue(eFmFmDriverMasterPO);
	}

	@Override
	public int addRouteName(EFmFmZoneMasterPO eFmFmZoneMasterPO) {		
		return iRouteDetailDAO.addRouteName(eFmFmZoneMasterPO);
	}

	@Override
	public int addAreaName(EFmFmAreaMasterPO eFmFmAreaMasterPO) {		
		return iRouteDetailDAO.addAreaName(eFmFmAreaMasterPO);
	}

	@Override
	public int saveAreaWithZone(EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO) {
		return iRouteDetailDAO.saveAreaWithZone(eFmFmRouteAreaMappingPO);
	}
	@Override
	public List<EFmFmClientRouteMappingPO> getAllRoutesOfParticularClientFromBranchId(String branchId) {
		return iRouteDetailDAO.getAllRoutesOfParticularClientFromBranchId(branchId);
	}

	@Override
	public List<EFmFmZoneMasterPO> getNonNodalRouteNameFromClientIdAndRouteName(String branchId, String routeName) {
		return iRouteDetailDAO.getNonNodalRouteNameFromClientIdAndRouteName(branchId, routeName);
	}

	@Override
	public List<EFmFmZoneMasterPO> getNodalRouteNameFromClientIdAndRouteName(String branchId, String routeName) {
		return iRouteDetailDAO.getNodalRouteNameFromClientIdAndRouteName(branchId, routeName);
	}

	@Override
	public boolean zoneNameExistCheck(String zoneName) {
		return iRouteDetailDAO.zoneNameExistCheck(zoneName);
	}

	@Override
	public boolean nodalZoneNameExistCheck(String zoneName) {
		return iRouteDetailDAO.nodalZoneNameExistCheck(zoneName);
	}

	@Override
	public void save(EFmFmClientAreaMappingPO eFmFmClientAreaMappingPO) {
		 iRouteDetailDAO.save(eFmFmClientAreaMappingPO);
	}

	@Override
	public List<EFmFmClientAreaMappingPO> getClientAreaMappaingData(int areaId, int brnachId) {
		return iRouteDetailDAO.getClientAreaMappaingData(areaId, brnachId);
	}

	@Override
	public List<EFmFmClientRouteMappingPO> getClientRouteMappaingAlreadyExistCheck(int zoneId, int branchId) {
		return iRouteDetailDAO.getClientRouteMappaingAlreadyExistCheck(zoneId, branchId);
	}

	@Override
	public List<EFmFmClientAreaMappingPO> getClientAreaMappaingDataFromFacilityIds(String branchId) {
		return iRouteDetailDAO.getClientAreaMappaingDataFromFacilityIds(branchId);
	}

	
}
