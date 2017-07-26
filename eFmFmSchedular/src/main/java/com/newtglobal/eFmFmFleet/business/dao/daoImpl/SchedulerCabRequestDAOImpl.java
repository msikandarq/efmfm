package com.newtglobal.eFmFmFleet.business.dao.daoImpl;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmDeviceMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmDriverMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeRequestMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmFixedDistanceContractDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;

@Repository("ICabRequestDAO")
public class SchedulerCabRequestDAOImpl {
	private EntityManager entityManager;
	
	public SchedulerCabRequestDAOImpl(EntityManager entityManager){
		setEntityManager(entityManager);
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {		
		entityManager.persist(eFmFmEmployeeTravelRequestPO);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(EFmFmEmployeeRequestMasterPO employeeRequestMasterPO) {		
		entityManager.persist(employeeRequestMasterPO);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTravelRequestPO> getParticularRequestDetail(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
		List <EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = this.entityManager.createQuery("SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r JOIN r.efmFmUserMaster u JOIN u.eFmFmClientBranchPO  c where  r.tripId='" + eFmFmEmployeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getTripId() + "' AND c.branchId='" + eFmFmEmployeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId() + "' AND b.requestType='" + eFmFmEmployeeTravelRequestPO.getRequestType() + "' AND b.isActive='Y' AND b.readFlg='Y' ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
	
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> particularDateRequestForEmployees(Date date,int branchId, int userId, String tripType) {
        List<EFmFmEmployeeTravelRequestPO> employeeRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        String todayDate;
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = formatter.format(date);
        Query query = entityManager.createQuery(
                "SELECT  b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where u.userId='"
                        + userId + "' and b.requestDate='" + todayDate + "' and  b.tripType='" + tripType
                        + "' and c.branchId='" + branchId
                        + "' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> particularDateRequestForEmployeesByShiftTime(Date date,int branchId, int userId, String tripType,Time shiftTime) {
        List<EFmFmEmployeeTravelRequestPO> employeeRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        String todayDate;
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = formatter.format(date);
        Query query = entityManager.createQuery(
                "SELECT  b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where u.userId='"
                        + userId + "' and b.requestDate='" +todayDate + "' and b.shiftTime='"+shiftTime+"' and  b.tripType='" + tripType
                        + "' and c.branchId='" + branchId
                        + "' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
    
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> particularTripIdDetails(Date date,int branchId, int userId, String tripType,int tripId) {
        List<EFmFmEmployeeTravelRequestPO> employeeRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        String todayDate;
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = formatter.format(date);
        Query query = entityManager.createQuery(
                "SELECT  b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where u.userId='"
                        + userId + "' and b.requestDate='" + todayDate + "' and  b.tripType='" + tripType
                        + "' and c.branchId='" + branchId
                        + "' and r.tripId='"+tripId+"' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
    
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<EFmFmEmployeeTravelRequestPO> disableBackDateRequest(Date date,int branchId, int userId) {
        List<EFmFmEmployeeTravelRequestPO> employeeRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        String todayDate;
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = formatter.format(date);
        Query query = entityManager.createQuery(
                "SELECT  b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where u.userId='"
                        + userId + "' and DATE(b.requestDate) < '" + todayDate + "'AND  c.branchId='" + branchId+ "' ");
        employeeRequestPO = query.getResultList();
        return employeeRequestPO;
    }
    
 	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTravelRequestPO> getParticularReadRequestDetail(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
		List <EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = this.entityManager.createQuery("SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.eFmFmEmployeeRequestMaster r JOIN r.efmFmUserMaster u JOIN u.eFmFmClientBranchPO  c where  r.tripId='" + eFmFmEmployeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getTripId() + "' AND c.branchId='" + eFmFmEmployeeTravelRequestPO.geteFmFmEmployeeRequestMaster().getEfmFmUserMaster().geteFmFmClientBranchPO().getBranchId() + "' AND b.requestType='" + eFmFmEmployeeTravelRequestPO.getRequestType() + "' AND (b.readFlg='R' OR b.readFlg='N') ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<EFmFmEmployeeTravelRequestPO> getAllRequestDetailsFromRequestMaster(int branchId) {
		List <EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
        Query query = this.entityManager.createQuery("SELECT b FROM EFmFmEmployeeRequestMasterPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO  c where   c.branchId='" +branchId+ "' AND b.readFlg='Y' AND b.status='Y'  ");
        employeeTravelRequestPO = query.getResultList();
        return employeeTravelRequestPO;
    }
	
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTripDetailPO> getParticularTripAllEmployees(int assignRouteId) {
	        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
	                        + assignRouteId + "' ORDER BY t.pickUpTime ASC");
	        tripemployees = query.getResultList();
	        return tripemployees;
	    }
	    
	    
	    
	  	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmAssignRoutePO> getRouteDetailByRouteId(int assignRouteId) {
			List<EFmFmAssignRoutePO>  eFmFmAssignRoutePO=new ArrayList<EFmFmAssignRoutePO>();
			String query = "SELECT t FROM EFmFmAssignRoutePO t  WHERE t.assignRouteId ='"+assignRouteId+"' " ;						
					Query q = entityManager.createQuery(query);
			eFmFmAssignRoutePO=q.getResultList();					
			return eFmFmAssignRoutePO;
		}
	    
	    
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmEmployeeTripDetailPO> getDropTripAllSortedEmployees(int assignRouteId) {
	        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
	                        + assignRouteId + "' ORDER BY t.dropSequence ASC");
	        tripemployees = query.getResultList();
	        return tripemployees;
	    }
	    	    
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmAssignRoutePO> getAllUnassignedRoutesForParticularShift(int branchId,Time siftTime) {
			List<EFmFmAssignRoutePO> allTrips = new ArrayList<EFmFmAssignRoutePO>();
			try {
				Query query = entityManager.createQuery(
						"SELECT b FROM EFmFmAssignRoutePO b JOIN b.eFmFmClientBranchPO c  JOIN b.eFmFmRouteAreaMapping a JOIN a.eFmFmZoneMaster z where c.branchId='"
								+ branchId
								+ "'AND b.shiftTime='" + siftTime + "' AND b.scheduleReadFlg ='Y' ORDER BY b.plannedDistance DESC");
				allTrips = query.getResultList();
			} catch (Exception e) {
				return allTrips;			    
			}
			return allTrips;
		}
		
	    
	    
	    
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleLargeCapacity(int capacity,long totalTravelTime) {
	        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d where d.capacity>='"
	                        + capacity + "' and d.vehicleNumber not like '%DUMMY%' and b.status='Y' and b.totalTravelTime='"+totalTravelTime+"' and b.checkOutTime is  null order by d.monthlyPendingFixedDistance DESC ");
	        vehicleCheckIn = query.getResultList();
	        return vehicleCheckIn;

	    }
	    
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmVehicleCheckInPO> getAllRunningCheckedInVehiclesForLargeCapacity(int capacity,long totalTravelTime,long endTravelTime) {
	        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d  where d.capacity>='"
	                        + capacity + "' and d.vehicleNumber not like '%DUMMY%' and b.status='N' and b.checkOutTime is null and  (b.totalTravelTime >='"+totalTravelTime+"' and b.totalTravelTime <='"+endTravelTime+"')  and b.checkOutTime is null order by b.numberOfTrips,d.monthlyPendingFixedDistance DESC ");
	        vehicleCheckIn = query.getResultList();
	        return vehicleCheckIn;

	    }
	    
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehiclesForLargeCapacityByTravelTime(int capacity,long totalTravelTime,long endTravelTime) {
	        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d where d.capacity='"
	                        + capacity + "' and d.vehicleNumber not like '%DUMMY%' and b.status='Y' and (b.totalTravelTime >='"+totalTravelTime+"' and b.totalTravelTime <='"+endTravelTime+"') and b.checkOutTime is  null order by d.monthlyPendingFixedDistance DESC ");
	        vehicleCheckIn = query.getResultList();
	        return vehicleCheckIn;

	    }   
	    
	    
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehiclesForSpecificCapacityByTravelTime(int capacity,long totalTravelTime,long endTravelTime) {
	        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d where d.capacity='"
	                        + capacity + "' and d.vehicleNumber not like '%DUMMY%' and b.status='Y' and (b.totalTravelTime >='"+totalTravelTime+"' and b.totalTravelTime <='"+endTravelTime+"') and b.checkOutTime is  null order by d.monthlyPendingFixedDistance DESC ");
	        vehicleCheckIn = query.getResultList();
	        return vehicleCheckIn;

	    }
	    
	    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	    public List<EFmFmVehicleCheckInPO> getAllRunningCheckedInVehiclesForSpecificCapacity(int capacity,long totalTravelTime,long endTravelTime) {
	        List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
	        Query query = entityManager.createQuery(
	                "SELECT b FROM EFmFmVehicleCheckInPO b JOIN b.efmFmVehicleMaster d where d.capacity='"
	                        + capacity + "' and d.vehicleNumber not like '%DUMMY%' and b.status='N' and b.checkOutTime is null and  (b.totalTravelTime >='"+totalTravelTime+"' and b.totalTravelTime <='"+endTravelTime+"')  and b.checkOutTime is null order by b.numberOfTrips,b.totalTravelTime ASC ");
	        vehicleCheckIn = query.getResultList();
	        return vehicleCheckIn;

	    }
	    
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		public List<EFmFmFixedDistanceContractDetailPO> getFixedDistanceDetails(int contrctDetailsId, int branchId) {
			List<EFmFmFixedDistanceContractDetailPO> fixedDistanceContractDetailPO = new ArrayList<EFmFmFixedDistanceContractDetailPO>();
			Query query = entityManager
					.createQuery("SELECT b FROM EFmFmFixedDistanceContractDetailPO b JOIN b.eFmFmVendorContractTypeMaster cn JOIN cn.eFmFmClientBranchPO c where  c.branchId='"+branchId+ "' AND b.distanceContractId='"+contrctDetailsId+"' AND b.status='Y' AND cn.contractStatus='Y'");
		fixedDistanceContractDetailPO = query.getResultList();
			return fixedDistanceContractDetailPO;
		}
		
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		    public List<EFmFmVehicleCheckInPO> getParticularCheckedInVehicleDetails(int checkInId) {
				List<EFmFmVehicleCheckInPO> vehicleCheckIn = new ArrayList<EFmFmVehicleCheckInPO>();
				Query query = entityManager
						.createQuery("SELECT b FROM EFmFmVehicleCheckInPO b where b.checkInId='"
								+ checkInId + "'");
				vehicleCheckIn = query.getResultList();
				return vehicleCheckIn;
		    }
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		    public EFmFmVehicleMasterPO getVehicleDetail(int vehicleId) {
		        List<EFmFmVehicleMasterPO> vehicleDetail = new ArrayList<EFmFmVehicleMasterPO>();
		        Query query = entityManager
		                .createQuery("SELECT b FROM EFmFmVehicleMasterPO as b  where b.vehicleId='" + vehicleId + "'");
		        vehicleDetail = query.getResultList();
		        
		        return vehicleDetail.get(0);
		    }
			@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
			public EFmFmDriverMasterPO getParticularDriverDetail(int driverId) {
				List<EFmFmDriverMasterPO> driverDetail = new ArrayList<EFmFmDriverMasterPO>();
				Query query = entityManager
						.createQuery("SELECT b FROM EFmFmDriverMasterPO as b  where b.driverId='" + driverId + "'");
				driverDetail = query.getResultList();
				return driverDetail.get(0);
			}
			
			
			@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
			public List<EFmFmDeviceMasterPO> getDeviceDetailsFromDeviceId(int deviceId,
					int branchId) {
				List<EFmFmDeviceMasterPO> deviceDetail = new ArrayList<EFmFmDeviceMasterPO>();
				Query query = entityManager
						.createQuery("SELECT b FROM EFmFmDeviceMasterPO b JOIN b.eFmFmClientBranchPO c where b.deviceId='"
								+ deviceId + "' AND c.branchId='" + branchId + "' ");
				deviceDetail = query.getResultList();
				return deviceDetail;
			}

			@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		    public List<EFmFmEmployeeTripDetailPO> getAllRoutePickUpEmployees(int assignRouteId) {
		        List<EFmFmEmployeeTripDetailPO> tripemployees = new ArrayList<EFmFmEmployeeTripDetailPO>();
		        Query query = entityManager.createQuery(
		                "SELECT b FROM EFmFmEmployeeTripDetailPO b JOIN b.eFmFmEmployeeTravelRequest t JOIN b.efmFmAssignRoute d where d.assignRouteId='"
		                        + assignRouteId + "' ORDER BY t.pickUpTime DESC");
		        tripemployees = query.getResultList();
		        return tripemployees;
		    }
			
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		    public void updateVehicleCheckInDetailsWithOutStatus(int vehicleId,double monthlyPendingFixedDistance,String status) {
		        Query query = entityManager
		                .createQuery("UPDATE EFmFmVehicleMasterPO set monthlyPendingFixedDistance='"+monthlyPendingFixedDistance+"',status='"+status+"' where vehicleId='" + vehicleId + "'");
		        query.executeUpdate();

		    }
		    
		   
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
			public List<String> getListOfApprovalPendingRequestForUser(int branchId, int userId,
					String approvoalFlg) {
		    	List<String> listOfDate=new ArrayList<>();		    	
		    	String todayDate;
		        Format formatter;
		        formatter = new SimpleDateFormat("dd-MM-yyyy");		          
		        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		        Query query = entityManager.createQuery(
		                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where c.branchId='"
		                        + branchId + "' AND u.userId='" + userId
		                        + "' AND b.approveStatus='Y' AND b.readFlg in ('R','Y') AND b.reqApprovalStatus='N' group by DATE(b.requestDate) order by DATE(b.requestDate)");
		        employeeTravelRequestPO = query.getResultList();
		        
		        for(EFmFmEmployeeTravelRequestPO  listRequestDates:employeeTravelRequestPO){
		        	listOfDate.add(formatter.format(listRequestDates.getRequestDate()));
		        }
		        return listOfDate;
		     }
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
			public List<String> getListOfApprovalPendingRequestForUserByDate(int branchId, int userId,
					Date requestDate) {
		    	List<String> listOfDate=new ArrayList<>();		    	
		    	String todayDate;
		        Format formatter;
		        formatter = new SimpleDateFormat("dd-MM-yyyy");		          
		        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		        Query query = entityManager.createQuery(
		                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where c.branchId='"
		                        + branchId + "' AND u.userId='" + userId
		                        + "' AND b.approveStatus='Y' AND DATE(b.requestDate)> '"+requestDate+"' AND b.readFlg in ('R','Y') AND b.reqApprovalStatus='N' group by DATE(b.requestDate) order by DATE(b.requestDate)");
		        employeeTravelRequestPO = query.getResultList();
		        
		        for(EFmFmEmployeeTravelRequestPO  listRequestDates:employeeTravelRequestPO){
		        	listOfDate.add(formatter.format(listRequestDates.getRequestDate()));
		        }
		        return listOfDate;
		     }
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
			public List<EFmFmEmployeeTravelRequestPO> getListOfApprovalPendingApprovalForUserByDate(int branchId, int userId,int reqCount) {
		        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		        Query query = entityManager.createQuery(
		                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where c.branchId='"
		                        + branchId + "' AND u.userId='" + userId
		                        + "' AND b.approveStatus ='N' AND b.readFlg ='Y' AND b.reqApprovalStatus='N' group by DATE(b.requestDate) order by DATE(b.requestDate)").setMaxResults(reqCount);
		        employeeTravelRequestPO = query.getResultList();
		        return employeeTravelRequestPO;
		    }
		    
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
			public List<EFmFmEmployeeTravelRequestPO> getListOfApprovalPendingApprovalForUserRequest(int branchId, int userId,
					Date requestDate) {
		        List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestPO = new ArrayList<EFmFmEmployeeTravelRequestPO>();
		        Query query = entityManager.createQuery(
		                "SELECT b FROM EFmFmEmployeeTravelRequestPO b JOIN b.efmFmUserMaster u JOIN u.eFmFmClientBranchPO c where c.branchId='"
		                        + branchId + "' AND u.userId='" + userId
		                        + "' AND b.approveStatus ='N' AND b.readFlg ='Y' AND b.reqApprovalStatus='N' and DATE(b.requestDate)='"+requestDate+"'");
		        employeeTravelRequestPO = query.getResultList();
		        return employeeTravelRequestPO;
		    }
		    
		    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
		    public List<Integer> getAllCapacitiesOfTheVehicles(String branchId) {
		        List<Integer> clientDetail = new ArrayList<Integer>();	        
		        Query query = entityManager.createQuery(
		                "SELECT distinct(b.capacity) FROM EFmFmVehicleCapacityMasterPO b JOIN b.eFmFmClientBranchPO c where c.branchId in ("+ branchId + ") AND b.status='Y'  ORDER BY b.capacity ASC");
		        clientDetail = query.getResultList();
		        return clientDetail;
		    }
		    

		
}
