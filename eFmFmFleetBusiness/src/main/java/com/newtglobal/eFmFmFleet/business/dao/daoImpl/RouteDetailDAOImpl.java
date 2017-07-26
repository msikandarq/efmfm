package com.newtglobal.eFmFmFleet.business.dao.daoImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newtglobal.eFmFmFleet.business.dao.IRouteDetailDAO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAreaNodalMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientRouteMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFacilityToFacilityMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmRouteAreaMappingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmZoneMasterPO;

@Repository("IRouteDetailDAO")
public class RouteDetailDAOImpl implements IRouteDetailDAO {
	
	private EntityManager entityManager;	

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/**
	* The saveDriverRecord implements for
	* storing drvier details into driver master table from xl utility. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveDriverRecord(EFmFmDriverMasterPO eFmFmDriverMasterPO) {		
		entityManager.persist(eFmFmDriverMasterPO);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmZoneMasterPO eFmFmZoneMasterPO) {
		entityManager.merge(eFmFmZoneMasterPO);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(EFmFmAreaNodalMasterPO eFmFmNodalAreaMaster) {
		entityManager.merge(eFmFmNodalAreaMaster);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveRouteMappingDetails(EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO) {
		entityManager.merge(eFmFmRouteAreaMappingPO);		
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmAreaNodalMasterPO eFmFmAreaNodalMasterPO) {
		entityManager.persist(eFmFmAreaNodalMasterPO);		
	}
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO) {
		entityManager.persist(eFmFmRouteAreaMappingPO);		
	}
	/**
	* The saveAreaRecord implements for
	* storing Area details into Area master table from xl utility. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveAreaRecord(EFmFmAreaMasterPO eFmFmAreaMasterPO) {
		entityManager.persist(eFmFmAreaMasterPO);		
	}
	
	/**
	* The saveAreaRecord with clientAreaMapping Table implements for
	* storing Area id and branchId. 
	*
	* @author  Sarfraz Khan
	* 
	* @since   2017-07-05 
	*/

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmClientAreaMappingPO eFmFmClientAreaMappingPO) {
		entityManager.persist(eFmFmClientAreaMappingPO);		
	}
	
	
	
	
	/**
	* The getGeoCode implements for
	* Generating Latitude & longitude for given Address.
	*
	* @author  Rajan R
	* 
	* @since   2015-05-05 
	*/
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String getGeoCode(String address) {
		String geoCode="unknown";
		try{					
				String line = "";
				String urlLocation = "https://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=true";			
				URL geocodeURL = new URL(urlLocation);
				URLConnection connection = geocodeURL.openConnection();
				connection.connect();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer data = new StringBuffer();
				line = "";
				while((line = reader.readLine()) != null){
					data.append(line.trim());
				}				
				JSONArray results = new JSONObject(data.toString()).getJSONArray("results");
				if(results.length() >0){
					JSONObject geo = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
					geoCode=geo.getDouble("lat")+","+geo.getDouble("lng");			
				}			
								
		}catch(Exception ex){
			ex.printStackTrace();
			
		}				
		return geoCode;
	}
	
	/**
	* The getAreaId implements for
	* Getting AreaId based on the Area Name. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-05 
	*/
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EFmFmAreaMasterPO  getAreaId(String areaName){
    	List <EFmFmAreaMasterPO> areaMasterPO = new ArrayList<EFmFmAreaMasterPO>();
    	Query query=entityManager.createQuery("SELECT b FROM EFmFmAreaMasterPO b where b.areaName='"+areaName+"'");
    	try {
    		areaMasterPO=query.getResultList();			
		} catch (Exception e) {
		}
    	if(areaMasterPO.isEmpty()){
    		return null;
    	}else{
    		return areaMasterPO.get(0);	
    	}
    	
       
    }
	
	
	/**
	* The particular area details from area name for
	* for validating and getting all area names from area master table. 
	*
	* @author  Sarfraz Khan
	* 
	* @since   2016-05-22
	*/
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAreaMasterPO> getParticularAreaDeatilsFromAreaName(String areaName) {
		List <EFmFmAreaMasterPO> areaMaster = new ArrayList<EFmFmAreaMasterPO>();
	    	Query query=entityManager.createQuery("SELECT b FROM EFmFmAreaMasterPO b where b.areaName='"+areaName+"'");

	    	areaMaster=query.getResultList();
		return areaMaster;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getValidLicenseNumber(String licenseNumber,int vendorId,String branchId) {
		List<EFmFmDriverMasterPO> allDriverDetail = new ArrayList<EFmFmDriverMasterPO>() ;
		Query query = entityManager.createQuery("SELECT b FROM EFmFmDriverMasterPO as b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.licenceNumber ='"+licenseNumber+"' AND v.vendorId='"+vendorId+"' AND c.branchId in ("+ branchId + ")");
		allDriverDetail = query.getResultList();
		return allDriverDetail;
		
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getValidMobileNumberCheck(String mobileNumber,int vendorId,String branchId) {
		List<EFmFmDriverMasterPO> allDriverDetail = new ArrayList<EFmFmDriverMasterPO>() ;
		Query query = entityManager.createQuery("SELECT b FROM EFmFmDriverMasterPO as b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.mobileNumber ='"+mobileNumber+"' AND v.vendorId='"+vendorId+"' AND c.branchId in ("+ branchId + ")");
		allDriverDetail = query.getResultList();
		return allDriverDetail;
		
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getValidUniqueBatchNumber(String badgeNumber,int vendorId,String branchId) {
		List<EFmFmDriverMasterPO> allDriverDetail = new ArrayList<EFmFmDriverMasterPO>() ;
		Query query = entityManager.createQuery("SELECT b FROM EFmFmDriverMasterPO as b JOIN b.efmFmVendorMaster v JOIN v.eFmFmClientBranchPO c where b.batchNumber ='"+badgeNumber+"' AND v.vendorId='"+vendorId+"' AND c.branchId in ("+ branchId + ")");
		allDriverDetail = query.getResultList();
		return allDriverDetail;
		
	}
	
	/**
	* The getAllAreaName implements for
	* for validating and getting all area names from area master table. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/	
	/*@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAreaMasterPO> getAllAreaName(String areaName) {
		List<EFmFmAreaMasterPO> areaMasterPO=new ArrayList<EFmFmAreaMasterPO>();
		Query query =entityManager.createQuery("SELECT b FROM EFmFmAreaMasterPO as b where TRIM(UPPER(b.areaName))=TRIM(UPPER('"+areaName+"'))");
		areaMasterPO=query.getResultList();
		return areaMasterPO;
	}*/
	
	@Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmAreaMasterPO> getAllAreaName(String areaName) {
	  List<EFmFmAreaMasterPO> areaMasterPO=new ArrayList<EFmFmAreaMasterPO>();
	  Query query =entityManager.createQuery("SELECT b FROM EFmFmAreaMasterPO as b "
	  		+ " where TRIM(UPPER(REPLACE(b.areaName,' ','')))=TRIM(UPPER(REPLACE('"+areaName+"',' ','')))");
	  areaMasterPO=query.getResultList();
	  return areaMasterPO;
	 }
	
	
	/**
	* The saveRouteNameRecord implements for
	* storing Area details into Route master table from xl utility. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveRouteNameRecord(EFmFmZoneMasterPO eFmFmZoneMasterPO) {
		entityManager.persist(eFmFmZoneMasterPO);
		
	}
	/**
	* The getAllRouteName implements for
	* for validating and getting all area names from area master table. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-06 
	*/
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmZoneMasterPO> getAllRouteName(String routeName) {
		List <EFmFmZoneMasterPO> routeMasterPO = new ArrayList<EFmFmZoneMasterPO>();
	     Query query=entityManager.createQuery(
	          "SELECT b FROM EFmFmZoneMasterPO as b where TRIM(UPPER(REPLACE(b.zoneName,' ','')))=TRIM(UPPER(REPLACE('"+routeName+"',' ','')))");
	     routeMasterPO=query.getResultList();
		return routeMasterPO;
	}
	
	
	
	/**
	* The getAllRouteName implements for
	* for getting the particular zone name
	*
	* @author Sarfraz Khan
	* 
	* @since   2016-11-08 
	*/
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmZoneMasterPO> getParticularRouteFromRouteId(int zoneId) {
		List <EFmFmZoneMasterPO> routeMasterPO = new ArrayList<EFmFmZoneMasterPO>();
	     Query query=entityManager.createQuery(
	          "SELECT b FROM EFmFmZoneMasterPO as b where b.zoneId='"+zoneId+"'");
	     routeMasterPO=query.getResultList();
		return routeMasterPO;
	}
	
	
	/**
	* Check Particular Zone Name exist or not
	*
	* @author Sarfraz Khan
	* 
	* @since   2016-06-09 
	*/
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean zoneNameExistCheck(String zoneName) {
		List <EFmFmZoneMasterPO> routeMasterPO = new ArrayList<EFmFmZoneMasterPO>();
	     Query query=entityManager.createQuery(
	          "SELECT b FROM EFmFmZoneMasterPO as b where b.zoneName='"+zoneName+"' AND b.isNodalRoute=false AND b.status='Y' ");
	     routeMasterPO=query.getResultList();
	     if(!(routeMasterPO.isEmpty())){
				return true;
			}
			return false;   
			}	
	
	

	/**
	* Check Particular Zone Name exist or not
	*
	* @author Sarfraz Khan
	* 
	* @since   2016-06-09 
	*/
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean nodalZoneNameExistCheck(String zoneName) {
		List <EFmFmZoneMasterPO> routeMasterPO = new ArrayList<EFmFmZoneMasterPO>();
	     Query query=entityManager.createQuery(
	          "SELECT b FROM EFmFmZoneMasterPO as b where b.zoneName='"+zoneName+"' AND b.isNodalRoute=true AND b.status='Y' ");
	     routeMasterPO=query.getResultList();
	     if(!(routeMasterPO.isEmpty())){
				return true;
			}
			return false;   
			}	
	/**
	* The NodalPoint implements for
	* for getting the particular NodalPoint
	*
	* @author Sarfraz Khan
	* 
	* @since   2016-11-08 
	*/
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAreaNodalMasterPO> getParticularNodalPointDetailFromNodalPointId(int nodalPointId) {
		List <EFmFmAreaNodalMasterPO> nodalPoints = new ArrayList<EFmFmAreaNodalMasterPO>();
	     Query query=entityManager.createQuery(
	          "SELECT b FROM EFmFmAreaNodalMasterPO as b where b.nodalPointId='"+nodalPointId+"'");
	     nodalPoints=query.getResultList();
		return nodalPoints;
	}
	
	 @Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmAssignRoutePO> getAllOnRoadVehicleDetails(int branchId,Date todayDate) {
	  List <EFmFmAssignRoutePO> onRoadVehicle = new ArrayList<EFmFmAssignRoutePO>();
	  Format formatter;  
	  formatter = new SimpleDateFormat("yyyy-MM-dd");  
	  Query query=entityManager.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.efmFmVehicleCheckIn c  JOIN c.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where g.branchId ='"+branchId+"' and DATE(b.tripShiftTime)=TRIM('"+formatter.format(todayDate)+"')) ");  
	  onRoadVehicle=query.getResultList();  
	  return onRoadVehicle;
	 }

	/*@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAssignRoutePO> getAllOnRoadVehicleDetails(EFmFmAssignRoutePO eFmFmAssignRoutePO) {
		List <EFmFmAssignRoutePO> onRoadVehicle = new ArrayList<EFmFmAssignRoutePO>();
		Format formatter;		
		formatter = new SimpleDateFormat("yyyy-MM-dd");		
		Query query=entityManager.createQuery("SELECT b FROM EFmFmAssignRoutePO b where DATE(b.tripShiftTime)=TRIM('"+formatter.format(eFmFmAssignRoutePO.getTripShiftTime())+"')) ");		
		onRoadVehicle=query.getResultList();		
		return onRoadVehicle;
	}*/

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmRouteAreaMappingPO> routeMappaingAlreadyExist(EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO) {
		List<EFmFmRouteAreaMappingPO> routeAreaMappingPO=new ArrayList<EFmFmRouteAreaMappingPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmRouteAreaMappingPO b JOIN b.eFmFmZoneMaster c JOIN b.efmFmAreaMaster d where c.zoneId='"+eFmFmRouteAreaMappingPO.geteFmFmZoneMaster().getZoneId()+"' and d.areaId='"+eFmFmRouteAreaMappingPO.getEfmFmAreaMaster().getAreaId()+"'");
		routeAreaMappingPO=query.getResultList();
		return routeAreaMappingPO;
	}	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmRouteAreaMappingPO> getAllAreasFromZoneId(int zoneId) {
		List<EFmFmRouteAreaMappingPO> routeAreaMappingPO=new ArrayList<EFmFmRouteAreaMappingPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmRouteAreaMappingPO b JOIN b.eFmFmZoneMaster c where c.zoneId='"+zoneId+"' ");
		routeAreaMappingPO=query.getResultList();
		return routeAreaMappingPO;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAreaNodalMasterPO> getAllAreasNodalPointsFromAreaId(int areaId) {
		List<EFmFmAreaNodalMasterPO> routeAreaMappingPO=new ArrayList<EFmFmAreaNodalMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmAreaNodalMasterPO b  JOIN b.efmFmAreaMaster n where n.areaId='"+areaId+"' ");
		routeAreaMappingPO=query.getResultList();
		return routeAreaMappingPO;
	}
	//AreaId and Nodel point name mapping Exist check	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAreaNodalMasterPO> getNodalPointsFromAreaIdAndNodalName(int areaId,String nodalPointName) {
		List<EFmFmAreaNodalMasterPO> routeAreaMappingPO=new ArrayList<EFmFmAreaNodalMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmAreaNodalMasterPO b  JOIN b.efmFmAreaMaster n where n.areaId='"+areaId+"' AND b.nodalPointName='"+nodalPointName+"' ");
		routeAreaMappingPO=query.getResultList();
		return routeAreaMappingPO;
	}
	
	//Nodal name Exist check	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAreaNodalMasterPO> getNodalPointsFromNodalName(String nodalPointName) {
		List<EFmFmAreaNodalMasterPO> routeAreaMappingPO=new ArrayList<EFmFmAreaNodalMasterPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmAreaNodalMasterPO b  where  b.nodalPointName='"+nodalPointName+"' ");
		routeAreaMappingPO=query.getResultList();
		return routeAreaMappingPO;
		}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmZoneMasterPO> getAllZoneNames(int branchId) {
		List<EFmFmZoneMasterPO> zoneDetails=new ArrayList<EFmFmZoneMasterPO>();
		Query query=entityManager.createQuery("SELECT r FROM EFmFmZoneMasterPO r JOIN r.efmFmClientRouteMappings v JOIN v.eFmFmClientBranchPO c   where r.status='Y' AND c.branchId="+branchId+" ");
		zoneDetails=query.getResultList();
		return zoneDetails;
	}
	
	//Get all nodal routes from branch id	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmZoneMasterPO> getAllNodalZoneNames(String branchId) {
		List<EFmFmZoneMasterPO> zoneDetails=new ArrayList<EFmFmZoneMasterPO>();
		Query query=entityManager.createQuery("SELECT r FROM EFmFmZoneMasterPO r JOIN r.efmFmClientRouteMappings v JOIN v.eFmFmClientBranchPO c   where r.status='Y' AND c.branchId in ("+ branchId + ") AND r.isNodalRoute=true");
		zoneDetails=query.getResultList();
		return zoneDetails;
	}
	
	
	
	//Get all areas  from area master TableO efmFmAreaMaster
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmAreaMasterPO> getAllAreaNames() {
		List<EFmFmAreaMasterPO> areaDetails=new ArrayList<EFmFmAreaMasterPO>();
		Query query=entityManager.createQuery("SELECT r FROM EFmFmAreaMasterPO r");
		areaDetails=query.getResultList();
		return areaDetails;
	}
	
	
	//Get all non nodal routes from branch id	
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmZoneMasterPO> getAllNonNodalZoneNames(String branchId) {
			List<EFmFmZoneMasterPO> zoneDetails=new ArrayList<EFmFmZoneMasterPO>();
			Query query=entityManager.createQuery("SELECT r FROM EFmFmZoneMasterPO r JOIN r.efmFmClientRouteMappings v JOIN v.eFmFmClientBranchPO c   where r.status='Y' AND c.branchId in ("+ branchId + ")  AND r.isNodalRoute=false");
			zoneDetails=query.getResultList();
			return zoneDetails;
		}
		
		//Get all non nodal routes from branch Id and Route Name	
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 	public List<EFmFmZoneMasterPO> getNonNodalRouteNameFromClientIdAndRouteName(String branchId,String routeName) {
	    		List<EFmFmZoneMasterPO> zoneDetails=new ArrayList<EFmFmZoneMasterPO>();
				Query query=entityManager.createQuery("SELECT r FROM EFmFmZoneMasterPO r JOIN r.efmFmClientRouteMappings v JOIN v.eFmFmClientBranchPO c   where r.status='Y' AND c.branchId in ("+ branchId + ") AND r.zoneName='"+routeName+"' AND r.isNodalRoute=false");
				zoneDetails=query.getResultList();
				return zoneDetails;
			}	
		
		//Get all  nodal routes from branch Id and Route Name	
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmZoneMasterPO> getNodalRouteNameFromClientIdAndRouteName(String branchId,String routeName) {
		 		List<EFmFmZoneMasterPO> zoneDetails=new ArrayList<EFmFmZoneMasterPO>();
				Query query=entityManager.createQuery("SELECT r FROM EFmFmZoneMasterPO r JOIN r.efmFmClientRouteMappings v JOIN v.eFmFmClientBranchPO c   where r.status='Y' AND c.branchId in ("+ branchId + ") AND r.zoneName='"+routeName+"' AND r.isNodalRoute=true");
				zoneDetails=query.getResultList();
				return zoneDetails;
		}		
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void modifyDriverRecord(EFmFmDriverMasterPO eFmFmDriverMasterPO) {
		entityManager.merge(eFmFmDriverMasterPO);		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveClientRouteMapping(
			EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO) {
		entityManager.persist(eFmFmClientRouteMappingPO);
 	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientRouteMappingPO> clientRouteMappaingAlreadyExist(
			EFmFmClientRouteMappingPO eFmFmClientRouteMappingPO) {
		List<EFmFmClientRouteMappingPO> eFmFmClientRouteMapping=new ArrayList<EFmFmClientRouteMappingPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmClientRouteMappingPO b JOIN b.eFmFmZoneMaster c JOIN b.eFmFmClientBranchPO d where c.zoneId='"+eFmFmClientRouteMappingPO.geteFmFmZoneMaster().getZoneId()+"' and d.branchId in ("+ eFmFmClientRouteMappingPO.getCombinedFacility() + ")");
		eFmFmClientRouteMapping=query.getResultList();
		return eFmFmClientRouteMapping;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientRouteMappingPO> getClientRouteMappaingAlreadyExistCheck(int zoneId,int branchId) {
		List<EFmFmClientRouteMappingPO> eFmFmClientRouteMapping=new ArrayList<EFmFmClientRouteMappingPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmClientRouteMappingPO b JOIN b.eFmFmZoneMaster c JOIN b.eFmFmClientBranchPO d where c.zoneId='"+zoneId+"' and d.branchId ='"+branchId+"' ");
		eFmFmClientRouteMapping=query.getResultList();
		return eFmFmClientRouteMapping;
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientAreaMappingPO> getClientAreaMappaingData(int areaId,int brnachId) {
		List<EFmFmClientAreaMappingPO> eFmFmClientRouteMapping=new ArrayList<EFmFmClientAreaMappingPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmClientAreaMappingPO b JOIN b.eFmFmAreaMaster a JOIN b.eFmFmClientBranchPO c where a.areaId='"+areaId+"' and c.branchId ='"+brnachId+"' ");
		eFmFmClientRouteMapping=query.getResultList();
		return eFmFmClientRouteMapping;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientAreaMappingPO> getClientAreaMappaingDataFromFacilityIds(String branchId) {
		List<EFmFmClientAreaMappingPO> eFmFmClientRouteMapping=new ArrayList<EFmFmClientAreaMappingPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmClientAreaMappingPO b JOIN b.eFmFmAreaMaster a JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") ");
		eFmFmClientRouteMapping=query.getResultList();
		return eFmFmClientRouteMapping;
	}
	
	////Checking Route Name Exist or Not.	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientRouteMappingPO> getParticularRouteDetailByClient(String branchId,String routeName) {
		List<EFmFmClientRouteMappingPO> eFmFmClientRouteMapping=new ArrayList<EFmFmClientRouteMappingPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmClientRouteMappingPO b JOIN b.eFmFmZoneMaster c JOIN b.eFmFmClientBranchPO d where  d.branchId in ("+ branchId + ") AND TRIM(UPPER(REPLACE(c.zoneName,' ','')))=TRIM(UPPER(REPLACE('"+routeName+"',' ','')))");
		eFmFmClientRouteMapping=query.getResultList();
		return eFmFmClientRouteMapping;
	}
	
	//Checking Nodel Point Name Exist or Not.	
	 @Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmAreaNodalMasterPO> getParticularNodalPointNameDetails(String nodalPointName) {
	  List<EFmFmAreaNodalMasterPO> nodalPoinTitle=new ArrayList<EFmFmAreaNodalMasterPO>();
	  Query query =entityManager.createQuery("SELECT b FROM EFmFmAreaNodalMasterPO as b where b.nodalPointName='"+nodalPointName+"'");
	  nodalPoinTitle=query.getResultList();
	  return nodalPoinTitle;
	 }
	 
	//Checking Nodel Point by nodal id.	
	 @Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmAreaNodalMasterPO> getParticularNodalPointNameByNodalId(int nodalPointId) {
	   List<EFmFmAreaNodalMasterPO> nodalPoints=new ArrayList<EFmFmAreaNodalMasterPO>();
	  Query query =entityManager.createQuery("SELECT b FROM EFmFmAreaNodalMasterPO as b where b.nodalPointId='"+nodalPointId+"'");
	  nodalPoints=query.getResultList();
	  return nodalPoints;
	 }
	 
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientRouteMappingPO> getAllRoutesOfParticularClient(String branchId) {
		List<EFmFmClientRouteMappingPO> eFmFmClientRouteMapping=new ArrayList<EFmFmClientRouteMappingPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmClientRouteMappingPO b JOIN b.eFmFmZoneMaster c JOIN b.eFmFmClientBranchPO d where  d.branchId in ("+ branchId + ") ");
		eFmFmClientRouteMapping=query.getResultList();
		return eFmFmClientRouteMapping;
	}
	
	
	//Methods of getting  All  routes by client id	
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmClientRouteMappingPO> getAllRoutesOfParticularClientFromBranchId(String branchId) {
			List<EFmFmClientRouteMappingPO> eFmFmClientRouteMapping=new ArrayList<EFmFmClientRouteMappingPO>();
			Query query=entityManager.createQuery("SELECT b FROM EFmFmClientRouteMappingPO b JOIN b.eFmFmZoneMaster c JOIN b.eFmFmClientBranchPO d where d.branchId in ("+ branchId + ") AND c.isNodalRoute=false");
			eFmFmClientRouteMapping=query.getResultList();
			return eFmFmClientRouteMapping;
		}
	
	//Methods of getting nodel All nodal routes by client id	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmClientRouteMappingPO> getAllNodalRoutesOfParticularClient(int branchId) {
		List<EFmFmClientRouteMappingPO> eFmFmClientRouteMapping=new ArrayList<EFmFmClientRouteMappingPO>();
		Query query=entityManager.createQuery("SELECT b FROM EFmFmClientRouteMappingPO b JOIN b.eFmFmZoneMaster c JOIN b.eFmFmClientBranchPO d where  d.branchId='"+branchId+"'  AND c.isNodalRoute=true");
		eFmFmClientRouteMapping=query.getResultList();
		return eFmFmClientRouteMapping;
	}
	
	//Methods of getting AllNodalPoints ByBranchId	
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmAreaNodalMasterPO> getAllNodalPointsByBranchId(int branchId) {
			List<EFmFmAreaNodalMasterPO> areaNodalMasterPO=new ArrayList<EFmFmAreaNodalMasterPO>();
			Query query=entityManager.createQuery("SELECT b FROM EFmFmAreaNodalMasterPO b ");
			areaNodalMasterPO=query.getResultList();
			return areaNodalMasterPO;
		}
	

	 @Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public EFmFmRouteAreaMappingPO getRouteAreaId(String areaName,int branchId,String zoneName) {
	  List <EFmFmRouteAreaMappingPO> routeAreaMapping = new ArrayList<EFmFmRouteAreaMappingPO>();
	     Query query=entityManager.createQuery("SELECT r FROM EFmFmRouteAreaMappingPO r JOIN r.efmFmAreaMaster a JOIN r.eFmFmZoneMaster m JOIN m.efmFmClientRouteMappings c JOIN c.eFmFmClientBranchPO f where TRIM(UPPER(REPLACE(a.areaName,' ','')))='"+areaName+"' and TRIM(UPPER(REPLACE(m.zoneName,' ','')))='"+zoneName+"' and f.branchId='"+branchId+"'");
	     try {
	      routeAreaMapping=query.getResultList();   
	  } catch (Exception e) {
	  }
	     if(routeAreaMapping.isEmpty()){
	      return null;
	     }else{
	      return routeAreaMapping.get(0); 
	     }
	 }
	 @Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmAssignRoutePO> getVehicleDetailFromVehicleId(int branchId,int checInId) {
	  List <EFmFmAssignRoutePO> vehicleDetail = new ArrayList<EFmFmAssignRoutePO>();
	  Query query=entityManager.createQuery("SELECT b FROM EFmFmAssignRoutePO b JOIN b.efmFmVehicleCheckIn c  JOIN c.efmFmVehicleMaster d JOIN d.efmFmVendorMaster f JOIN f.eFmFmClientBranchPO g where b.tripStatus !='completed' AND g.branchId ='"+branchId+"' AND c.checkInId='"+checInId+"' ");  
	  vehicleDetail=query.getResultList();  
	  return vehicleDetail;
	 }

	 @Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public  List <EFmFmRouteAreaMappingPO> getRouteAreaIdFromAreaNameAndZoneNameForExcelUpload(String areaName,int branchId,String zoneName,String nodalPointName) {
	  List <EFmFmRouteAreaMappingPO> routeAreaMapping = new ArrayList<EFmFmRouteAreaMappingPO>();
	     Query query=entityManager.createQuery("SELECT r FROM EFmFmRouteAreaMappingPO r JOIN r.efmFmAreaMaster a JOIN r.eFmFmZoneMaster m JOIN r.eFmFmNodalAreaMaster nd JOIN m.efmFmClientRouteMappings c JOIN c.eFmFmClientBranchPO f where a.areaName='"+areaName+"' and m.zoneName='"+zoneName+"' and nd.nodalPointName='"+nodalPointName+"' and f.branchId='"+branchId+"'");
	     routeAreaMapping=query.getResultList();
	     return routeAreaMapping; 
	 }
	 
	 
	 @Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public  List <EFmFmRouteAreaMappingPO> getRouteAreaIdFromAreaIdAndZoneIdNodelIdForExcelUpload(int areaId,String branchId,int zoneId,int nodalPointId) {
	  List <EFmFmRouteAreaMappingPO> routeAreaMapping = new ArrayList<EFmFmRouteAreaMappingPO>();
	     Query query=entityManager.createQuery("SELECT r FROM EFmFmRouteAreaMappingPO r JOIN r.efmFmAreaMaster a JOIN r.eFmFmZoneMaster m JOIN r.eFmFmNodalAreaMaster nd JOIN m.efmFmClientRouteMappings c JOIN c.eFmFmClientBranchPO f where a.areaId='"+areaId+"' and m.zoneId='"+zoneId+"' and nd.nodalPointId='"+nodalPointId+"' and f.branchId in ("+ branchId + ")");
	     routeAreaMapping=query.getResultList();
	     return routeAreaMapping; 
	 }
	 
	 
	 @Override
	 @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 public List<EFmFmAreaMasterPO> getParticularAreaNameDetails(String areaName) {
	  List<EFmFmAreaMasterPO> areaMasterPO=new ArrayList<EFmFmAreaMasterPO>();
	  Query query =entityManager.createQuery("SELECT b FROM EFmFmAreaMasterPO as b where b.areaName='"+areaName+"'");
	  areaMasterPO=query.getResultList();
	  return areaMasterPO;
	 }
	 @Override
	  @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	  public  List <EFmFmRouteAreaMappingPO> getRouteAreaIdbyAreaName(String areaName) {
	   List <EFmFmRouteAreaMappingPO> routeAreaMapping = new ArrayList<EFmFmRouteAreaMappingPO>();
	      Query query=entityManager.createQuery("SELECT r FROM EFmFmRouteAreaMappingPO r JOIN r.efmFmAreaMaster a JOIN r.eFmFmZoneMaster m where UPPER(a.areaName)='"+areaName.toUpperCase().trim()+"' and UPPER(m.zoneName)='"+areaName.toUpperCase().trim()+"'");
	      routeAreaMapping=query.getResultList();
	      return routeAreaMapping; 
	  }


	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmDriverMasterPO> getValidUniqueBatchNumber(String badgeNumber) {
		List<EFmFmDriverMasterPO> allDriverDetail = new ArrayList<EFmFmDriverMasterPO>() ;
		Query query = entityManager.createQuery("SELECT b FROM EFmFmDriverMasterPO as b where b.batchNumber ='"+badgeNumber+"'");
		allDriverDetail = query.getResultList();
		return allDriverDetail;
		
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int saveDriverRecordWithValue(EFmFmDriverMasterPO eFmFmDriverMasterPO) {		
		entityManager.persist(eFmFmDriverMasterPO);
		entityManager.flush();
		return eFmFmDriverMasterPO.getDriverId();
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int addAreaName(EFmFmAreaMasterPO eFmFmAreaMasterPO) {
		entityManager.persist(eFmFmAreaMasterPO);
		entityManager.flush();
		return eFmFmAreaMasterPO.getAreaId();
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int addRouteName(EFmFmZoneMasterPO eFmFmZoneMasterPO) {
		entityManager.persist(eFmFmZoneMasterPO);
		entityManager.flush();
		return eFmFmZoneMasterPO.getZoneId();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int saveAreaWithZone(EFmFmRouteAreaMappingPO eFmFmRouteAreaMappingPO) {
		entityManager.persist(eFmFmRouteAreaMappingPO);		
		entityManager.flush();
		return eFmFmRouteAreaMappingPO.getRouteAreaId();
	}	
}