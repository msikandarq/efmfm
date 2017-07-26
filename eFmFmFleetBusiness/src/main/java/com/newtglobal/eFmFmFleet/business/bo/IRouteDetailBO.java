package com.newtglobal.eFmFmFleet.business.bo;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmFmAreaMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaNodalMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientRouteMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmZoneMasterPO;




public interface IRouteDetailBO {
	/**
	* The getGeoCode implements for
	* Generating Latitude & longitude for given Address.
	*
	* @author  Rajan R
	* 
	* @since   2015-05-05 
	*/
	public String getGeoCode(String address);
	/**
	* The getRouteId implements for
	* Getting RouteId based on the Area Name. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-05 
	*/
	public EFmFmAreaMasterPO getAreaId(String areaName);
	
	void update(EFmFmZoneMasterPO eFmFmZoneMasterPO);

	/**
	* The getValidLicenseNumber implements for
	* validating driver license number. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/
	public List<EFmFmDriverMasterPO> getValidLicenseNumber(String licenseNumber,int vendorId,String branchId);
	/**
	* The saveDriverRecord implements for
	* storing drvier details into driver master table from xl utility. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/
	public void saveDriverRecord(EFmFmDriverMasterPO  eFmFmDriverMasterPO);
	
	/**
	* The saveAreaRecord implements for
	* storing Area details into Area master table from xl utility. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/
	public void saveAreaRecord(EFmFmAreaMasterPO  eFmFmAreaMasterPO);
	
	public void save(EFmFmAreaNodalMasterPO eFmFmAreaNodalMasterPO);

	/**
	* The getAllAreaName implements for
	* for validating and getting all area names from area master table. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/
	public List<EFmFmAreaMasterPO> getAllAreaName(String areaName);
	/**
	* The saveRouteNameRecord implements for
	* storing Area details into Route master table from xl utility. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/
	public void saveRouteNameRecord(EFmFmZoneMasterPO  eFmFmZoneMasterPO);
	/**
	* The getAllAreaName implements for
	* for validating and getting all area names from area master table. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/
	public List<EFmFmZoneMasterPO> getAllRouteName(String routeName);
	public void update(EFmFmAreaNodalMasterPO eFmFmNodalAreaMaster);


	/**
	* The getAllOnRoadVehicleDetails implements for
	* Getting all checkedIn on Road Vehicle details using Current Date. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-21 
	*/
	public List<EFmFmAssignRoutePO> getAllOnRoadVehicleDetails(int clientId,
			Date todayDate);
		/**
	* The saveRouteMappingDetails implements for
	* saving routeMapping with Routeid and areaId. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-22 
	*/
	public void saveRouteMappingDetails(EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO);
	public void save(EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO);

	/**
	* The routeMappaingAlreadyExist implements for
	* validating RouteID already Exist or not. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-22 
	*/
	public List<EFmFmRouteAreaMappingPO> routeMappaingAlreadyExist(EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO);
	
	/**
	* The getAllZoneNames implements for
	* getting all zoneNames. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-26 
	*/
	public List<EFmFmZoneMasterPO> getAllZoneNames(int clientId);
	
	
	/**
	* The modifyDriverRecord implements for
	* modifying drvier details on driver master table. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-29 
	*/
	public void modifyDriverRecord(EFmFmDriverMasterPO  eFmFmDriverMasterPO);
	/**
	* The saveClientRouteMappaing implements for
	* saving saveClientRouteMapping  details. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-29 
	*/
	public void saveClientRouteMapping(EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO);
	/**
	* The clientrouteMappaingAlreadyExist implements for
	* validating ClientRoute already Exist or not. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-29 
	*/
	public List<EFmFmClientRouteMappingPO> clientRouteMappaingAlreadyExist(EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO);
	public EFmFmRouteAreaMappingPO getRouteAreaId(String areaName, int branchId,
			String zoneName);
	public List<EFmFmClientRouteMappingPO> getAllRoutesOfParticularClient(String branchId);
	public List<EFmFmRouteAreaMappingPO> getAllAreasFromZoneId(int zoneId);
	public List<EFmFmAssignRoutePO> getVehicleDetailFromVehicleId(int branchId,
			int checInId);
	public List<EFmFmClientRouteMappingPO> getParticularRouteDetailByClient(
			String branchId, String routeName);	
	public List<EFmFmRouteAreaMappingPO> getRouteAreaIdFromAreaNameAndZoneNameForExcelUpload(
			String areaName, int branchId, String zoneName,
			String nodalPointName);
	public List<EFmFmAreaMasterPO> getParticularAreaNameDetails(String areaName);
	public List<EFmFmClientRouteMappingPO> getAllNodalRoutesOfParticularClient(
			int branchId);
	public List<EFmFmAreaNodalMasterPO> getAllAreasNodalPointsFromAreaId(int areaId);
	
	public List<EFmFmAreaNodalMasterPO> getParticularNodalPointNameDetails(
			String nodalPointName);
	public List<EFmFmAreaNodalMasterPO> getNodalPointsFromAreaIdAndNodalName(
			int areaId, String nodalPointName);

	public List<EFmFmZoneMasterPO> getAllNodalZoneNames(String branchId);
	public List<EFmFmRouteAreaMappingPO> getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(
			int areaId, String branchId, int zoneId, int nodalPointId);
	public List<EFmFmZoneMasterPO> getAllNonNodalZoneNames(String branchId);
	public List<EFmFmAreaNodalMasterPO> getParticularNodalPointNameByNodalId(
			int nodalPointId);
	public List<EFmFmAreaNodalMasterPO> getNodalPointsFromNodalName(
			String nodalPointName);
	public List<EFmFmAreaMasterPO> getParticularAreaDeatilsFromAreaName(String areaName);
	public List<EFmFmAreaMasterPO> getAllAreaNames();
	public List<EFmFmDriverMasterPO> getValidMobileNumberCheck(String mobileNumber,int vendorId,String branchId);
    public List<EFmFmRouteAreaMappingPO> getRouteAreaIdbyAreaName(String areaName);
	public List<EFmFmZoneMasterPO> getParticularRouteFromRouteId(int zoneId);
	public List<EFmFmAreaNodalMasterPO> getParticularNodalPointDetailFromNodalPointId(int nodalPointId);
	public List<EFmFmDriverMasterPO> getValidUniqueBatchNumber(String badgeNumber,int vendorId,String branchId);
	public List<EFmFmAreaNodalMasterPO> getAllNodalPointsByBranchId(int branchId);
	public int saveDriverRecordWithValue(EFmFmDriverMasterPO eFmFmDriverMasterPO);
	public int addRouteName(EFmFmZoneMasterPO eFmFmZoneMasterPO);
	public int addAreaName(EFmFmAreaMasterPO eFmFmAreaMasterPO);
	public int saveAreaWithZone(EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO);
	public List<EFmFmClientRouteMappingPO> getAllRoutesOfParticularClientFromBranchId(String branchId);
	public List<EFmFmZoneMasterPO> getNonNodalRouteNameFromClientIdAndRouteName(String branchId, String routeName);
	public List<EFmFmZoneMasterPO> getNodalRouteNameFromClientIdAndRouteName(String branchId, String routeName);
	public boolean zoneNameExistCheck(String zoneName);
	public boolean nodalZoneNameExistCheck(String zoneName);
	public void save(EFmFmClientAreaMappingPO eFmFmClientAreaMappingPO);
	public List<EFmFmClientAreaMappingPO> getClientAreaMappaingData(int areaId, int brnachId);
	public List<EFmFmClientRouteMappingPO> getClientRouteMappaingAlreadyExistCheck(int zoneId, int branchId);
	public List<EFmFmClientAreaMappingPO> getClientAreaMappaingDataFromFacilityIds(String branchId);
}
